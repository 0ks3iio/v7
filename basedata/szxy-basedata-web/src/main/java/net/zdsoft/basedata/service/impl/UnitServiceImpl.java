package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.dao.DeptDao;
import net.zdsoft.basedata.dao.UnitDao;
import net.zdsoft.basedata.dao.UnitJdbcDao;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.EduInfo;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.SchtypeSection;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.enums.DeptConstants;
import net.zdsoft.basedata.enums.EventSourceCode;
import net.zdsoft.basedata.enums.UnionCodeLengthCalculator;
import net.zdsoft.basedata.enums.UnitClassCode;
import net.zdsoft.basedata.enums.UnitClassEnum;
import net.zdsoft.basedata.enums.UnitLevelCode;
import net.zdsoft.basedata.enums.UnitTypeCode;
import net.zdsoft.basedata.enums.UserOwnerTypeCode;
import net.zdsoft.basedata.enums.UserSexCode;
import net.zdsoft.basedata.enums.UserStateCode;
import net.zdsoft.basedata.enums.UserTypeCode;
import net.zdsoft.basedata.remote.UnionCodeAlreadyExistsException;
import net.zdsoft.basedata.remote.UsernameAlreadyExistsException;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.basedata.service.EduInfoService;
import net.zdsoft.basedata.service.FamilyService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.RegionService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.SchtypeSectionService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.Specifications;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.system.service.config.UnitIniService;
import net.zdsoft.system.service.user.RoleService;

@Service("unitService")
public class UnitServiceImpl extends BaseServiceImpl<Unit, String> implements UnitService {

    @Autowired
    private UnitDao unitDao;
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private UnitJdbcDao unitJdbcDao;
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UnitIniService unitIniService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private EduInfoService eduInfoService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private FamilyService familyService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ClassService classService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private DateInfoService dateInfoService;
    @Autowired
    private SchtypeSectionService schtypeSectionService;

    @Override
    public List<Unit> findAll() {
        Specifications<Unit> s = new Specifications<Unit>().addEq("isDeleted", 0);
        return unitDao.findAll(s.getSpecification());
    }

    @Override
    public List<Unit> findAll(Pagination page) {
        Pageable pageable = new PageRequest(page.getPageIndex(), page.getPageSize());
        Page<Unit> units = unitDao.findAll(pageable);
        if (units != null) {
            return units.getContent();
        }

        return new ArrayList<Unit>();
    }

    @Override
    public Unit findTopUnit() {
        return unitDao.findTopUnit();
    }

    @Override
    public Unit findTopUnit(String unitId) {
        if (StringUtils.isBlank(unitId)) {
            return findTopUnit();
        }
        Unit unit = unitDao.findById(unitId).orElse(null);
        if (unit != null && StringUtils.isNotBlank(unit.getRootUnitId())) {
            return unitDao.findById(unit.getRootUnitId()).orElse(null);
        } else {
            return unitDao.findTopUnit();
        }
    }

    @Override
    public List<Unit> findDirectUnitsByParentId(final String parentId, final Integer unitClass) {
        return unitDao.findAll(new Specification<Unit>() {
            @Override
            public Predicate toPredicate(Root<Unit> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
                List<Predicate> ps = Lists.newArrayList();
                if (StringUtils.isNotEmpty(parentId)) {
                    ps.add(builder.equal(root.get("parentId").as(String.class), parentId));
                }
                if (unitClass != null) {
                    ps.add(builder.equal(root.get("unitClass").as(Integer.class), unitClass));
                }
                ps.add(builder.equal(root.get("isDeleted").as(String.class), "0"));
                criteria.where(ps.toArray(new Predicate[0]));
                return criteria.getRestriction();
            }
        });
    }

