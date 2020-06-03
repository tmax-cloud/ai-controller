package notebook.controller.models;

import io.kubernetes.client.openapi.models.V1ObjectMeta;

public class Notebook {

	private V1ObjectMeta metadata = null;
	private NotebookSpec spec = null;
	private NotebookStatus status = null;
	
	public V1ObjectMeta getMetadata() {
		return metadata;
	}
	public void setMetadata(V1ObjectMeta metadata) {
		this.metadata = metadata;
	}
	public NotebookSpec getSpec() {
		return spec;
	}
	public void setSpec(NotebookSpec spec) {
		this.spec = spec;
	}
	public NotebookStatus getStatus() {
		return status;
	}
	public void setStatus(NotebookStatus status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Notebook [metadata=" + metadata + ", spec=" + spec + ", status=" + status + "]";
	}
}
