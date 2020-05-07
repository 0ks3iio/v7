package net.zdsoft.basedata.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.SchoolSemester;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StusysSectionTimeSet;
import net.zdsoft.basedata.entity.SubSchool;
import net.zdsoft.basedata.entity.TeachArea;
import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.TeacherDuty;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.extension.entity.ServerExtension;
import net.zdsoft.basedata.extension.entity.UnitExtension;
import net.zdsoft.basedata.extension.enums.UnitExtensionExpireType;
import net.zdsoft.basedata.extension.enums.UnitExtensionNature;
import net.zdsoft.basedata.extension.enums.UnitExtensionState;
import net.zdsoft.basedata.extension.remote.service.ServerExtensionRemoteService;
import net.zdsoft.basedata.extension.remote.service.UnitExtensionRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
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
import net.zdsoft.basedata.remote.service.TeacherDutyRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.BaseSyncSaveService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.remote.service.RoleRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;

/**
 * @author yangsj  2018年4月23日下午5:24:35
 * 保存基础数据的接口
 */
@Service("baseSyncSaveService")
public class BaseSyncSaveServiceImpl implements BaseSyncSaveService {

	private Logger log = Logger.getLogger(BaseSyncSaveServiceImpl.class);
	
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private RoleRemoteService roleRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeacherDutyRemoteService teacherDutyRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeachAreaRemoteService teachAreaRemoteService;
	@Autowired
	private TeachBuildingRemoteService teachBuildingRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private SubSchoolRemoteService subSchoolRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private StusysSectionTimeSetRemoteService stusysSectionTimeSetRemoteService;
//	@Autowired
//	private DateInfoRemoteService dateInfoRemoteService;
//	@Autowired
//	private EclasscardRemoteService eclasscardRemoteService;
//	@Autowired
//	private XFStudentExService xfStudentExService;
	@Autowired
	private SchoolSemesterRemoteService schoolSemesterRemoteService;
	@Autowired
	private UnitExtensionRemoteService unitExtensionRemoteService;
	@Autowired
	private ServerExtensionRemoteService serverExtensionRemoteService;
	
	List<String> usernameExs = new ArrayList<>();
	
	@Override
	public void saveSchoolAndUnit(School[] schools, Unit[] units) {
		Set<String> regionCodeList = new HashSet<>();
		for (School school : schools) {
			regionCodeList.add(school.getRegionCode());
			regionCodeList.add(StringUtils.substring(school.getRegionCode(), 0, 4) + "00");
			regionCodeList.add(StringUtils.substring(school.getRegionCode(), 0, 2) + "0000");
		}
		List<Unit> edus = SUtils.dt(unitRemoteService.findByUnitClassAndRegion(Unit.UNIT_CLASS_EDU, 
				regionCodeList.toArray(new String[0])), new TR<List<Unit>>() {
		});
		Map<String, Unit> eduMap = EntityUtils.getMap(edus, Unit::getRegionCode);
		Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
		List<Unit> saveUnits = new ArrayList<>();
		if(units != null) {
			saveUnit(units);
		}else {
			//通过新增学校来新增单位
			for (School school : schools) {
        		Unit u = new Unit();
				u.setId(school.getId());
				u.setEventSource(school.getEventSource());
				u.setOrgVersion(1);
				u.setUnitState(Unit.UNIT_MARK_NORAML);
				u.setAuthorized(1);
				u.setUseType(1);
				u.setCreationTime(school.getCreationTime());
				u.setRegionCode(school.getRegionCode());
				String regionCode = StringUtils.substring(u.getRegionCode(), 0, 6);
				String regionCode1 = StringUtils.substring(u.getRegionCode(), 0, 4) + "00";
				String regionCode2 = StringUtils.substring(u.getRegionCode(), 0, 2) + "0000";
				Unit edu = eduMap.get(regionCode);
				if (edu != null) {
					u.setParentId(edu.getId());
				} else if (eduMap.get(regionCode1) != null) {
					u.setParentId(eduMap.get(regionCode1).getId());
				} else if (eduMap.get(regionCode2) != null) {
					u.setParentId(eduMap.get(regionCode2).getId());
				}else{
					u.setParentId(topUnit == null ? Constant.GUID_ZERO : topUnit.getId());
				}
				Long max = RedisUtils.incrby("syncdata.bjdy.unit.unioncode.max.regioncode." + u.getRegionCode(), 1);
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
			}
		}
		if(CollectionUtils.isNotEmpty(saveUnits)) {
			saveUnit(saveUnits.toArray(new Unit[0]));
		}
		saveSchool(schools);
	}
	
