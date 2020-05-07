/* 
 * @(#)DeveloperDaoTest.java    Created on 2017-3-7
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.BaseTest;
import net.zdsoft.remote.openapi.entity.Developer;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-7 上午09:18:44 $
 */
public class DeveloperDaoTest extends BaseTest {
    @Resource
    private DeveloperDao developerDao;

    @Test
    public void testFindAllOdereByCreationTime() {
        List<Developer> all = developerDao.findAllOdereByCreationTime();
        System.out.println(SUtils.s(all));
    }
}
