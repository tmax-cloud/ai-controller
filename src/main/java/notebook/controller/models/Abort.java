package notebook.controller.models;

public class Abort {

	Integer httpStatus = null;
	Percent percentage = null;
	
	public Integer getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(Integer httpStatus) {
		this.httpStatus = httpStatus;
	}
	public Percent getPercentage() {
		return percentage;
	}
	public void setPercentage(Percent percentage) {
		this.percentage = percentage;
	}
	@Override
	public String toString() {
		return "Abort [httpStatus=" + httpStatus + ", percentage=" + percentage + "]";
	}
}
