package net.zdsoft.gkelective.data.dto;

import java.util.List;

import net.zdsoft.gkelective.data.entity.GkGroupClass;

public class GkGroupDto {
	
	private String conditionName;//例如政史地
	private String subjectIds;//例如政史地
	private List<GkGroupClass> gkGroupClassList;
	private Integer allNumber;//总人数
	private Integer leftNumber;//剩余总人数
	private Integer maleNumber;//男生人数
	private Integer femaleNumber;//女生人数
	
	private int notexists=0;//是否不存在该组合  0:存在 1:不存在
	
	
	public String getConditionName() {
		return conditionName;
	}
	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}
	public List<GkGroupClass> getGkGroupClassList() {
		return gkGroupClassList;
	}
	public void setGkGroupClassList(List<GkGroupClass> gkGroupClassList) {
		this.gkGroupClassList = gkGroupClassList;
	}
	public Integer getAllNumber() {
		return allNumber;
	}
	public void setAllNumber(Integer allNumber) {
		this.allNumber = allNumber;
	}
	public Integer getMaleNumber() {
		return maleNumber;
	}
	public void setMaleNumber(Integer maleNumber) {
		this.maleNumber = maleNumber;
	}
	public Integer getFemaleNumber() {
		return femaleNumber;
	}
	public void setFemaleNumber(Integer femaleNumber) {
		this.femaleNumber = femaleNumber;
	}
	public String getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}
	public int getNotexists() {
		return notexists;
	}
	public void setNotexists(int notexists) {
		this.notexists = notexists;
	}
	public Integer getLeftNumber() {
		return leftNumber;
	}
	public void setLeftNumber(Integer leftNumber) {
		this.leftNumber = leftNumber;
	}
	
}
