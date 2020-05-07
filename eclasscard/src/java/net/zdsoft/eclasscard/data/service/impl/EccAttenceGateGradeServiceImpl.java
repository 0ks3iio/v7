package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccAttenceGateGradeDao;
import net.zdsoft.eclasscard.data.entity.EccAttenceGateGrade;
import net.zdsoft.eclasscard.data.service.EccAttenceGateGradeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
@Service("eccAttenceGateGradeService")
public class EccAttenceGateGradeServiceImpl extends BaseServiceImpl<EccAttenceGateGrade, String> implements EccAttenceGateGradeService{

	@Autowired
	private EccAttenceGateGradeDao eccAttenceGateGradeDao;
	@Override
	protected BaseJpaRepositoryDao<EccAttenceGateGrade, String> getJpaDao() {
		return eccAttenceGateGradeDao;
	}

	@Override
	protected Class<EccAttenceGateGrade> getEntityClass() {
		return EccAttenceGateGrade.class;
	}

	@Override
	public Map<String,List<EccAttenceGateGrade>>  findByPeriodIdMap(String[] ids) {
		Map<String,List<EccAttenceGateGrade>> gateGradeListMap  = Maps.newHashMap();
		List<EccAttenceGateGrade> gateGrades = findListByIn("periodId", ids);
		for(EccAttenceGateGrade gateGrade:gateGrades){
			List<EccAttenceGateGrade> dgs = gateGradeListMap.get(gateGrade.getPeriodId());
			if(dgs==null){
				dgs = Lists.newArrayList();
			}
			dgs.add(gateGrade);
			gateGradeListMap.put(gateGrade.getPeriodId(), dgs);
		}
		return gateGradeListMap;
	}

//	@Override
//	public Map<String, List<EccAttenceGateGrade>> findByGradeMap(
//			String[] gradeCodes) {
//		Map<String,List<EccAttenceGateGrade>> gateGradeListMap  = Maps.newHashMap();
//		List<EccAttenceGateGrade> gateGrades = eccAttenceGateGradeDao.findListByUnitIdInGrade(unitId, type, gradeCodes);
//		for(EccAttenceGateGrade gateGrade:gateGrades){
//			List<EccAttenceGateGrade> dgs = gateGradeListMap.get(gateGrade.getPeriodId());
//			dgs.add(gateGrade);
//			gateGradeListMap.put(gateGrade.getGrade(), dgs);
//		}
//		return gateGradeListMap;
//	}

	@Override
	public List<EccAttenceGateGrade> findByUnitIdAndTypeInCodes(String unitId,Integer classify,
			Integer type, String[] gradeCodes) {
		if(gradeCodes==null){
			return Lists.newArrayList();
		}
		return eccAttenceGateGradeDao.findByUnitIdAndTypeInCodes(unitId, classify,type, gradeCodes);
	}

	@Override
	public boolean findByBettwenTime(String unitId, Integer classify,Integer type,
			String gradeCode, String time) {
		List<EccAttenceGateGrade> gateGrades = eccAttenceGateGradeDao.findByUnitIdAndTypeInCodes(unitId, classify,type, new String[]{gradeCode});
		for(EccAttenceGateGrade gateGrade:gateGrades){
			if(addTimeStr(gateGrade.getBeginTime()).compareTo(time)<=0 && 
					addTimeStr(gateGrade.getEndTime()).compareTo(time)>=0){
				if(EccConstants.GATE_ATTENCE_PERIOD_TYPE3==type){
					if(DateUtils.date2StringByDay(new Date()).equals(DateUtils.date2StringByDay(gateGrade.getTempDate()))){
						return true;
					}
				}else if(EccConstants.GATE_ATTENCE_PERIOD_TYPE1==type){
					return true;
				}else if(EccConstants.GATE_ATTENCE_PERIOD_TYPE2==type){
					return true;
				}else if(EccConstants.GATE_ATTENCE_PERIOD_TYPE4==type){
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	
	private String addTimeStr(String timeStr){
		if(StringUtils.isNotBlank(timeStr)){
			if(timeStr.length()==4){
				timeStr="0"+timeStr;
			}
		}
		return timeStr;
	}

	@Override
	public List<EccAttenceGateGrade> findByInOutAndCode(String unitId,
			String gradeCode) {
		
		return eccAttenceGateGradeDao.findByInOutAndCode(unitId,gradeCode);
	}

	@Override
	public List<EccAttenceGateGrade> findInOutByAll() {
		return eccAttenceGateGradeDao.findInOutByAll();
	}

	@Override
	public List<EccAttenceGateGrade> findByClassifyAndPeriodId(
			Integer classify, String periodId) {
		return eccAttenceGateGradeDao.findByClassifyAndPeriodId(
				 classify, periodId);
	}

	@Override
	public List<EccAttenceGateGrade> findByUnitIdAndClassifyAndGradeCodes(String unitId, Integer classify, String[] gradeCodes) {
		if(ArrayUtils.isEmpty(gradeCodes)){
			return new ArrayList<EccAttenceGateGrade>();
		}
		return eccAttenceGateGradeDao.findByUnitIdAndClassifyAndGradeIn(unitId, classify, gradeCodes);
	}

	@Override
	public List<EccAttenceGateGrade> findByInOutAndClassify(String unitId,
			Integer classify) {
		return eccAttenceGateGradeDao.findByInOutAndClassify(unitId,classify);
	}

}
