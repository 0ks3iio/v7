package net.zdsoft.bigdata.system.action;

import java.util.*;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.bigdata.system.entity.BgModule;
import net.zdsoft.bigdata.system.entity.BgRolePerm;
import net.zdsoft.bigdata.system.entity.BgUserRole;
import net.zdsoft.bigdata.system.service.BgAuthorityService;
import net.zdsoft.bigdata.system.service.BgModuleService;
import net.zdsoft.bigdata.system.service.BgRolePermService;
import net.zdsoft.bigdata.system.service.BgUserAuthService;
import net.zdsoft.bigdata.system.service.BgUserRoleService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping(value = "/bigdata/authority")
public class BgAuthorityController extends BigdataBaseAction {

    @Autowired
    private UserRemoteService userRemoteService;

    @Autowired
    private BgAuthorityService bgAuthorityService;

    @Autowired
    BgModuleService bgModuleService;

    @Autowired
    BgUserRoleService bgUserRoleService;

    @Autowired
    BgRolePermService bgRolePermService;

    @Autowired
    private BgUserAuthService bgUserAuthService;

    @RequestMapping("/index")
    public String index(ModelMap map) {
        return "/bigdata/system/authority.ftl";
    }

    @RequestMapping("/userquery")
    public String userquery(String username, String realname, ModelMap map) {
        map.put("isResult", 0);
        map.put("username", username);
        map.put("realname", realname);

        List<User> userList = new ArrayList<User>();
        if (StringUtils.isBlank(username) && StringUtils.isBlank(realname)) {
            map.put("userList", userList);
        } else {
            if (StringUtils.isNotBlank(username)) {
                User _user = User
                        .dc(userRemoteService.findByUsername(username));
                if (_user != null)
                    userList.add(_user);
            } else if (StringUtils.isNotBlank(realname)) {
                userList = User.dt(userRemoteService.findByRealName(realname));
            }
            map.put("isResult", 1);
            map.put("userList", userList);
        }
        return "/bigdata/system/userrole/userQuery.ftl";
    }

    @RequestMapping("/userModuleDetail")
    public String userModuleDetail(String userId, Integer userType, ModelMap map) {
        map.put("userId", userId);
        map.put("userType", userType);
        return "/bigdata/system/userrole/userModuleDetail.ftl";
    }

    @SuppressWarnings("deprecation")
    @ResponseBody
    @ControllerInfo("获取权限模块树")
    @RequestMapping("/moduleZtree")
    public String modelZtree(String userId, Integer userType) {
        Set<String> moduleIds = new HashSet<String>();
        Set<String> roleIds = new HashSet<String>();

        List<BgUserRole> userRoleList = bgUserRoleService
                .findUserRoleListByUserId(userId);
        for (BgUserRole userRole : userRoleList) {
            if (userRole.getType() == BgUserRole.TYPE_MODULE) {
                moduleIds.add(userRole.getModuleId());
            }
            if (userRole.getType() == BgUserRole.TYPE_ROLE) {
                roleIds.add(userRole.getRoleId());
            }
        }
        // 查找用户组授权模块
        List<BgRolePerm> modelList = bgRolePermService.findByIn("roleId",
                roleIds.toArray(new String[0]));
        for (BgRolePerm rolePerm : modelList) {
            moduleIds.add(rolePerm.getModuleId());
        }
        JSONArray array = new JSONArray();
        Json json = null;
        boolean isBackgroudUser = bgUserAuthService.isBackgroundUser(userId,
                userType);
        boolean topAdmin = false;
        if (userType == User.USER_TYPE_TOP_ADMIN)
            topAdmin = true;
        // 顶级管理员特殊处理
        if (topAdmin) {
            moduleIds.add("20000000000000000000000000000001");
            moduleIds.add("20000000000000000000000000000003");
        }
        if (isBackgroudUser)
            userType = Integer.valueOf(BgModule.USER_TYPE_BACKGROUND);
        List<BgModule> allModelList = bgModuleService.findAllModuleList();
        Map<String, BgModule> dirModuleMap = new HashMap<String, BgModule>();
        if (CollectionUtils.isNotEmpty(allModelList)) {
            for (BgModule model : allModelList) {
                if (model.getType().equals("dir")) {
                    if (!moduleIds.contains(model.getId()))
                        dirModuleMap.put(model.getId(), model);
                }
            }

            for (BgModule model : allModelList) {
                if (!model.getUserType().contains(String.valueOf(userType))) {
                    continue;
                }
                if (!topAdmin && model.getFixed() == 1
                        && !model.getType().equals("dir")) {
                    continue;
                }
                json = new Json();
                json.put("pId", model.getParentId());
                json.put("type", model.getType());
                json.put("id", model.getId());
                json.put("name", model.getName());
                json.put("title", model.getName());
                if (moduleIds.contains(model.getId())) {
                    if (!moduleIds.contains(model.getParentId())) {
                        if (dirModuleMap.containsKey(model.getParentId())) {
                            BgModule parentModule = dirModuleMap.get(model.getParentId());
                            Json dirJson = new Json();
                            dirJson.put("pId", parentModule.getParentId());
                            dirJson.put("type", parentModule.getType());
                            dirJson.put("id", parentModule.getId());
                            dirJson.put("name", parentModule.getName());
                            dirJson.put("title", parentModule.getName());
                            dirJson.put("checked", true);
                            array.add(dirJson);
                            dirModuleMap.remove(model.getParentId());
                        }
                    }
                    json.put("checked", true);
                    array.add(json);
                } else {
                    if (model.getCommon() == 1
                            && !model.getType().equals("dir")) {
                        if (!moduleIds.contains(model.getParentId())) {
                            if (dirModuleMap.containsKey(model.getParentId())) {
                                BgModule parentModule = dirModuleMap.get(model.getParentId());
                                Json dirJson = new Json();
                                dirJson.put("pId", parentModule.getParentId());
                                dirJson.put("type", parentModule.getType());
                                dirJson.put("id", parentModule.getId());
                                dirJson.put("name", parentModule.getName());
                                dirJson.put("title", parentModule.getName());
                                dirJson.put("checked", true);
                                array.add(dirJson);
                                dirModuleMap.remove(model.getParentId());
                            }
                        }
                        json.put("checked", true);
                        array.add(json);
                    }
                }
            }
        }

        return success(JSONUtils.toJSONString(array));
    }

}
