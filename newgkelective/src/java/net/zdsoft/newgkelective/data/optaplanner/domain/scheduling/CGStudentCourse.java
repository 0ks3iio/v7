package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.zdsoft.newgkelective.data.optaplanner.util.AbstractPersistable;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.google.common.collect.Sets;
import com.thoughtworks.xstream.annotations.XStreamAlias;

//@PlanningEntity(movableEntitySelectionFilter = StudentSectionChangeMoveFilter.class)
//@PlanningEntity
@XStreamAlias("CGStudentCourse")
public class CGStudentCourse  extends AbstractPersistable implements Comparable<CGStudentCourse>{
	
	private CGStudent student;
	private CGCourse course;
	
	private List<CGStudentCourse> allStudentCourseList;//同一个学生所有的课
	
	private CGCourseSection courseSection;   //planning variable
	private List<CGCourseSection> courseSectionList;  //domain of courseSection
	
	private boolean isFixed;     			//true: 该班Section是固定的，不能做进一步的学生调整
	
	public String getCurriculumCode () {
		String curriculumCode = "";
		SortedSet<String> sortedCodes = new TreeSet<String> ();
		for (CGStudentCourse sc : allStudentCourseList) {
			CGCourseSection cs = sc.getCourseSection();
			sortedCodes.add(cs.getCourseSectionCode());
		}
		
		for (String s : sortedCodes) {
			curriculumCode += s; 
		}
		
		return curriculumCode;
	}
	
	public String getStudentName () {
		return student.getStudentName();
	}
	
	@Override
	public int compareTo(CGStudentCourse sc2) {
		return this.getStudent().getStudentId().compareTo(sc2.getStudent().getStudentId());
	}
	
	//学生不重复的时间点数量
	public int getStudentPeriodCount(){
		Set<CGPeriod> periodSet = new HashSet<CGPeriod>();
		List<CGSectionLecture> lectureListTemp;
		for(CGStudentCourse studentCourseItem : allStudentCourseList){
			lectureListTemp = studentCourseItem.getCourseSection().getLectureList();
			for (CGSectionLecture lectureItem : lectureListTemp) {
				periodSet.add(lectureItem.getPeriod());
			}
		}
		return periodSet.size();
	}
	
	public CGStudent getStudent() {
		return student;
	}
	public void setStudent(CGStudent student) {
		this.student = student;
	}
	public CGCourse getCourse() {
		return course;
	}
	public void setCourse(CGCourse course) {
		this.course = course;
	}
	public List<CGStudentCourse> getAllStudentCourseList() {
		return allStudentCourseList;
	}
	public void setAllStudentCourseList(List<CGStudentCourse> allStudentCourseList) {
		this.allStudentCourseList = allStudentCourseList;
	}
	
	//@PlanningVariable(valueRangeProviderRefs = { "courseSectionRange" })
	public CGCourseSection getCourseSection() {
		return courseSection;
	}
	public void setCourseSection(CGCourseSection courseSection) {
		this.courseSection = courseSection;
	}
	
	//@ValueRangeProvider(id = "courseSectionRange")
	public List<CGCourseSection> getCourseSectionList() {
		return courseSectionList;
	}
	public void setCourseSectionList(List<CGCourseSection> courseSectionList) {
		this.courseSectionList = courseSectionList;
	}
	//TODO 考略单双周 连排的情况
	public int getConflictPeriodCount () {
		Set<String> allPeriodSet = new HashSet<>();

		int totalPeriodCount = courseSection.getLectureCount();
		for (CGSectionLecture lecture : courseSection.getLectureList()) {
			allPeriodSet.addAll(lecture.getPeriodCodes());
		}
		
		for (CGStudentCourse studentCourse : allStudentCourseList) {
			if (this.getId() == studentCourse.getId()) 
				continue;
			totalPeriodCount += studentCourse.getCourseSection().getLectureCount();
			for (CGSectionLecture lecture2 : studentCourse.getCourseSection().getLectureList()) {
				allPeriodSet.addAll(lecture2.getPeriodCodes());
			}
		}
		
		return totalPeriodCount - allPeriodSet.size();
	}
	
	public boolean isFixed() {
		return isFixed;
	}

	public void setFixed(boolean isFixed) {
		this.isFixed = isFixed;
	}
	
}
