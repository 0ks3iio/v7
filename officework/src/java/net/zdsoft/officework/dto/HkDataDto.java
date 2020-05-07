package net.zdsoft.officework.dto;

import java.util.Date;

public class HkDataDto {

	private String cardNumber;
	private String unitIdx;
	private String deviceId;
	private String commInfo;
	private String extInfo;
	private int inOut;////进出方向0：无方向，1：进，2：出
	private String eventCode;
	private String picUrl;
	private String picTips;
	/**
	 * 进出校时间
	 */
	private Date inOutTime;

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getUnitIdx() {
		return unitIdx;
	}

	public void setUnitIdx(String unitIdx) {
		this.unitIdx = unitIdx;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getCommInfo() {
		return commInfo;
	}

	public void setCommInfo(String commInfo) {
		this.commInfo = commInfo;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public int getInOut() {
		return inOut;
	}

	public void setInOut(int inOut) {
		this.inOut = inOut;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPicTips() {
		return picTips;
	}

	public void setPicTips(String picTips) {
		this.picTips = picTips;
	}

	public Date getInOutTime() {
		return inOutTime;
	}

	public void setInOutTime(Date inOutTime) {
		this.inOutTime = inOutTime;
	}

	
}
