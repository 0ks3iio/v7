package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.EventProperty;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 事件属性
 *
 */
public interface EventPropertyDao extends BaseJpaRepositoryDao<EventProperty, String> {

    List<EventProperty> findAllByEventId(String eventId);

    List<EventProperty> findAllByEventIdAndIsShowChartEqualsOrderByOrderId(String eventId, Short isShowChart);

    void deleteByEventId(String eventId);

    Integer countByUnitIdIn(List<String> unitIds);

    Integer countByEventId(String eventId);

    @Query("FROM EventProperty where unitId = ?1 or unitId = '00000000000000000000000000000000' order by orderId")
    List<EventProperty> findAllByPageAndUnitId(Pageable page, String unitId);

    @Query("FROM EventProperty where eventId = ?1 order by orderId")
    List<EventProperty> findAllByPageAndEventId(Pageable page, String eventId);
}
