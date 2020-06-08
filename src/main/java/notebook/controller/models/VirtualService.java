package notebook.controller.models;

import io.kubernetes.client.openapi.models.V1ObjectMeta;

public class VirtualService {
	String kind = null;
	String apiVersion = null;
	V1ObjectMeta metadata = null;
	VirtualServiceSpec spec = null;
	
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	public V1ObjectMeta getMetadata() {
		return metadata;
	}
	public void setMetadata(V1ObjectMeta metadata) {
		this.metadata = metadata;
	}
	public VirtualServiceSpec getSpec() {
		return spec;
	}
	public void setSpec(VirtualServiceSpec spec) {
		this.spec = spec;
	}
	@Override
	public String toString() {
		return "VirtualService [kind=" + kind + ", apiVersion=" + apiVersion + ", metadata=" + metadata + ", spec="
				+ spec + "]";
	}
}
