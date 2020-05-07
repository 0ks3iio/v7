package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;

public class DivideResultDto {
	private String subjectId;
	private String courseName;
	private String className;
	private String classType;
	private int totalNum;
	private int levelNum;

	private Map<String, List<NewGkDivideClass>> levelMap;
	private Map<String, Map<String, List<NewGkDivideClass>>> levelBatchMap = new HashMap<String, Map<String,List<NewGkDivideClass>>>();

	
	/*  */
	private Integer aClassNum;
	private Integer aStuNum;
	
	private Integer aXZBNum;
	private Integer aXZBStuNum;
	
	private Integer bClassNum;
	private Integer bStuNum;
	
	private Integer bXZBNum;
	private Integer bXZBStuNum;
	
	/*按照行政班上课的教学班*/
	private List<NewGkDivideClass> asXzbClassList = new ArrayList<NewGkDivideClass>();
	/*走班上课的教学班*/
	private List<NewGkDivideClass> normalClassList = new ArrayList<NewGkDivideClass>();
	
	private Map<String,List<NewGkDivideClass>> batchClassListMap=new HashMap<>();
//	/*批次1*/
//	private List<NewGkDivideClass> batchClassList1 = new ArrayList<NewGkDivideClass>();
//	/*批次2*/
//	private List<NewGkDivideClass> batchClassList2 = new ArrayList<NewGkDivideClass>();
//	/*批次3*/
//	private List<NewGkDivideClass> batchClassList3 = new ArrayList<NewGkDivideClass>();
//	
	private List<String> studentList=new ArrayList<>();
	
	
	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getLevelNum() {
		return levelNum;
	}

	public void setLevelNum(int levelNum) {
		this.levelNum = levelNum;
	}

	public Map<String, List<NewGkDivideClass>> getLevelMap() {
		return levelMap;
	}

	public void setLevelMap(Map<String, List<NewGkDivideClass>> levelMap) {
		this.levelMap = levelMap;
	}

	public Integer getaClassNum() {
		return aClassNum;
	}

	public void setaClassNum(Integer aClassNum) {
		this.aClassNum = aClassNum;
	}

	public Integer getaStuNum() {
		return aStuNum;
	}

	public void setaStuNum(Integer aStuNum) {
		this.aStuNum = aStuNum;
	}

	public Integer getaXZBNum() {
		return aXZBNum;
	}

	public void setaXZBNum(Integer aXZBNum) {
		this.aXZBNum = aXZBNum;
	}

	public Integer getaXZBStuNum() {
		return aXZBStuNum;
	}

	public void setaXZBStuNum(Integer aXZBStuNum) {
		this.aXZBStuNum = aXZBStuNum;
	}

	public Integer getbClassNum() {
		return bClassNum;
	}

	public void setbClassNum(Integer bClassNum) {
		this.bClassNum = bClassNum;
	}

	public Integer getbStuNum() {
		return bStuNum;
	}

	public void setbStuNum(Integer bStuNum) {
		this.bStuNum = bStuNum;
	}

	public Integer getbXZBNum() {
		return bXZBNum;
	}

	public void setbXZBNum(Integer bXZBNum) {
		this.bXZBNum = bXZBNum;
	}

	public Integer getbXZBStuNum() {
		return bXZBStuNum;
	}

	public void setbXZBStuNum(Integer bXZBStuNum) {
		this.bXZBStuNum = bXZBStuNum;
	}

	public List<NewGkDivideClass> getAsXzbClassList() {
		return asXzbClassList;
	}

	public void setAsXzbClassList(List<NewGkDivideClass> asXzbClassList) {
		this.asXzbClassList = asXzbClassList;
	}

	public List<NewGkDivideClass> getNormalClassList() {
		return normalClassList;
	}

	public void setNormalClassList(List<NewGkDivideClass> normalClassList) {
		this.normalClassList = normalClassList;
	}

//	public List<NewGkDivideClass> getBatchClassList1() {
//		return batchClassList1;
//	}
//
//	public void setBatchClassList1(List<NewGkDivideClass> batchClassList1) {
//		this.batchClassList1 = batchClassList1;
//	}
//
//	public List<NewGkDivideClass> getBatchClassList2() {
//		return batchClassList2;
//	}
//
//	public void setBatchClassList2(List<NewGkDivideClass> batchClassList2) {
//		this.batchClassList2 = batchClassList2;
//	}
//
//	public List<NewGkDivideClass> getBatchClassList3() {
//		return batchClassList3;
//	}
//
//	public void setBatchClassList3(List<NewGkDivideClass> batchClassList3) {
//		this.batchClassList3 = batchClassList3;
//	}

	public Map<String, Map<String, List<NewGkDivideClass>>> getLevelBatchMap() {
		return levelBatchMap;
	}

	public void setLevelBatchMap(Map<String, Map<String, List<NewGkDivideClass>>> levelBatchMap) {
		this.levelBatchMap = levelBatchMap;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public List<String> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<String> studentList) {
		this.studentList = studentList;
	}

	public Map<String, List<NewGkDivideClass>> getBatchClassListMap() {
		return batchClassListMap;
	}

	public void setBatchClassListMap(Map<String, List<NewGkDivideClass>> batchClassListMap) {
		this.batchClassListMap = batchClassListMap;
	}
	
}
