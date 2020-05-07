package net.zdsoft.basedata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.TaskParameterDao;
import net.zdsoft.basedata.entity.TaskParameter;
import net.zdsoft.basedata.service.TaskParameterService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("taskParameterService")
public class TaskParameterServiceImpl extends BaseServiceImpl<TaskParameter, String> implements TaskParameterService {

	@Autowired
	private TaskParameterDao taskParameterDao;
	
	@Override
	protected BaseJpaRepositoryDao<TaskParameter, String> getJpaDao() {
		return taskParameterDao;
	}

	@Override
	protected Class<TaskParameter> getEntityClass() {
		return TaskParameter.class;
	}

	@Override
	public Map<String, Map<String, String>> findMap(String... jobIds) {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
		if(jobIds.length>0){
			List<TaskParameter> findList = taskParameterDao.findList(jobIds);
			for (TaskParameter item : findList) {
				Map<String, String> map2 = map.get(item.getJobId());
				if(map2 == null){
					map2 = new HashMap<String, String>();
					map.put(item.getJobId(), map2);
				}
				map2.put(item.getName(), item.getValue());
			}
		}
		return map;
	}

	@Override
	public List<TaskParameter> findList(String... jobIds) {
		return taskParameterDao.findList(jobIds);
	}

	@Override
	public List<TaskParameter> saveAllEntitys(TaskParameter... array) {
		return taskParameterDao.saveAll(checkSave(array));
	}

	@Override
	public void deleteAllByIds(String... ids) {
		if(ids!=null && ids.length>0)
			taskParameterDao.deleteAllByIds(ids);
	}

}
