package net.zdsoft.evaluation.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.evaluation.data.constants.EvaluationConstants;
import net.zdsoft.evaluation.data.dto.EvaluateTableDto;
import net.zdsoft.evaluation.data.dto.ResultStatDto;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;
import net.zdsoft.evaluation.data.entity.TeachEvaluateProject;
import net.zdsoft.evaluation.data.entity.TeachEvaluateRelation;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemService;
import net.zdsoft.evaluation.data.service.TeachEvaluateProjectService;
import net.zdsoft.evaluation.data.service.TeachEvaluateRelationService;
import net.zdsoft.evaluation.data.service.TeachEvaluateResultService;
import net.zdsoft.evaluation.data.service.TeachEvaluateResultStatService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;

@Controller
@RequestMapping("/evaluate/query")
public class TeachEvaluateQueryAction extends BaseAction {
	@Autowired
	private TeachEvaluateProjectService teachEvaluateProjectService;
	@Autowired
	private TeachEvaluateResultStatService teachEvaluateResultStatService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private TeachEvaluateItemService teachEvaluateItemService;
	@Autowired 
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeachEvaluateResultService teachEvaluateResultService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeachEvaluateRelationService teachEvaluateRelationService;
	
	@RequestMapping("/statTeaRankResult/page")
	@ControllerInfo(value = "班主任/导师--排名")
	public String showTeaRank(HttpServletRequest request,ModelMap map) {
		String evaluateType = request.getParameter("evaluateType");
		map.put("evaluateType", evaluateType);
		String projectId = request.getParameter("projectId");
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		map.put("project", project);
		//TODO
		List<ResultStatDto> dtolist = teachEvaluateResultStatService.findTeaRankBy(projectId,project.getEvaluateType());
		map.put("dtolist", dtolist);
		return "/evaluation/query/statResultTeaRank.ftl";
	}
	
