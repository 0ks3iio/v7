/*
* Project: v7
* Author : shenke
* @(#) BaseStudentNormalFlow.java Created on 2016-8-5
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 调动记录
 * @author: shenke
 * @version: 1.0
 * 2016-8-5上午10:31:27
 */
@Entity
@Table(name="base_student_normalflow")
public class StudentFlow extends BaseEntity<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9178132767029981637L;

	@Override
	public String fetchCacheEntitName() {
		return this.getClass().getSimpleName();
	}
	//student_id
	private String studentId;
	//school_id
	private String schoolId;
	//school_name
	private String schoolName;
	//class_id
	private String classId;
	//class_name
	private String className;
	//reason
	private String reason;
	//handle_user_id
	private String handleUserId;
	//handle_user_name
	private String handleUserName;
	//flow_type
	private String flowType;
	//creation_time
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date creationTime;
	//pin
	private String pin;
	//semester
	private Integer semester;
	//acadyear
	private String acadyear;
	/**
	 * 离校
	 */
	public static final String STUDENT_FLOW_LEAVE ="0";
	/**
	 * 调入
	 */
	public static final String STUDENT_FLOW_IN ="1";
	
	/**
	 * 异动文件存放子目录
	 */
	public static final String STORE_PATH_CHILD = "student_flow";
	
	public static final String IS_USE_VERIFY_CODE = "is.use.verify.code";
	
	public static final String PUSH_WEIKE_TYPE = "";
	
	/**
	 * student_id 
	 */
	public void setStudentId(String studentId){
		this.studentId = studentId;
	}
	/**
	 * student_id 
	 */
	public String getStudentId(){
		return this.studentId;
	}
	
	/**
	 * school_id 
	 */
	public void setSchoolId(String schoolId){
		this.schoolId = schoolId;
	}
	/**
	 * school_id 
	 */
	public String getSchoolId(){
		return this.schoolId;
	}
	
	/**
	 * school_name 
	 */
	public void setSchoolName(String schoolName){
		this.schoolName = schoolName;
	}
	/**
	 * school_name 
	 */
	public String getSchoolName(){
		return this.schoolName;
	}
	
	/**
	 * class_id 
	 */
	public void setClassId(String classId){
		this.classId = classId;
	}
	/**
	 * class_id 
	 */
	public String getClassId(){
		return this.classId;
	}
	
	/**
	 * class_name 
	 */
	public void setClassName(String className){
		this.className = className;
	}
	/**
	 * class_name 
	 */
	public String getClassName(){
		return this.className;
	}
	
	/**
	 * reason 
	 */
	public void setReason(String reason){
		this.reason = reason;
	}
	/**
	 * reason 
	 */
	public String getReason(){
		return this.reason;
	}
	
	/**
	 * handle_user_id 
	 */
	public void setHandleUserId(String handleUserId){
		this.handleUserId = handleUserId;
	}
	/**
	 * handle_user_id 
	 */
	public String getHandleUserId(){
		return this.handleUserId;
	}
	
	/**
	 * handle_user_name 
	 */
	public void setHandleUserName(String handleUserName){
		this.handleUserName = handleUserName;
	}
	/**
	 * handle_user_name 
	 */
	public String getHandleUserName(){
		return this.handleUserName;
	}
	
	/**
	 * flow_type 
	 */
	public void setFlowType(String flowType){
		this.flowType = flowType;
	}
	/**
	 * flow_type 
	 */
	public String getFlowType(){
		return this.flowType;
	}
	
	/**
	 * creation_time 
	 */
	public void setCreationTime(java.util.Date creationTime){
		this.creationTime = creationTime;
	}
	/**
	 * creation_time 
	 */
	public java.util.Date getCreationTime(){
		return this.creationTime;
	}
	
	/**
	 * pin 
	 */
	public void setPin(String pin){
		this.pin = pin;
	}
	/**
	 * pin 
	 */
	public String getPin(){
		return this.pin;
	}
	
	
	public Integer getSemester() {
		return semester;
	}
	
	public void setSemester(Integer semester) {
		this.semester = semester;
	}
	
	/**
	 * acadyear 
	 */
	public void setAcadyear(String acadyear){
		this.acadyear = acadyear;
	}
	/**
	 * acadyear 
	 */
	public String getAcadyear(){
		return this.acadyear;
	}
}