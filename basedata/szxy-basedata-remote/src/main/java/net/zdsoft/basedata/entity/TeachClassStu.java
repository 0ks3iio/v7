/*
* Project: v7
* Author : shenke
* @(#) TeachClassStu.java Created on 2016-9-26
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 教学班学生
 * @author: shenke
 * @version: 1.0
 * 2016-9-26下午3:00:48
 */
@Entity
@Table(name="base_teach_class_stu")
public class TeachClassStu extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5469316497454921660L;

	@Override
	public String fetchCacheEntitName() {
		return TeachClass.class.getSimpleName();
	}

	private String studentId;
	private String classId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	//后续业务 弱化字段 做硬删除20180115
	private Integer isDeleted;

	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
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
	
}
