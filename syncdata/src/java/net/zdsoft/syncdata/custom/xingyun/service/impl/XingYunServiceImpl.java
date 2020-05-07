package net.zdsoft.syncdata.custom.xingyun.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.constant.custom.XyConstant;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
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
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.syncdata.constant.WenXunConstant;
import net.zdsoft.syncdata.custom.xingyun.service.XingYunService;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.ModelOperation;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.entity.user.RolePermission;
import net.zdsoft.system.entity.user.UserRole;
import net.zdsoft.system.remote.service.ModelOperationRemoteService;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.RolePermissionRemoteService;
import net.zdsoft.system.remote.service.RoleRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.UserRoleRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;

@Service("xingYunService")
public class XingYunServiceImpl implements XingYunService {
	
	private Logger log = LoggerFactory.getLogger(XingYunServiceImpl.class);
	
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
	@Autowired
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private RoleRemoteService roleRemoteService;
	@Autowired
	private UserRoleRemoteService userRoleRemoteService;
	@Autowired
	private ModelRemoteService modelRemoteService;
	@Autowired
	private RolePermissionRemoteService rolePermissionRemoteService;
	@Autowired
	private ServerRemoteService serverRemoteService;
	@Autowired
	private ModelOperationRemoteService modelOperationRemoteService;
	@Override
	public void saveEdu(String value) {
		Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
		List<Unit> saveUnits = new ArrayList<>();
		Map<String, String> serverUMap = new HashMap<>();
		Map<String, String> serverCodeMap = getServerCodeMap();
		if(StringUtils.isNotBlank(value)){
			JSONArray dataArray = JSON.parseArray(value);
			for (int i = 0; i < dataArray.size(); i++) {
				JSONObject js = dataArray.getJSONObject(i);
				String unitId = js.getString("institutionId");
				String id = doChangeId(unitId);
//				String parentId = js.getString("parentId");
//				if(StringUtils.isBlank(parentId) ){
//					parentId = topUnit == null ? Unit.TOP_UNIT_PARENT_GUID_NOT_TRUE : topUnit.getId();
//				}else if (Unit.TOP_UNIT_GUID.equals(parentId)){
//					parentId = Unit.TOP_UNIT_PARENT_GUID_NOT_TRUE;
//				}
				String parentId = topUnit.getId();
				String unitName = js.getString("institutionName");
				if(StringUtils.isBlank(unitName)){
					unitName = "星云测试教育局";
				}
				Integer isDeleted = doChargeIsDeleted(js.getString("isDelete"));
				String regionCode = doChargeRegionCode(js);
				if(StringUtils.isBlank(regionCode)){
					continue;
				}
				Date creationTime = new Date();
				Date modifyTime   = new Date();	
				String address = doChargeAddress(js.getString("address"));
				//封装servercode 
				String modeNames = js.getString("modeNames");
				if(StringUtils.isNotBlank(modeNames)){
					getServerUMapTime(serverUMap, modeNames,id, serverCodeMap);
				}
//				String serverCode = js.getString("serverCode");
//				if(StringUtils.isNotBlank(serverCode)){
//					serverUMap.put(unitId, getServerExtension(js));
//				}
//				----------封装单位
				Unit unit = SUtils.dc(unitRemoteService.findOneById(doChangeId(unitId)), Unit.class);
				if(unit == null){
					unit = new Unit();
					unit.setUnitClass(Unit.UNIT_CLASS_EDU);
					unit.setId(id);
					unit.setUnionCode(regionCode);
					unit.setDisplayOrder(unit.getUnionCode());
					unit.setCreationTime(creationTime);
				}
				if(Unit.TOP_UNIT_GUID.equals(parentId) ){
					unit.setUnitType(Unit.UNIT_EDU_TOP);
				}else{
					unit.setUnitType(Unit.UNIT_EDU_SUB);
				}
				unit.setAddress(address);
				unit.setParentId(parentId);
				unit.setIsDeleted(isDeleted);
				unit.setUnitName(unitName);
				unit.setRegionCode(regionCode);
				unit.setRegionLevel(
						StringUtils.endsWith(regionCode, "0000") ? 3 : StringUtils.endsWith(regionCode, "00") ? 4 : 5);
				unit.setModifyTime(modifyTime);
				saveUnits.add(unit);
			}
		}
		if(CollectionUtils.isNotEmpty(saveUnits)){
			baseSyncSaveService.saveUnit(saveUnits.toArray(new Unit[saveUnits.size()])); 
			saveRoleModel(EntityUtils.getSet(saveUnits, Unit::getId));
		}
		if(MapUtils.isNotEmpty(serverUMap)){
			baseSyncSaveService.saveUnitAndServerExtension(serverUMap);
		}
	}

	@Override
	public void saveSchool(String value) {
		try {
			Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
			List<Unit> allUnit = SUtils.dt(unitRemoteService.findByUnitClass(Unit.UNIT_CLASS_EDU), Unit.class);
			Map<String, Unit> eduIdMap = EntityUtils.getMap(allUnit, Unit::getId);
			Map<String, String> serverCodeMap = getServerCodeMap();
			if(StringUtils.isNotBlank(value)){
				JSONArray dataArray = JSON.parseArray(value);
				Set<String> unitSets = new HashSet<>();
				for (int i = 0; i < dataArray.size(); i++) {
					JSONObject js = dataArray.getJSONObject(i);
					unitSets.add(doChangeId(js.getString("schoolId")));
				}
				if(CollectionUtils.isNotEmpty(unitSets)){
					List<Unit> allUnits = SUtils.dt(unitRemoteService.findListByIds(unitSets.toArray(new String[unitSets.size()])), new TR<List<Unit>>() {});
					Map<String, Unit> uidMap = EntityUtils.getMap(allUnits, Unit::getId);
					List<School> allSchool = SUtils.dt(schoolRemoteService.findListByIds(unitSets.toArray(new String[unitSets.size()])),
							new TR<List<School>>() {});
					Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getId);
					//先得到对应的idList
					List<School> saveSchools = new ArrayList<>();
					List<Unit> saveUnits = new ArrayList<>();
					log.info("获取学校的信息数据为------------" + dataArray.size());
					Map<String, String> serverUMap = new HashMap<>();
					Map<String, School> saveSchoolMap = new HashMap<>();
					Map<String, Unit> saveUnitMap = new HashMap<>();
					for (int i = 0; i < dataArray.size(); i++) {
						JSONObject js = dataArray.getJSONObject(i);
						
						String sid = doChangeId(js.getString("schoolId"));
						String schoolCode = js.getString("socialCode");
						String schoolName = js.getString("schoolName");
						String schoolType = doChangeSchoolType(js.getString("schoolType"));   //学校类型
						String parentId = doChangeId(js.getString("pSchoolId"));   
						if(!(StringUtils.isNotBlank(parentId) && MapUtils.isNotEmpty(eduIdMap) && eduIdMap.get(parentId) != null)){
							parentId = topUnit.getId();
						}
						Integer isDeleted = doChargeIsDeleted(js.getString("isDelete"));
						String state = String.valueOf(Unit.UNIT_MARK_NORAML);
						String sections = doChargeSection(schoolType);  // TODO 学段需要提供 
						String regionCode = doChargeRegionCode(js);
						if(StringUtils.isBlank(regionCode)){
							continue;
						}
						//开通对应的应用
						String modeNames = js.getString("modeNames");
						if(StringUtils.isNotBlank(modeNames)){
							getServerUMapTime(serverUMap, modeNames,sid, serverCodeMap);
						}
						
//						String serverCode = js.getString("serverCode");
//						if(StringUtils.isNotBlank(serverCode)){
//							serverUMap.put(sid, getServerExtension(js));
//						}
						String address = doChargeAddress(js.getString("address"));
//						String homepage = js.getString("homepage");
						String mobilePhone = js.getString("telephone");
//				----------封装学校
						School school;
						if(allSchoolMap != null && !allSchoolMap.isEmpty() && allSchoolMap.get(sid) != null){
							school = allSchoolMap.get(sid);
							school.setModifyTime(new Date());
						}else if (MapUtils.isNotEmpty(saveSchoolMap) && saveSchoolMap.get(sid) != null){
							school = saveSchoolMap.get(sid);
							school.setModifyTime(new Date());
						}else{
							school = new School();
							school.setRunSchoolType(School.DEFAULT_RUN_SCHOOL_TYPE);
							school.setRegionCode(regionCode);  // TODO 地区码 regionCode 需要提供 
							school.setCreationTime(new Date());
						}
						school.setId(sid);
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
//						school.setHomepage(homepage);
						school.setMobilePhone(mobilePhone);
						school.setLinkPhone(mobilePhone);
//					school.setAddress(address);
						saveSchoolMap.put(sid, school);
						saveSchools.add(school);
//			    ----------封装单位	
						Unit u;
						if(uidMap != null && !uidMap.isEmpty() && uidMap.get(sid) != null){
							u = uidMap.get(sid);
						}else if (MapUtils.isNotEmpty(saveUnitMap) && saveUnitMap.get(sid) != null){
							u = saveUnitMap.get(sid);
							u.setModifyTime(new Date());
						}
						else{
							u = new Unit();
							u.setId(school.getId());
							if(StringUtils.isBlank(state)){
								u.setUnitState(Unit.UNIT_MARK_NORAML);
							}
							u.setRegionCode(regionCode);
							
							if(RedisUtils.get("syncdata.xingyun.unit.unioncode.max.regioncode." + regionCode) == null) {
								RedisUtils.set("syncdata.xingyun.unit.unioncode.max.regioncode." + regionCode, String.valueOf(allUnits.size()));
							}
							Long max = RedisUtils.incrby("syncdata.xingyun.unit.unioncode.max.regioncode." + regionCode, 1);
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
						u.setParentId(StringUtils.isNotBlank(parentId)? parentId : topUnit.getId());
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
						u.setAddress(school.getAddress());
						u.setHomepage(school.getHomepage());
						u.setMobilePhone(school.getMobilePhone());
						u.setLinkPhone(school.getLinkPhone());
						saveUnitMap.put(sid, u);
						saveUnits.add(u);
					}
					
					if(CollectionUtils.isNotEmpty(saveSchools)){
						baseSyncSaveService.saveSchool(saveSchools.toArray(new School[saveSchools.size()]));
					}
					
					if(CollectionUtils.isNotEmpty(saveUnits))
						baseSyncSaveService.saveUnit(saveUnits.toArray(new Unit[saveUnits.size()]));
					    saveRoleModel(EntityUtils.getSet(saveUnits, Unit::getId));
					if(MapUtils.isNotEmpty(serverUMap)){
						baseSyncSaveService.saveUnitAndServerExtension(serverUMap);
					}
				}
			}
		} catch (Exception e) {
			log.error("天长同步学校数据失败-------" + e.getMessage());
			e.printStackTrace();
		}
	}

