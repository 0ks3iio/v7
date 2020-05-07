package net.zdsoft.basedata.dingding.entity;

import java.io.Serializable;

public class DingDingDept implements Serializable {

	private static final long serialVersionUID = -3297680205613886280L;
	
	private String id;
	
	private String name;
	
	private String parentid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	
	
}
