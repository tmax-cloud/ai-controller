package notebook.controller.models;

public class Destination {

	String host = null;
	String subset = null;
	PortSelector port = null;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getSubset() {
		return subset;
	}
	public void setSubset(String subset) {
		this.subset = subset;
	}
	public PortSelector getPort() {
		return port;
	}
	public void setPort(PortSelector port) {
		this.port = port;
	}
	@Override
	public String toString() {
		return "Destination [host=" + host + ", subset=" + subset + ", port=" + port + "]";
	}
}
