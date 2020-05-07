package net.zdsoft.bigdata.daq.data.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

public class BizOperationLog implements Serializable {

	private static final long serialVersionUID = -7060413386071654598L;

	public static final String LOG_TYPE_STAT = "stat";
	
	public static final String LOG_TYPE_QUERY = "query";

	public static final String LOG_TYPE_INSERT = "insert";

	public static final String LOG_TYPE_UPDATE = "update";

	public static final String LOG_TYPE_DELETE = "delete";
	
	public static final String LOG_TYPE_AUTH = "auth";
	
	public static final String LOG_TYPE_INIT = "init";
	
	public static final String LOG_TYPE_OTHER = "other";

	private String logType;

	private String bizCode;

	private String bizName;

	private String subSystem;

	private String description;
	
	private String oldData;
	
	private String newData;

	private String operator;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date operationTime;

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(String subSystem) {
		this.subSystem = subSystem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOldData() {
		return oldData;
	}

	public void setOldData(String oldData) {
		this.oldData = oldData;
	}

	public String getNewData() {
		return newData;
	}

	public void setNewData(String newData) {
		this.newData = newData;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

}
