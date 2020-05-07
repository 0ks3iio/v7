package net.zdsoft.newgkelective.data.dto;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;

public class NewGkDivideDto {
	
	
	private NewGkDivide ent;
	//7选3 
	private String courseA;
	private String courseB;
	private String courseO;//行政班课程
	//文理
	//理科教学班科目语数英
	private String courselkysy;
	//理科科目物化生
	private String courselk;
	//理科行政班
	private String courselO;
	//文科教学班科目语数英
	private String coursewkysy;
	//文科科目政史地
	private String coursewk;
	//文科行政班
	private String coursewO;
	
	private String referScoreId; //参考成绩id
	private String choiceId; //选课id
	private String openType;
	private String recombination;
	
	/******************以上新增分班需要用到的参数*******************/
	
	
	
	private int courseNumber;
	private int classSum;
	private int MaxiMumClassSize;
	private int miniMumClassSize;
	private int divideStudentNum;
	private int noDivideStudentNum;
	
	
	
	
	
	
	private boolean haveDivideIng;
	private int teaClsNum;// 教学班数量
	private int baseClsNum;// 行政班数量
	private int threeSubClsNum;// 三科相同班级数量
	private int maxStuNum;// 实际班级最大人数
	private int minStuNum;// 实际班级最小人数
	private List<String[]> threeSubCls;// 三科相同班级数据[3科目名称，数量]
	private List<String[]> teaCls;// 各科教学班数据[科目名称，数量]
	private List<NewGkDivideClass> baseClsList;
	private List<NewGkDivideClass> teaClsList;
	private Map<String, Integer> subTeaNum;
	
	/******************以下分班首页展示需要用到的参数*******************/
	private int xzbAllclassNum;
	private int threeAllclassNum;//7选3展现 3科
	private int twoAllclassNum;//7选3展现 2科
	private int oneAllclassNum;//7选3展现 1科
	private int mixAllclassNum;//7选3展现 0科
	private List<String[]> showCourseList=new ArrayList<String[]>();//[subjectId,subjectName]
	private String[] showFen;//分层：7选3展现（A,B） 文理科（A,B,C）
	private Map<String,Integer> abAllclassNum;//key:subjectId_subjectType
	private String errorMess;//错误消息
	
	// 纳入行政班的选考班 /学考班
	private String[] xzbToJxbs;
	
	public int getDivideStudentNum() {
		return divideStudentNum;
	}
	public void setDivideStudentNum(int divideStudentNum) {
		this.divideStudentNum = divideStudentNum;
	}
	public int getNoDivideStudentNum() {
		return noDivideStudentNum;
	}
	public void setNoDivideStudentNum(int noDivideStudentNum) {
		this.noDivideStudentNum = noDivideStudentNum;
	}
	
