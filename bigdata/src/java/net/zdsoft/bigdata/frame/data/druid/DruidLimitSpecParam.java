package net.zdsoft.bigdata.frame.data.druid;

import java.io.Serializable;
import java.util.List;

public class DruidLimitSpecParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4599150255072264954L;
	
	private String type;
	
	private Integer limit;
	
	private List<String> columns;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

}
