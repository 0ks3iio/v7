package net.zdsoft.syncdata.custom.longyou.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.EduInfo;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.SchtypeSection;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.SubSchool;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.entity.UserDept;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.EduInfoRemoteService;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.SchtypeSectionRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.SubSchoolRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.syncdata.custom.base.service.impl.BaseCustomServiceImpl;
import net.zdsoft.syncdata.custom.longyou.service.LYdySyncService;
import net.zdsoft.syncdata.entity.DYClazz;
import net.zdsoft.syncdata.entity.DYGrade;
import net.zdsoft.syncdata.service.DYClassService;
import net.zdsoft.syncdata.service.DYGradeService;
import net.zdsoft.syncdata.service.XFStudentExService;
import net.zdsoft.syncdata.util.DYSynHttpRequestUtils;
import net.zdsoft.syncdata.util.JledqSyncDataUtil;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
@Service("lydySyncService")
public class LYdySyncServiceImpl extends BaseCustomServiceImpl implements LYdySyncService {
	private Logger log = LoggerFactory.getLogger(LYdySyncServiceImpl.class);
	@Autowired
	private DYGradeService dyGradeService;
	@Autowired
	private DYClassService dyClassService;
	@Autowired
	private SubSchoolRemoteService subSchoolRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private SchtypeSectionRemoteService schtypeSectionRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private XFStudentExService xfStudentExService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private EduInfoRemoteService eduInfoRemoteService;
//	@Autowired
//	private UserDeptService userDeptService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;

	private int maxCount = 5;

	public void saveClass(String interfaceName, String apiCode, String mark,String unitId) throws Exception {
		System.out.println("------- 开始同步" + mark + "班级");
		log.info("------- 开始同步" + mark + "班级");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.class." + mark + unitId + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//先获取年级代码的数据
		String gradeBase = DYSynHttpRequestUtils.doInterface("BASENJDM", "c293f32d-e168-4729-91a0-7bd58e874d3d", null);
		Map<String, JSONObject> baseMap = new HashMap<String, JSONObject>();
		if(StringUtils.isNotBlank(gradeBase)) {
			JSONArray array = Json.parseArray(gradeBase);
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				if(js.getString("NJDM").charAt(0) == '1' ) {
					js.put("NJMC", "小"+(js.getString("NJMC").substring(0, 1)));
				}
				baseMap.put(js.getString("XX_NJDMB_ID"), js);
			}
		}
		//获取学段的信息  
		String sectionBase = DYSynHttpRequestUtils.doInterface("BASEXD", "2cdbecff-40b7-4847-8977-a96c991f15c7", null);
		Map<String, JSONObject> sectionMap = new HashMap<String, JSONObject>();
		if(StringUtils.isNotBlank(sectionBase)) {
			JSONArray array = Json.parseArray(sectionBase);
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				sectionMap.put(js.getString("XX_XDXX_ID"), js);
			}
		}
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);
			json.put("XX_JBXX_ID", unitId);
			String cc = null;
			Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0), Semester.class);
			if (semester == null) {
				log.info("------ 请先维护学年学期 -------");
				return;
			}
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
				int count = 0;
				while (StringUtils.equals("ERROR", cc) && count < maxCount) {
					count++;
					log.info("-------获取不到数据，第" + count + "次重新获取");
				}
				if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
					RedisUtils.set("syncdata.lydy.class." + mark + ".time", time);
					log.info("------- 同步班级结束");
					System.out.println("------- 同步班级" + mark + "结束");
					return;
				}
				JSONArray array = Json.parseArray(cc);
				//先得到对应的idList
				Set<String> schoolIds = new HashSet<>();
				Set<String> teacherIds = new HashSet<>();
				Set<String> clazzIds = new HashSet<>();
				Set<String> teachAreaIds = new HashSet<>();
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String clazzId ;
					String schoolId ;
					String xqid = null;
					if("XQ".equals(interfaceName)) {
						clazzId = js.getString(mark+"_XX_BJXX_ID").replaceAll("-", "");
						schoolId = js.getString(mark + "_XX_JBXX_ID").replaceAll("-", "");
					}else {
						clazzId = js.getString("XX_BJXX_ID").replaceAll("-", "");
						schoolId = js.getString("XX_JBXX_ID").replaceAll("-", "");
						xqid = js.getString("XX_XQXX_ID").replaceAll("-", "");
					}
					String bzrid = js.getString("BZRID");
					if(StringUtils.isNotBlank(bzrid)) {
						bzrid = bzrid.replaceAll("-", "");
						teacherIds.add(bzrid);
					}
					schoolIds.add(schoolId);
					clazzIds.add(clazzId);
					teachAreaIds.add(xqid);
				}
				Map<String, SubSchool> subSchoolMap = new HashMap<String, SubSchool>();
				if("XCGBJ".equals(interfaceName)) {
					List<SubSchool> subSchools = SUtils.dt(subSchoolRemoteService.findListByIds(teachAreaIds.toArray(new String[teachAreaIds.size()])), new TR<List<SubSchool>>() {
					});
					subSchoolMap = EntityUtils.getMap(subSchools, SubSchool::getId);
				}
				Map<String, Teacher> teacherMap = new HashMap<>();
				if(CollectionUtils.isNotEmpty(teacherIds)) {
					List<Teacher> teachers = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[teacherIds.size()])),
							new TR<List<Teacher>>() {});
					teacherMap = EntityUtils.getMap(teachers, Teacher::getId);
				}
				List<Clazz> allClazz = SUtils.dt(classRemoteService.findListByIds(clazzIds.toArray(new String[clazzIds.size()])),new TR<List<Clazz>>() {});
				Map<String, Clazz> allClazzMap = EntityUtils.getMap(allClazz, Clazz::getId);
				List<School> allSchool = SUtils.dt(schoolRemoteService.findListByIds(schoolIds.toArray(new String[schoolIds.size()])),new TR<List<School>>() {});
				Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getId);
				log.info("------------开始检索， " + mark + "班级总数：" + array.size());
				sdf.applyPattern("yyyyMMddHHmmss");
				List<Clazz> classes = new ArrayList<Clazz>();
//				List<DYClazz> dyClasses = new ArrayList<DYClazz>();
				List<Grade> grades = new ArrayList<Grade>();
				Map<String, Grade> gradeMap = new HashMap<String, Grade>();
				int invalidate = 0;
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String gxsj = js.getString("GXSJ");
					if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
						time = gxsj;
					}
					String clazzId ;
					String schoolId ;
					if("XQ".equals(interfaceName)) {
						clazzId = js.getString(mark+"_XX_BJXX_ID").replaceAll("-", "");
						schoolId = js.getString(mark + "_XX_JBXX_ID").replaceAll("-", "");
					}else {
						clazzId = js.getString("XX_BJXX_ID").replaceAll("-", "");
						schoolId = js.getString("XX_JBXX_ID").replaceAll("-", "");
					}
					Clazz clazz;
					if(MapUtils.isNotEmpty(allClazzMap) && allClazzMap.get(clazzId) != null) {
						clazz = allClazzMap.get(clazzId);
					}else {
						clazz = new Clazz();
						clazz.setId(clazzId);
						clazz.setCreationTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
					}
					String className = js.getString("BJMC");
					clazz.setClassName(className);
					clazz.setModifyTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
					//班级的软删的问题，同一个班级可能出现软删的问题
					clazz.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
					
					// 根据对方的班主任Id获取教师，设置教师Id
					String bzrid = js.getString("BZRID");
					if(StringUtils.isNotBlank(bzrid) && teacherMap.get(js.getString("BZRID").replaceAll("-", "")) != null) {
						clazz.setTeacherId(teacherMap.get(js.getString("BZRID").replaceAll("-", "")).getId());
					}else {
						clazz.setTeacherId("");
					}
					if(MapUtils.isNotEmpty(allSchoolMap) && allSchoolMap.get(schoolId) == null) {
						invalidate++;
						log.info("班级的"+clazzId + "的学校不存在");
						continue;
					}
					clazz.setSchoolId(schoolId);
					if(MapUtils.isNotEmpty(subSchoolMap) && subSchoolMap.get(js.getString("XX_XQXX_ID").replaceAll("-", "")) == null) {
						invalidate++;
						log.info("班级的"+clazzId+ "的校区不存在"+"----XX_XQXX_ID");
						continue;
					}
					if("XCGBJ".equals(interfaceName)){
						clazz.setCampusId(StringUtils.isBlank(js.getString("XX_XQXX_ID"))? null : js.getString("XX_XQXX_ID").replaceAll("-", ""));
					}
					String gradeCodeId = js.getString("XX_NJDMB_ID");
					if(StringUtils.isBlank(gradeCodeId) ) {
						invalidate++;
						log.info("班级的"+clazzId+ "的年级代码不存在"+"----XX_NJDMB_ID");
						continue;
					}
					String njdmbId = js.getString("XX_NJDMB_ID");
					JSONObject njdmbObject = baseMap.get(njdmbId);
					//得到年级code 
					String gradeCode = njdmbObject.getString("NJDM");
					if(gradeCode.length() == 1) {
						gradeCode = "0" + gradeCode;
					}
					//得到年纪名称
					String gradeName = njdmbObject.getString("NJMC");
					//转化学段
					String sectionId = njdmbObject.getString("XX_XDXX_ID");
					JSONObject sectionObject = sectionMap.get(sectionId);
					String section = sectionObject.getString("XDDM");
					if(section.equals("4")) {
						section = "0";  //转化成幼儿园
					}
					clazz.setSection(Integer.valueOf(section));
					//转化学制
					Integer xz = NumberUtils.toInt(js.getString("XZ"));
					if (xz == null || xz == 0) {
						xz = (clazz.getSection() == 1 ? 6 : 3);
					}
					clazz.setSchoolingLength(xz);
//					clazz.setAcadyear(js.getString("XNMC"));
					//转化学年学期
					String startAcadyear, endAcadyear;
					int num = Integer.valueOf(gradeCode.substring(1)) - 1 ;
					startAcadyear =String.valueOf( NumberUtils.toInt(semester.getAcadyear().substring(0, 4)) - num);
				    endAcadyear = String.valueOf( NumberUtils.toInt(semester.getAcadyear().substring(5)) - num );
				    String  acadyear =  startAcadyear + "-" + endAcadyear;
				    clazz.setAcadyear(acadyear);
					//班号我们自己生成
					String classCodePrefix = StringUtils.substring(clazz.getAcadyear(), 0, 4)
							+ StringUtils.leftPad(clazz.getSection() + "", 2, "0");
					Long max = RedisUtils
							.incrby("syncdata.lydy.class.code.max." + clazz.getSchoolId() + classCodePrefix, 1);
					clazz.setClassCode(classCodePrefix + StringUtils.leftPad("" + max, 2, "0"));
					
					String year = StringUtils.substring(clazz.getAcadyear(), 0, 4);
					String year2 = StringUtils.substring(semester.getAcadyear(), 0, 4);
					if (NumberUtils.toInt(year2) >= (NumberUtils.toInt(year) + clazz.getSchoolingLength())) {
						clazz.setIsGraduate(1);
						clazz.setGraduateDate(new Date());
					} else {
						clazz.setIsGraduate(0);
					}
					clazz.setClassType(js.getString("BJLXDM"));
					//进行id的匹配
//					DYClazz dyClazz = new DYClazz();
//					dyClazz.setId(clazzId);
//					dyClazz.setFixClassId(clazz.getId());
//					dyClasses.add(dyClazz);

//					String gradesJson = gradeRemoteService.findBySchoolId(clazz.getSchoolId(),
//							ArrayUtils.toArray(clazz.getSection()), clazz.getAcadyear(), true);
//					List<Grade> gradesExists = SUtils.dt(gradesJson, new TypeReference<List<Grade>>() {
//					});
					List<Grade> gradesExists = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(clazz.getSchoolId(),gradeCode), new TypeReference<List<Grade>>() {});
					for (Grade grade1 : gradesExists) {
						String key = grade1.getSchoolId() + grade1.getGradeCode() + grade1.getSection();
						gradeMap.put(key, grade1);
					}
					//年级名称的转化
					if("XCGBJ".equals(interfaceName)){
						String areaName = subSchoolMap.get(clazz.getCampusId()).getName();
						gradeName = "("+areaName+")"+gradeName;
					}
					Grade grade1 = gradeMap.get(clazz.getSchoolId() + gradeCode + clazz.getSection());
					if (grade1 == null) {
						grade1 = new Grade();
						grade1.setGradeName(gradeName);
						grade1.setCreationTime(sdf.parse(gxsj));
						grade1.setModifyTime(sdf.parse(gxsj));
						grade1.setEventSource(0);
						grade1.setAmLessonCount(4);
						grade1.setPmLessonCount(4);
						grade1.setOpenAcadyear(clazz.getAcadyear());
						grade1.setNightLessonCount(3);
						grade1.setGradeCode(gradeCode);
						grade1.setSubschoolId(clazz.getCampusId());
						grade1.setSchoolId(clazz.getSchoolId());
						grade1.setIsGraduate(0);
						grade1.setSection(clazz.getSection());
						grade1.setSchoolingLength(js.getInteger("XZ"));
						if (grade1.getSchoolingLength() == null || grade1.getSchoolingLength() == 0) {
							grade1.setSchoolingLength(grade1.getSection() == 1 ? 6 : 3);
						}
						grade1.setId(UuidUtils.generateUuid());
						grades.add(grade1);
						gradeMap.put(grade1.getSchoolId() + gradeCode + grade1.getSection(), grade1);
					}
					clazz.setGradeId(grade1.getId());
					classes.add(clazz);
				}
				log.info("------- 更新" + classes.size() + "个" + mark + "班级数据，不符合条件" + invalidate + "个 -------");
				//进行保存数据
				if(CollectionUtils.isNotEmpty(classes)) {
					baseSyncSaveService.saveClass(classes.toArray(new Clazz[classes.size()]));
				}
				if (CollectionUtils.isNotEmpty(grades)) {
					baseSyncSaveService.saveGrade(grades.toArray(new Grade[grades.size()]));
				}
//				dyClassService.saveAll(dyClasses.toArray(new DYClazz[0]));
			    RedisUtils.set("syncdata.lydy.class." + mark + unitId + ".time", time);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步班级结束");
		System.out.println("------- 同步班级" + mark + "结束");
	}

	public void saveXQClass(String interfaceName, String apiCode, String mark) throws Exception {
		log.info("------- 开始同步" + mark + "学前班级");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.class." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		//先获取年级代码的数据
		String gradeBase = DYSynHttpRequestUtils.doInterface("BASENJDM", "c293f32d-e168-4729-91a0-7bd58e874d3d", null);
		Map<String, JSONObject> baseMap = new HashMap<String, JSONObject>();
		if(StringUtils.isNotBlank(gradeBase)) {
			JSONArray array = Json.parseArray(gradeBase);
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				if(js.getString("NJDM").charAt(0) == '1' ) {
					js.put("NJMC", "小"+(js.getString("NJMC").substring(0, 1)));
				}
				baseMap.put(js.getString("XX_NJDMB_ID"), js);
			}
		}
		
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);
			String cc = null;
			Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
			if (semester == null) {
				log.info("------ 请先维护学年学期 -------");
				return;
			}
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
				int count = 0;
				while (StringUtils.equals("ERROR", cc) && count < maxCount) {
					count++;
					log.info("-------获取不到数据，第" + count + "次重新获取");
				}
				if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
					RedisUtils.set("syncdata.lydy.class." + mark + ".time", time);
					log.info("------- 同步学前班级结束");
					return;
				}
				JSONArray array = Json.parseArray(cc);
				//先得到对应的idList
				Set<String> schoolIds = new HashSet<>();
				Set<String> teacherIds = new HashSet<>();
				Set<String> clazzIds = new HashSet<>();
				Set<String> teachAreaIds = new HashSet<>();
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String clazzId ;
					String schoolId ;
					clazzId = js.getString("XQ_XX_BJXX_ID").replaceAll("-", "");
					schoolId = js.getString("XQ_XX_JBXX_ID").replaceAll("-", "");
					String bzrid = js.getString("BZRID");
					if(StringUtils.isNotBlank(bzrid)) {
						bzrid = bzrid.replaceAll("-", "");
						teacherIds.add(bzrid);
					}
					schoolIds.add(schoolId);
					clazzIds.add(clazzId);
				}
				
				Map<String, Teacher> teacherMap = new HashMap<>();
				if(CollectionUtils.isNotEmpty(teacherIds)) {
					List<Teacher> teachers = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[teacherIds.size()])),
							new TR<List<Teacher>>() {});
					teacherMap = EntityUtils.getMap(teachers, "id", null);
				}
				List<Clazz> allClazz = SUtils.dt(classRemoteService.findListByIds(clazzIds.toArray(new String[clazzIds.size()])), 
						new TR<List<Clazz>>() {});
				Map<String, Clazz> allClazzMap = EntityUtils.getMap(allClazz, "id", StringUtils.EMPTY);
				
				List<School> allSchool = SUtils.dt(schoolRemoteService.findListByIds(schoolIds.toArray(new String[schoolIds.size()])),
						new TR<List<School>>() {});
				Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, "id", StringUtils.EMPTY);
				
				log.info("------------开始检索， " + mark + "班级总数：" + array.size());
				sdf.applyPattern("yyyyMMddHHmmss");
				List<Clazz> classes = new ArrayList<Clazz>();
				List<DYClazz> dyClasses = new ArrayList<DYClazz>();
				List<Grade> grades = new ArrayList<Grade>();
				int invalidate = 0;
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String gxsj = js.getString("GXSJ");
					if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
						time = gxsj;
					}
					String clazzId ;
					String schoolId ;
					clazzId = js.getString("XQ_XX_BJXX_ID").replaceAll("-", "");
					schoolId = js.getString("XQ_XX_JBXX_ID").replaceAll("-", "");
					
					Clazz clazz;
					if(allClazzMap.get(clazzId) != null) {
						clazz = allClazzMap.get(clazzId);
					}else {
						clazz = new Clazz();
						clazz.setId(clazzId);
						clazz.setState(0);
						clazz.setArtScienceType(0);
						clazz.setEventSource(0);
						clazz.setCreationTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
					}
					String className = js.getString("BJMC");
					clazz.setClassName(className);
					clazz.setModifyTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
					
					//班级的软删的问题，同一个班级可能出现软删的问题
					clazz.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
					
					// 根据对方的班主任Id获取教师，设置教师Id
					String bzrid = js.getString("BZRID");
					if(StringUtils.isNotBlank(bzrid) && teacherMap.get(js.getString("BZRID").replaceAll("-", "")) != null) {
						clazz.setTeacherId(teacherMap.get(js.getString("BZRID").replaceAll("-", "")).getId());
					}else {
						clazz.setTeacherId("");
					}
					if(allSchoolMap.get(schoolId) == null) {
						invalidate++;
						log.info("班级的"+clazzId + "的学校不存在");
						continue;
					}
					clazz.setSchoolId(schoolId);
