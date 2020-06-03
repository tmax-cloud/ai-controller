package notebook.controller.models;

import io.kubernetes.client.openapi.models.V1PodSpec;

public class NotebookTemplateSpec {

	private V1PodSpec spec = null;

	public V1PodSpec getSpec() {
		return spec;
	}

	public void setSpec(V1PodSpec spec) {
		this.spec = spec;
	}

	@Override
	public String toString() {
		return "NotebookTemplateSpec [spec=" + spec + "]";
	}
}
