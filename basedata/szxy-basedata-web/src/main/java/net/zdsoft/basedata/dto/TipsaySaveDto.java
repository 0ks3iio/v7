package net.zdsoft.basedata.dto;

public class TipsaySaveDto {
	private String operateuser;//操作老师id
	private String acadyear;
	private String semester;
	private String teacherId;//需要代课的老师
	private String courseScheduleId;//选中的课程
	private String newTeacherId;//代课老师
	private String remark;
	
	private String unitId;
	public String getOperateuser() {
		return operateuser;
	}
	public void setOperateuser(String operateuser) {
		this.operateuser = operateuser;
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
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getCourseScheduleId() {
		return courseScheduleId;
	}
	public void setCourseScheduleId(String courseScheduleId) {
		this.courseScheduleId = courseScheduleId;
	}
	public String getNewTeacherId() {
		return newTeacherId;
	}
	public void setNewTeacherId(String newTeacherId) {
		this.newTeacherId = newTeacherId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
	
}
