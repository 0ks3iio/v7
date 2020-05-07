package net.zdsoft.evaluation.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.evaluation.data.constants.EvaluationConstants;
import net.zdsoft.evaluation.data.dto.EvaluateTableDto;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;
import net.zdsoft.evaluation.data.entity.TeachEvaluateProject;
import net.zdsoft.evaluation.data.entity.TeachEvaluateRelation;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemService;
import net.zdsoft.evaluation.data.service.TeachEvaluateProjectService;
import net.zdsoft.evaluation.data.service.TeachEvaluateRelationService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Controller
@RequestMapping("/evaluate/project")
public class TeachEvaluateProjectAction extends BaseAction {
	@Autowired
	private TeachEvaluateProjectService teachEvaluateProjectService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private TeachEvaluateItemService teachEvaluateItemService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeachEvaluateRelationService teachEvaluateRelationService;
	
	@RequestMapping("/showStuSubList/page")
	@ControllerInfo(value = "提交列表页面")
	public String showStuSubList(HttpServletRequest request,ModelMap map){
		String projectId = request.getParameter("projectId");
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String gradeId = request.getParameter("gradeId");
		String classId = request.getParameter("classId");
		String selectType = request.getParameter("selectType");
		String selectObj = request.getParameter("selectObj");
		//获得年级列表
		List<Grade> gradelist = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(this.getLoginInfo().getUnitId(),acadyear), new TR<List<Grade>>());
		List<Clazz> clslist = new ArrayList<Clazz>();
		if(StringUtils.isNotBlank(gradeId)){
			clslist = SUtils.dt(classRemoteService.findByGradeId(getLoginInfo().getUnitId(), gradeId, null), new TR<List<Clazz>>(){});
			if(!EntityUtils.getSet(clslist, "id").contains(classId)){
				classId=null;
			}
		}else{
			classId=null;
//			gradeId = gradelist.get(0).getId();
//			clslist = SUtils.dt(classRemoteService.findByGradeId(getLoginInfo().getUnitId(), gradeId, null), new TR<List<Clazz>>(){});
		}
		Set<String> clsIds = new HashSet<String>();
		if(StringUtils.isNotBlank(classId)){
			clsIds.add(classId);
		}else if(StringUtils.isNotBlank(gradeId)){
			for (Clazz cls : clslist) {
				clsIds.add(cls.getId());
			}
		}else{
			clsIds = null;
		}
		Pagination page=createPagination();
		List<Student> stulist = teachEvaluateProjectService.findByStuSubList(projectId,clsIds,selectType,selectObj,page);
		map.put("stulist", stulist);
		map.put("gradeId", gradeId);
		map.put("classId", classId);
		map.put("selectType", selectType);
		map.put("selectObj", selectObj);
		map.put("projectId", projectId);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("clslist", clslist);
		map.put("gradelist", gradelist);
		sendPagination(request, map, page);
		TeachEvaluateProject p = teachEvaluateProjectService.getSubNum(projectId);
		map.put("subNum", p.getSubmitNum());
		map.put("noSubNum", p.getNoSubmitNum());
		return "/evaluation/project/stuSubList.ftl";
	}
	@RequestMapping("/showStuNotSubList/page")
	@ControllerInfo(value = "未提交列表页面")
	public String showStuNotSubList(HttpServletRequest request,ModelMap map){
		//TODO
		String projectId = request.getParameter("projectId");
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String gradeId = request.getParameter("gradeId");
		String classId = request.getParameter("classId");
		String selectType = request.getParameter("selectType");
		String selectObj = request.getParameter("selectObj");
		//获得年级列表
		List<Grade> gradelist = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(this.getLoginInfo().getUnitId(),acadyear), new TR<List<Grade>>());
		List<Clazz> clslist = new ArrayList<Clazz>();
		if(StringUtils.isNotBlank(gradeId)){
			clslist = SUtils.dt(classRemoteService.findByGradeId(getLoginInfo().getUnitId(), gradeId, null), new TR<List<Clazz>>(){});
			if(!EntityUtils.getSet(clslist, "id").contains(classId)){
				classId=null;
			}
		}else{
			classId=null;
		}
		Pagination page=createPagination();
		Set<String> clsIds = new HashSet<String>();
		if(StringUtils.isNotBlank(classId)){
			clsIds.add(classId);
		}else if(StringUtils.isNotBlank(gradeId)){
			for (Clazz cls : clslist) {
				clsIds.add(cls.getId());
			}
		}else{
			clsIds = null;
		}
		List<Student> stulist = teachEvaluateProjectService.findByStuNoSubList(projectId,clsIds,selectType,selectObj,page);
		map.put("stulist", stulist);
		map.put("gradeId", gradeId);
		map.put("classId", classId);
		map.put("selectType", selectType);
		map.put("selectObj", selectObj);
		map.put("projectId", projectId);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("clslist", clslist);
		map.put("gradelist", gradelist);
		sendPagination(request, map, page);
		TeachEvaluateProject p = teachEvaluateProjectService.getSubNum(projectId);
		map.put("subNum", p.getSubmitNum());
		map.put("noSubNum", p.getNoSubmitNum());
		return "/evaluation/project/stuNoSubList.ftl";
	}
	
	@RequestMapping("/delete/page")
	@ControllerInfo(value = "删除页面")
	public String showDeleteDiv(HttpServletRequest request,ModelMap map){
		String projectId = request.getParameter("projectId");
		map.put("projectId", projectId);
		return "/evaluation/project/deleteDiv.ftl";
	}
	@ResponseBody
	@RequestMapping("/delete")
	@ControllerInfo(value = "删除")
	public String doDelete(String projectId,String verifyCode,HttpSession httpSession) {
		try{
            String verifyCodeKey = EvaluationConstants.VERIFY_CODE_CACHE_KEY + httpSession.getId();
            String sessionVerifyCode = StringUtils.trim(RedisUtils.get(verifyCodeKey));
            if (StringUtils.isBlank(sessionVerifyCode)) {
                return error("验证码已失效");
            }
            if (StringUtils.equalsIgnoreCase(sessionVerifyCode, verifyCode)) {
                RedisUtils.del(verifyCodeKey);
            } else {
                return error("验证码错误");
            }
            teachEvaluateProjectService.deleteById(projectId);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
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
		if(se != null && StringUtils.isBlank(acadyear)){
			 acadyear = se.getAcadyear();
			 semester = se.getSemester()+"";
		}
		if(StringUtils.isBlank(acadyear)){
			acadyear = acadyearList.get(0);
			semester = "1";
		}
		if(se!=null){
			if(StringUtils.equals(se.getAcadyear(), acadyear) && StringUtils.equals(se.getSemester()+"", semester)){
				map.put("currentAcadyear", "1");
			}else{
				map.put("currentAcadyear", "0");
			}
		}else{
			map.put("currentAcadyear", "0");
		}
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		List<TeachEvaluateProject> projectList = teachEvaluateProjectService.findByUnitId(getLoginInfo().getUnitId(), acadyear, semester);
		map.put("projectList",projectList);
		return "/evaluation/project/index.ftl";
	}
	@ResponseBody
	@RequestMapping("/getSubNum")
	@ControllerInfo(value = "获取提交未提交人数")
	public String getSubNum(String projectId){
		try {
			TeachEvaluateProject p = teachEvaluateProjectService.getSubNum(projectId);
			if(p!=null){
				JSONObject json=new JSONObject();
				json.put("submitNum", p.getSubmitNum());
				json.put("noSubmitNum", p.getNoSubmitNum());
				return json.toJSONString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@RequestMapping("/edit/page")
	@ControllerInfo(value = "新增（查看）页面")
	public String showProjectEdit(HttpServletRequest request,ModelMap map){
		String projectId = request.getParameter("projectId");
		if(StringUtils.isNotBlank(projectId)){
			TeachEvaluateProject project = teachEvaluateProjectService.findListByIdIn(new String[]{projectId}).get(0);
			Set<String> gradeIds=EntityUtils.getSet(teachEvaluateRelationService.findByProjectIds(new String[]{project.getId()}),"valueId");
			List<Grade> gradeList=SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])),new TR<List<Grade>>(){});
			String gardeName="";
			if(CollectionUtils.isNotEmpty(gradeList)){
				for(Grade grade:gradeList){
					gardeName+=grade.getGradeName()+" ";
				}
			}
			map.put("gardeName",gardeName);
			map.put("project",project);
			map.put("readonly","1");
			Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),Semester.class);
			map.put("startTime", semester.getWorkBegin());
			map.put("endTime", semester.getWorkEnd());
		}else{
			Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),Semester.class);
			TeachEvaluateProject project = new TeachEvaluateProject();
			project.setAcadyear(semester.getAcadyear());
			project.setSemester(semester.getSemester()+"");
			List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(this.getLoginInfo().getUnitId(),semester.getAcadyear()), new TR<List<Grade>>(){});
			
			map.put("gradeList", gradeList);
			map.put("startTime", semester.getWorkBegin());
			map.put("endTime", semester.getWorkEnd());
			map.put("project",project);
			map.put("readonly","0");
		}
		return "/evaluation/project/projectEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/save")
    @ControllerInfo(value = "保存项目")
	public String saveProject(TeachEvaluateProject project, ModelMap map){
		try {
			if(project == null){
				return error("无效数据!");
			}
			if(project.getBeginTime() == null){
				return error("开始时间不能为空！");
			}
			if(project.getEndTime() == null){
				return error("结束时间不能为空！");
			}
			if(DateUtils.compareIgnoreSecond(project.getBeginTime(), project.getEndTime())>0){
				return error("开始时间不能大于结束时间！");
			}
//			List<TeachEvaluateProject> list = teachEvaluateProjectService.findExist(getLoginInfo().getUnitId(),project.getAcadyear(),project.getSemester(),project.getEvaluateType(),project.getBeginTime(), project.getEndTime());
//			if(CollectionUtils.isNotEmpty(list) && list.size() > 0){
//				return error("时间和现有项目“时间”冲突，无法保存！");
//			}
			Map<String, McodeDetail>  mcodeDetailMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-PJLX"), new TypeReference<Map<String, McodeDetail> >(){});
			project.setUnitId(getLoginInfo().getUnitId());
			project.setProjectName(project.getAcadyear()+"学年第"+(StringUtils.equals(project.getSemester(), "1")?"一":"二")+"学期"+mcodeDetailMap.get(project.getEvaluateType()).getMcodeContent());
			teachEvaluateProjectService.saveProject(project);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}
		return success("操作成功！");
	}
	
	@ResponseBody
	@RequestMapping("/saveProjectName")
    @ControllerInfo(value = "保存项目名称")
	public String saveProjectName(String projectId,String projectName, ModelMap map){
		if(StringUtils.isBlank(projectId)){
			return error("无效数据!");
		}
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		if(project == null){
			return error("找不到该评教项目");
		}
		project.setProjectName(projectName);
		teachEvaluateProjectService.updatePorject(project);
		return success("操作成功！");
	}
	
	@RequestMapping("/itemTable/page")
	@ControllerInfo(value = "评教管理-评教项目")
	public String showItems(HttpServletRequest request,ModelMap map) {
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String projectId = request.getParameter("projectId");
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		String evaluateType = project.getEvaluateType();
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(getLoginInfo().getUnitId(),evaluateType,project.getId());
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
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("dto", dto);
		return "/evaluation/project/projectItemTable.ftl";
	}
	
	@RequestMapping("/addRelation/page")
	@ControllerInfo(value="添加参评班级")
	public String addRelation(HttpServletRequest request,ModelMap map){
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String projectId = request.getParameter("projectId");
		String evaluateType = request.getParameter("evaluateType");
		String gradeId = request.getParameter("gradeId");
		String canAdd = request.getParameter("canAdd");
		
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(this.getLoginInfo().getUnitId(),acadyear), new TR<List<Grade>>(){});
		
		List<TeachEvaluateRelation> relationList=teachEvaluateRelationService.getRelationList(this.getLoginInfo().getUnitId(), projectId, acadyear, semester, evaluateType, gradeId);
		map.put("gradeList", gradeList);
		map.put("relationList", relationList);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("projectId", projectId);
		map.put("evaluateType", evaluateType);
		map.put("gradeId", gradeId);
		map.put("canAdd", canAdd);
		return "/evaluation/project/projectAddRelation.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/addRelation/saveRelation")
	@ControllerInfo(value="保存参评班级")
	public String saveRelation(String valueIds,String noCheckIds,String projectId,ModelMap map){
		try {
			teachEvaluateRelationService.saveRelations(valueIds,noCheckIds, projectId);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@RequestMapping("/doExport/page")
	@ControllerInfo(value = "导出")
	public void doExport(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		String projectId = request.getParameter("projectId");
		String gradeId = request.getParameter("gradeId");
		String classId = request.getParameter("classId");
		String selectType = request.getParameter("selectType");
		String selectObj = request.getParameter("selectObj");
		List<Clazz> clslist = new ArrayList<Clazz>();
		if(StringUtils.isNotBlank(gradeId)){
			clslist = SUtils.dt(classRemoteService.findByGradeId(getLoginInfo().getUnitId(), gradeId, null), new TR<List<Clazz>>(){});
		}
		Set<String> clsIds = new HashSet<String>();
		if(StringUtils.isNotBlank(classId)){
			clsIds.add(classId);
		}else if(StringUtils.isNotBlank(gradeId)){
			for (Clazz cls : clslist) {
				clsIds.add(cls.getId());
			}
		}else{
			clsIds = null;
		}
		List<Student> stulist = teachEvaluateProjectService.findByStuNoSubList(projectId,clsIds,selectType,selectObj,null);
		//以下导出功能
		List<String> tis = new ArrayList<String>();
		tis.add("序号");
		tis.add("姓名");
		tis.add("学号");
		tis.add("行政班");
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		TeachEvaluateProject project=teachEvaluateProjectService.findOne(projectId);
		String projectName=project==null?"":project.getProjectName();
		getRecordList(recordList,stulist);
		sheetName2RecordListMap.put("noSubList",recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put("noSubList", tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile(projectName+"未提交学生", titleMap, sheetName2RecordListMap, response);
		
	}
	public void getRecordList(List<Map<String,String>> recordList,List<Student> stulist){
		if(CollectionUtils.isNotEmpty(stulist)){
			Map<String,String> valueMap=null;
			int i=1;
			for(Student stu:stulist){
				valueMap=new HashMap<String,String>();
				valueMap.put("序号", i+"");
				valueMap.put("姓名", stu.getStudentName());
				valueMap.put("学号", stu.getStudentCode());
				valueMap.put("行政班", stu.getClassName());
				recordList.add(valueMap);
				i++;
			}
		}
	}
}
