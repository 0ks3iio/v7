package net.zdsoft.eclasscard.data.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccClockInDao;
import net.zdsoft.eclasscard.data.dto.IsBoarder;
import net.zdsoft.eclasscard.data.dto.StuClockResultDto;
import net.zdsoft.eclasscard.data.dto.StuDormInfoDto;
import net.zdsoft.eclasscard.data.dto.cache.ClassAttCacheDto;
import net.zdsoft.eclasscard.data.entity.*;
import net.zdsoft.eclasscard.data.service.*;
import net.zdsoft.eclasscard.data.utils.BaiduUtils;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;

import net.zdsoft.system.remote.service.SmsRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.*;

import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("eccClockInService")
public class EccClockInServiceImpl  extends BaseServiceImpl<EccClockIn, String>  implements EccClockInService {

	@Autowired
	private EccClockInDao eccClockInDao;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private EccDateInfoService eccDateInfoService;
	@Autowired
	private EccAttenceDormGradeService eccAttenceDormGradeService;
	@Autowired
	private EccAttenceGateGradeService eccAttenceGateGradeService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
    private GradeRemoteService gradeRemoteService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
	private EccDormAttenceService eccDormAttenceService;
	@Autowired
	private EccStudormAttenceService eccStudormAttenceService;
	@Autowired
	private EccClassAttenceService eccClassAttenceService;
	@Autowired
	private EccStuclzAttenceService eccStuclzAttenceService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private EccGateAttanceService eccGateAttanceService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private EccTeaclzAttenceService eccTeaclzAttenceService;
	@Autowired
	private EccSignInService eccSignInService;
	@Autowired
	private DingdingMsgRemoteService dingdingMsgRemoteService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private EccCacheService eccCacheService;
	@Autowired
	private EccAttenceGatePeriodService eccAttenceGatePeriodService;
	@Autowired
	private EccInOutAttanceService eccInOutAttanceService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	@Autowired
	private SmsRemoteService smsRemoteService;
	@Autowired
	private EccAttenceNoticeSetService eccAttenceNoticeSetService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	
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
	public StuClockResultDto dealClockIn(String ownerId, String cardId, String cardNumber, String objectId, Integer clockType,Integer type) {
		StringBuilder remark = new StringBuilder("cardNumber: " + cardNumber + ", Time: " + System.currentTimeMillis() + " - ");
		StuClockResultDto resultDto = new StuClockResultDto();
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		resultDto.setType(eccInfo.getType());
		resultDto.setMsg("");
		//根据卡号或Id找到学生信息
		Student student = null;
		String studentId = "";
		if (StringUtils.isNotBlank(ownerId)) {
			student = SUtils.dt(studentRemoteService.findOneById(ownerId),new TR<Student>() {});
		} else {
			student = SUtils.dt(studentRemoteService.findByCardNumber(eccInfo.getUnitId(),cardNumber),new TR<Student>() {});
		}
		Integer bussinessType=null;
		if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType()) || EccConstants.ECC_MCODE_BPYT_2.equals(eccInfo.getType())) {
			if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType()) && Objects.equals(EccConstants.ECC_CLOCK_CLASS_1, type)) {
				bussinessType=EccConstants.ECC_BUSINESS_TYPE_1;
			}else {
				bussinessType=EccConstants.ECC_BUSINESS_TYPE_0;
			}
		}
		if(student==null){
			if(!EccConstants.ECC_MCODE_BPYT_6.equals(eccInfo.getType())){
				Teacher teacher = null;
				if (StringUtils.isNotBlank(ownerId)) {
					teacher = SUtils.dt(teacherRemoteService.findOneById(ownerId),new TR<Teacher>() {});
				} else {
					teacher = SUtils.dt(teacherRemoteService.findByCardNumber(eccInfo.getUnitId(),cardNumber),new TR<Teacher>() {});
				}
				if(teacher!=null){
					return teacherClockIn(eccInfo,teacher,objectId,resultDto, clockType, remark,bussinessType);
				}
			}
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setHaveStu(false);
			if(EccConstants.ECC_MCODE_BPYT_6.equals(eccInfo.getType())){
				resultDto.setMsg("非学生卡或用户信息不存在");
			}else{
				resultDto.setMsg("用户信息不存在");
			}
			clockInRecord(studentId, EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
			return resultDto;
		} else {
			cardNumber = student.getCardNumber();
			studentId = student.getId();
		}
		User user = SUtils.dt(userRemoteService.findByOwnerId(student.getId()),new TR<User>() {});
		if(user==null){
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setHaveStu(false);
			resultDto.setMsg("学生账号未开通");
			clockInRecord(studentId, EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
			return resultDto;
		}
		resultDto.setStuUserName(user.getUsername());
		resultDto.setStuRealName(user.getRealName());
		Clazz clazz = SUtils.dt(classRemoteService.findOneById(student.getClassId()),new TR<Clazz>() {});
		if(clazz==null){
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setHaveStu(false);
			resultDto.setMsg("学生班级信息不存在");
			clockInRecord(studentId, EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
			return resultDto;
		}
		Grade grade = SUtils.dt(gradeRemoteService.findOneById(clazz.getGradeId()),new TR<Grade>() {});
		if(grade==null){
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setHaveStu(false);
			resultDto.setMsg("学生年级信息不存在");
			clockInRecord(studentId, EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
			return resultDto;
		}
		resultDto.setShowPictrueUrl(EccUtils.showPicUrl(student.getDirId(), student.getFilePath(), student.getSex()));
		resultDto.setOwnerType("1");
		resultDto.setStudentId(student.getId());
		resultDto.setRowOneName("行政班");
		resultDto.setRowOneValue(grade.getGradeName()+clazz.getClassName());
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, eccInfo.getUnitId()), Semester.class);
		if(semester == null || new Date().before(semester.getSemesterBegin())
				|| new Date().after(semester.getSemesterEnd())){
			resultDto.setHaveStu(false);
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setMsg("放假时间，未设置考勤");
			clockInRecord(studentId, EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
			return resultDto;
		}
		if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType()) && Objects.equals(EccConstants.ECC_CLOCK_CLASS_1, type)) {
			//行政班班牌-student 上下学考勤 判断是不是上下学考勤
			String eccInfoClassId = eccInfo.getClassId();
			
			if(StringUtils.isBlank(eccInfoClassId)) {
				resultDto.setHaveStu(false);
				resultDto.setStatus(StuClockResultDto.FAILED);
				resultDto.setMsg("请到相应年级班级的班牌上签到");
				clockInRecord(studentId, EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
				return resultDto;
			}
			Clazz eccInfoClass = SUtils.dt(classRemoteService.findOneById(eccInfoClassId),new TR<Clazz>() {});
			if(eccInfoClass==null || !eccInfoClass.getGradeId().equals(grade.getId())) {
				resultDto.setHaveStu(false);
				resultDto.setStatus(StuClockResultDto.FAILED);
				resultDto.setMsg("请到相应年级班级的班牌上签到");
				clockInRecord(studentId, EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
				return resultDto;
			}
			//goOrBackClockIn 包括考勤成功的
			goOrBackClockIn(eccInfo,clazz.getTeachPlaceId(),student,objectId,resultDto,clockType,remark,bussinessType);
			return resultDto;
		}else if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())||EccConstants.ECC_MCODE_BPYT_2.equals(eccInfo.getType())){
			classClockIn(eccInfo,clazz.getTeachPlaceId(),student.getId(),objectId,resultDto);
		}else if(EccConstants.ECC_MCODE_BPYT_6.equals(eccInfo.getType())){
			resultDto.setRowTwoName("类型");
			resultDto.setRowTwoValue("签到");
			resultDto.setRowThreeName("时间");
			resultDto.setRowThreeValue(DateUtils.date2StringByMinute(new Date()));
			resultDto.setStatus(StuClockResultDto.SUCCESS);
			resultDto.setClockTime(DateUtils.date2String(new Date(), "HH:mm"));
			EccSignIn eccSignIn= new EccSignIn();
			eccSignIn.setId(UuidUtils.generateUuid());
			eccSignIn.setOwnerId(student.getId());
			eccSignIn.setUnitId(eccInfo.getUnitId());
			eccSignIn.setClassId(clazz.getId());
			eccSignIn.setClockInTime(new Date());
			eccSignIn.setEccInfoId(eccInfo.getId());
			eccSignInService.save(eccSignIn);
			RedisUtils.addDataToList(EccConstants.GATE_RECENT_CLOCK_REDIS
					+ eccInfo.getId(), JSON.toJSONString(resultDto), EccConstants.GATE_RECENT_CLOCK_COUNT);
			clockInRecord(studentId, EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
			return resultDto;
		}else{
			//studentId 是否住校生
			if(getOpenApiOfficeService()==null){
				resultDto.setHaveStu(false);
				resultDto.setMsg("调用远程服务失败，请联系管理员");
				clockInRecord(studentId, EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
				return resultDto;
			}
		  	String jsonStr = stuworkRemoteService.findIsInResidence(student.getId(), eccInfo.getUnitId(), semester.getAcadyear(), semester.getSemester()+"");
		  	IsBoarder isBoarder = SUtils.dc(jsonStr,IsBoarder.class);
		  	// 当前时间是否通校
		  	Set<String> studentIds = Sets.newHashSet();
		  	if(getOpenApiOfficeService()!=null){
		  		String jsonStr4 = getOpenApiOfficeService().getStuLeaveTime(eccInfo.getUnitId(), "4", student.getId(), new Date());
		  		String state4 = EccUtils.getResultStr(jsonStr4, "state");
				if("3".equals(state4)){
					studentIds.add(student.getId());
				}
//		  		
//		  		String txjsonStr1 = getOpenApiOfficeService().getHwStuLeavesByUnitId(eccInfo.getUnitId(), clazz.getId(), "3", "2", new Date());
//		  		String txjsonStr2 = getOpenApiOfficeService().getHwStuLeavesByUnitId(eccInfo.getUnitId(), clazz.getId(), "4", null, new Date());
//		  		JSONArray txStrings1 = EccUtils.getResultArray(txjsonStr1, "studentIds");
//		  		JSONArray txStrings2 = EccUtils.getResultArray(txjsonStr2, "studentIds");
//		  		for (int i = 0; i < txStrings1.size(); i++) {
//		  			studentIds.add(txStrings1.get(i).toString());
//		  		}
//		  		for (int i = 0; i < txStrings2.size(); i++) {
//		  			studentIds.add(txStrings2.get(i).toString());
//		  		}
		  	}
		  	boolean isWalk = false;
		  	if(studentIds.contains(student.getId())){
		  		isWalk = true;
		  	}
		  	if(isBoarder==null){
		  		isBoarder = new IsBoarder();
		  	}
			if(EccConstants.ECC_MCODE_BPYT_3.equals(eccInfo.getType())){
				if(isBoarder.isInResidence()&&!isWalk){
					dormClockIn(cardId,student.getId(),eccInfo.getPlaceId(),resultDto,eccInfo.getUnitId(),semester.getAcadyear(), semester.getSemester()+"");
				}else{
					resultDto.setRowTwoName("类型");
					resultDto.setRowTwoValue("非住校生");
					resultDto.setRowThreeName("打卡位置");
					resultDto.setRowThreeValue("寝室");
					resultDto.setStatus(StuClockResultDto.FAILED);
					resultDto.setMsg("非本寝室楼学生");
				}
			}else if(EccConstants.ECC_MCODE_BPYT_4.equals(eccInfo.getType())){
				resultDto.setRowTwoName("类型");
				resultDto.setToken(BaiduUtils.getAccessToken());
				if(isBoarder.isInResidence()&&!isWalk){
					resultDto.setRowTwoValue("住校生");
				}else{
					resultDto.setRowTwoValue("通校生");
				}
				resultDto.setRowThreeName("打卡位置");
				resultDto.setRowThreeValue("进校门");
				resultDto.setStatus(StuClockResultDto.SUCCESS);
				resultDto.setMsg("，请进校");
				resultDto.setClockTime(DateUtils.date2String(new Date(), "HH:mm"));
				EccGateAttance eccGateAttance = eccGateAttanceService.findByStuId(student.getId());
				if(eccGateAttance!=null){
					eccGateAttance.setStatus(EccConstants.GATE_ATTENCE_STATUS1);
					eccGateAttance.setClockInTime(new Date());
				}else{
					eccGateAttance = new EccGateAttance();
					eccGateAttance.setId(UuidUtils.generateUuid());
					eccGateAttance.setStudentId(student.getId());
					eccGateAttance.setUnitId(eccInfo.getUnitId());
					eccGateAttance.setStatus(EccConstants.GATE_ATTENCE_STATUS1);
					eccGateAttance.setClockInTime(new Date());
				}
				eccGateAttanceService.save(eccGateAttance);
				RedisUtils.addDataToList(EccConstants.GATE_RECENT_CLOCK_REDIS
						+ cardId, JSON.toJSONString(resultDto), EccConstants.GATE_RECENT_CLOCK_COUNT);		
				if (StuClockResultDto.SUCCESS.equals(resultDto.getStatus())
						&& RedisUtils.get(EccConstants.GATE_CLOCK_IN_REDIS_MSG+student.getId()) == null) {
					JSONArray inOutArr = getInOutArr(eccInfo.getUnitId(), student.getId(), cardNumber, 2);
					setMsg(eccInfo.getUnitId(), clazz.getTeacherId(), student.getId(), student.getStudentName() + EccConstants.ECC_DINGDING_MSG_IN,inOutArr);
					RedisUtils.set(EccConstants.GATE_CLOCK_IN_REDIS_MSG+student.getId(), EccConstants.GATE_CLOCK_IN_REDIS_MSG, 5*60);
				}
			}else if(EccConstants.ECC_MCODE_BPYT_5.equals(eccInfo.getType())){
				resultDto.setRowTwoName("类型");
				resultDto.setToken(BaiduUtils.getAccessToken());
				gateOutClockIn(eccInfo.getUnitId(),grade.getId(),grade.getGradeCode(),semester,isBoarder.isInResidence()&&!isWalk,resultDto);
				if(isBoarder.isInResidence()&&!isWalk){
					resultDto.setRowTwoValue("住校生");
				}else{
					resultDto.setRowTwoValue("通校生");
				}
				boolean isLeave = false;
				boolean underReview = false;
				String beginTime = "";
				String endTime = "";
				if(!(StuClockResultDto.SUCCESS==resultDto.getStatus())&&getOpenApiOfficeService()!=null){
					//是否请假
					String jsonStr1 = getOpenApiOfficeService().getStuLeaveTime(eccInfo.getUnitId(), "1", student.getId(), new Date());
					String leave1 = EccUtils.getResultStr(jsonStr1, "isLeave");
					String state1 = EccUtils.getResultStr(jsonStr1, "state");
        			if("true".equals(leave1)){
        				isLeave = true;
    					beginTime = EccUtils.getResultStr(jsonStr1, "startDate");
    					endTime = EccUtils.getResultStr(jsonStr1, "endDate");
        			}else{
        				String jsonStr2 = getOpenApiOfficeService().getStuLeaveTime(eccInfo.getUnitId(), "2", student.getId(), new Date());
        				String leave2 = EccUtils.getResultStr(jsonStr2, "isLeave");
        				String state2 = EccUtils.getResultStr(jsonStr2, "state");
        				if("true".equals(leave2)){
        					isLeave = true;
        					beginTime = EccUtils.getResultStr(jsonStr2, "startDate");
        					endTime = EccUtils.getResultStr(jsonStr2, "endDate");
        				}else{
        					if("2".equals(state1)){
        						underReview = true;
        						beginTime = EccUtils.getResultStr(jsonStr1, "startDate");
        						endTime = EccUtils.getResultStr(jsonStr1, "endDate");
        					}else if("2".equals(state2)){
        						underReview = true;
        						beginTime = EccUtils.getResultStr(jsonStr2, "startDate");
        						endTime = EccUtils.getResultStr(jsonStr2, "endDate");
        					}else{
        						String jsonStr3 = getOpenApiOfficeService().getStuLeaveTime(eccInfo.getUnitId(), "3", student.getId(), new Date());
        						String jsonStr4 = getOpenApiOfficeService().getStuLeaveTime(eccInfo.getUnitId(), "4", student.getId(), new Date());
        						
        						String state3 = EccUtils.getResultStr(jsonStr3, "state");
        						String state4 = EccUtils.getResultStr(jsonStr4, "state");
        						if("2".equals(state3)||"2".equals(state4)){
        							resultDto.setMsg("通校申请还未批准不能出校");
        						}
        					} 
        				} 
        			} 
        			
					if(isLeave){
						//请假成功
						resultDto.setRowTwoValue("一般请假出校");
						resultDto.setStatus(StuClockResultDto.SUCCESS);
						resultDto.setMsg("，请假已批准允许出校");
					}else if(underReview){
						//审批中
						resultDto.setStatus(StuClockResultDto.FAILED);
						resultDto.setMsg("请假还未批准不能出校");
					}
				}
				if(StuClockResultDto.SUCCESS==resultDto.getStatus()){
					if(!isLeave){
						resultDto.setMsg("，允许出校");
					}
					resultDto.setClockTime(DateUtils.date2String(new Date(), "HH:mm"));
					RedisUtils.addDataToList(EccConstants.GATE_RECENT_CLOCK_REDIS
							+ cardId, JSON.toJSONString(resultDto), EccConstants.GATE_RECENT_CLOCK_COUNT);		
					EccGateAttance eccGateAttance = eccGateAttanceService.findByStuId(student.getId());
					if(eccGateAttance!=null){
						eccGateAttance.setStatus(EccConstants.GATE_ATTENCE_STATUS2);
						eccGateAttance.setClockInTime(new Date());
					}else{
						eccGateAttance = new EccGateAttance();
						eccGateAttance.setId(UuidUtils.generateUuid());
						eccGateAttance.setStudentId(student.getId());
						eccGateAttance.setUnitId(eccInfo.getUnitId());
						eccGateAttance.setStatus(EccConstants.GATE_ATTENCE_STATUS2);
						eccGateAttance.setClockInTime(new Date());
					}
					eccGateAttanceService.save(eccGateAttance);
				}
				if(isLeave||underReview){
					resultDto.setRowThreeName("请假时间");
					if(StringUtils.isNotBlank(beginTime)&&beginTime.length()>16)
						beginTime = beginTime.substring(5, 16);
					if(StringUtils.isNotBlank(endTime)&&endTime.length()>16)
						endTime = endTime.substring(5, 16);
					resultDto.setRowThreeValue(beginTime+"至"+endTime);
				}else{
					resultDto.setRowThreeName("打卡位置");
					resultDto.setRowThreeValue("出校门");
				}
				if (StuClockResultDto.SUCCESS.equals(resultDto.getStatus())
						&& RedisUtils.get(EccConstants.GATE_CLOCK_OUT_REDIS_MSG+student.getId()) == null) {
					JSONArray inOutArr = getInOutArr(eccInfo.getUnitId(), student.getId(), cardNumber, 1);
					setMsg(eccInfo.getUnitId(), clazz.getTeacherId(), student.getId(), student.getStudentName() + EccConstants.ECC_DINGDING_MSG_OUT,inOutArr);
					RedisUtils.set(EccConstants.GATE_CLOCK_OUT_REDIS_MSG+student.getId(), EccConstants.GATE_CLOCK_OUT_REDIS_MSG, 5*60);
				}
			}
		}
		clockInRecord(studentId, EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
		return resultDto;
	}

	private void clockInRecord(String ownerId, String ownerType, Integer clockType, StringBuilder remark, EccInfo eccInfo, StuClockResultDto resultDto,Integer businessType) {
		//打卡流水记录
		remark.append( System.currentTimeMillis() + ", Msg: " + resultDto.getMsg());
		EccClockIn clockIn = new EccClockIn();
		clockIn.setId(UuidUtils.generateUuid());
		if (StringUtils.isNotBlank(ownerId)) {
			clockIn.setOwnerId(ownerId);
		}
		clockIn.setOwnerType(ownerType);
		clockIn.setUnitId(eccInfo.getUnitId());
		clockIn.setEccInfoId(eccInfo.getId());
		clockIn.setType(eccInfo.getType());
		if(businessType!=null) {
			clockIn.setBusinessType(businessType);
		}
		
		if(Objects.equals(resultDto.getStatus(),StuClockResultDto.SUCCESS) || Objects.equals(resultDto.getStatus(),StuClockResultDto.WARNING_LATE)){
			clockIn.setStatus(1);
		}else{
			clockIn.setStatus(0);
		}
		clockIn.setClockType(clockType);
		clockIn.setRemark(remark.toString());
		clockIn.setClockInTime(new Date());
		save(clockIn);
	}

	private void setMsg(String unitId,String teacherId,String studentId,String contentMsg,JSONArray inOutArr) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String content=contentMsg;
//					Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
//					if(unit!=null) {
//						content=content+"【"+unit.getUnitName()+"】";
//					}
					String dingdingUserIds = "";
					Set<String> dingUserIds = Sets.newHashSet();
					List<Family> familys = SUtils.dt(familyRemoteService.findByStudentId(studentId),new TypeReference<List<Family>>() {});
					dingUserIds = EntityUtils.getSet(familys, "id");
					dingUserIds.add(teacherId);
					if(CollectionUtils.isNotEmpty(dingUserIds)){
						List<User> userList = SUtils.dt(userRemoteService.findByOwnerIds(dingUserIds.toArray(new String[0])), new TypeReference<List<User>>() {});
						for (User dingUser : userList) {
							if(StringUtils.isNotBlank(dingUser.getDingdingId())){
								dingdingUserIds += "|" + dingUser.getDingdingId();
							}
						}
						if (StringUtils.isNotBlank(dingdingUserIds)) {
							dingdingUserIds = dingdingUserIds.substring(1);
							JSONObject textJson = DdMsgUtils.toDingDingTextJson(dingdingUserIds, "", content+" "+DateUtils.date2String(new Date(),"HH:mm"));
							List<JSONObject> contextList = new ArrayList<JSONObject>();
							contextList.add(textJson);
							dingdingMsgRemoteService.addDingDingMsgs(unitId,contextList);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				EccNeedServiceUtils.sendInOutDataToWeiKe(inOutArr);
			}
		}).start();
	}

	private StuClockResultDto teacherClockIn(EccInfo eccInfo, Teacher teacher, String objectId, StuClockResultDto resultDto, Integer clockType, StringBuilder remark,Integer bussinessType) {
		User user = SUtils.dt(userRemoteService.findByOwnerId(teacher.getId()),new TR<User>() {});
		if(user==null){
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setHaveStu(false);
			resultDto.setMsg("教师账号未开通");
			clockInRecord(teacher.getId(), EccConstants.ECC_CLOCK_IN_TEACHER, clockType, remark, eccInfo, resultDto,bussinessType);
			return resultDto;
		}
		resultDto.setOwnerType("2");
		resultDto.setStuUserName(user.getUsername());
		resultDto.setStuRealName(user.getRealName());
		resultDto.setStudentId(teacher.getId());
		resultDto.setRowOneName("类型");
		resultDto.setRowOneValue("教师");
		resultDto.setShowPictrueUrl("/zdsoft/crop/doPortrait?type=big&userName="+user.getUsername());
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, eccInfo.getUnitId()), Semester.class);
		if(semester == null || new Date().before(semester.getWorkBegin())
				|| new Date().after(semester.getWorkEnd())){
			resultDto.setHaveStu(false);
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setMsg("放假时间，未设置考勤");
			clockInRecord(teacher.getId(), EccConstants.ECC_CLOCK_IN_TEACHER, clockType, remark, eccInfo, resultDto,bussinessType);
			return resultDto;
		}
		if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())||EccConstants.ECC_MCODE_BPYT_2.equals(eccInfo.getType())){
			teaClassClockIn(eccInfo.getName(),teacher.getId(),objectId,resultDto);
		}else{
			String rowTwoValue = "";
			if(EccConstants.ECC_MCODE_BPYT_3.equals(eccInfo.getType())){
				rowTwoValue = "寝室";
			}else if(EccConstants.ECC_MCODE_BPYT_4.equals(eccInfo.getType())){
				rowTwoValue = "进校门";
			}else if(EccConstants.ECC_MCODE_BPYT_5.equals(eccInfo.getType())){
				rowTwoValue = "出校门";
			}
			resultDto.setRowTwoName("打卡位置");
			resultDto.setRowTwoValue(rowTwoValue);
			resultDto.setRowThreeName("打卡时间");
			resultDto.setRowThreeValue(DateUtils.date2String(new Date(),"MM-dd HH:mm"));
			resultDto.setStatus(StuClockResultDto.SUCCESS);
		}
		clockInRecord(teacher.getId(), EccConstants.ECC_CLOCK_IN_TEACHER, clockType, remark, eccInfo, resultDto,bussinessType);
		return resultDto;
	}

	private void teaClassClockIn(String name, String id,String objectId,
			StuClockResultDto resultDto) {
		String placeId = "";
		EccClassAttence classAttence = eccClassAttenceService.findOne(objectId);
		resultDto.setRowTwoName("当前科目");
		resultDto.setRowThreeName("教室");
		if(classAttence==null){
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setMsg("本教室暂无考勤课程");
		}else{
			resultDto.setRowTwoValue(classAttence.getSubjectName());
			if(StringUtils.isNotBlank(classAttence.getPlaceId())){
				placeId = classAttence.getPlaceId();
			}else{
				Clazz clazz = SUtils.dt(classRemoteService.findOneById(classAttence.getClassId()),new TR<Clazz>() {});
				if(clazz!=null&&StringUtils.isNotBlank(clazz.getTeachPlaceId())){
					placeId = clazz.getTeachPlaceId();
				}
			}
			if(StringUtils.isNotBlank(placeId)){
				TeachPlace place = SUtils.dt(teachPlaceRemoteService.findTeachPlaceById(placeId),new TR<TeachPlace>() {});
				if(place!=null){
					resultDto.setRowThreeValue(place.getPlaceName());
				}
			}
			EccTeaclzAttence teaclzAttence  = eccTeaclzAttenceService.findByAttId(classAttence.getId());
			if(!id.equals(classAttence.getTeacherId())){
				resultDto.setStatus(StuClockResultDto.FAILED);
				resultDto.setMsg("非本教室上课老师");
			}else{
				//插入教师考勤数据
				if(EccConstants.CLASS_ATTENCE_STATUS4==teaclzAttence.getStatus()||EccConstants.CLASS_ATTENCE_STATUS2==teaclzAttence.getStatus()){
					resultDto.setStatus(StuClockResultDto.WARNING);
					resultDto.setMsg("已签到，请勿重复刷卡");
				}else{
					teaclzAttence.setClockTime(new Date());
					Date date = DateUtils.string2Date(DateUtils.date2StringByDay(new Date())+" "+classAttence.getBeginTime(), "yyyy-MM-dd HH:mm");
					int minute = getMinuteFrom2Date(date);
					if(minute>0){
						teaclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS2);
						resultDto.setStatus(StuClockResultDto.WARNING_LATE);
						resultDto.setMsg("您已迟到"+minute+"分钟");
					}else{
						teaclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS4);
						resultDto.setStatus(StuClockResultDto.SUCCESS);
					}
					resultDto.setHaveStu(true);
					eccTeaclzAttenceService.save(teaclzAttence);
				}
				
			}
		}
		
	}

	/**
	 * 处理寝室考勤
	 * @param studentId
	 * @param buildingId
	 * @param resultDto
	 */
	private void dormClockIn(String cardId, String studentId,String buildingId,
			StuClockResultDto resultDto,String unitId,String acadyear,String semester) {
		resultDto.setRowTwoName("寝室");
		resultDto.setRowThreeName("寝室楼");
		//获取学生寝室信息
		String jsonStr = stuworkRemoteService.findbuildRoomByStudentId(studentId, unitId, acadyear, semester);
		StuDormInfoDto dormInfoDto = SUtils.dc(jsonStr,StuDormInfoDto.class);
		boolean isBuildingSTu = false;
		if(dormInfoDto!=null&&buildingId.equals(dormInfoDto.getBuildingId())){
			isBuildingSTu = true;
		}
		if(dormInfoDto!=null){
			resultDto.setRowTwoValue(dormInfoDto.getRoonName());
			resultDto.setRowThreeValue(dormInfoDto.getBuildingName());
		}
		if(!isBuildingSTu){
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setMsg("非本寝室楼学生");
		}else{
			List<EccDormAttence> dormAttences = eccDormAttenceService.findListByPlaceIdNotOver(buildingId,unitId);
			Set<String> dormAttIds = EntityUtils.getSet(dormAttences, "id");
			if(!(dormAttIds.size()>0)){
				resultDto.setStatus(StuClockResultDto.FAILED);
				resultDto.setMsg("不在考勤时间段");
			}else{
				List<EccStudormAttence> studormAttences = eccStudormAttenceService.findListByIn("dormAttId", dormAttIds.toArray(new String[dormAttIds.size()]));
				EccStudormAttence attence=null;
				if(CollectionUtils.isNotEmpty(studormAttences)){
					for(EccStudormAttence studormAttence:studormAttences){
						if(studentId.equals(studormAttence.getStudentId())){
							resultDto.setStatus(StuClockResultDto.SUCCESS);
							attence = studormAttence;
							break;
						}
					}
					if(attence!=null){
						if(EccConstants.DORM_ATTENCE_STATUS3==attence.getStatus()){
							resultDto.setStatus(StuClockResultDto.WARNING);
							resultDto.setMsg("已考勤，请勿重复刷卡");
						}else{
							attence.setClockDate(new Date());
							attence.setStatus(EccConstants.DORM_ATTENCE_STATUS3);
							resultDto.setClockTime(DateUtils.date2String(attence.getClockDate(), "HH:mm"));
							eccStudormAttenceService.save(attence);
							RedisUtils.addDataToList(EccConstants.DORM_RECENT_CLOCK_REDIS
									+ cardId, JSON.toJSONString(resultDto), EccConstants.DORM_RECENT_CLOCK_COUNT);
						}
					}else{
						resultDto.setStatus(StuClockResultDto.FAILED);
						resultDto.setMsg("不在考勤时间段");
					}
				}else{
					resultDto.setStatus(StuClockResultDto.FAILED);
					resultDto.setMsg("不在考勤时间段");
				}
			}
		}
	}
	/**
	 * 处理班级上课签到
	 * @param studentId
	 * @param resultDto
	 */
	private void classClockIn(EccInfo eccInfo,String placeId,String studentId,String objectId,StuClockResultDto resultDto) {
		EccClassAttence classAttence = eccClassAttenceService.findOne(objectId);
		resultDto.setRowTwoName("当前科目");
		resultDto.setRowThreeName("教室");
		if(classAttence==null){
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setMsg("本教室暂无考勤课程");
		}else{
			if(StringUtils.isNotBlank(classAttence.getPlaceId())){
				placeId = classAttence.getPlaceId();
			}
			//大部分打卡走缓存中考勤，提高刷卡速度
			if(classClockByCache(studentId,classAttence,placeId,resultDto,eccInfo)){
				return;
			}
			//随着数据量增大，此查询方法很慢，所以上面走了一层缓存数据考勤，后面也可以根据查询条件建索引优化
			EccStuclzAttence stuclzAttence = eccStuclzAttenceService.findByStuIdClzAttId(studentId, classAttence.getId());
			if(stuclzAttence==null){
				List<EccClassAttence> classAttences = eccClassAttenceService.findListByPeriodNotOver(classAttence.getUnitId(),classAttence.getPeriodInterval(),classAttence.getPeriod(),DateUtils.date2StringByDay(new Date()));
				Set<String> classAttIds = EntityUtils.getSet(classAttences, EccClassAttence::getId);
				String classAttId = "";
				String subjectName = "暂无课程";
				String placeName = "";
				if(classAttIds.size()>0){
					List<EccStuclzAttence> eccStuclzAttences = eccStuclzAttenceService.findListByIn("classAttId", classAttIds.toArray(new String[classAttIds.size()]));
					for(EccStuclzAttence attence:eccStuclzAttences){
						if(StringUtils.isNotBlank(studentId)&&studentId.equals(attence.getStudentId())){
							classAttId = attence.getClassAttId();
							break;
						}
					}
				}
				if(StringUtils.isNotBlank(classAttId)&&!classAttId.equals(classAttence.getId())){
					for(EccClassAttence attence:classAttences){
						if(classAttId.equals(attence.getId())){
							if(StringUtils.isNotBlank(attence.getPlaceId())){
								placeId = attence.getPlaceId();
							}
							subjectName = attence.getSubjectName();
							TeachPlace place = SUtils.dt(teachPlaceRemoteService.findTeachPlaceById(placeId),new TR<TeachPlace>() {});
							if(place!=null){
								placeName = place.getPlaceName();
							}
						}
					}
				}
				resultDto.setStatus(StuClockResultDto.FAILED);
				resultDto.setRowTwoValue(subjectName);
				resultDto.setRowThreeValue(placeName);
				if(StringUtils.isNotBlank(placeName)){
					resultDto.setMsg("请到"+placeName+"签到");
				}else{
					resultDto.setMsg("无签到课程");
					ClassAttCacheDto dto = new ClassAttCacheDto();
					eccCacheService.saveStuClassAttCacheDto(studentId, dto);
				}
			}else{
				TeachPlace place = SUtils.dt(teachPlaceRemoteService.findTeachPlaceById(placeId),new TR<TeachPlace>() {});
				resultDto.setRowTwoValue(classAttence.getSubjectName());
				if(place!=null){
					resultDto.setRowThreeValue(place.getPlaceName());
				}
				if(EccConstants.CLASS_ATTENCE_STATUS4==stuclzAttence.getStatus()||EccConstants.CLASS_ATTENCE_STATUS2==stuclzAttence.getStatus()){
					resultDto.setStatus(StuClockResultDto.WARNING);
					resultDto.setMsg("已签到，请勿重复刷卡");
				}else{
					stuclzAttence.setClockDate(new Date());
					Date date = DateUtils.string2Date(DateUtils.date2StringByDay(new Date())+" "+classAttence.getBeginTime(), "yyyy-MM-dd HH:mm");
					int minute = getMinuteFrom2Date(date);
					if(minute>0){
						resultDto.setStatus(StuClockResultDto.WARNING_LATE);
						resultDto.setMsg("您已迟到"+minute+"分钟");
						stuclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS2);
					}else{
						resultDto.setStatus(StuClockResultDto.SUCCESS);
						stuclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS4);
					}
					ClassAttCacheDto dto = new ClassAttCacheDto();
					dto.setId(stuclzAttence.getId());
					dto.setPlaceName(classAttence.getSubjectName());
					dto.setSubjectName(place.getPlaceName());
					dto.setStatus(stuclzAttence.getStatus());
					dto.setAttId(classAttence.getId());
					eccCacheService.saveStuClassAttCacheDto(studentId, dto);
//					JSONArray arr = new JSONArray();影响打卡性能，暂做注释
//					JSONObject json = new JSONObject();
//					json.put("studentId", stuclzAttence.getStudentId());
//					json.put("status", resultDto.getStatus());
//					arr.add(json);
//					if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
//						List<EccInfo> eccInfos = eccInfoService.findListBy("classId", eccInfo.getClassId());
//						Set<String>  sids = EntityUtils.getSet(eccInfos, EccInfo::getId);
//						sids.remove(eccInfo.getId());
//						if(sids.size()>0){
//							EccNeedServiceUtils.postRingClassClock(new ArrayList<String>(sids), arr);
//						}
//					}else{
//						List<EccInfo> eccInfos = eccInfoService.findListBy("placeId", eccInfo.getClassId());
//						Set<String>  sids = EntityUtils.getSet(eccInfos, EccInfo::getId);
//						sids.remove(eccInfo.getId());
//						if(sids.size()>0){
//							EccNeedServiceUtils.postRingClassClock(new ArrayList<String>(sids), arr);
//						}
//					}
					eccStuclzAttenceService.save(stuclzAttence);
				}
			}
		}
	}
	
	/**
	 * 上下学考勤
	 */
	private void goOrBackClockIn(EccInfo eccInfo,String placeId,Student student,String objectId,StuClockResultDto resultDto,Integer clockType,StringBuilder remark,Integer bussinessType) {
		String unitId = eccInfo.getUnitId();
//		EccAttenceGatePeriod attence = eccAttenceGatePeriodService.findOne(objectId);
		List<EccAttenceGateGrade> gateGrade = eccCacheService.getInOutCacheByPeroidId(objectId, unitId);
		resultDto.setRowTwoName("类型");
		resultDto.setRowThreeName("签到时间");
		Date nowTime = new Date();
		if(CollectionUtils.isEmpty(gateGrade)||gateGrade.get(0)==null){
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setMsg("暂无上下学考勤");
			clockInRecord(student.getId(), EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
		}else{
			if(Objects.equals(EccConstants.GATE_ATT_IN,gateGrade.get(0).getType())){
				resultDto.setRowTwoValue("上学考勤");
			}else{
				resultDto.setRowTwoValue("放学考勤");
			}
			//上下学考勤这边暂时考虑超时打卡时间---超时5分钟
			//String beginTime = gateGrade.get(0).getBeginTime();
			String endTime = gateGrade.get(0).getEndTime();
			String nowTimeStr = DateUtils.date2String(nowTime, "HH:mm");
			//差5分钟 当作正常考勤吧
			//小时 分钟
			String[] endarr = endTime.split(":");
			String[] nowarr = nowTimeStr.split(":");
			if(Integer.parseInt(endarr[0])<Integer.parseInt(nowarr[0])) {
				//超时
				resultDto.setStatus(StuClockResultDto.FAILED);
				resultDto.setMsg("超过考勤时间");
				clockInRecord(student.getId(), EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
				return;
			}else if(Integer.parseInt(endarr[0])==Integer.parseInt(nowarr[0])){
				if(Integer.parseInt(endarr[1])+5<Integer.parseInt(nowarr[1])) {
					//超时
					resultDto.setStatus(StuClockResultDto.FAILED);
					resultDto.setMsg("超过考勤时间");
					clockInRecord(student.getId(), EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
					return;
				}
			}
			
			//根据attence studentId是否已经有考勤
			EccInOutAttance eccInOutAttance=eccInOutAttanceService.findByPeriodIdAndStudentIdToDay(unitId,objectId,student.getId(),new Date());
			if(eccInOutAttance!=null) {
				if(EccConstants.CLASS_ATTENCE_STATUS4==eccInOutAttance.getStatus()){
					resultDto.setStatus(StuClockResultDto.WARNING);
					resultDto.setMsg("已签到，请勿重复刷卡");
					if(eccInOutAttance.getClockDate()!=null){
						resultDto.setRowThreeValue(DateUtils.date2StringBySecond(eccInOutAttance.getClockDate()));
					}
					clockInRecord(student.getId(), EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
					return;
				}
			}else {
				eccInOutAttance=new EccInOutAttance();
				eccInOutAttance.setId(UuidUtils.generateUuid());
				eccInOutAttance.setUnitId(unitId);
				eccInOutAttance.setStudentId(student.getId());
				eccInOutAttance.setPeriodId(objectId);
			}
			eccInOutAttance.setClassId(student.getClassId());
			eccInOutAttance.setStatus(EccConstants.CLASS_ATTENCE_STATUS4);
			eccInOutAttance.setEccInfoId(eccInfo.getId());
			eccInOutAttance.setClockDate(nowTime);
			eccInOutAttance.setType(gateGrade.get(0).getType());
			eccInOutAttanceService.save(eccInOutAttance);
			//通知数据，根据开关发消息或短信，写日志
			resultDto.setStatus(StuClockResultDto.SUCCESS);
			resultDto.setRowThreeValue(DateUtils.date2StringBySecond(eccInOutAttance.getClockDate()));
			clockInRecord(student.getId(), EccConstants.ECC_CLOCK_IN_STUDENT, clockType, remark, eccInfo, resultDto,bussinessType);
			if(!student.getClassId().equals(eccInfo.getClassId())) {
				List<String> cardList=new ArrayList<String>();
				List<EccInfo> eccList = eccInfoService.findByClassIdIn(new String[] {student.getClassId()});
				if(CollectionUtils.isNotEmpty(eccList)) {
					cardList.addAll(EntityUtils.getList(eccList, e->e.getId()));
					JSONArray arr = new JSONArray();
					JSONObject json = new JSONObject();
					json.put("studentId", student.getId());
					json.put("status", StuClockResultDto.SUCCESS);
					arr.add(json);
					EccNeedServiceUtils.postInoutClock(cardList, arr);
				}else {
					System.out.println("上下学考勤数据错误"+student.getClassId());
				}
				
			}
			//开关控制
			boolean isSendSms=false;
			boolean isSendWeike=false;
			
			EccAttenceNoticeSet noticeSet = eccAttenceNoticeSetService.findByUnitIdAndType(unitId, EccConstants.IN_OUT_ATTENCE_SET_TYPE3);
			if(noticeSet!=null && noticeSet.isSend() && noticeSet.isSendParentMaster()) {
				isSendSms=true;
				isSendWeike=true;
			}
			sendInOutDateToFamilys(student, eccInOutAttance.getClockDate(), eccInOutAttance.getType(), isSendSms, isSendWeike);
		}
	}
	
	private boolean classClockByCache(String studentId,
			EccClassAttence classAttence, String placeId,
			StuClockResultDto resultDto,EccInfo eccInfo) {
		ClassAttCacheDto dto = eccCacheService.getStuClassAttCacheDto(studentId);
		if(dto == null){
			return false;
		}
		if(StringUtils.isBlank(classAttence.getId()) ){
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setMsg("无签到课程");
			return true;
		}
		if(StringUtils.isNotBlank(classAttence.getId()) && classAttence.getId().equals(dto.getAttId())){
			resultDto.setRowTwoValue(dto.getSubjectName());
			resultDto.setRowThreeValue(dto.getPlaceName());
			if(EccConstants.CLASS_ATTENCE_STATUS4==dto.getStatus()||EccConstants.CLASS_ATTENCE_STATUS2==dto.getStatus()){
				resultDto.setStatus(StuClockResultDto.WARNING);
				resultDto.setMsg("已签到，请勿重复刷卡");
			}else{
				Date date = DateUtils.string2Date(DateUtils.date2StringByDay(new Date())+" "+classAttence.getBeginTime(), "yyyy-MM-dd HH:mm");
				int minute = getMinuteFrom2Date(date);
				if(minute>0){
					resultDto.setStatus(StuClockResultDto.WARNING_LATE);
					resultDto.setMsg("您已迟到"+minute+"分钟");
					dto.setStatus(EccConstants.CLASS_ATTENCE_STATUS2);
				}else{
					resultDto.setStatus(StuClockResultDto.SUCCESS);
					dto.setStatus(EccConstants.CLASS_ATTENCE_STATUS4);
				}
				eccCacheService.saveStuClassAttCacheDto(studentId, dto);
				EccStuclzAttence stuclzAttence = eccStuclzAttenceService.findOne(dto.getId());
				stuclzAttence.setClockDate(new Date());
				stuclzAttence.setStatus(dto.getStatus());
				eccStuclzAttenceService.save(stuclzAttence);
			}
			return true;
		}else{
			resultDto.setStatus(StuClockResultDto.FAILED);
			resultDto.setRowTwoValue(dto.getSubjectName());
			resultDto.setMsg("请到"+dto.getPlaceName()+"签到");
			return true;
		}
	}

	//	private void dormClockIn(String unitId,String gradeId,String gradeCode,Semester semester) {
//		//1.是否该住宿楼学生，是2，否6
//		//2.学生所在年级今天是否补课日，是4,否3
//		Calendar calendar = Calendar.getInstance();
//		EccDateInfo eccDateInfo = eccDateInfoService.getByDateGrade(gradeId, DateUtils.date2StringByDay(calendar.getTime()));
//		//3.学生所在年级今天是否上课日，是4，否5
////		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
//        DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
//        calendar.add(Calendar.DATE, 1);
//        EccDateInfo eccDateInfo2 = eccDateInfoService.getByDateGrade(gradeId, DateUtils.date2StringByDay(calendar.getTime()));
//        DateInfo dateInfo2 = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
//        boolean inTime = false;
//        String time = DateUtils.date2String(calendar.getTime(), "HH:mm");
//        if(eccDateInfo!=null||dateInfo.getIsFeast()!="1"){//上课日
//        	if(eccDateInfo2==null && dateInfo2.getIsFeast()=="1"){//第二天休息
//        		inTime = eccAttenceDormGradeService.findByBettwenTime(unitId, EccConstants.DORM_ATTENCE_PERIOD_TYPE1, gradeCode, time,true);
//        	}else{
//        		inTime = eccAttenceDormGradeService.findByBettwenTime(unitId, EccConstants.DORM_ATTENCE_PERIOD_TYPE1, gradeCode, time,false);
//        	}
//        	if(inTime){
////        		eccDormAttenceService.save(t);
//        		//更新打卡记录
//        	}else{
//        		promptMsg("非考勤时间");
//        	}
//        }else{//放假
//        	if(eccDateInfo2 != null || dateInfo2.getIsFeast()=="1"){//收假
//        		inTime = eccAttenceDormGradeService.findByBettwenTime(unitId, EccConstants.DORM_ATTENCE_PERIOD_TYPE2, gradeCode, time,false);
//    		}
//        	if(inTime){
//        		//更新打卡记录
//        	}else{
//        		promptMsg("非考勤时间");
//        	}
//        	
//        }
//		
//	}
//	
	/**处理住校生.通校生出校门打卡
	 * 
	 * @param unitId
	 * @param gradeId
	 * @param gradeCode
	 * @param semester
	 * @param resultDto
	 */
	
	private void gateOutClockIn(String unitId,String gradeId,String gradeCode,Semester semester,boolean isBoarder,StuClockResultDto resultDto) {
		Calendar calendar = Calendar.getInstance();
		String time = DateUtils.date2String(calendar.getTime(), "HH:mm");
		boolean inTime = false;
		inTime = eccAttenceGateGradeService.findByBettwenTime(unitId, EccConstants.ECC_CLASSIFY_1,EccConstants.GATE_ATTENCE_PERIOD_TYPE3, gradeCode, time);
		if(inTime){//临时离校
			resultDto.setStatus(StuClockResultDto.SUCCESS);
		}else{
			EccDateInfo eccDateInfo = eccDateInfoService.getByDateGrade(gradeId, DateUtils.date2StringByDay(calendar.getTime()));
			DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
			calendar.add(Calendar.DATE, 1);
			EccDateInfo eccDateInfo2 = eccDateInfoService.getByDateGrade(gradeId, DateUtils.date2StringByDay(calendar.getTime()));
			DateInfo dateInfo2 = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
			
			if(eccDateInfo!=null||(dateInfo!=null && "N".equals(dateInfo.getIsFeast()))){//上课日
				if(isBoarder){
					if(eccDateInfo2==null && "Y".equals(dateInfo2.getIsFeast())){//明天是放假
						inTime = eccAttenceGateGradeService.findByBettwenTime(unitId,EccConstants.ECC_CLASSIFY_1, EccConstants.GATE_ATTENCE_PERIOD_TYPE1, gradeCode, time);
						if(inTime){
							resultDto.setStatus(StuClockResultDto.SUCCESS);
						}else{
							resultDto.setMsg("没有请假记录不能出校");
							resultDto.setStatus(StuClockResultDto.FAILED);
						}
					}else{
						resultDto.setMsg("没有请假记录不能出校");
						resultDto.setStatus(StuClockResultDto.FAILED);
					}
				}else{
					inTime = eccAttenceGateGradeService.findByBettwenTime(unitId, EccConstants.ECC_CLASSIFY_1,EccConstants.GATE_ATTENCE_PERIOD_TYPE4, gradeCode, time);
					if(inTime){
						resultDto.setStatus(StuClockResultDto.SUCCESS);
					}else{
						resultDto.setMsg("未到通校生出校时间");
						resultDto.setStatus(StuClockResultDto.FAILED);
					}
				}
			}else{//休假日
				inTime = eccAttenceGateGradeService.findByBettwenTime(unitId, EccConstants.ECC_CLASSIFY_1,EccConstants.GATE_ATTENCE_PERIOD_TYPE2, gradeCode, time);
				if(inTime){
					resultDto.setStatus(StuClockResultDto.SUCCESS);
				}else{
					resultDto.setMsg("没有请假记录不能出校");
					resultDto.setStatus(StuClockResultDto.FAILED);
				}
			}
		}
	}

	@Override
	protected BaseJpaRepositoryDao<EccClockIn, String> getJpaDao() {
		return eccClockInDao;
	}

	@Override
	protected Class<EccClockIn> getEntityClass() {
		return EccClockIn.class;
	}

	@Override
	public List<EccClockIn> findListGateRecently(final String eccInfoId) {
		Specification<EccClockIn> specification = new Specification<EccClockIn>() {
            @Override
            public Predicate toPredicate(Root<EccClockIn> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();

                if (StringUtils.isNotBlank(eccInfoId)) {
                	ps.add(cb.equal(root.get("eccInfoId").as(String.class), eccInfoId));
                }
                ps.add(cb.equal(root.get("status").as(String.class), 1));
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.desc(root.get("clockInTime").as(Date.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        Pagination page = new Pagination(1, 3, false);
        Pageable pageable = Pagination.toPageable(page);
        Page<EccClockIn> findAll = eccClockInDao.findAll(specification, pageable);
        page.setMaxRowCount((int) findAll.getTotalElements());
        return findAll.getContent();
	}

	private int getMinuteFrom2Date(Date date){
		if(date==null){
			date = new Date();
		}
		Calendar dateOne=Calendar.getInstance(),dateTwo=Calendar.getInstance();
		dateOne.setTime(new Date());	//设置为当前系统时间 
		dateTwo.setTime(date);			//之前时间
		long timeOne=dateOne.getTimeInMillis();
		long timeTwo=dateTwo.getTimeInMillis();
		int minute=(int) ((timeOne-timeTwo)/(1000*60));//转化minute
		return minute;
	}
	
	private JSONArray getInOutArr(String unitId,String studentId,String cardNumber,int inOut){
		JSONArray array = new JSONArray();
		JSONObject wjson = new JSONObject();
		wjson.put("schoolId", unitId);
		wjson.put("userId", studentId);
		wjson.put("cardNumber", cardNumber);
		wjson.put("ioType", inOut);
		wjson.put("time", new Date().getTime());
		wjson.put("step", 0);
		wjson.put("calorie", 0);//微课  千卡
		wjson.put("distance", 0);//微课 公里
		array.add(wjson);
		return array;
	}

	@Override
	public List<EccClockIn> findListByAll(final String unitId, final String ownerType, final Date startTime,
			final Date endTime, final String type, final String ownerId, Pagination page) {
		Specification<EccClockIn> specification = new Specification<EccClockIn>() {
			@Override
			public Predicate toPredicate(Root<EccClockIn> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				ps.add(cb.equal(root.get("ownerType").as(String.class), ownerType));
                if(null!=startTime){
                	ps.add(cb.greaterThanOrEqualTo(root.<Date>get("clockInTime"), startTime));
                }
                if(null!=endTime){
                	//endTime 时分秒都是0
                	//加一天 小于1分
                	Date endTime2 = DateUtils.addDay(endTime, 1);
                	ps.add(cb.lessThan(root.<Date>get("clockInTime"), endTime2));
                }
                if(StringUtils.isNotBlank(type)){
                	ps.add(cb.equal(root.get("type").as(String.class), type));
                }
                if(StringUtils.isNotBlank(ownerId)){
                	ps.add(cb.equal(root.get("ownerId").as(String.class), ownerId));
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.desc(root.<Date>get("clockInTime")));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }			
		};
		if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<EccClockIn> findAll = eccClockInDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            return findAll.getContent();
        }
        else {
            return eccClockInDao.findAll(specification);
        }
	}
	
	
	
	public void sendInOutDateToFamilys(Student student,Date date,Integer type,boolean isSendSms,boolean isSendWeike) {
		//XXX学生在XX点XX分XX秒签到考勤成功（{上学考勤或放学考勤}）
		//XXX学生在XXXX年XX月XX日XX点XX分于班牌设备上签到考勤成功（上/方学考勤）【XXX学校】
		//您的孩子{姓名}在XXX年XX月XX日XX点XX分XX秒于班牌设备签到考勤成功（{上学考勤/放学考勤}） 【XX 学校单位名称】
		new Thread(new Runnable() {
			@Override
			public void run() {
				Unit unit = SUtils.dc(unitRemoteService.findOneById(student.getSchoolId()), Unit.class);
				
				String typeName="";
				if(Objects.equals(EccConstants.GATE_ATTENCE_STATUS1, type)) {
					typeName="上学考勤";
				}else {
					typeName="放学考勤";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
				
				String message="您的孩子"+student.getStudentName()+"在"+ sdf.format(date)+"于班牌设备上签到考勤成功（"+typeName+"）";
				if(unit!=null) {
					message=message+"【"+unit.getUnitName()+"】";
				}
				
				List<Family> familys = SUtils.dt(familyRemoteService.findByStudentId(student.getId()),new TypeReference<List<Family>>() {});
				if(CollectionUtils.isEmpty(familys)) {
					System.out.println(student.getId()+"未找到家长信息:"+message);
					return;
				}
				
				if(isSendSms) {
					Set<String> phoneset=new HashSet<>();
					if(CollectionUtils.isNotEmpty(familys)){
						//直接获取base_family下的手机号码
						for(Family f:familys) {
							if(StringUtils.isNotBlank(f.getMobilePhone())) {
								phoneset.add(f.getMobilePhone());
							}
						}
					}
					sendInOutDataToSms(message, phoneset,unit.getId());
				} 
				if(isSendWeike) {
					List<String> ownerIds = EntityUtils.getList(familys,e->e.getId());
					List<User> userList = SUtils.dt(userRemoteService.findByOwnerIds(ownerIds.toArray(new String[0] )), User.class);
					if(CollectionUtils.isEmpty(userList)){
						System.out.println(student.getId()+"未找到家长对应的用户信息:"+message);
						return;
					}
					String title = "您收到一条"+student.getStudentName()+"的"+typeName+"考勤信息";
					Set<String> userIds =EntityUtils.getSet(userList, e->e.getId());
					sendInOutDataToWeike(title, message, userIds);
				}
			}
		}).start();
	}
	private  void sendInOutDataToSms(String message,Set<String> phoneset,String unitId){
		try {
			if(CollectionUtils.isEmpty(phoneset)) {
				System.out.println("未找到家长信息或者家长属性手机号码:"+message);
			}else {
				if(CollectionUtils.isNotEmpty(phoneset)) {
					String sms=smsRemoteService.sendSmsByUnitId(phoneset.toArray(new String[0]), message, null, unitId);
					System.out.println("发送短信结果："+sms);
				}
			}
		}catch (Exception e) {
			System.out.println("发送短信报错:"+message);
			e.printStackTrace();
		}
	}
	
	public  void sendInOutDataToWeike(String title,String message,Set<String> userIds){
		JSONArray msgarr = new JSONArray();
		String[] rowsContent = new String[] { message };
		JSONArray userIdsArr = new JSONArray();
		for(String id:userIds){
			userIdsArr.add(id);
		}
		JSONObject msg = new JSONObject();
		msg.put("userIdArray", userIds);
		msg.put("msgTitle", title);
		msg.put("jumpType", "0");
		msg.put("url", "");//不需要跳转
		msg.put("rowsContent", rowsContent);
		msgarr.add(msg);
		try {
			if(getOpenApiOfficeService()!=null && msgarr.size()>0){
				getOpenApiOfficeService().pushWeikeMessage("LEAVE_WORD", msgarr.toJSONString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}
	
	
	
}
