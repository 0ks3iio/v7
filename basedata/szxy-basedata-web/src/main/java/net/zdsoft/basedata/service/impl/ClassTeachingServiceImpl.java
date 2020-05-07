package net.zdsoft.basedata.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.dao.ClassTeachingDao;
import net.zdsoft.basedata.dto.OpenTeachingSearchDto;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.ClassTeachingEx;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.ClassTeachingExService;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("classTeachingService")
public class ClassTeachingServiceImpl extends BaseServiceImpl<ClassTeaching, String> implements ClassTeachingService {

    @Autowired
    private ClassTeachingDao classTeachingDao;
    @Autowired
    private ClassService classService;
    @Autowired
    private TeachClassService teachClassService;
    @Autowired
    private ClassTeachingExService classTeachingExService;
    @Autowired
    private CourseScheduleService courseScheduleService;
    @Override
    protected BaseJpaRepositoryDao<ClassTeaching, String> getJpaDao() {
        return classTeachingDao;
    }

    @Override
    protected Class<ClassTeaching> getEntityClass() {
        return ClassTeaching.class;
    }


    @Override
    public List<ClassTeaching> findBySearchForList(String acadyear, String semester, String[] classIds) {
    	List<ClassTeaching> classTeachingList = new ArrayList<ClassTeaching>();
    	if(classIds!=null && classIds.length>0){
			Specification<ClassTeaching> s = new Specification<ClassTeaching>() {
				@Override
				public Predicate toPredicate(Root<ClassTeaching> root,
						CriteriaQuery<?> cq, CriteriaBuilder cb) {
					 List<Predicate> ps = new ArrayList<Predicate>();
					 queryIn("classId", classIds, root, ps, cb);
					 ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
					 ps.add(cb.equal(root.get("semester").as(String.class), semester));
					 ps.add(cb.equal(root.get("isDeleted").as(Integer.class), Constant.IS_DELETED_FALSE));
					 cq.where(ps.toArray(new Predicate[0]));
					 return cq.getRestriction();
				}
	        };
	        classTeachingList = classTeachingDao.findAll(s);
    	}
        return classTeachingList;
    }

    @Override
    public List<ClassTeaching> findBySearchForList(String subjectId, String acadyear, String semester, String[] classIds) {
        return classTeachingDao.findByIsDeletedAndSubjectIdAndAcadyearAndSemesterAndClassIdIn(
                Constant.IS_DELETED_FALSE, subjectId, acadyear, semester, classIds);
    }

    @Override
    public Map<String, Map<String, ClassTeaching>> findByCourseIdsMap(String unitId, String acadyear, String semester,
            String... subjectIds) {
        Map<String, Map<String, ClassTeaching>> returnMap = new HashMap<String, Map<String, ClassTeaching>>();
        List<ClassTeaching> cList = classTeachingDao.findByUnitIdAndIsDeletedAndAcadyearAndSemesterAndSubjectIdIn(
                unitId, Constant.IS_DELETED_FALSE, acadyear, semester, subjectIds);
        if (CollectionUtils.isNotEmpty(cList)) {
            for (ClassTeaching ct : cList) {
                if (!returnMap.containsKey(ct.getSubjectId())) {
                    returnMap.put(ct.getSubjectId(), new HashMap<String, ClassTeaching>());
                }
                returnMap.get(ct.getSubjectId()).put(ct.getClassId(), ct);
            }
        }
        return returnMap;
    }

	@Override
	public List<ClassTeaching> findClassTeachingListByTeacherId(String unitId, String... teaherIds) {
		List<ClassTeaching> ctList = new ArrayList<ClassTeaching>();
		if(StringUtils.isNotBlank(unitId) && ArrayUtils.isNotEmpty(teaherIds)){
			ctList = classTeachingDao.findClassTeachingListByTeacherId(unitId,teaherIds);
		}else if(StringUtils.isBlank(unitId) && ArrayUtils.isNotEmpty(teaherIds)){
			ctList = classTeachingDao.findClassTeachingListByTeacherId(teaherIds);
		}
		return ctList;
	}

