package net.zdsoft.base.entity.eis;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="eis_openapi_param_ticket")
public class OpenapiApiParameterTicket extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "openapiApiParameterTicket";
	}
	private String interfaceId;
	private String paramId;
	private String ticketKey;
	private String type;
	public String getInterfaceId() {
		return interfaceId;
	}
	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}
	public String getParamId() {
		return paramId;
	}
	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
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
}
