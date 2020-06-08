package notebook.controller.models;

public class HTTPRewrite {

	String uri = null;
	String authority = null;
	
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
	@Override
	public String toString() {
		return "HTTPRewrite [uri=" + uri + ", authority=" + authority + "]";
	}
}
