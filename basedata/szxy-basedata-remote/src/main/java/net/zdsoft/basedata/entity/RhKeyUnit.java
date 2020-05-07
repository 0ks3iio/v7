package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
/**
 * 
 * 荣厚数据接口
 *
 */
@Entity
@Table(name = "base_unit_rh_key")
public class RhKeyUnit extends BaseEntity<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ukey;
	private String unitId;
	private String serverIp;//单点 服务器地址
	private String dbUrl;
	private String dbUsername;
	private String dbPassword;
	

	@Override
	public String fetchCacheEntitName() {
		return "rhKeyUnit";
	}

	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	
	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
}
