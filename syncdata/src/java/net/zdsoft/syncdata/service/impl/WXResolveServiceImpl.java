package net.zdsoft.syncdata.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.SchoolSemester;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachArea;
import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.TeacherDuty;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachAreaRemoteService;
import net.zdsoft.basedata.remote.service.TeachBuildingRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherDutyRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.syncdata.constant.WenXunConstant;
import net.zdsoft.syncdata.service.WXResolveService;
import net.zdsoft.syncdata.util.JledqSyncDataUtil;
import net.zdsoft.syncdata.util.WXPlatformUtils;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author yangsj  2018年4月23日下午4:25:07
 * 
 * 进行数据的解析和转化 的实现
 */
@Service("wXResolveService")
public class WXResolveServiceImpl implements WXResolveService {

	private Logger log = Logger.getLogger(WXResolveServiceImpl.class);
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private BaseSyncSaveRemoteService baseSyncSaveService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private TeacherDutyRemoteService teacherDutyRemoteService;
	@Autowired
	private TeachAreaRemoteService teachAreaRemoteService;
	@Autowired
	private TeachBuildingRemoteService teachBuildingRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	protected SystemIniRemoteService systemIniRemoteService;

	@Override
	public void saveUnit(String json) {
		JSONArray array = getData(json);
		if(array == null || array.size() == 0) {
			log.info("------------开始解析学校 数据，总数：0");
			return;
		}
		log.info("------------开始解析学校 数据，总数：" + array.size());
		//先解析出数据中的 单位code
		Set<String> schoolCodeList = new HashSet<>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			String schoolCode = js.getString("code");
			schoolCodeList.add(schoolCode);
		}
		List<School> allSchool = SUtils.dt(
				schoolRemoteService.findByCodeIn(schoolCodeList.toArray(new String[schoolCodeList.size()])), 
				new TR<List<School>>() {
		});
		Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getSchoolCode);
		Set<String> schoolIds = EntityUtils.getSet(allSchool, School::getId);
		List<Unit> allUnits = SUtils.dt(unitRemoteService.findListByIds(schoolIds.toArray(new String[schoolIds.size()])),
				new TR<List<Unit>>() {});
		Map<String, Unit> allUnitMap = EntityUtils.getMap(allUnits, Unit::getId);
		//查找出文轩要挂载的单位
		List<Unit> edus = SUtils.dt(unitRemoteService.findByUnitClassAndRegion(Unit.UNIT_CLASS_EDU, 
				WenXunConstant.WX_UNIT_REGION_CODE_VALUE), new TR<List<Unit>>() {
		});
		Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
		String parentId = topUnit.getId();
		for (Unit unit : edus) {
			if(Constant.GUID_ONE.equals(unit.getParentId())) {
				parentId = unit.getId();
				break;
			}
			if(Constant.GUID_ZERO.equals(unit.getParentId())){
				parentId = unit.getId();
				break; 
			}
		}
		
		List<School> saveSchools = new ArrayList<>();
		List<Unit> saveUnits = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
		// 开始---------转换字段
			JSONObject js = array.getJSONObject(i);
			String schoolCode = js.getString("code");
			//峨眉二小的不加前缀
			String schoolName = doChargeName(js.getString("name"));
			String regionCode = js.getString("place");
			if(regionCode.length() < 6) {
				regionCode = StringUtils.rightPad(regionCode, 6, "0");
			}
			String addr = js.getString("addr");
			String isDeleted = js.getString("isDeleted");
			String section = js.getString("eduStage");
			String schoolType = null;
			//学段，学校类型
		   if(StringUtils.isNotBlank(section)) {
					JSONArray sections = Json.parseArray(section);
					if(sections.size() == 1) {
						String sec = String.valueOf(sections.get(0));
						switch (sec) {
						case "1":
							section = "1";
							schoolType = "211";
							break;
						case "2":
							section = "2";
							schoolType = "311";
							break;
						case "4":
							section = "3";
							schoolType = "342";
							break;
						case "9":
							section = "0";
							schoolType = "111";
							break;
						case "3":
							section = "3";
							schoolType = "342";
							break;
					  }
					}else {
						int n = 0 ;
						for (int j= 0; j < sections.size(); j++) {
							Integer s1 = Integer.valueOf(sections.getString(j));
							n+= s1;
						}
						if(n == 3) {
							section = "1,2";
							schoolType = "312";
						}else if (n == 6) {
							section = "2,3";
							schoolType = "312";
						}else if(n == 7) {
							section = "1,2,3";
							schoolType = "345";
						}else {
							continue;
						}
					}
			}else {
				continue;
			}
		// 结束---------转换字段
		
		// 开始 ---------封装school
		    School school;
			if(allSchoolMap.get(schoolCode) != null) {
				school = allSchoolMap.get(schoolCode);	
			}else {
				school = new School();
				school.setId(UuidUtils.generateUuid());
				school.setEventSource(WenXunConstant.DEFAULT_EVENT_SOURCE_VALUE);
				school.setCreationTime(new Date());
				school.setRunSchoolType(831);
				//区域属性码
				school.setRegionPropertyCode("121");  //镇中心区
			}
			//关键字段
			school.setSchoolType(schoolType);
			school.setRegionCode(regionCode);
			school.setSections(section);
			school.setIsDeleted(Integer.valueOf(isDeleted));
			school.setSchoolName(schoolName);
			school.setSchoolCode(schoolCode);
			//补充信息字段
			school.setModifyTime(new Date());
			school.setAddress(addr);
			
			saveSchools.add(school);
		// 结束 ---------封装school	
		// 开始 ---------封装unit
			Unit u;
			if(allSchoolMap.get(schoolCode) != null) {
				u = allUnitMap.get(school.getId());
			}else {
				u = new Unit();
				u.setOrgVersion(1);
				u.setUnitState(1);
				u.setAuthorized(1);
				u.setUseType(1);
			}
			    u.setParentId(parentId);
				u.setId(school.getId());
				u.setEventSource(school.getEventSource());
				u.setCreationTime(school.getCreationTime());
				u.setRegionCode(school.getRegionCode());
				
				if(RedisUtils.get("syncdata.wenxuan.unit.unioncode.max.regioncode." + regionCode) == null) {
					RedisUtils.set("syncdata.wenxuan.unit.unioncode.max.regioncode." + regionCode, String.valueOf(allUnits.size()));
				}
				Long max = RedisUtils.incrby("syncdata.wenxuan.unit.unioncode.max.regioncode." + regionCode, 1);
				if(StringUtils.isEmpty(regionCode)) {
					u.setUnionCode(StringUtils.leftPad(max + "", 6, "0"));
				}else {
					u.setUnionCode(regionCode + StringUtils.leftPad(max + "", 6, "0"));
				}
				u.setDisplayOrder(u.getUnionCode());
				u.setRegionLevel(
						StringUtils.endsWith(regionCode, "0000") ? 3 : StringUtils.endsWith(regionCode, "00") ? 4 : 5);
				u.setUnitName(school.getSchoolName());
				//默认是学校
				u.setUnitClass(Unit.UNIT_CLASS_SCHOOL);
				u.setModifyTime(school.getModifyTime());
				u.setIsDeleted(school.getIsDeleted());
				u.setSchoolType(school.getSchoolType());
				u.setRunSchoolType(school.getRunSchoolType()); 
				if(school.getSections().contains("0")) {
					u.setUnitType(Unit.UNIT_SCHOOL_KINDERGARTEN);
				}else {
					u.setUnitType(Unit.UNIT_SCHOOL_ASP);
				}
				saveUnits.add(u);
		// 结束 ---------封装unit	
		}
