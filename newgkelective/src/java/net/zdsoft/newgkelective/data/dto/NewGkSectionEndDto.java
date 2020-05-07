package net.zdsoft.newgkelective.data.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewGkSectionEndDto {
	
	private String itemId; //对应对象id
	
	private String subjectIds;//2科组合
	private String subjectNames;//2科组合名称
	
	//result
	private String groupName;//结果名称
	private String resultSubs;//结果开设的2科或者3科subIds
	private String resultSubNames;//结果开设的2科或者3科subNames
	private String subjectType;
	private String arrangeType;//算法前 算法后
	
	//end
	private int allStudengNum;
	private int openClassNum;//算法开设班级数量
	private List<String[]> clazzList;//已经开设的班级 
	
	
	private Map<String,Integer> subject3Map=new HashMap<>();
	public String getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}
	public String getSubjectNames() {
		return subjectNames;
	}
	public void setSubjectNames(String subjectNames) {
		this.subjectNames = subjectNames;
	}
	public int getOpenClassNum() {
		return openClassNum;
	}
	public void setOpenClassNum(int openClassNum) {
		this.openClassNum = openClassNum;
	}
	public Map<String, Integer> getSubject3Map() {
		return subject3Map;
	}
	public void setSubject3Map(Map<String, Integer> subject3Map) {
		this.subject3Map = subject3Map;
	}
	public int getAllStudengNum() {
		return allStudengNum;
	}
	public void setAllStudengNum(int allStudengNum) {
		this.allStudengNum = allStudengNum;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getResultSubs() {
		return resultSubs;
	}
	public void setResultSubs(String resultSubs) {
		this.resultSubs = resultSubs;
	}
	public String getResultSubNames() {
		return resultSubNames;
	}
	public void setResultSubNames(String resultSubNames) {
		this.resultSubNames = resultSubNames;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getArrangeType() {
		return arrangeType;
	}
	public void setArrangeType(String arrangeType) {
		this.arrangeType = arrangeType;
	}
	public List<String[]> getClazzList() {
		return clazzList;
	}
	public void setClazzList(List<String[]> clazzList) {
		this.clazzList = clazzList;
	}

	
}
