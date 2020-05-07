package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.extend.data.dao.EventTypeDao;
import net.zdsoft.bigdata.extend.data.entity.Event;
import net.zdsoft.bigdata.extend.data.entity.EventType;
import net.zdsoft.bigdata.extend.data.service.EventPropertyService;
import net.zdsoft.bigdata.extend.data.service.EventService;
import net.zdsoft.bigdata.extend.data.service.EventTypeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class EventTypeServiceImpl extends BaseServiceImpl<EventType, String>
		implements EventTypeService {

	@Resource
	private EventTypeDao eventTypeDao;
	@Resource
	private EventService eventService;
	@Resource
	private EventPropertyService eventPropertyService;

	@Override
	protected BaseJpaRepositoryDao<EventType, String> getJpaDao() {
		return eventTypeDao;
	}

	@Override
	protected Class<EventType> getEntityClass() {
		return EventType.class;
	}

	@Override
	public List<EventType> findAllByUnitId(String unitId) {
		return eventTypeDao.findAllByUnitIdOrderByOrderId(unitId);
	}

	@Override
	public Map<String, EventType> findMapByUnitId(String unitId) {
		List<EventType> eventTypeList = findAllByUnitId(unitId);
		Map<String, EventType> resultMap = new LinkedHashMap<>();
		eventTypeList.forEach(e -> resultMap.put(e.getId(), e));
		return resultMap;
	}

	@Override
	public void deleteEventType(String id) throws BigDataBusinessException {
		List<Event> events = eventService.findAllByTypeId(id);
		if (events.size() > 0) {
			throw new BigDataBusinessException("该分组下有事件，不能删除!");
		}
		eventTypeDao.deleteById(id);
	}

	@Override
	public void saveEventType(EventType eventType) throws BigDataBusinessException {
		List<EventType> listForName = this.findListBy(new String[]{"typeName", "unitId"}, new String[]{eventType.getTypeName(), eventType.getUnitId()});
		if (StringUtils.isBlank(eventType.getId())) {
			if (listForName.size() > 0) {
				throw new BigDataBusinessException("该名称已存在!");
			}
			eventType.setId(UuidUtils.generateUuid());
			eventTypeDao.save(eventType);
			return;
		}

		// 验证名称和code是否存在
		if (listForName.size() > 0) {
			if (!listForName.get(0).getId().equals(eventType.getId())) {
				throw new BigDataBusinessException("该名称已存在!");
			}
		}
		eventTypeDao.update(eventType, new String[]{"typeName", "orderId", "remark"});
	}

}
