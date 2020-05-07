package net.zdsoft.eclasscard.data.action.show;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.constant.EccUsedFor;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.dto.EccUsedForDto;
import net.zdsoft.eclasscard.data.dto.StuClockResultDto;
import net.zdsoft.eclasscard.data.entity.*;
import net.zdsoft.eclasscard.data.service.*;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.remote.openapi.service.OpenApiNewElectiveService;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.stuwork.data.entity.DyDormBed;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import net.zdsoft.system.remote.service.UnitIniRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.Objects;
import java.util.*;
import java.util.stream.Collectors;
@Controller
@RequestMapping("/eccShow/eclasscard")
public class EccClockInAction extends BaseAction {
	
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private EccClockInService eccClockInService;
	@Autowired
	private EccClassAttenceService eccClassAttenceService;
	@Autowired
	private EccStuclzAttenceService eccStuclzAttenceService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private StusysSectionTimeSetRemoteService stusysSectionTimeSetRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private EccTeaclzAttenceService eccTeaclzAttenceService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private EccFaceActivateService eccFaceActivateService;
	@Autowired
	private EccOtherSetService eccOtherSetService;
	@Autowired
	private EccTimingSetService eccTimingSetService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private EccTimingLogService eccTimingLogService;
	@Autowired
	private UnitIniRemoteService unitIniRemoteService;
	
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
    @ResponseBody
	@RequestMapping("/clockIn")
    public String clockIn(String ownerId, String cardId, String cardNumber, String objectId, Integer clockType, Integer type,ModelMap map){
    	StuClockResultDto resultDto = new StuClockResultDto();
    	try{
			if(RedisUtils.hasLocked(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+cardNumber)){//连续刷卡处理
				try{
					resultDto = eccClockInService.dealClockIn(ownerId,cardId, cardNumber, objectId, clockType,type);
				}finally{
					RedisUtils.unLock(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+cardNumber);
				}
			}else{
				resultDto.setHaveStu(false);
				resultDto.setMsg("重复刷卡！");
			}
		}catch (Exception e) {
			e.printStackTrace();
			resultDto.setHaveStu(false);
			resultDto.setMsg("打卡失败！");
			return Json.toJSONString(resultDto);
		}
		return Json.toJSONString(resultDto);
	}
    
