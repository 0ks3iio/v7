package net.zdsoft.scoremanage.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="scoremanage_info")
public class ScoreInfo extends BaseEntity<String>{

	/**
     * 成绩录入方式：分数或等第
     */
    public static final String ACHI_SCORE = "S";// 分数
    public static final String ACHI_GRADE = "G";// 等第
    
    public static final String SCORE_TYPE_0="0";//成绩状态正常
    public static final String SCORE_TYPE_1="1";//成绩状态缺考
    public static final String SCORE_TYPE_2="2";//成绩状态作弊
	public static final String SCORE_TYPE_3="3";//成绩状态本次未考
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String acadyear;
	private String semester;
	private String examId;
	private String subjectId;
	private String unitId;
	private String classId;//行政班
	private String teachClassId;//教学班
	private String studentId;
	private String scoreStatus;//成绩状态 DM-CJLX  正常 缺考 作弊
	private String score;//原始分
	private String gradeType;//几等分
	/**
	 * 如果examId是32个0 那就是选修课维护的学分值
	 * 如果examId考试类型是期末考试那么这个值是总评成绩
	 * 如果examId考试类型是7选3 就是转换后的赋分值
	 * 如果examId考试录入方式类型等第是转换后的分数值
	 */
	private String toScore;//赋分值
	private Date creationTime;
	private Date modifyTime;
	private String operatorId;
	private String inputType;//成绩录入方式
	private String isInStat;//是否计入统计（统计之后是否可以修改）
	
	@Transient
	private Float allScore;
	@Transient
	private String subjectName;
	@Transient
	private String courseTypeName;
	@Transient
	private String studentName;
	@Transient
	private String clsName;
	@Transient
	private String studentCode;
	@Transient
	private String clsCode;
	@Transient
	private String resitScore;//补考分
	@Transient
	private String subjectCode;

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getTeachClassId() {
		return teachClassId;
	}

	public void setTeachClassId(String teachClassId) {
		this.teachClassId = teachClassId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getScoreStatus() {
		return scoreStatus;
	}

	public void setScoreStatus(String scoreStatus) {
		this.scoreStatus = scoreStatus;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getGradeType() {
		return gradeType;
	}

	public void setGradeType(String gradeType) {
		this.gradeType = gradeType;
	}

	public String getToScore() {
		return toScore;
	}

	public void setToScore(String toScore) {
		this.toScore = toScore;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getIsInStat() {
		return isInStat;
	}

	public void setIsInStat(String isInStat) {
		this.isInStat = isInStat;
	}

	public Float getAllScore() {
		return allScore;
	}

	public void setAllScore(Float allScore) {
		this.allScore = allScore;
	}

	@Override
	public String fetchCacheEntitName() {
		return "scoreInfo";
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getCourseTypeName() {
		return courseTypeName;
	}

	public void setCourseTypeName(String courseTypeName) {
		this.courseTypeName = courseTypeName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getClsName() {
		return clsName;
	}

	public void setClsName(String clsName) {
		this.clsName = clsName;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public String getClsCode() {
		return clsCode;
	}

	public void setClsCode(String clsCode) {
		this.clsCode = clsCode;
	}

	public String getResitScore() {
		return resitScore;
	}

	public void setResitScore(String resitScore) {
		this.resitScore = resitScore;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

}
