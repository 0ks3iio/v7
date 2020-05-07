/*
* Project: v7
* Author : shenke
* @(#) ClassFlowDto.java Created on 2016-9-28
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.ClassFlow;

/**
 * 
 * @author: shenke
 * @version: 1.0
 * 2016-9-28下午1:50:52
 */
public class ClassFlowDto extends ClassFlow{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oldClassName;
	private String newClassName;
	private String studentName;
	private String identityCard;
	
	
	public ClassFlowDto() {
		super();
	}
	public String getOldClassName() {
		return oldClassName;
	}
	public void setOldClassName(String oldClassName) {
		this.oldClassName = oldClassName;
	}
	public String getNewClassName() {
		return newClassName;
	}
	public void setNewClassName(String newClassName) {
		this.newClassName = newClassName;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
}