	@RequestMapping("/statTeaClsResult/page")
	@ControllerInfo(value = "班主任/导师--班级维度")
	public String showTeableClsTea(HttpServletRequest request,ModelMap map) {
		String evaluateType = request.getParameter("evaluateType");
		map.put("evaluateType", evaluateType);
		String projectId = request.getParameter("projectId");
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		map.put("project", project);
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		map.put("dto", dto);
		String itemType = request.getParameter("itemType");
		if(StringUtils.isBlank(itemType)){
			itemType = "0";
		}
		map.put("itemType", itemType);
		if(StringUtils.equals(project.getEvaluateType(), EvaluationConstants.EVALUATION_TYPE_TOTOR)){
			String teaId = getLoginInfo().getOwnerId();
			map.put("teaId", teaId);
			if(StringUtils.isBlank(teaId)){
				map.put("stuCount", 0f);
				map.put("countScore", 0f);
				map.put("clsItemScoreMap", new HashMap<String,Float>());
				return "/evaluation/query/statResultTeaCls.ftl";
			}
			Teacher tea = SUtils.dc(teacherRemoteService.findOneById(teaId), Teacher.class);
			map.put("teaName", tea.getTeacherName());
			Set<String> stuIds = teachEvaluateResultService.getStuIdByProjectId(projectId,null, null, teaId);
			if(CollectionUtils.isNotEmpty(stuIds)){
				map.put("stuCount", stuIds.size());
			}else{
				map.put("stuCount", 0);
			}
			if(StringUtils.equals(itemType, "0")){
				Map<String,Float> clsItemScoreMap= teachEvaluateResultStatService.getResultStatTeaDto(projectId, teaId);
				map.put("clsItemScoreMap", clsItemScoreMap);
				float countScore = 0f;
				for (TeachEvaluateItem item : dto.getEvaluaList()) {
					if(clsItemScoreMap.containsKey(item.getId())){
						countScore = countScore + clsItemScoreMap.get(item.getId());
					}
				}
				map.put("countScore", countScore);
			}else{
				List<ResultStatDto> txtDtolist = teachEvaluateResultService.getResultTxtDto(projectId,null,null,teaId);
				map.put("txtDtolist", txtDtolist);
			}
		}else{
			String classId = request.getParameter("classId");
			map.put("classId", classId);
			List<Clazz> clslist = SUtils.dt(classRemoteService.findByTeacherId(getLoginInfo().getOwnerId()),new TR<List<Clazz>>() {});
			map.put("clslist", clslist);
			if(StringUtils.isBlank(classId)){
				map.put("stuCount", 0);
				map.put("countScore", 0f);
				map.put("clsItemScoreMap", new HashMap<String,Float>());
				return "/evaluation/query/statResultTeaCls.ftl";
			}
			String className = "";
			String teacherId = "";
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			className = cls.getClassNameDynamic();
			teacherId = cls.getTeacherId();
			Teacher tea = SUtils.dc(teacherRemoteService.findOneById(teacherId), Teacher.class);
			if(tea != null) {
				map.put("teacherName", tea.getTeacherName());
			}else {
				map.put("teacherName", "");
			}
			map.put("className", className);
			Set<String> stuIds = teachEvaluateResultService.getStuIdByProjectId(projectId,classId, null, null);
			if(CollectionUtils.isNotEmpty(stuIds)){
				map.put("stuCount", stuIds.size());
			}else{
				map.put("stuCount", 0);
			}
			if(StringUtils.equals(itemType, "0")){
				Map<String,Float> clsItemScoreMap= teachEvaluateResultStatService.getResultStatTeaClsDto(projectId, classId);
				map.put("clsItemScoreMap", clsItemScoreMap);
				float countScore = 0f;
				for (TeachEvaluateItem item : dto.getEvaluaList()) {
					if(clsItemScoreMap.containsKey(item.getId())){
						countScore = countScore + clsItemScoreMap.get(item.getId());
					}
				}
				map.put("countScore", countScore);
			}else{
				List<ResultStatDto> txtDtolist = teachEvaluateResultService.getResultTxtDto(projectId,null,null,teacherId);
				map.put("txtDtolist", txtDtolist);
			}
		}
		return "/evaluation/query/statResultTeaCls.ftl";
	}
	
