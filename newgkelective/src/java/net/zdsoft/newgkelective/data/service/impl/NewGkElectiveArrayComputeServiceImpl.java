package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AtomicDouble;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.action.NewGkElectiveArrayAction;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.NewGkArrayResultSaveDto;
import net.zdsoft.newgkelective.data.entity.NewGkArray;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassCombineRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkClassSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.entity.NewGkPlaceItem;
import net.zdsoft.newgkelective.data.entity.NewGkRelateSubtime;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;
import net.zdsoft.newgkelective.data.entity.NewGkTimetable;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;
import net.zdsoft.newgkelective.data.entity.NewGkplaceArrange;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGClass;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseSection;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGPeriod;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGRoom;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudent;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGTeacher;
import net.zdsoft.newgkelective.data.optaplanner.dto.CGInputData;
import net.zdsoft.newgkelective.data.optaplanner.dto.NKPeriod;
import net.zdsoft.newgkelective.data.optaplanner.dto.NKRoom;
import net.zdsoft.newgkelective.data.optaplanner.listener.CGSolverListener;
import net.zdsoft.newgkelective.data.optaplanner.solver.CGForLectureSolver;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkArrayService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassStudentService;
import net.zdsoft.newgkelective.data.service.NewGkClassSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkCourseHeapService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideExService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkElectiveArrayComputeService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeExService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;
import net.zdsoft.newgkelective.data.service.NewGkPlaceItemService;
import net.zdsoft.newgkelective.data.service.NewGkRelateSubtimeService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTeacherService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableOtherService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableTeacherService;
import net.zdsoft.newgkelective.data.service.NewGkplaceArrangeService;

@Service
public class NewGkElectiveArrayComputeServiceImpl implements NewGkElectiveArrayComputeService {
	@Autowired
	public CourseRemoteService courseRemoteService;
	@Autowired
	public TeacherRemoteService teacherRemoteService;
	@Autowired
	public NewGkClassStudentService newGkClassStudentService;
	@Autowired
	public NewGkTimetableOtherService newGkTimetableOtherService;
	@Autowired
	public NewGkArrayService newGkArrayService;
	@Autowired
	public NewGkDivideService newGkDivideService;
	@Autowired
	public NewGkDivideExService newGkDivideExService;
	@Autowired
	public NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	public NewGkOpenSubjectService newGkOpenSubjectService;
	@Autowired
	public NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	public NewGkplaceArrangeService newGkplaceArrangeService;
	@Autowired
	public TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	public NewGkDivideClassService newGkDivideClassService;
	@Autowired
	public NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	public GradeRemoteService gradeRemoteService;
	@Autowired
	public NewGkLessonTimeExService newGkLessonTimeExService;
	@Autowired
	public NewGkPlaceItemService newGkPlaceItemService;
	@Autowired
	public NewGkTimetableService newGkTimetableService;
	@Autowired
	public NewGkArrayItemService newGkArrayItemService;
	@Autowired
	public NewGkChoResultService newGkChoResultService;
	@Autowired
	public NewGkCourseHeapService newGkCourseHeapService;
	@Autowired
	public NewGkSubjectTeacherService newGkSubjectTeacherService;
	@Autowired
	public NewGkTimetableTeacherService newGkTimetableTeacherService;
	@Autowired
	public StudentRemoteService studentRemoteService;
	@Autowired
	public NewGkChoRelationService newGkChoRelationService;
	@Autowired
	public NewGkElectiveArrayAction newGkElectiveArrayAction;
	@Autowired
	public UnitRemoteService unitService;
	@Autowired
	public NewGkRelateSubtimeService relateSubtimeService;
	@Autowired
	public NewGkClassSubjectTimeService newGkClassSubjectTimeService;
	@Autowired
	public NewGkClassCombineRelationService combineRelationService;
	@Autowired
	public ClassRemoteService classRemoteService;
	
	private static Logger logger = LoggerFactory.getLogger(NewGkElectiveArrayComputeServiceImpl.class);

	@Override
	public CGInputData computeArray3F7(final String key,
									   final String key1, NewGkArray newGkArray) {
		String divideId = newGkArray.getDivideId();
		CGInputData returnDto=new CGInputData();
		final String arrayId = newGkArray.getId();
		returnDto.setArrayId(arrayId);
		// 取出学生分班结果
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		if(divide==null){
			returnDto.appendMsg("该排课使用的分班方案不存在");
		}
		String openType = divide.getOpenType();
		
		Unit unit = unitService.findOneObjectById(newGkArray.getUnitId());
		returnDto.setMaxSectionStudentCount(divide.getGalleryful());
		returnDto.setMargin(divide.getMaxGalleryful());
		returnDto.setUnitName(unit.getUnitName());
		
		Grade grade = gradeRemoteService.findOneObjectById(newGkArray.getGradeId());
		returnDto.setGradeName(grade.getGradeName());
		int checkDayLectureCount = checkDayLectureCount(grade);
		if(checkDayLectureCount <=0){
//			throw new RuntimeException("年级上课时间一天上课节次数没有维护");
			returnDto.appendMsg("年级上课时间一天上课节次数没有维护");
		}
		if(grade.getWeekDays() == null) {
			returnDto.appendMsg("未设置周天数");
//			throw new IllegalArgumentException("未设置周天数");
		}
		
		//各科目周课时 key  行政班课程：subjectId   教学班 subjectId+A	
		Map<String,NewGkSubjectTime> courseTimeMap=new HashMap<String,NewGkSubjectTime>();
		// K: subjectId+Type Vmap:k:subject+Type v:不联排类型
		Map<String,Map<String,String>> noAttachCourseCodeMap=new HashMap<>();
		// 1.周课时安排
		boolean isPeriod = makePeriod2(unit.getId(),newGkArray.getLessonArrangeId(),courseTimeMap, noAttachCourseCodeMap);
		if(!isPeriod){
			returnDto.appendMsg("周课时没有维护");
		}
		
		//allperiodMap(时间Map)
		Map<String, NKPeriod> allperiodMap=new HashMap<String, NKPeriod>();
		//allPeriod 班级所有一周时间数据 
		List<NKPeriod> allPeriod=new ArrayList<NKPeriod>();
		//一周所有时间节点
		makeGradeAllTime(grade,allperiodMap,allPeriod);
		
		/**年级上课时间不排课限制**/
		Map<String, NKPeriod> gradeNoTime=new HashMap<String,NKPeriod>();
		/**科目上课时间不排课限制--区分AB**/
		Map<String,List<NKPeriod>> subjectNoTime=new HashMap<>();
		/**科目上课时间排课限制--区分AB**/
		Map<String,List<NKPeriod>> subjectCoupleTime=new HashMap<>();
		/**不参与排课的科目 ；暂时没有用到**/
		Set<String> nosubjectIds=new HashSet<String>();
		/* 批次排课时间 */
		Map<String, List<NKPeriod>> batchPeriodMap = new HashMap<>();
		/* 固定排课科目 */
		Map<String, List<NKPeriod>> fixedSubjectMap = new HashMap<>();
		// 班级不排课时间
		Map<String, List<NKPeriod>> classSubjectIdNoTime = new HashMap<>();
		// 3. 科目时间限制
		makeGradeSubjectTimeLimit(newGkArray, gradeNoTime, subjectNoTime, subjectCoupleTime,fixedSubjectMap,
				batchPeriodMap, nosubjectIds,allperiodMap,classSubjectIdNoTime);
		
		//periodMap(时间Map)
		Map<String, NKPeriod> periodMap=new HashMap<String, NKPeriod>();
		//periodList 实际上课安排时间 
		List<NKPeriod> periodList=new ArrayList<NKPeriod>();
		/**除去年级限制   periodMap  periodList**/
		clearGradeLimitTime(gradeNoTime,periodMap,allperiodMap,periodList,allPeriod);	
		
		returnDto.setPeriodList(allPeriod);
		
		
		
		Integer maxTimeslotIndex = periodList.stream().map(e->e.getTimeslotIndex()).max(Integer::compareTo).orElse(1);
		returnDto.setMaxTimeslotIndex(maxTimeslotIndex);
		returnDto.setMmCount(grade.getMornPeriods());
		returnDto.setAmCount(grade.getAmLessonCount());
		returnDto.setPmCount(grade.getPmLessonCount());
		returnDto.setNightCount(grade.getNightLessonCount());
		
		
		// 2.所有排课科目
		List<CGCourse> nnkCourseList=new ArrayList<CGCourse>();
		Map<String,CGCourse> nnkCourseMap=new HashMap<String,CGCourse>();
		Set<String> xzbSubjectId=new HashSet<String>();
		
		Set<String> cSubjectId=new HashSet<String>();
		// 固定排课的科目
		makeFixedSubjectTime(courseTimeMap, fixedSubjectMap, cSubjectId);
		
		String[] subIds = courseTimeMap.values().stream().map(e->e.getSubjectId()).distinct().toArray(e->new String[e]);
		Map<String, String> subjectNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(subIds),new TypeReference<Map<String, String>>(){});
		if(subjectNameMap.isEmpty()){
			returnDto.appendMsg("从课程库中获取不到排课科目");
//			throw new RuntimeException("从课程库中获取不到排课科目");
		}
		// 所有排课课程
		Set<String> xzbcode = makeCourses(courseTimeMap, nnkCourseList, nnkCourseMap, cSubjectId, subjectNameMap);
		nnkCourseList.stream().filter(e->e.getIsBiweeklyCourse()!=null).forEach(e->{
			CGCourse cgCourse = nnkCourseMap.get(e.getIsBiweeklyCourse().split("-")[0]);
			e.setIsBiweeklyCourse(cgCourse.getCode());
		});


//		nnkCourseList = nnkCourseList.stream().filter(e->e.getMinWorkingDaySize()>0).collect(Collectors.toList());
		returnDto.setCourseList(nnkCourseList);
		// 互斥 课程
		Map<CGCourse, Map<CGCourse,String>> noAttachCourseMap = makeNoAttachCourses(noAttachCourseCodeMap, nnkCourseMap);
		returnDto.setNoAttachCourseMap(noAttachCourseMap);
		
		// 主从分离 出问题 取出班级
		List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(
				divide.getUnitId(), arrayId, null,
				true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		allClassList = allClassList.stream().filter(e->!(NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())&&StringUtils.isBlank(e.getBatch()))).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(allClassList)){
			returnDto.appendMsg("班级数据为空");
		}
		/* 1. 存在 异常 ，第一次抛出*/
		if(returnDto.hasMsg()) {
			throw new RuntimeException(returnDto.getMsgString());
		}
		
		Map<String, String> classNameMap = EntityUtils.getMap(allClassList, NewGkDivideClass::getOldDivideClassId, NewGkDivideClass::getClassName);
		Map<String, String> classTypeMap = EntityUtils.getMap(allClassList, NewGkDivideClass::getOldDivideClassId, NewGkDivideClass::getClassType);
		
		Map<String, String> toOldCidMap = EntityUtils.getMap(allClassList, NewGkDivideClass::getId,NewGkDivideClass::getOldDivideClassId);
		Map<String,Set<String>> xzbBatchMap = new HashMap<>();
		allClassList.forEach(cls->{
			if(NewGkElectiveConstant.CLASS_TYPE_2.equals(cls.getClassType())
				&& StringUtils.isBlank(cls.getOldDivideClassId())){
				return;
			}
			cls.setId(cls.getOldDivideClassId());
			if(cls.getRelateId() != null) {
				String newRelateId = Stream.of(cls.getRelateId().split(",")).filter(e->toOldCidMap.containsKey(e))
						.map(e->toOldCidMap.get(e))
						.collect(Collectors.joining(","));
				cls.setRelateId(newRelateId);
			}
			if(cls.getParentId() != null) {
				cls.setParentId(toOldCidMap.get(cls.getParentId()));
			}
			if(NewGkElectiveConstant.DIVIDE_TYPE_07.equals(openType)
					&& NewGkElectiveConstant.CLASS_TYPE_2.equals(cls.getClassType())){
				String clsIds = cls.getBatch().split("-")[1];
				String[] clsIdarr = clsIds.split(",");
				if(clsIdarr.length <=0){
					return;
				}
				String code = cls.getSubjectType() + cls.getBatch();
				for (String s : clsIdarr) {
					xzbBatchMap.computeIfAbsent(s,e->new HashSet<>()).add(code);
				}
			}
		});
		
		Set<String> pure3ZhbIds = allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())&&e.getSubjectIds()!=null)
				.filter(e->e.getSubjectIds().split(",").length==3).map(e->e.getId())
				.collect(Collectors.toSet());
		Set<String> pure3XzbIds = allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType())&&pure3ZhbIds.contains(e.getRelateId()))
				.map(e->e.getId())
				.collect(Collectors.toSet());
		
		
		List<NewGkChoRelation> relaList = newGkChoRelationService.findByChoiceIdsAndObjectType(unit.getId(), 
				new String[] {divide.getId()}, NewGkElectiveConstant.CHOICE_TYPE_09);
		Map<String, List<String>> childrenClasrelaMap = EntityUtils.getListMap(relaList, NewGkChoRelation::getObjectTypeVal, NewGkChoRelation::getObjectValue);
		
		// 获取班级特征数据
		List<NewGkClassSubjectTime> allClassTimeList = newGkClassSubjectTimeService.findByArrayItemIdAndClassIdIn(unit.getId(), newGkArray.getLessonArrangeId(), null, null, null);
		Function<NewGkClassSubjectTime,String> fun = e->e.getClassId()+"-"+e.getSubjectId()+"-"+e.getSubjectType();
		Map<String, String> classTimeIdMap = EntityUtils.getMap(allClassTimeList, NewGkClassSubjectTime::getId,fun);
		Map<String, NewGkClassSubjectTime> classTimeMap = EntityUtils.getMap(allClassTimeList, fun);
		Map<String, List<NKPeriod>> classSubjectNoTime = new HashMap<>();
		for (String classSubjId :classSubjectIdNoTime.keySet()) {
			if(classTimeIdMap.containsKey(classSubjId)) {
				classSubjectNoTime.put(classTimeIdMap.get(classSubjId), classSubjectIdNoTime.get(classSubjId));
			}else {
				returnDto.appendMsg("根据id:"+classSubjId+"找不到班级特征");
			}
		}
		
		// 组装 班级数据
		Map<String, List<CGClass>> batchClassMap = new HashMap<>();
		
		Map<String,CGStudent> studentMap=new  HashMap<String,CGStudent>();
		Set<String> aSubjectId= nnkCourseList.stream().filter(e->"A".equals(e.getSubjectType())).map(e->e.getSubjectId()).collect(Collectors.toSet());
		Set<String> bSubjectId= nnkCourseList.stream().filter(e->"B".equals(e.getSubjectType())).map(e->e.getSubjectId()).collect(Collectors.toSet());
		StringBuilder sbT = new StringBuilder();
		List<CGClass> classList = makeClassAndStus(nnkCourseMap, aSubjectId, xzbcode, allClassList, 
				batchClassMap, studentMap, bSubjectId, classTimeMap,childrenClasrelaMap,newGkArray,divide,sbT);
		if(sbT.length()>0) {
			returnDto.appendMsg(sbT.toString());
			throw new RuntimeException(returnDto.getMsgString());
		}
		if(CollectionUtils.isEmpty(classList)) {
			throw new RuntimeException("需要上课班级个数为0");
		}
		returnDto.setClassList(classList);
		returnDto.setStudentList(Arrays.asList(studentMap.values().toArray(new CGStudent[]{})));
		Map<String,List<CGClass>> classIdMap = EntityUtils.getListMap(classList, CGClass::getOldId,e->e); 
		
		
		// 合班数据
		List<NewGkClassCombineRelation> combiRelaList = combineRelationService.findByArrayItemId(unit.getId(), newGkArray.getLessonArrangeId());
		Map<String, CGClass> codeClassMap = classList.stream().collect(Collectors
				.toMap(e -> e.getOldId()+"-" + e.getCourse().getSubjectId()+"-" + e.getCourse().getSubjectType(), e -> e));
		sbT = new StringBuilder();
		List<NewGkClassCombineRelation> combineRawList = new ArrayList<>();
		List<NewGkClassCombineRelation> meanWhileRawList = new ArrayList<>();
		for (NewGkClassCombineRelation rela : combiRelaList) {
			String[] split = rela.getClassSubjectIds().split(",");
			sbT.append(checkCombine(subjectNameMap, classNameMap, codeClassMap, rela, split[0]));
			sbT.append(checkCombine(subjectNameMap, classNameMap, codeClassMap, rela, split[1]));
			if(NewGkElectiveConstant.COMBINE_TYPE_1.equals(rela.getType())){
				combineRawList.add(rela);
			}else if(NewGkElectiveConstant.COMBINE_TYPE_2.equals(rela.getType())){
				meanWhileRawList.add(rela);
			}
		}
		if(sbT.length()>0) {
			returnDto.appendMsg(sbT.toString());
		}
		List<Set<String>> combineGroups = combineRelationService.getGroupRelations(combineRawList);
		List<Set<String>> meanGroups = combineRelationService.getGroupRelations(meanWhileRawList);
		returnDto.setCombineClassList(combineGroups);
		returnDto.setMeanwhileClassList(meanGroups);


		// 3+1+2 不重组时 ；隶属于同一行政班的  合班教学班 进行同时排课
		List<String[]> noMeanWhileClassGroups = new ArrayList<>();
		String folowType = Optional.ofNullable(divide.getFollowType()).orElse("");
		if((folowType.contains(NewGkElectiveConstant.FOLLER_TYPE_A2)||folowType.contains(NewGkElectiveConstant.FOLLER_TYPE_B2))
				&& (NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())
						|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType()))) {
			List<NewGkDivideClass> hbJxbList = allClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_3.equals(e.getClassType())).collect(Collectors.toList());
			Map<String, List<NewGkDivideClass>> hbJxbMap = EntityUtils.getListMap(hbJxbList, NewGkDivideClass::getParentId, e->e);
			for (String xzbId : hbJxbMap.keySet()) {
				List<NewGkDivideClass> list = hbJxbMap.get(xzbId);
				if(list.size()>1) {
					// 两个班级同时排课
					if(folowType.contains(NewGkElectiveConstant.FOLLER_TYPE_A2)) {
						Set<String> meanWhileGroup1 = list.stream().filter(e->codeClassMap.containsKey(e.getId()+"-"+e.getSubjectIds()+"-"+NewGkElectiveConstant.SUBJECT_TYPE_A))
								.map(e->e.getId()+"-"+e.getSubjectIds()+"-"+NewGkElectiveConstant.SUBJECT_TYPE_A)
								.collect(Collectors.toSet());
						if(meanWhileGroup1.size() >1) {
							returnDto.getMeanwhileClassList().add(meanWhileGroup1);
						}
					}
					if(folowType.contains(NewGkElectiveConstant.FOLLER_TYPE_B2)) {
						Set<String> meanWhileGroup2 = list.stream().filter(e->codeClassMap.containsKey(e.getId()+"-"+e.getSubjectIdsB()+"-"+NewGkElectiveConstant.SUBJECT_TYPE_B))
								.map(e->e.getId()+"-"+e.getSubjectIdsB()+"-"+NewGkElectiveConstant.SUBJECT_TYPE_B)
								.collect(Collectors.toSet());
						if(meanWhileGroup2.size() > 1) {
							returnDto.getMeanwhileClassList().add(meanWhileGroup2);
						}
					}
				}
				
				for (NewGkDivideClass hb : list) {
					String relateId = hb.getRelateId();
					String[] clsIds = relateId.split(",");
//					Arrays.stream(clsIds).map(e->e)
					for (String cid : clsIds) {
						// 这几个班级绝对不能同时排课,不论科目
						noMeanWhileClassGroups.add(new String[] {hb.getId(),cid});
					}
				}
			}
		}
		returnDto.setNoMeanWhileClassGroups(noMeanWhileClassGroups);
		
		
		// 组装教师数据
		Map<String, Map<String,String>> classTeacherMap = new HashMap<String, Map<String,String>>();  // key:班级Id  value:科目id:教师Id
		
		Map<String, CGTeacher> teacherMap = new HashMap<String, CGTeacher>();
		Map<String, NewGkTeacherPlanEx> teachPlannerExMap = new HashMap<>();
		Map<String, Set<String>> subjectIdTeacherIdMap = makeClassTeacher(newGkArray, xzbSubjectId, classTeacherMap,
				 teacherMap, teachPlannerExMap);
		String[] teacherIds = teacherMap.keySet().toArray(new String[] {});
		Map<String, String> teacherNameMap = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIds),new TypeReference<Map<String, String>>(){});
		teacherMap.values().stream().forEach(e->{
			e.setTeacherName(teacherNameMap.getOrDefault(e.getCode(), "未知老师"));
		});
		
		// 获取 教师 不排课时间点
		Map<String,Set<NKPeriod>> teacherNoPeriods = new HashMap<>();
