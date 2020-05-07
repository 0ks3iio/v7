/* 
 * @(#)SchoolCalendarRemoteServiceImpl.java    Created on 2017-3-14
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.remote.service.SchoolCalendarRemoteService;
import net.zdsoft.basedata.service.SchoolCalendarService;
import net.zdsoft.framework.utils.SUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("schoolCalendarRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SchoolCalendarRemoteServiceImpl implements SchoolCalendarRemoteService {

    @Autowired
    private SchoolCalendarService schoolCalendarService;

    @Override
    public String findCurrentWeekAndMaxWeek(String acadyear, String semester, String schoolId) {
        return SUtils.s(schoolCalendarService.findCurrentWeekAndMaxWeek(acadyear,semester,schoolId));
    }
}