	@Override
	public List<ClassTeaching> findClassTeachingList(String unitId, String acadyear, String semester,
			String... teaherIds) {
		List<ClassTeaching> ctList = new ArrayList<ClassTeaching>();
		if(teaherIds!=null && teaherIds.length > 0){
			ctList = classTeachingDao.findClassTeachingList(unitId,acadyear,semester,teaherIds);
		}
		return ctList;
	}
	
	@Override
	public List<ClassTeaching> findClassTeachingList(final String acadyear, final String semester,
			final String[] classId, final String unitId, final Integer isDeleted,final String[] subjectIds,boolean flag) {
		List<ClassTeaching> classTeachingList = new ArrayList<ClassTeaching>();
		Specification<ClassTeaching> s = new Specification<ClassTeaching>() {
			@Override
			public Predicate toPredicate(Root<ClassTeaching> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				 List<Predicate> ps = new ArrayList<Predicate>();
				 ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
				 ps.add(cb.equal(root.get("semester").as(String.class), semester));
				 if(ArrayUtils.isNotEmpty(classId)){
                	 ps.add(root.get("classId").as(String.class).in(classId));
				 }
				 ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				 if(isDeleted != null) {
					 ps.add(cb.equal(root.get("isDeleted").as(Integer.class), isDeleted));
				 }
				 if(ArrayUtils.isNotEmpty(subjectIds)){
					 ps.add(root.get("subjectId").as(String.class).in(subjectIds));
				 }
				 cq.where(ps.toArray(new Predicate[0]));
				 return cq.getRestriction();
			}
        };
        classTeachingList = classTeachingDao.findAll(s);
        if(flag){
        	if(CollectionUtils.isNotEmpty(classTeachingList)){
        		Set<String> ids = EntityUtils.getSet(classTeachingList, "id");
        		List<ClassTeachingEx> exList = classTeachingExService.findByClassTeachingIdIn(ids.toArray(new String[]{}));
        		Map<String,Set<String>> setMap=new HashMap<String,Set<String>>();
        		Map<String,List<ClassTeachingEx>> listMap = new HashMap<String, List<ClassTeachingEx>>();
        		if(CollectionUtils.isNotEmpty(exList)){
        			for(ClassTeachingEx ex:exList){
        				if(!setMap.containsKey(ex.getClassTeachingId())){
        					setMap.put(ex.getClassTeachingId(), new HashSet<String>());
        					listMap.put(ex.getClassTeachingId(), new ArrayList<ClassTeachingEx>());
        				}
        				setMap.get(ex.getClassTeachingId()).add(ex.getTeacherId());
        				listMap.get(ex.getClassTeachingId()).add(ex);
        			}
        		}
        		for(ClassTeaching item:classTeachingList){
        			if(setMap.containsKey(item.getId())){
        				item.setTeacherIds(setMap.get(item.getId()));
        				item.setExList(listMap.get(item.getId()));
        			}
        		}
        	}
        }
        return classTeachingList;
	}

	@Override
	public List<ClassTeaching> findBySearch(OpenTeachingSearchDto classTeachDto, Boolean makeEx) {
		Specification<ClassTeaching> s = new Specification<ClassTeaching>() {
            @Override
            public Predicate toPredicate(Root<ClassTeaching> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = Lists.newArrayList();
                if(StringUtils.isNotBlank(classTeachDto.getUnitId())){
                	  ps.add(cb.equal(root.get("unitId").as(String.class), classTeachDto.getUnitId()));
                }
                if(StringUtils.isNotBlank(classTeachDto.getAcadyear())){
                	ps.add(cb.equal(root.get("acadyear").as(String.class), classTeachDto.getAcadyear()));
                }
                if(StringUtils.isNotBlank(classTeachDto.getSemester())){
                	ps.add(cb.equal(root.get("semester").as(String.class), classTeachDto.getSemester()));
                }
                if(StringUtils.isNotBlank(classTeachDto.getSubjectId())){
                	ps.add(cb.equal(root.get("subjectId").as(String.class), classTeachDto.getSubjectId()));
                }
                if(classTeachDto.getIsDeleted()!=null){
                	ps.add(cb.equal(root.get("isDeleted").as(Integer.class), classTeachDto.getIsDeleted()));
                }
                if(classTeachDto.getIsTeaCls()!=null){
                	ps.add(cb.equal(root.get("isTeaCls").as(Integer.class), classTeachDto.getIsTeaCls()));
                }
                if(ArrayUtils.isNotEmpty(classTeachDto.getClassIds())){
                	 ps.add(root.get("classId").as(String.class).in(classTeachDto.getClassIds()));
                }
                return cq.where(ps.toArray(new Predicate[0])).getRestriction();
            };
        };
        
        List<ClassTeaching> classTeachingList = classTeachingDao.findAll(s);
        if (Objects.equals(makeEx, true)) {
        	makeTeachingEx(classTeachingList);
        }
        
		return classTeachingList;
	}
	
