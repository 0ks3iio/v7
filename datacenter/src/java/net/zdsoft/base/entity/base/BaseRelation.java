package net.zdsoft.base.entity.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name = "base_relation")
public class BaseRelation extends BaseEntity<String> {
	private static final long serialVersionUID = 6460175583483265161L;

	@Column(nullable = false)
	private String businessId;
	@Column(nullable = false, length = 32)
	private String dcId;
	@Column(nullable = false, length = 32)
	private String ticketKey;
	@Column(nullable = false, length = 32)
	private String sourceAp;//数据来源apKey
	private String model;// 模型表名称
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private Integer isDeleted;
	private Integer unitClass = 0;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getDcId() {
		return dcId;
	}

	public void setDcId(String dcId) {
		this.dcId = dcId;
	}

	public String getTicketKey() {
		return ticketKey;
	}

	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}

	public String getSourceAp() {
		return sourceAp;
	}

	public void setSourceAp(String sourceAp) {
		this.sourceAp = sourceAp;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getUnitClass() {
		return unitClass;
	}

	public void setUnitClass(Integer unitClass) {
		this.unitClass = unitClass;
	}

	@Override
	public String fetchCacheEntitName() {
		return "baseRelation";
	}

}
