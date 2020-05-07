package net.zdsoft.bigdata.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "bg_sys_user_role")
public class BgUserRole extends BaseEntity<String> {

	private static final long serialVersionUID = -1980131445763716399L;
	public static final int TYPE_ROLE=1;
	public static final int TYPE_MODULE=2;
	private String userId;
	private String roleId;
	private String moduleId;
	private int type;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date creationTime;
	
	@Transient
	private String username;
	
	@Transient
	private String realName;
	
	@Transient
	private Integer sex;
	
	@Transient
	private String unitName;
	
	@Transient
	private String deptName;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Override
	public String fetchCacheEntitName() {
		return "bgUserRole";
	}

}
