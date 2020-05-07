package net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto;

import java.util.List;
import java.util.Set;

public class ThreeGroup {
	//参数
	private String type;//1文、2理
	private Set<String> subjectIds;//三门组合subject
	private String name;
	private List<TwoGroup> twoGroupList;
	private TwoGroup twoGroup;//本身subjectIds，不包括在twoGroupList
	
	private int allStuNum;//所有学生数量
	private int allArrangXzbNum;//分配行政班数量
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
	public List<TwoGroup> getTwoGroupList() {
		return twoGroupList;
	}
	public void setTwoGroupList(List<TwoGroup> twoGroupList) {
		this.twoGroupList = twoGroupList;
	}
	public TwoGroup getTwoGroup() {
		return twoGroup;
	}
	public void setTwoGroup(TwoGroup twoGroup) {
		this.twoGroup = twoGroup;
	}
	public int getAllStuNum() {
		return allStuNum;
	}
	public void setAllStuNum(int allStuNum) {
		this.allStuNum = allStuNum;
	}
	public int getAllArrangXzbNum() {
		return allArrangXzbNum;
	}
	public void setAllArrangXzbNum(int allArrangXzbNum) {
		this.allArrangXzbNum = allArrangXzbNum;
	}
	
	
}
