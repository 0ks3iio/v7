package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CGCourse")
public class CGCourse implements Serializable {

	//COMPULSORY_COURSE: 基础必修课，比如语数外；
	//MAJOR_COURSE：选中的3门参加高考的课
	
	public enum CourseType {COMPULSORY_COURSE, MAJOR_COURSE, MINOR_COURSE, OTHER_COURSE}
	
	public static final int WEEK_TYPE_ODD = 1;
	public static final int WEEK_TYPE_EVEN = -1;
	public static final int WEEK_TYPE_NORMAL = 2;
	
	private String code;			//如：数学，物理A，化学B，体育，美术
	private int minWorkingDaySize;	// 周课时
	private CourseType type; 		//必修（语数外），选考（高考科目），学考（合格考科目），其它（体育美术等）
	
	private int   	      isBiWeekly; //是否单双周:1是单周，-1是双周，2是其它。
	// 返回结果用
	private String subjectId;
	/**
	 * A:选考 B:学考 O:行政班 J:教学班(暂时语数英)
	 * 文理用到：A:选考 O:行政班 J:教学班(暂时语数英)
	 */
	private String subjectType;
	
	/**
	 * 文理分科的时候 才会用到，科目类型：2:理科(物化生)3:文科(历地政)4:语数英
	 */
	private String groupType;
	
	/**
	 * 连排次数
	 */
	private Integer times;  
	/**
	 * 每次连排节数
	 */
	private Integer count;
	
	/* 不联排 间隔时间   暂时无用*/
	private Float interval;
	private Integer needRoom;
	//
	private String coupleTimeLimit;
	
	// 绑定的单双周课程 subjectId + subjectType
	private String isBiweeklyCourse;
	
	// 一天之内课时分布
	private String arrangeDay;
	// 半天之内课时分布
	private String arrangeHalfDay;
	// 优先级
	private int priority = 1;
	// 主要用于判断 ，固定排课科目不需要进行自动连排
	private boolean isFixedSubject;
	
	public CourseType getType() {
		return type;
	}
	
	public CGCourse(String code, int minWorkingDaySize, CourseType type, int isBiWeekly, String subjectId,
			Integer times, Integer count) {
		super();
		this.code = code;
		this.minWorkingDaySize = minWorkingDaySize;
		this.type = type;
		this.isBiWeekly = isBiWeekly;
		this.subjectId = subjectId;
		this.times = times;
		this.count = count;
	}

	public CGCourse() {
		super();
	}
	public void setType(CourseType type) {
		this.type = type;
	}
	
	public String getCode() {
		return code;
	}
	public String getSimpleCode() {
		return getSubjectId()+getSimpleSubjectType();
	}
	public void setCode(String code) {
		this.code = code;
	}

	public int getMinWorkingDaySize() {
		return minWorkingDaySize;
	}
	public void setMinWorkingDaySize(int minWorkingDaySize) {
		this.minWorkingDaySize = minWorkingDaySize;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public int getIsBiWeekly() {
		return isBiWeekly;
	}

	public void setIsBiWeekly(int isBiWeekly) {
		this.isBiWeekly = isBiWeekly;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public String getSimpleSubjectType() {
		if(CourseType.OTHER_COURSE.equals(type))
			return "X";
		return getSubjectType();
	}
	

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getGroupCode() {
		if(StringUtils.isNotBlank(getGroupType())) {
			return getGroupType()+"-"+getCode();
		}
		return getCode();
	}
	
	public String getGroupTypeAndSubjectType() {
		return getGroupType()+"-"+getSubjectType();
	}

	public Float getInterval() {
		return interval;
	}

	public void setInterval(Float interval) {
		this.interval = interval;
	}

	public Integer getNeedRoom() {
		return needRoom;
	}

	public void setNeedRoom(Integer needRoom) {
		this.needRoom = needRoom;
	}

	public String getIsBiweeklyCourse() {
		return isBiweeklyCourse;
	}

	public void setIsBiweeklyCourse(String isBiweeklyCourse) {
		this.isBiweeklyCourse = isBiweeklyCourse;
	}

	public String getCoupleTimeLimit() {
		return coupleTimeLimit;
	}

	public void setCoupleTimeLimit(String coupleTimeLimit) {
		this.coupleTimeLimit = coupleTimeLimit;
	}

	public String getArrangeDay() {
		return arrangeDay;
	}

	public void setArrangeDay(String arrangeDay) {
		this.arrangeDay = arrangeDay;
	}

	public String getArrangeHalfDay() {
		return arrangeHalfDay;
	}

	public void setArrangeHalfDay(String arrangeHalfDay) {
		this.arrangeHalfDay = arrangeHalfDay;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isFixedSubject() {
		return isFixedSubject;
	}

	public void setFixedSubject(boolean isFixedSubject) {
		this.isFixedSubject = isFixedSubject;
	}

}
