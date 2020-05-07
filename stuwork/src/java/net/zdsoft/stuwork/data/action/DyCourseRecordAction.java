package net.zdsoft.stuwork.data.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.service.OpenApiNewElectiveService;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dto.DyCourseRecordDto;
import net.zdsoft.stuwork.data.entity.DyCourseRecord;
import net.zdsoft.stuwork.data.entity.DyCourseStudentRecord;
import net.zdsoft.stuwork.data.entity.DyNightScheduling;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleUser;
import net.zdsoft.stuwork.data.service.DyCourseRecordService;
import net.zdsoft.stuwork.data.service.DyCourseStudentRecordService;
import net.zdsoft.stuwork.data.service.DyNightSchedulingService;
import net.zdsoft.stuwork.data.service.DyPermissionService;
import net.zdsoft.stuwork.data.service.DyWeekCheckRoleUserService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/stuwork") 
public class DyCourseRecordAction extends BaseAction{
	@Autowired
	private DyCourseRecordService dyCourseRecordService;
	@Autowired
	private DyCourseStudentRecordService dyCourseStudentRecordService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private DyPermissionService dyPermissionService;
	@Autowired
	private DyNightSchedulingService dyNightSchedulingService;
	@Autowired 
	private UserRemoteService userRemoteService;
	@Autowired
	private DyWeekCheckRoleUserService dyWeekCheckRoleUserService;
	@Autowired
	private CustomRoleRemoteService customRoleRemoteService;
	
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

	
	@RequestMapping("/courserecord/page")
    @ControllerInfo(value = "")
	public String showPageAdmin(ModelMap map){
		return "/stuwork/courserecord/myCourseRecordAdmin.ftl";
	}
	
	
	@RequestMapping("/courserecord/myCourseHead")
    @ControllerInfo(value = "")
	public String showPageHead(HttpServletRequest request, ModelMap map){
		int period = NumberUtils.toInt(request.getParameter("period"));
		map.put("period", period);
		String queryStr = request.getParameter("queryDate");
		Date queryDate;
		if(StringUtils.isEmpty(queryStr)) {
			queryDate = new Date();
		} else {
			queryDate = DateUtils.string2Date(queryStr);
		}
		String unitId = getLoginInfo().getUnitId();
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
		if(null!=sem){
			boolean recordAdmin = customRoleRemoteService.checkUserRole(unitId, "74", StuworkConstants.COURSERECORD_ADMIN, getLoginInfo().getUserId());
			String acadyear = sem.getAcadyear();
			int semester = sem.getSemester();
			DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, semester, queryDate), DateInfo.class);
			if(dateInfo == null){
				return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！");
			}
			int week = dateInfo.getWeek();
			int weekDay = dateInfo.getWeekday();
			
			int pe = 0;
			if(sem.getMornPeriods() != null){
				pe += sem.getMornPeriods();
			}
			if(sem.getAmPeriods() != null){
				pe += sem.getAmPeriods();
			}
			if(sem.getPmPeriods() != null){
				pe += sem.getPmPeriods();
			}
			if(sem.getNightPeriods() != null){
				pe += sem.getNightPeriods();
			}
			map.put("periodCount", pe);
			map.put("nowDate", dateInfo.getInfoDate());
			map.put("week", week);
			map.put("weekDay", weekDay);
			map.put("acadyear", acadyear);
			map.put("semester", semester);
			map.put("teacherId",getLoginInfo().getOwnerId());
			map.put("recordAdmin", recordAdmin);
		}else{
			return errorFtl(map,"当前时间不在学年学期内，无法维护！");
		}
		return "/stuwork/courserecord/myCourseRecordHead.ftl";
	}
	
	/**
	 * 教师某天日志list
	 * @param acadyear
	 * @param semester
	 * @param queryDate
	 * @param teacherId
	 * @param map
	 * @return
	 */
	@RequestMapping("/courserecord/myCourseList")
    @ControllerInfo(value = "")
	public String showTeacherCourse(String acadyear, String semester, Date queryDate, String teacherId, 
			HttpServletRequest request, ModelMap map){
	    String unitId = getLoginInfo().getUnitId();
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
		if(null!=sem){
			DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, Integer.parseInt(semester), queryDate), DateInfo.class);			
			if(dateInfo == null){
				return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！");
			}
			int week = dateInfo.getWeek();
			int weekDay = dateInfo.getWeekday();
			boolean recordAdmin = BooleanUtils.toBoolean(request.getParameter("recordAdmin"));
			List<CourseSchedule> courseScheduleList;
			List<DyCourseRecord> dyCourseRecordList;
			int per = 0;
			if(recordAdmin) {
				String period = request.getParameter("period");
				if(StringUtils.isNotEmpty(period)) {
					per = NumberUtils.toInt(period);
				}
				String pi = null;
				if (per > 0) {// 换算节次和时间段
					int mp = sem.getMornPeriods() != null?sem.getMornPeriods().intValue():0;
					int ap = sem.getAmPeriods() != null?sem.getAmPeriods().intValue():0;
					int pp = sem.getPmPeriods() != null?sem.getPmPeriods().intValue():0;
					int np = sem.getNightPeriods() != null?sem.getNightPeriods().intValue():0;
					if (per <= mp) {
						pi = CourseSchedule.PERIOD_INTERVAL_1;
					} else if(per <= mp + ap) {
						per = per - mp;
						pi = CourseSchedule.PERIOD_INTERVAL_2;
					} else if(per <= mp + ap + pp) {
						per = per - mp - ap;
						pi = CourseSchedule.PERIOD_INTERVAL_3;
					} else if(per <= mp + ap + pp + np) {
						per = per - mp - ap - pp;
						pi = CourseSchedule.PERIOD_INTERVAL_4;
					}
				}
				CourseScheduleDto dto = new CourseScheduleDto();
				dto.setSchoolId(unitId);
				dto.setAcadyear(acadyear);
				dto.setSemester(NumberUtils.toInt(semester));
				dto.setWeekOfWorktime(week);
				dto.setDayOfWeek(weekDay - 1);
				dto.setPeriod(per);
				dto.setPeriodInterval(pi);
				courseScheduleList = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto), new TR<List<CourseSchedule>>() {});
				dyCourseRecordList = dyCourseRecordService.findListByPeriod(getLoginInfo().getUnitId(), acadyear, semester, "1", week, weekDay, per);
			} else {
				courseScheduleList = SUtils.dt(courseScheduleRemoteService.findByTeacherId(getLoginInfo().getUnitId(), acadyear, Integer.parseInt(semester), teacherId, week), new TR<List<CourseSchedule>>() {}); 
				dyCourseRecordList = dyCourseRecordService.findListBy(getLoginInfo().getUnitId(), acadyear, semester, "1", teacherId, week, weekDay);
			}
			//取出要查询的周次
			List<CourseSchedule> courseScheduleList2 = new ArrayList<CourseSchedule>();
			for(CourseSchedule item : courseScheduleList){
				if(item.getClassType() == 1 && "3".equals(item.getSubjectType())){
					continue;
				}
				if((weekDay-1) == item.getDayOfWeek()){
					//矫正课表中节次与日志中节次不一致
					if(CourseSchedule.PERIOD_INTERVAL_1.equals(item.getPeriodInterval())){
						item.setPeriod(item.getPeriod());
					}else if(CourseSchedule.PERIOD_INTERVAL_2.equals(item.getPeriodInterval())){
						item.setPeriod(item.getPeriod()+sem.getMornPeriods());
					}else if(CourseSchedule.PERIOD_INTERVAL_3.equals(item.getPeriodInterval())){
						item.setPeriod(item.getPeriod()+sem.getMornPeriods()+sem.getAmPeriods());
					}else if(CourseSchedule.PERIOD_INTERVAL_4.equals(item.getPeriodInterval())){
						item.setPeriod(item.getPeriod()+sem.getMornPeriods()+sem.getAmPeriods()+sem.getPmPeriods());
					}
					courseScheduleList2.add(item);
				}
			}
			
			Set<String> recordIdSet = new HashSet<String>();
			for(DyCourseRecord item : dyCourseRecordList){
				recordIdSet.add(item.getId());
			}
			Map<String, String> punishStuMap;
			if(recordIdSet.size()>0){
				punishStuMap = getPunishStuMap(recordIdSet);
			}else{
				punishStuMap = new HashMap<String, String>();
			}
			Set<String> teachClsIdSet2 = new HashSet<String>();
			Set<String> clsSet = new HashSet<String>();
			Set<String> subjectSet = new HashSet<String>();
			Set<String> xuanxiukeIdSet = new HashSet<String>();
			for(CourseSchedule item : courseScheduleList2){
				subjectSet.add(item.getSubjectId());
				if(CourseSchedule.CLASS_TYPE_TEACH==item.getClassType()){
					teachClsIdSet2.add(item.getClassId());
				}else if(CourseSchedule.CLASS_TYPE_4==item.getClassType()){
					xuanxiukeIdSet.add(item.getClassId());
				}else{
					clsSet.add(item.getClassId());
				}
			}
			List<Clazz> clsList2 = SUtils.dt(classRemoteService.findClassListByIds(clsSet.toArray(new String[0])), new TR<List<Clazz>>() {});
			Map<String, String> clsMap2 = new HashMap<String, String>();
			for(Clazz cls : clsList2){
				clsMap2.put(cls.getId(), cls.getClassNameDynamic());
			}
