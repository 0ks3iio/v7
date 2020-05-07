package net.zdsoft.stuwork.data.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name = "dy_week_check_item")
public class DyWeekCheckItem extends BaseEntity<String>{
	
	private static final long serialVersionUID = 1L;

	private String schoolId;
	private int type;//1卫生2纪律
	private String itemName;
	private float totalScore;
	private int hasTotalScore;
	private int orderId;
	
	@Transient
	private Set<String> days = new HashSet<String>();
	@Transient
	private Set<String> roles = new HashSet<String>();
	@Transient
	private List<DyWeekCheckItemDay> itemDays = new ArrayList<DyWeekCheckItemDay>();
	@Transient
	private List<DyWeekCheckItemRole> itemRoles = new ArrayList<DyWeekCheckItemRole>();

	public List<DyWeekCheckItemDay> getItemDays() {
		return itemDays;
	}
	public void setItemDays(List<DyWeekCheckItemDay> itemDays) {
		this.itemDays = itemDays;
	}
	public List<DyWeekCheckItemRole> getItemRoles() {
		return itemRoles;
	}
	public void setItemRoles(List<DyWeekCheckItemRole> itemRoles) {
		this.itemRoles = itemRoles;
	}
	public void setSchoolId(String schoolId){
		this.schoolId = schoolId;
	}
	public Set<String> getDays() {
		return days;
	}
	public void setDays(Set<String> days) {
		this.days = days;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	public String getSchoolId(){
		return this.schoolId;
	}
	public void setType(int type){
		this.type = type;
	}
	public int getType(){
		return this.type;
	}
	public void setItemName(String itemName){
		this.itemName = itemName;
	}
	public String getItemName(){
		return this.itemName;
	}
	public void setTotalScore(float totalScore){
		this.totalScore = totalScore;
	}
	public float getTotalScore(){
		return this.totalScore;
	}
	public void setHasTotalScore(int hasTotalScore){
		this.hasTotalScore = hasTotalScore;
	}
	public int getHasTotalScore(){
		return this.hasTotalScore;
	}
	public void setOrderId(int orderId){
		this.orderId = orderId;
	}
	public int getOrderId(){
		return this.orderId;
	}
	@Override
	public String fetchCacheEntitName() {
		return "dyWeekCheckItem";
	}
}