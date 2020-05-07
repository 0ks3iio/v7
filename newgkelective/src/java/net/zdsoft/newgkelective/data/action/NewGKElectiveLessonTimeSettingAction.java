package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
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
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.LessonTimeDto;
import net.zdsoft.newgkelective.data.dto.LessonTimeDtoPack;
import net.zdsoft.newgkelective.data.dto.SimpleLessonTimeInfDto;
import net.zdsoft.newgkelective.data.dto.SubjectLessonTimeDto;
import net.zdsoft.newgkelective.data.dto.TimeInfDto;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherGroupEx;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;
import net.zdsoft.newgkelective.data.optaplanner.dto.BatchInputDto;
import net.zdsoft.newgkelective.data.optaplanner.solver.BatchSolverUtils;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeExService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherGroupExService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.newgkelective.data.utils.MyNumberUtils;

@Controller
@RequestMapping("/newgkelective")
public class NewGKElectiveLessonTimeSettingAction extends BaseAction{
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkLessonTimeExService newGkLessonTimeExService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private NewGkOpenSubjectService newGkOpenSubjectService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkTeacherGroupExService teacherGroupExService;
	@Autowired
	private NewGkClassSubjectTimeService classSubjectTimeService;
	
	private static Logger logger = LoggerFactory.getLogger(NewGKElectiveLessonTimeSettingAction.class);
	
	private static final String SUCCESS = "SUCCESS";
	private static final String FAIL = "FAIL";
	/**
	 * 按照时间顺序展示课时安排列表
	 * @param divideId
	 * @return
	 */
	@RequestMapping("/{divideId}/lessonTimeArrange/index/page")
	public String showLessonTimeList(@PathVariable String divideId,String arrayId,ModelMap map){
		// 根据divede找到年级相关信息  比如  2017-2018  第一学期   高一年级
		NewGkDivide divide = newGkDivideService.findById(divideId);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);	
		
		//根据divide_id获取所有的排课方案array_item
		List<NewGkArrayItem> arrayItemList = newGkArrayItemService.findByDivideId(divideId, new String[]{NewGkElectiveConstant.ARRANGE_TYPE_04});
		
		//利用treeMap按照times进行排序
		ArrayList<SimpleLessonTimeInfDto> simpleLessonTimeInfs = new ArrayList<>();
		for (NewGkArrayItem newGkArrayItem : arrayItemList) {
			Map<String, Integer> freeLessonMap = newGkArrayItemService.findFreeLessonByArrayItem(newGkArrayItem);
			
			SimpleLessonTimeInfDto simpleLessonTimeInfDto = new SimpleLessonTimeInfDto();
			simpleLessonTimeInfDto.setArray_item_id(newGkArrayItem.getId());
			simpleLessonTimeInfDto.setCreation_time(newGkArrayItem.getCreationTime());
			simpleLessonTimeInfDto.setItem_name(newGkArrayItem.getItemName());
			
			Map<String,String> weekMap = NewGkElectiveConstant.dayOfWeekMap;
			Map<String, Integer> freeLessonMapShow = simpleLessonTimeInfDto.getFreeLessonMap();
			for(int i=0;i<NewGkElectiveConstant.dayOfWeeks;i++){
				if(!(freeLessonMap.get(i+"")==null)){
					freeLessonMapShow.put(weekMap.get(i+""), freeLessonMap.get(i+""));
				}
			}
			simpleLessonTimeInfs.add(simpleLessonTimeInfDto);
		}
		
		//将数据传到模板 
		map.put("grade", grade);
		map.put("simpleInfs", simpleLessonTimeInfs);
		map.put("divide_id", divideId);
		map.put("gradeId", divide.getGradeId());
		map.put("arrayId", arrayId);
		return "/newgkelective/array/lessonTimeList.ftl";
	}
	
	
	@RequestMapping("/{divide_id}/lessonTimeSetting/list/page")
	public String showLessonTimeSetIndex(@PathVariable String divide_id,String array_item_id,String arrayId,ModelMap map){
		NewGkDivide divide = newGkDivideService.findById(divide_id);
		//这里设计让修改和添加共用页面lessonTimeIndex.ftl
		map.put("openType", divide.getOpenType());
		map.put("divide_id", divide_id);
		map.put("array_item_id", array_item_id);
		map.put("arrayId", arrayId);
		return "/newgkelective/array/lessonTimeIndex.ftl";
	}
	
	
	/**
	 * 获取科目和老师设置的排课限制
	 * 
	 * @param arrayItemId
	 * @param divide
	 * @return key:gradeId-batch-groupType;周几-上下午-节次，value:List<1不排课科目，2不排课老师>
	 */
	@SuppressWarnings("unused")
	private Map<String, List<String>> getTimeLimit(String arrayItemId, NewGkDivide divide, String groupType) {
		// key:gradeId-batch-groupType;周几-上下午-节次，value:List<1不排课科目，2不排课老师>
		Map<String, List<String>> timeMap = new HashMap<String, List<String>>(); 
		
		List<NewGkDivideClass> jxbList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), 
				divide.getId(), 
				new String[] {NewGkElectiveConstant.CLASS_TYPE_2},
				false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		jxbList = filtJxb(jxbList);
		if(CollectionUtils.isEmpty(jxbList)) {
			return timeMap;
		}
		// 整理班级对应的选学考批次，班级科目对应的选学考批次
		Map<String, NewGkDivideClass> clsMap = new HashMap<String, NewGkDivideClass>();
		//batch :gradeId-batch-groupType
		Map<String, List<String>> batchSubs = new HashMap<String, List<String>>();
		// key:subjectId
		Map<String, List<String>> subBatchs = new HashMap<String, List<String>>();
		// key:clsId
		Map<String, List<String>> clsBatchs = new HashMap<String, List<String>>();
		for(NewGkDivideClass cls : jxbList) {
			
			clsMap.put(cls.getId(), cls);
			if(StringUtils.isEmpty(cls.getBatch())) {// 批次为空，过滤掉
				continue;
			}
			//cls.getSubjectIds()有为空的情况  暂时先这样解决报错问题  select distinct(divide_id) from newgkelective_divide_class where class_type=1 and subject_ids is null
			String subType = cls.getSubjectType();
			
			String gt = null;
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subType)) {
				gt = NewGkElectiveConstant.DIVIDE_GROUP_5;
			} else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subType)) {
				gt = NewGkElectiveConstant.DIVIDE_GROUP_6;
			}
			
			String batch = cls.getBatch();
			String key = divide.getGradeId()+"-"+batch+"-"+gt;
			List<String> gts = clsBatchs.get(cls.getId());
			if(gts == null) {
				gts = new ArrayList<String>();
				clsBatchs.put(cls.getId(), gts);
			}
			if (!gts.contains(key)) {
				gts.add(key);
			}
			
			String subId = cls.getSubjectIds();
			
			List<String> bas = subBatchs.get(subId);
			if(bas == null) {
				bas = new ArrayList<String>();
				subBatchs.put(subId, bas);
			}
			if (!bas.contains(key)) {
				bas.add(key);
			}
		}
		// 整理班级对应的老师，获得老师对应的选学考批次
		List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findByArrayItemIdAndSubjectIdIn(arrayItemId,
				subBatchs.keySet().toArray(new String[0]), true);
		// key:teacherId
		Map<String, List<String>> teaBatchs = new HashMap<String, List<String>>();
		if(CollectionUtils.isNotEmpty(teacherPlanList)) {
			for(NewGkTeacherPlan plan : teacherPlanList) {
				List<NewGkTeacherPlanEx> exList = plan.getTeacherPlanExList();
				if (CollectionUtils.isEmpty(exList)) {
					continue;
				}
				for(NewGkTeacherPlanEx ex : exList) {
					if (StringUtils.isNotEmpty(ex.getClassIds())) {
						String[] ids = ex.getClassIds().split(",");
						for (String id : ids) {
							if(clsBatchs.containsKey(id)) {
								List<String> bas = teaBatchs.get(ex.getTeacherId());
								if(bas == null) {
									bas = new ArrayList<String>();
									teaBatchs.put(ex.getTeacherId(), bas);
								}
								bas.addAll(clsBatchs.get(id));
							}
						}
					}
				}
			}
		}
		
		Map<String, String> subNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(subBatchs.keySet().toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		Map<String, String> teacherNameMap = new HashMap<String, String>();
		if (!teaBatchs.isEmpty()) {
			teacherNameMap  = SUtils.dt(teacherRemoteService.findPartByTeacher(teaBatchs.keySet().toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		}
		List<NewGkLessonTime> times = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, null, 
				new String[] {NewGkElectiveConstant.LIMIT_TEACHER_2, NewGkElectiveConstant.LIMIT_SUBJECT_9}, true);
		if(CollectionUtils.isEmpty(times)) {
			return timeMap;
		}
		Set<String> subIds = new HashSet<String>();
		Set<String> teaIds = new HashSet<String>();
		
		// key:gradeId-batch-groupType;周几-上下午-节次，value:List<1不排课科目，2不排课老师>
		// timeMap组装
		for(NewGkLessonTime time : times) {
			List<NewGkLessonTimeEx> exs = time.getTimesList();
			if(CollectionUtils.isEmpty(exs)) {
				continue;
			}
			List<String> batchs = null;
			boolean isTea = false;
			String obName = null;// 教师或者科目名称
			if(NewGkElectiveConstant.LIMIT_TEACHER_2.equals(time.getObjectType())) {
				obName = teacherNameMap.get(time.getObjectId());
				batchs = teaBatchs.get(time.getObjectId());
				isTea = true;
			} else {
				obName = subNameMap.get(time.getObjectId());
				batchs = subBatchs.get(time.getObjectId());
			}
			if(StringUtils.isEmpty(obName) || CollectionUtils.isEmpty(batchs)) {
				continue;
			}
			
			String timeGtype = time.getGroupType();// 选考或者学考
			boolean hasGtype = StringUtils.isNotEmpty(time.getLevelType()); 
			if(Objects.equals(time.getLevelType(), "A")) {
				timeGtype = "5";// 选考或者学考
			}else if(Objects.equals(time.getLevelType(), "B")) {
				timeGtype = "6";// 选考或者学考
			}
			for(NewGkLessonTimeEx ex : exs) {
				if(NewGkElectiveConstant.LIMIT_SUBJECT_9.equals(time.getObjectType()) &&
						NewGkElectiveConstant.ARRANGE_TIME_TYPE_03.equals(ex.getTimeType()))
					continue;
				
				String dw = ex.getDayOfWeek().intValue()+"";
				String period = ex.getPeriod().intValue()+"";
				String timeKey = dw+"-"+ex.getPeriodInterval()+"-"+period;
				for(String batch : batchs) {
					if(hasGtype && !batch.endsWith("-"+timeGtype)) {
						continue;
					}
					String baKey = batch + ";" + timeKey;
					List<String> tvs = timeMap.get(baKey);
					if(tvs == null) {
						tvs = new ArrayList<String>();
						tvs.add("");
						tvs.add("");
						timeMap.put(baKey, tvs);
					}
					StringBuilder strs = new StringBuilder();
					int index = 0;
					if(isTea) {
						strs.append(StringUtils.trimToEmpty(tvs.get(1)));
						index = 1;
					} else {
						strs.append(StringUtils.trimToEmpty(tvs.get(0)));
					}
					String[] strAr = strs.toString().split("、");
					if(ArrayUtils.contains(strAr, obName)) {
						continue;
					}
					if(strs.length() > 0) {
						strs.append("、");
					}
					strs.append(obName);
					tvs.set(index, strs.toString());
				}
			}
		}
		return timeMap;
	} 
	
	/**
	 * 获取各个星期节次可排课的批次
	 * @param map
	 * @param batchId 批次
	 * @param batchStr TODO
	 * @param groupType 
	 */
	@SuppressWarnings("unchecked")
	private void getPeriodTips(ModelMap map, String batchId, String batchStr, String groupType, Map<String, List<String>> periodTipMap) {
		//key:gradeId-batch-groupType;周几-上下午-节次 
		Map<String, List<String>> timeMap = (Map<String, List<String>>) map.get("timeMap");
		//key:周几-上下午-节次，value:list<0批次id，1选课批次，2学考批次>
		if(periodTipMap == null) {
			periodTipMap = new HashMap<>();
		}
		List<String> msList = (List<String>) map.get("amList");
		List<String> amList = (List<String>) map.get("amList");
		List<String> pmList = (List<String>) map.get("pmList");
		List<String> nightList = (List<String>) map.get("nightList");
		int index = 1;
		if(NewGkElectiveConstant.DIVIDE_GROUP_6.equals(groupType)) {
			index = 2;
		}
		dealPeriodMap(msList, "1", batchId, batchStr, index, timeMap, periodTipMap);
		dealPeriodMap(amList, "2", batchId, batchStr, index, timeMap, periodTipMap);
		dealPeriodMap(pmList, "3", batchId, batchStr, index, timeMap, periodTipMap);
		dealPeriodMap(nightList, "4", batchId, batchStr, index, timeMap, periodTipMap);
		return ;
	}
	
	/**
	 * 整理节次对应的提示
	 * @param periods 节次
	 * @param inter 时段，上下午 晚上
	 * @param batchId ：gradeId-batch-groupType，文理时为objId
	 * @param batchStr 显示内容 
	 * @param index 
	 * @param timeMap 冲突时间集合
	 * @param periodTipMap 节次提示信息集合 key:周几-上下午-节次，value:list<0批次id，1选课批次，2学考批次>
	 */
	private void dealPeriodMap(List<String> periods, String inter, String batchId, String batchStr, int index, 
			Map<String, List<String>> timeMap, Map<String, List<String>> periodTipMap) {
		if(CollectionUtils.isEmpty(periods)) {
			return;
		}
		boolean hasTime = MapUtils.isNotEmpty(timeMap);
		int wks = 7;
		for(String am : periods) {
			for (int i = 0; i < wks; i++) {
				String time = i+"-"+inter+"-"+am;
				if(hasTime && timeMap.containsKey(batchId+";"+time)) {// 该时间点有冲突
					continue;
				}
				List<String> strs = periodTipMap.get(time);
				if(strs == null) {
					strs = new ArrayList<String>();
					strs.add("");
					strs.add("");
					strs.add("");
//					strs.add(""); // hsdfz
//					strs.add("");
					periodTipMap.put(time, strs);
				}
				StringBuilder baIds = new StringBuilder(StringUtils.trimToEmpty(strs.get(0)));
				if(baIds.length() > 0) {
					baIds.append(",");
				}
				baIds.append(batchId);
				strs.set(0, baIds.toString());// 可选择的批次id
				StringBuilder or = new StringBuilder(StringUtils.trimToEmpty(strs.get(index)));
				if(or.length() > 0) {
					or.append(",");
				}
				or.append(batchStr);
				strs.set(index, or.toString());
			} 
		}
	}
	
	/**
	 * 排课特征-课表设置 首页
	 * @param divide_id
	 * @param array_item_id
	 * @param map
	 * @return
	 */
	@RequestMapping("/{divide_id}/gradeLessonTimeInf")
	public String getGradeLessonTimeInf(@PathVariable String divide_id,String array_item_id,ModelMap map){
		//先获取年级以构造课表
		NewGkDivide divide = newGkDivideService.findOne(divide_id);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);
		List<String> msList = MyNumberUtils.getNumList(grade.getMornPeriods());
		List<String> amList = MyNumberUtils.getNumList(grade.getAmLessonCount());
		List<String> pmList = MyNumberUtils.getNumList(grade.getPmLessonCount());
		List<String> nightList = MyNumberUtils.getNumList(grade.getNightLessonCount());
		
		map.put("gradeId", divide.getGradeId());
		map.put("divide_id", divide_id);
		map.put("objectType", NewGkElectiveConstant.LIMIT_SUBJECT_7);
		map.put("isXzbArray", NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType()));
		
		Map<String, Integer> piMap = getIntervalMap(grade);
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("piMap", piMap);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);

		map.put("msList", msList);
		map.put("amList", amList);
		map.put("pmList", pmList);
		map.put("nightList", nightList);
		
		if(array_item_id!=null&&!"".equals(array_item_id)){
			map.put("array_item_id", array_item_id);
		}
		int counta = divide.getBatchCountTypea() != null?divide.getBatchCountTypea():0;
		int countb = divide.getBatchCountTypeb() != null?divide.getBatchCountTypeb():0;
