package net.zdsoft.scoremanage.data.action;

import java.util.ArrayList;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.TreeRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.constant.ScoreChartsConstants;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.ChartsDetailDto;
import net.zdsoft.scoremanage.data.dto.ChartsDetailParDto;
import net.zdsoft.scoremanage.data.entity.Borderline;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.JoinexamschInfo;
import net.zdsoft.scoremanage.data.entity.ScoreStatistic;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.BorderlineService;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.JoinexamschInfoService;
import net.zdsoft.scoremanage.data.service.ScoreStatisticService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;

@Controller
@RequestMapping("/scoremanage")
public class ScoreStatisticAction extends BaseAction{
	
	@Autowired
	private UnitRemoteService unitService;
	@Autowired
	private ClassRemoteService classService;
	@Autowired
	private BorderlineService borderlineService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private ScoreStatisticService scoreStatisticService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private JoinexamschInfoService joinexamschInfoService;
	@Autowired
	private TreeRemoteService treeService;
	@Autowired
	private StudentRemoteService studentService;
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	
	@RequestMapping("/scoreStatistic/index/page")
	@ControllerInfo(value = "统计图")
	public String showHead(String documentLabel,ModelMap map, HttpSession httpSession) {
		List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){}); 
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(2), Semester.class);
		String acadyearSearch = semester.getAcadyear();
		String semesterSearch=semester.getSemester()+"";
		map.put("acadyearList", acadyearList);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
		map.put("unitClass", getLoginInfo().getUnitClass());
		map.put("charts73Map", ScoreChartsConstants.SCORE_CHARTS_73_MAP);
		map.put("chartsKSMap", ScoreChartsConstants.SCORE_CHARTS_KS_MAP);
		map.put("chartsRCMap", ScoreChartsConstants.SCORE_CHARTS_RC_MAP);
		return "/scoremanage/scoreStatistic/scoreStatisticIndex.ftl";
	}
	
	@RequestMapping("/scoreStatistic/list/page")
	@ControllerInfo(value = "统计图showList")
	public String showList(String examId,String documentLabel,ModelMap map){
		ExamInfo findOne = null;
		if(StringUtils.isNotBlank(examId)){
			findOne = examInfoService.findOne(examId);
			if(findOne == null){
				return errorFtl(map,"考试已不存在！");
			}
			map.put("examId", examId);
		}
		map.put("unitClass", getLoginInfo().getUnitClass());
		ChartsDetailDto chartsDetailDto = ScoreChartsConstants.SCORE_CHARTS_ALL_MAP.get(documentLabel);
		map.put("parMap", chartsDetailDto.getParMap());
		map.put("documentLabel", documentLabel);
		if(chartsDetailDto.getChartType() == 2 && findOne != null){
			if(!ScoreDataConstants.TKLX_0.equals(findOne.getExamUeType())){
				map.put("isHaveTK",true);
			}else{
				map.put("isHaveTK",false);
			}
		}
		return chartsDetailDto.getUrlFtl();
	}
	
	
	@RequestMapping("/scoreStatistic/list1/page")
	@ControllerInfo(value = "统计图showList")
	public String showList1(String lable,ModelMap map){
		if("score7choose3Grade".equals(lable)){
			return "/scoremanage/scoreStatistic/scoreCharts1.ftl";
		}else{
			return "/scoremanage/scoreStatistic/scoreCharts3.ftl";
		}
	}

	@ResponseBody
	@RequestMapping("/scoreStatistic/findCourseList")
	@ControllerInfo("统计图-找科目")
	public String showFindCourseForList(String examId,String id,String type,ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		JSONArray jsonArr=new JSONArray();
		List<SubjectInfo> findByExamIdClassId = null;
		if("1".equals(type)){
			//班级
			if(StringUtils.isBlank(examId)){
				Clazz clazz = SUtils.dt(classService.findOneById(id), new TypeReference<Clazz>(){});
				findCourseList(jsonArr,clazz.getSchoolId(),String.valueOf(clazz.getSection()));
				return jsonArr.toJSONString();
			}else{
				findByExamIdClassId = subjectInfoService.findByExamIdClassId(examId,id);
			}
		}else if("2".equals(type)){
			//年级
			if(StringUtils.isBlank(examId)){
				Grade grade = SUtils.dt(gradeRemoteService.findOneById(id), new TypeReference<Grade>(){});
				findCourseList(jsonArr,grade.getSchoolId(),String.valueOf(grade.getSection()));
				return jsonArr.toJSONString();
			}else{
				findByExamIdClassId = subjectInfoService.findByExamIdGradeId(examId,id);
			}
			
		}
		JSONObject job=null;
		JSONArray linJsonArr = new JSONArray();
		for (SubjectInfo item : findByExamIdClassId) {
			job=new JSONObject();
			job.put("id", item.getSubjectId());
			job.put("name", item.getCourseName());
			linJsonArr.add(job);
		}
		jsonArr.add(linJsonArr);
		return jsonArr.toJSONString();
	}

	private void findCourseList(JSONArray jsonArr, String schoolId,String section) {
		List<Course> courseList=new ArrayList<Course>();
		Set<String> unitIds=new HashSet<String>();
		Unit djunit = SUtils.dc(unitService.findTopUnit(schoolId), Unit.class);
		//学校
		unitIds.add(schoolId);
		if(djunit!=null){
			unitIds.add(djunit.getId());
		}
		courseList=SUtils.dt(courseRemoteService.findByUnitIdIn(unitIds.toArray(new String[]{}), section), new TR<List<Course>>(){});
		JSONObject job=null;
		JSONArray linJsonArr = new JSONArray();
		for (Course item : courseList) {
			job=new JSONObject();
			job.put("id", item.getId());
			job.put("name", item.getSubjectName());
			linJsonArr.add(job);
		}
		jsonArr.add(linJsonArr);
	}
	
	@ResponseBody
    @RequestMapping("/scoreStatistic/findCourseListForCla")
    @ControllerInfo("统计图-找班级考过的科目")
    public String showFindCourseList(String examId,String classId,ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		JSONArray jsonArr=new JSONArray();
		List<SubjectInfo> findByExamIdClassId = subjectInfoService.findByExamIdClassId(examId,classId);
		JSONObject job=null;
		String gradeCode = "";
		JSONArray linJsonArr = new JSONArray();
		for (SubjectInfo item : findByExamIdClassId) {
			job=new JSONObject();
			job.put("id", item.getSubjectId());
			job.put("name", item.getCourseName());
			linJsonArr.add(job);
			gradeCode = item.getRangeType();
		}
		jsonArr.add(linJsonArr);
		List<Borderline> findSubjectId32 = borderlineService.findSubjectId32(examId, gradeCode, ScoreDataConstants.STAT_TYPE10);
		linJsonArr = new JSONArray();
		for (Borderline item : findSubjectId32) {
			job=new JSONObject();
			job.put("id", item.getId());
			job.put("name", item.getNameOrUp());
			linJsonArr.add(job);
		}
		jsonArr.add(linJsonArr);
        return jsonArr.toJSONString();
    }
	
	@ResponseBody
	@RequestMapping("/scoreStatistic/findCourseLineList")
	@ControllerInfo("统计图-找科目对应的分数线")
	public String showFindCourseLine(String examId,String courseId,String classId,ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		JSONArray jsonArr=new JSONArray();
		List<SubjectInfo> findByExamIdClassId = subjectInfoService.findByExamIdClassId(examId,classId);
		String gradeCode = "";
		for (SubjectInfo item : findByExamIdClassId) {
			if(courseId.equals(item.getSubjectId())){
				gradeCode = item.getRangeType();
				break;
			}
		}
		if(StringUtils.isBlank(gradeCode)){
			return jsonArr.toJSONString();
		}
		List<Borderline> findBorderlineList = borderlineService.findBorderlineList(examId,gradeCode,ScoreDataConstants.STAT_TYPE10, courseId);
		JSONObject job=null;
		for (Borderline item : findBorderlineList) {
			job=new JSONObject();
			job.put("id", item.getId());
			job.put("name", item.getNameOrUp());
			jsonArr.add(job);
		}
		return jsonArr.toJSONString();
	}
	
	@RequestMapping("/scoreStatistic/showCharts/page")
	@ControllerInfo(value = "统计图showCharts")
	public String showCharts(String examId,String classId,String courseId,String courseLineId,String allLineId,String type,ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession){
		List<ScoreStatistic> sstList = new ArrayList<ScoreStatistic>();
		List<SubjectInfo> cifList = new ArrayList<SubjectInfo>(); 
		SubjectInfo cif = null;
		if(StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(classId) && StringUtils.isNotBlank(courseId)){
			sstList = scoreStatisticService.findList(examId,classId,courseId, type);
			cifList = subjectInfoService.findByExamIdClassId(examId, classId);
			for (SubjectInfo item : cifList) {
				if(courseId.equals(item.getSubjectId())){
					cif=item;
					break;
				}
			}
		}
		Clazz clazz = SUtils.dt(classService.findOneById(classId), new TypeReference<Clazz>(){});
		JSONObject json=new JSONObject();
		json.put("xName", "总分");//x轴名称
		json.put("yName", "单科");//y轴名称
		json.put("legendData", new String[]{"学生"});
		JSONArray jsonArr=new JSONArray();
		JSONArray jsonArr2=new JSONArray();
		if(CollectionUtils.isNotEmpty(sstList)){
			String[][] st = new String[sstList.size()][3];
			for (int i = 0; i < st.length; i++) {
				st[i][0]=String.valueOf(sstList.get(i).getAllScore());
				st[i][1]=sstList.get(i).getScore();
				st[i][2]=sstList.get(i).getStudentName();
			}
			jsonArr2.add(st);
		}
		jsonArr.add(jsonArr2);
		json.put("loadingData", jsonArr);
		//数据标线-适用于单图例及只有一组数据，若不需要则不用写下面内容
		if(CollectionUtils.isNotEmpty(sstList)){
			JSONObject json2=new JSONObject();
			if(StringUtils.isNotBlank(courseLineId)){
				Borderline bli = borderlineService.findOne(courseLineId);
				if(bli != null){
					json2.put("name", "学科分数线");
					int dS = 0;
					if(ScoreDataConstants.STAT_METHOD_DO11.equals(bli.getStatMethodDo())){
						//百分比
						json2.put("value", (int)(cif.getFullScore()*(Float.valueOf(bli.getRatioValue())/100.0)));
						dS = (int)(cif.getFullScore()*(Float.valueOf(bli.getRatioValue())/100.0));
					}else if(ScoreDataConstants.STAT_METHOD_DO12.equals(bli.getStatMethodDo())){
						//名次
						ScoreStatistic findOne = scoreStatisticService.findOne(clazz.getSchoolId(),clazz.getId(),examId,courseId,Integer.valueOf(bli.getRatioValue()), type, "1");
						if(findOne!=null){
							json2.put("value", findOne.getScore());
							Double valueOf = Double.valueOf(findOne.getScore());
							dS = valueOf.intValue();
						}
						
					}else if(ScoreDataConstants.STAT_METHOD_DO13.equals(bli.getStatMethodDo())){
						//分值
						json2.put("value", bli.getRatioValue());
						Double valueOf = Double.valueOf(bli.getRatioValue());
						dS = valueOf.intValue();
					}
					if(json2.containsKey("value")){
						json.put("horizontalLine", json2);//水平线，则需要设置x轴最小值和最大值
//						Float minX = sstList.get(sstList.size()-1).getAllScore();
//						if(minX-50>0){
//							json.put("xMin", minX-50);//x轴最小值
//						}else{
//							json.put("xMin", 0);//x轴最小值
//						}
//						json.put("xMax", sstList.get(0).getAllScore()+50);//x轴最大值
						
					}
				}
			}
			if(StringUtils.isNotBlank(allLineId)){
				Borderline bli = borderlineService.findOne(allLineId);
				if(bli != null){
					json2=new JSONObject();
					json2.put("name", "总分数线");
					int dS = 0;
					if(ScoreDataConstants.STAT_METHOD_DO11.equals(bli.getStatMethodDo())){
						//百分比
						List<SubjectInfo> findByExamIdIn = subjectInfoService.findByUnitIdExamId(null,examId,cif.getRangeType());
						Float allF = 0f;
						for (SubjectInfo courseInfo : findByExamIdIn) {
							allF+=courseInfo.getFullScore();
						}
						json2.put("value", (int)(allF*(Float.valueOf(bli.getRatioValue())/100.0)));
						dS = (int) (allF*(Float.valueOf(bli.getRatioValue())/100.0));
					}else if(ScoreDataConstants.STAT_METHOD_DO12.equals(bli.getStatMethodDo())){
						//名次
						ScoreStatistic findOne = scoreStatisticService.findOne(clazz.getSchoolId(),clazz.getId(),examId,courseId,Integer.valueOf(bli.getRatioValue()), type, "2");
						if(findOne!=null){
							json2.put("value", findOne.getAllScore());
							Double valueOf = Double.valueOf(findOne.getAllScore());
							dS = valueOf.intValue();
						}
					}else if(ScoreDataConstants.STAT_METHOD_DO13.equals(bli.getStatMethodDo())){
						//分值
						json2.put("value", bli.getRatioValue());
						Double valueOf = Double.valueOf(bli.getRatioValue());
						dS = valueOf.intValue();
					}
					
					if(json2.containsKey("value")){
						json.put("verticalLine", json2);//垂直线，则需要设置y轴最小值和最大值
//						json.put("yMin", 0);//y轴最小值
//						Float maxY = 0f;
//						if(cif!=null){
//							maxY=cif.getFullScore();
//						}
//						json.put("yMax", maxY);//y轴最大值
					}
				}
			}
		}
		map.put("jsonStringData", json.toString());
		return "/scoremanage/scoreStatistic/scoreStatisticCharts.ftl"; 
	}
	
	@ResponseBody
	@RequestMapping("/scoreStatistic/findSchoolList")
	@ControllerInfo("统计图-找对应学校")
	public String showFindSchoolList(String examId,String documentLabel,ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		JSONArray jsonArr=new JSONArray();
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		Set<String> unitIds = new HashSet<String>();
		if(StringUtils.isBlank(examId)){
			List<Unit> unitList = SUtils.dt(unitService.findDirectUnits(unitId, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});
			for (Unit unit : unitList) {
				unitIds.add(unit.getId());
			}
		}else{
			ExamInfo examInfo = examInfoService.findOne(examId);
			if(ScoreDataConstants.TKLX_1.equals(examInfo.getExamUeType())){
				List<Unit> unitList = SUtils.dt(unitService.findDirectUnits(unitId, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});
				for (Unit unit : unitList) {
					unitIds.add(unit.getId());
				}
			}else if(ScoreDataConstants.TKLX_3.equals(examInfo.getExamUeType())){
				List<JoinexamschInfo> joinexamschInfoList = joinexamschInfoService.findByExamInfoId(examInfo.getId());
				Set<String> schoolIds=new HashSet<String>();
				for (JoinexamschInfo item : joinexamschInfoList) {
					schoolIds.add(item.getSchoolId());
				}
				List<Unit> unitList = SUtils.dt(unitService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<Unit>>(){});
				for (Unit unit : unitList) {
					unitIds.add(unit.getId());
				}
			}
		}
		ChartsDetailDto chartsDetailDto = ScoreChartsConstants.SCORE_CHARTS_ALL_MAP.get(documentLabel);
		if(chartsDetailDto.getChartType() == 1){
			//bi图表
			Map<String, ChartsDetailParDto> parMap = chartsDetailDto.getParMap();
			if(parMap.containsKey("dept_id")){
				jsonArr = JSONArray.parseArray(treeService.deptForUnitInsetZTree(unitIds.toArray(new String[0])));
			}else if(parMap.containsKey("class_id")){
				jsonArr = JSONArray.parseArray(treeService.gradeClassForSchoolInsetZTree(unitIds.toArray(new String[0])));
			}else{
				jsonArr = JSONArray.parseArray(treeService.gradeForSchoolInsetZTree(unitIds.toArray(new String[0])));
			}
		}else{
			//echarts图表
			jsonArr = JSONArray.parseArray(treeService.gradeClassForSchoolInsetZTree(unitIds.toArray(new String[0])));
		}
		return jsonArr.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/scoreStatistic/findStudentList")
	@ControllerInfo("统计图-找对应的学生")
	public String findStudentByClass(String id,String type,ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		JSONArray jsonArr=new JSONArray();
		List<Student> studentList = null;
		if("1".equals(type)){
			//班级
			studentList = SUtils.dt(studentService.findByClassIds(id), new TR<List<Student>>(){});
		}else if("2".equals(type)){
			//年级
			studentList = SUtils.dt(studentService.findByGradeIds(id), new TR<List<Student>>(){});
		}
		JSONObject jsonObj = null;
		for (Student student : studentList) {
			jsonObj = new JSONObject();
			jsonObj.put("id", student.getId());
			jsonObj.put("name", student.getStudentName());
			jsonArr.add(jsonObj);
		}
		return jsonArr.toJSONString();
	}
	@ResponseBody
	@RequestMapping("/scoreStatistic/findTeacherList")
	@ControllerInfo("统计图-找对应的教师")
	public String findTeacherList(String id,ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		JSONArray jsonArr=new JSONArray();
		List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByDeptId(id), new TR<List<Teacher>>(){});
		JSONObject jsonObj = null;
		for (Teacher item : teacherList) {
			jsonObj = new JSONObject();
			jsonObj.put("id", item.getId());
			jsonObj.put("name", item.getTeacherName());
			jsonArr.add(jsonObj);
		}
		return jsonArr.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/scoreStatistic/findCourseListForTea")
	@ControllerInfo("统计图-找对应的科目")
	public String findCourseListForTea(String id,String examId,ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		JSONArray jsonArr=new JSONArray();
		List<Course> courseList = null;
		Teacher teacher = SUtils.dt(teacherRemoteService.findOneById(id), new TypeReference<Teacher>(){});
		Set<String> subjectIds = new HashSet<String>();
		if(StringUtils.isBlank(examId)){
			List<ClassTeaching> ctList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByTeacherId(teacher.getUnitId(),teacher.getId()), new TypeReference<List<ClassTeaching>>(){});
			for (ClassTeaching item : ctList) {
				subjectIds.add(item.getSubjectId());
			}
			courseList=SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])), new TR<List<Course>>(){});
		}else{
			ExamInfo findOne = examInfoService.findOne(examId);
			List<ClassTeaching> ctList = SUtils.dt(classTeachingRemoteService.findClassTeachingList(teacher.getUnitId(),findOne.getAcadyear(),findOne.getSemester(), teacher.getId()), new TypeReference<List<ClassTeaching>>(){});
			for (ClassTeaching item : ctList) {
				subjectIds.add(item.getSubjectId());
			}
			List<SubjectInfo> findByExamIdIn = subjectInfoService.findByExamIdIn(null, examId);
			Set<String> finSubjectIds = new HashSet<String>();
			if(CollectionUtils.isNotEmpty(findByExamIdIn)){
				for (SubjectInfo item : findByExamIdIn) {
					if(subjectIds.contains(item.getSubjectId())){
						finSubjectIds.add(item.getSubjectId());
					}
				}
			}
			courseList=SUtils.dt(courseRemoteService.findListByIds(finSubjectIds.toArray(new String[0])), new TR<List<Course>>(){});
		}
		JSONObject jsonObj = null;
		for (Course item : courseList) {
			jsonObj = new JSONObject();
			jsonObj.put("id", item.getId());
			jsonObj.put("name", item.getSubjectName());
			jsonArr.add(jsonObj);
		}
		return jsonArr.toJSONString();
	}

}
