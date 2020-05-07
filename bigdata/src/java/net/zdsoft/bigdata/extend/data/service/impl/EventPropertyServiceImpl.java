package net.zdsoft.bigdata.extend.data.service.impl;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.extend.data.dao.EventPropertyDao;
import net.zdsoft.bigdata.extend.data.entity.EventProperty;
import net.zdsoft.bigdata.extend.data.service.EventPropertyService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class EventPropertyServiceImpl extends
		BaseServiceImpl<EventProperty, String> implements EventPropertyService {

	@Resource
	private EventPropertyDao eventPropertyDao;

	@Override
	protected BaseJpaRepositoryDao<EventProperty, String> getJpaDao() {
		return eventPropertyDao;
	}

	@Override
	protected Class<EventProperty> getEntityClass() {
		return EventProperty.class;
	}

	@Override
	public List<EventProperty> findAllByEventId(String eventId) {
		return eventPropertyDao.findAllByEventId(eventId);
	}

	@Override
	public List<EventProperty> findPropertiesByEventId(String eventId,
			short user, short time, short env) {
		List<EventProperty> propertyList = eventPropertyDao
				.findAllByEventId(eventId);
		if (CollectionUtils.isEmpty(propertyList)) {
			propertyList = new ArrayList<EventProperty>();
		}
		if (user == 1) {
			List<EventProperty> userList = eventPropertyDao
					.findAllByEventId(EventProperty.EVENT_USER);
			if (CollectionUtils.isNotEmpty(userList)) {
				propertyList.addAll(userList);
			}
		}
		if (time == 1) {
			List<EventProperty> timeList = eventPropertyDao
					.findAllByEventId(EventProperty.EVENT_TIME);
			if (CollectionUtils.isNotEmpty(timeList)) {
				propertyList.addAll(timeList);
			}
		}
		if (env == 1) {
			List<EventProperty> envList = eventPropertyDao
					.findAllByEventId(EventProperty.EVENT_ENV);
			if (CollectionUtils.isNotEmpty(envList)) {
				propertyList.addAll(envList);
			}
		}
		return propertyList;
	}

	@Override
	public List<EventProperty> getChartEventProperty(String eventId) {
		return eventPropertyDao
				.findAllByEventIdAndIsShowChartEqualsOrderByOrderId(eventId,
						(short) 1);
	}

	@Override
	public void deleteByEventId(String eventId) {
		eventPropertyDao.deleteByEventId(eventId);
	}

	@Override
	public void saveEventProperty(EventProperty eventProperty)
			throws BigDataBusinessException {
		List<EventProperty> listForName = this.findListBy(
				new String[] { "propertyName", "eventId" },
				new String[] { eventProperty.getPropertyName(),
						eventProperty.getEventId() });
		if (StringUtils.isBlank(eventProperty.getId())) {
			if (listForName.size() > 0) {
				throw new BigDataBusinessException("该名称已存在!");
			}
			eventProperty.setId(UuidUtils.generateUuid());
			eventPropertyDao.save(eventProperty);
			return;
		}

		// 验证名称和code是否存在
		if (listForName.size() > 0) {
			if (!listForName.get(0).getId().equals(eventProperty.getId())) {
				throw new BigDataBusinessException("该名称已存在!");
			}
		}
		eventPropertyDao.update(eventProperty, new String[] { "propertyName",
				"orderId", "eventId", "fieldName", "orderJson", "isShowChart",
				"remark", "dataDictionary","isSequential" });
	}

	@Override
	public List<EventProperty> findByPage(Pagination page, String eventId,
			String unitId) {
		if (StringUtils.isBlank(eventId)) {
			Integer count = eventPropertyDao.countByUnitIdIn(Lists
					.newArrayList(unitId, "00000000000000000000000000000000"));
			page.setMaxRowCount(count == null ? 0 : count.intValue());
			return eventPropertyDao.findAllByPageAndUnitId(
					Pagination.toPageable(page), unitId);
		}
		Integer count = eventPropertyDao.countByEventId(eventId);
		page.setMaxRowCount(count == null ? 0 : count.intValue());
		return eventPropertyDao.findAllByPageAndEventId(
				Pagination.toPageable(page), eventId);
	}
}
