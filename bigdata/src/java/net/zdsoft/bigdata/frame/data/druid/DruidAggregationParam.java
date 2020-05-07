package net.zdsoft.bigdata.frame.data.druid;

import java.io.Serializable;

public class DruidAggregationParam implements Serializable {

	private static final long serialVersionUID = 361122537871939231L;

	private String type;
	private String name;
	private String fieldName;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
}
