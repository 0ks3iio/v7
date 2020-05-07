package net.zdsoft.api.base.entity.business;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="business_student_changes")
public class StudentChanges extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "studentChanges";
	}

	private String studentId;
	private String ticketKey;
	private String nowState;
	private Date   creationTime;
	private Date   modifyTime;
	private String isLeaveSchool;
	private String schoolId;
	private String newSchoolId;
	private String classId;
	private String newClassId;
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getTicketKey() {
		return ticketKey;
	}
	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}
	public String getNowState() {
		return nowState;
	}
	public void setNowState(String nowState) {
		this.nowState = nowState;
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
	public String getIsLeaveSchool() {
		return isLeaveSchool;
	}
	public void setIsLeaveSchool(String isLeaveSchool) {
		this.isLeaveSchool = isLeaveSchool;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getNewSchoolId() {
		return newSchoolId;
	}
	public void setNewSchoolId(String newSchoolId) {
		this.newSchoolId = newSchoolId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getNewClassId() {
		return newClassId;
	}
	public void setNewClassId(String newClassId) {
		this.newClassId = newClassId;
	}
}
