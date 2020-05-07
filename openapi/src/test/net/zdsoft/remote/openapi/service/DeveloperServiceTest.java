/* 
 * @(#)DeveloperServiceTest.java    Created on 2017-2-23
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

import net.zdsoft.remote.openapi.BaseTest;
import net.zdsoft.remote.openapi.entity.Developer;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-2-23 下午05:56:54 $
 */
public class DeveloperServiceTest extends BaseTest {
    @Resource
    private DeveloperService developerService;

    @Test
    public void findByTicketKeyTest() {
        Developer key = developerService.findByTicketKey("WinuponTestTicketKey");
        System.out.println(JSON.toJSONString(key));
    }
}
