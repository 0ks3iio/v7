package net.zdsoft.teaeaxam.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 
 * @author weixh 2018年10月26日
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "teaexam_site_setting")
public class TeaexamSiteSetting extends BaseEntity<String> {
	private String examId;
	private String schoolId;
	private int roomNum;
	private String subjectInfoId;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;

	@Transient
	private String schoolName;// 考点名称
	@Transient
	private int perNum;// 单个教室容纳人数/已安排教师数
	@Transient
	private String roomNo;
	@Transient
	private int usedNum;
	@Transient
	private String subName;
	
	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public int getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	public String getSubjectInfoId() {
		return subjectInfoId;
	}

	public void setSubjectInfoId(String subjectInfoId) {
		this.subjectInfoId = subjectInfoId;
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

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public int getPerNum() {
		return perNum;
	}

	public void setPerNum(int perNum) {
		this.perNum = perNum;
	}

	public String fetchCacheEntitName() {
		return "teaexamSiteSetting";
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	public int getUsedNum() {
		return usedNum;
	}

	public void setUsedNum(int usedNum) {
		this.usedNum = usedNum;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

}
