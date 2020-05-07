package net.zdsoft.basedata.dto;

import java.util.ArrayList;
import java.util.List;

public class TimetableChangeDto {
	
	private String deptId;//部门id
	private String deptName;//部门名称
	private String teacherId;//教师id
	private String teacherName;//教师姓名
	private Integer deptOrder;//部门排序id
	private Integer teacherOrder;//教师排序id
	private int takeNum;//代课次数
	private int manNum;//管课次数
	private int beTakeNum;//被代课次数
	private int beManNum;//被管课次数
	
	private String classId;//班级id
	private String className;//班级名称
	private String subjectId;//科目id
	private String subjectName;//科目名称
	private String type;//调代管类型
	private String searchDate;//查询展示时间
	private String changeStr;//调代管详细信息
	private String remark;//备注
	
	List<String> adjustedList = new ArrayList<String>();//多条调课信息，—导出调课单用
	
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public Integer getDeptOrder() {
		return deptOrder;
	}
	public void setDeptOrder(Integer deptOrder) {
		this.deptOrder = deptOrder;
	}
	public Integer getTeacherOrder() {
		return teacherOrder;
	}
	public void setTeacherOrder(Integer teacherOrder) {
		this.teacherOrder = teacherOrder;
	}
	public int getTakeNum() {
		return takeNum;
	}
	public void setTakeNum(int takeNum) {
		this.takeNum = takeNum;
	}
	public int getManNum() {
		return manNum;
	}
	public void setManNum(int manNum) {
		this.manNum = manNum;
	}
	public int getBeTakeNum() {
		return beTakeNum;
	}
	public void setBeTakeNum(int beTakeNum) {
		this.beTakeNum = beTakeNum;
	}
	public int getBeManNum() {
		return beManNum;
	}
	public void setBeManNum(int beManNum) {
		this.beManNum = beManNum;
	}
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
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}
	public String getChangeStr() {
		return changeStr;
	}
	public void setChangeStr(String changeStr) {
		this.changeStr = changeStr;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<String> getAdjustedList() {
		return adjustedList;
	}
	public void setAdjustedList(List<String> adjustedList) {
		this.adjustedList = adjustedList;
	}

}
