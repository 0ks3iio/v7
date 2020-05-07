package net.zdsoft.stuwork.data.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyNightScheduling;

/**
 * @author yangsj  2017年11月30日下午2:59:52
 */
public interface DyNightSchedulingService extends BaseService<DyNightScheduling, String>{

	/**
	 * @param unitId
	 * @param userId
	 * @param queryDate
	 * @return
	 */
	List<DyNightScheduling> findByUserAndNightTime(String unitId, String userId,
			Date queryDate);

	/**
	 * @param unitId
	 * @param datas
	 * @return
	 */
	String doImport(String unitId, List<String[]> datas);

	/**
	 * @param unitId
	 * @param queryDate
	 * @return
	 */
	List<DyNightScheduling> findByUnitIdAndNightTime(String unitId, Date nightTime);

	/**
	 * @param teacherId
	 * @param classId
	 * @param queryDate
	 * @return
	 */
	DyNightScheduling findByTeaIdAndClaIdAndTime(String unitId, String classId, Date queryDate);

	/**
	 * @param queryDate
	 * @param array
	 */
	void deleteByNightTimeAndClazzs(Date queryDate, String[] array);

	
}
