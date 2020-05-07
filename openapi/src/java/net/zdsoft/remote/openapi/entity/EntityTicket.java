/* 
 * @(#)EntityTicket.java    Created on 2017-3-1
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-1 上午09:51:18 $
 */

@Entity
@Table(name = "base_openapi_entity_ticketkey")
public class EntityTicket extends BaseEntity<String> {
	public static final int IS_SENSITIVE_FALSE = 0;
	public static final int IS_SENSITIVE_TRUE = 1;
	
    private static final long serialVersionUID = 1L;
    @Column(name = "ticket_key", length = 32, nullable = false)
    private String ticketKey;
    @Column(length = 50)
    private String type;
    @Column(name = "entity_column_name", length = 100)
    private String entityColumnName;
    private Integer displayOrder;
    private Integer isSensitive;

    public String getTicketKey() {
        return ticketKey;
    }

    public void setTicketKey(String ticketKey) {
        this.ticketKey = ticketKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntityColumnName() {
        return entityColumnName;
    }

    public void setEntityColumnName(String entityColumnName) {
        this.entityColumnName = entityColumnName;
    }

    @Override
    public String fetchCacheEntitName() {
        return "entityticket";
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getIsSensitive() {
        return isSensitive;
    }

    public void setIsSensitive(Integer isSensitive) {
        this.isSensitive = isSensitive;
    }

}
