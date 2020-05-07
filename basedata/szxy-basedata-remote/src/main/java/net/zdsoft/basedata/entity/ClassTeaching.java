package net.zdsoft.basedata.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_class_teaching")
public class ClassTeaching extends BaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ColumnInfo(displayName = "单位")
	private String unitId;
	@ColumnInfo(displayName = "班级")
	private String classId;
	@ColumnInfo(displayName = "科目")
	private String subjectId;
	@ColumnInfo(displayName = "是否以教学班形式教学")
	private Integer isTeaCls;
	@ColumnInfo(displayName = "教师")
	private String teacherId;
	@ColumnInfo(displayName = "录入人")
	private String operatorId;
	@ColumnInfo(displayName = "学年")
	private String acadyear;
	@ColumnInfo(displayName = "学期")
	private String semester;
	@ColumnInfo(displayName = "是否软删", disabled = true)
	private Integer isDeleted;
	@ColumnInfo(displayName = "本系统内", disabled = true)
	private Integer eventSource;
	@ColumnInfo(displayName = "创建时间", disabled = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnInfo(displayName = "修改时间", disabled = true)
	private Date modifyTime;
	@ColumnInfo(displayName = "科目类型")
	private String subjectType;
//	@Transient
	@ColumnInfo(displayName = "学分")
	private Integer credit;
//	@ColumnInfo(displayName = "周课时--不用啦")
//	private Integer weekCourseHour;
	
	@ColumnInfo(displayName = "周课时")
	private Float courseHour;
	@ColumnInfo(displayName = "上课类型，单/双周上课")
	private int weekType = CourseSchedule.WEEK_TYPE_NORMAL;
	
	@Transient
	@ColumnInfo(displayName = "满分")
	private Integer fullMark;
	@Transient
	@ColumnInfo(displayName = "及格分")
	private Integer passMark;
	@ColumnInfo(displayName = "是否需要一卡通打卡")
	private Integer punchCard;

	/************* 辅助字段 ************/
	@Transient
	private String teacherName;
	@Transient
	private String subjectName;
	@Transient
	private String shortName;
	@Transient
	private String subjectCode;
	@Transient
	private Integer orderId;
	@Transient
	private String deptId;// 用于查询
	@Transient
	private Set<String> teacherIds;//助教老师
	@Transient
	private List<ClassTeachingEx> exList = new ArrayList<ClassTeachingEx>();//助教老师
	
	

	public Integer getPunchCard() {
		return punchCard;
	}

	public void setPunchCard(Integer punchCard) {
		this.punchCard = punchCard;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

//	public Integer getWeekCourseHour() {
//		return weekCourseHour;
//	}
//
//	public void setWeekCourseHour(Integer weekCourseHour) {
//		this.weekCourseHour = weekCourseHour;
//	}
	

	public Integer getFullMark() {
		return fullMark;
	}

	public Float getCourseHour() {
		return courseHour;
	}

	public void setCourseHour(Float courseHour) {
		this.courseHour = courseHour;
	}

	public void setFullMark(Integer fullMark) {
		this.fullMark = fullMark;
	}

	public Integer getPassMark() {
		return passMark;
	}

	public void setPassMark(Integer passMark) {
		this.passMark = passMark;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
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

	public void setSubjectId(String subjecId) {
		this.subjectId = subjecId;
	}

	public Integer getIsTeaCls() {
		return isTeaCls;
	}

	public void setIsTeaCls(Integer isTeaCls) {
		this.isTeaCls = isTeaCls;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
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
		return "classTeaching";
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Set<String> getTeacherIds() {
		return teacherIds;
	}

	public void setTeacherIds(Set<String> teacherIds) {
		this.teacherIds = teacherIds;
	}

	@Override
	public String toString() {
		return "ClassTeaching [unitId=" + unitId + ", classId=" + classId
				+ ", subjectId=" + subjectId + ", teacherId=" + teacherId
				+ ", acadyear=" + acadyear + ", semester=" + semester
				+ ", teacherName=" + teacherName + ", subjectName="
				+ subjectName + "]";
	}

	public List<ClassTeachingEx> getExList() {
		return exList;
	}

	public void setExList(List<ClassTeachingEx> exList) {
		this.exList = exList;
	}

	public int getWeekType() {
		return weekType;
	}

	public void setWeekType(int weekType) {
		this.weekType = weekType;
	}

}
