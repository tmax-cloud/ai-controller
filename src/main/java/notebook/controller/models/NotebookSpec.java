package notebook.controller.models;

import java.util.List;

public class NotebookSpec {
	
	private List<NotebookVolumeSpec> volumeClaim = null; 
	private NotebookTemplateSpec template = null;

	public List<NotebookVolumeSpec> getVolumeClaim() {
		return volumeClaim;
	}

	public void setVolumeClaim(List<NotebookVolumeSpec> volumeClaim) {
		this.volumeClaim = volumeClaim;
	}

	public NotebookTemplateSpec getTemplate() {
		return template;
	}

	public void setTemplate(NotebookTemplateSpec template) {
		this.template = template;
	}

	@Override
	public String toString() {
		return "NotebookSpec [volumeClaim=" + volumeClaim + ", template=" + template + "]";
	}
}
