package net.zdsoft.stuwork.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dy_permission")
public class DyPermission extends BaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CLASS_TYPE_NORMAL = "1"; //行政班
	public static final String CLASS_TYPE_TEACH = "2"; //教学班

	public static final String PERMISSION_TYPE_STUWORK = "1"; //德育
	public static final String PERMISSION_TYPE_STUDENT  = "2"; //学籍
	public static final String PERMISSION_TYPE_COURSERECORD  = "3"; //学籍

	private String unitId;
	private String classId;
	private String userId;
	private String classType;
	private String permissionType;
	
	@Transient
	private String userName;


	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
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

	@Override
	public String fetchCacheEntitName() {
		return "dyPermission";
	}

}
