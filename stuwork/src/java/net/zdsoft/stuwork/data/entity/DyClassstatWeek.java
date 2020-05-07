package net.zdsoft.stuwork.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dy_classstat_week")
public class DyClassstatWeek  extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String schoolId;
	/**
	 * 
	 */
	private String acadyear;
	/**
	 * 
	 */
	private String semester;
	/**
	 * 
	 */
	private String classId;
	/**
	 * 
	 */
	private Integer week;
	/**
	 * 
	 */
	private Boolean isHealthExcellen;
	private String section;
	
	@Transient
	private String classCode;
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	/**
	 * 
	 */
	private Float healthScore;
	/**
	 * 
	 */
	private Integer healthRank;
	/**
	 * 
	 */
	private Boolean isDisciplineExcellen;
	/**
	 * 
	 */
	private Float disciplineScore;
	/**
	 * 
	 */
	private Integer disciplineRank;
	/**
	 * 
	 */
	private Date creationTime;

	/**
	 * 设置
	 */
	public void setSchoolId(String schoolId){
		this.schoolId = schoolId;
	}
	/**
	 * 获取
	 */
	public String getSchoolId(){
		return this.schoolId;
	}
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
	public void setSemester(String semester){
		this.semester = semester;
	}
	/**
	 * 获取
	 */
	public String getSemester(){
		return this.semester;
	}
	/**
	 * 设置
	 */
	public void setClassId(String classId){
		this.classId = classId;
	}
	/**
	 * 获取
	 */
	public String getClassId(){
		return this.classId;
	}
	/**
	 * 设置
	 */
	public void setWeek(Integer week){
		this.week = week;
	}
	/**
	 * 获取
	 */
	public Integer getWeek(){
		return this.week;
	}
	/**
	 * 设置
	 */
	public void setIsHealthExcellen(Boolean isHealthExcellen){
		this.isHealthExcellen = isHealthExcellen;
	}
	/**
	 * 获取
	 */
	public Boolean getIsHealthExcellen(){
		return this.isHealthExcellen;
	}
	/**
	 * 设置
	 */
	public void setHealthScore(Float healthScore){
		this.healthScore = healthScore;
	}
	/**
	 * 获取
	 */
	public Float getHealthScore(){
		return this.healthScore;
	}
	/**
	 * 设置
	 */
	public void setHealthRank(Integer healthRank){
		this.healthRank = healthRank;
	}
	/**
	 * 获取
	 */
	public Integer getHealthRank(){
		return this.healthRank;
	}
	/**
	 * 设置
	 */
	public void setIsDisciplineExcellen(Boolean isDisciplineExcellen){
		this.isDisciplineExcellen = isDisciplineExcellen;
	}
	/**
	 * 获取
	 */
	public Boolean getIsDisciplineExcellen(){
		return this.isDisciplineExcellen;
	}
	/**
	 * 设置
	 */
	public void setDisciplineScore(Float disciplineScore){
		this.disciplineScore = disciplineScore;
	}
	/**
	 * 获取
	 */
	public Float getDisciplineScore(){
		return this.disciplineScore;
	}
	/**
	 * 设置
	 */
	public void setDisciplineRank(Integer disciplineRank){
		this.disciplineRank = disciplineRank;
	}
	/**
	 * 获取
	 */
	public Integer getDisciplineRank(){
		return this.disciplineRank;
	}
	/**
	 * 设置
	 */
	public void setCreationTime(Date creationTime){
		this.creationTime = creationTime;
	}
	/**
	 * 获取
	 */
	public Date getCreationTime(){
		return this.creationTime;
	}
	@Override
	public String fetchCacheEntitName() {
		return "dyClassstatWeek";
	}

}
