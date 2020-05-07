package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolCalendarRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkLessonTimeDao;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherGroup;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherGroupEx;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeExService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherGroupExService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherGroupService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;
import net.zdsoft.newgkelective.data.utils.SplitUtils;

@Service("newGkLessonTimeService")
public class NewGkLessonTimeServiceImpl extends BaseServiceImpl<NewGkLessonTime, String> implements NewGkLessonTimeService{

	@Autowired
	private NewGkLessonTimeDao newGkLessonTimeDao;
	@Autowired
	private NewGkLessonTimeExService newGkLessonTimeExService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private SchoolCalendarRemoteService schoolCalendarRemoteService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private NewGkTimetableService newGkTimetableService;
	@Autowired
	private NewGkTeacherGroupService teacherGroupService;
	@Autowired
	private NewGkTeacherGroupExService teacherGroupExService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkLessonTime, String> getJpaDao() {
		return newGkLessonTimeDao;
	}

	@Override
	protected Class<NewGkLessonTime> getEntityClass() {
		return NewGkLessonTime.class;
	}

	@Override
	public List<NewGkLessonTime> findByItemIdObjectId(final String itemId,
			final String[] objectIds,final String[] objectTypes, boolean isMake) {
		Specification<NewGkLessonTime> specification = new Specification<NewGkLessonTime>() {

			@Override
			public Predicate toPredicate(
					Root<NewGkLessonTime> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb
						.equal(root.get("arrayItemId").as(String.class), itemId));
				if(objectIds!=null && objectIds.length>0){
					ps.add(root.get("objectId").in((String[]) objectIds));
				}
				if(objectTypes!=null && objectTypes.length>0){
					ps.add(root.get("objectType").in((String[]) objectTypes));
				}
				return cq.where(cb.and(ps.toArray(new Predicate[0]))).getRestriction();
			}
			
		};
		List<NewGkLessonTime> newGkLessonTimeList = newGkLessonTimeDao.findAll(specification);
		if(CollectionUtils.isNotEmpty(newGkLessonTimeList)){
			if(isMake){
				//组织其余数据
				toMakeTimeItem(newGkLessonTimeList);
			}
		}
		return newGkLessonTimeList;
	}

    @Override
    public List<NewGkLessonTime> findByItemIdObjectIdWithMaster(final String itemId,
                                                      final String[] objectIds,final String[] objectTypes, boolean isMake) {
	    return findByItemIdObjectId(itemId, objectIds, objectTypes, isMake);
    }
	
	public List<NewGkLessonTime> findByItemIdObjectIdAndGroupType(final String itemId,
			final String[] objectIds, final String[] objectTypes, final String groupType){
		Specification<NewGkLessonTime> specification = new Specification<NewGkLessonTime>() {

			@Override
			public Predicate toPredicate(
					Root<NewGkLessonTime> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb
						.equal(root.get("arrayItemId").as(String.class), itemId));
				if(objectIds!=null && objectIds.length>0){
					ps.add(root.get("objectId").in((String[]) objectIds));
				}
				if(objectTypes!=null && objectTypes.length>0){
					ps.add(root.get("objectType").in((String[]) objectTypes));
				}
				if(StringUtils.isNotEmpty(groupType)) {
					ps.add(cb.equal(root.get("groupType").as(String.class), groupType));
				}
				return cq.where(cb.and(ps.toArray(new Predicate[0]))).getRestriction();
			}
		};
		return newGkLessonTimeDao.findAll(specification);
	}

	private void toMakeTimeItem(List<NewGkLessonTime> newGkLessonTimeList){
		Set<String> ids = EntityUtils.getSet(newGkLessonTimeList, "id");
		List<NewGkLessonTimeEx> exList=newGkLessonTimeExService.findByObjectId(ids.toArray(new String[]{}), null);
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

	@Override
	public List<NewGkLessonTime> findByObjectTypeAndItem(String type,
			String[] itemIds,boolean isMake) {
		if(itemIds==null || itemIds.length<=0){
			return new ArrayList<NewGkLessonTime>();
		}
		List<NewGkLessonTime> list=newGkLessonTimeDao.findByObjectTypeAndItemIn(type,itemIds);
		if(CollectionUtils.isNotEmpty(list)){
			if(isMake){
				toMakeTimeItem(list);
			}
		}
		return list;
	}

	@Override
	public void saveCopyLessonTime(String arrayItemId, String[] objIds,
			List<NewGkLessonTime> insertNewGkLessonTimeList,
			List<NewGkLessonTimeEx> insertNewGkLessonTimeExList, String objType, Set<String> exSourceIds) {
		// 判断原来已有记录的科目，是否冲突，冲突的去掉
		if(CollectionUtils.isNotEmpty(exSourceIds) && CollectionUtils.isNotEmpty(insertNewGkLessonTimeExList)){
			String stype = insertNewGkLessonTimeExList.get(0).getScourceType();
			List<NewGkLessonTimeEx> exs = newGkLessonTimeExService.findByObjectId(exSourceIds.toArray(new String[0]), new String[] {stype});
			if (CollectionUtils.isNotEmpty(exs)) {
				Iterator<NewGkLessonTimeEx> it = insertNewGkLessonTimeExList.iterator();
				while (it.hasNext()) {
					NewGkLessonTimeEx ex = it.next();
					boolean hasConflict = false;
					for(NewGkLessonTimeEx old : exs) {
						if(old.getScourceTypeId().equals(ex.getScourceTypeId()) 
								&& old.getDayOfWeek().equals(ex.getDayOfWeek())
								&& old.getPeriodInterval().equals(ex.getPeriodInterval())
								&& old.getPeriod().equals(ex.getPeriod())) {
							hasConflict = true;
							break;
						}
					}
					if(hasConflict) {
						it.remove();
					}
				} 
			}
		}
		if(CollectionUtils.isNotEmpty(insertNewGkLessonTimeList)){
			saveAll(insertNewGkLessonTimeList.toArray(new NewGkLessonTime[]{}));
		}
		if(CollectionUtils.isNotEmpty(insertNewGkLessonTimeExList)){
			newGkLessonTimeExService.saveAll(insertNewGkLessonTimeExList.toArray(new NewGkLessonTimeEx[]{}));
		}
	}
	@Override
	public void deleteByIds(String[] ids){
		if(ids!=null && ids.length>0){
			SplitUtils.doSplit(Arrays.asList(ids), 1000,
					(list)->newGkLessonTimeDao.deleteByIdIn(list.toArray(new String[0])) );
		}
	}
	@Override
	public void deleteWithExByIds(String[] leeeonTimeIds){
		this.deleteByIds(leeeonTimeIds);
		newGkLessonTimeExService.deleteByScourceTypeIdIn(leeeonTimeIds);
	}

	@Override
	public void deleteByArrayItemId(String unitId, String arrayItemId) {
		 List<NewGkLessonTime>list= this.findListBy(new String[] {"arrayItemId","objectType"}, new Object[] {arrayItemId,"2"});
		 if(CollectionUtils.isNotEmpty(list)) {
			 Set<String> ids=EntityUtils.getSet(list, NewGkLessonTime::getId);
			 newGkChoRelationService.deleteByTypeChoiceIds(unitId, NewGkElectiveConstant.CHOICE_TYPE_07, ids.toArray(new String[0]));
		 }
		newGkLessonTimeExService.deleteByArrayItemId(arrayItemId);
		newGkLessonTimeDao.deleteByArrayItemId(arrayItemId);
	}

	@Override
	public void deleteByArrayItemIdAndObjectType(String itemId, String objType ,String[] objectIds) {
		List<NewGkLessonTime> list = findByItemIdObjectIdAndGroupType(itemId, objectIds, new String[] {objType}, null);
		if(CollectionUtils.isNotEmpty(list)) {
			Set<String> ids = EntityUtils.getSet(list, e->e.getId());
			newGkLessonTimeExService.deleteByScourceTypeIdIn(ids.toArray(new String[] {}));
			this.deleteByIds(ids.toArray(new String[] {}));
		}
		
		
	}

	@Override
	public void updateTeacherExisted(String unitId, String gradeId, String arrayItemId) {
		// 获取当前 排课特征内的所有教师 
    	List<NewGkTeacherPlan> tpList = newGkTeacherPlanService.findByArrayItemIds(new String[] {arrayItemId}, true);
    	Set<String> tIds = tpList.stream().flatMap(e->e.getExTeacherIdList().stream()).collect(Collectors.toSet());
    	
    	//1. 获取这些 教师 当前年级 现在开始到学期末 的课程 所在时间点
    	Grade grade = gradeRemoteService.findOneObjectById(gradeId);
    	String semesterJson = semesterRemoteService.getCurrentSemester(2, unitId);
		Semester semesterObj = SUtils.dc(semesterJson, Semester.class);
		String acadyear = semesterObj.getAcadyear();
		Integer semester = semesterObj.getSemester();
		String schoolId = grade.getSchoolId();
		Map<String, Integer> cur2Max = SUtils.dt(
				schoolCalendarRemoteService.findCurrentWeekAndMaxWeek(acadyear, semester+"", schoolId ),
				new TypeReference<Map<String, Integer>>() {
				});
		Integer weekNow = cur2Max.get("current");
		if(weekNow==null){
			weekNow=1;
		}
		String[] teacherIds = tIds.toArray(new String[0]);
    	List<CourseSchedule> courseScheduleList = SUtils.dt(courseScheduleRemoteService.findByTeacherIdsInWeekGte(schoolId, acadyear, semester, teacherIds, weekNow), CourseSchedule.class);
    	List<Clazz> classList = classRemoteService.findListObjectBy(Clazz.class, new String[] {"gradeId","isDeleted"},
    			new Object[] {gradeId,0}, new String[] {"id"});
    	List<TeachClass> teachClassList = teachClassRemoteService.findListObjectBy(TeachClass.class, new String[] {"acadyear","semester","gradeId","isDeleted","isUsing"},
    			new Object[] {acadyear,semester+"",gradeId,0,"1"}, new String[] {"id"});
    	Set<String> currClaIds = EntityUtils.getSet(classList, Clazz::getId);
    	teachClassList.forEach(e->currClaIds.add(e.getId()));
    	
    	Map<String, Integer> intervalMap = getIntervalMap(grade);
    	Integer weekDays = grade.getWeekDays();
    	final int weekNowF = weekNow;
    	int day = new Date().getDay();
    	if(day == 0) day = 7; 
    	final int dayOfWeekNow = day;
		Map<String, Set<String>> teacherTimeMap = courseScheduleList.stream()
				.filter(e -> {
					if(e.getDayOfWeek().equals(5)) {
						System.out.println();
					}
					if(currClaIds.contains(e.getClassId()) || weekDays < (e.getDayOfWeek()+1)) {
						return false;
					}
					if(e.getWeekOfWorktime() == weekNowF && (e.getDayOfWeek()+1) <= dayOfWeekNow) {
						return false;
					}
					Integer maxP = intervalMap.get(e.getPeriodInterval());
					if(maxP == null || maxP < e.getPeriod()) {
						return false;
					}
					return true;
				})
				.collect(Collectors.groupingBy(CourseSchedule::getTeacherId,
						Collectors.mapping(e -> e.getDayOfWeek() +"_"+ e.getPeriodInterval()+"_"+e.getPeriod(), Collectors.toSet())));
		
    	//2. 保存获取的时间点
		List<NewGkLessonTime> oldTeacherLTs = this.findByItemIdObjectId(arrayItemId, teacherIds, null, false);
		Map<String, String> tidLtIdMap = EntityUtils.getMap(oldTeacherLTs, NewGkLessonTime::getObjectId,NewGkLessonTime::getId);
    	List<NewGkLessonTime> ltList = new ArrayList<>();
    	List<NewGkLessonTimeEx> lteList = new ArrayList<>();
    	Date now = new Date();
    	for (String tid : teacherTimeMap.keySet()) {
    		Set<String> timeStr = teacherTimeMap.get(tid);
    		String ltId = tidLtIdMap.get(tid);
    		if(StringUtils.isBlank(ltId)) {
    			NewGkLessonTime lt = new NewGkLessonTime();
    			ltId = UuidUtils.generateUuid();
    			lt.setId(ltId);
    			lt.setArrayItemId(arrayItemId);
    			lt.setObjectId(tid);
    			lt.setCreationTime(now);
    			lt.setModifyTime(now);
    			lt.setObjectType(NewGkElectiveConstant.LIMIT_TEACHER_2);
    			lt.setIsJoin(NewGkElectiveConstant.IF_INT_1);
    			lt.setGroupType(NewGkElectiveConstant.GROUP_TYPE_1);
    			ltList.add(lt);
    		}
    		//lte
    		for (String ts : timeStr) {
    			String[] split = ts.split("_");
    			Integer d = Integer.parseInt(split[0]);
    			String pi = split[1];
    			Integer p = Integer.parseInt(split[2]);
    			
    			NewGkLessonTimeEx lte = new NewGkLessonTimeEx();
    			lte.setId(UuidUtils.generateUuid());
    			lte.setScourceTypeId(ltId);
    			lte.setDayOfWeek(d);
    			lte.setPeriodInterval(pi);
    			lte.setPeriod(p);
    			lte.setCreationTime(now);
    			lte.setModifyTime(now);
    			lte.setScourceType(NewGkElectiveConstant.SCOURCE_TEACHER_02);
    			lte.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_04);
				lteList.add(lte);
			}
		}
		
		
		//2. 清除 当前排课特征 中教师的其他年级 时间点 并且  保存获取的时间点
    	newGkLessonTimeExService.deleteByItemIdAndType(arrayItemId,NewGkElectiveConstant.LIMIT_TEACHER_2,NewGkElectiveConstant.ARRANGE_TIME_TYPE_04);
    	this.saveAll(ltList.toArray(new NewGkLessonTime[0]));
    	newGkLessonTimeExService.saveAll(lteList.toArray(new NewGkLessonTimeEx[0]));
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
	
	@Override
	public void updateTeacherGroupTime(String unitId, String gradeId, String arrayItemId) {
		// 获取当前 排课特征内的所有教师 
    	List<NewGkTeacherPlan> tpList = newGkTeacherPlanService.findByArrayItemIds(new String[] {arrayItemId}, true);
    	Set<String> teacherPlanTids = tpList.stream().flatMap(e->e.getExTeacherIdList().stream()).collect(Collectors.toSet());
		if(CollectionUtils.isEmpty(teacherPlanTids)) {
			// 没有选择任何老师
			return;
		}
    	
		List<NewGkTeacherGroup> tgList = teacherGroupService.findByObjectId(gradeId, false);
		Map<String, NewGkTeacherGroup> tgIdMap = EntityUtils.getMap(tgList, NewGkTeacherGroup::getId);
		List<NewGkTeacherGroupEx> tgExList = teacherGroupExService.findByTeacherGroupIdIn(tgIdMap.keySet().toArray(new String[0]));
		Map<String, List<String>> tgTidMap = EntityUtils.getListMap(tgExList, NewGkTeacherGroupEx::getTeacherGroupId, NewGkTeacherGroupEx::getTeacherId);
		
		List<NewGkLessonTime> noTimeList = this.findByItemIdObjectId(gradeId, null, 
				new String[] {NewGkElectiveConstant.LIMIT_TEACHER_GROUP_6}, true);
		
		Map<String,Set<String>> teacherTimeMap = new HashMap<>();
		List<NewGkLessonTimeEx> nullList = new ArrayList<>();
		for (NewGkLessonTime lt : noTimeList) {
			List<String> tids = tgTidMap.get(lt.getObjectId());
			List<NewGkLessonTimeEx> timesList = lt.getTimesList();
			if(CollectionUtils.isEmpty(tids)) {
				continue;
			}
			Set<String> noTimes = EntityUtils.getSet((timesList==null?nullList:timesList), e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod());
			for (String tid : tids) {
				if(!teacherPlanTids.contains(tid)) {
					continue;
				}
				Set<String> times = teacherTimeMap.get(tid);
				if(times == null) {
					times = new HashSet<>();
					teacherTimeMap.put(tid, times);
				}
				times.addAll(noTimes);
			}
		}
		
    	
		//2. 保存获取的时间点
		List<NewGkLessonTime> oldTeacherLTs = this.findByItemIdObjectId(arrayItemId, teacherPlanTids.toArray(new String[0]) , 
				new String[] {NewGkElectiveConstant.LIMIT_TEACHER_2}, true);
		Map<String, NewGkLessonTime> tidLtIdMap = EntityUtils.getMap(oldTeacherLTs, NewGkLessonTime::getObjectId,e->e);
    	List<NewGkLessonTime> ltList = new ArrayList<>();
    	List<NewGkLessonTimeEx> lteList = new ArrayList<>();
    	Date now = new Date();
    	List<String> delSourceTypeIds = new ArrayList<>();
    	for (String tid : teacherTimeMap.keySet()) {
    		Set<String> timeStr = teacherTimeMap.get(tid);
    		if(CollectionUtils.isEmpty(timeStr)) {
//    			continue;
    		}
    		NewGkLessonTime lt = tidLtIdMap.get(tid);
			if(lt == null) {
    			lt = new NewGkLessonTime();
    			lt.setId(UuidUtils.generateUuid());
    			lt.setArrayItemId(arrayItemId);
    			lt.setObjectId(tid);
    			lt.setCreationTime(now);
    			lt.setModifyTime(now);
    			lt.setObjectType(NewGkElectiveConstant.LIMIT_TEACHER_2);
    			lt.setIsJoin(NewGkElectiveConstant.IF_INT_1);
    			lt.setGroupType(NewGkElectiveConstant.GROUP_TYPE_1);
    			ltList.add(lt);
    		}else {
    			delSourceTypeIds.add(lt.getId());
    		}
			List<NewGkLessonTimeEx> timesList = lt.getTimesList();
			if(CollectionUtils.isNotEmpty(timesList)) {
				List<NewGkLessonTimeEx> collect = timesList.stream()
						.filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_04.equals(e.getTimeType()) 
								|| (NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(e.getTimeType())
							&&!timeStr.contains(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())))
						.collect(Collectors.toList());
				lteList.addAll(EntityUtils.copyProperties(collect, NewGkLessonTimeEx.class, NewGkLessonTimeEx.class));
			}
    		//lte
    		for (String ts : timeStr) {
    			String[] split = ts.split("_");
    			Integer d = Integer.parseInt(split[0]);
    			String pi = split[1];
    			Integer p = Integer.parseInt(split[2]);
    			
    			NewGkLessonTimeEx lte = new NewGkLessonTimeEx();
    			lte.setId(UuidUtils.generateUuid());
    			lte.setScourceTypeId(lt.getId());
    			lte.setDayOfWeek(d);
    			lte.setPeriodInterval(pi);
    			lte.setPeriod(p);
    			lte.setCreationTime(now);
    			lte.setModifyTime(now);
    			lte.setScourceType(NewGkElectiveConstant.SCOURCE_TEACHER_02);
    			lte.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_05);
    			lte.setArrayItemId(arrayItemId);
				lteList.add(lte);
			}
		}
    	
		this.saveAutoBatchResult(delSourceTypeIds, ltList, lteList, null);
	}

	@Override
	public void saveAutoBatchResult(List<String> delSourceTypeIds, List<NewGkLessonTime> lessonTimeList,
			List<NewGkLessonTimeEx> lessonTimeExList, String arrayId) {
		if(CollectionUtils.isNotEmpty(delSourceTypeIds)) {
			newGkLessonTimeExService.deleteByScourceTypeIdIn(delSourceTypeIds.toArray(new String[0]));
		}
		if(CollectionUtils.isNotEmpty(lessonTimeList)) {
			this.saveAll(lessonTimeList.toArray(new NewGkLessonTime[0]));
		}
		if(CollectionUtils.isNotEmpty(lessonTimeExList)) {
			newGkLessonTimeExService.saveAll(lessonTimeExList.toArray(new NewGkLessonTimeEx[0]));
		}
		
		if(StringUtils.isNotBlank(arrayId)) {
			newGkTimetableService.updatePreTimetableByBatch(arrayId);
		}
	}
	
}