//					if(subSchoolMap.get(js.getString("XX_XQXX_ID").replaceAll("-", "")) == null) {
//						invalidate++;
//						log.info("班级的"+clazzId+ "的校区不存在"+"----XX_XQXX_ID");
//						continue;
//					}
//					clazz.setCampusId(js.getString("XX_XQXX_ID").replaceAll("-", ""));
					String gradeCodeId = js.getString("XX_NJDMB_ID");
					if(StringUtils.isBlank(gradeCodeId) ) {
						invalidate++;
						log.info("班级的"+clazzId+ "的年级代码不存在"+"----XX_NJDMB_ID");
						continue;
					}
					String njdmbId = js.getString("XX_NJDMB_ID");
					JSONObject njdmbObject = baseMap.get(njdmbId);
					//得到年级code 
					String gradeCode = njdmbObject.getString("NJDM");
					if(gradeCode.length() == 1) {
						gradeCode = "0" + gradeCode;
					}
					//得到年纪名称
					String gradeName = njdmbObject.getString("NJMC");
					clazz.setSection(0);//转化成幼儿园
					//转化学制
					clazz.setSchoolingLength(3);
					
					clazz.setAcadyear(js.getString("XNMC"));
					//班号我们自己生成
					String classCodePrefix = StringUtils.substring(clazz.getAcadyear(), 0, 4)
							+ StringUtils.leftPad(clazz.getSection() + "", 2, "0");
					Long max = RedisUtils
							.incrby("syncdata.lydy.class.code.max." + clazz.getSchoolId() + classCodePrefix, 1);
					clazz.setClassCode(classCodePrefix + StringUtils.leftPad("" + max, 2, "0"));
					
					String year = StringUtils.substring(clazz.getAcadyear(), 0, 4);
					String year2 = StringUtils.substring(semester.getAcadyear(), 0, 4);
					if (NumberUtils.toInt(year2) >= (NumberUtils.toInt(year) + clazz.getSchoolingLength())) {
						clazz.setIsGraduate(1);
						clazz.setGraduateDate(new Date());
					} else {
						clazz.setIsGraduate(0);
					}
					clazz.setClassType(js.getString("BJLXDM"));
					//进行id的匹配
					DYClazz dyClazz = new DYClazz();
					dyClazz.setId(clazzId);
					dyClazz.setFixClassId(clazz.getId());
					dyClasses.add(dyClazz);

					String gradesJson = gradeRemoteService.findBySchoolId(clazz.getSchoolId(),
							ArrayUtils.toArray(clazz.getSection()), clazz.getAcadyear(), true);
					List<Grade> gradesExists = SUtils.dt(gradesJson, new TypeReference<List<Grade>>() {
					});
					Map<String, Grade> gradeMap = new HashMap<String, Grade>();
					for (Grade grade1 : gradesExists) {
						String key = grade1.getSchoolId() + grade1.getOpenAcadyear() + grade1.getSection();
						gradeMap.put(key, grade1);
					}
					//年级名称的转化
					Grade grade1 = gradeMap.get(clazz.getSchoolId() + clazz.getAcadyear() + clazz.getSection());
					if (grade1 == null) {
						grade1 = new Grade();
						grade1.setGradeName(gradeName);
						grade1.setCreationTime(sdf.parse(gxsj));
						grade1.setModifyTime(sdf.parse(gxsj));
						grade1.setEventSource(0);
						grade1.setAmLessonCount(4);
						grade1.setPmLessonCount(4);
						grade1.setOpenAcadyear(clazz.getAcadyear());
						grade1.setNightLessonCount(3);
						grade1.setGradeCode(gradeCode);
						grade1.setSubschoolId(clazz.getCampusId());
						grade1.setSchoolId(clazz.getSchoolId());
						grade1.setIsGraduate(0);
						grade1.setSection(clazz.getSection());
						grade1.setSchoolingLength(1);
						if (grade1.getSchoolingLength() == null || grade1.getSchoolingLength() == 0) {
							grade1.setSchoolingLength(grade1.getSection() == 1 ? 6 : 3);
						}
						grade1.setId(UuidUtils.generateUuid());
						grades.add(grade1);
						gradeMap.put(grade1.getSchoolId() + grade1.getOpenAcadyear() + grade1.getSection(), grade1);
					}
					clazz.setGradeId(grade1.getId());
					classes.add(clazz);
				}
				log.info("------- 更新" + classes.size() + "个" + mark + "班级数据，不符合条件" + invalidate + "个 -------");
				classRemoteService.saveAll(SUtils.s(classes.toArray(new Clazz[0])));
				if (CollectionUtils.isNotEmpty(grades)) {
					gradeRemoteService.saveAll(SUtils.s(grades.toArray(new Grade[0])));
				}
				dyClassService.saveAll(dyClasses.toArray(new DYClazz[0]));
			RedisUtils.set("syncdata.lydy.class." + mark + ".time", time);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步班级结束");
	}
	
	public void saveGrade(String interfaceName, String apiCode, String mark) throws Exception {
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
		if (semester == null) {
			log.info("------ 请先维护学年学期 -------");
			return;
		}
		
		//先获取年级代码的数据
		String gradeBase = DYSynHttpRequestUtils.doInterface("BASENJDM", "c293f32d-e168-4729-91a0-7bd58e874d3d", null);
		Map<String, JSONObject> baseMap = new HashMap<String, JSONObject>();
		if(StringUtils.isNotBlank(gradeBase)) {
			JSONArray array = Json.parseArray(gradeBase);
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				if(js.getString("NJDM").charAt(0) == '1' ) {
					js.put("NJMC", "小"+(js.getString("NJMC").substring(0, 1)));
				}
				baseMap.put(js.getString("XX_NJDMB_ID"), js);
			}
		}
		
		//获取学段的信息  
		String sectionBase = DYSynHttpRequestUtils.doInterface("BASEXD", "2cdbecff-40b7-4847-8977-a96c991f15c7", null);
		Map<String, JSONObject> sectionMap = new HashMap<String, JSONObject>();
		if(StringUtils.isNotBlank(sectionBase)) {
			JSONArray array = Json.parseArray(sectionBase);
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				sectionMap.put(js.getString("XX_XDXX_ID"), js);
			}
		}

		List<SubSchool> subSchools = SUtils.dt(subSchoolRemoteService.findAll(), new TR<List<SubSchool>>() {
		});
		Map<String, SubSchool> subSchoolMap = EntityUtils.getMap(subSchools, SubSchool::getId);
		
//		List<TeachArea> allTeachAreas = teachAreaService.findAll();
//		Map<String, TeachArea> subSchoolMap = EntityUtils.getMap(allTeachAreas, "id", null);
		
		List<Grade> allGrade = SUtils.dt(gradeRemoteService.findAll(), new TR<List<Grade>>() {
		});
//		Map<String, Grade> allGradeMap = EntityUtils.getMap(allGrade, Grade::getId);
		Map<String, List<Grade>> unitGradeMap = EntityUtils.getListMap(allGrade, Grade::getSchoolId, Function.identity());
		
		List<School> allSchool = SUtils.dt(schoolRemoteService.findAll(), new TR<List<School>>() {});
		Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getId);
		
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.grade." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);

			String cc = null;
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
				int count = 0;
				while (StringUtils.equals("ERROR", cc) && count < maxCount) {
					count++;
					cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
					log.info("------- 获取不到数据，第" + count + "次重新获取");
				}
				if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
					log.info("------- 同步单位" + mark + "结束");
					return;
				}
				JSONArray array = Json.parseArray(cc);
				log.info("------------开始检索，年级总数：" + array.size());

				sdf.applyPattern("yyyyMMddHHmmss");
				List<Grade> grades = new ArrayList<Grade>();
				List<DYGrade> dyGrades = new ArrayList<DYGrade>();
				int invalidate = 0;
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String gxsj = js.getString("GXSJ");
					if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
						time = gxsj;
					}
					String gradeId = js.getString("XX_NJXX_ID").replaceAll("-", "");
					String schoolId = js.getString("XX_JBXX_ID").replaceAll("-", "");
					String njdmbId = js.getString("XX_NJDMB_ID");
					JSONObject njdmbObject = baseMap.get(njdmbId);
					String gradeCode = njdmbObject.getString("NJDM");
					if(gradeCode.length() == 1) {
						gradeCode = "0" + gradeCode;
					}
					List<Grade> unitGrades = unitGradeMap.get(schoolId);
					Map<String,Grade> gradeCodeMap = EntityUtils.getMap(unitGrades, "gradeCode", StringUtils.EMPTY);
					Grade grade;
					if(gradeCodeMap.get(gradeCode) != null) {
						grade = gradeCodeMap.get(gradeCode);
						grade.setTeacherId(grade.getTeacherId());
					}else {
						grade = new Grade();
						grade.setId(gradeId);
						grade.setEventSource(0);
						grade.setAmLessonCount(4);
						grade.setPmLessonCount(4);
						grade.setNightLessonCount(3);
						grade.setTeacherId(null);
						grade.setCreationTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
						grade.setIsDeleted(0);  //都是默认存在
					}
					grade.setGradeCode(gradeCode);
					grade.setGradeName(njdmbObject.getString("NJMC"));
					grade.setModifyTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
					grade.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
					
					String subschoolId = js.getString("XX_XQXX_ID");
					if(StringUtils.isNotBlank(subschoolId)){
						if(subSchoolMap.get(subschoolId.replaceAll("-", "")) == null) {
							invalidate++;
							log.info("年级的"+js.getString(mark + "XX_NJXX_ID") + "的校区不存在");
							continue;
						}
						subschoolId = subschoolId.replaceAll("-", "");
					}
					grade.setSubschoolId(subschoolId);
					String schoolId1 = js.getString("XX_JBXX_ID").replaceAll("-", "");
					if(allSchoolMap.get(schoolId1) == null) {
						invalidate++;
						log.info("年级的"+js.getString(mark + "XX_NJXX_ID") + "的学校不存在");
						continue;
					}
					grade.setSchoolId(schoolId1);
					//转化学段
					String sectionId = njdmbObject.getString("XX_XDXX_ID");
					JSONObject sectionObject = sectionMap.get(sectionId);
					String section = sectionObject.getString("XDDM");
					if(section.equals("4")) {
						section = "0";  //转化成幼儿园
					}
					grade.setSection(Integer.valueOf(section));
					
					
					grade.setSchoolingLength(js.getInteger("XZ"));
					if (grade.getSchoolingLength() == null || grade.getSchoolingLength() == 0) {
						grade.setSchoolingLength(grade.getSection() == 1 ? 6 : 3);   //幼儿园的情况怎么判断？？？
					}
					String gradeName = grade.getGradeName();
//					TeachArea subSch = subSchoolMap.get(grade.getSubschoolId());
					SubSchool subSch = subSchoolMap.get(grade.getSubschoolId());
					if (subSch != null) {
						gradeName = "(" + subSch.getName() + ")" + gradeName;
						grade.setGradeName(gradeName);
					}
					
					//得到入学学年
					String year = StringUtils.substring(js.getString("RXNF"), 0, 4);
					String acadyear = NumberUtils.toInt(year) + "-" + (NumberUtils.toInt(year) + 1);
					grade.setOpenAcadyear(acadyear);
					
					//判断学生是否已毕业
					Integer schoolLength = Integer.valueOf(js.getString("XZ"));
					String nowYear = StringUtils.substring(sdf.format(new Date()), 0, 4);
					int year1 = (NumberUtils.toInt(nowYear) - schoolLength -1);
//					String year2 = StringUtils.substring(semester.getAcadyear(), 0, 4);
					if (year.compareTo(String.valueOf(year1)) > 0) {
						grade.setIsGraduate(0);
					} else {
						grade.setIsGraduate(1);
					}
					
					grades.add(grade);
					DYGrade dyGrade = new DYGrade();
					dyGrade.setId(js.getString("XX_NJXX_ID").replaceAll("-", ""));
					dyGrade.setFixGradeId(grade.getId());
					dyGrades.add(dyGrade);
//					grades.add(grade);
				}
				log.info("------- 更新" + grades.size() + "个" + mark + "年级数据,数据失败的"+invalidate+"-----个");
				gradeRemoteService.saveAll(SUtils.s(grades.toArray(new Grade[0])));
				dyGradeService.saveAll(dyGrades.toArray(new DYGrade[0]));
