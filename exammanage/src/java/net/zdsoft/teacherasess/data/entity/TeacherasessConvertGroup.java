package net.zdsoft.teacherasess.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="teacherasess_convert_group")
public class TeacherasessConvertGroup extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	private String unitId;
	private String examIds;
	private String scales;
	private String acadyear;
	private String convertId;//折算分方案id
	private Date creationTime;
	
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getExamIds() {
		return examIds;
	}

	public void setExamIds(String examIds) {
		this.examIds = examIds;
	}

	public String getScales() {
		return scales;
	}

	public void setScales(String scales) {
		this.scales = scales;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getConvertId() {
		return convertId;
	}

	public void setConvertId(String convertId) {
		this.convertId = convertId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}



	@Override
	public String fetchCacheEntitName() {
		return "teacherasessConvertGroup";
	}

}
