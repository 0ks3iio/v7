package net.zdsoft.syncdatamq.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "sym_data_modify_time")
public class SymDataModifyTime extends BaseEntity<String> {

	private String entityName;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private String clientId;

	@Override
	public String fetchCacheEntitName() {
		return null;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
