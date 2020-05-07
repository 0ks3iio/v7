package net.zdsoft.scoremanage.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseTypeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.OptionalScoreStatisticDto;
import net.zdsoft.scoremanage.data.dto.ScoreInfoDto;
import net.zdsoft.scoremanage.data.dto.ScoreLimitSearchDto;
import net.zdsoft.scoremanage.data.entity.ClassInfo;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.entity.ScoreLimit;
import net.zdsoft.scoremanage.data.service.ClassInfoService;
import net.zdsoft.scoremanage.data.service.NotLimitService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.scoremanage.data.service.ScoreLimitService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/scoremanage/optionalScore/")
public class OptionalScoreInfoAction  extends BaseAction{
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private ClassInfoService classInfoService;
	@Autowired
	private ClassRemoteService classService;
	@Autowired
	private TeachClassRemoteService teachClassService;
	@Autowired
	private ScoreInfoService scoreInfoService;
	@Autowired
	private StudentRemoteService studentService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuService;
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private ScoreLimitService scoreLimitService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private NotLimitService notLimitService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private CourseTypeRemoteService courseTypeRemoteService;
	@RequestMapping("/index/page")
	public String showIndex(ModelMap map, String noLimit,String acadyearSearch,String semesterSearch){
		List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){}); 
	    if(CollectionUtils.isEmpty(acadyearList)){
	        return errorFtl(map, "学年学期不存在");
	    }
        LoginInfo loginInfo = getLoginInfo();
		String unitId=loginInfo.getUnitId();
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(1, unitId), Semester.class);
		if(semester==null){
			return errorFtl(map, "学年学期不存在");
		}
		//判断录分角色 无 普通 管理员 2者都有
		if(noLimit == null){
			
			List<String> teacherIds = scoreLimitService.findTeacherIdByUnitId(unitId, BaseConstants.SUBJECT_TYPE_XX);
			List<String> noLimitTeacherIds = notLimitService.findTeacherIdByUnitId(unitId);
			
			if(teacherIds.contains(loginInfo.getOwnerId()) && noLimitTeacherIds.contains(loginInfo.getOwnerId())){
				//有两个录分角色，需要选择一个
				return "/scoremanage/scoreInfo/selectRecordingRole.ftl";
			}
			if(StringUtils.isBlank(noLimit) && noLimitTeacherIds.contains(getLoginInfo().getOwnerId())){
				noLimit = "1";
			}
		}
		//页面参数(初始值)
		if(StringUtils.isBlank(acadyearSearch) || StringUtils.isBlank(semesterSearch)){
			acadyearSearch = semester.getAcadyear();
			semesterSearch=semester.getSemester()+"";
			
		}
		
		//获取所有选修课程
//		String courseJsons = courseRemoteService.findByUnitIdIn(new String[]{unitId}, BaseConstants.TYPE_COURSE_OPTIONAL, new String[]{});
//		List<Course> courseList = SUtils.dt(courseJsons, Course.class);
		//获得该学期下已开设的课程
		List<Course> courseList = null;
		Set<String> courseIdSet = null;
		if("1".equals(noLimit)){
			List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findByUnitIdAndAcadyearAndSemesterAndSubjectType(unitId, acadyearSearch, semesterSearch, Integer.parseInt(BaseConstants.SUBJECT_TYPE_XX)),ClassTeaching.class);
			courseIdSet = EntityUtils.getSet(classTeachingList, "subjectId");
		}else{
			List<TeachClass> classList = SUtils.dt(teachClassService.findBySearch(unitId, acadyearSearch, semesterSearch, null, null, null),TeachClass.class);
			List<String> classIdList = new ArrayList<String>();
			for (TeachClass teachClass : classList) {
				if(Constant.IS_TRUE_Str.equals(teachClass.getIsUsing()) && !Constant.IS_TRUE_Str.equals(teachClass.getIsUsingMerge())){
					classIdList.add(teachClass.getId());
				}
			}
			ScoreLimitSearchDto searchDto = new ScoreLimitSearchDto();
			searchDto.setExamId(Constant.GUID_ZERO);
			searchDto.setAcadyear(acadyearSearch);
			searchDto.setSemester(semesterSearch);
			searchDto.setUnitId(unitId);
			searchDto.setTeacherId(getLoginInfo().getOwnerId());
			searchDto.setClassIds(classIdList.toArray(new String[0]));
			List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(searchDto);
			courseIdSet = EntityUtils.getSet(limitList, "subjectId");
		}
		if(CollectionUtils.isNotEmpty(courseIdSet)){
			courseList = SUtils.dt(courseRemoteService.findListByIds(courseIdSet.toArray(new String[0])),Course.class);
		}
		
		map.put("acadyearList", acadyearList);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);

		map.put("unitId",unitId);
