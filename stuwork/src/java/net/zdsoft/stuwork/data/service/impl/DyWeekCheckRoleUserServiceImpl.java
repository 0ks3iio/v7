package net.zdsoft.stuwork.data.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.dao.DyWeekCheckRoleUserDao;
import net.zdsoft.stuwork.data.dto.DyWeekCheckTable;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResultSubmit;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleStudent;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleUser;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemService;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultSubmitService;
import net.zdsoft.stuwork.data.service.DyWeekCheckRoleStudentService;
import net.zdsoft.stuwork.data.service.DyWeekCheckRoleUserService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Service("dyWeekCheckRoleUserService")
public class DyWeekCheckRoleUserServiceImpl extends BaseServiceImpl<DyWeekCheckRoleUser, String>  implements DyWeekCheckRoleUserService{

	@Autowired
	private DyWeekCheckRoleUserDao dyWeekCheckRoleUserDao;	
	@Autowired
	private DyWeekCheckItemService dyWeekCheckItemService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private DyWeekCheckResultSubmitService dyWeekCheckResultSubmitService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService; 
	@Autowired
	private DyWeekCheckRoleStudentService dyWeekCheckRoleStudentService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Override
	public List<DyWeekCheckRoleUser> findByRoleType(String roleType) {
		return null;
	}
	
	public void saveClass(DyWeekCheckRoleUser role){
		dyWeekCheckRoleUserDao.deleteByClass(role.getSchoolId(),role.getRoleType(),role.getAcadyear(),role.getSemester(),role.getWeek(),role.getSection());
		dyWeekCheckRoleStudentService.deleteByContion(role.getSchoolId(),role.getAcadyear(),role.getSemester(),role.getWeek(),role.getSection());
		if(StringUtils.isNotBlank(role.getClassId())){
			List<DyWeekCheckRoleStudent> roleStuList = new ArrayList<>();
			String stuIds = role.getClassId();
			role.setClassId(null);
			checkSave(role);
			save(role);
			DyWeekCheckRoleStudent roleStu;
			String[] studentIds = stuIds.split(",");
			Map<String,Student> stuMap = EntityUtils.getMap(SUtils.dt(studentRemoteService.findListByIds(studentIds), new TR<List<Student>>() {}), "id");
			for(String studentId : studentIds) {
				roleStu= new DyWeekCheckRoleStudent();
				roleStu.setAcadyear(role.getAcadyear());
				roleStu.setRoleId(role.getId());
				roleStu.setSemester(role.getSemester());
				roleStu.setSection(role.getSection());
				roleStu.setWeek(role.getWeek());
				roleStu.setStudentId(studentId);
				roleStu.setClassId(stuMap.get(studentId).getClassId());
				roleStu.setSchoolId(role.getSchoolId());
				roleStuList.add(roleStu);
			}
			if(CollectionUtils.isNotEmpty(roleStuList)) {
				dyWeekCheckRoleStudentService.saveList(roleStuList);
			}
		}
	}
	
