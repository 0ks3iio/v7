package net.zdsoft.basedata.job;

import java.util.Map;

/**
 * 数据参数：与任务相关的
 * 
 */
public class TaskJobDataParam {

	private String jobId;// 消息反馈id
	private Map<String, String> customParamMap;// 自定义参数

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Map<String, String> getCustomParamMap() {
		return customParamMap;
	}

	public void setCustomParamMap(Map<String, String> customParamMap) {
		this.customParamMap = customParamMap;
	}
	
}
