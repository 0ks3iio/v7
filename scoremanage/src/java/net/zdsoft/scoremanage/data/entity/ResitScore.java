package net.zdsoft.scoremanage.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="scoremanage_resit_score")
public class ResitScore extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4151221039626297427L;
	private String unitId;
	private String examId;
	private String gradeId;
	private String subjectId;
	private String studentId;
	private String score;

	@Override
	public String fetchCacheEntitName() {
		return "resitScore";
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

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

}
