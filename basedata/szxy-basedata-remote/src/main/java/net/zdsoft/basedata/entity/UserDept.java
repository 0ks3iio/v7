package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_user_dept")
public class UserDept extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;

	@Override
	public String fetchCacheEntitName() {
		return "userDept";
	}
	
	private String userId;
	
	private String deptId;
	
	@Transient
	private String deptName;
	
	@Transient
	private String deptDingdingId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptDingdingId() {
		return deptDingdingId;
	}

	public void setDeptDingdingId(String deptDingdingId) {
		this.deptDingdingId = deptDingdingId;
	}

}
