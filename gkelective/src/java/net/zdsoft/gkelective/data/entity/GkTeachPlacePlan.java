package net.zdsoft.gkelective.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="gkelective_teach_place_plan")
public class GkTeachPlacePlan extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String subjectArrangeId;
	private String roundsId;
	private String acadyear;//课程开始学年
	private String semester;//课程开始学期
	private Date startTime;//课程开始时间
	private Date endTime;//课程开始时间
	private int dayOfWeek;//课程开始星期
	private int weekOfWorktime;//课程开始周次
	private int dayOfWeek2;//课程结束星期
	private int weekOfWorktime2;//课程结束周次
	private int isDeleted;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	private int step;
	
	//辅助
	@Transient
	private GkRounds gkRounds;
	@Transient 
	private String timeStr;//第1周，星期一
	@Transient 
	private String timeStr2;//第1周，星期一
	@Transient
	private boolean canDelete;
	public GkRounds getGkRounds() {
		return gkRounds;
	}

	public void setGkRounds(GkRounds gkRounds) {
		this.gkRounds = gkRounds;
	}

	public String getSubjectArrangeId() {
		return subjectArrangeId;
	}

	public void setSubjectArrangeId(String subjectArrangeId) {
		this.subjectArrangeId = subjectArrangeId;
	}

	public String getRoundsId() {
		return roundsId;
	}

	public void setRoundsId(String roundsId) {
		this.roundsId = roundsId;
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getWeekOfWorktime() {
		return weekOfWorktime;
	}

	public void setWeekOfWorktime(int weekOfWorktime) {
		this.weekOfWorktime = weekOfWorktime;
	}

	public int getDayOfWeek2() {
		return dayOfWeek2;
	}

	public void setDayOfWeek2(int dayOfWeek2) {
		this.dayOfWeek2 = dayOfWeek2;
	}

	public int getWeekOfWorktime2() {
		return weekOfWorktime2;
	}

	public void setWeekOfWorktime2(int weekOfWorktime2) {
		this.weekOfWorktime2 = weekOfWorktime2;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
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

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public String fetchCacheEntitName() {
		return "gkTeachPlacePlan";
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public String getTimeStr2() {
		return timeStr2;
	}

	public void setTimeStr2(String timeStr2) {
		this.timeStr2 = timeStr2;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

}