	private String doChargeAddress(String address) {
		if(StringUtils.isNotBlank(address) && address.length() > 60)
			address = address.substring(0, 59);
		return address;
	}

	@Override
	public void saveClassAndStudent(String value) {
		if(StringUtils.isNotBlank(value)){
			JSONArray array = Json.parseArray(value);
			if(array != null){
			    Set<String> setUtils = new HashSet<>();
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String schoolId = doChangeId(js.getString("organizationId")); 
					setUtils.add(schoolId);	
				}
				Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
				if (semester == null) {
					log.error("------ 请先维护学年学期 -------");
					return;
				}
//				Semester semester = SUtils.dc(semesterRemoteServicer.getCurrentSemester(SemesterRemoteService.RET_NULL,user.getUnitId()), Semester.class);
//				if (semester == null) {
//					json.put("resultMsg", "获取不到学年学期数据！");
//					return json.toJSONString();
//				}
				List<Clazz> allClazzs = SUtils.dt(classRemoteService.findBySchoolIdIn(
						setUtils.toArray(new String[setUtils.size()])), Clazz.class);
				Map<String, Clazz> clazzIdMap = EntityUtils.getMap(allClazzs, Clazz::getId);
				Map<String, List<Clazz>> allClazzMap = EntityUtils.getListMap(allClazzs, Clazz::getSchoolId, Function.identity()); 
				List<Grade> allGrade  = SUtils.dt(gradeRemoteService.findByUnitIdsIn(setUtils.toArray(new String[setUtils.size()])), Grade.class);
				Map<String, String> gradeNameMap = EntityUtils.getMap(allGrade, Grade::getId, Grade::getGradeName);
				
				List<Student> studentList=SUtils.dt(studentRemoteService.findBySchoolIdIn(null, setUtils.toArray(new String[setUtils.size()])), new TR<List<Student>>(){});
				Map<String, Student> allStuMap = EntityUtils.getMap(studentList, Student::getId);
				List<User> allStuList = SUtils.dt(userRemoteService.findByUnitIds(setUtils.toArray(new String[setUtils.size()])),new TR<List<User>>() {});
				Map<String,User> allUserMap = EntityUtils.getMap(allStuList, User::getOwnerId);
				List<Student> saveStudentList = new ArrayList<>();
				List<User> saveUserList = new ArrayList<>();
				List<Clazz> saveClazzList = new ArrayList<>();
				List<Grade> saveGradeList = new ArrayList<Grade>();
				Map<String, Grade> gradeMap = new HashMap<>();
				Map<String, Clazz> clazzMap = new HashMap<>();
				Set<String> userNameSet = new HashSet<String>();
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					//判断是否是我们推送过去的班级数据
					String schoolId = doChangeId(js.getString("organizationId"));
					String gradeName = js.getString("grade");
					String state = js.getString("userInfoChange"); //用户的状态，是否是修改用户的名称
					String classNumber = js.getString("classNumber");
					classNumber = StringUtils.leftPad(classNumber, 2, "0");
					String className = classNumber + "班";
					String classNameDynamic = gradeName + classNumber + "班";
					System.out.println("第" + i  + "获取的班级名称：--------" + classNameDynamic);
					String clazzId = null;
					if(!MapUtils.isEmpty(allClazzMap) && CollectionUtils.isNotEmpty(allClazzMap.get(schoolId))){
						List<Clazz> unitClazzs = allClazzMap.get(schoolId);
						for (Clazz c : unitClazzs) {
							if(classNameDynamic.equals(c.getClassNameDynamic()) ){
								clazzId = c.getId();
							}else{
								String gradeName1 = gradeNameMap.get(c.getGradeId());
								String className1 = c.getClassCode().substring(4,6);
								className1 = StringUtils.leftPad(className1, 2, "0");
								String classNameDynamic1 = gradeName1 + className1 + "班";
								System.out.println("高考排课的班级名称：--------" + classNameDynamic1);
								if(classNameDynamic.equals(classNameDynamic1) ){
									clazzId = c.getId();
								}
							}
						}
					}
					Map<String, String> keyMap = getMapByGradeName(gradeName);
					if(MapUtils.isEmpty(keyMap)){
						continue;
					}
					String section   = keyMap.get("section");
					int  n           = Integer.valueOf(keyMap.get("num"));
					String gradeCode = keyMap.get("gradeCode");
					//转化学制
					Integer schoolingLength = (Integer.valueOf(section) == 1 ? 6 : 3);
				    String startAcadyear, endAcadyear;
				    startAcadyear =String.valueOf( NumberUtils.toInt(semester.getAcadyear().substring(0, 4)) - n );
				    endAcadyear = String.valueOf( NumberUtils.toInt(semester.getAcadyear().substring(5)) - n );
				    String  acadyear =  startAcadyear + "-" + endAcadyear;
					// --------学生信息
					String studentId = doChangeId(js.getString("userId"));
					String studentName = js.getString("userName");
					Integer sex = doChangeSex(js.getString("gender"));
					String username = js.getString("loginName");
					String identityCard = StringUtils.trimToEmpty(js.getString("certificateNumber")); //学生的身份证需要提供 TODO
					Integer isDeleted = doChargeIsDeleted(js.getString("isDelete"));
					
					//判断账号是否已经添加了，是的话直接跳过，
					if(CollectionUtils.isNotEmpty(userNameSet) && userNameSet.contains(username)){
						continue;
					} 
					userNameSet.add(username);
					if(StringUtils.isNotBlank(identityCard) && identityCard.length() > 30){
						identityCard = identityCard.substring(0, 29);
					}
					String studentCode = StringUtils.trimToEmpty(js.getString("studentNumber"));
					if(StringUtils.isBlank(studentCode)){
						studentCode = StringUtils.isBlank(identityCard) ? "0000"
								: identityCard;
					}
					if(StringUtils.isNotBlank(studentCode) && studentCode.length() > 20){
						studentCode = studentCode.substring(0, 19);
					}
//					----补充字段+
					String unitiveCode = js.getString("studentCode"); //学籍号
					unitiveCode = StringUtils.trimToEmpty(unitiveCode);
					if(StringUtils.isNotBlank(unitiveCode) && unitiveCode.length() > 30){
						unitiveCode = unitiveCode.substring(0, 29); 
					}
					String mobilePhone = js.getString("phoneNumber");
					
					//年级名称的转化
					Clazz clazz =  clazzMap.get(classNameDynamic + schoolId);
					if(StringUtils.isNotBlank(clazzId)){
						clazz = clazzIdMap.get(clazzId);
						clazz.setModifyTime(new Date());
					}else if (clazz == null){
						clazz = new Clazz();
						clazz.setId(UuidUtils.generateUuid());
						clazz.setCreationTime(new Date());
						clazzMap.put(classNameDynamic + schoolId, clazz);
						//班号我们自己生成
						String classCodePrefix = StringUtils.substring(acadyear, 0, 4)
								+ StringUtils.leftPad(section + "", 2, "0");
						Long max = RedisUtils
								.incrby("syncdata.xingyun.class.code.max." + schoolId + classCodePrefix, 1);
						clazz.setClassCode(classCodePrefix + StringUtils.leftPad("" + max, 2, "0"));
					}
					clazz.setSchoolId(schoolId);
					clazz.setClassName(className);
					clazz.setSection(Integer.valueOf(section));
					clazz.setSchoolingLength(schoolingLength);
					clazz.setAcadyear(acadyear);
	//			-----------封装年级数据
					String gradesJson = gradeRemoteService.findBySchoolId(clazz.getSchoolId(),
							ArrayUtils.toArray(clazz.getSection()), clazz.getAcadyear(), true);
					List<Grade> gradesExists = SUtils.dt(gradesJson, new TypeReference<List<Grade>>() {
					});
					for (Grade grade1 : gradesExists) {
						String key = grade1.getSchoolId() + grade1.getOpenAcadyear() + grade1.getSection();
						gradeMap.put(key, grade1);
					}
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
					}else{
						grade1.setGradeName(gradeName);
					}
					clazz.setGradeId(grade1.getId());
					saveClazzList.add(clazz);
					// ----封装学生 
					Student student;
					if(allStuMap != null && !allStuMap.isEmpty() && allStuMap.get(studentId) != null){
						student = allStuMap.get(studentId);
					}else{
						student = new Student();
						student.setId(studentId);
					}
					student.setSchoolId(schoolId);
					student.setClassId(clazz.getId());
					student.setStudentName(studentName);
					student.setSex(sex);
					student.setStudentCode(studentCode);
					student.setUnitiveCode(unitiveCode);
					student.setIdentityCard(identityCard);
					student.setIsDeleted(isDeleted);
					student.setMobilePhone(mobilePhone);
					saveStudentList.add(student);
//					----------封装用户	
					if(StringUtils.isNotBlank(username)){
						User user;
						boolean isUpdate = Boolean.TRUE;
						if(allUserMap != null && !allUserMap.isEmpty() && allUserMap.get(studentId) != null){
							user = allUserMap.get(studentId);
						}else{
							user = new User();
							user.setId(studentId);
							user.setPassword(getDefaultPwd());
							user.setOwnerType(User.OWNER_TYPE_STUDENT);
							user.setUserType(User.USER_TYPE_COMMON_USER);
							user.setUsername(username);
							isUpdate = Boolean.FALSE;
						}
						user.setOwnerId(studentId);
						user.setUnitId(schoolId);
						user.setSex(student.getSex());
						user.setRealName(student.getStudentName());
						if(isUpdate && StringUtils.isNotBlank(state) && "loginName".equals(state)  &&  !username.equals(user.getUsername())){
							//先进行软删，再进行新增数据
							user.setIsDeleted(1);
						}else{
							user.setIsDeleted(student.getIsDeleted());
						}
						user.setIdentityCard(student.getIdentityCard());
						user.setMobilePhone(student.getMobilePhone());
						saveUserList.add(user);
						
						if(isUpdate && StringUtils.isNotBlank(state) && "loginName".equals(state)  &&  !username.equals(user.getUsername())){
							User user1 = new User();
							user1.setId(UuidUtils.generateUuid());
							user1.setPassword(getDefaultPwd());
							user1.setOwnerType(User.OWNER_TYPE_STUDENT);
							user1.setUserType(User.USER_TYPE_COMMON_USER);
							user1.setUsername(username);
							//判断是否是管理员
							user1.setOwnerId(studentId);
							user1.setUnitId(schoolId);
							user1.setSex(student.getSex());
							user1.setRealName(student.getStudentName());
							user1.setIsDeleted(student.getIsDeleted());
							user1.setIdentityCard(student.getIdentityCard());
							user1.setMobilePhone(student.getMobilePhone());
							saveUserList.add(user1);
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
	}

	@Override
	public void saveRoleModel(Set<String> unitIds) {

//		List<SubSystem> subSystemList = subSystemRemoteService.findListObjectBy(new String[]{"mark"}, new String[]{"1"});
		List<Server> serverList = serverRemoteService.findByOrderTypeAndStatus(new Integer[]{1,2,3},Server.STATUS_TURNON);
		List<Model> modelList = modelRemoteService.findAllObject();
		Map<Integer, List<Model>> modelMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(modelList)) {
			for (Model model : modelList) {
			    //取mark值为1的 && "ECLASSCARD.HW".equals()
                if (model.getMark() != null && model.getMark().equals(1) && !"ECLASSCARD.HW".equals(model.getUnitMark()) ) {
                    List<Model> list = modelMap.get(model.getSubSystem());
                    if (list == null) {
                        list = new ArrayList<>();
                        modelMap.put(model.getSubSystem(), list);
                    }
                    list.add(model);
                }
			}
		}
//		List<Role> roles = roleRemoteService.findAllObject();
		List<Role> roles = SUtils.dt(roleRemoteService.findListBy(new String[]{"identifier"},new String[]{"xinyunCustom"}),Role.class);
		Map<String, Role> roleMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(roles)) {
			for (Role role : roles) {
				if (StringUtils.isNotEmpty(role.getSubSystem())) {
					roleMap.put(role.getUnitId() + "_" + role.getSubSystem().trim() ,role);
				}

			}
		}
//		List<RolePerm> rolePermLit = rolePermService.findAll();
		List<RolePermission> rolePermissionList = SUtils.dt(rolePermissionRemoteService.findAll() ,RolePermission.class);

        Map<String, RolePermission> rolePermissionMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(rolePermissionList)) {
            for (RolePermission rolePermission : rolePermissionList) {
                rolePermissionMap.put(rolePermission.getRoleId() + "_" + rolePermission.getModuleId(), rolePermission);
            }
        }

