package net.zdsoft.syncdata.custom.xunfei.action;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.SchtypeSection;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SchtypeSectionRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.syncdata.custom.xunfei.service.XFStudentService;
import net.zdsoft.syncdata.custom.xunfei.service.XFTeacherService;
import net.zdsoft.syncdata.custom.xunfei.service.XFUnitService;
import net.zdsoft.syncdata.entity.XFStudent;
import net.zdsoft.syncdata.entity.XFStudentEx;
import net.zdsoft.syncdata.entity.XFTeacher;
import net.zdsoft.syncdata.entity.XFUnit;
import net.zdsoft.syncdata.service.XFStudentExService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.remote.service.RoleRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/syncdata/hsxf")
@Lazy
public class HsxfSyncDataAction extends BaseAction {

	private static final Logger log = Logger.getLogger(HsxfSyncDataAction.class);

	@Autowired
	private XFUnitService xfUnitService;
	@Autowired
	private XFTeacherService xfTeacherService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private RoleRemoteService roleRemoteService;
	@Autowired
	private SchtypeSectionRemoteService schtypeSectionRemoteService;
	@Autowired
	private XFStudentService xfStudentService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private XFStudentExService xfStudentExService;

	private void putCache(String msg, String sessionId, String type) {
		RedisUtils.set("sync.data.xunfei." + type + ".session.id." + sessionId, msg, 36000);
	}

