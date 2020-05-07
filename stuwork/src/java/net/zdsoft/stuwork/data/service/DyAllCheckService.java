package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.dto.AllCheckDto;
import net.zdsoft.stuwork.data.entity.DyAllCheck;

public interface DyAllCheckService extends BaseService<DyAllCheck, String>{
	/**
	 * 统计班级综合考核
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param week
	 */
	 void saveStat(String unitId,String acadyear,String semester,int week);
	 /**
	 * 获取某班数据
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param week
	 * @param classId
	 */
	 DyAllCheck getAllCheckBy(String unitId,String acadyear,String semester,int week,String classId);
	 /**
	  * 汇总页
	  * @param unitId
	  * @param acadyear
	  * @param semester
	  * @return
	  */
	 List<AllCheckDto> getListBy(String unitId,String acadyear,String semester,int section,int allWeek);
}
