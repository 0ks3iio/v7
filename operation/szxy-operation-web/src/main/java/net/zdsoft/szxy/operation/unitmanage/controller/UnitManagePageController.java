package net.zdsoft.szxy.operation.unitmanage.controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.zdsoft.szxy.base.api.*;
import net.zdsoft.szxy.base.entity.*;
import net.zdsoft.szxy.base.enu.UnitClassCode;
import net.zdsoft.szxy.base.enu.UnitTypeCode;
import net.zdsoft.szxy.base.exception.UnionCodeAlreadyExistsException;
import net.zdsoft.szxy.base.exception.UsernameAlreadyExistsException;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.enums.MicroCode;
import net.zdsoft.szxy.operation.enums.SysOptionCode;
import net.zdsoft.szxy.operation.security.CurrentUser;
import net.zdsoft.szxy.operation.servermanage.dto.ServerExtensionDto;
import net.zdsoft.szxy.operation.servermanage.service.OpServerExService;
import net.zdsoft.szxy.operation.unitmanage.Router;
import net.zdsoft.szxy.operation.unitmanage.UnitOperateCode;
import net.zdsoft.szxy.operation.unitmanage.dto.EnableServerDto;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitAddDto;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitDto;
import net.zdsoft.szxy.operation.unitmanage.entity.OpUnitPrincipal;
import net.zdsoft.szxy.operation.unitmanage.service.IllegalRenewalTimeException;
import net.zdsoft.szxy.operation.unitmanage.service.NoUnitExtensionException;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitPrincipalService;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitService;
import net.zdsoft.szxy.operation.unitmanage.vo.UnitTypeVo;
import net.zdsoft.szxy.operation.unitmanage.vo.UsingNatureVo;
import net.zdsoft.szxy.operation.unitmanage.dto.StudentAndFamilyAccount;
import net.zdsoft.szxy.operation.unitmanage.dto.StudentAndFamilyAccountQuery;
import net.zdsoft.szxy.operation.unitmanage.service.UserManageService;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author ZhuJinY
 * @since 2019年1月17日下午6:51:11
 */
@Controller
@RequestMapping(
        value = Router.UNIT_MANAGE_PAGE
)
public class UnitManagePageController {
    @Resource
    private OpUnitService opUnitService;

    @Resource
    private UnitRemoteService unitRemoteService;

    @Resource
    private SchoolRemoteService schoolRemoteService;

    @Resource
    private MicrocodeDetailRemoteService microcodeDetailRemoteService;

    @Autowired
    private OpServerExService opServerExService;

    @Resource
    private UserRemoteService userRemoteService;

    @Resource
    private OpUnitPrincipalService opUnitPrincipalService;

    @Resource
    private GradeRemoteService gradeRemoteService;

    @Resource
    private ClassRemoteService classRemoteService;

    @Resource
    private SystemIniRemoteService systemIniRemoteService;

    @Resource
    private UserManageService userManageService;

    private Logger logger = LoggerFactory.getLogger(UnitManagePageController.class);

    /**
     * 判断unionCode是否存在(一个地区不能存在2个教育局)
     */
    @Secured(UnitOperateCode.UNIT_ADD)
    @Record(type = RecordType.URL)
    @RequestMapping("/isUniqueUnicode")
    @ResponseBody
    public Response isUniqueUnicode(String parentUnitId, Integer unitType, String fullCode) {
        String unionCode = unitRemoteService.createUnionCode(parentUnitId, unitType, fullCode);
        Boolean existsUnionCode = unitRemoteService.existsByUnionCode(unionCode);
        if (Boolean.TRUE.equals(existsUnionCode)) {
            return Response.error().data("valid", false).message("同一地区不能有两个教育局").build();
        }
        return Response.ok().data("valid", true).build();
    }

    /**
     * 验证用户名是否重复
     *
     * @param username
     * @return
     */
    @Secured(UnitOperateCode.UNIT_ADD)
    @RequestMapping("/isUniqueUsername")
    @ResponseBody
    @Record(type = RecordType.URL)
    public Response isUniqueUsername(String username) {
        Boolean flag = userRemoteService.existsUsername(username);
        if (flag) {
            return Response.error().data("valid", false).build();
        }
        return Response.ok().data("valid", true).build();
    }

