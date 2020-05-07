package net.zdsoft.stuwork.data.entity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="dy_stu_evaluation")
public class DyStuEvaluation extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String acadyear;
	private String semester;
	private String studentId;
	private String gradeId;//选项id
	private String grade;//选项名称
	private String remark;
	private String association;
	private float score;

	@Transient
	private String studentName;
	@Transient
	private Map<String,String> strMap;

	public Map<String, String> getStrMap() {
		return strMap;
	}


	public void setStrMap(Map<String, String> strMap) {
		this.strMap = strMap;
	}


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


	public String getStudentId() {
		return studentId;
	}


	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}


	public String getGradeId() {
		return gradeId;
	}


	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}


	public String getGrade() {
		return grade;
	}


	public void setGrade(String grade) {
		this.grade = grade;
	}




	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getAssociation() {
		return association;
	}


	public void setAssociation(String association) {
		this.association = association;
	}


	public float getScore() {
		return score;
	}


	public void setScore(float score) {
		this.score = score;
	}


	@Override
	public String fetchCacheEntitName() {
		return "stuEvaluation";
	}

}
