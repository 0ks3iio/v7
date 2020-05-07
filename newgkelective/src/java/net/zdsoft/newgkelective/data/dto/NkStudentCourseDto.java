package net.zdsoft.newgkelective.data.dto;

import java.util.List;
import java.util.Map;

import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;

public class NkStudentCourseDto {
	
	private String xzbId;//行政班Id
	private String xzbName;
	
	private List<NewGkDivideClass> searchXzbList;//页面下拉框
	
	//key:classId key:subjectId 所有行政班数据节点
	private Map<String,Map<String,List<NewGkTimetableOther>>> xzbSubjectTime;
	
	private List<NewGkDivideClass> jxbList;//学生对应的
	//key:subjectId_subjectType
	private Map<String,List<NewGkDivideClass>> searchJxbList;//页面下拉框
	
	
	//key:classId key:subjectId_type 所有教学班数据节点
	private Map<String,Map<String,List<NewGkTimetableOther>>> jxbSubjectTime;
	
	
	public String getXzbId() {
		return xzbId;
	}
	public void setXzbId(String xzbId) {
		this.xzbId = xzbId;
	}
	public String getXzbName() {
		return xzbName;
	}
	public void setXzbName(String xzbName) {
		this.xzbName = xzbName;
	}
	public List<NewGkDivideClass> getSearchXzbList() {
		return searchXzbList;
	}
	public void setSearchXzbList(List<NewGkDivideClass> searchXzbList) {
		this.searchXzbList = searchXzbList;
	}
	public List<NewGkDivideClass> getJxbList() {
		return jxbList;
	}
	public void setJxbList(List<NewGkDivideClass> jxbList) {
		this.jxbList = jxbList;
	}
	public Map<String, List<NewGkDivideClass>> getSearchJxbList() {
		return searchJxbList;
	}
	public void setSearchJxbList(Map<String, List<NewGkDivideClass>> searchJxbList) {
		this.searchJxbList = searchJxbList;
	}
	public Map<String, Map<String, List<NewGkTimetableOther>>> getJxbSubjectTime() {
		return jxbSubjectTime;
	}
	public void setJxbSubjectTime(
			Map<String, Map<String, List<NewGkTimetableOther>>> jxbSubjectTime) {
		this.jxbSubjectTime = jxbSubjectTime;
	}
	public Map<String, Map<String, List<NewGkTimetableOther>>> getXzbSubjectTime() {
		return xzbSubjectTime;
	}
	public void setXzbSubjectTime(
			Map<String, Map<String, List<NewGkTimetableOther>>> xzbSubjectTime) {
		this.xzbSubjectTime = xzbSubjectTime;
	}
	
}
