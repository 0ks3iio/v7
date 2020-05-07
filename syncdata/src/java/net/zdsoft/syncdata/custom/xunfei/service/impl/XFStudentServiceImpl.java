package net.zdsoft.syncdata.custom.xunfei.service.impl;

import java.util.Date;
import java.util.List;

import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.syncdata.custom.xunfei.dao.XFStudentDao;
import net.zdsoft.syncdata.custom.xunfei.service.XFStudentService;
import net.zdsoft.syncdata.entity.XFStudent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service("xFStudentService")
@Lazy(true)
public class XFStudentServiceImpl implements XFStudentService {

    @Autowired
    private XFStudentDao xfStudentDao;

    @Override
    public List<XFStudent> findLgTime(Date time, Pagination page) {
        return xfStudentDao.findLgTime(time, page);
    }

    @Override
    public List<XFStudent> findAll(String unitId, String time) {
        return xfStudentDao.findAll(unitId, time);
    }

}
