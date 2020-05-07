package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.ClassHour;

public interface ClassHourRemoteService extends BaseRemoteService<ClassHour,String> {

	String findListByUnitId(String acadyear, String semester, String unitId, String gradeId);
}
