package net.zdsoft.power.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2018年6月7日下午1:41:28 
 * ap权限关系表
 */
@Entity
@Table(name = "sys_ap_power")
public class SysApPower extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	@Override
	public String fetchCacheEntitName() {
		return "sysApPower";
	}
	private String powerId; //sysPower的主键id
	private Integer serverId; //baseServer的主键id
	private String unitId; //单位id
	
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getPowerId() {
		return powerId;
	}
	public void setPowerId(String powerId) {
		this.powerId = powerId;
	}
	public Integer getServerId() {
		return serverId;
	}
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
}
