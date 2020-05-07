package net.zdsoft.system.dto.common;

public class DomainListDto {
	
	private String unitId; //增加一个单位id
	private String regionAdmin; //域名参数
	private String region; //地区码
	private String regionName; //地区名称
	private String unitName; //单位名称
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getRegionAdmin() {
		return regionAdmin;
	}
	public void setRegionAdmin(String regionAdmin) {
		this.regionAdmin = regionAdmin;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
}
