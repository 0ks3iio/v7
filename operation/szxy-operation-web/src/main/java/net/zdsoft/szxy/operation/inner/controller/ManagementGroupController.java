package net.zdsoft.szxy.operation.inner.controller;

import net.zdsoft.szxy.base.api.RegionRemoteService;
import net.zdsoft.szxy.base.entity.Region;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.inner.OpManageOperateCode;
import net.zdsoft.szxy.operation.inner.controller.vo.GroupUserVo;
import net.zdsoft.szxy.operation.inner.controller.vo.GroupVo;
import net.zdsoft.szxy.operation.inner.dto.EditGroup;
import net.zdsoft.szxy.operation.inner.dto.EditGroupModule;
import net.zdsoft.szxy.operation.inner.dto.EditUserOperate;
import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.inner.permission.dto.GroupUserCounter;
import net.zdsoft.szxy.operation.inner.permission.entity.Group;
import net.zdsoft.szxy.operation.inner.permission.entity.GroupModuleRelation;
import net.zdsoft.szxy.operation.inner.permission.entity.Module;
import net.zdsoft.szxy.operation.inner.permission.entity.ModuleOperate;
import net.zdsoft.szxy.operation.inner.permission.entity.UserGroupRelation;
import net.zdsoft.szxy.operation.inner.permission.service.GroupModuleRelationService;
import net.zdsoft.szxy.operation.inner.permission.service.GroupService;
import net.zdsoft.szxy.operation.inner.permission.service.ModuleOperateService;
import net.zdsoft.szxy.operation.inner.permission.service.ModuleService;
import net.zdsoft.szxy.operation.inner.permission.service.UserGroupRelationService;
import net.zdsoft.szxy.operation.inner.service.OpUserService;
import net.zdsoft.szxy.plugin.mvc.Response;
import net.zdsoft.szxy.utils.UuidUtils;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/4/9 上午10:13
 */
@Controller
@RequestMapping("/operation/management/group")
public class ManagementGroupController {

    private Logger logger = LoggerFactory.getLogger(ManagementUserController.class);

    @Resource
    private GroupService groupService;
    @Resource
    private ModuleService moduleService;
    @Resource
    private ModuleOperateService moduleOperateService;
    @Resource
    private OpUserService opUserService;
    @Resource
    private RegionRemoteService regionRemoteService;
    @Resource
    private UserGroupRelationService userGroupRelationService;
    @Resource
    private GroupModuleRelationService groupModuleRelationService;

    @RequestMapping(
            value = "index",
            method = RequestMethod.GET
    )
    public String execute() {

        return "/inner/management/group-index.ftl";
    }

    @Record(type = RecordType.URL)
    @RequestMapping(
            value = "/list",
            method = RequestMethod.GET
    )
    public String groupList(Pageable pageable, Model model) {
        Page<Group> groups = groupService.getAllGroups(pageable);

        String[] regionCodes = groups.getContent().stream().map(Group::getRegionCode).toArray(String[]::new);
        Map<String, String> regionNameMap = regionRemoteService.getRegionsByRegionCodes(regionCodes).stream()
                .collect(Collectors.toMap(Region::getRegionCode, Region::getFullName));
        regionNameMap.put(Group.ALL_REGION, Group.ALL_REGION_NAME);

        Map<String, Long> userCountMap = userGroupRelationService.getGroupUserCounters(groups.stream().map(Group::getId).toArray(String[]::new))
                .stream().collect(Collectors.toMap(GroupUserCounter::getGroupId, GroupUserCounter::getCount));

        List<GroupVo> groupVos = groups.getContent().stream().map(e -> {
            GroupVo groupVo = new GroupVo();
            groupVo.setId(e.getId());
            groupVo.setCreationTime(e.getCreationTime());
            groupVo.setRegionName(regionNameMap.getOrDefault(e.getRegionCode(), Group.NO_REGION_NAME));
            groupVo.setUserCount(userCountMap.getOrDefault(e.getId(), 0L).intValue());
            groupVo.setName(e.getName());
            return groupVo;
        }).collect(Collectors.toList());

        model.addAttribute("groups", new PageImpl<>(groupVos, pageable, groups.getTotalElements()));
        return "/inner/management/group-list.ftl";
    }


    @Secured(OpManageOperateCode.GROUP_ADD)
    @Record(type = RecordType.URL)
    @RequestMapping(
            value = "/add",
            method = RequestMethod.GET
    )
    public String groupAdd(Model model) {

        model.addAttribute("modules", initModules(null));
        List<OpUser> users = opUserService.getAllUsers().stream()
                .filter(e->!OpUser.ADMIN_USER_NAME.equals(e.getUsername()))
                .collect(Collectors.toList());
        model.addAttribute("users", users);

        return "/inner/management/group-add.ftl";
    }

