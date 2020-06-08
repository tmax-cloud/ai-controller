package notebook.controller.models;

public class RouteDestination {

	Destination destination = null;
	Integer weight = null;
	
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
	@Override
	public String toString() {
		return "RouteDestination [destination=" + destination + ", weight=" + weight + "]";
	}
}
