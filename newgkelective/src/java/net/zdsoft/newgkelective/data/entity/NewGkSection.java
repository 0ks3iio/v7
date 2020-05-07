package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name = "newgkelective_section")
public class NewGkSection extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String divideId ;
	private String sectionName;
	//开设班级数
	private Integer openClassNum;
	//平均人数
	private Integer meanSize;
	//浮动人数
	private Integer marginSize;
	
	//subjectId_subjectId,subjectId_subjectId
//	private String excludedSubjectIds;
	//如果排不出来的原因展现
	private String remark;
	
	private Date creationTime;
	private Date modifyTime;
	
	private String stat;//状态 0 未开始  1:开始中 2: 完成 3:失败   5:使用

	@Override
	public String fetchCacheEntitName() {
		return "newGkSection";
	}

	public String getDivideId() {
		return divideId;
	}

	public void setDivideId(String divideId) {
		this.divideId = divideId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public Integer getOpenClassNum() {
		return openClassNum;
	}

	public void setOpenClassNum(Integer openClassNum) {
		this.openClassNum = openClassNum;
	}

	public Integer getMeanSize() {
		return meanSize;
	}

	public void setMeanSize(Integer meanSize) {
		this.meanSize = meanSize;
	}

	public Integer getMarginSize() {
		return marginSize;
	}

	public void setMarginSize(Integer marginSize) {
		this.marginSize = marginSize;
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

//	public String getExcludedSubjectIds() {
//		return excludedSubjectIds;
//	}
//
//	public void setExcludedSubjectIds(String excludedSubjectIds) {
//		this.excludedSubjectIds = excludedSubjectIds;
//	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}
	
	
}
