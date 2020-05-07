package net.zdsoft.newgkelective.data.dto;

public class ChosenSearchDto {
	
	public static final String SEARCH_NAME_1="1";
	public static final String SEARCH_CODE_2="2";
	private String chosenType;//1:已选0:未选
	private String subjectIds;
	private String gradeId;
	private String classIds;
	private String sex;
	private String unitId;
	private boolean isstuCache=true;
	
	private String searchType;//1：姓名:2：学号
	private String searchTex;
	
	private String subjectType;//1:不调剂；2：可调剂；3：优先调剂
	
	private boolean notShowGradeInClassName;//班级name包不包括年级 false:高一1班 true:1班
	
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getChosenType() {
		return chosenType;
	}
	public void setChosenType(String chosenType) {
		this.chosenType = chosenType;
	}
	public String getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}
	public String getClassIds() {
		return classIds;
	}
	public void setClassIds(String classIds) {
		this.classIds = classIds;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getSearchTex() {
		return searchTex;
	}
	public void setSearchTex(String searchTex) {
		this.searchTex = searchTex;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public boolean isIsstuCache() {
		return isstuCache;
	}
	public void setIsstuCache(boolean isstuCache) {
		this.isstuCache = isstuCache;
	}
	public boolean isNotShowGradeInClassName() {
		return notShowGradeInClassName;
	}
	public void setNotShowGradeInClassName(boolean notShowGradeInClassName) {
		this.notShowGradeInClassName = notShowGradeInClassName;
	}
	
}
