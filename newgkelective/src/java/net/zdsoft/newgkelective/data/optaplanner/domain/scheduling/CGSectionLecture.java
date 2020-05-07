package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import net.zdsoft.newgkelective.data.optaplanner.util.AbstractPersistable;
import net.zdsoft.newgkelective.data.optaplanner.util.CGMoveableFilter;

@PlanningEntity(movableEntitySelectionFilter = CGMoveableFilter.class)
@XStreamAlias("CGSectionLecture")
public class CGSectionLecture extends AbstractPersistable implements Comparable<CGSectionLecture>{
	private boolean moveable = true;
	
	@XStreamOmitField
	private CGCourse  		course;
	private CGCourseSection 	courseSection;
	private int   	        isBiWeekly; //是否单双周:1是单周，-1是双周，2是其它。给定两个SectionLecture，只有其和为0时才是可以接受的
	
	private CGPeriod period;             //Planning Variable
	private CGPeriod periodBak;             
	
	@XStreamOmitField
	private List<CGPeriod> periodList;   //domain of period

	private CGRoom   room;               //Planning Variable
	@XStreamOmitField
	private List<CGRoom> roomList;       //domain of room
	
	@XStreamOmitField
	private List<CGSectionLecture> sectionLectureList; // 本次课所属的所有教学课
	
	//1: 1课时单位；2:2个课时单位联排
	private int timeSlotCount;
	
	public CGCourseSection getCourseSection() {
		return courseSection;
	}

	public int getRoomCount () {
		Set<CGRoom> roomSet = new HashSet<>();
		sectionLectureList.stream().forEach(e -> roomSet.add(e.getRoom()));
		return roomSet.size();
	}
	
	public int getMinWorkDayDiff() {
		Set<CGDay> daySet = new HashSet<CGDay>();
		for(CGSectionLecture lectureItem : sectionLectureList){
			daySet.add(lectureItem.getPeriod().getDay());
		}
		
		int minWorkDay = courseSection.getMinWorkingDaySize(); 
		if (daySet.size() >= minWorkDay)
			return 0;
		else
			return minWorkDay - daySet.size();		
	}
	
	@Override
	public int compareTo(CGSectionLecture lecture2) {
		if (this.getId() > lecture2.getId())
			return 1;
		else 
			return -1;
	}
	
	public CGDay getDay() {
        if (period == null) {
            return null;
        }
        return period.getDay();
    }
	
	public int getTimeSlotIndex () {
		return period.getTimeslot().getTimeslotIndex();
	}
	 
	public String toString() {
		return courseSection.getCourseSectionCode() + "-ID-" + this.getId();
	}
	
	public void setCourseSection(CGCourseSection courseSection) {
		this.courseSection = courseSection;
	}

	public List<CGSectionLecture> getSectionLectureList() {
		return sectionLectureList;
	}

	public void setSectionLectureList(List<CGSectionLecture> sameAllLectureList) {
		this.sectionLectureList = sameAllLectureList;
	}

	@PlanningVariable(valueRangeProviderRefs = { "periodRange" })
	public CGPeriod getPeriod() {
		return period;
	}

	public void setPeriod(CGPeriod period) {
		this.period = period;
	}

	@ValueRangeProvider(id = "periodRange")
	public List<CGPeriod> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(List<CGPeriod> periodList) {
		this.periodList = periodList;
	}

	public CGCourse getCourse() {
		return course;
	}

	public void setCourse(CGCourse course) {
		this.course = course;
	}

	public int getIsBiWeekly() {
		return isBiWeekly;
	}

	public void setIsBiWeekly(int isBiWeekly) {
		this.isBiWeekly = isBiWeekly;
	}

//	@PlanningVariable(valueRangeProviderRefs = { "roomRange" })
	public CGRoom getRoom() {
		return room;
	}

	public String getRoomCode() {
		if(getRoom() == null)
			return null;
		return getRoom().getCode();
	}
	
	public void setRoom(CGRoom room) {
		this.room = room;
	}

//	@ValueRangeProvider(id = "roomRange")
	public List<CGRoom> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<CGRoom> roomList) {
		this.roomList = roomList;
	}

	public Integer getTeacherId() {
		if(courseSection.getTeacher() == null){
			return null;
		}
		return courseSection.getTeacher().getId();
	}
	public String getTeacherCode() {
		if(courseSection.getTeacher() == null){
			return null;
		}
		return courseSection.getTeacher().getCode();
	}
	
	public int getTimeSlotCount() {
		return timeSlotCount;
	}

	public void setTimeSlotCount(int timeslotCount) {
		this.timeSlotCount = timeslotCount;
	}

	public int getMinWorkingDaySize () {
		return courseSection.getMinWorkingDaySize();
	}

	// 获取periodCode 当连排时是两个时间点，非连排时是 一个时间点
	public Set<String> getPeriodCodes() {
		Set<String> periodCodes = new HashSet<>();
		for(int i=0;i<timeSlotCount;i++){
			periodCodes.add(period.getOffSetPeriodCode(i));
		}
		return periodCodes;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}
	
	public int getCoursePriority() {
		if(course==null) {
			return 1;
		}
		return course.getPriority();
	}

	public CGPeriod getPeriodBak() {
		return periodBak;
	}

	public void setPeriodBak(CGPeriod periodBak) {
		this.periodBak = periodBak;
	}
	
	public int getPeriodInterScore(){
		int score=0;
		int day = period.getDay().getDayIndex() - periodBak.getDay().getDayIndex();
		if(day!=0) {
			score += Math.abs(day)*10;
		}
		int ts = getTimeSlotIndex() - periodBak.getTimeslot().getTimeslotIndex();
		if(ts!=0) {
			score += Math.abs(ts);
		}
		
		return score;
	}
}
