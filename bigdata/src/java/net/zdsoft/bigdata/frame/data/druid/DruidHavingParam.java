package net.zdsoft.bigdata.frame.data.druid;

import java.io.Serializable;

public class DruidHavingParam implements Serializable{

	private static final long serialVersionUID = -4273740105743979832L;
	private String type;
	private String aggregation;
	private Integer value;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAggregation() {
		return aggregation;
	}
	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}

}