//		countb = 2;
		if(NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType()) && counta<1) {
			map.put("msg", "无需设置");
			return "/newgkelective/array/nodata.ftl";
		}
		if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())) {
			// 组合固定模式
			return fakeXzbLessonInfo(divide,map);
		}
		
		// 时间冲突信息
		Map<String, List<String>> periodTipMap = new HashMap<String, List<String>>();
		if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType()) 
				|| NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_02.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_06.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_08.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())
				) {
			
			Map<String, List<String>> timeMap = getTimeLimit(array_item_id, divide, null);
			map.put("timeMap", timeMap);
			
	    	Map<String, Integer> stpmap = new HashMap<String, Integer>();
	    	List<NewGkSubjectTime> sts = new ArrayList<>();
	    	if(StringUtils.isNotEmpty(array_item_id)) {
	    		sts = newGkSubjectTimeService.findByArrayItemId(array_item_id);
				for(NewGkSubjectTime time : sts) {
					stpmap.put(time.getSubjectId()+time.getSubjectType(), time.getPeriod());
				}
				
				Map<String, String> dt =  SUtils.dt(courseRemoteService.findPartCouByIds(
						EntityUtils.getSet(sts, e -> e.getSubjectId()).toArray(new String[0])), new TypeReference<Map<String, String>>() {});
//				Map<String, String> cnMap = EntityUtils.getMap(dt, Course::getId,Course::getSubjectName);
				sts.forEach(e->e.setSubjectName(dt.get(e.getSubjectId())));
				
			}
	    	// 拆分课程 的 处理
	    	List<NewGkChoRelation> relaList = newGkChoRelationService.findByChoiceIdsAndObjectType(divide.getUnitId(), 
	    			new String[] {divide_id}, NewGkElectiveConstant.CHOICE_TYPE_09);
	    	Map<String,List<String>> parentCids = EntityUtils.getListMap(relaList, NewGkChoRelation::getObjectTypeVal,NewGkChoRelation::getObjectValue);
	    	Set<String> childCids = EntityUtils.getSet(relaList, NewGkChoRelation::getObjectValue);
	    	List<NewGkSubjectTime> xgkStList = sts.stream().filter(e->!(!NewGkElectiveConstant.SUBJECT_TYPE_O.equals(e.getSubjectType()) && 
	    			childCids.contains(e.getSubjectId()))).collect(Collectors.toList());
	    	Map<String, NewGkSubjectTime> stMap = EntityUtils.getMap(sts, e->e.getSubjectId()+e.getSubjectType());
	    	
			List<LessonTimeDto> dtos = new ArrayList<LessonTimeDto>();
			LessonTimeDto xuan = new LessonTimeDto();
			xuan.setObjName("选考");
			xuan.setGroupType(NewGkElectiveConstant.SUBJECT_TYPE_A);
			dtos.add(xuan);
			LessonTimeDto xue = new LessonTimeDto();
			xue.setObjName("学考");
			xue.setGroupType(NewGkElectiveConstant.SUBJECT_TYPE_B);
			dtos.add(xue);
			int xuanPe = 0;
			int xuePe = 0;
			for(LessonTimeDto dto : dtos) {
				Iterator<NewGkSubjectTime> it = xgkStList.iterator();
				int period=0;
				List<NewGkSubjectTime> times = new ArrayList<NewGkSubjectTime>();
				while(it.hasNext()) {
					NewGkSubjectTime sub = it.next();
					if(dto.getGroupType().equals(sub.getSubjectType())){  // 判断 是选考 还是 学考
//						NewGkSubjectTime tm = new NewGkSubjectTime();
//						tm.setSubjectName(sub.getSubjectName());
//						tm.setPeriod(0);
//						Integer pe = stpmap.get(sub.getSubjectId()+sub.getSubjectType());
//						if(pe != null) {
//						}
						period+=sub.getPeriod();
//						tm.setPeriod(pe.intValue());
						
						
						if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(sub.getSubjectType())) {
							xuanPe = Math.max(xuanPe, sub.getPeriod());
						} else {
							xuePe = Math.max(xuePe, sub.getPeriod());
						}
						times.add(sub);
						// 拆分课程的 处理
						if (parentCids.containsKey(sub.getSubjectId())) {
							parentCids.get(sub.getSubjectId()).stream()
									.filter(e -> stMap.containsKey(e + sub.getSubjectType()))
									.map(e -> stMap.get(e + sub.getSubjectType()))
									.peek(e -> e.setId(null))
									.peek(e -> e.setSubjectName(e.getSubjectName() + "(" + sub.getSubjectName() + ")"))
									.forEach(e -> times.add(e));
						}
					}
				}
				//TODO 当是行政班课表设置时 objId为 虚拟课程 id 
				dto.setObjId(divide.getGradeId());
				dto.setPeriod(period);  // 选考/学考  总课时
				dto.setSubTimes(times); // 选考/学考 科目信息 
			}
			map.put("xuanPeriod", xuanPe); //选考 课时数最多的 科目 的 课时
			map.put("xuePeriod", xuePe);
			map.put("dtos", dtos);
			// 年级的objectid=gradeid，objecttype=0，isjoin=0
			// 选学考的objectid=gradeid，objecttype=0，isjoin=1
			List<LessonTimeDto> xdtos = new ArrayList<LessonTimeDto>();
			if(counta > 0) {
				for (int i = 1; i <= counta; i++) {
					LessonTimeDto dto = new LessonTimeDto();
					dto.setIs_join(1);
					dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_02);
					dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_5);  // 选考
					dto.setObjId(divide.getGradeId()+"-"+i+"-"+dto.getGroupType());
					dto.setObjName("选考"+i);
					xdtos.add(dto);
					
					getPeriodTips(map, divide.getGradeId()+"-"+i+"-"+dto.getGroupType(), i+"", dto.getGroupType(), periodTipMap);
				}
			}
			if(countb > 0) {
				for (int i = 1; i <= countb; i++) {
					LessonTimeDto dto = new LessonTimeDto();
					dto.setIs_join(1);
					dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_02);
					dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_6);	  // 学考
					dto.setObjId(divide.getGradeId()+"-"+i+"-"+dto.getGroupType());
					dto.setObjName("学考"+i);
					xdtos.add(dto);
					
					getPeriodTips(map, divide.getGradeId()+"-"+i+"-"+dto.getGroupType(), i+"", dto.getGroupType(), periodTipMap);
				}
			} 