	@Override
	public void saveCheckTeacher(String unitId, String dutyDate,
			List<DyWeekCheckRoleUser> roles) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(dutyDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dyWeekCheckRoleUserDao.deleteCheckTeacher(unitId,date);
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId),Semester.class);
		String acadyear = semester.getAcadyear();
		String se = semester.getSemester()+"";
		DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, semester.getSemester(), date),DateInfo.class);
		if(dateInfo == null){
			System.out.println("不在学年学期内");
			return;
		}
		List<DyWeekCheckRoleUser> saveRoles =new ArrayList<DyWeekCheckRoleUser>();
		for(DyWeekCheckRoleUser role : roles){
			if(StringUtils.isNotBlank(role.getUserId())){
				role.setAcadyear(acadyear);
				role.setSemester(se);
				role.setRoleType(DyWeekCheckRoleUser.CHECK_TEACHER);
				role.setSchoolId(unitId);
				role.setWeek(dateInfo.getWeek());
				role.setDay(dateInfo.getWeekday());
				role.setDutyDate(date);
				saveRoles.add(role);
			}
		}
		if(CollectionUtils.isEmpty(saveRoles)){
			return;
		}
		DyWeekCheckRoleUser[] rs = saveRoles.toArray(new DyWeekCheckRoleUser[]{});
		checkSave(rs);
		saveAll(rs);
	}
	
	@Override
	public void saveList(List<DyWeekCheckRoleUser> roleUserlist, String schoolId, String roleType, String acadyear, String semester, String gradeId, String section) {
		if(DyWeekCheckRoleUser.CHECK_ADMIN.equals(roleType)){
			dyWeekCheckRoleUserDao.deleteByRoleType(schoolId,roleType);
		}else if(DyWeekCheckRoleUser.CHECK_STUDENT.equals(roleType) || DyWeekCheckRoleUser.CHECK_DEFEND.equals(roleType)){
			for(DyWeekCheckRoleUser role : roleUserlist){
				role.setAcadyear(acadyear);
				role.setSemester(semester);
			}
			dyWeekCheckRoleUserDao.deleteByRoleTypeAndAcadyear(schoolId,roleType,acadyear,semester);
		}else if(DyWeekCheckRoleUser.CHECK_GRADE.equals(roleType)){
			for(DyWeekCheckRoleUser role : roleUserlist){
				role.setAcadyear(acadyear);
				role.setSemester(semester);
				role.setGrade(gradeId);
			}
			dyWeekCheckRoleUserDao.deleteByRoleTypeAndAcadyearAndGrade(schoolId,roleType,acadyear,semester,gradeId);
		}else if(DyWeekCheckRoleUser.CHECK_HEALTH.equals(roleType)){
			for(DyWeekCheckRoleUser role : roleUserlist){
				role.setAcadyear(acadyear);
				role.setSemester(semester);
				role.setSection(section);
			}
			dyWeekCheckRoleUserDao.deleteByRoleTypeAndAcadyearAndSection(schoolId,roleType,acadyear,semester,section);
		}
		else if(DyWeekCheckRoleUser.CHECK_HYGIENE.equals(roleType)){
			for(DyWeekCheckRoleUser role : roleUserlist){
				role.setAcadyear(acadyear);
				role.setSemester(semester);
				role.setSection(section);
			}
			dyWeekCheckRoleUserDao.deleteByRoleTypeAndAcadyear(schoolId,roleType,acadyear,semester);
		}
		if(CollectionUtils.isEmpty(roleUserlist)){
			return;
		}
		DyWeekCheckRoleUser[] roles = roleUserlist.toArray(new DyWeekCheckRoleUser[]{});
		checkSave(roles);
		saveAll(roles);
	}
	@Override
	public List<DyWeekCheckRoleUser> findByUserId(String unitId, String userId, String acadyear, String semester) {
		List<DyWeekCheckRoleUser> roles = dyWeekCheckRoleUserDao.findByUserId(unitId,userId, acadyear, semester);
		return roles;
	}
	@Override
	public DyWeekCheckRoleUser findByRoleTypeAndUser(String unitId,
			String roleType, String userId) {
		return dyWeekCheckRoleUserDao.findByRoleTypeAndUserId(unitId,roleType, userId);
	}
	@Override
	public List<DyWeekCheckRoleUser> findCheckTeacher(String unitId,
			String dutyDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		List<DyWeekCheckRoleUser> roles = new ArrayList<DyWeekCheckRoleUser>();
		try {
			date = sdf.parse(dutyDate);
			roles =  dyWeekCheckRoleUserDao.findCheckTeacher(unitId,date);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		Set<String> userIds = EntityUtils.getSet(roles, "userId");
		List<User> userList = SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[0])), new TR<List<User>>(){});
		Map<String,User> userMap = EntityUtils.getMap(userList, "id");
		for(DyWeekCheckRoleUser role : roles){
			role.setRoleName(userMap.get(role.getUserId()).getUsername());
		}
		return roles;
	}

	@Override
	public List<DyWeekCheckRoleUser> findCheckTeacherByUserId(String unitId, String userId, String dutyDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		List<DyWeekCheckRoleUser> roles = new ArrayList<DyWeekCheckRoleUser>();
		try {
			date = sdf.parse(dutyDate);
			roles =  dyWeekCheckRoleUserDao.findCheckTeacherByUserId(unitId,userId,date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Set<String> userIds = EntityUtils.getSet(roles, "userId");
		List<User> userList = SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[0])), new TR<List<User>>(){});
		Map<String,User> userMap = EntityUtils.getMap(userList, "id");
		for(DyWeekCheckRoleUser role : roles){
			role.setRoleName(userMap.get(role.getUserId()).getUsername());
		}
		return roles;
	}

	@Override
	public List<DyWeekCheckRoleUser> findByRoleType(String unitId,
			String roleType, String acadyear, String semester) {
		List<DyWeekCheckRoleUser> roles =  dyWeekCheckRoleUserDao.findByRoleType(unitId,roleType,acadyear,semester);
		if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_CLASS, roleType)){
			if(CollectionUtils.isNotEmpty(roles)) {
				Set<String> roleIds = EntityUtils.getSet(roles, "id");
				List<DyWeekCheckRoleStudent> roleStuList = dyWeekCheckRoleStudentService.findByRoleIds(roleIds.toArray(new String[0]));
				Set<String> stuIds = new HashSet<>();
				Map<String,List<DyWeekCheckRoleStudent>> roleStuMap = new HashMap<>();
				for(DyWeekCheckRoleStudent roleStu : roleStuList) {
					if(!roleStuMap.containsKey(roleStu.getRoleId())) {
						roleStuMap.put(roleStu.getRoleId(), new ArrayList<>());
					}
					stuIds.add(roleStu.getStudentId());
					roleStuMap.get(roleStu.getRoleId()).add(roleStu);
				}
				Map<String,Student> stuMap = EntityUtils.getMap(SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {}),"id");
				for(DyWeekCheckRoleUser role : roles){
					if(StringUtils.isNotBlank(role.getClassId())) {
						// classId为空表示老数据，这里会对该老数据进行重新处理
						List<Student> stus = SUtils.dt(studentRemoteService.findByClassIds(role.getClassId()), new TR<List<Student>>() {});
						role.setClassId(null);
						for(Student stu : stus) {
							role.setClassId(StringUtils.isBlank(role.getClassId())?stu.getId():role.getClassId()+","+stu.getId());
							role.setRoleName(StringUtils.isBlank(role.getRoleName())?stu.getStudentName():role.getRoleName()+","+stu.getStudentName());
						}
						saveClass(role);
					}else {
						List<DyWeekCheckRoleStudent> roleStus = roleStuMap.get(role.getId());
						for (DyWeekCheckRoleStudent rs : roleStus) {
							role.setClassId(StringUtils.isBlank(role.getClassId())?rs.getStudentId():role.getClassId()+","+rs.getStudentId());
							Student stu = stuMap.get(rs.getStudentId());
							role.setRoleName(StringUtils.isBlank(role.getRoleName())?stu.getStudentName():role.getRoleName()+","+stu.getStudentName());
						}
					}
//					role.setRoleName(clsMap.get(role.getClassId()).getClassNameDynamic());
				}
			}
			
		}else{
			Set<String> userIds = EntityUtils.getSet(roles, "userId");
			List<User> userList = SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[0])), new TR<List<User>>(){});
			Map<String,User> userMap = EntityUtils.getMap(userList, "id");
			for(DyWeekCheckRoleUser role : roles){
				role.setRoleName(userMap.get(role.getUserId()).getRealName());
			}
		}
		return roles;
	}
	
	
	@Override
	public List<DyWeekCheckRoleUser> findAdminByUnitId(String unitId) {
		List<DyWeekCheckRoleUser> roles =  dyWeekCheckRoleUserDao.findAdminByUnitId(unitId);
		if(CollectionUtils.isEmpty(roles)){
			return new ArrayList<DyWeekCheckRoleUser>();
		}
		Set<String> userIds = EntityUtils.getSet(roles, "userId");
		List<User> userList = SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[0])), new TR<List<User>>(){});
		Map<String,User> userMap = EntityUtils.getMap(userList, "id");
		for(DyWeekCheckRoleUser role : roles){
			role.setRoleName(userMap.get(role.getUserId()).getRealName());
		}
		return roles;
	}
	@Override
	public List<DyWeekCheckTable> findByCheckTable(String unitId,String acadyear,
			String semester, String roleType,  String roleId) {
		List<DyWeekCheckTable> teables = new ArrayList<DyWeekCheckTable>();
		List<DyWeekCheckRoleUser> roles = new ArrayList<DyWeekCheckRoleUser>();
		if(StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_TEACHER)){
			roles = dyWeekCheckRoleUserDao.findByRoleId(unitId,roleId, acadyear, semester,roleType);
		}else if(StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_CLASS)){
			//值周班
			List<DyWeekCheckRoleStudent> roleStu = dyWeekCheckRoleStudentService.findByStuId(unitId,acadyear, semester,roleId);
			if(CollectionUtils.isNotEmpty(roleStu)) {
				Set<String> roleIds = EntityUtils.getSet(roleStu, "roleId");
				roles = this.findListByIds(roleIds.toArray(new String[0]));
			}
		}else {
			roles = dyWeekCheckRoleUserDao.findByClassId(unitId,roleId, acadyear, semester,roleType);
		}
		if(CollectionUtils.isEmpty(roles)){
			return new ArrayList<DyWeekCheckTable>();
		}
		List<DyWeekCheckRoleUser> roleUsers = checkRoleEdit(unitId,roles);
		List<DyWeekCheckResultSubmit> submitList = dyWeekCheckResultSubmitService.findByRoleType(unitId, acadyear, semester, roleType);
		DateInfo dayInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, NumberUtils.toInt(semester), new Date()),DateInfo.class);
		DyWeekCheckTable teable = null;
		Map<String, McodeDetail>  mcodeDetailMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-RKXD"), new TypeReference<Map<String, McodeDetail> >(){});
