package net.zdsoft.evaluation.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.evaluation.data.constants.EvaluationConstants;
import net.zdsoft.evaluation.data.dto.OptionDto;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;
import net.zdsoft.evaluation.data.entity.TeachEvaluateProject;
import net.zdsoft.evaluation.data.service.TeachEvaluateProjectService;
import net.zdsoft.evaluation.data.service.TeachEvaluateResultService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;


@Controller
@RequestMapping("/evaluate")
public class TeachEvaResultAction extends BaseAction{

	@Autowired
	private TeachEvaluateProjectService teachEvaluateProjectService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private TeachEvaluateResultService teachEvaluateResultService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	McodeRemoteService mcodeRemoteService;
	
	@RequestMapping("/stu/index/page")
	public String stuIndex(ModelMap map){
		
		return "/evaluation/result/evaluateResultIndex.ftl";
	}
	@RequestMapping("/stu/list")
	public String stuList(ModelMap map){
		
		Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()),Semester.class);
		if(se==null){
			return errorFtl(map,"当前时间不在学年学期范围内");
		}
		List<TeachEvaluateProject> projectList=teachEvaluateResultService.findResultByUnitId(getLoginInfo().getUnitId(),getLoginInfo().getOwnerId(), se.getAcadyear(), se.getSemester()+"");
		
		map.put("projectList", projectList);
		return "/evaluation/result/evaluateResultList.ftl";
	}
	@ResponseBody
	@RequestMapping("/stu/saveItem")
	public String saveItem(ModelMap map,OptionDto dto){
		try {
			String subjectIdArr=dto.getSubjectId();
			String subjectId = null;
			String teaId = null;
			String clsId = null;
			if(StringUtils.isNotBlank(subjectIdArr)&&subjectIdArr.split(",").length>2) {
				subjectId = subjectIdArr.split(",")[0];
				teaId = StringUtils.trim(subjectIdArr.split(",")[1]);
				clsId = subjectIdArr.split(",")[2];
			}else {
				subjectId = subjectIdArr;
			}
			
			if(StringUtils.isNotBlank(clsId)) {
				dto.setTeachOrclassId(clsId);
			}
			if(StringUtils.isNotBlank(teaId) && !teaId.equals("null")) {
				dto.setTeacherId(teaId);
			}
			dto.setSubjectId(subjectId);
			String returnStr=teachEvaluateResultService.saveResult(getLoginInfo().getUnitId(),getLoginInfo().getOwnerId(), dto);
			if(!returnStr.equals("success")){
				return error(returnStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@RequestMapping("/stu/edit")
	public String stuEdit(ModelMap map,HttpServletRequest request,OptionDto dto){
		String status=request.getParameter("status");
		String unitId=getLoginInfo().getUnitId();
		String studentId=request.getParameter("studentId");
		String evaluateType=dto.getEvaluateType();
		String projectId=dto.getProjectId();
		String subjectIdArr=dto.getSubjectId();
		String tId =null;
		String cId = null;
		String subjectId = null;
		if(StringUtils.isNotBlank(subjectIdArr)&&subjectIdArr.split(",").length>2) {
			subjectId = subjectIdArr.split(",")[0];
			tId = subjectIdArr.split(",")[1];
			cId = subjectIdArr.split(",")[2];
		}else {
			subjectId = subjectIdArr;
		}
		if(StringUtils.isBlank(evaluateType)){
			TeachEvaluateProject project=teachEvaluateProjectService.findOne(projectId);
			if(project==null) return error("项目数据出错");
			evaluateType=project.getEvaluateType();
		}
		
		boolean isStu=false;
		
		if(StringUtils.isBlank(studentId)){
			studentId=getLoginInfo().getOwnerId();
		}
		if(getLoginInfo().getOwnerType()==1){//判断是否是学生进来
			isStu=true;
		}
		
		if(StringUtils.isBlank(dto.getAcadyear())){
			Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId),Semester.class);
			if(se==null){
				return errorFtl(map,"当前时间不在学年学期范围内");
			}
			dto.setAcadyear(se.getAcadyear());
			dto.setSemester(se.getSemester()+"");
		}
		Teacher teacher=null;
		String showName="教师";
		Student stu=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
		if(evaluateType.equals(EvaluationConstants.EVALUATION_TYPE_TEACH) || evaluateType.equals(EvaluationConstants.EVALUATION_TYPE_ELECTIVE)){//教学调查或选修课调查
			Set<String> subIdTeaIdClassIdSet = new HashSet<>();
			teachEvaluateResultService.setCourseMap(subIdTeaIdClassIdSet,unitId, studentId, dto.getAcadyear(), dto.getSemester(), projectId);
			if(CollectionUtils.isNotEmpty(subIdTeaIdClassIdSet)){
				Set<String> subjectIds = new HashSet<>();
				Set<String> teaIds = new HashSet<>();
				Set<String> clsIds = new HashSet<>();
				for(String e : subIdTeaIdClassIdSet) {
					String subId = e.split(",")[0];
					String teaId = e.split(",")[1];
					String clsId = e.split(",")[2];
					teaIds.add(teaId);
					clsIds.add(clsId);
					subjectIds.add(subId);
				}
				Map<String,Course> courseMap =EntityUtils.getMap(SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>(){}), Course :: getId);
				Map<String,Teacher> teaMap = EntityUtils.getMap(SUtils.dt(teacherRemoteService.findListByIds(teaIds.toArray(new String[0])), new TR<List<Teacher>>() {}), Teacher :: getId);
				List<Course> courseTeaList= new ArrayList<>();
				Course co = null;
				for(String e : subIdTeaIdClassIdSet) {
					String subId = e.split(",")[0];
					String teaId = e.split(",")[1];
					String clsId = e.split(",")[2];
					Course c = courseMap.get(subId);
					co = new Course();
					if(teaMap.containsKey(teaId)) {
						co.setSubjectName(c.getSubjectName()+"("+teaMap.get(teaId).getTeacherName()+")");
					}else {
						co.setSubjectName(c.getSubjectName()+"(无教师)");
					}
					co.setId(c.getId());
					co.setUnitName(teaId);
					co.setCourseTypeName(clsId);
					courseTeaList.add(co);
				}
				if(StringUtils.isBlank(subjectId)){
					if(CollectionUtils.isNotEmpty(courseTeaList)){
						subjectId=courseTeaList.get(0).getId();
						tId = courseTeaList.get(0).getUnitName();
						cId = courseTeaList.get(0).getCourseTypeName();
					}
				}
				teacher=SUtils.dc(teacherRemoteService.findOneById(tId),Teacher.class);
				//行政班或教学班id
				TeachClass teachClass=SUtils.dc(teachClassRemoteService.findOneById(cId),TeachClass.class);
				if(teachClass==null){
					Clazz clazz=SUtils.dc(classRemoteService.findOneById(cId),Clazz.class);
					map.put("teachOrclassName", clazz==null?"":clazz.getClassNameDynamic());
				}else{
					map.put("teachOrclassName",teachClass.getName());
				}
				
				map.put("teachOrclassId", cId);
				map.put("courseList", courseTeaList);
				map.put("cId", cId);
				map.put("tId", tId);
				dto.setSubjectId(subjectId);
			}
		}else if(evaluateType.equals(EvaluationConstants.EVALUATION_TYPE_TEACHER)){//班主任调查
			teacher=teachEvaluateResultService.getClassTeacher(studentId);
			if(stu!=null){
				Clazz clazz=SUtils.dc(classRemoteService.findOneById(stu.getClassId()),Clazz.class);
				map.put("className", clazz==null?"":clazz.getClassNameDynamic());
			}
			showName="班主任";
		}else{//导师调查
			teacher=teachEvaluateResultService.getTutorTeacher(studentId, unitId);
			showName="导师";
		}
		List<McodeDetail> zbmcodeList=SUtils.dt(mcodeRemoteService.findByMcodeIds(EvaluationConstants.EVA_ITEM_TYPE),new TR<List<McodeDetail>>(){});
		//已提交的 显示结果表中的老师
		Map<String,String> resultOfTeaIdMap=new HashMap<>();
		
		Map<String,List<TeachEvaluateItem>> itemMap=teachEvaluateResultService.findItemByUnitId(resultOfTeaIdMap,unitId,studentId,projectId, evaluateType,subjectId,status, teacher==null?"":teacher.getId(),cId);
		int trNum=0;
		for(McodeDetail mcode:zbmcodeList){
			if(CollectionUtils.isEmpty(itemMap.get(mcode.getThisId()))){
				itemMap.put(mcode.getThisId(), new ArrayList<TeachEvaluateItem>());
			}else{
				if(!(mcode.getThisId().equals(EvaluationConstants.ITEM_TYPE_ANSWER))){
					trNum+=itemMap.get(mcode.getThisId()).size();
				}
			}
		}
		if(!"1".equals(status) && teacher == null){//查看页面的显示老师
			String teacherId=resultOfTeaIdMap.get(subjectId);
			if(StringUtils.isBlank(teacherId)) {
				teacherId=resultOfTeaIdMap.get(studentId);
			}
			teacher=SUtils.dc(teacherRemoteService.findOneById(teacherId),Teacher.class);
		}
		if(stu!=null){	
			map.put("studentName", stu.getStudentName());
			map.put("studentCode", stu.getStudentCode());
		}
		
		map.put("trNum", trNum);
		map.put("itemMap", itemMap);
		map.put("zbmcodeList", zbmcodeList);
		
		map.put("status", status);
		map.put("teacherName", teacher==null?"":teacher.getTeacherName());
		map.put("teacherId", teacher==null?"":teacher.getId());
		map.put("showName", showName);
		map.put("dto", dto);
		if("1".equals(status)){
			return "/evaluation/result/evaluateResultEdit.ftl";
		}else{
			map.put("studentId", studentId);
			map.put("isStu", isStu);
			map.put("gradeId", request.getParameter("gradeId"));
			map.put("classId", request.getParameter("classId"));
			map.put("selectObj", request.getParameter("selectObj"));
			map.put("selectType", request.getParameter("selectType"));
			return "/evaluation/result/evaluateResultDetail.ftl";
		}
	}
	
}
