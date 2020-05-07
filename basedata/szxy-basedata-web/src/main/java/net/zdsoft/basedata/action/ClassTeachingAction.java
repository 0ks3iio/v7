package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.GradeTeachingDto;
import net.zdsoft.basedata.dto.GradeTeachingListDto;
import net.zdsoft.basedata.dto.OpenTeachingSearchDto;
import net.zdsoft.basedata.dto.TeachSettingDto;
import net.zdsoft.basedata.dto.TeacherDto;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.ClassTeachingEx;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachPlanEx;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.ClassTeachingExService;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.CourseTypeService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.GradeTeachingService;
import net.zdsoft.basedata.service.SchoolCalendarService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/basedata")
public class ClassTeachingAction extends BaseAction{
	
	@Autowired
	private ClassTeachingService classTeachingService;
	@Autowired
	private GradeService  gradeService;
	@Autowired
	private ClassService  classService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private GradeTeachingService gradeTeachingService;
	@Autowired
	private CourseTypeService courseTypeService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private ClassTeachingExService classTeachingExService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
    private SchoolCalendarService schoolCalendarService;
	@Autowired
	private TeachClassService teachClassService;
	
	@InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
	
	@RequestMapping("/courseopen/index/page")
	@ControllerInfo("进入首页")
	public String ShowIndex(ModelMap map, HttpServletRequest request) {
		return "/basedata/classTeaching/classTeachingIndex.ftl";
	}
	
	@RequestMapping("/courseopen/real/index/page")
	@ControllerInfo("进入课程开设首页")
	public String ShowCourseOpenIndex(String acadyear, String semester, String gradeId, String tabIndex, ModelMap map) {
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("gradeId", gradeId);
		map.put("tabIndex", tabIndex);
		return "/basedata/classTeaching/courseOpenIndex.ftl";
	}
	
	@RequestMapping("/courseopen/grade/index/page")
	@ControllerInfo("进入年级课程开设首页")
	public String ShowGradeCourseOpenIndex(String acadyear, String semester, String gradeId, ModelMap map) {
		List<String> acadyearList = semesterService.findAcadeyearList();
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        String unitId = getLoginInfo().getUnitId();
        if(StringUtils.isBlank(semester)){
        	Semester se = semesterService.findCurrentSemester(2,unitId);
        	if(se!=null){
        		acadyear = se.getAcadyear();
        		semester = String.valueOf(se.getSemester());
        	}
        }
        List<Grade> gradeList = gradeService.findByUnitId(unitId);
        
        map.put("acadyearList", acadyearList);
        map.put("acadyear", acadyear);
        map.put("semester", semester);
        map.put("gradeId", gradeId);
		map.put("gradeList", gradeList);
		return "/basedata/classTeaching/gradeCourseOpenIndex.ftl";
	}
	
	@RequestMapping("/courseopen/detail/index/page")
	@ControllerInfo("年级/班级课程开设详情")
	public String ShowClassCourseOpenDetailIndex(ModelMap map,String useMaster, HttpServletRequest request) {
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String gradeId = request.getParameter("gradeId");
		String classId = request.getParameter("classId");
		
		String unitId = getLoginInfo().getUnitId();
		List<GradeTeachingDto> requiredCourseList = new ArrayList<GradeTeachingDto>();
		List<GradeTeachingDto> optionaldCourseList = new ArrayList<GradeTeachingDto>();
		String[] types = getRealTypes(BaseConstants.SUBJECT_TYPE_BX, null);
		List<String> typeList = Arrays.asList(types);
		if(classId == null) {
			//年级课程开设详情
			OpenTeachingSearchDto gradeTeachingSearchDto = new OpenTeachingSearchDto();
			gradeTeachingSearchDto.setAcadyear(acadyear);
			gradeTeachingSearchDto.setSemester(semester);
			gradeTeachingSearchDto.setGradeIds(new String[]{gradeId});
			gradeTeachingSearchDto.setUnitId(unitId);
			gradeTeachingSearchDto.setIsDeleted(Constant.IS_DELETED_FALSE);
			List<GradeTeaching> gradeTeachingList = new ArrayList<GradeTeaching>();
			if(Objects.equals(useMaster, "1")){
				gradeTeachingList = gradeTeachingService.findBySearchWithMaster(gradeTeachingSearchDto);
			}else{
				gradeTeachingList = gradeTeachingService.findBySearch(gradeTeachingSearchDto);
			}
			
			Set<String> subjectIdSet = EntityUtils.getSet(gradeTeachingList, GradeTeaching::getSubjectId);
			List<Course> courseList = courseService.findListByIdIn(subjectIdSet.toArray(new String[subjectIdSet.size()]));
			Set<String> courseTypeIdSet = EntityUtils.getSet(courseList, Course::getCourseTypeId);
			Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
			List<CourseType> courseTypeList = courseTypeService.findListByIds(courseTypeIdSet.toArray(new String[courseTypeIdSet.size()]));
			Map<String, String> courseTypeNameMap = EntityUtils.getMap(courseTypeList, CourseType::getId, CourseType::getName);
			
			for (GradeTeaching gradeTeaching : gradeTeachingList) {
				GradeTeachingDto gradeTeachingDto = new GradeTeachingDto();
				gradeTeachingDto.setId(gradeTeaching.getId());
				gradeTeachingDto.setSubjectId(gradeTeaching.getSubjectId());
				gradeTeachingDto.setSubjectType(gradeTeaching.getSubjectType());
				Course course = courseMap.get(gradeTeaching.getSubjectId());
				if(course!=null){
					gradeTeachingDto.setSubjectName(course.getSubjectName());
					if(courseTypeNameMap.containsKey(course.getCourseTypeId())){
						gradeTeachingDto.setBelongToSubjectName(courseTypeNameMap.get(course.getCourseTypeId()));
					}
				}
				gradeTeachingDto.setIsTeaCls(gradeTeaching.getIsTeaCls());
				gradeTeachingDto.setCredit(gradeTeaching.getCredit());
				if(typeList.contains(gradeTeaching.getSubjectType())) {
					requiredCourseList.add(gradeTeachingDto);
				} else {
					optionaldCourseList.add(gradeTeachingDto);
				}
			}
			map.put("state", "1");
		}else {
			List<ClassTeaching> classTeachingList = new ArrayList<ClassTeaching>();
			if(Objects.equals(useMaster, "1")){
				classTeachingList = classTeachingService.findClassTeachingListWithMaster(acadyear, semester, new String[]{classId}, unitId, Constant.IS_DELETED_FALSE,null,false);
			}else{
				classTeachingList = classTeachingService.findClassTeachingList(acadyear, semester, new String[]{classId}, unitId, Constant.IS_DELETED_FALSE,null,false);
			}
			
			Set<String> subjectIdSet = EntityUtils.getSet(classTeachingList, ClassTeaching::getSubjectId);
			List<Course> courseList = courseService.findListByIdIn(subjectIdSet.toArray(new String[subjectIdSet.size()]));
			Set<String> courseTypeIdSet = EntityUtils.getSet(courseList, Course::getCourseTypeId);
			Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
			List<CourseType> courseTypeList = courseTypeService.findListByIds(courseTypeIdSet.toArray(new String[courseTypeIdSet.size()]));
			Map<String, String> courseTypeNameMap = EntityUtils.getMap(courseTypeList, CourseType::getId, CourseType::getName);
			
			for (ClassTeaching classTeaching : classTeachingList) {
				GradeTeachingDto gradeTeachingDto = new GradeTeachingDto();
				gradeTeachingDto.setId(classTeaching.getId());
				gradeTeachingDto.setSubjectId(classTeaching.getSubjectId());
				gradeTeachingDto.setIsDeleted(classTeaching.getIsDeleted());
				gradeTeachingDto.setSubjectType(classTeaching.getSubjectType());
				gradeTeachingDto.setCourseHour(classTeaching.getCourseHour());
				gradeTeachingDto.setPunchCard(classTeaching.getPunchCard()==null?0:classTeaching.getPunchCard());
				gradeTeachingDto.setIsTeaCls(classTeaching.getIsTeaCls());
				gradeTeachingDto.setCredit(classTeaching.getCredit());
				gradeTeachingDto.setWeekType(classTeaching.getWeekType());
				Course course = courseMap.get(classTeaching.getSubjectId());
				if(course!=null){
					gradeTeachingDto.setSubjectName(course.getSubjectName());
					if(courseTypeNameMap.containsKey(course.getCourseTypeId())){
						gradeTeachingDto.setBelongToSubjectName(courseTypeNameMap.get(course.getCourseTypeId()));
					}
				}
				if(typeList.contains(classTeaching.getSubjectType())) {
					requiredCourseList.add(gradeTeachingDto);
				} else {
					optionaldCourseList.add(gradeTeachingDto);
				}
			}
			map.put("state", "2");
		}
		map.put("requiredCourseList", requiredCourseList);
		map.put("optionaldCourseList", optionaldCourseList);
		return "/basedata/classTeaching/courseOpenDetailIndex.ftl";
	}
	
	@RequestMapping("/courseopen/class/add/index/page")
	@ControllerInfo("年级/班级添加选课首页tab页")
	public String showCourseOpenAddIndex(String acadyear, String semester, String gradeId, String classId, ModelMap map) {
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("gradeId", gradeId);
		map.put("classId", classId);
		return "/basedata/classTeaching/addCourseIndex.ftl";
	}
	
