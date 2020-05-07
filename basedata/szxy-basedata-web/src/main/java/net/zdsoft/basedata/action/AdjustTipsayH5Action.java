package net.zdsoft.basedata.action;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.dto.AdjustedDto;
import net.zdsoft.basedata.dto.TeachGroupDto;
import net.zdsoft.basedata.dto.TipsaySaveDto;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.service.*;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.constant.TipsayConstants;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.TeacherDto;
import net.zdsoft.basedata.dto.TipsayDto;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.WeiKeyUtils;

import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 手机端 调代
 * 
 */
@Controller
@RequestMapping("/mobile/open/adjusttipsay")
public class AdjustTipsayH5Action {

	@Autowired
	UserRemoteService userRemoteService;
	@Autowired
	CustomRoleRemoteService customRoleRemoteService;
	@Autowired
	SemesterService semesterService;
	@Autowired
	TipsayService tipsayService;
	@Autowired
	TipsayExService tipsayExService;
	@Autowired
	AdjustedService adjustedService;
	@Autowired
	DateInfoService dateInfoService;
	@Autowired
	TeacherService teacherService;
	@Autowired
	GradeService gradeService;
	@Autowired
	CourseScheduleService courseScheduleService;
	@Autowired
	TeachGroupService teachGroupService;
	@Autowired
    AdjustedDetailService adjustedDetailService;
	@Autowired
    CourseService courseService;
	@Autowired
    TeachClassService teachClassService;
	@Autowired
    ClassService classService;
	
	/**
	 * 判断用户是不是教务管理员
	 * @param userId
	 * @return
	 */
	public boolean isAdmin(String unitId,String userId) {
		return customRoleRemoteService.checkUserRole(unitId, TipsayConstants.SUBSYSTEM_86, TipsayConstants.EDUCATION_CODE, userId);
	}
	
	@RequestMapping("/indexModel/page")
	@ControllerInfo("调代管首页")
	public String showModelIndex(String token, ModelMap map) {
		User user = null;
		String ownerId = "";
		//ownerId="3DD772EB68FB4A02A0C70D3F325A6B8B";
        //ownerId="66F4CB34939E487192535283988528EE";

		try {
			if (StringUtils.isNotBlank(token))
				ownerId = WeiKeyUtils.decodeByDes(token);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("nonemess", "token解析出错了，请确认参数是否正确。");
			return "/basedata/tipsayh5/none.ftl";
		}

		if (StringUtils.isNotBlank(ownerId))
			user = User.dc(userRemoteService.findByOwnerId(ownerId));

		if (user == null) {
			map.put("nonemess", "user对象不存在，请确认参数是否正确(" + ownerId + ")。");
			return "/basedata/tipsayh5/none.ftl";
		}
		if (user.getOwnerType() != User.OWNER_TYPE_TEACHER) {
			map.put("nonemess", "user对象不是教师，请确认参数是否正确。");
			return "/basedata/tipsayh5/none.ftl";
		}
		return showModelList(user.getUnitId(), user.getId(), ownerId, map);
	}
	@RequestMapping("/modelList/page")
	@ControllerInfo("调代课模块列表")
	public String showModelList(String unitId, String userId,  String teacherId,ModelMap map) {
		if(StringUtils.isBlank(userId)) {
			User user = User.dc(userRemoteService.findByOwnerId(teacherId));
			if(user==null) {
				map.put("nonemess", "user对象不存在，请确认参数是否正确(" + teacherId + ")。");
				return "/basedata/tipsayh5/none.ftl";
			}
			userId=user.getId();
			unitId=user.getUnitId();
		}
		boolean isAdmin=isAdmin(unitId, userId);
		String adminType="0";//非管理员
		if(isAdmin) {
			adminType="1";//管理员
		}
		map.put("adminType", adminType);
		//当前学年学期
		Semester semesterObj = semesterService.getCurrentSemester(2);
		if(semesterObj==null) {
			map.put("nonemess", "当前学年学期未找到，请联系管理员。");
			return "/basedata/tipsayh5/none.ftl";
		}
		//String[]: 待审核数量， 已审核数量或者本月已审核，最近一条状态 0:不展现  1:通过 2:不通过
		Map<String,String[] > returnMap=new HashMap<>();
		returnMap.put(TipsayConstants.TYPE_3, new String[] {"0","0","0"});
		returnMap.put(TipsayConstants.TYPE_1, new String[] {"0","0","0"});
		returnMap.put(TipsayConstants.TYPE_2, new String[] {"0","0","0"});
		if("0".equals(adminType)) {
			//代管课数据
			/**
			 * 取得审核中的数据
			 * 已审核的数据
			 * 取得审核后 根据修改时间排序
			 **/
			List<Tipsay> list =tipsayService.findTipsayListWithMaster(unitId,semesterObj.getAcadyear(),semesterObj.getSemester(), null, teacherId);
			if(CollectionUtils.isNotEmpty(list)) {
				Map<String, List<Tipsay>> listMap = EntityUtils.getListMap(list,Tipsay::getType , e->e);
				for(Entry<String, List<Tipsay>>  item:listMap.entrySet()) {
					String key=item.getKey();
					List<Tipsay> list_1 = item.getValue();
					if(CollectionUtils.isNotEmpty(list_1)) {
						
						//根据修改时间排序
						int ingNum=0;//待审核
						int edNum=0;//已审核
						Date modifyTime=null;
						Tipsay chooseTipsay=null;
						for(Tipsay t:list_1) {
							if(teacherId.equals(t.getNewTeacherId())) {
								continue;
							}
							if(TipsayConstants.TIPSAY_STATE_1.equals(t.getState())
									|| TipsayConstants.TIPSAY_STATE_2.equals(t.getState())) {
								edNum++;
								Date temp=t.getModifyTime()==null?t.getCreationTime():t.getModifyTime();
								if(modifyTime==null) {
									modifyTime=temp;
									chooseTipsay=t;
								}else {
									if(DateUtils.compareIgnoreSecond(temp, modifyTime)>0) {
										modifyTime=temp;
										chooseTipsay=t;
									}
								}
							}else {
								ingNum++;
							}
						}
						String[] arr = returnMap.get(key);
						arr[0]=String.valueOf(ingNum);
						arr[1]=String.valueOf(edNum);
						if(chooseTipsay!=null) {
							arr[2]=chooseTipsay.getState();
						}
						
					}
				}

			}
			//调课数据
			List<Adjusted> adjustedList = adjustedService.findListByTeacherIdAndWeek(semesterObj.getAcadyear(),String.valueOf(semesterObj.getSemester()), teacherId, null);
			if(CollectionUtils.isNotEmpty(list)) {
				int ingNum=0;//待审核
				int edNum=0;//已审核
				Date modifyTime=null;
				Adjusted chooseAdjusted=null;
				String[] arr = returnMap.get(TipsayConstants.TYPE_3);
				for(Adjusted t:adjustedList) {
					if(TipsayConstants.TIPSAY_STATE_1.equals(t.getState())
							|| TipsayConstants.TIPSAY_STATE_2.equals(t.getState())) {
						edNum++;
						Date temp=t.getModifyTime()==null?t.getCreationTime():t.getModifyTime();
						if(modifyTime==null) {
							modifyTime=temp;
							chooseAdjusted=t;
						}else {
							if(DateUtils.compareIgnoreSecond(temp, modifyTime)>0) {
								modifyTime=temp;
								chooseAdjusted=t;
							}
						}
					}else {
						ingNum++;
					}
				}
				arr[0]=String.valueOf(ingNum);
				arr[1]=String.valueOf(edNum);
				if(chooseAdjusted!=null) {
					arr[2]=chooseAdjusted.getState();
				}
			}
		}else {
			//所有数据
			List<Tipsay> list =tipsayService.findTipsayListWithMaster(unitId,semesterObj.getAcadyear(),semesterObj.getSemester(), null, null);
			//待审核 --0
			//本月已审核---区分管理员
			Date nowDate=new Date();
			String yearMonth=DateUtils.date2String(nowDate,"yyyy-MM");
			if(CollectionUtils.isNotEmpty(list)) {
			    Set<String> idSet=list.stream().filter(e->e.getState().equals(TipsayConstants.TIPSAY_STATE_1) || e.getState().equals(TipsayConstants.TIPSAY_STATE_2)).
			    map(Tipsay::getId).collect(Collectors.toSet());
			    Set<String> tipsayIdInteacherIdSet=new HashSet<>();
			    if(CollectionUtils.isNotEmpty(idSet)) {
					List<TipsayEx> exList = tipsayExService.findListByIn("tipsayId", idSet.toArray(new String[] {}));
					if(CollectionUtils.isNotEmpty(exList)) {
						List<TipsayEx> exList2 = exList.stream().filter(e->e.getAuditorId().equals(teacherId)).collect(Collectors.toList());
						if(CollectionUtils.isNotEmpty(exList2)) {
							tipsayIdInteacherIdSet=EntityUtils.getSet(exList2,e->e.getTipsayId());
						}
					}
				}
				
				
				Map<String, List<Tipsay>> listMap = EntityUtils.getListMap(list,Tipsay::getType , e->e);
				
				for(Entry<String, List<Tipsay>>  item:listMap.entrySet()) {
					String key=item.getKey();
					List<Tipsay> list_1 = item.getValue();
					if(CollectionUtils.isNotEmpty(list_1)) {
						int ingNum=0;//待审核
						int edNum=0;//本月已审核
						for(Tipsay t:list_1) {
							if(TipsayConstants.TIPSAY_STATE_1.equals(t.getState())
									|| TipsayConstants.TIPSAY_STATE_2.equals(t.getState())) {
								//这个记录是不是改老师提供审核的
								if(!tipsayIdInteacherIdSet.contains(t.getId())) {
									continue;
								}
								Date temp=t.getModifyTime()==null?t.getCreationTime():t.getModifyTime();
								String tempStr=DateUtils.date2String(temp,"yyyy-MM");
								if(yearMonth.equals(tempStr)) {
									edNum++;
								}
							}else {
								ingNum++;
							}
						}
						String[] arr = returnMap.get(key);
						arr[0]=String.valueOf(ingNum);
						arr[1]=String.valueOf(edNum);
					}
				}

			}
			
			//调课接口
			List<Adjusted> adjustedList = adjustedService.findListBySchoolIdAndWeek(semesterObj.getAcadyear(),String.valueOf(semesterObj.getSemester()), unitId, null);
			if(CollectionUtils.isNotEmpty(adjustedList)) {
				Set<String> idSet=adjustedList.stream().filter(e->TipsayConstants.TIPSAY_STATE_1.equals(e.getState())
						|| TipsayConstants.TIPSAY_STATE_2.equals(e.getState())).map(Adjusted::getId).collect(Collectors.toSet());
			    Set<String> adjustedIdInteacherIdSet=new HashSet<>();
			    if(CollectionUtils.isNotEmpty(idSet)) {
					List<TipsayEx> exList = tipsayExService.findListByIn("tipsayId", idSet.toArray(new String[] {}));
					if(CollectionUtils.isNotEmpty(exList)) {
						List<TipsayEx> exList2 = exList.stream().filter(e->e.getAuditorId().equals(teacherId)).collect(Collectors.toList());
						if(CollectionUtils.isNotEmpty(exList2)) {
							adjustedIdInteacherIdSet=EntityUtils.getSet(exList2,e->e.getTipsayId());
						}
					}
				}
				
				int ingNum=0;//待审核
				int edNum=0;//本月已审核
				for(Adjusted t:adjustedList) {
					if(TipsayConstants.TIPSAY_STATE_1.equals(t.getState())
							|| TipsayConstants.TIPSAY_STATE_2.equals(t.getState())) {
						//这个记录是不是改老师提供审核的
						if(!adjustedIdInteacherIdSet.contains(t.getId())) {
							continue;
						}
						Date temp=t.getModifyTime()==null?t.getCreationTime():t.getModifyTime();
						String tempStr=DateUtils.date2String(temp,"yyyy-MM");
						if(yearMonth.equals(tempStr)) {
							edNum++;
						}
					}else {
						ingNum++;
					}
				}
				String[] arr = returnMap.get(TipsayConstants.TYPE_3);
				arr[0]=String.valueOf(ingNum);
				arr[1]=String.valueOf(edNum);
			}
		
		}
		map.put("showMap", returnMap);
		map.put("unitId", unitId);
		map.put("userId", userId);
		map.put("teacherId", teacherId);
		return "/basedata/tipsayh5/index.ftl";
	}
	
	@RequestMapping("/showList/page")
	@ControllerInfo("进入列表")
	public String showList(String unitId, String userId, String teacherId,String adminType,String type,
			String acadyear,String semester,String week,String statType, String from, ModelMap map) {
		if("3".equals(type)) {
			//调课
			return showSwitchHead(unitId, userId, teacherId, adminType, type, acadyear, semester, week, from, map);
		}else if("1".equals(type) || "2".equals(type)){
			return showTipsayHead(unitId, userId, teacherId, adminType, type, acadyear, semester, week,statType, map);
		}else {
			map.put("nonemess", "参数丢失。");
			return "/basedata/tipsayh5/none.ftl";
		}
	}