//			if(false) {
//				//hsdfz 加上 语数外分层批次和 高二学考 批次
//				for (int i=1;i<=3;i++) {
//					LessonTimeDto dto = new LessonTimeDto();
//					dto.setIs_join(1);
//					dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_02);
//					dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_4);
//					dto.setObjId(divide.getGradeId()+"-"+i+"-"+dto.getGroupType());
//					dto.setObjName("语数英"+i);
//					xdtos.add(dto);
//					getPeriodTips(map, divide.getGradeId()+"-"+i+"-"+dto.getGroupType(), i+"", dto.getGroupType(), periodTipMap);
//				}
//				LessonTimeDto dto = new LessonTimeDto();
//				dto.setIs_join(1);
//				dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_02);
//				dto.setGroupType("7");  // 高二学考暂时用7来表示
//				dto.setObjId(divide.getGradeId()+"-"+1+"-"+dto.getGroupType());
//				dto.setObjName("高二学考"+1);
//				xdtos.add(dto);
//				getPeriodTips(map, divide.getGradeId()+"-"+1+"-"+dto.getGroupType(), 1+"", dto.getGroupType(), periodTipMap);
//			}
			map.put("groupType5", NewGkElectiveConstant.DIVIDE_GROUP_5);
			map.put("xdtos", xdtos);
			map.put("periodTipMap", periodTipMap);
			return "/newgkelective/array/lessonTimeOfGradeWithXxk.ftl";
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType())) {
			// 获取 虚拟课程 批次 可排的时间点
			Map<String, List<String>> timeMap = getTimeLimit(array_item_id, divide, null);
			map.put("timeMap", timeMap);
			
	    	Map<String, Integer> stpmap = new HashMap<String, Integer>();
	    	List<NewGkSubjectTime> sts = new ArrayList<>();
	    	if(StringUtils.isNotEmpty(array_item_id)) {
	    		sts = newGkSubjectTimeService.findByArrayItemId(array_item_id);
				for(NewGkSubjectTime time : sts) {
					stpmap.put(time.getSubjectId()+time.getSubjectType(), time.getPeriod());
				}
				
				Map<String, String> dt =  SUtils.dt(courseRemoteService.findPartCouByIds(
						EntityUtils.getSet(sts, e -> e.getSubjectId()).toArray(new String[0])), new TypeReference<Map<String, String>>() {});
//				Map<String, String> cnMap = EntityUtils.getMap(dt, Course::getId,Course::getSubjectName);
				sts.forEach(e->e.setSubjectName(dt.get(e.getSubjectId())));
			}

	    	// 行政班排课没有  拆分课程 的 处理
			List<LessonTimeDto> dtos = new ArrayList<LessonTimeDto>();
			LessonTimeDto xuni = new LessonTimeDto();
			xuni.setObjName("虚拟课程");
			xuni.setGroupType(NewGkElectiveConstant.SUBJECT_TYPE_A);
			dtos.add(xuni);
			
			int xuanPe = 0;
			Iterator<NewGkSubjectTime> it = sts.iterator();
			int period=0;
			List<NewGkSubjectTime> times = new ArrayList<>();
			LessonTimeDto dto = dtos.get(0);
			while(it.hasNext()) {
				NewGkSubjectTime sub = it.next();
				if(dto.getGroupType().equals(sub.getSubjectType())){  // 判断 是选考 还是 学考
					period+=sub.getPeriod();
					
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(sub.getSubjectType())) {
						xuanPe = Math.max(xuanPe, sub.getPeriod());
					}
					times.add(sub);
					// 行政班排课 没有拆分课程的 处理
					
				}
			}
			//TODO 当是行政班课表设置时 objId为 虚拟课程 id 
			dto.setObjId(divide.getGradeId());
			dto.setPeriod(period);  // 选考/学考  总课时
			dto.setSubTimes(times); // 选考/学考 科目信息 
			map.put("xuanPeriod", xuanPe); //选考 课时数最多的 科目 的 课时