//			}
			RedisUtils.set("syncdata.lydy.grade." + mark + ".time", time);
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void saveUnit(String interfaceName, String apiCode, String mark) throws Exception {
		log.info("------- 开始同步" + mark + "单位");
		System.out.println("------- 开始同步" + mark + "单位");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.unit." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);
			String cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
			int count = 0;
			while (StringUtils.equals("ERROR", cc) && count < maxCount) {
				count++;
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
				log.info("------- 获取不到数据，第" + count + "次重新获取");
			}
			if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
				log.info("------- 同步单位" + mark + "结束");
				System.out.println("------- 同步单位" + mark + "结束");
				return;
			}

			JSONArray array = Json.parseArray(cc);
			//先得到对应的idList
			Set<String> schoolIds = new HashSet<>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String schoolId;
				if ("XQXX".equalsIgnoreCase(interfaceName)) {
					schoolId = js.getString("XQ_XX_JBXX_ID").replaceAll("-", "");
				}else if ("ZZXX".equalsIgnoreCase(interfaceName)) {
					schoolId = js.getString("ZZ_XX_JBXX_ID").replaceAll("-", "");
				}else{
					schoolId = js.getString("XX_JBXX_ID").replaceAll("-", "");
				}
				schoolIds.add(schoolId);
			}
			
			log.info("------------" + mark + "单位总数：" + array.size());
			List<Unit> saveUnits = new ArrayList<Unit>();
			List<School> saveSchools = new ArrayList<School>();
			sdf.applyPattern("yyyyMMddHHmmss");
			List<Unit> edus = SUtils.dt(unitRemoteService.findByUnitClass(Unit.UNIT_CLASS_EDU), new TR<List<Unit>>() {
			});
			Map<String, Unit> eduMap = EntityUtils.getMap(edus, Unit::getRegionCode);
			Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);

			List<SchtypeSection> schoolTypeSections = SUtils.dt(schtypeSectionRemoteService.findAll(),new TR<List<SchtypeSection>>() {
					});
			Map<String, String> stsMap = EntityUtils.getMap(schoolTypeSections, SchtypeSection::getSchoolType, SchtypeSection::getSection);
			List<Unit> allUnits = SUtils.dt(unitRemoteService.findListByIds(schoolIds.toArray(new String[schoolIds.size()])),
					new TR<List<Unit>>() {});
			Map<String, Unit> allUnitMap = EntityUtils.getMap(allUnits, Unit::getId);
			List<School> allSchool = SUtils.dt(schoolRemoteService.findListByIds(schoolIds.toArray(new String[schoolIds.size()])),
					new TR<List<School>>() {});
			Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getId);
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String gxsj = js.getString("GXSJ").replaceAll(" ", "");
				if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
					time = gxsj;
				}
				String schoolType = null;
				String section ;
				String schoolId;
				if ("XQXX".equalsIgnoreCase(interfaceName)) {
					schoolId = js.getString("XQ_XX_JBXX_ID").replaceAll("-", "");
					section = "0";
					schoolType = "111";
				}else if ("ZZXX".equalsIgnoreCase(interfaceName)) {
					schoolId = js.getString("ZZ_XX_JBXX_ID").replaceAll("-", "");
					section = "3";
					schoolType = "365";
				}else {
					schoolId = js.getString("XX_JBXX_ID").replaceAll("-", "");
					schoolType = StringUtils.isBlank(js.getString("XXBXLXDM")) ? "211" : js.getString("XXBXLXDM");
				}
				Integer runSchoolType = StringUtils.isBlank(js.getString("XXJBZDM")) ? School.DEFAULT_RUN_SCHOOL_TYPE :
					Integer.valueOf(js.getString("XXJBZDM"));
				
				Unit u;
				if(MapUtils.isNotEmpty(allUnitMap) && allUnitMap.get(schoolId) != null) {
					u = allUnitMap.get(schoolId);
				}else {
					u = new Unit();
					u.setId(schoolId);
					u.setCreationTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
					u.setRegionCode(StringUtils.substring(js.getString("SSZGJYXZDM"), 0, 6));
					String regionCode = u.getRegionCode();
					Unit edu = eduMap.get(regionCode);
					if (edu != null) {
						u.setParentId(edu.getId());
					} else {
						u.setParentId(topUnit == null ? Constant.GUID_ZERO : topUnit.getId());
					}
					//缓存为空时默认取region_code 的总数
					List<Unit> regionUnits = SUtils.dt(unitRemoteService.findByRegionCode(regionCode),
							new TR<List<Unit>>() {});
					if(RedisUtils.get("syncdata.lydy.unit.unioncode.max.regioncode." + regionCode) == null) {
						RedisUtils.set("syncdata.lydy.unit.unioncode.max.regioncode." + regionCode, String.valueOf(regionUnits.size()));
					}
					Long max = RedisUtils.incrby("syncdata.lydy.unit.unioncode.max.regioncode." + regionCode, 1);
					log.info("-----------新增单位的unioncode的值是-----" + max);
					if(StringUtils.isEmpty(regionCode)) {
						u.setUnionCode(StringUtils.leftPad(max + "", 6, "0"));
					}else {
						u.setUnionCode(regionCode + StringUtils.leftPad(max + "", 6, "0"));
					}
					u.setDisplayOrder(u.getUnionCode());
					u.setRegionLevel(
							StringUtils.endsWith(regionCode, "0000") ? 3 : StringUtils.endsWith(regionCode, "00") ? 4 : 5);
					
					u.setRootUnitId(topUnit.getId());
				}
				u.setUnitName(js.getString("XXMC"));
				u.setUnitClass(Unit.UNIT_CLASS_SCHOOL);
				u.setModifyTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
				u.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
				u.setSchoolType(schoolType);  
				u.setRunSchoolType(runSchoolType);   // 默认都是 831
				//5--幼儿园   3--小初高
				if(u.getUnitName().contains("幼儿园") 
						|| "XQXX".equalsIgnoreCase(interfaceName) ) {
					u.setUnitType(Unit.UNIT_SCHOOL_KINDERGARTEN);
				}else if ("ZZXX".equalsIgnoreCase(interfaceName)){
					u.setUnitType(Unit.UNIT_SCHOOL_COLLEGE);
				}else {
					u.setUnitType(Unit.UNIT_SCHOOL_ASP);
				}
				saveUnits.add(u);
				//学校的添加和修改
				School sch;
				if(MapUtils.isNotEmpty(allSchoolMap) && allSchoolMap.get(schoolId) != null) {
					sch = allSchoolMap.get(schoolId);
				}else {
					sch = new School();
					sch.setId(u.getId());
					sch.setCreationTime(u.getCreationTime());
					//区域属性码
					sch.setRegionPropertyCode("121");  //镇中心区
				}
				sch.setSchoolName(u.getUnitName());
				sch.setIsDeleted(u.getIsDeleted());
				sch.setModifyTime(u.getModifyTime());
				sch.setRunSchoolType(u.getRunSchoolType());
				sch.setRegionCode(u.getRegionCode());
				String  schoolCode = js.getString("XXBSM");
				if(StringUtils.isNotBlank(schoolCode)) {
					sch.setSchoolCode(js.getString("XXBSM"));
				}else {
					sch.setSchoolCode(u.getUnionCode());
				}
				sch.setSchoolType(u.getSchoolType());
				sch.setSections(stsMap.get(sch.getSchoolType()));
				saveSchools.add(sch);
			}
			log.info("------- 更新" + saveUnits.size() + "个" + mark + "单位数据 -------");
			System.out.println("同步学校数量------------- 更新" + saveUnits.size() + "个" + mark + "单位数据 -------" );
			if(CollectionUtils.isNotEmpty(saveSchools)){
				baseSyncSaveService.saveSchool(saveSchools.toArray(new School[saveSchools.size()]));
			}
			if(CollectionUtils.isNotEmpty(saveUnits)){
				baseSyncSaveService.saveUnit(saveUnits.toArray(new Unit[saveUnits.size()]));
			}
			RedisUtils.set("syncdata.lydy.unit." + mark + ".time", time);
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步单位结束");
		System.out.println("------- 同步单位" + mark + "单位");
	}

	@Override
	public void saveTeacher(String interfaceName, String apiCode, String mark) throws Exception {
		System.out.println("------- 开始同步" + mark + "教师");
		log.info("------- 开始同步" + mark + "教师");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.teacher." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);
			
			String cc = null;
			Integer pageSize = 1000;
			
			List<User> allUser = new ArrayList<>();
			List<Unit> allUnits = new ArrayList<>();
			List<Teacher> allTeachers = new ArrayList<>();
			for (int k = 0; k < 100; k++) {
				Set<String> schoolIds = new HashSet<>();
				Set<String> teacherIds = new HashSet<>();
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json, k + 1, pageSize);
				int count = 0;
				while (StringUtils.equals("ERROR", cc) && count < maxCount) {
					cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json, k + 1, pageSize);
					count++;
				}
				JSONArray array = Json.parseArray(cc);
				if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount || array.size() == 0 ) {
					System.out.println("------- 同步教师" + mark + "结束");
					break;
				}
				log.info("------------第" + (k + 1) + "次检索， 教师总数：" + array.size());
				sdf.applyPattern("yyyyMMddHHmmss");
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String teacherId;
					String schoolId;
					if("XQJS".equals(interfaceName)) {
						teacherId = js.getString("XQ_JG_JBXX_ID").replaceAll("-", "");
						schoolId = js.getString("XQ_XX_JBXX_ID").replaceAll("-", "");
					} else if ("ZZJS".equals(interfaceName)){
						teacherId = js.getString("ZZ_JG_JBXX_ID").replaceAll("-", "");
						schoolId = js.getString("ZZ_XX_JBXX_ID").replaceAll("-", "");
					} else {
						if("JYJJS".equals(interfaceName)) {
							teacherId = js.getString("RYID").replaceAll("-", "");
							schoolId = js.getString("JYJ_JBXX_ID").replaceAll("-", "");
						}else {
							teacherId = js.getString("JG_JBXX_ID").replaceAll("-", "");
							schoolId = js.getString("XX_JBXX_ID").replaceAll("-", "");
						}
					}
					schoolIds.add(schoolId);
					teacherIds.add(teacherId);
				}
				allUser.addAll(SUtils.dt(userRemoteService.findByOwnerIds(teacherIds.toArray(new String[teacherIds.size()])), new TR<List<User>>() {}));
				allUnits.addAll(SUtils.dt(unitRemoteService.findListByIds(schoolIds.toArray(new String[schoolIds.size()])), new TR<List<Unit>>() {}));
				allTeachers.addAll(SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[teacherIds.size()])), new TR<List<Teacher>>() {}));
			}
			Map<String, User> allTeaIdUserMap = EntityUtils.getMap(allUser, User::getOwnerId);
			Map<String, Unit> allUnitMap = EntityUtils.getMap(allUnits, Unit::getId);
			Map<String,List<Teacher>> unitIdMap = EntityUtils.getListMap(allTeachers, Teacher::getUnitId, Function.identity());
			Map<String, Teacher> allTeacherMap = EntityUtils.getMap(allTeachers, Teacher::getId);
			Set<String> unitIds = EntityUtils.getSet(allUnits, Unit::getId);
			Map<String, List<Dept>> deptMap = SUtils.dt(deptRemoteService.findByUnitIdMap(unitIds.toArray(new String[unitIds.size()])), new TypeReference<Map<String, List<Dept>>>(){});
			
			Set<String> saveUserNameSet = new HashSet<>();
			for (int k = 0; k < 100; k++) {
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json, k + 1, pageSize);
				JSONArray array = Json.parseArray(cc);
				if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc)  || array.size() == 0 ) {
					RedisUtils.set("syncdata.lydy.teacher." + mark + ".time", time);
					log.info("------- 同步教师结束");
					return;
				}
				log.info("------------第" + (k + 1) + "次检索， 教师总数：" + array.size());
				sdf.applyPattern("yyyyMMddHHmmss");
				List<Teacher> saveTeacherList = new ArrayList<Teacher>();
				List<User> saveUserList = new ArrayList<User>();
				int invalidate = 0;
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String teacherId;
					String schoolId;
					if("XQJS".equals(interfaceName)) {
						teacherId = js.getString("XQ_JG_JBXX_ID").replaceAll("-", "");
						schoolId = js.getString("XQ_XX_JBXX_ID").replaceAll("-", "");
					} else if ("ZZJS".equals(interfaceName)){
						teacherId = js.getString("ZZ_JG_JBXX_ID").replaceAll("-", "");
						schoolId = js.getString("ZZ_XX_JBXX_ID").replaceAll("-", "");
					} else {
						if("JYJJS".equals(interfaceName)) {
							teacherId = js.getString("RYID").replaceAll("-", "");
							schoolId = js.getString("JYJ_JBXX_ID").replaceAll("-", "");
						}else {
							teacherId = js.getString("JG_JBXX_ID").replaceAll("-", "");
							schoolId = js.getString("XX_JBXX_ID").replaceAll("-", "");
						}
					}
					String userName = js.getString("YHM");
					if(!js.containsKey("YHM") || StringUtils.isBlank(userName)) {
						invalidate++;
						log.info("老师的"+teacherId +  "的YHM为空");
						continue;
					}else {
						//验证username 信息是否满足 用户名必须为4-20个字符(包括字母、数字、下划线、中文)，1个汉字为2个字符
						if(net.zdsoft.framework.utils.StringUtils.getRealLength(userName) > 20
						        || net.zdsoft.framework.utils.StringUtils.getRealLength(userName) < 4 
						        || isContainChinese (userName)){
							invalidate++;
							System.out.println("老师的用户名不符合passport的规则------------------------》》》"+teacherId  + "username ----" + userName);
							continue;
						}
					}
					
					//判断该账户是否已经存在：
					if(CollectionUtils.isNotEmpty(saveUserNameSet) && saveUserNameSet.contains(userName)){
						System.out.println("老师的用户名" + userName + "已经存在了");
						continue;
					}
					String gxsj = doChangeGxsj(js.getString("GXSJ"));
					if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
						time = gxsj;
					}
					// 中文跳过
					if (isContainChinese(teacherId)) {
						invalidate++;
						log.info("老师的"+teacherId +  "为中文");
						continue;
					}
					
					Teacher t;
					if(MapUtils.isNotEmpty(allTeacherMap) && allTeacherMap.get(teacherId) != null) {
						t = allTeacherMap.get(teacherId);
					}else {
						t = new Teacher();
						t.setId(teacherId);
//						t.setFixTeacherId(teacherId);
						t.setIncumbencySign("11");
						if(allUnitMap.get(schoolId) == null) {
							invalidate++;
							log.info("老师的"+teacherId +  "的单位为空");
							continue;
						}
						t.setUnitId(schoolId);
						t.setWeaveUnitId(t.getUnitId());
						t.setInPreparation("1");  //是否在编  1 --是 0 --否
						t.setIncumbencySign("11"); //教师状态---在职
						t.setIsLeaveSchool("0");
						t.setEventSource(1);
						
						//设置排序号
						if(unitIdMap == null || unitIdMap.isEmpty() || unitIdMap.get(schoolId) == null) {
							t.setDisplayOrder(1);
						}else {
							int disPlayOrder = unitIdMap.get(schoolId).size();
							t.setDisplayOrder(disPlayOrder+1);
						}
					}
					t.setTeacherName(js.getString("XM"));
					if("JYJJS".equals(interfaceName)) {
						t.setIdentityCard(js.getString("ZJHM"));
						t.setMobilePhone(js.getString("YDDH"));
						t.setEmail(js.getString("EMAIL"));
						if (js.containsKey("CSRQ") && StringUtils.isNotBlank(js.getString("CSRQ")))
							t.setBirthday(DateUtils.parseDate(js.getString("CSRQ").substring(0, 10), "yyyy-MM-dd"));
						t.setTeacherCode(js.getString("JGZGH"));
					}else {
						t.setIdentityCard(js.getString("SFZJH"));
						t.setLinkPhone(js.getString("LXDH"));
						t.setMobilePhone(js.getString("LXDH"));
					}
					t.setSex(js.getInteger("XBM") == null ? McodeDetail.SEX_MALE : js.getInteger("XBM"));
//					Map<String, List<String>> teaDeptMap = deptidMap.get(schoolId);
//					if(teaDeptMap == null ||  teaDeptMap.isEmpty()) {
//						log.info("学校的部门中没有对应的教师，学校id是----------》"+schoolId);
//					}else {
//						if(CollectionUtils.isEmpty(teaDeptMap.get(teacherId))) {
//							log.info("教师没有对应的部门,教师的id是----------》"+teacherId);
//						}else {
//							int size = teaDeptMap.get(teacherId).size();
//							t.setDeptId(teaDeptMap.get(teacherId).get(size-1)); //取最后一个数据
//						}
//					}
				    
					Long max = RedisUtils.incrby("syncdata.lydy.teacher.code.by.school.id." + t.getUnitId(), 1);
					t.setTeacherCode(StringUtils.leftPad(max + "", 6, "0"));
					
					String nation = js.getString("MZM");
					if(StringUtils.isBlank(nation)) {
						t.setNation("01");
					}else {
						if("97".equals(js.getString("MZM"))) {
							t.setNation("99");
						}else {
							t.setNation(StringUtils.leftPad(js.getString("MZM"), 2, "0"));
						}
					}
					
					t.setCreationTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
					t.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
					//如果教师软删掉，多单位情况的处理
//					if(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p")) {
//						List<Teacher> teachers2 = allFixTeacherMap.get(t.getId());
//						if(CollectionUtils.isNotEmpty(teachers2)) {
//							for (Teacher teacher : teachers2) {
//								teacher.setIsDeleted(1);
//								if(allTeaIdUserMap.get(teacher.getId()) != null) {
//									User user = allTeaIdUserMap.get(teacher.getId());
////									user.setUserState(User.USER_MARK_LOCK);  //账号锁定
//									user.setIsDeleted(1);
//									users.add(user);
//								}
//							}
//							teachers.addAll(teachers2);
//						}
//					}
					
					t.setModifyTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
					// 设置教师的身份证类型
					String idCardType = js.getString("SFZJLXM");
					if(StringUtils.isBlank(idCardType)) {
						t.setIdentityType("1");
					}else {
						t.setIdentityType(idCardType);
					}
					
					if(StringUtils.isNotBlank(js.getString("ZYJSZWM"))) {
						t.setDuty(js.getString("ZYJSZWM"));
					}