		List<ModelOperation> modelOperations = SUtils.dt(modelOperationRemoteService.findAll(), ModelOperation.class);
		Map<Integer, List<ModelOperation>> modelOpetionMap = modelOperations.stream().collect(Collectors.groupingBy(ModelOperation::getModelId));
//		List<Unit> unitList = SUtils.dt( unitRemoteService.findAll() , Unit.class);
		List<Unit> unitList = SUtils.dt( unitRemoteService.findListByIds(unitIds.toArray(new String[unitIds.size()])) , Unit.class);
		List<Role> roleList = new ArrayList<>();
		List<RolePermission> rolePerms = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(unitList)) {
			for (Unit unit : unitList) {
				if (unit.getIsDeleted() == 1) {
					continue;
				}
				for (Server server : serverList) {
					if (server.getSubId() == null) {
						continue;
					}
					Role role = roleMap.get(unit.getId() + "_" + server.getSubId());
					if (role == null) {
						role  = new Role();
						role.setUnitId(unit.getId());
						role.setName(server.getName() );
						role.setDescription(server.getName() +"   拥有此系统所有模块权限");
						role.setIsActive(String.valueOf(1));
						role.setSubSystem(String.valueOf(server.getSubId()));
						role.setIdentifier("xinyunCustom");
						role.setRoleType(0);
						role.setIsSystem(0);
						role.setId(UuidUtils.generateUuid());

						roleList.add(role);
					}

					List<Model> models = modelMap.get(server.getSubId());
//                    List<RolePermission> permissionList = new ArrayList<>();
//                    boolean hasModule = false;
					if (CollectionUtils.isNotEmpty(models)) {
						for (Model model : models) {
							if (model.getUnitClass() == null) {
								continue;
							}
							if (StringUtils.isEmpty(model.getUserType())) {
								continue;
							}
							if(model.getUnitClass().equals(unit.getUnitClass()) && model.getUserType().indexOf(String.valueOf(unit.getUnitType())) >= 0){
								String key = role.getId() + "_" + model.getId();
								RolePermission perm = rolePermissionMap.get(key);
								if (perm != null) {
									continue;
								}
								perm = new RolePermission();

								perm.setModuleId(String.valueOf(model.getId()));
								perm.setId(UuidUtils.generateUuid());
								perm.setRoleId(role.getId());
								perm.setType(new Integer(3));
								List<ModelOperation> operations = modelOpetionMap.get(model.getId());
								StringBuilder sb = new StringBuilder();
								if (CollectionUtils.isNotEmpty(operations)) {

									for (ModelOperation operation : operations) {
										sb.append(operation.getId() + ",");
									}

								}
								if (sb.length() > 1) {
									perm.setOperationIds(sb.substring(0,sb.length()-1));
								}

//                                permissionList.add(perm);
//                                if (!"-1".equals(model.getParentId())) {
//                                    hasModule = true;
//                                }
								rolePerms.add(perm);
							}
						}
					}
                    //解决该系统下模块如果都不可用则不显示该系统 关联的模块
//                    if (hasModule) {
//                        rolePerms.addAll(permissionList);
//                    }

				}

			}
		}
		if (CollectionUtils.isNotEmpty(roleList)) {
			roleRemoteService.saveAll(roleList.toArray(new Role[0]));
		}
		if (CollectionUtils.isNotEmpty(rolePerms)) {
//			rolePermissionRemoteService.saveAll(rolePerms.toArray(new RolePermission[0]));
//            rolePermissionRemoteService.save(rolePerms.get(0));
            rolePermissionRemoteService.saveAll(SUtils.s(rolePerms));
		}
	}

	@Override
	public void saveRole(Map<String, Map<String,String>> teacherSerCodeMap ) {

		List<Role> roleList = SUtils.dt(roleRemoteService.findListBy(new String[]{"identifier"},new String[]{"xinyunCustom"}),Role.class);
		List<Server> serverList = serverRemoteService.findByOrderTypeAndStatus(new Integer[]{1,2,3},Server.STATUS_TURNON);
		Map<String, Integer> serverMap = new HashMap<>();
		for (Server server : serverList) {
			if (StringUtils.isEmpty(server.getCode())) {
				continue;
			}
			if (server.getSubId() == null) {
				continue;
			}
			serverMap.put(server.getCode(), server.getSubId());
		}
		Map<String, Role> roleMap = new HashMap<>();
		Set<String> roleIdSet = new HashSet<>();
		if (CollectionUtils.isNotEmpty(roleList)) {
			for (Role role : roleList) {
				if (StringUtils.isNotEmpty(role.getSubSystem())) {
					roleMap.put(role.getUnitId() + "_" +role.getSubSystem(), role);
				}
				roleIdSet.add(role.getId());
			}
		}

		List<User> userList = SUtils.dt(userRemoteService.findByUnitIds(teacherSerCodeMap.keySet().toArray(new String[0])) ,User.class) ;
		Map<String, User> userOwnerMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(userList)) {
			for (User user : userList) {
				userOwnerMap.put(user.getOwnerId(), user);
			}
//			userOwnerMap = userList.stream().collect(Collectors.toMap(User::getOwnerId, Function.identity()));
		}
		List<UserRole> userRoles = SUtils.dt(userRoleRemoteService.findListByIn("roleId", roleIdSet.toArray(new String[0])), UserRole.class);
		Map<String, UserRole> userRoleMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(userRoles)) {
			for (UserRole userRole : userRoles) {
				userRoleMap.put(userRole.getRoleId() + "_" + userRole.getUserId() , userRole);
			}
		}

		List<UserRole> userRoleList = new ArrayList<>();
		Set<String> userIdSet = new HashSet<>();
		if (teacherSerCodeMap != null) {
			for (Map.Entry<String, Map<String,String>> entry : teacherSerCodeMap.entrySet()) {
                Map<String,String> map = entry.getValue();
                String unitId = entry.getKey();
                for (Map.Entry<String, String> stringEntry : map.entrySet()) {
                    String teacherId = stringEntry.getKey();
                    String serverCodes = stringEntry.getValue();
                    User user = userOwnerMap.get(teacherId);
                    if (user == null) {
                        continue;
                    }
                    String userId = user.getId();
					userIdSet.add(userId);
                    if (StringUtils.isEmpty(serverCodes)) {
//                        if (CollectionUtils.isNotEmpty(roleList)) {
//                            for (Role role : roleList) {
//								if (unitId.equals(role.getUnitId())) {
////									UserRole userRole = userRoleMap.get(role.getId() + "_" + userId);
////									if (userRole != null) {
////										continue;
////									}
//									UserRole userRole = new UserRole();
//									userRole.setId(UuidUtils.generateUuid());
//									userRole.setRoleId(role.getId());
//									userRole.setUserId(userId);
//									userRoleList.add(userRole);
//								}
//                            }
//                        }
                    }else{
                        String[] serCodeArr = serverCodes.split(",");
                        for (String code : serCodeArr) {
							Integer subId = serverMap.get(code);
                            Role role = roleMap.get(unitId+"_"+subId);
                            if (role == null) {
                                continue;
                            }
//							UserRole userRole = userRoleMap.get(role.getId() + "_" + userId);
//							if (userRole != null) {
//								continue;
//							}
							UserRole userRole = new UserRole();
                            userRole.setId(UuidUtils.generateUuid());
                            userRole.setRoleId(role.getId());
                            userRole.setUserId(userId);
                            userRoleList.add(userRole);
                        }
                    }
                }


			}
		}
		if (CollectionUtils.isNotEmpty(userIdSet)) {
			userRoleRemoteService.deleteByUserIdIn(userIdSet.toArray(new String[0]));
		}
		if (CollectionUtils.isNotEmpty(userRoleList)) {
			userRoleRemoteService.saveAll(userRoleList.toArray(new UserRole[0]));
		}
	}

	@SuppressWarnings("null")
	@Override
	public void saveTeacher(String value) {
		// TODO Auto-generated method stub
		JSONArray array = Json.parseArray(value);
		System.out.println("获取到教师的数据：--------" +  array.size());
		Set<String> unitIds = new HashSet<>();
		List<Teacher> saveTeacherList = new ArrayList<>();
		List<User> saveUserList = new ArrayList<>();
		Map<String, Map<String, String>> unitIdServerMap = new HashMap<String, Map<String,String>>();
		Map<String, String> serverCodeMap = getServerCodeMap();
		if(array != null && array.size() >= 1 ){
			Set<String> useridSet = new HashSet<>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				unitIds.add(doChangeId(js.getString("organizationId")));
				useridSet.add(doChangeId(js.getString("userId")));
			}
			List<Teacher> allUnitTeacher = SUtils.dt(teacherRemoteService.findByUnitIdIn(unitIds.toArray(new String[unitIds.size()])), new TR<List<Teacher>>() {});
			Map<String,Teacher> teacherMap = EntityUtils.getMap(allUnitTeacher, Teacher::getId);
//			List<User> allTeacherList = SUtils.dt(userRemoteService.findListByIds(useridSet.toArray(new String[useridSet.size()])),new TR<List<User>>() {});
//			Map<String,User> allUserMap = EntityUtils.getMap(allTeacherList, User::getOwnerId);
			List<User> userList = SUtils.dt(userRemoteService.findByOwnerIds(useridSet.toArray(new String[useridSet.size()])), new TypeReference<List<User>>() {});
			Map<String,User> allUserMap = EntityUtils.getMap(userList, User::getOwnerId);
			
			Map<String, List<Dept>> deptMap = SUtils.dt(deptRemoteService.findByUnitIdMap(unitIds.toArray(new String[unitIds.size()])), new TypeReference<Map<String, List<Dept>>>(){});
			Set<String> userNameSet = new HashSet<String>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String tid = doChangeId(js.getString("userId"));
				String teacherName = js.getString("userName");
				Integer sex = doChangeSex(js.getString("gender"));
				String state = js.getString("userInfoChange");  //用户的状态，是否是修改用户的名称
				String newUserId = null;
				if(StringUtils.isNotBlank(state) && "loginName".equals(state)){
					newUserId = UuidUtils.generateUuid();
				}
				String username = js.getString("loginName");  //登陆名称
				//判断账号是否已经添加了，是的话直接跳过，
				if(CollectionUtils.isNotEmpty(userNameSet) && userNameSet.contains(username)){
					System.out.println("账号出现重复的-------------" + username);
					continue;
				} 
				userNameSet.add(username);
				String unitId = doChangeId(js.getString("organizationId"));
				String identityCard = StringUtils.trimToEmpty(js.getString("certificateNumber")); //身份证号需要提供  TODO
//				String identityCardType = js.getString("");
				String mobilePhone = js.getString("phoneNumber");
				String teacherCode = js.getString("worker_code");
				if(StringUtils.isBlank(teacherCode)){
					teacherCode = "00001";
				}
				Date birthday = DateUtils.string2Date(js.getString("birthdate"));
//				String cardNumber = js.getString("card_code");  
				
				String email = js.getString("mailAddress");
				Integer isDeleted = doChargeIsDeleted(js.getString("isDelete"));
//				isAdmin字段，1表示是，0或null表示不是
				String isAdmin = js.getString("isAdmin");
				Integer userType = User.USER_TYPE_COMMON_USER;
				if(StringUtils.isNotBlank(isAdmin) && "1".equals(isAdmin)){
//					userType = User.USER_TYPE_UNIT_ADMIN;
					teacherName = teacherName + XyConstant.XY_AFTER_TEACHER_NAME;
				}
				
//				String serverCode = StringUtils.isBlank(js.getString("serverCode")) ? null : js.getString("serverCode");
				Map<String,String> serverTMap;
				if(MapUtils.isNotEmpty(unitIdServerMap) && unitIdServerMap.get(unitId) != null){
					serverTMap = unitIdServerMap.get(unitId);
				}else{
					serverTMap = new HashMap<>();
				}
				if(User.USER_TYPE_COMMON_USER == userType){
					String modeNames = js.getString("modeNames");
					if(StringUtils.isNotBlank(modeNames)){
					    getServerUserMapTime(serverTMap, modeNames,tid, serverCodeMap);
					}
				}
				unitIdServerMap.put(unitId, serverTMap);
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
				
				Long displayOrder;
				if(RedisUtils.get("syncdata.xingyun.teacher.displayorder.max." + unitId) == null) {
					if(CollectionUtils.isEmpty(allUnitTeacher)) {
						displayOrder = (long)1;
					}else {
						displayOrder = (long) allUnitTeacher.size();
					}
				}
				displayOrder = RedisUtils
						.incrby("syncdata.xingyun.teacher.displayorder.max." + unitId, 1);
				t.setDisplayOrder( t.getDisplayOrder() == null ? displayOrder.intValue() : t.getDisplayOrder());
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
				t.setEmail(email);
				t.setIsDeleted(isDeleted);
				saveTeacherList.add(t);
//							----------封装用户	
				if(StringUtils.isNotBlank(username)){
					User user;
					boolean isUpdate = Boolean.TRUE;
					if(allUserMap != null && !allUserMap.isEmpty() && allUserMap.get(tid) != null){
						user = allUserMap.get(tid);
					}else{
						user = new User();
						user.setId(tid);
						user.setAccountId(UuidUtils.generateUuid());
						user.setPassword(getDefaultPwd());
						
						user.setOwnerType(User.OWNER_TYPE_TEACHER);
						user.setUsername(username);
						isUpdate = Boolean.FALSE;
					}
					user.setDisplayOrder(t.getDisplayOrder());
					user.setOwnerId(tid);
					user.setUnitId(unitId);
					user.setSex(t.getSex());
					user.setRealName(t.getTeacherName());
					//判断是否是管理员
					user.setUserType(userType);
					user.setIdentityCard(t.getIdentityCard());
					user.setMobilePhone(mobilePhone);
					user.setBirthday(t.getBirthday());
					user.setEmail(t.getEmail());
					if(isUpdate && StringUtils.isNotBlank(state) && "loginName".equals(state)  &&  !username.equals(user.getUsername())){
						//先进行软删，再进行新增数据
						user.setIsDeleted(1);
					}else{
						user.setIsDeleted(t.getIsDeleted());
					}
					saveUserList.add(user);
					
					if(isUpdate && StringUtils.isNotBlank(state) && "loginName".equals(state) && StringUtils.isNotBlank(newUserId)  &&  !username.equals(user.getUsername())){
						User user1 = new User();
						user1.setId(newUserId);
						user1.setAccountId(UuidUtils.generateUuid());
						user1.setPassword(getDefaultPwd());
						user1.setOwnerType(User.OWNER_TYPE_TEACHER);
						user1.setUsername(username);
						user1.setDisplayOrder(t.getDisplayOrder());
						user1.setOwnerId(tid);
						user1.setUnitId(unitId);
						user1.setSex(t.getSex());
						user1.setRealName(t.getTeacherName());
						//判断是否是管理员
						user1.setUserType(userType);
						user1.setIdentityCard(t.getIdentityCard());
						user1.setMobilePhone(mobilePhone);
						user1.setBirthday(t.getBirthday());
						user1.setEmail(t.getEmail());
						user1.setIsDeleted(t.getIsDeleted());
						saveUserList.add(user1);
					}
					
//					if(User.USER_TYPE_COMMON_USER == userType){
//						String modeNames = js.getString("modeNames");
//						if(StringUtils.isNotBlank(modeNames)){
////							if(isUpdate && StringUtils.isNotBlank(newUserId) ){
////								getServerUserMapTime(serverTMap, modeNames,newUserId, serverCodeMap);
////							}else{
//								getServerUserMapTime(serverTMap, modeNames,tid, serverCodeMap);
//						}
//					}
//					unitIdServerMap.put(unitId, serverTMap);
				}
			}
			for (Teacher teacher : saveTeacherList) {
				if(teacherMap == null || teacherMap.isEmpty() || teacherMap.get(teacher.getId()) == null){
					String uId = teacher.getUnitId();
					if(deptMap != null || deptMap.get(uId) != null){				
						List<Dept> depts = deptMap.get(uId);
						if(CollectionUtils.isNotEmpty(depts)){
							for (Dept dept : depts) {
								if(dept.getIsDefault() != null && dept.getIsDefault() == 1){
									teacher.setDeptId(dept.getId());
									break;
								}
							}
						}else{
							teacher.setDeptId(Unit.TOP_UNIT_GUID);  //教师单位不存在时， 部门id 保存为 32个 0
						}
					}
				}
			}
		}
		//进行保存教师和学生数据
		System.out.println("数据同步到教师的数据：--------" +  saveTeacherList.size() + "同步用户的数据是：------" + saveUserList.size());
		if(CollectionUtils.isNotEmpty(saveTeacherList)){
			if(CollectionUtils.isNotEmpty(saveUserList)){
				baseSyncSaveService.saveTeacherAndUser(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]),saveUserList.toArray(new User[saveUserList.size()]));
			}else{
				baseSyncSaveService.saveTeacher(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]));
			}
		}
		if(MapUtils.isNotEmpty(unitIdServerMap)){
			//进行角色授权
     		saveRole(unitIdServerMap);
		}
	}

	@Override
	public void saveStudent(String value) {
		JSONArray array = Json.parseArray(value);
		Set<String> unitIds = new HashSet<>();
		if(array != null){
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String schoolId = doChangeId(js.getString("organizationId")); 
				unitIds.add(schoolId);	
			}
			List<Student> studentList=SUtils.dt(studentRemoteService.findBySchoolIdIn(null, unitIds.toArray(new String[unitIds.size()])), new TR<List<Student>>(){});
			Map<String, Student> allStuMap = EntityUtils.getMap(studentList, Student::getId);
			List<User> allStuList = SUtils.dt(userRemoteService.findByUnitIds(unitIds.toArray(new String[unitIds.size()])),new TR<List<User>>() {});
			Map<String,User> allUserMap = EntityUtils.getMap(allStuList, User::getOwnerId);
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdIn(unitIds.toArray(new String[unitIds.size()])), Clazz.class);
			Map<String, List<Clazz>> allClazzMap = EntityUtils.getListMap(clazzList, Clazz::getSchoolId, Function.identity()); 
			List<Student> saveStudentList = new ArrayList<>();
			List<User> saveUserList = new ArrayList<>();
			
			Set<String> userNameSet = new HashSet<String>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String studentId = doChangeId(js.getString("userId"));
				String studentName = js.getString("userName");
				String schoolId = doChangeId(js.getString("organizationId"));
				String className = js.getString("grade") + js.getString("classNumber") + "班";
				String state = js.getString("userInfoChange"); //用户的状态，是否是修改用户的名称
				String classId = null;
				if(!MapUtils.isEmpty(allClazzMap) && CollectionUtils.isNotEmpty(allClazzMap.get(schoolId))){
					List<Clazz> unitClazzs = allClazzMap.get(schoolId);
					for (Clazz c : unitClazzs) {
						if(className.equals(c.getClassName()))
							classId = c.getId();
					}
				}
				Integer sex = doChangeSex(js.getString("gender"));
				String username = js.getString("loginName");
				//判断账号是否已经添加了，是的话直接跳过，
				if(CollectionUtils.isNotEmpty(userNameSet) && userNameSet.contains(username)){
					continue;
				} 
				userNameSet.add(username);
				
				String identityCard = StringUtils.trimToEmpty(js.getString("certificateNumber")); //学生的身份证需要提供 TODO
				Integer isDeleted = doChargeIsDeleted(js.getString("isDelete"));
