package net.zdsoft.bigdata.metadata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "bg_metadata_quality_dim")
public class QualityDim extends BaseEntity<String> {

	private static final long serialVersionUID = 8273165977963484226L;

	private Integer type;
	
	private String code;
	
	private String name;
	
	private Integer weight;
	
	private Integer orderId;
	
	private Long threshold;
	
	private Integer isAlarm;
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Long getThreshold() {
		return threshold;
	}

	public void setThreshold(Long threshold) {
		this.threshold = threshold;
	}

	public Integer getIsAlarm() {
		return isAlarm;
	}

	public void setIsAlarm(Integer isAlarm) {
		this.isAlarm = isAlarm;
	}

	@Override
	public String fetchCacheEntitName() {
		return "qualityDim";
	}
}
