package net.zdsoft.remote.openapi.action.business;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.constant.custom.YingShuoConstant;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.StusysSectionTimeSet;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolSemesterRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StusysSectionTimeSetRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.TipsayExRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.exception.BusinessErrorException;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.pushjob.constant.BaseOpenapiConstant;
import net.zdsoft.remote.openapi.action.OpenApiBaseAction;
import net.zdsoft.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.remote.openapi.service.DeveloperPowerService;
import net.zdsoft.remote.openapi.utils.IdChangeUtils;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(value = { "/remote/openapi", "/openapi" })
public class RemoteTimetableAction extends OpenApiBaseAction{
	private final String interfaceType = "timetable";
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteServicer;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private StusysSectionTimeSetRemoteService stusysSectionTimeSetRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private DeveloperPowerService developerPowerService;
	@Autowired
	private TipsayExRemoteService tipsayExRemoteService;
	@Autowired
	private SchoolSemesterRemoteService schoolSemesterRemoteService;
	@Autowired
	protected SystemIniRemoteService systemIniRemoteService;
	
	
	/**
	 * 获取教师调代课情况
	 * @param  acadyear
	 * @param  semester
	 * @param  teacherId
	 */
	@RequestMapping("/ngke/timeClassInfo")
	@ResponseBody
	public String getTeacherClassInfo(String acadyear,Integer  semester, String teacherId, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		if(StringUtils.isBlank(acadyear) || semester == null){
			Semester semester1 = getCurrentSemester();
			if (semester1 == null) {
				json.put("resultMsg", "获取不到学年学期数据！");
				return json.toJSONString();
			}
			acadyear = semester1.getAcadyear();
			semester = semester1.getSemester();
		}
		Set<String> unitSet = getPowerUnitSet(request);
		if(CollectionUtils.isEmpty(unitSet)){
			json.put("resultMsg", "没有授权单位！");
			return json.toJSONString();
		}
		//判断该教师是否在授权范围内
		boolean isTrue = Boolean.FALSE;
		Teacher teacher = SUtils.dc(teacherRemoteService.findOneById(teacherId), Teacher.class);
		for (String c : unitSet) {
			if(c.equals(teacher.getUnitId()))
				isTrue = Boolean.TRUE;
		}
		String data = null;
		if(isTrue){
			try{
				data = tipsayExRemoteService.findByAcadyearAndSemesterAndTeacherId(acadyear, semester, teacherId);
			}
			catch(Exception e) {
				json.put("resultMsg" , e.getMessage());
				return json.toJSONString();
			}
		}else{
			json.put("resultMsg", "該教師不在授权单位內！");
			return json.toJSONString();
		}
		json.put("data", data);
		json.put("resultMsg", "调用成功！");
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
		return json.toJSONString();
	}
	
	/**
	 * 获取课表json数据, 根据时间戳和分页
	 * 
	 * @param username
	 * @return 课表数据的json格式
	 */
	@RequestMapping("/ngke/timetableByUpdateTime")
	@ResponseBody
	public String getTimetableByUpdateTime(String dataModifyTime,Integer page, Integer limit, HttpServletRequest request) {
		Pagination pagination = new Pagination(page, limit, false);
		JSONObject json = new JSONObject();
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		String	deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		Semester semester = null;
		if(!YingShuoConstant.YS_AREA_VALUE.equalsIgnoreCase(deployRegion)){
			semester = getCurrentSemester();
			if (semester == null) {
				json.put("resultMsg", "获取不到学年学期数据！");
				return json.toJSONString();
			}
		}
		Set<String> unitSet = getPowerUnitSet(request);
		if(CollectionUtils.isEmpty(unitSet)){
			json.put("resultMsg", "没有授权单位！");
			return json.toJSONString();
		}
		List<CourseSchedule> courseSchedules = new ArrayList<CourseSchedule>();
		try{
			Date modifyDate = null;
			if(StringUtils.isNotBlank(dataModifyTime)){
				SimpleDateFormat format = new SimpleDateFormat(BaseOpenapiConstant.BASE_UPDATE_SIMPLE_DATE_FORMAT);
				modifyDate = format.parse(dataModifyTime);
			}
			courseSchedules = SUtils.dt(courseScheduleRemoteService.findByModifyTime(unitSet.toArray(new String[unitSet.size()]),
					(semester == null ? null : semester.getAcadyear()),(semester == null ? null : semester.getSemester()), modifyDate, pagination),
					CourseSchedule.class);
		}
		catch(Exception e) {
			json.put("resultMsg" , e.getMessage());
			return json.toJSONString();
		}
		JSONArray data = new JSONArray();
		if(CollectionUtils.isNotEmpty(courseSchedules)){
			Set<String> schoolSet = EntityUtils.getSet(courseSchedules, CourseSchedule::getSchoolId);
			for (String c : schoolSet) {
				Semester semester1 = SUtils.dc(semesterRemoteServicer.getCurrentSemester(SemesterRemoteService.RET_NULL,c), Semester.class);
				if (semester1 == null) {
					json.put("resultMsg", "获取不到学年学期数据！");
					return json.toJSONString();
				}
				DateInfo dateInfo = getCurrentDateInfo(c, semester1);
				JSONArray d1 = fillwithNames(semester1.getAcadyear(), semester1.getSemester(), c,
						courseSchedules, dateInfo);
				data.addAll(d1);
			}
		}
		json.put("data", data);
		json.put("resultMsg", "调用成功！");
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
		json.put("dataCount", data.size());
		return json.toJSONString();
	}
	
