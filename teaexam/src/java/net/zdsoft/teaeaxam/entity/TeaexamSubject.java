package net.zdsoft.teaeaxam.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="teaexam_subject")
public class TeaexamSubject extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6909577055897621599L;

	@Override
	public String fetchCacheEntitName() {
		return "teaexamSubject";
	}
	
	private String examId;
	private String subjectName;
	private int section;
	private Date startTime;
	private Date endTime;
	private Date creationTime;
	private Date modifyTime;
	private Float fullScore;
	@Transient
	private String endTimeStr;
	@Transient
	private int examNum;
	@Transient
	private int yxCount;
	@Transient
	private int hgCount;
	@Transient
	private int bhgCount;

	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public int getSection() {
		return section;
	}
	public void setSection(int section) {
		this.section = section;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public Float getFullScore() {
		return fullScore;
	}
	public void setFullScore(Float fullScore) {
		this.fullScore = fullScore;
	}
	public int getExamNum() {
		return examNum;
	}
	public void setExamNum(int examNum) {
		this.examNum = examNum;
	}
	public int getYxCount() {
		return yxCount;
	}
	public void setYxCount(int yxCount) {
		this.yxCount = yxCount;
	}
	public int getHgCount() {
		return hgCount;
	}
	public void setHgCount(int hgCount) {
		this.hgCount = hgCount;
	}
	public int getBhgCount() {
		return bhgCount;
	}
	public void setBhgCount(int bhgCount) {
		this.bhgCount = bhgCount;
	}

}
