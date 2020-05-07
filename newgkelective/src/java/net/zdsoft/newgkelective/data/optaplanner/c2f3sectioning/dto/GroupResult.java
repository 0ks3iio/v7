package net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto;

import java.util.List;
import java.util.Set;

public class GroupResult {
	private TwoGroup twoGroup;
	private Set<String> subjectIds;
	private List<ChooseStudent> stuIdList;
	private int stuNum;//stuIdList的数量
	private String subjectType;//3科 两科
	
	public Set<String> getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(Set<String> subjectIds) {
		this.subjectIds = subjectIds;
	}

	public List<ChooseStudent> getStuIdList() {
		return stuIdList;
	}

	public void setStuIdList(List<ChooseStudent> stuIdList) {
		this.stuIdList = stuIdList;
	}

	public int getStuNum() {
		return stuNum;
	}

	public void setStuNum(int stuNum) {
		this.stuNum = stuNum;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public TwoGroup getTwoGroup() {
		return twoGroup;
	}

	public void setTwoGroup(TwoGroup twoGroup) {
		this.twoGroup = twoGroup;
	}



}
