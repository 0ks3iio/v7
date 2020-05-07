package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 选课结果
 */
@Entity
@Table(name = "newgkelective_choice_result")
public class NewGkChoResult extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private String choiceId;
	private String studentId;
	private String subjectId;
	private Date creationTime;
	private Date modifyTime;
	private String subjectType;
	/**
	 * 选课结果类型
	 * 01：已选科目
	 * 02：优先调剂到科目
	 * 03：明确不选科目
	 */
	private String kindType;
	private String unitId;
	@Transient
	private String subjectName;

	public String getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
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

	@Override
	public String fetchCacheEntitName() {
		return "newGkChoResult";
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getKindType() {
		return kindType;
	}

	public void setKindType(String kindType) {
		this.kindType = kindType;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
}