    /**
     * 验证单位名称是否重复
     */
    @Secured(UnitOperateCode.UNIT_ADD)
    @Record(type = RecordType.URL)
    @RequestMapping("/checkUnitName")
    @ResponseBody
    public Response checkUnitName(String unitName, String unitId) {
        if (StringUtils.isBlank(unitName)) {
            return Response.error().data("valid", false).message("单位名称不能为空").build();
        }
        Unit unitToCheckName = unitRemoteService.getUnitById(unitId);
        if (unitToCheckName != null && unitToCheckName.getUnitName().equals(unitName)) {
            return Response.ok().data("valid", true).build();
        }
        Boolean exists = unitRemoteService.existsUnitName(unitName);
        return Response.ok().data("valid", Boolean.FALSE.equals(exists)).build();
    }

    /**
     * 并将单位名转成用户名 返回用户名
     */
    @Secured(UnitOperateCode.UNIT_ADD)
    @Record(type = RecordType.URL)
    @RequestMapping("/turnUnitNameToPinYin")
    @ResponseBody
    public Response turnToPinYin(String unitName) {
        String username = toHanyuPinyin(unitName, null, true);
        username += "_admin";
        int i = 1;
        String x = username;
        while (true) {
            Boolean existsUsername = userRemoteService.existsUsername(username);
            if (Boolean.FALSE.equals(existsUsername)) {
                break;
            } else {
                username = x + '0' + (i++);
            }
        }
        return Response.ok().data("username", username).build();
    }

    /**
     * 跳转到授权页面
     */
    @Secured(UnitOperateCode.UNIT_AUTH)
    @RequestMapping("/authorize")
    public String turnToAuthorize(Model model, String unitId) {
        List<EnableServerDto> allSystem = opServerExService.getAllEnableServers();
        List<ServerExtensionDto> authorizeSystem = opServerExService.findSystemByUnitId(unitId);
        model.addAttribute("allSystem", allSystem);
        model.addAttribute("authorizeSystem", authorizeSystem);
        return "/unitmanage/unitmanage-authorization.ftl";
    }

    /**
     * 跳转到新增页面
     */
    @Secured({UnitOperateCode.UNIT_ADD, UnitOperateCode.UNIT_EDIT, UnitOperateCode.UNIT_NATURE_CHANGE})
    @Record(type = RecordType.URL)
    @RequestMapping(value = {
            "/insert", "/edit", "/formal", "/maintain"
    })
    public String turnToInsert(@RequestBody(required = false) Unit unit,
                               @RequestBody(required = false) School school,
                               @RequestBody(required = false) UnitExtension unitExtension,
                               Model model, String unitId, boolean flag) {
        //获取微代码
        initAddPageMicroCode(model);
        model.addAttribute("titleName", "新增单位");
        if (StringUtils.isNotBlank(unitId)) {
            try {
                model.addAttribute("titleName", "编辑");
                unit = unitRemoteService.getUnitById(unitId);
                if (unit == null) {
                    throw new RuntimeException("该单位已经被删除,请刷新页面");
                }
                school = schoolRemoteService.getSchoolById(unitId);
                if (school == null) {
                    school = new School();
                }
                unitExtension = opUnitService.findByUnitId(unitId);
            } catch (NoUnitExtensionException e) {
                model.addAttribute("titleName", "维护单位");
                unitExtension = new UnitExtension();
            }
        } else {
            unit = new Unit();
            school = new School();
            unitExtension = new UnitExtension();
        }
        if (flag) {
            model.addAttribute("titleName", "试用转正式");
        }

        /**
         * 单位联系人回显
         */
        List<OpUnitPrincipal> unitPrincipals = opUnitPrincipalService.findUnitPrincipalsByUnitId(unitId);
        model.addAttribute("unitPrincipalList",unitPrincipals);
        model.addAttribute("unit", unit);
        model.addAttribute("school", school);
        model.addAttribute("unitExtension", unitExtension);
        return "/unitmanage/unitmanage-insert.ftl";
    }

