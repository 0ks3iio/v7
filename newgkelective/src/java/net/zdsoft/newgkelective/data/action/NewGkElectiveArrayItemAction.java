package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.CourseFeatureContainer;
import net.zdsoft.newgkelective.data.dto.NewGKCourseFeatureDto;
import net.zdsoft.newgkelective.data.dto.NewGkClassFeatureDto;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassCombineRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeExService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
/**
 *	设置排课条件
 */
@Controller
@RequestMapping("/newgkelective/{arrayItemId}")
public class NewGkElectiveArrayItemAction extends BaseAction{
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkOpenSubjectService newGkOpenSubjectService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private NewGkLessonTimeExService newGkLessonTimeExService;
	@Autowired
	private NewGkDivideClassService divideClassService;
	@Autowired
	private NewGkClassSubjectTimeService newGkClassSubjectTimeService;
	@Autowired
	private NewGkClassCombineRelationService newGkClassCombineRelationService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;

	private void makeRealtions(List<NewGkChoRelation> relaList, NewGkSubjectTime subjectTime, List<String> objectValues,
			String objectType) {
		NewGkChoRelation rela;
		if(CollectionUtils.isEmpty(objectValues)) {
			return;
		}
		LoginInfo lginfo=this.getLoginInfo();
		for (String objectValue : objectValues) {
			rela = new NewGkChoRelation();
			rela.setId(UuidUtils.generateUuid());
			rela.setChoiceId(subjectTime.getId());
			rela.setObjectType(objectType);
			rela.setObjectValue(objectValue);
			rela.setCreationTime(new Date());
			rela.setModifyTime(new Date());
			rela.setUnitId(lginfo.getUnitId());
			relaList.add(rela);
		}
	}
	
	/**
	 * 展示课程特征2
	 * @param divideId
	 * @param gradeId
	 * @param arrayItemId
	 * @param map
	 * @return
	 */
	@RequestMapping("/courseFeatures/index")
	public String courseFeaturesIndex(@PathVariable String arrayItemId, String useMaster, ModelMap map) {
		NewGkArrayItem arrayItem = newGkArrayItemService.findOne(arrayItemId);
		String divideId = arrayItem.getDivideId();
		NewGkDivide divide = newGkDivideService.findById(divideId);
		String openType = divide.getOpenType();
		
		map.put("openType", openType);
		map.put("arrayItemId", arrayItemId);
		map.put("gradeId", divide.getGradeId());
		// 先取得整体时间参数
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);
		
		Map<String, Integer> piMap = getIntervalMap(grade);
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("piMap", piMap);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
		
		map.put("notBasic", "1");
		if (StringUtils.isNotEmpty(grade.getRecess())) {
			String[] ris = grade.getRecess().split(",");
			for(String ri : ris) {
				if(StringUtils.isNotEmpty(ri)) {
					if (ri.startsWith(BaseConstants.PERIOD_INTERVAL_2)) {
						map.put("ab", NumberUtils.toInt(ri.substring(1)));
					} else if(ri.startsWith(BaseConstants.PERIOD_INTERVAL_3)) {
						map.put("pb", NumberUtils.toInt(ri.substring(1)));
					}
				}
			}
		}
		String unitId=getLoginInfo().getUnitId();
		List<NewGKCourseFeatureDto> dtoList = null;
		
		if(Objects.equals(useMaster, "1")) {
			dtoList = newGkSubjectTimeService.findCourseFeaturesWithMaster(arrayItemId, map, grade, unitId);
		}else {
			dtoList = newGkSubjectTimeService.findCourseFeatures(arrayItemId, map, grade, unitId);
		}
		if (CollectionUtils.isNotEmpty(dtoList)) {
			Collections.sort(dtoList, new Comparator<NewGKCourseFeatureDto>() {
				@Override
				public int compare(NewGKCourseFeatureDto o1, NewGKCourseFeatureDto o2) {
					if(o1.getOrder() == null && o2.getOrder() == null) {
						if(o1.getSubjectId().equals(o2.getSubjectId())) {
							if (StringUtils.isNotBlank(o1.getSubjectType())) {
								return o1.getSubjectType().compareTo(o2.getSubjectType());
							} else {
								return 0;
							}
						}else {
							return o1.getSubjectId().compareTo(o2.getSubjectId());
						}
					}
					if (o1.getOrder() == null) {
						return 0;
					}
					if (o2.getOrder() == null) {
						return -1;
					}
					if (o1.getOrder().equals(o2.getOrder())) {
						if(o1.getSubjectId().equals(o2.getSubjectId())) {
							if (StringUtils.isNotBlank(o1.getSubjectType())) {
								return o1.getSubjectType().compareTo(o2.getSubjectType());
							} else {
								return 0;
							}
						}else {
							return o1.getSubjectId().compareTo(o2.getSubjectId());
						}
					}
					return o1.getOrder() - o2.getOrder();
				}

			});
		}

