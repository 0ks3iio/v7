package net.zdsoft.eclasscard.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormGrade;

public interface EccAttenceDormGradeService extends BaseService<EccAttenceDormGrade, String> {

	public Map<String,List<EccAttenceDormGrade>>  findByPeriodIdMap(String[] ids);
	
	public Map<String,List<EccAttenceDormGrade>>  findByGradeMap(String unitId,Integer type,String[] gradeCodes);
	
	public void deleteByUnitIdPeriodId(String unitId, String periodId);
	public boolean findByBettwenTime(String unitId,Integer type,String gradeCode,String time,boolean nextHoliday);
	/**
	 * unitId type gradeCode获取考勤年级list
	 * @param unitId
	 * @param type
	 * @param gradeCode
	 * @return
	 */
	public List<EccAttenceDormGrade> findListByCon(String unitId,Integer[] types,String[] gradeCodes);
}