	private void saveUnitBySchool(School[] schools) {
		Set<String> regionCodeList = new HashSet<>();
		Set<String> unionCodeList = new HashSet<>();
		Set<String> parentIdList = new HashSet<>();
		for (School school : schools) {
			String regionCode = school.getRegionCode();
			if(StringUtils.isNotBlank(regionCode)) {
				if(regionCode.length() == 9) {
					unionCodeList.add(regionCode);
				}else {
					regionCodeList.add(school.getRegionCode());
					regionCodeList.add(StringUtils.substring(school.getRegionCode(), 0, 4) + "00");
					regionCodeList.add(StringUtils.substring(school.getRegionCode(), 0, 2) + "0000");
				}
			}
			String parentId = school.getParentId();
			if(StringUtils.isNotBlank(parentId)){
				parentIdList.add(parentId);
			}
		}
		Map<String, Unit> eduMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(regionCodeList)) {
			List<Unit> edus = SUtils.dt(unitRemoteService.findByUnitClassAndRegion(Unit.UNIT_CLASS_EDU, 
					regionCodeList.toArray(new String[0])), new TR<List<Unit>>() {
			});
			eduMap = EntityUtils.getMap(edus, Unit::getRegionCode);
		}
		Map<String, Unit> lecelEduMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(unionCodeList)) {
			List<Unit> lecelEdus = SUtils.dt(unitRemoteService.findByUnitClassAndUnionCode(Unit.UNIT_CLASS_EDU, 
					unionCodeList.toArray(new String[0])), new TR<List<Unit>>() {
			});
			lecelEduMap = EntityUtils.getMap(lecelEdus, Unit::getUnionCode);
		}
		
		Map<String, Unit> pidMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(parentIdList)){
			List<Unit> pidEdus = SUtils.dt(unitRemoteService.findListByIds(parentIdList.toArray(new String[0])), new TR<List<Unit>>() {
			});
			pidMap = EntityUtils.getMap(pidEdus, Unit::getId);
		}
		
		Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
		List<Unit> saveUnits = new ArrayList<>();
			//通过新增学校来新增单位
		boolean isdo = true;
		for (School school : schools) {
			String regionCode = school.getRegionCode();
			String parentId = school.getParentId();
    		Unit u = new Unit();
			u.setId(school.getId());
			u.setEventSource(school.getEventSource());
			u.setOrgVersion(1);
			u.setUnitState(Unit.UNIT_MARK_NORAML);
			u.setAuthorized(1);
			u.setUseType(1);
			u.setCreationTime(school.getCreationTime());
			u.setRegionCode(regionCode);
			Unit edu;
			if(regionCode.length() == 9) {
				edu = lecelEduMap.get(regionCode);
				if (edu != null) {
					u.setParentId(edu.getId());
					isdo = false;
				}
			}
			
			if(StringUtils.isNotBlank(parentId) &&  pidMap != null && pidMap.get(parentId) != null){
				u.setParentId(parentId);
				String unionCode = pidMap.get(parentId).getUnionCode();
				Long max = RedisUtils.incrby("syncdata.bjdy.unit.unioncode.max.regioncode." + unionCode, 1);
				Integer length = 12 - unionCode.length();
				u.setUnionCode(unionCode + StringUtils.leftPad(max + "", length, "0"));
			}else{
				if(isdo) {
					String regionCode3 = StringUtils.substring(regionCode, 0, 6);
					String regionCode1 = StringUtils.substring(regionCode, 0, 4) + "00";
					String regionCode2 = StringUtils.substring(regionCode, 0, 2) + "0000";
					edu = eduMap.get(regionCode3);
					if (edu != null) {
						u.setParentId(edu.getId());
					} else if (eduMap.get(regionCode1) != null) {
						u.setParentId(eduMap.get(regionCode1).getId());
					} else if (eduMap.get(regionCode2) != null) {
						u.setParentId(eduMap.get(regionCode2).getId());
					}else{
						u.setParentId(topUnit == null ? Constant.GUID_ZERO : topUnit.getId());
					}
				}
				Long max = RedisUtils.incrby("syncdata.bjdy.unit.unioncode.max.regioncode." + u.getRegionCode(), 1);
				if(StringUtils.isEmpty(regionCode)) {
					u.setUnionCode(StringUtils.leftPad(max + "", 6, "0"));
				}else {
					u.setUnionCode(regionCode + StringUtils.leftPad(max + "", 6, "0"));
				}
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
			
			//根据学校类型来得出单位类型
			String schoolType = school.getSchoolType();
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
		if(CollectionUtils.isNotEmpty(saveUnits)) {
			saveUnit(saveUnits.toArray(new Unit[0]));
		}
	}
	
	@Override
	public void saveSchool(School[] schools) {
		saveSchool(schools,null,false);
	}
	
	/**
	 * 
	 * @param schools
	 * @param returnMsg
	 * @param isSaveUnit 是否要保存unit
	 */
	@Override
	public void saveSchool(School[] schools,Map<String, String> returnMsg,Boolean isSaveUnit) {
		try {
			List<School> saveSchools = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (School school : schools) {
				if(StringUtils.isBlank(school.getRegionPropertyCode())) 
					school.setRegionPropertyCode("121");  //区域属性码  默认取值---  镇中心区
				if(school.getInfantYear() == null)
					school.setInfantYear(3); //幼儿园学制
				if(school.getGradeYear() == null)
					school.setGradeYear(6);  //小学学制
				if(school.getJuniorYear() == null)
					school.setJuniorYear(3); //初中学制
				if(school.getSeniorYear() == null)
					school.setSeniorYear(3); //高中学制
				if(school.getCreationTime() == null)
					school.setCreationTime(new Date());
				if(school.getModifyTime() == null)
					school.setModifyTime(new Date());
				List<Object> paramList = new ArrayList<>(); 
				//先判断数据库中非空的字段是否满足条件   
				String id = school.getId();
				String schoolName = school.getSchoolName();
				//判断关键的字段是否不为空
				String regionCode = school.getRegionCode();
				String schoolType = school.getSchoolType();
				String schoolCode = school.getSchoolCode();
				String sections = school.getSections();
				paramList.add(schoolName);
				paramList.add(schoolType);
				paramList.add(schoolCode);
				paramList.add(sections);
				if(validateId(id) || validateRegionCode(regionCode) || validateNoEmpty(paramList)) {
					   errorMsg.append(id +",");
					   continue;
				}
				saveSchools.add(school);
			}
			getReturnMsg(returnMsg, errorMsg);
			System.out.println("保存学校单位的数据是：-------------------" + saveSchools.size());
			if(CollectionUtils.isNotEmpty(saveSchools)) {
				schoolRemoteService.saveAll(SUtils.s(saveSchools.toArray(new School[0])));
			}
			if(isSaveUnit) {
				saveUnitBySchool(saveSchools.toArray(new School[0]));
			}
		} catch (Exception e) {
			log.error("保存学校数据失败"+ e, e);
		}
	}
	
	@Override
	public void saveUnit(Unit[] units) {
		saveUnit(units,null,Boolean.TRUE);
	}
	
	@Override
	public void saveUnit(Unit[] units,Map<String,String> returnMsg,Boolean isSaveAdmin) {
		//先解析出数据中的 unitid
		Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
		Set<String> unitIdList = new HashSet<>();
		for (Unit unit : units) {
			unit.setEventSource(getDefaultSource());
			if(unit.getOrgVersion() == null)
				unit.setOrgVersion(0);
			if(unit.getAuthorized() == null)
				unit.setAuthorized(1);
			if(unit.getUnitState() == null)
				unit.setUnitState(1);
			if(unit.getUseType()== null)
				unit.setUseType(1);
			if(unit.getRegionLevel() == null)
				unit.setRegionLevel(unit.createRegionLevel());
			if(unit.getCreationTime() == null)
				unit.setCreationTime(new Date());
			if(unit.getModifyTime() == null)
				unit.setModifyTime(new Date());
			if(unit.getIsDeleted() == null)
				unit.setIsDeleted(0);
			if(StringUtils.isBlank(unit.getRootUnitId()))
				unit.setRootUnitId(topUnit.getId());
			unitIdList.add(unit.getId());
		}
		List<Unit> allUnit = SUtils.dt(
				unitRemoteService.findListByIds(unitIdList.toArray(new String[unitIdList.size()])), 
				new TR<List<Unit>>() {
		});
		Map<String, Unit> allUnitMap = EntityUtils.getMap(allUnit, Unit::getId);
		
		
		
		
		List<Role> schoolDefault ;
		final String defaultRoleUid =  "defaultRoleUid";
		if(RedisUtils.get(defaultRoleUid) != null) {
			schoolDefault =  RedisUtils.getObject(defaultRoleUid, new TypeReference<List<Role>>(){});
		}else {
			String roleUid = StringUtils.substring(Constant.GUID_ZERO,0, Constant.GUID_ZERO.length()-1) + "1";
			schoolDefault = SUtils.dt(roleRemoteService.findByUnitId(roleUid), new TR<List<Role>>() {});
			RedisUtils.setObject(defaultRoleUid, schoolDefault, 300);
		}
		List<Unit> saveUnits = new ArrayList<>();
		List<Role> saveRoles = new ArrayList<>();
		List<Dept> saveDepts = new ArrayList<>();
		List<Teacher> saveTeachers = new ArrayList<>();
		List<User> saveUsers = new ArrayList<>();
		StringBuilder errorMsg = new StringBuilder();
		usernameExs.clear();
		for (Unit unit : units) {
			List<Object> paramList = new ArrayList<>(); 
			//数据库中非空的字段
			String id = unit.getId();
			String unitName = unit.getUnitName();
			String parentId = unit.getParentId();
			Date modifyTime = unit.getModifyTime();
			Integer isDeleted = unit.getIsDeleted();
			//主要的字段非空
			Integer unitClass = unit.getUnitClass();
			Integer unitType = unit.getUnitType();
			Integer regionLevel = unit.getRegionLevel();
			String regionCode = unit.getRegionCode();
			String schoolType = unit.getSchoolType();
			paramList.add(unitName);
			paramList.add(parentId);
			if(unit.getUnitClass() != Unit.UNIT_CLASS_EDU)
			   paramList.add(schoolType);
			paramList.add(unitClass);
			paramList.add(unitType);
			paramList.add(regionLevel);
			paramList.add(modifyTime);
			paramList.add(isDeleted);
			if(doJudgeField(paramList, regionCode,id)){
				log.error("单位数据关键字段为空="+id);
				errorMsg.append(id +",");
				continue;
			}
			if(MapUtils.isEmpty(allUnitMap) || allUnitMap.get(id) == null) {
				//判断是否是新增的unit,添加默认的角色 和部门
				addDefaultRole(saveRoles, schoolDefault, unit);
				 //部门ID，D开头
				Dept d = addDefaultDept(saveDepts, unit);
				if(isSaveAdmin){
					// 教师管理员ID，B开头
					Teacher t = doDefaultAdminTea(saveTeachers, unit, d);
					// 单位管理员用户ID，A开头
					User ur = addDefaultAdminUser(unit, d, t);
					saveUsers.add(ur);
				}
			}
			saveUnits.add(unit);
		}
		getReturnMsg(returnMsg, errorMsg);
		System.out.println("保存单位的数据是：-------------------" + saveUnits.size());
		if(CollectionUtils.isNotEmpty(saveUnits)) {
			unitRemoteService.saveAll(SUtils.s(saveUnits.toArray(new Unit[0])));
		}
		//保存默认角色
		if(CollectionUtils.isNotEmpty(saveRoles)) {
			saveRole(saveRoles.toArray(new Role[0]));
		}
		//保存默认部门
		if(CollectionUtils.isNotEmpty(saveDepts)) {
			saveDept(saveDepts.toArray(new Dept[0]));
		}
		//保存默认管理员教师
		if(CollectionUtils.isNotEmpty(saveTeachers)) {
			saveTeacher(saveTeachers.toArray(new Teacher[0]));
		}
		//保存默认管理员用户
		if(CollectionUtils.isNotEmpty(saveUsers)) {
			saveUser(saveUsers.toArray(new User[0]));
		}
	}

	@Override
	public void saveDept(Dept[] depts) {
		saveDept(depts,null);
	}
	
	@Override
	public void saveDept(Dept[] depts,Map<String, String> returnMsg) {
       try {
		List<Dept> saveDepts = new ArrayList<>();
		   StringBuilder errorMsg = new StringBuilder();
		   for (Dept dept : depts) {
			   List<Object> paramList = new ArrayList<>(); 
			   //数据库中非空的字段
			   String id = dept.getId();
			   if(StringUtils.isBlank(dept.getParentId()))
				   dept.setParentId(Constant.GUID_ZERO);
			   if(StringUtils.isBlank(dept.getInstituteId()))
				   dept.setInstituteId(Constant.GUID_ZERO);
			   if(dept.getEventSource() == null)
				   dept.setEventSource(getDefaultSource());
			   if(dept.getCreationTime() == null)
				   dept.setCreationTime(new Date());
			   if(dept.getModifyTime() == null)
				   dept.setModifyTime(new Date());
			   if(dept.getIsDeleted() == null)
				   dept.setIsDeleted(0);
			   if(dept.getDeptType() == null)
				   dept.setDeptType(1);
			   String unitId = dept.getUnitId();
			   String deptName = dept.getDeptName();
			   String parentId = dept.getParentId();
			   //主要的字段非空
			   String deptCode = dept.getDeptCode();
			   Integer deptType = dept.getDeptType();    //DM-JYBZ 1表示科室，2 教研室
			   paramList.add(unitId);
			   paramList.add(deptName);
			   paramList.add(parentId);
			   paramList.add(deptCode);
			   paramList.add(deptType);
			   if(doJudgeField(paramList,id)){
				   errorMsg.append(id +",");
				   log.error("部门中有关键的字段为空-----");
				   continue;
			   }
			   saveDepts.add(dept);
		   }
		   getReturnMsg(returnMsg, errorMsg);
		   System.out.println("保存部门数据是：-------------------" + saveDepts.size());
		   if(CollectionUtils.isNotEmpty(saveDepts)) {
			   deptRemoteService.saveAll(SUtils.s(saveDepts.toArray(new Dept[0])));
		   }
		} catch (Exception e) {
			log.error("保存部门数据失败"+ e, e);
		}
	}
	
	@Override
	public void saveRole(Role[] roles) {
	    try {
			List<Role> saveRoles = new ArrayList<>();
			for (Role role : roles) {
			   List<Object> paramList = new ArrayList<>(); 
			   //数据库中非空的字段
			   String id = role.getId();
			   String unitId = role.getUnitId();
			   String deptName = role.getName();
			   //主要的字段非空
			   paramList.add(unitId);
			   paramList.add(deptName);
			   if(validateId(id)|| validateNoEmpty(paramList)) {
				   log.error("角色中有关键的字段为空-----");
				   continue;
			   }
			   saveRoles.add(role);
			}
			if(CollectionUtils.isNotEmpty(saveRoles)) {
				roleRemoteService.saveAll(SUtils.s(saveRoles.toArray(new Role[0])));
			}
		} catch (Exception e) {
			log.error("保存角色数据失败"+ e, e);
		}
	}
	
	@Override
	public void saveTeacherDuty(TeacherDuty[] teacherDutys) {
		try {
			List<TeacherDuty> saveTeacherDutys = new ArrayList<>();
			for (TeacherDuty teacherDuty : teacherDutys) {
			   List<Object> paramList = new ArrayList<>(); 
			   //数据库中非空的字段
			   String id = teacherDuty.getId();
			   //主要的字段非空
			   String teacherId = teacherDuty.getTeacherId();
			   String dutyCode = teacherDuty.getDutyCode();
			   paramList.add(teacherId);
			   paramList.add(dutyCode);
			   if(doJudgeField(paramList,id)){
				   log.error("教师职务中有关键的字段为空-----");
				   continue;
			   }
			   saveTeacherDutys.add(teacherDuty);
			}
			if(CollectionUtils.isNotEmpty(saveTeacherDutys)) {
				teacherDutyRemoteService.saveAll(SUtils.s(saveTeacherDutys.toArray(new TeacherDuty[0])));
			}
		} catch (Exception e) {
			log.error("保存教师职务数据失败"+ e, e);
		}
	}
	
	@Override
	public void saveClass(Clazz[] clazzs) {
		saveClass(clazzs,null);
	}
	
	
	@Override
	public void saveClass(Clazz[] clazzs,Map<String, String> returnMsg) {
		try {
			List<Clazz> saveClazzs = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (Clazz clazz : clazzs) {
			   clazz.setEventSource(getDefaultSource());
			   if(clazz.getCreationTime() == null)
				   clazz.setCreationTime(new Date());
			   if(clazz.getModifyTime() == null)
				   clazz.setModifyTime(new Date());
			   Integer se = clazz.getSection();
			   if(clazz.getSchoolingLength() == null)
				   clazz.setSchoolingLength( (se!=null && se == 1) ? 6 : 3);
			   if(clazz.getState() == null)
				   clazz.setState(0);
			   if(clazz.getIsGraduate() == null)
				   clazz.setIsGraduate(0);  //是否毕业，默认是0
			   List<Object> paramList = new ArrayList<>(); 
			   //页面非空子段
			   if(clazz.getBuildDate() == null) {
				   clazz.setBuildDate(new Date());
			   }
			   //数据库中非空的字段
			   String id = clazz.getId();
			   String schoolId = clazz.getSchoolId();
			   String classCode = clazz.getClassCode();
			   String className = clazz.getClassName();
			   String gradeId = clazz.getGradeId();
			   //主要的字段非空
			   String acadyear = clazz.getAcadyear();
			   Integer section = clazz.getSection(); 
			   paramList.add(schoolId);
			   paramList.add(classCode);
			   paramList.add(className);
			   paramList.add(gradeId);
			   paramList.add(acadyear);
			   paramList.add(section);
			   if(validateId(id)|| validateNoEmpty(paramList)) {
				   errorMsg.append(id +",");
				   log.error("班级中有关键的字段为空-----");
				   continue;
			   }
			   saveClazzs.add(clazz);
			}
			getReturnMsg(returnMsg, errorMsg);
			System.out.println("保存班级数据是：-------------------" + saveClazzs.size());
			if(CollectionUtils.isNotEmpty(saveClazzs)) {
				classRemoteService.saveAll(SUtils.s(saveClazzs.toArray(new Clazz[0])));
			}
		} catch (Exception e) {
			log.error("保存班级数据失败"+ e, e);
		}
	}
	
	@Override
	public void saveGrade(Grade[] grades) {
		saveGrade(grades,null);
	}
	
	@Override
	public void saveGrade(Grade[] grades,Map<String, String> returnMsg) {
		try {
			List<Grade> saveGrades = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (Grade grade : grades) {
			   if(grade.getEventSource() == null)
				   grade.setEventSource(getDefaultSource());
			   if(grade.getCreationTime() == null)
				   grade.setCreationTime(new Date());
			   if(grade.getModifyTime() == null)
				   grade.setModifyTime(new Date());
			   if(grade.getAmLessonCount() == null)
				   grade.setAmLessonCount(4);
			   if(grade.getPmLessonCount() == null)
				   grade.setPmLessonCount(4);
			   if(grade.getNightLessonCount() == null)
				   grade.setNightLessonCount(0);
			   if(grade.getSchoolingLength() == null)
				   grade.setSchoolingLength( (grade.getSection()!=null && grade.getSection() == 1) ? 6 : 3);
			   if(grade.getWeekDays() == null)
				   grade.setWeekDays(Grade.DEFAULT_WEEK_DAYS);
			   List<Object> paramList = new ArrayList<>(); 
			   //数据库中非空的字段
			   String id = grade.getId();
			   String schoolId = grade.getSchoolId();
			   String apenAcadyear = grade.getOpenAcadyear();
			   //主要的字段非空
			   String gradeName = grade.getGradeName();
			   Integer section = grade.getSection();
			   String gradeCode = grade.getGradeCode();
			   paramList.add(schoolId);
			   paramList.add(apenAcadyear);
			   paramList.add(gradeName);
			   paramList.add(section);
			   paramList.add(gradeCode);
			   if(validateId(id)|| validateNoEmpty(paramList)) {
				   errorMsg.append(id +",");
				   log.error("年级中有关键的字段为空-----");
				   continue;
			   }
			   saveGrades.add(grade);
			}
			getReturnMsg(returnMsg, errorMsg);
			System.out.println("保存年级数据是：-------------------" + saveGrades.size());
			if(CollectionUtils.isNotEmpty(saveGrades)) {
				gradeRemoteService.saveAll(SUtils.s(saveGrades.toArray(new Grade[0])));
			}
		} catch (Exception e) {
			log.error("保存年级数据失败"+ e, e);
		}
	}
	
	@Override
	public void saveClassAndGrade(Clazz[] clazzs, Grade[] grades) {
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
		if (semester == null) {
			log.info("------ 请先维护学年学期 -------");
			return;
		}
		List<Grade> saveGrades = new ArrayList<>();
		if(grades != null) {
			saveGrade(grades);
		}else {
			Map<String, Grade> gradeMap = new HashMap<>();
			for (Clazz clazz : clazzs) {
				String gradesJson = gradeRemoteService.findBySchoolId(clazz.getSchoolId(),
						ArrayUtils.toArray(clazz.getSection()), clazz.getAcadyear(), true);
				List<Grade> gradesExists = SUtils.dt(gradesJson, new TypeReference<List<Grade>>() {
				});
				for (Grade grade : gradesExists) {
					String key = grade.getSchoolId() + grade.getOpenAcadyear() + grade.getSection();
					gradeMap.put(key, grade);
				}
				Grade grade = gradeMap.get(clazz.getSchoolId() + clazz.getAcadyear() + clazz.getSection());
				if (grade == null) {
					grade = new Grade();
					grade.setId(UuidUtils.generateUuid());
					grade.setCreationTime(new Date());
					grade.setModifyTime(new Date());
					grade.setEventSource(getDefaultSource());
					grade.setAmLessonCount(4);
					grade.setPmLessonCount(4);
					grade.setOpenAcadyear(clazz.getAcadyear());
					grade.setNightLessonCount(3);
					//进行转换得到年级code
					Integer section = clazz.getSection();
					String year = StringUtils.substring(clazz.getAcadyear(), 0, 4);
					String year2 = StringUtils.substring(semester.getAcadyear(), 0, 4);
					String gradeCode = String.valueOf(section) + (NumberUtils.toInt(year2) -  NumberUtils.toInt(year) + 1);
					grade.setGradeCode(gradeCode);
					grade.setSubschoolId(clazz.getCampusId());
					grade.setSchoolId(clazz.getSchoolId());
					grade.setIsGraduate(0);
					grade.setSection(clazz.getSection());
					grade.setSchoolingLength(clazz.getSchoolingLength());
					if (grade.getSchoolingLength() == null || grade.getSchoolingLength() == 0) {
						grade.setSchoolingLength(grade.getSection() == 1 ? 6 : 3);
					}
					
					String name = section == 1 ?"小学": section == 2 ? "初中" : section == 3 ? "高中" : "高职";
					String gradeName = name + (NumberUtils.toInt(year2) -  NumberUtils.toInt(year) +1) + "年级";
					grade.setGradeName(gradeName);
					
					// 年级组长使用本平台数据
//				Grade oldGrade = allGradeMap.get(grade1.getId());
//				if (oldGrade != null) {
//					grade1.setTeacherId(oldGrade.getTeacherId());
//				}
					saveGrades.add(grade);
					gradeMap.put(grade.getSchoolId() + grade.getOpenAcadyear() + grade.getSection(), grade);
				}
				clazz.setGradeId(grade.getId());
		    }
		}
		
		if(CollectionUtils.isNotEmpty(saveGrades)) {
			saveGrade(saveGrades.toArray(new Grade[0]));
		}
		saveClass(clazzs);
		
	}
	
	@Override
	public void saveTeacher(Teacher[] teachers) {
		saveTeacher(teachers,null);
	}
	
	@Override
	public void saveTeacher(Teacher[] teachers, Map<String, String> returnMsg) {
		saveTeacherAndUser(teachers,null,returnMsg);
	}
	
	@Override
	public void saveTeacherAndUser(Teacher[] teachers, User[] users) {
		saveTeacherAndUser(teachers,users,null);
	}
	
	@Override
	public void saveTeacherAndUser(Teacher[] teachers, User[] users, Map<String, String> returnMsg) {
		try {
			List<Teacher> saveTeachers = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (Teacher teacher : teachers) {
			   teacher.setEventSource(getDefaultSource());
			   if(teacher.getInPreparation() == null)
				   teacher.setInPreparation("1");  //是否在编  1 --是 0 --否
			   if(teacher.getIncumbencySign() == null)
				   teacher.setIncumbencySign("11"); //教师状态---在职   DQZTM
			   if(teacher.getIsLeaveSchool() == null) 
				   teacher.setIsLeaveSchool("0");  // 0--在校 1--离校
			   if(teacher.getWeaveUnitId() == null)
				   teacher.setWeaveUnitId(teacher.getUnitId());  //编制单位id  默认是当前的工作单位unitid
			   if(teacher.getCreationTime() == null)
				   teacher.setCreationTime(new Date());
			   if(teacher.getModifyTime() == null)
				   teacher.setModifyTime(new Date());
			   if(teacher.getIsDeleted() == null)
				   teacher.setIsDeleted(0);
			   if(teacher.getSex() == null)
				   teacher.setSex(User.USER_WOMAN_SEX); // 0-未知 1-男 2-女 9-未说明性别
			   if(StringUtils.isBlank(teacher.getTeacherCode()))
				   teacher.setTeacherCode("00001");
			   List<Object> paramList = new ArrayList<>(); 
			   //数据库中非空的字段
			   String id = teacher.getId();
			   String unitId = teacher.getUnitId();
			   String teacherCode = teacher.getTeacherCode();
			   Integer sex = teacher.getSex();
			   //主要的字段非空
			   String teacherName = teacher.getTeacherName();
			   String nation = teacher.getNation();
			   paramList.add(unitId);
			   paramList.add(teacherCode);
			   paramList.add(sex);
			   paramList.add(teacherName);
			   if(StringUtils.isBlank(nation)) {
				   nation = "01"; //默认汉族
				   teacher.setNation(nation);
			   }
			   paramList.add(nation);
			   if(doJudgeField(paramList,id)){
				   errorMsg.append(id +",");
				   log.error("教师中有关键的字段为空-----");
				   continue;
			   }
			   saveTeachers.add(teacher);
			}
			getReturnMsg(returnMsg, errorMsg);
			System.out.println("保存教师到数据库的数据：--------" + saveTeachers.size());
			if(CollectionUtils.isNotEmpty(saveTeachers)) {
				teacherRemoteService.saveAll(SUtils.s(saveTeachers.toArray(new Teacher[0])));
				if (ArrayUtils.isNotEmpty(users)) {
					Map<String, Teacher> teachMap = EntityUtils.getMap(saveTeachers, Teacher::getId);
					List<User> endSaveUser = new ArrayList<>();
					for (User user : users) {
						String ownerId = user.getOwnerId();
						if (teachMap.get(ownerId) != null) {
							user.setDeptId(teachMap.get(ownerId).getDeptId());
							endSaveUser.add(user);
						}
					}
					if (CollectionUtils.isNotEmpty(endSaveUser)) {
						saveUser(endSaveUser.toArray(new User[0]));
					} 
				}
			}
		} catch (Exception e) {
			log.error("保存教师数据失败"+ e, e);
		}
	}
	
	@Override
	public void saveUser(User[] users) {
		saveUser(users, null);
	}
	
	@Override
	public void saveUser(User[] users,Map<String, String> returnMsg) {
		try {
			List<User> saveUsers = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (User user : users) {
				user.setEventSource(getDefaultSource());
			   if(user.getCreationTime() == null)
				   user.setCreationTime(new Date());
			   if(user.getModifyTime() == null)
				   user.setModifyTime(new Date());
			   if(user.getIsDeleted() == null)
				   user.setIsDeleted(0);
			   if(user.getUserState() == null)
				   user.setUserState(1);  //DM-YHZT  0-未审核  1-正常  2-锁定 3-离职锁定
			   if(user.getUserType() == null)
				   user.setUserType(User.USER_TYPE_COMMON_USER);
			   if(user.getUserRole() == null)
				   user.setUserRole(2); //默认是2
			   if(user.getEnrollYear() == null)
				   user.setEnrollYear(0000);
			   if(user.getIconIndex() == null)
				   user.setIconIndex(0);
			   if(user.getSex() == null)
				   user.setSex(1);
			   List<Object> paramList = new ArrayList<>(); 
			   Set<String> idList = new HashSet<>();
			   //数据库中非空的字段
			   String id = user.getId();
			   String unitId = user.getUnitId();
			   String ownerId = user.getOwnerId();
			   Integer ownerType = user.getOwnerType();
			   String username = user.getUsername();
			   doDefeuleValue(user);
			   //主要的字段非空
			   String regionCode = user.getRegionCode();
			   String password = user.getPassword();
			   if(user.getUserRole() == null)
				   user.setUserRole(2);
			   if(user.getIconIndex() == null)
				   user.setIconIndex(0);
			   if(user.getEnrollYear() == null)
				   user.setEnrollYear(Calendar.getInstance().get(Calendar.YEAR));
			   if(user.getUserState() == null)
				   user.setUserState(1);
			   if(user.getUserType() == null)
				   user.setUserType(1);
			   if(StringUtils.isBlank(user.getAccountId()))
				   user.setAccountId(user.getId());
			   idList.add(id);
			   idList.add(unitId);
			   idList.add(ownerId);
			   paramList.add(ownerType);
			   paramList.add(username);
			   paramList.add(password);
			   if(doJudgeField(paramList, regionCode, idList.toArray(new String[idList.size()]))){
				       errorMsg.append(id +",");
				       log.error("用户中有关键的字段为空-----");
					   continue;
			   }
			   //密码进行加密
			   if(password.trim().length() != 64){
				   user.setPassword(new PWD(password).encode());
			   }
			   saveUsers.add(user);
			}
			getReturnMsg(returnMsg, errorMsg);
			System.out.println("保存用户到数据库的数据：--------" + saveUsers.size());
			if(CollectionUtils.isNotEmpty(saveUsers)) {
				userRemoteService.saveAll(SUtils.s(saveUsers.toArray(new User[0])));
				//同步passport
				syncToPassport(saveUsers);
			}
		} catch (Exception e) {
			log.error("保存用户数据失败"+ e, e);
		}
	}

	@Override
	public void saveFamily(Family[] familys) {
		saveFamily(familys,null);
	}
	
	@Override
	public void saveFamilyAndUser(Family[] families, User[] users) {
		saveFamilyAndUser(families,users, null);
	}
	
	@Override
	public void saveFamily(Family[] familys,Map<String, String> returnMsg) {
		saveFamilyAndUser(familys,null,returnMsg);
	}
	
	@Override
	public void saveFamilyAndUser(Family[] familys, User[] users, Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		try {
			List<Family> saveFamilys = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (Family family : familys) {
				family.setEventSource(getDefaultSource());
			   if(family.getIsLeaveSchool() == null) 
				   family.setIsLeaveSchool(0);  // 0--在校 1--离校
			   if(family.getCreationTime() == null)
				   family.setCreationTime(new Date());
			   if(family.getModifyTime() == null)
				   family.setModifyTime(new Date());
			   if(family.getIsDeleted() == null)
				   family.setIsDeleted(0); 
			   if(family.getIsGuardian() == null)
				   family.setIsGuardian(0);  //是否是监护人  1监护人/ 0非监护人
			   if(family.getLinkPhone() == null)
				   family.setLinkPhone(family.getMobilePhone());
			   List<Object> paramList = new ArrayList<>(); 
			   Set<String> idList = new HashSet<>();
			   //数据库中非空的字段
			   String id = family.getId();
			   String studentId = family.getStudentId();
			   String schoolId = family.getSchoolId();
			   String relation = family.getRelation();
			   String realName = family.getRealName();
			   //主要的字段非空
//			   String mobilePhone = family.getMobilePhone();
			   if(family.getOpenUserStatus() == null) {
				   family.setOpenUserStatus(1);
			   }
			   if(family.getIsGuardian() == null) {
				   family.setIsGuardian(0);
			   }
			   if(family.getRelation() == null) {
				   family.setRelation("97");
				   relation = "97";  //为null时默认为97 -- 其他亲属
			   }
			   idList.add(id);
			   idList.add(schoolId);
			   idList.add(studentId);
			   paramList.add(relation);
			   paramList.add(realName);
//			   paramList.add(mobilePhone);
			   if(doJudgeField(paramList,idList.toArray(new String[idList.size()]))){
				   errorMsg.append(id +",");
				   log.error("家长的真实姓名太长，超过30或者是关键的字段为空-----");
				   continue;
			   }
			   saveFamilys.add(family);
			}
			getReturnMsg(returnMsg, errorMsg);
			
			if(CollectionUtils.isNotEmpty(saveFamilys)) {
				familyRemoteService.saveAll(SUtils.s(saveFamilys.toArray(new Family[0])));
				Map<String, Family> familyMap = EntityUtils.getMap(saveFamilys, Family::getId);
				List<User> endSaveUser = new ArrayList<>();
				for (User user : users) {
					String ownerId = user.getOwnerId();
					if(familyMap.get(ownerId) != null){
						endSaveUser.add(user);
					}
				}
				if(CollectionUtils.isNotEmpty(endSaveUser)){
					saveUser(endSaveUser.toArray(new User[0]));
				}
			}
		} catch (Exception e) {
			log.error("保存家长数据失败"+ e.getMessage(), e);
		}
		
	}
	
	@Override
	public void saveStudent(Student[] students) {
		saveStudent(students,null);
	}
	
	@Override
	public void saveStudent(Student[] students,Map<String, String> returnMsg) {
		saveStudentAndUser(students,null,returnMsg);
	}
	
	@Override
	public void saveStudentAndUser(Student[] students, User[] users) {
		saveStudentAndUser(students,users,null);
	}
	
	@Override
	public void saveStudentAndUser(Student[] students, User[] users,
			Map<String, String> returnMsg) {
		try {
			List<Student> saveStudents = new ArrayList<>();
//			List<XFStudentEx> studentExs = new ArrayList<XFStudentEx>();
			StringBuilder errorMsg = new StringBuilder();
			for (Student student : students) {
				student.setEventSource(getDefaultSource());
			   if(student.getIsLeaveSchool() == null) 
				   student.setIsLeaveSchool(0);  // 0--在校 1--离校
			   if(student.getCreationTime() == null)
				   student.setCreationTime(new Date());
			   if(student.getModifyTime() == null)
				   student.setModifyTime(new Date());
			   if(student.getIsDeleted() == null)
				   student.setIsDeleted(0);
			   if(student.getSex() == null)
				   student.setSex(0);
			   List<Object> paramList = new ArrayList<>(); 
			   Set<String> idList = new HashSet<>();
			   //数据库中非空的字段
			   String id = student.getId();
			   String studentName = student.getStudentName();
			   String schoolId = student.getSchoolId();
			   //主要的字段非空
			   String classId = student.getClassId();
			   Integer sex = student.getSex();
			   String studentCode = student.getStudentCode();
			   if(StringUtils.isBlank(studentCode)) {
				   studentCode = student.getIdentityCard();
				   student.setStudentCode(studentCode);
			   }
			   idList.add(id);
			   idList.add(schoolId);
			   paramList.add(studentName);
			   paramList.add(classId);
			   paramList.add(sex);
			   paramList.add(studentCode);
			   if(doJudgeField(paramList,idList.toArray(new String[idList.size()]))){
				   errorMsg.append(id +",");
				   log.error("学生中有关键的字段为空-----");
				   continue;
			   }
			   saveStudents.add(student);
			   //把数据同步到base_student_ex表中
//			   XFStudentEx xfStudentEx = new XFStudentEx();
//			   xfStudentEx.setId(id);
//			   xfStudentEx.setSchoolId(schoolId);
//			   xfStudentEx.setIsDeleted(student.getIsDeleted());
//			   xfStudentEx.setModifyTime(student.getModifyTime());
//			   xfStudentEx.setNation(student.getNation());
//			   xfStudentEx.setHomeAddress(student.getHomeAddress());
//			   studentExs.add(xfStudentEx);
			}
			getReturnMsg(returnMsg, errorMsg);
			System.out.println("保存学生到数据库的数据：--------" + saveStudents.size());
			if(CollectionUtils.isNotEmpty(saveStudents)) {
				studentRemoteService.saveAll(SUtils.s(saveStudents.toArray(new Student[0])));
//				xfStudentExService.saveAll(studentExs.toArray(new XFStudentEx[0]));
				Map<String, Student> studentMap = EntityUtils.getMap(saveStudents, Student::getId);
				List<User> endSaveUser = new ArrayList<>();
				if(users != null ){
					for (User user : users) {
						String ownerId = user.getOwnerId();
						if(studentMap.get(ownerId) != null){
							endSaveUser.add(user);
						}
					}
				}
				if(CollectionUtils.isNotEmpty(endSaveUser)){
					saveUser(endSaveUser.toArray(new User[0]));
				}
			}
		} catch (Exception e) {
			log.error("保存学生数据失败"+ e.getMessage(), e);
		}
	}
	
	@Override
	public void saveTeachArea(TeachArea[] teachAreas) {
		try {
			List<TeachArea> saveTeachAreas = new ArrayList<>();
			for (TeachArea teachArea : teachAreas) {
			   List<Object> paramList = new ArrayList<>(); 
			   Set<String> idList = new HashSet<>();
			   //数据库中非空的字段
			   String id = teachArea.getId();
			   String unitId = teachArea.getUnitId();
			   String areaCode = teachArea.getAreaCode();
			   String areaName = teachArea.getAreaName();
			   //主要的字段非空
			   
			   idList.add(id);
			   idList.add(unitId);
			   paramList.add(areaCode);
			   paramList.add(areaName);
			   if(doJudgeField(paramList,idList.toArray(new String[idList.size()]))){
				   log.error("教师校区中有关键的字段为空-----");
				   continue;
			   }
			   saveTeachAreas.add(teachArea);
			}
			if(CollectionUtils.isNotEmpty(saveTeachAreas)) {
				teachAreaRemoteService.saveAll(SUtils.s(saveTeachAreas.toArray(new TeachArea[0])));
			}
		} catch (Exception e) {
			log.error("保存教师校区数据失败"+ e.getMessage(), e);
		}
	}

	@Override
	public void saveTeachBuilding(TeachBuilding[] teachBuildings) {
		try {
			List<TeachBuilding> saveTeachBuildings = new ArrayList<>();
			for (TeachBuilding teachBuilding : teachBuildings) {
			   List<Object> paramList = new ArrayList<>(); 
			   Set<String> idList = new HashSet<>();
			   //数据库中非空的字段
			   String id = teachBuilding.getId();
			   //主要的字段非空
			   String unitId = teachBuilding.getUnitId();
			   String teachAreaId = teachBuilding.getTeachAreaId();
			   String buildingName = teachBuilding.getBuildingName();
			   Integer isDeleted = teachBuilding.getIsDeleted();
			   if(isDeleted == null) {
				   isDeleted = 0; 
			   }
			   idList.add(id);
			   idList.add(unitId);
			   idList.add(teachAreaId);
			   paramList.add(buildingName);
			   paramList.add(isDeleted);
			   if(doJudgeField(paramList,idList.toArray(new String[idList.size()]))){
				   log.error("楼层信息中有关键的字段为空-----" + id);
				   continue;
			   }
			   saveTeachBuildings.add(teachBuilding);
			}
			System.out.println("保存楼层信息数据是：-------------------" + saveTeachBuildings.size());
			if(CollectionUtils.isNotEmpty(saveTeachBuildings)) {
				teachBuildingRemoteService.saveAll(SUtils.s(saveTeachBuildings.toArray(new TeachBuilding[0])));
			}
		} catch (Exception e) {
			log.error("保存楼层信息数据失败"+ e.getMessage(), e);
		}
	}
	

	@Override
	public void saveTeachPlace(TeachPlace[] teachPlaces) {
		try {
			List<TeachPlace> saveTeachPlaces = new ArrayList<>();
			for (TeachPlace teachPlace : teachPlaces) {
			   List<Object> paramList = new ArrayList<>(); 
			   Set<String> idList = new HashSet<>();
			   //数据库中非空的字段
			   String id = teachPlace.getId();
			   String unitId = teachPlace.getUnitId();
			   String placeCode = teachPlace.getPlaceCode();
			   String placeName = teachPlace.getPlaceName();
			   String teachAreaId = teachPlace.getTeachAreaId();
			   //主要的字段非空
			   String teachBuildingId = teachPlace.getTeachBuildingId();
			   idList.add(id);
			   idList.add(unitId);
			   idList.add(teachAreaId);
			   idList.add(teachBuildingId);
			   paramList.add(placeCode);
			   paramList.add(placeName);
			   if(doJudgeField(paramList,idList.toArray(new String[idList.size()]))){
				   log.error("教学场地信息中有关键的字段为空-----" + id);
				   continue;
			   }
			   saveTeachPlaces.add(teachPlace);
			}
			System.out.println("保存教学场地信息数据是：-------------------" + saveTeachPlaces.size());
			if(CollectionUtils.isNotEmpty(saveTeachPlaces)) {
				teachPlaceRemoteService.saveAll(SUtils.s(saveTeachPlaces.toArray(new TeachPlace[0])));
			}
		} catch (Exception e) {
			log.error("保存教学场地信息数据失败"+ e.getMessage(), e);
		}
	}
	/**
	 * 验证主键id
	 * @param id
	 * @return 
	 */
	private static Boolean validateId(String... ids) {
		for (String id : ids) {
			if(StringUtils.isBlank(id) || id.length() != 32 ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 验证regionCode
	 * @param regionCode
	 */
	private boolean validateRegionCode(String regionCode) {
		return (StringUtils.isBlank(regionCode) || regionCode.length() != 6 );
	}
	
	//初始化默认角色
	private void addDefaultRole(List<Role> roles, List<Role> roles1, Unit u) {
		//角色的添加和修改
		List<Role> unitRoles = SUtils.dt(roleRemoteService.findByUnitId(u.getId()), new TR<List<Role>>() {
		});
		if (CollectionUtils.isEmpty(unitRoles)) {
			for (Role role : roles1) {
				Role r = EntityUtils.copyProperties(role, Role.class);
				r.setId(UuidUtils.generateUuid());
				r.setUnitId(u.getId());
//								r.setName("");  ///这个角色的名字不用？？？ 数据库中是not null
				roles.add(r);
			}
		}
	}
	
	/**
	 * 新增单位管理员账号
	 */
	private Teacher doDefaultAdminTea(List<Teacher> teachers, Unit u, Dept d) {
		Teacher t = new Teacher();
		t.setTeacherName("管理员");
		t.setTeacherCode("000000");
		t.setId("B" + StringUtils.substring(UuidUtils.generateUuid(), 1));
		t.setUnitId(u.getId());
		t.setIncumbencySign("11");
		t.setDeptId(d.getId());
		t.setSex(McodeDetail.SEX_MALE);
		t.setCreationTime(new Date());
		t.setIsDeleted(0);
		t.setModifyTime(new Date());
		t.setEventSource(getDefaultSource());
		teachers.add(t);
		return t;
	}
	
	/**
	 * 新增单位管理员用户
	 */
	
	private User addDefaultAdminUser(Unit u, Dept d, Teacher t) {
		User ur = new User();
		String un = "wp_" + System.currentTimeMillis();
		if(usernameExs.contains(un)) {
			for(int i=0;i<10;i++) {
				un += i;
				if(!usernameExs.contains(un)) {
					break;
				}
			}
		}
		usernameExs.add(un);
		ur.setUsername(un);
		ur.setOwnerType(User.OWNER_TYPE_TEACHER);
		ur.setRegionCode(u.getRegionCode());
		ur.setId("A" + StringUtils.substring(UuidUtils.generateUuid(), 1));
		ur.setUnitId(u.getId());
		ur.setCreationTime(new Date());
		ur.setIsDeleted(0);
		ur.setUserState(1);
	    ur.setPassword("123456");
		ur.setUserRole(2);
		ur.setIconIndex(0);
		ur.setRegionCode(u.getRegionCode());
		ur.setSex(t.getSex());
		ur.setRealName("管理员");
		ur.setAccountId("C" + StringUtils.substring(ur.getId(), 1));
		ur.setModifyTime(new Date());
		ur.setEventSource(getDefaultSource());
		ur.setRoleType(0);
		ur.setEnrollYear(0000);
		ur.setDeptId(d.getId());
		ur.setOwnerId(t.getId());
		if(Unit.TOP_UNIT_GUID.equals(u.getParentId())){
			ur.setUserType(User.USER_TYPE_TOP_ADMIN);
		}else{
			ur.setUserType(User.USER_TYPE_UNIT_ADMIN);
		}
		return ur;
	}
	
	
	//初始化默认部门
	private Dept addDefaultDept(List<Dept> depts, Unit u) {
		Dept d = new Dept();
		String deptName = "默认部门";
		if(u.getUnitClass() == Unit.UNIT_CLASS_EDU)
			deptName = "管理员组";
		if(u.getUnitClass() == Unit.UNIT_CLASS_SCHOOL)
			deptName = "学校管理员组";
		d.setDeptName(deptName);
		d.setDeptCode("000001");
		d.setDeptType(1);
		d.setUnitId(u.getId());
		d.setCreationTime(new Date());
		d.setIsDeleted(0);
		d.setModifyTime(new Date());
		d.setEventSource(getDefaultSource());
		d.setParentId(Constant.GUID_ZERO);
		d.setInstituteId(Constant.GUID_ZERO);
		d.setId("D" + StringUtils.substring(u.getId(), 1));
		d.setIsDefault(1);
		// 教研组标识、部门负责人、分管领导使用原有数据
//		if(!allDeptMap.isEmpty()) {
//			Dept oldDept = allDeptMap.get(d.getId());
//			if (oldDept != null) {
//				d.setLeaderId(oldDept.getLeaderId());
//				d.setTeacherId(oldDept.getTeacherId());
//				d.setDeptType(oldDept.getDeptType());
//			}
//		}
		depts.add(d);
		return d;
	}
	
	//判断字段不能为空
	private static boolean validateNoEmpty(List<Object> ts) {
		if(CollectionUtils.isNotEmpty(ts)){
			for (Object t : ts) {
				if(t instanceof String && StringUtils.isBlank((String) t)) {
					return true;
				}
				if(t instanceof Integer && null == t) {
					return true;
				}
				if(null == t) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * @param user
	 * 进行默认值的赋值
	 */
	private void doDefeuleValue(User user) {
		Date creationTime = user.getCreationTime();
		   if(creationTime== null) {
			   user.setCreationTime(new Date());
		   }
		   Date modifyTime = user.getModifyTime();
		   if(modifyTime== null) {
			   user.setModifyTime(new Date());
		   }
		   Integer isDeleted = user.getIsDeleted();
		   if(isDeleted== null) {
			   user.setIsDeleted(BaseSaveConstant.DEFAULT_IS_DELETED_VALUE);
		   }
		   Integer eventSource = user.getEventSource();
		   if(eventSource== null) {
			   user.setEventSource(getDefaultSource());
		   }
	}
	
	//同步passport
	public void syncToPassport(List<User> urs) {
		if (!Evn.isPassport())
			return;
		List<String> accountIds = EntityUtils.getList(urs, User::getAccountId);
		Map<String, User> userMap = EntityUtils.getMap(urs, User::getAccountId);
		Account[] accounts = null;
		try {
			accounts = PassportClient.getInstance().queryAccounts(accountIds.toArray(new String[0]));
		} catch (PassportException e1) {
			log.error("passport根据accountid查询已经账号异常"+e1.getMessage(), e1);
			return;
		}
		List<Account> updateAccounts = new ArrayList<>();
		Set<String> deleteAccountIds = new HashSet<>();
		for (Account a : accounts) {
			if (a != null) {
				User user = userMap.get(a.getId());
				getAccountByUser(user,a);
				updateAccounts.add(a);
				if(user.getIsDeleted() == 1){
					deleteAccountIds.add(user.getAccountId());
				}
			}
		}
		List<String> returnedAccountIds = EntityUtils.getList(updateAccounts, Account::getId);
		@SuppressWarnings("unchecked")
		List<String> remainedAccountIds = ListUtils.subtract(accountIds, returnedAccountIds);
		List<Account> as2 = new ArrayList<>();
		List<String> an2 = new ArrayList<>();

		for (String accountId : remainedAccountIds) {
			User user = userMap.get(accountId);
			Account account = new Account();
			getAccountByUser(user, account);
			an2.add(user.getUsername());
			as2.add(account);
		}
		
		Set<String> names = new HashSet<>();
		try {
			names = PassportClient.getInstance().queryExistsAccountUsernames(an2.toArray(new String[0]));
		} catch (PassportException e1) {
			log.error("passport获取已经存在的账号异常"+e1.getMessage(), e1);
			return;
		}
		// PassportClient.getInstance().
		List<Account> as22 = new ArrayList<>();
		for (Account at : as2) {
			if (names.contains(at.getUsername())) {
				log.error(at.getUsername() + "已经存在");
				continue;
			}
			as22.add(at);
		}
		Account[] ats = null;
		try {
			ats = PassportClient.getInstance().addAccounts(as22.toArray(new Account[0]));
		} catch (Exception e) {
			log.error("同步passport信息失败"+e.getMessage(), e);
		}
		List<User> sequenceUser = new ArrayList<>();
		if(ats != null) {
			for (Account at : ats) {
				if (at == null) {
					continue;
				}
				String accountId = at.getId();
				User u = userMap.get(accountId);
				if (u != null && (u.getSequence() == null || u.getSequence() == 0)) {
					u.setSequence(at.getSequence());
					sequenceUser.add(u);
				}
			}
			if (CollectionUtils.isNotEmpty(sequenceUser)) {
				userRemoteService.saveAll(SUtils.s(sequenceUser.toArray(new User[0])));
			}
		}
		
		//更新passport数据
		if(CollectionUtils.isNotEmpty(updateAccounts)){
			try {
				PassportClient.getInstance().modifyAccounts(updateAccounts.toArray(new Account[updateAccounts.size()]),
						ArrayUtils.toArray("phone","realName","sex","password"));
				
			} catch (PassportException e) {
				log.error("更新passport信息失败"+e.getMessage(), e);
			}
		}
		//删除passport的用户数据
		if(CollectionUtils.isNotEmpty(deleteAccountIds)){
			 try {
				PassportClient.getInstance().deleteAccounts(deleteAccountIds.toArray(new String[deleteAccountIds.size()]));
			} catch (PassportException e) {
				log.error("删除 passport 用户信息失败"+e.getMessage(), e);
			}
		}
	}

	private void getAccountByUser(User user, Account account) {
		account.setUsername(user.getUsername());
		account.setRealName(user.getRealName());
		account.setSex(user.getSex());
		account.setId(user.getAccountId());
//			account.setModifyTime(new Date());   加上这个字段，测试那边的环境可能会一直卡在这个地方，不会往下执行
		account.setPassword(user.getPassword());
		String mobilePhone = user.getMobilePhone();
		if(StringUtils.isNotBlank(mobilePhone)){
			account.setPhone(mobilePhone);
		}
		account.setFixedType(Optional.of(user.getOwnerType()).orElse(0));
		account.setState(Account.STATE_OK);
		if(user.getOwnerType()== User.OWNER_TYPE_TEACHER) {
			account.setType(Account.TYPE_TEACHER);
		}else if(user.getOwnerType() == User.OWNER_TYPE_STUDENT) {
			account.setType(Account.TYPE_STUDENT);
		}else if(user.getOwnerType() == User.OWNER_TYPE_FAMILY){
			account.setType(Account.TYPE_PARENT);
		}
	}
	
	@Override
	public void saveSubSchool(SubSchool[] subSchools) {
		saveSubSchool(subSchools,null);
	}
	
	@Override
	public void saveSubSchool(SubSchool[] subSchools, Map<String, String> returnMsg) {
		try {
			List<SubSchool> saveSubSchools = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (SubSchool ss : subSchools) {
			    if(ss.getUpdatestamp() == null)
				   ss.setUpdatestamp(new Date().getTime());
			    if(ss.getIsDeleted() == null)
				   ss.setIsDeleted(0);
				Set<String> idList = new HashSet<>();
				List<Object> paramList = new ArrayList<>(); 
				//先判断数据库中非空的字段是否满足条件   
				String id = ss.getId();
				String name = ss.getName();
				String schoolId = ss.getSchoolId();
				long updatestamp = ss.getUpdatestamp();
				//判断关键的字段是否不为空
//				String address = ss.getAddress();
				Integer isDeleted = ss.getIsDeleted();
				idList.add(id);
				idList.add(schoolId);
				paramList.add(updatestamp);
//				paramList.add(address);
				paramList.add(isDeleted);
				paramList.add(name);
				if(doJudgeField(paramList,idList.toArray(new String[idList.size()]))){
					errorMsg.append(id +",");
					continue;
				}
				saveSubSchools.add(ss);
			}
			getReturnMsg(returnMsg, errorMsg);
			System.out.println("保存校区数据是：-------------------" + saveSubSchools.size());
			if(CollectionUtils.isNotEmpty(saveSubSchools)) {
				subSchoolRemoteService.saveAll(SUtils.s(saveSubSchools.toArray(new SubSchool[0])));
			}
		} catch (Exception e) {
			log.error("保存校区数据失败"+ e.getMessage(), e);
		}
	}

	@Override
	public void saveSemester(Semester[] semesters) {
		saveSemester(semesters, null);
	}
	
	@Override
	public void saveSemester(Semester[] semesters, Map<String, String> returnMsg) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			List<Semester> saveSemesters = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (Semester semester : semesters) {
			   if(semester.getEduDays() == null)
				   semester.setEduDays(7);
			   if(semester.getAmPeriods() == null)
				   semester.setAmPeriods(4);
			   if(semester.getPmPeriods() == null)
				   semester.setPmPeriods(4);
			   if(semester.getNightPeriods() == null)
				   semester.setNightPeriods(0);
			   if(semester.getEventSource() == null)
				   semester.setEventSource(getDefaultSource());
			   if(semester.getClassHour() == null)
				   semester.setClassHour(45);
			   if(semester.getMornPeriods() == null)
				   semester.setMornPeriods(0);
			   if(semester.getCreationTime() == null)
				   semester.setCreationTime(new Date());
			   if(semester.getModifyTime() == null)
				   semester.setModifyTime(new Date());
			   if(semester.getIsDeleted() == null)
				   semester.setIsDeleted(0);
			   List<Object> paramList = new ArrayList<>(); 
			   //数据库中非空的字段
			   String id = semester.getId();
			   Date workBegin = semester.getWorkBegin();
			   Date workEnd = semester.getWorkEnd();
			   Date semesterBegin = semester.getSemesterBegin();
			   Date semesterEnd = semester.getSemesterEnd();
			   Date modifyTime = semester.getModifyTime();
			   Integer isDeleted = semester.getIsDeleted();
			   String  ac = semester.getAcadyear();
			   Integer se = semester.getSemester();
			   //主要的字段非空
			   paramList.add(ac);
			   paramList.add(se);
			   paramList.add(modifyTime);
			   paramList.add(isDeleted);
			   if(doJudgeField(paramList,id)){
				    errorMsg.append(id +",");
					continue;
			   }
			   String start =ac.substring(0, ac.indexOf("-"));
			   String end = ac.substring(ac.indexOf("-")+1);
			   Date workBegin1,workEnd1,semesterBegin1,semesterEnd1;
			   if(se == 1) {
				   workBegin1 = sdf.parse(start+"0820");
				   workEnd1 = sdf.parse(end+"0120");
				   semesterBegin1 = sdf.parse(start+"0901");
				   semesterEnd1 = sdf.parse(end+"0115");
			   }else {
				   workBegin1 = sdf.parse(end+"0220");
				   workEnd1 = sdf.parse(end+"0720");
				   semesterBegin1 = sdf.parse(end+"0301");
				   semesterEnd1 = sdf.parse(end+"0710");
			   }
			   if(workBegin == null)
				   semester.setWorkBegin(workBegin1);
			   if(workEnd == null)
				   semester.setWorkEnd(workEnd1);
			   if(semesterBegin == null)
				   semester.setSemesterBegin(semesterBegin1);
			   if(semesterEnd == null)
				   semester.setSemesterEnd(semesterEnd1);
			   semester.setRegisterDate(semester.getSemesterBegin());
			   saveSemesters.add(semester);
			}
			getReturnMsg(returnMsg, errorMsg);
			if(CollectionUtils.isNotEmpty(saveSemesters)) {
				semesterRemoteService.saveAll(SUtils.s(saveSemesters.toArray(new Semester[0])));
			}
		} catch (Exception e) {
			log.error("保存学年学期数据失败"+ e.getMessage(), e);
		}
	}
	
	@Override
	public void saveSchoolSemester(SchoolSemester[] schoolSemesters) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			List<SchoolSemester> saveSchoolSemesters = new ArrayList<>();
			for (SchoolSemester schoolSemester : schoolSemesters) {
			   if(schoolSemester.getEduDays() == null)
				   schoolSemester.setEduDays(7);
			   if(schoolSemester.getAmPeriods() == null)
				   schoolSemester.setAmPeriods(4);
			   if(schoolSemester.getPmPeriods() == null)
				   schoolSemester.setPmPeriods(4);
			   if(schoolSemester.getNightPeriods() == null)
				   schoolSemester.setNightPeriods(0);
			   if(schoolSemester.getEventSource() == null)
				   schoolSemester.setEventSource(getDefaultSource());
			   if(schoolSemester.getClassHour() == null)
				   schoolSemester.setClassHour(45);
			   if(schoolSemester.getCreationTime() == null)
				   schoolSemester.setCreationTime(new Date());
			   if(schoolSemester.getModifyTime() == null)
				   schoolSemester.setModifyTime(new Date());
			   if(schoolSemester.getIsDeleted() == null)
				   schoolSemester.setIsDeleted(0);
			   if(schoolSemester.getUpdatestamp() == null)
				   schoolSemester.setUpdatestamp(new Date().getTime());;
			   List<Object> paramList = new ArrayList<>(); 
			   Set<String> idList = new HashSet<>();
			   //数据库中非空的字段
			   String id = schoolSemester.getId();
			   String schoolId = schoolSemester.getSchoolId();
			   String  ac = schoolSemester.getAcadyear();
			   Integer se = schoolSemester.getSemester();
			   Date workBegin = schoolSemester.getWorkBegin();
			   Date workEnd = schoolSemester.getWorkEnd();
			   Date semesterBegin = schoolSemester.getSemesterBegin();
			   Date semesterEnd = schoolSemester.getSemesterEnd();
			   Date registerDate = schoolSemester.getRegisterDate();
			   //主要的字段非空
			   paramList.add(ac);
			   paramList.add(se);
			   idList.add(schoolId);
			   if(doJudgeField(paramList,idList.toArray(new String[idList.size()]))){
//					errorMsg.append(id +",");
					log.error("学校学期中有关键的字段为空-----" + id);
					continue;
				}
			   String start =ac.substring(0, ac.indexOf("-"));
			   String end = ac.substring(ac.indexOf("-")+1);
			   Date workBegin1,workEnd1,semesterBegin1,semesterEnd1,registerDate1;
			   if(se == 1) {
				   workBegin1 = sdf.parse(start+"0820");
				   workEnd1 = sdf.parse(end+"0120");
				   semesterBegin1 = sdf.parse(start+"0901");
				   semesterEnd1 = sdf.parse(end+"0115");
				   registerDate1 = sdf.parse(start+"0901");
			   }else {
				   workBegin1 = sdf.parse(end+"0220");
				   workEnd1 = sdf.parse(end+"0720");
				   semesterBegin1 = sdf.parse(end+"0301");
				   semesterEnd1 = sdf.parse(end+"0710");
				   registerDate1 = sdf.parse(end+"0301");
			   }
			   if(workBegin == null)
				   schoolSemester.setWorkBegin(workBegin1);
			   if(workEnd == null)
				   schoolSemester.setWorkEnd(workEnd1);
			   if(semesterBegin == null)
				   schoolSemester.setSemesterBegin(semesterBegin1);
			   if(semesterEnd == null)
				   schoolSemester.setSemesterEnd(semesterEnd1);
			   if(registerDate == null)
				   schoolSemester.setRegisterDate(registerDate1);
			   saveSchoolSemesters.add(schoolSemester);
			}
			System.out.println("保存学校学期信息的数据是：-------------------" + saveSchoolSemesters.size());
			if(CollectionUtils.isNotEmpty(saveSchoolSemesters)) {
				schoolSemesterRemoteService.saveAll(SUtils.s(saveSchoolSemesters.toArray(new SchoolSemester[0])));
			}
		} catch (Exception e) {
			log.error("保存学校学期信息数据失败"+ e.getMessage(), e);
		}
	}
	

	@Override
	public void saveCourse(Course[] courses) {
		saveCourse(courses, null);
	}

	@Override
	public void saveCourse(Course[] courses,Map<String, String> returnMsg) {
		try {
			List<Course> saveCourses = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (int j = 0; j < courses.length; j++) {
			   List<Object> paramList = new ArrayList<>(); 
			   Course course = courses[j];
			   if(course.getIsUsing() == null)
				   course.setIsUsing(Course.TRUE_1);
			   if(course.getIsDeleted() == null)
				   course.setIsDeleted(getDefaultIsDeleted());
			   if(course.getEventSource() == null)
				   course.setEventSource(getDefaultSource());
			   if(course.getType() == null)
				   course.setType(BaseConstants.SUBJECT_TYPE_BX);
			   if(course.getCreationTime() == null)
				   course.setCreationTime(new Date());
			   if(course.getModifyTime() == null)
				   course.setModifyTime(new Date());
			   if(StringUtils.isBlank(course.getSubjectCode()))
				   course.setSubjectCode("S001");
			   //数据库中非空的字段
			   String id = course.getId();
			   if(StringUtils.isBlank(id)) {
				   id = UuidUtils.generateUuid();
				   course.setId(id);
			   }
			   String subjectCode = course.getSubjectCode();
			   String subjectName = course.getSubjectName();
			   String unitId = course.getUnitId();
			   //主要的字段非空
			   paramList.add(subjectCode);
			   paramList.add(subjectName);
			   if(doJudgeField(paramList,unitId)){
				    errorMsg.append(id +",");
				    log.error("学生中有关键的字段为空-----");
				    continue;
			   }
			   saveCourses.add(course);
			}
			getReturnMsg(returnMsg, errorMsg);
			System.out.println("保存课程信息数据是：-------------------" + saveCourses.size());
			if(CollectionUtils.isNotEmpty(saveCourses)) {
				courseRemoteService.saveAll(saveCourses.toArray(new Course[0]));
			}
		} catch (Exception e) {
			log.error("保存课程信息数据失败"+ e.getMessage(), e);
		}
	}

	@Override
	public void saveClassTeaching(ClassTeaching[] classTeachings) {
		saveClassTeaching(classTeachings, null);
	}
	
	@Override
	public void saveClassTeaching(ClassTeaching[] classTeachings,Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		try {
			List<ClassTeaching> saveClassTeachings = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (int j = 0; j < classTeachings.length; j++) {
			   Set<String> idList = new HashSet<>();
			   ClassTeaching classTeaching = classTeachings[j];
			   if(classTeaching.getIsDeleted() == null)
				   classTeaching.setIsDeleted(getDefaultIsDeleted());
			   if(classTeaching.getEventSource() == null)
				   classTeaching.setEventSource(getDefaultSource());
			   if(classTeaching.getCreationTime() == null)
				   classTeaching.setCreationTime(new Date());
			   if(classTeaching.getModifyTime() == null)
				   classTeaching.setModifyTime(new Date());
			   if(StringUtils.isBlank(classTeaching.getSubjectType()))
				   classTeaching.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
			   //数据库中非空的字段
			   String id = classTeaching.getId();
			   String unitId = classTeaching.getUnitId();
			   String classId = classTeaching.getClassId();
			   String subjectId = classTeaching.getSubjectId();
			   //主要的字段非空
			   String teacherId = classTeaching.getTeacherId();
			   idList.add(id);
			   idList.add(unitId);
			   idList.add(classId);
			   idList.add(subjectId);
			   idList.add(teacherId);
			   if(doJudgeField(null,idList.toArray(new String[idList.size()]))){
				    errorMsg.append(id +",");
				    log.error("教师任课中有关键的字段为空-----");
				    continue;
			   }
			   saveClassTeachings.add(classTeaching);
			}
			getReturnMsg(returnMsg, errorMsg);
			if(CollectionUtils.isNotEmpty(saveClassTeachings)) {
				classTeachingRemoteService.saveAll(saveClassTeachings.toArray(new ClassTeaching[0]));
			}
		} catch (Exception e) {
			log.error("保存教师任课信息数据失败"+ e.getMessage(), e);
		}
	}
	
	@Override
	public void saveStusysSectionTimeSet(StusysSectionTimeSet[] times) {
		// TODO Auto-generated method stub
		try {
			List<StusysSectionTimeSet> saveStusysSectionTimeSets = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			Semester se = getCurrentSemester();
			for (int j = 0; j < times.length; j++) {
			   Set<String> idList = new HashSet<>();
			   List<Object> paramList = new ArrayList<>(); 
			   StusysSectionTimeSet time = times[j];
			   if(time.getIsDeleted() == null)
				   time.setIsDeleted(getDefaultIsDeleted());
			   if(StringUtils.isBlank(time.getUserId()))
				   time.setUserId(UuidUtils.generateUuid());
			   if(StringUtils.isBlank(time.getAcadyear()))
				   time.setAcadyear(se.getAcadyear());
			   if(time.getSemester() == null)
				   time.setSemester(se.getSemester());
			   if(time.getSectionNumber() == null)
				   time.setSectionNumber(1);
			   //数据库中非空的字段
			   String id = time.getId();
			   String unitId = time.getUnitId();
			   String userId = time.getUserId();
			   Integer sectionNumber = time.getSectionNumber();
			   Integer semester = time.getSemester();
			   String acadyear = time.getAcadyear();
			   //主要的字段非空
			   idList.add(id);
			   idList.add(unitId);
			   idList.add(userId);
			   paramList.add(sectionNumber);
			   paramList.add(semester);
			   paramList.add(acadyear);
			   if(doJudgeField(paramList,idList.toArray(new String[idList.size()]))){
				    errorMsg.append(id +",");
				    log.error("节次时间中有关键的字段为空-----");
				    continue;
			   }
			   saveStusysSectionTimeSets.add(time);
			}
//			getReturnMsg(returnMsg, errorMsg);
			System.out.println("保存节次时间信息数据是：-------------------" + saveStusysSectionTimeSets.size());
			if(CollectionUtils.isNotEmpty(saveStusysSectionTimeSets)) {
				stusysSectionTimeSetRemoteService.saveAll(saveStusysSectionTimeSets.toArray(new StusysSectionTimeSet[0]));
			}
		} catch (Exception e) {
			log.error("保存节次时间信息数据失败"+ e.getMessage(), e);
		}
		
	}
	
	@Override
	public void saveUnitAndServerExtension(Map<String, String> serverUMap) {
		// TODO Auto-generated method stub
		Set<String> unitSet = new HashSet<>();
		serverUMap.forEach((k,v)->{
			unitSet.add(k);
		});
		List<UnitExtension> allExtensions = unitExtensionRemoteService.findByUnitIdIn(unitSet.toArray(new String[unitSet.size()]));
		Map<String, UnitExtension> unitIdUMap = EntityUtils.getMap(allExtensions, UnitExtension::getUnitId);
//		List<ServerExtension> allServerExtensions = serverExtensionRemoteService.findByUnitIdIn(unitSet.toArray(new String[unitSet.size()]));
//		Map<String, List<ServerExtension>> unitIdSMap = EntityUtils.getListMap(allServerExtensions, ServerExtension::getUnitId,
//				Function.identity());
		List<UnitExtension> saveUnitExtensions = new ArrayList<UnitExtension>();
		List<ServerExtension> saveServerExtensions = new ArrayList<ServerExtension>();
		serverUMap.forEach((k,v)->{
			UnitExtension unitExtension;
			if(MapUtils.isEmpty(unitIdUMap) || unitIdUMap.get(k) == null){
				unitExtension = new UnitExtension();
				unitExtension.setId(UuidUtils.generateUuid());
				unitExtension.setUnitId(k);
				unitExtension.setUsingNature(UnitExtensionNature.OFFICIAL);
				unitExtension.setUsingState(UnitExtensionState.NORMAL);
				unitExtension.setExpireTimeType(UnitExtensionExpireType.PERMANENT);
				saveUnitExtensions.add(unitExtension);
			}
			List<ServerExtension> serverExtensions = JSONArray.parseArray(v, ServerExtension.class);
			unitExtension = new UnitExtension();
			serverExtensions.forEach(c->{
				c.setId(UuidUtils.generateUuid());
				c.setUnitId(k);
				c.setUsingNature(ServerExtension.NATURE_OFFICIAL);
				c.setUsingState(ServerExtension.STATE_NORMAL);
			});
			saveServerExtensions.addAll(serverExtensions);
		});
		if(CollectionUtils.isNotEmpty(saveUnitExtensions)){
			unitExtensionRemoteService.saveAll(saveUnitExtensions);
		}
		if(CollectionUtils.isNotEmpty(saveServerExtensions)){
			serverExtensionRemoteService.deleteByUnitIdIn(unitSet.toArray(new String[unitSet.size()]));
			serverExtensionRemoteService.saveAll(saveServerExtensions);
		}
	}
	
	
	
	//------------------------私有方法区 -----------------------------------------------------------
	private void getReturnMsg(Map<String, String> returnMsg, int errorNum,
			StringBuilder errorMsg) {
		if(returnMsg != null) {
			returnMsg.put(BaseSaveConstant.PROVING_BASE_SAVE_KEY, String.valueOf(errorNum));
			if(errorNum > 0){
				returnMsg.put(BaseSaveConstant.PROVING_BASE_SAVE_ERROR_KEY, errorMsg.toString());
			}
		}
	}
	private void getReturnMsg(Map<String, String> returnMsg, StringBuilder errorMsg) {
		int errorNum = 0;
		if(!MapUtils.isEmpty(returnMsg)) {
			if(StringUtils.isNotBlank(errorMsg.toString())){
				String[] errorIds = errorMsg.toString().split(",");
				errorNum = errorIds.length;
			}
		}
		getReturnMsg(returnMsg,errorNum,errorMsg);
	}
	
	//----------------------------字段的验证代码
    private boolean doJudgeField(List<Object> paramList, String id) {
    	return doJudgeField(paramList, null, id);
	}
	private boolean doJudgeField(List<Object> paramList, String[] id) {
		return doJudgeField(paramList, null, id);
	}
	private boolean doJudgeField(List<Object> paramList,String regionCode, String... id) {
		boolean isDo = Boolean.FALSE;
		if(validateId(id) ||  validateNoEmpty(paramList)) {
			isDo = Boolean.TRUE;
		}
		if(StringUtils.isNotBlank(regionCode)){
			if(isDo || validateRegionCode(regionCode)){
				isDo = Boolean.TRUE;
			}
		}
		return isDo;
	}
	
	//----------------------------初始化的信息代码
	private Integer getDefaultIsDeleted (){
		return BaseSaveConstant.DEFAULT_IS_DELETED_VALUE;
	}
	
	private Integer getDefaultSource (){
		return BaseSaveConstant.DEFAULT_EVENT_SOURCE_VALUE;
	}

	@Override
	public void saveUserToPassport(List<User> urs) {
		// TODO Auto-generated method stub
		syncToPassport(urs);
	}
	
	/**
	 * 获取当前的学年学期
	 * @return
	 */
	private Semester getCurrentSemester() {
		String remoteSemester= "kade.remote.semester";
		Semester semester = Semester
				.dc(RedisUtils.get(remoteSemester, new RedisInterface<String>() {
					@Override
					public String queryData() {
						return semesterRemoteService.getCurrentSemester(SemesterRemoteService.RET_PRE);
					}
				}));
		return semester;
	}
}
























