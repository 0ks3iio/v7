package net.zdsoft.familydear.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.familydear.entity.FamDearPlan;
import net.zdsoft.framework.entity.Pagination;

public interface FamDearPlanService extends BaseService<FamDearPlan, String> {
	
	public List<FamDearPlan> getFamilyDearPlanList(String unitId , String year);

	public List<FamDearPlan> findListByYearAndTitleByPage(String year,String title,String state,Pagination page);
}
