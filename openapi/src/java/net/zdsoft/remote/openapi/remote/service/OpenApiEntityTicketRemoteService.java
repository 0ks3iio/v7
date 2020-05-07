package net.zdsoft.remote.openapi.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.remote.openapi.entity.EntityTicket;

public interface OpenApiEntityTicketRemoteService extends BaseRemoteService<EntityTicket, String>{

	String findByTicketKeyAndTypeIn(String ticketKey, String... types);

	void deleteByTypeInAndTicketKey(String[] type, String ticketKey);
}