    public String showSwitchHead(String unitId, String userId, String teacherId, String adminType, String type, String acadyear, String semester, String week, String from, ModelMap map) {
       //暂时默认 statType=null
    	showSearchHead(unitId, acadyear, semester, week,null, map);
        map.put("unitId", unitId);
        map.put("userId", userId);
        map.put("teacherId", teacherId);
        map.put("adminType", adminType);
        map.put("type", type);
        map.put("from", from);
        if ("0".equals(adminType)) {
            return "basedata/teacherClassSwitchH5/switchHead.ftl";
        } else if ("1".equals(adminType)) {
            return "basedata/teacherClassSwitchH5/switchManageHead.ftl";
        } else {
            map.put("nonemess", "参数丢失。");
            return "/basedata/tipsayh5/none.ftl";
        }
    }

    @RequestMapping("/switch/list/page")
    public String showSwitchList(String unitId, String teacherId, String adminType, String acadyear, String semester, String week, ModelMap map) {
        map.put("unitId", unitId);
        map.put("teacherId", teacherId);
        map.put("adminType", adminType);
        List<AdjustedDto> returnList = new ArrayList<>();
        List<Adjusted> adjustedList = adjustedService.findListByTeacherIdAndWeek(acadyear, semester, teacherId, week);
        if (CollectionUtils.isEmpty(adjustedList)) {
            map.put("resultList", returnList);
            return "basedata/teacherClassSwitchH5/switchList.ftl";
        }
        List<String> adjustedIds = EntityUtils.getList(adjustedList, Adjusted::getId);
        List<AdjustedDetail> adjustedDetailList = adjustedDetailService.findListByAdjustedIds(adjustedIds.toArray(new String[0]));
        returnList = makeAdjustDtoList(adjustedList, adjustedDetailList);
        map.put("resultList", returnList);
	    return "basedata/teacherClassSwitchH5/switchList.ftl";
    }

    @RequestMapping("/switch/apply")
    public String switchApply(String unitId, String userId, String teacherId, String adminType, String acadyear, String semester, ModelMap map) {
        boolean isAdmin = isAdmin(unitId, userId);
        map.put("isAdmin", isAdmin);
	    Teacher teacher = teacherService.findOne(teacherId);
	    map.put("unitId", unitId);
	    map.put("userId", userId);
	    map.put("teacherId", teacherId);
        map.put("adminType", adminType);
        map.put("acadyear", acadyear);
        map.put("semester", semester);
        map.put("teacherName", teacher.getTeacherName());
        List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(unitId, acadyear, Integer.valueOf(semester));
        if (CollectionUtils.isEmpty(dateInfoList)) {
            map.put("nonemess", "周次为空，请先维护节假日设置");
            return "/basedata/tipsayh5/none.ftl";
        }
        // 获取当前周次信息
		Integer maxWeek = Integer.valueOf(1);
        List<DateInfo> weekInfoList = new ArrayList<>();
        DateInfo nowWeek;
        DateInfo tmp = null;
        Date today = new Date();
        int index = 1;
        for (DateInfo one : dateInfoList) {
            if (Integer.valueOf(index).equals(one.getWeek())) {
            	tmp = one;
                if (today.equals(one.getInfoDate()) || today.before(one.getInfoDate())) {
                    weekInfoList.add(one);
                } else if (today.getTime() - one.getInfoDate().getTime() > 0L && today.getTime() - one.getInfoDate().getTime() < 1000 * 60 * 60 * 24 * 7L) {
                    weekInfoList.add(one);
                }
                index++;
            }
			if (one.getWeek() > maxWeek) {
				maxWeek = one.getWeek();
			}
        }
        if (CollectionUtils.isEmpty(weekInfoList)) {
        	weekInfoList.add(tmp);
		}
        nowWeek = weekInfoList.get(0);
        map.put("weekInfoList", weekInfoList);
        map.put("nowWeek", nowWeek);
		map.put("maxWeek", maxWeek);
        List<Grade> gradeList = gradeService.findByUnitId(unitId);
        Integer morn = 0;
        Integer am = 0;
        Integer pm = 0;
        Integer night = 0;
        for (Grade one : gradeList) {
			if (morn < one.getMornPeriods()) {
				morn = one.getMornPeriods();
			}
            if (am < one.getAmLessonCount()) {
                am = one.getAmLessonCount();
            }
            if (pm < one.getPmLessonCount()) {
                pm = one.getPmLessonCount();
            }
            if (night < one.getNightLessonCount()) {
                night = one.getNightLessonCount();
            }
        }
        map.put("morn", morn);
        map.put("am", am);
        map.put("pm", pm);
        map.put("night", night);
        return "basedata/teacherClassSwitchH5/switchApply.ftl";
    }

