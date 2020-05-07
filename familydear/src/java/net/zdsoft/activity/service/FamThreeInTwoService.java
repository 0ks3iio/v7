package net.zdsoft.activity.service;

import java.util.List;

import net.zdsoft.activity.entity.FamDearThreeInTwo;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;

public interface FamThreeInTwoService extends BaseService<FamDearThreeInTwo,String>{

	public List<FamDearThreeInTwo> getListByUnitIdAndYearAndTitleAndState(String unitId,String year,String title,String state,Pagination page);
}
