package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.SchoolSemester;

public interface SchoolSemesterRemoteService extends BaseRemoteService<SchoolSemester,String>{

	String findBySchoolIdIn(String[] schoolIds);

}