		if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(openType)||NewGkElectiveConstant.DIVIDE_TYPE_12.equals(openType)){
			// 组合固定模式
			map.put("isZugd",true);
		}

		map.put("dtoList", dtoList);
		Map<String, List<String[]>> mcodeMap = findMcode();
		map.put("blpfsList", mcodeMap.get(NewGkElectiveConstant.MCODE_BLPFS));
		map.put("btksfpList", mcodeMap.get(NewGkElectiveConstant.MCODE_BTKSFP));
		map.put("ksfpList", mcodeMap.get(NewGkElectiveConstant.MCODE_KSFP));
		return "/newgkelective/basic/courseFeatures.ftl";

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
//	private void relateSubtimeList(String arrayItemId, Map<String, Integer> subjectNoContinue,
//			List<String[]> subjectNoContinueList) {
//		List<NewGkRelateSubtime> relateSubtimeList = newGkRelateSubtimeService.findListByItemId(arrayItemId);
//		if (CollectionUtils.isNotEmpty(relateSubtimeList)) {
//			for (NewGkRelateSubtime n : relateSubtimeList) {
//				String sIds = n.getSubjectIds();
//				String[] arg = sIds.split(",");
//				String type = n.getType();
//				subjectNoContinueList.add(new String[] { arg[0] + "_" + arg[1], type });
//				if (subjectNoContinue.containsKey(arg[0])) {
//					subjectNoContinue.put(arg[0], subjectNoContinue.get(arg[0]) + 1);
//				} else {
//					subjectNoContinue.put(arg[0], 1);
//				}
//				if (subjectNoContinue.containsKey(arg[1])) {
//					subjectNoContinue.put(arg[1], subjectNoContinue.get(arg[1]) + 1);
//				} else {
//					subjectNoContinue.put(arg[1], 1);
//				}
//			}
//		}
//	}

	private void makeTimeEx(List<NewGkLessonTime> newGkLessonTimeList, Map<String, String> noClickTimeMap,
			Map<String, Map<String, List<String>>> subjectTimeExMap) {
		// 对应时间不能排课时间
		Set<String> ids = EntityUtils.getSet(newGkLessonTimeList, e -> e.getId());
		List<NewGkLessonTimeEx> exList = newGkLessonTimeExService.findByObjectId(ids.toArray(new String[] {}), null);

		if (CollectionUtils.isNotEmpty(exList)) {
			Map<String, List<NewGkLessonTimeEx>> exMap = new HashMap<>();
			for (NewGkLessonTimeEx ex : exList) {
				List<NewGkLessonTimeEx> ll = exMap.get(ex.getScourceTypeId());
				if (CollectionUtils.isEmpty(ll)) {
					ll = new ArrayList<>();
					exMap.put(ex.getScourceTypeId(), ll);
				}
				ll.add(ex);
			}

			for (NewGkLessonTime n : newGkLessonTimeList) {
				if (NewGkElectiveConstant.LIMIT_GRADE_0.contains(n.getObjectType())) {
					if (CollectionUtils.isNotEmpty(exMap.get(n.getId()))) {
						for (NewGkLessonTimeEx ee : exMap.get(n.getId())) {
							String key = ee.getDayOfWeek() + "_" + ee.getPeriodInterval() + "_" + ee.getPeriod();
							noClickTimeMap.put(key, key);
						}
					}
				} else {
					if (CollectionUtils.isNotEmpty(exMap.get(n.getId()))) {
						String key = n.getObjectId() + "-"
								+ (StringUtils.isNotBlank(n.getLevelType()) ? n.getLevelType()
										: NewGkElectiveConstant.SUBJECT_TYPE_O);
						Map<String, List<String>> map2 = subjectTimeExMap.get(key);
						if (MapUtils.isEmpty(map2)) {
							map2 = new HashMap<>();
							subjectTimeExMap.put(key, map2);
						}
						for (NewGkLessonTimeEx ee : exMap.get(n.getId())) {
							List<String> l1 = map2.get(ee.getTimeType());
							if (CollectionUtils.isEmpty(l1)) {
								l1 = new ArrayList<>();
								map2.put(ee.getTimeType(), l1);
							}
							String zz = ee.getDayOfWeek() + "_" + ee.getPeriodInterval() + "_" + ee.getPeriod();
							l1.add(zz);
						}
					}
				}
			}
		}
	}

	public Map<String, List<String[]>> findMcode() {
		Map<String, List<String[]>> returnMap = RedisUtils.getObject("GK_MCODE",
				new TypeReference<Map<String, List<String[]>>>() {
				});
		if (MapUtils.isEmpty(returnMap) || CollectionUtils.isEmpty(returnMap.get(NewGkElectiveConstant.MCODE_BLPFS))
				|| CollectionUtils.isEmpty(returnMap.get(NewGkElectiveConstant.MCODE_BTKSFP))
				|| CollectionUtils.isEmpty(returnMap.get(NewGkElectiveConstant.MCODE_KSFP))) {
			returnMap = findMcodeDetail();
			RedisUtils.setObject("GK_MCODE", returnMap, RedisUtils.TIME_ONE_WEEK);
		}
		return returnMap;
	}

	public Map<String, List<String[]>> findMcodeDetail() {
		Map<String, List<String[]>> returnMap1 = new HashMap<>();
		returnMap1.put(NewGkElectiveConstant.MCODE_BLPFS, new ArrayList<>());
		returnMap1.put(NewGkElectiveConstant.MCODE_BTKSFP, new ArrayList<>());
		returnMap1.put(NewGkElectiveConstant.MCODE_KSFP, new ArrayList<>());
		List<McodeDetail> list = SUtils
				.dt(mcodeRemoteService.findAllByMcodeIds(new String[] { NewGkElectiveConstant.MCODE_BLPFS,
						NewGkElectiveConstant.MCODE_BTKSFP, NewGkElectiveConstant.MCODE_KSFP }), McodeDetail.class);
		if (CollectionUtils.isNotEmpty(list)) {
			for (McodeDetail m : list) {
				returnMap1.get(m.getMcodeId()).add(new String[] { m.getThisId(), m.getMcodeContent() });
			}
		}
		return returnMap1;
	}
	
	/**
	 * 查询所有科目
	 * 
	 * @param
	 * @return
	 */
