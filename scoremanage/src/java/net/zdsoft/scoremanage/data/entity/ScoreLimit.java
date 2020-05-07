package net.zdsoft.scoremanage.data.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="scoremanage_limit")
public class ScoreLimit extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String acadyear;
	private String semester;
	private String unitId;
	private String examInfoId;//选修课 这个值为32个0
	
	private String classType;//1:行政班 2:教学班
	private String classId;
	private String subjectId;
	private String teacherId;
	
	
	@Override
	public String fetchCacheEntitName() {
		return "scoreLimit";
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

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getExamInfoId() {
		return examInfoId;
	}

	public void setExamInfoId(String examInfoId) {
		this.examInfoId = examInfoId;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

}
