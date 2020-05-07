package net.zdsoft.basedata.action;


import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.WeiKeyUtils;
import net.zdsoft.system.entity.config.SystemIni;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Controller
@RequestMapping("/basedata")
public class Timetableh5Action {

    public static final String ODD_WEEK = "（单）";
    public static final String EVEN_WEEK = "（双）";

	@Autowired
    UserService userService;
	@Autowired
    SemesterService semesterService;
	@Autowired
    DateInfoService dateInfoService;
	@Autowired
    CourseScheduleService courseScheduleService;
	@Autowired
    CourseService courseService;
	@Autowired
	SystemIniRemoteService systemIniRemoteService;
	@Autowired
	private ClassService classService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	TeachClassService teachClassService;
    @Autowired
    private TeacherService teacherService;

    @RequestMapping("/showTimetable/index/page")
    public String showTimetable(String token,String uname,ModelMap map) {
    	User user=null;
    	String ownerId=null;
		try {
			if(StringUtils.isNotBlank(token)){
				ownerId = WeiKeyUtils.decodeByDes(token);
            }
		}catch (Exception e) {
			e.printStackTrace();
			map.put("nonemess", "token解析出错了，请确认参数是否正确。");
			return "/basedata/timetableh5/none.ftl";
		}
		
    	if(StringUtils.isNotBlank(ownerId)) {
            user = userService.findByOwnerId(ownerId);
        }
        if(user == null){
        	if(StringUtils.isNotBlank(uname)){
        		user = userService.findByUsername(uname);
        		if(user == null){
    	        	map.put("nonemess", "user对象不存在，请确认参数是否正确("+uname+")。");
    	        	return "/basedata/timetableh5/none.ftl";
            	}
        	}else{
        		map.put("nonemess", "user对象不存在，请确认参数是否正确("+ownerId+")。");
	        	return "/basedata/timetableh5/none.ftl";
        	}
        }
        int type = 1;
        if (getIntValue(user.getOwnerType()) == User.OWNER_TYPE_TEACHER) {
            List<Clazz> clazzList = classService.findByTeacherId(user.getOwnerId());
            if(CollectionUtils.isNotEmpty(clazzList)){
                map.put("clazzList", clazzList);
            }
        	map.put("title", "我的课程表");
            type = 1;
        }
        if (getIntValue(user.getOwnerType()) == User.OWNER_TYPE_STUDENT) {
        	map.put("title", "我的课程表");
            type = 2;
        }
        if (getIntValue(user.getOwnerType()) == User.OWNER_TYPE_FAMILY) {
        	map.put("title", "孩子课程表");
            type = 2;
        }
        map.put("ownerId", user.getOwnerId());
        map.put("type", type);
        return "/basedata/timetableh5/index.ftl";
    }

    @RequestMapping("/showTimetable/detail/page")
    public String showTimetableDetail(String ownerId,String type,String tabType, String classId, ModelMap map) {
        User user = userService.findByOwnerId(ownerId);
        Semester semester = semesterService.findCurrentSemester(0, user.getUnitId());
        if(semester == null){
            map.put("nonemess", "semester对象不存在，请联系管理员维护学年学期！");
            return "/basedata/timetableh5/detail.ftl";
        }

        Calendar calendar = Calendar.getInstance();
        DateInfo dateInfo = dateInfoService.getDate(user.getUnitId(), semester.getAcadyear(), semester.getSemester(), calendar.getTime());
        Integer week=2;
        if(dateInfo == null){
            String val =systemIniRemoteService.findValue(SystemIni.SYSTEM_SHOW_SCHEDULE);
            if(!"Y".equals(val)){
                map.put("nonemess", "DateInfo对象不存在，请联系管理员维护节假日信息！");
                return "/basedata/timetableh5/detail.ftl";
            }
        }else{
            week = dateInfo.getWeek();
        }

        String perId = ownerId;
        List<CourseSchedule> tcs = new ArrayList<>();
        if(!"1".equals(tabType)) {//班级
            type="3";
            perId=classId;
        }
        //已经填充 班级名称
        tcs = courseScheduleService.findCourseScheduleListByPerId(semester.getAcadyear(), semester.getSemester(), week, perId, type + "");
        // 是否存在单双周问题
        List<CourseSchedule> timetableCourseScheduleTmp = courseScheduleService.findCourseScheduleListByPerId(semester.getAcadyear(), semester.getSemester(), week + 1, perId, type + "");
        if (CollectionUtils.isEmpty(timetableCourseScheduleTmp)) {
            timetableCourseScheduleTmp = courseScheduleService.findCourseScheduleListByPerId(semester.getAcadyear(), semester.getSemester(), week - 1, perId, type + "");
        }
        if (CollectionUtils.isNotEmpty(timetableCourseScheduleTmp)) {
            for (CourseSchedule one : timetableCourseScheduleTmp) {
                if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(one.getWeekType()) || Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(one.getWeekType())) {
                    tcs.add(one);
                }
            }
        }

