package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="eclasscard_studorm_attance")
public class EccStudormAttence extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	private String studentId; // 学生Id
	private String dormAttId; // 寝室考勤Id
	private String classId; // 班级Id
	private Integer status; 
	@Temporal(TemporalType.TIMESTAMP)
	private Date clockDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	@Transient
	private String studentName;
	@Transient
	private String className;
	@Transient
	private String gradeName;
	@Transient
	private String roomName;
	@Transient
	private String teacherName;
	@Transient
	private int InNum;//已签到次数
	@Transient
	private int leaveNum;//请假次数
	@Transient
	private int  noNum;//未签到次数
	@Transient
	private String  periodTime;//考勤时段
	@Transient
	private Date  dateTime;//考勤日期
	@Transient
	private String content;
	@Transient
	private Integer isFirst;
	
	@Transient
	private String classCode;
	@Transient
	private String acadyear;
	@Transient
	private Integer section;
	
	
	public Integer getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Integer isFirst) {
		this.isFirst = isFirst;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String fetchCacheEntitName() {
		return "eclasscardStudormAttance";
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getPeriodTime() {
		return periodTime;
	}

	public void setPeriodTime(String periodTime) {
		this.periodTime = periodTime;
	}

	public int getInNum() {
		return InNum;
	}

	public void setInNum(int inNum) {
		InNum = inNum;
	}

	public int getLeaveNum() {
		return leaveNum;
	}

	public void setLeaveNum(int leaveNum) {
		this.leaveNum = leaveNum;
	}

	public int getNoNum() {
		return noNum;
	}

	public void setNoNum(int noNum) {
		this.noNum = noNum;
	}

	public String getStudentName() {
		return studentName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
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

	public Date getClockDate() {
		return clockDate;
	}

	public void setClockDate(Date clockDate) {
		this.clockDate = clockDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDormAttId() {
		return dormAttId;
	}

	public void setDormAttId(String dormAttId) {
		this.dormAttId = dormAttId;
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

	
}
