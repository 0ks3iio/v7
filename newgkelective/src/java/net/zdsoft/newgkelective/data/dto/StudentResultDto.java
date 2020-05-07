package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.newgkelective.data.entity.NewGkChoResult;

public class StudentResultDto {
	private String studentId;
	private String studentName;
	private String classId;
	private String className;
	private String studentCode;
	private String sex;
	private String nojoinChoose;//1:不参与 
	private String lock;//1:锁
	private String oldClassName;
	private String chooseSubjects;
	private List<String> jxbAClasss;
	private List<String> jxbBClasss;
	
	private List<String> otherClasss;
	
	//选择的科目--排序直接根据subjectId排序
	private List<NewGkChoResult> resultList=new ArrayList<NewGkChoResult>();
	private Map<String,Float> subjectScore=new HashMap<String,Float>();
	
	private String choResultStr;
	
	private String[] courseIds;
	
	private Float score;//用于分班依据
	
	//用于纯2+x 3+0模式算法
	private boolean isTwo=false;//是否已经安排2+x
	
	private String mark;//备注
	
	
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
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
	public String getStudentCode() {
		return studentCode;
	}
	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNojoinChoose() {
		return nojoinChoose;
	}
	public void setNojoinChoose(String nojoinChoose) {
		this.nojoinChoose = nojoinChoose;
	}
	public List<NewGkChoResult> getResultList() {
		return resultList;
	}
	public void setResultList(List<NewGkChoResult> resultList) {
		this.resultList = resultList;
	}
	public Map<String, Float> getSubjectScore() {
		return subjectScore;
	}
	public void setSubjectScore(Map<String, Float> subjectScore) {
		this.subjectScore = subjectScore;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getLock() {
		return lock;
	}
	public void setLock(String lock) {
		this.lock = lock;
	}
	public String[] getCourseIds() {
		return courseIds;
	}
	public void setCourseIds(String[] courseIds) {
		this.courseIds = courseIds;
	}
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	public boolean isTwo() {
		return isTwo;
	}
	public void setTwo(boolean isTwo) {
		this.isTwo = isTwo;
	}
	public String getOldClassName() {
		return oldClassName;
	}
	public void setOldClassName(String oldClassName) {
		this.oldClassName = oldClassName;
	}
	public String getChooseSubjects() {
		return chooseSubjects;
	}
	public void setChooseSubjects(String chooseSubjects) {
		this.chooseSubjects = chooseSubjects;
	}
	public List<String> getJxbAClasss() {
		return jxbAClasss;
	}
	public void setJxbAClasss(List<String> jxbAClasss) {
		this.jxbAClasss = jxbAClasss;
	}
	public List<String> getJxbBClasss() {
		return jxbBClasss;
	}
	public void setJxbBClasss(List<String> jxbBClasss) {
		this.jxbBClasss = jxbBClasss;
	}
	public String getChoResultStr() {
		return choResultStr;
	}
	public void setChoResultStr(String choResultStr) {
		this.choResultStr = choResultStr;
	}
	public List<String> getOtherClasss() {
		return otherClasss;
	}
	public void setOtherClasss(List<String> otherClasss) {
		this.otherClasss = otherClasss;
	}
	

}
