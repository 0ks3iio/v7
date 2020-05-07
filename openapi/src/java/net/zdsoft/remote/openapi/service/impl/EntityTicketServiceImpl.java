/* 
 * @(#)EntityTicketServiceImpl.java    Created on 2017-3-7
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.remote.openapi.dao.EntityTicketDao;
import net.zdsoft.remote.openapi.entity.EntityTicket;
import net.zdsoft.remote.openapi.service.EntityTicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-7 上午10:08:52 $
 */
@Service("entityTicketService")
public class EntityTicketServiceImpl extends BaseServiceImpl<EntityTicket, String> implements EntityTicketService {
    @Autowired
    private EntityTicketDao entityTicketDao;

    @Override
    protected BaseJpaRepositoryDao<EntityTicket, String> getJpaDao() {
        return entityTicketDao;
    }

    @Override
    protected Class<EntityTicket> getEntityClass() {
        return EntityTicket.class;
    }

    @Override
    public List<EntityTicket> getEntitys(String ticketKey, int isSenitive, String... types) {
        return entityTicketDao.findByTicketKeyAndIsSensitiveAndTypeIn(ticketKey, isSenitive, types);
    }

    @Override
    public List<String> getColumnNames(String ticketKey, int isSenitive, String type) {
        return entityTicketDao.findColumnNames(ticketKey, isSenitive, type);
    }

    @Override
    public void updateEntityTicket(String type, String ticketKey, String[] columnNames, int isSensitive) {
        entityTicketDao.delete(type, ticketKey, isSensitive);
        if (!Validators.isEmpty(columnNames)) {
            List<EntityTicket> entitys = new ArrayList<>();
            for (String str : columnNames) {
                EntityTicket en = new EntityTicket();
                en.setEntityColumnName(str);
                en.setIsSensitive(isSensitive);
                en.setTicketKey(ticketKey);
                en.setType(type);
                en.setId(UuidUtils.generateUuid());
                entitys.add(en);
            }
            entityTicketDao.saveAll(entitys);
        }
    }

	@Override
	public void deleteByType(String type) {
		entityTicketDao.deleteByType(type);
	}

	@Override
	public List<EntityTicket> findByTicketKeyAndTypeIn(String ticketKey,
			String... types) {
		return entityTicketDao.findByTicketKeyAndTypeIn(ticketKey, types);
	}

	@Override
	public void deleteByTypeInAndTicketKey(String[] type, String ticketKey) {
		entityTicketDao.deleteEntityTicket(type,ticketKey);
	}
}
