package net.zdsoft.basedata.action;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.constant.TipsayConstants;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.TeachGroupDto;
import net.zdsoft.basedata.dto.TeacherDto;
import net.zdsoft.basedata.dto.TipsayDto;
import net.zdsoft.basedata.dto.TipsayScheduleDto;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachGroupEx;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Tipsay;
import net.zdsoft.basedata.entity.TipsayEx;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachGroupExService;
import net.zdsoft.basedata.service.TeachGroupService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.TipsayService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/basedata")
public class TipsayAction extends BaseAction {
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private TeachGroupService teachGroupService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ClassTeachingService classTeachingService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private ClassService classService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private TeachGroupExService teachGroupExService;
	@Autowired
	private TipsayService tipsayService;
	@Autowired
	private CustomRoleRemoteService customRoleRemoteService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private UserService userService;

	/***代课模块列表展现开始***/
	@RequestMapping("/tipsay/index/page")
	@ControllerInfo("代课管理")
	public String showTipsayIndex(String type,ModelMap map) {
		if(StringUtils.isBlank(type)) {
			type="1";
		}
		map.put("type", type);
		return "/basedata/tipsay/tipsayIndex.ftl";
	}
	
	@RequestMapping("/tipsay/tab/page")
	@ControllerInfo("tab")
	public String showTipsayTab(String type,ModelMap map) {
		map.put("type", type);
		map.put("isAdmin", isAdmin(getLoginInfo().getUnitId(), getLoginInfo().getUserId()));
		return "/basedata/tipsay/tipsayTab.ftl";
	}

	@RequestMapping("/tipsay/head/page")
	@ControllerInfo("代课头部")
	public String showTipsayHead(String acadyear, String semester,
			Integer week,String type, ModelMap map) {
		List<String> acadyearList = semesterService.findAcadeyearList();
		if (CollectionUtils.isEmpty(acadyearList)) {
			return errorFtl(map, "没有设置学年学期");
		}
		Semester semesterObj = null;
		if (StringUtils.isNotBlank(acadyear)
				&& StringUtils.isNotBlank(semester)) {
			semesterObj = new Semester();
			semesterObj.setAcadyear(acadyear);
			semesterObj.setSemester(Integer.parseInt(semester));
		} else {
			semesterObj = semesterService.getCurrentSemester(2);
		}
		
		
		List<Integer> weekList = new ArrayList<Integer>();
		int nowWeek = 1;
		if (semesterObj == null) {
			semesterObj = new Semester();
		} else {
			nowWeek = findBySemester(getLoginInfo().getUnitId(),
					semesterObj.getAcadyear(), semesterObj.getSemester(),
					weekList, new Date());
		}
		if (week != null) {
			nowWeek = week;
		}
		map.put("semesterObj", semesterObj);
		map.put("acadyearList", acadyearList);
		map.put("weekList", weekList);
		map.put("nowWeek", nowWeek);
		map.put("type", type);
		return "/basedata/tipsay/tipsayHead.ftl";
	}
	

	@ResponseBody
	@RequestMapping("/tipsay/findWeekList")
	@ControllerInfo("查找周次")
	public String findWeekList(String acadyear, String semester, ModelMap map) {
		List<Integer> weekList = new ArrayList<Integer>();
		findBySemester(getLoginInfo().getUnitId(),acadyear, Integer.parseInt(semester),weekList, new Date());
		return Json.toJSONString(weekList);
	}
	

	@RequestMapping("/tipsay/tipsayList/page")
	@ControllerInfo("代课列表")
	public String showTipsayList(String acadyear, String semester, String week,String type,
			ModelMap map) {
		Integer weekIndex=null;
		if(StringUtils.isNotBlank(week)){
			weekIndex=Integer.parseInt(week);
		}
		LoginInfo loginInfo = getLoginInfo();
		String unitId= loginInfo.getUnitId();
		List<TipsayDto> tipsayDtoList = new ArrayList<TipsayDto>();
		if("1".equals(type)) {
			//代课申请列表   teacherId:原老师 只有申请老师跟teacherId相同才有操作权限
 			String userId = loginInfo.getUserId();
			String teacherId=userService.findOne(userId)==null?"":userService.findOne(userId).getOwnerId();
			List<Tipsay> tipsayList = tipsayService.findTipsayListWithMaster(unitId, acadyear, Integer.parseInt(semester), weekIndex,teacherId);
			map.put("teacherId", teacherId);
			if (CollectionUtils.isNotEmpty(tipsayList)) {
				Map<String, String> weekDateMap = makeDateTime(unitId, acadyear,
						Integer.parseInt(semester), weekIndex);
 				tipsayDtoList = tipsayService.tipsayToTipsayDto(weekDateMap, tipsayList,false);
				
			}
			map.put("tipsayDtoList", tipsayDtoList);
			return "/basedata/tipsay/tipsaySelfList.ftl";
		}else {
			//代课管理列表
			List<Tipsay> tipsayList = tipsayService.findTipsayListWithMaster(unitId, acadyear, Integer.parseInt(semester), weekIndex,null);
			if (CollectionUtils.isNotEmpty(tipsayList)) {
				Map<String, String> weekDateMap = makeDateTime(unitId, acadyear,
						Integer.parseInt(semester), weekIndex);
 				tipsayDtoList = tipsayService.tipsayToTipsayDto(weekDateMap, tipsayList,false);
				//在删除的时候判断时间是否可以删除
			}
		}
		
		
		map.put("tipsayDtoList", tipsayDtoList);
		map.put("isAdmin", isAdmin(getLoginInfo().getUnitId(), getLoginInfo().getUserId()));
		return "/basedata/tipsay/tipsayList.ftl";
	}
	
	private Map<String, String> makeDateTime(String schoolId, String acadyear,
			Integer semester, Integer weekOfWorkTime) {
		List<DateInfo> dateList = dateInfoService.findByWeek(schoolId,
				acadyear, semester, weekOfWorkTime);
		Map<String, String> dateByWeek = new LinkedHashMap<String, String>();
		if (CollectionUtils.isNotEmpty(dateList)) {
			SimpleDateFormat f = new SimpleDateFormat("MM-dd");
			for (DateInfo info : dateList) {
				String tStr = f.format(info.getInfoDate());
				dateByWeek.put(info.getWeek() + "_" + (info.getWeekday() - 1),
						tStr);
			}
		}
		return dateByWeek;
	}
	/***代课模块列表展现结束***/
	
	
	@Deprecated
	@RequestMapping("/tipsay/addhead/page")
	@ControllerInfo("新增代课头部")
	public String addTipsayHead(String acadyear, String semester, Integer week,
			ModelMap map) {
		// 学年学期
		Semester chooseSemester = semesterService.findByAcadyearAndSemester(
				acadyear, Integer.parseInt(semester), getLoginInfo()
						.getUnitId());
		if (chooseSemester == null) {
			return errorFtl(map, "未找到当前学年学期");
		}
		map.put("oldWeek", week);
		List<Integer> weekList = new ArrayList<Integer>();

		int nowWeek = findBySemester(getLoginInfo().getUnitId(),
				chooseSemester.getAcadyear(), chooseSemester.getSemester(),
				weekList, new Date());
		if (CollectionUtils.isEmpty(weekList)) {
			return errorFtl(map, "当前学年学期未找到节假日设置");
		}
		if (week != null) {
			nowWeek = week;
		} else if (nowWeek == 0) {
			nowWeek = 1;
		}
		map.put("semesterObj", chooseSemester);
		map.put("weekList", weekList);
		map.put("nowWeek", nowWeek);
		map.put("type", "2");

		return "/basedata/tipsay/tipsayAddHead.ftl";
	}