//					if(StringUtils.isNotBlank(js.getString("CJNY"))) {
//						t.setEducationWorkDate(sdf.parse(js.getString("CJNY")));
//					}
					saveTeacherList.add(t);
					//添加对应的用户
					if(StringUtils.isNotBlank(userName)) {
						User ur;
						if(MapUtils.isNotEmpty(allTeaIdUserMap) && allTeaIdUserMap.get(t.getId()) != null) {
							ur = allTeaIdUserMap.get(t.getId());
							if(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p")) {
								ur.setUserState(User.USER_MARK_LOCK);
								ur.setIsDeleted(1);
							}
						}else {
							ur = new User();
							ur.setUsername(js.getString("YHM"));
							ur.setOwnerType(User.OWNER_TYPE_TEACHER);
							ur.setId(UuidUtils.generateUuid());
							ur.setPassword(getDefaultPwd());
							ur.setCreationTime(new Date());
							ur.setUserRole(2);
							ur.setEventSource(1);
							ur.setEnrollYear(0000);
							ur.setUserType(User.USER_TYPE_COMMON_USER);
							ur.setAccountId(t.getId());
							ur.setUnitId(t.getUnitId());
							Unit unit = allUnitMap.get(t.getUnitId());
							if(unit != null) {
								ur.setRegionCode(unit.getRegionCode());
							}else {
								invalidate++;
								log.info("教师的"+js.getString(mark + "_XS_JBXX_ID") + "的单位不存在");
								continue;
							}
							ur.setUserState(1);
							ur.setIconIndex(0);
							ur.setMobilePhone(t.getMobilePhone());
							ur.setSex(t.getSex());
							
							ur.setRealName(t.getTeacherName());
							ur.setModifyTime(new Date());
							ur.setDeptId(t.getDeptId());
							ur.setOwnerId(t.getId());
							
							//设置排序号，以前的数据不处理，新增的排序
							ur.setDisplayOrder(t.getDisplayOrder());
						}
						ur.setIsDeleted(t.getIsDeleted());
						//进行管理员的身份处理
//						if(js.getString("ISADMIN") != null) {
//							String isAdmin = js.getString("ISADMIN");
//							if("1".equals(isAdmin) && "JYJJS".equals(interfaceName)) {
//								ur.setUserType(User.USER_TYPE_TOP_ADMIN);
//							}else if("1".equals(isAdmin)){
//								ur.setUserType(User.USER_TYPE_UNIT_ADMIN);
//							}
//						}
						ur.setUserState(User.USER_MARK_NORMAL);
						saveUserList.add(ur);
						saveUserNameSet.add(userName);
					}
				}
				for (Teacher teacher : saveTeacherList) {
					if( MapUtils.isEmpty(allTeacherMap) || allTeacherMap.get(teacher.getId()) == null){
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
				//进行保存教师和学生数据
				System.out.println("数据同步到教师的数据：--------" +  saveTeacherList.size() + "同步用户的数据是：------" + saveUserList.size());
				if(CollectionUtils.isNotEmpty(saveTeacherList)){
					if(CollectionUtils.isNotEmpty(saveUserList)){
						baseSyncSaveService.saveTeacherAndUser(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]),saveUserList.toArray(new User[saveUserList.size()]));
					}else{
						baseSyncSaveService.saveTeacher(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]));
					}
				}
			}
			RedisUtils.set("syncdata.lydy.teacher." + mark + ".time", time);
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步教师结束");
		System.out.println("------- 同步教师" + mark + "结束");
	}

	/**
	 * 根据用户组信息来得到对应的用户信息
	 * @param interfaceName
	 * @param json
	 * @param allUnits
	 * @param deptidMap
	 */
	private void getUserGroupMap(String interfaceName, JSONObject json, List<Unit> allUnits,
			Map<String, Map<String, List<String>>> deptidMap) {
		for (Unit unit2 : allUnits) {
			String cc1 = DYSynHttpRequestUtils.doInterface(interfaceName, "93dc4ac0-dc4c-408b-85f3-a15c4d9f51d9", json, null,unit2.getId());
			JSONArray array = Json.parseArray(cc1);
			Map<String,List<String>> teacherIdMap = new HashMap<String,List<String>>();
			Set<String> teacherIds1 = new HashSet<>();
			if(CollectionUtils.isNotEmpty(array)) {
				for (int i = 0; i < array.size(); i++) {
					//现在先只考虑只有一个部门
					JSONObject js = array.getJSONObject(i);
					String teacherId = js.getString("YHXX").replaceAll("-", "");
					teacherIds1.add(teacherId);
				}
				for (String tid : teacherIds1) {
					Set<String> deptIds  = new HashSet<>();
					for (int i = 0; i < array.size(); i++) {
						//现在先只考虑只有一个部门
						JSONObject js = array.getJSONObject(i);
						String teacherId = js.getString("YHXX").replaceAll("-", "");
						if(tid.equals(teacherId)) {
							String deptId = js.getString("BZ_YHXX_YHZDMB_ID").replaceAll("-", "");
							deptIds.add(deptId);
						}
					}
					teacherIdMap.put(tid, new ArrayList<>(deptIds));
				}
			}
			deptidMap.put(unit2.getId(), teacherIdMap);
		}
	}

	@Override
	public void saveStudent(String interfaceName, String apiCode, String mark, String unitId) throws Exception {
		log.info("------- 开始同步" + mark + "学生");
		System.out.println("------- 开始同步" + mark + "学生");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.student." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<Student> studentList=SUtils.dt(studentRemoteService.findBySchoolId(unitId), new TR<List<Student>>(){});
		Map<String, Student> allStuMap = EntityUtils.getMap(studentList, Student::getId);
		List<User> allStuList = SUtils.dt(userRemoteService.findByUnitIdAndOwnerTypeAll(unitId,User.OWNER_TYPE_STUDENT),new TR<List<User>>() {});
		Map<String,User> allUserMap = EntityUtils.getMap(allStuList, User::getOwnerId);
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			if("XCGXS".equals(interfaceName)){
				json.put("GXSJ", time);  
			}
			json.put("XX_JBXX_ID", unitId);
			String cc = null;
		/** 要进行验证的数据封装   --- 开始*/
			Integer pageSize = 1000;
			for (int k = 0; k < 1000; k++) {
		 		cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json, k + 1, pageSize);
				int count = 0;
				while (StringUtils.equals("ERROR", cc) && count < maxCount) {
					cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json, k + 1, pageSize);
					count++;
					log.info("------- 分页:" + (k + 1) + "获取不到数据，第" + count + "次重新获取");
				}
				JSONArray array = Json.parseArray(cc);
				if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount  || array.size() == 0 ) {
					RedisUtils.set("syncdata.lydy.student." + mark + ".time", time);
					log.info("------- 同步学生结束");
					System.out.println("------- 同步学生" + mark + "结束");
					break;
				}
				System.out.println("学校id = " + unitId + "学生的总数是 ---" + array.size());
				log.info("------------第" + (k + 1) + "次检索， " + mark + "学生总数：" + array.size());
				sdf.applyPattern("yyyyMMddHHmmss");
				int invalidate = 0;
				//学生状态微代码
				Map<String, String> stuState = new HashMap<String, String>();
				stuState.put("46619","01");
				stuState.put("46620","03");
				stuState.put("626","52");
				stuState.put("629","41");
				stuState.put("630","42");
				stuState.put("625","51");
				stuState.put("623","31");
				stuState.put("622","11");
				//还有一些不一样的怎么转化。是默认取40 还是这边添加微代码类型  DM-YDLB
				List<Student> saveStudentList = new ArrayList<>();
				List<User> saveUserList = new ArrayList<>();
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String studentId;
					String classId;
					String schoolId;
					if("XQXS".equals(interfaceName)) {
						studentId = js.getString("XQ_XS_JBXX_ID").replaceAll("-", "");
						if(StringUtils.isBlank(js.getString("XQ_XX_BJXX_ID"))){
							System.out.println("学生的id----" + studentId  + "的班级id 是null");
							continue;
						}
						classId = js.getString("XQ_XX_BJXX_ID").replaceAll("-", "");
						schoolId = js.getString("XQ_XX_JBXX_ID").replaceAll("-", "");
					}else {
						studentId = js.getString("XS_JBXX_ID").replaceAll("-", "");
						String errClassId = js.getString("XX_BJXX_ID");
						if(StringUtils.isNotBlank(errClassId)) {
							classId = js.getString("XX_BJXX_ID").replaceAll("-", "");
						}else {
							invalidate++;
							log.info("学生的"+studentId + "的班级为空");
							continue;
						}
						schoolId = js.getString("XX_JBXX_ID").replaceAll("-", "");
					}
					Student student;
					if(MapUtils.isNotEmpty(allStuMap) && allStuMap.get(studentId) != null) {
						student = allStuMap.get(studentId);
						student.setNowState(stuState.get(js.getString("XSZT")));  //????
					}else {
						student = new Student();
						student.setId(studentId);
						student.setNowState("40"); //40--为登记状态
						student.setIsLeaveSchool(0);
						student.setEventSource(0);
						student.setSchoolId(schoolId);
						Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
						student.setEnrollYear(clazz != null ? clazz.getAcadyear() : "");
					}
					student.setStudentName(js.getString("XM"));
					student.setSex(js.getInteger("XBM") == null ? McodeDetail.SEX_MALE : js.getInteger("XBM"));
					student.setBackground(js.getString("ZZMMM"));
					student.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
					String nation = js.getString("MZM");
					if(StringUtils.isBlank(nation)) {
						student.setNation("01");
					}else {
						if("97".equals(js.getString("MZM"))) {
							student.setNation("99");
						}else {
							student.setNation(StringUtils.leftPad(js.getString("MZM"), 2, "0"));
						}
					}
					//添加学号和学籍号
					String studentCode = StringUtils.trimToEmpty(js.getString("BNXH"));
					String unitiveCode = StringUtils.trimToEmpty(js.getString("XJFH"));
					if(StringUtils.isBlank(studentCode)){
						if(StringUtils.isNotBlank(unitiveCode)){
							studentCode = unitiveCode;
						}else{
							studentCode = BaseSaveConstant.DEFAULT_STU_CODE;
						}
					}
					student.setStudentCode(studentCode);
					student.setUnitiveCode(unitiveCode);
					//需要添加的字段   丁勇那边提供
					student.setIdentityCard(js.getString("SFZJH"));
					student.setIdentitycardType(js.getString("SFZJLXM"));
//					student.setNowState(stuState.get(js.getString("XSZT")));
					String classId1 = classId;
					student.setClassId(classId1);
					String gxsj = js.getString("GXSJ");
					if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
						time = gxsj;
					}
					try {
						if (js.containsKey("CSRQ") && StringUtils.isNotBlank(js.getString("CSRQ")))
							student.setBirthday(DateUtils.parseDate(js.getString("CSRQ"), "yyyy-MM-dd"));
					} catch (ParseException e) {
					}
					student.setModifyTime(new Date());
					student.setCreationTime(new Date());
