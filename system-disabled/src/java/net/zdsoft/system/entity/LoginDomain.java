package net.zdsoft.system.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 保存不同单位的登录域名参数设置
 */
@Table(name = "sys_login_domain")
@Entity
public class LoginDomain extends BaseEntity<String>{

	/**
	 * 默认isdeleted 的值
	 */
	public static final int DEFAULT_IS_DELETED_VALUE = 0;
	
	private String unitId; //增加一个单位id
	private String regionAdmin; //域名参数
	private String region; //地区码
	private Integer isDeleted; //是否软删
	
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
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	@Override
	public String fetchCacheEntitName() {
		return "loginDomain";
	}

}
