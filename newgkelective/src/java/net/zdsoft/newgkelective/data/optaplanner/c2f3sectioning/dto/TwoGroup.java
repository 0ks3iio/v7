package net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto;

import java.util.List;
import java.util.Set;


public class TwoGroup {
	//传递的参数
	private Set<String> subjectIds;
	private String name;
	private ThreeGroup threeGroup;//上级
	private List<ChooseStudent> studentList;
	private int studentNum;
	//计算
	private int needClassNum;
//	private int avgMax;//如果不在加入人员情况下，班级平均值
	
	
	public Set<String> getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(Set<String> subjectIds) {
		this.subjectIds = subjectIds;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ThreeGroup getThreeGroup() {
		return threeGroup;
	}
	public void setThreeGroup(ThreeGroup threeGroup) {
		this.threeGroup = threeGroup;
	}
	public List<ChooseStudent> getStudentList() {
		return studentList;
	}
	public void setStudentList(List<ChooseStudent> studentList) {
		this.studentList = studentList;
	}
	public int getStudentNum() {
		return studentNum;
	}
	public void setStudentNum(int studentNum) {
		this.studentNum = studentNum;
	}
	public int getNeedClassNum() {
		return needClassNum;
	}
	public void setNeedClassNum(int needClassNum) {
		this.needClassNum = needClassNum;
	}
//	public int getAvgMax() {
//		return avgMax;
//	}
//	public void setAvgMax(int avgMax) {
//		this.avgMax = avgMax;
//	}
	
}
