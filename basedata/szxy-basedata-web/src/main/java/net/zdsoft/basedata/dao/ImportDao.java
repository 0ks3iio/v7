/*
* Project: v7
* Author : shenke
* @(#) ImportDao.java Created on 2016-8-18
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dao;

import org.springframework.context.annotation.Lazy;

import net.zdsoft.basedata.entity.ImportEntity;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @description: TODO
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-18下午3:39:44
 */
@Lazy
public interface ImportDao extends BaseJpaRepositoryDao<ImportEntity, String>, ImportJdbcDao {

    @Query(value = "select * from base_import_entity where import_Type=?1 and unit_Id=?2 order by creation_time desc,  ?#{#pageable}",
    countQuery = "select count(*) from base_import_entity  where import_Type=?1 and unit_Id=?2",
    nativeQuery = true)
    Page<ImportEntity> findByTypeAndUnitId(String type, String unitId, Pageable pageable);

    List<ImportEntity> findByStatusAndImportType(String status, String importType);
}
