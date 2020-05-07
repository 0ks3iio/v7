package net.zdsoft.diathesis.data.dto;

import java.io.Serializable;
import java.util.List;

public class DiatheisSubjectScoreDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String subjectId;
	public String subjectName;
	
	public String timeId;
	public String score;
	public String credit;
	public String chargePerson;
	private List<DiathesisFieldDto> fieldList;

	public List<DiathesisFieldDto> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<DiathesisFieldDto> fieldList) {
		this.fieldList = fieldList;
	}

	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getChargePerson() {
		return chargePerson;
	}
	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}
}
