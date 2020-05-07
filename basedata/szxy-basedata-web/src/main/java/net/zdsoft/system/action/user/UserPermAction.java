/* 
 * @(#)UserPermAction.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.action.user;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
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
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.dto.common.TabMenuDto;
import net.zdsoft.system.dto.user.UserPermDto;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.entity.user.UserPerm;
import net.zdsoft.system.enums.YesNoEnum;
import net.zdsoft.system.enums.server.AppStatusEnum;
import net.zdsoft.system.enums.server.OrderTypeEnum;
import net.zdsoft.system.enums.user.UserTypeEnum;
import net.zdsoft.system.service.server.ModelService;
import net.zdsoft.system.service.server.ServerAuthorizeService;
import net.zdsoft.system.service.server.ServerService;
import net.zdsoft.system.service.user.RolePermService;
import net.zdsoft.system.service.user.UserPermService;
import net.zdsoft.system.service.user.UserRoleService;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:23:54 $
 */
@Controller
@RequestMapping("/system/userPerm")
public class UserPermAction extends BaseAction {

    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserPermService userPermService;
    @Autowired
    private ServerAuthorizeService serverAuthorizeService;
    @Autowired
    private RolePermService rolePermService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private ServerService serverService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;

    @RequestMapping("/index")
    public String userHome(String tabCode, ModelMap map) {
        String url = "/system/userPerm/tab";
        if (StringUtils.isNotEmpty(tabCode)) {
            url += "?tabCode=" + tabCode;
        }
        map.put("url", url);
        return "/system/common/home.ftl";
    }

    /**
     * 用户管理首页
     * 
     * @author cuimq
     * @return
     */
    @RequestMapping("/tab")
    public String userTab(String tabCode, ModelMap map) {
        List<TabMenuDto> tabList = new ArrayList<TabMenuDto>();
        tabList.add(new TabMenuDto("用户授权", "/system/userPerm/userList", "userPerm"));
        tabList.add(new TabMenuDto("用户组授权", "/system/role/roleList", "rolePerm"));

        map.put("tabCode", tabCode);
        map.put("tabList", tabList);
        return "/system/common/tabHome.ftl";
    }

    /**
     * 用户列表
     * 
     * @author cuimq
     * @param name
     * @param map
     * @return
     */
    @ControllerInfo("进入用户授权列表页面")
    @RequestMapping("/userList")
    public String userList(String realName, ModelMap map) {
        Pagination page = createPagination();
        if (StringUtils.isNotEmpty(realName)) {
            try {
                realName = URLDecoder.decode(realName, "utf-8");
            }
            catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // 查找用户列表
        List<User> userList = User.dt(userRemoteService.findByUnitIdAndRealNameAndOwnerTypeAndUserType(getLoginInfo()
                .getUnitId(), realName, UserTypeEnum.TEACHER.getValue(), new Integer[] { 2 }, SUtils.s(page)), page);

        List<UserPermDto> userPermDtoList = new ArrayList<UserPermDto>();
        if (CollectionUtils.isNotEmpty(userList)) {
            Set<String> userIdSet = EntityUtils.getSet(userList, "id");

            // 用户所属用户组
            Map<String, List<Role>> userIdAndRoles = userRoleService.findUserIdAndRoles(userIdSet
                    .toArray(new String[userIdSet.size()]));

            for (User user : userList) {
                userPermDtoList.add(new UserPermDto(user, userIdAndRoles.get(user.getId())));
            }
        }

        map.put("realName", realName);
        map.put("pagination", page);
        map.put("userPermList", userPermDtoList);

        return "/system/user/userPermList.ftl";
    }

    /**
     * 模块树
     * 
     * @author cuimq
     * @return
     */
    @ResponseBody
    @ControllerInfo("获取用户授权模块树")
    @RequestMapping("/userPermZtree")
    public String modelZtree(String userId) {
        Integer unitClass = getLoginInfo().getUnitClass();
        List<Server> serverList = new ArrayList<Server>();
        String sections = "";
        // 学段
        if (null != unitClass && unitClass == UnitClassEnum.SCHOOL.getValue() && StringUtils.isEmpty(sections)) {
            sections = schoolRemoteService.findSectionsById(getLoginInfo().getUnitId());
        }

        // 查找单位订阅需授权应用
        serverList.addAll(serverAuthorizeService.listServers(getLoginInfo().getUnitId(), unitClass,
                UserTypeEnum.TEACHER.getValue(), new Integer[] { OrderTypeEnum.UNIT_PERSONAL_AUTH.getValue() },
                sections, YesNoEnum.YES.getValue(), "isNotNull"));

        // 系统订阅需授权应用
        serverList.addAll(serverService.findServerList(new Integer[] { OrderTypeEnum.SYSTEM.getValue() }, unitClass,
                AppStatusEnum.ONLINE.getValue(), sections, UserTypeEnum.TEACHER.getValue(), new Integer[] {},
                Integer.valueOf(YesNoEnum.YES.getValue()), "isNotNull"));

        // 查找用户授权模块
        List<Integer> userPermModelIds = userPermService.findModelIdsByUserId(userId);
        if (CollectionUtils.isEmpty(userPermModelIds)) {
            userPermModelIds = new ArrayList<Integer>();
        }

        // 查找用户所在用户组授权模块
        List<Integer> rolePermModelIds = rolePermService.findModelIdsByUserIdAndIsSystem(userId,
                YesNoEnum.NO.getValue());
        if (CollectionUtils.isEmpty(rolePermModelIds)) {
            rolePermModelIds = new ArrayList<Integer>();
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
            Set<Integer> serverIdSet = EntityUtils.getSet(serverList, Server::getSubId);
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
                    if (userPermModelIds.contains(model.getId())) {
                        json.put("checked", true);
                    }
                    if (rolePermModelIds.contains(model.getId())) {
                        json.put("checked", true);
                        json.put("chkDisabled", true);
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
                        if (userPermModelIds.contains(model.getId())) {
                            json.put("checked", true);
                        }
                        if (rolePermModelIds.contains(model.getId())) {
                            json.put("checked", true);
                            json.put("chkDisabled", true);
                        }
                        array.add(json);
                    }
                }
            }

        }

        return success(JSONUtils.toJSONString(array));
    }

    /**
     * 修改用户授权
     * 
     * @author cuimq
     * @param userId
     * @param modelIds
     * @param allModelIds
     * @return
     */
    @ResponseBody
    @ControllerInfo("修改用户{userId}权限信息")
    @RequestMapping("/{userId}/saveUserPerm")
    public String saveUserPerm(@PathVariable String userId, String[] modelIds, String[] allModelIds) {
        List<UserPerm> userPermList = new ArrayList<UserPerm>();
        if (null != modelIds && modelIds.length > 0) {
            Integer[] newModelIds = removeModelIdPrefix(modelIds);
            for (Integer modelId : newModelIds) {
                UserPerm userPerm = new UserPerm();
                userPerm.setId(UuidUtils.generateUuid());
                userPerm.setModelId(modelId);
                userPerm.setUserId(userId);
                userPermList.add(userPerm);
            }
        }
        Integer[] newAllModelIds = removeModelIdPrefix(allModelIds);
        if (null != newAllModelIds && newAllModelIds.length > 0) {
            userPermService.saveUserPerm(userPermList, userId, newAllModelIds);
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
}
