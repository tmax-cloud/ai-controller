package notebook.controller.models;

public class BoolValue {

	Boolean value = null;

	public Boolean getValue() {
		return value;
	}
	public void setValue(Boolean value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "BoolValue [value=" + value + "]";
	}
}