//					if(StringUtils.isNotBlank(gxsj)){
//						student.setModifyTime(DateUtils.parseDate(gxsj, "yyyyMMddHHmmss"));
//						student.setCreationTime(DateUtils.parseDate(gxsj, "yyyyMMddHHmmss"));
//					}
					saveStudentList.add(student);
					
					//增加对应的学生用户 base_user 
					String userName = js.getString("YHM");
					if(StringUtils.isNotBlank(userName)) {
						User ur;
						if(MapUtils.isNotEmpty(allUserMap) && allUserMap.get(studentId) != null){
							ur = allUserMap.get(studentId);
							ur.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
							if(StringUtils.isBlank(student.getNowState()) || !"11".equals(student.getNowState())) {
								ur.setUserState(User.USER_MARK_NORMAL);
							}
						}else {
							ur = new User();
							ur.setUsername(js.getString("YHM"));
							ur.setOwnerType(User.OWNER_TYPE_STUDENT);
							ur.setId(UuidUtils.generateUuid());
							ur.setPassword(getDefaultPwd());
							ur.setUserType(User.USER_TYPE_COMMON_USER);
							ur.setAccountId(student.getId());
							ur.setUnitId(student.getSchoolId());
							ur.setUserState(User.USER_MARK_NORMAL);
							ur.setIconIndex(0);
							ur.setMobilePhone(student.getMobilePhone());
							ur.setSex(student.getSex());
							ur.setRealName(student.getStudentName());
							ur.setOwnerId(student.getId());
						}
						saveUserList.add(ur);
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
			RedisUtils.set("syncdata.lydy.student." + mark + ".time", time);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步学生结束");
		System.out.println("------- 同步学生" + mark + "结束");
	}

	@Override
	public void saveSubSchool(String interfaceName, String apiCode) throws Exception {
		log.info("------- 开始同步校区");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.subschool.time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);
			String cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
			int count = 0;
			while (StringUtils.equals("ERROR", cc) && count < maxCount) {
				count++;
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
				log.info("------- 获取不到数据，第" + count + "次重新获取");
			}
			if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
				log.info("------- 同步分校结束");
				return;
			}
			JSONArray array = Json.parseArray(cc);
			log.info("------- 校区总数：" + array.size());
			Set<String> setUids = new HashSet<>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String schoolId = js.getString("XX_JBXX_ID").replaceAll("-", "");
				setUids.add(schoolId);
			}
			List<SubSchool>  subSchools = SUtils.dt(subSchoolRemoteService.findbySchoolIdIn(setUids.toArray(new String[setUids.size()])),new TR<List<SubSchool>>() {});
			Map<String, SubSchool> subSchoolMap = EntityUtils.getMap(subSchools, SubSchool::getId);
			List<School> allSchool = SUtils.dt(schoolRemoteService.findListByIds(setUids.toArray(new String[setUids.size()])),
					new TR<List<School>>() {});
			Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getId);
			
			sdf.applyPattern("yyyyMMddHHmmss");
			List<SubSchool> saveSubSchools = new ArrayList<SubSchool>();
			int invalidate = 0;
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String gxsj = js.getString("GXSJ");
				if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
					time = gxsj;
				}
				String subSchoolId = js.getString("XX_XQXX_ID").replaceAll("-", "");
				if(subSchoolId.length() != 32) {
					continue;
				}
				SubSchool ss;
				if(MapUtils.isNotEmpty(subSchoolMap) && subSchoolMap.get(subSchoolId) != null) {
					ss = subSchoolMap.get(subSchoolId);
				}else {
					ss = new SubSchool();
					ss.setId(subSchoolId);
				}
				ss.setAddress(js.getString("XQDZ"));
				ss.setUpdatestamp(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj).getTime():new Date().getTime());
				ss.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
				String name = js.getString("XQMC");
				if(StringUtils.isNotBlank(js.getString("XQH"))) {
//					name = js.getString("XQH")+"-"+name;
					ss.setAreaCode(js.getString("XQH"));
				}
				ss.setName(name);
				if(allSchoolMap.get(js.getString("XX_JBXX_ID").replaceAll("-", "")) != null) {
					ss.setSchoolId(js.getString("XX_JBXX_ID").replaceAll("-", ""));
				}else {
					invalidate++;
					continue;
				}
				saveSubSchools.add(ss);
			}
			if(CollectionUtils.isNotEmpty(saveSubSchools)){
				log.info("------- 更新" + saveSubSchools.size() + "个校区数据 ,学校不存在的" + invalidate + "个 -------\"");
				baseSyncSaveService.saveSubSchool(saveSubSchools.toArray(new SubSchool[saveSubSchools.size()]));
			}
			
			RedisUtils.set("syncdata.lydy.subschool.time", time);
		} catch (ParseException e) {
			throw e;
		}
		log.info("------- 同步校区结束");
	}
	
	public void saveDept(String interfaceName, String apiCode, String mark) throws Exception {
		log.info("------- 开始同步" + mark + "单位");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.dept." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);
			String cc = null;
			List<Dept> allDept = SUtils.dt(deptRemoteService.findAll(), new TR<List<Dept>>() {});
			RedisUtils.set("syncdata.lydy.all.dept.code.max", allDept.size()+"");
	        Map<String, Dept> deptMap = EntityUtils.getMap(allDept, Dept::getId);
			List<Unit> allSchool = SUtils.dt(unitRemoteService.findByUnitClass(Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>() {});
			List<Dept> saveDepts = new ArrayList<Dept>();
			int invalidate = 0;
			for (Unit unit : allSchool) {
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json, null,unit.getId());
				if (StringUtils.isBlank(cc)) {
					invalidate++;
					continue;
				}
				JSONArray array = Json.parseArray(cc);
				log.info("------------开始检索"+unit.getUnitName()+"，部门总数：" + array.size());
				sdf.applyPattern("yyyyMMddHHmmss");
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String gxsj = doChangeGxsj(js.getString("GXSJ"));
					if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
						time = gxsj;
					}
					String deptId = js.getString("BZ_YHXX_YHZDMB_ID").replaceAll("-", "");
					Dept d;
					if(MapUtils.isNotEmpty(deptMap) && deptMap.get(deptId) != null) {
						d = deptMap.get(deptId);
					}else {
						d = new Dept();
						d.setId(deptId);
						d.setDeptType(1);
						d.setCreationTime(new Date());
						d.setModifyTime(new Date());
						d.setEventSource(0);
						d.setInstituteId(Constant.GUID_ZERO);
						d.setDeptType(1);
					}
					d.setUnitId(unit.getId());
					d.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
					String deptName = js.getString("YHZMC");
					if(StringUtils.isNotBlank(deptName)) {
						deptName = "(" + unit.getUnitName() + ")" + deptName;
					}
					d.setDeptName(deptName);
					String deptCode = js.getString("YHZDM");
					Long max = RedisUtils
							.incrby("syncdata.lydy.all.dept.code.max", 1);
					if(StringUtils.isBlank(deptCode)) {
						deptCode = max+ "";
						d.setDeptCode(StringUtils.leftPad(deptCode, 6, "0"));
					}else {
						d.setDeptCode(StringUtils.leftPad(deptCode, 6, "0"));
					}
					d.setParentId(StringUtils.isBlank(js.getString("PID"))?Constant.GUID_ZERO:js.getString("PID").replaceAll("-", ""));
					saveDepts.add(d);
			    }
				if(CollectionUtils.isNotEmpty(saveDepts)){
					log.info("------- 更新" + saveDepts.size() + "个" + mark + "部门数据 ,学校中部门数据为空的" + invalidate + "个 -------");
					baseSyncSaveService.saveDept(saveDepts.toArray(new Dept[saveDepts.size()]));
				}
			}
			RedisUtils.set("syncdata.lydy.dept." + mark + ".time", time);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
    
	@Override
	public void saveSemester(String interfaceName, String apiCode, String mark) throws Exception {
		log.info("------- 开始同步" + mark + "学年学期");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.dept." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);
			String cc = null;
			cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
			int count = 0;
			while (StringUtils.equals("ERROR", cc) && count < maxCount) {
				count++;
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
				log.info("------- 获取不到数据，第" + count + "次重新获取");
			}
			if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
				log.info("------- 同步学年学期结束");
				return;
			}	
			List<Semester> allSemester = SUtils.dt(semesterRemoteService.findAll(), new TR<List<Semester>>() {});
			Map<String, Semester> acadSemeMap = new HashMap<String, Semester>();
			for (Semester semester : allSemester) {
			  String key = semester.getAcadyear()+"-"+String.valueOf(semester.getSemester());
		      acadSemeMap.put(key, semester);
			}
				JSONArray array = Json.parseArray(cc);
				log.info("------------学年学期总数：" + array.size());
				List<Semester> semesters = new ArrayList<Semester>();
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String key = js.getString("XNMC")+"-"+(js.getString("XQMC").equals("上")?1:2);
					Semester se;
					String workBegin;
					String workEnd;
					String semesterBegin;
					String semesterEnd;
					String KSRQ = js.getString("KSRQ");
					String JSRQ = js.getString("JSRQ");
					sdf.applyPattern("yyyyMMdd");
					if(acadSemeMap.get(key) != null) {
						se = acadSemeMap.get(key);
					}else {
						se = new Semester();
						se.setId(UuidUtils.generateUuid());
						se.setCreationTime(new Date());
						se.setModifyTime(new Date());
						//取数据库中默认的值
						se.setEventSource(0);
						se.setIsDeleted(0);
						se.setEduDays(5);
						se.setAmPeriods(4);
						se.setPmPeriods(4);
						se.setNightPeriods(0);
						se.setClassHour(45);
						se.setMornPeriods(0);
						
						String registerDate ; //开学日期
						String acadYear = js.getString("XNMC");
						String start =acadYear.substring(0, acadYear.indexOf("-"));
						String end = acadYear.substring(acadYear.indexOf("-")+1);
						if(js.getString("XQMC").equals("上")) {
							workBegin = start+"0820";
							workEnd = end+"0120";
							semesterBegin = start+"0901";
							semesterEnd = end+"0115";
							registerDate = start+"0901";
						}else {
							workBegin = end+"0220";
							workEnd = end+"0720";
							semesterBegin = end+"0301";
							semesterEnd = end+"0710";
							registerDate = end+"0301";
						}
						se.setWorkBegin(sdf.parse(workBegin));
						se.setWorkEnd(sdf.parse(workEnd));
						se.setSemesterBegin(sdf.parse(semesterBegin));
						se.setSemesterEnd(sdf.parse(semesterEnd));
						se.setRegisterDate(sdf.parse(registerDate));
					}
					Integer isDeleted = 0 ;
					if(StringUtils.isNotBlank(js.getString("LJSCBZ"))) {
						isDeleted = StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0;
					}
					if(isDeleted == 1) {
						log.info("这条数据不是有效数据，跳过------"+ key);
						continue;
					}
					se.setIsDeleted(isDeleted);
					se.setAcadyear(js.getString("XNMC"));
					se.setSemester(js.getString("XQMC").equals("上")?1:2);
					if(StringUtils.isNotBlank(KSRQ) && StringUtils.isNotBlank(JSRQ)) {
						Date begin = sdf.parse(KSRQ);
						Date enDate = sdf.parse(JSRQ);
//						se.setWorkBegin(net.zdsoft.framework.utils.DateUtils.addDay(begin, -15));
//						se.setWorkEnd(net.zdsoft.framework.utils.DateUtils.addDay(enDate, 15));
						se.setWorkBegin(begin);
						se.setWorkEnd(enDate);
						se.setSemesterBegin(sdf.parse(js.getString("KSRQ")));
						se.setSemesterEnd(sdf.parse(js.getString("JSRQ")));
					}
					semesters.add(se);
				}
				log.info("------- 更新" + semesters.size() + "个学年学期数据 ");
				semesterRemoteService.saveAll(SUtils.s(semesters.toArray(new Semester[0])));
			    RedisUtils.set("syncdata.lydy.dept." + mark + ".time", time);
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步学年学期结束");
	}
	
	@Override
	public void saveFamily(String interfaceName, String apiCode) throws Exception {
		log.info("------- 开始同步家长");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.student.family.time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);
			String cc = null;

			/** 要进行验证的数据封装   --- 开始*/
			List<Family> allFamily = new ArrayList<>();
			List<Student> allStudent = new ArrayList<>();
			List<User> allUser = new ArrayList<>();
			//先得到对应的idList
			Integer pageSize = 1000;
			for (int k = 0; k < 10000; k++) {
				Set<String> familyIds = new HashSet<>();
				Set<String> studentIds = new HashSet<>();
				json.put("PAGEINDEX", k + 1);
				json.put("PAGESIZE", pageSize);
				cc = DYSynHttpRequestUtils.doPostInterface(interfaceName, apiCode, json,json.toJSONString());
				int count = 0;
				while (StringUtils.equals("ERROR", cc) && count < maxCount) {
					cc = DYSynHttpRequestUtils.doPostInterface(interfaceName, apiCode, json,json.toJSONString());
					count++;
				}
				JSONArray array = Json.parseArray(cc);
				if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount  || array.size() == 0 ) {
					break;
				}
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String familyId = js.getString("XS_JTCY_ID").replaceAll("-", "");
					String studentId = js.getString("XS_JBXX_ID").replaceAll("-", "");
					
					studentIds.add(studentId);
					familyIds.add(familyId);
				}
				allFamily.addAll(SUtils.dt(familyRemoteService.findListByIds(familyIds.toArray(new String[familyIds.size()])), new TR<List<Family>>() {}));
				allStudent.addAll(SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[studentIds.size()])), new TR<List<Student>>() {}));
				allUser.addAll(SUtils.dt(userRemoteService.findByOwnerIds(familyIds.toArray(new String[familyIds.size()])), new TR<List<User>>() {}));
			}
			Map<String, Student> allStudentMap = EntityUtils.getMap(allStudent, "id", StringUtils.EMPTY);
			Map<String, Family> allFamilyMap = EntityUtils.getMap(allFamily, "id", StringUtils.EMPTY);
			Map<String, User> allFamUserMap = EntityUtils.getMap(allUser, "username", StringUtils.EMPTY);
			Set<String> schoolIds = EntityUtils.getSet(allStudent, "schoolId");
			List<Unit> allUnits = SUtils.dt(unitRemoteService.findListByIds(schoolIds.toArray(new String[schoolIds.size()])), new TR<List<Unit>>() {});
			Map<String, Unit> allUnitMap = EntityUtils.getMap(allUnits, "id", StringUtils.EMPTY);
		/** 要进行验证的数据封装   --- 结束*/
			for (int k = 0; k < 10000; k++) {
				json.put("PAGEINDEX", k + 1);
				json.put("PAGESIZE", pageSize);
				cc = DYSynHttpRequestUtils.doPostInterface(interfaceName, apiCode, json,json.toJSONString());
				int count = 0;
				while (StringUtils.equals("ERROR", cc) && count < maxCount) {
					cc = DYSynHttpRequestUtils.doPostInterface(interfaceName, apiCode, json,json.toJSONString());
					count++;
					log.info("------- 分页:" + (k + 1) + "获取不到数据，第" + count + "次重新获取");
				}
				JSONArray array = Json.parseArray(cc);
				if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount || array.size() == 0) {
					RedisUtils.set("syncdata.lydy.student.family.time", time);
					log.info("------- 同步家长结束");
					return;
				}
				log.info("------------第" + (k + 1) + "次检索，家长总数：" + array.size());
				sdf.applyPattern("yyyyMMddHHmmss");
				int invalidate = 0;
				
				List<Family> familys = new ArrayList<Family>();
				List<User> users = new ArrayList<User>();
				
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
//					if(!js.containsKey("YHM") || StringUtils.isBlank(js.getString("YHM"))) {
//						invalidate++;
//						log.info("家长的"+js.getString("ZXX_XS_JTCY_ID") +  "的YHM为空");
//						continue;
//					}
					
					String gxsj = js.getString("GXSJ");
					if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
						time = gxsj;
					}
					String familyId = js.getString("XS_JTCY_ID").replaceAll("-", "");
					Family family;
					if(allFamilyMap.get(familyId) != null) {
						family = allFamilyMap.get(familyId);
					}else {
						family = new Family();
						family.setId(familyId);
						family.setCreationTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
						family.setEventSource(0);
						family.setIsGuardian(0);
						family.setOpenUserStatus(1);
					}
					if(allStudentMap.get(js.getString("XS_JBXX_ID")) == null) {
						invalidate++;
						log.info("学生的"+js.getString("XS_JBXX_ID") + "id不存在");
						continue;
					}
					family.setStudentId(js.getString("XS_JBXX_ID"));
					family.setSchoolId(allStudentMap.get(js.getString("XS_JBXX_ID")).getSchoolId());
					family.setRelation(js.getString("GXM"));
					family.setRealName(js.getString("CYXM"));
					family.setLinkPhone(js.getString("LXDH"));
					family.setMobilePhone(js.getString("SJDH"));
					family.setIdentityCard(js.getString("SFZJH"));
					family.setIdentitycardType(js.getString("SFZJLXM"));
					String birthday = js.getString("CSNY");
					if(StringUtils.isNotBlank(birthday)) {
						family.setBirthday(DateUtils.parseDate(js.getString("CSNY"), "yyyyMM"));
					}
					
					String nation = js.getString("MZM");
					if(StringUtils.isBlank(nation)) {
						family.setNation("01");
					}else {
						if("97".equals(js.getString("MZM"))) {
							family.setNation("99");
						}else {
							family.setNation(StringUtils.leftPad(js.getString("MZM"), 2, "0"));
						}
					}
					family.setLinkAddress(js.getString("XZZ"));
					family.setModifyTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
					family.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
					familys.add(family);
					//增加对应的家长用户 base_user 
					String userName = js.getString("YHM");
					if(StringUtils.isNotBlank(userName)) {
						User ur;
						if(allFamUserMap.get(userName) != null) {
							ur = allFamUserMap.get(userName);
//							ur.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
							if(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p")) {
								ur.setUserState(User.USER_MARK_LOCK);
							}
						}else {
							ur = new User();
							ur.setUsername(js.getString("YHM"));
							ur.setOwnerType(User.OWNER_TYPE_FAMILY);
							ur.setId(UuidUtils.generateUuid());
							String pwd = "12345678";
							ur.setPassword(new PWD(pwd).encode());
							ur.setCreationTime(new Date());
							ur.setIsDeleted(0);
							ur.setUserRole(2);
							ur.setEventSource(1);
							ur.setEnrollYear(0000);
							ur.setUserType(User.USER_TYPE_COMMON_USER);
							ur.setAccountId(family.getId());
							ur.setUnitId(family.getSchoolId());
							Unit unit = allUnitMap.get(family.getSchoolId());
							if(unit != null) {
								ur.setRegionCode(unit == null ? null : unit.getRegionCode());
							}else {
								invalidate++;
								log.info("学生的"+js.getString("XS_JBXX_ID") + "的单位不存在");
								continue;
							}
							ur.setUserState(1);
							ur.setIconIndex(0);
							ur.setMobilePhone(family.getMobilePhone());
							ur.setSex(family.getSex());
							ur.setRealName(family.getRealName());
							ur.setModifyTime(new Date());
							ur.setDeptId("");
							ur.setOwnerId(family.getId());
						}
						//用户状态 ZHZT
						String state = js.getString("ZHZT");
						if(StringUtils.isNotBlank(state)) {
							ur.setUserState(state.equals("true")? User.USER_MARK_NORMAL :User.USER_MARK_LOCK);
						}else {
							ur.setUserState(User.USER_MARK_LOCK);
						}
						users.add(ur);
					}
				}
				log.info("------- 更新" + familys.size() + "个家长数据，不符合" + invalidate + " -------个");
				familyRemoteService.saveAll(SUtils.s(familys.toArray(new Family[0])));
				log.info("------- 更新" + users.size() + "个用户数据 -------");
				userRemoteService.saveAll(SUtils.s(users.toArray(new User[0])));
				 try{
					 syncToPassport(users);
				 }
				 catch(Exception e){
					 log.error("------- 同步家长passport失败"+e.getMessage());
			     }
			}
			RedisUtils.set("syncdata.lydy.student.family.time", time);
		} catch (ParseException e) {
			throw e;
		}
		log.info("------- 同步家长结束");
	}
	
	
	private void syncToPassport(List<User> urs) throws PassportException {
		if (!Evn.isPassport())
			return;
		List<String> accountIds = EntityUtils.getList(urs, "accountId");
		Map<String, User> userMap = EntityUtils.getMap(urs, "accountId", null);
		Account[] accounts = PassportClient.getInstance().queryAccounts(accountIds.toArray(new String[0]));
		List<Account> as = new ArrayList<Account>();
		for (Account a : accounts) {
			if (a != null) {
				as.add(a);
			}
		}
		List<String> returnedAccountIds = EntityUtils.getList(as, "id");
		@SuppressWarnings("unchecked")
		List<String> remainedAccountIds = ListUtils.subtract(accountIds, returnedAccountIds);
		List<Account> as2 = new ArrayList<Account>();
		List<String> an2 = new ArrayList<String>();

		for (String accountId : remainedAccountIds) {
			User user = userMap.get(accountId);
			Account account = new Account();
			String userName = user.getUsername();
			account.setUsername(userName);
			account.setRealName(user.getRealName());
			account.setSex(user.getSex());
			account.setId(user.getAccountId());
			account.setModifyTime(new Date());
			account.setPassword(user.getPassword());
			
			an2.add(user.getUsername());
			as2.add(account);
		}
		Set<String> names = PassportClient.getInstance().queryExistsAccountUsernames(an2.toArray(new String[0]));
		// PassportClient.getInstance().
		List<Account> as22 = new ArrayList<Account>();
		for (Account at : as2) {
			if (names.contains(at.getUsername())) {
				log.info(at.getUsername() + "已经存在");
				continue;
			}
			as22.add(at);
		}

		Account[] ats = null;
		try {
			ats = PassportClient.getInstance().addAccounts(as22.toArray(new Account[0]));
		} catch (Exception e) {
			log.info("同步passport信息失败"+e.getMessage());
		}
		List<User> sequenceUser = new ArrayList<User>();
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
	}


	private static boolean isContainChinese(String str) {
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

	public void updateTeacherTransaction(String interfaceName, String apiCode, String mark) throws Exception{
		log.info("------- 开始同步教师异动信息");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.unit." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);
			String cc = DYSynHttpRequestUtils.doPostInterface(interfaceName, apiCode, json,json.toJSONString());
			int count = 0;
			while (StringUtils.equals("ERROR", cc) && count < maxCount) {
				count++;
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
				log.info("------- 获取不到数据，第" + count + "次重新获取");
			}
			if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
				log.info("------- 同步教师异动信息结束");
				return;
			}

			JSONArray array = Json.parseArray(cc);
			log.info("------------教师异动信息总数：" + array.size());
			sdf.applyPattern("yyyyMMddHHmmss");

			//先得到对应的idList
			Set<String> schoolIds = new HashSet<>();
			Set<String> teacherIds = new HashSet<>();
			Set<String> deptIds = new HashSet<>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String teacherId = js.getString("RYXX_ID").replaceAll("-", "");
				String oldUnitId = js.getString("YDLY_JG_ID").replaceAll("-", "");
				String oldDeptId = js.getString("YDLY_YHZ_ID");
				if(StringUtils.isNotBlank(oldDeptId)) {
					oldDeptId = js.getString("YDLY_YHZ_ID").replaceAll("-", "");
					deptIds.add(oldDeptId);
				}
				String newUnitId = js.getString("YDQX_JG_ID");
				if(StringUtils.isNotBlank(newUnitId)) {
					newUnitId = js.getString("YDQX_JG_ID").replaceAll("-", "");
					schoolIds.add(newUnitId);
				}
				String newDeptId = js.getString("YDQX_YHZ_ID");
				if(StringUtils.isNotBlank(newDeptId)) {
					newDeptId = js.getString("YDQX_YHZ_ID").replaceAll("-", "");
					deptIds.add(newDeptId);
				}
				teacherIds.add(teacherId);
				schoolIds.add(oldUnitId);
			}
			
			
			List<Teacher> allTeacher = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[teacherIds.size()])), new TR<List<Teacher>>() {});
			Map<String, List<Teacher>> allFixTeacherMap = EntityUtils.getListMap(allTeacher, "fixTeacherId", StringUtils.EMPTY);
			
			List<Dept> allDept = SUtils.dt(deptRemoteService.findListByIds(deptIds.toArray(new String[deptIds.size()])), new TR<List<Dept>>() {});
	        Map<String, Dept> deptMap = JledqSyncDataUtil.getMap(allDept, "id", StringUtils.EMPTY);
			
			List<User> allUser = SUtils.dt(userRemoteService.findByOwnerIds(teacherIds.toArray(new String[teacherIds.size()])), new TR<List<User>>() {});
			Map<String, User> allTeaUserMap = EntityUtils.getMap(allUser, "ownerId", StringUtils.EMPTY);
			
			List<Unit> allUnits = SUtils.dt(unitRemoteService.findListByIds(schoolIds.toArray(new String[schoolIds.size()])), new TR<List<Unit>>() {});
			Map<String, Unit> allUnitMap = EntityUtils.getMap(allUnits, "id", StringUtils.EMPTY);

