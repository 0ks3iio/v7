package net.zdsoft.syncdata.custom.xunfei.service.impl;

import java.util.List;

import net.zdsoft.syncdata.custom.xunfei.dao.XFTeacherDao;
import net.zdsoft.syncdata.custom.xunfei.service.XFTeacherService;
import net.zdsoft.syncdata.entity.XFTeacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service("xFTeacherService")
@Lazy(true)
public class XFTeacherServiceImpl implements XFTeacherService {

    @Autowired
    private XFTeacherDao xfTeacherDao; 
    
    @Override
    public List<XFTeacher> findAll(String unitId) {
        return xfTeacherDao.findAll(unitId);
    }

}
