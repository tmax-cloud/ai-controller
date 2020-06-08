package notebook.controller.models;

public class HTTPFaultInjection {

	Delay delay = null;
	Abort abort = null;
	
	public Delay getDelay() {
		return delay;
	}
	public void setDelay(Delay delay) {
		this.delay = delay;
	}
	public Abort getAbort() {
		return abort;
	}
	public void setAbort(Abort abort) {
		this.abort = abort;
	}
	@Override
	public String toString() {
		return "HTTPFaultInjection [delay=" + delay + ", abort=" + abort + "]";
	}
}
