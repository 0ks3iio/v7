package net.zdsoft.syncdata.custom.xunfei.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.syncdata.entity.XFUnit;

public interface XFUnitDao {

    public List<XFUnit> findAll();

    public List<XFUnit> findLgTime(Date time);

}
