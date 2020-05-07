package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkClassSubjectTimeDao;
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
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeExService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;

@Service("newGkClassSubjectTimeService")
public class NewGkClassSubjectTimeServiceImpl extends BaseServiceImpl<NewGkClassSubjectTime, String>
		implements NewGkClassSubjectTimeService {
	@Autowired
	private NewGkClassSubjectTimeDao newGkClassSubjectTimeDao;
	@Autowired
	private NewGkClassCombineRelationService newGkClassCombineRelationService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkLessonTimeExService newGkLessonTimeExService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private NewGkArrayItemService arrayItemService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkClassSubjectTime, String> getJpaDao() {
		return newGkClassSubjectTimeDao;
	}

	@Override
	protected Class<NewGkClassSubjectTime> getEntityClass() {
		return NewGkClassSubjectTime.class;
	}

	@Override
	public List<NewGkClassSubjectTime> findByArrayItemIdAndClassIdIn(String unitId, String arrayItemId, String[] classIds,
			String[] subjectIds, String[] subjectTypes) {
		if(StringUtils.isBlank(unitId) || StringUtils.isBlank(arrayItemId))
			return new ArrayList<>();
		
		Specification<NewGkClassSubjectTime> specification = new Specification<NewGkClassSubjectTime>() {
			@Override
			public Predicate toPredicate(Root<NewGkClassSubjectTime> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> pl = new ArrayList<>();
				pl.add(cb.equal(root.get("unitId"), unitId));
				pl.add(cb.equal(root.get("arrayItemId"), arrayItemId));
				if(classIds != null && classIds.length >0) {
					pl.add(root.get("classId").in(classIds));
				}
				if(subjectIds != null && subjectIds.length >0) {
					pl.add(root.get("subjectId").in(subjectIds));
				}
				if(subjectTypes != null && subjectTypes.length >0) {
					pl.add(root.get("subjectType").in(subjectTypes));
				}
//				cb.selectCase(root.get("subjectType"));
				
				return cq.where(pl.toArray(new Predicate[0])).getRestriction();
			}
		};
		
		return newGkClassSubjectTimeDao.findAll(specification);
	}

	@Override
	public void saveResult(NewGkClassFeatureDto dto) {
//		int i=1/0;
		if(CollectionUtils.isNotEmpty(dto.getClassSubjectTimeList())) {
			newGkClassSubjectTimeDao.saveAll(dto.getClassSubjectTimeList());
		}
		if(CollectionUtils.isNotEmpty(dto.getClassCombineList())) {
			newGkClassCombineRelationService.saveAll(dto.getClassCombineList().toArray(new NewGkClassCombineRelation[0]));
		}
		if(CollectionUtils.isNotEmpty(dto.getLessonTimeList())) {
			newGkLessonTimeService.saveAll(dto.getLessonTimeList().toArray(new NewGkLessonTime[0]));
		}
		if(CollectionUtils.isNotEmpty(dto.getDelTimeExScourceIdList())) {
			newGkLessonTimeExService.deleteByScourceTypeIdIn(dto.getDelTimeExScourceIdList().toArray(new String[0]));
		}
		if(CollectionUtils.isNotEmpty(dto.getLessonTimeExList())) {
			newGkLessonTimeExService.saveBatch(dto.getLessonTimeExList());
		}
		if(CollectionUtils.isNotEmpty(dto.getDelRelaList())) {
			newGkClassCombineRelationService.deleteAll(dto.getDelRelaList().toArray(new NewGkClassCombineRelation[0]));
		}
	}

	@Override
	public void updateBySubjectTimeChange(String unitId, String itemId,
			List<NewGkSubjectTime> newSubjectTimeList, List<NewGkLessonTime> newLessonTimeList,
                                          List<NewGkLessonTimeEx> newLessonTimeExList) {
		// 获取历史课程特征数据进行对比
		List<NewGkSubjectTime> oldSubjectTimeList = newGkSubjectTimeService.findByArrayItemId(itemId);
		List<NewGkLessonTime> oldLessonTimeList = newGkLessonTimeService.findByItemIdObjectId(itemId, null,
				new String[] {NewGkElectiveConstant.LIMIT_SUBJECT_9}, true);

		Map<String, NewGkSubjectTime> oldSubjectMap = EntityUtils.getMap(oldSubjectTimeList, e->e.getSubjectId()+"-"+e.getSubjectType());
		Map<String, NewGkSubjectTime> newSubjectMap = EntityUtils.getMap(newSubjectTimeList, e->e.getSubjectId()+"-"+e.getSubjectType());

		Set<String> delCodeSet = new HashSet<>();
		List<String> delRelaCodeList = new ArrayList<>();
		for (NewGkSubjectTime oldSt: oldSubjectTimeList){
			String code = oldSt.getSubjectId() + "-" + oldSt.getSubjectType();
			NewGkSubjectTime newSt = newSubjectMap.get(code);

			if(newSt != null){
				Integer followZhb1 = Optional.of(newSt.getFollowZhb()).orElse(0);
				Integer followZhb2 = Optional.of(oldSt.getFollowZhb()).orElse(0);
				if(!Objects.equals(followZhb1,followZhb2)){
					delCodeSet.add(code);
					delRelaCodeList.add(code);
				}
			}
		}

		// k:subId-subType  V:2-2-3,2-2-4
		Map<String, Set<String>> oldNoTimeMap = getNoTimeMap(oldLessonTimeList);
		Map<String, List<NewGkLessonTimeEx>> listMap = EntityUtils.getListMap(newLessonTimeExList, NewGkLessonTimeEx::getScourceTypeId,Function.identity());
		for (NewGkLessonTime lt : newLessonTimeList) {
			if(listMap.get(lt.getId()) != null) {
				lt.setTimesList(listMap.get(lt.getId()));
			}
		}
		Map<String, Set<String>> newNoTimeMap = getNoTimeMap(newLessonTimeList);

		Map<String,Integer> periodUpMap = new HashMap<>();
		Map<String,Set<String>> noTimeUpMap = new HashMap<>();
		final Set<String> empty = new HashSet<>();
		Set<String> resetWeekTypes = new HashSet<>();
		for (String code : oldSubjectMap.keySet()) {
			// code: subjectId-subjectType
			NewGkSubjectTime oldSt = oldSubjectMap.get(code);
            NewGkSubjectTime newSt = newSubjectMap.get(code);
            if(newSt ==null) {
				// 此科目已经删除
				delCodeSet.add(code);
				delRelaCodeList.add(code);
				continue;
			}
            if(!oldSt.getPeriod().equals(newSt.getPeriod())) {
				periodUpMap.put(code, newSt.getPeriod());
			}
            if(!Objects.equals(oldNoTimeMap.get(code), newNoTimeMap.get(code))){
				noTimeUpMap.put(code, newNoTimeMap.get(code)==null?empty:newNoTimeMap.get(code));
			}
            if(!Objects.equals(oldSt.getFirstsdWeekSubject(),newSt.getFirstsdWeekSubject())){
                resetWeekTypes.add(code);
            }
		}

		if(CollectionUtils.isNotEmpty(delCodeSet) || periodUpMap.size()>0 || noTimeUpMap.size()>0 || resetWeekTypes.size()>0) {
			this.deleteBySubjectCode(unitId, itemId, delCodeSet,delRelaCodeList.stream().distinct().collect(Collectors.toList()),
                    periodUpMap,noTimeUpMap,resetWeekTypes);
		}
	}

	/**
	 * K:objectId + levelType  V: 时间字符串 d-pinterval-p
	 * @param lessonTimeList
	 * @return
	 */
	private Map<String, Set<String>> getNoTimeMap(List<NewGkLessonTime> lessonTimeList) {
		Map<String,Set<String>> oldNoTimeMap = new HashMap<>();
		for (NewGkLessonTime lt : lessonTimeList) {
			
			Set<String> times = lt.getTimesList().stream()
					.filter(e->NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(e.getTimeType()))
					.map(e->e.getDayOfWeek()+"-"+e.getPeriodInterval()+"-"+e.getPeriod())
					.collect(Collectors.toSet());
			
			oldNoTimeMap.put(lt.getObjectId()+"-"+lt.getLevelType(), times);
		}
		return oldNoTimeMap;
	}

	@Override
	public void deleteBySubjectCode(String unitId, String arrayItemId, Set<String> delSubjectCodes, List<String> delRelaCodeList,
                                    Map<String, Integer> periodUpMap, Map<String, Set<String>> noTimeUpMap, Set<String> resetWeekTypes) {
		NewGkArrayItem arrayItem = arrayItemService.findOne(arrayItemId);
		if(arrayItem == null) {
			return;
		}
		if(periodUpMap == null) {
			periodUpMap = new HashMap<>();
		}
		if(noTimeUpMap == null) {
			noTimeUpMap = new HashMap<>();
		}
		if(resetWeekTypes == null) {
            resetWeekTypes = new HashSet<>();
		}

		Set<String> subjectIds = delSubjectCodes.stream().map(e->e.split("-")[0]).collect(Collectors.toSet());
		subjectIds.addAll(periodUpMap.keySet().stream().map(e->e.split("-")[0]).collect(Collectors.toList()));
		subjectIds.addAll(noTimeUpMap.keySet().stream().map(e->e.split("-")[0]).collect(Collectors.toList()));
		Set<String> subjectTypes = delSubjectCodes.stream().map(e->e.split("-")[1]).collect(Collectors.toSet());
		subjectIds.addAll(periodUpMap.keySet().stream().map(e->e.split("-")[1]).collect(Collectors.toList()));
		subjectIds.addAll(noTimeUpMap.keySet().stream().map(e->e.split("-")[1]).collect(Collectors.toList()));
		subjectIds.addAll(resetWeekTypes.stream().map(e->e.split("-")[0]).collect(Collectors.toList()));

		List<NewGkClassSubjectTime> allCstList = this.findByArrayItemIdAndClassIdIn(unitId, arrayItemId, null, subjectIds.toArray(new String[0]), subjectTypes.toArray(new String[0]));
		Set<String> objectIds = EntityUtils.getSet(allCstList, NewGkClassSubjectTime::getId);
		List<NewGkClassSubjectTime> delCstList = allCstList.stream().filter(e->delSubjectCodes.contains(e.getSubjectId()+"-"+e.getSubjectType())).collect(Collectors.toList());
		
		
		List<NewGkLessonTime> lessonTimeList = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, 
				objectIds.toArray(new String[0]), new String[] {NewGkElectiveConstant.LIMIT_SUBJECT_5}, false);
		
		allCstList.removeAll(delCstList);
		Map<String, NewGkLessonTime> cstIdLtMap = EntityUtils.getMap(lessonTimeList, NewGkLessonTime::getObjectId);
		NewGkLessonTimeEx lte;
		Date now = new Date();
		List<NewGkLessonTime> saveLtList = new ArrayList<>();
		List<NewGkLessonTimeEx> saveLtExList = new ArrayList<>();
		List<String> delTimeList = new ArrayList<>();
		Iterator<NewGkClassSubjectTime> iterator = allCstList.iterator();
		while (iterator.hasNext()) {
			NewGkClassSubjectTime cst = iterator.next();
			String code = cst.getSubjectId()+"-"+cst.getSubjectType();
			Integer period = periodUpMap.get(code);
			if(period != null || resetWeekTypes.contains(code)) {
				if(period != null){
				    cst.setPeriod(period);
                }
                if(resetWeekTypes.contains(code)){
                    cst.setWeekType(NewGkElectiveConstant.WEEK_TYPE_NORMAL);
                }
			}else {
				iterator.remove();
			}
			Set<String> noTime = noTimeUpMap.get(code);
			if(noTime != null) {
				NewGkLessonTime lt = cstIdLtMap.get(cst.getId());
				if(lt == null) {
					lt = new NewGkLessonTime();
					lt.setId(UuidUtils.generateUuid());
					lt.setArrayItemId(arrayItemId);
					lt.setCreationTime(now);
					lt.setModifyTime(now);
					lt.setObjectType(NewGkElectiveConstant.LIMIT_SUBJECT_5);
					lt.setObjectId(cst.getId());
					lt.setIsJoin(NewGkElectiveConstant.IF_INT_1);
					lt.setGroupType(NewGkElectiveConstant.GROUP_TYPE_1);
					saveLtList.add(lt);
				}else {
					delTimeList.add(lt.getId());
				}
				for (String time : noTime) {
					String[] split = time.split("-");
					Integer day = Integer.parseInt(split[0]);
					String periodInterval = split[1];
					Integer period2 = Integer.parseInt(split[2]);
					
					lte = new NewGkLessonTimeEx();
					lte.setId(UuidUtils.generateUuid());
					lte.setArrayItemId(arrayItemId);
					lte.setScourceTypeId(lt.getId());
					lte.setScourceType(NewGkElectiveConstant.SCOURCE_LESSON_01);
					lte.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
					lte.setCreationTime(now);
					lte.setModifyTime(now);
					lte.setDayOfWeek(day);
					lte.setPeriodInterval(periodInterval);
					lte.setPeriod(period2);
					saveLtExList.add(lte);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(delCstList)) {
			// 删除 班级科目
			this.deleteAll(delCstList.toArray(new NewGkClassSubjectTime[0]));
		}
		if(CollectionUtils.isNotEmpty(allCstList)) {
			// 删除 班级科目
			this.saveAll(allCstList.toArray(new NewGkClassSubjectTime[0]));
		}
		// 删除禁排时间
		Set<String> delCsIds = EntityUtils.getSet(delCstList, NewGkClassSubjectTime::getId);
		Set<String> delLtList = lessonTimeList.stream().filter(e->delCsIds.contains(e.getObjectId())).map(e->e.getId()).collect(Collectors.toSet());
		delTimeList.addAll(delLtList);
		newGkLessonTimeService.deleteByIds(delLtList.toArray(new String[0]));
		newGkLessonTimeService.saveAutoBatchResult(delTimeList, saveLtList, saveLtExList, null);
		
		
		// 删除 对应的合班设置
		if(CollectionUtils.isNotEmpty(delRelaCodeList)) {
			List<NewGkClassCombineRelation> relaList = newGkClassCombineRelationService.findByArrayItemId(unitId, arrayItemId);
			relaList = relaList.stream().filter(rela->
						delRelaCodeList.stream().anyMatch(e->rela.getClassSubjectIds().contains(e))
							)
					.collect(Collectors.toList());
			newGkClassCombineRelationService.deleteAll(relaList.toArray(new NewGkClassCombineRelation[0]));
		}
	}

	@Override
	public void deleteByArrayItemId(String arrayItemId) {
		if(StringUtils.isNotEmpty(arrayItemId))
			newGkClassSubjectTimeDao.deleteByArrayItemId(arrayItemId);
	}

	@Override
	public List<NewGkClassFeatureDto> makeExistsClassSubjectInfo(String unitId, String arrayItemId, String classId) {
		List<NewGkClassSubjectTime> classTimeList = this.findByArrayItemIdAndClassIdIn(unitId, 
				arrayItemId,new String[] {classId},null, null);
		List<NewGkClassFeatureDto> courseTimeList2 = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(classTimeList)) {
			String[] objectIds = EntityUtils.getSet(classTimeList, NewGkClassSubjectTime::getId).toArray(new String[0]);
			List<NewGkLessonTime> lessonTimeList = newGkLessonTimeService.findByItemIdObjectId(arrayItemId, objectIds,
					new String[] {NewGkElectiveConstant.LIMIT_SUBJECT_5}, true);
			Map<String, NewGkLessonTime> ltMap = EntityUtils.getMap(lessonTimeList, NewGkLessonTime::getObjectId);
			NewGkClassFeatureDto dto = null;
			for (NewGkClassSubjectTime cst : classTimeList) {
				dto = new NewGkClassFeatureDto();
				
				dto.setSubjectId(cst.getSubjectId());
				dto.setSubjectType(cst.getSubjectType());
				dto.setCourseWorkDay(cst.getPeriod());
				dto.setWeekType(cst.getWeekType());

				// 禁排时间
				NewGkLessonTime lt = ltMap.get(cst.getId());
				if(lt != null && CollectionUtils.isNotEmpty(lt.getTimesList())) {
					StringBuilder sb = new StringBuilder();
					for (NewGkLessonTimeEx lte : lt.getTimesList()) {
						sb.append(",");
						sb.append(lte.getDayOfWeek()).append("_")
						.append(lte.getPeriodInterval()).append("_")
						.append(lte.getPeriod());
					}
					dto.setNoArrangeTime(sb.substring(1).toString());
				}
				courseTimeList2.add(dto);
			}
		}
		
		return courseTimeList2;
	
	}

	@Override
	public List<NewGkSubjectTime> findClassSubjectList(String arrayItemId, String classId, String unitId) {

		List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findByArrayItemId(arrayItemId);
		
		List<NewGkSubjectTime> courseTimeList = subjectTimeList.stream().filter(e->NewGkElectiveConstant.SUBJECT_TYPE_O.equals(e.getSubjectType()))
				.collect(Collectors.toList());
		
		NewGkDivideClass theClass = newGkDivideClassService.findOne(classId);
		if(StringUtils.isNotBlank(theClass.getRelateId())) {
			NewGkDivideClass relatedClass = newGkDivideClassService.findOne(theClass.getRelateId());
			String subjectIds = Optional.ofNullable(relatedClass.getSubjectIds()).orElse("");
			String[] subjectIdsT = subjectIds.split(",");
			List<NewGkDivideClass> jxbList = newGkDivideClassService.findByDivideIdAndClassType(unitId, relatedClass.getDivideId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, 
					false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			Set<String> hbSubjectIds = jxbList.stream().filter(e->relatedClass.getId().equals(e.getRelateId()))
					.map(e->e.getSubjectIds()).collect(Collectors.toSet());
			if(!NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(relatedClass.getSubjectType()) 
					&& StringUtils.isNotBlank(relatedClass.getSubjectIdsB())) {
				List<String> subjectIdsB = Arrays.asList(relatedClass.getSubjectIdsB().split(","));
				List<NewGkSubjectTime> bDto = subjectTimeList.stream()
						.filter(s->(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(s.getSubjectType()) 
								&& subjectIdsB.contains(s.getSubjectId())))
						.filter(e->!hbSubjectIds.contains(e.getSubjectId()))
						.collect(Collectors.toList());
				courseTimeList.addAll(bDto);
			}
			if(!BaseConstants.ZERO_GUID.equals(subjectIdsT[0])) {
				List<String> asList = Arrays.asList(subjectIdsT);
				// 不是混合班
				if(subjectIdsT.length >2) { // 纯组合班
					List<NewGkSubjectTime> aDto = subjectTimeList.stream().filter(s->(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(s.getSubjectType()) 
								&& asList.contains(s.getSubjectId())))
								.filter(e->!hbSubjectIds.contains(e.getSubjectId()))
								.collect(Collectors.toList());
					List<NewGkSubjectTime> bDto = subjectTimeList.stream().filter(s->(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(s.getSubjectType()) 
							&& !asList.contains(s.getSubjectId())))
							.filter(e->!hbSubjectIds.contains(e.getSubjectId()))
							.collect(Collectors.toList());
					courseTimeList.addAll(aDto);
					courseTimeList.addAll(bDto);
				}else if(subjectIdsT.length <= 2) {
					List<NewGkSubjectTime> aDto = subjectTimeList.stream().filter(s->NewGkElectiveConstant.SUBJECT_TYPE_A.equals(s.getSubjectType()) 
									&& asList.contains(s.getSubjectId()))
						.filter(e->!hbSubjectIds.contains(e.getSubjectId()))
						.collect(Collectors.toList());
					courseTimeList.addAll(aDto);
				}else {
					throw new RuntimeException("出现未知异常，请联系管理员");
				}
				
				// 存在 拆教学班时 
				List<NewGkChoRelation> relaList = newGkChoRelationService.findByChoiceIdsAndObjectType(unitId, 
						new String[] {theClass.getDivideId()}, NewGkElectiveConstant.CHOICE_TYPE_09);
				if(CollectionUtils.isNotEmpty(relaList)) {
					Map<String, NewGkSubjectTime> stMap = EntityUtils.getMap(subjectTimeList, e->e.getSubjectId()+e.getSubjectType());
					Map<String,List<String>> parentCids = EntityUtils.getListMap(relaList, NewGkChoRelation::getObjectTypeVal,NewGkChoRelation::getObjectValue);
					List<NewGkSubjectTime> pst = courseTimeList.stream()
							.filter(e -> !NewGkElectiveConstant.SUBJECT_TYPE_O.equals(e.getSubjectType())
									&& parentCids.containsKey(e.getSubjectId())).collect(Collectors.toList());
					
					List<NewGkSubjectTime> cst = pst.stream()
							.flatMap(e->parentCids.get(e.getSubjectId()).stream().map(c->c+e.getSubjectType()))
							.filter(e->stMap.containsKey(e))
							.map(e->stMap.get(e))
							.collect(Collectors.toList());
					courseTimeList.removeAll(pst);
					courseTimeList.addAll(cst);
					
				}
			}
		}else if(StringUtils.isNotBlank(theClass.getSubjectIds())){
			// 判断是否 是 行政班 排课
			NewGkDivide divide = newGkDivideService.findOne(theClass.getDivideId()); 
			if(NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType())) {
				List<String> subjectId = Arrays.asList(theClass.getSubjectIds().split(","));
				List<NewGkSubjectTime> xzbStList = subjectTimeList.stream()
					.filter(e->NewGkElectiveConstant.SUBJECT_TYPE_A.equals(e.getSubjectType())&&subjectId.contains(e.getSubjectId()))
					.collect(Collectors.toList());
				courseTimeList.addAll(xzbStList);
			}
		}
		return courseTimeList.stream().distinct().collect(Collectors.toList());
	
	}

	@Override
	public List<NewGkClassFeatureDto> findSubjectInfo(String unitId, String arrayItemId,
			List<String[]> classSubjectTypeIds) {
		Set<String> classIds = new HashSet<>();
		Set<String> subjectIds = new HashSet<>();
		Set<String> subjectTypes = new HashSet<>();
		for (String[] strings : classSubjectTypeIds) {
			classIds.add(strings[0]);
			subjectIds.add(strings[1]);
			subjectTypes.add(strings[2]);
		}
		
		List<NewGkClassSubjectTime> classTimeList = this.findByArrayItemIdAndClassIdIn(unitId, 
				arrayItemId,classIds.toArray(new String[0]),subjectIds.toArray(new String[0]), subjectTypes.toArray(new String[0]));
		// 获取禁排时间
		Set<String> objectIds = EntityUtils.getSet(classTimeList, NewGkClassSubjectTime::getId);
		objectIds.addAll(subjectIds);
		List<NewGkLessonTime> lessonTimeList = newGkLessonTimeService.findByItemIdObjectId(arrayItemId,
				objectIds.toArray(new String[0]), new String[] { NewGkElectiveConstant.LIMIT_SUBJECT_5,NewGkElectiveConstant.LIMIT_SUBJECT_9 }, true);
		
		// 获取课程特征
		List<NewGkLessonTime> classNoTimeList = lessonTimeList.stream().filter(e->NewGkElectiveConstant.LIMIT_SUBJECT_5.equals(e.getObjectType())).collect(Collectors.toList());
		List<NewGkLessonTime> subjectNoTimeList = lessonTimeList.stream().filter(e->NewGkElectiveConstant.LIMIT_SUBJECT_9.equals(e.getObjectType())).collect(Collectors.toList());
		Map<String, NewGkClassSubjectTime> classSubjectCodeMap = EntityUtils.getMap(classTimeList, e->e.getClassId()+"-"+e.getSubjectId()+"-"+e.getSubjectType());
		Map<String, NewGkLessonTime> ltMap = EntityUtils.getMap(classNoTimeList, NewGkLessonTime::getObjectId);
		Map<String, NewGkLessonTime> lt2Map = EntityUtils.getMap(subjectNoTimeList, e->e.getObjectId()+"-"+e.getLevelType());
		
		List<NewGkSubjectTime> subjectTimeList = newGkSubjectTimeService.findByArrayItemIdAndSubjectId(arrayItemId, subjectIds.toArray(new String[0]), subjectTypes.toArray(new String[0]));
		Map<String, NewGkSubjectTime> subjectTimeMap = EntityUtils.getMap(subjectTimeList, e->e.getSubjectId()+"-"+e.getSubjectType());
		List<NewGkClassFeatureDto> courseTimeList2 = new ArrayList<>();
		NewGkClassFeatureDto dto = null;
		for (String[] strings : classSubjectTypeIds) {
			dto = new NewGkClassFeatureDto();
			dto.setClassId(strings[0]);
			dto.setSubjectId(strings[1]);
			dto.setSubjectType(strings[2]);
			
			String code = strings[0]+"-"+strings[1]+"-"+strings[2];
			String code2 = strings[1]+"-"+strings[2];
			NewGkClassSubjectTime cst = classSubjectCodeMap.get(code);
			NewGkLessonTime lt = null;
			if(cst != null) {
				dto.setCourseWorkDay(cst.getPeriod());
				lt = ltMap.get(cst.getId());
			}else if(subjectTimeMap.containsKey(code2)){
				NewGkSubjectTime st = subjectTimeMap.get(code2);
				dto.setCourseWorkDay(st.getPeriod());
				lt = lt2Map.get(code2);
			}else {
				throw new RuntimeException("找不到对应的科目："+code2);
			}
			
			if(lt != null && CollectionUtils.isNotEmpty(lt.getTimesList())) {
				StringBuilder sb = new StringBuilder();
				for (NewGkLessonTimeEx lte : lt.getTimesList()) {
					if (!NewGkElectiveConstant.ARRANGE_TIME_TYPE_01.equals(lte.getTimeType()))
						continue;
					sb.append(",");
					sb.append(lte.getDayOfWeek()).append("_").append(lte.getPeriodInterval()).append("_")
							.append(lte.getPeriod());
				}
				dto.setNoArrangeTime(sb.substring(1).toString());
			}
			courseTimeList2.add(dto);
		}
		
		return courseTimeList2;
	}

	// Basedata Sync Method
    @Override
    public void deleteBySubjectIds(String... subjectIds) {
        newGkClassSubjectTimeDao.deleteBySubjectIdIn(subjectIds);
    }

    // Basedata Sync Method
    @Override
    public void deleteByClassIds(String... classIds) {
        newGkClassSubjectTimeDao.deleteByClassIdIn(classIds);
    }

    @Override
    public void deleteClassFeatureBySubjectTime(String unitId, List<NewGkSubjectTime> stList) {
	    if(CollectionUtils.isNotEmpty(stList)){
            String arrayItemId = stList.get(0).getArrayItemId();
            Set<String> asSet = new HashSet<>();
            Set<String> resetSet = new HashSet<>();
            for (NewGkSubjectTime st : stList) {
                asSet.add(st.getSubjectId()+"-"+st.getSubjectType());
                if(StringUtils.isNotBlank(st.getFirstsdWeekSubject())){
                    resetSet.add(st.getSubjectId()+"-"+st.getSubjectType());
                    resetSet.add(st.getFirstsdWeekSubject());
                }
            }
            this.deleteBySubjectCode(unitId, arrayItemId, asSet, new ArrayList<>(asSet),null,null, resetSet);
        }
    }
}
