package notebook.controller.models;

public class Duration {

	Integer seconds = null;
	Integer nanos = null;
	
	public Integer getSeconds() {
		return seconds;
	}
	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}
	public Integer getNanos() {
		return nanos;
	}
	public void setNanos(Integer nanos) {
		this.nanos = nanos;
	}
	@Override
	public String toString() {
		return "Duration [seconds=" + seconds + ", nanos=" + nanos + "]";
	}
}
