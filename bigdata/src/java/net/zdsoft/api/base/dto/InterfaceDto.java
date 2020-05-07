/* 
 * @(#)InterfaceDto.java    Created on 2017-3-6
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.api.base.dto;

import java.io.Serializable;
import java.util.List;
import net.zdsoft.api.base.entity.eis.ApiEntity;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.entity.eis.ApiParameter;
public class InterfaceDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<ApiEntity> entitys;//
    private List<ApiParameter> parameters;
    private ApiInterface apiInterface;
    private boolean isShow;
    private String  ticketKey;
	public List<ApiEntity> getEntitys() {
		return entitys;
	}
	public void setEntitys(List<ApiEntity> entitys) {
		this.entitys = entitys;
	}
	public List<ApiParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<ApiParameter> parameters) {
		this.parameters = parameters;
	}
	public ApiInterface getApiInterface() {
		return apiInterface;
	}
	public void setApiInterface(ApiInterface apiInterface) {
		this.apiInterface = apiInterface;
	}
	public boolean isShow() {
		return isShow;
	}
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}
	public String getTicketKey() {
		return ticketKey;
	}
	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}
}
