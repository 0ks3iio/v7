package net.zdsoft.eclasscard.data.dto;

public class PushDingMsgDto {

	private String studentId;
	private String attId;
	private String studentName;
	private String classId;
	private String classAllName;
	private String classTeaId;
	private String classTeaUserId;
	private String gradeId;
//	private String gradeName;
	private String gradeTeaId;
	private String gradeTeaUserId;
//	private String subjectId;
	private String subjectName;
	private String statusStr;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getAttId() {
		return attId;
	}

	public void setAttId(String attId) {
		this.attId = attId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassAllName() {
		return classAllName;
	}

	public void setClassAllName(String classAllName) {
		this.classAllName = classAllName;
	}

	public String getClassTeaId() {
		return classTeaId;
	}

	public void setClassTeaId(String classTeaId) {
		this.classTeaId = classTeaId;
	}

	public String getClassTeaUserId() {
		return classTeaUserId;
	}

	public void setClassTeaUserId(String classTeaUserId) {
		this.classTeaUserId = classTeaUserId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeTeaId() {
		return gradeTeaId;
	}

	public void setGradeTeaId(String gradeTeaId) {
		this.gradeTeaId = gradeTeaId;
	}

	public String getGradeTeaUserId() {
		return gradeTeaUserId;
	}

	public void setGradeTeaUserId(String gradeTeaUserId) {
		this.gradeTeaUserId = gradeTeaUserId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	@Override
	public String toString() {
		return "PushDingMsgDto [studentId=" + studentId + ", attId=" + attId
				+ ", studentName=" + studentName + ", classId=" + classId
				+ ", classAllName=" + classAllName + ", classTeaId="
				+ classTeaId + ", classTeaUserId=" + classTeaUserId
				+ ", gradeId=" + gradeId 
				+ ", gradeTeaId=" + gradeTeaId + ", gradeTeaUserId="
				+ gradeTeaUserId + ", subjectName=" + subjectName
				+ ", statusStr=" + statusStr + "]";
	}
	

}
