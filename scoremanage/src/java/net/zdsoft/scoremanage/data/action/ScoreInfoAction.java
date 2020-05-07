package net.zdsoft.scoremanage.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.ScoreInfoDto;
import net.zdsoft.scoremanage.data.dto.ScoreLimitSearchDto;
import net.zdsoft.scoremanage.data.entity.ClassInfo;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.entity.ScoreLimit;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ClassInfoService;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.ExamNumService;
import net.zdsoft.scoremanage.data.service.FiltrationService;
import net.zdsoft.scoremanage.data.service.NotLimitService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.scoremanage.data.service.ScoreLimitService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
@Controller
@RequestMapping("/scoremanage")
public class ScoreInfoAction extends BaseAction{
	
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private ClassInfoService classInfoService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassService;
	@Autowired
	private ScoreInfoService scoreInfoService;
	@Autowired
	private StudentRemoteService studentService;
	@Autowired
	private ExamNumService examNumService;
	@Autowired
	private FiltrationService filtrationService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private ScoreLimitService scoreLimitService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private NotLimitService notLimitService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	
	private static Logger logger = Logger.getLogger(ScoreInfoAction.class);
	
	@RequestMapping("/scoreInfo/lock/index/page")
    @ControllerInfo(value = "锁定成绩")
	public String showLockIndex(ModelMap map, HttpSession httpSession, HttpServletRequest request, String noLimit) {
		List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>() {
		});
		if (CollectionUtils.isEmpty(acadyearList)) {
			return errorFtl(map, "学年学期不存在");
		}
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(1,unitId), Semester.class);
		if (semester == null) {
			return errorFtl(map, "学年学期不存在");
		}
		//页面参数(初始值)
		String acadyearSearch = request.getParameter("acadyearSearch"); 
		String semesterSearch = request.getParameter("semesterSearch");
		if (StringUtils.isEmpty(acadyearSearch)) {
			acadyearSearch = semester.getAcadyear();
			semesterSearch = semester.getSemester()+"";
		}
		String examId= request.getParameter("examId");
	    String gradeCode= request.getParameter("gradeCode");
	    String subjectInfoId= request.getParameter("subjectInfoId");
		//班级类型 默认行政班
		String classType=ScoreDataConstants.CLASS_TYPE1;
		
		map.put("acadyearList", acadyearList);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
		map.put("examId",examId);
		map.put("gradeCode", gradeCode);
		map.put("subjectInfoId", subjectInfoId);
		map.put("unitId",unitId);
		map.put("noLimit", noLimit);
		map.put("classType",classType);
		map.put("type", 1);
		return "/scoremanage/scoreInfo/scoreLockIndex.ftl";
	}
	
	@RequestMapping("/scoreInfo/index/page")
    @ControllerInfo(value = "录入成绩")
    public String showIndex(ModelMap map, HttpSession httpSession, HttpServletRequest request, String noLimit) {
		 List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){}); 
	     if(CollectionUtils.isEmpty(acadyearList)){
	        return errorFtl(map, "学年学期不存在");
	     }
        LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(1,unitId), Semester.class);
		if(semester==null){
			return errorFtl(map, "学年学期不存在");
		}
		map.put("type", 1);
		//判断录分角色 无 普通 管理员 2者都有
		if(noLimit == null){
			List<String> teacherIds = scoreLimitService.findTeacherIdByUnitId(unitId, BaseConstants.SUBJECT_TYPE_BX);
			List<String> noLimitTeacherIds = notLimitService.findTeacherIdByUnitId(unitId);
			if(teacherIds.contains(loginInfo.getOwnerId()) && noLimitTeacherIds.contains(loginInfo.getOwnerId())){
				//有两个录分角色，需要选择一个
				return "/scoremanage/scoreInfo/selectRecordingRole.ftl";
			}
			if(StringUtils.isBlank(noLimit) && noLimitTeacherIds.contains(getLoginInfo().getOwnerId())){
				noLimit = "1";
			} else {
				noLimit = "0";
			}
			return "redirect:/scoremanage/scoreInfo/lock/index/page?noLimit="+noLimit;
		}
		
		//页面参数(初始值)
		String acadyearSearch = request.getParameter("acadyearSearch"); 
		String semesterSearch = request.getParameter("semesterSearch");
		if (StringUtils.isEmpty(acadyearSearch)) {
			acadyearSearch = semester.getAcadyear();
			semesterSearch = semester.getSemester()+"";
		}
		String classType = request.getParameter("classType");
		if (StringUtils.isEmpty(classType)) {
			classType = ScoreDataConstants.CLASS_TYPE1;
		}
		String examId= request.getParameter("examId");
	    String gradeCode= request.getParameter("gradeCode");
		String classIdSearch= request.getParameter("classIdSearch");
	    String subjectId= request.getParameter("subjectId");
	    String subjectInfoId= request.getParameter("subjectInfoId");
	    
	    map.put("examId", examId);
		map.put("gradeCode", gradeCode);
		map.put("classIdSearch", classIdSearch);
		map.put("subjectId", subjectId);
		map.put("subjectInfoId", subjectInfoId);
		
		map.put("acadyearList", acadyearList);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
		map.put("unitId", unitId);
		map.put("noLimit", noLimit);
		//普通录分老师班级类型控制
		if(!"1".equals(noLimit)){
			ScoreLimitSearchDto dto = new ScoreLimitSearchDto();
			dto.setAcadyear(acadyearSearch);
			dto.setSemester(semesterSearch);
			dto.setUnitId(unitId);
			dto.setTeacherId(getLoginInfo().getOwnerId());
			
			List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(dto);
			if(CollectionUtils.isNotEmpty(limitList)){
				Iterator<ScoreLimit> iterator = limitList.iterator();
				while(iterator.hasNext()){
					if(Constant.GUID_ZERO.equals(iterator.next().getExamInfoId())){
						iterator.remove();
					}
				}
				Set<String> classTypeSet = EntityUtils.getSet(limitList, ScoreLimit::getClassType);
				if(classTypeSet.size()==1){
					for (String t : classTypeSet) {
						classType=t;
					}
				}
			}
	    }
		map.put("classType", classType);
		return "/scoremanage/scoreInfo/scoreInfoIndex.ftl";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/scoreInfo/lock/page")
    @ControllerInfo(value = "班级锁定列表")
	public String tabLockList(ModelMap map, HttpServletRequest request) {
		String acadyearSearch = request.getParameter("acadyearSearch"); 
		String semesterSearch = request.getParameter("semesterSearch");
		String examId= request.getParameter("examId");
	    String gradeCode= request.getParameter("gradeCode");
	    String subjectId= request.getParameter("subjectInfoId");
	    String noLimit= request.getParameter("noLimit");
	    if(StringUtils.isEmpty(subjectId)) {
	    	return "/scoremanage/scoreInfo/scoreLockList.ftl";
	    }
	    SubjectInfo si = subjectInfoService.findOne(subjectId);
	    if(si == null) {
	    	return "/scoremanage/scoreInfo/scoreLockList.ftl";
	    }
	    List<ClassInfo> inputDtoList = classInfoService.findBySchoolIdAndSubjectInfoIdIn(getLoginInfo().getUnitId(), subjectId);
	    if(CollectionUtils.isNotEmpty(inputDtoList) && !"1".equals(noLimit)) {// 普通老师过滤
	    	ScoreLimitSearchDto searchDto = new ScoreLimitSearchDto();
			searchDto.setExamId(examId);
			searchDto.setAcadyear(acadyearSearch);
			searchDto.setSemester(semesterSearch);
			searchDto.setUnitId(getLoginInfo().getUnitId());
			searchDto.setTeacherId(getLoginInfo().getOwnerId());
			searchDto.setSubjectId(si.getSubjectId());
			List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(searchDto);
			if(CollectionUtils.isNotEmpty(limitList)){
				Set<String> limitClassIds = EntityUtils.getSet(limitList, ScoreLimit::getClassId);
				if(CollectionUtils.isNotEmpty(limitClassIds)) {
					inputDtoList = inputDtoList.stream().filter(e->limitClassIds.contains(e.getClassId())).collect(Collectors.toList());
				} else {
					inputDtoList = null;
					return "/scoremanage/scoreInfo/scoreLockList.ftl";
				}
			}
	    }
	    if(CollectionUtils.isEmpty(inputDtoList)) {
	    	return "/scoremanage/scoreInfo/scoreLockList.ftl";
	    }
	    
	    // 班级名称、教师名称组装
	    List<ClassInfo> jxbs = inputDtoList.stream().filter(e->ScoreDataConstants.CLASS_TYPE2.equals(e.getClassType())).collect(Collectors.toList());
	    List<ClassInfo> xzbs = null;
	    if(CollectionUtils.isNotEmpty(jxbs)) {
	    	xzbs = (List<ClassInfo>) CollectionUtils.removeAll(inputDtoList, jxbs);
	    } else {
	    	xzbs = inputDtoList;
	    }
	    Map<String, String> cnMap = new HashMap<>();
	    Map<String, String> ctnMap = new HashMap<>();
	    List<String> tids = new ArrayList<>();
	    if(CollectionUtils.isNotEmpty(xzbs)) {// 行政班任课老师
	    	String[] cids = EntityUtils.getList(xzbs, ClassInfo::getClassId).toArray(new String[0]);
	    	cnMap.putAll(toMakeClassMap(ScoreDataConstants.CLASS_TYPE1, cids));
	    	List<ClassTeaching> cts = SUtils.dt(classTeachingRemoteService.findByClassIdsSubjectIds(getLoginInfo().getUnitId(), 
	    				acadyearSearch, semesterSearch, cids, new String[] {si.getSubjectId()}, false), 
	    			new TR<List<ClassTeaching>>() {});
	    	for(ClassTeaching ct : cts) {
	    		if(StringUtils.isEmpty(ct.getTeacherId())) {
	    			continue;
	    		}
	    		if(!tids.contains(ct.getTeacherId())) {
	    			tids.add(ct.getTeacherId());
	    		}
	    		ctnMap.put(ct.getClassId(), ct.getTeacherId());
	    	}
	    }
	    if(CollectionUtils.isNotEmpty(jxbs)) {// 教学班名称及老师
	    	List<TeachClass> tcalssList = SUtils.dt(teachClassService.findListByIds(EntityUtils.getList(jxbs, ClassInfo::getClassId).toArray(new String[0])), new TR<List<TeachClass>>(){});
			if(CollectionUtils.isNotEmpty(tcalssList)){
				for(TeachClass t:tcalssList){
					if(t.getIsDeleted()==Constant.IS_DELETED_TRUE){
						continue;
					}
					cnMap.put(t.getId(), t.getName());
					if(StringUtils.isEmpty(t.getTeacherId())) {
		    			continue;
		    		}
					if(!tids.contains(t.getTeacherId())) {
						tids.add(t.getTeacherId());
					}
					ctnMap.put(t.getId(), t.getTeacherId());
				}
			}
	    }
	    Map<String, String> tnMap = null;
	    if(tids.size() > 0) {
	    	List<Teacher> teas = teacherRemoteService.findListObjectBy(Teacher.class, null, null, "id",
	    			tids.toArray(), new String[] {"id","teacherName"});
	    	if(CollectionUtils.isNotEmpty(teas)) {
	    		tnMap = EntityUtils.getMap(teas, Teacher::getId, Teacher::getTeacherName);
	    	}
	    }
	    boolean hasTea = MapUtils.isNotEmpty(tnMap);
	    for(ClassInfo ci : inputDtoList) {
	    	if(cnMap.containsKey(ci.getClassId())) {
	    		ci.setClassName(cnMap.get(ci.getClassId()));
	    	}
	    	if(hasTea && ctnMap.containsKey(ci.getClassId())) {
	    		ci.setTeacherNames(tnMap.get(ctnMap.get(ci.getClassId())));
	    	}
	    }
	    map.put("noLimit", noLimit);
	    map.put("examId", examId);
	    map.put("gradeCode", gradeCode);
	    map.put("inputDtoList", inputDtoList);
	    map.put("subjectId", si.getSubjectId());
		return "/scoremanage/scoreInfo/scoreLockList.ftl";
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/scoreInfo/setNumSpan")
	public String setNumSpan(HttpServletRequest request) {
		JSONArray jsona = new JSONArray();
		String examId= request.getParameter("examId");
	    String subjectId= request.getParameter("subjectInfoId");
	    String acadyearSearch = request.getParameter("acadyearSearch"); 
		String semesterSearch = request.getParameter("semesterSearch");
		String noLimit= request.getParameter("noLimit");
		SubjectInfo si = subjectInfoService.findOne(subjectId);
		if(si == null) {
			return jsona.toJSONString();
		}
	    List<ClassInfo> inputDtoList = classInfoService.findBySchoolIdAndSubjectInfoIdIn(getLoginInfo().getUnitId(), subjectId);
	    if(CollectionUtils.isNotEmpty(inputDtoList) && !"1".equals(noLimit)) {// 普通老师过滤
	    	ScoreLimitSearchDto searchDto = new ScoreLimitSearchDto();
			searchDto.setExamId(examId);
			searchDto.setAcadyear(acadyearSearch);
			searchDto.setSemester(semesterSearch);
			searchDto.setUnitId(getLoginInfo().getUnitId());
			searchDto.setTeacherId(getLoginInfo().getOwnerId());
			searchDto.setSubjectId(si.getSubjectId());
			List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(searchDto);
			if(CollectionUtils.isNotEmpty(limitList)){
				Set<String> limitClassIds = EntityUtils.getSet(limitList, ScoreLimit::getClassId);
				if(CollectionUtils.isNotEmpty(limitClassIds)) {
					inputDtoList = inputDtoList.stream().filter(e->limitClassIds.contains(e.getClassId())).collect(Collectors.toList());
				} else {
					inputDtoList = null;
				}
			}
	    }
	    if(CollectionUtils.isEmpty(inputDtoList)) {
	    	return jsona.toJSONString();
	    }
	    List<ClassInfo> jxbs = inputDtoList.stream().filter(e->ScoreDataConstants.CLASS_TYPE2.equals(e.getClassType())).collect(Collectors.toList());
	    List<ClassInfo> xzbs = null;
	    if(CollectionUtils.isNotEmpty(jxbs)) {
	    	xzbs = (List<ClassInfo>) CollectionUtils.removeAll(inputDtoList, jxbs);
	    } else {
	    	xzbs = inputDtoList;
	    }
	    Map<String, Integer> numMap = new HashMap<>();// 班级总人数
	    Map<String, Integer> tjMap = new HashMap<>();// 已提交人数
	    if(CollectionUtils.isNotEmpty(xzbs)) {
	    	String[] cids = EntityUtils.getList(xzbs, ClassInfo::getClassId).toArray(new String[0]);
	    	numMap.putAll(SUtils.dt(studentService.countMapByClassIds(cids), new TR<Map<String, Integer>>(){}));
	    	tjMap.putAll(scoreInfoService.findNumByExamIdClsIds(examId, ScoreDataConstants.CLASS_TYPE1, cids));
	    }
	    if(CollectionUtils.isNotEmpty(jxbs)) {
	    	String[] cids = EntityUtils.getList(jxbs, ClassInfo::getClassId).toArray(new String[0]);
			List<Student> stulist = SUtils.dt(studentService.findListBlendClassIds(cids), new TR<List<Student>>() {});
	    	//List<TeachClassStu> stus = SUtils.dt(
            //        teachClassStuRemoteService.findByClassIds(cids),
            //        new TR<List<TeachClassStu>>() {
            //        });
	    	if(CollectionUtils.isNotEmpty(stulist)) {
	    		Iterator<Student> it = stulist.iterator();
	    		while(it.hasNext()) {
					Student en = it.next();
	    			if (numMap.containsKey(en.getClassId())) {
						numMap.put(en.getClassId(), numMap.get(en.getClassId())+1);
					} else {
						numMap.put(en.getClassId(), 1);
					}
	    		}
	    	}
	    	tjMap.putAll(scoreInfoService.findNumByExamIdClsIds(examId, ScoreDataConstants.CLASS_TYPE2, cids));
	    }
	    
	    for(ClassInfo ci : inputDtoList) {
	    	Integer cn = numMap.get(ci.getClassId());
	    	if(cn == null) {
	    		cn = 0;
	    	}
	    	Integer tj = tjMap.get(ci.getClassId() + si.getSubjectId());
	    	if(tj == null) {
	    		tj = 0;
	    	}
	    	JSONObject json = new JSONObject();
	    	json.put("allNum", cn);
	    	json.put("scoreNum", tj);
	    	jsona.add(json);
	    }
		return jsona.toJSONString();
	}
	
	private Map<String,String> toMakeClassMap(String classType,String[] classIds){
		Map<String,String> classMap=new LinkedHashMap<String, String>();
		if(classIds==null || classIds.length<=0){
			return classMap;
		}
		if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
			//行政班
			List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(classIds), new TR<List<Clazz>>(){});
			if(CollectionUtils.isNotEmpty(classList)){
				for(Clazz z:classList){
					if(z.getIsDeleted()==Constant.IS_DELETED_TRUE){
						continue;
					}
					classMap.put(z.getId(), z.getClassNameDynamic());
				}
			}
		}else{
			List<TeachClass> tcalssList = SUtils.dt(teachClassService.findListByIds(classIds), new TR<List<TeachClass>>(){});
			if(CollectionUtils.isNotEmpty(tcalssList)){
				for(TeachClass t:tcalssList){
					if(t.getIsDeleted()==Constant.IS_DELETED_TRUE){
						continue;
					}
					classMap.put(t.getId(), t.getName());
				}
			}
		}
		return classMap;
	}

	@RequestMapping("/scoreInfo/tablist/page")
    @ControllerInfo(value = "科目tab")
	public String tabList(ModelMap map, HttpSession httpSession,HttpServletRequest request){
		//录入成绩学校端
		String examId= request.getParameter("examId");
	    String classIdSearch= request.getParameter("classIdSearch");
	    String subjectId= request.getParameter("subjectId");
	    String noLimit = request.getParameter("noLimit");
	    String subInfoId = "";
	    //某个班级一次考试中科目不可能有重复(关联查询)
	    List<SubjectInfo> infoList=subjectInfoService.findByExamIdClassId(examId,classIdSearch);
	    
	    if (StringUtils.isNotEmpty(subjectId) && !subjectId.equals("undefined")) {
	    	for (SubjectInfo info : infoList) {
	    		if (subjectId.equals(info.getSubjectId())) {
	    			subInfoId = info.getId();
	    			break;
	    		}
	    	}
	    }
	    //普通录分老师科目控制
	    if(CollectionUtils.isNotEmpty(infoList) && !"1".equals(noLimit)){
		    ExamInfo examInfo = examInfoService.findOne(examId);
			ScoreLimitSearchDto searchDto = new ScoreLimitSearchDto();
			searchDto.setExamId(examId);
			searchDto.setAcadyear(examInfo.getAcadyear());
			searchDto.setSemester(examInfo.getSemester());
			searchDto.setUnitId(getLoginInfo().getUnitId());
			searchDto.setClassIds(new String[]{classIdSearch});
			searchDto.setTeacherId(getLoginInfo().getOwnerId());
			
			List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(searchDto);
			if(CollectionUtils.isNotEmpty(limitList)){
				Set<String> courseIdSet = EntityUtils.getSet(limitList, ScoreLimit::getSubjectId);
				Iterator<SubjectInfo> iterator = infoList.iterator();
				while(iterator.hasNext()){
					if(!courseIdSet.contains(iterator.next().getSubjectId())){
						iterator.remove();
					}
				}
			}else{
				infoList = new ArrayList<SubjectInfo>();
			}
	    }
	    if(StringUtils.isEmpty(subInfoId)) {
	    	subjectId = "";
	    }
	    map.put("infoList",infoList);
	    map.put("subjectId", subjectId);
	    map.put("subInfoId", subInfoId);
		return "/scoremanage/scoreInfo/scoreInfoTabIndex.ftl";
	}
	@RequestMapping("/scoreInfo/alllist/page")
    @ControllerInfo(value = "某个班级全部成绩列表 不分页")
	public String scoreLists(ModelMap map, HttpSession httpSession,HttpServletRequest request){
		String examId= request.getParameter("examId");
	    String classType= request.getParameter("classType");
	    String classIdSearch= request.getParameter("classIdSearch");
	    LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		//根据classIdSearch拿到该班级下面的正常学生
		Set<String> stuIds=new HashSet<String>();//为了分页先这样 但是前提学生id不能太多
		List<Student> stuList=null;
		if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
			//行政班
			stuList= SUtils.dt(studentService.findByClassIds(new String[]{classIdSearch}),new TR<List<Student>>(){});
		}else{
			stuList = SUtils.dt(studentService.findByTeachClassIds(new String[]{classIdSearch}),new TR<List<Student>>(){});
		}
		//用于组装学生信息
		Map<String,Student> stuMap=new HashMap<String, Student>();
		if(CollectionUtils.isNotEmpty(stuList)){
			for(Student student:stuList){
				stuMap.put(student.getId(), student);
				stuIds.add(student.getId());
			}
		}
		
		//页面返回值
		List<ScoreInfoDto> dtoList=new ArrayList<ScoreInfoDto>();
		//页面表头返回值
		List<Course> courseList=new ArrayList<Course>();
		Set<String> courseIds=new HashSet<String>();
		//学生考试成绩
        //根据
		Map<String,ScoreInfo>  stuScoreMap=null;
		if(stuIds.size()>0){
			stuScoreMap=scoreInfoService.findByExamIdAndUnitIdAndStudentIds(examId,unitId,stuIds.toArray(new String[0]),courseIds);
		}else{
			stuScoreMap=new HashMap<String, ScoreInfo>();
		}
		if(courseIds!=null && courseIds.size()>0){
			courseList = SUtils.dt(courseService.findListByIds(courseIds.toArray(new String[]{})), new TR<List<Course>>(){});
		}
		//学生考号
        Map<String, String> examNumMap = examNumService.findByExamId(examId, unitId);
        //班级
        Map<String, String> classMap = toMakeClassMap(classType, new String[]{classIdSearch});
        ScoreInfoDto dto=null;
        
        toMakeGradeSore(stuScoreMap);
        
        //目前不分页 stuList:是当前班级的所有学生(页面显示已有成绩的学生,排除无成绩学生)
        if(CollectionUtils.isNotEmpty(stuList)){
        	for(Student stu:stuList){
        		dto=new ScoreInfoDto();
        		dto.setStuId(stu.getId());
        		dto.setStuName(stu.getStudentName());
        		dto.setStuCode(stu.getStudentCode());
        		dto.setUnitiveCode(stu.getUnitiveCode());
        		dto.setClassId(stu.getClassId());
        		if(examNumMap.containsKey(stu.getId())){
        			dto.setStuExamNum(examNumMap.get(stu.getId()));
        		}
        		dto.setScoreInfoMap(new HashMap<String,ScoreInfo>());
        		for(Course c:courseList){
        			String key=stu.getId()+"_"+c.getId();
        			if(stuScoreMap.containsKey(key)){
        				ScoreInfo score = stuScoreMap.get(key);
        				dto.getScoreInfoMap().put(c.getId(), score);
        			}
        		}
        		//排除无成绩学生
        		if(dto.getScoreInfoMap().size()>0){
        			dto.setClassName(classMap.get(classIdSearch));
            		dtoList.add(dto);
        		}
        		
        	}
        	sortDtoList(dtoList);
		}
        map.put("courseList", courseList);
        map.put("dtoList", dtoList);
	    return "/scoremanage/scoreInfo/scoreInfoAllList.ftl";
	}
	/**
	 * 组装等第分数
	 * @param stuScoreMap
	 */
	private void toMakeGradeSore(Map<String, ScoreInfo> stuScoreMap) {
		 //等第微代码
		Map<String,Map<String,String>> codeDetailMap=new HashMap<String,Map<String,String>>();
		List<McodeDetail> ddList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-DDMC"), new TR<List<McodeDetail>>(){});
		String[] mcodes = new String[ddList.size()];
		for (int i = 0; i < ddList.size(); i++) {
			McodeDetail ddto = ddList.get(i);
			mcodes[i] = ddto.getThisId();
		}
		List<McodeDetail> codeList= SUtils.dt(mcodeRemoteService.findByMcodeIds(mcodes), new TR<List<McodeDetail>>(){});
		for(McodeDetail m:codeList){
			if(!codeDetailMap.containsKey(m.getMcodeId())){
				codeDetailMap.put(m.getMcodeId(), new HashMap<String, String>());
			}
			codeDetailMap.get(m.getMcodeId()).put(m.getThisId(), m.getMcodeContent());
		}
		
		for(String key:stuScoreMap.keySet()){
			ScoreInfo ss = stuScoreMap.get(key);
			if(ScoreDataConstants.ACHI_GRADE.equals(ss.getInputType())){
				String mcoId = ss.getGradeType();
				if(codeDetailMap.containsKey(mcoId)){
					Map<String, String> mcMap = codeDetailMap.get(mcoId);
					String t = mcMap.get(ss.getScore());
					if(StringUtils.isNotBlank(t)){
						ss.setScore(t);
					}
				}
			}
		}
	}
	
	private void sortDtoList(List<ScoreInfoDto> dtoList){
		if(CollectionUtils.isEmpty(dtoList)){
			return;
		}
		Collections.sort(dtoList, new Comparator<ScoreInfoDto>() {
			@Override
			public int compare(ScoreInfoDto o1, ScoreInfoDto o2) {
				if(!StringUtils.equals(o1.getClassName(), o2.getClassName())) {
					String s1 = o1.getClassName().replaceAll("[0-9]", "");
					String s2 = o1.getClassName().replaceAll("[0-9]", "");
					if(!StringUtils.equals(s1, s2)) {
						return o1.getClassName().compareTo(o2.getClassName());
					}
					int c1 = NumberUtils.toInt(o1.getClassName().replaceAll("[^0-9]", ""));
					int c2 = NumberUtils.toInt(o2.getClassName().replaceAll("[^0-9]", ""));
					return c1-c2;
				}
				if(!StringUtils.equals(o1.getClassId(), o2.getClassId())) {
					return o1.getClassId().compareTo(o2.getClassId());
				}
				if(StringUtils.isNotBlank(o1.getClsInnerCode()) && StringUtils.isNotBlank(o2.getClsInnerCode())){
					return NumberUtils.toInt(o1.getClsInnerCode()) - NumberUtils.toInt(o2.getClsInnerCode());
				} else if(StringUtils.isNotBlank(o1.getClsInnerCode())) {
					return 1;
				} else if(StringUtils.isNotBlank(o2.getClsInnerCode())) {
					return 0;
				}
				
				if(StringUtils.isNotBlank(o1.getStuCode()) && StringUtils.isNotBlank(o2.getStuCode())) {
					return o1.getStuCode().compareTo(o2.getStuCode());
				}
				
				return 0;
			}
		});
	}
	
	@RequestMapping("/scoreInfo/onelist/page")
    @ControllerInfo("编辑列表")
    public String scoreList( ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		LoginInfo loginInfo = getLoginInfo(httpSession);
        String examId= request.getParameter("examId");
        String classType= request.getParameter("classType");
        String classIdSearch= request.getParameter("classIdSearch");
        String subjectInfoId= request.getParameter("subjectInfoId"); 
        String courseId= request.getParameter("courseId");
        String noLimit= request.getParameter("noLimit");
        String unitId=loginInfo.getUnitId();
        //考试科目信息
        SubjectInfo subjectInfo = subjectInfoService.findOne(subjectInfoId);
        map.put("subjectInfo", subjectInfo);
        map.put("unitId",unitId);
	    map.put("examId",examId);
		map.put("classType",classType);
		map.put("classIdSearch",classIdSearch);
		map.put("subjectId",courseId);
		map.put("subjectInfoId",subjectInfoId);
		//wyy.录分权限
		ExamInfo examInfo = examInfoService.findOne(examId);
		int isCanScoreInfo = scoreInfoService.getEditRole(examId, classIdSearch, courseId,
				unitId,examInfo.getAcadyear(),examInfo.getSemester(),getLoginInfo().getOwnerId());
		if(isCanScoreInfo == 2){
			if("1".equals(noLimit)){
				//无限制 为录分管理员
				isCanScoreInfo = 0;
			}
			if("0".equals(noLimit)){
				//有限制 为普通录分人员
				isCanScoreInfo = 1;
			}
		}
		//老师是录分管理员，也有普通录分人员的权限，但是对这一门课没有录分权限，当老师选择普通录分角色进入时，页面不可编辑
		if(isCanScoreInfo == 0 && "0".equals(noLimit)){
			isCanScoreInfo = -1;
		}
		map.put("isCanScoreInfo",isCanScoreInfo);
		//班级下学生
		List<Student> stuList=new ArrayList<Student>();
        //不排考学生
        Map<String, String> filterMap = filtrationService.findByExamIdAndSchoolIdAndType(examId,unitId,ScoreDataConstants.FILTER_TYPE1);     
        if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
        	 stuList = SUtils.dt(studentService.findByClassIds(new String[]{classIdSearch}), new TR<List<Student>>(){});
        }else{
        	stuList = SUtils.dt(studentService.findByTeachClassIds(new String[]{classIdSearch}), new TR<List<Student>>(){});
        }
        //学生考号
        Map<String, String> examNumMap = examNumService.findByExamId(examId, unitId);
        //班级
        Map<String, String> classMap = toMakeClassMap(classType, new String[]{classIdSearch});
        //数据库已有数据
        Set<String> stuIds=new HashSet<String>();//学生id
        Set<String> courseIds=new HashSet<String>();
        courseIds.add(courseId);
        Map<String, ScoreInfo> scoremap=scoreInfoService.findByExamIdAndUnitIdAndClassId(examId, unitId, classIdSearch, classType, stuIds, courseIds);
        
        ScoreInfoDto dto=null;
        List<ScoreInfoDto> dtoList=new ArrayList<ScoreInfoDto>();
        if(CollectionUtils.isNotEmpty(stuList)){
        	for(Student stu:stuList){
        		if(filterMap.containsKey(stu.getId())){
        			continue;
        		}
        		dto=new ScoreInfoDto();
        		dto.setStuId(stu.getId());
        		dto.setStuName(stu.getStudentName());
        		dto.setStuCode(stu.getStudentCode());
        		dto.setClassId(stu.getClassId());
        		dto.setUnitiveCode(stu.getUnitiveCode());
        		dto.setClsInnerCode(stu.getClassInnerCode());
        		//同一科目考试 理论上教学班id应该只能一个 默认第一个
        		/*if(stuTeachClId.containsKey(stu.getId()) && CollectionUtils.isNotEmpty(stuTeachClId.get(stu.getId()))){
        			dto.setTeachClassId(stuTeachClId.get(stu.getId()).get(0));
        		}*/
        		if(ScoreDataConstants.CLASS_TYPE2.equals(classType)){
        			dto.setTeachClassId(classIdSearch);
        		}
        		if(examNumMap.containsKey(stu.getId())){
        			dto.setStuExamNum(examNumMap.get(stu.getId()));
        		}
        		String key=stu.getId()+"_"+courseId;
        		if(scoremap.containsKey(key)){
        			ScoreInfo score = scoremap.get(key);
        			dto.setScore(score.getScore());
        			dto.setScoreStatus(score.getScoreStatus());
        			dto.setScoreId(score.getId());
        			//wyy.设置总评成绩
        			dto.setToScore(score.getToScore());
        		}
        		dto.setClassName(classMap.get(classIdSearch));
        		dtoList.add(dto);
        	}
        	sortDtoList(dtoList);
        }
		map.put("dtoList", dtoList);
		
		//传递考试班级信息
		ClassInfo classInfo = classInfoService.findByAll(unitId, classType, classIdSearch, subjectInfoId);
		if(classInfo == null){
			classInfo = new ClassInfo();
			classInfo.setClassId(classIdSearch);
			classInfo.setClassType(classType);
			classInfo.setSchoolId(unitId);
			classInfo.setSubjectInfoId(subjectInfoId);
			classInfo.setIsLock(Constant.IS_FALSE_Str);
			classInfo.setId(UuidUtils.generateUuid());
			
			classInfoService.save(classInfo);
		}
		map.put("classInfo", classInfo);
		
		//是否需要总评成绩
		Course course = SUtils.dc(courseService.findOneById(courseId),Course.class);
		String subjectCode = null;
		if(course == null){
			logger.error("根据课程id："+ courseId +",找不到对应课程");
		}else{
			subjectCode = course.getSubjectCode();
			if(ScoreDataConstants.EXAM_TYPE_FINAL.equals(examInfo.getExamType()) 
					&& !BaseConstants.HW_CODE_KS.equals(subjectCode) && !BaseConstants.HW_CODE_XZ.equals(subjectCode)){
				map.put("needGeneral", true);
			}
		}
		//如果成绩已经锁定，需要打印成绩表 信息
		if(Constant.IS_TRUE_Str.equals(classInfo.getIsLock())){
			int rowCount = (dtoList.size()+1)/2;
			map.put("rowCount", rowCount);
			map.put("stuCount", dtoList.size());
			List<String> bindingNameList = new ArrayList<>();
			map.put("bindingNameList", bindingNameList);
			//语文需要绑定 写作成绩
			doBinding(map, examId, classType, classIdSearch, courseId, unitId, stuIds, dtoList,BaseConstants.HW_CODES_YU,BaseConstants.HW_CODE_XZ,bindingNameList);
			//外文需要绑定 口试成绩
			doBinding(map, examId, classType, classIdSearch, courseId, unitId, stuIds, dtoList,BaseConstants.HW_CODES_WW,BaseConstants.HW_CODE_KS,bindingNameList);
			
			//获取学校名
			Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
			String acadyear = examInfo.getAcadyear();
			String semester = examInfo.getSemester();
			String semesterName = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-XQ", semester),McodeDetail.class).getMcodeContent();
			String examName = examInfo.getExamName();
			
			String className = null;
			if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
				 Clazz clazz = SUtils.dc(classRemoteService.findOneById(classIdSearch),Clazz.class);
				 String gradeName = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()),Grade.class).getGradeName();
