package net.zdsoft.eclasscard.data.action.show;

import static net.zdsoft.eclasscard.data.constant.EccConstants.PERIOD_AM;
import static net.zdsoft.eclasscard.data.constant.EccConstants.PERIOD_MORN;
import static net.zdsoft.eclasscard.data.constant.EccConstants.PERIOD_NIGHT;
import static net.zdsoft.eclasscard.data.constant.EccConstants.PERIOD_PM;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.StuDormInfoDto;
import net.zdsoft.eclasscard.data.dto.StudentLeaveDto;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.service.EccDateInfoService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.officework.remote.service.OfficeAttanceRemoteService;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
@Controller
@RequestMapping("/eccShow/eclasscard")
public class EccStudentSpaceAction extends BaseAction {
	
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
    private UserRemoteService userRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private OfficeAttanceRemoteService officeAttanceRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private EccDateInfoService eccDateInfoService;
	
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
    
	@RequestMapping("/studentSpace/login")
	public String studentSpaceLogin(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		map.put("cardId", cardId);
		map.put("view", view);
		School school = SUtils.dc(schoolRemoteService.findOneById(eccInfo.getUnitId()), School.class);
		map.put("schoolName", school.getSchoolName());
		if (EccConstants.ECC_VIEW_1.equals(view)) {
			return "/eclasscard/show/studentspace/eccStudentSpaceLogin.ftl";
		} else {
			return "/eclasscard/verticalshow/studentspace/eccStudentSpaceLogin.ftl";
		}
	}
	
	@RequestMapping("/studentSpace/index")
	public String studentSpaceIndex(String cardId,String view,String userId,ModelMap map){
		map.put("cardId", cardId);
		if(StringUtils.isBlank(userId)){
			userId=getLoginInfo().getUserId();
		}
		User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
		if(user!=null){	
			String studentId=user.getOwnerId();
			Student student=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
			if(student!=null){
				map.put("userName", user.getUsername());
				map.put("studentName", student.getStudentName());
				map.put("studentCode", student.getStudentCode());
				Clazz clazz=SUtils.dc(classRemoteService.findOneById(student.getClassId()),Clazz.class);
				if(clazz!=null){
					map.put("className", clazz.getClassNameDynamic());
					Teacher teacher=SUtils.dc(teacherRemoteService.findOneById(clazz.getTeacherId()),Teacher.class);
					map.put("teacherName",teacher==null?"":teacher.getTeacherName());
				}
				map.put("showPicUrl", EccUtils.showPicUrl(student.getDirId(), student.getFilePath(), student.getSex()));
			}
		}
		Set<String> unitSet = officeAttanceRemoteService.getAttUnitIds();
		String unitId = getLoginInfo().getUnitId();
		boolean healthData = false;
		if (unitSet.contains(unitId)) {
			healthData = true;
		}
		map.put("userId", userId);
		map.put("view", view);
		map.put("healthData", healthData);
		if (EccConstants.ECC_VIEW_1.equals(view)) {
			return "/eclasscard/show/studentspace/eccStudentSpaceIndex.ftl";
		} else {
			return "/eclasscard/verticalshow/studentspace/eccStudentSpaceIndex.ftl";
		}
	}
	
