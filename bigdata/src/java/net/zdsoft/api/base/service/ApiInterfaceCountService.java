package net.zdsoft.api.base.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.api.base.entity.eis.ApiInterfaceCount;
import net.zdsoft.basedata.service.BaseService;

/**
 * @author yangsj  2018年1月5日上午10:18:30
 */
public interface ApiInterfaceCountService extends BaseService<ApiInterfaceCount, String>{

	long findCountByType(String ticketKey, String type,
			Date startTime, Date endTime);

	long findCountByInterfaceId(String ticketKey, String interfaceId,
			Date startTime, Date endTime);

	List<ApiInterfaceCount> findByResultType(String type);

	List<ApiInterfaceCount> findByResultTypeAndCreationTime(String type, Date mouthFirst, Date mouthEnd);

	List<String> getDistinctTicketKey();

	List<ApiInterfaceCount> findByTicketKeyAndCreationTime(String ticketKey,Date dateStart, Date dataEnd);

	List<ApiInterfaceCount> findByTicketKey(String ticketKey);
}
