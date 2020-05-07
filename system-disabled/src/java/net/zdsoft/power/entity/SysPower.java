package net.zdsoft.power.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2018年6月7日下午1:34:58 
 * 权限表
 */
@Entity
@Table(name = "sys_power")
public class SysPower extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "sysPower";
	}
	
	private String powerName; //权限名称
	private String description; //权限描述
	private String value; //权限的唯一特征值
	private int source; //权限来源 1--默认 2--其他ap
    private String isActive; //权限是否启用  0--不启用 1 --启用
    private String unitId; //单位id
    
    @Transient
    private Integer serverId;
	public String getPowerName() {
		return powerName;
	}
	public void setPowerName(String powerName) {
		this.powerName = powerName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
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
