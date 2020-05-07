package net.zdsoft.scoremanage.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="scoremanage_class_info")
public class ClassInfo extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ColumnInfo(displayName="考试科目id")
	@Column
	private String subjectInfoId;
	@ColumnInfo(displayName="班级id")
	private String classId;
	@ColumnInfo(displayName="班级类型")
	private String classType;
	@ColumnInfo(displayName="学校id")
	private String schoolId;
	@ColumnInfo(displayName="是否上锁")
	private String isLock;
	
	@Transient
	private String className;
	@Transient
	private String teacherNames;

	public String getSubjectInfoId() {
		return subjectInfoId;
	}
	public void setSubjectInfoId(String subjectInfoId) {
		this.subjectInfoId = subjectInfoId;
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
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String fetchCacheEntitName() {
		return "classInfo";
	}
	public String getIsLock() {
		return isLock;
	}
	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getTeacherNames() {
		return teacherNames;
	}
	public void setTeacherNames(String teacherNames) {
		this.teacherNames = teacherNames;
	}

}
