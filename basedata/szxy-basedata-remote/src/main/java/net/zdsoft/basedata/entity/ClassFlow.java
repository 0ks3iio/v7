/*
* Project: v7
* Author : shenke
* @(#) ClassFlow.java Created on 2016-9-27
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 转班记录表
 * @author: shenke
 * @version: 1.0
 * 2016-9-27下午4:14:53
 */
@Entity
@Table(name="base_class_flow")
public class ClassFlow extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;

	private String studentId;
	private String oldClassId;
	private String newClassId;
	private String operateUserId;
	private String operateUserName;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
	private String acadyear;
	private Integer semester;
	
	private String schoolId;
	//該字段屬於導出Excel輔助字段
	
	@Override
	public String fetchCacheEntitName() {
		return ClassFlow.class.getSimpleName();
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getOldClassId() {
		return oldClassId;
	}

	public void setOldClassId(String oldClassId) {
		this.oldClassId = oldClassId;
	}

	public String getNewClassId() {
		return newClassId;
	}

	public void setNewClassId(String newClassId) {
		this.newClassId = newClassId;
	}

	public String getOperateUserId() {
		return operateUserId;
	}

	public void setOperateUserId(String operateUserId) {
		this.operateUserId = operateUserId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getOperateUserName() {
		return operateUserName;
	}

	public void setOperateUserName(String operateUserName) {
		this.operateUserName = operateUserName;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getCreationTimeStr() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			if(creationTime != null){
				return format.format(creationTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return StringUtils.EMPTY;
		}
		return StringUtils.EMPTY;
	}
	
	
}
