package net.zdsoft.eclasscard.data.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccCacheConstants;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccInOutAttanceDao;
import net.zdsoft.eclasscard.data.entity.EccAttenceGateGrade;
import net.zdsoft.eclasscard.data.entity.EccInOutAttance;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccTaskEntity;
import net.zdsoft.eclasscard.data.service.EccAttenceGateGradeService;
import net.zdsoft.eclasscard.data.service.EccCacheService;
import net.zdsoft.eclasscard.data.service.EccInOutAttanceService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccTaskService;
import net.zdsoft.eclasscard.data.task.EccTask;
import net.zdsoft.eclasscard.data.task.InOutAttanceTask;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

@Service("eccInOutAttanceService")
public class EccInOutAttanceServiceImpl extends BaseServiceImpl<EccInOutAttance, String> implements EccInOutAttanceService{

	@Autowired
	private EccInOutAttanceDao eccInOutAttanceDao;
	@Autowired
	private EccAttenceGateGradeService eccAttenceGateGradeService;
	@Autowired
	private EccTaskService eccTaskService;
	@Autowired
	private EccCacheService eccCacheService;
	@Autowired
    private GradeRemoteService gradeRemoteService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
	private EccInfoService eccInfoService;
	
	@Override
	protected BaseJpaRepositoryDao<EccInOutAttance, String> getJpaDao() {
		return eccInOutAttanceDao;
	}

	@Override
	protected Class<EccInOutAttance> getEntityClass() {
		return EccInOutAttance.class;
	}

	@Override
	public EccInOutAttance findByPeriodIdAndStudentIdToDay(String unitId, String periodId, String studentId,Date date) {
		return eccInOutAttanceDao.findByUnitIdAndPeriodIdAndStudentIdToDay(unitId, periodId, studentId,DateUtils.date2StringByDay(date));
	}

	@Override
	public List<EccInOutAttance> findByPeriodIdAndClassId(String unitId,
			String periodId, String classId) {
		return eccInOutAttanceDao.findByPeriodIdAndClassIdToday(unitId, periodId, classId,DateUtils.date2StringByDay(new Date()));
	}

	@Override
	public void addInOutAttenceQueue(String unitId) {
		List<EccAttenceGateGrade> inoutPeriods = Lists.newArrayList();
		if(StringUtils.isBlank(unitId)){
			inoutPeriods = eccAttenceGateGradeService.findInOutByAll();
		}else{
			inoutPeriods = eccAttenceGateGradeService.findByInOutAndClassify(unitId,EccConstants.ECC_CLASSIFY_2);
		}
		//加入定时队列
		Set<String> periodIds = Sets.newHashSet();
		for(EccAttenceGateGrade inout:inoutPeriods){
			if(periodIds.contains(inout.getPeriodId())){
				continue;
			}
			Calendar calendarEnd = Calendar.getInstance();
			String beginTime = inout.getBeginTime();
			String endTime = inout.getEndTime();
			String nowTime = DateUtils.date2String(new Date(), "HH:mm");
			String nowDate = DateUtils.date2String(new Date(), "yyyy-MM-dd");
			EccTaskEntity taskEntity = new EccTaskEntity() {
				@Override
				public String fetchCacheEntitName() {
					return "taskEntity";
				}
			};
			taskEntity.setId(inout.getPeriodId());
			taskEntity.setBeginTime(beginTime);
			taskEntity.setEndTime(endTime);
			if(EccUtils.addTimeStr(nowTime).compareTo(EccUtils.addTimeStr(endTime))<0){
				// 初始化日期
				beginTime = nowDate+" "+EccUtils.addTimeStr(beginTime)+":00";
				endTime = nowDate+" "+EccUtils.addTimeStr(endTime)+":00";
				calendarEnd.setTime(DateUtils.string2DateTime(endTime));
				calendarEnd.add(Calendar.MINUTE, 1);
				endTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
				if(EccUtils.addTimeStr(nowTime).compareTo(EccUtils.addTimeStr(inout.getBeginTime()))>=0){
					beginTime = DateUtils.date2StringBySecond(DateUtils.addMinute(new Date(), 1));
				}
				EccTask dormAttanceTaskStart = new InOutAttanceTask(inout.getPeriodId(),inout.getUnitId(),false);
				EccTask dormAttanceTaskEnd = new InOutAttanceTask(inout.getPeriodId(),inout.getUnitId(),true);
				
				eccTaskService.addEccTaskBandE(dormAttanceTaskStart, dormAttanceTaskEnd, beginTime, endTime, taskEntity,null, false);
			}
			periodIds.add(inout.getPeriodId());
		}
		Map<String, List<EccAttenceGateGrade>> unitGateTimeMap = inoutPeriods.stream().collect(Collectors.groupingBy(EccAttenceGateGrade::getUnitId));
		for(Map.Entry<String, List<EccAttenceGateGrade>> en:unitGateTimeMap.entrySet()){
			String schoolId = en.getKey();
			List<EccAttenceGateGrade> value = en.getValue();
			RedisUtils.setObject(EccCacheConstants.CACHE_SERVICE_CACHE_HEAD +EccCacheConstants.CLASS_ATT + schoolId,value,24*60*60);
		}
		
	}

	@Override
	public void InOutTaskRun(String periodId,String schoolId, boolean isEnd) {
		List<EccAttenceGateGrade> gateGrades = eccCacheService.getInOutCacheByPeroidId(periodId, schoolId);
		if(CollectionUtils.isEmpty(gateGrades)){
			return;
		}
		Set<String> gradeCodes = EntityUtils.getSet(gateGrades, EccAttenceGateGrade::getGrade);
		List<Grade> grades = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(schoolId, gradeCodes.toArray(new String[gradeCodes.size()])),new TR<List<Grade>>() {});
		Set<String> gradeIds = EntityUtils.getSet(grades, Grade::getId);
		if(CollectionUtils.isEmpty(gradeIds)){
			return;
		}
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findByInGradeIds(gradeIds.toArray(new String[gradeIds.size()])),new TR<List<Clazz>>() {});
		Set<String> classIds = EntityUtils.getSet(clazzs, Clazz::getId);
		if(CollectionUtils.isEmpty(classIds)){
			return;
		}
		List<EccInfo> infos = eccInfoService.findByClassIdIn(classIds.toArray(new String[0]));
		if(CollectionUtils.isEmpty(infos)){
			return;
		}
		if(isEnd){
			//跳回首页
			EccNeedServiceUtils.postClassClock(EntityUtils.getSet(infos, EccInfo::getId),1);
		}else{
			//跳到考勤页
			EccNeedServiceUtils.postClassClock(EntityUtils.getSet(infos, EccInfo::getId),3);
		}
	}

}
