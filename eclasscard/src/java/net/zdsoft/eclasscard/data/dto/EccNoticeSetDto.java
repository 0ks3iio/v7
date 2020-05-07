package net.zdsoft.eclasscard.data.dto;

import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeSet;

public class EccNoticeSetDto {
	private String idClass;
	private String idDorm;
	private String idInOut;
	private String unitId;
	private boolean isSendClass;
	private boolean isSendDorm;
	private boolean isSendClassMasterClass;
	private boolean isSendClassMasterDorm;
	private boolean isSendGradeMasterClass;
	private boolean isSendGradeMasterDorm;
	private boolean isSendInOut;
	private boolean isSendParentMasterInOut;
	private Integer delayTime;
	
	public EccAttenceNoticeSet toEccNoticeSetClass() {
		EccAttenceNoticeSet noticeSet = new EccAttenceNoticeSet(); 
		noticeSet.setId(idClass);
		noticeSet.setSend(isSendClass);
		noticeSet.setDelayTime(delayTime);
		noticeSet.setSendClassMaster(isSendClassMasterClass);
		noticeSet.setSendGradeMaster(isSendGradeMasterClass);
		noticeSet.setSendParentMaster(false);
		noticeSet.setType(EccConstants.CLASS_ATTENCE_SET_TYPE1);
		noticeSet.setUnitId(unitId);
		return noticeSet;
	}
	
	public EccAttenceNoticeSet toEccNoticeSetDorm() {
		EccAttenceNoticeSet noticeSet = new EccAttenceNoticeSet(); 
		noticeSet.setId(idDorm);
		noticeSet.setSend(isSendDorm);
		noticeSet.setSendClassMaster(isSendClassMasterDorm);
		noticeSet.setSendGradeMaster(isSendGradeMasterDorm);
		noticeSet.setSendParentMaster(false);
		noticeSet.setType(EccConstants.DORM_ATTENCE_SET_TYPE2);
		noticeSet.setUnitId(unitId);
		return noticeSet;
	}
	
	public EccAttenceNoticeSet toEccNoticeSetInOut() {
		EccAttenceNoticeSet noticeSet = new EccAttenceNoticeSet(); 
		noticeSet.setId(idInOut);
		noticeSet.setSend(isSendInOut);
		noticeSet.setSendClassMaster(false);
		noticeSet.setSendGradeMaster(false);
		noticeSet.setSendParentMaster(isSendParentMasterInOut);
		noticeSet.setType(EccConstants.IN_OUT_ATTENCE_SET_TYPE3);
		noticeSet.setUnitId(unitId);
		return noticeSet;
	}
	
	public String getIdClass() {
		return idClass;
	}

	public void setIdClass(String idClass) {
		this.idClass = idClass;
	}

	public String getIdDorm() {
		return idDorm;
	}

	public void setIdDorm(String idDorm) {
		this.idDorm = idDorm;
	}

	public String getIdInOut() {
		return idInOut;
	}

	public void setIdInOut(String idInOut) {
		this.idInOut = idInOut;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public boolean isSendClass() {
		return isSendClass;
	}

	public void setSendClass(boolean isSendClass) {
		this.isSendClass = isSendClass;
	}

	public boolean isSendDorm() {
		return isSendDorm;
	}

	public void setSendDorm(boolean isSendDorm) {
		this.isSendDorm = isSendDorm;
	}

	public boolean isSendClassMasterClass() {
		return isSendClassMasterClass;
	}

	public void setSendClassMasterClass(boolean isSendClassMasterClass) {
		this.isSendClassMasterClass = isSendClassMasterClass;
	}

	public boolean isSendClassMasterDorm() {
		return isSendClassMasterDorm;
	}

	public void setSendClassMasterDorm(boolean isSendClassMasterDorm) {
		this.isSendClassMasterDorm = isSendClassMasterDorm;
	}

	public boolean isSendGradeMasterClass() {
		return isSendGradeMasterClass;
	}

	public void setSendGradeMasterClass(boolean isSendGradeMasterClass) {
		this.isSendGradeMasterClass = isSendGradeMasterClass;
	}

	public boolean isSendGradeMasterDorm() {
		return isSendGradeMasterDorm;
	}

	public void setSendGradeMasterDorm(boolean isSendGradeMasterDorm) {
		this.isSendGradeMasterDorm = isSendGradeMasterDorm;
	}

	public boolean isSendInOut() {
		return isSendInOut;
	}

	public void setSendInOut(boolean isSendInOut) {
		this.isSendInOut = isSendInOut;
	}

	public boolean isSendParentMasterInOut() {
		return isSendParentMasterInOut;
	}

	public void setSendParentMasterInOut(boolean isSendParentMasterInOut) {
		this.isSendParentMasterInOut = isSendParentMasterInOut;
	}

	public Integer getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}

}
