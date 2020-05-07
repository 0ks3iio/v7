package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.service.TeachClassService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.dao.ClassDao;
import net.zdsoft.basedata.dto.OpenTeachingSearchDto;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("classService")
public class ClassServiceImpl extends BaseServiceImpl<Clazz, String> implements ClassService {

    @Autowired
    private ClassDao clazzDao;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private ClassTeachingService classTeachingService;
    @Autowired
    private TeachClassService teachClassService;

    @Override
    public List<Clazz> findBySchoolIdAndGradeIds(final String schoolId, final String... gradeIds) {
        return clazzDao.findAll(new Specification<Clazz>() {
            @Override
            public Predicate toPredicate(Root<Clazz> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = Lists.newArrayList();
                ps.add(cb.equal(root.get("schoolId").as(String.class), schoolId));
                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                if (ArrayUtils.isNotEmpty(gradeIds)) {
                    ps.add(root.get("gradeId").as(String.class).in(gradeIds));
                }
                return cq.where(ps.toArray(new Predicate[0])).getRestriction();
            };
        });

    }

    @Override
    public List<Clazz> findByGradeIdIn(String... gradeIds) {
        List<Clazz> clazzList = this.findByGradeIdsIn(gradeIds);
        return setClassNameDyn(clazzList.toArray(new Clazz[0]));
    }

    /**
     * 批量生成班级名称
     * 
     * @param findByGradeId
     * @return
     */
    private List<Clazz> setClassNameDyn(Clazz... clazzs) {
        List<Clazz> list = new ArrayList<Clazz>();
        Set<String> gradeIds = new HashSet<String>();
        for (Clazz item : clazzs) {
            gradeIds.add(item.getGradeId());
        }
        Map<String, Grade> findByIdInMap = gradeService.findMapByIdIn(gradeIds.toArray(new String[0]));
        for (Clazz item : clazzs) {
            Grade grade = findByIdInMap.get(item.getGradeId());
            if (grade != null) {
                item.setClassNameDynamic(grade.getGradeName() + item.getClassName());
            }
            else {
                item.setClassNameDynamic(item.getClassName());
            }
            list.add(item);
        }
        return list;
    }

    @Override
    protected BaseJpaRepositoryDao<Clazz, String> getJpaDao() {
        return clazzDao;
    }

    @Override
    public Map<String, Integer> countByGradeIds(String[] gradeIds) {
        return clazzDao.countByGradeIds(gradeIds);
    }

    @Override
    protected Class<Clazz> getEntityClass() {
        return Clazz.class;
    }

    @Override
    public List<Clazz> findClassList(String gradeId, String className) {
        return clazzDao.findClassList(gradeId, className);
    }

    @Override
    public Map<String, List<Clazz>> findMapByGradeIdIn(final String[] gradeIds) {
        List<Clazz> findByGradeIdIn = findByGradeIdsIn(gradeIds);
        Map<String, List<Clazz>> map = new HashMap<String, List<Clazz>>();
        for (Clazz item : findByGradeIdIn) {
            List<Clazz> list = map.get(item.getGradeId());
            if (list == null) {
                list = new ArrayList<Clazz>();
                map.put(item.getGradeId(), list);
            }
            list.add(item);
        }
        return map;
    }

    private List<Clazz> findByGradeIdsIn(final String[] gradeIds) {
        List<Clazz> findByGradeIdIn = new ArrayList<Clazz>();
        if (gradeIds != null && gradeIds.length > 0) {
            Specification<Clazz> s = new Specification<Clazz>() {
                @Override
                public Predicate toPredicate(Root<Clazz> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    List<Predicate> ps = new ArrayList<Predicate>();
                    queryIn("gradeId", gradeIds, root, ps, null);
                    ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                    ps.add(cb.equal(root.get("isGraduate").as(Integer.class), 0));
                    List<Order> orderList = new ArrayList<Order>();
                    orderList.add(cb.asc(root.get("classCode").as(String.class)));
                    cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                    return cq.getRestriction();
                }
            };
            findByGradeIdIn = clazzDao.findAll(s);
        }
        return findByGradeIdIn;
    }

