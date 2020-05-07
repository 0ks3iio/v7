package net.zdsoft.basedata.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.constant.TipsayConstants;
import net.zdsoft.basedata.dto.TeachGroupDto;
import net.zdsoft.basedata.dto.TipsayDto;
import net.zdsoft.basedata.dto.TipsaySaveDto;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Tipsay;
import net.zdsoft.basedata.entity.TipsayEx;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.TeachGroupService;
import net.zdsoft.basedata.service.TipsayService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.WeiKeyUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * 手机端代课
 * 
 * @author zhouyz 代课对于辅助教师 一代代所有教师 一撤撤所有教师
 */
@Controller
@RequestMapping("/mobile/open/tipsay")
public class TipsayH5Action {
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private TipsayService tipsayService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private TeachGroupService teachGroupService;

	@Autowired
	private CustomRoleRemoteService customRoleRemoteService;
	/**
	 * 判断用户是不是教务管理员
	 * @param userId
	 * @return
	 */
	private boolean isAdmin(String unitId,String userId) {
		return customRoleRemoteService.checkUserRole(unitId, TipsayConstants.SUBSYSTEM_86, TipsayConstants.EDUCATION_CODE, userId);
	}
	
	@RequestMapping("/index/page")
	@ControllerInfo("代课首页")
	public String showTipsayIndex(String token, ModelMap map) {
		User user = null;
		String ownerId = "";
//		ownerId="74ED18C2BFAE4FA282144951903AEC0B";
//		ownerId="402896C04CFEBA55014CFEDB8F8600C9";
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
//		user=SUtils.dc(userRemoteService.findOneById("485DC6D1B44E4615ABB5081F9ED6AB1E"), User.class);
		boolean isAdmin=isAdmin(user.getUnitId(), user.getId());
		String adminType="0";
		if(isAdmin) {
			adminType="1";
		}
		return showTipsayHead(user.getUnitId(), "","","", user.getOwnerId(),adminType, map);
//		return showTipsayHead("55D786A7B03F4D279697ABB214BCF2DB", "","","", "3DD772EB68FB4A02A0C70D3F325A6B8B", map);
	}

	@RequestMapping("/head/page")
	@ControllerInfo("代课头部")
	public String showTipsayHead(String unitId, String acadyear,
			String semester, String week, String teacherId,String adminType, ModelMap map) {
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
		if (semesterObj == null) {
			semesterObj = new Semester();
			if (StringUtils.isNotBlank(week)) {
				nowWeek = Integer.parseInt(week);
			}
		} else {
			// 一共周次数
			if (StringUtils.isNotBlank(week)) {
				nowWeek = Integer.parseInt(week);
				findBySemester(unitId, semesterObj.getAcadyear(),
						semesterObj.getSemester(), weekList, new Date());
			} else {
				nowWeek = findBySemester(unitId, semesterObj.getAcadyear(),
						semesterObj.getSemester(), weekList, new Date());
			}

		}
		map.put("semesterObj", semesterObj);
		map.put("weekList", weekList);
		map.put("nowWeek", nowWeek);
		map.put("acadyearList", acadyearList);
		map.put("unitId", unitId);
		map.put("teacherId", teacherId);
		map.put("adminType", adminType);
		return "/basedata/tipsayh5/tipsayHead.ftl";
	}

	@RequestMapping("/list/page")
	@ControllerInfo("代课列表")
	public String showTipsayList(String unitId, String acadyear,
			String semester, String week, String teacherId,String adminType, ModelMap map) {
		List<TipsayDto> tipsayDtoList = new ArrayList<TipsayDto>();
		if (StringUtils.isBlank(week)) {
			map.put("tipsayDtoList", tipsayDtoList);
			return "/basedata/tipsayh5/tipsayList.ftl";
		}
		List<Tipsay> tipsayList = tipsayService.findTipsayListWithMaster(
				unitId, acadyear, Integer.parseInt(semester),
				Integer.parseInt(week),null);
		Date newDate = new Date();
		DateInfo date = dateInfoService.getDate(unitId, acadyear,
				Integer.parseInt(semester), newDate);
		Map<String, String> weekDateMap = makeDateTime(unitId, acadyear,
				Integer.parseInt(semester), Integer.parseInt(week));
		tipsayDtoList = tipsayService.tipsayToTipsayDto(weekDateMap, tipsayList,true);
		if(CollectionUtils.isNotEmpty(tipsayDtoList)){
			for(TipsayDto dto:tipsayDtoList){
				if (TipsayConstants.TIPSAY_STATE_1.equals(dto.getTipsay().getState())) {
					if (date != null) {
						if (dto.getTipsay().getWeekOfWorktime() > date.getWeek()
								|| (dto.getTipsay().getWeekOfWorktime() == date.getWeek() && dto.getTipsay()
										.getDayOfWeek() + 1 > date.getWeekday())) {
							dto.setCanDeleted(true);
						} else {
							
							dto.setCanDeleted(false);
						}
					} else {
						// 暂时没有找到这个时间 不能删除 ----这个地方存在这个时间不在这个学年学期内
						dto.setCanDeleted(false);
					}
				} else {
					dto.setCanDeleted(true);
				}
			}
			
		}
		map.put("adminType", adminType);
		map.put("tipsayDtoList", tipsayDtoList);
		return "/basedata/tipsayh5/tipsayList.ftl";
	}

