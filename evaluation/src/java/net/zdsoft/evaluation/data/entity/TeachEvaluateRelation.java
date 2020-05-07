package net.zdsoft.evaluation.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="teach_evaluate_relation")
public class TeachEvaluateRelation extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String projectId;
	
	private String valueId;
	
	private String subjectId;
	
	private String classType;

	/**
	 * 教学班名称
	 */
	@Transient
	private String teachClassName;
	/**
	 * 课程名称
	 */
	@Transient
	private String courseName;
	/**
	 * 所属学科
	 */
	@Transient
	private String courseTypeName;
	@Transient
	private String gradeName;
	@Transient
	private boolean haveSelected;
	
	
	public String getProjectId() {
		return projectId;
	}


	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}


	public String getValueId() {
		return valueId;
	}


	public void setValueId(String valueId) {
		this.valueId = valueId;
	}


	public String getSubjectId() {
		return subjectId;
	}


	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}


	public String getClassType() {
		return classType;
	}


	public void setClassType(String classType) {
		this.classType = classType;
	}


	@Override
	public String fetchCacheEntitName() {
		return "getEvaRelation";
	}


	public String getTeachClassName() {
		return teachClassName;
	}


	public void setTeachClassName(String teachClassName) {
		this.teachClassName = teachClassName;
	}


	public String getCourseName() {
		return courseName;
	}


	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	public String getCourseTypeName() {
		return courseTypeName;
	}


	public void setCourseTypeName(String courseTypeName) {
		this.courseTypeName = courseTypeName;
	}


	public String getGradeName() {
		return gradeName;
	}


	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}


	public boolean isHaveSelected() {
		return haveSelected;
	}


	public void setHaveSelected(boolean haveSelected) {
		this.haveSelected = haveSelected;
	}
	
}
