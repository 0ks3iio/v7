package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkTeacherPlanExDao;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanExService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;

@Service("newGkTeacherPlanExService")
public class NewGkTeacherPlanExServiceImpl extends BaseServiceImpl<NewGkTeacherPlanEx, String> implements NewGkTeacherPlanExService{

	@Autowired 
	private NewGkTeacherPlanExDao newGkTeacherPlanExDao;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkTimetableService newGkTimetableService;
	@Autowired
	private NewGkDivideClassService divideClassService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkTeacherPlanEx, String> getJpaDao() {
		return newGkTeacherPlanExDao;
	}

	@Override
	protected Class<NewGkTeacherPlanEx> getEntityClass() {
		return NewGkTeacherPlanEx.class;
	}

	@Override
	public void deleteByTeacherPlanIdIn(String[] teacherPlanIds) {
		newGkTeacherPlanExDao.deleteByTeacherPlanIdIn(teacherPlanIds);
	}

	@Override
	public void deleteByIdIn(String[] ids) {
		newGkTeacherPlanExDao.deleteByIdIn(ids);
	}
	
	public void saveExs(String unitId, String arrayId, String subjectId, String itemId, List<NewGkTeacherPlanEx> planExList) {
		
		Set<String> exids = EntityUtils.getSet(planExList, NewGkTeacherPlanEx::getTeacherId);
		List<NewGkLessonTime> times = newGkLessonTimeService.findByItemIdObjectId(itemId,
				exids.toArray(new String[0]), new String[] { NewGkElectiveConstant.LIMIT_TEACHER_2 },
				false);
		List<NewGkChoRelation> res = new ArrayList<NewGkChoRelation>();
		exids = EntityUtils.getSet(times, NewGkLessonTime::getId);
		Map<String, String> ttimeId = EntityUtils.getMap(times, "objectId", "id");
		if (exids.size() > 0) {
			newGkChoRelationService.deleteByTypeChoiceIds(unitId,
					NewGkElectiveConstant.CHOICE_TYPE_07, exids.toArray(new String[0]));
		}
		
		Set<String> allCids = planExList.stream().filter(e->StringUtils.isNotBlank(e.getClassIds()))
				.flatMap(e->Arrays.stream(e.getClassIds().split(",")))
				.collect(Collectors.toSet());
		List<NewGkDivideClass> divideClassList = divideClassService.findByIdIn(allCids.toArray(new String[0]));
		Map<String,NewGkDivideClass> divideClassIdMap = EntityUtils.getMap(divideClassList, NewGkDivideClass::getId);
		List<NewGkLessonTime> tos = new ArrayList<NewGkLessonTime>(); 
		for(NewGkTeacherPlanEx ex : planExList) {
			if(StringUtils.isNotEmpty(ex.getMutexTeaIds())) {
				String choiceId = null;
				if(ttimeId.containsKey(ex.getTeacherId())) {
					choiceId = ttimeId.get(ex.getTeacherId());
				} else {
					NewGkLessonTime time = new NewGkLessonTime();
					time.setArrayItemId(itemId);
					time.setCreationTime(new Date());
					time.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
					time.setId(UuidUtils.generateUuid());
					time.setIsJoin(1);
					time.setModifyTime(new Date());
					time.setObjectId(ex.getTeacherId());
					time.setObjectType(NewGkElectiveConstant.LIMIT_TEACHER_2);
					tos.add(time);
					choiceId = time.getId();
				}
				res.addAll(getRelationByExs(unitId,choiceId, ex));
			}
			if(StringUtils.isNotBlank(ex.getClassIds())) {
				String[] cids = ex.getClassIds().split(",");
				Arrays.sort(cids, (xid,yid)->{
					NewGkDivideClass x = divideClassIdMap.get(xid);
					NewGkDivideClass y = divideClassIdMap.get(yid);
					if(x == null || y == null)
						return 0;
					if(x.getOrderId()!=null && y.getOrderId()!=null) {
						return x.getOrderId()-y.getOrderId();
					}else if(x.getOrderId() == null) {
						return 1;
					}else if(y.getOrderId() == null) {
						return -1;
					}
					return 0;
				});
				ex.setClassIds(String.join(",", cids));
			}
		}
		if(tos.size() > 0) {
			newGkLessonTimeService.saveAll(tos.toArray(new NewGkLessonTime[0]));
		}
		if(res.size() > 0) {
			List<String> list = new ArrayList<String>();
			Iterator<NewGkChoRelation> iterator = res.iterator();
			while(iterator.hasNext()){
				NewGkChoRelation next = iterator.next();
				if(list.contains(next.getChoiceId()+next.getObjectValue())){
					iterator.remove();
				}else{
					list.add(next.getChoiceId()+next.getObjectValue());
				}
			}
			newGkChoRelationService.saveAll(res.toArray(new NewGkChoRelation[0]));
		}
		this.saveAll(planExList.toArray(new NewGkTeacherPlanEx[0]));
		
		// 一定在保存教师特征之后更新预排课表
		if(StringUtils.isNotBlank(arrayId) && StringUtils.isNotBlank(subjectId)) {
//			newGkTimetableService.updateTimetableTeachers(arrayId, subjectId , planExList);
			newGkTimetableService.updateTimetableAllTeachers(arrayId, Arrays.asList(subjectId));
		}
	}

    @Override
    public void deleteByTeacherIds(String... teacherId) {
        newGkTeacherPlanExDao.deleteByTeacherIdIn(teacherId);
    }

    @Override
    public void deleteByClassIds(String... classId) {
	    List<NewGkTeacherPlanEx> newGkTeacherPlanExList = newGkTeacherPlanExDao.findByClassIdsLike("%" + classId + "%");
        if (CollectionUtils.isNotEmpty(newGkTeacherPlanExList)) {
            for (NewGkTeacherPlanEx one : newGkTeacherPlanExList) {
                String[] classIds = StringUtils.split(one.getClassIds(), ',');
                StringBuffer classIdsTmp = new StringBuffer();
                for (String tmp : classIds) {
                    if (!classId.equals(tmp)) {
                        classIdsTmp.append("," + tmp);
                    }
                }
                one.setClassIds(classIdsTmp.length() > 0 ? classIdsTmp.substring(1) : null);
            }
            newGkTeacherPlanExDao.saveAll(newGkTeacherPlanExList);
        }
    }

    private List<NewGkChoRelation> getRelationByExs(String unitId,String choiceId, NewGkTeacherPlanEx ex) {
		List<NewGkChoRelation> res = new ArrayList<NewGkChoRelation>();
		String[] mtids = ex.getMutexTeaIds().split(",");
		NewGkChoRelation re;
		for(String tid : mtids) {
			re = new NewGkChoRelation();
			re.setChoiceId(choiceId);
			re.setCreationTime(new Date());
			re.setId(UuidUtils.generateUuid());
			re.setUnitId(unitId);
			re.setModifyTime(new Date());
			re.setObjectType(NewGkElectiveConstant.CHOICE_TYPE_07);
			re.setObjectValue(tid);
			res.add(re);
			re = null;
		}
		return res;
	}

	@Override
	public List<String> findByTeacherPlanId(String teacherPlanId) {
		return newGkTeacherPlanExDao.findByTeacherPlanId(teacherPlanId);
	}

	@Override
	public List<NewGkTeacherPlanEx> findByTeacherId(String arrayItemId, String[] teacherIds) {
		if(StringUtils.isBlank(arrayItemId) || teacherIds == null || teacherIds.length ==0) {
			return null;
		}
		return newGkTeacherPlanExDao.findByTeacherId(arrayItemId,teacherIds);
	}

}
