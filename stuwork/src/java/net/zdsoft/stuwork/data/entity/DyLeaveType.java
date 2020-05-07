package net.zdsoft.stuwork.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="office_leave_type")
public class DyLeaveType extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private String unitId;
	/**
	 * 适用类型：1:学生，2：老师
	 */
	private Integer state;
	/**
	 * 
	 */
	private int isDeleted;
	
	
	/**
	 * 设置
	 */
	public void setName(String name){
		this.name = name;
	}
	/**
	 * 获取
	 */
	public String getName(){
		return this.name;
	}
	/**
	 * 设置
	 */
	public void setUnitId(String unitId){
		this.unitId = unitId;
	}
	/**
	 * 获取
	 */
	public String getUnitId(){
		return this.unitId;
	}
	/**
	 * 设置适用类型：1:学生，2：老师
	 */
	public void setState(Integer state){
		this.state = state;
	}
	/**
	 * 获取适用类型：1:学生，2：老师
	 */
	public Integer getState(){
		return this.state;
	}
	/**
	 * 设置
	 */
	public void setIsDeleted(int isDeleted){
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取
	 */
	public int getIsDeleted(){
		return this.isDeleted;
	}
	@Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return "dyLeaveType";
	}

}
