package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.extend.data.dao.EventIndicatorDao;
import net.zdsoft.bigdata.extend.data.entity.EventIndicator;
import net.zdsoft.bigdata.extend.data.service.EventIndicatorService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 事件指标接口
 * @author jiangf
 *
 */
@Service("eventIndicatorService")
public class EventIndicatorServiceImpl extends BaseServiceImpl<EventIndicator, String>
implements EventIndicatorService{

	@Autowired
	private EventIndicatorDao eventIndicatorDao;
	
	@Override
	public List<EventIndicator> findIndicatorListByEventId(String eventId) {
		return eventIndicatorDao.findIndicatorListByEventId(eventId);
	}

	@Override
	public void saveEventIndicator(EventIndicator eventIndicator) throws BigDataBusinessException {
		List<EventIndicator> listForName = this.findListBy(new String[]{"indicatorName", "eventId"}, new String[]{eventIndicator.getIndicatorName(), eventIndicator.getEventId()});
		if (StringUtils.isBlank(eventIndicator.getId())) {
			if (listForName.size() > 0) {
				throw new BigDataBusinessException("该名称已存在!");
			}
			eventIndicator.setId(UuidUtils.generateUuid());
			eventIndicatorDao.save(eventIndicator);
			return;
		}

		// 验证名称和code是否存在
		if (listForName.size() > 0) {
			if (!listForName.get(0).getId().equals(eventIndicator.getId())) {
				throw new BigDataBusinessException("该名称已存在!");
			}
		}

		eventIndicatorDao.update(eventIndicator, new String[]{"indicatorName", "eventId",
				"aggType", "aggField", "aggOutputName", "orderId", "remark"});
	}

	@Override
	public List<EventIndicator> findByUnitId(String unitId) {
		return eventIndicatorDao.findAllByUnitIdOrderByOrderId(unitId);
	}

	@Override
	protected BaseJpaRepositoryDao<EventIndicator, String> getJpaDao() {
		return eventIndicatorDao;
	}

	@Override
	protected Class<EventIndicator> getEntityClass() {
		return EventIndicator.class;
	}

}
