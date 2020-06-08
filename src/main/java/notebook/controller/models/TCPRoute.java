package notebook.controller.models;

import java.util.List;

public class TCPRoute {

	List<L4MatchAttributes> match = null;
	List<RouteDestination> route = null;
	
	public List<L4MatchAttributes> getMatch() {
		return match;
	}
	public void setMatch(List<L4MatchAttributes> match) {
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
		return "TCPRoute [match=" + match + ", route=" + route + "]";
	}
}
