package notebook.controller.models;

public class StringMatch {

	String exact = null;
	String prefix = null;
	String regex = null;
	
	public String getExact() {
		return exact;
	}
	public void setExact(String exact) {
		this.exact = exact;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	@Override
	public String toString() {
		return "StringMatch [exact=" + exact + ", prefix=" + prefix + ", regex=" + regex + "]";
	}
}
