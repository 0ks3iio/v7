package net.zdsoft.desktop.utils;

/**
 * @author shenke date 2017/10/31上午9:50
 */
public enum Version {

	_7("7");

	private String version;

	Version(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}
}
