package net.zdsoft.framework.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author linqz
 *
 */
@Entity
@Table(name = "sync_trigger_data")
public class SyncTriggerData extends BaseEntity<String> {

	private String dataType;
	private String dataId;
	private Date creationTime;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return null;
	}
}
