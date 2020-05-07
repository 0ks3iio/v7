package net.zdsoft.newgkelective.data.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.newgkelective.data.entity.*;
import net.zdsoft.newgkelective.data.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkSubjectTimeDao;
import net.zdsoft.newgkelective.data.dto.NewGKCourseFeatureDto;

@Service("newGkSubjectTimeService")
public class NewGkSubjectTimeServiceImpl extends BaseServiceImpl<NewGkSubjectTime, String> implements NewGkSubjectTimeService{

	@Autowired
	private NewGkSubjectTimeDao newGkSubjectTimeDao;
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkLessonTimeExService newGkLessonTimeExService;
	@Autowired
	private NewGkRelateSubtimeService newGkRelateSubtimeService;
	@Autowired
	private NewGkClassSubjectTimeService newGkClassSubjectTimeService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkOpenSubjectService openSubjectService;
	@Autowired
	private NewGkTeacherPlanService teacherPlanService;
	@Autowired
	private NewGkTeacherPlanExService teacherPlanExService;
	@Autowired
	private NewGkArrayService arrayService;
	@Autowired
	private NewGkTimetableService timetableService;

	@Override
	protected BaseJpaRepositoryDao<NewGkSubjectTime, String> getJpaDao() {
		return newGkSubjectTimeDao;
	}

	@Override
	protected Class<NewGkSubjectTime> getEntityClass() {
		return NewGkSubjectTime.class;
	}

	public void saveWithChoRelation(List<NewGkSubjectTime> subjectTimeList, List<NewGkChoRelation> saveList, String[] choiceIds, String unitId) {
		if(CollectionUtils.isNotEmpty(subjectTimeList)) {
			this.saveAll(subjectTimeList.toArray(new NewGkSubjectTime[0]));
		}
		newGkChoRelationService.saveAndDeleteByList(saveList, choiceIds,unitId,NewGkElectiveConstant.CHOICE_TYPE_08);
	}
	
	@Override
	public String saveList(String divideId, List<NewGkSubjectTime> subjectTimeList) {
		NewGkDivide newGkDivide = newGkDivideService.findOneBy("id", divideId);
		NewGkArrayItem item = new NewGkArrayItem();
		String itemId = UuidUtils.generateUuid();
		item.setId(itemId);
		item.setDivideId(divideId);
		item.setDivideType(NewGkElectiveConstant.ARRANGE_TYPE_03);
		List<NewGkArrayItem> itemList = newGkArrayItemService.findByDivideId(divideId, new String[]{NewGkElectiveConstant.ARRANGE_TYPE_03});
		List<Integer> timesList = EntityUtils.getList(itemList, "times");
		Integer times=1;
		if(CollectionUtils.isNotEmpty(timesList)){
			times = Collections.max(timesList)+1;
		}
		item.setTimes(times);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(newGkDivide.getGradeId()),Grade.class);
		//获取学年学期等信息
		String semesterJson = semesterRemoteService.getCurrentSemester(2, newGkDivide.getUnitId());
		Semester semester = SUtils.dc(semesterJson, Semester.class);
		String itemName = semester.getAcadyear()+"学年"+grade.getGradeName()+"第"+semester.getSemester()+"学期每周课时安排"+times;
		
