package net.zdsoft.basedata.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.constant.TipsayConstants;
import net.zdsoft.basedata.dto.ClassFeatureDto;
import net.zdsoft.basedata.dto.PeriodSwapDto;
import net.zdsoft.basedata.dto.ScheduleSwapDto;
import net.zdsoft.basedata.dto.TeachGroupDto;
import net.zdsoft.basedata.entity.Adjusted;
import net.zdsoft.basedata.entity.AdjustedDetail;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.TipsayEx;
import net.zdsoft.basedata.remote.utils.BusinessUtils;
import net.zdsoft.basedata.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 课表调整。包括 新高考排课系统 和 基础数据课表调整相关入口
 * @author wangyy
 *
 */
@RequestMapping("/basedata/scheduleModify/mutil/{gradeId}")
@Controller
public class CourseScheduleMutilModifyAction extends BaseAction{
	@Autowired
	private GradeService gradeService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TeachPlaceService teachPlaceService;
	@Autowired
	private ClassService classService;
	@Autowired
	private SchoolCalendarService schoolCalendarService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private TeachClassStuService teachClassStuService;
	@Autowired
	private AdjustedService adjustedService;
	@Autowired
	private StudentService studentService;
	@Autowired
    private TipsayService tipsayService;
	@Autowired
	private ClassTeachingService classTeachingService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private TeachGroupService teachGroupService;

	
	public static final int WEEK_TYPE_ODD = 1; //单周
	public static final int WEEK_TYPE_EVEN = 2; //双周
	public static final int WEEK_TYPE_NORMAL = 3; //正常
	
	/**
	 * 
	 * @param objId 智能排课则 为arrayId,基础数据则是 gradeId
	 * @param location 处于 智能排课还是 基础数据
	 * @return
	 */
	@RequestMapping("/index")
	public String scheduleModifyIndex(@PathVariable String gradeId, String objId, String searchAcadyear, String searchSemester,
			int weekIndex, String fromAlone, ModelMap map) {
		
		//为了生成课表，需要年级的对象
//		Grade grade = gradeService.findOne(gradeId);
//		List<Clazz> classList = classService.findByGradeId(gradeId);
		String unitId = getLoginInfo().getUnitId();
		List<Clazz> classList = classService.findBySchoolId(unitId);
		List<Grade> gradeList = gradeService.findBySchoolIdAndIsGraduate(unitId, 0);
		Grade grade = gradeList.get(0);
		Map<String, String> gradeNameMap = EntityUtils.getMap(gradeList, e -> e.getId(), e -> e.getGradeName());
        classList = classList.stream().filter(e->gradeNameMap.containsKey(e.getGradeId())
                &&e.getIsDeleted()==0).collect(Collectors.toList());
        classList.stream().filter(e->gradeNameMap.containsKey(e.getGradeId())).forEach(e->e.setClassName(gradeNameMap.get(e.getGradeId())+e.getClassName()));
        Map<String, String> allClassNameMap = EntityUtils.getMap(classList, Clazz::getId,Clazz::getClassName);

		//TODO 先检查是否年级上课时间相同
		for (Grade gradeTemp : gradeList) {
			if(!java.util.Objects.equals(grade.getMornPeriods(),gradeTemp.getMornPeriods())
					|| !java.util.Objects.equals(grade.getAmLessonCount(),gradeTemp.getAmLessonCount())
					|| !java.util.Objects.equals(grade.getPmLessonCount(),gradeTemp.getPmLessonCount())
					|| !java.util.Objects.equals(grade.getNightLessonCount(),gradeTemp.getNightLessonCount())
					|| !java.util.Objects.equals(grade.getWeekDays(),gradeTemp.getWeekDays())){
				return errorFtl(map,"请保持各年级授课时间相同");
			}
		}
		
		Map<String, Integer> cur2Max =schoolCalendarService.findCurrentWeekAndMaxWeek(searchAcadyear, searchSemester, grade.getSchoolId());
        Integer weekNow = cur2Max.get("current");
        Integer weekMax = cur2Max.get("max");
        if(weekNow==null){
        	weekNow=1;
        }
        if(weekMax==null || weekMax<weekNow){
        	map.put("msg", "无法获取本学期周次信息");
        	return "\\basedata\\courseSchedule\\limit.ftl";
        }
        
        List<String> weekList = BusinessUtils.getNumList(weekMax);
        weekList = weekList.subList(weekNow-1, weekList.size());
        if(weekIndex < weekNow)
        	weekIndex = weekNow;


		Map<String,String> objArrayIdMap = new HashMap<>();
        if(Objects.equals("1", fromAlone)) {
        	// 获取所有 场地 和 教师信息
        	List<ClassTeaching> allClassTeachings = classTeachingService.findListByGradeId(searchAcadyear, searchSemester, grade.getSchoolId(), null);
        	Map<String,Map<String,String>> classSubjTeacherMap = new HashMap<>();
        	Set<String> tids = new HashSet<>();
        	for (ClassTeaching ct : allClassTeachings) {
				Map<String, String> subTeaMap = classSubjTeacherMap.computeIfAbsent(ct.getClassId(), k -> new HashMap<>());
				subTeaMap.put(ct.getSubjectId(), ct.getTeacherId());
        		if(StringUtils.isNotBlank(ct.getTeacherId())) {
        			tids.add(ct.getTeacherId());
        		}
        	}
        	List<TeachClass> teachClassList = teachClassService.findBySearch(grade.getSchoolId(), searchAcadyear, searchSemester, null);
        	for (TeachClass tc : teachClassList) {
				Map<String, String> subTeaMap = classSubjTeacherMap.computeIfAbsent(tc.getId(), k -> new HashMap<>());
				subTeaMap.put(tc.getCourseId(), tc.getTeacherId());
        		if(StringUtils.isNotBlank(tc.getTeacherId())) {
        			tids.add(tc.getTeacherId());
        		}
			}
        	
        	List<Teacher> teacherList = teacherService.findByIds(tids.toArray(new String[0]));
        	Map<String,String> teacherNameMap = EntityUtils.getMap(teacherList, Teacher::getId, Teacher::getTeacherName);
        	Map<String,String> classPlaceMap = classList.stream().filter(e->e.getTeachPlaceId()!=null)
        			.collect(Collectors.toMap(Clazz::getId, Clazz::getTeachPlaceId));
        	Set<String> placeIds = EntityUtils.getSet(classList, Clazz::getTeachPlaceId);
        	List<String> usedPlaceIds = courseScheduleService.findAllPlaceIds(grade.getSchoolId(), searchAcadyear, Integer.parseInt(searchSemester), weekIndex, gradeId);
        	placeIds.addAll(usedPlaceIds);
        	
        	List<TeachPlace> placeList = teachPlaceService.findListByIdIn(placeIds.toArray(new String[0]));
			Map<String, String> placeNameMap = placeList.stream().sorted(Comparator.comparing(TeachPlace::getPlaceCode))
					.collect(Collectors.toMap(e -> e.getId(), e -> e.getPlaceName(), (e1, e2) -> e1, () -> new LinkedHashMap<>()));

        	// 根据教研组 去排序
			List<TeachGroupDto> allTeacherGroup = teachGroupService.findAllTeacherGroup(unitId, true);
			Map<String,Integer> tidOrderMap = new HashMap<>();
			AtomicInteger in = new AtomicInteger(0);
			for (TeachGroupDto dto : allTeacherGroup) {
				dto.getMainTeacherList().forEach(e->tidOrderMap.put(e.getTeacherId(),in.getAndAdd(1)));
			}
			in.set(10000);
			Map<String,String> teacherNameTemps = new TreeMap<>(Comparator.comparing(x -> tidOrderMap.computeIfAbsent(x, e -> in.getAndDecrement())));
			teacherNameTemps.putAll(teacherNameMap);
			teacherNameMap = teacherNameTemps;

			map.put("classSubjTeacherMap", classSubjTeacherMap);
        	map.put("teacherNameMap", teacherNameMap);
        	map.put("classPlaceMap", classPlaceMap);
        	map.put("placeNameMap", placeNameMap);
        	map.put("fromAlone", "1");

			for (String tid : teacherNameMap.keySet()) {
				objArrayIdMap.put(tid,grade.getId());
			}
			for (String pid : placeNameMap.keySet()) {
				objArrayIdMap.put(pid,grade.getId());
			}

        	teachClassList.forEach(e->allClassNameMap.put(e.getId(), e.getName()));
        }
       
        Map<String,JSONObject> cnMap = new HashMap<>();
        for (String cid : allClassNameMap.keySet()) {
        	String cn = allClassNameMap.get(cid);
        	JSONObject jsonObject = new JSONObject();
        	jsonObject.put("className", cn);
        	cnMap.put(cid, jsonObject);
		}
		Map<String, Integer> piMap = getIntervalMap(grade);
		
		map.put("piMap", piMap);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
		map.put("weekDays", grade.getWeekDays());
		
		map.put("allClassMap", cnMap);
		map.put("classList", classList);
		map.put("indexId", gradeId);
		map.put("objId", objId);
		
		map.put("weekList", weekList);
		map.put("searchAcadyear", searchAcadyear);
		map.put("searchSemester", searchSemester);
		map.put("weekIndex", weekIndex);

		map.put("sub_sys", "BASE");
		map.put("type", BaseConstants.SCHEDULE_CONTENT_TYPE_C);

		JSONObject jsonObject = new JSONObject();
		for (Clazz clazz : classList) {
			objArrayIdMap.put(clazz.getId(),clazz.getGradeId());
		}

		for (Clazz clazz : classList) {
			objArrayIdMap.put(clazz.getId(),clazz.getGradeId());
		}
		jsonObject.put("objArrayIdMap",objArrayIdMap);
		map.put("jsonDataStr", jsonObject.toJSONString());
		
		return "/basedata/courseSchedule/modify/mutil/modifyIndex.ftl";
	}
	