	@RequestMapping("/index")
	public String xunfeiIndex(final boolean force, ModelMap map, HttpSession httpSession, HttpServletRequest request) {
		final String sessionId = httpSession.getId();
		final int batchUnitSize = 10;
		log.info("开始同步！");
		new Thread(new Runnable() {
			@Override
			public void run() {
				doSync(sessionId);
			}

			private void doSync(final String sessionId) {
				try {
					if (!force && "1".equals(RedisUtils.get("sync.data.xunfei.state"))) {
						log.info("已经在同步，本次忽略！");
						return;
					}
					putCache("同步准备中", sessionId, "unit");
					putCache("同步准备中", sessionId, "school");
					putCache("同步准备中", sessionId, "dept");
					putCache("同步准备中", sessionId, "teacher");
					putCache("同步准备中", sessionId, "student");
					putCache("同步准备中", sessionId, "class");
					putCache("同步准备中", sessionId, "grade");
					putCache("同步准备中", sessionId, "user");
					putCache("同步准备中", sessionId, "userpt");
					putCache("同步准备中", sessionId, "teacherpt");

					putCache("获取数据源基础数据", sessionId, "unit");

					RedisUtils.set("sync.data.xunfei.state", "1");
					putCache("获取数据源基础数据", sessionId, "unit");
					List<XFUnit> xfunits = xfUnitService.findAll();
					Set<String> usernames = new HashSet<String>();

					final Set<String> regionCodes = EntityUtils.getSet(xfunits, "regionCode");
					log.info("开始获取单位数据！");
					List<Unit> units = SUtils.dt(unitRemoteService.findByUnitClassAndRegion(Unit.UNIT_CLASS_EDU,
							regionCodes.toArray(new String[0])), new TR<List<Unit>>() {
							});

					Map<String, Unit> edus = EntityUtils.getMap(units, "regionCode", null);

					putCache("数据整理转换", sessionId, "unit");
					log.info("开始获取默认角色数据！");
					List<Role> roles = SUtils.dt(roleRemoteService.findByUnitId("00000000000000000000000000000001"),
							new TR<List<Role>>() {
							});

					log.info("开始获取学校学段数据！");
					List<SchtypeSection> schoolTypeSections = SUtils.dt(schtypeSectionRemoteService.findAll(),
							new TR<List<SchtypeSection>>() {
							});
					Map<String, String> stsMap = EntityUtils.getMap(schoolTypeSections, "schoolType", "section");

					Map<String, XFUnit> unitMap = new HashMap<String, XFUnit>();
					List<Role> rs = new ArrayList<Role>();
					List<Unit> us = new ArrayList<Unit>();
					List<School> ss = new ArrayList<School>();
					List<Dept> ds = new ArrayList<Dept>();
					List<User> urs = new ArrayList<User>();
					Map<String, Grade> gradeMap = new HashMap<String, Grade>();
					Map<String, Clazz> classMap = new HashMap<String, Clazz>();
					List<Teacher> ts = new ArrayList<Teacher>();
					List<Student> sts = new ArrayList<Student>();
					List<XFStudentEx> stexs = new ArrayList<XFStudentEx>();
					int count = xfunits.size();
					int ci = 0;

					Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(getLoginInfo().getUnitId()), Unit.class);

					log.info("开始获取默认学年学期数据！");
					Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
					for (XFUnit xfunit : xfunits) {
						log.info("开始获取单位" + xfunit.getId() + "数据！[" + ci + "/" + count + "]");
						unitMap.put(xfunit.getId(), xfunit);
						Unit u = new Unit();
						u = EntityUtils.copyProperties(xfunit, u, false);
						String regionCode = u.getRegionCode();
						Unit edu = edus.get(regionCode);
						if (edu != null) {
							u.setParentId(edu.getId());
						} else {
							u.setParentId(topUnit == null ? Constant.GUID_ZERO : topUnit.getId());
						}
						u.setOrgVersion(1);
						u.setUnionCode(regionCode + StringUtils.leftPad(xfunit.getSchoolIntId() + "", 6, "0"));
						u.setUnitState(1);
						u.setAuthorized(1);
						u.setUseType(1);
						u.setUnitType(Unit.UNIT_SCHOOL_ASP);
						u.setRegionLevel(StringUtils.endsWith(regionCode, "0000") ? 3
								: StringUtils.endsWith(regionCode, "00") ? 4 : 5);
						us.add(u);

						List<Role> unitRoles = SUtils.dt(roleRemoteService.findByUnitId(u.getId()),
								new TR<List<Role>>() {
								});
						if (CollectionUtils.isEmpty(unitRoles)) {
							for (Role role : roles) {
								Role r = EntityUtils.copyProperties(role, Role.class);
								r.setId(UuidUtils.generateUuid());
								r.setUnitId(u.getId());
								rs.add(r);
							}
						}

						School sch = new School();
						sch.setSchoolName(u.getUnitName());
						sch.setId(u.getId());
						sch.setIsDeleted(u.getIsDeleted());
						sch.setEventSource(u.getEventSource());
						sch.setCreationTime(u.getCreationTime());
						sch.setModifyTime(u.getModifyTime());
						sch.setRunSchoolType(u.getRunSchoolType());
						sch.setRegionCode(u.getRegionCode());
						sch.setSchoolCode(xfunit.getSchoolIc());
						sch.setSchoolType(xfunit.getFunctionTypeCode());
						sch.setSections(stsMap.get(sch.getSchoolType()));
						ss.add(sch);

						// 部门ID，D开头
						Dept d = new Dept();
						d.setDeptName("默认部门");
						d.setDeptCode("000001");
						d.setDeptType(1);
						d.setUnitId(u.getId());
						d.setCreationTime(new Date());
						d.setIsDeleted(0);
						d.setModifyTime(new Date());
						d.setEventSource(1);
						d.setParentId(Constant.GUID_ZERO);
						d.setInstituteId(Constant.GUID_ZERO);
						d.setId("D" + StringUtils.substring(u.getId(), 1));
						ds.add(d);

						// 教师管理员ID，B开头
						Teacher t = new Teacher();
						t.setTeacherName("管理员");
						t.setTeacherCode(StringUtils.leftPad(xfunit.getSchoolIntId() + "", 6, "0"));
						t.setId("B" + StringUtils.substring(u.getId(), 1));
						t.setUnitId(u.getId());
						t.setIncumbencySign("11");
						t.setDeptId(d.getId());
						t.setSex(McodeDetail.SEX_MALE);
						t.setCreationTime(new Date());
						t.setIsDeleted(0);
						t.setModifyTime(new Date());
						t.setEventSource(1);
						ts.add(t);

						// 单位管理员用户ID，A开头
						User ur = new User();
						ur.setUsername(xfunit.getSchoolIc() + "admin");
						ur.setOwnerType(User.OWNER_TYPE_TEACHER);
						ur.setId("A" + StringUtils.substring(u.getId(), 1));
						ur.setUnitId(u.getId());
						ur.setCreationTime(new Date());
						ur.setIsDeleted(0);
						ur.setUserState(1);
						ur.setPassword(StringUtils.substring(ur.getUsername(), 0, 3) + "123");
						ur.setUserRole(2);
						ur.setIconIndex(0);
						ur.setRegionCode(xfunit.getRegionCode());
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
						urs.add(ur);

						String time = RedisUtils.get("datasync.xunfei.unit.id." + xfunit.getId() + ".student.time");
						if (StringUtils.isBlank(time)) {
							time = "1900-01-01 00:00:00";
						}
						List<XFStudent> xfStudents = xfStudentService.findAll(xfunit.getSchoolIc(), time);

						String maxTime = time;

						for (XFStudent xs : xfStudents) {
							if (maxTime.compareTo(xs.getUpdatestamp()) < 0) {
								maxTime = xs.getUpdatestamp();
							}

							String schoolId = xfunit.getSchoolIc();
							String gradeName = xs.getGradeName();
							if (StringUtils.isNotBlank(gradeName)) {
								String sectionName = StringUtils.substring(gradeName, 0, 2);
								int section = "小学".equals(sectionName) ? 1
										: "初中".equals(sectionName) ? 2 : "高中".equals(sectionName) ? 3 : 0;
								String gradeIntStr = StringUtils.substringBetween(gradeName, sectionName, "年级");
								int gradeInt = "一".equals(gradeIntStr) ? 1
										: "二".equals(gradeIntStr) ? 2
												: "三".equals(gradeIntStr) ? 3
														: "四".equals(gradeIntStr) ? 4
																: "五".equals(gradeIntStr) ? 5
																		: "六".equals(gradeIntStr) ? 6 : 0;

								String year = StringUtils.substring(semester.getAcadyear(), 0, 4);
								int y = (NumberUtils.toInt(year) - gradeInt + 1);
								String gradeId = schoolId
										+ StringUtils.leftPad("" + section + y, 32 - schoolId.length(), "0");
								Grade grade = gradeMap.get(gradeId);
								if (grade == null) {
									grade = new Grade();
									grade.setGradeName(gradeName);
									grade.setCreationTime(new Date());
									grade.setModifyTime(new Date());
									grade.setEventSource(0);
									grade.setIsDeleted(0);
									grade.setAmLessonCount(4);
									grade.setPmLessonCount(4);
									grade.setSchoolingLength(section == 1 ? 6 : 3);
									grade.setNightLessonCount(3);
									grade.setSchoolId(u.getId());
									grade.setIsGraduate(0);
									grade.setSection(section);
									grade.setOpenAcadyear(y + "-" + (y + 1));
									grade.setId(gradeId);
									gradeMap.put(grade.getId(), grade);
								}

								String className = xs.getClassName();
								String classId = RedisUtils.hget("syncdata.xunfei.class.id.school.id." + schoolId,
										grade.getSection() + URLEncoder.encode(className, "utf-8"));
								if (StringUtils.isBlank(classId)) {
									classId = UuidUtils.generateUuid();
									RedisUtils.hset("syncdata.xunfei.class.id.school.id." + schoolId,
											grade.getSection() + URLEncoder.encode(className, "utf-8"), classId);
								}

								Clazz clazz = classMap.get(classId);
								if (clazz == null) {
									clazz = new Clazz();
									clazz.setId(classId);
									clazz.setClassName(className);
									clazz.setClassCode(StringUtils.substring(grade.getOpenAcadyear(), 0, 4)
											+ StringUtils.leftPad(section + "", 2, "0")
											+ RandomStringUtils.randomNumeric(2));
									clazz.setCreationTime(new Date());
									clazz.setModifyTime(new Date());
									clazz.setCampusId(Constant.GUID_ZERO);
									clazz.setSection(grade.getSection());
									clazz.setState(0);
									clazz.setIsDeleted(0);
									clazz.setIsGraduate(0);
									clazz.setArtScienceType(0);
									clazz.setEventSource(0);
									clazz.setSchoolingLength(section == 1 ? 6 : 3);
									clazz.setGradeId(grade.getId());
									clazz.setSchoolId(grade.getSchoolId());
									clazz.setAcadyear(grade.getOpenAcadyear());
									classMap.put(classId, clazz);
								}

								Student stu = new Student();
								stu = EntityUtils.copyProperties(xs, stu, false);
								stu.setClassId(classId);
								stu.setUnitiveCode(stu.getPin());
								stu.setStudentCode(stu.getPin());
								String enrollYear = stu.getEnrollYear();
								if (StringUtils.isBlank(StringUtils.trimToEmpty(enrollYear))) {
									stu.setEnrollYear(clazz.getAcadyear());
								}
								sts.add(stu);

								XFStudentEx stex = new XFStudentEx();
								stex.setId(stu.getId());
								stex.setSchoolId(stu.getSchoolId());
								stex.setModifyTime(stu.getModifyTime());
								stex.setIsDeleted(stu.getIsDeleted());
								stexs.add(stex);
							}
						}

						ci++;
						if (ci % batchUnitSize == 0) {
							log.info("开始保存本批次单位数据！");
							putCache("更新数据" + (ci * batchUnitSize / count) + "%", sessionId, "unit");
							unitRemoteService.saveAllEntitys(SUtils.s(us.toArray(new Unit[0])));
							us.clear();
							log.info("开始保存本批次角色数据！");
							roleRemoteService.saveAllEntitys(SUtils.s(rs.toArray(new Role[0])));
							rs.clear();

							log.info("开始保存本批次学校数据！");
							schoolRemoteService.saveAllEntitys(SUtils.s(ss.toArray(new School[0])));
							putCache("更新数据" + (ci * batchUnitSize / count) + "%", sessionId, "school");
							ss.clear();

							log.info("开始保存本批次学生数据！");
							studentRemoteService.saveAllEntitys(SUtils.s(sts.toArray(new Student[0])));
							xfStudentExService.saveAllEntitys(stexs.toArray(new XFStudentEx[0]));
							putCache("更新数据" + (ci * batchUnitSize / count) + "%", sessionId, "student");
							sts.clear();
							stexs.clear();
							log.info("开始保存本批次年级数据！");
							gradeRemoteService.saveAllEntitys(SUtils.s(gradeMap.values().toArray(new Grade[0])));
							putCache("更新数据" + (ci * batchUnitSize / count) + "%", sessionId, "grade");
							gradeMap.clear();
							log.info("开始保存本批次班级数据！");
							classRemoteService.saveAllEntitys(SUtils.s(classMap.values().toArray(new Clazz[0])));
							putCache("更新数据" + (ci * batchUnitSize / count) + "%", sessionId, "class");
							classMap.clear();
							log.info("开始保存本批次部门数据！");
							deptRemoteService.saveAllEntitys(SUtils.s(ds.toArray(new Dept[0])));
							putCache("更新数据" + (ci * batchUnitSize / count) + "%", sessionId, "dept");
							ds.clear();
							log.info("开始保存本批次教师数据！");
							teacherRemoteService.saveAllEntitys(SUtils.s(ts.toArray(new Teacher[0])));
							putCache("更新数据" + (ci * batchUnitSize / count) + "%", sessionId, "teacher");
							ts.clear();
							log.info("开始保存本批次用户数据！");
							userRemoteService.saveAllEntitys(SUtils.s(urs.toArray(new User[0])));
							putCache("更新数据" + (ci * batchUnitSize / count) + "%", sessionId, "user");
							syncToPassport(urs);
							urs.clear();
						}
						RedisUtils.set("datasync.xunfei.unit.id." + xfunit.getId() + ".student.time", maxTime);
					}

					if (us.size() > 0) {
						putCache("同步结束", sessionId, "unit");
						unitRemoteService.saveAllEntitys(SUtils.s(us.toArray(new Unit[0])));
						us.clear();
					}
					if (rs.size() > 0) {
						roleRemoteService.saveAllEntitys(SUtils.s(rs.toArray(new Role[0])));
						rs.clear();
					}

					if (gradeMap.size() > 0) {
						gradeRemoteService.saveAllEntitys(SUtils.s(gradeMap.values().toArray(new Grade[0])));
						putCache("同步结束" + (ci * batchUnitSize / count) + "%", sessionId, "grade");
						gradeMap.clear();
					}
					if (classMap.size() > 0) {
						classRemoteService.saveAllEntitys(SUtils.s(classMap.values().toArray(new Clazz[0])));
						putCache("同步结束" + (ci * batchUnitSize / count) + "%", sessionId, "class");
						classMap.clear();
					}

					if (ss.size() > 0) {
						schoolRemoteService.saveAllEntitys(SUtils.s(ss.toArray(new School[0])));
						putCache("同步结束", sessionId, "school");
						ss.clear();
					}
					if (sts.size() > 0) {
						studentRemoteService.saveAllEntitys(SUtils.s(sts.toArray(new Student[0])));
						xfStudentExService.saveAllEntitys(stexs.toArray(new XFStudentEx[0]));
						putCache("同步结束" + (ci * batchUnitSize / count) + "%", sessionId, "student");
						sts.clear();
						stexs.clear();
					}
					if (ds.size() > 0) {
						deptRemoteService.saveAllEntitys(SUtils.s(ds.toArray(new Dept[0])));
						putCache("同步结束", sessionId, "dept");
						ds.clear();
					}
					if (ts.size() > 0) {
						teacherRemoteService.saveAllEntitys(SUtils.s(ts.toArray(new Teacher[0])));
						putCache("同步结束", sessionId, "teacher");
						ts.clear();
					}
					if (urs.size() > 0) {
						userRemoteService.saveAllEntitys(SUtils.s(urs.toArray(new User[0])));
						putCache("同步结束", sessionId, "user");
						syncToPassport(urs);
						urs.clear();
					}

					putCache("同步结束", sessionId, "unit");
					putCache("同步结束", sessionId, "school");
					putCache("同步结束", sessionId, "dept");
					putCache("同步结束", sessionId, "teacher");
					putCache("同步结束", sessionId, "user");
					putCache("同步结束", sessionId, "class");
					putCache("同步结束", sessionId, "student");
					putCache("同步结束", sessionId, "grade");

					count = xfunits.size();
					ci = 0;
					log.info("开始处理普通教师和用户数据！");
					Map<String, XFTeacher> xfts = new HashMap<String, XFTeacher>();
					for (XFUnit xfunit : xfunits) {
						if (StringUtils.equals(xfunit.getId(), "00000000000000000000002134011198"))
							System.out.println();
						log.info("开始处理单位" + xfunit.getId() + "的教师数据！[" + ci + "/" + count + "]");
						List<XFTeacher> xfTeachers = xfTeacherService.findAll(xfunit.getSchoolIc());
						for (XFTeacher teacher : xfTeachers) {
							String idCard = teacher.getIdentityCard();
							if (xfts.get(idCard) != null) {
								continue;
							}
							xfts.put(idCard, teacher);

							Teacher t = new Teacher();
							EntityUtils.copyProperties(teacher, t, false);
							ts.add(t);

							User ur = new User();
							String username = null;
							while (username == null || usernames.contains(username)) {
								String py = "hs_" + PinyinUtils.toHanyuPinyin(teacher.getTeacherName(), false);
								if (StringUtils.length(py) > 16) {
									py = "hs_" + PinyinUtils.toHanyuPinyin(teacher.getTeacherName(), true);
								}
								if (StringUtils.equals("hs_wuzuying", py)) {
									System.out.println();
								}
								if (StringUtils.length(py) > 16) {
									py = StringUtils.substring(py, 0, 16);
								}
								String postfix = teacher.getIdentityCard();
								if (StringUtils.isBlank(postfix)) {
									postfix = teacher.getTeacherIntId() + "";
								}
								postfix = StringUtils.substring(postfix, postfix.length() - 4);

								Pattern p = Pattern.compile("[^a-zA-Z0-9_]");
								Matcher m = p.matcher(py);
								py = m.replaceAll("");
								postfix = p.matcher(postfix).replaceAll("");
								username = py + postfix;
								username = username.toLowerCase();
								if (usernames.contains(username))
									postfix = RandomStringUtils.randomNumeric(4);
								username = (py + postfix).toLowerCase();
							}
							usernames.add(username);

							xfunit = unitMap.get(t.getUnitId());
							ur.setUsername(username);
							ur.setOwnerType(User.OWNER_TYPE_TEACHER);
							ur.setId("1" + StringUtils.substring(teacher.getId(), 1));
							ur.setUnitId(teacher.getUnitId());
							ur.setCreationTime(new Date());
							ur.setIsDeleted(0);
							ur.setUserRole(2);
							ur.setUserState(1);
							ur.setAccountId("C" + StringUtils.substring(ur.getId(), 1));
							ur.setIconIndex(0);
							ur.setSex(t.getSex());
							ur.setPassword(
									StringUtils.substring(StringUtils.substringAfter(ur.getUsername(), "hs_"), 0, 3)
											+ "123");
							ur.setRealName(teacher.getTeacherName());
							ur.setModifyTime(new Date());
							ur.setRegionCode(xfunit == null ? null : xfunit.getRegionCode());
							ur.setEventSource(1);
							ur.setEnrollYear(0000);
							ur.setDeptId(teacher.getDeptId());
							ur.setOwnerId(t.getId());
							ur.setUserType(User.USER_TYPE_COMMON_USER);
							urs.add(ur);
						}

						ci++;
						if (ci % batchUnitSize == 0) {
							log.info("开始保存本批次普通教师数据！");
							putCache("更新数据" + (ci * batchUnitSize / count) + "%", sessionId, "teacherpt");
							teacherRemoteService.saveAllEntitys(SUtils.s(ts.toArray(new Teacher[0])));
							ts.clear();
							log.info("开始保存本批次普通用户数据！");
							userRemoteService.saveAllEntitys(SUtils.s(urs.toArray(new User[0])));
							putCache("更新数据" + (ci * batchUnitSize / count) + "%", sessionId, "userpt");
							syncToPassport(urs);
							urs.clear();
						}
					}

					if (ts.size() > 0) {
						teacherRemoteService.saveAllEntitys(SUtils.s(ts.toArray(new Teacher[0])));
						putCache("同步结束", sessionId, "teacherpt");
						ts.clear();
					}
					if (urs.size() > 0) {
						userRemoteService.saveAllEntitys(SUtils.s(urs.toArray(new User[0])));
						putCache("同步结束", sessionId, "userpt");
						syncToPassport(urs);
						urs.clear();
					}

					putCache("同步结束", sessionId, "teacherpt");
					putCache("同步结束", sessionId, "userpt");

					RedisUtils.del("sync.data.xunfei.state");
				} catch (Exception e) {
					clearState();
					e.printStackTrace();
				}
			}

			private void syncToPassport(List<User> urs) throws PassportException {
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
				for (String accountId : remainedAccountIds) {
					User user = userMap.get(accountId);
					Account account = new Account();
					account.setUsername(user.getUsername());
					account.setRealName(user.getRealName());
					account.setSex(user.getSex());
					account.setId(user.getAccountId());
					account.setModifyTime(new Date());
					account.setPassword(user.getPassword());
					as2.add(account);
				}
				Account[] ats = PassportClient.getInstance().addAccounts(as2.toArray(new Account[0]));
				List<User> sequenceUser = new ArrayList<User>();
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
					userRemoteService.saveAllEntitys(SUtils.s(sequenceUser.toArray(new User[0])));
				}
			}
		}).start();

		map.put("showFramework", true);
		return "/syncdata/xunfei/xfSyncIndex.ftl";
	}

	@RequestMapping("/clearState")
	@ResponseBody
	public String clearState() {
		RedisUtils.del("sync.data.xunfei.state");
		return "true";
	}

	@RequestMapping("/syncState")
	@ResponseBody
	public String syncState(String appName, String type, HttpSession httpSession) {
		String s = RedisUtils.get("sync.data." + appName + "." + type + ".session.id." + httpSession.getId());
		return s;
	}

}
