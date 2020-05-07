/*
* Project: v7
* Author : shenke
* @(#) ImportDao.java Created on 2016-8-18
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.ImportResult;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;


public interface ImportResultDao extends BaseJpaRepositoryDao<ImportResult, String> {

	@Query("From ImportResult where userId = ?1 and businessId=?2 order by modifyTime desc")
	List<ImportResult> findListByUserIdAndBusinessId(String userId,String businessId,Pageable page);
	
	@Query("From ImportResult where businessId=?1 order by modifyTime desc")
	List<ImportResult> findListByBusinessId(String businessId,Pageable page);
}
