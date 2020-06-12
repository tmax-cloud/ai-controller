package notebook.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.util.Watch;
import notebook.controller.Constants;
import notebook.controller.models.Notebook;
import notebook.controller.models.NotebookVolumeSpec;

public class Controller extends Thread {
	private ExecutorService executorService;
	private final Watch<Notebook> notebookController;
	private static int latestResourceVersion = 0;
	private Logger logger = Main.logger;

	ApiClient client = null;
	CustomObjectsApi api = null;
	ObjectMapper mapper = new ObjectMapper();
	Gson gson = new GsonBuilder().create();

	Controller(ApiClient client, CustomObjectsApi api, int resourceVersion) throws Exception {
		notebookController = Watch.createWatch(client,
				api.listClusterCustomObjectCall(Constants.NOTEBOOK_RESOURCE_GROUP, Constants.NOTEBOOK_RESOURCE_VERSION,
						Constants.NOTEBOOK_RESOURCE_PLURAL, null, null, null, null, null,
						Integer.toString(resourceVersion), null, Boolean.TRUE, null),
				new TypeToken<Watch.Response<Notebook>>() {
				}.getType());

		latestResourceVersion = resourceVersion;
		this.client = client;
		this.api = api;
		this.executorService = Executors.newCachedThreadPool();
	}