	@Override
	public List<ClassTeaching> findBySearchWithMaster(OpenTeachingSearchDto classTeachDto, Boolean makeEx){
		return findBySearch(classTeachDto, makeEx);
	}

	private void makeTeachingEx(List<ClassTeaching> classTeachingList) {
		if (CollectionUtils.isNotEmpty(classTeachingList)) {
			List<ClassTeachingEx> classTeachingExList = classTeachingExService.findByClassTeachingIdIn(
					EntityUtils.getSet(classTeachingList, ClassTeaching::getId).toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(classTeachingExList)) {
				Map<String, Set<String>> ctIdTeacherIdMap = classTeachingExList.stream()
						.collect(Collectors.groupingBy(ClassTeachingEx::getClassTeachingId,
								Collectors.mapping(ClassTeachingEx::getTeacherId, Collectors.toSet())));
				
				for (ClassTeaching ct : classTeachingList) {
					if (ctIdTeacherIdMap.containsKey(ct.getId())) {
						ct.setTeacherIds(ctIdTeacherIdMap.get(ct.getId()));
					}
				}
			}
		}
	}

	public void deleteCurrentClassIds(String unitId, String acadyear, String semester, String[] classIds){
		classTeachingDao.deleteCurrentClassIds(unitId, acadyear, semester, classIds);
	}
	
	@Override
	public List<ClassTeaching> findByUnitIdAndAcadyearAndSemesterAndSubjectType(String unitId, String acadyear, String semester, String subjectType) {
		return classTeachingDao.findByIsDeletedAndUnitIdAndAcadyearAndSemesterAndSubjectType(Constant.IS_DELETED_FALSE, unitId,acadyear,
                semester, subjectType);
	}

	@Override
	public List<String> findBySubidTeacherList(String acadyear,
			String semester, String unitId,String gradeId, String subjectId) {
		String[] subjectIds=null;
		if(StringUtils.isNotBlank(subjectId)){
			subjectIds=new String[]{subjectId};
		}
		String[] classIds=null;
		Set<String> teacherIds = new HashSet<String>();
		if(StringUtils.isNotBlank(gradeId)){
			List<Clazz> clazzList = classService.findByGradeId(gradeId);
			if(CollectionUtils.isEmpty(clazzList)){
				return new ArrayList<String>();
			}
			Set<String> ids = EntityUtils.getSet(clazzList, Clazz::getId);
			classIds=ids.toArray(new String[]{});
		}
		List<ClassTeaching> list =findClassTeachingList(acadyear, semester, classIds, unitId, Constant.IS_DELETED_FALSE, subjectIds, false);
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<String>();
		}else{
			Set<String> ids = EntityUtils.getSet(list, ClassTeaching::getId);
    		List<ClassTeachingEx> exList = classTeachingExService.findByClassTeachingIdIn(ids.toArray(new String[]{}));
    		teacherIds = EntityUtils.getSet(exList, ClassTeachingEx::getTeacherId);
		}
		
