package net.zdsoft.gkelective.data.dto;

import java.util.List;

public class GkArrangeGroupResultDto {
	//组合班字段
	private String groupName;//组合班名称
	private int groupNum1;//组合班人数
	private String classId1;
	private int groupNum2;//组合班人数
	private String classId2;
	private int groupNum3;//组合班人数
	private String classId3;
	private int groupNum;
	private String batchOne;//批次一（学科名称，老师）
	private String batchTwo;
	private String batchThree;
	private String place;//开班场地
	private String placeId;
	private String classId;
	private String conditionId;
	private String groupStr;
	private String batchIds;//id-batch
	private String gkType;
	
	//单科班字段
	private String batch;
	private String className;
	private String teacherName;
	private String teacherId;
	private String courseName;
	//行政班
	
	private List<GkClassResultDto> dtolist; 
	
	private String place1;
	private String place2;
	private String place3;
	private String stuName;
	private String stuCode;
	private String stuSex;
	private String stuChosenSubNames;
	
	public String getStuChosenSubNames() {
		return stuChosenSubNames;
	}
	public void setStuChosenSubNames(String stuChosenSubNames) {
		this.stuChosenSubNames = stuChosenSubNames;
	}
	public String getStuSex() {
		return stuSex;
	}
	public void setStuSex(String stuSex) {
		this.stuSex = stuSex;
	}
	public String getConditionId() {
		return conditionId;
	}
	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}
	
	public String getClassId1() {
		return classId1;
	}
	public void setClassId1(String classId1) {
		this.classId1 = classId1;
	}
	public String getClassId2() {
		return classId2;
	}
	public void setClassId2(String classId2) {
		this.classId2 = classId2;
	}
	public String getClassId3() {
		return classId3;
	}
	public void setClassId3(String classId3) {
		this.classId3 = classId3;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	public String getGroupStr() {
		return groupStr;
	}
	public void setGroupStr(String groupStr) {
		this.groupStr = groupStr;
	}
	public List<GkClassResultDto> getDtolist() {
		return dtolist;
	}
	public void setDtolist(List<GkClassResultDto> dtolist) {
		this.dtolist = dtolist;
	}
	public String getPlace1() {
		return place1;
	}
	public void setPlace1(String place1) {
		this.place1 = place1;
	}
	public String getPlace2() {
		return place2;
	}
	public void setPlace2(String place2) {
		this.place2 = place2;
	}
	public String getPlace3() {
		return place3;
	}
	public void setPlace3(String place3) {
		this.place3 = place3;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getStuCode() {
		return stuCode;
	}
	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	
	public int getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}
	public int getGroupNum1() {
		return groupNum1;
	}
	public void setGroupNum1(int groupNum1) {
		this.groupNum1 = groupNum1;
	}
	public int getGroupNum2() {
		return groupNum2;
	}
	public void setGroupNum2(int groupNum2) {
		this.groupNum2 = groupNum2;
	}
	public int getGroupNum3() {
		return groupNum3;
	}
	public void setGroupNum3(int groupNum3) {
		this.groupNum3 = groupNum3;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getBatchOne() {
		return batchOne;
	}
	public void setBatchOne(String batchOne) {
		this.batchOne = batchOne;
	}
	public String getBatchTwo() {
		return batchTwo;
	}
	public void setBatchTwo(String batchTwo) {
		this.batchTwo = batchTwo;
	}
	public String getBatchThree() {
		return batchThree;
	}
	public void setBatchThree(String batchThree) {
		this.batchThree = batchThree;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getBatchIds() {
		return batchIds;
	}
	public void setBatchIds(String batchIds) {
		this.batchIds = batchIds;
	}
	public String getGkType() {
		return gkType;
	}
	public void setGkType(String gkType) {
		this.gkType = gkType;
	}
	
}
