package notebook.controller;

import java.io.IOException;
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
	private final Watch<Notebook> notebookController;
	private static int latestResourceVersion = 0;
    private Logger logger = Main.logger;
	
	ApiClient client = null;
	CustomObjectsApi api = null;
	ObjectMapper mapper = new ObjectMapper();
	Gson gson = new GsonBuilder().create();

	Controller(ApiClient client, CustomObjectsApi api, int resourceVersion) throws Exception {
		notebookController = Watch.createWatch(
		        client,
		        api.listClusterCustomObjectCall(Constants.NOTEBOOK_RESOURCE_GROUP, Constants.NOTEBOOK_RESOURCE_VERSION, Constants.NOTEBOOK_RESOURCE_PLURAL, null, null, null, null, null, Integer.toString(resourceVersion), null, Boolean.TRUE, null),
		        new TypeToken<Watch.Response<Notebook>>(){}.getType()
        );
		
		latestResourceVersion = resourceVersion;
		this.client = client;
		this.api = api;
	}
	
	@Override
	public void run() {
		try {
			notebookController.forEach(response -> {
				try {
					if(Thread.interrupted()) {
						logger.info("Interrupted!");
						notebookController.close();
					}
				} catch(Exception e) {
					logger.info(e.getMessage());
				}
				
				String notebookName = "unknown";
				String notebookNamespace = "unknown";
				try {
					Notebook notebook = response.object;
					notebookName = notebook.getMetadata().getName();
					notebookNamespace = notebook.getMetadata().getNamespace();
					
					if(notebook != null) {
						latestResourceVersion = Integer.parseInt(notebook.getMetadata().getResourceVersion());
						String eventType = response.type.toString();
						logger.info("[Notebook Controller] Event Type : " + eventType);
						logger.info("[Notebook Controller] == Notebook == \n" + notebook.toString());
						
						switch(eventType) {
							case Constants.EVENT_TYPE_ADDED:
								
								// Creating Notebook volume (workspace & data)
								if(notebook.getSpec().getVolumeClaim() != null ) {
									replaceStatus(notebookName, Constants.STATUS_WATING, "Waiting for volume binding");
									List<NotebookVolumeSpec> volumeList = notebook.getSpec().getVolumeClaim();
									for(NotebookVolumeSpec volume : volumeList) {
										try {
											K8sApiCaller.createPersistentVolumeClaim(volume, notebook.getMetadata());
										} catch (ApiException e) {
											logger.info(e.getResponseBody());
											replaceStatus(notebookName, Constants.STATUS_FAILED, "Faild to request volume creation");
											throw e;
										}
									}
									
									int checkCnt = 0;
									while(!checkVolumeBinding(volumeList, notebookNamespace)) {
										if(checkCnt == 10) {
											replaceStatus(notebookName, Constants.STATUS_FAILED, "Volume binding is pending");
											throw new Exception("Volume binding is pending");
										}
										Thread.sleep(500);
										checkCnt += 1;
									}
									replaceStatus(notebookName, Constants.STATUS_WATING, "Initializing pod");
								}
								
								// Creating service for Notebook server
								try {
									K8sApiCaller.createService(notebook.getMetadata());
								} catch (ApiException e) {
									logger.info(e.getResponseBody());
									replaceStatus(notebookName, Constants.STATUS_FAILED, "Faild to request service creation");
									throw e;
								}
								
								// Creating statefulset for Notebook server
								try {
									K8sApiCaller.createStatefulSet(notebook.getMetadata(), notebook.getSpec().getTemplate().getSpec());
								} catch (ApiException e) {
									logger.info(e.getResponseBody());
									replaceStatus(notebookName, Constants.STATUS_FAILED, "Faild to request statefulset creation");
									throw e;
								}
								
								break;
							case Constants.EVENT_TYPE_MODIFIED:
								break;
							case Constants.EVENT_TYPE_DELETED:
								break;
						}
					}
				} catch(Exception e) {
					logger.info(e.getMessage());
				}
        	});
		} catch (Exception e) {
			logger.info("[Notebook Controller] Notebook Controller Exception: " + e.getMessage());
		}
	}
	
	private boolean checkVolumeBinding(List<NotebookVolumeSpec> volumeList, String namespace) throws Exception {
		boolean volumeBound = false; // Bound, Pending
		for(NotebookVolumeSpec volume : volumeList) {
			try {
				String status = K8sApiCaller.getPersistentVolumeClaimStatus(volume.getName(), namespace);
				if(!status.equals("Bound")) {
					break;
				}
				volumeBound = true;
			} catch (ApiException e) {
				e.getResponseBody();
				throw e;
			}
		}
		return volumeBound;
	}
	
	@SuppressWarnings("unchecked")
    private void replaceStatus( String name, String status, String reason ) throws ApiException {
        JsonArray patchStatusArray = new JsonArray();
        JsonObject patchStatus = new JsonObject();
        JsonObject statusObject = new JsonObject();
        patchStatus.addProperty("op", "replace");
        patchStatus.addProperty("path", "/status");
        statusObject.addProperty( "status", status );
        statusObject.addProperty( "reason", reason );
        patchStatus.add("value", statusObject);
        patchStatusArray.add( patchStatus );
        
        logger.info( "Patch Status Object : " + patchStatusArray );
        /*[
          "op" : "replace",
          "path" : "/status",
          "value" : {
            "status" : "Awaiting"
          }
        ]*/
        try {
            api.patchClusterCustomObjectStatus(
                    Constants.NOTEBOOK_RESOURCE_GROUP, 
                    Constants.NOTEBOOK_RESOURCE_VERSION, 
                    Constants.NOTEBOOK_RESOURCE_PLURAL, 
                    name, 
                    patchStatusArray );
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