//		Map<String,Set<NKPeriod>> teacherdoPeriods = new HashMap<>();
		List<NewGkLessonTime> teacherNoTimes = makeTeacherPeriods(newGkArray, allperiodMap, teacherNoPeriods, null);
		
		// 向班级 添加教师
		for (CGClass clazz : classList) {
			String oldId = clazz.getOldId();
			if(classTeacherMap.containsKey(oldId)){
				Map<String, String> subjTeacherMap = classTeacherMap.get(oldId);
				if(subjTeacherMap != null && StringUtils.isNotBlank(subjTeacherMap.get(clazz.getCourse().getSubjectId()))){
					String teacherId = subjTeacherMap.get(clazz.getCourse().getSubjectId());
					CGTeacher teacher = teacherMap.get(teacherId);
					if(teacher == null){
						returnDto.appendMsg("error occur in adding Teachers to section:"+teacherId);
						continue;
					}
					clazz.setTeacher(teacher);
				}
			}
		}
		
		List<CGTeacher> teacherList = teacherMap.entrySet().stream().map(e->e.getValue()).collect(Collectors.toList());
		returnDto.setTeacherList(teacherList);
		
		boolean autoArrangeTeacher = false;
		if(autoArrangeTeacher) {  // 自动安排老师
//			String msg = autoAddTeacher(subjectNameMap, batchClassMap, pureClassSet, twoXClassSet, teacherMap,
//					subjectIdTeacherIdMap, workLoadMap);
//			if(StringUtils.isNotBlank(msg)) {
//				throw new RuntimeException(msg);
//			}
		}


		// 检验合班班级老师是否相同
		Map<String, CGClass> code2Map = EntityUtils.getMap(classList, e->e.getOldId()+"-"+e.getCourse().getSubjectId()+"-"+e.getCourse().getSubjectType());
		for (Set<String> code : combineGroups) {
			long count = code.stream().filter(e->code2Map.containsKey(e)).map(e->code2Map.get(e).getTeacher()).distinct().count();
			if(count > 1) {
				String className = code.stream().filter(e->code2Map.containsKey(e)).map(e->code2Map.get(e).getClassName()).findFirst().get();
				returnDto.appendMsg("合班班级教师必须相同，请重新设置。(包含"+className+"等班级)");
			}
		}
		for (Set<String> code : meanGroups) {
			List<CGTeacher> trs = code.stream().filter(e->code2Map.containsKey(e))
					.map(e->code2Map.get(e).getTeacher())
					.filter(e->e!=null)
					.collect(Collectors.toList());
			if(new HashSet<>(trs).size() < trs.size()) {
				String className = code.stream().filter(e->code2Map.containsKey(e)).map(e->code2Map.get(e).getClassName()).findFirst().get();
				returnDto.appendMsg("同时排课班级教师不能相同，请重新设置。(包含"+className+"等班级)");
			}
		}

		// 互斥老师 teachPlannerExMap
		if(teacherNoTimes.size() > 0) {
			Map<CGTeacher, Set<CGTeacher>> mutexTeacherMap = setMutexTeacher(unit.getId(),teacherMap, teacherNoTimes);
			returnDto.setMutexTeacherMap(mutexTeacherMap);
		}

		/**全部场地**/
		Map<CGClass, NKRoom> roomConstrains = null;
		List<NKRoom> roomList=new ArrayList<NKRoom>();
		if(NewGkElectiveConstant.DIVIDE_TYPE_07.equals(openType)) { // 行政班排课 安排场地
			Map<String, String> cidMap = allClassList.stream()
					.filter(e -> NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
					.collect(Collectors.toMap(NewGkDivideClass::getOldClassId, NewGkDivideClass::getId));

			List<Clazz> clzList = classRemoteService.findListObjectBy(Clazz.class, null, null, "id",
					cidMap.keySet().toArray(new String[0]), new String[] { "id", "teachPlaceId","className" });
			Map<String,List<String>> placeCidMap = new HashMap<>();

			String msg = checkXzbRoomConflicts(clzList, placeCidMap);
			if(StringUtils.isNotBlank(msg)) {
				returnDto.appendMsg(msg);
			}

			Map<String, String> clzPlaceMap = clzList.stream().filter(e -> e.getTeachPlaceId() != null)
					.collect(Collectors.toMap(e->cidMap.get(e.getId()), Clazz::getTeachPlaceId));
			List<NewGkPlaceItem> jxbPlaceItemList = newGkPlaceItemService.findByArrayItemId(divideId);  // 获取走班 预设场地信息
			jxbPlaceItemList.stream()
					.filter(e->StringUtils.isNotBlank(e.getPlaceId())
						&& StringUtils.isNotBlank(e.getObjectId()))
					.forEach(e->clzPlaceMap.put(e.getObjectId(), e.getPlaceId()));

			roomConstrains = new HashMap<>();
			int i = 0;
			Map<String, NKRoom> roomMap = new HashMap<>();
			NKRoom roomT;
			for (String cid : classIdMap.keySet()) {
				if(clzPlaceMap.containsKey(cid)) {
					String roomId = clzPlaceMap.get(cid);
					roomT = roomMap.get(roomId);
					if(roomT == null) {
						roomT = new NKRoom();
						roomT.setRoomId(roomId);
						roomMap.put(roomId, roomT);
						roomList.add(roomT);
					}
				}else {
					roomT = new NKRoom();
					roomT.setRoomId(CGRoom.NO_ROOM_CODE +arrayId+ (i++));
					roomT.setRoomName(CGRoom.NO_ROOM_CODE);
					roomList.add(roomT);
				}
				for (CGClass e : classIdMap.get(cid)) {
					roomConstrains.put(e, roomT);
				}

			}
		}else {  // 7选3  排课 安排场地
			List<TeachPlace> allplaceList= makeAllPlace(newGkArray.getPlaceArrangeId());
			if(CollectionUtils.isNotEmpty(allplaceList)){
				//获取场地
				Map<String,NKRoom> roomMap=new HashMap<String,NKRoom>();
				makeRooms(allplaceList,roomList,roomMap);
				//获取 场地限制  取消了科目场地限制
				List<NewGkPlaceItem> newGkPlaceItemList = newGkPlaceItemService.findByArrayItemId(newGkArray.getPlaceArrangeId());
				roomConstrains = makeClassPlaceConstraint(classIdMap, roomMap, newGkPlaceItemList);

				// 被拆分出来的班级 比如 由技术 拆出来的， 信息 和 通用 班级 ；非3+1+2 系列模式才有的
				List<String> parttern312 = Arrays.asList(NewGkElectiveConstant.DIVIDE_TYPE_09,NewGkElectiveConstant.DIVIDE_TYPE_10,
						NewGkElectiveConstant.DIVIDE_TYPE_11,NewGkElectiveConstant.DIVIDE_TYPE_12);
				if(!parttern312.contains(divide.getOpenType())) {
					List<NewGkDivideClass> collect = allClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())
							&& e.getParentId()!=null).collect(Collectors.toList());
					Set<CGClass> delSet = new HashSet<>();
					for (NewGkDivideClass newGkDivideClass : collect) {
						List<CGClass> parents = classIdMap.get(newGkDivideClass.getParentId());
						List<CGClass> list = classIdMap.get(newGkDivideClass.getId());
						if(parents!=null && list != null) {
							delSet.addAll(parents);
							// 保存 场地时 已经为 拆分的班级 安排场地 这里不需要安排了 ;还是需要的，以防重新拆分班级后 班级安排失效
							for (CGClass cgClass : parents) {
								NKRoom nkRoom = roomConstrains.get(cgClass);
								if(nkRoom!=null) {
									roomConstrains.put(list.get(0), nkRoom);
									break;
								}
							}
						}
					}
					// 删除 父班级
					classList.removeAll(delSet);
					for (CGClass cgClass : delSet) {
						roomConstrains.remove(cgClass);
					}
				}
				
			}else {
				returnDto.appendMsg("场地没有维护");
			}
			
		}
		
