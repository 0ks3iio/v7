package net.zdsoft.bigdata.taskScheduler.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;

import java.util.Date;
import java.util.List;

/**
 * etl任务service
 * @author feekang
 *
 */
public interface EtlJobService extends BaseService<EtlJob, String>{
	/**
	 * 获取所有定时任务
	 * @return
	 */
	public List<EtlJob> findScheduledEtlJobs();

	/**
	 * 根据单位获取Etl任务列表
	 * @return
	 */
	public List<EtlJob> findByUnitId(String unitId);

	/**
	 * 根据类型获取Etl任务列表
	 * @param unitId
	 * @param etlType
	 * @return
	 */
	public List<EtlJob> findEtlJobsByUnitId(String unitId,Integer etlType);
	
	/**
	 * 根据名称获取Etl任务列表
	 * @param unitId
	 * @param name
	 * @return
	 */
	public List<EtlJob> findEtlJobsByName(String unitId,String name);
	
	
	/**
	 * 根据jobCode获取Etl任务列表
	 * @param unitId
	 * @param jobCode
	 * @return
	 */
	public List<EtlJob> findEtlJobsByJobCode(String unitId,String jobCode);
	
	/**
	 * 更新etl的状态
	 * @param id
	 * @param state
	 * @param logId
	 */
	public void updateEtlJobStateById(String id,Integer state,String logId);


	/**
	 * 保存Etl Job的关系
	 * @param job
	 */
	public void saveEtlJobRelations(EtlJob job);


	/**
	 * 删除Job
	 * @param job
	 */
	public void deleteJob(EtlJob job);


	/**
	 * 判断节点是否被引用
	 * @param nodeId
	 * @return
	 */
	public boolean isExistsNode(String nodeId);


    /**
     * 判断节点是否被引用
     * @param nodeId
     * @param type
     * @return
     */
    public boolean isExistsNodeAndType(String nodeId,String type);


	/**
	 * 定时处理etl任务
	 * @param id
	 */
	public boolean dealEtlQuantzJob(String id);

	long count(Date start, Date end);

}
