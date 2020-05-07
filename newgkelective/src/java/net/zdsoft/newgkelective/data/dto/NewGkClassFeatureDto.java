package net.zdsoft.newgkelective.data.dto;

import java.util.List;
import java.util.Set;

import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.entity.NewGkClassCombineRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;

public class NewGkClassFeatureDto {
	private String subjectId;
	private String subjectName;
	private String subjectType;
	private String teacherId;
	private String teacherName;
	
	private String classId;
	// 单双周
	private Integer weekType = NewGkElectiveConstant.WEEK_TYPE_NORMAL;
	private Integer courseWorkDay;
	// 1-2-3,3-2-2
	private String noArrangeTime;
	// classId1,classId2
	private String combineClass;
	// classId1 : subjectCode1, classId2 : subjectCode2
	private String meanwhiles;
	
	/* 以下保存时 使用 */
	
	private List<NewGkClassSubjectTime> classSubjectTimeList ;
	private List<NewGkClassCombineRelation> classCombineList;
	private List<NewGkLessonTime> lessonTimeList;
	private List<NewGkLessonTimeEx> lessonTimeExList;
	
	// 
	Set<NewGkClassCombineRelation> delRelaList;
	Set<String> delTimeExScourceIdList;
	
	public NewGkClassFeatureDto(String subjectId, String subjectType, Integer period) {
		this(subjectId,subjectType,period,null);
	}
	public NewGkClassFeatureDto(String subjectId, String subjectType, Integer period, Integer weekType) {
		this.subjectId = subjectId;
		this.subjectType = subjectType;
		this.courseWorkDay = period;
		this.weekType = weekType;
	}
	public NewGkClassFeatureDto() {}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public Integer getCourseWorkDay() {
		return courseWorkDay;
	}
	public void setCourseWorkDay(Integer courseWorkDay) {
		this.courseWorkDay = courseWorkDay;
	}
	public String getNoArrangeTime() {
		return noArrangeTime;
	}
	public void setNoArrangeTime(String noArrangeTime) {
		this.noArrangeTime = noArrangeTime;
	}
	public String getCombineClass() {
		return combineClass;
	}
	public void setCombineClass(String combineClass) {
		this.combineClass = combineClass;
	}
	public String getMeanwhiles() {
		return meanwhiles;
	}
	public void setMeanwhiles(String meanwhiles) {
		this.meanwhiles = meanwhiles;
	}
	
	public List<NewGkLessonTime> getLessonTimeList() {
		return lessonTimeList;
	}
	public void setLessonTimeList(List<NewGkLessonTime> lessonTimeList) {
		this.lessonTimeList = lessonTimeList;
	}
	public List<NewGkLessonTimeEx> getLessonTimeExList() {
		return lessonTimeExList;
	}
	public void setLessonTimeExList(List<NewGkLessonTimeEx> lessonTimeExList) {
		this.lessonTimeExList = lessonTimeExList;
	}
	public Set<NewGkClassCombineRelation> getDelRelaList() {
		return delRelaList;
	}
	public void setDelRelaList(Set<NewGkClassCombineRelation> delRelaList) {
		this.delRelaList = delRelaList;
	}
	public List<NewGkClassSubjectTime> getClassSubjectTimeList() {
		return classSubjectTimeList;
	}
	public void setClassSubjectTimeList(List<NewGkClassSubjectTime> classSubjectTimeList) {
		this.classSubjectTimeList = classSubjectTimeList;
	}
	public List<NewGkClassCombineRelation> getClassCombineList() {
		return classCombineList;
	}
	public void setClassCombineList(List<NewGkClassCombineRelation> classCombineList) {
		this.classCombineList = classCombineList;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public String getSubjectCode() {
		return subjectId+"-"+subjectType;
	}
	public Set<String> getDelTimeExScourceIdList() {
		return delTimeExScourceIdList;
	}
	public void setDelTimeExScourceIdList(Set<String> delTimeExScourceIdList) {
		this.delTimeExScourceIdList = delTimeExScourceIdList;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public Integer getWeekType() {
		return weekType;
	}
	public void setWeekType(Integer weekType) {
		this.weekType = weekType;
	}
}
