package notebook.controller.models;

public class PortSelector {

	Integer number = null;

	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "PortSelector [number=" + number + "]";
	}
}