//				Integer isLeaveSchool = doChargeIsLeaveSchool(js.getString("school_rool_status"));
				if(StringUtils.isNotBlank(identityCard) && identityCard.length() > 30){
					identityCard = identityCard.substring(0, 29);
				}
				String studentCode = StringUtils.trimToEmpty(js.getString("studentNumber"));
				if(StringUtils.isBlank(studentCode)){
					studentCode = StringUtils.isBlank(identityCard) ? BaseSaveConstant.DEFAULT_STU_CODE 
							: identityCard;
				}
				if(StringUtils.isNotBlank(studentCode) && studentCode.length() > 20){
					studentCode = studentCode.substring(0, 19);
				}
//				----补充字段
				String unitiveCode = js.getString("studentCode"); //学籍号
				unitiveCode = StringUtils.trimToEmpty(unitiveCode);
				if(StringUtils.isNotBlank(unitiveCode) && unitiveCode.length() > 30){
					unitiveCode = unitiveCode.substring(0, 29); 
				}
				String mobilePhone = js.getString("phoneNumber");
				
//				--------封装学生数据
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
				student.setSex(sex);
				student.setStudentCode(studentCode);
				student.setUnitiveCode(unitiveCode);
				student.setIdentityCard(identityCard);
				student.setIsDeleted(isDeleted);
				student.setMobilePhone(mobilePhone);
				saveStudentList.add(student);
