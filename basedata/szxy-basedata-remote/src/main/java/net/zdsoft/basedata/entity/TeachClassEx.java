package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="base_teach_class_ex")
public class TeachClassEx extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String teachClassId;
	private String placeId;
	private String periodInterval;
	private Integer period;
	private Integer dayOfWeek;
	@Transient
	private String timeStr;
	@Transient
	private String placeName;
	
	public String getTeachClassId() {
		return teachClassId;
	}

	public void setTeachClassId(String teachClassId) {
		this.teachClassId = teachClassId;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getPeriodInterval() {
		return periodInterval;
	}

	public void setPeriodInterval(String periodInterval) {
		this.periodInterval = periodInterval;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	@Override
	public String fetchCacheEntitName() {
		return "teachClassEx";
	}

}
