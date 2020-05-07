package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="eclasscard_stuclz_attance")
public class EccStuclzAttence extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	private String studentId; // 学生Id
	private String classAttId; // 班级考勤Id
	private String classId; // 班级Id
	private Integer status; 
	@Temporal(TemporalType.TIMESTAMP)
	private Date clockDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	@Transient
	private String stuUserName; //学生用户名
	@Transient
	private String stuRealName; //学生姓名
	@Transient
	private String teacherName; //老师姓名
	@Transient
	private String className; //班级名
	@Transient
	private String stuCode; //学号
	@Transient
	private Integer sex; //性别
	@Transient
	private String showPictrueUrl; //头像显示
	@Transient
	private int rowNo;
	@Transient
	private int colNo;
	
	
	
	@Override
	public String fetchCacheEntitName() {
		return "eclasscardStuclzAttance";
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

	public String getClassAttId() {
		return classAttId;
	}

	public void setClassAttId(String classAttId) {
		this.classAttId = classAttId;
	}

	public String getStuUserName() {
		return stuUserName;
	}

	public void setStuUserName(String stuUserName) {
		this.stuUserName = stuUserName;
	}

	public String getStuRealName() {
		return stuRealName;
	}

	public void setStuRealName(String stuRealName) {
		this.stuRealName = stuRealName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getStuCode() {
		return stuCode;
	}

	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getShowPictrueUrl() {
		return showPictrueUrl;
	}

	public void setShowPictrueUrl(String showPictrueUrl) {
		this.showPictrueUrl = showPictrueUrl;
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

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public int getColNo() {
		return colNo;
	}

	public void setColNo(int colNo) {
		this.colNo = colNo;
	}

	
}
