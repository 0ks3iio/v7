package net.zdsoft.newgkelective.data.action;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.ClassFeatureDto;
import net.zdsoft.basedata.dto.CourseScheduleModifyDto;
import net.zdsoft.basedata.dto.PeriodSwapDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.TeachGroupRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.NewGkArrayResultSaveDto;
import net.zdsoft.newgkelective.data.dto.NewGkClassFeatureDto;
import net.zdsoft.newgkelective.data.entity.NewGkArray;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkPlaceItem;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;
import net.zdsoft.newgkelective.data.entity.NewGkTimetable;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;
import net.zdsoft.newgkelective.data.service.NewGkArrayService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassStudentService;
import net.zdsoft.newgkelective.data.service.NewGkClassSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkPlaceItemService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableOtherService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableTeacherService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/newgkelective/scheduleModify/mutil/{arrayId}")
//@RequestMapping("/basedata/scheduleModify/{location}")
public class NewGkArrayTimetableMutilModifyAction extends BaseAction{
	@Autowired
	private NewGkTimetableService newGkTimetableService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private NewGkArrayService newGkArrayService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private NewGkPlaceItemService newGkPlaceItemService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private NewGkClassStudentService newGkClassStudentService;
	@Autowired
	private NewGkTimetableTeacherService newGkTimetableTeacherService;
	@Autowired
	private NewGkTimetableOtherService newGkTimetableOtherService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private NewGkClassSubjectTimeService newGkClassSubjectTimeService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkClassCombineRelationService combineRelationService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeachGroupRemoteService teachGroupRemoteService;

	/**
	 * 
	 * @param arrayId 智能排课则 为arrayId,基础数据则是 gradeId
	 * @param type 处于 智能排课还是 基础数据
	 * @return
	 */
	@RequestMapping("/index")
	public String scheduleModifyIndex(@PathVariable String arrayId, @RequestParam(name="arrayIds") String[] arrayIds,
									  String type, ModelMap map) {
		List<NewGkArray> arrayList = newGkArrayService.findListByIds(arrayIds);

		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();
		List<Clazz> classList = new ArrayList<>();
		Map<String,Map<String,String>> classSubjTeacherMap = new HashMap<>();
		Map<String, String> teacherNameMap = new HashMap<>();
		Map<String, String> classPlaceMap = new HashMap<>();
		Map<String, String> placeNameMap = new HashMap<>();
		Set<String> noNeedPlaceCourse = new HashSet<>();
		List<String> gradeNoClick = new ArrayList<>();
		Map<String, List<String>> classMovePeriodMap = new HashMap<>();
		Map<String, Set<String>> teacherMoveTimeMap = new HashMap<>();
		Map<String, Set<String>> placeMoveTimeMap = new HashMap<>();
		List<String> xzbPeriodList = new ArrayList<>();
		List<NewGkDivideClass> allClassList = new ArrayList<>();
		List<Set<String>> combineRelations = new ArrayList<>();
		Set<String> pure3XzbIds = new HashSet<>();
		Map<String, String> teacherNoTimeMap = new HashMap<>();
		Grade grade = null;
		Map<String, Integer> piMap = null;
		Map<String, Float> lectureSurplus = new HashMap<>();
		Map<String,String> objArrayIdMap = new HashMap<>();
		String gradeId = null;

//		String arrayId = objId;

		Map<String, String> arrgradeMap = EntityUtils.getMap(arrayList, e -> e.getId(), e -> e.getGradeId());
		List<Grade> gradeList = gradeRemoteService.findListObjectByIds(arrgradeMap.values().toArray(new String[0]));
		Map<String, String> gradeCodeMap = EntityUtils.getMap(gradeList, e -> e.getId(), e -> e.getGradeCode());
		arrayList.sort(Comparator.comparing(x -> gradeCodeMap.get(x.getGradeId())));
		for (NewGkArray newGkArray : arrayList) {
			String arrayid = newGkArray.getId();
			ParamEntity paramEntity = new ParamEntity(arrayid, type, unitId).invoke();
			if (paramEntity.is())
				return errorFtl(map, "旧数据不支持手动调课，请使用或创建新的排课方案");


			type = paramEntity.getType();
			classList.addAll(paramEntity.getClassList());
			teacherNameMap.putAll(paramEntity.getTeacherNameMap());
			classPlaceMap.putAll(paramEntity.getClassPlaceMap());
			placeNameMap.putAll(paramEntity.getPlaceNameMap());
			noNeedPlaceCourse.addAll(paramEntity.getNoNeedPlaceCourse());
			//TODO 年级禁排时间
			gradeNoClick.addAll(paramEntity.getGradeNoClick());
			classMovePeriodMap.putAll(paramEntity.getClassMovePeriodMap());
			teacherMoveTimeMap.putAll(paramEntity.getTeacherMoveTimeMap());
			placeMoveTimeMap.putAll(paramEntity.getPlaceMoveTimeMap());
			classSubjTeacherMap.putAll(paramEntity.getClassSubjTeacherMap());
			xzbPeriodList = paramEntity.getXzbPeriodList();
			allClassList.addAll(paramEntity.getAllClassList());
			combineRelations.addAll(paramEntity.getCombineRelations());
			pure3XzbIds.addAll(paramEntity.getPure3XzbIds());
			teacherNoTimeMap.putAll(paramEntity.getTeacherNoTimeMap());
			Grade gradeTemp = paramEntity.getGrade();
			if(grade == null){
				grade = gradeTemp;
			}else if(!Objects.equals(grade.getMornPeriods(),gradeTemp.getMornPeriods())
				|| !Objects.equals(grade.getAmLessonCount(),gradeTemp.getAmLessonCount())
				|| !Objects.equals(grade.getPmLessonCount(),gradeTemp.getPmLessonCount())
				|| !Objects.equals(grade.getNightLessonCount(),gradeTemp.getNightLessonCount())
				|| !Objects.equals(grade.getWeekDays(),gradeTemp.getWeekDays())){
				return errorFtl(map,"请保持各年级授课时间相同");
			}
			if(piMap == null){
				piMap = paramEntity.getPiMap();
			}else if(!piMap.equals(paramEntity.getPiMap())){
				return errorFtl(map,"请保持各年级授课时间相同");
			}
			lectureSurplus.putAll(paramEntity.getLectureSurplus());

			for (NewGkDivideClass acls : paramEntity.getAllClassList()) {
				acls.setClassName(gradeTemp.getGradeName()+acls.getClassName());
			}
			for (Clazz clazz : paramEntity.getClassList()) {
				objArrayIdMap.put(clazz.getId(),newGkArray.getId());
				clazz.setClassName(gradeTemp.getGradeName()+clazz.getClassName());
			}
			for (String tid : paramEntity.getTeacherNameMap().keySet()) {
				objArrayIdMap.put(tid,newGkArray.getId());
			}
			for (String pid : paramEntity.getPlaceNameMap().keySet()) {
				objArrayIdMap.put(pid,newGkArray.getId());
			}
		}

		// 根据教研组 去排序
		List<TeachGroup> allTeacherGroup = SUtils.dt(teachGroupRemoteService.findBySchoolId(unitId,true), TeachGroup.class);
		Map<String,Integer> tidOrderMap = new HashMap<>();
		AtomicInteger in = new AtomicInteger(0);
		for (TeachGroup dto : allTeacherGroup) {
			dto.getTeacherIdSet().forEach(e->tidOrderMap.put(e,in.getAndAdd(1)));
		}
		in.set(10000);
		Map<String,String> teacherNameTemps = new TreeMap<>(Comparator.comparing(x -> tidOrderMap.computeIfAbsent(x, e -> in.getAndDecrement())));
		teacherNameTemps.putAll(teacherNameMap);
		teacherNameMap = teacherNameTemps;
		// 场地排序
		List<TeachPlace> placeList = teachPlaceRemoteService.findListObjectByIds(placeNameMap.keySet().toArray(new String[0]));
		placeNameMap = placeList.stream().sorted(Comparator.comparing(TeachPlace::getPlaceCode))
				.collect(Collectors.toMap(e -> e.getId(), e -> e.getPlaceName(), (e1, e2) -> e1, () -> new LinkedHashMap<>()));

		Map<String, NewGkDivideClass> allClassMap = EntityUtils.getMap(allClassList, NewGkDivideClass::getId);
		
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("piMap", piMap);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
		
		map.put("lectureSurplus", lectureSurplus );
		map.put("allClassMap", allClassMap );
		map.put("classList", classList );
		map.put("type", type );
		map.put("indexId", arrayId );
		map.put("teacherNameMap", teacherNameMap );
		map.put("classSubjTeacherMap", classSubjTeacherMap );
		map.put("classPlaceMap", classPlaceMap );
		map.put("placeNameMap", placeNameMap );
		map.put("noNeedPlaceCourse", noNeedPlaceCourse );
		map.put("gradeNoClick", gradeNoClick);
		map.put("teacherNoTimeMap", teacherNoTimeMap);
		map.put("classMovePeriodMap", classMovePeriodMap);
		map.put("teacherMoveTimeMap", teacherMoveTimeMap);
		map.put("placeMoveTimeMap", placeMoveTimeMap);
		map.put("xzbPeriodList", xzbPeriodList);
		map.put("pure3XzbIds", pure3XzbIds);
		map.put("isXzb", true);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("objArrayIdMap",objArrayIdMap);
		jsonObject.put("arrayIds",arrayIds);
		map.put("jsonDataStr", jsonObject.toJSONString());

		
		return "/basedata/courseSchedule/modify/mutil/modifyIndex.ftl";
	}