//			List<UserDept> allUserDept = userDeptService.findAll();
//			Map<String,List<UserDept>>  useridMap = EntityUtils.getListMap(allUserDept, "userId", StringUtils.EMPTY);
			
			Map<String,Map<String,List<String>>> deptidMap = new HashMap<String,Map<String,List<String>>>();
			getUserGroupMap(interfaceName, json, allUnits, deptidMap);
			
			List<Teacher> teachers = new ArrayList<>();
			List<User> users = new ArrayList<>();
			List<UserDept> userDepts = new ArrayList<>();
			List<UserDept> deleteDepts = new ArrayList<>();
			int invalidate = 0;
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				System.out.println(js.toJSONString());
				String gxsj = js.getString("GXSJ");
				if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
					time = gxsj;
				}
				
				String teacherId = js.getString("RYXX_ID").replaceAll("-", "");
				String oldUnitId = js.getString("YDLY_JG_ID").replaceAll("-", "");
				
				String oldDeptId = js.getString("YDLY_YHZ_ID");
				if(StringUtils.isNotBlank(oldDeptId)) {
					oldDeptId = js.getString("YDLY_YHZ_ID").replaceAll("-", "");
				}
				
				String newUnitId = js.getString("YDQX_JG_ID");
				if(StringUtils.isNotBlank(newUnitId)) {
					newUnitId = js.getString("YDQX_JG_ID").replaceAll("-", "");
				}
				String newDeptId = js.getString("YDQX_YHZ_ID");
				if(StringUtils.isNotBlank(newDeptId)) {
					newDeptId = js.getString("YDQX_YHZ_ID").replaceAll("-", "");
				}
				
				//转化教师的异动类型
				String ydlbm = js.getString("YDLBM");
				String incumbencySign = "11";
				int userState = User.USER_MARK_NORMAL;
				if("1".equals(ydlbm)) {
					incumbencySign = "13"; //借调 ，unitid改变，编制不变
				}
				if("2".equals(ydlbm)) {
					incumbencySign = "2"; //转单位 ，编制也转
				}
				if("3".equals(ydlbm)) {
					incumbencySign = "1"; //离职
					userState = User.USER_MARK_LOCK;
				}
				if("4".equals(ydlbm)) {
					incumbencySign = "2"; //退休
					userState = User.USER_MARK_LOCK;
				}
				if("9".equals(ydlbm)) {
					incumbencySign = "11"; //一人多岗  这个待商量 
				}
				
				//处理多单位和多部门
				if(CollectionUtils.isNotEmpty(allFixTeacherMap.get(teacherId))) {
					List<Teacher> teachers2 = allFixTeacherMap.get(teacherId);
					Map<String, Teacher> uidMap = EntityUtils.getMap(teachers2, "unitId", StringUtils.EMPTY);
					if(uidMap.get(oldUnitId) != null) {
						Teacher teacher = uidMap.get(oldUnitId);
						if(StringUtils.isNotBlank(newUnitId) && allUnitMap.get(newUnitId) != null) {
							teacher.setUnitId(newUnitId);
						}
						//更新部门
						if(StringUtils.isNotBlank(newDeptId) && deptMap.get(newDeptId) != null) {
							teacher.setDeptId(newDeptId);
						}else {
							Map<String, List<String>> teaDeptMap = deptidMap.get(newUnitId);
							if(teaDeptMap == null ||  teaDeptMap.isEmpty()) {
								log.info("学校的部门中没有对应的教师，学校id是----------》"+newUnitId);
								teacher.setDeptId("");
							}else {
								if(CollectionUtils.isEmpty(teaDeptMap.get(teacherId))) {
									log.info("教师没有对应的部门,教师的id是----------》"+teacherId);
									teacher.setDeptId("");
								}else {
									int size = teaDeptMap.get(teacherId).size();
									teacher.setDeptId(teaDeptMap.get(teacherId).get(size-1)); //取最后一个数据
								}
							}
						}
						
						//更新教师在职状态
						teacher.setIncumbencySign(incumbencySign);
						if("2".equals(incumbencySign)) {
							teacher.setWeaveUnitId(newUnitId); //更新编制单位
						}
						
						teachers.add(teacher);
						
						if(allTeaUserMap.get(teacher.getId()) != null) {
							User user = allTeaUserMap.get(teacher.getId());
							if(StringUtils.isNotBlank(newUnitId)) {
								user.setUnitId(newUnitId);
							}else {
								user.setUserState(User.USER_MARK_LOCK);
							}
							
							user.setUserState(userState);  //用户的状态
							users.add(user);
							
//							List<UserDept> userDepts1 = useridMap.get(user.getId());
							List<UserDept> userDepts1 = null;
							if(CollectionUtils.isNotEmpty(userDepts1)) {
								Map<String, UserDept> deptIdUserDept = EntityUtils
										.getMap(userDepts1, "deptId", StringUtils.EMPTY);
								UserDept userDept = deptIdUserDept.get(oldDeptId);
								if(userDept != null) {
									if (StringUtils.isNotBlank(newDeptId)) {
										userDept.setDeptId(newDeptId);
										userDepts.add(userDept);
									}else {
										deleteDepts.add(userDept);
									}
								}else {
									if (StringUtils.isNotBlank(newDeptId)) {
										UserDept userDept2 = new UserDept();
										userDept2.setId(UuidUtils.generateUuid());
										userDept2.setDeptId(newDeptId);
										userDept2.setUserId(user.getId());
										userDepts.add(userDept2);
									}
								}
							}
						}
					}
				}
				
			}
			log.info("------- 更新" + teachers.size() + "个" + mark + "教师数据,不符合" + invalidate + " -------个");
			teacherRemoteService.saveAll(SUtils.s(teachers.toArray(new Teacher[0])));
			userRemoteService.saveAll(SUtils.s(users.toArray(new User[0])));
			log.info("------- 更新" + users.size() + "个教师异动信息 -------，不符合" + invalidate + " -------个");
//			userDeptService.saveAll(userDepts.toArray(new UserDept[0]));
			log.info("------- 同步" + userDepts.size() + "个" + mark + "教师部门对应表中 -------");
//			userDeptService.deleteAll(deleteDepts.toArray(new UserDept[0]));
			log.info("------- 同步删除" + deleteDepts.size() + "个" + mark + "教师部门对应表中 -------");
			
			RedisUtils.set("syncdata.lydy.unit." + mark + ".time", time);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步单位结束");
	}

	public void saveClassTeach(String interfaceName, String apiCode, String mark) throws Exception {
		log.info("------- 开始同步任课信息");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.unit." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);
			String cc = DYSynHttpRequestUtils.doPostInterface(interfaceName, apiCode, json,json.toJSONString());
			int count = 0;
			while (StringUtils.equals("ERROR", cc) && count < maxCount) {
				count++;
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
				log.info("------- 获取不到数据，第" + count + "次重新获取");
			}
			if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
				log.info("------- 同步任课信息结束");
				return;
			}

			JSONArray array = Json.parseArray(cc);
			log.info("------------同步任课信息总数：" + array.size());
			List<ClassTeaching> clazzTeachings = new ArrayList<>(); 
			sdf.applyPattern("yyyyMMddHHmmss");

			List<Unit> allUnits = SUtils.dt(unitRemoteService.findByUnitClass(Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>() {});
			Map<String, Unit> allUnitMap = EntityUtils.getMap(allUnits, "id", StringUtils.EMPTY);

			List<Course> allCourse = SUtils.dt(courseRemoteService.findAll(), new TR<List<Course>>() {
			});
			Map<String, Course> allCourseMap = EntityUtils.getMap(allCourse, "id", StringUtils.EMPTY);
			
			List<Teacher> teachers = SUtils.dt(teacherRemoteService.findAll(), new TR<List<Teacher>>() {
			});
			Map<String, Teacher> teacherMap = EntityUtils.getMap(teachers, "id", null);
			List<Clazz> allClazz = SUtils.dt(classRemoteService.findAll(), new TR<List<Clazz>>() {
			});
			Map<String, Clazz> allClazzMap = EntityUtils.getMap(allClazz, "id", StringUtils.EMPTY);
			
			List<ClassTeaching> allClassTeach = SUtils.dt(classTeachingRemoteService.findAll(), new TR<List<ClassTeaching>>() {
			});
			Map<String, ClassTeaching> allClassTeachMap = EntityUtils.getMap(allClassTeach, "id", StringUtils.EMPTY);
			
		    //获取学年学期的基础数据
			String semesters = DYSynHttpRequestUtils.doInterface("XNXQ", "42acb4dd-16b0-4a4e-b711-c277478821fe", json);
			Map<String, JSONObject> baseSemesterMap = new HashMap<String, JSONObject>();
			if(StringUtils.isNotBlank(semesters)) {
				JSONArray sArray = Json.parseArray(semesters);
				for (int i = 0; i < sArray.size(); i++) {
					JSONObject js = sArray.getJSONObject(i);
					baseSemesterMap.put(js.getString("XX_XNXQ_ID"), js);
				}
			}
			
			
			int invalidate = 0;
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String gxsj = js.getString("GXSJ");
				if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
					time = gxsj;
				}
				
				String classTeaId = js.getString("BZ_JSRKXX_ID").replaceAll("-", "");
				
				String schoolId = js.getString("XXXX_ID").replaceAll("-", "");
				if(allUnitMap.get(schoolId) == null) {
					invalidate++;
					log.info(classTeaId+"学校不存在");
					continue;
				}
				
				String courseId = js.getString("BZ_KCXX_KCDMB_ID").replaceAll("-", "");
				if(allCourseMap.get(courseId) == null) {
					invalidate++;
					log.info(classTeaId+"科目不存在");
					continue;
				}
				
				String clazzId = js.getString("BJXX_ID").replaceAll("-", "");
				if(allClazzMap.get(clazzId) == null) {
					invalidate++;
					log.info(classTeaId+"班级不存在");
					continue;
				}
				
				String teacherId = js.getString("RYXX_ID");
				if(StringUtils.isBlank(teacherId) ) {
					invalidate++;
					log.info(classTeaId+"教师id为空");
					continue;
				}else {
					teacherId = teacherId.replaceAll("-", "");
					if(teacherMap.get(teacherId) == null) {
						invalidate++;
						log.info(classTeaId+"教师不存在");
						continue;
					}
				}
				
				ClassTeaching classTeaching;
				if(allClassTeachMap.get(classTeaId) != null) {
					classTeaching = allClassTeachMap.get(classTeaId);
				}else {
					classTeaching = new ClassTeaching();
					classTeaching.setId(courseId);
					classTeaching.setEventSource(0);
					classTeaching.setCreationTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
					classTeaching.setCredit(0);
					classTeaching.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
					classTeaching.setPunchCard(0);
					classTeaching.setIsTeaCls(0);
				}
				classTeaching.setClassId(clazzId);
				classTeaching.setUnitId(schoolId);
				classTeaching.setSubjectId(courseId);
				classTeaching.setTeacherId(teacherId);
				
				String semesterId = js.getString("XX_XNXQ_ID");   // 鼎永那边的学年学期的id，需要转化成学年和学期
				if(StringUtils.isNotBlank(semesterId)) {
					JSONObject  semester = baseSemesterMap.get(String.valueOf(js.getString("XX_XNXQ_ID")));
					if(semester != null) {
						classTeaching.setAcadyear(semester.getString("XNMC"));
						classTeaching.setSemester(String.valueOf(semester.getString("XQMC").equals("上")?1:2));
					}else {
						invalidate++;
						log.info(classTeaId+"学年学期不存在");
						continue;
					}
				}else {
					invalidate++;
					log.info(classTeaId+"学年学期不存在");
					continue;
				}
				classTeaching.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
				classTeaching.setModifyTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
				
				clazzTeachings.add(classTeaching);
			}	
			log.info("------- 更新" + array.size() + "个同步任课信息 -------，不符合" + invalidate + " -------个");
			classTeachingRemoteService.saveAll(SUtils.s(clazzTeachings.toArray(new ClassTeaching[0])));
			RedisUtils.set("syncdata.lydy.unit." + mark + ".time", time);
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步任课信息结束");
	}

	/**
	 * @param string
	 * @param string2
	 * @param string3
	 */
	public void saveCourse(String interfaceName, String apiCode, String mark) throws Exception {
		log.info("------- 开始同步课程信息");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.unit." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);
			String cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
			int count = 0;
			while (StringUtils.equals("ERROR", cc) && count < maxCount) {
				count++;
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
				log.info("------- 获取不到数据，第" + count + "次重新获取");
			}
			if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
				log.info("------- 同步课程信息结束");
				return;
			}

			JSONArray array = Json.parseArray(cc);
			log.info("------------同步课程信息总数：" + array.size());
			List<Course> courses = new ArrayList<>(); 
			sdf.applyPattern("yyyyMMddHHmmss");

			List<School> allSchool = SUtils.dt(schoolRemoteService.findAll(), new TR<List<School>>() {
			});
			Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, "id", StringUtils.EMPTY);
			
			
			List<Unit> allUnits = SUtils.dt(unitRemoteService.findAll(), new TR<List<Unit>>() {});
			Map<String, Unit> allUnitMap = EntityUtils.getMap(allUnits, "id", StringUtils.EMPTY);

			List<Unit> edus = SUtils.dt(unitRemoteService.findByUnitClass(1), new TR<List<Unit>>() {
			});
			
			List<Course> allCourse = SUtils.dt(courseRemoteService.findAll(), new TR<List<Course>>() {
			});
			Map<String, Course> allCourseMap = EntityUtils.getMap(allCourse, "id", StringUtils.EMPTY);
			
			int invalidate = 0;
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String gxsj = js.getString("GXSJ");
				if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
					time = gxsj;
				}
				
				String courseId = js.getString("BZ_KCXX_KCDMB_ID").replaceAll("-", "");
				Course course;
				if(allCourseMap.get(courseId) != null) {
					course = allCourseMap.get(courseId);
				}else {
					course = new Course();
					course.setId(courseId);
					course.setEventSource(0);
					course.setCreationTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
					course.setFullMark(100);
				}
				String code = js.getString("KCDM");
				course.setSubjectCode(StringUtils.isNotBlank(code)?js.getString("KCDM"):"DE001");
				course.setSubjectName(js.getString("KCMC"));
				course.setIsUsing(StringUtils.isNotBlank(js.getString("SFTY"))
						? ((Integer.valueOf(js.getString("SFTY")) == 1) ? 0 : 1 ) :1);
				course.setSubjectType(js.getString("KCLX"));
				course.setShortName(js.getString("KCJC"));
				String unitId;
				if(js.getString("KCLX").equals("1")) {
					unitId = edus.get(0).getId();
					course.setSection("");
				}else {
					unitId = js.getString("XXXX_ID").replaceAll("-", "");
					String sections;
					if(allSchoolMap.get(unitId) != null) {
						sections = allSchoolMap.get(unitId).getSections();
						if(StringUtils.isNotBlank(sections) && sections.contains(",")) {
							String[] s1 =sections.split(",");
							course.setSection(s1[s1.length-1]);
						}
					}else {
						log.info(unitId+"单位不存在");
						continue;
					}
				}
				if(allUnitMap.get(unitId) == null) {
					invalidate++;
					log.info(unitId+"单位不存在");
					continue;
				}
				course.setUnitId(unitId);
				course.setModifyTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
				course.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
				courses.add(course);
			}	
			log.info("------- 更新" + array.size() + "个同步课程信息 -------，不符合" + invalidate + " -------个");
			courseRemoteService.saveAll(SUtils.s(courses.toArray(new Course[0])));
			RedisUtils.set("syncdata.lydy.unit." + mark + ".time", time);
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步单位结束");
		
		
	}

	/**
	 * @throws ParseException 
	 * 
	 */
	public void updateStudentTransaction(String interfaceName, String apiCode, String mark) throws Exception {
		log.info("------- 开始同步学生异动信息");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.unit." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ",time);
			String cc = DYSynHttpRequestUtils.doPostInterface(interfaceName, apiCode, json,json.toJSONString());
			int count = 0;
			while (StringUtils.equals("ERROR", cc) && count < maxCount) {
				count++;
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
				log.info("------- 获取不到数据，第" + count + "次重新获取");
			}
			if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
				log.info("------- 同步学生异动信息结束");
				return;
			}

			JSONArray array = Json.parseArray(cc);
			log.info("------------学生异动信息总数：" + array.size());
			List<Student> students = new ArrayList<>();
			List<User> users = new ArrayList<>();
			sdf.applyPattern("yyyyMMddHHmmss");

			//先得到对应的idList
			Set<String> schoolIds = new HashSet<>();
			Set<String> studentIds = new HashSet<>();
			Set<String> clazzIds = new HashSet<>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String studentId = js.getString("XSXX_ID").replaceAll("-", "");
				String schoolId = js.getString("YDQX_XX_ID");
				String clazzId =  js.getString("XBH");
				
				if(StringUtils.isNotBlank(schoolId)) {
					schoolId = schoolId.replaceAll("-", "");
					schoolIds.add(schoolId);
				}
				if(StringUtils.isNotBlank(clazzId)) {
					clazzId = clazzId.replaceAll("-", "");
					clazzIds.add(clazzId);
				}
				studentIds.add(studentId);
			}
			List<School> allSchool = SUtils.dt(schoolRemoteService.findListByIds(schoolIds.toArray(new String[schoolIds.size()])), new TR<List<School>>() {
			});
			Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, "id", StringUtils.EMPTY);
			List<Clazz> allClazz = SUtils.dt(classRemoteService.findListByIds(clazzIds.toArray(new String[clazzIds.size()])), new TR<List<Clazz>>() {
			});
			Map<String, Clazz> allClazzMap = EntityUtils.getMap(allClazz, "id", StringUtils.EMPTY);
			List<Student> allStudent = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[studentIds.size()])), new TR<List<Student>>() {
			});
			Map<String, Student> allStudentMap = EntityUtils.getMap(allStudent, "id", StringUtils.EMPTY);
			
			List<User> allUser = SUtils.dt(userRemoteService.findByOwnerIds(studentIds.toArray(new String[studentIds.size()])), new TR<List<User>>() {});
			Map<String, User> allStuUserMap = EntityUtils.getMap(allUser, "ownerId", StringUtils.EMPTY);
			
			List<Unit> allUnits = SUtils.dt(unitRemoteService.findListByIds(schoolIds.toArray(new String[schoolIds.size()])), new TR<List<Unit>>() {});
			Map<String, Unit> allUnitMap = EntityUtils.getMap(allUnits, "id", StringUtils.EMPTY);

			int invalidate = 0;
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String gxsj = js.getString("GXSJ");
				if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
					time = gxsj;
				}
				//转化学生的异动类型
				String studentId = js.getString("XSXX_ID").replaceAll("-", "");
				String ydlbm = js.getString("YDLBM");
				String nowState = "";
				int isLeaveSchool = 0;
				int userState = User.USER_MARK_NORMAL;
				if("5".equals(ydlbm)) {
					nowState = "11"; //休学
					isLeaveSchool = 1;
					userState = User.USER_MARK_LOCK;
				}
				if("6".equals(ydlbm)) {
					nowState = "21"; //转学
				}
				if("7".equals(ydlbm)) {
					nowState = "05"; //调班级
				}
				if("8".equals(ydlbm)) {
					nowState = "01"; //留级
				}
				
				if(allStudentMap.get(studentId) == null) {
					invalidate++;
					log.info("学生的"+studentId + "不存在");
					continue;
				}else {
					Student student = allStudentMap.get(studentId);
					
					//转学
					if("6".equals(ydlbm)) {
						String schoolId = js.getString("YDQX_XX_ID");
						if(StringUtils.isBlank(schoolId) || allSchoolMap.get(schoolId) == null) {
							invalidate++;
							log.info("学生"+studentId + "要转的学校不存在");
							continue;
						}	
						student.setSchoolId(schoolId.replaceAll("-", ""));
					}
					//调班级
					if("7".equals(ydlbm) || "8".equals(ydlbm)) {
						String clazzId =  js.getString("XBH");
						if(StringUtils.isBlank(clazzId) || allClazzMap.get(clazzId) == null) {
							invalidate++;
							log.info("学生"+studentId + "要转的班级不存在");
							continue;
						}
						student.setClassId(clazzId.replaceAll("-", ""));
					}
					student.setNowState(nowState);
					student.setIsLeaveSchool(isLeaveSchool);
					students.add(student);
					
					//更新user表中的数据
					if(StringUtils.isNotBlank(studentId)) {
						if(allStuUserMap.get(studentId) != null) {
							User ur = allStuUserMap.get(studentId);
							ur.setUnitId(student.getSchoolId());
							Unit unit = allUnitMap.get(student.getSchoolId());
							if(unit != null) {
								ur.setRegionCode(unit == null ? null : unit.getRegionCode());
							}else {
								invalidate++;
								log.info("学生的"+js.getString("YDQX_XX_ID") + "的单位不存在");
								continue;
							}
							
							ur.setUserState(userState);
							users.add(ur);
						}else {
							invalidate++;
							log.info("学生的"+studentId + "账号不存在");
							continue;
						}
					}
				}
			}
			log.info("------- 更新" + array.size() + "个学生异动信息 -------，不符合" + invalidate + " -------个");
			userRemoteService.saveAll(SUtils.s(users.toArray(new User[0])));
			studentRemoteService.saveAll(SUtils.s(students.toArray(new Student[0])));
			RedisUtils.set("syncdata.lydy.unit." + mark + ".time", time);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步单位结束");
	}

	/**
	 * @param string
	 * @param string2
	 * @param string3
	 * @throws ParseException 
	 */
	public void saveEduUnit(String interfaceName, String apiCode, String mark) throws Exception {
		log.info("------- 开始同步" + mark + "单位");
		System.out.println("------- 开始同步" + mark + "教育局单位");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.unit." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			json.put("GXSJ", time);
			String cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
			int count = 0;
			while (StringUtils.equals("ERROR", cc) && count < maxCount) {
				count++;
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
				log.info("------- 获取不到数据，第" + count + "次重新获取");
			}
			if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
				log.info("------- 同步教育局单位" + mark + "结束");
				System.out.println("------- 同步教育局单位" + mark + "结束");
				return;
			}
			List<Unit> saveUnits = new ArrayList<>();
			List<EduInfo> eduInfos = new ArrayList<>();
			JSONArray array = Json.parseArray(cc);
			log.info("------------" + mark + "单位总数：" + array.size());
			sdf.applyPattern("yyyyMMddHHmmss");
			
			List<Unit> allUnits = SUtils.dt(unitRemoteService.findByUnitClass(Unit.UNIT_CLASS_EDU), new TR<List<Unit>>() {});
			Map<String, Unit> allUnitMap = EntityUtils.getMap(allUnits, Unit::getId);
			Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				String gxsj = doChangeGxsj(js.getString("GXSJ"));
				if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
					time = gxsj;
				}
				String schoolId = js.getString(mark + "_JBXX_ID").replaceAll("-", "");
				Unit u;
