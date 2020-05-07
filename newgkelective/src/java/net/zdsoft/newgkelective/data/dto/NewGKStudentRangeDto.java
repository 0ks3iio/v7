package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGKStudentRangeEx;

public class NewGKStudentRangeDto {
	
	private String subjectId;
	private String subjectType;
	private String subjectName;
	private int stuNum;
	private List<NewGKStudentRangeEx> exList = new ArrayList<NewGKStudentRangeEx>();
	private String pngName;
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public int getStuNum() {
		return stuNum;
	}
	public void setStuNum(int stuNum) {
		this.stuNum = stuNum;
	}
	public List<NewGKStudentRangeEx> getExList() {
		return exList;
	}
	public void setExList(List<NewGKStudentRangeEx> exList) {
		this.exList = exList;
	}
	public String getPngName() {
		return pngName;
	}
	public void setPngName(String pngName) {
		this.pngName = pngName;
	}
	
}
