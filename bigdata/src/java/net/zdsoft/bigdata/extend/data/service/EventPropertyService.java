package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.extend.data.entity.EventProperty;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;

/**
 * Created by wangdongdong on 2018/9/25 13:41.
 */
public interface EventPropertyService extends
		BaseService<EventProperty, String> {

	/**
	 * 根据事件查询所有属性
	 * 
	 * @param eventId
	 * @return
	 */
	List<EventProperty> findAllByEventId(String eventId);

	/**
	 * 根据事件查询所有属性(含公共属性)
	 * 
	 * @param eventId
	 * @param user
	 * @param time
	 * @param env
	 * @return
	 */
	List<EventProperty> findPropertiesByEventId(String eventId, short user,
			short time, short env);

	List<EventProperty> getChartEventProperty(String eventId);

	void deleteByEventId(String eventId);

	void saveEventProperty(EventProperty eventProperty)
			throws BigDataBusinessException;

	List<EventProperty> findByPage(Pagination page, String eventId,
			String unitId);
}
