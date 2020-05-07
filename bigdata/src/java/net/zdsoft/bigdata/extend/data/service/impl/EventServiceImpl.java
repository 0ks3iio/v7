package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.extend.data.dao.EventDao;
import net.zdsoft.bigdata.extend.data.entity.Event;
import net.zdsoft.bigdata.extend.data.entity.EventFavorite;
import net.zdsoft.bigdata.extend.data.entity.EventIndicator;
import net.zdsoft.bigdata.extend.data.entity.EventProperty;
import net.zdsoft.bigdata.extend.data.service.EventFavoriteService;
import net.zdsoft.bigdata.extend.data.service.EventIndicatorService;
import net.zdsoft.bigdata.extend.data.service.EventPropertyService;
import net.zdsoft.bigdata.extend.data.service.EventService;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class EventServiceImpl extends BaseServiceImpl<Event, String> implements
		EventService {

	@Resource
	private EventDao eventDao;
	@Resource
	private EventPropertyService eventPropertyService;
	@Resource
	private EventIndicatorService eventIndicatorService;
	@Resource
	private EventFavoriteService eventFavoriteService;
	@Resource
    private MetadataRelationService metadataRelationService;

	@Override
	protected BaseJpaRepositoryDao<Event, String> getJpaDao() {
		return eventDao;
	}

	@Override
	protected Class<Event> getEntityClass() {
		return Event.class;
	}

	@Override
	public List<Event> findAllByTypeId(String typeId) {
		return eventDao.findAllByTypeIdOrderByOrderId(typeId);
	}

	@Override
	public void updateLastCommitDateByEventId(String eventId,
			Date lastCommitDate) {
		eventDao.updateLastCommitDateByEventId(eventId, lastCommitDate);
	}

	@Override
	public List<Event> findAll(String unitId) {
		return eventDao.findByUnitIdOrderByOrderId(unitId);
	}

	@Override
	public long count(Date start, Date end) {
		if (start == null && end == null) {
			return eventDao.count((Specification<Event>) (root, criteriaQuery, criteriaBuilder)
					-> criteriaQuery.getRestriction());
		} else {
			return eventDao.count((Specification<Event>) (root, criteriaQuery, criteriaBuilder)
					-> {
				Predicate time = criteriaBuilder.between(root.get("creationTime").as(Timestamp.class), start, end);
				return criteriaQuery.where(time).getRestriction();
			});
		}
	}
	
	@Override
	public void updateMetadata(String id, String metadata) {
		Event event = findOne(id);
		event.setMetadata(metadata);
		update(event, id, new String[] { "metadata" });
	}
	
	@Override
	public void updateKafkaInfo(String id, String kafkaInfo) {
		Event event = findOne(id);
		event.setKafkaInfo(kafkaInfo);
		update(event, id, new String[] { "kafkaInfo" });
	}

	@Override
	public void deleteByEventType(String typeId) {
		eventDao.deleteByTypeId(typeId);
	}

	@Override
	public void saveEvent(Event event) throws BigDataBusinessException {
		List<Event> listForName = this.findListBy(new String[]{"eventName", "unitId"}, new String[]{event.getEventName(), event.getUnitId()});
		List<Event> listForCode = this.findListBy(new String[]{"eventCode", "unitId"}, new String[]{event.getEventCode(), event.getUnitId()});
		if (StringUtils.isBlank(event.getId())) {
			if (listForName.size() > 0) {
				throw new BigDataBusinessException("该名称已存在!");
			}
			if (listForCode.size() > 0) {
				throw new BigDataBusinessException("该Code已存在!");
			}
			event.setCreationTime(new Date());
			event.setId(UuidUtils.generateUuid());
			eventDao.save(event);
			return;
		}

		// 验证名称和code是否存在
		if (listForName.size() > 0) {
			if (!listForName.get(0).getId().equals(event.getId())) {
				throw new BigDataBusinessException("该名称已存在!");
			}
		}

		if (listForCode.size() > 0) {
			if (!listForCode.get(0).getId().equals(event.getId())) {
				throw new BigDataBusinessException("该Code已存在!");
			}
		}
		eventDao.update(event, new String[]{"eventName", "typeId", "orderId", "topicName", "userProperty", "envProperty"
				, "timeProperty", "remark", "urls", "eventCode", "tableName", "intervalTime", "granularity", "isCustom","importSwitch"});
	}

	@Override
	public void deleteEvent(String id) throws BigDataBusinessException {
		List<EventProperty> eventProperties = eventPropertyService.findAllByEventId(id);
		if (eventProperties.size() > 0) {
			throw new BigDataBusinessException("该事件下面有属性，不能删除!");
		}

		List<EventIndicator> indexList = eventIndicatorService.findIndicatorListByEventId(id);
		if (indexList.size() > 0) {
			throw new BigDataBusinessException("该事件下面有指标，不能删除!");
		}

		List<EventFavorite> favoriteList = eventFavoriteService.findListBy("eventId", id);
		if (favoriteList.size() > 0) {
			throw new BigDataBusinessException("该事件被事件库引用，不能删除!");
		}

        metadataRelationService.deleteByTargetId(id);
		eventDao.deleteById(id);
	}
}
