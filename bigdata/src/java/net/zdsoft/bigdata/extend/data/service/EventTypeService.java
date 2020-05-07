package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.extend.data.entity.EventType;

import java.util.List;
import java.util.Map;

/**
 * Created by wangdongdong on 2018/9/25 13:41.
 */
public interface EventTypeService extends BaseService<EventType, String> {

    List<EventType> findAllByUnitId(String unitId);
    
    Map<String,EventType> findMapByUnitId(String unitId);

    void deleteEventType(String id) throws BigDataBusinessException;

    void saveEventType(EventType eventType) throws BigDataBusinessException;
}