		teacherIds.addAll(EntityUtils.getSet(list, ClassTeaching::getTeacherId));
		return new ArrayList<String>(teacherIds);
	}

	@Override
	public Map<String, Map<String, ClassTeaching>> findBySearchMap(String unitId,String acadyear, String semester,String[] classIds) {
		 Map<String, Map<String, ClassTeaching>> returnMap = new HashMap<String, Map<String, ClassTeaching>>();
	        List<ClassTeaching> cList = classTeachingDao.findByUnitIdAndAcadyearAndSemesterAndClassIdIn(unitId,acadyear, semester,classIds);
	        if (CollectionUtils.isNotEmpty(cList)) {
	            for (ClassTeaching ct : cList) {
	                if (!returnMap.containsKey(ct.getSubjectId())) {
	                    returnMap.put(ct.getSubjectId(), new HashMap<String, ClassTeaching>());
	                }
	                returnMap.get(ct.getSubjectId()).put(ct.getClassId(), ct);
	            }
	        }
	        return returnMap;
	}

	@Override
	public void deleteClassTeachCouSch(String[] ids) {
		courseScheduleService.deleteByCourseIdIn(ids);
		classTeachingExService.deleteClassTeachingIdIn(ids);
		classTeachingDao.deleteByIds(ids);
	}
	
	@Override
	public List<ClassTeaching> findListByGradeId(String acadyear,String semester, String unitId,String gradeId) {
		String[] classIds=null;
		if(StringUtils.isNotBlank(gradeId)){
			List<Clazz> clazzList = classService.findByGradeId(gradeId);
			if(CollectionUtils.isEmpty(clazzList)){
				return new ArrayList<ClassTeaching>();
			}
			Set<String> ids = EntityUtils.getSet(clazzList, Clazz::getId);
			classIds=ids.toArray(new String[]{});
		}
		return findClassTeachingList(acadyear, semester, classIds, unitId, Constant.IS_DELETED_FALSE, null, false);
	}
	@Override
	public List<ClassTeaching> findListByGradeIdAndSubId(String acadyear, String semester, String unitId,
			String gradeId, String subjectId) {
		String[] classIds=null;
		if(StringUtils.isNotBlank(gradeId)){
			List<Clazz> clazzList = classService.findByGradeId(gradeId);
			if(CollectionUtils.isEmpty(clazzList)){
				return new ArrayList<ClassTeaching>();
			}
			Set<String> ids = EntityUtils.getSet(clazzList, Clazz::getId);
			classIds=ids.toArray(new String[]{});
		}
		return findClassTeachingList(acadyear, semester, classIds, unitId, Constant.IS_DELETED_FALSE, new String[] {subjectId}, false);
	}

    @Override
    public List<ClassTeaching> findClassTeachingListHistoryByTeacherId(String unitId, String[] teaherIds) {
        return classTeachingDao.findClassTeachingListHistoryByTeacherId(unitId,teaherIds);
    }

    @Override
	public void deleteByTeacherIds(String... teacherIds) {
		classTeachingExService.deleteByTeacherIds(teacherIds);
	}

	@Override
	public void deleteByClassIds(String... classIds) {
		classTeachingExService.deleteByClassIds(classIds);
		classTeachingDao.deleteByClassIds(classIds);
	}

	@Override
	public void deleteBySubjectIds(String... subjectIds) {
		classTeachingExService.deleteBySubjectIds(subjectIds);
		classTeachingDao.deleteBySubjectIds(subjectIds);
	}

	@Override
	public List<ClassTeaching> findClassTeachingListWithMaster(String acadyear, String semester, String[] classId,
			String unitId, Integer isDeleted, String[] subjectIds, boolean flag) {
		return this.findClassTeachingList(acadyear, semester, classId, unitId, isDeleted, subjectIds, flag);
	}

	@Override
	public String deleteAndSave(String acadyear, String semester, String classId, String unitId, String[] delSubjectIds, List<ClassTeaching> newClassTeachingList, List<ClassTeaching> delClassTeachingList) {
		//处理classTeaching
		List<ClassTeaching> allClassTeachingList = new ArrayList<ClassTeaching>();
		if(CollectionUtils.isNotEmpty(newClassTeachingList)){
			newClassTeachingList.stream().filter(e->e!=null&&e.getCourseHour()==null).forEach(e->e.setCourseHour(0f));
			allClassTeachingList.addAll(newClassTeachingList);
		}
		//删除扩展表数据
		if(CollectionUtils.isNotEmpty(delClassTeachingList)){
			classTeachingExService.deleteClassTeachingIdIn(EntityUtils.getList(delClassTeachingList, ClassTeaching::getId).toArray(new String[delClassTeachingList.size()]));
			allClassTeachingList.addAll(delClassTeachingList);
		}
		if(allClassTeachingList.size() > 0) {
			classTeachingDao.saveAll(allClassTeachingList);
			// 更新 课表考勤设置
			if(CollectionUtils.isNotEmpty(newClassTeachingList)){
				Map<String, Integer> statusMap = EntityUtils.getMap(newClassTeachingList, e -> e.getSubjectId(), e -> e.getPunchCard());
				courseScheduleService.updatePunchCard(unitId,acadyear,Integer.parseInt(semester),classId,statusMap);
			}
		}
		//删除课表数据
		if(ArrayUtils.isNotEmpty(delSubjectIds)){
			String msg = courseScheduleService.deleteByClassIdAndSubjectId(unitId, acadyear, semester, new String[]{classId}, delSubjectIds);
			if(StringUtils.isNotBlank(msg)){
				return msg;
			}
		}
		return null;
	}
	
	
	@Override
	public Map<String,Integer> getClassTeachingHourMap(String unitId, String[] gradeIds, String acadyear, String semester, int weekIndex){
		Map<String,Integer> map = new HashMap<>();
		
		int weekType;
		if(weekIndex%2 == 0)
			weekType = CourseSchedule.WEEK_TYPE_EVEN;
		else
			weekType = CourseSchedule.WEEK_TYPE_ODD;

		List<Object[]> result = classTeachingDao.getClassTeachingHourMap(unitId, gradeIds, acadyear, semester, weekType);
		for (Object[] objects : result) {
			map.put((String)objects[0], ((BigDecimal)objects[1]).intValue());
		}
		
		return map;
	}
	@Override
	public List<ClassTeaching> findClassTeachingListByBlendTeacherIds(String unitId, String[] teaherIds) {
		List<ClassTeaching> list = this.findClassTeachingListHistoryByTeacherId(unitId,teaherIds);
		if(CollectionUtils.isEmpty(list)){
			list = new ArrayList<ClassTeaching>();
		}
		List<TeachClass> list1 = teachClassService.findByUnitIdAndTeaIds(unitId,teaherIds);
		if(CollectionUtils.isNotEmpty(list1)){
			for (TeachClass c : list1) {
				ClassTeaching ct =new ClassTeaching();
				ct.setClassId(c.getId());
				ct.setTeacherId(c.getTeacherId());
				ct.setSubjectId(c.getCourseId());
				ct.setSemester(c.getSemester());
				ct.setAcadyear(c.getAcadyear());
				ct.setSubjectType(c.getClassType());
				list.add(ct);
			}
		}
		return list;
	}

	@Override
	public List<ClassTeaching> findClassTeachingListByBlendTeacherIds(String unitId, String acadyear, String semester, String[] teaherIds) {
		List<ClassTeaching> list = this.findClassTeachingList(unitId,acadyear,semester,teaherIds);
		if(CollectionUtils.isEmpty(list)){
			list = new ArrayList<ClassTeaching>();
		}
		List<TeachClass> list1 =teachClassService.findByUnitIdAndAcadyearAndSemesterAndTeaIds(unitId,acadyear,semester,teaherIds);
		if(CollectionUtils.isNotEmpty(list1)){
			for (TeachClass c : list1) {
				ClassTeaching ct =new ClassTeaching();
				ct.setClassId(c.getId());
				ct.setTeacherId(c.getTeacherId());
				ct.setSubjectId(c.getCourseId());
				ct.setSemester(c.getSemester());
				ct.setAcadyear(c.getAcadyear());
				ct.setSubjectType(c.getClassType());
				list.add(ct);
			}
		}
		return list;
	}
}
