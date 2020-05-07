package net.zdsoft.syncdata.custom.xunfei.service;

import java.util.List;

import net.zdsoft.syncdata.entity.XFTeacher;

public interface XFTeacherService {

    List<XFTeacher> findAll(String unitId);

}
