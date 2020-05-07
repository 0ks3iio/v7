package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 代课记录
 */
@Table(name="base_tipsay")
@Entity
public class Tipsay extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String schoolId;
	private String acadyear;
	private Integer semester;
	private String courseScheduleId;//课程表id
	private Integer weekOfWorktime;
	private Integer dayOfWeek;
	private Integer period;
	private String periodInterval;
	private String newTeacherId;//代课老师
	private String classId;
	private Integer classType;
	private String subjectId;
	private String teacherId;//原来主教师
	private String teacherExIds;//原来辅助教师
	private Integer isDeleted;
	private String remark;//需要代课备注
	private String state;//代课状态
	private String operator;//操作人  存教师id
	private String type;//类型 1：代课 2：管课
	private String tipsayType;//代课类型  01：管理员直接安排 02:自主申请  03：自主申请管理员安排
	
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

	public String getNewTeacherId() {
		return newTeacherId;
	}

	public void setNewTeacherId(String newTeacherId) {
		this.newTeacherId = newTeacherId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public Integer getClassType() {
		return classType;
	}

	public void setClassType(Integer classType) {
		this.classType = classType;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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
		return "tipsay";
	}

	public String getTipsayType() {
		return tipsayType;
	}

	public void setTipsayType(String tipsayType) {
		this.tipsayType = tipsayType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
