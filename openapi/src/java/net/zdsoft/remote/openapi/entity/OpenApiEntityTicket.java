package net.zdsoft.remote.openapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_openapi_entity_ticketkey")
public class OpenApiEntityTicket extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    @Column(name = "ticket_key", length = 32, nullable = false)
    private String ticketKey;
    @Column(length = 50)
    private String type;
    @Column(name = "entity_column_name", length = 100)
    private String entityColumnName;
    private Integer displayOrder;

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
        return "openapientityticket";
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

}
