package net.zdsoft.bigdata.frame.data.druid;

import java.util.List;

import net.zdsoft.framework.entity.Json;

public interface DruidClientService {

	/**
	 * druid查询
	 * 
	 * @param druidParam
	 * @param resultFieldList
	 * @return
	 */
	public List<Json> getDruidQueries(DruidParam druidParam,
			List<Json> resultFieldList) ;
	
	/**
	 * 提交任务
	 * @param json
	 * @return
	 */
	public boolean submitDruidJob(String json);
	
	/**
	 * 获取任务状态
	 * @param taskId
	 * @return
	 */
	public String getDruidTaskStatus(String taskId);
}