        Set<String> classIds = EntityUtils.getSet(tcs, CourseSchedule::getClassId);
        List<Clazz> classes = classService.findListByIds(classIds.toArray(new String[0]));
        List<TeachClass> teachClasses = teachClassService.findListByIds(classIds.toArray(new String[0]));
        Set<String> gradeIds=new HashSet<>();
        if(CollectionUtils.isNotEmpty(classes)) {
            gradeIds.addAll(EntityUtils.getSet(classes, Clazz::getGradeId));
        }
        if(CollectionUtils.isNotEmpty(teachClasses)) {
            for(TeachClass tt:teachClasses) {
                if(StringUtils.isNotBlank(tt.getGradeId())) {
                    gradeIds.addAll(Arrays.asList(tt.getGradeId().split(",")));
                }
            }
        }

        Grade grade = gradeService.findTimetableMaxRangeBySchoolId(user.getUnitId(),gradeIds.toArray(new String[0]));
        Integer ms = grade.getMornPeriods();
        Integer am = grade.getAmLessonCount();
        Integer pm = grade.getPmLessonCount();
        Integer night = grade.getNightLessonCount();
        //获取 早中晚 上课节数
        Map<String, Integer> timeIntervalMap = new LinkedHashMap<String, Integer>();
        timeIntervalMap.put(CourseSchedule.PERIOD_INTERVAL_1, ms);
        timeIntervalMap.put(CourseSchedule.PERIOD_INTERVAL_2, am);
        timeIntervalMap.put(CourseSchedule.PERIOD_INTERVAL_3, pm);
        timeIntervalMap.put(CourseSchedule.PERIOD_INTERVAL_4, night);

        int allCourseNum = 0;
        for (Map.Entry<String, Integer> entry : timeIntervalMap.entrySet()) {
            allCourseNum += entry.getValue();
        }

