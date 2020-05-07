package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

public class CourseSection {
	private Course 			course;
	private List<Student> 	studentList;
	private int             sectionID;
	
	public CourseSection() {
		studentList = new ArrayList<>();
	}

	public CourseSection(Course c) {
		course = c;
		studentList = new ArrayList<>();
	}

	public boolean isConflictWith(CourseSection cs) {
		Set<Student> thisSet = new HashSet<>();
		thisSet.addAll(studentList);
		Set<Student> otherSet = new HashSet<>();
		otherSet.addAll(cs.getStudentList());
		if (Sets.intersection(thisSet, otherSet).size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getSectionCode() {
		return course.getCode() + "-" + sectionID;
	}
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setSectionID(int id) {
		this.sectionID = id;
	}
	
	public int getSectionID () {
		return sectionID;
	}
	
	public int getStudentCount() {
		return studentList.size();
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}
	
	public void addStudentListAll(List<Student> studentList) {
		this.studentList.addAll(studentList);
	}

	public void addStudent(Student s) {
		this.studentList.add(s);
	}
	
	public void clearStudentAll () {
		this.studentList.clear();
	}
}
