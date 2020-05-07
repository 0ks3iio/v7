package net.zdsoft.tutor.data.dto;

import net.zdsoft.basedata.entity.Student;

/**
 * @author chenlw
 *
 */
public class TutorStuTeaDto {

	private Student student;
	private String tutorTeaId;
	private String tutorTeaName;
	private boolean haveTutor;

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getTutorTeaId() {
		return tutorTeaId;
	}

	public void setTutorTeaId(String tutorTeaId) {
		this.tutorTeaId = tutorTeaId;
	}

	public String getTutorTeaName() {
		return tutorTeaName;
	}

	public void setTutorTeaName(String tutorTeaName) {
		this.tutorTeaName = tutorTeaName;
	}

	public boolean isHaveTutor() {
		return haveTutor;
	}

	public void setHaveTutor(boolean haveTutor) {
		this.haveTutor = haveTutor;
	}

	
	
}
