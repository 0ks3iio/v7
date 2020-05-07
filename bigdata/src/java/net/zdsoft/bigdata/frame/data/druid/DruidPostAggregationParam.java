package net.zdsoft.bigdata.frame.data.druid;

import java.io.Serializable;
import java.util.List;

public class DruidPostAggregationParam implements Serializable{

	private static final long serialVersionUID = -7304506124013216510L;
	
	private String type;
	private String name;
	private String fn;
	private List<DruidAggregationParam> fields;
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
	public String getFn() {
		return fn;
	}
	public void setFn(String fn) {
		this.fn = fn;
	}
	public List<DruidAggregationParam> getFields() {
		return fields;
	}
	public void setFields(List<DruidAggregationParam> fields) {
		this.fields = fields;
	}

}