	private Map<String, Integer> getIntervalMap(Grade grade) {
		Integer mmCount = grade.getMornPeriods();
		Integer amCount = grade.getAmLessonCount();
		Integer pmCount = grade.getPmLessonCount();
		Integer nightCount = grade.getNightLessonCount();
	
		Map<String,Integer> piMap = new LinkedHashMap<>();
		piMap.put(BaseConstants.PERIOD_INTERVAL_1, mmCount);
		piMap.put(BaseConstants.PERIOD_INTERVAL_2, amCount);
		piMap.put(BaseConstants.PERIOD_INTERVAL_3, pmCount);
		piMap.put(BaseConstants.PERIOD_INTERVAL_4, nightCount);
		return piMap;
	}
	
	@RequestMapping("/queryClassOrTeacherTable/page")
	public String classOrTeacher(@PathVariable String gradeId, String cType, String indexId, String objId,
			String searchAcadyear, String searchSemester, int weekIndex, String fromAlone, ModelMap map) {
        if (weekIndex < 1) {
            return errorFtl(map,"周次不存在，请先维护基础信息节假日设置");
        }
		String unitId = getLoginInfo().getUnitId();
		List<Grade> gradeList = gradeService.findBySchoolIdAndIsGraduate(unitId, 0);
		Grade grade = gradeList.get(0);

        Map<String, Integer> cur2Max =schoolCalendarService.findCurrentWeekAndMaxWeek(searchAcadyear, searchSemester, grade.getSchoolId());
        Integer weekNow = cur2Max.get("current");
        if(weekNow==null){
        	weekNow=1;
        }
        Integer weekMax = cur2Max.get("max");
        if(weekMax==null || weekMax<weekNow){
        	return errorFtl(map, "无法获取本学期周次信息");
        }
        
        List<String> weekList = BusinessUtils.getNumList(weekMax);
        weekList = weekList.subList(weekNow-1, weekList.size());
		
		Map<String, Integer> piMap = getIntervalMap(grade);
		
		map.put("piMap", piMap);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("weekList", weekList);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
		map.put("weekDays", grade.getWeekDays());
		map.put("weekIndex", weekIndex);
		map.put("indexId", gradeId);
        
        
		List<CourseSchedule> scheduleList=new ArrayList<CourseSchedule>();
		List<List<ClassFeatureDto>> classSubjectList = new ArrayList<>();
		int weekType= weekIndex%2==0?CourseSchedule.WEEK_TYPE_EVEN:CourseSchedule.WEEK_TYPE_ODD;
        if(BaseConstants.SCHEDULE_CONTENT_TYPE_C.equals(cType)) {
        	 String classId = objId;
             Clazz clazz = classService.findOne(classId);
             if(clazz==null){
             	return error("班级不存在");
             }
            
            scheduleList = courseScheduleService.findCourseScheduleListByClassIdes(searchAcadyear, 
            		Integer.valueOf(searchSemester), new String[] {classId}, weekIndex);
            courseScheduleService.makeScheduleInfo(scheduleList,"1");
            
            Set<String> teacherIdSet = scheduleList.stream().filter(e->StringUtils.isNotBlank(e.getTeacherId())).map(e->e.getTeacherId()).collect(Collectors.toSet());
            List<CourseSchedule> tScheduleList = courseScheduleService.findCourseScheduleListByTeacherIdIn(searchAcadyear, Integer.parseInt(searchSemester),
            		weekIndex, teacherIdSet.toArray(new String[0]));
            Map<String, List<CourseSchedule>> teacherScheduleMap = tScheduleList.stream()
    				.filter(e->StringUtils.isNotBlank(e.getTeacherId()))
    				.collect(Collectors.groupingBy(CourseSchedule::getTeacherId));
    		map.put("teacherScheduleMap", teacherScheduleMap);
    		
    		if("1".equals(fromAlone)) {
    			classSubjectList = makeClassSubs(searchAcadyear, searchSemester, weekType, classId);
    		}
        }else if(BaseConstants.SCHEDULE_CONTENT_TYPE_T.equals(cType)) {
        	String teacherId = objId;
        	scheduleList = courseScheduleService.findCourseScheduleListByTeacherIdIn(searchAcadyear, Integer.parseInt(searchSemester),
        			weekIndex, new String[] {teacherId});
        	courseScheduleService.makeScheduleInfo(scheduleList, "1");
        	
        	if("1".equals(fromAlone)) {
        		classSubjectList = makeTeacherClassSubs(grade.getSchoolId(),searchAcadyear, searchSemester, weekType, teacherId,
        				scheduleList,map);
        	}
        	//TODO 待完善
        	Set<String> movePeriods = scheduleList.stream().filter(e->(CourseSchedule.CLASS_TYPE_TEACH == e.getClassType()
        				|| (CourseSchedule.SUBJECT_TYPE_3+"") == e.getSubjectType()))
        			.map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())
        			.collect(Collectors.toSet());
        	map.put("movePeriods", movePeriods);
        }else if(BaseConstants.SCHEDULE_CONTENT_TYPE_P.equals(cType)){
        	String placeId = objId;
        	scheduleList = courseScheduleService.findCourseScheduleListByPlaceId(grade.getSchoolId(), searchAcadyear, Integer.parseInt(searchSemester),
        			placeId, weekIndex, false);
        	courseScheduleService.makeScheduleInfo(scheduleList, "1");
        	
        	if("1".equals(fromAlone)) {
        		classSubjectList = makePlaceClassSubs(grade.getSchoolId(),searchAcadyear, searchSemester, weekType, placeId,scheduleList,map);
        	}
        	// TODO 待完善
        	Set<String> movePeriods = scheduleList.stream().filter(e->(CourseSchedule.CLASS_TYPE_TEACH == e.getClassType()
    				|| (CourseSchedule.SUBJECT_TYPE_3+"") == e.getSubjectType()))
        			.map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())
        			.collect(Collectors.toSet());
        	map.put("movePeriods", movePeriods);
        	
        	Set<String> teacherIdSet = scheduleList.stream().filter(e->CourseSchedule.CLASS_TYPE_NORMAL == e.getClassType() && StringUtils.isNotBlank(e.getTeacherId())).map(e->e.getTeacherId()).collect(Collectors.toSet());
            List<CourseSchedule> tScheduleList = courseScheduleService.findCourseScheduleListByTeacherIdIn(searchAcadyear, Integer.parseInt(searchSemester),
            		weekIndex, teacherIdSet.toArray(new String[0]));
            Map<String, List<CourseSchedule>> teacherScheduleMap = tScheduleList.stream()
    				.filter(e->StringUtils.isNotBlank(e.getTeacherId()))
    				.collect(Collectors.groupingBy(CourseSchedule::getTeacherId));
    		map.put("teacherScheduleMap", teacherScheduleMap);
    		
    		//
    		String xzbId = classSubjectList.stream().map(e->e.get(0).getClassId()).findFirst().orElse("");
    		if(StringUtils.isNotBlank(xzbId)) {
    			List<CourseSchedule> xzbScheduleList = courseScheduleService.findCourseScheduleListByClassIdes(searchAcadyear, 
                		Integer.valueOf(searchSemester), new String[] {xzbId}, weekIndex);
    			List<CourseSchedule> virtuslSchedules = xzbScheduleList.stream().filter(e->BaseConstants.SUBJECT_TYPE_VIRTUAL.equals(e.getSubjectType()))
    					.collect(Collectors.toList());
    			Map<String, List<String>> classVirtualTimeMap = EntityUtils.getListMap(virtuslSchedules, CourseSchedule::getClassId, e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod());
    			map.put("classVirtualTimeMap", classVirtualTimeMap);
    		}
        }else{
        	return errorFtl(map, "未知的contentType类型");
        }
       
        
     // 组装 排序
        Map<String,Map<String,List<CourseSchedule>>> scheduleMap = new HashMap<>();
		for (CourseSchedule courseSchedule : scheduleList) {
			String periodInterval = courseSchedule.getPeriodInterval();
			int period = courseSchedule.getPeriod();
			String dayOfWeek = courseSchedule.getDayOfWeek()+"";
			Map<String, List<CourseSchedule>> map2 = scheduleMap.computeIfAbsent(periodInterval + "-" + period, k -> new HashMap<>());
			List<CourseSchedule> list = map2.computeIfAbsent(dayOfWeek, k -> new ArrayList<>());
			list.add(courseSchedule);
		}
		for (Map<String, List<CourseSchedule>> map2 : scheduleMap.values()) {
			for (List<CourseSchedule> list : map2.values()) {
				list.sort(Comparator.comparingInt(CourseSchedule::getWeekType));
				if(list.size()<2) {
					list.forEach(e->{
						if(e.getWeekType() == WEEK_TYPE_EVEN) {							
							e.setSubjectName(e.getSubjectName()+"(双)");
						}else if(e.getWeekType() == WEEK_TYPE_ODD){
							e.setSubjectName(e.getSubjectName()+"(单)");
						}
					});
				}
			}
		}
		
		
		map.put("scheduleMap", scheduleMap);
		
