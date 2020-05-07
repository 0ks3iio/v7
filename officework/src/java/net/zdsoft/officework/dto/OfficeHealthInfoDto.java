package net.zdsoft.officework.dto;

import java.util.Date;

import javax.persistence.Column;

public class OfficeHealthInfoDto {
	
	
	private String unitId;
	/**
	 * 
	 */
	private String studentId;
	/**
	 * 
	 */
	private String studentCode;
	/**
	 * 日期(若日期为当前日期  则获取的是当时的运动情况)
	 */
	private Date date;
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
	
	private String sourceType;
	
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getStudentCode() {
		return studentCode;
	}
	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public Double getCalorie() {
		return calorie;
	}
	public void setCalorie(Double calorie) {
		this.calorie = calorie;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	
}