//		Map<String,String> secMap = new HashMap<String, String>();
//		secMap.put("1", "小学");
//		secMap.put("2", "初中");
//		secMap.put("3", "高中");
//		secMap.put("4", "剑桥高中");
		
		//时间没到为未开始 0，时间到，没有点提交按钮为未提交，点提交按钮的为已提交
		for(DyWeekCheckRoleUser roleUser : roleUsers){
			if(StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_TEACHER)){
				if(teable == null){
					teable = new DyWeekCheckTable();
					teable.setWeekDate(roleUser.getDutyDate());
				}
				if(DateUtils.compareForDay(roleUser.getDutyDate(), teable.getWeekDate()) == 0){
					teable.getSections().add(roleUser.getSection());
					if(StringUtils.isBlank(teable.getSectionStr())){
						
						teable.setSectionStr(mcodeDetailMap.get(roleUser.getSection()).getMcodeContent());
					}else{
					teable.setSectionStr(teable.getSectionStr() + ","+mcodeDetailMap.get(roleUser.getSection()).getMcodeContent());
					}
				}else{
					teables.add(teable);
					teable = new DyWeekCheckTable();
					teable.setWeekDate(roleUser.getDutyDate());
					teable.getSections().add(roleUser.getSection());
					teable.setSectionStr(mcodeDetailMap.get(roleUser.getSection()).getMcodeContent());
				}
				if(DateUtils.compareForDay(roleUser.getDutyDate(),new Date()) > 0){
					teable.setState(0);
				}else{
					teable.setState(1);
					for(DyWeekCheckResultSubmit submit:submitList){
						if(DateUtils.compareForDay(submit.getCheckDate(), roleUser.getDutyDate()) == 0){
							if(submit.getIsSubmit()){
								teable.setState(2);
							}
						}
					}
				}
			}else if(StringUtils.equals(roleType, DyWeekCheckRoleUser.CHECK_CLASS)){
				if(teable == null){
					teable = new DyWeekCheckTable();
					teable.setWeek(roleUser.getWeek());
				}
				if(teable.getWeek() == roleUser.getWeek()){
					teable.getSections().add(roleUser.getSection());
					if(StringUtils.isBlank(teable.getSectionStr())){
						teable.setSectionStr(mcodeDetailMap.get(roleUser.getSection()).getMcodeContent());
					}else{
						teable.setSectionStr(teable.getSectionStr() + ","+mcodeDetailMap.get(roleUser.getSection()).getMcodeContent());
					}
				}else{
					teables.add(teable);
					teable = new DyWeekCheckTable();
					teable.setWeek(roleUser.getWeek());
					teable.getSections().add(roleUser.getSection());
					teable.setSectionStr(mcodeDetailMap.get(roleUser.getSection()).getMcodeContent());
				}
				if(teable.getBeginDate() == null || teable.getEndDate() ==null){
					List<DateInfo> dateInfos = SUtils.dt(dateInfoRemoteService.findByWeek(unitId,acadyear,NumberUtils.toInt(semester),roleUser.getWeek()),new TR<List<DateInfo>>(){}); 
					teable.setBeginDate(dateInfos.get(0).getInfoDate());
					teable.setEndDate(dateInfos.get(dateInfos.size()-1).getInfoDate());
				}
				if(dayInfo.getWeek() < roleUser.getWeek()){
					teable.setState(0);
				}else{
					teable.setState(1);
					for(DyWeekCheckResultSubmit submit:submitList){
						if(submit.getWeek() == teable.getWeek()){
							if(submit.getIsSubmit()){
								teable.setState(2);
							}
						}
					}
				}
			}
		}
		if(teable != null)
			teables.add(teable);
		return teables;
	}
	/**
	 *这里需要对数据进行处理
	 *值周干部（可能在该天没有考核项可以维护（角色权限和时间权限），所以需要去掉这些数据）
	 *值周班（权限控制到一周，故只要判断有没有角色权限即可） 
	 */
	private List<DyWeekCheckRoleUser> checkRoleEdit(String schoolId,List<DyWeekCheckRoleUser> roles){
		Set<String> roleTypes = EntityUtils.getSet(roles, "roleType");
		List<DyWeekCheckItem> items = dyWeekCheckItemService.findByRoleTypes(schoolId,roleTypes);
		List<DyWeekCheckRoleUser> roleUsers = new ArrayList<DyWeekCheckRoleUser>();
		boolean itemFag = false;
		for(DyWeekCheckRoleUser role : roles){
			itemFag = false;
			for(DyWeekCheckItem item : items){
				if(item.getRoles().contains(role.getRoleType())){
					//有角色权限
					if(StringUtils.equals(role.getRoleType(), DyWeekCheckRoleUser.CHECK_TEACHER)){
						if(item.getDays().contains(role.getDay()+"")){
							//有时间权限
							itemFag = true;
						}
					}else{
						itemFag = true;
					}
				}
				if(itemFag){
					break;
				}
			}
			if(itemFag){
				roleUsers.add(role);
			}
		}
		return roleUsers;
	}
	@Override
	public String roleTeacherImport(String schoolId, String acadyear,
			String semester, String sections, List<String[]> datas) {
		//  值周干部导入数据处理
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<DateInfo> infos = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(schoolId, acadyear, NumberUtils.toInt(semester)), new TR<List<DateInfo>>(){});
		Map<String,DateInfo> infoMap  =new HashMap<String, DateInfo>();
		String startDate = sdf.format(infos.get(0).getInfoDate());
		String endDate = sdf.format(infos.get(infos.size() - 1).getInfoDate());
		for(DateInfo info : infos){
			infoMap.put(sdf.format(info.getInfoDate()), info);
		}
		Map<String, McodeDetail>  mcodeDetailMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-RKXD"), new TypeReference<Map<String, McodeDetail> >(){});
		
		List<User> userList = SUtils.dt(userRemoteService.findByOwnerType(new String[]{schoolId},User.OWNER_TYPE_TEACHER), new TR<List<User>>(){});
		Map<String,User> userMap = EntityUtils.getMap(userList, "username");
		
		List<Teacher> teacherListTemp = SUtils.dt(teacherRemoteService.findByUnitId(schoolId), new TR<List<Teacher>>(){});
		Set<String> teacherNameSet1 = new HashSet<String>();
		Set<String> teacherNameSet2 = new HashSet<String>();//重复的教师名字哦
		Set<String> teacherNameSet3 = new HashSet<String>();//不重复的教师名字
		for(Teacher teacher : teacherListTemp){
			if(teacherNameSet1.contains(teacher.getTeacherName())){
				teacherNameSet2.add(teacher.getTeacherName());
			}
			teacherNameSet1.add(teacher.getTeacherName());
		}
		Set<String> teacherIdSet = new HashSet<String>();
		for(Teacher teacher : teacherListTemp){
			if(!teacherNameSet2.contains(teacher.getTeacherName())){
				teacherIdSet.add(teacher.getId());
				teacherNameSet3.add(teacher.getTeacherName());
			}
		}
		List<User> userList2 = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(teacherIdSet)){			
			userList2 = SUtils.dt(userRemoteService.findByOwnerIds(teacherIdSet.toArray(new String[0])), new TR<List<User>>(){});
		}
		Map<String, String> userMap2 = new HashMap<String, String>();
		for(User user : userList2){
			userMap2.put(user.getOwnerId(), user.getId());
		}
		Map<String, String> resultMap = new HashMap<String, String>();
		for(Teacher teacher : teacherListTemp){
			resultMap.put(teacher.getTeacherName(), userMap2.get(teacher.getId()));
		}
		//取出该学期下的老数据，更新是需要先删除老数据
		List<DyWeekCheckRoleUser> roleUsers = findByRoleType(schoolId,DyWeekCheckRoleUser.CHECK_TEACHER,acadyear,semester);
		Set<DyWeekCheckRoleUser> deleteIds = new HashSet<DyWeekCheckRoleUser>();
		List<DyWeekCheckRoleUser> roleList = new ArrayList<DyWeekCheckRoleUser>();
		DyWeekCheckRoleUser role = null;
		String[] sectionArrs  =sections.split(",");
		String[] errorData=null;//new String[4]
		Map<String,String> dateExictMap = new HashMap<String, String>();
		boolean hasError = false;
		for(int i =0;i< datas.size();i++){
			String[] dataArr = datas.get(i);
			if(StringUtils.isBlank(dataArr[0].trim())){
				errorData = new String[4];
				errorData[0]=errorDataList.size() +1 +"";
				errorData[1]="日期";
				errorData[2]="";
				errorData[3]="日期不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(!infoMap.containsKey(dataArr[0].trim())){
				errorData = new String[4];
				errorData[0]=errorDataList.size() +1 +"";
				errorData[1]="日期";
				errorData[2]=dataArr[0];
				errorData[3]="不是有效的日期,日期范围："+startDate+"~"+endDate;
				errorDataList.add(errorData);
				continue;
			}
			if(dateExictMap.containsKey(dataArr[0].trim())){
				errorData = new String[4];
				errorData[0]=errorDataList.size() +1 +"";
				errorData[1]="日期";
				errorData[2]=dataArr[0];
				errorData[3]="第"+(i+1)+"条的日期与第"+dateExictMap.get(dataArr[0].trim())+"条重复，以第一条数据为准";
				errorDataList.add(errorData);
				continue;
			}else{
				dateExictMap.put(dataArr[0].trim(), i+1+"");
			}
			DateInfo dateInfo  =infoMap.get(dataArr[0]);
			//hasError = false;//一行有数据没有通过校验，则这一整行的各个学段的数据都不能导入
			for(int j = 0;j<sectionArrs.length;j++){
				String section = sectionArrs[j];
				/*if(dataArr.length>(j+3)&&StringUtils.isNotBlank(dataArr[j+3])&&!userMap.containsKey(dataArr[j+3])){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]=mcodeDetailMap.get(section).getMcodeContent();
					errorData[2]=dataArr[j+3];
					errorData[3]="该用户名的教师在本单位不存在！";
					errorDataList.add(errorData);
					hasError = true;
					break;
				}*/
				if(dataArr.length>(j+3) && StringUtils.isNotBlank(dataArr[j+3]) && teacherNameSet2.contains(dataArr[j+3])){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]=mcodeDetailMap.get(section).getMcodeContent();
					errorData[2]=dataArr[j+3];
					errorData[3]="本单位存在多个相同的该教师姓名！";
					errorDataList.add(errorData);
					//hasError = true;
					continue;
				}
				
				if(dataArr.length>(j+3) && StringUtils.isNotBlank(dataArr[j+3]) && !teacherNameSet2.contains(dataArr[j+3]) && !teacherNameSet3.contains(dataArr[j+3])){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]=mcodeDetailMap.get(section).getMcodeContent();
					errorData[2]=dataArr[j+3];
					errorData[3]="该教师姓名在本单位不存在！";
					errorDataList.add(errorData);
					//hasError = true;
					continue;
				}
				role = new DyWeekCheckRoleUser();
				role.setAcadyear(acadyear);
				role.setSemester(semester);
				role.setSection(section);
				role.setSchoolId(schoolId);
				role.setRoleType(DyWeekCheckRoleUser.CHECK_TEACHER);
				role.setWeek(dateInfo.getWeek());
				role.setDay(dateInfo.getWeekday());
				role.setDutyDate(dateInfo.getInfoDate());
				if(dataArr.length>(j+3) && StringUtils.isNotBlank(dataArr[j+3])){
					//对于维护空的数据  不需要操作，如果数据库有数据则删除
					//role.setUserId(userMap.get(dataArr[j+3]).getId());
					role.setUserId(resultMap.get(dataArr[j+3]));
					roleList.add(role);
					successCount++;
				}
				//检查数据库中是否存在该数据
				for(DyWeekCheckRoleUser oldRole : roleUsers){
					if(StringUtils.equals(oldRole.getSection(), section) && DateUtils.compareForDay(oldRole.getDutyDate(), dateInfo.getInfoDate())==0){
						deleteIds.add(oldRole);
					}
				}
			}
			//if(hasError){
				//continue;
			//}
			
			/*for(int j = 0;j<sectionArrs.length;j++){
				String section = sectionArrs[j];
				
			}*/
		}
		if(CollectionUtils.isNotEmpty(deleteIds)){
			dyWeekCheckRoleUserDao.deleteAll(deleteIds);
		}
		if(CollectionUtils.isNotEmpty(roleList)){
			DyWeekCheckRoleUser[] roles = roleList.toArray(new DyWeekCheckRoleUser[0]);
			checkSave(roles);
			saveAll(roles);
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}
	
	@Override
	public String roleClassImport(String schoolId, String acadyear, String semester, String sections, List<String[]> datas) {
		//TODO  值周班导入数据处理
		Json importResultJson=new Json();
		String[] sectionArrs  =sections.split(",");
		Set<String> stuCodes = new HashSet<>();
		Set<String> stuIds = new HashSet<>();
		Set<String> roleIds = new HashSet<>();
		for(int i =0;i< datas.size();i++){
			String[] dataArr = datas.get(i);
			for (int j = 0;j<sectionArrs.length;j++) {
				if(StringUtils.isNotBlank(dataArr[j*3+2])) {
					stuCodes.add(dataArr[j*3+2]);
				}
			}
		}
		Map<String,Student> stuMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(stuCodes)) {
			List<Student> stulist = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(schoolId, stuCodes.toArray(new String[0])), new TR<List<Student>>() {});
			stuMap = EntityUtils.getMap(stulist, "studentCode");
		}
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		List<Clazz> clazzlist = SUtils.dt(classRemoteService.findByIdCurAcadyear(schoolId,acadyear), new TR<List<Clazz>>(){});
		//取出最大周次
		List<DateInfo> infos = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(schoolId, acadyear, NumberUtils.toInt(semester)), new TR<List<DateInfo>>(){});
		int maxWeek = 0;
		for(DateInfo dateInfo : infos){
			if(dateInfo.getWeek() > maxWeek){
				maxWeek = dateInfo.getWeek();
			}
		}
