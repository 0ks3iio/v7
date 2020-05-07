package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain;

import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common.AbstractPersistable;

@PlanningEntity
public class CurriculumCourse extends AbstractPersistable {
	V4Course				course;
	
	StudentCurriculum	studentCurriculum; 		//source of List<StudentCourse>
	
	CourseSection 		courseSection;  		//planning variable
	List<CourseSection>	courseSectionList; 		//domain of courseSection
	
	public CurriculumCourse() {
		super();
	}

	public CurriculumCourse(V4Course course, StudentCurriculum studentCurriculum) {
		super();
		this.course = course;
		this.studentCurriculum = studentCurriculum;
	}


	
	@ValueRangeProvider(id = "courseSectionRange")
	public List<CourseSection> getCourseSectionList () {
		return courseSectionList;
	}
	
	@PlanningVariable(valueRangeProviderRefs = { "courseSectionRange" })
	public CourseSection getCourseSection () {
		return courseSection;
	}
	
	public List<V4Student> getStudentList () {
		return studentCurriculum.getStudentList();
	}

	public V4Course getCourse() {
		return course;
	}

	public void setCourse(V4Course course) {
		this.course = course;
	}

	public StudentCurriculum getStudentCurriculum() {
		return studentCurriculum;
	}

	public void setStudentCurriculum(StudentCurriculum studentCurriculum) {
		this.studentCurriculum = studentCurriculum;
	}

	public void setCourseSection(CourseSection courseSection) {
		this.courseSection = courseSection;
	}

	public void setCourseSectionList(List<CourseSection> courseSectionList) {
		this.courseSectionList = courseSectionList;
	}
	
	public int getStudentCount () {
		return studentCurriculum.getStudentList().size();
	}
	
	public int getTotalCourseScore () {
		int totalCourseScore = 0;
		for (V4Student s : studentCurriculum.getStudentList()) {
			totalCourseScore += s.getCourseScoreByCourse(course);
		}
		return totalCourseScore;
	}
}