    @Override
    public List<Unit> findByUnitClassAndRegionCode(final int unitClass, final String... regionCodes) {
        List<Unit> units = unitDao.findAll(new Specification<Unit>() {
            @Override
            public Predicate toPredicate(Root<Unit> r, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                return cq.where(
                        cb.and(cb.equal(r.get("unitClass").as(Integer.class), unitClass),
                                r.get("regionCode").in(regionCodes))).getRestriction();
            }
        });
        return units;
    }

    @Override
    public List<Unit> findByUnionId(final String unionId, final int state, final int unitClass) {

        return unitDao.findAll(new Specification<Unit>() {
            @Override
            public Predicate toPredicate(Root<Unit> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
                List<Predicate> ps = Lists.newArrayList();
                ps.add(builder.like(root.<String>get("unionCode"), unionId == null ? "%" : unionId + "%"));
                if (unitClass > -1) {
                    ps.add(builder.equal(root.get("unitClass").as(Integer.class), unitClass));
                }
                if (state > -1) {
                    ps.add(builder.equal(root.get("unitState").as(Integer.class), state));
                }
                ps.add(builder.equal(root.get("isDeleted").as(String.class), "0"));
                criteria.where(ps.toArray(new Predicate[0]));
                return criteria.getRestriction();
            }
        });
        // return unitDao.fandByUnionId(unionId,state,unitClass);
    }

    @Override
    public List<Unit> findByUnitName(String... unitNames) {
        if (unitNames.length > 1) {
            return unitDao.findByUnitNames(unitNames);
        } else {
            return unitDao.findByUnitName(unitNames[0]);
        }
    }

    @Override
    public List<Unit> findByParentIdAndUnitClass(String[] parentId, int state, int unitClass) {
        return unitDao.findByParentIdAndUnitClass(parentId, state, unitClass);
    }

    @Override
    public List<Unit> findByRegion(String region, int state) {
        return unitDao.findByRegion(region, state);
    }

    @Override
    public List<Unit> saveAllEntitys(Unit... unit) {
        return unitDao.saveAll(checkSave(unit));
    }

    @Override
    public List<Unit> findByUseType(int state, int useType) {
        return unitDao.findByUseType(state, useType);
    }

    @Override
    public List<Unit> findByNameAndUnionCode(String unitName, String unionId, int state, int unitClass) {
        return unitDao.findByNameAndUnionCode(unitName, unionId, state, unitClass);
    }

    @Override
    public List<Unit> findByUnderlingUnits(String unitName, String unionId, Pageable pageable) {
        return unitDao.findByUnderlingUnits(unitName, unionId, pageable);
    }

    @Override
    public List<Unit> findByUnderlingUnits(String unitName, String unionId) {
        return unitDao.findByUnderlingUnits(unitName, unionId);
    }

    @Override
    public List<Unit> findBySerialNumber(String sNumber, String eNumber) {
        return unitDao.findBySerialNumber(sNumber, eNumber);
    }

    @Override
    public Long countUnderlingUnits(String unitName, String unionId) {
        return unitDao.countUnderlingUnits(unitName, unionId);
    }

    @Override
    public List<Unit> findByUnitName(String unitName, Pageable pageable) {
        return unitDao.findByUnitName(unitName, pageable);
    }

    @Override
    public Long countByUnitName(final String unitName) {
        return unitDao.count(new Specification<Unit>() {
            @Override
            public Predicate toPredicate(Root<Unit> r, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                return cq.where(cb.like(r.<String>get("unitName"), unitName == null ? "%" : unitName))
                        .getRestriction();
            }
        });
    }

    @Override
    public List<Unit> findByParentIdAndUnitClass(String parentId, int state, int unitClass, Pageable pageable) {
        return unitDao.findByParentIdAndUnitClass(parentId, state, unitClass, pageable);
    }

    @Override
    public Long countParentIdAndUnitClass(final String parentId, final int state, final int unitClass) {
        return unitDao.count(new Specification<Unit>() {
            @Override
            public Predicate toPredicate(Root<Unit> r, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                return cq.where(cb.equal(r.get("parentId").as(Integer.class), parentId),
                        cb.equal(r.get("unitState").as(Integer.class), state),
                        cb.equal(r.get("unitClass").as(Integer.class), unitClass)).getRestriction();
            }
        });
    }