//		map.put("type", 1);
		map.put("courseList", courseList);
		map.put("noLimit", noLimit);
		return "/scoremanage/optionalScoreInfo/optionScoreIndex.ftl";
	}
	
	@RequestMapping("/updateClass")
	@ResponseBody
	public String updateClass(TeachClass teachClass,String noLimit){
		String teachClassJsons = teachClassService.findTeachClassList(teachClass.getUnitId(), teachClass.getAcadyear(),
				teachClass.getSemester(), teachClass.getCourseId());
		List<TeachClass> teachClassList = SUtils.dt(teachClassJsons, TeachClass.class);

		Iterator<TeachClass> iterator = teachClassList.iterator();
		while(iterator.hasNext()){
			if(Constant.IS_TRUE_Str.equals(iterator.next().getIsUsingMerge())){
				iterator.remove();
			}
		}
		List<String> noLimitTeacherIds = notLimitService.findTeacherIdByUnitId(teachClass.getUnitId());
		if(StringUtils.isBlank(noLimit) && noLimitTeacherIds.contains(getLoginInfo().getOwnerId())){
			noLimit = "1";
		}
		if(CollectionUtils.isNotEmpty(teachClassList) && !"1".equals(noLimit)){
			String[] teachClassIds = EntityUtils.getSet(teachClassList, "id").toArray(new String[0]);
			ScoreLimitSearchDto searchDto = new ScoreLimitSearchDto();
			searchDto.setExamId(Constant.GUID_ZERO);
			searchDto.setAcadyear(teachClass.getAcadyear());
			searchDto.setSemester(teachClass.getSemester());
			searchDto.setUnitId(teachClass.getUnitId());
			searchDto.setSubjectId(teachClass.getCourseId());
			searchDto.setClassIds(teachClassIds);
			searchDto.setTeacherId(getLoginInfo().getOwnerId());
			
			List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(searchDto);
			if(CollectionUtils.isNotEmpty(limitList)){
				String[] limitClassIds = EntityUtils.getSet(limitList, "classId").toArray(new String[0]);
				teachClassList = SUtils.dt(teachClassService.findListByIds(limitClassIds),TeachClass.class);
			}else{
				teachClassList = new ArrayList<TeachClass>();
			}
		}
		
		boolean success =false;
		String mess = "";
		if(CollectionUtils.isNotEmpty(teachClassList)){
			success = true;
			mess = "成功";
		}else{
			mess = "暂无班级";
		}
		
		JSONObject jo = new JSONObject();
		jo.put("success", success);
		jo.put("message", mess);
		jo.put("businessValue", teachClassList);
		
		return jo.toJSONString();
	}
	
	@RequestMapping("/tablist")
	public String showList(ScoreInfo scoreInfo,String noLimit, ModelMap map){
		TeachClass parentClass = SUtils.dc(teachClassService.findOneById(scoreInfo.getTeachClassId()),TeachClass.class);
		String[] classIds = null;
		List<ScoreInfo> scoreInfList = null;
		Map<String,TeachClass> teachClassMap = new HashMap<String, TeachClass>();
		if(Constant.IS_TRUE_Str.equals(parentClass.getIsMerge())){
			List<TeachClass> childTeachClassList = SUtils.dt(teachClassService.findByParentIds(new String[]{scoreInfo.getTeachClassId()}),TeachClass.class);
			teachClassMap = EntityUtils.getMap(childTeachClassList, TeachClass::getId);
			Set<Object> classIdSet = EntityUtils.getSet(childTeachClassList, TeachClass::getId);
			classIds = classIdSet.toArray(new String[0]);
			scoreInfList = scoreInfoService.findOptionalCourseScore(scoreInfo.getUnitId(),classIds);
		}else{
			teachClassMap.put(parentClass.getId(), parentClass);
			scoreInfList =  scoreInfoService.findOptionalCourseScore(scoreInfo.getUnitId(),scoreInfo.getSubjectId(),scoreInfo.getTeachClassId());
			classIds = new String[]{scoreInfo.getTeachClassId()};
		}
		
		Map<String, ScoreInfo> stuIdScoreMap = EntityUtils.getMap(scoreInfList, ScoreInfo::getStudentId);
		
		LoginInfo loginInfo = getLoginInfo();
		List<ScoreInfoDto> dtoList=new ArrayList<ScoreInfoDto>();
		String viewName = "/scoremanage/optionalScoreInfo/optionalScoreInfoList.ftl";
		
		//此班级成绩是否已经被提交 锁定
		ClassInfo classInfo = classInfoService.findByAll(scoreInfo.getUnitId(), ScoreDataConstants.CLASS_TYPE2, 
				scoreInfo.getTeachClassId(), scoreInfo.getSubjectId());
		if(classInfo == null){
			classInfo = new ClassInfo();
			classInfo.setClassId(scoreInfo.getTeachClassId());
			classInfo.setClassType(ScoreDataConstants.CLASS_TYPE2);
			classInfo.setSchoolId(scoreInfo.getUnitId());
			classInfo.setSubjectInfoId(scoreInfo.getSubjectId());
			classInfo.setIsLock(Constant.IS_FALSE_Str);
			classInfo.setId(UuidUtils.generateUuid());
			
			classInfoService.save(classInfo);
		}
		
		List<Student> realStudentList = null;
		//已经提交
		if(Constant.IS_TRUE_Str.equals(classInfo.getIsLock())){
			String[] stuIds = EntityUtils.getSet(scoreInfList, ScoreInfo::getStudentId).toArray(new String[0]);
			realStudentList = SUtils.dt(studentService.findListByIds(stuIds), Student.class);
			
		}else{
			//教学班的真实学生
			List<TeachClassStu> teachStuList = SUtils.dt(teachClassStuService.findByClassIds(classIds), TeachClassStu.class);
			String[] realStuIds = EntityUtils.getSet(teachStuList, TeachClassStu::getStudentId).toArray(new String[0]);
			realStudentList = SUtils.dt(studentService.findListByIds(realStuIds), Student.class);
			Map<String, Student> realStuMap = EntityUtils.getMap(realStudentList, Student::getId);
			//未提交
			List<ScoreInfo> toSaveList = new ArrayList<>();
			for (TeachClassStu teachStu : teachStuList) {
				if(stuIdScoreMap.get(teachStu.getStudentId())==null){
					//数据库中无数据
					ScoreInfo scoreInfo2 = new ScoreInfo();
					scoreInfo2.setId(UuidUtils.generateUuid());
					scoreInfo2.setAcadyear(scoreInfo.getAcadyear());
					scoreInfo2.setSemester(scoreInfo.getSemester());
					scoreInfo2.setUnitId(scoreInfo.getUnitId());
					scoreInfo2.setExamId(BaseConstants.ZERO_GUID);
					if(Constant.IS_TRUE_Str.equals(parentClass.getIsMerge())){
						scoreInfo2.setTeachClassId(teachStu.getClassId());
						scoreInfo2.setSubjectId(teachClassMap.get(teachStu.getClassId())==null?"":teachClassMap.get(teachStu.getClassId()).getCourseId());
					}else{
						scoreInfo2.setTeachClassId(scoreInfo.getTeachClassId());
						scoreInfo2.setSubjectId(scoreInfo.getSubjectId());
					}
					scoreInfo2.setStudentId(teachStu.getStudentId());
					scoreInfo2.setOperatorId(loginInfo.getUserId());
					scoreInfo2.setInputType(ScoreDataConstants.ACHI_SCORE);
					scoreInfo2.setClassId(realStuMap.get(teachStu.getStudentId()).getClassId());
					scoreInfo2.setScoreStatus("0");
					//scoreInfo2.setToScore("0");
					
					toSaveList.add(scoreInfo2);
				}
			}
			
			boolean doubleRequest = false;  //判断是否需要二次查询成绩信息
			//保存新增的学生成绩
			if(CollectionUtils.isNotEmpty(toSaveList)){
				doubleRequest = true;
				scoreInfoService.saveAllEntitys(toSaveList.toArray(new ScoreInfo[0]));
			}
			//删除已经不在的学生成绩
			List<ScoreInfo> toDeleteList = new ArrayList<>();
			for (ScoreInfo scoreInfo2 : scoreInfList) {
				if(realStuMap.get(scoreInfo2.getStudentId()) == null){
					//已经被教学班级删除
					toDeleteList.add(scoreInfo2);
				}
			}
			if(CollectionUtils.isNotEmpty(toDeleteList)){
				doubleRequest = true;
				scoreInfoService.deleteAll(toDeleteList.toArray(new ScoreInfo[0]));
			}
			
			//如果此班级的学生有变动，再查询一次
			if(doubleRequest){
				if(Constant.IS_TRUE_Str.equals(parentClass.getIsMerge())){
					scoreInfList = scoreInfoService.findOptionalCourseScore(scoreInfo.getUnitId(),classIds);
				}else{
					scoreInfList =  scoreInfoService.findOptionalCourseScore(scoreInfo.getUnitId(),
							scoreInfo.getSubjectId(),scoreInfo.getTeachClassId());
				}
					
			}
		}
		//若还是没有学生，则直接返回
		if(CollectionUtils.isEmpty(scoreInfList)){
			return viewName;
		}
		
		//已经存在成绩表里的学生数据
		Map<String, Student> stuMap = EntityUtils.getMap(realStudentList, "id");
		//从成绩表里获取班级数据 --> 修改为根据学生来获取班级信息，因为行政班学生会调整，成绩表里的信息会失效
		List<Clazz> clazzList = SUtils.dt(classService.findListByIds(EntityUtils.getSet(realStudentList, "classId").toArray(new String[]{})),Clazz.class);
		Map<String, Clazz> clazMap = EntityUtils.getMap(clazzList, "id");
		List<ScoreInfo> toUpdateScoreList = new ArrayList<ScoreInfo>();
		for (ScoreInfo scoreinfo : scoreInfList) {
			ScoreInfoDto dto = new ScoreInfoDto();
			if(teachClassMap.containsKey(scoreinfo.getTeachClassId())&&teachClassMap.get(scoreinfo.getTeachClassId()).getFullMark()!=null){
				dto.setFullMark(teachClassMap.get(scoreinfo.getTeachClassId()).getFullMark());
			}else
				dto.setFullMark(100);
			dto.setScoreId(scoreinfo.getId());
			dto.setStuId(scoreinfo.getStudentId());
			dto.setScoreStatus(scoreinfo.getScoreStatus());
			dto.setScore(scoreinfo.getScore());
			//显示学分 打印成绩表时使用
			dto.setToScore(scoreinfo.getToScore());
			//页面显示信息
			Student stu = stuMap.get(scoreinfo.getStudentId());
			dto.setStuCode(stu.getStudentCode());
			dto.setClsInnerCode(stu.getClassInnerCode());
			//获取 年级信息 和 行政班 的信息
			Clazz clazz = clazMap.get(stu.getClassId());
			if(clazz!=null){
				dto.setClassName(clazz.getClassNameDynamic());
			}else{
				dto.setClassName("");
			}
			dto.setStuName(stu.getStudentName());
			dto.setUnitiveCode(stu.getUnitiveCode());
			dtoList.add(dto);
			
			if(!stu.getClassId().equals(scoreinfo.getClassId())){
				//学生所在行政班发生了变化（比如发生行政班重组）
				scoreinfo.setClassId(stu.getClassId());
				toUpdateScoreList.add(scoreinfo);
			}
		}
		sortDtoList(dtoList);
		if(CollectionUtils.isNotEmpty(toUpdateScoreList)){
			scoreInfoService.saveAllEntitys(toUpdateScoreList.toArray(new ScoreInfo[0]));
		}
		
		//此人是否有录分权限
		int hasEditRole = scoreInfoService.getEditRole(null, scoreInfo.getTeachClassId(), scoreInfo.getSubjectId(), scoreInfo.getUnitId(), scoreInfo.getAcadyear(),
				scoreInfo.getSemester(), loginInfo.getOwnerId());
		if(hasEditRole == 2){
			if("1".equals(noLimit)){
				//无限制 为录分管理员
				hasEditRole = 0;
			}
			if("0".equals(noLimit)){
				//有限制 为普通录分人员
				hasEditRole = 1;
			}
		}
		// 考虑此人有 普通 和管理员两个角色，但是 1.在这个班级上没有普通录分角色，2.同时 他又选择以普通录分权限的角色进入，所以他没有这个班级的录分权限。
		if(hasEditRole == 0&& "0".equals(noLimit)){
			hasEditRole = -1;
		}
		
		map.put("hasEditRole", hasEditRole);
		
		//如果成绩已经锁定，需要打印成绩表 信息
		TeachClass teachClass = SUtils.dc(teachClassService.findOneById(scoreInfo.getTeachClassId()),TeachClass.class);
		if(Constant.IS_TRUE_Str.equals(classInfo.getIsLock())){
			int rowCount = (dtoList.size()+1)/2;
			map.put("rowCount", rowCount);
			map.put("stuCount", dtoList.size());
			
			//获取学校名
			Unit unit = SUtils.dc(unitRemoteService.findOneById(scoreInfo.getUnitId()), Unit.class);
			String acadyear = teachClass.getAcadyear();
			String semester = teachClass.getSemester();
			String semesterName = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-XQ", semester),McodeDetail.class).getMcodeContent();
			
			String className = null;
			//String gradeName = SUtils.dc(gradeRemoteService.findById(teachClass.getGradeId()),Grade.class).getGradeName();
			className = teachClass.getName();
			
			Course course = SUtils.dc(courseService.findOneById(scoreInfo.getSubjectId()),Course.class);
			
			map.put("unitName", unit.getUnitName());
			map.put("acadyear", acadyear);
			map.put("semesterName", semesterName);
			map.put("subjectName", course.getSubjectName());
			map.put("className", className);
		}
		
		map.put("classInfo", classInfo);
		//获取满分值
		
		map.put("dtoList", dtoList);
		map.put("acadyear", scoreInfo.getAcadyear());
		map.put("semester", scoreInfo.getSemester());
		map.put("unitId", scoreInfo.getUnitId());
		map.put("subjectId", scoreInfo.getSubjectId());
		map.put("teachClassId", scoreInfo.getTeachClassId());
		
		map.put("fullMark", teachClass.getFullMark());
		
		return viewName;
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
	@RequestMapping("/saveAll")
	@ResponseBody
	public String saveAll(ScoreInfoDto dto){
		String subjectId = dto.getSubjectId();
		String unitId = dto.getUnitId();
		String teachClassId = dto.getTeachClassId();
		TeachClass teachClass = SUtils.dc(teachClassService.findOneById(teachClassId), TeachClass.class);
		List<ScoreInfo> dtoList = dto.getDtoList();
		Map<String, ScoreInfo> scoreinfoMap = EntityUtils.getMap(dtoList, "id");

		//先检查是否已经被锁定
		ClassInfo classInfo = classInfoService.findByAll(unitId, ScoreDataConstants.CLASS_TYPE2, teachClassId, subjectId);
    	if(Constant.IS_TRUE_Str.equals(classInfo.getIsLock())){
    		return error("此班级数据已经锁定，无法再修改");
    	}
    	
		//有没有数据要保存
		if(dto==null || CollectionUtils.isEmpty(dtoList)){
    		return error("没有数据要保存");
    	}
		
		Map<String, String> teachStuClassMap = null;
		Map<String,TeachClass> teachClassMap = null;
		List<ScoreInfo> scoreInfList = null;
		if(Constant.IS_TRUE_Str.equals(teachClass.getIsMerge())){
			List<TeachClass> childTeachClassList = SUtils.dt(teachClassService.findByParentIds(new String[]{teachClassId}),TeachClass.class);
			for (TeachClass childTeachClass : childTeachClassList) {
				if(childTeachClass.getPassMark()==null)childTeachClass.setPassMark(60);
			}
			teachClassMap = EntityUtils.getMap(childTeachClassList, "id");
			List<TeachClassStu> teachClassStuList = SUtils.dt(teachClassStuService.findByClassIds(EntityUtils.getSet(childTeachClassList, "id").toArray(new String[0])),TeachClassStu.class);
			teachStuClassMap = EntityUtils.getMap(teachClassStuList,"studentId","classId");
			scoreInfList = scoreInfoService.findOptionalCourseScore(unitId,EntityUtils.getSet(childTeachClassList, "id").toArray(new String[0]));
		}else{
			scoreInfList = scoreInfoService.findOptionalCourseScore(unitId, subjectId, teachClassId);
		}
		
		try {
			if(CollectionUtils.isEmpty(scoreInfList)){
				//数据库中没有数据 执行添加操作
				List<Student> studentList = SUtils.dt(studentService.findListByIds(EntityUtils.getSet(dtoList, "studentId").toArray(new String[0])), Student.class);
				Map<String, Student> stuMap = EntityUtils.getMap(studentList, "id");
				String acadyear = teachClass.getAcadyear();
				String semester = teachClass.getSemester();
				for (ScoreInfo scoreInfo : dtoList) {
					if(StringUtils.isBlank(scoreInfo.getId())){
						scoreInfo.setId(UuidUtils.generateUuid());
					}
					scoreInfo.setAcadyear(acadyear);
					scoreInfo.setSemester(semester);
					scoreInfo.setUnitId(unitId);
					scoreInfo.setExamId(BaseConstants.ZERO_GUID);
					if(Constant.IS_TRUE_Str.equals(teachClass.getIsMerge())){
						scoreInfo.setTeachClassId(teachStuClassMap.get(scoreInfo.getStudentId()));
						scoreInfo.setSubjectId(teachClassMap.get(scoreInfo.getTeachClassId())==null?"":teachClassMap.get(scoreInfo.getTeachClassId()).getCourseId());
					}else{
						scoreInfo.setTeachClassId(teachClassId);
						scoreInfo.setSubjectId(subjectId);
					}
					scoreInfo.setStudentId(scoreInfo.getStudentId());
					scoreInfo.setOperatorId(getLoginInfo().getUserId());
					scoreInfo.setInputType(ScoreDataConstants.ACHI_SCORE);
					scoreInfo.setClassId(stuMap.get(scoreInfo.getStudentId()).getClassId());
				}
				scoreInfList = dtoList;
			}else{
				//有数据执行修改操作
				for (ScoreInfo scoreInfo : scoreInfList) {
					ScoreInfo scoreInfo2 = scoreinfoMap.get(scoreInfo.getId());
					scoreInfo.setScore(scoreInfo2.getScore());
					scoreInfo.setScoreStatus(scoreInfo2.getScoreStatus());
					scoreInfo.setOperatorId(getLoginInfo().getUserId());
				}
			}
			//赋予学分
			if(Constant.IS_TRUE_Str.equals(teachClass.getIsMerge())){
				for (ScoreInfo scoreInfo : scoreInfList) {
					TeachClass inTeachClass = teachClassMap.get(scoreInfo.getTeachClassId());
					if(inTeachClass!=null){
						if(Float.parseFloat(scoreInfo.getScore()) >= inTeachClass.getPassMark()){
							scoreInfo.setToScore(inTeachClass.getCredit()==null?null:(inTeachClass.getCredit()+""));
						}else{
							scoreInfo.setToScore("0");
						}
					}
				}
			}else{
				// 学分 及格分将要改动 有教学班获取
				Integer passMark = teachClass.getPassMark();
				if(passMark == null){
					passMark = 60;
				}
				Integer credit = teachClass.getCredit();
				for (ScoreInfo scoreInfo : scoreInfList) {
					if(Float.parseFloat(scoreInfo.getScore()) >= passMark){
						scoreInfo.setToScore(credit==null?null:(credit+""));
					}else{
						scoreInfo.setToScore("0");
					}
				}
			}
			scoreInfoService.saveAllEntitys(scoreInfList.toArray(new ScoreInfo[0]));
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("-1", "保存失败", e.getMessage());
		}
		
		return success("保存成功");
	}
	@RequestMapping("/creditStatisticMain")
	@ControllerInfo("学分统计")
	private String creditStaMain(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(1,unitId), Semester.class);
		List<Clazz> classList = SUtils.dt(classService.findBySchoolIdCurAcadyear(unitId,semester.getAcadyear()), new TR<List<Clazz>>() {});
		List<String> claIdList = classList.stream().map(Clazz::getId).collect(Collectors.toList());
		classList = SUtils.dt(classService.findClassListByIds(claIdList.toArray(new String[0])), new TR<List<Clazz>>() {});
		map.put("classList" , classList);

		return "/scoremanage/optionnalScoreStatictic/studentCreditShowMain.ftl";
	}
	@RequestMapping("/creditStatisticStuList")
	@ControllerInfo("学分统计班级列表展示")
	public String getClaStudentList(String classId ,ModelMap map){
		List<CourseType> courseTypeList = SUtils.dt(courseTypeRemoteService.findListBy(new String[]{"type","isDeleted"},new String[]{ScoreDataConstants.CLASS_TYPE2,"0"}),CourseType.class);
		courseTypeList =courseTypeList.stream().sorted((s1,s2) ->{
			return s1.getCode().compareTo(s2.getCode());
		}).collect(Collectors.toList());
		List<OptionalScoreStatisticDto> statisticDtoList = getstatisticDtoList(classId,courseTypeList);
		map.put("statisticDtoList" ,statisticDtoList);
		map.put("courseTypeList" ,courseTypeList);
		return "/scoremanage/optionnalScoreStatictic/studentCreditShowList.ftl";
	}
	private List<OptionalScoreStatisticDto> getstatisticDtoList(String classId  ,List<CourseType> courseTypeList ){
		String unitId = getLoginInfo().getUnitId();
		List<Student> studentList = Student.dt(studentRemoteService.findByClassIds(classId));
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(1,unitId), Semester.class);
		Set<String> stuIds = studentList.stream().map(Student::getId).collect(Collectors.toSet());
		List<ScoreInfo> scoreInfoList = scoreInfoService.findBystudentIds(unitId ,null,null,stuIds.toArray(new String[0]),ScoreDataConstants.ZERO32);
