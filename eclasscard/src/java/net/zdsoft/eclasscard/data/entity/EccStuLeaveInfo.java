package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="eclasscard_stuleave_info")
public class EccStuLeaveInfo extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String stuDormAttId;
	private String studentId;
	private String leaveId;
	private Integer isFirst;
	
	public String getStuDormAttId() {
		return stuDormAttId;
	}



	public void setStuDormAttId(String stuDormAttId) {
		this.stuDormAttId = stuDormAttId;
	}



	public String getStudentId() {
		return studentId;
	}



	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}



	public String getLeaveId() {
		return leaveId;
	}



	public void setLeaveId(String leaveId) {
		this.leaveId = leaveId;
	}



	public Integer getIsFirst() {
		return isFirst;
	}



	public void setIsFirst(Integer isFirst) {
		this.isFirst = isFirst;
	}



	@Override
	public String fetchCacheEntitName() {
		return "eccStuLeaveInfo";
	}

}
