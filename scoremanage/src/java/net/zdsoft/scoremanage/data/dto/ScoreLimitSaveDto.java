package net.zdsoft.scoremanage.data.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.scoremanage.data.entity.ScoreLimit;

public class ScoreLimitSaveDto {
	
	private String gradeCode;
	//ScoreLimit 中id,teacherId,classIds,teachClassIds
//	public List<ScoreLimit> scoreLimitList;
	
	//页面展现
	public String classId;
	public String classType;//1:行政班 2:教学班
	public String className;
	public Map<String,String> teacherMap=new HashMap<String,String>();
	
	//保存参数
	private String acadyear;
	private String semester;
	private String examId;
	private String unitId;
	private String subjectId;
	
	//classId,teacherIds;
	public List<ScoreLimit> scoreLimitList=new ArrayList<ScoreLimit>();
	
	
	
	
	
	

	public List<ScoreLimit> getScoreLimitList() {
		return scoreLimitList;
	}

	public void setScoreLimitList(List<ScoreLimit> scoreLimitList) {
		this.scoreLimitList = scoreLimitList;
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

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, String> getTeacherMap() {
		return teacherMap;
	}

	public void setTeacherMap(Map<String, String> teacherMap) {
		this.teacherMap = teacherMap;
	}

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

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}
	
}
