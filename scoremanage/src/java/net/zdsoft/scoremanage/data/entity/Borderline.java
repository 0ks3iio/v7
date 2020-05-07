package net.zdsoft.scoremanage.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="scoremanage_borderline")
public class Borderline extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String examId;
	private String subjectId;
	private String statType;
	private String statMethodDo;
	private String nameOrUp;
	private String ratioValue;
	private String gradeCode;
	private int isUsing;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	@Transient
	private String courseName;
	
	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}


	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getStatType() {
		return statType;
	}

	public void setStatType(String statType) {
		this.statType = statType;
	}

	public String getNameOrUp() {
		return nameOrUp;
	}

	public void setNameOrUp(String nameOrUp) {
		this.nameOrUp = nameOrUp;
	}

	public String getStatMethodDo() {
		return statMethodDo;
	}

	public void setStatMethodDo(String statMethodDo) {
		this.statMethodDo = statMethodDo;
	}

	public String getRatioValue() {
		return ratioValue;
	}

	public void setRatioValue(String ratioValue) {
		this.ratioValue = ratioValue;
	}

	public int getIsUsing() {
		return isUsing;
	}

	public void setIsUsing(int isUsing) {
		this.isUsing = isUsing;
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
		return "borderline";
	}
}
