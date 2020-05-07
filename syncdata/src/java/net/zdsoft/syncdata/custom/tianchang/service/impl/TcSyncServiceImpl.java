package net.zdsoft.syncdata.custom.tianchang.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.syncdata.custom.tianchang.constant.TcBaseConstant;
import net.zdsoft.syncdata.custom.tianchang.service.TcSyncService;
import net.zdsoft.syncdata.util.SyncTimeUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Service("tcSyncService")
public class TcSyncServiceImpl implements TcSyncService{

	private Logger log = Logger.getLogger(TcSyncServiceImpl.class);
	@Autowired
    private BaseSyncSaveRemoteService baseSyncSaveService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
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
	
	
	static Map<String, String> schoolTypeMap = new HashMap<>();
	static{
		schoolTypeMap.put("01", "111");
		schoolTypeMap.put("02", "211"); 
		schoolTypeMap.put("03", "311"); 
		schoolTypeMap.put("04", "342"); 
		schoolTypeMap.put("05", "341");  
		schoolTypeMap.put("06", "312"); 
		schoolTypeMap.put("07", "345"); 
		schoolTypeMap.put("09", "218"); 
		schoolTypeMap.put("10", "365"); 
		schoolTypeMap.put("11", "362"); 
		schoolTypeMap.put("12", "363"); 
		schoolTypeMap.put("13", "371"); 
		schoolTypeMap.put("16", "511"); 
		schoolTypeMap.put("14", "512"); 
		schoolTypeMap.put("15", "513"); 
		schoolTypeMap.put("18", "364"); 
		schoolTypeMap.put("21", "228"); 
		schoolTypeMap.put("22", "312"); 
		schoolTypeMap.put("24", "311"); 
	}
	
