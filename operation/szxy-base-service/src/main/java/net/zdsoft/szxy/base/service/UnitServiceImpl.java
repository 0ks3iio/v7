package net.zdsoft.szxy.base.service;

import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.szxy.base.api.UnitRemoteService;
import net.zdsoft.szxy.base.dao.*;
import net.zdsoft.szxy.base.entity.*;
import net.zdsoft.szxy.base.enu.*;
import net.zdsoft.szxy.base.exception.NoRollbackException;
import net.zdsoft.szxy.base.exception.SzxyPassportException;
import net.zdsoft.szxy.base.exception.UnionCodeAlreadyExistsException;
import net.zdsoft.szxy.base.exception.UsernameAlreadyExistsException;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.utils.AssertUtils;
import net.zdsoft.szxy.utils.UuidUtils;
import net.zdsoft.szxy.utils.crypto.PasswordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/3/18 下午2:52
 */
@Service("unitRemoteService")
public class UnitServiceImpl implements UnitRemoteService {

    @Resource
    private UnitDao unitDao;
    @Resource
    private ServerExtensionDao serverExtensionDao;
    @Resource
    private UserDao userDao;
    @Resource
    private DeptDao deptDao;
    @Resource
    private TeacherDao teacherDao;
    @Resource
    private ClassDao classDao;
    @Resource
    private GradeDao gradeDao;
    @Resource
    private FamilyDao familyDao;
    @Resource
    private SchoolTypeSectionDao schoolTypeSectionDao;
    @Resource
    private SchoolDao schoolDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private RolePermDao rolePermDao;
    @Resource
    private RegionDao regionDao;
    @Resource
    private PassportClient passportClient;
    @Resource
    private StudentDao studentDao;

    @Override
    public Unit getUnitById(String unitId) {
        return unitDao.getUnitById(unitId);
    }

    @Override
    public Boolean existsUnderlingUnits(String unitId) {
        return unitDao.countUnderlingUnits(unitId) > 0;
    }

    @Override
    public List<Unit> getUnderlingUnits(String unitId) {
        AssertUtils.notNull(unitId, "单位ID不能为空");
        return unitDao.getUnderlingUnits(unitId);
    }

    @Override
    public List<Unit> getUnitsByIds(String[] unitIds) {
        AssertUtils.hasElements(unitIds, "单位ID不能为空");
        return unitDao.getUnitsByUnitIds(unitIds);
    }

    @Transactional(rollbackFor = NoRollbackException.class)
    @Record(type = RecordType.Call)
    @Override
    public Map<String, String> getUnitNameMap(String[] unitIds) {
        AssertUtils.hasElements(unitIds, "unitIds can't empty");
        return unitDao.getUnitsByUnitIds(unitIds).stream().collect(Collectors.toMap(Unit::getId, Unit::getUnitName));
    }

    @Override
    public List<Unit> getTopUnits() {
        return unitDao.getTopUnits();
    }

    @Override
    public Unit getUnitByUnitName(String unitName) {
        return unitDao.getUnitByUnitName(unitName).orElse(null);
    }

    @Record(type = RecordType.Call)
    @Override
    public Boolean existsUnitName(String unitName) {
        AssertUtils.notNull(unitName, "unitName 不能为空");
        Unit unit = new Unit();
        unit.setUnitName(unitName);
        unit.setIsDeleted(DeleteCode.NOT_DELETED);
        return unitDao.exists(Example.of(unit, ExampleMatcher.matchingAll().withIgnoreNullValues()));
    }

    @Record(type = RecordType.Call)
    @Override
    public Boolean existsByUnionCode(String unionCode) {
        AssertUtils.notNull(unionCode, "unionCode can't null");
        Unit unit = new Unit();
        unit.setIsDeleted(DeleteCode.NOT_DELETED);
        unit.setUnionCode(unionCode);
        return unitDao.exists(Example.of(unit, ExampleMatcher.matchingAll().withIgnoreNullValues()));
    }

