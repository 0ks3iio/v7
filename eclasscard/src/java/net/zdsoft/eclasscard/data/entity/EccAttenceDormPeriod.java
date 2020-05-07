package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="eclasscard_dorm_period")
public class EccAttenceDormPeriod extends EccTaskEntity{
	private static final long serialVersionUID = 1L;

	private String unitId;
	private Integer type;
	private boolean isNextDayAttence;
	@Transient
	private String gradeNames;

	@Override
	public String fetchCacheEntitName() {
		return "eclasscardDormPeriod";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public boolean isNextDayAttence() {
		return isNextDayAttence;
	}

	public void setNextDayAttence(boolean isNextDayAttence) {
		this.isNextDayAttence = isNextDayAttence;
	}

	public String getGradeNames() {
		return gradeNames;
	}

	public void setGradeNames(String gradeNames) {
		this.gradeNames = gradeNames;
	}


}