//		Map<String,ScoreInfo> scoreInfoMap = scoreInfoList.stream().collect(Collectors.toMap(scoreInfo -> {
//			return scoreInfo.getStudentId() + "_" + scoreInfo.getSubjectId()+"_"+scoreInfo.getTeachClassId();
//		},Function.identity()));
		Map<String,List<ScoreInfo>> scoreInfoListMap = new HashMap<>();
		for (ScoreInfo scoreInfo : scoreInfoList) {
			String key = scoreInfo.getStudentId() + "_" + scoreInfo.getSubjectId();
			List<ScoreInfo> list = scoreInfoListMap.get(key);
			if(list == null){
				list = new ArrayList<>();
				scoreInfoListMap.put(key ,list);
			}
			list.add(scoreInfo);
		}
		Set<String> teaClaId = scoreInfoList.stream().map(ScoreInfo::getTeachClassId).collect(Collectors.toSet());
		List<ClassInfo> classInfoList = classInfoService.findBySchoolIdAndClassIdIn(unitId,teaClaId.toArray(new String[0]));
		Map<String,ClassInfo> classInfoMap = classInfoList.stream().collect(Collectors.toMap(ClassInfo::getClassId, Function.identity()));

		Clazz clazz = SUtils.dc(classService.findOneById(classId) ,Clazz.class);

		List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdIn(new String[]{unitId},Integer.valueOf(ScoreDataConstants.CLASS_TYPE2),String.valueOf(clazz.getSection())),Course.class);
		courseList = courseList.stream().filter(s1 -> !"0".equals(s1.getIsUsing())).collect(Collectors.toList());
		Map<String ,List<Course>> coursetTypeListMap = EntityUtils.getListMap(courseList,Course::getCourseTypeId,Function.identity());
