package net.zdsoft.remote.openapi.remote.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.entity.EntityTicket;
import net.zdsoft.remote.openapi.remote.service.OpenApiEntityTicketRemoteService;
import net.zdsoft.remote.openapi.service.EntityTicketService;

@Service("openApiEntityTicketRemoteService")
public class OpenApiEntityTicketRemoteServiceImpl extends BaseRemoteServiceImpl<EntityTicket, String> implements OpenApiEntityTicketRemoteService{

	@Autowired
    private EntityTicketService entityTicketService;
	
	@Override
	protected BaseService<EntityTicket, String> getBaseService() {
		return entityTicketService;
	}

	@Override
	public String findByTicketKeyAndTypeIn(String ticketKey, String... types) {
		return SUtils.s(entityTicketService.findByTicketKeyAndTypeIn(ticketKey,types));
	}

	@Override
	public void deleteByTypeInAndTicketKey(String[] type, String ticketKey) {
		entityTicketService.deleteByTypeInAndTicketKey(type,ticketKey);
	}
}
