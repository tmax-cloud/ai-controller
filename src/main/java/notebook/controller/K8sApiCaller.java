package notebook.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ContainerPort;
import io.kubernetes.client.openapi.models.V1EnvVar;
import io.kubernetes.client.openapi.models.V1LabelSelector;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1OwnerReference;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaim;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaimSpec;
import io.kubernetes.client.openapi.models.V1PodSecurityContext;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.openapi.models.V1PodTemplateSpec;
import io.kubernetes.client.openapi.models.V1ResourceRequirements;
import io.kubernetes.client.openapi.models.V1RollingUpdateStatefulSetStrategy;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServicePort;
import io.kubernetes.client.openapi.models.V1ServiceSpec;
import io.kubernetes.client.openapi.models.V1StatefulSet;
import io.kubernetes.client.openapi.models.V1StatefulSetSpec;
import io.kubernetes.client.openapi.models.V1StatefulSetUpdateStrategy;
import io.kubernetes.client.util.Config;
import notebook.controller.Constants;
import notebook.controller.Controller;
import notebook.controller.Main;
import notebook.controller.models.Notebook;
import notebook.controller.models.NotebookVolumeSpec;

public class K8sApiCaller {

	private static ApiClient k8sClient;
	private static CoreV1Api api;
	private static AppsV1Api appApi;
	private static CustomObjectsApi customObjectApi;
	private static ObjectMapper mapper = new ObjectMapper();
	private static Gson gson = new GsonBuilder().create();

    private static Logger logger = Main.logger;
    
	public static void initK8SClient() throws Exception {
		k8sClient = Config.fromCluster();
		k8sClient.setConnectTimeout(0);
		k8sClient.setReadTimeout(0);
		k8sClient.setWriteTimeout(0);		
		Configuration.setDefaultApiClient(k8sClient);

		api = new CoreV1Api();
		appApi = new AppsV1Api();
		customObjectApi = new CustomObjectsApi();
	}

