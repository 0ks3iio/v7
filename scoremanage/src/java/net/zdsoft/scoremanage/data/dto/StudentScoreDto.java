package net.zdsoft.scoremanage.data.dto;

/**
 * 学生成绩查询dto
 */
public class StudentScoreDto {
	
	private String subjectId; //科目id
	private String subjectName;//科目名称
	private String className;//班级名称
	private String classId;//班级id
	private String isLock;//是否录入
	private String score;//成绩
	private String toScore;//总评成绩|学分
	private String isPass;//是否及格
	private String scoreStatus;//状态
	
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
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getIsLock() {
		return isLock;
	}
	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getToScore() {
		return toScore;
	}
	public void setToScore(String toScore) {
		this.toScore = toScore;
	}
	public String getIsPass() {
		return isPass;
	}
	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}
	public String getScoreStatus() {
		return scoreStatus;
	}
	public void setScoreStatus(String scoreStatus) {
		this.scoreStatus = scoreStatus;
	}

}
