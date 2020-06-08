package notebook.controller;

public class Constants {

	public static final String NOTEBOOK_RESOURCE_GROUP = "kubeflow.org";
	public static final String NOTEBOOK_RESOURCE_VERSION = "v1alpha1";
	public static final String NOTEBOOK_RESOURCE_PLURAL = "notebooks";
	public static final String NOTEBOOK_RESOURCE_KIND = "Notebook";
	public static final String VIRTUAL_SERVICE_RESOURCE_GROUP = "networking.istio.io";
	public static final String VIRTUAL_SERVICE_RESOURCE_VERSION = "v1alpha3";
	public static final String VIRTUAL_SERVICE_RESOURCE_PLURAL = "virtualservices";
	public static final String VIRTUAL_SERVICE_RESOURCE_KIND = "VirtualService";
	
	public static final String EVENT_TYPE_ADDED = "ADDED";
	public static final String EVENT_TYPE_MODIFIED = "MODIFIED";
	public static final String EVENT_TYPE_DELETED = "DELETED";
	
	public static final String STS_LABEL_KEY = "statefulset";
	public static final String PVC_ACCESS_MODE = "ReadWriteOnce";
	public static final String SVC_TYPE_CLUSTER_IP = "ClusterIP";
	public static final String SVC_PORT_PREFIX_HTTP = "http-";
	public static final String KUBEFLOW_GATEWAY = "kubeflow/kubeflow-gateway";
	
	public static final String STATUS_FAILED = "Failed";
	public static final String STATUS_WATING = "Waiting";
	
}
