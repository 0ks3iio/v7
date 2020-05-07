package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkTeacherPlanDao;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherGroupExService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanExService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;

@Service("newGkTeacherPlanService")
public class NewGkTeacherPlanServiceImpl extends
		BaseServiceImpl<NewGkTeacherPlan, String> implements
		NewGkTeacherPlanService {

	@Autowired
	private NewGkTeacherPlanDao newGkTeacherPlanDao;
	@Autowired
	private NewGkTeacherPlanExService newGkTeacherPlanExService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private NewGkTimetableService newGkTimetableService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkTeacherGroupExService teacherGroupExService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkTeacherPlan, String> getJpaDao() {
		return newGkTeacherPlanDao;
	}

	@Override
	protected Class<NewGkTeacherPlan> getEntityClass() {
		return NewGkTeacherPlan.class;
	}

	@Override
	public List<NewGkTeacherPlan> findByArrayItemIds(String[] teacherArrangeIds,boolean isMakeEx) {
		List<NewGkTeacherPlan> list = newGkTeacherPlanDao.findByArrayItemIdIn(teacherArrangeIds);
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<NewGkTeacherPlan>();
		}
		if(isMakeEx){
			Set<String> ids = EntityUtils.getSet(list, e->e.getId());
			List<NewGkTeacherPlanEx> exlist = newGkTeacherPlanExService.findListByIn("teacherPlanId", ids.toArray(new String[]{}));
			//组装map
			Map<String, List<String>> map = new HashMap<>();
			Map<String , List<NewGkTeacherPlanEx>> teacherPlanExMap = new HashMap<>();
			if(CollectionUtils.isNotEmpty(exlist)){
				for(NewGkTeacherPlanEx planEx : exlist){
					List<String> list1 = map.get(planEx.getTeacherPlanId());
					List<NewGkTeacherPlanEx> exList = teacherPlanExMap.get(planEx.getTeacherPlanId());
					if(list1 == null){
						list1 = new ArrayList<>();
						map.put(planEx.getTeacherPlanId(),list1);
					}
					if(exList == null){
						exList = new ArrayList<>();
						teacherPlanExMap.put(planEx.getTeacherPlanId() ,exList);
					}
					list1.add(planEx.getTeacherId());
					exList.add(planEx);
				}
				
			}
			
			for(NewGkTeacherPlan plan:list){
				if(map.containsKey(plan.getId())){
					List<String> list1 = map.get(plan.getId());
					if(CollectionUtils.isNotEmpty(list1)){
						plan.setExTeacherIdList(list1);
						plan.setTeacherCounts(String.valueOf(list1.size()));
					}else{
						plan.setTeacherCounts("0");
					}
					
				}
				if(teacherPlanExMap.containsKey(plan.getId())){
					List<NewGkTeacherPlanEx> exList = teacherPlanExMap.get(plan.getId());
					plan.setTeacherPlanExList(exList);
				}
			}
		}
		return list;
	}

	@Override
	public List<NewGkTeacherPlan> findByArrayItemIdsWithMaster(String[] teacherArrangeIds,boolean isMakeEx) {
		return findByArrayItemIds(teacherArrangeIds, isMakeEx);
	}
	
	@Override
	public String saveAddUpList(String arrayItemId,List<NewGkTeacherPlan> ents ,String divideId, String arrayId,String unitId) {
		
		List<String>ids = new ArrayList<String>();
		Set<String> subjectIdsSet = new HashSet<String>();
		Map<String , NewGkTeacherPlan> teacherPlanMap = new HashMap<>();
		// 需要删除的 planexId 值
		Set<String> planExIdsDelete = new HashSet<>();
		Set<String> teacherIdsDelete = new HashSet<>();
		if(StringUtils.isBlank(arrayItemId)){
			NewGkDivide newGkDivide = newGkDivideService.findOneBy("id", divideId);
			NewGkArrayItem item = new NewGkArrayItem();
			arrayItemId = UuidUtils.generateUuid();
			item.setId(arrayItemId);
			item.setDivideId(divideId);
			item.setDivideType(NewGkElectiveConstant.ARRANGE_TYPE_02);
			List<NewGkArrayItem> itemList = newGkArrayItemService.findByDivideId(divideId, new String[]{NewGkElectiveConstant.ARRANGE_TYPE_02});
			List<Integer> timesList = EntityUtils.getList(itemList, e->e.getTimes());
			Integer times=1;
			if(CollectionUtils.isNotEmpty(timesList)){
				times = Collections.max(timesList)+1;
			}
			item.setTimes(times);
			Grade grade = SUtils.dc(gradeRemoteService.findOneById(newGkDivide.getGradeId()),Grade.class);
			//获取学年学期等信息
			String semesterJson = semesterRemoteService.getCurrentSemester(2, newGkDivide.getUnitId());
			Semester semester = SUtils.dc(semesterJson, Semester.class);
			String itemName = semester.getAcadyear()+"学年"+grade.getGradeName()+"第"+semester.getSemester()+"学期教师资源安排"+times;

			item.setItemName(itemName);
			item.setIsDeleted(0);
			item.setCreationTime(new Date());
			item.setModifyTime(new Date());
			newGkArrayItemService.save(item);
		}else{
			List<NewGkTeacherPlan> planList = findByArrayItemIds(new String[]{arrayItemId} , true);
			teacherPlanMap = EntityUtils.getMap(planList,e->e.getSubjectId());
			if(CollectionUtils.isNotEmpty(ents)){
				subjectIdsSet = EntityUtils.getSet(ents,e->e.getSubjectId());
				for(NewGkTeacherPlan plan:planList){
					if(!subjectIdsSet.contains(plan.getSubjectId())){// 需要删除的信息
						teacherPlanMap.remove(plan.getSubjectId());
						ids.add(plan.getId());
						List<NewGkTeacherPlanEx> planExList = plan.getTeacherPlanExList();
						if(CollectionUtils.isNotEmpty(planExList)) {
							planExIdsDelete.addAll(EntityUtils.getSet(planExList, e->e.getId()));
							teacherIdsDelete.addAll(EntityUtils.getSet(planExList, e->e.getTeacherId()));
						}
					}
				}
			}
		}

		List<NewGkTeacherPlan> planListSave = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(ents)){
			List<NewGkTeacherPlanEx> exlist=new ArrayList<NewGkTeacherPlanEx>();
			Iterator<NewGkTeacherPlan> iterator = ents.iterator();
			while(iterator.hasNext()){
				NewGkTeacherPlan ent = iterator.next();
				if(StringUtils.isBlank(ent.getSubjectId())){
					iterator.remove();
					continue;
				}
				// 数据库已有数据处理
				if(teacherPlanMap.containsKey(ent.getSubjectId())){
					NewGkTeacherPlan plan = teacherPlanMap.get(ent.getSubjectId());
//					planListSave.add(plan);
					List<String> teachids = ent.getExTeacherIdList();
					List<NewGkTeacherPlanEx> planExList = plan.getTeacherPlanExList();
					Map<String , NewGkTeacherPlanEx> planExMap = EntityUtils.getMap(planExList,e->e.getTeacherId());
					if(CollectionUtils.isNotEmpty(teachids)){
						for(NewGkTeacherPlanEx planEx :planExList){
							if(!teachids.contains(planEx.getTeacherId())){
								planExIdsDelete.add(planEx.getId());
								teacherIdsDelete.add(planEx.getTeacherId());
							}
						}
						for(String tea: teachids){
							if(StringUtils.isBlank(tea)){
								continue;
							}
							NewGkTeacherPlanEx ex = planExMap.get(tea);
							if(ex == null){
								ex=new NewGkTeacherPlanEx();
								ex.setId(UuidUtils.generateUuid());
								ex.setMutexNum(0);
								ex.setTeacherId(tea);
								ex.setTeacherPlanId(plan.getId());
								ex.setCreationTime(new Date());
								ex.setModifyTime(new Date());
								exlist.add(ex);
							}
						}
					}
				}else{// 新增数据
					ent.setId(UuidUtils.generateUuid());
					ent.setArrayItemId(arrayItemId);
					ent.setCreationTime(new Date());
					planListSave.add(ent);
					List<String> teachids = ent.getExTeacherIdList();
					if(CollectionUtils.isNotEmpty(teachids)){
						for(String teachid:teachids){
							if(StringUtils.isBlank(teachid)){
								continue;
							}
							NewGkTeacherPlanEx ex=new NewGkTeacherPlanEx();
							ex.setId(UuidUtils.generateUuid());
							ex.setMutexNum(0);
							ex.setTeacherId(teachid);
							ex.setTeacherPlanId(ent.getId());
							ex.setCreationTime(new Date());
							ex.setModifyTime(new Date());
							exlist.add(ex);
						}
					}
				}

			}

			if(CollectionUtils.isNotEmpty(planExIdsDelete)){
				newGkTeacherPlanExService.deleteByIdIn(planExIdsDelete.toArray(new String[0]));
				newGkChoRelationService.deleteByTypeChoiceIds(unitId,NewGkElectiveConstant.CHOICE_TYPE_07, planExIdsDelete.toArray(new String[0]));
				newGkLessonTimeService.deleteByArrayItemIdAndObjectType(arrayItemId,NewGkElectiveConstant.LIMIT_TEACHER_2,teacherIdsDelete.toArray(new String[0]));
				teacherGroupExService.deleteByTeacherIds(arrayItemId, new ArrayList<>(teacherIdsDelete));
			}
			if(CollectionUtils.isNotEmpty(ids)){
				newGkTeacherPlanDao.deleteByIdIn(ids.toArray(new String[0]));
			}
			newGkTeacherPlanDao.saveAll(this.checkSave(planListSave.toArray(new NewGkTeacherPlan[0])));
			if(CollectionUtils.isNotEmpty(exlist)){
				newGkTeacherPlanExService.saveAll(exlist.toArray(new NewGkTeacherPlanEx[0]));
			}
		}
		// 必须在保存教师特征之后执行
		if(StringUtils.isNotBlank(arrayId)) {
			newGkTimetableService.updateTimetableAllTeachers(arrayId, null);
		}
		return arrayItemId;
	}

	@Override
	public void deleteByIdIn(String[] ids) {
		newGkTeacherPlanDao.deleteByIdIn(ids);
	}

	@Override
	public List<NewGkTeacherPlan> findByArrayItemIdAndSubjectIdIn(String arrayItemId, String[] subjectIds, boolean isMakeEx) {
		List<NewGkTeacherPlan> list = newGkTeacherPlanDao.findByArrayItemIdAndSubjectIdIn(arrayItemId,subjectIds);
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<NewGkTeacherPlan>();
		}
		if(isMakeEx){
			Set<String> ids = EntityUtils.getSet(list, e->e.getId());
			List<NewGkTeacherPlanEx> exlist = newGkTeacherPlanExService.findListByIn("teacherPlanId", ids.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(exlist)){
				//组装map 
				Map<String, List<String>> map = new HashMap<>();
				Map<String , List<NewGkTeacherPlanEx>> teacherPlanExMap = new HashMap<>();
				for(NewGkTeacherPlanEx planEx : exlist){
					List<String> list1 = map.get(planEx.getTeacherPlanId());
					List<NewGkTeacherPlanEx> exList = teacherPlanExMap.get(planEx.getTeacherPlanId());
					if(list1 == null){
						list1 = new ArrayList<>();
						map.put(planEx.getTeacherPlanId(),list1);
					}
					if(exList == null){
						exList = new ArrayList<>();
						teacherPlanExMap.put(planEx.getTeacherPlanId() ,exList);
					}
					list1.add(planEx.getTeacherId());
					exList.add(planEx);
				}
				for(NewGkTeacherPlan plan:list){
					if(map.containsKey(plan.getId())){
						List<String> list1 = map.get(plan.getId());
						plan.setExTeacherIdList(list1);
						plan.setTeacherCounts(String.valueOf(exlist.size()));
					}
					if(teacherPlanExMap.containsKey(plan.getId())){
						List<NewGkTeacherPlanEx> exList = teacherPlanExMap.get(plan.getId());
						plan.setTeacherPlanExList(exList);
					}
				}
			}
		}
		return list;
	}

	@Override
	public void deleteByArrayItemId(String arrayItemId) {
		List<NewGkTeacherPlan> list = newGkTeacherPlanDao.findByArrayItemIdIn(new String[] {arrayItemId});
		if(CollectionUtils.isNotEmpty(list)){
			Set<String> ids = EntityUtils.getSet(list, NewGkTeacherPlan::getId);
			newGkTeacherPlanExService.deleteByIdIn(ids.toArray(new String[0]));
			newGkTeacherPlanDao.deleteByArrayItemId(arrayItemId);
		}
	}

	@Override
	public void saveList(List<NewGkTeacherPlan> teacherPlanList, List<NewGkTeacherPlanEx> planExList) {
		if(CollectionUtils.isNotEmpty(teacherPlanList)){
			saveAll(teacherPlanList.toArray(new NewGkTeacherPlan[0]));
		}
		if(CollectionUtils.isNotEmpty(planExList)){
			newGkTeacherPlanExService.saveAll(planExList.toArray(new NewGkTeacherPlanEx[0]));
		}
	}

	@Override
	public void saveImportPlan(String arrayItemId,String[] subjectIds, Map<String, Map<String, String>> subjectTeacherClazz) {
		
		//key:subjectId teacherId
		Map<String,Set<String>> teacherBySubId=new HashMap<>(); 
		//key:subjectId_teacherId classId
		Map<String, Set<String>> map1=new HashMap<>();
		//key:subjectId value classId 教师为空的班级
		Map<String,Set<String>> noArrangeClassBySubjectId=new HashMap<>();
		//存在有老师的班级 需要移除它存在的原有位置
		Map<String,Set<String>> arrangeClassMap=new HashMap<>();
		for(Map.Entry<String, Map<String, String>> item :subjectTeacherClazz.entrySet()) {
			String clazzId=item.getKey();
			for(Map.Entry<String, String> item2:item.getValue().entrySet()) {
				String subId=item2.getKey();
				String tId=item2.getValue();
				if(StringUtils.isBlank(tId)) {
					//去除安排的教师
					if(!noArrangeClassBySubjectId.containsKey(subId)) {
						noArrangeClassBySubjectId.put(subId, new HashSet<>());
					}
					noArrangeClassBySubjectId.get(subId).add(clazzId);
					continue;
				}
				if(!arrangeClassMap.containsKey(subId)) {
					arrangeClassMap.put(subId, new HashSet<>());
				}
				arrangeClassMap.get(subId).add(clazzId);
				String key=subId+"_"+tId;
				Set<String> set1 = map1.get(key);
				if(set1==null) {
					set1=new HashSet<>();
					map1.put(key, set1);
				}
				set1.add(clazzId);
				
				Set<String> set2 = teacherBySubId.get(subId);
				if(set2==null) {
					set2=new HashSet<>();
					teacherBySubId.put(subId, set2);
				}
				set2.add(tId);
			}
		}
		
		List<NewGkTeacherPlan> insertPlan=new ArrayList<>();
		List<NewGkTeacherPlanEx> insertOrUpdatePlan=new ArrayList<>();
		
		List<NewGkTeacherPlan> list = findByArrayItemIdAndSubjectIdIn(arrayItemId, subjectIds, true);
		
		Set<String> oldPlanSubId=new HashSet<>();
		for(NewGkTeacherPlan p:list) {
			String subId=p.getSubjectId();
			oldPlanSubId.add(subId);
			Set<String> noClassIds = noArrangeClassBySubjectId.get(subId);
			Set<String> tIdSet = teacherBySubId.get(subId);
			if(CollectionUtils.isEmpty(tIdSet)) {
				tIdSet=new HashSet<>();
			}
			Set<String> arrangeClassIds=arrangeClassMap.get(subId);
			if(CollectionUtils.isNotEmpty(p.getExTeacherIdList())) {
				boolean isUpdate=false;
				for(NewGkTeacherPlanEx ex:p.getTeacherPlanExList()) {
					isUpdate=false;
					String tId = ex.getTeacherId();
					String classIds = ex.getClassIds();
					Set<String> classSet=new HashSet<>();
					if(StringUtils.isNotBlank(classIds)) {
						List<String> list1=Arrays.asList(classIds.split(","));
						classSet.addAll(list1);
						if(CollectionUtils.isNotEmpty(arrangeClassIds)) {
							int oldSize=classSet.size();
							classSet.removeAll(arrangeClassIds);
							int newSize=classSet.size();
							if(oldSize!=newSize) {
								//有交集
								isUpdate=true;
							}
						}
						if(CollectionUtils.isNotEmpty(classSet) && CollectionUtils.isNotEmpty(noClassIds)) {
							int oldSize=classSet.size();
							//从有到无 修改科目
							//删除
							classSet.removeAll(noClassIds);
							int newSize=classSet.size();
							if(!isUpdate && newSize!=oldSize) {
								isUpdate=true;
							}
						}
					}
					
					//后来新增的
					Set<String> set11 = map1.get(subId+"_"+tId);
					if(CollectionUtils.isNotEmpty(set11)) {
						int oldSize=classSet.size();
						classSet.addAll(set11);
						int newSize=classSet.size();
						if(!isUpdate && newSize!=oldSize) {
							isUpdate=true;
						}
						tIdSet.remove(tId);
					}
					if(isUpdate) {
						//有修改 
						if(classSet.size()>0) {
							ex.setClassIds(ArrayUtil.print(classSet.toArray(new String[] {})));
						}else {
							ex.setClassIds("");
						}
						ex.setModifyTime(new Date());
						insertOrUpdatePlan.add(ex);
					}
				}
			}
			//需要新增数据
			if(CollectionUtils.isNotEmpty(tIdSet)) {
				for(String s:tIdSet) {
					if(CollectionUtils.isEmpty(map1.get(subId+"_"+s))) {
						continue;
					}
					NewGkTeacherPlanEx ee=new NewGkTeacherPlanEx();
					ee.setId(UuidUtils.generateUuid());
					ee.setMutexNum(0);
					ee.setTeacherId(s);
					ee.setTeacherPlanId(p.getId());
					ee.setCreationTime(new Date());
					ee.setModifyTime(new Date());
					ee.setClassIds(ArrayUtil.print(map1.get(subId+"_"+s).toArray(new String[] {})));
					insertOrUpdatePlan.add(ee);
				}
			}
		}
		Map<String,NewGkTeacherPlan> planMap=new HashMap<>();
		//没有的数据
		for(Entry<String, Set<String>> item1:map1.entrySet()) {
			String[] arr=item1.getKey().split("_");
			if(CollectionUtils.isEmpty(item1.getValue())){
				continue;
			}
			if(oldPlanSubId.contains(arr[0])) {
				continue;
			}
			NewGkTeacherPlan plan=planMap.get(arr[0]);
			if(plan==null) {
				plan=new NewGkTeacherPlan();
				plan.setArrayItemId(arrayItemId);
				plan.setCreationTime(new Date());
				plan.setModifyTime(new Date());
				plan.setId(UuidUtils.generateUuid());
				plan.setSubjectId(arr[0]);
				planMap.put(arr[0], plan);
				insertPlan.add(plan);
			}
			NewGkTeacherPlanEx ee=new NewGkTeacherPlanEx();
			ee.setId(UuidUtils.generateUuid());
			ee.setMutexNum(0);
			ee.setTeacherId(arr[1]);
			ee.setTeacherPlanId(plan.getId());
			ee.setCreationTime(new Date());
			ee.setModifyTime(new Date());
			ee.setClassIds(ArrayUtil.print(item1.getValue().toArray(new String[] {})));
			insertOrUpdatePlan.add(ee);
		}
		if(CollectionUtils.isNotEmpty(insertPlan)) {
			saveAll(insertPlan.toArray(new NewGkTeacherPlan[0]));
		}
		if(CollectionUtils.isNotEmpty(insertOrUpdatePlan)) {
			newGkTeacherPlanExService.saveAll(insertOrUpdatePlan.toArray(new NewGkTeacherPlanEx[0]));
		}
	}

    @Override
    public void deleteBySubjectIds(String... subIds) {
        newGkTeacherPlanDao.deleteBySubjectIdIn(subIds);
    }

	@Override
	public List<NewGkTeacherPlan> findByArrayItemIdAndSubjectIdInWithMaster(String arrayItemId, String[] subjectIds,
			boolean isMakeEx) {
		return findByArrayItemIdAndSubjectIdIn(arrayItemId, subjectIds, isMakeEx);
	}

}
