package net.zdsoft.officework.constant;

public enum HKPicTips {
	DS_K1T600MF("DS_K1T600MF","288x300");
	private String key;
	private String value;
	
	private HKPicTips(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
