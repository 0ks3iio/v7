package net.zdsoft.syncdata.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.syncdata.entity.XFStudentEx;

public interface XFStudentExService extends BaseService<XFStudentEx, String> {

	List<XFStudentEx> saveAllEntitys(XFStudentEx... students);

}
