package net.zdsoft.basedata.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 备份课表用的临时表
 * @author user
 *
 */
@Entity
@Table(name = "BASE_COURSE_SCHEDULE_REVERT")
public class CourseScheduleRevert extends BaseEntity<String>{

    private static final long serialVersionUID = 1L;
	
	private String acadyear; // 学年
	private int semester; // 学期
    private String schoolId; // 学校Id
    private String gradeId; // 学校Id
	private String classId; // 班级Id
	private String subjectId; // 课程Id
	private String teacherId; // 教师Id
	private String placeId;//场地Id
	private int weekOfWorktime; //开学时间的周次
	//private int dayOfWeek; // 星期一:0; 星期二:1;以此类推
	private Integer dayOfWeek; // 星期一:0; 星期二:1;以此类推
	private String periodInterval;//早上，上午，下午，晚上
	private int period; // 节课
	private int classType;//班级类型  
	private String subjectType;
	private String subjectName;
	private int weekType = CourseSchedule.WEEK_TYPE_NORMAL;//单周，双周，正常
	private int punchCard;//是否需要电子班排打卡
	private String courseId;//学期课程安排id 冗余字段 20170410丢弃;因6.0课表导入 重新开启20180315
	//private String computerlabId; // 机房id  20170410丢弃
	//private String timeInterval; //  时段 20170410丢弃
	
	private Date creationTime;
	private Date modifyTime;
	private int isDeleted;
	
	//辅助字段
    @Transient
    private String className;
    @Transient
    private String oldDivideClassId;
    @Transient
    private String teacherName;
    @Transient
    private String placeName;
    @Transient
    private String subAndTeacherName;
    @Transient
    private String batch;
    @Transient
    private String score;
    @Transient
    private String recordId;
    @Transient
    private String remark;
    @Transient
    private String punishStudentName;
    @Transient
    private String bgColor;
    @Transient
    private String borderColor;
    
    @Transient
    private Set<String> teacherIds;//教师 包括辅助与主教师
    @Transient
    private String dayTimeStr;//时间组装 
    @Transient
    private String periodTimeStr;
   
	public int getPunchCard() {
		return punchCard;
	}

	public void setPunchCard(int punchCard) {
		this.punchCard = punchCard;
	}

	public String getPunishStudentName() {
		return punishStudentName;
	}

	public void setPunishStudentName(String punishStudentName) {
		this.punishStudentName = punishStudentName;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String fetchCacheEntitName() {
		return "courseScheduleRevert";
	}

	public int getWeekType() {
		return weekType;
	}

	public void setWeekType(int weekType) {
		this.weekType = weekType;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
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

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public String getPeriodInterval() {
		return periodInterval;
	}

	public void setPeriodInterval(String periodInterval) {
		this.periodInterval = periodInterval;
	}

	public int getClassType() {
		return classType;
	}

	public void setClassType(int classType) {
		this.classType = classType;
	}

	public int getWeekOfWorktime() {
		return weekOfWorktime;
	}

	public void setWeekOfWorktime(int weekOfWorktime) {
		this.weekOfWorktime = weekOfWorktime;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getSubAndTeacherName() {
		return subAndTeacherName;
	}

	public void setSubAndTeacherName(String subAndTeacherName) {
		this.subAndTeacherName = subAndTeacherName;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Set<String> getTeacherIds() {
		return teacherIds;
	}

	public void setTeacherIds(Set<String> teacherIds) {
		this.teacherIds = teacherIds;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public String getDayTimeStr() {
		return dayTimeStr;
	}

	public void setDayTimeStr(String dayTimeStr) {
		this.dayTimeStr = dayTimeStr;
	}

	public String getPeriodTimeStr() {
		return periodTimeStr;
	}

	public void setPeriodTimeStr(String periodTimeStr) {
		this.periodTimeStr = periodTimeStr;
	}

	public String getOldDivideClassId() {
		return oldDivideClassId;
	}

	public void setOldDivideClassId(String oldDivideClassId) {
		this.oldDivideClassId = oldDivideClassId;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

}
