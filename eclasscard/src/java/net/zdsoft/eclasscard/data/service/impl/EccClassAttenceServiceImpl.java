package net.zdsoft.eclasscard.data.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StusysSectionTimeSet;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.StusysSectionTimeSetRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccClassAttenceDao;
import net.zdsoft.eclasscard.data.dto.StuClockResultDto;
import net.zdsoft.eclasscard.data.dto.cache.ClassAttCacheDto;
import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeSet;
import net.zdsoft.eclasscard.data.entity.EccClassAttence;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccStuclzAttence;
import net.zdsoft.eclasscard.data.entity.EccTeaclzAttence;
import net.zdsoft.eclasscard.data.service.EccAttenceNoticeSetService;
import net.zdsoft.eclasscard.data.service.EccAttenceNoticeUserService;
import net.zdsoft.eclasscard.data.service.EccCacheService;
import net.zdsoft.eclasscard.data.service.EccClassAttenceService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccStuclzAttenceService;
import net.zdsoft.eclasscard.data.service.EccTaskService;
import net.zdsoft.eclasscard.data.service.EccTeaclzAttenceService;
import net.zdsoft.eclasscard.data.task.ClassAttanceTask;
import net.zdsoft.eclasscard.data.task.ClassRingAttanceTask;
import net.zdsoft.eclasscard.data.task.PushMsgTask;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.delayQueue.DelayItem;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.officework.remote.service.OfficeAttanceRemoteService;
import net.zdsoft.remote.openapi.service.OpenApiNewElectiveService;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("eccClassAttenceService")
public class EccClassAttenceServiceImpl extends BaseServiceImpl<EccClassAttence, String>  implements EccClassAttenceService {
	public static final String KEY = "eclasscard.class.attence.";
	@Autowired
	private EccClassAttenceDao eccClassAttenceDao;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private EccTaskService eccTaskService;
	@Autowired
	private StusysSectionTimeSetRemoteService stusysSectionTimeSetRemoteService;
	@Autowired
    private GradeRemoteService gradeRemoteService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
	private EccAttenceNoticeSetService eccAttenceNoticeSetService;
	@Autowired
	private EccAttenceNoticeUserService eccAttenceNoticeUserService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private EccStuclzAttenceService eccStuclzAttenceService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private EccTeaclzAttenceService eccTeaclzAttenceService;
	@Autowired
	private OfficeAttanceRemoteService officeAttanceRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private EccCacheService eccCacheService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	private static OpenApiNewElectiveService openApiNewElectiveService;
	public OpenApiNewElectiveService getOpenApiNewElectiveService() {
	        if (openApiNewElectiveService == null) {
	        	openApiNewElectiveService = Evn.getBean("openApiNewElectiveService");
	            if(openApiNewElectiveService == null){
					System.out.println("openApiNewElectiveService为null，需开启dubbo服务");
				}
	        }
	        return openApiNewElectiveService;
	    }
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
	protected BaseJpaRepositoryDao<EccClassAttence, String> getJpaDao() {
		return eccClassAttenceDao;
	}

	@Override
	protected Class<EccClassAttence> getEntityClass() {
		return EccClassAttence.class;
	}

	@Override
	public List<EccClassAttence>  findListByPeriodNotOver(String unitId,String periodInterval,int period,String date) {
		return eccClassAttenceDao.findListByPeriodNotOver(unitId,periodInterval,period,date);
	}

