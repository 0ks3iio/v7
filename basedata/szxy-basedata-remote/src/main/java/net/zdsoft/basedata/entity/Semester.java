package net.zdsoft.basedata.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.utils.SUtils;

@Entity
@Table(name="base_semester")
public class Semester extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	private String acadyear;
	private Integer semester;
	@JSONField(format="yyyy-MM-dd")
	private Date workBegin;
	@JSONField(format="yyyy-MM-dd")
	private Date workEnd;
	@JSONField(format="yyyy-MM-dd")
	private Date semesterBegin;
	@JSONField(format="yyyy-MM-dd")
	private Date semesterEnd;
	private Integer eduDays;
	private Integer amPeriods;
	private Integer pmPeriods;
	private Integer nightPeriods;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private Integer isDeleted;
	private Integer eventSource;
	private Integer classHour;
	private Date registerDate;
	private Integer week;
	private Integer mornPeriods;

	@Override
	public String fetchCacheEntitName() {
		return "semester";
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public Date getWorkBegin() {
		return workBegin;
	}

	public void setWorkBegin(Date workBegin) {
		this.workBegin = workBegin;
	}

	public Date getWorkEnd() {
		return workEnd;
	}

	public void setWorkEnd(Date workEnd) {
		this.workEnd = workEnd;
	}

	public Date getSemesterBegin() {
		return semesterBegin;
	}

	public void setSemesterBegin(Date semesterBegin) {
		this.semesterBegin = semesterBegin;
	}

	public Date getSemesterEnd() {
		return semesterEnd;
	}

	public void setSemesterEnd(Date semesterEnd) {
		this.semesterEnd = semesterEnd;
	}

	public Integer getEduDays() {
		return eduDays;
	}

	public void setEduDays(Integer eduDays) {
		this.eduDays = eduDays;
	}

	public Integer getAmPeriods() {
		return amPeriods;
	}

	public void setAmPeriods(Integer amPeriods) {
		this.amPeriods = amPeriods;
	}

	public Integer getPmPeriods() {
		return pmPeriods;
	}

	public void setPmPeriods(Integer pmPeriods) {
		this.pmPeriods = pmPeriods;
	}

	public Integer getNightPeriods() {
		return nightPeriods;
	}

	public void setNightPeriods(Integer nightPeriods) {
		this.nightPeriods = nightPeriods;
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

	public Integer getEventSource() {
		return eventSource;
	}

	public void setEventSource(Integer eventSource) {
		this.eventSource = eventSource;
	}

	public Integer getClassHour() {
		return classHour;
	}

	public void setClassHour(Integer classHour) {
		this.classHour = classHour;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getMornPeriods() {
		return mornPeriods;
	}

	public void setMornPeriods(Integer mornPeriods) {
		this.mornPeriods = mornPeriods;
	}
	
	 public static List<Semester> dt(String data) {
			List<Semester> ts = SUtils.dt(data, new TypeReference<List<Semester>>() {
			});
			if (ts == null)
				ts = new ArrayList<Semester>();
			return ts;

		}
	    
	    public static Semester dc(String data) {
			return SUtils.dc(data, Semester.class);
		}


}
