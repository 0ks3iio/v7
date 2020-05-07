package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning3.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sectioning2A1XInput {
//	//算法参数1：每个2A组合最多由几个3A组合构成，在6选3的情况下，此值如果是3，说明还有一门B课可以作为行政班
//	@JsonProperty
//	private int maxGroup3ACountPerGroup2A = 3;
	
	//算法参数2：平均班级人数
	@JsonProperty
	private int sectionSizeMean = 50;
	
	//算法参数3：平均班级人数的误差值，也就是说，可以额外多塞几个人
	@JsonProperty
	private int sectionSizeMargin = 8;
	
	//算法参数4：总的教室数量
	@JsonProperty
	private int maxRoomCount = 20;
	
	//算法参数5：期望3A独立开班的班级数
	@JsonProperty
	private int pure3ASectionCount = 0;
	

	//输入1：group3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	@JsonProperty
	private List<List<String>> group3AList;
	
	//输入2： maxTeacherCountList，每门课的老师数量： {{<课名><老师数量供1X部分使用>}}
	@JsonProperty
	private List<List<String>> maxTeacherCountList;
	
	//输入3：excludedGroup2AList，不允许出现的2A组合: {<选课1-2A><选课2-2A>}
	@JsonProperty
	private List<List<String>> excludedGroup2AList;
	
	//输入4：pre1XList，手动2+X遗留下来的1X部分: {<选课><人数>}
	@JsonProperty
	private List<List<String>> pre1XList;

	//输入5： 每门课的班级平均人数
	//  sectionSizeList: {<选课><每个教学班平均人数><误差大小><教学班数量>}
	@JsonProperty
	private List<List<String>> sectionSizeList;	
	
	public List<List<String>> getSectionSizeList() {
		return sectionSizeList;
	}

	public void setSectionSizeList(List<List<String>> sectionSizeList) {
		this.sectionSizeList = sectionSizeList;
	}

//	public int getMaxGroup3ACountPerGroup2A() {
//		return maxGroup3ACountPerGroup2A;
//	}
//
//	public void setMaxGroup3ACountPerGroup2A(int maxGroup3ACountPerGroup2A) {
//		this.maxGroup3ACountPerGroup2A = maxGroup3ACountPerGroup2A;
//	}

	public int getSectionSizeMean() {
		return sectionSizeMean;
	}

	public void setSectionSizeMean(int sectionSizeMean) {
		this.sectionSizeMean = sectionSizeMean;
	}

	public int getSectionSizeMargin() {
		return sectionSizeMargin;
	}

	public void setSectionSizeMargin(int sectionSizeMargin) {
		this.sectionSizeMargin = sectionSizeMargin;
	}

	public int getMaxRoomCount() {
		return maxRoomCount;
	}

	public void setMaxRoomCount(int maxRoomCount) {
		this.maxRoomCount = maxRoomCount;
	}

	public List<List<String>> getGroup3AList() {
		return group3AList;
	}

	public void setGroup3AList(List<List<String>> group3aList) {
		group3AList = group3aList;
	}

	public List<List<String>> getMaxTeacherCountList() {
		return maxTeacherCountList;
	}

	public void setMaxTeacherCountList(List<List<String>> maxTeacherCountList) {
		this.maxTeacherCountList = maxTeacherCountList;
	}

	public List<List<String>> getExcludedGroup2AList() {
		return excludedGroup2AList;
	}

	public void setExcludedGroup2AList(List<List<String>> excludedGroup2AList) {
		this.excludedGroup2AList = excludedGroup2AList;
	}

	public List<List<String>> getPre1XList() {
		return pre1XList;
	}

	public void setPre1XList(List<List<String>> pre1xList) {
		pre1XList = pre1xList;
	}

	public int getPure3ASectionCount() {
		return pure3ASectionCount;
	}

	public void setPure3ASectionCount(int pure3aSectionCount) {
		pure3ASectionCount = pure3aSectionCount;
	}
}
