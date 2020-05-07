package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.Event;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * 事件
 *
 */
public interface EventDao extends BaseJpaRepositoryDao<Event, String> {

    List<Event> findAllByTypeIdOrderByOrderId(String typeId);

    @Modifying
	@Query("update Event set lastCommitDate = ?2 Where id = ?1")
	public void updateLastCommitDateByEventId(String eventId,Date lastCommitDate);

    List<Event> findByUnitIdOrderByOrderId(String unitId);

    void deleteByTypeId(String typeId);
}
