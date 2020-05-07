
package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.Api;

import java.util.Date;
import java.util.List;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 下午6:30
 */
public interface ApiService extends BaseService<Api, String> {
	/**
	 * 根据单位id获取api的list
	 * @param unitId
	 * @return
	 */
	public List<Api> findApisByUnitId(String unitId);
	
	/**
	 * 保存api数据源
	 * @return
	 */
	public void saveApi(Api api);
	
	/**
	 * 删除api数据源
	 * @param id 
	 * @return
	 */
	public void deleteApi(String id);

	/**
	 * 根据名称、描述模糊查询api
	 * @param unitId
	 * @param name
	 * @param description
	 * @return
	 */
	List<Api> findApis(String unitId, String name, String description);

	long count(Date start, Date end);
}
