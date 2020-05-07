/*
 * Project: v7
 * Author : shenke
 * @(#) ImportServiceImpl.java Created on 2016-8-18
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.dao.ImportDao;
import net.zdsoft.basedata.entity.ImportEntity;
import net.zdsoft.basedata.service.ImportService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;

/**
 * @description: TODO
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-18下午3:38:54
 */
@Service("importService")
public class ImportServiceImpl extends BaseServiceImpl<ImportEntity, String> implements ImportService {
    @Autowired
    private ImportDao importDao;

    @Override
    protected BaseJpaRepositoryDao<ImportEntity, String> getJpaDao() {
        return importDao;
    }

    @Override
    protected Class<ImportEntity> getEntityClass() {
        return ImportEntity.class;
    }

    @Override
    public List<ImportEntity> findByTypeAndUnitId(String type, String unitId, Pagination page) {
        Page<ImportEntity> pages = importDao.findByTypeAndUnitId(type, unitId, page.toPageable());
        page.setMaxRowCount((int) pages.getTotalElements());
        page.initialize();
        return pages.getContent();
    }

    @Override
    public ImportEntity checkImportEntity(final String type) {
        /**
         * 处理之前可能因为特殊原因阻塞的导入任务,将其改为未导入状态
         */
        ImportEntity improting = findOne(new Specification<ImportEntity>() {
            @Override
            public Predicate toPredicate(Root<ImportEntity> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {

                List<Predicate> ps = Lists.newArrayList();
                ps.add(builder.equal(root.get("importType"), type));
                ps.add(builder.equal(root.get("status"), ImportEntity.IMPORT_STATUS_START));
                criteria.where(ps.toArray(new Predicate[0]));
                return criteria.getRestriction();
            }
        });
        /**
         * 新的等待导入的任务
         */
        ImportEntity newImproting = findOne(new Specification<ImportEntity>() {
            @Override
            public Predicate toPredicate(Root<ImportEntity> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {

                List<Predicate> ps = Lists.newArrayList();
                ps.add(builder.equal(root.get("importType"), type));
                ps.add(builder.equal(root.get("status"), ImportEntity.IMPORT_STATUS_WAIT));
                criteria.where(ps.toArray(new Predicate[0])).orderBy(
                        builder.asc(root.get("creationTime").as(Date.class)));
                return criteria.getRestriction();
            }
        });
        if (improting != null) {
            improting.setStatus(ImportEntity.IMPORT_STATUS_WAIT);
            saveAllEntitys(improting);
        }
        if (newImproting != null) {
            newImproting.setStatus(ImportEntity.IMPORT_STATUS_START);
            saveAllEntitys(newImproting);
        }
        return newImproting;
    }

    @Override
    public List<ImportEntity> saveAllEntitys(ImportEntity... importEntity) {
        return importDao.saveAll(checkSave(importEntity));
    }
}
