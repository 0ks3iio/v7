package net.zdsoft.newgkelective.data.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.entity.*;
import net.zdsoft.newgkelective.data.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkTeacherGroupDao;

@Service("newGkTeacherGroupService")
public class NewGkTeacherGroupServiceImpl extends BaseServiceImpl<NewGkTeacherGroup, String>
		implements NewGkTeacherGroupService {
	@Autowired
	private NewGkTeacherGroupDao newGkTeacherGroupDao;
	@Autowired
	private NewGkTeacherGroupExService teacherGroupExService;
	@Autowired
	private NewGkLessonTimeService lessonTimeService;
	@Autowired
	private NewGkLessonTimeExService lessonTimeExService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	private GradeRemoteService gradeRemoteService;

	@Override
	protected BaseJpaRepositoryDao<NewGkTeacherGroup, String> getJpaDao() {
		return newGkTeacherGroupDao;
	}

	@Override
	protected Class<NewGkTeacherGroup> getEntityClass() {
		return NewGkTeacherGroup.class;
	}
	
	@Override
	public List<NewGkTeacherGroup> findByObjectId(String objectId, boolean makeTeachers){
		if(StringUtils.isBlank(objectId)) {
			return new ArrayList<>();
		}
		
		List<NewGkTeacherGroup> teacherGroupList = newGkTeacherGroupDao.findByObjectId(objectId);
		
		if(CollectionUtils.isNotEmpty(teacherGroupList) && makeTeachers) {
			Map<String, NewGkTeacherGroup> tgIdMap = EntityUtils.getMap(teacherGroupList, NewGkTeacherGroup::getId);
			List<NewGkTeacherGroupEx> tgExList = teacherGroupExService.findByTeacherGroupIdIn(tgIdMap.keySet().toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(tgExList)) {
				for (NewGkTeacherGroupEx ex : tgExList) {
					if(tgIdMap.containsKey(ex.getTeacherGroupId())) {
						tgIdMap.get(ex.getTeacherGroupId()).getTeacherIdSet().add(ex.getTeacherId());
					}
				}
			}
		}
		
		return teacherGroupList;
	}

	@Override
	public void saveOrUpdate(List<NewGkTeacherGroup> teacherGroupList, List<NewGkTeacherGroupEx> tgExList) {
		
		if(CollectionUtils.isNotEmpty(teacherGroupList)) {
			this.saveAll(teacherGroupList.toArray(new NewGkTeacherGroup[0]));
			teacherGroupExService.deleteByTeacherGroupIds(EntityUtils.getSet(teacherGroupList, NewGkTeacherGroup::getId).toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(tgExList)) {
				teacherGroupExService.saveAll(tgExList.toArray(new NewGkTeacherGroupEx[0]));
			}
		}
	}

	@Override
	public void dealTeacherGroupInit(String gradeId){
		List<NewGkTeacherGroup> oldTgList = this.findByObjectId(gradeId, false);
		List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findByArrayItemIds(new String[] {gradeId}, true);


		List<NewGkTeacherGroupEx> initTgExListT = new ArrayList<>();
		List<NewGkTeacherGroup> initTgListT = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(teacherPlanList)) {
			// 教师组 是空的 话，默认 添加 所有 教师特征中 的 老师
			Date now = new Date();
			Grade grade = gradeRemoteService.findOneObjectById(gradeId);
			if(grade == null){
				throw new RuntimeException("无法获取年级信息");
			}
			String unitId = grade.getSchoolId();
			Map<String, String> courseNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(
					EntityUtils.getSet(teacherPlanList, NewGkTeacherPlan::getSubjectId).toArray(new String[0])),
					new TypeReference<Map<String,String>>() {});
			NewGkTeacherGroup tg;
			NewGkTeacherGroupEx tge;
			for (NewGkTeacherPlan tp : teacherPlanList) {
				List<String> tl = tp.getExTeacherIdList();
				if(CollectionUtils.isEmpty(tl)) {
					continue;
				}
				String sName = courseNameMap.get(tp.getSubjectId());
				if(sName == null) {
					sName = "未知科目";
				}
				tg = new NewGkTeacherGroup();
				tg.setId(UuidUtils.generateUuid());
				tg.setTeacherGroupName(sName+"组");
				tg.setCreationTime(now);
				tg.setModifyTime(now);
				tg.setObjectId(gradeId);
				tg.setUnitId(unitId);
				initTgListT.add(tg);

				for (String tid : tl) {
					tge = new NewGkTeacherGroupEx();
					tge.setId(UuidUtils.generateUuid());
					tge.setTeacherGroupId(tg.getId());
					tge.setTeacherId(tid);
					initTgExListT.add(tge);
				}
			}
		}

		if(CollectionUtils.isNotEmpty(oldTgList)){
			Set<String> tgIds = EntityUtils.getSet(oldTgList, e -> e.getId());
			this.deleteGroups(gradeId,tgIds.toArray(new String[0]));
		}
		this.saveOrUpdate(initTgListT, initTgExListT);
	}

	@Override
	public void deleteGroups(String gradeId, String[] teacherGroupIds) {
		if(teacherGroupIds == null || teacherGroupIds.length <1) {
			return;
		}
		
		List<NewGkTeacherGroupEx> exList = teacherGroupExService.findByTeacherGroupIdIn(teacherGroupIds);
//		Map<String, List<String>> tgTidMap = EntityUtils.getListMap(exList, NewGkTeacherGroupEx::getTeacherGroupId,NewGkTeacherGroupEx::getTeacherId);
		
		Set<String> objIds = EntityUtils.getSet(exList, NewGkTeacherGroupEx::getTeacherId);
		objIds.addAll(Arrays.asList(teacherGroupIds));
		List<NewGkLessonTime> ltList = lessonTimeService.findByItemIdObjectId(gradeId, objIds.toArray(new String[0]),
				new String[] {NewGkElectiveConstant.LIMIT_TEACHER_GROUP_6,NewGkElectiveConstant.LIMIT_TEACHER_2}, true);
		List<NewGkLessonTime> tgLtList = ltList.stream().filter(e->NewGkElectiveConstant.LIMIT_TEACHER_GROUP_6.equals(e.getObjectType())).collect(Collectors.toList());
		
		
//		List<NewGkLessonTime> teaLtList = ltList.stream().filter(e->NewGkElectiveConstant.LIMIT_TEACHER_2.equals(e.getObjectType())).collect(Collectors.toList());
//		deleteRelaTeacherTime(tgTidMap, teaLtList, tgLtList);
		if(CollectionUtils.isNotEmpty(tgLtList)) {
			Set<String> ltIds = EntityUtils.getSet(tgLtList, NewGkLessonTime::getId);
			lessonTimeService.deleteWithExByIds(ltIds.toArray(new String[0]));
		}
		newGkTeacherGroupDao.deleteByIdIn(teacherGroupIds);
		teacherGroupExService.deleteByTeacherGroupIds(teacherGroupIds);
	}

	@Override
	public void deleteRelaTeacherTime(Map<String, List<String>> tgTidMap, List<NewGkLessonTime> teaLtList,
			List<NewGkLessonTime> tgLtList) {
		if(tgTidMap == null || tgTidMap.size()<=0|| CollectionUtils.isEmpty(tgLtList)) {
			return;
		}
		
		Map<String,Set<String>> teacherTimeMap = new HashMap<>();
		List<NewGkLessonTimeEx> nullList = new ArrayList<>();
		for (NewGkLessonTime lt : tgLtList) {
			List<String> tids = tgTidMap.get(lt.getObjectId());
			List<NewGkLessonTimeEx> timesList = lt.getTimesList();
			if(CollectionUtils.isEmpty(tids) || CollectionUtils.isEmpty(timesList)) {
				continue;
			}
			Set<String> noTimes = EntityUtils.getSet((timesList==null?nullList:timesList), e->e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod());
			for (String tid : tids) {
				Set<String> times = teacherTimeMap.get(tid);
				if(times == null) {
					times = new HashSet<>();
					teacherTimeMap.put(tid, times);
				}
				times.addAll(noTimes);
			}
		}
		
		String gradeId = tgLtList.get(0).getArrayItemId();
		if(teaLtList == null) {
			Set<String> objIds = tgTidMap.values().stream().flatMap(e->e.stream()).collect(Collectors.toSet());
			teaLtList = lessonTimeService.findByItemIdObjectId(gradeId, objIds.toArray(new String[0]),
					new String[] {NewGkElectiveConstant.LIMIT_TEACHER_2}, true);
		}
		
		List<NewGkLessonTimeEx> delExList = new ArrayList<>();
		for (NewGkLessonTime lt : teaLtList) {
			List<NewGkLessonTimeEx> timesList = lt.getTimesList();
			Set<String> noTimes = teacherTimeMap.get(lt.getObjectId());
			if(CollectionUtils.isEmpty(noTimes) || CollectionUtils.isEmpty(timesList)) {
				continue;
			}
			List<NewGkLessonTimeEx> collect = timesList.stream().filter(e->noTimes.contains(e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod())).collect(Collectors.toList());
			delExList.addAll(collect);
		}
		
		
		if(CollectionUtils.isNotEmpty(delExList)) {
			lessonTimeExService.deleteAll(delExList.toArray(new NewGkLessonTimeEx[0]));
		}
	}
}
