package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dao.ClassTeachingDao;
import net.zdsoft.basedata.dao.GradeTeachingDao;
import net.zdsoft.basedata.dto.OpenTeachingSearchDto;
import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.service.ClassHourService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.ClassTeachingExService;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.GradeTeachingService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("gradeTeachingService")
public class GradeTeachingServiceImpl extends BaseServiceImpl<GradeTeaching, String> implements GradeTeachingService{

	@Autowired
	private GradeTeachingDao gradeTeachingDao;
	@Autowired
	private ClassService classService;
	@Autowired
	private ClassTeachingDao classTeachingDao;
	@Autowired
	private ClassTeachingService classTeachingService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private ClassHourService classHourService;
	@Autowired
	private ClassTeachingExService classTeachingExService;
	
	
	@Override
	protected BaseJpaRepositoryDao<GradeTeaching, String> getJpaDao() {
		return gradeTeachingDao;
	}

	@Override
	protected Class<GradeTeaching> getEntityClass() {
		return GradeTeaching.class;
	}

	@Override
	public String deleteAndSave(String acadyear, String semester,
			String gradeId, String unitId,String subjectType,String operatorId,
			List<GradeTeaching> newGradeTeachingList, List<GradeTeaching> delGradeTeachingList) {
		//处理gradeTeaching
		List<GradeTeaching> allGradeTeachingList = new ArrayList<GradeTeaching>();
		if(CollectionUtils.isNotEmpty(newGradeTeachingList)){
			allGradeTeachingList.addAll(newGradeTeachingList);
		}
		if(CollectionUtils.isNotEmpty(delGradeTeachingList)){
			allGradeTeachingList.addAll(delGradeTeachingList);
		}
		if(allGradeTeachingList.size() > 0) {
			gradeTeachingDao.saveAll(allGradeTeachingList);
		}
		//处理classTeaching
		Set<String> allSubjectIdSet = EntityUtils.getSet(allGradeTeachingList, GradeTeaching::getSubjectId);
		Set<String> delSubjectIdSet = EntityUtils.getSet(delGradeTeachingList, GradeTeaching::getSubjectId);
		List<ClassTeaching> classTeachingList = new ArrayList<ClassTeaching>();
		List<Clazz> clazzList = classService.findByGradeIdIn(new String[]{gradeId});
		Set<String> classIdSet = EntityUtils.getSet(clazzList, Clazz::getId);
		
		List<String> delClassTeachingIds = new ArrayList<String>();
		Map<String,List<ClassTeaching>> classIdToClassTeachingListMap = new HashMap<String, List<ClassTeaching>>();
		List<ClassTeaching> classTeachingHave = classTeachingService.findClassTeachingList(acadyear, semester, classIdSet.toArray(new String[classIdSet.size()]), unitId, Constant.IS_DELETED_FALSE,allSubjectIdSet.toArray(new String[0]),false);
		for (ClassTeaching classTeaching : classTeachingHave) {
			if(delSubjectIdSet.contains(classTeaching.getSubjectId())){
				classTeaching.setOperatorId(operatorId);
				classTeaching.setIsDeleted(Constant.IS_DELETED_TRUE);
				classTeaching.setModifyTime(new Date());
				delClassTeachingIds.add(classTeaching.getId());
				classTeachingList.add(classTeaching);
			}else{
				String classId = classTeaching.getClassId();
				if(!classIdToClassTeachingListMap.containsKey(classId)){
					classIdToClassTeachingListMap.put(classId, new ArrayList<ClassTeaching>());
				}
				classIdToClassTeachingListMap.get(classId).add(classTeaching);
			}
		}
		
		List<Course> courseList = courseService.findListByIds(allSubjectIdSet.toArray(new String[allSubjectIdSet.size()]));
		Map<String, Course> subjectIdToCourse = EntityUtils.getMap(courseList, Course::getId);
		
		//处理删除的虚拟课程
		List<String> delVirtualIds = courseList.stream().filter(e->delSubjectIdSet.contains(e.getId())&&BaseConstants.VIRTUAL_COURSE_TYPE.equals(e.getCourseTypeId())).map(e->e.getId()).collect(Collectors.toList());
		Set<String> delClassIds = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(delVirtualIds)){
			List<ClassHour> classHours = classHourService.findBySubjectIds(acadyear, semester, unitId, gradeId, delVirtualIds.toArray(new String[delVirtualIds.size()]));
			if(CollectionUtils.isNotEmpty(classHours)){
				List<TeachClass> teachClassList = teachClassService.findByRelaCourseIds(unitId, acadyear, semester, gradeId, EntityUtils.getList(classHours, ClassHour::getId).toArray(new String[0]));
				if(CollectionUtils.isNotEmpty(teachClassList)){
					delClassIds=EntityUtils.getSet(teachClassList, TeachClass::getId);
					for (TeachClass teachClass : teachClassList) {
						teachClass.setRelaCourseId(null);
					}
					teachClassService.saveAll(teachClassList.toArray(new TeachClass[0]));
				}
				classHours.forEach(e->{
					e.setIsDeleted(Constant.IS_DELETED_TRUE);
					e.setModifyTime(new Date());
				});
				classHourService.saveAll(classHours.toArray(new ClassHour[classHours.size()]));
			}
		}
		
