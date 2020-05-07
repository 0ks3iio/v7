package net.zdsoft.scoremanage.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="scoremanage_statistic")
public class ScoreStatistic extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;

	private String unitId;
	private String acadyear;
	private String semester;
	private String examId;
	private String subjectId;
	private String studentId;
	private String classId;
	private String teachClassId;
	private String scoreType;
	private String score;
	private Integer courseRanking;
	private Integer allRanking;
	private Float allScore;
	/**
	 * 0班级范围
	 * 1学校范围
		2统考范围
		用于区别courseRanking和allRanking的所在范围
	 */
	private String type;
	
	@Transient
	private String studentName;
	
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

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

	public String getTeachClassId() {
		return teachClassId;
	}

	public void setTeachClassId(String teachClassId) {
		this.teachClassId = teachClassId;
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

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Integer getCourseRanking() {
		return courseRanking;
	}

	public void setCourseRanking(Integer courseRanking) {
		this.courseRanking = courseRanking;
	}

	public Integer getAllRanking() {
		return allRanking;
	}

	public void setAllRanking(Integer allRanking) {
		this.allRanking = allRanking;
	}

	public Float getAllScore() {
		return allScore;
	}

	public void setAllScore(Float allScore) {
		this.allScore = allScore;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String fetchCacheEntitName() {
		return "scoreStatistic";
	}
	
}
