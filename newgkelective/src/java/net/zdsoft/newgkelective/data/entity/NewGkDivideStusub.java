package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 
 * 新增分班方案同时，增加学生信息统计辅助表，之后分班这边取值来源于这张表，防止选课数据变动导致的数据问题
 */
@Entity
@Table(name="newgkelective_divide_stusub")
public class NewGkDivideStusub extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String divideId;
	private String choiceId;
	private String studentId;
	private String subjectType;//A,B 选考 学考
	private String subjectIds;//放进去的数据排序 ，隔开
	private String classId;
	private String studentSex;//默认男 (未维护性别当作男)
	private String studentName;
	private String studentCode;
	private String className;
	private Date creationTime;
	private Date modifyTime;
	
	
	public String getUnitId() {
		return unitId;
	}


	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}


	public String getDivideId() {
		return divideId;
	}


	public void setDivideId(String divideId) {
		this.divideId = divideId;
	}


	public String getChoiceId() {
		return choiceId;
	}


	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
	}


	public String getStudentId() {
		return studentId;
	}


	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}


	public String getSubjectType() {
		return subjectType;
	}


	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}


	public String getSubjectIds() {
		return subjectIds;
	}


	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}


	public String getClassId() {
		return classId;
	}


	public void setClassId(String classId) {
		this.classId = classId;
	}


	public String getStudentSex() {
		return studentSex;
	}


	public void setStudentSex(String studentSex) {
		this.studentSex = studentSex;
	}


	public String getStudentName() {
		return studentName;
	}


	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}


	public String getStudentCode() {
		return studentCode;
	}


	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}


	@Override
	public String fetchCacheEntitName() {
		return "newGkDivideStusub";
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


	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}

}
