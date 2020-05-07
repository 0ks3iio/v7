package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="base_class_hour_ex")
public class ClassHourEx extends BaseEntity<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String classHourId;
	
	private Integer dayOfWeek;
	private String periodInterval;
	private Integer period;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	private Integer isDeleted;
	
	@Override
	public String fetchCacheEntitName() {
		return "classHourEx";
	}

	public String getClassHourId() {
		return classHourId;
	}

	public void setClassHourId(String classHourId) {
		this.classHourId = classHourId;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
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

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