	@RequestMapping("/homepage")
	@ControllerInfo("个人主页")
	public String studentSpaceIndex(String view,String userId,ModelMap map){
		User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
		if(user!=null){	
			String studentId=user.getOwnerId();
			Student student=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
			if(student!=null){
				map.put("studentName", student.getStudentName());
				map.put("studentCode", student.getStudentCode());
				map.put("studentSex", student.getSex());
				Clazz clazz=SUtils.dc(classRemoteService.findOneById(student.getClassId()),Clazz.class);
				if(clazz!=null){
					map.put("className", clazz.getClassNameDynamic());
					Teacher teacher=SUtils.dc(teacherRemoteService.findOneById(clazz.getTeacherId()),Teacher.class);
					map.put("teacherName",teacher==null?"":teacher.getTeacherName());
				}
			}
		}
		Set<String> unitSet = officeAttanceRemoteService.getAttUnitIds();
		String unitId = getLoginInfo().getUnitId();
		boolean healthData = false;
		if (unitSet.contains(unitId)) {
			healthData = true;
		}
		map.put("healthData", healthData);
		map.put("view", view);
		map.put("userId", userId);
		return "/eclasscard/verticalshow/studentspace/eccStudentHomePageIndex.ftl";
	}
	@ResponseBody
	@ControllerInfo("请假申请保存")
	@RequestMapping("/studentLeave/applyPage/save")
    public String showStuLeaveApplySave(String userId,StudentLeaveDto dto,ModelMap map){
		try {
			if(StringUtils.equals(dto.getLeaveType(), "4")) {
				if(StringUtils.isNotBlank(dto.getStartTime())) {
					dto.setStartTime(dto.getStartTime().substring(0, 10));
				}
				if(StringUtils.isNotBlank(dto.getEndTime())) {
					dto.setEndTime(dto.getEndTime().substring(0, 10));
				}
			}
			String jsonStr = JSONObject.toJSONString(dto);
			String black = getOpenApiOfficeService().submitStuLeave(jsonStr);
			if(StringUtils.equals(black, "1")) {
				return success("操作成功！");
			}else if(StringUtils.equals(black, "2")){
				return error("存在有交叉时间段的信息正在审核中或已经被审核通过");
			}else{
				return error("没有维护流程审核人员！");
			}
		} catch (Exception e) {
			return error("操作失败");
		}
	}
	
