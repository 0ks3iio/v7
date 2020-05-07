package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.sectioning2a1x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SectioningAppDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * group3AList { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	 * maxGroup3ACountPerGroup2A  //每个2A组合最多由几个3A组合构成，在6选3的情况下，此值如果是3，说明还有一门B课可以作为行政班
	 * sectionSizeMean 平均班级人数
	 * sectionSizeMargin 平均班级人数的误差值，也就是说，可以额外多塞几个人
	 * maxTeacherCountList  {{<课名><老师数量供1X部分使用>}}
	 * maxRoomCount 总的教室数量
	 */
	private List<List<String>> group3AList=new ArrayList<List<String>>();
	private int maxGroup3ACountPerGroup2A;
	private int sectionSizeMean; 
	private int sectionSizeMargin;
	private List<List<String>> maxTeacherCountList=new ArrayList<List<String>>();
	private int maxRoomCount;
	
	// 输入3：excludedGroup2AList，不允许出现的2A组合: {<选课1-2A><选课2-2A>}
	private List<List<String>> excludedGroup2AList =new ArrayList<List<String>>();;

	// 输入4：pre1XList，手动2+X遗留下来的1X部分: {<选课1-3A><选课2-3A><选课3-3A><人数><选课1X>}
	private List<List<String>> pre1XList =new ArrayList<List<String>>();
	
	public SectioningAppDto(){
		
	}
	public List<List<String>> getGroup3AList() {
		return group3AList;
	}
	public void setGroup3AList(List<List<String>> group3aList) {
		group3AList = group3aList;
	}
	public int getMaxGroup3ACountPerGroup2A() {
		return maxGroup3ACountPerGroup2A;
	}
	public void setMaxGroup3ACountPerGroup2A(int maxGroup3ACountPerGroup2A) {
		this.maxGroup3ACountPerGroup2A = maxGroup3ACountPerGroup2A;
	}
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
	public List<List<String>> getMaxTeacherCountList() {
		return maxTeacherCountList;
	}
	public void setMaxTeacherCountList(List<List<String>> maxTeacherCountList) {
		this.maxTeacherCountList = maxTeacherCountList;
	}
	public int getMaxRoomCount() {
		return maxRoomCount;
	}
	public void setMaxRoomCount(int maxRoomCount) {
		this.maxRoomCount = maxRoomCount;
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
	
	

}