//		Map<CGCourse, List<NKRoom>> roomConstrains=new HashMap<CGCourse, List<NKRoom>>();
//		returnDto.setRoomConstrains(roomConstrains);
		returnDto.setRoomList(roomList);
		returnDto.setXzbRoomConstrains(roomConstrains);
		Map<CGClass, NKRoom> singleRoomConstrains = roomConstrains;
		
		
		// 获取课程时间点的限制 对象模型 ；科目不排课时间 ，科目要排课时间 ，科目优先排课时间(暂无)，科目固定排课时间
		Map<CGCourse, List<NKPeriod>> doCoursePeriods=new HashMap<CGCourse,List<NKPeriod>>();
		Map<CGCourse, List<NKPeriod>> noCoursePeriods=new HashMap<CGCourse,List<NKPeriod>>();
		
		Set<NKPeriod> fixedPeriods = makeCoursePeriodConstrains(subjectNoTime, fixedSubjectMap,
				nnkCourseMap, doCoursePeriods, noCoursePeriods);
		
		// 此处，将对科目的时间限制转化为对班级的限制
		Map<CGClass, Set<NKPeriod>> doClassPeriods = new HashMap<>();
		Map<CGClass, Set<NKPeriod>> noClassPeriods = new HashMap<>();
		
		Map<CGCourse, List<CGClass>> courseClassMap = classList.stream().collect(Collectors.groupingBy(CGClass::getCourse));
		makeClassPeriods(noCoursePeriods, courseClassMap, noClassPeriods);
		makeClassPeriods(doCoursePeriods, courseClassMap, doClassPeriods);
		
		for (CGClass cls : classList) {
			String c = cls.getOldId()+"-"+cls.getCourse().getSubjectId()+"-"+cls.getCourse().getSubjectType();
			if(classSubjectNoTime.containsKey(c)) {
				noClassPeriods.put(cls, new HashSet<>(classSubjectNoTime.get(c)));
			}
		}
		
		// 对科目的不排课限制只对 跟随行政班上课的班级有用
		new HashSet<>(noClassPeriods.keySet()).forEach(e->{
			if(!e.isFixed()) {
	//				noClassPeriods.remove(e);
			}
		});
		// 将老师不准排课的时间 转换为  班级不准排课的时间
		classList.stream().filter(e->e.getTeacher()!=null).forEach((CGClass e)->{
			Set<NKPeriod> set = teacherNoPeriods.get(e.getTeacher().getCode());
			if(CollectionUtils.isNotEmpty(set)) {
				if(noClassPeriods.get(e) == null) {
					noClassPeriods.put(e, new HashSet<>(set));
				}else {
					noClassPeriods.get(e).addAll(set);
				}
			}
		});
		
		// 连排时间点
		Map<CGCourse,List<NKPeriod>> subjectCoupleTimeMap = new HashMap<>();
		for (String code : subjectCoupleTime.keySet()) {
			List<NKPeriod> list = subjectCoupleTime.get(code);
			
			CGCourse cgCourse = nnkCourseMap.get(code);
			if(cgCourse == null) {
				continue;
			}
			subjectCoupleTimeMap.put(cgCourse, new ArrayList<>(list));
		}
		returnDto.setSubjectCoupleTimeMap(subjectCoupleTimeMap);

		//------------------为每个班级限定可排课时间---------------------//
		// 除去 固定排课 以后 剩下 的时间点
		Set<NKPeriod> availablePeriod = periodList.stream().filter(e->!fixedPeriods.contains(e)).collect(Collectors.toSet());

		// hsdfz 将高二学考 不按批次排课
		Set<NKPeriod> batchPeriods = batchPeriodMap.keySet().stream()
	//				.filter(e->!e.startsWith("C"))
				.flatMap(e->batchPeriodMap.get(e).stream()).collect(Collectors.toSet());
		// 除去固定排课 和 批次时间
		Set<NKPeriod> remainPeriods = availablePeriod.stream().filter(e->!batchPeriods.contains(e)).collect(Collectors.toSet());
		// A1. 为各个批次中的走班班级设置上课时间
		String msg = "";
		String batchName = "";
		for (String batchStr : batchClassMap.keySet()) {
			List<CGClass> list = batchClassMap.get(batchStr);
			if(CollectionUtils.isEmpty(list)) {
				continue;
			}
			
			if(batchStr.startsWith(NewGkElectiveConstant.SUBJECT_TYPE_A)) {
				batchName = batchStr.replace(NewGkElectiveConstant.SUBJECT_TYPE_A, "选考");
			}else if(batchStr.startsWith(NewGkElectiveConstant.SUBJECT_TYPE_B)) {
				batchName = batchStr.replace(NewGkElectiveConstant.SUBJECT_TYPE_B, "学考");
			}else {
				// 不可能
				returnDto.appendMsg("存在选考 学考以外的批次点");
			}

			long count = list.stream().filter(e->e.getParentId()==null).count();
			if(count > roomList.size()) {
				msg += batchName+"有"+list.size()+"个班级  ";
			}else {
				// 为每个批次的班级设置固定的场地
			}

			List<NKPeriod> periodList2 = batchPeriodMap.get(batchStr);
			if(CollectionUtils.isEmpty(periodList2)) {
				String msg2 = batchName+"有"+list.size()+"个班级，却没有设置可排课时间";
				returnDto.appendMsg(msg2);
			}

			list.forEach(e->doClassPeriods.put(e, new HashSet<>(periodList2)));
		}
		if(StringUtils.isNotBlank(msg)) {
			msg = "教室不足：共有" +roomList.size()+"个教室。"+ msg;
			returnDto.appendMsg(msg);
		}
		
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayIdWithMaster(newGkArray.getUnitId(), arrayId);
		List<NewGkTimetableOther> timetableOtherList = newGkTimetableOtherService.findListByInWithMaster("timetableId", EntityUtils.getList(timetableList, NewGkTimetable::getId).toArray(new String[0]));
		Map<String, NewGkTimetable> ttMap = EntityUtils.getMap(timetableList, NewGkTimetable::getId);
		
		Map<CGStudent,Set<CGClass>> jxbStuClsMap = new HashMap<>();
		classList.stream().filter(e->!e.isFixed()).forEach(cls->{
			if(CollectionUtils.isNotEmpty(cls.getStudentList())) {
				cls.getStudentList().forEach(stu->{
					if(!jxbStuClsMap.containsKey(stu)) {
						jxbStuClsMap.put(stu, new HashSet<>());
					}
					jxbStuClsMap.get(stu).add(cls);
				});
			}
		});

		// 预排课程的 科目名称
		Set<String> exisTsubIds = EntityUtils.getSet(timetableList, e -> e.getSubjectId());
		Map<String, String> existSubNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(exisTsubIds.toArray(new String[0])),
				new TypeReference<Map<String, String>>() {});

		Map<String,List<NKPeriod>> clsSubTimeMap = new HashMap<>();
		Map<NKPeriod,List<CGClass>> periodClsMap = new HashMap<>();
		Map<String,List<NKPeriod>> fixedTable = new HashMap<>();
		Map<String,String> handChangeMap = new HashMap<>();
		
		Map<String,List<NKPeriod>> existedTable = new HashMap<>();
		List<NKPeriod> claSubList = null;
		List<NKPeriod> list2 = null;
		NKPeriod p = null;
		List<String> discardLectures = new ArrayList<>();
		for (NewGkTimetableOther tto : timetableOtherList) {
			NewGkTimetable tt = ttMap.get(tto.getTimetableId());
			String cid = tt.getClassId();
			String oldId = toOldCidMap.get(cid);
			String classSubjectId = oldId +"-"+tt.getSubjectId();
			if(!code2Map.containsKey(classSubjectId+"-"+tt.getSubjectType())){
				returnDto.appendMsg(classNameMap.get(oldId)+" 预排科目 "+existSubNameMap.get(tt.getSubjectId())+" 在排课特征中不存在");
				continue;
			}
			//-------------//
			List<NKPeriod> list = clsSubTimeMap.get(classSubjectId);
			if(list == null) {
				list = new ArrayList<>();
				clsSubTimeMap.put(classSubjectId, list);
			}
			//-----------------//
			if(handChangeMap.containsKey(tto.getId())) {
				list2 = fixedTable.get(handChangeMap.get(tto.getId()));
				if(list2 == null) {
					list2 = new ArrayList<>();
					fixedTable.put(classSubjectId, list2);
				}
				if((p = allperiodMap.get(handChangeMap.get(tto.getId()))) != null) {
					list2.add(p); // 不包含在 allperiodMap 中，可能是将 某一时间段的上课时间取消了 比如：取消了晚自习
				}
				
				tto.setDayOfWeek(p.getDayIndex());
				tto.setPeriodInterval(p.getPeriodInterval());
				tto.setPeriod(p.getOtherIndex());
			}
			if((p = allperiodMap.get(tto.getDayOfWeek()+"_"+tto.getPeriodInterval()+"_"+tto.getPeriod())) == null) {
				// 不包含在 allperiodMap 中，可能是将 某一时间段的上课时间取消了 比如：取消了晚自习
				discardLectures.add(tto.getDayOfWeek()+"_"+tto.getPeriodInterval()+"_"+tto.getPeriod());
				continue;
			}
			claSubList = existedTable.get(classSubjectId);
			if(claSubList == null) {
				claSubList = new ArrayList<>();
				existedTable.put(classSubjectId, claSubList);
			}
			list.add(p); 
			claSubList.add(p);
			//------------//
			if(!NewGkElectiveConstant.CLASS_TYPE_2.equals(tt.getClassType())) {
				continue;
			}
			List<CGClass> list21 = periodClsMap.get(p);
			if(list21 == null) {
				list21 = new ArrayList<>();
				periodClsMap.put(p, list21);
			}
			CGClass cgClass = codeClassMap.get(classSubjectId+"-"+tt.getSubjectType());
			if(cgClass != null) {
				list21.add(cgClass);
			}
		}
		if(discardLectures.size() > 0) {
			logger.error("舍弃"+discardLectures.size()+"节预排课程，可能是因为此时间段不再上课 (比如取消晚自习)");
		}
		returnDto.setExistedTable(existedTable);
		if(handChangeMap!=null && handChangeMap.size()>0)
			returnDto.setFixedLectures(fixedTable);
		
		/* 计算行政班 课程 课时数 */
		Map<String,Double> xzbLecCountMap = new HashMap<>();
		classList.stream().filter(e->e.isFixed()).forEach(e->{
			Double count = xzbLecCountMap.get(e.getOldId());
			if(count == null) {
				count = 0d;
			}
			Double lectureCount = e.getLectureCount() + count;
			if(e.getCourse().getIsBiWeekly() != CGCourse.WEEK_TYPE_NORMAL) {
				lectureCount -= 0.5;
			}
			
			xzbLecCountMap.put(e.getOldId(), lectureCount);
		});
		
		//TODO 不重组的 行政班 物理历史 课时计算
		Map<String,List<String>> xzbHbMap = new HashMap<>();
		Map<String,NewGkDivideClass> c3ClassMap = new HashMap<>();
		allClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_3.equals(e.getClassType()))
				.forEach(c->{
					String[] xzbIds = c.getRelateId().split(",");
					for (String xzbId : xzbIds) {
						List<String> list = xzbHbMap.get(xzbId);
						if(list == null) {
							list = new ArrayList<>();
							xzbHbMap.put(xzbId, list);
						}
						list.add(c.getId());
					}
					c3ClassMap.put(c.getId(), c);
				});

		// 当 3+1+2 模式且 分班时物理/历史 在行政班上课,将物理 历史的课时 计入行政班
		addWuliLishiCount(divide, nnkCourseMap, xzbLecCountMap, xzbHbMap, c3ClassMap);

		// 组合固定学生
		Set<String> zhgdStuIds = new HashSet<>();
		if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())) {
			Set<String> stuIds = allClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType()))
					.flatMap(e->e.getStudentList().stream()).collect(Collectors.toSet());
			zhgdStuIds.addAll(stuIds);
		}
		
		// 伪行政班 上课 节次数
		Set<NKPeriod> realXzbPeriods  = batchPeriods;
		Set<NKPeriod> fakeXzbPeriods = availablePeriod.stream().filter(e->!realXzbPeriods.contains(e)).collect(Collectors.toSet());
		// 所有行政班 设置上课时间点
		StringBuilder sb = new StringBuilder();
		for (String oldId : classIdMap.keySet()) {
			List<CGClass> clsList = classIdMap.get(oldId);
			CGClass cls = clsList.get(0);
			if(!cls.isFixed()) { // 过滤教学班的班级
				continue;
			}
			String classType = classTypeMap.get(oldId);
			Set<NKPeriod> ps = new HashSet<>();
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(classType)) {
				if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
						|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())) {
					// 处理 纯三科组合班级
					ps = availablePeriod;
					boolean anyMatch = cls.getStudentList().stream().anyMatch(e->zhgdStuIds.contains(e.getStudentId()));
					if(anyMatch) {
						ps  = realXzbPeriods;
					}

				}else {
					// 只获取行政班的班级
					Set<NKPeriod> jxbTimes = cls.getStudentList().stream().filter(e->jxbStuClsMap.containsKey(e))
							.flatMap(e->jxbStuClsMap.get(e).stream())
							.filter(e->clsSubTimeMap.containsKey(e.getOldId()+"-"+e.getCourse().getSubjectId()))
							.flatMap(e->clsSubTimeMap.get(e.getOldId()+"-"+e.getCourse().getSubjectId()).stream())
							.collect(Collectors.toSet());
					// 行政班排课时 jxbTimes 应该加上 批次点的 时间 无论是否有学生走班
					int old = jxbTimes.size();
					if(NewGkElectiveConstant.DIVIDE_TYPE_07.equals(openType)
						&& xzbBatchMap.containsKey(cls.getOldId())){
						xzbBatchMap.get(cls.getOldId()).stream()
								.filter(e->batchPeriodMap.containsKey(e))
								.flatMap(e->batchPeriodMap.get(e).stream())
								.forEach(e->jxbTimes.add(e));
						if(jxbTimes.size() > old){
							System.out.println(jxbTimes.size()-old);
						}
					}

					ps = availablePeriod.stream().filter(e->!jxbTimes.contains(e)).collect(Collectors.toSet());
					NKRoom nkRoom = singleRoomConstrains.get(cls);
					for (NKPeriod nkPeriod : periodClsMap.keySet()) {
						if(jxbTimes.contains(nkPeriod) || nkRoom == null) {
							continue;
						}
						
						boolean roomConflict = periodClsMap.get(nkPeriod).stream().filter(e->singleRoomConstrains.containsKey(e))
								.anyMatch(e->nkRoom.equals(singleRoomConstrains.get(e)));
						if(roomConflict) {
							ps.remove(nkPeriod);
						}
					}
				}
				//判断 行政班 是否时间足够
				Double lecCount = xzbLecCountMap.get(cls.getOldId());
				if(lecCount > (ps.size() + fixedPeriods.size())) {
					sb.append("，"+cls.getClassName());
				}
				// 相关的 不重组 班级
			}else if(NewGkElectiveConstant.CLASS_TYPE_4.equals(classType)){
				//
				ps = fakeXzbPeriods;
				//判断 伪行政班 是否时间足够 TODO 计算行政班 总节次数的 代码 可以简化一下  在这里完成 无需 另开foreach循环
				double sum = clsList.stream().map(e->e.getLectureCount()).collect(Collectors.summarizingDouble(e->e)).getSum();
				if(sum > ps.size()) {
					sb.append("，"+cls.getClassName());
				}
			}else {
				continue;
			}
			
			final Set<NKPeriod> ps2 = ps;
			clsList.forEach(e->doClassPeriods.put(e, new HashSet<>(ps2)));
			List<String> list = xzbHbMap.get(oldId);
			if(CollectionUtils.isNotEmpty(list)) {
				list.stream().filter(e->classIdMap.containsKey(e)).map(e->classIdMap.get(e))
						.forEach(l3->{
							Set<NKPeriod> set = doClassPeriods.get(l3.get(0));
							if(CollectionUtils.isEmpty(set)) {
								l3.forEach(e->doClassPeriods.put(e,new HashSet<>(ps2)));
							}else {
								Set<NKPeriod> intersection = Sets.intersection(ps2, set);
								l3.forEach(e->doClassPeriods.put(e,new HashSet<>(intersection)));
							}
						});
			}
		}
		
		if(sb.length()>0) {
			returnDto.appendMsg(sb.substring(1)+" 行政班上课时间点不够");
		}
		/* 2. 第二次 集中抛出异常 */
		if(returnDto.hasMsg()) {
			throw new RuntimeException(returnDto.getMsgString());
		}
//		int ui = 1/0;
		returnDto.setDoClassPeriods(doClassPeriods);
		returnDto.setNoClassPeriods(noClassPeriods);
		
		// 校验 批次内的 场地 和教师是否有重复
		msg = validateBtachRoomAndTeachers(batchClassMap, singleRoomConstrains);
		if(StringUtils.isNotBlank(msg)) {
			returnDto.appendMsg(msg);
		}