	@RequestMapping("/addTipsay/page")
	@ControllerInfo("新增代课")
	public String addTipsay(String unitId, String acadyear, int semester,
			String week, String teacherId,String adminType, ModelMap map) {
		// 所有教师列表
		List<TeachGroupDto> groupDtoList = teachGroupService
				.findTeachers(unitId);
		map.put("groupDtoList", groupDtoList);
		// 时间范围
		Semester semesterObj = semesterService.findByAcadyearAndSemester(
				acadyear, semester, unitId);
		if (semesterObj == null) {
			map.put("nonemess", "请确认学年学期设置是否正确。");
			return "/basedata/tipsayh5/none.ftl";
		}
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date beginTime = semesterObj.getSemesterBegin();
		Date endTime = semesterObj.getSemesterEnd();
		Date nowDate = new Date();
		// 判断开始时间
		if (DateUtils.compareForDay(beginTime, nowDate) < 0) {
			// nowDate>beginTime
			beginTime = nowDate;
		} else if (DateUtils.compareForDay(nowDate, endTime) > 0) {
			// endTime>nowDate
			map.put("nonemess", "学年学期已经过去，不能代课。");
			return "/basedata/tipsayh5/none.ftl";
		}

		String[] beginTimeArr = f.format(beginTime).split("-");
		String[] endTimeArr = f.format(endTime).split("-");

		map.put("beginYear", Integer.parseInt(beginTimeArr[0]));
		map.put("endYear", Integer.parseInt(endTimeArr[0]));
		map.put("beginMonth", Integer.parseInt(beginTimeArr[1]));
		map.put("endMonth", Integer.parseInt(endTimeArr[1]));
		map.put("beginDay", Integer.parseInt(beginTimeArr[2]));
		map.put("endDay", Integer.parseInt(endTimeArr[2]));

		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("week", week);
		map.put("unitId", unitId);
		map.put("teacherId", teacherId);
		// 默认登陆老师
		map.put("adminType", adminType);
		return "/basedata/tipsayh5/tipsayAdd.ftl";
	}

