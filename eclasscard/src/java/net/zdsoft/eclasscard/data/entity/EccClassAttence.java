package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="eclasscard_class_attance")
public class EccClassAttence extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	private String infoName; // 班牌号
	private String unitId; // 单位Id
	private String classId; // 班级Id
	private String placeId; // 场地Id
	private String courseScheduleId; // 课表Id
	private String teacherId; // 任课老师Id
	private String subjectId; // 课程Id
	private String subjectName; // 课程
	private String periodInterval;//早上，上午，下午，晚上
	private int period; // 节课
	private boolean isOver; //这节课考勤是否结束
	private String section; // 学段
	private String cardId; // 班牌Id
	private int sectionNumber; // 节次
	private String beginTime; // 课程开始时间
	private String endTime; // 课程结束时间
	private int classType; // 课程结束时间
	@Temporal(TemporalType.TIMESTAMP)//上课时间
	private Date clockDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	@Transient
	private String teacherName; // 老师
	@Transient
	private String teacherRealName; // 老师姓名
	@Transient
	private String className; // 班级，包含教学班，行政班
	@Transient
	private String sectionName; // 节次，带时间
	@Transient
	private int zcStuNum; // 正常人数
	@Transient
	private int qjStuNum; // 请假人数
	@Transient
	private int cdStuNum; // 迟到人数
	@Transient
	private int qkStuNum; // 缺课人数
	@Transient
	private int stuStatus; //学生打卡状态
	
	@Override
	public String fetchCacheEntitName() {
		return "eclasscardClassAttance";
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCourseScheduleId() {
		return courseScheduleId;
	}

	public void setCourseScheduleId(String courseScheduleId) {
		this.courseScheduleId = courseScheduleId;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Date getClockDate() {
		return clockDate;
	}

	public void setClockDate(Date clockDate) {
		this.clockDate = clockDate;
	}

	public String getPeriodInterval() {
		return periodInterval;
	}

	public void setPeriodInterval(String periodInterval) {
		this.periodInterval = periodInterval;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherRealName() {
		return teacherRealName;
	}

	public void setTeacherRealName(String teacherRealName) {
		this.teacherRealName = teacherRealName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getZcStuNum() {
		return zcStuNum;
	}

	public void setZcStuNum(int zcStuNum) {
		this.zcStuNum = zcStuNum;
	}

	public int getQjStuNum() {
		return qjStuNum;
	}

	public void setQjStuNum(int qjStuNum) {
		this.qjStuNum = qjStuNum;
	}

	public int getCdStuNum() {
		return cdStuNum;
	}

	public void setCdStuNum(int cdStuNum) {
		this.cdStuNum = cdStuNum;
	}

	public int getQkStuNum() {
		return qkStuNum;
	}

	public void setQkStuNum(int qkStuNum) {
		this.qkStuNum = qkStuNum;
	}

	public int getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(int sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getClassType() {
		return classType;
	}

	public void setClassType(int classType) {
		this.classType = classType;
	}

	public int getStuStatus() {
		return stuStatus;
	}

	public void setStuStatus(int stuStatus) {
		this.stuStatus = stuStatus;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
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
	
}
