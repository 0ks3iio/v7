/* 
 * @(#)McodeDaoTest.java    Created on 2017-3-2
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.zdsoft.system.dao.mcode.McodeDetailDao;

/**
 * @author gzjsd
 * @version $Revision: 1.0 $, $Date: 2017-3-2 下午02:38:07 $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:conf/spring/applicationContext.xml")
public class McodeDaoTest {
    @Resource
    private McodeDetailDao mcodeDetailDao;

    @Test
    public void TestFindByMcodeContentLike() {
        mcodeDetailDao.findByMcodeContentLike("DM-XB", "男");
    }
}
