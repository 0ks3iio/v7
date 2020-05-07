package net.zdsoft.bigdata.metadata.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "bg_md_quality_dim_result")
public class QualityDimResult extends BaseEntity<String> {

	private static final long serialVersionUID = 8273165977963484226L;

	private BigDecimal result;

	private BigDecimal threshold;

	private Integer type;

	private String dimCode;

	private String dimName;

	private String grade;
	
	private BigDecimal trend;

	private Integer isAlarm;
	
	private Integer isSaveDetail;
	
	private Integer status;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date statTime;

	public BigDecimal getResult() {
		return result;
	}

	public void setResult(BigDecimal result) {
		this.result = result;
	}

	public BigDecimal getThreshold() {
		return threshold;
	}

	public void setThreshold(BigDecimal threshold) {
		this.threshold = threshold;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDimCode() {
		return dimCode;
	}

	public void setDimCode(String dimCode) {
		this.dimCode = dimCode;
	}

	public String getDimName() {
		return dimName;
	}

	public void setDimName(String dimName) {
		this.dimName = dimName;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public BigDecimal getTrend() {
		return trend;
	}

	public void setTrend(BigDecimal trend) {
		this.trend = trend;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStatTime() {
		return statTime;
	}

	public void setStatTime(Date statTime) {
		this.statTime = statTime;
	}

	public Integer getIsAlarm() {
		return isAlarm;
	}

	public void setIsAlarm(Integer isAlarm) {
		this.isAlarm = isAlarm;
	}

	public Integer getIsSaveDetail() {
		return isSaveDetail;
	}

	public void setIsSaveDetail(Integer isSaveDetail) {
		this.isSaveDetail = isSaveDetail;
	}

	@Override
	public String fetchCacheEntitName() {
		return "qualityResult";
	}
}
