package net.zdsoft.eclasscard.remote.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.eclasscard.data.entity.EccClassAttence;
import net.zdsoft.eclasscard.data.service.EccClassAttenceService;
import net.zdsoft.eclasscard.data.service.EccDateInfoService;
import net.zdsoft.eclasscard.data.service.EccExamTimeService;
import net.zdsoft.eclasscard.data.service.EccStuclzAttenceService;
import net.zdsoft.eclasscard.remote.service.EclasscardRemoteService;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Sets;

@Service("eclasscardRemoteService")
public class EclasscardRemoteServiceImpl implements EclasscardRemoteService{

	@Autowired
	private EccClassAttenceService eccClassAttenceService;
	@Autowired
	private EccStuclzAttenceService eccStuclzAttenceService;
	@Autowired
	private EccDateInfoService eccDateInfoService;
	@Autowired
	private EccExamTimeService eccExamTimeService;
	@Override
	public void attancePerMinute(Map<String, String> unitIdMap) {
		//do nothing
		//各单位当前时间考勤班级
		String date = DateUtils.date2StringByDay(new Date());
		String time = DateUtils.date2String(DateUtils.addMinute(new Date(), 9),"HH:mm");
		String nowTime = DateUtils.date2String(new Date(),"HH:mm");
		for(String unitId:unitIdMap.keySet()){
			Set<String> attIds = Sets.newHashSet();
			boolean isEnd = false;
			List<EccClassAttence> attences = eccClassAttenceService.findByUnitIdThisDate(date, unitId);
			for(EccClassAttence attence:attences){
				if(time.compareTo(attence.getBeginTime())>0&&nowTime.compareTo(attence.getEndTime())<0){
					attIds.add(attence.getId());
					if(nowTime.compareTo(attence.getBeginTime())>0){
						isEnd = true;
					}
				}
			}
			if(attIds.size()>0){
				eccClassAttenceService.classRingTaskRun(unitId, attIds, isEnd);
			}
		}
	}
	@Override
	public List<Date> getInfoDateList(String unitId,String gradeId, Date beginDate, Date endDate) {
		return eccDateInfoService.getInfoDateList(unitId,gradeId,beginDate,endDate);
	}
	
	@Override
	public String syncExamTimeToEClassCard(JSONArray examTimeArray) {
		return eccExamTimeService.saveOrUpdate(examTimeArray);
	}
	
	@Override
	public String syncDeleteExamTimeTo(String[] subjectInfoId) {
		return eccExamTimeService.deleteBySubjectInfoIds(subjectInfoId);
	}
	@Override
	public int stuClassAttance(String unitId,String studentId, String placeId,
			Integer sectionNumber, Date clockTime) {
		if(StringUtils.isBlank(unitId)||StringUtils.isBlank(studentId)||StringUtils.isBlank(placeId)||sectionNumber == null){
			return -1;
		}
		EccClassAttence attence = eccClassAttenceService.findbyPlaceIdSecNumToDay(unitId,placeId,sectionNumber,studentId);
		if(attence == null){
			return 0;
		}
		boolean flag = eccClassAttenceService.saveStuAttSate(unitId,attence,studentId,clockTime);
		if(flag){
			return 1;
		}else{
			return -1;
		}
	}


}
