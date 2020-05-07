/* 
 * @(#)OpenApiApplyDaoTest.java    Created on 2017-2-24
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import net.zdsoft.remote.openapi.BaseTest;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-2-24 下午03:00:28 $
 */
public class OpenApiApplyDaoTest extends BaseTest {
    @Resource
    private OpenApiApplyDao openApiApplyDao;

    @Test
    public void findInterfaceTypeTest() {
        List<String> list = openApiApplyDao.findInterfaceTypes(2, "01234567890123456789012345678901");
        String[] strs = { "student" };
        Assert.assertArrayEquals(strs, list.toArray(new String[list.size()]));
    }
}
