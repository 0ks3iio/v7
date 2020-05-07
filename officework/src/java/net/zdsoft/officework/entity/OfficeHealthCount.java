package net.zdsoft.officework.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="office_health_count")
public class OfficeHealthCount extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	private String unitId;
	/**
	 * 
	 */
	private String ownerId;
	private String ownerType;
	/**
	 * 1:统计的是一天的数据，2：统计的是一小时的数据
	 */
	private Integer type;
	/**
	 * 日期
	 */
	@Column(name="check_date")
	private Date date;
	/**
	 * 小时（1、2、3...）
	 */
	private Integer hour;
	/**
	 * 步数
	 */
	private Integer step;
	/**
	 * 距离
	 */
	private Double distance;
	/**
	 * 卡路里
	 */
	private Double calorie;
	/**
	 * 
	 */
	private Date creationTime;
	
	private String sourceType;

	/**
	 * 学生排名
	 */
	@Transient
	private Integer stuRank; 

	/**
	 * 图片路径
	 */
	@Transient
	private String photoUrl;
	
	/**
	 * 
	 */
	@Transient
	private String dateStr;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	/**
	 * 设置1:统计的是一天的数据，2：统计的是一小时的数据
	 */
	public void setType(Integer type){
		this.type = type;
	}
	/**
	 * 获取1:统计的是一天的数据，2：统计的是一小时的数据
	 */
	public Integer getType(){
		return this.type;
	}
	/**
	 * 设置
	 */
	public void setDate(Date date){
		this.date = date;
	}
	/**
	 * 获取
	 */
	public Date getDate(){
		return this.date;
	}
	/**
	 * 设置
	 */
	public void setHour(Integer hour){
		this.hour = hour;
	}
	/**
	 * 获取
	 */
	public Integer getHour(){
		return this.hour;
	}
	/**
	 * 设置
	 */
	public void setStep(Integer step){
		this.step = step;
	}
	/**
	 * 获取
	 */
	public Integer getStep(){
		return this.step;
	}
	/**
	 * 设置
	 */
	public void setDistance(Double distance){
		this.distance = distance;
	}
	/**
	 * 获取
	 */
	public Double getDistance(){
		return this.distance;
	}
	/**
	 * 设置
	 */
	public void setCalorie(Double calorie){
		this.calorie = calorie;
	}
	/**
	 * 获取
	 */
	public Double getCalorie(){
		return this.calorie;
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
		return OfficeHealthCount.class.getSimpleName();
	}
	public Integer getStuRank() {
		return stuRank;
	}
	public void setStuRank(Integer stuRank) {
		this.stuRank = stuRank;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
}