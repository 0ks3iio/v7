package net.zdsoft.eclasscard.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccAttenceGateGrade;

public interface EccAttenceGateGradeService extends BaseService<EccAttenceGateGrade, String> {

	public Map<String,List<EccAttenceGateGrade>>  findByPeriodIdMap(String[] ids);
//	public Map<String,List<EccAttenceGateGrade>>  findByGradeMap(String[] gradeCodes);
	/**
	 * @param unitId
	 * @param classify //1进出校  2上下学 必填
	 * @param type
	 * @param classify
	 * @param gradeCodes
	 * @return
	 */
	public List<EccAttenceGateGrade> findByUnitIdAndTypeInCodes(String unitId,Integer classify,
			Integer type, String[] gradeCodes);
	
	public List<EccAttenceGateGrade> findByInOutAndCode(String unitId, String gradeCode);
	
	public List<EccAttenceGateGrade> findByInOutAndClassify(String unitId, Integer classify);
	
	public List<EccAttenceGateGrade> findByClassifyAndPeriodId(Integer classify, String periodId);
	
	public List<EccAttenceGateGrade> findInOutByAll();
	
	public boolean findByBettwenTime(String unitId,Integer classify,Integer type,String gradeCode,String time);
	
	public List<EccAttenceGateGrade> findByUnitIdAndClassifyAndGradeCodes(String unitId,Integer classify, String[] gradeCodes);

}
