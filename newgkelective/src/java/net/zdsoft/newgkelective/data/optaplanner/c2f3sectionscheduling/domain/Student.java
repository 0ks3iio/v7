package net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain;

import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.common.AbstractPersistable;
/**
 * 算法中学生对象
 */
public class Student extends AbstractPersistable {
	String studentName;
	String studentId;
	String groupClassId;//已经位于的组合班

	public Student() {
		
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

	@Override
	public String toString () {
		return studentName + "-" + studentId;
	}
}
