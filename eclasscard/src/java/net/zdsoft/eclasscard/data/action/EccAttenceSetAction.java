package net.zdsoft.eclasscard.data.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.eclasscard.data.constant.EccCacheConstants;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.constant.EccUsedFor;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.dto.EccDateInfoDto;
import net.zdsoft.eclasscard.data.dto.EccDateInfoListDto;
import net.zdsoft.eclasscard.data.dto.EccFaceSetDto;
import net.zdsoft.eclasscard.data.dto.EccGradeDto;
import net.zdsoft.eclasscard.data.dto.EccNoticeSetDto;
import net.zdsoft.eclasscard.data.dto.EccOtherSetDto;
import net.zdsoft.eclasscard.data.dto.TimingSetDto;
import net.zdsoft.eclasscard.data.dto.cache.StudentCacheDto;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormGrade;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormPeriod;
import net.zdsoft.eclasscard.data.entity.EccAttenceGateGrade;
import net.zdsoft.eclasscard.data.entity.EccAttenceGatePeriod;
import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeSet;
import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeUser;
import net.zdsoft.eclasscard.data.entity.EccFaceActivate;
import net.zdsoft.eclasscard.data.entity.EccFaceLowerLog;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccOtherSet;
import net.zdsoft.eclasscard.data.entity.EccTimingSet;
import net.zdsoft.eclasscard.data.entity.EccUserFace;
import net.zdsoft.eclasscard.data.service.EccAttenceDormGradeService;
import net.zdsoft.eclasscard.data.service.EccAttenceDormPeriodService;
import net.zdsoft.eclasscard.data.service.EccAttenceGateGradeService;
import net.zdsoft.eclasscard.data.service.EccAttenceGatePeriodService;
import net.zdsoft.eclasscard.data.service.EccAttenceNoticeSetService;
import net.zdsoft.eclasscard.data.service.EccAttenceNoticeUserService;
import net.zdsoft.eclasscard.data.service.EccCacheService;
import net.zdsoft.eclasscard.data.service.EccDateInfoService;
import net.zdsoft.eclasscard.data.service.EccFaceActivateService;
import net.zdsoft.eclasscard.data.service.EccFaceLowerLogService;
import net.zdsoft.eclasscard.data.service.EccFaceSetService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccOtherSetService;
import net.zdsoft.eclasscard.data.service.EccTimingSetService;
import net.zdsoft.eclasscard.data.service.EccUserFaceService;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.FaceUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.entity.config.UnitIni;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
@Controller
@RequestMapping("/eclasscard")
public class EccAttenceSetAction extends BaseAction{
	public static final String ECC_ATTENCE_SET_ACTION = "ecc.attence_set_action";
	@Autowired
	private EccAttenceNoticeSetService eccAttenceNoticeSetService;
	@Autowired
	private EccAttenceNoticeUserService eccAttenceNoticeUserService;
	@Autowired
	private EccDateInfoService eccDateInfoService;
	@Autowired
	private EccAttenceGatePeriodService eccAttenceGatePeriodService;
	@Autowired
	private EccAttenceDormPeriodService eccAttenceDormPeriodService;
	@Autowired
	private EccAttenceDormGradeService eccAttenceDormGradeService;
	@Autowired
	private EccAttenceGateGradeService eccAttenceGateGradeService;
	@Autowired
    private GradeRemoteService gradeRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private EccOtherSetService eccOtherSetService;
	@Autowired
	private EccUserFaceService eccUserFaceService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private EccFaceActivateService eccFaceActivateService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private EccCacheService eccCacheService;
	@Autowired
	private EccFaceLowerLogService eccFaceLowerLogService;
	@Autowired
	private EccTimingSetService eccTimingSetService;
	@Autowired
	private EccFaceSetService eccFaceSetService;
	
	
	@RequestMapping("/attence/set/page")
	public String attenceSetPage(String type,ModelMap map){
		String  unitId = getLoginInfo().getUnitId();
		boolean isStandard = false;
		boolean isUseFace = false;
		boolean isUseTiming = false;
		if (UnitIni.ECC_USE_VERSION_STANDARD.equals(EccNeedServiceUtils.getEClassCardVerison(unitId))) {
			isStandard = true;
		}
		EccOtherSet faceSet = eccOtherSetService.findByUnitIdAndType(unitId, EccConstants.ECC_OTHER_SET_6);
		if (faceSet == null || Objects.equals(1,faceSet.getNowvalue())) {
			isUseFace = true;
		}
		if (Objects.equals(EccNeedServiceUtils.getEClassCardOpenClose(unitId), "1")) {
			isUseTiming = true;
		}
		map.put("isStandard", isStandard);
		map.put("isUseFace", isUseFace);
		map.put("isUseTiming", isUseTiming);
		return "/eclasscard/attanceset/attanceSetIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/attence/fullscreen/permissions/set")
	@ControllerInfo("其他权限设置保存")
	public String fullScreenSet(Integer nowValue,Integer type,ModelMap map) {
		try{
			eccOtherSetService.saveOtherSet(getLoginInfo().getUnitId(),nowValue,type);
		}catch (Exception e) {
			e.printStackTrace();
			return error("设置失败！"+e.getMessage());
		}
		return success("设置成功"); 
	}

