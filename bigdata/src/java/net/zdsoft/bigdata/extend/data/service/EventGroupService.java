package net.zdsoft.bigdata.extend.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.EventGroup;

/**
 * 事件预览组接口
 * @author jiangf
 *
 */
public interface EventGroupService extends BaseService<EventGroup, String> {

	/**
	 * 根据用户获取组list
	 * 
	 * @param userId
	 * @return
	 */
	public List<EventGroup> findGroupListByUserId(String userId);

	/**
	 * 保存或者更新组
	 * 
	 * @param group
	 */
	public void saveOrUpdateGroup(EventGroup group) throws Exception;
	
	/**
	 * 获取用户最大排序号
	 * @param userId
	 * @return
	 */
	public Integer getMaxOrderIdByUserId(String userId);

}
