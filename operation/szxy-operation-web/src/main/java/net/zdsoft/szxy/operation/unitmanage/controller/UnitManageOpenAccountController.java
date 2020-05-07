package net.zdsoft.szxy.operation.unitmanage.controller;

import com.alibaba.fastjson.JSON;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.zdsoft.szxy.base.api.*;
import net.zdsoft.szxy.base.dto.OpenAccount;
import net.zdsoft.szxy.base.entity.*;
import net.zdsoft.szxy.base.enu.FamilyRelationCode;
import net.zdsoft.szxy.base.enu.UserOwnerTypeCode;
import net.zdsoft.szxy.base.exception.SzxyPassportException;
import net.zdsoft.szxy.base.exception.UsernameAlreadyExistsException;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.enums.SysOptionCode;
import net.zdsoft.szxy.operation.usermanage.controller.vo.ClazzVo;
import net.zdsoft.szxy.operation.unitmanage.dto.OpenAccountInfo;
import net.zdsoft.szxy.operation.utils.AccountGenerator;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author yangkj
 * @since 2019/4/17 10:40
 */
@RestController
@RequestMapping("operation/user/manage")
public class UnitManageOpenAccountController {

    private static final String USER_DEFAULT_PASSWORD = "123456";

    private Logger logger = LoggerFactory.getLogger(UnitManageOpenAccountController.class);

    @Resource
    private UserRemoteService userRemoteService;
    @Resource
    private StudentRemoteService studentRemoteService;
    @Resource
    private SystemIniRemoteService systemIniRemoteService;
    @Resource
    private ClassRemoteService classRemoteService;
    @Resource
    private SchoolRemoteService schoolRemoteService;
    @Resource
    private FamilyRemoteService familyRemoteService;
    @Resource
    private UnitRemoteService unitRemoteService;

    private static Pattern usernamePattern = Pattern.compile("[a-zA-Z0-9_]{4,20}");

    @Record(type = RecordType.URL)
    @GetMapping("/username/checked")
    public Response doCheckUsername(@RequestParam("username") String username) {

        if (!usernamePattern.matcher(username).matches()) {
            return Response.error().message("用户名【%s】不合法，只能包含字母和数字（4-20个字符）", username).build();
        }

        User user = userRemoteService.getUserByUsername(username);
        logger.debug("Check exists username is {}", username);
        if (user != null) {
            return Response.error().message("用户名【%s】已存在", new String[]{username}).build();
        }
        return Response.ok().build();
    }

    @Record(type = RecordType.URL)
    @PostMapping("/student/username")
    public Response doGenerateStudentUsername(String[] studentIds, String classId) {
        //查询学生信息
        List<Student> students = studentRemoteService.getStudentsById(studentIds);
        //查询学生账号信息
        List<User> users = userRemoteService.getUsersByOwnerIds(studentIds);
        Map<String, User> existsUserMap = users.stream().collect(Collectors.toMap(User::getOwnerId, Function.identity()));
        String usernameRule = systemIniRemoteService.getRawValueByIniId(SysOptionCode.KEY_STUDENT_FAMILY_USERNAME_RULE);
        //家长信息
        Map<String, Family> familyMap = null;
        if (usernameRule.contains("fMobile")) {
            List<Family> families = familyRemoteService.getFamiliesByStudentId(studentIds);
            familyMap = families.stream().collect(Collectors.toMap(Family::getStudentId, Function.identity()));
        }

        Map<String, String> usernameMap = new HashMap<>(studentIds.length);
        try {
            Clazz clazz = classRemoteService.getClazzById(classId);
            School school = schoolRemoteService.getSchoolById(clazz.getSchoolId());
            for (Student student : students) {
                User user = existsUserMap.get(student.getId());
                if (user == null) {
                    String fatherMobile = null;
                    if (familyMap != null) {
                        Family family = familyMap.get(student.getId());
                        if (family != null && FamilyRelationCode.FATHER.equals(family.getRelation())) {
                            fatherMobile = family.getMobilePhone();
                        }
                    }
                    AccountGenerator.AccountInfo accountInfo = createAccountInfo(student, clazz, school, fatherMobile, null);
                    accountInfo.setOwnerType(UserOwnerTypeCode.STUDENT);
                    String username = AccountGenerator.generateAccount(usernameRule, accountInfo);
                    usernameMap.put(student.getId(), username);
                }
            }
        } catch (AccountGenerator.IllegalAccountRuleException e) {
            logger.error("无法解析的学生家长账号规则 {}", usernameRule);
            return Response.error().message("系统配置的账号规则不合法").build();
        }

        return Response.ok().data("usernames", usernameMap).build();
    }