//		if(CollectionUtils.isNotEmpty(saveSchools)) {
//			baseSyncSaveService.saveSchoolAndUnit(saveSchools.toArray(new School[0]),null);
//		}
		if(CollectionUtils.isNotEmpty(saveSchools)) {
			baseSyncSaveService.saveSchool(saveSchools.toArray(new School[0]));
		}
		if(CollectionUtils.isNotEmpty(saveUnits)) {
			baseSyncSaveService.saveUnit(saveUnits.toArray(new Unit[0]));
		}
		
	}

	@Override
	public void saveClass(String json) {
		JSONArray array = getData(json);
		if(array == null || array.size() == 0) {
			log.info("------------开始解析班级数据，总数：0");
			return;
		}
		log.info("------------开始解析班级数据，总数：" + array.size());
		//先解析出数据中的 学生id ,单位code ,班级classid
		Set<String> schoolCodeList = new HashSet<>();
		Set<String> clazzIdList = new HashSet<>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			String schoolCode = js.getString("schoolCode");
			String clazzId = js.getString("classId");
			
			schoolCodeList.add(schoolCode);
			clazzIdList.add(clazzId);
		}
		List<Clazz> allClazz = SUtils.dt(
				classRemoteService.findListByIds(clazzIdList.toArray(new String[clazzIdList.size()])),
				new TR<List<Clazz>>() {});
		Map<String, Clazz> allClazzMap = EntityUtils.getMap(allClazz, Clazz::getId);
		List<School> allSchool = SUtils.dt(
				schoolRemoteService.findByCodeIn(schoolCodeList.toArray(new String[schoolCodeList.size()])), 
				new TR<List<School>>() {
		});
		Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getSchoolCode);
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
		if (semester == null) {
			log.info("------ 请先维护学年学期 -------");
			return;
		}
		List<Clazz> saveClazzList = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
		// 开始---------转换字段
			JSONObject js = array.getJSONObject(i);
			String clazzId = js.getString("classId");
			String className = js.getString("className");
			String classCode = js.getString("classNo");
			String section = js.getString("teachLevel");
			if(StringUtils.isNotBlank(section)) {
				if( section.equals("3") || section.equals("4")) {
					section = "3";
				}else if (section.equals("9")) {
					section = "0";  //学前的班级不保存到数据库中
					continue;
				}
			}
			String enterYear = js.getString("enterYear");
			String acadyear = enterYear+"-"+(Integer.parseInt(enterYear)+1);
			int schoolingLength = Integer.parseInt(js.getString("eudLength"));
			//根据schoolcode 得到id
			String unitId;
			String classNameDynamic;
			String schoolCode = js.getString("schoolCode");
			if(allSchoolMap.isEmpty() || allSchoolMap.get(schoolCode) == null) {
				log.error("班级的"+clazzId +  "的单位为空");
				continue;
			}else {
				unitId = allSchoolMap.get(schoolCode).getId();
				classNameDynamic = allSchoolMap.get(schoolCode).getSchoolName();
			}
			String teacherId = js.getString("chargeTeacherId"); //班主任id,可以为空
			int isDeleted = Integer.parseInt(js.getString("isDeleted"));
		// 结束---------转换字段
			
		// 开始 ---------封装Clazz
			Clazz clazz;
			if(allClazzMap.get(clazzId) != null) {
				clazz = allClazzMap.get(clazzId);
			}else {
				clazz = new Clazz();
				clazz.setId(clazzId);
				clazz.setState(0);
				clazz.setArtScienceType(0);
				clazz.setEventSource(0);
				clazz.setCreationTime(new Date());
				clazz.setBuildDate(new Date());  //建班日期
			}
			//关键字段
			clazz.setClassName(className);
			clazz.setIsDeleted(isDeleted);
			clazz.setTeacherId(teacherId);
			clazz.setSchoolId(unitId);
			clazz.setSection(Integer.valueOf(section));
			clazz.setSchoolingLength(schoolingLength);
			
			//补充信息字段
			clazz.setModifyTime(new Date());
