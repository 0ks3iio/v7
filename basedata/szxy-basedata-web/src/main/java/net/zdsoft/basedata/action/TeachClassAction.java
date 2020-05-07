package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.OpenTeachingSearchDto;
import net.zdsoft.basedata.dto.StudentDto;
import net.zdsoft.basedata.dto.TeachClassSaveDto;
import net.zdsoft.basedata.dto.TeachClassSearchDto;
import net.zdsoft.basedata.dto.TimePlaceDto;
import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.entity.ClassHourEx;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassEx;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.ClassHourExService;
import net.zdsoft.basedata.service.ClassHourService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.GradeTeachingService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.TeachClassExService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachClassStuService;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

@Controller
@RequestMapping("/basedata/teachclass")
public class TeachClassAction extends BaseAction {

	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TeachClassStuService teachClassStuService;
	@Autowired
	private ClassService classService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private TeachPlaceService teachPlaceService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private TeachClassExService teachClassExService;
	@Autowired
	private ClassTeachingService classTeachingService;
	@Autowired
	private GradeTeachingService gradeTeachingService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassHourService classHourService;
	@Autowired
	private ClassHourExService classHourExService;
	@RequestMapping("/index/page")
	@ControllerInfo("进入教学班模块")
	public String execute(String showTabType,ModelMap map) {
//		School school = schoolService.findOne(getLoginInfo().getUnitId());
//		if(school!=null && school.getSections().indexOf(BaseConstants.SECTION_HIGH_SCHOOL.toString())>=0){
//			map.put("isShowSeven", true);
//		}else{
//			map.put("isShowSeven", false);
//		}
		//map.put("showTabType", showTabType);
		//if("1".equals(flag)){
			//System.out.println("点击返回进入");
			//map.put("tabType", "2");
			//map.put("acadyear", acadyear);
			//map.put("semester", semester);
			//map.put("showTabType", showTabType);
			//String limit = systemIniRemoteService.findValue(BaseConstants.TEACLSS_TEACHING_LIMIE);
			//map.put("limit", limit);
		//}
		map.put("showTabType", showTabType);
		return "/basedata/teachClass/teachclassIndex.ftl";
	}
	
	@RequestMapping("/indexItem/page")
	@ControllerInfo("进入教学班模块页面展现")
	public String gotoIndexItem(String showTabType, ModelMap map) {
//		School school = schoolService.findOne(getLoginInfo().getUnitId());
//		if(school!=null && school.getSections().indexOf(BaseConstants.SECTION_HIGH_SCHOOL.toString())>=0){
//			map.put("isShowSeven", true);
//		}else{
//			map.put("isShowSeven", false);
//		}
//		if("2".equals(flag)) {
//			//System.out.println("点击返回进入");
//			map.put("flag", "1");
//			map.put("tabType", "2");
//			map.put("acadyear", acadyear);
//			map.put("semester", semester);
//			String limit = systemIniRemoteService.findValue(BaseConstants.TEACLSS_TEACHING_LIMIE);
//			map.put("limit", limit);
//			map.put("showTabType", showTabType);
//		}
		map.put("showTabType", showTabType);
		return "/basedata/teachClass/teachclassIndexItem.ftl";
	}

	// 初始页
	@RequestMapping("/head/page")
	@ControllerInfo("进入教学班模块")
	public String showHead(String flag, ModelMap map,TeachClassSearchDto dto) {
		List<String> acadyearList = semesterService.findAcadeyearList();
		if(CollectionUtils.isEmpty(acadyearList)){
			return promptFlt(map, "请先设置学年学期");
		}
		map.put("acadyearList", acadyearList);
		String acadyear="";
		String semester="";
		List<Grade> gradeList = new ArrayList<Grade>();
        
		if(dto!=null){
			if(StringUtils.isNotBlank(dto.getAcadyearSearch()) && StringUtils.isNotBlank(dto.getSemesterSearch())){
				acadyear=dto.getAcadyearSearch();
			    semester=dto.getSemesterSearch();
			}
			if(StringUtils.isNotBlank(dto.getGradeIds())){
				map.put("gradeIds", dto.getGradeIds());
			}
			if(StringUtils.isNotBlank(dto.getTeachClassName())){
				map.put("teachClassName", dto.getTeachClassName());
			}
			if(StringUtils.isNotBlank(dto.getStudentName())){
				map.put("studentName", dto.getStudentName());
			}
			if(StringUtils.isNotBlank(dto.getTeacherName())){
				map.put("teacherName", dto.getTeacherName());
			}
			gradeList = gradeService.findByUnitId(getLoginInfo().getUnitId());

//			if(BaseConstants.SUBJECT_TYPE_BX.equals(dto.getShowTabType())){
//				//必修课
//				gradeList = gradeService.findByUnitId(getLoginInfo().getUnitId());
//			}else if(BaseConstants.SUBJECT_TYPE_XX.equals(dto.getShowTabType())){
//				//选修课
//				gradeList = gradeService.findByUnitId(getLoginInfo().getUnitId());
//			}else if(String.valueOf(BaseConstants.TYPE_COURSE_DISCIPLINE).equals(dto.getShowTabType())){
//				//7选3
//				gradeList=gradeService.findByUnitIdNotGraduate(getLoginInfo().getUnitId(), new Integer[]{BaseConstants.SECTION_HIGH_SCHOOL});
//			}else{
//				return promptFlt(map, "参数丢失，请重新加载");
//			}
			if(StringUtils.isNotBlank(dto.getShowTabType())){
				map.put("tabType", dto.getShowTabType());
			}
		}else{
			return promptFlt(map, "参数丢失，请重新加载");
		}
		if(StringUtils.isBlank(acadyear)|| StringUtils.isBlank(semester)){
			//取学校当前学年学期
			Semester sem = semesterService.findCurrentSemester(2,getLoginInfo().getUnitId());
			if(sem!=null ){
				acadyear=sem.getAcadyear();
				semester=String.valueOf(sem.getSemester());
			}
		}
		String limit = systemIniRemoteService.findValue(BaseConstants.TEACLSS_TEACHING_LIMIE);
		map.put("limit", limit);
		map.put("gradeList", gradeList);
		map.put("acadyear", acadyear);
	    map.put("semester", semester);
//	    if(!"2".equals(dto.getShowTabType())) {
//        	flag = null;
//	    	map.put("flag", null);
//        }
//        if("1".equals(flag)){
//        	map.put("tabType", "2");
//        }
        map.put("tabType", dto.getShowTabType());
		return "/basedata/teachClass/teachclassHead.ftl";
	}
	@ResponseBody
	@RequestMapping("/gradeOpenSubject")
	@ControllerInfo("进入单位下学年学期班级列表下的班级页面")
	public List<Course> gradeOpenSubject(TeachClassSearchDto dto){
		dto.makeClassType();
		OpenTeachingSearchDto searchDto=new OpenTeachingSearchDto();
		searchDto.setAcadyear(dto.getAcadyearSearch());
		searchDto.setSemester(dto.getSemesterSearch());
		searchDto.setIsDeleted(0);
		searchDto.setUnitId(getLoginInfo().getUnitId());
		List<Course> courseList = new ArrayList<Course>();
		if(TeachClass.CLASS_TYPE_REQUIRED.equals(dto.getClassType())){
			//必修课--取得学年学期该单位的所有年级开设必修课
			searchDto.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
		}else if(TeachClass.CLASS_TYPE_ELECTIVE.equals(dto.getClassType())){
			//选修课 --取得学年学期该单位的所有年级开设选修课
			searchDto.setSubjectType(BaseConstants.SUBJECT_TYPE_XX);
		}
//		else if(TeachClass.CLASS_TYPE_SEVEN.equals(dto.getClassType())){
//			//7选3也没有查看 默认取的7门
//			String[] subjectCode=BaseConstants.SUBJECT_73;
//			courseList=courseService.findBySubjectCodes(subjectCode);
//			return courseList;
//		}
		else{
			return new ArrayList<Course>();
		}
		List<GradeTeaching> list = gradeTeachingService.findBySearch(searchDto);
		
		if(CollectionUtils.isNotEmpty(list)){
			Set<String> subjectIds = EntityUtils.getSet(list, "subjectId");
			courseList=courseService.findListByIdIn(subjectIds.toArray(new String[]{}));
		}
		return courseList;
	}