//			map.put("xuePeriod", xuePe);
			map.put("dtos", dtos);
			// 年级的objectid=gradeid，objecttype=0，isjoin=0
			// 选学考的objectid=gradeid，objecttype=0，isjoin=1
			List<LessonTimeDto> xdtos = new ArrayList<LessonTimeDto>();
			// 获取次分班方案的 所有的虚拟课程
			String unitId = getLoginInfo().getUnitId();
			List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divide_id,
					new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			List<NewGkDivideClass> jxbClazzs = allClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())).collect(Collectors.toList());
			Map<String, String> xzbNameMap = allClassList.stream().filter(e -> NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
					.collect(Collectors.toMap(e -> e.getId(), e -> e.getClassName()));
			List<String> sortedXzbs = allClassList.stream().filter(e -> NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
					.sorted((x, y) -> {
						if (x.getOrderId() == null) {
							return 1;
						} else if (y.getOrderId() == null) {
							return -1;
						}
						return x.getClassName().compareTo(y.getClassName());
					})
					.map(e->e.getId()).collect(Collectors.toList());

			Set<String> xuniKeys = EntityUtils.getSet(jxbClazzs, NewGkDivideClass::getBatch);
			Set<String> xuniSubIds = xuniKeys.stream().map(e -> e.split("-")[0]).collect(Collectors.toSet());
			List<Course> xuniCoures = courseRemoteService.findListObjectBy(Course.class, null, null, "id", xuniSubIds.toArray(new String[0]),
					new String[] {"id","shortName","subjectName"});
			Map<String, Course> courseIdMap = EntityUtils.getMap(xuniCoures, e -> e.getId(), e -> e);
			Map<String, String> subNameMap= new HashMap<>();
			Map<String, String> subShortNameMap= new HashMap<>();
			for (String key : xuniKeys) {
				String[] split = key.split("-");
				String virId = split[0];
				String clsIds = split[1];
				String[] classIdarr = clsIds.split(",");
				String clsNames = Stream.of(classIdarr).filter(e -> xzbNameMap.containsKey(e))
						.sorted(Comparator.comparingInt(sortedXzbs::indexOf)).map(e -> xzbNameMap.get(e))
						.collect(Collectors.joining(","));
				String shortClsNames = "";
				if(clsNames.length()>8){
					int i = clsNames.indexOf(',');
					if(i +1 < clsNames.length()){
						i = clsNames.indexOf(',', i + 1);
					}
					shortClsNames = clsNames.substring(0, i);
				}
				if(BaseConstants.ZERO_GUID.equals(clsIds)){
					// 不限班级的情况
					clsNames = "不限班级";
					shortClsNames = "不限";
				}

				Course course = courseIdMap.get(virId);
				subNameMap.put(key, course.getSubjectName()+" "+clsNames);
				subShortNameMap.put(key, course.getShortName()+" "+shortClsNames);
				if(StringUtils.isBlank(course.getShortName()))
					subShortNameMap.put(key, course.getSubjectName()+" "+shortClsNames);
			}
			
			if(counta > 0) {
				for (String xuniCouseId:subNameMap.keySet()) {
					String xuniName = subNameMap.get(xuniCouseId);
					dto = new LessonTimeDto();
					dto.setIs_join(1);
					dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_02);
					dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_5);  // 选考
					dto.setObjId(divide.getGradeId()+"-"+xuniCouseId+"-"+dto.getGroupType());
					dto.setFullName(xuniName);
					xdtos.add(dto);
					
					String shortName =  subShortNameMap.get(xuniCouseId);
					if(StringUtils.isBlank(shortName)) {
						shortName = dto.getFullName().substring(0, 1);
					}
					dto.setObjName(shortName);
					getPeriodTips(map, dto.getObjId(), shortName, dto.getGroupType(), periodTipMap);
				}
			}
			
			map.put("groupType5", NewGkElectiveConstant.DIVIDE_GROUP_5);
			map.put("xdtos", xdtos);
			map.put("periodTipMap", periodTipMap);
			
			return "/newgkelective/array/lessonTimeOfGradeWithXxk.ftl";
		}
		else{
			return errorFtl(map, "你来到了未知的地方！");
		}
	}
	
	/**
	 * 3+1+2 组合固定模式 的
	 * @param divide
	 * @param map
	 * @return
	 */
	private String fakeXzbLessonInfo(NewGkDivide divide, ModelMap map) {
		List<LessonTimeDto> xdtos = new ArrayList<LessonTimeDto>();
		LessonTimeDto dto = new LessonTimeDto();
		dto.setIs_join(1);
		dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_02);
		dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_7);  // 选考
		dto.setObjId(divide.getGradeId()+"-"+1+"-"+dto.getGroupType());
		dto.setObjName("行政班");
		xdtos.add(dto);
		
		// 行政班最大课时数，普通行政班课程 + 物理/历史 
		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(), 
				new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_4}, false, 
				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		String arrayItemId = (String) map.get("array_item_id");
		List<NewGkSubjectTime> stList = newGkSubjectTimeService.findByArrayItemId(arrayItemId);
		List<NewGkClassSubjectTime> cstList = classSubjectTimeService.findByArrayItemIdAndClassIdIn(divide.getUnitId(), arrayItemId, null, null, null);
		Map<String, Integer> stPeriodMap = EntityUtils.getMap(stList, e->e.getSubjectId()+"_"+e.getSubjectType(),e->e.getPeriod());
		Set<String> weekTypeSet = stList.stream().filter(e->!Objects.equals(NewGkElectiveConstant.WEEK_TYPE_NORMAL, e.getFirstsdWeek()))
				.map(e->e.getSubjectId()+"_"+e.getSubjectType()).collect(Collectors.toSet());
		Map<String, Integer> cstPeriodMap = EntityUtils.getMap(cstList, e->e.getClassId()+"_"+e.getSubjectId(),e->e.getPeriod());
		Set<String> pure3ZhbIds = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()))
				.filter(e->e.getSubjectIds()!=null && e.getSubjectIds().split(",").length==3)
				.map(e->e.getId())
				.collect(Collectors.toSet());
		List<NewGkDivideClass> xzbList = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
				.filter(e->StringUtils.isBlank(e.getRelateId())||!pure3ZhbIds.contains(e.getRelateId()))
				.collect(Collectors.toList());
		List<NewGkDivideClass> zhb2List = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType()))
				.collect(Collectors.toList());
		Map<String, NewGkDivideClass> xzbIdMap = EntityUtils.getMap(xzbList, NewGkDivideClass::getId);
		Map<String, List<String[]>> xzbSubjects = newGkDivideClassService.findXzbSubjects(divide.getUnitId(), divide.getId(), arrayItemId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, 
				new ArrayList<>(xzbIdMap.keySet()));
		Map<String, NewGkDivideClass> zhb2IdMap = EntityUtils.getMap(zhb2List, NewGkDivideClass::getId);
		Map<String, List<String[]>> fakeXzbSubjects = newGkDivideClassService.findFakeXzbSubjects(divide.getUnitId(), divide.getId(), arrayItemId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, 
				new ArrayList<>(zhb2IdMap.keySet()));
		
		int wlMax = 0;
		// 物理/历史
		if(NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())) {
			List<Course> course2List = SUtils.dt(courseRemoteService.findWuliLiShi(divide.getUnitId()), Course.class);
			List<String> courseId2List = EntityUtils.getList(course2List, Course::getId);
			for(int i=0;i<courseId2List.size();i++) {
				String subId = courseId2List.get(i);
				String subId2 = courseId2List.get(courseId2List.size()-i-1);
				Integer period = stPeriodMap.get(subId+"_"+NewGkElectiveConstant.SUBJECT_TYPE_A);
				Integer period2 = stPeriodMap.get(subId2+"_"+NewGkElectiveConstant.SUBJECT_TYPE_B);
				if(period == null) period = 0;
				if(period2 == null) period2 = 0;
				
				if((period +period2) > wlMax) {
					wlMax = period +period2;
				}
			}
		}
		
		double maxXzbLecCount = getMaxWorkTime(stPeriodMap, cstPeriodMap, xzbSubjects,weekTypeSet);
		// 伪行政班 课时
		double maxFakeLecCount = getMaxWorkTime(stPeriodMap, cstPeriodMap, fakeXzbSubjects,weekTypeSet);
		
		
		map.put("maxXzbLecCount", maxXzbLecCount+wlMax);
		map.put("maxFakeLecCount", maxFakeLecCount);
		map.put("xdtos", xdtos);
		map.put("groupType5", NewGkElectiveConstant.DIVIDE_GROUP_5);
		return "/newgkelective/array/lessonTimeOfGradeWithXxk2.ftl";
	}


	private double getMaxWorkTime(Map<String, Integer> stPeriodMap, Map<String, Integer> cstPeriodMap,
			Map<String, List<String[]>> xzbSubjects, Set<String> weekTypeSet) {
		double maxLecCount = 0;
		for (String xzbId : xzbSubjects.keySet()) {
			List<String[]> subjInfos = xzbSubjects.get(xzbId);
			double count = 0;
			for (String[] subIfo : subjInfos) {
				String subId = subIfo[0];
				String subType = subIfo[1];
				
				Integer period = cstPeriodMap.get(xzbId+"_"+subId);
				if(period == null && stPeriodMap.get(subId+"_"+subType) != null) {
					period = stPeriodMap.get(subId+"_"+subType);
				}
				if(period != null && period > 0) {
					count += period;
					if(weekTypeSet.contains(subId+"_"+subType)) {
						count -= 0.5;
					}
				}
			}
			
			if(count > maxLecCount) {
				maxLecCount = count;
			}
		}
		return maxLecCount;
	}


	@ResponseBody
	@RequestMapping("/{divideId}/autoBatchSolve")
	public String autoBatchSolve(@PathVariable String divideId, String arrayItemId,String arrayId) {
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		String unitId = getLoginInfo().getUnitId();
		// 班级学生 课程禁排 教师禁排
		List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(
				unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2 },
				false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> jxbClassList = filtJxb(allClassList);
		if(CollectionUtils.isEmpty(jxbClassList)) {
			return error("没有教学班，无需设置");
		}
		
		List<NewGkTeacherPlan> teacherPlan = newGkTeacherPlanService.findByArrayItemIds(
				new String[]{Optional.ofNullable(arrayItemId).orElse("")}, true);
		List<NewGkLessonTime> lessonList = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, null, 
				new String[] {NewGkElectiveConstant.LIMIT_GRADE_0,NewGkElectiveConstant.LIMIT_TEACHER_2,
						NewGkElectiveConstant.LIMIT_SUBJECT_7,NewGkElectiveConstant.LIMIT_SUBJECT_9}, true);
		List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findByArrayItemIdAndSubjectTypeIn(arrayItemId, 
				new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A,NewGkElectiveConstant.SUBJECT_TYPE_B});
		
		Set<String> allBatchs = new HashSet<>();
		Map<String, String> batchMap = new HashMap<>();
		Map<String, Set<String>> subjTypeBatchMap = new HashMap<>();
		Map<String, Integer> subPeriodMap = EntityUtils.getMap(subjectTimeList, e->e.getSubjectId()+e.getSubjectType(), NewGkSubjectTime::getPeriod);
		Map<String, Integer> batchWorkTimeMap = new HashMap<>();

		jxbClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())&&StringUtils.isNotBlank(e.getBatch()))
				.forEach(cls->{
					String batchKey = cls.getSubjectType()+"-"+cls.getBatch();
					allBatchs.add(batchKey);
					batchMap.put(cls.getId(), batchKey);
					String k = cls.getSubjectIds()+cls.getSubjectType();
					Set<String> set = subjTypeBatchMap.get(k);
					if(set == null) {
						set = new HashSet<>();
						subjTypeBatchMap.put(k, set);
					}
					set.add(batchKey);
					
					Integer maxPeriod = batchWorkTimeMap.get(batchKey);
					if(maxPeriod == null) {
						maxPeriod =0;
						batchWorkTimeMap.put(batchKey, maxPeriod);
					}
					if(!subPeriodMap.containsKey(k)) return;
					Integer p = subPeriodMap.get(k);
					if(p>maxPeriod) {
						batchWorkTimeMap.put(batchKey, p);
					}
					
				});
		Map<String,Set<String>> teacherBatchMap = new HashMap<>();
		teacherPlan.stream().filter(e->CollectionUtils.isNotEmpty(e.getTeacherPlanExList()))
			.flatMap(e->e.getTeacherPlanExList().stream())
			.forEach(e->{
				String classIds = e.getClassIds();
				if(StringUtils.isBlank(classIds))
					return;
				String[] classIdArr = classIds.split(",");
				
				Set<String> batchs = teacherBatchMap.get(e.getTeacherId());
				if(batchs == null) {
					batchs = new HashSet<>();
					teacherBatchMap.put(e.getTeacherId(), batchs);
				}
				for (String cid : classIdArr) {
					if(batchMap.containsKey(cid))
						batchs.add(batchMap.get(cid));
				}
			});
		
		//所有时间点
		Set<String> allTimes = new HashSet<>();
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()),Grade.class);
		if(grade.getWeekDays() == null)
			throw new IllegalArgumentException("未设置周天数");
		
		int mm = grade.getMornPeriods()==null?0:grade.getMornPeriods();
		int am = grade.getAmLessonCount()==null?0:grade.getAmLessonCount();
		int pm = grade.getPmLessonCount()==null?0:grade.getPmLessonCount();
		int nm = grade.getNightLessonCount()==null?0:grade.getNightLessonCount();
		Map<String, String> toDtoTimeMap = new HashMap<>();
		if(mm+am+pm+nm>0){
			makeAllWeekTime(mm, am, pm, nm,grade.getWeekDays(),allTimes, toDtoTimeMap);
		}
		
		Set<String> gradeNoTimes = new HashSet<>();
		Map<String,Set<String>> batchNoTimeMap = new HashMap<>(); 
		Map<String,NewGkLessonTime> oldLtMap = new HashMap<>();
		for (NewGkLessonTime lt : lessonList) {
			if(NewGkElectiveConstant.LIMIT_SUBJECT_7.equals(lt.getObjectType())) {
				oldLtMap.put(lt.getGroupType()+lt.getLevelType(), lt);
				continue;
			}
			
			if(CollectionUtils.isEmpty(lt.getTimesList())){
				continue;
			}
			Set<String> batchs = null;
			Set<String> times = new HashSet<>();
			for (NewGkLessonTimeEx lte : lt.getTimesList()) {
				String timeKey= toDtoTimeMap.get(lte.getDayOfWeek()+"-"+lte.getPeriodInterval()+"-"+lte.getPeriod());
				
				if(timeKey == null || !allTimes.contains(timeKey)) {
					continue;
				}
				if(NewGkElectiveConstant.LIMIT_GRADE_0.equals(lt.getObjectType())) {
					gradeNoTimes.add(timeKey);
				}else if(NewGkElectiveConstant.LIMIT_TEACHER_2.equals(lt.getObjectType())) {
					batchs = teacherBatchMap.get(lt.getObjectId());
					if(CollectionUtils.isEmpty(batchs))
						continue;
					times.add(timeKey);
				}else if(NewGkElectiveConstant.LIMIT_SUBJECT_9.equals(lt.getObjectType()) 
						&& NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(lte.getTimeType())) {
					batchs = subjTypeBatchMap.get(lt.getObjectId()+lt.getLevelType());
					if(CollectionUtils.isEmpty(batchs))
						continue;
					times.add(timeKey);
				}
			}
			if(CollectionUtils.isNotEmpty(batchs)) {
				for (String batch : batchs) {
					Set<String> set = batchNoTimeMap.get(batch);
					if(set == null) {
						set = new HashSet<>();
						batchNoTimeMap.put(batch, set);
					}
					set.addAll(times);
				}
			}
		}
		
		// 批次点名称
		Map<String,String> batchNameMap = new HashMap<>();
		if(NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType())) {
			Set<String> xuniSubIds = allBatchs.stream().map(e -> e.split("-")[1]).collect(Collectors.toSet());
			List<Course> xuniCoures = courseRemoteService.findListObjectBy(Course.class, null, null, "id", xuniSubIds.toArray(new String[0]),
					new String[] {"id","shortName","subjectName"});
			List<NewGkDivideClass> xzbList = allClassList.stream().filter(e -> NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType())).collect(Collectors.toList());
			Map<String, String> xzbNameMap = EntityUtils.getMap(xzbList,e -> e.getId(), e -> e.getClassName());
			List<String> sortedXzbs = xzbList.stream()
					.sorted((x, y) -> {
						if (x.getOrderId() == null) {
							return 1;
						} else if (y.getOrderId() == null) {
							return -1;
						}
						return x.getClassName().compareTo(y.getClassName());
					})
					.map(e->e.getId()).collect(Collectors.toList());
			Map<String, Course> courseIdMap = EntityUtils.getMap(xuniCoures, e -> e.getId(), e -> e);
			for (String batchKey : allBatchs) {
				String[] split = batchKey.split("-");
				String virId = split[1];
				String clsIds = split[2];

				String[] classIdarr = clsIds.split(",");
				String clsNames = Stream.of(classIdarr).filter(e -> xzbNameMap.containsKey(e))
						.sorted(Comparator.comparingInt(sortedXzbs::indexOf)).map(e -> xzbNameMap.get(e))
						.collect(Collectors.joining(","));
				String shortClsNames = "";
				if(clsNames.length()>8){
					int i = clsNames.indexOf(',');
					if(i +1 < clsNames.length()){
						i = clsNames.indexOf(',', i + 1);
					}
					shortClsNames = clsNames.substring(0, i);
				}
				if(BaseConstants.ZERO_GUID.equals(clsIds)){
					// 不限班级的情况
					shortClsNames = "不限";
				}

				Course course = courseIdMap.get(virId);
				batchNameMap.put(batchKey,course.getShortName()+" "+shortClsNames);
				if(StringUtils.isBlank(course.getShortName()))
					batchNameMap.put(batchKey, course.getSubjectName()+" "+shortClsNames);
			}

		}else {
			allBatchs.forEach(e->{
				if(e.contains("A"))
					batchNameMap.put(e, e.replace("A-", "选考"));
				else
					batchNameMap.put(e, e.replace("B-", "学考"));
			});
		}
		
		List<String> availableTimes = allTimes.stream().filter(e->!gradeNoTimes.contains(e)).collect(Collectors.toList());
		Map<String,List<String>> batchDomainMap = new HashMap<>();
		Set<String> domainUnion = new HashSet<>();
		for (String batch : allBatchs) {
			List<String> domain = new ArrayList<>(availableTimes);
			Set<String> notimes = batchNoTimeMap.get(batch);
			if(CollectionUtils.isNotEmpty(notimes)) {
				domain.removeAll(notimes);
			}
			batchDomainMap.put(batch, domain);
			domainUnion.addAll(domain);
			
			Integer period = batchWorkTimeMap.get(batch);
			if(period > domain.size()) {
				return error(batchNameMap.get(batch)+" 至少需要时间："+period+"，实际可安排时间："+domain.size());
			}
		}
		
		int sum = (int)batchWorkTimeMap.values().stream().collect(Collectors.summarizingInt(e->e)).getSum();
		if(sum > domainUnion.size())
			return error("至少需要时间："+sum+",实际可安排时间:"+domainUnion.size());
		
		BatchInputDto inputDto = new BatchInputDto();
		inputDto.setAllPeriods(availableTimes);
		inputDto.setBatchDomainPeriodMap(batchDomainMap);
		inputDto.setArrayItemId(arrayItemId);
		inputDto.setBatchWorkTimeMap(batchWorkTimeMap );
		
		// 调用算法 返回结果
		BatchInputDto solution = null;
		try {
			solution = BatchSolverUtils.solve(inputDto);
			if(solution == null) {
				return error("自动安排失败，请手动安排");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage()+"");
		}
		
		Map<String, String> dtoToNormalTime = new HashMap<>();
		toDtoTimeMap.keySet().forEach(e->dtoToNormalTime.put(toDtoTimeMap.get(e),e));
		Map<String, List<String>> batchResultMap = solution.getBatchDomainPeriodMap();
		List<NewGkLessonTime> lessonTimeList =  new ArrayList<>(); 
		List<NewGkLessonTimeEx> lessonTimeExList =  new ArrayList<>(); 
		NewGkLessonTime lt;
		NewGkLessonTimeEx lte;
		Date now = new Date();
		for (String batchCode : batchResultMap.keySet()) {
			List<String> times = batchResultMap.get(batchCode);
			if(CollectionUtils.isEmpty(times)) {
				continue;
			}
			String[] split = batchCode.split("-",2);
			String subjectType = split[0];
			String batch = split[1];
			//
			lt = new NewGkLessonTime();
			lt.setId(UuidUtils.generateUuid());
			lt.setCreationTime(now );
			lt.setModifyTime(now);
			lt.setObjectType(NewGkElectiveConstant.LIMIT_SUBJECT_7);
			lt.setArrayItemId(arrayItemId);
			
			lt.setObjectId(grade.getId());
			lt.setLevelType(batch);
			lt.setIsJoin(NewGkElectiveConstant.IF_INT_1);
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType))
				lt.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_5);
			else
				lt.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_6);
			if(oldLtMap.containsKey(lt.getGroupType()+lt.getLevelType())) {
				lt = oldLtMap.get(lt.getGroupType()+lt.getLevelType());
			}else {
				lessonTimeList.add(lt);
			}
			
			for (String t : times) {
				String timestr = dtoToNormalTime.get(t);
				if(StringUtils.isBlank(timestr))
					continue;
				String[] split2 = timestr.split("-");
				lte = new NewGkLessonTimeEx();
				lte.setId(UuidUtils.generateUuid());
				lte.setArrayItemId(arrayItemId);
				lte.setScourceTypeId(lt.getId());
				lte.setCreationTime(now);
				lte.setModifyTime(now);
				lte.setScourceType(NewGkElectiveConstant.SCOURCE_LESSON_01);
				lte.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_02);
				
				lte.setDayOfWeek(Integer.parseInt(split2[0]));
				lte.setPeriodInterval(split2[1]);
				lte.setPeriod(Integer.parseInt(split2[2]));
				lessonTimeExList.add(lte);
			}
		}
		
		// 保存
		List<String> delSourceTypeIds = oldLtMap.values().stream().map(e->e.getId()).collect(Collectors.toList());
		try {
			newGkLessonTimeService.saveAutoBatchResult(delSourceTypeIds,lessonTimeList,lessonTimeExList, arrayId);
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage()+"");
		}
		
		return returnSuccess();
	}
	
	private void makeAllWeekTime(int mm,int am,int pm,int nm, Integer weekDays, Set<String> alltimes, Map<String,String> toDtoTimeMap) {
		String t;
		for(int i=0;i<weekDays;i++){
			if(mm>0){
				for(int j=1;j<=mm;j++){
					t = i+"-"+BaseConstants.PERIOD_INTERVAL_1+"-"+j;
					alltimes.add(t);
					toDtoTimeMap.put(i+"-"+BaseConstants.PERIOD_INTERVAL_1+"-"+j, t);
				}
			}
			if(am>0){
				for(int j=1;j<=am;j++){
					t = i+"-"+BaseConstants.PERIOD_INTERVAL_2+"-"+(mm+j);
					alltimes.add(t);
					toDtoTimeMap.put(i+"-"+BaseConstants.PERIOD_INTERVAL_2+"-"+j, t);
				}
				
			}
			if(pm>0){
				for(int j=1;j<=pm;j++){
					t = i+"-"+BaseConstants.PERIOD_INTERVAL_3+"-"+(mm+am+j);
					alltimes.add(t);
					toDtoTimeMap.put(i+"-"+BaseConstants.PERIOD_INTERVAL_3+"-"+j, t);
				}
			}
			if(nm>0){
				for(int j=1;j<=nm;j++){
					t = i+"-"+BaseConstants.PERIOD_INTERVAL_4+"-"+(mm+am+pm+j);
					alltimes.add(t);
					toDtoTimeMap.put(i+"-"+BaseConstants.PERIOD_INTERVAL_4+"-"+j, t);
				}
			}
		}
	}
	
	/**
	 * 非文理单科科目数据
	 * @param openType
	 * @param divide_id
	 * @param map
	 */
