package net.zdsoft.stuwork.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="dy_class_other_check")
public class DyClassOtherCheck extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String acadyear;
	private Integer semester;
	private String classId;
	private float score;
	private String remark;
	private Date checkTime;
	private Integer week;
	
	@Transient
	private String className;
	
	public String getClassName() {
		return className;
	}



	public void setClassName(String className) {
		this.className = className;
	}



	public String getUnitId() {
		return unitId;
	}



	public void setUnitId(String unitId) {
		this.unitId = unitId;
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



	public String getClassId() {
		return classId;
	}



	public void setClassId(String classId) {
		this.classId = classId;
	}



	public float getScore() {
		return score;
	}



	public void setScore(float score) {
		this.score = score;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public Date getCheckTime() {
		return checkTime;
	}



	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}



	public Integer getWeek() {
		return week;
	}



	public void setWeek(Integer week) {
		this.week = week;
	}



	@Override
	public String fetchCacheEntitName() {
		return "dyClassOtherCheck";
	}

	
}
