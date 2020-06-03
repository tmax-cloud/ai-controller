package notebook.controller.models;

public class NotebookSpec {

	private NotebookTemplateSpec template = null;

	public NotebookTemplateSpec getTemplate() {
		return template;
	}

	public void setTemplate(NotebookTemplateSpec template) {
		this.template = template;
	}

	@Override
	public String toString() {
		return "NotebookSpec [template=" + template + "]";
	}
}
