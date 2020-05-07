package net.zdsoft.newgkelective.data.action;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.entity.TeachGroupEx;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeachBuildingRemoteService;
import net.zdsoft.basedata.remote.service.TeachGroupExRemoteService;
import net.zdsoft.basedata.remote.service.TeachGroupRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.CourseFeatureContainer;
import net.zdsoft.newgkelective.data.dto.LessonTimeDto;
import net.zdsoft.newgkelective.data.dto.LessonTimeDtoPack;
import net.zdsoft.newgkelective.data.dto.NewGKCourseFeatureDto;
import net.zdsoft.newgkelective.data.dto.NewGkDivideDto;
import net.zdsoft.newgkelective.data.dto.NewGkSubjectTeacherDto;
import net.zdsoft.newgkelective.data.dto.SubjectInfo;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.entity.NewGkRelateSubtime;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherGroup;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherGroupEx;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;
import net.zdsoft.newgkelective.data.entity.NewGkplaceArrange;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeExService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherGroupExService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherGroupService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanExService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.newgkelective.data.service.NewGkplaceArrangeService;
import net.zdsoft.newgkelective.data.utils.MyNumberUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

/**
 * 基础条件设置
 * 
 * @author weixh 2018年8月10日
 */
@Controller
@RequestMapping("/newgkelective/{gradeId}")
public class NewGkElectiveBasicAction extends BaseAction {
	private static final Logger logger = LoggerFactory.getLogger(NewGkElectiveBasicAction.class);
	
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkLessonTimeExService newGkLessonTimeExService;
	@Autowired
	private NewGkplaceArrangeService newGkplaceArrangeService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private TeachBuildingRemoteService teachBuildingRemoteService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	private NewGkTeacherPlanExService newGkTeacherPlanExService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private TeachGroupRemoteService teachGroupRemoteService;
	@Autowired
	private TeachGroupExRemoteService teachGroupExRemoteService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private NewGkTeacherGroupService teacherGroupService;
	@Autowired
	private NewGkTeacherGroupExService teacherGroupExService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;

	@ControllerInfo("基础条件设置首页")
	@RequestMapping("/goBasic/index/page")
	public String index(@PathVariable String gradeId, ModelMap map) {
		map.put("gradeId", gradeId);
		map.put("arrayItemId", gradeId);
		return "/newgkelective/basic/basicIndex.ftl";
	}

