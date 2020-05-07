package net.zdsoft.szxy.operation.inner.controller;

import net.zdsoft.szxy.alarm.SzxyMailSender;
import net.zdsoft.szxy.base.api.RegionRemoteService;
import net.zdsoft.szxy.base.entity.Region;
import net.zdsoft.szxy.base.enu.DeleteCode;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.inner.OpManageOperateCode;
import net.zdsoft.szxy.operation.inner.controller.vo.EditUserGroupVo;
import net.zdsoft.szxy.operation.inner.controller.vo.UserVo;
import net.zdsoft.szxy.operation.inner.dto.EditUser;
import net.zdsoft.szxy.operation.inner.dto.EditUserModule;
import net.zdsoft.szxy.operation.inner.dto.EditUserOperate;
import net.zdsoft.szxy.operation.inner.dto.ManagementUser;
import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.inner.enums.UserState;
import net.zdsoft.szxy.operation.inner.exception.IllegalStateException;
import net.zdsoft.szxy.operation.inner.permission.entity.Group;
import net.zdsoft.szxy.operation.inner.permission.entity.Module;
import net.zdsoft.szxy.operation.inner.permission.entity.ModuleOperate;
import net.zdsoft.szxy.operation.inner.permission.entity.UserModuleRelation;
import net.zdsoft.szxy.operation.inner.permission.service.GroupService;
import net.zdsoft.szxy.operation.inner.permission.service.ModuleOperateService;
import net.zdsoft.szxy.operation.inner.permission.service.ModuleService;
import net.zdsoft.szxy.operation.inner.permission.service.UserGroupRelationService;
import net.zdsoft.szxy.operation.inner.permission.service.UserModuleRelationService;
import net.zdsoft.szxy.operation.inner.service.OpUserService;
import net.zdsoft.szxy.operation.utils.PinyinUtils;
import net.zdsoft.szxy.plugin.mvc.Response;
import net.zdsoft.szxy.utils.UuidUtils;
import net.zdsoft.szxy.utils.crypto.PasswordUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 运营平台后台用户管理
 *
 * @author shenke
 * @since 2019/4/9 上午9:39
 */
@Controller
@RequestMapping("/operation/management/user")
public class ManagementUserController {

    private Logger logger = LoggerFactory.getLogger(ManagementUserController.class);

    @Resource
    private OpUserService opUserService;
    @Resource
    private GroupService groupService;
    @Resource
    private ModuleService moduleService;
    @Resource
    private ModuleOperateService moduleOperateService;
    @Resource
    private UserModuleRelationService userModuleRelationService;
    @Resource
    private UserGroupRelationService userGroupRelationService;
    @Resource
    private RegionRemoteService regionRemoteService;
    @Resource
    private SzxyMailSender szxyMailSender;

    @RequestMapping(
            value = "/index",
            method = RequestMethod.GET
    )
    public String index() {

        return "/inner/management/user-index.ftl";
    }

    @RequestMapping(
            value = "/list",
            method = RequestMethod.GET
    )
    @Record(type = RecordType.URL)
    public String doUserList(Pageable page, Model model) {
        Page<OpUser> pages = opUserService.getAllUsers(page);

        List<UserVo> userVos = Collections.emptyList();
        if (!pages.getContent().isEmpty()) {
            String[] userIds = pages.getContent().stream().map(OpUser::getId).toArray(String[]::new);
            Map<String, Set<String>> names = userGroupRelationService.getGroupNamesByUserId(userIds);
            userVos = pages.getContent().stream().map(e -> {
                UserVo userVo = new UserVo();
                userVo.setCreationTime(e.getCreationTime());
                userVo.setId(e.getId());
                userVo.setRealName(e.getRealName());
                userVo.setUsername(e.getUsername());
                userVo.setPhone(e.getPhone());
                userVo.setStateName(UserState.from(e.getState()).getName());
                userVo.setState(e.getState());
                if (names.containsKey(e.getId())) {
                    userVo.setGroupNames(String.join("、", names.get(e.getId())));
                }
                return userVo;
            }).collect(Collectors.toList());
        }
        model.addAttribute("users", new PageImpl<>(userVos, page, pages.getTotalElements()));
        return "/inner/management/user-list.ftl";
    }

