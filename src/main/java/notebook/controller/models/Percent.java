package notebook.controller.models;

public class Percent {

	double value = 0;

	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Percent [value=" + value + "]";
	}
}
