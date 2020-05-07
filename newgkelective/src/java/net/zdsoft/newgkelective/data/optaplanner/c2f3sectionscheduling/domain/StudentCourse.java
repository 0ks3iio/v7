package net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain;

import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.common.AbstractPersistable;

/**
 *学生需要安排的科目
 */
@PlanningEntity
public class StudentCourse extends AbstractPersistable {
	Student  student;
	TakeCourse   course;
	
	TimeSlot 		timeSlot;		 //Planning Variable
	List<TimeSlot>  timeSlotDomain;  //domain of Planning Variable timeSlot
	
	
	public StudentCourse() {
		
	}
	
	public String getStudentId() {
		return student.getStudentId();
	}
	
	public Student getStudent() {
		return student;
	}
	
	public void setStudent(Student student) {
		this.student = student;
	}
	
	public TakeCourse getCourse() {
		return course;
	}
	
	public void setCourse(TakeCourse course) {
		this.course = course;
	}
	
	@PlanningVariable(valueRangeProviderRefs = { "timeSlotRange" })
	public TimeSlot getTimeSlot() {
		return timeSlot;
	}
	
	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	@ValueRangeProvider(id = "timeSlotRange")
	public List<TimeSlot> getTimeSlotDomain() {
		return timeSlotDomain;
	}
	
	public void setTimeSlotDomain(List<TimeSlot> timeSlotDomain) {
		this.timeSlotDomain = timeSlotDomain;
	}
}