    @Record(type = RecordType.Call)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void addUnit(Unit unit, School school, String username, String password)
            throws UnionCodeAlreadyExistsException, UsernameAlreadyExistsException, SzxyPassportException {
        //预处理单位数据，id unitClass unionCode regionLevel 等
        preInitUnit(unit);


        Teacher teacher = initAdminTeacher(unit);
        try {
            initUnitAdminAccount(teacher, username, password);
        } catch (PassportException e) {
            throw new SzxyPassportException("调用Passport处理单位管理员出错");
        }

        Dept dept = null;
        if (UnitClassCode.EDUCATION.equals(unit.getUnitClass())) {
            dept = initDept(unit.getId(), DeptConstants.EDUCATION_ADMIN_DEPT_NAME);
            //initEduInfo(unit);
        } else {
            dept = initDept(unit.getId(), DeptConstants.SCHOOL_ADMIN_DEPT_NAME);
            //设置学年学制、学段信息
            school.setId(unit.getId());
            initSchoolInfo(school);
        }

        teacher.setDeptId(dept.getId());
        deptDao.save(dept);
        initUnitDefaultRole(unit.getId(), unit.getUnitClass());
        //TODO 单位默认参数不在base库中，新增单位暂时不处理这个数据
        unitDao.save(unit);
    }

    private void preInitUnit(Unit unit) throws UnionCodeAlreadyExistsException {
        if (StringUtils.isBlank(unit.getId())) {
            unit.setId(UuidUtils.generateUuid());
        }
        if (UnitTypeCode.NO_EDUCATION.equals(unit.getUnitType())) {
            unit.setUnitClass(UnitClassCode.EDUCATION);
        }

        //生成unionCode
        String unionCode = createUnionCode(unit.getParentId(), unit.getUnitType(), unit.getRegionCode());
        /**
         * 判断是否为教育局单位
         */
        boolean flag = UnitTypeCode.TOP_EDUCATION.equals(unit.getUnitType())||UnitTypeCode.UNDERLING_EDUCATION.equals(unit.getUnitType());
        if ( flag && unitDao.existsByUnionCodeAndIsDeleted(unionCode, 0)) {
            throw new UnionCodeAlreadyExistsException("该所在行政区域已存在相应的单位", unionCode);
        }
        unit.setUnionCode(unionCode);

        //根据 regionLevel
        if (UnitClassCode.SCHOOL.equals(unit.getUnitClass())) {
            unit.setRegionLevel(UnitRegionLevel.REGION_SCHOOL);
        } else {
            switch (unit.getUnionCode().length()) {
                case 1:
                    unit.setRegionLevel(UnitRegionLevel.REGION_COUNTRY);
                    break;
                case 2:
                    unit.setRegionLevel(UnitRegionLevel.REGION_PROVINCE);
                    break;
                case 4:
                    unit.setRegionLevel(UnitRegionLevel.REGION_CITY);
                    break;
                case 6:
                    unit.setRegionLevel(UnitRegionLevel.REGION_COUNTY);
                    break;
                case 9:
                    unit.setRegionLevel(UnitRegionLevel.REGION_TOWN);
                    break;
                default:
                    break;
            }
        }
    }