//				EduInfo eduInfo;
//				----------封装单位
				if(MapUtils.isNotEmpty(allUnitMap) &&  allUnitMap.get(schoolId) != null) {
					u = allUnitMap.get(schoolId);
				}else {
					u = new Unit();
					u.setId(schoolId);
					u.setCreationTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
					u.setUnitClass(Unit.UNIT_CLASS_EDU);
					//添加到base_eduInfo 
//					eduInfo = new EduInfo();
//					eduInfo.setId(u.getId());
//					eduInfo.setNationPoverty(0);
//					eduInfo.setIsAutonomy(0);
//					eduInfo.setIsFrontier(0);
//					eduInfo.setEduCode(u.getRegionCode());
//					eduInfo.setCreationTime(u.getCreationTime());
//					eduInfo.setModifyTime(u.getModifyTime());
//					eduInfo.setIsDeleted(0);
//					eduInfos.add(eduInfo);
				}
				u.setUnitName(js.getString("JGMC"));
				u.setRegionCode(StringUtils.isNotBlank(js.getString("DYSKID"))?js.getString("DYSKID").substring(0, 6):"330825");
				u.setModifyTime(StringUtils.isNotBlank(gxsj)?sdf.parse(gxsj):new Date());
				u.setIsDeleted(StringUtils.equalsIgnoreCase(js.getString("LJSCBZ"), "p") ? 1 : 0);
				String regionCode = u.getRegionCode();
				if(StringUtils.isNotBlank(js.getString("PARENTID")) && !schoolId.equals(js.getString("PARENTID"))) {
					u.setParentId(js.getString("PARENTID"));
				}else{
					u.setParentId(topUnit.getId());
				}
				u.setUnitType(Unit.UNIT_EDU_SUB);
				Long max = RedisUtils.incrby("syncdata.lydy.unit.unioncode.max.regioncode." + u.getRegionCode(), 1);
				String unionCode = js.getString("JGDM");
				if(StringUtils.isNotBlank(unionCode)){
					u.setUnionCode(unionCode);
				}else{
					u.setUnionCode(u.getRegionCode());
				}
				u.setRegionLevel(
						StringUtils.endsWith(regionCode, "0000") ? 2 : StringUtils.endsWith(regionCode, "00") ? 3 : 4);
				
				u.setRootUnitId(topUnit.getId());
				saveUnits.add(u);
			}
			log.info("------- 更新" + saveUnits.size() + "个" + mark + "单位数据 -------");
			System.out.println("同步教育局数量------------- 更新" + saveUnits.size() + "个" + mark + "单位数据 -------" );
			if(CollectionUtils.isNotEmpty(saveUnits)){
				baseSyncSaveService.saveUnit(saveUnits.toArray(new Unit[saveUnits.size()])); 
			}
//			eduInfoRemoteService.saveAll(SUtils.s(eduInfos.toArray(new EduInfo[0])));
			RedisUtils.set("syncdata.lydy.unit." + mark + ".time", time);
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步单位结束");
		System.out.println("------- 同步教育局单位" + mark + "结束");
	}

	/**
	 * @param u
	 * @param d
	 * @param t
	 * @return
	 */
	protected User addDefaultAdminUser(Unit u, Dept d, Teacher t) {
		User ur = new User();
		ur.setUsername("bj_" + System.currentTimeMillis() + "admin");
		ur.setOwnerType(User.OWNER_TYPE_TEACHER);
		ur.setRegionCode(u.getRegionCode());
		ur.setId("A" + StringUtils.substring(u.getId(), 1));
		ur.setUnitId(u.getId());
		ur.setCreationTime(new Date());
		ur.setIsDeleted(0);
		ur.setUserState(1);
		// 密码不做更新
		User oldUser = User.dc(userRemoteService.findOneById(ur.getId()));
		if (oldUser != null) {
			ur.setPassword(oldUser.getPassword());
		} else {
			ur.setPassword(
					StringUtils.substring(StringUtils.substringAfter(ur.getUsername(), "bj_"), 0, 3) + "123");
		}

		ur.setUserRole(2);
		ur.setIconIndex(0);
		ur.setRegionCode(u.getRegionCode());
		ur.setSex(t.getSex());
		ur.setRealName("管理员");
		ur.setAccountId("C" + StringUtils.substring(ur.getId(), 1));
		ur.setModifyTime(new Date());
		ur.setEventSource(1);
		ur.setRoleType(0);
		ur.setEnrollYear(0000);
		ur.setDeptId(d.getId());
		ur.setOwnerId(t.getId());
		ur.setUserType(User.USER_TYPE_UNIT_ADMIN);
		return ur;
	}

	/**
	 * @param teachers
	 * @param u
	 * @param d
	 * @return
	 */
	protected Teacher doDefaultAdminTea(List<Teacher> teachers, Unit u, Dept d) {
		Teacher t = new Teacher();
		t.setTeacherName("管理员");
		t.setTeacherCode("000000");
		t.setId("B" + StringUtils.substring(u.getId(), 1));
		t.setUnitId(u.getId());
		t.setIncumbencySign("11");
		t.setDeptId(d.getId());
		t.setSex(McodeDetail.SEX_MALE);
		t.setCreationTime(new Date());
		t.setIsDeleted(0);
		t.setModifyTime(new Date());
		t.setEventSource(1);
		teachers.add(t);
		return t;
	}

	/**
	 * @param depts
	 * @param allDeptMap
	 * @param u
	 * @return
	 */
	private Dept addDefaultDept(List<Dept> depts, Map<String, Dept> allDeptMap, Unit u) {
		Dept d = new Dept();
		d.setDeptName("默认部门");
		d.setDeptCode("000001");
		d.setDeptType(1);
		d.setUnitId(u.getId());
		d.setCreationTime(new Date());
//		d.setIsDefault(0);
		d.setIsDeleted(0);
		d.setModifyTime(new Date());
		d.setEventSource(1);
		d.setParentId(Constant.GUID_ZERO);
		d.setInstituteId(Constant.GUID_ZERO);
		d.setId("D" + StringUtils.substring(u.getId(), 1));

		// 教研组标识、部门负责人、分管领导使用原有数据
		if(!allDeptMap.isEmpty()) {
			Dept oldDept = allDeptMap.get(d.getId());
			if (oldDept != null) {
				d.setLeaderId(oldDept.getLeaderId());
				d.setTeacherId(oldDept.getTeacherId());
				d.setDeptType(oldDept.getDeptType());
			}
		}

		depts.add(d);
		return d;
	}

	/**
	 * @param roles
	 * @param roles1
	 * @param u
	 */
	private void addDefaultRole(List<Role> roles, List<Role> roles1, Unit u) {
		//角色的添加和修改
		List<Role> unitRoles = SUtils.dt(roleRemoteService.findByUnitId(u.getId()), new TR<List<Role>>() {
		});
		if (CollectionUtils.isEmpty(unitRoles)) {
			for (Role role : roles1) {
				Role r = EntityUtils.copyProperties(role, Role.class);
				r.setId(UuidUtils.generateUuid());
				r.setUnitId(u.getId());
//							r.setName("");  ///这个角色的名字不用？？？ 数据库中是not null
				roles.add(r);
			}
		}
	}

	@Override
	public void saveTeacherInfo(String interfaceName, String apiCode, String mark) throws Exception {
		log.info("------- 开始同步" + mark + "教师");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.unit." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = sdf.parse(time);
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.SECOND, 1);
			date = c.getTime();
			json.put("GXSJ", sdf.format(date));
			//获得所有的学校
			List<Teacher> allTeacher = SUtils.dt(teacherRemoteService.findAll(), new TR<List<Teacher>>() {
			});
			// 过滤掉类型不一样的
			allTeacher = EntityUtils.filter(allTeacher, new EntityUtils.Filter<Teacher>() {
				@Override
				public boolean doFilter(Teacher teacher) {
					return teacher.getIsDeleted() != 0;
				}
			});
			Map<String, Teacher> allTeacherMap = EntityUtils.getMap(allTeacher, "id", null);
			Set<String> allTeaId = EntityUtils.getSet(allTeacher, "id");
			//超过1000的情况
			List<String> teList;
			if (CollectionUtils.isNotEmpty(allTeaId)) {
	            if ( allTeaId.size() > 1000 ) {
	                List<String> allTeaId1 = Lists.newArrayList(allTeaId);
	                int loopNumber = allTeaId.size()/1000;
	                for (int i=0; i<loopNumber; i++ ) {
	                	teList = allTeaId1.subList(i * 1000, (i+1)*1000);
	                    if ( i+1 == loopNumber &&  allTeaId1.size() -(1000 * loopNumber) > 0) {
	                    	teList = allTeaId1.subList((i+1) * 1000, allTeaId1.size());
	                    }
	                    doSaveTeacherInfo(interfaceName, apiCode, mark, json, time, sdf, allTeacherMap, teList);
	                }
	            } else {
	            	teList = Lists.newArrayList(allTeaId);
	            	doSaveTeacherInfo(interfaceName, apiCode, mark, json, time, sdf, allTeacherMap, teList);
	            }
	        }
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步教师附属信息结束");
	}

	/**
	 * @param mark
	 * @param sdf
	 * @param allTeacherMap
	 * @param array
	 * @return
	 * @throws ParseException
	 */

	//添加学校的附属信息
	@Override
	public void saveSchoolInfo(String interfaceName, String apiCode, String mark,Integer type) throws ParseException {
		log.info("------- 开始同步" + mark + "单位");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.unit." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = sdf.parse(time);
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.SECOND, 1);
			date = c.getTime();
			json.put("GXSJ", sdf.format(date));
			//获得所有的学校
			List<Unit> allSchool = SUtils.dt(unitRemoteService.findByUnitClass(2), new TR<List<Unit>>() {
			});
			// 过滤掉类型不一样的
			allSchool = EntityUtils.filter(allSchool, new EntityUtils.Filter<Unit>() {
				@Override
				public boolean doFilter(Unit unit) {
					return unit.getUnitType() != type;
				}
			});
			Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, "id", StringUtils.EMPTY);
			
			JSONArray jsonArray = new JSONArray();
            for (Unit unit : allSchool) {
            	jsonArray.add(unit.getId());
			}
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("XX_JBXX_IDS", jsonArray);
			String cc = DYSynHttpRequestUtils.doPostInterface(interfaceName, apiCode, json,jsonObject.toJSONString());
			int count = 0;
			while (StringUtils.equals("ERROR", cc) && count < maxCount) {
				count++;
				cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
				log.info("------- 获取不到数据，第" + count + "次重新获取");
			}
			if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
				log.info("------- 同步单位" + mark + "结束");
				return;
			}
			JSONArray array = Json.parseArray(cc);
			log.info("------------" + mark + "单位总数：" + array.size());
			List<School> schools = new ArrayList<School>();
			sdf.applyPattern("yyyyMMddHHmmss");

			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				System.out.println(js.toJSONString());
				String gxsj = js.getString("GXSJ");
				if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
					time = gxsj;
				}
				String schoolId = js.getString("XX_JBXX_ID").replaceAll("-", "");
				//学校的添加和修改
				School sch;
				if(allSchoolMap.get(schoolId) != null) {
					sch = allSchoolMap.get(schoolId);
					sch.setEnglishName(js.getString("XXYWMC"));
					sch.setSchoolmaster(js.getString("XZXM"));
					sch.setLinkPhone(js.getString("LXDH"));
					sch.setPostalcode(js.getString("YZBM"));
					sch.setEmail(js.getString("DWDZXX"));
					sch.setBuildDate(StringUtils.isNotBlank(js.getString("JXNY"))?
							sdf.parse(js.getString("JXNY")):new Date());
					sch.setLegaRegistrationNumber(js.getString("FRZSH"));
					sch.setLegalPerson(js.getString("FDDBR"));
					sch.setLandCertificateNo(js.getString("TDZH"));
					if(StringUtils.isNotBlank(js.getString("XXJD"))) {
						sch.setLongitude(Double.valueOf(js.getString("XXJD")));
					}
					if(StringUtils.isNotBlank(js.getString("XXWD"))) {
						sch.setLatitude(Double.valueOf(js.getString("XXWD")));
					}
					sch.setEventSource(1);
				}else {
					log.info("------- 更新学校数据" + mark + "失败");
					return;
				 }
				schools.add(sch);
			}
			log.info("------- 更新" + array.size() + "个" + mark + "单位数据 -------");
			schoolRemoteService.saveAll(SUtils.s(schools.toArray(new School[0])));
			RedisUtils.set("syncdata.lydy.unit." + mark + ".time", time);
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步学校附属信息结束");
	}

	@Override
	public void saveStudentInfo(String interfaceName, String apiCode, String mark) throws Exception {
		log.info("------- 开始同步" + mark + "学生");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.unit." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = sdf.parse(time);
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.SECOND, 1);
			date = c.getTime();
			json.put("GXSJ", sdf.format(date));
			//获得所有的学校
			List<Student> allStudent = SUtils.dt(teacherRemoteService.findAll(), new TR<List<Student>>() {
			});
			// 过滤掉类型不一样的
			allStudent = EntityUtils.filter(allStudent, new EntityUtils.Filter<Student>() {
				@Override
				public boolean doFilter(Student student) {
					return student.getIsDeleted() != 0;
				}
			});
			Set<String> allStuId = EntityUtils.getSet(allStudent, "id");
			Map<String, Student> allStudentMap = EntityUtils.getMap(allStudent, "id", null);
			
			//超过1000的情况
			List<String> stuList;
			if (CollectionUtils.isNotEmpty(allStuId)) {
	            if ( allStuId.size() > 1000 ) {
	                List<String> allStuId1 = Lists.newArrayList(allStuId);
	                int loopNumber = allStuId.size()/1000;
	                for (int i=0; i<loopNumber; i++ ) {
	                	stuList = allStuId1.subList(i * 1000, (i+1)*1000);
	                    if ( i+1 == loopNumber &&  allStuId1.size() -(1000 * loopNumber) > 0) {
	                    	stuList = allStuId1.subList((i+1) * 1000, allStuId1.size());
	                    }
	                    doSaveStudentInfo(interfaceName, apiCode, mark, json, time, sdf, allStudentMap, stuList);
	                }
	            } else {
	            	stuList = Lists.newArrayList(allStuId);
	            	doSaveStudentInfo(interfaceName, apiCode, mark, json, time, sdf, allStudentMap, stuList);
	            }
	        }
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步学生附属信息结束");
	}

	/**
	 * 
	 * 学生 老师
	 */
	private JSONArray getJsonArray(String interfaceName, String apiCode, String mark, JSONObject json,
			List<String> stuList ,String firstParam) {
		JSONArray jsonArray = new JSONArray();
		for (String stuid : stuList) {
			jsonArray.add(stuid);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(firstParam, jsonArray);
		String cc = DYSynHttpRequestUtils.doPostInterface(interfaceName, apiCode, json,jsonObject.toJSONString());
		int count = 0;
		while (StringUtils.equals("ERROR", cc) && count < maxCount) {
			count++;
			cc = DYSynHttpRequestUtils.doInterface(interfaceName, apiCode, json);
			log.info("------- 获取不到数据，第" + count + "次重新获取");
		}
		if (StringUtils.isBlank(cc) || StringUtils.equals("ERROR", cc) || count >= maxCount) {
			log.info("------- 同步" + mark + "结束");
			return null;
		}
		JSONArray array = Json.parseArray(cc);
		return array;
	}
	
	private void doSaveTeacherInfo(String interfaceName, String apiCode, String mark, JSONObject json, String time,
			SimpleDateFormat sdf, Map<String, Teacher> allTeacherMap, List<String> teList) throws ParseException {
		JSONArray array = getJsonArray(interfaceName, apiCode, mark, json, teList,"JG_JBXX_IDS");
		if (CollectionUtils.isEmpty(array)) {
			log.info("------- 同步教师" + mark + "结束");
			return;
		}				
		log.info("------------" + mark + "教师总数：" + array.size());
		List<Teacher> teachers = getTeacherInfoList(mark, sdf, allTeacherMap, array);
		log.info("------- 更新" + array.size() + "个" + mark + "教师数据 -------");
		teacherRemoteService.saveAll(SUtils.s(teachers.toArray(new School[0])));
		RedisUtils.set("syncdata.lydy.unit." + mark + ".time", time);
	}
	
	private List<Teacher> getTeacherInfoList(String mark, SimpleDateFormat sdf, Map<String, Teacher> allTeacherMap,
			JSONArray array) throws ParseException {
		List<Teacher> teachers = new ArrayList<Teacher>();
		sdf.applyPattern("yyyyMMddHHmmss");

		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			System.out.println(js.toJSONString());
//				String gxsj = js.getString("GXSJ");
//				if (time.compareTo(gxsj) < 0) {
//					time = gxsj;
//				}
			String teacherId = js.getString("JG_JBXX_ID").replaceAll("-", "");
			//学校的添加和修改
			Teacher tea;
			if(allTeacherMap.get(teacherId) != null) {
				tea = allTeacherMap.get(teacherId);
				if(StringUtils.isNotBlank(js.getString("")))
				 tea.setBirthday(sdf.parse(js.getString("")));
				tea.setNativePlace(js.getString("JG"));
				tea.setRegionCode(js.getString("GJDQM"));
				tea.setHomeAddress(js.getString("JTZZ"));
				tea.setLinkAddress(js.getString("XZZ"));
				if(StringUtils.isNotBlank(js.getString("GZNY")))
				 tea.setWorkDate(sdf.parse(js.getString("GZNY")));
				tea.setFileNumber(js.getString("DABH"));
				tea.setEmail(js.getString("DZXX"));
				tea.setPostalcode(js.getString("YZBM"));
			}else {
				log.info("------- 更新老师数据" + mark + "失败");
				continue;
			 }
			teachers.add(tea);
		}
		return teachers;
	}
    
	private void doSaveStudentInfo(String interfaceName, String apiCode, String mark, JSONObject json, String time,
			SimpleDateFormat sdf, Map<String, Student> allStudentMap, List<String> stuList) throws ParseException {
		JSONArray array = getJsonArray(interfaceName, apiCode, mark, json, stuList,"XS_JBXX_IDS");
		if (CollectionUtils.isEmpty(array)) {
			log.info("------- 同步学生" + mark + "结束");
			return;
		}
		log.info("------------" + mark + "学生总数：" + array.size());
		List<Student> students = getStudentInfoList(mark, sdf, allStudentMap, array);
		log.info("------- 更新" + array.size() + "个" + mark + "教师数据 -------");
		studentRemoteService.saveAll(SUtils.s(students.toArray(new Student[0])));
		RedisUtils.set("syncdata.lydy.unit." + mark + ".time", time);
	}
	
	private List<Student> getStudentInfoList(String mark, SimpleDateFormat sdf, Map<String, Student> allStudentMap,
			JSONArray array) throws ParseException {
		List<Student> students = new ArrayList<Student>();
		sdf.applyPattern("yyyyMMddHHmmss");

		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			System.out.println(js.toJSONString());
//						String gxsj = js.getString("GXSJ");
//						if (time.compareTo(gxsj) < 0) {
//							time = gxsj;
//						}
			String stuId = js.getString("XS_JBXX_ID").replaceAll("-", "");
			//学校的添加和修改
			Student stu;
			if(allStudentMap.get(stuId) != null) {
				stu = allStudentMap.get(stuId);
				stu.setEnglishName(js.getString("YWXM"));
				stu.setSpellName(js.getString("XMPY"));
				stu.setOldName(js.getString("CYM"));
				if(StringUtils.isNotBlank(js.getString("CSRQ")))
				  stu.setBirthday(sdf.parse(js.getString("CSRQ")));
				stu.setRegionCode(js.getString("GJDQM"));
				stu.setHomeAddress(js.getString("JTZZ"));
				stu.setLinkPhone(js.getString("LXDH"));
				stu.setEmail(js.getString("DZXX"));
				stu.setPostalcode(js.getString("YZBM"));
				if(StringUtils.isNotBlank(js.getString("JDFSM")))
				  stu.setStudyMode(Integer.valueOf(js.getString("JDFSM"))); //就读方式 是否一致？？
				if(StringUtils.isNotBlank(js.getString("XSLYM")))
				  stu.setStudentRecruitment(Integer.valueOf(js.getString("XSLYM")));  //学生来源 是否一致？？
				stu.setIsBoarding(js.getString("SFJSSM"));
				if(StringUtils.isNotBlank(js.getString("SFDSZNM")))
				  stu.setIsSingleton(Integer.valueOf(js.getString("SFDSZNM")));  //是否独生子女 是否一致？？
				if(StringUtils.isNotBlank(js.getString("SFSGXQJYM")))
				  stu.setIsPreedu(Integer.valueOf(js.getString("SFSGXQJYM")));  //是否受过学前教育 是否一致？？
				if(StringUtils.isNotBlank(js.getString("SFLSETM")))
				  stu.setStayin(Integer.valueOf(js.getString("SFLSETM")));   //是否留守儿童
				if(StringUtils.isNotBlank(js.getString("SFGE")))
				  stu.setIsEnjoyAssistance(Integer.valueOf(js.getString("SFGE")));  //是否孤儿
				if(StringUtils.isNotBlank(js.getString("SFLSHYFZN")))
				  stu.setIdentitycardValid(js.getString("SFLSHYFZN"));  //是否烈士或优抚子女
				if(StringUtils.isNotBlank(js.getString("SFXSYBM")))
				  stu.setIsMartyrChild(Integer.valueOf(js.getString("SFXSYBM"))); //是否享受一补
				stu.setStrong(js.getString("TC"));
				stu.setRemark(js.getString("BZ"));
			}else {
				log.info("------- 更新学生数据" + mark + "失败");
				continue;
			 }
			students.add(stu);
		}
		return students;
	}
	
	//进行多单位和多部门的处理
	public void doUpdateTeacher(String interfaceName, String apiCode, String mark) throws Exception {
		log.info("------- 开始同步老师多单位，多部门信息");
		JSONObject json = new JSONObject();
		String time = RedisUtils.get("syncdata.lydy.unit." + mark + ".time");
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = sdf.parse(time);
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.SECOND, 1);
			date = c.getTime();
			json.put("GXSJ", sdf.format(date));
			
			//获取所有部门老师和部门的对应
			List<Unit> allSchool = SUtils.dt(unitRemoteService.findAll(), new TR<List<Unit>>() {});
			// 过滤掉detailedId为空 的
			allSchool = EntityUtils.filter(allSchool, new EntityUtils.Filter<Unit>() {
				@Override
				public boolean doFilter(Unit unit) {
					return unit.getIsDeleted() == 1;
				}
			});
			
			List<User> allUser = SUtils.dt(userRemoteService.findByOwnerType(User.OWNER_TYPE_TEACHER), new TR<List<User>>() {});
			Map<String, User> allTeaUserMap = EntityUtils.getMap(allUser, "ownerId", StringUtils.EMPTY);
			List<Teacher> allTeacher = SUtils.dt(teacherRemoteService.findAll(), new TR<List<Teacher>>() {});
			Map<String, Teacher> allTeacherMap = EntityUtils.getMap(allTeacher, "id", StringUtils.EMPTY);
