package net.zdsoft.system.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * server region 对照表 （不同地区对应着不同的域名）</br>
 * 对于域名来说，domain 是必填字段，port protocol contextPath 若不填写则取base_server 数据
 * @author ke_shen@126.com
 * @since 2018/2/2 下午4:51
 */
@Table(name = "sys_server_region")
@Entity
public class ServerRegion extends BaseEntity<String> {


	private int serverId;
	@Transient
	private String serverName;
	private String region;
	private String regionName;

	private String domain;

	private int port;
	private String protocol;
	private String contextPath;
	private String indexUrl;
	
	private String unitId; //增加一个单位id

	public String getIndexUrl() {
		return indexUrl;
	}

	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
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

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public String fetchCacheEntitName() {
		return "serverRegion";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
}