//			Map<String, String> clsNameMap = getClsMap(teachClsIdSet2);
			Map<String, String> clsNameMap = getteachClsNameMap(teachClsIdSet2);
			//选修课
//			for(String xxid : xuanxiukeIdSet){
//				List<String> stuIds = getOpenApiNewElectiveService().getStusByClassId(xxid);
//				List<Student> studentList = SUtils.dt(studentRemoteService.findByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {});
//				Set<String> clsIdSet3 = new HashSet<String>();
//				for(Student stu : studentList){
//					clsIdSet3.add(stu.getClassId());
//				}
//				List<Clazz> clsList3 = SUtils.dt(classRemoteService.findClassListByIds(clsIdSet3.toArray(new String[0])), new TR<List<Clazz>>() {});
//			    String clsNames = "";
//				for(Clazz cls : clsList3){
//					clsNames = clsNames + cls.getClassNameDynamic()+",";
//			    }
//				if(StringUtils.isNotBlank(clsNames)){
//					xxMap.put(xxid, clsNames.substring(0, clsNames.length()-1));
//				}else{
//					xxMap.put(xxid, "");
//				}
//			}
			Map<String, String> courseMap = new HashMap<String, String>();
			if(subjectSet.size()>0){
				List<Course> courseList =  SUtils.dt(courseRemoteService.findListByIds(subjectSet.toArray(new String[0])), new TR<List<Course>>() {});
				for(Course course : courseList){
					courseMap.put(course.getId(), course.getSubjectName());
				}
			}
			for(CourseSchedule item1 : courseScheduleList2){
				for(DyCourseRecord item2 : dyCourseRecordList){
					if(item1.getPeriod() == item2.getPeriod()
							&& StringUtils.isNotBlank(item1.getSubjectId()) && item1.getSubjectId().equals(item2.getSubjectId())
							&& StringUtils.equals(item2.getRecordClass(), item1.getClassId())){
						item1.setRecordId(item2.getId());
						item1.setScore(String.valueOf(item2.getScore()));
						item1.setRemark(item2.getRemark());
						item1.setPunishStudentName(punishStuMap.get(item2.getId()));
					}
				}
				if(2==item1.getClassType() || 3==item1.getClassType()){
					item1.setClassName(clsNameMap.get(item1.getClassId()));
				}else if(4==item1.getClassType()){
					item1.setClassName(item1.getSubjectName());					
				}else{
					item1.setClassName(clsMap2.get(item1.getClassId()));
				}
				if(StringUtils.isBlank(item1.getScore())){
					item1.setScore("/");
				}else{
					item1.setScore(String.valueOf(item1.getScore()).split("\\.")[0]);
				}
				if(StringUtils.isBlank(item1.getPunishStudentName())){
					item1.setPunishStudentName("/");
				}
				if(StringUtils.isBlank(item1.getRemark())){
					item1.setRemark("/");
				}
				item1.setDayOfWeek(item1.getDayOfWeek()+1);
				if(courseMap.containsKey(item1.getSubjectId())){
					item1.setSubjectName(courseMap.get(item1.getSubjectId()));
				}
			}
			Collections.sort(courseScheduleList2,new Comparator<CourseSchedule>(){
	            public int compare(CourseSchedule arg0, CourseSchedule arg1) {
	                int i = String.valueOf(arg0.getPeriod()).compareTo(String.valueOf(arg1.getPeriod()));
	                if(i == 0) {
	                	if(StringUtils.isBlank(arg0.getClassName())) {
	                		return -1;
	                	}else if(StringUtils.isBlank(arg1.getClassName())) {
	                		return 1;
	                	}else {
	                		return arg0.getClassName().compareTo(arg1.getClassName());
	                	}
	                }
	                return i;
	            }
	        });
			map.put("courseScheduleList", courseScheduleList2);
			map.put("acadyear", acadyear);
			map.put("semester", semester);
			map.put("queryDate", queryDate);
			map.put("recordAdmin", recordAdmin);
		}else{
			return errorFtl(map,"当前时间不在学年学期内，无法维护！");
		}		
		return "/stuwork/courserecord/myCourseRecordList.ftl";
	}
	
	/**
	 * 根据教师课表获取日志 点击新增时
	 * @param map
	 * @return
	 */
	@RequestMapping("/courserecord/myCourseAdd")
    @ControllerInfo(value = "")
	public String teacherRecordAdd(String classId, Date queryDate, String acadyear, String semester, String week, String day, 
			String period, String subjectId, HttpServletRequest req, ModelMap map){
		String teacherId = req.getParameter("teacherId");
		map.put("teacherId", teacherId);
		DyCourseRecord dyCourseRecord = dyCourseRecordService.findByAll(getLoginInfo().getUnitId(), acadyear,  semester, "1", teacherId, Integer.parseInt(week), Integer.parseInt(day), Integer.parseInt(period));
		if(null!=dyCourseRecord){
			map.put("recordId", dyCourseRecord.getId());
			List<DyCourseStudentRecord> dyCourseStudentRecordList = dyCourseStudentRecordService.findListByRecordId(dyCourseRecord.getId());
			Map<String, String> selectStuMap = new HashMap<String, String>();
			for(DyCourseStudentRecord item : dyCourseStudentRecordList){
				selectStuMap.put(item.getStudentId(), item.getId());
			}
			map.put("selectStuMap", selectStuMap);
			map.put("dyCourseStudentRecordList", dyCourseStudentRecordList);
			map.put("dyCourseRecord", dyCourseRecord);
		}
		
		String rc = classId;
		map.put("recordClass", rc);
		//classId有可能是行政班id也可能是教学班id
		Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		List<Student> studentList;
		if(cls != null){
			studentList =  SUtils.dt(studentRemoteService.findByClassIds(classId), new TR<List<Student>>() {});
		}else{
			TeachClass teachClass = SUtils.dc(teachClassRemoteService.findOneById(classId), TeachClass.class);
			if(null != teachClass){
				List<TeachClassStu> teachClassStuList =  SUtils.dt(teachClassStuRemoteService.findStudentByClassIds(new String[]{classId}),new TR<List<TeachClassStu>>() {});
				Set<String> stuIdSet = new HashSet<String>();
				for(TeachClassStu item : teachClassStuList){
					stuIdSet.add(item.getStudentId());
			    }			
				studentList =  SUtils.dt(studentRemoteService.findListByIds(stuIdSet.toArray(new String[0])), new TR<List<Student>>() {});
				Set<String> clsIdSet = new HashSet<String>();
				for(Student stu : studentList){
					clsIdSet.add(stu.getClassId());
				}
				String clsIds = "";
				for(String clsId : clsIdSet){
					clsIds = clsIds + clsId + ",";
				}
				classId = clsIds;
			}else{
				List<String> stuIds = getOpenApiNewElectiveService().getStusByClassId(classId);
				studentList =  SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {});
				Set<String> clsIdSet = new HashSet<String>();
				for(Student stu : studentList){
					clsIdSet.add(stu.getClassId());
				}
				String clsIds = "";
				for(String clsId : clsIdSet){
					clsIds = clsIds + clsId + ",";
				}
				classId = clsIds;
			}
		}
		map.put("studentList", studentList);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("queryDate", queryDate);
		map.put("week", week);
		map.put("day", day);
		map.put("period", period);
		map.put("subjectId", subjectId);
		map.put("classId", classId);
		return "/stuwork/courserecord/myCourseRecordAdd.ftl";
	}
	
	/**
	 * 全校日志head
	 * @param map
	 * @return
	 */
	@RequestMapping("/courserecord/allCourseHead")
    @ControllerInfo(value = "")
	public String allClassRecordHead(ModelMap map){
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
		if(null!=sem){
			String acadyear = sem.getAcadyear();
			int semester = sem.getSemester();
			DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), acadyear, semester, new Date()), DateInfo.class);
			if(dateInfo == null){
				return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！");
			}
			int week = dateInfo.getWeek();
			int weekDay = dateInfo.getWeekday();
			map.put("nowDate", dateInfo.getInfoDate());
			map.put("week", week);
			map.put("weekDay", weekDay);
			map.put("acadyear", acadyear);
			map.put("semester", semester);
			//节次list
			int periodNum = sem.getMornPeriods()+sem.getAmPeriods()+sem.getPmPeriods()+sem.getNightPeriods();
			List<String> periodList = new ArrayList<String>();
			for(int i=1;i<=periodNum;i++){
				periodList.add(String.valueOf(i));
			}
			map.put("periodList", periodList);
		}else{
			return errorFtl(map,"当前时间不在学年学期内，无法维护！");
		}
		List<Clazz> clsList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(), sem.getAcadyear()), new TR<List<Clazz>>() {});
		
		//权限班级
		Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
		Iterator<Clazz> it = clsList.iterator();
		while(it.hasNext()) {
			Clazz clazz = it.next();
			if (!classPermission.contains(clazz.getId())) {
				it.remove();
			}
		}
		
		map.put("clsList", clsList);
		List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(getLoginInfo().getUnitId()), new TR<List<Teacher>>() {});
		map.put("teacherList", teacherList);
		return "/stuwork/courserecord/allCourseRecordHead.ftl";
	}
	
	/**
	 * 全校上课日志查询
	 * @param classId
	 * @param period
	 * @param queryDate
	 * @param teacherName
	 * @param map
	 * @return
	 */
	@RequestMapping("/courserecord/allCourseList")
    @ControllerInfo(value = "")
	public String allClassRecordList(String classId, Date queryDate, String period, String teacherId, ModelMap map){
		List<Student> studentList =  SUtils.dt(studentRemoteService.findByClassIds(classId), new TR<List<Student>>() {});
		Set<String> stuIdSet = new HashSet<String>();
		for(Student stu : studentList){
			stuIdSet.add(stu.getId());
		}
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
		Set<String> teachClsIdSet = new HashSet<String>();//教学班id
		if(stuIdSet.size()>0){
    		List<TeachClass> teachClasses = SUtils.dt(teachClassRemoteService.findByStuIds(sem.getAcadyear(), sem.getSemester()+"", "1", stuIdSet.toArray(new String[stuIdSet.size()])),TeachClass.class);
    		Set<String> tClassIds = EntityUtils.getSet(teachClasses, TeachClass::getId);
    		teachClsIdSet.addAll(tClassIds);
    	}
//		List<TeachClassStu> teachClassStuList = SUtils.dt(teachClassStuRemoteService.findByStuIds(stuIdSet.toArray(new String[0])), new TR<List<TeachClassStu>>() {});
//		for(TeachClassStu item : teachClassStuList){
//			teachClsIdSet.add(item.getClassId());
//		}
		
		teachClsIdSet.add(classId);
		//日期转换成周天
		if(null!=sem){
			String acadyear = sem.getAcadyear();
			int semester = sem.getSemester();
			DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), acadyear, semester, queryDate), DateInfo.class);
			if(dateInfo == null){
				return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！");
			}
			int week = dateInfo.getWeek();
			int weekDay = dateInfo.getWeekday();
			int dayOfWeek;
			//if(weekDay == 7){
			//	dayOfWeek = 0;
			//}else{
				dayOfWeek = weekDay;
			//}
			if(getOpenApiNewElectiveService() != null&&stuIdSet.size()>0) {	
				List<String> xxCourseIdList = getOpenApiNewElectiveService().getCouseIdsByUidSemesterAndStuId(getLoginInfo().getUnitId(), sem.getAcadyear(), String.valueOf(sem.getSemester()), stuIdSet.toArray(new String[0]));
				for(String xxCourseId : xxCourseIdList){
					teachClsIdSet.add(xxCourseId);
				}
			}
			List<CourseSchedule> courseScheduleList = SUtils.dt(courseScheduleRemoteService.findByClassIdes(getLoginInfo().getUnitId(), acadyear, semester, teachClsIdSet.toArray(new String[0]), week), new TR<List<CourseSchedule>>() {});
		    //选出那一天的课
			List<CourseSchedule> courseScheduleList1 = new ArrayList<CourseSchedule>();//行政班当天课程
			for(CourseSchedule item : courseScheduleList){
				if(item.getClassType() == 1 && "3".equals(item.getSubjectType())){
					continue;
				}
				if((dayOfWeek-1) == item.getDayOfWeek()){
					if("1".equals(item.getPeriodInterval())){
						item.setPeriod(item.getPeriod());
					}else if("2".equals(item.getPeriodInterval())){
						item.setPeriod(item.getPeriod()+sem.getMornPeriods());
					}else if("3".equals(item.getPeriodInterval())){
						item.setPeriod(item.getPeriod()+sem.getMornPeriods()+sem.getAmPeriods());
					}else if("4".equals(item.getPeriodInterval())){
						item.setPeriod(item.getPeriod()+sem.getMornPeriods()+sem.getAmPeriods()+sem.getPmPeriods());
					}
					courseScheduleList1.add(item);
				}
			}
			//判断是否有period、teacherId
			List<CourseSchedule> courseScheduleList2 = new ArrayList<CourseSchedule>();
			if(StringUtils.isNotBlank(period) && StringUtils.isBlank(teacherId)){
				for(CourseSchedule item : courseScheduleList1){
					if(Integer.parseInt(period) == item.getPeriod()){
						courseScheduleList2.add(item);
					}
				}
			}else if(StringUtils.isBlank(period) && StringUtils.isNotBlank(teacherId)){
				for(CourseSchedule item : courseScheduleList1){
					if(teacherId.equals(item.getTeacherId())){
						courseScheduleList2.add(item);
					}
				}
			}else if(StringUtils.isNotBlank(period) && StringUtils.isNotBlank(teacherId)){
				for(CourseSchedule item : courseScheduleList1){
					if(teacherId.equals(item.getTeacherId()) && (Integer.parseInt(period) == item.getPeriod())){
						courseScheduleList2.add(item);
					}
				}
			}else{
				courseScheduleList2 = courseScheduleList1;
			}
			List<DyCourseRecord> dyCourseRecordList = dyCourseRecordService.findListByRecordDate(getLoginInfo().getUnitId(), "1", queryDate);
			//取出教学班关联的行政班
			Set<String> teachClsIdSet2 = new HashSet<String>();
			for(CourseSchedule item : courseScheduleList2){
				if(2==item.getClassType() || 3==item.getClassType()){
					teachClsIdSet2.add(item.getClassId());
				}
			}
			Map<String, String> clsNameMap = getteachClsNameMap(teachClsIdSet2);
			//////////////////
			Set<String> teacherIdSet = new HashSet<String>();
			Set<String> subjectSet = new HashSet<String>();
			Set<String> classIdSet = new HashSet<String>();
			Set<String> xuanxiukeIdSet = new HashSet<String>();
			for(CourseSchedule item : courseScheduleList2){
				teacherIdSet.add(item.getTeacherId());
				subjectSet.add(item.getSubjectId());
				if(1 == item.getClassType()){
					classIdSet.add(item.getClassId());
				}else if(4 == item.getClassType()){
					xuanxiukeIdSet.add(item.getClassId());
				}
			}
			Map<String, String> clsMap = new HashMap<String, String>();
			List<Clazz> clsList3 = SUtils.dt(classRemoteService.findClassListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>() {});
			for(Clazz cls : clsList3){
				clsMap.put(cls.getId(), cls.getClassNameDynamic());
			}
			
			//选修课
