package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.extend.data.entity.Event;

import java.util.Date;
import java.util.List;

/**
 * Created by wangdongdong on 2018/9/25 13:41.
 */
public interface EventService extends BaseService<Event, String> {

    List<Event> findAllByTypeId(String typeId);

    /**
     * 根据事件id更新上次执行时间
     * @param eventId
     * @param lastCommitDate
     */
    public void updateLastCommitDateByEventId(String eventId,Date lastCommitDate);

    List<Event> findAll(String unitId);

    long count(Date start, Date end);
    
    /**
     * 更新metadata
     * @param id
     * @param metadata
     */
	public void updateMetadata(String id, String metadata) ;
	
	/**
	 * 更新kafka信息
	 * @param id
	 * @param kafkaInfo
	 */
	public void updateKafkaInfo(String id, String kafkaInfo);

    void deleteByEventType(String typeId);

    void saveEvent(Event event) throws BigDataBusinessException;

    void deleteEvent(String id) throws BigDataBusinessException;
}
