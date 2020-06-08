package notebook.controller.models;

public class Delay {

	Duration fixedDelay = null;
	Percent percentage = null;
	Integer percent = null;
	
	public Duration getFixedDelay() {
		return fixedDelay;
	}
	public void setFixedDelay(Duration fixedDelay) {
		this.fixedDelay = fixedDelay;
	}
	public Percent getPercentage() {
		return percentage;
	}
	public void setPercentage(Percent percentage) {
		this.percentage = percentage;
	}
	public Integer getPercent() {
		return percent;
	}
	public void setPercent(Integer percent) {
		this.percent = percent;
	}
	@Override
	public String toString() {
		return "Delay [fixedDelay=" + fixedDelay + ", percentage=" + percentage + ", percent=" + percent + "]";
	}
}
