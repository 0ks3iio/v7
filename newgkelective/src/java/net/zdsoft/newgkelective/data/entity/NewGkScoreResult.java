package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 *  成绩信息 
 */
@Entity
@Table(name = "newgkelective_score_result")
public class NewGkScoreResult extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private String referScoreId;
	private String studentId;
	private String subjectId;
	private Float score;
	private Date creationTime;
	private Date modifyTime;
	private String unitId;
	@Transient
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReferScoreId() {
		return referScoreId;
	}

	public void setReferScoreId(String referScoreId) {
		this.referScoreId = referScoreId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
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

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkScoreResult";
	}

}
