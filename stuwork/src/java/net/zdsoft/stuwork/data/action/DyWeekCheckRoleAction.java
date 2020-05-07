package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.dto.DyWeekCheckRoleUserDto;
import net.zdsoft.stuwork.data.dto.StuworkDateInfoDto;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleUser;
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
public class DyWeekCheckRoleAction extends BaseAction{
	@Autowired
	private DyWeekCheckRoleUserService dyWeekCheckRoleUserService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	
	@RequestMapping("/checkweek/roleUserTeacherEdit/page")
	@ControllerInfo(value = "人员权限设置--值周干部编辑")
	public String roleTeacherEdit(HttpServletRequest request,ModelMap map, HttpSession httpSession) {
		String dutyDate = request.getParameter("dutyDate");
		List<DyWeekCheckRoleUser> roles = dyWeekCheckRoleUserService.findCheckTeacher(getLoginInfo().getUnitId(),dutyDate);
		School school = SUtils.dc(schoolRemoteService.findOneById(getLoginInfo().getUnitId()), School.class);
		String[] sections = school.getSections().split(",");
		map.put("sections", sections);
		map.put("roles", roles);
		map.put("dutyDate", dutyDate);
		List<User> users = SUtils.dt(userRemoteService.findByUnitIds(getLoginInfo().getUnitId()), new TR<List<User>>(){});
		map.put("users", users);
		return "/stuwork/weekCheck/roleUser/roleUserTeacherEdit.ftl";
	}
	
	
	@ResponseBody
	@RequestMapping("/checkweek/roleUserTeacher/save")
    @ControllerInfo(value = "保存值周干部")
	public String saveCheckTeacher(String dutyDate,DyWeekCheckRoleUserDto roleDto, ModelMap map){
		try {
			if(StringUtils.isBlank(dutyDate) || CollectionUtils.isEmpty(roleDto.getRoles())){
				return error("保存失败！");
			}
			dyWeekCheckRoleUserService.saveCheckTeacher(getLoginInfo().getUnitId(),dutyDate,roleDto.getRoles());
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}
		return success("新增成功！");
	}
	
