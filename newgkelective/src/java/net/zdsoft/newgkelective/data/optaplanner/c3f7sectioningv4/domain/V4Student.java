package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain;

import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common.AbstractPersistable;

//@PlanningEntity
public class V4Student extends AbstractPersistable {
	String studentName;

	String studentId;
	
	Map<V4Course, Integer>	studentCourseScoreMap;  //每门课的成绩
	
	StudentCurriculum  		studentCurriculum; 		//planning variable
	List<StudentCurriculum> curriculumSectionList; 	//domain of studentCurriculum
	
	
	public String getCurriculumCode() {
		return studentCourseScoreMap.keySet().stream()
			.map(e->e.getCourseCode())
			.reduce((e1,e2)->e1+"-"+e2)
			.orElse("");
	}
	public Map<V4Course, Integer> getStudentCourseScoreMap() {
		return studentCourseScoreMap;
	}

	public void setStudentCourseScoreMap(Map<V4Course, Integer> studentCourseScoreMap) {
		this.studentCourseScoreMap = studentCourseScoreMap;
	}

	//@PlanningVariable(valueRangeProviderRefs = { "curriculumSectionRange" })
	public StudentCurriculum getStudentCurriculum() {
		return studentCurriculum;
	}

	public void setStudentCurriculum(StudentCurriculum studentCurriculum) {
		this.studentCurriculum = studentCurriculum;
	}

	public V4Student() {
		super();
	}
	
	public V4Student(String studentName, String studentId) {
		super();
		this.studentName = studentName;
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	public String getStudentId() {
		return studentId;
	}
	
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	
	public StudentCurriculum getCurriculumSection() {
		return studentCurriculum;
	}
	
	public void setCurriculumSection(StudentCurriculum studentCurriculum) {
		this.studentCurriculum = studentCurriculum;
	}
	
	//@ValueRangeProvider(id = "curriculumSectionRange")
	public List<StudentCurriculum> getCurriculumSectionList() {
		return curriculumSectionList;
	}
	
	public void setCurriculumSectionList(List<StudentCurriculum> curriculumSectionList) {
		this.curriculumSectionList = curriculumSectionList;
	}
	
	public int getCourseScoreByCourse(V4Course c) {
		return studentCourseScoreMap.get(c).intValue();
	}
	
}
