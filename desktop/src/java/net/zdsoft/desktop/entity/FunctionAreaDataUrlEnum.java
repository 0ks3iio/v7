package net.zdsoft.desktop.entity;

public enum FunctionAreaDataUrlEnum {
	/**
	 * 占位符，当前登录者单位ID
	 */
	UNITID(1, "{unitId}"), 
	/**
	 * 占位符，当前登录者用户ID
	 */
	USERID(2, "{userId}"), 
	/**
	 * 占位符，当前登录者基本信息ID
	 */
	OWNERID(3, "{ownerId}"),
	/**
	 * 占位符，当前登录者基本信息ID
	 */
	STUDENTID(4, "{studentId}");
	
	private FunctionAreaDataUrlEnum(int value, String matchText) {
		this.value = value;
		this.matchText = matchText;
	}

	private int value;
	private String matchText;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getMatchText() {
		return matchText;
	}

	public void setMatchText(String matchText) {
		this.matchText = matchText;
	}
}
