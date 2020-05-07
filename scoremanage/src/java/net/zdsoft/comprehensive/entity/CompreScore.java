package net.zdsoft.comprehensive.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 原始成绩分
 * @author niuchao
 * @date 2018-12-10
 *
 */
@Entity
@Table(name="scoremanage_compre_score")
public class CompreScore extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String compreInfoId;
	private String acadyear;
	private String semester;
	private String examId;
	private String subjectId;
	private String classId;
	private String studentId;
	private String score;
	private Float toScore;
	private Integer ranking;
	private String scoremanageType;
	private Integer virtual;
	private String remark;
	private Date creationTime;
	private Date modifyTime;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getCompreInfoId() {
		return compreInfoId;
	}

	public void setCompreInfoId(String compreInfoId) {
		this.compreInfoId = compreInfoId;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
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

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Float getToScore() {
		return toScore;
	}

	public void setToScore(Float toScore) {
		this.toScore = toScore;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public String getScoremanageType() {
		return scoremanageType;
	}

	public void setScoremanageType(String scoremanageType) {
		this.scoremanageType = scoremanageType;
	}

	public Integer getVirtual() {
		return virtual;
	}

	public void setVirtual(Integer virtual) {
		this.virtual = virtual;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
		return "compreScore";
	}

}
