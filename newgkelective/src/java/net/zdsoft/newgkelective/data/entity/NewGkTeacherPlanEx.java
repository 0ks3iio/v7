package net.zdsoft.newgkelective.data.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 教师具体安排
 */
@Entity
@Table(name = "newgkelective_teacher_plan_ex")
public class NewGkTeacherPlanEx extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private String teacherPlanId;
	private String teacherId;
	private Integer mutexNum;// 互斥课时数
	private Date creationTime;
	private Date modifyTime;
	private String classIds;
	private String weekPeriodType;//周课时分布，1-均衡，2-集中
	private String dayPeriodType;//每天课时分配，1-不限，2-同半天，3-同半天连续，4-同半天间隔

	@Transient
	private String teacherName;
	@Transient
	private List<String> classIdList;
	@Transient
	private String noTimeStr;
	@Transient
	private String mutexTeaIds;
	@Transient
	private String weekTime;
	@Transient
	private String mutexTeaNames;
	@Transient
	private List<String> mutexTeaIdList;
	
	public List<String> getMutexTeaIdList() {
		return mutexTeaIdList;
	}

	public void setMutexTeaIdList(List<String> mutexTeaIdList) {
		this.mutexTeaIdList = mutexTeaIdList;
	}

	public List<String> getClassIdList() {
		return classIdList;
	}

	public void setClassIdList(List<String> classIdList) {
		this.classIdList = classIdList;
	}

	public String getClassIds() {
		return classIds;
	}

	public void setClassIds(String classIds) {
		this.classIds = classIds;
	}

	public String getTeacherPlanId() {
		return teacherPlanId;
	}

	public void setTeacherPlanId(String teacherPlanId) {
		this.teacherPlanId = teacherPlanId;
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
		return "newGkTeacherPlanEx";
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public Integer getMutexNum() {
		return mutexNum;
	}

	public void setMutexNum(Integer mutexNum) {
		this.mutexNum = mutexNum;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getNoTimeStr() {
		return noTimeStr;
	}

	public void setNoTimeStr(String noTimeStr) {
		this.noTimeStr = noTimeStr;
	}

	public String getMutexTeaIds() {
		return mutexTeaIds;
	}

	public void setMutexTeaIds(String mutexTeaIds) {
		this.mutexTeaIds = mutexTeaIds;
	}

	public String getWeekTime() {
		return weekTime;
	}

	public void setWeekTime(String weekTime) {
		this.weekTime = weekTime;
	}

	public String getMutexTeaNames() {
		return mutexTeaNames;
	}

	public void setMutexTeaNames(String mutexTeaNames) {
		this.mutexTeaNames = mutexTeaNames;
	}

	public String getWeekPeriodType() {
		return weekPeriodType;
	}

	public void setWeekPeriodType(String weekPeriodType) {
		this.weekPeriodType = weekPeriodType;
	}

	public String getDayPeriodType() {
		return dayPeriodType;
	}

	public void setDayPeriodType(String dayPeriodType) {
		this.dayPeriodType = dayPeriodType;
	}
	
}
