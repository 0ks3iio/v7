package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 调课扩展信息
 */
@Table(name="base_adjusted_detail")
@Entity
public class AdjustedDetail extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String schoolId;
	private String adjustedId;//主表id
    private String courseScheduleId;
	private Integer weekOfWorktime;
	private Integer dayOfWeek;
	private Integer period;
	private String periodInterval;
	private String classId;
	private String subjectId;
	private String teacherId;
	private String teacherExIds;
	private String adjustedType; // 01 调课；02 被调
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getAdjustedId() {
		return adjustedId;
	}

	public void setAdjustedId(String adjustedId) {
		this.adjustedId = adjustedId;
	}

    public String getCourseScheduleId() {
        return courseScheduleId;
    }

    public void setCourseScheduleId(String courseScheduleId) {
        this.courseScheduleId = courseScheduleId;
    }

    public Integer getWeekOfWorktime() {
		return weekOfWorktime;
	}

	public void setWeekOfWorktime(Integer weekOfWorktime) {
		this.weekOfWorktime = weekOfWorktime;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getPeriodInterval() {
		return periodInterval;
	}

	public void setPeriodInterval(String periodInterval) {
		this.periodInterval = periodInterval;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherExIds() {
		return teacherExIds;
	}

	public void setTeacherExIds(String teacherExIds) {
		this.teacherExIds = teacherExIds;
	}

    public String getAdjustedType() {
        return adjustedType;
    }

    public void setAdjustedType(String adjustedType) {
        this.adjustedType = adjustedType;
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
		return "adjustedEx";
	}

}