	@RequestMapping("/teachclassList/page")
	@ControllerInfo("进入单位下学年学期班级列表下的班级页面")
	public String showTeachClassList(TeachClassSearchDto dto, String useMaster, ModelMap map) {
		String limit = systemIniRemoteService.findValue(BaseConstants.TEACLSS_TEACHING_LIMIE);
		List<Student> studentList = new ArrayList<Student>();
		String studentName = dto.getStudentName().trim();
		dto.setUnitId(getLoginInfo().getUnitId());
		dto.makeClassType();
		if(StringUtils.isNotBlank(dto.getTeachClassName())){
			dto.setTeachClassName(dto.getTeachClassName().trim());
		}
		if(StringUtils.isNotBlank(dto.getTeacherName())){
			dto.setTeacherName(dto.getTeacherName().trim());
		}
		
		//根据查询条件
		List<TeachClass> teachClassList=null;
		if(Objects.equals(useMaster, "1")) {
			teachClassList=teachClassService.findListByDtoWithMaster(dto);
		}else {
			teachClassList=teachClassService.findListByDto(dto);
		}
		//根据学生姓名过滤教学班
		//TODO 
		if(StringUtils.isNotEmpty(studentName)) {
			String unitId = getLoginInfo().getUnitId();
			ArrayList<String> list = new ArrayList<String>();
			list.add(unitId);
			studentList = SUtils.dt(studentRemoteService.findBySchoolIdIn(studentName, list.toArray(new String[list.size()])), new TR<List<Student>>() {});
			Set<String> studentIdSet = EntityUtils.getSet(studentList, "id");
			List<TeachClass> tempTeachClassList = teachClassStuService.findByStudentIds(studentIdSet.toArray(new String[studentIdSet.size()]), dto.getAcadyearSearch(), dto.getSemesterSearch());
			for(int i = 0; i < teachClassList.size(); i++) {
				int flag = 1;
				for(int j = 0; j < tempTeachClassList.size(); j++) {
					if(teachClassList.get(i).getId().equals(tempTeachClassList.get(j).getId())){
						flag = 0;
						break;
					}
				}
				if(flag == 1) {
					teachClassList.remove(i);
					i--;
				}
			}
			Set<String> classIdSet = new HashSet<String>();
			for(int i = 0; i < teachClassList.size(); i++) {
				String isUsingMerge = teachClassList.get(i).getIsUsingMerge();
				if("1".equals(isUsingMerge)){
					String parentId = teachClassList.get(i).getParentId();
					if(StringUtils.isNotEmpty(parentId)){
						classIdSet.add(parentId);
					}
				}
			}
			List<TeachClass> bigClass = teachClassService.findListByIdIn(classIdSet.toArray(new String[classIdSet.size()]));
			for (TeachClass teachClass : bigClass) {
				teachClassList.add(teachClass);
			}
		}
		
		if(CollectionUtils.isNotEmpty(teachClassList)){
			//组装上课时间及场地 科目 教师 学生人数
			teachClassService.makeTimePlace(teachClassList,true);
		}
		map.put("limit", limit);
		map.put("tabType", dto.getShowTabType());
		map.put("teachClassList", teachClassList);
		return "/basedata/teachClass/teachclassList.ftl";
	}
	@RequestMapping("/studentList/page")
	@ControllerInfo("进入教学班下学生页面")
	public String showTeachClassStudentList(String teachClassId,String isView,String useMaster,TeachClassSearchDto dto,ModelMap map) {
		if(dto==null){
			dto=new TeachClassSearchDto();
		}
		TeachClass teachClass = teachClassService.findOne(teachClassId);
		if(teachClass==null){
			return errorFtl(map, "查询的教学班已经不存在");
		}
		map.put("teachClass", teachClass);
		
		String[] teachClassIds = null;
		if( Constant.IS_TRUE_Str.equals(teachClass.getIsMerge())){
			List<TeachClass> childTeachClassList = teachClassService.findByParentIds(new String[]{teachClassId});
			teachClassIds = EntityUtils.getSet(childTeachClassList, "id").toArray(new String[0]);
		}else{
			teachClassIds = new String[]{teachClassId};
		}
		List<TeachClassStu> teachStudentList=null;
		if(Objects.equals(useMaster, "1")) {
			teachStudentList = teachClassStuService.findByClassIdsWithMaster(teachClassIds);
		}else{
			teachStudentList = teachClassStuService.findByClassIds(teachClassIds);
		}
		List<StudentDto> dtos = Lists.newArrayList();
		List<Student> stent =studentService.findListBlendClassIds(teachClassIds);
		Map<String,Student> czmap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(stent)){
			czmap = stent.stream().collect(Collectors.toMap(t->t.getId(),t->t));
		}
		if(CollectionUtils.isNotEmpty(teachStudentList)){
			Set<String> studentIds = EntityUtils.getSet(teachStudentList,"studentId");

			List<Student> students = studentService.findListByIds(studentIds.toArray(new String[]{}));

			if(CollectionUtils.isNotEmpty(students)){
				Set<String> classIds = EntityUtils.getSet(students, "classId");
				List<Clazz> clazzList = classService.findListByIds(classIds.toArray(new String[]{}));
				Map<String, Clazz> clazzMap=new HashMap<String, Clazz>();
				if(CollectionUtils.isNotEmpty(clazzList)){
					clazzMap=EntityUtils.getMap(clazzList, "id");
				}
				for(Student st : students){
					if(!czmap.containsKey(st.getId())){
						continue;
					}
					StudentDto studentDto = new StudentDto();
					studentDto.setStudent(st);
					if(clazzMap.get(st.getClassId()) != null){
						studentDto.setClassName(clazzMap.get(st.getClassId()).getClassNameDynamic());
						studentDto.setClassCode(clazzMap.get(st.getClassId()).getClassCode());
					}
					dtos.add(studentDto);
				}
				/**
				 * students 排序     dtos  classCode
				 */
				sortStudentDto(dtos);
			}
		}
		map.put("searchDto", dto);
		map.put("dtos", dtos);
		map.put("isView", isView);
		return "/basedata/teachClass/teachClassStudentList.ftl";
	}
	
	private void sortStudentDto(List<StudentDto> dtos){
		if(CollectionUtils.isEmpty(dtos)){
			return;
		}
		//升序
		Collections.sort(dtos, new Comparator<StudentDto>() {
			 
            @Override
            public int compare(StudentDto o1, StudentDto o2) {
            	if(StringUtils.isNotBlank(o1.getStudent().getUnitiveCode())&&StringUtils.isNotBlank(o2.getStudent().getUnitiveCode())){
            		return o1.getStudent().getUnitiveCode().compareTo(o2.getStudent().getUnitiveCode());
            	}
            	return 0;
            }
               /*if(StringUtils.isNotBlank(o1.getClassCode())){
            	   if(StringUtils.isNotBlank(o2.getClassCode())){
            		   
            		   if(o2.getClassCode().equals(o1.getClassCode())){
            			   String o1Code = o1.getStudent().getStudentCode();
            			   String o2Code = o2.getStudent().getStudentCode();
            			   if(StringUtils.isNotBlank(o1Code)){
            				   if(StringUtils.isNotBlank(o2Code)){
            					   return o1Code.compareTo(o2Code);
            				   }else{
            					   return 1;
            				   }
            			   }else{
            				   return -1;
            			   }
            		   }else{
            			   return o1.getClassCode().compareTo(o2.getClassCode());
            		   }
            		   
            	   }else{
            		   return 1;
            	   }
            	   
               }else{
            	   if(StringUtils.isBlank(o2.getClassCode())){
            		   //班级code都是null
            		   String o1Code = o1.getStudent().getStudentCode();
        			   String o2Code = o2.getStudent().getStudentCode();
        			   if(StringUtils.isNotBlank(o1Code)){
        				   if(StringUtils.isNotBlank(o2Code)){
        					   return o1Code.compareTo(o2Code);
        				   }else{
        					   return 1;
        				   }
        			   }else{
        				   return -1;
        			   }
            	   }else{
            		   return -1;
            	   }
            	   
               }
            }*/
        });
	}
	
	/**
	 * 一旦有重复 就返回
	 * @param stuSearchDto
	 * @param studentId
	 * @param timestr
	 * @param teachClassId
	 * @return
	 */
	private String checkStudentTime(CourseScheduleDto stuSearchDto,String[] studentId,List<String> timestr,String teachClassId){
		if(stuSearchDto==null){
			return null;
		}
		List<Student> studentList = studentService.findListByIdIn(studentId);
		if(CollectionUtils.isEmpty(studentList)){
			//学生都不存在 就返回null;
			return null;
		}
		//根据学生id取得班级is_using=1的教学班 以及所在的行政班
		List<TeachClass> teachClassList=teachClassService.findByStudentIds(stuSearchDto.getAcadyear(),stuSearchDto.getSemester()+"",stuSearchDto.getSchoolId(),studentId);
		Set<String> classIds=new HashSet<String>();
		Map<String,TeachClass> jxbMap=new HashMap<String,TeachClass>();
		if(CollectionUtils.isNotEmpty(teachClassList)){
			classIds.addAll(EntityUtils.getSet(teachClassList, "id"));
			jxbMap=EntityUtils.getMap(teachClassList, "id");
		}
		
		Map<String,Clazz> xzbMap=new HashMap<String,Clazz>();
		if(CollectionUtils.isNotEmpty(studentList)){
			Set<String> xzbclassId=new HashSet<String>();
			xzbclassId.addAll(EntityUtils.getSet(studentList, "classId"));
			List<Clazz> clazzList = classService.findListByIds(xzbclassId.toArray(new String[]{}));
			xzbMap=EntityUtils.getMap(clazzList, "id");
			classIds.addAll(EntityUtils.getSet(clazzList, "id"));
		}
		if(classIds.size()<=0){
			return null;
		}
		classIds.remove(teachClassId);
		if(classIds.size()<=0){
			return null;
		}
		int weekOfWorktime1=stuSearchDto.getWeekOfWorktime1();
		int weekOfWorktime2=stuSearchDto.getWeekOfWorktime2();
		int dayOfWeek1=stuSearchDto.getDayOfWeek1();
		int dayOfWeek2=stuSearchDto.getDayOfWeek2();
		stuSearchDto.setClassIds(classIds.toArray(new String[]{}));
		List<CourseSchedule> courseList = courseScheduleService.findByTimes(stuSearchDto, timestr.toArray(new String[]{}));
		
		for(CourseSchedule c:courseList){
			if(c.getWeekOfWorktime()==weekOfWorktime1 && c.getDayOfWeek()<dayOfWeek1){
				continue;
			}
			if(c.getWeekOfWorktime()==weekOfWorktime2 && c.getDayOfWeek()>dayOfWeek2){
				continue;
			}
			//场地是不是冲突
			String mess=BaseConstants.dayOfWeekMap.get(c.getDayOfWeek()+"")+BaseConstants.PERIOD_INTERVAL_Map.get(c.getPeriodInterval())+"第"+c.getPeriod()+"节";
			if(jxbMap.containsKey(c.getClassId())){
				return "在"+mess+"，存在学生已经在"+jxbMap.get(c.getClassId()).getName()+"上课";
			}else if(xzbMap.containsKey(c.getClassId())){
				return "在"+mess+"，存在学生已经在"+xzbMap.get(c.getClassId()).getClassNameDynamic()+"上课";
			}
			return "在"+mess+"，存在学生上课冲突";
		}
		return null;
	}
	
	/**
	 * 取得时间重复的学生id
	 * @param stuSearchDto
	 * @param studentId
	 * @param timestr
	 * @param teachClassId
	 * @return
	 */
	private List<String> findSameTimeStudentIds(CourseScheduleDto stuSearchDto,String[] studentId,List<String> timestr,String teachClassId){
		if(stuSearchDto==null){
			return new ArrayList<String>();
		}
		List<Student> studentList = studentService.findListByIdIn(studentId);
		if(CollectionUtils.isEmpty(studentList)){
			//学生都不存在 就返回null;
			return new ArrayList<String>();
		}
		//判断学生整体用List
		List<String> checkStudentId = Arrays.asList(studentId);
		//key:classId(包括行政班，教学班)
		Map<String,Set<String>> studentIdByClass=new HashMap<String,Set<String>>();
		Set<String> classIds=new HashSet<String>();
//		Map<String,String> classNameById=new HashMap<String,String>();
		

		
		//根据学生id取得班级is_using=1的教学班 以及所在的行政班
		List<TeachClass> teachClassList=teachClassService.findByStudentIds(stuSearchDto.getAcadyear(),stuSearchDto.getSemester()+"",stuSearchDto.getSchoolId(),studentId);
		if(CollectionUtils.isNotEmpty(teachClassList)){
			//取得教学班下的学生
			Set<String> jxbClassIds = EntityUtils.getSet(teachClassList, "id");
			jxbClassIds.remove(teachClassId);//去掉相同教学班
			List<TeachClassStu> stuList = teachClassStuService.findByClassIds(jxbClassIds.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(stuList)){
				for(TeachClassStu ss:stuList){
					if(!checkStudentId.contains(ss.getStudentId())){
						//去除无用学生
						continue;
					}
					if(!studentIdByClass.containsKey(ss.getClassId())){
						studentIdByClass.put(ss.getClassId(), new HashSet<String>());
					}
					studentIdByClass.get(ss.getClassId()).add(ss.getStudentId());
				}
			}
			classIds.addAll(jxbClassIds);
		}
		
		if(CollectionUtils.isNotEmpty(studentList)){
			for(Student ss:studentList){
				if(!studentIdByClass.containsKey(ss.getClassId())){
					studentIdByClass.put(ss.getClassId(), new HashSet<String>());
				}
				studentIdByClass.get(ss.getClassId()).add(ss.getId());
			}
			Set<String> xzbclassId=new HashSet<String>();
			xzbclassId.addAll(EntityUtils.getSet(studentList, "classId"));
			List<Clazz> clazzList = classService.findListByIds(xzbclassId.toArray(new String[]{}));
			classIds.addAll(EntityUtils.getSet(clazzList, "id"));
		}
		if(classIds.size()<=0){
			return null;
		}
		int weekOfWorktime1=stuSearchDto.getWeekOfWorktime1();
		int weekOfWorktime2=stuSearchDto.getWeekOfWorktime2();
		int dayOfWeek1=stuSearchDto.getDayOfWeek1();
		int dayOfWeek2=stuSearchDto.getDayOfWeek2();
		stuSearchDto.setClassIds(classIds.toArray(new String[]{}));
		List<CourseSchedule> courseList = courseScheduleService.findByTimes(stuSearchDto, timestr.toArray(new String[]{}));
		Set<String> sameStudent=new HashSet<String>();
		for(CourseSchedule c:courseList){
			if(c.getWeekOfWorktime()==weekOfWorktime1 && c.getDayOfWeek()<dayOfWeek1){
				continue;
			}
			if(c.getWeekOfWorktime()==weekOfWorktime2 && c.getDayOfWeek()>dayOfWeek2){
				continue;
			}
			if(studentIdByClass.containsKey(c.getClassId())){
				Set<String> ssId = studentIdByClass.get(c.getClassId());
				if(CollectionUtils.isNotEmpty(ssId)){
					sameStudent.addAll(ssId);
				}
			}
			if(sameStudent.containsAll(checkStudentId)){
				//包括所有
				break;
			}
			
		}
		if(CollectionUtils.isNotEmpty(sameStudent)){
			return Arrays.asList(sameStudent.toArray(new String[]{}));
		}
		return new ArrayList<String>();
	}
	
	
	@ResponseBody
    @RequestMapping("/deleteClassStudent")
    @ControllerInfo("删除教学班下某个学生")
	public String deleteClassStudent(String teachClassId,String studentId){
		if (StringUtils.isEmpty(teachClassId)) {
			return error("teachClassId为空无法删除!");
		}
		if (StringUtils.isEmpty(studentId)) {
			return error("studentId为空无法删除!");
		}
		
		try {
			int count=teachClassStuService.delete(new String[]{teachClassId},new String[]{studentId});
			if(count==0){
				return success("班级下学生已不存在，无需删除");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！" + e.getMessage());
		}

		return success("删除成功！");
	}
	
	// 新增或者修改页面或者查看
	@RequestMapping("/addOredit/page")
	public String addOrEdit(String id, TeachClassSearchDto dto,ModelMap map) {
		TeachClass teachClass = null;
		/**
		 * 一周上课天数默认 7天 8节课
		 */
		map.put("dayOfWeek",7);
		/**
		 * 场地
		 */
		List<TeachPlace> placeList = teachPlaceService.findTeachPlaceListByType(getLoginInfo().getUnitId(), null);
		map.put("placeList", placeList);
		
		String limit = systemIniRemoteService.findValue(BaseConstants.TEACLSS_TEACHING_LIMIE);
		if(StringUtils.isBlank(limit)){
			limit="0";
		}
		map.put("limit", limit);
		
		
		if(StringUtils.isNotBlank(id)){
			boolean isEdit=false;
			if("1".equals(dto.getShowView())){
				//编辑
				isEdit=true;
			}else{
				//查看
			}
			//编辑
			teachClass = teachClassService.findById(id);
			if(teachClass==null || teachClass.getIsDeleted()==1){
				return errorFtl(map, "查看的教学班已不存在，请刷新后操作");
			}
			if("0".equals(teachClass.getIsUsing()) && isEdit){
				isEdit=false;
				//return errorFtl(map, "选择的教学班已经完成教学，不能编辑");
			}
			if(TeachClass.CLASS_TYPE_REQUIRED.equals(teachClass.getClassType())){
				//必修课--取得学年学期年级必修课课程
				if(teachClass.getGradeId().length()!=32){
					return promptFlt(map, "参数丢失请重新操作");
				}
				
				if(!makeTimeByGrade(new String[]{teachClass.getGradeId()}, map)){
					return promptFlt(map, "选中的年级参数丢失，请重新操作");
				}
				
			}else if(TeachClass.CLASS_TYPE_ELECTIVE.equals(teachClass.getClassType())){
				//选修课 --取得学年学期该单位的所有选修课
				String[] gradeIdArr = teachClass.getGradeId().split(",");
				
				if(!makeTimeByGrade(gradeIdArr, map)){
					return promptFlt(map, "选中的年级参数丢失，请重新操作");
				}
				
			}else if(TeachClass.CLASS_TYPE_SEVEN.equals(teachClass.getClassType())){
				//7选3也没有查看
				return promptFlt(map, "参数值不对，请刷新后操作");
			}else{
				return promptFlt(map, "参数值不对，请刷新后操作");
			}
			Course course = courseService.findOne(teachClass.getCourseId());
			teachClass.setCourseName(course==null?"":course.getSubjectName());
			Teacher teacher = teacherService.findOne(teachClass.getTeacherId());
			teachClass.setTeacherNames(teacher==null?"":teacher.getTeacherName());
			//已有的上课时间
			List<TeachClassEx> exList=teachClassExService.findByTeachClassId(teachClass.getId());
			//编辑的关联课程 默认直接拿所有
			map.put("teachClass", teachClass);
			map.put("exList", exList);
			map.put("isEdit", isEdit);
			if(!isEdit){
				makeTimeStr(exList);
				//只能查看
				if(StringUtils.isNotBlank(teachClass.getRelaCourseId())) {
				    List<ClassHour> hourList = classHourService.findListByIdIn(new String[] {teachClass.getRelaCourseId()});
				    List<ClassHour> ll = classHourService.makeClassNames(hourList);
				    if(CollectionUtils.isNotEmpty(ll)) {
				    	ClassHour hour = ll.get(0);
						Course cou = courseService.findOne(hour.getSubjectId());
						map.put("relaName", cou.getSubjectName()+"("+hour.getClassNames()+")");
					}
				}
				if(StringUtils.isNotBlank(teachClass.getPlaceId())) {
					TeachPlace place = teachPlaceService.findOne(teachClass.getPlaceId());
					if(place!=null) {
						map.put("placeName", place.getPlaceName());
					}
				}
			}else{
				List<GradeTeaching> gradeTeachingList=new ArrayList<GradeTeaching>();
				
				OpenTeachingSearchDto gradeSearchdto = new OpenTeachingSearchDto();
				gradeSearchdto.setUnitId(teachClass.getUnitId());
				gradeSearchdto.setIsDeleted(0);
				gradeSearchdto.setSemester(teachClass.getSemester());
				gradeSearchdto.setAcadyear(teachClass.getAcadyear());
				
				if(TeachClass.CLASS_TYPE_REQUIRED.equals(teachClass.getClassType())){
					//必修课--取得学年学期年级必修课课程
					if(teachClass.getGradeId().length()!=32){
						return promptFlt(map, "参数丢失请重新操作");
					}
					gradeSearchdto.setSubjectType(TeachClass.CLASS_TYPE_REQUIRED);
					gradeSearchdto.setGradeIds(new String[]{teachClass.getGradeId()});
					gradeTeachingList=gradeTeachingService.findBySearch(gradeSearchdto);
				}else if(TeachClass.CLASS_TYPE_ELECTIVE.equals(teachClass.getClassType())){
					//选修课 --取得学年学期该单位的所有选修课
					String[] gradeIdArr = teachClass.getGradeId().split(",");
					gradeSearchdto.setSubjectTypes(new String[]{BaseConstants.SUBJECT_TYPE_XX,BaseConstants.SUBJECT_TYPE_XX_4,BaseConstants.SUBJECT_TYPE_XX_6,BaseConstants.SUBJECT_TYPE_XX_6});
					gradeSearchdto.setGradeIds(gradeIdArr);
					gradeTeachingList=gradeTeachingService.findBySearch(gradeSearchdto);
					
				}else{
					return promptFlt(map, "参数值不对，请刷新后操作");
				}
				
				//课程列表
				if(CollectionUtils.isEmpty(gradeTeachingList)){
					return promptFlt(map, "年级课程开设开设数据调整");
				}
				Set<String> subjectIds = EntityUtils.getSet(gradeTeachingList, e->e.getSubjectId());
				List<Course> allCourseList=courseService.findListByIdIn(subjectIds.toArray(new String[]{}));
				if(CollectionUtils.isEmpty(allCourseList)){
					return promptFlt(map, "年级课程开设开设数据调整");
				}
				//过滤学段
				List<Course> relaCourseList =new ArrayList<>();
				List<ClassHour> allHourList=new ArrayList<>();
				if(TeachClass.CLASS_TYPE_REQUIRED.equals(teachClass.getClassType())) {
					//获取虚拟课程
					Grade grade = gradeService.findOne(teachClass.getGradeId());
					if(grade!=null) {
						 List<ClassHour> classHourList = classHourService.findListByUnitId(teachClass.getAcadyear(), teachClass.getSemester(),teachClass.getUnitId() , grade.getId(), false);
						 if(CollectionUtils.isNotEmpty(classHourList)) {
							 Set<String> vCourseIds = EntityUtils.getSet(classHourList, e->e.getSubjectId());
							 relaCourseList =courseService.findListByIdIn(vCourseIds.toArray(new String[0]));
							 allHourList=classHourService.makeClassNames(classHourList);
						 }	
					}
				}
				map.put("allHourList", allHourList);
				map.put("relaCourseList", relaCourseList);
			}
			
			return "/basedata/teachClass/teachclassEdit.ftl";
		}else{
			//新增
			String unitId = getLoginInfo().getUnitId();
			dto.makeClassType();
			teachClass=new TeachClass();
			teachClass.setAcadyear(dto.getAcadyearSearch());
			teachClass.setSemester(dto.getSemesterSearch());
			teachClass.setClassType(dto.getClassType());
			teachClass.setUnitId(unitId);
			//必修课是默认只有一个年级的
			if(StringUtils.isBlank(dto.getGradeIds())){
				return promptFlt(map, "参数丢失请重新操作");
			}
			
			
			teachClass.setGradeId(dto.getGradeIds());
			map.put("tabType", dto.getShowTabType());
			
			
			/**
			 * 年级开课计划 需要走班 gradeTeachingList
			 */
			List<GradeTeaching> gradeTeachingList=new ArrayList<GradeTeaching>();
			
			OpenTeachingSearchDto gradeSearchdto = new OpenTeachingSearchDto();
			gradeSearchdto.setUnitId(teachClass.getUnitId());
			gradeSearchdto.setIsDeleted(0);
			gradeSearchdto.setSemester(teachClass.getSemester());
			gradeSearchdto.setAcadyear(teachClass.getAcadyear());
			if(TeachClass.CLASS_TYPE_REQUIRED.equals(dto.getClassType())){
				//必修课--取得学年学期年级必修课课程
				if(dto.getGradeIds().length()!=32){
					return promptFlt(map, "参数丢失请重新操作");
				}
				/**
				 * dto.getGradeIds() 上课时间
				 * 默认 5天 8节课
				 */
				if(!makeTimeByGrade(new String[]{dto.getGradeIds()}, map)){
					return promptFlt(map, "选中的年级丢失，请重新操作");
				}
				gradeSearchdto.setSubjectType(TeachClass.CLASS_TYPE_REQUIRED);
				gradeSearchdto.setGradeIds(new String[]{dto.getGradeIds()});
				gradeTeachingList=gradeTeachingService.findBySearch(gradeSearchdto);
				
			}else if(TeachClass.CLASS_TYPE_ELECTIVE.equals(dto.getClassType())){
				//选修课 --取得学年学期该单位的所有选修课
				String[] gradeIdArr = dto.getGradeIds().split(",");
				/**
				 * gradeIdArr 上课时间
				 */
				if(!makeTimeByGrade(gradeIdArr, map)){
					return promptFlt(map, "选中的年级丢失，请重新操作");
				}
				gradeSearchdto.setSubjectTypes(new String[]{BaseConstants.SUBJECT_TYPE_XX,BaseConstants.SUBJECT_TYPE_XX_4,BaseConstants.SUBJECT_TYPE_XX_6,BaseConstants.SUBJECT_TYPE_XX_6});
				gradeSearchdto.setGradeIds(gradeIdArr);
				gradeTeachingList=gradeTeachingService.findBySearch(gradeSearchdto);
				
			}else{
				return promptFlt(map, "参数值不对，请刷新后操作");
			}
			
			//课程列表
			if(CollectionUtils.isEmpty(gradeTeachingList)){
				return promptFlt(map, "请先去维护年级课程开设");
			}
			Set<String> subjectIds = EntityUtils.getSet(gradeTeachingList, e->e.getSubjectId());
			List<Course> allCourseList=courseService.findListByIdIn(subjectIds.toArray(new String[]{}));
			if(CollectionUtils.isEmpty(allCourseList)){
				return promptFlt(map, "请先去维护年级课程开设");
			}
			List<Course> courseList=new ArrayList<>();
			//过滤学段
			List<Course> relaCourseList =new ArrayList<>();
			List<ClassHour> allHourList=new ArrayList<>();
			if(TeachClass.CLASS_TYPE_REQUIRED.equals(teachClass.getClassType())) {
				//获取虚拟课程
				Grade grade = gradeService.findOne(teachClass.getGradeId());
				if(grade!=null) {
					 List<ClassHour> classHourList = classHourService.findListByUnitId(teachClass.getAcadyear(), teachClass.getSemester(), unitId, grade.getId(), false);
					 if(CollectionUtils.isNotEmpty(classHourList)) {
						 Set<String> vCourseIds = EntityUtils.getSet(classHourList, e->e.getSubjectId());
						 relaCourseList =courseService.findListByIdIn(vCourseIds.toArray(new String[0]));
						 allHourList=classHourService.makeClassNames(classHourList);
					 }	
				}
			}
			for(Course c:allCourseList) {
				if(BaseConstants.VIRTUAL_COURSE_TYPE.equals(c.getCourseTypeId())) {
					//虚拟课程
					continue;
				}else {
					courseList.add(c);
				}
			}
			
			if(CollectionUtils.isEmpty(courseList)){
				return promptFlt(map, "请先去维护年级课程开设");
			}
			map.put("relaCourseList", relaCourseList);
			map.put("courseList", courseList);
			map.put("teachClass", teachClass);
			map.put("allHourList", allHourList);
			return "/basedata/teachClass/teachclassAdd.ftl";
		}
		
	}
		
	private void makeTimeStr(List<TeachClassEx> exList){
		if(CollectionUtils.isNotEmpty(exList)){
			Set<String> placeId=new HashSet<String>();
			for(TeachClassEx ex:exList){
				Map<String, String> periodIntegervalMap = BaseConstants.PERIOD_INTERVAL_Map;
				Map<String, String> dayOfWeekMap = BaseConstants.dayOfWeekMap;
				String timeTr="";
				timeTr=timeTr+dayOfWeekMap.get(String.valueOf(ex.getDayOfWeek()));
				timeTr=timeTr+periodIntegervalMap.get(ex.getPeriodInterval());
				timeTr=timeTr+"第"+ex.getPeriod()+"节";
				ex.setTimeStr(timeTr);
				if(StringUtils.isNotBlank(ex.getPlaceId())){
					placeId.add(ex.getPlaceId());
				}
				
			}
			if(placeId.size()>0){
				Map<String, TeachPlace> pMap = teachPlaceService.findMapByIdIn(placeId.toArray(new String[]{}));
				for(TeachClassEx ex:exList){
					if(pMap.containsKey(ex.getPlaceId())){
						ex.setPlaceName(pMap.get(ex.getPlaceId()).getPlaceName());
					}
				}
			}
		}
	}
	
	public boolean makeTimeByGrade(String[] gradeIds,ModelMap map){
		//如果总节数为0  默认上下午4节
		List<Grade> gradeList = gradeService.findListByIds(gradeIds);
		if(CollectionUtils.isEmpty(gradeList) || gradeList.size()!=gradeIds.length){
			//年级有不存在
			return false;
		}
		int am=0;
		int pm=0;
		int nm=0;
		String gradeNames="";
		for(Grade g:gradeList){
			gradeNames=gradeNames+","+g.getGradeName();
			if(g.getAmLessonCount()!=null){
				if(am<g.getAmLessonCount()){
					am=g.getAmLessonCount();
				}
			}
			if(g.getPmLessonCount()!=null){
				if(pm<g.getPmLessonCount()){
					pm=g.getPmLessonCount();
				}
			}
			if(g.getNightLessonCount()!=null){
				if(nm<g.getNightLessonCount()){
					nm=g.getNightLessonCount();
				}
			}
			
		}
		if(StringUtils.isNotBlank(gradeNames)){
			gradeNames=gradeNames.substring(1);
			map.put("gradeNames", gradeNames);
		}
		if(am+pm+nm==0){
			map.put("p_"+BaseConstants.PERIOD_INTERVAL_2,4);
			map.put("p_"+BaseConstants.PERIOD_INTERVAL_3,4);
			map.put("p_"+BaseConstants.PERIOD_INTERVAL_4,0);
		}else{
			map.put("p_"+BaseConstants.PERIOD_INTERVAL_2,am);
			map.put("p_"+BaseConstants.PERIOD_INTERVAL_3,pm);
			map.put("p_"+BaseConstants.PERIOD_INTERVAL_4,nm);
		}
		return true;
	}
	
	
	// 保存或者修改
	@ResponseBody
	@RequestMapping("/saveorupdate")
	public String saveOrUpdate(TeachClassSaveDto saveDto, HttpSession httpSession) {
		TeachClass teachClass = saveDto.getTeachClass();
		if(teachClass==null){
			return error("数据不存在");
		}
		if("0".equals(teachClass.getIsUsing())){
			return success("选择的教学班已经完成教学，不能编辑");
		}
		if(teachClass.getPassMark() !=null && teachClass.getFullMark()!=null) {
			if(teachClass.getPassMark() > teachClass.getFullMark()) {
				return error("及格分不能大于满分值");
			}
		}
		if (teachClassService.findExistsClassName(teachClass.getId(),teachClass.getUnitId(), teachClass.getAcadyear(), teachClass.getSemester(), teachClass.getName())) {
			return error("当前学年学期下教学班名称已经存在，请修改！");
		}
		if(StringUtils.isBlank(teachClass.getUnitId())){
			LoginInfo info = getLoginInfo(httpSession);
			String unitId = info.getUnitId();
			teachClass.setUnitId(unitId);
		}
		
		
		
		boolean isDeleted=false;//是不是需要删除课表数据
		boolean isTimeUpdate=false;//是否需要验证时间 也就是插入课表（新增 true 更新判断教师，上课场地，时间是否改变）
		boolean idDeleteTimeEx=false;
		
		StringBuffer msg=new StringBuffer();
		//int oldPunchCard=0;
//		String oldUsingMerge="0";
		String[] studentIds=null;
		
		if (StringUtils.isNotEmpty(teachClass.getId())) {
			//更新
			TeachClass oldTeachClass = teachClassService.findById(teachClass.getId());
			if(oldTeachClass==null){
				return error("数据不存在");
			}
			if(Constant.IS_TRUE_Str.equals(saveDto.getRelaOpen())){
				//有关联id
				if(!teachClass.getRelaCourseId().equals(oldTeachClass.getRelaCourseId())) {
					isTimeUpdate=true;
				}
				if(!isTimeUpdate || !Objects.equals(teachClass.getPlaceId(), oldTeachClass.getPlaceId())) {
					isTimeUpdate=true;
				}
				if(!isTimeUpdate || !teachClass.getTeacherId().equals(oldTeachClass.getTeacherId())) {
					isTimeUpdate=true;
				}
				if(StringUtils.isBlank(oldTeachClass.getRelaCourseId())) {
					//从无关联--》关联 需要清空给ex
					
				}
			}else {
				if(StringUtils.isNotBlank(oldTeachClass.getRelaCourseId())) {
					isTimeUpdate=true;
				}else {
					if(!Constant.IS_TRUE_Str.equals(teachClass.getIsUsingMerge())){
						
						//老的时间点
						List<TeachClassEx> exList = teachClassExService.findByTeachClassIdIn(new String[] {oldTeachClass.getId()}, false);
						//新时间
						List<TimePlaceDto> dtoList = saveDto.getTimePlaceDtoList();
						isTimeUpdate=compareTimes(exList,dtoList);
					}
					
				}
				if(!isTimeUpdate || !teachClass.getTeacherId().equals(oldTeachClass.getTeacherId())) {
					isTimeUpdate=true;
				}
			}
			if(!isTimeUpdate) {
				//从考勤变不考勤--课表数据也是要修改的
				if(oldTeachClass.getPunchCard()!=teachClass.getPunchCard()) {
					isTimeUpdate=true;
				}
			}
			
			if(!isTimeUpdate) {
				//只做更新数据教学班数据
				oldTeachClass.setFullMark(teachClass.getFullMark());
				oldTeachClass.setPassMark(teachClass.getPassMark());
				oldTeachClass.setCredit(teachClass.getCredit());
				oldTeachClass.setPunchCard(teachClass.getPunchCard());
				oldTeachClass.setIsDeleted(0);
				oldTeachClass.setIsUsing("1");
				oldTeachClass.setIsMerge("0");
				oldTeachClass.setModifyTime(new Date());
				try {
					teachClassService.saveAllEntitys(oldTeachClass);
				} catch (Exception e) {
					e.printStackTrace();
					return error(msg.append("失败！").toString() + e.getMessage());
				}
				return success(msg.append("成功！").toString());
			}
			//需要验证冲突
			
			
			List<TeachClassStu> stuList = teachClassStuService.findByClassIds(new String[]{oldTeachClass.getId()});
			if(CollectionUtils.isNotEmpty(stuList)){
				studentIds=EntityUtils.getSet(stuList, e->e.getStudentId()).toArray(new String[]{});
			}
//			oldUsingMerge = oldTeachClass.getIsUsingMerge();
			//oldPunchCard = oldTeachClass.getPunchCard();
			//只修改教师与考勤信息
//			oldTeachClass.setTeacherId(teachClass.getTeacherId());
			//oldTeachClass.setIsUsingMerge(teachClass.getIsUsingMerge());
//			if(Constant.IS_TRUE_Str.equals(teachClass.getIsUsingMerge())){
//				oldTeachClass.setPunchCard(0);
//			}else {
//				oldTeachClass.setPunchCard(teachClass.getPunchCard());
//			}
			oldTeachClass.setFullMark(teachClass.getFullMark());
			oldTeachClass.setPassMark(teachClass.getPassMark());
			oldTeachClass.setCredit(teachClass.getCredit());
			
			oldTeachClass.setModifyTime(new Date());
			EntityUtils.copyProperties(teachClass, oldTeachClass,true);//更新维护值
			teachClass=oldTeachClass;  //重新替换下
			//更新
			msg.append("更新");
			isDeleted=true;
		} else {
			isTimeUpdate=true;
			//新增
			msg.append("新增");
			if(Constant.IS_TRUE_Str.equals(teachClass.getIsUsingMerge())){
				//用于合并
				teachClass.setPunchCard(0);
			}
			String isUsingMerge = teachClass.getIsUsingMerge();
			if(!StringUtils.isNotEmpty(isUsingMerge)){
				teachClass.setIsUsingMerge("0");
			}
			teachClass.setId(UuidUtils.generateUuid());
			teachClass.setModifyTime(new Date());
			teachClass.setCreationTime(new Date());
		}
		//目前subjectType 类型当作必修/选修
		teachClass.setSubjectType(teachClass.getClassType());
		
		List<String> timeStrList=new ArrayList<String>();
		Map<String,String> placeByTime=new HashMap<String,String>();
		if(Constant.IS_TRUE_Str.equals(saveDto.getRelaOpen())) {
			//关联
			//找到该课程的时间点 +placeId 判断时间冲突
			ClassHour classHour = classHourService.findOne(teachClass.getRelaCourseId());
			if(classHour==null) {
				return error("关联走班课程数据已不存在，请刷新后重新操作！");
			}
			//验证学生是否符合班级权限
			if(studentIds!=null && studentIds.length>0 && StringUtils.isNotBlank(classHour.getClassIds())) {
				//学生信息
				List<Student> stuList = studentService.findListByIds(studentIds);
				for(Student s:stuList) {
					if(s.getIsDeleted()==0 && s.getIsLeaveSchool()==0) {
						//正常学生
						if(!classHour.getClassIds().contains(s.getClassId())) {
							return error("已存在的学生不适合该关联的课程，如学生："+s.getStudentName()+"！");
						}
					}
				}
			}
			List<ClassHourEx> exList = classHourExService.findByClassHourIdIn(new String[] {classHour.getId()});
			if(CollectionUtils.isNotEmpty(exList)) {
				for(ClassHourEx item:exList) {
					String kk=item.getDayOfWeek()+"_"+item.getPeriodInterval()+"_"+item.getPeriod();
					if(StringUtils.isNotBlank(teachClass.getPlaceId())) {
						placeByTime.put(kk, teachClass.getPlaceId());
					}
					timeStrList.add(kk);
				}
			}else {
				//找到同年级的对应的数据 ---只是根据同样hourIdid
				List<TeachClass> list = teachClassService.findBySearch(teachClass.getUnitId(), teachClass.getAcadyear(), teachClass.getSemester(), teachClass.getClassType(), teachClass.getGradeId(), null);
				if(CollectionUtils.isNotEmpty(list)) {
					for(TeachClass t:list) {
						if(t.getId().equals(teachClass.getId())) {
							continue;
						}
						if(Constant.IS_TRUE_Str.equals(t.getIsUsing()) && StringUtils.isNotBlank(t.getRelaCourseId()) && t.getRelaCourseId().equals(teachClass.getRelaCourseId())) {
							if(teachClass.getTeacherId().equals(t.getTeacherId())) {
								return error("该老师在其他关联同样的课程的教学班教学！");
							}
							if(StringUtils.isNotBlank(teachClass.getPlaceId()) && teachClass.getPlaceId().equals(t.getPlaceId())) {
								return error("该场地在其他关联同样的课程的教学班使用！");
							}
						}
					}
				}
				
			
			}
			
		}else {
			teachClass.setRelaCourseId("");
			teachClass.setPlaceId("");
			if(!Constant.IS_TRUE_Str.equals(teachClass.getIsUsingMerge())) {
				//不是用于合并 才取上课时间与场地
				List<TimePlaceDto> dtoList = saveDto.getTimePlaceDtoList();
				if(CollectionUtils.isNotEmpty(dtoList)){
					//上课时间
					for(TimePlaceDto ddto:dtoList){
						if(ddto==null){
							continue;
						}
						if(ddto.getDayOfWeek()==null || ddto.getPeriod()==null || StringUtils.isBlank(ddto.getPeriodInterval())){
							continue;
						}
						String kk=ddto.getDayOfWeek()+"_"+ddto.getPeriodInterval()+"_"+ddto.getPeriod();
						placeByTime.put(kk, ddto.getPlaceId());
						timeStrList.add(kk);
					}
				}
				
				if(teachClass.getPunchCard()==1 && CollectionUtils.isEmpty(timeStrList)) {
					return error("考勤需要维护上课时间场地！");
				}
			}
			
		}
		
		/***
		 * 是否进入课表
		 */
		List<CourseSchedule> insertList=new ArrayList<CourseSchedule>();
		List<TeachClassEx> teachExList=new ArrayList<TeachClassEx>();
		CourseScheduleDto deleteDto=null;
		int weekOfWorktime1=0;
		int weekOfWorktime2=0;
		int dayOfWeek1=0;//开始星期
		int dayOfWeek2=0;//结束星期
		
		if(CollectionUtils.isEmpty(timeStrList)){
			//不需要进课表---直接保存
			//清除本来课表数据
			if(isDeleted){
				//用于组装
				//teachClass.setPunchCard(oldPunchCard);
				Map<String, Object> searchmap = makeDto(teachClass);
				//teachClass.setPunchCard(0);
				if(searchmap.containsKey("error")){
					return error((String)searchmap.get("error"));
				}
				CourseScheduleDto searchDto=null;
				if(searchmap.containsKey("searchDto")){
					searchDto=(CourseScheduleDto)searchmap.get("searchDto");
				}
				if(searchDto!=null){
					deleteDto=searchDto;
				}
				//删除之后数据 
				deleteDto.setWeekOfWorktime2(0);//不需要考虑结尾周次
			}
		}else {
			Map<String, Object> searchmap = makeDto(teachClass);
			if(searchmap.containsKey("error")){
				return error((String)searchmap.get("error"));
			}
			CourseScheduleDto searchDto=null;
			if(searchmap.containsKey("searchDto")){
				searchDto=(CourseScheduleDto)searchmap.get("searchDto");
			}
			if(searchDto!=null){
				weekOfWorktime1=searchDto.getWeekOfWorktime1();
				weekOfWorktime2=searchDto.getWeekOfWorktime2();
				dayOfWeek1=searchDto.getDayOfWeek1();
				dayOfWeek2=searchDto.getDayOfWeek2();
			}
			List<CourseSchedule> courseScheduleList = findByTimes(teachClass, weekOfWorktime1, weekOfWorktime2, timeStrList.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(courseScheduleList)){
				//判断上课时间 或者场地有没有冲突
				for(CourseSchedule c:courseScheduleList){
					if(c.getWeekOfWorktime()==weekOfWorktime1 && c.getDayOfWeek()<dayOfWeek1){
						continue;
					}
					if(c.getWeekOfWorktime()==weekOfWorktime2 && c.getDayOfWeek()>dayOfWeek2){
						continue;
					}
					//场地是不是冲突
					String kk=c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
					String mess=BaseConstants.dayOfWeekMap.get(c.getDayOfWeek()+"")+BaseConstants.PERIOD_INTERVAL_Map.get(c.getPeriodInterval())+"第"+c.getPeriod()+"节";
					if(StringUtils.isNotBlank(c.getPlaceId())){
						if(placeByTime.containsKey(kk) && StringUtils.isNotBlank(placeByTime.get(kk))){
							if(placeByTime.get(kk).equals(c.getPlaceId()) && (!c.getClassId().equals(teachClass.getId()))){
								return error("在"+mess+"，场地冲突");
							}
						}
					}
					
					//教师冲突
					if(CollectionUtils.isNotEmpty(c.getTeacherIds())){
						if(c.getTeacherIds().contains(teachClass.getTeacherId()) && (!c.getClassId().equals(teachClass.getId()))){
							return error("在"+mess+"，教师冲突");
						}
					}
					
				}
			}
			//学生冲突
			if(ArrayUtils.isNotEmpty(studentIds)){
				CourseScheduleDto stuSearchDto=new CourseScheduleDto();
				stuSearchDto=new CourseScheduleDto();
				stuSearchDto.setAcadyear(teachClass.getAcadyear());
				stuSearchDto.setSemester(Integer.parseInt(teachClass.getSemester()));
				stuSearchDto.setDayOfWeek1(dayOfWeek1);
				stuSearchDto.setDayOfWeek2(dayOfWeek2);
				stuSearchDto.setWeekOfWorktime1(weekOfWorktime1);
				stuSearchDto.setWeekOfWorktime2(weekOfWorktime2);
				stuSearchDto.setSchoolId(teachClass.getUnitId());
				String mess=checkStudentTime(stuSearchDto, studentIds, timeStrList, teachClass.getId());
				if(StringUtils.isNotBlank(mess)){
					return error(mess);
				}
			}
			
			
			//没有冲突
			insertList=makeCourseScheduleList(teachClass, weekOfWorktime1, weekOfWorktime2, dayOfWeek1, dayOfWeek2, timeStrList, placeByTime);
			if(StringUtils.isBlank(teachClass.getRelaCourseId())) {
				teachExList=makeTeachExList(teachClass,timeStrList,placeByTime);
			}
			
			if(isDeleted){	
				//删除原来未上课的数据
				deleteDto=new CourseScheduleDto();
				deleteDto.setAcadyear(teachClass.getAcadyear());
				deleteDto.setSemester(Integer.parseInt(teachClass.getSemester()));
				deleteDto.setClassId(teachClass.getId());
				deleteDto.setDayOfWeek1(dayOfWeek1);
				//deleteDto.setDayOfWeek2(dayOfWeek2);
				deleteDto.setWeekOfWorktime1(weekOfWorktime1);
				//deleteDto.setWeekOfWorktime2(weekOfWorktime2);
				deleteDto.setSchoolId(teachClass.getUnitId());
				deleteDto.setWeekOfWorktime2(0);//不需要考虑结尾周次
			}
		}
		
		teachClass.setIsDeleted(0);
		teachClass.setIsUsing("1");
		
		teachClass.setIsMerge("0");
		try {
			teachClassService.saveAllTeachClassAndCourseSchedule(teachClass,teachExList,insertList,deleteDto,null);
		} catch (Exception e) {
			e.printStackTrace();
			return error(msg.append("失败！").toString() + e.getMessage());
		}
		return success(msg.append("成功！").toString());
	}
	
	/**
	 * 判断老数据于新数据有没有差别
	 * @param exList
	 * @param dtoList
	 * @return
	 */
	private boolean compareTimes(List<TeachClassEx> exList, List<TimePlaceDto> dtoList) {
		List<String> timeStrList=new ArrayList<String>();
		Map<String,String> placeByTime=new HashMap<String,String>();
		if(CollectionUtils.isEmpty(dtoList) && CollectionUtils.isNotEmpty(exList)) {
			return true;
		}
		if(CollectionUtils.isEmpty(dtoList)) {
			return true;
		}
		for(TimePlaceDto ddto:dtoList){
			if(ddto==null){
				continue;
			}
			if(ddto.getDayOfWeek()==null || ddto.getPeriod()==null || StringUtils.isBlank(ddto.getPeriodInterval())){
				continue;
			}
			String kk=ddto.getDayOfWeek()+"_"+ddto.getPeriodInterval()+"_"+ddto.getPeriod();
			placeByTime.put(kk, ddto.getPlaceId());
			timeStrList.add(kk);
		}
		if(timeStrList.size()!=exList.size()) {
			return true;
		}
		for(TeachClassEx ex:exList) {
			String kk=ex.getDayOfWeek()+"_"+ex.getPeriodInterval()+"_"+ex.getPeriod();
			if(timeStrList.contains(kk)) {
				if(Objects.equals(ex.getPlaceId(), placeByTime.get(kk))) {
					
				}else {
					return true;
				}
			}else {
				return true;
			}
		}
		return false;
	}

	// 合并班级的保存或者修改
	@ResponseBody
	@RequestMapping("/saveorupdatemerge")
	public String saveOrUpdateMerge(TeachClassSaveDto saveDto, HttpSession httpSession) {
		TeachClass teachClass = saveDto.getTeachClass();
		if(teachClass==null){
			return error("数据不存在");
		}
		if("0".equals(teachClass.getIsUsing())){
			return success("选择的教学班已经完成教学，不能编辑");
		}
		if (teachClassService.findExistsClassName(teachClass.getId(),teachClass.getUnitId(), teachClass.getAcadyear(), teachClass.getSemester(), teachClass.getName())) {
			return error("当前学年学期下教学班名称已经存在，请修改！");
		}
		if(StringUtils.isBlank(teachClass.getUnitId())){
			LoginInfo info = getLoginInfo(httpSession);
			String unitId = info.getUnitId();
			teachClass.setUnitId(unitId);
		}
		boolean isDeleted=false;
		StringBuffer msg=new StringBuffer();
		int oldPunchCard=0;
		String[] studentIds=null;
		String teachClassIds = teachClass.getTeachClassIds();
		String[] teachClassIdArr = teachClassIds.split(",");
		List<TeachClass> teachClassList = teachClassService.findListByIdIn(teachClassIdArr);
		Set<String> gradeIdset = EntityUtils.getSet(teachClassList, "gradeId");
		String oldTeachClassId = "";
		String gradeId = "";
		for (String string : gradeIdset) {
			gradeId = gradeId + "," + string;
		}
		gradeId = gradeId.substring(1);
		if (StringUtils.isNotEmpty(teachClass.getId())) {
			TeachClass oldTeachClass = teachClassService.findById(teachClass.getId());
			if(oldTeachClass==null){
				return error("数据不存在");
			}
			oldTeachClassId = oldTeachClass.getId();
			String[] classIds = null;
			if(Constant.IS_TRUE_Str.equals(teachClass.getIsMerge())){
				List<TeachClass> childTeachClassList = teachClassService.findByParentIds(new String[]{teachClass.getId()});
				Set<String> classIdSet = EntityUtils.getSet(childTeachClassList, "id");
				if(CollectionUtils.isNotEmpty(classIdSet)){
					classIds = classIdSet.toArray(new String[0]);
				}
			}else{
				classIds = new String[]{oldTeachClass.getId()};
			}
			List<TeachClassStu> stuList = teachClassStuService.findByClassIds(classIds);
			if(CollectionUtils.isNotEmpty(stuList)){
				studentIds=EntityUtils.getSet(stuList, "studentId").toArray(new String[]{});
			}
			oldPunchCard = oldTeachClass.getPunchCard();
			EntityUtils.copyProperties(teachClass, oldTeachClass, true);
			teachClass=oldTeachClass;
			//更新
			msg.append("更新");
			isDeleted=true;
		} else {
			msg.append("新增");
			if(Constant.IS_TRUE_Str.equals(teachClass.getIsUsingMerge())){
				teachClass.setPunchCard(0);
			}
			teachClass.setClassType(TeachClass.CLASS_TYPE_ELECTIVE);
			teachClass.setGradeId(gradeId);
			teachClass.setId(UuidUtils.generateUuid());
			teachClass.setIsUsingMerge("0");
			teachClass.setModifyTime(new Date());
			teachClass.setCreationTime(new Date());
		}
		//暂时subjectType与classType 含义一样
		teachClass.setSubjectType(teachClass.getClassType());
		//用于合并的小班 学生查出来  // 学生集合
		//TODO
		Set<String> studtIds = new HashSet<>();
		List<TeachClassStu> stuList = teachClassStuService.findByClassIds(teachClassIdArr);
		for (TeachClassStu teachClassStu : stuList) {
			String sId = teachClassStu.getStudentId();
			if(studtIds.contains(sId)){
				Student student = studentService.findOneBy("id", sId);
				return error(student.getStudentName() + "不能处于多个教学班，请修改");
			}
			studtIds.add(teachClassStu.getStudentId());
		}
		
		List<TimePlaceDto> dtoList = saveDto.getTimePlaceDtoList();
		Map<String,String> placeByTime=new HashMap<String,String>();
		List<String> timeStrList=new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(dtoList)){
			//上课时间
			for(TimePlaceDto ddto:dtoList){
				if(ddto==null){
					continue;
				}
				if(ddto.getDayOfWeek()==null || ddto.getPeriod()==null || StringUtils.isBlank(ddto.getPeriodInterval())){
					continue;
				}
				String kk=ddto.getDayOfWeek()+"_"+ddto.getPeriodInterval()+"_"+ddto.getPeriod();
				placeByTime.put(kk, ddto.getPlaceId());
				timeStrList.add(kk);
			}
		}
		/***
		 * 是否进入课表
		 */
		List<CourseSchedule> insertList=new ArrayList<CourseSchedule>();
		List<TeachClassEx> teachExList=new ArrayList<TeachClassEx>();
		CourseScheduleDto deleteDto=null;
		int weekOfWorktime1=0;
		int weekOfWorktime2=0;
		int dayOfWeek1=0;//开始星期
		int dayOfWeek2=0;//结束星期
		if(teachClass.getPunchCard()==1) {
			if(CollectionUtils.isEmpty(timeStrList)){
				return error("选择考勤，无有效上课时间！");
			}
		}
		if(CollectionUtils.isNotEmpty(timeStrList)){
			
			Map<String, Object> searchmap = makeDto(teachClass);
			if(searchmap.containsKey("error")){
				return error((String)searchmap.get("error"));
			}
			CourseScheduleDto searchDto=null;
			if(searchmap.containsKey("searchDto")){
				searchDto=(CourseScheduleDto)searchmap.get("searchDto");
			}
			if(searchDto!=null){
				weekOfWorktime1=searchDto.getWeekOfWorktime1();
				weekOfWorktime2=searchDto.getWeekOfWorktime2();
				dayOfWeek1=searchDto.getDayOfWeek1();
				dayOfWeek2=searchDto.getDayOfWeek2();
			}
			List<CourseSchedule> courseScheduleList = findByTimes(teachClass, weekOfWorktime1, weekOfWorktime2, timeStrList.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(courseScheduleList)){
				//判断上课时间 或者场地有没有冲突
				for(CourseSchedule c:courseScheduleList){
					if(c.getWeekOfWorktime()==weekOfWorktime1 && c.getDayOfWeek()<dayOfWeek1){
						continue;
					}
					if(c.getWeekOfWorktime()==weekOfWorktime2 && c.getDayOfWeek()>dayOfWeek2){
						continue;
					}
					//场地是不是冲突
					String kk=c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
					String mess=BaseConstants.dayOfWeekMap.get(c.getDayOfWeek()+"")+BaseConstants.PERIOD_INTERVAL_Map.get(c.getPeriodInterval())+"第"+c.getPeriod()+"节";
					if(StringUtils.isNotBlank(c.getPlaceId())){
						if(placeByTime.containsKey(kk) && StringUtils.isNotBlank(placeByTime.get(kk))){
							if(placeByTime.get(kk).equals(c.getPlaceId()) && (!c.getClassId().equals(teachClass.getId()))){
								return error("在"+mess+"，场地冲突");
							}
						}
					}
					
					//教师冲突
					if(CollectionUtils.isNotEmpty(c.getTeacherIds())){
						if(c.getTeacherIds().contains(teachClass.getTeacherId()) && (!c.getClassId().equals(teachClass.getId()))){
							return error("在"+mess+"，教师冲突");
						}
					}
					
				}
			}
			//学生冲突
			if(ArrayUtils.isNotEmpty(studentIds)){
				CourseScheduleDto stuSearchDto=new CourseScheduleDto();
				stuSearchDto=new CourseScheduleDto();
				stuSearchDto.setAcadyear(teachClass.getAcadyear());
				stuSearchDto.setSemester(Integer.parseInt(teachClass.getSemester()));
				stuSearchDto.setDayOfWeek1(dayOfWeek1);
				stuSearchDto.setDayOfWeek2(dayOfWeek2);
				stuSearchDto.setWeekOfWorktime1(weekOfWorktime1);
				stuSearchDto.setWeekOfWorktime2(weekOfWorktime2);
				stuSearchDto.setSchoolId(teachClass.getUnitId());
				String mess=checkStudentTime(stuSearchDto, studentIds, timeStrList, teachClass.getId());
				if(StringUtils.isNotBlank(mess)){
					return error(mess);
				}
			}
			
			//没有冲突
			insertList=makeCourseScheduleList(teachClass, weekOfWorktime1, weekOfWorktime2, dayOfWeek1, dayOfWeek2, timeStrList, placeByTime);
			teachExList=makeTeachExList(teachClass,timeStrList,placeByTime);
			if(isDeleted){	
				//删除原来未上课的数据
				deleteDto=new CourseScheduleDto();
				deleteDto.setAcadyear(teachClass.getAcadyear());
				deleteDto.setSemester(Integer.parseInt(teachClass.getSemester()));
				deleteDto.setClassId(teachClass.getId());
				deleteDto.setDayOfWeek1(dayOfWeek1);
				//deleteDto.setDayOfWeek2(dayOfWeek2);
				deleteDto.setWeekOfWorktime1(weekOfWorktime1);
				//deleteDto.setWeekOfWorktime2(weekOfWorktime2);
				deleteDto.setWeekOfWorktime2(0);
				deleteDto.setSchoolId(teachClass.getUnitId());
			}
			
		}else{
			//if(oldPunchCard==1){
				if(isDeleted){
					//用于组装
					teachClass.setPunchCard(oldPunchCard);
					Map<String, Object> searchmap = makeDto(teachClass);
					teachClass.setPunchCard(0);
					if(searchmap.containsKey("error")){
						return error((String)searchmap.get("error"));
					}
					CourseScheduleDto searchDto=null;
					if(searchmap.containsKey("searchDto")){
						searchDto=(CourseScheduleDto)searchmap.get("searchDto");
					}
					if(searchDto!=null){
						deleteDto=searchDto;
					}
					deleteDto.setWeekOfWorktime2(0);
				}
			//}
		}
		
		teachClass.setIsDeleted(0);
		teachClass.setIsUsing("1");
		teachClass.setIsMerge("1");
		String gradId = teachClass.getGradeId();
		String[] split = gradId.split(",");
		Set<String> id = new HashSet<String>();
		for(String st : split) {
			id.add(st);
		}
		String graId = "";
		for (String string : id) {
			graId = graId + "," + string;
		}
		if(StringUtils.isNotEmpty(graId)){
			graId = graId.substring(1);
		}
		teachClass.setGradeId(graId);
		try {
			teachClassService.saveAllTeachClassAndCourseSchedule(teachClass,teachExList,insertList,deleteDto,oldTeachClassId);
		} catch (Exception e) {
			e.printStackTrace();
			return error(msg.append("失败！").toString() + e.getMessage());
		}
		return success(msg.append("成功！").toString());
	}
	
	private List<TeachClassEx> makeTeachExList(TeachClass teachClass,
			List<String> timeStrList, Map<String, String> placeByTime) {
		List<TeachClassEx> exList=new ArrayList<TeachClassEx>();
		TeachClassEx ex=null;
		for(String s:timeStrList){
			String[] tt = s.split("_");
			ex=new TeachClassEx();
			ex.setId(UuidUtils.generateUuid());
			ex.setPeriod(Integer.parseInt(tt[2]));
			if(placeByTime.containsKey(s) && StringUtils.isNotBlank(placeByTime.get(s))){
				ex.setPlaceId(placeByTime.get(s));
			}
			ex.setTeachClassId(teachClass.getId());
			ex.setPeriodInterval(tt[1]);
			ex.setDayOfWeek(Integer.parseInt(tt[0]));
			exList.add(ex);
		}
		return exList;
	}

	private List<CourseSchedule> makeCourseScheduleList(TeachClass teachClass,int weekOfWorktime1,int weekOfWorktime2,
			int dayOfWeek1,int dayOfWeek2,List<String> timeStrList,Map<String,String> placeByTime){
		List<CourseSchedule> insertList=new ArrayList<CourseSchedule>();
		CourseSchedule insertCourse=null;
		for(String s:timeStrList){
			String[] tt = s.split("_");
			for(int i=weekOfWorktime1;i<=weekOfWorktime2;i++){
				//每一周
				if(i==weekOfWorktime1){
					if(Integer.parseInt(tt[0])<dayOfWeek1){
						continue;
					}
				}else if(i==weekOfWorktime2){
					if(Integer.parseInt(tt[0])>dayOfWeek2){
						continue;
					}
					
				}
				insertCourse=makeOne(teachClass);
				insertCourse.setWeekOfWorktime(i);
				insertCourse.setDayOfWeek(Integer.parseInt(tt[0]));
				insertCourse.setPeriodInterval(tt[1]);
				insertCourse.setPeriod(Integer.parseInt(tt[2]));
				if(placeByTime.containsKey(s) && StringUtils.isNotBlank(placeByTime.get(s))){
					insertCourse.setPlaceId(placeByTime.get(s));
				}
				insertList.add(insertCourse);
			}
		}
		return insertList;
	}
	
	private CourseSchedule makeOne(TeachClass teachClass){
		CourseSchedule insertCourse=new CourseSchedule();
		insertCourse.setId(UuidUtils.generateUuid());
		insertCourse.setClassId(teachClass.getId());
		insertCourse.setClassType(CourseSchedule.CLASS_TYPE_TEACH);
		insertCourse.setClassName(teachClass.getName());
		insertCourse.setAcadyear(teachClass.getAcadyear());
		insertCourse.setPunchCard(teachClass.getPunchCard());
		insertCourse.setSchoolId(teachClass.getUnitId());
		insertCourse.setSemester(Integer.parseInt(teachClass.getSemester()));
		insertCourse.setSubjectId(teachClass.getCourseId());
		insertCourse.setTeacherId(teachClass.getTeacherId());
		insertCourse.setWeekType(3);
		if(StringUtils.isNotBlank(teachClass.getRelaCourseId())) {
			//关联走班课程的教学班--类型为虚拟
			insertCourse.setSubjectType(BaseConstants.SUBJECT_TYPE_VIRTUAL);
		}else{
			insertCourse.setSubjectType(teachClass.getSubjectType());
		}
		
		return insertCourse;
	}
	
	private List<CourseSchedule> findByTimes(TeachClass teachClass,int weekOfWorktime1,int weekOfWorktime2,String[] timeStr){
		CourseScheduleDto dto=new CourseScheduleDto();
		dto.setSchoolId(teachClass.getUnitId());
		dto.setAcadyear(teachClass.getAcadyear());
		dto.setSemester(Integer.parseInt(teachClass.getSemester()));
		dto.setWeekOfWorktime1(weekOfWorktime1);
		dto.setWeekOfWorktime2(weekOfWorktime2);
		List<CourseSchedule> list=courseScheduleService.findByTimes(dto,timeStr);
		courseScheduleService.makeTeacherSet(list);
		return list;
		
	}
	/**
	 * 获取课程表查询字段 周次时间段 星期段   如果节假日有报错 返回key:error  成功 返回：searchDto
	 * @param oldTeachClass
	 * 结合当前时间
	 * @return
	 */
	private Map<String,Object> makeDto(TeachClass oldTeachClass){
		Map<String,Object> objMap=new HashMap<String,Object>();
		CourseScheduleDto searchDto =null;
		
		Semester chooseSemest=semesterService.findByAcadyearAndSemester(oldTeachClass.getAcadyear(),Integer.parseInt(oldTeachClass.getSemester()),oldTeachClass.getUnitId());
		Date nowDate = new Date();
		if(DateUtils.compareForDay(nowDate, chooseSemest.getSemesterEnd())>0){
			//学年学期已经结束  不用删除  无需进出课表
			return objMap;
		}else{
			DateInfo endDateInfo = dateInfoService.getDate(getLoginInfo().getUnitId(), oldTeachClass.getAcadyear(), Integer.valueOf(oldTeachClass.getSemester()), chooseSemest.getSemesterEnd());
			if(endDateInfo == null){
				objMap.put("error", "保存失败,未维护节假日信息或者上课开始时间不在当前选择的学年学期内！");
				return objMap;
			}
			int weekOfWorktime1=0;
			int weekOfWorktime2=endDateInfo.getWeek();
			
			int dayOfWeek1=0;//开始星期
			int dayOfWeek2=endDateInfo.getWeekday()-1;//结束星期
			if(DateUtils.compareForDay(nowDate, chooseSemest.getSemesterBegin())<0){
				//学期未开始
				weekOfWorktime1=0;
				DateInfo startDateInfo = dateInfoService.getDate(getLoginInfo().getUnitId(), oldTeachClass.getAcadyear(), Integer.valueOf(oldTeachClass.getSemester()), chooseSemest.getSemesterBegin());
				if(startDateInfo == null){
					objMap.put("error", "保存失败,未维护当前选择的学年学期内的节假日信息！");
					return objMap;
				}
				dayOfWeek1=startDateInfo.getWeekday()-1;
			}else{
				DateInfo nowDateInfo = dateInfoService.getDate(getLoginInfo().getUnitId(), oldTeachClass.getAcadyear(), Integer.valueOf(oldTeachClass.getSemester()), nowDate);
				if(nowDateInfo == null){
					objMap.put("error", "保存失败,未维护当前选择的学年学期内的节假日信息！");
					return objMap;
				}
				weekOfWorktime1=nowDateInfo.getWeek();
				dayOfWeek1=nowDateInfo.getWeekday();
			}
			//删除原来未上课的数据
			searchDto=new CourseScheduleDto();
			searchDto.setAcadyear(oldTeachClass.getAcadyear());
			searchDto.setSemester(Integer.parseInt(oldTeachClass.getSemester()));
			searchDto.setClassId(oldTeachClass.getId());
			searchDto.setDayOfWeek1(dayOfWeek1);
			searchDto.setDayOfWeek2(dayOfWeek2);
			searchDto.setWeekOfWorktime1(weekOfWorktime1);
			searchDto.setWeekOfWorktime2(weekOfWorktime2);
			searchDto.setSchoolId(oldTeachClass.getUnitId());
		}
		
		objMap.put("searchDto", searchDto);
		return objMap;
	}
	
	@ResponseBody
	@RequestMapping("/updateUsing")
	@ControllerInfo("完成教学")
	public String updateUsing(String id, String using) {
		TeachClass oldTeachClass = teachClassService.findById(id);
		if(oldTeachClass==null || oldTeachClass.getIsDeleted()==1){
			return error("数据不存在或者已经删除");
		}
		CourseScheduleDto delDto =null;
		if("0".equals(using)){
			//完成 删除当前及以后
			if(using.equals(oldTeachClass.getIsUsing())){
				return success("已经是完成状态，无需保存");
			}
			oldTeachClass.setIsUsing(using);
			oldTeachClass.setModifyTime(new Date());
			//if(oldTeachClass.getPunchCard()==1){
				//考勤
				Map<String, Object> map = makeDto(oldTeachClass);
				if(map.containsKey("error")){
					return error((String)map.get("error"));
				}
				Object searchDto = map.get("searchDto");
				if(searchDto!=null){
					delDto=(CourseScheduleDto)searchDto;
				}
			//}
			try{
				if(Constant.IS_TRUE_Str.equals(oldTeachClass.getIsMerge())){
					List<TeachClass> childTeachClassList = teachClassService.findByParentIds(new String[]{oldTeachClass.getId()});
					for (TeachClass teachClass : childTeachClassList) {
						teachClass.setIsUsing("0");
						teachClass.setModifyTime(new Date());
					}
					teachClassService.saveAll(childTeachClassList.toArray(new TeachClass[0]));
				}
				teachClassService.updateIsUsing(oldTeachClass,delDto);
			}catch(Exception e){
				e.printStackTrace();
				return error("操作失败！"+e.getMessage());
			}
		}else if("1".equals(using)){
			//暂时用不到这边数据---因为一旦完成，不能再次切换到未完成
			//新增当前及以后课表
//			if(using.equals(oldTeachClass.getIsUsing())){
//				return success("目前本身为未完成状态，无需保存");
//			}
//			oldTeachClass.setIsUsing(using);
//			oldTeachClass.setModifyTime(new Date());
//			List<CourseSchedule> insertCourseScheduleList=new ArrayList<CourseSchedule>();
//			if(oldTeachClass.getPunchCard()==1){
//				//考勤
//				Map<String, Object> map = makeDto(oldTeachClass);
//				if(map.containsKey("error")){
//					return error((String)map.get("error"));
//				}
//				Object searchDto = map.get("searchDto");
//				if(searchDto!=null){
//					delDto=(CourseScheduleDto)searchDto;
//					//新增当前及以后课表
//					List<TeachClassEx> exList = teachClassExService.findByTeachClassId(oldTeachClass.getId());
//					if(CollectionUtils.isNotEmpty(exList)){
//						List<String> timeStrList = new ArrayList<String>();
//						Map<String,String> placeByTime=new HashMap<String,String>();
//						for(TeachClassEx ee:exList){
//							String s=ee.getDayOfWeek()+"_"+ee.getPeriodInterval()+"_"+ee.getPeriod();
//							placeByTime.put(s, ee.getPlaceId());
//						}
//						//判断时间是否冲突
//						List<CourseSchedule> courseScheduleList = findByTimes(oldTeachClass, delDto.getWeekOfWorktime1(), delDto.getWeekOfWorktime2(), timeStrList.toArray(new String[]{}));
//						if(CollectionUtils.isNotEmpty(courseScheduleList)){
//							//判断上课时间 或者场地有没有冲突
//							for(CourseSchedule c:courseScheduleList){
//								if(c.getWeekOfWorktime()==delDto.getWeekOfWorktime1() && c.getDayOfWeek()<delDto.getDayOfWeek1()){
//									continue;
//								}
//								if(c.getWeekOfWorktime()==delDto.getWeekOfWorktime2() && c.getDayOfWeek()>delDto.getDayOfWeek2()){
//									continue;
//								}
//								//场地是不是冲突
//								String kk=c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
//								String mess=BaseConstants.dayOfWeekMap.get(c.getDayOfWeek()+"")+BaseConstants.PERIOD_INTERVAL_Map.get(c.getPeriodInterval())+"第"+c.getPeriod()+"节";
//								if(StringUtils.isNotBlank(c.getPlaceId())){
//									if(placeByTime.containsKey(kk) && StringUtils.isNotBlank(placeByTime.get(kk))){
//										if(placeByTime.get(kk).equals(c.getPlaceId()) && (!c.getClassId().equals(oldTeachClass.getId()))){
//											return error("在"+mess+"，场地冲突");
//										}
//									}
//								}
//								
//								//教师冲突
//								if(CollectionUtils.isNotEmpty(c.getTeacherIds())){
//									if(c.getTeacherIds().contains(oldTeachClass.getTeacherId()) && (!c.getClassId().equals(oldTeachClass.getId()))){
//										return error("在"+mess+"，教师冲突");
//									}
//								}
//								
//							}
//						}
//						insertCourseScheduleList = makeCourseScheduleList(oldTeachClass, delDto.getWeekOfWorktime1(), delDto.getWeekOfWorktime2(), delDto.getDayOfWeek1(), delDto.getDayOfWeek2(), timeStrList, placeByTime);
//					}
//				}
//			}
//			try{
//				teachClassService.saveAllTeachClassAndCourseSchedule(oldTeachClass, null, insertCourseScheduleList, null,null);
//			}catch(Exception e){
//				e.printStackTrace();
//				return error("操作失败！"+e.getMessage());
//			}
		}else{
			return error("参数丢失，请重新操作！");
		}
		return success("操作成功！");
	}
	
	
	@ResponseBody
	@RequestMapping("/deleteAllStudent")
	@ControllerInfo("清空")
	public String deleteAllStudent(String id) {
		try{
			//强删
			teachClassStuService.deleteByClassId(id);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return success("操作成功！");
	}
	
	@ResponseBody
	@RequestMapping("/saveSomeStudent")
	@ControllerInfo("保存学生")
	public String saveSomeStudent(String id,String studentIds) {
		if(StringUtils.isBlank(studentIds)){
			return error("没有选择学生！");
		}
		String[] studentIdArr = studentIds.split(",");
		//id 为 teachClass 的 Id
		TeachClass oldTeachClass = teachClassService.findById(id);
		if(oldTeachClass==null || oldTeachClass.getIsDeleted()==1){
			return error("数据不存在或者已经删除");
		}
		if(oldTeachClass.getIsUsing().equals("0")){
			//已经完成
			return error("该教学班已经完成教学，不能新增学生");
		}
		
		
		List<String> sameStudentId=new ArrayList<String>();//时间冲突
		List<String> timeStrList=new ArrayList<String>();
		
		
		//根据教学班面向对象限制  拿到可进入该教学班的行政班 
		Set<String> allclassId = new HashSet<String>();
		String gradeIds = oldTeachClass.getGradeId();
		String[] gradeIdArr=null;
		if(getLimit()){
			//选修和必修都需要能够跨年级
			List<Grade> gradeList = gradeService.findByUnitId(oldTeachClass.getUnitId());
			gradeIdArr = EntityUtils.getList(gradeList,e->e.getId()).toArray(new String[0]);
		}else{
			if(gradeIds.indexOf(",")>=0){
				gradeIdArr=gradeIds.split(",");
			}else{
				gradeIdArr=new String[]{gradeIds};
			}
		}
		List<Clazz> clazzlist = classService.findByGradeIdIn(gradeIdArr);
		if(CollectionUtils.isEmpty(clazzlist)){
			return error("对应的年级下没有班级");
		}
		//能进去该科目的行政班班级
		allclassId = EntityUtils.getSet(clazzlist, s->s.getId());

//		boolean isRela=false;//走关联课程
		//if(oldTeachClass.getPunchCard()==1 ){
		if(StringUtils.isBlank(oldTeachClass.getRelaCourseId())) {
			List<TeachClass> tList=new ArrayList<TeachClass>();
			tList.add(oldTeachClass);
			
			teachClassService.makeTimePlace(tList,false);
			
			
			if(CollectionUtils.isNotEmpty(oldTeachClass.getExList())){
				
				if(CollectionUtils.isNotEmpty(oldTeachClass.getExList())){
					//上课时间
					for(TeachClassEx ddto:oldTeachClass.getExList()){
						String kk=ddto.getDayOfWeek()+"_"+ddto.getPeriodInterval()+"_"+ddto.getPeriod();
						timeStrList.add(kk);
					}
				}
				
			}
			
		}else {
//			isRela=true;
			//找到该课程的时间点 +placeId 判断时间冲突
			ClassHour hour = classHourService.findOne(oldTeachClass.getRelaCourseId());
			if(hour!=null) {
				if(StringUtils.isNotBlank(hour.getClassIds())) {
					Set<String> s1=new HashSet<>();
					for(String s:allclassId) {
						if(hour.getClassIds().contains(s)) {
							s1.add(s);
						}
					}
					if(CollectionUtils.isEmpty(s1)) {
						return error("根据关联课程要求，未找到符合条件的行政班数据");
					}
					allclassId=s1;
				}
				List<ClassHourEx> exhourlist = classHourExService.findByClassHourIdIn(new String[] {hour.getId()});
				if(CollectionUtils.isNotEmpty(exhourlist)) {
					for(ClassHourEx item:exhourlist ) {
						String kk=item.getDayOfWeek()+"_"+item.getPeriodInterval()+"_"+item.getPeriod();
						timeStrList.add(kk);
					}
				}
			}else {
				return error("该教学班数据有误，关联的课程数据不存在");
			}
			
		}
		//新添加的验证开设科目 已经存在的暂时不修改
		if(CollectionUtils.isNotEmpty(timeStrList)) {
			Map<String, Object> searchmap = makeDto(oldTeachClass);
			if(searchmap.containsKey("error")){
				return error((String)searchmap.get("error"));
			}
			CourseScheduleDto searchDto=null;
			if(searchmap.containsKey("searchDto")){
				searchDto=(CourseScheduleDto)searchmap.get("searchDto");
			}
			if(searchDto!=null){
				CourseScheduleDto stuSearchDto=new CourseScheduleDto();
				stuSearchDto=new CourseScheduleDto();
				stuSearchDto.setAcadyear(oldTeachClass.getAcadyear());
				stuSearchDto.setSemester(Integer.parseInt(oldTeachClass.getSemester()));
				stuSearchDto.setSchoolId(oldTeachClass.getUnitId());
				stuSearchDto.setWeekOfWorktime1(searchDto.getWeekOfWorktime1());
				stuSearchDto.setWeekOfWorktime2(searchDto.getWeekOfWorktime2());
				stuSearchDto.setDayOfWeek1(searchDto.getDayOfWeek1());
				stuSearchDto.setDayOfWeek2(searchDto.getDayOfWeek2());
				sameStudentId = findSameTimeStudentIds(stuSearchDto, studentIdArr, timeStrList, oldTeachClass.getId());
			}
		}
		
//		Set<String> relaClassIds=new HashSet<>();//存在该关联科目走班的
//		if(isRela) {
//			//该班级有没有关联课程的课程时间点--行政班科目
//			//根据学年学期 必修/选修 班级课程计划 
//			OpenTeachingSearchDto classTeachDto=new OpenTeachingSearchDto();
//			classTeachDto.setAcadyear(oldTeachClass.getAcadyear());
//			classTeachDto.setSemester(oldTeachClass.getSemester());
//			classTeachDto.setSubjectId(oldTeachClass.getRelaCourseId());
//			classTeachDto.setUnitId(oldTeachClass.getUnitId());
//			classTeachDto.setIsTeaCls(0);
//			classTeachDto.setIsDeleted(0);
//			classTeachDto.setClassIds(allclassId.toArray(new String[]{}));
//			List<ClassTeaching> classTeachingList =classTeachingService.findBySearch(classTeachDto, null);
//			if(CollectionUtils.isNotEmpty(classTeachingList)){
//				relaClassIds = EntityUtils.getSet(classTeachingList, e->e.getClassId());
//			}
//		}
		
		
		int chooseNum=0;//已经存在该教学班
		int deleteNum=0;//选择中学生删除的数量

		int noLimit=0;//没有该课程权限
		int nowInOther=0;//已经在其他班级啦
		int nowTimeOther=0;//时间冲突
		int noLimitRela=0;//学生出现在多个同样关联走班课程的班级
		Map<String, Student> studentMap = studentService.findMapByIdIn(studentIdArr);
		//根据学年学期 必修/选修 班级课程计划 
		OpenTeachingSearchDto classTeachDto=new OpenTeachingSearchDto();
		classTeachDto.setAcadyear(oldTeachClass.getAcadyear());
		classTeachDto.setSemester(oldTeachClass.getSemester());
		classTeachDto.setSubjectId(oldTeachClass.getCourseId());
		classTeachDto.setUnitId(oldTeachClass.getUnitId());
		classTeachDto.setIsTeaCls(1);
		classTeachDto.setIsDeleted(0);
		classTeachDto.setClassIds(allclassId.toArray(new String[]{}));
		List<ClassTeaching> classTeachingList =classTeachingService.findBySearch(classTeachDto, null);
		Set<String> classIds =new HashSet<String>();//存在该科目走班的
		if(CollectionUtils.isNotEmpty(classTeachingList)){
			classIds = EntityUtils.getSet(classTeachingList, e->e.getClassId());
		}
		
		
		Set<String> tIds =new HashSet<>();//--同科目教学班
		Set<String> tSameRelIds=new HashSet<>();//--同批次点教学班
		List<TeachClass> teachList=new ArrayList<>();
		boolean isRela=false;
		if(StringUtils.isNotBlank(oldTeachClass.getRelaCourseId()) ){
			 isRela=true;
			 teachList=teachClassService.findByAcadyearAndSemesterAndUnitId(oldTeachClass.getAcadyear(), oldTeachClass.getSemester(),oldTeachClass.getUnitId());
		}else {
			 teachList=teachClassService.findTeachClassList(oldTeachClass.getUnitId(), oldTeachClass.getAcadyear(), oldTeachClass.getSemester(), oldTeachClass.getCourseId());
		}
		for(TeachClass t:teachList) {
			if(t.getCourseId().equals(oldTeachClass.getCourseId())) {
				tIds.add(t.getId());
			}
			if(isRela && StringUtils.isNotBlank(t.getRelaCourseId()) && t.getRelaCourseId().equals(oldTeachClass.getRelaCourseId()) && t.getGradeId().equals(oldTeachClass.getGradeId())) {
				tSameRelIds.add(t.getId());
			}
		}
		if(isRela) {
			tSameRelIds.remove(id);
		}
		tIds.remove(id);
		
		Set<String> allClassIds=new HashSet<>();
		allClassIds.add(id);
		if(CollectionUtils.isNotEmpty(tSameRelIds)) {
			allClassIds.addAll(tSameRelIds);
		}
		boolean isUsingMerg=false;
		if(!getLimit() || Constant.IS_TRUE_Str.equals(oldTeachClass.getIsUsingMerge())){
			if(CollectionUtils.isNotEmpty(tIds)) {
				isUsingMerg=true;
				allClassIds.addAll(tIds);
			}
		}
		
		List<TeachClassStu> teachStuList = teachClassStuService.findByClassIds(allClassIds.toArray(new String[] {}));
		Set<String> oldStudentId=new HashSet<String>();//原来的id
		//学生允许排进同个课程的多个教学班
		Set<String> otherStudentId=new HashSet<String>();
		Set<String> sameBathStudentId=new HashSet<>();//存在其他关联的走班课程的
		if(CollectionUtils.isNotEmpty(teachStuList)){
			for(TeachClassStu s:teachStuList) {
				if(id.equals(s.getClassId())) {
					oldStudentId.add(s.getStudentId());
				}
				if(isUsingMerg) {
					if(tIds.contains(s.getClassId())) {
						otherStudentId.add(s.getStudentId());
					}
				}
				if(isRela) {
					if(tSameRelIds.contains(s.getClassId())) {
						sameBathStudentId.add(s.getStudentId());
					}
				}
			}
		}
		
		
	
		List<TeachClassStu> insertTeachStuList=new ArrayList<TeachClassStu>();
		TeachClassStu teachStu=null;
		for(String s:studentIdArr){
			if(oldStudentId.contains(s)){
				chooseNum++;
				continue;
			}
			if(!studentMap.containsKey(s)){
				deleteNum++;
				continue;
			}
			if(isRela && sameBathStudentId.contains(s)) {
				noLimitRela++;
				continue;
			}
//			if(isRela && !relaClassIds.contains(studentMap.get(s).getClassId()))  {
//				noLimitRela++;
//				continue;
//			}
			if(!classIds.contains(studentMap.get(s).getClassId())){
				noLimit++;
				continue;
			}
			if(otherStudentId.contains(s)){
				nowInOther++;
				continue;
			}
			if(sameStudentId.contains(s)){
				nowTimeOther++;
				continue;
			}
			teachStu=new TeachClassStu();
			teachStu.setId(UuidUtils.generateUuid());
			teachStu.setClassId(id);
			teachStu.setCreationTime(new Date());
			teachStu.setIsDeleted(0);
			teachStu.setModifyTime(new Date());
			teachStu.setStudentId(s);
			insertTeachStuList.add(teachStu);
		}
		//消息
		String msg="";
		if(chooseNum>0){
			msg=msg+"，或者"+"部分学生已经在该班级中";
		}
		if(deleteNum>0){
			msg=msg+"，或者"+"部分学生已经不存在";
		}
		if(noLimit>0){
			msg=msg+"，或者"+"部分学生所在行政班没有开设对应走班科目或学生所在行政班不在该教学班的对象中";
		}
		if(noLimitRela>0) {
			msg=msg+"，或者"+"部分学生已经在其他关联该走班课程的教学班内";
		}
		if(nowInOther>0){
			if(!getLimit()){
				msg=msg+"，或者"+"部分学生已经存在于其他同课程的教学班";
			}else{
				msg=msg+"，或者"+"部分学生已经存在于其他同课程的用于合并的教学班";
			}
		}
		if(nowTimeOther>0){
			msg=msg+"，或者"+"部分学生上课时间冲突";
		}
		if(StringUtils.isNotBlank(msg)){
			msg=msg.substring(3);
		}
		
		
		try{
			if(CollectionUtils.isEmpty(insertTeachStuList)){
				if(StringUtils.isNotBlank(msg)){
					return error("新增失败！由于"+msg);
				}else{
					return error("新增失败");
				}
			}else{
				teachClassStuService.saveAll(insertTeachStuList.toArray(new TeachClassStu[]{}));
				if(StringUtils.isNotBlank(msg)){
					return success("成功新增"+insertTeachStuList.size()+"条，部分由于"+msg+"失败");
				}else{
					return success("成功新增"+insertTeachStuList.size()+"条");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
	}
	
	// 删除
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(String id,String isAll) {
			//isAll  如果1 删除课表把所有信息  0:删除当前及以后
		try {
			if (StringUtils.isEmpty(id)) {
				return error("id为空无法删除!");
			}
			TeachClass teachClass=teachClassService.findById(id);
			if(teachClass==null || teachClass.getIsDeleted()==1){
				return error("教学班已不存在!");
			}
			if(Constant.IS_TRUE_Str.equals(teachClass.getIsUsingMerge())&&StringUtils.isNotBlank(teachClass.getParentId())){
				return error("该教学班已合并为新教学班，不能删除！");
			}
			List<TeachClassStu> list = teachClassStuService.findByClassIds(new String[]{id});
			if(CollectionUtils.isNotEmpty(list)){
				return error("该教学班下有学生，不能删除!");
			}
//			if(isAll!=null && teachClass.getPunchCard()==0){
//				return error("教学班数据有调整，请刷新后操作!");
//			}
//			if(isAll==null && teachClass.getPunchCard()==1){
//				return error("教学班数据有调整，请刷新后操作!");
//			}
			CourseScheduleDto dto=null;
			if(isAll!=null){
				dto=new CourseScheduleDto();
				dto.setSchoolId(teachClass.getUnitId());
				dto.setAcadyear(teachClass.getAcadyear());
				dto.setSemester(Integer.parseInt(teachClass.getSemester()));
				dto.setClassId(teachClass.getId());
				int weekOfWorktime1=0;
				int weekOfWorktime2=0;
				
				int dayOfWeek1=0;//开始星期
				int dayOfWeek2=0;//结束星期
				//删除数据 不需要考虑到学期结束，只要该学年学期维护的课表结尾
				if("0".equals(isAll)){
					//当前及以后
					Map<String, Object> searchmap = makeDto(teachClass);
					if(searchmap.containsKey("error")){
						return error((String)searchmap.get("error"));
					}
					CourseScheduleDto searchDto=null;
					if(searchmap.containsKey("searchDto")){
						searchDto=(CourseScheduleDto)searchmap.get("searchDto");
					}
					if(searchDto!=null){
						weekOfWorktime1=searchDto.getWeekOfWorktime1();
						//weekOfWorktime2=searchDto.getWeekOfWorktime2();
						dayOfWeek1=searchDto.getDayOfWeek1();
						//dayOfWeek2=searchDto.getDayOfWeek2();
						dto.setWeekOfWorktime1(weekOfWorktime1);
						dto.setWeekOfWorktime2(weekOfWorktime2);
						dto.setDayOfWeek1(dayOfWeek1);
						//dto.setDayOfWeek2(dayOfWeek2);
					}else{
						dto=null;
					}
				}else{
					dto.setWeekOfWorktime1(weekOfWorktime1);
					dto.setWeekOfWorktime2(weekOfWorktime2);
					//dto.setDayOfWeek1(dayOfWeek1);
					//dto.setDayOfWeek2(dayOfWeek2);
				}
				
			}
			teachClassService.deleteClassAndScheduleById(id,dto);
			if(Constant.IS_TRUE_Str.equals(teachClass.getIsMerge())){
				List<TeachClass> childTeachClassList = teachClassService.findByParentIds(new String[]{teachClass.getId()});
				for (TeachClass childTeachClass : childTeachClassList) {
					childTeachClass.setParentId("");
				}
				teachClassService.saveAll(childTeachClassList.toArray(new TeachClass[0]));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！" + e.getMessage());
		}

		return success("删除成功！");
	}
	// 初始页
	@RequestMapping("/importhead/page")
	@ControllerInfo("进入教学班导入首页")
	public String showImportHead(ModelMap map,String type,String showTabType) {
		if("1".equals(type)){
			//教学班导入
		}else if("2".equals(type)){
			//教学班学生导入
		}else{
			return errorFtl(map, "参数不对，刷新后操作");
		}
		map.put("showTabType", showTabType);
		map.put("type", type);
		List<String> acadyearList = semesterService.findAcadeyearList();
		if(CollectionUtils.isEmpty(acadyearList)){
			return promptFlt(map, "请先设置学年学期");
		}
		map.put("acadyearList", acadyearList);
		//取学校当前学年学期
		Semester sem = semesterService.findCurrentSemester(2,getLoginInfo().getUnitId());
		String acadyear="";
		String semester="";
		if(sem!=null ){
			acadyear=sem.getAcadyear();
			semester=String.valueOf(sem.getSemester());
		}
		map.put("acadyear", acadyear);
	    map.put("semester", semester);
        
		return "/basedata/teachClass/teachclassImportHead.ftl";
	}
	
	@RequestMapping("/check/mergeclass")
	@ResponseBody
	public String doCheckMergeClass(String acadyearSearch, String semesterSearch) {
		OpenTeachingSearchDto searchDto = new OpenTeachingSearchDto();
		searchDto.setAcadyear(acadyearSearch);
		searchDto.setSemester(semesterSearch);
		searchDto.setUnitId(getLoginInfo().getUnitId());
		searchDto.setIsDeleted(0);
		searchDto.setSubjectType(BaseConstants.SUBJECT_TYPE_XX);
		List<GradeTeaching> gradeTeachList = gradeTeachingService.findBySearch(searchDto);
		if(gradeTeachList.size() == 0) {
			return error("没有年级课程开设信息，请先去维护年级课程开设");
		}
		Set<String> subjectIdSet = EntityUtils.getSet(gradeTeachList, "subjectId");
		List<Course> courseList = courseService.findListByIdIn(subjectIdSet.toArray(new String[subjectIdSet.size()]));
		//获取用于合并教学班的老师
		TeachClassSearchDto classSearchDto = new TeachClassSearchDto();
		classSearchDto.setAcadyearSearch(acadyearSearch);
		classSearchDto.setSemesterSearch(semesterSearch);
		classSearchDto.setShowTabType(BaseConstants.SUBJECT_TYPE_XX);
		classSearchDto.setUnitId(getLoginInfo().getUnitId());
		classSearchDto.setIsUsing("1");
		classSearchDto.setIsUsingMerge("1");
		classSearchDto.makeClassType();
		List<TeachClass> teachClassList=teachClassService.findListByDto(classSearchDto);
		if(teachClassList.size() == 0) {
			return error("没有用于合并教学班的教学班，请先去维护教学班");
		}
		return success("");
	}
	
	@RequestMapping("/mergeclass/index/page")
	public String showMergeClassIndex(String acadyearSearch, String semesterSearch, String showTabType,ModelMap map){
		//只做新增
		
		//只有学年学期，获取开设的课程
		OpenTeachingSearchDto searchDto = new OpenTeachingSearchDto();
		searchDto.setAcadyear(acadyearSearch);
		searchDto.setSemester(semesterSearch);
		searchDto.setUnitId(getLoginInfo().getUnitId());
		searchDto.setIsDeleted(0);
		searchDto.setSubjectType(BaseConstants.SUBJECT_TYPE_XX);
		List<GradeTeaching> gradeTeachList = gradeTeachingService.findBySearch(searchDto);
		if(gradeTeachList.size() == 0) {
			return promptFlt(map, "请先去维护年级课程开设");
		}
		Set<String> subjectIdSet = EntityUtils.getSet(gradeTeachList, "subjectId");
		List<Course> courseList = courseService.findListByIdIn(subjectIdSet.toArray(new String[subjectIdSet.size()]));
		//获取用于合并教学班的老师
		TeachClassSearchDto classSearchDto = new TeachClassSearchDto();
		classSearchDto.setAcadyearSearch(acadyearSearch);
		classSearchDto.setSemesterSearch(semesterSearch);
		classSearchDto.setShowTabType(BaseConstants.SUBJECT_TYPE_XX);
		classSearchDto.setUnitId(getLoginInfo().getUnitId());
		classSearchDto.setIsUsing("1");
		classSearchDto.setIsUsingMerge("1");
		classSearchDto.makeClassType();
		List<TeachClass> teachClassList=teachClassService.findListByDto(classSearchDto);
		if(teachClassList.size() == 0) {
			return promptFlt(map, "没有用于合并教学班的教学班，请先去维护教学班");
		}
		//
		map.put("dayOfWeek",7);
		List<TeachPlace> placeList = teachPlaceService.findTeachPlaceListByType(getLoginInfo().getUnitId(), null);
		map.put("placeList", placeList);
		
		// 获取 年级Id
		Set<String> gradeIdSet = new HashSet<String>();
		for (TeachClass teachClass : teachClassList) {
			String gradeIds = teachClass.getGradeId();
			String[] gradeId = gradeIds.split(",");
			for (String string : gradeId) {
				gradeIdSet.add(string);
			}
		}
		
		boolean isEdit = false;
		
		makeTimeByGrade(gradeIdSet.toArray(new String[gradeIdSet.size()]), map);
		
		// 获取老师
		Set<String> teacherIdSet = EntityUtils.getSet(teachClassList, "teacherId");
		List<Teacher> teacherList = teacherService.findListByIdIn(teacherIdSet.toArray(new String[teacherIdSet.size()]));
		
		TeachClass teachClass = new TeachClass();
		
		String unitId = getLoginInfo().getUnitId();
		map.put("isEdit", isEdit);
		map.put("unitId", unitId);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
		map.put("showTabType", showTabType);
		map.put("courseList", courseList);
		map.put("teacherList", teacherList);
		teachClass.setAcadyear(acadyearSearch);
		teachClass.setSemester(semesterSearch);
		teachClass.setUnitId(unitId);
		
		map.put("teachClass", teachClass);
		String value = systemIniRemoteService.findValue(BaseConstants.TEACLSS_TEACHING_LIMIE);
		return "/basedata/teachClass/mergeTeachclassIndex.ftl";
	}
	
	@RequestMapping("/getmergeteachclass/index/page")
	public String showGetMergeTeachClassIndex(String acadyearSearch, String semesterSearch, String showTabType, String teacherId, String tcIds, ModelMap map) {
		TeachClassSearchDto classSearchDto = new TeachClassSearchDto();
		classSearchDto.setAcadyearSearch(acadyearSearch);
		classSearchDto.setSemesterSearch(semesterSearch);
		classSearchDto.setShowTabType(BaseConstants.SUBJECT_TYPE_XX);
		classSearchDto.setUnitId(getLoginInfo().getUnitId());
		classSearchDto.setIsUsing("1");
		classSearchDto.setIsUsingMerge("1");
		classSearchDto.setTeacherId(teacherId);
		classSearchDto.makeClassType();
		List<TeachClass> teachClassList = teachClassService.findListByDto(classSearchDto);
		map.put("teachClassList", teachClassList);
		map.put("tcIds", tcIds);
		return "/basedata/teachClass/mergeTeacherClassGet.ftl";
	}
	
	@RequestMapping("/mergeclass/edit/page")
	public String showEditTeachClassIndex(String id, String showView, String acadyearSearch, String semesterSearch, String showTabType, ModelMap map) {
		TeachClass teachClass = teachClassService.findById(id);
		List<TeachClass> tClassList = teachClassService.findByParentIds(new String[]{id});
		String teachClassId = "";
		for (TeachClass teachClass2 : tClassList) {
			teachClassId = teachClassId + "," + teachClass2.getId();
		}
		if(StringUtils.isNotBlank(teachClassId)) {
			
			teachClassId = teachClassId.substring(1);
		}
		map.put("dayOfWeek",7);
		List<TeachPlace> placeList = teachPlaceService.findTeachPlaceListByType(getLoginInfo().getUnitId(), null);
		boolean isEdit=false;
		if("1".equals(showView)){
			//编辑
			isEdit=true;
		}
		if("0".equals(teachClass.getIsUsing()) && isEdit){
			isEdit=false;
		}
		map.put("placeList", placeList);
		String[] gradeIdArr = teachClass.getGradeId().split(",");
		if(!makeTimeByGrade(gradeIdArr, map)){
			return promptFlt(map, "选中的年级参数丢失，请重新操作");
		}
		Course course = courseService.findOne(teachClass.getCourseId());
		List<Course> courseList = new ArrayList<Course>();
		courseList.add(course);
		teachClass.setCourseName(course==null?"":course.getSubjectName());
		Teacher teacher = teacherService.findOne(teachClass.getTeacherId());
		List<Teacher> teacherList = new ArrayList<Teacher>();
		teacherList.add(teacher);
		teachClass.setTeacherNames(teacher==null?"":teacher.getTeacherName());
		//已有的上课时间
		List<TeachClassEx> exList=teachClassExService.findByTeachClassId(teachClass.getId());
		map.put("tcIds", teachClassId);
		map.put("courseList", courseList);
		map.put("teacherList", teacherList);
		map.put("subjectId", course.getId());
		map.put("teacherId", teacher.getId());
		map.put("exList", exList);
		map.put("isEdit", isEdit);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
		map.put("showTabType", showTabType);
		map.put("tClassList", tClassList);
		if(!isEdit){
			makeTimeStr(exList);
		}
		map.put("teachClass", teachClass);
		return "/basedata/teachClass/mergeTeachclassIndex.ftl";
	}
	
	private boolean getLimit(){
		String limit = systemIniRemoteService.findValue(BaseConstants.TEACLSS_TEACHING_LIMIE);
		if(Constant.IS_FALSE_Str.equals(limit)){
			return true;
		}
		return false;
	}
}