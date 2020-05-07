package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccDormAttenceDao;
import net.zdsoft.eclasscard.data.dto.DormStuAttDto;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormGrade;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormPeriod;
import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeSet;
import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeUser;
import net.zdsoft.eclasscard.data.entity.EccDateInfo;
import net.zdsoft.eclasscard.data.entity.EccDormAttence;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccStuLeaveInfo;
import net.zdsoft.eclasscard.data.entity.EccStudormAttence;
import net.zdsoft.eclasscard.data.service.EccAttenceDormGradeService;
import net.zdsoft.eclasscard.data.service.EccAttenceDormPeriodService;
import net.zdsoft.eclasscard.data.service.EccAttenceNoticeSetService;
import net.zdsoft.eclasscard.data.service.EccAttenceNoticeUserService;
import net.zdsoft.eclasscard.data.service.EccDateInfoService;
import net.zdsoft.eclasscard.data.service.EccDingMsgPushService;
import net.zdsoft.eclasscard.data.service.EccDormAttenceService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccStuLeaveInfoService;
import net.zdsoft.eclasscard.data.service.EccStudormAttenceService;
import net.zdsoft.eclasscard.data.service.EccTaskService;
import net.zdsoft.eclasscard.data.task.DormAttanceTask;
import net.zdsoft.eclasscard.data.task.EccTask;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("eccDormAttenceService")
public class EccDormAttenceServiceImpl extends BaseServiceImpl<EccDormAttence, String>  implements EccDormAttenceService {

	@Autowired
	private EccDormAttenceDao eccDormAttenceDao;
	@Autowired
	private EccAttenceDormPeriodService eccAttenceDormPeriodService;
	@Autowired
	private EccTaskService eccTaskService;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private EccAttenceDormGradeService eccAttenceDormGradeService;
	@Autowired
	private EccStudormAttenceService eccStudormAttenceService;
	@Autowired
    private GradeRemoteService gradeRemoteService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
	private EccAttenceNoticeSetService eccAttenceNoticeSetService;
	@Autowired
	private EccAttenceNoticeUserService eccAttenceNoticeUserService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private EccDateInfoService eccDateInfoService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private EccStuLeaveInfoService eccStuLeaveInfoService;
	@Autowired
	private EccDingMsgPushService eccDingMsgPushService;
	private static OpenApiOfficeService openApiOfficeService;

    public OpenApiOfficeService getOpenApiOfficeService() {
        if (openApiOfficeService == null) {
            openApiOfficeService = Evn.getBean("openApiOfficeService");
            if(openApiOfficeService == null){
				System.out.println("openApiOfficeService为null，需开启dubbo服务");
			}
        }
        return openApiOfficeService;
    }
	@Override
	protected BaseJpaRepositoryDao<EccDormAttence, String> getJpaDao() {
		return eccDormAttenceDao;
	}

	@Override
	protected Class<EccDormAttence> getEntityClass() {
		return EccDormAttence.class;
	}

	@Override
	public List<EccDormAttence> findDormAttenceByPeriodId(String periodId, Date date) {
		
		return eccDormAttenceDao.findDormAttenceByPeriodId(periodId,DateUtils.date2StringByDay(date));
	}

