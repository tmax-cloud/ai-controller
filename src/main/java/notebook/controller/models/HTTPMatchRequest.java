package notebook.controller.models;

import java.util.List;
import java.util.Map;

public class HTTPMatchRequest {

	String name = null;
	StringMatch uri = null;
	StringMatch scheme = null;
	StringMatch method = null;
	StringMatch authority = null;
	Map<String, StringMatch> headers = null;
	Integer port = null;
	Map<String, String> sourceLabels = null;
	List<String> gateways = null;
	Map<String, StringMatch> queryParams = null;
	Boolean ignoreUriCase = null;
	Map<String, StringMatch> withoutHeaders = null;
	String sourceNamespace = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public StringMatch getUri() {
		return uri;
	}
	public void setUri(StringMatch uri) {
		this.uri = uri;
	}
	public StringMatch getScheme() {
		return scheme;
	}
	public void setScheme(StringMatch scheme) {
		this.scheme = scheme;
	}
	public StringMatch getMethod() {
		return method;
	}
	public void setMethod(StringMatch method) {
		this.method = method;
	}
	public StringMatch getAuthority() {
		return authority;
	}
	public void setAuthority(StringMatch authority) {
		this.authority = authority;
	}
	public Map<String, StringMatch> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, StringMatch> headers) {
		this.headers = headers;
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
	public Map<String, StringMatch> getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(Map<String, StringMatch> queryParams) {
		this.queryParams = queryParams;
	}
	public Boolean getIgnoreUriCase() {
		return ignoreUriCase;
	}
	public void setIgnoreUriCase(Boolean ignoreUriCase) {
		this.ignoreUriCase = ignoreUriCase;
	}
	public Map<String, StringMatch> getWithoutHeaders() {
		return withoutHeaders;
	}
	public void setWithoutHeaders(Map<String, StringMatch> withoutHeaders) {
		this.withoutHeaders = withoutHeaders;
	}
	public String getSourceNamespace() {
		return sourceNamespace;
	}
	public void setSourceNamespace(String sourceNamespace) {
		this.sourceNamespace = sourceNamespace;
	}
	@Override
	public String toString() {
		return "HTTPMatchRequest [name=" + name + ", uri=" + uri + ", scheme=" + scheme + ", method=" + method
				+ ", authority=" + authority + ", headers=" + headers + ", port=" + port + ", sourceLabels="
				+ sourceLabels + ", gateways=" + gateways + ", queryParams=" + queryParams + ", ignoreUriCase="
				+ ignoreUriCase + ", withoutHeaders=" + withoutHeaders + ", sourceNamespace=" + sourceNamespace + "]";
	}
}
