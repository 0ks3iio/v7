/* 
 * @(#)SchoolCalendarService.java    Created on 2017-3-14
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.service;

import java.util.Date;
import java.util.Map;

public interface SchoolCalendarService {

    /**
     * 取指定学校某个学年学期的最大周数
     * 
     * @author dingw
     * @param acadyear
     * @param semester
     * @param schoolId
     * @return
     */
    Integer findMaxWeekByAcadyearAndSemesterAndSchId(String acadyear, String semester, String schoolId);

    /**
     * 判断指定日期所在的周次
     * 
     * @author dingw
     * @param date
     * @param schoolId
     * @return
     */
    Integer findWeekByInfoDateAndSchoolId(Date date, String schoolId);

	Map<String, Integer> findCurrentWeekAndMaxWeek(String acadyear, String semester, String schoolId);

}