	@Override
	public void addDormAttenceQueue(){
		List<EccAttenceDormPeriod> dormPeriods = eccAttenceDormPeriodService.findAll();
		Calendar time = Calendar.getInstance();
		time.set(Calendar.HOUR, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		List<EccDormAttence> dormAttences = eccDormAttenceDao.findStatByNoOverBefToday(time.getTime());
		//今天之前没结束的，结束掉
		if(CollectionUtils.isNotEmpty(dormAttences)){
			for(EccDormAttence attence:dormAttences){
				attence.setOver(true);
			}
			saveAll(dormAttences.toArray(new EccDormAttence[dormAttences.size()]));
		}
		//加入定时队列
		for(EccAttenceDormPeriod eccAttenceDormPeriod:dormPeriods){
			Calendar calendarEnd = Calendar.getInstance();
			String beginTime = eccAttenceDormPeriod.getBeginTime();
			String endTime = eccAttenceDormPeriod.getEndTime();
			String nowTime = DateUtils.date2String(new Date(), "HH:mm");
			String nowDate = DateUtils.date2String(new Date(), "yyyy-MM-dd");
			if(EccUtils.addTimeStr(nowTime).compareTo(EccUtils.addTimeStr(endTime))<0){
				// 初始化日期
				beginTime = nowDate+" "+EccUtils.addTimeStr(beginTime)+":00";
				endTime = nowDate+" "+EccUtils.addTimeStr(endTime)+":00";
				calendarEnd.setTime(DateUtils.string2DateTime(endTime));
				calendarEnd.add(Calendar.MINUTE, 1);
				endTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
				if(EccUtils.addTimeStr(nowTime).compareTo(EccUtils.addTimeStr(eccAttenceDormPeriod.getBeginTime()))>=0){
					beginTime = DateUtils.date2StringBySecond(DateUtils.addMinute(new Date(), 1));
				}
				EccTask dormAttanceTaskStart = new DormAttanceTask(eccAttenceDormPeriod.getId(),false);
				EccTask dormAttanceTaskEnd = new DormAttanceTask(eccAttenceDormPeriod.getId(),true);
				eccTaskService.addEccTaskBandE(dormAttanceTaskStart, dormAttanceTaskEnd, beginTime, endTime, eccAttenceDormPeriod,null, false);
			}
		}
	}
	
	@Override
	public void dormTaskRun(String periodId, boolean isEnd) {
		EccAttenceDormPeriod dormPeriod = eccAttenceDormPeriodService.findOne(periodId);
		if(dormPeriod==null){
			return;
		}
		String unitId = dormPeriod.getUnitId();
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
		Calendar calendar = Calendar.getInstance();
		if(semester==null || calendar.getTime().after(semester.getSemesterEnd()) || calendar.getTime().before(semester.getSemesterBegin())){
			return;
		}
		DateInfo dateInfo1 = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
		List<EccDateInfo> eccDateInfos1 = eccDateInfoService.getDateSchoolId(unitId, DateUtils.date2StringByDay(calendar.getTime()));
		calendar.add(Calendar.DATE, 1);
		DateInfo dateInfo2 = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
		List<EccDateInfo> eccDateInfos2 = eccDateInfoService.getDateSchoolId(unitId, DateUtils.date2StringByDay(calendar.getTime()));
		List<EccAttenceDormGrade> dormGrades = eccAttenceDormGradeService.findListBy("periodId", periodId);
		List<EccAttenceDormGrade> dormGrades2 = Lists.newArrayList();
		
		Map<String,Grade> attGradeCodeMap = Maps.newHashMap();
		Map<String,Grade> attGradeCodesNextMap = Maps.newHashMap();
		if(EccConstants.DORM_ATTENCE_PERIOD_TYPE1==dormPeriod.getType()){//上课日
			boolean allGradeNeed = false;
			boolean allGradeNeedNext = false;
			if(dateInfo1!=null && "Y".equals(dateInfo1.getIsFeast())){//节假日看是否补课
				Set<String> gradeIds = EntityUtils.getSet(eccDateInfos1, "gradeId");
				if(gradeIds.size()<1){
					return;
				}
				String jsonStr = gradeRemoteService.findListByIds(gradeIds.toArray(new String[gradeIds.size()]));
				List<Grade> grades = SUtils.dt(jsonStr,new TR<List<Grade>>() {});
				attGradeCodeMap = EntityUtils.getMap(grades, "gradeCode");//今日补课的年级
			}else{
				//今日全部年级上课
				allGradeNeed = true;
			}
			if(dormPeriod.isNextDayAttence()){
				if(dateInfo2!=null &&"Y".equals(dateInfo2.getIsFeast())){//次日节假日
					//要上课的，要考勤
					Set<String> gradeIds = EntityUtils.getSet(eccDateInfos2, "gradeId");
					String jsonStr = gradeRemoteService.findListByIds(gradeIds.toArray(new String[gradeIds.size()]));
					List<Grade> grades = SUtils.dt(jsonStr,new TR<List<Grade>>() {});
					attGradeCodesNextMap = EntityUtils.getMap(grades, "gradeCode");//明日补课的年级，今日要考勤
				}else{
					//明日上课，今日全部年级要考勤
					allGradeNeedNext = true;
				}
			}else{
				allGradeNeedNext = true;
			}
			if(allGradeNeed&&!allGradeNeedNext){
				for(EccAttenceDormGrade dormGrade:dormGrades){
					if(attGradeCodesNextMap.containsKey(dormGrade.getGrade())){
						dormGrades2.add(dormGrade);
					}
				}
				dormGrades = dormGrades2;
			}else if(!allGradeNeed&&allGradeNeedNext){
				for(EccAttenceDormGrade dormGrade:dormGrades){
					if(attGradeCodeMap.containsKey(dormGrade.getGrade())){
						dormGrades2.add(dormGrade);
					}
				}
				dormGrades = dormGrades2;
			}else if(!allGradeNeed&&!allGradeNeedNext){
				for(EccAttenceDormGrade dormGrade:dormGrades){
					if(attGradeCodeMap.containsKey(dormGrade.getGrade()) && attGradeCodesNextMap.containsKey(dormGrade.getGrade())){
						dormGrades2.add(dormGrade);
					}
				}
				dormGrades = dormGrades2;
			}
		}else{//收假日
			boolean toDayFeast = false;
			boolean nextDayAttendClass = false;
			if(dateInfo1!=null && "Y".equals(dateInfo1.getIsFeast())){
				Set<String> gradeIds = EntityUtils.getSet(eccDateInfos1, "gradeId");
				if(gradeIds.size()<1){
					toDayFeast = true;//今日全部年级休息
				}else{
					String jsonStr = gradeRemoteService.findListByIds(gradeIds.toArray(new String[gradeIds.size()]));
					List<Grade> grades = SUtils.dt(jsonStr,new TR<List<Grade>>() {});
					attGradeCodeMap = EntityUtils.getMap(grades, "gradeCode");//今日补课的年级
				}
				if(dateInfo2!=null && "Y".equals(dateInfo2.getIsFeast())){//次日节假日
					//要上课的，要考勤
					Set<String> gradeIds2 = EntityUtils.getSet(eccDateInfos2, "gradeId");
					String jsonStr2 = gradeRemoteService.findListByIds(gradeIds2.toArray(new String[gradeIds2.size()]));
					List<Grade> grades2 = SUtils.dt(jsonStr2,new TR<List<Grade>>() {});
					attGradeCodesNextMap = EntityUtils.getMap(grades2, "gradeCode");//明日补课的年级，
				}else{
					nextDayAttendClass = true;//明日全部年级上课
				}
			}else{
				return;
			}
			if(toDayFeast && !nextDayAttendClass){//今日休息，明日补课年级的为收假日
				for(EccAttenceDormGrade dormGrade:dormGrades){
					if(attGradeCodesNextMap.containsKey(dormGrade.getGrade())){
						dormGrades2.add(dormGrade);
					}
				}
				dormGrades = dormGrades2;
			}else if(!toDayFeast && nextDayAttendClass){//今日补课的移除
				for(EccAttenceDormGrade dormGrade:dormGrades){
					if(attGradeCodeMap.containsKey(dormGrade.getGrade())){
						dormGrades2.add(dormGrade);
					}
				}
				dormGrades.removeAll(dormGrades2);
			}else if(!toDayFeast && !nextDayAttendClass){//今日不补课，明日补课的
				for(EccAttenceDormGrade dormGrade:dormGrades){
					if(attGradeCodeMap.containsKey(dormGrade.getGrade())){
						dormGrades2.add(dormGrade);
					}
				}
				dormGrades.removeAll(dormGrades2);
				for(EccAttenceDormGrade dormGrade:dormGrades){
					if(attGradeCodesNextMap.containsKey(dormGrade.getGrade())){
						dormGrades2.add(dormGrade);
					}
				}
				dormGrades = dormGrades2;
			}
			
		}
		
		
		if(CollectionUtils.isNotEmpty(dormGrades)){
			if(isEnd){
				List<EccDormAttence> dormAttences = findDormAttenceByPeriodId(periodId, new Date());
				if(CollectionUtils.isEmpty(dormAttences)){
					return;
				}
				Set<String> dormAttenceIds = Sets.newHashSet();
				for(EccDormAttence attence:dormAttences){
					dormAttenceIds.add(attence.getId());
					attence.setOver(true);
				}
				saveAll(dormAttences.toArray(new EccDormAttence[dormAttences.size()]));
				// 单位下学生请假列表
				Set<String> studentIds = Sets.newHashSet();
				Map<String,JSONArray> map1 = Maps.newHashMap();
				Map<String,JSONArray> map2 = Maps.newHashMap();
				if(getOpenApiOfficeService()!=null){
					String jsonStr1 = getOpenApiOfficeService().getHwStuLeavesByUnitId(unitId, null, "1", null, new Date());
					String jsonStr2 = getOpenApiOfficeService().getHwStuLeavesByUnitId(unitId, null, "4", null, new Date());
//					String jsonStr2 = getOpenApiOfficeService().getHwStuLeavesByUnitId(unitId, null, "2", null, new Date());
					JSONArray strings1 = EccUtils.getResultArray(jsonStr1, "studentIds");
					JSONArray strings2 = EccUtils.getResultArray(jsonStr2, "studentIds");
					String str1 = EccUtils.getResultStr(jsonStr1, "map");
					String str2 = EccUtils.getResultStr(jsonStr2, "map");
					map1 = (Map)JSONObject.parseObject(str1);
					map2 = (Map)JSONObject.parseObject(str2);
					for (int i = 0; i < strings1.size(); i++) {
						studentIds.add(strings1.get(i).toString());
					}
					for (int i = 0; i < strings2.size(); i++) {
						studentIds.add(strings2.get(i).toString());
					}
				}
				
				//获得的学生请假信息studentId_leaveId
				Set<String> leaveIdsSet = Sets.newHashSet();
				Set<String> stuLeaveSet = Sets.newHashSet();
				for (Map.Entry<String, JSONArray> entry : map1.entrySet()) {
					JSONArray arrays1 = entry.getValue();
					for (int i=0;i<arrays1.size();i++) {
						stuLeaveSet.add(entry.getKey()+"_"+arrays1.getString(i));
						leaveIdsSet.add(arrays1.getString(i));
					}
				}
				for (Map.Entry<String, JSONArray> entry : map2.entrySet()) {
					JSONArray arrays2 = entry.getValue();
					for (int i=0;i<arrays2.size();i++) {
						stuLeaveSet.add(entry.getKey()+"_"+arrays2.getString(i));
						leaveIdsSet.add(arrays2.getString(i));
					}
				}
				Map<String,String> stuAttIdMap = Maps.newHashMap();
				if(dormAttenceIds.size()>0 && studentIds.size()>0){
					List<EccStudormAttence> studormAttences = eccStudormAttenceService.findListLeaveStudent(dormAttenceIds.toArray(new String[dormAttenceIds.size()]),studentIds.toArray(new String[studentIds.size()]));
					if(CollectionUtils.isNotEmpty(studormAttences)){
						for(EccStudormAttence attence:studormAttences){
							attence.setStatus(EccConstants.DORM_ATTENCE_STATUS2);
							stuAttIdMap.put(attence.getStudentId(), attence.getId());
						}
						eccStudormAttenceService.saveAll(studormAttences.toArray(new EccStudormAttence[studormAttences.size()]));
					}
					Set<String> oldStuLeaveSet = Sets.newHashSet();
					if (CollectionUtils.isNotEmpty(leaveIdsSet)) {
						List<EccStuLeaveInfo> eccStuLeaveInfos = eccStuLeaveInfoService.findByLeaveIdIn(leaveIdsSet.toArray(new String[leaveIdsSet.size()]));
						for (EccStuLeaveInfo eccStuLeaveInfo : eccStuLeaveInfos) {
							oldStuLeaveSet.add(eccStuLeaveInfo.getStudentId()+"_"+eccStuLeaveInfo.getLeaveId());
						}
					}
					List<EccStuLeaveInfo> leaveInfos = Lists.newArrayList();
					EccStuLeaveInfo leaveInfo = null;
					
					String[] stuLeave = null;
					for (String str : stuLeaveSet) {
						stuLeave = str.split("_");
						if (!stuAttIdMap.containsKey(stuLeave[0])) {
							continue;
						}
						leaveInfo = new EccStuLeaveInfo();
						leaveInfo.setId(UuidUtils.generateUuid());
						leaveInfo.setStudentId(stuLeave[0]);
						leaveInfo.setLeaveId(stuLeave[1]);
						leaveInfo.setStuDormAttId(stuAttIdMap.get(stuLeave[0]));
						if (oldStuLeaveSet.contains(str)) {
							leaveInfo.setIsFirst(EccConstants.ECC_IS_FIRST_0);
						} else {
							leaveInfo.setIsFirst(EccConstants.ECC_IS_FIRST_1);
						}
						leaveInfos.add(leaveInfo);
					}
					eccStuLeaveInfoService.saveAll(leaveInfos.toArray(new EccStuLeaveInfo[leaveInfos.size()]));
				}
				
				
				List<EccAttenceNoticeSet> noticeSets = eccAttenceNoticeSetService.findListBy("unitId", unitId);
				EccAttenceNoticeSet noticeSet = new EccAttenceNoticeSet();
				for(EccAttenceNoticeSet attenceNoticeSet:noticeSets){
					if(EccConstants.DORM_ATTENCE_SET_TYPE2.equals(attenceNoticeSet.getType())){
						noticeSet = attenceNoticeSet;
					}
				}
				Set<String> buildIds = EntityUtils.getSet(dormAttences, "placeId");
				List<EccInfo> eccInfos = eccInfoService.findListByIn("placeId", buildIds.toArray());
				Set<String> sids = EntityUtils.getSet(eccInfos, EccInfo::getId);
				if(sids.size()>0){
					EccNeedServiceUtils.postDormClock(sids);
				}
				//2.结束时推送消息
				if(noticeSet.isSend()){
					List<EccAttenceNoticeUser> noticeUsers = eccAttenceNoticeUserService.findListBy(new String[]{"type","unitId"}, new String[]{EccConstants.DORM_ATTENCE_SET_TYPE2,unitId});
					Set<String> sendUserIds = EntityUtils.getSet(noticeUsers, "userId");
					String periodStr = dormPeriod.getBeginTime()+"-"+dormPeriod.getEndTime();
					eccDingMsgPushService.dormAttPushDingMsg(dormAttences, unitId, sendUserIds,noticeSet,periodStr,studentIds);
				}
			}else{
				Set<String> gradeCodes = EntityUtils.getSet(dormGrades, "grade");
				List<Grade> grades = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(dormGrades.get(0).getUnitId(), gradeCodes.toArray(new String[gradeCodes.size()])),new TR<List<Grade>>() {});
				Set<String> gradeIds = EntityUtils.getSet(grades, "id");
				List<Clazz> clazzs = SUtils.dt(classRemoteService.findByInGradeIds(gradeIds.toArray(new String[gradeIds.size()])),new TR<List<Clazz>>() {});
				Set<String> classIds = EntityUtils.getSet(clazzs, "id");
				// 根据classIds获取住校生List,遍历生成寝室考勤数据
				List<String[]> reList = Lists.newArrayList();
				if(classIds.size()>0){
					reList = SUtils.dt(stuworkRemoteService.findStuRoomByClassIds(unitId, semester.getAcadyear(), semester.getSemester()+"", classIds.toArray(new String[classIds.size()])),new TR<List<String[]>>() {});
				}
				List<DormStuAttDto> stuAttDtos = Lists.newArrayList();
				for(String[] strings:reList){
					DormStuAttDto attDto=new DormStuAttDto(); 
					attDto.setStudentId(strings[0]);
					attDto.setClassId(strings[1]);
					attDto.setBuildingId(strings[2]); 
					stuAttDtos.add(attDto);
				}
				Set<String> buildIds = EntityUtils.getSet(stuAttDtos, "buildingId");
				buildIds.remove(null);
				if(!(buildIds.size()>0)){
					return;
				}
				List<EccDormAttence> eccDormAttences = Lists.newArrayList();
				List<EccStudormAttence> studormAttences = Lists.newArrayList();
				Map<String,String> placeAttMap = Maps.newHashMap();
				Map<String,EccDormAttence> placeAttInitMap = Maps.newHashMap();
				List<EccDormAttence>  dormAttenceList=findListByEccNotInit(unitId,periodId, DateUtils.date2String
						(new Date(),"yyyy-MM-dd"),buildIds.toArray(new String[0]));
				placeAttInitMap = EntityUtils.getMap(dormAttenceList, EccDormAttence::getPlaceId);
//				Set<String> dormAttIds = EntityUtils.getSet(dormAttenceList, "id");
				for(String placeId:buildIds){//生成寝室楼考勤表
					if(!placeAttInitMap.containsKey(placeId)){
						EccDormAttence dormAttence = new EccDormAttence();
						dormAttence.setId(UuidUtils.generateUuid());
						dormAttence.setUnitId(unitId);
						dormAttence.setPeriodId(periodId);
						dormAttence.setPlaceId(placeId);
						dormAttence.setOver(false);
						dormAttence.setClockDate(new Date());
						placeAttMap.put(placeId, dormAttence.getId());
						eccDormAttences.add(dormAttence);
					}
				}
				// 单位下当天申请通校的学生
//				Set<String> studentIds = Sets.newHashSet();
//				if(getOpenApiOfficeService()!=null){
//					String jsonStr1 = getOpenApiOfficeService().getHwStuLeavesByUnitId(unitId, null, "3", "2", new Date());
//					String jsonStr2 = getOpenApiOfficeService().getHwStuLeavesByUnitId(unitId, null, "4", null, new Date());
//					JSONArray strings1 = EccUtils.getResultArray(jsonStr1, "studentIds");
//					JSONArray strings2 = EccUtils.getResultArray(jsonStr2, "studentIds");
//					for (int i = 0; i < strings1.size(); i++) {
//						studentIds.add(strings1.get(i).toString());
//					}
//					for (int i = 0; i < strings2.size(); i++) {
//						studentIds.add(strings2.get(i).toString());
//					}
//				}
				for(DormStuAttDto attDto:stuAttDtos){//初始化寝室楼对应的要考勤的学生
					if(placeAttMap.containsKey(attDto.getBuildingId())&&StringUtils.isNotEmpty(placeAttMap.get(attDto.getBuildingId()))){
						EccStudormAttence studormAttence = new EccStudormAttence();
						studormAttence.setClassId(attDto.getClassId());
						if(placeAttMap.containsKey(attDto.getBuildingId())){
							studormAttence.setDormAttId(placeAttMap.get(attDto.getBuildingId()));
						}
						studormAttence.setId(UuidUtils.generateUuid());
						studormAttence.setStatus(EccConstants.DORM_ATTENCE_STATUS1);
						studormAttence.setStudentId(attDto.getStudentId());
						studormAttences.add(studormAttence);
					}
				}
				if(eccDormAttences.size()>0){
					saveAll(eccDormAttences.toArray(new EccDormAttence[eccDormAttences.size()]));
				}
				if(studormAttences.size()>0){
					eccStudormAttenceService.saveAll(studormAttences.toArray(new EccStudormAttence[studormAttences.size()]));
				}
//				if(dormAttIds.size()>0 && studentIds.size()>0){
//					List<EccStudormAttence> sdas = eccStudormAttenceService.findListLeaveStudent(dormAttIds.toArray(new String[dormAttIds.size()]),studentIds.toArray(new String[studentIds.size()]));
//					if(CollectionUtils.isNotEmpty(sdas)){
//						eccStudormAttenceService.deleteAll(sdas.toArray(new EccStudormAttence[sdas.size()]));
//					}
//				}
				List<EccInfo> eccInfos = eccInfoService.findListByIn("placeId", buildIds.toArray());
				Set<String> sids = EntityUtils.getSet(eccInfos, EccInfo::getId);
				if(sids.size()>0){
					EccNeedServiceUtils.postDormClock(sids);
				}
			}
		}
		
	}
	@Override
	public List<EccDormAttence> findListByEccNotInit(String unitId,
			String periodId, String date, String[] placeIds) {
		return eccDormAttenceDao.findListByEccNotInit(unitId,periodId,date,placeIds);
	}