	@Override
	public void addClassAttenceQueue(boolean isMorn)  {
		List<DelayItem<?>> itemList = new ArrayList<DelayItem<?>>();
		List<DelayItem<?>> itemListRing = new ArrayList<DelayItem<?>>();
		List<EccInfo> eccInfos = eccInfoService.findAll();//系统下维护了班牌的多个单位
		//使用手环的单位
		Set<String> ringUnitIds = officeAttanceRemoteService.getAttUnitIds();
		Set<String> unitIds = EntityUtils.getSet(eccInfos, EccInfo::getUnitId);
		unitIds.addAll(ringUnitIds);
		for(String unitId:unitIds){
			Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
			if(semester != null){
				String sections = schoolRemoteService.findSectionsById(unitId);
	        	if(StringUtils.isBlank(sections)){
	        		continue;
	        	}
	        	String[] section = sections.split(",");
				List<StusysSectionTimeSet> stusysSectionTimeSetList = SUtils.dt(stusysSectionTimeSetRemoteService.findByAcadyearAndSemesterAndUnitId(semester.getAcadyear(), semester.getSemester(),unitId,section,true),StusysSectionTimeSet.class);
				for(StusysSectionTimeSet timeSet:stusysSectionTimeSetList){
					if(DateUtils.date2String(DateUtils.addMinute(new Date(), 10), "HH:mm").compareTo(timeSet.getBeginTime())<0){
						if(StringUtils.isNotBlank(timeSet.getBeginTime())){
							Calendar time = Calendar.getInstance();
							String today = DateUtils.date2StringByDay(time.getTime());
							Date date = DateUtils.string2Date(today+" "+timeSet.getBeginTime(), "yyyy-MM-dd HH:mm");
							time.setTime(date);
							time.add(Calendar.MINUTE, -10);
							ClassAttanceTask classAttanceTask = new ClassAttanceTask(unitId, timeSet.getSectionNumber(),timeSet.getSection(), false);
							try {
								DelayItem<ClassAttanceTask> caStart = new DelayItem<ClassAttanceTask>(unitId+timeSet.getSection()+timeSet.getSectionNumber(), DateUtils.date2StringBySecond(time.getTime()), classAttanceTask);
								itemList.add(caStart);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
					if(DateUtils.date2String(DateUtils.addMinute(new Date(), 5), "HH:mm").compareTo(timeSet.getEndTime())<0){
						if(StringUtils.isNotBlank(timeSet.getEndTime())){
							Calendar time = Calendar.getInstance();
							String today = DateUtils.date2StringByDay(time.getTime());
							Date date = DateUtils.string2Date(today+" "+timeSet.getEndTime(), "yyyy-MM-dd HH:mm");
							time.setTime(date);
							time.add(Calendar.MINUTE, -5);
							ClassAttanceTask classAttanceTask = new ClassAttanceTask(unitId, timeSet.getSectionNumber(), timeSet.getSection(),true);
							try {
								DelayItem<ClassAttanceTask> caEnd = new DelayItem<ClassAttanceTask>(unitId+timeSet.getSection()+timeSet.getSectionNumber(), DateUtils.date2StringBySecond(time.getTime()), classAttanceTask);
								itemList.add(caEnd);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
					if(ringUnitIds.contains(unitId)){//加入上课考勤去第三方数据
						if(DateUtils.date2String(DateUtils.addMinute(new Date(), 10), "HH:mm").compareTo(timeSet.getBeginTime())>0
								&&DateUtils.date2String(new Date(), "HH:mm").compareTo(timeSet.getBeginTime())<0){
							String today = DateUtils.date2StringByDay(new Date());
							List<EccClassAttence> attences = eccClassAttenceDao.findListBySecNotOver(timeSet.getSection(), timeSet.getSectionNumber(), today, unitId);
							Set<String> attIds = EntityUtils.getSet(attences, EccClassAttence::getId);
							ClassRingAttanceTask classRingAttanceTask = new ClassRingAttanceTask(unitId, attIds, false);
		        			Date date1 = DateUtils.string2Date(today+" "+timeSet.getBeginTime(), "yyyy-MM-dd HH:mm");
							try {
								DelayItem<ClassRingAttanceTask> craStart = new DelayItem<ClassRingAttanceTask>(unitId+timeSet.getSectionNumber()+"ring", DateUtils.date2StringBySecond(date1), classRingAttanceTask);
								itemListRing.add(craStart);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		if(itemListRing.size()>0){
			eccTaskService.addEccTaskList(itemListRing);
		}
		if(itemList.size()>0){
			eccTaskService.addEccTaskList(itemList);
		}
		//昨天没结束的结束掉
		List<EccClassAttence> classAttences = findListNotOver();
    	if(classAttences.size()>0){
    		for(EccClassAttence attence:classAttences){
    			attence.setOver(true);
    			attence.setModifyTime(new Date());
    		}
    		saveAll(classAttences.toArray(new EccClassAttence[classAttences.size()]));
    	}
    	if(isMorn){//不关机的设备，首页刷新
    		Set<String> sids = EntityUtils.getSet(eccInfos, EccInfo::getId);
    		EccNeedServiceUtils.postClassClock(sids, 1);
    	}
	}

	@Override
	public void classTaskRun(String unitId,Integer sectionNumber,String section,boolean isEnd){
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
		if(semester==null){
			return;
		}
		Calendar now = Calendar.getInstance();
		DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), now.getTime()), DateInfo.class);
		List<DelayItem<?>> itemListRing = new ArrayList<DelayItem<?>>();
		if(semester != null && dateInfo!=null){
			//使用手环的单位
			Set<String> ringUnitIds = officeAttanceRemoteService.getAttUnitIds();
        	int weekOfWorktime; //开学时间的周次
        	Integer dayOfWeek; // 星期一:0; 星期二:1;以此类推
        	int period = 0; // 节课
        	String periodInterval;//上午，下午，晚上
        	dayOfWeek = now.get(Calendar.DAY_OF_WEEK)-2;
        	if(dayOfWeek==-1){
        		dayOfWeek = 6;
        	}
        	weekOfWorktime = dateInfo.getWeek();
        	if(sectionNumber<=semester.getMornPeriods()){
				periodInterval = EccConstants.PERIOD_MORN;
				period = sectionNumber;
        	}else if(sectionNumber<=semester.getAmPeriods()){
        		periodInterval = EccConstants.PERIOD_AM;
        		period = sectionNumber-semester.getMornPeriods();
        	}else if(sectionNumber<=semester.getAmPeriods()+semester.getPmPeriods()){
        		periodInterval = EccConstants.PERIOD_PM;
        		period = sectionNumber-semester.getAmPeriods()-semester.getMornPeriods();
        	}else{
        		periodInterval = EccConstants.PERIOD_NIGHT;
        		period = sectionNumber-semester.getAmPeriods()-semester.getPmPeriods()-semester.getMornPeriods();
        	}
        	if(isEnd){
        		List<EccClassAttence> classAttences = Lists.newArrayList();
        		List<EccClassAttence> unitClassAttences = findListByPeriodNotOver(unitId,periodInterval,period,DateUtils.date2StringByDay(new Date()));
        		for(EccClassAttence attence:unitClassAttences){
        			if(section.equals(attence.getSection())){
        				classAttences.add(attence);
        			}
        		}
        		if(classAttences.size()>0){
        			List<EccInfo> eccInfos = eccInfoService.findListByUnitAndType(unitId, new String[]{EccConstants.ECC_MCODE_BPYT_1,EccConstants.ECC_MCODE_BPYT_2});
        			Set<String> sids = Sets.newHashSet();
        			Set<String> placeIds = Sets.newHashSet();
        			Set<String> classIds = Sets.newHashSet();
        			Set<String> classAttenceId = Sets.newHashSet();
        			for(EccClassAttence classAttence:classAttences){
        				classAttenceId.add(classAttence.getId());
        				if(EccConstants.CLASS_TYPE_NORMAL==classAttence.getClassType()){
        					classIds.add(classAttence.getClassId());
        				}else{
        					placeIds.add(classAttence.getPlaceId());
        				}
        				classAttence.setOver(true);
        				classAttence.setModifyTime(new Date());
        			}
        			for(EccInfo eccInfo:eccInfos){
        				if((StringUtils.isNotBlank(eccInfo.getClassId())&&classIds.contains(eccInfo.getClassId()))||(StringUtils.isNotBlank(eccInfo.getPlaceId())&&placeIds.contains(eccInfo.getPlaceId()))){
        					sids.add(eccInfo.getId());
        				}
        			}
        			if(classAttences.size()>0){
        				saveAll(classAttences.toArray(new EccClassAttence[classAttences.size()]));
        			}
        			
        			List<EccAttenceNoticeSet> noticeSets = eccAttenceNoticeSetService.findListBy("unitId", unitId);
    				EccAttenceNoticeSet noticeSet = new EccAttenceNoticeSet();
    				for(EccAttenceNoticeSet attenceNoticeSet:noticeSets){
    					if(EccConstants.CLASS_ATTENCE_SET_TYPE1.equals(attenceNoticeSet.getType())){
    						noticeSet = attenceNoticeSet;
    					}
    				}
    				Set<String> attIds = EntityUtils.getSet(classAttences,"id");
            		if(ringUnitIds.contains(unitId)&&attIds.size()>0){
            			ClassRingAttanceTask classRingAttanceTask = new ClassRingAttanceTask(unitId, attIds, true);
    					try {
    						now.add(Calendar.SECOND, 5);
    						DelayItem<ClassRingAttanceTask> craEnd = new DelayItem<ClassRingAttanceTask>(unitId+sectionNumber+"ring", DateUtils.date2StringBySecond(now.getTime()), classRingAttanceTask);
    						itemListRing.add(craEnd);
    					} catch (ParseException e) {
    						e.printStackTrace();
    					}
            		}
    				//2.结束时推送消息  
    				if(noticeSet.isSend()){
    					List<DelayItem<?>> itemList = new ArrayList<DelayItem<?>>();
    					Calendar time = Calendar.getInstance();
    					time.add(Calendar.MINUTE, noticeSet.getDelayTime()+5);
    					PushMsgTask pushMsgTask = new PushMsgTask(classAttenceId.toArray(new String[classAttenceId.size()]),EccConstants.CLASS_ATTENCE_SET_TYPE1,sectionNumber,unitId);
    					DelayItem<PushMsgTask> pushMsgDelay;
						try {
							pushMsgDelay = new DelayItem<PushMsgTask>(unitId, DateUtils.date2StringBySecond(time.getTime()), pushMsgTask);
							itemList.add(pushMsgDelay);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if(itemList.size()>0){
							eccTaskService.addEccTaskList(itemList);
						}
    				}
    				if(sids.size()>0){
        				EccNeedServiceUtils.postClassClock(sids, 1);
        			}
    				if(CollectionUtils.isNotEmpty(classAttenceId)){
    					Set<String> stuIds = eccStuclzAttenceService.findListStuByAttIds(classAttenceId.toArray(new String[classAttenceId.size()]));
    					if(CollectionUtils.isNotEmpty(stuIds)){
    						eccCacheService.deleteStuClassAttCacheDto(stuIds.toArray(new String[stuIds.size()]));
    					}
    				}
        		}
        	}else{
        		Date date = null;
        		String beginTime = ""; 
        		String endTime = ""; 
        		List<StusysSectionTimeSet> stusysSectionTimeSetList = SUtils.dt(stusysSectionTimeSetRemoteService.findByAcadyearAndSemesterAndUnitId(semester.getAcadyear(), semester.getSemester(),unitId,new String[]{section},true),StusysSectionTimeSet.class);
        		for(StusysSectionTimeSet timeSet:stusysSectionTimeSetList){
        			if(sectionNumber==timeSet.getSectionNumber()){
        				Calendar time = Calendar.getInstance();
        				beginTime = timeSet.getBeginTime();
        				endTime = timeSet.getEndTime();
        				String today = DateUtils.date2StringByDay(time.getTime());
        				date = DateUtils.string2Date(today+" "+timeSet.getBeginTime(), "yyyy-MM-dd HH:mm");
        			}
        		}
        		if(date==null){
        			Calendar time = Calendar.getInstance();
        			time.add(Calendar.MINUTE, 10);
        			date = time.getTime();
        		}
        		//acadyear,semester,schoolId,weekOfWorktime,dayOfWeek,periodInterval,period
        		//根据以上参数,需要考勤的课表List  
        		List<CourseSchedule> csList = SUtils.dt(courseScheduleRemoteService.findCourseScheduleList(semester.getAcadyear(), semester.getSemester(), unitId, weekOfWorktime, dayOfWeek, periodInterval, 1,period),new TR<List<CourseSchedule>>() {});
        		
        		System.out.println(unitId+"----"+weekOfWorktime+"-----"+dayOfWeek+"-----"+periodInterval+"-----"+period+"---"+section+"---"+DateUtils.date2StringBySecond(new Date()));
        		Map<String,List<CourseSchedule>> map = findSectionCourseMap(csList);
        		if(CollectionUtils.isEmpty(map.get(section))){
        			return;
        		}
        		List<EccClassAttence> attences = dealClassAttence(map.get(section),unitId,date,sectionNumber,beginTime, endTime,true,section);
        		
        		Set<String> attIds = EntityUtils.getSet(attences,EccClassAttence::getId);
        		if(ringUnitIds.contains(unitId)&&attIds.size()>0){
        			ClassRingAttanceTask classRingAttanceTask = new ClassRingAttanceTask(unitId, attIds, false);
        			String today = DateUtils.date2StringByDay(date);
        			Date date1 = DateUtils.string2Date(today+" "+beginTime, "yyyy-MM-dd HH:mm");
					try {
						DelayItem<ClassRingAttanceTask> craStart = new DelayItem<ClassRingAttanceTask>(unitId+sectionNumber+"ring", DateUtils.date2StringBySecond(date1), classRingAttanceTask);
						itemListRing.add(craStart);
					} catch (ParseException e) {
						e.printStackTrace();
					}
        		}
        	}
        	if(itemListRing.size()>0){
				eccTaskService.addEccTaskList(itemListRing);
			}
        }
	}
	
	/**
	 * 根据课表生成每节课考勤数据
	 * @param csList
	 * @param unitId
	 * @param date
	 * @param sectionNumber
	 * @param beginTime
	 * @param endTime
	 * @param push
	 * @param section
	 * @return
	 */
	private List<EccClassAttence> dealClassAttence(List<CourseSchedule> csList ,String unitId,Date date,int sectionNumber,String beginTime,String endTime,boolean push,String section) {
		List<EccClassAttence> ecaList = Lists.newArrayList();
		Set<String> sids = Sets.newHashSet();
		Set<String> placeIds = Sets.newHashSet();
		Set<String> xzPlaceIds = Sets.newHashSet();
		Set<String> classIds = Sets.newHashSet();
		List<EccInfo> eccInfos = eccInfoService.findByUnitId(unitId);
		Map<String,String> placeMap = Maps.newHashMap();
		Map<String,String> classMap = Maps.newHashMap();
		for(EccInfo eccInfo:eccInfos){
			if(StringUtils.isNotBlank(eccInfo.getType())){
				if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
					classMap.put(eccInfo.getClassId(), eccInfo.getId());
				}else if(EccConstants.ECC_MCODE_BPYT_2.equals(eccInfo.getType())){
					placeMap.put(eccInfo.getPlaceId(), eccInfo.getId());
				}
			}
		}
		List<EccClassAttence> oldClassAttences = eccClassAttenceDao.findBySectionClass(DateUtils.date2StringByDay(date), sectionNumber, unitId);
    	Map<String,EccClassAttence> oldClassAttMap = Maps.newHashMap();
    	Map<String,EccClassAttence> oldPlaceAttMap = Maps.newHashMap();
    	for(EccClassAttence classAttence:oldClassAttences){
    		if(EccConstants.CLASS_TYPE_NORMAL==classAttence.getClassType()){
    			oldClassAttMap.put(classAttence.getClassId(), classAttence);
    		}else{
    			oldPlaceAttMap.put(classAttence.getPlaceId(), classAttence);
    		}
    	}
    	List<Clazz> clazzs = SUtils.dt(classRemoteService.findBySchoolId(unitId),new TR<List<Clazz>>() {});
    	xzPlaceIds = EntityUtils.getSet(clazzs, Clazz::getTeachPlaceId);
    	Map<String,String> placeClassMap = EntityUtils.getMap(clazzs, Clazz::getTeachPlaceId,Clazz::getId);
		for(CourseSchedule cs:csList){
			if(EccConstants.CLASS_TYPE_NORMAL==cs.getClassType()){
				if(!oldClassAttMap.containsKey(cs.getClassId())&&StringUtils.isNotBlank(cs.getPlaceId())){
					EccClassAttence classAttence = new EccClassAttence();
					classAttence.setId(UuidUtils.generateUuid());
					classAttence.setClassId(cs.getClassId());
					classAttence.setOver(false);
					classAttence.setPeriod(cs.getPeriod());
					classAttence.setPeriodInterval(cs.getPeriodInterval());
					classAttence.setSubjectId(cs.getSubjectId());
					classAttence.setSubjectName(cs.getSubjectName());
					classAttence.setTeacherId(cs.getTeacherId());
					classAttence.setPlaceId(cs.getPlaceId());
					classAttence.setUnitId(unitId);
					if(push){
						classAttence.setCardId(classMap.get(cs.getClassId()));
					}
					classAttence.setClockDate(date);
					classAttence.setSectionNumber(sectionNumber);
					classAttence.setBeginTime(beginTime);
					classAttence.setEndTime(endTime);
					classAttence.setClassType(cs.getClassType());
					classAttence.setSection(section);
					classAttence.setCourseScheduleId(cs.getId());
					classAttence.setCreationTime(new Date());
					classAttence.setModifyTime(new Date());
					ecaList.add(classAttence);
				}
				if(push){
					classIds.add(cs.getClassId());
				}
			}
			else{
				if(!oldPlaceAttMap.containsKey(cs.getPlaceId())){
					EccClassAttence classAttence = new EccClassAttence();
					classAttence.setId(UuidUtils.generateUuid());
					classAttence.setClassId(cs.getClassId());
					classAttence.setPlaceId(cs.getPlaceId());
					classAttence.setOver(false);
					classAttence.setPeriod(cs.getPeriod());
					classAttence.setPeriodInterval(cs.getPeriodInterval());
					classAttence.setSubjectId(cs.getSubjectId());
					classAttence.setSubjectName(cs.getSubjectName());
					classAttence.setTeacherId(cs.getTeacherId());
					if(push){
						classAttence.setCardId(placeMap.get(cs.getPlaceId()));
					}
					classAttence.setUnitId(unitId);
					classAttence.setClockDate(date);
					classAttence.setSectionNumber(sectionNumber);
					classAttence.setBeginTime(beginTime);
					classAttence.setEndTime(endTime);
					classAttence.setClassType(cs.getClassType());
					classAttence.setSection(section);
					classAttence.setCourseScheduleId(cs.getId());
					classAttence.setCreationTime(new Date());
					classAttence.setModifyTime(new Date());
					ecaList.add(classAttence);
				}
				if(push){
					String placeId = cs.getPlaceId();
					if(xzPlaceIds.contains(placeId)){//没有绑定行政班的场地
						if(StringUtils.isNotBlank(placeId) && placeClassMap.containsKey(placeId)){
							classIds.add(placeClassMap.get(cs.getPlaceId()));
						}
					}else{
						placeIds.add(cs.getPlaceId());
					}
				}
			}
		}
		if(ecaList.size()>0){
			saveAll(ecaList.toArray(new EccClassAttence[ecaList.size()]));
			dealStuAndTeaClassAttence(ecaList,unitId);
		}
		
		for(EccInfo eccInfo:eccInfos){
			if((StringUtils.isNotBlank(eccInfo.getClassId())&&classIds.contains(eccInfo.getClassId()))||(StringUtils.isNotBlank(eccInfo.getPlaceId())&&placeIds.contains(eccInfo.getPlaceId()))){
				sids.add(eccInfo.getId());
			}
		}
		if(push&&sids.size()>0){
			EccNeedServiceUtils.postClassClock(sids,2);
		}
		ecaList.addAll(oldClassAttences);
		return ecaList;
	}

	/**
	 * 生成需要上课考勤的学生数据
	 * @param ecaList
	 */
	private void dealStuAndTeaClassAttence(List<EccClassAttence> ecaList,String unitId) {
		List<EccStuclzAttence> eccStuclzAttences = Lists.newArrayList();
		Set<String> normalClassIds = Sets.newHashSet();
		Set<String> type4ClassIds = Sets.newHashSet();
		Set<String> otherClassIds = Sets.newHashSet();
		Set<String> placeIds = EntityUtils.getSet(ecaList, EccClassAttence::getPlaceId);
		Map<String,String> attClassMap = Maps.newHashMap();
		Map<String,EccClassAttence> eccClassAttenceMap = Maps.newHashMap();
		List<EccTeaclzAttence> teaclzAttences = Lists.newArrayList();
		for(EccClassAttence classAttence:ecaList){
			eccClassAttenceMap.put(classAttence.getId(), classAttence);
			if(StringUtils.isNotBlank(classAttence.getClassId())){
				attClassMap.put(classAttence.getClassId(), classAttence.getId());
				if(EccConstants.CLASS_TYPE_NORMAL==classAttence.getClassType()){
					normalClassIds.add(classAttence.getClassId());
				}else if(EccConstants.CLASS_TYPE_4==classAttence.getClassType()){
					type4ClassIds.add(classAttence.getClassId());
				}else{
					otherClassIds.add(classAttence.getClassId());
				}
			}
			//0.生成老师考勤数据
			if(StringUtils.isNotBlank(classAttence.getTeacherId())){
				EccTeaclzAttence teaclzAttence = new EccTeaclzAttence();
				teaclzAttence.setClassAttId(classAttence.getId());
				teaclzAttence.setClockDate(classAttence.getClockDate());
				teaclzAttence.setId(UuidUtils.generateUuid());
				teaclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS1);
				teaclzAttence.setTeacherId(classAttence.getTeacherId());
				teaclzAttence.setCreationTime(new Date());
				teaclzAttence.setModifyTime(new Date());
				teaclzAttences.add(teaclzAttence);
			}
		}
		//获取请假学生
		Set<String> leaveStudentIds = Sets.newHashSet();
		if(getOpenApiOfficeService()!=null){
			try {
				String jsonStr1 = getOpenApiOfficeService().getHwStuLeavesByUnitId(unitId, null, "1", null, DateUtils.addMinute(new Date(), 10));
				JSONArray strings1 = EccUtils.getResultArray(jsonStr1, "studentIds");
				for (int i = 0; i < strings1.size(); i++) {
					leaveStudentIds.add(strings1.get(i).toString());
				}
			} catch (Exception e) {
			}
		}
		//1.行政班考勤学生生成
		if(CollectionUtils.isNotEmpty(normalClassIds)){
			List<Student> normalStudents = SUtils.dt(studentRemoteService.findByClassIds(normalClassIds.toArray(new String[normalClassIds.size()])),new TR<List<Student>>() {});
			for(Student student:normalStudents){
				String classId = student.getClassId();
				if(StringUtils.isNotBlank(classId)){
					String attId = attClassMap.get(classId);
					if(StringUtils.isNotBlank(attId)){
						EccStuclzAttence stuclzAttence = new EccStuclzAttence();
						stuclzAttence.setClassAttId(attId);
						stuclzAttence.setClassId(classId);
						if(leaveStudentIds.contains(student.getId())){
							stuclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS3);
						}else{
							stuclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS1);
						}
						stuclzAttence.setId(UuidUtils.generateUuid());
						stuclzAttence.setStudentId(student.getId());
						stuclzAttence.setCreationTime(new Date());
						stuclzAttence.setModifyTime(new Date());
						eccStuclzAttences.add(stuclzAttence);
					}
				}
			}
		}
		//2.6.0选课学生考勤数据
		if(CollectionUtils.isNotEmpty(type4ClassIds)&&getOpenApiNewElectiveService()!=null){
			for(String classId:type4ClassIds){
				List<String> stuIds = getOpenApiNewElectiveService().getStusByClassId(classId);
				for(String stuId:stuIds){
					String attId = attClassMap.get(classId);
					if(StringUtils.isNotBlank(attId)){
						EccStuclzAttence stuclzAttence = new EccStuclzAttence();
						stuclzAttence.setClassAttId(attId);
						stuclzAttence.setClassId(classId);
						if(leaveStudentIds.contains(stuId)){
							stuclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS3);
						}else{
							stuclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS1);
						}
						stuclzAttence.setId(UuidUtils.generateUuid());
						stuclzAttence.setStudentId(stuId);
						stuclzAttence.setCreationTime(new Date());
						stuclzAttence.setModifyTime(new Date());
						eccStuclzAttences.add(stuclzAttence);
					}
				}
			}
			
		}
		//3.其他，走班，7选3
		if(CollectionUtils.isNotEmpty(otherClassIds)){
			List<TeachClassStu> teachClassStus = SUtils.dt(teachClassStuRemoteService.findStudentByClassIds(otherClassIds.toArray(new String[otherClassIds.size()])),new TR<List<TeachClassStu>>() {});
			Map<String,String> teachstuClassMap = EntityUtils.getMap(teachClassStus, TeachClassStu::getStudentId,TeachClassStu::getClassId);
			for(String stuId:teachstuClassMap.keySet()){
				String classId = teachstuClassMap.get(stuId);
				if(StringUtils.isNotBlank(classId)){
					String attId = attClassMap.get(classId);
					if(StringUtils.isNotBlank(attId)){
						EccStuclzAttence stuclzAttence = new EccStuclzAttence();
						stuclzAttence.setClassAttId(attId);
						stuclzAttence.setClassId(classId);
						if(leaveStudentIds.contains(stuId)){
							stuclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS3);
						}else{
							stuclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS1);
						}
						stuclzAttence.setId(UuidUtils.generateUuid());
						stuclzAttence.setStudentId(stuId);
						stuclzAttence.setCreationTime(new Date());
						stuclzAttence.setModifyTime(new Date());
						eccStuclzAttences.add(stuclzAttence);
					}
				}
			}
		}
		if(teaclzAttences.size()>0){
			eccTeaclzAttenceService.saveAll(teaclzAttences.toArray(new EccTeaclzAttence[teaclzAttences.size()]));
		}
		if(eccStuclzAttences.size()>0){
			Map<String, String> teachPlaceMap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(placeIds.toArray(new String[0])),new TR<Map<String, String>>() {});
			eccStuclzAttenceService.saveAll(eccStuclzAttences.toArray(new EccStuclzAttence[eccStuclzAttences.size()]));
			for(EccStuclzAttence stuAtt:eccStuclzAttences){
				if(eccClassAttenceMap.containsKey(stuAtt.getClassAttId())){
					EccClassAttence attence = eccClassAttenceMap.get(stuAtt.getClassAttId());
					if(attence==null){
						continue;
					}
					ClassAttCacheDto dto = new ClassAttCacheDto();
					dto.setAttId(stuAtt.getClassAttId());
					dto.setId(stuAtt.getId());
					if(teachPlaceMap.containsKey(attence.getPlaceId())){
						dto.setPlaceName(teachPlaceMap.get(attence.getPlaceId()));
					}else{
						dto.setPlaceName("未知场地");
					}
					dto.setStatus(stuAtt.getStatus());
					dto.setSubjectName(attence.getSubjectName());
					eccCacheService.saveStuClassAttCacheDto(stuAtt.getStudentId(), dto);
				}
			}
		}
		
	}
	
	@Override
	public List<EccClassAttence> findListNotOver() {
		Calendar time = Calendar.getInstance();
		time.set(Calendar.HOUR, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		return eccClassAttenceDao.findListNotOver(time.getTime());
	}

	@Override
	public List<EccClassAttence> findListByStuAtt(String teacherId, Date date,
			String unitId,final int section,final String classId,String type,Pagination page) {
	        List<EccClassAttence> classAttences = Lists.newArrayList();
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
	        if(semester == null){
	        	return classAttences;
	        }
	        if("1".equals(type)&&StringUtils.isNotBlank(teacherId)){
	        	classAttences = eccClassAttenceDao.findListByTeacherId(teacherId, DateUtils.date2StringByDay(calendar.getTime()), unitId);
				for(EccClassAttence eca:classAttences){
					eca.setSectionName(EccUtils.getSectionName(eca.getSectionNumber())+"("+eca.getBeginTime()+"-"+eca.getEndTime()+")");
				}
//	        	DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
//	        	if(dateInfo == null){
//	        		return classAttences;
//	        	}
//	        	List<CourseSchedule> tcs = Lists.newArrayList();
//	        	List<CourseSchedule> thisdateTcs = Lists.newArrayList();
//	        	Integer dayOfWeek; // 星期一:0; 星期二:1;以此类推
//	        	int period = 0; // 节课
//	        	String periodInterval;//上午，下午，晚上
//	        	dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-2;
//	        	if(dayOfWeek==-1){
//	        		dayOfWeek = 6;
//	        	}
//	        	tcs = SUtils.dt(courseScheduleRemoteService.findByTeacherId(unitId, semester.getAcadyear(), semester.getSemester(), teacherId, dateInfo.getWeek()), CourseSchedule.class);
//	        	for(CourseSchedule schedule:tcs){
//	        		if(dayOfWeek==schedule.getDayOfWeek()&& schedule.getPunchCard()==1){
//	        			thisdateTcs.add(schedule);
//	        		}
//	        	}
//	        	Set<String> subjectIds = EntityUtils.getSet(thisdateTcs, "subjectId");
//	        	Map<String, String> courseNameMap = new HashMap<String, String>();
//	        	if(subjectIds.size()>0){//课程名
//					if (CollectionUtils.isNotEmpty(subjectIds)) {
//						List<Course> courseList = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIds.toArray(new String[0])),Course.class);
//						if (CollectionUtils.isNotEmpty(courseList)) {
//							courseNameMap =EntityUtils.getMap(courseList,"id","subjectName");
//						}
//					}
//				}
//	        	for(CourseSchedule cs:thisdateTcs){
//	        		if(courseNameMap.containsKey(cs.getSubjectId())){
//	        			cs.setSubjectName(courseNameMap.get(cs.getSubjectId()));
//	        		}
//	        	}
//	        	List<EccClassAttence> oldClassAttences = eccClassAttenceDao.findListByTeacherId(teacherId, DateUtils.date2StringByDay(calendar.getTime()), unitId);
//	        	Map<Integer,EccClassAttence> oldClassAttMap = Maps.newHashMap();
//	        	for(EccClassAttence classAttence:oldClassAttences){
//	        		oldClassAttMap.put(classAttence.getSectionNumber(), classAttence);
//	        	}
//	        	List<EccInfo> eccInfos = eccInfoService.findListBy("unitId", unitId);
//	    		Map<String,String> placeMap = Maps.newHashMap();
//	    		Map<String,String> classMap = Maps.newHashMap();
//	    		for(EccInfo eccInfo:eccInfos){
//	    			if(StringUtils.isNotBlank(eccInfo.getType())){
//	    				if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
//	    					classMap.put(eccInfo.getClassId(), eccInfo.getName());
//	    				}else if(EccConstants.ECC_MCODE_BPYT_2.equals(eccInfo.getType())){
//	    					placeMap.put(eccInfo.getPlaceId(), eccInfo.getName());
//	    				}
//	    			}
//	    		}
//	    		String sections = schoolRemoteService.findSectionsById(unitId);
//	        	if(StringUtils.isBlank(sections)){
//	        		return classAttences;
//	        	}
//	        	String[] secs = sections.split(",");
//	    		List<StusysSectionTimeSet> stusysSectionTimeSetList = SUtils.dt(stusysSectionTimeSetRemoteService.findByAcadyearAndSemesterAndUnitId(semester.getAcadyear(), semester.getSemester(),unitId,secs,true),StusysSectionTimeSet.class);
//	    		for(StusysSectionTimeSet timeSet:stusysSectionTimeSetList){
//	        		//今天没到上课时间的不初始化
//	        		if(DateUtils.date2StringByDay(new Date()).equals(DateUtils.date2StringByDay(date))&&DateUtils.date2String(DateUtils.addMinute(new Date(), 10), "HH:mm").compareTo(timeSet.getBeginTime())<0){
//	        			continue;
//	        		}
//	        		int sectionNumber = timeSet.getSectionNumber();
//	        		if(sectionNumber<=semester.getAmPeriods()){
//	        			periodInterval = EccConstants.PERIOD_AM;
//	        			period = sectionNumber;
//	        		}else if(sectionNumber<=semester.getAmPeriods()+semester.getPmPeriods()){
//	        			periodInterval = EccConstants.PERIOD_PM;
//	        			period = sectionNumber-semester.getAmPeriods();
//	        		}else{
//	        			periodInterval = EccConstants.PERIOD_NIGHT;
//	        			period = sectionNumber-semester.getAmPeriods()-semester.getPmPeriods();
//	        		}
//	        		
//	        		Map<String,List<CourseSchedule>> sectionCoMap = findSectionCourseMap(thisdateTcs);
//	        		for(String key:sectionCoMap.keySet()){
//	        			if(key.equals(timeSet.getSection())){
//	        				for(CourseSchedule cs:sectionCoMap.get(key)){
//	        					if(periodInterval.equals(cs.getPeriodInterval())&&period == cs.getPeriod()){
//	        						if(oldClassAttMap.containsKey(sectionNumber)){
//	        							EccClassAttence eca = oldClassAttMap.get(sectionNumber);
//	        							if(eca!=null){
//	        								eca.setSectionName(EccUtils.getSectionName(sectionNumber)+"("+eca.getBeginTime()+"-"+eca.getEndTime()+")");
//	        								classAttences.add(eca);
//	        							}
//	        						}
//	        						else{
//	        							EccClassAttence classAttence = new EccClassAttence();
//	        							classAttence.setId(UuidUtils.generateUuid());
//	        							classAttence.setClassId(cs.getClassId());
//	        							classAttence.setOver(false);
//	        							classAttence.setPeriod(cs.getPeriod());
//	        							classAttence.setPeriodInterval(cs.getPeriodInterval());
//	        							classAttence.setSubjectId(cs.getSubjectId());
//	        							classAttence.setSubjectName(cs.getSubjectName());
//	        							classAttence.setTeacherId(cs.getTeacherId());
//	        							classAttence.setPlaceId(cs.getPlaceId());
//	        							classAttence.setUnitId(unitId);
//	        							classAttence.setClockDate(date);
//	        							classAttence.setSectionNumber(sectionNumber);
//	        							classAttence.setBeginTime(timeSet.getBeginTime());
//	        							classAttence.setEndTime(timeSet.getEndTime());
//	        							classAttence.setClassType(cs.getClassType());
//	        							classAttence.setSection(key);
//	        							classAttence.setCourseScheduleId(cs.getId());
//	        							if(EccConstants.CLASS_TYPE_NORMAL==cs.getClassType()){
//	        								if(classMap.containsKey(cs.getClassId())){
//	        									classAttence.setInfoName(classMap.get(cs.getClassId()));
//	        								}else{
//	        									classAttence.setInfoName("");
//	        								}
//	        							}else{
//	        								if(placeMap.containsKey(cs.getPlaceId())){
//	        									classAttence.setInfoName(placeMap.get(cs.getPlaceId()));
//	        								}else{
//	        									classAttence.setInfoName("");
//	        								}
//	        							}
//	        							insertClassAttences.add(classAttence);
//	        							classAttence.setSectionName(EccUtils.getSectionName(sectionNumber)+"("+timeSet.getBeginTime()+"-"+timeSet.getEndTime()+")");
//	        							classAttences.add(classAttence);
//	        						}
//	        					}
//	        				}
//	        			}
//	        		}
//	        	}
	        }else if("2".equals(type)){
	        	if(StringUtils.isBlank(classId)){
	        		return classAttences;
	        	}
	        	String selectDate = DateUtils.date2StringByDay(calendar.getTime());
	        	Set<String> classIds = Sets.newHashSet();
	        	Set<String> stuIds = Sets.newHashSet();
	        	classIds.add(classId);
	        	List<Student> students = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}),new TR<List<Student>>() {});
	        	stuIds = EntityUtils.getSet(students,"id");
	        	if(stuIds.size()>0){
	        		List<TeachClass> teachClasses = SUtils.dt(teachClassRemoteService.findByStuIds(semester.getAcadyear(), semester.getSemester()+"", "1", stuIds.toArray(new String[stuIds.size()])),TeachClass.class);
	        		Set<String> tClassIds = EntityUtils.getSet(teachClasses, "id");
	        		classIds.addAll(tClassIds);
	        	}
//	        	List<TeachClassStu> classStus = SUtils.dt(teachClassStuRemoteService.findByStuIds(stuIds.toArray(new String[0])),new TR<List<TeachClassStu>>() {});
	        	if(getOpenApiNewElectiveService() != null){
	        		List<String> xxCourseIdList = getOpenApiNewElectiveService().getCouseIdsByUidSemesterAndStuId(unitId, semester.getAcadyear(), String.valueOf(semester.getSemester()), stuIds.toArray(new String[0]));
	        		for(String xxCourseId : xxCourseIdList){
	        			classIds.add(xxCourseId);
	        		}
	        	}
				String[] clzIds = classIds.toArray(new String[classIds.size()]);
	        	classAttences = eccClassAttenceDao.findByClassIdsSection(unitId,selectDate, section,clzIds, page);
	        	for(EccClassAttence attence:classAttences){
        			attence.setSectionName(EccUtils.getSectionName(attence.getSectionNumber())+"("+attence.getBeginTime()+"-"+attence.getEndTime()+")");
	        	}
	        }
	        fillNamedata(classAttences,true);
		return classAttences;
	}
	
	
	@Override
	public List<EccClassAttence> findListByStuAtt(String teacherId, Date date,
			String unitId) {
		  List<EccClassAttence> classAttences = Lists.newArrayList();
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
	        if(semester == null){
	        	return classAttences;
	        }
        	DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
        	if(dateInfo == null){
        		return classAttences;
        	}
        	classAttences = eccClassAttenceDao.findListByTeacherId(teacherId, DateUtils.date2StringByDay(calendar.getTime()), unitId);
        	for(EccClassAttence attence:classAttences){
        		attence.setSectionName(EccUtils.getSectionName(attence.getSectionNumber())+"("+attence.getBeginTime()+"-"+attence.getEndTime()+")");
        	}
	        fillNamedata(classAttences,true);
		return classAttences;
	}
	
	
	
	/**
	 * 填充名称
	 * @param classAttences
	 * @param isSum 是否包含统计数据
	 */
	private void fillNamedata(List<EccClassAttence> classAttences,boolean isSum){
		if(CollectionUtils.isNotEmpty(classAttences)){
			Set<String> attIds = EntityUtils.getSet(classAttences, "id");
			Set<String> teacherIds = EntityUtils.getSet(classAttences, "teacherId");
			Set<String> classIds = EntityUtils.getSet(classAttences, "classId");
			Set<String> subjectIds = EntityUtils.getSet(classAttences, "subjectId");
			List<EccStuclzAttence> eccStuclzAttences = eccStuclzAttenceService.findListByIn("classAttId", attIds.toArray(new String[attIds.size()]));
			Map<String,Integer> attStuNumMap = Maps.newHashMap();
			if(isSum){
				for(EccStuclzAttence stuclzAttence:eccStuclzAttences){
					if(EccConstants.CLASS_ATTENCE_STATUS1 ==stuclzAttence.getStatus()){
						if(attStuNumMap.containsKey(stuclzAttence.getClassAttId()+"qk")){
							attStuNumMap.put(stuclzAttence.getClassAttId()+"qk", attStuNumMap.get(stuclzAttence.getClassAttId()+"qk")+1);
						}else{
							attStuNumMap.put(stuclzAttence.getClassAttId()+"qk", 1);
						}
					}else if(EccConstants.CLASS_ATTENCE_STATUS2 ==stuclzAttence.getStatus()){
						if(attStuNumMap.containsKey(stuclzAttence.getClassAttId()+"cd")){
							attStuNumMap.put(stuclzAttence.getClassAttId()+"cd", attStuNumMap.get(stuclzAttence.getClassAttId()+"cd")+1);
						}else{
							attStuNumMap.put(stuclzAttence.getClassAttId()+"cd", 1);
						}
					}else if(EccConstants.CLASS_ATTENCE_STATUS3 ==stuclzAttence.getStatus()){
						if(attStuNumMap.containsKey(stuclzAttence.getClassAttId()+"qj")){
							attStuNumMap.put(stuclzAttence.getClassAttId()+"qj", attStuNumMap.get(stuclzAttence.getClassAttId()+"qj")+1);
						}else{
							attStuNumMap.put(stuclzAttence.getClassAttId()+"qj", 1);
						}
					}else if(EccConstants.CLASS_ATTENCE_STATUS4 ==stuclzAttence.getStatus()){
						if(attStuNumMap.containsKey(stuclzAttence.getClassAttId()+"zc")){
							attStuNumMap.put(stuclzAttence.getClassAttId()+"zc", attStuNumMap.get(stuclzAttence.getClassAttId()+"zc")+1);
						}else{
							attStuNumMap.put(stuclzAttence.getClassAttId()+"zc", 1);
						}
					}
				}
			}
			Map<String,String> teacherMap = Maps.newHashMap();
			Map<String,String> classMap = Maps.newHashMap();
			Map<String, String> courseNameMap = new HashMap<String, String>();
			if(teacherIds.size()>0){//老师名
				List<Teacher> teachers = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[teacherIds.size()])),Teacher.class);
				teacherMap = EntityUtils.getMap(teachers, "id", "teacherName");
			}
			
			if(classIds.size()>0){//班级名：行政班，教学班
				List<Clazz> classes = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])),Clazz.class);
				if(CollectionUtils.isNotEmpty(classes)){
					for (Clazz c : classes) {
						classMap.put(c.getId(), c.getClassNameDynamic());
					}
				}
				// 获取学年学期下学生的所有的教学班List
				List<TeachClass> teachClasses = SUtils.dt(teachClassRemoteService.findTeachClassContainNotUseByIds(classIds.toArray(new String[0])),TeachClass.class);
				if(CollectionUtils.isNotEmpty(teachClasses)){
					for (TeachClass c : teachClasses) {
						classMap.put(c.getId(), c.getName());
					}
				}
			}
			if(subjectIds.size()>0){//课程名
				if (CollectionUtils.isNotEmpty(subjectIds)) {
					List<Course> courseList = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIds.toArray(new String[0])),Course.class);
					if (CollectionUtils.isNotEmpty(courseList)) {
						courseNameMap =EntityUtils.getMap(courseList,"id","subjectName");
					}
				}
			}
			
			for(EccClassAttence classAttence:classAttences){
				if(isSum){
					if(attStuNumMap.containsKey(classAttence.getId()+"zc")){
						classAttence.setZcStuNum(attStuNumMap.get(classAttence.getId()+"zc"));
					}
					if(attStuNumMap.containsKey(classAttence.getId()+"qj")){
						classAttence.setQjStuNum(attStuNumMap.get(classAttence.getId()+"qj"));
					}
					if(attStuNumMap.containsKey(classAttence.getId()+"cd")){
						classAttence.setCdStuNum(attStuNumMap.get(classAttence.getId()+"cd"));
					}
					if(attStuNumMap.containsKey(classAttence.getId()+"qk")){
						classAttence.setQkStuNum(attStuNumMap.get(classAttence.getId()+"qk"));
					}
				}
				if(teacherMap.containsKey(classAttence.getTeacherId())){
					classAttence.setTeacherRealName(teacherMap.get(classAttence.getTeacherId()));
				}
				if(classMap.containsKey(classAttence.getClassId())){
					classAttence.setClassName(classMap.get(classAttence.getClassId()));
				}
				if(courseNameMap.containsKey(classAttence.getSubjectId())){
					classAttence.setSubjectName(courseNameMap.get(classAttence.getSubjectId()));
				}
				if(StringUtils.isBlank(classAttence.getClassName())){
					classAttence.setClassName(classAttence.getSubjectName());
				}
			}
		}
	}

	@Override
	public EccClassAttence findByIdFillName(String id) {
		List<EccClassAttence> classAttences = Lists.newArrayList();
		EccClassAttence attence = findOne(id);
		if(attence!=null){
			attence.setSectionName(EccUtils.getSectionName(attence.getSectionNumber()));
			classAttences.add(attence); 
			fillNamedata(classAttences,true);
		}
		return attence;
	}
	
	@Override
	public List<EccClassAttence> findByStudentIdSum(String studentId,
			String beginDate, String endDate,Pagination page) {
		List<EccClassAttence> classAttences  = Lists.newArrayList();
		List<EccStuclzAttence> stuclzAttences = eccStuclzAttenceService.findByStudentIdSum(studentId, beginDate, endDate);
		Set<String>  classAttIds = EntityUtils.getSet(stuclzAttences, EccStuclzAttence::getClassAttId);
		Map<String ,Integer> stuStatusMap = EntityUtils.getMap(stuclzAttences, EccStuclzAttence::getClassAttId, EccStuclzAttence::getStatus);
		if(classAttIds.size()>0){
			String[] caIds = classAttIds.toArray(new String[classAttIds.size()]);
			classAttences = eccClassAttenceDao.findByIdsSort(caIds,beginDate, endDate,page);
		}
		fillNamedata(classAttences, false);
		for(EccClassAttence classAttence:classAttences){
			if(stuStatusMap.containsKey(classAttence.getId())){
				classAttence.setStuStatus(stuStatusMap.get(classAttence.getId()));
				classAttence.setSectionName(EccUtils.getSectionName(classAttence.getSectionNumber())+"("+classAttence.getBeginTime()+"-"+classAttence.getEndTime()+")");
			}
		}
		return classAttences;
	}

	@Override
	public Map<String,String> findSectionNumMap(String unitId) {
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
		Map<String,String> periodMap = Maps.newLinkedHashMap();
		if(semester!=null){
			int secNum = semester.getAmPeriods()+semester.getPmPeriods()+semester.getNightPeriods();
//			List<StusysSectionTimeSet> stusysSectionTimeSetList = SUtils.dt(stusysSectionTimeSetRemoteService.findByAcadyearAndSemesterAndUnitId(semester.getAcadyear(), semester.getSemester(),unitId),StusysSectionTimeSet.class);
			for(int i=0;i<secNum;i++){
				periodMap.put(i+1+"", EccUtils.getSectionName(i+1));
			}
		}
		return periodMap;
	}

	@Override
	public List<EccClassAttence> findByIdsIsOver(String[] ids) {
		if(ids==null || ids.length==0) {
			return new ArrayList<>();
		}
		if (ids.length <= 1000) {
			return eccClassAttenceDao.findByIdsIsOver(ids);
		} else {
			List<EccClassAttence> list=new ArrayList<>();
			int cyc = ids.length / 1000 + (ids.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > ids.length)
					max = ids.length;
				List<EccClassAttence> list1 = eccClassAttenceDao.findByIdsIsOver(ArrayUtils.subarray(ids, i * 1000, max));
				if(CollectionUtils.isNotEmpty(list1)) {
					list.addAll(list1);
				}
			}
			return list;
		}
		
		
		
	}

	@Override
	public EccClassAttence findListByClassIdNotOver(String classId,
			int sectionNumber, String date, String unitId) {
		return eccClassAttenceDao.findListByClassIdNotOver(classId, sectionNumber, date, unitId);
	}

	@Override
	public EccClassAttence findListByPlaceIdNotOver(String placeId,
			int sectionNumber, String date, String unitId) {
		return eccClassAttenceDao.findListByPlaceIdNotOver(placeId, sectionNumber, date, unitId);
	}

	@Override
	public List<EccClassAttence> findListByTeaAtt(Date date, String unitId) {
		List<EccClassAttence> classAttences = Lists.newLinkedList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
        if(semester == null){
        	return classAttences;
        }
        DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
    	if(dateInfo == null){
    		return classAttences;
    	}
//    	List<StusysSectionTimeSet> stusysSectionTimeSetList = SUtils.dt(stusysSectionTimeSetRemoteService.findByAcadyearAndSemesterAndUnitId(semester.getAcadyear(), semester.getSemester(),unitId),StusysSectionTimeSet.class);
    	List<EccClassAttence> eccClassAttences = eccClassAttenceDao.findByUnitIdThisDate(DateUtils.date2StringByDay(date), unitId);
    	Map<Integer,Set<String>> attSectionMap = Maps.newHashMap();
    	Map<String,Integer> attSectionStatusMap = Maps.newHashMap();
    	Set<String> calssAttIds = EntityUtils.getSet(eccClassAttences, "id");
    	List<EccTeaclzAttence> teaclzAttences = Lists.newArrayList();
    	if(calssAttIds.size()>0){
    		teaclzAttences  = eccTeaclzAttenceService.findListByAttIds(calssAttIds.toArray(new String[calssAttIds.size()]));
    	}
    	
    	for(EccClassAttence attence:eccClassAttences){
    		if(attSectionMap.containsKey(attence.getSectionNumber())){
    			Set<String> attIds = attSectionMap.get(attence.getSectionNumber());
    			if(attIds!=null){
    				attIds.add(attence.getId());
    			}
    			attSectionMap.put(attence.getSectionNumber(), attIds);
    		}else{
    			Set<String> attIds = Sets.newHashSet();
    			attIds.add(attence.getId());
    			attSectionMap.put(attence.getSectionNumber(), attIds);
    		}
    	}
    	for (Integer key : attSectionMap.keySet()) {
    		Set<String> attIds = attSectionMap.get(key);
    		if(attIds!=null){
    			for(EccTeaclzAttence teaclzAttence:teaclzAttences){
    				if(attIds.contains(teaclzAttence.getClassAttId())){
    					if(EccConstants.CLASS_ATTENCE_STATUS1 ==teaclzAttence.getStatus()){
    						if(attSectionStatusMap.containsKey(key+"qk")){
    							attSectionStatusMap.put(key+"qk", attSectionStatusMap.get(key+"qk")+1);
    						}else{
    							attSectionStatusMap.put(key+"qk", 1);
    						}
    					}else if(EccConstants.CLASS_ATTENCE_STATUS2 ==teaclzAttence.getStatus()){
    						if(attSectionStatusMap.containsKey(key+"cd")){
    							attSectionStatusMap.put(key+"cd", attSectionStatusMap.get(key+"cd")+1);
    						}else{
    							attSectionStatusMap.put(key+"cd", 1);
    						}
    					}else if(EccConstants.CLASS_ATTENCE_STATUS4 ==teaclzAttence.getStatus()){
    						if(attSectionStatusMap.containsKey(key+"zc")){
    							attSectionStatusMap.put(key+"zc", attSectionStatusMap.get(key+"zc")+1);
    						}else{
    							attSectionStatusMap.put(key+"zc", 1);
    						}
    					}
    				}
    			}
    		}
    	}
    	int sectionNumber = semester.getAmPeriods()+semester.getPmPeriods()+semester.getNightPeriods();
    	for(int i=1;i<=sectionNumber;i++){
    		//今天没到上课时间的不显示
//    		if(DateUtils.date2StringByDay(new Date()).equals(DateUtils.date2StringByDay(date))&&DateUtils.date2String(DateUtils.addMinute(new Date(), 10), "HH:mm").compareTo(timeSet.getBeginTime())<0){
//    			continue;
//    		}
    		EccClassAttence classAttence = new EccClassAttence();
    		classAttence.setSectionNumber(i);
    		if(attSectionStatusMap.containsKey(i+"cd")){
    			classAttence.setCdStuNum(attSectionStatusMap.get(i+"cd"));
    		}
    		if(attSectionStatusMap.containsKey(i+"qk")){
    			classAttence.setQkStuNum(attSectionStatusMap.get(i+"qk"));
    		}
    		if(attSectionStatusMap.containsKey(i+"zc")){
    			classAttence.setZcStuNum(attSectionStatusMap.get(i+"zc"));
    		}
    		classAttence.setSectionName(EccUtils.getSectionName(i));
    		classAttences.add(classAttence);
    	}
		return classAttences;
	}
	
	@Override
	public List<EccTeaclzAttence> findListByTeaAttDetail(Date date,
			String unitId, Integer sectionNumber) {
//		List<EccTeaclzAttence> eccTeaclzAttences = Lists.newArrayList();
		List<EccClassAttence> classAttences = eccClassAttenceDao.findBySectionClass(DateUtils.date2StringByDay(date), sectionNumber, unitId);
//		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
//		if(semester==null){
//			return eccTeaclzAttences;
//		}
//		Calendar now = Calendar.getInstance();
//		now.setTime(date);
//		DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), now.getTime()), DateInfo.class);
//		if( dateInfo==null){
//			return eccTeaclzAttences;
//		}
//		int weekOfWorktime; //开学时间的周次
//		Integer dayOfWeek; // 星期一:0; 星期二:1;以此类推
//		int period = 0; // 节课
//		String periodInterval;//上午，下午，晚上
//		dayOfWeek = now.get(Calendar.DAY_OF_WEEK)-2;
//		if(dayOfWeek==-1){
//			dayOfWeek = 6;
//		}
//		weekOfWorktime = dateInfo.getWeek();
//		if(sectionNumber<=semester.getAmPeriods()){
//			periodInterval = EccConstants.PERIOD_AM;
//			period = sectionNumber;
//		}else if(sectionNumber<=semester.getAmPeriods()+semester.getPmPeriods()){
//			periodInterval = EccConstants.PERIOD_PM;
//			period = sectionNumber-semester.getAmPeriods();
//		}else{
//			periodInterval = EccConstants.PERIOD_NIGHT;
//			period = sectionNumber-semester.getAmPeriods()-semester.getPmPeriods();
//		}
//		String beginTime = ""; 
//		String endTime = ""; 
//		String sections = schoolRemoteService.findSectionsById(unitId);
//    	if(StringUtils.isBlank(sections)){
//    		return eccTeaclzAttences;
//    	}
//    	String[] secs = sections.split(",");
//		//根据课表生成没还生成的考勤数据
//		List<CourseSchedule> csList = SUtils.dt(courseScheduleRemoteService.findCourseScheduleList(semester.getAcadyear(), semester.getSemester(), unitId, weekOfWorktime, dayOfWeek, periodInterval, 1,period),new TR<List<CourseSchedule>>() {});
//		Map<String,List<CourseSchedule>> sectionCoMap = findSectionCourseMap(csList);
//		List<StusysSectionTimeSet> stusysSectionTimeSetList = SUtils.dt(stusysSectionTimeSetRemoteService.findByAcadyearAndSemesterAndUnitId(semester.getAcadyear(), semester.getSemester(),unitId,secs,true),StusysSectionTimeSet.class);
//		String sec = "";
//		for(String key:sectionCoMap.keySet()){
//			for(StusysSectionTimeSet timeSet:stusysSectionTimeSetList){
//				if(key.equals(timeSet.getSection())&&sectionNumber==timeSet.getSectionNumber()){
//					beginTime = timeSet.getBeginTime();
//					endTime = timeSet.getEndTime();
//					sec = timeSet.getSection();
//				}
//			}
//			List<EccClassAttence> attences= dealClassAttence(csList, unitId, date, sectionNumber, beginTime, endTime, false,sec);
//			classAttences.addAll(attences);
//		}
		Set<String> calssAttIds = EntityUtils.getSet(classAttences, "id");
    	List<EccTeaclzAttence> teaclzAttences = Lists.newArrayList();
//    	List<EccTeaclzAttence> insertTeaclzAttences = Lists.newArrayList();
//    	List<EccClassAttence> needInsertTeaAtt = Lists.newArrayList();
    	if(calssAttIds.size()>0){//已生成教师考勤数据
    		teaclzAttences  = eccTeaclzAttenceService.findListByAttIds(calssAttIds.toArray(new String[calssAttIds.size()]));
    	}
//    	for(EccTeaclzAttence teaclzAttence:teaclzAttences){//获取未生成的classAttIds
//    		if(calssAttIds.contains(teaclzAttence.getClassAttId())){
//    			calssAttIds.remove(teaclzAttence.getClassAttId());
//    		}
//    	}
//    	for(EccClassAttence classAttence:classAttences){//获取未生成的EccClassAttence
//    		if(calssAttIds.contains(classAttence.getId())){
//    			needInsertTeaAtt.add(classAttence);
//    		}
//    	}
//    	for(EccClassAttence classAttence:needInsertTeaAtt){//生成教师考勤数据
//    		EccTeaclzAttence teaclzAttence = new EccTeaclzAttence();
//    		teaclzAttence.setClassAttId(classAttence.getId());
//    		teaclzAttence.setId(UuidUtils.generateUuid());
//    		teaclzAttence.setClockDate(classAttence.getClockDate());
//    		teaclzAttence.setTeacherId(classAttence.getTeacherId());
//    		teaclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS1);
//    		insertTeaclzAttences.add(teaclzAttence);
//    	}
//    	if(insertTeaclzAttences.size()>0){
//    		eccTeaclzAttenceService.saveAll(insertTeaclzAttences.toArray(new EccTeaclzAttence[insertTeaclzAttences.size()]));
//    	}
    	fillNamedata(classAttences, false);
    	Map<String,EccClassAttence> ecaMap = EntityUtils.getMap(classAttences, EccClassAttence::getId);
    	for(EccTeaclzAttence teaclzAttence:teaclzAttences){
    		if(ecaMap.containsKey(teaclzAttence.getClassAttId())){
    			EccClassAttence classAttence = ecaMap.get(teaclzAttence.getClassAttId());
    			if(classAttence!=null){
    				teaclzAttence.setTeaRealName(classAttence.getTeacherRealName());
    				teaclzAttence.setClassName(classAttence.getClassName());
    				teaclzAttence.setSubjectName(classAttence.getSubjectName());
    			}
    		}
    	}
		return teaclzAttences;
	}

	@Override
	public List<EccClassAttence> findByTeacherIdSum(String teacherId,
			String bDate, String eDate, Pagination page) {
		List<EccClassAttence> classAttences  = Lists.newArrayList();
		List<EccTeaclzAttence> teaclzAttences = eccTeaclzAttenceService.findByTeacherIdSum(teacherId,bDate,eDate);
		Set<String>  classAttIds = EntityUtils.getSet(teaclzAttences, "classAttId");
		Map<String ,Integer> teaStatusMap = EntityUtils.getMap(teaclzAttences, "classAttId", "status");
		if(classAttIds.size()>0){
			String[] caIds = classAttIds.toArray(new String[classAttIds.size()]);
			classAttences = eccClassAttenceDao.findByIdsSort(caIds,bDate, eDate,page);
		}
		fillNamedata(classAttences, false);
		for(EccClassAttence classAttence:classAttences){
			if(teaStatusMap.containsKey(classAttence.getId())){
				classAttence.setStuStatus(teaStatusMap.get(classAttence.getId()));
				classAttence.setSectionName(EccUtils.getSectionName(classAttence.getSectionNumber())+"("+classAttence.getBeginTime()+"-"+classAttence.getEndTime()+")");
			}
		}
		return classAttences;
	}

	@Override
	public void classRingTaskRun(String unitId, Set<String> attIds, boolean isEnd) {
		List<EccClassAttence> classAttences = findListByIds(attIds.toArray(new String[0]));
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findBySchoolId(unitId),new TR<List<Clazz>>() {});
		Map<String,String> classIdPlaceMap = Maps.newHashMap();
		for(Clazz clazz:clazzs){
			classIdPlaceMap.put(clazz.getId(), clazz.getTeachPlaceId());
		}
		Map<String,EccClassAttence> classAttenceMap = EntityUtils.getMap(classAttences, EccClassAttence::getId);
		if(isEnd){
			for(String attId:attIds){
				EccClassAttence classAttence = classAttenceMap.get(attId);
				if(classAttence==null){
					continue;
				}
				String placeId = "";
				if(EccConstants.CLASS_TYPE_NORMAL==classAttence.getClassType()){
					placeId = classIdPlaceMap.get(classAttence.getClassId());
				}else{
					placeId = classAttence.getPlaceId();
				}
				if(StringUtils.isBlank(placeId)){
					continue;
				}
				List<EccStuclzAttence> eccStuclzAttences = eccStuclzAttenceService.findListByAttIdWithMaster(attId);
				
				List<EccStuclzAttence> updatestuclzAttences = Lists.newArrayList();
				Set<String> classStuIds = Sets.newHashSet();
				for(EccStuclzAttence stuclzAttence:eccStuclzAttences){
					if(!EccConstants.CLASS_ATTENCE_STATUS4.equals(stuclzAttence.getStatus())){
						classStuIds.add(stuclzAttence.getStudentId());
					}
				}
				if(CollectionUtils.isEmpty(classStuIds)){
					continue;
				}
				Set<String> studentIds= officeAttanceRemoteService.findInClassStudentIds(unitId,placeId,classStuIds);
				for(EccStuclzAttence stuclzAttence:eccStuclzAttences){
					if(studentIds.contains(stuclzAttence.getStudentId())&&EccConstants.CLASS_ATTENCE_STATUS1.equals(stuclzAttence.getStatus())){
						stuclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS2);
						updatestuclzAttences.add(stuclzAttence);
					}
				}
				if(updatestuclzAttences.size()>0){
					eccStuclzAttenceService.saveAll(updatestuclzAttences.toArray(new EccStuclzAttence[updatestuclzAttences.size()]));
				}
			}
		}else{
			for(String attId:attIds){
				EccClassAttence classAttence = classAttenceMap.get(attId);
				if(classAttence==null){
					continue;
				}
				String placeId = "";
				if(EccConstants.CLASS_TYPE_NORMAL==classAttence.getClassType()){
					placeId = classIdPlaceMap.get(classAttence.getClassId());
				}else{
					placeId = classAttence.getPlaceId();
				}
				if(StringUtils.isBlank(placeId)){
					continue;
				}
				List<EccStuclzAttence> eccStuclzAttences = eccStuclzAttenceService.findListByAttIdWithMaster(attId);
				List<EccStuclzAttence> updatestuclzAttences = Lists.newArrayList();
				Set<String> classStuIds = EntityUtils.getSet(eccStuclzAttences,EccStuclzAttence::getStudentId);
				Set<String> studentIds= officeAttanceRemoteService.findInClassStudentIds(unitId,placeId,classStuIds);
				
				JSONArray arr = new JSONArray();
				for(EccStuclzAttence stuclzAttence:eccStuclzAttences){
					if(studentIds.contains(stuclzAttence.getStudentId())&&!EccConstants.CLASS_ATTENCE_STATUS4.equals(stuclzAttence.getStatus())){
						stuclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS4);
						JSONObject json = new JSONObject();
						json.put("studentId", stuclzAttence.getStudentId());
						json.put("status", StuClockResultDto.SUCCESS);
						arr.add(json);
						updatestuclzAttences.add(stuclzAttence);
					}
				}
				if(StringUtils.isNotBlank(classAttence.getCardId())&&arr.size()>0){
					List<String> cardList = Lists.newArrayList();
					cardList.add(classAttence.getCardId());
					EccNeedServiceUtils.postRingClassClock(cardList, arr);
				}
				if(updatestuclzAttences.size()>0){
					eccStuclzAttenceService.saveAll(updatestuclzAttences.toArray(new EccStuclzAttence[updatestuclzAttences.size()]));
				}
			}
		}
		
	}

	/**
	 * 根据课表取学段对应的课表Map
	 * key--学段  value--课表List
	 * @param schedules
	 * @return
	 */
	@Override
	public Map<String,List<CourseSchedule>> findSectionCourseMap(List<CourseSchedule> schedules) {
		Map<String,List<CourseSchedule>> schedulesMap = Maps.newHashMap();
		Map<String,String> clzStuMap = Maps.newHashMap();
		Map<String,String> clzSectionMap = Maps.newHashMap();
		Set<String> classIds = Sets.newHashSet();//行政班
		Set<String> tClassIds = Sets.newHashSet();//教学班
		Set<String> cClassIds = Sets.newHashSet();//选修课
		Set<String> studentIds = Sets.newHashSet();
		List<CourseSchedule> useSchedules = Lists.newArrayList();
		for(CourseSchedule schedule:schedules){
			if(EccConstants.CLASS_TYPE_NORMAL==schedule.getClassType()&&String.valueOf(EccConstants.SUBJECT_TYPE_3).equals(schedule.getSubjectType())){
				continue;
			}
			if(EccConstants.CLASS_TYPE_NORMAL==schedule.getClassType()){
				classIds.add(schedule.getClassId());
			}else if(EccConstants.CLASS_TYPE_4==schedule.getClassType()){
				cClassIds.add(schedule.getClassId());
			}else{
				tClassIds.add(schedule.getClassId());
			}
			useSchedules.add(schedule);
		}
		if(classIds.size()>0){
			List<Clazz> classes = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])),Clazz.class);
			for(Clazz clazz:classes){
				clzSectionMap.put(clazz.getId(), clazz.getSection()+"");
			}
		}
		if(cClassIds.size()>0 && getOpenApiNewElectiveService()!=null){
			for(String classId:cClassIds){
				List<String> stuIds = getOpenApiNewElectiveService().getStusByClassId(classId);
				if(CollectionUtils.isNotEmpty(stuIds)&&StringUtils.isNotBlank(stuIds.get(0))){
					clzStuMap.put(classId, stuIds.get(0));
					studentIds.add(stuIds.get(0));
				}
			}
		}
		if(tClassIds.size()>0){
			List<TeachClassStu> classStus = SUtils.dt(teachClassStuRemoteService.findStudentByClassIds(tClassIds.toArray(new String[0])),new TR<List<TeachClassStu>>() {});
			for(TeachClassStu tcstu:classStus){
				if(!clzStuMap.containsKey(tcstu.getClassId())){
					clzStuMap.put(tcstu.getClassId(), tcstu.getStudentId());
					studentIds.add(tcstu.getStudentId());
				}
			}
		}
		if(studentIds.size()>0){
			List<Student> students = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])),new TR<List<Student>>() {});
			Set<String> claIds = EntityUtils.getSet(students, "classId");
			List<Clazz> clas = SUtils.dt(classRemoteService.findListByIds(claIds.toArray(new String[0])),Clazz.class);
			Map<String,Integer> claSecMap = EntityUtils.getMap(clas, "id","section");
			Map<String,String> stuSecMap = Maps.newHashMap();
			for(Student student:students){
				if(claSecMap.containsKey(student.getClassId())){
					stuSecMap.put(student.getId(), claSecMap.get(student.getClassId())+"");
				}
			}
			for(String key: clzStuMap.keySet()){
				if(stuSecMap.containsKey(clzStuMap.get(key))){
					clzSectionMap.put(key, stuSecMap.get(clzStuMap.get(key)));
				}
			}
		}
		for(CourseSchedule schedule:useSchedules){
			if(clzSectionMap.containsKey(schedule.getClassId())){
				String section = clzSectionMap.get(schedule.getClassId());
				if(StringUtils.isNotBlank(section)){
					List<CourseSchedule> slist= schedulesMap.get(section);
					if(CollectionUtils.isEmpty(slist)){
						slist = Lists.newArrayList();
					}
					slist.add(schedule);
					schedulesMap.put(section, slist);
				}
			}
		}
		return schedulesMap;
	}
	
	@Override
	public List<EccClassAttence> findByUnitIdThisDate(String date, String unitId) {
		return eccClassAttenceDao.findByUnitIdThisDate(date, unitId);
	}
	
	@Override
	public EccClassAttence findbyPlaceIdSecNumToDay(String unitId,
			String placeId, Integer sectionNumber,String studentId) {
		String toDay = DateUtils.date2StringByDay(new Date());
		EccClassAttence attence = findbyPlaceIdSecNumAndDay(unitId,placeId,sectionNumber,toDay);
		if(attence == null){
			 Student stu = SUtils.dt(studentRemoteService.findOneById(studentId),new TR<Student>() {});
			 if(stu == null){
				 return attence;
			 }
			 String classId = stu.getClassId();//此处直接找该学生行政班课表
			 if(StringUtils.isBlank(classId)){
				 return attence;
			 }
			 attence = findbyClassIdSecNumAndDay(unitId,classId,sectionNumber,toDay);
		}
		return attence;
	}
	
	@Override
	public EccClassAttence findbyPlaceIdSecNumAndDay(String unitId,
			String placeId, Integer sectionNumber, String toDay) {
		 return RedisUtils.getObject(KEY + unitId+"."+placeId +"."+sectionNumber+toDay+ ".object", 2*60*60, new TypeReference<EccClassAttence>() {
	        }, new RedisInterface<EccClassAttence>() {
	            @Override
	            public EccClassAttence queryData() {
	                return eccClassAttenceDao.findbyPlaceIdSecNumAndDay(unitId,placeId,sectionNumber,toDay);
	            }

	        });
	}
	@Override
	public EccClassAttence findbyClassIdSecNumAndDay(String unitId,
			String classId, Integer sectionNumber, String toDay) {
		 return RedisUtils.getObject(KEY + unitId+"."+classId +"."+sectionNumber+toDay+ ".object", 2*60*60, new TypeReference<EccClassAttence>() {
	        }, new RedisInterface<EccClassAttence>() {
	            @Override
	            public EccClassAttence queryData() {
	                return eccClassAttenceDao.findbyClassIdSecNumAndDay(unitId,classId,sectionNumber,toDay);
	            }
	        });
	}
	
	@Override
	public boolean saveStuAttSate(String unitId,EccClassAttence classAttence,String studentId,Date clockTime) {
		EccStuclzAttence eccStuclzAttence = eccStuclzAttenceService.findByStuIdClzAttId(studentId, classAttence.getId());
		if(eccStuclzAttence!=null){
			JSONArray arr = new JSONArray();
			if(EccConstants.CLASS_ATTENCE_STATUS1==eccStuclzAttence.getStatus()){
				eccStuclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS4);
				JSONObject json = new JSONObject();
				json.put("studentId", studentId);
				json.put("status", StuClockResultDto.SUCCESS);
				arr.add(json);
			}
			if(StringUtils.isNotBlank(classAttence.getCardId())&&arr.size()>0){
				List<String> cardList = Lists.newArrayList();
				cardList.add(classAttence.getCardId());
				EccNeedServiceUtils.postRingClassClock(cardList, arr);
			}
			eccStuclzAttenceService.save(eccStuclzAttence);
		}else{
			return false;
		}
		return true;
	}
}
