package net.zdsoft.bigdata.daq.data.service;

/**
 * 业务操作日志采集service
 * @author jiangf
 *
 */
public interface BizOperationLogService {
	
	/**
	 * 保存业务操作日志
	 * 
	 */
	public void saveBizOperationLog();
	
	/**
	 * 保存模块操作日志
	 * 
	 */
	public void saveModuleOperationLog();


	/**
	 * 保存sql分析日志
	 */
	public void saveSqlAnalyseLog();
}