//		List<CourseType> courseTypeList = SUtils.dt(courseRemoteService.findListBy(new String[]{"type","isDeleted"},new String[]{ScoreDataConstants.CLASS_TYPE2,"0"}),CourseType.class);
//		courseTypeList =courseTypeList.stream().sorted((s1,s2) ->{
//			return s1.getCode().compareTo(s2.getCode());
//		}).collect(Collectors.toList());
		List<OptionalScoreStatisticDto> statisticDtoList = new ArrayList<>();
		for (Student student : studentList) {
			OptionalScoreStatisticDto dto = new OptionalScoreStatisticDto();
			dto.setClassName(clazz.getClassNameDynamic());
			dto.setStudentName(student.getStudentName());
			dto.setStudentCode(student.getStudentCode());
			List<String> scoreList = new ArrayList<>();
			for(CourseType courseType : courseTypeList){
				List<Course> list = coursetTypeListMap.get(courseType.getId());
				int score = 0;
				if(CollectionUtils.isNotEmpty(list)){
					for(Course course : list){
						//课程未启用
						if( 0 == course.getIsUsing() ){
							continue;
						}
						String key = student.getId() + "_" + course.getId();
						List<ScoreInfo>  scoreInfos = scoreInfoListMap.get(key);
						if(CollectionUtils.isEmpty(scoreInfos)){
							continue;
						}
						for (ScoreInfo info : scoreInfos) {
							ClassInfo classInfo = classInfoMap.get(info.getTeachClassId());
							if(classInfo == null){
								continue;
							}

							if(Constant.IS_TRUE_Str.equals(classInfo.getIsLock()) ){
								if(NumberUtils.isNumber(info.getToScore())){
									score += Integer.valueOf(info.getToScore());
								}

							}
						}



					}
				}

				scoreList.add(String.valueOf(score));
			}
			dto.setScoreList(scoreList);

			statisticDtoList.add(dto);
		}
		return  statisticDtoList;
	}
	@RequestMapping("/exportCreditStatisticStuList")
	public void exportScoreList( String classId , HttpServletResponse response){

		String[] fieldTitles = new String[] { "姓名", "原班级", "转入班级",
				"转班时间", "操作人" };
		List<String> titleList = new ArrayList<>();
		titleList.add("班级");
		titleList.add("姓名");
		titleList.add("学号");
		List<CourseType> courseTypeList = SUtils.dt(courseTypeRemoteService.findListBy(new String[]{"type","isDeleted"},new String[]{ScoreDataConstants.CLASS_TYPE2,"0"}),CourseType.class);
		courseTypeList =courseTypeList.stream().sorted((s1,s2) ->{
			return s1.getCode().compareTo(s2.getCode());
		}).collect(Collectors.toList());
		List<OptionalScoreStatisticDto> statisticDtoList = getstatisticDtoList(classId,courseTypeList);
		for (CourseType courseType : courseTypeList) {
			titleList.add(courseType.getName());
		}

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 10);// 字体大小
		headfont.setBold(true);// 加粗
		style.setFont(headfont);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);


		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0,"选修课学分统计");

		HSSFRow rowTitle = sheet.createRow(0);

		for(int i=0;i<titleList.size();i++){
			String title =titleList.get(i);
			HSSFCell cell = rowTitle.createCell(i);
			cell.setCellValue(new HSSFRichTextString(title));
		}
		int j=1;
		for (OptionalScoreStatisticDto statisticDto : statisticDtoList) {

			HSSFRow rowVal = sheet.createRow(j);

			HSSFCell cell = rowVal.createCell(0);
			cell.setCellValue(new HSSFRichTextString(statisticDto.getClassName()));
			cell = rowVal.createCell(1);
			cell.setCellValue(new HSSFRichTextString(statisticDto.getStudentName()));
			cell = rowVal.createCell(2);
			cell.setCellValue(new HSSFRichTextString(statisticDto.getStudentCode()));
			List<String> list = statisticDto.getScoreList();
			int i=3;
			for(String str : list){
				cell = rowVal.createCell(i);
				cell.setCellValue(new HSSFRichTextString(str));
				i++;
			}
			j++;

		}
		ExportUtils.outputData(workbook,"选修课学分统计",response);
	}
}