	/**
	 * 获取课表json数据
	 * 
	 * @param username
	 * @return 课表数据的json格式
	 */
	@RequestMapping("/ngke/timetableByUsername/{username}")
	@ResponseBody
	public String getTimetableByUsername(@PathVariable String username, String startDate, String endDate, HttpServletRequest request) {
		if (isOverMaxNumDay(interfaceType, request, "/openapi/ngke/timetableByUsername/{username}")){
        	return returnOpenJsonError(OpenApiBaseAction.MAX_NUM_DAY_ERROR_MSG);
        } 
		doSaveInterfaceCount("/openapi/ngke/timetableByUsername/{username}",request);
		JSONObject json = new JSONObject();
		json.put("resultCode", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		User user = User.dc(userRemoteService.findByUsername(username));
		if (user == null) {
			json.put("resultMsg", "找不到此用户！");
			return json.toJSONString();
		} else {
			if(!doPowerProving(request,user.getUnitId())){
				json.put("resultMsg", "此用户所在的单位没有授权！");
				return json.toJSONString();
			}
		}
		
		//先根据学校来查找 ，不存在后取base_semester
		Semester semester = SUtils.dc(semesterRemoteServicer.getCurrentSemester(SemesterRemoteService.RET_NULL,user.getUnitId()), Semester.class);
		if (semester == null) {
			json.put("resultMsg", "获取不到学年学期数据！");
			return json.toJSONString();
		}
		
		String userType = setUserType(user);
		
		DateInfo dateInfo = getCurrentDateInfo(user.getUnitId(), semester);
		List<CourseSchedule> courseSchedules = new ArrayList<CourseSchedule>();
		try{
			courseSchedules = getCourseSchedules(courseSchedules,startDate, endDate, user.getOwnerId(), semester, dateInfo,
					userType,user.getUnitId());
		}
		catch(BusinessErrorException e) {
			json.put("resultMsg" , e.getMessage());
			return json.toJSONString();
		}
		JSONArray data = fillwithNames(semester.getAcadyear(), semester.getSemester(), user.getUnitId(),
				courseSchedules, dateInfo);
		
		json.put("data", data);
		json.put("resultMsg", "调用成功！");
		json.put("resultCode", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
		return json.toJSONString();
	}

	@RequestMapping("/ngke/timetableByClassId/{classId}")
	@ResponseBody
	public String getTimetableByClassId(@PathVariable String classId, String startDate, String endDate, HttpServletRequest request) {
		if (isOverMaxNumDay(interfaceType, request, "/openapi/ngke/timetableByClassId/{classId}")){
        	return returnOpenJsonError(OpenApiBaseAction.MAX_NUM_DAY_ERROR_MSG);
        } 
		doSaveInterfaceCount("/openapi/ngke/timetableByClassId/{classId}",request);
		JSONObject json = new JSONObject();
		json.put("resultCode", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		classId = IdChangeUtils.get32Id(classId);
		Clazz clazz = classRemoteService.findOneObjectById(classId);
		if (clazz == null) {
			json.put("resultMsg", "找不到此班级！");
			return json.toJSONString();
		} else {
			if(!doPowerProving(request,clazz.getSchoolId())){
				json.put("resultMsg", "此班级所在的单位没有授权！");
				return json.toJSONString();
			}
		}
		
		//先根据学校来查找 ，不存在后取base_semester
		Semester semester = SUtils.dc(semesterRemoteServicer.getCurrentSemester(SemesterRemoteService.RET_NULL,clazz.getSchoolId()), Semester.class);
		if (semester == null) {
			json.put("resultMsg", "获取不到学年学期数据！");
			return json.toJSONString();
		}
		
		DateInfo dateInfo = getCurrentDateInfo(clazz.getSchoolId(), semester);
		List<CourseSchedule> courseSchedules = new ArrayList<>();
		try {
			courseSchedules = getCourseSchedules(courseSchedules,startDate, endDate, classId, semester, dateInfo,"3",clazz.getSchoolId());
		}
		catch(BusinessErrorException e) {
			json.put("resultMsg" , e.getMessage());
			return json.toJSONString();
		}
		JSONArray data = fillwithNames(semester.getAcadyear(), semester.getSemester(), clazz.getSchoolId(),
				courseSchedules, dateInfo);
		
		json.put("data", data);
		json.put("resultMsg", "调用成功！");
		json.put("resultCode", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
		return json.toJSONString();
	
	}
	
	@RequestMapping("/ngke/timetableBySchoolId/{schoolId}")
	@ResponseBody
	public String getTimetableBySchoolId(@PathVariable String schoolId, String startDate, String endDate, HttpServletRequest request) {
		if (isOverMaxNumDay(interfaceType, request, "/openapi/ngke/timetableBySchoolId/{schoolId}")){
        	return returnOpenJsonError(OpenApiBaseAction.MAX_NUM_DAY_ERROR_MSG);
        } 
		doSaveInterfaceCount("/openapi/ngke/timetableBySchoolId/{schoolId}",request);
		JSONObject json = new JSONObject();
		json.put("resultCode", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		schoolId = IdChangeUtils.get32Id(schoolId);
		List<Clazz> clazzs = Clazz.dt(classRemoteService.findBySchoolId(schoolId));
		if(CollectionUtils.isEmpty(clazzs)){
			json.put("resultMsg", "找不到班级数据！");
			return json.toJSONString();
		} else {
			if(!doPowerProving(request,schoolId)){
				json.put("resultMsg", "此单位没有授权！");
				return json.toJSONString();
			}
		}
		//先根据学校来查找 ，不存在后取base_semester
		Semester semester = SUtils.dc(semesterRemoteServicer.getCurrentSemester(SemesterRemoteService.RET_NULL,schoolId), Semester.class);
		if (semester == null) {
			json.put("resultMsg", "获取不到学年学期数据！");
			return json.toJSONString();
		}
		
		DateInfo dateInfo = getCurrentDateInfo(schoolId, semester);
		List<CourseSchedule> courseSchedules = new ArrayList<>();
		try {
			courseSchedules = getCourseSchedules(courseSchedules,startDate, endDate, schoolId, semester, dateInfo,"4",schoolId);
		}
		catch(BusinessErrorException e) {
			json.put("resultMsg" , e.getMessage());
			return json.toJSONString();
		}
		JSONArray data = fillwithNames(semester.getAcadyear(), semester.getSemester(), schoolId,
				courseSchedules, dateInfo);
		
		json.put("data", data);
		json.put("resultMsg", "调用成功！");
		json.put("resultCode", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
		return json.toJSONString();
	}
	
	
	
	/**
	 * 获取课表json数据
	 * 
	 * @param ownerId
	 * @return 课表数据的json格式
	 */
	@RequestMapping("/ngke/timetableByOwnerId/{ownerId}")
	@ResponseBody
	public String getTimetableByOwnerId(@PathVariable String ownerId, String startDate, String endDate ,HttpServletRequest request) {
		if (isOverMaxNumDay(interfaceType, request, "/openapi/ngke/timetableByOwnerId/{ownerId}")){
        	return returnOpenJsonError(OpenApiBaseAction.MAX_NUM_DAY_ERROR_MSG);
        } 
		doSaveInterfaceCount("/openapi/ngke/timetableByOwnerId/{ownerId}",request);
		return getTimeTable(ownerId,Boolean.FALSE, startDate, endDate, request);
	}
	
	/**
	 * 获取课表json数据
	 * 
	 * @param userId
	 * @return 课表数据的json格式
	 */
	@RequestMapping("/ngke/timetableByUserId/{userId}")
	@ResponseBody
	public String getTimetableByUserId(@PathVariable String userId, String startDate, String endDate ,HttpServletRequest request) {
		if (isOverMaxNumDay(interfaceType, request, "/openapi/ngke/timetableByUserId/{userId}")){
        	return returnOpenJsonError(OpenApiBaseAction.MAX_NUM_DAY_ERROR_MSG);
        } 
		doSaveInterfaceCount("/openapi/ngke/timetableByUserId/{userId}",request);
		return getTimeTable(userId, Boolean.TRUE,startDate, endDate, request);
	}

	private String getTimeTable(String id, boolean isUserId, String startDate,
			String endDate, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("resultCode", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		id = IdChangeUtils.get32Id(id);
		User user;
		if(isUserId){
			user = User.dc(userRemoteService.findOneById(id));
		}else{
			user = User.dc(userRemoteService.findByOwnerId(id));
		}
		if (user == null) {
			json.put("resultMsg", "找不到此用户！");
			return json.toJSONString();
		} else {
			if(!doPowerProving(request,user.getUnitId())){
				json.put("resultMsg", "此用户所在的单位没有授权！");
				return json.toJSONString();
			}
		}
		//先根据学校来查找 ，不存在后取base_semester
		Semester semester = SUtils.dc(semesterRemoteServicer.getCurrentSemester(SemesterRemoteService.RET_NULL,user.getUnitId()), Semester.class);
		if (semester == null) {
			json.put("resultMsg", "获取不到学年学期数据！");
			return json.toJSONString();
		}
		
		String userType = setUserType(user);
		
		DateInfo dateInfo = getCurrentDateInfo(user.getUnitId(), semester);
		List<CourseSchedule> courseSchedules = new ArrayList<>();
		try {
			courseSchedules = getCourseSchedules(courseSchedules,startDate, endDate, user.getOwnerId(), semester, dateInfo,
					userType,user.getUnitId());
		}
		catch(BusinessErrorException e) {
			json.put("resultMsg" , e.getMessage());
			return json.toJSONString();
			
		}
		JSONArray data = fillwithNames(semester.getAcadyear(), semester.getSemester(), user.getUnitId(),
				courseSchedules, dateInfo);
		
		json.put("data", data);
		json.put("resultMsg", "调用成功！");
		json.put("resultCode", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
		return json.toJSONString();
	}

	private List<CourseSchedule> getCourseSchedules(List<CourseSchedule> courseSchedules ,String startDate, String endDate, String ownerId, Semester semester,
			DateInfo dateInfo, String userType,String unitId) throws BusinessErrorException{
		if(dateInfo == null || StringUtils.isBlank(dateInfo.getAcadyear()))
			 EntityUtils.copyProperties(getCurrentDateInfo(unitId, semester), dateInfo);
		if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
			if (dateInfo == null) {
				throw new BusinessErrorException("获取不到周次数据！");
			}
			return SUtils.dt(
					courseScheduleRemoteService.findCourseScheduleListByPerId(semester.getAcadyear(),
							semester.getSemester(), dateInfo.getWeek(), ownerId, userType),
					CourseSchedule.class);
		} else {
			if (dateInfo == null) {
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByDates(unitId,format.parse(startDate),format.parse(endDate)), DateInfo.class);
					if(CollectionUtils.isNotEmpty(dateInfoList)){
						dateInfo = dateInfoList.get(0);
					}
				} catch (ParseException e) {
					throw new BusinessErrorException("startDate/endDate转换日期出错格式不对，请调整yyyyMMdd");
				}
			}
			return SUtils.dt(
					courseScheduleRemoteService.findCourseScheduleListByPerId(semester.getAcadyear(),
							semester.getSemester(), startDate, endDate, ownerId, userType),
					CourseSchedule.class);
		}
	}

	private String setUserType(User user) {
		String userType = "1";
		if (user.getOwnerType() == User.OWNER_TYPE_FAMILY || user.getOwnerType() == User.OWNER_TYPE_STUDENT) {
			userType = "2";
		}
		return userType;
	}

	private DateInfo getCurrentDateInfo(String unitId, Semester semester) {
		DateInfo dateInfo = SUtils
				.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(),semester.getSemester(), new Date()), DateInfo.class);
		return dateInfo;
	}


	private JSONArray fillwithNames(String acadyear, int semester, String unitId, List<CourseSchedule> courseSchedules,
			DateInfo dateInfo) {
		Map<String, String> teacherMap = genTeacherMap(courseSchedules);
		Map<String, String> claGraMap =new HashMap<>();
		Map<String, String> classMap = genClassMap(courseSchedules,claGraMap);
		Map<String, Grade> gradeMap = genGradeMap(claGraMap);
		Map<String, String> subjectMap = genSubjectMap(courseSchedules);
		Map<String, String> palceMap = genPlaceMap(courseSchedules);
		
		Map<String, String> usernameMap = new HashMap<>();
		if (MapUtils.isNotEmpty(teacherMap)) {
			List<User> users = User.dt(userRemoteService.findByOwnerIds(teacherMap.keySet().toArray(new String[0])));
			usernameMap = EntityUtils.getMap(users, User::getOwnerId, User::getUsername);
		}

		List<StusysSectionTimeSet> times = SUtils.dt(stusysSectionTimeSetRemoteService
				.findByAcadyearAndSemesterAndUnitId(acadyear, semester, unitId, null, true),
				StusysSectionTimeSet.class);
		Map<Integer, StusysSectionTimeSet> timeMap = EntityUtils.getMap(times, StusysSectionTimeSet::getSectionNumber);

		JSONArray array = new JSONArray();
		for (CourseSchedule cs : courseSchedules) {
			String teacherId = cs.getTeacherId();
			String teacherName = cs.getTeacherName();
			if (StringUtils.isNotBlank(teacherId) && StringUtils.isBlank(teacherName))
				cs.setTeacherName(teacherMap.get(teacherId));

			String classId = cs.getClassId();
			Grade grade = gradeMap.get(classId);

			String className = cs.getClassName();
			if (StringUtils.isNotBlank(classId) && StringUtils.isBlank(className))
				cs.setClassName(classMap.get(classId));

			String subjectId = cs.getSubjectId();
			String subectName = cs.getSubjectName();
			if (StringUtils.isNotBlank(subjectId) && StringUtils.isBlank(subectName))
				cs.setSubjectName(subjectMap.get(subjectId));

			String palceId = cs.getPlaceId();
			String placeName = cs.getPlaceName();
			if (StringUtils.isNotBlank(palceId) && StringUtils.isBlank(placeName))
				cs.setPlaceName(palceMap.get(palceId));

			JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(cs));
			int sectionNum = cs.getPeriod();
			if (grade != null) {
				if (StringUtils.equals(cs.getPeriodInterval(), CourseSchedule.PERIOD_INTERVAL_2)) {
					sectionNum = sectionNum + grade.getMornPeriods();
				}
				if (StringUtils.equals(cs.getPeriodInterval(), CourseSchedule.PERIOD_INTERVAL_3)) {
					sectionNum = sectionNum + grade.getAmLessonCount() + grade.getMornPeriods();
				}
				if (StringUtils.equals(cs.getPeriodInterval(), CourseSchedule.PERIOD_INTERVAL_4)) {
					sectionNum = sectionNum + grade.getAmLessonCount() + grade.getMornPeriods() + grade.getPmLessonCount();
				}
			}
			StusysSectionTimeSet timeSet = timeMap.get(sectionNum);
			if (timeSet != null) {
				json.put("startTime", timeSet.getBeginTime());
				json.put("endTime", timeSet.getEndTime());
			}
			json.put("periodc", sectionNum);
			json.put("username", usernameMap.get(teacherId));

			Date date = dateInfo.getInfoDate();
			int	weekDay = dateInfo.getWeekday();
			int	week = dateInfo.getWeek();
			
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			
			int csWeekDay = cs.getDayOfWeek();  
			int csWeek = cs.getWeekOfWorktime(); 

			int diff = (csWeek - week) * 7 + (csWeekDay + 1 - weekDay);
			c.add(Calendar.DATE, diff);
			json.put("date", DateFormatUtils.format(c, "yyyy-MM-dd"));

			array.add(json);
		}
		return array;
	}

	private Map<String, String> genSubjectMap(List<CourseSchedule> courseSchedules) {
		Set<String> ids = EntityUtils.getSet(courseSchedules, CourseSchedule::getSubjectId);
		Set<String> names = EntityUtils.getSet(courseSchedules, CourseSchedule::getSubjectName);
		Map<String, String> map = new HashMap<>();
		if (CollectionUtils.size(ids) != CollectionUtils.size(names)) {
			List<Course> list = SUtils.dt(courseRemoteService.findListByIds(ids.toArray(new String[0])), Course.class);
			map = EntityUtils.getMap(list, Course::getId, Course::getSubjectName);
		}
		return map;
	}

	private Map<String, String> genPlaceMap(List<CourseSchedule> courseSchedules) {
		Set<String> ids = EntityUtils.getSet(courseSchedules, CourseSchedule::getPlaceId);
		Set<String> names = EntityUtils.getSet(courseSchedules, CourseSchedule::getPlaceName);
		Map<String, String> map = new HashMap<>();
		if (CollectionUtils.size(ids) != CollectionUtils.size(names)) {
			List<TeachPlace> list = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(ids.toArray(new String[0])),
					TeachPlace.class);
			map = EntityUtils.getMap(list, TeachPlace::getId, TeachPlace::getPlaceName);
		}
		return map;
	}

	private Map<String, String> genTeacherMap(List<CourseSchedule> courseSchedules) {
		Set<String> teacherIds = EntityUtils.getSet(courseSchedules, CourseSchedule::getTeacherId);
		Set<String> teacherNames = EntityUtils.getSet(courseSchedules, CourseSchedule::getTeacherName);
		Map<String, String> teacherMap = new HashMap<>();
		if (CollectionUtils.size(teacherNames) != CollectionUtils.size(teacherIds)) {
			List<Teacher> teachers = Teacher.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[0])));
			teacherMap = EntityUtils.getMap(teachers, Teacher::getId, Teacher::getTeacherName);
		}
		return teacherMap;
	}

	private Map<String, String> genClassMap(List<CourseSchedule> courseSchedules,Map<String, String> claGraMap) {
		Set<String> xzClassIds = new HashSet<>();
		Set<String> jxClassIds = new HashSet<>();
		Map<String, String> classMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(courseSchedules)) {
			for(CourseSchedule ent:courseSchedules) {
				if(CourseSchedule.CLASS_TYPE_NORMAL==ent.getClassType()) {
					xzClassIds.add(ent.getClassId());
				}else {
					jxClassIds.add(ent.getClassId());
				}
			}
			
			//行政班
			if(CollectionUtils.isNotEmpty(xzClassIds)) {
				List<Clazz> clazzs = Clazz.dt(classRemoteService.findListByIds(xzClassIds.toArray(new String[0])));
				classMap.putAll(EntityUtils.getMap(clazzs, Clazz::getId, Clazz::getClassNameDynamic));
				claGraMap.putAll(EntityUtils.getMap(clazzs, Clazz::getId, Clazz::getGradeId));
			}
			
			//教学班
			if(CollectionUtils.isNotEmpty(jxClassIds)) {
				List<TeachClass> clazzs = TeachClass.dt(teachClassRemoteService.findListByIds(jxClassIds.toArray(new String[0])));
				classMap.putAll(EntityUtils.getMap(clazzs, TeachClass::getId, TeachClass::getName));
				claGraMap.putAll(EntityUtils.getMap(clazzs, TeachClass::getId, TeachClass::getGradeId));
			}
		}
		
		return classMap;
	}

	private Map<String, Grade> genGradeMap(Map<String, String> claGraMap) {
		Map<String, Grade> classGradeMap = new HashMap<>();
		if(!claGraMap.isEmpty()) {
			List<Grade> grades = SUtils.dt(gradeRemoteService.findListByIds(claGraMap.values().toArray(new String[0])),
					Grade.class);
			if(CollectionUtils.isNotEmpty(grades)) {
				Map<String, Grade> gradeMap = EntityUtils.getMap(grades, Grade::getId);
				for (String key:claGraMap.keySet()) {
					String gradeId = claGraMap.get(key);
					classGradeMap.put(key, gradeMap.get(gradeId));
				}
			}
		}
		return classGradeMap;
	}

	@RequestMapping("/ngke/timetableByPlaceId/{placeId}")
	@ResponseBody
	public String getPlaceTimetableByUsername(@PathVariable String placeId, String startDate, String endDate, HttpServletRequest request) {
		if (isOverMaxNumDay(interfaceType, request, "/openapi/ngke/timetableByPlaceId/{placeId}")){
        	return returnOpenJsonError(OpenApiBaseAction.MAX_NUM_DAY_ERROR_MSG);
        } 
		doSaveInterfaceCount("/openapi/ngke/timetableByPlaceId/{placeId}",request);
		JSONObject json = new JSONObject();
		json.put("resultCode", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		TeachPlace place = SUtils.dc(teachPlaceRemoteService.findTeachPlaceById(placeId), TeachPlace.class);
		if (place == null) {
			json.put("resultMsg", "获取不到场地信息！");
			return json.toJSONString();
		} else {
			if(!doPowerProving(request,place.getUnitId())){
				json.put("resultMsg", "此场地所在的单位没有授权！");
				return json.toJSONString();
			}
		}
		
		//先根据学校来查找 ，不存在后取base_semester
		Semester semester = SUtils.dc(semesterRemoteServicer.getCurrentSemester(SemesterRemoteService.RET_NULL,place.getUnitId()), Semester.class);
		if (semester == null) {
			json.put("resultMsg", "获取不到学年学期数据！");
			return json.toJSONString();
		}
		
		DateInfo dateInfo = getCurrentDateInfo(place.getUnitId(), semester);
		List<CourseSchedule> courseSchedules;
		if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
			if (dateInfo == null) {
				json.put("resultMsg", "获取不到周次数据！");
				return json.toJSONString();
			}
			courseSchedules = SUtils.dt(courseScheduleRemoteService.findByPlaceId(place.getUnitId(),
					semester.getAcadyear(), semester.getSemester(), placeId, dateInfo.getWeek(), true),
					CourseSchedule.class);
		} else {
			if (dateInfo == null) {
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByDates(place.getUnitId(),format.parse(startDate),format.parse(endDate)), DateInfo.class);
					if(CollectionUtils.isNotEmpty(dateInfoList)){
						dateInfo = dateInfoList.get(0);
					}
				} catch (ParseException e) {
					json.put("resultMsg", "startDate/endDate转换日期出错格式不对，请调整yyyyMMdd");
				}
			}
			
			courseSchedules = SUtils.dt(courseScheduleRemoteService.findByPlaceId(place.getUnitId(),
					semester.getAcadyear(), semester.getSemester(), placeId, startDate, endDate, true),
					CourseSchedule.class);
		}

		if(CollectionUtils.isEmpty(courseSchedules)) {
			List<Clazz> clazzs = Clazz.dt(classRemoteService.findListBy(ArrayUtils.toArray("isDeleted", "teachPlaceId", "schoolId"), ArrayUtils.toArray(BooleanUtils.toInteger(Boolean.FALSE.booleanValue()), placeId, place.getUnitId())));
			if(CollectionUtils.isNotEmpty(clazzs)) {
				return getTimetableByClassId(clazzs.get(0).getId(), startDate, endDate, request);
			}
		}
		
		JSONArray data = fillwithNames(semester.getAcadyear(), semester.getSemester(), place.getUnitId(),
				courseSchedules, dateInfo);
		json.put("data", data);
		json.put("resultMsg", "调用成功！");
		json.put("resultCode", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
		return json.toJSONString();
	}
	
	// -------------------------------------------------------私有方法区 ----------------------------------------
	
	/**
	 * @param uri
	 * @param request
	 */
	private void doSaveInterfaceCount(String uri, HttpServletRequest request) {
		doSaveInterfaceCount(interfaceType, request, uri);
	}
}
