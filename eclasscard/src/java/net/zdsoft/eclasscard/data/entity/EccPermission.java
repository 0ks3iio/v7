package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="eclasscard_permission")
public class EccPermission  extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	private String eccName;
	private String userId;
	private String unitId;
	@Transient
	private String userName;
	
	@Override
	public String fetchCacheEntitName() {
		return "eclasscardPermission";
	}

	public String getEccName() {
		return eccName;
	}

	public void setEccName(String eccName) {
		this.eccName = eccName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
