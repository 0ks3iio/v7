/* 
 * @(#)SchoolCalendarServiceImpl.java    Created on 2017-3-14
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.service.impl;

import net.zdsoft.basedata.dao.DateInfoDao;
import net.zdsoft.basedata.service.SchoolCalendarService;
import net.zdsoft.framework.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("schoolCalendarService")
public class SchoolCalendarServiceImpl implements SchoolCalendarService {

    public static final String KEY_CURRENT_WEEK = "current";
    public static final String KEY_MAX_WEEK = "max";

    @Autowired
    private DateInfoDao dateInfoDao;

    @Override
    public Integer findMaxWeekByAcadyearAndSemesterAndSchId(String acadyear, String semester, String schoolId) {
        return dateInfoDao.findMaxWeekByAcadyearAndSemesterAndSchId(acadyear, semester, schoolId);
    }

    @Override
    public Integer findWeekByInfoDateAndSchoolId(Date date, String schoolId) {
        return dateInfoDao.findWeekByInfoDateAndSchoolId(date, schoolId);
    }


	@Override
	public Map<String, Integer> findCurrentWeekAndMaxWeek(String acadyear, String semester, String schoolId) {
		Map<String, Integer> map = new HashMap<String, Integer>();
        map.put(KEY_CURRENT_WEEK, findWeekByInfoDateAndSchoolId(DateUtils.getStartDate(new Date()), schoolId));
        map.put(KEY_MAX_WEEK, findMaxWeekByAcadyearAndSemesterAndSchId(acadyear, semester, schoolId));
		return map;
	}

}
