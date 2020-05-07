package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.Dept;

public class DeptDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dept dept;

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

}
