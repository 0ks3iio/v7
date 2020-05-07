package net.zdsoft.stuwork.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="dy_dorm_check_result")
public class DyDormCheckResult extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String schoolId;
	private String acadyear;
	private String semester;
	private String buildingId;
	private String roomId;
	private Float wsScore;
	private Float nwScore;
	private Float jlScore;
	private String operatorId;
	private String remark;
	private int week;
	private int day;
	private Date inputDate;
	
	
	
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
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
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	public Float getWsScore() {
		return wsScore;
	}
	public void setWsScore(Float wsScore) {
		this.wsScore = wsScore;
	}
	public Float getNwScore() {
		return nwScore;
	}
	public void setNwScore(Float nwScore) {
		this.nwScore = nwScore;
	}
	public Float getJlScore() {
		return jlScore;
	}
	public void setJlScore(Float jlScore) {
		this.jlScore = jlScore;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	
	public Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	@Override
	public String fetchCacheEntitName() {
		return "getCheckResult";
	}
	
	
}