	public static void startWatcher() throws Exception {    	
		// Get latest resource version
		logger.info("Get latest resource version");

		int notebookLatestResourceVersion = 0;
		try {
			Object response = customObjectApi.listClusterCustomObject(
					Constants.NOTEBOOK_RESOURCE_GROUP, 
					Constants.NOTEBOOK_RESOURCE_VERSION, 
					Constants.NOTEBOOK_RESOURCE_PLURAL, 
					null, null, null, null, null, null, null, Boolean.FALSE);
			JsonObject respJson = (JsonObject) new JsonParser().parse((new Gson()).toJson(response));
			
			mapper.registerModule(new JodaModule());
			ArrayList<Notebook> notebookList = mapper.readValue((new Gson()).toJson(respJson.get("items")), new TypeReference<ArrayList<Notebook>>() {});

			for(Notebook notebook : notebookList) {
				int notebookResourceVersion = Integer.parseInt(notebook.getMetadata().getResourceVersion());
				notebookLatestResourceVersion = (notebookLatestResourceVersion >= notebookResourceVersion) ? notebookLatestResourceVersion : notebookResourceVersion;
			}
		} catch (ApiException e) {
        	logger.info("Response body: " + e.getResponseBody());
        	e.printStackTrace();
        	throw e;
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		logger.info("Notebook Latest resource version: " + notebookLatestResourceVersion);

		// Start Controller
		logger.info("Start Notebook Controller");
		Controller controller = new Controller(k8sClient, customObjectApi, notebookLatestResourceVersion);
		controller.start();

		while(true) {			
			if(!controller.isAlive()) {
				notebookLatestResourceVersion = Controller.getLatestResourceVersion();
				logger.info(("Notebook Controller is not Alive. Restart Operator! (Latest Resource Version: " + notebookLatestResourceVersion + ")"));
				controller.interrupt();
				controller = new Controller(k8sClient, customObjectApi, notebookLatestResourceVersion);
				controller.start();
			}

			Thread.sleep(10000); // Period: 10 sec
    	}
    }
	
	public static void createPersistentVolumeClaim(NotebookVolumeSpec volumeSpec, V1ObjectMeta notebookMeta) throws Exception {
		V1PersistentVolumeClaim pvc = new V1PersistentVolumeClaim();
		V1ObjectMeta pvcMeta = new V1ObjectMeta();
		V1PersistentVolumeClaimSpec pvcSpec = new V1PersistentVolumeClaimSpec();
		V1ResourceRequirements pvcResource = new V1ResourceRequirements();
		Map<String, Quantity> limit = new HashMap<>();
		List<String> accessModes = new ArrayList<>();
		
		pvcMeta.setName(volumeSpec.getName());
		pvcMeta.setNamespace(notebookMeta.getNamespace());
		
		limit.put("storage", new Quantity(volumeSpec.getSize()));
		pvcResource.setRequests(limit);
		pvcSpec.setResources(pvcResource);
		accessModes.add(Constants.PVC_ACCESS_MODE);
		pvcSpec.setAccessModes(accessModes);

		pvc.setMetadata(pvcMeta);
		pvc.setSpec(pvcSpec);

		try {
			api.createNamespacedPersistentVolumeClaim(notebookMeta.getNamespace(), pvc, null, null, null);
		} catch (ApiException e) {
			logger.info(e.getResponseBody());
			throw e;
		}
	}
	
	public static String getPersistentVolumeClaimStatus(String name, String namespace) throws ApiException {
		try {
			V1PersistentVolumeClaim pvc = api.readNamespacedPersistentVolumeClaim(name, namespace, null, null, null);
			return pvc.getStatus().getPhase();
		} catch (ApiException e) {
			logger.info(e.getResponseBody());
			throw e;
		}
	}
	
	public static void createService(V1ObjectMeta notebookMeta) throws ApiException {
		V1Service svc = new V1Service();
		V1ObjectMeta svcMeta = new V1ObjectMeta();
		V1ServiceSpec svcSpec = new V1ServiceSpec();
		List<V1ServicePort> svcPorts = new ArrayList<>();
		V1ServicePort svcPort = new V1ServicePort();
		
		svcMeta.setName(notebookMeta.getName());
		svcMeta.setNamespace(notebookMeta.getNamespace());
		svcMeta.setOwnerReferences(getOwnerReferences(notebookMeta));
		
		Map<String, String> selector = new HashMap<>();
		selector.put(Constants.STS_LABEL_KEY, notebookMeta.getName());
		svcSpec.setSelector(selector);
		svcSpec.setType(Constants.SVC_TYPE_CLUSTER_IP);
		
		svcPort.setName(Constants.SVC_PORT_PREFIX_HTTP + notebookMeta.getName());
		svcPort.setPort(80);
		svcPort.setTargetPort(new IntOrString(8888));
		svcPort.setProtocol("TCP");
		svcPorts.add(svcPort);
		svcSpec.setPorts(svcPorts);
		
		svc.setMetadata(svcMeta);
		svc.setSpec(svcSpec);
		
		try {
			api.createNamespacedService(notebookMeta.getNamespace(), svc, null, null, null);
		} catch (ApiException e) {
			logger.info(e.getResponseBody());
			throw e;
		}
	}
	
	public static void createStatefulSet(V1ObjectMeta notebookMeta, V1PodSpec spec) throws Exception {
		V1StatefulSet sts = new V1StatefulSet();
		V1ObjectMeta stsMeta = new V1ObjectMeta();
		V1StatefulSetSpec stsSpec = new V1StatefulSetSpec();
		V1LabelSelector selector = new V1LabelSelector();
		Map<String, String> matchLabels = new HashMap<>();
		V1PodTemplateSpec template = new V1PodTemplateSpec();
		V1ObjectMeta templateMeta = new V1ObjectMeta();
		Map<String, String> templateLabels = new HashMap<>();
		V1EnvVar env = new V1EnvVar();
		List<V1ContainerPort> contPortList = new ArrayList<>();
		V1ContainerPort port = new V1ContainerPort();
		V1PodSecurityContext securityContext = new V1PodSecurityContext();
		V1StatefulSetUpdateStrategy stsUpdateStrategy = new V1StatefulSetUpdateStrategy();
		V1RollingUpdateStatefulSetStrategy stsRollingUpdate = new V1RollingUpdateStatefulSetStrategy();
		
		stsMeta.setName(notebookMeta.getName());
		stsMeta.setNamespace(notebookMeta.getNamespace());
		stsMeta.setOwnerReferences(getOwnerReferences(notebookMeta));
		
		matchLabels.put(Constants.STS_LABEL_KEY, notebookMeta.getName());
		selector.setMatchLabels(matchLabels);
		stsSpec.setSelector(selector);
		stsSpec.setServiceName("");
		
		templateLabels.put("app", notebookMeta.getName());
		templateLabels.put("notebook-name", notebookMeta.getName());
		templateLabels.put(Constants.STS_LABEL_KEY, notebookMeta.getName());
		templateMeta.setLabels(templateLabels);
		template.setMetadata(templateMeta);
		
		V1Container container = spec.getContainers().get(0);
		env.setName("NB_PREFIX");
		env.setValue("/notebook/" + notebookMeta.getNamespace() + "/" + notebookMeta.getName());
		container.addEnvItem(env);
		container.setImagePullPolicy("IfNotPresent");
		port.setContainerPort(8888);
		port.setName("notebook-port");
		port.setProtocol("TCP");
		contPortList.add(port);
		container.setPorts(contPortList);
		container.setTerminationMessagePath("/dev/termination-log");
		container.setTerminationMessagePolicy("File");
		container.setWorkingDir("/home/jovyan");
		
		securityContext.setFsGroup(Long.valueOf(100));
		spec.setSecurityContext(securityContext);
		template.setSpec(spec);
		stsSpec.setTemplate(template);
		
		stsRollingUpdate.setPartition(0);
		stsUpdateStrategy.setRollingUpdate(stsRollingUpdate);
		stsSpec.setUpdateStrategy(stsUpdateStrategy);
		
		sts.setMetadata(stsMeta);
		sts.setSpec(stsSpec);
		
		try {
			appApi.createNamespacedStatefulSet(notebookMeta.getNamespace(), sts, null, null, null);
		} catch (ApiException e) {
			logger.info(e.getResponseBody());
			throw e;
		}
	}
	
	private static List<V1OwnerReference> getOwnerReferences(V1ObjectMeta notebookMeta) {
		List<V1OwnerReference> ownerList = new ArrayList<V1OwnerReference>();
		V1OwnerReference owner = new V1OwnerReference();
		owner.setApiVersion(Constants.NOTEBOOK_RESOURCE_GROUP + "/" + Constants.NOTEBOOK_RESOURCE_VERSION);
		owner.setKind(Constants.NOTEBOOK_RESOURCE_KIND);
		owner.setName(notebookMeta.getName());
		owner.setUid(notebookMeta.getUid());
		owner.setBlockOwnerDeletion(true);
		owner.setController(true);
		ownerList.add(owner);
		
		return ownerList;
	}
}
