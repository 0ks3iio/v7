package net.zdsoft.basedata.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="base_class_hour")
public class ClassHour extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String subjectId;
	private String gradeId;
	private String acadyear;
	private String semester;
	private String classIds;//存放关联行政班
//	@Transient
//	private Integer dayOfWeek;
//	@Transient
//	private String periodInterval;
//	@Transient
//	private Integer period;  
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private Integer isDeleted;
	@Transient
	private String classNames;
	
	@Transient
	//classHourEx.getDayOfWeek()+"_"+classHourEx.getPeriodInterval()+"_"+classHourEx.getPeriod()
	private List<String> timeList;
	/**
	 * classIds
	 */
	@Transient
	private List<String> classIdList;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
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

//	public Integer getDayOfWeek() {
//		return dayOfWeek;
//	}
//
//	public void setDayOfWeek(Integer dayOfWeek) {
//		this.dayOfWeek = dayOfWeek;
//	}
//	
//	public String getPeriodInterval() {
//		return periodInterval;
//	}
//
//	public void setPeriodInterval(String periodInterval) {
//		this.periodInterval = periodInterval;
//	}
//
//	public Integer getPeriod() {
//		return period;
//	}
//
//	public void setPeriod(Integer period) {
//		this.period = period;
//	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public String getClassIds() {
		return classIds;
	}

	public void setClassIds(String classIds) {
		this.classIds = classIds;
	}

	@Override
	public String fetchCacheEntitName() {
		return "classHour";
	}

	public List<String> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<String> timeList) {
		this.timeList = timeList;
	}

	public String getClassNames() {
		return classNames;
	}

	public void setClassNames(String classNames) {
		this.classNames = classNames;
	}

	public List<String> getClassIdList() {
		return classIdList;
	}

	public void setClassIdList(List<String> classIdList) {
		this.classIdList = classIdList;
	}
}
