package net.zdsoft.base.entity.base;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "base_class")
public class BaseDept extends DataCenterBaseEntity {
	private static final long serialVersionUID = 4845424977829317908L;
	private String schoolId;
	private String gradeId;
	private String className;
	private String classCode;
	private Integer section;// 学段
	private String acadyear;// 新生入学学年
	private Integer isGraduate;// 是否毕业
	private Integer schoolingLength;// 学制
	private String teacherId;// 班主任
	private String studentId;// 班长

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public Integer getIsGraduate() {
		return isGraduate;
	}

	public void setIsGraduate(Integer isGraduate) {
		this.isGraduate = isGraduate;
	}

	public Integer getSchoolingLength() {
		return schoolingLength;
	}

	public void setSchoolingLength(Integer schoolingLength) {
		this.schoolingLength = schoolingLength;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "baseClass";
	}

}
