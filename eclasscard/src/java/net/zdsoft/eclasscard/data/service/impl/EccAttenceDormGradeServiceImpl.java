package net.zdsoft.eclasscard.data.service.impl;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccAttenceDormGradeDao;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormGrade;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormPeriod;
import net.zdsoft.eclasscard.data.service.EccAttenceDormGradeService;
import net.zdsoft.eclasscard.data.service.EccAttenceDormPeriodService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
@Service("eccAttenceDormGradeService")
public class EccAttenceDormGradeServiceImpl extends BaseServiceImpl<EccAttenceDormGrade, String> implements EccAttenceDormGradeService{

	@Autowired
	private EccAttenceDormGradeDao eccAttenceDormGradeDao;
	@Autowired
	private EccAttenceDormPeriodService eccAttenceDormPeriodService;
	@Override
	protected BaseJpaRepositoryDao<EccAttenceDormGrade, String> getJpaDao() {
		return eccAttenceDormGradeDao;
	}

	@Override
	protected Class<EccAttenceDormGrade> getEntityClass() {
		return EccAttenceDormGrade.class;
	}

	@Override
	public Map<String,List<EccAttenceDormGrade>>  findByPeriodIdMap(String[] ids) {
		Map<String,List<EccAttenceDormGrade>> dormGradeListMap  = Maps.newHashMap();
		List<EccAttenceDormGrade> dormGrades = findListByIn("periodId", ids);
		for(EccAttenceDormGrade dormGrade:dormGrades){
			List<EccAttenceDormGrade> dgs = dormGradeListMap.get(dormGrade.getPeriodId());
			if(dgs==null){
				dgs = Lists.newArrayList();
			}
			dgs.add(dormGrade);
			dormGradeListMap.put(dormGrade.getPeriodId(), dgs);
		}
		return dormGradeListMap;
	}

	@Override
	public Map<String, List<EccAttenceDormGrade>> findByGradeMap(String unitId,Integer type,
			String[] gradeCodes) {
		if(gradeCodes==null){
			return Maps.newHashMap();
		}
		Map<String,List<EccAttenceDormGrade>> dormGradeListMap  = Maps.newHashMap();
//		List<EccAttenceDormGrade> dormGrades = findByIn("grade", gradeCodes);
		List<EccAttenceDormGrade> dormGrades = eccAttenceDormGradeDao.findListByUnitIdInGrade(unitId,type,gradeCodes);
		for(EccAttenceDormGrade dormGrade:dormGrades){
			List<EccAttenceDormGrade> dgs = dormGradeListMap.get(dormGrade.getPeriodId());
			if(dgs==null){
				dgs = Lists.newArrayList();
			}
			dgs.add(dormGrade);
			dormGradeListMap.put(dormGrade.getGrade(), dgs);
		}
		return dormGradeListMap;
	}

	@Override
	public void deleteByUnitIdPeriodId(String unitId, String periodId) {
		eccAttenceDormGradeDao.deleteByUnitIdPeriodId(unitId,periodId);
	}

	@Override
	public boolean findByBettwenTime(String unitId, Integer type,
			String gradeCode,String time,boolean nextHoliday) {
		List<EccAttenceDormGrade> dormGrades = eccAttenceDormGradeDao.findListByUnitIdInGrade(unitId,type,new String[]{gradeCode});
		for(EccAttenceDormGrade dormGrade:dormGrades){
			if(dormGrade.getBeginTime().compareTo(time)<=0 && 
					dormGrade.getEndTime().compareTo(time)>=0){
				if(!nextHoliday)
					return true;
				EccAttenceDormPeriod period = eccAttenceDormPeriodService.findOne(dormGrade.getPeriodId());
				if(nextHoliday && (period!=null&&!period.isNextDayAttence()))
					return true;
			}
		}
		return false;
	}
	@Override
	public List<EccAttenceDormGrade> findListByCon(String unitId,Integer[] types,String[] gradeCodes){
		List<EccAttenceDormGrade>  dormGList=null;
		if(types!=null && types.length>0){
			if(gradeCodes!=null && gradeCodes.length>0){
				dormGList=eccAttenceDormGradeDao.findListByUnitIdTypeGrade(unitId, types, gradeCodes);
			}else{
				dormGList=eccAttenceDormGradeDao.findListByUnitIdType(unitId, types);
			}
		}
		return dormGList;
	}
}