//		map.put("gradeNoClick", gradeNoClick);
		
		map.put("indexId", gradeId);
		map.put("objId", objId);
		map.put("type", cType);
		map.put("sub_sys", "BASE");
        
		if(!"1".equals(fromAlone)) {
			Map<String, String> teacherNameMap = scheduleList.stream().filter(e -> StringUtils.isNotBlank(e.getTeacherId()))
					.collect(Collectors.toMap(CourseSchedule::getTeacherId, CourseSchedule::getTeacherName, (k1, k2) -> k1));
			map.put("teacherNameMap", teacherNameMap);
			return "/basedata/courseSchedule/modify/mutil/mainScheduleList.ftl";
		}
        
		map.put("fromAlone", "1");
        // 获取班级特征  只获取必修课
        
        map.put("classSubjectList", classSubjectList);
		
		return "/basedata/courseSchedule/modify/mutil/mainScheduleList.ftl";
	}


	private List<List<ClassFeatureDto>> makePlaceClassSubs(String schoolId, String searchAcadyear,
			String searchSemester, int weekType, String placeId, List<CourseSchedule> scheduleList,ModelMap map) {
//		String gradeId = (String) map.get("indexId");
		List<Clazz> classList = classService.findByPlaceIds(schoolId, new String[] {placeId});
//		classList = classList.stream().filter(e->gradeId.equals(e.getGradeId())).collect(Collectors.toList());
		Set<String> classIds = EntityUtils.getSet(classList, Clazz::getId);
		List<ClassTeaching> classTeachingList = classTeachingService.findBySearchForList(searchAcadyear, searchSemester,
				classIds.toArray(new String[0]));
		Map<String, String> subName = new HashMap<>();
		Set<String> othSubIds = classTeachingList.stream().map(e->e.getSubjectId()).collect(Collectors.toSet());
		if(CollectionUtils.isNotEmpty(othSubIds)) {
			LinkedHashMap<String, String> courseMap = courseService.findPartCouByIds(othSubIds.toArray(new String[0]));
			subName = courseMap;
		}
		// TODO 暂时不显示 其他年级的场地
//		List<Clazz> collect = classList.stream().filter(e->!gradeId.equals(e.getGradeId())).collect(Collectors.toList());
//		Set<String> gradeIds = EntityUtils.getSet(collect, Clazz::getGradeId);
//		List<Grade> gradeList = gradeService.findListByIdIn(gradeIds.toArray(new String[0]));
//		Map<String, String> gradeNameMap = EntityUtils.getMap(gradeList, e->e.getId(),e->e.getGradeName());
//		Map<String, String> extraClassNameMap = EntityUtils.getMap(collect, e->e.getId(),e->gradeNameMap.get(e.getGradeId())+e.getClassName());
//		map.put("extraClassNameMap", extraClassNameMap);
		
		List<List<ClassFeatureDto>> classSubjectList = new ArrayList<>();
		ClassFeatureDto cfDto = null;
		for (ClassTeaching ct : classTeachingList) {
			// TODO 选修课 是否要显示
			if(ct.getWeekType() != CourseSchedule.WEEK_TYPE_NORMAL && weekType != ct.getWeekType()) {
				int ch = ct.getCourseHour().intValue()-1;
				if(ch <1)
					continue;
				else {
					ct.setCourseHour((float)ch);
					ct.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
				}
			}
			cfDto = new ClassFeatureDto();
			cfDto.setSubjectId(ct.getSubjectId());
			cfDto.setSubjectName(subName.get(ct.getSubjectId()));
			cfDto.setSubjectType(ct.getSubjectType());
			cfDto.setTeacherId(ct.getTeacherId());
			cfDto.setWeekType(ct.getWeekType());
			if(ct.getCourseHour()!=null) {
				cfDto.setCourseWorkDay(ct.getCourseHour().intValue()); // 减一法 取整
			}else{
				cfDto.setCourseWorkDay(0);
			}
			cfDto.setClassId(ct.getClassId());
			int uu = cfDto.getCourseWorkDay()-1;
			if(ct.getWeekType() != CourseSchedule.WEEK_TYPE_NORMAL) {
				ClassFeatureDto clDto = EntityUtils.copyProperties(cfDto, ClassFeatureDto.class);
				clDto.setCourseWorkDay(1);
				if(ct.getWeekType() == CourseSchedule.WEEK_TYPE_EVEN) {
					clDto.setSubjectName(clDto.getSubjectName()+"(双)");
				}else if(ct.getWeekType() == CourseSchedule.WEEK_TYPE_ODD){
					clDto.setSubjectName(clDto.getSubjectName()+"(单)");
				}else {
					clDto.setSubjectName(clDto.getSubjectName()+"(未知)");
				}
				classSubjectList.add(Arrays.asList(clDto));

				if(uu < 1)
					continue;

				cfDto.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
				cfDto.setCourseWorkDay(uu);
			}

			classSubjectList.add(Arrays.asList(cfDto));
		}
		
		return classSubjectList;
	}


	private List<List<ClassFeatureDto>> makeTeacherClassSubs(String unitId, String searchAcadyear, String searchSemester, int weekType,
			String teacherId, List<CourseSchedule> scheduleList, ModelMap map) {
//		String gradeId = (String) map.get("indexId");
//		List<Clazz> classList = classService.findByGradeId(gradeId);
//		List<Clazz> bySchoolId = classService.findBySchoolId(unitId);
//		Set<String> cids = EntityUtils.getSet(classList, Clazz::getId);
		List<ClassTeaching> classTeachingList = classTeachingService.findClassTeachingList(unitId, searchAcadyear, searchSemester, new String[] {teacherId});
//		List<ClassTeaching> classTeachingList = classTeachingService.findBySearchForList(searchAcadyear, searchSemester, cids.toArray(new String[0]));
		List<ClassTeaching> classTeachingList2 = new ArrayList<>();
		//TODO 校验正确性
		for (ClassTeaching ct : classTeachingList) {
			if(BaseConstants.SUBJECT_TYPE_BX.equals(ct.getSubjectType())) {
				// TODO 选修课 是否要显示
				if(ct.getWeekType() != CourseSchedule.WEEK_TYPE_NORMAL && weekType != ct.getWeekType()) {
					int ch = ct.getCourseHour().intValue()-1;
					if(ch <1)
						continue;
					else {
						ct.setCourseHour((float)ch);
						ct.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
					}
				}
				classTeachingList2.add(ct);
			}
		}

		
		classTeachingList = classTeachingList2;

		Map<String, String> subName = new HashMap<>();
		Set<String> othSubIds = classTeachingList.stream().map(e->e.getSubjectId()).collect(Collectors.toSet());
		if(CollectionUtils.isNotEmpty(othSubIds)) {
			LinkedHashMap<String, String> courseMap = courseService.findPartCouByIds(othSubIds.toArray(new String[0]));
			subName = courseMap;
		}
		List<List<ClassFeatureDto>> classSubjectList = new ArrayList<>();
		ClassFeatureDto cfDto = null;
		for (ClassTeaching ctg : classTeachingList) {
			if(ctg.getCourseHour()==null || ctg.getCourseHour().intValue()<1)
				continue;

			cfDto = new ClassFeatureDto();
			cfDto.setSubjectId(ctg.getSubjectId());
			cfDto.setSubjectName(subName.get(ctg.getSubjectId()));
			cfDto.setSubjectType(ctg.getSubjectType());
			cfDto.setTeacherId(ctg.getTeacherId());
			cfDto.setWeekType(ctg.getWeekType());
			if(ctg.getCourseHour()!=null) {
				cfDto.setCourseWorkDay(ctg.getCourseHour().intValue()); // 减一法 取整
			}
			cfDto.setClassId(ctg.getClassId());
			int uu = cfDto.getCourseWorkDay()-1;
			if(ctg.getWeekType() != CourseSchedule.WEEK_TYPE_NORMAL) {
				ClassFeatureDto clDto = EntityUtils.copyProperties(cfDto, ClassFeatureDto.class);
				clDto.setCourseWorkDay(1);
				if(ctg.getWeekType() == CourseSchedule.WEEK_TYPE_EVEN) {
					clDto.setSubjectName(clDto.getSubjectName()+"(双)");
				}else if(ctg.getWeekType() == CourseSchedule.WEEK_TYPE_ODD){
					clDto.setSubjectName(clDto.getSubjectName()+"(单)");
				}else {
					clDto.setSubjectName(clDto.getSubjectName()+"(未知)");
				}
				classSubjectList.add(Arrays.asList(clDto));

				if(uu < 1)
					continue;

				cfDto.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
				cfDto.setCourseWorkDay(uu);
			}

			classSubjectList.add(Arrays.asList(cfDto));
		}

		//TODO 教学班 课时情况
//		Set<String> jxbIds = scheduleList.stream().filter(e -> CourseSchedule.CLASS_TYPE_NORMAL != e.getClassType())
//				.map(e -> e.getClassId()).collect(Collectors.toSet());
//		List<TeachClass> jxbs = teachClassService.findListByIds(jxbIds.toArray(new String[0]));
//		for (TeachClass ctg : jxbs) {
//			//TODO 取 courseHour FIXME 没有教学班 上了 几节课 的 信息，只能固定了
//			if(ctg.getCourseHour()==null || ctg.getCourseHour().intValue()<1)
//				continue;
//
//			cfDto = new ClassFeatureDto();
//			cfDto.setSubjectId(ctg.getCourseId());
//			cfDto.setSubjectName(subName.get(ctg.getCourseId()));
//			cfDto.setSubjectType(ctg.getSubjectType());  //TODO courseschedule 的 subjectType
//			cfDto.setTeacherId(ctg.getTeacherId());
//			cfDto.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
//			if(ctg.getCourseHour()!=null) {
//				cfDto.setCourseWorkDay(ctg.getCourseHour().intValue()); // 减一法 取整
//			}
//			cfDto.setClassId(ctg.getId());
//			int uu = cfDto.getCourseWorkDay()-1;
////			if() {}
//			// 教学班没有单双周
//
//			classSubjectList.add(Arrays.asList(cfDto));
//		}

		return classSubjectList;
	}


	private List<List<ClassFeatureDto>> makeClassSubs(String searchAcadyear, String searchSemester, int weekType,
			String classId) {
		List<ClassTeaching> classTeachingList = classTeachingService.findBySearchForList(searchAcadyear, searchSemester, new String[] {classId});
//        classTeachingList = classTeachingList.stream()
//        		.filter(e->!BaseConstants.ONE_STR.equals(e.getIsTeaCls()+"")
//        				&& BaseConstants.SUBJECT_TYPE_BX.equals(e.getSubjectType())
//        				&& (weekType == e.getWeekType() || e.getWeekType() == CourseSchedule.WEEK_TYPE_NORMAL))
//        		.collect(Collectors.toList());
        List<ClassTeaching> classTeachingList2 = new ArrayList<>();
        for (ClassTeaching ct : classTeachingList) {
        	if(!BaseConstants.ONE_STR.equals(ct.getIsTeaCls()+"")
    				&& BaseConstants.SUBJECT_TYPE_BX.equals(ct.getSubjectType())) {
        		
        		if(ct.getWeekType() != CourseSchedule.WEEK_TYPE_NORMAL && weekType != ct.getWeekType()) {
        			int ch = ct.getCourseHour().intValue()-1;
        			if(ch <1)
        				continue;
        			else {
        				ct.setCourseHour((float)ch);
        				ct.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
        			}
        		}
        		classTeachingList2.add(ct);
        	}
			
		}
        classTeachingList = classTeachingList2;
       
        Map<String, String> subName = new HashMap<>();
		Set<String> othSubIds = classTeachingList.stream().map(e->e.getSubjectId()).collect(Collectors.toSet());
        if(CollectionUtils.isNotEmpty(othSubIds)) {
        	LinkedHashMap<String, String> courseMap = courseService.findPartCouByIds(othSubIds.toArray(new String[0]));
        	subName = courseMap;
        }
        List<List<ClassFeatureDto>> classSubjectList = new ArrayList<>();
        ClassFeatureDto cfDto = null;
        for (ClassTeaching ctg : classTeachingList) {
        	if(ctg.getCourseHour()==null || ctg.getCourseHour().intValue()<1)
        		continue;
        	
			cfDto = new ClassFeatureDto();
			cfDto.setSubjectId(ctg.getSubjectId());
			cfDto.setSubjectName(subName.get(ctg.getSubjectId()));
			cfDto.setSubjectType(ctg.getSubjectType());
			cfDto.setTeacherId(ctg.getTeacherId());
			cfDto.setWeekType(ctg.getWeekType());
			if(ctg.getCourseHour()!=null) {
				cfDto.setCourseWorkDay(ctg.getCourseHour().intValue()); // 减一法 取整
			}
			cfDto.setClassId(classId);
			int uu = cfDto.getCourseWorkDay()-1;
			if(ctg.getWeekType() != CourseSchedule.WEEK_TYPE_NORMAL) {
				ClassFeatureDto clDto = EntityUtils.copyProperties(cfDto, ClassFeatureDto.class);
				clDto.setCourseWorkDay(1);
				if(ctg.getWeekType() == CourseSchedule.WEEK_TYPE_EVEN) {
					clDto.setSubjectName(clDto.getSubjectName()+"(双)");
				}else if(ctg.getWeekType() == CourseSchedule.WEEK_TYPE_ODD){
					clDto.setSubjectName(clDto.getSubjectName()+"(单)");
				}else {
					clDto.setSubjectName(clDto.getSubjectName()+"(未知)");
				}
				classSubjectList.add(Arrays.asList(clDto));
				
				if(uu < 1)
					continue;
				
				cfDto.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
				cfDto.setCourseWorkDay(uu);
			}
			
			classSubjectList.add(Arrays.asList(cfDto));
		}
		return classSubjectList;
	}
	
	/**
	 * 
	 * @param gradeId
	 * @param coverAll 1:覆盖所有周次的课程 null：本周的课程调整
	 * @param dto
	 * @return
	 */
	@RequestMapping("/swapCourse")
	@ResponseBody
	public String swapCourse(@PathVariable String gradeId, String coverAll, ScheduleSwapDto dto, String fromAlone) {
		CourseSchedule[] leftSchedules = dto.getLeftSchedules();
		CourseSchedule[] rightSchedules = dto.getRightSchedules();
		
		String leftPeriod = dto.getLeftPeriod();
		String rightPeriod = dto.getRightPeriod();
		
		String[] leftCourseScheduleIds = new String[] {};
		String[] rightCourseScheduleIds = new String[] {};
		if(leftSchedules != null && leftSchedules.length >0) {
			leftCourseScheduleIds = Arrays.stream(leftSchedules).map(e->e.getId()).collect(Collectors.toList()).toArray(new String[0]);
		}
		if(rightSchedules != null && rightSchedules.length >0) {
			rightCourseScheduleIds = Arrays.stream(rightSchedules).map(e->e.getId()).collect(Collectors.toList()).toArray(new String[0]);
		}

		if ((leftCourseScheduleIds.length == 0) && (rightCourseScheduleIds.length == 0)) {
			return error("两个位置都没有课程，不需要交换");
		}

		List<CourseSchedule> leftCourseSchedules = courseScheduleService.findListByIds(leftCourseScheduleIds);
		List<CourseSchedule> rightCourseSchedules = courseScheduleService.findListByIds(rightCourseScheduleIds);
		
		String schoolId = leftCourseSchedules.get(0).getSchoolId();
		String acadyear = leftCourseSchedules.get(0).getAcadyear();
		int semester = leftCourseSchedules.get(0).getSemester();
		int weekOfWorktime = leftCourseSchedules.get(0).getWeekOfWorktime();
		
		String msg = checkClassConflict(leftCourseSchedules, rightCourseSchedules,
				leftPeriod, rightPeriod,null, acadyear, semester, weekOfWorktime);
		if(StringUtils.isNotBlank(msg)) {
			return msg;
		}
		

		String[] leftTimeArr = leftPeriod.split("_");
		String[] rightTimeArr = rightPeriod.split("_");
		
		List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(schoolId, acadyear, semester);
		List<String> weekList = EntityUtils.getList(dateInfoList, e->e.getWeek()+"_"+e.getWeekday());
		if(CollectionUtils.isEmpty(rightCourseSchedules)){
			if(!weekList.contains(weekOfWorktime+"_"+(Integer.parseInt(rightTimeArr[0])+1))){
				return error("不能将课程调至节假日");
			}
		}
		
		List<CourseSchedule> others = new ArrayList<>();
		String operatorId = getLoginInfo().getOwnerId();
		Adjusted adjusted = new Adjusted();
		adjusted.setId(UuidUtils.generateUuid());
		adjusted.setSchoolId(schoolId);
		adjusted.setAcadyear(acadyear);
		adjusted.setSemester(semester);
		adjusted.setWeekOfWorktime(weekOfWorktime);
		adjusted.setOperator(operatorId);
		adjusted.setRemark("教务安排直接安排");
		adjusted.setState(TipsayConstants.TIPSAY_STATE_1);
		adjusted.setCreationTime(new Date());
		adjusted.setModifyTime(new Date());
		TipsayEx tipsayEx = new TipsayEx();
		tipsayEx.setId(UuidUtils.generateUuid());
		tipsayEx.setTipsayId(adjusted.getId());
		tipsayEx.setSchoolId(schoolId);
		tipsayEx.setAuditorId(operatorId);
		tipsayEx.setAuditorType(TipsayConstants.AUDITOR_TYPE_1);
		tipsayEx.setRemark("教务安排直接安排");
		tipsayEx.setState(TipsayConstants.TIPSAY_STATE_1);
		tipsayEx.setSourceType(TipsayConstants.TYPE_02);
		tipsayEx.setCreationTime(new Date());
		AdjustedDetail adjustedDetail = null;
		List<AdjustedDetail> adjustedDetailList = new ArrayList<AdjustedDetail>();
		for (CourseSchedule to : leftCourseSchedules) {
			adjustedDetail = makeAdjustedDetail(to, adjusted.getId(), "01");
			adjustedDetailList.add(adjustedDetail);
			to.setDayOfWeek(Integer.parseInt(rightTimeArr[0]));
			to.setPeriodInterval(rightTimeArr[1]);
			to.setPeriod(Integer.parseInt(rightTimeArr[2]));
		}
		if(CollectionUtils.isNotEmpty(rightCourseSchedules)){
			for (CourseSchedule to : rightCourseSchedules) {
				adjustedDetail = makeAdjustedDetail(to, adjusted.getId(), "02");
				adjustedDetailList.add(adjustedDetail);
				to.setDayOfWeek(Integer.parseInt(leftTimeArr[0]));
				to.setPeriodInterval(leftTimeArr[1]);
				to.setPeriod(Integer.parseInt(leftTimeArr[2]));
			}
		}else{
			adjustedDetail = new AdjustedDetail();
			adjustedDetail.setId(UuidUtils.generateUuid());
	        adjustedDetail.setAdjustedId(adjusted.getId());
	        adjustedDetail.setSchoolId(schoolId);
	        adjustedDetail.setClassId(BaseConstants.ZERO_GUID);
	        adjustedDetail.setWeekOfWorktime(weekOfWorktime);
	        adjustedDetail.setDayOfWeek(Integer.parseInt(rightTimeArr[0]));
	        adjustedDetail.setPeriodInterval(rightTimeArr[1]);
	        adjustedDetail.setPeriod(Integer.parseInt(rightTimeArr[2]));
	        adjustedDetail.setCreationTime(new Date());
	        adjustedDetail.setModifyTime(new Date());
	        adjustedDetail.setAdjustedType("02");
	        adjustedDetailList.add(adjustedDetail);
		}
		

		others.addAll(leftCourseSchedules);
		others.addAll(rightCourseSchedules);
		
		if("1".equals(fromAlone)) {
			try {
				courseScheduleService.saveScheduleModify(others, null);
			} catch (Exception e1) {
				e1.printStackTrace();
				return error(e1.getMessage());
			}
			return returnSuccess();
		}
		
		// int i = 1/0;
		try {
			adjustedService.saveAll(adjusted,adjustedDetailList.toArray(new AdjustedDetail[0]),tipsayEx,others,null);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> teacherIdList = new ArrayList<>();
                Set<String> classIds = new HashSet<>();
                for (AdjustedDetail one : adjustedDetailList) {
                    if (one.getTeacherId() != null) {
                        teacherIdList.add(one.getTeacherId());
                    }
                    if (one.getClassId() != null) {
                    	classIds.add(one.getClassId());
					}
                }
                tipsayService.pushMessage(teacherIdList.toArray(new String[0]), null, "您的课表因教务调整有所更改，请及时登录查看。", "调课通知");
                if (CollectionUtils.isNotEmpty(classIds)) {
                    List<Clazz> clazzList = classService.findClassListByIds(classIds.toArray(new String[0]));
                    String[] headMasters = clazzList.stream().filter(e -> StringUtils.isNotBlank(e.getTeacherId())).map(e -> e.getTeacherId()).toArray(String[]::new);
                    tipsayService.pushMessage(headMasters, null, "您所属班级有新的代课信息。", "所属班级调课通知");
                }
            }
        }).start();
		return returnSuccess();
	}


	/**
	 * 手动调课-教师 交换课程
	 * @param arrayId
	 * @param dto
	 * @param timestr
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param weekIndex
	 * @return
	 */
	@RequestMapping("/swapTeacherCourse")
	@ResponseBody
	public String swapTeacherCourse(@PathVariable String gradeId, PeriodSwapDto dto, String timestr,
			String searchAcadyear, String searchSemester, int weekIndex) {
		/* 分了周次 不存在 单双周 如果两节课 则认为 是冲突，不进行调课 */
		CourseSchedule[] leftSchedules = dto.getLeftSchedules();
		CourseSchedule[] rightSchedules = dto.getRightSchedules();
		
		String teacherId = leftSchedules[0].getTeacherId();
		String leftPeriod = dto.getLeftPeriod();
		String rightPeriod = dto.getRightPeriod();
		
		String[] leftTimes = leftPeriod.split("_");
		String[] rightTimes = rightPeriod.split("_");
		
		if(leftSchedules.length>1 && (Arrays.stream(leftSchedules).map(e->e.getClassId()).count()>1)) {
			return error("教师课表本身存在时间冲突");
		}
		if(rightSchedules!=null && rightSchedules.length>1 && (Arrays.stream(rightSchedules).map(e->e.getClassId()).count()>1)) {
			return error("教师课表本身存在时间冲突");
		}
		
		//
		/* 最多一共比较4次 冲突 */
		Set<String> cids = Arrays.asList(leftSchedules).stream().map(e->e.getClassId()).collect(Collectors.toSet());
		if(rightSchedules!=null) {
			Set<String> cids2 = Arrays.asList(rightSchedules).stream().map(e->e.getClassId()).collect(Collectors.toSet());
			cids.addAll(cids2);
		}
		List<CourseSchedule> allScheduleList = courseScheduleService.findCourseScheduleListByClassIdes(searchAcadyear, 
        		Integer.valueOf(searchSemester), cids.toArray(new String[0]), weekIndex);
		Map<String, List<CourseSchedule>> cidScheduleMap = EntityUtils.getListMap(allScheduleList, CourseSchedule::getClassId, Function.identity());
		List<CourseSchedule> saveList = new ArrayList<>();
		//1.old to new 
		for (CourseSchedule cs : leftSchedules) {
			String classId = cs.getClassId();
			List<CourseSchedule> scheduleList = cidScheduleMap.get(classId);
			List<CourseSchedule> leftScheduleList = scheduleList.stream().filter(e->leftPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
			List<CourseSchedule> rightScheduleList = scheduleList.stream().filter(e->rightPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
			
			String msg = checkClassConflict(leftScheduleList,
					rightScheduleList, leftPeriod, rightPeriod, teacherId, searchAcadyear, 
					Integer.valueOf(searchSemester),weekIndex);
			if(StringUtils.isNotBlank(msg))
				return msg;
			// save TODO
			makeTtoSaveResult(rightTimes, saveList, leftScheduleList);
			makeTtoSaveResult(leftTimes, saveList, rightScheduleList);
		}
		//2. new to old
		if(rightSchedules!=null) {
			for (CourseSchedule cs : rightSchedules) {
				String classId = cs.getClassId();
				List<CourseSchedule> scheduleList = cidScheduleMap.get(classId);
				List<CourseSchedule> leftScheduleList = scheduleList.stream().filter(e->rightPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
				List<CourseSchedule> rightScheduleList = scheduleList.stream().filter(e->leftPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
				
				String msg = checkClassConflict(leftScheduleList,
						rightScheduleList, rightPeriod, leftPeriod, teacherId, searchAcadyear, 
						Integer.valueOf(searchSemester),weekIndex);
				if(StringUtils.isNotBlank(msg))
					return msg;
				// save
				makeTtoSaveResult(leftTimes, saveList, leftScheduleList);
				makeTtoSaveResult(rightTimes, saveList, rightScheduleList);
			}
		}
		
		String schooleId = getLoginInfo().getUnitId();
		List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(schooleId, searchAcadyear, Integer.parseInt(searchSemester));
		List<String> weekList = EntityUtils.getList(dateInfoList, e->e.getWeek()+"_"+e.getWeekday());
		if(rightSchedules == null || rightSchedules.length <=0){
			if(!weekList.contains(weekIndex+"_"+(Integer.parseInt(rightTimes[0])+1))){
				return error("不能将课程调至节假日");
			}
		}
		
		try {
			courseScheduleService.saveScheduleModify(saveList, null);
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage());
		}
		
		return returnSuccess();
	}
	
	/**
	 * 手动调课 场地  交换课程
	 * @param gradeId
	 * @param dto
	 * @param timestr
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param weekIndex
	 * @return
	 */
	@RequestMapping("/swapPlaceCourse")
	@ResponseBody
	public String swapPlaceCourse(@PathVariable String gradeId, PeriodSwapDto dto, String timestr,String fromAlone,
			String searchAcadyear, String searchSemester, int weekIndex) {
		CourseSchedule[] leftSchedules = dto.getLeftSchedules();
		CourseSchedule[] rightSchedules = dto.getRightSchedules();
		
		String leftPeriod = dto.getLeftPeriod();
		String rightPeriod = dto.getRightPeriod();
		String[] leftTimeArr = leftPeriod.split("_");
		String[] rightTimeArr = rightPeriod.split("_");
		
		String schooleId = getLoginInfo().getUnitId();
		List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(schooleId, searchAcadyear, Integer.parseInt(searchSemester));
		List<String> weekList = EntityUtils.getList(dateInfoList, e->e.getWeek()+"_"+e.getWeekday());
		if(rightSchedules == null || rightSchedules.length <=0){
			if(!weekList.contains(weekIndex+"_"+(Integer.parseInt(rightTimeArr[0])+1))){
				return error("不能将课程调至节假日");
			}
		}
		
		String[] leftCourseScheduleIds = new String[] {};
		String[] rightCourseScheduleIds = new String[] {};
		if(leftSchedules != null && leftSchedules.length >0) {
			leftCourseScheduleIds = Arrays.stream(leftSchedules).map(e->e.getId()).collect(Collectors.toList()).toArray(new String[0]);
		}
		if(rightSchedules != null && rightSchedules.length >0) {
			rightCourseScheduleIds = Arrays.stream(rightSchedules).map(e->e.getId()).collect(Collectors.toList()).toArray(new String[0]);
		}

		if ((leftCourseScheduleIds.length == 0) && (rightCourseScheduleIds.length == 0)) {
			return error("两个位置都没有课程，不需要交换");
		}

		Set<String> cids = Arrays.asList(leftSchedules).stream().map(e->e.getClassId()).collect(Collectors.toSet());
		if(rightSchedules!=null) {
			Set<String> cids2 = Arrays.asList(rightSchedules).stream().map(e->e.getClassId()).collect(Collectors.toSet());
			cids.addAll(cids2);
		}
		
		List<CourseSchedule> saveList = new ArrayList<>();
		if(cids.size()==1) {
			List<CourseSchedule> leftCourseSchedules = courseScheduleService.findListByIds(leftCourseScheduleIds);
			List<CourseSchedule> rightCourseSchedules = courseScheduleService.findListByIds(rightCourseScheduleIds);
			
			String msg = checkClassConflict(leftCourseSchedules, rightCourseSchedules,
					leftPeriod, rightPeriod,null, searchAcadyear, Integer.parseInt(searchSemester), weekIndex);
			if(StringUtils.isNotBlank(msg)) {
				return msg;
			}
			
			makeTtoSaveResult(rightTimeArr, saveList , leftCourseSchedules);
			makeTtoSaveResult(leftTimeArr, saveList , rightCourseSchedules);
		}else if(cids.size()>1) {
			List<CourseSchedule> allScheduleList = courseScheduleService.findCourseScheduleListByClassIdes(searchAcadyear, 
	        		Integer.valueOf(searchSemester), cids.toArray(new String[0]), weekIndex);
			Map<String, List<CourseSchedule>> cidScheduleMap = EntityUtils.getListMap(allScheduleList, CourseSchedule::getClassId, Function.identity());
			//1.old to new 
			for (CourseSchedule cs : leftSchedules) {
				String classId = cs.getClassId();
				List<CourseSchedule> scheduleList = cidScheduleMap.get(classId);
				List<CourseSchedule> leftScheduleList = scheduleList.stream().filter(e->leftPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
				List<CourseSchedule> rightScheduleList = scheduleList.stream().filter(e->rightPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
				
				String msg = checkClassConflict(leftScheduleList,
						rightScheduleList, leftPeriod, rightPeriod, null, searchAcadyear, 
						Integer.valueOf(searchSemester),weekIndex);
				if(StringUtils.isNotBlank(msg))
					return msg;
				// save TODO
				makeTtoSaveResult(rightTimeArr, saveList, leftScheduleList);
				makeTtoSaveResult(leftTimeArr, saveList, rightScheduleList);
			}
			//2. new to old
			if(rightSchedules!=null) {
				for (CourseSchedule cs : rightSchedules) {
					String classId = cs.getClassId();
					List<CourseSchedule> scheduleList = cidScheduleMap.get(classId);
					List<CourseSchedule> leftScheduleList = scheduleList.stream().filter(e->rightPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
					List<CourseSchedule> rightScheduleList = scheduleList.stream().filter(e->leftPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
					
					String msg = checkClassConflict(leftScheduleList,
							rightScheduleList, rightPeriod, leftPeriod, null, searchAcadyear, 
							Integer.valueOf(searchSemester),weekIndex);
					if(StringUtils.isNotBlank(msg))
						return msg;
					// save
					makeTtoSaveResult(leftTimeArr, saveList, leftScheduleList);
					makeTtoSaveResult(rightTimeArr, saveList, rightScheduleList);
				}
			}
		}else {
			return error("未知异常");
		}
		
		try {
			courseScheduleService.saveScheduleModify(saveList, null);
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage());
		}
		
		return returnSuccess();
	}
	
	
	private void makeTtoSaveResult(String[] rightTimes, List<CourseSchedule> saveList,
			List<CourseSchedule> leftScheduleList) {
		for (CourseSchedule cs : leftScheduleList) {
			cs.setDayOfWeek(Integer.parseInt(rightTimes[0]));
			cs.setPeriodInterval(rightTimes[1]);
			cs.setPeriod(Integer.parseInt(rightTimes[2]));
			saveList.add(cs);
		}
	}


	private String checkClassConflict(List<CourseSchedule> leftCourseSchedules, List<CourseSchedule> rightCourseSchedules, String leftPeriod,
			String rightPeriod, String teacherId, String acadyear, int semester, int weekOfWorktime) {
		String[] leftCourseScheduleIds =  leftCourseSchedules.stream().map(e->e.getId()).toArray(e->new String[e]);
		String[] rightCourseScheduleIds = rightCourseSchedules.stream().map(e->e.getId()).toArray(e->new String[e]);
		
		CourseSchedule courseScheduleT = leftCourseSchedules.get(0);
		if (leftCourseScheduleIds.length == 1 && rightCourseScheduleIds.length == 1) {
			if (leftCourseSchedules.get(0).getId().equals(rightCourseSchedules.get(0).getId())) {
				// 同一个科目无需交换时间
				return error("相同科目的课程不需要交换");
			}
		}
	
		if (leftCourseSchedules.stream().anyMatch(e -> BaseConstants.SUBJECT_TYPE_VIRTUAL.equals(e.getSubjectType()))) {
			return error("需调课程为虚拟课程");
		}
	
		if (rightCourseSchedules.stream().anyMatch(e -> BaseConstants.SUBJECT_TYPE_VIRTUAL.equals(e.getSubjectType()))) {
			return error("被调班级目标时间存在虚拟课程");
		}
	
		// a老师的在 b时间上课会不会冲突
		Set<String> teacherIds1 = EntityUtils.getSet(leftCourseSchedules, CourseSchedule::getTeacherId);
		Set<String> teacherIds2 = EntityUtils.getSet(rightCourseSchedules, CourseSchedule::getTeacherId);
		teacherIds1.remove(teacherId);
		teacherIds2.remove(teacherId);
		
	
		List<CourseSchedule> teacher1TimeList = courseScheduleService.findCourseScheduleListByTeacherIdIn(acadyear,
				semester, weekOfWorktime, teacherIds1.toArray(new String[0]));
		List<CourseSchedule> teacher2TimeList = courseScheduleService.findCourseScheduleListByTeacherIdIn(acadyear,
				semester, weekOfWorktime, teacherIds2.toArray(new String[0]));
	
		Map<String, List<CourseSchedule>> teacher1TimeMap = EntityUtils.getListMap(teacher1TimeList,
				CourseSchedule::getTeacherId, Function.identity());
		Map<String, List<CourseSchedule>> teacher2TimeMap = EntityUtils.getListMap(teacher2TimeList,
				CourseSchedule::getTeacherId, Function.identity());
		// 检查 教师冲突
		String msg = null;
		msg = checkTeacherConflicts(leftCourseScheduleIds, leftPeriod, rightPeriod, teacher1TimeMap);
		if (StringUtils.isNotBlank(msg)) {
			return msg;
		}
		msg = checkTeacherConflicts(rightCourseScheduleIds, rightPeriod, leftPeriod, teacher2TimeMap);
		if (StringUtils.isNotBlank(msg)) {
			return msg;
		}
	
		// 如果有一个时间点是空的，则实际为移动课程；还需要检验 场地和学生的冲突
		List<CourseSchedule> srcCourseSchedules = null;
		String destPeriod = null;
		if (leftCourseScheduleIds.length == 0) {
			srcCourseSchedules = rightCourseSchedules;
			destPeriod = leftPeriod;
		} else if (rightCourseScheduleIds.length == 0) {
			srcCourseSchedules = leftCourseSchedules;
			destPeriod = rightPeriod;
		}
		if (destPeriod != null) {
			msg = checkRoomConflicts(courseScheduleT, srcCourseSchedules.get(0).getPlaceId(), destPeriod);
			if (StringUtils.isNotBlank(msg)) {
				return error(msg);
			}
			msg = checkStudentConflicts(courseScheduleT, srcCourseSchedules.get(0).getClassId(), destPeriod);
			if (StringUtils.isNotBlank(msg)) {
				return error(msg);
			}
		}
		return null;
	}


	/**
	 * 从底部添加课程 或者 删除某些课程
	 * @param gradeId
	 * @param dto
	 * @param type
	 * @param period
	 * @return
	 */
	@RequestMapping("/delOrAddLecture")
	@ResponseBody
	public String delOrAddLecture(@PathVariable String gradeId, PeriodSwapDto dto, String type, String period,
			String searchAcadyear, String searchSemester, int weekIndex) {
		CourseSchedule[] outterSchedules = dto.getLeftSchedules();  // 主课表
		CourseSchedule[] innerSchedules = dto.getRightSchedules();  // 底部待排列表
		String unitId = getLoginInfo().getUnitId();
		
		if (outterSchedules!= null && Arrays.stream(outterSchedules).anyMatch(e -> BaseConstants.SUBJECT_TYPE_VIRTUAL.equals(e.getSubjectType()))) {
			return error("需调课程为虚拟课程");
		}
	
		String[] timestr = period.split("_");
		String schooleId = getLoginInfo().getUnitId();
		List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(schooleId, searchAcadyear, Integer.parseInt(searchSemester));
		List<String> weekList = EntityUtils.getList(dateInfoList, e->e.getWeek()+"_"+e.getWeekday());
		if(outterSchedules == null || outterSchedules.length <=0){
			if(!weekList.contains(weekIndex+"_"+(Integer.parseInt(timestr[0])+1))){
				return error("不能将课程调至节假日");
			}
		}
		
		CourseSchedule courseScheduleT = null;
		int semester = Integer.parseInt(searchSemester);
		if(innerSchedules != null && innerSchedules.length>0) {
			courseScheduleT = innerSchedules[0];
			courseScheduleT.setAcadyear(searchAcadyear);
			courseScheduleT.setSemester(semester);
			courseScheduleT.setWeekOfWorktime(weekIndex);
			courseScheduleT.setSchoolId(unitId);
			courseScheduleT.setGradeId(gradeId);
		}
		
		String destPeriod = period;
//		CourseSchedule[] outterSchedules2 = null;
		// 检查 教师冲突
		if(BaseConstants.SCHEDULE_CONTENT_TYPE_C.equals(type)) {
			
			List<String> tids = null;
			if(innerSchedules!=null)
				tids = Arrays.stream(innerSchedules).filter(e->e!=null && e.getTeacherId()!=null).map(e->e.getTeacherId()).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(tids)) {
				List<CourseSchedule> teacher1TimeList = courseScheduleService.findCourseScheduleListByTeacherIdIn(searchAcadyear,
						Integer.parseInt(searchSemester), weekIndex, tids.toArray(new String[0]));
				
				Map<String, List<CourseSchedule>> timesMap = teacher1TimeList.stream()  
						.collect(Collectors.groupingBy(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod()));
				if(timesMap.containsKey(destPeriod)) {
					Teacher teacher = teacherService.findOne(tids.get(0));
					return error("教师"+teacher.getTeacherName()+" 在目标时间存在冲突");
				}
				
//				outterSchedules2 = outterSchedules;
			}
		}else if(BaseConstants.SCHEDULE_CONTENT_TYPE_T.equals(type)) {
			// 班级场地冲突 
			if(innerSchedules != null) {
				String[] cids = Arrays.stream(innerSchedules).map(CourseSchedule::getClassId).toArray(e->new String[e]);
				List<CourseSchedule> allScheduleList = courseScheduleService.findCourseScheduleListByClassIdes(searchAcadyear, 
		        		Integer.valueOf(searchSemester), cids, weekIndex);
				Map<String, List<CourseSchedule>> clsScheduleMap = EntityUtils.getListMap(allScheduleList, CourseSchedule::getClassId,e->e);
				for (String cid : clsScheduleMap.keySet()) {
					List<CourseSchedule> clsSchedule = clsScheduleMap.get(cid);
					for (CourseSchedule cs : clsSchedule) {
						String timestrT = cs.getDayOfWeek()+"_"+cs.getPeriodInterval()+"_"+cs.getPeriod();
						if(destPeriod.equals(timestrT)) {
							if(cs.getSubjectType().equals(BaseConstants.SUBJECT_TYPE_VIRTUAL)) {
								return error("被调班级存在虚拟课程");
							}else {
								return error("目标时间有冲突");
							}
						}
					}
					
				}
			}
//			return error("暂不支持");
		}else if(BaseConstants.SCHEDULE_CONTENT_TYPE_P.equals(type)){
			List<String> tids = null;
			if(innerSchedules!=null)
				tids = Arrays.stream(innerSchedules).filter(e->e!=null && e.getTeacherId()!=null).map(e->e.getTeacherId()).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(tids)) {
				List<CourseSchedule> teacher1TimeList = courseScheduleService.findCourseScheduleListByTeacherIdIn(searchAcadyear,
						Integer.parseInt(searchSemester), weekIndex, tids.toArray(new String[0]));
				
				Map<String, List<CourseSchedule>> timesMap = teacher1TimeList.stream()  
						.collect(Collectors.groupingBy(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod()));
				if(timesMap.containsKey(destPeriod)) {
					Teacher teacher = teacherService.findOne(tids.get(0));
					return error("教师"+teacher.getTeacherName()+" 在目标时间存在冲突");
				}
				
//				outterSchedules2 = outterSchedules;
			}
		}else {
			return error("未知的类型");
		}
		
		// 检查 场地 和学生冲突
		List<CourseSchedule> savedCsList = null;
		int day = Integer.parseInt(timestr[0]);
		String periodInterval = timestr[1];
		int periodint = Integer.parseInt(timestr[2]);
		if(innerSchedules != null && innerSchedules.length>0) {
			List<String> placeIds = Arrays.stream(innerSchedules).filter(e->e!=null && e.getPlaceId()!=null).map(e->e.getPlaceId()).collect(Collectors.toList());
			List<String> upPlaceIds = null;
			if(outterSchedules != null)
				upPlaceIds = Arrays.stream(outterSchedules).filter(e->e!=null && e.getPlaceId()!=null).map(e->e.getPlaceId()).collect(Collectors.toList());
			// 待安排课程 需要场地 待删除的课程不需要场地再去 判断场地冲突
			
			if(BaseConstants.SCHEDULE_CONTENT_TYPE_T.equals(type)&&upPlaceIds!=null) {
				placeIds.removeAll(upPlaceIds);
			}
			if(!BaseConstants.SCHEDULE_CONTENT_TYPE_P.equals(type) && CollectionUtils.isNotEmpty(placeIds)) {
				if(BaseConstants.SCHEDULE_CONTENT_TYPE_T.equals(type) || CollectionUtils.isEmpty(upPlaceIds)) {
					
					// 添加的课程有教室 才需要判断教室冲突
					String msg = checkRoomConflicts(courseScheduleT, placeIds.get(0), destPeriod);
					if (StringUtils.isNotBlank(msg)) {
						return error(msg);
					}
				}
			}
			
			// 学生冲突; 待删除位置 没有课程才需要判断学生冲突
			if(!(BaseConstants.SCHEDULE_CONTENT_TYPE_C.equals(type) && (outterSchedules == null || outterSchedules.length==0))) {
				String msg = checkStudentConflicts(courseScheduleT, courseScheduleT.getClassId(), destPeriod);
				if (StringUtils.isNotBlank(msg)) {
					return error(msg);
				}
			}
			
			Date now = new Date();
			savedCsList = new ArrayList<>();
			for (CourseSchedule sc : innerSchedules) {
				// 前端 subjectId subjectType teacherId placeId   已经存在
				sc.setId(UuidUtils.generateUuid());
				sc.setCreationTime(now);
				sc.setModifyTime(now);
				sc.setGradeId(gradeId);
				sc.setSchoolId(unitId);
				sc.setAcadyear(searchAcadyear);
				sc.setSemester(semester);
				sc.setWeekOfWorktime(weekIndex);
				
				sc.setDayOfWeek(day);
				sc.setPeriodInterval(periodInterval);
				sc.setPeriod(periodint);
				sc.setClassType(CourseSchedule.CLASS_TYPE_NORMAL);
				sc.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
				if(sc.getWeekType() != CourseSchedule.WEEK_TYPE_EVEN && sc.getWeekType() != CourseSchedule.WEEK_TYPE_ODD)
					sc.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
				sc.setIsDeleted(0);
				sc.setPunchCard(1);
				
				savedCsList.add(sc);
			}
		}
		
		List<String> delOtherIds = Arrays.stream(outterSchedules==null?new CourseSchedule[0]:outterSchedules)
				.map(e->e.getId()).collect(Collectors.toList());
		
		// 保存结果
		try {
			courseScheduleService.saveScheduleModify(savedCsList, delOtherIds);
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(""+e1.getMessage());
		}
		
		return returnSuccess();
	}
	
	/**
	 * 将某班级 本周的课程覆盖到 其他周次
	 * @param gradeId
	 * @param classId
	 * @param weekIndex
	 * @return
	 */
	@RequestMapping("/coverAllWeek")
	@ResponseBody
	public String coverAllWeek(@PathVariable String gradeId, String searchAcadyear, String searchSemester, String classId, Integer weekIndex) {
		String unitId = getLoginInfo().getUnitId();
		
		if(StringUtils.isBlank(searchAcadyear)||StringUtils.isBlank(searchAcadyear)) {
			return error("无法获取学年学期信息");
		}
		if(StringUtils.isBlank(classId)) {
			return error("无法获取班级信息");
		}
		if(weekIndex == null) {
			return error("无法获取周次信息");
		}
		
		
		try {
			String msg = courseScheduleService.saveCoverAll(searchAcadyear,Integer.parseInt(searchSemester),unitId, classId, weekIndex);
			if(StringUtils.isNotBlank(msg))
				return error(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage()+"");
		}
		return success("操作成功");
	}
	
	private AdjustedDetail makeAdjustedDetail(CourseSchedule courseSchedule, String adjustId, String adjustedType) {
        AdjustedDetail adjustedDetail = new AdjustedDetail();
        adjustedDetail.setId(UuidUtils.generateUuid());
        adjustedDetail.setAdjustedId(adjustId);
        adjustedDetail.setCourseScheduleId(courseSchedule.getId());
        adjustedDetail.setClassId(courseSchedule.getClassId());
        adjustedDetail.setSubjectId(courseSchedule.getSubjectId());
        adjustedDetail.setSchoolId(courseSchedule.getSchoolId());
        adjustedDetail.setTeacherId(courseSchedule.getTeacherId());
        adjustedDetail.setWeekOfWorktime(courseSchedule.getWeekOfWorktime());
        adjustedDetail.setDayOfWeek(courseSchedule.getDayOfWeek());
        adjustedDetail.setPeriodInterval(courseSchedule.getPeriodInterval());
        adjustedDetail.setPeriod(courseSchedule.getPeriod());
        adjustedDetail.setCreationTime(new Date());
        adjustedDetail.setModifyTime(new Date());
        adjustedDetail.setAdjustedType(adjustedType);
        return adjustedDetail;
    }
	 private String checkStudentConflicts(CourseSchedule courseScheduleT, String classId,
				String destPeriod) {
			// 取出当前行政班的 学生
//			String classId = srcCourseSchedules.get(0).getClassId();
			List<Student> studentList = studentService.findByClassIds(classId);
//			List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classId), Student.class);
			Set<String> studentIds = EntityUtils.getSet(studentList, Student::getId);
			
			// 取出目标时间上课的 走班班级的学生
			String[] split = destPeriod.split("_");
			Integer dayOfWeek = Integer.valueOf(split[0]);
			String periodInterval = split[1];
			Integer period = Integer.valueOf(split[2]);
			
			String schoolId = courseScheduleT.getSchoolId();
			String acadyear = courseScheduleT.getAcadyear();
			int semester = courseScheduleT.getSemester();
			int weekOfWorktime = courseScheduleT.getWeekOfWorktime();
			List<CourseSchedule> dt1 = courseScheduleService.findCourseScheduleList(acadyear, semester, schoolId, weekOfWorktime,
					dayOfWeek, periodInterval, 1, period);
			List<CourseSchedule> dt2 = courseScheduleService.findCourseScheduleList(acadyear, semester, schoolId, weekOfWorktime,
					dayOfWeek, periodInterval, 0, period);
			dt1.addAll(dt2);
			Set<String> classIds = dt1.stream().map(CourseSchedule::getClassId).collect(Collectors.toSet());
			
			List<TeachClassStu> teachStus = teachClassStuService.findByClassIds(classIds.toArray(new String[0]));
			Set<String> destStudentIds = EntityUtils.getSet(teachStus, TeachClassStu::getStudentId);
			
			boolean anyMatch = studentIds.stream().anyMatch(e->destStudentIds.contains(e));
			if(anyMatch) {
				return "存在学生冲突";
			}
			return null;
		}
	
	private String checkRoomConflicts(CourseSchedule courseScheduleT, String placeId,
			String destPeriod) {
		if(StringUtils.isBlank(placeId)) {
			return null;
		}
		List<CourseSchedule> roomUseageList = courseScheduleService.findCourseScheduleListByPlaceId(courseScheduleT.getSchoolId(), courseScheduleT.getAcadyear(),
				courseScheduleT.getSemester(), placeId, courseScheduleT.getWeekOfWorktime(), false);
		Set<String> roomTimes = roomUseageList.stream()
				.map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())
				.collect(Collectors.toSet());
		if(roomTimes.contains(destPeriod)) {
			// 重合
			return "场地冲突";
		}
		return null;
	}
	
	private String checkTeacherConflicts(String[] srcCourseScheduleIds, String srcPeriod, String destPeriod,
			Map<String, List<CourseSchedule>> teacher1TimeMap) {

		Set<String> oldTimeOtherIds = Arrays.asList(srcCourseScheduleIds).stream().collect(Collectors.toSet());
		for (String teacherId : teacher1TimeMap.keySet()) {
			List<CourseSchedule> timesT = teacher1TimeMap.get(teacherId);
			Map<String, List<CourseSchedule>> timesMap = timesT.stream()  
//				.map(e->e.getDayOfWeek()+"-"+e.getPeriodInterval()+"-"+e.getPeriod())
				.collect(Collectors.groupingBy(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod()));
			
			if(timesMap.containsKey(destPeriod)) {
				List<CourseSchedule> list = timesMap.get(destPeriod);
				if(list.size() >= 2) {
					// 交换的目标 位置有两节课
					return error("存在教师冲突，不能交换");
				}
				if(timesMap.get(srcPeriod) == null) {
					return error("数据已过期，请刷新页面！");
				}
				
				List<CourseSchedule> leftList = timesMap.get(srcPeriod).stream()
						.filter(e->oldTimeOtherIds.contains(e.getId()))
						.collect(Collectors.toList());
				if(leftList.size() > 2) {
					// 交换的原始 位置有两节课
					return error("存在教师冲突，不能交换");
				}
				if(CollectionUtils.isEmpty(leftList)) {
					return error("数据已过期，请刷新页面！");
				}
				CourseSchedule other = list.get(0);
				CourseSchedule left = leftList.get(0);
				if(left.getWeekType() + other.getWeekType() != 3) {
					// 交换的 两个位置 不是一单 一双
					return error("存在教师冲突，不能交换");
				}
			}
		}
		return null;
	}
	
	@RequestMapping("/teacherSchedule")
	@ResponseBody
	public String teacherSchedule(@PathVariable String gradeId,
			@RequestParam(name = "teacherIds[]", required = false) String[] teacherIds, String searchAcadyear,
			String searchSemester, int weekIndex) {
		if(teacherIds == null || teacherIds.length <1) {
			return "{}";
		}
		Set<String> teacherIdSet = Arrays.stream(teacherIds).collect(Collectors.toSet());

		String acadyear = searchAcadyear;
		Integer semester = Integer.valueOf(searchSemester);
		Integer weekOfWorktime = weekIndex;
		List<CourseSchedule> scheduleList = courseScheduleService.findCourseScheduleListByTeacherIdIn(acadyear, semester, weekOfWorktime,
						teacherIdSet.toArray(new String[0]));

		courseScheduleService.makeScheduleInfo(scheduleList, "1");
		
		Map<String, List<CourseSchedule>> teacherScheduleMap = EntityUtils.getListMap(scheduleList, CourseSchedule::getTeacherId,Function.identity());
		String s = SUtils.s(teacherScheduleMap);
		
		return s;
	}
	@RequestMapping("/classSchedule")
	@ResponseBody
	public String classSchedule(@PathVariable String gradeId,
			@RequestParam(name = "classIds[]", required = false) String[] classIds, String searchAcadyear,
			String searchSemester, int weekIndex) {
		if(classIds == null || classIds.length <1) {
			return "{}";
		}
		List<CourseSchedule> scheduleList = courseScheduleService.findCourseScheduleListByClassIdes(searchAcadyear, 
        		Integer.valueOf(searchSemester), classIds, weekIndex);
        courseScheduleService.makeScheduleInfo(scheduleList,"1");
		
		Map<String, List<CourseSchedule>> classScheduleMap = EntityUtils.getListMap(scheduleList, CourseSchedule::getClassId,Function.identity());
		String s = SUtils.s(classScheduleMap);
		
		return s;
	}
	
	@RequestMapping("/classSurplusHour")
	@ResponseBody
	public String getClassSurplusHour(@PathVariable String gradeId, String searchAcadyear, String searchSemester, int weekIndex) {
		String unitId = getLoginInfo().getUnitId();
		// 根据班级课程特征 每个班级应该有多少课时 key:classId
        List<Grade> gradeList = gradeService.findBySchoolIdAndIsGraduate(unitId, 0);
        String[] gradeIds = gradeList.stream().map(e->e.getId()).toArray(e -> new String[e]);
        Map<String,Integer> classTeachingHourMap = classTeachingService.getClassTeachingHourMap(unitId, gradeIds, searchAcadyear, searchSemester, weekIndex);
		
		
		// 根据课表 每个班级实际有多少课时 key:classId
		Map<String,Integer> classRealHourMap = courseScheduleService.getClassRealHour(unitId, gradeIds, searchAcadyear, searchSemester, weekIndex);
		
		for (String classId : classTeachingHourMap.keySet()) {
			Integer  ctHour = classTeachingHourMap.get(classId);
			
			Integer realHour = classRealHourMap.get(classId);
			if(realHour != null)
				classTeachingHourMap.put(classId, ctHour - realHour);
			else
				classTeachingHourMap.put(classId, ctHour);
		}
		
		Map<String,Integer> surplusMap = classTeachingHourMap;
		
		Json json = new Json();
		json.put("success", true);
		json.put("msg", "");
		json.put("map", surplusMap);
		
		return json.toJSONString();
	}
	
	@RequestMapping("/myscript-array2")
	public String getArrayJs(String type) {
		if(BaseConstants.SCHEDULE_CONTENT_TYPE_P.equals(type)) {
			return "/basedata/courseSchedule/modify/mutil/myscript-array-p.js";
		}else if(BaseConstants.SCHEDULE_CONTENT_TYPE_T.equals(type)) {
			return "/basedata/courseSchedule/modify/mutil/myscript-array-t.js";
		}
		return "/basedata/courseSchedule/modify/mutil/myscript-array2.js";
	}
}