    private void initSchoolInfo(School school) {
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
            Optional<SchoolTypeSection> section = schoolTypeSectionDao.getSectionBySchoolType(school.getSchoolType());

            school.setSections(section.orElseThrow(() -> new IllegalArgumentException("无法根据学校类别码获取学段数据")).getSection());
        }
        schoolDao.save(school);
    }

    private Teacher initAdminTeacher(Unit unit) {
        Teacher teacher = new Teacher();
        teacher.setId(UuidUtils.generateUuid());
        teacher.setUnitId(unit.getId());
        teacher.setSex(UserSexCode.MALE);
        teacher.setIsDeleted(0);
        teacher.setTeacherName("单位管理员");
        teacher.setEventSource(EventSourceCode.LOCAL);
        teacher.setCreationTime(new Date());
        teacher.setModifyTime(teacher.getCreationTime());
        teacher.setTeacherCode("000001");
        return teacher;
    }

    private void initUnitAdminAccount(Teacher teacher, String username, String password)
            throws UsernameAlreadyExistsException, PassportException {
        User user = userDao.getUserByUsername(username);
        if (user != null) {
            throw new UsernameAlreadyExistsException("用户名已存在", username);
        }
        //同步数据到passport
        Account account = new Account();
        Account existAccounts = passportClient.queryAccountByUsername(username);
        if (existAccounts != null) {
            throw new UsernameAlreadyExistsException("用户名已存在", username);
        }

        account.setId(UuidUtils.generateUuid());
        account.setUsername(username);
        account.setPassword(PasswordUtils.encodeIfNot(password));
        account.setSex(teacher.getSex());
        account.setFixedType(UserOwnerTypeCode.TEACHER);
        account.setRealName(teacher.getTeacherName());
        account.setType(Account.TYPE_TEACHER);
        account.setState(Account.STATE_OK);
        passportClient.addAccount(account);

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
        user.setUserState(UserStateCode.USER_MARK_NORMAL);
        user.setOwnerId(teacher.getId());
        user.setUsername(username);
        user.setIconIndex(0);
        user.setUserRole(2);
        user.setUserType(UserTypeCode.USER_TYPE_UNIT_ADMIN);
        user.setPassword(PasswordUtils.encodeIfNot(password));
        user.setEnrollYear(0);
        user.setDisplayOrder(1);
        user.setSequence(account.getSequence());
        userDao.save(user);
        teacherDao.save(teacher);
    }

    private Dept initDept(String unitId, String deptName) {
        Dept dept = deptDao.getDeptByDeptName(unitId, deptName);
        if (dept == null) {
            dept = new Dept();
            dept.setId(UuidUtils.generateUuid());
            dept.setIsDeleted(0);
            dept.setUnitId(unitId);
            String deptCode = deptDao.getAvailableDeptCodeByUnitId(unitId);
            dept.setDeptCode(deptCode);
            dept.setParentId(ID.ZERO_32);
            dept.setDisplayOrder(deptDao.getMaxDisplayOrderByUnitId(unitId).orElse(0) + 1);
            dept.setCreationTime(new Date());
            dept.setModifyTime(dept.getCreationTime());
            dept.setDeptName(deptName);
            dept.setIsDefault(1);
            dept.setInstituteId(ID.ZERO_32);
            dept.setEventSource(EventSourceCode.LOCAL);
        }
        return dept;
    }

    //private void initEduInfo(Unit unit) {
    //    EduInfo eduInfo = new EduInfo();
    //    eduInfo.setId(unit.getId());
    //    eduInfo.setIsAutonomy(0);
    //    eduInfo.setIsFrontier(0);
    //    eduInfo.setEduCode(unit.getUnionCode());
    //    eduInfo.setCreationTime(new Date());
    //    eduInfo.setModifyTime(new Date());
    //    eduInfo.setIsDeleted(DeleteCode.NOT_DELETED);
    //    eduInfoDao.save(eduInfo);
    //}

    private void initUnitDefaultRole(String unitId, Integer unitClass) {
        List<Role> roles = roleDao.getRolesByUnitId(UnitClassCode.EDUCATION.equals(unitClass) ? ID.ZERO_32 : ID.ZERO_31_ONE_1);
        List<RolePerm> rolePerms = rolePermDao.getRolePermsByRoleId(roles.stream().map(Role::getId).toArray(String[]::new));
        Map<String, Role> roleMap = new HashMap<>(roles.size());

        List<Role> defaultUnitReles = new ArrayList<>(roles.size());
        List<RolePerm> defaultUnitRolePerms = new ArrayList<>(rolePerms.size());
        for (Role role : roles) {
            Role defaultRole = new Role();
            defaultRole.setRoleType(role.getRoleType());
            defaultRole.setUnitId(unitId);
            defaultRole.setDescription(role.getDescription());
            defaultRole.setIsActive(role.getIsActive());
            defaultRole.setName(role.getName());
            defaultRole.setIdentifier(role.getIdentifier());
            defaultRole.setSubSystem(role.getSubSystem());
            defaultRole.setDynamicDataSet(role.getDynamicDataSet());
            defaultRole.setModId(role.getModId());
            defaultRole.setOperId(role.getOperId());
            defaultRole.setRefId(role.getRefId());
            roleMap.put(role.getId(), defaultRole);
            defaultRole.setId(UuidUtils.generateUuid());
            defaultUnitReles.add(defaultRole);
        }

        for (RolePerm rolePerm : rolePerms) {
            RolePerm defaultRolePerm = new RolePerm();
            defaultRolePerm.setModelId(rolePerm.getModelId());
            defaultRolePerm.setRoleId(roleMap.get(rolePerm.getRoleId()).getId());
            defaultRolePerm.setType(rolePerm.getType());
            defaultRolePerm.setOperIds(rolePerm.getOperIds());
            defaultRolePerm.setId(UuidUtils.generateUuid());
            defaultUnitRolePerms.add(defaultRolePerm);
        }

        roleDao.saveAll(defaultUnitReles);
        rolePermDao.saveAll(defaultUnitRolePerms);
    }

    @Record(type = RecordType.Call)
    @Override
    public String createUnionCode(String parentUnitId, Integer unitType, String regionCode) {
        Unit parentUnit = unitDao.getUnitById(parentUnitId);
        AssertUtils.notNull(parentUnit, "当前单位的上级单位为空，无法创建UnionCode");

        //非教育
        if (UnitTypeCode.NO_EDUCATION.equals(unitType)) {
            return createNoEduUnionCode(parentUnit);
        }

        //判断是否是教育局
        boolean isEdu = UnitTypeCode.NO_EDUCATION.equals(unitType)
                || UnitTypeCode.TOP_EDUCATION.equals(unitType)
                || UnitTypeCode.UNDERLING_EDUCATION.equals(unitType);
        if (isEdu) {
            return createEduUnionCode(parentUnit, regionCode);
        }
        //默认作为学校处理

        return createSchoolUnionCode(parentUnit);
    }

    private String createNoEduUnionCode(Unit parentUnit) {
        Integer currentUnionCodeLength = UnionCodeLengthCalculator.getUnionCodeLength(parentUnit.getRegionLevel() + 1);
        Optional<String> maxCode = unitDao.getMaxNoEduUnionCode(parentUnit.getId(), currentUnionCodeLength, parentUnit.getUnionCode().length());
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

    private String createEduUnionCode(Unit parentUnit, String regionCode) {
        /*
         * 当上级单位为区县教育局时，乡镇教育局的生成策略
         * 乡镇教育局的UnionCode长度为9为
         */
        if (6 == parentUnit.getUnionCode().length()) {
            Optional<String> maxCode = unitDao.getMaxTownEduUnionCode(parentUnit.getId());
            String maxUnionCode = maxCode.orElse(parentUnit.getUnionCode() + "000");
            String serialNumberString = maxUnionCode.substring(parentUnit.getUnionCode().length());
            long serialNumber = NumberUtils.toLong(serialNumberString);
            serialNumber++;
            return parentUnit.getUnionCode() + StringUtils.leftPad(String.valueOf(serialNumber), 3, "0");
        }
        Region region = regionDao.getRegionByFullCode(regionCode, RegionTypeCode.TYPE_1);
        return region.getRegionCode();
    }

    private String createSchoolUnionCode(Unit parentUnit) {
        //学校 获取学校UnionCode最大值
        Optional<String> maxCode = unitDao.getMaxSchoolUnionCode(parentUnit.getId());
        String maxUnionCode = maxCode.orElseGet(() -> StringUtils.rightPad(parentUnit.getUnionCode(), 12, "0"));
        //取流水号自增
        String serialNumberString = maxUnionCode.substring(parentUnit.getUnionCode().length());
        long serialNumber = NumberUtils.toLong(serialNumberString);
        serialNumber++;
        //补齐12位
        return parentUnit.getUnionCode() + StringUtils.leftPad(String.valueOf(serialNumber), 12 - parentUnit.getUnionCode().length(), "0");
    }

    @Record(type = RecordType.Call)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteByUnitId(String unitId) {
        userDao.deleteUsersByUnitId(unitId);
        familyDao.deleteFamiliesBySchoolId(unitId);
        studentDao.deleteStudentsBySchoolId(unitId);
        classDao.deleteClazzBySchoolId(unitId);
        deptDao.deleteDeptsByUnitId(unitId);
        teacherDao.deleteTeachersByUnitId(unitId);
        gradeDao.deleteGradesBySchoolId(unitId);
        unitDao.deleteUnitByUnitId(unitId);
        serverExtensionDao.deleteByUnitId(unitId);
    }

    @Record(type = RecordType.Call)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateUnit(String unitName, String unitId) {
        unitDao.updateUnitName(unitName, unitId);
    }
}
