package notebook.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.openapi.models.V1StatefulSet;
import io.kubernetes.client.openapi.models.V1StatefulSetStatus;
import io.kubernetes.client.util.Watch;
import notebook.controller.Constants;
import notebook.controller.models.Notebook;

public class Reconciler extends Thread {
	private ExecutorService executorService;
	private final Watch<V1StatefulSet> notebookReconciler;
	private static int latestResourceVersion = 0;
	private Logger logger = Main.logger;

	ApiClient client = null;
	CustomObjectsApi customApi = null;
	AppsV1Api appApi = null;
	ObjectMapper mapper = new ObjectMapper();
	Gson gson = new GsonBuilder().create();

	Reconciler(ApiClient client, CustomObjectsApi customApi, AppsV1Api appApi, int resourceVersion) throws Exception {
		notebookReconciler = Watch.createWatch(client,
				appApi.listStatefulSetForAllNamespacesCall(null, null, null, "app=notebook", null, null, Integer.toString(resourceVersion), null, Boolean.TRUE, null),
				new TypeToken<Watch.Response<Notebook>>() {
				}.getType());

		latestResourceVersion = resourceVersion;
		this.client = client;
		this.customApi = customApi;
		this.appApi = appApi;
		this.executorService = Executors.newCachedThreadPool();
	}

	@Override
	public void run() {
		try {
			notebookReconciler.forEach(response -> {
				try {
					if (Thread.interrupted()) {
						logger.info("Interrupted!");
						notebookReconciler.close();
						executorService.shutdown();
					}
				} catch (Exception e) {
					logger.info(e.getMessage());
				}

				V1StatefulSet sts = response.object;
				V1StatefulSetStatus stsStat = sts.getStatus();
				String stsName = sts.getMetadata().getName();
				String stsNamespace = sts.getMetadata().getNamespace();

				if (sts != null) {
					latestResourceVersion = Integer.parseInt(sts.getMetadata().getResourceVersion());
					String eventType = response.type.toString();
					logger.info("[Reconciler] Event Type : " + eventType);
					logger.info("[Reconciler] StatefulSet Name : " + stsName);
					logger.info("[Reconciler] StatefulSet Namespace : " + stsNamespace);
					logger.info("[Reconciler]  == Status == \n  " + stsStat.toString());

					switch (eventType) {
					case Constants.EVENT_TYPE_ADDED:
						// Do nothing
						break;
					case Constants.EVENT_TYPE_MODIFIED:
						Runnable runnable = new Runnable() {
							@Override
							public void run() {
								try {
									int replicas = stsStat.getReplicas();
									int readyReplicas = stsStat.getReadyReplicas();
									
									if(replicas == readyReplicas) {
										logger.info("[Reconciler] Replicas : " + replicas);
										logger.info("[Reconciler] Ready Replicas : " + readyReplicas);
										logger.info("[Reconciler] Ready Replicas : " + readyReplicas);
										logger.info("[Reconciler] All replicas are ready");
										updateStatus(stsName, stsNamespace, Constants.STATUS_RUNNING, "All replicas are ready");
									} else {
										logger.info("[Reconciler] Replicas : " + replicas);
										logger.info("[Reconciler] Ready Replicas : " + readyReplicas);
										logger.info("[Reconciler] Ready Replicas : " + readyReplicas);
										logger.info("[Reconciler] Some replicas may not ready");
										updateStatus(stsName, stsNamespace, Constants.STATUS_INITIALIZING_POD, "Pod is initializing");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						executorService.execute(runnable);
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
			customApi.patchNamespacedCustomObjectStatus(Constants.NOTEBOOK_RESOURCE_GROUP,
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
