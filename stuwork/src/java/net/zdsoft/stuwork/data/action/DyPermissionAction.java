package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.stuwork.data.dto.ClazzPermissionDto;
import net.zdsoft.stuwork.data.entity.DyPermission;
import net.zdsoft.stuwork.data.service.DyPermissionService;

@Controller
@RequestMapping("/stuwork") 
public class DyPermissionAction  extends BaseAction{
	@Autowired
	private DyPermissionService dyPermissionService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@RequestMapping("/permission/page")
	public String showPermissionPage(ModelMap map){
		return "/stuwork/permission/dyPermissionIndex.ftl";
	}
	@RequestMapping("/permission/list")
	public String showPermissionList(ModelMap map,String classType,String permissionType ,String gradeId){

		String unitId = getLoginInfo().getUnitId();
		List<ClazzPermissionDto> returnDtos = Lists.newLinkedList();
		List<DyPermission> dyPermissions = dyPermissionService.findListByUnitId(unitId,classType,permissionType);
		fillUserName(dyPermissions);
		Map<String,List<DyPermission>> pMap = Maps.newHashMap();
		for(DyPermission permission:dyPermissions){
			List<DyPermission> eps = pMap.get(permission.getClassId());
			if(eps==null){
				eps = Lists.newArrayList();
			}
			eps.add(permission);
			pMap.put(permission.getClassId(), eps);
		}
		if(DyPermission.CLASS_TYPE_NORMAL.equals(classType)){
			List<Clazz> clazzs = SUtils.dt(classRemoteService.findBySchoolId(unitId),new TR<List<Clazz>>() {});
			for(Clazz clazz:clazzs){
				if(clazz.getIsGraduate() == 0  && clazz.getIsDeleted() == 0){
					ClazzPermissionDto permissionDto = new ClazzPermissionDto();
					permissionDto.setClassId(clazz.getId());
					permissionDto.setClassName(clazz.getClassNameDynamic());
					if(pMap.containsKey(clazz.getId())){
						List<DyPermission> dyPs = pMap.get(clazz.getId());
						for(DyPermission dyPermission:dyPs){
							if(StringUtils.isBlank(permissionDto.getPermisionUserIds())){
								permissionDto.setPermisionUserIds(dyPermission.getUserId());
								permissionDto.setPermisionUserNames(dyPermission.getUserName());
							}else{
								permissionDto.setPermisionUserIds(permissionDto.getPermisionUserIds()+","+dyPermission.getUserId());
								permissionDto.setPermisionUserNames(permissionDto.getPermisionUserNames()+","+dyPermission.getUserName());
							}
						}
					}
					returnDtos.add(permissionDto);
				}
			}

			map.put("returnDtos", returnDtos);
			map.put("classType",classType);
			map.put("permissionType" ,permissionType);
			return "/stuwork/permission/dyPermission.ftl";
		}else{
			Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);

			List<Grade> gradeList =  SUtils.dt(gradeRemoteService.findBySchoolId(unitId) , Grade.class);
			map.put("gradeList" , gradeList);
			map.put("gradeId" , gradeId);

			List<Teacher> teacherList = Teacher.dt(teacherRemoteService.findByUnitId(unitId) );
			Map<String,String> teacherNameMap = teacherList.parallelStream().collect(Collectors.toMap(Teacher::getId,Teacher::getTeacherName));
			Map<String,String> gradeNameMap = gradeList.parallelStream().collect(Collectors.toMap(Grade::getId,Grade::getGradeName));
			Set<String> gradeSet = new HashSet<>();
			if(StringUtils.isNotEmpty(gradeId)){
				gradeSet.add(gradeId);

			}else{
				gradeSet = gradeNameMap.keySet();
			}
			Unit unit = unitRemoteService.findTopUnitObject(unitId);
			Set<String> unitIdSet = new HashSet<>();
			unitIdSet.add(unit.getId());
			unitIdSet.add(unitId);
			List<Course> courseBXList = SUtils.dt(courseRemoteService.findByUnitIdIn(unitIdSet.toArray(new String[0]) , Integer.valueOf(BaseConstants.SUBJECT_TYPE_BX), null),new TR<List<Course>>() {});
			List<Course> courseXXList = SUtils.dt(courseRemoteService.findByUnitIdIn(unitIdSet.toArray(new String[0]) , Integer.valueOf(BaseConstants.SUBJECT_TYPE_XX), null),new TR<List<Course>>() {});
			List<Course> courseList = new ArrayList<>();
			courseList.addAll(courseBXList);
			courseList.addAll(courseXXList);
			Map<String,String> courseMap = courseList.parallelStream().collect(Collectors.toMap(Course::getId,Course::getSubjectName));
			List<TeachClass> teachClassList = SUtils.dt(teachClassRemoteService.findTeachClassList(unitId,semester.getAcadyear(),String.valueOf(semester.getSemester()),null,gradeSet.toArray(new String[0]),true) , TeachClass.class);
			for (TeachClass teachClass : teachClassList) {
				ClazzPermissionDto permissionDto = new ClazzPermissionDto();
				permissionDto.setClassId(teachClass.getId());
				permissionDto.setClassName(teachClass.getName());
				if(teacherNameMap.containsKey(teachClass.getTeacherId())){
					permissionDto.setTeacherName(teacherNameMap.get(teachClass.getTeacherId()));
				}
				if(gradeNameMap.containsKey(teachClass.getGradeId())){
					permissionDto.setGradeName(gradeNameMap.get(teachClass.getGradeId()));
				}
				if(courseMap.containsKey(teachClass.getCourseId())){
					permissionDto.setCourseName(courseMap.get(teachClass.getCourseId()));
				}
				if(pMap.containsKey(teachClass.getId())){
					List<DyPermission> dyPs = pMap.get(teachClass.getId());
					for(DyPermission dyPermission:dyPs){
						if(StringUtils.isBlank(permissionDto.getPermisionUserIds())){
							permissionDto.setPermisionUserIds(dyPermission.getUserId());
							permissionDto.setPermisionUserNames(dyPermission.getUserName());
						}else{
							permissionDto.setPermisionUserIds(permissionDto.getPermisionUserIds()+","+dyPermission.getUserId());
							permissionDto.setPermisionUserNames(permissionDto.getPermisionUserNames()+","+dyPermission.getUserName());
						}
					}
				}
				returnDtos.add(permissionDto);
			}
			map.put("returnDtos", returnDtos);
			map.put("classType",classType);
			map.put("permissionType" ,permissionType);

			return "/newstusys/sch/studentShow/studentTeaClaPermission.ftl";
		}

	}
	
	@ResponseBody
	@RequestMapping("/permission/save")
	public String permissionSave(String[] classIds,String[] userIds,boolean isAll ,String classsType,String permissionType){
		try{
			dyPermissionService.savePermission(classIds, userIds,getLoginInfo().getUnitId(),isAll ,classsType,permissionType);
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	@RequestMapping("/permissionStuSys/admin")
	@ControllerInfo("学生信息查询权限设置")
	public String permissionStuAdmin(){

		return "/newstusys/sch/studentShow/studentPermissionAdmin.ftl";
	}

	private void fillUserName(List<DyPermission> dyPermissions){
		if(CollectionUtils.isEmpty(dyPermissions)){
			return;
		}
		Set<String> userIds = Sets.newHashSet();
		for(DyPermission permission:dyPermissions){
			userIds.add(permission.getUserId());
		}
		List<User> users = SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[userIds.size()])),new TR<List<User>>() {});
		Map<String,String> userMap = EntityUtils.getMap(users, "id", "realName");
		for(DyPermission permission:dyPermissions){
			if(userMap.containsKey(permission.getUserId())){
				permission.setUserName(userMap.get(permission.getUserId()));
			}
		}
	}

	
}
