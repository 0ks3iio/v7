package net.zdsoft.bigdata.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "bg_sys_role_perm")
public class BgRolePerm extends BaseEntity<String> {

	private static final long serialVersionUID = -1980131445763716399L;
	
	private String roleId;
	private String moduleId;
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date creationTime;
	
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

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public String fetchCacheEntitName() {
		return "bgRolePerm";
	}

}