	@Override
	public List<EccDormAttence> findListByPlaceIdNotOver(String placeId,String unitId) {
		List<EccDormAttence> dormAttences = Lists.newArrayList();
		List<EccAttenceDormPeriod> dormPeriods= eccAttenceDormPeriodService.findInNowTime(unitId);
		if(CollectionUtils.isNotEmpty(dormPeriods)){
			Set<String> periodIds = EntityUtils.getSet(dormPeriods, "id");
			dormAttences = eccDormAttenceDao.findListByPlaceIdNotOver(placeId,DateUtils.date2StringByDay(new Date()),periodIds.toArray(new String[periodIds.size()]));
		}
		
//		Set<String> dormAttIds = EntityUtils.getSet(dormAttences, "id");
//		if(dormAttIds.size()>0){
//			List<EccStudormAttence> studormAttences = eccStudormAttenceService.findByInAttIdInit(dormAttIds.toArray(new String[dormAttIds.size()]));
//			if(CollectionUtils.isNotEmpty(studormAttences)){
//				for(EccStudormAttence studormAttence:studormAttences){
//					studormAttence.setStatus(EccConstants.DORM_ATTENCE_STATUS1);
//				}
//				eccStudormAttenceService.saveAll(studormAttences.toArray(new EccStudormAttence[studormAttences.size()]));
//			}
//		}
		return dormAttences;
	}
	
