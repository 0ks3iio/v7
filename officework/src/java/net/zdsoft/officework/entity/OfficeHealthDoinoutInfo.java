package net.zdsoft.officework.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="office_health_doinout_info")
public class OfficeHealthDoinoutInfo extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String unitId;
	/**
	 * 
	 */
	private String studentId;
	/**
	 * 手环id
	 */
	private String wristbandId;
	/**
	 * 0 出校 1 进校
	 */
	private Integer inOut;
	/**
	 * 进出校时间
	 */
	private Date inOutTime;
	/**
	 * 
	 */
	private Date creationTime;
	
	private String sourceType;
	
	private String picUrl;
	
	
	//学号
	@Transient
	private String studentCode;
	@Transient
	private String picTips;;

	public String getStudentCode() {
		return studentCode;
	}
	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}
	public Date getInOutTime() {
		return inOutTime;
	}
	public void setInOutTime(Date inOutTime) {
		this.inOutTime = inOutTime;
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
	public void setStudentId(String studentId){
		this.studentId = studentId;
	}
	/**
	 * 获取
	 */
	public String getStudentId(){
		return this.studentId;
	}
	
	public String getWristbandId() {
		return wristbandId;
	}
	public void setWristbandId(String wristbandId) {
		this.wristbandId = wristbandId;
	}
	/**
	 * 设置0 出校 1 进校
	 */
	public void setInOut(Integer inOut){
		this.inOut = inOut;
	}
	/**
	 * 获取0 出校 1 进校
	 */
	public Integer getInOut(){
		return this.inOut;
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
	
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	public String getPicUrl() {
		return picUrl;
	}
	
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public String getPicTips() {
		return picTips;
	}
	
	public void setPicTips(String picTips) {
		this.picTips = picTips;
	}
	@Override
	public String fetchCacheEntitName() {
		return OfficeHealthDoinoutInfo.class.getSimpleName();
	}
}