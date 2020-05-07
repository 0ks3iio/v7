package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common.AbstractPersistable;

//@PlanningEntity
public class StudentCurriculum extends AbstractPersistable{
	Curriculum 	curriculum;
	int			indexInCurriculum;
	
	
	List<V4Student>		studentList = new ArrayList<>(); //inverse planning variable

	private List<V4Course> courseInStucurriculumList;
	
	//@InverseRelationShadowVariable(sourceVariableName = "studentCurriculum")
	public List<V4Student> getStudentList () {
		return studentList;
	}
	
	public BitSet getStudentBitSet () {
		BitSet studentBitSet = new BitSet();
		studentList.stream().forEach(e -> studentBitSet.set(e.getId()));
		return studentBitSet;
	}
	
	public void addStudent(V4Student sutdent) {
		if(studentList == null) {
			studentList = new ArrayList<>();
		}
		studentList.add(sutdent);
	}
	public void addStudents(List<V4Student> sutdents) {
		if(studentList == null) {
			studentList = new ArrayList<>();
		}
		studentList.addAll(sutdents);
	}
	public StudentCurriculum() {
		super();
	}

	public StudentCurriculum(Curriculum curriculum, int indexInCurriculum) {
		super();
		this.curriculum = curriculum;
		this.indexInCurriculum = indexInCurriculum;
	}
	public List<V4Course> getCourseList(){
		if(courseInStucurriculumList != null) {
			return courseInStucurriculumList;
		}
		return curriculum.getCourseInCurriculumList();
	}
	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	public int getIndexInCurriculum() {
		return indexInCurriculum;
	}

	public void setIndexInCurriculum(int indexInCurriculum) {
		this.indexInCurriculum = indexInCurriculum;
	}

	public void setStudentList(List<V4Student> studentList) {
		this.studentList = studentList;
	}

	public void setCourseInStucurriculumList(List<V4Course> courseInStucurriculumList) {
		this.courseInStucurriculumList = courseInStucurriculumList;
	}
	
	
}
