package net.zdsoft.stuwork.data.service;


import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormForm;
import net.zdsoft.stuwork.data.entity.DyDormStatResult;

public interface DyDormStatResultService extends BaseService<DyDormStatResult, String>{
	/**
	 * 根据schoolId, acadyear,semesterStr,week统计DyDormStatResult 
	 * @param schoolId, acadyear,semesterStr,week
	 * @param 
	 * @return
	 */
	public void save(String schoolId,String acadyear,String semesterStr,int week);
	/**
	 * 根据schoolId, acadyear,semesterStr,week得到List DyDormStatResult 
	 * @param schoolId, acadyear,semesterStr,week
	 * @param 
	 * @return
	 */
	public List<DyDormStatResult> getStat(DormSearchDto dormDto);
	/**
	 * 获取所有班级的数据
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param week
	 * @return
	 */
	public List<DyDormStatResult> getStatNotClassId(String schoolId,String acadyear,String semester,int week);
	
	public List<DyDormForm> getStar(DormSearchDto dormDto,int allWeek);
	
}	
