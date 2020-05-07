/* 
 * @(#)ServiceDaoTest.java    Created on 2017-2-27
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.zdsoft.system.dao.server.ServerDao;
import net.zdsoft.system.entity.server.Server;

/**
 * @author xuxy
 * @version $Revision: 1.0 $, $Date: 2017-2-27 下午07:20:57 $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:conf/spring/applicationContext.xml")
public class ServerDaoTest {
    @Resource
    private ServerDao serverDao;

    @Test
    public void testFindInterfaceType() {
        List<Server> list = serverDao.findByOrderType(new Integer[] { 1, 2, 3 }, 1);
        Assert.assertNotNull(list);
    }
}