//	private void getXxkSubjects(String openType, String divide_id, ModelMap map) {
//		List<NewGkOpenSubject> oss = getOpenSubjects(divide_id, null, null, false); 
//				newGkOpenSubjectService.findByDivideId(divide_id);
//		if(CollectionUtils.isNotEmpty(oss)) {
//			Iterator<NewGkOpenSubject> it = oss.iterator();
//			while(it.hasNext()) {
//				NewGkOpenSubject os = it.next();
//				os.setSubjectName(os.getSubjectName());
//				if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(os.getSubjectType()) 
//						|| NewGkElectiveConstant.SUBJECT_TYPE_B.equals(os.getSubjectType())) {
//					os.setSubjectName(os.getSubjectName()+ os.getSubjectType());
//				}
//				os.setId(os.getSubjectId()+"-"+os.getSubjectType());
//			}
//			Collections.sort(oss, new Comparator<NewGkOpenSubject>() {
//				@Override
//				public int compare(NewGkOpenSubject o1, NewGkOpenSubject o2) {
//					if(o1.getSubjectType().equals(o2.getSubjectType())) {
//						return o1.getSubjectName().compareTo(o2.getSubjectName());
//					}
//					return o1.getSubjectType().compareTo(o2.getSubjectType());
//				}
//			});
//			map.put("subjects", oss);
//		}
//	}
	
	/**
	 * 获取科目列表
	 * @param divide_id
	 * @param map
	 * @return
	 */
	@RequestMapping("/{divide_id}/subjectLessonTimeInf")
	public String getSubjectLessonTimeInf(@PathVariable("divide_id") String divide_id, String groupType, String arrayItemId, ModelMap map){
		//先获取年级以构造课表
		NewGkDivide divide = newGkDivideService.findOne(divide_id);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);	
		List<String> amList = MyNumberUtils.getNumList(grade.getAmLessonCount());
		List<String> pmList = MyNumberUtils.getNumList(grade.getPmLessonCount());
		List<String> nightList = MyNumberUtils.getNumList(grade.getNightLessonCount());
		
		map.put("divide_id", divide_id);
		map.put("objectType", NewGkElectiveConstant.LIMIT_SUBJECT_9);
		map.put("amList", amList);
		map.put("pmList", pmList);
		map.put("nightList", nightList);
		map.put("groupType", groupType);
		//if (NewGkElectiveConstant.DIVIDE_GROUP_1.equals(groupType)) {
