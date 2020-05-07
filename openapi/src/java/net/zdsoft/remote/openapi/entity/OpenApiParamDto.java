package net.zdsoft.remote.openapi.entity;

import java.io.Serializable;

public class OpenApiParamDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String paramName;
	
	private String value;
	
	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
