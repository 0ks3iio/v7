
package net.zdsoft.newgkelective.data.optaplanner.api;

import java.util.List;

public class SimpleCourseSchedulerInput {

	private String arrayId;
	
	//Section标记信息
	//<sectionID，classId,subjectId,subjectType>
	private List<List<String>> sectionList;
	
	//单双周标记信息，记录 合成的lecture隐藏的一门科目
	//<lectureID,subjectId,subjectType>
	private List<List<String>> coupleList;
	
	
	// 需要算法给出时间点的Lecture, {CourseID, SectionID, LectureID, RoomID, TeacherID}
	// 如果是单双周的课，{CourseID, SectionID, LectureID, RoomID, 单周TeacherID, 双周TeacherID}
	private List<List<String>> lectureList; // Entities

	// 所有可用的时间点, {PeriodID, DayIndex, TimeSlotIndex, AM|PM}, DayIndex: 0..6,
	// TimeSlotIndex: 1..10, Morning==1 AM==2, PM==3, Night==4
	private List<List<String>> periodList; // Facts

	// 预排课：已经排好了，不能动了， {LectureID, PeriodID}
	private List<List<String>> lectureFixedList;

	// 课程分组：(O)语数外，(A)选课，(B)学课，(X)其它， {CourseID, CourseType, subjectId,needRoom}
	private List<List<String>> courseList;

	// 联排课：{LectureID, timeSlotCount}, 目前只支持 timeSlotCount==2
	private List<List<String>> lectureTwinList;

	// 相同老师引起的冲突：{LectureID1, LectureID2}
	private List<List<String>> lectureBinaryConflictConstraintList;

	// {LectureID, PeriodIds, Level: 0..5}， 
	// PeriodIds:多个periodId 用 "|" 连接。 1~5表示喜好程度。隐含了 domainList
	// 一个lecture 的某个period 如果属于 level-3，那么 此lecture的这个period就不会出现在 level-1里面。
	// 所以 如果 要 取某个 lecture的doamin 就需要 遍历 这个 LectureID对应的所有 Level的 period,而不能只取 Level-1的
	private List<List<String>> lecturePreferredPeriodConstraintList;

	// 同一个行政班的2个课，不能排在同一个半天的2个时间点，{LectureID1, LectureID2}
	private List<List<String>> lectureNotSameHalfDayConstraintList;

	// 同一个行政班的2个课，不能排在同一天的2个时间点，{LectureID1, LectureID2}
	private List<List<String>> lectureNotSameDayConstraintList;

	// 同一个行政班的2个课，不能排在连续的2个时间点，{LectureID1, LectureID2}
	private List<List<String>> lectureNotNextConstraintList;

	// 同排课, 一行一组，必须排在同一个时间点：{LectureID1, LectureID2, ...}
	private List<List<String>> lectureSamePeriodConstraintList;

	// 互斥老师：{TeacherID1, TeacherID2, NonOverlappingCount}
	private List<List<String>> teacherNonOverlappingCountList;

	// 教师教学进度一致
	//sectionIDstr: 多个sectionID使用 | 连接，表示老师带的这几个班级需要教学进度一致
	// <teacehrID,sectionIDstr>  sectionIDstr
	private List<List<String>> teacherPlanList;
	
	public List<List<String>> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<List<String>> sectionList) {
		this.sectionList = sectionList;
	}

	public List<List<String>> getCoupleList() {
		return coupleList;
	}

	public void setCoupleList(List<List<String>> coupleList) {
		this.coupleList = coupleList;
	}

	public List<List<String>> getLectureList() {
		return lectureList;
	}

	public void setLectureList(List<List<String>> lectureList) {
		this.lectureList = lectureList;
	}

	public List<List<String>> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(List<List<String>> periodList) {
		this.periodList = periodList;
	}

	public List<List<String>> getLectureFixedList() {
		return lectureFixedList;
	}

	public void setLectureFixedList(List<List<String>> lectureFixedList) {
		this.lectureFixedList = lectureFixedList;
	}

	public List<List<String>> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<List<String>> courseList) {
		this.courseList = courseList;
	}

	public List<List<String>> getLectureTwinList() {
		return lectureTwinList;
	}

	public void setLectureTwinList(List<List<String>> lectureTwinList) {
		this.lectureTwinList = lectureTwinList;
	}

	public List<List<String>> getLectureBinaryConflictConstraintList() {
		return lectureBinaryConflictConstraintList;
	}

	public void setLectureBinaryConflictConstraintList(List<List<String>> lectureBinaryConflictConstraintList) {
		this.lectureBinaryConflictConstraintList = lectureBinaryConflictConstraintList;
	}

	public List<List<String>> getLecturePreferredPeriodConstraintList() {
		return lecturePreferredPeriodConstraintList;
	}

	public void setLecturePreferredPeriodConstraintList(List<List<String>> lecturePreferredPeriodConstraintList) {
		this.lecturePreferredPeriodConstraintList = lecturePreferredPeriodConstraintList;
	}

	public List<List<String>> getLectureNotSameHalfDayConstraintList() {
		return lectureNotSameHalfDayConstraintList;
	}

	public void setLectureNotSameHalfDayConstraintList(List<List<String>> lectureNotSameHalfDayConstraintList) {
		this.lectureNotSameHalfDayConstraintList = lectureNotSameHalfDayConstraintList;
	}

	public List<List<String>> getLectureNotSameDayConstraintList() {
		return lectureNotSameDayConstraintList;
	}

	public void setLectureNotSameDayConstraintList(List<List<String>> lectureNotSameDayConstraintList) {
		this.lectureNotSameDayConstraintList = lectureNotSameDayConstraintList;
	}

	public List<List<String>> getLectureNotNextConstraintList() {
		return lectureNotNextConstraintList;
	}

	public void setLectureNotNextConstraintList(List<List<String>> lectureNotNextConstraintList) {
		this.lectureNotNextConstraintList = lectureNotNextConstraintList;
	}

	public List<List<String>> getLectureSamePeriodConstraintList() {
		return lectureSamePeriodConstraintList;
	}

	public void setLectureSamePeriodConstraintList(List<List<String>> lectureSamePeriodConstraintList) {
		this.lectureSamePeriodConstraintList = lectureSamePeriodConstraintList;
	}

	public List<List<String>> getTeacherNonOverlappingCountList() {
		return teacherNonOverlappingCountList;
	}

	public void setTeacherNonOverlappingCountList(List<List<String>> teacherNonOverlappingCountList) {
		this.teacherNonOverlappingCountList = teacherNonOverlappingCountList;
	}

	public String getArrayId() {
		return arrayId;
	}

	public void setArrayId(String arrayId) {
		this.arrayId = arrayId;
	}

	public List<List<String>> getTeacherPlanList() {
		return teacherPlanList;
	}

	public void setTeacherPlanList(List<List<String>> teacherPlanList) {
		this.teacherPlanList = teacherPlanList;
	}

}
