package net.zdsoft.stuwork.data.dto;

import net.zdsoft.basedata.entity.Clazz;

/**
 * @author yangsj  2017年12月1日上午11:31:32
 */
public class DyNightSchedulingDto {

	private Clazz clazz;
	private String teacherId;
	private String teacherName;
	public Clazz getClazz() {
		return clazz;
	}
	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
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
	
	
	
}