		item.setItemName(itemName);
		item.setIsDeleted(0);
		item.setCreationTime(new Date());
		item.setModifyTime(new Date());
		for (NewGkSubjectTime subjectTime : subjectTimeList) {
			subjectTime.setId(UuidUtils.generateUuid());
			subjectTime.setArrayItemId(itemId);
			subjectTime.setCreationTime(new Date());
			subjectTime.setModifyTime(new Date());
		}
		newGkArrayItemService.save(item);
		newGkSubjectTimeDao.saveAll(subjectTimeList);
		return itemId;
	}

	@Override
	public void updateList(List<NewGkSubjectTime> subjectTimeList) {
//		List<String> idList = EntityUtils.getList(subjectTimeList, "id");
		List<String> idList = subjectTimeList.stream().map(NewGkSubjectTime::getId).collect(Collectors.toList());
		List<NewGkSubjectTime> oldSubjectTimeList = newGkSubjectTimeDao.findByIdIn(idList);
		Map<String, NewGkSubjectTime> oldmap = EntityUtils.getMap(oldSubjectTimeList, "id");
		for (NewGkSubjectTime newGkSubjectTime : subjectTimeList) {
			NewGkSubjectTime item = oldmap.get(newGkSubjectTime.getId());
			if(item==null){
				newGkSubjectTime.setId(UuidUtils.generateUuid());
				newGkSubjectTime.setCreationTime(new Date());
				newGkSubjectTime.setModifyTime(new Date());
			}else{
//				newGkSubjectTime.setPeriod(item.getPeriod());
//				newGkSubjectTime.setWeekRowNumber(item.getWeekRowNumber());
//				newGkSubjectTime.setWeekRowPeriod(item.getWeekRowPeriod());
//				newGkSubjectTime.setFirstsdWeek(item.getFirstsdWeek());
//				newGkSubjectTime.setPunchCard(item.getPunchCard());
				newGkSubjectTime.setModifyTime(new Date());
			}
		}
		
		if(CollectionUtils.isNotEmpty(subjectTimeList)){
			newGkSubjectTimeDao.saveAll(subjectTimeList);
		}
	}

	
	
	@Override
	public void deleteByArrayItemId(String unitId, String itemId) {
		List<NewGkSubjectTime> list= newGkSubjectTimeDao.findByArrayItemId(itemId);
		newGkChoRelationService.deleteByTypeChoiceIds(unitId, NewGkElectiveConstant.CHOICE_TYPE_08, EntityUtils.getSet(list, NewGkSubjectTime::getId).toArray(new String[0]));
		newGkSubjectTimeDao.deleteAll(list);
	}

	@Override
	public List<NewGkSubjectTime> findByArrayItemId(String subjectArrangeId) {
		return newGkSubjectTimeDao.findByArrayItemId(subjectArrangeId);
	}
	
	public List<NewGkSubjectTime> findByArrayItemIdAndGroupType(String itemId, String groupType){
		return findByArrayItemId(itemId);
//		if(StringUtils.isEmpty(groupType)) {
//			return findByArrayItemId(itemId);
//		}
//		return newGkSubjectTimeDao.findByArrayItemIdAndGroupType(itemId, groupType);
	}

	@Override
	public List<NewGkSubjectTime> findByArrayItemIdAndSubjectTypeIn(String subjectArrangeId, String[] subjectTypes) {
		return newGkSubjectTimeDao.findByArrayItemIdAndSubjectTypeIn(subjectArrangeId,subjectTypes);
	}

	@Override
	public void updateByArrayItem(String unitId, List<NewGkSubjectTime> subjectTimeList) {
		if(CollectionUtils.isEmpty(subjectTimeList)) {
			return;
		}
		String arrayItemId = subjectTimeList.get(0).getArrayItemId();
		deleteByArrayItemId(unitId, arrayItemId);
		saveAll(subjectTimeList.toArray(new NewGkSubjectTime[] {}));
	}

	@Override
	public void saveAllSubjectTimeItem(String unitId, String itemId,
									   List<NewGkSubjectTime> subjectTimeList, List<NewGkLessonTime> lessonTimeList,
									   List<NewGkLessonTimeEx> lessonTimeExList, List<NewGkRelateSubtime> relateSubtimeList, String arrayId) {
		/** 更新 班级课程特征/教师特征 对应的信息，务必要在保存之前执行*/
		NewGkArrayItem arrayItem = newGkArrayItemService.findOne(itemId);
		if(arrayItem != null) {
			newGkClassSubjectTimeService.updateBySubjectTimeChange(unitId, itemId, subjectTimeList, lessonTimeList, lessonTimeExList);
			// 根据课程是否跟随 组合班上课， 更新教师 特征数据
			updateByZhbCourse(itemId, subjectTimeList);
			// 更新 手动调课 课表 以及关联的 教学班
			if(StringUtils.isNotBlank(arrayId)){
				timetableService.updateByCouseFeature(arrayId,subjectTimeList);
			}
		}
		
		//删除课程时间
		newGkLessonTimeService.deleteByArrayItemIdAndObjectType(itemId,NewGkElectiveConstant.LIMIT_SUBJECT_9,null);
		//删除课程
		deleteByArrayItemId(unitId, itemId);
		//删除关联,并保存
		newGkRelateSubtimeService.saveAllNewGkRelateSubtime(itemId,relateSubtimeList);
		if(CollectionUtils.isNotEmpty(subjectTimeList)) {
			this.saveAll(subjectTimeList.toArray(new NewGkSubjectTime[] {}));
		}
		if(CollectionUtils.isNotEmpty(lessonTimeList)) {
			newGkLessonTimeService.saveAll(lessonTimeList.toArray(new NewGkLessonTime[] {}));
		}
		if(CollectionUtils.isNotEmpty(lessonTimeExList)) {
			newGkLessonTimeExService.saveBatch(lessonTimeExList);
		}
		
	}

	private void updateByZhbCourse(String itemId, List<NewGkSubjectTime> subjectTimeList) {
		List<NewGkSubjectTime> oldSubjectTimeList = newGkSubjectTimeService.findByArrayItemId(itemId);
		Map<String, NewGkSubjectTime> newSubjectMap = EntityUtils.getMap(subjectTimeList, e->e.getSubjectId()+"-"+e.getSubjectType());

		Set<String> delSubSet = new HashSet<>();
		for (NewGkSubjectTime oldSt: oldSubjectTimeList){
			String code = oldSt.getSubjectId() + "-" + oldSt.getSubjectType();
			NewGkSubjectTime newSt = newSubjectMap.get(code);
			if(newSt != null){
				Integer followZhb1 = Optional.of(newSt.getFollowZhb()).orElse(0);
				Integer followZhb2 = Optional.of(oldSt.getFollowZhb()).orElse(0);
				if(!Objects.equals(followZhb1,followZhb2)){
					delSubSet.add(oldSt.getSubjectId());
				}
			}
		}
		// 清除教师 安排数据
		List<NewGkTeacherPlan> tpList = teacherPlanService.findByArrayItemIdAndSubjectIdIn(itemId, delSubSet.toArray(new String[0]), true);
		List<NewGkTeacherPlanEx> updateTpList = tpList.stream().filter(e -> CollectionUtils.isNotEmpty(e.getTeacherPlanExList())).flatMap(e -> e.getTeacherPlanExList().stream())
				.peek(e -> {
					e.setClassIds(null);
					e.setMutexNum(0);
				}).collect(Collectors.toList());
		teacherPlanExService.saveAll(updateTpList.toArray(new NewGkTeacherPlanEx[0]));
	}

	@Override
	public void deleteOneSubject(String unitId, String itemId, String subjectId, String subjectType, String arrayId) {
		
 		List<NewGkSubjectTime> list = this.findListBy(new String[] {"arrayItemId","subjectId","subjectType"}, new String[] {itemId,subjectId,subjectType});
		if(CollectionUtils.isNotEmpty(list)) {
			//1、删除NewGkSubjectTime
			//2、删除对应禁排时间，指定时间
			List<NewGkLessonTime> newGkLessonTimeList = newGkLessonTimeService.findByItemIdObjectIdAndGroupType(itemId,
					null, new String[] {  NewGkElectiveConstant.LIMIT_SUBJECT_9 },
					null);
			Set<String> timeId=new HashSet<>();
			if(CollectionUtils.isNotEmpty(newGkLessonTimeList)) {
				for(NewGkLessonTime time:newGkLessonTimeList) {
					if(time.getObjectId().equals(subjectId) && subjectType.equals(time.getLevelType())) {
						timeId.add(time.getId());
					}
				}
			}
			
			if(CollectionUtils.isNotEmpty(timeId)) {
				newGkLessonTimeService.deleteByIds(timeId.toArray(new String[] {}));
				newGkLessonTimeExService.deleteByScourceTypeIdIn(timeId.toArray(new String[] {}));
			}
			//修改单双周
			newGkSubjectTimeDao.updateFirstsdWeekSubject(itemId,subjectId+"-"+subjectType);
			newGkSubjectTimeDao.deleteByIdIn(EntityUtils.getSet(list,NewGkSubjectTime::getId).toArray(new String[] {}));
			//删除关联 不连排科目
			newGkRelateSubtimeService.deleteLikeSubjectIdType(itemId,subjectId+"_"+subjectType);
			
			// 删除科目时 同时删除 班级特征对应数据
			NewGkArrayItem item = newGkArrayItemService.findOne(itemId);
			if(item != null){
				newGkClassSubjectTimeService.deleteClassFeatureBySubjectTime(unitId, list);
			}

			// 删除 课程时 删除 课表 以及组合班 对应班级;务必要在 删除科目以后 执行
			if(StringUtils.isNotBlank(arrayId)){
				List<NewGkSubjectTime> subjectTimeList = findByArrayItemId(itemId);
				timetableService.updateByCouseFeature(arrayId,subjectTimeList);
				timetableService.deleteScheduleBySubIds(arrayId,new String[]{subjectId+"-"+subjectType});
			}

		}else {
			//没有值 页面直接删除
			
		}
	}

    @Override
    public void deleteBySubjectIds(String... subids) {
        newGkSubjectTimeDao.deleteBySubjectIdIn(subids);
    }

    @Override
	public List<NewGkSubjectTime> findByArrayItemIdAndSubjectId(String itemId, String[] subjectIds,
			String[] subjectTypes) {
		
		if(StringUtils.isBlank(itemId))
			return new ArrayList<>();
		
		Specification<NewGkSubjectTime> specification = new Specification<NewGkSubjectTime>() {
			@Override
			public Predicate toPredicate(Root<NewGkSubjectTime> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> pl = new ArrayList<>();
				pl.add(cb.equal(root.get("arrayItemId"), itemId));
				if(subjectIds != null && subjectIds.length >0) {
					pl.add(root.get("subjectId").in(subjectIds));
				}
				if(subjectTypes != null && subjectTypes.length >0) {
					pl.add(root.get("subjectType").in(subjectTypes));
				}
				
				return cq.where(pl.toArray(new Predicate[0])).getRestriction();
			}
		};
		
		return newGkSubjectTimeDao.findAll(specification);
	}

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
    private void relateSubtimeList(String arrayItemId, Map<String, Integer> subjectNoContinue,
			List<String[]> subjectNoContinueList) {
		List<NewGkRelateSubtime> relateSubtimeList = newGkRelateSubtimeService.findListByItemId(arrayItemId);
		if (CollectionUtils.isNotEmpty(relateSubtimeList)) {
			for (NewGkRelateSubtime n : relateSubtimeList) {
				String sIds = n.getSubjectIds();
				String[] arg = sIds.split(",");
				String type = n.getType();
				subjectNoContinueList.add(new String[] { arg[0] + "_" + arg[1], type });
				if (subjectNoContinue.containsKey(arg[0])) {
					subjectNoContinue.put(arg[0], subjectNoContinue.get(arg[0]) + 1);
				} else {
					subjectNoContinue.put(arg[0], 1);
				}
				if (subjectNoContinue.containsKey(arg[1])) {
					subjectNoContinue.put(arg[1], subjectNoContinue.get(arg[1]) + 1);
				} else {
					subjectNoContinue.put(arg[1], 1);
				}
			}
		}
	}
	@Override
	public List<NewGKCourseFeatureDto> findCourseFeatures(String arrayItemId, Map<String, Object> map,
			Grade grade, String unitId) {
		// 只取必修课
		List<Course> xzbCourseList = SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection(unitId,BaseConstants.SUBJECT_TYPE_BX, grade.getSection()+""), new TR<List<Course>>() {});
		List<Course> fixedSubs = SUtils.dt(courseRemoteService.getListByCondition(unitId, null, 
				 null, BaseConstants.ZERO_GUID, null, 1, null), new TR<List<Course>>() {});
		Set<String> fixedIds = EntityUtils.getSet(fixedSubs, Course::getId);
		// 屏蔽 掉固定排课科目
		xzbCourseList = xzbCourseList.stream().filter(e->!fixedIds.contains(e.getId())).collect(Collectors.toList());
		
		map.put("xzbCourseList", xzbCourseList);
		List<Course> gkCourseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {});
		map.put("gkCourseList", gkCourseList);
		
		// 年级基本设置--object_type=0 objectId=gradeId(不排课) objectId=subjectId(固定科目排课)
		// 科目设置--object_type=9 objectId=subjectId LevelType=A/B/O
		List<NewGkLessonTime> newGkLessonTimeList = newGkLessonTimeService.findByItemIdObjectIdAndGroupType(arrayItemId,
				null, new String[] { NewGkElectiveConstant.LIMIT_GRADE_0, NewGkElectiveConstant.LIMIT_SUBJECT_9 },
				null);
		// 年级默认已设置时间点
		Map<String, String> noClickTimeMap = new HashMap<>();
		// 科目时间 key:subjectId-A,不排课，时间
		Map<String, Map<String, List<String>>> subjectTimeExMap = new HashMap<>();
		makeTimeEx(newGkLessonTimeList, noClickTimeMap, subjectTimeExMap);

		map.put("noClickTimeMap", noClickTimeMap);
		String openType = (String)map.get("openType");
		boolean isXzbArray = false;
		if(StringUtils.isNotBlank(openType) && NewGkElectiveConstant.DIVIDE_TYPE_07.equals(openType)) {
			isXzbArray = true;
		}

		// 科目
		List<NewGKCourseFeatureDto> dtoList = new ArrayList<>();
		NewGKCourseFeatureDto dto;
			
		List<NewGkSubjectTime> sujectTimeList = newGkSubjectTimeService.findByArrayItemId(arrayItemId);

		Set<String> subjectIds = EntityUtils.getSet(sujectTimeList, NewGkSubjectTime::getSubjectId);
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[] {})), Course.class);
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		
		//获取不可删除科目 即分班方案中存在的科目
		Set<String> jxbSubIdTypes=new HashSet<>();
		Map<String, String> toParentCourse = new HashMap<>();
		if(!Objects.equals(arrayItemId, grade.getId())) {
			NewGkArrayItem arrayItem = newGkArrayItemService.findOne(arrayItemId);
			NewGkDivide divide = newGkDivideService.findOne(arrayItem.getDivideId());
			List<NewGkOpenSubject> osList = openSubjectService.findByDivideIdAndGroupType(divide.getId(), NewGkElectiveConstant.GROUP_TYPE_1);
			jxbSubIdTypes = osList.stream().filter(e->NewGkElectiveConstant.SUBJECT_TYPE_A.equals(e.getSubjectType()) 
					|| NewGkElectiveConstant.SUBJECT_TYPE_B.equals(e.getSubjectType())).map(e->e.getSubjectId()+e.getSubjectType())
					.collect(Collectors.toSet());
			if(!NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType())) {
				// 7 选3 排课特征
				
				// 存在绑定科目 gkCourseList ...
				List<NewGkChoRelation> relaList = newGkChoRelationService.findByChoiceIdsAndObjectType(unitId, 
						new String[] {divide.getId()}, NewGkElectiveConstant.CHOICE_TYPE_09);
				Map<String, List<String>> relaMap = EntityUtils.getListMap(relaList, NewGkChoRelation::getObjectTypeVal, NewGkChoRelation::getObjectValue);
				Set<String> chCids = EntityUtils.getSet(relaList, NewGkChoRelation::getObjectValue);
				List<Course> childrenCoruses = courseRemoteService.findListObjectByIds(chCids.toArray(new String[0]));
				Map<String, Course> childCourseMap = EntityUtils.getMap(childrenCoruses, Course::getId);
				List<Course> newList = new ArrayList<>();
				for (Course course : gkCourseList) {
					newList.add(course);
					if(relaMap.containsKey(course.getId())) {
						List<String> list = relaMap.get(course.getId());
						List<Course> children = list.stream().filter(e->childCourseMap.containsKey(e)).map(e->childCourseMap.get(e)).collect(Collectors.toList());
						children = EntityUtils.copyProperties(children, Course.class, Course.class);
						children.forEach(e->{
							e.setSubjectName(e.getSubjectName()+"("+course.getSubjectName()+")");
							toParentCourse.put(e.getId(), course.getSubjectName());
							});
						newList.addAll(children);
					}
				}
				map.put("gkCourseList", newList);
				map.put("parMap", relaMap);
			}
		}
		
		// 连排科目
		Map<String, Integer> subjectNoContinue = new HashMap<String, Integer>();
		List<String[]> subjectNoContinueList = new ArrayList<>();
		relateSubtimeList(arrayItemId, subjectNoContinue, subjectNoContinueList);
		map.put("subjectNoContinueList", subjectNoContinueList);

		for (NewGkSubjectTime item : sujectTimeList) {
			dto = new NewGKCourseFeatureDto();
			
			if(jxbSubIdTypes.contains(item.getSubjectId()+item.getSubjectType())) {
				dto.setDeletedStr(1);
			}
			
			String courseName = courseMap.get(item.getSubjectId()).getSubjectName();
			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(item.getSubjectType())
					|| NewGkElectiveConstant.SUBJECT_TYPE_B.equals(item.getSubjectType())) {
				if(toParentCourse.containsKey(item.getSubjectId())) {
					courseName = courseName
							+ (item.getSubjectType().equals(NewGkElectiveConstant.SUBJECT_TYPE_A) ? "选" : "学")+"("+toParentCourse.get(item.getSubjectId())+")";
				}else if(isXzbArray){
					courseName = courseName+"走";
				}else {
					courseName = courseName
							+ (item.getSubjectType().equals(NewGkElectiveConstant.SUBJECT_TYPE_A) ? "选" : "学");
				}
			}
			dto.setCourseName(courseName);
			dto.setSubjectId(item.getSubjectId());
			dto.setSubjectType(item.getSubjectType());
			dto.setOrder(courseMap.get(item.getSubjectId()).getOrderId());
			
			
			dto.setCourseWorkDay(item.getPeriod());
			dto.setCourseCoupleType(item.getWeekRowType());
			dto.setCourseCoupleTimes(item.getWeekRowNumber());
			dto.setNeedRoom(item.getIsNeed());
			dto.setFollowZhb(item.getFollowZhb());
			dto.setPunchCard(item.getPunchCard());
			// dto.setFirstsdWeek(item.getFirstsdWeek());
			// 单双周科目
			dto.setFirstsdWeekSubjectId(item.getFirstsdWeekSubject());
			dto.setArrangeDay(item.getArrangeDay());
			dto.setArrangeHalfDay(item.getArrangeHalfDay());
			dto.setArrangePrior(item.getArrangeFrist());
			// 不连排科目数量
			if (subjectNoContinue.containsKey(item.getSubjectId() + "-" + item.getSubjectType())) {
				dto.setNoContinueNum(subjectNoContinue.get(item.getSubjectId() + "-" + item.getSubjectType()));
			} else {
				dto.setNoContinueNum(0);
			}

			Map<String, List<String>> map1 = subjectTimeExMap
					.get(item.getSubjectId() + "-" + item.getSubjectType());
			if (MapUtils.isNotEmpty(map1)) {
				// 禁止排时间以及数量
				List<String> ll1 = map1.get(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
				if (CollectionUtils.isNotEmpty(ll1)) {
					dto.setNoArrangeTimeNum(ll1.size());
					dto.setNoArrangeTime(ArrayUtil.print(ll1.toArray(new String[] {})));
				}
				// 连排时间
				List<String> ll2 = map1.get(NewGkElectiveConstant.ARRANGE_TIME_TYPE_03);
				if (CollectionUtils.isNotEmpty(ll2)) {
					dto.setCourseCoupleTypeTimes(ArrayUtil.print(ll2.toArray(new String[] {})));
				}
			}
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public List<NewGKCourseFeatureDto> findCourseFeaturesWithMaster(String arrayItemId, Map<String, Object> map,
			Grade grade, String unitId) {
		return findCourseFeatures(arrayItemId, map, grade, unitId);
	}

}
