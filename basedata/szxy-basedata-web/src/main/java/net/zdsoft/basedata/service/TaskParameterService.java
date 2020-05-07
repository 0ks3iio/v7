package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.TaskParameter;

public interface TaskParameterService extends BaseService<TaskParameter, String>{

	/**
	 * 
	 * @param jobIds
	 * @return key jobId name
	 */
	Map<String,Map<String,String>> findMap(String... jobIds);

	List<TaskParameter> findList(String... jobIds);

	List<TaskParameter> saveAllEntitys(TaskParameter... taskParameter);

	void deleteAllByIds(String... ids);
	
}
