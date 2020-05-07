package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.shuffling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShufflingAppDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//算法参数1：给定几个时间点
	private int timeSlotCount;
	
	//算法参数2，算法需要的是每门课的平均大小和误差范围
	//sectionSizeList: {<选课><每个教学班平均人数><误差大小>}
	private List<List<String>> sectionSizeList=new ArrayList<List<String>>();
	
	//算法参数3：教室数量
	//学考B的排法  教室数量 暂时用组合班数量-三科组合
	//选考A 教室数量暂时不考虑 因为不同批次对应的教室数量不一致 暂时也取用组合班数量-三科组合
	private int maxRoomCount = 20; //【注意】当 maxRoomCount == 0 时，忽略本参数
	//studentCourseSelectionList: {<studentID><选课1><选课2><选课3>...}
	private List<List<String>> studentCourseSelectionList=new ArrayList<List<String>>();
	
	//pre1XList: {<studentID><选课><时间点({"T1", "T2", "T3"}，三者选一)>} 2+x中x所在时间点
	private List<List<String>> pre1XList=new ArrayList<List<String>>();

	public int getTimeSlotCount() {
		return timeSlotCount;
	}

	public void setTimeSlotCount(int timeSlotCount) {
		this.timeSlotCount = timeSlotCount;
	}

	public List<List<String>> getSectionSizeList() {
		return sectionSizeList;
	}

	public void setSectionSizeList(List<List<String>> sectionSizeList) {
		this.sectionSizeList = sectionSizeList;
	}

	public int getMaxRoomCount() {
		return maxRoomCount;
	}

	public void setMaxRoomCount(int maxRoomCount) {
		this.maxRoomCount = maxRoomCount;
	}

	public List<List<String>> getStudentCourseSelectionList() {
		return studentCourseSelectionList;
	}

	public void setStudentCourseSelectionList(
			List<List<String>> studentCourseSelectionList) {
		this.studentCourseSelectionList = studentCourseSelectionList;
	}

	public List<List<String>> getPre1XList() {
		return pre1XList;
	}

	public void setPre1XList(List<List<String>> pre1xList) {
		pre1XList = pre1xList;
	}
	
	
	
	
}
