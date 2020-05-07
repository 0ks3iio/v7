package net.zdsoft.credit.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.GradeTeachingRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.credit.data.constant.CreditConstants;
import net.zdsoft.credit.data.dto.CredutDailyInfoDto;
import net.zdsoft.credit.data.entity.CreditDailyInfo;
import net.zdsoft.credit.data.entity.CreditExamSet;
import net.zdsoft.credit.data.entity.CreditModuleInfo;
import net.zdsoft.credit.data.entity.CreditPatchStudent;
import net.zdsoft.credit.data.entity.CreditSet;
import net.zdsoft.credit.data.entity.CreditStatLog;
import net.zdsoft.credit.data.service.CreditDailyInfoService;
import net.zdsoft.credit.data.service.CreditExamSetService;
import net.zdsoft.credit.data.service.CreditModuleInfoService;
import net.zdsoft.credit.data.service.CreditPatchStudentService;
import net.zdsoft.credit.data.service.CreditSetService;
import net.zdsoft.credit.data.service.CreditStatLogService;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;

@Controller
@RequestMapping("/exammanage/credit")
public class CreditEditAction extends BaseAction {
	
	@Autowired
	private CustomRoleRemoteService customRoleRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private CreditSetService creditSetService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private GradeTeachingRemoteService gradeTeachingRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private CreditDailyInfoService creditDailyInfoService;
	@Autowired
	private CreditExamSetService creditExamSetService;
	@Autowired
	private CreditModuleInfoService creditModuleInfoService;
	@Autowired
	private EmExamInfoService emExamInfoService;
	
	
	@RequestMapping("/register")
	@ControllerInfo(value = "学分登记")
	public String showIndex(HttpServletRequest request,ModelMap map){
		String unitId = this.getLoginInfo().getUnitId();
		String userId = this.getLoginInfo().getUserId();
		String semester = request.getParameter("semester");
		String acadyear = request.getParameter("acadyear");
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		map.put("acadyearList", acadyearList);
		if(org.apache.commons.lang3.StringUtils.isBlank(acadyear)) {
			Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
			acadyear = se.getAcadyear();
			semester = se.getSemester()+"";
		}
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("unitId", unitId);
		CreditSet set = creditSetService.findAndInit(unitId, acadyear, semester);
		if(set == null) {
			return errorFtl(map,"没有设置规则信息");
		}
		if(isAdmin(unitId, userId)) {
			// 管理员进入全部年级首页
			List<Grade> grades = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(unitId, acadyear), new TR<List<Grade>>() {});
			SortUtils.ASC(grades, "gradeCode");
			map.put("grades", grades);
			return "/exammanage/credit/register/index.ftl";
		}else {
			//班主任或任课老师直接进入录入页面
			return "/exammanage/credit/register/listIndex.ftl";
		}
	}
	
	@RequestMapping("/register/toDailyPage")
	@ControllerInfo(value = "学分登记--日常表现登记")
	public String toDailyPage(ModelMap map,HttpServletRequest request) {
		String semester = request.getParameter("semester");
		String acadyear = request.getParameter("acadyear");
		String gradeId = request.getParameter("gradeId");
		String unitId = this.getLoginInfo().getUnitId();
		String userId = this.getLoginInfo().getUserId();
		if(isAdmin(unitId, userId)) {
			map.put("hasAdmin","1");
		}else {
			map.put("hasAdmin","0");
		}
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("gradeId", gradeId);
		map.put("type","1");
		return "/exammanage/credit/register/listIndex.ftl";
	}
	
	@RequestMapping("/register/toExamPage")
	@ControllerInfo(value = "学分登记--成绩登记")
	public String toExamPage(ModelMap map,HttpServletRequest request) {
		String semester = request.getParameter("semester");
		String acadyear = request.getParameter("acadyear");
		String gradeId = request.getParameter("gradeId");
		String unitId = this.getLoginInfo().getUnitId();
		String userId = this.getLoginInfo().getUserId();
		if(isAdmin(unitId, userId)) {
			map.put("hasAdmin","1");
		}else {
			map.put("hasAdmin","0");
		}
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("gradeId", gradeId);
		map.put("type","2");
		return "/exammanage/credit/register/listIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/register/daily/save")
	@ControllerInfo(value = "学分登记--日常表现登记保存")
	public String dailySave(CredutDailyInfoDto dto,ModelMap map) {
		try {
			if(CollectionUtils.isEmpty(dto.getDtolist())) {
				return error("没有需保存数据");
			}
			String userId = this.getLoginInfo().getUserId();
			String unitId = this.getLoginInfo().getUnitId();
			dto.setUserId(userId);
			creditDailyInfoService.saveDto(unitId, dto);
		}catch (Exception e) {
			return error("保存失败");
		}
		return success("操作成功");
	}
	
	@ResponseBody
	@RequestMapping("/register/exam/save")
	@ControllerInfo(value = "学分登记--成绩登记保存")
	public String examSave(CredutDailyInfoDto dto,ModelMap map) {
		try {
			if(CollectionUtils.isEmpty(dto.getDtolist())) {
				return error("没有需保存数据");
			}
			String userId = this.getLoginInfo().getUserId();
			String unitId = this.getLoginInfo().getUnitId();
			dto.setUserId(userId);
			creditModuleInfoService.saveDto(unitId,dto);
		}catch (Exception e) {
			return error("保存失败");
		}
		return success("操作成功");
	}
	
	
	@RequestMapping("/register/daily/page")
	@ControllerInfo(value = "学分登记--日常表现登记")
	public String dailyPage(ModelMap map,HttpServletRequest request) {
		String semester = request.getParameter("semester");
		String acadyear = request.getParameter("acadyear");
		String subjectId = request.getParameter("subjectId");
		String clsTypeId = request.getParameter("clsTypeId");
		map.put("semester",semester);
		map.put("acadyear",acadyear);
		map.put("subjectId",subjectId);
		String unitId = this.getLoginInfo().getUnitId();
		String userId = this.getLoginInfo().getUserId();
		CreditSet set = creditSetService.findAndInit(unitId, acadyear, semester);
		String classId = clsTypeId.split("_")[0];
		String classType = clsTypeId.split("_")[1];
		map.put("classId",classId);
		map.put("classType",classType);
		map.put("setId",set.getId());
		map.put("isAdmin",isAdmin(unitId, userId));
		List<Student> stulist = new ArrayList<>();
		Set<String> stuIds = new HashSet<>();
		if(StringUtils.equals(classType, CreditConstants.CLASS_TYPE2)) {
			//教学班
			List<TeachClassStu> clsStuList = SUtils.dt(teachClassStuRemoteService.findByClassIds(new String[] {classId}), new TR<List<TeachClassStu>>() {});
			stuIds = EntityUtils.getSet(clsStuList, TeachClassStu::getStudentId);
			stulist = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {});
		}else {
			//教学班
			stulist = SUtils.dt(studentRemoteService.findByClassIds(classId), new TR<List<Student>>() {});
			stuIds =EntityUtils.getSet(stulist, Student::getId);
		}
		String gradeId = "";
		if(stulist != null && stulist.size() > 0) {
			Clazz cls = SUtils.dc(classRemoteService.findOneById(stulist.get(0).getClassId()), Clazz.class);
			gradeId = cls.getGradeId();

		}
		map.put("gradeId",gradeId);
		Map<String, CreditDailyInfo> infoMap = creditDailyInfoService.findMapByStuIds(acadyear,semester,subjectId,set,stuIds);
		map.put("infoMap",infoMap);
		map.put("stulist",stulist);
		map.put("setList",set.getDailySetList());
		return "/exammanage/credit/register/dailyEditDiv.ftl";
	}
	
	@Autowired
	private CreditStatLogService creditStatLogService;
	@Autowired
	private CreditPatchStudentService creditPatchStudentService;
	
	@RequestMapping("/register/exam/page")
	@ControllerInfo(value = "学分登记--成绩登记")
	public String examPage(ModelMap map,HttpServletRequest request) {
		String semester = request.getParameter("semester");
		String acadyear = request.getParameter("acadyear");
		String subjectId = request.getParameter("subjectId");
		String clsTypeId = request.getParameter("clsTypeId");
		map.put("semester",semester);
		map.put("acadyear",acadyear);
		map.put("subjectId",subjectId);
		Course course = SUtils.dc(courseRemoteService.findOneById(subjectId),Course.class);
		map.put("subFullMark",course.getFullMark()==null?100:course.getFullMark());
		String unitId = this.getLoginInfo().getUnitId();
		String userId = this.getLoginInfo().getUserId();
		CreditSet set = creditSetService.findAndInit(unitId, acadyear, semester);
		String classId = clsTypeId.split("_")[0];
		String classType = clsTypeId.split("_")[1];
		map.put("classId",classId);
		map.put("classType",classType);
		List<Student> stulist = new ArrayList<>();
		Set<String> stuIds = new HashSet<>();
		String gradeId = "";
		map.put("isAdmin",isAdmin(unitId, userId));
		if(StringUtils.equals(classType, CreditConstants.CLASS_TYPE2)) {
			//教学班
			List<TeachClassStu> clsStuList = SUtils.dt(teachClassStuRemoteService.findByClassIds(new String[] {classId}), new TR<List<TeachClassStu>>() {});
			stuIds = EntityUtils.getSet(clsStuList, TeachClassStu::getStudentId);
			stulist = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {});
			TeachClass teachClass =SUtils.dc(teachClassRemoteService.findOneById(classId), TeachClass.class);
			gradeId = teachClass.getGradeId();
		}else {
			//教学班
			stulist = SUtils.dt(studentRemoteService.findByClassIds(classId), new TR<List<Student>>() {});
			stuIds =EntityUtils.getSet(stulist, Student::getId);
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			gradeId = cls.getGradeId();
		}//2c9180846cbde7b0016cbe0364d10283,CAAFDD58D79B4C4CB8EB8FD7CBBD3986
		CreditStatLog log = creditStatLogService.findBySetIdAndGradeId(set.getId(),gradeId);
		if(log != null && StringUtils.equals(log.getHasStat(), "1")) {
			map.put("hasStat","1");
		}else {
			map.put("hasStat","0");
		}
		Map<String, CreditPatchStudent> pkStuMap = new HashMap<>();
		List<CreditPatchStudent> pkStulist = creditPatchStudentService.findListByParam(acadyear, semester, null, null, classId, subjectId);
		if(CollectionUtils.isNotEmpty(pkStulist)) {
			pkStuMap = EntityUtils.getMap(pkStulist, CreditPatchStudent::getStudentId);
		}
		map.put("pkStuMap",pkStuMap);
		List<CreditExamSet> usualSetList = creditExamSetService.findByUsualSet(set.getId(), acadyear, semester, subjectId, classId, classType);
		map.put("usualSetList",usualSetList);
		List<CreditExamSet> moudleSets = creditExamSetService.findBySetIdAndAcadyearAndSemesterAndGradeIdAndType(set.getId(), acadyear, semester, gradeId, CreditConstants.CREDIT_EXAM_TYPE_2);
		CreditExamSet moudleSet = null;
		if(CollectionUtils.isNotEmpty(moudleSets)) {
			moudleSet = moudleSets.get(0);
		}
		map.put("moudleSet",moudleSet);
		map.put("gradeId",gradeId);
		Map<String, CreditModuleInfo> infoMap = creditModuleInfoService.findMapByStuIds(acadyear,semester,subjectId,usualSetList,moudleSet,set, stuIds);
		map.put("infoMap",infoMap);
		map.put("stulist",stulist);
		map.put("set",set);
		
		return "/exammanage/credit/register/examEditDiv.ftl";
	}
	/**
     * 数据绑定
     * 
     * @param binder
     *            WebDataBinder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(2048);  
        
    }
	@ResponseBody
	@RequestMapping("/getNum")
	@ControllerInfo(value = "获取登记率")
	public String getSubNum(String gradeId,String type,String acadyear,String semester){
		try {
			JSONObject json=new JSONObject();
			String unitId = this.getLoginInfo().getUnitId();
			if(StringUtils.equals(type, "1")) {
				int num =  creditDailyInfoService.statNum(acadyear,semester,unitId, gradeId);
				json.put("num", num);
			}else if(StringUtils.equals(type, "2")) {
				int num =  creditModuleInfoService.statNum(unitId,acadyear,semester, gradeId);
				json.put("num", num);
			}else {
				int num =  creditDailyInfoService.statNum(acadyear,semester,unitId, gradeId);
				int num1 =  creditModuleInfoService.statNum(unitId,acadyear,semester, gradeId);
				json.put("num", (num1+num)/2);
			}
			return json.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
//		return returnSuccess();
	}
	
	@RequestMapping("/register/exam/moduleSet")
	@ControllerInfo(value = "学分登记--模块成绩设置")
	public String moduleSet(ModelMap map,HttpServletRequest request) {
		String semester = request.getParameter("semester");
		String acadyear = request.getParameter("acadyear");
		String gradeId = request.getParameter("gradeId");
		String unitId = this.getLoginInfo().getUnitId();
		CreditSet set = creditSetService.findAndInit(unitId, acadyear, semester);
		List<CreditExamSet> examSets = creditExamSetService.findBySetIdAndAcadyearAndSemesterAndGradeIdAndType(set.getId(),acadyear,semester,gradeId,CreditConstants.CREDIT_EXAM_TYPE_2);
		String setExamId = "";
		if(CollectionUtils.isNotEmpty(examSets)) {
			setExamId = examSets.get(0).getExamId();
		}
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		String openAcadyear = grade.getOpenAcadyear();
		int a = NumberUtils.toInt(acadyear.split("-")[1]);
		int b = NumberUtils.toInt(openAcadyear.split("-")[0]);
		String c = a-b+"";
		String gradeCode = grade.getSection()+c;
		EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
		searchDto.setSearchAcadyear(acadyear);
		searchDto.setSearchSemester(semester);
		searchDto.setSearchGradeCode(gradeCode);
		List<EmExamInfo> examlist = emExamInfoService.findExamList(null, unitId, searchDto, false);
		map.put("examlist", examlist);
		map.put("setExamId",setExamId);
		map.put("gradeId", gradeId);
		map.put("acadyear",acadyear);
		map.put("semester",semester);
		map.put("setId",set.getId());
		return "/exammanage/credit/register/moduleSetDiv.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/register/exam/moduleSet/save")
	@ControllerInfo(value = "学分登记--模块成绩设置保存")
	public String moduleSetSave(ModelMap map,HttpServletRequest request) {
		try {
			String semester = request.getParameter("semester");
			String acadyear = request.getParameter("acadyear");
			String gradeId = request.getParameter("gradeId");
			String setId = request.getParameter("setId");
			String setExamId = request.getParameter("setExamId");
			if(StringUtils.isBlank(setExamId)) {
				return error("没有需保存数据");
			}
			String userId = this.getLoginInfo().getUserId();
			String unitId = this.getLoginInfo().getUnitId();
			creditModuleInfoService.saveModuleId(unitId, acadyear,semester,gradeId,setId,setExamId, userId);
		}catch (Exception e) {
			return error("保存失败");
		}
		return success("操作成功");
	}
	
	@RequestMapping("/register/exam/usualSet")
	@ControllerInfo(value = "学分登记--平时成绩设置")
	public String usualSet(ModelMap map,HttpServletRequest request) {
		String semester = request.getParameter("semester");
		String acadyear = request.getParameter("acadyear");
		String gradeId = request.getParameter("gradeId");
		String subjectId = request.getParameter("subjectId");
		String clsTypeId = request.getParameter("clsTypeId");
		String classId = clsTypeId.split("_")[0];
		String classType = clsTypeId.split("_")[1];
		String unitId = this.getLoginInfo().getUnitId();
		CreditSet set = creditSetService.findAndInit(unitId, acadyear, semester);
		List<CreditExamSet> examSets = creditExamSetService.findByUsualSet(set.getId(),acadyear,semester,subjectId,classId,classType);
		map.put("examSets",examSets);
		map.put("setId",set.getId());
		map.put("gradeId",gradeId);
		map.put("subjectId",subjectId);
		map.put("classId",classId);
		map.put("classType",classType);
		return "/exammanage/credit/register/usualSetDiv.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/register/exam/usualSet/save")
	@ControllerInfo(value = "学分登记--平时成绩设置保存")
	public String usualSetSave(ModelMap map,HttpServletRequest request) {
		try {
			String gradeId = request.getParameter("gradeId");
			String setId = request.getParameter("setId");
			String classId = request.getParameter("classId");
			String classType = request.getParameter("classType");
			String subjectId = request.getParameter("subjectId");
			String col = request.getParameter("col");
			int maxRow = NumberUtils.toInt(col);
			if(maxRow<1) {
				return error("没有需保存数据");
			}
			String userId = this.getLoginInfo().getUserId();
			String unitId = this.getLoginInfo().getUnitId();
			creditExamSetService.saveUsualSet(unitId,userId,setId,gradeId,classId,classType,subjectId,maxRow);
		}catch (Exception e) {
			return error("保存失败");
		}
		return success("操作成功");
	}
	
	@RequestMapping("/register/exam")
	@ControllerInfo(value = "学分登记--成绩登记")
	public String examList(ModelMap map,HttpServletRequest request) {
		String semester = request.getParameter("semester");
		String acadyear = request.getParameter("acadyear");
		String subjectId = request.getParameter("subjectId");
		String clsTypeId = request.getParameter("clsTypeId");
		String unitId = this.getLoginInfo().getUnitId();
		String userId = this.getLoginInfo().getUserId();
		String teacherId = this.getLoginInfo().getOwnerId();
		CreditSet set = creditSetService.findAndInit(unitId, acadyear, semester);
		if(set == null) {
			return errorFtl(map,"未维护学分规则设置");
		}
		map.put("isAdmin",isAdmin(unitId, userId));
		if(isAdmin(unitId, userId)) {
			String gradeId = request.getParameter("gradeId");
			List<GradeTeaching> gradeTeachings = SUtils.dt(gradeTeachingRemoteService.findBySearchList(unitId, acadyear, semester, gradeId, 1),new TR<List<GradeTeaching>>() {});
			Set<String> subIds = EntityUtils.getSet(gradeTeachings, GradeTeaching::getSubjectId);
			List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])),new TR<List<Course>>(){});
			if(CollectionUtils.isEmpty(courseList)) {
				return errorFtl(map,"未维护年级课程信息");
			}
			SortUtils.ASC(courseList, "subjectCode");
			map.put("courseList", courseList);
			if(StringUtils.isBlank(subjectId)) {
				subjectId = courseList.get(0).getId();
			}
			map.put("subjectId",subjectId);
			//教学班数据
			List<TeachClass> teaClslist = SUtils.dt(teachClassRemoteService.findTeachClassListByGradeId(unitId, acadyear, semester, subjectId, gradeId), new TR<List<TeachClass>>() {});
			//行政班数据
			List<ClassTeaching> clsTealist = SUtils.dt(classTeachingRemoteService.findListByGradeIdAndSubId(acadyear, semester, unitId, gradeId,subjectId), new TR<List<ClassTeaching>>() {});
			List<Clazz> clslist = new ArrayList<>();
			Set<String> clsIds = new HashSet<>();
			for (ClassTeaching classTeaching : clsTealist) {
				if(classTeaching.getIsTeaCls() != 1) {
					clsIds.add(classTeaching.getClassId());
				}
			}
			if(clsIds.size()>0) {
				clslist = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR	<List<Clazz>>() {});
			}
			if(StringUtils.isBlank(clsTypeId)) {
				if(CollectionUtils.isNotEmpty(clslist)) {
					clsTypeId = clslist.get(0).getId() + "_" + CreditConstants.CLASS_TYPE1;
				}else if(CollectionUtils.isNotEmpty(teaClslist)) {
					clsTypeId = teaClslist.get(0).getId() + "_" +  CreditConstants.CLASS_TYPE2;
				}
			}
			map.put("clsTypeId",clsTypeId);
			map.put("clslist",clslist);
			map.put("teaClslist",teaClslist);
		}else {
			// 找到自己相关的班级
			Set<String> clsIds = new HashSet<>();
			//任课老师
			//行政班任课信息
			String str = classTeachingRemoteService.findClassTeachingList(unitId, acadyear, semester, teacherId);
			List<ClassTeaching> list1 = SUtils.dt(str, new TR<List<ClassTeaching>>() {});
			for (ClassTeaching classTeaching : list1) {
				if(classTeaching.getIsTeaCls() == 1) {
					continue;
				}
				clsIds.add(classTeaching.getClassId());
			}
			List<Clazz> clsAlllist = SUtils.dt(classRemoteService.findBySchoolIdAcadyear(unitId, acadyear),new TR<List<Clazz>>() {});
			List<Clazz> clslist = new ArrayList<>();
			Set<String> teaClsIds = new HashSet<>();
			for (Clazz clazz : clsAlllist) {
				//班主任权限或者行政班任课
				if(StringUtils.equals(clazz.getTeacherId(), teacherId)
						|| clsIds.contains(clazz.getId())) {
					clslist.add(clazz);
					if(StringUtils.equals(clazz.getTeacherId(), teacherId)) {
						teaClsIds.add(clazz.getId());
					}
				}
			}
			//教学班任课信息
			List<TeachClass> teaClslist = SUtils.dt(teachClassRemoteService.findListByTeacherId(teacherId, acadyear, semester), new TR<List<TeachClass>>() {});
			if(StringUtils.isBlank(clsTypeId)) {
				if(CollectionUtils.isNotEmpty(clslist)) {
					clsTypeId = clslist.get(0).getId() + "_" + CreditConstants.CLASS_TYPE1;
				}else if(CollectionUtils.isNotEmpty(teaClslist)) {
					clsTypeId = teaClslist.get(0).getId() + "_" +  CreditConstants.CLASS_TYPE2;
				}
			}
			map.put("teaClslist",teaClslist);
			map.put("clslist",clslist);
			map.put("clsTypeId",clsTypeId);
			Set<String> subIds = new HashSet<>();
			if(StringUtils.isNotBlank(clsTypeId)) {
				String clsId = clsTypeId.split("_")[0];
				String clsType = clsTypeId.split("_")[1];
				if(StringUtils.equals(clsType, CreditConstants.CLASS_TYPE1)) {
					List<ClassTeaching> classTeachingsList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByClassIds(acadyear, semester, clsId), new TR<List<ClassTeaching>>() {});
					if(teaClsIds.contains(clsId)) {
						subIds = EntityUtils.getSet(classTeachingsList, ClassTeaching::getSubjectId);
					}else {
						for (ClassTeaching e : classTeachingsList) {
							if(StringUtils.equals(e.getTeacherId(), teacherId)) {
								subIds.add(e.getSubjectId());
							}
						}
					}
				}else {
					TeachClass teaCls = SUtils.dc(teachClassRemoteService.findOneById(clsId), TeachClass.class);
					subIds.add(teaCls.getCourseId());
				}
			}
			if(CollectionUtils.isNotEmpty(subIds)) {
				List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])),new TR<List<Course>>(){});
				SortUtils.ASC(courseList, "subjectCode");
				if(StringUtils.isBlank(subjectId)) {
					map.put("subjectId",courseList.get(0).getId());
				}else {
					if(subIds.contains(subjectId)) {
						map.put("subjectId",subjectId);
					}else {
						map.put("subjectId",courseList.get(0).getId());
					}
				}
				map.put("courseList", courseList);
			}else {
				map.put("courseList", new ArrayList<Course>());
				map.put("subjectId","");
			}
		}
		return "/exammanage/credit/register/examEditList.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value="/checkInputPower")
	public String checkPower(String acadyear,String semester,String clsTypeId, String subjectId){
		String unitId = this.getLoginInfo().getUnitId();
		String userId = this.getLoginInfo().getUserId();
		String teacherId = this.getLoginInfo().getOwnerId();
		if(isAdmin(unitId, userId)) {
			return returnSuccess();
		}else{
			String classId = clsTypeId.split("_")[0];
			String clsType = clsTypeId.split("_")[1];
			Set<String> subIds = new HashSet<>();
			List<Clazz> clsAlllist = SUtils.dt(classRemoteService.findBySchoolIdAcadyear(unitId, acadyear),new TR<List<Clazz>>() {});
			Set<String> teaClsIds = new HashSet<>();
			for (Clazz clazz : clsAlllist) {
				//班主任权限
				if(StringUtils.equals(clazz.getTeacherId(), teacherId)) {
					teaClsIds.add(clazz.getId());
				}
			}
			if(StringUtils.equals(clsType, CreditConstants.CLASS_TYPE1)) {
				List<ClassTeaching> classTeachingsList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByClassIds(acadyear, semester, classId), new TR<List<ClassTeaching>>() {});
				if(teaClsIds.contains(classId)) {
					subIds = EntityUtils.getSet(classTeachingsList, ClassTeaching::getSubjectId);
				}else {
					for (ClassTeaching e : classTeachingsList) {
						if(StringUtils.equals(e.getTeacherId(), teacherId)) {
							subIds.add(e.getSubjectId());
						}
					}
				}
			}else {
				TeachClass teaCls = SUtils.dc(teachClassRemoteService.findOneById(classId), TeachClass.class);
				subIds.add(teaCls.getCourseId());
			}
			if(!subIds.contains(subjectId)) {
				return error("没有该科目的提交权限，不可导入！");
			}
		}
		return returnSuccess();
	}
	
	@RequestMapping("/register/daily")
	@ControllerInfo(value = "学分登记--日常表现登记")
	public String dailyList(ModelMap map,HttpServletRequest request) {
		String semester = request.getParameter("semester");
		String acadyear = request.getParameter("acadyear");
		String subjectId = request.getParameter("subjectId");
		String clsTypeId = request.getParameter("clsTypeId");
		String unitId = this.getLoginInfo().getUnitId();
		String userId = this.getLoginInfo().getUserId();
		String teacherId = this.getLoginInfo().getOwnerId();
		CreditSet set = creditSetService.findAndInit(unitId, acadyear, semester);
		if(set == null) {
			return errorFtl(map,"未维护学分规则设置");
		}
		map.put("isAdmin",isAdmin(unitId, userId));
		if(isAdmin(unitId, userId)) {
			String gradeId = request.getParameter("gradeId");
			List<GradeTeaching> gradeTeachings = SUtils.dt(gradeTeachingRemoteService.findBySearchList(unitId, acadyear, semester, gradeId, 1),new TR<List<GradeTeaching>>() {});
			Set<String> subIds = EntityUtils.getSet(gradeTeachings, GradeTeaching::getSubjectId);
			List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])),new TR<List<Course>>(){});
			if(CollectionUtils.isEmpty(courseList)) {
				return errorFtl(map,"未维护年级课程信息");
			}
			SortUtils.ASC(courseList, "subjectCode");
			map.put("courseList", courseList);
			if(StringUtils.isBlank(subjectId)) {
				subjectId = courseList.get(0).getId();
			}
			map.put("subjectId",subjectId);
			//教学班数据
			List<TeachClass> teaClslist = SUtils.dt(teachClassRemoteService.findTeachClassListByGradeId(unitId, acadyear, semester, subjectId, gradeId), new TR<List<TeachClass>>() {});
			//行政班数据
			List<ClassTeaching> clsTealist = SUtils.dt(classTeachingRemoteService.findListByGradeIdAndSubId(acadyear, semester, unitId, gradeId,subjectId), new TR<List<ClassTeaching>>() {});
			List<Clazz> clslist = new ArrayList<>();
			Set<String> clsIds = new HashSet<>();
			for (ClassTeaching classTeaching : clsTealist) {
				if(classTeaching.getIsTeaCls() != 1) {
					clsIds.add(classTeaching.getClassId());
				}
			}
			if(clsIds.size()>0) {
				clslist = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR	<List<Clazz>>() {});
			}
			if(StringUtils.isBlank(clsTypeId)) {
				if(CollectionUtils.isNotEmpty(clslist)) {
					clsTypeId = clslist.get(0).getId() + "_" + CreditConstants.CLASS_TYPE1;
				}else if(CollectionUtils.isNotEmpty(teaClslist)) {
					clsTypeId = teaClslist.get(0).getId() + "_" +  CreditConstants.CLASS_TYPE2;
				}
			}
			map.put("clsTypeId",clsTypeId);
			map.put("clslist",clslist);
			map.put("teaClslist",teaClslist);
		}else {
			//TODO 找到自己相关的班级
			Set<String> clsIds = new HashSet<>();
			//任课老师
			//行政班任课信息
			String str = classTeachingRemoteService.findClassTeachingList(unitId, acadyear, semester, teacherId);
			List<ClassTeaching> list1 = SUtils.dt(str, new TR<List<ClassTeaching>>() {});
			for (ClassTeaching classTeaching : list1) {
				if(classTeaching.getIsTeaCls() == 1) {
					continue;
				}
				clsIds.add(classTeaching.getClassId());
			}
			List<Clazz> clsAlllist = SUtils.dt(classRemoteService.findBySchoolIdAcadyear(unitId, acadyear),new TR<List<Clazz>>() {});
			List<Clazz> clslist = new ArrayList<>();
			Set<String> teaClsIds = new HashSet<>();
			for (Clazz clazz : clsAlllist) {
				//班主任权限或者行政班任课
				if(StringUtils.equals(clazz.getTeacherId(), teacherId)
						|| clsIds.contains(clazz.getId())) {
					clslist.add(clazz);
					if(StringUtils.equals(clazz.getTeacherId(), teacherId)) {
						teaClsIds.add(clazz.getId());
					}
				}
			}
			//教学班任课信息
			List<TeachClass> teaClslist = SUtils.dt(teachClassRemoteService.findListByTeacherId(teacherId, acadyear, semester), new TR<List<TeachClass>>() {});
			if(StringUtils.isBlank(clsTypeId)) {
				if(CollectionUtils.isNotEmpty(clslist)) {
					clsTypeId = clslist.get(0).getId() + "_" + CreditConstants.CLASS_TYPE1;
				}else if(CollectionUtils.isNotEmpty(teaClslist)) {
					clsTypeId = teaClslist.get(0).getId() + "_" +  CreditConstants.CLASS_TYPE2;
				}
			}
			map.put("teaClslist",teaClslist);
			map.put("clslist",clslist);
			map.put("clsTypeId",clsTypeId);
			Set<String> subIds = new HashSet<>();
			if(StringUtils.isNotBlank(clsTypeId)) {
				String clsId = clsTypeId.split("_")[0];
				String clsType = clsTypeId.split("_")[1];
				if(StringUtils.equals(clsType, CreditConstants.CLASS_TYPE1)) {
					List<ClassTeaching> classTeachingsList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByClassIds(acadyear, semester, clsId), new TR<List<ClassTeaching>>() {});
					if(teaClsIds.contains(clsId)) {
						subIds = EntityUtils.getSet(classTeachingsList, ClassTeaching::getSubjectId);
					}else {
						for (ClassTeaching e : classTeachingsList) {
							if(StringUtils.equals(e.getTeacherId(), teacherId)) {
								subIds.add(e.getSubjectId());
							}
						}
					}
				}else {
					TeachClass teaCls = SUtils.dc(teachClassRemoteService.findOneById(clsId), TeachClass.class);
					subIds.add(teaCls.getCourseId());
				}
			}
			if(CollectionUtils.isNotEmpty(subIds)) {
				List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])),new TR<List<Course>>(){});
				SortUtils.ASC(courseList, "subjectCode");
				if(StringUtils.isBlank(subjectId)) {
					map.put("subjectId",courseList.get(0).getId());
				}else {
					if(subIds.contains(subjectId)) {
						map.put("subjectId",subjectId);
					}else {
						map.put("subjectId",courseList.get(0).getId());
					}
				}
				map.put("courseList", courseList);
			}else {
				map.put("courseList", new ArrayList<Course>());
				map.put("subjectId","");
			}
			
		}
		return "/exammanage/credit/register/dailyEditList.ftl";
	}

   /**
    * 判断是否为教务管理员
    */
   private boolean isAdmin(String unitId,String userId) {
	   return customRoleRemoteService.checkUserRole(unitId, CreditConstants.SUBSYSTEM_86, CreditConstants.EDUCATION_CODE, userId);
//	   return true;
   }
}
