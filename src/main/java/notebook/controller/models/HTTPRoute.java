package notebook.controller.models;

import java.util.List;

public class HTTPRoute {

	String name = null;
	List<HTTPMatchRequest> match = null;
	List<HTTPRouteDestination> route = null;
	HTTPRedirect redirect = null;
	Delegate delegate = null;
	HTTPRewrite rewrite = null;
	String timeout = null;
	HTTPRetry retries = null;
	HTTPFaultInjection fault = null;
	Destination mirror = null;
	Percent mirrorPercentage = null;
	CorsPolicy corsPolicy = null;
	Headers headers = null;
	UInt32Value mirrorPercent = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<HTTPMatchRequest> getMatch() {
		return match;
	}
	public void setMatch(List<HTTPMatchRequest> match) {
		this.match = match;
	}
	public List<HTTPRouteDestination> getRoute() {
		return route;
	}
	public void setRoute(List<HTTPRouteDestination> route) {
		this.route = route;
	}
	public HTTPRedirect getRedirect() {
		return redirect;
	}
	public void setRedirect(HTTPRedirect redirect) {
		this.redirect = redirect;
	}
	public Delegate getDelegate() {
		return delegate;
	}
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	public HTTPRewrite getRewrite() {
		return rewrite;
	}
	public void setRewrite(HTTPRewrite rewrite) {
		this.rewrite = rewrite;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	public HTTPRetry getRetries() {
		return retries;
	}
	public void setRetries(HTTPRetry retries) {
		this.retries = retries;
	}
	public HTTPFaultInjection getFault() {
		return fault;
	}
	public void setFault(HTTPFaultInjection fault) {
		this.fault = fault;
	}
	public Destination getMirror() {
		return mirror;
	}
	public void setMirror(Destination mirror) {
		this.mirror = mirror;
	}
	public Percent getMirrorPercentage() {
		return mirrorPercentage;
	}
	public void setMirrorPercentage(Percent mirrorPercentage) {
		this.mirrorPercentage = mirrorPercentage;
	}
	public CorsPolicy getCorsPolicy() {
		return corsPolicy;
	}
	public void setCorsPolicy(CorsPolicy corsPolicy) {
		this.corsPolicy = corsPolicy;
	}
	public Headers getHeaders() {
		return headers;
	}
	public void setHeaders(Headers headers) {
		this.headers = headers;
	}
	public UInt32Value getMirrorPercent() {
		return mirrorPercent;
	}
	public void setMirrorPercent(UInt32Value mirrorPercent) {
		this.mirrorPercent = mirrorPercent;
	}
	@Override
	public String toString() {
		return "HTTPRoute [name=" + name + ", match=" + match + ", route=" + route + ", redirect=" + redirect
				+ ", delegate=" + delegate + ", rewrite=" + rewrite + ", timeout=" + timeout + ", retries=" + retries
				+ ", fault=" + fault + ", mirror=" + mirror + ", mirrorPercentage=" + mirrorPercentage + ", corsPolicy="
				+ corsPolicy + ", headers=" + headers + ", mirrorPercent=" + mirrorPercent + "]";
	}
}
