package net.zdsoft.gkelective.data.dto;

import java.util.HashMap;
import java.util.Map;

import net.zdsoft.basedata.entity.Student;

public class GkStuScoreDto {

	private Student student;
	private Map<String,Double> subjectScore = new HashMap<String,Double>();
	
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public Map<String, Double> getSubjectScore() {
		return subjectScore;
	}
	public void setSubjectScore(Map<String, Double> subjectScore) {
		this.subjectScore = subjectScore;
	}
	
}
