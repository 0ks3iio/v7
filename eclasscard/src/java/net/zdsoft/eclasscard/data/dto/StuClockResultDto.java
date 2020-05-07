package net.zdsoft.eclasscard.data.dto;


public class StuClockResultDto {
	public static final Integer SUCCESS = 1;
	public static final Integer FAILED = 2;
	public static final Integer WARNING = 3;
	public static final Integer WARNING_LATE = 4;
	private Integer status;//
	private boolean haveStu = true; //是否存在学生信息
	private String stuUserName;
	private String studentId;
	private String stuRealName;
	private String rowOneName;
	private String rowOneValue;
	private String rowTwoName;
	private String rowTwoValue;
	private String rowThreeName;
	private String rowThreeValue;
	private String msg;
	private String ownerType;//1.学生 2.教师
	// 2018.1.2  班牌类型 
	private String type;  
	private String voiceMsg;
	private String clockTime;
	private String token;
	private String showPictrueUrl;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public boolean isHaveStu() {
		return haveStu;
	}

	public void setHaveStu(boolean haveStu) {
		this.haveStu = haveStu;
	}

	public String getStuUserName() {
		return stuUserName;
	}

	public void setStuUserName(String stuUserName) {
		this.stuUserName = stuUserName;
	}

	public String getStuRealName() {
		return stuRealName;
	}

	public void setStuRealName(String stuRealName) {
		this.stuRealName = stuRealName;
	}

	public String getRowOneName() {
		return rowOneName;
	}

	public void setRowOneName(String rowOneName) {
		this.rowOneName = rowOneName;
	}

	public String getRowOneValue() {
		return rowOneValue;
	}

	public void setRowOneValue(String rowOneValue) {
		this.rowOneValue = rowOneValue;
	}

	public String getRowTwoName() {
		return rowTwoName;
	}

	public void setRowTwoName(String rowTwoName) {
		this.rowTwoName = rowTwoName;
	}

	public String getRowTwoValue() {
		return rowTwoValue;
	}

	public void setRowTwoValue(String rowTwoValue) {
		this.rowTwoValue = rowTwoValue;
	}

	public String getRowThreeName() {
		return rowThreeName;
	}

	public void setRowThreeName(String rowThreeName) {
		this.rowThreeName = rowThreeName;
	}

	public String getRowThreeValue() {
		return rowThreeValue;
	}

	public void setRowThreeValue(String rowThreeValue) {
		this.rowThreeValue = rowThreeValue;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getVoiceMsg() {
		return voiceMsg;
	}

	public void setVoiceMsg(String voiceMsg) {
		this.voiceMsg = voiceMsg;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getShowPictrueUrl() {
		return showPictrueUrl;
	}

	public void setShowPictrueUrl(String showPictrueUrl) {
		this.showPictrueUrl = showPictrueUrl;
	}

	public String getClockTime() {
		return clockTime;
	}

	public void setClockTime(String clockTime) {
		this.clockTime = clockTime;
	}
	
}
