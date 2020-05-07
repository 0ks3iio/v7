package net.zdsoft.newgkelective.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 排课结果课程上课信息
 */
@Entity
@Table(name = "newgkelective_timetable_other")
public class NewGkTimetableOther extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String timetableId;
	private String placeId;
	private Integer dayOfWeek; // 星期
	private String periodInterval; // 时间段
	private Integer period; // 节次
	private Integer firstsdWeek;//单双周
	private String unitId;
	@Transient
	private String placeName;
	@Transient
	private String className;
	@Transient
	private String subjectName;
	@Transient
	private String teacherName;
	
	@Transient
	private String timeTr;
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	@Transient
	private boolean haveConflict;
	
	
	public boolean isHaveConflict() {
		return haveConflict;
	}

	public void setHaveConflict(boolean haveConflict) {
		this.haveConflict = haveConflict;
	}

	public String getTimetableId() {
		return timetableId;
	}

	public void setTimetableId(String timetableId) {
		this.timetableId = timetableId;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getPeriodInterval() {
		return periodInterval;
	}

	public void setPeriodInterval(String periodInterval) {
		this.periodInterval = periodInterval;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkTimetableOther";
	}

	public String getTimeTr() {
		return timeTr;
	}

	public void setTimeTr(String timeTr) {
		this.timeTr = timeTr;
	}

	public Integer getFirstsdWeek() {
		return firstsdWeek;
	}

	public void setFirstsdWeek(Integer firstsdWeek) {
		this.firstsdWeek = firstsdWeek;
	}
	
}
