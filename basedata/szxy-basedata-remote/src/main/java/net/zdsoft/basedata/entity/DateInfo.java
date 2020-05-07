package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "sys_date_info")
public class DateInfo extends BaseEntity<String> {

	@Column(name = "sch_id")
	private String schoolId;

	@Temporal(TemporalType.DATE)
	private Date infoDate;

	private String acadyear;
	private Integer semester;
	private Integer week;
	private Integer weekday;
	@Column(name = "is_feast")
	private String isFeast;
	
	
	public Integer getWeekday() {
		return weekday;
	}

	public void setWeekday(Integer weekday) {
		this.weekday = weekday;
	}

	public String getIsFeast() {
		return isFeast;
	}

	public void setIsFeast(String isFeast) {
		this.isFeast = isFeast;
	}

	@Override
	public String fetchCacheEntitName() {
		return "dateInfo";
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

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}
}
