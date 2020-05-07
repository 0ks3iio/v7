package net.zdsoft.comprehensive.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 总评表
 *@author niuchao
 *@date 2018-12-10
 *
 */
@Entity
@Table(name="scoremanage_compre_info")
public class CompreInfo extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String gradeId;
	private String gradeCode;
	private String acadyear;
	private String semester;
	private String stateArea;
	private Date creationTime;
	private Date modifyTime;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
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

	public String getStateArea() {
		return stateArea;
	}

	public void setStateArea(String stateArea) {
		this.stateArea = stateArea;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Override
	public String fetchCacheEntitName() {
		return "compreInfo";
	}
	
}
