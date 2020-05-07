package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="eclasscard_teaclz_attance")
public class EccTeaclzAttence extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	private String teacherId; // 学生Id
	private String classAttId; // 班级考勤Id
	private Integer status; 
	@Temporal(TemporalType.DATE)
	private Date clockDate;//打卡日期
	@Temporal(TemporalType.TIMESTAMP)
	private Date clockTime;//打卡时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	@Transient
	private String teaUserName; //学生用户名
	@Transient
	private String teaRealName; //学生姓名
	@Transient
	private String className; //班级名
	@Transient
	private String subjectName; //班级名
	@Transient
	private Integer sex; //学号
	@Transient
	private int cdNum; //迟到节数
	@Transient
	private int wqdNum; //未签到节数
	
	@Override
	public String fetchCacheEntitName() {
		return "eclasscardStuclzAttance";
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeaUserName() {
		return teaUserName;
	}

	public void setTeaUserName(String teaUserName) {
		this.teaUserName = teaUserName;
	}

	public String getTeaRealName() {
		return teaRealName;
	}

	public void setTeaRealName(String teaRealName) {
		this.teaRealName = teaRealName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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

	public String getClassAttId() {
		return classAttId;
	}

	public void setClassAttId(String classAttId) {
		this.classAttId = classAttId;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getClockTime() {
		return clockTime;
	}

	public void setClockTime(Date clockTime) {
		this.clockTime = clockTime;
	}

	public int getCdNum() {
		return cdNum;
	}

	public void setCdNum(int cdNum) {
		this.cdNum = cdNum;
	}

	public int getWqdNum() {
		return wqdNum;
	}

	public void setWqdNum(int wqdNum) {
		this.wqdNum = wqdNum;
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
