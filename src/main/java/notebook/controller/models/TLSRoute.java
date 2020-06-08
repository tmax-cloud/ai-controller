package notebook.controller.models;

import java.util.List;

public class TLSRoute {

	List<TLSMatchAttributes> match = null;
	List<RouteDestination> route = null;
	
	public List<TLSMatchAttributes> getMatch() {
		return match;
	}
	public void setMatch(List<TLSMatchAttributes> match) {
		this.match = match;
	}
	public List<RouteDestination> getRoute() {
		return route;
	}
	public void setRoute(List<RouteDestination> route) {
		this.route = route;
	}
	@Override
	public String toString() {
		return "TLSRoute [match=" + match + ", route=" + route + "]";
	}
}
