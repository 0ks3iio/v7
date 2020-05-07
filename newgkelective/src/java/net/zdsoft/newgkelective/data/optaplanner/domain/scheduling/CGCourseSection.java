package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import net.zdsoft.newgkelective.data.optaplanner.util.AbstractPersistable;

//@PlanningEntity
@XStreamAlias("CGCourseSection")
public class CGCourseSection  extends AbstractPersistable implements Comparable<CGCourseSection> {
	private CGCourse course;
	private int sectionIndex;				//同一个Course中几个Section的序号
	private int sectionAvgStudentCount;		//该Section的期望平均学生人数，排课算法中用来评估分班好差
	private boolean isFixed;     			//true: 该教学班Section是固定的，不能做进一步的学生调整
	private CGTeacher teacher;
	private boolean pure3; // 纯组合班
	private int isBiweekly = CGCourse.WEEK_TYPE_NORMAL; // 固定单双周


	// 返回结果用
	private String oldId;
	
	private String className;
	
	// 班级实际 每周课时数
	private int lectureCount;
	private int minWorkingDaySize;
	// null：为 普通班级 0：不参与比较的合班  1：参与比较的合班班级
	private Integer combineFlag;
	// 0:不需要教室  1:需要教室
	private Integer needRoom = 1; 
	
	@XStreamOmitField
	private List<CGSectionLecture> lectureList;  //本教学班对应的几次课
	
	@XStreamOmitField
	private List<CGStudent> studentList = new ArrayList<>();  
	// 拆分以后 的班级记录 父班级
	@XStreamOmitField
	private String parentId;
	
	
	@InverseRelationShadowVariable(sourceVariableName = "courseSection")
	public List<CGSectionLecture> getLectureList() {
		return lectureList;
	}
	
	@Override
	public int compareTo(CGCourseSection cs2) {
		return this.getName().compareTo(cs2.getName());
	}
	
	public String getName() {
		return course.getCode() + "-" + sectionIndex;
	}

	
	public String getCourseSectionCode() {
		return getOldId() + "-"+course.getSimpleCode();
	}
	
	public int getLectureCount() {
		return lectureCount;
	}
	
	public String getSectionCode(){
		return course.getCode()+sectionIndex;
	}
	

	public void setLectureList(List<CGSectionLecture> lectureList) {
		this.lectureList = lectureList;
	}

	public int getSectionIndex() {
		return sectionIndex;
	}

	public void setSectionIndex(int jxbIndex) {
		this.sectionIndex = jxbIndex;
	}

	public CGCourse getCourse() {
		return course;
	}

	public void setCourse(CGCourse course) {
		this.course = course;
	}

	public int getSectionAvgStudentCount() {
		return sectionAvgStudentCount;
	}

	public void setSectionAvgStudentCount(int classStudentCountLimit) {
		this.sectionAvgStudentCount = classStudentCountLimit;
	}

	public boolean isFixed() {
		return isFixed;
	}

	public void setFixed(boolean isFixed) {
		this.isFixed = isFixed;
	}

	public CGTeacher getTeacher() {
		return teacher;
	}

	public void setTeacher(CGTeacher teacher) {
		this.teacher = teacher;
	}

	public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public boolean isPure3() {
		return pure3;
	}

	public void setPure3(boolean pure3) {
		this.pure3 = pure3;
	}

	public void setLectureCount(int lectureCount) {
		this.lectureCount = lectureCount;
	}

	public int getMinWorkingDaySize() {
		return minWorkingDaySize;
	}

	public void setMinWorkingDaySize(int minWorkingDaySize) {
		this.minWorkingDaySize = minWorkingDaySize;
	}

	public Integer getCombineFlag() {
		return combineFlag;
	}

	public void setCombineFlag(Integer combineFlag) {
		this.combineFlag = combineFlag;
	}

	public Integer getNeedRoom() {
		return needRoom;
	}

	public void setNeedRoom(Integer needRoom) {
		this.needRoom = needRoom;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<CGStudent> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<CGStudent> studentList) {
		this.studentList = studentList;
	}

	public int getIsBiweekly() {
		return isBiweekly;
	}

	public void setIsBiweekly(int isBiweekly) {
		this.isBiweekly = isBiweekly;
	}

	public int getRealWeekType(){
		if(getIsBiweekly() != CGCourse.WEEK_TYPE_NORMAL){
			return getIsBiweekly();
		}
		return course.getIsBiWeekly();
	}
}
