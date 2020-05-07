package net.zdsoft.gkelective.data.dto;

public class GkClassResultDto {
	private String place;
	private String subName;
	private int type;
	private String className;
	private String teacherId;
	private String classId;//教学班id
	private String oldId;//原来的教师/场地
	private int batch;
	private String batchIds;
	private int stuNum;
	private String gkType;
	private String teaName;
	
	public String getTeaName() {
		return teaName;
	}
	public void setTeaName(String teaName) {
		this.teaName = teaName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getSubName() {
		return subName;
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getOldId() {
		return oldId;
	}
	public void setOldId(String oldId) {
		this.oldId = oldId;
	}
	public int getBatch() {
		return batch;
	}
	public void setBatch(int batch) {
		this.batch = batch;
	}
	public String getBatchIds() {
		return batchIds;
	}
	public void setBatchIds(String batchIds) {
		this.batchIds = batchIds;
	}
	public int getStuNum() {
		return stuNum;
	}
	public void setStuNum(int stuNum) {
		this.stuNum = stuNum;
	}
	public String getGkType() {
		return gkType;
	}
	public void setGkType(String gkType) {
		this.gkType = gkType;
	}
	
}
