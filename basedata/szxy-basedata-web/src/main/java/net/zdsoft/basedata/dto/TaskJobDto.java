package net.zdsoft.basedata.dto;

import java.util.HashMap;
import java.util.Map;

import net.zdsoft.framework.entity.LoginInfo;

public class TaskJobDto {
	private boolean isHasTask = false;//是否走任务模式
	private String name;//任务名称	可为空，建议填
	private String serviceName;//执行的service名称	必须
	private String businessType;//业务类型	走任务时必须
	private LoginInfo loginInfo;//登录用户	走任务时必须
	//要传的自定义参数	业务逻辑用到的参数
	private Map<String, String> customParamMap = new HashMap<String, String>();
	
	public String getName() {
		return name;
	}
	/**
	 * 任务名称	可为空，建议填
	 */
	public void setName(String name) {
		this.name = name;
	}
	public String getBusinessType() {
		return businessType;
	}
	/**
	 * 业务类型	走任务时必须
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public LoginInfo getLoginInfo() {
		return loginInfo;
	}
	/**
	 * 登录用户	走任务时必须
	 */
	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	public boolean isHasTask() {
		return isHasTask;
	}
	/**
	 * 是否走任务模式	默认true
	 */
	public void setHasTask(boolean isHasTask) {
		this.isHasTask = isHasTask;
	}
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * 执行的service名称	必须
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Map<String, String> getCustomParamMap() {
		return customParamMap;
	}
	/**
	 * 要传的自定义参数	业务逻辑用到的参数
	 */
	public void setCustomParamMap(Map<String, String> customParamMap) {
		this.customParamMap = customParamMap;
	}
	
}
