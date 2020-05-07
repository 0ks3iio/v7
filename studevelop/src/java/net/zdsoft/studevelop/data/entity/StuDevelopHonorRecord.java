package net.zdsoft.studevelop.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
/**
 * 荣誉评选
 * @author gzjsd
 *
 */
@Entity
@Table(name = "studoc_honor_record")
public class StuDevelopHonorRecord extends BaseEntity<String>{
	
	private static final long serialVersionUID = 1L;

	private String unitId; 
	private String acadyear;
	private String semester;
	private String studentId;
	private String honorType; //荣誉类型
	private String honorLevel; //荣誉名称
	private Date giveDate; //给定日期
	private String remark;
	
	//辅助字段
	@Transient
	private String studentName;
	@Transient
	private String className;
	@Transient
	private String honorTypeStr;//荣誉类型名称
	@Transient
	private String[] honorLevelArray;
	
	
	
	public String getClassName() {
		return className;
	}



	public void setClassName(String className) {
		this.className = className;
	}



	public String[] getHonorLevelArray() {
		return honorLevelArray;
	}



	public void setHonorLevelArray(String[] honorLevelArray) {
		this.honorLevelArray = honorLevelArray;
	}



	public String getHonorTypeStr() {
		return honorTypeStr;
	}



	public void setHonorTypeStr(String honorTypeStr) {
		this.honorTypeStr = honorTypeStr;
	}


	public String getUnitId() {
		return unitId;
	}



	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}



	public String getAcadyear() {
		return acadyear;
	}



	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}



	public String getSemester() {
		return semester;
	}



	public void setSemester(String semester) {
		this.semester = semester;
	}



	public String getStudentId() {
		return studentId;
	}



	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}



	public String getHonorType() {
		return honorType;
	}



	public void setHonorType(String honorType) {
		this.honorType = honorType;
	}



	public String getHonorLevel() {
		return honorLevel;
	}



	public void setHonorLevel(String honorLevel) {
		this.honorLevel = honorLevel;
	}



	public Date getGiveDate() {
		return giveDate;
	}



	public void setGiveDate(Date giveDate) {
		this.giveDate = giveDate;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getStudentName() {
		return studentName;
	}



	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}



	@Override
	public String fetchCacheEntitName() {
		return "StuDevelopHonorRecord";
	}
}