	@ResponseBody
	@RequestMapping("/findBySemester")
	@ControllerInfo("查询周次")
	public String findWeekListBySemester(String unitId, String acadyear,
			String semester) {
		List<Integer> weekList = new ArrayList<Integer>();
		findBySemester(unitId, acadyear, Integer.parseInt(semester), weekList,
				null);
		return Json.toJSONString(weekList);
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
	@RequestMapping("/deleteTipsay")
	@ControllerInfo("撤销")
	public String deleteTipsay(String tipsayId) {
		Tipsay tipsay = tipsayService.findOne(tipsayId);
		if (tipsay == null || tipsay.getIsDeleted() == TipsayConstants.IF_1) {
			return resultMess(false, "代课记录不存在或者已经删除，请刷新后操作");
		}
		CourseSchedule courseSchedule = null;
		if (TipsayConstants.TIPSAY_STATE_1.equals(tipsay.getState())) {
			// 这个删除需要撤销原来课程
			courseSchedule = courseScheduleService.findOne(tipsay
					.getCourseScheduleId());
			if (courseSchedule == null) {
				return resultMess(false, "代课对应的课不存在");
			}
			String checkMess = tipsayService.checkCanDeleted(tipsay, courseSchedule);
			if (StringUtils.isNotBlank(checkMess)) {
				return resultMess(false, checkMess);
			}
		}

		try {
			tipsayService.deleteTipsayOrSaveCourseSchedule(tipsay,
					courseSchedule);

		} catch (Exception e) {
			e.printStackTrace();
			return resultMess(false, "撤销失败!");
		}

		return resultMess(true, "操作成功!");
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

	private String makeTimeStr(String periodInterval, int period, String dateStr) {
		String returnStr = "";
		if (StringUtils.isNotBlank(dateStr)) {
			returnStr = returnStr + dateStr;
		}
		String ss = BaseConstants.PERIOD_INTERVAL_Map.get(periodInterval);
		if (StringUtils.isNotBlank(ss)) {
			returnStr = returnStr + ss;
		}
		returnStr = returnStr + "第" + period + "节";
		return returnStr;
	}

	public String resultMess(boolean isSuccess, String msg) {
		JSONObject on = new JSONObject();
		on.put("success", isSuccess);
		on.put("msg", msg);
		return on.toJSONString();
	}

	@ResponseBody
	@RequestMapping("/chooseByDateAndTeacherId")
	@ControllerInfo("查询教师当天课表")
	public String chooseByDateAndTeacherId(String unitId, String acadyear,
			String semester, String dateStr, String teacherId) {
		// dateStr:2015-08-11
		if (StringUtils.isBlank(dateStr)) {
			return resultMess(false, "代课时间不能为空");
		}
		Date date = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			date = formatter.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			return resultMess(false, "代课时间参数不对");
		}
		// teacherId
		Integer semesterInt = null;
		try {
			semesterInt = Integer.parseInt(semester);
		} catch (Exception e) {
			e.printStackTrace();
			return resultMess(false, "学年学期参数不对");
		}
		DateInfo dateInfo = dateInfoService.getDate(unitId, acadyear,
				semesterInt, date);
		if (dateInfo == null) {
			return resultMess(false, "未找到代课时间，请先去节假日设置");
		}
		// 当日课程
		List<CourseSchedule> scheduleList = findCourseScheduleByTeacherId(
				dateInfo, teacherId);
		if(CollectionUtils.isNotEmpty(scheduleList)){
			return resultMess(true, Json.toJSONString(scheduleList));
		}else{
			return resultMess(true, "");
		}
		
	}

	private List<CourseSchedule> findCourseScheduleByTeacherId(
			DateInfo dateInfo, String teacherId) {
		List<CourseSchedule> list = courseScheduleService
				.findCourseScheduleListByPerId(dateInfo.getAcadyear(),
						dateInfo.getSemester(), dateInfo.getWeek(), teacherId,
						"1");
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<CourseSchedule>();
		}
		Set<String> classIds = new HashSet<String>();
		Set<String> subjectIds = new HashSet<String>();
		List<CourseSchedule> returnlist = new ArrayList<CourseSchedule>();
		for (CourseSchedule cc : list) {
			if (dateInfo.getWeekday() - 1 == cc.getDayOfWeek()) {
				returnlist.add(cc);
				classIds.add(cc.getClassId());
				subjectIds.add(cc.getSubjectId());
			}
		}
		Map<String, String> classNameMap=new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(classIds)){
			classNameMap = tipsayService.getClassNameMap(classIds.toArray(new String[]{}));
		}
		List<Course> courseList = courseService.findListByIdIn(subjectIds
				.toArray(new String[] {}));
		Map<String, String> courseNameMap = new HashMap<String, String>();
		if (CollectionUtils.isNotEmpty(courseList)) {
			courseNameMap = EntityUtils.getMap(courseList, "id", "subjectName");
		}
		// 上午第1节-高一（1）班（物理） remark
		if (CollectionUtils.isNotEmpty(returnlist)) {

			for (CourseSchedule c : returnlist) {
				// 时间
				String remark = makeTimeStr(c.getPeriodInterval(),
						c.getPeriod(), "")
						+ "-";
				// 班级 课程
				if (classNameMap.containsKey(c.getClassId())) {
					remark = remark + classNameMap.get(c.getClassId());
				}
				if (courseNameMap.containsKey(c.getSubjectId())) {
					remark = remark + "(" + courseNameMap.get(c.getSubjectId())
							+ ")";
				}
				c.setRemark(remark);
			}
			// 根据节次升序
			Collections.sort(returnlist, new Comparator<CourseSchedule>() {

				@Override
				public int compare(CourseSchedule o1, CourseSchedule o2) {
					if (o1.getPeriodInterval().equals(o2.getPeriodInterval())) {
						return o1.getPeriod() - o2.getPeriod();
					} else {
						return o1.getPeriodInterval().compareTo(
								o2.getPeriodInterval());
					}

				}
			});
		}

		return returnlist;
	}

	@ResponseBody
	@RequestMapping("/saveTipsay")
	@ControllerInfo("保存")
	public String saveTipsay(TipsaySaveDto dto) {
		if (dto == null) {
			resultMess(false, "数据不存在");
		}
		CourseSchedule courseSchedule = courseScheduleService.findOne(dto
				.getCourseScheduleId());
		if (courseSchedule == null) {
			resultMess(false, "对应课程数据不存在");
		}
		List<String> ttIds = new ArrayList<String>();
		ttIds.add(dto.getNewTeacherId());
		String error = tipsayService.checkTimeByTeacher(ttIds, courseSchedule);
		if (StringUtils.isNotBlank(error)) {
			return resultMess(false, error);
		}

		List<CourseSchedule> ll = new ArrayList<CourseSchedule>();
		ll.add(courseSchedule);
		courseScheduleService.makeTeacherSet(ll);

		Tipsay tipsay = new Tipsay();
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
		tipsay.setCourseScheduleId(courseSchedule.getId());
		tipsay.setSubjectId(courseSchedule.getSubjectId());
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
		tipsay.setNewTeacherId(dto.getNewTeacherId());
		tipsay.setRemark(dto.getRemark());
		tipsay.setOperator(dto.getOperateuser());
		tipsay.setState(TipsayConstants.TIPSAY_STATE_1);
		tipsay.setTipsayType(TipsayConstants.TIPSAY_TYPE_01);
		// 目前默认管理员直接同意
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
		try {
			tipsayService.saveAll(tipsay, tipsayEx, courseSchedule);
		} catch (Exception e) {
			e.printStackTrace();
			return resultMess(true, "保存失败");
		}
		return resultMess(true, "保存成功");
	}

}
