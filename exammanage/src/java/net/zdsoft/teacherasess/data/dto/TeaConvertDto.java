package net.zdsoft.teacherasess.data.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeaConvertDto {
	
	private String convertId;
	private String convertName;
	private String acadyear;
	private String gradeId;
	private String gradeName;
	private String status;
	private int examNum;
	private String unitId;
	private Date creationTime;
	private List<TeaConvertExamDto> examDtos = new ArrayList<>();
	private List<GroupDto> groupList=new ArrayList<>();
	
	private String xuankaoType;
	
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public int getExamNum() {
		return examNum;
	}
	public void setExamNum(int examNum) {
		this.examNum = examNum;
	}
	public String getConvertId() {
		return convertId;
	}
	public void setConvertId(String convertId) {
		this.convertId = convertId;
	}
	public String getConvertName() {
		return convertName;
	}
	public void setConvertName(String convertName) {
		this.convertName = convertName;
	}
	public String getAcadyear() {
		return acadyear;
	}
	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public List<TeaConvertExamDto> getExamDtos() {
		return examDtos;
	}
	public void setExamDtos(List<TeaConvertExamDto> examDtos) {
		this.examDtos = examDtos;
	}
	public List<GroupDto> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<GroupDto> groupList) {
		this.groupList = groupList;
	}
	public String getXuankaoType() {
		return xuankaoType;
	}
	public void setXuankaoType(String xuankaoType) {
		this.xuankaoType = xuankaoType;
	}
	
}