//	private List<Course> findByGradeId(String unitId, String gradeId) {
//		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
//		if (grade == null) {
//			return new ArrayList<Course>();
//		}
//		List<String> unitIds = new ArrayList<String>();
//		unitIds.add(unitId);
//		Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(unitId), Unit.class);
//		if (topUnit != null) {
//			unitIds.add(topUnit.getId());
//		}
//		// 只取必修课
//		List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdIn(unitIds.toArray(new String[] {}),
//				Integer.parseInt(BaseConstants.SUBJECT_TYPE_BX), NewGkElectiveConstant.SECTION_3 + ""), new TR<List<Course>>() {
//				});
//		return courseList;
//	}

	@RequestMapping("/classFeature/idnex")
	public String showClassFeatureIndex(@PathVariable String arrayItemId, String classId, ModelMap map) {
		NewGkArrayItem arrayItem = newGkArrayItemService.findOne(arrayItemId);
		String divideId = arrayItem.getDivideId();
		List<NewGkDivideClass> allClassList = divideClassService.findByDivideIdAndClassType(getLoginInfo().getUnitId(), divideId, 
				new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_4},
				true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> xzbClassList = allClassList.stream()
				.filter(c->NewGkElectiveConstant.CLASS_TYPE_1.equals(c.getClassType()))
				.collect(Collectors.toList());
		
		Collections.sort(xzbClassList, new Comparator<NewGkDivideClass>() {
			@Override
			public int compare(NewGkDivideClass o1, NewGkDivideClass o2) {
				if(o1.getOrderId() != null && o2.getOrderId() != null && o1.getOrderId().compareTo(o2.getOrderId()) != 0) {
					return o1.getOrderId().compareTo(o2.getOrderId());
				}else if(o1.getClassName() != null) {
					return o1.getClassName().compareTo(Optional.ofNullable(o2.getClassName()).orElse(""));
				}
				
				return 0;
			}
		});
		
		
		// 获取所有教师 安排信息
		List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findByArrayItemIds(
				new String[]{arrayItemId}, true);
		Map<String,Map<String,String>> classSubjTeacherMap = new HashMap<>();
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
					Map<String, String> map2 = classSubjTeacherMap.get(cid);
					if(map2 == null) {
						map2 = new HashMap<>();
						classSubjTeacherMap.put(cid, map2);
					}
					map2.put(subjectId, teacherId);
				}
				teacherIds.add(teacherId);
			}
		}
		Map<String, String> teacherNameMap = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIds.toArray(new String[teacherIds.size()])),new TypeReference<Map<String, String>>(){});
		// TODO
		Map<String,List<NewGkDivideClass>> stuJxbMap = new HashMap<>();
		allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()))
				.filter(e->StringUtils.isNotBlank(e.getBatch()))
				.forEach(cls->{
					cls.getStudentList().forEach(stuId->{
						if(!stuJxbMap.containsKey(stuId)) {
							stuJxbMap.put(stuId, new ArrayList<>());
						}
						stuJxbMap.get(stuId).add(cls);
					});
				});
		
		
		// 获取每个班的按照行政班上课的科目
		NewGkDivide divide = newGkDivideService.findOne(divideId); 
		String unitId = divide.getUnitId();
		List<NewGkSubjectTime> subjectTimeList2 = newGkSubjectTimeService.findByArrayItemId(arrayItemId);
		List<NewGkClassSubjectTime> clsSubTimeList = newGkClassSubjectTimeService.findByArrayItemIdAndClassIdIn(unitId, arrayItemId, null, null, null);
		Map<String, String> firstsdSubjectMap = subjectTimeList2.stream().filter(e->Objects.equals(1, e.getFirstsdWeek())||Objects.equals(2, e.getFirstsdWeek()))
			.collect(Collectors.toMap(e->e.getSubjectId()+"_"+e.getSubjectType(), NewGkSubjectTime::getFirstsdWeekSubject));
		map.put("firstsdSubjectMap", firstsdSubjectMap);
		
		// 存在 拆教学班时 
		List<NewGkChoRelation> relaList = newGkChoRelationService.findByChoiceIdsAndObjectType(unitId, 
				new String[] {divide.getId()}, NewGkElectiveConstant.CHOICE_TYPE_09);
		Map<String, NewGkSubjectTime> stMap = EntityUtils.getMap(subjectTimeList2, e->e.getSubjectId()+"_"+e.getSubjectType());
		if(CollectionUtils.isNotEmpty(relaList)) {
//			Map<String, NewGkSubjectTime> stMap = EntityUtils.getMap(subjectTimeList2, e->e.getSubjectId()+e.getSubjectType());
			
		}
		
		List<NewGkClassFeatureDto> subjectTimeList = subjectTimeList2.stream().map(e->new NewGkClassFeatureDto(e.getSubjectId(), e.getSubjectType(), e.getPeriod())).collect(Collectors.toList());
		Map<String, NewGkClassFeatureDto> stDtoMap = EntityUtils.getMap(subjectTimeList, e->e.getSubjectId()+"_"+e.getSubjectType());
		Map<String, Integer> clsSubCodeMap = EntityUtils.getMap(clsSubTimeList, e->e.getClassId()+e.getSubjectId()+e.getSubjectType(),e->e.getPeriod());
		
		Map<String,List<NewGkClassFeatureDto>> classXzbCourseMap = new HashMap<>();
		List<String> xzbIds = EntityUtils.getList(xzbClassList, NewGkDivideClass::getId);
		Map<String, List<String[]>> xzbSubsMap = divideClassService.findXzbSubjects(unitId, divideId, arrayItemId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, xzbIds);
		Map<String,Number[]> clsWorkTimeMap = new HashMap<>();
		for (NewGkDivideClass theClass : xzbClassList) {
			List<NewGkClassFeatureDto> list = new ArrayList<>();
			List<String[]> subs = xzbSubsMap.get(theClass.getId());
			subs.stream().map(e->e[0]+"_"+e[1]).filter(e->stDtoMap.containsKey(e))
					.map(e->stDtoMap.get(e)).forEach(e->list.add(e));
			
			// FIXME 还需要取出 班级特征的数据
			// 计算 班级的 课时数 单双周 TODO 拆分班级 
			Double xzbCount = 0d;
			for (NewGkClassFeatureDto dto : list) {
				NewGkSubjectTime subTime = stMap.get(dto.getSubjectId()+"_"+dto.getSubjectType());
				if(subTime == null) {
					continue;
				}
				Integer p = clsSubCodeMap.get(theClass.getId()+dto.getSubjectId()+dto.getSubjectType());
				if(p == null) {
					p = subTime.getPeriod();
				}
				xzbCount += p;
				if(p > 0 && !Objects.equals(NewGkElectiveConstant.WEEK_TYPE_NORMAL, subTime.getFirstsdWeek())) {
					xzbCount -= 0.5;
				}
			}
			
			// 教学班课时数
			Map<String, List<NewGkDivideClass>> batchClassMap = theClass.getStudentList().stream()
					.filter(e->stuJxbMap.containsKey(e))
					.flatMap(e->stuJxbMap.get(e).stream())
					.collect(Collectors.groupingBy(cls->cls.getSubjectType()+"_"+cls.getBatch()));
			Integer jxbLecCount = 0;
			for (String batchCode : batchClassMap.keySet()) {
				List<NewGkDivideClass> jxbs = batchClassMap.get(batchCode);
				
				Integer max = jxbs.stream()
						.filter(e->stMap.containsKey(e.getSubjectIds()+"_"+e.getSubjectType()))
						.map(e->stMap.get(e.getSubjectIds()+"_"+e.getSubjectType()).getPeriod())
						.max(Integer::compareTo).orElse(0);
				jxbLecCount += max;
			}
			clsWorkTimeMap.put(theClass.getId(), new Number[2]);
			clsWorkTimeMap.get(theClass.getId())[0] = (jxbLecCount + xzbCount);
			clsWorkTimeMap.get(theClass.getId())[1] = jxbLecCount;
			
			classXzbCourseMap.put(theClass.getId(), list);
		}
		String openType =  divide.getOpenType();
		List<NewGkDivideClass> classList = new ArrayList<>(xzbClassList);
		if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(openType)
				|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(openType)) {
			List<NewGkDivideClass> fakeClassList = allClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType()))
					.collect(Collectors.toList());
			List<String> fakeIds = EntityUtils.getList(fakeClassList, NewGkDivideClass::getId);
			Map<String, List<String[]>> fakeXzbSubjects = divideClassService.findFakeXzbSubjects(unitId, divideId, arrayItemId,
					NewGkElectiveConstant.CLASS_SOURCE_TYPE1, fakeIds);
			for (NewGkDivideClass dc : fakeClassList) {
				List<NewGkClassFeatureDto> list = new ArrayList<>();
				List<String[]> subs = fakeXzbSubjects.get(dc.getId());
				subs.stream().map(e->e[0]+"_"+e[1]).filter(e->stDtoMap.containsKey(e))
						.map(e->stDtoMap.get(e)).forEach(e->list.add(e));
				
				Double xzbCount = 0d;
				for (NewGkClassFeatureDto dto : list) {
					NewGkSubjectTime subTime = stMap.get(dto.getSubjectId()+"_"+dto.getSubjectType());
					if(subTime == null) {
						continue;
					}
					Integer p = clsSubCodeMap.get(dc.getId()+dto.getSubjectId()+dto.getSubjectType());
					if(p == null) {
						p = subTime.getPeriod();
					}
					xzbCount += p;
					if(p > 0 && !Objects.equals(NewGkElectiveConstant.WEEK_TYPE_NORMAL, subTime.getFirstsdWeek())) {
						xzbCount -= 0.5;
					}
				}
				clsWorkTimeMap.put(dc.getId(), new Number[] {xzbCount,0});
				classXzbCourseMap.put(dc.getId(), list);
			}
			
			map.put("fakeXzbList", fakeClassList);
			classList.addAll(fakeClassList);
		}
		
		
		makeCourseName(subjectTimeList,openType);
		
		map.put("allClassList", classList);
		map.put("clsWorkTimeMap", clsWorkTimeMap);
		map.put("teacherNameMap", teacherNameMap);
		map.put("classSubjTeacherMap", classSubjTeacherMap);
		map.put("xzbClassList", xzbClassList);
		map.put("classXzbCourseMap", classXzbCourseMap);
		map.put("classId", classId);
		
		return "/newgkelective/arrayItem/classFeatureIndex.ftl";
	}
	@RequestMapping("/classFeature/one")
	public String showClassFeatureOne(@PathVariable String arrayItemId, String divideId, String classId, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		// 构造 不排课 课程表 信息
		makeScheduleInfo(arrayItemId, map);
		
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		String openType = divide.getOpenType();
		/* 已经有保存的结果 */
		List<NewGkClassFeatureDto> savedCourseTimeList = newGkClassSubjectTimeService.makeExistsClassSubjectInfo(unitId, arrayItemId, classId);
		
		/* 没有保存的结果  */
//		List<NewGkSubjectTime> courseTimeList = newGkClassSubjectTimeService.findClassSubjectList(arrayItemId, classId, unitId);
		NewGkDivideClass divideClass = divideClassService.findOne(classId);
		Map<String, List<String[]>> xzbSubjectsMap = new HashMap<>();
//		Predicate<NewGkSubjectTime> pre = e->false;
		if((NewGkElectiveConstant.DIVIDE_TYPE_10.equals(openType)
				|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(openType))
				&& NewGkElectiveConstant.CLASS_TYPE_4.equals(divideClass.getClassType())) {
			xzbSubjectsMap = divideClassService.findFakeXzbSubjects(unitId, divideId, arrayItemId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, Arrays.asList(classId));
		}else {
//			pre = e->NewGkElectiveConstant.SUBJECT_TYPE_O.equals(e.getSubjectType());
			xzbSubjectsMap = divideClassService.findXzbSubjects(unitId, divideId, arrayItemId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, Arrays.asList(classId));
		}
//		Predicate<NewGkSubjectTime> pre2 = pre;
		List<String[]> subjectIdTypes = xzbSubjectsMap.get(classId);
		Set<String> subjectIdTypes2 = EntityUtils.getSet(subjectIdTypes, e->e[0]+"_"+e[1]);
		List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findByArrayItemId(arrayItemId);
		List<NewGkSubjectTime> courseTimeList = subjectTimeList.stream()
				.filter(e->/*pre2.test(e)|| */subjectIdTypes2.contains(e.getSubjectId()+"_"+e.getSubjectType()))
				.collect(Collectors.toList());
		
		List<NewGkClassFeatureDto> classTimeList = courseTimeList.stream()
				.map(e->new NewGkClassFeatureDto(e.getSubjectId(),e.getSubjectType(),e.getPeriod(),NewGkElectiveConstant.WEEK_TYPE_NORMAL))
				.collect(Collectors.toList());
		Map<String, Integer> weekTypeMap = EntityUtils.getMap(courseTimeList, e->e.getSubjectId()+e.getSubjectType(),e->e.getFirstsdWeek());
		
		// 没有保存结果 根据 科目获取不排课时间
		Set<String> subjectIds = classTimeList.stream().map(e->e.getSubjectId()).collect(Collectors.toSet());
		Map<String, NewGkClassFeatureDto> codToMap = EntityUtils.getMap(classTimeList, NewGkClassFeatureDto::getSubjectCode);
		String[] objectIds = subjectIds.toArray(new String[0]);
		String[] objectType = new String[] {NewGkElectiveConstant.LIMIT_SUBJECT_9};
		List<NewGkLessonTime> lessonTimeList = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, objectIds, objectType, true);
