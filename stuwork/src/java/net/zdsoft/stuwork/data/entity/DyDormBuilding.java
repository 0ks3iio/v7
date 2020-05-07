package net.zdsoft.stuwork.data.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="dy_dorm_building")
public class DyDormBuilding extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String name;
	@Transient
	private List<DyDormCheckRole> checkRoleList;
	@Transient
	private String userIds;
	@Transient
	private String userNames;
	
	
	public String getUserNames() {
		return userNames;
	}
	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public List<DyDormCheckRole> getCheckRoleList() {
		return checkRoleList;
	}
	public void setCheckRoleList(List<DyDormCheckRole> checkRoleList) {
		this.checkRoleList = checkRoleList;
	}
	@Override
	public String fetchCacheEntitName() {
		return "getBuilding";
	}

}
