package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.ArrayFeaturesDto;
import net.zdsoft.newgkelective.data.dto.LessonTimeDto;
import net.zdsoft.newgkelective.data.dto.LessonTimeDtoPack;
import net.zdsoft.newgkelective.data.entity.NewGkArray;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassCombineRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.entity.NewGkRelateSubtime;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkArrayService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeExService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;
import net.zdsoft.newgkelective.data.service.NewGkRelateSubtimeService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.newgkelective.data.utils.MyNumberUtils;

/**
 * 
 * @author weixh
 * @since 2018年4月27日 下午2:20:22
 */
@Controller
@RequestMapping("/newgkelective")
public class NewGKGradeArrangeAction extends BaseAction {
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private NewGkLessonTimeService lessonTimeService;
	@Autowired
	private NewGkSubjectTimeService subjectTimeService;
	@Autowired
	private NewGkChoRelationService relationService;
	@Autowired
	private NewGkOpenSubjectService openSubjectService;
	@Autowired
	private NewGkTeacherPlanService teacherPlanService;
	@Autowired
	private NewGkLessonTimeExService newGkLessonTimeExService;
	@Autowired
	private NewGkRelateSubtimeService relateSubtimeService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkArrayService newGkArrayService;
	@Autowired
	private NewGkClassSubjectTimeService newGkClassSubjectTimeService;
	@Autowired
	private NewGkClassCombineRelationService newGkClassCombineRelationService;
	
	@ControllerInfo("年级特征编辑index")
	@RequestMapping("/{divideId}/gradeArrange/index")
	public String index(@PathVariable String divideId, String arrayId, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findById(divideId);
		List<NewGkArrayItem> items = newGkArrayItemService.findByDivideId(divideId, new String[] {NewGkElectiveConstant.ARRANGE_TYPE_04});
		map.put("divideId", divideId);
		map.put("arrayId", arrayId);
		map.put("gradeId", divide.getGradeId());
		map.put("items", items);
		
		//页面展现
		//固定时间点 不排课时间点 各科目老师数量
		newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_04, divide.getGradeId(), items);
		return "/newgkelective/gradeArrange/gradeArrangeList.ftl";
	}
	
	@ControllerInfo("排课特征编辑index")
	@RequestMapping("/{divideId}/gradeArrange/edit")
	public String infoIndex(@PathVariable String divideId, String fromSolve, String arrayId, String arrayItemId, HttpServletRequest request, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findById(divideId);
		map.put("fromSolve", fromSolve);
		map.put("divideId", divideId);
		map.put("arrayId", arrayId);
		map.put("gradeId", divide.getGradeId());

		Grade grade = gradeRemoteService.findOneObjectById(divide.getGradeId());
		if(grade.getWeekDays() == null){
			return errorFtl(map,"请先设置周天数");
		}

		if(StringUtils.isEmpty(arrayItemId)) {
			// 默认新增
			arrayItemId = defaultSaveTime(divide);
			//根据基础条件初始化教师特征安排和课程特征安排
			initGradeBasicSet(arrayItemId,divide);
		}
		map.put("arrayItemId", StringUtils.trimToEmpty(arrayItemId));
		String toLi = request.getParameter("toLi");
		map.put("toLi", toLi);
		map.put("lessArrayId", StringUtils.trimToEmpty(request.getParameter("lessArrayId")));
		map.put("plArrayId", StringUtils.trimToEmpty(request.getParameter("plArrayId")));
		return "/newgkelective/gradeArrange/arrangeEditIndex.ftl";
	}

	/**
	 * 根据基础条件初始化教师特征安排和课程特征安排
	 * @param arrayItemId
	 * @param gradeId
	 */
	private void initGradeBasicSet(String arrayItemId, NewGkDivide divide) {
		String gradeId = divide.getGradeId();
		//暂时只有AB
		List<NewGkOpenSubject> openSubjectList = openSubjectService.findByDivideId(divide.getId());
		
		
		List<NewGkLessonTime> lessonTimeList = new ArrayList<NewGkLessonTime>();//包括教师和课程
		List<NewGkLessonTimeEx> timeExList = new ArrayList<NewGkLessonTimeEx>();//包括教师和课程
		
		//课程特征
 		List<NewGkSubjectTime> gradeSujectTimeList = subjectTimeService.findByArrayItemId(gradeId);
		
		Set<String> addSubject=new HashSet<>();//新增的所有科目数据
 		Set<String> arrangeSubList=new HashSet<>();//新增的所有科目数据
 		Map<String,NewGkSubjectTime> subjectTimeMap = new HashMap<String, NewGkSubjectTime>();
		if(CollectionUtils.isNotEmpty(gradeSujectTimeList)){
			for (NewGkSubjectTime subjectTime : gradeSujectTimeList) {
				subjectTimeMap.put(subjectTime.getSubjectId()+"-"+subjectTime.getSubjectType(), subjectTime);
			}
			
			
			List<NewGkSubjectTime> subjectTimeList = makJxbSubjectTime(arrayItemId, openSubjectList, addSubject,
					arrangeSubList, subjectTimeMap);
			
			//剩余行政班科目
			for (NewGkSubjectTime subjectTime : gradeSujectTimeList) {
				if(arrangeSubList.contains(subjectTime.getSubjectId())) {
					continue;
				}
				if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(subjectTime.getSubjectType())) {
					//将原来的对象直接修改
					subjectTime.setId(UuidUtils.generateUuid());
					subjectTime.setArrayItemId(arrayItemId);
					subjectTime.setCreationTime(new Date());
					subjectTime.setModifyTime(new Date());
					
					if(StringUtils.isNotBlank(subjectTime.getFirstsdWeekSubject())&&!subjectTimeMap.containsKey(subjectTime.getFirstsdWeekSubject())){
						subjectTime.setFirstsdWeek(NewGkElectiveConstant.WEEK_TYPE_NORMAL);
						subjectTime.setFirstsdWeekSubject(null);
					}
					subjectTimeList.add(subjectTime);
					addSubject.add(subjectTime.getSubjectId()+"-"+subjectTime.getSubjectType());
					arrangeSubList.add(subjectTime.getSubjectId());
				}
			}
			
			
			
			if(CollectionUtils.isNotEmpty(subjectTimeList)){
				subjectTimeService.saveAll(subjectTimeList.toArray(new NewGkSubjectTime[0]));
			}
			
			List<NewGkRelateSubtime> gradeRelateSubtimeList = relateSubtimeService.findListByItemId(gradeId);
			if (CollectionUtils.isNotEmpty(gradeRelateSubtimeList)) {
				List<NewGkRelateSubtime> relateSubtimeList = new ArrayList<NewGkRelateSubtime>();
				for (NewGkRelateSubtime n : gradeRelateSubtimeList) {
					String[] arr = n.getSubjectIds().split(",");
					Boolean f=false;
					
					for (String a : arr) {
						if(!addSubject.contains(a)){
							f=true;
							break;
						}
					}
					if(!f) {
						//将原来的对象直接修改
						n.setId(UuidUtils.generateUuid());
						n.setItemId(arrayItemId);
						n.setCreationTime(new Date());
						n.setModifyTime(new Date());
						relateSubtimeList.add(n);
					}
					
				}
				if(CollectionUtils.isNotEmpty(relateSubtimeList)){
					relateSubtimeService.saveAll(relateSubtimeList.toArray(new NewGkRelateSubtime[0]));
				}
			}
			List<NewGkLessonTime> courseLessonTimeList = lessonTimeService.findByItemIdObjectId(gradeId,
					null, new String[] { NewGkElectiveConstant.LIMIT_SUBJECT_9 },true);
			if(CollectionUtils.isNotEmpty(courseLessonTimeList)){
				Map<String,String> oldToNewId = new HashMap<String, String>();
				for (NewGkLessonTime lessonTime : courseLessonTimeList) {
					if(addSubject.contains(lessonTime.getObjectId()+"-"+lessonTime.getLevelType())){
						String newId = UuidUtils.generateUuid();
						oldToNewId.put(lessonTime.getId(), newId);
						lessonTime.setId(newId);
						lessonTime.setArrayItemId(arrayItemId);
						lessonTime.setCreationTime(new Date());
						lessonTime.setModifyTime(new Date());
						lessonTimeList.add(lessonTime);
						if(CollectionUtils.isNotEmpty(lessonTime.getTimesList())){
							for (NewGkLessonTimeEx lessonTimeEx : lessonTime.getTimesList()) {
								lessonTimeEx.setArrayItemId(arrayItemId);
								lessonTimeEx.setCreationTime(new Date());
								lessonTimeEx.setId(UuidUtils.generateUuid());
								lessonTimeEx.setScourceTypeId(newId);
								timeExList.add(lessonTimeEx);
							}
						}
					}
				}
			}
		}else {
			List<NewGkSubjectTime> subjectTimeList = makJxbSubjectTime(arrayItemId, openSubjectList, addSubject,
					arrangeSubList, subjectTimeMap);
			if(CollectionUtils.isNotEmpty(subjectTimeList)){
				subjectTimeService.saveAll(subjectTimeList.toArray(new NewGkSubjectTime[0]));
			}
		}
		
		
		//教师特征