//		Map<String,List<String>> subjectNoTimeMap = new HashMap<>();
		for (NewGkLessonTime lt : lessonTimeList) {
			String courseCode = lt.getObjectId()+"-"+lt.getLevelType();
			
			if(CollectionUtils.isNotEmpty(lt.getTimesList()) && codToMap.containsKey(courseCode)) {
				String noArrangeTime = lt.getTimesList().stream()
						.filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(e.getTimeType()))
						.map(e -> e.getDayOfWeek() + "_" + e.getPeriodInterval() + "_" + e.getPeriod())
						.collect(Collectors.joining(","));
				if(StringUtils.isBlank(noArrangeTime)) {
					continue;
				}
				codToMap.get(courseCode).setNoArrangeTime(noArrangeTime);
			}
		}
		
		
		// 如果某个科目在课程特征中有在 班级特征中没有 就将它添加到 班级特征中去
		Set<String> codes = EntityUtils.getSet(savedCourseTimeList, e->e.getSubjectId()+"-"+e.getSubjectType());
		for (NewGkClassFeatureDto dto : classTimeList) {
			if(!codes.contains(dto.getSubjectId()+"-"+dto.getSubjectType())) {
				savedCourseTimeList.add(dto);
			}
		}

		// 获取科目 名称
		makeCourseName(savedCourseTimeList,openType);
		
		/* 合班和 同时排课信息 */
		Map<String,List<String>> combineMap = new HashMap<>();
		Map<String,List<String>> meanwhileMap = new HashMap<>();
		makeRelations(unitId, arrayItemId, classId, combineMap, meanwhileMap);
		map.put("combineMap", combineMap);
		map.put("meanwhileMap", meanwhileMap);
		
		map.put("courseTimeList", savedCourseTimeList);
		
		return "/newgkelective/arrayItem/classFeature.ftl";
	}
	
	private void makeScheduleInfo(String arrayItemId, ModelMap map) {
		// 先取得整体时间参数
		NewGkArrayItem arrayItem = newGkArrayItemService.findOne(arrayItemId);
		String divideId = arrayItem.getDivideId();
		NewGkDivide divide = newGkDivideService.findById(divideId);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);
		Map<String, Integer> piMap = getIntervalMap(grade);
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("piMap", piMap);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
		
