package net.zdsoft.stuwork.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Table(name="dy_all_check")
@Entity
public class DyAllCheck extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String schoolId;
	
	private String acadyear;
	
	private String semester;
	
	private String classId;
	
	private int week;
	
	private int section;
	
	private float  healthExcellentScore;//卫生奖励分
	
	private float healthScore;//卫生总分
	
	private float disExcellentScore;//纪律奖励分
	
	private float disciplineScore;//纪律总分
	
	private float dormScore;//寝室得分
	
	private float  dormExcellentScore;//文明寝室奖励分
	
	private float studentDecScore;//学生个人扣分
	
	private float allScore;//小计
	
	private int allRank;//排名
	
	@Transient
	private float otherScorer;//班级其他考核分
	
	@Transient
	private String value;//

	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public float getOtherScorer() {
		return otherScorer;
	}

	public void setOtherScorer(float otherScorer) {
		this.otherScorer = otherScorer;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
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

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}


	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public float getHealthExcellentScore() {
		return healthExcellentScore;
	}

	public void setHealthExcellentScore(float healthExcellentScore) {
		this.healthExcellentScore = healthExcellentScore;
	}

	public float getHealthScore() {
		return healthScore;
	}

	public void setHealthScore(float healthScore) {
		this.healthScore = healthScore;
	}

	public float getDisExcellentScore() {
		return disExcellentScore;
	}

	public void setDisExcellentScore(float disExcellentScore) {
		this.disExcellentScore = disExcellentScore;
	}

	public float getDisciplineScore() {
		return disciplineScore;
	}

	public void setDisciplineScore(float disciplineScore) {
		this.disciplineScore = disciplineScore;
	}

	public float getDormScore() {
		return dormScore;
	}

	public void setDormScore(float dormScore) {
		this.dormScore = dormScore;
	}

	public float getDormExcellentScore() {
		return dormExcellentScore;
	}

	public void setDormExcellentScore(float dormExcellentScore) {
		this.dormExcellentScore = dormExcellentScore;
	}

	public float getStudentDecScore() {
		return studentDecScore;
	}

	public void setStudentDecScore(float studentDecScore) {
		this.studentDecScore = studentDecScore;
	}

	public float getAllScore() {
		return allScore;
	}

	public void setAllScore(float allScore) {
		this.allScore = allScore;
	}

	public int getAllRank() {
		return allRank;
	}

	public void setAllRank(int allRank) {
		this.allRank = allRank;
	}

	@Override
	public String fetchCacheEntitName() {
		return "getAllCheck";
	}
	
}
