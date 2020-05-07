package net.zdsoft.remote.openapi.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceCount;

/**
 * @author yangsj  2018年1月5日上午10:18:30
 */
public interface OpenApiInterfaceCountService extends BaseService<OpenApiInterfaceCount, String>{

	/**
	 * @param ticketKey
	 * @return
	 */
	List<OpenApiInterfaceCount> findDoInterfaceNum(String ticketKey);

	/**
	 * @param ticketKey
	 * @param type
	 * @return
	 */
	List<OpenApiInterfaceCount> findDoInterfaceNum(String ticketKey, String type);

	/**
	 * @param ticketKey
	 * @param uri
	 * @return
	 */
	List<OpenApiInterfaceCount> findDoInterfaceNum(String ticketKey,String type,String uri);

	/**
	 * @param ticketKey
	 * @param types
	 * @return
	 */
	List<OpenApiInterfaceCount> findByTicketKeyAndTypeIn(String ticketKey, String[] types);

	/**
	 * @param ticketKey
	 * @param interfaceType
	 * @param start
	 * @param end
	 * @return
	 */
	List<OpenApiInterfaceCount> findDoInterfaceNum(String ticketKey, String interfaceType, Date start, Date end);

	List<OpenApiInterfaceCount> findByType(String type);

	Map<String, Integer> findCountByType(String type);

	List<OpenApiInterfaceCount> findDoInterfaceNumAndPage(String ticketKey,
			String interfaceType, Date start, Date end, Pagination page);

	Map<String, Integer> getTypeCountMap(String ticketKey, String[] types);

	

}
