package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.Grade;

public class GradeDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Grade grade;
	private Integer clazzCount;
	private int needGraduate;

	public int getNeedGraduate() {
		return needGraduate;
	}

	public void setNeedGraduate(int needGraduate) {
		this.needGraduate = needGraduate;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Integer getClazzCount() {
		return clazzCount;
	}

	public void setClazzCount(Integer clazzCount) {
		this.clazzCount = clazzCount;
	}

}