	@ControllerInfo("请假申请")
	@RequestMapping("/studentLeave/applyPage")
    public String showStuLeaveApply(String view,String userId,String leaveType,ModelMap map){
		if(StringUtils.isBlank(leaveType)) {
			leaveType = "1";
		}
		if(StringUtils.isBlank(userId)){
			userId=getLoginInfo().getUserId();
		}
		User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
		String studentId=user.getOwnerId();
		map.put("studentId", studentId);
		Student student=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
		map.put("studentName", student.getStudentName());
		String classId = student.getClassId();
		map.put("classId", classId);
		Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		map.put("className", cls.getClassNameDynamic());
		String jsonDormStr = stuworkRemoteService.findbuildRoomByStudentId(studentId, student.getSchoolId(), null, null);
		StuDormInfoDto dormInfoDto = SUtils.dc(jsonDormStr,StuDormInfoDto.class);
		if(dormInfoDto != null && StringUtils.isNotBlank(dormInfoDto.getBuildingName())&&StringUtils.isNotBlank(dormInfoDto.getRoonName())){
			map.put("room", dormInfoDto.getBuildingName()+"#"+dormInfoDto.getRoonName());
		}else{
			map.put("room", "");
		}
		Map<String,String> flowMap = new HashMap<>(); 
		String jsonStr = getOpenApiOfficeService().getHwStuLeaveFlows(cls.getSchoolId(), cls.getGradeId()	, leaveType);
		System.out.println("jsonStr="+jsonStr);
		JSONArray jsonArray = JSONArray.parseArray(jsonStr);
		if(jsonArray != null&&jsonArray.size()>0){
			for(int i = 0;i<jsonArray.size();i++){
				JSONObject jsonParam = jsonArray.getJSONObject(i);
				if(jsonParam.containsKey("flowId")&&
						jsonParam.containsKey("flowName")){
					flowMap.put(jsonParam.getString("flowId").toString(), jsonParam.getString("flowName").toString());
				}
			}
		}
//		flowMap.put("111", "111");
//		flowMap.put("222", "222");
//		flowMap.put("333", "333");
		map.put("flows", flowMap);
		map.put("userId", userId);
		map.put("leaveType", leaveType);
		return "/eclasscard/show/studentspace/eccStudentSpaceLeaveApply.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/studentLeave/countDays")
	public String countDays(ModelMap map,String classId,String date1,String date2){
		float days = 0f;
		
		try {
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			String unitId = cls.getSchoolId();
			String jsonStr = getOpenApiOfficeService().getOfficeStudentLeaveTimeByUnitId(unitId);
//			String jsonStr = "{\"result\":\"1\",\"beginTime\":\"1900-01-01 08:00\",\"endTime\":\"1900-01-01 16:00\"}";
			JSONObject jsonObject =JSONObject.parseObject(jsonStr);
			if(StringUtils.equals(jsonObject.getString("result"), "0")) {
				return error("请先联系老师设置每日上课时间段");
			}
			SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date startTime = sdff.parse(date1);
			Date endTime = sdff.parse(date2);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startTimeStr = sdf.format(startTime);
			String endTimeStr = sdf.format(endTime);
			Set<String> dateStrSet = new HashSet<String>();
			Map<String,DateInfo> infoMap = new HashMap<String,DateInfo>();
			List<DateInfo> infos = SUtils.dt(dateInfoRemoteService.findByDates(unitId, startTime, endTime), DateInfo.class);
			for (DateInfo info : infos) {
				if(StringUtils.equals(info.getIsFeast(), "N")) {
					dateStrSet.add(sdf.format(info.getInfoDate()));
					infoMap.put(sdf.format(info.getInfoDate()), info);
				}
			}
			List<Date> bkTimeList = eccDateInfoService.getInfoDateList(unitId, cls.getGradeId(), sdf.parse(startTimeStr), sdf.parse(endTimeStr));
			for(Date bk : bkTimeList) {
				System.out.print(sdf.format(bk)+",");
				dateStrSet.add(sdf.format(bk));
			}
			
			for (String dateStr : dateStrSet) {
				if(StringUtils.equals(startTimeStr, endTimeStr)) {
					Date beginDate = toDate(startTime,sdff.parse(jsonObject.getString("beginTime")));
					Date endDate = toDate(startTime,sdff.parse(jsonObject.getString("endTime")));
					long sumM = (endDate.getTime() - beginDate.getTime())/60000;
					Date a = startTime;
					Date b = endTime;
					if(DateUtils.compareIgnoreSecond(beginDate,startTime) >= 0 ) {
						a = beginDate;
					}
					if(DateUtils.compareIgnoreSecond(endTime, endDate) >= 0) {
						b = endDate;
					}
					long curM = (b.getTime() - a.getTime())/60000;
					if(curM<0f) {
						days = days+0f;
					}else {
						days = days + ((float)curM/(float)sumM);
					}
					break;
				}else {
					if(StringUtils.equals(dateStr, startTimeStr)) {
						Date beginDate = toDate(startTime,sdff.parse(jsonObject.getString("beginTime")));
						Date endDate = toDate(startTime,sdff.parse(jsonObject.getString("endTime")));
						if(DateUtils.compareIgnoreSecond(startTime, endDate) >= 0) {
							days = days + 0f;
						}else if(DateUtils.compareIgnoreSecond(beginDate, startTime) >= 0) {
							days = days + 1f;
						}else {
							long sumM = (endDate.getTime() - beginDate.getTime())/60000;
							long curM = (endDate.getTime() - startTime.getTime())/60000;
							days = days + ((float)curM/(float)sumM);
						}
					}else if(StringUtils.equals(dateStr, endTimeStr)) {
						Date beginDate = toDate(endTime,sdff.parse(jsonObject.getString("beginTime")));
						Date endDate = toDate(endTime,sdff.parse(jsonObject.getString("endTime")));
						if(DateUtils.compareIgnoreSecond(endTime, endDate) >= 0) {
							days = days + 1f;
						}else if(DateUtils.compareIgnoreSecond(beginDate, endTime) >= 0) {
							days = days + 0f;
						}else {
							long sumM = (endDate.getTime() - beginDate.getTime())/60000;
							long curM = (endTime.getTime() - beginDate.getTime())/60000;
							days = days + ((float)curM/(float)sumM);
						}
					}else {
						days = days + 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error("计算出错");
		}
		float b = (float)(Math.round(days*10))/10;
		return success(b+"");
	}
	
	//取date1的年月日，取date2的时分
	private Date toDate(Date date1,Date date2) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:00");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
		String date1Str = sdf1.format(date1);
		String date2Str = sdf2.format(date2);
		String date3Str = date1Str + " " + date2Str;
		System.out.println(date3Str);
		Date date3 = null;
		try {
			date3 = sdf3.parse(date3Str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date3;
	}
	
	@ControllerInfo("请假")
	@RequestMapping("/studentLeave/page")
    public String showStuLeave(String view,String userId,ModelMap map){
		if(StringUtils.isBlank(userId)){
			userId=getLoginInfo().getUserId();
		}
		User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
		List<StudentLeaveDto> dtolist = new ArrayList<StudentLeaveDto>();
		if(user!=null){	
			String studentId=user.getOwnerId();
			Student student=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
			map.put("studentName", student.getStudentName());
			String classId = student.getClassId();
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			map.put("className", cls.getClassNameDynamic());
			String jsonDormStr = stuworkRemoteService.findbuildRoomByStudentId(studentId, student.getSchoolId(), null, null);
			StuDormInfoDto dormInfoDto = SUtils.dc(jsonDormStr,StuDormInfoDto.class);
			if(dormInfoDto != null && StringUtils.isNotBlank(dormInfoDto.getBuildingName())&&StringUtils.isNotBlank(dormInfoDto.getRoonName())){
				map.put("room", dormInfoDto.getBuildingName()+"#"+dormInfoDto.getRoonName());
			}else{
				map.put("room", "");
			}
			String jsonStr = getOpenApiOfficeService().getStuLeaveInfo(user.getUnitId(), studentId);
//			String jsonStr ="[{\"studentId\":\"FF8080815C0D52DB015C15772D2E0015\",\"leaveType\":\"1\",\"applyTime\":\"2017-09-12\",\"state\":\"2\",\"task\":[],\"leaveTime\":\"2017-09-11 23:58:00至2017-09-12 23:58:00\",\"days\":1,\"remark\":\"锌咀嚼\"}]";
//			String jsonStr = "[{\"studentId\":\"FF8080815BF2A6EB015BF2C079A10048\",\"leaveType\":\"1\",\"applyTime\":\"2017-12-08\",\"state\":\"3\",\"task\":[\"测试(梅振衣)：[审核通过]1\"],\"leaveTime\":\"2017-12-08 00:00:00至2017-12-09 00:00:00\",\"days\":1,\"remark\":\"111\"},{\"studentId\":\"FF8080815BF2A6EB015BF2C079A10048\",\"leaveType\":\"2\",\"applyTime\":\"2017-12-08\",\"state\":\"2\",\"task\":[],\"leaveTime\":\"2017-12-06 00:00:00至2017-12-06 00:00:00\",\"remark\":\"555\",\"linkPhone\":\"555\"}]";
			JSONArray jsonArray = JSONArray.parseArray(jsonStr);
	    	if(jsonArray != null&&jsonArray.size()>0){
	    		StudentLeaveDto dto = null;
	    		for(int i = 0;i<jsonArray.size();i++){
	    			dto =new StudentLeaveDto();
	    			JSONObject jsonParam = jsonArray.getJSONObject(i);
	    			StudentLeaveDto.toDto(jsonParam,dto);
	    			dtolist.add(dto);
	    		}
	    	}
	    	map.put("leaveDtos", dtolist);
		}
		map.put("userId", userId);
		School school = SUtils.dc(schoolRemoteService.findOneById(user.getUnitId()), School.class);
		map.put("schoolName", school.getSchoolName());
		if (EccConstants.ECC_VIEW_1.equals(view)) {
			return "/eclasscard/show/studentspace/eccStudentSpaceLeave.ftl";
		} else {
			return "/eclasscard/verticalshow/studentspace/eccStudentSpaceLeave.ftl";
		}		
	}
	
	@ResponseBody
	@RequestMapping("/stuLoginUser/page")
	public String stuLoginUser(String cardId,String cardNumber, String password, HttpServletRequest request,
            HttpServletResponse response, HttpSession httpSession, ModelMap map){
			//根据卡号号查找
			EccInfo eccInfo = eccInfoService.findOne(cardId);
			Student student = SUtils.dt(studentRemoteService.findByCardNumber(eccInfo.getUnitId(),cardNumber),new TR<Student>() {});
			User user = null;
			if (student == null) {
				user = SUtils.dc(userRemoteService.findByUsername(cardNumber), User.class);
				if(user==null||!(eccInfo.getUnitId().equals(user.getUnitId()))){
					return error("找不到此学生！");
				}
			}else{
				user = SUtils.dc(userRemoteService.findByOwnerId(student.getId()), User.class);
			}
	        if (user == null) {
	        	return error("找不到此用户！");
	        }
	        if(User.OWNER_TYPE_STUDENT != user.getOwnerType()){
	        	return error("非学生账号！");
	        }
	        String pwd = user.getPassword();
	        if (StringUtils.length(pwd) == 64) {
	            pwd = PWD.decode(pwd);
	        }
	        if (StringUtils.equalsIgnoreCase(pwd, password)) {
	            // 开发模式不校验验证码
                initLoginInfo(httpSession, user);
	            return success("登录成功");
	        }
	        else {
	        	return error("密码错误！");
	        }
	}
	@ControllerInfo("课表")
	@RequestMapping("/stuSchedule/page")
	public String stuCourseSchedule(String view,String userId,ModelMap map){
            User user = User.dc(userRemoteService.findOneById(userId, true));
            int type = 1;
            if (getIntValue(user.getOwnerType()) == User.OWNER_TYPE_TEACHER) {
                type = 1;
            }
            if (getIntValue(user.getOwnerType()) == User.OWNER_TYPE_STUDENT) {
                type = 2;
            }
            if (getIntValue(user.getOwnerType()) == User.OWNER_TYPE_FAMILY) {
                type = 2;
            }
            Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
            if(semester == null){
            	return "";
            }
            Calendar calendar = Calendar.getInstance();
            DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
            if(dateInfo == null){
            	return "";
            }
            //已经填充 班级名称
            List<CourseSchedule> tcs = SUtils.dt(courseScheduleRemoteService.findCourseScheduleListByPerId
            		(semester.getAcadyear(), semester.getSemester(), dateInfo.getWeek(), user.getOwnerId(), type + ""), CourseSchedule.class);
            CourseSchedule dule=new CourseSchedule();

            //获取 早中晚 上课节数
            Map<String, Integer> intervalMap = new LinkedHashMap<String, Integer>();
            int mornPeriods=semester.getMornPeriods() == null ? 0 : semester.getMornPeriods();
            int amPeriods=semester.getMornPeriods() == null ? 0 : semester.getAmPeriods();
            int pmPeriods=semester.getMornPeriods() == null ? 0 : semester.getPmPeriods();
            int nightPeriods=semester.getMornPeriods() == null ? 0 : semester.getNightPeriods();
            int allCourseNum=mornPeriods+amPeriods+pmPeriods+nightPeriods;
            intervalMap.put(PERIOD_MORN,mornPeriods);
            intervalMap.put(PERIOD_AM,amPeriods);
            intervalMap.put(PERIOD_PM,pmPeriods);
            intervalMap.put(PERIOD_NIGHT,nightPeriods);

            Map<String, List<CourseSchedule>> csMap = new HashMap<String, List<CourseSchedule>>();
            Set<String> subjectIds = EntityUtils.getSet(tcs, cs -> cs.getSubjectId());
            List<Course> coureList=SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>(){});
            Map<String,String> courseNameMap=new HashMap<String, String>();
            if(CollectionUtils.isNotEmpty(coureList)){
            	for(Course course:coureList){
            		courseNameMap.put(course.getId(), course.getShortName());
            	}
            }
            
            for (CourseSchedule tc : tcs) {
                int period = tc.getPeriod();
                String periodInverval = tc.getPeriodInterval();
                tc.setSubjectName(courseNameMap.get(tc.getSubjectId()));
                if ( periodInverval.equals(PERIOD_MORN) ) {
                    if ( intervalMap.get(PERIOD_MORN) == 0 ) {
                        continue;
                    }
                }
                if ( periodInverval.equals(PERIOD_AM) ) {
                    if ( intervalMap.get(PERIOD_AM) == 0 ) {
                        continue;
                    } else {
                        period = period + intervalMap.get(PERIOD_MORN);
                    }
                }
                if ( periodInverval.equals(PERIOD_PM) ) {
                    if ( intervalMap.get(PERIOD_PM) == 0 ) {
                        continue;
                    } else {
                        period = period + intervalMap.get(PERIOD_MORN) + intervalMap.get(PERIOD_AM);
                    }
                }
                if ( periodInverval.equals(PERIOD_NIGHT) ) {
                    if ( intervalMap.get(PERIOD_NIGHT) == 0 ) {
                        continue;
                    } else {
                        period = period + intervalMap.get(PERIOD_MORN) + intervalMap.get(PERIOD_AM) + intervalMap.get(PERIOD_PM);
                    }
                }
                List<CourseSchedule> ts = csMap.get(period+"");
                if (ts == null) {
                    ts = new ArrayList<CourseSchedule>();
                }
                ts.add(tc);
                csMap.put(period+"", ts);
            }
            JSONObject values = new JSONObject();
            Set<Integer> dayOfWeekSets = Sets.newHashSet();
            for (int i = 1; i < allCourseNum+1; i++) {
            	JSONObject course = new JSONObject();
            	List<CourseSchedule> ts = csMap.get(i + "");
            	if (ts == null)
                    continue;
            	for (CourseSchedule t : ts) {
                    String weekday = String.valueOf(t.getDayOfWeek() + 1);
                    dayOfWeekSets.add(t.getDayOfWeek());
                    course.put("courseName" + weekday, t.getSubjectName());
                    course.put("className" + weekday, t.getClassName());
                    String clazz=t.getClassName();
                    course.put("className" + weekday, clazz == null ? "" : clazz);
                    course.put("placeName" + weekday, t.getPlaceName());
                }
                values.put("p" + i, course);
            }
            map.put("weekEnd",dayOfWeekSets.containsAll(new HashSet<Integer>(){{add(new Integer(5));add(new Integer(6));}}));
            map.put("values", values);
            map.put("intervalMap",intervalMap);
            map.put("allCourseNum", allCourseNum);
        if (EccConstants.ECC_VIEW_1.equals(view)) {
        	return "/eclasscard/show/studentspace/eccStudentSpaceSchedule.ftl";
        } else {
        	return "/eclasscard/verticalshow/studentspace/eccStudentSpaceSchedule.ftl";
        }
	}
	private void initLoginInfo(HttpSession httpSession, User user) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setOwnerType(user.getOwnerType());
        loginInfo.setUnitId(user.getUnitId());
        loginInfo.setUserId(user.getId());
        loginInfo.setOwnerId(user.getOwnerId());
        loginInfo.setUserName(user.getUsername());
        loginInfo.setRealName(user.getRealName());
        loginInfo.setUnitClass(2);
        ServletUtils.setLoginInfo(httpSession, loginInfo);
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
