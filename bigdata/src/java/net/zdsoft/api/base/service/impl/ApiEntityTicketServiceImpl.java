/* 
 * @(#)OpenapiEntityTicketServiceImpl.java    Created on 2017-3-7
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.api.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.api.base.dao.ApiEntityTicketDao;
import net.zdsoft.api.base.entity.eis.ApiEntityTicket;
import net.zdsoft.api.base.service.ApiEntityTicketService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-7 上午10:08:52 $
 */
@Service("apiEntityTicketService")
public class ApiEntityTicketServiceImpl extends BaseServiceImpl<ApiEntityTicket, String> implements ApiEntityTicketService {
    @Autowired
    private ApiEntityTicketDao apiEntityTicketDao;

    @Override
    protected BaseJpaRepositoryDao<ApiEntityTicket, String> getJpaDao() {
        return apiEntityTicketDao;
    }

    @Override
    protected Class<ApiEntityTicket> getEntityClass() {
        return ApiEntityTicket.class;
    }

    @Override
    public List<ApiEntityTicket> getEntitys(String ticketKey, int isSenitive, String... types) {
        return null;
    }

    @Override
    public List<String> getColumnNames(String ticketKey, int isSenitive, String type) {
        return null;
    }

    @Override
    public void updateOpenapiEntityTicket(String type, String ticketKey, String[] columnNames, int isSensitive) {
//    	openapiEntityTicketDao.delete(type, ticketKey, isSensitive);
        if (!Validators.isEmpty(columnNames)) {
            List<ApiEntityTicket> entitys = new ArrayList<>();
            for (String str : columnNames) {
                ApiEntityTicket en = new ApiEntityTicket();
                en.setTicketKey(ticketKey);
                en.setType(type);
                en.setId(UuidUtils.generateUuid());
                entitys.add(en);
            }
            apiEntityTicketDao.saveAll(entitys);
        }
    }

	@Override
	public void deleteByType(String type) {
		apiEntityTicketDao.deleteByType(type);
	}

	@Override
	public List<ApiEntityTicket> findByTicketKeyAndTypeIn(String ticketKey,
			String... types) {
		return apiEntityTicketDao.findByTicketKeyAndTypeIn(ticketKey, types);
	}

	@Override
	public void deleteByTypeInAndTicketKey(String[] type, String ticketKey) {
		apiEntityTicketDao.deleteByTypeInAndTicketKey(type,ticketKey);
	}

	@Override
	public List<ApiEntityTicket> fingByTicketKeyAndInterfaceIdIn(
			String ticketKey, String[] interfaceIds) {
		return apiEntityTicketDao.fingByTicketKeyAndInterfaceIdIn(ticketKey,interfaceIds);
	}
}
