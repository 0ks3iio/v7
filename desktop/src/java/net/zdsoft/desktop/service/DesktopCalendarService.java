package net.zdsoft.desktop.service;


import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.desktop.entity.DesktopCalendar;

import java.util.Date;
import java.util.List;

/**
 * @author yangsj  2017-5-9下午2:54:38
 */
public interface DesktopCalendarService extends BaseService<DesktopCalendar, String>{

	/**
	 * @param userId TODO
	 * @param nowTime
	 * @param addDays
	 * @return
	 */
	List<DesktopCalendar> findConstantsByTimeSlot(String userId,Date beginTime, Date endTime);

	/**
	 * 根据通知时间 精确查找
	 * @param notifyTime 通知时间
	 * @return maybe null by jpa
	 */
	List<DesktopCalendar> findByNotifyTime(Date notifyTime);
}
