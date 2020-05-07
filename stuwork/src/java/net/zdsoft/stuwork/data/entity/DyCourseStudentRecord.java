package net.zdsoft.stuwork.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="dy_course_student_record")
public class DyCourseStudentRecord extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String schoolId;
	private String acadyear;
	private String semester;
	private String recordId;
	private String type;
	private String teacherId;
	private String studentId;
	private String classId;
	private int week;
	private int day;
	private int period;
	private Date recordDate;
	
	

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



	public String getRecordId() {
		return recordId;
	}



	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getTeacherId() {
		return teacherId;
	}



	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}



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



	public int getPeriod() {
		return period;
	}



	public void setPeriod(int period) {
		this.period = period;
	}



	public Date getRecordDate() {
		return recordDate;
	}



	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}



	@Override
	public String fetchCacheEntitName() {
		return "dyCourseStudentRecord";
	}
}
