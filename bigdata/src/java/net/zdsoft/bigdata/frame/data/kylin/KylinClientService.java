package net.zdsoft.bigdata.frame.data.kylin;

import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.entity.KylinCube;
import net.zdsoft.framework.entity.Json;

import java.util.List;

public interface KylinClientService {
	
	/**
	 * 重新初始化
	 */
	public void reInit();
	
	/**
	 * 获取kylin的job
	 * @param unitId
	 * @param cubeName
	 * @return
	 */
	public EtlJob getKylinJob(String unitId,String cubeName);

	/**
	 * 获取kylin的cubes
	 * @param unitId
	 * @return
	 */
	public List<KylinCube> getKylinCubesList(String unitId) throws Exception;
	
	/**
	 * 获取kylin的cube
	 * @return
	 */
	public KylinCube getKylinCube(String cubeName) throws Exception;
	
	/**
	 * 获取kylin的job
	 * @param kylinJob
	 * @return
	 */
	public void saveKylinJob(EtlJob kylinJob);
	
	/**
	 * 执行kylin的job
	 * @param kylinJob
	 */
	public boolean dealJob(EtlJob kylinJob);

	/**
	 * kylin jdbc查询
	 * 
	 * @param projectName
	 * @param sql
	 * @param paramList
	 * @param resultFieldList
	 * @return
	 */
	public List<Json> getDataListFromKylin(String projectName, String sql,
			List<Json> paramList, List<Json> resultFieldList);

	/**
	 * kylin jdbc查询
	 *
	 * @param dataSourceId 数据源id
	 * @param projectName
	 * @param sql
	 * @param paramList
	 * @param resultFieldList
	 * @return
	 */
	public List<Json> getDataListFromKylin(String dataSourceId, String projectName, String sql,
										   List<Json> paramList, List<Json> resultFieldList);


	/**
	 * 多维模型查询
	 * @param projectName 项目名称
	 * @param sql 语句
	 * @param paramList 参数
	 * @param resultFieldList 结果
	 * @param rowDimensionList 行维度
	 * @param columnDimensionList 列维度
	 * @param indexList 指标
	 * @return json 含列头 数据 和列数
	 */
	public String getDataListFromKylin(String projectName, String sql,
			List<Json> paramList, List<Json> resultFieldList,List<Json> rowDimensionList,List<Json> columnDimensionList,List<Json> indexList);

	/**
	 * 多维模型查询
	 * @param projectName 项目名称
	 * @param sql 语句
	 * @param paramList 参数
	 * @param resultFieldList 结果
	 * @param rowDimensionList 行维度
	 * @param columnDimensionList 列维度
	 * @param indexList 指标
	 * @return json 含列头 数据 和列数
	 */
	public String getDataListFromKylin(String dataSourceId, String projectName, String sql,
									   List<Json> paramList, List<Json> resultFieldList,List<Json> rowDimensionList,List<Json> columnDimensionList,List<Json> indexList);
}
