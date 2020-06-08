package notebook.controller.models;

import java.util.List;

public class CorsPolicy {

	List<StringMatch> allowOrigins = null;
	List<String> allowMethods = null;
	List<String> allowHeaders = null;
	List<String> exposeHeaders = null;
	Duration maxAge = null;
	BoolValue allowCredentials = null;
	
	public List<StringMatch> getAllowOrigins() {
		return allowOrigins;
	}
	public void setAllowOrigins(List<StringMatch> allowOrigins) {
		this.allowOrigins = allowOrigins;
	}
	public List<String> getAllowMethods() {
		return allowMethods;
	}
	public void setAllowMethods(List<String> allowMethods) {
		this.allowMethods = allowMethods;
	}
	public List<String> getAllowHeaders() {
		return allowHeaders;
	}
	public void setAllowHeaders(List<String> allowHeaders) {
		this.allowHeaders = allowHeaders;
	}
	public List<String> getExposeHeaders() {
		return exposeHeaders;
	}
	public void setExposeHeaders(List<String> exposeHeaders) {
		this.exposeHeaders = exposeHeaders;
	}
	public Duration getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(Duration maxAge) {
		this.maxAge = maxAge;
	}
	public BoolValue getAllowCredentials() {
		return allowCredentials;
	}
	public void setAllowCredentials(BoolValue allowCredentials) {
		this.allowCredentials = allowCredentials;
	}
	@Override
	public String toString() {
		return "CorsPolicy [allowOrigins=" + allowOrigins + ", allowMethods=" + allowMethods + ", allowHeaders="
				+ allowHeaders + ", exposeHeaders=" + exposeHeaders + ", maxAge=" + maxAge + ", allowCredentials="
				+ allowCredentials + "]";
	}
}
