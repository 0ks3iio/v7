package net.zdsoft.stuwork.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dy_week_check_item_day")
public class DyWeekCheckItemDay extends BaseEntity<String>{
	
	private static final long serialVersionUID = 1L;
	

	private String schoolId;
	private String itemId;
	private int day;//周几s
	@Transient
	private String dayName;
	@Transient
	private String hasSelect;
	
	public String getHasSelect() {
		return hasSelect;
	}
	public void setHasSelect(String hasSelect) {
		this.hasSelect = hasSelect;
	}
	public String getDayName() {
		return dayName;
	}
	public void setDayName(String dayName) {
		this.dayName = dayName;
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
	public void setDay(int day){
		this.day = day;
	}
	public int getDay(){
		return this.day;
	}
	
	@Override
	public String fetchCacheEntitName() {
		return "dyWeekCheckItemDay";
	}
}