package net.zdsoft.eclasscard.data.dto;

import java.util.Date;

import net.zdsoft.eclasscard.data.entity.EccDateInfo;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

public class EccDateInfoDto {
	private String id;
	private String schoolId;
	private Date infoDate;
	private Integer week;
	private String weekDay;
	private String gradeId;
	private boolean isMarkup;
	private String remark;
	
	public EccDateInfo toEccDateInfo(){
		if(this!=null&&this.isMarkup()){
			EccDateInfo dateInfo = new EccDateInfo();
			if(StringUtils.isBlank(this.id)){
				dateInfo.setId(UuidUtils.generateUuid());
			}else{
				dateInfo.setId(this.id);
			}
			dateInfo.setGradeId(this.getGradeId());
			dateInfo.setInfoDate(this.getInfoDate());
			dateInfo.setSchoolId(this.getSchoolId());
			dateInfo.setRemark(this.getRemark());
			return dateInfo;
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public Date getInfoDate() {
		return infoDate;
	}

	public void setInfoDate(Date infoDate) {
		this.infoDate = infoDate;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public boolean isMarkup() {
		return isMarkup;
	}

	public void setMarkup(boolean isMarkup) {
		this.isMarkup = isMarkup;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
