package net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.AbstractPersistable;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.CalculateSections;

@PlanningSolution
public class SectioningSolution extends AbstractPersistable{
	//只是用于标记本次分班算法对象
	private String  appName;
	//算法必填
	private List<Student>			studentList;					
	private List<StudentCourse>		studentCourseList;
	private List<TakeCourse>			courseList;
	private List<TimeSlot>			timeSlotList;
	//每个科目班级人数平均值
	private Map<String, Integer>    courseSectionSizeMeanMap;
	//每个科目人数浮动范围
	private Map<String, Integer>    courseSectionSizeMarginMap;	
	
	@PlanningScore(bendableHardLevelsSize = 4, bendableSoftLevelsSize = 2)
	private BendableScore score;
	
	public BendableScore getScore() {
		return score;
	}

	public void setScore(BendableScore score) {
		this.score = score;
	}
	
	public void printResult() {
		timeSlotList.stream().forEach(e -> e.printResult());
		timeSlotList.stream().forEach(e -> System.out.println(e.getTimeSlotName() + " Student# = " + e.getStudentSet().size()));
	}
	
	public Map<String, Integer> getCourseSectionSizeMeanMap() {
		return courseSectionSizeMeanMap;
	}

	public void setCourseSectionSizeMeanMap(Map<String, Integer> courseSectionSizeMeanMap) {
		this.courseSectionSizeMeanMap = courseSectionSizeMeanMap;
	}

	public Map<String, Integer> getCourseSectionSizeMarginMap() {
		return courseSectionSizeMarginMap;
	}

	public void setCourseSectionSizeMarginMap(Map<String, Integer> courseSectionSizeMarginMap) {
		this.courseSectionSizeMarginMap = courseSectionSizeMarginMap;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	@ProblemFactCollectionProperty
	public List<Student> getStudentList() {
		return studentList;
	}
	
	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}


	@ProblemFactCollectionProperty
	public List<TakeCourse> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<TakeCourse> courseList) {
		this.courseList = courseList;
	}

	@PlanningEntityCollectionProperty
	public List<StudentCourse> getStudentCourseList() {
		return studentCourseList;
	}

	public void setStudentCourseList(List<StudentCourse> studentCourseList) {
		this.studentCourseList = studentCourseList;
	}
	
	@PlanningEntityCollectionProperty
	public List<TimeSlot> getTimeSlotList() {
		return timeSlotList;
	}

	public void setTimeSlotList(List<TimeSlot> timeSlotList) {
		this.timeSlotList = timeSlotList;
	}

	public void validate() {
		for (TimeSlot timeslot : timeSlotList) {
			List<StudentCourse> studentCoursesT = timeslot.getStudentCourseList();
			
			Map<Student, List<StudentCourse>> stuMap = studentCoursesT.stream().collect(Collectors.groupingBy(StudentCourse::getStudent));
			for (Student student : stuMap.keySet()) {
				List<StudentCourse> list = stuMap.get(student);
				if(list.size() > 1) {
					System.out.println("学生"+student.getStudentName()
							+"在"+timeslot.getTimeSlotName()
							+"有"+list.size()+"门课在同一时间  ["
							+list.stream()
								.map(e->e.getCourse().getCourseName())
								.reduce((e1,e2)->e1+","+e2)
								.orElse("")+"]");
				}
			}
			
		}
		
	}
	
}