	/**
	 * 获取行政班 教师 场地 等元素 对应的走班时间 作为固定 不可移动时间
	 * @param arrayId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMovePeriods")
	public String updateMovePeriods(@PathVariable String arrayId){

		Map<String, Set<String>> teacherMoveTimeMap = new HashMap<>();
		Map<String, Set<String>> placeMoveTimeMap = new HashMap<>();
		Map<String, List<String>> classMovePeriodMap = newGkTimetableService.findClassMovePeriodMap(arrayId, teacherMoveTimeMap, placeMoveTimeMap);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success",true);
		jsonObject.put("teacherMoveTimeMap",teacherMoveTimeMap);
		jsonObject.put("placeMoveTimeMap",placeMoveTimeMap);
		jsonObject.put("classMovePeriodMap",classMovePeriodMap);

		return jsonObject.toString();
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
	public String classOrTeacher(@PathVariable String arrayId, @RequestParam(name="arrayIds") String[] arrayIds,
								 Boolean useMaster, String cType, String objId, ModelMap map) {
		if(StringUtils.isBlank(arrayId) || ArrayUtils.isEmpty(arrayIds)){
			return errorFtl(map,"arrayId 信息为空");
		}


		List<CourseSchedule> scheduleList = new ArrayList<>();
		List<List<ClassFeatureDto>> classSubjectList = new ArrayList<>();
		Set<String> fixedSubjects = null;
		String gradeId = null;

		Map<String, String> arrayGradeNameMap = getArrGradeMap(arrayIds);


		NewGkArray array = newGkArrayService.findOne(arrayId);
		gradeId = array.getGradeId();
		CourseScheduleModifyDto dt = null;
		if(BaseConstants.SCHEDULE_CONTENT_TYPE_C.equals(cType)) {
			// 显示班级课程表
			// 此班级需要上的课程 以及 节次数
			if(Objects.equals(useMaster, true)) {
				dt = newGkTimetableService.findClassScheduleInfoWithMaster(arrayId, objId);
			}else {
				dt = newGkTimetableService.findClassScheduleInfo(arrayId, objId);
			}
			
			scheduleList = dt.getScheduleList();
			classSubjectList = dt.getClassSubjectList();
			Set<String> codes = classSubjectList.stream().flatMap(e->e.stream()).map(ClassFeatureDto::getSubjectCode).collect(Collectors.toSet());
			fixedSubjects = scheduleList.stream()
					.distinct().filter(e -> !codes.contains(e.getSubjectId() + "-" + e.getSubjectType()))
					.map(e->e.getSubjectId())
					.collect(Collectors.toSet());
			Set<String> teacherIdSet = scheduleList.stream().filter(e->StringUtils.isNotBlank(e.getTeacherId())).map(e->e.getTeacherId()).collect(Collectors.toSet());
			classSubjectList.stream().flatMap(e->e.stream()).filter(e->e.getTeacherId()!=null).forEach(e->teacherIdSet.add(e.getTeacherId()));
			// TODO 获取教师在所有 排课方案的课程
			List<CourseSchedule> tScheduleList = new ArrayList<>();
			for (String arrId : arrayIds) {
				List<CourseSchedule> tScheduleTemps = newGkTimetableService.makeScheduleByTeacher(arrId, teacherIdSet);
				tScheduleList.addAll(tScheduleTemps);
			}
			Map<String, List<CourseSchedule>> teacherScheduleMap = tScheduleList.stream()
					.filter(e->StringUtils.isNotBlank(e.getTeacherId()))
					.collect(Collectors.groupingBy(CourseSchedule::getTeacherId));
			map.put("teacherScheduleMap", teacherScheduleMap);
			
			NewGkDivideClass divideClass = newGkDivideClassService.findOne(objId);
			String oldId = divideClass.getOldDivideClassId();
			map.put("oldId", oldId);
		}else if(BaseConstants.SCHEDULE_CONTENT_TYPE_T.equals(cType)) {
			// 显示老师课表
			String teacherId = objId;
			for (String arrId : arrayIds) {
				if(Objects.equals(useMaster, true)) {
					dt = newGkTimetableService.findTeacherScheduleInfoWithMaster(arrId, teacherId);
				}else {
					dt = newGkTimetableService.findTeacherScheduleInfo(arrId, teacherId);
				}
				List<CourseSchedule> scheduleTemps = dt.getScheduleList();
				scheduleTemps.forEach(e->e.setClassName(arrayGradeNameMap.get(arrId)+e.getClassName()));
				List<List<ClassFeatureDto>> classSubjectTemps = dt.getClassSubjectList();
				scheduleList.addAll(scheduleTemps);
				classSubjectList.addAll(classSubjectTemps);
			}
		}else if(BaseConstants.SCHEDULE_CONTENT_TYPE_P.equals(cType)){
			// 显示场地课表 以及 底部 课时信息  以 班级 + 科目 为 单位
			String placeId = objId;
			for (String arrId : arrayIds) {
				if(Objects.equals(useMaster, true)) {
					dt = newGkTimetableService.findPlaceScheduleInfoWithMaster(arrId, placeId);
				}else {
					dt = newGkTimetableService.findPlaceScheduleInfo(arrId, placeId);
				}
				List<CourseSchedule> scheduleTemps = dt.getScheduleList();
				scheduleTemps.forEach(e->e.setClassName(arrayGradeNameMap.get(arrId)+e.getClassName()));
				List<List<ClassFeatureDto>> classSubjectTemps = dt.getClassSubjectList();
				scheduleList.addAll(scheduleTemps);
				classSubjectList.addAll(classSubjectTemps);
			}

			Set<String> codes = classSubjectList.stream().flatMap(Collection::stream).map(ClassFeatureDto::getSubjectCode).collect(Collectors.toSet());
			fixedSubjects = scheduleList.stream()
					.distinct().filter(e -> !codes.contains(e.getSubjectId() + "-" + e.getSubjectType()))
					.map(CourseSchedule::getSubjectId)
					.collect(Collectors.toSet());
			
			// 走班课程;TODO 暂时不需要了
//			Set<String> movePeriods = scheduleList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()+""))
//					.map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod()).collect(Collectors.toSet());
//			map.put("movePeriods", movePeriods);
			// 所有教师的课表
			Set<String> teacherIdSet = scheduleList.stream().filter(e->StringUtils.isNotBlank(e.getTeacherId())).map(CourseSchedule::getTeacherId).collect(Collectors.toSet());
			List<CourseSchedule> tScheduleList = new ArrayList<>();
			for (String arrId : arrayIds) {
				List<CourseSchedule> tScheduleTemps = newGkTimetableService.makeScheduleByTeacher(arrId, teacherIdSet);
				tScheduleList.addAll(tScheduleTemps);
			}
			Map<String, List<CourseSchedule>> teacherScheduleMap = tScheduleList.stream()
					.filter(e->StringUtils.isNotBlank(e.getTeacherId()))
					.collect(Collectors.groupingBy(CourseSchedule::getTeacherId));
			map.put("teacherScheduleMap", teacherScheduleMap);
			
			Set<String> relaCids = classSubjectList.stream().flatMap(Collection::stream).map(ClassFeatureDto::getClassId).collect(Collectors.toSet());
			Set<String> collect = scheduleList.stream().map(CourseSchedule::getClassId).collect(Collectors.toSet());
			relaCids.addAll(collect);
			map.put("relaCids", relaCids);
//			NewGkDivideClass divideClass = newGkDivideClassService.findOne(objId);
//			String oldId = divideClass.getOldDivideClassId();
//			map.put("oldId", oldId);
		}else {
			return errorFtl(map, "未知的contentType类型");
		}
		
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
				if(list.size()==2 
						&& (list.get(0).getWeekType()+list.get(1).getWeekType()) == NewGkElectiveConstant.WEEK_TYPE_NORMAL) {
					list.sort(Comparator.comparingInt(CourseSchedule::getWeekType));
				}else if(list.size()<2) {
					list.forEach(e->{
						if(e.getWeekType() == NewGkElectiveConstant.WEEK_TYPE_EVEN) {							
							e.setSubjectName(e.getSubjectName()+"(双)");
						}else if(e.getWeekType() == NewGkElectiveConstant.WEEK_TYPE_ODD){
							e.setSubjectName(e.getSubjectName()+"(单)");
						}
					});
				}
			}
		}
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		Map<String, Integer> piMap = getIntervalMap(grade);
		
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("piMap", piMap);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
		
		map.put("fixedSubjects", fixedSubjects);
		map.put("scheduleMap", scheduleMap);
		map.put("classSubjectList", classSubjectList);
//		map.put("gradeNoClick", gradeNoClick);
		
		map.put("indexId", arrayId);
		map.put("objId", objId);
		map.put("type", cType);
		
		return "/basedata/courseSchedule/modify/mutil/mainScheduleList.ftl";
	}

	private Map<String, String> getArrGradeMap(@RequestParam(name = "arrayIds") String[] arrayIds) {
		List<NewGkArray> arrayList = newGkArrayService.findListByIds(arrayIds);
		Set<String> gradeIds = EntityUtils.getSet(arrayList, e -> e.getGradeId());
		List<Grade> grades = gradeRemoteService.findListObjectByIds(gradeIds.toArray(new String[0]));
		Map<String, String> gradeNameMap = EntityUtils.getMap(grades, e -> e.getId(), e -> e.getGradeName());
		Map<String, String> arrayGradeNameMap = new HashMap<>();
		for (NewGkArray array : arrayList) {
			arrayGradeNameMap.put(array.getId(), gradeNameMap.computeIfAbsent(array.getGradeId(), e -> ""));
		}
		return arrayGradeNameMap;
	}

	@RequestMapping("/classSchedule")
	@ResponseBody
	public String getClassSchedule(@PathVariable String arrayId, @RequestParam(name="classIds[]",required=false) String[] classIds) {
		if(classIds == null || classIds.length <1) {
			return "{}";
		}
		String unitId = getLoginInfo().getUnitId();
		List<CourseSchedule> scheduleList = newGkTimetableService.findScheduleByClass(unitId, arrayId, classIds);
		Map<String, List<CourseSchedule>> teacherScheduleMap = EntityUtils.getListMap(scheduleList, CourseSchedule::getClassId,Function.identity());
		
		String s = SUtils.s(teacherScheduleMap);
		
		return s;
	}
	
	@RequestMapping("/swapCourse")
	@ResponseBody
	public String swapCourse(@PathVariable String arrayId, @RequestParam(name="arrayIds") String[] arrayIds,
							 PeriodSwapDto dto, String timestr) {
		CourseSchedule[] leftSchedules = dto.getLeftSchedules();
		CourseSchedule[] rightSchedules = dto.getRightSchedules();
		
		String unitId = getLoginInfo().getUnitId();
//		String lessonArrangeId = array.getLessonArrangeId();
		// 先比较教师冲突
		String leftPeriod = dto.getLeftPeriod();
		String rightPeriod = dto.getRightPeriod();
		String classId = leftSchedules[0].getClassId();
		NewGkDivideClass one1 = newGkDivideClassService.findOne(classId);
		String oldDivideClassId = one1.getOldDivideClassId();
		Arrays.stream(leftSchedules).forEach(e->e.setOldDivideClassId(oldDivideClassId));
		if(rightSchedules != null) {
			Arrays.stream(rightSchedules).forEach(e->e.setOldDivideClassId(oldDivideClassId));
		}

		List<Set<String>> combineGroups = new ArrayList<>();
		for (String arrId : arrayIds) {
			NewGkArray one = newGkArrayService.findOne(arrId);
			List<Set<String>> combineGroupTemps = combineRelationService.getCombineRelation(unitId,one.getLessonArrangeId());
			combineGroups.addAll(combineGroupTemps);
		}
		String msg = checkClassConflicts(arrayIds, one1.getDivideId(), leftSchedules, rightSchedules, leftPeriod, rightPeriod,
				null, null, null, combineGroups);
		if(StringUtils.isNotBlank(msg)) {
			return error(msg);
		}

		// 保存结果
		List<String> ttoIds = new ArrayList<>();
		ttoIds.add(leftSchedules[0].getId());
		if(leftSchedules.length>1)
			ttoIds.add(leftSchedules[1].getId());
		List<NewGkTimetableOther> timetableOthers = newGkTimetableOtherService.findListByIdIn(ttoIds.toArray(new String[0]));
		
		String[] split = rightPeriod.split("_");
		for (NewGkTimetableOther newGkTimetableOther : timetableOthers) {
			newGkTimetableOther.setDayOfWeek(Integer.parseInt(split[0]));
			newGkTimetableOther.setPeriodInterval(split[1]);
			newGkTimetableOther.setPeriod(Integer.parseInt(split[2]));
		}
		List<NewGkTimetableOther> timetableOthers2 = new ArrayList<>();
		if(rightSchedules!=null && rightSchedules[0].getId()!=null) {
			ttoIds = new ArrayList<>();
			ttoIds.add(rightSchedules[0].getId());
			if(rightSchedules.length>1)
				ttoIds.add(rightSchedules[1].getId());
			timetableOthers2 = newGkTimetableOtherService.findListByIdIn(ttoIds.toArray(new String[0]));
			String timestrL = dto.getLeftPeriod();
			split = timestrL.split("_");
			for (NewGkTimetableOther newGkTimetableOther : timetableOthers2) {
				newGkTimetableOther.setDayOfWeek(Integer.parseInt(split[0]));
				newGkTimetableOther.setPeriodInterval(split[1]);
				newGkTimetableOther.setPeriod(Integer.parseInt(split[2]));
			}
		}
		try {
			List<NewGkTimetableOther> ttos = new ArrayList<>();
			ttos.addAll(timetableOthers);
			ttos.addAll(timetableOthers2);
			newGkTimetableOtherService.saveAll(ttos.toArray(new NewGkTimetableOther[] {}));
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		
		return success("成功");
	}

	/**
	 * 
	 *
	 * @param arrayIds
	 * @param arrayId
	 * @param leftSchedules
	 * @param rightSchedules
	 * @param leftPeriod
	 * @param rightPeriod
	 * @param extraTeaIds 比较老师的冲突情况  不再包含这位老师
	 * @param extraCids 比较这些班级 的 学生冲突 不包含这些班级
	 * @param teacherWeekType
	 * @return
	 */
	private String checkClassConflicts(String[] arrayIds, String arrayId, CourseSchedule[] leftSchedules,
									   CourseSchedule[] rightSchedules, String leftPeriod, String rightPeriod, Set<String> extraTeaIds,
									   Set<String> extraCids, Map<String, Integer> teacherWeekType, List<Set<String>> combineGroups) {
		if((leftSchedules!=null && leftSchedules.length>2) || (rightSchedules!=null && rightSchedules.length>2)) {
			return "课表本身存在时间冲突";
		}
		if(rightPeriod == null) {
			// 目标时间点 为空 ，是减少课程 无需冲突 比较
			return null;
		}
		
		String msg = checkTeacherConflict(arrayIds, arrayId, leftSchedules, rightPeriod, leftPeriod, teacherWeekType,extraTeaIds,combineGroups);
		if(StringUtils.isNotBlank(msg))
			return msg;
		msg = checkTeacherConflict(arrayIds, arrayId, rightSchedules, leftPeriod, rightPeriod, teacherWeekType,extraTeaIds,combineGroups);
		if(StringUtils.isNotBlank(msg))
			return msg;
		
		
		String unitId = getLoginInfo().getUnitId();
		String timestrT = rightPeriod;
		String[] split = timestrT.split("_");
		NewGkTimetableOther tto = new NewGkTimetableOther();
		tto.setDayOfWeek(Integer.parseInt(split[0]));
		tto.setPeriodInterval(split[1]);
		tto.setPeriod(Integer.parseInt(split[2]));
		// place and student
		if(rightSchedules==null || rightSchedules.length < 1 || rightSchedules[0] == null || rightSchedules[0].getId() == null) {
			//教室学生这个班级的所有 学生
			msg = checkStuConflict(arrayId, unitId, leftSchedules, extraCids, tto);
			if(StringUtils.isNotBlank(msg))
				return msg;
		}
		if(true) {
			// 场地 比较
			String placeId = Arrays.stream(leftSchedules).filter(e->StringUtils.isNotBlank(e.getPlaceId())).map(e->e.getPlaceId()).findFirst().orElse(null);
			String placeId2 = Arrays.stream(rightSchedules==null?new CourseSchedule[0]:rightSchedules).filter(e->StringUtils.isNotBlank(e.getPlaceId())).map(e->e.getPlaceId()).findFirst().orElse(null);
			if(StringUtils.isNotBlank(placeId) && StringUtils.isBlank(placeId2)) {
				msg = checkPlaceConflict(unitId, arrayIds, arrayId, timestrT, placeId);
				if(StringUtils.isNotBlank(msg))
					return msg;
			}else if(StringUtils.isNotBlank(placeId2) && StringUtils.isBlank(placeId)) {
				timestrT = leftPeriod;
				msg = checkPlaceConflict(unitId, arrayIds, arrayId, timestrT, placeId2);
				if(StringUtils.isNotBlank(msg))
					return msg;
			}
		}
		return null;
	}