//			clazz.setCampusId(js.getString("XX_XQXX_ID").replaceAll("-", ""));   校区id没有赋值  -----------
			clazz.setAcadyear(acadyear);
			if(StringUtils.isBlank(classCode)) {
				String classCodePrefix = StringUtils.substring(clazz.getAcadyear(), 0, 4)
						+ StringUtils.leftPad(clazz.getSection() + "", 2, "0");
				Long max = RedisUtils
						.incrby("syncdata.bjdy.class.code.max." + clazz.getSchoolId() + classCodePrefix, 1);
				clazz.setClassCode(classCodePrefix + StringUtils.leftPad("" + max, 2, "0"));
			}else {
				clazz.setClassCode(classCode);
			}
			String year = StringUtils.substring(clazz.getAcadyear(), 0, 4);
			String year2 = StringUtils.substring(semester.getAcadyear(), 0, 4);
			if (NumberUtils.toInt(year2) >= (NumberUtils.toInt(year) + clazz.getSchoolingLength())) {
				clazz.setIsGraduate(1);
				clazz.setGraduateDate(new Date());
			} else {
				clazz.setIsGraduate(0);
			}
			clazz.setClassNameDynamic(classNameDynamic);
			saveClazzList.add(clazz);
		// 结束 ---------封装Clazz
		}
		//更新班级
		log.info("------- 更新 班级数据" + saveClazzList.size() + "个");
		if(CollectionUtils.isNotEmpty(saveClazzList)) {
			baseSyncSaveService.saveClassAndGrade(saveClazzList.toArray(new Clazz[0]),null);
		}
		
		
	}

	@Override
	public void saveGrade(String json) {
		// Do nothing because of X and Y.

	}

	@Override
	public void saveTeacher(String json) {
		JSONArray array = getData(json);
		if(array == null || array.size() == 0) {
			log.info("------------开始解析教师 数据，总数：0");
			return;
		}		
		log.info("------------开始解析教师数据，总数：" + array.size());
		//先解析出数据中的 教师id ,单位code 
		Set<String> schoolCodeList = new HashSet<>();
		Set<String> teacherIdList = new HashSet<>();
		Set<String> deptIdList = new HashSet<>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			String schoolCode = js.getString("schoolCode");
			String teacherId = js.getString("ID");
			String deptId = js.getString("deptId");
			
			deptIdList.add(deptId);
			schoolCodeList.add(schoolCode);
			teacherIdList.add(teacherId);
		}
		List<TeacherDuty> allTeacherDuty = SUtils.dt(
				teacherDutyRemoteService.findByTeacherIds(teacherIdList.toArray(new String[teacherIdList.size()])), 
				new TR<List<TeacherDuty>>() {});
        Map<String, List<TeacherDuty>> tidDutyMap = EntityUtils.getListMap(allTeacherDuty, TeacherDuty::getTeacherId, Function.identity());
		List<User> allUser = SUtils.dt(
				userRemoteService.findByOwnerIds(teacherIdList.toArray(new String[teacherIdList.size()])), 
				new TR<List<User>>() {});
		Map<String, User> allTeaIdUserMap = EntityUtils.getMap(allUser, User::getOwnerId);
		List<Dept> allDept = SUtils.dt(
				deptRemoteService.findListByIds(deptIdList.toArray(new String[deptIdList.size()])), 
				new TR<List<Dept>>() {});
        Map<String, Dept> deptMap = JledqSyncDataUtil.getMap(allDept, "id", StringUtils.EMPTY);
		List<School> allSchool = SUtils.dt(
				schoolRemoteService.findByCodeIn(schoolCodeList.toArray(new String[schoolCodeList.size()])), 
				new TR<List<School>>() {
		});
		Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getSchoolCode);
		Set<String> allUnitId = EntityUtils.getSet(allSchool, School::getId);
        List<Teacher> allTeacher = SUtils.dt(
        		teacherRemoteService.findListByIds(teacherIdList.toArray(new String[teacherIdList.size()])), 
        		new TR<List<Teacher>>() {});
		Map<String, Teacher> allTeacherMap = EntityUtils.getMap(allTeacher, Teacher::getId);
		Map<String,List<User>> unitIdSizeMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(allUnitId)) {
			List<User> allUnitUser = SUtils.dt(
					userRemoteService.findByUnitIds(allUnitId.toArray(new String[allUnitId.size()])), 
					new TR<List<User>>() {});
			unitIdSizeMap = EntityUtils.getListMap(allUnitUser, User::getUnitId, Function.identity());
		}
        List<Teacher> saveTeachers = new ArrayList<>();
        List<Dept> saveDepts = new ArrayList<>();
        List<User> saveUsers = new ArrayList<>();
        List<TeacherDuty> saveTeacherDutys = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
		// 开始---------转换字段
			JSONObject js = array.getJSONObject(i);
			String teacherId = js.getString("ID");
			//是中文跳过
			if (isContainChinese(teacherId)) {
				log.info("老师的"+teacherId +  "为中文");
				continue;
			}
			String username = js.getString("loginCode");
			username = validateName(username,WenXunConstant.WX_SUBSTRING_USER_NAEM_LENGTH);
			String teacherCode = js.getString("code");
			String teacherName = js.getString("name");
			String sexString = js.getString("sex");
			if(StringUtils.isBlank(sexString)) {
				sexString = "0";
			}
			int sex = Integer.parseInt(sexString);
			String phone = js.getString("mobile"); //加密
			if(StringUtils.isNotBlank(phone)) {
				phone = decryptData(phone);
			}
			String idCard = js.getString("idNumber"); //加密
			if(StringUtils.isNotBlank(idCard)) {
				idCard = decryptData(idCard);
			}
			String certificationCode = js.getString("techNumber"); //加密   教师资格证号
			if(StringUtils.isNotBlank(certificationCode)) {
				certificationCode = decryptData(certificationCode);
			}
			String nativePlace = js.getString("home"); //籍贯
			String nation = StringUtils.leftPad(js.getString("nation"), 2, "0"); //民族
			String email = js.getString("email");
			int isDeleted = Integer.parseInt(js.getString("isDeleted"));
			//密码：
			String password = js.getString(WenXunConstant.PASSWORD_PARAM_NAME);
			if(StringUtils.isNotBlank(password)){
				password = decryptData(password);
			}else{
				password = WenXunConstant.DEFAULT_PASS_WORD_VALUE;
			}
			//根据schoolcode 得到id
			String unitId;
			String schoolCode = js.getString("schoolCode");
			if(allSchoolMap.isEmpty() || allSchoolMap.get(schoolCode) == null) {
				log.error("老师的"+teacherId +  "的单位为空");
				continue;
			}else {
				unitId = allSchoolMap.get(schoolCode).getId();
			}
			//地区码
			String regionCode = StringUtils.rightPad(js.getString("place"), 6, "0");
			//部门 --没有就新增
			String deptName = js.getString("deptName");
			String schoolName = js.getString("schoolName");
			
			String deptId = js.getString("deptId");
			if(StringUtils.isNotBlank(deptId) && deptId.length() > 32 ){
				log.error("解析教师数据,部门id的值大于32位：-------------------"+deptId+"--------");
				deptId = deptId.length() > 32 ? StringUtils.substring(deptId, 0, 32) : StringUtils.rightPad(deptId, 32, "0"); 
			}
			JSONObject deptJs = new JSONObject();
			deptJs.put("unitId", unitId);
			deptJs.put("deptId", deptId);
			deptJs.put("deptName", js.getString("deptName"));
			deptJs.put("schoolName", js.getString("schoolName"));
			//多职位的 -- 保存进base_teacher_duty 
			String duties = js.getString("duties");
			//用户名 -- teacherid
		// 结束---------转换字段	
			
		// 开始 ---------封装Teacher	
			Teacher t;
			if(allTeacherMap.get(teacherId) != null) {
				t = allTeacherMap.get(teacherId);
			}else {
				t = new Teacher();
				t.setId(teacherId);
				t.setIncumbencySign("11");
				t.setUnitId(unitId);
				t.setWeaveUnitId(t.getUnitId());
				t.setInPreparation("1");  //是否在编  1 --是 0 --否
				t.setIncumbencySign("11"); //教师状态---在职
				t.setIsLeaveSchool("0");
				t.setEventSource(WenXunConstant.DEFAULT_EVENT_SOURCE_VALUE);
				//设置排序号
				Long displayOrder;
				if(RedisUtils.get("syncdata.wx.teacher.displayorder.max." + unitId) == null) {
					if(unitIdSizeMap == null || unitIdSizeMap.isEmpty() || unitIdSizeMap.get(unitId) == null) {
						displayOrder = (long)1;
					}else {
						displayOrder = (long) unitIdSizeMap.get(unitId).size();
					}
				}
				displayOrder = RedisUtils
						.incrby("syncdata.wx.teacher.displayorder.max." + unitId, 1);
				t.setDisplayOrder(displayOrder.intValue());
				t.setCreationTime(new Date());
				t.setIdentityType("1"); //身份证类型为 -- 居民身份证
			}
			if(StringUtils.isBlank(teacherCode)) {
				Long max = RedisUtils
						.incrby("syncdata.wx.teacher.code.max." + unitId, 1);
				teacherCode = StringUtils.leftPad("" + max, 6, "0");
			}
			t.setTeacherCode(teacherCode);
			t.setTeacherName(teacherName);
			t.setSex(sex);
			t.setLinkPhone(phone);
			t.setMobilePhone(phone);
			t.setIdentityCard(idCard);
			t.setCertificationCode(certificationCode);
			t.setNativePlace(nativePlace);
			t.setNation(nation);
			t.setEmail(email);
			t.setDeptId(deptId);
			t.setModifyTime(new Date());
			t.setIsDeleted(isDeleted);
