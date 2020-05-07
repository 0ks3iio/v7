package net.zdsoft.power.dto;

import net.zdsoft.power.entity.SysPower;

/**
 * @author yangsj  2018年6月8日上午11:11:00
 */
public class ApPowerDto {

	private SysPower sysPower;
	private String sourceName;
	private String isEmpower; //是否已经授权
	private String description; //描述
	public SysPower getSysPower() {
		return sysPower;
	}
	public void setSysPower(SysPower sysPower) {
		this.sysPower = sysPower;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getIsEmpower() {
		return isEmpower;
	}
	public void setIsEmpower(String isEmpower) {
		this.isEmpower = isEmpower;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
