/* 
 * @(#)SchoolCalendarRemoteService.java    Created on 2017-3-14
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.remote.service;

public interface SchoolCalendarRemoteService {

    /**
     * 查询当前周次和指定学校某个学年学期的总周数
     * 
     * @author dingw
     * @param acadyear
     * @param semester
     * @param schoolId
     * @return JSON(Map<String,Integer>)
     */
    String findCurrentWeekAndMaxWeek(String acadyear, String semester, String schoolId);
}
