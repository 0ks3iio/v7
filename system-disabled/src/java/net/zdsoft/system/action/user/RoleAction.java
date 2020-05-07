/* 
 * @(#)RoleAction.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.action.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.enums.UnitClassEnum;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.enums.AppStatusEnum;
import net.zdsoft.system.dto.user.RoleDto;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.enums.YesNoEnum;
import net.zdsoft.system.enums.server.OrderTypeEnum;
import net.zdsoft.system.enums.user.UserTypeEnum;
import net.zdsoft.system.service.server.ModelService;
import net.zdsoft.system.service.server.ServerAuthorizeService;
import net.zdsoft.system.service.server.ServerService;
import net.zdsoft.system.service.user.RolePermService;
import net.zdsoft.system.service.user.RoleService;
import net.zdsoft.system.service.user.UserRoleService;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:24:06 $
 */
@Controller
@RequestMapping("/system/role")
public class RoleAction extends BaseAction {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private ServerAuthorizeService serverAuthorizeService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private RolePermService rolePermService;
    @Autowired
    private ServerService serverService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;

    /**
     * 用户组列表
     * 
     * @author cuimq
     * @param map
     * @return
     */
    @ControllerInfo("进入用户组授权列表页面")
    @RequestMapping("/roleList")
    public String roleList(ModelMap map) {
        // 用户组列表
        List<Role> roleList = roleService.findByUnitId(getLoginInfo().getUnitId());

        List<RoleDto> roleDtoList = new ArrayList<RoleDto>();
        if (CollectionUtils.isNotEmpty(roleList)) {
            // 查找用户组成员
            Set<String> roleIdSet = EntityUtils.getSet(roleList, "id");
            Map<String, List<User>> roleIdAndUsers = userRoleService.findRoleIdAndUsers(roleIdSet
                    .toArray(new String[roleIdSet.size()]));

            for (Role role : roleList) {
                roleDtoList.add(new RoleDto(role, roleIdAndUsers.get(role.getId())));
            }
        }

        map.put("roleList", roleDtoList);
        return "/system/user/roleList.ftl";
    }

    /**
     * 新增用户组
     * 
     * @author cuimq
     * @param map
     * @return
     */
    @ControllerInfo("进入新增用户组页面")
    @RequestMapping("/addRole")
    public String addRoleView(ModelMap map) {
        // 获取单位教师用户列表
        List<User> userList = User.dt(userRemoteService.findByUnitIdAndRealNameAndOwnerTypeAndUserType(getLoginInfo()
                .getUnitId(), "", UserTypeEnum.TEACHER.getValue(), new Integer[] { 2 }, ""), new Pagination());

        map.put("userList", userList);
        map.put("role", new Role());
        map.put("memberUsers", new ArrayList<String>());
        map.put("breadcrumbName", "新增用户组");
        return "/system/user/roleDetail.ftl";
    }

    /**
     * 检测用户组名称
     * 
     * @author cuimq
     * @param id
     * @param name
     * @return
     */
    @ResponseBody
    @ControllerInfo("检测用户组名是否有重复")
    @RequestMapping("/checkRoleName")
    public String checkRoleName(String id, String name) {
        Role role = roleService.findByIdAndNameAndUnitId(id, name, getLoginInfo().getUnitId());
        if (null == role) {
            return success("成功");
        }
        else {
            return error("用户组名称已存在");
        }
    }

    /**
     * 
     * 
     * @author cuimq
     * @param id
     * @param map
     * @return
     */
    @ControllerInfo("进入修改用户组页面")
    @RequestMapping("/modifyRole")
    public String modifyRole(String roleId, ModelMap map) {
        // 用户组信息
        Role role = roleService.findById(roleId);

        // 获取单位教师用户列表
        List<User> userList = User.dt(userRemoteService.findByUnitIdAndRealNameAndOwnerTypeAndUserType(getLoginInfo()
                .getUnitId(), "", UserTypeEnum.TEACHER.getValue(), new Integer[] { 2 }, ""), new Pagination());
        // 用户组成员
        List<String> memberUserIdList = userRoleService.findUserIdsByRoleId(roleId);

        map.put("role", role);
        map.put("userList", userList);
        map.put("breadcrumbName", "授权");
        map.put("memberUsers", memberUserIdList);

        return "/system/user/roleDetail.ftl";
    }

