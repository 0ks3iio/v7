package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 *     班级内学生
 */
@Entity
@Table(name="newgkelective_class_student")
public class NewGkClassStudent extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	
	private String divideId;

	private String classId;
	
	private String studentId;
	
	private Date creationTime;
	
	private Date modifyTime;
	
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


	public Date getCreationTime() {
		return creationTime;
	}


	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}


	public Date getModifyTime() {
		return modifyTime;
	}
	public String getDivideId() {
		return divideId;
	}


	public void setDivideId(String divideId) {
		this.divideId = divideId;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}


	@Override
	public String fetchCacheEntitName() {
		return "newGkClassStudent";
	}


	public String getUnitId() {
		return unitId;
	}


	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
}