	@RequestMapping("/checkweek/roleUserTeacher/page")
	@ControllerInfo(value = "人员权限设置--值周干部")
	public String roleTeacherAdmin(HttpServletRequest request,ModelMap map, HttpSession httpSession) {
		String roleType = request.getParameter("roleType");
		if(StringUtils.isBlank(roleType)){
			roleType = DyWeekCheckRoleUser.CHECK_TEACHER;
		}
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0,unitId),Semester.class);
		if(semester == null){
			return errorFtl(map, "当前时间不在学年学期内，无法维护！");
		}
		String acadyear = semester.getAcadyear();
		String se = semester.getSemester()+"";
		List<DyWeekCheckRoleUser> roles = dyWeekCheckRoleUserService.findByRoleType(unitId,roleType,acadyear,se);
		map.put("roles", roles);
		map.put("startDate", semester.getSemesterBegin());
		map.put("endDate", semester.getSemesterEnd());
		//TODO
		School sch = SUtils.dc(schoolRemoteService.findOneById(getLoginInfo().getUnitId()), School.class);
		String[] sections = sch.getSections().split(",");
		map.put("sections", sections);
		List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(unitId, acadyear, NumberUtils.toInt(se)), new TR<List<DateInfo>>(){});
		map.put("dateInfoList", dateInfoList);
		
		return "/stuwork/weekCheck/roleUser/roleUserTeacher.ftl";
	}
	
	@RequestMapping("/checkweek/roleUser/page")
    @ControllerInfo(value = "人员权限设置")
	public String roleAdmin(HttpServletRequest request,ModelMap map, HttpSession httpSession) {
		String roleType = request.getParameter("roleType");
		if(StringUtils.isBlank(roleType)){
			roleType = DyWeekCheckRoleUser.CHECK_ADMIN;
		}
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		String userId = info.getUserId();
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0,unitId),Semester.class);
		if(semester == null){
			return errorFtl(map, "当前时间不在学年学期内，请先设置！");
		}
		String acadyear = semester.getAcadyear();
		String se = semester.getSemester()+"";
		List<DateInfo> dateInfos = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(unitId, acadyear, semester.getSemester()), new TR<List<DateInfo>>(){});
		if(CollectionUtils.isEmpty(dateInfos)){
			return errorFtl(map, "当前学年学期还没有设置节假日周次信息，请先到节假日设置模块设置！");
		}
		//判断是否是总管理员
		DyWeekCheckRoleUser role = dyWeekCheckRoleUserService.findByRoleTypeAndUser(unitId,DyWeekCheckRoleUser.CHECK_ADMIN, userId);
		if(role == null){
			map.put("hasAdmin", "0");
		}else{
			map.put("hasAdmin", "1");
		}
		if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_ADMIN, roleType)){
			//总管理员
			List<DyWeekCheckRoleUser> roles = dyWeekCheckRoleUserService.findAdminByUnitId(unitId);
			String roleIds = "";
			String roleName = "";
			for(DyWeekCheckRoleUser r : roles){
				if(StringUtils.isBlank(roleIds)){
					roleIds = r.getUserId();
					roleName = r.getRoleName();
				}else{
					roleIds = roleIds + "," + r.getUserId();
					roleName =roleName + ","+ r.getRoleName();
				}
			}
			map.put("roleNames", roleName);
			map.put("roleIds", roleIds);
		}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_STUDENT, roleType) || DyWeekCheckRoleUser.CHECK_DEFEND.equals(roleType)){
			//学生处、保卫处
			List<DyWeekCheckRoleUser> roles = dyWeekCheckRoleUserService.findByRoleType(unitId,roleType,acadyear,se);
			String roleIds = "";
			String roleName = "";
			for(DyWeekCheckRoleUser r : roles){
				if(StringUtils.isBlank(roleIds)){
					roleIds = r.getUserId();
					roleName = r.getRoleName();
				}else{
					roleIds = roleIds + "," + r.getUserId();
					roleName =roleName + ","+ r.getRoleName();
				}
			}
			map.put("roleNames", roleName);
			map.put("roleIds", roleIds);
		}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_GRADE, roleType)){
			//年级组
			List<DyWeekCheckRoleUser> roleUsers = dyWeekCheckRoleUserService.findByRoleType(unitId,roleType,acadyear,se);
			List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolId(unitId),new TR<List<Grade>>(){});
			Collections.sort(grades, new Comparator<Grade>(){
				@Override
				public int compare(Grade o1, Grade o2) {
					return o1.getGradeCode().compareTo(o2.getGradeCode());
				}});
			map.put("roles", roleUsers);
			map.put("grades", grades);
		}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_HEALTH, roleType)){
			//体育老师
			School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
			String[] sections = school.getSections().split(",");
			List<DyWeekCheckRoleUser> roleUsers = dyWeekCheckRoleUserService.findByRoleType(unitId,roleType,acadyear,se);
			map.put("roles", roleUsers);
			map.put("sections", sections);
		}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_CLASS, roleType)){
			//值周班
			List<DyWeekCheckRoleUser> roleUsers = dyWeekCheckRoleUserService.findByRoleType(unitId,roleType,acadyear,se);
			School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
			String[] sections = school.getSections().split(",");
			map.put("sections", sections);
			List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(unitId, acadyear, NumberUtils.toInt(se)), new TR<List<DateInfo>>(){});
			Map<Integer,StuworkDateInfoDto> dtoMap = new HashMap<Integer,StuworkDateInfoDto>();
			StuworkDateInfoDto dto = null;
			for(DateInfo dateInfo : dateInfoList){
				if(dtoMap.containsKey(dateInfo.getWeek())){
					dto = dtoMap.get(dateInfo.getWeek());
					if(DateUtils.compareForDay(dateInfo.getInfoDate(), dto.getBeginDate())<0){
						dto.setBeginDate(dateInfo.getInfoDate());
					}else if(DateUtils.compareForDay(dateInfo.getInfoDate(), dto.getEndDate())>0){
						dto.setEndDate(dateInfo.getInfoDate());
					}
				}else{
					dto = new StuworkDateInfoDto();
					dto.setAcadyear(acadyear);
					dto.setSemester(semester+"");
					dto.setWeek(dateInfo.getWeek()+"");
					dto.setBeginDate(dateInfo.getInfoDate());
					dto.setEndDate(dateInfo.getInfoDate());
					dtoMap.put(dateInfo.getWeek(), dto);
				}
			}
			List<StuworkDateInfoDto> dateDto =new ArrayList<StuworkDateInfoDto>(dtoMap.values());
			Collections.sort(dateDto, new Comparator<StuworkDateInfoDto>(){
				@Override
				public int compare(StuworkDateInfoDto o1, StuworkDateInfoDto o2) {
					return NumberUtils.toInt(o1.getWeek())-NumberUtils.toInt(o2.getWeek());
				}});
			map.put("dateDto", dateDto);
			map.put("roles", roleUsers);
		}
		else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_HYGIENE, roleType)) {
			//卫生检查
			List<DyWeekCheckRoleUser> roles = dyWeekCheckRoleUserService.findByRoleType(unitId,roleType,acadyear,se);
			String roleIds = "";
			String roleName = "";
			for(DyWeekCheckRoleUser r : roles){
				if(StringUtils.isBlank(roleIds)){
					roleIds = r.getUserId();
					roleName = r.getRoleName();
				}else{
					roleIds = roleIds + "," + r.getUserId();
					roleName =roleName + ","+ r.getRoleName();
				}
			}
			map.put("roleNames", roleName);
			map.put("roleIds", roleIds);
		}
		map.put("acadyear", acadyear);
		map.put("se", se);
		map.put("roleType", roleType);
		return "/stuwork/weekCheck/roleUser/roleUserIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/weekCheck/roleUser/role/save")
    @ControllerInfo(value = "保存")
	public String saveArrange(String roleType,String userIds,String gradeId,String section,ModelMap map){
		try {
			if(StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_TEACHER)){
				return error("保存失败！保存类型不对");
			}
			Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()),Semester.class);
			String acadyear = semester.getAcadyear();
			String se = semester.getSemester()+"";
			if(StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_CLASS)){
//				if(StringUtils.isNotBlank(userIds)){
//					Clazz cls = SUtils.dc(classRemoteService.findById(userIds), Clazz.class);
//					if(!StringUtils.equals(cls.getSection()+"", section)){
//						return error("保存失败！所选班级的学段和值周学段范围不一致！");
//					}
//				}
				DyWeekCheckRoleUser roleUser = new DyWeekCheckRoleUser();
				roleUser.setAcadyear(acadyear);
				roleUser.setSemester(se);
				roleUser.setSchoolId(this.getLoginInfo().getUnitId());
				roleUser.setWeek(NumberUtils.toInt(gradeId));
				roleUser.setRoleType(roleType);
				roleUser.setClassId(userIds);
				roleUser.setSection(section);
				System.out.println(userIds);
				//TODO
				dyWeekCheckRoleUserService.saveClass(roleUser);
			}else{
				if(StringUtils.isNotBlank(roleType)&&StringUtils.isNotBlank(userIds)){
					List<DyWeekCheckRoleUser> roleUserlist = new ArrayList<DyWeekCheckRoleUser>();
					String[] roleIds = userIds.split(",");
					DyWeekCheckRoleUser role;
					for(String roleId : roleIds){
						role = new DyWeekCheckRoleUser();
						role.setUserId(roleId);
						role.setRoleType(roleType);
						role.setSchoolId(this.getLoginInfo().getUnitId());
						roleUserlist.add(role);
					}
					dyWeekCheckRoleUserService.saveList(roleUserlist, getLoginInfo().getUnitId(), roleType,acadyear , se, gradeId, section);
				}
				if(StringUtils.isBlank(userIds)){
					//该值为空表示直接删掉原来的数据
					dyWeekCheckRoleUserService.saveList(new ArrayList<DyWeekCheckRoleUser>(), getLoginInfo().getUnitId(), roleType,acadyear , se, gradeId, section);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}
		return success("新增成功！");
	}
	
}
