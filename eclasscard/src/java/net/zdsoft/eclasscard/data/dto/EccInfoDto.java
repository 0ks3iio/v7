package net.zdsoft.eclasscard.data.dto;

import net.zdsoft.eclasscard.data.entity.EccInfo;

public class EccInfoDto {

	private EccInfo eccInfo = new EccInfo();
	private String permisionUserIds;
	private String permisionUserNames;

	public String getPermisionUserIds() {
		return permisionUserIds;
	}

	public void setPermisionUserIds(String permisionUserIds) {
		this.permisionUserIds = permisionUserIds;
	}

	public String getPermisionUserNames() {
		return permisionUserNames;
	}

	public void setPermisionUserNames(String permisionUserNames) {
		this.permisionUserNames = permisionUserNames;
	}

	public EccInfo getEccInfo() {
		return eccInfo;
	}

	public void setEccInfo(EccInfo eccInfo) {
		this.eccInfo = eccInfo;
	}
}
