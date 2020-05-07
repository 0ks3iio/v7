package net.zdsoft.webservice.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "office_work_staff_bind")
public class SuCdStaffBind extends BaseEntity<String> {
	private static final long serialVersionUID = 4160519693417274836L;
	private String corpAccount;
	private String ufId;
	private String userType;
	private int optType;
	private String optNote;
	private String staffName;
	private String staffMobile;
	private int resultCode;
	private String pushSysTime;
	private String serviceId;

	public String getCorpAccount() {
		return corpAccount;
	}

	public void setCorpAccount(String corpAccount) {
		this.corpAccount = corpAccount;
	}

	public String getUfId() {
		return ufId;
	}

	public void setUfId(String ufId) {
		this.ufId = ufId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getOptType() {
		return optType;
	}

	public void setOptType(int optType) {
		this.optType = optType;
	}

	public String getOptNote() {
		return optNote;
	}

	public void setOptNote(String optNote) {
		this.optNote = optNote;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffMobile() {
		return staffMobile;
	}

	public void setStaffMobile(String staffMobile) {
		this.staffMobile = staffMobile;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getPushSysTime() {
		return pushSysTime;
	}

	public void setPushSysTime(String pushSysTime) {
		this.pushSysTime = pushSysTime;
	}


	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "suCdStaffBind";
	}

}
