package net.zdsoft.gkelective.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="timetable_limit_arrang")
public class GkTimetableLimitArrang extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String acadyear;
	
	private String semester;
	@Column(name="limit_id")
	private String limitId;
	@Column(name="limit_type")
	private String limitType;
	
	@Column(name="unit_id")
	private String unitId;
	
	private String period;
	@Column(name="period_interval")
	private String periodInterval;
	
	private String weekday;
	
	private String remark;
	@Column(name="arrang_id")
	private String arrangId;
	
	private int isUsing = 1;
	
	public int getIsUsing() {
		return isUsing;
	}

	public void setIsUsing(int isUsing) {
		this.isUsing = isUsing;
	}

	@Override
	public String fetchCacheEntitName() {
		return "gktimetablelimitarrang";
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getLimitId() {
		return limitId;
	}

	public void setLimitId(String limitId) {
		this.limitId = limitId;
	}

	public String getLimitType() {
		return limitType;
	}

	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPeriodInterval() {
		return periodInterval;
	}

	public void setPeriodInterval(String periodInterval) {
		this.periodInterval = periodInterval;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getArrangId() {
		return arrangId;
	}

	public void setArrangId(String arrangId) {
		this.arrangId = arrangId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
	

}
