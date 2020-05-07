package net.zdsoft.newgkelective.data.dto;

import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;

public class LessonTimeDto {
	// private String divide_id;
	private Integer weekday;
	private String period_interval;
	private Integer period;
	private Integer is_join;
	/**
	 * divide.openType=01/05：年级总课表不排课，objId可以确定一条time记录；
	 * 						    年级总课表批次记录以objId+levelType+groupType可以确定一条记录；
	 * 						    单科课表记录以objId+levelType确定一条记录
	 * divide.openType=其他值：objId可以确定time记录
	 * 页面参数值同上述说明，数据展示处理及保存时要注意
	 */
	private String objId;
	private String objName;
	private String fullName;
	private String timeType;
	private String groupType;

	private List<NewGkSubjectTime> subTimes;

	public Integer getIs_join() {
		return is_join;
	}

	public void setIs_join(Integer is_join) {
		this.is_join = is_join;
	}

	public Integer getWeekday() {
		return weekday;
	}

	public void setWeekday(Integer weekday) {
		this.weekday = weekday;
	}

	public String getPeriod_interval() {
		return period_interval;
	}

	public void setPeriod_interval(String period_interval) {
		this.period_interval = period_interval;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public List<NewGkSubjectTime> getSubTimes() {
		return subTimes;
	}

	public void setSubTimes(List<NewGkSubjectTime> subTimes) {
		this.subTimes = subTimes;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "LessonTimeDto [weekday=" + weekday + ", period_interval=" + period_interval + ", period=" + period
				+ ", is_join=" + is_join + "]";
	}
}
