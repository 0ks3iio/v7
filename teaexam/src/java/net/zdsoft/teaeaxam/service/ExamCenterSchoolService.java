package net.zdsoft.teaeaxam.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teaeaxam.entity.ExamCenterSchool;

/**
 * 
 * @author weixh
 * 2019年7月19日	
 */
public interface ExamCenterSchoolService extends BaseService<ExamCenterSchool, String> {
	public List<ExamCenterSchool> findCenterSchool();
	
	public ExamCenterSchool findCenterSchoolById(String id);
}