	public boolean isHaveDivideIng() {
		return haveDivideIng;
	}
	public void setHaveDivideIng(boolean haveDivideIng) {
		this.haveDivideIng = haveDivideIng;
	}
	public String getReferScoreId() {
		return referScoreId;
	}
	public void setReferScoreId(String referScoreId) {
		this.referScoreId = referScoreId;
	}
	public String getChoiceId() {
		return choiceId;
	}
	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
	}
	public String getOpenType() {
		return openType;
	}
	public void setOpenType(String openType) {
		this.openType = openType;
	}
	public NewGkDivide getEnt() {
		return ent;
	}
	public void setEnt(NewGkDivide ent) {
		this.ent = ent;
	}
	public int getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(int courseNumber) {
		this.courseNumber = courseNumber;
	}
	public int getClassSum() {
		return classSum;
	}
	public void setClassSum(int classSum) {
		this.classSum = classSum;
	}
	public int getMaxiMumClassSize() {
		return MaxiMumClassSize;
	}
	public void setMaxiMumClassSize(int maxiMumClassSize) {
		MaxiMumClassSize = maxiMumClassSize;
	}
	public int getMiniMumClassSize() {
		return miniMumClassSize;
	}
	public void setMiniMumClassSize(int miniMumClassSize) {
		this.miniMumClassSize = miniMumClassSize;
	}
	public String getCourseA() {
		return courseA;
	}
	public void setCourseA(String courseA) {
		this.courseA = courseA;
	}
	public String getCourseB() {
		return courseB;
	}
	public void setCourseB(String courseB) {
		this.courseB = courseB;
	}
	public String getCourseO() {
		return courseO;
	}
	public void setCourseO(String courseO) {
		this.courseO = courseO;
	}
	public int getBaseClsNum() {
		return baseClsNum;
	}
	public void setBaseClsNum(int baseClsNum) {
		this.baseClsNum = baseClsNum;
	}
	public int getThreeSubClsNum() {
		return threeSubClsNum;
	}
	public void setThreeSubClsNum(int threeSubClsNum) {
		this.threeSubClsNum = threeSubClsNum;
	}
	public List<String[]> getThreeSubCls() {
		return threeSubCls;
	}
	public void setThreeSubCls(List<String[]> threeSubCls) {
		this.threeSubCls = threeSubCls;
	}
	public List<NewGkDivideClass> getBaseClsList() {
		return baseClsList;
	}
	public void setBaseClsList(List<NewGkDivideClass> baseClsList) {
		this.baseClsList = baseClsList;
	}
	public List<NewGkDivideClass> getTeaClsList() {
		return teaClsList;
	}
	public void setTeaClsList(List<NewGkDivideClass> teaClsList) {
		this.teaClsList = teaClsList;
	}
	public int getMaxStuNum() {
		return maxStuNum;
	}
	public void setMaxStuNum(int maxStuNum) {
		this.maxStuNum = maxStuNum;
	}
	public int getMinStuNum() {
		return minStuNum;
	}
	public void setMinStuNum(int minStuNum) {
		this.minStuNum = minStuNum;
	}
	public List<String[]> getTeaCls() {
		return teaCls;
	}
	public void setTeaCls(List<String[]> teaCls) {
		this.teaCls = teaCls;
	}
	public int getTeaClsNum() {
		return teaClsNum;
	}
	public void setTeaClsNum(int teaClsNum) {
		this.teaClsNum = teaClsNum;
	}
	public Map<String, Integer> getSubTeaNum() {
		return subTeaNum;
	}
	public void setSubTeaNum(Map<String, Integer> subTeaNum) {
		this.subTeaNum = subTeaNum;
	}
	public int getXzbAllclassNum() {
		return xzbAllclassNum;
	}
	public void setXzbAllclassNum(int xzbAllclassNum) {
		this.xzbAllclassNum = xzbAllclassNum;
	}
	public int getThreeAllclassNum() {
		return threeAllclassNum;
	}
	public void setThreeAllclassNum(int threeAllclassNum) {
		this.threeAllclassNum = threeAllclassNum;
	}
	public int getTwoAllclassNum() {
		return twoAllclassNum;
	}
	public void setTwoAllclassNum(int twoAllclassNum) {
		this.twoAllclassNum = twoAllclassNum;
	}
	public List<String[]> getShowCourseList() {
		return showCourseList;
	}
	public void setShowCourseList(List<String[]> showCourseList) {
		this.showCourseList = showCourseList;
	}
	public Map<String, Integer> getAbAllclassNum() {
		return abAllclassNum;
	}
	public void setAbAllclassNum(Map<String, Integer> abAllclassNum) {
		this.abAllclassNum = abAllclassNum;
	}
	public String getCourselkysy() {
		return courselkysy;
	}
	public void setCourselkysy(String courselkysy) {
		this.courselkysy = courselkysy;
	}
	public String getCourselk() {
		return courselk;
	}
	public void setCourselk(String courselk) {
		this.courselk = courselk;
	}
	public String getCourselO() {
		return courselO;
	}
	public void setCourselO(String courselO) {
		this.courselO = courselO;
	}
	public String getCoursewkysy() {
		return coursewkysy;
	}
	public void setCoursewkysy(String coursewkysy) {
		this.coursewkysy = coursewkysy;
	}
	public String getCoursewk() {
		return coursewk;
	}
	public void setCoursewk(String coursewk) {
		this.coursewk = coursewk;
	}
	public String getCoursewO() {
		return coursewO;
	}
	public void setCoursewO(String coursewO) {
		this.coursewO = coursewO;
	}
	public String[] getShowFen() {
		return showFen;
	}
	public void setShowFen(String[] showFen) {
		this.showFen = showFen;
	}
	public String getErrorMess() {
		return errorMess;
	}
	public void setErrorMess(String errorMess) {
		this.errorMess = errorMess;
	}
	public String[] getXzbToJxbs() {
		return xzbToJxbs;
	}
	public void setXzbToJxbs(String[] xzbToJxbs) {
		this.xzbToJxbs = xzbToJxbs;
	}
	public int getOneAllclassNum() {
		return oneAllclassNum;
	}
	public void setOneAllclassNum(int oneAllclassNum) {
		this.oneAllclassNum = oneAllclassNum;
	}
	public int getMixAllclassNum() {
		return mixAllclassNum;
	}
	public void setMixAllclassNum(int mixAllclassNum) {
		this.mixAllclassNum = mixAllclassNum;
	}
	public String getRecombination() {
		return recombination;
	}
	public void setRecombination(String recombination) {
		this.recombination = recombination;
	}
	
}