//		map.put("notBasic", "1");
		if (StringUtils.isNotEmpty(grade.getRecess())) {
			String[] ris = grade.getRecess().split(",");
			for(String ri : ris) {
				if(StringUtils.isNotEmpty(ri)) {
					if (ri.startsWith(BaseConstants.PERIOD_INTERVAL_2)) {
						map.put("ab", NumberUtils.toInt(ri.substring(1)));
					} else if(ri.startsWith(BaseConstants.PERIOD_INTERVAL_3)) {
						map.put("pb", NumberUtils.toInt(ri.substring(1)));
					}
				}
			}
		}
		
		// 年级基本设置--object_type=0 objectId=gradeId(不排课) objectId=subjectId(固定科目排课)
		// 科目设置--object_type=9 objectId=subjectId LevelType=A/B/O
		List<NewGkLessonTime> newGkLessonTimeList = newGkLessonTimeService.findByItemIdObjectIdAndGroupType(arrayItemId,
				null, new String[] { NewGkElectiveConstant.LIMIT_GRADE_0},
				null);
		// 年级默认已设置时间点
		Map<String, String> noClickTimeMap = new HashMap<>();
		// 科目时间 key:subjectId-A,不排课，时间
//		Map<String, Map<String, List<String>>> subjectTimeExMap = new HashMap<>();
		makeTimeEx(newGkLessonTimeList, noClickTimeMap, new HashMap<>());

		map.put("noClickTimeMap", noClickTimeMap);
	}
	
	
	/**
	 * 获取 合班 和 同时排课信息
	 * @param unitId TODO
	 * @param arrayItemId
	 * @param classId
	 * @param combineMap
	 * @param meanwhileMap
	 */
	private void makeRelations(String unitId, String arrayItemId, String classId,
			Map<String, List<String>> combineMap, Map<String, List<String>> meanwhileMap) {
		List<NewGkClassCombineRelation> relationList = newGkClassCombineRelationService.findByArrayItemId(unitId, arrayItemId);
		for (NewGkClassCombineRelation rela : relationList) {
			String classSubjectIds = rela.getClassSubjectIds();
			if(classSubjectIds.contains(classId)) {
				
				// 和这个班级有关
				if(NewGkElectiveConstant.COMBINE_TYPE_1.equals(rela.getType())) {
					//合班
					String aid = null;
					String[] csIdArr = classSubjectIds.split(",");
					if(csIdArr[0].contains(classId)) {
						aid = csIdArr[1];
					}else {
						aid = csIdArr[0];
					}
					String subjectCode = aid.substring(aid.indexOf("-")+1);
					List<String> list = combineMap.get(subjectCode);
					if(list == null) {
						list = new ArrayList<>();
						combineMap.put(subjectCode, list);
					}
					list.add(aid.split("-")[0]);
				}else if(NewGkElectiveConstant.COMBINE_TYPE_2.equals(rela.getType())) {
					//同时排课
					String aid = null;
					String subjectCode = null;
					String[] csIdArr = classSubjectIds.split(",");
					if(csIdArr[0].contains(classId)) {
						aid = csIdArr[1];
						subjectCode = csIdArr[0].substring(csIdArr[1].indexOf("-")+1);
					}else {
						aid = csIdArr[0];
						subjectCode = csIdArr[1].substring(csIdArr[0].indexOf("-")+1);
					}
					List<String> list = meanwhileMap.get(subjectCode);
					if(list == null) {
						list = new ArrayList<>();
						meanwhileMap.put(subjectCode, list);
					}
					list.add(aid.replaceFirst("-", ":"));
				}else {
					//未知的合班类型
				}
			}
		}
	}
	
	@RequestMapping("/classFeature/save")
	@ResponseBody
	public String saveClassFeature(@PathVariable String arrayItemId, String classId, 
			@RequestBody List<NewGkClassFeatureDto> courseTimeList) {
		String unitId = getLoginInfo().getUnitId();
		Date now = new Date();
		//1. 获取原始数据
		// newGkClassCombineRelationService;
		Map<String,NewGkClassSubjectTime> classSubjectTimeMap = null;
		Map<String,NewGkLessonTime> lessonTimeMap = null;
		List<NewGkClassSubjectTime> subjectTimeList = newGkClassSubjectTimeService.findByArrayItemIdAndClassIdIn(unitId,
				arrayItemId,new String[] {classId},null, null);
		List<NewGkLessonTime> oldlessonTimeList = null;
		if(CollectionUtils.isNotEmpty(subjectTimeList)) {
			classSubjectTimeMap = EntityUtils.getMap(subjectTimeList, e->e.getClassId()+"-"+e.getSubjectId()+"-"+e.getSubjectType());
			
			Set<String> objectIds = EntityUtils.getSet(subjectTimeList, NewGkClassSubjectTime::getId);
			String[] objectType = new String[] {NewGkElectiveConstant.LIMIT_SUBJECT_5};
			oldlessonTimeList = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, objectIds.toArray(new String[0]), objectType , false);
			lessonTimeMap = EntityUtils.getMap(oldlessonTimeList, NewGkLessonTime::getObjectId);
		}
		
		List<NewGkClassCombineRelation> oldRelaList = newGkClassCombineRelationService.findByArrayItemId(unitId, arrayItemId);
		
		Map<String,List<NewGkClassCombineRelation>> classSubjectRelaMap2 = new HashMap<>();
		Map<String,List<String>> classSubjectRelaMap3 = new HashMap<>();
		for (NewGkClassCombineRelation cr : oldRelaList) {
			String[] split = cr.getClassSubjectIds().split(",");
			
			if(!classSubjectRelaMap2.containsKey(split[0])) {
				classSubjectRelaMap2.put(split[0], new ArrayList<>());
				classSubjectRelaMap3.put(split[0], new ArrayList<>());
			}
			classSubjectRelaMap2.get(split[0]).add(cr);
			classSubjectRelaMap3.get(split[0]).add(split[1]);
			
			if(!classSubjectRelaMap2.containsKey(split[1])) {
				classSubjectRelaMap2.put(split[1], new ArrayList<>());
				classSubjectRelaMap3.put(split[1], new ArrayList<>());
			}
			classSubjectRelaMap2.get(split[1]).add(cr);
			classSubjectRelaMap3.get(split[1]).add(split[0]);
		}
		
		Map<String,Set<NewGkClassCombineRelation>> classSubjectRelaMap = new HashMap<>();
		for (String csc : classSubjectRelaMap3.keySet()) {
			List<String> list = classSubjectRelaMap3.get(csc);
			
			Set<NewGkClassCombineRelation> rList = new HashSet<>();
			for (String c : list) {
				if(classSubjectRelaMap2.get(c) != null)
					rList.addAll(classSubjectRelaMap2.get(c));
			}
			classSubjectRelaMap.put(csc, rList);
		}
		// key : objectId 是NewGkClassSubjectTime的id
		// key : classId-subjectId-subjectType
		// 与某个班级课程相关的所有entity