    /**
     * 保存群组信息
     * 
     * @author cuimq
     * @param roleId
     * @param userIds
     * @param modelIds
     * @param name
     * @param description
     * @return
     */
    @ResponseBody
    @ControllerInfo("保存群组信息")
    @RequestMapping("/saveRole")
    public String saveRole(String roleId, String[] userIds, String[] modelIds, String name, String description,
            String[] allModelIds) {
        Role role = new Role();
        role.setDescription(description);
        role.setName(name);
        role.setUnitId(getLoginInfo().getUnitId());
        role.setIsActive("1");
        role.setRoleType(0);
        role.setModifyTime(new Date());
        role.setIsSystem(YesNoEnum.NO.getValue());

        if (StringUtils.isNotEmpty(roleId)) {// 修改
            role.setId(roleId);
            roleService.updateRole(role, userIds, removeModelIdPrefix(modelIds), removeModelIdPrefix(allModelIds));
        }
        else {// 新增
            role.setId(UuidUtils.generateUuid());
            roleService.insertRole(role, userIds, removeModelIdPrefix(modelIds));
        }
        return success("保存成功");
    }

    /**
     * 去除防止model和server id重复所加前缀
     * 
     * @author cuimq
     * @param modelIds
     * @return
     */
    private Integer[] removeModelIdPrefix(String[] modelIds) {
        if (null != modelIds && modelIds.length > 0) {
            List<Integer> modelIdList = new ArrayList<Integer>();
            for (int i = 0; i < modelIds.length; i++) {
                if (modelIds[i].length() > 2) {
                    modelIdList.add(Integer.valueOf(modelIds[i].substring(2)));
                }
            }
            return modelIdList.toArray(new Integer[modelIdList.size()]);
        }
        else {
            return null;
        }
    }

