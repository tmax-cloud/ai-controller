package notebook.controller.models;

public class HTTPRedirect {

	String uri = null;
	String authority = null;
	Integer redirectCode = null;
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public Integer getRedirectCode() {
		return redirectCode;
	}
	public void setRedirectCode(Integer redirectCode) {
		this.redirectCode = redirectCode;
	}
	@Override
	public String toString() {
		return "HTTPRedirect [uri=" + uri + ", authority=" + authority + ", redirectCode=" + redirectCode + "]";
	}
}