	private String checkStuConflict(String arrayId, String unitId, CourseSchedule[] leftSchedules, Set<String> extraCids,
			NewGkTimetableOther tto) {
		String classId = leftSchedules[0].getClassId();
		
		Set<String> studentIds = newGkClassStudentService.findSetByClassIds(unitId, arrayId, new String[] {classId});
		
		List<String> searchClassTypes = Stream.of(NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,
				NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4).collect(Collectors.toList());
//		String leftClassType = leftSchedules[0].getClassType()+"";
//		if(StringUtils.isNotBlank(leftClassType) && !Objects.equals(NewGkElectiveConstant.CLASS_TYPE_2, leftClassType)) {
//			// 除了 classType=2 教学班以外 其他类型的 班级 不会和 同类型的其他班级 有相同的学生
//			searchClassTypes.remove(leftClassType);
//		}
		List<String> classIdsSet = newGkDivideClassService.findByClassTypeAndTime(arrayId, tto, searchClassTypes.toArray(new String[0]));
		classIdsSet.removeAll(extraCids==null?new HashSet<>():extraCids);
		Set<String> destStudentIds = newGkClassStudentService.findSetByClassIds(unitId, arrayId, classIdsSet.toArray(new String[0]));
		
		boolean anyMatch = studentIds.stream().anyMatch(e->destStudentIds.contains(e));
		if(anyMatch) {
			return "存在学生冲突";
		}
		return null;
	}

