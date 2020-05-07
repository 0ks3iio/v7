package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 科目周课表
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "newgkelective_subject_time")
public class NewGkSubjectTime extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	/**
	 * newgkelective_array_item.id
	 */
	private String arrayItemId;
	private String subjectId;
	private Integer period;
	/**
	 * O:行政班课程(空 行政班课程需要确认的下最好用O) A:选考 B:学考 J:教学班
	 */
	private String subjectType;
	/**
	 * 单双周  1单周 2 双周 3正常
	 */
	private Integer firstsdWeek=3;
	/**
	 * 关联的单双周科目id
	 */
	private String firstsdWeekSubject;
	
	//--周连排次数
	private Integer weekRowNumber;
	//--周连排节次
	private Integer weekRowPeriod;
	//连排时间限制 0:无限制 1：限制
	private String weekRowType;
	
	
	/*课时分配 01：无限制*/
	private String arrangeDay;
	/*半天课时分配  01：无限制*/
	private String arrangeHalfDay;
	/*优先级 1:优先  0默认*/
	private String arrangeFrist;
	
	private Date creationTime;
	private Date modifyTime;
	/**
	 * 1:7选3或者6选3(物化生历地政技)  2:理科(物化生)3:文科(历地政)4:语数英
	 */
//	private String groupType;
	/**
	 * 默认打卡这个字段不在体现
	 */
	@Deprecated
	private Integer punchCard;
	private Float timeInterval;
	// 0.不需要教室 1.需要教室
	private Integer isNeed;
	// 是否跟随 组合班 上课，只有在 3+1+2 组合固定模式时才有效
	private Integer followZhb;
	/**
	 * 连排课程放在某一天，或几天
	 */
	private String coupleDays;
	@Transient
	private String subjectName;
	@Transient
	private boolean isFixedSubject;

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getArrayItemId() {
		return arrayItemId;
	}

	public void setArrayItemId(String arrayItemId) {
		this.arrayItemId = arrayItemId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
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

	public Integer getPunchCard() {
		return punchCard;
	}

	public void setPunchCard(Integer punchCard) {
		this.punchCard = punchCard;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkSubjectTime";
	}

	public Integer getFirstsdWeek() {
		return firstsdWeek;
	}

	public void setFirstsdWeek(Integer firstsdWeek) {
		this.firstsdWeek = firstsdWeek;
	}

	public Integer getWeekRowNumber() {
		return weekRowNumber;
	}

	public void setWeekRowNumber(Integer weekRowNumber) {
		this.weekRowNumber = weekRowNumber;
	}

	public Integer getWeekRowPeriod() {
		return weekRowPeriod;
	}

	public void setWeekRowPeriod(Integer weekRowPeriod) {
		this.weekRowPeriod = weekRowPeriod;
	}


	public Float getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(Float timeInterval) {
		this.timeInterval = timeInterval;
	}

	public Integer getIsNeed() {
		return isNeed;
	}

	public void setIsNeed(Integer isNeed) {
		this.isNeed = isNeed;
	}

	public String getCoupleDays() {
		return coupleDays;
	}

	public void setCoupleDays(String coupleDays) {
		this.coupleDays = coupleDays;
	}

	public String getFirstsdWeekSubject() {
		return firstsdWeekSubject;
	}

	public void setFirstsdWeekSubject(String firstsdWeekSubject) {
		this.firstsdWeekSubject = firstsdWeekSubject;
	}

	public String getWeekRowType() {
		return weekRowType;
	}

	public void setWeekRowType(String weekRowType) {
		this.weekRowType = weekRowType;
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

	public String getArrangeFrist() {
		return arrangeFrist;
	}

	public void setArrangeFrist(String arrangeFrist) {
		this.arrangeFrist = arrangeFrist;
	}

	public boolean isFixedSubject() {
		return isFixedSubject;
	}

	public void setFixedSubject(boolean isFixedSubject) {
		this.isFixedSubject = isFixedSubject;
	}

	public Integer getFollowZhb() {
		return followZhb;
	}

	public void setFollowZhb(Integer followZhb) {
		this.followZhb = followZhb;
	}

}