	@Override
	public Map<String, Integer> findDormAttType(String unitId,String[] gradeIds, Date date) {
		Map<String, Integer> attTypeMap = Maps.newHashMap();
		if(gradeIds==null){
			return attTypeMap;
		}
		for(String gradeId:gradeIds){
			attTypeMap.put(gradeId, 0);
		}
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		DateUtils.date2String(semester.getSemesterBegin());
		DateUtils.date2String(semester.getSemesterEnd());
		if(semester==null || calendar.getTime().after(semester.getSemesterEnd()) || calendar.getTime().before(semester.getSemesterBegin())){
			return attTypeMap;
		}
		DateInfo dateInfo1 = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
		List<EccDateInfo> eccDateInfos1 = eccDateInfoService.getDateSchoolId(unitId, DateUtils.date2StringByDay(calendar.getTime()));
		calendar.add(Calendar.DATE, 1);
		DateInfo dateInfo2 = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
		if(dateInfo1==null||dateInfo2==null){
			return attTypeMap;
		}
		List<EccDateInfo> eccDateInfos2 = eccDateInfoService.getDateSchoolId(unitId, DateUtils.date2StringByDay(calendar.getTime()));
		if("Y".equals(dateInfo1.getIsFeast())){//节假日看是否补课
			Set<String> gIds = EntityUtils.getSet(eccDateInfos1, "gradeId");
			Set<String> gIds2 = EntityUtils.getSet(eccDateInfos2, "gradeId");
			for(String gradeId:gradeIds){
				if(gIds.contains(gradeId)){
					attTypeMap.put(gradeId, EccConstants.DORM_ATTENCE_PERIOD_TYPE1);
				}else{
					if("N".equals(dateInfo2.getIsFeast())||gIds2.contains(gradeId)){//次日节假日
						attTypeMap.put(gradeId, EccConstants.DORM_ATTENCE_PERIOD_TYPE2);
					}
				}
			}
		}else{
			//今日全部年级上课
			for(String gradeId:gradeIds){
				attTypeMap.put(gradeId, EccConstants.DORM_ATTENCE_PERIOD_TYPE1);
			}
		}
		
		return attTypeMap;
	}


	@Override
	public List<EccDormAttence> findListByCon(String unitId,String periodId,String date,String buildingId,String[] periodIds){
		if(periodIds.length>0 && StringUtils.isNotBlank(date) && StringUtils.isNotBlank(buildingId)){
			if(StringUtils.isNotBlank(periodId)){
				return eccDormAttenceDao.findListByCon(unitId,periodId, DateUtils.string2Date(date,"yyyy-MM-dd"),buildingId, periodIds);
			}else{
				return eccDormAttenceDao.findListByCon(unitId,DateUtils.string2Date(date,"yyyy-MM-dd"),buildingId, periodIds);
			}
		}
		return new ArrayList<EccDormAttence>();
	}
	@Override
	public List<EccDormAttence> findStatByCon(String unitId,String startTime,String endTime){
		if(StringUtils.isNotBlank(startTime)){
			return eccDormAttenceDao.findStatByCon(unitId,DateUtils.string2Date(startTime,"yyyy-MM-dd"),DateUtils.string2Date(endTime,"yyyy-MM-dd"));
		}else{
			return eccDormAttenceDao.findStatByCon(unitId,DateUtils.string2Date(endTime,"yyyy-MM-dd"));
		}
	}
}