	@RequestMapping("/courseopen/class/add/page")
	@ControllerInfo("年级/班级添加选课查询条件页")
	public String showCourseOpenAddPage(String acadyear, String semester, String gradeId, String classId, String subjectType, ModelMap map) {
		List<CourseType> courseTypeList = new ArrayList<CourseType>();
        if((BaseConstants.SUBJECT_TYPE_BX).equals(subjectType)){
        	courseTypeList = courseTypeService.findByTypes(new String[]{BaseConstants.SUBJECT_TYPE_BX, BaseConstants.SUBJECT_TYPE_VIRTUAL});
        }else{
        	courseTypeList = courseTypeService.findByNameAndTypeWithPage("", BaseConstants.SUBJECT_TYPE_XX, null);
        }
		
        map.put("acadyear", acadyear);
        map.put("semester", semester);
        map.put("gradeId", gradeId);
        map.put("classId", classId);
        map.put("subjectType", subjectType);
        map.put("courseTypeList", courseTypeList);
        
        return "/basedata/classTeaching/addCourse.ftl";
	}
	
	@RequestMapping("/courseopen/class/add/detail/page")
	@ControllerInfo("年级/班级添加选课列表页")
	public String showCourseOpenAddDetailPage(ModelMap map,HttpServletRequest request) {
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String gradeId = request.getParameter("gradeId");
		String classId = request.getParameter("classId");
		String type = request.getParameter("subjectType");
		String subjectType = request.getParameter("courseSource");
		String courseTypeId = request.getParameter("searchType");
		String searchName = request.getParameter("searchName");
		String xxType = request.getParameter("xxType");
		Grade grade = gradeService.findById(gradeId);
		Integer section = grade.getSection();
		String unitId = getLoginInfo().getUnitId();
		
		String[] types = getRealTypes(type, xxType);
		List<Course> courseList = courseService.getListByCondition(unitId, types, courseTypeId, searchName, section+"", subjectType, Course.TRUE_1);
		List<GradeTeaching> gradeTeachingList = gradeTeachingService.findGradeTeachingList(acadyear, semester, gradeId, unitId, types);
		
		List<GradeTeachingDto> gradeTeachingDtoList = new ArrayList<GradeTeachingDto>();
		Set<String> subjectIds = EntityUtils.getSet(gradeTeachingList, GradeTeaching::getSubjectId);
		if(StringUtils.isNotBlank(classId)){
			List<String> subjectIdList = subjectIds.stream().collect(Collectors.toList());
			courseList = courseList.stream().filter(e->subjectIdList.contains(e.getId())).collect(Collectors.toList());
			/*虚拟课程按照普通课程开课
			List<String> virtualIds = courseList.stream().filter(e->BaseConstants.VIRTUAL_COURSE_TYPE.equals(e.getCourseTypeId())).map(e->e.getId()).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(virtualIds)){
				Map<String, List<String>> subjectClassMap = teachClassService.findClassIdsByVirtualIds(unitId, acadyear, semester, "", virtualIds.toArray(new String[virtualIds.size()]));
				for (String virtualId : virtualIds) {
					if(!(subjectClassMap.containsKey(virtualId)&&subjectClassMap.get(virtualId).contains(classId))){
						courseList = courseList.stream().filter(e->!virtualId.equals(e.getId())).collect(Collectors.toList());
					}
				}
			}
			*/
			List<ClassTeaching> classTeachingList = classTeachingService.findClassTeachingList(acadyear, semester, new String[]{classId}, unitId, Constant.IS_DELETED_FALSE
					, subjectIds.toArray(new String[subjectIds.size()]),false);
			subjectIds = EntityUtils.getSet(classTeachingList, ClassTeaching::getSubjectId);
		}
		for (Course course : courseList) {
			GradeTeachingDto gradeTeachingDto = new GradeTeachingDto();
			gradeTeachingDto.setSubjectId(course.getId());
			gradeTeachingDto.setSubjectName(course.getSubjectName());
			gradeTeachingDto.setCredit(course.getInitCredit());
			if(subjectIds.contains(course.getId())){
				gradeTeachingDto.setFlag("1");
			}else{
				gradeTeachingDto.setFlag("0");
			}
			gradeTeachingDto.setSubjectType(course.getType());
			gradeTeachingDtoList.add(gradeTeachingDto);
		}
		
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("gradeId", gradeId);
		map.put("classId", classId);
		map.put("subjectType", type);
		map.put("xxType", xxType);
		map.put("gradeTeachingDtoList", gradeTeachingDtoList);
		return "/basedata/classTeaching/addCourseDetail.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/courseopen/save")
	@ControllerInfo(value = "年级开设科目保存")
	public String doSaveCourseOpenList(GradeTeachingListDto gradeTeachingListDto) {
		
		String acadyear = gradeTeachingListDto.getAcadyear();
		String semester = gradeTeachingListDto.getSemester();
		String gradeId = gradeTeachingListDto.getGradeId();
		String subjectType = gradeTeachingListDto.getSubjectType();
		String xxType = gradeTeachingListDto.getXxType();
		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();

		String[] types = getRealTypes(subjectType, xxType);
		List<GradeTeaching> oldGradeTeachingList = gradeTeachingService.findGradeTeachingListWithMaster(acadyear, semester, gradeId, unitId, types);
		List<GradeTeachingDto> gradeTeachingDtoList = gradeTeachingListDto.getGradeTeachingDtoList();
		if(CollectionUtils.isNotEmpty(gradeTeachingDtoList)){
			Set<String> otherSubjectIds = EntityUtils.getSet(gradeTeachingDtoList, GradeTeachingDto::getSubjectId);
			oldGradeTeachingList = oldGradeTeachingList.stream().filter(e->otherSubjectIds.contains(e.getSubjectId())).collect(Collectors.toList());
			gradeTeachingDtoList = gradeTeachingDtoList.stream().filter(e->Constant.IS_TRUE_Str.equals(e.getFlag())).collect(Collectors.toList());
		}
		//如果没有旧数据且新数据为空，说明没有需要保存的数据
		if(gradeTeachingDtoList.size() == 0 && CollectionUtils.isEmpty(oldGradeTeachingList)) {
			return error("没有需要保存的课程");
		}
		
		try {
			List<GradeTeaching> newGradeTeachingList = new ArrayList<GradeTeaching>();
			List<GradeTeaching> delGradeTeachingList = new ArrayList<GradeTeaching>();
			//旧数据不为空
			if(CollectionUtils.isNotEmpty(oldGradeTeachingList)){
				//新数据为空，说明只需删除原有的年级课程开设及班级课程开设
				if(gradeTeachingDtoList.size()==0){
					for (GradeTeaching gradeTeaching : oldGradeTeachingList) {
						gradeTeaching.setIsDeleted(Constant.IS_TRUE);
						gradeTeaching.setModifyTime(new Date());
					}
					delGradeTeachingList.addAll(oldGradeTeachingList);
				}else{//需删除旧的数据，并添加新的数据
					Map<String, GradeTeaching> subjectId2GradeTeaching = EntityUtils.getMap(oldGradeTeachingList, GradeTeaching::getSubjectId);

					/*Set<String> subjectIdSet = EntityUtils.getSet(gradeTeachingList, GradeTeaching::getSubjectId);
					List<Course> courseList = courseService.findListByIdIn(subjectIdSet.toArray(new String[subjectIdSet.size()]));
					Set<String> courseTypeIdSet = EntityUtils.getSet(courseList, Course::getCourseTypeId);
					Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
					List<CourseType> courseTypeList = courseTypeService.findListByIds(courseTypeIdSet.toArray(new String[courseTypeIdSet.size()]));
					Map<String, String> courseTypeNameMap = EntityUtils.getMap(courseTypeList, CourseType::getId, CourseType::getName);
*/

					for (GradeTeachingDto gradeTeachingDto : gradeTeachingDtoList) {
						if(subjectId2GradeTeaching.containsKey(gradeTeachingDto.getSubjectId()) ){
							GradeTeaching gradeTeaching = subjectId2GradeTeaching.get(gradeTeachingDto.getSubjectId());
							newGradeTeachingList.add(gradeTeaching);
							subjectId2GradeTeaching.remove(gradeTeachingDto.getSubjectId());
						}else{
							GradeTeaching gradeTeaching = new GradeTeaching();
							gradeTeaching.setId(UuidUtils.generateUuid());
							gradeTeaching.setAcadyear(acadyear);
							gradeTeaching.setSemester(semester);
							gradeTeaching.setGradeId(gradeId);
							gradeTeaching.setCreationTime(new Date());
							gradeTeaching.setIsDeleted(Constant.IS_FALSE);
							gradeTeaching.setModifyTime(new Date());
							gradeTeaching.setSubjectId(gradeTeachingDto.getSubjectId());
							gradeTeaching.setSubjectType(gradeTeachingDto.getSubjectType());
							gradeTeaching.setUnitId(unitId);
							gradeTeaching.setCredit(gradeTeachingDto.getCredit());
							if(BaseConstants.SUBJECT_TYPE_XX.equals(subjectType)) {
								gradeTeaching.setIsTeaCls(Constant.IS_TRUE);
							}else{
								gradeTeaching.setIsTeaCls(Constant.IS_FALSE);
							}
							newGradeTeachingList.add(gradeTeaching);
						}
					}
					for (Entry<String, GradeTeaching> entry : subjectId2GradeTeaching.entrySet()) {
						GradeTeaching gradeTeaching = entry.getValue();
						gradeTeaching.setIsDeleted(Constant.IS_TRUE);
						gradeTeaching.setModifyTime(new Date());
						delGradeTeachingList.add(gradeTeaching);
					}
				}
			}else{
				GradeTeaching gradeTeaching = null;
				for (GradeTeachingDto gradeTeachingDto : gradeTeachingDtoList) {
						gradeTeaching = new GradeTeaching();
						gradeTeaching.setId(UuidUtils.generateUuid());
						gradeTeaching.setAcadyear(acadyear);
						gradeTeaching.setSemester(semester);
						gradeTeaching.setGradeId(gradeId);
						gradeTeaching.setCreationTime(new Date());
						gradeTeaching.setIsDeleted(Constant.IS_FALSE);
						gradeTeaching.setModifyTime(new Date());
						gradeTeaching.setSubjectId(gradeTeachingDto.getSubjectId());
						gradeTeaching.setSubjectType(gradeTeachingDto.getSubjectType());
						gradeTeaching.setUnitId(unitId);
						//gradeTeachingDto.getSubjectId()
						gradeTeaching.setCredit(gradeTeachingDto.getCredit());
						if(BaseConstants.SUBJECT_TYPE_XX.equals(subjectType)) {
							gradeTeaching.setIsTeaCls(Constant.IS_TRUE);
						}else{
							gradeTeaching.setIsTeaCls(Constant.IS_FALSE);
						}
						gradeTeaching.setIsTeaCls(Constant.IS_FALSE);
						newGradeTeachingList.add(gradeTeaching);
				}
				
			}
			
			String msg = gradeTeachingService.deleteAndSave(acadyear,semester,gradeId,unitId,subjectType,loginInfo.getUserId(),newGradeTeachingList,delGradeTeachingList);
			if(StringUtils.isNotBlank(msg)){
				return error(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}

    
    @ResponseBody
	@RequestMapping("/courseopen/changeGradeIsTeaCls")
	@ControllerInfo("年级课程更改是否以教学班形式教学")
	public String updateGradeTeachingIsTeaCls(String gradeTeachingId,Integer isTeaCls){
    	GradeTeaching gt = gradeTeachingService.findOne(gradeTeachingId);
    	Course course = courseService.findOne(gt.getSubjectId());
    	if(BaseConstants.VIRTUAL_COURSE_TYPE.equals(course.getCourseTypeId())&&Constant.IS_TRUE==isTeaCls){
    		return error("走班课程不能以教学班形式教学。");
    	}
    	gt.setIsTeaCls(isTeaCls);
    	
		try{
			String msg = gradeTeachingService.updateOne(gt,getLoginInfo().getUserId());
			if(StringUtils.isNotBlank(msg)){
				return error(msg);
			}
		}catch (Exception e) {
			return returnError();
		}
		return returnSuccess();
	}

	@ResponseBody
	@RequestMapping("/courseopen/changeGradeCredit")
	@ControllerInfo("更改年级必修课程的学分")
	public String updateGradeCredit(String gradeTeachingId,Integer credit){
		GradeTeaching gt = gradeTeachingService.findOne(gradeTeachingId);
		gt.setCredit(credit);
		try{
			String msg = gradeTeachingService.updateOne(gt,getLoginInfo().getUserId());
			if(StringUtils.isNotBlank(msg)){
				return error(msg);
			}
		}catch (Exception e) {
			return returnError();
		}
		return returnSuccess();
	}

	@ResponseBody
	@RequestMapping("/courseopen/changeGradeXcredit")
	@ControllerInfo("更改年级选修课程的学分")
	public String updateGradeXcredit(String gradeTeachingId,Integer credit){
		GradeTeaching gt = gradeTeachingService.findOne(gradeTeachingId);
		gt.setCredit(credit);
		try{
			String msg = gradeTeachingService.updateOne(gt,getLoginInfo().getUserId());
			if(StringUtils.isNotBlank(msg)){
				return error(msg);
			}
		}catch (Exception e) {
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/courseopen/copyToOtherGrade")
	@ControllerInfo("复制课程到其他年级")
	public String doCopyToOtherGrade(String acadyear, String semester, String gradeId) {
		
		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();
		Grade grade = gradeService.findById(gradeId);
		//剔除本年级
		List<Grade> gradeList = gradeService.findByUnitIdNotGraduate(unitId, new Integer[]{grade.getSection()});
		gradeList = gradeList.stream().filter(e->!grade.getId().equals(e.getId())).collect(Collectors.toList());
		
		//只增加不删除
		//gradeList存储的即为需要复制的年级列表
		Set<String> gradeIdset = EntityUtils.getSet(gradeList, Grade::getId);
		
		//本学年，本学期，本年纪的开设科目
		List<GradeTeaching> gradeTeachingNewList = gradeTeachingService.findGradeTeachingList(acadyear, semester, new String[]{gradeId}, unitId, Constant.IS_DELETED_FALSE, null, null);
		if(gradeTeachingNewList.size() == 0) {
			return error("本学年，本学期，本年级没有开设科目，无法复制到其他年级");
		}
		List<Course> subjectList = courseService.findListByIds(EntityUtils.getList(gradeTeachingNewList, GradeTeaching::getSubjectId).toArray(new String[0]));
		Map<String, String> courseTypeMap = subjectList.stream().filter(e->StringUtils.isNotBlank(e.getCourseTypeId())).collect(Collectors.toMap(Course::getId, Course::getCourseTypeId));
		if(MapUtils.isNotEmpty(courseTypeMap)){
			gradeTeachingNewList = gradeTeachingNewList.stream().filter(e->!BaseConstants.VIRTUAL_COURSE_TYPE.equals(courseTypeMap.get(e.getSubjectId()))).collect(Collectors.toList());
		}
		if(CollectionUtils.isEmpty(gradeTeachingNewList)){
			return error("本学年，本学期，本年级没有开设非走班课程，无法复制到其他年级");
		}
		Map<String,List<String>> gradeTeachingHaveMap = new HashMap<String, List<String>>();
		Map<String,List<GradeTeaching>> gradeTeachingSaveMap = new HashMap<String, List<GradeTeaching>>();
		for (Grade tempGrade : gradeList) {
			String tempGradeId = tempGrade.getId();
			if(!gradeTeachingHaveMap.containsKey(tempGradeId)){
				gradeTeachingHaveMap.put(tempGradeId, new ArrayList<String>());
			}
			if(!gradeTeachingSaveMap.containsKey(tempGradeId)){
				gradeTeachingSaveMap.put(tempGradeId, new ArrayList<GradeTeaching>());
			}
		}
		
		List<GradeTeaching> gradeTeachingHave = gradeTeachingService.findGradeTeachingList(acadyear, semester, gradeIdset.toArray(new String[gradeIdset.size()]), unitId, Constant.IS_DELETED_FALSE, null, null);
		for (GradeTeaching gradeTeaching : gradeTeachingHave) {
			gradeTeachingHaveMap.get(gradeTeaching.getGradeId()).add(gradeTeaching.getSubjectId());
		}
		
		for(Entry<String, List<String>> entry : gradeTeachingHaveMap.entrySet()){
			String gradId = entry.getKey();
			
			for(GradeTeaching gradeTeachingNew : gradeTeachingNewList) {
				String newSubjectId = gradeTeachingNew.getSubjectId();
				if(entry.getValue().contains(newSubjectId)){
					continue;
				}
				//该课程需要复制
				GradeTeaching gradeTeaching = new GradeTeaching();
				gradeTeaching.setId(UuidUtils.generateUuid());
				gradeTeaching.setAcadyear(gradeTeachingNew.getAcadyear());
				gradeTeaching.setGradeId(gradeTeachingNew.getGradeId());
				gradeTeaching.setIsDeleted(gradeTeachingNew.getIsDeleted());
				gradeTeaching.setSemester(gradeTeachingNew.getSemester());
				gradeTeaching.setCreationTime(new Date());
				gradeTeaching.setModifyTime(new Date());
				gradeTeaching.setSubjectId(gradeTeachingNew.getSubjectId());
				gradeTeaching.setSubjectType(gradeTeachingNew.getSubjectType());
				gradeTeaching.setUnitId(unitId);
				gradeTeaching.setIsTeaCls(gradeTeachingNew.getIsTeaCls());
				gradeTeachingSaveMap.get(gradId).add(gradeTeaching);
			}
		}
		
		try {
			gradeTeachingService.saveAndInit(acadyear, semester, unitId, gradeId, loginInfo.getUserId(), gradeTeachingSaveMap);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/grade/courseopen/delete")
	@ControllerInfo(value = "年级开设科目删除")
	public String dodeleteGradeTeaching(String gradeTeachingId ) {
		try {
			//同时删除行政班该科目课表
			GradeTeaching gt = gradeTeachingService.findOne(gradeTeachingId);
			gt.setIsDeleted(Constant.IS_DELETED_TRUE);
			List<GradeTeaching> delGtList = new ArrayList<GradeTeaching>();
			delGtList.add(gt);
			String msg = gradeTeachingService.deleteAndSave(gt.getAcadyear(), gt.getSemester(), gt.getGradeId(), gt.getUnitId(),getLoginInfo().getUserId(), null, null, delGtList);
			if(StringUtils.isNotBlank(msg)){
				return error(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	
	// 初始页
	@RequestMapping("/courseopen/gradeImport/head/page")
	@ControllerInfo("进入年级课程开设导入首页")
	public String showImportHead(String acadyear, String semester, String gradeId, String tabIndex, ModelMap map) {
		List<String> acadyearList = semesterService.findAcadeyearList();
		if(CollectionUtils.isEmpty(acadyearList)){
			return promptFlt(map, "请先设置学年学期");
		}
		//取学校当前学年学期
		if(StringUtils.isBlank(semester)){
			Semester se = semesterService.findCurrentSemester(2,getLoginInfo().getUnitId());
			if(se!=null ){
				acadyear=se.getAcadyear();
				semester=String.valueOf(se.getSemester());
			}
		}
		
		map.put("acadyearList", acadyearList);
		map.put("tabIndex", tabIndex);
		map.put("acadyear", acadyear);
	    map.put("semester", semester);
	    map.put("gradeId", gradeId);
		return "/basedata/classTeaching/gradeCourseOpenImportHead.ftl";
	}

    @RequestMapping("/import/courseScheduleImport/head/page")
   	@ControllerInfo("年级课程表导入首页")
   	public String showClassImportHead(ModelMap map,String tabIndex, String acadyear, String semester, String gradeId) {
    	List<String> acadyearList = semesterService.findAcadeyearList();
		if (CollectionUtils.isEmpty(acadyearList)) {
			return promptFlt(map, "请先设置学年学期");
		}
		String unitId = getLoginInfo().getUnitId();
   		List<Grade> gradeList = gradeService.findByUnitId(unitId);
   		Map<String, Integer> cur2Max =schoolCalendarService.findCurrentWeekAndMaxWeek(acadyear, semester, unitId);
        Integer week = cur2Max.get("current");
        if(week==null){
        	week=1;
        }
        
        map.put("acadyearList", acadyearList);
        map.put("week", week);
        map.put("max", cur2Max.get("max"));
		map.put("gradeList", gradeList);
   		map.put("acadyear", acadyear);
   	    map.put("semester", semester);
   	    map.put("gradeId", gradeId);
   	    map.put("tabIndex", tabIndex);
           
   		return "/basedata/classTeaching/courseScheduleImportHead.ftl";
    }
    
    @RequestMapping("/courseScheduleCopy/index/page")
    @ControllerInfo("年级复制课表首页")
    public String showClassCopyHead(ModelMap map,String tabIndex, String acadyear, String semester, String gradeId) {
    	List<String> acadyearList = semesterService.findAcadeyearList();
    	if (CollectionUtils.isEmpty(acadyearList)) {
    		return promptFlt(map, "请先设置学年学期");
    	}
    	
    	map.put("acadyearList", acadyearList);
    	String unitId = getLoginInfo().getUnitId();
    	List<Grade> gradeList = gradeService.findByUnitId(unitId);
    	map.put("gradeList", gradeList);
    	map.put("destAcadyear", acadyear);
    	map.put("destSemester", semester);
    	map.put("gradeId", gradeId);
    	map.put("tabIndex", tabIndex);
    	String srcSemester;
    	String srcAcadyear=acadyear;
    	if("2".equals(semester)){
    		srcSemester = "1";
    	}else{
    		srcAcadyear = (Integer.parseInt(acadyear.substring(0,4))-1)+"-"+(Integer.parseInt(acadyear.substring(5,9))-1);
    		srcSemester = "2";
    	}
    	if(!acadyearList.contains(srcAcadyear)){
    		srcAcadyear=acadyear;
    		srcSemester=semester;
    	}
    	
    	map.put("srcAcadyear", srcAcadyear);
    	map.put("srcSemester", srcSemester);
    	Map<String, Integer> srcCur2Max =schoolCalendarService.findCurrentWeekAndMaxWeek(srcAcadyear, srcSemester, unitId);
    	Integer srcWeek = srcCur2Max.get("current");
    	if(srcWeek==null){
    		srcWeek=1;
    	}
    	map.put("srcWeek", srcWeek);
    	map.put("srcMax", srcCur2Max.get("max"));
        
        Map<String, Integer> destCur2Max =schoolCalendarService.findCurrentWeekAndMaxWeek(acadyear, semester, unitId);
        Integer destWeek = destCur2Max.get("current");
        if(destWeek==null){
        	destWeek=1;
        }
        map.put("destWeek", destWeek);
        map.put("destMax", destCur2Max.get("max"));
    	return "/basedata/classTeaching/courseScheduleCopyIndex.ftl";
    }
    
    @RequestMapping("/copyCourseSchedule")
    @ControllerInfo("年级复制课表保存")
    @ResponseBody
    public String copyCourseSchedule(String gradeId, String isCopySchedule, String srcAcadyear, String srcSemester, String srcWeek, 
    		String destAcadyear, String destSemester, String destWeek) {
    	try {
    		String msg = courseScheduleService.saveCopy(gradeId,isCopySchedule,srcAcadyear,srcSemester,srcWeek,destAcadyear,destSemester,destWeek, getLoginInfo().getUserId());
    		if(StringUtils.isNotBlank(msg)){
    			return error(msg);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		return returnError();
    	}
    	
    	return returnSuccess();
    }
	
	@RequestMapping("/courseopen/class/index/page")
	@ControllerInfo("进入班级课程开设首页")
	public String ShowClassCourseOpenIndex(String acadyear, String semester, String gradeId, ModelMap map) {
		
		List<String> acadyearList = semesterService.findAcadeyearList();
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        String unitId = getLoginInfo().getUnitId();
        if(StringUtils.isBlank(semester)){
        	Semester se = semesterService.findCurrentSemester(2,unitId);
        	if(se!=null){
        		acadyear=se.getAcadyear();
        		semester=String.valueOf(se.getSemester());
        	}
        }
        List<Grade> gradeList = gradeService.findByUnitId(unitId);
        
        map.put("acadyearList", acadyearList);
        map.put("acadyear", acadyear);
        map.put("semester", semester);
		map.put("gradeList", gradeList);
		if(CollectionUtils.isNotEmpty(gradeList)) {
			if(StringUtils.isBlank(gradeId)){
				gradeId = gradeList.get(0).getId();
			}
			List<Clazz> clazzList = classService.findByGradeIdIn(new String[]{gradeId});
			map.put("gradeId", gradeId);
			map.put("clazzList", clazzList);
		}
		return "/basedata/classTeaching/classCourseOpenIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/courseopen/class/save")
	@ControllerInfo(value = "班级开设科目保存")
	public String doSaveClassCourseOpenList(GradeTeachingListDto gradeTeachingListDto) {
		String acadyear = gradeTeachingListDto.getAcadyear();
		String semester = gradeTeachingListDto.getSemester();
		String classId = gradeTeachingListDto.getClassId();
		String subjectType = gradeTeachingListDto.getSubjectType();
		String xxType = gradeTeachingListDto.getXxType();
		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();
		String operatorId = loginInfo.getUserId();
		
		String[] types = getRealTypes(subjectType, xxType);
		List<String> typeList = Arrays.stream(types).collect(Collectors.toList());
		List<ClassTeaching> oldClassTeachingList = classTeachingService.findClassTeachingList(acadyear, semester, new String[]{classId}, unitId, Constant.IS_DELETED_FALSE,null,false);
		oldClassTeachingList = oldClassTeachingList.stream().filter(e->typeList.contains(e.getSubjectType())).collect(Collectors.toList());
		List<GradeTeachingDto> gradeTeachingDtoList = gradeTeachingListDto.getGradeTeachingDtoList();
		if(CollectionUtils.isNotEmpty(gradeTeachingDtoList)){
			Set<String> otherSubjectIds = EntityUtils.getSet(gradeTeachingDtoList, GradeTeachingDto::getSubjectId);
			oldClassTeachingList = oldClassTeachingList.stream().filter(e->otherSubjectIds.contains(e.getSubjectId())).collect(Collectors.toList());
			gradeTeachingDtoList = gradeTeachingDtoList.stream().filter(e->Constant.IS_TRUE_Str.equals(e.getFlag())).collect(Collectors.toList());
		}
		//如果没有旧数据且新数据为空，说明没有需要保存的数据
		if(gradeTeachingDtoList.size() == 0 && CollectionUtils.isEmpty(oldClassTeachingList)) {
			return error("没有需要保存的课程");
		}
		List<Course> courseList = courseService.findListByIds(EntityUtils.getList(gradeTeachingDtoList, GradeTeachingDto::getSubjectId).toArray(new String[gradeTeachingDtoList.size()]));
		Map<String, Course> subjectIdToCourse = EntityUtils.getMap(courseList, Course::getId);
		try {
			List<ClassTeaching> newClassTeachingList = new ArrayList<ClassTeaching>();
			List<ClassTeaching> delClassTeachingList = new ArrayList<ClassTeaching>();
			List<String> delSubjectIds = new ArrayList<String>();
			//旧数据不为空
			if(CollectionUtils.isNotEmpty(oldClassTeachingList)){
				//新数据为空，说明只需删除原有的班级课程开设
				if(gradeTeachingDtoList.size()==0){
					for (ClassTeaching classTeaching : oldClassTeachingList) {
						classTeaching.setIsDeleted(Constant.IS_TRUE);
						classTeaching.setModifyTime(new Date());
						classTeaching.setOperatorId(operatorId);
						delSubjectIds.add(classTeaching.getSubjectId());
					}
					delClassTeachingList.addAll(oldClassTeachingList);
				}else{//需删除旧的数据，并添加新的数据
					Map<String, ClassTeaching> subjectId2ClassTeaching = EntityUtils.getMap(oldClassTeachingList, ClassTeaching::getSubjectId);
					
					for (GradeTeachingDto gradeTeachingDto : gradeTeachingDtoList) {
						if(subjectId2ClassTeaching.containsKey(gradeTeachingDto.getSubjectId()) ){
							subjectId2ClassTeaching.remove(gradeTeachingDto.getSubjectId());
						}else{
							ClassTeaching classTeaching = new ClassTeaching();
							classTeaching.setId(UuidUtils.generateUuid());
							classTeaching.setUnitId(unitId);
							classTeaching.setClassId(classId);
							classTeaching.setSubjectId(gradeTeachingDto.getSubjectId());
							classTeaching.setAcadyear(acadyear);
							classTeaching.setSemester(semester);
							classTeaching.setCreationTime(new Date());
							classTeaching.setModifyTime(new Date());
							classTeaching.setEventSource(0);
							classTeaching.setIsDeleted(Constant.IS_FALSE);
							classTeaching.setSubjectType(gradeTeachingDto.getSubjectType());
							classTeaching.setOperatorId(operatorId);
							classTeaching.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
							classTeaching.setCredit(subjectIdToCourse.get(gradeTeachingDto.getSubjectId()).getInitCredit());
							classTeaching.setFullMark(subjectIdToCourse.get(gradeTeachingDto.getSubjectId()).getFullMark());
							classTeaching.setPassMark(subjectIdToCourse.get(gradeTeachingDto.getSubjectId()).getInitPassMark());
							if(BaseConstants.SUBJECT_TYPE_XX.equals(gradeTeachingDto.getSubjectType())) {
								classTeaching.setIsTeaCls(Constant.IS_TRUE);
								classTeaching.setPunchCard(Constant.IS_FALSE);
							}else {
								//必修 设置是否考勤
								classTeaching.setIsTeaCls(Constant.IS_FALSE);
								classTeaching.setPunchCard(Constant.IS_TRUE);
							}
							newClassTeachingList.add(classTeaching);
						}
					}
					for (Entry<String, ClassTeaching> entry : subjectId2ClassTeaching.entrySet()) {
						ClassTeaching classTeaching = entry.getValue();
						classTeaching.setIsDeleted(Constant.IS_TRUE);
						classTeaching.setModifyTime(new Date());
						classTeaching.setOperatorId(operatorId);
						delSubjectIds.add(classTeaching.getSubjectId());
						delClassTeachingList.add(classTeaching);
					}
				}
			}else{
				ClassTeaching classTeaching = null;
				for (GradeTeachingDto gradeTeachingDto : gradeTeachingDtoList) {
					classTeaching = new ClassTeaching();
					classTeaching.setId(UuidUtils.generateUuid());
					classTeaching.setUnitId(unitId);
					classTeaching.setClassId(classId);
					classTeaching.setSubjectId(gradeTeachingDto.getSubjectId());
					classTeaching.setAcadyear(acadyear);
					classTeaching.setSemester(semester);
					classTeaching.setCreationTime(new Date());
					classTeaching.setModifyTime(new Date());
					classTeaching.setEventSource(0);
					classTeaching.setIsDeleted(Constant.IS_FALSE);
					classTeaching.setSubjectType(gradeTeachingDto.getSubjectType());
					classTeaching.setOperatorId(operatorId);
					classTeaching.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
					classTeaching.setCredit(subjectIdToCourse.get(gradeTeachingDto.getSubjectId()).getInitCredit());
					classTeaching.setFullMark(subjectIdToCourse.get(gradeTeachingDto.getSubjectId()).getFullMark());
					classTeaching.setPassMark(subjectIdToCourse.get(gradeTeachingDto.getSubjectId()).getInitPassMark());
					if(BaseConstants.SUBJECT_TYPE_XX.equals(gradeTeachingDto.getSubjectType())) {
						classTeaching.setIsTeaCls(Constant.IS_TRUE);
						classTeaching.setPunchCard(Constant.IS_FALSE);
					}else {
						//必修 设置是否考勤
						classTeaching.setIsTeaCls(Constant.IS_FALSE);
						classTeaching.setPunchCard(Constant.IS_TRUE);
					}
					newClassTeachingList.add(classTeaching);
				}
				
			}
			
			String msg = classTeachingService.deleteAndSave(acadyear,semester,classId,unitId,delSubjectIds.toArray(new String[delSubjectIds.size()]),newClassTeachingList,delClassTeachingList);
			if(StringUtils.isNotBlank(msg)){
				return error(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/class/courseopen/save")
	@ControllerInfo(value = "班级课程开设修改保存")
	public String doSaveClassCourseOpenList(GradeTeachingListDto gradeTeachingListDto,HttpServletRequest request) {
		String acadyear = gradeTeachingListDto.getAcadyear();
		String semester = gradeTeachingListDto.getSemester();
		String classId = gradeTeachingListDto.getClassId();
		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();
		String operatorId = loginInfo.getUserId();
		List<GradeTeachingDto> optionaldCourseList = gradeTeachingListDto.getOptionaldCourseList();
		List<GradeTeachingDto> requiredCourseList = gradeTeachingListDto.getRequiredCourseList();
		
		if(optionaldCourseList.size() + requiredCourseList.size() == 0) {
			return error("没有要保存的数据");
		}
		
		List<String> classTeachingIds = new ArrayList<String>();
		for (GradeTeachingDto gradeTeachingDto : optionaldCourseList) {
			classTeachingIds.add(gradeTeachingDto.getId());
		}
		
		for (GradeTeachingDto gradeTeachingDto : requiredCourseList) {
			classTeachingIds.add(gradeTeachingDto.getId());
		}
		
		List<ClassTeaching> classTeachingList = classTeachingService.findListByIdIn(classTeachingIds.toArray(new String[classTeachingIds.size()]));
		Map<String, ClassTeaching> idToClassTeachingMap = EntityUtils.getMap(classTeachingList, ClassTeaching::getId);
		List<String> subjectIdList = new ArrayList<String>();
		//必修
		boolean flag = false;
		for (GradeTeachingDto gradeTeachingDto : requiredCourseList) {
			String id = gradeTeachingDto.getId();
			Integer punchCard = gradeTeachingDto.getPunchCard();
			ClassTeaching classTeaching = idToClassTeachingMap.get(id);
			if(Constant.IS_TRUE==gradeTeachingDto.getIsTeaCls()){
				if(classTeaching.getSubjectType().equals(BaseConstants.SUBJECT_TYPE_VIRTUAL)){
					flag=true;
					continue;
				}
				subjectIdList.add(classTeaching.getSubjectId());
			}
			if(punchCard != null) {
				classTeaching.setPunchCard(gradeTeachingDto.getPunchCard());
			}
			classTeaching.setCourseHour(gradeTeachingDto.getCourseHour());
			classTeaching.setIsTeaCls(gradeTeachingDto.getIsTeaCls());
			classTeaching.setWeekType(gradeTeachingDto.getWeekType());
			classTeaching.setModifyTime(new Date());
			classTeaching.setOperatorId(operatorId);
			classTeaching.setCredit(gradeTeachingDto.getCredit());
		}
		//选修
		for (GradeTeachingDto gradeTeachingDto : optionaldCourseList) {
			String id = gradeTeachingDto.getId();
			ClassTeaching classTeaching = idToClassTeachingMap.get(id);
			classTeaching.setCourseHour(gradeTeachingDto.getCourseHour());
			classTeaching.setCredit(gradeTeachingDto.getCredit());
			classTeaching.setModifyTime(new Date());
			classTeaching.setOperatorId(operatorId);
		}
		try {
			String msg = classTeachingService.deleteAndSave(acadyear, semester, classId, unitId, subjectIdList.toArray(new String[subjectIdList.size()]), classTeachingList, null);
			if(StringUtils.isNotBlank(msg)){
				return error(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		if(flag){
			return success("除走班课程不能更改为以教学班形式开设之外其他课程保存成功");
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/class/courseopen/delete")
	@ControllerInfo(value = "班级开设科目删除")
	public String dodeleteClassTeaching(String classTeachingId ) {
		try {
			//同时删除行政班该科目课表
			ClassTeaching ct = classTeachingService.findOne(classTeachingId);
			ct.setIsDeleted(Constant.IS_DELETED_TRUE);
			ct.setOperatorId(getLoginInfo().getUserId());
			ct.setModifyTime(new Date());
			List<ClassTeaching> delCtList = new ArrayList<ClassTeaching>();
			delCtList.add(ct);
			String msg = classTeachingService.deleteAndSave(ct.getAcadyear(), ct.getSemester(), ct.getClassId(), ct.getUnitId(), new String[]{ct.getSubjectId()}, null, delCtList);
			if(StringUtils.isNotBlank(msg)){
				return error(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@RequestMapping("/courseopen/teach/setting/index/page")
	@ControllerInfo("任课教师设置首页")
	public String showTeachSettingIndexPage(String acadyear, String semester, String gradeId, ModelMap map) {
		List<String> acadyearList = semesterService.findAcadeyearList();
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        String unitId = getLoginInfo().getUnitId();
        if(StringUtils.isBlank(semester)){
        	Semester se = semesterService.findCurrentSemester(2,unitId);
        	if(se!=null){
        		acadyear=se.getAcadyear();
        		semester=String.valueOf(se.getSemester()); 
        	}
        }
        List<Grade> gradeList = gradeService.findByUnitId(unitId);
        
        map.put("acadyearList", acadyearList);
        map.put("acadyear", acadyear);
        map.put("semester", semester);
        map.put("gradeId", gradeId);
		map.put("gradeList", gradeList);
		return "/basedata/classTeaching/teachSettingIndex.ftl";
	}
	
	@RequestMapping("/courseopen/teach/setting/page")
	@ControllerInfo("任课教师设置详情页")
	public String showTeachSettingPage(HttpServletRequest request, String useMaster, ModelMap map) {
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String gradeId = request.getParameter("gradeId");
		String subjectId = request.getParameter("subjectId");
		String unitId = getLoginInfo().getUnitId();
		
		if(StringUtils.isBlank(gradeId)) {
			return "/basedata/classTeaching/teachSetting.ftl";
		}
		List<Clazz> clazzList = classService.findByGradeId(unitId, gradeId, null);
		if(CollectionUtils.isEmpty(clazzList)){
			return "/basedata/classTeaching/teachSetting.ftl";
		}
		
		Map<String, String> classIdToClassName = EntityUtils.getMap(clazzList, Clazz::getId, Clazz::getClassNameDynamic);
		List<String> classIdList = EntityUtils.getList(clazzList, Clazz::getId);
		
		OpenTeachingSearchDto openTeachingSearchDto = new OpenTeachingSearchDto();
		openTeachingSearchDto.setAcadyear(acadyear);
		openTeachingSearchDto.setSemester(semester);
		openTeachingSearchDto.setSubjectId(subjectId);
		openTeachingSearchDto.setIsTeaCls(0);
		openTeachingSearchDto.setIsDeleted(0);
		openTeachingSearchDto.setUnitId(unitId);
		openTeachingSearchDto.setClassIds(classIdList.toArray(new String[classIdList.size()]));
		List<ClassTeaching> classTeachingList = null;
		if(Objects.equals(useMaster, "1")) {
			classTeachingList = classTeachingService.findBySearchWithMaster(openTeachingSearchDto, true);
		}else {
			classTeachingList = classTeachingService.findBySearch(openTeachingSearchDto, true);
		}
		
		if(CollectionUtils.isNotEmpty(classTeachingList)){
			List<Course> courseList = courseService.findListByIds(EntityUtils.getList(classTeachingList, ClassTeaching::getSubjectId).toArray(new String[0]));
			Map<String, String> courseTypeMap = courseList.stream().filter(e->StringUtils.isNotBlank(e.getCourseTypeId())).collect(Collectors.toMap(Course::getId, Course::getCourseTypeId));
			if(MapUtils.isNotEmpty(courseTypeMap)){
				classTeachingList = classTeachingList.stream().filter(e->!BaseConstants.VIRTUAL_COURSE_TYPE.equals(courseTypeMap.get(e.getSubjectId()))).collect(Collectors.toList());
			}
			
			Stream<String> mainTidstm = classTeachingList.stream().filter(e->e.getTeacherId()!=null).map(e->e.getTeacherId());
			Stream<String> tidstm = classTeachingList.stream().filter(e->e.getTeacherIds()!=null).flatMap(e->e.getTeacherIds().stream());
			Set<String> teacherIdSet = Stream.concat(mainTidstm, tidstm).collect(Collectors.toSet());
			
			Set<String> subids = EntityUtils.getSet(classTeachingList, ClassTeaching::getSubjectId);
			Map<String, String> subMap= courseService.findPartCouByIds(subids.toArray(new String[0]));
			List<Teacher> teacherList = new ArrayList<Teacher>();
			if(teacherIdSet.size() > 0) {
				teacherList = teacherService.findListByIdIn(teacherIdSet.toArray(new String[teacherIdSet.size()]));
			}
			Map<String,String> teacherIdToTeacherName = EntityUtils.getMap(teacherList, Teacher::getId, Teacher::getTeacherName);
			
			List<TeachSettingDto> teachSettingDtoList = new ArrayList<TeachSettingDto>();
			List<String> delctids =new ArrayList<>();
			for (ClassTeaching classTeaching : classTeachingList) {
				TeachSettingDto teachSettingDto = new TeachSettingDto();
				String classId = classTeaching.getClassId();
				teachSettingDto.setClazzId(classId);
				teachSettingDto.setSubjectId(classTeaching.getSubjectId());
				teachSettingDto.setClassTeachingId(classTeaching.getId());
				String classname =classIdToClassName.get(classId);
				if(StringUtils.isNotBlank(classname)){
					teachSettingDto.setClassName(classname);
				}else{
					delctids.add(classTeaching.getId());
					continue;
				}
				
				teachSettingDto.setMainTeacherId(classTeaching.getTeacherId());
				teachSettingDto.setMainTeacherName(teacherIdToTeacherName.get(classTeaching.getTeacherId()));
				teachSettingDto.setSubName(subMap.get(classTeaching.getSubjectId()));
				Set<String> teacherIds = classTeaching.getTeacherIds();
				List<TeacherDto> teacherDtoList = new ArrayList<TeacherDto>();
				if(teacherIds != null && teacherIds.size() > 0) {
					teachSettingDto.setOtherTeacherIds(String.join(",", teacherIds));
					for (String string : teacherIds) {
						TeacherDto teacherDto = new TeacherDto();
						teacherDto.setTeacherId(string);
						teacherDto.setTeacherName(teacherIdToTeacherName.get(string));
						teacherDtoList.add(teacherDto);
					}
				}
				teachSettingDto.setTeacherDtoList(teacherDtoList);
				teachSettingDtoList.add(teachSettingDto);
			}
			if(CollectionUtils.isNotEmpty(delctids)){
				classTeachingService.deleteClassTeachCouSch(delctids.toArray(new String[0]));
			}
			map.put("teachSettingDtoList", teachSettingDtoList);
		}
		return "/basedata/classTeaching/teachSetting.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/courseopen/main/teacher/save")
	@ControllerInfo("主任课老师保存")
	public String doMainTeacherSave(String acadyear, String semester, String classTeachingId, String teacherId) {
		
		try {
			String unitId = getLoginInfo().getUnitId();
			String[] classTeachingIds = classTeachingId.split(",");
			
			Teacher teacher = teacherService.findOne(teacherId);
			List<ClassTeachingEx> classTeachingExList = classTeachingExService.findByClassTeachingIdIn(classTeachingIds);
			Map<String, List<String>> classTeachingId2teacherIds = EntityUtils.getListMap(classTeachingExList, ClassTeachingEx::getClassTeachingId, ClassTeachingEx::getTeacherId);
			for (String ctId : classTeachingIds) {
				if(classTeachingId2teacherIds.containsKey(ctId)){
					if(classTeachingId2teacherIds.get(ctId).contains(teacherId)){
						return error(teacher.getTeacherName()+"老师不能同时在一个班级担任主任课老师和助教老师");
					}
				}
			}
			
			List<ClassTeaching> classTeachingList = classTeachingService.findListByIds(classTeachingIds);
			Set<String> classIds = EntityUtils.getSet(classTeachingList, ClassTeaching::getClassId);
			Set<String> subjectIds = EntityUtils.getSet(classTeachingList, ClassTeaching::getSubjectId);
			List<String> classIdSubjectId = classTeachingList.parallelStream().map(e->e.getClassId()+e.getSubjectId()).collect(Collectors.toList());
			
			//处理课表数据
			Semester se=semesterService.findByAcadyearAndSemester(acadyear,Integer.parseInt(semester),unitId);
			Date nowDate = new Date();
			List<CourseSchedule> classCsList = new ArrayList<CourseSchedule>();
			if(DateUtils.compareForDay(nowDate, se.getSemesterEnd())<0){//学期未结束
				Map<String,List<CourseSchedule>> teacherTimeMap = new HashMap<String, List<CourseSchedule>>();
				//各班科目课表
				classCsList = courseScheduleService.findCourseScheduleListByClassIdsAndSubjectIds(unitId,acadyear, Integer.valueOf(semester), classIds.toArray(new String[0]),subjectIds.toArray(new String[0]));
				if(CollectionUtils.isNotEmpty(classCsList)){
					List<CourseSchedule> allCsList = new ArrayList<CourseSchedule>();
					classCsList = classCsList.parallelStream().filter(e->!teacherId.equals(e.getTeacherId())&&classIdSubjectId.contains(e.getClassId()+e.getSubjectId())).collect(Collectors.toList());
					allCsList.addAll(classCsList);
					//该老师课表
					List<CourseSchedule> teacherCsList = courseScheduleService.findCourseScheduleListByTeacherId(acadyear, Integer.parseInt(semester), teacherId, null);
					if(CollectionUtils.isNotEmpty(teacherCsList)){
						allCsList.addAll(teacherCsList);
					}
					if(DateUtils.compareForDay(nowDate, se.getSemesterBegin())>0){//学期已开始
						DateInfo nowDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), nowDate);
						if(nowDateInfo==null){
							return error("未维护当前选择的学年学期内的节假日信息");
						}
						Iterator<CourseSchedule> iterator = allCsList.iterator();
						while(iterator.hasNext()){
							CourseSchedule cs = iterator.next();
							if((cs.getWeekOfWorktime()<nowDateInfo.getWeek()) || (cs.getWeekOfWorktime()==nowDateInfo.getWeek() && cs.getDayOfWeek()+1<=nowDateInfo.getWeekday())){
								iterator.remove();
							}
						}
					}
					for (CourseSchedule cs : allCsList) {
						String key = cs.getWeekOfWorktime()+"_"+cs.getDayOfWeek()+"_"+cs.getPeriodInterval()+"_"+cs.getPeriod();
						if(!teacherTimeMap.containsKey(key)){
							teacherTimeMap.put(key, new ArrayList<CourseSchedule>());
						}
						teacherTimeMap.get(key).add(cs);
					}
					for (Entry<String, List<CourseSchedule>> entry : teacherTimeMap.entrySet()) {
						if(entry.getValue().size()>1){
							return error(teacher.getTeacherName()+"老师上课时间有冲突");
						}
					}
					for (CourseSchedule cs : classCsList) {
						cs.setTeacherId(teacherId);
					}
				}
			}
			
			classTeachingList.forEach(e->e.setTeacherId(teacherId));
			
			courseScheduleService.updateClassTeaching(null, null, null ,classTeachingList, null, classCsList, null);
		
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/courseopen/other/teacher/save")
	@ControllerInfo("助教老师保存")
	public String doOtherTeacherSave(String acadyear, String semester, String classTeachingId, String teacherId) {
		String unitId = getLoginInfo().getUnitId();
		String[] classTeachingIds = classTeachingId.split(",");
		List<String> teacherIds= Stream.of(teacherId.split(",")).collect(Collectors.toList());
		List<TeachPlanEx> teachPlanExList = new ArrayList<TeachPlanEx>();
		List<ClassTeachingEx> classTeachingExSaveList = new ArrayList<ClassTeachingEx>();
		try {
			String msg = makeData(acadyear, Integer.parseInt(semester), unitId, classTeachingIds, teacherIds, teachPlanExList, classTeachingExSaveList);
			if(StringUtils.isNotBlank(msg)){
				return msg;
			}
			courseScheduleService.updateClassTeaching(null, null, null, null, classTeachingExSaveList, null, teachPlanExList);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/courseopen/delete/main/teacher")
	@ControllerInfo("主任课老师删除")
	public String doDeleteMainTeacher(String classTeachingId) {
		try {
			ClassTeaching classTeaching = classTeachingService.findOne(classTeachingId);
			String acadyear = classTeaching.getAcadyear();
			Integer semester = Integer.parseInt(classTeaching.getSemester());
			String unitId = classTeaching.getUnitId();
			String classId = classTeaching.getClassId();
			String subjectId = classTeaching.getSubjectId();
			Semester se=semesterService.findByAcadyearAndSemester(acadyear,semester,unitId);
			Date nowDate = new Date();
			List<CourseSchedule> csList = new ArrayList<CourseSchedule>();
			if(DateUtils.compareForDay(nowDate, se.getSemesterEnd())<0){//学期未结束
				csList = courseScheduleService.findByClassIdAndSubjectIdAndTeacherId(unitId, acadyear, semester, 
						classId, subjectId,classTeaching.getTeacherId());
				if(DateUtils.compareForDay(nowDate, se.getSemesterBegin())>0){//学期已开始
					DateInfo nowDateInfo = dateInfoService.getDate(unitId, acadyear, semester, nowDate);
					if(nowDateInfo==null){
						return error("未维护当前选择的学年学期内的节假日信息");
					}
					Iterator<CourseSchedule> iterator = csList.iterator();
					while(iterator.hasNext()){
						CourseSchedule cs = iterator.next();
						if((cs.getWeekOfWorktime()<nowDateInfo.getWeek()) || (cs.getWeekOfWorktime()==nowDateInfo.getWeek() && cs.getDayOfWeek()+1<=nowDateInfo.getWeekday())){
							iterator.remove();
						}
					}
				}
				for (CourseSchedule cs : csList) {
					cs.setTeacherId(null);
				}
			}
			classTeaching.setTeacherId(null);
			ArrayList<ClassTeaching> ctList = new ArrayList<ClassTeaching>();
			ctList.add(classTeaching);
			courseScheduleService.updateClassTeaching(null, null, null, ctList, null, csList, null);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/courseopen/delete/other/teacher")
	@ControllerInfo("助教老师删除")
	public String doDeleteOtherTeacher(String classTeachingId, String teacherId, String oldTeacherId) {
		try {
			List<String> teacherIds = new ArrayList<String>();
			if(StringUtils.isNotBlank(teacherId)){
				teacherIds = Stream.of(teacherId.split(",")).collect(Collectors.toList());
			}
			List<String> oldTeacherIds = new ArrayList<String>();
			if(StringUtils.isNotBlank(oldTeacherId)){
				oldTeacherIds = Stream.of(oldTeacherId.split(",")).collect(Collectors.toList());
			}
			@SuppressWarnings("unchecked")
			List<String> retain = ListUtils.retainAll(teacherIds, oldTeacherIds);
			teacherIds.removeAll(retain);
			oldTeacherIds.removeAll(retain);
			
			ClassTeaching classTeaching = classTeachingService.findOne(classTeachingId);
			String acadyear = classTeaching.getAcadyear();
			Integer semester = Integer.parseInt(classTeaching.getSemester());
			String unitId = classTeaching.getUnitId();
			String classId = classTeaching.getClassId();
			String subjectId = classTeaching.getSubjectId();
			
			Semester se=semesterService.findByAcadyearAndSemester(acadyear,semester,unitId);
			Date nowDate = new Date();
			
			List<TeachPlanEx> teachPlanExList = new ArrayList<TeachPlanEx>();
			List<ClassTeachingEx> classTeachingExSaveList = new ArrayList<ClassTeachingEx>();
			
			//处理新数据
			if(CollectionUtils.isNotEmpty(teacherIds)){
				if(StringUtils.isNotBlank(classTeaching.getTeacherId())){
					if(teacherIds.contains(classTeaching.getTeacherId())){
						Teacher teacher = teacherService.findOne(classTeaching.getTeacherId());
						return error(teacher.getTeacherName()+"老师不能在一个班级同时担任主任课老师和助教老师");
					}
				}
				ClassTeachingEx classTeachingEx; 
				for (String tId : teacherIds) {
					classTeachingEx = new ClassTeachingEx();
					classTeachingEx.setId(UuidUtils.generateUuid());
					classTeachingEx.setClassTeachingId(classTeaching.getId());
					classTeachingEx.setTeacherId(tId);
					classTeachingExSaveList.add(classTeachingEx);
				}
				if(DateUtils.compareForDay(nowDate, se.getSemesterEnd())<0){//学期未结束
					List<Teacher> teacherList = teacherService.findListByIdIn(teacherIds.toArray(new String[0]));
					Map<String,String> teacherNameMap = EntityUtils.getMap(teacherList, Teacher::getId, Teacher::getTeacherName);
					Map<String,List<CourseSchedule>> teacherScheduleMap = courseScheduleService.findCourseScheduleMapByTeacherId(acadyear, String.valueOf(semester), 
							teacherIds.toArray(new String[0]),null);
					//各班科目课表
					List<CourseSchedule> classCsList = courseScheduleService.findCourseScheduleListByClassIdsAndSubjectIds(unitId,acadyear, 
							Integer.valueOf(semester), new String[]{classTeaching.getClassId()}, new String[]{classTeaching.getSubjectId()});
					if(CollectionUtils.isNotEmpty(classCsList)){
						String tid = null;
						List<CourseSchedule> allCsList = null;
						Map<String,List<CourseSchedule>> teacherTimeMap = null;
						TeachPlanEx teachPlanEx = null;
						for (Entry<String, List<CourseSchedule>> entry : teacherScheduleMap.entrySet()) {
							tid = entry.getKey();
							allCsList = new ArrayList<CourseSchedule>();
							allCsList.addAll(classCsList);
							if(CollectionUtils.isNotEmpty(entry.getValue())){
								allCsList.addAll(entry.getValue());
							}
							if(DateUtils.compareForDay(nowDate, se.getSemesterBegin())>0){//学期已开始
								DateInfo nowDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), nowDate);
								if(nowDateInfo==null){
									return error("未维护当前选择的学年学期内的节假日信息");
								}
								Iterator<CourseSchedule> iterator = allCsList.iterator();
								while(iterator.hasNext()){
									CourseSchedule cs = iterator.next();
									if((cs.getWeekOfWorktime()<nowDateInfo.getWeek()) || (cs.getWeekOfWorktime()==nowDateInfo.getWeek() && cs.getDayOfWeek()+1<=nowDateInfo.getWeekday())){
										iterator.remove();
									}
								}
								
								Iterator<CourseSchedule> iterator2 = classCsList.iterator();
								while(iterator2.hasNext()){
									CourseSchedule cs = iterator2.next();
									if((cs.getWeekOfWorktime()<nowDateInfo.getWeek()) || (cs.getWeekOfWorktime()==nowDateInfo.getWeek() && cs.getDayOfWeek()+1<=nowDateInfo.getWeekday())){
										iterator2.remove();
									}
								}
							}
							teacherTimeMap = new HashMap<String, List<CourseSchedule>>();
							for (CourseSchedule cs : allCsList) {
								String key = cs.getWeekOfWorktime()+"_"+cs.getDayOfWeek()+"_"+cs.getPeriodInterval()+"_"+cs.getPeriod();
								if(!teacherTimeMap.containsKey(key)){
									teacherTimeMap.put(key, new ArrayList<CourseSchedule>());
								}
								teacherTimeMap.get(key).add(cs);
							}
							for (Entry<String, List<CourseSchedule>> entry2 : teacherTimeMap.entrySet()) {
								if(entry2.getValue().size()>1){
									return error(teacherNameMap.get(tid)+"老师上课时间有冲突");
								}
							}
							
							for (CourseSchedule cs : classCsList) {
								teachPlanEx = new TeachPlanEx();
								teachPlanEx.setId(UuidUtils.generateUuid());
								teachPlanEx.setAcadyear(acadyear);
								teachPlanEx.setSemester(semester);
								teachPlanEx.setPrimaryTableId(cs.getId());
								teachPlanEx.setTeacherId(tid);
								teachPlanEx.setUnitId(unitId);
								teachPlanEx.setType("2");
								teachPlanExList.add(teachPlanEx);
							}
						}
					}
				}
				
			}
			
			//处理旧数据
			List<String> timetableIds = new ArrayList<String>();
			if(CollectionUtils.isNotEmpty(oldTeacherIds)){
				if(DateUtils.compareForDay(nowDate, se.getSemesterEnd())<0){//学期未结束
					List<CourseSchedule> csList = courseScheduleService.findCourseScheduleListByClassIdsAndSubjectIds(unitId, acadyear,
							semester,new String[]{classId}, new String[]{subjectId});
					if(DateUtils.compareForDay(nowDate, se.getSemesterBegin())>0){//学期已开始
						DateInfo nowDateInfo = dateInfoService.getDate(unitId, acadyear, semester, nowDate);
						if(nowDateInfo==null){
							return error("未维护当前选择的学年学期内的节假日信息");
						}
						Iterator<CourseSchedule> iterator = csList.iterator();
						while(iterator.hasNext()){
							CourseSchedule cs = iterator.next();
							if((cs.getWeekOfWorktime()<nowDateInfo.getWeek()) || (cs.getWeekOfWorktime()==nowDateInfo.getWeek() && cs.getDayOfWeek()+1<=nowDateInfo.getWeekday())){
								iterator.remove();
							}
						}
					}
				}
			}
			
			courseScheduleService.updateClassTeaching(classTeachingId, oldTeacherIds, timetableIds , null, classTeachingExSaveList, null, teachPlanExList);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}

	@ResponseBody
	@RequestMapping("/courseopen/classByGradeId")
	@ControllerInfo("根据年级Id获取班级列表")
	public String doGetClassByGradeId(String gradeId) {
		List<Clazz> clazzList = classService.findByGradeIdIn(new String[]{gradeId});
		JSONObject json=new JSONObject();
		JSONArray jsonArr=new JSONArray();
		for (Clazz clazz : clazzList) {
			JSONObject tempJson = new JSONObject();
			tempJson.put("classId", clazz.getId());
			tempJson.put("className", clazz.getClassName());
			jsonArr.add(tempJson);
		}
		json.put("jsonArr", jsonArr);
		return json.toJSONString();
	}

	@ResponseBody
	@RequestMapping("/courseopen/getSubject")
	@ControllerInfo("根据年级Id获取科目列表")
	public String getSubjectList(String gradeId){
		String schoolId = getLoginInfo().getUnitId();
		Grade grade = gradeService.findById(gradeId);
		List<Course> courseList = courseService.findByUnitIdAndTypeAndLikeSection(schoolId,BaseConstants.SUBJECT_TYPE_BX,grade.getSection()+"");
		courseList = courseList.stream().filter(e->!BaseConstants.VIRTUAL_COURSE_TYPE.equals(e.getCourseTypeId())).collect(Collectors.toList());
		JSONObject json=new JSONObject();
		JSONArray jsonArr=new JSONArray();
		
		JSONObject tempJson = new JSONObject();
		tempJson.put("courseId", "");
		tempJson.put("courseName", "--全部--");
		jsonArr.add(tempJson);
		for (Course course : courseList) {
			tempJson = new JSONObject();
			tempJson.put("courseId", course.getId());
			tempJson.put("courseName", course.getSubjectName());
			jsonArr.add(tempJson);
		}
		json.put("jsonArr", jsonArr);
		return json.toJSONString();
	}

	@ResponseBody
    @RequestMapping("/getWeek/json")
	@ControllerInfo("根据学年学期获取周次")
    public String getWeek(String acadyear, String semester) {
    	String unitId = getLoginInfo().getUnitId();
     	Map<String, Integer> cur2Max =schoolCalendarService.findCurrentWeekAndMaxWeek(acadyear, semester, unitId);
         Integer week = cur2Max.get("current");
         if(week==null){
         	week=1;
         }
         JSONObject jo = new JSONObject();
         jo.put("week", week);
         jo.put("max", cur2Max.get("max"));
    	return jo.toJSONString();
    }

	/**
	 * 任课老师数据组装
	 */
	private String makeData(String acadyear, Integer semester, String unitId, String[] classTeachingIds, List<String> teacherIds, List<TeachPlanEx> teachPlanExList,
			List<ClassTeachingEx> classTeachingExSaveList) {
		List<ClassTeaching> classTeachingList = classTeachingService.findListByIdIn(classTeachingIds);
		List<ClassTeachingEx> classTeachingExList = classTeachingExService.findByClassTeachingIdIn(classTeachingIds);
		Map<String, List<String>> classTeachingId2teacherIds = EntityUtils.getListMap(classTeachingExList, ClassTeachingEx::getClassTeachingId, ClassTeachingEx::getTeacherId);
		
		for (ClassTeaching classTeaching : classTeachingList) {
			if(StringUtils.isNotBlank(classTeaching.getTeacherId())){
				if(teacherIds.contains(classTeaching.getTeacherId())){
					Teacher teacher = teacherService.findOne(classTeaching.getTeacherId());
					return error(teacher.getTeacherName()+"老师不能在一个班级同时担任主任课老师和助教老师");
				}
			}
		}
		
		ClassTeachingEx classTeachingEx; 
		for (ClassTeaching classTeaching : classTeachingList) {
			List<String> ctList = classTeachingId2teacherIds.get(classTeaching.getId());
			for (String tId : teacherIds) {
				if(CollectionUtils.isNotEmpty(ctList)&&ctList.contains(tId)){
					continue;
				}
				classTeachingEx = new ClassTeachingEx();
				classTeachingEx.setId(UuidUtils.generateUuid());
				classTeachingEx.setClassTeachingId(classTeaching.getId());
				classTeachingEx.setTeacherId(tId);
				classTeachingExSaveList.add(classTeachingEx);
			}
		}
		
		if(CollectionUtils.isEmpty(classTeachingExSaveList)){
			return returnSuccess();
		}
		
		//处理课表数据
		Semester se=semesterService.findByAcadyearAndSemester(acadyear,semester,unitId);
		Date nowDate = new Date();
		if(DateUtils.compareForDay(nowDate, se.getSemesterEnd())<0){//学期未结束
			List<Teacher> teacherList = teacherService.findListByIdIn(EntityUtils.getSet(classTeachingExSaveList, ClassTeachingEx::getTeacherId).toArray(new String[0]));
			Map<String,String> teacherNameMap = EntityUtils.getMap(teacherList, Teacher::getId, Teacher::getTeacherName);
			Map<String, ClassTeaching> classTeachingMap = EntityUtils.getMap(classTeachingList, ClassTeaching::getId);
			Map<String, List<String>> teacherId2ClaSubIds = new HashMap<String, List<String>>();
			Set<String> classIdSet = new HashSet<String>();
			Set<String> subjectIdSet = new HashSet<String>();
			Set<String> claSubIdSet = new HashSet<String>();
			for (ClassTeachingEx ex : classTeachingExSaveList) {
				if(!teacherId2ClaSubIds.containsKey(ex.getTeacherId())){
					teacherId2ClaSubIds.put(ex.getTeacherId(), new ArrayList<String>());
				}
				ClassTeaching classTeaching = classTeachingMap.get(ex.getClassTeachingId());
				teacherId2ClaSubIds.get(ex.getTeacherId()).add(classTeaching.getClassId()+classTeaching.getSubjectId());
				claSubIdSet.add(classTeaching.getClassId()+classTeaching.getSubjectId());
				classIdSet.add(classTeaching.getClassId());
				subjectIdSet.add(classTeaching.getSubjectId());
			}
			Map<String,List<CourseSchedule>> teacherScheduleMap = courseScheduleService.findCourseScheduleMapByTeacherId(acadyear, String.valueOf(semester), 
					teacherId2ClaSubIds.keySet().toArray(new String[0]),null);
			//各班科目课表
			List<CourseSchedule> classScheduleList = courseScheduleService.findCourseScheduleListByClassIdsAndSubjectIds(unitId,acadyear, 
					Integer.valueOf(semester), classIdSet.toArray(new String[classIdSet.size()]), subjectIdSet.toArray(new String[subjectIdSet.size()]));
			classScheduleList = classScheduleList.stream().filter(e->claSubIdSet.contains(e.getClassId()+e.getSubjectId())).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(classScheduleList)){
				String tid = null;
				List<CourseSchedule> allCsList = null;
				List<CourseSchedule> classCsList = null;
				Map<String,List<CourseSchedule>> teacherTimeMap = null;
				TeachPlanEx teachPlanEx = null;
				for (Entry<String, List<String>> entry : teacherId2ClaSubIds.entrySet()) {
					tid = entry.getKey();
					List<String> claSubIdList = entry.getValue();
					classCsList = classScheduleList.stream().filter(e->claSubIdList.contains(e.getClassId()+e.getSubjectId())).collect(Collectors.toList());
					if(CollectionUtils.isNotEmpty(classCsList)){
						allCsList = new ArrayList<CourseSchedule>();
						allCsList.addAll(classCsList);
						if(CollectionUtils.isNotEmpty(teacherScheduleMap.get(tid))){
							allCsList.addAll(teacherScheduleMap.get(tid));
						}
						if(DateUtils.compareForDay(nowDate, se.getSemesterBegin())>0){//学期已开始
							DateInfo nowDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), nowDate);
							if(nowDateInfo==null){
								return error("未维护当前选择的学年学期内的节假日信息");
							}
							Iterator<CourseSchedule> iterator = allCsList.iterator();
							while(iterator.hasNext()){
								CourseSchedule cs = iterator.next();
								if((cs.getWeekOfWorktime()<nowDateInfo.getWeek()) || (cs.getWeekOfWorktime()==nowDateInfo.getWeek() && cs.getDayOfWeek()+1<=nowDateInfo.getWeekday())){
									iterator.remove();
								}
							}
							
							Iterator<CourseSchedule> iterator2 = classCsList.iterator();
							while(iterator2.hasNext()){
								CourseSchedule cs = iterator2.next();
								if((cs.getWeekOfWorktime()<nowDateInfo.getWeek()) || (cs.getWeekOfWorktime()==nowDateInfo.getWeek() && cs.getDayOfWeek()+1<=nowDateInfo.getWeekday())){
									iterator2.remove();
								}
							}
						}
						teacherTimeMap = new HashMap<String, List<CourseSchedule>>();
						for (CourseSchedule cs : allCsList) {
							String key = cs.getWeekOfWorktime()+"_"+cs.getDayOfWeek()+"_"+cs.getPeriodInterval()+"_"+cs.getPeriod();
							if(!teacherTimeMap.containsKey(key)){
								teacherTimeMap.put(key, new ArrayList<CourseSchedule>());
							}
							teacherTimeMap.get(key).add(cs);
						}
						for (Entry<String, List<CourseSchedule>> entry2 : teacherTimeMap.entrySet()) {
							if(entry2.getValue().size()>1){
								return error(teacherNameMap.get(tid)+"老师上课时间有冲突");
							}
						}
						
						for (CourseSchedule cs : classCsList) {
							teachPlanEx = new TeachPlanEx();
							teachPlanEx.setId(UuidUtils.generateUuid());
							teachPlanEx.setAcadyear(acadyear);
							teachPlanEx.setSemester(semester);
							teachPlanEx.setPrimaryTableId(cs.getId());
							teachPlanEx.setTeacherId(tid);
							teachPlanEx.setUnitId(unitId);
							teachPlanEx.setType("2");
							teachPlanExList.add(teachPlanEx);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获取实际的课程类型
	 * @param subjectType
	 * @param xxType
	 * @return
	 */
	private String[] getRealTypes(String subjectType, String xxType){
		String[] types;
		if(BaseConstants.SUBJECT_TYPE_XX.equals(subjectType)){
			if(StringUtils.isBlank(xxType)){
				types  = new String[]{BaseConstants.SUBJECT_TYPE_XX, BaseConstants.SUBJECT_TYPE_XX_4, BaseConstants.SUBJECT_TYPE_XX_5, BaseConstants.SUBJECT_TYPE_XX_6};
			}else{
				types = new String[]{xxType};
			}
		}else{
			types = new String[]{BaseConstants.SUBJECT_TYPE_BX, BaseConstants.SUBJECT_TYPE_VIRTUAL};
		}
		return types;
	}
}
