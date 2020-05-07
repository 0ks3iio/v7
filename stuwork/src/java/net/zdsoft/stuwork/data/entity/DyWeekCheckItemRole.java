package net.zdsoft.stuwork.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dy_week_check_item_role")
public class DyWeekCheckItemRole extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;


	private String schoolId;
	private String itemId;
	private String roleType;
	
	@Transient
	private String roleName;
	@Transient
	private String hasSelect;
	
	public String getHasSelect() {
		return hasSelect;
	}
	public void setHasSelect(String hasSelect) {
		this.hasSelect = hasSelect;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public void setSchoolId(String schoolId){
		this.schoolId = schoolId;
	}
	public String getSchoolId(){
		return this.schoolId;
	}
	public void setItemId(String itemId){
		this.itemId = itemId;
	}
	public String getItemId(){
		return this.itemId;
	}
	public void setRoleType(String roleType){
		this.roleType = roleType;
	}
	public String getRoleType(){
		return this.roleType;
	}
	@Override
	public String fetchCacheEntitName() {
		return "dyWeekCheckItemRole";
	}
}