/*
 * Project: v7
 * Author : shenke
 * @(#) SystemOptionDao.java Created on 2016-8-25
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.system.dao.config;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.config.SysOption;

/**
 * @description: 系统参数
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-25下午5:50:19
 */
public interface SysOptionDao extends BaseJpaRepositoryDao<SysOption, String> {

    @Query("From SysOption where optionCode in (?1)")
    List<SysOption> findByOptionCode(String... optionCode);

    @Modifying
    @Query("update SysOption set nowValue = ?1 where id=?2")
    void updateNowValue(String nowValue, String id);

    @Modifying
    @Query("update SysOption set nowValue = ?1 where optionCode=?2")
    void updateNowValueByCode(String nowValue, String code);

    @Modifying
    @Query("update SysOption set valueType = ?1 where optionCode=?2")
    void updateValueType(int valueType, String code);
}