	@RequestMapping("/statResultTxt/page")
	@ControllerInfo(value = "文本信息")
	public String showTeableTxt(HttpServletRequest request,ModelMap map) {
		String evaluateType = request.getParameter("evaluateType");
		map.put("evaluateType", evaluateType);
		String projectId = request.getParameter("projectId");
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		map.put("project", project);
		map.put("projectType", project.getEvaluateType());
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		map.put("dto", dto);
		
		String teaId = getLoginInfo().getOwnerId();
		String clsId = request.getParameter("clsId");
		String subId = request.getParameter("subId");
		String teaName = "";
		if(StringUtils.isNotBlank(teaId)){
			Teacher tea = SUtils.dc(teacherRemoteService.findOneById(teaId), Teacher.class);
			teaName = tea.getTeacherName();
		}
		map.put("teaId", teaId);
		map.put("teaName", teaName);
		if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TEACH, project.getEvaluateType())){
			String classId = request.getParameter("classId");
			map.put("classId", classId);
			String subjectId = request.getParameter("subjectId");
			map.put("subjectId", subjectId);
			List<ResultStatDto> txtDtolist = teachEvaluateResultService.getResultTxtDto(projectId,subId,clsId,teaId);
			map.put("txtDtolist", txtDtolist);
		}else if (StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_ELECTIVE, project.getEvaluateType())){
			System.out.println(subId);
			List<ResultStatDto> txtDtolist = teachEvaluateResultService.getResultTxtDto(projectId,subId,null,teaId);
			map.put("txtDtolist", txtDtolist);
		}
		return "/evaluation/query/statResultTxt.ftl";
	}
	
	@RequestMapping("/statResultSub/page")
	@ControllerInfo(value = "教师+学科维度（必须课）")
	public String showTableSub(HttpServletRequest request,ModelMap map){
		String evaluateType = request.getParameter("evaluateType");
		map.put("evaluateType", evaluateType);
		String projectId = request.getParameter("projectId");
		map.put("projectId", projectId);
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		map.put("project", project);
		String subjectId = request.getParameter("subjectId");
		String classId = request.getParameter("classId");
		map.put("subjectId", subjectId);
		map.put("classId", classId);
		List<TeachEvaluateRelation> relations = new ArrayList<>(); 
		relations = teachEvaluateRelationService.findByProjectIds(new String[] {projectId});
		Set<String> clsIds = new HashSet<>();
		Set<String> courseIds = new HashSet<>();
		for (TeachEvaluateRelation e : relations) {
			clsIds.add(e.getValueId());
			courseIds.add(e.getSubjectId());
		}
		List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){});
		map.put("courseList", courseList);
		String teacherId = getLoginInfo().getOwnerId();
		String subId = request.getParameter("subId");
		map.put("subId", subId);
		map.put("teacherId", teacherId);
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		map.put("dto", dto);
		//取数据
		List<ResultStatDto> subDtoList = teachEvaluateResultStatService.getResultSubStat(projectId,EvaluationConstants.STATE__DIMENSION_SUBJECT,teacherId,subId);
		map.put("subDtoList", subDtoList);
		return "/evaluation/query/statResultSub.ftl";
	}
	@RequestMapping("/statResult/page")
	@ControllerInfo(value = "班级维度")
	public String showTeable(HttpServletRequest request,ModelMap map) {
		String evaluateType = request.getParameter("evaluateType");
		map.put("evaluateType", evaluateType);
		String projectId = request.getParameter("projectId");
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		map.put("project", project);
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		map.put("dto", dto);
		LoginInfo loginInfo = getLoginInfo();
		String teaId = loginInfo.getOwnerId();
		System.out.println(teaId);
		if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TEACH, project.getEvaluateType()) 
				|| StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_ELECTIVE, project.getEvaluateType())){
			//班级维度
			if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TEACH, project.getEvaluateType())){
				// 必修课
				
				String teaName = "";
				if(StringUtils.isNotBlank(teaId)){
					Teacher tea = SUtils.dc(teacherRemoteService.findOneById(teaId), Teacher.class);
					teaName = tea.getTeacherName();
				}
				map.put("teaId", teaId);
				map.put("teaName", teaName);
				//科目范围可以直接到项目设置班级关系表中找
				String subjectId = request.getParameter("subjectId");
				List<TeachEvaluateRelation> relations = new ArrayList<>(); 
				relations = teachEvaluateRelationService.findByProjectIds(new String[] {projectId});
				Set<String> clsIds = new HashSet<>();
				Set<String> courseIds = new HashSet<>();
				for (TeachEvaluateRelation e : relations) {
					clsIds.add(e.getValueId());
					courseIds.add(e.getSubjectId());
				}
				List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){});
				map.put("subjectId", subjectId);
				map.put("courseList", courseList);
				List<Clazz> classes = new ArrayList<>();
				String classId = request.getParameter("classId");
				if(StringUtils.isNotBlank(classId) && !clsIds.contains(classId)) {
					classId = "";
				}
				classes = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>(){});
				List<TeachClass> teaClses = SUtils.dt(teachClassRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<TeachClass>>(){});
				if(CollectionUtils.isNotEmpty(teaClses)) {
					Clazz clazz = null;
					for (TeachClass e : teaClses) {
						clazz = new Clazz();
						clazz.setId(e.getId());
						clazz.setClassNameDynamic(e.getName());
						classes.add(clazz);
					}
				}
				map.put("classId", classId);
				map.put("classes", classes);
				List<ResultStatDto> dtoAlllist = teachEvaluateResultStatService.getResultStatDto(projectId,EvaluationConstants.STATE__DIMENSION_CLASS,subjectId,classId,teaId);
				map.put("dtolist",dtoAlllist );
				return "/evaluation/query/statResultCls.ftl";
			}else{
				// 选修课
				// 获得表格数据
				List<ResultStatDto> dtoAlllist = teachEvaluateResultStatService.getResultStatElectiveDto(projectId,EvaluationConstants.STATE__DIMENSION_CLASS);
				List<ResultStatDto> dtolist = new ArrayList<>();
				for (ResultStatDto e : dtoAlllist) {
					if(StringUtils.equals(e.getTeaId(), teaId)) {
						dtolist.add(e);
					}
				}
				map.put("dtolist",dtolist );
				return "/evaluation/query/statResultElective.ftl";
			}
		}else{
			// 年级维度与全校维度
			//班主任调查和导师调查
			return showTeableClsTea(request, map);
//			Map<String,Float> gradeItemScoreMap= teachEvaluateResultStatService.getResultStatTeaSchDto(projectId);
//			map.put("gradeItemScoreMap", gradeItemScoreMap);
//			Set<String> gradeIds = new HashSet<String>();
//			for(String gradeItemId : gradeItemScoreMap.keySet()){
//				String gradeId = gradeItemId.split(",")[0];
//				if(!StringUtils.equals(gradeId, "school")){
//					gradeIds.add(gradeId);
//				}
//			}
//			if(gradeIds.size()>0){
//				List<Grade> gradelist = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])), new TR<List<Grade>>());
//				map.put("gradelist", gradelist);
//			}else{
//				map.put("gradelist", null);
//			}
//			return "/evaluation/query/statResultTea.ftl";
		}
	}
	
	@RequestMapping("/index/page")
	@ControllerInfo(value = "评教管理-评教项目")
	public String showIndex(HttpServletRequest request,ModelMap map) {
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		map.put("acadyearList", acadyearList);
		Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()),Semester.class);
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String evaluateType = request.getParameter("evaluateType");
		if(se != null && StringUtils.isBlank(acadyear)){
			 acadyear = se.getAcadyear();
			 semester = se.getSemester()+"";
		}
		if(StringUtils.isBlank(acadyear)){
			acadyear = acadyearList.get(0);
			semester = "1";
		}
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("evaluateType", evaluateType);
		List<TeachEvaluateProject> projectList = teachEvaluateProjectService.findByUnitIdAndType(getLoginInfo().getUnitId(), acadyear, semester,evaluateType);
		for (TeachEvaluateProject project : projectList) {
			String hasStat = RedisUtils.get("statEvaluateState"+project.getId());
			if(StringUtils.isNotBlank(hasStat)){
				project.setHasStat(hasStat);
			}
		}
		map.put("projectList",projectList);
		return "/evaluation/query/statIndex.ftl";
	}
	
	public class StatThread extends Thread{
		private String projectId;
		private TeachEvaluateResultStatService teachEvaluateResultStatService;
	    public StatThread(String projectId,TeachEvaluateResultStatService teachEvaluateResultStatService) { 
	    	this.projectId = projectId;
	    	this.teachEvaluateResultStatService = teachEvaluateResultStatService;
	    } 
	    public void run() { 
	    	RedisUtils.set("statEvaluateState"+projectId, "2");
	    	try {
	    		teachEvaluateResultStatService.statResult(projectId);
	    		RedisUtils.del("statEvaluateState"+projectId);
			} catch (Exception e) {
				System.out.println("统计失败：projectId="+projectId);
				RedisUtils.del("statEvaluateState"+projectId);
				e.printStackTrace();
			}
	    } 
	}
	
	@ResponseBody
	@RequestMapping("/statResult")
    @ControllerInfo(value = "统计")
	public String statResult(String projectId,ModelMap map){
		if(StringUtils.isBlank(projectId)){
			return error("项目不存在!");
		}
		try {
			StatThread statThread = new StatThread(projectId, teachEvaluateResultStatService);
			statThread.start();
		} catch (Exception e) {
			return error("统计启动失败!");
		}
		return success("统计中，请稍后进行查看！");
	}
}
