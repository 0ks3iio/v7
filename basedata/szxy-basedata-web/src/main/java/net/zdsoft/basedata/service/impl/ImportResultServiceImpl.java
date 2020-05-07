/*
 * Project: v7
 * Author : shenke
 * @(#) ImportServiceImpl.java Created on 2016-8-18
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service.impl;

import java.util.List;

import net.zdsoft.basedata.dao.ImportResultDao;
import net.zdsoft.basedata.entity.ImportResult;
import net.zdsoft.basedata.service.ImportResultService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("importResultService")
public class ImportResultServiceImpl extends BaseServiceImpl<ImportResult, String> implements ImportResultService {
    @Autowired
    private ImportResultDao importResultDao;

    @Override
    protected BaseJpaRepositoryDao<ImportResult, String> getJpaDao() {
        return importResultDao;
    }

    @Override
    protected Class<ImportResult> getEntityClass() {
        return ImportResult.class;
    }

    @Override
    public List<ImportResult> findListByUserIdAndBusinessId(String userId,String businessId,Pagination page){
    	return importResultDao.findListByUserIdAndBusinessId(userId, businessId, Pagination.toPageable(page));
    }
    
    @Override
    public List<ImportResult> findListByBusinessId(String businessId,Pagination page){
    	return importResultDao.findListByBusinessId(businessId, Pagination.toPageable(page));
    }



}