//			Map<String, List<Teacher>> allFixTeacherMap = EntityUtils.getListMap(allTeacher, "fixTeacherId", StringUtils.EMPTY);
			
//			List<UserDept> allUserDept = userDeptService.findAll();
//			Map<String,List<UserDept>>  useridMap = EntityUtils.getListMap(allUserDept, "userId", StringUtils.EMPTY);
			
			
			List<Teacher> teachers = new ArrayList<>();
			List<User> users = new ArrayList<>();
			List<UserDept> userDepts = new ArrayList<>();
			//建立关联
            Set<String> allteacherIds = new HashSet<>();
            
            List<UserDept> deleteUserDepts = new ArrayList<>();
            List<UserDept> addUserDepts = new ArrayList<>();
            List<MoreUDTeacher> moreUDTeachers = new ArrayList<>();
			for (Unit unit2 : allSchool) {
				String cc = DYSynHttpRequestUtils.doInterface(interfaceName, "93dc4ac0-dc4c-408b-85f3-a15c4d9f51d9", json, null,unit2.getId());
				JSONArray array = Json.parseArray(cc);
				Set<String> teacherIds = new HashSet<>();
				if(CollectionUtils.isNotEmpty(array)) {
					for (int i = 0; i < array.size(); i++) {
						MoreUDTeacher moreUDTeacher = new MoreUDTeacher();
						//现在先只考虑只有一个部门
						JSONObject js = array.getJSONObject(i);
						String teacherId = js.getString("YHXX").replaceAll("-", "");
						String deptId = js.getString("BZ_YHXX_YHZDMB_ID").replaceAll("-", "");
						
						moreUDTeacher.setDeptId(deptId);
						moreUDTeacher.setUnitId(unit2.getId());
						moreUDTeacher.setTeacherId(teacherId);
						
						moreUDTeachers.add(moreUDTeacher);
						teacherIds.add(teacherId);
					}
				}
				allteacherIds.addAll(teacherIds);
			}
			log.info("------- 开始同步老师多单位，多部门信息"+allteacherIds.size());
			
			if(CollectionUtils.isNotEmpty(moreUDTeachers)) {
				Map<String, List<MoreUDTeacher>> tidMap = EntityUtils
						.getListMap(moreUDTeachers, "teacherId", StringUtils.EMPTY);
				
				for (String tid : allteacherIds) {
					Teacher teacher = allTeacherMap.get(tid);
					User user = allTeaUserMap.get(tid);
					if(user == null) {
						log.error("教师的用户数据是空，教师id是：-----------" + tid);
						continue;
					}
					List<MoreUDTeacher> moreUDTeachers2 = tidMap.get(tid);
					if(moreUDTeachers2.size() > 1) {
						//考虑多单位
						Set<String> uidList = EntityUtils.getSet(moreUDTeachers2, "unitId");
						Set<String> didList = EntityUtils.getSet(moreUDTeachers2, "deptId");
						
						Map<String, List<MoreUDTeacher>> uidMapAndTidEqus = EntityUtils
								.getListMap(moreUDTeachers2, "unitId", StringUtils.EMPTY);
						if(uidList.size() >1) {
							for (String uid : uidList) {
								 List<MoreUDTeacher> moreUDTeachers3 = uidMapAndTidEqus.get(uid);
								 
								 Set<String> deptIds = EntityUtils.getSet(moreUDTeachers3, "deptId");
								
								 //判断编制单位是否一致
								 if(teacher.getWeaveUnitId().equals(uid)) {
									 //相同用户下的多部门的处理
//									List<UserDept> userDepts1 = useridMap.get(user.getId());
									 List<UserDept> userDepts1 = null;
									//先清空之前的数据，再添加新的数据 1删除
									if(CollectionUtils.isNotEmpty(userDepts1)) {
										deleteUserDepts.addAll(userDepts1);  
									}
								 }else {
									 //新增用户信息
								 }
								 //2新增
								 for (String did : deptIds) {
									 UserDept userDept = new UserDept();
									 userDept.setId(UuidUtils.generateUuid());
									 userDept.setUserId(user.getId());
									 userDept.setDeptId(did);
									 
									 addUserDepts.add(userDept);
									 log.info("------- 开始同步老师多部门信息"+userDept.getUserId());
								 }
							}
						}else {
							//同一单位下的多部门
//							List<UserDept> userDepts1 = useridMap.get(user.getId());
							List<UserDept> userDepts1 = null;
							//先清空之前的数据，再添加新的数据 1删除
							if(CollectionUtils.isNotEmpty(userDepts1)) {
								deleteUserDepts.addAll(userDepts1);  
							}
							
                            for (String did : didList) {
								UserDept userDept = new UserDept();
								userDept.setId(UuidUtils.generateUuid());
								userDept.setUserId(user.getId());
								userDept.setDeptId(did);
								addUserDepts.add(userDept);
								log.info("------- 开始同步老师多部门信息"+userDept.getUserId());
							}
						}
					}else {
							//同一单位下的多部门
//							List<UserDept> userDepts1 = useridMap.get(user.getId());
							List<UserDept> userDepts1 = null;
							//先清空之前的数据，再添加新的数据 1删除
							if(CollectionUtils.isNotEmpty(userDepts1)) {
								deleteUserDepts.addAll(userDepts1);  
							}
							
							UserDept userDept = new UserDept();
							userDept.setId(UuidUtils.generateUuid());
							userDept.setUserId(user.getId());
							userDept.setDeptId(moreUDTeachers2.get(0).getDeptId());
							
							addUserDepts.add(userDept);
							log.info("------- 开始同步老师多部门信息"+userDept.getUserId());
					}
				}
			}
			userRemoteService.saveAll(SUtils.s(users.toArray(new User[0])));
			log.info("------- 更新" + users.size() + "个" + mark + "用户数据");
			teacherRemoteService.saveAll(SUtils.s(teachers.toArray(new Teacher[0])));
			log.info("------- 更新" + teachers.size() + "个" + mark + "用户数据");
//			userDeptService.deleteAll(deleteUserDepts.toArray(new UserDept[0]));
//			userDeptService.saveAll(addUserDepts.toArray(new UserDept[0]));
			log.info("------- 同步" + userDepts.size() + "个" + mark + "教师部门对应表中 -------");
//			 syncToPassport(users);
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		log.info("------- 同步老师多单位，多部门信息结束");
	}
	
	class MoreUDTeacher {
		private String teacherId;
		private String unitId;
		private String deptId;
		public String getTeacherId() {
			return teacherId;
		}
		public void setTeacherId(String teacherId) {
			this.teacherId = teacherId;
		}
		public String getUnitId() {
			return unitId;
		}
		public void setUnitId(String unitId) {
			this.unitId = unitId;
		}
		public String getDeptId() {
			return deptId;
		}
		public void setDeptId(String deptId) {
			this.deptId = deptId;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <O, K, V> Map<K, V> getListMap(List<O> os, String keyXpath, String valueXpath) {
		if (CollectionUtils.isEmpty(os)) {
			return new HashMap<K, V>();
		}
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("list", os);
		List<K> keys = parser
				.parseExpression("#list.![#this" + (StringUtils.isNotBlank(keyXpath) ? ("." + keyXpath) : "") + "]")
				.getValue(context, List.class);
		List<V> values;
		if (StringUtils.isBlank(valueXpath)) {
			values = parser.parseExpression("#list.![#this]").getValue(context, List.class);
		} else {
			values = parser.parseExpression("#list.![#this." + valueXpath + "]").getValue(context, List.class);
		}
		Map<K, V> map = new HashMap<K, V>();
		if (values.size() == keys.size()) {
			for (int i = 0; i < keys.size(); i++) {
				List<V> vaList = new ArrayList<V>();
				List<V> vaList2 = new ArrayList<V>();
				for (int j = 0; j < keys.size(); j++) {
					if(keys.get(i) == null || keys.get(j) == null) {
						continue;
					}
					if (keys.get(i).equals(keys.get(j)))

						vaList.add(values.get(j));

				}
				if (vaList.size() > 1)
					vaList2 = vaList;
				else {
					vaList2.add(values.get(i));
				}

				map.put(keys.get(i), (V) vaList2);
			}
		}
		return (Map<K, V>) map;
	}
	
	private String doChangeGxsj(String gxsj) {
		return StringUtils.isNotBlank(gxsj) ? gxsj.trim() : null;
	}
	
	public static void main(String[] args) {
		String ss = "21";
		System.out.println(ss.substring(1));
		int num = Integer.valueOf(ss.substring(1)) - 1 ;
		System.out.println(num);
	}
}
