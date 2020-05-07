/*
* Project: v7
* Author : shenke
* @(#) WebArticleDto.java Created on 2016-10-12
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.nbwebsite.dto;

import net.zdsoft.basedata.dto.BaseDto;
import net.zdsoft.nbwebsite.entity.WebArticle;

/**
 * @description: 
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-12下午2:24:37
 */
public class WebArticleDto extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private WebArticle webArticle;
	
	private String commitUserName;
	private String auditUserName;
	
	public WebArticle getWebArticle() {
		return webArticle;
	}
	public void setWebArticle(WebArticle webArticle) {
		this.webArticle = webArticle;
	}
	public String getCommitUserName() {
		return commitUserName;
	}
	public void setCommitUserName(String commitUserName) {
		this.commitUserName = commitUserName;
	}
	public String getAuditUserName() {
		return auditUserName;
	}
	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}
	
	

}
