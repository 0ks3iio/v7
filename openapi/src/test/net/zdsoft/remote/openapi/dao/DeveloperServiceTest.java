/* 
 * @(#)DeveloperServiceTest.java    Created on 2017-3-6
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.zdsoft.remote.openapi.BaseTest;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-6 下午03:29:10 $
 */
public class DeveloperServiceTest extends BaseTest {
    @Autowired
    private DeveloperDao developerDao;

    @Test
    public void testGetAllOdereByCreationTime() {
        // List<DeveloperDto> all = developerDao.findAllOdereByCreationTime();
        // System.out.println(SUtils.s(all));
    }
}
