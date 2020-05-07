package net.zdsoft.desktop.utils;

/**
 * @author shenke date 2017/10/31上午9:42
 */
public enum Protocol {

	HTTP("http"),
	HTTPS("https");

	private String value;

	Protocol(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public boolean matche(String protocol) {
		return this.value.equalsIgnoreCase(protocol);
	}
}
