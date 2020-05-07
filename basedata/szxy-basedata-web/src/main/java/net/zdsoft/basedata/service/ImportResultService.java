/*
 * Project: v7
 * Author : shenke
 * @(#) ImportService.java Created on 2016-8-18
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.ImportResult;
import net.zdsoft.framework.entity.Pagination;


public interface ImportResultService extends BaseService<ImportResult, String> {

	/**
	 * 根据用户和业务id获取
	 * @param userId
	 * @param businessId
	 * @param page
	 * @return
	 */
	List<ImportResult> findListByUserIdAndBusinessId(String userId,String businessId,Pagination page);
	
	/**
	 * 根据业务id获取
	 * @param businessId
	 * @param page
	 * @return
	 */
	List<ImportResult> findListByBusinessId(String businessId,Pagination page);

}
