package net.zdsoft.bigdata.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "bg_sys_user_auth")
public class BgUserAuth extends BaseEntity<String> {

	private static final long serialVersionUID = -1980131445763716399L;
	
	private String userId;

	private int status;

	private int isSuperUser;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date creationTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	@Transient
	private String userName;
	
	@Transient
	private String realName;
	
	@Transient
	private Integer sex;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
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
	
	@Override
	public String fetchCacheEntitName() {
		return "bgUserAuth";
	}

	public int getIsSuperUser() {
		return isSuperUser;
	}

	public void setIsSuperUser(int isSuperUser) {
		this.isSuperUser = isSuperUser;
	}
}
