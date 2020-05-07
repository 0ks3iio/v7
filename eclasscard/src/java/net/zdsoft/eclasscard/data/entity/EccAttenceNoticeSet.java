package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="eclasscard_notice_set")
public class EccAttenceNoticeSet  extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	private String unitId;
	private String type;
	private boolean isSend;
	private boolean isSendClassMaster;
	private boolean isSendGradeMaster;
	private boolean isSendParentMaster;
	private Integer delayTime;
	
	@Override
	public String fetchCacheEntitName() {
		return "eclasscardNoticeSet";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isSend() {
		return isSend;
	}

	public void setSend(boolean isSend) {
		this.isSend = isSend;
	}

	public boolean isSendClassMaster() {
		return isSendClassMaster;
	}

	public void setSendClassMaster(boolean isSendClassMaster) {
		this.isSendClassMaster = isSendClassMaster;
	}

	public boolean isSendGradeMaster() {
		return isSendGradeMaster;
	}

	public void setSendGradeMaster(boolean isSendGradeMaster) {
		this.isSendGradeMaster = isSendGradeMaster;
	}

	public boolean isSendParentMaster() {
		return isSendParentMaster;
	}

	public void setSendParentMaster(boolean isSendParentMaster) {
		this.isSendParentMaster = isSendParentMaster;
	}

	public Integer getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}

}
