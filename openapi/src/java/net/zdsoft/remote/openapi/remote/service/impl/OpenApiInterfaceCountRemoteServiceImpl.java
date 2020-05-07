package net.zdsoft.remote.openapi.remote.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.remote.service.OpenApiInterfaceCountRemoteService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceCountService;

/**
 * @author yangsj  2018年7月17日下午2:28:23
 */
@Service("openApiInterfaceCountRemoteService")
public class OpenApiInterfaceCountRemoteServiceImpl implements OpenApiInterfaceCountRemoteService {

	@Autowired
	private  OpenApiInterfaceCountService openApiInterfaceCountService;
	
	@Override
	public String findByTicketKeyAndTypeIn(String ticketKey, String[] passType) {
		return SUtils.s(openApiInterfaceCountService.findByTicketKeyAndTypeIn(ticketKey,passType));
	}

	@Override
	public Map<String, Integer> getTypeCountMap(String ticketKey, String[] types) {
		return openApiInterfaceCountService.getTypeCountMap(ticketKey,types);
	}


}