    @RequestMapping("/switch/timetableinfo")
    @ResponseBody
    public String switchTimeTable(String unitId, String objectId, String teacherId, String nowAcadyear, String nowSemester, String week) {
        if (StringUtils.isBlank(week)) {
            return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("请选择周次"));
        }
        List<CourseSchedule> courseScheduleList =
                courseScheduleService.findCourseScheduleListByTeacherId(nowAcadyear, Integer.valueOf(nowSemester), objectId, Integer.valueOf(week));
        if (CollectionUtils.isEmpty(courseScheduleList)) {
            courseScheduleList =
                    courseScheduleService.findCourseScheduleListByClassId(nowAcadyear, Integer.valueOf(nowSemester), objectId, Integer.valueOf(week));
			// 不为空，判断冲突的课程
			if (CollectionUtils.isNotEmpty(courseScheduleList) && teacherId != null) {
				List<CourseSchedule> teacherCourseScheduleList =
						courseScheduleService.findCourseScheduleListByTeacherId(nowAcadyear, Integer.valueOf(nowSemester), teacherId, Integer.valueOf(week));
				if (CollectionUtils.isNotEmpty(teacherCourseScheduleList)) {
					Map<String, CourseSchedule> locationToCourseScheduleMap = new HashMap<>();
					for (CourseSchedule one : teacherCourseScheduleList) {
						locationToCourseScheduleMap.put(one.getDayOfWeek() + "_" + one.getPeriodInterval() + "_" + one.getPeriod(), one);
					}
					for (CourseSchedule one : courseScheduleList) {
						if (locationToCourseScheduleMap.get(one.getDayOfWeek() + "_" + one.getPeriodInterval() + "_" + one.getPeriod()) != null) {
							one.setIsDeleted(2);
						}
					}
				}
			}
        }
        // 仅支持行政班课程调课
        List<CourseSchedule> returnList = new ArrayList<>();
        for (CourseSchedule one : courseScheduleList) {
            if (one.getClassType() == 1) {
                returnList.add(one);
            }
        }
        if (CollectionUtils.isEmpty(returnList)) {
            return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("本周无课程"));
        }
        makeCourseSchedule(returnList);
		Map<String, List<CourseSchedule>> returnMap = returnList.stream().collect(Collectors.groupingBy(e -> e.getDayOfWeek() + "_" + e.getPeriodInterval() + "_" + e.getPeriod()));
		DateInfo dateInfo = dateInfoService.getDate(unitId, nowAcadyear, Integer.valueOf(nowSemester), new Date());
		returnList.stream().forEach(e -> {
			if (e.getWeekOfWorktime() < dateInfo.getWeek() || (e.getWeekOfWorktime() == dateInfo.getWeek() && (e.getDayOfWeek() + 1) < dateInfo.getWeekday())) {
				e.setIsDeleted(1);
			}
			if (returnMap.get(e.getDayOfWeek() + "_" + e.getPeriodInterval() + "_" + e.getPeriod()) != null && returnMap.get(e.getDayOfWeek() + "_" + e.getPeriodInterval() + "_" + e.getPeriod()).size() > 1) {
				e.setIsDeleted(3);
			}
		});
        String resultJson = SUtils.s(returnList);
        return JSON.toJSONString(new ResultDto().setCode("00").setSuccess(true).setBusinessValue(resultJson));
    }

    @RequestMapping("apply/commit")
    @ResponseBody
    public String switchCommit(String unitId, String userId, String teacherId, String adjustIds, String beenAdjustIds, String remark, String adminType) {
	    if (StringUtils.isBlank(adjustIds) || StringUtils.isBlank(beenAdjustIds)) {
            return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("数据为空"));
        }
        String[] adjustIdArr = StringUtils.split(adjustIds, ',');
        String[] beenAdjustIdArr = StringUtils.split(beenAdjustIds, ',');
        List<CourseSchedule> courseScheduleList =
                courseScheduleService.findListByIdIn(ArrayUtil.concat(adjustIdArr, beenAdjustIdArr));
        Map<String, CourseSchedule> courseScheduleMap =
                EntityUtils.getMap(courseScheduleList, CourseSchedule::getId);
        // 冲突检测
        String msg = checkConflict(unitId, adjustIdArr, beenAdjustIdArr, courseScheduleMap);
        if (!"noneError".equals(msg)) {
            return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg(msg));
        }
        List<Adjusted> adjustedList = new ArrayList<>();
        List<AdjustedDetail> adjustedDetailList = new ArrayList<>();
        List<TipsayEx> tipsayExList = new ArrayList<>();
        CourseSchedule adjustTmp = null;
        CourseSchedule beenAdjustTmp = null;
        for (int i = 0; i < adjustIdArr.length; i++) {
            adjustTmp = courseScheduleMap.get(adjustIdArr[i]);
            beenAdjustTmp = courseScheduleMap.get(beenAdjustIdArr[i]);
            if (adjustTmp.getTeacherId().equals(beenAdjustTmp.getTeacherId())) {
                return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("第" + (i + 1) + "项需调课程与被调课程老师相同，不需要调换"));
            }
            Adjusted adjusted = new Adjusted();
            adjusted.setId(UuidUtils.generateUuid());
            adjusted.setSchoolId(adjustTmp.getSchoolId());
            adjusted.setAcadyear(adjustTmp.getAcadyear());
            adjusted.setSemester(adjustTmp.getSemester());
            AdjustedDetail adjustedDetail = makeAdjustedDetail(courseScheduleMap.get(adjustIdArr[i]), adjusted.getId(), "01");
            AdjustedDetail beenAdjustedDetail = makeAdjustedDetail(courseScheduleMap.get(beenAdjustIdArr[i]), adjusted.getId(), "02");
            adjusted.setWeekOfWorktime(adjustTmp.getWeekOfWorktime());
            adjusted.setRemark(remark);
            adjusted.setState("0");
            adjusted.setOperator(adjustTmp.getTeacherId());
            adjusted.setCreationTime(new Date());
            adjusted.setModifyTime(new Date());
            TipsayEx tipsayEx = new TipsayEx();
            tipsayEx.setId(UuidUtils.generateUuid());
            tipsayEx.setTipsayId(adjusted.getId());
            tipsayEx.setAuditorId(BaseConstants.ZERO_GUID);
            tipsayEx.setAuditorType("1");
            tipsayEx.setState("0");
            tipsayEx.setCreationTime(new Date());
            tipsayEx.setSchoolId(adjusted.getSchoolId());
            tipsayEx.setSourceType("02");
            adjustedList.add(adjusted);
            tipsayExList.add(tipsayEx);
            adjustedDetailList.add(adjustedDetail);
            adjustedDetailList.add(beenAdjustedDetail);
        }
        try {
            adjustedService.saveAllAdjust(adjustedList, adjustedDetailList, tipsayExList);
        } catch(Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("申请失败"));
        }
        try {
            List<AdjustedDto> resultList = makeAdjustDtoList(adjustedList, adjustedDetailList);
            final StringBuffer stringBuffer = new StringBuffer("申请人：" + resultList.get(0).getOperatorName() + ",");
            final StringBuffer stringBuffer2 = new StringBuffer("申请人：" +resultList.get(0).getOperatorName());
            for (AdjustedDto one : resultList) {
                stringBuffer.append("由 " + one.getAdjustingName() + " 调至 " + one.getBeenAdjustedName() + "；");
            }
            final String[] adminIds = tipsayService.findUserRole(unitId).toArray(new String[0]);
            new Thread(new Runnable() {
                public void run() {
                	stringBuffer2.append("老师，有调课信息需要审核。");
                    tipsayService.pushMessage(null, adminIds, stringBuffer2.toString(), "调课申请");
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(new ResultDto().setSuccess(true).setMsg("申请成功，请等待审核"));
    }

    @RequestMapping("/apply/copyswitch")
    @ResponseBody
    public String copySwitch(String adjustId, String beenAdjustId, String copyWeeks, String teacherId, String nowAcadyear, String nowSemester) {
        copyWeeks = StringUtils.substring(copyWeeks, 0, copyWeeks.length() - 1);
        String[] copyWeekArr = StringUtils.split(copyWeeks, ',');
        CourseSchedule adjust = courseScheduleService.findOne(adjustId);
		// 检测是否当前周
		for (String one : copyWeekArr) {
			if (adjust.getWeekOfWorktime() == Integer.valueOf(one)) {
				return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("当前周次无需复制"));
			}
		}

        CourseSchedule beenAdjust = courseScheduleService.findOne(beenAdjustId);
        List<CourseSchedule> returnList = new ArrayList<>();
        List<CourseSchedule> copyAdjustList =
                courseScheduleService.findListBy(
                		new String[]{"isDeleted","acadyear", "semester", "teacherId", "dayOfWeek", "periodInterval", "period"},
                		new Object[]{0, nowAcadyear, Integer.valueOf(nowSemester), teacherId, adjust.getDayOfWeek(), 
                				adjust.getPeriodInterval(), adjust.getPeriod()});
        List<CourseSchedule> copyBeenAdjustList =
                courseScheduleService.findListBy(
                		new String[]{"isDeleted", "acadyear", "semester", "classId", "dayOfWeek", "periodInterval", 
                				"period"}, 
                		new Object[]{0,nowAcadyear, Integer.valueOf(nowSemester), beenAdjust.getClassId(), beenAdjust.getDayOfWeek(), beenAdjust.getPeriodInterval(), beenAdjust.getPeriod()});
        Map<Integer, CourseSchedule> copyAdjustMap = EntityUtils.getMap(copyAdjustList, CourseSchedule::getWeekOfWorktime);
        Map<Integer, CourseSchedule> copyBeenAdjustMap = EntityUtils.getMap(copyBeenAdjustList, CourseSchedule::getWeekOfWorktime);
        int weekDif = beenAdjust.getWeekOfWorktime() - adjust.getWeekOfWorktime();
        for (String one : copyWeekArr) {
            if (copyAdjustMap.containsKey(Integer.valueOf(one)) && copyBeenAdjustMap.containsKey(Integer.valueOf(one) + weekDif)) {
                returnList.add(copyAdjustMap.get(Integer.valueOf(one)));
                returnList.add(copyBeenAdjustMap.get(Integer.valueOf(one) + weekDif));
            } else {
                return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("第" + one + "周无需调课程或被调课程"));
            }
        }
        if (CollectionUtils.isEmpty(returnList)) {
            return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("无可供拷贝课程"));
        }
        makeCourseSchedule(returnList);
        String resultJson = SUtils.s(returnList);
        return JSON.toJSONString(new ResultDto().setCode("00").setSuccess(true).setBusinessValue(resultJson));
    }

    @RequestMapping("/list/cancel")
    @ResponseBody
    public String switchListCancel(String adjustedId) {
        final Adjusted adjusted = adjustedService.findOne(adjustedId);
        final AdjustedDetail adjustedDetail = adjustedDetailService.findOneBy(new String[]{"adjustedId", "adjustedType"}, new String[]{adjustedId, "01"});
        final AdjustedDetail beenAdjustedDetail = adjustedDetailService.findOneBy(new String[]{"adjustedId", "adjustedType"}, new String[]{adjustedId, "02"});
        try {
            if ("1".equals(adjusted.getState())) {
                CourseSchedule courseSchedule = courseScheduleService.findOne(adjustedDetail.getCourseScheduleId());
                if (courseSchedule == null) {
                    throw new Exception("需调课程不存在");
                }
                if (!StringUtils.equals(adjustedDetail.getTeacherId(), courseSchedule.getTeacherId())) {
                    if (!(adjustedDetail.getTeacherId() == null && courseSchedule.getTeacherId() == null)) {
                        throw new Exception("需调课程教师已发生更改，无法撤回");
                    }
                }
                if (!beenAdjustedDetail.getWeekOfWorktime().equals(courseSchedule.getWeekOfWorktime())
                        || !beenAdjustedDetail.getDayOfWeek().equals(courseSchedule.getDayOfWeek())
                        || !beenAdjustedDetail.getPeriodInterval().equals(courseSchedule.getPeriodInterval())
                        || !beenAdjustedDetail.getPeriod().equals(courseSchedule.getPeriod())) {
                    throw new Exception("需调课程时间已发生更改，无法撤回");
                }
                CourseSchedule beenCourseSchedule;
                beenCourseSchedule = courseScheduleService.findOne(beenAdjustedDetail.getCourseScheduleId());
                if (beenCourseSchedule != null) {
                    if (!StringUtils.equals(beenAdjustedDetail.getTeacherId(), beenCourseSchedule.getTeacherId())) {
                        if (!(beenAdjustedDetail.getTeacherId() == null && beenCourseSchedule.getTeacherId() == null)) {
                            throw new Exception("被调课程教师已发生更改，无法撤回");
                        }
                    }
                    if (!adjustedDetail.getWeekOfWorktime().equals(beenCourseSchedule.getWeekOfWorktime())
                            || !adjustedDetail.getDayOfWeek().equals(beenCourseSchedule.getDayOfWeek())
                            || !adjustedDetail.getPeriodInterval().equals(beenCourseSchedule.getPeriodInterval())
                            || !adjustedDetail.getPeriod().equals(beenCourseSchedule.getPeriod())) {
                        throw new Exception("被调课程时间已发生更改，无法撤回");
                    }
                }
                courseSchedule.setWeekOfWorktime(adjustedDetail.getWeekOfWorktime());
                courseSchedule.setDayOfWeek(adjustedDetail.getDayOfWeek());
                courseSchedule.setPeriodInterval(adjustedDetail.getPeriodInterval());
                courseSchedule.setPeriod(adjustedDetail.getPeriod());
                if (beenCourseSchedule != null) {
                    beenCourseSchedule.setWeekOfWorktime(beenAdjustedDetail.getWeekOfWorktime());
                    beenCourseSchedule.setDayOfWeek(beenAdjustedDetail.getDayOfWeek());
                    beenCourseSchedule.setPeriodInterval(beenAdjustedDetail.getPeriodInterval());
                    beenCourseSchedule.setPeriod(beenAdjustedDetail.getPeriod());
                }
                adjustedService.updateRollBack(adjustedId, courseSchedule, beenCourseSchedule);
                try {
                    final String adjustMsg = "您第" + adjustedDetail.getWeekOfWorktime() + "周" + BaseConstants.dayOfWeekMap2.get(adjustedDetail.getDayOfWeek().toString()) + BaseConstants.PERIOD_INTERVAL_Map2.get(adjustedDetail.getPeriodInterval()) + "第" + adjustedDetail.getPeriod() + "节课调课申请已撤销。";
                    if (StringUtils.isNotBlank(beenAdjustedDetail.getTeacherId())) {
                        final String beenAdjustMsg = "您的" + beenAdjustedDetail.getWeekOfWorktime() + "周" + BaseConstants.dayOfWeekMap2.get(beenAdjustedDetail.getDayOfWeek().toString()) + BaseConstants.PERIOD_INTERVAL_Map2.get(beenAdjustedDetail.getPeriodInterval()) + "第" + beenAdjustedDetail.getPeriod() + "节课调课申请已撤销。";
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                tipsayService.pushMessage(new String[]{adjustedDetail.getTeacherId()}, null, adjustMsg, "调课申请已撤销");
                                tipsayService.pushMessage(new String[]{beenAdjustedDetail.getTeacherId()}, null, beenAdjustMsg, "调课申请已撤销");
								if (StringUtils.isNotBlank(adjustedDetail.getClassId())) {
									Clazz clazz = classService.findOne(adjustedDetail.getClassId());
									if (clazz != null && StringUtils.isNotBlank(clazz.getTeacherId())) {
										tipsayService.pushMessage(new String[]{beenAdjustedDetail.getTeacherId()}, null, "您所属班级调课申请已撤销。", "所属班级调课申请变更");
									}
								}
                            }
                        }).start();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                tipsayService.pushMessage(new String[]{adjustedDetail.getTeacherId()}, null, adjustMsg, "调课申请已撤销");
								if (StringUtils.isNotBlank(adjustedDetail.getClassId())) {
									Clazz clazz = classService.findOne(adjustedDetail.getClassId());
									if (clazz != null && StringUtils.isNotBlank(clazz.getTeacherId())) {
										tipsayService.pushMessage(new String[]{beenAdjustedDetail.getTeacherId()}, null, "您所属班级调课申请已撤销。", "所属班级调课申请变更");
									}
								}
                            }
                        }).start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                adjustedService.deleteById(adjustedId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg(e.getMessage() == null ? "撤销失败" : e.getMessage()));
        }
        return JSON.toJSONString(new ResultDto().setSuccess(true).setMsg("提交成功"));
    }

    @RequestMapping("/manage/table")
    public String switchManageDetail(String unitId, String userId, String teacherId, String acadyear, String semester, String week, String from, ModelMap map) {
        List<Adjusted> adjustedList = adjustedService.findListBySchoolIdAndWeek(acadyear, semester, unitId, week);
        Iterator<Adjusted> iterator = adjustedList.iterator();
        Set<String> auditedAdjustedIdSet = new HashSet<>();
        while (iterator.hasNext()) {
            Adjusted adjusted = iterator.next();
            if ("01".equals(from) && ("1".equals(adjusted.getState()) || "2".equals(adjusted.getState()))) {
                iterator.remove();
            }
            if ("02".equals(from)) {
                if ("0".equals(adjusted.getState())) {
                    iterator.remove();
                } else {
                    auditedAdjustedIdSet.add(adjusted.getId());
                }
            }
        }
        // 手机端已审核按teacherId进行过滤
        if ("02".equals(from)) {
            List<TipsayEx> exList = tipsayExService.findListByIn("tipsayId", auditedAdjustedIdSet.toArray(new String[] {}));
            Set<String> adjustedIdInTeacherId = new HashSet<>();
            for (TipsayEx one : exList) {
                if (teacherId.equals(one.getAuditorId())) {
                    adjustedIdInTeacherId.add(one.getTipsayId());
                }
            }
            iterator = adjustedList.iterator();
            while (iterator.hasNext()) {
                Adjusted adjusted = iterator.next();
                if (!adjustedIdInTeacherId.contains(adjusted.getId())) {
                    iterator.remove();
                }
            }
        }
        map.put("unitId", unitId);
        map.put("userId", userId);
        map.put("teacherId", teacherId);
        if (CollectionUtils.isEmpty(adjustedList)) {
            return "basedata/teacherClassSwitchH5/switchManageList.ftl";
        }
        List<AdjustedDto> returnList;
        List<String> adjustedIds = EntityUtils.getList(adjustedList, Adjusted::getId);
        List<AdjustedDetail> adjustedDetailList = adjustedDetailService.findListByAdjustedIds(adjustedIds.toArray(new String[0]));
        returnList = makeAdjustDtoList(adjustedList, adjustedDetailList);
        map.put("resultList", returnList);
        return "basedata/teacherClassSwitchH5/switchManageList.ftl";
    }

    @RequestMapping("/manage/agree")
    @ResponseBody
    public String switchManageAgree(String teacherId, String adjustedId, String state) {
        String msg;
        try {
            msg = adjustedService.updateStateById(adjustedId, state, teacherId);
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("审核失败"));
        }
        try {
            final AdjustedDetail adjust = adjustedDetailService.findOneBy(new String[]{"adjustedId", "adjustedType"}, new String[]{adjustedId, "01"});
            if ("1".equals(state) && msg == null) {
				// 通知班主任
				final Clazz clazz = classService.findOne(adjust.getClassId());

                final AdjustedDetail beenAdjust = adjustedDetailService.findOneBy(new String[]{"adjustedId", "adjustedType"}, new String[]{adjustedId, "02"});
                final String adjustMsg = "您第" + adjust.getWeekOfWorktime() + "周" + BaseConstants.dayOfWeekMap2.get(adjust.getDayOfWeek().toString()) + BaseConstants.PERIOD_INTERVAL_Map2.get(adjust.getPeriodInterval()) + "第" + adjust.getPeriod() + "节课调课申请已通过。";
                CourseSchedule courseSchedule = courseScheduleService.findOneWithMaster(beenAdjust.getCourseScheduleId());
                final String switchMsgFirst = "第" + beenAdjust.getWeekOfWorktime() + "周" + BaseConstants.dayOfWeekMap2.get(beenAdjust.getDayOfWeek().toString()) + BaseConstants.PERIOD_INTERVAL_Map2.get(beenAdjust.getPeriodInterval()) + "第" + beenAdjust.getPeriod() + "节";
				final String switchMsgSecond = "第" + courseSchedule.getWeekOfWorktime() + "周" + BaseConstants.dayOfWeekMap2.get(courseSchedule.getDayOfWeek().toString()) + BaseConstants.PERIOD_INTERVAL_Map2.get(courseSchedule.getPeriodInterval()) + "第" + courseSchedule.getPeriod() + "节";
                new Thread(new Runnable() {
                    public void run() {
						if (clazz != null) {
							tipsayService.pushMessage(new String[]{clazz.getTeacherId()}, null, "您所负责的班级" + switchMsgFirst + "课已与" + switchMsgSecond + "课互换。", "所属班级调课通知");
						}
                        tipsayService.pushMessage(new String[]{adjust.getTeacherId()}, null, adjustMsg, "调课已通过");
                        tipsayService.pushMessage(new String[]{beenAdjust.getTeacherId()}, null, "您" + switchMsgFirst + "课已调整至" + switchMsgSecond + "。", "调课通知");
                    }
                }).start();
            } else {
                final String adjustMsg = "您第" + adjust.getWeekOfWorktime() + "周" + BaseConstants.dayOfWeekMap2.get(adjust.getDayOfWeek().toString()) + BaseConstants.PERIOD_INTERVAL_Map2.get(adjust.getPeriodInterval()) + "第" + adjust.getPeriod() + "节课调课申请未通过。";
                new Thread(new Runnable() {
                    public void run() {
                        tipsayService.pushMessage(new String[]{adjust.getTeacherId()}, null, adjustMsg, "调课未通过");
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(new ResultDto().setSuccess(true).setMsg(msg == null ? "审核完成" : msg));
    }

    @RequestMapping("/manage/arrange")
    @ResponseBody
    public String switchManageArrange(String unitId, String userId, String teacherId, String adjustIds, String beenAdjustIds, String remark, String adminType) {
        if ("1".equals(adminType) && !isAdmin(unitId, userId)) {
            return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("无管理员权限"));
        }
        if (StringUtils.isBlank(adjustIds) || StringUtils.isBlank(beenAdjustIds)) {
            return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("数据为空"));
        }
        String[] adjustIdArr = StringUtils.split(adjustIds, ',');
        String[] beenAdjustIdArr = StringUtils.split(beenAdjustIds, ',');
        List<CourseSchedule> courseScheduleList =
                courseScheduleService.findListByIdIn(ArrayUtil.concat(adjustIdArr, beenAdjustIdArr));
        final Map<String, CourseSchedule> courseScheduleMap =
                EntityUtils.getMap(courseScheduleList, CourseSchedule::getId);
        // 冲突检测
        String msg = checkConflict(unitId, adjustIdArr, beenAdjustIdArr, courseScheduleMap);
        if (!"noneError".equals(msg)) {
            return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg(msg));
        }
        final List<Adjusted> adjustedList = new ArrayList<>();
        final List<AdjustedDetail> adjustedDetailList = new ArrayList<>();
        final Set<String> classIdSet = new HashSet<>();
        List<TipsayEx> tipsayExList = new ArrayList<>();
        CourseSchedule adjustTmp = null;
        CourseSchedule beenAdjustTmp = null;
        for (int i = 0; i < adjustIdArr.length; i++) {
            adjustTmp = courseScheduleMap.get(adjustIdArr[i]);
            beenAdjustTmp = courseScheduleMap.get(beenAdjustIdArr[i]);
            if (adjustTmp.getTeacherId().equals(beenAdjustTmp.getTeacherId())) {
                return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("第" + (i + 1) + "项需调课程与被调课程老师相同，不需要调换"));
            }
            Adjusted adjusted = new Adjusted();
            adjusted.setId(UuidUtils.generateUuid());
            adjusted.setSchoolId(adjustTmp.getSchoolId());
            adjusted.setAcadyear(adjustTmp.getAcadyear());
            adjusted.setSemester(adjustTmp.getSemester());
            AdjustedDetail adjustedDetail = makeAdjustedDetail(adjustTmp, adjusted.getId(), "01");
            AdjustedDetail beenAdjustedDetail = makeAdjustedDetail(beenAdjustTmp, adjusted.getId(), "02");
            if (StringUtils.isNotBlank(adjustedDetail.getClassId())) {
            	classIdSet.add(adjustedDetail.getClassId());
			}
            if (StringUtils.isNotBlank(beenAdjustedDetail.getClassId())) {
            	classIdSet.add(beenAdjustedDetail.getClassId());
			}
            adjustTmp.setWeekOfWorktime(beenAdjustedDetail.getWeekOfWorktime());
            adjustTmp.setDayOfWeek(beenAdjustedDetail.getDayOfWeek());
            adjustTmp.setPeriodInterval(beenAdjustedDetail.getPeriodInterval());
            adjustTmp.setPeriod(beenAdjustedDetail.getPeriod());
            beenAdjustTmp.setWeekOfWorktime(adjustedDetail.getWeekOfWorktime());
            beenAdjustTmp.setDayOfWeek(adjustedDetail.getDayOfWeek());
            beenAdjustTmp.setPeriodInterval(adjustedDetail.getPeriodInterval());
            beenAdjustTmp.setPeriod(adjustedDetail.getPeriod());
            adjusted.setWeekOfWorktime(adjustTmp.getWeekOfWorktime());
            adjusted.setRemark("教务安排直接安排 " + remark);
            adjusted.setState(TipsayConstants.TIPSAY_STATE_1);
            adjusted.setOperator(adjustTmp.getTeacherId());
            adjusted.setCreationTime(new Date());
            adjusted.setModifyTime(new Date());
            TipsayEx tipsayEx = new TipsayEx();
            tipsayEx.setId(UuidUtils.generateUuid());
            tipsayEx.setTipsayId(adjusted.getId());
            tipsayEx.setAuditorId(teacherId);
            tipsayEx.setAuditorType(TipsayConstants.AUDITOR_TYPE_1);
            tipsayEx.setState(TipsayConstants.TIPSAY_STATE_1);
            tipsayEx.setCreationTime(new Date());
            tipsayEx.setSchoolId(adjusted.getSchoolId());
            tipsayEx.setSourceType("02");
            adjustedList.add(adjusted);
            tipsayExList.add(tipsayEx);
            adjustedDetailList.add(adjustedDetail);
            adjustedDetailList.add(beenAdjustedDetail);
        }
        try {
            adjustedService.saveAllArrange(adjustedList, adjustedDetailList, tipsayExList, courseScheduleMap.values().toArray(new CourseSchedule[0]));
        } catch(Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new ResultDto().setSuccess(false).setMsg("安排失败"));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
            	// 通知班主任
            	List<Clazz> clazzList = classService.findClassListByIds(classIdSet.toArray(new String[0]));
            	String[] headMasters = clazzList.stream().filter(e -> StringUtils.isNotBlank(e.getTeacherId())).map(e -> e.getTeacherId()).toArray(String[]::new);

                List<String> teacherIdList = new ArrayList<>();
                for (AdjustedDetail one : adjustedDetailList) {
                    if (one.getTeacherId() != null) {
                        teacherIdList.add(one.getTeacherId());
                    }
                }
                tipsayService.pushMessage(teacherIdList.toArray(new String[0]), null, "您的课表因教务调整有所更改，请及时登录查看。", "调课通知");
				tipsayService.pushMessage(headMasters, null, "您所属班级因教务安排已发生调课。", "所属班级调课通知");
			}
        }).start();
        return JSON.toJSONString(new ResultDto().setSuccess(true).setMsg("安排成功"));
    }

    public String showTipsayHead(String unitId, String userId, String teacherId,String adminType,String type,
			String acadyear,String semester, String week,String statType,ModelMap map) {
		//statType:状态
		showSearchHead(unitId, acadyear, semester, week,statType, map);
		map.put("unitId", unitId);
		map.put("userId", userId);
		map.put("teacherId", teacherId);
		map.put("adminType", adminType);
		map.put("type", type);
		return "/basedata/tipsayh5/head.ftl";
	}
	
	//头部查询
	public void showSearchHead(String unitId, String acadyear,
			String semester, String week, String statType,ModelMap map) {
		// 学年学期
		List<String> acadyearList = semesterService.findAcadeyearList();
		Semester semesterObj = null;// 当前学年学期
		if (StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
			if (CollectionUtils.isNotEmpty(acadyearList)) {
				semesterObj = semesterService.getCurrentSemester(2);
			}
		} else {
			semesterObj = new Semester();
			semesterObj.setAcadyear(acadyear);
			semesterObj.setSemester(Integer.parseInt(semester));
		}

		List<Integer> weekList = new ArrayList<Integer>();
		int nowWeek = 0;
		
		// 一共周次数
		if (StringUtils.isNotBlank(week)) {
			nowWeek = Integer.parseInt(week);
			Integer chooseNowWeek = findBySemester(unitId, semesterObj.getAcadyear(),
					semesterObj.getSemester(), weekList, new Date());
			if(chooseNowWeek==0) {
				nowWeek=0;
			}
			if(nowWeek==0 && CollectionUtils.isNotEmpty(weekList)) {
				if(chooseNowWeek==0) {
					nowWeek=weekList.get(0);
				}else {
					nowWeek=chooseNowWeek;
				}
				
			}
		} else {
			nowWeek = findBySemester(unitId, semesterObj.getAcadyear(),
                    semesterObj.getSemester(), weekList, new Date());
			semesterObj.setWeek(nowWeek == 0 ? 1 : nowWeek);
			//页面week未传入
			//默认全部
			nowWeek=0;
		}
		map.put("semesterObj", semesterObj);
		map.put("weekList", weekList);
		map.put("nowWeek", nowWeek);
		map.put("acadyearList", acadyearList);
		map.put("statType", statType);
	}
	
	public Integer findBySemester(String unitId, String acadyear,
			Integer semester, List<Integer> weekList, Date date) {
		//根据infoDate升序
		List<DateInfo> dates = dateInfoService.findByAcadyearAndSemester(
				unitId, acadyear, semester);
		int nowWeek = 0;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = null;
		if (date != null) {
			dateStr = f.format(date);
		}
		if (CollectionUtils.isNotEmpty(dates)) {
			Set<Integer> weeks = new HashSet<Integer>();
			for (DateInfo info : dates) {
				weeks.add(info.getWeek());
				if (date != null) {
					String tStr = f.format(info.getInfoDate());
					if (dateStr.equals(tStr)) {
						nowWeek = info.getWeek();
						continue;
					}
				}
			}

			CollectionUtils.addAll(weekList, weeks.iterator());
		}
		return nowWeek;
	}
	@RequestMapping("/showTipsayList/page")
	@ControllerInfo("加载代管课列表")
	public String showTipsayList(String unitId, String teacherId,String adminType,String type,
			String acadyear,String semester, String week,String statType,ModelMap map) {
		List<TipsayDto> dtoList =new ArrayList<>();
		if(StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
			
		}else {
			Integer weekInt=null;
			if(StringUtils.isNotBlank(week)) {
				//全部
				weekInt=Integer.parseInt(week);
			}
			if("1".equals(adminType)) {
				List<Tipsay> list =tipsayService.findTipsayListWithMaster(unitId,acadyear,Integer.parseInt(semester), weekInt,null);
				//过滤代管
				if(CollectionUtils.isNotEmpty(list)) {
					list=list.stream().filter(e->e.getType().equals(type)).collect(Collectors.toList());
				}
				//过滤状态
				if(StringUtils.isNotBlank(statType)) {
					if(CollectionUtils.isNotEmpty(list)) {
						if("0".equals(statType)) {
							//待审核
							list=list.stream().filter(e->e.getState().equals(TipsayConstants.TIPSAY_STATE_0)).collect(Collectors.toList());
						}else {
							//审核后
							list=list.stream().filter(e->(!e.getState().equals(TipsayConstants.TIPSAY_STATE_0))).collect(Collectors.toList());
							
							//过滤掉不是自己的审核数据
							Set<String> adjustedIdInteacherIdSet=new HashSet<>();
							if(CollectionUtils.isNotEmpty(list)) {
								List<TipsayEx> exList = tipsayExService.findListByIn("tipsayId", EntityUtils.getSet(list, e->e.getId()).toArray(new String[] {}));
								if(CollectionUtils.isNotEmpty(exList)) {
									List<TipsayEx> exList2 = exList.stream().filter(e->e.getAuditorId().equals(teacherId)).collect(Collectors.toList());
									if(CollectionUtils.isNotEmpty(exList2)) {
										adjustedIdInteacherIdSet = EntityUtils.getSet(exList2,e->e.getTipsayId());
									}
								}
								if(CollectionUtils.isNotEmpty(adjustedIdInteacherIdSet)) {
									List<Tipsay> list2 = new ArrayList<Tipsay>();
									for(Tipsay yy:list) {
										if(adjustedIdInteacherIdSet.contains(yy.getId())) {
											list2.add(yy);
										}
									}
									list=list2;
								}else {
									//没有数据是该老师审核的
									list=new ArrayList<Tipsay>();
								}
							}
							
						}
								
						
					}
				}
				if(CollectionUtils.isNotEmpty(list)) {
					dtoList = tipsayService.tipsayToTipsayDto(new HashMap<>(), list, false);
				}
				
			}else {
				List<Tipsay> list =tipsayService.findTipsayListWithMaster(unitId,acadyear,Integer.parseInt(semester), weekInt, teacherId);
				//过滤代管
				if(CollectionUtils.isNotEmpty(list)) {
					list=list.stream().filter(e->e.getType().equals(type)).collect(Collectors.toList());
				}
				//过滤状态
				if(StringUtils.isNotBlank(statType)) {
					if(CollectionUtils.isNotEmpty(list)) {
						if("0".equals(statType)) {
							//待审核
							list=list.stream().filter(e->e.getState().equals(TipsayConstants.TIPSAY_STATE_0)).collect(Collectors.toList());
						}else {
							//审核后
							list=list.stream().filter(e->(!e.getState().equals(TipsayConstants.TIPSAY_STATE_0))).collect(Collectors.toList());
							
						}
					}
				}
				if(CollectionUtils.isNotEmpty(list)) {
					dtoList = tipsayService.tipsayToTipsayDto(new HashMap<>(), list, false);
				}
				
			}
		}
		map.put("dtoList", dtoList);
		map.put("teacherId", teacherId);
		map.put("type", type);
		map.put("adminType", adminType);
		return "/basedata/tipsayh5/list.ftl";
	}
	
	@RequestMapping("/addApplyTipsay/page")
	@ControllerInfo("申请代课")
	public String addApplyTipsay(String unitId, String teacherId,String adminType,String type,
			String acadyear,String semester,String week,String statType, String applyType,ModelMap map) {
		//applyType 1:申请教务安排 2:自主申请 3:教务安排
		//acadyear semester 时间是不是过去啦
		Semester semesterObj = semesterService.findByAcadyearAndSemester(acadyear, Integer.parseInt(semester), unitId);
		if(semesterObj==null) {
			map.put("nonemess", "未设置："+acadyear+"学年第"+semester+"学期");
			return "/basedata/tipsayh5/none.ftl";
		}
		Date nowDate = new Date();
		if(DateUtils.compareForDay(nowDate, semesterObj.getSemesterEnd())>0) {
			map.put("nonemess", acadyear+"学年第"+semester+"学期"+"已经过去，不能操作");
			return "/basedata/tipsayh5/none.ftl";
		}
		Teacher teacher = teacherService.findOne(teacherId);
		if(teacher==null) {
			map.put("nonemess", "教师参数丢失");
			return "/basedata/tipsayh5/none.ftl";
		}
		map.put("teacherName", teacher.getTeacherName());
		map.put("teacherId", teacherId);
		
		//起止时间范围
		//时间
		Date startTime = nowDate;
		if(DateUtils.compareForDay(startTime, semesterObj.getSemesterBegin())<0) {
			startTime=semesterObj.getSemesterBegin();
		}
		Date endTime=nowDate;
		if(DateUtils.compareForDay(semesterObj.getSemesterEnd(),endTime)>0) {
			endTime=semesterObj.getSemesterEnd();
		}
		String[] startTimeStr = DateUtils.date2String(startTime, "yyyy-MM-dd").split("-");
		String[] endTimeStr=DateUtils.date2String(endTime, "yyyy-MM-dd").split("-");
		map.put("startTimeStr", startTimeStr);
		map.put("endTimeStr", endTimeStr);
		
		map.put("unitId", unitId);
		map.put("adminType", adminType);
		map.put("type", type);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("week", week);
		map.put("applyType", applyType);
		map.put("statType", statType);
		
		Integer days = semesterObj.getEduDays();
		if(days==null) {
			//默认5天
			days=5;
		}
		// 0,周一
		List<String[]> weekDayList = new ArrayList<String[]>();
		for(int i=0;i<days;i++) {
			String[] arr = new String[2];
			arr[0] = i+ "";
			arr[1] = BaseConstants.dayOfWeekMap2.get(arr[0]);
			weekDayList.add(arr);
		}
		map.put("weekDayList", weekDayList);
		//课程表时间
		List<Grade> gradeList = gradeService.findByUnitId(unitId);
		Integer mm = 0;
		Integer am = 0;
		Integer pm = 0;
		Integer nm = 0;
		for (Grade g : gradeList) {
        	if(g.getMornPeriods() > mm) {
        		mm = g.getMornPeriods();
        	}
            if (g.getAmLessonCount() > am) {
                am = g.getAmLessonCount();
            }
            if (g.getPmLessonCount() > pm) {
                pm = g.getPmLessonCount();
            }
            if (g.getNightLessonCount() > nm) {
            	nm = g.getNightLessonCount();
            }
        }
		if (mm + am + pm + nm == 0) {
			map.put("nonemess", acadyear+"学年第"+semester+"学期上课节次数没有设置，请先去设置");
			return "/basedata/tipsayh5/none.ftl";
		}
		map.put("mm", mm);
		map.put("am", am);
		map.put("pm", pm);
		map.put("nm", nm);
		
		return "/basedata/tipsayh5/addApply.ftl";
	}
	@ResponseBody
	@RequestMapping("/findWeekList")
	@ControllerInfo("申请代课")
	public String findWeekList(String unitId, String searchType,
			String acadyear,String semester,String startTime, String endTime,ModelMap map) {
		Date nowDate = new Date();
		List<String> weekList=new ArrayList<>();
		Date startDate=null;
		Date endDate=null;
		JSONObject on = new JSONObject();
		if("2".equals(searchType)) {
			//时间范围内的
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				startDate = formatter.parse(startTime);
				endDate = formatter.parse(endTime);
			} catch (Exception e) {
				e.printStackTrace();
				//
				on.put("success", false);
				on.put("msg", "时间格式不对");
				return on.toJSONString();
			}
			
		}else {
			//拿到当前周次以及以后
			startDate=nowDate;
		}
		//排序的
		List<DateInfo> list = dateInfoService.findByAcadyearAndSemesterAndDates(unitId,acadyear,Integer.parseInt(semester), startDate, endDate);
		if(CollectionUtils.isNotEmpty(list)) {
			for(DateInfo info:list) {
				if(!weekList.contains(String.valueOf(info.getWeek()))) {
					weekList.add(String.valueOf(info.getWeek()));
				}
			}
		}else {
			on.put("success", false);
			on.put("msg", "未找到周次列表");
			return on.toJSONString();
		}
		
		on.put("success", true);
		on.put("weekList", weekList);
		on.put("startWeekDay", list.get(0).getWeek()+"_"+list.get(0).getWeekday());
		return on.toJSONString();
		
	}
	
	@ResponseBody
	@RequestMapping("/schedultByTeacherId")
	@ControllerInfo("查询教师当天课表")
	public String findSchedultByTeacherId(String unitId, String acadyear,
			String semester, String week, String teacherId) {
		List<CourseSchedule> list = courseScheduleService.findCourseScheduleListByPerId(acadyear,
						Integer.parseInt(semester), Integer.parseInt(week), teacherId, "1");
		JSONObject on = new JSONObject();
		if(CollectionUtils.isEmpty(list)) {
			on.put("success", false);
		}else {
			on.put("success", true);
		}
		on.put("courseScheduleList", list);
		return on.toJSONString();
	}
	@ResponseBody
	@RequestMapping("/saveAllTipsay")
	@ControllerInfo("保存申请")
	public String saveAllTipsay(TipsaySaveDto dto,String applyType,String type) {
		JSONObject on = new JSONObject();
		if (dto == null) {
			on.put("success", false);
			on.put("msg", "数据丢失");
			return on.toJSONString();
		}
		if (StringUtils.isBlank(dto.getCourseScheduleId())
				|| StringUtils.isBlank(dto.getTeacherId()) ) {
			on.put("success", false);
			on.put("msg", "数据丢失");
			return on.toJSONString();
		}
		String typename="";
		if(TipsayConstants.TYPE_1.equals(type)) {
			typename="代课";
		}else if(TipsayConstants.TYPE_2.equals(type)) {
			typename="管课";
		}else {
			on.put("success", false);
			on.put("msg", "参数丢失");
			return on.toJSONString();
		}
		String newTeacherId=null;
		if("1".equals(applyType)) {
			//申请教务安排
			
		}else if("2".equals(applyType)) {
			//自主安排
			if(StringUtils.isBlank(dto.getNewTeacherId())) {
				on.put("success", false);
				on.put("msg", "请选择"+typename+"老师");
				return on.toJSONString();
			}
			newTeacherId=dto.getNewTeacherId();
		}else if("3".equals(applyType)) {
			//教务直接安排
			if(StringUtils.isBlank(dto.getNewTeacherId())) {
				on.put("success", false);
				on.put("msg", "请选择"+typename+"老师");
				return on.toJSONString();
			}
			newTeacherId=dto.getNewTeacherId();
		}else {
			on.put("success", false);
			on.put("msg", "参数丢失");
			return on.toJSONString();
		}
		
		
		String courseScheduleIds = dto.getCourseScheduleId();
		String[] courseScheduleIdsArr = courseScheduleIds.split(",");
		List<CourseSchedule> scheduleList = courseScheduleService.findListByIds(courseScheduleIdsArr);
		if(CollectionUtils.isEmpty(scheduleList)) {
			on.put("success", false);
			on.put("msg", "对应课程数据不存在,请刷新后操作");
			return on.toJSONString();
		}
		if(scheduleList.size()!=courseScheduleIdsArr.length) {
			on.put("success", false);
			on.put("msg", "课程数据有调整，请重新操作");
			return on.toJSONString();
		}
		//scheduleList 是不是原老师有没有改变
		courseScheduleService.makeTeacherSet(scheduleList);
		int minWorkTime=0;
		int maxWorkTime=0;
		for(CourseSchedule c:scheduleList) {
			if(!(c.getAcadyear().equals(dto.getAcadyear()) && String.valueOf(c.getSemester()).equals(dto.getSemester()))) {
				on.put("success", false);
				on.put("msg", "课程数据有调整，请重新操作");
				return on.toJSONString();
			}
			if(CollectionUtils.isEmpty(c.getTeacherIds())) {
				on.put("success", false);
				on.put("msg", "课程数据有调整，请重新操作");
				return on.toJSONString();
			}
			if(!c.getTeacherIds().contains(dto.getTeacherId())) {
				on.put("success", false);
				on.put("msg", "课程数据有调整，请重新操作");
				return on.toJSONString();
			}
			if(minWorkTime==0) {
				minWorkTime=c.getWeekOfWorktime();
				maxWorkTime=c.getWeekOfWorktime();
			}else {
				if(minWorkTime>c.getWeekOfWorktime()) {
					minWorkTime=c.getWeekOfWorktime();
				}
				if(maxWorkTime<c.getWeekOfWorktime()) {
					maxWorkTime=c.getWeekOfWorktime();
				}
			}
		}
		String error = tipsayService.checkTimesByTeacher(dto.getAcadyear(),dto.getSemester(),minWorkTime,maxWorkTime,dto.getNewTeacherId(), scheduleList);
		if (StringUtils.isNotBlank(error)) {
			on.put("success", false);
			on.put("msg", typename+"老师存在时间冲突");
			return on.toJSONString();
		}
		List<Tipsay> tipsayList=new ArrayList<>();
		List<TipsayEx> tipsayExList=new ArrayList<>();
		Tipsay tipsay;
		//申请
		for(CourseSchedule c:scheduleList) {
			tipsay=makeNewTipsay(c);
			tipsay.setRemark(dto.getRemark());
			tipsay.setOperator(dto.getOperateuser());
			tipsay.setState(TipsayConstants.TIPSAY_STATE_0);
			
			tipsay.setType(type);
			
			
			if("3".equals(applyType)) {
				tipsay.setNewTeacherId(newTeacherId);
				tipsay.setState(TipsayConstants.TIPSAY_STATE_1);
				tipsay.setTipsayType(TipsayConstants.TIPSAY_TYPE_01);
				
				TipsayEx tipsayEx = new TipsayEx();
				tipsayEx.setAuditorId(dto.getOperateuser());
				tipsayEx.setAuditorType(TipsayConstants.AUDITOR_TYPE_1);
				tipsayEx.setCreationTime(new Date());
				tipsayEx.setRemark("直接同意，生效");
				tipsayEx.setId(UuidUtils.generateUuid());
				tipsayEx.setState(TipsayConstants.TIPSAY_STATE_1);
				tipsayEx.setTipsayId(tipsay.getId());
				tipsayEx.setSchoolId(tipsay.getSchoolId());
				tipsayEx.setSourceType(TipsayConstants.TYPE_01);
				tipsayExList.add(tipsayEx);
				c.setTeacherId(dto.getNewTeacherId());
				
			}else {
				if(StringUtils.isNotBlank(newTeacherId)) {
					tipsay.setNewTeacherId(newTeacherId);
					tipsay.setTipsayType(TipsayConstants.TIPSAY_TYPE_02);
				}else {
					tipsay.setNewTeacherId(BaseConstants.ZERO_GUID);
					tipsay.setTipsayType(TipsayConstants.TIPSAY_TYPE_03);
				}
			}
			tipsayList.add(tipsay);
		}
		
		try {
			if("3".equals(applyType)) {
				tipsayService.saveAllApplyList(dto.getUnitId(),tipsayList, tipsayExList, scheduleList,dto.getNewTeacherId(),dto.getTeacherId(),TipsayConstants.AUDITOR_TYPE_1);
			}else {
				tipsayService.saveAllApplyList(dto.getUnitId(),tipsayList, null, null,dto.getNewTeacherId(),dto.getTeacherId(),TipsayConstants.AUDITOR_TYPE_2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			on.put("success", false);
			on.put("msg", "操作失败");
			return on.toJSONString();
		}
		on.put("success", true);
		on.put("msg", "成功");
		return on.toJSONString();
	}

	/**
	 * 初始化Tipsay
	 * @return
	 */
	private Tipsay makeNewTipsay(CourseSchedule c) {
		Tipsay tipsay = new Tipsay();
		tipsay.setId(UuidUtils.generateUuid());
		tipsay.setCourseScheduleId(c.getId());
		tipsay.setAcadyear(c.getAcadyear());
		tipsay.setSemester(c.getSemester());
		tipsay.setWeekOfWorktime(c.getWeekOfWorktime());
		tipsay.setDayOfWeek(c.getDayOfWeek());
		tipsay.setPeriod(c.getPeriod());
		tipsay.setPeriodInterval(c.getPeriodInterval());
		tipsay.setClassId(c.getClassId());
		tipsay.setClassType(c.getClassType());
		tipsay.setSchoolId(c.getSchoolId());
		tipsay.setSubjectId(c.getSubjectId());
		Set<String> set = c.getTeacherIds();
		String exIds = "";
		if (CollectionUtils.isNotEmpty(set)) {
			for (String s : set) {
				if (!s.equals(c.getTeacherId())) {
					exIds = exIds + "," + s;
				}
			}
		}

		if (StringUtils.isNotBlank(exIds)) {
			exIds = exIds.substring(1);
		}
		if (StringUtils.isNotBlank(exIds)) {
			exIds = exIds.substring(1);
		}
		tipsay.setTeacherExIds(exIds);
		tipsay.setTeacherId(c.getTeacherId());
		tipsay.setCreationTime(new Date());
		tipsay.setModifyTime(new Date());
		tipsay.setIsDeleted(TipsayConstants.IF_0);
		return tipsay;
	}
	@ResponseBody
	@RequestMapping("/findTeacherList")
	@ControllerInfo("教师列表")
	public String findTeacherList(String unitId, String acadyear,
			String semester, String week, String teacherId,String scheduleIds,String searchTeacherName) {
		List<TeachGroupDto> groupDtoList=new ArrayList<>();
		List<TeachGroupDto> allGroupDtoList = findGroupDtoList(unitId);
		TeachGroupDto returnDto;
		if(CollectionUtils.isNotEmpty(allGroupDtoList)){
			Set<String> noTeacherId=new HashSet<>();
			if(StringUtils.isNotBlank(scheduleIds)) {
				String[] ids = scheduleIds.split(",");
				List<CourseSchedule> list = courseScheduleService.findListByIds(ids);
				if(CollectionUtils.isNotEmpty(list)) {
					List<CourseSchedule> sameList = findSameTimeSchedule(unitId, acadyear, semester, list);
					if(CollectionUtils.isNotEmpty(sameList)) {
						courseScheduleService.makeTeacherSet(sameList);
						for(CourseSchedule c:sameList) {
							if(CollectionUtils.isNotEmpty(c.getTeacherIds())) {
								noTeacherId.addAll(c.getTeacherIds());
							}
						}
					}
				}
			}
			if(StringUtils.isNotBlank(teacherId)) {
				noTeacherId.remove(teacherId);
			}
			
			List<String[]> showList;
			if(StringUtils.isNotBlank(searchTeacherName)) {
				for(TeachGroupDto dto:allGroupDtoList) {
					showList=new ArrayList<>();
					if(CollectionUtils.isEmpty(dto.getMainTeacherList())) {
						continue;
					}
					for(TeacherDto item:dto.getMainTeacherList()) {
						if(item.getTeacherId().equals(teacherId)) {
							continue;
						}
						if(item.getTeacherName().indexOf(searchTeacherName)>-1) {
							if(noTeacherId.contains(item.getTeacherId())) {
								showList.add(new String[] {item.getTeacherId(),item.getTeacherName(),"1"});
							}else {
								showList.add(new String[] {item.getTeacherId(),item.getTeacherName(),"0"});
							}
						}
						
					}
					if(CollectionUtils.isEmpty(showList)) {
						continue;
					}
					returnDto=new TeachGroupDto();
					returnDto.setTeachGroupName(dto.getTeachGroupName());
					returnDto.setShowList(showList);
					groupDtoList.add(returnDto);
				}
			}else {
				for(TeachGroupDto dto:allGroupDtoList) {
					showList=new ArrayList<>();
					if(CollectionUtils.isEmpty(dto.getMainTeacherList())) {
						continue;
					}
					for(TeacherDto item:dto.getMainTeacherList()) {
						if(item.getTeacherId().equals(teacherId)) {
							continue;
						}
						if(noTeacherId.contains(item.getTeacherId())) {
							showList.add(new String[] {item.getTeacherId(),item.getTeacherName(),"1"});
						}else {
							showList.add(new String[] {item.getTeacherId(),item.getTeacherName(),"0"});
						}
					}
					if(CollectionUtils.isEmpty(showList)) {
						continue;
					}
					returnDto=new TeachGroupDto();
					returnDto.setTeachGroupName(dto.getTeachGroupName());
					returnDto.setShowList(showList);
					groupDtoList.add(returnDto);
				}
			}
		}
		JSONObject on = new JSONObject();
		if(CollectionUtils.isEmpty(groupDtoList)) {
			on.put("success", false);
		}else {
			on.put("success", true);
			on.put("groupDtoList", groupDtoList);
		}
		return on.toJSONString();
	}
	
	public List<TeachGroupDto> findGroupDtoList(String unitId){
		//缓存一天
		//需要的字段：teachGroupName; mainTeacherList( teacherId,teacherName)
		List<TeachGroupDto> returnList = RedisUtils.getObject("TIPSAY_GROUPTEACHER" + unitId, RedisUtils.TIME_ONE_DAY, new TypeReference<List<TeachGroupDto>>(){}, new RedisInterface<List<TeachGroupDto>>(){
			@Override
			public List<TeachGroupDto> queryData() {
				//查询全校老师 并且未分配的教研组老师
				List<TeachGroupDto> dtoList=teachGroupService.findAllTeacherGroup(unitId, false);
				//空时 返回null 下一次还进入查询
				if(CollectionUtils.isEmpty(dtoList)) {
					return null;
				}
//				List<Teacher> teacherList = teacherService.findByUnitId(unitId);
//				List<TeachGroupDto> dtoList=teachGroupService.findTeachers(unitId);
//				List<TeacherDto> leftList=new ArrayList<TeacherDto>();
//				if(CollectionUtils.isNotEmpty(dtoList)) {
//					Set<String> teacherIds=new HashSet<>();
//					for(TeachGroupDto d:dtoList) {
//						if(CollectionUtils.isNotEmpty(d.getMainTeacherList())) {
//							teacherIds.addAll(EntityUtils.getSet(d.getMainTeacherList(), e->e.getTeacherId()));
//						}
//					}
//					TeacherDto dto;
//					for(Teacher t:teacherList) {
//						if(!teacherIds.contains(t.getId())) {
//							dto=new TeacherDto();
//							dto.setTeacherId(t.getId());
//							dto.setTeacherName(t.getTeacherName());
//							leftList.add(dto);
//						}
//					}
//				}else {
//					dtoList=new ArrayList<TeachGroupDto>();
//				}
//				if(CollectionUtils.isNotEmpty(leftList)) {
//					TeachGroupDto groupDto=new TeachGroupDto();
//					groupDto.setTeachGroupName("未分配教研组");
//					groupDto.setMainTeacherList(leftList);
//					dtoList.add(groupDto);
//				}
				return dtoList;
			}
        });
		if(CollectionUtils.isEmpty(returnList)) {
			returnList=new ArrayList<TeachGroupDto>();
		}
		return returnList;
	}
//	public List<TeachGroupDto> findAllGroup(String unitId){
//		List<Teacher> teacherList = teacherService.findByUnitId(unitId);
//		List<TeachGroupDto> dtoList=teachGroupService.findTeachers(unitId);
//		List<TeacherDto> leftList=new ArrayList<TeacherDto>();
//		if(CollectionUtils.isNotEmpty(dtoList)) {
//			Set<String> teacherIds=new HashSet<>();
//			for(TeachGroupDto d:dtoList) {
//				if(CollectionUtils.isNotEmpty(d.getMainTeacherList())) {
//					teacherIds.addAll(EntityUtils.getSet(d.getMainTeacherList(), e->e.getTeacherId()));
//				}
//			}
//			TeacherDto dto;
//			for(Teacher t:teacherList) {
//				if(!teacherIds.contains(t.getId())) {
//					dto=new TeacherDto();
//					dto.setTeacherId(t.getId());
//					dto.setTeacherName(t.getTeacherName());
//					leftList.add(dto);
//				}
//			}
//		}else {
//			dtoList=new ArrayList<TeachGroupDto>();
//		}
//		if(CollectionUtils.isNotEmpty(leftList)) {
//			TeachGroupDto groupDto=new TeachGroupDto();
//			groupDto.setTeachGroupName("未分配教研组");
//			groupDto.setMainTeacherList(leftList);
//			dtoList.add(groupDto);
//		}
//		return dtoList;
//	}
	
	public List<CourseSchedule> findSameTimeSchedule(String unitId, String acadyear,
			String semester,List<CourseSchedule> list){
		CourseScheduleDto searchDto=new CourseScheduleDto();
		searchDto.setSchoolId(unitId);
		searchDto.setAcadyear(acadyear);
		searchDto.setSemester(Integer.parseInt(semester));
		List<String> timeStr=new ArrayList<>();
		Set<String> times=new HashSet<>();
		int minWeek=0;
		int maxWeek=0;
		for(CourseSchedule c:list) {
			String key1=c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
			if(!timeStr.contains(key1)) {
				timeStr.add(key1);
			}
			times.add(c.getWeekOfWorktime()+"_"+key1);
			if(minWeek==0) {
				minWeek=c.getWeekOfWorktime();
				maxWeek=c.getWeekOfWorktime();
			}else {
				if(c.getWeekOfWorktime()<minWeek) {
					minWeek=c.getWeekOfWorktime();
				}else if(c.getWeekOfWorktime()>maxWeek) {
					maxWeek=c.getWeekOfWorktime();
				}
			}
		}
		searchDto.setWeekOfWorktime1(minWeek);
		searchDto.setWeekOfWorktime2(minWeek);
		List<CourseSchedule> chooseList = courseScheduleService.findByTimes(searchDto, timeStr.toArray(new String[0]));
		List<CourseSchedule> realList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(chooseList)) {
			//过滤times
			for(CourseSchedule c:chooseList) {
				String key=c.getWeekOfWorktime()+"_"+c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
				if(times.contains(key)) {
					realList.add(c);
				}
			}
		}
		return realList;
	}

	@ResponseBody
	@RequestMapping("/deleteTipsay")
	@ControllerInfo("撤销")
	public String deleteTipsay(String tipsayId,String type) {
		Tipsay tipsay = tipsayService.findOne(tipsayId);
		String typename="";
		JSONObject on = new JSONObject();
		if(TipsayConstants.TYPE_1.equals(type)) {
			typename="代课";
		}else if(TipsayConstants.TYPE_2.equals(type)) {
			typename="管课";
		}else {
			on.put("success", false);
			on.put("msg", "参数丢失");
			return on.toJSONString();
		}
		
		if (tipsay == null || tipsay.getIsDeleted() == TipsayConstants.IF_1) {
			on.put("success", false);
			on.put("msg", typename+"记录不存在或者已经删除");
			return on.toJSONString();
		}
		CourseSchedule courseSchedule = null;
		if (TipsayConstants.TIPSAY_STATE_1.equals(tipsay.getState())) {
			// 这个删除需要撤销原来课程
			courseSchedule = courseScheduleService.findOne(tipsay
					.getCourseScheduleId());
			if (courseSchedule == null) {
				on.put("success", false);
				on.put("msg", typename+"对应的课不存在");
				return on.toJSONString();
			}
			String checkMess = tipsayService.checkCanDeleted(tipsay, courseSchedule);
			if (StringUtils.isNotBlank(checkMess)) {
				on.put("success", false);
				on.put("msg", checkMess);
				return on.toJSONString();
			}
		}

		try {
			tipsayService.deleteTipsayOrSaveCourseSchedule(tipsay,
					courseSchedule);

		} catch (Exception e) {
			e.printStackTrace();
			on.put("success", false);
			on.put("msg","操作失败");
			return on.toJSONString();
		}
		on.put("success", true);
		on.put("msg","操作成功");
		return on.toJSONString();
	}
	
	
	@ResponseBody
	@RequestMapping("/agreeOrNot")
	@ControllerInfo("管理员审核")
	public String agreeOrNot(String tipsayId,String state,String teacherId) {
		JSONObject on = new JSONObject();
		Tipsay tipsay = tipsayService.findOne(tipsayId);
		if (tipsay == null || tipsay.getIsDeleted() == TipsayConstants.IF_1) {
			on.put("success", false);
			on.put("msg","记录不存在或者已经删除");
			return on.toJSONString();
		}
		if(!TipsayConstants.TIPSAY_STATE_0.equals(tipsay.getState())) {
			on.put("success", false);
			on.put("msg","记录已调整，请刷新后操作");
			return on.toJSONString();
		}
		
		Date nowDate=new Date();
		List<DateInfo> list = dateInfoService.findByWeek(tipsay.getSchoolId(), tipsay.getAcadyear(), tipsay.getSemester(), tipsay.getWeekOfWorktime());
		Date chooseDate=null;
		for(DateInfo d:list){
			if(d.getWeekday()-1==tipsay.getDayOfWeek()){
				chooseDate=d.getInfoDate();
			}
		}
		if(chooseDate==null){
			on.put("success", false);
			on.put("msg","在节假日设置未找到对应代课时间");
			return on.toJSONString();
		}
		if(DateUtils.compareForDay(nowDate, chooseDate) >=0){
			on.put("success", false);
			on.put("msg","时间已经过去，不能操作");
			return on.toJSONString();
		}
		
		String typename="";
		if(TipsayConstants.TYPE_1.equals(tipsay.getType())) {
			typename="代课";
		}else if(TipsayConstants.TYPE_2.equals(tipsay.getType())) {
			typename="管课";
		}
		//判断是不是原来的
		CourseSchedule courseSchedule = courseScheduleService.findOne(tipsay
				.getCourseScheduleId());
		if (courseSchedule == null) {
			on.put("success", false);
			on.put("msg","对应课程数据不存在");
			return on.toJSONString();
		}
		String errorMes = checkReturnError(tipsay, courseSchedule);
		//判断教师在这个时间有没有课
		List<String> tList=new ArrayList<>();
		tList.add(tipsay.getNewTeacherId());
		String error =tipsayService.checkTimeByTeacher(tList, courseSchedule);
		if (StringUtils.isNotBlank(error)) {
			on.put("success", false);
			on.put("msg",typename+"老师存在时间冲突");
			return on.toJSONString();
		}
		if(StringUtils.isNotBlank(errorMes)) {
			on.put("success", false);
			on.put("msg",errorMes);
			return on.toJSONString();
		}
		TipsayEx tipsayEx = new TipsayEx();
		tipsayEx.setAuditorId(teacherId);
		tipsayEx.setAuditorType(TipsayConstants.AUDITOR_TYPE_1);
		tipsayEx.setCreationTime(new Date());
		
		tipsayEx.setId(UuidUtils.generateUuid());
		tipsayEx.setTipsayId(tipsay.getId());
		tipsayEx.setSchoolId(tipsay.getSchoolId());
		tipsayEx.setSourceType(TipsayConstants.TYPE_01);
		
		if("1".equals(state)) {
			tipsayEx.setState(TipsayConstants.TIPSAY_STATE_1);
			tipsayEx.setRemark("同意");
			tipsay.setState(TipsayConstants.TIPSAY_STATE_1);
			tipsay.setModifyTime(new Date());
		}else if("0".equals(state)) {
			tipsayEx.setState(TipsayConstants.TIPSAY_STATE_2);
			tipsayEx.setRemark("不同意");
			tipsay.setState(TipsayConstants.TIPSAY_STATE_2);
			tipsay.setModifyTime(new Date());
		}else {
			on.put("success", false);
			on.put("msg","参数错误");
			return on.toJSONString();
		}
		try {
			tipsayService.saveAll(tipsay, tipsayEx, courseSchedule);
		} catch (Exception e) {
			e.printStackTrace();
			on.put("success", false);
			on.put("msg","操作失败");
			return on.toJSONString();
		}
		on.put("success", true);
		on.put("msg","操作成功");
		return on.toJSONString();
	}
	
	private String checkReturnError(Tipsay tipsay,CourseSchedule courseSchedule) {
    	if(!tipsayService.checkCourseSchedule(courseSchedule,tipsay,false)) {
			return "对应课程数据已被修改,无法操作";
		}
		//只要原教师包括该老师就可以操作
		List<CourseSchedule> list=new ArrayList<CourseSchedule>();
		list.add(courseSchedule);
		courseScheduleService.makeTeacherSet(list);
		if(CollectionUtils.isNotEmpty(courseSchedule.getTeacherIds())
				&& courseSchedule.getTeacherIds().contains(tipsay.getOperator())) {
			return null;
		}else {
			return "对应课程数据已被修改,无法操作";
		}
    }
	@RequestMapping("/showTipsayItem/page")
	@ControllerInfo("展开详细")
	public String showTipsayItem(String unitId, String userId, String teacherId,String adminType,String type,
			String acadyear,String semester, String week,String statType,String tipsayId,ModelMap map){
		map.put("unitId", unitId);
		map.put("userId", userId);
		map.put("teacherId", teacherId);
		map.put("adminType", adminType);
		map.put("type", type);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("week", week);
		map.put("statType", statType);
		
		Tipsay tipsay = tipsayService.findOne(tipsayId);
		if (tipsay == null || tipsay.getIsDeleted() == TipsayConstants.IF_1) {
			map.put("error","记录不存在或者已经删除");
			if("1".equals(adminType)) {
				return "/basedata/tipsayh5/tipsayItem1.ftl";
			}else {
				return "/basedata/tipsayh5/tipsayItem2.ftl";
			}
		}else {
			List<Tipsay> tipsayList = new ArrayList<>();
			tipsayList.add(tipsay);
			List<TipsayDto> list = tipsayService.tipsayToTipsayDto(new HashMap<>(), tipsayList, false);
			map.put("tipsayDto", list.get(0));
		}
		if("1".equals(adminType)) {
			
			if(TipsayConstants.TIPSAY_STATE_0.equals(tipsay.getState()) && TipsayConstants.TIPSAY_TYPE_03.equals(tipsay.getTipsayType())) {
				//需要安排
				List<TeachGroupDto> groupDtoList = findGroupInState(tipsay,null);
				map.put("groupDtoList", groupDtoList);
			}
			return "/basedata/tipsayh5/tipsayItem1.ftl";
		}else {
			return "/basedata/tipsayh5/tipsayItem2.ftl";
		}
	}
	
	public List<TeachGroupDto> findGroupInState(Tipsay tipsay,String searchTeacherName){
		List<TeachGroupDto> allGroupDtoList = findGroupDtoList(tipsay.getSchoolId());
		List<TeachGroupDto> groupDtoList=new ArrayList<>();
		TeachGroupDto returnDto;
		if(CollectionUtils.isNotEmpty(allGroupDtoList)){
			Set<String> noTeacherId=new HashSet<>();
			CourseScheduleDto searchDto=new CourseScheduleDto();
			searchDto.setSchoolId(tipsay.getSchoolId());
			searchDto.setAcadyear(tipsay.getAcadyear());
			searchDto.setSemester(tipsay.getSemester());
			searchDto.setWeekOfWorktime1(tipsay.getWeekOfWorktime());
			searchDto.setWeekOfWorktime2(tipsay.getWeekOfWorktime());
			List<String> timeStr=new ArrayList<>();
			timeStr.add(tipsay.getDayOfWeek()+"_"+tipsay.getPeriodInterval()+"_"+tipsay.getPeriod());
			List<CourseSchedule> sameList = courseScheduleService.findByTimes(searchDto, timeStr.toArray(new String[0]));
			
			if(CollectionUtils.isNotEmpty(sameList)) {
				courseScheduleService.makeTeacherSet(sameList);
				for(CourseSchedule c:sameList) {
					if(CollectionUtils.isNotEmpty(c.getTeacherIds())) {
						noTeacherId.addAll(c.getTeacherIds());
					}
				}
			}
			Set<String> oldTeacherId=new HashSet<>();
			if(StringUtils.isNotBlank(tipsay.getTeacherId())) {
				oldTeacherId.add(tipsay.getTeacherId());
			}
			if(StringUtils.isNotBlank(tipsay.getTeacherExIds())) {
				String[] arr = tipsay.getTeacherExIds().split(",");
				for (String s : arr) {
					oldTeacherId.add(s);
				}
			}
			
			List<String[]> showList;
			for(TeachGroupDto dto:allGroupDtoList) {
				showList=new ArrayList<>();
				if(CollectionUtils.isEmpty(dto.getMainTeacherList())) {
					continue;
				}
				for(TeacherDto item:dto.getMainTeacherList()) {
					if(oldTeacherId.contains(item.getTeacherId())) {
						continue;
					}
					if(StringUtils.isNotBlank(searchTeacherName)) {
						if(item.getTeacherName().indexOf(searchTeacherName)==-1) {
							continue;
						}
					}
					if(noTeacherId.contains(item.getTeacherId())) {
						showList.add(new String[] {item.getTeacherId(),item.getTeacherName(),"1"});
					}else {
						showList.add(new String[] {item.getTeacherId(),item.getTeacherName(),"0"});
					}
				}
				if(CollectionUtils.isEmpty(showList)) {
					continue;
				}
				returnDto=new TeachGroupDto();
				returnDto.setTeachGroupName(dto.getTeachGroupName());
				returnDto.setShowList(showList);
				groupDtoList.add(returnDto);
			}
			
		}
		return groupDtoList;
	}
	@ResponseBody
	@RequestMapping("/arrangeTeacher")
	@ControllerInfo("管理员安排老师")
	public String arrangeTeacher(String tipsayId,String newTeacherId,String teacherId) {
		JSONObject on = new JSONObject();
		if(StringUtils.isBlank(newTeacherId)) {
			on.put("success", false);
			on.put("msg","参数错误");
			return on.toJSONString();
		}
		Tipsay tipsay = tipsayService.findOne(tipsayId);
		if (tipsay == null || tipsay.getIsDeleted() == TipsayConstants.IF_1) {
			on.put("success", false);
			on.put("msg","记录不存在或者已经删除");
			return on.toJSONString();
		}
		if(!TipsayConstants.TIPSAY_STATE_0.equals(tipsay.getState())) {
			on.put("success", false);
			on.put("msg","记录已调整，请刷新后操作");
			return on.toJSONString();
		}
		
		//时间是不是过期
	
		Date nowDate=new Date();
		List<DateInfo> list = dateInfoService.findByWeek(tipsay.getSchoolId(), tipsay.getAcadyear(), tipsay.getSemester(), tipsay.getWeekOfWorktime());
		Date chooseDate=null;
		for(DateInfo d:list){
			if(d.getWeekday()-1==tipsay.getDayOfWeek()){
				chooseDate=d.getInfoDate();
			}
		}
		if(chooseDate==null){
			on.put("success", false);
			on.put("msg","在节假日设置未找到对应代课时间");
			return on.toJSONString();
		}
		if(DateUtils.compareForDay(nowDate, chooseDate) >=0){
			on.put("success", false);
			on.put("msg","时间已经过去，不能操作");
			return on.toJSONString();
		}
		
		String typename="";
		if(TipsayConstants.TYPE_1.equals(tipsay.getType())) {
			typename="代课";
		}else if(TipsayConstants.TYPE_2.equals(tipsay.getType())) {
			typename="管课";
		}
		//判断是不是原来的
		CourseSchedule courseSchedule = courseScheduleService.findOne(tipsay
				.getCourseScheduleId());
		if (courseSchedule == null) {
			on.put("success", false);
			on.put("msg","对应课程数据不存在");
			return on.toJSONString();
		}
		String errorMes = checkReturnError(tipsay, courseSchedule);
		if(StringUtils.isNotBlank(errorMes)) {
			on.put("success", false);
			on.put("msg",errorMes);
			return on.toJSONString();
		}
		
		if(courseSchedule.getTeacherIds().contains(newTeacherId)) {
			on.put("success", false);
			on.put("msg","该课程本来就是由该老师上课，无需"+typename);
			return on.toJSONString();
		}
		//判断教师在这个时间有没有课
		List<String> tList=new ArrayList<>();
		tList.add(newTeacherId);
		String error =tipsayService.checkTimeByTeacher(tList, courseSchedule);
		if (StringUtils.isNotBlank(error)) {
			on.put("success", false);
			on.put("msg",typename+"老师存在时间冲突");
			return on.toJSONString();
		}
		 
		//重新设置教师参数
		String exIds = "";
		for (String s : courseSchedule.getTeacherIds()) {
			if (!s.equals(courseSchedule.getTeacherId())) {
				exIds = exIds + "," + s;
			}
		}

		if (StringUtils.isNotBlank(exIds)) {
			exIds = exIds.substring(1);
		}
		if (StringUtils.isNotBlank(exIds)) {
			exIds = exIds.substring(1);
		}
		tipsay.setTeacherExIds(exIds);
		tipsay.setTeacherId(courseSchedule.getTeacherId());
		
		//参数填充
		tipsay.setNewTeacherId(newTeacherId);
		tipsay.setModifyTime(new Date());
		tipsay.setState(TipsayConstants.TIPSAY_STATE_1);
		
		TipsayEx tipsayEx = new TipsayEx();
		tipsayEx.setAuditorId(teacherId);
		tipsayEx.setAuditorType(TipsayConstants.AUDITOR_TYPE_1);
		tipsayEx.setCreationTime(new Date());
		tipsayEx.setRemark("完成安排教师");
		tipsayEx.setId(UuidUtils.generateUuid());
		tipsayEx.setState(TipsayConstants.TIPSAY_STATE_1);
		tipsayEx.setTipsayId(tipsay.getId());
		
		tipsayEx.setSchoolId(tipsay.getSchoolId());
		tipsayEx.setSourceType(TipsayConstants.TYPE_01);
		
		try {
			tipsayService.saveAll(tipsay, tipsayEx, courseSchedule);
		} catch (Exception e) {
			e.printStackTrace();
			on.put("success", false);
			on.put("msg","操作失败");
			return on.toJSONString();
		}
		on.put("success", true);
		on.put("msg","操作成功");
		return on.toJSONString();
	}
	
	
	@ResponseBody
	@RequestMapping("/findTeacherList2")
	@ControllerInfo("申请安排教师列表")
	public String findTeacherList2(String unitId,String tipsayId,String searchTeacherName) {
		Tipsay tipsay = tipsayService.findOne(tipsayId);
		List<TeachGroupDto> groupDtoList=new ArrayList<>();
		if(tipsay!=null) {
			groupDtoList=findGroupInState(tipsay,searchTeacherName);
		}else {
			List<TeachGroupDto> allGroupDtoList = findGroupDtoList(unitId);
			List<String[]> showList = new ArrayList<>();
			TeachGroupDto returnDto;
			for(TeachGroupDto dto:allGroupDtoList) {
				
				if(CollectionUtils.isEmpty(dto.getMainTeacherList())) {
					continue;
				}
				for(TeacherDto item:dto.getMainTeacherList()) {
					if(StringUtils.isNotBlank(searchTeacherName)) {
						if(item.getTeacherName().indexOf(searchTeacherName)==-1) {
							continue;
						}
					}
					showList.add(new String[] {item.getTeacherId(),item.getTeacherName(),"0"});
				}
				if(CollectionUtils.isEmpty(showList)) {
					continue;
				}
				returnDto = new TeachGroupDto();
				returnDto.setTeachGroupName(dto.getTeachGroupName());
				returnDto.setShowList(showList);
				groupDtoList.add(returnDto);
			}
		}

		JSONObject on = new JSONObject();
		if(CollectionUtils.isEmpty(groupDtoList)) {
			on.put("success", false);
		}else {
			on.put("success", true);
			on.put("groupDtoList", groupDtoList);
		}
		return on.toJSONString();
	}
	

    /**
     * 填充课程名称，班级名称，教师名称
     * @param courseScheduleList
     */
    private void makeCourseSchedule(List<CourseSchedule> courseScheduleList) {
        if (CollectionUtils.isEmpty(courseScheduleList)) {
            return;
        }
        Set<String> courseIdSet = new HashSet<>();
        Set<String> classIdSet = new HashSet<>();
        Set<String> teacherIdSet = new HashSet<>();
        for (CourseSchedule one : courseScheduleList) {
            courseIdSet.add(one.getSubjectId());
            classIdSet.add(one.getClassId());
            teacherIdSet.add(one.getTeacherId());
        }
        Map<String, String> courseMap =
                courseService.findPartCouByIds(courseIdSet.toArray(new String[0]));
        Map<String, String> clazzMap =
                EntityUtils.getMap(classService.findListByIds(classIdSet.toArray(new String[0])), Clazz::getId, Clazz::getClassNameDynamic);
        Map<String, String> teachClazzMap =
                EntityUtils.getMap(teachClassService.findListByIds(classIdSet.toArray(new String[0])), TeachClass::getId, TeachClass::getName);
        Map<String, String> teacherMap =
                teacherService.findPartByTeacher(teacherIdSet.toArray(new String[0]));
        for (CourseSchedule one : courseScheduleList) {
            one.setSubjectName(courseMap.get(one.getSubjectId()));
            if (StringUtils.isNotBlank(clazzMap.get(one.getClassId()))) {
                one.setClassName(clazzMap.get(one.getClassId()));
            } else {
                one.setClassName(teachClazzMap.get(one.getClassId()));
            }
            one.setTeacherName(teacherMap.get(one.getTeacherId()));
        }
    }

    private AdjustedDetail makeAdjustedDetail(CourseSchedule courseSchedule, String adjustId, String adjustedType) {
        AdjustedDetail adjustedDetail = new AdjustedDetail();
        adjustedDetail.setId(UuidUtils.generateUuid());
        adjustedDetail.setAdjustedId(adjustId);
        adjustedDetail.setCourseScheduleId(courseSchedule.getId());
        adjustedDetail.setClassId(courseSchedule.getClassId());
        adjustedDetail.setSubjectId(courseSchedule.getSubjectId());
        adjustedDetail.setSchoolId(courseSchedule.getSchoolId());
        adjustedDetail.setTeacherId(courseSchedule.getTeacherId());
        adjustedDetail.setWeekOfWorktime(courseSchedule.getWeekOfWorktime());
        adjustedDetail.setDayOfWeek(courseSchedule.getDayOfWeek());
        adjustedDetail.setPeriodInterval(courseSchedule.getPeriodInterval());
        adjustedDetail.setPeriod(courseSchedule.getPeriod());
        adjustedDetail.setCreationTime(new Date());
        adjustedDetail.setModifyTime(new Date());
        adjustedDetail.setAdjustedType(adjustedType);
        return adjustedDetail;
    }

    /**
     * 检测需调与被调课程是否存在冲突
     * @param adjustIdArr
     * @param beenAdjustIdArr
     * @param courseScheduleMap
     */
    private String checkConflict(String unitId, String[] adjustIdArr, String[] beenAdjustIdArr, Map<String, CourseSchedule> courseScheduleMap) {
        Set<String> teacherIdsSet = new HashSet<>();
        for (CourseSchedule one : courseScheduleMap.values()) {
            teacherIdsSet.add(one.getTeacherId());
        }
        Semester semester = semesterService.findCurrentSemester(2, unitId);
        DateInfo dateInfo = dateInfoService.getDate(unitId, semester.getAcadyear(), semester.getSemester(), new Date());
        List<CourseSchedule> courseScheduleList = courseScheduleService.findListByTeacherIdsIn(unitId, semester.getAcadyear(), semester.getSemester(), teacherIdsSet.toArray(new String[0]));
        List<Adjusted> adjustedList = adjustedService.findListBy(new String[]{"schoolId", "acadyear", "semester", "state"}, new Object[]{unitId, semester.getAcadyear(), semester.getSemester(), "0"});
        List<String> adjustedIdList = EntityUtils.getList(adjustedList, Adjusted::getId);
        // 正在申请的列表
        Map<String, String> applyingCourseScheduleMap = adjustedDetailService.findCourseScheduleIdMapByAdjustedIdIn(adjustedIdList.toArray(new String[0]));
        Map<String, CourseSchedule> locationMap = new HashMap<>();
        for (CourseSchedule one : courseScheduleList) {
            String location = one.getTeacherId() + "_" + one.getWeekOfWorktime() + "_" + one.getDayOfWeek() + "_" + one.getPeriodInterval() + "_" + one.getPeriod();
            locationMap.put(location, one);
        }
        for (int i = 0; i < adjustIdArr.length; i++) {
            CourseSchedule adjust = courseScheduleMap.get(adjustIdArr[i]);
            CourseSchedule beenAdjust = courseScheduleMap.get(beenAdjustIdArr[i]);
            if (adjust.getWeekOfWorktime() < dateInfo.getWeek().intValue()) {
                return "无法调整已发生的课程";
            } else if (adjust.getWeekOfWorktime() == dateInfo.getWeek().intValue()) {
                if (adjust.getDayOfWeek() + 1 < dateInfo.getWeekday()) {
                    return "无法调整已发生的课程";
                }
            }
            if (beenAdjust.getWeekOfWorktime() < dateInfo.getWeek().intValue()) {
                return "无法调整已发生的课程";
            } else if (beenAdjust.getWeekOfWorktime() == dateInfo.getWeek().intValue()) {
                if (beenAdjust.getDayOfWeek() + 1 < dateInfo.getWeekday()) {
                    return "无法调整已发生的课程";
                }
            }
            String adjustTargetLocate = adjust.getTeacherId() + "_" + beenAdjust.getWeekOfWorktime() + "_" + beenAdjust.getDayOfWeek() + "_" + beenAdjust.getPeriodInterval() + "_" + beenAdjust.getPeriod();
            String beenAdjustTargetLocate = beenAdjust.getTeacherId() + "_" + adjust.getWeekOfWorktime() + "_" + adjust.getDayOfWeek() + "_" + adjust.getPeriodInterval() + "_" + adjust.getPeriod();
            if (locationMap.get(adjustTargetLocate) != null) {
                return "第" + (i + 1) + "项需调课程教师课程冲突";
            }
            if (locationMap.get(beenAdjustTargetLocate) != null) {
                return "第" + (i + 1) + "项被调课程教师课程冲突";
            }
            if (applyingCourseScheduleMap.containsKey(adjustIdArr[i])) {
                if (applyingCourseScheduleMap.get(adjustIdArr[i]).equals(beenAdjustIdArr[i])) {
                    return "第" + (i + 1) + "项正在申请中，无需重复提交";
                }
            }
        }
        return "noneError";
    }

    private List<AdjustedDto> makeAdjustDtoList(List<Adjusted> adjustedList, List<AdjustedDetail> adjustedDetailList) {
        List<AdjustedDto> returnList = new ArrayList<>();
        if(CollectionUtils.isEmpty(adjustedList)) {
            return returnList;
        }
        Set<String> courseIdSet = new HashSet<>();
        Set<String> classIdSet = new HashSet<>();
        Set<String> teacherIdSet = new HashSet<>();
        Map<String, AdjustedDetail> adjustedDetailMap = new HashMap<>();
        Map<String, AdjustedDetail> beenAdjustedDetailMap = new HashMap<>();
        for (AdjustedDetail one : adjustedDetailList) {
            courseIdSet.add(one.getSubjectId());
            classIdSet.add(one.getClassId());
            teacherIdSet.add(one.getTeacherId());
            if ("01".equals(one.getAdjustedType())) {
                adjustedDetailMap.put(one.getAdjustedId(), one);
            } else if ("02".equals(one.getAdjustedType())) {
                beenAdjustedDetailMap.put(one.getAdjustedId(), one);
            }
        }
        Map<String, String> courseMap =
                courseService.findPartCouByIds(courseIdSet.toArray(new String[0]));
        Map<String, String> clazzMap =
                EntityUtils.getMap(classService.findListByIds(classIdSet.toArray(new String[0])), Clazz::getId, Clazz::getClassNameDynamic);
        Map<String, String> teachClazzMap =
                EntityUtils.getMap(teachClassService.findListByIds(classIdSet.toArray(new String[0])), TeachClass::getId, TeachClass::getName);
        Map<String, String> teacherMap =
                teacherService.findPartByTeacher(teacherIdSet.toArray(new String[0]));
        String unitId = adjustedList.get(0).getSchoolId();
        Semester semester = semesterService.findCurrentSemester(2, unitId);
        DateInfo today = dateInfoService.getDate(unitId, semester.getAcadyear(), semester.getSemester(), new Date());
        AdjustedDto tmp;
        AdjustedDetail adjustTmp;
        AdjustedDetail beenAdjustTmp;
        for (Adjusted one : adjustedList) {
            tmp = new AdjustedDto();
            adjustTmp = adjustedDetailMap.get(one.getId());
            beenAdjustTmp = beenAdjustedDetailMap.get(one.getId());
            tmp.setId(one.getId());
            tmp.setOperatorName(teacherMap.get(one.getOperator()));
            tmp.setAdjustingTeacherName(teacherMap.get(adjustTmp.getTeacherId()));
            tmp.setBeenAdjustedTeacherName(teacherMap.get(beenAdjustTmp.getTeacherId()));
            if(adjustTmp!=null){
                String className = clazzMap.get(adjustTmp.getClassId());
                if (className == null) {
                    className = teachClazzMap.get(adjustTmp.getClassId());
                }
                tmp.setClassName(className == null ? "" : className);
                tmp.setAdjustingId(adjustTmp.getId());
                tmp.setAdjustingName("" + adjustTmp.getWeekOfWorktime() + "周" + BaseConstants.dayOfWeekMap2.get(adjustTmp.getDayOfWeek().toString()) + BaseConstants.PERIOD_INTERVAL_Map.get(adjustTmp.getPeriodInterval()) + "第" + adjustTmp.getPeriod() + "节");
                if ("1".equals(one.getState())) {
                    if (adjustTmp.getWeekOfWorktime() > today.getWeek()) {
                        tmp.setCanDelete(true);
                    } else if (adjustTmp.getWeekOfWorktime() == today.getWeek()) {
                        if (adjustTmp.getDayOfWeek() + 1 >= today.getWeekday()) {
                            tmp.setCanDelete(true);
                        }
                    }
                } else {
                    tmp.setCanDelete(true);
                }
            }

            if(beenAdjustTmp!=null){
                tmp.setBeenAdjustedId(beenAdjustTmp.getId());
                tmp.setBeenAdjustedName("" + beenAdjustTmp.getWeekOfWorktime() + "周" + BaseConstants.dayOfWeekMap2.get(beenAdjustTmp.getDayOfWeek().toString()) + BaseConstants.PERIOD_INTERVAL_Map.get(beenAdjustTmp.getPeriodInterval()) + "第" + beenAdjustTmp.getPeriod() + "节");
                tmp.setBeenAdjustedTeacherName(teacherMap.get(beenAdjustTmp.getTeacherId()) == null ? "" : teacherMap.get(beenAdjustTmp.getTeacherId()));
                if ("1".equals(one.getState())) {
                    if (beenAdjustTmp.getWeekOfWorktime() > today.getWeek()) {
                        tmp.setCanDelete(true);
                    } else if (beenAdjustTmp.getWeekOfWorktime() == today.getWeek()) {
                        if (beenAdjustTmp.getDayOfWeek() + 1 >= today.getWeekday()) {
                            tmp.setCanDelete(true);
                        }
                    }
                } else {
                    tmp.setCanDelete(true);
                }
            }
            if (StringUtils.containAny(one.getRemark(), "教务安排直接安排") || StringUtils.containAny(one.getRemark(), "行政班调课")) {
                tmp.setApplyType("教务安排");
            } else {
                tmp.setApplyType("自主申请");
            }
            tmp.setRemark(one.getRemark());
            tmp.setState(one.getState());
            returnList.add(tmp);
        }
        return returnList;
    }
}
