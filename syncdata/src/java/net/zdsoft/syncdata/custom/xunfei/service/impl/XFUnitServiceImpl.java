package net.zdsoft.syncdata.custom.xunfei.service.impl;

import java.util.List;

import net.zdsoft.syncdata.custom.xunfei.dao.XFUnitDao;
import net.zdsoft.syncdata.custom.xunfei.service.XFUnitService;
import net.zdsoft.syncdata.entity.XFUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service("xFUnitService")
@Lazy(true)
public class XFUnitServiceImpl implements XFUnitService {

    @Autowired
    private XFUnitDao xfUnitDao;

    @Override
    public List<XFUnit> findAll() {
        return xfUnitDao.findAll();
    }

}