//				----------封装用户	
				if(StringUtils.isNotBlank(username)){
					User user;
					boolean isUpdate = Boolean.TRUE;
					if(allUserMap != null && !allUserMap.isEmpty() && allUserMap.get(studentId) != null){
						user = allUserMap.get(studentId);
					}else{
						user = new User();
						user.setId(studentId);
						user.setPassword(getDefaultPwd());
						user.setOwnerType(User.OWNER_TYPE_STUDENT);
						user.setUserType(User.USER_TYPE_COMMON_USER);
						user.setUsername(username);
						isUpdate = Boolean.FALSE;
					}
					user.setOwnerId(studentId);
					user.setUnitId(schoolId);
					user.setSex(student.getSex());
					user.setRealName(student.getStudentName());
					if(isUpdate && StringUtils.isNotBlank(state) && "loginName".equals(state)  &&  !username.equals(user.getUsername())){
						//先进行软删，再进行新增数据
						user.setIsDeleted(1);
					}else{
						user.setIsDeleted(student.getIsDeleted());
					}
					user.setIdentityCard(student.getIdentityCard());
					user.setMobilePhone(student.getMobilePhone());
					saveUserList.add(user);
					
					if(isUpdate && StringUtils.isNotBlank(state) && "loginName".equals(state)  &&  !username.equals(user.getUsername())){
						User user1 = new User();
						user1.setId(UuidUtils.generateUuid());
						user1.setPassword(getDefaultPwd());
						user1.setOwnerType(User.OWNER_TYPE_STUDENT);
						user1.setUserType(User.USER_TYPE_COMMON_USER);
						user1.setUsername(username);
						//判断是否是管理员
						user1.setOwnerId(studentId);
						user1.setUnitId(schoolId);
						user1.setSex(student.getSex());
						user1.setRealName(student.getStudentName());
						user1.setIsDeleted(student.getIsDeleted());
						user1.setIdentityCard(student.getIdentityCard());
						user1.setMobilePhone(student.getMobilePhone());
						saveUserList.add(user1);
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
	public void saveFamily(String value) {
		// TODO Auto-generated method stub
		JSONArray array = Json.parseArray(value);
		if(array == null || array.size() == 0) {
			log.info("------------开始解析家长数据，总数：0");
			return;
		}
		log.info("------------开始解析家长数据，总数：" + array.size());
		//先解析出数据中的 家长的手机号,学生的id
		Set<String> famIdList = new HashSet<>();
		Set<String> stuIdList = new HashSet<>();
		Set<String> nameList = new HashSet<>();
		Set<String> phoneList = new HashSet<>();
		Set<String> userNameSet = new HashSet<String>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			String famId = doChangeId(js.getString("userId"));
			String students = js.getString("students");
			JSONArray studentList = Json.parseArray(students);
			for (int j = 0; j < studentList.size(); j++) {
				JSONObject stu = studentList.getJSONObject(j);
				String studentId = doChangeId(stu.getString("studentUserId"));
				stuIdList.add(studentId);
			}
			nameList.add(js.getString("userName"));
			String phone = js.getString("phoneNumber");
			phoneList.add(phone);
			famIdList.add(famId);
		}
		Map<String, Student> stuidMap = null;
		Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
		if(CollectionUtils.isNotEmpty(stuIdList)){
			List<Student> allStudents = SUtils.dt(studentRemoteService.findListByIds(
					stuIdList.toArray(new String[stuIdList.size()])), new TR<List<Student>>() {});
			stuidMap = EntityUtils.getMap(allStudents, Student::getId);
		}
		List<User> famUsers = SUtils.dt(userRemoteService.findByUserIdCodeIn(
				famIdList.toArray(new String[famIdList.size()])), User.class);
		famUsers = EntityUtils.filter2(famUsers, t->{
    		return t.getIsDeleted() == 0;
    	});
		Map<String, List<User>> userIdCodeMap = EntityUtils.getListMap(famUsers, User::getUserIdCode, Function.identity());
		Map<String, User> ownerIdMap = EntityUtils.getMap(famUsers, User::getOwnerId);
		List<Family> saveFamilys = new ArrayList<>();
		List<User>   saveUsers   = new ArrayList<>();
		//关系转化Map<String，String>
		Map<String, String> conversionMap = new HashMap<>();
		conversionMap.put("父亲", "51"); // 父亲
		conversionMap.put("母亲", "52"); // 母亲
		conversionMap.put("爷爷", "61"); // 祖父 （我们没有转为其他）
		conversionMap.put("奶奶", "62"); // 祖母
		conversionMap.put("外祖父", "63"); // 外祖父
		conversionMap.put("外祖母", "64"); // 外祖母
		conversionMap.put("亲属", "97"); // 其他亲属
		conversionMap.put("其他", "80"); // 监护人（我们没有转为其他） --把is_guardian 设为 1
		for (int i = 0; i < array.size(); i++) {
			// 开始---------转换字段
			JSONObject js = array.getJSONObject(i);
			String familyId = doChangeId(js.getString("userId"));
			String state = js.getString("userInfoChange");  //用户的状态，是否是修改用户的名称
			String username = js.getString("loginName");
			//判断账号是否已经添加了，是的话直接跳过，
			if(CollectionUtils.isNotEmpty(userNameSet) && userNameSet.contains(username)){
				continue;
			} 
			userNameSet.add(username);
			String realName = js.getString("userName");
			String phone = js.getString("phoneNumber");  
			int isDeleted = doChargeIsDeleted(js.getString("isDelete"));
			Date birthday = DateUtils.string2Date(js.getString("birthdate"));
			Integer sex = doChangeSex(js.getString("gender"));
			String students = js.getString("students");
			JSONArray studentList = Json.parseArray(students);
			
			Map<String, Family> familyStuMap = new HashMap<String, Family>();
			if(MapUtils.isNotEmpty(userIdCodeMap) && CollectionUtils.isNotEmpty(userIdCodeMap.get(familyId))){
				List<User> havUsers = userIdCodeMap.get(familyId);
				Set<String> ownerIdSet1 = EntityUtils.getSet(havUsers, User::getOwnerId);
				List<Family> allFamilys = SUtils.dt( familyRemoteService.findListByIds(ownerIdSet1.toArray(new String[ownerIdSet1.size()])),new TR<List<Family>>() {});
				familyStuMap = EntityUtils.getMap(allFamilys, Family::getStudentId);
			}
			// 结束---------转换字段
			for (int j = 0; j < studentList.size(); j++) {
			 // 开始 ---------封装family
				JSONObject stu = studentList.getJSONObject(j);
				String studentId = doChangeId(stu.getString("studentUserId"));
				String relationId = stu.getString("relationship");  
				Integer isGuardian = stu.getInteger("isMain");
				if(j != 0){
					username = username + j + System.currentTimeMillis();
				}
				relationId = conversionMap.get(relationId);
				if(StringUtils.isBlank(relationId)){
					relationId = "97";
				}
				String unitId;
				if(stuidMap.isEmpty() || stuidMap.get(studentId) == null) {
					unitId = topUnit.getId();
				}else {
					unitId = stuidMap.get(studentId).getSchoolId();
				}
				Family family;
				if(MapUtils.isNotEmpty(familyStuMap) && familyStuMap.get(studentId) != null) {
					family = familyStuMap.get(studentId);
				}else {
					family = new Family();
					family.setId(UuidUtils.generateUuid());
					family.setStudentId(studentId);
					family.setCreationTime(new Date());
					family.setEventSource(WenXunConstant.DEFAULT_EVENT_SOURCE_VALUE);
					if(MapUtils.isNotEmpty(userIdCodeMap)){
						username = username + (userIdCodeMap.get(familyId).size() + 1) +  System.currentTimeMillis();
					}
				}
				//关键字段
				family.setSchoolId(unitId);
				family.setRelation(relationId);
				family.setRealName(realName);
				family.setMobilePhone(phone);
				family.setIsDeleted(isDeleted);
				family.setSex(sex);
				//补充信息字段
				family.setModifyTime(new Date());
				family.setIsGuardian(isGuardian);
				family.setBirthday(birthday);
				saveFamilys.add(family);
			 // 结束 ---------封装family
			// 开始 ---------封装user 
				User user = null;
				boolean isUpdate = Boolean.TRUE;
				if(MapUtils.isNotEmpty(ownerIdMap) &&  ownerIdMap.get(family.getId()) != null){
					user = ownerIdMap.get(family.getId());
				}else{
					user = new User();
					user.setId(UuidUtils.generateUuid());
					user.setPassword(getDefaultPwd());
					user.setOwnerType(User.OWNER_TYPE_FAMILY);
					user.setUsername(username);
					user.setUserIdCode(familyId);
					user.setUserType(User.USER_TYPE_COMMON_USER);
					isUpdate = Boolean.FALSE;
				}
				user.setOwnerId(family.getId());
				user.setUnitId(unitId);
				user.setSex(family.getSex());
				user.setRealName(family.getRealName());
				user.setIdentityCard(family.getIdentityCard());
				if(isUpdate && StringUtils.isNotBlank(state)  && "loginName".equals(state)  &&  !username.equals(user.getUsername())){
					//先进行软删，再进行新增数据
					user.setIsDeleted(1);
				}else{
					user.setIsDeleted(family.getIsDeleted());
				}
				user.setMobilePhone(phone);
				user.setBirthday(family.getBirthday());
				saveUsers.add(user);
		    // 结束 ---------封装user	
				if(isUpdate && StringUtils.isNotBlank(state)  && "loginName".equals(state)  &&  !username.equals(user.getUsername())){
					User user1 = new User();
					user1.setId(UuidUtils.generateUuid());
					user1.setPassword(getDefaultPwd());
					user1.setOwnerType(User.OWNER_TYPE_FAMILY);
					user1.setUsername(username);
					user1.setUserIdCode(familyId);
					user1.setUserType(User.USER_TYPE_COMMON_USER);
					//判断是否是管理员
					user1.setOwnerId(family.getId());
					user1.setUnitId(unitId);
					user1.setSex(family.getSex());
					user1.setRealName(family.getRealName());
					user1.setIdentityCard(family.getIdentityCard());
					user1.setIsDeleted(family.getIsDeleted());
					user1.setMobilePhone(phone);
					user1.setBirthday(family.getBirthday());
					saveUsers.add(user1);
				}
			}
		}
		//保存家长数据 
		if(CollectionUtils.isNotEmpty(saveFamilys)) {
			if(CollectionUtils.isNotEmpty(saveUsers)){
				baseSyncSaveService.saveFamilyAndUser(saveFamilys.toArray(new Family[saveFamilys.size()]),saveUsers.toArray(new User[saveUsers.size()]));
			}else{
				baseSyncSaveService.saveFamily(saveFamilys.toArray(new Family[0]));
			}
		}
	}

	// -----------------------------------私有方法区 ------------------------------------------
	
	/**
	 * 得到默认加密密码
	 * @return
	 */
	private String getDefaultPwd() {
		return new PWD(BaseSaveConstant.DEFAULT_PASS_WORD_VALUE).encode();
	}
	
	/**
	 * 根据年级名称 来得到 学段
	 * @param
	 * @return
	 */
	private Map<String, String> getMapByGradeName(String gradeName) {
		String section = null, gradeCode = null, num = null;
		if(StringUtils.isNotBlank(gradeName)){
			switch (gradeName) {
			case "一年级":
				section = "1"; 
				gradeCode = "11";
				num = "0";
				break;
			case "二年级":
				section = "1"; 
				gradeCode = "12";
				num = "1";
				break;
			case "三年级":
				section = "1"; 
				gradeCode = "13";
				num = "2";
				break;
			case "四年级":
				section = "1"; 
				gradeCode = "14";
				num = "3";
				break;
			case "五年级":
				section = "1";
				gradeCode = "15";
				num = "4";
				break;
			case "六年级":
				section = "1";
				gradeCode = "16";
				num = "5";
				break;
			case "七年级":
				section = "2";
				gradeCode = "21";
				num = "0";
				break;
			case "八年级":
				section = "2";
				gradeCode = "22";
				num = "1";
				break;
			case "九年级":
				section = "2";
				gradeCode = "23";
				num = "2";
				break;
			case "高一":
				section = "3";
				gradeCode = "31";
				num = "0";
				break;
			case "高二":
				section = "3";
				gradeCode = "32";
				num = "1";
				break;
			case "高三":
				section = "3";
				gradeCode = "33";
				num = "2";
				break;
			default:
				break;
			}
		}
		Map<String, String> keyMap = Maps.newHashMap();
		keyMap.put("section", section);
		keyMap.put("gradeCode", gradeCode);
		keyMap.put("num", num);
		keyMap.put("gradeName", gradeName);
		return keyMap;
	}

	
	/**
	 * id不足32位的， 前面补为0
	 * @param id
	 * @return
	 */
	private String doChangeId(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		return StringUtils.leftPad(id, 32, "0");
	}
	
	/**
	 * 性别转化
	 * @param
	 * @return
	 */
	private Integer doChangeSex(String sex) {
		Integer endSex = 0;
		if(StringUtils.isNotBlank(sex)){
			switch (sex) {
			case "0":
				endSex = User.USER_WOMAN_SEX; //女
				break;
			case "1":
				endSex = 1;  //男
				break;
			case "2":
				endSex = 0;
				break;
			default:
				break;
			}
		}
		return endSex;
	}
	
	/**
	 * 根据学校类型 得到 学段
	 * @param schoolType
	 * @return
	 */
	private String doChargeSection(String schoolType) {
		String section = "1,2,3";//其他培训机构（含社会培训机构）;
		if(StringUtils.isNotBlank(schoolType)){
			switch (schoolType) {
			case "111":
				section = "0"; 
				break;
			case "211":
				section = "1"; 
				break;
			case "221":
				section = "1"; 
				break;
			case "311":
				section = "2"; 
				break;
			case "321":
				section = "2";
				break;
			case "331":
				section = "2";
				break;
			case "342":
				section = "3";
				break;
			case "351":
				section = "3";
				break;
			case "361":
				section = "3";
				break;
			case "371":
				section = "1,2,3";
				break;
			default:
				break;
			}
		}
		return section;
	}
	
	
    /**
     * 得到学校类型
     * @param name
     * @return
     */
	private String doChangeSchoolType(String name) {
		String schoolType = "933";//其他培训机构（含社会培训机构）;
		if(StringUtils.isNotBlank(name)){
			switch (name) {
			case "幼儿园":
				schoolType = "111"; 
				break;
			case "小学":
				schoolType = "211"; 
				break;
			case "成人小学":
				schoolType = "221"; 
				break;
			case "普通初中":
				schoolType = "311"; 
				break;
			case "职业初中":
				schoolType = "321";
				break;
			case "成人初中":
				schoolType = "331";
				break;
			case "普通高中":
				schoolType = "342";
				break;
			case "成人高中":
				schoolType = "351";
				break;
			case "中等职业学校":
				schoolType = "361";
				break;
			case "工读学校":
				schoolType = "371";
				break;	
			default:
				break;
			}
		}
		return schoolType;
	}
	
	/**
	 * 得到 regionCode
	 * @param js
	 * @return
	 */
	private String doChargeRegionCode(JSONObject js) {
		String provinceCode = js.getString("provinceCode");
		String areaCode     = js.getString("areaCode");
		String cityCode     = js.getString("cityCode");
		String regionCode   = StringUtils.isNotBlank(areaCode) ? areaCode : StringUtils.isNotBlank(cityCode) ? cityCode
				: StringUtils.isNotBlank(provinceCode) ? provinceCode : "";
		if(StringUtils.isNotBlank(regionCode)){
			regionCode = StringUtils.rightPad(regionCode, 6, "0");
		}
		return regionCode;
	}
	
	/**
	 * 是否软删
	 * @param isDeleted
	 * @return
	 */
	private Integer doChargeIsDeleted(String isDeleted) {
		if(StringUtils.isBlank(isDeleted)){
			return BaseSaveConstant.DEFAULT_IS_DELETED_VALUE;
		}else if ("1".equals(isDeleted)){
			return BaseSaveConstant.DEFAULT_IS_DELETED_VALUE;
		}else if ("0".equals(isDeleted)){
			return BaseSaveConstant.TRUE_IS_DELETED_VALUE;
		}
		return Integer.valueOf(isDeleted);
	}
	
	/**
	 * 获取到 单位订购的模块  和 订购时间 
	 * @param serverUMap
	 * @param modeNames
	 */
	private void getServerUMapTime(Map<String, String> serverUMap,
			String modeNames, String unitId, Map<String, String> serverCodeMap) {
		JSONArray dataArray = JSON.parseArray(modeNames);
		JSONArray array = new JSONArray();
		for (int j = 0; j < dataArray.size(); j++) {
			JSONObject jsonObject = new JSONObject();
			JSONObject js = dataArray.getJSONObject(j);
			String modeName = js.getString("modeName");
			String code = serverCodeMap.get(modeName);
			if(StringUtils.isNotBlank(code)){
				String expireTime = js.getString("endTime");
				if(StringUtils.isNotBlank(expireTime)){
					Date  expireTime1 = DateUtils.string2Date(expireTime, "yyyy-MM-dd HH:mm:ss");
					jsonObject.put("expireTime", expireTime1);
					jsonObject.put("serverCode", code);
					array.add(jsonObject);
				}
			}
		}
		serverUMap.put(unitId, array.toJSONString());
	}
	
	/**
	 * 获取到 用户订购的模块 和 订购时间
	 * @param serverTMap
	 * @param modeNames
	 * @param tid
	 * @param serverCodeMap
	 */
	private void getServerUserMapTime(Map<String, String> serverTMap,
			String modeNames, String tid, Map<String, String> serverCodeMap) {
		// TODO Auto-generated method stub
		JSONArray dataArray = JSON.parseArray(modeNames);
		String codeString = "";
		for (int j = 0; j < dataArray.size(); j++) {
			JSONObject js = dataArray.getJSONObject(j);
			String modeName = js.getString("modeName");
			String code = serverCodeMap.get(modeName);
			if(StringUtils.isNotBlank(code)){
				Date  expireTime = js.getDate("endTime");
				Date  nowDate  = new Date();
				if(nowDate.compareTo(expireTime) != 1){
					if(j != 0){
						codeString = codeString + "," + code;
					}else{
						codeString = codeString + code;
					}
				}
//				String accessTime = js.getString("accessTime");
//				if(StringUtils.isNotBlank(accessTime)){
//					JSONArray accessTimeArray = JSON.parseArray(modeNames);
//					Date  expireTime  = accessTimeArray.getJSONObject(0).getDate("endTime");
//					Date  nowDate  = new Date();
//					if(nowDate.compareTo(expireTime) != 1){
//						serverTMap.put(tid, code);
//					}
//				}
			}
		}
		if(StringUtils.isNotBlank(codeString)){
			serverTMap.put(tid, codeString);
		}
	}
	
	/**
	 * 得到名称和code 对应的map 
	 * @return
	 */
	private Map<String, String> getServerCodeMap() {
		List<Server> serverList = serverRemoteService.findByOrderTypeAndStatus(new Integer[]{1,2,3},Server.STATUS_TURNON);
		serverList = EntityUtils.filter2(serverList, t->{
				return StringUtils.isNotBlank(t.getCode()) && StringUtils.isNotBlank(t.getName());
		});
		return EntityUtils.getMap(serverList, Server::getName, Server::getCode);
	}
}