    private List<EditGroupModule> initModules(String groupId) {
        List<Module> modules = moduleService.getAllModules();

        Map<String, List<ModuleOperate>> operateMap = new HashMap<>(modules.size());
        for (ModuleOperate operate : moduleOperateService.getAllOperates()) {
            operateMap.computeIfAbsent(operate.getModuleId(), e -> new ArrayList<>()).add(operate);
        }

        Set<String> moduleIds = Collections.emptySet();
        Set<String> operateIds = Collections.emptySet();
        if (groupId != null) {
            List<GroupModuleRelation> groupModuleRelations = groupModuleRelationService.getGroupModuleRelationsByGroupId(groupId);
            moduleIds = groupModuleRelations.stream().map(GroupModuleRelation::getModuleId).collect(Collectors.toSet());
            operateIds = groupModuleRelations.stream().map(GroupModuleRelation::getOperateId).collect(Collectors.toSet());
        }

        List<EditGroupModule> groupModules = new ArrayList<>(modules.size());

        for (Module module : modules) {
            EditGroupModule groupModule = new EditGroupModule();
            groupModule.setModuleId(module.getId());
            groupModule.setModuleName(module.getName());
            groupModule.setHasAuth(moduleIds.contains(module.getId()));

            List<ModuleOperate> moduleOperates = operateMap.get(module.getId());
            if (moduleOperates != null) {
                List<EditUserOperate> userOperates = new ArrayList<>(moduleOperates.size());
                for (ModuleOperate operate : moduleOperates) {
                    EditUserOperate userOperate = new EditUserOperate();
                    userOperate.setId(operate.getId());
                    userOperate.setOperateName(operate.getOperateName());
                    userOperates.add(userOperate);
                    userOperate.setHasAuth(operateIds.contains(operate.getId()));
                }
                groupModule.setOperates(userOperates);
            }
            groupModules.add(groupModule);
        }

        return groupModules;
    }

    @Record(type = RecordType.URL)
    @ResponseBody
    @GetMapping(
            value = "checkGroupName"
    )
    public Response doCheckGroupName(@RequestParam("name") String groupName) {
        boolean exists = groupService.existsByGroupName(groupName);
        String message = exists ? "分组名已被使用" : "可用的分组名称";
        return Response.ok().data("valid", !exists).message(message).build();
    }

    @Secured(OpManageOperateCode.GROUP_ADD)
    @ResponseBody
    @Record(type = RecordType.URL)
    @PostMapping(
            value = "/save"
    )
    public Response doSaveGroup(@Valid EditGroup editGroup, BindingResult errors) {
        //TODO 先不管后台校验出现的错误

        Group group = new Group();
        group.setId(UuidUtils.generateUuid());
        group.setName(editGroup.getName());
        group.setCreationTime(new Date());
        if (StringUtils.isNotBlank(editGroup.getRegionCode())) {
            group.setRegionCode(editGroup.getRegionCode());
        } else {
            group.setRegionCode(Group.ALL_REGION);
        }

        List<GroupModuleRelation> groupModuleRelations = convertGroupModelRelations(group.getId(), editGroup.getModules());
        List<UserGroupRelation> userGroupRelations = convertUserGroupRelations(group.getId(), editGroup.getUsers());

        if (logger.isDebugEnabled()) {
            logger.debug("Group is {}", editGroup);
        }

        groupService.saveGroups(group, groupModuleRelations, userGroupRelations);
        return Response.ok().message("分组保存成功").build();
    }

    private List<GroupModuleRelation> convertGroupModelRelations(String groupId, List<EditGroupModule> modules) {
        if (modules == null) {
            return null;
        }
        List<GroupModuleRelation> moduleRelations = new ArrayList<>();
        for (EditGroupModule module : modules) {
            if (module.getOperates() != null) {
                for (EditUserOperate operate : module.getOperates()) {
                    if (operate.getId() != null) {
                        GroupModuleRelation moduleRelation = new GroupModuleRelation();
                        moduleRelation.setGroupId(groupId);
                        moduleRelation.setId(UuidUtils.generateUuid());
                        moduleRelation.setModuleId(module.getModuleId());
                        moduleRelation.setOperateId(operate.getId());
                        moduleRelations.add(moduleRelation);
                    }
                }
            }
        }
        return moduleRelations;
    }

    private List<UserGroupRelation> convertUserGroupRelations(final String groupId, List<OpUser> users) {
        if (users == null) {
            return null;
        }
        return users.stream().filter(Objects::nonNull).filter(e -> StringUtils.isNotBlank(e.getId())).map(e -> {
            UserGroupRelation userGroupRelation = new UserGroupRelation();
            userGroupRelation.setId(UuidUtils.generateUuid());
            userGroupRelation.setGroupId(groupId);
            userGroupRelation.setUserId(e.getId());
            return userGroupRelation;
        }).collect(Collectors.toList());
    }