    @Override
    public void deleteAllIsDeleted(String... ids) {
        clazzDao.updateIsDelete(ids);
    }

    @Override
    public String findNextClassCode(String schoolId, String section, String acadyear, int schoolingLength) {
        // 暂不使用开关
        // String value = systemIniService.findValue("SYSTEM.CLASSCODE");

        String classCode = null;

        // if ("1".equals(value)) {
        // // 1=毕业年份+2位学段+2位序号
        // int graduatedYear =
        // NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-")) +
        // schoolingLength;
        // String sectionStr = StringUtils.leftPad(section, 2, "0");
        // String maxCode = clazzDao.findMaxClassCodeByPrefix(schoolId,
        // graduatedYear + sectionStr, 8);
        // if(StringUtils.isBlank(maxCode))
        // classCode = graduatedYear + sectionStr + "01";
        // else
        // classCode = "" + (NumberUtils.toLong(maxCode) + 1);
        // }else if ("2".equals(value)) {
        // // 2=入学年份+2位序号
        // int enrollYear =
        // NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"));
        // String maxCode = clazzDao.findMaxClassCodeByPrefix(schoolId,
        // enrollYear + "", 6);
        // if(StringUtils.isBlank(maxCode))
        // classCode = enrollYear + "01";
        // else
        // classCode = "" + (NumberUtils.toLong(maxCode) + 1);
        // }else {
        // // 3=入学年份+2位学段+2位序号,默认取这种
        int enrollYear = NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"));
        String sectionStr = StringUtils.leftPad(section, 2, "0");
        String maxCode = clazzDao.findMaxClassCodeByPrefix(schoolId, enrollYear + sectionStr, 8);
        if (StringUtils.isBlank(maxCode))
            classCode = enrollYear + sectionStr + "01";
        else
            classCode = "" + (NumberUtils.toLong(maxCode) + 1);
        // }
        return classCode;
    }

    @Override
    public List<Clazz> findByTeachAreaIdIn(String... teachAreaIds) {
        return clazzDao.findByTeachAreaIdIn(teachAreaIds);
    }

    @Override
    public List<Clazz> findListByIds(String[] ids) {
        List<Clazz> clazzList = findListByIdIn(ids);
        return setClassNameDyn(clazzList.toArray(new Clazz[0]));
    }

    @Override
	public List<Clazz> findByIdsSort(String... classIds) {
    	List<Clazz> clazzList = clazzDao.findByIdsSort(classIds);
		return setClassNameDyn(clazzList.toArray(new Clazz[0]));
	}
    
    @Override
    public List<Clazz> findClassList(String schoolId, String courseId, String oepnAcadyear, String acadyear,
            String semester, int section) {
        List<Clazz> clazzList = clazzDao.findClassList(schoolId, oepnAcadyear, section);
        Set<String> classIds = new HashSet<String>();
        for (Clazz clazz : clazzList) {
            classIds.add(clazz.getId());
        }
        List<ClassTeaching> findBySearchForList = classTeachingService.findBySearchForList(courseId, acadyear,
                semester, classIds.toArray(new String[0]));
        Map<String, ClassTeaching> map = new HashMap<String, ClassTeaching>();
        for (ClassTeaching item : findBySearchForList) {
            map.put(item.getClassId(), item);
        }
        List<Clazz> finList = new ArrayList<Clazz>();
        for (Clazz clazz : clazzList) {
            ClassTeaching classTeaching = map.get(clazz.getId());
            if (classTeaching != null) {
                finList.add(clazz);
            }
        }
        
        return setClassNameDyn(finList.toArray(new Clazz[0]));
    }

