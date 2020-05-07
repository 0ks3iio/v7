package net.zdsoft.newgkelective.data.dto;

import java.util.List;


public class NewGKCourseFeatureDto {
	private String courseName;
	private String courseCode;
	
	private Integer order;
	
	private String subjectId;
	private String subjectType;
	
	/* 周课时数*/
	private Integer courseWorkDay;
	
	/* 单双周  */
	private Integer firstsdWeek;
	/* 单双周对应科目 */
	private String firstsdWeekSubjectId;
	
	/* 周连排次数 连堂次数*/
	private Integer courseCoupleTimes;
	/*连堂时间范围 0：不限 1：指定*/
	private String courseCoupleType;
	/*连堂指定时间范围  1-1-1,2-2-2*/
	private String courseCoupleTypeTimes;
	
	//不连排科目数量 页面显示
	private Integer noContinueNum;
	
	/*课时分配*/
	private String arrangeDay;
	/*半天课时分配*/
	private String arrangeHalfDay;
	/*优先级*/
	private String arrangePrior;
	/*禁排时间 1-1-1，2-2-2*/
	private String noArrangeTime;
	
	//禁排时间时间数量 页面显示
	private Integer noArrangeTimeNum;
	
	// 不联排课程
	private List<String> noAttachSubjects;
	
	// 连排课程放在那几天
	private List<String> coupleDaysSet;
	// 不联排间隔时间
	private Float noAttachInterval;
	// 指定教室
	private List<String> limitRooms;
	// 不排课节次
	private Integer noLectureNum;
	
	private Integer needRoom;
	private Integer followZhb;

	//是否需要考勤

	private Integer punchCard;
	// 接受 课程特征结果时使用

	private List<String> subjectIds;
	private List<String> roomIds;
	//显示能否删除

	private int deletedStr=0;

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public Integer getCourseWorkDay() {
		return courseWorkDay;
	}

	public void setCourseWorkDay(Integer courseWorkDay) {
		this.courseWorkDay = courseWorkDay;
	}

	public Integer getCourseCoupleTimes() {
		return courseCoupleTimes;
	}

	public void setCourseCoupleTimes(Integer courseCoupleTimes) {
		this.courseCoupleTimes = courseCoupleTimes;
	}


	public Float getNoAttachInterval() {
		return noAttachInterval;
	}

	public void setNoAttachInterval(Float noAttachInterval) {
		this.noAttachInterval = noAttachInterval;
	}

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

	public List<String> getNoAttachSubjects() {
		return noAttachSubjects;
	}

	public void setNoAttachSubjects(List<String> noAttachSubjects) {
		this.noAttachSubjects = noAttachSubjects;
	}

	public List<String> getLimitRooms() {
		return limitRooms;
	}

	public void setLimitRooms(List<String> limitRooms) {
		this.limitRooms = limitRooms;
	}

	public Integer getNoLectureNum() {
		return noLectureNum;
	}

	public void setNoLectureNum(Integer noLectureNum) {
		this.noLectureNum = noLectureNum;
	}

	public Integer getNeedRoom() {
		return needRoom;
	}

	public void setNeedRoom(Integer needRoom) {
		this.needRoom = needRoom;
	}

	public List<String> getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(List<String> subjectIds) {
		this.subjectIds = subjectIds;
	}

	public List<String> getRoomIds() {
		return roomIds;
	}

	public void setRoomIds(List<String> roomIds) {
		this.roomIds = roomIds;
	}

	public Integer getPunchCard() {
		return punchCard;
	}

	public void setPunchCard(Integer punchCard) {
		this.punchCard = punchCard;
	}

	public Integer getFirstsdWeek() {
		return firstsdWeek;
	}

	public void setFirstsdWeek(Integer firstsdWeek) {
		this.firstsdWeek = firstsdWeek;
	}

	public String getCoupleDayStr() {
		if(coupleDaysSet == null) {
			return null;
		}
		return coupleDaysSet.stream().reduce((x,y)->x+","+y).orElse(null);
	}

	public List<String> getCoupleDaysSet() {
		return coupleDaysSet;
	}

	public void setCoupleDaysSet(List<String> coupleDaysSet) {
		this.coupleDaysSet = coupleDaysSet;
	}

	public String getFirstsdWeekSubjectId() {
		return firstsdWeekSubjectId;
	}

	public void setFirstsdWeekSubjectId(String firstsdWeekSubjectId) {
		this.firstsdWeekSubjectId = firstsdWeekSubjectId;
	}

	public String getCourseCoupleType() {
		return courseCoupleType;
	}

	public void setCourseCoupleType(String courseCoupleType) {
		this.courseCoupleType = courseCoupleType;
	}

	public String getCourseCoupleTypeTimes() {
		return courseCoupleTypeTimes;
	}

	public void setCourseCoupleTypeTimes(String courseCoupleTypeTimes) {
		this.courseCoupleTypeTimes = courseCoupleTypeTimes;
	}

	public String getArrangeDay() {
		return arrangeDay;
	}

	public void setArrangeDay(String arrangeDay) {
		this.arrangeDay = arrangeDay;
	}

	public String getArrangeHalfDay() {
		return arrangeHalfDay;
	}

	public void setArrangeHalfDay(String arrangeHalfDay) {
		this.arrangeHalfDay = arrangeHalfDay;
	}

	public String getArrangePrior() {
		return arrangePrior;
	}

	public void setArrangePrior(String arrangePrior) {
		this.arrangePrior = arrangePrior;
	}

	public String getNoArrangeTime() {
		return noArrangeTime;
	}

	public void setNoArrangeTime(String noArrangeTime) {
		this.noArrangeTime = noArrangeTime;
	}

	public Integer getNoContinueNum() {
		return noContinueNum;
	}

	public void setNoContinueNum(Integer noContinueNum) {
		this.noContinueNum = noContinueNum;
	}

	public Integer getNoArrangeTimeNum() {
		return noArrangeTimeNum;
	}

	public void setNoArrangeTimeNum(Integer noArrangeTimeNum) {
		this.noArrangeTimeNum = noArrangeTimeNum;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public int getDeletedStr() {
		return deletedStr;
	}

	public void setDeletedStr(int deletedStr) {
		this.deletedStr = deletedStr;
	}

	public Integer getFollowZhb() {
		return followZhb;
	}

	public void setFollowZhb(Integer followZhb) {
		this.followZhb = followZhb;
	}

}