//		Map<String,List<NewGkClassCombineRelation>> classSubjectRelaMap = new HashMap<>();
		
		
		//2. 
		List<NewGkClassSubjectTime> cstList = new ArrayList<>();
		List<NewGkClassCombineRelation> relaList = new ArrayList<>();
		List<NewGkLessonTime> lessonTimeList = new ArrayList<>();
		List<NewGkLessonTimeEx> lessonTimeExList = new ArrayList<>();
		
		// lessontimeex 也需要删除
		Set<NewGkClassCombineRelation> delRelaList = new HashSet<>();
		NewGkClassSubjectTime cst = null;
		for (NewGkClassFeatureDto dto : courseTimeList) {
			String subjectCode = dto.getSubjectId()+"-"+dto.getSubjectType();
			String classSubjectCode = classId+"-"+subjectCode;
			if(classSubjectTimeMap != null && classSubjectTimeMap.containsKey(classSubjectCode)) {
				cst = classSubjectTimeMap.get(classSubjectCode);
				
				cst.setPeriod(dto.getCourseWorkDay());
				cst.setModifyTime(now);
				cst.setWeekType(dto.getWeekType());
			}else {
				cst = new NewGkClassSubjectTime();
				
				cst.setUnitId(unitId);
				cst.setArrayItemId(arrayItemId);
				cst.setClassId(classId);
				cst.setSubjectId(dto.getSubjectId());
				cst.setSubjectType(dto.getSubjectType());
				cst.setPeriod(dto.getCourseWorkDay());
				cst.setWeekType(dto.getWeekType());
				cst.setId(UuidUtils.generateUuid());
				cst.setCreationTime(now);
				cst.setModifyTime(now);
			}
			cstList.add(cst);
			
			// 禁排时间
			NewGkLessonTime lt = null;
			NewGkLessonTimeEx lte = null;
			String noArrangeTime = dto.getNoArrangeTime();
			if(StringUtils.isNotBlank(noArrangeTime)) {
				String[] split = noArrangeTime.split(",");
				
				if(lessonTimeMap == null || !lessonTimeMap.containsKey(cst.getId())) { // 第一次保存 则新建  lessonTime
					lt = new NewGkLessonTime();
					lt.setId(UuidUtils.generateUuid());
					lt.setArrayItemId(arrayItemId);
					lt.setGroupType(NewGkElectiveConstant.GROUP_TYPE_1);
					lt.setIsJoin(1);
					lt.setModifyTime(now);
					lt.setObjectId(cst.getId());
					lt.setCreationTime(now);
					lt.setObjectType(NewGkElectiveConstant.LIMIT_SUBJECT_5);
					
					lessonTimeList.add(lt);
				}else {  // 不是第一次则从历史数据中取出
					lt = lessonTimeMap.get(cst.getId());
				}
				for (String t : split) {
					String[] timeStr = t.split("_");
					
					lte = new NewGkLessonTimeEx();
					lte.setId(UuidUtils.generateUuid());
					lte.setScourceTypeId(lt.getId());
					lte.setArrayItemId(arrayItemId);
					lte.setCreationTime(now);
					lte.setModifyTime(now);
					lte.setDayOfWeek(Integer.parseInt(timeStr[0]));
					lte.setPeriodInterval(timeStr[1]);
					lte.setPeriod(Integer.parseInt(timeStr[2]));
					lte.setScourceType(NewGkElectiveConstant.SCOURCE_LESSON_01);
					lte.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
					
					lessonTimeExList.add(lte);
				}
				
			}
			
			// 合班及同时排课
			String combineClass = dto.getCombineClass();
			NewGkClassCombineRelation rela = null;
			if(StringUtils.isNotBlank(combineClass)) {  
				String[] split = combineClass.split(",");
				
				List<String> list = new ArrayList<>(Arrays.asList(split));
				list.add(classId);
				for (int i=0;i<list.size();i++) {
					for (int j=i+1;j<list.size();j++) {
						String s = list.get(i)+"-"+subjectCode + "," + list.get(j)+"-"+subjectCode;
						
						rela = new NewGkClassCombineRelation();
						rela.setId(UuidUtils.generateUuid());
						rela.setType(NewGkElectiveConstant.COMBINE_TYPE_1);
						rela.setUnitId(unitId);
						rela.setArrayItemId(arrayItemId);
						rela.setCreationTime(now);
						rela.setModifyTime(now);
						rela.setClassSubjectIds(s);
						relaList.add(rela);
					}
				}
			}
			
			String meanwhiles = dto.getMeanwhiles();
			if(StringUtils.isNotBlank(meanwhiles)) {  
				String[] split = meanwhiles.split(",");
				
				List<String> list = new ArrayList<>(Arrays.asList(split));
				list.add(classId+"-"+subjectCode);
				list = list.stream().map(e->e.replaceAll(":", "-")).collect(Collectors.toList());
				for (int i=0;i<list.size();i++) {
					for (int j=i+1;j<list.size();j++) {
						String s = list.get(i) + "," + list.get(j);
						
						rela = new NewGkClassCombineRelation();
						rela.setId(UuidUtils.generateUuid());
						rela.setType(NewGkElectiveConstant.COMBINE_TYPE_2);
						rela.setUnitId(unitId);
						rela.setArrayItemId(arrayItemId);
						rela.setCreationTime(now);
						rela.setModifyTime(now);
						rela.setClassSubjectIds(s);
						relaList.add(rela);
					}
				}
			}
			//TODO 删除与此相关的所有 同时排课记录
			Set<NewGkClassCombineRelation> list = classSubjectRelaMap.get(classSubjectCode);
			if(CollectionUtils.isNotEmpty(list)) {
				delRelaList.addAll(list);
			}
		}
		
		//3.
		NewGkClassFeatureDto dto = new NewGkClassFeatureDto();
		dto.setClassCombineList(relaList);
		dto.setClassSubjectTimeList(cstList);
		dto.setDelRelaList(delRelaList);
		dto.setDelTimeExScourceIdList(EntityUtils.getSet(oldlessonTimeList, NewGkLessonTime::getId));
		dto.setLessonTimeExList(lessonTimeExList);
		dto.setLessonTimeList(lessonTimeList);
		
		try {
			newGkClassSubjectTimeService.saveResult(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		
		return returnSuccess();
	}
	
	@RequestMapping("/classFeature/copyItem")
	@ResponseBody
	public String copyClassFeatureItem(@PathVariable String arrayItemId,String classId, String subjectCode, 
			@RequestParam(name="copyFlags")String copyFlags, @RequestParam(name="classSubjectCodes[]")String[] classSubjectCodes) {
		// 先获取当前课程的 课时 以及 禁排时间
		String[] split = subjectCode.split("-");
		String subId = split[0];
		String subType = split[1];
		
		String unitId = getLoginInfo().getUnitId();
		List<NewGkClassSubjectTime> claSubjectTimeList = newGkClassSubjectTimeService.findByArrayItemIdAndClassIdIn(unitId,
				arrayItemId,new String[] {classId},new String[] {subId}, new String[] {subType});
		List<NewGkSubjectTime> subjectTimes = newGkSubjectTimeService.findByArrayItemId(arrayItemId);
		Integer period = null;
		List<String> noArrangeTimes = new ArrayList<>();
		if(claSubjectTimeList.size() >0) {
			NewGkClassSubjectTime theOrigin = claSubjectTimeList.get(0);
			if(copyFlags.contains("1"))
				period = theOrigin.getPeriod();
			
			if(copyFlags.contains("2")) { // 禁排时间
				List<NewGkLessonTime> lts = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, new String[] {theOrigin.getId()},
						new String[] {NewGkElectiveConstant.LIMIT_SUBJECT_5}, true);
				if(CollectionUtils.isNotEmpty(lts) && CollectionUtils.isNotEmpty(lts.get(0).getTimesList())) {
					noArrangeTimes = lts.get(0).getTimesList().stream()
						.map(e->e.getDayOfWeek()+"-"+e.getPeriodInterval()+"-"+e.getPeriod()).collect(Collectors.toList());
				}
			}
		}else { // 当前课程 还没有保存
			if(copyFlags.contains("1")) {
				NewGkSubjectTime theOne = subjectTimes.stream().filter(e->subjectCode.equals(e.getSubjectId()+"-"+e.getSubjectType())).findFirst().orElse(null);
				if(theOne != null) {
					period = theOne.getPeriod();
				}
			}
			
			if(copyFlags.contains("2")) { 
				List<NewGkLessonTime> lts = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, new String[] {subId}, 
						new String[] {NewGkElectiveConstant.LIMIT_SUBJECT_9}, true);
				if(CollectionUtils.isNotEmpty(lts)) {
					NewGkLessonTime orElse = lts.stream().filter(e->subType.equals(e.getLevelType())).findFirst().orElse(null);
					if(orElse != null && CollectionUtils.isNotEmpty(orElse.getTimesList())) {
						noArrangeTimes = orElse.getTimesList().stream()
								.filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(e.getTimeType()))
								.map(e->e.getDayOfWeek()+"-"+e.getPeriodInterval()+"-"+e.getPeriod()).collect(Collectors.toList());
					}
				}
				
			}
		}
		// 获取要复制的班级课程 查看是否 存在 存在更改 不存在 添加
		Set<String> csCodes = Stream.of(classSubjectCodes).collect(Collectors.toSet());
		Set<String> classIds = new HashSet<>();
		Set<String> subjectIds = new HashSet<>();
		Set<String> subjectTypes = new HashSet<>();
		for (String classSubCode : csCodes) {
			String[] split2 = classSubCode.split("-");
			classIds.add(split2[0]);
			subjectIds.add(split2[1]);
			subjectTypes.add(split2[2]);
		}
		
		Date now = new Date();
		List<NewGkClassSubjectTime> subjectTimeList2 = newGkClassSubjectTimeService.findByArrayItemIdAndClassIdIn(unitId,
				arrayItemId,classIds.toArray(new String[0]),subjectIds.toArray(new String[0]), subjectTypes.toArray(new String[0]));
		Set<String> set = EntityUtils.getSet(subjectTimeList2, NewGkClassSubjectTime::getId);
		Map<String, NewGkSubjectTime> subjTimeMap = EntityUtils.getMap(subjectTimes, e->e.getSubjectId()+"-"+e.getSubjectType());

		List<NewGkLessonTime> oldLessonTimes = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, set.toArray(new String[0]), 
				new String[] {NewGkElectiveConstant.LIMIT_SUBJECT_5}, true);
		Map<String, NewGkLessonTime> oldLessonTimeMap = EntityUtils.getMap(oldLessonTimes, NewGkLessonTime::getObjectId);
		Map<String, NewGkClassSubjectTime> csCodeMap = EntityUtils.getMap(subjectTimeList2, e->e.getClassId()+"-"+e.getSubjectId()+"-"+e.getSubjectType());
		NewGkClassSubjectTime cst = null;
		NewGkLessonTimeEx lte = null;
		List<NewGkLessonTime> ltL = new ArrayList<>();
		List<NewGkLessonTimeEx> lteL = new ArrayList<>();
		List<NewGkClassSubjectTime> cstL = new ArrayList<>();
		for (String csCode : csCodes) {
			String subjCode = csCode.substring(csCode.indexOf("-")+1);
			NewGkSubjectTime subjectTime = subjTimeMap.get(subjCode);
			
			NewGkLessonTime lt = null;
			if(csCodeMap.get(csCode) != null) { // 数据库中已有数据
				cst = csCodeMap.get(csCode);
				
				lt = oldLessonTimeMap.get(cst.getId());
				
			}else {  // 还没有保存
				String[] split2 = csCode.split("-");
				
				cst = new NewGkClassSubjectTime();
				cst.setId(UuidUtils.generateUuid());
				cst.setCreationTime(now);
				cst.setModifyTime(now);
				cst.setArrayItemId(arrayItemId);
				cst.setUnitId(unitId);
				cst.setClassId(split2[0]);
				cst.setSubjectId(split2[1]);
				cst.setSubjectType(split2[2]);
			}
			if(copyFlags.contains("1")) {
				cst.setPeriod(period);
			}else if(cst.getPeriod() == null){
				cst.setPeriod(subjectTime.getPeriod());
			}
			cstL.add(cst);
			
			if(copyFlags.contains("2")) {
				if(lt == null) {
					lt = makeLessonTime(arrayItemId, now, cst.getId());
					ltL.add(lt);
				}
				List<NewGkLessonTimeEx> timesList = Optional.ofNullable(lt.getTimesList()).orElse(new ArrayList<>());
				Set<String> Oldtimes = EntityUtils.getSet(timesList, e->e.getDayOfWeek()+"-"+e.getPeriodInterval()+"-"+e.getPeriod());
				for (String timestr : noArrangeTimes) {
					if(Oldtimes.contains(timestr))
						continue;
					
					String[] split2 = timestr.split("-");
					lte = makeLessonTimeEx(arrayItemId, now, lt.getId(), split2);
//					timesList.add(lte);
					lteL.add(lte);
				}
//				lteL.addAll(timesList);
			}
		}
		
		NewGkClassFeatureDto dto = new NewGkClassFeatureDto();
		dto.setClassSubjectTimeList(cstL);
