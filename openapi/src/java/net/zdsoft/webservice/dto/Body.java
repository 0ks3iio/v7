package net.zdsoft.webservice.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement(name = "BODY")
public class Body {
	String corpAccount;
	List<StaffInfo> staffInfo = Lists.newArrayList();

	public static class StaffInfo {
		String ufId;
		String userType;
		int optType;
		String optNote;
		String staffName;
		String staffMobile;
		int resultCode;
		String resultMsg;

		public String getUfId() {
			return ufId;
		}
		@XmlElement(name = "UFID")
		public void setUfId(String ufId) {
			this.ufId = ufId;
		}

		public String getUserType() {
			return userType;
		}
		@XmlElement(name = "USERTYPE")
		public void setUserType(String userType) {
			this.userType = userType;
		}

		public int getOptType() {
			return optType;
		}
		@XmlElement(name = "OPTYPE")
		public void setOptType(int optType) {
			this.optType = optType;
		}

		public String getOptNote() {
			return optNote;
		}
		@XmlElement(name = "OPNOTE")
		public void setOptNote(String optNote) {
			this.optNote = optNote;
		}

		public String getStaffName() {
			return staffName;
		}
		@XmlElement(name = "STAFFNAME")
		public void setStaffName(String staffName) {
			this.staffName = staffName;
		}

		public String getStaffMobile() {
			return staffMobile;
		}
		@XmlElement(name = "STAFFMOBILE")
		public void setStaffMobile(String staffMobile) {
			this.staffMobile = staffMobile;
		}
		public int getResultCode() {
			return resultCode;
		}
		public void setResultCode(int resultCode) {
			this.resultCode = resultCode;
		}
		public String getResultMsg() {
			return resultMsg;
		}
		public void setResultMsg(String resultMsg) {
			this.resultMsg = resultMsg;
		}

	}

	public String getCorpAccount() {
		return corpAccount;
	}

	@XmlElement(name = "CORPACCOUNT")
	public void setCorpAccount(String corpAccount) {
		this.corpAccount = corpAccount;
	}

	public List<StaffInfo> getStaffInfo() {
		return staffInfo;
	}
	@XmlElementWrapper(name="STAFFLIST")
	@XmlElement(name = "STAFFINFO")
	public void setStaffInfo(List<StaffInfo> staffInfo) {
		this.staffInfo = staffInfo;
	}

}