    @Override
    public List<Unit> findByUnitClass(int unitClass) {
        return unitDao.findByUnitClass(unitClass);
    }

    /**
     * 分页，取出所有下属单位 不做缓存
     */
    @Override
    public List<Unit> findAllUnitsByParentId(final String parentId, final Pagination page) {
        Unit parentUnit = unitDao.findById(parentId).orElse(null);
        String unionCode = parentUnit.getUnionCode();
        
        return unitDao.findByBeginWithUnionCode(unionCode, Pagination.toPageable(page));
    }

    @Override
    protected BaseJpaRepositoryDao<Unit, String> getJpaDao() {
        return unitDao;
    }

    @Override
    protected Class<Unit> getEntityClass() {
        return Unit.class;
    }

    @Override
    public Map<String, List<Unit>> findDirectUnitsByParentIds(final Integer unitClass, final String... parentId) {

        Specification<Unit> s = new Specification<Unit>() {
            @Override
            public Predicate toPredicate(Root<Unit> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                queryIn("parentId", parentId, root, ps, null);
                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                if (unitClass != null) {
                    ps.add(cb.equal(root.get("unitClass").as(Integer.class), unitClass));
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("unionCode").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        List<Unit> unitList = unitDao.findAll(s);
        Map<String, List<Unit>> map = new HashMap<String, List<Unit>>();
        for (Unit item : unitList) {
            List<Unit> list = map.get(item.getParentId());
            if (list == null) {
                list = new ArrayList<Unit>();
                map.put(item.getParentId(), list);
            }
            list.add(item);
        }
        return map;
    }

    @Override
    public void deleteAllByIds(String... id) {
        if (id != null && id.length > 0) {
            unitDao.deleteAllByIds(id);
        }
    }

    @Override
    public List<Unit> findByRegionAndUnitName(final String region, final String unitName, Pagination page) {

        Specification<Unit> specification = new Specification<Unit>() {

            @Override
            public Predicate toPredicate(Root<Unit> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();

                if (StringUtils.isNotEmpty(region)) {
                    ps.add(cb.like(root.get("regionCode").as(String.class), region + "%"));
                }

                if (StringUtils.isNotEmpty(unitName)) {
                    ps.add(cb.like(root.get("unitName").as(String.class), unitName + "%"));
                }
                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));

                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("unitName").as(String.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }

        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<Unit> findAll = unitDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            return findAll.getContent();
        } else {
            return unitDao.findAll(specification);
        }
    }

    @Override
    public List<Unit> findByUnitClassAndRegion(final int unitClass, final String regionCodes, Pagination page) {
        // TODO Auto-generated method stub
        return findAll(new Specification<Unit>() {

            @Override
            public Predicate toPredicate(Root<Unit> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> ps = Lists.newArrayList();
                List<Order> orders = Lists.newArrayList();
                if (unitClass != 0) {
                    ps.add(cb.equal(root.<Integer>get("unitClass"), unitClass));
                }
                if (StringUtils.isNotEmpty(regionCodes)) {
                    ps.add(cb.like(root.<String>get("regionCode"), regionCodes));
                }
                ps.add(cb.equal(root.<String>get("isDeleted"), "0"));
                // 排序
                orders.add(cb.asc(root.<String>get("regionCode")));
                return query.where(ps.toArray(new Predicate[0])).orderBy(orders).getGroupRestriction();
            }
        }, page);
    }

    @Override
    public List<Unit> findDirectUnits(final String commitUnitId, final Integer unitClassInteger, Pagination page) {
        // TODO Auto-generated method stub
        return findAll(new Specification<Unit>() {

            @Override
            public Predicate toPredicate(Root<Unit> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> ps = Lists.newArrayList();
                List<Order> orders = Lists.newArrayList();
                if (unitClassInteger != null) {
                    ps.add(cb.equal(root.<Integer>get("unitClass"), unitClassInteger));
                }
                if (StringUtils.isNotEmpty(commitUnitId)) {
                    ps.add(cb.like(root.<String>get("parentId"), commitUnitId));
                }
                ps.add(cb.equal(root.<String>get("isDeleted"), "0"));
                // 排序
                orders.add(cb.asc(root.<String>get("regionCode")));
                return query.where(ps.toArray(new Predicate[0])).orderBy(orders).getGroupRestriction();
            }
        }, page);
    }

    @Override
    public List<Unit> findByRegionAndUnitClassAndUnitIdIn(final Integer[] unitClass, final String regionCode,
                                                          final String[] unitIds, Pagination page) {
        Specification<Unit> specification = new Specification<Unit>() {

            @Override
            public Predicate toPredicate(Root<Unit> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();

                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                ps.add(root.get("unitClass").as(Integer.class).in(unitClass));

                if (StringUtils.isNotEmpty(regionCode)) {
                    ps.add(cb.like(root.get("regionCode").as(String.class), regionCode + "%"));
                }

                if (null != unitIds && unitIds.length > 0) {
                    queryIn("id", unitIds, root, ps, null);
                }

                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("unitName").as(String.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }

        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<Unit> findAll = unitDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            return findAll.getContent();
        } else {
            return unitDao.findAll(specification);
        }
    }

    @Override
    public List<Unit> findByRegionAndUnitClassAndUnitIdNotIn(final Integer[] unitClass, final String regionCode,
                                                             final String[] unitIds, Pagination page) {
        Specification<Unit> specification = new Specification<Unit>() {

            @Override
            public Predicate toPredicate(Root<Unit> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();

                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                ps.add(root.get("unitClass").as(Integer.class).in(unitClass));

                if (StringUtils.isNotEmpty(regionCode)) {
                    ps.add(cb.like(root.get("regionCode").as(String.class), regionCode + "%"));
                }

                if (null != unitIds && unitIds.length > 0) {
                    ps.add(cb.not(root.get("id").as(String.class).in(unitIds)));
                }

                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("unitName").as(String.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }

        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<Unit> findAll = unitDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            return findAll.getContent();
        } else {
            return unitDao.findAll(specification);
        }
    }

    @Override
    public void addTopUnit(String unitName, String regionCode) {
        Date now = new Date();
        // 顶级单位
        Unit topUnit = new Unit();
        topUnit.setId(UuidUtils.generateUuid());
        topUnit.setUnitName(unitName);
        topUnit.setRegionCode(regionCode);
        topUnit.setUnitClass(Unit.UNIT_CLASS_EDU);
        topUnit.setRegionLevel(buildRegionLevel(regionCode));
        topUnit.setParentId(Unit.TOP_UNIT_GUID);
        topUnit.setCreationTime(now);
        topUnit.setModifyTime(now);
        topUnit.setIsDeleted(0);
        topUnit.setEventSource(0);
        topUnit.setOrgVersion(1);
        topUnit.setUnitState(1);
        int regionLevel = regionCode.equals("000000") ? 1 : regionCode.endsWith("0000") ? 2 : regionCode.endsWith("00") ? 3 : 4;
        topUnit.setRegionCode(regionCode);
        topUnit.setRegionLevel(regionLevel);
        topUnit.setUnitState(1);
        topUnit.setUnitClass(1);
        topUnit.setUnitType(1);
        int subLength = regionCode.equals("000000") ? 0 : regionCode.endsWith("0000") ? 2 : regionCode.endsWith("00") ? 4 : 6;
        topUnit.setUnionCode(org.apache.commons.lang3.StringUtils.substring(regionCode, 0, subLength));
        topUnit.setSerialNumber("1");
        // 默认部门
        Dept dept = new Dept();
        dept.setId(UuidUtils.generateUuid());
        dept.setUnitId(topUnit.getId());
        dept.setDeptCode("000001");
        dept.setDeptType(1); //科室
        dept.setParentId(Constant.GUID_ZERO);
        dept.setDeptName("管理员组");
        dept.setCreationTime(now);
        dept.setModifyTime(now);
        dept.setDeptState(1);
        dept.setIsDeleted(0);
        dept.setEventSource(0);
        dept.setInstituteId(Constant.GUID_ZERO);

        unitDao.save(topUnit);
        deptDao.save(dept);
    }

    // 2 省 3 地市 4 区县
    private int buildRegionLevel(String regionCode) {
        regionCode = StringUtils.addZeroRight(regionCode, 6);
        if (regionCode.equals("000000")) {
            // 几乎不会有
            return 1;
        } else if (regionCode.endsWith("0000")) {
            return 2;
        } else if (regionCode.endsWith("00")) {
            return 3;
        } else {
            return 4;
        }
    }

    @Override
    public List<Unit> findAutorizeServerUnit(Integer[] unitClass, String regionCode, Integer serverId, Pagination page) {
        return unitJdbcDao.findAutorizeServerUnit(unitClass, regionCode, serverId, page);
    }

    @Override
    public List<Unit> findUnAutorizeServerUnit(Integer[] unitClass, String regionCode, Integer serverId, Pagination page) {
        return unitJdbcDao.findUnAutorizeServerUnit(unitClass, regionCode, serverId, page);
    }

    @Override
    public String findNextUnionCode(String regionCode, int unitClass) {
        if (unitClass == Unit.UNIT_CLASS_EDU)
            return regionCode;
        String unionCode = unitDao.findMaxUnionCode(regionCode + "%", unitClass);
        if (StringUtils.length(unionCode) != 12) {
            return regionCode + StringUtils.leftPad("1", 12 - StringUtils.length(regionCode), "0");
        }
        return NumberUtils.toLong(unionCode) + 1 + "";
    }

    @Override
    public List<Unit> findByUnitClassAndUnionCode(int unitClass, String... unionCodes) {
        return unitDao.findByUnitClassAndUnionCode(unitClass, unionCodes);
    }

    @Override
    public List<Unit> findTopUnitList() {
        return unitJdbcDao.findTopUnitList();
    }

    @Override
    public Unit findByOrganizationCode(String unitCode) {
        return unitDao.findByOrganizationCode(unitCode);
    }

    @Override
    public List<Unit> findUnionCodeSectionList(String unionCode, String section, boolean isedu, boolean isSchool) {
        if (!isedu && !isSchool) {
            return new ArrayList<Unit>();
        }
        return unitJdbcDao.findUnionCodeSectionList(unionCode, section, isedu, isSchool);
    }
    
//    @Override
//    public List<Unit> findByAhUnitIds(String[] unitAhIds) {
//    	return unitDao.findByAhUnitIds(unitAhIds);
//    }
    
    /**
     * 删除和单位相关的基础数据
     * base_unit base_user base_family base_student base_class base_dept base_eduinfo
     * base_teacher base_grade sys_date_info sys_systemini_unit base_unit_extension base_server_extension
     *
     * @param unitId
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteUnit(String unitId) throws PassportException {
        userService.deleteUsersByUnitId(unitId);
        familyService.deleteFamiliesBySchoolId(unitId);
        studentService.deleteStudentsBySchoolId(unitId);
        classService.deleteClazzBySchoolId(unitId);
        deptService.deleteDeptsByUnitId(unitId);
        eduInfoService.deleteEduInfosByUnitId(unitId);
        teacherService.deleteTeachersByUnitId(unitId);
        gradeService.deleteGradesBySchoolId(unitId);
        dateInfoService.deleteDateInfosBySchoolId(unitId);
        unitIniService.deleteUnitInisByUnitId(unitId);
        unitDao.deleteUnitByUnitId(unitId);
    }

    @Override
    public void delete(String id) {
        try {
            deleteUnit(id);
        } catch (PassportException e) {
            throw new RuntimeException("删除Passport账号异常");
        }
    }

    @Override
    public void delete(Unit unit) {
        try {
            deleteUnit(unit.getId());
        } catch (PassportException e) {
            throw new RuntimeException("删除Passport账号异常");
        }
    }

    @Override
    public void deleteAll(Unit[] units) {
        throw new RuntimeException("Not Support");
    }


    /**
     * 新增单位可能会进行以下数据生成
     * 1、单位信息 base_unit
     * 2、学校 base_school
     * 3、edu_info
     * 4、单位管理员账号
     * 5、管理员账号对应的教师信息
     * 6、默认部门
     * 7、默认角色
     * 8、同步增管理员账号到Passport
     * 9、当前单位的默认配置参数
     *
     * @param unit     单位信息
     * @param username
     * @param password
     */
    @Override
    public void addUnit(Unit unit, School school, String username, String password) throws UsernameAlreadyExistsException, PassportException, UnionCodeAlreadyExistsException {
        if (org.apache.commons.lang3.StringUtils.isBlank(unit.getId())) {
            unit.setId(UuidUtils.generateUuid());
        }

        if (UnitTypeCode.NO_EDUCATION_UNIT.equals(unit.getUnitType())) {
            unit.setUnitClass(UnitClassCode.EDUCATION);
        }

        //生成unionCode
        String unionCode = createUnionCode(unit.getParentId(), unit.getUnitClass(), unit.getUnitType(), unit.getRegionCode());

        if (!UnitTypeCode.NO_EDUCATION_UNIT.equals(unit.getUnitType()) && unitDao.existsByUnionCodeAndIsDeleted(unionCode, 0)) {
            throw new UnionCodeAlreadyExistsException("该所在行政区域已存在相应的单位", unionCode);
        }

        unit.setUnionCode(unionCode);

        //根据 regionLevel
        if (UnitClassCode.SCHOOL.equals(unit.getUnitClass())) {
            unit.setRegionLevel(UnitLevelCode.UNIT_LEVEL_SCHOOL);
        } else {
            switch (unit.getUnionCode().length()) {
                case 2:
                    unit.setRegionLevel(UnitLevelCode.UNIT_LEVEL_PROVINCE);
                    break;
                case 4:
                    unit.setRegionLevel(UnitLevelCode.UNIT_LEVEL_CITY);
                    break;
                case 6:
                    unit.setRegionLevel(UnitLevelCode.UNIT_LEVEL_COUNTY);
                    break;
                case 9:
                    unit.setRegionLevel(UnitLevelCode.UNIT_LEVEL_TOWN);
                    break;
                default:
                    break;
            }
        }


        Teacher teacher = initAdminTeacher(unit);
        initUnitAdminAccount(teacher, username, password);

        Dept dept = null;
        if (UnitClassEnum.EDUCATION.getIntegerValue().equals(unit.getUnitClass())) {
            dept = initDept(unit.getId(), DeptConstants.EDUCATION_ADMIN_DEPT_NAME);
            initEduInfo(unit);
        } else {
            dept = initDept(unit.getId(), DeptConstants.SCHOOL_ADMIN_DEPT_NAME);
            school.setId(unit.getId());
            //学年制
            if (school.getSeniorYear() == null || Integer.valueOf(0).equals(school.getSeniorYear())) {
                school.setSeniorYear(3);
            }
            if (school.getJuniorYear() == null || Integer.valueOf(0).equals(school.getJuniorYear())) {
                school.setJuniorYear(3);
            }
            if (school.getGradeYear() == null || Integer.valueOf(0).equals(school.getGradeYear())) {
                school.setGradeYear(6);
            }
            if (school.getInfantYear() == null || Integer.valueOf(0).equals(school.getInfantYear())) {
                school.setInfantYear(3);
            }
            //学段
            if (StringUtils.isBlank(school.getSections())) {
                SchtypeSection section = schtypeSectionService.findBySchoolType(school.getSchoolType());
                school.setSections(section.getSection());
            }
            schoolService.save(school);
        }
        teacher.setDeptId(dept.getId());
        teacher.setDeptName(dept.getDeptName());

        deptService.save(dept);
        roleService.initUnitDefaultRole(unit.getId(), unit.getUnitClass());
        unitIniService.initUnitDefaultOptions(unit.getId());
        save(unit);
    }

    private Dept initDept(String unitId, String deptName) {
        Dept dept = deptDao.getDeptByDeptName(unitId, deptName);
        if (dept == null) {
            dept = new Dept();
            dept.setId(UuidUtils.generateUuid());
            dept.setIsDeleted(0);
            dept.setUnitId(unitId);
            String deptCode = deptService.getAvailableDeptCodeByUnitId(unitId);
            dept.setDeptCode(deptCode);
            dept.setParentId(Constant.GUID_ZERO);
            dept.setDisplayOrder(deptDao.getMaxDisplayOrderByUnitId(unitId).orElse(0) + 1);
            dept.setCreationTime(new Date());
            dept.setModifyTime(dept.getCreationTime());
            dept.setDeptName(deptName);
            dept.setIsDefault(1);
            dept.setInstituteId(Constant.GUID_ZERO);
            dept.setEventSource(EventSourceCode.LOCAL);
        }
        return dept;
    }

    private void initEduInfo(Unit unit) {
        EduInfo eduInfo = new EduInfo();
        eduInfo.setId(unit.getId());
        eduInfo.setIsAutonomy(0);
        eduInfo.setIsFrontier(0);
        eduInfo.setEduCode(unit.getUnionCode());
        eduInfo.setCreationTime(new Date());
        eduInfo.setModifyTime(new Date());
        eduInfo.setIsDeleted(0);
        eduInfoService.save(eduInfo);
    }

    private Teacher initAdminTeacher(Unit unit) {
        Teacher teacher = new Teacher();
        teacher.setId(UuidUtils.generateUuid());
        teacher.setUnitId(unit.getId());
        teacher.setSex(UserSexCode.MALE);
        teacher.setIsDeleted(0);
        teacher.setTeacherName("单位管理员");
        teacher.setEventSource(EventSourceCode.LOCAL);
        teacher.setUnitName(unit.getUnitName());
        teacher.setCreationTime(new Date());
        teacher.setModifyTime(teacher.getCreationTime());
        teacher.setTeacherCode("000001");
        return teacher;
    }

    private void initUnitAdminAccount(Teacher teacher, String username, String password)
            throws UsernameAlreadyExistsException, PassportException {
        User user = userService.findByUsername(username);
        if (user != null) {
            throw new UsernameAlreadyExistsException("用户名已存在", username);
        }
        Account account = new Account();
        if (Evn.isPassport()) {
            Account existAccounts = PassportClientUtils.getPassportClient().queryAccountByUsername(username);
            if (existAccounts != null) {
                throw new UsernameAlreadyExistsException("用户名已存在", username);
            }

            account.setId(UuidUtils.generateUuid());
            account.setUsername(username);
            account.setPassword(PWD.encodeIfNot(password));
            account.setSex(teacher.getSex());
            account.setFixedType(UserOwnerTypeCode.TEACHER);
            account.setRealName(teacher.getTeacherName());
            account.setType(Account.TYPE_TEACHER);
            account.setState(Account.STATE_OK);
            PassportClientUtils.getPassportClient().addAccount(account);
        }
        user = new User();
        user.setId(UuidUtils.generateUuid());
        user.setEventSource(EventSourceCode.LOCAL);
        user.setModifyTime(new Date());
        user.setCreationTime(user.getModifyTime());
        user.setAccountId(account.getId());
        user.setOwnerType(UserOwnerTypeCode.TEACHER);
        user.setUnitId(teacher.getUnitId());
        user.setRegionCode(teacher.getRegionCode());
        user.setIsDeleted(0);
        user.setRealName("单位管理员");
        user.setUserState(UserStateCode.NORMAL);
        user.setOwnerId(teacher.getId());
        user.setUsername(username);
        user.setIconIndex(0);
        user.setUserRole(2);
        user.setUserType(UserTypeCode.UNIT_ADMIN);
        user.setPassword(PWD.encodeIfNot(password));
        user.setEnrollYear(0);
        user.setDisplayOrder(1);
        user.setSequence(account.getSequence());
        userService.save(user);
        teacherService.save(teacher);
    }


    @Override
    public String createUnionCode(String parentUnitId, Integer unitClass, Integer unitType, String regionCode) {
        Unit parentUnit = findOne(parentUnitId);

        if (UnitTypeCode.NO_EDUCATION_UNIT.equals(unitType)) {
            //非教育
            Integer currentUnionCodeLength = UnionCodeLengthCalculator.getUnionCodeLength(parentUnit.getRegionLevel() + 1);
            Optional<String> maxCode = unitDao.getMaxNoEduUnionCode(parentUnitId, currentUnionCodeLength, parentUnit.getUnionCode().length());
            String maxUnionCode = maxCode.orElse(StringUtils.rightPad(parentUnit.getUnionCode(), currentUnionCodeLength, "A"));
            if (maxUnionCode.length() != currentUnionCodeLength) {
                maxUnionCode = parentUnit.getUnionCode() + maxUnionCode;
            }
            //获取增量部分
            String step = maxUnionCode.substring(parentUnit.getUnionCode().length());
            //增量部分是AA 这种
            step = getNextCode(step, step.length() - 1);

            return parentUnit.getUnionCode() + StringUtils.leftPad(step, currentUnionCodeLength - parentUnit.getUnionCode().length(), "0");
        }
        //教育局
        if (UnitClassCode.EDUCATION.equals(unitClass)) {
            /*
             * 当上级单位为区县教育局时，乡镇教育局的生成策略
             * 乡镇教育局的UnionCode长度为9为
             */
            if (6 == parentUnit.getUnionCode().length()) {
                Optional<String> maxCode = unitDao.getMaxTownEduUnionCode(parentUnitId);
                String maxUnionCode = maxCode.orElse(parentUnit.getUnionCode() + "000");
                String serialNumberString = maxUnionCode.substring(parentUnit.getUnionCode().length());
                long serialNumber = NumberUtils.toLong(serialNumberString);
                serialNumber++;
                return parentUnit.getUnionCode() + StringUtils.leftPad(String.valueOf(serialNumber), 3, "0");
            }
            Region region = regionService.findByFullCode(regionCode);
            return region.getRegionCode();
        }
        //学校 获取学校UnionCode最大值
        Optional<String> maxCode = unitDao.getMaxSchoolUnionCode(parentUnitId);
        String maxUnionCode = maxCode.orElseGet(() -> StringUtils.rightPad(parentUnit.getUnionCode(), 12, "0"));
        //取流水号自增
        String serialNumberString = maxUnionCode.substring(parentUnit.getUnionCode().length());
        long serialNumber = NumberUtils.toLong(serialNumberString);
        serialNumber++;
        //补齐12位
        return parentUnit.getUnionCode() + StringUtils.leftPad(String.valueOf(serialNumber), 12 - parentUnit.getUnionCode().length(), "0");
    }

    private String getNextCode(String code, int index) {
        if (code == null || index >= code.length()) {
            return code;
        }
        StringBuffer sb = new StringBuffer(code);
        Character c = sb.charAt(index);
        c = Character.toUpperCase(c);
        if (c == 'Z') {
            sb.setCharAt(index, 'A');
            if (index == 0) {
                return sb.toString();
            } else {
                sb = new StringBuffer(getNextCode(sb.toString(), --index));
            }
        }
        else {
            sb.setCharAt(index, ++c);
        }
        return sb.toString();
    }
}
