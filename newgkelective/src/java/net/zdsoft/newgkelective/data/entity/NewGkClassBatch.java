package net.zdsoft.newgkelective.data.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 
 * @author weixh
 * @since 2018年6月12日 上午9:56:36
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "newgkelective_class_batch")
public class NewGkClassBatch extends BaseEntity<String> {
	private String divideId;
	private String unitId;
	private String divideClassId;
	private String subjectIds;
	private String subjectId;
	private String batch;
	// @Transient
	private String subjectType;
	
	@Transient
	private String exSubId;
	@Transient
	private List<String> stuIds;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getDivideClassId() {
		return divideClassId;
	}

	public void setDivideClassId(String divideClassId) {
		this.divideClassId = divideClassId;
	}

	public String getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	@Override
	public String fetchCacheEntitName() {
		return "NewGkClassBatch";
	}

	public String getDivideId() {
		return divideId;
	}

	public void setDivideId(String divideId) {
		this.divideId = divideId;
	}

	public String getExSubId() {
		return exSubId;
	}

	public void setExSubId(String exSubId) {
		this.exSubId = exSubId;
	}

	public List<String> getStuIds() {
		return stuIds;
	}

	public void setStuIds(List<String> stuIds) {
		this.stuIds = stuIds;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

}