	@Override
	public void run() {
		try {
			notebookController.forEach(response -> {
				try {
					if (Thread.interrupted()) {
						logger.info("Interrupted!");
						notebookController.close();
						executorService.shutdown();
					}
				} catch (Exception e) {
					logger.info(e.getMessage());
				}

				Notebook notebook = response.object;
				String notebookName = notebook.getMetadata().getName();
				String notebookNamespace = notebook.getMetadata().getNamespace();

				if (notebook != null) {
					latestResourceVersion = Integer.parseInt(notebook.getMetadata().getResourceVersion());
					String eventType = response.type.toString();
					logger.info("[Notebook Controller] Event Type : " + eventType);
					logger.info("[Notebook Controller] == Notebook == \n" + notebook.toString());

					switch (eventType) {
					case Constants.EVENT_TYPE_ADDED:
						Runnable runnable = new Runnable() {
							@Override
							public void run() {
								try {
									// Creating Notebook volume (workspace & data)
									if (notebook.getSpec().getVolumeClaim() != null) {
										logger.info("[Notebook Controller] Creating volume for Notebook");
										List<NotebookVolumeSpec> volumeList = notebook.getSpec().getVolumeClaim();
										for (NotebookVolumeSpec volume : volumeList) {
											if (!volummeAlreadyExist(volume.getName(), notebookNamespace)) {
												try {
													K8sApiCaller.createPersistentVolumeClaim(volume, notebook.getMetadata());
													logger.info("[Notebook Controller] Waiting for Notebook volume");
													updateStatus(notebookName, notebookNamespace, Constants.STATUS_WATING_VOLUME_CREATION, "Waiting for volume (" + volume.getName() + ") creation");
												} catch (ApiException e) {
													logger.info(e.getResponseBody());
													updateStatus(notebookName, notebookNamespace, Constants.STATUS_FAILED, "Failed to request volume (" + volume.getName() + ") creation");
													throw e;
												}
											}
										}

										try {
											checkVolumeBinding(volumeList, notebookNamespace);
											logger.info("[Notebook Controller] Notebook volumes are successfully created");
											updateStatus(notebookName, notebookNamespace, Constants.STATUS_VOLUME_CREATED, "All volumes are successfully created");
										} catch (Exception e) {
											logger.info(e.getMessage());
											updateStatus(notebookName, notebookNamespace, Constants.STATUS_FAILED, e.getMessage());
											throw e;
										}
									}

									// Creating service for Notebook server
									try {
										logger.info("[Notebook Controller] Creating Notebook service");
										K8sApiCaller.createService(notebook.getMetadata());
									} catch (ApiException e) {
										logger.info(e.getResponseBody());
										updateStatus(notebookName, notebookNamespace, Constants.STATUS_FAILED, "Failed to request service creation");
										throw e;
									}

									// Creating statefulset for Notebook server
									try {
										logger.info("[Notebook Controller] Creating Notebook statefulset");
										K8sApiCaller.createStatefulSet(notebook.getMetadata(), notebook.getSpec().getTemplate().getSpec());
										updateStatus(notebookName, notebookNamespace, Constants.STATUS_INITIALIZING_POD, "Pod is initializing");
									} catch (ApiException e) {
										logger.info(e.getResponseBody());
										updateStatus(notebookName, notebookNamespace, Constants.STATUS_FAILED, "Failed to request statefulset creation");
										throw e;
									}

									// Creating virtual service for Notebook server
									try {
										logger.info("[Notebook Controller] Creating Notebook virtual service");
										K8sApiCaller.createVirtualService(notebook.getMetadata());
									} catch (ApiException e) {
										logger.info(e.getResponseBody());
										updateStatus(notebookName, notebookNamespace, Constants.STATUS_FAILED, "Failed to request virtual service creation");
										throw e;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						executorService.execute(runnable);
						break;
					case Constants.EVENT_TYPE_MODIFIED:
						// DO nothing
						break;
					case Constants.EVENT_TYPE_DELETED:
						// Do nothing
						break;
					}
				}
			});
		} catch (Exception e) {
			logger.info("[Notebook Controller] Notebook Controller Exception: " + e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.info(sw.toString());
			executorService.shutdown();
		}
	}

	private boolean volummeAlreadyExist(String name, String namespace) throws Exception {
		boolean exist;
		try {
			exist = K8sApiCaller.persistentVolumeClaimAlreadyExist(name, namespace);
		} catch (ApiException e) {
			e.getResponseBody();
			throw e;
		}
		return exist;
	}

	private void checkVolumeBinding(List<NotebookVolumeSpec> volumeList, String namespace) throws Exception {
		String volumeName = null;
		boolean volumeBound = false; // Bound, Pending
		int checkCnt = 0;

		while (!volumeBound == true) {
			if (checkCnt == 20) {
				throw new Exception("Creating volume (" + volumeName + ") is pending");
			}

			for (NotebookVolumeSpec volume : volumeList) {
				try {
					String status = K8sApiCaller.getPersistentVolumeClaimStatus(volume.getName(), namespace);
					if (!status.equals("Bound")) {
						volumeBound = false;
						volumeName = volume.getName();
						checkCnt += 1;
						Thread.sleep(500);
						break;
					}
				} catch (ApiException e) {
					e.getResponseBody();
					throw e;
				}
				volumeBound = true;
			}
		}
	}

	private void checkStatefulsetReady(String name, String namespace) throws Exception {
		boolean ready = false;
		int checkCnt = 0;

		while (!ready) {
			if (checkCnt == 20) {
				throw new Exception("Creating Statefulset for notebook is pending");
			}

			try {
				ready = K8sApiCaller.statefulSetReady(name, namespace);
			} catch (ApiException e) {
				e.getResponseBody();
				throw e;
			}

			Thread.sleep(5000);
		}
	}

	@SuppressWarnings("unchecked")
	private void updateStatus(String name, String namespace, String status, String reason) throws ApiException {
		JsonArray patchStatusArray = new JsonArray();
		JsonObject patchStatus = new JsonObject();
		JsonObject statusObject = new JsonObject();
		patchStatus.addProperty("op", "replace");
		patchStatus.addProperty("path", "/status");
		statusObject.addProperty("status", status);
		statusObject.addProperty("reason", reason);
		patchStatus.add("value", statusObject);
		patchStatusArray.add(patchStatus);

		logger.info("Patch Status Object : " + patchStatusArray);
		/*
		 * [ "op" : "replace", "path" : "/status", "value" : { "status" : "Awaiting" } ]
		 */
		try {
			api.patchNamespacedCustomObjectStatus(Constants.NOTEBOOK_RESOURCE_GROUP,
					Constants.NOTEBOOK_RESOURCE_VERSION, namespace, Constants.NOTEBOOK_RESOURCE_PLURAL, name,
					patchStatusArray);
		} catch (ApiException e) {
			logger.info(e.getResponseBody());
			logger.info("ApiException Code: " + e.getCode());
			throw e;
		}
	}

	public static int getLatestResourceVersion() {
		return latestResourceVersion;
	}
}
