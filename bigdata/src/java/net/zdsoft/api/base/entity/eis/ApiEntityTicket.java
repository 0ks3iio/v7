package net.zdsoft.api.base.entity.eis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "bg_openapi_entity_ticketkey")
public class ApiEntityTicket extends BaseEntity<String> {
	public static final int IS_SENSITIVE_FALSE = 0;
	public static final int IS_SENSITIVE_TRUE = 1;
    private static final long serialVersionUID = 1L;
    @Override
    public String fetchCacheEntitName() {
        return "openapiEntityTicket";
    }
    @Column(name = "ticket_key", length = 32, nullable = false)
    private String ticketKey;
    @Column(length = 50)
    private String type;
    @Column(name = "interface_id", length = 32)
    private String interfaceId;
    @Column(name = "entity_id", length = 32, nullable = false)
    private String entityId;

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

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
}
