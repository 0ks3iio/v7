package net.zdsoft.newstusys.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newstusys.constants.BaseStudentConstants;
import net.zdsoft.newstusys.entity.StudentAbnormalFlow;
import net.zdsoft.newstusys.service.StudentAbnormalFlowService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

/**
 * 学生异动
 * @author weixh
 * 2019年2月25日	
 */
@Controller
@RequestMapping("/newstusys/abnormal")
public class StudentAnormalflowAction extends BaseAction {
	@Autowired
	private StudentRemoteService StudentRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private StudentAbnormalFlowService studentAbnormalFlowService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
    private RegionRemoteService regionRemoteService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	
	@ControllerInfo("异动首页")
	@RequestMapping("/edu/index/page")
	public String index(HttpServletRequest req, ModelMap map) {
		List<Unit> units = Unit.dt(unitRemoteService.findByParentIdAndUnitClass(new String[] {getLoginInfo().getUnitId()}, Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL));
		if(CollectionUtils.isEmpty(units)) {
			return errorFtl(map, "当前单位下没有学校");
		}
		String un = StringUtils.trimToEmpty(req.getParameter("searchUnitName"));
		if(StringUtils.isNotEmpty(un)) {
			units = units.stream().filter(e->e.getUnitName().indexOf(un) != -1).collect(Collectors.toList());
		}
		if(CollectionUtils.isNotEmpty(units)) {
			Collections.sort(units, new Comparator<Unit>() {

				public int compare(Unit a, Unit b) {
					long ss = NumberUtils.toLong(a.getDisplayOrder())-NumberUtils.toLong(b.getDisplayOrder());
					if(ss > 0) {
						return 1;
					} else if(ss < 0) {
						return -1;
					}
					return 0;
				}
			});
		}
		map.put("searchUnitName", un);
		map.put("units", units);
		return "/newstusys/abnormal/eduIndex.ftl";
	}
	