//		CGForLectureSolver solver= new CGForLectureSolver();
//		solver.addListener(getListener(key, key1, newGkArray, arrayId));
//
//		Long dd = new Date().getTime();
//		solver.solve(returnDto, Evn.isDevModel()?dd:null);
		return returnDto;
	}

	/**
	 * 当 3+1+2 模式且 分班时物理/历史 在行政班上课,将物理 历史的课时 计入行政班
	 * @param divide
	 * @param nnkCourseMap
	 * @param xzbLecCountMap
	 * @param xzbHbMap
	 * @param c3ClassMap
	 */
	private void addWuliLishiCount(NewGkDivide divide, Map<String, CGCourse> nnkCourseMap, Map<String, Double> xzbLecCountMap, Map<String, List<String>> xzbHbMap, Map<String, NewGkDivideClass> c3ClassMap) {
		String followType = Optional.ofNullable(divide.getFollowType()).orElse("");
		if(followType.contains(NewGkElectiveConstant.FOLLER_TYPE_A2)|| followType.contains(NewGkElectiveConstant.FOLLER_TYPE_B2)){
			// 当 3+1+2 模式且 分班时物理/历史 在行政班上课,将物理 历史的课时 计入行政班
			for (String xzbId : xzbHbMap.keySet()) {
				List<String> list = xzbHbMap.get(xzbId);
				int max = 0;
				for (String cid : list) {
					//3+1+2 合班 教学班
					NewGkDivideClass cls = c3ClassMap.get(cid);
					int workTimes = 0;
					if(followType.contains(NewGkElectiveConstant.FOLLER_TYPE_A2) && nnkCourseMap.containsKey(cls.getSubjectIds()+NewGkElectiveConstant.SUBJECT_TYPE_A)){
						workTimes += nnkCourseMap.get(cls.getSubjectIds()+NewGkElectiveConstant.SUBJECT_TYPE_A).getMinWorkingDaySize();
					}
					if(followType.contains(NewGkElectiveConstant.FOLLER_TYPE_B2) && nnkCourseMap.containsKey(cls.getSubjectIdsB()+NewGkElectiveConstant.SUBJECT_TYPE_B)){
						workTimes += nnkCourseMap.get(cls.getSubjectIdsB()+NewGkElectiveConstant.SUBJECT_TYPE_B).getMinWorkingDaySize();
					}
					if(max < workTimes) {
						max = workTimes;
					}
				}
				Double double1 = xzbLecCountMap.get(xzbId);
				xzbLecCountMap.put(xzbId, double1+max);
			}
		}
	}

	private CGSolverListener getListener(String key, String key1, NewGkArray newGkArray, String arrayId,List<String> arrayIds) {
		return new CGSolverListener() {

			@Override
			public void solveFinished(CGInputData resultClass, boolean noConflict) {
				RedisUtils.set(key,"success");
				RedisUtils.set(key1, "排课成功，正准备组装数据");
				long start = System.currentTimeMillis();

				saveResult3(resultClass,arrayIds);

				long end = System.currentTimeMillis();
				System.out.println("make耗时：" + (end-start)/1000 + "s");
				RedisUtils.set(key,"success");
				RedisUtils.set(key1, "排课成功");

				//判断冲突
				try{
					for (String arrId : arrayIds) {
						int conflictNum = newGkTimetableService.getConflictNumWithMaster(arrId, new ArrayList<>());
						if(conflictNum <= 0){
							// 因为读写分离 需要等待1000毫秒
							Thread.sleep(1000);
							//直接分堆
							String mess=newGkArrayService.autoArraySameClass(newGkArray.getUnitId(), arrId);
							if(StringUtils.isBlank(mess)){
								RedisUtils.set(key,"success");
								RedisUtils.set(key1, "自动分堆成功！");
							}
							// 检查教师冲突
							newGkElectiveArrayAction.checkAllTeacher(arrId);
						}
					}

				}catch(Exception e){
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
				RedisUtils.set(key,"error");
				RedisUtils.set(key1,"排课中异常：\n"+e.getMessage());
			}
		};
	}

	@Override
	public void dealMutilArray(List<CGInputData> inputList, NewGkArray newGkArray, String key, String key1){
		if(CollectionUtils.isEmpty(inputList)){
			throw new RuntimeException("排课方案个数为0");
		}

		// fix index 时间点 场地 教师 是否有批次，
		CGInputData mutilInputData = null;
		if(inputList.size() == 1){
			mutilInputData = inputList.get(0);
		}else{
			mutilInputData = combineMutilInput(inputList,newGkArray.getId());
		}

		//
		CGForLectureSolver solver= new CGForLectureSolver();
		String arrayId = newGkArray.getId();
		List<String> arrayIdList = EntityUtils.getList(inputList, e -> e.getArrayId());
		solver.addListener(getListener(key, key1, newGkArray, arrayId,arrayIdList));

		Long dd = new Date().getTime();
		solver.solve(mutilInputData, Evn.isDevModel()?dd:null);
	}

	/**
	 * 将多个input 结合在一起
	 * @param inputList
	 * @param arrayId
	 * @return
	 */
	private CGInputData combineMutilInput(List<CGInputData> inputList, String arrayId) {
		Map<String,NKPeriod> allPeriodMap = new HashMap<>();
		Map<String,NKRoom> allRoomMap = new HashMap<>();
		Map<String,CGTeacher> allTeacherMap = new HashMap<>();
		Map<String,CGCourse> allCourseMap = new HashMap<>();
		// combine
		for (CGInputData data : inputList) {
			data.getPeriodList().forEach(e->{
				if(!allPeriodMap.containsKey(e.getPeriodCode())){
					allPeriodMap.put(e.getPeriodCode(),e);
				}
			});
			data.getRoomList().forEach(e->{
				if(!allRoomMap.containsKey(e.getRoomId())){
					allRoomMap.put(e.getRoomId(),e);
				}
			});
			data.getTeacherList().forEach(e->{
				if(!allTeacherMap.containsKey(e.getCode())){
					allTeacherMap.put(e.getCode(),e);
				}
			});

			for (CGCourse e : data.getCourseList()) {
				e.setCode(data.getArrayId()+"-"+e.getCode());
				if(StringUtils.isNotBlank(e.getIsBiweeklyCourse())){
					e.setIsBiweeklyCourse(data.getArrayId()+"-"+e.getIsBiweeklyCourse());
				}
			}
		}
		CGInputData mutilInput = new CGInputData();
		mutilInput.setArrayIds(new ArrayList<>());
		mutilInput.setArrayId(arrayId);
		mutilInput.setPeriodList(new ArrayList<>(allPeriodMap.values()));
		mutilInput.setRoomList(new ArrayList<>(allRoomMap.values()));
		mutilInput.setTeacherList(new ArrayList<>(allTeacherMap.values()));
		mutilInput.setCourseList(new ArrayList<>(allCourseMap.values()));

		mutilInput.setStudentList(new ArrayList<>());
		mutilInput.setClassList(new ArrayList<>());
		mutilInput.setXzbRoomConstrains(new HashMap<>());
		mutilInput.setMutexTeacherMap(new HashMap<>());
		mutilInput.setNoAttachCourseMap(new HashMap<>());
		mutilInput.setSubjectCoupleTimeMap(new HashMap<>());
		mutilInput.setExistedTable(new HashMap<>());
		mutilInput.setDoClassPeriods(new HashMap<>());
		mutilInput.setNoClassPeriods(new HashMap<>());
		mutilInput.setCombineClassList(new ArrayList<>());
		mutilInput.setMeanwhileClassList(new ArrayList<>());
		mutilInput.setNoMeanWhileClassGroups(new ArrayList<>());

		// replace
		for (CGInputData data : inputList) {
			mutilInput.getArrayIds().add(data.getArrayId());
			mutilInput.appendMsg(data.getMsgString());
			if(data.getArrayId().equals(arrayId)){
				mutilInput.setUnitName(data.getUnitName());
				mutilInput.setMmCount(data.getMmCount());
				mutilInput.setAmCount(data.getAmCount());
				mutilInput.setPmCount(data.getPmCount());
				mutilInput.setNightCount(data.getNightCount());
				mutilInput.setMaxSectionStudentCount(data.getMaxSectionStudentCount());
				mutilInput.setMaxTimeslotIndex(data.getMaxTimeslotIndex());
			}
			//cgcouse
			mutilInput.getCourseList().addAll(data.getCourseList());
			//combineClassList
			mutilInput.getCombineClassList().addAll(data.getCombineClassList());
			// meanWhileClassList
			mutilInput.getMeanwhileClassList().addAll(data.getMeanwhileClassList());
			// nomeanWhileClassList
			mutilInput.getNoMeanWhileClassGroups().addAll(data.getNoMeanWhileClassGroups());

			//cgClass
			data.getClassList().forEach(cls->{
				if(cls.getTeacher() == null){
					return;
				}
//				CGCourse cgCourse = allCourseMap.get(cls.getCourse().getCode());
//				if(cgCourse == null){
//					System.out.println("class--cgCourse 为 null"+cls.getClassName()+" -- "+cls.getCourse().getCode());
//				}
//				cls.setCourse(cgCourse);
				CGTeacher cgTeacher = allTeacherMap.get(cls.getTeacher().getCode());
				if(cgTeacher == null){
					System.out.println("class--cgCourse 为 null"+cls.getClassName()+" -- "+cls.getTeacher().getTeacherName());
				}
				cls.setTeacher(cgTeacher);
			});
			mutilInput.getClassList().addAll(data.getClassList());
			// student
			mutilInput.getStudentList().addAll(data.getStudentList());
			//roomConstrainMap
			for (Entry<CGClass, NKRoom> entry : data.getXzbRoomConstrains().entrySet()) {
				NKRoom nkRoom = allRoomMap.get(entry.getValue().getRoomId());
				if(nkRoom != null){
					mutilInput.getXzbRoomConstrains().put(entry.getKey(),nkRoom);
				}else{
					System.out.println("xzbRoomConstrainMap room null");
				}
			}
			// mutexTeacherMap
			Map<CGTeacher, Set<CGTeacher>> newMutexTeacherMap = mutilInput.getMutexTeacherMap();
			for (Entry<CGTeacher, Set<CGTeacher>> entry : getOrDefault(data.getMutexTeacherMap(),new HashMap<CGTeacher, Set<CGTeacher>>()).entrySet()) {
				CGTeacher newTea = allTeacherMap.get(entry.getKey().getCode());
				if(newTea == null){
					System.out.println("mutexTeamap newTea null");
					continue;
				}
				Set<CGTeacher> cgTeachers = newMutexTeacherMap.get(newTea);
				if(cgTeachers == null){
					newMutexTeacherMap.put(newTea,cgTeachers = new HashSet<>());
				}
				Set<CGTeacher> collect = entry.getValue().stream().filter(e -> allTeacherMap.containsKey(e.getCode()))
						.map(e -> allTeacherMap.get(e.getCode())).collect(Collectors.toSet());
				cgTeachers.addAll(collect);
				if(collect.size() != entry.getValue().size()){
					System.out.println("mutexTeacherMap teacher set null");
				}
			}
			// noAttachCourseMap
			mutilInput.getNoAttachCourseMap().putAll(data.getNoAttachCourseMap());
			// subjectCoupleTimeMap
			for (Entry<CGCourse, List<NKPeriod>> entry : data.getSubjectCoupleTimeMap().entrySet()) {
				List<NKPeriod> collect = entry.getValue().stream().filter(e -> allPeriodMap.containsKey(e.getPeriodCode()))
						.map(e -> allPeriodMap.get(e.getPeriodCode())).collect(Collectors.toList());
				mutilInput.getSubjectCoupleTimeMap().put(entry.getKey(),collect);
				if(collect.size() != entry.getValue().size()){
					System.out.println("subjectCoupleTimeMap Period error");
				}
			}
			// existedTable
			for (Entry<String, List<NKPeriod>> entry : data.getExistedTable().entrySet()) {
				List<NKPeriod> collect = entry.getValue().stream().filter(e -> allPeriodMap.containsKey(e.getPeriodCode()))
						.map(e -> allPeriodMap.get(e.getPeriodCode())).collect(Collectors.toList());
				mutilInput.getExistedTable().put(entry.getKey(),collect);
				if(collect.size() != entry.getValue().size()){
					System.out.println("existTable Period error");
				}
			}
			// doClassPeriods
			for (Entry<CGClass, Set<NKPeriod>> entry : data.getDoClassPeriods().entrySet()) {
				Set<NKPeriod> collect = entry.getValue().stream().filter(e -> allPeriodMap.containsKey(e.getPeriodCode()))
						.map(e -> allPeriodMap.get(e.getPeriodCode())).collect(Collectors.toSet());
				mutilInput.getDoClassPeriods().put(entry.getKey(),collect);
				if(collect.size() != entry.getValue().size()){
					System.out.println("doClassPeriods Period error");
				}
			}
			// noClassPeriods
			for (Entry<CGClass, Set<NKPeriod>> entry : data.getNoClassPeriods().entrySet()) {
				Set<NKPeriod> collect = entry.getValue().stream().filter(e -> allPeriodMap.containsKey(e.getPeriodCode()))
						.map(e -> allPeriodMap.get(e.getPeriodCode())).collect(Collectors.toSet());
				mutilInput.getNoClassPeriods().put(entry.getKey(),collect);
				if(collect.size() != entry.getValue().size()){
					System.out.println("noClassPeriods Period error");
				}
			}
		}
		return mutilInput;
	}

	private <T> T getOrDefault(T obj, T defaultV) {
		return obj==null? defaultV:obj;
	}

	private void appendMsg(StringBuilder exceptionMsg, int msgCounter, String msg) {
		exceptionMsg.append(msgCounter).append(". ").append(msg).append("\n");
	}

	private String checkCombine(Map<String, String> subjectNameMap, Map<String, String> classNameMap,
			Map<String, CGClass> codeClassMap, NewGkClassCombineRelation rela, String csCode) {
		if(!codeClassMap.containsKey(csCode)) {
			String[] code1 = csCode.split("-");
			String subjName = subjectNameMap.get(code1[1]);
			String cname = classNameMap.get(code1[0]);
			if(NewGkElectiveConstant.COMBINE_TYPE_1.equals(rela.getType())) {
				return "    参与合班的 "+cname +"-"+subjName+" 课程不存在 或者 课时为零";
			}
			else {
				return "    参与同时排课的 "+cname +"-"+subjName+" 课程不存在 或者 课时为零";
			}
		}
		return "";
	}

	private String checkXzbRoomConflicts(List<Clazz> clzList, Map<String, List<String>> placeCidMap) {
		Set<String> pIds = new HashSet<>();
		for (Clazz clazz : clzList) {
			String tp = clazz.getTeachPlaceId();
			if(StringUtils.isNotBlank(tp) && placeCidMap.containsKey(tp) && placeCidMap.get(tp).size()>0) {
				pIds.add(tp);
				placeCidMap.get(tp).add(clazz.getClassName());
			}else if(StringUtils.isNotBlank(tp)){
				placeCidMap.put(tp, new ArrayList<>());
				placeCidMap.get(tp).add(clazz.getClassName());
			}
		}
		if(pIds.size()>0) {
			StringBuilder sb = new StringBuilder("以下班级场地相同：");
			for (String string : pIds) {
				List<String> cns = placeCidMap.get(string);
				int i = 0;
				for (String cn : cns) {
					if((i++)!=0)
						sb.append(",");
					sb.append(cn);
				}
				sb.append(";");
			}
			return sb.toString();
		}
		return null;
	}
	
	private Set<NKPeriod> makeCoursePeriodConstrains(Map<String, List<NKPeriod>> subjectNoTime,
			Map<String, List<NKPeriod>> fixedSubjectMap,
			Map<String, CGCourse> nnkCourseMap, Map<CGCourse, List<NKPeriod>> doCoursePeriods,
			Map<CGCourse, List<NKPeriod>> noCoursePeriods) {
		
		if(subjectNoTime.size()>0){
			for(Entry<String, List<NKPeriod>> noTime:subjectNoTime.entrySet()){
				String key12=noTime.getKey();
				List<NKPeriod> value1=noTime.getValue();
				
				if(CollectionUtils.isEmpty(value1)){
					continue;
				}
				
				CGCourse course = nnkCourseMap.get(key12);
				if(course!=null){
					List<NKPeriod> period2List=new ArrayList<NKPeriod>();
					period2List.addAll(value1);
					noCoursePeriods.put(course, period2List);
				}
			}
		}
		
		//固定 课程 时间点
		Set<NKPeriod> fixedPeriods = new HashSet<>();
		for(String courseCode :fixedSubjectMap.keySet()) {
			List<NKPeriod> list1 = fixedSubjectMap.get(courseCode);
			
			if(CollectionUtils.isEmpty(list1)){
				continue;
			}
			
			CGCourse course = nnkCourseMap.get(courseCode);
			if(course!=null){
				List<NKPeriod> period2List=new ArrayList<NKPeriod>();
				period2List.addAll(list1);
				doCoursePeriods.put(course, period2List);
				fixedPeriods.addAll(list1);
			}
		}
		return fixedPeriods;
	}
	
	private String autoAddTeacher(Map<String, String> subjectNameMap,
			Map<String, List<CGClass>> batchClassMap, Set<CGClass> pureClassSet, Set<CGClass> twoXClassSet,
			Map<String, CGTeacher> teacherMap, Map<String, Set<String>> subjectIdTeacherIdMap,
			Map<String, Integer> workLoadMap) {
		List<CGClass> clazzListT;
		String subjectId;
		Set<String> teacherIdsT;
		// 走班班级添加 的老师
		Comparator<String> comparator = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Optional.ofNullable(workLoadMap.get(o1)).orElse(0).compareTo(Optional.ofNullable(workLoadMap.get(o2)).orElse(0)); 
			}
		};
		for (String batchStr : batchClassMap.keySet()) {
			if(CollectionUtils.isEmpty(batchClassMap.get(batchStr))) {
				continue;
			}
			
			Set<String> usedTeachersT = batchClassMap.get(batchStr).stream()
					.filter(e->e.getTeacher()!=null)
					.map(e->e.getTeacher().getCode())
					.collect(Collectors.toSet());
			Map<String, List<CGClass>> subjectIdClazzsMap = batchClassMap.get(batchStr).stream().collect(Collectors.groupingBy(e->e.getCourse().getSubjectId()));
			for (String subjectIdT : subjectIdClazzsMap.keySet()) {
				clazzListT = subjectIdClazzsMap.get(subjectIdT);
				teacherIdsT = subjectIdTeacherIdMap.get(subjectIdT);
				if(CollectionUtils.isEmpty(teacherIdsT)) {
					continue;
				}
				for (CGClass clazz : clazzListT) {
					if(clazz.getTeacher() != null) {
						continue;
					}
					
//						subjectId = clazz.getCourse().getSubjectId();
					String theOneId = teacherIdsT.stream().filter(e->!usedTeachersT.contains(e)).sorted(comparator).findFirst().orElse(null);
					if(theOneId == null) {
						return subjectNameMap.get(subjectIdT) + "教师不够,"+"至少需要此类教师"+clazzListT.size()+"名";
					}
					if(teacherMap.containsKey(theOneId)) {
						clazz.setTeacher(teacherMap.get(theOneId));
					}
					usedTeachersT.add(theOneId);
					
					if(!workLoadMap.containsKey(theOneId)) {
						workLoadMap.put(theOneId, 0);
					}
					workLoadMap.put(theOneId, workLoadMap.get(theOneId)+clazz.getLectureCount());
				}
			}
		}
		
		// 向 与行政班 上课的教学班 添加教师
		Set<CGClass> set3 = new HashSet<>(twoXClassSet);  // 行政班中的 教学班课程
		set3.addAll(pureClassSet);
		set3 = set3.stream().filter(e->!"O".equals(e.getCourse().getSubjectType()))
				.collect(Collectors.toSet());
		
		for (CGClass clazz : set3) {
			if(clazz.getTeacher() != null) {
				continue;
			}
			
			subjectId = clazz.getCourse().getSubjectId();
			teacherIdsT = subjectIdTeacherIdMap.get(subjectId);
			if(CollectionUtils.isEmpty(teacherIdsT)) {
				continue;
			}
			String theOneId = teacherIdsT.stream().sorted(comparator).findFirst().orElse(null);
			if(teacherMap.containsKey(theOneId)) {
				clazz.setTeacher(teacherMap.get(theOneId));
			}
			
			if(!workLoadMap.containsKey(theOneId)) {
				workLoadMap.put(theOneId, 0);
			}
			workLoadMap.put(theOneId, workLoadMap.get(theOneId) + clazz.getCourse().getMinWorkingDaySize());
		}
		
		return null;
	}
	
	private Map<CGClass, NKRoom> makeClassPlaceConstraint(Map<String, List<CGClass>> classIdMap,
			Map<String, NKRoom> roomMap, List<NewGkPlaceItem> newGkPlaceItemList) {
		Map<CGClass, NKRoom> singleRoomConstrains=new HashMap<CGClass, NKRoom>();
		if(CollectionUtils.isNotEmpty(newGkPlaceItemList)){
			List<CGClass> classPlaceList=null;
			NKRoom room=null;
			for(NewGkPlaceItem item:newGkPlaceItemList){
				if(StringUtils.isNotBlank(item.getPlaceId()) && roomMap.containsKey(item.getPlaceId())){
					room = roomMap.get(item.getPlaceId());
					
					if(classIdMap.containsKey(item.getObjectId())){
						classPlaceList = classIdMap.get(item.getObjectId());
						if(CollectionUtils.isNotEmpty(classPlaceList)){
							for (CGClass cgClass : classPlaceList) {
								singleRoomConstrains.put(cgClass,room);
							}
						}
					}
					
				}
			}
		}
		return singleRoomConstrains;
	}
	
	private String validateBtachRoomAndTeachers(Map<String, List<CGClass>> batchClassMap, Map<CGClass, NKRoom> singleRoomConstrains) {
		String msg = null;
		String batchName = "";
		for (String batchStr : batchClassMap.keySet()) {
			List<CGClass> list = batchClassMap.get(batchStr);
			List<CGTeacher> collect = list.stream()
					.filter(e->e.getTeacher()!=null)
					.map(e->e.getTeacher())
					.collect(Collectors.toList());
			
			if(batchStr.startsWith("A")) {
				batchName = batchStr.replace("A", "选考");
			}else if(batchStr.startsWith("B")) {
				batchName = batchStr.replace("B", "学考");
			}else {
				// 不可能
			}
			
			Set<CGTeacher> teachers = new HashSet<>();
			StringBuilder sb = new StringBuilder();
			for (CGTeacher cgTeacher : collect) {
				if(!teachers.contains(cgTeacher)) {
					teachers.add(cgTeacher);
				}else {
					if(sb.length()>0) {
						sb.append(',');
					}
					sb.append(cgTeacher.getTeacherName());
				}
			}
			if(sb.length() > 0) {
				return msg = "在"+batchName+"中出现相同的教师:"+sb+"，请检查教师安排";
			}
			
			// 场地验证;还需要验证 行政班 在批次点上课时 是否和 其他教学班场地 重合 冲突，在 分班方案时 解决 
			Map<NKRoom, List<CGClass>> collect3 = list.stream().filter(e->singleRoomConstrains.containsKey(e))
					.collect(Collectors.groupingBy(e->singleRoomConstrains.get(e)));
			for (NKRoom nkRoom : collect3.keySet()) {
				List<CGClass> list2 = collect3.get(nkRoom);
				if(list2.size()>1) {
					List<String> collect4 = list2.stream().filter(e->e.getParentId()!=null).map(e->e.getParentId()).collect(Collectors.toList());
					list2 = list2.stream().filter(e->!collect4.contains(e.getOldId())).collect(Collectors.toList());
					if(collect4 == null 
							|| collect4.size() < list2.size()
							|| new HashSet<>(collect4).size() >1) {
						return msg = "在"+batchName+"中出现相同的场地，请检查场地安排";
					}
				}
			}
		}
		return msg;
	}
	
	private Map<CGTeacher, Set<CGTeacher>> setMutexTeacher(String unitId,Map<String, CGTeacher> teacherMap,
			List<NewGkLessonTime> teacherNoTimes) {
		Set<String> choiceIds = EntityUtils.getSet(teacherNoTimes, NewGkLessonTime::getId);
		Map<String, NewGkLessonTime> teacherNoTimeMap = EntityUtils.getMap(teacherNoTimes, NewGkLessonTime::getId);
		
		List<NewGkChoRelation> relationList = newGkChoRelationService.findByChoiceIdsAndObjectType(unitId,
				choiceIds.toArray(new String[] {}), NewGkElectiveConstant.CHOICE_TYPE_07); // 
		Map<CGTeacher, Set<CGTeacher>> mutexTeacherMap = new HashMap<>();
		for (NewGkChoRelation relation : relationList) {
			CGTeacher cgTeacher = teacherMap.get(relation.getObjectValue());
			NewGkLessonTime lessonTime = teacherNoTimeMap.get(relation.getChoiceId());
			
			if(cgTeacher != null && lessonTime != null) {
				if(teacherMap.get(lessonTime.getObjectId()) != null) {
					if(!mutexTeacherMap.containsKey(teacherMap.get(lessonTime.getObjectId()))) {
						mutexTeacherMap.put(teacherMap.get(lessonTime.getObjectId()), new HashSet<>());
					}
					mutexTeacherMap.get(teacherMap.get(lessonTime.getObjectId())).add(cgTeacher);
				}else {
					//二次安排教师时，删除了某些教师，教师对应的数据例如 互斥教师  、不排课时间等没有 删除
					logger.error("排课特征中，某些教师删除之后，对应的相关数据没有删除");
				}
				
			}else {
				// 互斥的老师不在 本次排课范围内 可以忽略
//					throw new RuntimeException("error occur in mutexTeacherMap 2");
			}
		}
		return mutexTeacherMap;
	}
	
	private int checkDayLectureCount(Grade grade){
		/**根据年级**/
		int mm = Optional.ofNullable(grade.getMornPeriods()).orElse(0);
		int am = Optional.ofNullable(grade.getAmLessonCount()).orElse(0);
		int pm = Optional.ofNullable(grade.getPmLessonCount()).orElse(0);
		int nm = Optional.ofNullable(grade.getNightLessonCount()).orElse(0);
		
		return  am+pm+nm+mm;
	}
	
	/**
	 * 周课时数据
	 * @param noAttachCourseMap 
	 * @return
	 */
	private boolean makePeriod2(String unitId,String subjectArrangeId,Map<String,NewGkSubjectTime> courseTimeMap, 
			Map<String, Map<String,String>> noAttachCourseMap){
		//周课时
		List<NewGkSubjectTime> newGkSubjectTimeList=newGkSubjectTimeService.findByArrayItemId(subjectArrangeId);
		if(CollectionUtils.isEmpty(newGkSubjectTimeList)){
			return false;
		}
		for(NewGkSubjectTime subjectTime:newGkSubjectTimeList){
			if(subjectTime.getPeriod()==null){
				continue;
			}
			if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(subjectTime.getSubjectType())){
				courseTimeMap.put(subjectTime.getSubjectId(),subjectTime);
			}else{
				courseTimeMap.put(subjectTime.getSubjectId()+subjectTime.getSubjectType(), subjectTime);
			}
		}
		
		// 不联排 课程
		List<NewGkRelateSubtime> relateSubTimeList = relateSubtimeService.findListByItemId(subjectArrangeId);
		for (NewGkRelateSubtime relation : relateSubTimeList) {
			String type = relation.getType();
			
			String[] codes = relation.getSubjectIds().split(",");
			for (int i=0;i<codes.length;i++) {
				String[] code = codes[i].split("-");
				if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(code[1])) {
					codes[i] = code[0];
				}else {
					codes[i] = codes[i].replaceAll("-", "");
				}
			}
			if(!noAttachCourseMap.containsKey(codes[0])) {
				noAttachCourseMap.put(codes[0], new HashMap<>());
			}
			noAttachCourseMap.get(codes[0]).put(codes[1], type);
		}
		return true;
				
	}
	
	/**
	 * 组装年级与科目的时间限制数据
	 * @param newGkArray 
	 * @param gradeNoTime 返回年级限制
	 * @param subjectNoTime 返回科目不排课限制
	 * @param subjectCoupleTime 返回科目必排限制
	 * @param nosubjectIds 返回不参与排课科目
	 * @param classSubjectNoTime 
	 */
	private void makeGradeSubjectTimeLimit(NewGkArray newGkArray,Map<String, NKPeriod> gradeNoTime,
			Map<String, List<NKPeriod>> subjectNoTime, Map<String, List<NKPeriod>> subjectCoupleTime, Map<String, List<NKPeriod>> fixedSubjectMap,
			Map<String, List<NKPeriod>> batchPeriodMap, Set<String> nosubjectIds,Map<String, NKPeriod> allperiodMap, Map<String, List<NKPeriod>> classSubjectNoTime) {
		List<NewGkLessonTime> lessonList = newGkLessonTimeService.findByItemIdObjectId(newGkArray.getLessonArrangeId(), null, null, true);
		if(CollectionUtils.isEmpty(lessonList)){
			return;
		}
		String code;
		for(NewGkLessonTime tt:lessonList){
			if(CollectionUtils.isEmpty(tt.getTimesList())){
				continue;
			}
			if(NewGkElectiveConstant.LIMIT_GRADE_0.equals(tt.getObjectType()) && newGkArray.getGradeId().equals(tt.getObjectId())){
				//年级 不排课时间
				for (NewGkLessonTimeEx ex : tt.getTimesList()) {
					if(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(ex.getTimeType())){
						String timeKey=ex.getDayOfWeek()+"_"+ex.getPeriodInterval()+"_"+ex.getPeriod();
						if(allperiodMap.containsKey(timeKey)){
							gradeNoTime.put(timeKey, allperiodMap.get(timeKey));
						}else{
							/*** 不存在可能性*/
						}
						
					}else{
						/*** 不存在可能性*/
					}
				}
			}else if(NewGkElectiveConstant.LIMIT_GRADE_0.equals(tt.getObjectType())){
				// 固定排课
				code = tt.getObjectId();
				// code += NewGkElectiveConstant.SUBJECT_TYPE_O;
				
				if(!fixedSubjectMap.containsKey(code)){
					fixedSubjectMap.put(code, new ArrayList<>());
				}
				
				for (NewGkLessonTimeEx ex : tt.getTimesList()) {
					String timeKey=ex.getDayOfWeek()+"_"+ex.getPeriodInterval()+"_"+ex.getPeriod();
					
					if(!allperiodMap.containsKey(timeKey)) {
						continue;
					}
					fixedSubjectMap.get(code).add(allperiodMap.get(timeKey));
				}
				
			}else if(NewGkElectiveConstant.LIMIT_SUBJECT_9.equals(tt.getObjectType())){
				// 科目+A/B 不排课时间
				code = tt.getObjectId();
				if(tt.getLevelType() != null && !NewGkElectiveConstant.SUBJECT_TYPE_O.equals(tt.getLevelType())) {
					code = code + tt.getLevelType();
				}
				if(!subjectNoTime.containsKey(code)){
					subjectNoTime.put(code, new ArrayList<>());
				}
				if(!subjectCoupleTime.containsKey(code)){
					subjectCoupleTime.put(code, new ArrayList<>());
				}
				
				/**参加**/
				for (NewGkLessonTimeEx ex : tt.getTimesList()) {
					String timeKey=ex.getDayOfWeek()+"_"+ex.getPeriodInterval()+"_"+ex.getPeriod();
					
					if(!allperiodMap.containsKey(timeKey)) {
						continue;
					}
					if(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(ex.getTimeType())){
						subjectNoTime.get(code).add(allperiodMap.get(timeKey));
					}else if(NewGkElectiveConstant.ARRANGE_TIME_TYPE_02.equals(ex.getTimeType())){
//							subjectYesTime.get(code).add(allperiodMap.get(timeKey));
					}else if(NewGkElectiveConstant.ARRANGE_TIME_TYPE_03.equals(ex.getTimeType())) {
						// 连堂指定时间
						subjectCoupleTime.get(code).add(allperiodMap.get(timeKey));
					}
				}
			}else if(NewGkElectiveConstant.LIMIT_SUBJECT_7.equals(tt.getObjectType())) {
				//年级 批次 时间
				String groupType = tt.getGroupType();
				String level = tt.getLevelType();
				String levelType = null;
				if(NewGkElectiveConstant.DIVIDE_GROUP_5.equals(groupType)) {
					levelType = NewGkElectiveConstant.SUBJECT_TYPE_A + level;
				}else if(NewGkElectiveConstant.DIVIDE_GROUP_6.equals(groupType)) {
					levelType = NewGkElectiveConstant.SUBJECT_TYPE_B + level;
				}else if(NewGkElectiveConstant.DIVIDE_GROUP_7.equals(groupType)) {
					levelType = NewGkElectiveConstant.SUBJECT_TYPE_O;
				}
//				else if("4".equals(groupType)) {
//					levelType = "C" + level;
//				}
				else {
					// 不可能
					continue;
				}
				for(NewGkLessonTimeEx ex : tt.getTimesList()) {
					String timeKey=ex.getDayOfWeek()+"_"+ex.getPeriodInterval()+"_"+ex.getPeriod();
					if(allperiodMap.containsKey(timeKey)) {
						if(!batchPeriodMap.containsKey(levelType)) {
							batchPeriodMap.put(levelType, new ArrayList<>());
						}
						batchPeriodMap.get(levelType).add(allperiodMap.get(timeKey));
					}else {
						
					}
					
				}
				
			}else if(NewGkElectiveConstant.LIMIT_SUBJECT_5.equals(tt.getObjectType())){
				// 班级特征
				code = tt.getObjectId();
				
				if(!classSubjectNoTime.containsKey(code)){
					classSubjectNoTime.put(code, new ArrayList<>());
				}
				
				/**参加**/
				for (NewGkLessonTimeEx ex : tt.getTimesList()) {
					String timeKey=ex.getDayOfWeek()+"_"+ex.getPeriodInterval()+"_"+ex.getPeriod();
					
					if(!allperiodMap.containsKey(timeKey)) {
						continue;
					}
					if(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(ex.getTimeType())){
						classSubjectNoTime.get(code).add(allperiodMap.get(timeKey));
					}else{
						// 班级不排课暂时没有其他选项
					}
				}
				
			}else {
				// 老师不排课时间
//				System.out.println("error occur in lessontime");
			}
		}
		
	}
	
	private void makeFixedSubjectTime(Map<String, NewGkSubjectTime> courseTimeMap,
			Map<String, List<NKPeriod>> fixedSubjectMap, Set<String> cSubjectId) {
		// 加入固定排课的科目
		NewGkSubjectTime subjectTime;
		for (String subjectId : fixedSubjectMap.keySet()) {
			subjectTime = courseTimeMap.get(subjectId);
			int size = fixedSubjectMap.get(subjectId).size();
			if(subjectTime==null) {
				subjectTime = new NewGkSubjectTime();
				subjectTime.setSubjectId(subjectId);
				subjectTime.setIsNeed(1);
				subjectTime.setTimeInterval(0.0f);
				subjectTime.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_O);
				subjectTime.setWeekRowType("0");
				// 是否需要 场地
				subjectTime.setIsNeed(1);
				
				courseTimeMap.put(subjectId, subjectTime);
			}
			
			subjectTime.setFixedSubject(true);
			subjectTime.setPeriod(size);
		}
	}
	
	private Set<String> makeCourses(Map<String, NewGkSubjectTime> courseTimeMap, List<CGCourse> nnkCourseList,
			Map<String, CGCourse> nnkCourseMap, Set<String> cSubjectId, Map<String, String> subjectNameMap) {
		CGCourse nnkCourse=null;
		Set<String> xzbcode=new HashSet<String>();//所有学生要上的subjectId
		
		for (String key : courseTimeMap.keySet()) {
			String keyLin=key;
			NewGkSubjectTime newGkSubjectTime = courseTimeMap.get(key);
			String subjectType = newGkSubjectTime.getSubjectType();
			
			if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(subjectType)) {
				nnkCourse=new CGCourse();
				nnkCourse.setSubjectId(newGkSubjectTime.getSubjectId());
				nnkCourse.setCode(subjectNameMap.get(nnkCourse.getSubjectId()));//subjectName
				nnkCourse.setType(CGCourse.CourseType.COMPULSORY_COURSE);
				nnkCourse.setSubjectType(subjectType);

				makePeriodTimes(courseTimeMap, keyLin, nnkCourse);
				if(!Objects.equals(NewGkElectiveConstant.IF_INT_1,newGkSubjectTime.getFollowZhb())){
					xzbcode.add(keyLin);
				}

				nnkCourseList.add(nnkCourse);
				nnkCourseMap.put(keyLin, nnkCourse);
			}
			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
				nnkCourse = new CGCourse();
				nnkCourse.setSubjectId(newGkSubjectTime.getSubjectId());
				nnkCourse.setCode(subjectNameMap.get(nnkCourse.getSubjectId()) + subjectType);// subjectName
				nnkCourse.setType(CGCourse.CourseType.MAJOR_COURSE);
				nnkCourse.setSubjectType(subjectType);
				makePeriodTimes(courseTimeMap, keyLin, nnkCourse);
				nnkCourseList.add(nnkCourse);
				nnkCourseMap.put(keyLin, nnkCourse);
			}
			if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType)) {
				nnkCourse = new CGCourse();
				nnkCourse.setSubjectId(newGkSubjectTime.getSubjectId());
				nnkCourse.setCode(subjectNameMap.get(nnkCourse.getSubjectId()) + subjectType);// subjectName
				nnkCourse.setType(CGCourse.CourseType.MINOR_COURSE);
				nnkCourse.setSubjectType(subjectType);
				makePeriodTimes(courseTimeMap, keyLin, nnkCourse);
				nnkCourseList.add(nnkCourse);
				nnkCourseMap.put(keyLin, nnkCourse);
			}
		}
		return xzbcode;
	}
	
	private void makePeriodTimes(Map<String,NewGkSubjectTime> courseTimeMap,String key,CGCourse nnkCourse){
		NewGkSubjectTime courseTime = courseTimeMap.get(key);
		if(courseTime!=null){
			nnkCourse.setMinWorkingDaySize(courseTime.getPeriod()==null?0:courseTime.getPeriod());
			
			// 周连排次数 连排节次 暂时只支持2 
			if(courseTime.getWeekRowPeriod()!=null && courseTime.getWeekRowPeriod()>0){
				nnkCourse.setCount(courseTime.getWeekRowPeriod());
			}
			// 周连排次数
			if(courseTime.getWeekRowNumber()!=null && courseTime.getWeekRowNumber()>0){
				nnkCourse.setTimes(courseTime.getWeekRowNumber());
			}
			nnkCourse.setCoupleTimeLimit(courseTime.getWeekRowType());
			
			// 是否需要 场地
			nnkCourse.setNeedRoom(courseTime.getIsNeed());
			if(nnkCourse.getNeedRoom() == null) {
				nnkCourse.setNeedRoom(1);
			}
			// 单双周
			nnkCourse.setIsBiWeekly(upWeekType(courseTime.getFirstsdWeek()));
			// 单双周 绑定科目
			if(nnkCourse.getIsBiWeekly() != CGCourse.WEEK_TYPE_NORMAL && StringUtils.isNotBlank(courseTime.getFirstsdWeekSubject())) {
				nnkCourse.setIsBiweeklyCourse(courseTime.getFirstsdWeekSubject());
			}
			
			// 课时分配
			if(StringUtils.isNotBlank(courseTime.getArrangeDay())) {
				nnkCourse.setArrangeDay(courseTime.getArrangeDay());
			}else {
				nnkCourse.setArrangeDay("01");
			}
			// 半天课时分配
			if(StringUtils.isNotBlank(courseTime.getArrangeHalfDay())) {
				nnkCourse.setArrangeHalfDay(courseTime.getArrangeHalfDay());
			}else {
				nnkCourse.setArrangeHalfDay("01");
			}
			// 优先级
			if("1".equals(courseTime.getArrangeFrist())) {
				nnkCourse.setPriority(3);
			}
			nnkCourse.setFixedSubject(courseTime.isFixedSubject());
		}else{
			// 周课时为0的科目
			nnkCourse.setMinWorkingDaySize(0);
			nnkCourse.setIsBiWeekly(2);
			nnkCourse.setArrangeHalfDay("01");
			nnkCourse.setArrangeDay("01");
			nnkCourse.setCoupleTimeLimit("0");
		}
	}

	private int upWeekType(Integer newgkWeekType) {
		if(Objects.equals(NewGkElectiveConstant.WEEK_TYPE_EVEN,newgkWeekType)){
			return (CGCourse.WEEK_TYPE_EVEN);
		}else if(Objects.equals(NewGkElectiveConstant.WEEK_TYPE_ODD,newgkWeekType)){
			return (CGCourse.WEEK_TYPE_ODD);
		}else{
			return (CGCourse.WEEK_TYPE_NORMAL);
		}
	}

	private Map<CGCourse, Map<CGCourse,String>> makeNoAttachCourses(Map<String,Map<String,String>> noAttachCourseCodeMap,
			Map<String, CGCourse> nnkCourseMap) {
		CGCourse nnkCourse;
		CGCourse nnkCourse2;
		Map<CGCourse, Map<CGCourse,String>> noAttachCourseMap = new HashMap<>();
		for (String code : noAttachCourseCodeMap.keySet()) {
			Map<String, String> map = noAttachCourseCodeMap.get(code);
			nnkCourse = nnkCourseMap.get(code);
			
			if(nnkCourse == null) {
				throw new RuntimeException("code:"+code+"找不到 对应课程");
			}
			
			Map<CGCourse,String> hashMap = new HashMap<>();
			noAttachCourseMap.put(nnkCourse, hashMap);
			for (String code2 : map.keySet()) {
				nnkCourse2 = nnkCourseMap.get(code2);
				if(nnkCourse2 == null) {
					throw new RuntimeException("code:"+code2+"找不到 对应课程");
				}
				hashMap.put(nnkCourse2, map.get(code2));
			}
		}
		return noAttachCourseMap;
	}
	
	/**
	 * 7选3 组装 班级学生数据
	 * @param nnkCourseMap
	 * @param aSubjectId
	 * @param xzbcode
	 * @param allClassList
	 * @param batchClassMap
	 * @param studentMap
	 * @param bSubjectIds
	 * @param exceptionMsg
	 * @param classTimeMap
	 * @param childrenClasrelaMap 
	 * @return
	 */
	private List<CGClass> makeClassAndStus(Map<String, CGCourse> nnkCourseMap, Set<String> aSubjectId, Set<String> xzbcode,
			List<NewGkDivideClass> allClassList, Map<String, List<CGClass>> batchClassMap,Map<String, CGStudent> studentMap,
			 Set<String> bSubjectIds,Map<String, NewGkClassSubjectTime> classTimeMap, Map<String, List<String>> childrenClasrelaMap, 
			 NewGkArray array, NewGkDivide divide,StringBuilder exceptionMsg) {
		
		List<NewGkDivideClass> zhbClassList = allClassList.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_0))
				.collect(Collectors.toList());
		Map<String, NewGkDivideClass> zhbMap = EntityUtils.getMap(zhbClassList, NewGkDivideClass::getId);
