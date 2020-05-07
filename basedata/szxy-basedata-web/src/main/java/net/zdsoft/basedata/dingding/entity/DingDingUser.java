package net.zdsoft.basedata.dingding.entity;

import java.io.Serializable;
import java.util.List;

public class DingDingUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4545974616186634727L;

	private String userid;
	
	private String name;
	
	private String mobile;
	
	List<String> department;
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<String> getDepartment() {
		return department;
	}

	public void setDepartment(List<String> department) {
		this.department = department;
	}
}
