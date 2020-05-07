package net.zdsoft.syncdata.custom.xunfei.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.syncdata.entity.XFTeacher;

public interface XFTeacherDao {

    public List<XFTeacher> findAll(String unitId);

    public List<XFTeacher> findLgTime(Date time);

}
