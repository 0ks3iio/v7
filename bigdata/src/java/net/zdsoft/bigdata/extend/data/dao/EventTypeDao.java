package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.EventType;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

/**
 * 事件类型
 *
 */
public interface EventTypeDao extends BaseJpaRepositoryDao<EventType, String> {

    List<EventType> findAllByUnitIdOrderByOrderId(String unitId);

}
