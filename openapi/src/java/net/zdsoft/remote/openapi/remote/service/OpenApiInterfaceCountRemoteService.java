package net.zdsoft.remote.openapi.remote.service;

import java.util.Map;

/**
 * @author yangsj  2018年7月17日下午2:27:16
 */
public interface OpenApiInterfaceCountRemoteService {

	/**
	 * @param ticketKey
	 * @param passType
	 * @return
	 */
	String findByTicketKeyAndTypeIn(String ticketKey, String[] passType);

	
	Map<String, Integer> getTypeCountMap(String ticketKey, String[] array);

}
