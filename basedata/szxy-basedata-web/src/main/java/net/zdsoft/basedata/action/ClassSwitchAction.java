package net.zdsoft.basedata.action;

import com.alibaba.fastjson.JSON;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.constant.TipsayConstants;
import net.zdsoft.basedata.dto.AdjustedDto;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 教师调课申请
 *
 * @Author
 * @since 2018/10/17
 */
@RequestMapping("/basedata/classswitch")
@Controller
public class ClassSwitchAction extends BaseAction {

    @Autowired
    private CourseScheduleService courseScheduleService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private DateInfoService dateInfoService;
    @Autowired
    private ClassService classService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private AdjustedService adjustedService;
    @Autowired
    private AdjustedDetailService adjustedDetailService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CustomRoleRemoteService customRoleRemoteService;
    @Autowired
    private TeachClassService teachClassService;
    @Autowired
    private TipsayService tipsayService;
    @Autowired
    private SchoolCalendarService schoolCalendarService;

    @RequestMapping("/index/page")
    @ControllerInfo("教师调课申请首页")
    public String classSwitchIndex(ModelMap map) {
        String userId = getLoginInfo().getUserId();
        String unitId = getLoginInfo().getUnitId();
        Teacher teacher = teacherService.findByUserId(userId);
        map.put("schoolId", unitId);
        map.put("teacherId",teacher.getId());
        map.put("isAdmin", isAdmin(unitId, userId));
        return "basedata/teacherClassSwitch/classSwitchIndex.ftl";
    }

