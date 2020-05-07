package net.zdsoft.syncdata.custom.xunfei.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.syncdata.entity.XFStudent;

public interface XFStudentService {

    public List<XFStudent> findAll(String unitId, String time);

    public List<XFStudent> findLgTime(Date time, Pagination page);

}
