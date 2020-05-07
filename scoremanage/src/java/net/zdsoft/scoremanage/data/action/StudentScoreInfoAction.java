package net.zdsoft.scoremanage.data.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Unit; 
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseTypeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.StudentScoreDto;
import net.zdsoft.scoremanage.data.entity.ClassInfo;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.Filtration;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ClassInfoService;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.FiltrationService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;
import net.zdsoft.stuwork.data.dto.DyBusinessOptionDto;
import net.zdsoft.stuwork.data.entity.DyStuEvaluation;
import net.zdsoft.stuwork.data.entity.DyStuHealthResult;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;


@Controller
@RequestMapping("/scoremanage/student")
public class StudentScoreInfoAction extends BaseAction{
	
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private ClassRemoteService classService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuService;
	@Autowired
	private StudentRemoteService studentService;
	@Autowired
	private ScoreInfoService scoreInfoService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private ClassInfoService classInfoService;
	@Autowired
	private FiltrationService filtrationService;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private ClassTeachingRemoteService classTeachingService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private CourseTypeRemoteService courseTypeRemoteService;
	@Autowired
	private UnitRemoteService unitService;
	@RequestMapping("/index/page")
    @ControllerInfo(value = "tab页")
    public String showTab(ModelMap map, String tabIndex) {
		if(StringUtils.isBlank(tabIndex)){
			tabIndex="1";//1:必修课 2:选修课
		}
		map.put("tabIndex", tabIndex);
		return "/scoremanage/student/studentScoreInfoTab.ftl";
	}
	
