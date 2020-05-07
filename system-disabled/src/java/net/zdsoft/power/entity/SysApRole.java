package net.zdsoft.power.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2018年6月7日下午1:44:47
 * ap角色关系表
 */
@Entity
@Table(name = "sys_ap_role")
public class SysApRole extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	@Override
	public String fetchCacheEntitName() {
		return "sysApRole";
	}
	
	private String roleId;   //sysRole的主键id
	private Integer serverId; //baseServer的主键id
	private String unitId; //unitId 单位id

	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public Integer getServerId() {
		return serverId;
	}
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
}