        if(CollectionUtils.isNotEmpty(tcs)){
            Map<String, List<CourseSchedule>> csmap = new HashMap<String, List<CourseSchedule>>();
            Set<String> subjectIds = EntityUtils.getSet(tcs, CourseSchedule::getSubjectId);
            Set<String> teacherIds = EntityUtils.getSet(tcs, CourseSchedule::getTeacherId);
            for (CourseSchedule tc : tcs) {
                //教师课表过滤掉虚拟课程
                if("1".equals(tabType) && tc.getClassType()==1 && BaseConstants.SUBJECT_TYPE_VIRTUAL.equals(tc.getSubjectType())) {
                    continue;
                }

                int period = tc.getPeriod();
                String periodInverval = tc.getPeriodInterval();
                if ( CourseSchedule.PERIOD_INTERVAL_1.equals(periodInverval) ) {
                    if ( timeIntervalMap.get(CourseSchedule.PERIOD_INTERVAL_1) == 0 ) {
                        continue;
                    }
                }
                if ( CourseSchedule.PERIOD_INTERVAL_2.equals(periodInverval) ) {
                    if ( timeIntervalMap.get(CourseSchedule.PERIOD_INTERVAL_2) == 0 ) {
                        continue;
                    } else {
                        period = period + timeIntervalMap.get(CourseSchedule.PERIOD_INTERVAL_1);
                    }
                }
                if ( CourseSchedule.PERIOD_INTERVAL_3.equals(periodInverval) ) {
                    if ( timeIntervalMap.get(CourseSchedule.PERIOD_INTERVAL_3) == 0 ) {
                        continue;
                    } else {
                        period = period + timeIntervalMap.get(CourseSchedule.PERIOD_INTERVAL_1) + timeIntervalMap.get(CourseSchedule.PERIOD_INTERVAL_2);
                    }
                }
                if ( CourseSchedule.PERIOD_INTERVAL_4.equals(periodInverval) ) {
                    if ( timeIntervalMap.get(CourseSchedule.PERIOD_INTERVAL_4) == 0 ) {
                        continue;
                    } else {
                        period = period + timeIntervalMap.get(CourseSchedule.PERIOD_INTERVAL_1) + timeIntervalMap.get(CourseSchedule.PERIOD_INTERVAL_2) + timeIntervalMap.get(CourseSchedule.PERIOD_INTERVAL_3);
                    }
                }
                List<CourseSchedule> ts = csmap.get(period+"");
                if (ts == null) {
                    ts = new ArrayList<CourseSchedule>();
                    csmap.put(period+"", ts);
                }
                ts.add(tc);
            }

            Map<String, String> subjectMap =  courseService.findPartCouByIds(subjectIds.toArray(new String[0]));
            Map<String, String> teacherMap = teacherService.findPartByTeacher(teacherIds.toArray(new String[0]));

            //Set<Integer> dayOfWeekSets = Sets.newHashSet();
            Map<String, Map<String, String>> values = new HashMap<String,  Map<String, String>>();
            for (int i = 1; i < allCourseNum+1; i++) {
                List<CourseSchedule> ts = csmap.get(i + "");
                Map<String, String> course=new HashMap<String, String>();
                if (ts == null){
                    continue;
                }
                for (CourseSchedule t : ts) {
                    String weekday = String.valueOf(t.getDayOfWeek() + 1);
                    //dayOfWeekSets.add(t.getDayOfWeek());
                    if ( subjectMap.get(t.getSubjectId()) == null ) {
                        continue;
                    }
                    String c = subjectMap.get(t.getSubjectId());
                    if (c != null) {
                        if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(t.getWeekType())) {
                            c = c + ODD_WEEK;
                        }
                        if (Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(t.getWeekType())) {
                            c = c + EVEN_WEEK;
                        }
                    }
                    if (course.get("courseName" + weekday) == null) {
                        course.put("courseName" + weekday, c == null ? "" : c);
                        String clazz=t.getClassName();
                        if("1".equals(tabType)){
                            course.put("className" + weekday, clazz == null ? "" : clazz);
                        }else{
                            course.put("className" + weekday, teacherMap.get(t.getTeacherId()));
                        }
                        course.put("placeName" + weekday, t.getPlaceName());
                    } else {
                        course.put("courseNameRe" + weekday, c == null ? "" : c);
                        String clazz=t.getClassName();
                        if("1".equals(tabType)){
                            course.put("classNameRe" + weekday, clazz == null ? "" : clazz);
                        }else{
                            course.put("classNameRe" + weekday, teacherMap.get(t.getTeacherId()));
                        }
                        course.put("placeNameRe" + weekday, t.getPlaceName());
                    }
                }
                values.put("p" + i, course);
            }
            map.put("values", values);
        }
        boolean isweekEnd=false;
        if(Integer.valueOf(5).compareTo(grade.getWeekDays())<0){
            isweekEnd=true;
        }

        map.put("weekEnd",isweekEnd);
        map.put("allCourseNum",allCourseNum);
        map.put("timeIntervalMap",timeIntervalMap);
        map.put("tabType", tabType);
        Date nowDay = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDay);
        int w = cal.get(Calendar.DAY_OF_WEEK);
        if(w==1){
            w=6;//星期天
        }else{
            w=w-2;
        }

        map.put("dayOfWeek", w);
        return "/basedata/timetableh5/detail.ftl";
    }

    protected int getIntValue(Integer integer){

        return getIntValue(integer, 0);
    }
    
    protected int getIntValue(Integer integer, int defaultValue) {
        if ( integer != null ) {
            return integer.intValue();
        }
        return defaultValue;
    }


}
