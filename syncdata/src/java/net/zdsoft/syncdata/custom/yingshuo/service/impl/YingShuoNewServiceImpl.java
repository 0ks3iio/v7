package net.zdsoft.syncdata.custom.yingshuo.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.SchoolSemester;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StusysSectionTimeSet;
import net.zdsoft.basedata.entity.SubSchool;
import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SchoolSemesterRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.StusysSectionTimeSetRemoteService;
import net.zdsoft.basedata.remote.service.SubSchoolRemoteService;
import net.zdsoft.basedata.remote.service.TeachAreaRemoteService;
import net.zdsoft.basedata.remote.service.TeachBuildingRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.syncdata.custom.yingshuo.constant.YingShuoNewConstant;
import net.zdsoft.syncdata.custom.yingshuo.service.YingShuoNewService;
import net.zdsoft.syncdata.util.JledqSyncDataUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.util.TypeUtils;

@Service("yingShuoNewService")
public class YingShuoNewServiceImpl implements YingShuoNewService {

	private static RestTemplate restTemplate = new RestTemplate();
	private Logger log = Logger.getLogger(YingShuoServiceImpl.class);
	@Autowired
    private BaseSyncSaveRemoteService baseSyncSaveService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeachAreaRemoteService teachAreaRemoteService;
	@Autowired
	private TeachBuildingRemoteService teachBuildingRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private SchoolSemesterRemoteService schoolSemesterRemoteService;
	@Autowired
	private StusysSectionTimeSetRemoteService stusysSectionTimeSetRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteServicer;
	@Autowired
	private SubSchoolRemoteService subSchoolRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	
	@Override
	public void saveSchool() {
		try {
			List<Unit> regionUnits = SUtils.dt(unitRemoteService.findByRegionCode(YingShuoNewConstant.YS_REGION_CODE_VAL),
					new TR<List<Unit>>() {});
			Map<String, Unit> uidMap = EntityUtils.getMap(regionUnits, Unit::getId);
			Set<String> sidSet = EntityUtils.getSet(regionUnits, Unit::getId);
			List<School> allSchool = SUtils.dt(schoolRemoteService.findListByIds(sidSet.toArray(new String[sidSet.size()])),
					new TR<List<School>>() {});
			Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getId);
			
			List<SubSchool> subSchools = SUtils.dt(subSchoolRemoteService.findAll(), new TR<List<SubSchool>>() {
			});
			Map<String, SubSchool> subSchoolMap = EntityUtils.getMap(subSchools, SubSchool::getId);
			
			Semester semester = getCurrentSemester();
//			if (semester == null) {
//				json.put("resultMsg", "获取不到学年学期数据！");
//				return json.toJSONString();
//			}
			Map<String, StusysSectionTimeSet> timeMap = null;
			if(CollectionUtils.isNotEmpty(sidSet)){
				List<StusysSectionTimeSet> times = SUtils.dt(stusysSectionTimeSetRemoteService
						.findByUnitIdIn(sidSet.toArray(new String[sidSet.size()])),StusysSectionTimeSet.class);
				timeMap = EntityUtils.getMap(times, StusysSectionTimeSet::getId);
			}
//			TODO 根据教育据code 来获取学校信息 ，改为 根据 用户信息来获取 
			String cc = getDate (YingShuoNewConstant.YS_GET_USER_LIST_URL,StringUtils.EMPTY,Boolean.TRUE);
			String eduCode = YingShuoNewConstant.YS_EDU_ID;
			JSONArray array = Json.parseArray(cc);
			log.info("获取学校的信息数据为------------" + array.size());
			//先得到对应的idList
			List<School> saveSchools = new ArrayList<>();
			List<Unit> saveUnits = new ArrayList<>();
			List<SubSchool> saveSubSchools = new ArrayList<>();
			List<StusysSectionTimeSet> saveStusysSectionTimeSets = new ArrayList<>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String fkCode = js.getString("schoolFkCode");
				String unitId = doChangeId(fkCode);
				//-----根据外键得到 学校，校区， 日期的信息 ------
				String url = getUrl(YingShuoNewConstant.YS_GET_UNIT_URL, "school_fkcode", fkCode);
				String cc1 = getDate (url,StringUtils.EMPTY,Boolean.TRUE);
				JSONObject jsonObject = Json.parseObject(cc1);
				//得到学校信息
				doInstallSchool(regionUnits, uidMap, allSchoolMap, eduCode,
						saveSchools, saveUnits, unitId, jsonObject);
				//得到校区信息
				doInstallSubSchool(subSchoolMap, saveSubSchools,unitId, jsonObject);
				//保存 学校节次时间   stusys_section_time_set
				doInstallSectionTime(semester, timeMap,
						saveStusysSectionTimeSets, unitId, jsonObject);
			}
			if(CollectionUtils.isNotEmpty(saveSchools))
				baseSyncSaveService.saveSchool(saveSchools.toArray(new School[saveSchools.size()]));
			if(CollectionUtils.isNotEmpty(saveUnits))
				baseSyncSaveService.saveUnit(saveUnits.toArray(new Unit[saveUnits.size()]));
			if(CollectionUtils.isNotEmpty(saveSubSchools))
				baseSyncSaveService.saveSubSchool(saveSubSchools.toArray(new SubSchool[saveSubSchools.size()]));
			if(CollectionUtils.isNotEmpty(saveStusysSectionTimeSets))
				baseSyncSaveService.saveStusysSectionTimeSet(saveStusysSectionTimeSets.toArray(new StusysSectionTimeSet[saveStusysSectionTimeSets.size()]));
		} catch (Exception e) {
			log.error("鹰硕同步学校数据失败-------" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveDept() {
		// TODO Auto-generated method stub
		List<Dept> allDept = SUtils.dt(deptRemoteService.findAll(), new TR<List<Dept>>() {});
        Map<String, Dept> deptMap = JledqSyncDataUtil.getMap(allDept, "id", StringUtils.EMPTY);
		List<Unit> allSchool = SUtils.dt(unitRemoteService.findByRegionCode(YingShuoNewConstant.YS_REGION_CODE_VAL),
				new TR<List<Unit>>() {});
		Set<String> unitIds = EntityUtils.getSet(allSchool, Unit::getId);
		List<Dept> saveDepts = new ArrayList<Dept>();
		unitIds.forEach(c->{
			String url = getUrl(YingShuoNewConstant.YS_GET_DEPT_URL, "school_fkcode", getFkId(c));
			String cc =  getDate (url,StringUtils.EMPTY,Boolean.TRUE);
			JSONArray array = Json.parseArray(cc);
			if(array != null){
				for (int i = 0; i < array.size(); i++) {
					JSONObject dept = array.getJSONObject(i);
					String num = dept.getString("num");
					if(StringUtils.isNotBlank(num) && "2".equals(num)){
						String id = doChangeId(dept.getString("id"));
						String deptName = dept.getString("name");
						String areaId = doChangeId(dept.getString("pid"));
						Dept d;
						if(deptMap != null && !deptMap.isEmpty() && deptMap.get(id) != null){
							d = deptMap.get(id);
						}else{
							d = new Dept();
							d.setId(id);
						}
						Long max = RedisUtils
								.incrby("syncdata.bjdy.all.dept.code.max", 1);
						String deptCode = max+ "";
						d.setDeptCode(StringUtils.leftPad(deptCode, 6, "0"));
						d.setDeptName(deptName);
						d.setAreaId(areaId);
						d.setUnitId(c);
						saveDepts.add(d);
					}
				}
			}
		});
		if(CollectionUtils.isNotEmpty(saveDepts)){
			baseSyncSaveService.saveDept(saveDepts.toArray(new Dept[saveDepts.size()]));
		}
	}

	@Override
	public void saveTeacher(String unitId) {
		// TODO Auto-generated method stub
		List<Teacher> allUnitTeacher = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {});
		Map<String,Teacher> teacherMap = EntityUtils.getMap(allUnitTeacher, Teacher::getId);
		List<User> allTeacherList = SUtils.dt(userRemoteService.findByUnitIdAndOwnerTypeAll(unitId,User.OWNER_TYPE_TEACHER),new TR<List<User>>() {});
		Map<String,User> allUserMap = EntityUtils.getMap(allTeacherList, User::getOwnerId);
		List<Dept> allDepts = SUtils.dt(deptRemoteService.findByUnitId(unitId),new TR<List<Dept>>() {});
		Map<String, List<Dept>> deptMap = EntityUtils.getListMap(allDepts, Dept::getUnitId, Function.identity());
		List<Teacher> saveTeacherList = new ArrayList<>();
		List<User> saveUserList = new ArrayList<>();
		String url = getUrl(YingShuoNewConstant.YS_GET_TEACHER_URL, "school_fkcode", getFkId(unitId));
		String cc =  getDate (url,Boolean.TRUE);
		JSONArray array = Json.parseArray(cc);
		if(array != null){
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String tid = doChangeId(js.getString("fkcode"));
				String teacherName = js.getString("worker_name");
				Integer sex = doChangeSex(js.getString("worker_gender"));
				String username = doChargeUserName(js.getString("user_account"));
				String identityCard = js.getString("worker_id_code");
				String mobilePhone = js.getString("worker_tel");
				String teacherCode = js.getString("worker_code");
				if(StringUtils.isBlank(teacherCode)){
					teacherCode = "00001";
				}
				Date birthday = DateUtils.string2Date(js.getString("worker_birthday"));
				String cardNumber = js.getString("card_code");
				String usertype  = js.getString("identity");  //判断是否是管理员 ，待确定 TODO  5是管理员
				Integer isDeleted = doChargeIsDeleted(js.getString("del_status"));
				//-------封装教师数据
				Teacher t;
				if(teacherMap != null && !teacherMap.isEmpty() && teacherMap.get(tid) != null){
					t = teacherMap.get(tid);
					t.setModifyTime(new Date());
				}else{
					t = new Teacher();
					t.setId(tid);
					t.setCreationTime(new Date());
				}
				//设置排序号
				Long displayOrder;
				if(RedisUtils.get("syncdata.yingshuo.teacher.displayorder.max." + unitId) == null) {
					if(CollectionUtils.isEmpty(allUnitTeacher)) {
						displayOrder = (long)1;
					}else {
						displayOrder = (long) allUnitTeacher.size();
					}
				}
				displayOrder = RedisUtils
						.incrby("syncdata.yingshuo.teacher.displayorder.max." + unitId, 1);
				t.setDisplayOrder( t.getDisplayOrder() == null ? displayOrder.intValue() : t.getDisplayOrder());
				t.setIsDeleted(isDeleted);
				t.setTeacherCode(teacherCode);
				t.setUnitId(unitId);
				t.setSex(sex);
				t.setTeacherName(teacherName);
				if(StringUtils.isNotBlank(identityCard) && identityCard.length() > 18 ){
					identityCard = identityCard.substring(0, 18);
				}
				t.setIdentityCard(identityCard);
				t.setMobilePhone(mobilePhone);
				t.setBirthday(birthday);
				t.setCardNumber(cardNumber);
				saveTeacherList.add(t);
//					----------封装用户	
				if(StringUtils.isNotBlank(username)){
					User user;
					if(allUserMap != null && !allUserMap.isEmpty() && allUserMap.get(tid) != null){
						user = allUserMap.get(tid);
					}else{
						user = new User();
						user.setId(UuidUtils.generateUuid());
						user.setPassword(new PWD(YingShuoNewConstant.YS_DEFAULT_PASS_WORD_VALUE).encode());
						user.setOwnerType(User.OWNER_TYPE_TEACHER);
//					user.setRegionCode(YingShuoNewConstant.YS_REGION_CODE_VAL);
						user.setUsername(username);
					}
					user.setOwnerId(tid);
					user.setUnitId(unitId);
					user.setSex(t.getSex());
					user.setRealName(t.getTeacherName());
					//判断是否是管理员
					if("5".equals(usertype)){
						user.setUserType(User.USER_TYPE_UNIT_ADMIN);
					}else{
						user.setUserType(User.USER_TYPE_COMMON_USER);
					}
					user.setDisplayOrder(t.getDisplayOrder());
					user.setIsDeleted(t.getIsDeleted());
					user.setIdentityCard(t.getIdentityCard());
					user.setMobilePhone(mobilePhone);
					user.setBirthday(t.getBirthday());
					saveUserList.add(user);
				}
			}
			for (Teacher teacher : saveTeacherList) {
				if(teacherMap == null || teacherMap.isEmpty() || teacherMap.get(teacher.getId()) == null){
					String uId = teacher.getUnitId();
					if(deptMap != null || deptMap.get(uId) != null){				
						List<Dept> depts = deptMap.get(uId);
						for (Dept dept : depts) {
							if(dept.getIsDefault() != null && dept.getIsDefault() == 1){
								teacher.setDeptId(dept.getId());
								break;
							}
						}
					}
				}
			}
		}
		//进行保存教师和学生数据
		if(CollectionUtils.isNotEmpty(saveTeacherList)){
			if(CollectionUtils.isNotEmpty(saveUserList)){
				baseSyncSaveService.saveTeacherAndUser(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]),saveUserList.toArray(new User[saveUserList.size()]));
			}else{
				baseSyncSaveService.saveTeacher(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]));
			}
		}
	}

	@Override
	public void saveStudent(String unitId) {
		// TODO Auto-generated method stub
		List<Student> studentList=SUtils.dt(studentRemoteService.findBySchoolId(unitId), new TR<List<Student>>(){});
		Map<String, Student> allStuMap = EntityUtils.getMap(studentList, Student::getId);
		List<User> allStuList = SUtils.dt(userRemoteService.findByUnitIdAndOwnerTypeAll(unitId,User.OWNER_TYPE_STUDENT),new TR<List<User>>() {});
		Map<String,User> allUserMap = EntityUtils.getMap(allStuList, User::getOwnerId);
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdIn(new String[]{unitId}), Clazz.class);
		List<Student> saveStudentList = new ArrayList<>();
		List<User> saveUserList = new ArrayList<>();
		for (Clazz clazz : clazzList) {
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("school_fkcode", unitId);
			paramMap.put("class_fkcode", clazz.getId());
			String cc = getDate (YingShuoNewConstant.YS_GET_STUDENT_URL,getBodyParam(paramMap));
			JSONArray array = Json.parseArray(cc);
			if(array != null){
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String studentId = doChangeId(js.getString("fkcode"));
					String classId   = clazz.getId();
					String studentName = js.getString("stu_name");
					if(StringUtils.isBlank(studentName)){
						System.out.println("学生的姓名为空的值----" + studentId);
						continue;
					}
					Integer sex = doChangeSex(js.getString("stu_gender"));
					String username = doChargeUserName(js.getString("account"));
					String identityCard = StringUtils.trimToEmpty(js.getString("stu_id_code"));
					Integer isDeleted = doChargeIsDeleted(js.getString("del_status"));
					Integer isLeaveSchool = doChargeIsLeaveSchool(js.getString("school_rool_status"));
					if(StringUtils.isNotBlank(identityCard) && identityCard.length() > 30){
						identityCard = identityCard.substring(0, 29);
					}
					String studentCode = StringUtils.trimToEmpty(js.getString("school_roll_code"));
					if(StringUtils.isBlank(studentCode)){
						studentCode = StringUtils.isBlank(identityCard) ? YingShuoNewConstant.YS_DEFAULT_STU_CODE 
								: identityCard;
					}
					if(StringUtils.isNotBlank(studentCode) && studentCode.length() > 20){
						studentCode = studentCode.substring(0, 19);
					}
//				----补充字段
					String unitiveCode = js.getString("school_roll_code"); //学籍号
					unitiveCode = StringUtils.trimToEmpty(unitiveCode);
					if(StringUtils.isNotBlank(unitiveCode) && unitiveCode.length() > 30){
						unitiveCode = unitiveCode.substring(0, 29); 
					}
					String mobilePhone = js.getString("tel");
					String cardNumber = js.getString("card_code");
					
//				--------封装学生数据
					Student student;
					if(allStuMap != null && !allStuMap.isEmpty() && allStuMap.get(studentId) != null){
						student = allStuMap.get(studentId);
					}else{
						student = new Student();
						student.setId(studentId);
						student.setCreationTime(new Date());
					}
					student.setSchoolId(unitId);
					student.setClassId(classId);
					student.setStudentName(studentName);
					student.setSex(sex);
					student.setStudentCode(studentCode);
					student.setUnitiveCode(unitiveCode);
					student.setIdentityCard(identityCard);
					student.setIsDeleted(isDeleted);
					student.setMobilePhone(mobilePhone);
					student.setCardNumber(cardNumber);
					student.setIsLeaveSchool(isLeaveSchool);
					student.setModifyTime(new Date());
					saveStudentList.add(student);
//				----------封装用户	
					if(StringUtils.isNotBlank(username)){
						User user;
						if(allUserMap != null && !allUserMap.isEmpty() && allUserMap.get(studentId) != null){
							user = allUserMap.get(studentId);
						}else{
							user = new User();
							user.setId(UuidUtils.generateUuid());
							user.setPassword(new PWD(YingShuoNewConstant.YS_DEFAULT_PASS_WORD_VALUE).encode());
							user.setOwnerType(User.OWNER_TYPE_STUDENT);
//						user.setRegionCode(YingShuoNewConstant.YS_REGION_CODE_VAL);
							user.setUserType(User.USER_TYPE_COMMON_USER);
							user.setUsername(username);
							user.setCreationTime(new Date());
						}
						user.setOwnerId(studentId);
						user.setUnitId(unitId);
						user.setSex(student.getSex());
						user.setRealName(student.getStudentName());
						user.setIsDeleted(student.getIsDeleted());
						user.setIdentityCard(student.getIdentityCard());
						user.setMobilePhone(student.getMobilePhone());
						user.setModifyTime(new Date());
						saveUserList.add(user);
					}
				}
			}
			//进行保存教师和学生数据
			if(CollectionUtils.isNotEmpty(saveStudentList)){
				if(CollectionUtils.isNotEmpty(saveUserList)){
					baseSyncSaveService.saveStudentAndUser(saveStudentList.toArray(new Student[saveStudentList.size()]),saveUserList.toArray(new User[saveUserList.size()]));
				}else{
					baseSyncSaveService.saveStudent(saveStudentList.toArray(new Student[saveStudentList.size()]));
				}
			}
		}
	}

	@Override
	public void saveClass() {
		// TODO Auto-generated method stub
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
		if (semester == null) {
			log.error("------ 请先维护学年学期 -------");
			return;
		}
		List<School> allSchool = SUtils.dt(schoolRemoteService.findByRegionCodes(YingShuoNewConstant.YS_REGION_CODE_VAL),
				new TR<List<School>>() {});
		Set<String> setUtils = EntityUtils.getSet(allSchool, School::getId);
		List<Clazz> allClazzs = SUtils.dt(classRemoteService.findAllBySchoolIdIn(
				setUtils.toArray(new String[setUtils.size()])), Clazz.class);
		Map<String, Clazz> allClazzMap = EntityUtils.getMap(allClazzs, Clazz::getId);
		List<SubSchool> subSchools = SUtils.dt(subSchoolRemoteService.findAll(), new TR<List<SubSchool>>() {
		});
		Map<String, SubSchool> subSchoolMap = EntityUtils.getMap(subSchools, SubSchool::getId);
		List<Clazz> saveClazzList = new ArrayList<>();
		List<Grade> saveGradeList = new ArrayList<Grade>();
		if(CollectionUtils.isNotEmpty(allSchool)){
			for (School school : allSchool) {
				String schoolId = school.getId();
				String url = getUrl(YingShuoNewConstant.YS_GET_CLAZZ_URL, "school_fkcode", getFkId(schoolId));
				String data =  getDate (url,Boolean.TRUE);
				JSONArray array = Json.parseArray(data);
				Map<String, Grade> gradeMap = new HashMap<>();
				if(array != null){
					for (int i = 0; i < array.size(); i++) {
						JSONObject js = array.getJSONObject(i);
						//判断是否是我们推送过去的班级数据
						String wpClassId = js.getString("wp_class_id");
						String clazzId;
						if(StringUtils.isNotBlank(wpClassId)){
							clazzId = doChangeId(wpClassId);
						}else{
							clazzId = doChangeId(js.getString("fkcode"));
						}
						String className = js.getString("class_name");
						String campusId  = doChangeId(js.getString("campus_fkcode"));  //校区
						String section = doChargeSection(js.getString("school_stage"));
						if(StringUtils.isBlank(section)){
							section = "1";
						}
					    String teacherId = doChangeId(js.getString(" head_teacher_fkcode")); // 班主任id
//					    Timestamp creyear = js.getTimestamp("start_year");
//						Date creyear = js.getDate("start_year"); //学年
					    String creyear = js.getString("start_year");
						String startYear;
						if(creyear == null){
							Calendar date = Calendar.getInstance();
							startYear = String.valueOf(date.get(Calendar.YEAR));
						}else{
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
							startYear = dateFormat.format(new Date(Long.parseLong(creyear)*1000));
						}
						String acadyear = startYear + "-" + String.valueOf(Integer.valueOf(startYear) + 1);
						//转化学制
						Integer schoolingLength = (Integer.valueOf(section) == 1 ? 6 : 3);
						
						String gradeName = js.getString("grade_name");
						Integer isDeleted = doChargeIsDeleted(js.getString("del_status"));
//			----------封装班级数据
						Integer isGraduate = null;
						Clazz clazz;
						if(allClazzMap != null && !allClazzMap.isEmpty() && allClazzMap.get(clazzId) != null){
							clazz = allClazzMap.get(clazzId);
							clazz.setModifyTime(new Date());
							isGraduate = clazz.getIsGraduate();
						}else{
							clazz = new Clazz();
							clazz.setId(clazzId);
							clazz.setCreationTime(new Date());
						}
						clazz.setSchoolId(schoolId);
						if(StringUtils.isNotBlank(campusId) && !subSchoolMap.isEmpty() && subSchoolMap.get(campusId) != null){
							className = "(" + subSchoolMap.get(campusId).getName() + ")" + className;
						}
						clazz.setClassName(className);
						clazz.setCampusId(campusId);
						clazz.setSection(Integer.valueOf(section));
						clazz.setSchoolingLength(schoolingLength);
						clazz.setAcadyear(acadyear);
                        clazz.setIsDeleted(isDeleted);
						//班号我们自己生成
						String classCodePrefix = StringUtils.substring(clazz.getAcadyear(), 0, 4)
								+ StringUtils.leftPad(clazz.getSection() + "", 2, "0");
						Long max = RedisUtils
								.incrby("syncdata.yingshuo.class.code.max." + clazz.getSchoolId() + classCodePrefix, 1);
						clazz.setClassCode(classCodePrefix + StringUtils.leftPad("" + max, 2, "0"));
						clazz.setTeacherId(teacherId);
//			-----------封装年级数据
						int year = NumberUtils.toInt(StringUtils.substring(clazz.getAcadyear(), 0, 4));
						int year2 = NumberUtils.toInt(StringUtils.substring(semester.getAcadyear(), 0, 4));
						if(semester.getSemester() == 2){
							year2 = NumberUtils.toInt(StringUtils.substring(semester.getAcadyear(), 5));
						}
						Integer yearNum = null;
						String monthTime = "0910";  //班级的新建时间暂定是9月1号
						String nowCreatime = new SimpleDateFormat("MMdd").format(new Date());
						if(NumberUtils.toInt(nowCreatime) > NumberUtils.toInt(monthTime)){
							year2++;
						}
						if (year2 > (year + clazz.getSchoolingLength())) {
							if(isGraduate == null){
								clazz.setIsGraduate(1);
							}
							clazz.setGraduateDate(new Date());
							yearNum = 6;
						} else {
							yearNum = year2 - year;
							if(yearNum == 0 ){
								yearNum = 1;
							}
							if(isGraduate == null){
								clazz.setIsGraduate(0);
							}
						}
						String gradesJson = gradeRemoteService.findBySchoolId(clazz.getSchoolId(),
								ArrayUtils.toArray(clazz.getSection()), clazz.getAcadyear(), true);
						List<Grade> gradesExists = SUtils.dt(gradesJson, new TypeReference<List<Grade>>() {
						});
						for (Grade grade1 : gradesExists) {
							String key = grade1.getSchoolId() + grade1.getOpenAcadyear() + grade1.getSection();
							gradeMap.put(key, grade1);
						}
						//根据学段和当前学期之间的时间差来得到gradename ,gradecode
//						String gradeBefore;
//						String gradeYear = yearNum == 1 ? "一" : yearNum == 2 ? "二" : yearNum == 3 ? "三" :
//							yearNum == 4 ? "四" : yearNum == 5 ? "五" : "六";
//						if(clazz.getSection() == 1){
//							gradeBefore = "小";
//						}else if (clazz.getSection() == 2) {
//							gradeBefore = "初";
//							if(yearNum > 3){
//								gradeYear = "三";
//							}
//						}else {
//							gradeBefore = "高";
//							if(yearNum > 3){
//								gradeYear = "三";
//							}
//						}
//						String gradeName = gradeBefore + gradeYear;
						String gradeCode = ""+clazz.getSection() + yearNum;
						//年级名称的转化
						Grade grade1 = gradeMap.get(clazz.getSchoolId() + clazz.getAcadyear() + clazz.getSection());
						if (grade1 == null) {
							grade1 = new Grade();
							grade1.setGradeName(gradeName);
							grade1.setCreationTime(clazz.getCreationTime());
							grade1.setModifyTime(clazz.getModifyTime());
							grade1.setEventSource(1);
							grade1.setAmLessonCount(5);
							grade1.setPmLessonCount(4);
							grade1.setOpenAcadyear(clazz.getAcadyear());
							grade1.setNightLessonCount(3);
							grade1.setGradeCode(gradeCode);
							grade1.setSubschoolId(clazz.getCampusId());
							grade1.setSchoolId(clazz.getSchoolId());
							grade1.setIsGraduate(0);
							grade1.setSection(clazz.getSection());
							grade1.setSchoolingLength(clazz.getSchoolingLength());
							if (grade1.getSchoolingLength() == null || grade1.getSchoolingLength() == 0) {
								grade1.setSchoolingLength(grade1.getSection() == 1 ? 6 : 3);
							}
							grade1.setId(UuidUtils.generateUuid());
							saveGradeList.add(grade1);
							gradeMap.put(grade1.getSchoolId() + grade1.getOpenAcadyear() + grade1.getSection(), grade1);
						}
						clazz.setGradeId(grade1.getId());
						saveClazzList.add(clazz);
					}
				}
		    }
			//进行保存数据
			if(CollectionUtils.isNotEmpty(saveClazzList)) {
				baseSyncSaveService.saveClass(saveClazzList.toArray(new Clazz[saveClazzList.size()]));
			}
			if (CollectionUtils.isNotEmpty(saveGradeList)) {
				baseSyncSaveService.saveGrade(saveGradeList.toArray(new Grade[saveGradeList.size()]));
			}
		}
	}

	@Override
	public void saveCourse() {
		List<School> allSchool = SUtils.dt(schoolRemoteService.findByRegionCodes(YingShuoNewConstant.YS_REGION_CODE_VAL),
				new TR<List<School>>() {});
		Set<String> setUids = EntityUtils.getSet(allSchool, School::getId);
		Map<String, String> sectionMap = EntityUtils.getMap(allSchool, School::getId, School::getSections);
		List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdIn(setUids.toArray(new String[setUids.size()])), Course.class);
		Map<String, Course> allCourseMap = EntityUtils.getMap(courseList, Course::getId);
		Unit topUnit = unitRemoteService.findTopUnitObject(StringUtils.EMPTY);
		List<Course> topCourseList = SUtils.dt(courseRemoteService.findByUnitIdIn(topUnit.getId()), Course.class);
		Map<String,Course> subjectNameMap = EntityUtils.getMap(topCourseList, Course::getSubjectName);
		List<Course> saveCourseList = new ArrayList<>();
		setUids.forEach(c->{
			String url = getUrl(YingShuoNewConstant.YS_GET_COURSE_URL, "school_fkcode", getFkId(c));
			String cc =  getDate (url,Boolean.TRUE);
			JSONArray array = Json.parseArray(cc);
			if(array != null){
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String id = doChangeId(js.getString("fkcode"));
					String subjectCode = js.getString("subjectCode");
					String subjectName = js.getString("course_name");
					String subjectType = String.valueOf(Unit.UNIT_CLASS_SCHOOL);
					String type = js.getString("course_type");
					Integer isDeleted = doChargeIsDeleted(js.getString("del_status"));
					Integer isUsing = doChargeIsUsing(js.getString("del_status"));
					
					//名称和我们顶级单位的科目名称重复不添加
					if(StringUtils.isNotBlank(subjectName) && subjectNameMap.get(subjectName) == null){
						//     -------封装course数据	
						Course course;
						if(allCourseMap != null && allCourseMap.get(id) != null) {
							course = allCourseMap.get(id);
							course.setModifyTime(new Date());
						}else{
							course = new Course();
							course.setId(id);
							course.setCreationTime(new Date());
						}
						//设置排序号
						Long displayOrder;
						if(RedisUtils.get("syncdata.yingshuo.course.displayorder.max." + c) == null) {
							if(CollectionUtils.isEmpty(courseList)) {
								displayOrder = (long)1;
							}else {
								displayOrder = (long) courseList.size();
							}
						}
						displayOrder = RedisUtils
								.incrby("syncdata.yingshuo.course.displayorder.max." + c, 1);
						course.setIsDeleted(isDeleted);
						course.setSubjectName(subjectName);
						course.setSection(sectionMap.get(c));
						course.setIsUsing(isUsing);
						course.setType(type);
						course.setSubjectType(subjectType);
						course.setUnitId(c);
						if(StringUtils.isBlank(course.getSubjectCode())){
							course.setSubjectCode("YS_S" + displayOrder);
						}
						saveCourseList.add(course);
					}
				}
			}
		});
		if (CollectionUtils.isNotEmpty(saveCourseList)) {
			baseSyncSaveService.saveCourse(saveCourseList.toArray(new Course[saveCourseList.size()]));
		}
	}

	@Override
	public void saveBuilding() {
		// TODO Auto-generated method stub
		List<School> allSchool = SUtils.dt(schoolRemoteService.findByRegionCodes(YingShuoNewConstant.YS_REGION_CODE_VAL),
				new TR<List<School>>() {});
		Set<String> setUids = EntityUtils.getSet(allSchool, School::getId);
		List<SubSchool>  subSchools = SUtils.dt(subSchoolRemoteService.findbySchoolIdIn(setUids.toArray(new String[setUids.size()])),new TR<List<SubSchool>>() {});
		Set<String> subIds = EntityUtils.getSet(subSchools, SubSchool::getId);
		Map<String, String> uidMap = EntityUtils.getMap(subSchools, SubSchool::getId, SubSchool::getSchoolId);
		List<TeachBuilding> teachBuildings = SUtils.dt(teachBuildingRemoteService.findByUnitIdIn(setUids.toArray(new String[setUids.size()])), 
				new TR<List<TeachBuilding>>() {});
		Map<String, TeachBuilding> allTeachBuildingMap = EntityUtils.getMap(teachBuildings, TeachBuilding::getId);
		List<TeachBuilding> saveTeachBuildings = new ArrayList<>();
		subIds.forEach(c->{
			String url = getUrl(YingShuoNewConstant.YS_GET_BUILDING_URL, "campus_fkcode", getFkId(c));
			String cc = getDate (url,Boolean.TRUE);
			JSONArray array = Json.parseArray(cc);
			if(array != null){
				for (int i = 0; i < array.size(); i++) {
					JSONObject build = array.getJSONObject(i);
					// 开始 ---------封装TeachBuilding
					String id = doChangeId(build.getString("fkcode"));
					String floorCount = build.getString("building_layer");
					if(StringUtils.isBlank(floorCount)){
						floorCount = "1";
					}
					String buildName  = build.getString("building_name");
					String buildingType = doChargeBuildingType(build.getString("building_type"));
					Integer isDeleted = YingShuoNewConstant.BOOLEAN_FALSE_VALUE;
					if(StringUtils.isNotBlank(build.getString("del_status")) && 
							("3".equals(build.getString("del_status")) || "1".equals(build.getString("del_status")))){
						isDeleted = YingShuoNewConstant.BOOLEAN_TRUE_VALUE;
					}
					String teachAreaId = c;
					TeachBuilding teachBuilding;
					if(allTeachBuildingMap.get(id) != null) {
						teachBuilding = allTeachBuildingMap.get(id);
					}else {
						teachBuilding = new TeachBuilding();
						teachBuilding.setId(id);
					}
					//关键字段
					teachBuilding.setUnitId(uidMap.get(c));
					teachBuilding.setBuildingName(buildName);
					teachBuilding.setTeachAreaId(teachAreaId);
					teachBuilding.setIsDeleted(isDeleted);
					teachBuilding.setBuildingType(buildingType);
					teachBuilding.setFloorCount(Integer.valueOf(floorCount));
					saveTeachBuildings.add(teachBuilding);
					// 结束 ---------封装TeachBuilding
				}
			}
		});
		//保存楼层信息
		if(CollectionUtils.isNotEmpty(saveTeachBuildings)) {
			baseSyncSaveService.saveTeachBuilding(saveTeachBuildings.toArray(new TeachBuilding[0]));
		}
	}
	
	@Override
	public void savePlace() {
		// TODO Auto-generated method stub
		List<School> allSchool = SUtils.dt(schoolRemoteService.findByRegionCodes(YingShuoNewConstant.YS_REGION_CODE_VAL),
				new TR<List<School>>() {});
		Set<String> setUids = EntityUtils.getSet(allSchool, School::getId);
		List<TeachBuilding> teachBuildings = SUtils.dt(teachBuildingRemoteService.findByUnitIdIn(setUids.toArray(new String[setUids.size()])), 
				new TR<List<TeachBuilding>>() {});
		Set<String> buidIdSet = EntityUtils.getSet(teachBuildings, TeachBuilding::getId);
		Map<String, String> uidMap = EntityUtils.getMap(teachBuildings, TeachBuilding::getId, TeachBuilding::getUnitId);
		Map<String, String> teachAreaIdMap = EntityUtils.getMap(teachBuildings, TeachBuilding::getId, 
				TeachBuilding::getTeachAreaId);
		List<TeachPlace> teachPlaces = SUtils.dt(
				teachPlaceRemoteService.findByUnitIdIn(setUids.toArray(new String[setUids.size()])), 
				new TR<List<TeachPlace>>() {
		});
		Map<String, TeachPlace> allTeachPlaceMap = EntityUtils.getMap(teachPlaces, TeachPlace::getId);
		List<TeachPlace> saveTeachPlaces = new ArrayList<>();
		buidIdSet.forEach(c->{
			String url = getUrl(YingShuoNewConstant.YS_GET_PLACE_URL, "building_fkcode", getFkId(c));
			String cc = getDate (url,Boolean.TRUE);
			JSONArray array = Json.parseArray(cc);
			if(array != null){
				for (int i = 0; i < array.size(); i++) {
					// 开始---------转换字段
					JSONObject js = array.getJSONObject(i);
					String teachPlaceId = doChangeId(js.getString("fkcode"));
					String placeName = js.getString("classroom_name");
					Integer isDeleted = YingShuoNewConstant.BOOLEAN_FALSE_VALUE;
					if(StringUtils.isNotBlank(js.getString("del_status")) && 
							("3".equals(js.getString("del_status")) || "1".equals(js.getString("del_status")))){
						isDeleted = YingShuoNewConstant.BOOLEAN_TRUE_VALUE;
					}
					Integer floorNumber = js.getInteger("belong_layer");
					String teachAreaId = teachAreaIdMap.get(c);
					Integer placeNum = js.getInteger("classroom_capacity");
					String  placeType = doChargePlaceType(js.getString("classroom_type"));
					// 开始 ---------封装TeachPlace 
					TeachPlace teachPlace;
					if(allTeachPlaceMap.get(teachPlaceId) != null) {
						teachPlace = allTeachPlaceMap.get(teachPlaceId);
					}else {
						teachPlace = new TeachPlace();
						teachPlace.setId(teachPlaceId);
						teachPlace.setCreationTime(new Date());
//					teachPlace.setPlaceType("4");// 默认是教室
						teachPlace.setPlaceCode(teachPlaces.size()+i+1+"");
					}
					//关键字段
					teachPlace.setUnitId(uidMap.get(c));
					teachPlace.setDeleted(1 == isDeleted ? true : false );
					teachPlace.setTeachAreaId(teachAreaId);
					teachPlace.setPlaceName(placeName);
					teachPlace.setPlaceType(placeType);  //TODO 教师类型需要确定   参考微代码DM-CDLX;多选，半角逗号分隔
					//补充信息字段
					teachPlace.setModifyTime(new Date());
					teachPlace.setTeachBuildingId(c);
					teachPlace.setFloorNumber(floorNumber);
					teachPlace.setPlaceNum(placeNum);
					// 结束 ---------封装TeachPlace 	
					saveTeachPlaces.add(teachPlace);
				}
			}
		});
		//保存场地信息
		if(CollectionUtils.isNotEmpty(saveTeachPlaces)) {
			baseSyncSaveService.saveTeachPlace(saveTeachPlaces.toArray(new TeachPlace[0]));
		}
	}

	@Override
	public void saveSchoolSemester() {
		// TODO Auto-generated method stub
		List<School> allSchool = SUtils.dt(schoolRemoteService.findByRegionCodes(YingShuoNewConstant.YS_REGION_CODE_VAL),
				new TR<List<School>>() {});
		Set<String> schoolIds = EntityUtils.getSet(allSchool, School::getId);
		List<SchoolSemester> allSemesters = SUtils.dt(schoolSemesterRemoteService.findBySchoolIdIn(schoolIds.toArray(new String[schoolIds.size()])),
				new TR<List<SchoolSemester>>() {});
		Map<String, SchoolSemester> schoolSemesterMap = EntityUtils.getMap(allSemesters, SchoolSemester::getId);
		List<SchoolSemester> saveSchoolSemesters = new ArrayList<>();
		schoolIds.forEach(c->{
			String url = getUrl(YingShuoNewConstant.YS_GET_SCHOOL_SEMESTER_URL, "school_fkcode", getFkId(c));
			String cc = getDate (url,Boolean.TRUE);
			JSONArray array = Json.parseArray(cc);
			if (array != null) {
				for (int i = 0; i < array.size(); i++) {
					// 开始---------转换字段
					JSONObject js = array.getJSONObject(i);
					String id = doChangeId(js.getString("fkcode"));
					String acadyear = js.getString("school_year");
					Integer semester = doChargeSemester(js.getString("school_term_name"));
					String startDate = js.getString("start_date");
					Date semesterBegin = TypeUtils.castToDate(Long.parseLong(startDate)*1000);
					String endDate  = js.getString("end_date");
					Date semesterEnd = TypeUtils.castToDate(Long.parseLong(endDate)*1000);
					Integer isDeleted = YingShuoNewConstant.BOOLEAN_FALSE_VALUE;
					if(StringUtils.isNotBlank(js.getString("del_status")) && 
							("3".equals(js.getString("del_status")) || "1".equals(js.getString("del_status")))){
						isDeleted = YingShuoNewConstant.BOOLEAN_TRUE_VALUE;
					}
				    // 开始 ---------封装SchoolSemester 
					SchoolSemester schoolSemester;
					if(schoolSemesterMap.get(id) != null) {
						schoolSemester = schoolSemesterMap.get(id);
					}else {
						schoolSemester = new SchoolSemester();
						schoolSemester.setId(id);
						schoolSemester.setCreationTime(new Date());
					}
					schoolSemester.setAcadyear(acadyear);
					schoolSemester.setSemester(semester);
					schoolSemester.setSemesterBegin(semesterBegin);
					schoolSemester.setSemesterEnd(semesterEnd);
					schoolSemester.setModifyTime(new Date());
					schoolSemester.setIsDeleted(isDeleted);
					schoolSemester.setSchoolId(c);
					saveSchoolSemesters.add(schoolSemester);
				}
			}
		});
		//保存学校学年信息
		if(CollectionUtils.isNotEmpty(saveSchoolSemesters)) {
			baseSyncSaveService.saveSchoolSemester(saveSchoolSemesters.toArray(new SchoolSemester[0]));
		}
	}
	
	//----------------------------私有方法区 ------------------------------------------------------
	private  String getAccessToken() {
		String accessToken = RedisUtils.get(YingShuoNewConstant.YS_TOKEN_REDIS_KEY);
		if (StringUtils.isBlank(accessToken)) {
			ResponseEntity<String> responseBody = getResponseEntity();
			JSONObject json = Json.parseObject(responseBody.getBody());
			String date = getDate(json);
			   if(StringUtils.isNotBlank(date)){
				   JSONObject dataJson = Json.parseObject(date);
				   accessToken = dataJson.getString("token");
			   }
			//提前一分钟过期
			if(StringUtils.isNotBlank(accessToken))
			    RedisUtils.set(YingShuoNewConstant.YS_TOKEN_REDIS_KEY, accessToken, (int)TimeUnit.MINUTES.toSeconds(29));
		}
		return accessToken;
	}
	
	private ResponseEntity<String> getResponseEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", YingShuoNewConstant.YS_KEY_VALUE);
		jsonObject.put("security", YingShuoNewConstant.YS_SECURITY_VALUE);
		jsonObject.put("user_account", YingShuoNewConstant.YS_USER_NAME);
		HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toJSONString(), headers);
		ResponseEntity<String> responseBody = restTemplate.exchange(YingShuoNewConstant.YS_GET_TOKEN_URL, HttpMethod.POST, requestEntity, String.class);
		return responseBody;
	}
	
	private String getDate(String url,String bodyParam){
		return getDate(url,bodyParam,Boolean.FALSE);
	}
	
	private String getDate(String url,boolean isGet){
		return getDate(url,null,isGet);
	}
	
	private String getDate(String url, String bodyParam, boolean isGet) {
		  HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		  headers.set(YingShuoNewConstant.YS_TOKEN_NAME, getAccessToken());
		if(isGet){
			String jsonData = null;
			HttpEntity<String> requestEntity = new HttpEntity<String>(bodyParam, headers);
			ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			jsonData = responseBody.getBody();
			if(StringUtils.isBlank(jsonData))
				return null;
			JSONObject jsonObject = Json.parseObject(jsonData);
			return jsonObject.getString(YingShuoNewConstant.YS_RESOLVE_DATA_NAME);
		}else{
			HttpEntity<String> requestEntity = new HttpEntity<String>(bodyParam, headers);
			ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			JSONObject json = Json.parseObject(responseBody.getBody());
//			return getRecords(json);
			return json.getString(YingShuoNewConstant.YS_RESOLVE_DATA_NAME);
		}
	}
	
	
	private String getBodyParam(Map<String, String> paramMap){
		JSONObject jsonObject = new JSONObject();
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			jsonObject.put(entry.getKey(), getFkId(entry.getValue()));
		}
		return jsonObject.toJSONString();
	}
	
	private String getDate(JSONObject json){
		if(json != null){
			String code = json.getString(YingShuoNewConstant.YS_RESULT_CODE_NAME);
			if("0000".equals(code) || "ok".equalsIgnoreCase(code)){
				return json.getString(YingShuoNewConstant.YS_RESOLVE_DATA_NAME);
			}
		}
		return null;
	}
	
	private String getRecords(JSONObject json){
	   String date = getDate(json);
	   if(StringUtils.isNotBlank(date)){
			JSONObject dataJson = Json.parseObject(date);
			try {
				return dataJson.getString("records");
//				return URLEncoder.encode(dataJson.getString("records"), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	private String getUrl(String url,String name,String value){
		StringBuilder uBuilder = new StringBuilder();
		uBuilder.append(url);
		if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value)){
			uBuilder.append("?"+name+"=");
			uBuilder.append(value);
		}
		return uBuilder.toString();
	}
	
	/**
	 * 进行学段的转化
	 * @return
	 */
	private String doChargeSection(String sections){
		sections = sections.replaceAll("0", ",");
		sections = sections.replaceAll("1", "0");
		sections = sections.replaceAll("2", "1");
		sections = sections.replaceAll("3", "2");
		sections = sections.replaceAll("4", "3");
		if(",".equals(sections)){
			return StringUtils.EMPTY;
		}
		return sections;
	}
	
	/*
	 * 1--启用   0--禁用
	 */
	private Integer doChargeIsUsing(String isUsing) {
		if(StringUtils.isNotBlank(isUsing) && "3".equals(isUsing)){
			return YingShuoNewConstant.BOOLEAN_FALSE_VALUE;
		}
		return YingShuoNewConstant.BOOLEAN_TRUE_VALUE; 
	}
	
	private Integer doChargeIsDeleted(String isDeleted) {
		if(StringUtils.isNotBlank(isDeleted) && "1".equals(isDeleted)){
			return YingShuoNewConstant.BOOLEAN_TRUE_VALUE;
		}
		return YingShuoNewConstant.BOOLEAN_FALSE_VALUE;
	}
	
	/**
	 * 转化是否毕业 0正常1离校
	 * @param string
	 * @return
	 */
	private Integer doChargeIsLeaveSchool(String leaveSchool) {
		if(StringUtils.isNotBlank(leaveSchool) && "2".equals(leaveSchool)){
			return YingShuoNewConstant.BOOLEAN_TRUE_VALUE;
		}
		return YingShuoNewConstant.BOOLEAN_FALSE_VALUE;
	}
	
	/**
	 * id不足32位的， 前面补为0
	 * @param id
	 * @return
	 */
	private String doChangeId(String id) {
		return StringUtils.leftPad(id, 32, "0");
	}
	/**
	 * 32位去掉前面补的 14位
	 * @param unitId
	 * @return
	 */
	private String getFkId(String unitId) {
		if(unitId.length() == 32){
			return StringUtils.substring(unitId, 14);
		}
		return unitId;
	}
	/**
	 * 进行学期的转化
	 * @param semester
	 * @return
	 */
	private Integer doChargeSemester(String semester) {
		if(StringUtils.isBlank(semester) || "第一学期".equals(semester)){
			return YingShuoNewConstant.FIRST_SEMESTER_VALUE;
		}
		return YingShuoNewConstant.SECTOND_SEMESTER_VALUE;
	}
	
	/**
	 * 性别转化
	 * @param string
	 * @return
	 */
	private Integer doChangeSex(String sex) {
		Integer endSex = 0;
		if(StringUtils.isNotBlank(sex)){
			switch (sex) {
			case "1":
				endSex = 1; //女
				break;
			case "2":
				endSex = User.USER_WOMAN_SEX;  //男
				break;
			case "3":
				endSex = 0;
				break;
			default:
				break;
			}
		}
		return endSex;
	}
	
	/**
	 * 楼层类型转化
	 * @param buildingType
	 * @return
	 */
	private String doChargeBuildingType(String buildingType) {
		if(StringUtils.isBlank(buildingType) || "7".equals(buildingType) || "8".equals(buildingType)){
			return YingShuoNewConstant.YS_BUILDING_TYPE_OTHER;
		}
		return YingShuoNewConstant.YS_BUILDING_TYPE_TEACH;
	}
	
	/**
	 * 场地类型转化
	 * @param string
	 * @return
	 */
	private String doChargePlaceType(String placeType) {
		Map<String, String> placeTypeMap = new HashMap<>();
		placeTypeMap.put("18", "5");
		placeTypeMap.put("19", "10"); // 10--其他
		placeTypeMap.put("20", "10");
		placeTypeMap.put("21", "10");
		placeTypeMap.put("22", "10");
		placeTypeMap.put("23", "10");
		placeTypeMap.put("24", "10");
		placeTypeMap.put("25", "10");
		placeTypeMap.put("26", "70");
		placeTypeMap.put("27", "12");
		placeTypeMap.put("28", "10");
		placeTypeMap.put("29", "10");
		placeTypeMap.put("30", "10");
		placeTypeMap.put("31", "10");
		placeTypeMap.put("32", "3");
		placeTypeMap.put("33", "10");
		placeTypeMap.put("34", "10");
		placeTypeMap.put("35", "10");
		placeTypeMap.put("36", "10");
		placeTypeMap.put("37", "4");
		placeTypeMap.put("38", "11");
		placeTypeMap.put("39", "11");
		placeTypeMap.put("40", "13");
		placeTypeMap.put("41", "4");
		if(StringUtils.isBlank(placeType) || placeTypeMap.get(placeType) == null){
			return "10";
		}
		return placeTypeMap.get(placeType);
	}
	
	/**
	 * 学校类型转化
	 * @param sections
	 * @return
	 */
	private String doChargeSchoolType(String sections) {
		String schoolType = "211";
		switch (sections) {
		case "0":
		    schoolType = "111";
		    break;
        case "1":
        	schoolType = "211";
			break;
		case "2":
			schoolType = "311";
			break;
        case "3":
        	schoolType = "342";
			break;
        case "1,2":
        	schoolType = "312";
			break;
        case "2,1":
        	schoolType = "312";
			break;
        case "2,3":
        	schoolType = "341";
			break;
        case "3,2":
        	schoolType = "341";
			break;
        case "1,2,3":
        	schoolType = "345";
        	break;
        case "1,3,2":
        	schoolType = "345";
        	break;
        case "3,1,2":
        	schoolType = "345";
        	break;
        case "2,1,3":
        	schoolType = "345";
        	break;
        case "2,3,1":
        	schoolType = "345";
        	break;
        case "3,2,1":
        	schoolType = "345";
        	break;
		default:
			schoolType = "345";
			break;
		}
		return schoolType;
	}
	
	private Semester getCurrentSemester() {
		String remoteSemesterKey4Hours = "ngke.remote.semester.4hour";
		Semester semester = Semester
				.dc(RedisUtils.get(remoteSemesterKey4Hours, RedisUtils.TIME_ONE_HOUR * 4, new RedisInterface<String>() {
					@Override
					public String queryData() {
						return semesterRemoteServicer.getCurrentSemester(SemesterRemoteService.RET_PRE);
					}
				}));
		return semester;
	}
	
	private void doInstallSectionTime(Semester semester,
			Map<String, StusysSectionTimeSet> timeMap,
			List<StusysSectionTimeSet> saveStusysSectionTimeSets, String unitId, JSONObject jsonObject) {
		String sectionTime = jsonObject.getString("schedule_list");
		Semester semester2 = SUtils.dc(semesterRemoteService.findByAcadyearAndSemester(semester.getAcadyear(), semester.getSemester(),unitId),
				Semester.class);
		int mornPeriods = semester2.getMornPeriods();
		int amPeriods  = semester2.getAmPeriods() + mornPeriods ;
		int pmPeriods  = semester2.getPmPeriods() + amPeriods;
		JSONArray sectionTimeArray = Json.parseArray(sectionTime);
		for (int j = 0; j < sectionTimeArray.size(); j++) {
			JSONObject stjs = sectionTimeArray.getJSONObject(j);
			String beginTime = stjs.getString("start_time").substring(0, 5);
			String endTime = stjs.getString("end_time").substring(0, 5);
			Integer sectionNumber = stjs.getInteger("section");
			//进行转化
			String periodInterval ;
			Integer period ;
			if(sectionNumber > pmPeriods){
				period = sectionNumber - pmPeriods;
				periodInterval = BaseConstants.PERIOD_INTERVAL_4;
			}else if (sectionNumber > amPeriods){
				period = sectionNumber - amPeriods;
				periodInterval = BaseConstants.PERIOD_INTERVAL_3;
			}else if (sectionNumber > mornPeriods) {
				period = sectionNumber - mornPeriods;
				periodInterval = BaseConstants.PERIOD_INTERVAL_2;
			}else {
				period = sectionNumber;
				periodInterval = BaseConstants.PERIOD_INTERVAL_1;
			}
			String tid = doChangeId(stjs.getString("fkcode"));
			Integer isDeleted = doChargeIsDeleted(stjs.getString("del_status"));
			StusysSectionTimeSet timeSet;
			if(timeMap != null && !timeMap.isEmpty() && timeMap.get(tid) != null){
				timeSet = timeMap.get(tid);
			}else{
				timeSet = new StusysSectionTimeSet();
				timeSet.setId(tid);
				timeSet.setUserId(UuidUtils.generateUuid());
			}
			timeSet.setAcadyear(semester.getAcadyear());
			timeSet.setSemester(semester.getSemester());
			timeSet.setBeginTime(beginTime);
			timeSet.setEndTime(endTime);
			timeSet.setSectionNumber(sectionNumber);
			timeSet.setIsDeleted(isDeleted);
			timeSet.setUnitId(unitId);
			timeSet.setPeriodInterval(periodInterval);
			timeSet.setPeriod(period);
			saveStusysSectionTimeSets.add(timeSet);
		}
	}

	private void doInstallSubSchool(Map<String, SubSchool> subSchoolMap,
			List<SubSchool> saveSubSchools,String unitId, JSONObject jsonObject) {
		String schoolInfo = jsonObject.getString("campus_list");
		JSONArray schoolArray = Json.parseArray(schoolInfo);
		for (int j = 0; j < schoolArray.size(); j++) {
			JSONObject schoolJson = schoolArray.getJSONObject(j);
			String name = schoolJson.getString("campus_name");
			String id = doChangeId(schoolJson.getString("fkcode"));
			String address = schoolJson.getString("campus_address");
			Integer isDeleted = doChargeIsDeleted(schoolJson.getString("del_status"));
		    long updatestamp = new Date().getTime();
		    SubSchool subSchool;
			if(subSchoolMap != null && !subSchoolMap.isEmpty() && subSchoolMap.get(id) != null){
				subSchool = subSchoolMap.get(id);
			}else{
				subSchool = new SubSchool();
				subSchool.setId(id);
			}
			subSchool.setAddress(address);
			subSchool.setName(name);
			subSchool.setIsDeleted(isDeleted);
			subSchool.setUpdatestamp(updatestamp);
			subSchool.setSchoolId(unitId);
			saveSubSchools.add(subSchool);
		}
	}

	/**
	 * 得到 封装后的学生数据
	 * @param regionUnits
	 * @param uidMap
	 * @param allSchoolMap
	 * @param eduCode
	 * @param saveSchools
	 * @param saveUnits
	 * @param unitId
	 * @param jsonObject
	 */
	private void doInstallSchool(List<Unit> regionUnits,
			Map<String, Unit> uidMap, Map<String, School> allSchoolMap,
			String eduCode, List<School> saveSchools, List<Unit> saveUnits,
			String unitId, JSONObject jsonObject) {
		String schoolInfo = jsonObject.getString("school_infor");
//		JSONArray schoolArray = Json.pase(schoolInfo);
//		for (int j = 0; j < schoolArray.size(); j++) {
			JSONObject schoolJson = Json.parseObject(schoolInfo);
			String schoolCode = schoolJson.getString("school_code");
			String schoolName = schoolJson.getString("school_name");
			String englishName = schoolJson.getString("school_english_name");
			String sections = doChargeSection(schoolJson.getString("school_stages"));
			String address = schoolJson.getString("school_address");
			String linkPhone = schoolJson.getString("school_tel");
			String fax  = schoolJson.getString("school_fax");
			String postalcode = schoolJson.getString("school_postcode");
			String setupTime = schoolJson.getString("setup_time");
			Date creationTime = new Date();
			if(StringUtils.isNotBlank(setupTime)){
				creationTime = TypeUtils.castToDate(Long.parseLong(setupTime)*1000);
			}
			String area = schoolJson.getString("school_area"); 
			Date modifyTime = new Date();
			Integer isDeleted = doChargeIsDeleted(schoolJson.getString("del_status"));
			String schoolType = doChargeSchoolType(sections);
		    //---------封装学校
			School school;
			if(allSchoolMap != null && !allSchoolMap.isEmpty() && allSchoolMap.get(unitId) != null){
				school = allSchoolMap.get(unitId);
				school.setModifyTime(modifyTime);
			}else{
				school = new School();
				school.setRunSchoolType(School.DEFAULT_RUN_SCHOOL_TYPE);
				school.setRegionCode(YingShuoNewConstant.YS_REGION_CODE_VAL);
				school.setCreationTime(creationTime);
				school.setId(unitId);
			}
			school.setSchoolCode(schoolCode);
			school.setSchoolName(schoolName);
			school.setEnglishName(englishName);
			school.setSections(sections);
			school.setLinkPhone(linkPhone);
			school.setFax(fax);
			school.setPostalcode(postalcode);
			school.setArea(area);
			school.setIsDeleted(isDeleted);
			school.setSchoolType(schoolType);
			saveSchools.add(school);
			//------封装unit
			String regionCode = school.getRegionCode();
			Unit u;
			if(uidMap != null && !uidMap.isEmpty() && uidMap.get(unitId) != null){
				u = uidMap.get(unitId);
				u.setModifyTime(modifyTime);
			}else{
				u = new Unit();
				u.setId(school.getId());
				u.setUnitState(Unit.UNIT_MARK_NORAML);
				u.setRegionCode(regionCode);
				if(RedisUtils.get("syncdata.yingshuo.unit.unioncode.max.regioncode." + regionCode) == null) {
					RedisUtils.set("syncdata.yingshuo.unit.unioncode.max.regioncode." + regionCode, String.valueOf(regionUnits.size()));
				}
				Long max = RedisUtils.incrby("syncdata.yingshuo.unit.unioncode.max.regioncode." + regionCode, 1);
				log.info("-----------新增单位的unioncode的值是-----" + max);
				if(StringUtils.isEmpty(regionCode)) {
					u.setUnionCode(StringUtils.leftPad(max + "", 6, "0"));
				}else {
					u.setUnionCode(regionCode + StringUtils.leftPad(max + "", 6, "0"));
				}
				u.setDisplayOrder(u.getUnionCode());
				u.setRegionLevel(doChargeRegionLevel(regionCode));
				u.setUnitClass(Unit.UNIT_CLASS_SCHOOL);
			}
			u.setParentId(doChangeId(eduCode));
			u.setUnitName(school.getSchoolName());
			u.setIsDeleted(school.getIsDeleted());
			u.setSchoolType(school.getSchoolType());
			u.setRunSchoolType(school.getRunSchoolType()); 
			//根据学校类型来得出单位类型
			Integer unitType;
			if(schoolType.startsWith("1")){
				unitType = Unit.UNIT_SCHOOL_KINDERGARTEN;
			}else if (schoolType.startsWith("2")) {
				unitType = Unit.UNIT_SCHOOL_ASP;
			}else if (schoolType.startsWith("3")) {
				unitType = Unit.UNIT_SCHOOL_ASP;
			}else if (schoolType.startsWith("5")) {
				unitType = Unit.UNIT_NOTEDU_NOTSCH;
			}else if (schoolType.startsWith("9")) {
				unitType = Unit.UNIT_NOTEDU_NOTSCH;
			}else{
				unitType = Unit.UNIT_SCHOOL_ASP;
			}
			u.setUnitType(unitType);
			u.setAddress(address);
			u.setLinkPhone(linkPhone);
			u.setFax(fax);
			saveUnits.add(u);
//		}
	}
	
	private Integer doChargeRegionLevel(String regionCode) {
		return (StringUtils.endsWith(regionCode, "0000") ? 3 : StringUtils.endsWith(regionCode, "00") ? 4 : 5);
	}

	private String doChargeUserName(String name) {
		return name = YingShuoNewConstant.YS_USERNAME_DEFEULT_BEFORE + name;
	}
	
	
	public static void main(String[] args) {
		
		String  ssString = "2017-2018";
		String aa = StringUtils.substring(ssString, 5);
		System.out.println(aa);
	}
}
