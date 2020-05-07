package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuDevelopHolidayNotice;

import java.util.List;

public interface StuDevelopHolidayNoticeService extends BaseService<StuDevelopHolidayNotice,String> {


    public List<StuDevelopHolidayNotice> getStuDevelopHolidayNoticeByUnitId(String acadyear, String semester, String UnitId);
}
