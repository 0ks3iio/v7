package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="eclasscard_timing_log")
public class EccTimingLog extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String cardId;
	private String type;//1:设置定时开关时间日志 2：保存可能客户端时间不正确，只用于之后保存 is_deleted=1,或者类型不对
	private String timeRemark;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private Integer isDeleted;
	@Override
	public String fetchCacheEntitName() {
		return "eccTimingLog";
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getTimeRemark() {
		return timeRemark;
	}
	public void setTimeRemark(String timeRemark) {
		this.timeRemark = timeRemark;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
