/*
* Project: v7
* Author : shenke
* @(#) ImportEntityDto.java Created on 2016-8-19
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.ImportEntity;

/**
 * 
 * @author: shenke
 * @version: 1.0
 * 2016-8-19下午5:42:07
 */
public class ImportEntityDto extends BaseDto{

	private ImportEntity importEntity;
	
	private String successUrl;
	private String errorUrl;
	private String originUrl;
	private String handlerUserName;
	
	public ImportEntity getImportEntity() {
		return importEntity;
	}
	public void setImportEntity(ImportEntity importEntity) {
		this.importEntity = importEntity;
	}
	public String getSuccessUrl() {
		return successUrl;
	}
	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}
	public String getErrorUrl() {
		return errorUrl;
	}
	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}
	public String getOriginUrl() {
		return originUrl;
	}
	public void setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
	}
	public String getHandlerUserName() {
		return handlerUserName;
	}
	public void setHandlerUserName(String handlerUserName) {
		this.handlerUserName = handlerUserName;
	}
	
	
}
