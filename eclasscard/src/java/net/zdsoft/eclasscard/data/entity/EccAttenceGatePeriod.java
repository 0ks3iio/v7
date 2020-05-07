package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="eclasscard_gate_period")
public class EccAttenceGatePeriod extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	private String unitId;
	@Temporal(TemporalType.DATE)
	private Date tempDate;
	private String beginTime;
	private String endTime;
	private Integer type;//'1:进校(上学) ２:出校 '(放学);
	private Integer classify ;//1进出校  2上下学
	
	@Transient
	private String gradeNames;

	@Override
	public String fetchCacheEntitName() {
		return "eclasscardGatePeriod";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
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

	public String getGradeNames() {
		return gradeNames;
	}

	public void setGradeNames(String gradeNames) {
		this.gradeNames = gradeNames;
	}

	public Date getTempDate() {
		return tempDate;
	}

	public void setTempDate(Date tempDate) {
		this.tempDate = tempDate;
	}

	public Integer getClassify() {
		return classify;
	}

	public void setClassify(Integer classify) {
		this.classify = classify;
	}


}
