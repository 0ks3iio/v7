package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.SchoolSemester;

public interface SchoolSemesterService extends BaseService<SchoolSemester, String> {

	List<SchoolSemester> findBySchoolIdIn(String[] schoolIds);

}
