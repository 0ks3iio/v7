/*
 * Project: v7
 * Author : shenke
 * @(#) ImportService.java Created on 2016-8-18
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.ImportEntity;
import net.zdsoft.framework.entity.Pagination;

/**
 * @description: TODO
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-18下午3:37:57
 */
public interface ImportService extends BaseService<ImportEntity, String> {

    List<ImportEntity> findByTypeAndUnitId(String type, String unitId, Pagination page);

    ImportEntity checkImportEntity(String type);

    List<ImportEntity> saveAllEntitys(ImportEntity... importEntity);
}