//		List<NewGkDivideClass> batchJxbList = allClassList.stream()
//				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())
//					&& StringUtils.isNotBlank(e.getBatch())
//					&& StringUtils.isNotBlank(e.getRelateId()))
//				.collect(Collectors.toList());
		List<NewGkDivideClass> xzbList = allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
				.collect(Collectors.toList());
		allClassList.removeAll(zhbClassList);
		boolean hasJxb = allClassList.stream().anyMatch(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())
				&&StringUtils.isNotBlank(e.getBatch()));
		Map<String,List<String>> relaSubjmap = new HashMap<>();
		for (String pcid : childrenClasrelaMap.keySet()) {
			List<String> list = childrenClasrelaMap.get(pcid);
			relaSubjmap.put(pcid+"A", list.stream().map(e->e+"A").filter(e->nnkCourseMap.containsKey(e)).collect(Collectors.toList()));
			relaSubjmap.put(pcid+"B", list.stream().map(e->e+"B").filter(e->nnkCourseMap.containsKey(e)).collect(Collectors.toList()));
		}
		Set<String> childCids = childrenClasrelaMap.values().stream().flatMap(e->e.stream()).collect(Collectors.toSet());
		aSubjectId.removeAll(childCids);
		bSubjectIds.removeAll(childCids);
		
		Set<NewGkDivideClass> chdClas = allClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())
				&& StringUtils.isNotBlank(e.getParentId()))
				.collect(Collectors.toSet());
		long count2 = chdClas.stream().filter(e->nnkCourseMap.containsKey(e.getSubjectIds()+e.getSubjectType())).count();
		Set<String> parentIds = new HashSet<>();
		if(count2 > 0)
			parentIds = chdClas.stream().map(e->e.getParentId()).collect(Collectors.toSet());
		
		
		// TODO 获取每个班级 的 上课科目
		List<String> xzbIds = EntityUtils.getList(xzbList, e->e.getId());
		Map<String, List<String[]>> xzbSubjects = newGkDivideClassService.findXzbSubjects(array.getUnitId(), array.getDivideId(), 
				array.getLessonArrangeId(), NewGkElectiveConstant.CLASS_SOURCE_TYPE1, xzbIds);
		List<String> xzbIds2 = allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType()))
				.map(e->e.getId())
				.collect(Collectors.toList());
		Map<String, List<String[]>> fakeXzbSubjects = newGkDivideClassService.findFakeXzbSubjects(array.getUnitId(), array.getDivideId(), 
				array.getLessonArrangeId(), NewGkElectiveConstant.CLASS_SOURCE_TYPE1, xzbIds2);
		
		String followType = Optional.ofNullable(divide.getFollowType()).orElse("");
		if(divide.getOpenType().equals("12")) {
			followType = "A-2,B-2";
		}
		
		Map<CGCourse,Integer> classNumIndex=new HashMap<CGCourse,Integer>();
		List<CGClass> classList = new ArrayList<>();
		Set<String> xzbCodesT;
		CGClass chClass=null;
		List<CGStudent> nKStudentList=null;
		StringBuilder sb = new StringBuilder();
		for(NewGkDivideClass item:allClassList){
			
			if(parentIds.contains(item.getId())
					|| (hasJxb && CollectionUtils.isEmpty(item.getStudentList()))){
				continue;
			}
			// 没有为拆分的班级 设置课时
			if(NewGkElectiveConstant.CLASS_TYPE_2.equals(item.getClassType())&& parentIds.size()<1 && StringUtils.isNotBlank(item.getParentId())) {
				continue;
			}
			if(NewGkElectiveConstant.CLASS_TYPE_2.equals(item.getClassType()) && StringUtils.isBlank(item.getBatch())) {
				continue;
			}
			
			List<CGCourse> stuCourselist=new ArrayList<>();
			List<CGClass> itemList=new ArrayList<>();
//			double countPeriod=0;
			AtomicDouble countPeriod = new AtomicDouble();
			
			nKStudentList=new ArrayList<>();
			boolean pure3 = false;
//			Map<String,String> codeBatchMap = new HashMap<>();
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(item.getClassType())){
				if(StringUtils.isNotBlank(item.getRelateId())) {
					NewGkDivideClass zhbClass = zhbMap.get(item.getRelateId());
					String[] subjectIdsT = zhbClass.getSubjectIds().split(",");
					if(subjectIdsT.length > 2) {
						// 3科组合班
						// 暂时用不到 但是算法里面有这个字段 可能用到了；先保留
						pure3 = true;
					}
				}
				
				List<String[]> list = xzbSubjects.get(item.getId());
				xzbCodesT = list.stream()
						.map(e->{
							if(xzbcode.contains(e[0]))
								return e[0];
							else
								return e[0]+e[1];
						}).collect(Collectors.toSet());
				xzbCodesT.addAll(xzbcode);
				for(String xzbSubject: xzbCodesT){
					chClass = makeBasicClass(item, xzbSubject, nnkCourseMap, pure3, classTimeMap, classNumIndex,countPeriod
							,classList,itemList,stuCourselist,sb);
				}
				
			}else if(NewGkElectiveConstant.CLASS_TYPE_2.equals(item.getClassType())){
				chClass = makeBasicClass(item, item.getSubjectIds()+item.getSubjectType(), nnkCourseMap,
						pure3, classTimeMap, classNumIndex,countPeriod,classList,itemList,stuCourselist,sb);
			}else if(NewGkElectiveConstant.CLASS_TYPE_3.equals(item.getClassType())) {
				// 合班教学班
				if(followType.contains(NewGkElectiveConstant.FOLLER_TYPE_A2)) {
					// 选考跟随行政班上课
					chClass = makeBasicClass(item, item.getSubjectIds()+NewGkElectiveConstant.SUBJECT_TYPE_A, nnkCourseMap,
							pure3, classTimeMap, classNumIndex,countPeriod,classList,itemList,stuCourselist,sb);
				}else {
//					// 班级 走班上课
//					continue;
				}
				if(followType.contains(NewGkElectiveConstant.FOLLER_TYPE_B2)) {
					// 学考按照行政班级上课
					chClass = makeBasicClass(item, item.getSubjectIdsB()+NewGkElectiveConstant.SUBJECT_TYPE_B, nnkCourseMap,
							pure3, classTimeMap, classNumIndex,countPeriod,classList,itemList,stuCourselist,sb);
				}
			}else if(NewGkElectiveConstant.CLASS_TYPE_4.equals(item.getClassType())) {
				// 组合固定班级
				List<String[]> list = fakeXzbSubjects.get(item.getId());
				xzbCodesT = list.stream()
						.map(e->{
							if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(e[1])){
								return e[0];
							}else{
								return e[0]+e[1];
							}
						}).collect(Collectors.toSet());
				for(String xzbSubject: xzbCodesT){
					chClass = makeBasicClass(item, xzbSubject, nnkCourseMap, pure3, classTimeMap, classNumIndex,countPeriod
							,classList,itemList,stuCourselist,sb);
				}
			}
			if(CollectionUtils.isEmpty(itemList)) {
				continue;
			}
			for(String s:item.getStudentList()){
				CGStudent nKStudent=null;
				if(studentMap.containsKey(s)){
					nKStudent=studentMap.get(s);
					nKStudent.getCourseList().addAll(stuCourselist);
					nKStudent.setLectureCount(nKStudent.getLectureCount()+countPeriod.get());
					nKStudentList.add(nKStudent);
				}else{
					nKStudent=new CGStudent();
					nKStudent.setStudentId(s);
					nKStudent.setCourseList(new ArrayList<CGCourse>());
					nKStudent.getCourseList().addAll(stuCourselist);
					nKStudent.setLectureCount(countPeriod.get());
					nKStudentList.add(nKStudent);
					studentMap.put(s, nKStudent);				
				}
			}
			
			for(CGClass item1:itemList){
				item1.setStudentList(new ArrayList<CGStudent>());
				item1.getStudentList().addAll(nKStudentList);
				
				if(NewGkElectiveConstant.CLASS_TYPE_2.equals(item.getClassType()) && StringUtils.isNotBlank(item1.getBatch())) {
					// 有批次的班级 存批次
					if(!batchClassMap.containsKey(item1.getCourse().getSubjectType()+item1.getBatch())) {
						batchClassMap.put(item1.getCourse().getSubjectType()+item1.getBatch(), new ArrayList<>());
					}
					batchClassMap.get(item1.getCourse().getSubjectType()+item1.getBatch()).add(item1);
				}
			}
		}
		
		return classList;
	}
	
	private CGClass makeBasicClass(NewGkDivideClass item, String subjectCode, Map<String, CGCourse> nnkCourseMap, boolean pure3,
			Map<String, NewGkClassSubjectTime> classTimeMap,Map<CGCourse,Integer> classNumIndex, AtomicDouble countPeriod,
			List<CGClass> classList, List<CGClass> itemList, List<CGCourse> stuCourselist, StringBuilder sb) {
		boolean isJxb = NewGkElectiveConstant.CLASS_TYPE_2.equals(item.getClassType());
		CGCourse course = nnkCourseMap.get(subjectCode);
		if(course == null) {
			if(isJxb &&item.getParentId()!=null) {
				// 被拆分班级
				return null;
			}
			sb.append("    ").append(item.getClassName()+"找不到"+item.getClassName()+"<span style=\"display:none;\">"+subjectCode+"</span>对应的科目").append("\n");
//			throw new RuntimeException(item.getClassName()+"找不到"+item.getClassName()+"<span style=\"display:none;\">"+subjectCode+"</span>对应的科目");
			return null;
		}
		CGClass chClass = new CGClass();
		chClass.setLectureCount(course.getMinWorkingDaySize());
		
		NewGkClassSubjectTime classPeriod = null;
		if((classPeriod = classTimeMap.get(item.getId()+"-"+course.getSubjectId()+"-"+course.getSubjectType())) != null) {
			chClass.setLectureCount(classPeriod.getPeriod());
			chClass.setIsBiWeekly(upWeekType(classPeriod.getWeekType()));
		}
		
		if(chClass.getLectureCount() < 1){
			return null;
		}
		if(course.getIsBiWeekly() != CGCourse.WEEK_TYPE_NORMAL) {
			countPeriod.addAndGet(chClass.getLectureCount() - 0.5);
		}else {
			countPeriod.addAndGet(chClass.getLectureCount());
		}
		chClass.setCourse(course);
		chClass.setJxbId(UuidUtils.generateUuid());
		chClass.setFixed(true);
		if(isJxb) {
			chClass.setJxbId(item.getId());
			chClass.setFixed(false);
			chClass.setParentId(item.getParentId());
		}
		chClass.setOldId(item.getId());
		chClass.setClassName(item.getClassName());
		
		chClass.setBatch(item.getBatch());
		if(classNumIndex.containsKey(course)){
			classNumIndex.put(course, classNumIndex.get(course)+1);
			chClass.setJxbIndex(classNumIndex.get(course));
		}else{
			chClass.setJxbIndex(1);
			classNumIndex.put(course, 1);
		}
		if(pure3) {
			chClass.setPure3(pure3);
		}
		classList.add(chClass);
		itemList.add(chClass);
		stuCourselist.add(course);
		return chClass;
	}

	@Override
	public void saveResult2(CGInputData cgInputData){
		List<CGSectionLecture> lectureList = cgInputData.getLectureList();
		
		String arrayId = cgInputData.getArrayId();
		NewGkArray newGKArray = newGkArrayService.findOneWithMaster(arrayId);
		String gradeId = newGKArray.getGradeId();
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId),Grade.class);
		
		List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(newGKArray.getUnitId(), 
				arrayId, null, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		
		
		// 不复制班级 改为 将原分班的id 转为 排课方案中的分班id
		Map<String, NewGkDivideClass> oldNewClassIdMap = EntityUtils.getMap(allClassList, NewGkDivideClass::getOldDivideClassId);
		
		
		int mm = grade.getAmLessonCount()==null?0:grade.getMornPeriods();
		int am = grade.getAmLessonCount()==null?0:grade.getAmLessonCount();
		int pm = grade.getPmLessonCount()==null?0:grade.getPmLessonCount();
		int nm = grade.getNightLessonCount()==null?0:grade.getNightLessonCount();
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put(BaseConstants.PERIOD_INTERVAL_1, mm);
		map.put(BaseConstants.PERIOD_INTERVAL_2, am);
		map.put(BaseConstants.PERIOD_INTERVAL_3, pm);
		map.put(BaseConstants.PERIOD_INTERVAL_4, nm);
		// 转换为 课程表 newgkelective_week_schedule
		Map<CGCourseSection, List<CGSectionLecture>> sectionToLecture = EntityUtils.getListMap(lectureList, CGSectionLecture::getCourseSection, e->e);
		Map<String,NewGkTimetable> timetableMap = new HashMap<>();
		List<NewGkTimetable> timeTableList = new ArrayList<NewGkTimetable>();
		List<NewGkTimetableOther> timeTableOtherList = new ArrayList<NewGkTimetableOther>();
		List<NewGkTimetableTeacher> teacherList = new ArrayList<NewGkTimetableTeacher>();
		Date now = new Date();
		for (Entry<CGCourseSection, List<CGSectionLecture>> entry : sectionToLecture.entrySet()) {
			CGCourseSection cgCourseSection = entry.getKey();
			List<CGSectionLecture> lectureList2 = entry.getValue();
			CGCourse course = cgCourseSection.getCourse();
			String subjectType = course.getSubjectType();
			
			// 构造timetableList
			NewGkDivideClass newClass = oldNewClassIdMap.get(cgCourseSection.getOldId());
			NewGkTimetable newGkTimetable = timetableMap.get(newClass.getId()+course.getSubjectId());
			if(newGkTimetable == null) {
				newGkTimetable = new NewGkTimetable();
				
				newGkTimetable.setArrayId(arrayId);
				newGkTimetable.setClassId(newClass.getId());
				newGkTimetable.setClassType(newClass.getClassType());
				newGkTimetable.setSubjectId(course.getSubjectId());
				newGkTimetable.setSubjectType(subjectType);
				
				newGkTimetable.setId(UuidUtils.generateUuid());
				newGkTimetable.setCreationTime(now);
				newGkTimetable.setModifyTime(now);
				newGkTimetable.setUnitId(grade.getSchoolId());
				
				timeTableList.add(newGkTimetable);
				// 构造 教师List
				if(cgCourseSection.getTeacher() != null){
					if(StringUtils.isBlank(cgCourseSection.getTeacher().getCode())){
						continue;
					}
					NewGkTimetableTeacher timeTableTeacher = new NewGkTimetableTeacher();
					timeTableTeacher.setId(UuidUtils.generateUuid());
					timeTableTeacher.setTimetableId(newGkTimetable.getId());
					timeTableTeacher.setTeacherId(cgCourseSection.getTeacher().getCode());
					teacherList.add(timeTableTeacher);
				}
			}
			
			
			// 构造timetableother
			for (CGSectionLecture cgSectionLecture : lectureList2) {
				int cc = 1;
				for(int i =0;i<cc;i++){
					NewGkTimetableOther newGkTimetableOther = generateTimeTableOther(
							grade, newGkTimetable, cgSectionLecture, i, map);
					if(newGkTimetableOther != null)
						timeTableOtherList.add(newGkTimetableOther);
				}
			}
		}
		
		List<NewGkTimetable> oldlist=newGkTimetableService.findByArrayId(newGKArray.getUnitId(), arrayId);
		NewGkArrayResultSaveDto dto=new NewGkArrayResultSaveDto();
		dto.setInsertOtherList(timeTableOtherList);
		dto.setInsertTimeTableList(timeTableList);
		dto.setInsertTeacherList(teacherList);
		if(CollectionUtils.isNotEmpty(oldlist)){
			Set<String> set = EntityUtils.getSet(oldlist, NewGkTimetable::getId);
			dto.setTimeTableIds(set.toArray(new String[]{}));
		}
		dto.setArrayId(arrayId);
		dto.setStat(NewGkElectiveConstant.IF_OTHER_2);
		newGkTimetableService.saveAll(newGKArray.getUnitId(), newGKArray.getDivideId(), dto);
	}

	/**
	 * 临时 跨年级排课使用
	 * @param cgInputData
	 * @param arrayIds
	 */
	@Override
	public void saveResult3(CGInputData cgInputData, List<String> arrayIds){
		List<CGSectionLecture> lectureList = cgInputData.getLectureList();

		String arrayId = cgInputData.getArrayId();
		NewGkArray newGKArray = newGkArrayService.findOneWithMaster(arrayId);
		String unitId = newGKArray.getUnitId();
		String gradeId = newGKArray.getGradeId();
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId),Grade.class);
		if(CollectionUtils.isEmpty(arrayIds)){
			arrayIds = Stream.of(arrayId).collect(Collectors.toList());
		}

		List<NewGkDivideClass> allClassList = new ArrayList<>();
		for (String arId : arrayIds) {
			List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(unitId,
					arId, null, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
			allClassList.addAll(classList);
		}


		// 不复制班级 改为 将原分班的id 转为 排课方案中的分班id
		Map<String, NewGkDivideClass> oldNewClassIdMap = EntityUtils.getMap(allClassList, NewGkDivideClass::getOldDivideClassId);


		int mm = grade.getAmLessonCount()==null?0:grade.getMornPeriods();
		int am = grade.getAmLessonCount()==null?0:grade.getAmLessonCount();
		int pm = grade.getPmLessonCount()==null?0:grade.getPmLessonCount();
		int nm = grade.getNightLessonCount()==null?0:grade.getNightLessonCount();

		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put(BaseConstants.PERIOD_INTERVAL_1, mm);
		map.put(BaseConstants.PERIOD_INTERVAL_2, am);
		map.put(BaseConstants.PERIOD_INTERVAL_3, pm);
		map.put(BaseConstants.PERIOD_INTERVAL_4, nm);
		// 转换为 课程表 newgkelective_week_schedule
		Map<CGCourseSection, List<CGSectionLecture>> sectionToLecture = EntityUtils.getListMap(lectureList, CGSectionLecture::getCourseSection, e->e);
		Map<String,NewGkTimetable> timetableMap = new HashMap<>();
		List<NewGkTimetable> timeTableList = new ArrayList<>();
		List<NewGkTimetableOther> timeTableOtherList = new ArrayList<>();
		List<NewGkTimetableTeacher> teacherList = new ArrayList<>();
		Date now = new Date();
		for (Entry<CGCourseSection, List<CGSectionLecture>> entry : sectionToLecture.entrySet()) {
			CGCourseSection cgCourseSection = entry.getKey();
			List<CGSectionLecture> lectureList2 = entry.getValue();
			CGCourse course = cgCourseSection.getCourse();
			String subjectType = course.getSubjectType();

			// 构造timetableList
			NewGkDivideClass newClass = oldNewClassIdMap.get(cgCourseSection.getOldId());
			NewGkTimetable newGkTimetable = timetableMap.get(newClass.getId()+course.getSubjectId());
			if(newGkTimetable == null) {
				newGkTimetable = new NewGkTimetable();

				String arrayIdT = newClass.getDivideId();
				newGkTimetable.setArrayId(arrayIdT);
				newGkTimetable.setClassId(newClass.getId());
				newGkTimetable.setClassType(newClass.getClassType());
				newGkTimetable.setSubjectId(course.getSubjectId());
				newGkTimetable.setSubjectType(subjectType);

				newGkTimetable.setId(UuidUtils.generateUuid());
				newGkTimetable.setCreationTime(now);
				newGkTimetable.setModifyTime(now);
				newGkTimetable.setUnitId(grade.getSchoolId());

				timeTableList.add(newGkTimetable);
				// 构造 教师List
				if(cgCourseSection.getTeacher() != null){
					if(StringUtils.isBlank(cgCourseSection.getTeacher().getCode())){
						continue;
					}
					NewGkTimetableTeacher timeTableTeacher = new NewGkTimetableTeacher();
					timeTableTeacher.setId(UuidUtils.generateUuid());
					timeTableTeacher.setTimetableId(newGkTimetable.getId());
					timeTableTeacher.setTeacherId(cgCourseSection.getTeacher().getCode());
					teacherList.add(timeTableTeacher);
				}
			}


			// 构造timetableother
			for (CGSectionLecture cgSectionLecture : lectureList2) {
				int cc = 1;
				for(int i =0;i<cc;i++){
					NewGkTimetableOther newGkTimetableOther = generateTimeTableOther(
							grade, newGkTimetable, cgSectionLecture, i, map);
					if(newGkTimetableOther != null)
						timeTableOtherList.add(newGkTimetableOther);
				}
			}
		}

		List<NewGkTimetable> oldlist= new ArrayList<>();
		for (String arId : arrayIds) {
			newGkArrayService.updateStatById(NewGkElectiveConstant.IF_OTHER_2, arId);
			List<NewGkTimetable> oldlist2= newGkTimetableService.findByArrayId(newGKArray.getUnitId(), arId);
			oldlist.addAll(oldlist2);
		}
		NewGkArrayResultSaveDto dto=new NewGkArrayResultSaveDto();
		dto.setInsertOtherList(timeTableOtherList);
		dto.setInsertTimeTableList(timeTableList);
		dto.setInsertTeacherList(teacherList);
		if(CollectionUtils.isNotEmpty(oldlist)){
			Set<String> set = EntityUtils.getSet(oldlist, NewGkTimetable::getId);
			dto.setTimeTableIds(set.toArray(new String[]{}));
		}
		dto.setArrayId(arrayId);
//		dto.setStat(NewGkElectiveConstant.IF_OTHER_2);
		newGkTimetableService.saveAll(newGKArray.getUnitId(), newGKArray.getDivideId(), dto);
	}

	
	@Override
	public Map<String, NewGkDivideClass> copyDivideClassToArray(List<NewGkDivideClass> oldClassList, NewGkArray array,
			String newSourceType, List<NewGkClassStudent> newStuList, List<NewGkDivideClass> newClassList) {
		Map<String, NewGkDivideClass> oldNewClassIdMap = new HashMap<String, NewGkDivideClass>();
		String newDivideId = array.getId();
		for (NewGkDivideClass oldClass : oldClassList) {
			if(NewGkElectiveConstant.CLASS_TYPE_2.equals(oldClass)
					&& StringUtils.isBlank(oldClass.getOldDivideClassId())){
				continue;
			}

			List<String> studentList = oldClass.getStudentList();
			NewGkDivideClass newClass = copyBasicClassStu(newDivideId, newSourceType, newStuList, newClassList,
					oldClass, studentList);
			oldNewClassIdMap.put(oldClass.getId(), newClass);
			// 保存新的教学班
		}

		// 更新relatedId
		for (NewGkDivideClass newClass : newClassList) {
			String relateId = newClass.getRelateId();
			if(StringUtils.isNotBlank(relateId)) {
				String newRelateId = Stream.of(relateId.split(",")).filter(e->oldNewClassIdMap.containsKey(e))
						.map(e->oldNewClassIdMap.get(e).getId())
						.collect(Collectors.joining(","));
				newClass.setRelateId(newRelateId);
			}

			if(oldNewClassIdMap.containsKey(newClass.getParentId())) {
				newClass.setParentId(oldNewClassIdMap.get(newClass.getParentId()).getId());
			}
		}
		// 针对在组合班级上课的 行政班课程开班
		buildByZhbSpecialCourse(array, newSourceType, newStuList, newClassList, newDivideId);

		if(CollectionUtils.isNotEmpty(oldClassList)) {
			final String unitId = array.getUnitId();
			newStuList.forEach(s->{
				s.setUnitId(unitId);
				s.setDivideId(newDivideId);
			});
		}

		return oldNewClassIdMap;
	}

	private void buildByZhbSpecialCourse(NewGkArray array, String newSourceType, List<NewGkClassStudent> newStuList, List<NewGkDivideClass> newClassList, String newDivideId) {
		String divideId = array.getDivideId();
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		if(NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())){
			List<NewGkSubjectTime> stList = newGkSubjectTimeService.findByArrayItemIdAndSubjectTypeIn(array.getLessonArrangeId()
					, new String[]{NewGkElectiveConstant.SUBJECT_TYPE_O});
			List<String> subIds = stList.stream().filter(e -> Objects.equals(NewGkElectiveConstant.IF_INT_1, e.getFollowZhb()))
					.map(e -> e.getSubjectId())
					.collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(subIds)){
				Map<String, String> subNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(subIds.toArray(new String[0])),
						new TypeReference<Map<String, String>>() {});
				newClassList.stream()
						.filter(e->NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType()))
						.collect(Collectors.toList())
						.forEach(cls->{
							List<String> studentList = cls.getStudentList();
							for (String subId:subIds){
								NewGkDivideClass newClass = copyBasicClassStu(newDivideId, newSourceType, newStuList, newClassList,
										cls, studentList);
								newClass.setClassType(NewGkElectiveConstant.CLASS_TYPE_2);
								newClass.setSubjectIds(subId);
								newClass.setSubjectIdsB(null);
								newClass.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_O);
								newClass.setRelateId(cls.getId());
								newClass.setOldDivideClassId(null);
								newClass.setParentId(null);
								newClass.setBatch(null);
								String s = subNameMap.get(subId);
								if(StringUtils.isNotBlank(s)){
									newClass.setClassName(cls.getClassName()+"-"+s);
								}else{
									newClass.setClassName(cls.getClassName()+"-未知");
								}
							}
						});
			}
		}
	}

	private NewGkDivideClass copyBasicClassStu(String newDivideId, String newSourceType,
			List<NewGkClassStudent> newStuList, List<NewGkDivideClass> newClassList, NewGkDivideClass oldClass,
			List<String> studentList) {
		NewGkDivideClass newClass = generateNewClass(newDivideId, oldClass);
		newClass.setSourceType(newSourceType);
		newClassList.add(newClass);
		if(CollectionUtils.isNotEmpty(studentList)) {
			makeClassStudent(newStuList, studentList, newClass.getId());
			newClass.setStudentList(studentList);
		}
		return newClass;
	}
	
	private NewGkTimetableOther generateTimeTableOther(Grade grade,
			NewGkTimetable newGkTimetable, CGSectionLecture cgSectionLecture, int offset, Map<String, Integer> map) {
		CGPeriod period = cgSectionLecture.getPeriod();
		if(period == null)
			return null;
		
		NKPeriod nkPeriod = getNkPeriodByTimeslot(period.getTimeslot().getTimeslotIndex(), grade);
		
		NewGkTimetableOther newGkTimetableOther = new NewGkTimetableOther();
		newGkTimetableOther.setUnitId(grade.getSchoolId());
		newGkTimetableOther.setTimetableId(newGkTimetable.getId());
		newGkTimetableOther.setPlaceId(cgSectionLecture.getRoom().getCode());
		if(newGkTimetableOther.getPlaceId()!=null && newGkTimetableOther.getPlaceId().contains(CGRoom.NO_ROOM_CODE)) {
			newGkTimetableOther.setPlaceId("");
		}
		switch(cgSectionLecture.getIsBiWeekly()) {
			case 1:
				newGkTimetableOther.setFirstsdWeek(NewGkElectiveConstant.WEEK_TYPE_ODD);
				break;
			case -1:
				newGkTimetableOther.setFirstsdWeek(NewGkElectiveConstant.WEEK_TYPE_EVEN);
				break;
			default:
				newGkTimetableOther.setFirstsdWeek(NewGkElectiveConstant.WEEK_TYPE_NORMAL);
		}
		newGkTimetableOther.setDayOfWeek(period.getDay().getDayIndex());
		newGkTimetableOther.setPeriodInterval(nkPeriod.getPeriodInterval());
		newGkTimetableOther.setPeriod(nkPeriod.getOtherIndex() + offset);
		if(offset > 0){
			if(offset + nkPeriod.getOtherIndex() > map.get(nkPeriod.getPeriodInterval())){
				newGkTimetableOther.setPeriodInterval((Integer.parseInt(nkPeriod.getPeriodInterval())+1)+"");
				newGkTimetableOther.setPeriod(nkPeriod.getOtherIndex() + offset - map.get(nkPeriod.getPeriodInterval()));
			}else{
				newGkTimetableOther.setPeriod(nkPeriod.getOtherIndex() + offset);
			}
		}
		return newGkTimetableOther;
	}
	
	private NKPeriod getNkPeriodByTimeslot(int timeslot, Grade grade) {
		int mm = grade.getMornPeriods()==null?0:grade.getMornPeriods();
		int am = grade.getAmLessonCount()==null?0:grade.getAmLessonCount();
		int pm = grade.getPmLessonCount()==null?0:grade.getPmLessonCount();
		int nm = grade.getNightLessonCount()==null?0:grade.getNightLessonCount();
		
		NKPeriod nkPeriod = new NKPeriod();
		if(timeslot <= mm){
			nkPeriod.setPeriodInterval(BaseConstants.PERIOD_INTERVAL_1);
			nkPeriod.setOtherIndex(timeslot);
		}else if(timeslot <= (mm+am)){
			nkPeriod.setPeriodInterval(BaseConstants.PERIOD_INTERVAL_2);
			nkPeriod.setOtherIndex(timeslot-mm);
		}else if(timeslot <= (mm+am+pm)){
			nkPeriod.setPeriodInterval(BaseConstants.PERIOD_INTERVAL_3);
			nkPeriod.setOtherIndex(timeslot-am-mm);
		}else if(timeslot <= (mm+am+pm+nm)){
			nkPeriod.setPeriodInterval(BaseConstants.PERIOD_INTERVAL_4);
			nkPeriod.setOtherIndex(timeslot-am-pm-mm);
		}else{
			System.out.println("未知的时间！");
		}
		return nkPeriod;
	}
	private void makeClassStudent(List<NewGkClassStudent> addStuList,
			List<String> studentIdList, String newClassId) {
		NewGkClassStudent student;
		for (String stuId : studentIdList) {
			student=initClassStudent(newClassId, stuId);
			addStuList.add(student);
		}
	}
	private NewGkDivideClass generateNewClass(String newDivideId, NewGkDivideClass oldClass) {
		NewGkDivideClass newClass = new NewGkDivideClass();
		newClass.setDivideId(newDivideId);
		newClass.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE2);
		newClass.setId(UuidUtils.generateUuid());
		
		newClass.setClassName(oldClass.getClassName());
		newClass.setClassType(oldClass.getClassType());
		newClass.setRelateId(oldClass.getRelateId());
		newClass.setSubjectIds(oldClass.getSubjectIds());
		newClass.setSubjectIdsB(oldClass.getSubjectIdsB());
		newClass.setSubjectType(oldClass.getSubjectType());
		newClass.setBestType(oldClass.getBestType());
		newClass.setIsHand(oldClass.getIsHand());
		newClass.setOldClassId(oldClass.getOldClassId());
		newClass.setOldDivideClassId(oldClass.getId());
		newClass.setOrderId(oldClass.getOrderId());
		newClass.setBatch(oldClass.getBatch());
		newClass.setParentId(oldClass.getParentId());
		//if(oldClass.getBatch() != null) {
		//	System.out.print("");
		//}
		newClass.setCreationTime(new Date());
		newClass.setModifyTime(new Date());
		
		return newClass;
	}
	
