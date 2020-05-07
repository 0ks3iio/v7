package net.zdsoft.stuwork.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
	
@Entity
@Table(name = "dy_week_check_result_submit")
public class DyWeekCheckResultSubmit extends BaseEntity<String>{
	
	private static final long serialVersionUID = 1L;
	private String schoolId;
	private String acadyear;
	private String semester;
	private String roleType;
	private int week;
	private int day;
	private Date checkDate;
	private boolean isSubmit;
	
	public void setSchoolId(String schoolId){
		this.schoolId = schoolId;
	}
	public String getSchoolId(){
		return this.schoolId;
	}
	public void setAcadyear(String acadyear){
		this.acadyear = acadyear;
	}
	public String getAcadyear(){
		return this.acadyear;
	}
	public void setSemester(String semester){
		this.semester = semester;
	}
	public String getSemester(){
		return this.semester;
	}
	public void setRoleType(String roleType){
		this.roleType = roleType;
	}
	public String getRoleType(){
		return this.roleType;
	}
	public void setWeek(int week){
		this.week = week;
	}
	public int getWeek(){
		return this.week;
	}
	public void setDay(int day){
		this.day = day;
	}
	public int getDay(){
		return this.day;
	}
	public void setCheckDate(Date checkDate){
		this.checkDate = checkDate;
	}
	public Date getCheckDate(){
		return this.checkDate;
	}
	public void setIsSubmit(boolean isSubmit){
		this.isSubmit = isSubmit;
	}
	public boolean getIsSubmit(){
		return this.isSubmit;
	}
	@Override
	public String fetchCacheEntitName() {
		return "dyWeekCheckResultSubmit";
	}
}