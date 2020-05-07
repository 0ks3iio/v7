package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 6.0的账号绑定 移到 7.0中 
 * @author yangsj
 *
 */
@Entity
@Table(name = "sys_user_bind")
public class SysUserBind {
	@Id
	private String remoteUserId;
	private String userId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private String remoteUsername;
	private String remotePassword;
	public String getRemoteUserId() {
		return remoteUserId;
	}
	public void setRemoteUserId(String remoteUserId) {
		this.remoteUserId = remoteUserId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getRemoteUsername() {
		return remoteUsername;
	}
	public void setRemoteUsername(String remoteUsername) {
		this.remoteUsername = remoteUsername;
	}
	public String getRemotePassword() {
		return remotePassword;
	}
	public void setRemotePassword(String remotePassword) {
		this.remotePassword = remotePassword;
	}
}
