package net.zdsoft.basedata.service.impl;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.dao.StudentDao;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeacherSubject;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

import java.lang.reflect.Field;
import java.util.*;

@Service("studentService")
public class StudentServiceImpl extends BaseServiceImpl<Student, String>
		implements StudentService {

	@Autowired
	private StudentDao studentDao;
	@Autowired
	private ClassService classService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private GradeService gradeService;
	@Override
	public Map<String, Integer> countMapByClassIds(String... classIds) {
		return studentDao.countMapByClassIds(classIds);
	}

	@Override
	protected BaseJpaRepositoryDao<Student, String> getJpaDao() {
		return studentDao;
	}

	@Override
	public Map<String, String> getStudentNameMap(String[] studentIds) {
		return studentDao.getStudentNameMap(studentIds);
	}

	@Override
	public List<Student> getStudentIsLeaveSchool(String studentName,
			String identityCard, String unitId, Pagination page) {
		return studentDao.getStudentIsLeaveSchool(studentName, identityCard,
				unitId, page);
	}

	@Override
	protected Class<Student> getEntityClass() {
		return Student.class;
	}

	@Override
	public List<Student> findByClassIds(String... classId) {
		if (classId == null || classId.length == 0) {
			return new ArrayList<Student>();
		}
		return studentDao.findByClassIdsIn(classId);
	}

	@Override
	public List<Student> findByTeachClass(String[] teachClassIds,
			Pagination page) {
		List<Student> list = findByTeachClass(teachClassIds);
		if (CollectionUtils.isNotEmpty(list)) {
			page.setMaxRowCount(list.size());
		} else {
			page.setMaxRowCount(0);
		}
		return studentDao.findByTeachClass(teachClassIds,
				Pagination.toPageable(page));
	}

	@Override
	public List<Student> findByTeachClass(String[] teachClassIds) {
		if (teachClassIds == null || teachClassIds.length == 0) {
			return new ArrayList<Student>();
		}
		List<Student> studentList = studentDao.findByTeachClass(teachClassIds);
		//去重
		List<Student> returnStudentList=new ArrayList<Student>();
		Set<String> ids=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(studentList)){
			for (Student string : studentList) {
				if(ids.contains(string.getId())){
					continue;
				}
				ids.add(string.getId());
				returnStudentList.add(string);
			}
		}
		return returnStudentList;
	}

	@Override
	public List<Student> findByClassIdIn(String stuname, String[] classIds,
			Pagination page) {
		if (StringUtils.isNotBlank(stuname)) {
			stuname = "%" + stuname + "%";
		} else {
			stuname = "%";
		}
		List<Student> list = findByClassIdIn(stuname, classIds);
		if (CollectionUtils.isNotEmpty(list)) {
			page.setMaxRowCount(list.size());
		} else {
			page.setMaxRowCount(0);
		}
		return studentDao.findByClassIdIn(stuname, classIds,
				Pagination.toPageable(page));
	}

	@Override
	public List<Student> findByClassIdIn(String stuname, String[] classIds) {
		if (StringUtils.isNotBlank(stuname)) {
			stuname = "%" + stuname + "%";
		} else {
			stuname = "%";
		}
		return studentDao.findByClassIdIn(stuname, classIds);
	}

	@Override
	public List<Student> findBy(String stuname, String unitId, String schoolId,
			String gradeId, String classId, Pagination page) {
		List<Student> sList = new ArrayList<Student>();
		if (page != null) {
			page.setMaxRowCount(0);
		}
		if (StringUtils.isBlank(unitId)) {
			return sList;
		}
		if (StringUtils.isBlank(schoolId)) {
			List<Unit> schoolIds = unitService.findDirectUnitsByParentId(
					unitId, Constant.CLASS_SCH);
			if (CollectionUtils.isEmpty(schoolIds)) {
				return sList;
			}
			Set<String> schIds = EntityUtils.getSet(schoolIds, "id");
			// 学校
			if (page != null) {
				sList = findBySchoolIdIn(stuname,
						schIds.toArray(new String[] {}), page);
			} else {
				sList = findBySchoolIdIn(stuname,
						schIds.toArray(new String[] {}));
			}
		} else {
			if (StringUtils.isBlank(classId)) {
				if (StringUtils.isBlank(gradeId)) {
					// 学校
					if (page != null) {
						sList = findBySchoolIdIn(stuname,
								new String[] { schoolId }, page);
					} else {
						sList = findBySchoolIdIn(stuname,
								new String[] { schoolId });
					}
				} else {
					// 年级
					List<Clazz> clazzList = classService
							.findByGradeIdIn(new String[] { gradeId });
					if (CollectionUtils.isNotEmpty(clazzList)) {
						Set<String> clIds = EntityUtils.getSet(clazzList, "id");
						if (page != null) {
							sList = findByClassIdIn(stuname,
									clIds.toArray(new String[] {}), page);
						} else {
							sList = findByClassIdIn(stuname,
									clIds.toArray(new String[] {}));
						}

					}
				}
			} else {
				// 班级
				if (page != null) {
					sList = findByClassIdIn(stuname, new String[] { classId },
							page);
				} else {
					sList = findByClassIdIn(stuname, new String[] { classId });
				}
			}
		}
		// 给班级name
		makeClassName(sList);
		return sList;
	}

	@Override
	public List<Student> findByNameSexNoCard(String schoolId, String name,
			Integer sex) {
		return studentDao.findByNameSexNoCard(schoolId, name, sex);
	}

	@Override
	public void updateIdCard(List<Student> studentList) {
		studentDao.updateIdCard(studentList);
	}

	@Override
	public Student findBySchIdStudentCode(String schoolId, String studentCode) {
		return studentDao.findBySchIdStudentCode(schoolId, studentCode);
	}

	public List<Student> findBySchIdStudentCodes(final String schoolId,
			final String[] studentCodes) {
		List<Student> findByGradeIdIn = new ArrayList<Student>();
		Specification<Student> s = new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("schoolId").as(String.class), schoolId));
                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                queryIn("studentCode", studentCodes, root, ps, cb);
                cq.where(ps.toArray(new Predicate[0]));
                return cq.getRestriction();
            }
        };
        findByGradeIdIn = studentDao.findAll(s);
        return findByGradeIdIn;
	}
	
	public List<Student> findByIdentityCards(final String[] identityCards){
		Specification<Student> s = new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("isDeleted").as(String.class), 0));
                queryIn("identityCard", identityCards, root, ps, cb);
                cq.where(ps.toArray(new Predicate[0]));
                return cq.getRestriction();
            }
        };
        return studentDao.findAll(s);
	}

	private void makeClassName(List<Student> sList) {
		if (CollectionUtils.isNotEmpty(sList)) {
			Set<String> clIds = EntityUtils.getSet(sList, "classId");
			Set<String> schIds = EntityUtils.getSet(sList, "schoolId");
			Map<String, Clazz> classMap = classService.findByIdInMapName(clIds
					.toArray(new String[] {}));

			Map<String, Unit> unitMap = unitService.findMapByIdIn(schIds
					.toArray(new String[] {}));

			if (classMap != null) {
				for (Student stu : sList) {
					stu.setClassName((classMap.get(stu.getClassId()) == null) ? ""
							: classMap.get(stu.getClassId())
									.getClassNameDynamic());
					stu.setSchoolName((unitMap.get(stu.getSchoolId()) == null) ? ""
							: unitMap.get(stu.getSchoolId()).getUnitName());
				}
			}
		}

	}

	@Override
	public List<Student> findBySchoolIdIn(String stuname, String[] schoolIds,
			Pagination page) {
		if (StringUtils.isNotBlank(stuname)) {
			stuname = "%" + stuname + "%";
		} else {
			stuname = "%";
		}
		List<Student> list = findBySchoolIdIn(stuname, schoolIds);
		if (CollectionUtils.isNotEmpty(list)) {
			page.setMaxRowCount(list.size());
		} else {
			page.setMaxRowCount(0);
		}
		return studentDao.findBySchoolIdIn(stuname, schoolIds,
				Pagination.toPageable(page));
	}

	@Override
	public List<Student> findBySchoolIdIn(String stuname, String[] schoolIds) {
		if (StringUtils.isNotBlank(stuname)) {
			stuname = "%" + stuname + "%";
		} else {
			stuname = "%";
		}
		return studentDao.findBySchoolIdIn(stuname, schoolIds);
	}

	@Override
	public void updateIsDeleteds(String[] ids) {
		studentDao.updateIsDeleteds(new Date(), ids);
	}

	@Override
	public List<Student> saveAllEntitys(Student... student) {
		return studentDao.saveAll(checkSave(student));
	}

	@Override
	public List<Student> findByIdsClaIdLikeStuCodeNames(final String unitId,
			final String[] ids, final String[] classIds,
			final Student searchStudent, final Pagination page) {
		if (ids != null && ids.length == 0) {
			return new ArrayList<Student>();
		} else if (classIds != null && classIds.length == 0) {
			return new ArrayList<Student>();
		}
		Specification<Student> specification = new Specification<Student>() {
			@Override
			public Predicate toPredicate(Root<Student> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {

				List<Predicate> ps = Lists.newArrayList();

				ps.add(cb.equal(root.get("schoolId").as(String.class), unitId));
				ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));

				if(searchStudent.getIsLeaveSchool() != null) {
					ps.add(cb.equal(root.get("isLeaveSchool").as(Integer.class), searchStudent.getIsLeaveSchool().intValue()));
				}
				if (searchStudent.getSex() != null) {
					ps.add(cb.equal(root.get("sex").as(Integer.class),
							searchStudent.getSex()));
				}
				if (StringUtils.isNotEmpty(searchStudent.getStudentCode())) {
					ps.add(cb.like(root.get("studentCode").as(String.class),
							searchStudent.getStudentCode()));
				}
				if (StringUtils.isNotEmpty(searchStudent.getStudentName())) {
					ps.add(cb.like(root.get("studentName").as(String.class),
							searchStudent.getStudentName()));
				}
				if(StringUtils.isNotEmpty(searchStudent.getOldSchoolName())){
					ps.add(cb.like(root.get("oldSchoolName").as(String.class),
							searchStudent.getOldSchoolName()));
				}
				if(StringUtils.isNotEmpty(searchStudent.getHomeAddress())){
					ps.add(cb.like(root.get("homeAddress").as(String.class),
							searchStudent.getHomeAddress()));
				}
				// List<Predicate> inPredicateList = Lists.newArrayList();
				if (classIds != null && classIds.length > 0) {
					queryIn("classId", classIds, root, ps, null);
				}

				List<Predicate> idInPredicateList = Lists.newArrayList();
				if (ids != null && ids.length > 0) {
					queryIn("id", ids, root, idInPredicateList, null);
				}

				Predicate andPredicate = cb.and(ps.toArray(new Predicate[ps
						.size()]));
				List<Predicate> orPredicates = Lists.newArrayList();
				for (int i = 0; i < idInPredicateList.size(); i++) {
					orPredicates.add(cb.and(cb.and(andPredicate,
							idInPredicateList.get(i))));
				}
				// List<Predicate> orList = Lists.newArrayList();

				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(root.get("classId").as(String.class)));
				orderList.add(cb.asc(root.get("studentCode").as(String.class)));
				if (orPredicates.size() > 0)
					cq.where(
							cb.or(orPredicates
									.toArray(new Predicate[orPredicates.size()])))
							.orderBy(orderList);
				else
					cq.where(andPredicate).orderBy(orderList);
				return cq.getRestriction();
			}

		};
		if (page != null) {
			Pageable pageable = Pagination.toPageable(page);
			Page<Student> findAll = studentDao.findAll(specification, pageable);
			page.setMaxRowCount((int) findAll.getTotalElements());
			return findAll.getContent();
		} else {
			return studentDao.findAll(specification);
		}
	}

	@Override
	public List<Student> findByNotIdsClaIdLikeStuCodeNames(final String unitId,
			final String[] ids, final String[] classIds,
			final Student searchStudent, final Pagination page) {
		if (classIds != null && classIds.length == 0) {
			return new ArrayList<Student>();
		}
		Specification<Student> specification = new Specification<Student>() {
			@Override
			public Predicate toPredicate(final Root<Student> root,
					final CriteriaQuery<?> cq, final CriteriaBuilder cb) {
				final List<Predicate> ps = Lists.newArrayList();

				ps.add(cb.equal(root.get("schoolId").as(String.class), unitId));
				ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
				if(searchStudent.getIsLeaveSchool() != null) {
					ps.add(cb.equal(root.get("isLeaveSchool").as(Integer.class), searchStudent.getIsLeaveSchool().intValue()));
				}
				if (classIds != null && classIds.length > 0) {
					queryIn("classId", classIds, root, ps, null);
				}

				if (ids != null && ids.length > 0) {
					queryNotIn("id", ids, root, ps, cb);
				}

				if (searchStudent.getSex() != null) {
					ps.add(cb.equal(root.get("sex").as(Integer.class),
							searchStudent.getSex()));
				}
				if (StringUtils.isNotEmpty(searchStudent.getStudentCode())) {
					ps.add(cb.like(root.get("studentCode").as(String.class),
							searchStudent.getStudentCode()));
				}
				if (StringUtils.isNotEmpty(searchStudent.getStudentName())) {
					ps.add(cb.like(root.get("studentName").as(String.class),
							searchStudent.getStudentName()));
				}

				final List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(root.get("classId").as(String.class)));
				orderList.add(cb.asc(root.get("studentCode").as(String.class)));
				cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				return cq.getRestriction();
			}

		};
		if (page != null) {
			Pageable pageable = Pagination.toPageable(page);
			Page<Student> findAll = studentDao.findAll(specification, pageable);
			page.setMaxRowCount((int) findAll.getTotalElements());
			return findAll.getContent();
		} else {
			return studentDao.findAll(specification);
		}
	}

	@Override
	public List<Student> findByUnitLikeCode(final String unitId,
			final String valCode, final String Val) {
		return studentDao.findAll(new Specification<Student>() {
			@Override
			public Predicate toPredicate(Root<Student> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("schoolId").as(String.class), unitId));
				ps.add(cb.like(root.get(valCode).as(String.class), Val));
				ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
				return cq.where(ps.toArray(new Predicate[0])).getRestriction();
			}

		});
	}

	@Override
	public Student findByGreadIdStuCode(String greadId, String studentCode) {
		return studentDao.findByGreadIdStuCode(greadId, studentCode);
	}

	@Override
	public Map<String, List<Student>> findMapByClassIdIn(String[] classIds) {
		List<Student> findByClassIdsIn = findByClassIdsIn(classIds);
		Map<String, List<Student>> map = new HashMap<String, List<Student>>();
		for (Student item : findByClassIdsIn) {
			List<Student> list = map.get(item.getClassId());
			if (list == null) {
				list = new ArrayList<Student>();
				map.put(item.getClassId(), list);
			}
			list.add(item);
		}
		return map;
	}

	private List<Student> findByClassIdsIn(final String[] classIds) {
		List<Student> findByClassIdsIn = new ArrayList<Student>();
		if (classIds != null && classIds.length > 0) {
			Specification<Student> s = new Specification<Student>() {
				@Override
				public Predicate toPredicate(Root<Student> root,
						CriteriaQuery<?> cq, CriteriaBuilder cb) {
					List<Predicate> ps = new ArrayList<Predicate>();
					queryIn("classId", classIds, root, ps, null);
					ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
					ps.add(cb.equal(
							root.get("isLeaveSchool").as(Integer.class), 0));
					List<Order> orderList = new ArrayList<Order>();
					orderList.add(cb.asc(root.get("studentCode").as(
							String.class)));
					cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
					return cq.getRestriction();
				}
			};
			findByClassIdsIn = studentDao.findAll(s);
		}
		return findByClassIdsIn;
	}

	@Override
	public List<Student> findByGradeIds(String... gradeIds) {
		List<Clazz> classList = classService.findByGradeIdIn(gradeIds);
		List<Student> studentList = new ArrayList<Student>();
		if (CollectionUtils.isNotEmpty(classList)) {
			Set<String> classIds = new HashSet<String>();
			for (Clazz clazz : classList) {
				classIds.add(clazz.getId());
			}
			studentList = findByClassIds(classIds.toArray(new String[0]));
		}
		return studentList;
	}

	@Override
	public Student findByCardNumber(String unitId, String cardNumber) {
		return studentDao.findByCardNumber(unitId, cardNumber);
	}

	@Override
	public List<Student> findByStudentNameAndIdentityCardWithNoUser(
			String studentName, String identityCard) {
		List<Student> studentList = studentDao
				.findByStudentNameAndIdentityCardWithNoUser(studentName,
						identityCard);
		makeClassName(studentList);
		return studentList;
	}

	@Override
	public List<Student> findByStudentNameAndIdentityCard(String studentName,
			String identityCard) {
		List<Student> studentList = studentDao
				.findByStudentNameAndIdentityCard(studentName, identityCard);
		makeClassName(studentList);
		return studentList;
	}

	@Override
	public int[] updateCardNumber(List<String[]> studentList) {
		return studentDao.updateCardNumber(studentList);
	}

	@Override
	public List<Student> findByGradeId(String gradeId) {
		return studentDao.findByGreadId(gradeId);
	}

	@Override
	public List<Student> findAllStudentByGradeId(String gradeId) {
		return studentDao.findAllStudentByGradeId(gradeId);
	}

	@Override
	public List<Student> findByClaIdsLikeStuCodeNames(String unitId,
			String gradeId, String[] classIds, Student searchStudent) {
		List<Student> list= studentDao.findByClaIdsLikeStuCodeNames(unitId, gradeId, classIds, searchStudent);
		if(CollectionUtils.isNotEmpty(list)){
			Grade g= gradeService.findById(gradeId);
			for(Student ent:list){
				ent.setClassName(g.getGradeName()+ent.getClassName());
			}
		}
		return list;
	}

	@Override
	public List<Student> findBySchoolId(String schoolId) {
		return studentDao.findBySchoolId(schoolId);
	}

	@Override
	public List<Student> findPartStudByGradeId(String unitId, String gradeId, String[] classIds, String[] studentIds) {
		if(StringUtils.isNotBlank(gradeId) &&classIds ==null &&studentIds == null) {
			List<Clazz> list= classService.findByGradeId(gradeId);
			if(CollectionUtils.isNotEmpty(list)) {
			 return	studentDao.findPartStudByClaIds(unitId, EntityUtils.getSet(list, Clazz::getId).toArray(new String[0]));
			}
			return new ArrayList<Student>();
		}else if(classIds!=null && classIds.length>0 &&StringUtils.isBlank(gradeId) && studentIds == null){
			return	studentDao.findPartStudByClaIds(unitId, classIds);
		}else if( studentIds!=null && studentIds.length>0 &&StringUtils.isBlank(gradeId) && classIds == null) {
			return studentDao.findPartStudByStuIds(unitId, studentIds);
		}
		return new ArrayList<Student>();
	}

	@Override
	public long CountStudByGradeId(String gradeId) {
		List<Clazz> list= classService.findByGradeId(gradeId);
		if(CollectionUtils.isNotEmpty(list)) {
		 return	studentDao.CountStudByClaIds(EntityUtils.getSet(list, Clazz::getId).toArray(new String[0]));
		}
		return 0;
	}

	@Override
	public void updateClaIds(List<Student> studentList) {
		studentDao.updateClaIds(studentList);
	}
	@Override
	public List<Student> findPartStudentById(String[] studentIds) {
		return studentDao.findPartAllStudentByStuIds(studentIds);
	}
	@Override
	public Map<String,Object> findMapByAttr(String[] ids, String attrName){
		
		List<Student> findAll = studentDao.findListBy(Student.class, null, null, "id", ids, new String[] {"id",attrName});
		Map<String,Object> map = new HashMap<>();
		try {
			Field field = Student.class.getDeclaredField(attrName);
			field.setAccessible(true);
			for (Student student : findAll) {
				
				map.put(student.getId(), field.get(student));
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public void deleteStudentsBySchoolId(String schoolId) {
		studentDao.deleteStudentsBySchoolId(schoolId);
	}

	@Override
	public List<Student> findBySchoolIdStudentNameStudentCode(String unitId, String studentName, String studentCode) {
		return studentDao.findBySchoolIdStudentNameStudentCode(unitId,studentName,studentCode);
	}

	@Override
	public Student findByStudentCode(String studentCode) {
		return studentDao.findByStudentCode(studentCode);
	}

	@Override
	public Map<String,Integer> countListBySchoolIds(String[] schoolIds) {
		return studentDao.countListBySchoolIds(schoolIds);
	}

	@Override
	public Map<String, Integer> countMapByGradeIds(String[] gradeIds) {
		return studentDao.countMapByGradeIds(gradeIds);
	}

	@Override
	public List<Student> findListBlendClassIds(String[] ids) {
		return studentDao.findListBlendClassIds(ids);
	}

	@Override
	public List<Student> findListByIds(String[] ids){
		if (ArrayUtils.isEmpty(ids)) {
            return new ArrayList<Student>();
        }else{//ids去重复
			Set<String> staffsSet = new HashSet<>(Arrays.asList(ids));
			ids  = staffsSet.toArray(new String[0]);
		}
        List<Student> stuList = new ArrayList<>();
        if (ids.length <= 1000) {
        	stuList = studentDao.findAllById(Arrays.asList(ids));
        } else {
            int cyc = ids.length / 1000 + (ids.length % 1000 == 0 ? 0 : 1);
            for (int i = 0; i < cyc; i++) {
                int max = (i + 1) * 1000;
                if (max > ids.length)
                    max = ids.length;
                List<Student> students = studentDao.findAllById(Arrays.asList(ArrayUtils.subarray(ids, i * 1000, max)));
                if (CollectionUtils.isNotEmpty(students)) {
                	stuList.addAll(students);
                }
            }
        }
        return stuList;
	}


}