//		Map<String, McodeDetail> mcodeDetailMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-RKXD"), new TypeReference<Map<String, McodeDetail> >(){});
		Map<String,Clazz> clazzMap = EntityUtils.getMap(clazzlist, "classNameDynamic"); 
		//取出该学期下的老数据，更新是需要先删除老数据
		List<DyWeekCheckRoleUser> roleUsers = findByRoleType(schoolId,DyWeekCheckRoleUser.CHECK_CLASS,acadyear,semester);
		Set<DyWeekCheckRoleUser> deleteIds = new HashSet<DyWeekCheckRoleUser>();
		List<DyWeekCheckRoleUser> roleList = new ArrayList<DyWeekCheckRoleUser>();
		List<DyWeekCheckRoleStudent> roleStuList = new ArrayList<>();
		Map<String,String> weekData = new HashMap<>();
		DyWeekCheckRoleUser role = null;
		DyWeekCheckRoleStudent roleStu = null;
		String[] errorData=null;//new String[4]
		boolean hasError = false;
		for(int i =0;i< datas.size();i++){
			String[] dataArr = datas.get(i);
			if(StringUtils.isBlank(dataArr[0])){
				errorData = new String[4];
				errorData[0]=errorDataList.size() +1 +"";
				errorData[1]="周次";
				errorData[2]="";
				errorData[3]="周次不能为空";
				errorDataList.add(errorData);
				continue;
			}
			int week = NumberUtils.toInt(dataArr[0]);
			if(week<1 || week>maxWeek){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="周次";
				errorData[2]=dataArr[0];
				errorData[3]="周次不在有效范围内，有效范围为1~"+maxWeek;
				errorDataList.add(errorData);
				continue;
			}
			hasError = false;//一行有数据没有通过校验，则这一整行的各个学段的数据都不能导入
			for(int j = 0;j<sectionArrs.length;j++){
				String section = sectionArrs[j];
				if(dataArr.length > (j*3+1)&&StringUtils.isNotBlank(dataArr[j*3+1])){
					if(!clazzMap.containsKey(dataArr[j*3+1].trim())){
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="班级";
						errorData[2]=dataArr[j*3+1];
						errorData[3]="本学期内找不到该班级！";
						errorDataList.add(errorData);
						hasError = true;
						break;
					}
					Clazz cls = clazzMap.get(dataArr[j*3+1].trim());
//					if(!StringUtils.equals(section, cls.getSection()+"")){
//						errorData = new String[4];
//						errorData[0]=errorDataList.size()+1+"";
//						errorData[1]="班级";
//						errorData[2]=dataArr[j*3+1];
//						errorData[3]="该班级学段与要值周的学段范围不匹配！";
//						errorDataList.add(errorData);
//						hasError = true;
//						break;
//					}
					if(StringUtils.isNotBlank(dataArr[j*3+2].trim())) {
						if(!stuMap.containsKey(dataArr[j*3+2])) {
							errorData = new String[4];
							errorData[0]=errorDataList.size()+1+"";
							errorData[1]="学号";
							errorData[2]=dataArr[j*3+2];
							errorData[3]="找不到该学号的学生！";
							errorDataList.add(errorData);
							hasError = true;
							break;
						}
						Student stu = stuMap.get(dataArr[j*3+2].trim());
						if(!StringUtils.equals(stu.getClassId(), cls.getId())) {
							errorData = new String[4];
							errorData[0]=errorDataList.size()+1+"";
							errorData[1]="学号";
							errorData[2]=dataArr[j*3+2];
							errorData[3]="该学号的学生不在所选择的班级内！";
							errorDataList.add(errorData);
							hasError = true;
							break;
						}
						if(StringUtils.isBlank(dataArr[j*3+3].trim())) {
							errorData = new String[4];
							errorData[0]=errorDataList.size()+1+"";
							errorData[1]="学生姓名";
							errorData[2]=dataArr[j*3+3];
							errorData[3]="请填写学生姓名！";
							errorDataList.add(errorData);
							hasError = true;
							break;
						}
						if(!StringUtils.equals(stu.getStudentName(), dataArr[j*3+3].trim())) {
							errorData = new String[4];
							errorData[0]=errorDataList.size()+1+"";
							errorData[1]="学生姓名";
							errorData[2]=dataArr[j*3+3];
							errorData[3]="该学号的学生姓名与所填学生不符！";
							errorDataList.add(errorData);
							hasError = true;
							break;
						}
					}
				}
			}
			if(hasError){
				continue;
			}
			for(int j = 0;j<sectionArrs.length;j++){
				String section = sectionArrs[j];
				if(dataArr.length > (j*3+3)) {
					//TODO 如果有此学段列 则需要进行操作（删除和保存）
					if(StringUtils.isNotBlank(dataArr[j*3+2])) {
						//保存操作
						if(!weekData.containsKey(section+""+week)) {
							role = new DyWeekCheckRoleUser();
							role.setId(UuidUtils.generateUuid());
							role.setAcadyear(acadyear);
							role.setSemester(semester);
							role.setSection(section);
							role.setSchoolId(schoolId);
							role.setRoleType(DyWeekCheckRoleUser.CHECK_CLASS);
							role.setWeek(week);
							roleList.add(role);
							weekData.put(section+""+week, role.getId());
						}
						String roleId = weekData.get(section+""+week);
						Student stu = stuMap.get(dataArr[j*3+2].trim());
						if(!stuIds.contains(week+""+section+stu.getId())) {
							roleStu = new DyWeekCheckRoleStudent();
							roleStu.setAcadyear(acadyear);
							roleStu.setSemester(semester);
							roleStu.setStudentId(stu.getId());
							roleStu.setSchoolId(schoolId);
							roleStu.setClassId(stu.getClassId());
							roleStu.setSection(section);
							roleStu.setRoleId(roleId);
							roleStu.setWeek(week);
							roleStuList.add(roleStu);
						}
					}
					//检查数据库中是否存在该数据
					for(DyWeekCheckRoleUser oldRole : roleUsers){
						if(StringUtils.equals(oldRole.getSection(), section) && week == oldRole.getWeek()){
							deleteIds.add(oldRole);
							roleIds.add(oldRole.getId());
						}
					}
				}
			}
			successCount++;
		}
		if(CollectionUtils.isNotEmpty(deleteIds)){
//			dyWeekCheckRoleUserDao.deleteByInIds(deleteIds.toArray(new String[0]));
			dyWeekCheckRoleUserDao.deleteAll(deleteIds);
			dyWeekCheckRoleStudentService.deleteByRoleId(roleIds.toArray(new String[0]));
		}
		if(CollectionUtils.isNotEmpty(roleList)){
			DyWeekCheckRoleUser[] roles = roleList.toArray(new DyWeekCheckRoleUser[0]);
			saveAll(roles);
			dyWeekCheckRoleStudentService.saveList(roleStuList);
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}
	
	@Override
	protected BaseJpaRepositoryDao<DyWeekCheckRoleUser, String> getJpaDao() {
		return dyWeekCheckRoleUserDao;
	}
	
	@Override
	protected Class<DyWeekCheckRoleUser> getEntityClass() {
		return DyWeekCheckRoleUser.class;
	}

}
