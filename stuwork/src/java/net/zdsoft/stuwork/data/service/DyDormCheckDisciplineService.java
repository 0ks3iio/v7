package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormCheckDiscipline;

public interface DyDormCheckDisciplineService extends BaseService<DyDormCheckDiscipline, String>{
	/**
	 * 
	 * 
	 * */
	public List<DyDormCheckDiscipline> getCheckDisList(DormSearchDto dormDto,String[] buildingIds,Pagination page);
	/**unitId,dormDto中的各个参数 来获取汇总页面
	 * List<DyDormCheckDiscipline>
	 * 
	 * */
	public List<DyDormCheckDiscipline> getPoolByCon(String unitId,String[] classIds,DormSearchDto dormDto);
	/**unitId,dormDto中的各个参数 来获取详情
	 * List<DyDormCheckDiscipline>
	 * 
	 * */
	public List<DyDormCheckDiscipline> getDetailByCon(String unitId,DormSearchDto dormDto);
}