	@Deprecated
	@RequestMapping("/tipsay/loadContent/page")
	@ControllerInfo("中心数据")
	public String loadContent(String acadyear, String semester, Integer week,
			ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		Semester chooseSemester = semesterService.findByAcadyearAndSemester(
				acadyear, Integer.parseInt(semester), unitId);
		if (chooseSemester == null) {
			return errorFtl(map, "未找到当前学年学期");
		}

		if (week == null) {
			return errorFtl(map, "请选择周次");
		}
		Integer days = chooseSemester.getEduDays();
		if(days==null) {
			days=7;
		}
		map.put("week", week);
		List<DateInfo> list = dateInfoService.findByWeek(getLoginInfo()
				.getUnitId(), chooseSemester.getAcadyear(), chooseSemester
				.getSemester(), week);
		if (CollectionUtils.isEmpty(list)) {
			return errorFtl(map, "对应周次的节假日没有设置，请选择其他周次");
		}
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		// 0,周一,2018-01-01
		List<String[]> weekDayList = new ArrayList<String[]>();
		for (DateInfo info : list) {
			String[] arr = new String[3];
			arr[0] = (info.getWeekday() - 1) + "";
			if (info.getWeekday() <= days && info.getWeekday() >= 1) {
				arr[1] = BaseConstants.dayOfWeekMap2.get(arr[0]);
			} else {
				continue;
//				arr[1] = "未知";
			}
			arr[2] = form.format(info.getInfoDate());

			weekDayList.add(arr);
		}
		map.put("weekDayList", weekDayList);
		// 1,2,3,4
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
			return errorFtl(map, "对应学年学期上课节次数没有设置，请先去设置");
		}
		map.put("mm", mm);
		map.put("am", am);
		map.put("pm", pm);
		map.put("nm", nm);

		// 教研组数据
		List<TeachGroupDto> groupDtoList = teachGroupService
				.findTeachers(getLoginInfo().getUnitId());
		map.put("groupDtoList", groupDtoList);

