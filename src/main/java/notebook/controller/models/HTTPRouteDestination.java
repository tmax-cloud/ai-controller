package notebook.controller.models;

public class HTTPRouteDestination {

	Destination destination = null;
	Integer weight = null;
	Headers headers = null;
	
	public Destination getDestination() {
		return destination;
	}
	public void setDestination(Destination destination) {
		this.destination = destination;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Headers getHeaders() {
		return headers;
	}
	public void setHeaders(Headers headers) {
		this.headers = headers;
	}
	@Override
	public String toString() {
		return "HTTPRouteDestination [destination=" + destination + ", weight=" + weight + ", headers=" + headers + "]";
	}
}