	@RequestMapping("/head/page")
    @ControllerInfo(value = "head页")
    public String showHead(ModelMap map, String tabIndex) {
        List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){}); 
        if(CollectionUtils.isEmpty(acadyearList)){
        	return errorFtl(map, "学年学期不存在");
		}
		String unitId=getLoginInfo().getUnitId();
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(1,unitId), Semester.class);
		//遗留学年学期都没有
		if(semester==null){
			return errorFtl(map, "学年学期不存在");
		}
		//页面参数
		String acadyearSearch = semester.getAcadyear();
		String semesterSearch=semester.getSemester()+"";
        if(StringUtils.isBlank(tabIndex)){
			tabIndex="1";
		}
		map.put("acadyearList", acadyearList);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
		map.put("unitId",unitId);
		map.put("tabIndex", tabIndex);
		return "/scoremanage/student/studentScoreInfoHead.ftl";	
    }
	
	@ResponseBody
  	@RequestMapping("/examList")
  	public List<ExamInfo> examList(String acadyear,String semester,String searchType,String unitId){
  		List<ExamInfo> examList=new ArrayList<ExamInfo>();
  		if(StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)){
  			return examList;
  		}
  		String studentId = getLoginInfo().getOwnerId();
  		List<TeachClass> teachClassList = SUtils.dt(teachClassStuService.findTeachClassByStudentId(studentId, acadyear, semester),TeachClass.class);
  		List<String> classIds = EntityUtils.getList(teachClassList,"id");
  		Student student = SUtils.dc(studentService.findOneById(studentId),Student.class);
  		classIds.add(student.getClassId());
  		List<ClassInfo> classInfoList = classInfoService.findBySchoolIdAndClassIdIn(unitId,classIds.toArray(new String[0]));
  		Set<String> subjectInfoIds = EntityUtils.getSet(classInfoList, "subjectInfoId");
  		List<SubjectInfo> subjectInfoList = subjectInfoService.findListByIds(subjectInfoIds.toArray(new String[0]));
  		Set<String> examIds = EntityUtils.getSet(subjectInfoList, "examId");
  		//去除不排考的考试
  		List<Filtration> filtrationList = filtrationService.findBySchoolIdAndStudentIdAndType(unitId, studentId, ScoreDataConstants.FILTER_TYPE1);
  		if(CollectionUtils.isNotEmpty(filtrationList)){
  			Set<String> filtExamIds = EntityUtils.getSet(filtrationList, "examId");
  			examIds.removeAll(filtExamIds);
  		}
  		examList = examInfoService.findListByIds(examIds.toArray(new String[0]));
  		Iterator<ExamInfo> iterator = examList.iterator();
  		while(iterator.hasNext()){
  			ExamInfo examInfo = iterator.next();
  			if(!acadyear.equals(examInfo.getAcadyear())||!semester.equals(examInfo.getSemester())){
  				iterator.remove();
  			}
  		}
  		if(CollectionUtils.isNotEmpty(examList)){
			for (ExamInfo item : examList) {
				String examNameNew  = item.getExamName();
				if("1".equals(item.getIsgkExamType())){
					examNameNew += "<新高考>";
				}
				if(ScoreDataConstants.TKLX_3.equals(item.getExamUeType())){
					examNameNew += "（校校）";
				}
				item.setExamNameOther(examNameNew);
			}
		}
        return examList;
  	}
	
	@RequestMapping("/list/page")
	@ControllerInfo("显示列表")
	public String showList( ModelMap map, String tabIndex,String acadyearSearch,String semesterSearch,String examId ) {
		String unitId = getLoginInfo().getUnitId();
		String studentId = getLoginInfo().getOwnerId();
		if(!"1".equals(tabIndex) && !"3".equals(tabIndex)){
			examId = Constant.GUID_ZERO;
		}else{
			if(StringUtils.isBlank(examId)){
				 return "/scoremanage/student/studentScoreInfoList.ftl";
			}
		}

		//公用
		List<TeachClass> teachClassList = SUtils.dt(teachClassStuService.findTeachClassByStudentId(studentId, acadyearSearch, semesterSearch),TeachClass.class);
		List<ScoreInfo> scoreInfoList = scoreInfoService.findByCondition(unitId, acadyearSearch, semesterSearch, studentId, examId);
		
		List<StudentScoreDto> dtoList = new ArrayList<StudentScoreDto>();
		if("1".equals(tabIndex)){//必修课成绩
			ExamInfo examInfo = examInfoService.findOne(examId);
			//是否需要显示总评成绩
			if(ScoreDataConstants.EXAM_TYPE_FINAL.equals(examInfo.getExamType())){
				map.put("needGeneral", true);
			}
			
			//获取学生所在的班级及考试科目
	  		Map<String, String> classNameMap = EntityUtils.getMap(teachClassList, "id","name");
	  		List<String> classIds = EntityUtils.getList(teachClassList,"id");
	  		Student student = SUtils.dc(studentService.findOneById(studentId),Student.class);
	  		classIds.add(student.getClassId());
	  		Clazz clazz = SUtils.dc(classService.findOneById(student.getClassId()),Clazz.class);
	  		List<ClassInfo> classInfoList = classInfoService.findByExamInfoId(examId);
	  		Iterator<ClassInfo> iterator = classInfoList.iterator();
	  		while(iterator.hasNext()){
	  			if(!classIds.contains(iterator.next().getClassId())){
	  				iterator.remove();
	  			}
	  		}
	  		Set<String> subjectInfoIds = EntityUtils.getSet(classInfoList, "subjectInfoId");
	  		List<SubjectInfo> subjectInfoList = subjectInfoService.findListByIds(subjectInfoIds.toArray(new String[0]));
	  		Set<String> subjectIds = EntityUtils.getSet(subjectInfoList,"subjectId");
	  		Map<String, String> subjectInfoMap = EntityUtils.getMap(subjectInfoList, "id","subjectId");
	  		List<Course> courseList = SUtils.dt(courseService.findListByIds(subjectIds.toArray(new String[0])),Course.class);
	  		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, "id","subjectName");
	  		Map<String, ScoreInfo> classScoreMap = EntityUtils.getMap(scoreInfoList, "teachClassId");
	  		Map<String, ScoreInfo> subjectScoreMap = EntityUtils.getMap(scoreInfoList, "subjectId");
	  		
	  		
	  		//组装数据
	  		StudentScoreDto dto = null;
	  		for (ClassInfo classInfo : classInfoList) {
				dto = new StudentScoreDto();
				dto.setClassId(classInfo.getClassId());
				String subjectId = subjectInfoMap.get(classInfo.getSubjectInfoId());
				dto.setSubjectId(subjectId);
				dto.setSubjectName(courseNameMap.get(subjectId));
				if(ScoreDataConstants.CLASS_TYPE1.equals(classInfo.getClassType())){
					dto.setClassName(clazz.getClassNameDynamic());
				}else if(classNameMap.containsKey(classInfo.getClassId())){
					dto.setClassName(classNameMap.get(classInfo.getClassId()));
				}
				if(Constant.IS_TRUE_Str.equals(classInfo.getIsLock())){
					dto.setIsLock("已录入");
					ScoreInfo scoreInfo = classScoreMap.get(classInfo.getClassId());
					if(ScoreDataConstants.CLASS_TYPE1.equals(classInfo.getClassType())){
						scoreInfo = subjectScoreMap.get(subjectId);
					}
					if(scoreInfo!=null){
						dto.setScore(scoreInfo.getScore());
						dto.setScoreStatus(scoreInfo.getScoreStatus());
						dto.setToScore(scoreInfo.getToScore());
					}
				}else{
					dto.setIsLock("未录入");
				}
				dtoList.add(dto);
			}
	  		map.put("tabIndex", tabIndex);
			map.put("dtoList", dtoList);
		    return "/scoremanage/student/studentScoreInfoList.ftl";
		}else if("2".equals(tabIndex)){//选修课成绩
			List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingService.findByUnitIdAndAcadyearAndSemesterAndSubjectType(unitId, acadyearSearch, semesterSearch, Integer.parseInt(BaseConstants.SUBJECT_TYPE_XX)),ClassTeaching.class);
			if(CollectionUtils.isEmpty(classTeachingList)){
				return "/scoremanage/student/studentScoreInfoList.ftl";
			}
			Set<String> courseIds = EntityUtils.getSet(classTeachingList, "subjectId");
			Iterator<TeachClass> iterator = teachClassList.iterator();
			while(iterator.hasNext()){
				TeachClass teachClass = iterator.next();
				if(!courseIds.contains(teachClass.getCourseId())){
					iterator.remove();
				}
			}
			
			List<Course> courseList = SUtils.dt(courseService.findListByIds(courseIds.toArray(new String[0])),Course.class);
	  		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, "id","subjectName");
	  		List<ClassInfo> classInfoList = classInfoService.findByAll(unitId, ScoreDataConstants.CLASS_TYPE2, courseIds.toArray(new String[0]));
	  		Map<String, ClassInfo> classInfoMap = EntityUtils.getMap(classInfoList, "classId");
	  		Map<String,ScoreInfo> classScoreMap = EntityUtils.getMap(scoreInfoList,"teachClassId");
	  		
			StudentScoreDto dto = null;
			for (TeachClass teachClass : teachClassList) {
				dto = new StudentScoreDto();
				dto.setClassId(teachClass.getId());
				dto.setSubjectId(teachClass.getCourseId());
				dto.setSubjectName(courseNameMap.get(teachClass.getCourseId()));
				dto.setClassName(teachClass.getName());
				ClassInfo classInfo = classInfoMap.get(teachClass.getId());
				if(StringUtils.isNotBlank(teachClass.getParentId())){
					classInfo = classInfoMap.get(teachClass.getParentId());
				}
				if(classInfo!=null && Constant.IS_TRUE_Str.equals(classInfo.getIsLock())){
					dto.setIsLock("已录入");
					ScoreInfo scoreInfo = classScoreMap.get(teachClass.getId());
					if(scoreInfo!=null){
						dto.setScore(scoreInfo.getScore());
						dto.setScoreStatus(scoreInfo.getScoreStatus());
						dto.setToScore(scoreInfo.getToScore());
						if(StringUtils.isNotBlank(scoreInfo.getToScore())) {
							dto.setIsPass(Float.parseFloat(scoreInfo.getToScore())>0f?"是":"否");
						}
					}
				}else{
					dto.setIsLock("未录入");
				}
				dtoList.add(dto);
			}
			map.put("tabIndex", tabIndex);
			map.put("dtoList", dtoList);
		    return "/scoremanage/student/studentScoreInfoList.ftl";
		}else{
			if(StringUtils.isNotBlank(studentId)){
				Student student = SUtils.dc(studentService.findOneById(studentId), Student.class);
				dealData(acadyearSearch, semesterSearch, studentId, examId, map);
				map.put("studentName", student.getStudentName());
				map.put("studentCode", student.getStudentCode());
				Clazz cls = SUtils.dc(classService.findOneById(student.getClassId()), Clazz.class);
				map.put("className", cls.getClassNameDynamic());
				Unit unit = SUtils.dc(unitService.findOneById(getLoginInfo().getUnitId()), Unit.class);
				String unitName = unit.getUnitName();
				String schName = "";
				if(unitName.contains("数字校园")){
					schName = unitName.replace("数字校园", "");
				}else{
					schName = unitName;
				}
				map.put("unitName", schName);
			}
			map.put("examId", examId);
			map.put("tabIndex", tabIndex);
			map.put("dtoList", dtoList);
			map.put("acadyear", acadyearSearch);
			map.put("semester", semesterSearch);
			map.put("nowDate", new Date());
		    return "/scoremanage/student/studentScoreReport.ftl";
		}		
	}
	
	public void dealData(String acadyear, String semester, String studentId, String examId, ModelMap map){
		//操行等第
				DyBusinessOptionDto dyBusinessOptionDto = new DyBusinessOptionDto();
				dyBusinessOptionDto.setAcadyear(acadyear);
				dyBusinessOptionDto.setSemester(String.valueOf(semester));
				dyBusinessOptionDto.setStudentId(studentId);
				DyStuEvaluation dyStuEvaluation = SUtils.dc(stuworkRemoteService.findStuEvaluationOneByUidAndDto(getLoginInfo().getUnitId(), dyBusinessOptionDto), DyStuEvaluation.class);
				if(null!=dyStuEvaluation){
					map.put("optionName", dyStuEvaluation.getGrade());
					map.put("remark", dyStuEvaluation.getRemark());
				}
				//体检信息
				List<DyStuHealthResult> dyStuHealthResultList = SUtils.dt(stuworkRemoteService.findStuHealthResultOneByStudnetId(getLoginInfo().getUnitId(), acadyear, semester, studentId), new TR<List<DyStuHealthResult>>() {});
				map.put("dyStuHealthResultList", dyStuHealthResultList);
				List<ScoreInfo> scoreInfoList = scoreInfoService.findByCondition(getLoginInfo().getUnitId(), acadyear, semester, studentId, examId);
				Set<String> subjectIdSet = new HashSet<String>();
				for(ScoreInfo info : scoreInfoList){
					subjectIdSet.add(info.getSubjectId());
				}
				if(CollectionUtils.isNotEmpty(subjectIdSet)){
					List<Course> courseList = SUtils.dt(courseService.findBySubjectIdIn(subjectIdSet.toArray(new String[0])), new TR<List<Course>>() {});
					Map<String, String> courseNameMap = new HashMap<String, String>();
					for(Course course : courseList){
						courseNameMap.put(course.getId(), course.getSubjectName());
					}
					for(ScoreInfo score : scoreInfoList){
						score.setSubjectName(courseNameMap.get(score.getSubjectId()));
					}
				}
				map.put("scoreInfoList", scoreInfoList);
				//选修课成绩
				List<ScoreInfo> optionalScoreInfoList = scoreInfoService.findByCondition(getLoginInfo().getUnitId(), acadyear, semester, studentId, "00000000000000000000000000000000");
				Set<String> teachSubjectIdSet = new HashSet<String>();
				for(ScoreInfo info : optionalScoreInfoList){
					teachSubjectIdSet.add(info.getSubjectId());
				}
				if(CollectionUtils.isNotEmpty(teachSubjectIdSet)){
					List<Course> teachCourseList = SUtils.dt(courseService.findListByIds(teachSubjectIdSet.toArray(new String[0])),Course.class);
					Map<String, String> courseNameMap =  new HashMap<String, String>();
					Map<String, String> courseTypeIdMap =  new HashMap<String, String>();
					Set<String> courseTypeIdSet = new HashSet<String>();
					for(Course course : teachCourseList){
						courseNameMap.put(course.getId(), course.getSubjectName());
						courseTypeIdMap.put(course.getId(), course.getCourseTypeId());
						courseTypeIdSet.add(course.getCourseTypeId());
					}
					List<CourseType> courseTypeList = SUtils.dt(courseTypeRemoteService.findListByIds(courseTypeIdSet.toArray(new String[0])), new TR<List<CourseType>>() {});
					Map<String, String> courseTypeNameMap = new HashMap<String, String>();
					for(CourseType type : courseTypeList){
						courseTypeNameMap.put(type.getId(), type.getName());
					}
					for(ScoreInfo score : optionalScoreInfoList){
						score.setSubjectName(courseNameMap.get(score.getSubjectId()));
						score.setCourseTypeName(courseTypeNameMap.get(courseTypeIdMap.get(score.getSubjectId())));
					}
				}
				map.put("optionalScoreInfoList", optionalScoreInfoList);
	}
	
}
