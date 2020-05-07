package net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.common.AbstractPersistable;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.common.CalculateSections;

@PlanningEntity
public class TimeSlot extends AbstractPersistable {
	
	String timeSlotName;
//	Map<Course, List<StudentCourse>>  preStudentCourseMap;
	
	List<StudentCourse>  studentCourseList = new ArrayList<>(); 
	
	
	
	public TimeSlot() {
//		preStudentCourseMap = new HashMap<>(); 
	}

	@InverseRelationShadowVariable(sourceVariableName = "timeSlot")
	public List<StudentCourse> getStudentCourseList () {
		return studentCourseList;
	}

	
	
//	public Map<Course, List<StudentCourse>> getPreStudentCourseMap() {
//		return preStudentCourseMap;
//	}
//
//	public void setPreStudentCourseMap(Map<Course, List<StudentCourse>> preStudentCourseMap) {
//		this.preStudentCourseMap = preStudentCourseMap;
//	}

	public Set<Student> getStudentSet() {
		Set<Student> studentSet = studentCourseList.stream().map(e -> e.getStudent()).collect(Collectors.toSet());
		return studentSet;
	}
	
	public void printResult() {
		System.out.println("TimeSlot: " + timeSlotName + ", " + studentCourseList.size());
		
		Map<TakeCourse, List<StudentCourse>> studentCourseGroup = studentCourseList.stream().collect(Collectors.groupingBy(StudentCourse::getCourse));
		int score = 0;
		
		int sectionSizeMean = 0, sectionSizeMargin = 0, studentCount = 0, sectionCount = 0;
		for (Map.Entry<TakeCourse, List<StudentCourse>> me : studentCourseGroup.entrySet()) {
			sectionSizeMean = me.getKey().getSectionSizeMean();
			sectionSizeMargin = me.getKey().getSectionSizeMargin();
			studentCount = me.getValue().size();
			sectionCount += CalculateSections.calculateSectioning(studentCount, sectionSizeMean, sectionSizeMargin).size();
			System.out.println("\t " + me.getKey().getCourseName() + ", " + studentCount + ", " + CalculateSections.calculateSectioning(studentCount, sectionSizeMean, sectionSizeMargin));
		} 
		System.out.println("\t Total Sections in " + timeSlotName + ": " + sectionCount);
	}
	
	public int getCurrentPartialSectionCount() {
		Map<TakeCourse, List<StudentCourse>> studentCourseGroup = studentCourseList.stream().collect(Collectors.groupingBy(StudentCourse::getCourse));
		
//		for(Map.Entry<Course, List<StudentCourse>> me : preStudentCourseMap.entrySet()) {
//			if (studentCourseGroup.containsKey(me.getKey())) {
//				studentCourseGroup.get(me.getKey()).addAll(me.getValue());
//			}
//			else
//				studentCourseGroup.put(me.getKey(), me.getValue());
//		}
		
		int score = 0;
		
		int sectionSizeMean = 0, sectionSizeMargin = 0, studentCount = 0;
		for (Map.Entry<TakeCourse, List<StudentCourse>> me : studentCourseGroup.entrySet()) {
			sectionSizeMean = me.getKey().getSectionSizeMean();
			sectionSizeMargin = me.getKey().getSectionSizeMargin();
			studentCount = me.getValue().size();
			int localSectionCount = 0;
			while (studentCount > sectionSizeMean + sectionSizeMargin) {
				studentCount -= sectionSizeMean;
				localSectionCount ++;
			}
			
			if (studentCount < sectionSizeMean - sectionSizeMargin && studentCount > 0) {
				double d = Math.sqrt((studentCount * 1.0) / (localSectionCount + 1)) * 100;
				score += (int) d;
			}
		} 

		return score;
		
	}
	
	public String getTimeSlotName() {
		return timeSlotName;
	}

	public void setTimeSlotName(String timeSlotName) {
		this.timeSlotName = timeSlotName;
	}

	public void setStudentCourseList(List<StudentCourse> studentCourseList) {
		this.studentCourseList = studentCourseList;
	}	
	
	
}