    @Secured(OpManageOperateCode.OP_USER_EDIT)
    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/{id}/edit"
    )
    public String doEdit(@PathVariable("id") String userId, Model model) {
        return doAdd(userId, model);
    }

    @Secured(OpManageOperateCode.OP_USER_ADD)
    @RequestMapping(
            value = "/add",
            method = RequestMethod.GET
    )
    @Record(type = RecordType.URL)
    public String doAdd(@RequestParam(value = "id", required = false) String userId, Model model) {
        Optional<OpUser> optional = Optional.empty();
        if (StringUtils.isNotBlank(userId)) {
            optional = opUserService.getUserById(userId);
        }
        model.addAttribute("opUser", optional.orElse(new OpUser()));

        Set<String> selecteds = Collections.emptySet();
        if (optional.isPresent()) {
            //获取指定用户的分组信息
            selecteds = groupService.getGroupsByUserId(userId).stream().map(Group::getId).collect(Collectors.toSet());
        }

        model.addAttribute("groups", initUserGroupAuth(userId, selecteds));
        model.addAttribute("authGroupSize", selecteds.size());

        //个人权限
        model.addAttribute("modules", initUserAuth(userId));

        return "/inner/management/user-add.ftl";
    }

    private List<EditUserGroupVo> initUserGroupAuth(String userId, Set<String> selectedGroups) {
        //分组权限
        List<Group> groups = groupService.getAllGroups();
        String[] regionCodes = groups.stream().map(Group::getRegionCode).distinct().toArray(String[]::new);
        Map<String, String> regionMap = regionRemoteService.getRegionsByRegionCodes(regionCodes).stream().
                collect(Collectors.toMap(Region::getRegionCode, Region::getRegionName));


        return groups.stream().map(e -> {
            EditUserGroupVo userGroupVo = new EditUserGroupVo();
            userGroupVo.setId(e.getId());
            userGroupVo.setName(e.getName());
            userGroupVo.setRegionName(regionMap.get(e.getRegionCode()));
            userGroupVo.setHasAuth(selectedGroups.contains(e.getId()));
            return userGroupVo;
        }).collect(Collectors.toList());
    }

    private List<EditUserModule> initUserAuth(String userId) {
        List<Module> modules = moduleService.getAllModules();

        Map<String, List<ModuleOperate>> operateMap = new HashMap<>(modules.size());
        for (ModuleOperate operate : moduleOperateService.getAllOperates()) {
            operateMap.computeIfAbsent(operate.getModuleId(), e -> new ArrayList<>()).add(operate);
        }


        List<UserModuleRelation> userModuleRelations = userModuleRelationService.getRelationsByUserId(userId);
        Map<String, Set<String>> moduleOperateMap = new HashMap<>();
        for (UserModuleRelation relation : userModuleRelations) {
            if (relation.getOperateId() != null) {
                moduleOperateMap.computeIfAbsent(relation.getModuleId(), e -> new HashSet<>()).add(relation.getOperateId());
            }
        }

        List<EditUserModule> userModules = new ArrayList<>(modules.size());

        for (Module module : modules) {
            EditUserModule userModule = new EditUserModule();
            userModule.setModuleId(module.getId());
            userModule.setModuleName(module.getName());

            userModule.setHasAuth(moduleOperateMap.containsKey(module.getId()));
            Set<String> authOperates = moduleOperateMap.get(module.getId());

            List<ModuleOperate> moduleOperates = operateMap.get(module.getId());
            if (moduleOperates != null) {
                List<EditUserOperate> userOperates = new ArrayList<>(moduleOperates.size());
                for (ModuleOperate operate : moduleOperates) {
                    EditUserOperate userOperate = new EditUserOperate();
                    userOperate.setHasAuth(authOperates != null && authOperates.contains(operate.getId()));
                    userOperate.setId(operate.getId());
                    userOperate.setOperateName(operate.getOperateName());
                    userOperates.add(userOperate);
                }
                userModule.setOperates(userOperates);
            }
            userModules.add(userModule);
        }

        return userModules;
    }

    @Secured(OpManageOperateCode.OP_USER_ADD)
    @Record(type = RecordType.URL)
    @ResponseBody
    @GetMapping(
            value = "/generate-username"
    )
    public Response generateUsername(String realName) {
        /*
         * 账号生成规则暂定如下
         * 1、若用户姓名的汉字个数小于3，则直接将姓名转换为拼音作为用户名
         * 2、若用户姓名的汉字个数大于3，则取前面拼音的首字母 + 最后一个汉字拼音全称作为用户名
         */
        String username;
        if (StringUtils.length(realName) < 3) {
            username = PinyinUtils.toHanyuPinyi(realName);
        } else {
            StringBuilder nameBuilder = new StringBuilder();
            char[] realNames = realName.toCharArray();
            for (int i = 0, length = realNames.length; i < length; i++) {
                if (i == 0) {
                    nameBuilder.append(PinyinUtils.toHanyuPinyinForChar(realNames[i]));
                } else {
                    nameBuilder.append(PinyinUtils.toHanyuPinyinForFirstLetter(realNames[i]));
                }

            }
            username = checkUsername(nameBuilder.toString(), 0);
        }

        boolean existsRealNameUser = opUserService.existsByRealName(realName);
        String message = existsRealNameUser ? String.format("姓名为：【%s】已存在相同姓名的账号", realName) : null;
        return Response.ok().message(message).data("username", username).build();
    }

    private String checkUsername(String username, int index) {
        if (opUserService.existsByUsername(username)) {
            return checkUsername(username + (++index), index);
        }
        return username;
    }

    @Secured(OpManageOperateCode.OP_USER_ADD)
    @ResponseBody
    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/checkUsername"
    )
    public Response doCheckUsername(OpUser user) {
        boolean exists = opUserService.existsByUsername(user.getUsername());
        String message = exists ? "用户名已存在" : "可用的用户名";
        return Response.ok().message(message).data("valid", !exists).build();
    }

    @Secured({OpManageOperateCode.OP_USER_ADD, OpManageOperateCode.OP_USER_EDIT})
    @ResponseBody
    @RequestMapping(
            value = {"/save", "/update"},
            method = {RequestMethod.POST}
    )
    @Record(type = RecordType.URL)
    public Response doSaveUser(@Valid ManagementUser managementUser, BindingResult errors) {
        checkNotAdmin(managementUser.getUser().getId());
        if (errors.hasErrors()) {
            //TODO resolve error
        }

        EditUser editUser = managementUser.getUser();
        OpUser user = new OpUser();
        if (StringUtils.isBlank(editUser.getId())) {
            user.setId(UuidUtils.generateUuid());
        } else {
            user.setId(editUser.getId());
        }
        user.setUsername(editUser.getUsername());
        user.setRealName(editUser.getRealName());
        user.setEmail(editUser.getEmail());
        user.setPhone(editUser.getPhone());
        user.setPassword(PasswordUtils.encode(generatePassword()));
        user.setIsDeleted(DeleteCode.NOT_DELETED);
        user.setState(UserState.NORMAL.getState());
        user.setCreationTime(new Date());
        user.setSex(editUser.getSex());
        user.setAuthRegionCode(editUser.getRegionCode());

        List<UserModuleRelation> moduleRelations = convertModules(managementUser.getModules(), user.getId());

        List<String> groupIds = managementUser.getGroups();
        if (groupIds != null) {
            groupIds = managementUser.getGroups().stream().filter(Objects::nonNull).collect(Collectors.toList());
        }

        opUserService.saveOrUpdateUser(user, groupIds, moduleRelations);
        if (StringUtils.isBlank(editUser.getId())) {
            sendOpenAccountMail(user.getEmail(), "您的运营平台账号已开通，【" + user.getUsername() + "/" + PasswordUtils.decode(user.getPassword()) + "】");
        }
        return Response.ok().message(StringUtils.isEmpty(editUser.getId()) ? "新增用户成功" : "用户更新成功").build();
    }

    private void sendOpenAccountMail(String email, String message) {
        new Thread(() -> {
            try {
                szxyMailSender.sendSimpleMailMessage(message, "运营平台系统消息", email);
            } catch (Exception e) {
                logger.error("密码重置提醒邮件发送失败", e);
            }
        }).start();
    }


    private List<UserModuleRelation> convertModules(List<EditUserModule> modules, String userId) {
        if (CollectionUtils.isEmpty(modules)) {
            return null;
        }
        List<UserModuleRelation> relations = new ArrayList<>();
        for (EditUserModule module : modules) {
            if (module.getOperates() != null) {
                for (EditUserOperate operate : module.getOperates()) {
                    if (StringUtils.isNotBlank(operate.getId())) {
                        UserModuleRelation relation = new UserModuleRelation();
                        relation.setId(UuidUtils.generateUuid());
                        relation.setModuleId(module.getModuleId());
                        relation.setOperateId(operate.getId());
                        relation.setUserId(userId);
                        relations.add(relation);
                    }
                }
            }
        }
        return relations;
    }

    @Secured(OpManageOperateCode.OP_USER_DELETE)
    @ResponseBody
    @RequestMapping(
            value = "{id}",
            method = RequestMethod.DELETE
    )
    @Record(type = RecordType.URL)
    public Response doDelete(@PathVariable("id") String userId) {
        checkNotAdmin(userId);
        opUserService.deleteUser(userId);
        return Response.ok().message("删除用户成功").build();
    }

    @Secured(OpManageOperateCode.OP_USER_STATE_CHANGE)
    @ResponseBody
    @RequestMapping(
            value = "{id}",
            method = RequestMethod.PUT
    )
    public Response doChangeState(@PathVariable("id") String userId,
                                  @RequestParam("state") Integer state) {
        checkNotAdmin(userId);
        try {
            opUserService.updateUserState(userId, state);
            return Response.ok().message("更新成功").build();
        } catch (IllegalStateException e) {
            logger.error("不合法的用户状态值: {}", state);
            return Response.error().message("不合法的状态值", state).build();
        }
    }

    @Secured(OpManageOperateCode.OP_USER_RESET_PASSWORD)
    @ResponseBody
    @RequestMapping(
            value = "{id}/reset-password",
            method = RequestMethod.POST
    )
    @Record(type = RecordType.URL)
    public Response doResetPassword(@PathVariable("id") String userId) {
        checkNotAdmin(userId);
        String defaultPassword = generatePassword();
        opUserService.updatePassword(userId, defaultPassword);

        sendMail(userId, defaultPassword);

        String message = "密码重置成功，若已配置邮箱，将会收到提醒邮件";
        return Response.ok().message(message).data("password", defaultPassword).build();
    }

    private void sendMail(String userId, String defaultPassword) {
        //TODO 这里应该走消息中心，很可惜没有消息中心，发送邮件又比较耗时，所以临时起了一个线程（Fixed：Thread-Pool）
        new Thread(() -> {
            Optional<OpUser> opUser = opUserService.getUserById(userId);
            if (opUser.isPresent()) {
                try {
                    szxyMailSender.sendSimpleMailMessage(String.format("您的密码已被重置（%s），请及时修改您的密码", defaultPassword), "密码重置提醒", opUser.get().getEmail());
                } catch (Exception e) {
                    logger.error("密码重置提醒邮件发送失败", e);
                }
            }
        }).start();
    }

    private String generatePassword() {
        String now = String.valueOf(System.currentTimeMillis());
        return "zdsoft" + now.substring(now.length() - 9);
    }

    @Secured(OpManageOperateCode.OP_USER_AUTH)
    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/{id}/auth"
    )
    public String userAuth(@PathVariable("id") String userId, Model model) {
        checkNotAdmin(userId);
        model.addAttribute("modules", initUserAuth(userId));
        Set<String> selecteds = groupService.getGroupsByUserId(userId).stream().map(Group::getId).collect(Collectors.toSet());
        model.addAttribute("groups", initUserGroupAuth(userId, selecteds));
        model.addAttribute("opUser", opUserService.getUserById(userId).orElseThrow(()->new RuntimeException("该用户不存在")));
        return "/inner/management/user-auth-manage.ftl";
    }

    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/{id}/viewAuth"
    )
    public String doViewAuth(@PathVariable("id") String userId, Model model) {
        model.addAttribute("modules", initUserAuth(userId));
        Set<String> selecteds = groupService.getGroupsByUserId(userId).stream().map(Group::getId).collect(Collectors.toSet());
        model.addAttribute("groups", initUserGroupAuth(userId, selecteds));
        return "/inner/management/user-auth-manage.ftl";
    }

    @Secured(OpManageOperateCode.OP_USER_AUTH)
    @ResponseBody
    @Record(type = RecordType.URL)
    @PostMapping(value = "/{id}/auth")
    public Response doUpdateUserAuth(@PathVariable("id") String userId, ManagementUser managementUser) {
        checkNotAdmin(userId);
        List<UserModuleRelation> moduleRelations = convertModules(managementUser.getModules(), userId);

        List<String> groupIds = managementUser.getGroups();
        if (groupIds != null) {
            groupIds = managementUser.getGroups().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        String regionCode = null;
        if (managementUser.getUser() != null) {
            regionCode = managementUser.getUser().getRegionCode();
        }
        OpUser user = new OpUser();
        user.setId(userId);
        user.setAuthRegionCode(regionCode);
        opUserService.saveOrUpdateUser(user, groupIds, moduleRelations);
        return Response.ok().message("权限变更成功").build();
    }

    private void checkNotAdmin(String userId) {
        opUserService.getUserById(userId).ifPresent(e -> {
            if (OpUser.ADMIN_USER_NAME.equals(e.getUsername())) {
                throw new RuntimeException("系统管理员信息不能编辑");
            }
        });
    }


}