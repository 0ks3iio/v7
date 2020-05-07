package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="stusys_section_time_set")
public class StusysSectionTimeSet extends BaseEntity<String>{
	private static final long serialVersionUID = 2488471425535078866L;
	
	private String acadyear;
	private Integer semester;
	private String unitId;
	private Integer sectionNumber;
	private String beginTime;
	private String endTime;
	private String userId;
	private String section;
	private Integer isDeleted;
	
	private String periodInterval;//早上，上午，下午，晚上
	private Integer period;//节次
	
	/**
	 * 设置
	 */
	public void setAcadyear(String acadyear){
		this.acadyear = acadyear;
	}
	/**
	 * 获取
	 */
	public String getAcadyear(){
		return this.acadyear;
	}
	/**
	 * 设置
	 */
	public void setSemester(Integer semester){
		this.semester = semester;
	}
	/**
	 * 获取
	 */
	public Integer getSemester(){
		return this.semester;
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
	 * 设置
	 */
	public void setSectionNumber(Integer sectionNumber){
		this.sectionNumber = sectionNumber;
	}
	/**
	 * 获取
	 */
	public Integer getSectionNumber(){
		return this.sectionNumber;
	}
	/**
	 * 设置
	 */
	public void setBeginTime(String beginTime){
		this.beginTime = beginTime;
	}
	/**
	 * 获取
	 */
	public String getBeginTime(){
		return this.beginTime;
	}
	/**
	 * 设置
	 */
	public void setEndTime(String endTime){
		this.endTime = endTime;
	}
	/**
	 * 获取
	 */
	public String getEndTime(){
		return this.endTime;
	}
	/**
	 * 设置
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}
	/**
	 * 获取
	 */
	public String getUserId(){
		return this.userId;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	@Override
	public String fetchCacheEntitName() {
		return this.getClass().getSimpleName();
	}
	public String getPeriodInterval() {
		return periodInterval;
	}
	public void setPeriodInterval(String periodInterval) {
		this.periodInterval = periodInterval;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	
}