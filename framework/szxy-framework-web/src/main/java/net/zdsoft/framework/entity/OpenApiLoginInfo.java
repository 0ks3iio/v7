package net.zdsoft.framework.entity;

import java.io.Serializable;

public class OpenApiLoginInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userId;
	private String ticketKey;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTicketKey() {
		return ticketKey;
	}
	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}

}
