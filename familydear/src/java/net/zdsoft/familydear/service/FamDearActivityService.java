package net.zdsoft.familydear.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.familydear.dao.FamDearActivityDao;
import net.zdsoft.familydear.dto.FamActivityDto;
import net.zdsoft.familydear.entity.FamDearActivity;
import net.zdsoft.framework.entity.Pagination;

public interface FamDearActivityService extends BaseService<FamDearActivity, String> {
	
	public List<FamDearActivity> findByPlanIds(String[] planIds);

	public List<FamDearActivity> findByNameAndPlanIds(String activityName,String[] planIds);

	public List<FamDearActivity> findListByYearAndTitleByPage(String[] ids,String state,String year, Pagination pagination);

	public List<FamDearActivity> findListByYearAndState(String [] ids,String state,String year);

	public void saveFamDearActivityDto(FamActivityDto famActivityDto,String arrangeIds);
}
