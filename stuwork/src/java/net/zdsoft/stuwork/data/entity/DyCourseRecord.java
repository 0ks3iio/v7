package net.zdsoft.stuwork.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="dy_course_record")
public class DyCourseRecord extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String schoolId;
	private String acadyear;
	private String semester;
	private String type;//1是上课日志 2是晚自习日志
	private String subjectId;
	private float score;
	private String remark;
	private String teacherId;
	private String subjectName;
	private int week;
	private int day;
	private int period;
	private Date recordDate;
	private String classId;//晚自习用
	private String recordClass;
	
	//辅助字段
	@Transient
	private String punishStuNames;
	@Transient
	private String clsNames;
	@Transient
	private String studentIds;
	@Transient
	private String teacherName;
		
	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getStudentIds() {
		return studentIds;
	}

	public void setStudentIds(String studentIds) {
		this.studentIds = studentIds;
	}

	public String getClsNames() {
		return clsNames;
	}

	public void setClsNames(String clsNames) {
		this.clsNames = clsNames;
	}

	public String getPunishStuNames() {
		return punishStuNames;
	}

	public void setPunishStuNames(String punishStuNames) {
		this.punishStuNames = punishStuNames;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
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

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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
		return "dyCourseRecord";
	}

	public String getRecordClass() {
		return recordClass;
	}

	public void setRecordClass(String recordClass) {
		this.recordClass = recordClass;
	}

}