//		dto.setDelTimeExScourceIdList(EntityUtils.getSet(oldLessonTimes, NewGkLessonTime::getId));
		dto.setLessonTimeExList(lteL);
		dto.setLessonTimeList(ltL);
		
		try {
			newGkClassSubjectTimeService.saveResult(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		
		return success("复制成功");
	}
	private NewGkLessonTimeEx makeLessonTimeEx(String arrayItemId, Date now, String stId, String[] split2) {
		NewGkLessonTimeEx lte;
		lte = new NewGkLessonTimeEx();
		lte.setId(UuidUtils.generateUuid());
		lte.setCreationTime(now);
		lte.setCreationTime(now);
		lte.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
		lte.setScourceType(NewGkElectiveConstant.SCOURCE_LESSON_01);
		lte.setArrayItemId(arrayItemId);
		lte.setScourceTypeId(stId);
		lte.setDayOfWeek(Integer.parseInt(split2[0]));
		lte.setPeriodInterval(split2[1]);
		lte.setPeriod(Integer.parseInt(split2[2]));
		return lte;
	}
	private NewGkLessonTime makeLessonTime(String arrayItemId, Date now, String objId) {
		NewGkLessonTime lt;
		lt = new NewGkLessonTime();
		lt.setId(UuidUtils.generateUuid());
		lt.setArrayItemId(arrayItemId);
		lt.setCreationTime(now);
		lt.setModifyTime(now);
		lt.setGroupType(NewGkElectiveConstant.GROUP_TYPE_1);
		lt.setObjectId(objId);
		lt.setObjectType(NewGkElectiveConstant.LIMIT_SUBJECT_5);
		lt.setIsJoin(1);
		return lt;
	}
	
	private void makeCourseName(List<NewGkClassFeatureDto> courseTimeList, String openType) {
		Set<String> subjectIds = courseTimeList.stream().map(e->e.getSubjectId()).collect(Collectors.toSet());
		List<Course> courseList = courseRemoteService.findListObjectByIds(subjectIds.toArray(new String[] {}));
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		courseTimeList.forEach(c->{
			if(courseMap.get(c.getSubjectId()) != null) {
				c.setSubjectName(courseMap.get(c.getSubjectId()).getSubjectName());
				if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(c.getSubjectType())) {
					if(NewGkElectiveConstant.DIVIDE_TYPE_07.equals(openType)) {
						c.setSubjectName(c.getSubjectName()+"走");
					}else {
						c.setSubjectName(c.getSubjectName()+"选");
					}
				}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(c.getSubjectType())) {
					c.setSubjectName(c.getSubjectName()+"学");
				}
			}
		});
		// 排序
		Collections.sort(courseTimeList, (x,y)->{
			if(courseMap.get(x.getSubjectId()) != null) {
				return Optional.ofNullable(courseMap.get(x.getSubjectId()).getOrderId()).orElse(Integer.MAX_VALUE)
						.compareTo(Optional.ofNullable(
								Optional.ofNullable(courseMap.get(y.getSubjectId())).orElse(new Course()).getOrderId())
								.orElse(Integer.MAX_VALUE));
			}
			return 1;
		});
	}
	
	
	
}