		//删除课表数据
		if(CollectionUtils.isNotEmpty(delSubjectIdSet)){
			String[] subjectIds = delSubjectIdSet.toArray(new String[0]);
			delClassIds.addAll(classIdSet);
			String msg = courseScheduleService.deleteByClassIdAndSubjectId(unitId, acadyear, semester, delClassIds.toArray(new String[0]), subjectIds);
			if(StringUtils.isNotBlank(msg)){
				return msg;
			}
		}
		
		/*处理添加的虚拟课程，虚拟课程按照普通课程开设
		Map<String, List<String>> subjectClassMap = new HashMap<String, List<String>>();
		List<String> newVirtualIds = courseList.stream().filter(e->newSubjectIdSet.contains(e.getId())&&BaseConstants.VIRTUAL_COURSE_TYPE.equals(e.getCourseTypeId())).map(e->e.getId()).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(newVirtualIds)){
			subjectClassMap = teachClassService.findClassIdsByVirtualIds(unitId, acadyear, semester, gradeId, newVirtualIds.toArray(new String[newVirtualIds.size()]));
		}
		*/
		ClassTeaching classTeaching = null;
		if(CollectionUtils.isNotEmpty(clazzList)&&CollectionUtils.isNotEmpty(newGradeTeachingList)){
			for (Clazz clazz : clazzList) {
				String classId = clazz.getId();
				List<ClassTeaching> cTeachingList = classIdToClassTeachingListMap.get(classId);
				Map<String, ClassTeaching> cTeachingMap = EntityUtils.getMap(cTeachingList, ClassTeaching::getSubjectId);
				for(GradeTeaching gradeTeaching : newGradeTeachingList) {
					String subjectId = gradeTeaching.getSubjectId();
					if(!cTeachingMap.containsKey(subjectId)){
						/*
						if(newVirtualIds.contains(subjectId) && subjectClassMap.containsKey(subjectId) && !subjectClassMap.get(subjectId).contains(classId)){
							continue;
						}
						*/
						classTeaching = new ClassTeaching();
						classTeaching.setId(UuidUtils.generateUuid());
						classTeaching.setUnitId(unitId);
						classTeaching.setClassId(clazz.getId());
						classTeaching.setSubjectId(gradeTeaching.getSubjectId());
						classTeaching.setAcadyear(acadyear);
						classTeaching.setSemester(semester);
						classTeaching.setCreationTime(new Date());
						classTeaching.setModifyTime(new Date());
						classTeaching.setEventSource(0);
						classTeaching.setOperatorId(operatorId);
						classTeaching.setIsDeleted(Constant.IS_FALSE);
						classTeaching.setSubjectType(gradeTeaching.getSubjectType());
						classTeaching.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
						classTeaching.setCredit(subjectIdToCourse.get(subjectId).getInitCredit());
						classTeaching.setFullMark(subjectIdToCourse.get(subjectId).getFullMark());
						classTeaching.setPassMark(subjectIdToCourse.get(subjectId).getInitPassMark());
						if(BaseConstants.SUBJECT_TYPE_XX.equals(subjectType)) {
							classTeaching.setIsTeaCls(Constant.IS_TRUE);
							classTeaching.setPunchCard(Constant.IS_FALSE);
						}else {
							//必修 设置是否考勤
							classTeaching.setIsTeaCls(Constant.IS_FALSE);
							classTeaching.setPunchCard(Constant.IS_TRUE);
						}
						classTeachingList.add(classTeaching);
					}
				}
			}
		}
		classTeachingDao.saveAll(classTeachingList);
		//处理classTeachingEx
		if(CollectionUtils.isNotEmpty(delClassTeachingIds)){
			classTeachingExService.deleteClassTeachingIdIn(delClassTeachingIds.toArray(new String[delClassTeachingIds.size()]));
		}
		return null;
	}

	@Override
	public void saveAndInit(String acadyearVal, String semesterVal,
			String unitId, String gradeIdVal, String operatorId, Map<String, List<GradeTeaching>> gradeTeachingSaveMap) {

		//封装gradeTeaching
		List<GradeTeaching> gradeTeachingList = new ArrayList<GradeTeaching>();
		//封装classTeaching
		List<ClassTeaching> classTeachingList = new ArrayList<ClassTeaching>();
		
		List<Clazz> clazzList = classService.findByGradeIdIn(gradeTeachingSaveMap.keySet().toArray(new String[0]));
		Map<String, List<String>> gradeIdToClassIdMap = EntityUtils.getListMap(clazzList, Clazz::getGradeId, Clazz::getId);
		Set<String> subjectIdSet = new HashSet<String>();
		gradeTeachingSaveMap.values().forEach(e->{
			e.forEach(t->subjectIdSet.add(t.getSubjectId()));
		});
		List<Course> courseList = courseService.findListByIds(subjectIdSet.toArray(new String[subjectIdSet.size()]));
		Map<String, Course> subjectIdToCourse = EntityUtils.getMap(courseList, Course::getId);
		
		GradeTeaching gradeTeaching = null;
		for(Entry<String, List<GradeTeaching>> entry : gradeTeachingSaveMap.entrySet()){
			String gradeId = entry.getKey();
			List<GradeTeaching> gradeTeachingNewList = entry.getValue();
			for (GradeTeaching gradeTeachingNew : gradeTeachingNewList) {
				gradeTeaching = new GradeTeaching();
				gradeTeaching.setId(UuidUtils.generateUuid());
				gradeTeaching.setAcadyear(gradeTeachingNew.getAcadyear());
				gradeTeaching.setGradeId(gradeId);
				gradeTeaching.setIsDeleted(gradeTeachingNew.getIsDeleted());
				gradeTeaching.setSemester(gradeTeachingNew.getSemester());
				gradeTeaching.setCreationTime(new Date());
				gradeTeaching.setModifyTime(new Date());
				gradeTeaching.setSubjectId(gradeTeachingNew.getSubjectId());
				gradeTeaching.setSubjectType(gradeTeachingNew.getSubjectType());
				gradeTeaching.setUnitId(unitId);
				gradeTeaching.setIsTeaCls(gradeTeachingNew.getIsTeaCls());
				gradeTeachingList.add(gradeTeaching);
				List<String> classIdSet = gradeIdToClassIdMap.get(gradeId);
				if(classIdSet != null) {
					for (String classId : classIdSet) {
						ClassTeaching classTeaching = new ClassTeaching();
						String subjectId = gradeTeaching.getSubjectId();
						classTeaching.setId(UuidUtils.generateUuid());
						classTeaching.setUnitId(unitId);
						classTeaching.setClassId(classId);
						classTeaching.setSubjectId(gradeTeaching.getSubjectId());
						classTeaching.setAcadyear(acadyearVal);
						classTeaching.setSemester(semesterVal);
						classTeaching.setCreationTime(new Date());
						classTeaching.setModifyTime(new Date());
						classTeaching.setEventSource(0);
						classTeaching.setIsDeleted(0);
						classTeaching.setSubjectType(gradeTeaching.getSubjectType());
						classTeaching.setIsTeaCls(gradeTeaching.getIsTeaCls());
						classTeaching.setOperatorId(operatorId);
						classTeaching.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
						classTeaching.setCredit(subjectIdToCourse.get(subjectId).getInitCredit());
						classTeaching.setFullMark(subjectIdToCourse.get(subjectId).getFullMark());
						classTeaching.setPassMark(subjectIdToCourse.get(subjectId).getInitPassMark());
						if(!BaseConstants.SUBJECT_TYPE_XX.equals(gradeTeaching.getSubjectType())) {
							classTeaching.setPunchCard(Constant.IS_TRUE);
						}else {
							classTeaching.setPunchCard(Constant.IS_FALSE);
						}
						classTeachingList.add(classTeaching);
					}
				}
			}
		}
		gradeTeachingDao.saveAll(gradeTeachingList);
		classTeachingDao.saveAll(classTeachingList);
	}

	@Override
	public List<GradeTeaching> findBySearch(OpenTeachingSearchDto dto) {
		
		Specification<GradeTeaching> s = new Specification<GradeTeaching>() {
            @Override
            public Predicate toPredicate(Root<GradeTeaching> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = Lists.newArrayList();
                if(StringUtils.isNotBlank(dto.getUnitId())){
                	  ps.add(cb.equal(root.get("unitId").as(String.class), dto.getUnitId()));
                }
                if(StringUtils.isNotBlank(dto.getAcadyear())){
                	ps.add(cb.equal(root.get("acadyear").as(String.class), dto.getAcadyear()));
                }
                if(StringUtils.isNotBlank(dto.getSemester())){
                	ps.add(cb.equal(root.get("semester").as(String.class), dto.getSemester()));
                }
                if(ArrayUtils.isNotEmpty(dto.getSubjectIds())){
                	ps.add(root.get("subjectId").as(String.class).in(dto.getSubjectIds()));
                }
                if(dto.getIsDeleted()!=null){
                	ps.add(cb.equal(root.get("isDeleted").as(Integer.class), dto.getIsDeleted()));
                }
                if(StringUtils.isNotBlank(dto.getSubjectType())){
                	ps.add(cb.equal(root.get("subjectType").as(String.class), dto.getSubjectType()));
//                	 ps.add(cb.like(root.get("subjectType").as(String.class),"%"+dto.getSubjectType()+"%"));
                }
                if(ArrayUtils.isNotEmpty(dto.getSubjectTypes())){
                	ps.add(root.get("subjectType").as(String.class).in(dto.getSubjectTypes()));
                }
                if(ArrayUtils.isNotEmpty(dto.getGradeIds())){
                	 ps.add(root.get("gradeId").as(String.class).in(dto.getGradeIds()));
                }
                return cq.where(ps.toArray(new Predicate[0])).getRestriction();
            };
        };
		
		return gradeTeachingDao.findAll(s);
		
	}
	
	@Override
	public List<GradeTeaching> findBySearchWithMaster(OpenTeachingSearchDto dto) {
		return findBySearch(dto);
	}

	@Override
	public List<GradeTeaching> findGradeTeachingList(String acadyear,
			String semester, String[] gradeId, String unitId,
			Integer isDeleted, String subjectType, String id) {

		List<GradeTeaching> gradeTeachingList = new ArrayList<GradeTeaching>();
		Specification<GradeTeaching> s = new Specification<GradeTeaching>() {
			@Override
			public Predicate toPredicate(Root<GradeTeaching> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				 List<Predicate> ps = new ArrayList<Predicate>();
				 if(StringUtils.isNotBlank(acadyear)) {
					 ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
				 }
				 if(StringUtils.isNotBlank(semester)) {
					 ps.add(cb.equal(root.get("semester").as(String.class), semester));
				 }
				 if(gradeId != null && gradeId.length > 0) {
					 ps.add(root.get("gradeId").as(String.class).in(gradeId));
				 }
				 if(StringUtils.isNotBlank(unitId)) {
					 ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				 }
				 if(isDeleted != null) {
					 ps.add(cb.equal(root.get("isDeleted").as(Integer.class), isDeleted));
				 }
				 if(subjectType != null) {
					 //ps.add(cb.equal(root.get("subjectType").as(String.class), subjectType));
					 ps.add(cb.like(root.get("subjectType").as(String.class),"%"+subjectType+"%"));
				 }
				 if(StringUtils.isNotBlank(id)) {
					 ps.add(cb.equal(root.get("id").as(String.class), id));
				 }
				 cq.where(ps.toArray(new Predicate[0]));
				 return cq.getRestriction();
			}
        };
        gradeTeachingList = gradeTeachingDao.findAll(s);
        return gradeTeachingList;
	}

	@Override
	public void saveAndInit(String unitId, String acadyear,String semester,String operatorId,List<GradeTeaching> insertList) {
		if(CollectionUtils.isEmpty(insertList)){
			return;
		}
		gradeTeachingDao.saveAll(insertList);
		Set<String> gradeIds = EntityUtils.getSet(insertList, GradeTeaching::getGradeId);
		
		List<ClassTeaching> classTeachingList = new ArrayList<ClassTeaching>();
		List<Clazz> clazzList = classService.findByGradeIdIn(gradeIds.toArray(new String[gradeIds.size()]));
		List<Course> courseList = courseService.findListByIds(EntityUtils.getSet(insertList, GradeTeaching::getSubjectId).toArray(new String[0]));
		Map<String, Course> subjectIdToCourse = EntityUtils.getMap(courseList, Course::getId);
		/*虚拟课程按照普通课程开设
		Map<String, List<String>> subjectClassMap = new HashMap<String, List<String>>();
		List<String> virtualIds = courseList.stream().filter(e->BaseConstants.VIRTUAL_COURSE_TYPE.equals(e.getCourseTypeId())).map(e->e.getId()).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(virtualIds)){
			subjectClassMap = teachClassService.findClassIdsByVirtualIds(unitId, acadyear, semester, "", virtualIds.toArray(new String[virtualIds.size()]));
		}
		*/
		if(CollectionUtils.isNotEmpty(clazzList)){
			Map<String, List<String>> gradeClassMap = EntityUtils.getListMap(clazzList, Clazz::getGradeId, Clazz::getId);
			for(GradeTeaching gradeTeaching : insertList) {
				List<String> classList = gradeClassMap.get(gradeTeaching.getGradeId());
				for (String classId : classList) {
					/*
					if(virtualIds.contains(gradeTeaching.getSubjectId()) && subjectClassMap.containsKey(gradeTeaching.getSubjectId()) && !subjectClassMap.get(gradeTeaching.getSubjectId()).contains(classId)){
						continue;
					}
					*/
					String subjectId = gradeTeaching.getSubjectId();
					ClassTeaching classTeaching=initClassTeaching(gradeTeaching, classId, subjectIdToCourse.get(subjectId), operatorId);
					classTeachingList.add(classTeaching);
				}
			}
			classTeachingDao.saveAll(classTeachingList);
		}
	}
	
	private ClassTeaching initClassTeaching(GradeTeaching gradeTeaching,String clazzId,Course course,String operatorId){
		ClassTeaching classTeaching = new ClassTeaching();
		classTeaching.setId(UuidUtils.generateUuid());
		classTeaching.setUnitId(gradeTeaching.getUnitId());
		classTeaching.setClassId(clazzId);
		classTeaching.setSubjectId(gradeTeaching.getSubjectId());
		classTeaching.setAcadyear(gradeTeaching.getAcadyear());
		classTeaching.setSemester(gradeTeaching.getSemester());
		classTeaching.setCreationTime(new Date());
		classTeaching.setModifyTime(new Date());
		classTeaching.setEventSource(0);
		classTeaching.setIsDeleted(0);
		classTeaching.setSubjectType(gradeTeaching.getSubjectType());
		classTeaching.setWeekType(CourseSchedule.WEEK_TYPE_NORMAL);
		classTeaching.setOperatorId(operatorId);
		classTeaching.setIsTeaCls(gradeTeaching.getIsTeaCls());
		classTeaching.setCredit(course.getInitCredit());
		classTeaching.setFullMark(course.getFullMark());
		classTeaching.setPassMark(course.getInitPassMark());
		if(!BaseConstants.SUBJECT_TYPE_XX.equals(gradeTeaching.getSubjectType())) {
			//必修 设置是否需要考勤
			classTeaching.setPunchCard(Constant.IS_TRUE);
		}else{
			classTeaching.setPunchCard(Constant.IS_FALSE);
		}
		return classTeaching;
	}

	@Override
	public Map<String, GradeTeaching> findBySearchMap(String unitId,String acadyear, String semester, String gradeId) {
		 Map<String, GradeTeaching> returnMap = new HashMap<String, GradeTeaching>();
		 List<GradeTeaching> cList = gradeTeachingDao.findByUnitIdAndIsDeletedAndAcadyearAndSemesterAndGradeId(unitId, Constant.IS_DELETED_FALSE, acadyear, semester,gradeId);
	        if (CollectionUtils.isNotEmpty(cList)) {
	            for (GradeTeaching ct : cList) {
	                returnMap.put(ct.getSubjectId(), ct);
	            }
	        }
	        return returnMap;
	}

	@Override
	public List<GradeTeaching> findBySearchList(String unitId, String acadyear,String semester, String gradeId,String subjectType) {
		OpenTeachingSearchDto dto=new OpenTeachingSearchDto();
		dto.setAcadyear(acadyear);
		dto.setGradeIds(new String[]{gradeId});
		dto.setSemester(semester);
		dto.setUnitId(unitId);
		dto.setSubjectType(subjectType);
		dto.setIsDeleted(0);
		return findBySearch(dto);
	}

	@Override
	public List<GradeTeaching> findGradeTeachingList(String acadyear, String semester, String gradeId, String unitId, String[] subjectTypes) {
		return gradeTeachingDao.findGradeTeachingList(acadyear, semester, gradeId, unitId, subjectTypes);
	}
	@Override
	public List<GradeTeaching> findGradeTeachingListWithMaster(String acadyear, String semester, String gradeId, String unitId, String[] subjectTypes) {
		return gradeTeachingDao.findGradeTeachingList(acadyear, semester, gradeId, unitId, subjectTypes);
	}
	@Override
	public void deleteByGradeIds(String... gradeIds) {
		gradeTeachingDao.deleteByGradeIds(gradeIds);
	}

	@Override
	public void deleteBySubjectIds(String... subjectIds) {
		gradeTeachingDao.deleteBySubjectIds(subjectIds);
		classTeachingService.deleteBySubjectIds(subjectIds);
	}

	@Override
	public String updateOne(GradeTeaching gradeTeaching, String operatorId) {
		List<Clazz> clazzList = classService.findByGradeIdIn(new String[]{gradeTeaching.getGradeId()});
		Set<String> clazzIds = EntityUtils.getSet(clazzList, Clazz::getId);
		List<ClassTeaching> classTeachingList = classTeachingService.findBySearchForList(gradeTeaching.getSubjectId(), gradeTeaching.getAcadyear(), gradeTeaching.getSemester(), clazzIds.toArray(new String[0]));
    	classTeachingList.forEach(e->{
    		e.setIsTeaCls(gradeTeaching.getIsTeaCls());
    		e.setOperatorId(operatorId);
    		e.setCredit(gradeTeaching.getCredit());
    	});
    	if(Constant.IS_TRUE==gradeTeaching.getIsTeaCls()){
			//同时删除行政班该科目课表
			List<Clazz> classList = classService.findByGradeId(gradeTeaching.getGradeId());
			if(CollectionUtils.isNotEmpty(classList)){
				String msg = courseScheduleService.deleteByClassIdAndSubjectId(gradeTeaching.getUnitId(), gradeTeaching.getAcadyear(), gradeTeaching.getSemester(), EntityUtils.getSet(classList, Clazz::getId).toArray(new String[0]), new String[]{gradeTeaching.getSubjectId()});
				if(StringUtils.isNotBlank(msg)){
					return msg;
				}
			}
		}
		this.save(gradeTeaching);
		classTeachingService.saveAll(classTeachingList.toArray(new ClassTeaching[0]));
		return null;
	}

}
