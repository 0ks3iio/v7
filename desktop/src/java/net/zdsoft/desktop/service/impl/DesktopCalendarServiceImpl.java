package net.zdsoft.desktop.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.desktop.dao.DesktopCalendarDao;
import net.zdsoft.desktop.entity.DesktopCalendar;
import net.zdsoft.desktop.service.DesktopCalendarService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author yangsj  2017-5-9下午2:55:47
 */
@Service(value = "desktopCalendarService")
public class DesktopCalendarServiceImpl extends BaseServiceImpl<DesktopCalendar, String> implements DesktopCalendarService {
    
	@Autowired
	private DesktopCalendarDao desktopCalendarDao;
	
	@Override
	protected BaseJpaRepositoryDao<DesktopCalendar, String> getJpaDao() {
		return desktopCalendarDao;
	}

	@Override
	protected Class<DesktopCalendar> getEntityClass() {
		return DesktopCalendar.class;
	}

	@Override
	public List<DesktopCalendar> findConstantsByTimeSlot(String userId,
			Date beginTime, Date endTime) {
		return desktopCalendarDao.findConstantsByTimeSlot(userId,beginTime,endTime);
	}

	@Override
	public List<DesktopCalendar> findByNotifyTime(Date notifyTime) {
		return findListBy("notifyTime", notifyTime);
	}
}
