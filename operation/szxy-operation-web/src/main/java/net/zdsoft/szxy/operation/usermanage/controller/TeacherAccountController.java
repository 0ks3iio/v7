package net.zdsoft.szxy.operation.usermanage.controller;

import net.zdsoft.szxy.base.api.UnitRemoteService;
import net.zdsoft.szxy.base.api.UserRemoteService;
import net.zdsoft.szxy.base.dto.UserUpdater;
import net.zdsoft.szxy.base.entity.UnitExtension;
import net.zdsoft.szxy.base.entity.User;
import net.zdsoft.szxy.base.enu.UnitExtensionExpireType;
import net.zdsoft.szxy.base.enu.UnitExtensionNature;
import net.zdsoft.szxy.base.enu.UserOwnerTypeCode;
import net.zdsoft.szxy.base.query.UserQuery;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.security.UserDataRegionHolder;
import net.zdsoft.szxy.operation.unitmanage.service.NoUnitExtensionException;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitService;
import net.zdsoft.szxy.operation.usermanage.UserOperateCode;
import net.zdsoft.szxy.operation.usermanage.controller.vo.TeacherAccountEditVo;
import net.zdsoft.szxy.operation.usermanage.controller.vo.UserStateVo;
import net.zdsoft.szxy.operation.usermanage.controller.vo.UserVo;
import net.zdsoft.szxy.operation.usermanage.dto.TeacherAccountQuery;
import net.zdsoft.szxy.operation.usermanage.dto.TeacherAccountUpdateInfo;
import net.zdsoft.szxy.plugin.mvc.Response;
import net.zdsoft.szxy.utils.crypto.PasswordUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/4/15 上午11:22
 */
@Controller
@RequestMapping("/operation/user/manage/teacher")
public class TeacherAccountController {

    @Resource
    private UnitRemoteService unitRemoteService;
    @Resource
    private UserRemoteService userRemoteService;
    @Resource
    private OpUnitService opUnitService;

    @GetMapping(
            value = "/index"
    )
    public String teacherAccountListIndex(@RequestParam(value = "unitId", required = false) String unitId,
                                          Model model) {
        model.addAttribute("unitId", unitId);
        model.addAttribute("userStateList", UserStateVo.getUserStates());
        return "/usermanage/usermanage-teacherAccountListIndex.ftl";
    }

    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/list"
    )
    public String teacherListPage(String unitId,
                                  TeacherAccountQuery teacherAccountQuery,
                                  Pageable pageable, Model model) {

        UserQuery userQuery = convertUserQuery(teacherAccountQuery, unitId);
        Page<User> users = userRemoteService.queryUsers(userQuery, pageable);

        //查找单位数据
        List<UserVo> userVos = convertUserVos(users.getContent());
        model.addAttribute("pages", new PageImpl<>(userVos, pageable, users.getTotalElements()));

        return "/usermanage/usermanage-teacherAccountList.ftl";
    }

    private List<UserVo> convertUserVos(List<User> userList) {
        String[] unitIds = userList.stream().map(User::getUnitId).toArray(String[]::new);
        Map<String, String> unitNameMap = ArrayUtils.isEmpty(unitIds) ? Collections.emptyMap() : unitRemoteService.getUnitNameMap(unitIds);
        ;
        Date now = new Date();
        return userList.stream().map(e -> {
            UserVo userVo = new UserVo();
            userVo.setId(e.getId());
            userVo.setExpireDate(e.getExpireDate());
            if (Objects.nonNull(e.getExpireDate())) {
                userVo.setExpire(now.after(e.getExpireDate()));
            } else {
                userVo.setExpire(false);
            }
            userVo.setMobilePhone(e.getMobilePhone());
            userVo.setRealName(e.getRealName());
            userVo.setUnitName(unitNameMap.get(e.getUnitId()));
            userVo.setUsername(e.getUsername());
            userVo.setUserState(e.getUserState());
            return userVo;
        }).collect(Collectors.toList());
    }

    private UserQuery convertUserQuery(TeacherAccountQuery query, String unitId) {
        UserQuery userQuery = new UserQuery();
        if (StringUtils.isNotBlank(unitId)) {
            userQuery.setUnitId(unitId);
        } else {
            Set<String> regions = UserDataRegionHolder.getRegions();
            if (!regions.isEmpty()) {
                userQuery.setRegions(regions);
            }
        }
        userQuery.setMobilePhone(query.getMobilePhone());
        userQuery.setOwnerType(UserOwnerTypeCode.TEACHER);
        userQuery.setRealName(query.getRealName());
        userQuery.setUsername(query.getUsername());
        userQuery.setUserState(query.getUserState());

        return userQuery;
    }

    @Secured(UserOperateCode.USER_EDIT)
    @Record(type = RecordType.URL)
    @RequestMapping(
            value = "/edit"
    )
    public String teacherEdit(@RequestParam("userId") String id, Model model) {
        User user = userRemoteService.getUserById(id);
        if (user == null) {
            throw new RuntimeException("该账号已不存在");
        }
        TeacherAccountEditVo editVo = new TeacherAccountEditVo();
        editVo.setId(user.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        editVo.setCreationTime(formatter.format(user.getCreationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
        if (user.getExpireDate() != null) {
            editVo.setExpireTime(formatter.format(user.getExpireDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
        }
        editVo.setOrderNumber(user.getDisplayOrder());
        editVo.setPassword(PasswordUtils.encodeIfNot(user.getPassword()));
        editVo.setUsername(user.getUsername());
        editVo.setUserState(user.getUserState());

        boolean trial = false;
        Date unitExpireTime = null;
        UnitExtension unitExtension = null;
        try {
            unitExtension = opUnitService.findByUnitId(user.getUnitId());
            trial = UnitExtensionNature.TRIAL.equals(unitExtension.getUsingNature());
            if (UnitExtensionExpireType.SPECIFY_TIME.equals(unitExtension.getExpireTimeType())) {
                unitExpireTime = unitExtension.getExpireTime();
                model.addAttribute("unitExpireTime", unitExpireTime.getTime());
            }
            model.addAttribute("trial", trial);

        } catch (NoUnitExtensionException e) {

        }
        model.addAttribute("teacherAccount", editVo);
        return "/usermanage/usermanage-teacherAccountEdit.ftl";
    }

    @ResponseBody
    @Secured(UserOperateCode.USER_EDIT)
    @Record(type = RecordType.URL)
    @PutMapping(
            value = "update"
    )
    public Response doUpdateAccount(@Valid TeacherAccountUpdateInfo updateInfo,
                                    BindingResult errors) {
        if (errors.hasFieldErrors()) {
            return Response.error().message(errors.getFieldError().getDefaultMessage()).build();
        }

        UserUpdater updater = new UserUpdater();
        updater.setId(updateInfo.getId());
        updater.setDisplayOrder(updateInfo.getOrderNumber());
        updater.setUserState(updateInfo.getUserState());
        updater.setExpireTime(updateInfo.getExpireDate());
        updater.setIgnoreExpireTime(false);
        userRemoteService.updateUser(updater);
        return Response.ok().message("更新成功").build();
    }
}