	@Override
	public void saveEdu() {
		// TODO listSchoolByArea
		try {
			List<Unit> allUnits = SUtils.dt(unitRemoteService.findByUnitClass(Unit.UNIT_CLASS_EDU), new TR<List<Unit>>() {
			});
			Map<String, Unit> allUnitMap = EntityUtils.getMap(allUnits, Unit::getId);
			String cc = getDate (TcBaseConstant.TC_GET_EDU_UNIT_URL);
			JSONArray array = Json.parseArray(cc);
			log.info("获取教育局的信息数据为------------" + array.size());
			//先得到对应的idList
			List<Unit> saveUnits = new ArrayList<>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String id = js.getString("id");
				if(StringUtils.isNotBlank(id)){
					id = StringUtils.leftPad(id+ "", 32, "0");
				}
				String parentId = js.getString("parentOrgId");
				String unitName = js.getString("eduorgName");
				Integer isDeleted = Integer.valueOf(js.getString("delFlag"));
				String regionCode = TcBaseConstant.TC_REGION_CODE_VAL;
//				    ----------封装单位
				Unit u;
				if(allUnitMap != null && allUnitMap.get(id) != null){
					u = allUnitMap.get(id);
					u.setModifyTime(new Date());
				}else{
					u = new Unit();
					u.setId(id);
					u.setRegionCode(regionCode);
					u.setUnionCode(regionCode);
					u.setDisplayOrder(u.getUnionCode());
					u.setRegionLevel(
							StringUtils.endsWith(regionCode, "0000") ? 3 : StringUtils.endsWith(regionCode, "00") ? 4 : 5);
					u.setUnitClass(Unit.UNIT_CLASS_EDU);
					u.setUnitType(Unit.UNIT_EDU_SUB);
					u.setCreationTime(new Date());
				}
				u.setParentId(StringUtils.leftPad(parentId+ "", 32, "0"));
				if("天长市教体局".equals(unitName)){
					u.setParentId(Unit.TOP_UNIT_GUID);
					u.setUnitType(Unit.UNIT_EDU_TOP);
				}
				u.setUnitName(unitName);
				u.setIsDeleted(isDeleted);
				saveUnits.add(u);
			}
			if(CollectionUtils.isNotEmpty(saveUnits))
				baseSyncSaveService.saveUnit(saveUnits.toArray(new Unit[saveUnits.size()]));
		} catch (Exception e) {
			log.error("天才同步学校数据失败-------");
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveSchool() {
		// TODO listSchoolByArea
		try {
			Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
			//缓存为空时默认取region_code 的总数
			List<Unit> regionUnits = SUtils.dt(unitRemoteService.findByRegionCode(TcBaseConstant.TC_REGION_CODE_VAL),
					new TR<List<Unit>>() {});
			Map<String, Unit> uidMap = EntityUtils.getMap(regionUnits, Unit::getId);
			Set<String> sidSet = EntityUtils.getSet(regionUnits, Unit::getId);
			List<School> allSchool = SUtils.dt(schoolRemoteService.findListByIds(sidSet.toArray(new String[sidSet.size()])),
					new TR<List<School>>() {});
			Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getId);
			
			String time = SyncTimeUtils.getModifyTime(TcBaseConstant.TC_SCHOOL_REDIS_KEY);
			String cc = getDate (TcBaseConstant.TC_GET_UNIT_URL);
			JSONArray array = Json.parseArray(cc);
			log.info("获取学校的信息数据为------------" + array.size());
			//先得到对应的idList
			List<School> saveSchools = new ArrayList<>();
			List<Unit> saveUnits = new ArrayList<>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				
				String sid = StringUtils.leftPad(js.getString("id")+"",32,"0");
				String schoolCode = js.getString("schoolCode");
				String schoolName = js.getString("schoolName");
				String schoolType = js.getString("educationalSystem");
//				String parentId = js.getString("parentOrgId");
				Integer isDeleted = Integer.valueOf(js.getString("delFlag"));
//				String state = js.getString("forbidden");
				String state = String.valueOf(Unit.UNIT_MARK_NORAML);
				String sections = doChargeSection(js.getString("ownPhase"));
				String address = js.getString("address");
//			----------封装学校
				School school;
				if(allSchoolMap != null && !allSchoolMap.isEmpty() && allSchoolMap.get(sid) != null){
					school = allSchoolMap.get(sid);
					school.setModifyTime(new Date());
				}else{
					school = new School();
					school.setRunSchoolType(School.DEFAULT_RUN_SCHOOL_TYPE);
					school.setRegionCode(TcBaseConstant.TC_REGION_CODE_VAL);
					school.setCreationTime(new Date());
				}
				school.setId(sid);
				if(StringUtils.isBlank(schoolType)){
					if(schoolName.contains("中学")){
						schoolType = "311";
						if(StringUtils.isBlank(schoolCode))
						   sections = "2";
					}
					if(schoolName.contains("小学")){
						schoolType = "211";
						if(StringUtils.isBlank(schoolCode))
						   sections = "1";
					}
					if(schoolName.contains("高中")){
						schoolType = "342";
						if(StringUtils.isBlank(schoolCode))
						   sections = "3";
					}
					if(schoolName.contains("幼儿园")){
						schoolType = "111";
						if(StringUtils.isBlank(schoolCode))
						   sections = "0";
					}
				}else{
					schoolType = schoolTypeMap.get(schoolType);
				}
				schoolType = StringUtils.isBlank(schoolType)  ? "211" : schoolType;
				school.setSchoolType(schoolType);
				school.setSchoolCode(StringUtils.isBlank(schoolCode)  ? "0000" : schoolCode);
				if(StringUtils.isBlank(sections)){
					sections = "1";
				}
				school.setSections(sections);
				school.setSchoolName(schoolName);
				school.setIsDeleted(isDeleted);
				school.setAddress(address);
				saveSchools.add(school);
//		    ----------封装单位	
				String regionCode = school.getRegionCode();
				Unit u;
				if(uidMap != null && !uidMap.isEmpty() && uidMap.get(sid) != null){
					u = uidMap.get(sid);
				}else{
					u = new Unit();
					u.setId(school.getId());
					if(StringUtils.isBlank(state)){
						u.setUnitState(Unit.UNIT_MARK_NORAML);
					}
					u.setRegionCode(regionCode);
					
					if(RedisUtils.get("syncdata.tianchang.unit.unioncode.max.regioncode." + regionCode) == null) {
						RedisUtils.set("syncdata.tianchang.unit.unioncode.max.regioncode." + regionCode, String.valueOf(regionUnits.size()));
					}
					Long max = RedisUtils.incrby("syncdata.tianchang.unit.unioncode.max.regioncode." + regionCode, 1);
					log.info("-----------新增单位的unioncode的值是-----" + max);
					if(StringUtils.isEmpty(regionCode)) {
						u.setUnionCode(StringUtils.leftPad(max + "", 6, "0"));
					}else {
						u.setUnionCode(regionCode + StringUtils.leftPad(max + "", 6, "0"));
					}
					u.setDisplayOrder(u.getUnionCode());
					u.setRegionLevel(
							StringUtils.endsWith(regionCode, "0000") ? 3 : StringUtils.endsWith(regionCode, "00") ? 4 : 5);
					u.setUnitClass(Unit.UNIT_CLASS_SCHOOL);
				}
				u.setParentId(topUnit.getId());
				if(StringUtils.isNotBlank(state)){
					u.setUnitState(Integer.valueOf(state));
				}
				u.setUnitName(school.getSchoolName());
				//默认是学校
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
				saveUnits.add(u);
			}
			if(CollectionUtils.isNotEmpty(saveSchools))
				baseSyncSaveService.saveSchool(saveSchools.toArray(new School[saveSchools.size()]));
			if(CollectionUtils.isNotEmpty(saveUnits))
				baseSyncSaveService.saveUnit(saveUnits.toArray(new Unit[saveUnits.size()]));
			time = String.valueOf(System.currentTimeMillis());
			RedisUtils.set(TcBaseConstant.TC_SCHOOL_REDIS_KEY, time);
		} catch (Exception e) {
			log.error("天长同步学校数据失败-------" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveClass() {
		//TODO listClassBySchool
		try {
			int num = 0;
			Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
			if (semester == null) {
				log.error("------ 请先维护学年学期 -------");
				return;
			}
			List<School> allSchool = SUtils.dt(schoolRemoteService.findByRegionCodes(TcBaseConstant.TC_REGION_CODE_VAL),
					new TR<List<School>>() {});
			Set<String> setUtils = EntityUtils.getSet(allSchool, School::getId);
			List<Clazz> allClazzs = SUtils.dt(classRemoteService.findBySchoolIdIn(
					setUtils.toArray(new String[setUtils.size()])), Clazz.class);
			Map<String, Clazz> allClazzMap = EntityUtils.getMap(allClazzs, Clazz::getId);
			if(CollectionUtils.isNotEmpty(allSchool)){
				for (School school : allSchool) {
					Map<String, String> paramMap = new HashMap<>();
					paramMap.put("schoolId", StringUtils.substring(school.getId(), 13));
					String data = getDate (TcBaseConstant.TC_GET_CLAZZ_URL,paramMap);
					JSONArray array = Json.parseArray(data);
					if(array != null){
						num += (array.size());
					}
					Map<String, Grade> gradeMap = new HashMap<>();
					List<Clazz> saveClazzList = new ArrayList<>();
					List<Grade> saveGradeList = new ArrayList<Grade>();
					for (int i = 0; i < array.size(); i++) {
						JSONObject js = array.getJSONObject(i);
						String clazzId =  StringUtils.leftPad(js.getString("id")+"",32,"0");
						String schoolId = StringUtils.leftPad(js.getString("schoolId")+"",32,"0");
						String className = js.getString("className");
						String campusId  = js.getString("campusId");  //校区
						String classCode = js.getString("classCode");
						if(StringUtils.isNotBlank(classCode) && classCode.length() > 10){
							classCode = classCode.substring(0, 10);
						}
						String section = doChargeSection(js.getString("phase"));
						if(StringUtils.isBlank(section)){
							section = "1";
						}
						String creyear = js.getString("year"); //学年
						if(StringUtils.isBlank(creyear)){
							Calendar date = Calendar.getInstance();
							creyear = String.valueOf(date.get(Calendar.YEAR));
						}
						String acadyear = creyear + "-" + String.valueOf(Integer.valueOf(creyear) + 1);
						//转化学制
						Integer schoolingLength = (Integer.valueOf(section) == 1 ? 6 : 3);
						
//				----------封装班级数据
						Clazz clazz;
						if(allClazzMap != null && !allClazzMap.isEmpty() && allClazzMap.get(clazzId) != null){
							clazz = allClazzMap.get(clazzId);
							clazz.setModifyTime(new Date());
						}else{
							clazz = new Clazz();
							clazz.setId(clazzId);
							clazz.setCreationTime(new Date());
						}
						clazz.setSchoolId(schoolId);
						clazz.setClassName(className);
//					clazz.setCampusId(campusId);
						clazz.setSection(Integer.valueOf(section));
						clazz.setSchoolingLength(schoolingLength);
						clazz.setAcadyear(acadyear);
						clazz.setClassCode(classCode);
//				-----------封装年级数据
						int year = NumberUtils.toInt(StringUtils.substring(clazz.getAcadyear(), 0, 4));
						int year2 = NumberUtils.toInt(StringUtils.substring(semester.getAcadyear(), 0, 4));
						Integer yearNum = null;
						String monthTime = "0910";  //班级的新建时间暂定是9月1号
						String nowCreatime = new SimpleDateFormat("MMdd").format(new Date());
						if(NumberUtils.toInt(nowCreatime) > NumberUtils.toInt(monthTime)){
							year2++;
						}
						if (year2 > (year + clazz.getSchoolingLength())) {
							clazz.setIsGraduate(1);
							clazz.setGraduateDate(new Date());
							yearNum = 6;
						} else {
							yearNum = year2 - year;
							clazz.setIsGraduate(0);
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
						String gradeBefore;
						String gradeYear = yearNum == 1 ? "一" : yearNum == 2 ? "二" : yearNum == 3 ? "三" :
							yearNum == 4 ? "四" : yearNum == 5 ? "五" : "六";
						if(clazz.getSection() == 1){
							gradeBefore = "小";
						}else if (clazz.getSection() == 2) {
							gradeBefore = "初";
							if(yearNum > 3){
								gradeYear = "三";
							}
						}else {
							gradeBefore = "高";
							if(yearNum > 3){
								gradeYear = "三";
							}
						}
						String gradeName = gradeBefore + gradeYear;
						String gradeCode = ""+clazz.getSection() + yearNum;
						
						//年级名称的转化
						Grade grade1 = gradeMap.get(clazz.getSchoolId() + clazz.getAcadyear() + clazz.getSection());
						if (grade1 == null) {
							grade1 = new Grade();
							grade1.setGradeName(gradeName);
							grade1.setCreationTime(clazz.getCreationTime());
							grade1.setModifyTime(clazz.getModifyTime());
							grade1.setEventSource(0);
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
					//进行保存数据
					if(CollectionUtils.isNotEmpty(saveClazzList)) {
						baseSyncSaveService.saveClass(saveClazzList.toArray(new Clazz[saveClazzList.size()]));
					}
					if (CollectionUtils.isNotEmpty(saveGradeList)) {
						baseSyncSaveService.saveGrade(saveGradeList.toArray(new Grade[saveGradeList.size()]));
					}
			  }
           }
			log.info("获取班级的信息数据为------------" + num);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("拉取班级数据出错，拉取时间取上一次" + e.getMessage());
		}
	}

	@Override
	public void saveCourse() {
		// TODO getSubjectInfo
//		String time = SyncTimeUtils.getModifyTime(TcBaseConstant.TC_COURSE_REDIS_KEY);
//		String endTime = time;
//		String cc = getDate ();
//		JSONArray array = Json.parseArray(cc);
//		SimpleDateFormat sdf = SyncTimeUtils.getDataFormat("YYYY-MM-dd HH:mm:ss");
//		List<Course> saveCourseList = new ArrayList<>();
//		for (int i = 0; i < array.size(); i++) {
//			JSONObject js = array.getJSONObject(i);
//			
//			String cid = StringUtils.leftPad(js.getString("id")+"",32,"0");
//			String subjectCode = js.getString("subjectCode");
//			String subjectName = js.getString("subjectName");
//			String unitId = js.getString("unitId");
//			String creationTime = js.getString("creationTime");
//			
////     -------封装course数据			
//			Course course = new Course();
//			course.setId(cid);
//			course.setSubjectCode(subjectCode);
//			course.setSubjectName(subjectName);
//			course.setUnitId(unitId);
//			if(StringUtils.isNotBlank(creationTime))
//				course.setCreationTime(new Date());
//			saveCourseList.add(course);
//		}
//		if (CollectionUtils.isNotEmpty(saveCourseList)) {
//			baseSyncSaveService.saveCourse(saveCourseList.toArray(new Course[saveCourseList.size()]));
//		}
	}

	@Override
	public void saveTeacher() {
		// TODO listTeacherBySchoolSubject
		try {
			int num = 0;
			List<School> allSchool = SUtils.dt(schoolRemoteService.findByRegionCodes(TcBaseConstant.TC_REGION_CODE_VAL),
					new TR<List<School>>() {});
			Set<String> setUtils = EntityUtils.getSet(allSchool, School::getId);
			List<Teacher> allUnitTeacher = SUtils.dt(
	        		teacherRemoteService.findByUnitIdIn(setUtils.toArray(new String[setUtils.size()])), 
	        		new TR<List<Teacher>>() {});
			Map<String,Teacher> teacherMap = EntityUtils.getMap(allUnitTeacher, Teacher::getId);
			
			List<User> allTeacherList = SUtils.dt(userRemoteService.findByOwnerType(User.OWNER_TYPE_TEACHER),new TR<List<User>>() {});
			Map<String,User> allUserMap = EntityUtils.getMap(allTeacherList, User::getId);
			if(CollectionUtils.isNotEmpty(allSchool)){
				Set<String> uidList = EntityUtils.getSet(allSchool, School::getId);
				//设置deptid
				Map<String, List<Dept>> deptMap = SUtils.dt(deptRemoteService.findByUnitIdMap(uidList.toArray(new String[uidList.size()])),
						new TypeReference<Map<String, List<Dept>>>(){});
				for (School school : allSchool) {
					Map<String, String> paramMap = new HashMap<>();
					paramMap.put("schoolId", StringUtils.substring(school.getId(), 13));
 					String data = getDate (TcBaseConstant.TC_GET_TEACHER_URL,paramMap);
					JSONArray array = Json.parseArray(data);
					if(array != null){
						num += (array.size());
						List<Teacher> saveTeacherList = new ArrayList<>();
						List<User> saveUserList = new ArrayList<>();
						for (int i = 0; i < array.size(); i++) {
							JSONObject js = array.getJSONObject(i);
							
							String tid = StringUtils.leftPad(js.getString("id"), 32, "0");
							String unitId = school.getId();
							String sex = js.getString("gender");
							if(StringUtils.isBlank(sex)){
								sex = "1";
							}else if("0".equals(sex)){
								sex = "2";
							}
							String teacherName = js.getString("userName");
							Integer isDeleted = Integer.valueOf(js.getString("delFlag"));
							String username = js.getString("loginName");
							String identityCard = js.getString("idCardNo");
							String usertype  = js.getString("roleData");
							String mobilePhone = js.getString("mobile");
							if(StringUtils.isNotBlank(usertype)){
								JSONArray uArray = Json.parseArray(usertype);
								for (int j = 0; j < uArray.size(); j++) {
									JSONObject uObject = uArray.getJSONObject(j);
									String enName = uObject.getString("enName");
									if(StringUtils.isNotBlank(enName)){
										if(TcBaseConstant.TC_SCHOOL_ADMIN_TYPE_NAME.equals(enName)){
											usertype = enName;
											break;
										}else{
											usertype = enName;
										}
									}
								}
							}else{
								usertype = TcBaseConstant.TC_NORMAL_TYPE_NAME;
							}
	//			       -------封装教师数据
							Teacher t;
							if(teacherMap != null && !teacherMap.isEmpty() && teacherMap.get(tid) != null){
								t = teacherMap.get(tid);
								t.setModifyTime(new Date());
							}else{
								t = new Teacher();
								t.setId(tid);
								t.setTeacherCode(StringUtils.leftPad(i+"", 6, "0"));
								t.setCreationTime(new Date());
							}
							t.setUnitId(unitId);
							t.setSex(Integer.valueOf(sex));
							t.setTeacherName(teacherName);
							if(StringUtils.isNotBlank(identityCard) && identityCard.length() > 18 ){
								identityCard = identityCard.substring(0, 18);
							}
							t.setIdentityCard(identityCard);
							t.setIsDeleted(isDeleted);
							t.setMobilePhone(mobilePhone);
							saveTeacherList.add(t);
	//					----------封装用户	
							User user;
							if(allUserMap != null && !allUserMap.isEmpty() && allUserMap.get(tid) != null){
								user = allUserMap.get(tid);
							}else{
								user = new User();
								user.setId(tid);
								user.setPassword(new PWD(TcBaseConstant.TC_DEFAULT_PASS_WORD_VALUE).encode());
								user.setOwnerType(User.OWNER_TYPE_TEACHER);
								user.setRegionCode(TcBaseConstant.TC_REGION_CODE_VAL);
								user.setUsername(username);
							}
							user.setOwnerId(tid);
							user.setUnitId(unitId);
	//						//验证username 信息是否满足 用户名必须为4-20个字符(包括字母、数字、下划线、中文)，1个汉字为2个字符
	//						username = doChargeUserName(username);
	//						user.setUsername(username);
							user.setSex(t.getSex());
							user.setRealName(t.getTeacherName());
							//判断是否是管理员
							if(TcBaseConstant.TC_SCHOOL_ADMIN_TYPE_NAME.equals(usertype)){
								user.setUserType(User.USER_TYPE_UNIT_ADMIN);
							}else{
								user.setUserType(User.USER_TYPE_COMMON_USER);
							}
							user.setIsDeleted(t.getIsDeleted());
							user.setIdentityCard(t.getIdentityCard());
							user.setMobilePhone(mobilePhone);
							saveUserList.add(user);
						}
						
						for (Teacher teacher : saveTeacherList) {
							if(teacherMap == null || teacherMap.isEmpty() || teacherMap.get(teacher.getId()) == null){
								String unitId = teacher.getUnitId();
								if(deptMap != null || deptMap.get(unitId) != null){				
									List<Dept> depts = deptMap.get(unitId);
									for (Dept dept : depts) {
										if(dept.getIsDefault() == 1){
											teacher.setDeptId(dept.getId());
											break;
										}
									}
								}
							}
						}
						//进行保存数据
						if(CollectionUtils.isNotEmpty(saveTeacherList))
							baseSyncSaveService.saveTeacher(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]));
						if(CollectionUtils.isNotEmpty(saveUserList))
							baseSyncSaveService.saveUser(saveUserList.toArray(new User[saveUserList.size()]));
					}
				}
				log.info("获取教师的信息数据为------------" + num);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("拉取教师数据出错，拉取时间取上一次" + e.getMessage());
		}
	}
	
	@Override
	public void saveStudent(String schoolId) {
		try {
			int num = 0;
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdIn(new String[]{schoolId}), Clazz.class);
			List<Student> studentList=SUtils.dt(studentRemoteService.findBySchoolId(schoolId), new TR<List<Student>>(){});
			Map<String, Student> allStuMap = EntityUtils.getMap(studentList, Student::getId);
			List<User> allStuList = SUtils.dt(userRemoteService.findByUnitIds(schoolId),new TR<List<User>>() {});
			Map<String,User> allUserMap = EntityUtils.getMap(allStuList, User::getId);
			if(CollectionUtils.isNotEmpty(clazzList)){
				for (Clazz clazz : clazzList) {
					Map<String, String> paramMap = new HashMap<>();
					paramMap.put("schoolId", StringUtils.substring(schoolId, 13));
					paramMap.put("classId", StringUtils.substring(clazz.getId(), 13));
					String cc = getDate (TcBaseConstant.TC_GET_STUDENT_URL,paramMap);
					JSONArray array = Json.parseArray(cc);
					if(array != null ){
						log.info("拉取班级的学生数据是：-----------" + array.size());
						num += (array.size());
					}
					List<Student> saveStudentList = new ArrayList<>();
					List<User> saveUserList = new ArrayList<>();
					for (int i = 0; i < array.size(); i++) {
						JSONObject js = array.getJSONObject(i);
						
						String studentId = StringUtils.leftPad(js.getString("id"), 32, "0");
						String classId   = clazz.getId();
						String studentName = js.getString("userName");
						if(StringUtils.isBlank(studentName)){
							studentName = TcBaseConstant.TC_DEFAULT_STU_NAME;
						}
						String sex = js.getString("gender");
						if(StringUtils.isBlank(sex)){
							sex = "1";
						}else if("0".equals(sex)){
							sex = "2";
						}
						Integer isDeleted = Integer.valueOf(js.getString("delFlag"));
						String username = js.getString("loginName");
						String identityCard = StringUtils.trimToEmpty(js.getString("idCardNo"));
						if(StringUtils.isNotBlank(identityCard) && identityCard.length() > 30){
							identityCard = identityCard.substring(0, 29);
						}
						String studentCode = StringUtils.trimToEmpty(js.getString("studentId"));
						if(StringUtils.isBlank(studentCode)){
							studentCode = StringUtils.isBlank(identityCard) ? TcBaseConstant.TC_DEFAULT_STU_CODE 
									: identityCard;
						}
						if(StringUtils.isNotBlank(studentCode) && studentCode.length() > 20){
							studentCode = studentCode.substring(0, 19);
						}
//						----补充字段
						String unitiveCode = js.getString("studentId"); //学籍号
						unitiveCode = StringUtils.trimToEmpty(unitiveCode);
						if(StringUtils.isNotBlank(unitiveCode) && unitiveCode.length() > 30){
							unitiveCode = unitiveCode.substring(0, 29); 
						}
//						--------封装学生数据
						Student student;
						if(allStuMap != null && !allStuMap.isEmpty() && allStuMap.get(studentId) != null){
							student = allStuMap.get(studentId);
						}else{
							student = new Student();
							student.setId(studentId);
						}
						student.setSchoolId(schoolId);
						student.setClassId(classId);
						student.setStudentName(studentName);
						student.setSex(Integer.valueOf(sex));
						student.setStudentCode(studentCode);
						student.setUnitiveCode(unitiveCode);
						student.setIdentityCard(identityCard);
						student.setIsDeleted(isDeleted);
						saveStudentList.add(student);
//						----------封装用户	
						if(StringUtils.isNotBlank(username)){
							User user;
							if(allUserMap != null && !allUserMap.isEmpty() && allUserMap.get(studentId) != null){
								user = allUserMap.get(studentId);
							}else{
								user = new User();
								user.setId(studentId);
								user.setPassword(new PWD(TcBaseConstant.TC_DEFAULT_PASS_WORD_VALUE).encode());
								user.setOwnerType(User.OWNER_TYPE_STUDENT);
								user.setRegionCode(TcBaseConstant.TC_REGION_CODE_VAL);
								user.setUserType(User.USER_TYPE_COMMON_USER);
								user.setUsername(username);
							}
							user.setOwnerId(studentId);
							user.setUnitId(schoolId);
							user.setSex(student.getSex());
							user.setRealName(student.getStudentName());
							user.setIsDeleted(student.getIsDeleted());
							user.setIdentityCard(student.getIdentityCard());
							saveUserList.add(user);
						}
					}
					if(CollectionUtils.isNotEmpty(saveStudentList)){
						baseSyncSaveService.saveStudent(saveStudentList.toArray(new Student[saveStudentList.size()]));
					}
					if(CollectionUtils.isNotEmpty(saveUserList))
						baseSyncSaveService.saveUser(saveUserList.toArray(new User[saveUserList.size()]));
				}
			}
			log.info("获取学生的信息数据为------------" + num);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("拉取学生数据出错：--------" + e.getMessage());
		}
	}
	
	@Override
	public void saveEduTeacher() {
		try {
			int num = 0;
			List<Unit> edus = SUtils.dt(unitRemoteService.findByUnitClassAndRegion(Unit.UNIT_CLASS_EDU, 
					TcBaseConstant.TC_REGION_CODE_VAL), new TR<List<Unit>>() {});
			Set<String> setUtils = EntityUtils.getSet(edus, Unit::getId);
			List<Teacher> allUnitTeacher = SUtils.dt(
	        		teacherRemoteService.findByUnitIdIn(setUtils.toArray(new String[setUtils.size()])), 
	        		new TR<List<Teacher>>() {});
			Map<String,Teacher> teacherMap = EntityUtils.getMap(allUnitTeacher, Teacher::getId);
			Set<String> teacherIds = EntityUtils.getSet(allUnitTeacher, Teacher::getId);
			List<User> allTeacherList  = SUtils.dt(userRemoteService.findByOwnerIds(teacherIds.toArray(new String[teacherIds.size()])), 
					new TR<List<User>>() {});
			Map<String,User> allUserMap = EntityUtils.getMap(allTeacherList, User::getId);
			
			if(CollectionUtils.isNotEmpty(edus)){
				Set<String> uidList = EntityUtils.getSet(edus, Unit::getId);
				//设置deptid
				Map<String, List<Dept>> deptMap = SUtils.dt(deptRemoteService.findByUnitIdMap(uidList.toArray(new String[uidList.size()])),
						new TypeReference<Map<String, List<Dept>>>(){});
				for (Unit unit : edus) {
					String sid = StringUtils.substring(unit.getId(), 13);
					String data = getDate(TcBaseConstant.TC_GET_EDU_TEACHER_URL,"orgId",sid);
					JSONArray array = Json.parseArray(data);
					num += (array.size());
					List<Teacher> saveTeacherList = new ArrayList<>();
					List<User> saveUserList = new ArrayList<>();
					for (int i = 0; i < array.size(); i++) {
						JSONObject js = array.getJSONObject(i);
						
						String tid = StringUtils.leftPad(js.getString("id"), 32, "0");
						String unitId = unit.getId();
						String sex = js.getString("gender");
						if(StringUtils.isBlank(sex)){
							sex = "1";
						}
						String teacherName = js.getString("userName");
						Integer isDeleted = Integer.valueOf(js.getString("delFlag"));
						String username = js.getString("loginName");
						String mobilePhone = js.getString("mobile");
						
						String usertype  = js.getString("roleData");
						if(StringUtils.isNotBlank(usertype)){
							JSONArray uArray = Json.parseArray(usertype);
							for (int j = 0; j < uArray.size(); j++) {
								JSONObject uObject = uArray.getJSONObject(j);
								String enName = uObject.getString("enName");
								if(StringUtils.isNotBlank(enName)){
									if(TcBaseConstant.TC_EDU_ADMIN_TYPE_NAME.equals(enName)){
										usertype = enName;
										break;
									}else{
										usertype = enName;
									}
								}
							}
						}else{
							usertype = TcBaseConstant.TC_NORMAL_TYPE_NAME;
						}
						
//		       -------封装教师数据
						Teacher t;
						if(teacherMap != null && !teacherMap.isEmpty() && teacherMap.get(tid) != null){
							t = teacherMap.get(tid);
						}else{
							t = new Teacher();
							t.setId(tid);
							t.setTeacherCode(StringUtils.leftPad(i+"", 6, "0"));
						}
						t.setUnitId(unitId);
						t.setSex(Integer.valueOf(sex));
						t.setTeacherName(teacherName);
						t.setIsDeleted(isDeleted);
						t.setMobilePhone(mobilePhone);
						t.setModifyTime(new Date());
						saveTeacherList.add(t);
//				----------封装用户	
						
						if(StringUtils.isNotBlank(username)){
							User user;
							if(allUserMap != null && !allUserMap.isEmpty() && allUserMap.get(tid) != null){
								user = allUserMap.get(tid);
							}else{
								user = new User();
								user.setId(tid);
								user.setPassword(new PWD(TcBaseConstant.TC_DEFAULT_PASS_WORD_VALUE).encode());
								user.setOwnerType(User.OWNER_TYPE_TEACHER);
								user.setRegionCode(TcBaseConstant.TC_REGION_CODE_VAL);
								user.setUsername(username);
							}
							user.setOwnerId(tid);
							user.setUnitId(unitId);
							//验证username 信息是否满足 用户名必须为4-20个字符(包括字母、数字、下划线、中文)，1个汉字为2个字符
//							username = doChargeUserName(username);
							
//							user.setUsername(username);
							user.setSex(t.getSex());
							user.setRealName(t.getTeacherName());
							//判断是否是管理员
							if(TcBaseConstant.TC_EDU_ADMIN_TYPE_NAME.equals(usertype)){
								user.setUserType(User.USER_TYPE_UNIT_ADMIN);
							}else{
								user.setUserType(User.USER_TYPE_COMMON_USER);
							}
							user.setIsDeleted(t.getIsDeleted());
							user.setModifyTime(new Date());
							user.setMobilePhone(mobilePhone);
							saveUserList.add(user);
						}
					}
					for (Teacher teacher : saveTeacherList) {
						if(teacherMap == null || teacherMap.isEmpty() || teacherMap.get(teacher.getId()) == null){
							String unitId = teacher.getUnitId();
							if(deptMap != null && deptMap.get(unitId) != null){				
								List<Dept> depts = deptMap.get(unitId);
								for (Dept dept : depts) {
									if(dept.getIsDefault() == 1){
										teacher.setDeptId(dept.getId());
										break;
									}
								}
							}
						}
					}
					//进行保存数据
					if(CollectionUtils.isNotEmpty(saveTeacherList))
						baseSyncSaveService.saveTeacher(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]));
					if(CollectionUtils.isNotEmpty(saveUserList))
						baseSyncSaveService.saveUser(saveUserList.toArray(new User[saveUserList.size()]));
				}
			}
			log.info("获取教育局教师的信息数据为------------" + num);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取教育局教师的信息数据失败------------" + e.getMessage());
		}
	}

	@Override
	public void saveUser() {
		// TODO Auto-generated method stub
	}
	
//    -------------------------公共的代码区 ----------------------------------------------
	//连接服务，获取数据
	private String getDate(String url){
		return getDate(url,null,null);
	}
	
	private String getDate(String url,String name,String value){
		String dataString = null;
		try {
            url = getUrl(url,name,value);
			String jsonData = UrlUtils.get(url, new String());
			JSONObject jsonObject = Json.parseObject(jsonData);
			Integer status = jsonObject.getInteger("status");
			if(1 == status){
				dataString = jsonObject.getString(TcBaseConstant.TC_RESOLVE_DATA_NAME);
			}
		} catch (IOException e) {
			e.printStackTrace();
			dataString = null;
			log.error("调用接口获取数据失败：--------" + e.getMessage());
		}
		return dataString;
	}
	
	private String getDate(String url,Map<String, String> paramMap){
		String dataString = null;
		try {
            url = getUrl(url,paramMap);
			String jsonData = UrlUtils.get(url, new String());
			JSONObject jsonObject = Json.parseObject(jsonData);
			Integer status = jsonObject.getInteger("status");
			if(1 == status){
				dataString = jsonObject.getString(TcBaseConstant.TC_RESOLVE_DATA_NAME);
			}
		} catch (IOException e) {
			e.printStackTrace();
			dataString = null;
			log.error("调用接口获取数据失败：--------" + e.getMessage());
		}
		return dataString;
	}
	/**
	 * 得到拼接后的地址
	 * @param method
	 * @return
	 */
	private String getUrl(String url){
		return getUrl(url,null,null);
	}
	/**
	 * 得到拼接后的地址
	 * @param url     方法地址
	 * @param name    参数名称
	 * @param value   参数值
	 * @return
	 */
	private String getUrl(String url,String name,String value){
		StringBuilder uBuilder = new StringBuilder();
		String timestamp = getTimestamp();
		uBuilder.append(url);
		uBuilder.append("&timestamp=");
		uBuilder.append(timestamp);
		uBuilder.append("&AppSecret=");
		uBuilder.append(getAppSecret(timestamp));
		if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value)){
			uBuilder.append("&"+name+"=");
			uBuilder.append(value);
		}
		return uBuilder.toString();
	}
	
	private String getUrl(String url,Map<String, String> paramMap){
		paramMap = paramMap == null ? Collections.EMPTY_MAP : paramMap;
		StringBuilder uBuilder = new StringBuilder();
		String timestamp = getTimestamp();
		uBuilder.append(url);
		uBuilder.append("&timestamp=");
		uBuilder.append(timestamp);
		uBuilder.append("&AppSecret=");
		uBuilder.append(getAppSecret(timestamp));
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			uBuilder.append("&"+entry.getKey()+"=");
			uBuilder.append(entry.getValue());
		}
		return uBuilder.toString();
	}
	
	/**
	 * 得到时间戳
	 * @return
	 */
	private static String getTimestamp(){
		String timestamp = RedisUtils.get(TcBaseConstant.TC_APP_TIMESTAMP_REDIS_KEY);
		if(StringUtils.isBlank(timestamp)){
			timestamp = String.valueOf(new Date().getTime()/1000); 
			//提前一分钟过期
			if(StringUtils.isNotBlank(timestamp))
			    RedisUtils.set(TcBaseConstant.TC_APP_TIMESTAMP_REDIS_KEY, timestamp, (int)TimeUnit.MINUTES.toSeconds(1));
		}
		return timestamp;
	}
	
	/**
	 * 得到最终的秘钥
	 * @param timestamp
	 * @return
	 */
	private static String getAppSecret(String timestamp){
		String appSecret = RedisUtils.get(TcBaseConstant.TC_APP_SECRET_REDIS_KEY);
		if (StringUtils.isBlank(appSecret)) {
			String key = DigestUtils.md5Hex(TcBaseConstant.TC_API_SECRET) + TcBaseConstant.TC_SIGH_VAL;
			String secret1 = DigestUtils.md5Hex(key);
			String key1 = secret1 + TcBaseConstant.TC_APP_NAME + timestamp;
			appSecret = DigestUtils.md5Hex(key1);
			//提前一分钟过期
			if(StringUtils.isNotBlank(appSecret))
			    RedisUtils.set(TcBaseConstant.TC_APP_SECRET_REDIS_KEY, appSecret, (int)TimeUnit.MINUTES.toSeconds(1));
		}
		return appSecret;
	}
	
	/**
	 * 进行学段的转化
	 * @return
	 */
	private String doChargeSection(String sections){
		StringBuilder endSection = new StringBuilder();
		if(StringUtils.isNotBlank(sections)){
			String[] sec = sections.split(",");
			for (String string : sec) {
				switch (string) {
				case "03":
					endSection.append("1,");
					break;
				case "04":
					endSection.append("2,");
					break;
				case "05":
					endSection.append("3,");
					break;
				default:
					endSection.append("1,");
					break;
				}
			}
		}
		String endSection1 = endSection.toString();
		if(StringUtils.isNotBlank(endSection1)){
			endSection1 = endSection1.substring(0,endSection1.length() - 1);
		}
		return endSection1;
	}
	
	/**
	 * 对账号的数据处理  小于4位的 加前缀 tc_  
	 * 高于20位的 进行截取
  	 * @param username
	 */
	private String doChargeUserName(String username) {
		// TODO Auto-generated method stub
		//验证username 信息是否满足 用户名必须为4-20个字符(包括字母、数字、下划线、中文)，1个汉字为2个字符
		if(net.zdsoft.framework.utils.StringUtils.getRealLength(username) > 20){
			log.error("用户名不符合passport的规则------------------------》》》"+username  );
			if(StringUtils.endsWithIgnoreCase(username, "@tzcsxx")){
				username = username.substring(0, username.length()-7);
			}else{
				username = username.substring(0, 19);
			}
		}
		if(net.zdsoft.framework.utils.StringUtils.getRealLength(username) < 4){
			log.error("用户名不符合passport的规则------------------------》》》"+username  );
			username = TcBaseConstant.TC_USERNAME_DEFEULT_BEFORE + username;
		}
		return username;
	}
	
	public static void main(String[] args) {
		String name = "xfbjsyxx2015142@tzcsxx";
		if(StringUtils.endsWithIgnoreCase(name, "@tzcsxx")){
			name = name.substring(0, name.length()-7);
		}
		System.out.println(name);
	}
}
