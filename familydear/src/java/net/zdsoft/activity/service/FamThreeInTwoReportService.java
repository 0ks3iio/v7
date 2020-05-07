package net.zdsoft.activity.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.activity.entity.FamDearThreeInTwoReport;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;

public interface FamThreeInTwoReportService extends BaseService<FamDearThreeInTwoReport,String>{
	
	public List<FamDearThreeInTwoReport> getFamDearThreeInTwoReportByUnitIdAndOthers(String unitId,String userId,Date startTime,Date endTime,
			String title,String[] stuId,Pagination pagination);
	
	public List<FamDearThreeInTwoReport> getFamDearThreeInTwoReportsByUnitIdAndUserIds(String unitId,String[] userId,Date startTime,
			Date endTime,String title,Pagination pagination);

}