    /**
     * 模块树
     * 
     * @author cuimq
     * @return
     */
    @ResponseBody
    @ControllerInfo("获取权限模块树")
    @RequestMapping("/modelZtree")
    public String modelZtree(String roleId) {
        Integer unitClass = getLoginInfo().getUnitClass();

        List<Server> serverList = new ArrayList<Server>();
        String sections = "";
        // 学段
        if (null != unitClass && unitClass == UnitClassEnum.SCHOOL.getValue() && StringUtils.isEmpty(sections)) {
            sections = schoolRemoteService.findSectionsById(getLoginInfo().getUnitId());
        }

        // 查找单位订阅需授权应用
        serverList.addAll(serverAuthorizeService.listServers(getLoginInfo().getUnitId(), unitClass,
                UserTypeEnum.TEACHER.getValue(), new Integer[] { OrderTypeEnum.UNIT_PERSONAL_AUTH.getValue() }, "",
                null, "isNotNull"));

        // 系统订阅需授权应用
        serverList.addAll(serverService.findServerList(new Integer[] { OrderTypeEnum.SYSTEM.getValue() }, unitClass,
                AppStatusEnum.ONLINE.getValue(), sections, UserTypeEnum.TEACHER.getValue(), new Integer[] {},
                Integer.valueOf(YesNoEnum.YES.getValue()), "isNotNull"));

        // 系统订阅需授权应用

        // 查找用户组授权模块
        List<Integer> modelList = rolePermService.findModelIdsByRoleId(roleId);
        if (CollectionUtils.isEmpty(modelList)) {
            modelList = new ArrayList<Integer>();
        }

        JSONArray array = new JSONArray();
        Json json = new Json();
        json.put("pId", "");
        json.put("type", "");
        json.put("id", "00");
        json.put("name", "内置应用");
        json.put("title", "内置应用");
        json.put("open", true); // 此节点默认展开
        array.add(json);

        // json = new Json();
        // json.put("pId", "");
        // json.put("type", "");
        // json.put("id", "11");
        // json.put("name", "第三方应用");
        // json.put("title", "第三方应用");
        // json.put("open", true); // 此节点默认展开
        // array.add(json);

        final String serverIdPre = "11", firstModelPre = "22", secondModelPre = "33";// 防止modelId和serverId有重复

        if (CollectionUtils.isNotEmpty(serverList)) {
            for (Server server : serverList) {
                json = new Json();
                // if (server.getServerClass() == ServerClassEnum.INNER_PRODUCT.getValue()) {
                // json.put("pId", "00");
                // }
                // else if (server.getServerClass() == ServerClassEnum.AP_PRODUCT.getValue()) {
                // json.put("pId", "11");
                // }
                json.put("pId", "00");
                json.put("type", "");
                json.put("id", serverIdPre + server.getSubId());
                json.put("name", server.getName());
                json.put("title", server.getName());
                array.add(json);
            }

            // 一级模块
            Set<Integer> serverIdSet = EntityUtils.getSet(serverList, "subId");
            List<Model> firstModelList = modelService.findBySubSystemIdsAndParentIdsAndUnitClass(
                    serverIdSet.toArray(new Integer[serverIdSet.size()]), new Integer[] { -1 }, unitClass);

            if (CollectionUtils.isNotEmpty(firstModelList)) {
                for (Model model : firstModelList) {
                    json = new Json();
                    json.put("pId", serverIdPre + model.getSubSystem());
                    json.put("type", "");
                    json.put("id", firstModelPre + model.getId());
                    json.put("name", model.getName());
                    json.put("title", model.getName());
                    if (modelList.contains(model.getId())) {
                        json.put("checked", true);
                    }
                    array.add(json);
                }

                // 二级模块
                Set<Integer> parIdSet = EntityUtils.getSet(firstModelList, "id");
                List<Model> secondModelList = modelService.findBySubSystemIdsAndParentIdsAndUnitClass(
                        serverIdSet.toArray(new Integer[serverIdSet.size()]),
                        parIdSet.toArray(new Integer[parIdSet.size()]), unitClass);

                if (CollectionUtils.isNotEmpty(secondModelList)) {
                    for (Model model : secondModelList) {
                        json = new Json();
                        json.put("pId", firstModelPre + model.getParentId());
                        json.put("type", "");
                        json.put("id", secondModelPre + model.getId());
                        json.put("name", model.getName());
                        json.put("title", model.getName());
                        if (modelList.contains(model.getId())) {
                            json.put("checked", true);
                        }
                        array.add(json);
                    }
                }
            }

        }

        return success(JSONUtils.toJSONString(array));
    }
    // @RequestMapping("/role/{id}/permission/list/page")
    // public String showRolePermission(@PathVariable String id, HttpSession httpSession, ModelMap map) {
    // List<Permission> permissions = permissionService.findAll();
    // map.put("permissions", permissions);
    // return "/basedata/permission/permissionList.ftl";
    // }
    //
    // @ResponseBody
    // @RequestMapping("/role/ztree")
    // public String showZtree(HttpSession httpSession, ModelMap map) {
    // LoginInfo info = getLoginInfo(httpSession);
    // String unitId = info.getUnitId();
    // Unit unit = unitService.findOne(unitId);
    //
    // Specifications<Role> sp = new Specifications<Role>();
    // sp.addEq("unitId", info.getUnitId());
    // List<Role> roles = roleService.findAll(sp.getSpecification());
    //
    // List<User> users = userService.findByUnitId(unitId);
    //
    // userRoleService.findAll();
    // List<UserRole> urs = userRoleService
    // .findByIn("roleId", EntityUtils.getList(roles, "id").toArray(new String[0]));
    //
    // Map<String, List<String>> urm = new HashMap<String, List<String>>();
    // for (UserRole ur : urs) {
    // List<String> uls = urm.get(ur.getRoleId());
    // if (uls == null) {
    // uls = new ArrayList<String>();
    // urm.put(ur.getRoleId(), uls);
    // }
    // uls.add(ur.getUserId());
    // }
    //
    // map.put("roles", roles);
    // JSONArray array = new JSONArray();
    // Json json = new Json();
    // json.put("pId", "");
    // json.put("type", "unit");
    // json.put("id", unit.getId());
    // json.put("name", unit.getUnitName());
    // json.put("title", unit.getUnitName());
    // json.put("open", true); // 此节点默认展开
    // array.add(json);
    //
    // json = new Json();
    // json.put("pId", unitId);
    // json.put("type", "");
    // json.put("id", "00");
    // json.put("name", "[所有用户]");
    // json.put("title", "所有用户");
    // array.add(json);
    //
    // Map<String, User> um = new HashMap<String, User>();
    //
    // for (User user : users) {
    // json = new Json();
    // json.put("pId", "00");
    // json.put("type", "user");
    // json.put("id", user.getId());
    // json.put("name", user.getRealName());
    // json.put("title", user.getRealName());
    // array.add(json);
    // um.put(user.getId(), user);
    // }
    //
    // for (Role role : roles) {
    // json = new Json();
    // json.put("pId", unitId);
    // json.put("type", "role");
    // json.put("id", role.getId());
    // json.put("name", role.getName());
    // json.put("title", role.getName());
    // array.add(json);
    // List<String> uids = urm.get(role.getId());
    // if (uids != null) {
    // for (String uid : uids) {
    // User user = um.get(uid);
    // json = new Json();
    // json.put("pId", role.getId());
    // json.put("type", "user");
    // json.put("id", user.getId());
    // json.put("name", user.getRealName());
    // json.put("title", user.getRealName());
    // array.add(json);
    // }
    // }
    // }
    //
    // return success(JSONUtils.toJSONString(array));
    // }
}
