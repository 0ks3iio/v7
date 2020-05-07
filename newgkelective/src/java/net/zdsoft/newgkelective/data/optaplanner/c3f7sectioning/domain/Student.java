package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.AbstractPersistable;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain.Course;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain.Curriculum;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.StudentVO;

public class Student extends AbstractPersistable{
	private StudentVO   rawStudent;
	private String    	studentName;
	private String    	studentID;
	
	private String 		curriculumCode; //the 3 A course names glued together, as the Key for the curriculum
	private Curriculum  curriculum;		//one of the 35 combinations
	
	private Set<String> aCourseNameSet;
	private Set<String> bCourseNameSet;
	private Set<String> allCourseNameSet;

	public Student (StudentVO rawStudent, Set<String> allANameSet) {
		this.rawStudent = rawStudent;
		studentName = rawStudent.getName();
		studentID = rawStudent.getStuId();
		
		aCourseNameSet = new TreeSet<String> ();
		aCourseNameSet.add(rawStudent.getChooseSubject1() + "A");
		aCourseNameSet.add(rawStudent.getChooseSubject2() + "A");
		aCourseNameSet.add(rawStudent.getChooseSubject3() + "A");
		
		Set<String> notMyA = Sets.difference(allANameSet, aCourseNameSet);
		bCourseNameSet = notMyA.stream().map(e -> e.substring(0, e.length() - 1) + "B").collect(Collectors.toSet());
		
		allCourseNameSet = new TreeSet<String> ();
		allCourseNameSet.addAll(aCourseNameSet);
		allCourseNameSet.addAll(bCourseNameSet);
		
		curriculumCode = "";
		for (String s : aCourseNameSet) {
			curriculumCode += s;
		}
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getCurriculumCode() {
		return curriculumCode;
	}

	public void setCurriculumCode(String curriculumCode) {
		this.curriculumCode = curriculumCode;
	}

	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	public Set<String> getACourseNameSet() {
		return aCourseNameSet;
	}

	public void setACourseNameSet(Set<String> aCourseNameSet) {
		this.aCourseNameSet = aCourseNameSet;
	}

	public Set<String> getBCourseNameSet() {
		return bCourseNameSet;
	}

	public void setBCourseNameSet(Set<String> bCourseNameSet) {
		this.bCourseNameSet = bCourseNameSet;
	}

	public Set<String> getAllCourseNameSet() {
		return allCourseNameSet;
	}

	public void setAllCourseNameSet(Set<String> allCourseNameSet) {
		this.allCourseNameSet = allCourseNameSet;
	}

	@Override
    public String toString() {
        return studentID + "\t " + studentName;
    }	
}
