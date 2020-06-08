package notebook.controller.models;

import java.util.List;
import java.util.Map;

public class HeaderOperations {

	Map<String, String> set = null;
	Map<String, String> add = null;
	List<String> remove = null;
	
	public Map<String, String> getSet() {
		return set;
	}
	public void setSet(Map<String, String> set) {
		this.set = set;
	}
	public Map<String, String> getAdd() {
		return add;
	}
	public void setAdd(Map<String, String> add) {
		this.add = add;
	}
	public List<String> getRemove() {
		return remove;
	}
	public void setRemove(List<String> remove) {
		this.remove = remove;
	}
	@Override
	public String toString() {
		return "HeaderOperations [set=" + set + ", add=" + add + ", remove=" + remove + "]";
	}
}
