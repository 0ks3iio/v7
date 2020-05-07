/*
* Project: v7
* Author : shenke
* @(#) CustomRole.java Created on 2016-10-13
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 *  
 * @author: shenke
 * @version: 1.0
 * 2016-10-13下午6:56:20
 */
@Entity
@Table(name = "sys_custom_role")
public class CustomRole extends BaseEntity<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String roleName;
	private String roleCode;
	private String subsystems;
	private String orderId;
	private String type;
	private String remark;
	
	@Transient
	private String userIds;
	@Transient
	private String userNames;
	
	
	/**
	 * unit_id 
	 */
	public void setUnitId(String unitId){
		this.unitId = unitId;
	}
	/**
	 * unit_id 
	 */
	public String getUnitId(){
		return this.unitId;
	}
	
	/**
	 * role_name 
	 */
	public void setRoleName(String roleName){
		this.roleName = roleName;
	}
	/**
	 * role_name 
	 */
	public String getRoleName(){
		return this.roleName;
	}
	
	/**
	 * role_code 
	 */
	public void setRoleCode(String roleCode){
		this.roleCode = roleCode;
	}
	/**
	 * role_code 
	 */
	public String getRoleCode(){
		return this.roleCode;
	}
	
	/**
	 * subsystems 
	 */
	public void setSubsystems(String subsystems){
		this.subsystems = subsystems;
	}
	/**
	 * subsystems 
	 */
	public String getSubsystems(){
		return this.subsystems;
	}
	
	/**
	 * order_id 
	 */
	public void setOrderId(String orderId){
		this.orderId = orderId;
	}
	/**
	 * order_id 
	 */
	public String getOrderId(){
		return this.orderId;
	}
	
	/**
	 * type 
	 */
	public void setType(String type){
		this.type = type;
	}
	/**
	 * type 
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 * remark 
	 */
	public void setRemark(String remark){
		this.remark = remark;
	}
	/**
	 * remark 
	 */
	public String getRemark(){
		return this.remark;
	}
	@Override
	public String fetchCacheEntitName() {
		return "customRole";
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getUserNames() {
		return userNames;
	}
	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}
	
	
}
