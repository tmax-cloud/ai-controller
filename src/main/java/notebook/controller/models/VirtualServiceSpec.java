package notebook.controller.models;

import java.util.List;

public class VirtualServiceSpec {

	List<String> hosts = null;
	List<String> gateways = null;
	List<HTTPRoute> http = null;
	List<TLSRoute> tls = null;
	List<TCPRoute> tcp = null;
	List<String> exportTo = null;
	
	public List<String> getHosts() {
		return hosts;
	}
	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}
	public List<String> getGateways() {
		return gateways;
	}
	public void setGateways(List<String> gateways) {
		this.gateways = gateways;
	}
	public List<HTTPRoute> getHttp() {
		return http;
	}
	public void setHttp(List<HTTPRoute> http) {
		this.http = http;
	}
	public List<TLSRoute> getTls() {
		return tls;
	}
	public void setTls(List<TLSRoute> tls) {
		this.tls = tls;
	}
	public List<TCPRoute> getTcp() {
		return tcp;
	}
	public void setTcp(List<TCPRoute> tcp) {
		this.tcp = tcp;
	}
	public List<String> getExportTo() {
		return exportTo;
	}
	public void setExportTo(List<String> exportTo) {
		this.exportTo = exportTo;
	}
	@Override
	public String toString() {
		return "VirtualService [hosts=" + hosts + ", gateways=" + gateways + ", http=" + http + ", tls=" + tls
				+ ", tcp=" + tcp + ", exportTo=" + exportTo + "]";
	}
}
