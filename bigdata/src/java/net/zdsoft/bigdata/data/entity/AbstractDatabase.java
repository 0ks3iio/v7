package net.zdsoft.bigdata.data.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author ke_shen@126.com
 * @since 2018/4/8 下午5:36
 */
@MappedSuperclass
public abstract class AbstractDatabase<K extends Serializable> extends BaseEntity<K> {

	private static final long serialVersionUID = -5127265830087801086L;
	protected String unitId;
	protected String name;
	protected String remark;
	@Temporal(TemporalType.TIMESTAMP)
	protected Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	protected Date modifyTime;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
}
