package net.zdsoft.activity.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="famdear_permission")
public class FamilyDearPermission extends BaseEntity<String>{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String unitId;
	 private String userIds;
	 private String permissionType;
	 private Date creationTime;
	 private Date ModifyTime;
	 @Transient
	 private String userNames;
	 
	 
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getPermissionType() {
		return permissionType;
	}
	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getModifyTime() {
		return ModifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		ModifyTime = modifyTime;
	}
	public String getUserNames() {
		return userNames;
	}
	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}
	@Override
	public String fetchCacheEntitName() {
		return "getFamilyDearPermission";
	}
	 
}