    @RequestMapping("/apply/index/page")
    public String classSwitchApply(String schoolId, String teacherId, String nowAcadyear, String nowSemester, ModelMap map) {
        Teacher teacher = teacherService.findOne(teacherId);
        map.put("schoolId", schoolId);
        map.put("teacherId", teacherId);
        map.put("teacherName", teacher.getTeacherName());
        map.put("nowAcadyear", nowAcadyear);
        map.put("nowSemester", nowSemester);
        List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(schoolId, nowAcadyear, Integer.valueOf(nowSemester));
        if (CollectionUtils.isEmpty(dateInfoList)) {
            return errorFtl(map, "周次为空，请先维护基础信息节假日设置");
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
        List<Grade> gradeList = gradeService.findByUnitId(schoolId);
        Integer mm=0;
        Integer am = 0;
        Integer pm = 0;
        Integer night = 0;
        for (Grade one : gradeList) {
        	if (mm < one.getMornPeriods()) {
        		mm = one.getMornPeriods();
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
        map.put("mm", mm);
        map.put("am", am);
        map.put("pm", pm);
        map.put("night", night);

        return "basedata/teacherClassSwitch/applyIndex.ftl";
    }

    /**
     * @param week
     * @param objectId teacherId 或 classId
     * @return
     */
    @RequestMapping("apply/timetableinfo")
    @ResponseBody
    public String timeTableInfo(String nowAcadyear, String nowSemester, String week, String objectId, String teacherId) {
        if (StringUtils.isBlank(week)) {
            return error("请选择周次");
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
            return error("第" + week + "周无课程");
        }
        makeCourseSchedule(returnList);
        DateInfo dateInfo = dateInfoService.getDate(getLoginInfo().getUnitId(), nowAcadyear, Integer.valueOf(nowSemester), new Date());
        Map<String, List<CourseSchedule>> returnMap = returnList.stream().collect(Collectors.groupingBy(e -> e.getDayOfWeek() + "_" + e.getPeriodInterval() + "_" + e.getPeriod()));
        returnList.stream().forEach(e -> {
            if (dateInfo != null && (e.getWeekOfWorktime() < dateInfo.getWeek() || (e.getWeekOfWorktime() == dateInfo.getWeek() && (e.getDayOfWeek() + 1) < dateInfo.getWeekday()))) {
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
    public String switchCommit(String adjustIds, String beenAdjustIds, String remark) {
        if (StringUtils.isBlank(adjustIds) || StringUtils.isBlank(beenAdjustIds)) {
            return error("数据为空");
        }
        String[] adjustIdArr = StringUtils.split(adjustIds, ',');
        String[] beenAdjustIdArr = StringUtils.split(beenAdjustIds, ',');
        List<CourseSchedule> courseScheduleList =
                courseScheduleService.findListByIdIn(ArrayUtil.concat(adjustIdArr, beenAdjustIdArr));
        Map<String, CourseSchedule> courseScheduleMap =
                EntityUtils.getMap(courseScheduleList, CourseSchedule::getId);
        // 冲突检测
        String msg = checkConflict(adjustIdArr, beenAdjustIdArr, courseScheduleMap);
        if (!"noneError".equals(msg)) {
            return error(msg);
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
                return error("第" + (i + 1) + "项需调课程与被调课程老师相同，不需要调换");
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
            return error("申请失败");
        }
        try {
            List<AdjustedDto> resultList = makeAdjustDtoList(adjustedList, adjustedDetailList,null);
            final StringBuffer stringBuffer = new StringBuffer("申请人：" + resultList.get(0).getOperatorName() + ",");
            final StringBuffer stringBuffer2 = new StringBuffer("申请人：" +resultList.get(0).getOperatorName());
            for (AdjustedDto one : resultList) {
                stringBuffer.append("由 " + one.getAdjustingName() + " 调至 " + one.getBeenAdjustedName() + "；");
            }
            final String[] adminIds = tipsayService.findUserRole(getLoginInfo().getUnitId()).toArray(new String[0]);
            new Thread(new Runnable() {
                public void run() {
                	stringBuffer2.append("老师，有调课信息需要审核。");
                    tipsayService.pushMessage(null, adminIds, stringBuffer2.toString(), "调课申请");
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success("申请成功，请等待审核");
    }

    @RequestMapping("/apply/copyswitch")
    @ResponseBody
    public String copySwitch(String adjustId, String beenAdjustId, String copyWeeks, String teacherId, String nowAcadyear, String nowSemester) {
        copyWeeks = StringUtils.substring(copyWeeks, 0, copyWeeks.length() - 1);
        String[] copyWeekArr = StringUtils.split(copyWeeks, ',');
        CourseSchedule adjust = courseScheduleService.findOne(adjustId);
        CourseSchedule beenAdjust = courseScheduleService.findOne(beenAdjustId);
        List<CourseSchedule> returnList = new ArrayList<>();
        List<CourseSchedule> copyAdjustList =
                courseScheduleService.findListBy(
                		new String[]{"isDeleted","acadyear", "semester", "teacherId", "dayOfWeek", "periodInterval", "period"}, 
                		new Object[]{0, nowAcadyear, Integer.valueOf(nowSemester), teacherId, adjust.getDayOfWeek(), adjust.getPeriodInterval(), adjust.getPeriod()});
        List<CourseSchedule> copyBeenAdjustList =
                courseScheduleService.findListBy(new String[]{"isDeleted", "acadyear", "semester", "classId", "dayOfWeek", "periodInterval", "period"}, 
                		new Object[]{0, nowAcadyear, Integer.valueOf(nowSemester), beenAdjust.getClassId(), beenAdjust.getDayOfWeek(), beenAdjust.getPeriodInterval(), beenAdjust.getPeriod()});
        Map<Integer, CourseSchedule> copyAdjustMap = EntityUtils.getMap(copyAdjustList, CourseSchedule::getWeekOfWorktime);
        Map<Integer, CourseSchedule> copyBeenAdjustMap = EntityUtils.getMap(copyBeenAdjustList, CourseSchedule::getWeekOfWorktime);
        int weekDif = beenAdjust.getWeekOfWorktime() - adjust.getWeekOfWorktime();
        for (String one : copyWeekArr) {
            if (copyAdjustMap.containsKey(Integer.valueOf(one)) && copyBeenAdjustMap.containsKey(Integer.valueOf(one) + weekDif)) {
                returnList.add(copyAdjustMap.get(Integer.valueOf(one)));
                returnList.add(copyBeenAdjustMap.get(Integer.valueOf(one) + weekDif));
            } else {
                return error("第" + one + "周无需调课程或被调课程");
            }
        }
        if (CollectionUtils.isEmpty(returnList)) {
            return error("无可供拷贝课程");
        }
        makeCourseSchedule(returnList);
        String resultJson = SUtils.s(returnList);
        return JSON.toJSONString(new ResultDto().setCode("00").setSuccess(true).setBusinessValue(resultJson));
    }

    @RequestMapping("/switchhead/page")
    public String switchHeadIndex(String schoolId, String teacherId, String from, ModelMap map) {
        if ("02".equals(from) && !isAdmin(schoolId, getLoginInfo().getUserId())) {
            return errorFtl(map, "无管理员权限");
        }
        List<String> acadyearList = semesterService.findAcadeyearList();
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"学年学期不存在");
        }
        Semester semester = semesterService.findCurrentSemester(2, schoolId);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(schoolId, semester.getAcadyear(), semester.getSemester());
        if (CollectionUtils.isEmpty(dateInfoList)) {
            return errorFtl(map, "周次为空，请先维护基础信息节假日设置");
        }
        Map<String, Integer> cur2Max =schoolCalendarService.findCurrentWeekAndMaxWeek(semester.getAcadyear(), String.valueOf(semester.getSemester()), schoolId);
        Integer nowWeek = cur2Max.get("current");
        Integer max = cur2Max.get("max");
        if(nowWeek==null){
        	nowWeek=1;
        }
        map.put("nowWeek", nowWeek);
        map.put("max", max);
        /*List<DateInfo> weekInfoList = new ArrayList<>();
        DateInfo nowWeek = null;
        Date today = new Date();
        int index = 1;
        for (DateInfo one : dateInfoList) {
            if (Integer.valueOf(index).equals(one.getWeek())) {
                if (today.getTime() - one.getInfoDate().getTime() > 0L && today.getTime() - one.getInfoDate().getTime() < 1000 * 60 * 60 * 24 * 7L) {
                    if (nowWeek == null) {
                        nowWeek = one;
                    }
                }
                weekInfoList.add(one);
                index++;
            }
        }*/
        
        String unitId = getLoginInfo().getUnitId();
        List<Grade> gradeList = gradeService.findByUnitId(unitId);
        map.put("gradeList", gradeList);

        map.put("schoolId", schoolId);
        map.put("nowWeek", nowWeek);
        // map.put("weekInfoList", weekInfoList);
        map.put("teacherId", teacherId);
        map.put("from", from);
        return "basedata/teacherClassSwitch/switchHead.ftl";
    }

    @RequestMapping("/list/table")
    public String switchListDetail(String teacherId, String acadyear, String semester, String gradeId, String week, ModelMap map) {
        List<AdjustedDto> returnList = new ArrayList<>();
        List<Adjusted> adjustedList = adjustedService.findListByTeacherIdAndWeek(acadyear, semester, teacherId, week);
        if (CollectionUtils.isEmpty(adjustedList)) {
            map.put("resultList", returnList);
            return "basedata/teacherClassSwitch/listDetail.ftl";
        }
        List<String> adjustedIds = EntityUtils.getList(adjustedList, Adjusted::getId);
        List<AdjustedDetail> adjustedDetailList = adjustedDetailService.findListByAdjustedIds(adjustedIds.toArray(new String[0]));
        returnList = makeAdjustDtoList(adjustedList, adjustedDetailList,gradeId);
        map.put("resultList", returnList);
        return "basedata/teacherClassSwitch/listDetail.ftl";
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
                                        tipsayService.pushMessage(new String[]{clazz.getTeacherId()}, null, "您所属班级调课申请已撤销。", "所属班级调课申请变更");
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
                                        tipsayService.pushMessage(new String[]{clazz.getTeacherId()}, null, "您所属班级调课申请已撤销。", "所属班级调课申请变更");
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
            return error(e.getMessage() == null ? "撤销失败" : e.getMessage());
        }
        return success("提交成功");
    }

    @RequestMapping("/manage/table")
    public String switchManageDetail(String schoolId, String acadyear, String semester, String gradeId, String week, String isExport, ModelMap map) {
        List<Adjusted> adjustedList = adjustedService.findListBySchoolIdAndWeek(acadyear, semester, schoolId, week);
        if("1".equals(isExport)){
        	adjustedList = adjustedList.stream().filter(e->TipsayConstants.TIPSAY_STATE_1.equals(e.getState())).collect(Collectors.toList());
        	map.put("isExport", isExport);
        }
        map.put("schoolId", schoolId);
        if (CollectionUtils.isEmpty(adjustedList)) {
            return "basedata/teacherClassSwitch/manageDetail.ftl";
        }
        List<AdjustedDto> returnList;
        List<String> adjustedIds = EntityUtils.getList(adjustedList, Adjusted::getId);
        List<AdjustedDetail> adjustedDetailList = adjustedDetailService.findListByAdjustedIds(adjustedIds.toArray(new String[0]));
        returnList = makeAdjustDtoList(adjustedList, adjustedDetailList,gradeId);
        map.put("resultList", returnList);
        return "basedata/teacherClassSwitch/manageDetail.ftl";
    }

    @RequestMapping("/manage/agree")
    @ResponseBody
    public String switchManageAgree(String adjustedId, String state) {
        String msg;
        try {
            msg = adjustedService.updateStateById(adjustedId, state, getLoginInfo().getOwnerId());
        } catch (Exception e) {
            e.printStackTrace();
            return error("审核失败");
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
        return success(msg == null ? "审核完成" : msg);
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

    private List<AdjustedDto> makeAdjustDtoList(List<Adjusted> adjustedList, List<AdjustedDetail> adjustedDetailList, String gradeId) {
        List<AdjustedDto> returnList = new ArrayList<>();
        if(CollectionUtils.isEmpty(adjustedList)) {
            return returnList;
        }
        Set<String> courseIdSet = new HashSet<>();
        Set<String> classIdSet = new HashSet<>();
        Set<String> teacherIdSet = new HashSet<>();
        Map<String, AdjustedDetail> adjustedDetailMap = new HashMap<>();
        Map<String, AdjustedDetail> beenAdjustedDetailMap = new HashMap<>();
        for (Adjusted one : adjustedList) {
            teacherIdSet.add(one.getOperator());
        }
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
        List<Clazz> clazzList = classService.findListByIds(classIdSet.toArray(new String[0]));
        Map<String, String> clazzMap =
                EntityUtils.getMap(clazzList, Clazz::getId, Clazz::getClassNameDynamic);
        Map<String, String> clazzGradeIdMap =
        		EntityUtils.getMap(clazzList, Clazz::getId, Clazz::getGradeId);
        List<TeachClass> teachClazzList = teachClassService.findListByIds(classIdSet.toArray(new String[0]));
        Map<String, String> teachClazzMap =
                EntityUtils.getMap(teachClazzList, TeachClass::getId, TeachClass::getName);
        Map<String, String> teachGradeIdMap =
        		EntityUtils.getMap(teachClazzList, TeachClass::getId, TeachClass::getName);
        Map<String, String> teacherMap =
                teacherService.findPartByTeacher(teacherIdSet.toArray(new String[0]));
        String unitId = getLoginInfo().getUnitId();
        Semester semester = semesterService.findCurrentSemester(1, unitId);
        DateInfo today = dateInfoService.getDate(unitId, semester.getAcadyear(), semester.getSemester(), new Date());
        // 若为寒暑假，则为空，取上一个学期最后一天
        if (today == null) {
            List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(unitId, semester.getAcadyear(), semester.getSemester());
            today = dateInfoList.get(dateInfoList.size() - 1);
        }
        AdjustedDto tmp;
        AdjustedDetail adjustTmp;
        AdjustedDetail beenAdjustTmp;
        for (Adjusted one : adjustedList) {
            tmp = new AdjustedDto();
            adjustTmp = adjustedDetailMap.get(one.getId());
            beenAdjustTmp = beenAdjustedDetailMap.get(one.getId());
            tmp.setId(one.getId());
            tmp.setOperatorName(teacherMap.get(one.getOperator()));
            if (adjustTmp != null) {
                if (StringUtils.isNotBlank(gradeId)) {
                    String gId = clazzGradeIdMap.get(adjustTmp.getClassId());
                    if (gId == null)
                        gId = teachGradeIdMap.get(adjustTmp.getClassId());

                    if (!gradeId.equals(gId)) {
                        continue;
                    }
                }

                String className = clazzMap.get(adjustTmp.getClassId());
                if (className == null) {
                    className = teachClazzMap.get(adjustTmp.getClassId());
                }
                tmp.setClassName(className == null ? "" : className);
                tmp.setAdjustingId(adjustTmp.getId());
                if (StringUtils.isNotBlank(adjustTmp.getSubjectId())) {
                    tmp.setAdjustingName("第" + adjustTmp.getWeekOfWorktime() + "周" + BaseConstants.dayOfWeekMap2.get(adjustTmp.getDayOfWeek().toString()) + BaseConstants.PERIOD_INTERVAL_Map.get(adjustTmp.getPeriodInterval()) + "第" + adjustTmp.getPeriod() + "节 - " + courseMap.get(adjustTmp.getSubjectId()));
                } else {
                    tmp.setAdjustingName("第" + adjustTmp.getWeekOfWorktime() + "周" + BaseConstants.dayOfWeekMap2.get(adjustTmp.getDayOfWeek().toString()) + BaseConstants.PERIOD_INTERVAL_Map.get(adjustTmp.getPeriodInterval()) + "第" + adjustTmp.getPeriod() + "节");
                }
                if (StringUtils.isNotBlank(teacherMap.get(adjustTmp.getTeacherId()))) {
                    tmp.setAdjustingName(tmp.getAdjustingName() + "(" + teacherMap.get(adjustTmp.getTeacherId()) + ")");
                }

                if ("1".equals(one.getState())) {
                    if (adjustTmp.getWeekOfWorktime() > today.getWeek()) {
                        tmp.setCanDelete(true);
                    } else if (adjustTmp.getWeekOfWorktime() == today.getWeek()) {
                        if (adjustTmp.getDayOfWeek() + 1 >= today.getWeekday()) {
                            tmp.setCanDelete(true);
                        }
                    }
                    if (beenAdjustTmp != null) {
                        if (beenAdjustTmp.getWeekOfWorktime() > today.getWeek()) {
                            tmp.setCanDelete(true);
                        } else if (beenAdjustTmp.getWeekOfWorktime() == today.getWeek()) {
                            if (beenAdjustTmp.getDayOfWeek() + 1 >= today.getWeekday()) {
                                tmp.setCanDelete(true);
                            }
                        }
                    }
                } else {
                    tmp.setCanDelete(true);
                }
            }

            if (beenAdjustTmp != null) {
                tmp.setBeenAdjustedId(beenAdjustTmp.getId());
                if (StringUtils.isNotBlank(beenAdjustTmp.getSubjectId())) {
                    tmp.setBeenAdjustedName("第" + beenAdjustTmp.getWeekOfWorktime() + "周" + BaseConstants.dayOfWeekMap2.get(beenAdjustTmp.getDayOfWeek().toString()) + BaseConstants.PERIOD_INTERVAL_Map.get(beenAdjustTmp.getPeriodInterval()) + "第" + beenAdjustTmp.getPeriod() + "节 - " + courseMap.get(beenAdjustTmp.getSubjectId()));
                } else {
                    tmp.setBeenAdjustedName("第" + beenAdjustTmp.getWeekOfWorktime() + "周" + BaseConstants.dayOfWeekMap2.get(beenAdjustTmp.getDayOfWeek().toString()) + BaseConstants.PERIOD_INTERVAL_Map.get(beenAdjustTmp.getPeriodInterval()) + "第" + beenAdjustTmp.getPeriod() + "节");
                }
                if (StringUtils.isNotBlank(teacherMap.get(beenAdjustTmp.getTeacherId()))) {
                    tmp.setBeenAdjustedName(tmp.getBeenAdjustedName() + "(" + teacherMap.get(beenAdjustTmp.getTeacherId()) + ")");
                }
            }
            tmp.setRemark(one.getRemark());
            tmp.setState(one.getState());
            returnList.add(tmp);
        }
        return returnList;
    }

    /**
     * 判断是否有调课管理权限
     */
    private boolean isAdmin(String unitId,String userId) {
        return customRoleRemoteService.checkUserRole(unitId, TipsayConstants.SUBSYSTEM_86, TipsayConstants.EDUCATION_CODE, userId);
    }

    /**
     * 检测需调与被调课程是否存在冲突
     * @param adjustIdArr
     * @param beenAdjustIdArr
     * @param courseScheduleMap
     */
    private String checkConflict(String[] adjustIdArr, String[] beenAdjustIdArr, Map<String, CourseSchedule> courseScheduleMap) {
        Set<String> teacherIdsSet = new HashSet<>();
        for (CourseSchedule one : courseScheduleMap.values()) {
            teacherIdsSet.add(one.getTeacherId());
        }
        String unitId = getLoginInfo().getUnitId();
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
}
