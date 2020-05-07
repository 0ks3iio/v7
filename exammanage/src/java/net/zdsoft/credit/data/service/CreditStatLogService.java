package net.zdsoft.credit.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.credit.data.entity.CreditStatLog;

public interface CreditStatLogService extends BaseService<CreditStatLog, String> {
    void deleteByParams(String year ,String semster,String gradeId);

	CreditStatLog findBySetIdAndGradeId(String setId, String gradeId);
	
	
}