//			Map<String, String> xxMap = new HashMap<String,String>();
//			for(String xxid : xuanxiukeIdSet){
//				List<String> stuIds = getOpenApiNewElectiveService().getStusByClassId(xxid);
//				List<Student> studentList2 = SUtils.dt(studentRemoteService.findByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {});
//				Set<String> clsIdSet3 = new HashSet<String>();
//				for(Student stu : studentList2){
//					clsIdSet3.add(stu.getClassId());
//				}
//				List<Clazz> clsList4= SUtils.dt(classRemoteService.findClassListByIds(clsIdSet3.toArray(new String[0])), new TR<List<Clazz>>() {});
//			    String clsNames = "";
//				for(Clazz cls : clsList4){
//					clsNames = clsNames + cls.getClassNameDynamic()+",";
//			    }
//				if(StringUtils.isNotBlank(clsNames)){
//					xxMap.put(xxid, clsNames.substring(0, clsNames.length()-1));
//				}else{
//					xxMap.put(xxid, "");
//				}
//			}
//			
			Map<String, String> teacherMap = new HashMap<String, String>();
			teacherIdSet.remove(null);
			if(teacherIdSet.size()>0){
				List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIdSet.toArray(new String[0])), new TR<List<Teacher>>() {});
				for(Teacher teacher : teacherList){
					teacherMap.put(teacher.getId(), teacher.getTeacherName());
				}
			}
			Map<String, String> courseMap = new HashMap<String, String>();
			List<Course> courseList =  SUtils.dt(courseRemoteService.findListByIds(subjectSet.toArray(new String[0])), new TR<List<Course>>() {});
			for(Course course : courseList){
				courseMap.put(course.getId(), course.getSubjectName());
			}
			//违纪学生
			Set<String> recordIdSet = new HashSet<String>();
			for(DyCourseRecord item : dyCourseRecordList){
				recordIdSet.add(item.getId());
			}
			Map<String, String> punishStuMap;
			if(recordIdSet.size()>0){
				punishStuMap = getPunishStuMap(recordIdSet);
			}else{
				punishStuMap = new HashMap<String, String>();
			}
			//////
			for(CourseSchedule item : courseScheduleList2){
				for(DyCourseRecord item2 : dyCourseRecordList){
					if(/*item.getPeriodInterval().equals(String.valueOf(getInterval(item2.getPeriod()))) &&*/ item.getPeriod()== item2.getPeriod()
							&& StringUtils.equals(item.getClassId(), item2.getRecordClass())
							&& StringUtils.isNotBlank(item.getSubjectId()) && item.getSubjectId().equals(item2.getSubjectId())
							){
						item.setRecordId(item2.getId());
						item.setRemark(item2.getRemark());
						item.setScore(String.valueOf(item2.getScore()).split("\\.")[0]);
						//item.setPeriod(item2.getPeriod());
						item.setPunishStudentName(punishStuMap.get(item2.getId()));
					}
				}
				if(StringUtils.isBlank(item.getRemark())){
					item.setRemark("/");
				}
				if(StringUtils.isBlank(item.getPunishStudentName())){
					item.setPunishStudentName("/");
				}
				if(StringUtils.isBlank(item.getScore())){
					item.setScore("/");
				}
				item.setTeacherName(teacherMap.get(item.getTeacherId()));
				if(2==item.getClassType() || 3==item.getClassType()){
					item.setClassName(clsNameMap.get(item.getClassId()));
				}else if(4==item.getClassType()){
					item.setClassName(item.getSubjectName());
				}else{
					item.setClassName(clsMap.get(item.getClassId()));
				}
				item.setSubjectName(courseMap.get(item.getSubjectId()));
			}
			Collections.sort(courseScheduleList2,new Comparator<CourseSchedule>(){
	            public int compare(CourseSchedule arg0, CourseSchedule arg1) {
	                int i = String.valueOf(arg0.getPeriod()).compareTo(String.valueOf(arg1.getPeriod()));
	                if(i == 0) {
	                	if(StringUtils.isBlank(arg0.getClassName())) {
	                		return -1;
	                	}else if(StringUtils.isBlank(arg1.getClassName())) {
	                		return 1;
	                	}else {
	                		return arg0.getClassName().compareTo(arg1.getClassName());
	                	}
	                }
	                return i;
	            }
	        });
			map.put("courseScheduleList", courseScheduleList2);
		}else{
			return errorFtl(map,"当前时间不在学年学期内，无法维护！");
		}
		return "/stuwork/courserecord/allCourseRecordList.ftl";
	}
	
	private Map<String, String> getteachClsNameMap(Set<String> teachClsIdSet){
		Map<String, String> teachClsNameMap = new HashMap<String, String>();
		if(CollectionUtils.isEmpty(teachClsIdSet)){
			return teachClsNameMap;
		}
		List<TeachClass> teachClasses = SUtils.dt(teachClassRemoteService.findTeachClassContainNotUseByIds(teachClsIdSet.toArray(new String[0])),TeachClass.class);
		if(CollectionUtils.isNotEmpty(teachClasses)){
			for (TeachClass c : teachClasses) {
				teachClsNameMap.put(c.getId(), c.getName());
			}
		}
		return teachClsNameMap;
	}
	//取出教学班关联的行政
	public Map<String, String> getClsMap(Set<String> teachClsIdSet2){
		List<TeachClassStu> teachClassStuList2 = SUtils.dt(teachClassStuRemoteService.findStudentByClassIds(teachClsIdSet2.toArray(new String[0])), new TR<List<TeachClassStu>>() {});
		Set<String> stuIdSet2 = new HashSet<String>();
		for(TeachClassStu teachStu : teachClassStuList2){
			stuIdSet2.add(teachStu.getStudentId());
		}
		List<Student> studentList2 =  SUtils.dt(studentRemoteService.findListByIds(stuIdSet2.toArray(new String[0])), new TR<List<Student>>() {});
		Set<String> clsIdSet = new HashSet<String>();
		for(Student stu : studentList2){
			clsIdSet.add(stu.getClassId());
		}
		List<Clazz> clsList = SUtils.dt(classRemoteService.findClassListByIds(clsIdSet.toArray(new String[0])), new TR<List<Clazz>>() {});
		Map<String, String> clsMap = new HashMap<String, String>();
		for(Clazz cls : clsList){
			clsMap.put(cls.getId(), cls.getClassNameDynamic());
		}
		Map<String, String> stuMap = new HashMap<String, String>();
		for(Student stu : studentList2){
			stuMap.put(stu.getId(), clsMap.get(stu.getClassId()));
		}
		Map<String, String> teachClsNameMap = new HashMap<String, String>();
		for(String teachClsId : teachClsIdSet2){
			Set<String> clsNameSet = new HashSet<String>();
			for(TeachClassStu teachStu : teachClassStuList2){
				if(teachClsId.equals(teachStu.getClassId())){
					clsNameSet.add(stuMap.get(teachStu.getStudentId()));
				}
			}
			String clsNames = "";
			for(String clsName : clsNameSet){
				clsNames = clsNames + clsName + "、";
			}
			clsNames = clsNames.substring(0, clsNames.length()-1);
			teachClsNameMap.put(teachClsId, clsNames);
		}
		return teachClsNameMap;
	}
	
	//获取违纪学生map
	public Map<String, String> getPunishStuMap(Set<String> recordIdSet){
		List<DyCourseStudentRecord> dyCourseStudentRecordList = dyCourseStudentRecordService.findListByRecordIds(recordIdSet.toArray(new String[0]));
		Set<String> studentIdSet = new HashSet<String>();
		for(DyCourseStudentRecord stuRecord : dyCourseStudentRecordList){
			studentIdSet.add(stuRecord.getStudentId());
		}
		List<Student> studentList3 =  SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>() {});
		Map<String, String> studentMap = new HashMap<String, String>();
		for(Student stu : studentList3){
			studentMap.put(stu.getId(), stu.getStudentName());
		}
		Map<String, String> punishStuMap = new HashMap<String, String>();
		for(String recordId : recordIdSet){
			String punishStuNames = "";
			for(DyCourseStudentRecord item : dyCourseStudentRecordList){
				if(recordId.equals(item.getRecordId())){
					punishStuNames = punishStuNames + studentMap.get(item.getStudentId()) + "、";
				}
			}
			if(StringUtils.isBlank(punishStuNames)){
				punishStuNames = "";
			}else{
				punishStuNames = punishStuNames.substring(0, punishStuNames.length()-1);
			}
			punishStuMap.put(recordId, punishStuNames);
		}
		return punishStuMap;
	}
	
	public Map<String, String> getPunishStuIdMap(Set<String> recordIdSet){
		List<DyCourseStudentRecord> dyCourseStudentRecordList = dyCourseStudentRecordService.findListByRecordIds(recordIdSet.toArray(new String[0]));
		Set<String> studentIdSet = new HashSet<String>();
		for(DyCourseStudentRecord stuRecord : dyCourseStudentRecordList){
			studentIdSet.add(stuRecord.getStudentId());
		}
		List<Student> studentList3 =  SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>() {});
		Map<String, String> studentIdMap = new HashMap<String, String>();
		for(Student stu : studentList3){
			studentIdMap.put(stu.getId(), stu.getId());
		}
		Map<String, String> punishStuIdMap = new HashMap<String, String>();
		for(String recordId : recordIdSet){
			String punishStuIds = "";
			for(DyCourseStudentRecord item : dyCourseStudentRecordList){
				if(recordId.equals(item.getRecordId())){
					punishStuIds = punishStuIds + studentIdMap.get(item.getStudentId()) + ",";
				}
			}
			if(StringUtils.isBlank(punishStuIds)){
				punishStuIds = "";
			}else{
				punishStuIds = punishStuIds.substring(0, punishStuIds.length()-1);
			}
			punishStuIdMap.put(recordId, punishStuIds);
		}
		return punishStuIdMap;
	}
	
	
	
	
	
	public int getInterval(int peroid){
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
		int interval;
		if(peroid<=sem.getMornPeriods()){
			interval = 1;
		}else if(peroid>sem.getMornPeriods() && peroid<=sem.getMornPeriods()+sem.getAmPeriods()){
			interval = 2;
		}else if(peroid>sem.getMornPeriods()+sem.getAmPeriods() && peroid<=sem.getMornPeriods()+sem.getAmPeriods()+sem.getPmPeriods()){
			interval = 3;
		}else{
			interval = 4;
		}
		return interval;
	}
	
	public int getPeriod(int peroid){
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
		int rePeriod;
		if(peroid<=sem.getMornPeriods()){
			rePeriod = peroid;
		}else if(peroid>sem.getMornPeriods() && peroid<=sem.getMornPeriods()+sem.getAmPeriods()){
			rePeriod = peroid - sem.getMornPeriods();
		}else if(peroid>sem.getMornPeriods()+sem.getAmPeriods() && peroid<=sem.getMornPeriods()+sem.getAmPeriods()+sem.getPmPeriods()){
			rePeriod = peroid - sem.getMornPeriods() - sem.getAmPeriods();
		}else{
			rePeriod = peroid - sem.getMornPeriods() - sem.getAmPeriods() - sem.getPmPeriods();
		}
		return rePeriod;
	}
	
	/**
	 * 晚自习head页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/courserecord/nightCoursePage")
    @ControllerInfo(value = "")
	public String teacherNightHead(HttpServletRequest req, ModelMap map){
		String gradeId = req.getParameter("gradeId");
	    map.put("gradeId", gradeId);
	    String qd = req.getParameter("queryDate");
	    Date queryDate = null;
	    if(StringUtils.isNotEmpty(qd)) {
	    	queryDate = DateUtils.string2Date(qd);
	    } else {
	    	queryDate = new Date();
	    }
		String unitId = getLoginInfo().getUnitId();
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), new TR<List<Grade>>() {});
		if(null!=sem){
			String acadyear = sem.getAcadyear();
			int semester = sem.getSemester();
			map.put("nowDate", queryDate);
			map.put("acadyear", acadyear);
			map.put("semester", semester);
		}else{
			return errorFtl(map,"当前时间不在学年学期内，无法维护！");
		}
		map.put("gradeId", gradeId);
		map.put("gradeList", gradeList);
		return "/stuwork/courserecord/nightCourseRecordHead.ftl";
	}
	
	/**
	 * 晚自习list页面
	 * @param acadyear
	 * @param semester
	 * @param gradeId
	 * @param queryDate
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/courserecord/nightCourseList")
    @ControllerInfo(value = "")
	public String teacherNightList(String acadyear, int semester, String gradeId, Date queryDate, 
			HttpServletRequest request, ModelMap map){
		DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), acadyear, semester, queryDate), DateInfo.class);
		if(dateInfo == null){
			return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！");
		}
		boolean recordAdmin = customRoleRemoteService.checkUserRole(getLoginInfo().getUnitId(), "74", StuworkConstants.COURSERECORD_ADMIN, getLoginInfo().getUserId());
		map.put("recordAdmin", recordAdmin);
		map.put("acadyear", acadyear);
		map.put("semester", String.valueOf(semester));
		int week = dateInfo.getWeek();
		int day = dateInfo.getWeekday();
		map.put("week", week);
		map.put("day", day);
		map.put("queryDate", queryDate);
		List<Clazz>	clsList;
		if (!recordAdmin) {
			Grade grade = gradeRemoteService.findOneObjectById(gradeId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<DyWeekCheckRoleUser> dyWeekCheckRoleUsers = dyWeekCheckRoleUserService.findCheckTeacherByUserId(
					getLoginInfo().getUnitId(), getLoginInfo().getUserId(), sdf.format(queryDate));
			clsList = new ArrayList<Clazz>();
			boolean hasSection = false;
			if (dyWeekCheckRoleUsers != null && dyWeekCheckRoleUsers.size() > 0) {
				for (DyWeekCheckRoleUser dyWeekCheckRoleUser : dyWeekCheckRoleUsers) {
					if (dyWeekCheckRoleUser.getSection().equals(grade.getSection() + "")) {
						clsList = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {
						});
						hasSection = true;
						break;
					}
				}
			}
			if (!hasSection) {
				List<Clazz> clsList1 = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId),
						new TR<List<Clazz>>() {
						});
				Set<String> classIds1 = EntityUtils.getSet(clsList1, Clazz::getId);
				Map<String, Clazz> clazzMap = EntityUtils.getMap(clsList1, Clazz::getId);
				//根据晚自习排班表 来过滤班级
				User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId()), User.class);
				List<DyNightScheduling> nightSchedulings = dyNightSchedulingService
						.findByUserAndNightTime(getLoginInfo().getUnitId(), user.getOwnerId(), queryDate);
				Set<String> classIds2 = EntityUtils.getSet(nightSchedulings, DyNightScheduling::getClassId);
				//取两个集合的交集
				List<String> showClassIds = (List<String>) CollectionUtils.intersection(classIds1, classIds2);

				for (String classId : showClassIds) {
					clsList.add(clazzMap.get(classId));
				}
			} 
		} else {
			clsList = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {});
		}
		List<DyCourseRecord> dyCourseRecordList;
		if (CollectionUtils.isNotEmpty(clsList)) {
			dyCourseRecordList = dyCourseRecordService.findListByRecordDate(getLoginInfo().getUnitId(), "2",
					queryDate);
		} else {
			dyCourseRecordList  = new ArrayList<>();
		}
		Set<String> recordIdSet = new HashSet<String>();
		for(DyCourseRecord item : dyCourseRecordList){
			recordIdSet.add(item.getId());
		}
		Map<String, String> stuNameMap;
		Map<String, String> stuIdMap;
		if(recordIdSet.size()>0){
			stuNameMap = getPunishStuMap(recordIdSet);
			stuIdMap = getPunishStuIdMap(recordIdSet);
		}else{
			stuNameMap = new HashMap<String, String>();
			stuIdMap = new HashMap<String, String>();
		}
		for(Clazz cls : clsList){
			for(DyCourseRecord item : dyCourseRecordList){
				if(cls.getId().equals(item.getClassId()) 
						|| cls.getId().equals(item.getRecordClass())){
					cls.setRemark(item.getRemark());
					cls.setScore(item.getScore());
					cls.setPunishStuNames(stuNameMap.get(item.getId()));
					cls.setStudentIds(stuIdMap.get(item.getId()));
				}
			}
		}

//		String roleId = this.getLoginInfo().getUserId();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		List<DyWeekCheckRoleUser> roles = dyWeekCheckRoleUserService.findByUserId(getLoginInfo().getUnitId(), roleId, acadyear, semester+"");
//		if(roles!=null) {
//			List<DyWeekCheckTable> teables = dyWeekCheckRoleUserService.findByCheckTable(getLoginInfo().getUnitId(), acadyear, semester + "", roles.get(0).getRoleType(), roleId);
//			for(DyWeekCheckTable tea : teables){
//				String secs =null;
//				for(String s : tea.getSections()){
//					if(org.apache.commons.lang3.StringUtils.isBlank(secs)){
//						secs = s;
//					}else{
//						secs = secs+","+s;
//					}
//				}
//				tea.setSec(secs);
//			}
//
//			if(teables.size()!=0&&teables!=null) {
//				//0代表没有权限1代表有
//				String querydate = sdf.format(queryDate);
//				String weekdate = sdf.format(teables.get(0).getWeekDate());
//				if(!querydate.equals(weekdate)||teables.get(0).getSec().indexOf(grade.getSection()+"")==-1){
//					map.put("allowsave",0);
//				}else{
//					map.put("allowsave",1);
//				}
//			}
//		}

		map.put("clsList", clsList);
		map.put("isDefault", CollectionUtils.isEmpty(dyCourseRecordList));
		return "/stuwork/courserecord/nightCourseRecordList.ftl";
	}
	

	@ResponseBody
	@RequestMapping("/courserecord/nightCourseRecordSave")
    @ControllerInfo(value = "")
	public String nightCourseRecordSave(DyCourseRecordDto dyCourseRecordDto, String acadyear, String semester, String type, int week, int day, String queryDate){
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String dstr=queryDate;  
			Date date=sdf.parse(dstr);
			List<DyCourseRecord> dyCourseRecordList = dyCourseRecordDto.getDyCourseRecordList();
			if(null==dyCourseRecordList){
				return error("所选年级下没有需要录入晚自习日志的班级！");
			}
			Set<String> clsIdSet = new HashSet<String>();
			for(DyCourseRecord item : dyCourseRecordList){
				clsIdSet.add(item.getClassId());
			}
			List<Clazz> clsList = SUtils.dt(classRemoteService.findClassListByIds(clsIdSet.toArray(new String[0])), new TR<List<Clazz>>() {});
			Map<String, String> clsNameMap = new HashMap<String, String>();			
			for(Clazz cls : clsList){
				clsNameMap.put(cls.getId(), cls.getClassNameDynamic());
			}
			List<DyCourseStudentRecord> dyCourseStudentRecordList = new ArrayList<DyCourseStudentRecord>();
			Set<String> classIdSet = new HashSet<String>();
			for(DyCourseRecord item : dyCourseRecordList){
				classIdSet.add(item.getClassId());
				String id = UuidUtils.generateUuid();
				item.setId(id);
				item.setSchoolId(getLoginInfo().getUnitId());
				item.setAcadyear(acadyear);
				item.setSemester(semester);
				item.setTeacherId(getLoginInfo().getOwnerId());
				item.setWeek(week);
				item.setDay(day);
				item.setRecordDate(date);
				item.setType(type);
				item.setRecordClass(item.getClassId());
				/*String tscore = String.valueOf(item.getScore()).split("\\.")[1];
				if(item.getScore()>5 || item.getScore()<0 || tscore.length()>1){
					return error(clsNameMap.get(item.getClassId())+"--考核分请输入正确格式，可输入分数范围0-5，可以保留一位小数！");
				}*/
				if(StringUtils.isNotBlank(item.getStudentIds())){
					if(item.getStudentIds().length()==32){
						DyCourseStudentRecord stuRec = new DyCourseStudentRecord();
						stuRec.setId(UuidUtils.generateUuid());
						stuRec.setSchoolId(getLoginInfo().getUnitId());
						stuRec.setAcadyear(acadyear);
						stuRec.setSemester(semester);
						stuRec.setTeacherId(getLoginInfo().getOwnerId());
						stuRec.setType(item.getType());
						stuRec.setClassId(item.getClassId());
						stuRec.setWeek(week);
						stuRec.setDay(day);
						stuRec.setRecordId(id);
						stuRec.setStudentId(item.getStudentIds());
						stuRec.setType(type);
						stuRec.setRecordDate(date);
						dyCourseStudentRecordList.add(stuRec);
					}else{
						String[] stuIdArray = item.getStudentIds().split(",");
						for(String stuId : stuIdArray){
							DyCourseStudentRecord stuRec = new DyCourseStudentRecord();
							stuRec.setId(UuidUtils.generateUuid());
							stuRec.setSchoolId(getLoginInfo().getUnitId());
							stuRec.setAcadyear(acadyear);
							stuRec.setSemester(semester);
							stuRec.setTeacherId(getLoginInfo().getOwnerId());
							stuRec.setType(item.getType());
							stuRec.setClassId(item.getClassId());
							stuRec.setWeek(week);
							stuRec.setDay(day);
							stuRec.setRecordId(id);
							stuRec.setStudentId(stuId);
							stuRec.setType(type);
							stuRec.setRecordDate(date);
							dyCourseStudentRecordList.add(stuRec);
						}
					}
				}
			}
			dyCourseRecordService.saveNightCourseRecord(dyCourseRecordList, dyCourseStudentRecordList, acadyear, semester, week, day, "2", classIdSet.toArray(new String[0]));
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
	}
	
	/**
	 * 教师上课日志保存
	 * @param dyCourseRecord
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/courserecord/myCourseSave")
    @ControllerInfo(value = "")
	public String save(DyCourseRecord dyCourseRecord, String queryDate, String[] lkxzSelect){
		try{
			List<DyCourseStudentRecord> dyCourseStudentRecordList = new ArrayList<DyCourseStudentRecord>();
			if(null!=lkxzSelect && lkxzSelect.length>0){
				List<Student> studentList =  SUtils.dt(studentRemoteService.findListByIds(lkxzSelect), new TR<List<Student>>() {});
				Map<String, String> stuMap = new HashMap<String, String>();
				for(Student stu : studentList){
					stuMap.put(stu.getId(), stu.getClassId());
				}
				for(String stuId : lkxzSelect){
					DyCourseStudentRecord item = new DyCourseStudentRecord();
				    item.setStudentId(stuId);
				    item.setClassId(stuMap.get(stuId));
				    dyCourseStudentRecordList.add(item);
				}
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String dstr=queryDate;  
			Date date=sdf.parse(dstr);
			dyCourseRecord.setRecordDate(date);
			dyCourseRecord.setSchoolId(getLoginInfo().getUnitId());
			if (StringUtils.isEmpty(dyCourseRecord.getTeacherId())) {
				dyCourseRecord.setTeacherId(getLoginInfo().getOwnerId());
			}
			if(StringUtils.isNotBlank(dyCourseRecord.getSubjectId())){
				if(null == SUtils.dc(courseRemoteService.findOneById(dyCourseRecord.getSubjectId()), Course.class)){
					List<CourseSchedule> scheduleList = SUtils.dt(courseScheduleRemoteService.findBySubjectId(getLoginInfo().getUnitId(), dyCourseRecord.getSubjectId()), new TR<List<CourseSchedule>>() {});
				    if(scheduleList.size()>0){
				    	dyCourseRecord.setSubjectName(scheduleList.get(0).getSubjectName());
				    }else{
				    	dyCourseRecord.setSubjectName("");
				    }
				}else{
					String subjectName = SUtils.dc(courseRemoteService.findOneById(dyCourseRecord.getSubjectId()), Course.class).getSubjectName();
					dyCourseRecord.setSubjectName(subjectName);
				}
			}
			dyCourseRecordService.save(dyCourseRecord, dyCourseStudentRecordList);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
	}
	
	/**
	 * 日志汇总页面Head
	 * @param acadyear
	 * @param semester
	 * @param week
	 * @param weekday
	 * @param classId
	 * @param map
	 * @return
	 */
	@RequestMapping("/courserecord/recordCollectPage")
    @ControllerInfo(value = "")
	public String recordCollectHead(ModelMap map){
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
		if(null!=sem){
			String acadyear = sem.getAcadyear();
			int semester = sem.getSemester();
			DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), acadyear, semester, new Date()), DateInfo.class);
			if(dateInfo == null){
				return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！");
			}
			List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(getLoginInfo().getUnitId(), acadyear, semester), new TR<List<DateInfo>>() {});
			Set<String> weekSet = new HashSet<String>();
			for(DateInfo item : dateInfoList){
				weekSet.add(String.valueOf(item.getWeek()));
			}
			int[] weekArray = new int[weekSet.size()];
			int i = 0;
			for(String week : weekSet){
				weekArray[i] = Integer.parseInt(week);
				i++;
			}
			Arrays.sort(weekArray);			
			int week = dateInfo.getWeek();
			int weekDay = dateInfo.getWeekday();
			map.put("nowDate", dateInfo.getInfoDate());
			map.put("week", week);
			map.put("weekDay", weekDay);
			map.put("acadyear", acadyear);
			map.put("semester", semester);
			map.put("weekArray", weekArray);
			map.put("unitClass", getLoginInfo().getUnitClass());
		}else{
			return errorFtl(map,"当前时间不在学年学期内，无法维护！");
		}
		map.put("acadyearList", acadyearList);
		return "/stuwork/courserecord/recordCollectHead.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/courserecord/searchWeek")
	public int[] clsList(String acadyear,int semester){
		List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(getLoginInfo().getUnitId(), acadyear, semester), new TR<List<DateInfo>>() {});
		Set<String> weekSet = new HashSet<String>();
		for(DateInfo item : dateInfoList){
			weekSet.add(String.valueOf(item.getWeek()));
		}
		int[] weekArray = new int[weekSet.size()];
		int i = 0;
		for(String week : weekSet){
			weekArray[i] = Integer.parseInt(week);
			i++;
		}
		Arrays.sort(weekArray);	
		return weekArray;
	}
	
	//获取汇总违纪学生
	public Map<String, String> getColletPunishStuNameMap(String unitId, String acadyear, String semester, int week, int day, String classId, int periodNum){
		Map<String, String> colletPunishStuNameMap = new HashMap<String, String>();
		List<DyCourseStudentRecord> dyCourseStudentRecordList = dyCourseStudentRecordService.findListByClassId(unitId, acadyear, semester, week, day, classId);
		Set<String> studentIdSet = new HashSet<String>();
		for(DyCourseStudentRecord stuRecord : dyCourseStudentRecordList){
			studentIdSet.add(stuRecord.getStudentId());
		}
		List<Student> studentList =  SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>() {});
		Map<String, String> studentMap = new HashMap<String, String>();
		for(Student stu : studentList){
			studentMap.put(stu.getId(), stu.getStudentName());
		}
		for(int i=1;i<=periodNum;i++){
			String stuNames = "";
			for(DyCourseStudentRecord item : dyCourseStudentRecordList){
				if(i == item.getPeriod()){
					stuNames = stuNames + studentMap.get(item.getStudentId()) + "、";
				}
			}
			if(StringUtils.isNotBlank(stuNames)){
				colletPunishStuNameMap.put(String.valueOf(i), stuNames.substring(0, stuNames.length()-1));
			}
		}
		return colletPunishStuNameMap;
	}
	
	
	/**
	 * 日志汇总页面
	 * @param acadyear
	 * @param semester
	 * @param week
	 * @param weekday
	 * @param classId
	 * @param map
	 * @return
	 */
	@RequestMapping("/courserecord/recordCollectList")
    @ControllerInfo(value = "")
    public String recordCollectList(String acadyear, String semester, int week, int weekDay, String classId, ModelMap map){
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
    	if(null!=sem){
        	int dayOfWeek = weekDay;
    		int periodNum = sem.getMornPeriods() + sem.getAmPeriods() + sem.getPmPeriods() + sem.getNightPeriods();
    		List<DyCourseRecord> dyCourseRecordList = dyCourseRecordService.findListByRecordClassId(getLoginInfo().getUnitId(), acadyear, semester, week, dayOfWeek, "1", classId);
    		Map<String, String> studentNameMap = getColletPunishStuNameMap(getLoginInfo().getUnitId(), acadyear, semester, week, weekDay, classId, periodNum);
    		List<CourseSchedule> courseScheduleListResult = new ArrayList<CourseSchedule>();
    		for(int i=1;i<=periodNum;i++){
    			int count = 0;
    			float num = 0;
    			float score;
    			CourseSchedule item = new CourseSchedule();
    			if(dyCourseRecordList.size()>0){
    				for(DyCourseRecord item2 : dyCourseRecordList){
        				if(item2.getPeriod() == i){
        					count = count + 1;
        					num = num + item2.getScore();
        				}
        			}
    				score = num/count;
    				if(String.valueOf(score).equals("NaN")){
    					item.setScore(String.valueOf("/"));
    				}else{
    					item.setScore(String.valueOf(score).split("\\.")[0]);
    				}
    				item.setPunishStudentName(studentNameMap.get(String.valueOf(i)));
    			}else{
    				item.setScore(String.valueOf("/"));
    			}
    			if(StringUtils.isBlank(item.getPunishStudentName())){
					item.setPunishStudentName("/");
				}
    			item.setPeriod(i);//节次
    			courseScheduleListResult.add(item);
    		}
    		map.put("courseScheduleListResult", courseScheduleListResult);
    		int scoreNum = 0;
    		for(CourseSchedule item :  courseScheduleListResult){
    			if(!"/".equals(item.getScore())){
    				scoreNum = scoreNum + Integer.parseInt(item.getScore());
    			}
    		}
    		map.put("scoreNum", String.valueOf(scoreNum));
    		//晚自习
    		List<DyCourseRecord> dyCourseRecordList2 = dyCourseRecordService.findListByRecordClassId(getLoginInfo().getUnitId(), acadyear, semester, week, dayOfWeek, "2", classId);    		
    		String nightScore = "";
    		String nightScoreNum = "";
    		String nightPunishStuName = "";
    		if(dyCourseRecordList2.size()>0){
    			nightScore = String.valueOf(dyCourseRecordList2.get(0).getScore());
    			nightScoreNum = String.valueOf(dyCourseRecordList2.get(0).getScore());
    			List<DyCourseStudentRecord> dyCourseStudentRecordList = dyCourseStudentRecordService.findListByRecordIds(new String[]{dyCourseRecordList2.get(0).getId()});
    			Set<String> studentIdSet = new HashSet<String>();
    			for(DyCourseStudentRecord stuRecord : dyCourseStudentRecordList){
    				studentIdSet.add(stuRecord.getStudentId());
    			}
    			List<Student> studentList3 =  SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>() {});
    			for(Student stu : studentList3){
    				nightPunishStuName = nightPunishStuName + stu.getStudentName() + "、";
    			}
    			if(StringUtils.isNotBlank(nightPunishStuName)){
    				nightPunishStuName = nightPunishStuName.substring(0, nightPunishStuName.length()-1);
    			}else{
    				nightPunishStuName = "/";
    			}
    		}else{
    			nightScore = "/";
    			nightPunishStuName = "/";
    			nightScoreNum = "0";
    		}
    		map.put("nightScore", nightScore);
    		map.put("nightPunishStuName", nightPunishStuName);
    		map.put("nightScoreNum", nightScoreNum);
    		map.put("acadyear", acadyear);
    		map.put("semester", semester);
    		map.put("week", String.valueOf(week));
    		map.put("day", String.valueOf(weekDay));
    		map.put("classId", classId);
    		String className = SUtils.dt(classRemoteService.findClassListByIds(new String[]{classId}), new TR<List<Clazz>>() {}).get(0).getClassNameDynamic();
    		map.put("className", className);
    	}else{
    		return errorFtl(map,"当前时间不在学年学期内，无法维护！");
    	}
    	return "/stuwork/courserecord/recordCollectList.ftl";
    }
	
	@RequestMapping("/courserecord/recordCollectDetail")
    @ControllerInfo(value = "")
	public String recordCollectDetail(String acadyear, String semester, int week, int day, int period, String classId, String type, String score, String punishStudentName, ModelMap map){
		if("1".equals(type)){
			List<DyCourseRecord> dyCourseRecordList = dyCourseRecordService.findListByRecordClassId(getLoginInfo().getUnitId(), acadyear, semester, week, day, "1", "%"+classId+"%");
			List<DyCourseRecord> dyCourseRecordList1 = new ArrayList<DyCourseRecord>();
			Set<String> teacherIdSet = new HashSet<String>();
			Set<String> subjectSet = new HashSet<String>();
			for(DyCourseRecord item : dyCourseRecordList){
				if(period == item.getPeriod()){
					dyCourseRecordList1.add(item);
					teacherIdSet.add(item.getTeacherId());
					subjectSet.add(item.getSubjectId());
				}
			}
			Map<String, String> courseMap = new HashMap<String, String>();
			if(subjectSet.size()>0){
				List<Course> courseList =  SUtils.dt(courseRemoteService.findListByIds(subjectSet.toArray(new String[0])), new TR<List<Course>>() {});
				for(Course course : courseList){
					courseMap.put(course.getId(), course.getSubjectName());
				}
			}
			Map<String, String> teacherMap = new HashMap<String, String>();
			teacherIdSet.remove(null);
			if(teacherIdSet.size()>0){
				List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIdSet.toArray(new String[0])), new TR<List<Teacher>>() {});
				for(Teacher teacher : teacherList){
					teacherMap.put(teacher.getId(), teacher.getTeacherName());
				}
			}
			for(DyCourseRecord item : dyCourseRecordList1){
				item.setTeacherName(teacherMap.get(item.getTeacherId()));
				item.setSubjectName(courseMap.get(item.getSubjectId()));
			}
			map.put("dyCourseRecordList", dyCourseRecordList1);
		}else{
			List<DyCourseRecord> dyCourseRecordList1 = dyCourseRecordService.findListByRecordClassId(getLoginInfo().getUnitId(), acadyear, semester, week, day, "2", classId);
			map.put("dyCourseRecordList", dyCourseRecordList1);
		}
		map.put("type", type);
		map.put("score", score);
		map.put("punishStudentName", punishStudentName);
		return "/stuwork/courserecord/recordCollectDetail.ftl";
	}
}
