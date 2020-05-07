package net.zdsoft.basedata.remote.service.impl;

import java.util.Date;

import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.framework.utils.SUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dateInfoRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class DateInfoRemoteServiceImpl extends BaseRemoteServiceImpl<DateInfo,String> implements DateInfoRemoteService {

    @Autowired
    private DateInfoService dateInfoService;

    @Override
    protected BaseService<DateInfo, String> getBaseService() {
        return dateInfoService;
    }

	@Override
	public String findByDate(String schId, String acadyear, Integer semester,
			Date date) {
		return SUtils.s(dateInfoService.getDate(schId, acadyear, semester, date));
	}
	
	@Override
	public String findByDates(String schId, Date startDate, Date endDate) {
		return SUtils.s(dateInfoService.findByDates(schId, startDate, endDate));
	}

	@Override
	public String findByAcadyearAndSemester(String schId, String acadyear, Integer semester) {
		return SUtils.s(dateInfoService.findByAcadyearAndSemester(schId, acadyear, semester));
	}
	
	@Override
	public String findByWeek(String unitId, String acadyear,
			Integer semester, Integer week) {
		return SUtils.s(dateInfoService.findByWeek(unitId, acadyear, semester,week));
	}

	@Override
	public String getDate(String unitId, String acadyear, Integer semester, Date date) {
		return SUtils.s(dateInfoService.getDate(unitId, acadyear, semester, date));
	}
}
