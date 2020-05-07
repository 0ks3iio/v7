package net.zdsoft.basedata.dto;

import java.util.List;

import net.zdsoft.basedata.entity.ClassTeaching;

public class ClassTeachingDto extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String classId;
	private String acadyear;
	private String semester;
	private String unitId;
	private List<ClassTeaching> claTeaList;
	private String[] classIds;//用于复制课程中的其他班级
	private String teachTitle;
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getAcadyear() {
		return acadyear;
	}
	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public List<ClassTeaching> getClaTeaList() {
		return claTeaList;
	}
	public void setClaTeaList(List<ClassTeaching> claTeaList) {
		this.claTeaList = claTeaList;
	}
	public String getTeachTitle() {
		return teachTitle;
	}
	public void setTeachTitle(String teachTitle) {
		this.teachTitle = teachTitle;
	}
	public String[] getClassIds() {
		return classIds;
	}
	public void setClassIds(String[] classIds) {
		this.classIds = classIds;
	}
	
	
	

}
