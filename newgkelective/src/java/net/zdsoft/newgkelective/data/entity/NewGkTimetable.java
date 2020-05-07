package net.zdsoft.newgkelective.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
/**
 * 排课结果课程信息
 */
@Entity
@Table(name = "newgkelective_timetable")
public class NewGkTimetable extends BaseEntity<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String arrayId;
	private String classId;
	private String classType;
	private String subjectId;
	private String subjectType;
	private Date creationTime;
	private Date modifyTime;
	private String unitId;
	/**
	 * 时间点 地点
	 */
	@Transient
	private List<NewGkTimetableOther> timeList=new ArrayList<NewGkTimetableOther>();
	/**
	 * 授课老师
	 */
	@Transient
	private List<String> teacherIdList=new ArrayList<String>();
	
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getArrayId() {
		return arrayId;
	}

	public void setArrayId(String arrayId) {
		this.arrayId = arrayId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
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

	@Override
	public String fetchCacheEntitName() {
		return "newGkTimetable";
	}

	public List<NewGkTimetableOther> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<NewGkTimetableOther> timeList) {
		this.timeList = timeList;
	}

	public List<String> getTeacherIdList() {
		return teacherIdList;
	}

	public void setTeacherIdList(List<String> teacherIdList) {
		this.teacherIdList = teacherIdList;
	}
	
}