    @Secured(OpManageOperateCode.GROUP_DELETE)
    @Record(type = RecordType.URL)
    @ResponseBody
    @DeleteMapping(
            value = "{id}"
    )
    public Response doDelete(@PathVariable("id") String groupId) {
        groupService.deleteGroup(groupId);
        return Response.ok().message("用户组解散成功").build();
    }

    @Secured(OpManageOperateCode.GROUP_NAME_CHANGE)
    @Record(type = RecordType.URL)
    @ResponseBody
    @PutMapping(
            value = "{id}"
    )
    public Response doUpdateGroupName(@PathVariable("id") String groupId,
                                      @RequestParam("name") String groupName) {
        if (groupService.existsByGroupName(groupName)) {
            return Response.error().message("分组名称已存在").build();
        }
        groupService.updateGroupName(groupId, groupName);
        return Response.ok().message("分组名称变更成功").build();
    }

    @Secured(OpManageOperateCode.GROUP_MEMBER_CHANGE)
    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/{id}/users"
    )
    public String doEditGroupUsers(@PathVariable("id") String groupId, Model model) {
        Set<String> userIds = userGroupRelationService.getUsersByGroupId(groupId);
        List<GroupUserVo> users = opUserService.getAllUsers().stream()
                .filter(e -> !OpUser.ADMIN_USER_NAME.equals(e.getUsername()))
                .map(e -> {
                    GroupUserVo groupUserVo = new GroupUserVo();
                    groupUserVo.setChecked(userIds.contains(e.getId()));
                    groupUserVo.setId(e.getId());
                    groupUserVo.setName(e.getRealName());
                    return groupUserVo;
                }).collect(Collectors.toList());
        model.addAttribute("users", users);
        return "/inner/management/group-user-manage.ftl";
    }

    @Secured(OpManageOperateCode.GROUP_MEMBER_CHANGE)
    @ResponseBody
    @Record(type = RecordType.URL)
    @PostMapping(
            value = "/{id}/users"
    )
    public Response doUpdateGroupUsers(@PathVariable("id") String groupId, String[] id) {
        userGroupRelationService.updateGroupUsers(groupId, id);
        return Response.ok().message("分组成员变更成功").build();
    }

    @Secured(OpManageOperateCode.GROUP_AUTH_CHANGE)
    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/{id}/auth"
    )
    public String doEditGroupAuth(@PathVariable("id") String groupId, Model model) {
        model.addAttribute("modules", initModules(groupId));
        GroupVo groupVo = new GroupVo();
        groupVo.setId(groupId);
        String regionCode = groupService.getGroupById(groupId).orElseThrow(() -> new RuntimeException("该分组已被删除")).getRegionCode();
        groupVo.setRegionCode(regionCode);
        model.addAttribute("group", groupVo);
        return "/inner/management/group-auth-manage.ftl";
    }

    @Secured(OpManageOperateCode.GROUP_AUTH_CHANGE)
    @ResponseBody
    @Record(type = RecordType.URL)
    @PostMapping(
            value = "/{id}/auth"
    )
    public Response doUpdateGroupAuth(@PathVariable("id") String groupId, EditGroup editGroup) {
        groupModuleRelationService.updateGroupModuleRelations(groupId, editGroup.getRegionCode(), convertGroupModelRelations(groupId, editGroup.getModules()));
        if (logger.isDebugEnabled()) {
            logger.debug("授权变更: {}", editGroup);
        }
        return Response.ok().message("分组授权变更成功").build();
    }

    /**
     * 新增用户时，查看每个分组的权限情况
     */
    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/{id}/viewAuth"
    )
    public String doGetGroupAuth(@PathVariable("id") String groupId, Model model) {
        List<EditGroupModule> modules =
                initModules(groupId).stream().filter(EditGroupModule::isHasAuth).collect(Collectors.toList());
        model.addAttribute("modules", modules);
        model.addAttribute("regionName", getRegionName(groupId));
        return "/inner/management/group-auth-view.ftl";
    }

    private String getRegionName(String groupId) {
        Group group = groupService.getGroupById(groupId).orElseThrow(() -> new RuntimeException("该分组已被删除"));
        if (Group.ALL_REGION.equals(group.getRegionCode())) {
            return Group.ALL_REGION_NAME;
        } else {
            Region region = regionRemoteService.getRegionByRegionCode(group.getRegionCode());
            if (region == null) {
                return Group.NO_REGION_NAME;
            }
            return region.getFullName();
        }
    }
}
