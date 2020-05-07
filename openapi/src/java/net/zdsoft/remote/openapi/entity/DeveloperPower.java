package net.zdsoft.remote.openapi.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2018年7月11日下午5:38:05
 * 开发者的单位权限
 */
@Entity
@Table(name = "base_openapi_developer_power")
public class DeveloperPower extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;
	private String unitId; //单位id
	private String developerId; //开发者id

	@Override
	public String fetchCacheEntitName() {
		return "developerPower";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(String developerId) {
		this.developerId = developerId;
	}
}
