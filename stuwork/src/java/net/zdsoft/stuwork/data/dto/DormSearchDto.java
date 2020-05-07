package net.zdsoft.stuwork.data.dto;

import java.util.Date;
import java.util.List;

import net.zdsoft.stuwork.data.entity.DyDormCheckRemind;
import net.zdsoft.stuwork.data.entity.DyDormCheckResult;

public class DormSearchDto {
	private String searchBuildId;
	private String searchRoomId;
	private Date searchDate;
	private Date startTime;
	private Date endTime;
	private String searchDateStr;
	
	private String acadyear;
	private String semesterStr;
	private int week;
	private int day;
	private int weekDay;
	private int section;
	
	private String unitId;
	private String classId;
	private String studentId;
	
	private List<DyDormCheckResult> resultList;
	private List<DyDormCheckRemind> remindList;
	
	
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getSearchDateStr() {
		return searchDateStr;
	}
	public void setSearchDateStr(String searchDateStr) {
		this.searchDateStr = searchDateStr;
	}
	public int getSection() {
		return section;
	}
	public void setSection(int section) {
		this.section = section;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public List<DyDormCheckResult> getResultList() {
		return resultList;
	}
	public void setResultList(List<DyDormCheckResult> resultList) {
		this.resultList = resultList;
	}
	public String getSearchBuildId() {
		return searchBuildId;
	}
	public void setSearchBuildId(String searchBuildId) {
		this.searchBuildId = searchBuildId;
	}
	public String getSearchRoomId() {
		return searchRoomId;
	}
	public Date getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}
	public String getAcadyear() {
		return acadyear;
	}
	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}
	public String getSemesterStr() {
		return semesterStr;
	}
	public void setSemesterStr(String semesterStr) {
		this.semesterStr = semesterStr;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public void setSearchRoomId(String searchRoomId) {
		this.searchRoomId = searchRoomId;
	}
	public List<DyDormCheckRemind> getRemindList() {
		return remindList;
	}
	public void setRemindList(List<DyDormCheckRemind> remindList) {
		this.remindList = remindList;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public int getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
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
	
	
}
