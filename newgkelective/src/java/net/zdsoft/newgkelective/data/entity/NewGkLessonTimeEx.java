package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "newgkelective_lesson_time_ex")
public class NewGkLessonTimeEx extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private String arrayItemId;
	private String scourceTypeId;
	/**
	 * 01:上课时间 02:教师安排
	 */
	private String scourceType;
	private Integer dayOfWeek; // 星期
	private String periodInterval; // 时间段
	private Integer period; // 节次
	/**
	 * 上课时间设置参数 01:不排课02:必排 03：连堂指定时间  04:教师在其他年级 的上课时间 05:来自教师组的禁排时间
	 */
	private String timeType;
	private Date creationTime;
	private Date modifyTime;

	

	public String getArrayItemId() {
		return arrayItemId;
	}

	public void setArrayItemId(String arrayItemId) {
		this.arrayItemId = arrayItemId;
	}

	public String getScourceTypeId() {
		return scourceTypeId;
	}

	public void setScourceTypeId(String scourceTypeId) {
		this.scourceTypeId = scourceTypeId;
	}

	public String getScourceType() {
		return scourceType;
	}

	public void setScourceType(String scourceType) {
		this.scourceType = scourceType;
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

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
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

	@Override
	public String fetchCacheEntitName() {
		return "newGkLessonTimeEx";
	}

}