    @Override
    public Map<String, Clazz> findByIdInMapName(String... ids) {
        List<Clazz> clazzList = this.findListByIdIn(ids);
        clazzList =setClassNameDyn(clazzList.toArray(new Clazz[0]));
        Map<String, Clazz> classMap = new HashMap<String, Clazz>();
        for (Clazz clazz : clazzList) {
            classMap.put(clazz.getId(), clazz);
        }
        return classMap;
    }

    @Override
    public List<Clazz> findByTeacherId(String teacherId) {
        List<Clazz> clazzs = clazzDao.findByTeacherId(teacherId);
        return setClassNameDyn(clazzs.toArray(new Clazz[0]));
    }

    @Override
    public List<Clazz> findByIdAcadyear(String teacherId, String graduateAcadyear) {
        return clazzDao.findByIdAcadyear(teacherId, graduateAcadyear);
    }

    @Override
    public List<Clazz> findByteacherIdAcadyear(String teacheId, String graduateAcadyear) {
        return clazzDao.findByteacherIdAcadyear(teacheId, graduateAcadyear);
    }

    @Override
    public List<Clazz> findBySchoolId(String schoolId) {
        return clazzDao.findBySchoolId(schoolId);
    }

    @Override
    public List<Clazz> findByOpenAcadyear(String schoolId, String openAcadyear) {
        return clazzDao.findByOpenAcadyear(schoolId, openAcadyear);
    }

    @Override
    public List<Clazz> findByGradeId(String schoolId, String gradeId, String teacherId) {
    	List<Clazz> clazzs= null;
    	if(StringUtils.isBlank(teacherId)){
    		clazzs =  clazzDao.findByGradeId(gradeId);
    	}else{
    		clazzs= clazzDao.findByGradeId(schoolId, gradeId, teacherId);
    	}
        return setClassNameDyn(clazzs.toArray(new Clazz[0]));
    }

    @Override
    public List<Clazz> findByIdCurAcadyear(String schoolId, String curAcadyear) {
    	List<Clazz> clazzs= clazzDao.findByIdCurAcadyear(schoolId, curAcadyear);
        return setClassNameDyn(clazzs.toArray(new Clazz[0]));
    }

    @Override
    public List<Clazz> findByOverSchoolinglen(String schoolId, String curAcadyear) {
        return clazzDao.findByOverSchoolinglen(schoolId, curAcadyear);
    }

    @Override
    public List<Clazz> findByGraduateyear(String schoolId, String graduateAcadyear) {
        return clazzDao.findByGraduateyear(schoolId, graduateAcadyear);
    }

    @Override
    public List<Clazz> findByCampusId(String campusId) {
        return clazzDao.findByCampusId(campusId);
    }

    @Override
    public List<Clazz> findByIdYear(String campusId, String graduateAcadyear) {
        return clazzDao.findByIdYear(campusId, graduateAcadyear);
    }

    @Override
    public void updateGraduateSign(int sign, Date currentDate, String classId) {
        clazzDao.updateGraduateSign(sign, currentDate, classId);
    }

    @Override
    public List<Clazz> findClassListByIds(String[] ids) {
        List<Clazz> list = new ArrayList<Clazz>();
        if (ids != null && ids.length > 0) {
            list = clazzDao.findClassListByIds(ids);
            list = setClassNameDyn(list.toArray(new Clazz[0]));
        }
        return list;
    }

    @Override
    public List<Clazz> saveAllEntitys(Clazz... clazz) {
        return clazzDao.saveAll(checkSave(clazz));
    }

	@Override
	public List<Clazz> findByClassCode(final String unitId, final String[] classCodes) {
		List<Clazz> findByClassCodeIn = new ArrayList<Clazz>();
        if (classCodes != null && classCodes.length > 0) {
            Specification<Clazz> s = new Specification<Clazz>() {
                @Override
                public Predicate toPredicate(Root<Clazz> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    List<Predicate> ps = new ArrayList<Predicate>();
                    ps.add(cb.equal(root.get("schoolId").as(String.class), unitId));
                    queryIn("classCode", classCodes, root, ps, null);                 
                    ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                    ps.add(cb.equal(root.get("isGraduate").as(Integer.class), 0));
                    List<Order> orderList = new ArrayList<Order>();
                    orderList.add(cb.asc(root.get("classCode").as(String.class)));
                    cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                    return cq.getRestriction();
                }
            };
            findByClassCodeIn = clazzDao.findAll(s);
        }
        return findByClassCodeIn;
	}

