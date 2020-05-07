package net.zdsoft.desktop.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.desktop.entity.DesktopCalendar;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj  2017-5-9下午2:58:10
 */
public interface DesktopCalendarDao extends BaseJpaRepositoryDao<DesktopCalendar, String> {
    
	String SQL_AFTER=" and isDeleted= 0  Order By beginTime asc";
	/**
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@Query("From DesktopCalendar  Where userId = ?1  and beginTime>=?2 and endTime <=?3 "+SQL_AFTER)
	List<DesktopCalendar> findConstantsByTimeSlot(String userId,Date beginTime, Date endTime);

}
