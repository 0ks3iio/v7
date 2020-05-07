package net.zdsoft.eclasscard.data.dto;

import java.util.Date;

public class AttanceSearchDto {
	
	private String buildingId;//寝室楼id
	
	private Date searchDate;//查询日期
	
	private String periodId;//考勤id
	
	private String gradeCode;//年级code
	
	private int attStatus;//状态    0初始化    1未刷卡  2请假  3正常
	
	private String classId;
	private String gradeId;
	private Date startTime;
	private Date endTime;
	
	

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public int getAttStatus() {
		return attStatus;
	}

	public void setAttStatus(int attStatus) {
		this.attStatus = attStatus;
	}

	
	
}
