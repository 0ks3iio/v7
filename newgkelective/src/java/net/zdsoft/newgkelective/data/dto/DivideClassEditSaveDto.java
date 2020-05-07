package net.zdsoft.newgkelective.data.dto;

import java.io.Serializable;

public class DivideClassEditSaveDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String classId;
	private String className;
	private int stuNum;
	private String[] subjectIds;//保留的组合数据
	
	private String[] subGroupName;
	private Integer[] subStuNum;
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String[] getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(String[] subjectIds) {
		this.subjectIds = subjectIds;
	}
	public Integer[] getSubStuNum() {
		return subStuNum;
	}
	public void setSubStuNum(Integer[] subStuNum) {
		this.subStuNum = subStuNum;
	}
	public String[] getSubGroupName() {
		return subGroupName;
	}
	public void setSubGroupName(String[] subGroupName) {
		this.subGroupName = subGroupName;
	}
	public int getStuNum() {
		return stuNum;
	}
	public void setStuNum(int stuNum) {
		this.stuNum = stuNum;
	}

}
