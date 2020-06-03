package notebook.controller.models;

import org.joda.time.DateTime;

public class NotebookCondition {

	private String type = null;
	private DateTime lastProbeTime = null;
	private String reason = null;
	private String message = null;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public DateTime getLastProbeTime() {
		return lastProbeTime;
	}
	public void setLastProbeTime(DateTime lastProbeTime) {
		this.lastProbeTime = lastProbeTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "NotebookCondition [type=" + type + ", lastProbeTime=" + lastProbeTime + ", reason=" + reason
				+ ", message=" + message + "]";
	}
}
