package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.extend.data.entity.EventIndicator;

import java.util.List;

public interface EventIndicatorService extends BaseService<EventIndicator, String>{

	/**
	 * 根据事件id获取事件的指标
	 * @param eventId
	 * @return
	 */
	public List<EventIndicator> findIndicatorListByEventId(String eventId);

    void saveEventIndicator(EventIndicator eventIndicator) throws BigDataBusinessException;

	List<EventIndicator> findByUnitId(String unitId);
}
