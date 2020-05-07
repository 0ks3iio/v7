package net.zdsoft.stuwork.data.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.dto.DyWeekCheckResultDto;
import net.zdsoft.stuwork.data.dto.DyWeekCheckTable;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResult;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResultSubmit;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleUser;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemService;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultService;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultSubmitService;
import net.zdsoft.stuwork.data.service.DyWeekCheckRoleUserService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/stuwork")
public class DyWeekCheckEditAction extends BaseAction{
	@Autowired
	private DyWeekCheckRoleUserService dyWeekCheckRoleUserService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DyWeekCheckResultService dyWeekCheckResultService;
	@Autowired
	private DyWeekCheckResultSubmitService dyWeekCheckResultSubmitService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private DyWeekCheckItemService dyWeekCheckItemService;
	
	private String dRoleType="";
	
	@ResponseBody
	@RequestMapping("/checkweek/itemListEdit/saveSubmit")
    @ControllerInfo(value = "保存考核项")
	public String saveSubmit(String roleType,String dutyDate,String acadyear,String semester, ModelMap map){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(dutyDate);
			String unitId = getLoginInfo().getUnitId();
			if (StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_ADMIN)) {
				DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), acadyear, Integer.valueOf(semester), date), DateInfo.class);
				List<DyWeekCheckResultSubmit> dyWeekCheckResultSubmitList = dyWeekCheckResultSubmitService.findByWeek(getLoginInfo().getUnitId(), acadyear, semester, dateInfo.getWeek());
				Map<String, DyWeekCheckResultSubmit> dyWeekCheckResultSubmitMap = dyWeekCheckResultSubmitList.stream().filter(e -> dateInfo.getWeekday().equals(e.getDay())).collect(Collectors.toMap(e -> e.getRoleType(), e -> e));
				List<DyWeekCheckResultSubmit> saveList = new ArrayList<>();
				for (String role : DyWeekCheckRoleUser.ROLE_LIST) {
					if (!DyWeekCheckRoleUser.CHECK_ADMIN.equals(role) && dyWeekCheckResultSubmitMap.get(role) == null) {
						DyWeekCheckResultSubmit sub = new DyWeekCheckResultSubmit();
						sub.setId(UuidUtils.generateUuid());
						sub.setAcadyear(acadyear);
						sub.setSchoolId(unitId);
						sub.setSemester(semester);
						sub.setIsSubmit(true);
						sub.setRoleType(role);
						sub.setCheckDate(date);
						sub.setWeek(dateInfo.getWeek());
						sub.setDay(dateInfo.getWeekday());
						saveList.add(sub);
					}
				}
				dyWeekCheckResultSubmitService.saveAll(saveList.toArray(new DyWeekCheckResultSubmit[0]));
			} else {
				DyWeekCheckResultSubmit sub = new DyWeekCheckResultSubmit();
				sub.setAcadyear(acadyear);
				sub.setSchoolId(unitId);
				sub.setSemester(semester);
				sub.setIsSubmit(true);
				sub.setRoleType(roleType);
				sub.setCheckDate(date);
				DateInfo dateInfo  =SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), acadyear, NumberUtils.toInt(semester), date), DateInfo.class);
				sub.setWeek(dateInfo.getWeek());
				sub.setDay(dateInfo.getWeekday());
				dyWeekCheckResultSubmitService.saveSub(sub);
			}
		} catch (ParseException e) {
			return error("操作失败");
		}
		return success("操作成功！");
	}
	@ResponseBody
	@RequestMapping("/checkweek/itemListEdit/saveResult")
    @ControllerInfo(value = "保存考核结果")
	public String saveResult(String dutyDate,String classId,DyWeekCheckResultDto dto, ModelMap map){
		try {
			if(dto == null || CollectionUtils.isEmpty(dto.getResult())){
				return error("没有需要保存的数据!");
			}
			for(DyWeekCheckResult re : dto.getResult()){
				re.setSchoolId(this.getLoginInfo().getUnitId());
				re.setOperator(this.getLoginInfo().getUserId());
			}
			List<DyWeekCheckResult> newRelist = dto.getResult();
			Set<String> itemIds = EntityUtils.getSet(newRelist, "itemId");
			Set<String> reIds = EntityUtils.getSet(newRelist, "id");
			List<DyWeekCheckResult> relist = dyWeekCheckResultService.findByClassIdAndCheckDate(
					getLoginInfo().getUnitId(), dto.getResult().get(0).getAcadyear(), dto.getResult().get(0).getSemester(), classId,dto.getResult().get(0).getCheckDate());
			Map<String,DyWeekCheckItem> itemMap = dyWeekCheckItemService.findMapByIdIn(itemIds.toArray(new String[0]));
			float sumScore = 0f; 
			boolean error = false;
			String errorMsg = "";
			for(String itemId : itemIds){
				DyWeekCheckItem item = itemMap.get(itemId);
				if(item.getHasTotalScore() == 0){
					continue;
				}
				sumScore = item.getTotalScore();
				for(DyWeekCheckResult re : newRelist){
					if(StringUtils.equals(re.getItemId(), itemId)){
						sumScore = sumScore - re.getScore();
					}
				}
				for(DyWeekCheckResult oldRe : relist){
					if(StringUtils.equals(oldRe.getItemId(), itemId) && !reIds.contains(oldRe.getId())){
						sumScore = sumScore - oldRe.getScore();
					}
				}
				if(sumScore < 0){
					error = true;
					errorMsg = item.getItemName() + "的扣分总计已经超过了总分值！";
					break;
				}
			}
			if(error){
				return error("操作失败:"+errorMsg);
			}
			dyWeekCheckResultService.saveDto(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("操作失败！", e.getMessage());
		}
		return success("操作成功！");
	}
	
	@RequestMapping("/checkweek/checkInfo/page")
	@ControllerInfo(value = "考核录入详细页面")
	public String showCheckInfo(HttpServletRequest request,ModelMap map){
		String roleType = request.getParameter("roleType");
		String dutyDate = request.getParameter("dutyDate");
		String classId = request.getParameter("classId");
		Clazz clz = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(clz.getGradeId()), Grade.class);
		clz.setClassNameDynamic(grade.getGradeName()+clz.getClassName());
		map.put("className", clz.getClassNameDynamic());
		map.put("classId", classId);
		map.put("dutyDate", dutyDate);
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()),Semester.class);
		String acadyear = semester.getAcadyear();
		String se = semester.getSemester()+"";
		List<DyWeekCheckResultDto> resultDtos = dyWeekCheckResultService.findByItemList(getLoginInfo().getUnitId(),acadyear,se,classId,dutyDate);
		map.put("resultDtos", resultDtos);
		map.put("roleType", roleType);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = sdf.parse(dutyDate);
			DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), acadyear,semester.getSemester(), date), DateInfo.class);
			if(dateInfo == null){
				return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！ ");
			}
			map.put("week", dateInfo.getWeek());
			map.put("day", dateInfo.getWeekday());
			map.put("acadyear", acadyear);
			map.put("semester", se);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return "/stuwork/weekCheck/checkEdit/checkInfo.ftl";
	}
	
	@RequestMapping("/checkweek/checkList/page")
	@ControllerInfo(value = "考核录入页面")
	public String showCheckList(HttpServletRequest request,ModelMap map){
		String roleType = request.getParameter("roleType");
		if(StringUtils.isBlank(roleType)){
			roleType = dRoleType;
		}
		String dutyDate = request.getParameter("dutyDate");
//		String classId = request.getParameter("classId");
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),Semester.class);
		if(semester == null){
			return errorFtl(map, "当前时间不在学年学期内，无法维护！");
		}
		String acadyear = semester.getAcadyear();
		String se = semester.getSemester()+"";
		map.put("beginDate", semester.getSemesterBegin());
		map.put("endDate", semester.getSemesterEnd());
		map.put("userEnd", false);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_CLASS)){
				String beginDate = request.getParameter("beginDate");
				String endDate = request.getParameter("endDate");
				if(StringUtils.isNotBlank(beginDate)){
					map.put("beginDate", sdf.parse(beginDate));
				}
				if(StringUtils.isNotBlank(endDate)){
					map.put("userEnd", true);
					map.put("endDate", sdf.parse(endDate));
				}
				if(StringUtils.isBlank(dutyDate)){
					if(DateUtils.compareForDay(sdf.parse(beginDate), new Date())>0){
						return errorFtl(map, "未开始，不能维护！");
					}else if(DateUtils.compareForDay(sdf.parse(endDate), new Date())<0){
						dutyDate = endDate;
					}else{
						dutyDate = sdf.format(new Date());
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(StringUtils.isBlank(dutyDate)){
			dutyDate = sdf.format(new Date());
		}
		School school = SUtils.dc(schoolRemoteService.findOneById(getLoginInfo().getUnitId()),School.class);
//		List<Clazz> clazzList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(), acadyear), Clazz.class);
		if(StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_ADMIN)
				||StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_STUDENT)
				||StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_DEFEND)
				||StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_HYGIENE)){
			//全部班级权限
			map.put("sections", school.getSections());
			map.put("grades", "");
//			if(StringUtils.isBlank(classId)){
//				classId  =clazzList.get(0).getId();
//			}
		}else if(StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_TEACHER)
				||StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_CLASS)
				||StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_HEALTH)){
			//分学段班级权限
			if(StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_TEACHER)||
					StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_CLASS)){
				map.put("grades", "");
				//值周班和值周干部的考核范围可以从值周表传递过来
				String sections = request.getParameter("sections");
				map.put("sections", sections);
//				if(StringUtils.isBlank(classId)){
//					for(Clazz c :clazzList){
//						if(sections.indexOf(c.getSection()+"") > -1){
//							classId = c.getId();
//							break;
//						}
//					}
//				}
			}else{
				//体育老师
				List<DyWeekCheckRoleUser> roleUser = dyWeekCheckRoleUserService.findByRoleType(getLoginInfo().getUnitId(), roleType, acadyear, se);
				String sections = "";
				for(DyWeekCheckRoleUser role : roleUser){
					if(StringUtils.equals(getLoginInfo().getUserId(), role.getUserId())){
						if(sections.indexOf(role.getSection())<0){
							if(StringUtils.isBlank(sections)){
								sections = role.getSection();
							}else{
								sections = sections +"," + role.getSection();
							}
						}
					}
				}
				map.put("sections", sections);
//				if(StringUtils.isBlank(classId)){
//					for(Clazz c :clazzList){
//						if(sections.indexOf(c.getSection()+"") > -1){
//							classId = c.getId();
//							break;
//						}
//					}
//				}
			}
		}else if(StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_GRADE)){
			//年级组--分年级班级权限
			map.put("sections", "");
			List<DyWeekCheckRoleUser> roleUser = dyWeekCheckRoleUserService.findByRoleType(getLoginInfo().getUnitId(), roleType, acadyear, se);
			String grades = "";
			for(DyWeekCheckRoleUser role : roleUser){
				if(StringUtils.equals(getLoginInfo().getUserId(), role.getUserId())){
					if(grades.indexOf(role.getGrade())<0){
						if(StringUtils.isBlank(grades)){
							grades = role.getGrade();
						}else{
							grades = grades +"," + role.getGrade();
						}
					}
				}
			}
			map.put("grades", grades);
//			List<Grade> grade = SUtils.dt(
//					gradeRemoteService.findByUnitIdAndGradeCode(getLoginInfo().getUnitId(), grades.split(",")), 
//					new TR<List<Grade>>(){});
//			Set<String> gradeIds = EntityUtils.getSet(grade, "id");
//			Map<String, List<Clazz>> classMap = SUtils.dt(classRemoteService.findMapByGradeIdIn(gradeIds.toArray(new String[0])), new TypeReference<Map<String, List<Clazz>>>(){});
//			if(StringUtils.isBlank(classId)){
//				for(String gradeId : gradeIds){
//					if(CollectionUtils.isNotEmpty(classMap.get(gradeId))){
//						classId = classMap.get(gradeId).get(0).getId();
//					}
//				}
//			}
		}
