package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="eclasscard_notice_user")
public class EccAttenceNoticeUser extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	private String unitId;
	private String type;
	private String userId;

	@Transient
	private String userName;
	
	@Override
	public String fetchCacheEntitName() {
		return "eclasscardNoticeUser";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
