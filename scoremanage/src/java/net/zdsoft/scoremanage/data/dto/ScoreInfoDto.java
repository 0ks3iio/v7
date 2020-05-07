package net.zdsoft.scoremanage.data.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.zdsoft.scoremanage.data.entity.ScoreInfo;


public class ScoreInfoDto {
	
	//-----------------页面数据组装------------
	private String stuId;
	private String stuName;//姓名
	private String stuExamNum;//考号
	private String stuCode;//学号
	private String className;//班级名称
	private String unitiveCode;//学籍号
	private String clsInnerCode;//班内编号
	//状态
	private String scoreStatus;//成绩状态
	private String score;
	private String toScore;  //  学分/总评成绩
	private String scoreId;//成绩id;
	private String classId;//行政班
	private String teachClassId;//教学班
	private Map<String,ScoreInfo> scoreInfoMap;//成绩(<subjectId,score>)
	
	
	//-----成绩录入form提交保存某个班级数据-----------
	private List<ScoreInfo> dtoList;//表格数据
	//总体参数
	private String examId;
	private String subjectId;
	private String unitId;
	private String subjectInfoId;
	private String classIdSearch;//页面查询
	private String classType;
	private String inputType;
	private String gradeType;
	private Integer fullMark;
	
	
	public String getUnitiveCode() {
		return unitiveCode;
	}
	public void setUnitiveCode(String unitiveCode) {
		this.unitiveCode = unitiveCode;
	}
	//打印信息
	private String writingScore;
	private String speehScore;
	private List<String> bindingScores = new ArrayList<>();
	
	public String getStuId() {
		return stuId;
	}
	public void setStuId(String stuId) {
		this.stuId = stuId;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getStuExamNum() {
		return stuExamNum;
	}
	public void setStuExamNum(String stuExamNum) {
		this.stuExamNum = stuExamNum;
	}
	public String getStuCode() {
		return stuCode;
	}
	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getScoreId() {
		return scoreId;
	}
	public void setScoreId(String scoreId) {
		this.scoreId = scoreId;
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
	public String getScoreStatus() {
		return scoreStatus;
	}
	public void setScoreStatus(String scoreStatus) {
		this.scoreStatus = scoreStatus;
	}
	public List<ScoreInfo> getDtoList() {
		return dtoList;
	}
	public void setDtoList(List<ScoreInfo> dtoList) {
		this.dtoList = dtoList;
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
	public String getSubjectInfoId() {
		return subjectInfoId;
	}
	public void setSubjectInfoId(String subjectInfoId) {
		this.subjectInfoId = subjectInfoId;
	}
	public String getClassIdSearch() {
		return classIdSearch;
	}
	public void setClassIdSearch(String classIdSearch) {
		this.classIdSearch = classIdSearch;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public String getGradeType() {
		return gradeType;
	}
	public void setGradeType(String gradeType) {
		this.gradeType = gradeType;
	}
	public Map<String, ScoreInfo> getScoreInfoMap() {
		return scoreInfoMap;
	}
	public void setScoreInfoMap(Map<String, ScoreInfo> scoreInfoMap) {
		this.scoreInfoMap = scoreInfoMap;
	}
	public String getToScore() {
		return toScore;
	}
	public void setToScore(String toScore) {
		this.toScore = toScore;
	}
	public String getWritingScore() {
		return writingScore;
	}
	public void setWritingScore(String writingScore) {
		this.writingScore = writingScore;
	}
	public String getSpeehScore() {
		return speehScore;
	}
	public void setSpeehScore(String speehScore) {
		this.speehScore = speehScore;
	}
	public Integer getFullMark() {
		return fullMark;
	}
	public void setFullMark(Integer fullMark) {
		this.fullMark = fullMark;
	}
	public List<String> getBindingScores() {
		return bindingScores;
	}
	public void setBindingScores(List<String> bindingScores) {
		this.bindingScores = bindingScores;
	}
	public String getClsInnerCode() {
		return clsInnerCode;
	}
	public void setClsInnerCode(String clsInnerCode) {
		this.clsInnerCode = clsInnerCode;
	}
	
}
