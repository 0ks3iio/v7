package net.zdsoft.szxy.operation.usermanage.controller;

import net.zdsoft.szxy.base.api.ClassRemoteService;
import net.zdsoft.szxy.base.api.FamilyRemoteService;
import net.zdsoft.szxy.base.api.GradeRemoteService;
import net.zdsoft.szxy.base.api.StudentRemoteService;
import net.zdsoft.szxy.base.api.UnitRemoteService;
import net.zdsoft.szxy.base.api.UserRemoteService;
import net.zdsoft.szxy.base.entity.Clazz;
import net.zdsoft.szxy.base.entity.Family;
import net.zdsoft.szxy.base.entity.Grade;
import net.zdsoft.szxy.base.entity.Student;
import net.zdsoft.szxy.base.entity.Unit;
import net.zdsoft.szxy.base.entity.User;
import net.zdsoft.szxy.base.exception.SzxyPassportException;
import net.zdsoft.szxy.base.query.FamilyQuery;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.security.UserDataRegionHolder;
import net.zdsoft.szxy.operation.usermanage.UserOperateCode;
import net.zdsoft.szxy.operation.usermanage.controller.vo.FamilyEditVo;
import net.zdsoft.szxy.operation.usermanage.controller.vo.FamilyVo;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/4/15 下午1:48
 */
@Controller
@RequestMapping("/operation/user/manage/family")
public class FamilyAccountController {

    private Logger logger = LoggerFactory.getLogger(FamilyAccountController.class);

    @Resource
    private FamilyRemoteService familyRemoteService;
    @Resource
    private UnitRemoteService unitRemoteService;
    @Resource
    private StudentRemoteService studentRemoteService;
    @Resource
    private UserRemoteService userRemoteService;
    @Resource
    private ClassRemoteService classRemoteService;
    @Resource
    private GradeRemoteService gradeRemoteService;

    @Record(type = RecordType.URL)
    @GetMapping(
            value = "index"
    )
    public String execute(String unitId, Model model) {
        model.addAttribute("unitId", unitId);
        return "/usermanage/usermanage-familyIndex.ftl";
    }

    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/list"
    )
    public String list(String unitId, String mobilePhone,
                       String username, String realName,
                       Pageable pageable, Model model) {
        //构造查询条件
        FamilyQuery familyQuery = new FamilyQuery();
        familyQuery.setMobilePhone(mobilePhone);
        familyQuery.setRealName(realName);
        familyQuery.setUnitId(unitId);
        familyQuery.setUsername(username);
        if (StringUtils.isBlank(unitId)) {
            Set<String> regions = UserDataRegionHolder.getRegions();
            if (!regions.isEmpty()) {
                familyQuery.setRegions(regions);
            }
        }
        //查询
        Page<Family> families = familyRemoteService.queryFamilies(familyQuery, pageable);
        //转换（包含用户名和学校名称显示）
        Page<FamilyVo> pages = new PageImpl<>(convertFamilyVo(families), pageable, families.getTotalElements());

        model.addAttribute("families", pages);
        return "/usermanage/usermanage-familyList.ftl";
    }

    private List<FamilyVo> convertFamilyVo(Page<Family> families) {
        String[] unitIds = families.stream().map(Family::getSchoolId).toArray(String[]::new);
        Map<String, String> unitNameMap =
                ArrayUtils.isEmpty(unitIds) ? Collections.emptyMap() : unitRemoteService.getUnitNameMap(unitIds);

        String[] ownerIds = families.stream().map(Family::getId).toArray(String[]::new);
        Map<String, String> usernameMap =
                ArrayUtils.isEmpty(ownerIds) ? Collections.emptyMap(): userRemoteService.getUsernameMapByOwnerIds(ownerIds);

        return families.getContent().stream().map(e->{
            FamilyVo familyVo = new FamilyVo();
            familyVo.setCreationTime(e.getCreationTime());
            familyVo.setId(e.getId());
            familyVo.setRealName(e.getRealName());
            familyVo.setUnitName(unitNameMap.get(e.getSchoolId()));
            familyVo.setUsername(usernameMap.get(e.getId()));
            familyVo.setMobilePhone(e.getMobilePhone());
            return familyVo;
        }).collect(Collectors.toList());
    }

    @Secured(UserOperateCode.USER_EDIT)
    @Record(type = RecordType.URL)
    @GetMapping(value = "{id}")
    public String doEdit(@PathVariable("id") String familyId, Model model) {
        Family family = familyRemoteService.getFamilyById(familyId);
        if (family == null) {
            throw new RuntimeException("该家长已被删除");
        }
        Student student = studentRemoteService.getStudentById(family.getStudentId());
        User user = userRemoteService.getUserByOwnerId(student.getId());

        Clazz clazz = classRemoteService.getClazzById(student.getClassId());

        Grade grade = gradeRemoteService.getGradeById(clazz.getGradeId());

        Unit unit = unitRemoteService.getUnitById(family.getSchoolId());

        FamilyEditVo familyEditVo = new FamilyEditVo();
        familyEditVo.setId(familyId);
        familyEditVo.setClassName(grade.getGradeName() + clazz.getClassName());
        familyEditVo.setStudentRealName(student.getStudentName());
        if (user != null) {
            familyEditVo.setStudentUsername(user.getUsername());
        }
        familyEditVo.setMobilePhone(family.getMobilePhone());
        familyEditVo.setUnitName(unit.getUnitName());
        familyEditVo.setRealName(family.getRealName());
        model.addAttribute("family", familyEditVo);
        return "/usermanage/usermanage-familyEdit.ftl";
    }

    @Secured(UserOperateCode.USER_EDIT)
    @ResponseBody
    @Record(type = RecordType.URL)
    @PutMapping(value = "{id}")
    public Response doUpdate(@PathVariable("id") String familyId,  String mobilePhone) {
        //更新手机号

        try {
            familyRemoteService.updateMobilePhone(familyId, mobilePhone);
        } catch (SzxyPassportException e) {
            logger.error("更新手机号出错", e);
            return Response.error().message("同步更新Passport手机号出错").build();
        }
        return Response.ok().message("更新成功").build();
    }
}
