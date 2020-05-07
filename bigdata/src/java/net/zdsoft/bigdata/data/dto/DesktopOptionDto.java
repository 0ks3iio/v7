package net.zdsoft.bigdata.data.dto;

import java.io.Serializable;

public class DesktopOptionDto implements Serializable{


	private static final long serialVersionUID = -7237291200843403857L;

	private String platformName;
	
	private String logoPath;
	
	private String logoFileName;

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public String getLogoFileName() {
		return logoFileName;
	}

	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}
}
