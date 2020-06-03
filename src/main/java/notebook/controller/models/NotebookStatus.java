package notebook.controller.models;

import java.util.List;
import io.kubernetes.client.openapi.models.V1ContainerState;

public class NotebookStatus {

	private List<NotebookCondition> conditions = null;
	private Integer readyReplicas = null;
	private V1ContainerState containerState = null;
	
	public List<NotebookCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<NotebookCondition> conditions) {
		this.conditions = conditions;
	}
	public Integer getReadyReplicas() {
		return readyReplicas;
	}
	public void setReadyReplicas(Integer readyReplicas) {
		this.readyReplicas = readyReplicas;
	}
	public V1ContainerState getContainerState() {
		return containerState;
	}
	public void setContainerState(V1ContainerState containerState) {
		this.containerState = containerState;
	}
	@Override
	public String toString() {
		return "NotebookStatus [conditions=" + conditions + ", readyReplicas=" + readyReplicas + ", containerState="
				+ containerState + "]";
	}
}
