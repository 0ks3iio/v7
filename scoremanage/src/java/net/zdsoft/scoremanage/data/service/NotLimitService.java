package net.zdsoft.scoremanage.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.NotLimit;

public interface NotLimitService extends BaseService<NotLimit, String>{
	
	List<String> findTeacherIdByUnitId(String unitId);
	
	void saveTeacherIds(String[] teacherIds,String unitId);
	
	void deleteByUnitId(String unitId);
}
