/* 
 * @(#)OpenapiEntityTicketServiceImpl.java    Created on 2017-3-7
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.base.dao.OpenApiEntityTicketDao;
import net.zdsoft.base.entity.eis.OpenApiEntityTicket;
import net.zdsoft.base.service.OpenApiEntityTicketService;
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
@Service("OpenapiEntityTicketService")
public class OpenapiEntityTicketServiceImpl extends BaseServiceImpl<OpenApiEntityTicket, String> implements OpenApiEntityTicketService {
    @Autowired
    private OpenApiEntityTicketDao openapiEntityTicketDao;

    @Override
    protected BaseJpaRepositoryDao<OpenApiEntityTicket, String> getJpaDao() {
        return openapiEntityTicketDao;
    }

    @Override
    protected Class<OpenApiEntityTicket> getEntityClass() {
        return OpenApiEntityTicket.class;
    }

    @Override
    public List<OpenApiEntityTicket> getEntitys(String ticketKey, int isSenitive, String... types) {
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
            List<OpenApiEntityTicket> entitys = new ArrayList<>();
            for (String str : columnNames) {
                OpenApiEntityTicket en = new OpenApiEntityTicket();
                en.setTicketKey(ticketKey);
                en.setType(type);
                en.setId(UuidUtils.generateUuid());
                entitys.add(en);
            }
            openapiEntityTicketDao.saveAll(entitys);
        }
    }

	@Override
	public void deleteByType(String type) {
		openapiEntityTicketDao.deleteByType(type);
	}

	@Override
	public List<OpenApiEntityTicket> findByTicketKeyAndTypeIn(String ticketKey,
			String... types) {
		return openapiEntityTicketDao.findByTicketKeyAndTypeIn(ticketKey, types);
	}

	@Override
	public void deleteByTypeInAndTicketKey(String[] type, String ticketKey) {
		openapiEntityTicketDao.deleteByTypeInAndTicketKey(type,ticketKey);
	}
}
