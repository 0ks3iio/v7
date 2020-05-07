package net.zdsoft.qulity.data.dto;

public class StudentScoreDto {

	private String studentId;
	private String studentCode;
	private Integer sex;
	private String studentName;
	private String className;
	private String gradeId;
	private String classId;
	private float totalScore;//总分
	private int gradeRank;//总分年级排名
	private int classRank;//总分班级排名
	private float compreScore;//综合素质总分
	private int compreGradeRank;//综合素质总分年级排名
	private float englishScore;//英语笔试总折分
	private int englishGradeRank;//英语笔试总折分年级排名

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public float getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}
	public int getGradeRank() {
		return gradeRank;
	}
	public void setGradeRank(int gradeRank) {
		this.gradeRank = gradeRank;
	}
	public int getClassRank() {
		return classRank;
	}
	public void setClassRank(int classRank) {
		this.classRank = classRank;
	}

	public float getCompreScore() {
		return compreScore;
	}

	public void setCompreScore(float compreScore) {
		this.compreScore = compreScore;
	}

	public int getCompreGradeRank() {
		return compreGradeRank;
	}

	public void setCompreGradeRank(int compreGradeRank) {
		this.compreGradeRank = compreGradeRank;
	}

	public float getEnglishScore() {
		return englishScore;
	}

	public void setEnglishScore(float englishScore) {
		this.englishScore = englishScore;
	}

	public int getEnglishGradeRank() {
		return englishGradeRank;
	}

	public void setEnglishGradeRank(int englishGradeRank) {
		this.englishGradeRank = englishGradeRank;
	}
}