	@RequestMapping("/attence/notice/set")
    public String attenceNoticeShow(ModelMap map){
		boolean isStandard = false;
		String unitId = getLoginInfo().getUnitId();
		Float distinguish = 0.85F;
		Float control = 0.85F;
		if (UnitIni.ECC_USE_VERSION_STANDARD.equals(EccNeedServiceUtils.getEClassCardVerison(unitId))) {
			isStandard = true;
			//全屏信息权限设置
			List<EccOtherSet> otherSets = eccOtherSetService.findListByUnitId(unitId);
			Map<Integer,EccOtherSet> otherMap = EntityUtils.getMap(otherSets, EccOtherSet::getType);
			if (otherMap.containsKey(EccConstants.ECC_OTHER_SET_1) &&
					Objects.equals(1, otherMap.get(EccConstants.ECC_OTHER_SET_1).getNowvalue())) {
				map.put("fullScreen", true);
			} else {
				map.put("fullScreen", false);
			}
			if (otherMap.containsKey(EccConstants.ECC_OTHER_SET_2) &&
					Objects.equals(0, otherMap.get(EccConstants.ECC_OTHER_SET_2).getNowvalue())) {
				map.put("vox", false);
			} else {
				map.put("vox", true);
			}
			if (otherMap.containsKey(EccConstants.ECC_OTHER_SET_3) &&
					otherMap.get(EccConstants.ECC_OTHER_SET_3).getNowvalue() > 0) {
				map.put("doorSticker", true);
				map.put("doorDelayTime", otherMap.get(EccConstants.ECC_OTHER_SET_3).getParam());
			} else {
				if(otherMap.containsKey(EccConstants.ECC_OTHER_SET_3)){
					map.put("doorDelayTime", otherMap.get(EccConstants.ECC_OTHER_SET_3).getParam());
				}else{
					map.put("doorDelayTime", "20");
				}
				map.put("doorSticker", false);
			}
			if (otherMap.containsKey(EccConstants.ECC_OTHER_SET_4)) {
				map.put("loginType", otherMap.get(EccConstants.ECC_OTHER_SET_4).getNowvalue());
			} else {
				map.put("loginType", 1);
			}
			if (otherMap.containsKey(EccConstants.ECC_OTHER_SET_5)) {
				map.put("speedValue", otherMap.get(EccConstants.ECC_OTHER_SET_5).getParam());
			}
			if (otherMap.containsKey(EccConstants.ECC_OTHER_SET_6) &&
					Objects.equals(1, otherMap.get(EccConstants.ECC_OTHER_SET_6).getNowvalue())) {
				map.put("showArcFace",true);
				String param = otherMap.get(EccConstants.ECC_OTHER_SET_6).getParam();
				if(StringUtils.isNotEmpty(param)){
					String[] pas = param.split(",");
					if(pas.length==2){
						control = Float.valueOf(pas[0]);
						distinguish = Float.valueOf(pas[1]);
					}
				}
			} else {
				map.put("showArcFace",false);
			}
			if (otherMap.containsKey(EccConstants.ECC_OTHER_SET_7) &&
					Objects.equals(1, otherMap.get(EccConstants.ECC_OTHER_SET_7).getNowvalue())) {
				map.put("openPrompt",true);
			} else {
				map.put("openPrompt",false);
			}
		}
		map.put("isStandard", isStandard);
		map.put("control", control);//下发阈值
		map.put("distinguish", distinguish);//识别阈值

		List<EccAttenceNoticeSet> noticeSets = eccAttenceNoticeSetService.findListBy("unitId", unitId);
		List<String> typeList = Lists.newArrayList(EccConstants.CLASS_ATTENCE_SET_TYPE1,EccConstants.DORM_ATTENCE_SET_TYPE2,EccConstants.IN_OUT_ATTENCE_SET_TYPE3); 
		if(CollectionUtils.isNotEmpty(noticeSets)){
			Set<String> haveTypeSet = EntityUtils.getSet(noticeSets, EccAttenceNoticeSet::getType);
			typeList.removeAll(haveTypeSet);
		}
		if(CollectionUtils.isNotEmpty(typeList)){
			List<EccAttenceNoticeSet> insertNoticeSets = Lists.newArrayList(); 
			for (String noticeType : typeList) {
				EccAttenceNoticeSet noticeSet = new EccAttenceNoticeSet();
				noticeSet.setId(UuidUtils.generateUuid());
				noticeSet.setSend(false);
				noticeSet.setSendClassMaster(false);
				noticeSet.setSendGradeMaster(false);
				noticeSet.setSendParentMaster(false);
				noticeSet.setType(noticeType);
				noticeSet.setUnitId(unitId);
				noticeSets.add(noticeSet);
				insertNoticeSets.add(noticeSet);
			}
			eccAttenceNoticeSetService.saveAll(insertNoticeSets.toArray(new EccAttenceNoticeSet[insertNoticeSets.size()]));
		}
		for(EccAttenceNoticeSet noticeSet:noticeSets){
			if(EccConstants.CLASS_ATTENCE_SET_TYPE1.equals(noticeSet.getType())){
				map.put("noticeSetClass", noticeSet);
			}else if(EccConstants.DORM_ATTENCE_SET_TYPE2.equals(noticeSet.getType())){
				map.put("noticeSetDorm", noticeSet);
			}else{
				map.put("noticeSetInOut", noticeSet);
			}
		}
		String dormUserIds = "";
		String classUserIds = "";
		String dormUserNames = "";
		String classUserNames = "";
		List<EccAttenceNoticeUser> noticeUsers = eccAttenceNoticeUserService.findListBy("unitId", unitId);
		Set<String> userIds = EntityUtils.getSet(noticeUsers, EccAttenceNoticeUser::getUserId);
		List<User> users = SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[userIds.size()])),new TR<List<User>>() {});
		Map<String,String> userMaps = EntityUtils.getMap(users, User::getId, User::getRealName);
		for(EccAttenceNoticeUser noticeUser:noticeUsers){
			if(EccConstants.CLASS_ATTENCE_SET_TYPE1.equals(noticeUser.getType())){
				if(StringUtils.isBlank(classUserIds)){
					classUserIds = noticeUser.getUserId();
				}else{
					classUserIds = classUserIds+","+noticeUser.getUserId();
				}
				if(userMaps.containsKey(noticeUser.getUserId())){
					if(StringUtils.isBlank(classUserNames)){
						classUserNames = userMaps.get(noticeUser.getUserId());
					}else{
						classUserNames = classUserNames+","+userMaps.get(noticeUser.getUserId());
					}
				}
			}else if(EccConstants.DORM_ATTENCE_SET_TYPE2.equals(noticeUser.getType())){
				if(StringUtils.isBlank(dormUserIds)){
					dormUserIds = noticeUser.getUserId();
				}else{
					dormUserIds = dormUserIds +","+noticeUser.getUserId();
				}
				if(userMaps.containsKey(noticeUser.getUserId())){
					if(StringUtils.isBlank(dormUserNames)){
						dormUserNames = userMaps.get(noticeUser.getUserId());
					}else{
						dormUserNames = dormUserNames+","+userMaps.get(noticeUser.getUserId());
					}
				}
			}
		}
		map.put("dormUserIds", dormUserIds);
		map.put("classUserIds", classUserIds);
		map.put("classUserNames", classUserNames);
		map.put("dormUserNames", dormUserNames);
		return "/eclasscard/attanceset/attanceNoticeSet.ftl";
	}
	@ResponseBody
	@RequestMapping("/attence/notice/save")
	public String attenceNoticeSave(EccNoticeSetDto eccNoticeSetDto,EccOtherSetDto otherSetDto,String classUserIds,String dormUserIds){
		try{
			String unitId = getLoginInfo().getUnitId();
			if (UnitIni.ECC_USE_VERSION_STANDARD.equals(EccNeedServiceUtils.getEClassCardVerison(unitId))) {
				eccOtherSetService.saveOtherSet(otherSetDto,unitId);
			}
			if(eccNoticeSetDto!=null){
				List<EccAttenceNoticeSet> noticeSets = Lists.newArrayList(); 
				noticeSets.add(eccNoticeSetDto.toEccNoticeSetClass());
				noticeSets.add(eccNoticeSetDto.toEccNoticeSetDorm());
				noticeSets.add(eccNoticeSetDto.toEccNoticeSetInOut());
				eccAttenceNoticeSetService.saveAll(noticeSets.toArray(new EccAttenceNoticeSet[3]));
			}
			
			if(StringUtils.isNotBlank(classUserIds)){
				eccAttenceNoticeUserService.updateNoticeUser(EccConstants.CLASS_ATTENCE_SET_TYPE1,classUserIds.split(","),eccNoticeSetDto.getUnitId());
			}
			if(StringUtils.isNotBlank(dormUserIds)){
				eccAttenceNoticeUserService.updateNoticeUser(EccConstants.DORM_ATTENCE_SET_TYPE2,dormUserIds.split(","),eccNoticeSetDto.getUnitId());
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@RequestMapping("/attence/query/head")
    public String attenceDateInfo(String type,ModelMap map){
		List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()),new TR<List<Grade>>() {});
		map.put("grades", grades);
		map.put("type", type);
		return "/eclasscard/attanceset/attanceQueryTab.ftl";
	}
	
	@RequestMapping("/attence/date/info/list")
	public String attenceDateInfoList(ModelMap map,String grade,String justSunDay){
		
    	List<EccDateInfoDto> dateInfos = eccDateInfoService.getDateList(getLoginInfo().getUnitId(), grade,justSunDay);
		map.put("dateInfos", dateInfos);
		return "/eclasscard/attanceset/attenceDateInfo.ftl";
	}
	@ResponseBody
	@RequestMapping("/attence/date/info/save")
	public String attenceDateInfoSave(EccDateInfoListDto eccDateInfoDtos,String grade){
		try{
			if(eccDateInfoDtos!=null && CollectionUtils.isNotEmpty(eccDateInfoDtos.getEccDateInfoDtoList()))
			eccDateInfoService.saveDateInfos(getLoginInfo().getUnitId(),grade,eccDateInfoDtos.getEccDateInfoDtoList());
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@RequestMapping("/attence/dorm/period/list")
    public String attenceDormPeriodList(HttpServletRequest request,ModelMap map,String gradeCode){
		int row = NumberUtils.toInt(syncParameters(request).get("_pageSize"));
		Pagination page = createPagination();
		if (row <= 0) {
			page.setPageSize(20);
		}
		List<EccAttenceDormPeriod> attenceDormPeriods = eccAttenceDormPeriodService.findList(getLoginInfo().getUnitId(),gradeCode,page);
		map.put("attenceDormPeriods", attenceDormPeriods);
		sendPagination(request, map, page);
		return "/eclasscard/attanceset/attenceDormPeriodList.ftl";
	}
	
	@RequestMapping("/attence/dorm/period/edit")
	public String attenceDormPeriodEdit(String id,ModelMap map){
		EccAttenceDormPeriod eccAttenceDormPeriod = new EccAttenceDormPeriod();
		Set<String> codes = Sets.newHashSet();
		if(StringUtils.isNotBlank(id)){
			eccAttenceDormPeriod = eccAttenceDormPeriodService.findOne(id);
			List<EccAttenceDormGrade> attenceDormGrades = eccAttenceDormGradeService.findListBy("periodId", eccAttenceDormPeriod.getId());
			for(EccAttenceDormGrade dormGrade:attenceDormGrades){
				codes.add(dormGrade.getGrade());
			}
		}
		
		List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()),new TR<List<Grade>>() {});
		List<EccGradeDto> eccGradeDtos = Lists.newArrayList(); 
		for(Grade grade:grades){
			EccGradeDto dto = new EccGradeDto();
			dto.setGradeCode(grade.getGradeCode());
			dto.setGradeName(grade.getGradeName());
			if(codes.contains(grade.getGradeCode())){
				dto.setSelect(true);
			}
			eccGradeDtos.add(dto);
		}
		map.put("eccGradeDtos", eccGradeDtos);
		map.put("eccAttenceDormPeriod", eccAttenceDormPeriod);
		return "/eclasscard/attanceset/attenceDormPeriodEdit.ftl";
	}
	@ResponseBody
	@RequestMapping("/attence/dorm/period/save")
	public String attenceDormPeriodSave(EccAttenceDormPeriod eccAttenceDormPeriod,String[] gradeCodes){
		try{
			eccAttenceDormPeriodService.saveAndCheck(getLoginInfo().getUnitId(),eccAttenceDormPeriod,gradeCodes);
		}catch (RuntimeException e) {
			return error("保存失败！"+e.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	@ResponseBody
	@RequestMapping("/attence/dorm/period/delete")
	public String attenceDormPeriodDelete(String id){
		try{
			List<EccAttenceDormGrade> dormGrades = eccAttenceDormGradeService.findListBy("periodId", id);
			eccAttenceDormPeriodService.deleteDormPeriod(id);
			eccAttenceDormGradeService.deleteAll(dormGrades.toArray(new EccAttenceDormGrade[0]));
		}catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！"+e.getMessage());
		}
		return success("删除成功");
	}
	
	@RequestMapping("/attence/gate/period/list")
    public String attenceGatePeriod(HttpServletRequest request,ModelMap map,String gradeCode, Integer classify){
		int row = NumberUtils.toInt(syncParameters(request).get("_pageSize"));
		Pagination page = createPagination();
		if (row <= 0) {
			page.setPageSize(20);
		}
		List<EccAttenceGatePeriod> gatePeriods = eccAttenceGatePeriodService.findList(getLoginInfo().getUnitId(),gradeCode,classify,page);
		
		map.put("gatePeriods", gatePeriods);
		map.put("classify", classify);
		sendPagination(request, map, page);
		return "/eclasscard/attanceset/attenceGatePeriodList.ftl";
	}
	
	@RequestMapping("/attence/gate/period/edit")
	public String attenceGatePeriodEdit(String id, Integer classify, ModelMap map){
		EccAttenceGatePeriod gatePeriod =new EccAttenceGatePeriod();
		Set<String> gradeCodes = Sets.newHashSet();
		if(StringUtils.isNotEmpty(id)){
			gatePeriod = eccAttenceGatePeriodService.findOne(id);
			List<EccAttenceGateGrade> gateGrades = eccAttenceGateGradeService.findListBy("periodId", id);
			for(EccAttenceGateGrade gateGrade:gateGrades){
				gradeCodes.add(gateGrade.getGrade());
			}
		}
		List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()),new TR<List<Grade>>() {});
		List<EccGradeDto> eccGradeDtos = Lists.newArrayList(); 
		for(Grade grade:grades){
			EccGradeDto dto = new EccGradeDto();
			dto.setGradeCode(grade.getGradeCode());
			dto.setGradeName(grade.getGradeName());
			if(gradeCodes.contains(grade.getGradeCode())){
				dto.setSelect(true);
			}
			eccGradeDtos.add(dto);
		}
		map.put("gatePeriod", gatePeriod);
		map.put("eccGradeDtos", eccGradeDtos);
		map.put("classify", classify);
		return "/eclasscard/attanceset/attenceGatePeriodEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/attence/gate/period/save")
	public String attenceGatePeriodSave(EccAttenceGatePeriod eccAttenceGatePeriod,String[] gradeCodes){
		try{
			eccAttenceGatePeriodService.saveAndCheck(getLoginInfo().getUnitId(),eccAttenceGatePeriod,gradeCodes);
		}catch (RuntimeException e) {
			return error("保存失败！"+e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@ResponseBody
	@RequestMapping("/attence/gate/period/delete")
	public String attenceGatePeriodDelete(String id){
		try{
			eccAttenceGatePeriodService.deleteById(id);
		}catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！"+e.getMessage());
		}
		return success("删除成功");
	}
	
	/***************************人脸部分***********************************************************************************/
	@RequestMapping("/face/index")
	@ControllerInfo("人脸识别设置")
	public String faceIndex(String subTabType,ModelMap map){
		if(StringUtils.isEmpty(subTabType)){
			subTabType = "1";
		}
		map.put("subTabType", subTabType);
		return "/eclasscard/attanceset/faceIndex.ftl";
	}
	
	@RequestMapping("/face/student/tab")
	@ControllerInfo("学生选择条件")
	public String faceStuTab(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolId(unitId),new TR<List<Grade>>(){});
		map.put("grades", grades);
		
		return "/eclasscard/attanceset/faceTab.ftl";
	}
	
	
	@ResponseBody
	@RequestMapping(value="/face/bacth/upload/save",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ControllerInfo("人脸图片批量上传")
	public String faceBacthUpload(@RequestBody String array){
		String result = "";
		try{	
			String unitId = getLoginInfo().getUnitId();
			result = eccUserFaceService.saveBacthUpload(array,unitId);
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return successByValue(result);
	}
	
	@RequestMapping("/face/lower/hair")
	@ControllerInfo("下发页面")
	public String faceLowerHair(ModelMap map,HttpServletRequest request){
		String unitId = getLoginInfo().getUnitId();
		int row = NumberUtils.toInt(syncParameters(request).get("_pageSize"));
		Pagination page = createPagination();
		if (row <= 0) {
			page.setPageSize(20);
		}
		List<EccInfo> eccInfos = eccInfoService.findByNameAndType("", "", "",0,unitId, page);
		String jsonStr = stuworkRemoteService.getBuildingSbyUnitId(getLoginInfo().getUnitId());
		List<DormBuildingDto> buildingDtos = SUtils.dt(jsonStr,new TR<List<DormBuildingDto>>() {});
		Map<String,String> dormBuildMap = EntityUtils.getMap(buildingDtos, DormBuildingDto::getBuildingId,DormBuildingDto::getBuildingName);
		List<EccFaceActivate> activates = eccFaceActivateService.findListByUnitId(unitId);
		Map<String,Integer> currentFaceNumMap = EntityUtils.getMap(activates, EccFaceActivate::getInfoId,EccFaceActivate::getCurrentFaceNum);
		Map<String,String[]> map1=new HashMap<String, String[]>();
		//eccUserFaceService
		if(CollectionUtils.isNotEmpty(eccInfos)) {
			List<String> ids=EntityUtils.getList(eccInfos, e->e.getId());
			map1=eccFaceSetService.findEccFaceSetListByInfoIds(unitId,ids.toArray(new String[0]));
		}
		
		
		for(EccInfo eccInfo:eccInfos){
			if(EccConstants.ECC_MCODE_BPYT_3.equals(eccInfo.getType())){
				if(dormBuildMap.containsKey(eccInfo.getPlaceId())){
					eccInfo.setPlaceName(dormBuildMap.get(eccInfo.getPlaceId()));
				}else if(StringUtils.isNotBlank(eccInfo.getPlaceId())){
					eccInfo.setPlaceName("（已删除）");
				}
			}
			if(currentFaceNumMap.containsKey(eccInfo.getId())){
				eccInfo.setCurrentFaceNum(currentFaceNumMap.get(eccInfo.getId()));
			}
			if(map1.containsKey(eccInfo.getId())) {
				eccInfo.setClassIds(map1.get(eccInfo.getId())[0]);
				eccInfo.setClassNames(map1.get(eccInfo.getId())[1]);
				eccInfo.setAllNum(Integer.parseInt(map1.get(eccInfo.getId())[2]));
			}
		}
		//int allNum = eccUserFaceService.countByUnitId(unitId);
		map.put("usedForMap", EccUsedFor.getEccUsedForMap());
		//map.put("allNum", allNum);
		map.put("eccInfos", eccInfos);
		sendPagination(request, map, page);
		return "/eclasscard/attanceset/lowerHairList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/face/lower/hair/todevice")
	@ControllerInfo("同步人脸到设备")
	public String loweHairTodevice(String cardId){
		if(RedisUtils.get(ECC_ATTENCE_SET_ACTION+"loweHairTodevice"+cardId) == null){
			try{	
				String unitId = getLoginInfo().getUnitId();
				EccInfo eccInfo = eccInfoService.findOne(cardId);
				EccFaceActivate face = eccFaceActivateService.findByInfoId(unitId,cardId);
				if(eccInfo==null||eccInfo.getStatus()!=2){
					return error("下发失败,设备离线");
				}
				if(face==null){
					return error("下发失败,沒有关联IOT设备");
				}else{
					if(face.getIsLower()==1 && DateUtils.compareForDay(DateUtils.addMinute(new Date(), -10), face.getLastLowerTime())<0){
						return error("下发失败,客户端设备处理中");
					}
					RedisUtils.set(ECC_ATTENCE_SET_ACTION+"loweHairTodevice"+cardId, "1",20);
					EccFaceLowerLog lowerLog = new EccFaceLowerLog();
					lowerLog.setId(UuidUtils.generateUuid());
					lowerLog.setType(2);
					lowerLog.setInfoId(cardId);
					lowerLog.setUnitId(unitId);
					face.setLastLowerTime(new Date());
					boolean flag = eccFaceLowerLogService.lowerFaceByCard(face, lowerLog);
					lowerLog.setState(1);
					face.setIsLower(1);
					face.setNeedLower(0);
					eccFaceActivateService.save(face);
					lowerLog.setCreationTime(new Date());
					lowerLog.setModifyTime(new Date());
					eccFaceLowerLogService.save(lowerLog);
					if(!flag){
						return error("下发失败！");
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				return error("下发失败！");
			}
		}else{
			return error("下发太频繁了！");
		}
		return success("");
	}
	
	@RequestMapping("/face/student/list")
	@ControllerInfo("人脸列表")
    public String faceList(HttpServletRequest request,String gradeId,String classId,int status,ModelMap map){
		int row = NumberUtils.toInt(syncParameters(request).get("_pageSize"));
		Pagination page = createPagination();
		if (row <= 0) {
			page.setPageSize(20);
		}
		String unitId = getLoginInfo().getUnitId();
		List<Student> studentList = Lists.newArrayList();
		List<StudentCacheDto> studentCacheDtos = Lists.newArrayList();
		List<StudentCacheDto> returnDtos = Lists.newArrayList();
		List<StudentCacheDto> wschDtos = Lists.newArrayList();
		String wschName = "全部未上传：";
		if(StringUtils.isBlank(gradeId) && StringUtils.isBlank(classId)){
			studentCacheDtos = eccCacheService.getSchoolStuListCache(unitId);
		}else{
			if(StringUtils.isNotBlank(gradeId) && StringUtils.isBlank(classId)){
				Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId),Grade.class);
				if(grade!=null){
					wschName = grade.getGradeName()+"未上传：";
				}
				List<Clazz> clazzs = SUtils.dt(classRemoteService.findBySchoolIdGradeId(getLoginInfo().getUnitId(), gradeId),new TR<List<Clazz>>() {});
				Set<String> classIds = EntityUtils.getSet(clazzs, Clazz::getId);
				if(CollectionUtils.isNotEmpty(classIds)){
					studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[classIds.size()])),new TR<List<Student>>() {});
				}
			}else if(StringUtils.isNotBlank(classId)){
				Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
				if(clazz!=null){
					wschName = clazz.getClassNameDynamic()+"未上传：";
				}
				studentList = SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>() {});
			}
			studentCacheDtos = StudentCacheDto.toStudentCacheDto(studentList);
		}
		if (CollectionUtils.isNotEmpty(studentCacheDtos)) {
			Collections.sort(studentCacheDtos, new Comparator<StudentCacheDto>() {
				@Override
				public int compare(StudentCacheDto o1, StudentCacheDto o2) {
					if(StringUtils.isBlank(o1.getStudentCode())&&StringUtils.isBlank(o2.getStudentCode())){
						return 0;
					}
					if(StringUtils.isBlank(o1.getStudentCode())){
						return 1;
					}
					if(StringUtils.isBlank(o2.getStudentCode())){
						return -1;
					}
					return o1.getStudentCode().compareTo(o2.getStudentCode());
				}
			});
		}
		Set<String> stuIds = EntityUtils.getSet(studentCacheDtos, StudentCacheDto::getId);
		if(CollectionUtils.isNotEmpty(stuIds)){
			List<EccUserFace> faces = eccUserFaceService.findByOwnerIds(stuIds.toArray(new String[stuIds.size()]));
			Map<String,String> faceMap = EntityUtils.getMap(faces, EccUserFace::getOwnerId,EccUserFace::getId);
			Map<String,Integer> faceFailMap = EntityUtils.getMap(faces, EccUserFace::getOwnerId,EccUserFace::getFailTimes);
			wschDtos = studentCacheDtos.stream().filter(line ->!faceMap.containsKey(line.getId())).collect(Collectors.toList());
			if(status==1){
				studentCacheDtos = studentCacheDtos.stream().filter(line ->faceMap.containsKey(line.getId())).collect(Collectors.toList());
			}else if(status==2){
				studentCacheDtos = wschDtos;
			}
			for(StudentCacheDto dto:studentCacheDtos){
				if(faceMap.containsKey(dto.getId())){
					dto.setShowPictrueUrl("/eccShow/eclasscard/loadface?loadType=0&id="+faceMap.get(dto.getId()));
				}
				if(faceFailMap.containsKey(dto.getId())){
					dto.setFailTimes(faceFailMap.get(dto.getId()));
				}
			}
		}
		//假分页
		page.setMaxRowCount(studentCacheDtos.size());
		Integer pageSize = page.getPageSize();
		Integer pageIndex = page.getPageIndex();
		for(int i=pageSize*(pageIndex-1);i<studentCacheDtos.size();i++){
			if(i<pageSize*pageIndex&&i>=pageSize*(pageIndex-1)){
				returnDtos.add(studentCacheDtos.get(i));
			} else {
				break;
			}
		}
		Set<String> classIds = EntityUtils.getSet(returnDtos, StudentCacheDto::getClassId);
		if(CollectionUtils.isNotEmpty(classIds)){
			List<Clazz> classList = SUtils.dt(classRemoteService.findByIdsSort(classIds.toArray(new String[classIds.size()])), new TR<List<Clazz>>(){});
			Map<String,String> classNameMap = EntityUtils.getMap(classList, Clazz::getId,Clazz::getClassNameDynamic);
			for(StudentCacheDto dto:returnDtos){
				if(classNameMap.containsKey(dto.getClassId())){
					dto.setClassName(classNameMap.get(dto.getClassId()));
				}
			}
		}
		sendPagination(request, map, page);
		map.put("wschName",wschName);
		map.put("wschNum",wschDtos.size());
		map.put("returnDtos", returnDtos);
		return "/eclasscard/attanceset/faceList.ftl";
	}
	
	@RequestMapping("/face/upload/param")
	@ControllerInfo("上传参数获取")
	public String faceUploadParam(String type,String ownerId,ModelMap map){
		String key = UuidUtils.generateUuid();
		map.put("key", key);
		map.put("ownerId", ownerId);
		map.put("type", type);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		String photoPath = ymd + "\\"+File.separator+key;
		map.put("photoPath", photoPath);
		if("1".equals(type)){
			map.put("limitNum", 100);
			map.put("tipInfo", "照片的文件名需是学号 ，");
		}else{
			map.put("tipInfo", "");
			map.put("limitNum", 1);
		}
		return "/eclasscard/attanceset/faceUploadParam.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/clear/card/cache")
	@ControllerInfo("清除缓存")
	public String clearCardCache(String cardId){
		Set<String> sids = Sets.newHashSet();
		sids.add(cardId);
		EccNeedServiceUtils.postClassClock(sids,1);
		return success("");
	}
	
	@ResponseBody
	@RequestMapping("/clearCacheByCardId")
	@ControllerInfo("清除课表缓存")
	public String clearCacheByCardId(String cardId) {
		//清除班牌今日课表、
		Set<String> sids = Sets.newHashSet();
		if(StringUtils.isNotBlank(cardId)) {
			String scheduleKey=EccCacheConstants.CACHE_SERVICE_CACHE_HEAD + cardId + ".today.schedule.list";
			RedisUtils.del(scheduleKey);
			sids.add(cardId);
		}else {
			//清除本学校所有班牌课表的缓存
			List<EccInfo> eccList = eccInfoService.findByUnitId(getLoginInfo().getUnitId());
			if(CollectionUtils.isNotEmpty(eccList)) {
				List<String> clearIds=new ArrayList<>();
				for(EccInfo e:eccList) {
					clearIds.add(EccCacheConstants.CACHE_SERVICE_CACHE_HEAD + e.getId() + ".today.schedule.list");
					sids.add(e.getId());
				}
				RedisUtils.del(clearIds.toArray(new String[0]));
			}
		}
		EccNeedServiceUtils.postClassClock(sids,1);
		return success("");
	}
	@ResponseBody
	@RequestMapping(value="/faceinfo/save",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ControllerInfo("保存人脸图片")
	public String faceinfoSave(@RequestBody String array,String ownerId){
		String objectId = UuidUtils.generateUuid();
		String unitId = getLoginInfo().getUnitId();
		try{
			List<AttFileDto> fileDtos = SUtils.dt(array,new TR<List<AttFileDto>>() {});
			String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
					"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
			String filePath = "";
			for(AttFileDto fileDto:fileDtos){
				fileDto.setObjectType(EccConstants.ECC_ATTACHMENT_TYPE);
				fileDto.setObjectId(objectId);
				fileDto.setObjectUnitId(unitId);
				filePath = fileDto.getFilePath();
			}
			String srcPath = fileSystemPath + File.separator
					+ filePath;
			File srcFile = new File(srcPath);
			Student student=SUtils.dc(studentRemoteService.findOneById(ownerId),Student.class);
			String userInfo = student==null?"未知用户":student.getStudentName();
			if(srcFile.exists()){
				List<EccUserFace> faces = eccUserFaceService.findByOwnerIds(new String[]{ownerId});
				boolean isOk = false;
				if(CollectionUtils.isNotEmpty(faces)){
					isOk =FaceUtils.updateFace(ownerId, FaceUtils.getImgStr(srcPath), userInfo);
				}else{
					isOk =FaceUtils.registerFace(unitId, ownerId, FaceUtils.getImgStr(srcPath), userInfo);
				}
				if(isOk){
					EccUserFace face = new EccUserFace();
					face.setId(objectId);
					face.setCreationTime(new Date());
					face.setModifyTime(new Date());
					face.setOwnerId(ownerId);
					face.setState(0);
					face.setFailTimes(0);
					face.setUnitId(unitId);
					Set<String> faceIds = EntityUtils.getSet(faces, EccUserFace::getId);
					if(CollectionUtils.isNotEmpty(faceIds)){
						List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(faceIds.toArray(new String[faceIds.size()])),new TR<List<Attachment>>() {});
						Set<String> attIds = EntityUtils.getSet(attachments, Attachment::getId);
						if(CollectionUtils.isNotEmpty(attIds)){
							attachmentRemoteService.deleteAttachments(attIds.toArray(new String[attIds.size()]), null);
						}
						eccUserFaceService.deleteAll(faces.toArray(new EccUserFace[faces.size()]));
					}
					List<Attachment> attachments = SUtils.dt(attachmentRemoteService.saveAttachment(SUtils.s(fileDtos)),new TR<List<Attachment>>() {});
					eccUserFaceService.save(face);
					eccFaceActivateService.updateNeedLowerUnitId(unitId);
				}else{
					return error("照片验证失败！");
				}
			}else{
				return error("保存失败！");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("");
	}

	@ResponseBody
	@RequestMapping("/faceinfo/delete")
	@ControllerInfo("删除人脸图片")
	public String faceinfoDelete(String ownerId){
		String unitId = getLoginInfo().getUnitId();
		Set<String> ownerSet = Sets.newHashSet();
		ownerSet.add(ownerId);
		try{
			eccUserFaceService.deleteByOwnerIds(ownerSet,unitId);
			eccFaceActivateService.updateNeedLowerUnitId(unitId);
		}catch (Exception e) {
			e.printStackTrace();
			return error("刪除失败！"+e.getMessage());
		}
		return success("成功");
	}

	@RequestMapping("/attence/timing/set")
	@ControllerInfo("显示定时开关机设置")
	public String timingSetIndex(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<EccTimingSet> timingSets = eccTimingSetService.findByUnitId(unitId);
		if (CollectionUtils.isNotEmpty(timingSets)) {
			
			map.put("openTime", timingSets.get(0).getOpenTime());
			map.put("closeTime", timingSets.get(0).getCloseTime());
			map.put("timingSets", timingSets);
		}
		map.put("unitId", unitId);
		return "/eclasscard/attanceset/timingSetIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/attence/timing/save")
	@ControllerInfo("保存定时开关机设置")
	public String timingSetSave(TimingSetDto timingSetDto){
		try {
			eccTimingSetService.saveTimingSet(timingSetDto);
		} catch (Exception e) {
			e.printStackTrace();
			return error("失败");
		}
		return success("成功");
	}
	
	@ResponseBody
	@RequestMapping("/face/setSave")
	@ControllerInfo("保存下发范围")
	public String faceSetSave(EccFaceSetDto dto) {
		dto.setUnitId(getLoginInfo().getUnitId());
		try {
			eccFaceSetService.saveEccFaceSetList(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return error("操作失败");
		}
		return success("操作成功");
	}
	

}
