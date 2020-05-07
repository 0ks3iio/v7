package net.zdsoft.stuwork.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;


@Entity
@Table(name="dy_dorm_check_discipline")
public class DyDormCheckDiscipline extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String schoolId;
	/**
	 * 
	 */
	private String acadyear;
	/**
	 * 
	 */
	private String semester;
	/**
	 * 
	 */
	private String buildingId;
	/**
	 * 
	 */
	private String roomId;
	/**
	 * 
	 */
	private String studentId;
	/**
	 * 
	 */
	private String classId;
	/**
	 * 
	 */
	private Integer week;
	/**
	 * 
	 */
	private Integer day;
	/**
	 * 
	 */
	private Date checkDate;
	/**
	 * 
	 */
	private Float score;
	/**
	 * 
	 */
	private String reason;
	/**
	 * 
	 */
	private String operatorId;
	
	
	//辅助字段
	@Transient
	private String studentName;
	@Transient
	private String roomName;
	@Transient
	private String buildingName;
	@Transient
	private String className;
	
	
	
	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	/**
	 * 设置
	 */
	 public void setSchoolId(String schoolId){
	 	this.schoolId = schoolId;
	 }
	 
	 /**
	  * 获取
	  */
	 public String getSchoolId(){
	 	return this.schoolId;
	 }
	/**
	 * 设置
	 */
	 public void setAcadyear(String acadyear){
	 	this.acadyear = acadyear;
	 }
	 
	 /**
	  * 获取
	  */
	 public String getAcadyear(){
	 	return this.acadyear;
	 }
	/**
	 * 设置
	 */
	 public void setSemester(String semester){
	 	this.semester = semester;
	 }
	 
	 /**
	  * 获取
	  */
	 public String getSemester(){
	 	return this.semester;
	 }
	/**
	 * 设置
	 */
	 public void setBuildingId(String buildingId){
	 	this.buildingId = buildingId;
	 }
	 
	 /**
	  * 获取
	  */
	 public String getBuildingId(){
	 	return this.buildingId;
	 }
	/**
	 * 设置
	 */
	 public void setRoomId(String roomId){
	 	this.roomId = roomId;
	 }
	 
	 /**
	  * 获取
	  */
	 public String getRoomId(){
	 	return this.roomId;
	 }
	/**
	 * 设置
	 */
	 public void setStudentId(String studentId){
	 	this.studentId = studentId;
	 }
	 
	 /**
	  * 获取
	  */
	 public String getStudentId(){
	 	return this.studentId;
	 }
	/**
	 * 设置
	 */
	 public void setClassId(String classId){
	 	this.classId = classId;
	 }
	 
	 /**
	  * 获取
	  */
	 public String getClassId(){
	 	return this.classId;
	 }
	/**
	 * 设置
	 */
	 public void setWeek(Integer week){
	 	this.week = week;
	 }
	 
	 /**
	  * 获取
	  */
	 public Integer getWeek(){
	 	return this.week;
	 }
	/**
	 * 设置
	 */
	 public void setDay(Integer day){
	 	this.day = day;
	 }
	 
	 /**
	  * 获取
	  */
	 public Integer getDay(){
	 	return this.day;
	 }
	/**
	 * 设置
	 */
	 public void setCheckDate(Date checkDate){
	 	this.checkDate = checkDate;
	 }
	 
	 /**
	  * 获取
	  */
	 public Date getCheckDate(){
	 	return this.checkDate;
	 }
	/**
	 * 设置
	 */
	 public void setScore(Float score){
	 	this.score = score;
	 }
	 
	 /**
	  * 获取
	  */
	 public Float getScore(){
	 	return this.score;
	 }
	/**
	 * 设置
	 */
	 public void setReason(String reason){
	 	this.reason = reason;
	 }
	 
	 /**
	  * 获取
	  */
	 public String getReason(){
	 	return this.reason;
	 }
	/**
	 * 设置
	 */
	 public void setOperatorId(String operatorId){
	 	this.operatorId = operatorId;
	 }
	 
	 /**
	  * 获取
	  */
	 public String getOperatorId(){
	 	return this.operatorId;
	 }

	@Override
	public String fetchCacheEntitName() {
		return "getDis";
	}
}
