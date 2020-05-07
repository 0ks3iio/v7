package net.zdsoft.studevelop.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "studevelop_score")
public class StuDevelopScoreRecord extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3889885467078412431L;
	private String acadyear;
	private String semester;
	private String studentId;
	private String subjectId;
	private String categoryId;
	private String projectId;
	private String score;
	/**
	 * 只有 平时 或 期末 一个值
	 */
	@Transient
	private boolean isSingle;
	/**
	 * 平时或期末的值
	 */
	@Transient
	private String  PinshiOrQimo;

	public boolean isSingle() {
		return isSingle;
	}

	public void setSingle(boolean single) {
		isSingle = single;
	}

	public String getPinshiOrQimo() {
		return PinshiOrQimo;
	}

	public void setPinshiOrQimo(String pinshiOrQimo) {
		PinshiOrQimo = pinshiOrQimo;
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

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public String fetchCacheEntitName() {
		return "stuDevelopScoreRecord";
	}

}
