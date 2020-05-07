package net.zdsoft.stuwork.data.entity;

import java.util.List;

public class DyDormForm {
	private String classId;//用去各个统计的
	private String roomId;
	private String checkResult;//考核情况
	private String checkRemind;//提醒事项
	private String otherInfo;//其他情况
	private String roomName;
	private String className;
	private boolean isExcellent;//是否优秀
	
	private int day;
	
	private List<DyDormForm> formList;
	private List<DyDormStatResult> statList;
	
	private String classCode;
	private String classAcadyear;
	
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getOtherInfo() {
		return otherInfo;
	}
	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}
	public List<DyDormStatResult> getStatList() {
		return statList;
	}
	public void setStatList(List<DyDormStatResult> statList) {
		this.statList = statList;
	}
	public boolean isExcellent() {
		return isExcellent;
	}
	public void setExcellent(boolean isExcellent) {
		this.isExcellent = isExcellent;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<DyDormForm> getFormList() {
		return formList;
	}
	public void setFormList(List<DyDormForm> formList) {
		this.formList = formList;
	}
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public String getCheckRemind() {
		return checkRemind;
	}
	public void setCheckRemind(String checkRemind) {
		this.checkRemind = checkRemind;
	}
	public String getClassAcadyear() {
		return classAcadyear;
	}
	public void setClassAcadyear(String classAcadyear) {
		this.classAcadyear = classAcadyear;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
	
	
}
