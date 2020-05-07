package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Teacher;

public class ClazzDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Clazz clazz;
	//班级学生数
	private int studentCount;
	//班主任
	private Teacher teacher;
	//新增班级数量
	private int addClassCount;

	public int getAddClassCount() {
		return addClassCount;
	}

	public void setAddClassCount(int addClassCount) {
		this.addClassCount = addClassCount;
	}

	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	public int getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(int studentCount) {
		this.studentCount = studentCount;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

}
