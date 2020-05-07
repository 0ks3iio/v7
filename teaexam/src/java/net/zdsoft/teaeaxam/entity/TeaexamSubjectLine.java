package net.zdsoft.teaeaxam.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="teaexam_subject_line")
public class TeaexamSubjectLine extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6775118483552578190L;

	@Override
	public String fetchCacheEntitName() {
		return "teaexamSubjectLine";
	}
	
	private String subjectInfoId;
	private String gradeCode;
	private Float minScore;
	private Date creationTime;
	private Date modifyTime;

	public String getSubjectInfoId() {
		return subjectInfoId;
	}
	public void setSubjectInfoId(String subjectInfoId) {
		this.subjectInfoId = subjectInfoId;
	}
	public String getGradeCode() {
		return gradeCode;
	}
	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}
	public Float getMinScore() {
		return minScore;
	}
	public void setMinScore(Float minScore) {
		this.minScore = minScore;
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