    private AccountGenerator.AccountInfo createAccountInfo(Student student, Clazz clazz, School school, String mobile, String familyType) {
        AccountGenerator.AccountInfo accountInfo = new AccountGenerator.AccountInfo();
        accountInfo.setStuCode(student.getStudentCode());
        accountInfo.setClsCode(clazz.getClassCode());
        accountInfo.setOwnerType(UserOwnerTypeCode.STUDENT);
        //容错，防止其他系统操作单位没有同步school的数据
        String serialNumber = school.getSerialNumber();
        if (StringUtils.isBlank(serialNumber)) {
            serialNumber = unitRemoteService.getUnitById(school.getId()).getSerialNumber();
        }
        accountInfo.setEtohSchoolId(serialNumber);
        if (StringUtils.isNotBlank(student.getIdentityCard())) {
            accountInfo.setIdentitycard(student.getIdentityCard());
        } else {
            //TODO 生成临时身份证
        }
        accountInfo.setFamilyType(familyType);
        accountInfo.setMaxcode(maxCode(clazz.getClassCode(), student.getSchoolId()).get());
        accountInfo.setFMobile(mobile);
        accountInfo.setUnitiveCode(student.getUnitiveCode());
        accountInfo.setUserNamePrefix(systemIniRemoteService.getRawValueByIniId(SysOptionCode.KEY_USERNAME_PREFIX));
        accountInfo.setFirstSpell(firstSpell(student.getStudentName()));
        return accountInfo;
    }

    private String firstSpell(String realName) {
        return realName.chars().map(e -> PinyinHelper.toHanyuPinyinStringArray((char) e)[0].charAt(0)).toString();
    }

    private Supplier<String> maxCode(String classCode, String unitId) {
        return new Supplier<String>() {
            private Integer maxCode;

            @Override
            public String get() {
                if (maxCode == null) {
                    maxCode = userRemoteService.getUsernameMaxCode(classCode, unitId);
                    if (maxCode == null) {
                        maxCode = 0;
                    }
                }
                return StringUtils.leftPad(String.valueOf(maxCode + 1), 2, "0");
            }
        };
    }

    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/{gradeId}/classList"
    )
    public Response doGetClassList(@PathVariable("gradeId") String gradeId) {
        List<Clazz> clazzList = classRemoteService.getClazzesByGradeId(gradeId);
        return Response.ok().data("classList", clazzList.stream().map(e -> {
            ClazzVo clazzVo = new ClazzVo();
            clazzVo.setClassName(e.getClassName());
            clazzVo.setId(e.getId());
            return clazzVo;
        }).collect(Collectors.toList())).build();
    }

    @Record(type = RecordType.URL)
    @PostMapping("/family/username")
    public Response doGenerateFamilyUsername(@RequestParam("familyIds[]") String[] familyIds,
                                             String classId) {
        //生成家长账号
        List<Family> families = familyRemoteService.getFamiliesById(familyIds);
        List<User> familyUsers = userRemoteService.getUsersByOwnerIds(familyIds);

        Map<String, User> familyUserMap =
                familyUsers.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        String usernameRule = systemIniRemoteService.getRawValueByIniId(SysOptionCode.KEY_STUDENT_FAMILY_USERNAME_RULE);

        List<Student> students =
                studentRemoteService.getStudentsById(families.stream().map(Family::getStudentId).toArray(String[]::new));
        Map<String, Student> studentMap =
                students.stream().collect(Collectors.toMap(Student::getId, Function.identity()));

        Clazz clazz = classRemoteService.getClazzById(classId);
        School school = schoolRemoteService.getSchoolById(clazz.getSchoolId());

        Map<String, String> familyUsername = new HashMap<>();
        try {
            for (Family family : families) {
                User familyUser = familyUserMap.get(family.getId());
                if (familyUser == null) {
                    Student student = studentMap.get(family.getStudentId());
                    AccountGenerator.AccountInfo accountInfo = createAccountInfo(student, clazz, school, family.getMobilePhone(), family.getRelation());
                    accountInfo.setOwnerType(UserOwnerTypeCode.STUDENT);
                    familyUsername.put(family.getId(), AccountGenerator.generateAccount(usernameRule, accountInfo));
                }
            }
        } catch (AccountGenerator.IllegalAccountRuleException e) {
            return Response.error().message("系统家长规则异常，无法生成账号").build();
        }

        return Response.ok().data("usernames", familyUsername).build();
    }

    /**
     * 开通账号
     */
    @Record(type = RecordType.URL)
    @PostMapping(
            value = "/open/account"
    )
    public Response doOpenAccount(@RequestBody List<OpenAccountInfo> openAccountInfos) {
        try {
            if (openAccountInfos.stream().noneMatch(it -> StringUtils.isNotBlank(it.getUsername()))) {
                return Response.error().message("请生成账号信息").build();
            }
            for (OpenAccountInfo openAccountInfo : openAccountInfos) {
                if (!usernamePattern.matcher(openAccountInfo.getUsername()).matches()) {
                    return Response.error().message("用户名【%s】不合法，只能包含字母和数字（4-20个字符）",
                            openAccountInfo.getUsername()).build();
                }
            }
            userRemoteService.openAccount(
                    openAccountInfos.stream().filter(it->StringUtils.isNotBlank(it.getUsername())).map(e->{
                        OpenAccount openAccountInfo = new OpenAccount();
                        openAccountInfo.setId(e.getId());
                        openAccountInfo.setOwnerType(e.getOwnerType());
                        openAccountInfo.setPassword(USER_DEFAULT_PASSWORD);
                        openAccountInfo.setUsername(e.getUsername());
                        openAccountInfo.setUnitId(e.getUnitId());
                        return openAccountInfo;
                    }).collect(Collectors.toList())
            );
        } catch (UsernameAlreadyExistsException e) {
            return Response.error().message(String.format("用户名【%s】已存在", e.getUsername())).build();
        } catch (SzxyPassportException e) {
            return Response.error().message(e.getMessage()).build();
        }
        return Response.ok().message("账号开通成功").build();
    }
}