		return "/basedata/tipsay/tipsayAddContent.ftl";
	}


	private Integer findBySemester(String unitId, String acadyear,
			Integer semester, List<Integer> weekList, Date date) {
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
			;
			for (DateInfo info : dates) {
				weeks.add(info.getWeek());
				if (date != null) {
					String tStr = f.format(info.getInfoDate());
					if (dateStr.equals(tStr)) {
						nowWeek = info.getWeek();
					}
				}
			}

			CollectionUtils.addAll(weekList, weeks.iterator());
		}
		return nowWeek;
	}

	@ResponseBody
	@RequestMapping("/tipsay/scheduleByTeacherId")
	@ControllerInfo("查询需要教师一周课表")
	public String findWeekListBySemester(String acadyear, String semester,
			Integer week, String teacherId) {
		// 当周课程
		List<CourseSchedule> scheduleList = findCourseScheduleByTeacherId(
				acadyear, semester, week, teacherId);
		
		return Json.toJSONString(scheduleList);
	}
	
	
	@ResponseBody
	@RequestMapping("/tipsay/classByTeacherId")
	@ControllerInfo("查询需要教师某个学年学期下所教的班级")
	public String findClassByTeacher(String acadyear, String semester, String teacherId) {
		List<CourseSchedule> allList = courseScheduleService.findCourseScheduleListByTeacherId(acadyear, Integer.parseInt(semester), teacherId, null);
		Set<String> classIds = EntityUtils.getSet(allList, CourseSchedule::getClassId);
		Map<String, String> classMap=new LinkedHashMap<>();
		if(CollectionUtils.isNotEmpty(classIds)) {
			classMap = getClassNameMap(classIds);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("classMap", classMap);
		return jsonObject.toString();
	}
	
	private List<CourseSchedule> findCourseScheduleByTeacherId(String acadyear,
			String semester, Integer week, String teacherId) {
		//className,subjectName,placeName内容已经填充
		List<CourseSchedule> list = courseScheduleService
				.findCourseScheduleListByPerId(acadyear,
						Integer.parseInt(semester), week, teacherId, "1");
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<CourseSchedule>();
		}
//		Set<String> classIds = new HashSet<String>();
//		Set<String> subjectIds = new HashSet<String>();
//		Set<String> placeIds = new HashSet<String>();
//		for (CourseSchedule cc : list) {
//			classIds.add(cc.getClassId());
//			subjectIds.add(cc.getSubjectId());
//			if(StringUtils.isNotBlank(cc.getPlaceId())) {
//				placeIds.add(cc.getPlaceId());
//			}
//		}
//		Map<String, String> classNameMap = getClassNameMap(classIds);
//		List<Course> courseList = courseService.findListByIdIn(subjectIds
//				.toArray(new String[] {}));
//		Map<String, Course> courseNameMap = new HashMap<String, Course>();
//		if (CollectionUtils.isNotEmpty(courseList)) {
//			courseNameMap = EntityUtils.getMap(courseList, Course::getId);
//		}
//		Map<String, TeachPlace> placeNameMap = new HashMap<String, TeachPlace>();
//		if(CollectionUtils.isNotEmpty(placeIds)) {
//			List<TeachPlace> placeList = teachPlaceService.findListByIdIn(placeIds.toArray(new String[] {}));
//			if (CollectionUtils.isNotEmpty(placeList)) {
//				placeNameMap = EntityUtils.getMap(placeList, TeachPlace::getId);
//			}
//		}
//		
//		// 上午第1节-高一（1）班（物理） remark
//		if (CollectionUtils.isNotEmpty(list)) {
//			for (CourseSchedule c : list) {
//				if (classNameMap.containsKey(c.getClassId())) {
//					c.setClassName(classNameMap.get(c.getClassId()));
//				}
//				if (courseNameMap.containsKey(c.getSubjectId())) {
//					Course cs = courseNameMap.get(c.getSubjectId());
//					c.setSubjectName(cs.getSubjectName());
////					if (StringUtils.isNotEmpty(cs.getBgColor())) {
////						String[] bcs = cs.getBgColor().split(",");
////						if (StringUtils.isNotEmpty(bcs[0])) {
////							c.setBgColor(bcs[0]);
////						}
////						if (bcs.length > 1 && StringUtils.isNotEmpty(bcs[1])) {
////							c.setBorderColor(bcs[1]);
////						}
////					}
//					
//				}
//				if(StringUtils.isNotBlank(c.getPlaceId()) && placeNameMap.containsKey(c.getPlaceId())) {
//					c.setPlaceName(placeNameMap.get(c.getPlaceId()).getPlaceName());
//				}
//			}
//		}
		return list;
	}

	private Map<String, String> getClassNameMap(Set<String> classIds) {
		Map<String, String> classNameMap = new LinkedHashMap<>();
		if (CollectionUtils.isNotEmpty(classIds)) {
			String[] arr = classIds.toArray(new String[] {});
			List<Clazz> clazzList = classService.findClassListByIds(arr);
			if (CollectionUtils.isNotEmpty(clazzList)) {
				for (Clazz clazz : clazzList) {
					classNameMap
							.put(clazz.getId(), clazz.getClassNameDynamic());
				}
			}
			List<TeachClass> teachList = teachClassService.findListByIdIn(arr);
			if (CollectionUtils.isNotEmpty(teachList)) {
				for (TeachClass teachClass : teachList) {
					classNameMap.put(teachClass.getId(), teachClass.getName());
				}
			}
		}
		return classNameMap;
	}

	@RequestMapping("/tipsay/loadTeacherList")
	@ControllerInfo("查询教师")
	public String loadTeacherList(String acadyear, String semester, String type,
			String teacherId, String groupId, String teacherName,
			String classId, ModelMap map) {
		List<Teacher> teacherList = new ArrayList<Teacher>();
		if (StringUtils.isBlank(type)) {
			map.put("teacherList", teacherList);
			return "/basedata/tipsay/teacherList.ftl";
		}
		if ("1".equals(type) || "2".equals(type)) {
			Set<String> ids = new HashSet<String>();
			if ("1".equals(type)) {
				// 同教研组
				String[] groupIds = null;
				if (StringUtils.isNotBlank(groupId)) {
					groupIds = new String[] { groupId };
				} else {

				}
				if (groupIds != null) {
					List<TeachGroupEx> list = teachGroupExService
							.findByTeachGroupId(groupIds);
					if (CollectionUtils.isNotEmpty(list)) {
						ids.addAll(EntityUtils.getSet(list, TeachGroupEx::getTeacherId));
					}
				}
			} else {
				// 本班
				// 某个班级的任课老师--暂时班级课程计划部分获取
				List<ClassTeaching> classTeachingList = classTeachingService
						.findClassTeachingList(acadyear, semester,
								new String[] { classId }, getLoginInfo()
										.getUnitId(), 0, null, true);
				if (CollectionUtils.isNotEmpty(classTeachingList)) {
					for (ClassTeaching classTeaching : classTeachingList) {
						if (CollectionUtils.isNotEmpty(classTeaching
								.getTeacherIds())) {
							ids.addAll(classTeaching.getTeacherIds());
						}

					}
				}
				// 教学班
				TeachClass teachClass = teachClassService.findOne(classId);
				if (teachClass != null) {
					if (StringUtils.isNotBlank(teachClass.getTeacherId())) {
						ids.add(teachClass.getTeacherId());
					}

				}
			}
			if (CollectionUtils.isNotEmpty(ids)) {
				List<Teacher> allTecherList = teacherService.findListByIdIn(ids
						.toArray(new String[] {}));
				if (CollectionUtils.isNotEmpty(allTecherList)) {
					if (StringUtils.isNotBlank(teacherName)) {
						for (Teacher teacher : allTecherList) {
							if (teacher.getTeacherName().indexOf(teacherName) > -1) {
								teacherList.add(teacher);
							}
						}
					} else {
						teacherList.addAll(allTecherList);
					}

				}
			}
		} else {
			// 全校
			if (StringUtils.isBlank(teacherName)) {
				map.put("teacherList", teacherList);
				return "/basedata/tipsay/teacherList.ftl";
			}
			teacherList = teacherService.findListByTeacherName(getLoginInfo()
					.getUnitId(), teacherName);
		}
		//去除形相同老师teacherId
		List<Teacher> allTeacherList = new ArrayList<Teacher>();
		if(CollectionUtils.isNotEmpty(teacherList)){
			for(Teacher t:teacherList){
				if(!t.getId().equals(teacherId)){
					allTeacherList.add(t);
				}
			}
		}
		
		map.put("teacherList", allTeacherList);
		return "/basedata/tipsay/teacherList.ftl";
	}

	@ResponseBody
	@RequestMapping("/tipsay/saveTipsay")
	@ControllerInfo("保存")
	public String saveTipsay(Tipsay tipsay) {
		if (tipsay == null) {
			return error("数据丢失");
		}
		if (StringUtils.isBlank(tipsay.getCourseScheduleId())
				|| StringUtils.isBlank(tipsay.getNewTeacherId())) {
			return error("数据丢失");
		}
		

		CourseSchedule courseSchedule = courseScheduleService.findOne(tipsay
				.getCourseScheduleId());
		if (courseSchedule == null) {
			return error("对应课程数据不存在,请刷新后操作");
		}
		List<String> ttIds = new ArrayList<String>();
		ttIds.add(tipsay.getNewTeacherId());
		String error = tipsayService.checkTimeByTeacher(ttIds, courseSchedule);
		if (StringUtils.isNotBlank(error)) {
			return error("所选的代课老师在这节课已经有课");
		}

		List<CourseSchedule> ll = new ArrayList<CourseSchedule>();
		ll.add(courseSchedule);
		courseScheduleService.makeTeacherSet(ll);
		tipsay.setId(UuidUtils.generateUuid());
		tipsay.setAcadyear(courseSchedule.getAcadyear());
		tipsay.setSemester(courseSchedule.getSemester());
		tipsay.setWeekOfWorktime(courseSchedule.getWeekOfWorktime());
		tipsay.setDayOfWeek(courseSchedule.getDayOfWeek());
		tipsay.setPeriod(courseSchedule.getPeriod());
		tipsay.setPeriodInterval(courseSchedule.getPeriodInterval());
		tipsay.setClassId(courseSchedule.getClassId());
		tipsay.setClassType(courseSchedule.getClassType());
		tipsay.setSchoolId(courseSchedule.getSchoolId());
		tipsay.setSubjectId(courseSchedule.getSubjectId());
		tipsay.setTipsayType(TipsayConstants.TIPSAY_TYPE_01);
		tipsay.setType(TipsayConstants.TYPE_1);
		Set<String> set = courseSchedule.getTeacherIds();
		String exIds = "";
		if (CollectionUtils.isNotEmpty(set)) {
			for (String s : set) {
				if (!s.equals(courseSchedule.getTeacherId())) {
					exIds = exIds + "," + s;
				}
			}
		}

		if (StringUtils.isNotBlank(exIds)) {
			exIds = exIds.substring(1);
		}
		tipsay.setTeacherExIds(exIds);
		tipsay.setTeacherId(courseSchedule.getTeacherId());
		tipsay.setCreationTime(new Date());
		tipsay.setModifyTime(new Date());
		tipsay.setIsDeleted(TipsayConstants.IF_0);
		tipsay.setRemark(tipsay.getRemark());
		tipsay.setOperator(getLoginInfo().getOwnerId());
		tipsay.setState(TipsayConstants.TIPSAY_STATE_1);
		
		tipsay.setTipsayType(TipsayConstants.TIPSAY_TYPE_01);
		
		if(StringUtils.isBlank(tipsay.getType())) {
			tipsay.setType(TipsayConstants.TYPE_1);
		}
		
		
		// 目前默认管理员直接同意
		TipsayEx tipsayEx = new TipsayEx();
		tipsayEx.setAuditorId(getLoginInfo().getOwnerId());
		tipsayEx.setAuditorType(TipsayConstants.AUDITOR_TYPE_1);
		tipsayEx.setCreationTime(new Date());
		tipsayEx.setRemark("直接同意，生效");
		tipsayEx.setId(UuidUtils.generateUuid());
		tipsayEx.setState(TipsayConstants.TIPSAY_STATE_1);
		tipsayEx.setTipsayId(tipsay.getId());
		
		tipsayEx.setSchoolId(tipsay.getSchoolId());
		tipsayEx.setSourceType(TipsayConstants.TYPE_01);

		try {
			tipsayService.saveAll(tipsay, tipsayEx, courseSchedule);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败");
		}
		return success("保存成功");
	}

	
	
	@ResponseBody
	@RequestMapping("/tipsay/deleteTipsay")
	@ControllerInfo("撤销")
	public String deleteTipsay(String tipsayId) {
		Tipsay tipsay = tipsayService.findOne(tipsayId);
		if (tipsay == null || tipsay.getIsDeleted() == TipsayConstants.IF_1) {
			return error("代课记录不存在或者已经删除，请刷新后操作");
		}
		CourseSchedule courseSchedule = null;
		if (TipsayConstants.TIPSAY_STATE_1.equals(tipsay.getState())) {
			// 这个删除需要撤销原来课程
			courseSchedule = courseScheduleService.findOne(tipsay
					.getCourseScheduleId());
			if (courseSchedule == null) {
				return error("代课对应的课不存在");
			}
			String checkMess = tipsayService.checkCanDeleted(tipsay, courseSchedule);
			if (StringUtils.isNotBlank(checkMess)) {
				return error(checkMess);
			}
		}

		try {
			tipsayService.deleteTipsayOrSaveCourseSchedule(tipsay,
					courseSchedule);

		} catch (Exception e) {
			e.printStackTrace();
			return error( "删除失败!");
		}

		return success("删除成功!");
	}
	
	
	@RequestMapping("/lslecture/index/page2")
	@ControllerInfo("行政班调课--临时演示用")
	public String showLslectureIndex2(String clazz,ModelMap map) {
		if("1".equals(clazz)){
			return "/basedata/tipsay/lslectureIndex.ftl";
		}else if("2".equals(clazz)){
			return "/basedata/tipsay/lslectureIndex2.ftl";
		}else{
			return "/basedata/tipsay/lslectureIndex3.ftl";
		}
	}
	
	@RequestMapping("/lsvacation/index/page")
	@ControllerInfo("临时假期--临时演示用")
	public String showLsvacationIndex(ModelMap map) {
		return "/basedata/tipsay/lslectureIndex3.ftl";
	}
	/**
	 * 判断用户是不是教务管理员
	 * @param userId
	 * @return
	 */
	private boolean isAdmin(String unitId,String userId) {
		return customRoleRemoteService.checkUserRole(unitId, TipsayConstants.SUBSYSTEM_86, TipsayConstants.EDUCATION_CODE, userId);
	}
	
	@RequestMapping("/tipsay/addSelfTipsay2/page")
	@ControllerInfo("自主申请代课")
	public String addSelfTipsay2(String acadyear, String semester, Integer week,ModelMap map) {
		LoginInfo loginInfo = getLoginInfo();
		String userId=loginInfo.getUserId();
		String teacherId=userService.findOne(userId)==null?"":userService.findOne(userId).getOwnerId();
		if(StringUtils.isBlank(teacherId)) {
			return errorFtl(map, "该用户信息没有绑定教师");
		}
		return showFullPageIndex(acadyear, semester, week, loginInfo.getUnitId(), teacherId, map);
	}
	
	@RequestMapping("/tipsay/addTipsay/page")
	@ControllerInfo("管理员直接安排")
	public String addTipsayFull(String acadyear, String semester, Integer week,ModelMap map) {
		LoginInfo loginInfo = getLoginInfo();
		return showFullPageIndex(acadyear, semester, week, loginInfo.getUnitId(), null, map);
	}
	
	/**
	 * 全屏首页
	 * @param acadyear
	 * @param semester
	 * @param week
	 * @param unitId
	 * @param teacherId
	 * @param map
	 * @return
	 */
	public String showFullPageIndex(String acadyear, String semester, Integer week,String unitId,String teacherId,ModelMap map) {
		if(StringUtils.isNotBlank(teacherId)) {
			//自主申请代课
			map.put("showLeft", false);
			map.put("teacherId", teacherId);
			Teacher t = teacherService.findOne(teacherId);
			if(t!=null) {
				map.put("teacherName", t.getTeacherName());
			}
			
		}else {
			//教务安排
			map.put("showLeft", true);
			//取所有教研组
			List<TeachGroupDto> groupDtoList = teachGroupService
					.findTeachers(unitId);
			//获取教师列表 如果存在多个教研组 取一个  随机取一个
			Set<String> teacherIdSet=new HashSet<>();
			Map<String,String> teacherInGroupMap=new LinkedHashMap<>();
			if(CollectionUtils.isNotEmpty(groupDtoList)) {
				for(TeachGroupDto dto:groupDtoList) {
					if(CollectionUtils.isNotEmpty(dto.getMainTeacherList())) {
						for(TeacherDto dto1:dto.getMainTeacherList()) {
//							if(teacherInGroupMap.containsKey(dto.getTeachGroupId()+"_"+dto1.getTeacherId())) {
//								continue;
//							}
							if(teacherIdSet.contains(dto1.getTeacherId())) {
								continue;
							}
							teacherIdSet.add(dto1.getTeacherId());
							teacherInGroupMap.put(dto.getTeachGroupId()+"_"+dto1.getTeacherId(), dto1.getTeacherName());
						}
					}
				}
			}
			map.put("teacherInGroupMap", teacherInGroupMap);
			map.put("groupDtoList", groupDtoList);
		}
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		
		//空白课程表  以及头部数据
		return makeNoDataSchedule(unitId, acadyear, semester,week, map);
	}
	
	//空白课程表 以及头部数据
	public String makeNoDataSchedule(String unitId,String acadyear,String semester,Integer week,ModelMap map) {
		Semester chooseSemester = semesterService.findByAcadyearAndSemester(
				acadyear, Integer.parseInt(semester), unitId);
		if (chooseSemester == null) {
			return errorFtl(map, "未找到当前学年学期");
		}
		Integer days = chooseSemester.getEduDays();
		if(days==null) {
			days=7;
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
		List<Grade> gradeList = gradeService.findByUnitId(unitId);
		Integer mm = 0;
		Integer am = 0;
		Integer pm = 0;
		Integer nm = 0;
		for (Grade g : gradeList) {
        	if(g.getMornPeriods()!= null && g.getMornPeriods() > mm) {
        		mm = g.getMornPeriods();
        	}
            if (g.getAmLessonCount()!= null && g.getAmLessonCount() > am) {
                am = g.getAmLessonCount();
            }
            if (g.getPmLessonCount()!= null && g.getPmLessonCount() > pm) {
                pm = g.getPmLessonCount();
            }
            if (g.getNightLessonCount()!= null && g.getNightLessonCount() > nm) {
            	nm = g.getNightLessonCount();
            }
        }
		if (mm + am + pm + nm == 0) {
			return errorFtl(map, "对应学年学期上课节次数没有设置，请先去设置");
		}
		map.put("mm", mm);
		map.put("am", am);
		map.put("pm", pm);
		map.put("nm", nm);
		
		//周次列表
		List<Integer> weekList = new ArrayList<Integer>();

		int nowWeek = findBySemester(getLoginInfo().getUnitId(),
				chooseSemester.getAcadyear(), chooseSemester.getSemester(),
				weekList, new Date());
		if (CollectionUtils.isEmpty(weekList)) {
			return errorFtl(map, "当前学年学期未找到节假日设置");
		}
		
		//显示当前周以及后面周次
		List<Integer> newWeekList = new ArrayList<Integer>();
		for(Integer ii:weekList) {
			if(ii>=nowWeek) {
				newWeekList.add(ii);
			}
		}
		
		//没有传入周次 后面取默认周次
		if (week != null && week>nowWeek) {
			nowWeek = week;
		} else if (nowWeek == 0) {
			nowWeek = 1;
		}
		
		map.put("weekList", newWeekList);
		map.put("nowWeek", nowWeek);
		map.put("semesterObj", chooseSemester);
		
		
		//时间
		Date startTime = new Date();
		if(DateUtils.compareForDay(startTime, chooseSemester.getSemesterBegin())<0) {
			startTime=chooseSemester.getSemesterBegin();
		}
		Date endTime=new Date();
		if(DateUtils.compareForDay(chooseSemester.getSemesterEnd(),endTime)>0) {
			endTime=chooseSemester.getSemesterEnd();
		}
		Date afterWeekTime=DateUtils.addDay(startTime, 7);
		if(DateUtils.compareForDay(afterWeekTime,endTime)>0) {
			afterWeekTime=endTime;
		}
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("afterWeekTime", afterWeekTime);
		return "/basedata/tipsay/tipsayFullIndex.ftl";
	}
	
	
	

	@RequestMapping("/tipsay/loadTeacherList/page")
	@ControllerInfo("查询教师列表")
	public String loadTeacherListAll(String acadyear, String semester, String tabType,
			String teacherId,String groupId, String teacherName,
			String classIds, ModelMap map) {
		map.put("teacherList", findRightTeacherList(acadyear, semester, tabType, teacherId, groupId, teacherName, classIds));
		return "/basedata/tipsay/rightTeacherList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/tipsay/loadTeacherList/all")
	@ControllerInfo("查询教师列表")
	public String loadTeacherListAll2(String acadyear, String semester, String tabType,
			String teacherId, String teacherName,
			String scheduleIds, ModelMap map) {
		
		List<String> choseScheIds=new ArrayList<>();
		if(StringUtils.isNotBlank(scheduleIds)) {
			choseScheIds.addAll(Arrays.asList(scheduleIds.split(",")));
		}
		List<TipsayTeacher> teacherList = findRightTeacherList2(acadyear, semester, tabType, teacherId, null, teacherName, choseScheIds);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("teacherList", teacherList);
		return jsonObject.toString();
		
	}
	
	
	
	@RequestMapping("/tipsay/loadTeacherByTime/page")
	@ControllerInfo("查询教师某个时间段的课表列表")
	public String loadTeacherByTime(String acadyear, String semester, 
			String teacherId,String timeArea,String classId,  ModelMap map) {
		List<CourseSchedule> courseScheduleList = new ArrayList<CourseSchedule>();
		String unitId=getLoginInfo().getUnitId();
		if(StringUtils.isNotBlank(timeArea)) {
			if(timeArea.length()!=21) {
				return errorFtl(map, "时间参数不对");
			}
			String startTime = timeArea.substring(0, 10);
			String endTime = timeArea.substring(11, 21);
			//根据时间获取周次
			Date startDate = DateUtils.string2Date(startTime, "yyyy-MM-dd");
			Date endDate = DateUtils.string2Date(endTime, "yyyy-MM-dd");
			if(startDate==null || endDate==null) {
				return errorFtl(map, "时间参数不对");
			}
			if(DateUtils.compareForDay(startDate, endDate)>0) {
				return errorFtl(map, "开始时间不能大于结束时间");
			}
			//已经排序
			List<DateInfo> list = dateInfoService.findByDates(unitId, startDate, endDate);
			if(CollectionUtils.isEmpty(list)) {
				return errorFtl(map, "该范围时间节假日设置为空");
			}
			//weekday 星期一：1
			Map<String,DateInfo> dateInfoByWeekdayMap=new HashMap<>();
			//时间中最小周次
			Integer minWeek = list.get(0).getWeek();
			map.put("minWeek", minWeek);
			//范围 
			courseScheduleList=courseScheduleService.findCourseScheduleListByPerId(acadyear, Integer.parseInt(semester), startTime.replace("-", ""), endTime.replace("-", ""), teacherId, "1");
			
			for(DateInfo d:list) {
				String weekDayStr=d.getWeek()+"_"+(d.getWeekday()-1);
				dateInfoByWeekdayMap.put(weekDayStr,d);
			}
			Map<String, String> dayOfWeekMap2 = BaseConstants.dayOfWeekMap2;
			Map<String, String> periodMap = BaseConstants.PERIOD_INTERVAL_Map;
			if(CollectionUtils.isNotEmpty(courseScheduleList)) {
				if(StringUtils.isNotBlank(classId)) {
					courseScheduleList = courseScheduleList.stream().filter(e->classId.equals(e.getClassId())).collect(Collectors.toList());
				}
				Collections.sort(courseScheduleList,new Comparator<CourseSchedule>() {

					@Override
					public int compare(CourseSchedule o1, CourseSchedule o2) {
						if(o1.getWeekOfWorktime()==o2.getWeekOfWorktime()) {
							if(o1.getDayOfWeek()==o2.getDayOfWeek()) {
								if(o1.getPeriodInterval().equals(o2.getPeriodInterval())) {
									if(o1.getClassType()==o2.getClassType()) {
										return o2.getClassId().compareTo(o1.getClassId());
									}
									return o1.getClassType()-o2.getClassType();
								}
								return o2.getPeriodInterval().compareTo(o1.getPeriodInterval());
								
								
							}else {
								return o1.getDayOfWeek()-o2.getDayOfWeek();
							}
							
						}
						
						return o1.getWeekOfWorktime()-o2.getWeekOfWorktime();
					}
				});
				
				for(CourseSchedule cc:courseScheduleList) {
					String timeStr="";
					String key=cc.getWeekOfWorktime()+"_"+cc.getDayOfWeek();
					if(dateInfoByWeekdayMap.containsKey(key)) {
						timeStr=DateUtils.date2StringByDay(dateInfoByWeekdayMap.get(key).getInfoDate());
					}
					timeStr=timeStr+"("+dayOfWeekMap2.get(cc.getDayOfWeek().toString())+")";
					cc.setDayTimeStr(timeStr);
					String periodTimeStr=periodMap.get(cc.getPeriodInterval())+"第"+cc.getPeriod()+"节";
					cc.setPeriodTimeStr(periodTimeStr);
				}
				
			}
		}
		
		
		map.put("courseScheduleList", courseScheduleList);
		return "/basedata/tipsay/teacherScheduleList.ftl"; 
	}
	
	/**
	 * 保存多条申请记录
	 * @param tipsayScheduleDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/tipsay/saveTipsay1")
	@ControllerInfo("保存")
	public String saveTipsay1(TipsayScheduleDto tipsayScheduleDto) {
		if (tipsayScheduleDto == null) {
			return error("数据丢失");
		}
		if(StringUtils.isBlank(tipsayScheduleDto.getApplyType())) {
			return error("数据丢失");
		}
		if (StringUtils.isBlank(tipsayScheduleDto.getCourseScheduleIds())
				|| StringUtils.isBlank(tipsayScheduleDto.getNewTeacherId())
				|| StringUtils.isBlank(tipsayScheduleDto.getOldTeacherId()) ) {
			return error("数据丢失");
		}
		
		String courseScheduleIds = tipsayScheduleDto.getCourseScheduleIds();
		String[] courseScheduleIdsArr = courseScheduleIds.split(",");
		List<CourseSchedule> scheduleList = courseScheduleService.findListByIds(courseScheduleIdsArr);
		if(CollectionUtils.isEmpty(scheduleList)) {
			return error("对应课程数据不存在,请刷新后操作");
		}
		if(scheduleList.size()!=courseScheduleIdsArr.length) {
			return error("课程数据有调整，请重新操作");
		}
		//scheduleList 是不是原老师有没有改变
		courseScheduleService.makeTeacherSet(scheduleList);
		int minWorkTime=0;
		int maxWorkTime=0;
		for(CourseSchedule c:scheduleList) {
			if(!(c.getAcadyear().equals(tipsayScheduleDto.getAcadyear()) && String.valueOf(c.getSemester()).equals(tipsayScheduleDto.getSemester()))) {
				return error("课程数据有调整，请重新操作");
			}
			if(CollectionUtils.isEmpty(c.getTeacherIds())) {
				return error("课程数据有调整，请重新操作");
			}
			if(!c.getTeacherIds().contains(tipsayScheduleDto.getOldTeacherId())) {
				return error("课程数据有调整，请重新操作");
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
		String error = tipsayService.checkTimesByTeacher(tipsayScheduleDto.getAcadyear(),tipsayScheduleDto.getSemester(),minWorkTime,maxWorkTime,tipsayScheduleDto.getNewTeacherId(), scheduleList);
		if (StringUtils.isNotBlank(error)) {
			return error("代课/管课老师存在时间冲突");
		}
		List<Tipsay> tipsayList=new ArrayList<>();
		List<TipsayEx> tipsayExList=new ArrayList<>();
		Tipsay tipsay;
		if(TipsayConstants.AUDITOR_TYPE_1.equals(tipsayScheduleDto.getApplyType())) {
			//管理员直接审核通过
			for(CourseSchedule c:scheduleList) {
				tipsay=makeNewTipsay(c);
				tipsay.setRemark(tipsayScheduleDto.getRemark());
				tipsay.setOperator(getLoginInfo().getOwnerId());
				tipsay.setType(tipsayScheduleDto.getType());
				tipsay.setNewTeacherId(tipsayScheduleDto.getNewTeacherId());
				
				tipsay.setState(TipsayConstants.TIPSAY_STATE_1);
				tipsay.setTipsayType(TipsayConstants.TIPSAY_TYPE_01);
				tipsayList.add(tipsay);
				TipsayEx tipsayEx = new TipsayEx();
				tipsayEx.setAuditorId(getLoginInfo().getOwnerId());
				tipsayEx.setAuditorType(TipsayConstants.AUDITOR_TYPE_1);
				tipsayEx.setCreationTime(new Date());
				tipsayEx.setRemark("直接同意，生效");
				tipsayEx.setId(UuidUtils.generateUuid());
				tipsayEx.setState(TipsayConstants.TIPSAY_STATE_1);
				tipsayEx.setTipsayId(tipsay.getId());
				
				tipsayEx.setSchoolId(tipsay.getSchoolId());
				tipsayEx.setSourceType(TipsayConstants.TYPE_01);
				tipsayExList.add(tipsayEx);
				
				c.setTeacherId(tipsayScheduleDto.getNewTeacherId());
			}
		}else {
			//申请
			for(CourseSchedule c:scheduleList) {
				tipsay=makeNewTipsay(c);
				tipsay.setRemark(tipsayScheduleDto.getRemark());
				tipsay.setOperator(getLoginInfo().getOwnerId());
				tipsay.setState(TipsayConstants.TIPSAY_STATE_0);
				tipsay.setNewTeacherId(tipsayScheduleDto.getNewTeacherId());
				tipsay.setTipsayType(TipsayConstants.TIPSAY_TYPE_02);
				tipsay.setType(tipsayScheduleDto.getType());
				tipsayList.add(tipsay);
			}
		}
		
		try {
			tipsayService.saveAllApplyList(getLoginInfo().getUnitId(),tipsayList, tipsayExList, scheduleList,tipsayScheduleDto.getNewTeacherId(),tipsayScheduleDto.getOldTeacherId(),tipsayScheduleDto.getApplyType());
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败");
		}
		return success("保存成功");
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
	
	
	/**
	 * 申请教务安排----管理员安排
	 */
	
	@RequestMapping("/tipsay/arrangeSelfTipsay/page")
	@ControllerInfo("教师代课申请安排老师")
	public String arrangeSelfTipsay(String tipsayId, ModelMap map) {
		Tipsay tipsay = tipsayService.findOne(tipsayId);
	    if(tipsay==null){
	    	return errorFtl(map, "数据不存在");
		}
	    CourseSchedule courseschedule = courseScheduleService.findOne(tipsay.getCourseScheduleId());
	    if(courseschedule==null) {
	    	return errorFtl(map, "数据不存在");
	    }
	    //判断是不是原来的课程
	    Teacher teacher = teacherService.findOne(tipsay.getOperator());
	    String teacherName = "";
	    //原来老师
	    if(teacher!=null) {
	    	teacherName=teacher.getTeacherName();
	    }
	    //填充 
	    if(StringUtils.isNotBlank(courseschedule.getSubjectId())){
	    	 Course course = courseService.findOne(courseschedule.getSubjectId());
	    	 if(course!=null) {
	    		 courseschedule.setSubjectName(course.getSubjectName());
	    	 }
	    }
	    if(StringUtils.isNotBlank(courseschedule.getClassId())) {
	    	Set<String> classIds=new HashSet<>();
	    	classIds.add(courseschedule.getClassId());
	    	Map<String, String> map1 = getClassNameMap(classIds);
	    	if(map1.size()>0) {
	    		courseschedule.setClassName(map1.get(courseschedule.getClassId()));
	    	}
	    }
	   
	    map.put("teacherName", teacherName);
	    map.put("tipsay", tipsay);
		map.put("courseschedule", courseschedule);
		
		Semester chooseSemester = semesterService.findByAcadyearAndSemester(
				courseschedule.getAcadyear(), courseschedule.getSemester(), courseschedule.getSchoolId());
		if (chooseSemester == null) {
			return errorFtl(map, "未找到当前学年学期");
		}
		
		Integer days = chooseSemester.getEduDays();
		if(days==null) {
			days=7;
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
		List<Grade> gradeList = gradeService.findByUnitId(courseschedule.getSchoolId());
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
			return errorFtl(map, "对应学年学期上课节次数没有设置，请先去设置");
		}
		map.put("mm", mm);
		map.put("am", am);
		map.put("pm", pm);
		map.put("nm", nm);
		
		map.put("semesterObj", chooseSemester);
		
		return "/basedata/tipsay/tipsayFullContentOne.ftl";
		
	}
	
	@RequestMapping("/tipsay/loadTeacherList2/page")
	@ControllerInfo("管理员只安排代课老师查询教师列表")
	public String loadTeacherListAll2(String acadyear, String semester, String tabType,
			String teacherId,String groupId, String teacherName,
			String classIds,String type, ModelMap map) {
		map.put("type", type);
		map.put("teacherList", findRightTeacherList(acadyear, semester, tabType, teacherId, groupId, teacherName, classIds));
		return "/basedata/tipsay/rightTeacherList2.ftl";
	}
	
	//times  周次_星期_上下午_节次
	public List<TipsayTeacher> findRightTeacherList2(String acadyear, String semester,String tabType,
			String teacherId,String groupId, String teacherName,
			List<String> choseScheIds){
		List<TipsayTeacher> ttList=new ArrayList<>();
		String[] times=null;
		String classIds="";
		if(CollectionUtils.isNotEmpty(choseScheIds)) {
			List<CourseSchedule> courseScheList = courseScheduleService.findListByIdIn(choseScheIds.toArray(new String[] {}));
			if(CollectionUtils.isNotEmpty(courseScheList)) {
				Set<String> timeArr=new HashSet<>();
				Set<String> classIdList=new HashSet<>();
				for(CourseSchedule v:courseScheList) {
					timeArr.add(v.getWeekOfWorktime()+"_"+v.getDayOfWeek()+"_"+v.getPeriodInterval()+"_"+v.getPeriod());
					classIdList.add(v.getClassId());
				}
				if(CollectionUtils.isNotEmpty(classIdList)) {
					classIds=ArrayUtil.print(classIdList.toArray(new String[0]));
				}
				if(CollectionUtils.isNotEmpty(timeArr)) {
					times=timeArr.toArray(new String[0]);
				}
			}
		}
		
		List<Teacher> list = findRightTeacherList(acadyear, semester, tabType, teacherId, groupId, teacherName, classIds);
		Set<String> tIds=new HashSet<>();
		
		if(CollectionUtils.isNotEmpty(list)) {
			CourseScheduleDto dto=new CourseScheduleDto();
			dto.setSchoolId(getLoginInfo().getUnitId());
			dto.setAcadyear(acadyear);
			dto.setSemester(Integer.parseInt(semester));
			List<CourseSchedule> clist=courseScheduleService.findByWeekTimes(dto,times);
			courseScheduleService.makeTeacherSet(clist);
			//暂时不考虑班级所在的班级
			for(CourseSchedule c:clist) {
				if(choseScheIds.contains(c.getId())) {
					continue;
				}
				if(CollectionUtils.isEmpty(c.getTeacherIds())) {
					continue;
				}
				tIds.addAll(c.getTeacherIds());
			}
			
		}else {
			return ttList;
		}
		TipsayTeacher tt;
		for(Teacher t:list) {
			if(tIds.contains(t.getId())) {
				tt=new TipsayTeacher(t.getTeacherName(), t.getId(), t.getTeacherCode(), "1");
			}else {
				tt=new TipsayTeacher(t.getTeacherName(), t.getId(), t.getTeacherCode(), "0");
			}
			ttList.add(tt);
		}
		return ttList;
	}
	
	public class TipsayTeacher implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String teacherName;
		private String teacherId;
		private String teacherCode;
		private String error;
		
		public TipsayTeacher() {
			
		}
		public TipsayTeacher(String teacherName, String teacherId, String teacherCode, String error) {
			this.teacherName = teacherName;
			this.teacherId = teacherId;
			this.teacherCode = teacherCode;
			this.error = error;
		}
		public String getTeacherName() {
			return teacherName;
		}
		public void setTeacherName(String teacherName) {
			this.teacherName = teacherName;
		}
		public String getTeacherId() {
			return teacherId;
		}
		public void setTeacherId(String teacherId) {
			this.teacherId = teacherId;
		}
		public String getTeacherCode() {
			return teacherCode;
		}
		public void setTeacherCode(String teacherCode) {
			this.teacherCode = teacherCode;
		}
		public String getError() {
			return error;
		}
		public void setError(String error) {
			this.error = error;
		}
		
	}
	
	public List<Teacher> findRightTeacherList(String acadyear, String semester, String tabType,
			String teacherId,String groupId, String teacherName,
			String classIds){
		List<Teacher> teacherList = new ArrayList<Teacher>();
		if (StringUtils.isBlank(tabType)) {
			return teacherList;
		}
		if("1".equals(tabType) || "2".equals(tabType)) {
			Set<String> teacherIds=new HashSet<>();
			//1:同教研组  2:同班老师
			if("1".equals(tabType)) {
				Set<String> groupIds = new HashSet<String>();
				if(StringUtils.isNotBlank(groupId)) {
					groupIds.add(groupId);
				}else {
					//该教师所在的教研组对应的所有老师
					List<TeachGroupEx> exList=teachGroupExService.findListBy("teacherId", teacherId);
					if(CollectionUtils.isNotEmpty(exList)) {
						groupIds.addAll(EntityUtils.getSet(exList, TeachGroupEx::getTeachGroupId));
					}
				}
				if(CollectionUtils.isNotEmpty(groupIds)) {
					List<TeachGroupEx> list = teachGroupExService
							.findByTeachGroupId(groupIds.toArray(new String[] {}));
					if (CollectionUtils.isNotEmpty(list)) {
						teacherIds.addAll(EntityUtils.getSet(list, TeachGroupEx::getTeacherId));
					}
				}
				
			}else if("2".equals(tabType)) {
				//同班
				if(StringUtils.isNotBlank(classIds)) {
					String[] classIdArr = classIds.split(",");
					// 本班
					// 某个班级的任课老师--暂时班级课程计划部分获取
					List<ClassTeaching> classTeachingList = classTeachingService
							.findClassTeachingList(acadyear, semester,
									classIdArr, getLoginInfo()
											.getUnitId(), 0, null, true);
					if (CollectionUtils.isNotEmpty(classTeachingList)) {
						for (ClassTeaching classTeaching : classTeachingList) {
							if (CollectionUtils.isNotEmpty(classTeaching
									.getTeacherIds()) || StringUtils.isNotBlank(classTeaching.getTeacherId())) {
								if(CollectionUtils.isNotEmpty(classTeaching
										.getTeacherIds())) {
									teacherIds.addAll(classTeaching.getTeacherIds());
								}
								if(StringUtils.isNotBlank(classTeaching.getTeacherId())) {
									teacherIds.add(classTeaching.getTeacherId());
								}
								
							}

						}
					}
					// 教学班
					List<TeachClass> teachClassList = teachClassService.findListByIdIn(classIdArr);
					if (CollectionUtils.isNotEmpty(teachClassList)) {
						teacherIds.addAll(EntityUtils.getSet(teachClassList, e->e.getTeacherId()));
					}
				}
			}
			if (CollectionUtils.isNotEmpty(teacherIds)) {
				List<Teacher> allTecherList = teacherService.findListByIdIn(teacherIds
						.toArray(new String[] {}));
				if (CollectionUtils.isNotEmpty(allTecherList)) {
					if (StringUtils.isNotBlank(teacherName)) {
						for (Teacher teacher : allTecherList) {
							if (teacher.getTeacherName().indexOf(teacherName) > -1) {
								teacherList.add(teacher);
							}
						}
					} else {
						teacherList.addAll(allTecherList);
					}

				}
			}
		}else if("3".equals(tabType)) {
			if (StringUtils.isBlank(teacherName)) {
				return teacherList;
			}
			teacherList = teacherService.findListByTeacherName(getLoginInfo()
					.getUnitId(), teacherName);
		}else {
			return teacherList;
		}

		//去除形相同老师teacherId
		List<Teacher> allTeacherList = new ArrayList<Teacher>();
		if(CollectionUtils.isNotEmpty(teacherList)){
			for(Teacher t:teacherList){
				if(!t.getId().equals(teacherId)){
					allTeacherList.add(t);
				}
			}
		}
		return allTeacherList;
	}
	
	@ResponseBody
	@RequestMapping("/tipsay/saveTipsay2")
	@ControllerInfo("管理员安排代课老师保存")
	public String saveTipsay2(TipsayScheduleDto tipsayScheduleDto) {
		if (tipsayScheduleDto == null) {
			return error("数据丢失");
		}
		if (StringUtils.isBlank(tipsayScheduleDto.getTipsayId())
				|| StringUtils.isBlank(tipsayScheduleDto.getNewTeacherId())) {
			return error("数据丢失");
		}
		Tipsay tipsay = tipsayService.findOne(tipsayScheduleDto.getTipsayId());
		if(tipsay==null) {
			return error("需要安排代课老师的申请数据已不存在");
		}
		//判断是不是原来的
		CourseSchedule courseSchedule = courseScheduleService.findOne(tipsay.getCourseScheduleId());
		if (courseSchedule == null) {
			return error("对应课程数据不存在,请重新操作");
		}
		
		String errorMes = checkReturnError(tipsay, courseSchedule);
		if(StringUtils.isNotBlank(errorMes)) {
			return error(errorMes);
		}
		if(courseSchedule.getTeacherIds().contains(tipsayScheduleDto.getNewTeacherId())) {
			return error("该课程本来就是由该老师上课，无需代课");
		}
		List<String> tList=new ArrayList<>();
		tList.add(tipsayScheduleDto.getNewTeacherId());
		String error =tipsayService.checkTimeByTeacher(tList, courseSchedule);
		if (StringUtils.isNotBlank(error)) {
			return error("代课/管课老师存在时间冲突");
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
		tipsay.setNewTeacherId(tipsayScheduleDto.getNewTeacherId());
		tipsay.setModifyTime(new Date());
		tipsay.setState(TipsayConstants.TIPSAY_STATE_1);
		
		TipsayEx tipsayEx = new TipsayEx();
		tipsayEx.setAuditorId(getLoginInfo().getOwnerId());
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
			return error("保存失败");
		}
		return success("保存成功");
	}
	
	
	@ResponseBody
	@RequestMapping("/tipsay/checkTipsay")
	@ControllerInfo("管理员安排代课老师前，先验证数据")
	public String checkTipsay(String tipsayId) {
		Tipsay tipsay = tipsayService.findOne(tipsayId);
		if (tipsay == null || tipsay.getIsDeleted() == TipsayConstants.IF_1) {
			return error("代课记录不存在或者已经删除，请刷新后操作");
		}
		//判断是不是原来的
		CourseSchedule courseSchedule = courseScheduleService.findOne(tipsay
				.getCourseScheduleId());
		if (courseSchedule == null) {
			return error("对应课程数据不存在,无法操作");
		}
		String errorMes = checkReturnError(tipsay, courseSchedule);
		
		if(StringUtils.isNotBlank(errorMes)) {
			return error(errorMes);
		}
		return success("验证成功!");
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
    @ResponseBody
	@RequestMapping("/tipsay/arrangeYesOrNo")
	@ControllerInfo("管理员审核")
	public String arrangeYesOrNo(String tipsayId,String state) {
		Tipsay tipsay = tipsayService.findOne(tipsayId);
		if (tipsay == null || tipsay.getIsDeleted() == TipsayConstants.IF_1) {
			return error("代课记录不存在或者已经删除，请刷新后操作");
		}
		if(!TipsayConstants.TIPSAY_STATE_0.equals(tipsay.getState())) {
			return error("代课记录已调整，请刷新后操作");
		}
		
		//判断是不是原来的
		CourseSchedule courseSchedule = courseScheduleService.findOne(tipsay
				.getCourseScheduleId());
		if (courseSchedule == null) {
			return error("对应课程数据不存在,无法操作");
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
			return error("在节假日设置未找到对应代课时间");
		}
		if(DateUtils.compareForDay(nowDate, chooseDate) >=0){
			return error("时间已经过去，不能操作");
		}
		
		String errorMes = checkReturnError(tipsay, courseSchedule);
		//判断教师在这个时间有没有课
		List<String> tList=new ArrayList<>();
		tList.add(tipsay.getNewTeacherId());
		String error =tipsayService.checkTimeByTeacher(tList, courseSchedule);
		if (StringUtils.isNotBlank(error)) {
			return error("代课/管课老师存在时间冲突");
		}
		TipsayEx tipsayEx = new TipsayEx();
		tipsayEx.setAuditorId(getLoginInfo().getOwnerId());
		tipsayEx.setAuditorType(TipsayConstants.AUDITOR_TYPE_1);
		tipsayEx.setCreationTime(new Date());
		
		tipsayEx.setId(UuidUtils.generateUuid());
		tipsayEx.setTipsayId(tipsay.getId());
		tipsayEx.setSchoolId(tipsay.getSchoolId());
		tipsayEx.setSourceType(TipsayConstants.TYPE_01);
		if(StringUtils.isNotBlank(errorMes)) {
			return error(errorMes);
		}
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
			return error("参数错误");
		}
		try {
			tipsayService.saveAll(tipsay, tipsayEx, courseSchedule);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败");
		}
		return success("保存成功");
	}
    
}