//		if(!NewGkElectiveConstant.DIVIDE_TYPE_03.equals(divide.getOpenType()) 
//				&& !NewGkElectiveConstant.DIVIDE_TYPE_04.equals(divide.getOpenType())) {// 非文理
//			getXxkSubjects(divide.getOpenType(), divide_id, map);
//			
//		}
		return "/newgkelective/array/lessonTimeOfSubject.ftl";
		//return errorFtl(map, "文理分科模式已经停止支持");
	}
	
	@RequestMapping("/{array_item_id}/gradeLessonTimeinf/json")
	@ResponseBody
	public String getGradeLessonTimeInfByArrayItemId(@PathVariable String array_item_id){
		//获取年级课时信息
		List<LessonTimeDto> lessonTimeDtoList = getLessonTimeDtoList(array_item_id, NewGkElectiveConstant.LIMIT_GRADE_0, null);
		String lessonTimeDtosJson = SUtils.s(lessonTimeDtoList);
		//String subjectTimeDtosJson = SUtils.s(subjectTimeDtos); 
		
		String result = "{\"lessonTimeDtosJson\":"+lessonTimeDtosJson+"}";
		return result;
	}
	
	@RequestMapping("/{array_item_id}/lessonTimeinf/json")
	@ResponseBody
	public String getLessonTimeInfByArray_item_id(@PathVariable String array_item_id){
		//获取年级课时信息
		List<LessonTimeDto> lessonTimeDtoList = getLessonTimeDtoList(array_item_id, NewGkElectiveConstant.LIMIT_GRADE_0, null);
		String lessonTimeDtosJson = SUtils.s(lessonTimeDtoList);
		//String subjectTimeDtosJson = SUtils.s(subjectTimeDtos);
		
		List<LessonTimeDto> lessonTimeDtoList2 = getLessonTimeDtoList(array_item_id, NewGkElectiveConstant.LIMIT_SUBJECT_7, null);
		String lessonTimeDtosJson2 = SUtils.s(lessonTimeDtoList2);
		//String subjectTimeDtosJson = SUtils.s(subjectTimeDtos); 
		String result = "{\"gradeTimeDtosJson\":"+lessonTimeDtosJson+",\"lessonTimeDtosJson\":"+lessonTimeDtosJson2+"}";
		return result;
	}
	
	/**
	 * 获取课时信息
	 * @param array_item_id
	 * @param objectType
	 * @param groupType
	 * @return
	 */
	private List<LessonTimeDto> getLessonTimeDtoList(String array_item_id, String objectType, String groupType){
		// 1.根据array_item_id获取lesson_time,进而获取lesson_timeEx对象
		List<NewGkLessonTime> lessonTimes = newGkLessonTimeService.findByItemIdObjectIdAndGroupType(array_item_id, 
				null, new String[] {objectType}, groupType);
		List<LessonTimeDto> lessonTimeDtoList = new ArrayList<LessonTimeDto>();
		if(CollectionUtils.isEmpty(lessonTimes)) {
			return lessonTimeDtoList;
		}
		
		Map<String, String> stids = new HashMap<String, String>();
		for(NewGkLessonTime time : lessonTimes) {
			if(StringUtils.isNotEmpty(time.getLevelType())) {
				stids.put(time.getId(), time.getObjectId()+"-"+time.getLevelType()+"-"+time.getGroupType());
			} else {
				stids.put(time.getId(), time.getObjectId());
			}
		}
		
		lessonTimeDtoList = getLessonTimeDtos(stids.keySet().toArray(new String[0]), NewGkElectiveConstant.SCOURCE_LESSON_01, stids);
		return lessonTimeDtoList;
	}
	
	/**
	 * 组装时间dto数据
	 * @param stypeIds sourceTypeIds
	 * @param stype sourceType
	 * @param stids <sourceTypeId, objectId或objectId-levelType> 
	 * @return
	 */
	private List<LessonTimeDto> getLessonTimeDtos(String[] stypeIds, String stype, Map<String, String> stids){
		List<LessonTimeDto> lessonTimeDtoList = new ArrayList<LessonTimeDto>();
		List<NewGkLessonTimeEx> lessonTimeEx = newGkLessonTimeExService.findByObjectId(stypeIds, new String[]{stype});
		//装配成dto
		for (NewGkLessonTimeEx newGkLessonTimeEx : lessonTimeEx) {
			LessonTimeDto lessonTimeDto = new LessonTimeDto();
			//---进行一些操作
			lessonTimeDto.setIs_join(0);
			lessonTimeDto.setWeekday(newGkLessonTimeEx.getDayOfWeek());
			lessonTimeDto.setPeriod_interval(newGkLessonTimeEx.getPeriodInterval());
			lessonTimeDto.setPeriod(newGkLessonTimeEx.getPeriod());
			lessonTimeDto.setTimeType(newGkLessonTimeEx.getTimeType());
			lessonTimeDto.setObjId(stids.get(newGkLessonTimeEx.getScourceTypeId()));
			
			lessonTimeDtoList.add(lessonTimeDto);
		}
		return lessonTimeDtoList;
	}
	
	@RequestMapping("/{array_item_id}/subjectTimeinf/json")
	@ResponseBody
	public String getSubjectTimeInfByArray_item_id(@PathVariable String array_item_id, String groupType){
		//获取年级课时信息
		List<LessonTimeDto> lessonTimeDtoList = getLessonTimeDtoList(array_item_id, NewGkElectiveConstant.LIMIT_GRADE_0, null);
		
		List<LessonTimeDto> timeDtoList = getLessonTimeDtoList(array_item_id, NewGkElectiveConstant.LIMIT_SUBJECT_7, null);
		//1.获取科目课时信息 
		String subjectTimeDtosJson;
		String timeJson = "";
		if (StringUtils.isNotEmpty(groupType) 
				&& !NewGkElectiveConstant.DIVIDE_GROUP_1.equals(groupType)) {
			// 文理时 取总课表的语数英设置
			lessonTimeDtoList.addAll(timeDtoList);
			
			List<LessonTimeDto> subjectTimeDtoList = getLessonTimeDtoList(array_item_id, NewGkElectiveConstant.LIMIT_SUBJECT_9, groupType);
			subjectTimeDtosJson = SUtils.s(subjectTimeDtoList);
		} else {
			// 根据array_item_id获取lessonTime对象
			List<NewGkLessonTime> lessonTimes = newGkLessonTimeService.findByItemIdObjectIdAndGroupType(array_item_id,
					null, new String[] { NewGkElectiveConstant.LIMIT_SUBJECT_9 }, groupType);
			//获取时间信息 周几 第几节课等
			List<NewGkLessonTimeEx> lessonTimeExList = newGkLessonTimeExService.findByObjectId(
					EntityUtils.getSet(lessonTimes, "id").toArray(new String[0]),
					new String[] { NewGkElectiveConstant.SCOURCE_LESSON_01 });
			Map<String, List<NewGkLessonTimeEx>> lessonTimeExMap = EntityUtils.getListMap(lessonTimeExList,
					"scourceTypeId", null);
			//1.2根据lesson_time_id获取lesson_time_Ex对象
			List<SubjectLessonTimeDto> subjectTimeDtos = new ArrayList<SubjectLessonTimeDto>();
			for (NewGkLessonTime newGkLessonTime : lessonTimes) {
				//获取科目对象 Course
				String subjectId = newGkLessonTime.getObjectId();
				String courseJson = courseRemoteService.findOneById(subjectId);
				Course course = SUtils.dc(courseJson, Course.class);
				if (course == null) {
					logger.error("找不到指定id的课程");
					System.out.println("找不到指定id的课程");
					continue;
				}

				//获取时间信息 周几 第几节课等
				List<NewGkLessonTimeEx> lessonTimeExs = lessonTimeExMap.get(newGkLessonTime.getId());

				if (lessonTimeExs == null || lessonTimeExs.size() < 1)
					continue;
				SubjectLessonTimeDto subjectLessonTimeDto = assembleSubjectLessonTimeDto(course, lessonTimeExs);
				if (StringUtils.isNotEmpty(newGkLessonTime.getLevelType())) {
					subjectLessonTimeDto.setSubjectId(
							course.getId() + "-" + newGkLessonTime.getLevelType());
				}
				subjectTimeDtos.add(subjectLessonTimeDto);
			}
			subjectTimeDtosJson = SUtils.s(subjectTimeDtos);
			timeJson = SUtils.s(timeDtoList);
		}
		
		String lessonTimeDtosJson = SUtils.s(lessonTimeDtoList);
		if(StringUtils.isBlank(timeJson)) {
			timeJson = "\"\"";
		}
		
		String result = "{\"subjectTimeDtosJson\":"+subjectTimeDtosJson
					+",\"lessonTimeDtosJson\":"+lessonTimeDtosJson
					+",\"timeDtosJson\":"+timeJson+"}";
		
		return result;
	}
	
	private SubjectLessonTimeDto assembleSubjectLessonTimeDto(Course course,
			List<NewGkLessonTimeEx> lessonTimeExs) {
		List<TimeInfDto> timeInfs = new ArrayList<TimeInfDto>();
		for (NewGkLessonTimeEx lessonTimeEx : lessonTimeExs) {
			TimeInfDto timeInfDto = new TimeInfDto();
			timeInfDto.setDay_of_week(lessonTimeEx.getDayOfWeek());
			timeInfDto.setPeriod(lessonTimeEx.getPeriod());
			timeInfDto.setPeriod_interval(lessonTimeEx.getPeriodInterval());
			timeInfDto.setTime_type(lessonTimeEx.getTimeType());
			
			timeInfs.add(timeInfDto);
		}
		SubjectLessonTimeDto subjectLessonTimeDto = new SubjectLessonTimeDto();
		subjectLessonTimeDto.setSubjectId(course.getId());
		subjectLessonTimeDto.setSubjectIdName(course.getSubjectName());
		subjectLessonTimeDto.setTimeInf(timeInfs);
		return subjectLessonTimeDto;
	}
	/**
	 * 新增加一个某年级什么时候排课 什么时候不排课的记录
	 * @param lessonTimeDto
	 * @return
	 */
	@RequestMapping("/{divide_id}/addLessonTimeTable")
	@ResponseBody
	public String addLessonTimeTable(LessonTimeDtoPack lessonTimeDtoPack,@PathVariable String divide_id,ModelMap map){
		
		String array_item_id = "";
		String msg = "";
		try {
			array_item_id = newGkLessonTimeExService.addLessonTimeTable(lessonTimeDtoPack, divide_id);
			msg = "{\"msg\":\""+SUCCESS+"\",\"arrayItemId\":\""+array_item_id+"\"}";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "{\"msg\":\""+FAIL+"\",\"arrayItemId\":\""+array_item_id+"\"}";
		}
		
		return msg;
	}

	@RequestMapping("/{array_item_id}/addSubjectLessonTime")
	@ResponseBody
	public String addSubjectLessonTime(@PathVariable String array_item_id,SubjectLessonTimeDto subjectLessonTimeDto){
		//整个过程需要过程支持，全部放到service中去
		
		try {
			newGkLessonTimeExService.addSubjectLessonTime(subjectLessonTimeDto
					,array_item_id);
		} catch (Exception e) {
			e.printStackTrace();
			return FAIL;
		}
		
		return SUCCESS;
	}
	
	@RequestMapping("/{array_item_id}/updateLessonTimeTable")
	@ResponseBody
	public String updateLessonTimeTable(@PathVariable String array_item_id,LessonTimeDtoPack lessonTimeDtoPack){
		//1.根据array_item_id获取lesson_time_id
		String objectType = NewGkElectiveConstant.LIMIT_SUBJECT_7;
		List<NewGkLessonTime> lessonTimes = newGkLessonTimeService.findByItemIdObjectId(array_item_id,
				new String[]{}, new String[]{objectType}, false);
		NewGkLessonTime newGkLessonTime = lessonTimes.get(0);
		
		//2.根据lesson_time_id删除lesson_time_ex中对应的记录,并在lesson_time_ex表中增加相应的新的记录
		//根据dto装配实体类对象
		Map<String, String> objSourceTypeIdMap = new HashMap<String, String>();
		for(NewGkLessonTime time : lessonTimes) {
			objSourceTypeIdMap.put(time.getObjectId(), time.getId());
		}
//		EntityUtils.getMap(lessonTimes, "objectId", "id");
		lessonTimeDtoPack.setObjType(NewGkElectiveConstant.LIMIT_SUBJECT_7);
		lessonTimeDtoPack.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
		List<NewGkLessonTimeEx> lessonTimeExs = newGkLessonTimeExService.assembleLessonTimeExFromDtoForGrade(array_item_id, lessonTimeDtoPack, objSourceTypeIdMap);
		
		String returnStr = "";
		try {
			newGkLessonTimeExService.updateGradeLessonTimeEx(newGkLessonTime.getId(),array_item_id,
					NewGkElectiveConstant.SCOURCE_LESSON_01,
					NewGkElectiveConstant.ARRANGE_TIME_TYPE_01,
					lessonTimeExs);
			returnStr = "{\"msg\":\""+SUCCESS+"\",\"arrayItemId\":\""+array_item_id+"\"}";
		} catch (Exception e) {
			e.printStackTrace();
			returnStr = "{\"msg\":\""+FAIL+"\",\"arrayItemId\":\""+array_item_id+"\"}";
		}
		return returnStr;
	}
	
	@RequestMapping("/{array_item_id}/updateSubjectLessonTime")
	@ResponseBody
	public String updateSubjectLessonTime(@PathVariable String array_item_id,SubjectLessonTimeDto subjectLessonTimeDto){
		//根据arrayItemId获取lessonTime
		//2.根据lesson_time_id删除lesson_time_ex中对应的记录,并在lesson_time_ex表中增加相应的新的记录
		//根据dto装配实体类对象
		try {
			newGkLessonTimeExService.updateSubjectLessonTimes(array_item_id, subjectLessonTimeDto);
		} catch (Exception e) {
			e.printStackTrace();
			return FAIL;
		}
		
		return SUCCESS;
	}
	
	private NewGkLessonTime initNewGkLessonTime(String arrayItemId,String objId,String objType){
		NewGkLessonTime newGkLessonTime = new NewGkLessonTime();
		newGkLessonTime.setId(UuidUtils.generateUuid());
		String[] objs = objId.split("-");
		newGkLessonTime.setObjectId(objs[0]);
		newGkLessonTime.setObjectType(objType);
		newGkLessonTime.setIsJoin(1);
		newGkLessonTime.setArrayItemId(arrayItemId);
		newGkLessonTime.setCreationTime(new Date());
		newGkLessonTime.setModifyTime(new Date());
		if(objs.length > 1) {
			newGkLessonTime.setLevelType(objs[1]);
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(newGkLessonTime.getLevelType())) {
				newGkLessonTime.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_5);
			} else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(newGkLessonTime.getLevelType())) {
				newGkLessonTime.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_6);
			}
		}
		if(StringUtils.isEmpty(newGkLessonTime.getGroupType())) {
			newGkLessonTime.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
		}
		return newGkLessonTime;
	}
	
	private NewGkLessonTimeEx initNewGkLessonTimeEx(String lessonTimeId,String arrayItemId,TimeInfDto timeInfDto){
		NewGkLessonTimeEx newGkLessonTimeEx = new NewGkLessonTimeEx();
		newGkLessonTimeEx.setId(UuidUtils.generateUuid());
		newGkLessonTimeEx.setDayOfWeek(timeInfDto.getDay_of_week());
		newGkLessonTimeEx.setPeriod(timeInfDto.getPeriod());
		newGkLessonTimeEx.setPeriodInterval(timeInfDto.getPeriod_interval());
		newGkLessonTimeEx.setScourceType(NewGkElectiveConstant.SCOURCE_LESSON_01);
		newGkLessonTimeEx.setScourceTypeId(lessonTimeId);
		newGkLessonTimeEx.setArrayItemId(arrayItemId);
		newGkLessonTimeEx.setTimeType(timeInfDto.getTime_type());
		newGkLessonTimeEx.setCreationTime(new Date());
		newGkLessonTimeEx.setModifyTime(new Date());
		return newGkLessonTimeEx;
	}
	
	private TimeInfDto initTimeInfDto(String time,String type){
		String[] tt = time.split("_");
		TimeInfDto timeInfDto = new TimeInfDto();
		timeInfDto.setDay_of_week(Integer.parseInt(tt[0]));
		timeInfDto.setPeriod(Integer.parseInt(tt[2]));
		timeInfDto.setPeriod_interval(tt[1]);
		timeInfDto.setTime_type(type);
		return timeInfDto;
	}
	
	@RequestMapping("/{array_item_id}/copySubjectLessonTime")
	@ResponseBody
	public String copySubjectLessonTime(@PathVariable String array_item_id, String subjectIds, 
			String timeDto1, String timeDto2, String fromSubId){
		//subjectIds subjectId,subjectId
		//timeDto1 2-1-1,3-1-1 不排课
		//timeDto2 2-1-1,3-1-1 优先排课 不处理
		if(StringUtils.isBlank(subjectIds)){
			return error("科目为空");
		}
		try {
			String[] timeDtoArr1=null;
			List<TimeInfDto> timeInfDtoList=new ArrayList<TimeInfDto>();
			TimeInfDto timeInfDto;
			if(StringUtils.isNotBlank(timeDto1)){
				timeDtoArr1=timeDto1.split(",");
				for(String s:timeDtoArr1){
					timeInfDto=initTimeInfDto(s, NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
					timeInfDtoList.add(timeInfDto);
				}
				
			}
//			String[] timeDtoArr2=null;
//			if(StringUtils.isNotBlank(timeDto2)){
//				timeDtoArr2=timeDto2.split(",");
//				for(String s:timeDtoArr2){
//					timeInfDto=initTimeInfDto(s, NewGkElectiveConstant.ARRANGE_TIME_TYPE_02);
//					timeInfDtoList.add(timeInfDto);
//				}
//			}
			
			String[] subjectIdArr = subjectIds.split(",");
			List<String> subIds = new ArrayList<String>();
			for(String str : subjectIdArr) {
				String[] strs = str.split("-");
				subIds.add(strs[0]);
			}
			List<NewGkLessonTime> timeList = newGkLessonTimeService.findByItemIdObjectId(array_item_id, subIds.toArray(new String[0]), new String[]{NewGkElectiveConstant.LIMIT_SUBJECT_9}, false);
			Map<String, NewGkLessonTime> tmMap = new HashMap<String, NewGkLessonTime>();
			if(CollectionUtils.isNotEmpty(timeList)) {
				for(NewGkLessonTime time : timeList) {
					String key = time.getObjectId();
					if(StringUtils.isNotEmpty(time.getLevelType())) {
						key += "-"+time.getLevelType();
					}
					if(key.equals(fromSubId)) {
						continue;
					}
					tmMap.put(key, time);
				}
			}
			NewGkLessonTime dto;
			NewGkLessonTimeEx exdto;
			List<NewGkLessonTime> insertNewGkLessonTimeList=new ArrayList<NewGkLessonTime>();
			List<NewGkLessonTimeEx> insertNewGkLessonTimeExList=new ArrayList<NewGkLessonTimeEx>();
			Set<String> exSourceIds = new HashSet<String>();
			for(int i = 0; i < subjectIdArr.length; i++){
				String subjectId = subjectIdArr[i];
		    	if (!tmMap.containsKey(subjectId)) {
					dto = initNewGkLessonTime(array_item_id, subjectId, NewGkElectiveConstant.LIMIT_SUBJECT_9);
					insertNewGkLessonTimeList.add(dto);
				} else {
					dto = tmMap.get(subjectId);
					exSourceIds.add(dto.getId());
				}
		    	for(TimeInfDto tDo:timeInfDtoList){
		    		exdto=initNewGkLessonTimeEx(dto.getId(),dto.getArrayItemId(), tDo);
		    		insertNewGkLessonTimeExList.add(exdto);
		    	}
		    }
		    newGkLessonTimeService.saveCopyLessonTime(array_item_id,subjectIdArr,insertNewGkLessonTimeList,insertNewGkLessonTimeExList,NewGkElectiveConstant.LIMIT_SUBJECT_9, exSourceIds);
		} catch (Exception e) {
			e.printStackTrace();
			return error("复制失败");
		}
		return success("");
	}

    /**
     * 更新/保存 课表设置
     * @param array_item_id
     * @param arrayId
     * @param lessonTimeDtoPack
     * @param objectType
     * @return
     */
	@RequestMapping("/{array_item_id}/updateLessonTimeNew")
	@ResponseBody
	public String updateLessonTimeNew(@PathVariable String array_item_id, String arrayId, LessonTimeDtoPack lessonTimeDtoPack, String objectType){
		// 根据arrayItemId获取lessonTime
		//2.根据lesson_time_id删除lesson_time_ex中对应的记录,并在lesson_time_ex表中增加相应的新的记录
		//根据dto装配实体类对象
		String msg;
		try {
			newGkLessonTimeExService.updateLessonTime(arrayId, array_item_id, objectType, lessonTimeDtoPack);
			msg = "{\"msg\":\""+SUCCESS+"\",\"arrayItemId\":\""+array_item_id+"\"}";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "{\"msg\":\""+e.getMessage()+"\",\"arrayItemId\":\""+array_item_id+"\"}";
		}
		return msg;
	}

	@RequestMapping("/{array_item_id}/deleteLessonTimeItem")
	@ResponseBody
	public String deleteLessonTimeItem(@PathVariable String array_item_id){
		NewGkArrayItem newGkArrayItem = newGkArrayItemService.findOne(array_item_id);
		newGkArrayItem.setIsDeleted(1);
		newGkArrayItem.setModifyTime(new Date());
		
		try {
			newGkArrayItemService.update(newGkArrayItem, array_item_id, new String[]{"isDeleted","modifyTime"});
		} catch (Exception e) {
			e.printStackTrace();
			return FAIL;
		}
		
		return SUCCESS;
	}
	
	@ControllerInfo("教师时间安排设置")
	@RequestMapping("/{divideId}/teacherClass/teacherTime/index/page")
	public String teacherTimePage(@PathVariable String divideId, String arrayItemId, String teacherId, 
			int rowIndex,boolean basicSave, ModelMap map) {
		map.put("itemId", arrayItemId);
		map.put("teacherId", teacherId);
		map.put("rowIndex", rowIndex+"");
		map.put("basicSave", basicSave);
		map.put("groupType", NewGkElectiveConstant.DIVIDE_GROUP_1);
		map.put("objectType", NewGkElectiveConstant.LIMIT_TEACHER_2);
		map.put("sourceType", NewGkElectiveConstant.SCOURCE_TEACHER_02);
		
		NewGkDivide divide = newGkDivideService.findById(divideId);
		Grade grade;
		if(divide!=null){
			grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);	
		}else{//如果是基础数据则divideId为年级id
			grade = SUtils.dc(gradeRemoteService.findOneById(divideId), Grade.class);	
		}
		
		Map<String, Integer> piMap = getIntervalMap(grade);
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("piMap", piMap);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
		
		return "/newgkelective/teacherArrange/teacherTimeDiv.ftl";
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
	@ControllerInfo("教师时间安排设置")
	@RequestMapping("/teacherClass/teacherTime/json")
	public String teacherTimeJson(String arrayItemId, String teacherId, 
			ModelMap map) {
		//获取年级课时信息
		List<LessonTimeDto> lessonTimeDtoList = getLessonTimeDtoList(arrayItemId, NewGkElectiveConstant.LIMIT_GRADE_0, null);
		
		List<NewGkLessonTime> times = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, new String[] {teacherId}, new String[] {NewGkElectiveConstant.LIMIT_TEACHER_2}, false);
		String timeStr = "";
		List<LessonTimeDto> dtos ;
		if(CollectionUtils.isNotEmpty(times)) {
			String timeId = times.get(0).getId();
			Map<String, String> soids = new HashMap<String, String>();
			soids.put(timeId, teacherId);
			dtos = getLessonTimeDtos(new String[] {timeId}, NewGkElectiveConstant.SCOURCE_TEACHER_02, soids);
		} else {
			dtos = new ArrayList<LessonTimeDto>();
		}
		// 加入 教师在其他年级已经安排的课程;和来自教研组的 禁排时间
		List<LessonTimeDto> texists = dtos.stream().filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_04.equals(e.getTimeType())
				||NewGkElectiveConstant.ARRANGE_TIME_TYPE_05.equals(e.getTimeType()))
				.collect(Collectors.toList());
		dtos.removeAll(texists);
		lessonTimeDtoList.addAll(texists);
		
		Grade grade = gradeRemoteService.findOneObjectById(arrayItemId);
		if(grade != null) {
			List<NewGkTeacherGroupEx> tgExList = teacherGroupExService.findByGradeIdAndTid(arrayItemId, new String[] {teacherId});
			if(CollectionUtils.isNotEmpty(tgExList)) {
				Set<String> tgIds = EntityUtils.getSet(tgExList, NewGkTeacherGroupEx::getTeacherGroupId);
				List<NewGkLessonTime> lessonTimes = newGkLessonTimeService.findByItemIdObjectIdAndGroupType(arrayItemId, 
						tgIds.toArray(new String[0]), new String[] {NewGkElectiveConstant.LIMIT_TEACHER_GROUP_6}, null);
				Map<String, String> soids = EntityUtils.getMap(lessonTimes, NewGkLessonTime::getId,NewGkLessonTime::getObjectId);
				List<LessonTimeDto> lessonTimeDtoList2 = getLessonTimeDtos(soids.keySet().toArray(new String[0]), NewGkElectiveConstant.SCOURCE_TEACHER_02, soids);
				lessonTimeDtoList2.forEach(e->e.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_05));
				lessonTimeDtoList.addAll(lessonTimeDtoList2);
			}
		}
		
		timeStr = SUtils.s(dtos);
		String lessonTimeDtosJson = SUtils.s(lessonTimeDtoList);
		String result = "{\"timeDtosJson\":"+timeStr+",\"lessonTimeDtosJson\":"+lessonTimeDtosJson+"}";
		return result;
	}
	
	@ResponseBody
	@ControllerInfo("教师时间安排复制")
	@RequestMapping("/teacherClass/teacherTime/copyTeacherTime")
	public String copyTeacherTime(String arrayItemId, String teacherId, String teacherIds,
			ModelMap map) {
		if(StringUtils.isEmpty(teacherIds)) {
			return error("没有选择要复制到的老师");
		}
		String[] arrTeacherIds = teacherIds.split(",");
		List<NewGkLessonTime> times = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, new String[] {teacherId}, new String[] {NewGkElectiveConstant.LIMIT_TEACHER_2}, false);
		if(CollectionUtils.isEmpty(times)){
			return error("没有需要复制的时间限制");
		}
		Set<String> ids = EntityUtils.getSet(times, "id");
		List<NewGkLessonTimeEx> allExList=newGkLessonTimeExService.findByObjectId(ids.toArray(new String[]{}), new String[]{NewGkElectiveConstant.SCOURCE_TEACHER_02});
		
		if(CollectionUtils.isEmpty(allExList)){
			return error("没有需要复制的时间限制");
		}
		List<NewGkLessonTime> timeList = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, arrTeacherIds, new String[]{NewGkElectiveConstant.LIMIT_TEACHER_2}, false);
		Map<String, NewGkLessonTime> tmMap = new HashMap<String, NewGkLessonTime>();
		if(CollectionUtils.isNotEmpty(timeList)) {
			tmMap = EntityUtils.getMap(timeList, "objectId");
		}
		List<NewGkLessonTime> insertList=new ArrayList<NewGkLessonTime>();
		List<NewGkLessonTimeEx> exInsertList=new ArrayList<NewGkLessonTimeEx>();
		Set<String> exSourceIds = new HashSet<String>();
		for(String s:arrTeacherIds){
			//新增一条NewGkLessonTime记录
			NewGkLessonTime tt;
			if (tmMap.containsKey(s)) {
				tt = tmMap.get(s);
				exSourceIds.add(tt.getId());
			} else {
				tt = initNewGkLessonTime(arrayItemId, s, NewGkElectiveConstant.LIMIT_TEACHER_2);
				insertList.add(tt);
			}
			List<NewGkLessonTimeEx> exList1 = initNewGkLessonTimeExListCopy(tt.getId(),tt.getArrayItemId(), allExList);
			exInsertList.addAll(exList1);
		}
		//原有基础上增加
		try{
			newGkLessonTimeService.saveCopyLessonTime(arrayItemId, arrTeacherIds, insertList, exInsertList,NewGkElectiveConstant.LIMIT_TEACHER_2, exSourceIds);
		}catch (Exception e) {
			e.printStackTrace();
			return error("复制失败");
		}
		return success("");
	}
	
	private List<NewGkLessonTimeEx> initNewGkLessonTimeExListCopy(String lessonTimeId,String arrayItemId,List<NewGkLessonTimeEx> oldList){
		NewGkLessonTimeEx newGkLessonTimeEx=null;
		List<NewGkLessonTimeEx> newGkLessonTimeExList = new ArrayList<NewGkLessonTimeEx>();
		for(NewGkLessonTimeEx ex:oldList){
			newGkLessonTimeEx = new NewGkLessonTimeEx();
			newGkLessonTimeEx.setId(UuidUtils.generateUuid());
			newGkLessonTimeEx.setDayOfWeek(ex.getDayOfWeek());
			newGkLessonTimeEx.setPeriod(ex.getPeriod());
			newGkLessonTimeEx.setPeriodInterval(ex.getPeriodInterval());
			newGkLessonTimeEx.setScourceType(ex.getScourceType());//教师 NewGkElectiveConstant.SCOURCE_TEACHER_02
			newGkLessonTimeEx.setScourceTypeId(lessonTimeId);
			newGkLessonTimeEx.setArrayItemId(arrayItemId);
			newGkLessonTimeEx.setTimeType(ex.getTimeType());
			newGkLessonTimeEx.setCreationTime(new Date());
			newGkLessonTimeEx.setModifyTime(new Date());
			newGkLessonTimeExList.add(newGkLessonTimeEx);
		}
		return newGkLessonTimeExList;
	}
	
	private List<NewGkDivideClass> filtJxb(List<NewGkDivideClass> list){
		return list.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())&&StringUtils.isNotBlank(e.getBatch())).collect(Collectors.toList());
	}
}
