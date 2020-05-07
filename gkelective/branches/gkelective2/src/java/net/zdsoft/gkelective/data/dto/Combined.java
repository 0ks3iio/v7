package net.zdsoft.gkelective.data.dto;

import java.util.List;

public class Combined {
	 private String id; 
	 private String combinedName;
	 private List<String> subjectIds;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(List<String> subjectIds) {
		this.subjectIds = subjectIds;
	}
	public String getCombinedName() {
		return combinedName;
	}
	public void setCombinedName(String combinedName) {
		this.combinedName = combinedName;
	}
	 
	 
}
