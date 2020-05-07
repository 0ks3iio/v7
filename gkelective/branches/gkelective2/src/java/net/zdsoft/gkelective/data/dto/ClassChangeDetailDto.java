package net.zdsoft.gkelective.data.dto;

import java.util.HashMap;
import java.util.Map;

public class ClassChangeDetailDto {
	
	private String classId;
	private Integer allCountNum;//总人数
	private Integer mCountNum;//男生人数
	private Integer wCountNum;//女生人数
	private Map<String,Double> courseScore = new HashMap<String, Double>();//科目对应平均分
	private Map<String,Map<String,Double>> studentCourseScore = new HashMap<String, Map<String,Double>>();//学生对应科目对应分数
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public Integer getAllCountNum() {
		return allCountNum;
	}
	public void setAllCountNum(Integer allCountNum) {
		this.allCountNum = allCountNum;
	}
	public Integer getmCountNum() {
		return mCountNum;
	}
	public void setmCountNum(Integer mCountNum) {
		this.mCountNum = mCountNum;
	}
	public Integer getwCountNum() {
		return wCountNum;
	}
	public void setwCountNum(Integer wCountNum) {
		this.wCountNum = wCountNum;
	}
	public Map<String, Double> getCourseScore() {
		return courseScore;
	}
	public void setCourseScore(Map<String, Double> courseScore) {
		this.courseScore = courseScore;
	}
	public Map<String, Map<String, Double>> getStudentCourseScore() {
		return studentCourseScore;
	}
	public void setStudentCourseScore(Map<String, Map<String, Double>> studentCourseScore) {
		this.studentCourseScore = studentCourseScore;
	}
	
}