    private void initAddPageMicroCode(Model model) {
        List<MicrocodeDetail> unitTypeList = microcodeDetailRemoteService.getMicrocodesByMicrocode(MicroCode.KEY_UNIT_TYPE);
        model.addAttribute("unitTypeList", unitTypeList);
        List<MicrocodeDetail> schoolTypeList = microcodeDetailRemoteService.getMicrocodesByMicrocode(MicroCode.KEY_SCHOOL_TYPE);
        model.addAttribute("schoolTypeList", schoolTypeList);
        List<MicrocodeDetail> runSchoolTypeList = microcodeDetailRemoteService.getMicrocodesByMicrocode(MicroCode.KEY_RUN_SCHOOL_TYPE);
        model.addAttribute("runSchoolTypeList", runSchoolTypeList);
        List<MicrocodeDetail> emphasesList = microcodeDetailRemoteService.getMicrocodesByMicrocode(MicroCode.KEY_SCHOOL_EMPHASES);
        model.addAttribute("emphasesList", emphasesList);
        List<Unit> parentUnitList = opUnitService.findByUnitClassAndUnitType(UnitClassCode.EDUCATION, UnitTypeCode.NO_EDUCATION);
        model.addAttribute("parentUnitList", parentUnitList);
    }


    @RequestMapping("/index")
    public String execute(Model model) {
        model.addAttribute("unitTypeList", UnitTypeVo.getUnitTypes());
        model.addAttribute("usingNatureList", UsingNatureVo.getUsingNatureVo());
        return "/unitmanage/unitmanage-index.ftl";
    }

    /**
     * 分页进行单位页面展示
     */
    @Record(type = RecordType.URL)
    @RequestMapping("/unitList")
    public String findUnitPageByParentId(@RequestParam(value = "parentId", required = false) String parentId,
                                         String unitType,
                                         String usingNature, Pageable pageable, Model model) {
        try {
            Page<UnitDto> list = opUnitService.findPageByParentId(parentId, unitType, usingNature, pageable);
            model.addAttribute("pages", list);
            return "/unitmanage/unitmanage-unitAccountList.ftl";

        } catch (Exception e) {
            logger.error("分页查询失败");
            return "/unitmanage/unitmanage-index.ftl";
        }
    }

    /**
     * 新增单位
     */
    @Secured(UnitOperateCode.UNIT_ADD)
    @Record(type = RecordType.URL)
    @RequestMapping("/saveAllUnit")
    @ResponseBody
    public Response saveAllUnit(UnitAddDto unitAddDto, @CurrentUser("id") String operatorId) {
        try {
            opUnitService.saveUnit(unitAddDto, operatorId);
        } catch (UsernameAlreadyExistsException e) {
            return Response.error().message("用户名已存在").build();
        } catch (UnionCodeAlreadyExistsException e) {
            return Response.error().message("同一地区不能有两个教育局").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error().message("新增时发现未知错误，请检查").build();
        }
        return Response.ok().message("新增成功").build();
    }


    /**
     * 通过unitId查询3张表
     */
    @Record(type = RecordType.URL)
    @RequestMapping("/findUnitByUnitId")
    @ResponseBody
    public Response findUnitByUnitId(String unitId) {
        Unit unit = null;
        School school = null;
        UnitExtension unitEx = null;
        try {
            unit = unitRemoteService.getUnitById(unitId);
            school = schoolRemoteService.getSchoolById(unitId);
            unitEx = opUnitService.findByUnitId(unitId);
            if (unitEx == null) {
                throw new NoUnitExtensionException(String.format("没有指定单位:%s的扩展信息", unitId));
            }
        } catch (Exception e) {
            logger.error("没有扩展表数据，该单位需要维护！");
            return Response.error().message("该单位需要维护").data("unit", unit).data("school", school).build();
        }
        return Response.ok()
                .data("unit", unit)
                .data("school", school)
                .data("opUnitEx", unitEx).message("查询成功").build();
    }


