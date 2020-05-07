package net.zdsoft.newgkelective.data.optaplanner.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGClass;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseSection;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudent;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGTeacher;
import org.apache.commons.lang3.StringUtils;

@XStreamAlias("CGInputData")
public class CGInputData implements Serializable {
	/**
	 * 班级最大人数
	 */
	private Integer maxSectionStudentCount;
	/**
	 * 班级可以再增加的人数
	 */
	private Integer margin;
	private String arrayId;
	private List<String> arrayIds;

	// 是否是文理分班下的排课
	private boolean isWL;
	private int mmCount; 
	private int amCount; 
	private int pmCount; 
	private int nightCount; 
	// 一天之内最后一节课的 下标
	private int maxTimeslotIndex; 
	private String unitName;
	private String gradeName;

	private List<CGStudent> studentList;
	private List<CGCourse>  courseList;
	private List<NKPeriod>  periodList;
	private List<NKRoom>    roomList;

	private List<CGTeacher> teacherList;//这个后需考虑
	
	private List<CGClass> classList;
	private Map<CGClass, NKRoom> xzbRoomConstrains;
	
	private Map<CGTeacher, Set<CGTeacher>> mutexTeacherMap;
	private Map<CGCourse, Map<CGCourse,String>> noAttachCourseMap;
	// 课程连排时间点
	private Map<CGCourse,List<NKPeriod>> subjectCoupleTimeMap;
	// 合班班级组合 string: classId-subjectId-subjectType
	private List<Set<String>> combineClassList;
	// 同时排课班级组合
	private List<Set<String>> meanwhileClassList;
	/**
	 * 指定班级不能在同一时间上课
	 */
	private List<String[]> noMeanWhileClassGroups; 
	// 已经手动安排的课程
	private Map<String,List<NKPeriod>> existedTable;
	
	// 手动指定的 不能动的课程；如果不为空则表示用算法 协助 手动调课，将某几节课 调整到 其他位置
	private Map<String,List<NKPeriod>> fixedLectures;
	
	
	/**
	 * 班级要排课的时间点
	 */
	private Map<CGClass,Set<NKPeriod>> doClassPeriods;
	/**
	 * 班级不排课的时间点
	 */
	private Map<CGClass,Set<NKPeriod>> noClassPeriods;

	// 排课返回 结果
	private List<CGSectionLecture> lectureList;
	@XStreamOmitField
	private List<CGCourseSection> sectionList;
	
	@XStreamOmitField
	private int msgCounter = 0;
	@XStreamOmitField
	private StringBuilder msgBuilder = new StringBuilder();
	
	public int appendMsg(String msg) {
		if(StringUtils.isNotBlank(msg)) {
			msgBuilder.append(++msgCounter).append(". ").append(msg).append("\n");
		}
		return msgCounter;
	}
	public boolean hasMsg() {
		return msgCounter > 0;
	}
	public String getMsgString() {
		if(StringUtils.isNotBlank(msgBuilder)){
			if(StringUtils.isBlank(gradeName)){
				gradeName = "";
			}
			return "\n"+gradeName+"\n"+msgBuilder;
		}
		return msgBuilder.toString();
	}
	
	public Map<CGClass, Set<NKPeriod>> getDoClassPeriods() {
		return doClassPeriods;
	}
	public void setDoClassPeriods(Map<CGClass, Set<NKPeriod>> doClassPeriods) {
		this.doClassPeriods = doClassPeriods;
	}
	public Map<CGClass, Set<NKPeriod>> getNoClassPeriods() {
		return noClassPeriods;
	}
	public void setNoClassPeriods(Map<CGClass, Set<NKPeriod>> noClassPeriods) {
		this.noClassPeriods = noClassPeriods;
	}
	
	
	public List<CGStudent> getStudentList() {
		return studentList;
	}
	public void setStudentList(List<CGStudent> studentList) {
		this.studentList = studentList;
	}
	public List<CGCourse> getCourseList() {
		return courseList;
	}
	public void setCourseList(List<CGCourse> courseList) {
		this.courseList = courseList;
	}
	
	public List<NKPeriod> getPeriodList() {
		return periodList;
	}
	public void setPeriodList(List<NKPeriod> periodList) {
		this.periodList = periodList;
	}
	public List<NKRoom> getRoomList() {
		return roomList;
	}
	public void setRoomList(List<NKRoom> roomList) {
		this.roomList = roomList;
	}
	public List<CGTeacher> getTeacherList() {
		return teacherList;
	}
	public void setTeacherList(List<CGTeacher> teacherList) {
		this.teacherList = teacherList;
	}
	public List<CGClass> getClassList() {
		return classList;
	}
	public void setClassList(List<CGClass> classList) {
		this.classList = classList;
	}
	