	@Override
	public List<Clazz> findClassList(String schoolId, String courseId,
			String oepnAcadyear, String acadyear, String semester, int section,
			Integer isTeach) {
		List<Clazz> clazzList = clazzDao.findClassList(schoolId, oepnAcadyear, section);
        Set<String> classIds = new HashSet<String>();
        for (Clazz clazz : clazzList) {
            classIds.add(clazz.getId());
        }
        OpenTeachingSearchDto  classTeachDto=new OpenTeachingSearchDto();
        classTeachDto.setAcadyear(acadyear);
        classTeachDto.setSemester(semester);
        classTeachDto.setSubjectId(courseId);
        classTeachDto.setUnitId(schoolId);
        classTeachDto.setIsTeaCls(isTeach);
        classTeachDto.setIsDeleted(0);
        classTeachDto.setClassIds(classIds.toArray(new String[0]));
        List<ClassTeaching> findBySearchForList= classTeachingService.findBySearch(classTeachDto, null);
        
        Map<String, ClassTeaching> map = new HashMap<String, ClassTeaching>();
        for (ClassTeaching item : findBySearchForList) {
            map.put(item.getClassId(), item);
        }
        List<Clazz> finList = new ArrayList<Clazz>();
        for (Clazz clazz : clazzList) {
            ClassTeaching classTeaching = map.get(clazz.getId());
            if (classTeaching != null) {
                finList.add(clazz);
            }
        }
        return setClassNameDyn(finList.toArray(new Clazz[0]));
	}

	@Override
	public List<Clazz> findByGradeId(String gradeId) {
		return clazzDao.findByGradeId(gradeId);
	}

	@Override
	public List<Clazz> findBySchoolIdIn(String[] schoolIds) {
		return clazzDao.findBySchoolIdIn(schoolIds);
	}

    @Override
    public Clazz findOneDynamic(String classId) {
        Clazz clazz = findOne(classId);
        if (clazz == null) {
            return null;
        }
        Grade grade = gradeService.findOne(clazz.getGradeId());
        if (grade != null) {
            clazz.setClassNameDynamic(grade.getGradeName() + clazz.getClassName());
        } else {
            clazz.setClassNameDynamic(clazz.getClassName());
        }
        return clazz;
    }

    @Override
    public void deleteClazzBySchoolId(String unitId) {
        clazzDao.deleteClazzBySchoolId(unitId);
    }
    
    @Override
    public List<Clazz> findByPlaceIds(String schoolId, String[] placeIds){
    	if(placeIds == null || placeIds.length<=0) {
    		return new ArrayList<>();
    	}
    	return clazzDao.findByPlaceIds(schoolId, placeIds);
    }

	@Override
	public List<Clazz> findAllBySchoolIdIn(String[] schoolIds) {
		
		return clazzDao.findAllBySchoolIdIn(schoolIds);
	}

    @Override
    public List<Clazz> findListBlendIds(String[] ids) {
        List<Clazz> list = this.findListByIds(ids);
        if(CollectionUtils.isEmpty(list)){
            list=new ArrayList<>();
        }
        List<TeachClass> list1 =teachClassService.findTeachClassListByIds(ids);
        if(CollectionUtils.isNotEmpty(list1)){
            for(TeachClass ent:list1){
                Clazz ent1 = new Clazz();
                ent1.setId(ent.getId());
                ent1.setGradeId(ent.getGradeId());
                ent1.setSchoolId(ent.getUnitId());
                ent1.setClassName(ent.getName());
                list.add(ent1);
            }
        }
        return list;
    }
}
