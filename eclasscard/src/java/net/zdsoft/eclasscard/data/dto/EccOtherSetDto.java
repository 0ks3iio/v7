package net.zdsoft.eclasscard.data.dto;

public class EccOtherSetDto {
	private boolean screenRadio;
	private boolean voiceRadio;
	private boolean doorRadio;
	private String doorDelayTime;
	private Integer loginRadio;
	private String speedValue;
	private Integer faceService;
	private Integer prompt;
	
	private Float pushThreshold;//人脸下发阈值
	private Float checkThreshold;//人脸验证阈值

	public boolean isScreenRadio() {
		return screenRadio;
	}

	public void setScreenRadio(boolean screenRadio) {
		this.screenRadio = screenRadio;
	}

	public boolean isVoiceRadio() {
		return voiceRadio;
	}

	public void setVoiceRadio(boolean voiceRadio) {
		this.voiceRadio = voiceRadio;
	}

	public boolean isDoorRadio() {
		return doorRadio;
	}

	public void setDoorRadio(boolean doorRadio) {
		this.doorRadio = doorRadio;
	}

	public String getDoorDelayTime() {
		return doorDelayTime;
	}

	public void setDoorDelayTime(String doorDelayTime) {
		this.doorDelayTime = doorDelayTime;
	}

	public Integer getLoginRadio() {
		return loginRadio;
	}

	public void setLoginRadio(Integer loginRadio) {
		this.loginRadio = loginRadio;
	}

	public String getSpeedValue() {
		return speedValue;
	}

	public void setSpeedValue(String speedValue) {
		this.speedValue = speedValue;
	}

	public Integer getFaceService() {
		return faceService;
	}

	public void setFaceService(Integer faceService) {
		this.faceService = faceService;
	}

	public Integer getPrompt() {
		return prompt;
	}

	public void setPrompt(Integer prompt) {
		this.prompt = prompt;
	}

	public Float getPushThreshold() {
		return pushThreshold;
	}

	public void setPushThreshold(Float pushThreshold) {
		this.pushThreshold = pushThreshold;
	}

	public Float getCheckThreshold() {
		return checkThreshold;
	}

	public void setCheckThreshold(Float checkThreshold) {
		this.checkThreshold = checkThreshold;
	}
	
	
}
