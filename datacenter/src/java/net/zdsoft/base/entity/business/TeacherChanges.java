package net.zdsoft.base.entity.business;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="business_teacher_changes")
public class TeacherChanges extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "teacherChanges";
	}

	private String teacherId;
	private String ticketKey;
	private String isLeaveSchool;
	private String nowState;
	private Date   creationTime;
	private Date   modifyTime;
	private String schoolId;
	private String newSchoolId;
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTicketKey() {
		return ticketKey;
	}
	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}
	public String getIsLeaveSchool() {
		return isLeaveSchool;
	}
	public void setIsLeaveSchool(String isLeaveSchool) {
		this.isLeaveSchool = isLeaveSchool;
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
}