    @ResponseBody
    @RequestMapping("/classClockIn/chechClock")
    public String classChechClock(String cardId){
    	EccInfo eccInfo = eccInfoService.findOne(cardId);
    	if(eccInfo==null){
    		return error("no");
    	}
    	Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, eccInfo.getUnitId()), Semester.class);
    	if(semester==null){
    		return error("no");
    	}
    	Calendar now = Calendar.getInstance();
		DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(eccInfo.getUnitId(), semester.getAcadyear(), semester.getSemester(), now.getTime()), DateInfo.class);
    	if(dateInfo!=null){
			Integer sectionNumber = 0;
			int period = 0; // 节课
        	String periodInterval;//上午，下午，晚上
        	int weekOfWorktime; //开学时间的周次
        	Integer dayOfWeek; // 星期一:0; 星期二:1;以此类推
        	dayOfWeek = now.get(Calendar.DAY_OF_WEEK)-2;
        	if(dayOfWeek==-1){
        		dayOfWeek = 6;
        	}
        	weekOfWorktime = dateInfo.getWeek();
        	String beginTime = ""; 
        	String endTime = ""; 
        	String sections = schoolRemoteService.findSectionsById(eccInfo.getUnitId());
        	if(StringUtils.isBlank(sections)){
        		return error("no");
        	}
        	String[] section = sections.split(",");
        	List<StusysSectionTimeSet> stusysSectionTimeSetList = SUtils.dt(stusysSectionTimeSetRemoteService.findByAcadyearAndSemesterAndUnitId(semester.getAcadyear(), semester.getSemester(),eccInfo.getUnitId(),section,true),StusysSectionTimeSet.class);
			for(String sec:section){
				for(StusysSectionTimeSet timeSet:stusysSectionTimeSetList){
					if(sec.equals(timeSet.getSection())){
						if(DateUtils.date2String(DateUtils.addMinute(new Date(), 10), "HH:mm").compareTo(timeSet.getBeginTime())>=0 && 
								DateUtils.date2String(DateUtils.addMinute(new Date(), 5), "HH:mm").compareTo(timeSet.getEndTime())<0){
							sectionNumber = timeSet.getSectionNumber();
							beginTime = timeSet.getBeginTime();
							endTime = timeSet.getEndTime();
						}
					}
				}
				if(sectionNumber<=semester.getMornPeriods()){
					periodInterval = EccConstants.PERIOD_AM;
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
//				sectionNumber = 1;
				if(sectionNumber!=0){
					EccClassAttence classAttence=null;
					if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
						classAttence = eccClassAttenceService.findListByClassIdNotOver(eccInfo.getClassId(), sectionNumber,DateUtils.date2StringByDay(new Date()), eccInfo.getUnitId());
						if(classAttence == null){
							Clazz clazz = SUtils.dt(classRemoteService.findOneById(eccInfo.getClassId()),new TR<Clazz>() {});
							if(clazz!=null && StringUtils.isNotBlank(clazz.getTeachPlaceId())){
								classAttence = eccClassAttenceService.findListByPlaceIdNotOver(clazz.getTeachPlaceId(), sectionNumber,DateUtils.date2StringByDay(new Date()), eccInfo.getUnitId());
								if(classAttence!=null && EccConstants.CLASS_TYPE_NORMAL==classAttence.getClassType()){
									classAttence = null;
								}
							}
						}
					}
					if(EccConstants.ECC_MCODE_BPYT_2.equals(eccInfo.getType())){
						if(StringUtils.isNotBlank(eccInfo.getPlaceId())) {
							classAttence=eccClassAttenceService.findListByPlaceIdNotOver(eccInfo.getPlaceId(), sectionNumber, DateUtils.date2StringByDay(new Date()), eccInfo.getUnitId());
						}
						if(classAttence != null && EccConstants.CLASS_TYPE_NORMAL==classAttence.getClassType()){//行政班的考勤不显示
							classAttence = null;
						}
					}
//					classAttence = eccClassAttenceService.findOne("79025241172351659877474029749905");
					if(classAttence!=null){
						classAttence.setInfoName(eccInfo.getName());
						eccClassAttenceService.save(classAttence);
						return successByValue(classAttence.getId());
					}
				}
			}
		}
    	return error("no");
    }
	
	@RequestMapping("/classClockIn/index")
	public String classClockInIndex(String cardId,String view,HttpServletRequest request,String id,ModelMap map){
		String basePath = request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath();
		map.put("cardId", cardId);
		map.put("view", view);
		map.put("webSocketUrl", "ws://" + basePath + "/eClassCard/webSocketServer?sid="+cardId);
		map.put("eccIndexUrl", "http://" + basePath + "/eccShow/eclasscard/showIndex?cardId="+cardId);
		map.put("sockJSUrl", "http://" + basePath + "/eClassCard/sockjs/webSocketServer?sid="+cardId);
		EccClassAttence classAttence = eccClassAttenceService.findOne(id);
		if(classAttence==null){
			return "redirect:/eccShow/eclasscard/showIndex?cardId="+cardId+"&view="+view;
		}
		
		List<EccStuclzAttence> eccStuclzAttences = eccStuclzAttenceService.findListByAttIdWithMaster(classAttence.getId());
		EccTeaclzAttence teaclzAttence = eccTeaclzAttenceService.findByAttId(classAttence.getId());
		if(teaclzAttence==null){
			teaclzAttence = new EccTeaclzAttence();
			teaclzAttence.setClassAttId(classAttence.getId());
			teaclzAttence.setClockDate(classAttence.getClockDate());
			teaclzAttence.setId(UuidUtils.generateUuid());
			teaclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS1);
			teaclzAttence.setTeacherId(classAttence.getTeacherId());
		}
		Map<String,String> stuNameMap = Maps.newHashMap();
		Map<String,Integer> stuSexMap = Maps.newHashMap();
		Map<String,Student> stuMap = Maps.newHashMap();
		Set<String> ownerIds = EntityUtils.getSet(eccStuclzAttences, EccStuclzAttence::getStudentId);
		List<Student> allAtudents = SUtils.dt(studentRemoteService.findListByIds(ownerIds.toArray(new String[ownerIds.size()])),new TR<List<Student>>() {});
		for(Student student:allAtudents){
			stuNameMap.put(student.getId(), student.getStudentName());
			stuSexMap.put(student.getId(), student.getSex());
			stuMap.put(student.getId(), student);
		}
		ownerIds.add(classAttence.getTeacherId());
		Map<String,User> userMap = Maps.newHashMap();
		ownerIds.remove(null);
		if(ownerIds.size()>0){
			List<User> users = SUtils.dt(userRemoteService.findByOwnerIds(ownerIds.toArray(new String[ownerIds.size()])),new TR<List<User>>() {});
			userMap = EntityUtils.getMap(users, User::getOwnerId);
		}
		if(userMap.containsKey(classAttence.getTeacherId())){
			User user = userMap.get(classAttence.getTeacherId());
			if(user!=null){
				classAttence.setTeacherName(user.getUsername());
				classAttence.setTeacherRealName(user.getRealName());
			}
		}
		int clockNum = 0;
		int leaveNum = 0;
		int notClockNum = 0;
		int lateNum = 0;
		for(EccStuclzAttence attence:eccStuclzAttences){
			if(userMap.containsKey(attence.getStudentId())){
				User user = userMap.get(attence.getStudentId());
				if(user!=null){
					attence.setStuUserName(user.getUsername());
				}
			}
			if(stuMap.containsKey(attence.getStudentId())){
				Student student = stuMap.get(attence.getStudentId());
				if(student!=null){
					attence.setShowPictrueUrl(EccUtils.showPicUrl(student.getDirId(), student.getFilePath(), student.getSex()));
				}
			}
			if(stuNameMap.containsKey(attence.getStudentId())){
				attence.setStuRealName(stuNameMap.get(attence.getStudentId()));
			}
			if(stuSexMap.containsKey(attence.getStudentId())){
				attence.setSex(stuSexMap.get(attence.getStudentId()));
			}else{
				attence.setSex(1);
			}
			if(EccConstants.CLASS_ATTENCE_STATUS1==attence.getStatus()){
				notClockNum++;
			}else if(EccConstants.CLASS_ATTENCE_STATUS2==attence.getStatus()){
				lateNum++;
			}else if(EccConstants.CLASS_ATTENCE_STATUS3==attence.getStatus()){
				leaveNum++;
			}else if(EccConstants.CLASS_ATTENCE_STATUS4==attence.getStatus()){
				clockNum++;
			}
		}
		map.put("sumNum", eccStuclzAttences.size());//应到人数
		map.put("clockNum", clockNum);//实到人数
		map.put("leaveNum", leaveNum);//请假人数
		map.put("notClockNum", notClockNum);//未到人数
		map.put("lateNum", lateNum);//迟到人数
		map.put("classAttence", classAttence);
		map.put("teaclzAttence", teaclzAttence);
		map.put("eccStuclzAttences", eccStuclzAttences);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/verticalshow/eccClassClockIn.ftl";
		}else{
			return "/eclasscard/show/eccClassClockIn.ftl";
		}
	}
	
	@RequestMapping("/indexUrl")
	public String getIndexUrl(HttpServletRequest request){
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath();
		JSONObject returnObject = new JSONObject();
		returnObject.put("indexUrl", basePath+"/eccShow/eclasscard/showIndex");
		return RemoteCallUtils.returnResultJson(returnObject);
	}
	
	@ResponseBody
	@RequestMapping("/saveParam")
    public String saveParam(String remoteParam){
		try{
			String id = "";
			String deviceNumber = RemoteCallUtils.getParamValue(remoteParam, "deviceNumber");
			String unitIdentify = RemoteCallUtils.getParamValue(remoteParam, "unitIdentify");
			String factoryType = RemoteCallUtils.getParamValue(remoteParam, "factoryType");
			String wpversion = RemoteCallUtils.getParamValue(remoteParam, "wpversion");
			
			if (StringUtils.isBlank(deviceNumber)) {
				return RemoteCallUtils.returnResultError("设备号不能为空");
			}
			if(deviceNumber.length()>20){
				return RemoteCallUtils.returnResultError("设备号不可超过20位");
			}
			if (StringUtils.isBlank(unitIdentify)) {
				return RemoteCallUtils.returnResultError("单位标识不能为空");
			}
//			Unit unit = SUtils.dt(unitRemoteService.findByUnitClassAndUnionCode("unionCode", unitIdentify),new TR<Unit>(){});
			List<Unit> school = SUtils.dt(unitRemoteService.findByUnitClassAndUnionCode(Unit.UNIT_CLASS_SCHOOL, 
					unitIdentify), new TR<List<Unit>>() {
			});
			if(CollectionUtils.isEmpty(school)||school.get(0)==null){
				return RemoteCallUtils.returnResultError("单位标识不正确，请重新维护");
			}
			if(school.size()>1){
				return RemoteCallUtils.returnResultError("单位标识冲突，请联系管理员");
			}
			Unit unit = school.get(0);
			if(RedisUtils.hasLocked(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+deviceNumber)){
				try {
					EccInfo eccInfo = eccInfoService.findByUnitIdAndName(unit.getId(), deviceNumber);
					JSONObject returnObject = new JSONObject();
					if (eccInfo != null) {
						boolean isUpdate=false;
						if(StringUtils.isNotBlank(wpversion) && !wpversion.equals(eccInfo.getWpVersion())) {
							eccInfo.setWpVersion(wpversion);
							isUpdate=true;
						}
						if(StringUtils.isNotBlank(factoryType)) {
							if(factoryType.equals(eccInfo.getFactoryType())) {
								//未改变厂家
							}else {
								if(!isUpdate) {
									isUpdate=true;
								}
								//改变厂家 重新验证
								eccInfo.setFactoryType(factoryType);
							}
						}else {
							if(StringUtils.isBlank(eccInfo.getFactoryType())) {
								//默认取厂家类型
								factoryType=EccNeedServiceUtils.getEClassCardFactory(unit.getId());
								if(StringUtils.isNotBlank(factoryType)) {
									if(!isUpdate) {
										isUpdate=true;
									}
									eccInfo.setFactoryType(factoryType);
								}	
							}
						}
						if(isUpdate) {
							eccInfoService.save(eccInfo);
						}
						id = eccInfo.getId();
						returnObject.put("success", "保存成功");
						returnObject.put("id", id);
						return RemoteCallUtils.returnResultJson(returnObject);
					}
					eccInfo = new EccInfo();
					eccInfo.setUnitId(unit.getId());
					eccInfo.setId(UuidUtils.generateUuid());
					eccInfo.setName(deviceNumber);
					if(StringUtils.isNotBlank(factoryType)) {
						eccInfo.setFactoryType(factoryType);
					}else {
						factoryType=EccNeedServiceUtils.getEClassCardFactory(unit.getId());
						if(StringUtils.isNotBlank(factoryType)) {
							eccInfo.setFactoryType(factoryType);
						}		
					}
					if(StringUtils.isNotBlank(wpversion)) {
						eccInfo.setWpVersion(wpversion);
					}
					
					eccInfo.setStatus(1);
					EccFaceActivate face = new EccFaceActivate();
					face.setId(UuidUtils.generateUuid());
					face.setCreationTime(new Date());
					face.setCurrentFaceNum(0);
					face.setInfoId(eccInfo.getId());
					face.setIsLower(0);
					face.setLastLowerTime(new Date());
					face.setModifyTime(new Date());
					face.setNeedLower(0);
					face.setUnitId(unit.getId());
					eccInfoService.save(eccInfo);
					eccFaceActivateService.save(face);
					id = eccInfo.getId();
				}finally{
					RedisUtils.unLock(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+deviceNumber);
				}
			}else{
				return RemoteCallUtils.returnResultError("请勿重复保存");
			}
			JSONObject returnObject = new JSONObject();
			returnObject.put("success", "保存成功");
			returnObject.put("id", id);
			return RemoteCallUtils.returnResultJson(returnObject);
		}catch (Exception e) {
			e.printStackTrace();
			return RemoteCallUtils.returnResultError("保存失败");
		}
	}
	
	/**
	 * 升级过程，之前接口没有cardId,已配置好的班牌，启动时获取一次
	 * @param remoteParam
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCardId")
	public String getCardId(String remoteParam){
		try{
			String id = "";
			String deviceNumber = RemoteCallUtils.getParamValue(remoteParam, "deviceNumber");
			String unitIdentify = RemoteCallUtils.getParamValue(remoteParam, "unitIdentify");
			if (StringUtils.isNotBlank(deviceNumber) && StringUtils.isNotBlank(unitIdentify)) {
				Unit unit = SUtils.dt(unitRemoteService.findOneBy("unionCode", unitIdentify),new TR<Unit>(){});
				if(unit != null){
					EccInfo eccInfo = eccInfoService.findByUnitIdAndName(unit.getId(), deviceNumber);
					if (eccInfo != null) {
						id = eccInfo.getId();
					}
				}
			}
			JSONObject returnObject = new JSONObject();
			returnObject.put("success", "获取成功");
			returnObject.put("id", id);
			return RemoteCallUtils.returnResultJson(returnObject);
		}catch (Exception e) {
			e.printStackTrace();
			return RemoteCallUtils.returnResultError("获取失败");
		}
	}
	/**
	 * 原来班牌没有设置厂家的
	 * @param remoteParam
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getFactoryType")
	public String getFactoryType(String remoteParam){
		try{
			String cardId = RemoteCallUtils.getParamValue(remoteParam, "cardId");
			String factoryType="";
			if (StringUtils.isNotBlank(cardId)) {
				EccInfo eccInfo = eccInfoService.findOne(cardId);
				if(eccInfo!=null) {
					if(StringUtils.isNotBlank(eccInfo.getFactoryType())) {
						factoryType=eccInfo.getFactoryType();
					}else {
						factoryType=EccNeedServiceUtils.getEClassCardFactory(eccInfo.getUnitId());
					}
				}else {
					return RemoteCallUtils.returnResultError("获取失败,未找到班牌;cardId:"+cardId);
				}
			}
			JSONObject returnObject = new JSONObject();
			returnObject.put("success", "获取成功");
			returnObject.put("factoryType", factoryType);
			return RemoteCallUtils.returnResultJson(returnObject);
		}catch (Exception e) {
			e.printStackTrace();
			return RemoteCallUtils.returnResultError("获取失败");
		}
	}
	
	/**
	 * 班牌版本code
	 * @param remoteParam
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sendCardVersion")
	public String sendCardVersion(String remoteParam){
		try{
			String cardId = RemoteCallUtils.getParamValue(remoteParam, "cardId");
			String wpvistion = RemoteCallUtils.getParamValue(remoteParam, "wpvistion");
			if (StringUtils.isNotBlank(wpvistion)) {
				EccInfo eccInfo = eccInfoService.findOne(cardId);
				if(eccInfo!=null) {
					if(!wpvistion.equals(eccInfo.getWpVersion())) {
						eccInfo.setWpVersion(wpvistion);
						eccInfoService.save(eccInfo);
					}
				}else {
					return RemoteCallUtils.returnResultError("获取失败,未找到班牌;cardId:"+cardId);
				}
			}else {
				wpvistion="";
			}
			JSONObject returnObject = new JSONObject();
			returnObject.put("success", "上传成功");
			returnObject.put("wpversion", wpvistion);
			return RemoteCallUtils.returnResultJson(returnObject);
		}catch (Exception e) {
			e.printStackTrace();
			return RemoteCallUtils.returnResultError("获取失败");
		}
	}
	
	
	/**
	 * 判断是否有人脸功能，若有则获取设备信息提供三元组参数
	 * @param remoteParam
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getpdd")
	public String getPDD(String remoteParam){
		String cardId = RemoteCallUtils.getParamValue(remoteParam, "cardId");
		if(StringUtils.isBlank(cardId)){
			return RemoteCallUtils.returnResultError("cardId为空!");
		}
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo == null){
			return RemoteCallUtils.returnResultError("未找到班牌!");
		}
		String unitId = eccInfo.getUnitId();
		EccOtherSet otherSet = eccOtherSetService.findByUnitIdAndType(unitId,EccConstants.ECC_OTHER_SET_6);
		if (otherSet!=null && Objects.equals(1,otherSet.getNowvalue())) {
			JSONObject returnObject = new JSONObject();
			returnObject.put("success", "获取成功");
			return RemoteCallUtils.returnResultJson(returnObject);
		} else {
			return RemoteCallUtils.returnResultError("没有开启人脸识别服务!");
		}
	}
	
    @RequestMapping("/clockInAdmin")
	public String clockInAdmin(ModelMap map){
    	Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
    	if(null==semester){
    		return errorFtl(map,"当前时间不在学年学期内，无法维护！");
    	}else{
    		return "/eclasscard/show/chechClockAdmin.ftl";
    	}
		
	}
    
    @RequestMapping("/studentClockInHead")
    public String studentClockInHead(ModelMap map){
		Set<String> notContains = Sets.newHashSet();
		notContains.add(EccConstants.ECC_MCODE_BPYT_7);
		List<EccUsedForDto> usedForDtos = EccNeedServiceUtils.getEccUsedForList(notContains,getLoginInfo().getUnitId());
		map.put("usedForDtos", usedForDtos);
		return "/eclasscard/show/studentClockInHead.ftl";
    }
    
    @RequestMapping("/teacherClockInHead")
    public String teacherClockInHead(ModelMap map){
    	Set<String> notContains = Sets.newHashSet();
    	notContains.add(EccConstants.ECC_MCODE_BPYT_7);
    	List<EccUsedForDto> usedForDtos = EccNeedServiceUtils.getEccUsedForList(notContains,getLoginInfo().getUnitId());
    	map.put("usedForDtos", usedForDtos);
        return "/eclasscard/show/teacherClockInHead.ftl";
    }
    
    @RequestMapping("/studentClockInList")
    public String studentClockInList(Date startTime, Date endTime, String type, String studentId, ModelMap map, HttpServletRequest request){
    	Pagination page = createPagination();
    	List<EccClockIn> eccClockInList = eccClockInService.findListByAll(getLoginInfo().getUnitId(), EccConstants.ECC_CLOCK_IN_STUDENT, startTime, endTime, type, studentId, page);
    	Set<String> eccInfoIdSet = new HashSet<String>();
		Set<String> ownerIdSet = new HashSet<String>();
    	for(EccClockIn item : eccClockInList){
    		eccInfoIdSet.add(item.getEccInfoId());
    		if (StringUtils.isNotBlank(item.getOwnerId())) {
    			ownerIdSet.add(item.getOwnerId());
			}
    	}
    	Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
    	List<DyDormBed> dyDormBedList;
    	if(ownerIdSet.size()>0){
    		dyDormBedList = SUtils.dt(stuworkRemoteService.findDyDormBedByUnitId(getLoginInfo().getUnitId(), semester.getAcadyear(), String.valueOf(semester.getSemester()), ownerIdSet.toArray(new String[0])), new TR<List<DyDormBed>>() {});
    	}else{
    		dyDormBedList = new ArrayList<DyDormBed>();
    	}
    	Map<String, String> dormMap = new HashMap<String, String>();
    	Set<String> roomIdSet = new HashSet<String>();
    	for(DyDormBed item : dyDormBedList){
    		roomIdSet.add(item.getRoomId());
    	}
    	List<DyDormRoom> dyDormRoomList = SUtils.dt(stuworkRemoteService.findDyDormByIds(roomIdSet.toArray(new String[0])), new TR<List<DyDormRoom>>() {});
    	for(DyDormRoom item : dyDormRoomList){
    		dormMap.put(item.getId(), item.getRoomName());
    	}
    	Map<String, String> stuDormMap = new HashMap<String, String>();
    	for(DyDormBed item : dyDormBedList){
    		stuDormMap.put(item.getOwnerId(), dormMap.get(item.getRoomId()));
    	}
    	List<Student> students = SUtils.dt(studentRemoteService.findListByIds(ownerIdSet.toArray(new String[0])),new TR<List<Student>>() {});
    	Map<String, String> stuNameMap = new HashMap<String, String>();
    	Map<String, String> stuCodeMap = new HashMap<String, String>();
    	Set<String> clsIdSet = new HashSet<String>();
    	for(Student stu : students){
    		stuNameMap.put(stu.getId(), stu.getStudentName());
    		stuCodeMap.put(stu.getId(), stu.getStudentCode());
    		clsIdSet.add(stu.getClassId());
    	}
    	Map<String, String> clsNameMap = new HashMap<String, String>();
    	List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(clsIdSet.toArray(new String[0])), new TR<List<Clazz>>() {});
    	for(Clazz cls : classList){
    		clsNameMap.put(cls.getId(), cls.getClassNameDynamic());
    	}
    	Map<String, String> stuClsMap = new HashMap<String, String>();
    	for(Student stu : students){
    		stuClsMap.put(stu.getId(), clsNameMap.get(stu.getClassId()));
    	}
    	Map<String, String> placeMap = getPlaceMap(eccInfoIdSet);
    	int i = 1;
    	for(EccClockIn item : eccClockInList){
    		item.setOrderNumber(i);
    		item.setPlaceName(placeMap.get(item.getEccInfoId()));
    		if (StringUtils.isNotBlank(item.getOwnerId())) {
    			item.setStudentName(stuNameMap.get(item.getOwnerId()));
    			item.setStudentCode(stuCodeMap.get(item.getOwnerId()));
    			item.setClassName(stuClsMap.get(item.getOwnerId()));
    			item.setRoomName(stuDormMap.get(item.getOwnerId()));
			}
    		i++;
    	}
    	map.put("usedForMap", EccUsedFor.getEccUsedForMap());
    	map.put("eccClockInList", eccClockInList);
    	sendPagination(request, map, page);
    	return "/eclasscard/show/studentClockInList.ftl";
    }
    
    public Map<String, String> getPlaceMap(Set<String> eccInfoIdSet){
    	Map<String, String> placeMap = new HashMap<String, String>();
    	List<EccInfo> eccInfoList = eccInfoService.findListByIdIn(eccInfoIdSet.toArray(new String[0]));
    	Set<String> classIdSet = new HashSet<String>(); 
    	Set<String> placeIdSet = new HashSet<String>(); 
    	Set<String> dormBuildingIdSet = new HashSet<String>();
    	for(EccInfo item : eccInfoList){
    		if(EccConstants.ECC_MCODE_BPYT_1.equals(item.getType())){//行政班
    			classIdSet.add(item.getClassId());
    		}else if(EccConstants.ECC_MCODE_BPYT_2.equals(item.getType())){//非行政班
    			placeIdSet.add(item.getPlaceId());
    		}else if(EccConstants.ECC_MCODE_BPYT_3.equals(item.getType())){//寝室
    			dormBuildingIdSet.add(item.getPlaceId());
    		}else if(EccConstants.ECC_MCODE_BPYT_4.equals(item.getType())){//校门（进）
    			placeIdSet.add(item.getPlaceId());
    		}else if(EccConstants.ECC_MCODE_BPYT_5.equals(item.getType())){//校门（出）
    			placeIdSet.add(item.getPlaceId());
    		} else if(EccConstants.ECC_MCODE_BPYT_6.equals(item.getType())) {//签到
				placeIdSet.add(item.getPlaceId());
			}
    	}
    	List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>() {});
    	Map<String, String> teachPlaceMap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(placeIdSet.toArray(new String[0])),new TR<Map<String, String>>() {});
    	List<DormBuildingDto> dyDormBuildingList = SUtils.dt(stuworkRemoteService.getBuildingSbyUnitId(getLoginInfo().getUnitId()), new TR<List<DormBuildingDto>>() {});
    	for(EccInfo item : eccInfoList){
    		for(Clazz cls : classList){
    			if(cls.getId().equals(item.getClassId())){
    				placeMap.put(item.getId(), cls.getClassNameDynamic());
    			}
    		}
    		if(teachPlaceMap.containsKey(item.getPlaceId())){
    			placeMap.put(item.getId(), teachPlaceMap.get(item.getPlaceId()));
    		}
    		for(DormBuildingDto dormBuiding : dyDormBuildingList){
    			if(dormBuiding.getBuildingId().equals(item.getPlaceId())){
    				placeMap.put(item.getId(), dormBuiding.getBuildingName());
    			}
    		}
    	}   	
    	return placeMap;
    }
    
    @RequestMapping("/teacherClockInList")
    public String teacherClockInList(Date startTime, Date endTime, String type, String teacherId, ModelMap map, HttpServletRequest request){
    	Pagination page = createPagination();
    	List<EccClockIn> eccClockInList = eccClockInService.findListByAll(getLoginInfo().getUnitId(), EccConstants.ECC_CLOCK_IN_TEACHER, startTime, endTime, type, teacherId, page);
    	Set<String> teacherIdSet = new HashSet<String>();
    	Set<String> eccInfoIdSet = new HashSet<String>();
    	for(EccClockIn item : eccClockInList){
    		eccInfoIdSet.add(item.getEccInfoId());
    		teacherIdSet.add(item.getOwnerId());
    	}
    	List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIdSet.toArray(new String[0])), new TR<List<Teacher>>() {});
    	Map<String, String> teacherMap = new HashMap<String, String>();
    	//主教师
    	List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByTeacherId(getLoginInfo().getUnitId(), teacherIdSet.toArray(new String[0])), new TR<List<ClassTeaching>>() {});
    	Map<String, String> teacherTeachMap = new HashMap<String, String>();
    	if(CollectionUtils.isNotEmpty(classTeachingList)) {
    		Set<String> subIds = EntityUtils.getSet(classTeachingList, e->e.getSubjectId());
    		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])),  new TR<List<Course>>() {});
    		if(CollectionUtils.isNotEmpty(courseList)) {
    			Map<String, Course> cmap = EntityUtils.getMap(courseList, e->e.getId());
    			Map<String, Set<String>> subIdsByTeacherId = new HashMap<>();
    			for(ClassTeaching item : classTeachingList){
    				if(!cmap.containsKey(item.getSubjectId())) {
    					continue;
    				}
    				if(subIdsByTeacherId.containsKey(item.getTeacherId())) {
    					if(!subIdsByTeacherId.get(item.getTeacherId()).contains(item.getSubjectId())) {
    						subIdsByTeacherId.get(item.getTeacherId()).add(item.getSubjectId());
    						teacherTeachMap.put(item.getTeacherId(), teacherTeachMap.get(item.getTeacherId())+"、"+cmap.get(item.getSubjectId()).getSubjectName());
    					}
    				}else {
    					subIdsByTeacherId.put(item.getTeacherId(), new HashSet<>());
    					subIdsByTeacherId.get(item.getTeacherId()).add(item.getSubjectId());
    					teacherTeachMap.put(item.getTeacherId(), cmap.get(item.getSubjectId()).getSubjectName());
    				}
    				
            	}
    		}
    		
    	}
    	
    	for(Teacher teacher : teacherList){
    		teacherMap.put(teacher.getId(), teacher.getTeacherName());
    	}
    	Map<String, String> placeMap = getPlaceMap(eccInfoIdSet);
    	int i=1;
    	for(EccClockIn item : eccClockInList){
    		item.setOrderNumber(i);
    		item.setPlaceName(placeMap.get(item.getEccInfoId()));
    		item.setTeacherName(teacherMap.get(item.getOwnerId()));
    		item.setSubjectName(teacherTeachMap.get(item.getOwnerId()));
            i++;
    	}
    	map.put("usedForMap", EccUsedFor.getEccUsedForMap());
    	map.put("eccClockInList", eccClockInList);
    	sendPagination(request, map, page);
    	return "/eclasscard/show/teacherClockInList.ftl";
    }
    
    @ResponseBody
	@RequestMapping("/openClose/time")
	@ControllerInfo("返回开关时间数据")
	public String getTiming(String remoteParam){
    	String cardId = RemoteCallUtils.getParamValue(remoteParam, "cardId");
    	Integer index = Integer.valueOf(RemoteCallUtils.getParamValue(remoteParam, "index"));
    	//"yyyy-MM-dd-HH"
    	String nowCardTime = RemoteCallUtils.getParamValue(remoteParam, "nowCardTime");
    	EccInfo eccInfo = eccInfoService.findOne(cardId);
    	JSONArray array = new JSONArray();
		if(eccInfo==null) {
			return RemoteCallUtils.returnResultJson(array);
		}
		if(StringUtils.isNotBlank(nowCardTime)) {
			try {
		    	Calendar cal = Calendar.getInstance();
		    	//当前时间
		    	String nd=DateUtils.date2String(cal.getTime(), "yyyy-MM-dd-HH");
		    	//index:班牌上时间数据--日期+小时是否准确
		    	if(!nd.equals(nowCardTime)) {
		    		log.error("查看一下班牌时间："+cardId);
		   			RedisUtils.hasLocked("openclose_log_time"+cardId);
		   			EccTimingLog log=new EccTimingLog();
		   			log.setId(UuidUtils.generateUuid());
		   			log.setUnitId(eccInfo.getUnitId());
		   			log.setCardId(cardId);
		   			log.setId(UuidUtils.generateUuid());
		   			log.setTimeRemark("查看一下班牌时间,客户端："+nowCardTime+",服务器："+nd);
		   			log.setModifyTime(new Date());
		   			log.setCreationTime(new Date());
		   			log.setType("2");
		   			log.setIsDeleted(1);
		   			eccTimingLogService.saveTimeLog(null,log);
		   			RedisUtils.unLock("openclose_log_time"+cardId);
		    	}
	    	}catch(Exception e) {
	    		RedisUtils.unLock("openclose_log_time"+cardId);
	    		e.printStackTrace();
	    	}
		}
    	
    	List<EccTimingSet> sets = eccTimingSetService.findByUnitId(eccInfo.getUnitId());
		if (sets.size()>3) {
			List<EccTimingSet> oneSets = sets.stream().filter(set->set.getOrderIndex()>=index).limit(3).collect(Collectors.toList());
			if (oneSets.size()<3) {
				List<EccTimingSet> twoSets = sets.stream().filter(set->set.getOrderIndex()<index).limit(3-oneSets.size()).collect(Collectors.toList());
				oneSets.addAll(twoSets);
			}
			sets = oneSets;
		}
		
		JSONObject object = null;
		JSONObject timeObject = null;
		String[] times = null;
		for (EccTimingSet set : sets) {
			object = new JSONObject();
			object.put("code", set.getCode());
			timeObject = new JSONObject();
			times = set.getCloseTime().split(":");
			timeObject.put("minus", times[1]);
			timeObject.put("hour", times[0]);
			object.put("closeTime", timeObject);
			timeObject = new JSONObject();
			times = set.getOpenTime().split(":");
			timeObject.put("minus", times[1]);
			timeObject.put("hour", times[0]);
			object.put("openTime", timeObject);
			array.add(object);
		}
		return RemoteCallUtils.returnResultJson(array);
	}
    
    @ResponseBody
   	@RequestMapping("/openClose/sendlog")
   	@ControllerInfo("开关时间日志")
   	public String getTimingLog(String remoteParam){
       	String cardId = RemoteCallUtils.getParamValue(remoteParam, "cardId");
       	String timeRemark =RemoteCallUtils.getParamValue(remoteParam, "timeRemark");
       	String type=RemoteCallUtils.getParamValue(remoteParam, "type");//保存班牌类型，有可能操作方式不一样
       	JSONObject returnObject = new JSONObject();
       	if(StringUtils.isBlank(timeRemark) || StringUtils.isBlank(cardId)) {
       		returnObject.put("msg", "error0");
       		return RemoteCallUtils.returnResultJson(returnObject);
       	}
   		try {
   			EccInfo eccInfo = eccInfoService.findOne(cardId);
   			RedisUtils.hasLocked("openclose_log"+cardId);
   			EccTimingLog log=new EccTimingLog();
   			log.setId(UuidUtils.generateUuid());
   			log.setUnitId(eccInfo.getUnitId());
   			log.setCardId(cardId);
   			log.setId(UuidUtils.generateUuid());
   			log.setTimeRemark(timeRemark);
   			log.setModifyTime(new Date());
   			log.setCreationTime(new Date());
   			log.setIsDeleted(0);
   			log.setType("1");
   			EccTimingLog typeLog=null;
   			if(StringUtils.isNotBlank(type)) {
   				typeLog=new EccTimingLog();
   				typeLog.setId(UuidUtils.generateUuid());
   				typeLog.setUnitId(eccInfo.getUnitId());
   				typeLog.setCardId(cardId);
   				typeLog.setId(UuidUtils.generateUuid());
   				typeLog.setTimeRemark("班牌类型："+type);
   				typeLog.setModifyTime(log.getModifyTime());
   				typeLog.setCreationTime(log.getCreationTime());
   				typeLog.setIsDeleted(1);
   				typeLog.setType("2");
   			}
   			
   			eccTimingLogService.saveTimeLog(log,typeLog);
   			RedisUtils.unLock("openclose_log"+cardId);
   		}catch (Exception e) {
			e.printStackTrace();
			RedisUtils.unLock("openclose_log"+cardId);
			returnObject.put("msg", "error1");
       		return RemoteCallUtils.returnResultJson(returnObject);
		}
		returnObject.put("msg", "success");
		return RemoteCallUtils.returnResultJson(returnObject);
   	}
         

}
