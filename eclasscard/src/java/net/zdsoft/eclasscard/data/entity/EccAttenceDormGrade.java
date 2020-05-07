package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="eclasscard_dorm_grade")
public class EccAttenceDormGrade extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	private String unitId;
	private String periodId;
	private String grade;
	private String beginTime;
	private String endTime;
	private Integer type;

	@Override
	public String fetchCacheEntitName() {
		return "eclasscardDormGrade";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}


}
