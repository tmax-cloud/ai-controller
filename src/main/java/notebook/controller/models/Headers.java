package notebook.controller.models;

public class Headers {

	HeaderOperations request = null;
	HeaderOperations response = null;
	
	public HeaderOperations getRequest() {
		return request;
	}
	public void setRequest(HeaderOperations request) {
		this.request = request;
	}
	public HeaderOperations getResponse() {
		return response;
	}
	public void setResponse(HeaderOperations response) {
		this.response = response;
	}
	@Override
	public String toString() {
		return "Headers [request=" + request + ", response=" + response + "]";
	}
}