	public Integer getMaxSectionStudentCount() {
		return maxSectionStudentCount;
	}
	public void setMaxSectionStudentCount(Integer maxSectionStudentCount) {
		this.maxSectionStudentCount = maxSectionStudentCount;
	}
	public Integer getMargin() {
		return margin;
	}
	public void setMargin(Integer margin) {
		this.margin = margin;
	}
	public List<CGSectionLecture> getLectureList() {
		return lectureList;
	}
	public void setLectureList(List<CGSectionLecture> lectureList) {
		this.lectureList = lectureList;
	}
	public List<CGCourseSection> getSectionList() {
		return sectionList;
	}
	public void setSectionList(List<CGCourseSection> sectionList) {
		this.sectionList = sectionList;
	}
	public String getArrayId() {
		return arrayId;
	}
	public void setArrayId(String arrayId) {
		this.arrayId = arrayId;
	}
	public Map<CGClass, NKRoom> getXzbRoomConstrains() {
		return xzbRoomConstrains;
	}
	public void setXzbRoomConstrains(Map<CGClass, NKRoom> xzbRoomConstrains) {
		this.xzbRoomConstrains = xzbRoomConstrains;
	}
	
	public Map<CGTeacher, Set<CGTeacher>> getMutexTeacherMap() {
		return mutexTeacherMap;
	}
	public void setMutexTeacherMap(Map<CGTeacher, Set<CGTeacher>> mutexTeacherMap) {
		this.mutexTeacherMap = mutexTeacherMap;
	}
	public Map<CGCourse, Map<CGCourse,String>> getNoAttachCourseMap() {
		return noAttachCourseMap;
	}
	public void setNoAttachCourseMap(Map<CGCourse, Map<CGCourse,String>> noAttachCourseMap) {
		this.noAttachCourseMap = noAttachCourseMap;
	}
	public boolean isWL() {
		return isWL;
	}
	public void setWL(boolean isWL) {
		this.isWL = isWL;
	}
	public int getAmCount() {
		return amCount;
	}
	public void setAmCount(int amCount) {
		this.amCount = amCount;
	}
	public int getPmCount() {
		return pmCount;
	}
	public void setPmCount(int pmCount) {
		this.pmCount = pmCount;
	}
	public int getMaxTimeslotIndex() {
		return maxTimeslotIndex;
	}
	public void setMaxTimeslotIndex(int maxTimeslotIndex) {
		this.maxTimeslotIndex = maxTimeslotIndex;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Map<CGCourse, List<NKPeriod>> getSubjectCoupleTimeMap() {
		return subjectCoupleTimeMap;
	}
	public void setSubjectCoupleTimeMap(Map<CGCourse, List<NKPeriod>> subjectCoupleTimeMap) {
		this.subjectCoupleTimeMap = subjectCoupleTimeMap;
	}
	public List<Set<String>> getCombineClassList() {
		return combineClassList;
	}
	public void setCombineClassList(List<Set<String>> combineClassList) {
		this.combineClassList = combineClassList;
	}
	public List<Set<String>> getMeanwhileClassList() {
		return meanwhileClassList;
	}
	public void setMeanwhileClassList(List<Set<String>> meanwhileClassList) {
		this.meanwhileClassList = meanwhileClassList;
	}
	public Map<String, List<NKPeriod>> getExistedTable() {
		return existedTable;
	}
	public void setExistedTable(Map<String, List<NKPeriod>> existedTable) {
		this.existedTable = existedTable;
	}
	public int getNightCount() {
		return nightCount;
	}
	public void setNightCount(int nightCount) {
		this.nightCount = nightCount;
	}
	public int getMmCount() {
		return mmCount;
	}
	public void setMmCount(int mmCount) {
		this.mmCount = mmCount;
	}
	public Map<String, List<NKPeriod>> getFixedLectures() {
		return fixedLectures;
	}
	public void setFixedLectures(Map<String, List<NKPeriod>> fixedLectures) {
		this.fixedLectures = fixedLectures;
	}
	public List<String[]> getNoMeanWhileClassGroups() {
		return noMeanWhileClassGroups;
	}
	public void setNoMeanWhileClassGroups(List<String[]> noMeanWhileClassGroups) {
		this.noMeanWhileClassGroups = noMeanWhileClassGroups;
	}

	public List<String> getArrayIds() {
		return arrayIds;
	}

	public void setArrayIds(List<String> arrayIds) {
		this.arrayIds = arrayIds;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
}