	@ControllerInfo("转出首页")
	@RequestMapping("/sch/index/page")
	public String schIndex(HttpServletRequest req, ModelMap map) {
		String schoolId = StringUtils.trimToEmpty(req.getParameter("schoolId"));
		LoginInfo li = getLoginInfo();
		if (StringUtils.isEmpty(schoolId)) {
			if (li.getUnitClass() == Unit.UNIT_CLASS_SCHOOL) {
				schoolId = li.getUnitId();
			} else {
				return errorFtl(map, "请先选择要操作的学校");
			}
		}
		List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolId(schoolId), new TR<List<Grade>>() {});
		if(CollectionUtils.isEmpty(grades)) {
			return errorFtl(map, "当前学校下没有年级记录");
		}
		String gradeId = req.getParameter("gradeId");
		if(StringUtils.isEmpty(gradeId)) {
			gradeId = grades.get(0).getId();
		}
		List<Clazz> clsList = Clazz.dt(classRemoteService.findByGradeId(schoolId, gradeId, null));
		String deploy = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		map.put("deploy", deploy);
		String clsId = StringUtils.trimToEmpty(req.getParameter("clsId"));
		map.put("schoolId", schoolId);
		map.put("grades", grades);
		map.put("clsList", clsList);
		map.put("gradeId", gradeId);
		map.put("clsId", clsId);
		return "/newstusys/abnormal/schIndex.ftl";
	}
	
	@ControllerInfo("转出页")
	@RequestMapping("/stuList/page")
	public String stuList(HttpServletRequest req, ModelMap map) {
		String schoolId = StringUtils.trimToEmpty(req.getParameter("schoolId"));
		String gradeId = req.getParameter("gradeId");
		String clsId = req.getParameter("clsId");
		map.put("schoolId", schoolId);
		map.put("gradeId", gradeId);
		map.put("clsId", clsId);
		Student stu = new Student();
		stu.setIsLeaveSchool(0);
		List<Student> stus = Student.dt(StudentRemoteService.findByIdsClaIdLikeStuCodeNames(schoolId, null, new String[] {clsId}, SUtils.s(stu), null));
		if(CollectionUtils.isNotEmpty(stus)) {
			Map<String, String> clsNames = EntityUtils.getMap(
					Clazz.dt(classRemoteService
							.findByIdsSort(EntityUtils.getList(stus, Student::getClassId).toArray(new String[0]))),
					Clazz::getId, Clazz::getClassNameDynamic);
			for(Student ss : stus) {
				ss.setClassName(clsNames.get(ss.getClassId()));
			}
		}
		Collections.sort(stus, new Comparator<Student>() {

			@Override
			public int compare(Student a, Student b) {
				String classInnerCode1 = a.getClassInnerCode();
                if (StringUtils.isEmpty(a.getClassInnerCode())) {
                    classInnerCode1 = "9999999999";
                }
                String classInnerCode2 = b.getClassInnerCode();
                if (StringUtils.isEmpty(b.getClassInnerCode())) {
                    classInnerCode2 = "9999999999";
                }
                long result = NumberUtils.toLong(classInnerCode1) - NumberUtils.toLong(classInnerCode2);
                if (result == 0l) {
                	classInnerCode1 = a.getStudentCode();
	                if (StringUtils.isEmpty(a.getStudentCode())) {
	                    classInnerCode1 = "999999999999999";
	                }
	                classInnerCode2 = b.getStudentCode();
	                if (StringUtils.isEmpty(b.getStudentCode())) {
	                    classInnerCode2 = "999999999999999";
	                }
                    return NumberUtils.toLong(classInnerCode1) <= NumberUtils.toLong(classInnerCode2)?-1:1;
                }
                return result>0?1:-1;
			}
		});
		map.put("stus", stus);
		String deploy = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		map.put("deploy", deploy);
		return "/newstusys/abnormal/schStuList.ftl";
	}
	
	@ControllerInfo("转出页")
	@RequestMapping("/out/page")
	public String outIndex(HttpServletRequest req, ModelMap map) {
		String stuId = req.getParameter("studentId");
		Student stu = Student.dc(StudentRemoteService.findOneById(stuId));
		List<Clazz> cls = Clazz.dt(classRemoteService.findByIdsSort(new String[] {stu.getClassId()}));
		if (CollectionUtils.isNotEmpty(cls)) {
			stu.setClassName(cls.get(0).getClassNameDynamic());
		}
		map.put("stu", stu);
		map.put("nowDate", new Date());
		List<McodeDetail> flowTypes = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-YDLB"), new TR<List<McodeDetail>>() {});
		String[] withIn = BaseStudentConstants.getFlowTypeBelong(BaseStudentConstants.FLOWTYPE_LEAVE);
		flowTypes = BaseStudentConstants.findInUsingMcode(flowTypes, withIn);
		map.put("schoolId", stu.getSchoolId());
		map.put("abnormalType", BaseStudentConstants.FLOWTYPE_LEAVE);
		map.put("flowTypes", flowTypes);
		return "/newstusys/abnormal/stuOutPage.ftl";
	}
	
	@ControllerInfo("转出保存")
	@RequestMapping("/saveLeave")
	@ResponseBody
	public String saveLeave(StudentAbnormalFlow flow, HttpServletRequest req) {
		try {
			LoginInfo li = getLoginInfo();
			flow.setOperator(li.getUserId());
			flow.setOperateunit(li.getUnitName());
			studentAbnormalFlowService.saveLeaveStu(flow);
		} catch (RuntimeException re) {
			return error("保存失败："+re.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("保存失败！");
		}
		return success("保存成功！");
	}
	
	@ControllerInfo("转入首页")
	@RequestMapping("/in/page")
	public String inIndex(HttpServletRequest req, ModelMap map) {
		String schoolId = StringUtils.trimToEmpty(req.getParameter("schoolId"));
		String gradeId = req.getParameter("gradeId");
		String clsId = req.getParameter("clsId");
		map.put("schoolId", schoolId);
		map.put("gradeId", gradeId);
		map.put("clsId", clsId);
		String deploy = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		map.put("deploy", deploy);
		return "/newstusys/abnormal/inPage.ftl";
	}
	
	@ControllerInfo("转入学生信息页")
	@RequestMapping("/in/edit")
	public String searchStu(HttpServletRequest req, ModelMap map) {
		String stuName = req.getParameter("stuName");
		String idcard = req.getParameter("stuIdentityCard");
		String schoolId = req.getParameter("schoolId");
		List<Student> idstus = Student.dt(StudentRemoteService.findByIdentityCards(idcard));
		if(CollectionUtils.isEmpty(idstus)) {
			map.put("stuName", stuName);
			map.put("stuIdentityCard", idcard);
			map.put("schoolId", schoolId);
			List<Region> regionList = Region.dt(regionRemoteService.findByType(Region.TYPE_1));
			map.put("regionList", regionList);
			getToDatas(schoolId, map);
			return "/newstusys/abnormal/stuNew.ftl";
		}
		List<Student> stus = idstus;
//		.stream()
//				.filter(e -> StringUtils.equals(e.getStudentName(), stuName)).collect(Collectors.toList());
//		if(CollectionUtils.isEmpty(stus)) {// 匹配不上，新增学生
//			return "/newstusys/abnormal/stuNew.ftl";
//		}
		List<Student> exStus = stus.stream().filter(e->e.getIsLeaveSchool() == 0).collect(Collectors.toList());
		Student exStu = null;
		boolean canEdit = true;// 有在校生，不能转入
//		boolean notSame = false;
		if(CollectionUtils.isNotEmpty(exStus)) {
			canEdit = false;
			exStu = exStus.get(0);
		} else {
			exStus = stus.stream().filter(e->e.getIsLeaveSchool() == 1 && StringUtils.equals(e.getStudentName(), stuName)).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(exStus)) {
//				List<Student> exStus2 = exStus.stream().filter(e->StringUtils.equals(e.getStudentName(), stuName)).collect(Collectors.toList());
//				if(CollectionUtils.isNotEmpty(exStus2)) {
//					exStu = exStus2.get(0);
//				} else {
//					notSame = true;
					exStu = exStus.get(0);
					StudentAbnormalFlow exFlow = studentAbnormalFlowService.findAbnormalFlowByFlowByStuid(exStu.getId(), 
							BaseStudentConstants.getFlowTypeBelong(BaseStudentConstants.FLOWTYPE_LEAVE));
					if(exFlow != null) {
						map.put("exFlowType", exFlow.getFlowtype());
						map.put("exFlowDate", exFlow.getFlowdate());
					}
//				}
			}
		}
		if(exStu == null) {// 没有在校或离校生，新增学生
			map.put("stuName", stuName);
			map.put("stuIdentityCard", idcard);
			map.put("schoolId", schoolId);
			List<Region> regionList = Region.dt(regionRemoteService.findByType(Region.TYPE_1));
			map.put("regionList", regionList);
			getToDatas(schoolId, map);
			return "/newstusys/abnormal/stuNew.ftl";
		}
		List<Clazz> clss = Clazz.dt(classRemoteService.findByIdsSort(new String[] {exStu.getClassId()}));
		if(CollectionUtils.isNotEmpty(clss)) {
			exStu.setClassName(clss.get(0).getClassNameDynamic());
		}
		Unit un = Unit.dc(unitRemoteService.findOneById(exStu.getSchoolId()));
		if(un != null) {
			exStu.setSchoolName(un.getUnitName());
		}
		map.put("exStu", exStu);
		
		if(!canEdit) {
			return "/newstusys/abnormal/stuWarn.ftl";
		}
		getToDatas(schoolId, map);
		return "/newstusys/abnormal/stuEdit.ftl";
	}
	
	/**
	 * 获取转入需要的信息,转入类型/班级列表/flow字段封装/默认转入日期今天
	 * @param schId
	 * @param map
	 */
	private void getToDatas(String schId, ModelMap map) {
		map.put("nowDate", new Date());
		List<McodeDetail> flowTypes = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-YDLB"), new TR<List<McodeDetail>>() {});
		String[] withIn = BaseStudentConstants.getFlowTypeBelong(BaseStudentConstants.FLOWTYPE_ENTER);
		flowTypes = BaseStudentConstants.findInUsingMcode(flowTypes, withIn);
		map.put("flowTypes", flowTypes);
		
		StudentAbnormalFlow flow = new StudentAbnormalFlow();
		flow.setToSchName(Unit.dc(unitRemoteService.findOneById(schId)).getUnitName());
		flow.setSchid(schId);
		flow.setToschoolid(schId);
		Semester se = Semester.dc(semesterRemoteService.getCurrentSemester(1, schId));
		List<Clazz> clsList;
		if (se != null) {
			clsList = Clazz.dt(classRemoteService.findBySchoolIdCurAcadyear(schId, se.getAcadyear()));
			Map<String, String> gradeMap = EntityUtils.getMap(SUtils.dt(
					gradeRemoteService
							.findListByIds(EntityUtils.getList(clsList, Clazz::getGradeId).toArray(new String[0])),
					new TR<List<Grade>>() {
					}), Grade::getId, Grade::getGradeName);
			for(Clazz clazz : clsList){
				clazz.setClassNameDynamic(StringUtils.trimToEmpty(gradeMap.get(clazz.getGradeId())) + clazz.getClassName());
			}
			flow.setAcadyear(se.getAcadyear());
			flow.setSemester(se.getSemester()+"");
		} else {
			clsList = Clazz.dt(classRemoteService.findBySchoolId(schId)).stream().filter(c -> c.getIsGraduate() == 0)
					.collect(Collectors.toList());
		}
		map.put("clsList", clsList);
		map.put("flow", flow);
	}
	
	@ControllerInfo("保存离校转入")
	@RequestMapping("/saveLeaveIn")
	@ResponseBody
	public String saveLeaveIn(StudentAbnormalFlow flow) {
		try {
			LoginInfo li = getLoginInfo();
			flow.setOperator(li.getUserId());
			flow.setOperateunit(li.getUnitName());
			studentAbnormalFlowService.saveInStu(flow);
		} catch (RuntimeException re) {
			return error("保存失败："+re.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("保存失败！");
		}
		return success("保存成功！");
	}
	
}
