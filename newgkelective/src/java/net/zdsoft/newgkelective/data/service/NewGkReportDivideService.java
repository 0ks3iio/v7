package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkReport;
import net.zdsoft.newgkelective.data.entity.NewGkReportDivide;

public interface NewGkReportDivideService extends BaseService<NewGkReportDivide, String>{

	List<NewGkReportDivide> findByReportIdAnd(String[] reportIds, String subjectId);

	void saveAndDelete(NewGkReport report, List<NewGkReportDivide> divideList);
}
