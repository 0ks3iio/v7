package net.zdsoft.newgkelective.data.dto;

import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkReportBase;

public class NewGkReportBaseDto {
	private String unitName;
	private String parentName;
	private String unitHeader;
	private String tel;
	private Integer xzbNum;
	private Integer jxbNum;
	private Integer placeNum;
	private Integer teacherNum;
	private Integer stuNum;
	private String subjectName;
	private String subjectId;
	
	private List<NewGkReportBase> baseList;
	
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getUnitHeader() {
		return unitHeader;
	}
	public void setUnitHeader(String unitHeader) {
		this.unitHeader = unitHeader;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Integer getXzbNum() {
		return xzbNum;
	}
	public void setXzbNum(Integer xzbNum) {
		this.xzbNum = xzbNum;
	}
	public Integer getPlaceNum() {
		return placeNum;
	}
	public void setPlaceNum(Integer placeNum) {
		this.placeNum = placeNum;
	}
	public Integer getJxbNum() {
		return jxbNum;
	}
	public void setJxbNum(Integer jxbNum) {
		this.jxbNum = jxbNum;
	}
	public Integer getTeacherNum() {
		return teacherNum;
	}
	public void setTeacherNum(Integer teacherNum) {
		this.teacherNum = teacherNum;
	}
	public Integer getStuNum() {
		return stuNum;
	}
	public void setStuNum(Integer stuNum) {
		this.stuNum = stuNum;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public List<NewGkReportBase> getBaseList() {
		return baseList;
	}
	public void setBaseList(List<NewGkReportBase> baseList) {
		this.baseList = baseList;
	}
}
