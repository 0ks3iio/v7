package net.zdsoft.datareport.data.enums;

public enum PlaceholderEnum {
	
	/**
	 * 占位符，当前学年
	 * @return 
	 */
	ACADYEAR(1, "{acadyear}"), 
	/**
	 * 占位符，当前学期
	 */
	SEMESTER(2, "{semester}"); 
	
	private String matchText;
	private Integer value;
	
	private PlaceholderEnum(Integer value, String matchText) {
		this.value = value;
		this.matchText = matchText;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getMatchText() {
		return matchText;
	}

	public void setMatchText(String matchText) {
		this.matchText = matchText;
	}
	
	
	
}