    /**
     * 编辑确定按钮的更新操作
     */
    @Secured({UnitOperateCode.UNIT_ADD, UnitOperateCode.UNIT_EDIT, UnitOperateCode.UNIT_NATURE_CHANGE})
    @Record(type = RecordType.URL)
    @RequestMapping("/updateUnitById")
    @ResponseBody
    public Response updateUnitById(UnitAddDto unitAddDto, @CurrentUser("id") String operatorId) {
        try {
            opUnitService.updateUnitAndSchoolAndUnitEx(unitAddDto, operatorId);
            return Response.ok().message("编辑成功").build();
        } catch (UnionCodeAlreadyExistsException e) {
            return Response.error().message("同一地区不能存在两个教育局").build();
        } catch (IllegalRenewalTimeException e) {
            return Response.error().message("续期时间必须超过当前时间!").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error().message("操作失败").build();
        }
    }

    /**
     * 取得汉语拼音字母，如果是不可转化的中文，如全角符号，则会自动过滤
     *
     * @param chineseStr  要转化的字符串（含中文、英语、数字等）
     * @param filterStrs  要过滤的字符
     * @param firstLetter 是否取首字母
     * @return
     */
    public static String toHanyuPinyin(String chineseStr, String[] filterStrs, boolean firstLetter) {
        if (StringUtils.isBlank(chineseStr)) {
            return "";
        }
        char[] cs = chineseStr.toCharArray();
        String names = "";
        for (char c : cs) {
            if (filterStrs != null && ArrayUtils.contains(filterStrs, c)) {
                continue;
            }
            if (CharUtils.isAscii(c)) {
                names += c;
            } else {
                String[] py = PinyinHelper.toHanyuPinyinStringArray(c);
                if (py != null) {
                    if (firstLetter) {
                        names += PinyinHelper.toHanyuPinyinStringArray(c)[0].substring(0, 1).toUpperCase();
                    } else {
                        String t = PinyinHelper.toHanyuPinyinStringArray(c)[0];
                        if (NumberUtils.isNumber(t.substring(t.length() - 1))) {
                            t = t.substring(0, t.length() - 1);
                        }
                        names += t;
                    }
                }
            }
        }
        return names;
    }

    @Secured(UnitOperateCode.UNIT_OPENACCOUNT)
    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/student-and-family/index"
    )
    public String studentAndFamilyAccountListIndex(@RequestParam(value = "unitId", required = false) String unitId,
                                                   Model model) {
        model.addAttribute("unitId", unitId);
        if (StringUtils.isNotBlank(unitId)) {
            //年级
            List<Grade> gradeList = gradeRemoteService.getGradesBySchoolId(unitId);
            model.addAttribute("gradeList", gradeList);
            //班级
            if (CollectionUtils.isNotEmpty(gradeList)) {
                Grade grade = gradeList.get(0);
                List<Clazz> clazzList = classRemoteService.getClazzesByGradeId(grade.getId());
                model.addAttribute("classList", clazzList);
            }
        }
        model.addAttribute("accountRule",
                systemIniRemoteService.getRawValueByIniId(SysOptionCode.KEY_STUDENT_FAMILY_USERNAME_RULE));
        return "/unitmanage/unitmanage-studentAndFamilyAccountListIndex.ftl";
    }

    @Secured(UnitOperateCode.UNIT_OPENACCOUNT)
    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/{unitId}/studentAndFamilyList"
    )
    public String studentAndFamilyListPage(@PathVariable("unitId") String unitId,
                                           @RequestParam("clazzId") String clazzId,
                                           Pageable pageable, Model model) {
        if (StringUtils.isNotBlank(clazzId)) {
            Page<StudentAndFamilyAccount> pages = userManageService.getStudentAndFamilyAccountsByUnitId(unitId,
                    StudentAndFamilyAccountQuery.of(clazzId), pageable);
            model.addAttribute("pages", pages);
            //是否需要创建账号
            boolean createStudentUsername = false;
            boolean createFamilyUsername = false;
            for (StudentAndFamilyAccount account : pages.getContent()) {
                if (account.getStudentUsername() == null) {
                    createStudentUsername = true;
                }
                if (account.getFamilyName() != null && account.getFamilyUsername() == null) {
                    createFamilyUsername = true;
                }
                if (createFamilyUsername && createStudentUsername) {
                    break;
                }
            }
            model.addAttribute("createFamilyUsername", createFamilyUsername);
            model.addAttribute("createStudentUsername", createStudentUsername);
        }
        return "/unitmanage/unitmanage-studentAndFamilyAccountList.ftl";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