	private String checkPlaceConflict(String unitId, String[] arrayIds, String arrayId, String timestrT, String placeId) {
		List<NewGkTimetableOther> ttos = new ArrayList<>();
		if(ArrayUtils.isNotEmpty(arrayIds)){
			for (String arrId : arrayIds) {
				List<NewGkTimetableOther> ttoTemps = newGkTimetableOtherService.findByArrayIdAndPlaceId(unitId,arrId,placeId);
				ttos.addAll(ttoTemps);
			}
		}else{
			ttos = newGkTimetableOtherService.findByArrayIdAndPlaceId(unitId,arrayId,placeId);
		}
		Set<String> placeTimes = ttos.stream().map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod()).collect(Collectors.toSet());
		if(placeTimes.contains(timestrT)) {
			return "存在场地冲突";
		}
		return null;
	}

	@RequestMapping("/swapTeacherCourse")
	@ResponseBody
	public String swapTeacherCourse(@PathVariable String arrayId, @RequestParam(name="arrayIds") String[] arrayIds,
									PeriodSwapDto dto, String timestr) {
		CourseSchedule[] leftSchedules = dto.getLeftSchedules();
		CourseSchedule[] rightSchedules = dto.getRightSchedules();
		
		String teacherId = leftSchedules[0].getTeacherId();
		String leftPeriod = dto.getLeftPeriod();
		String rightPeriod = dto.getRightPeriod();
		
		String[] leftTimes = leftPeriod.split("_");
		String[] rightTimes = rightPeriod.split("_");
		
		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();
		NewGkArray array = newGkArrayService.findOne(arrayId);
//		String lessonArrangeId = array.getLessonArrangeId();
		/* 最多一共比较4次 冲突 */
		Set<String> cids = Arrays.asList(leftSchedules).stream().map(e->e.getClassId()).collect(Collectors.toSet());
		if(rightSchedules!=null) {
			Set<String> cids2 = Arrays.asList(rightSchedules).stream().map(e->e.getClassId()).collect(Collectors.toSet());
			cids.addAll(cids2);
		}
		List<NewGkArray> arrayList = newGkArrayService.findListByIds(arrayIds);
		Map<String,String> arrMap = new HashMap<>();
		List<CourseSchedule> allScheduleList = new ArrayList<>();
		Map<String,String> clsArrIdMap = new HashMap<>();
		for (NewGkArray arr : arrayList) {
			String arrId = arr.getId();
			List<CourseSchedule> allScheduleTemps = newGkTimetableService.findScheduleByClass(unitId, arrId, cids.toArray(new String[0]));
			allScheduleList.addAll(allScheduleTemps);
			allScheduleTemps.forEach(e->clsArrIdMap.put(e.getClassId(),arrId));
			arrMap.put(arrId,arr.getLessonArrangeId());
		}
		Map<String, List<CourseSchedule>> cidScheduleMap = EntityUtils.getListMap(allScheduleList, CourseSchedule::getClassId, Function.identity());
		List<NewGkTimetableOther> saveList = new ArrayList<>();
		Set<String> extraTids = Stream.of(teacherId).collect(Collectors.toSet());
		//1.old to new
		for (CourseSchedule cs : leftSchedules) {
			String classId = cs.getClassId();
			List<CourseSchedule> scheduleList = cidScheduleMap.get(classId);
			List<CourseSchedule> leftScheduleList = scheduleList.stream().filter(e->leftPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
			List<CourseSchedule> rightScheduleList = scheduleList.stream().filter(e->rightPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());

			String arrId = clsArrIdMap.get(classId);
			String msg = checkSubjectNoTime(leftPeriod, unitId, arrMap.get(arrId), rightScheduleList);
			if(StringUtils.isNotBlank(msg)) {
				return error(msg);
			}

			msg = checkClassConflicts(arrayIds, arrId, leftScheduleList.toArray(new CourseSchedule[0]),
					rightScheduleList.toArray(new CourseSchedule[0]), leftPeriod, rightPeriod, extraTids, cids, null, null);
			if(StringUtils.isNotBlank(msg))
				return error(msg);
			// save
			makeTtoSaveResult(unitId, rightTimes, saveList, leftScheduleList);
			makeTtoSaveResult(unitId, leftTimes, saveList, rightScheduleList);
		}
		
		//2. new to old
		if(rightSchedules!=null) {
			for (CourseSchedule cs : rightSchedules) {
				String classId = cs.getClassId();
				List<CourseSchedule> scheduleList = cidScheduleMap.get(classId);
				List<CourseSchedule>  rightScheduleList = scheduleList.stream().filter(e->leftPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
				List<CourseSchedule> leftScheduleList = scheduleList.stream().filter(e->rightPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());

				String arrId = clsArrIdMap.get(classId);
				String msg = checkSubjectNoTime(rightPeriod, unitId, arrMap.get(arrId), rightScheduleList);
				if(StringUtils.isNotBlank(msg)) {
					return error(msg);
				}

				msg = checkClassConflicts(arrayIds, arrId, leftScheduleList.toArray(new CourseSchedule[0]),
						rightScheduleList.toArray(new CourseSchedule[0]), rightPeriod, leftPeriod, extraTids, cids, null, null);
				if(StringUtils.isNotBlank(msg))
					return error(msg);
				
				makeTtoSaveResult(unitId, leftTimes, saveList, leftScheduleList);
				makeTtoSaveResult(unitId, rightTimes, saveList, rightScheduleList);
			}
		}
		
		//3. 保存操作
		try {
			newGkTimetableOtherService.saveAll(saveList.toArray(new NewGkTimetableOther[0]));
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage());
		}
		
		return success("操作成功");
	}

	@RequestMapping("/swapPlaceCourse")
	@ResponseBody
	public String swapPlaceCourse(@PathVariable String arrayId, PeriodSwapDto dto, String timestr) {
		CourseSchedule[] leftSchedules = dto.getLeftSchedules();
		CourseSchedule[] rightSchedules = dto.getRightSchedules();
		
		String teacherId = leftSchedules[0].getTeacherId();
		String leftPeriod = dto.getLeftPeriod();
		String rightPeriod = dto.getRightPeriod();
		
		String[] leftTimes = leftPeriod.split("_");
		String[] rightTimes = rightPeriod.split("_");
		
		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();
		NewGkArray array = newGkArrayService.findOne(arrayId);
		String lessonArrangeId = array.getLessonArrangeId();
		
		if(Arrays.stream(leftSchedules).anyMatch(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(""+e.getClassType()))
				|| (rightSchedules !=null && Arrays.stream(rightSchedules).anyMatch(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(""+e.getClassType())))) {
			return error("教学班不允许 调课");
		}
		
		Set<String> cids = Arrays.asList(leftSchedules).stream().map(e->e.getClassId()).collect(Collectors.toSet());
		if(rightSchedules!=null) {
			Set<String> cids2 = Arrays.asList(rightSchedules).stream().map(e->e.getClassId()).collect(Collectors.toSet());
			cids.addAll(cids2);
		}
		
		List<NewGkTimetableOther> saveList = new ArrayList<>();
		if(cids.size()==1) {
			List<Set<String>> combineGroups = combineRelationService.getCombineRelation(unitId,lessonArrangeId);
			String classId = leftSchedules[0].getClassId();
			String oldDivideClassId = newGkDivideClassService.findOne(classId).getOldDivideClassId();
			Arrays.stream(leftSchedules).forEach(e->e.setOldDivideClassId(oldDivideClassId));
			if(rightSchedules != null)
				Arrays.stream(rightSchedules).forEach(e->e.setOldDivideClassId(oldDivideClassId));
			String msg = checkClassConflicts(null, arrayId, leftSchedules, rightSchedules, leftPeriod, rightPeriod,null, null, null, combineGroups);
			if(StringUtils.isNotBlank(msg)) {
				return error(msg);
			}
			
			List<NewGkTimetableOther> ttoList = makeSwapTtoTime(leftSchedules, rightSchedules, leftTimes, rightTimes);
			saveList.addAll(ttoList);
		}else if(cids.size() >1) {  // 超过1个班
			/* 最多一共比较4次 冲突 */
			List<CourseSchedule> allScheduleList = newGkTimetableService.findScheduleByClass(unitId, arrayId, cids.toArray(new String[0]));
			Map<String, List<CourseSchedule>> cidScheduleMap = EntityUtils.getListMap(allScheduleList, CourseSchedule::getClassId, Function.identity());
			//1.old to new 
			for (CourseSchedule cs : leftSchedules) {
				String classId = cs.getClassId();
				List<CourseSchedule> scheduleList = cidScheduleMap.get(classId);
				List<CourseSchedule> leftScheduleList = scheduleList.stream().filter(e->leftPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
				List<CourseSchedule> rightScheduleList = scheduleList.stream().filter(e->rightPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
				
				String msg = checkSubjectNoTime(leftPeriod, unitId, lessonArrangeId, rightScheduleList);
				if(StringUtils.isNotBlank(msg))
					return error(msg);
				
				msg = checkClassConflicts(null, arrayId, leftScheduleList.toArray(new CourseSchedule[0]),
						rightScheduleList.toArray(new CourseSchedule[0]), leftPeriod, rightPeriod, null, cids, null, null);
				if(StringUtils.isNotBlank(msg))
					return error(msg);
				// save
				makeTtoSaveResult(unitId, rightTimes, saveList, leftScheduleList);
				makeTtoSaveResult(unitId, leftTimes, saveList, rightScheduleList);
			}
			
			//2. new to old
			if(rightSchedules!=null) {
				for (CourseSchedule cs : rightSchedules) {
					String classId = cs.getClassId();
					List<CourseSchedule> scheduleList = cidScheduleMap.get(classId);
					List<CourseSchedule>  rightScheduleList = scheduleList.stream().filter(e->leftPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
					List<CourseSchedule> leftScheduleList = scheduleList.stream().filter(e->rightPeriod.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
					
					String msg = checkSubjectNoTime(rightPeriod, unitId, lessonArrangeId, rightScheduleList);
					if(StringUtils.isNotBlank(msg))
						return error(msg);
					
					msg = checkClassConflicts(null, arrayId, leftScheduleList.toArray(new CourseSchedule[0]),
							rightScheduleList.toArray(new CourseSchedule[0]), rightPeriod, leftPeriod, null, cids, null, null);
					if(StringUtils.isNotBlank(msg))
						return error(msg);
					
					makeTtoSaveResult(unitId, leftTimes, saveList, leftScheduleList);
					makeTtoSaveResult(unitId, rightTimes, saveList, rightScheduleList);
				}
			}
			
		}else {
			// 不可能
		}
		
		
		//3. 保存操作
		try {
			newGkTimetableOtherService.saveAll(saveList.toArray(new NewGkTimetableOther[0]));
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage());
		}
		
		return success("操作成功");
	}

	private List<NewGkTimetableOther> makeSwapTtoTime(CourseSchedule[] leftSchedules, CourseSchedule[] rightSchedules,
			String[] leftTimes, String[] rightTimes) {
		Set<String> allTtoIds = new HashSet<>();
		Set<String> ttoIds = Arrays.asList(leftSchedules).stream().map(e->e.getId()).collect(Collectors.toSet());
		Set<String> ttoIds2 = new HashSet<>();
		if(rightSchedules!=null) {
			ttoIds2 = Arrays.asList(rightSchedules).stream().map(e->e.getId()).collect(Collectors.toSet());
		}
		allTtoIds.addAll(ttoIds);
		allTtoIds.addAll(ttoIds2);
		List<NewGkTimetableOther> ttoList = newGkTimetableOtherService.findByIds(allTtoIds.toArray(new String[0]));
		for (NewGkTimetableOther tto : ttoList) {
			if(ttoIds.contains(tto.getId())) {
				tto.setDayOfWeek(Integer.parseInt(rightTimes[0]));
				tto.setPeriodInterval(rightTimes[1]);
				tto.setPeriod(Integer.parseInt(rightTimes[2]));
			}else if(ttoIds2.contains(tto.getId())) {
				tto.setDayOfWeek(Integer.parseInt(leftTimes[0]));
				tto.setPeriodInterval(leftTimes[1]);
				tto.setPeriod(Integer.parseInt(leftTimes[2]));
			}
		}
		return ttoList;
	}
	
	private String checkSubjectNoTime(String destPeriod, String unitId, String lessonArrangeId,
			List<CourseSchedule> originSchedules) {
		if(CollectionUtils.isNotEmpty(originSchedules)) {
			List<String[]> collect = originSchedules.stream().map(e->new String[] {e.getOldDivideClassId(),e.getSubjectId(),e.getSubjectType()}).collect(Collectors.toList());
			List<NewGkClassFeatureDto> subjectTimeDtoList = newGkClassSubjectTimeService.findSubjectInfo(unitId, lessonArrangeId, collect);
			Map<String, String> noTimeMap = EntityUtils.getMap(subjectTimeDtoList, e->e.getClassId()+"-"+e.getSubjectId()+"-"+e.getSubjectType(),
					e->e.getNoArrangeTime()==null?"":e.getNoArrangeTime());
			
			for (CourseSchedule e : originSchedules) {
				String noTime = noTimeMap.get(e.getOldDivideClassId()+"-"+e.getSubjectId()+"-"+e.getSubjectType());
				if(noTime.contains(destPeriod)) {
					if(StringUtils.isBlank(e.getClassName())) {
						NewGkDivideClass one = newGkDivideClassService.findOne(e.getClassId());
						if(one != null) e.setClassName(one.getClassName());
					}
					if(StringUtils.isBlank(e.getSubjectName())) {
						Course one = courseRemoteService.findOneObjectById(e.getSubjectId());
						if(one != null) e.setSubjectName(one.getSubjectName());
					}
					return e.getClassName()+" "+e.getSubjectName()+" 在目标时间点禁排";
				}
			}
		}
		return null;
	}

	private void makeTtoSaveResult(String unitId, String[] rightTimes, List<NewGkTimetableOther> saveList,
			List<CourseSchedule> leftScheduleList) {
		for (CourseSchedule cs2 : leftScheduleList) {
			NewGkTimetableOther tto = new NewGkTimetableOther();
			tto.setId(cs2.getId());
			tto.setTimetableId(cs2.getRecordId());
			tto.setPlaceId(cs2.getPlaceId());
			tto.setDayOfWeek(Integer.parseInt(rightTimes[0]));
			tto.setPeriodInterval(rightTimes[1]);
			tto.setPeriod(Integer.parseInt(rightTimes[2]));
			tto.setFirstsdWeek(cs2.getWeekType());
			tto.setUnitId(unitId);
			saveList.add(tto);
		}
	}
	/**
	 * 
	 *
	 * @param arrayIds
	 * @param arrayId
	 * @param schedules
	 * @param destPeriod
	 * @param originPeriod
	 * @param teacherWeekType
	 * @param extraTeaIds
	 * @param combineGroups 合班班级 组合；ele:classId-subjectId-subjectType 一个set是一组
	 * @return
	 */
	private String checkTeacherConflict(String[] arrayIds, String arrayId, CourseSchedule[] schedules, String destPeriod,
										String originPeriod, Map<String, Integer> teacherWeekType, Set<String> extraTeaIds, List<Set<String>> combineGroups) {
		if(schedules == null || destPeriod == null) {
			// 目标时间点 不存在 ，是减少一节课程 无需比较冲突
			return null;
		}
		
		Set<String> tids = new HashSet<>();
		Map<String,List<CourseSchedule>> tSMap = new HashMap<>();
		for (CourseSchedule cs : schedules) {
			String teacherId = cs.getTeacherId();
			if(CollectionUtils.isNotEmpty(extraTeaIds) && extraTeaIds.contains(teacherId)) {
				continue;  // 不比较这位教师的冲突
			}
			if(StringUtils.isNotEmpty(teacherId)) {
				tids.add(teacherId);
				List<CourseSchedule> list = tSMap.get(teacherId);
				if(list == null) {
					list = new ArrayList<>();
					tSMap.put(teacherId, list);
				}
				list.add(cs);
			}
		}
		
		if(tids.size()<1) 
			return null;
		
		
		List<CourseSchedule> leftList = null;
		List<CourseSchedule> scheduleList = new ArrayList<>();
		if(ArrayUtils.isNotEmpty(arrayIds)){
			for (String arrId : arrayIds) {
				List<CourseSchedule> scheduleTemps = newGkTimetableService.makeScheduleByTeacher(arrId, tids);
				scheduleList.addAll(scheduleTemps);
			}
		}else{
			scheduleList = newGkTimetableService.makeScheduleByTeacher(arrayId, tids);
		}
		Map<String, List<CourseSchedule>> teacherScheduleMap = EntityUtils.getListMap(scheduleList, CourseSchedule::getTeacherId,Function.identity());
		for (String teacherId : teacherScheduleMap.keySet()) {
			List<CourseSchedule> css = teacherScheduleMap.get(teacherId);
			Map<String, List<CourseSchedule>> map = EntityUtils.getListMap(css,
					(CourseSchedule e) -> e.getDayOfWeek() + "_" + e.getPeriodInterval() + "_" + e.getPeriod(),
					Function.identity());
			leftList = Optional.ofNullable(map.get(originPeriod)).orElse(new ArrayList<>());
			List<CourseSchedule> list = map.get(destPeriod);
			if(CollectionUtils.isNotEmpty(list)) {
				int weekType = NewGkElectiveConstant.WEEK_TYPE_NORMAL;
				if(leftList.size()>0)
					weekType = leftList.get(0).getWeekType();
				int weekType2 = list.get(0).getWeekType();
				
				// 合班情况下的判断
				if(CollectionUtils.isNotEmpty(combineGroups) && CollectionUtils.isEmpty(extraTeaIds) && list.size()>0) {
					// 存在合班 而且 是班级调课
					String key =null;
					List<CourseSchedule> list2 = tSMap.get(teacherId);
					CourseSchedule lcs = list2.get(0);
					if(list2.size()==1) {
						key = lcs.getOldDivideClassId()+"-"+lcs.getSubjectId()+"-"+lcs.getSubjectType();
					}
					
					if(StringUtils.isNotBlank(key)) {
						String key2 = key;
						Optional<Set<String>> findGroup = combineGroups.stream().filter(g->g.contains(key2)).findFirst();
						
						if(findGroup.isPresent()) {
							Set<String> group = findGroup.get();
							Predicate<CourseSchedule> predicate = e -> group.contains(
									e.getOldDivideClassId() + "-" + e.getSubjectId() + "-" + e.getSubjectType());
							Stream<CourseSchedule> stm = list.stream();
							if(lcs.getWeekType() != NewGkElectiveConstant.WEEK_TYPE_NORMAL) {
								stm = list.stream().filter(e->e.getWeekType()==lcs.getWeekType());
							}
							if(stm.allMatch(predicate)) {
								continue;
							}
						}else if(lcs.getWeekType() != NewGkElectiveConstant.WEEK_TYPE_NORMAL){
							long count = list.stream().filter(e->e.getWeekType()==lcs.getWeekType()).count();
							if(count<=0) 
								continue;
						}
					}
				}
				
				
				if(list.size()>1) {
					Teacher t = teacherRemoteService.findOneObjectById(teacherId);
					return "教师 "+t.getTeacherName()+" 存在冲突";
				}else if(leftList.size()==1&&list.size() == 1 && (weekType+weekType2)==NewGkElectiveConstant.WEEK_TYPE_NORMAL) {
					continue;  //交换 或者 移动 课程 ;正好是单双周
				}else if(leftList.size()==0&&list.size() == 1 && schedules.length==2 && weekType2<NewGkElectiveConstant.WEEK_TYPE_NORMAL && teacherWeekType != null){
					// 添加课程
					Optional<CourseSchedule> optional = Stream.of(schedules).filter(e -> teacherId.equals(e.getTeacherId())).findFirst();
					if(optional.isPresent()){
						CourseSchedule cs = optional.get();
						if(Objects.equals(cs.getWeekType(),weekType2)){
							Teacher t = teacherRemoteService.findOneObjectById(teacherId);
							return "教师 "+t.getTeacherName()+"  存在冲突";
						}
					}
					teacherWeekType.put(teacherId, NewGkElectiveConstant.WEEK_TYPE_NORMAL-weekType2);
				}else {
					Teacher t = teacherRemoteService.findOneObjectById(teacherId);
					return "教师 "+t.getTeacherName()+"  存在冲突";
				}
			}
		}
		
		if(teacherWeekType!=null&&teacherWeekType.size()>1 && teacherWeekType.values().stream().collect(Collectors.summingInt(e->e))!=NewGkElectiveConstant.WEEK_TYPE_NORMAL) {
			return "教师 无法 同时在目标时间点上课";
		}
		return null;
	}

	/**
	 * 交换一个班级 某时间两节单双周课程的 单双周类型
	 * @param arrayId
	 * @param dto
	 * @param period
	 * @return
	 */
	@RequestMapping("/swapWeekType")
	@ResponseBody
	public String swapWeekType(@PathVariable String arrayId,  @RequestParam(name="arrayIds") String[] arrayIds,
							   PeriodSwapDto dto, String period, String type) {
		if(StringUtils.isBlank(period)) {
			return error("未发现时间参数 ，请联系管理员！");
		}
		
		CourseSchedule leftSchedule = dto.getLeftSchedules()[0];
		CourseSchedule rightSchedule = dto.getLeftSchedules()[1];
		
		Map<String,Integer> resMap = new HashMap<>();
		String unitId = getLoginInfo().getUnitId();
		for (String arrId : arrayIds) {
			if("C".equals(type) || "P".equals(type)) {
				String classId = leftSchedule.getClassId();
				String oldDivideClassId = newGkDivideClassService.findOne(classId).getOldDivideClassId();
				leftSchedule.setOldDivideClassId(oldDivideClassId);
				if(rightSchedule != null) rightSchedule.setOldDivideClassId(oldDivideClassId);
				String msg = checkWeekTypeConflicts(unitId, arrId, period, leftSchedule, rightSchedule, null);
				if(StringUtils.isNotBlank(msg))
					return error(msg);
				resMap.put(leftSchedule.getId(), rightSchedule.getWeekType());
				resMap.put(rightSchedule.getId(), leftSchedule.getWeekType());
			}else if("T".equals(type)) {
				List<CourseSchedule> scheduleList = newGkTimetableService.findScheduleByClass(unitId, arrayId, new String[] {leftSchedule.getClassId(),rightSchedule.getClassId()});

				Function<CourseSchedule,String> fun = e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod();
				List<CourseSchedule> leftClassSchedules = scheduleList.stream()
						.filter(e -> e.getClassId().equals(leftSchedule.getClassId()) && period.equals(fun.apply(e)))
						.collect(Collectors.toList());
				List<CourseSchedule> rightClassSchedules = scheduleList.stream()
						.filter(e -> e.getClassId().equals(rightSchedule.getClassId()) && period.equals(fun.apply(e)))
						.collect(Collectors.toList());
				String teacherId = leftSchedule.getTeacherId();
				String msg = checkWeekTypeConflicts(unitId, arrId, period, leftClassSchedules.get(0), leftClassSchedules.size()>1?leftClassSchedules.get(1):null, teacherId);
				if(StringUtils.isNotBlank(msg))
					return error(msg);
				msg = checkWeekTypeConflicts(unitId, arrayId, arrId, rightClassSchedules.get(0), rightClassSchedules.size()>1?rightClassSchedules.get(1):null, teacherId);
				if(StringUtils.isNotBlank(msg))
					return error(msg);

				List<CourseSchedule> list = new ArrayList<>();
				list.addAll(leftClassSchedules);
				list.addAll(rightClassSchedules);

				if(list.stream().anyMatch(e->e.getWeekType() == NewGkElectiveConstant.WEEK_TYPE_NORMAL)) {
					return error("发生未知错误，不应出现非单双周课程");
				}
				list.forEach(e->{
					resMap.put(e.getId(), NewGkElectiveConstant.WEEK_TYPE_NORMAL-e.getWeekType());
				});
			}
		}

		
		
		// 保存结果
		try {
			newGkTimetableOtherService.update(resMap, "id", "firstsdWeek");
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		
		return success("成功");
	}

	private String checkWeekTypeConflicts(String unitId, String arrayId, String period, CourseSchedule leftSchedule,
			CourseSchedule rightSchedule, String teacherId) {
		String tid1 = leftSchedule.getTeacherId();
		String tid2 = null;
		if(rightSchedule != null)
			tid2 = rightSchedule.getTeacherId();
		
		//TODO 教师交换单双周
		
		Set<String> teacherIdSet = new HashSet<>();
		if(StringUtils.isNotEmpty(tid1)) {
			teacherIdSet.add(tid1);
		}
		if(StringUtils.isNotEmpty(tid2)) {
			teacherIdSet.add(tid2);
		}
		if(teacherIdSet.size() > 0) {
			String lessonArrangeId = newGkArrayService.findOne(arrayId).getLessonArrangeId();
			List<Set<String>> combineGroups = combineRelationService.getCombineRelation(unitId,lessonArrangeId);
			Map<String, String> teacherNameMap  = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIdSet.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
			List<CourseSchedule> scheduleByTeacher = newGkTimetableService.makeScheduleByTeacher(arrayId, teacherIdSet);
			Map<String, List<CourseSchedule>> tScheduleMap = EntityUtils.getListMap(scheduleByTeacher, CourseSchedule::getTeacherId, e->e);
			for (String tid : tScheduleMap.keySet()) {
				List<CourseSchedule> schedules1 = tScheduleMap.get(tid);
				
				List<CourseSchedule> collect = schedules1.stream().filter(tto->Objects.equals(tto.getWeekType(), rightSchedule.getWeekType())
						&&period.equals(tto.getDayOfWeek()+"_"+tto.getPeriodInterval()+"_"+tto.getPeriod())).collect(Collectors.toList());
				if(tid.equals(tid2))
					collect = schedules1.stream().filter(tto->Objects.equals(tto.getWeekType(), leftSchedule.getWeekType())
							&&period.equals(tto.getDayOfWeek()+"_"+tto.getPeriodInterval()+"_"+tto.getPeriod())).collect(Collectors.toList());
					
				if(collect.size()>0) {
					if(CollectionUtils.isNotEmpty(combineGroups)) {
						String key = leftSchedule.getOldDivideClassId()+"-"+leftSchedule.getSubjectId()+"-"+leftSchedule.getSubjectType();
						if(tid.equals(tid2))
							key = rightSchedule.getOldDivideClassId()+"-"+rightSchedule.getSubjectId()+"-"+rightSchedule.getSubjectType();
						String key2 = key;
						Optional<Set<String>> findGroup = combineGroups.stream().filter(g->g.contains(key2)).findFirst();
						if(findGroup.isPresent()) {
							Set<String> group = findGroup.get();
							Predicate<CourseSchedule> predicate = e -> group.contains(
									e.getOldDivideClassId() + "-" + e.getSubjectId() + "-" + e.getSubjectType());
							
							if(collect.stream().allMatch(predicate)) {
								continue;
							}
						}
					}
					
					return "教师 "+teacherNameMap.get(tid)+" 存在冲突";
				}
			}
		}
		
		// 一个使用教室 另一个不适用教室 时 检测教室冲突
		String placeId1 = leftSchedule.getPlaceId();
		String placeId2 = null;
		if(rightSchedule !=null)
			placeId2 = rightSchedule.getPlaceId();
			
		String destPlaceId = null;
		if(StringUtils.isNotBlank(placeId1) && StringUtils.isBlank(placeId2)) {
			destPlaceId = placeId1;
		}else if(StringUtils.isNotBlank(placeId2) && StringUtils.isBlank(placeId1)) {
			destPlaceId = placeId2;
		}
		if(StringUtils.isNotBlank(destPlaceId)) {
			List<NewGkTimetableOther> ttos = newGkTimetableOtherService.findByArrayIdAndPlaceId(unitId,arrayId,destPlaceId);
			Map<String, List<NewGkTimetableOther>> listMap = EntityUtils.getListMap(ttos, e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod(), Function.identity());
			List<NewGkTimetableOther> list = listMap.get(period);
//			int weekType = rightSchedule.getWeekType();
			if(list != null) {
				if(list.size()>1) {
					return "存在教室冲突";
				}
				/*else if(list.size()==1 && list.get(0).getFirstsdWeek() == weekType) {
					return error("存在教室冲突");
				}*/
			}
		}
		
		return null;
	}
	
	@RequestMapping("/delOrAddLecture")
	@ResponseBody
	public String delOrAddLecture(@PathVariable String arrayId, @RequestParam(name="arrayIds") String[] arrayIds,
								  PeriodSwapDto dto, String type, String period) {
		CourseSchedule[] outterSchedules = dto.getLeftSchedules();  // 主课表
		CourseSchedule[] innerSchedules = dto.getRightSchedules();  // 底部待排列表
//		String lessonArrangeId = array.getLessonArrangeId();
		String unitId = getLoginInfo().getUnitId();

		List<NewGkArray> arrayList = newGkArrayService.findListByIds(arrayIds);

		String innerClassId = null;
		if(innerSchedules != null && StringUtils.isNotBlank(innerSchedules[0].getClassId())) {
			innerClassId = innerSchedules[0].getClassId();
		}
		
		Set<String> extraCids = null;
		Set<String> extraTids = null;
		CourseSchedule[] outterSchedules2 = null;
		// 先比较教师冲突
		Map<String,Integer> teacherWeekType = new HashMap<>();
		if(BaseConstants.SCHEDULE_CONTENT_TYPE_C.equals(type)) {
			List<Set<String>> combineGroups = null;
			if(innerSchedules != null && innerSchedules.length>0) {
				for (NewGkArray newGkArray : arrayList) {
					String arrId = newGkArray.getId();
					String lessonArrangeIdTemp = newGkArray.getLessonArrangeId();
					combineGroups = combineRelationService.getCombineRelation(unitId,lessonArrangeIdTemp);
					String classId = innerSchedules[0].getClassId();
					String oldDivideClassId = newGkDivideClassService.findOne(classId).getOldDivideClassId();
					Arrays.stream(innerSchedules).forEach(e->e.setOldDivideClassId(oldDivideClassId));
					if(outterSchedules != null)
						Arrays.stream(outterSchedules).forEach(e->e.setOldDivideClassId(oldDivideClassId));
					// 获取 科目 班级 教师 禁排 信息
					if(Objects.equals("1", dto.getCourseLimit())) {
						String msg = checkSubjectNoTime(period, unitId, lessonArrangeIdTemp, Arrays.asList(innerSchedules));
						if(StringUtils.isNotBlank(msg)) {
							return error(msg);
						}
					}
					if(Objects.equals("1", dto.getTeacherLimit())) {
						String msg = checkTeaacherNoTime(period,unitId,lessonArrangeIdTemp,Arrays.asList(innerSchedules));
						if(StringUtils.isNotBlank(msg)) {
							return error(msg);
						}
					}
					//TODO 添加学生冲突 和 场地 检查
					CourseSchedule[] leftSchedules = innerSchedules;
					CourseSchedule[] rightSchedules = outterSchedules;

					String msg = checkClassConflicts(null, arrId, leftSchedules, rightSchedules, null, period,null,
							null,teacherWeekType, combineGroups);
					if(StringUtils.isNotBlank(msg))
						return error(msg);
				}
			}
			
//			String msg = checkTeacherConflict(arrayId, innerSchedules, period, null, teacherWeekType,null,combineGroups);
//			if(StringUtils.isNotBlank(msg))
//				return error(msg);
			outterSchedules2 = outterSchedules;
		}else if(BaseConstants.SCHEDULE_CONTENT_TYPE_T.equals(type)) {
			if(innerSchedules != null && innerSchedules.length>0) {
				for (NewGkArray newGkArray : arrayList) {
					String arrId = newGkArray.getId();
					String[] split = period.split("_");
					NewGkTimetableOther tto = new NewGkTimetableOther();
					tto.setDayOfWeek(Integer.parseInt(split[0]));
					tto.setPeriodInterval(split[1]);
					tto.setPeriod(Integer.parseInt(split[2]));
					CourseSchedule[] leftSchedules = innerSchedules;
					if(outterSchedules != null) {
						extraCids = Arrays.stream(outterSchedules).map(e->e.getClassId()).collect(Collectors.toSet());
					}
					String msg = checkStuConflict(arrId, unitId, leftSchedules , extraCids, tto);
					if(StringUtils.isNotBlank(msg)) {
						return error("存在学生冲突");
					}
					String placeId = innerSchedules[0].getPlaceId();
					msg = checkPlaceConflict(unitId, null, arrId, period, placeId);
					if(StringUtils.isNotBlank(msg))
						return msg;
				}
			}
		}else if(BaseConstants.SCHEDULE_CONTENT_TYPE_P.equals(type)){
			List<Set<String>> combineGroups = null;
			if(innerSchedules != null && innerSchedules.length>0) {
				for (NewGkArray newGkArray : arrayList) {
					String arrId = newGkArray.getId();
					String lessonArrangeIdTemp = newGkArray.getLessonArrangeId();
					combineGroups = combineRelationService.getCombineRelation(unitId,lessonArrangeIdTemp);
					String classId = innerSchedules[0].getClassId();
					String oldDivideClassId = newGkDivideClassService.findOne(classId).getOldDivideClassId();
					Arrays.stream(innerSchedules).forEach(e->e.setOldDivideClassId(oldDivideClassId));
					if(outterSchedules != null) {
						String classId2 = outterSchedules[0].getClassId();
						if(classId2.equals(classId)) {
							Arrays.stream(outterSchedules).forEach(e->e.setOldDivideClassId(oldDivideClassId));
						}else {
							String oldDivideClassId2 = newGkDivideClassService.findOne(classId2).getOldDivideClassId();
							Arrays.stream(outterSchedules).forEach(e->e.setOldDivideClassId(oldDivideClassId2));
						}
					}
					// 获取 科目 班级 教师 禁排 信息
					if(Objects.equals("1", dto.getCourseLimit())) {
						String msg = checkSubjectNoTime(period, unitId, lessonArrangeIdTemp, Arrays.asList(innerSchedules));
						if(StringUtils.isNotBlank(msg)) {
							return error(msg);
						}
					}
					if(Objects.equals("1", dto.getTeacherLimit())) {
						String msg = checkTeaacherNoTime(period,unitId,lessonArrangeIdTemp,Arrays.asList(innerSchedules));
						if(StringUtils.isNotBlank(msg)) {
							return error(msg);
						}
					}
					// 学生冲突
					String[] split = period.split("_");
					NewGkTimetableOther tto = new NewGkTimetableOther();
					tto.setDayOfWeek(Integer.parseInt(split[0]));
					tto.setPeriodInterval(split[1]);
					tto.setPeriod(Integer.parseInt(split[2]));
					CourseSchedule[] leftSchedules = innerSchedules;
					if(outterSchedules != null) {
						extraCids = Arrays.stream(outterSchedules).map(e->e.getClassId()).collect(Collectors.toSet());
					}
					String msg = checkStuConflict(arrId, unitId, leftSchedules, extraCids, tto);
					if(StringUtils.isNotBlank(msg)) {
						return error("存在学生冲突");
					}
					//TODO
					if(outterSchedules != null) {
						extraTids = Arrays.stream(outterSchedules).map(e->e.getTeacherId()).collect(Collectors.toSet());
					}
					msg = checkTeacherConflict(null, arrId, innerSchedules, period, null, teacherWeekType,extraTids,combineGroups);
					if(StringUtils.isNotBlank(msg))
						return error(msg);

				}
				outterSchedules2 = outterSchedules;
			}
		}
		
		if(innerSchedules!= null && innerSchedules.length == 2) {
			//初始化 单双周
			String msg = initWeekType(innerSchedules, teacherWeekType);
			if(StringUtils.isNotBlank(msg)) {
				return error(msg);
			}
		}
		
		String[] split = period.split("_");
		List<NewGkTimetableTeacher> tttList = new ArrayList<>();
		List<NewGkTimetableOther> ttoList = new ArrayList<>();
		List<NewGkTimetable> ttList = new ArrayList<>();
		List<String> delttIds = new ArrayList<>();
		if(innerSchedules != null) {
			NewGkTimetableOther tto = new NewGkTimetableOther();
			// 保存结果
			NewGkDivideClass innerClass = newGkDivideClassService.findOne(innerClassId);
			String realArrId = innerClass.getDivideId();
			List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayIdAndClassId(unitId, realArrId, innerClassId);
			Set<String> ttids = EntityUtils.getSet(timetableList, NewGkTimetable::getId);
			List<NewGkTimetableTeacher> timeteacherList = newGkTimetableTeacherService.findByTimetableIds(ttids.toArray(new String[0]));
			Map<String, NewGkTimetable> ttMap = EntityUtils.getMap(timetableList, e->e.getClassId()+"-"+e.getSubjectId());
			Map<String, NewGkTimetableTeacher> ttTMap = EntityUtils.getMap(timeteacherList, e->e.getTimetableId());

			NewGkTimetableTeacher ttt = null;
			tto = null;
			NewGkTimetable tt = null;
			Date now = new Date();
			for (CourseSchedule sc : innerSchedules) {
				String subjectId = sc.getSubjectId();
				String placeId = sc.getPlaceId();
				String teacherId = sc.getTeacherId();
				String subjectType = sc.getSubjectType();
				
				tt = ttMap.get(innerClassId+"-"+subjectId);
				if(tt == null) {
					tt = new NewGkTimetable();
					tt.setClassId(innerClassId);
					tt.setClassType(innerClass.getClassType());
					tt.setSubjectId(subjectId);
					tt.setSubjectType(subjectType);
					tt.setArrayId(realArrId);
					tt.setCreationTime(now);
					tt.setModifyTime(now);
					tt.setId(UuidUtils.generateUuid());
					tt.setUnitId(unitId);
					ttList.add(tt);
				}
				
				tto = new NewGkTimetableOther();
				tto.setId(UuidUtils.generateUuid());
				tto.setTimetableId(tt.getId());
				tto.setPlaceId(placeId);
				if(innerSchedules.length==1) {
					tto.setFirstsdWeek(NewGkElectiveConstant.WEEK_TYPE_NORMAL);
				}else {
					tto.setFirstsdWeek(sc.getWeekType());
				}
				
				tto.setDayOfWeek(Integer.parseInt(split[0]));
				tto.setPeriodInterval(split[1]);
				tto.setPeriod(Integer.parseInt(split[2]));
				tto.setUnitId(unitId);
				ttoList.add(tto);

				if(ttTMap.get(tt.getId()) != null){
					ttt = ttTMap.get(tt.getId());
					if(StringUtils.isBlank(teacherId)){
						delttIds.add(ttt.getTimetableId());
						continue;
					}
					ttt.setTeacherId(teacherId);
					tttList.add(ttt);
				}else if(StringUtils.isNotBlank(teacherId)){
					ttt = new NewGkTimetableTeacher();
					ttt.setId(UuidUtils.generateUuid());
					ttt.setTeacherId(teacherId);
					ttt.setTimetableId(tt.getId());
					tttList.add(ttt);
				}
				
				
			}
			
		}

		newGkTimetableTeacherService.deleteByTimetableIdIn(delttIds.toArray(new String[0]));
		Set<String> delOtherIds = Arrays.stream(outterSchedules==null?new CourseSchedule[0]:outterSchedules).map(e->e.getId()).collect(Collectors.toSet());
		NewGkArrayResultSaveDto saveDto = new NewGkArrayResultSaveDto();
		saveDto.setInsertTimeTableList(ttList);
		saveDto.setInsertOtherList(ttoList);
		saveDto.setUpdateTeacherList(tttList);
		saveDto.setDelTimeTableOtherIds(delOtherIds.toArray(new String[0]));
		
		try {
			newGkTimetableService.saveAll(null, null, saveDto );
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage());
		}
		
		return success("操作成功");
	}

	private String initWeekType(CourseSchedule[] innerSchedules, Map<String, Integer> teacherWeekType) {
		if(teacherWeekType.size()==0) {
			innerSchedules[0].setWeekType(NewGkElectiveConstant.WEEK_TYPE_ODD);
			innerSchedules[1].setWeekType(NewGkElectiveConstant.WEEK_TYPE_EVEN);
		}else if(teacherWeekType.size()==1) {
			if(teacherWeekType.get(innerSchedules[0].getTeacherId()) != null) {
				innerSchedules[0].setWeekType(teacherWeekType.get(innerSchedules[0].getTeacherId()));
				innerSchedules[1].setWeekType(NewGkElectiveConstant.WEEK_TYPE_NORMAL-innerSchedules[0].getWeekType());
			}else if(teacherWeekType.get(innerSchedules[1].getTeacherId()) != null) {
				innerSchedules[1].setWeekType(teacherWeekType.get(innerSchedules[1].getTeacherId()));
				innerSchedules[0].setWeekType(NewGkElectiveConstant.WEEK_TYPE_NORMAL-innerSchedules[1].getWeekType());
			}else {
				return "未知错误-delOrAdd --1";
			}
		}else if(teacherWeekType.size()==2) {
			innerSchedules[0].setWeekType(teacherWeekType.get(innerSchedules[0].getTeacherId()));
			innerSchedules[1].setWeekType(teacherWeekType.get(innerSchedules[1].getTeacherId()));
		}else {
			return "未知错误-delOrAdd --2";
		}
		return null;
	}
	
	private String checkTeaacherNoTime(String period, String unitId, String lessonArrangeId,
			List<CourseSchedule> asList) {
		//
		String[] tids = asList.stream().filter(e->StringUtils.isNotBlank(e.getTeacherId())).map(e->e.getTeacherId()).toArray(e->new String[e]);
		if(tids==null || tids.length==0) return null;
		// 获取所有教师的不排课时间
		List<NewGkLessonTime> teacherNoTimeList = newGkLessonTimeService.findByItemIdObjectId(lessonArrangeId, tids,
				new String[] {NewGkElectiveConstant.LIMIT_TEACHER_2}, true);
		
		Map<String, List<CourseSchedule>> tMap = EntityUtils.getListMap(asList, e->e.getTeacherId(),e->e);
		for (NewGkLessonTime lt : teacherNoTimeList) {
			String teacherId = lt.getObjectId();
			if(CollectionUtils.isNotEmpty(lt.getTimesList())) {
				boolean anyMatch = lt.getTimesList().stream()
						.filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(e.getTimeType())
								|| NewGkElectiveConstant.ARRANGE_TIME_TYPE_04.equals(e.getTimeType()))
						.anyMatch(e->period.equals(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod()));
				if(anyMatch) {
					CourseSchedule sc = tMap.get(teacherId).get(0);
					
					if(StringUtils.isBlank(sc.getClassName())) {
						NewGkDivideClass one = newGkDivideClassService.findOne(sc.getClassId());
						if(one != null) sc.setClassName(one.getClassName());
					}
					if(StringUtils.isBlank(sc.getTeacherName())) {
						Teacher teacher = teacherRemoteService.findOneObjectById(teacherId);
						if(teacher!=null) sc.setTeacherName(teacher.getTeacherName());
					}
					return sc.getClassName()+"老师 "+sc.getTeacherName()+"在目标时间禁排";
				}
			}
		}
		return null;
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
	
	@RequestMapping("/teacherSchedule")
	@ResponseBody
	public String getTeacherSchedule(@PathVariable String arrayId, @RequestParam(name="arrayIds") String[] arrayIds,
					@RequestParam(name="teacherIds[]",required=false) String[] teacherIds) {
		if(teacherIds == null || teacherIds.length <1) {
			return "{}";
		}

		// 获取年级
		Map<String, String> arrayGradeNameMap = getArrGradeMap(arrayIds);

		Set<String> teacherIdSet = Stream.of(teacherIds).collect(Collectors.toSet());
		List<CourseSchedule> scheduleList = new ArrayList<>();
		for (String arrId : arrayIds) {
			List<CourseSchedule> scheduleTemps = newGkTimetableService.makeScheduleByTeacher(arrId, teacherIdSet);
			scheduleTemps.forEach(e->e.setClassName(arrayGradeNameMap.get(arrId)+e.getClassName()));
			scheduleList.addAll(scheduleTemps);
		}
		Map<String, List<CourseSchedule>> teacherScheduleMap = EntityUtils.getListMap(scheduleList, CourseSchedule::getTeacherId,Function.identity());
		
		return SUtils.s(teacherScheduleMap);
	}

	private class ParamEntity {
		private boolean myResult;
		private String arrayId;
		private String type;
		private ModelMap map;
		private String unitId;
		private List<Clazz> classList= new ArrayList<>();
		private Map<String, Map<String, String>> classSubjTeacherMap = new HashMap<>();
		private Map<String, List<String>> classMovePeriodMap = new HashMap<>();
		private Map<String, Set<String>> teacherMoveTimeMap = new HashMap<>();
		private Map<String, Set<String>> placeMoveTimeMap = new HashMap<>();
		private List<String> xzbPeriodList= new ArrayList<>();
		private Map<String, String> teacherNameMap = new HashMap<>();
		private Map<String, String> classPlaceMap= new HashMap<>();;
		private Map<String, String> placeNameMap = new HashMap<>();
		private Set<String> noNeedPlaceCourse= new HashSet<>();
		private List<String> gradeNoClick= new ArrayList<>();;
		private List<Set<String>> combineRelations = new ArrayList<>();
		private List<NewGkDivideClass> allClassList= new ArrayList<>();;
		private Set<String> pure3XzbIds = new HashSet<>();
		private Map<String, String> teacherNoTimeMap  = new HashMap<>();
		private Grade grade;
		private Map<String, Integer> piMap  = new HashMap<>();
		private Map<String, Float> lectureSurplus  = new HashMap<>();

		public ParamEntity(String arrayId, String type, String unitId) {
			this.arrayId = arrayId;
			this.type = type;
			this.unitId = unitId;
		}
		public ParamEntity(String arrayId, String type, ModelMap map, String unitId, List<Clazz> classList,
						   Map<String, Map<String, String>> classSubjTeacherMap, Map<String, List<String>> classMovePeriodMap,
						   Map<String, Set<String>> teacherMoveTimeMap, Map<String, Set<String>> placeMoveTimeMap,
						   List<String> xzbPeriodList) {
			this.arrayId = arrayId;
			this.type = type;
			this.map = map;
			this.unitId = unitId;
			this.classList = classList;
			this.classSubjTeacherMap = classSubjTeacherMap;
			this.classMovePeriodMap = classMovePeriodMap;
			this.teacherMoveTimeMap = teacherMoveTimeMap;
			this.placeMoveTimeMap = placeMoveTimeMap;
			this.xzbPeriodList = xzbPeriodList;
		}

		public List<Set<String>> getCombineRelations() {
			return combineRelations;
		}

		boolean is() {
			return myResult;
		}

		public Map<String, Map<String, String>> getClassSubjTeacherMap() {
			return classSubjTeacherMap;
		}

		public Map<String, Set<String>> getTeacherMoveTimeMap() {
			return teacherMoveTimeMap;
		}

		public Map<String, Set<String>> getPlaceMoveTimeMap() {
			return placeMoveTimeMap;
		}

		public String getType() {
			return type;
		}

		public List<Clazz> getClassList() {
			return classList;
		}

		public Map<String, String> getTeacherNameMap() {
			return teacherNameMap;
		}

		public Map<String, String> getClassPlaceMap() {
			return classPlaceMap;
		}

		public Map<String, String> getPlaceNameMap() {
			return placeNameMap;
		}

		public Set<String> getNoNeedPlaceCourse() {
			return noNeedPlaceCourse;
		}

		public List<String> getGradeNoClick() {
			return gradeNoClick;
		}

		public Map<String, List<String>> getClassMovePeriodMap() {
			return classMovePeriodMap;
		}

		public List<String> getXzbPeriodList() {
			return xzbPeriodList;
		}

		public List<NewGkDivideClass> getAllClassList() {
			return allClassList;
		}

		public Set<String> getPure3XzbIds() {
			return pure3XzbIds;
		}

		public Map<String, String> getTeacherNoTimeMap() {
			return teacherNoTimeMap;
		}

		public Grade getGrade() {
			return grade;
		}

		public Map<String, Integer> getPiMap() {
			return piMap;
		}

		public Map<String, Float> getLectureSurplus() {
			return lectureSurplus;
		}

		public ParamEntity invoke() {
			String gradeId;
			NewGkArray array = newGkArrayService.findOne(arrayId);
			NewGkDivide divide = newGkDivideService.findOne(array.getDivideId());
			gradeId = array.getGradeId();
			List<NewGkDivideClass> xzbClassList = null;
			allClassList = null;
			type = type==null? BaseConstants.SCHEDULE_CONTENT_TYPE_C:type;
			if(type.equals(BaseConstants.SCHEDULE_CONTENT_TYPE_C)) {
				allClassList = newGkDivideClassService.findByDivideIdAndClassType(
						unitId, arrayId, null,
						false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
				allClassList = allClassList.stream().filter(e->!(NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())&& StringUtils.isBlank(e.getBatch()))).collect(Collectors.toList());
				xzbClassList = allClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType())).collect(Collectors.toList());
				classList = xzbClassList.stream().map(c->{
					Clazz clazz = new Clazz();
					clazz.setId(c.getId());
					clazz.setClassName(c.getClassName());
					clazz.setDisplayOrder(c.getOrderId());
					return clazz;
				}).sorted(Comparator.comparingInt((Clazz x) -> Optional.ofNullable(x.getDisplayOrder()).orElse(999)).thenComparing(Clazz::getClassName))
				.collect(Collectors.toList());
				classMovePeriodMap = newGkTimetableService.findClassMovePeriodMap(arrayId, teacherMoveTimeMap, placeMoveTimeMap);

				// 获取合班 和 同时排课的 班级组合
				combineRelations = combineRelationService.getCombineRelation(array.getUnitId(), array.getLessonArrangeId());
//				map.put("combineRelations", combineRelations);
			}else if(type.equals(BaseConstants.SCHEDULE_CONTENT_TYPE_T)) {
				// 获取教师列表
			}

			if(allClassList.size()<1 || allClassList.stream().anyMatch(e->StringUtils.isBlank(e.getOldDivideClassId()))) {
				myResult = true;
				return this;
			}

			Map<String, String> oldClassIdMap = EntityUtils.getMap(allClassList, NewGkDivideClass::getOldDivideClassId,NewGkDivideClass::getId);
			// 获取教师信息
			List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findByArrayItemIds(
					new String[]{array.getLessonArrangeId()}, true);

			Set<String> teacherIds = new HashSet<>();
			for (NewGkTeacherPlan tp : teacherPlanList) {
				String subjectId = tp.getSubjectId();
				List<NewGkTeacherPlanEx> teacherPlanExList = tp.getTeacherPlanExList();
				if(CollectionUtils.isEmpty(teacherPlanExList))
					continue;
				for (NewGkTeacherPlanEx tpe : teacherPlanExList) {
					String teacherId = tpe.getTeacherId();
					String classIds = tpe.getClassIds();
					if(StringUtils.isBlank(classIds))
						continue;
					String[] classIdArr = classIds.split(",");
					for (String cid : classIdArr) {
						if(!oldClassIdMap.containsKey(cid))
							continue;


						Map<String, String> map2 = classSubjTeacherMap.get(oldClassIdMap.get(cid));
						if(map2 == null) {
							map2 = new HashMap<>();
							classSubjTeacherMap.put(oldClassIdMap.get(cid), map2);
						}
						map2.put(subjectId, teacherId);
					}
					teacherIds.add(teacherId);
				}
			}
			List<String> usedTeacherIds = newGkTimetableTeacherService.findTeachersByArrayId(arrayId);
			teacherIds.addAll(usedTeacherIds);
			teacherNameMap = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIds.toArray(new String[teacherIds.size()])),new TypeReference<Map<String, String>>(){});

			// 场地信息
			List<NewGkPlaceItem> placeItemList = newGkPlaceItemService.findByArrayItemId(array.getPlaceArrangeId());
			classPlaceMap = placeItemList.stream().filter(e -> oldClassIdMap.containsKey(e.getObjectId()))
					.collect(Collectors.toMap(e -> oldClassIdMap.get(e.getObjectId()), NewGkPlaceItem::getPlaceId));
			if(NewGkElectiveConstant.ARRANGE_XZB.equals(array.getArrangeType())){
				// 行政班 排课
				Map<String,String> oldIdMap = EntityUtils.getMap(xzbClassList, e -> e.getOldClassId(),e->e.getId());
				List<Clazz> classListT = classRemoteService.findListObjectBy(Clazz.class, null, null,
						"id", oldIdMap.keySet().toArray(new String[0]),
						new String[]{"id", "teachPlaceId"});
				for (Clazz clazz : classListT) {
					if(StringUtils.isNotBlank(clazz.getTeachPlaceId())
					&& oldIdMap.containsKey(clazz.getId())){
						classPlaceMap.put(oldIdMap.get(clazz.getId()),clazz.getTeachPlaceId());
					}
				}
			}
			List<String> usingPlaceIds = newGkTimetableOtherService.findPlaceIds(array.getUnitId(), arrayId);
			Set<String> allPlaceIds = new HashSet<>(classPlaceMap.values());
			allPlaceIds.addAll(usingPlaceIds);
			Map<String, String> placeNameMap2 = SUtils.dt(
					teachPlaceRemoteService.findTeachPlaceMapByAttr(allPlaceIds.toArray(new String[] {}),"placeName"),
					new TR<Map<String, String>>() {
					});
			Map<String, String> treeMap = new TreeMap<>((x, y)->placeNameMap2.get(x).compareTo(placeNameMap2.get(y)));
			treeMap.putAll(placeNameMap2);
			placeNameMap = treeMap;


			// 获取课程特征 是否需要场地信息
			List<NewGkSubjectTime> newGkSubjectTimeList=newGkSubjectTimeService.findByArrayItemId(array.getLessonArrangeId());
			noNeedPlaceCourse = newGkSubjectTimeList.stream()
					.filter(e->Integer.valueOf(NewGkElectiveConstant.IF_INT_0).equals(e.getIsNeed()))
					.map(e->e.getSubjectId()+"-"+e.getSubjectType())
					.collect(Collectors.toSet());
			//年级不排课时间
			List<NewGkLessonTime> newGkLessonTimeList = newGkLessonTimeService.findByItemIdObjectId(array.getLessonArrangeId(), null,
					new String[] { NewGkElectiveConstant.LIMIT_GRADE_0,NewGkElectiveConstant.LIMIT_TEACHER_2,NewGkElectiveConstant.LIMIT_SUBJECT_7 }, true);
			List<NewGkLessonTime> gradeNoTimeList = newGkLessonTimeList.stream()
					.filter(e -> NewGkElectiveConstant.LIMIT_GRADE_0.equals(e.getObjectType()))
					.collect(Collectors.toList());
			gradeNoClick = gradeNoTimeList.stream()
					.filter(e->CollectionUtils.isNotEmpty(e.getTimesList()))
					.flatMap(e->e.getTimesList().stream())
					.filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(e.getTimeType())/*||NewGkElectiveConstant.ARRANGE_TIME_TYPE_02.equals(e.getTimeType())*/)
					.map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())
					.collect(Collectors.toList());
			List<NewGkLessonTime> xzbLessonTimeList = newGkLessonTimeList.stream()
					.filter(e -> NewGkElectiveConstant.LIMIT_SUBJECT_7.equals(e.getObjectType()))
					.collect(Collectors.toList());
			pure3XzbIds = new HashSet<>();
			if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())) {
				// 组合固定
				xzbPeriodList = xzbLessonTimeList.stream()
						.filter(e->CollectionUtils.isNotEmpty(e.getTimesList()))
						.flatMap(e->e.getTimesList().stream())
						.filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_02.equals(e.getTimeType()))
						.map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())
						.collect(Collectors.toList());
				// 三科组合班级 不需要遵循
				Set<String> pure3ZhbIds = allClassList.stream()
						.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())&&e.getSubjectIds()!=null)
						.filter(e->e.getSubjectIds().split(",").length==3).map(e->e.getId())
						.collect(Collectors.toSet());
				pure3XzbIds = allClassList.stream()
						.filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType())&&pure3ZhbIds.contains(e.getRelateId()))
						.map(e->e.getId())
						.collect(Collectors.toSet());

			}

			// 获取所有教师的不排课时间
			List<NewGkLessonTime> teacherNoTimeList = newGkLessonTimeList.stream()
					.filter(e -> NewGkElectiveConstant.LIMIT_TEACHER_2.equals(e.getObjectType()))
					.collect(Collectors.toList());
			teacherNoTimeMap = new HashMap<>();
			for (NewGkLessonTime lt : teacherNoTimeList) {
				String teacherId = lt.getObjectId();
				if(CollectionUtils.isNotEmpty(lt.getTimesList())) {
					String times = lt.getTimesList().stream()
							.filter(e->!NewGkElectiveConstant.ARRANGE_TIME_TYPE_02.equals(e.getTimeType())
									&& !NewGkElectiveConstant.ARRANGE_TIME_TYPE_03.equals(e.getTimeType()))
							.map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())
							.collect(Collectors.joining(","));
					if(StringUtils.isNotBlank(times)) teacherNoTimeMap.put(teacherId, times);
				}
			}

			grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
			piMap = getIntervalMap(grade);
			// 获取每个班级的未排课节次数
			List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayIdAndClassIds(unitId, arrayId, EntityUtils.getSet(xzbClassList, e->e.getId()).toArray(new String[0]));
			newGkTimetableService.makeTime(unitId, arrayId, timetableList);
			List<NewGkClassSubjectTime> classSubjTimeList = newGkClassSubjectTimeService.findByArrayItemIdAndClassIdIn(array.getUnitId(), array.getLessonArrangeId(), null, null, null);

			lectureSurplus = getSurplusLectureCount(xzbClassList, allClassList, newGkSubjectTimeList,
					gradeNoTimeList, piMap, timetableList, classSubjTimeList, array);
			myResult = false;
			return this;
		}

		private Map<String, Float> getSurplusLectureCount(List<NewGkDivideClass> xzbClassList,
															  List<NewGkDivideClass> allClassList, List<NewGkSubjectTime> newGkSubjectTimeList,
															  List<NewGkLessonTime> gradeNoTimeList, Map<String, Integer> piMap, List<NewGkTimetable> timetableList,
															  List<NewGkClassSubjectTime> classSubjTimeList, NewGkArray array) {
			String divideId = array.getId();
			String unitId = array.getUnitId();

			List<NewGkChoRelation> relaList = newGkChoRelationService.findByChoiceIdsAndObjectType(unitId,
					new String[] {divideId}, NewGkElectiveConstant.CHOICE_TYPE_09);
			Map<String, List<String>> childrenClasrelaMap = EntityUtils.getListMap(relaList, NewGkChoRelation::getObjectTypeVal, NewGkChoRelation::getObjectValue);
			Set<String> childs = childrenClasrelaMap.values().stream().flatMap(e->e.stream()).collect(Collectors.toSet());
			boolean hasChai = newGkSubjectTimeList.stream().anyMatch(e -> !NewGkElectiveConstant.SUBJECT_TYPE_O.equals(e.getSubjectType())
					&& childs.contains(e.getSubjectId()));
			Map<String, NewGkSubjectTime> subTimeMap = EntityUtils.getMap(newGkSubjectTimeList, e->e.getSubjectId()+e.getSubjectType());
			Map<String,List<String>> relaSubjMap = new HashMap<>();
			if(hasChai) {
				for (String pid : childrenClasrelaMap.keySet()) {
					List<String> list = childrenClasrelaMap.get(pid);
					relaSubjMap.put(pid+"A",list.stream().map(e->e+"A").collect(Collectors.toList()));
				}
			}

			Map<String,Float> lectureSurplus = new HashMap<>();
			int fixedCount = (int) gradeNoTimeList.stream().filter(e -> CollectionUtils.isNotEmpty(e.getTimesList()))
					.flatMap(e -> e.getTimesList().stream())
					.filter(e -> NewGkElectiveConstant.ARRANGE_TIME_TYPE_02.equals(e.getTimeType())
							&& piMap.containsKey(e.getPeriodInterval()) && piMap.get(e.getPeriodInterval())>0).count();
			Map<String, NewGkDivideClass> cidMap = EntityUtils.getMap(allClassList, e->e.getId(),e->e);
			Map<String, Set<String>> relaJxbList = allClassList.stream()
					.filter(e ->e.getRelateId() != null && NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()))
					.collect(Collectors.groupingBy(NewGkDivideClass::getRelateId,Collectors.mapping(NewGkDivideClass::getSubjectIds, Collectors.toSet())));
			Map<String, List<NewGkTimetable>> classIdTimeMap = EntityUtils.getListMap(timetableList, NewGkTimetable::getClassId, e->e);
			Map<String, NewGkClassSubjectTime> cstMap = EntityUtils.getMap(classSubjTimeList, e->e.getClassId()+"-"+e.getSubjectId()+"-"+e.getSubjectType(),e->e);

			Set<String> types = new HashSet<>();
			types.add(NewGkElectiveConstant.DIVIDE_TYPE_05);
			types.add(NewGkElectiveConstant.DIVIDE_TYPE_07);
			List<String> xzbIds = EntityUtils.getList(xzbClassList, e->e.getId());
			Map<String, List<String[]>> xzbSubjects = newGkDivideClassService.findXzbSubjects(unitId, divideId, array.getLessonArrangeId(), NewGkElectiveConstant.CLASS_SOURCE_TYPE2, xzbIds);
	//		List<NewGkSubjectTime> xzbSubjectTimeList = newGkSubjectTimeList.stream()
	//				.filter(e -> NewGkElectiveConstant.SUBJECT_TYPE_O.equals(e.getSubjectType()))
	//				.collect(Collectors.toList());
			Map<String, NewGkSubjectTime> stMap = EntityUtils.getMap(newGkSubjectTimeList, e->e.getSubjectId()+"_"+e.getSubjectType());
			for (NewGkDivideClass xzb : xzbClassList) {
				String cid = xzb.getOldDivideClassId();

				//TODO 计算应该有的行政班级 课程数量
				/***********************/
				List<String[]> subIdTypes = xzbSubjects.get(xzb.getId());
				List<NewGkSubjectTime> xzbSubjectTimeList = subIdTypes.stream().filter(e->stMap.containsKey(e[0]+"_"+e[1]))
						.map(e->stMap.get(e[0]+"_"+e[1])).collect(Collectors.toList());
				/***********************/
				float all = 0;
				for (NewGkSubjectTime newGkSubjectTime : xzbSubjectTimeList) {
					NewGkClassSubjectTime cst = cstMap.get(cid+"-"+newGkSubjectTime.getSubjectId()+"-"+newGkSubjectTime.getSubjectType());
					if(cst != null) {
						all += cst.getPeriod();
					}else {
						all += newGkSubjectTime.getPeriod();
					}
					if(!Objects.equals(NewGkElectiveConstant.WEEK_TYPE_NORMAL, newGkSubjectTime.getFirstsdWeek()))
						all -= 0.5f;
				}

				// 获取这个班级课表中的课程
				List<NewGkTimetable> list = classIdTimeMap.get(xzb.getId());
				if(list != null) {
					Float used = list.stream().filter(e -> e.getTimeList() != null).flatMap(e -> e.getTimeList().stream())
							.filter(e->piMap.containsKey(e.getPeriodInterval()) && piMap.get(e.getPeriodInterval())>0)
							.map(e -> (e.getFirstsdWeek().intValue() == NewGkElectiveConstant.WEEK_TYPE_NORMAL)?1f:0.5f)
							.reduce((x,y)->x+y).orElse(0f);
					all -= used;
				}else {
					// 这个班级没有排课
				}
				all += fixedCount;
				lectureSurplus.put(xzb.getId(), all);
			}
			return lectureSurplus;
		}
	}
}
