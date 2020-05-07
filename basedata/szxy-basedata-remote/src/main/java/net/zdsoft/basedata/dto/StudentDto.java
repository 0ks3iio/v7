/*
* Project: v7
* Author : shenke
* @(#) StudentDto.java Created on 2016-9-26
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.Student;

/**
 * @author: shenke
 * @version: 1.0
 * 2016-9-26下午3:25:42
 */
public class StudentDto extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Student student;
	private String className;//行政班名称(或者教学班名称)
	private String examNum;//考号
	private String id;//修改考号时的学生id
	
	private String classCode;//base_class用于排序
	
	
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getExamNum() {
		return examNum;
	}
	public void setExamNum(String examNum) {
		this.examNum = examNum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	
}