//	private String getSubjectType(CGCourse course) {
//		String subjectType = "";
//		switch(course.getType()){
//			case COMPULSORY_COURSE:
//				subjectType = NewGkElectiveConstant.SUBJECT_TYPE_O;
//				break;
//			case MAJOR_COURSE:
//				subjectType = NewGkElectiveConstant.SUBJECT_TYPE_A;
//				break;
//			case MINOR_COURSE:
//				subjectType = NewGkElectiveConstant.SUBJECT_TYPE_B;
//				break;
//			case OTHER_COURSE:
//				subjectType = NewGkElectiveConstant.SUBJECT_TYPE_O;
//				break;
//			default:
//				System.out.println("未知的科目类型");
//		}
//		return subjectType;
//	}
	
	private Map<String, Set<String>> makeClassTeacher(NewGkArray newGkArray, Set<String> xzbSubjectId,
			Map<String, Map<String, String>> classTeacherMap, Map<String, CGTeacher> teacherMap, 
			Map<String, NewGkTeacherPlanEx> teachPlannerExMap) {
		Map<String,Set<String>> subjectIdTeacherIdMap = new HashMap<>();
		List<NewGkTeacherPlan> teacherPlan = newGkTeacherPlanService.findByArrayItemIds(
				new String[]{Optional.ofNullable(newGkArray.getLessonArrangeId()).orElse("")}, true);
		CGTeacher cgTeacher;
		for (NewGkTeacherPlan newGkTeacherPlan : teacherPlan) {
//			if(!xzbSubjectId.contains(newGkTeacherPlan.getSubjectId())) {
//				// 非行政班 不会指定教师的 
//				teacherPlans.add(newGkTeacherPlan);
//				continue;
//			}
			if(CollectionUtils.isEmpty(newGkTeacherPlan.getTeacherPlanExList())) {
				continue;
			}
			
			subjectIdTeacherIdMap.put(newGkTeacherPlan.getSubjectId(), new HashSet<>());
			subjectIdTeacherIdMap.get(newGkTeacherPlan.getSubjectId())
					.addAll(new HashSet<>(newGkTeacherPlan.getExTeacherIdList()));
			
			for (NewGkTeacherPlanEx newGkTeacherPlanEx : newGkTeacherPlan.getTeacherPlanExList()) {
				if(newGkTeacherPlanEx == null) {
					continue;
				}
				teachPlannerExMap.put(newGkTeacherPlanEx.getId(), newGkTeacherPlanEx);
				
				String teacherId = newGkTeacherPlanEx.getTeacherId();
				if(!teacherMap.containsKey(teacherId)) {
					cgTeacher = new CGTeacher(teacherId, Optional.ofNullable(newGkTeacherPlanEx.getMutexNum()).orElse(0),
							newGkTeacherPlanEx.getWeekPeriodType(), newGkTeacherPlanEx.getDayPeriodType());
					teacherMap.put(teacherId, cgTeacher);
				}
				
				if(StringUtils.isBlank(newGkTeacherPlanEx.getClassIds())){
					continue;
				}
				String[] classIdList = newGkTeacherPlanEx.getClassIds().split(",");
				for (String classId : classIdList) {
					Map<String, String> map = classTeacherMap.get(classId);
					if(map == null){
						map = new HashMap<String, String>(); 
						classTeacherMap.put(classId, map);
					}
					map.put(newGkTeacherPlan.getSubjectId(), teacherId);
				}
			}
		}
		
		return subjectIdTeacherIdMap;
	}
	
	private List<NewGkLessonTime> makeTeacherPeriods(NewGkArray newGkArray, Map<String, NKPeriod> allperiodMap,
			Map<String, Set<NKPeriod>> teacherNoPeriods, Map<String, Set<NKPeriod>> teacherdoPeriods) {
		List<NewGkLessonTime> teacherTimes = newGkLessonTimeService.findByItemIdObjectId(Optional.ofNullable(newGkArray.getLessonArrangeId()).orElse(""),
				null, new String[] {NewGkElectiveConstant.LIMIT_TEACHER_2}, false);
		toMakeTimeItem(teacherTimes);
		for (NewGkLessonTime newGkLessonTime : teacherTimes) {
			String teacherId = newGkLessonTime.getObjectId();
			for (NewGkLessonTimeEx ex : newGkLessonTime.getTimesList()) {
				String timeKey=ex.getDayOfWeek()+"_"+ex.getPeriodInterval()+"_"+ex.getPeriod();
				NKPeriod nkPeriod = allperiodMap.get(timeKey);
				if(!NewGkElectiveConstant.ARRANGE_TIME_TYPE_02.equals(ex.getTimeType())) {
					// 不排课
					if(!teacherNoPeriods.containsKey(teacherId)) {
						teacherNoPeriods.put(teacherId, new HashSet<>());
					}
					teacherNoPeriods.get(teacherId).add(nkPeriod);
				}else {
					// 优先排课/必排课 ?
				}
			}
		}
		return teacherTimes;
	}
	
	private void toMakeTimeItem(List<NewGkLessonTime> newGkLessonTimeList){
		Set<String> ids = EntityUtils.getSet(newGkLessonTimeList, NewGkLessonTime::getId);
		List<NewGkLessonTimeEx> exList=newGkLessonTimeExService.findByObjectId(ids.toArray(new String[]{}),new String[]{NewGkElectiveConstant.SCOURCE_TEACHER_02});
		if(CollectionUtils.isNotEmpty(exList)){
			Map<String,List<NewGkLessonTimeEx>> map=new HashMap<String, List<NewGkLessonTimeEx>>();
			for(NewGkLessonTimeEx e:exList){
				if(!map.containsKey(e.getScourceTypeId())){
					map.put(e.getScourceTypeId(), new ArrayList<NewGkLessonTimeEx>());
				}
				map.get(e.getScourceTypeId()).add(e);
			}
			
			
			for(NewGkLessonTime time:newGkLessonTimeList){
				if(map.containsKey(time.getId())){
					time.setTimesList(map.get(time.getId()));
				}
			}
		}
	
	}
	
	public List<TeachPlace> makeAllPlace(String placeArrangeId) {
		List<NewGkplaceArrange> placeArrangeList = newGkplaceArrangeService.findByArrayItemId(placeArrangeId);
		if(CollectionUtils.isEmpty(placeArrangeList)){
			return new ArrayList<TeachPlace>();
		}
		Set<String> placeIds = EntityUtils.getSet(placeArrangeList, NewGkplaceArrange::getPlaceId);
		List<TeachPlace> allplaceList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(placeIds.toArray(new String[]{})),new TR<List<TeachPlace>>(){});
		return allplaceList;
	}
	
	/**
	 * 组装场地基础id,name
	 * @param roomList
	 * @param roomMap
	 * @param allplaceList
	 */
	private void makeRooms(List<TeachPlace> allplaceList,List<NKRoom> roomList,Map<String ,NKRoom>roomMap) {
		NKRoom room=null;
		for(TeachPlace t:allplaceList){
			room=new NKRoom();
			room.setRoomId(t.getId());
			room.setRoomName(t.getPlaceName());
			room.setCapacity(100);
			roomMap.put(room.getRoomId(), room);
			roomList.add(room);
		}
	}
	
	private int makeGradeAllTime(Grade grade,Map<String, NKPeriod> allperiodMap,List<NKPeriod> allPeriod){
		/**根据年级**/
		int mm = grade.getMornPeriods()==null?0:grade.getMornPeriods();
		int am = grade.getAmLessonCount()==null?0:grade.getAmLessonCount();
		int pm = grade.getPmLessonCount()==null?0:grade.getPmLessonCount();
		int nm = grade.getNightLessonCount()==null?0:grade.getNightLessonCount();
		if(mm+am+pm+nm>0){
			makeAllWeekTime(mm, am, pm, nm,allperiodMap, allPeriod,grade.getWeekDays());
		}
		return  mm+am+pm+nm;
	}
	
	private void makeAllWeekTime(int mm,int am,int pm,int nm,Map<String,NKPeriod> periodMap, List<NKPeriod> periodList, Integer weekDays) {
		if(weekDays == null){
			weekDays = 7;
		}
		for(int i=0;i<weekDays;i++){
			if(mm>0){
				for(int j=1;j<=mm;j++){
					NKPeriod nkPeriod=new NKPeriod();
					nkPeriod.setDayIndex(i);
					nkPeriod.setTimeslotIndex(j);
					nkPeriod.setPeriodInterval(BaseConstants.PERIOD_INTERVAL_1);//早上
					nkPeriod.setOtherIndex(j);
					periodMap.put(nkPeriod.getDayIndex()+"_"+nkPeriod.getPeriodInterval()+"_"+nkPeriod.getOtherIndex(), nkPeriod);
					periodList.add(nkPeriod);
				}
				
			}
			if(am>0){
				for(int j=1;j<=am;j++){
					NKPeriod nkPeriod=new NKPeriod();
					nkPeriod.setDayIndex(i);
					nkPeriod.setTimeslotIndex(mm+j);
					nkPeriod.setPeriodInterval(BaseConstants.PERIOD_INTERVAL_2);//上午
					nkPeriod.setOtherIndex(j);
					periodMap.put(nkPeriod.getDayIndex()+"_"+nkPeriod.getPeriodInterval()+"_"+nkPeriod.getOtherIndex(), nkPeriod);
					periodList.add(nkPeriod);
				}
				
			}
			if(pm>0){
				for(int j=1;j<=pm;j++){
					NKPeriod nkPeriod=new NKPeriod();
					nkPeriod.setDayIndex(i);
					nkPeriod.setTimeslotIndex(mm+am+j);
					nkPeriod.setPeriodInterval(BaseConstants.PERIOD_INTERVAL_3);//下午
					nkPeriod.setOtherIndex(j);
					periodMap.put(nkPeriod.getDayIndex()+"_"+nkPeriod.getPeriodInterval()+"_"+nkPeriod.getOtherIndex(), nkPeriod);
					periodList.add(nkPeriod);
				}
			}
			if(nm>0){
				for(int j=1;j<=nm;j++){
					NKPeriod nkPeriod=new NKPeriod();
					nkPeriod.setDayIndex(i);
					nkPeriod.setTimeslotIndex(mm+am+pm+j);
					nkPeriod.setPeriodInterval(BaseConstants.PERIOD_INTERVAL_4);//晚上
					nkPeriod.setOtherIndex(j);
					periodMap.put(nkPeriod.getDayIndex()+"_"+nkPeriod.getPeriodInterval()+"_"+nkPeriod.getOtherIndex(), nkPeriod);
					periodList.add(nkPeriod);
				}
			}
		}
	}
	
	/**
	 * 去除限制的时间节点
	 * @param gradeNoTime 年级限制
	 */
	private void clearGradeLimitTime(Map<String, NKPeriod> gradeNoTime,Map<String, NKPeriod> periodMap,
			Map<String, NKPeriod> allperiodMap,List<NKPeriod> periodList,List<NKPeriod> allPeriod){
		if(gradeNoTime.size()>0){
			for(NKPeriod p:allPeriod){
				String timeKey=p.getDayIndex()+"_"+p.getPeriodInterval()+"_"+p.getOtherIndex();
				if(gradeNoTime.containsKey(timeKey)){
					p.setExtra(true);
					continue;
				}
				periodList.add(p);
				periodMap.put(timeKey, p);
			}
		}else{
			periodMap.putAll(allperiodMap);;
			periodList.addAll(allPeriod);
		}
	}
	
	private void makeClassPeriods(Map<CGCourse, List<NKPeriod>> coursePeriods,
			Map<CGCourse, List<CGClass>> courseClassMap, Map<CGClass, Set<NKPeriod>> classPeriods) {
		for (CGCourse cgcourse : coursePeriods.keySet()) {
			List<NKPeriod> peridsT = coursePeriods.get(cgcourse);
			List<CGClass> list = courseClassMap.get(cgcourse);
			
			if(CollectionUtils.isEmpty(list)) {
				System.out.println("课程："+cgcourse.getCode()+"没有开设班级");
				continue;
			}
			
			for (CGClass cgClass : list) {
				if(!classPeriods.containsKey(cgClass)) {
					classPeriods.put(cgClass, new HashSet<>());
				}
				classPeriods.get(cgClass).addAll(peridsT);
			}
		}
	}
	
	
	/**
	 *  初始化
	 * @param classId
	 * @param studentId
	 * @return
	 */
	public NewGkClassStudent initClassStudent(String classId,String studentId) {
		NewGkClassStudent item=new NewGkClassStudent();
		item.setId(UuidUtils.generateUuid());
//		item.setUnitId(unitId);
//		item.setDivideId(newDivideId);
		item.setClassId(classId);
		item.setStudentId(studentId);
		item.setModifyTime(new Date());
		item.setCreationTime(new Date());
		return item;
	}
}