	@ControllerInfo("保存新增科目")
	@ResponseBody
	@RequestMapping("/courseFeatures/addSubject")
	public String addSubject(@PathVariable String gradeId, NewGkDivideDto dto) {
		String courseA = dto.getCourseA();
		String courseB = dto.getCourseB();
		String courseO = dto.getCourseO();
		NewGkSubjectTime item;
		List<NewGkSubjectTime> list = new ArrayList<>();
		if (StringUtils.isNotBlank(courseA)) {
			String[] aCourseArr = courseA.split(",");
			for (String courseId : aCourseArr) {
				if (StringUtils.isNotBlank(courseId)) {
					item = new NewGkSubjectTime();
					item.setId(UuidUtils.generateUuid());
					item.setSubjectId(courseId);
					item.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
					item.setCreationTime(new Date());
					item.setPeriod(0);
					item.setIsNeed(1);
					//item.setPunchCard(1);
					item.setArrayItemId(gradeId);
					list.add(item);
				}
			}
		}
		if (StringUtils.isNotBlank(courseB)) {
			String[] courseArr = courseB.split(",");
			for (String courseId : courseArr) {
				if (StringUtils.isNotBlank(courseId)) {
					item = new NewGkSubjectTime();
					item.setId(UuidUtils.generateUuid());
					item.setSubjectId(courseId);
					item.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_B);
					item.setCreationTime(new Date());
					item.setPeriod(0);
					item.setIsNeed(1);
					item.setPunchCard(1);
					item.setArrayItemId(gradeId);
					list.add(item);
				}
			}
		}
		if (StringUtils.isNotBlank(courseO)) {
			String[] courseArr = courseO.split(",");
			for (String courseId : courseArr) {
				if (StringUtils.isNotBlank(courseId)) {
					item = new NewGkSubjectTime();
					item.setId(UuidUtils.generateUuid());
					item.setSubjectId(courseId);
					item.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_O);
					item.setCreationTime(new Date());
					item.setPeriod(0);
					item.setIsNeed(1);
					item.setPunchCard(1);
					item.setArrayItemId(gradeId);
					list.add(item);
				}
			}
		}
		try {
			newGkSubjectTimeService.updateByArrayItem(this.getLoginInfo().getUnitId(), list);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败");
		}
		return success("");
	}

	@ControllerInfo("科目设置保存")
	@ResponseBody
	@RequestMapping("/courseFeatures/courseFeaturesSave")
	public String courseFeaturesSave(@PathVariable String gradeId, CourseFeatureContainer courseFeatureContainer) {
		String arrayId = getRequest().getParameter("arrayId");

		// 不连排科目
		String noContinueSubjectIds = courseFeatureContainer.getNoContinueSubjectIds();
		List<NewGkRelateSubtime> insertRelateSubtime = new ArrayList<>();
		if (StringUtils.isNotBlank(noContinueSubjectIds)) {
			NewGkRelateSubtime obj;
			String[] arr = noContinueSubjectIds.split(",");
			Set<String> set = new HashSet<>();
			for (String s : arr) {
				String[] ss = s.split("_");
				// 去重
				String[] ss1 = new String[] { ss[0], ss[1] };
				Arrays.sort(ss1);
				obj = new NewGkRelateSubtime();
				obj.setSubjectIds(ss1[0] + "," + ss1[1]);
				if (set.contains(obj.getSubjectIds())) {
					continue;
				}
				obj.setCreationTime(new Date());
				obj.setModifyTime(new Date());
				obj.setType(ss[2]);
				obj.setItemId(gradeId);
				insertRelateSubtime.add(obj);
				set.add(obj.getSubjectIds());
			}
		}

		List<NewGKCourseFeatureDto> dtoList = courseFeatureContainer.getDtoList();
		List<NewGkSubjectTime> oldSujectTimeList = newGkSubjectTimeService.findByArrayItemId(gradeId);
		Map<String, NewGkSubjectTime> subjectTimeMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(oldSujectTimeList)) {
			for (NewGkSubjectTime n : oldSujectTimeList) {
				subjectTimeMap.put(n.getSubjectId() + "-" + n.getSubjectType(), n);
			}
		}
		List<NewGkLessonTimeEx> insertTimeExList = new ArrayList<>();
		List<NewGkLessonTime> insertTimeList = new ArrayList<>();
		List<NewGkSubjectTime> subjectTimeList = new ArrayList<>();
		NewGkLessonTimeEx ex;
		NewGkLessonTime newGkLessonTime;
		NewGkSubjectTime subjectTime;
		Map<String, NewGkLessonTime> lessonTimeMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(dtoList)) {
			for (NewGKCourseFeatureDto item : dtoList) {
				if (item != null && StringUtils.isNotBlank(item.getSubjectId())) {
					String key = item.getSubjectId() + "-" + item.getSubjectType();
					if (subjectTimeMap.containsKey(key)) {
						subjectTime = subjectTimeMap.get(key);
					} else {
						subjectTime = new NewGkSubjectTime();
						subjectTime.setId(UuidUtils.generateUuid());
						subjectTime.setCreationTime(new Date());
						subjectTime.setSubjectId(item.getSubjectId());
						subjectTime.setSubjectType(item.getSubjectType());
					}
					subjectTime.setModifyTime(new Date());
					subjectTime.setArrayItemId(gradeId);
					subjectTime.setPeriod(item.getCourseWorkDay());
					subjectTime.setFirstsdWeekSubject(item.getFirstsdWeekSubjectId());
					if (StringUtils.isNotBlank(item.getFirstsdWeekSubjectId())) {
						// 默认1
						subjectTime.setFirstsdWeek(NewGkElectiveConstant.WEEK_TYPE_ODD);
					} else {
						subjectTime.setFirstsdWeek(NewGkElectiveConstant.WEEK_TYPE_NORMAL);
					}
					// 默认7选3
//					subjectTime.setGroupType(NewGkElectiveConstant.GROUP_TYPE_1);
					// 连排课程放在某一天，或几天--不要
					// subjectTime.setCoupleDays(coupleDays);
					subjectTime.setIsNeed(Objects.equals(item.getNeedRoom(),NewGkElectiveConstant.IF_INT_1)?1:0);
					subjectTime.setFollowZhb(item.getFollowZhb());
					if(subjectTime.getFollowZhb() == null){
						subjectTime.setFollowZhb(NewGkElectiveConstant.IF_INT_0);
					}
					//subjectTime.setPunchCard(item.getPunchCard());//默认打卡
					if(subjectTime.getPunchCard() == null) {
						subjectTime.setPunchCard(0);
					}
					// --周连排次数
					subjectTime.setWeekRowNumber(item.getCourseCoupleTimes());
					// --周连排节次--暂时用不到，因为用时间点确定
					// subjectTime.setWeekRowPeriod(weekRowPeriod);
					// subjectTime.setTimeInterval(timeInterval);
					if (StringUtils.isBlank(item.getCourseCoupleType())) {
						// disabled
						item.setCourseCoupleType("0");
					}
					subjectTime.setWeekRowType(item.getCourseCoupleType());
					subjectTime.setArrangeHalfDay(item.getArrangeHalfDay());
					subjectTime.setArrangeDay(item.getArrangeDay());
					subjectTime.setArrangeFrist(item.getArrangePrior());

					if (!"0".equals(item.getCourseCoupleType())) {
						if (StringUtils.isNotBlank(item.getCourseCoupleTypeTimes())) {
							newGkLessonTime = lessonTimeMap.get(key);
							if (newGkLessonTime == null) {
								newGkLessonTime = new NewGkLessonTime();
								newGkLessonTime.setId(UuidUtils.generateUuid());
								newGkLessonTime.setArrayItemId(gradeId);
								newGkLessonTime.setObjectId(item.getSubjectId());
								// 科目
								newGkLessonTime.setObjectType(NewGkElectiveConstant.LIMIT_SUBJECT_9);
								// 学考 选考 O
								newGkLessonTime.setLevelType(item.getSubjectType());
								// 默认7选3
								newGkLessonTime.setGroupType(NewGkElectiveConstant.GROUP_TYPE_1);
								insertTimeList.add(newGkLessonTime);
								lessonTimeMap.put(key, newGkLessonTime);
							}
							// 1-1-1,2-1-1
							String[] timeArr = item.getCourseCoupleTypeTimes().split(",");
							for (String s : timeArr) {
								String[] timeArr2 = s.split("_");
								// 连堂时间限制
								ex = new NewGkLessonTimeEx();
								ex.setId(UuidUtils.generateUuid());
								ex.setDayOfWeek(Integer.parseInt(timeArr2[0]));
								ex.setPeriodInterval(timeArr2[1]);
								ex.setPeriod(Integer.parseInt(timeArr2[2]));
								ex.setScourceType(NewGkElectiveConstant.SCOURCE_LESSON_01);//
								ex.setScourceTypeId(newGkLessonTime.getId());
								ex.setArrayItemId(gradeId);
								// 连堂时间指定时间
								ex.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_03);
								ex.setCreationTime(new Date());
								ex.setModifyTime(new Date());
								insertTimeExList.add(ex);
							}

						}
						
					}
					// 禁止时间

					if (StringUtils.isNotBlank(item.getNoArrangeTime())) {
						newGkLessonTime = lessonTimeMap.get(key);
						if (newGkLessonTime == null) {
							newGkLessonTime = new NewGkLessonTime();
							newGkLessonTime.setId(UuidUtils.generateUuid());
							newGkLessonTime.setArrayItemId(gradeId);
							newGkLessonTime.setObjectId(item.getSubjectId());
							// 科目
							newGkLessonTime.setObjectType(NewGkElectiveConstant.LIMIT_SUBJECT_9);
							// 学考 选考 O
							newGkLessonTime.setLevelType(item.getSubjectType());
							// 默认7选3
							newGkLessonTime.setGroupType(NewGkElectiveConstant.GROUP_TYPE_1);
							insertTimeList.add(newGkLessonTime);
							lessonTimeMap.put(key, newGkLessonTime);
						}
						// 1-1-1,2-1-1
						String[] timeArr = item.getNoArrangeTime().split(",");
						for (String s : timeArr) {
							String[] timeArr2 = s.split("_");
							// 连堂时间限制
							ex = new NewGkLessonTimeEx();
							ex.setId(UuidUtils.generateUuid());
							ex.setDayOfWeek(Integer.parseInt(timeArr2[0]));
							ex.setPeriodInterval(timeArr2[1]);
							ex.setPeriod(Integer.parseInt(timeArr2[2]));
							ex.setScourceType(NewGkElectiveConstant.SCOURCE_LESSON_01);//
							ex.setScourceTypeId(newGkLessonTime.getId());
							ex.setArrayItemId(gradeId);
							// 连堂时间指定时间
							ex.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
							ex.setCreationTime(new Date());
							ex.setModifyTime(new Date());
							insertTimeExList.add(ex);
						}

					}
					subjectTimeList.add(subjectTime);
				}
			}
		}
		try {
			newGkSubjectTimeService.saveAllSubjectTimeItem(this.getLoginInfo().getUnitId(), gradeId, subjectTimeList, insertTimeList,
					insertTimeExList, insertRelateSubtime, arrayId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败");
		}
		return success("");
	}

	@ControllerInfo("科目设置列表")
	@RequestMapping("/courseFeatures/gradeIndex")
	public String courseFeaturesIndex(@PathVariable String gradeId, String useMaster, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		// 先取得整体时间参数
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		Map<String, Integer> piMap = getIntervalMap(grade);

		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("piMap", piMap);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);

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

		List<NewGkSubjectTime> oldSujectTimeList = null;
		// 已经保存的数据
		if(Objects.equals(useMaster, "1")) {
			oldSujectTimeList = newGkSubjectTimeService.findListByWithMaster("arrayItemId", gradeId);
		}else {
			oldSujectTimeList = newGkSubjectTimeService.findListBy("arrayItemId", gradeId);
		}
		if (CollectionUtils.isEmpty(oldSujectTimeList)) {
			List<Course> xzbCourseList = null;

			// 只取必修课
			String sectionStr="";
			Integer section = grade.getSection();
			if(section!=null) {
				sectionStr=String.valueOf(section);
			}
			xzbCourseList = SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection(unitId,
					BaseConstants.SUBJECT_TYPE_BX, sectionStr), new TR<List<Course>>() {
			});
			List<Course> fixedSubs = SUtils.dt(courseRemoteService.getListByCondition(unitId, null, 
					 null, BaseConstants.ZERO_GUID, null, 1, null), new TR<List<Course>>() {});
			Set<String> fixedIds = EntityUtils.getSet(fixedSubs, Course::getId);
			// 屏蔽 掉固定排课科目
			xzbCourseList = xzbCourseList.stream().filter(e->!fixedIds.contains(e.getId())).collect(Collectors.toList());
			
			map.put("xzbCourseList", xzbCourseList);

			List<Course> gkCourseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {
			});
			map.put("gkCourseList", gkCourseList);

			// 默认选中xzb科目(语数英)
			// 默认选中jxb科目(政史地物化生)
			String[] ysyCode = BaseConstants.SUBJECT_TYPES_YSY;
			Set<String> jxbCode = new HashSet<>();
			jxbCode.addAll(BaseConstants.WHS_73);
			jxbCode.addAll(BaseConstants.SDZ_73);

			String ysyCodes = ArrayUtil.print(ysyCode);
			String jxbCodes = jxbCode.stream().reduce((x, y) -> x + "," + y).orElse(null);
			map.put("ysyCodes", ysyCodes);
			map.put("jxbCodes", jxbCodes);
			return "/newgkelective/basic/courseFeaturesAdd.ftl";
		}
		List<NewGKCourseFeatureDto> dtoList = null;

		if(Objects.equals(useMaster, "1")) {
			dtoList = newGkSubjectTimeService.findCourseFeaturesWithMaster(gradeId, map, grade, unitId);
		}else {
			dtoList = newGkSubjectTimeService.findCourseFeatures(gradeId, map, grade, unitId);
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

	@ResponseBody
	@RequestMapping("/courseFeatures/deleteSubject")
	public String deleteSubject(@PathVariable String gradeId, String subjectId,String subjectType) {
		try {
			String arrayId = getRequest().getParameter("arrayId");
			newGkSubjectTimeService.deleteOneSubject(getLoginInfo().getUnitId(),gradeId,subjectId,subjectType, arrayId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("操作失败！");
		}
		return success("");
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

	@ControllerInfo("基础条件设置首页")
	@RequestMapping("/goBasic/gradeSet/index/page")
	public String gradeEdit(@PathVariable String gradeId, ModelMap map) {
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		if (StringUtils.isNotEmpty(grade.getRecess())) {
			String[] ris = grade.getRecess().split(",");
			for(String ri : ris) {
				if(StringUtils.isNotEmpty(ri)) {
					if (ri.startsWith(BaseConstants.PERIOD_INTERVAL_2)) {
						map.put("ab", NumberUtils.toInt(ri.substring(1)) - 1);
					} else if(ri.startsWith(BaseConstants.PERIOD_INTERVAL_3)) {
						map.put("pb", NumberUtils.toInt(ri.substring(1)) - 1);
					}
				}
			}
		}
		map.put("msCount", grade.getMornPeriods());
		map.put("amCount", grade.getAmLessonCount());
		map.put("pmCount", grade.getPmLessonCount());
		map.put("nightCount", grade.getNightLessonCount());
		map.put("recess", grade.getRecess());
		map.put("gradeId", gradeId);
		map.put("arrayItemId", gradeId);
		map.put("objectType", NewGkElectiveConstant.LIMIT_GRADE_0);
		map.put("fixGuid", BaseConstants.ZERO_GUID);
		map.put("dayOfWeekMap2", BaseConstants.dayOfWeekMap2);
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		defaultSaveTime(grade);
		List<Course> subs = SUtils.dt(courseRemoteService.getListByCondition(this.getLoginInfo().getUnitId(), null, null, BaseConstants.ZERO_GUID , null, 1, null), new TR<List<Course>>() {});
		map.put("subs", subs);
		//当前学年最大值
		String semesterJson = semesterRemoteService.getCurrentSemester(2, grade.getSchoolId());
		Semester semester = SUtils.dc(semesterJson, Semester.class);
		if(semester!=null) {
			//获取当前
			int maxMm=semester.getMornPeriods()==null?0:semester.getMornPeriods();
			int maxam=semester.getAmPeriods()==null?0:semester.getAmPeriods();
			int maxpm=semester.getPmPeriods()==null?0:semester.getPmPeriods();
			int maxNi=semester.getNightPeriods()==null?0:semester.getNightPeriods();
			map.put("maxMm", maxMm);
			map.put("maxam", maxam);
			map.put("maxpm", maxpm);
			map.put("maxNi", maxNi);
		}
		
		
		return "/newgkelective/basic/basicGradeSet.ftl";
	}
	
	/**
	 * 年级特征默认添加周末不排课时间
	 * @param grade
	 * @return
	 */
	private void defaultSaveTime(Grade grade) {
		try {
			List<NewGkLessonTime> nots = newGkLessonTimeService.findByItemIdObjectIdAndGroupType(grade.getId(), 
					null, new String[] {NewGkElectiveConstant.LIMIT_GRADE_0}, NewGkElectiveConstant.DIVIDE_GROUP_1);
			if(CollectionUtils.isNotEmpty(nots)) {
				return;
			}
			List<String> amList = MyNumberUtils.getNumList(grade.getAmLessonCount());
			List<String> pmList = MyNumberUtils.getNumList(grade.getPmLessonCount());
			List<String> nightList = MyNumberUtils.getNumList(grade.getNightLessonCount());
			LessonTimeDtoPack pack = new LessonTimeDtoPack();
			pack.setObjType(NewGkElectiveConstant.LIMIT_GRADE_0);
			pack.setNeedSource(true);
			pack.setSelGradeId(grade.getId());
			List<LessonTimeDto> sources = new ArrayList<LessonTimeDto>();
			LessonTimeDto sd = new LessonTimeDto();
			sd.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
			sd.setIs_join(0);
			sd.setObjId(grade.getId());
			sd.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
			sources.add(sd);
			pack.setSourceTimeDto(sources);
			List<LessonTimeDto> lds = new ArrayList<LessonTimeDto>(); 
			int[] wds = {5,6};// 周六、日
			if(CollectionUtils.isNotEmpty(amList)) {
				for(String am : amList) {
					for (int wd : wds) {
						LessonTimeDto dto = new LessonTimeDto();
						dto.setObjId(grade.getId());
						dto.setWeekday(wd);
						dto.setPeriod_interval(BaseConstants.PERIOD_INTERVAL_2);
						dto.setPeriod(NumberUtils.toInt(am));
						dto.setIs_join(0);
						dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
						dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
						lds.add(dto);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(pmList)) {
				for(String am : pmList) {
					for (int wd : wds) {
						LessonTimeDto dto = new LessonTimeDto();
						dto.setObjId(grade.getId());
						dto.setWeekday(wd);
						dto.setPeriod_interval(BaseConstants.PERIOD_INTERVAL_3);
						dto.setPeriod(NumberUtils.toInt(am));
						dto.setIs_join(0);
						dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
						dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
						lds.add(dto);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(nightList)) {
				for(String am : nightList) {
					for (int wd : wds) {
						LessonTimeDto dto = new LessonTimeDto();
						dto.setObjId(grade.getId());
						dto.setWeekday(wd);
						dto.setPeriod_interval(BaseConstants.PERIOD_INTERVAL_4);
						dto.setPeriod(NumberUtils.toInt(am));
						dto.setIs_join(0);
						dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
						dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
						lds.add(dto);
					}
				}
			}
			pack.setLessonTimeDto(lds);
			newGkLessonTimeExService.saveBasicGradeTime(null, grade.getId(), NewGkElectiveConstant.LIMIT_GRADE_0, pack);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@ResponseBody
	@ControllerInfo("基础条件设置首页")
	@RequestMapping("/goBasic/gradeSetSave")
	public String saveBasicGrade(String arrayId, String arrayItemId, LessonTimeDtoPack lessonTimeDtoPack, String objectType) {
		try {
			newGkLessonTimeExService.saveBasicGradeTime(arrayId, arrayItemId, objectType, lessonTimeDtoPack);
		} catch (RuntimeException re) {
			return error(re.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("保存失败！");
		}
		return success("保存成功！");
	}

	@RequestMapping("/goBasic/teacherSet/index/page")
	@ControllerInfo(value = "基础条件-教师特征首页")
	public String teacherSetIndex(@PathVariable("gradeId") String arrayItemId, String courseId,String useMaster, ModelMap map) {
		Map<String, List<NewGkTeacherPlanEx>> subjectTeacherPlanExMap = new HashMap<>();
		List<NewGkTeacherPlan> teacherPlanList=null;
		if(Objects.equals(useMaster, "1")){
			teacherPlanList = newGkTeacherPlanService.findByArrayItemIdsWithMaster(new String[] { arrayItemId }, true);
		}else{
			teacherPlanList = newGkTeacherPlanService.findByArrayItemIds(new String[] { arrayItemId }, true);
		}
		String unitId = getLoginInfo().getUnitId();
		List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findListBy("arrayItemId", arrayItemId);
		Set<String> subjectIds = EntityUtils.getSet(subjectTimeList, e->e.getSubjectId());
		if(CollectionUtils.isEmpty(teacherPlanList)){
			if(CollectionUtils.isNotEmpty(subjectIds)){
				List<TeachGroup> teachGroupList = SUtils.dt(teachGroupRemoteService.findBySchoolIdAndSubjectIdIn(unitId, subjectIds.toArray(new String[subjectIds.size()])), TeachGroup.class);
				List<TeachGroupEx> teachGroupExList = SUtils.dt(teachGroupExRemoteService.findByTeachGroupId(EntityUtils.getList(teachGroupList, TeachGroup::getId).toArray(new String[0])),TeachGroupEx.class);
				Map<String, List<String>> groupTeacherMap = EntityUtils.getListMap(teachGroupExList, e->e.getTeachGroupId(),e->e.getTeacherId());
				Map<String, Set<String>> subjectTeacherMap = new HashMap<String, Set<String>>();
				for (TeachGroup teachGroup : teachGroupList) {
					if(!subjectTeacherMap.containsKey(teachGroup.getSubjectId())){
						subjectTeacherMap.put(teachGroup.getSubjectId(), new HashSet<String>());
					}
					if(groupTeacherMap.containsKey(teachGroup.getId())){
						subjectTeacherMap.get(teachGroup.getSubjectId()).addAll(groupTeacherMap.get(teachGroup.getId()));
					}
				}
				NewGkTeacherPlan plan = null;
				List<NewGkTeacherPlan> planList = new ArrayList<NewGkTeacherPlan>();
				for (Entry<String, Set<String>> entry : subjectTeacherMap.entrySet()) {
					plan = new NewGkTeacherPlan();
					plan.setSubjectId(entry.getKey());
					plan.setExTeacherIdList(entry.getValue().parallelStream().collect(Collectors.toList()));
					planList.add(plan);
				}
				newGkTeacherPlanService.saveAddUpList(arrayItemId, planList, null, null,unitId);
			}
		}
		
		if(CollectionUtils.isNotEmpty(teacherPlanList)){
			Set<String>teacherIdSet = new HashSet<String>();
			for (NewGkTeacherPlan plan : teacherPlanList) {
				subjectIds.add(plan.getSubjectId());
				List<NewGkTeacherPlanEx> planExList = plan.getTeacherPlanExList();
				if (CollectionUtils.isNotEmpty(planExList)) {
					Iterator<NewGkTeacherPlanEx> iter1 = planExList.iterator();
					while (iter1.hasNext()) {
						NewGkTeacherPlanEx ex = iter1.next();
						if (ex == null) {
							iter1.remove();
							continue;
						}
						teacherIdSet.add(ex.getTeacherId());
					}
				}
			}
			
			Map<String, String> teacherNameMap  = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIdSet.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
			for (NewGkTeacherPlan plan : teacherPlanList) {
				List<NewGkTeacherPlanEx> planExList = plan.getTeacherPlanExList();
				if (CollectionUtils.isNotEmpty(planExList)) {
					Iterator<NewGkTeacherPlanEx> iter1 = planExList.iterator();
					while (iter1.hasNext()) {
						NewGkTeacherPlanEx ex = iter1.next();
						if (ex == null) {
							iter1.remove();
							continue;
						}
						String tName = teacherNameMap.get(ex.getTeacherId());
						if (StringUtils.isNotBlank(tName)) {
							ex.setTeacherName(tName);
						} else {
							iter1.remove();
						}
					}
				}
				subjectTeacherPlanExMap.put(plan.getSubjectId(), plan.getTeacherPlanExList());
			}
			
		}
		LinkedHashMap<String,String> coumap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<LinkedHashMap<String, String>>(){});
		map.put("arrayItemId", arrayItemId);
		map.put("subjectTeacherPlanExMap", subjectTeacherPlanExMap);
		map.put("coumap", coumap); 
		if(StringUtils.isNotBlank(courseId) && coumap.containsKey(courseId)){
			map.put("courseId", courseId);
		}
		return "/newgkelective/basic/teacherArrangeIndex.ftl";
	}

	@RequestMapping("/goBasic/teacherSet/index/list")
	@ControllerInfo(value = "基础条件-教师特征设置")
	public String doAdd(@PathVariable("gradeId") String arrayItemId, String courseId, String teacherName, String useMaster,
			ModelMap map) {
		map.put("arrayItemId", arrayItemId);
		map.put("courseId", courseId);
		map.put("teacherName", teacherName);
		map.put("useMaster", useMaster);
        List<NewGkTeacherPlan> teacherPlanList = null;
        if ("1".equals(useMaster)) {
            teacherPlanList = newGkTeacherPlanService
                    .findByArrayItemIdsWithMaster(new String[] { arrayItemId }, true);
        } else {
            teacherPlanList = newGkTeacherPlanService
                    .findByArrayItemIds(new String[] { arrayItemId }, true);
        }
        List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findListBy("arrayItemId", arrayItemId);
        Set<String> subjectIds = EntityUtils.getSet(subjectTimeList, e->e.getSubjectId());
        
        // 教师基础数据
        Set<String>teacherIdSet = new HashSet<String>();
		for (NewGkTeacherPlan plan : teacherPlanList) {
			subjectIds.add(plan.getSubjectId());
			List<NewGkTeacherPlanEx> planExList = plan.getTeacherPlanExList();
			if (CollectionUtils.isNotEmpty(planExList)) {
				Iterator<NewGkTeacherPlanEx> iter1 = planExList.iterator();
				while (iter1.hasNext()) {
					NewGkTeacherPlanEx ex = iter1.next();
					if (ex == null) {
						iter1.remove();
						continue;
					}
					teacherIdSet.add(ex.getTeacherId());
				}
			}
		}
		Map<String, String> teacherNameMap  = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIdSet.toArray(new String[0])),new TypeReference<Map<String, String>>(){});

		LinkedHashMap<String,String> coumap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<LinkedHashMap<String, String>>(){});
		map.put("courseNameMap", coumap);
		Map<String, String> courseCountMap = EntityUtils.getMap(teacherPlanList, NewGkTeacherPlan::getSubjectId, NewGkTeacherPlan::getTeacherCounts);
		map.put("courseCountMap", courseCountMap);
		// 科目老师数据整理
		List<NewGkTeacherPlan> showPlanList = new ArrayList<NewGkTeacherPlan>();
		showPlanList.addAll(teacherPlanList);
		if (StringUtils.isNotBlank(courseId)) {
			showPlanList = showPlanList.parallelStream().filter(e -> courseId.equals(e.getSubjectId()))
					.collect(Collectors.toList());
		}
		List<NewGkTeacherPlanEx> planExList = new ArrayList<NewGkTeacherPlanEx>();
		if (StringUtils.isNotBlank(teacherName)) {
			/*if (teacherNameMap.containsValue(teacherName.trim())) {
				return "/newgkelective/basic/teacherArrangeList.ftl";
			}*/
			Map<String, String> tmp = new HashMap<>();
			for (Map.Entry<String, String> one : teacherNameMap.entrySet()) {
				if (StringUtils.isNotBlank(one.getValue()) && one.getValue().indexOf(teacherName.trim()) >= 0) {
					tmp.put(one.getKey(), one.getValue());
				}
			}
			teacherNameMap = tmp;
			for (NewGkTeacherPlan plan : showPlanList) {
				List<NewGkTeacherPlanEx> inList = plan.getTeacherPlanExList();
				for (NewGkTeacherPlanEx planEx : inList) {
					if (teacherNameMap.containsKey(planEx.getTeacherId())) {
						planExList.add(planEx);
					}
				}
			}
		} else {
			for (NewGkTeacherPlan plan : showPlanList) {
				planExList.addAll(plan.getTeacherPlanExList());
			}
		}

		Set<String> tids = EntityUtils.getSet(planExList, NewGkTeacherPlanEx::getTeacherId);

		// 排课时间显示
		Map<String, List<NewGkLessonTimeEx>> exMap = new HashMap<String, List<NewGkLessonTimeEx>>();
        List<NewGkLessonTime> times = null;
        if ("1".equals(useMaster)) {
            times = newGkLessonTimeService.findByItemIdObjectIdWithMaster(arrayItemId,
                    tids.toArray(new String[0]), new String[] { NewGkElectiveConstant.LIMIT_TEACHER_2 }, false);
        } else {
            times = newGkLessonTimeService.findByItemIdObjectId(arrayItemId,
                    tids.toArray(new String[0]), new String[] { NewGkElectiveConstant.LIMIT_TEACHER_2 }, false);
        }
        Map<String, StringBuilder> exTids = new HashMap<String, StringBuilder>();
        Set<String> mutexTids = new HashSet<>();
		if (CollectionUtils.isNotEmpty(times)) {
			Map<String, String> sids = EntityUtils.getMap(times, NewGkLessonTime::getId, NewGkLessonTime::getObjectId);
			// 互斥教师
            List<NewGkChoRelation> res = null;
            if ("1".equals(useMaster)) {
                res = newGkChoRelationService.findByChoiceIdsAndObjectTypeWithMaster(
                        getLoginInfo().getUnitId(), sids.keySet().toArray(new String[0]),
                        NewGkElectiveConstant.CHOICE_TYPE_07);
            } else {
                res = newGkChoRelationService.findByChoiceIdsAndObjectType(
                        getLoginInfo().getUnitId(), sids.keySet().toArray(new String[0]),
                        NewGkElectiveConstant.CHOICE_TYPE_07);
            }
            if (CollectionUtils.isNotEmpty(res)) {
				for (NewGkChoRelation re : res) {
					String tid = sids.get(re.getChoiceId());
					StringBuilder sb = exTids.get(tid);
					if (sb == null) {
						sb = new StringBuilder();
						exTids.put(tid, sb);
					} else {
						sb.append(",");
					}
					sb.append(re.getObjectValue());
					mutexTids.add(re.getObjectValue());
				}
			}
            List<NewGkLessonTimeEx> exs = null;
            if ("1".equals(useMaster)) {
                exs = newGkLessonTimeExService.findByObjectIdWithMaster(sids.keySet().toArray(new String[0]),
                        new String[] { NewGkElectiveConstant.SCOURCE_TEACHER_02 });
            } else {
                exs = newGkLessonTimeExService.findByObjectId(sids.keySet().toArray(new String[0]),
                        new String[] { NewGkElectiveConstant.SCOURCE_TEACHER_02 });
            }
            if (CollectionUtils.isNotEmpty(exs)) {
				for (NewGkLessonTimeEx ex : exs) {
					String tid = sids.get(ex.getScourceTypeId());
					List<NewGkLessonTimeEx> exList = exMap.get(tid + ex.getTimeType());
					if (exList == null) {
						exList = new ArrayList<NewGkLessonTimeEx>();
						exMap.put(tid + ex.getTimeType(), exList);
					}
					exList.add(ex);
				}
			}
		}
		
		// 添加 教师组 禁排安排
		List<NewGkTeacherGroupEx> tgExList = teacherGroupExService.findByGradeIdAndTid(arrayItemId, tids.toArray(new String[0]));
		Map<String,List<String>> tgTidMap = EntityUtils.getListMap(tgExList, NewGkTeacherGroupEx::getTeacherGroupId,NewGkTeacherGroupEx::getTeacherId);
		List<NewGkLessonTime> lessonTimes = newGkLessonTimeService.findByItemIdObjectIdWithMaster(arrayItemId, tgTidMap.keySet().toArray(new String[0]), 
				new String[] {NewGkElectiveConstant.LIMIT_TEACHER_GROUP_6}, true); 
		Map<String,List<NewGkLessonTimeEx>> teacherTimeMap = new HashMap<>();
		List<NewGkLessonTimeEx> nullList = new ArrayList<>();
		for (NewGkLessonTime lt : lessonTimes) {
			List<String> tids2 = tgTidMap.get(lt.getObjectId());
			List<NewGkLessonTimeEx> timesList = lt.getTimesList();
			if(CollectionUtils.isEmpty(tids2) || CollectionUtils.isEmpty(timesList)) {
				continue;
			}
//			Set<String> noTimes = EntityUtils.getSet((timesList==null?nullList:timesList), e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod());
			for (String tid : tids2) {
				List<NewGkLessonTimeEx> times2 = teacherTimeMap.get(tid);
				if(times2 == null) {
					times2 = new ArrayList<>();
					teacherTimeMap.put(tid, times2);
				}
				times2.addAll(timesList);
			}
		}
		
		Map<String, String> mutexTeaNameMap  = SUtils.dt(teacherRemoteService.findPartByTeacher(mutexTids.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		for (NewGkTeacherPlanEx ex : planExList) {
			// 教师
			ex.setTeacherName(teacherNameMap.get(ex.getTeacherId()));
			// time
			List<NewGkLessonTimeEx> texList = exMap.get(ex.getTeacherId() + NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
			List<NewGkLessonTimeEx> set = teacherTimeMap.get(ex.getTeacherId());
			long count = Stream.of(texList,set).filter(e->e!=null).flatMap(e->e.stream())
					.map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())
					.distinct().count();
			ex.setNoTimeStr(String.valueOf(count));
			
			// 互斥教师
			if (exTids.containsKey(ex.getTeacherId())) {
				ex.setMutexTeaIds(exTids.get(ex.getTeacherId()).toString());
				String[] mtids = ex.getMutexTeaIds().split(",");
				List<String> mutexTeaIdList = new ArrayList<String>();
				StringBuilder sbi = new StringBuilder();
				StringBuilder sb = new StringBuilder();
				for (String tid : mtids) {
					if (mutexTeaNameMap.containsKey(tid)) {
						if (sbi.length() > 0) {
							sbi.append(",");
						}
						sbi.append(tid);
						mutexTeaIdList.add(tid);
						if (sb.length() > 0) {
							sb.append(",");
						}
						sb.append(mutexTeaNameMap.get(tid));
					}
				}
				ex.setMutexTeaIds(sbi.toString());
				ex.setMutexTeaNames(sb.toString());
				ex.setMutexTeaIdList(mutexTeaIdList);
			}
		}
		Map<String, String> planSubjectMap = EntityUtils.getMap(teacherPlanList, NewGkTeacherPlan::getId,
				NewGkTeacherPlan::getSubjectId);
		Map<String, List<NewGkTeacherPlanEx>> planExMap = EntityUtils.getListMap(planExList,
				NewGkTeacherPlanEx::getTeacherPlanId, Function.identity());
		
		Map<String, List<NewGkTeacherPlanEx>> subjectExMap = new HashMap<String, List<NewGkTeacherPlanEx>>();
		for (Entry<String, List<NewGkTeacherPlanEx>> entry : planExMap.entrySet()) {
			subjectExMap.put(planSubjectMap.get(entry.getKey()), entry.getValue());
		}
		LinkedHashMap<String,List<NewGkTeacherPlanEx>> subjectExMap1 = new LinkedHashMap<String, List<NewGkTeacherPlanEx>>();
		for(String key :coumap.keySet()){//排序
			List<NewGkTeacherPlanEx> list = subjectExMap.get(key);
			if(CollectionUtils.isNotEmpty(list)){
				subjectExMap1.put(key, list);
			}
		}
		map.put("subjectExMap", subjectExMap1);
		teacherNameMap.putAll(mutexTeaNameMap);
		map.put("teacherNameMap", teacherNameMap);

		return "/newgkelective/basic/teacherArrangeList.ftl";
	}

	@RequestMapping("/goBasic/teacherSet/selectTeacher")
	@ControllerInfo(value = "基础条件-选择教研组")
	public String doSelectTeacher(@PathVariable("gradeId") String arrayItemId,String courseId, ModelMap map) {
		map.put("arrayItemId", arrayItemId);
		map.put("courseId", courseId);
		String unitId = getLoginInfo().getUnitId();
		List<TeachGroup> baseTeachGroupList = SUtils.dt(teachGroupRemoteService.findBySchoolId(unitId),TeachGroup.class);
		Map<String, List<String>> selectSubjectTeacherMap = new HashMap<>();
		
		if (StringUtils.isNotEmpty(arrayItemId)) {
			List<NewGkTeacherPlan> planList = newGkTeacherPlanService.findByArrayItemIds(new String[] { arrayItemId },
					true);
			for (NewGkTeacherPlan plan : planList) {
				selectSubjectTeacherMap.put(plan.getSubjectId(), plan.getExTeacherIdList());
			}
		}

		Set<String> baseTeachGroupIdSet = EntityUtils.getSet(baseTeachGroupList, TeachGroup::getId);
		List<TeachGroupEx> baseTeachGroupExList = SUtils.dt(teachGroupExRemoteService.findByTeachGroupId(baseTeachGroupIdSet.toArray(new String[0])),TeachGroupEx.class);
		Map<String, List<String>> teachGroupIdToTeacherId = EntityUtils.getListMap(baseTeachGroupExList,TeachGroupEx::getTeachGroupId, TeachGroupEx::getTeacherId);

		Set<String> allSubIds = EntityUtils.getSet(baseTeachGroupList, TeachGroup::getSubjectId);
		List<Course> courseList = SUtils.dt(courseRemoteService.findByIds(allSubIds.toArray(new String[0])), Course.class);
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		Set<String> teacherIdSet = new HashSet<String>();
		Grade grade = gradeRemoteService.findOneObjectById(arrayItemId);
		String section = grade.getGradeCode().substring(0, 1);
		// key-groupId
		LinkedHashMap<String, SubjectInfo> subjectInfoMap = new LinkedHashMap<String, SubjectInfo>();
		for (TeachGroup group : baseTeachGroupList) {
			Course course = courseMap.get(group.getSubjectId());
			if(course == null|| course.getSection() == null || !course.getSection().contains(section)) {
				continue;
			}
			
			String groupId = group.getId();
			subjectInfoMap.put(group.getId(), new SubjectInfo());
			subjectInfoMap.get(groupId).setSubjectId(group.getSubjectId());
			// 设置subjectName
			subjectInfoMap.get(groupId).setSubjectName(group.getTeachGroupName());
			List<String> teacherIds = Optional.ofNullable(teachGroupIdToTeacherId.get(groupId)).orElse(new ArrayList<>());
			if(CollectionUtils.isNotEmpty(teacherIds)) {
				for (String teacherId : teacherIds) {
					teacherIdSet.add(teacherId);
					subjectInfoMap.get(groupId).getTeacherIdAndState().put(teacherId, "0");
				}
			}
			// 改变已经选择老师的状态
			List<String> selectTeacherIds = selectSubjectTeacherMap.get(group.getSubjectId());
			int i = 0;
			if (selectTeacherIds != null && selectTeacherIds.size() > 0) {
				for (String teacherId : selectTeacherIds) {
					// 已经保存过 老师数据 后来在教研组删除这个老师造成的bug
					if (teacherIds.contains(teacherId)) {
						subjectInfoMap.get(groupId).getTeacherIdAndState().put(teacherId, "1");
						i++;
					}
				}
			}
			subjectInfoMap.get(groupId).setSelectTeacherNumber(i);
		}
		
		Map<String, String> teachMap = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIdSet.toArray(new String[teacherIdSet.size()])),new TypeReference<Map<String, String>>(){});

		map.put("teacherIdToTeacherName", teachMap);

		map.put("subjectInfoMap", subjectInfoMap);

		map.put("teacherNumber", teacherIdSet.size() + "");

		return "/newgkelective/basic/teacherSelect.ftl";
	}

	@ResponseBody
	@RequestMapping("/goBasic/teacherSet/saveTeacher")
	@ControllerInfo(value = "基础条件-保存教师安排")
	public String doSaveTeacher(@PathVariable("gradeId") String arrayItemId, NewGkSubjectTeacherDto subjectTeacherDto) {
		try {
			List<NewGkTeacherPlan> planList = subjectTeacherDto.getTeacherPlanList();
			if (CollectionUtils.isEmpty(planList)) {
				newGkTeacherPlanService.deleteByArrayItemId(arrayItemId);
				return success("操作成功！");
			}
			Map<String, Set<String>> tempMap = new HashMap<String, Set<String>>();
			for (NewGkTeacherPlan plan : planList) {
				if (!tempMap.containsKey(plan.getSubjectId())) {
					tempMap.put(plan.getSubjectId(), new HashSet<String>());
				}
				List<String> exTeacherIdList = plan.getExTeacherIdList();
				tempMap.get(plan.getSubjectId()).addAll(exTeacherIdList);
			}
			planList.clear();
			NewGkTeacherPlan plan;
			for (Entry<String, Set<String>> entry : tempMap.entrySet()) {
				plan = new NewGkTeacherPlan();
				plan.setSubjectId(entry.getKey());
				plan.setExTeacherIdList(entry.getValue().parallelStream().collect(Collectors.toList()));
				planList.add(plan);
			}
			newGkTeacherPlanService.saveAddUpList(arrayItemId, planList, null, null,this.getLoginInfo().getUnitId());
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return Json.toJSONString(
				new ResultDto().setSuccess(true).setCode("00").setMsg("操作成功！"));
	}

	@ResponseBody
	@RequestMapping("/goBasic/teacherSet/copyArrange")
	@ControllerInfo("基础条件-教师特征复制")
	public String copyTeacherTime(@PathVariable("gradeId") String arrayItemId, String planExId, String planExIds,
			String copyFields, ModelMap map) {
		try {
			NewGkTeacherPlanEx planEx = newGkTeacherPlanExService.findOne(planExId);
			List<NewGkTeacherPlanEx> planExList = newGkTeacherPlanExService.findListByIds(planExIds.split(","));
			if (copyFields.contains("1")) {
				for (NewGkTeacherPlanEx ex : planExList) {
					ex.setDayPeriodType(planEx.getDayPeriodType());
				}
			}
			if (copyFields.contains("2")) {
				for (NewGkTeacherPlanEx ex : planExList) {
					ex.setWeekPeriodType(planEx.getWeekPeriodType());
				}
			}
			if (copyFields.contains("3") || copyFields.contains("4")) {
				String unitId = getLoginInfo().getUnitId();
				Set<String> tidSet = EntityUtils.getSet(planExList, NewGkTeacherPlanEx::getTeacherId);
				List<NewGkLessonTime> thisTimes = newGkLessonTimeService.findByItemIdObjectId(arrayItemId,
						new String[] { planEx.getTeacherId() }, new String[] { NewGkElectiveConstant.LIMIT_TEACHER_2 },
						false);
				if (CollectionUtils.isNotEmpty(thisTimes)) {
					if (copyFields.contains("3")) {
						List<NewGkLessonTime> times = newGkLessonTimeService.findByItemIdObjectId(arrayItemId,
								tidSet.toArray(new String[0]), new String[] { NewGkElectiveConstant.LIMIT_TEACHER_2 },
								false);
						times.addAll(thisTimes);
						Map<String, HashSet<String>> exTids = new HashMap<String, HashSet<String>>();
						Map<String, String> sids = EntityUtils.getMap(times, NewGkLessonTime::getId,
								NewGkLessonTime::getObjectId);
						// 互斥教师
						List<NewGkChoRelation> res = newGkChoRelationService.findByChoiceIdsAndObjectType(
								getLoginInfo().getUnitId(), sids.keySet().toArray(new String[0]),
								NewGkElectiveConstant.CHOICE_TYPE_07);
						if (CollectionUtils.isNotEmpty(res)) {
							for (NewGkChoRelation re : res) {
								String tid = sids.get(re.getChoiceId());
								HashSet<String> se = exTids.get(tid);
								if (se == null) {
									se = new HashSet<String>();
									exTids.put(tid, se);
								}
								se.add(re.getObjectValue());
							}
						}
						if (exTids.containsKey(planEx.getTeacherId())) {
							for (NewGkTeacherPlanEx ex : planExList) {
								if (exTids.containsKey(ex.getTeacherId())) {
									Set<String> se = exTids.get(ex.getTeacherId());
									se.addAll((exTids.get(planEx.getTeacherId())));
									se.remove(ex.getTeacherId());
									ex.setMutexTeaIds(StringUtils.join(se, ","));
									ex.setMutexNum(ex.getMutexNum() + planEx.getMutexNum());
								} else {
									HashSet<String> se = exTids.get(planEx.getTeacherId());
									se.remove(ex.getTeacherId());
									ex.setMutexTeaIds(StringUtils.join(se, ","));
									ex.setMutexNum(planEx.getMutexNum());
								}
							}
							newGkTeacherPlanExService.saveExs(unitId, null, null, arrayItemId, planExList);
						}
					}
					if (copyFields.contains("4")) {
						Set<String> ids = EntityUtils.getSet(thisTimes, NewGkLessonTime::getId);
						List<NewGkLessonTimeEx> allExList = newGkLessonTimeExService.findByObjectId(
								ids.toArray(new String[] {}),
								new String[] { NewGkElectiveConstant.SCOURCE_TEACHER_02 });
						allExList = allExList.stream().filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(e.getTimeType())).collect(Collectors.toList());
						String[] arrTeacherIds = tidSet.toArray(new String[0]);
						List<NewGkLessonTime> timeList = newGkLessonTimeService.findByItemIdObjectId(arrayItemId,
								arrTeacherIds, new String[] { NewGkElectiveConstant.LIMIT_TEACHER_2 }, false);
						Map<String, NewGkLessonTime> tmMap = new HashMap<String, NewGkLessonTime>();
						if (CollectionUtils.isNotEmpty(timeList)) {
							tmMap = EntityUtils.getMap(timeList, NewGkLessonTime::getObjectId);
						}
						List<NewGkLessonTime> insertList = new ArrayList<NewGkLessonTime>();
						List<NewGkLessonTimeEx> exInsertList = new ArrayList<NewGkLessonTimeEx>();
						Set<String> exSourceIds = new HashSet<String>();
						for (String s : arrTeacherIds) {
							// 新增一条NewGkLessonTime记录
							NewGkLessonTime tt;
							if (tmMap.containsKey(s)) {
								tt = tmMap.get(s);
								exSourceIds.add(tt.getId());
							} else {
								tt = initNewGkLessonTime(arrayItemId, s, NewGkElectiveConstant.LIMIT_TEACHER_2);
								insertList.add(tt);
							}
							List<NewGkLessonTimeEx> exList1 = initNewGkLessonTimeExListCopy(tt.getId(),
									tt.getArrayItemId(), allExList);
							exInsertList.addAll(exList1);
						}

						// 原有基础上增加
						newGkLessonTimeService.saveCopyLessonTime(arrayItemId, arrTeacherIds, insertList, exInsertList,
								NewGkElectiveConstant.LIMIT_TEACHER_2, exSourceIds);
					}
				}
			} else {
				newGkTeacherPlanExService.saveAll(planExList.toArray(new NewGkTeacherPlanEx[0]));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return error("复制失败");
		}
		return success("");
	}

	private NewGkLessonTime initNewGkLessonTime(String arrayItemId, String objId, String objType) {
		NewGkLessonTime newGkLessonTime = new NewGkLessonTime();
		newGkLessonTime.setId(UuidUtils.generateUuid());
		String[] objs = objId.split("-");
		newGkLessonTime.setObjectId(objs[0]);
		newGkLessonTime.setObjectType(objType);
		newGkLessonTime.setIsJoin(1);
		newGkLessonTime.setArrayItemId(arrayItemId);
		newGkLessonTime.setCreationTime(new Date());
		newGkLessonTime.setModifyTime(new Date());
		if (objs.length > 1) {
			newGkLessonTime.setLevelType(objs[1]);
			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(newGkLessonTime.getLevelType())) {
				newGkLessonTime.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_5);
			} else if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(newGkLessonTime.getLevelType())) {
				newGkLessonTime.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_6);
			}
		}
		if (StringUtils.isEmpty(newGkLessonTime.getGroupType())) {
			newGkLessonTime.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
		}
		return newGkLessonTime;
	}

	private List<NewGkLessonTimeEx> initNewGkLessonTimeExListCopy(String lessonTimeId, String arrayItemId,
			List<NewGkLessonTimeEx> oldList) {
		NewGkLessonTimeEx newGkLessonTimeEx = null;
		List<NewGkLessonTimeEx> newGkLessonTimeExList = new ArrayList<NewGkLessonTimeEx>();
		for (NewGkLessonTimeEx ex : oldList) {
			newGkLessonTimeEx = new NewGkLessonTimeEx();
			newGkLessonTimeEx.setId(UuidUtils.generateUuid());
			newGkLessonTimeEx.setDayOfWeek(ex.getDayOfWeek());
			newGkLessonTimeEx.setPeriod(ex.getPeriod());
			newGkLessonTimeEx.setPeriodInterval(ex.getPeriodInterval());
			newGkLessonTimeEx.setScourceType(ex.getScourceType());// 教师 NewGkElectiveConstant.SCOURCE_TEACHER_02
			newGkLessonTimeEx.setScourceTypeId(lessonTimeId);
			newGkLessonTimeEx.setArrayItemId(arrayItemId);
			newGkLessonTimeEx.setTimeType(ex.getTimeType());
			newGkLessonTimeEx.setCreationTime(new Date());
			newGkLessonTimeEx.setModifyTime(new Date());
			newGkLessonTimeExList.add(newGkLessonTimeEx);
		}
		return newGkLessonTimeExList;
	}

	@ControllerInfo("基础条件设置首页")
	@RequestMapping("/goBasic/placeSet/index/page")
	public String placeSet(@PathVariable("gradeId") String gradeId, ModelMap map) {
		map.put("gradeId", gradeId);
		String unitId = getLoginInfo().getUnitId();
		List<NewGkplaceArrange> arrangePlaceList = newGkplaceArrangeService.findByArrayItemId(gradeId);
		
		List<TeachPlace> teachPlaceList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceListByType(unitId, null), TeachPlace.class);
		List<TeachBuilding> buildList =  SUtils.dt(teachBuildingRemoteService.findTeachBuildListByUnitId(unitId),TeachBuilding.class);
		TeachBuilding noBuilding = new TeachBuilding();
		final String noBuildingId = "NO_BUILDING";
		noBuilding.setId(noBuildingId);
		noBuilding.setBuildingName("无楼层");
		teachPlaceList.stream().filter(e->StringUtils.isBlank(e.getTeachBuildingId())).forEach(e->e.setTeachBuildingId(noBuildingId));
		
		Map<String, List<TeachPlace>> placeByBuildingMap = EntityUtils.getListMap(teachPlaceList, TeachPlace::getTeachBuildingId, e->e);
		if(placeByBuildingMap.containsKey(noBuildingId)) {
			buildList.add(noBuilding);
		}
		Map<String, String> buildNameMap = EntityUtils.getMap(buildList, TeachBuilding::getId,TeachBuilding::getBuildingName);
		teachPlaceList.stream().filter(e->buildNameMap.containsKey(e.getTeachBuildingId()))
				.forEach(e->e.setTeachBuildingName(buildNameMap.get(e.getTeachBuildingId())));
		List<String> placeIds = EntityUtils.getList(arrangePlaceList, NewGkplaceArrange::getPlaceId);
		Map<String,Integer> placeOldMap = new HashMap<>();
		int i=0;
		for (String pid : placeIds) {
			placeOldMap.put(pid, i++);
		}
		
		map.put("teachPlaceList", teachPlaceList);
		map.put("placeIds", placeIds);
		map.put("buildList", buildList);
		map.put("arrangePlaceList", teachPlaceList.stream().filter(e->placeIds.contains(e.getId()))
				.sorted((x,y)->placeOldMap.get(x.getId()).compareTo(placeOldMap.get(y.getId()))).collect(Collectors.toList()));
		map.put("placeByBuildingMap", placeByBuildingMap);
		return "/newgkelective/basic/basicPlaceSet.ftl";
	}

	@ResponseBody
	@RequestMapping("/goBasic/placeSetSave")
	public String saveBasicPlace(@PathVariable("gradeId") String gradeId, String placeIdsTemp, String placeNamesTemp) {
		try {
			String[] placeIdArr = placeIdsTemp.split(",");
			NewGkplaceArrange[] all = new NewGkplaceArrange[0];
			if (StringUtils.isNotBlank(placeIdsTemp)) {
				all = new NewGkplaceArrange[placeIdArr.length];
				for (int i = 0; i < placeIdArr.length; i++) {
					NewGkplaceArrange newGkplaceArrange = new NewGkplaceArrange();
					newGkplaceArrange.setId(UuidUtils.generateUuid());
					newGkplaceArrange.setArrayItemId(gradeId);
					newGkplaceArrange.setCreationTime(new Date());
					newGkplaceArrange.setModifyTime(new Date());
					newGkplaceArrange.setOrderId(i);
					newGkplaceArrange.setPlaceId(placeIdArr[i]);
					all[i] = newGkplaceArrange;
				}
			}
			newGkplaceArrangeService.updateBasicPlaceSet(gradeId, all);
		} catch (RuntimeException re) {
			return error(re.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("保存失败！");
		}
		return success("保存成功！");
	}

	@ResponseBody
	@RequestMapping("/goBasic/showBuildName")
	public String showBuildName(String placeIds) {
		try {
			String[] placeIdArr = placeIds.split(",");
			List<TeachPlace> list = teachPlaceRemoteService.findListObjectByIds(placeIdArr);
			StringBuffer result = new StringBuffer();
			if(CollectionUtils.isNotEmpty(list)){
				Set<String> teachBuildingIds =EntityUtils.getSet(list, TeachPlace::getTeachBuildingId);
				Map<String,String> bdMap= SUtils.dt(teachBuildingRemoteService.findTeachBuildMap(teachBuildingIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
				for (TeachPlace one : list) {
					String id = one.getTeachBuildingId();
					if (StringUtils.isNotBlank(id)) {
						result.append(bdMap.get(id) + ",");
					} else {
						result.append("其他楼,");
					}
				}
			}
			return success(result.substring(0, result.length() - 1));
		} catch (RuntimeException re) {
			return error(re.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("保存失败！");
		}
	}
	
	/**
	 * 教师组设置
	 * @param gradeId
	 * @param map
	 * @return
	 */
	@RequestMapping("/goBasic/teacherGroup/index")
	public String setTeacherGroup2(@PathVariable String gradeId, ModelMap map) {
		List<NewGkTeacherGroup> tgList = teacherGroupService.findByObjectId(gradeId, false);
		List<NewGkTeacherGroupEx> initTgExListT = new ArrayList<>();
		List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findByArrayItemIds(new String[] {gradeId}, true);

		if(CollectionUtils.isNotEmpty(tgList)) {
			Map<String, NewGkTeacherGroup> tgIdMap = EntityUtils.getMap(tgList, NewGkTeacherGroup::getId);
			List<NewGkTeacherGroupEx> tgExList = null;
			if(CollectionUtils.isEmpty(initTgExListT)) {
				tgExList = teacherGroupExService.findByTeacherGroupIdIn(tgIdMap.keySet().toArray(new String[0]));
			}else {
				tgExList = initTgExListT;
			}
			
			Set<String> allTids = tgExList.stream().map(e->e.getTeacherId()).collect(Collectors.toSet());
			Map<String, String> tnMap = SUtils.dt(teacherRemoteService.findPartByTeacher(allTids.toArray(new String[0])), new TypeReference<Map<String,String>>() {});
			List<NewGkTeacherGroupEx> delExList = tgExList.stream().filter(e->!tnMap.containsKey(e.getTeacherId())).collect(Collectors.toList());
			tgExList.removeAll(delExList);
			
			if(CollectionUtils.isNotEmpty(delExList)) {
				try {
					teacherGroupExService.deleteAll(delExList.toArray(new NewGkTeacherGroupEx[0]));
				} catch (Exception e1) {
					logger.error("删除未知的教师失败。"+e1.getMessage());
				}
			}
			tgExList.stream().filter(e->tgIdMap.containsKey(e.getTeacherGroupId()))
				.forEach(e->tgIdMap.get(e.getTeacherGroupId()).getTeacherIdSet().add(e.getTeacherId()));
			for (NewGkTeacherGroup tg : tgList) {
				String tns = tg.getTeacherIdSet().stream().filter(e->tnMap.containsKey(e)).map(e->tnMap.get(e)).collect(Collectors.joining(","));
				tg.setTeacherNameStr(tns);
			}
			
			// - 获取禁排时间 ------//
			List<NewGkLessonTime> noTimeList = newGkLessonTimeService.findByItemIdObjectId(gradeId, null, 
					new String[] {NewGkElectiveConstant.LIMIT_GRADE_0, NewGkElectiveConstant.LIMIT_TEACHER_GROUP_6}, true);
			for (NewGkLessonTime lt : noTimeList) {
				if(NewGkElectiveConstant.LIMIT_TEACHER_GROUP_6.equals(lt.getObjectType())) {
					List<NewGkLessonTimeEx> timesList = lt.getTimesList();
					NewGkTeacherGroup tg = tgIdMap.get(lt.getObjectId());
					if(tg != null && CollectionUtils.isNotEmpty(timesList)) {
						String noTimeStr = timesList.stream().filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(e.getTimeType()))
								.map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())
								.collect(Collectors.joining(","));
						tg.setNoTimeStr(noTimeStr);
					}
				}
			}
			
			Map<String, String> noClickTimeMap = noTimeList.stream().filter(e->NewGkElectiveConstant.LIMIT_GRADE_0.equals(e.getObjectType())&&e.getTimesList()!=null)
					.flatMap(e->e.getTimesList().stream())
					.map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())
					.collect(Collectors.toMap(e->e, e->e));
			map.put("noClickTimeMap", noClickTimeMap);
			map.put("tnMap", tnMap);
		}
		
		// 获取 已经选择的所有教师
		List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findListBy("arrayItemId", gradeId);
		Set<String> subjectIds = EntityUtils.getSet(subjectTimeList, e->e.getSubjectId());
		LinkedHashMap<String,String> coumap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<LinkedHashMap<String, String>>(){});
		
		Map<String, List<NewGkTeacherPlanEx>> subjectTeacherPlanExMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(teacherPlanList)){
			Set<String>teacherIdSet = new HashSet<String>();
			for (NewGkTeacherPlan plan : teacherPlanList) {
				subjectIds.add(plan.getSubjectId());
				List<NewGkTeacherPlanEx> planExList = plan.getTeacherPlanExList();
				if (CollectionUtils.isNotEmpty(planExList)) {
					Iterator<NewGkTeacherPlanEx> iter1 = planExList.iterator();
					while (iter1.hasNext()) {
						NewGkTeacherPlanEx ex = iter1.next();
						if (ex == null) {
							iter1.remove();
							continue;
						}
						teacherIdSet.add(ex.getTeacherId());
					}
				}
			}
			
			Map<String, String> teacherNameMap  = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIdSet.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
			for (NewGkTeacherPlan plan : teacherPlanList) {
				List<NewGkTeacherPlanEx> planExList = plan.getTeacherPlanExList();
				if (CollectionUtils.isNotEmpty(planExList)) {
					Iterator<NewGkTeacherPlanEx> iter1 = planExList.iterator();
					while (iter1.hasNext()) {
						NewGkTeacherPlanEx ex = iter1.next();
						if (ex == null) {
							iter1.remove();
							continue;
						}
						String tName = teacherNameMap.get(ex.getTeacherId());
						if (StringUtils.isNotBlank(tName)) {
							ex.setTeacherName(tName);
						} else {
							iter1.remove();
						}
					}
				}
				subjectTeacherPlanExMap.put(plan.getSubjectId(), plan.getTeacherPlanExList());
			}
		}
		
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		Map<String, Integer> piMap = getIntervalMap(grade);

		
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("piMap", piMap);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
		
		map.put("subjectTeacherPlanExMap", subjectTeacherPlanExMap);
		map.put("coumap", coumap); 
		
		map.put("tgList", tgList);
		map.put("gradeId", gradeId);
		
		return "/newgkelective/basic/teacherGroup2List.ftl";
	}

	@RequestMapping("/goBasic/teacherGroup/init")
	@ResponseBody
	public String initTeacherGroup(@PathVariable String gradeId){

		try {
			teacherGroupService.dealTeacherGroupInit(gradeId);
		} catch (Exception e1) {
			logger.error("教师组初始化异常："+e1.getMessage());
			return error(e1.getMessage()+"");
		}

		return returnSuccess();
	}


	/**
	 * 添加 或者 修改 教师组
	 * @param gradeId
	 * @param teacherGroup
	 * @param teacherIds
	 * @return
	 */
	@RequestMapping("/goBasic/teacherGroup/save")
	@ResponseBody
	public String addTeacherGroup(@PathVariable String gradeId, NewGkTeacherGroup teacherGroup, 
			@RequestParam(required=false,name="teacherIds[]") String[] teacherIds) {
		if(teacherGroup == null || StringUtils.isBlank(teacherGroup.getTeacherGroupName())) {
			return error("参数有误，请填写 组合名称");
		}
		
		Date now = new Date();
		boolean isAdd = true;
		if(StringUtils.isBlank(teacherGroup.getId())) {
			String unitId = getLoginInfo().getUnitId();
			teacherGroup.setId(UuidUtils.generateUuid());
			teacherGroup.setCreationTime(now);
			teacherGroup.setObjectId(gradeId);
			teacherGroup.setUnitId(unitId);
		}else {
			isAdd = false;
			NewGkTeacherGroup oldTeacherGroup = teacherGroupService.findOne(teacherGroup.getId());
			oldTeacherGroup.setTeacherGroupName(teacherGroup.getTeacherGroupName());
			teacherGroup = oldTeacherGroup;
		}
		teacherGroup.setModifyTime(now);
		
		
		List<NewGkTeacherGroupEx> tgExList = new ArrayList<>();
		if(teacherIds!= null && teacherIds.length>0) {
			NewGkTeacherGroupEx ex;
			Set<String> tids = new HashSet<>();
			for (String tid : teacherIds) {
				if(tids.contains(tid)) {
					continue;
				}
				ex = new NewGkTeacherGroupEx();
				ex.setId(UuidUtils.generateUuid());
				ex.setTeacherGroupId(teacherGroup.getId());
				ex.setTeacherId(tid);
				tgExList.add(ex);
				tids.add(tid);
			}
		}
		
		
		Set<String> tids = null;
		String[] noTimeArr = null;
		List<NewGkLessonTime> noTimeList=null;
//		Map<String, List<String>> tgTidMap = null;
		if(!isAdd) {
			List<NewGkTeacherGroupEx> oldTgExList = teacherGroupExService.findByTeacherGroupIdIn(new String[] {teacherGroup.getId()});
			Set<String> oldTids = EntityUtils.getSet(oldTgExList, NewGkTeacherGroupEx::getTeacherId);
			noTimeList = newGkLessonTimeService.findByItemIdObjectId(gradeId, new String[] {teacherGroup.getId()}, 
					new String[] {NewGkElectiveConstant.LIMIT_TEACHER_GROUP_6}, true);
			noTimeArr = noTimeList.stream().flatMap(e->e.getTimesList().stream())
					.map(e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())
					.toArray(e->new String[e]);
			tids = Arrays.stream(teacherIds).filter(e->!oldTids.contains(e)).collect(Collectors.toSet());
			
		}
		try {
			teacherGroupService.saveOrUpdate(Arrays.asList(teacherGroup),tgExList);
			
			if(CollectionUtils.isNotEmpty(tids)) {
				saveNoTime(gradeId, teacherGroup.getId(), noTimeArr, tids);
			}
//			if(tgTidMap != null && tgTidMap.size()>0) {
//				teacherGroupService.deleteRelaTeacherTime(tgTidMap, null, noTimeList);
//			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(""+e.getMessage());
		}
		
		return returnSuccess();
	}
	
	/**
	 * 修改教师组 禁排时间；需要应用到 具体的教师 禁排时间
	 * @return
	 */
	@RequestMapping("/goBasic/teacherGroup/saveNoTime")
	@ResponseBody
	public String saveTgNoTime(@PathVariable String gradeId, String teacherGroupId,
			@RequestParam(name="noTimeArr[]",required=false) String[] noTimeArr) {
		
		List<NewGkTeacherGroupEx> tgExList = teacherGroupExService.findByTeacherGroupIdIn(new String[] {teacherGroupId});
		Set<String> tids = EntityUtils.getSet(tgExList, NewGkTeacherGroupEx::getTeacherId);
		List<NewGkTeacherPlanEx> tpExList = newGkTeacherPlanExService.findByTeacherId(gradeId, tids.toArray(new String[0]));
		Set<String> existsTids = EntityUtils.getSet(tpExList, NewGkTeacherPlanEx::getTeacherId);
		tids = existsTids;
		
		
		try {
			saveNoTime(gradeId, teacherGroupId, noTimeArr, tids);
		} catch (Exception e) {
			e.printStackTrace();
			return error(""+e.getMessage());
		}
		
		return returnSuccess();
	}

	private void saveNoTime(String gradeId, String teacherGroupId, String[] noTimeArr, Set<String> teacherIds) {
		Set<String> objectIds = new HashSet<>();
		objectIds.add(teacherGroupId);
		objectIds.addAll(teacherIds);
		List<NewGkLessonTime> savedLessTimeList = newGkLessonTimeService.findByItemIdObjectId(gradeId, objectIds.toArray(new String[0]),
				new String[] {NewGkElectiveConstant.LIMIT_TEACHER_2, NewGkElectiveConstant.LIMIT_TEACHER_GROUP_6}, true);
		Map<String, NewGkLessonTime> ltMap = EntityUtils.getMap(savedLessTimeList, NewGkLessonTime::getObjectId);
		
		Date now = new Date();
		List<NewGkLessonTime> toSaveLtList = new ArrayList<>();
		NewGkLessonTime tgLessonTime = ltMap.get(teacherGroupId);
		if(tgLessonTime == null) {
			tgLessonTime = new NewGkLessonTime();
			tgLessonTime.setId(UuidUtils.generateUuid());
			tgLessonTime.setArrayItemId(gradeId);
			tgLessonTime.setObjectId(teacherGroupId);
			tgLessonTime.setObjectType(NewGkElectiveConstant.LIMIT_TEACHER_GROUP_6);
			tgLessonTime.setIsJoin(NewGkElectiveConstant.IF_INT_1);
			tgLessonTime.setCreationTime(now);
			tgLessonTime.setModifyTime(now);
			tgLessonTime.setGroupType(NewGkElectiveConstant.GROUP_TYPE_1);
			toSaveLtList.add(tgLessonTime);
			ltMap.put(teacherGroupId, tgLessonTime);
		}

		List<NewGkLessonTimeEx> toSaveLteList = new ArrayList<>();
		NewGkLessonTimeEx lte;
		
		if(noTimeArr != null && noTimeArr.length > 0) {
			for (String nt : noTimeArr) {
				String[] split = nt.split("_");
				if(split.length<3) {
					logger.error("时间格式错误："+nt);
					continue;
				}
				// 每一个教师组 的禁排时间
				lte = new NewGkLessonTimeEx();
				lte.setId(UuidUtils.generateUuid());
				lte.setCreationTime(now);
				lte.setModifyTime(now);
				lte.setDayOfWeek(Integer.parseInt(split[0]));
				lte.setPeriodInterval(split[1]);
				lte.setPeriod(Integer.parseInt(split[2]));
				lte.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
				lte.setArrayItemId(gradeId);
				lte.setScourceType(NewGkElectiveConstant.SCOURCE_TEACHER_02);
				lte.setScourceTypeId(ltMap.get(teacherGroupId).getId());
				toSaveLteList.add(lte);
			}
		}
		
		if(noTimeArr == null) {
			noTimeArr = new String[0];
		}
		Set<String> noTimeSet = Arrays.stream(noTimeArr).collect(Collectors.toSet());
		for (String tid : teacherIds) {
			NewGkLessonTime lt = ltMap.get(tid);
			if(lt == null) {
				continue;
			}
			List<NewGkLessonTimeEx> timesList = lt.getTimesList();
			List<NewGkLessonTimeEx> collect = timesList.stream()
					.filter(e->!NewGkElectiveConstant.ARRANGE_TIME_TYPE_05.equals(e.getTimeType())
							&&!noTimeSet.contains(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod()))
					.collect(Collectors.toList());
			toSaveLteList.addAll(collect);
		}
		
		List<String> delSourceTypeIds = EntityUtils.getList(savedLessTimeList, NewGkLessonTime::getId);
		newGkLessonTimeService.saveAutoBatchResult(delSourceTypeIds, toSaveLtList, toSaveLteList, null);
	}
	
	@RequestMapping("/goBasic/teacherGroup/del")
	@ResponseBody
	public String delTeacherGroups(@PathVariable String gradeId, @RequestParam(required=false,name="teacherGroupIds[]") String[] teacherGroupIds) {
		// tg tgex lt lte
		if(teacherGroupIds == null || teacherGroupIds.length <1) {
			return error("请选择要删除的条目");
		}
		
		try {
			teacherGroupService.deleteGroups(gradeId,teacherGroupIds);
		} catch (Exception e) {
			e.printStackTrace();
			return error(""+e.getMessage());
		}
		
		return returnSuccess();
	}
}