//		List<String> subjectIdList = EntityUtils.getList(openSubjectList, NewGkOpenSubject::getSubjectId);
		List<NewGkTeacherPlan> teacherPlanList = teacherPlanService.findByArrayItemIdAndSubjectIdIn(gradeId, arrangeSubList.toArray(new String[0]), true);
		if(CollectionUtils.isNotEmpty(teacherPlanList)){
			Set<String> teacherIdSet = new HashSet<String>();
			List<NewGkTeacherPlanEx> planExList = new ArrayList<NewGkTeacherPlanEx>();
			for (NewGkTeacherPlan plan : teacherPlanList) {
				String teacherPlanId = UuidUtils.generateUuid();
				plan.setId(teacherPlanId);
				plan.setCreationTime(new Date());
				plan.setModifyTime(new Date());
				plan.setArrayItemId(arrayItemId);
				if(CollectionUtils.isNotEmpty(plan.getTeacherPlanExList())){
					for (NewGkTeacherPlanEx planEx : plan.getTeacherPlanExList()) {
						planEx.setId(UuidUtils.generateUuid());
						planEx.setTeacherPlanId(teacherPlanId);
						planEx.setCreationTime(new Date());
						planEx.setModifyTime(new Date());
						planExList.add(planEx);
					}
				}
				teacherIdSet.addAll(plan.getExTeacherIdList());
			}
			teacherPlanService.saveList(teacherPlanList, planExList);
			List<NewGkLessonTime> teacherLessonTimeList = lessonTimeService.findByItemIdObjectId(gradeId, teacherIdSet.toArray(new String[0]), new String[] {NewGkElectiveConstant.LIMIT_TEACHER_2}, true);
			if(CollectionUtils.isNotEmpty(teacherLessonTimeList)){
				Map<String,String> oldToNewId = new HashMap<String, String>();
				for (NewGkLessonTime lessonTime : teacherLessonTimeList) {
					String newId = UuidUtils.generateUuid();
					oldToNewId.put(lessonTime.getId(), newId);
					lessonTime.setId(newId);
					lessonTime.setArrayItemId(arrayItemId);
					lessonTime.setCreationTime(new Date());
					lessonTime.setModifyTime(new Date());
					if(CollectionUtils.isNotEmpty(lessonTime.getTimesList())){
						for (NewGkLessonTimeEx lessonTimeEx : lessonTime.getTimesList()) {
							lessonTimeEx.setArrayItemId(arrayItemId);
							lessonTimeEx.setCreationTime(new Date());
							lessonTimeEx.setId(UuidUtils.generateUuid());
							lessonTimeEx.setScourceTypeId(newId);
							timeExList.add(lessonTimeEx);
						}
					}
				}
				lessonTimeList.addAll(teacherLessonTimeList);
				List<NewGkChoRelation> reList = relationService.findByChoiceIdsAndObjectType(getLoginInfo().getUnitId(), oldToNewId.keySet().toArray(new String[0]), NewGkElectiveConstant.CHOICE_TYPE_07);
				if(CollectionUtils.isNotEmpty(reList)){
					for (NewGkChoRelation re : reList) {
						re.setId(UuidUtils.generateUuid());
						re.setCreationTime(new Date());
						re.setModifyTime(new Date());
						re.setChoiceId(oldToNewId.get(re.getChoiceId()));
					}
					relationService.saveAll(reList.toArray(new NewGkChoRelation[0]));
				}
			}
		}
		
		
		lessonTimeService.saveCopyLessonTime(arrayItemId, null, lessonTimeList, timeExList, null, null);
	}

	private List<NewGkSubjectTime> makJxbSubjectTime(String arrayItemId, List<NewGkOpenSubject> openSubjectList,
			Set<String> addSubject, Set<String> arrangeSubList, Map<String, NewGkSubjectTime> subjectTimeMap) {
		List<NewGkSubjectTime> subjectTimeList = new ArrayList<NewGkSubjectTime>();
		for (NewGkOpenSubject openSubject : openSubjectList) {
			NewGkSubjectTime subjectTime = subjectTimeMap.get(openSubject.getSubjectId()+"-"+openSubject.getSubjectType());
			arrangeSubList.add(openSubject.getSubjectId());
			if(subjectTime!=null){
				//将原来的对象直接修改
				subjectTime.setId(UuidUtils.generateUuid());
				subjectTime.setArrayItemId(arrayItemId);
				subjectTime.setCreationTime(new Date());
				subjectTime.setModifyTime(new Date());
				if(StringUtils.isNotBlank(subjectTime.getFirstsdWeekSubject())&&!subjectTimeMap.containsKey(subjectTime.getFirstsdWeekSubject())){
					subjectTime.setFirstsdWeek(NewGkElectiveConstant.WEEK_TYPE_NORMAL);
					subjectTime.setFirstsdWeekSubject(null);
				}
				subjectTimeList.add(subjectTime);
			}else {
				//未找到新增
				subjectTime=new NewGkSubjectTime();
				subjectTime.setId(UuidUtils.generateUuid());
				subjectTime.setArrayItemId(arrayItemId);
				subjectTime.setCreationTime(new Date());
				subjectTime.setModifyTime(new Date());
				subjectTime.setPeriod(0);
				subjectTime.setIsNeed(1);
				//subjectTime.setPunchCard(1);
				subjectTime.setSubjectId(openSubject.getSubjectId());
				subjectTime.setSubjectType(openSubject.getSubjectType());
				subjectTimeList.add(subjectTime);
			}
			addSubject.add(openSubject.getSubjectId()+"-"+openSubject.getSubjectType());
		}
		return subjectTimeList;
	}

	/**
	 * 新增排课特征时，年级特征默认取基础条件中设置的，没有则添加周末不排课时间
	 * @param divide
	 * @return
	 */
	private String defaultSaveTime(NewGkDivide divide) {
		try {
			LessonTimeDtoPack pack = new LessonTimeDtoPack();
			pack.setObjType(NewGkElectiveConstant.LIMIT_GRADE_0);
			pack.setNeedSource(true);
			List<LessonTimeDto> sources = new ArrayList<LessonTimeDto>();
			List<LessonTimeDto> lds = new ArrayList<LessonTimeDto>(); 
			// 基础条件-年级特征 TODO
			List<NewGkLessonTime> basicTimes = newGkLessonTimeService.findByItemIdObjectIdAndGroupType(divide.getGradeId(), 
					null, new String[] {NewGkElectiveConstant.LIMIT_GRADE_0}, NewGkElectiveConstant.DIVIDE_GROUP_1);
			if(CollectionUtils.isNotEmpty(basicTimes)) {
				List<String> timeIds = EntityUtils.getList(basicTimes, NewGkLessonTime::getId);
				List<NewGkLessonTimeEx> exs = newGkLessonTimeExService.findByObjectId(timeIds.toArray(new String[0]), new String[] {NewGkElectiveConstant.SCOURCE_LESSON_01});
				Map<String, String> objSid = new HashMap<String, String>();
				for(NewGkLessonTime time : basicTimes) {
					LessonTimeDto sd = new LessonTimeDto();
					sd.setGroupType(time.getGroupType());
					sd.setIs_join(0);
					sd.setObjId(time.getObjectId());
					sources.add(sd);
					objSid.put(time.getId(), time.getObjectId());
				}
				if(CollectionUtils.isNotEmpty(exs)) {
					for(NewGkLessonTimeEx ex : exs) {
						if(!objSid.containsKey(ex.getScourceTypeId())) {
							continue;
						}
						LessonTimeDto dto = new LessonTimeDto();
						dto.setObjId(objSid.get(ex.getScourceTypeId()));
						dto.setWeekday(ex.getDayOfWeek());
						dto.setPeriod_interval(ex.getPeriodInterval());
						dto.setPeriod(ex.getPeriod());
						dto.setIs_join(0);
						dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
						dto.setTimeType(ex.getTimeType());
						lds.add(dto);
					}
				}
			} else {// 基础条件没有设置，默认周末2天不排课
				Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);	
				List<String> msList = MyNumberUtils.getNumList(grade.getMornPeriods());
				List<String> amList = MyNumberUtils.getNumList(grade.getAmLessonCount());
				List<String> pmList = MyNumberUtils.getNumList(grade.getPmLessonCount());
				List<String> nightList = MyNumberUtils.getNumList(grade.getNightLessonCount());
				
				LessonTimeDto sd = new LessonTimeDto();
				sd.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
				sd.setIs_join(0);
				sd.setObjId(divide.getGradeId());
				sd.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
				sources.add(sd);
				
				int[] wds = {5,6};// 周六、日
				if(CollectionUtils.isNotEmpty(msList)) {
					for(String ms : msList) {
						for (int wd : wds) {
							LessonTimeDto dto = new LessonTimeDto();
							dto.setObjId(divide.getGradeId());
							dto.setWeekday(wd);
							dto.setPeriod_interval(BaseConstants.PERIOD_INTERVAL_1);
							dto.setPeriod(NumberUtils.toInt(ms));
							dto.setIs_join(0);
							dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
							dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
							lds.add(dto);
						}
					}
				}
				if(CollectionUtils.isNotEmpty(amList)) {
					for(String am : amList) {
						for (int wd : wds) {
							LessonTimeDto dto = new LessonTimeDto();
							dto.setObjId(divide.getGradeId());
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
							dto.setObjId(divide.getGradeId());
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
							dto.setObjId(divide.getGradeId());
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
			}
			pack.setSourceTimeDto(sources);
			pack.setLessonTimeDto(lds);
			return newGkLessonTimeExService.addLessonTimeTable(pack, divide.getId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	@ControllerInfo("年级特征编辑")
	@RequestMapping("/{divideId}/gradeSet/edit")
	public String gradeSetEdit(@PathVariable String divideId, String arrayItemId, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);
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
		map.put("gradeId", divide.getGradeId());
		map.put("divideId", divideId);
		map.put("objectType", NewGkElectiveConstant.LIMIT_GRADE_0);
		map.put("arrayItemId", arrayItemId);
		map.put("fixGuid", BaseConstants.ZERO_GUID);
		
		List<Course> subs = SUtils.dt(courseRemoteService.getListByCondition(this.getLoginInfo().getUnitId(), null, 
				   null,BaseConstants.ZERO_GUID,null, 1, null), new TR<List<Course>>() {});
		map.put("subs", subs);
		
		Map<String, Integer> piMap = getIntervalMap(grade);
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("piMap", piMap);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
		
		return "/newgkelective/gradeArrange/gradeSetEdit.ftl";
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
	@ControllerInfo("删除排课特征")
	@RequestMapping("/{arrayItemId}/gradeArrange/delete")
	public String delArrange(@PathVariable String arrayItemId, String gradeId) {
		try {
			 //是否已经被引用
			 List<NewGkArray> newGkArrayList =  newGkArrayService.findListBy("lessonArrangeId", arrayItemId);
			 boolean hasRefer = newGkArrayList.stream().anyMatch(e->Integer.valueOf(NewGkElectiveConstant.IF_INT_0).equals(e.getIsDeleted()));
			 if(hasRefer){
				 return error("该排课特征被引用，不能删除！");
			 }
			newGkArrayItemService.deleteById(this.getLoginInfo().getUnitId(), arrayItemId);
		} catch (Exception e) {
			return error("删除失败！");
		}
		return success("删除成功！");
	}
	
	
	@ResponseBody
	@ControllerInfo("arrayItem名称编辑")
	@RequestMapping("/{arrayItemId}/gradeArrange/changName")
	public String saveArrayFeatureName(@PathVariable String arrayItemId, String itemName) {
		try {
			
			NewGkArrayItem item = newGkArrayItemService.findOne(arrayItemId);
			if(item == null) {
				return error("获取不到相应记录，请确定页面是否过期");
			}
			List<String> aids = newGkArrayItemService.findIdsByDivideIdName(item.getDivideId(), item.getDivideType(), itemName);
			if(CollectionUtils.isNotEmpty(aids)) {
				aids.remove(arrayItemId);
			}
			if(CollectionUtils.isNotEmpty(aids)) {
				return error("名称和其它记录重复");
			}
			item.setItemName(itemName);
			newGkArrayItemService.save(item);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		
		return success("操作成功！");
	}
	
	@ResponseBody
	@ControllerInfo("复制课程特征")
	@RequestMapping("/{arrayItemId}/gradeArrange/copyFeature")
	public String copyFeature(@PathVariable String arrayItemId) {
		if(StringUtils.isBlank(arrayItemId)) {
			return error("找不到当前排课特征，请刷新页面");
		}
		String unitId = getLoginInfo().getUnitId();
		
		NewGkArrayItem srcItem = newGkArrayItemService.findOne(arrayItemId);
		NewGkDivide divide = newGkDivideService.findById(srcItem.getDivideId());
		
		Integer times = getMaxFeatureTimes(divide);
		++times;
		String itemName = makeItemName(divide, times);
//		itemName += "--复制于 排课特征"+srcItem.getTimes();
		
		
		Date now = new Date();
		// 1.复制  arrayItem
		NewGkArrayItem copyItem = copyArrayItem(divide.getId(), times, itemName);
		
		// 2.0. 复制班级特征
		List<NewGkClassSubjectTime> allClasSubjTimes = newGkClassSubjectTimeService.findByArrayItemIdAndClassIdIn(unitId, arrayItemId, null, null, null);
		NewGkClassSubjectTime newCst = null;
		List<NewGkClassSubjectTime> newClasSubjTimes = new ArrayList<>();
		Map<String,String> oldIdNewCstMap = new HashMap<>();
		for (NewGkClassSubjectTime oldCst : allClasSubjTimes) {
			newCst = new NewGkClassSubjectTime();
			
			newCst.setId(UuidUtils.generateUuid());
			newCst.setArrayItemId(copyItem.getId());
			newCst.setCreationTime(now);
			newCst.setModifyTime(now);
			
			newCst.setUnitId(oldCst.getUnitId());
			newCst.setClassId(oldCst.getClassId());
			newCst.setSubjectId(oldCst.getSubjectId());
			newCst.setSubjectType(oldCst.getSubjectType());
			newCst.setPeriod(oldCst.getPeriod());
			
			newClasSubjTimes.add(newCst);
			oldIdNewCstMap.put(oldCst.getId(), newCst.getId());
		}
		
		// 2.1. 复制时间点相关数据
		List<NewGkLessonTime> lessonTimeList = lessonTimeService.findByItemIdObjectId(arrayItemId, null,
				new String[] {NewGkElectiveConstant.LIMIT_GRADE_0,NewGkElectiveConstant.LIMIT_TEACHER_2, NewGkElectiveConstant.LIMIT_SUBJECT_9, 
						NewGkElectiveConstant.LIMIT_SUBJECT_7,NewGkElectiveConstant.LIMIT_SUBJECT_5}, true);
		List<NewGkLessonTime> copyLessonTimeList = new ArrayList<>();
		List<NewGkLessonTimeEx> copyLessonTimeExList = new ArrayList<>();
		Map<String, String> lessonTimeIdMap = new HashMap<>();
		NewGkLessonTime lessonTime;
		NewGkLessonTimeEx lessonTimeEx;
		for (NewGkLessonTime oldTime : lessonTimeList) {
			lessonTime = copyLessonTime(copyItem, oldTime);
			if(NewGkElectiveConstant.LIMIT_SUBJECT_5.equals(oldTime.getObjectType())) {
				if(oldIdNewCstMap.containsKey(oldTime.getObjectId())) {
					lessonTime.setObjectId(oldIdNewCstMap.get(oldTime.getObjectId()));
				}else {
					return error("lessonTime_ObjectType-5,根据objectId:"+oldTime.getObjectId()+"无法获取对应新id");
				}
			}
			
			copyLessonTimeList.add(lessonTime);
			// Ex
			List<NewGkLessonTimeEx> timesList = oldTime.getTimesList();
			for (NewGkLessonTimeEx oldTimeEx : timesList) {
				lessonTimeEx = copyLessonTimeEx(lessonTime, oldTimeEx);
				
				copyLessonTimeExList.add(lessonTimeEx);
			}
			lessonTimeIdMap.put(oldTime.getId(), lessonTime.getId());
		}
		
		// 2.2 复制合班、同时排课数据
		List<NewGkClassCombineRelation> combineList = newGkClassCombineRelationService.findByArrayItemId(unitId, arrayItemId);
		List<NewGkClassCombineRelation> newCombineList = new ArrayList<>();
		NewGkClassCombineRelation newComb = null;
		for (NewGkClassCombineRelation old : combineList) {
			newComb = new NewGkClassCombineRelation();
			
			newComb.setId(UuidUtils.generateUuid());
			newComb.setArrayItemId(copyItem.getId());
			newComb.setCreationTime(now);
			newComb.setModifyTime(now);
			
			newComb.setUnitId(old.getUnitId());
			newComb.setType(old.getType());
			newComb.setClassSubjectIds(old.getClassSubjectIds());
			
			newCombineList.add(newComb);
		}
		
		
		// 3.  复制课程特征
		List<NewGkSubjectTime> subjectTimeList = subjectTimeService.findByArrayItemId(arrayItemId);
		List<NewGkSubjectTime> copySubjectTimes = new ArrayList<>();
		Map<String, String> subjectTimeIdMap = new HashMap<>();
		NewGkSubjectTime subjTime;
		for (NewGkSubjectTime oldSubjectTime : subjectTimeList) {
			subjTime = copySubjectTime(copyItem, oldSubjectTime);
			
			copySubjectTimes.add(subjTime);
			subjectTimeIdMap.put(oldSubjectTime.getId(), subjTime.getId());
		}
		
		
		// 4. 不联排科目
		List<NewGkRelateSubtime> relateSubTimeList = relateSubtimeService.findListByItemId(arrayItemId);
		List<NewGkRelateSubtime> newRelateSubTimeList = new ArrayList<>();
		NewGkRelateSubtime newRela = null;
		for (NewGkRelateSubtime old : relateSubTimeList) {
			newRela = new NewGkRelateSubtime();
			
			newRela.setId(UuidUtils.generateUuid());
			newRela.setCreationTime(new Date());
			newRela.setModifyTime(new Date());
			newRela.setItemId(copyItem.getId());
			newRela.setSubjectIds(old.getSubjectIds());
			newRela.setType(old.getType());
			
			newRelateSubTimeList.add(newRela);
		}
		
		// 5.教师信息 复制 teacherplan teacherPlaneEX
		List<NewGkTeacherPlan> teacherPlanList = teacherPlanService.findByArrayItemIds(new String[] {arrayItemId}, true);
		List<NewGkTeacherPlan> newTpList = new ArrayList<>();
		List<NewGkTeacherPlanEx> newTpExList = new ArrayList<>();
		NewGkTeacherPlan tp ;
		NewGkTeacherPlanEx ex;
		for (NewGkTeacherPlan oldTp : teacherPlanList) {
			tp = new NewGkTeacherPlan();
			tp.setId(UuidUtils.generateUuid());
			tp.setArrayItemId(copyItem.getId());
			tp.setCreationTime(new Date());
			tp.setModifyTime(new Date());
			
			tp.setSubjectId(oldTp.getSubjectId());
			tp.setClassNum(oldTp.getClassNum());
			
			newTpList.add(tp);
			
			List<NewGkTeacherPlanEx> oldExList = oldTp.getTeacherPlanExList();
			for (NewGkTeacherPlanEx oldEx : oldExList) {
				ex = new NewGkTeacherPlanEx();
				ex.setId(UuidUtils.generateUuid());
				ex.setCreationTime(new Date());
				ex.setModifyTime(new Date());
				ex.setTeacherPlanId(tp.getId());
				
				ex.setMutexNum(oldEx.getMutexNum());
				ex.setClassIds(oldEx.getClassIds());
				ex.setTeacherId(oldEx.getTeacherId());
				ex.setWeekPeriodType(oldEx.getDayPeriodType());
				ex.setDayPeriodType(oldEx.getDayPeriodType());
				
				newTpExList.add(ex);
			}
		}
		
		//5.1 互斥教师
		List<String> teacherTimeIds = lessonTimeList.stream()
				.filter(e->NewGkElectiveConstant.LIMIT_TEACHER_2.equals(e.getObjectType()))
				.map(e->e.getId()).collect(Collectors.toList());
		List<NewGkChoRelation> relationList = relationService.findByChoiceIdsAndObjectType(divide.getUnitId(), 
				teacherTimeIds.toArray(new String[] {}), NewGkElectiveConstant.CHOICE_TYPE_07);
		List<NewGkChoRelation> copyRelationList = new ArrayList<>();
		//		List<NewGkChoRelation> copyRelationList = new  ArrayList<>();
//		NewGkChoRelation relation;
		for (NewGkChoRelation newGkChoRelation : relationList) {
			if(lessonTimeIdMap.get(newGkChoRelation.getChoiceId()) == null) {
				continue;
			}
			
			NewGkChoRelation relation = new NewGkChoRelation();
			relation.setId(UuidUtils.generateUuid());
			relation.setChoiceId(lessonTimeIdMap.get(newGkChoRelation.getChoiceId()));
			relation.setCreationTime(new Date());
			relation.setModifyTime(new Date());
			relation.setUnitId(divide.getUnitId());
			relation.setObjectType(newGkChoRelation.getObjectType());
			relation.setObjectValue(newGkChoRelation.getObjectValue());
			
			copyRelationList.add(relation);
		}
		
		
		ArrayFeaturesDto dto = new ArrayFeaturesDto();
		dto.setArrayItem(copyItem);
		dto.setLessonTimeList(copyLessonTimeList);
		dto.setLessonTimeExList(copyLessonTimeExList);
		dto.setSubjectTimes(copySubjectTimes);
		dto.setRelationList(copyRelationList);
		dto.setTeacherPlanList(newTpList);
		dto.setTeacherPlanExList(newTpExList);
		dto.setRelateSubTimeList(newRelateSubTimeList);
		dto.setNewClasSubjTimes(newClasSubjTimes);
		dto.setNewCombineList(newCombineList);
		
		try {
			newGkArrayItemService.saveCopyArrayFeature(dto);
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage());
		}
		
		return success("操作成功！");
	}
	
	@ControllerInfo("显示所有分班方案")
	@RequestMapping("/{divideId}/showDivedes/page")
	public String showDivides(@PathVariable String divideId, ModelMap map) {
		// 本次分班方案
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		if(divide == null) {
			return errorFtl(map, "找不到当前分班方案");
		}
		
		// 年级下所有分班方案
		List<NewGkDivide> divideList = newGkDivideService.findByGradeId(divide.getUnitId(), divide.getGradeId(), NewGkElectiveConstant.IF_1);
		
		Set<String> wlSet = new HashSet<>();
//		wlSet.add(NewGkElectiveConstant.DIVIDE_TYPE_03);
//		wlSet.add(NewGkElectiveConstant.DIVIDE_TYPE_04);
		
		boolean isWl = wlSet.contains(divide.getOpenType());
		
		if(isWl) {
			divideList = divideList.stream().filter(e->e.getOpenType().equals(divide.getOpenType())).collect(Collectors.toList());
		}else {
			divideList = divideList.stream().filter(e->wlSet.contains(e.getOpenType()) == isWl).collect(Collectors.toList());
		}
		// 过滤掉本次分班方案
		divideList = divideList.stream().filter(e->!divideId.equals(e.getId())).collect(Collectors.toList());
		
		map.put("divideList", divideList);
		
		return "/newgkelective/arrayItem/allDivides.ftl";
	}
	
	@ControllerInfo("显示其他分班方案的所有排课特征")
	@RequestMapping("/{divideId}/showItherFeatures/page")
	public String showFeatures(@PathVariable String divideId, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		// 显示所有分班方案 以及 对应的排课特征
		
		// 本次分班方案
		List<NewGkArrayItem> itemList = newGkArrayItemService.findByDivideId(divideId,
				new String[] {NewGkElectiveConstant.ARRANGE_TYPE_04});
		newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_04,divide.getGradeId(), itemList);
		
		map.put("itemList", itemList);
		
		return "/newgkelective/arrayItem/allFeatures.ftl";
	}

	@ResponseBody
	@RequestMapping("/{divideId}/copyFeatureFromOthers")
	@Deprecated
	public String copyFromOthers(@PathVariable String divideId, String arrayItemId) {
		if(StringUtils.isBlank(arrayItemId)) {
			return error("未选中排课特征");
		}
		
		NewGkArrayItem srcItem = newGkArrayItemService.findOne(arrayItemId);
		// 被复制的 排课特征所属分班方案
		NewGkDivide srcDivide = newGkDivideService.findById(srcItem.getDivideId());
		// 本次分班方案
		NewGkDivide divide = newGkDivideService.findById(divideId);
		
		Integer times = getMaxFeatureTimes(divide);
		++times;
		String itemName = makeItemName(divide, times);
		itemName += "--复制于排课特征"+srcItem.getTimes()+" @分班方案"+srcDivide.getTimes();
		
//		Set<String> wlSet = new HashSet<>();
//		wlSet.add(NewGkElectiveConstant.DIVIDE_TYPE_03);
//		wlSet.add(NewGkElectiveConstant.DIVIDE_TYPE_04);
//		boolean isWl = wlSet.contains(divide.getOpenType());
		
		// 1.
		NewGkArrayItem copyItem = copyArrayItem(divideId, times, itemName);
		
		//2.
		//获取分班开设科目
		List<NewGkOpenSubject> openSubjectList = openSubjectService.findByDivideId(divideId);
		Set<String> codeSet = openSubjectList.stream().map(e->e.getSubjectId()+""+e.getSubjectType()).collect(Collectors.toSet());
//		if(isWl) {
//			codeSet = openSubjectList.stream().map(e->e.getSubjectId()+""+e.getSubjectType()+e.getGroupType()).collect(Collectors.toSet());
//		}
		
		List<NewGkSubjectTime> subjectTimeList = subjectTimeService.findByArrayItemId(arrayItemId);
//		Map<String, String> subjectTimeGroup = EntityUtils.getMap(subjectTimeList, NewGkSubjectTime::getId,NewGkSubjectTime::getGroupType);
		List<NewGkSubjectTime> copySubjectTimes = new ArrayList<>();
		Map<String, String> subjectTimeIdMap = new HashMap<>();
		NewGkSubjectTime subjTime;
		String code;
		for (NewGkSubjectTime oldSubjectTime : subjectTimeList) {
//			if(isWl) {
//				code = oldSubjectTime.getSubjectId()+""+oldSubjectTime.getSubjectType()+oldSubjectTime.getGroupType();
//			}else {
				code = oldSubjectTime.getSubjectId()+""+oldSubjectTime.getSubjectType();
//			}
			if(!codeSet.contains(code)) {
				continue;
			}
			
			subjTime = copySubjectTime(copyItem, oldSubjectTime);
			
			copySubjectTimes.add(subjTime);
			subjectTimeIdMap.put(oldSubjectTime.getId(), subjTime.getId());
		}
		
		
		// 3.
		List<NewGkLessonTime> lessonTimeList = lessonTimeService.findByItemIdObjectId(arrayItemId, null,
				new String[] {NewGkElectiveConstant.LIMIT_GRADE_0,NewGkElectiveConstant.LIMIT_SUBJECT_9}, true);
		
		List<NewGkLessonTime> copyLessonTimeList = new ArrayList<>();
		List<NewGkLessonTimeEx> copyLessonTimeExList = new ArrayList<>();
		NewGkLessonTime lessonTime;
		NewGkLessonTimeEx lessonTimeEx;
		for (NewGkLessonTime oldTime : lessonTimeList) {
//			if(isWl) {
//				code = oldTime.getObjectId()+""+oldTime.getLevelType() + oldTime.getGroupType();
//			}else {
				code = oldTime.getObjectId()+""+oldTime.getLevelType();
//			}
			if(NewGkElectiveConstant.LIMIT_SUBJECT_9.equals(oldTime.getObjectType()) 
					&& !codeSet.contains(code)) {
				continue;
			}
			
			lessonTime = copyLessonTime(copyItem, oldTime);
			
			copyLessonTimeList.add(lessonTime);
			// Ex
			List<NewGkLessonTimeEx> timesList = oldTime.getTimesList();
			for (NewGkLessonTimeEx oldTimeEx : timesList) {
				lessonTimeEx = copyLessonTimeEx(lessonTime, oldTimeEx);
				
				copyLessonTimeExList.add(lessonTimeEx);
			}
		}
		
		// 4.
		Set<String> choiceIds = subjectTimeIdMap.keySet();
		List<NewGkChoRelation> relationList = relationService.findByChoiceIdsAndObjectType(divide.getUnitId(), 
				choiceIds.toArray(new String[] {}), NewGkElectiveConstant.CHOICE_TYPE_08);
		List<NewGkChoRelation> copyRelationList = new  ArrayList<>();
		NewGkChoRelation relation;
		String groupType = "";
		for (NewGkChoRelation newGkChoRelation : relationList) {
//			if(isWl) {
//				groupType = Optional.ofNullable(subjectTimeGroup.get(newGkChoRelation.getChoiceId())).orElse("");
//			}
			if(subjectTimeIdMap.get(newGkChoRelation.getChoiceId()) == null || !codeSet.contains(newGkChoRelation.getObjectValue()+groupType)) {
				continue;
			}
			
			relation = new NewGkChoRelation();
			relation.setId(UuidUtils.generateUuid());
			relation.setChoiceId(subjectTimeIdMap.get(newGkChoRelation.getChoiceId()));
			relation.setCreationTime(new Date());
			relation.setModifyTime(new Date());
			relation.setUnitId(divide.getUnitId());
			relation.setObjectType(newGkChoRelation.getObjectType());
			relation.setObjectValue(newGkChoRelation.getObjectValue());
			
			copyRelationList.add(relation);
		}
		
		ArrayFeaturesDto dto = new ArrayFeaturesDto();
		dto.setArrayItem(copyItem);
		dto.setLessonTimeList(copyLessonTimeList);
		dto.setLessonTimeExList(copyLessonTimeExList);
		dto.setSubjectTimes(copySubjectTimes);
		dto.setRelationList(copyRelationList);
		
		try {
			newGkArrayItemService.saveCopyArrayFeature(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		return success("操作成功！");
	}
	
	private NewGkArrayItem copyArrayItem(String divideId, Integer times, String itemName) {
		NewGkArrayItem copyItem = new NewGkArrayItem();
		copyItem.setId(UuidUtils.generateUuid());
		copyItem.setModifyTime(new Date());
		copyItem.setCreationTime(new Date());
		copyItem.setTimes(times);
		copyItem.setItemName(itemName);
		copyItem.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
		copyItem.setDivideId(divideId);
		copyItem.setDivideType(NewGkElectiveConstant.ARRANGE_TYPE_04);
		return copyItem;
	}

	private NewGkSubjectTime copySubjectTime(NewGkArrayItem copyItem, NewGkSubjectTime oldSubjectTime) {
		NewGkSubjectTime subjTime;
		subjTime = new NewGkSubjectTime();
		subjTime.setId(UuidUtils.generateUuid());
		subjTime.setArrayItemId(copyItem.getId());
		subjTime.setCreationTime(new Date());
		subjTime.setModifyTime(new Date());
		
		subjTime.setFirstsdWeek(oldSubjectTime.getFirstsdWeek());
		subjTime.setIsNeed(oldSubjectTime.getIsNeed());
		subjTime.setPeriod(oldSubjectTime.getPeriod());
		//subjTime.setPunchCard(oldSubjectTime.getPunchCard());
		subjTime.setSubjectId(oldSubjectTime.getSubjectId());
		subjTime.setSubjectType(oldSubjectTime.getSubjectType());
		subjTime.setTimeInterval(oldSubjectTime.getTimeInterval());
		subjTime.setWeekRowNumber(oldSubjectTime.getWeekRowNumber());
		subjTime.setWeekRowPeriod(oldSubjectTime.getWeekRowPeriod());
		
		subjTime.setWeekRowType(oldSubjectTime.getWeekRowType());
		subjTime.setArrangeDay(oldSubjectTime.getArrangeDay());
		subjTime.setArrangeHalfDay(oldSubjectTime.getArrangeHalfDay());
		subjTime.setArrangeFrist(oldSubjectTime.getArrangeFrist());
		subjTime.setFirstsdWeekSubject(oldSubjectTime.getFirstsdWeekSubject());
		
		return subjTime;
	}

	private NewGkLessonTimeEx copyLessonTimeEx(NewGkLessonTime lessonTime, NewGkLessonTimeEx oldTimeEx) {
		NewGkLessonTimeEx lessonTimeEx;
		lessonTimeEx = new NewGkLessonTimeEx();
		lessonTimeEx.setId(UuidUtils.generateUuid());
		lessonTimeEx.setCreationTime(new Date());
		lessonTimeEx.setModifyTime(new Date());
		lessonTimeEx.setScourceTypeId(lessonTime.getId());
		lessonTimeEx.setArrayItemId(lessonTime.getArrayItemId());
		lessonTimeEx.setDayOfWeek(oldTimeEx.getDayOfWeek());
		lessonTimeEx.setPeriod(oldTimeEx.getPeriod());
		lessonTimeEx.setPeriodInterval(oldTimeEx.getPeriodInterval());
		lessonTimeEx.setScourceType(oldTimeEx.getScourceType());
		lessonTimeEx.setTimeType(oldTimeEx.getTimeType());
		return lessonTimeEx;
	}

	private NewGkLessonTime copyLessonTime(NewGkArrayItem copyItem, NewGkLessonTime oldTime) {
		NewGkLessonTime lessonTime;
		lessonTime = new NewGkLessonTime();
		lessonTime.setArrayItemId(copyItem.getId());
		lessonTime.setCreationTime(new Date());
		lessonTime.setModifyTime(new Date());
		lessonTime.setId(UuidUtils.generateUuid());
		
		lessonTime.setGroupType(oldTime.getGroupType());
		lessonTime.setIsJoin(oldTime.getIsJoin());
		lessonTime.setLevelType(oldTime.getLevelType());
		lessonTime.setObjectId(oldTime.getObjectId());
		lessonTime.setObjectType(oldTime.getObjectType());
		return lessonTime;
	}

	private String makeItemName(NewGkDivide divide, Integer times) {
		String semesterJson = semesterRemoteService.getCurrentSemester(2, divide.getUnitId());
		Semester semester = SUtils.dc(semesterJson, Semester.class);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);
		String itemName = semester.getAcadyear() + "学年" + grade.getGradeName() + "第" + semester.getSemester()
				+ "学期排课特征" + times;
		return itemName;
	}

	private Integer getMaxFeatureTimes(NewGkDivide divide) {
		List<NewGkArrayItem> arrayItemList = newGkArrayItemService.findByDivideId(divide.getId(),
				new String[] { NewGkElectiveConstant.ARRANGE_TYPE_04 });
		Integer times = 0;
		if (arrayItemList.size() > 0) {
			times = arrayItemList.get(0).getTimes();
		}
		return times;
	}
}
