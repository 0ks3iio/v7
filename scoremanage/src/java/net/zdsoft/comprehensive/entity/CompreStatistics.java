package net.zdsoft.comprehensive.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="scoremanage_compre_statistics")
public class CompreStatistics extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String unitId;
	private String acadyear;
	private String semester;
	private String classId;
	private String studentId;
	private String type;//成绩类型 1、总评综合素质分 2、英语综合素质分 3、英语口试综合素质分 4、体育综合素质分
	private Integer ranking;
	private Float score;
	private Float score2;
	private Float split;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Float getScore2() {
		return score2;
	}

	public void setScore2(Float score2) {
		this.score2 = score2;
	}

	public Float getSplit() {
		return split;
	}

	public void setSplit(Float split) {
		this.split = split;
	}

	@Override
	public String fetchCacheEntitName() {
		return "compreStatistics";
	}

}