//		map.put("classId", classId);
//		Clazz clz = SUtils.dc(classRemoteService.findById(classId),Clazz.class);
//		if(clz == null){
//			return errorFtl(map, "没有班级可维护!");
//		}
//		Grade grade = SUtils.dc(gradeRemoteService.findById(clz.getGradeId()), Grade.class);
//		clz.setClassNameDynamic(grade.getGradeName()+clz.getClassName());
//		map.put("className", clz.getClassNameDynamic());
//		List<DyWeekCheckResultDto> resultDtos = dyWeekCheckResultService.findByItemList(getLoginInfo().getUnitId(),acadyear,se,classId,dutyDate);
		map.put("acadyear", acadyear);
		map.put("semester", se);
		try {
			Date date = sdf.parse(dutyDate);
			DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), acadyear,semester.getSemester(), date), DateInfo.class);
			if(dateInfo == null){
				return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！ ");
			}
			map.put("week", dateInfo.getWeek());
			map.put("day", dateInfo.getWeekday());
			if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_ADMIN, roleType)){
				// 放开总管理员全部提交权限
				map.put("hasSubmit", "1");
			}else{
				DyWeekCheckResultSubmit sub  =dyWeekCheckResultSubmitService.findByRoleTypeAndCheckDate(getLoginInfo().getUnitId(), acadyear, se, roleType, date);
				if(sub == null){
					map.put("hasSubmit", "1");
				}else{
					map.put("hasSubmit", "0");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		map.put("roleType", roleType);
//		map.put("resultDtos", resultDtos);
		map.put("dutyDate", dutyDate);
		String blackTable = request.getParameter("blackTable");
		String blackAdmin = request.getParameter("blackAdmin");
		map.put("blackAdmin", blackAdmin);
		map.put("blackTable", blackTable);
		return "/stuwork/weekCheck/checkEdit/checkEditList.ftl";
	}
	

	@RequestMapping("/checkweek/checkEditStu/page")
	@ControllerInfo(value = "（值周班）值周表页面")
	public String showClassAdmin(HttpServletRequest request,ModelMap map) {
		map.put("blackTable", "1");
		return showCheckTable(request,map);
//		return "/stuwork/weekCheck/checkEdit/checkEditTable.ftl";
	}
	
	
	@RequestMapping("/checkweek/checkTable/page")
	@ControllerInfo(value = "值周表页面")
	public String showCheckTable(HttpServletRequest request,ModelMap map){
		String roleType = request.getParameter("roleType");
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),Semester.class);
		if(semester == null){
			return errorFtl(map, "当前时间不在学年学期内，无法维护！");
		}
		String acadyear = semester.getAcadyear();
		String se = semester.getSemester()+"";
		String roleId = this.getLoginInfo().getUserId();
		if(getLoginInfo().getOwnerType().equals(User.OWNER_TYPE_STUDENT)){
			//TODO 值周班
			Student stu = SUtils.dc(studentRemoteService.findOneById(getLoginInfo().getOwnerId()), Student.class);
			roleId = stu.getId();
			roleType = DyWeekCheckRoleUser.CHECK_CLASS;
		}
		List<DyWeekCheckTable> teables = dyWeekCheckRoleUserService.findByCheckTable(getLoginInfo().getUnitId(),acadyear,se,roleType, roleId);
		for(DyWeekCheckTable tea : teables){
			String secs =null;
			for(String s : tea.getSections()){
				if(StringUtils.isBlank(secs)){
					secs = s;
				}else{
					secs = secs+","+s;
				}
			}
			tea.setSec(secs);
		}
		String blackAdmin = request.getParameter("blackAdmin");
		if(StringUtils.isBlank(blackAdmin)){
			map.put("blackAdmin", "0");
		}else{
			map.put("blackAdmin", blackAdmin);
		}
		map.put("teables", teables);
		map.put("roleType", roleType);
		return "/stuwork/weekCheck/checkEdit/checkEditTable.ftl";
	}
	
	@RequestMapping("/checkweek/checkEdit/page")
	@ControllerInfo(value = "考核录入主页面")
	public String checkEditAdmin(HttpServletRequest request,ModelMap map, HttpSession httpSession) {
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		String userId = info.getUserId();
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0,unitId),Semester.class);
		if(semester == null){
			return errorFtl(map, "当前时间不在学年学期内，无法维护！");
		}
		String acadyear = semester.getAcadyear();
		String se = semester.getSemester()+"";
		List<String> roleTypeList= new ArrayList<String>(); 
		//判断是否是总管理员
		DyWeekCheckRoleUser role = dyWeekCheckRoleUserService.findByRoleTypeAndUser(unitId,DyWeekCheckRoleUser.CHECK_ADMIN, userId);
		if(role == null){
			map.put("hasAdmin", "0");
		}else{
			map.put("hasAdmin", "1");
			roleTypeList.add(DyWeekCheckRoleUser.CHECK_ADMIN);
		}
		List<DyWeekCheckRoleUser> roles = dyWeekCheckRoleUserService.findByUserId(unitId, userId, acadyear, se);
		Set<String> allRoleType = EntityUtils.getSet(roles, "roleType");
		roleTypeList.addAll(allRoleType);
		if(CollectionUtils.isEmpty(roleTypeList)){
			//无权限来维护
			return errorFtl(map, "你没有任何值周录入权限！");
		}else{
			if(roleTypeList.size() == 1){
				map.put("blackAdmin", "0");
				//直接进入到具体的考评页面(或者值周表页面)
				String roleType = roleTypeList.get(0);
				if(DyWeekCheckRoleUser.CHECK_TEACHER.equals(roleType) ||DyWeekCheckRoleUser.CHECK_CLASS.equals(roleType)){
					// 值周老师需要先进入值周表页面
					map.put("blackTable", "1");
					String roleId = this.getLoginInfo().getUserId();
					if(getLoginInfo().getOwnerType().equals(User.OWNER_TYPE_STUDENT)){
						//值周班
						Student stu = SUtils.dc(studentRemoteService.findOneById(getLoginInfo().getOwnerId()), Student.class);
						roleId = stu.getClassId();
					}
					List<DyWeekCheckTable> teables = dyWeekCheckRoleUserService.findByCheckTable(getLoginInfo().getUnitId(),acadyear,se,roleType, roleId);
					for(DyWeekCheckTable tea : teables){
						String secs =null;
						for(String s : tea.getSections()){
							if(StringUtils.isBlank(secs)){
								secs = s;
							}else{
								secs = secs+","+s;
							}
						}
						tea.setSec(secs);
					}
					map.put("teables", teables);
					map.put("roleType", roleType);
					return "/stuwork/weekCheck/checkEdit/checkEditTable.ftl";
				}else{
					map.put("blackTable", "0");
					// 其它角色可以直接进入考核页面
					dRoleType = roleType;
					return showCheckList(request, map);
//					return "/stuwork/weekCheck/checkEdit/checkEditList.ftl";
				}
			}else{
				//返回到选择考评角色页面
				map.put("blackAdmin", "1");
				map.put("roleTypes", roleTypeList);
				return "/stuwork/weekCheck/checkEdit/checkEditAdmin.ftl";
			}
		}
	}
}
