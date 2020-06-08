package notebook.controller.models;

import java.util.List;
import java.util.Map;

public class TLSMatchAttributes {

	List<String> sniHosts = null;
	List<String> destinationSubnets = null;
	Integer port = null;
	Map<String, String> sourceLabels = null;
	List<String> gateways = null;
	String sourceNamespace = null;
	
	public List<String> getSniHosts() {
		return sniHosts;
	}
	public void setSniHosts(List<String> sniHosts) {
		this.sniHosts = sniHosts;
	}
	public List<String> getDestinationSubnets() {
		return destinationSubnets;
	}
	public void setDestinationSubnets(List<String> destinationSubnets) {
		this.destinationSubnets = destinationSubnets;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Map<String, String> getSourceLabels() {
		return sourceLabels;
	}
	public void setSourceLabels(Map<String, String> sourceLabels) {
		this.sourceLabels = sourceLabels;
	}
	public List<String> getGateways() {
		return gateways;
	}
	public void setGateways(List<String> gateways) {
		this.gateways = gateways;
	}
	public String getSourceNamespace() {
		return sourceNamespace;
	}
	public void setSourceNamespace(String sourceNamespace) {
		this.sourceNamespace = sourceNamespace;
	}
	@Override
	public String toString() {
		return "TLSMatchAttributes [sniHosts=" + sniHosts + ", destinationSubnets=" + destinationSubnets + ", port="
				+ port + ", sourceLabels=" + sourceLabels + ", gateways=" + gateways + ", sourceNamespace="
				+ sourceNamespace + "]";
	}
}
