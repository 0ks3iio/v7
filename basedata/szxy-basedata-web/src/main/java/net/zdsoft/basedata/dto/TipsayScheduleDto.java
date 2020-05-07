package net.zdsoft.basedata.dto;

public class TipsayScheduleDto {
	private String applyType;
	private String acadyear;
	private String semester;
	private String oldTeacherId;
	private String courseScheduleIds;
	
	private String tipsayId;//审核安排代课老师
	
	private String newTeacherId;
	private String remark;
	private String type;
	
	
	
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
	public String getOldTeacherId() {
		return oldTeacherId;
	}
	public void setOldTeacherId(String oldTeacherId) {
		this.oldTeacherId = oldTeacherId;
	}
	public String getNewTeacherId() {
		return newTeacherId;
	}
	public void setNewTeacherId(String newTeacherId) {
		this.newTeacherId = newTeacherId;
	}
	public String getCourseScheduleIds() {
		return courseScheduleIds;
	}
	public void setCourseScheduleIds(String courseScheduleIds) {
		this.courseScheduleIds = courseScheduleIds;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getApplyType() {
		return applyType;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	public String getTipsayId() {
		return tipsayId;
	}
	public void setTipsayId(String tipsayId) {
		this.tipsayId = tipsayId;
	}

	
}