//			if(StringUtils.isNotBlank(js.getString("ZYJSZWM"))) {
//				t.setDuty(js.getString("ZYJSZWM"));
//			}
			t.setRegionCode(regionCode);
			saveTeachers.add(t);
		// 结束 ---------封装Teacher
		
		// 开始 ---------封装user
			User ur;
			if(allTeaIdUserMap.get(teacherId) != null) {
				ur = allTeaIdUserMap.get(teacherId);
			}else {
				ur = new User();
				ur.setId(UuidUtils.generateUuid());
				ur.setOwnerType(User.OWNER_TYPE_TEACHER);
				ur.setEventSource(WenXunConstant.DEFAULT_EVENT_SOURCE_VALUE);
				ur.setUserRole(2);
				ur.setCreationTime(new Date());
				ur.setEnrollYear(0000);
				ur.setUserType(User.USER_TYPE_COMMON_USER);
				ur.setUserState(User.USER_MARK_NORMAL); // 0-未审核 1-正常 2-锁定 3-离职锁定
				ur.setIconIndex(0);
				ur.setAccountId(UuidUtils.generateUuid());
				ur.setRegionCode(t.getRegionCode());
				ur.setOwnerId(t.getId());
				//设置排序号，以前的数据不处理，新增的排序
				ur.setDisplayOrder(t.getDisplayOrder());
			}
			//关键字段
			ur.setUnitId(unitId);
			ur.setRegionCode(regionCode);
			ur.setIsDeleted(isDeleted);
			ur.setUsername(username);
			ur.setDeptId(deptId);
			//补充信息字段
			ur.setMobilePhone(phone);
			ur.setSex(sex);
			ur.setRealName(teacherName);
			ur.setModifyTime(new Date());
			ur.setPassword(new PWD(password).encode());
			saveUsers.add(ur);
		// 结束 ---------封装user
			
		// 开始 ---------封装dept
			Dept d;
			if(deptMap == null || deptMap.isEmpty() || deptMap.get(deptId) == null) {
				d = new Dept();
				d.setId(deptId);
				d.setDeptType(1);
				d.setCreationTime(new Date());
				d.setEventSource(0);
				d.setInstituteId(Constant.GUID_ZERO);
				d.setIsDeleted(WenXunConstant.DEFAULT_IS_DELETED_VALUE);
//				d.setDeptCode("000001");
				Long max = RedisUtils
						.incrby("syncdata.scwx.all.dept.code.max", 1);
				String deptCode = max+ "";
				d.setDeptCode(StringUtils.leftPad(deptCode, 6, "0"));
				
				d.setParentId(StringUtils.isBlank(js.getString("PID"))?Constant.GUID_ZERO:js.getString("PID").replaceAll("-", ""));
			}else {
				d = deptMap.get(deptId);
			}
			//关键字段
			d.setUnitId(unitId);
			deptName = "(" + schoolName + ")" + deptName;  //部门名字 = 学校名字 + 部门名称
			d.setDeptName(deptName);
			//补充信息字段
			d.setModifyTime(new Date());
			saveDepts.add(d);
		// 结束 ---------封装dept
			
		// 开始 ---------封装base_teacher_duty
	        List<TeacherDuty> oldTeacherDuty = new ArrayList<>();
	        List<TeacherDuty> saveOldTeacherDuty = new ArrayList<>();
	        
	        JSONArray dutyArray = Json.parseArray(duties);
	        if(tidDutyMap == null || tidDutyMap.isEmpty() || tidDutyMap.get(teacherId) == null) {
				for (int j= 0; j < dutyArray.size(); j++) {
					JSONObject duty = dutyArray.getJSONObject(j);
					addNewTeacherDuty(saveTeacherDutys, teacherId, duty);
				}
			}else {
				//得到之前的职务
				List<TeacherDuty> haveTD = tidDutyMap.get(teacherId);
				oldTeacherDuty.addAll(haveTD);
				Map<String, TeacherDuty> codeMap = EntityUtils.getMap(haveTD, TeacherDuty::getDutyCode);
				//新增的职务 -- 添加 ； 撤离的职务 -- 删除
				List<TeacherDuty> saveTD = new ArrayList<>(); 
				for (int j= 0; j < dutyArray.size(); j++) {
					JSONObject duty = dutyArray.getJSONObject(j);
					String code = duty.getString("id");
					if(codeMap.get(code) == null) {
						addNewTeacherDuty(saveTeacherDutys, teacherId, duty);
					}else {
						TeacherDuty teacherDuty = codeMap.get(code);
						teacherDuty.setIsDeleted(0);
						saveTeacherDutys.add(teacherDuty);
					}
				}
				saveOldTeacherDuty.addAll(saveTD);
				
				// 撤离的职务 -- 删除
				List<TeacherDuty> deleteTeacherDuty = ListUtils.subtract(oldTeacherDuty, saveOldTeacherDuty);
				if(CollectionUtils.isNotEmpty(deleteTeacherDuty)) {
					for (TeacherDuty teacherDuty : deleteTeacherDuty) {
						teacherDuty.setIsDeleted(1);
					}
					saveTeacherDutys.addAll(deleteTeacherDuty);
				}
			}
		// 结束 ---------封装base_teacher_duty
		}
		//更新教师信息
		if(CollectionUtils.isNotEmpty(saveTeachers)) {
			log.info("同步教师的总数---" + saveTeachers.size());
			baseSyncSaveService.saveTeacher(saveTeachers.toArray(new Teacher[0]));
		}
		//更新用户信息
		if(CollectionUtils.isNotEmpty(saveUsers)) {
			log.info("同步教师用户的总数---" + saveUsers.size());
			baseSyncSaveService.saveUser(saveUsers.toArray(new User[0]));
		}
		//更新教师部门
		if(CollectionUtils.isNotEmpty(saveDepts)) {
			log.info("同步教师部门的总数---" + saveDepts.size());
			baseSyncSaveService.saveDept(saveDepts.toArray(new Dept[0]));
		}
		//更新教师职务
		if(CollectionUtils.isNotEmpty(saveTeacherDutys)) {
			log.info("同步教师职务的总数---" + saveTeacherDutys.size());
			baseSyncSaveService.saveTeacherDuty(saveTeacherDutys.toArray(new TeacherDuty[0]));
		}
	}



	@Override
	public void saveStudent(String json) {
		JSONArray array = getData(json);
		if(array == null || array.size() == 0) {
			log.info("------------开始解析学生数据，总数：0");
			return;
		}
		log.info("------------开始解析学生数据，总数：" + array.size());
		//先解析出数据中的 学生id ,单位code ,班级classid
		Set<String> stuIdList = new HashSet<>();
		Set<String> schoolCodeList = new HashSet<>();
		Set<String> clazzIdList = new HashSet<>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			String stuId = js.getString("ID");
			String schoolCode = js.getString("schoolCode");
			String clazzId = js.getString("classId");
			
			stuIdList.add(stuId);
			schoolCodeList.add(schoolCode);
			clazzIdList.add(clazzId);
		}
		List<Student> allStudent = SUtils.dt(
				studentRemoteService.findListByIds(stuIdList.toArray(new String[stuIdList.size()])),
				new TR<List<Student>>() {});
		Map<String, Student> allStudentMap = EntityUtils.getMap(allStudent, Student::getId);
		List<Clazz> allClazz = SUtils.dt(
				classRemoteService.findListByIds(clazzIdList.toArray(new String[clazzIdList.size()])),
				new TR<List<Clazz>>() {});
		Map<String, Clazz> allClazzMap = EntityUtils.getMap(allClazz, Clazz::getId);
		List<School> allSchool = SUtils.dt(
				schoolRemoteService.findByCodeIn(schoolCodeList.toArray(new String[schoolCodeList.size()])), 
				new TR<List<School>>() {
		});
		Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getSchoolCode);
		Set<String> allUnitId = EntityUtils.getSet(allSchool, School::getId);
		List<User> allUser = SUtils.dt(
				userRemoteService.findByOwnerIds(stuIdList.toArray(new String[stuIdList.size()])), 
				new TR<List<User>>() {});
		Map<String, User> allStuUserMap = EntityUtils.getMap(allUser, User::getOwnerId);
		Map<String,List<User>> unitIdSizeMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(allUnitId)) {
			List<User> allUnitUser = SUtils.dt(
					userRemoteService.findByUnitIds(allUnitId.toArray(new String[allUnitId.size()])), 
					new TR<List<User>>() {});
			unitIdSizeMap = EntityUtils.getListMap(allUnitUser, User::getUnitId, Function.identity());
		}
		List<Student> saveStudents = new ArrayList<>();
		List<User> saveUsers = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
	   // 开始---------转换字段
			JSONObject js = array.getJSONObject(i);
			String studentId = js.getString("ID");
			String username = js.getString("loginCode");
			username = validateName(username,WenXunConstant.WX_SUBSTRING_USER_NAEM_LENGTH);
			String unitiveCode = js.getString("stuNumber");  //学籍号
			String studentCode = js.getString("stuId");  //学号
			String studentName = js.getString("name"); 
			String sexString = js.getString("sex");
			if(StringUtils.isBlank(sexString)) {
				sexString = "0";
			}
			int sex = Integer.parseInt(sexString);
			String idCard = js.getString("idNumber");
			if(StringUtils.isNotBlank(idCard)) {
				idCard = decryptData(idCard);
			}
			String phone = js.getString("mobile");
			if(StringUtils.isNotBlank(phone)) {
				phone = decryptData(phone);
			}
			String nativePlace = js.getString("home"); //籍贯
			String nation = StringUtils.leftPad(js.getString("nation"), 2, "0"); //民族
			String email = js.getString("email");
			String birthday = js.getString("birthDay");
			Date userBirThday = null;
			try {
				if(StringUtils.isNotBlank(birthday)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					userBirThday = sdf.parse(birthday);
				}
			} catch (ParseException e) {
				userBirThday = null;
			}
			//根据schoolcode 得到id
			String unitId;
			String schoolCode = js.getString("schoolCode");
			if(allSchoolMap.isEmpty() || allSchoolMap.get(schoolCode) == null) {
				log.error("学生的"+studentId +  "的单位为空");
				continue;
			}else {
				unitId = allSchoolMap.get(schoolCode).getId();
			}
			String clazzId = js.getString("classId");
			Clazz clazz;
			if(allClazzMap == null || allClazzMap.isEmpty() || allClazzMap.get(clazzId) == null) {
				log.error("学生的"+studentId +  "的班级为空");
				continue;
			}else {
				clazz = allClazzMap.get(clazzId);
			}
			int isDeleted = Integer.parseInt(js.getString("isDeleted"));
			
			//密码：
			String password = js.getString(WenXunConstant.PASSWORD_PARAM_NAME);
			if(StringUtils.isNotBlank(password)){
				password = decryptData(password);
			}else{
				password = WenXunConstant.DEFAULT_PASS_WORD_VALUE;
			}
	 // 结束---------转换字段
			
	 // 开始 ---------封装student
			Student student;
			if(allStudentMap.get(studentId) != null) {
				student = allStudentMap.get(studentId);
			}else {
				student = new Student();
				student.setId(studentId);
				student.setNowState("40"); //40--为登记状态
				student.setIsLeaveSchool(0);
				student.setEventSource(0);
				student.setEnrollYear(clazz != null ? clazz.getAcadyear() : "");
				student.setCreationTime(new Date());
				student.setIdentitycardType("1"); //身份证类型为 -- 居民身份证
			}
			//关键字段
			student.setSchoolId(unitId);
			student.setStudentName(studentName);
			student.setSex(sex);
			student.setClassId(clazzId);
			student.setIsDeleted(isDeleted);
			//补充信息字段
			student.setNation(nation);
			student.setStudentCode(studentCode);
			student.setUnitiveCode(unitiveCode);
			student.setIdentityCard(idCard);
			student.setModifyTime(new Date());
			student.setMobilePhone(phone);
			student.setNativePlace(nativePlace);
			student.setEmail(email);
			student.setBirthday(userBirThday);
			
			saveStudents.add(student);
	  // 结束 ---------封装student
		
	  // 开始 ---------封装user
			if(StringUtils.isNotBlank(username)) {
				User ur;
				if(allStuUserMap.get(studentId) != null) {
					ur = allStuUserMap.get(studentId);
					if(!ur.getUsername().equals(username)) {
						log.error("学生的"+studentId +  "的username 不一致，不能保存进去");
						continue;
					}
					//用户删除，状态改为 2-锁定
					if(isDeleted == 1) {
						ur.setUserState(User.USER_MARK_LOCK);
					}
				}else {
					ur = new User();
					ur.setId(UuidUtils.generateUuid());
					ur.setOwnerId(student.getId());
					ur.setUsername(username);
					ur.setOwnerType(User.OWNER_TYPE_STUDENT);
					ur.setEventSource(WenXunConstant.DEFAULT_EVENT_SOURCE_VALUE);
					ur.setEnrollYear(0000);
					ur.setUserType(User.USER_TYPE_COMMON_USER);
					ur.setCreationTime(new Date());
					
					ur.setUserRole(2);
					ur.setAccountId(student.getId());
					ur.setUserState(User.USER_MARK_NORMAL); // 0-未审核 1-正常 2-锁定 3-离职锁定
					ur.setIconIndex(0);
					ur.setDeptId("");
					//进行排序
					Long displayOrder;
					if(RedisUtils.get("syncdata.wx.student.displayorder.max." + unitId) == null) {
						if(unitIdSizeMap == null || unitIdSizeMap.isEmpty() || unitIdSizeMap.get(unitId) == null) {
							displayOrder = (long)1;
						}else {
							displayOrder = (long) unitIdSizeMap.get(unitId).size();
						}
					}
					displayOrder = RedisUtils
							.incrby("syncdata.wx.student.displayorder.max." + unitId, 1);
					ur.setDisplayOrder(displayOrder.intValue());
				}
				//关键字段
				ur.setUnitId(unitId);
				String regionCode = allSchoolMap.get(schoolCode).getRegionCode();
				ur.setRegionCode(regionCode);
				ur.setIsDeleted(isDeleted);
				//补充信息字段
				ur.setMobilePhone(phone);
				ur.setSex(sex);
				ur.setRealName(studentName);
				ur.setModifyTime(new Date());
				ur.setPassword(new PWD(password).encode());
				saveUsers.add(ur);
			}
	  // 结束 ---------封装user	
		}
		
		//保存学生数据 和用户信息
		log.info("------------保存学生数据，总数："+saveStudents.size());
		if(CollectionUtils.isNotEmpty(saveStudents)) {
			baseSyncSaveService.saveStudent(saveStudents.toArray(new Student[0]));
		}
		log.info("------------保存学生用户数据，总数："+saveUsers.size());
		if(CollectionUtils.isNotEmpty(saveUsers)) {
			baseSyncSaveService.saveUser(saveUsers.toArray(new User[0]));
		}
	}
	

	@Override
	public void saveSubSchool(String json) {
		// Do nothing because of X and Y.

	}

	@Override
	public void saveDept(String json) {
		// Do nothing because of X and Y.

	}

	@Override
	public void saveData() {
		// Do nothing because of X and Y.

	}

	@Override
	public void saveFamily(String json) {
		JSONArray array = getData(json);
		if(array == null || array.size() == 0) {
			log.info("------------开始解析家长数据，总数：0");
			return;
		}
		log.info("------------开始解析家长数据，总数：" + array.size());
		//先解析出数据中的 家长的手机号,学生的id
		Set<String> famIdList = new HashSet<>();
		Set<String> schoolCodeList = new HashSet<>();
		Set<String> nameList = new HashSet<>();
		Set<String> phoneList = new HashSet<>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			String famId = js.getString("ID");
			String students = js.getString("students");
			JSONArray studentList = Json.parseArray(students);
			for (int j = 0; j < studentList.size(); j++) {
				JSONObject stu = studentList.getJSONObject(j);
				String schoolCode = stu.getString("schoolCode");
				schoolCodeList.add(schoolCode);
			}
			nameList.add(js.getString("name"));
			String phone = js.getString("mobile");
			if(StringUtils.isNotBlank(phone)) {
				phone = decryptData(phone);
			}
			phoneList.add(phone);
			famIdList.add(famId);
		}
		List<School> allSchool = SUtils.dt(
				schoolRemoteService.findByCodeIn(schoolCodeList.toArray(new String[schoolCodeList.size()])), 
				new TR<List<School>>() {
		});
		Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getSchoolCode);
		List<Family> allFamily = SUtils.dt(
				familyRemoteService.findByRealNameInAndMobilePhoneIn(
						nameList.toArray(new String[nameList.size()]),
						phoneList.toArray(new String[phoneList.size()])), 
				new TR<List<Family>>() {});
		Map<String, Family> familyMap = new HashMap<>();
		for (Family family : allFamily) {
			String key = family.getStudentId() + family.getMobilePhone() + family.getRealName();
			familyMap.put(key, family);
		}
		List<Family> saveFamilys = new ArrayList<>();
		//关系转化Map<String，String>
		Map<String, String> conversionMap = new HashMap<>();
		conversionMap.put("101", "51"); // 父亲
		conversionMap.put("102", "52"); // 母亲
		conversionMap.put("103", "80"); // 子女 （我们没有转为其他）
		conversionMap.put("104", "71"); // 哥哥
		conversionMap.put("105", "73"); // 弟弟
		conversionMap.put("106", "75"); // 姐姐
		conversionMap.put("107", "77"); // 妹妹
		conversionMap.put("108", "80"); // 监护人（我们没有转为其他） --把is_guardian 设为 1
		for (int i = 0; i < array.size(); i++) {
			// 开始---------转换字段
			JSONObject js = array.getJSONObject(i);
			String familyId = js.getString("ID");
			String username = js.getString("loginCode");
			username = validateName(username,WenXunConstant.WX_SUBSTRING_USER_NAEM_LENGTH);
			String realName = js.getString("name");
			//对姓名进行截取 超过30的截取
//			realName = validateName(realName,WenXunConstant.WX_SUBSTRING_REAL_NAEM_LENGTH);
			String phone = js.getString("mobile");  //加密
			if(StringUtils.isNotBlank(phone)) {
				phone = decryptData(phone);
			}
			int isDeleted = Integer.parseInt(js.getString("isDeleted"));
			String students = js.getString("students");
			
			JSONArray studentList = Json.parseArray(students);
			// 结束---------转换字段
			for (int j = 0; j < studentList.size(); j++) {
			 // 开始 ---------封装family
				JSONObject stu = studentList.getJSONObject(j);
				String studentId = stu.getString("studentId");
				String relationId = stu.getString("relationId");  
				Integer isGuardian = 0;
				if(relationId.equals("108")) {
					isGuardian = 1;
				}
				relationId = conversionMap.get(relationId);
				String schoolCode = stu.getString("schoolCode");
				String unitId;
				if(allSchoolMap.isEmpty() || allSchoolMap.get(schoolCode) == null) {
					log.error("学生的"+studentId +  "的单位为空");
					continue;
				}else {
					unitId = allSchoolMap.get(schoolCode).getId();
				}
				String key = studentId + phone + realName;
				Family family;
				if(familyMap.get(key) != null) {
					family = familyMap.get(key);
				}else {
					family = new Family();
					family.setId(UuidUtils.generateUuid());
					family.setStudentId(studentId);
					family.setCreationTime(new Date());
					family.setEventSource(WenXunConstant.DEFAULT_EVENT_SOURCE_VALUE);
				}
				//关键字段
				family.setSchoolId(unitId);
				family.setRelation(relationId);
				family.setRealName(realName);
				family.setMobilePhone(phone);
				family.setIsDeleted(isDeleted);
				//补充信息字段
				family.setModifyTime(new Date());
				family.setIsGuardian(isGuardian);
				
				saveFamilys.add(family);
			 // 结束 ---------封装family
			// 开始 ---------封装user 
		    // 结束 ---------封装user	
			}
		}
		//保存家长数据 
		if(CollectionUtils.isNotEmpty(saveFamilys)) {
			baseSyncSaveService.saveFamily(saveFamilys.toArray(new Family[0]));
		}
	}

	@Override
	public void saveCourse(String json) {
		 // Do nothing because of X and Y.
	}

	@Override
	public void saveClassTeach(String json) {
		// Do nothing because of X and Y.
	}

	/**
	 * 设置学校的管理员
	 */
	@Override
	public void saveAdmin(String json) {
		JSONArray array = getData(json);
		if(array == null || array.size() == 0) {
			log.info("------------开始同步管理员数据，总数：" + 0);
			return;
		}
		log.info("------------开始同步管理员数据，总数：" + array.size());
		//先解析出数据中的schoolcode,username
		Set<String> schoolCodeList = new HashSet<>();
		Set<String> userNameList = new HashSet<>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			String schoolCode = js.getString("schoolCode");
			schoolCodeList.add(schoolCode);
			String userName = js.getString("loginCode");
			userName = validateName(userName,WenXunConstant.WX_SUBSTRING_USER_NAEM_LENGTH);
			userNameList.add(userName);
		}
		List<School> allSchool = SUtils.dt(
				schoolRemoteService.findByCodeIn(schoolCodeList.toArray(new String[schoolCodeList.size()])), 
				new TR<List<School>>() {
		});
		Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getSchoolCode);
		Set<String> idList = EntityUtils.getSet(allSchool, School::getId);
		List<Unit> allUnit =  SUtils.dt(
				unitRemoteService.findListByIds(idList.toArray(new String[idList.size()])), 
				new TR<List<Unit>>() {
		});
		Map<String, Unit> allUnitMap = EntityUtils.getMap(allUnit, Unit::getId);
		List<User> allAdminUser = SUtils.dt(
				userRemoteService.findAdmins(idList.toArray(new String[idList.size()])), 
				new TR<List<User>>() {});
		Map<String,User> adminMap = EntityUtils.getMap(allAdminUser, User::getUnitId);
		List<User> allUser = SUtils.dt(
				userRemoteService.findByUsernames(userNameList.toArray(new String[userNameList.size()])), 
				new TR<List<User>>() {});
		Map<String, User> allStuUserMap = EntityUtils.getMap(allUser, User::getUsername);
		Set<String> teacherIdList = EntityUtils.getSet(allUser, User::getOwnerId);
		List<Teacher> allTeacher = SUtils.dt(
        		teacherRemoteService.findListByIds(teacherIdList.toArray(new String[teacherIdList.size()])), 
        		new TR<List<Teacher>>() {});
		Map<String, Teacher> allTeacherMap = EntityUtils.getMap(allTeacher, Teacher::getId);
		List<Teacher> allUnitTeacher = SUtils.dt(
        		teacherRemoteService.findByUnitIdIn(idList.toArray(new String[idList.size()])), 
        		new TR<List<Teacher>>() {});
		Map<String,List<Teacher>> unitIdMap = EntityUtils.getListMap(allUnitTeacher, Teacher::getUnitId, Function.identity());
		List<Teacher> saveTeachers = new ArrayList<>();
		List<User> saveUsers = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			// 开始---------转换字段
			JSONObject js = array.getJSONObject(i);
			String schoolCode = js.getString("schoolCode");
			String username = js.getString("loginCode");
			username = validateName(username,WenXunConstant.WX_SUBSTRING_USER_NAEM_LENGTH);
			String name = js.getString("name");
			String unitId;
			if(allSchoolMap.isEmpty() || allSchoolMap.get(schoolCode) == null) {
				log.error("学校的schoolCode为："+schoolCode +  "----的单位不存在");
				continue;
			}else {
				unitId = allSchoolMap.get(schoolCode).getId();
			}
			String id = js.getString("ID");
			String isDeleted = js.getString("isDeleted");
			String schoolName = js.getString("schoolName");
			//密码：
			String pwd = js.getString(WenXunConstant.PASSWORD_PARAM_NAME);
			if(StringUtils.isNotBlank(pwd)){
				pwd = decryptData(pwd);
			}else{
				pwd = WenXunConstant.DEFAULT_PASS_WORD_VALUE;
			}
			
			// 结束---------转换字段
			// 开始 ---------封装Teacher
			if(StringUtils.isNotBlank(username)) {
				Teacher t;
				if(allStuUserMap.get(username) != null) {
					t = allTeacherMap.get(allStuUserMap.get(username).getOwnerId());
				}else {
					t = new Teacher();
					t.setId(id);
					t.setIncumbencySign("11");
					t.setUnitId(unitId);
					t.setWeaveUnitId(t.getUnitId());
					t.setInPreparation("1");  //是否在编  1 --是 0 --否
					t.setIncumbencySign("11"); //教师状态---在职
					t.setIsLeaveSchool("0");
					t.setEventSource(WenXunConstant.DEFAULT_EVENT_SOURCE_VALUE);
					//设置排序号
					if(unitIdMap == null || unitIdMap.isEmpty() || unitIdMap.get(unitId) == null) {
						t.setDisplayOrder(1);
					}else {
						int disPlayOrder = unitIdMap.get(unitId).size();
						t.setDisplayOrder(disPlayOrder+1);
					}
					t.setCreationTime(new Date());
					t.setIdentityType("1"); //身份证类型为 -- 居民身份证
				}
				Long max = RedisUtils
						.incrby("syncdata.wx.teacher.code.max." + unitId, 1);
				String teacherCode = StringUtils.leftPad("" + max, 6, "0");
				t.setTeacherCode(teacherCode);
				t.setTeacherName("("+schoolName+")"+name);
				t.setSex(1);
				t.setLinkPhone("");
				t.setMobilePhone("");
				t.setIdentityCard("");
				t.setCertificationCode("");
				t.setNativePlace("");
				t.setNation("");
				t.setEmail("");
				t.setDeptId("");
				t.setModifyTime(new Date());
				t.setIsDeleted(StringUtils.isBlank(isDeleted)?WenXunConstant.DEFAULT_IS_DELETED_VALUE : Integer.valueOf(isDeleted));
				t.setRegionCode(allUnitMap.get(unitId).getRegionCode());
				saveTeachers.add(t);
			// 结束 ---------封装Teacher
			
			// 开始---------封装user
				User ur;
				if(allStuUserMap.get(username) != null) {
					ur = allStuUserMap.get(username);
					if(!ur.getUnitId().equals(unitId)) {
						log.error("该管理员没有在当前的学校下面，请核实数据 ------");
						continue;
					}
					if(ur.getIsDeleted() == 1) {
						log.error("该管理员已经删除，请核实数据 ------");
						continue;
					}
					//先判断本单位下是否已经有管理员
					if(adminMap.get(unitId) != null) {
						log.error("该单位下已经有管理员，请核实数据 ------");
						continue;
					}
				}else {
					ur = new User();
					ur.setId(UuidUtils.generateUuid());
					ur.setOwnerType(User.OWNER_TYPE_TEACHER);
					//密码：
//					String pwd = WenXunConstant.DEFAULT_PASS_WORD_VALUE;
					ur.setPassword(new PWD(pwd).encode());
					ur.setEventSource(WenXunConstant.DEFAULT_EVENT_SOURCE_VALUE);
					ur.setUserRole(2);
					ur.setCreationTime(new Date());
					ur.setEnrollYear(0000);
					ur.setUserState(User.USER_MARK_NORMAL); // 0-未审核 1-正常 2-锁定 3-离职锁定
					ur.setIconIndex(0);
					ur.setAccountId(t.getId());
					ur.setRegionCode(t.getRegionCode());
					ur.setOwnerId(t.getId());
					//设置排序号，以前的数据不处理，新增的排序
					ur.setDisplayOrder(t.getDisplayOrder());
					//关键字段
					ur.setUnitId(t.getUnitId());
					ur.setIsDeleted(t.getIsDeleted());
					ur.setUsername(username);
					ur.setDeptId(t.getDeptId());
					//补充信息字段
					ur.setMobilePhone(t.getMobilePhone());
					ur.setSex(t.getSex());
					ur.setRealName(t.getTeacherName());
					ur.setModifyTime(new Date());
					
					ur.setUserType(User.USER_TYPE_UNIT_ADMIN);
				}
				saveUsers.add(ur);
			// 结束---------封装user
		   }
		}
		//更新教师信息
		if(CollectionUtils.isNotEmpty(saveTeachers)) {
			log.info("同步教师的总数---" + saveTeachers.size());
			baseSyncSaveService.saveTeacher(saveTeachers.toArray(new Teacher[0]));
		}
		//保存用户信息
		if(CollectionUtils.isNotEmpty(saveUsers)) {
			baseSyncSaveService.saveUser(saveUsers.toArray(new User[0]));
		}
		
	}
	
	@Override
	public void saveTeachPlace(String json,String param) {
		//解析参数
		JSONObject jsonObject = JSONObject.parseObject(param);
		String schoolCode = jsonObject.getString("schoolCode");
		String teachAreaId = jsonObject.getString("teachAreaId");
		String buildingId = jsonObject.getString("buildingId");
		School school = SUtils.dc(schoolRemoteService.findByCode(schoolCode), School.class);
		String unitId = school.getId();
		
		JSONArray array = getData(json);
		if(array == null || array.size() == 0) {
			log.info("------------开始同步教学场地数据，总数：" + 0);
			return;
		}
		log.info("------------开始同步教学场地数据，总数：" + array.size());
		//先解析出数据中的teachPlaceList
		Set<String> teachPlaceList = new HashSet<>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			String teachPlaceId = js.getString("classRoomId");
			teachPlaceList.add(teachPlaceId);
		}
		List<TeachPlace> allTeachPlace = SUtils.dt(
				teachPlaceRemoteService.findListByIds(teachPlaceList.toArray(new String[teachPlaceList.size()])), 
				new TR<List<TeachPlace>>() {
		});
		Map<String, TeachPlace> allTeachPlaceMap = EntityUtils.getMap(allTeachPlace, TeachPlace::getId);
		List<TeachPlace> teachPlaces = SUtils.dt(
				teachPlaceRemoteService.findTeachPlaceListByType(unitId,null), 
				new TR<List<TeachPlace>>() {
		});
		
		List<TeachPlace> saveTeachPlaces = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			// 开始---------转换字段
			JSONObject js = array.getJSONObject(i);
			String teachPlaceId = js.getString("classRoomId");
			String placeName = js.getString("classRoomName");
			String isNormal = js.getString("status");  
		// 开始 ---------封装TeachPlace 
			TeachPlace teachPlace;
			if(allTeachPlaceMap.get(teachPlaceId) != null) {
				teachPlace = allTeachPlaceMap.get(teachPlaceId);
			}else {
				teachPlace = new TeachPlace();
				teachPlace.setId(teachPlaceId);
				teachPlace.setCreationTime(new Date());
				teachPlace.setPlaceType("4");// 默认是教室
				teachPlace.setPlaceCode(teachPlaces.size()+i+1+"");
			}
			//关键字段
			teachPlace.setUnitId(unitId);
			teachPlace.setDeleted("1".equals(isNormal) ? false : true );
			teachPlace.setTeachAreaId(teachAreaId);
			teachPlace.setPlaceName(placeName);
			//补充信息字段
			teachPlace.setModifyTime(new Date());
			teachPlace.setTeachBuildingId(buildingId);
	    // 结束 ---------封装TeachPlace 	
			saveTeachPlaces.add(teachPlace);
		}
		//更新教学场地信息
		if(CollectionUtils.isNotEmpty(saveTeachPlaces)) {
			log.info("同步教学场地的总数---" + saveTeachPlaces.size());
			baseSyncSaveService.saveTeachPlace(saveTeachPlaces.toArray(new TeachPlace[0]));
		}
	}

	//stusys_semester
	@Override
	public void saveSchoolSemester(String json,String schoolCode) {
		School school = SUtils.dc(schoolRemoteService.findByCode(schoolCode), School.class);
		if(school == null) {
			log.info("------------同步学校--学年学期中的学校不存在，schoolCode：" + schoolCode);
			return;
		}
		String unitId = school.getId();
		
		JSONArray array = getData(json);
		if(array == null || array.size() == 0) {
			log.info("------------开始同步学校--学年学期数据，总数：" + 0);
			return;
		}
		log.info("------------开始同步学校--学年学期数据，总数：" + array.size());
		List<SchoolSemester> allSemester = SUtils.dt(semesterRemoteService.findAll(), new TR<List<SchoolSemester>>() {});
		Map<String, SchoolSemester> acadSemeMap = new HashMap<>();
		for (SchoolSemester schoolSemester : allSemester) {
		  String key = schoolSemester.getAcadyear()+"-"+String.valueOf(schoolSemester.getSemester());
	      acadSemeMap.put(key, schoolSemester);
		}
		
		List<SchoolSemester> saveSchoolSemesters = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			// 开始---------转换字段
			JSONObject js = array.getJSONObject(i);
			String startDate = js.getString("startDate");
			String endDate = js.getString("endDate");
			String schoolYear = js.getString("schoolYear");
			
			String semesters = js.getString("semesters");
			JSONArray semesterList = Json.parseArray(semesters);
			// 结束---------转换字段
			for (int j = 0; j < semesterList.size(); j++) {
			 // 开始 ---------封装TeachBuilding
				JSONObject build = semesterList.getJSONObject(j);
				String sName = build.getString("name");
				String sStartDate = build.getString("startDate");  
				String sEndDate = build.getString("endDate");  
				
				String key = js.getString("XNMC")+"-"+(js.getString("XQMC").equals("上")?1:2);
				
				SchoolSemester schoolSemester;
				if(acadSemeMap.get(key) != null) {
					schoolSemester = acadSemeMap.get(key);
				}else {
					schoolSemester = new SchoolSemester();
					schoolSemester.setId(UuidUtils.generateUuid());
//					se.setCreationTime(new Date());
//					se.setModifyTime(new Date());
				}
				//关键字段
//				teachBuilding.setUnitId(unitId);
//				teachBuilding.setBuildingName(buildName);
//				teachBuilding.setTeachAreaId(teachAreaId);
//				teachBuilding.setIsDeleted("0".equals(isNormal)? 1 : 0);
//				
//				saveTeachBuildings.add(teachBuilding);
			 // 结束 ---------封装TeachBuilding
			}
		}
	}

	@Override
	public void saveTeachArea(String data,String schoolCode) {
		School school = SUtils.dc(schoolRemoteService.findByCode(schoolCode), School.class);
		if(school == null) {
			log.info("------------同步校区数据中的学校不存在，schoolCode：" + schoolCode);
			return;
		}
		String unitId = school.getId();
		
		JSONArray array = getData(data);
		if(array == null || array.size() == 0) {
			log.info("------------开始同步校区数据，总数：" + 0);
			return;
		}
		log.info("------------开始同步校区数据，总数：" + array.size());
		//先解析出数据中的teachAreaList,teachBuildList
		Set<String> teachAreaList = new HashSet<>();
		Set<String> teachBuildList = new HashSet<>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			String teachAreaId = js.getString("districtId");
			teachAreaList.add(teachAreaId);
			String teachBuilds = js.getString("bulidings");
			JSONArray buildList = Json.parseArray(teachBuilds);
			for (int j = 0; j < buildList.size(); j++) {
				JSONObject build = buildList.getJSONObject(j);
				String buildId = build.getString("id");
				teachBuildList.add(buildId);
			}
		}
		List<TeachArea> allTeachArea = SUtils.dt(
				teachAreaRemoteService.findListByIds(teachAreaList.toArray(new String[teachAreaList.size()])), 
				new TR<List<TeachArea>>() {
		});
		Map<String, TeachArea> allTeachAreaMap = EntityUtils.getMap(allTeachArea, TeachArea::getId);
		List<TeachBuilding> allTeachBuilding = SUtils.dt(
				teachBuildingRemoteService.findListByIds(teachBuildList.toArray(new String[teachBuildList.size()])), 
				new TR<List<TeachBuilding>>() {
		});
		Map<String, TeachBuilding> allTeachBuildingMap = EntityUtils.getMap(allTeachBuilding, TeachBuilding::getId);
		List<TeachArea> saveTeachAreas = new ArrayList<>();
		List<TeachBuilding> saveTeachBuildings = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			// 开始---------转换字段
			JSONObject js = array.getJSONObject(i);
			String teachAreaId = js.getString("districtId");
			String areaName = js.getString("districtName");
			
			String teachBuilds = js.getString("bulidings");
			JSONArray buildList = Json.parseArray(teachBuilds);
			// 结束---------转换字段
			for (int j = 0; j < buildList.size(); j++) {
			 // 开始 ---------封装TeachBuilding
				JSONObject build = buildList.getJSONObject(j);
				String buildId = build.getString("id");
				String buildName  = build.getString("name");  
				String isNormal = build.getString("status");  
				
				TeachBuilding teachBuilding;
				if(allTeachBuildingMap.get(buildId) != null) {
					teachBuilding = allTeachBuildingMap.get(buildId);
				}else {
					teachBuilding = new TeachBuilding();
					teachBuilding.setId(buildId);
				}
				//关键字段
				teachBuilding.setUnitId(unitId);
				teachBuilding.setBuildingName(buildName);
				teachBuilding.setTeachAreaId(teachAreaId);
				teachBuilding.setIsDeleted("0".equals(isNormal)? 1 : 0);
				
				saveTeachBuildings.add(teachBuilding);
			 // 结束 ---------封装TeachBuilding
			}
		// 开始 ---------封装TeachArea 
			TeachArea teachArea;
			if(allTeachAreaMap.get(teachAreaId) != null) {
				teachArea = allTeachAreaMap.get(teachAreaId);
			}else {
				teachArea = new TeachArea();
				teachArea.setId(teachAreaId);
				teachArea.setRemark("文轩智慧平台推送过来的数据");
				teachArea.setCreationTime(new Date());
				teachArea.setIsDeleted(WenXunConstant.DEFAULT_IS_DELETED_VALUE);
				teachArea.setAreaCode("000001");
			}
			//关键字段
			teachArea.setUnitId(unitId);
			teachArea.setAreaName(areaName);
			teachArea.setModifyTime(new Date());
			
			saveTeachAreas.add(teachArea);
		 // 结束 ---------封装TeachArea
		}
		
		//更新教师学区信息
		if(CollectionUtils.isNotEmpty(saveTeachAreas)) {
			log.info("同步教师学区的总数---" + saveTeachAreas.size());
			baseSyncSaveService.saveTeachArea(saveTeachAreas.toArray(new TeachArea[0]));
		}
		//保存楼层信息
		if(CollectionUtils.isNotEmpty(saveTeachBuildings)) {
			baseSyncSaveService.saveTeachBuilding(saveTeachBuildings.toArray(new TeachBuilding[0]));
		}
	}
	
	/**
	 * @param json
	 * @return
	 */
	private  JSONArray getData(String json) {
//		JSONObject jsonObject = JSONObject.parseObject(json);
		JSONArray array = null;
		try {
			array = Json.parseArray(json);
		} catch (Exception e) {
			log.error("数据转化异常" + e.getMessage());
			return null;
		}
		return array;
	}
	
	private boolean isContainChinese(String str) {
		boolean isChinese = false;
		if (StringUtils.isBlank(str)) {
			return false;
		}
		for (char ch : str.toCharArray()) {
			if (ch >= '\u4e00' && ch <= '\u9fa5') {
				isChinese = true;
				break;
			}
		}
		return isChinese;
	}
	
	/**
	 * 峨眉二小 不加前缀
	 * @param string
	 * @return
	 */
	private String doChargeName(String name) {
		if(doProvingDeploy(WenXunConstant.DEPLOY_EMEIERXIAO)){
			name = WenXunConstant.WENXUN_BEFORE_SCHOOLNAME_VALUE + name;
		}
		return name;
	}
	
	/**
     * 验证部署地区
     * @param region
     * @return
     */
    private boolean doProvingDeploy(String region) {
		String	deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		return !region.equals(deployRegion);
	}
    
	/**
	 * @param ADteacherDuty
	 * @param teacherId
	 * @param duty
	 * 添加新的教师职务
	 */
	private void addNewTeacherDuty(List<TeacherDuty> ADteacherDuty, String teacherId, JSONObject duty) {
		TeacherDuty teacherDuty = new TeacherDuty();
		teacherDuty.setId(UuidUtils.generateUuid());
		teacherDuty.setTeacherId(teacherId);
		teacherDuty.setEventSource(WenXunConstant.DEFAULT_EVENT_SOURCE_VALUE);
		teacherDuty.setIsDeleted(WenXunConstant.DEFAULT_IS_DELETED_VALUE);
		teacherDuty.setDutyCode(duty.getString("id"));  //职务码要转化 --------------------- 未处理
		
		ADteacherDuty.add(teacherDuty);
	}
	
	//加密字段的解密
	private  String decryptData(String data) {
		WXPlatformUtils platform = new WXPlatformUtils();
		try {
			data = platform.decrypt(data, WenXunConstant.WENXUN_APP_KEY_VALUE);
		} catch (Exception e) {
			log.error("数据解密失败----");
			return null;
		}
		return data;
	}
	
	/**
	 * @param name
	 * 名字的截取
	 */
	private String validateName(String name,int length) {
		name = WenXunConstant.WENXUN_BEFORE_USERNAME_VALUE + name;
		if(net.zdsoft.framework.utils.StringUtils.getRealLength(name) > length){
			name = StringUtils.substring(name, 0, length);
		}
		return name;
	}
	
	public static void main(String[] args) {
		
		String name = "11111111111112222222222222222222333333333";
		System.out.println(name.length());
		name = StringUtils.substring(name, 0, 32);
		System.out.println(name.length());
	}
}
