package notebook.controller.models;

public class Delegate {

	String name = null;
	String namespace = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	@Override
	public String toString() {
		return "Delegate [name=" + name + ", namespace=" + namespace + "]";
	}
}
