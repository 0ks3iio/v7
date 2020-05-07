package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;

public class TeacherDto extends BaseDto {
	private static final long serialVersionUID = -5504279997746666085L;
	private Teacher teacher = new Teacher();
	private User user = new User();

	private Unit unit;
	private Dept dept;

	private String teacherId;
	private String teacherName;
	private String classTeachingId;
	
	private String firsePinyin;//首字母

	
	public String getClassTeachingId() {
		return classTeachingId;
	}

	public void setClassTeachingId(String classTeachingId) {
		this.classTeachingId = classTeachingId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFirsePinyin() {
		return firsePinyin;
	}

	public void setFirsePinyin(String firsePinyin) {
		this.firsePinyin = firsePinyin;
	}

}
