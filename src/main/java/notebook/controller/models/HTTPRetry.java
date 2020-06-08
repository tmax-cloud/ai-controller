package notebook.controller.models;

public class HTTPRetry {

	Integer attempts = null;
	Duration perTryTimeout = null;
	String retryOn = null;
	BoolValue retryRemoteLocalities = null;
	
	public Integer getAttempts() {
		return attempts;
	}
	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}
	public Duration getPerTryTimeout() {
		return perTryTimeout;
	}
	public void setPerTryTimeout(Duration perTryTimeout) {
		this.perTryTimeout = perTryTimeout;
	}
	public String getRetryOn() {
		return retryOn;
	}
	public void setRetryOn(String retryOn) {
		this.retryOn = retryOn;
	}
	public BoolValue getRetryRemoteLocalities() {
		return retryRemoteLocalities;
	}
	public void setRetryRemoteLocalities(BoolValue retryRemoteLocalities) {
		this.retryRemoteLocalities = retryRemoteLocalities;
	}
	@Override
	public String toString() {
		return "HTTPRetry [attempts=" + attempts + ", perTryTimeout=" + perTryTimeout + ", retryOn=" + retryOn
				+ ", retryRemoteLocalities=" + retryRemoteLocalities + "]";
	}
}
