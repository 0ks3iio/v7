package net.zdsoft.credit.data.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "exammanage_credit_patch_studen")
public class CreditPatchStudent extends BaseEntity<String> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String acadyear;
	private String semester;
	private String gradeId;
	private String classId;
	private String classType;
	private String studentId;
	private String studentCode;
	private String studentName;
	private String subjectId;
	
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


	public String getGradeId() {
		return gradeId;
	}


	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}


	public String getClassId() {
		return classId;
	}


	public void setClassId(String classId) {
		this.classId = classId;
	}


	public String getClassType() {
		return classType;
	}


	public void setClassType(String classType) {
		this.classType = classType;
	}


	public String getStudentId() {
		return studentId;
	}


	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}


	public String getStudentCode() {
		return studentCode;
	}


	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}


	public String getStudentName() {
		return studentName;
	}


	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}


	public String getSubjectId() {
		return subjectId;
	}


	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}


	@Override
	public String fetchCacheEntitName() {
		return "getCreditPatchStudent";
	}
	
}
