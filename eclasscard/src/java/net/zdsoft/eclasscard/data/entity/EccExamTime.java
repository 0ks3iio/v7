package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name="eclasscard_exam_time")
public class EccExamTime  extends EccTaskEntity{
	private static final long serialVersionUID = 1L;

	private String examId;
	private String unitId;
	private String subjectId;
	private String subType;
	@Override
	public String fetchCacheEntitName() {
		return "eccExamTime";
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

}