//				 gradeName = "";
				 className = gradeName+ clazz.getClassName();
	        }else{
	        	 TeachClass teachClass = SUtils.dc(teachClassService.findOneById(classIdSearch),TeachClass.class);
	        	 String gradeName = SUtils.dc(gradeRemoteService.findOneById(teachClass.getGradeId()),Grade.class).getGradeName();
	        	 //行政班暂时不需要加上年级名称
	        	 gradeName = "";
	        	 className = gradeName + teachClass.getName();
	        }
			
			map.put("unitName", unit.getUnitName());
			map.put("acadyear", acadyear);
			map.put("semesterName", semesterName);
			map.put("examName", examName);
			map.put("className", className);
			map.put("subjectName", course.getSubjectName());
		}
//		map.put("classInfo", classInfo);
		
		//scoreInfoService.findJsonScoreInfo("2ED9CFC8AA38432D81C6370A16731CC9", "24181660519546692508842063636925", "31");
		return "/scoremanage/scoreInfo/scoreInfoOneList.ftl";
	}

	private void doBinding(ModelMap map, String examId, String classType, String classIdSearch, String courseId,
			String unitId, Set<String> stuIds, List<ScoreInfoDto> dtoList, final List<String> mainCodeList, final String bingingCode, List<String> bindingNames) {
		
		if(mainCodeList.contains(SUtils.dc(courseService.findOneById(courseId),Course.class).getSubjectCode())){
			List<Course> xzCourses = SUtils.dt(courseService.findByUnitCourseCodes(unitId, bingingCode),Course.class);
			if(CollectionUtils.isNotEmpty(xzCourses)){
				// 先判断本次 考试 是否存在 写作课程
				Course xzCourse = xzCourses.get(0);
				String xzCourseId = xzCourse.getId();
				
				List<SubjectInfo> siList = subjectInfoService.findByExamIdAndCourseIdAndUnitId(unitId, examId, xzCourseId);
				
				// 如果本次考试有 写作 科目 
				if(CollectionUtils.isNotEmpty(siList)) {
					Set<String> infoIds = EntityUtils.getSet(siList, SubjectInfo::getId);
					ClassInfo xzClassInfo = null; 
					if(ScoreDataConstants.CLASS_TYPE2.equals(classType)) {
						classIdSearch = null;
					}
					List<ClassInfo> cis = classInfoService.findList(unitId, classType, classIdSearch, infoIds.toArray(new String[0]));
					if(CollectionUtils.isNotEmpty(cis)) {
						Optional<ClassInfo> first = cis.stream().filter(e->classType.equals(e.getClassType())).findFirst();
						if(first.isPresent())
							xzClassInfo = first.get();
					}
					if(ScoreDataConstants.CLASS_TYPE2.equals(classType) || xzClassInfo != null && Constant.IS_TRUE_Str.equals(xzClassInfo.getIsLock())) {
						HashSet<String> xzIdSet = new HashSet<String>();
						xzIdSet.add(xzCourseId);
						Map<String, ScoreInfo> xzScoreMap = scoreInfoService.findByExamIdAndUnitIdAndClassId(examId, unitId, 
								classIdSearch, classType, stuIds, xzIdSet);
						//判断写作成绩是否已经录入
						for (ScoreInfoDto dto2:dtoList) {
							ScoreInfo scoreInfo = xzScoreMap.get(dto2.getStuId()+"_"+xzCourseId);
							if(scoreInfo == null || scoreInfo.getScore() == null) {
								dto2.getBindingScores().add("");
							}else {
								dto2.getBindingScores().add(scoreInfo.getScore());
							}
						}
						bindingNames.add(xzCourse.getSubjectName());
					}else {
						// 写作没有录入完成
						map.put("bindMsg", "请先提交  "+xzCourse.getSubjectName()+" 成绩后再打印成绩单");
					}
				}
			}else{
				logger.error("根据课程码："+bingingCode+" 找不到课程");
			}
		}
	}
	@ResponseBody
	@RequestMapping("/common/queryScoreInfo")
	public String queryScoreInfo(HttpServletRequest request) {
		String examId= request.getParameter("examId");
		String unitId= request.getParameter("unitId");
		String gradeCode= request.getParameter("gradeCode");
		String json =scoreInfoService.findJsonScoreInfo(unitId, examId, gradeCode, null);
		return json;
	}
	@ResponseBody
	@RequestMapping("/common/queryExamList")
	public String queryExamList(HttpServletRequest request) {
		String unitId= request.getParameter("unitId");
		String json =examInfoService.findJsonExamList(unitId);
		return json;
	}
	@ResponseBody
	@RequestMapping("/common/queryGradeList")
	public String queryGradeList(HttpServletRequest request) {
		String unitId= request.getParameter("unitId");
		String examId= request.getParameter("examId");
		String json =examInfoService.findJsonGradeList(unitId,examId);
		return json;
	}
	@ResponseBody
	@RequestMapping("/scoreInfo/lock")
	@ControllerInfo("成绩锁定")
    public String lockTheExamScore(String examClassId,String isLock){
		ClassInfo classInfo = new ClassInfo();
		if(Constant.IS_FALSE_Str.equals(isLock)){
			classInfo.setIsLock(Constant.IS_FALSE_Str);
		}else{
			classInfo.setIsLock(Constant.IS_TRUE_Str);
		}
		try {
			classInfoService.update(classInfo, examClassId, new String[]{"isLock"});
			log.info(this.getLoginInfo().getUserName()+"操作了examClassId:"+examClassId+",做isLock状态："+isLock);
		} catch (Exception e) {
			e.printStackTrace();
			return error("提交失败");
		}
    	return success("提交成功");
    }
    
	@ResponseBody
	@RequestMapping(value="/scoreInfo/checkInputPower")
	public String checkPower(ScoreInfoDto scoreInfoDto, String noLimit){
		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();
		String classType = scoreInfoDto.getClassType();
		String classIdSearch = scoreInfoDto.getClassIdSearch();
		String subjectInfoId = scoreInfoDto.getSubjectInfoId();
		String examId = scoreInfoDto.getExamId();
		String courseId = scoreInfoDto.getSubjectId();
		
		ClassInfo classInfo = classInfoService.findByAll(unitId, classType,
				classIdSearch, subjectInfoId);
		if(classInfo == null){
			classInfo = new ClassInfo();
			classInfo.setClassId(classIdSearch);
			classInfo.setClassType(classType);
			classInfo.setSchoolId(unitId);
			classInfo.setSubjectInfoId(subjectInfoId);
			classInfo.setIsLock(Constant.IS_FALSE_Str);
			classInfo.setId(UuidUtils.generateUuid());
			
			classInfoService.save(classInfo);
		}
		
		if(Constant.IS_FALSE_Str.equals(classInfo.getIsLock())){
			//如果成绩未提交，判断是否有录入权限
			ExamInfo examInfo = examInfoService.findOne(examId);
			int editCode = scoreInfoService.getEditRole(examId, classIdSearch, courseId,
					unitId,examInfo.getAcadyear(),examInfo.getSemester(),loginInfo.getOwnerId());
			if(editCode == 2){
				if("1".equals(noLimit)){
					//无限制 为录分管理员
					editCode = 0;
				}
				if("0".equals(noLimit)){
					//有限制 为普通录分人员
					editCode = 1;
				}
			}
			//老师是录分管理员，也有普通录分人员的权限，但是对这一门课没有录分权限，当老师选择普通录分角色进入时，页面不可编辑
			if(editCode == 0 && "0".equals(noLimit)){
				editCode = -1;
			}
			
			//返回结果
			if(editCode == -1){
				return error("您没有导入权限！");
			}else{
				return returnSuccess();
			}
		}else{
			return error("成绩已提交，不可导入！");
		}
	}
	
    @ResponseBody
	@RequestMapping(value="/scoreInfo/saveAll")
	public String saveAll(ScoreInfoDto dto,HttpServletResponse response,HttpSession httpSession) {
    	//先检查指定班级是否已经被锁定
    	ClassInfo classInfo = classInfoService.findByAll(dto.getUnitId(), dto.getClassType(), dto.getClassIdSearch(), dto.getSubjectInfoId());
    	if(Constant.IS_TRUE_Str.equals(classInfo.getIsLock())){
    		return error("此班级数据已经锁定，无法再修改");
    	}
    	
    	if(dto==null || CollectionUtils.isEmpty(dto.getDtoList())){
    		return error("没有数据要保存");
    	}
    	List<ScoreInfo> dtoList = dto.getDtoList();
    	try{
    		ExamInfo exam = examInfoService.findOne(dto.getExamId());
    		if(exam==null || exam.getIsDeleted()==Constant.IS_DELETED_TRUE){
				return error("该考试已删除");
			}
    		SubjectInfo subjectInfo = subjectInfoService.findOne(dto.getSubjectInfoId());
			if(subjectInfo==null){
				return error("该考试科目已删除");
			}
			if(!subjectInfo.getInputType().equals(dto.getInputType())){
				return error("该考试科目信息已经改变");
			}
			
			Set<String> stuId=new HashSet<String>();
			Set<String> subjectIds=new HashSet<String>();
			subjectIds.add(subjectInfo.getSubjectId());
			//查询当前考试科目下 该班级已有学生信息
			Map<String, ScoreInfo> scoremap=scoreInfoService.findByExamIdAndUnitIdAndClassId(dto.getExamId(), dto.getUnitId(), dto.getClassIdSearch(), null,stuId,subjectIds );
	        if(scoremap==null){
	        	scoremap=new HashMap<String, ScoreInfo>();
	        }
			List<ScoreInfo> insertOrupdateList=new ArrayList<ScoreInfo>();
			//不统分学生
			//Map<String, String> filterMap = filtrationService.findByExamIdAndSchoolIdAndType(dto.getExamId(),dto.getUnitId(),ScoreDataConstants.FILTER_TYPE2);     
	        //List<ScoreInfo> infolist=scoreInfoService.findByExamIdCourseId(dto.getUnitId(),dto.getExamId(),dto.getSubjectId(),dto.getClassIdSearch(),dto.getClassType());
	        //Map<String,ScoreInfo> scoreInfoMap=new HashMap<String, ScoreInfo>();
	        // if(CollectionUtils.isNotEmpty(infolist)){
	        //	scoreInfoMap = EntityUtils.getMap(infolist, "id", StringUtils.EMPTY);
	        //}
	        ScoreInfo newinfo=null;
	        
	        String opoUserId = getLoginInfo().getUserId();
	        String unitId=dto.getUnitId()==null?getLoginInfo().getUnitId():dto.getUnitId();
	        String subjectId=dto.getSubjectId();
			if(ScoreDataConstants.ACHI_GRADE.equals(subjectInfo.getInputType())){
				//等第
				if(!subjectInfo.getGradeType().equals(dto.getGradeType())){
					return error("该考试科目信息已经改变");
				}
				for(ScoreInfo info:dtoList){
					if(scoremap.containsKey(info.getStudentId()+"_"+subjectId)){
						//修改
						newinfo=scoremap.get(info.getStudentId()+"_"+subjectId);
						newinfo.setOperatorId(opoUserId);
						newinfo.setModifyTime(new Date());
					}else{
						newinfo=makeScoreInfo(exam,subjectInfo,subjectId,opoUserId,unitId);
					}
					newinfo.setStudentId(info.getStudentId());
					newinfo.setClassId(info.getClassId());
					newinfo.setTeachClassId(info.getTeachClassId());
					newinfo.setScore(info.getScore());
					newinfo.setScoreStatus(info.getScoreStatus());
					newinfo.setGradeType(dto.getGradeType());
					//wyy.设置总评成绩
					newinfo.setToScore(info.getToScore());
//					if(filterMap.containsKey(newinfo.getStudentId())){
//						//不统分
//						newinfo.setIsInStat(Constant.IS_FALSE+"");
//					}else if(Constant.IS_TRUE_Str.equals(exam.getIsTotalScore()) && "1".equals(info.getScoreStatus())){
//						//考试设置缺考不记录总分  缺考：1
//						newinfo.setIsInStat(Constant.IS_FALSE+"");
//					}else{
//						newinfo.setIsInStat(Constant.IS_TRUE+"");
//					}
					insertOrupdateList.add(newinfo);
				}
				
			}else{
				//分数
				for(ScoreInfo info:dtoList){
					if(scoremap.containsKey(info.getStudentId()+"_"+subjectId)){
						//修改
						newinfo=scoremap.get(info.getStudentId()+"_"+subjectId);
						newinfo.setOperatorId(opoUserId);
						newinfo.setModifyTime(new Date());
						//分数--防止之前的数据是等第
						newinfo.setInputType(subjectInfo.getInputType());
						newinfo.setGradeType(subjectInfo.getGradeType());
					}else{
						newinfo=makeScoreInfo(exam,subjectInfo,subjectId,opoUserId,unitId);
					}
					newinfo.setStudentId(info.getStudentId());
					newinfo.setClassId(info.getClassId());
					newinfo.setTeachClassId(info.getTeachClassId());
					newinfo.setScore(info.getScore());
					newinfo.setScoreStatus(info.getScoreStatus());
					//wyy.设置总评成绩
					newinfo.setToScore(info.getToScore());
					
					/*if(filterMap.containsKey(newinfo.getStudentId())){
						//不统分
						newinfo.setIsInStat(Constant.IS_FALSE+"");
					}else if(Constant.IS_TRUE_Str.equals(exam.getIsTotalScore()) && "1".equals(info.getScoreStatus())){
						//考试设置缺考不记录总分  缺考：1
						newinfo.setIsInStat(Constant.IS_FALSE+"");
					}else{
						newinfo.setIsInStat(Constant.IS_TRUE+"");
					}*/
					insertOrupdateList.add(newinfo);
				}
			}
			if(CollectionUtils.isNotEmpty(insertOrupdateList)){
				scoreInfoService.saveAllEntitys(insertOrupdateList.toArray(new ScoreInfo[]{}));
			}
			
    	}catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}
		return success("保存成功");
	}
	
	private ScoreInfo makeScoreInfo(ExamInfo exam,SubjectInfo subjectInfo,
			String subjectId,String userId,String unitId){
		ScoreInfo info=new ScoreInfo();
		info.setAcadyear(exam.getAcadyear());
		info.setSemester(exam.getSemester());
		info.setSubjectId(subjectId);
		info.setCreationTime(new Date());
		info.setExamId(exam.getId());
		info.setGradeType(subjectInfo.getGradeType());
		info.setId(UuidUtils.generateUuid());
		info.setModifyTime(new Date());
		info.setOperatorId(userId);
		info.setUnitId(unitId);
		info.setInputType(subjectInfo.getInputType());
		return info;
	}
}
