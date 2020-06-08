package notebook.controller.models;

public class UInt32Value {

	Integer value = null;

	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "UInt32Value [value=" + value + "]";
	}
}
