package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common.AbstractPersistable;

@PlanningSolution
public class SectioningSolution extends AbstractPersistable{
	private String  appName;
	
	private List<V4Course>			courseList;				//fact
	private List<Curriculum>		curriculumList;			//fact
	
	private List<V4Student>			studentList;			//1+: each Student pointing to a StudentCurriculum		
	private List<StudentCurriculum>	studentCurriculumList;	//1-: inverse of StudentCurriculum, each StudentCurriculum pointing to a list of Student
	
	private List<CurriculumCourse>	curriculumCourseList;	//2+: each CurriculumCourse pointing to a CourseSection
	private List<CourseSection>		courseSectionList;		//2-: inverse of CurriculumCourse, each CourseSection pointing toa list of CurriculumCourse
	
	
	@PlanningScore(bendableHardLevelsSize = 4, bendableSoftLevelsSize = 2)
	private BendableScore score;
	
	
	public BendableScore getScore() {
		return score;
	}

	public void setScore(BendableScore score) {
		this.score = score;
	}

	public void printCourseSectionList () {
		System.out.println("sectionList.size = " + courseSectionList.stream().filter(e -> e.getCurrentStudentCount() > 0).count());
		courseSectionList.stream().forEach(e -> e.printCourseSection());
	}
	
	public void printCurriculumList () {
		curriculumList.stream().forEach(e -> e.printCurriculum());
	}
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	@ProblemFactCollectionProperty
	public List<V4Course> getCourseList() {
		return courseList;
	}
	
	public void setCourseList(List<V4Course> courseList) {
		this.courseList = courseList;
	}
	
	@ProblemFactCollectionProperty
	public List<Curriculum> getCurriculumList() {
		return curriculumList;
	}
	
	public void setCurriculumList(List<Curriculum> curriculumList) {
		this.curriculumList = curriculumList;
	}
	
	//@PlanningEntityCollectionProperty
	@ProblemFactCollectionProperty
	public List<V4Student> getStudentList() {
		return studentList;
	}
	
	public void setStudentList(List<V4Student> studentList) {
		this.studentList = studentList;
	}
	
	//@PlanningEntityCollectionProperty
	@ProblemFactCollectionProperty
	public List<StudentCurriculum> getStudentCurriculumList() {
		return studentCurriculumList;
	}
	
	public void setStudentCurriculumList(List<StudentCurriculum> studentCurriculumList) {
		this.studentCurriculumList = studentCurriculumList;
	}
	
	@PlanningEntityCollectionProperty
	public List<CurriculumCourse> getCurriculumCourseList() {
		return curriculumCourseList;
	}
	
	public void setCurriculumCourseList(List<CurriculumCourse> curriculumCourseList) {
		this.curriculumCourseList = curriculumCourseList;
	}
	
	@PlanningEntityCollectionProperty
	public List<CourseSection> getCourseSectionList() {
		return courseSectionList;
	}
	
	public void setCourseSectionList(List<CourseSection> courseSectionList) {
		this.courseSectionList = courseSectionList;
	}

	public boolean validateResult() {
		boolean success = true;
		// 每一个学生的  每个科目都有对应班级
		courseSectionList = courseSectionList.stream().filter(e -> e.getCurrentStudentCount() > 0).collect(Collectors.toList());
		Map<V4Student,Map<V4Course, CourseSection>> stuSectionMap = new HashMap<>();
		
		for (CourseSection section : courseSectionList) {
			Set<V4Student> studentSet = section.getCurrentStudentSet();
			V4Course course = section.getCourse();
			for (V4Student student : studentSet) {
				if(!student.getStudentCurriculum().getCourseList().contains(course)) {
					System.out.println("该学生不需要上这个班级的课");
					success = false;
				}
				
				Map<V4Course, CourseSection> map = stuSectionMap.get(student);
				if(map == null) {
					map = new HashMap<>();
					stuSectionMap.put(student, map);
				}
				map.put(course, section);
			}
		}
		
		// 每个班的 学生 都会上 本班的课
		for (V4Student stu : studentList) {
			List<V4Course> courses = stu.getStudentCurriculum().getCourseList();
			Map<V4Course, CourseSection> sectionMap = stuSectionMap.get(stu);
			
			if(sectionMap == null || !sectionMap.keySet().containsAll(courses)) {
				System.out.println("此学生"+stu.getStudentName()+"对应的课 没有足够教学班");
				success = false;
			}
		}
		
		System.out.println("分半算法 v4结果验证"+(success?"成功":"失败"));
		return success;
	}
}
