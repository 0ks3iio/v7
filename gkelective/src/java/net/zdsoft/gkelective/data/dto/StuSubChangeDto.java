package net.zdsoft.gkelective.data.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StuSubChangeDto {
	
	private String groupClassId;
	private String groupClassName;
	private Integer groupClassStuNum;
	private String classIds;
	private Set<String> classIdsSet = new HashSet<String>();
	private List<String> classNames = new ArrayList<String>();
	
	public String getGroupClassName() {
		return groupClassName;
	}
	public void setGroupClassName(String groupClassName) {
		this.groupClassName = groupClassName;
	}
	public Integer getGroupClassStuNum() {
		return groupClassStuNum;
	}
	public void setGroupClassStuNum(Integer groupClassStuNum) {
		this.groupClassStuNum = groupClassStuNum;
	}
	public String getGroupClassId() {
		return groupClassId;
	}
	public void setGroupClassId(String groupClassId) {
		this.groupClassId = groupClassId;
	}
	public Set<String> getClassIdsSet() {
		return classIdsSet;
	}
	public void setClassIdsSet(Set<String> classIdsSet) {
		this.classIdsSet = classIdsSet;
	}
	public String getClassIds() {
		return classIds;
	}
	public void setClassIds(String classIds) {
		this.classIds = classIds;
	}
	public List<String> getClassNames() {
		return classNames;
	}
	public void setClassNames(List<String> classNames) {
		this.classNames = classNames;
	}
	
}
