package net.zdsoft.bigdata.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.datasource.jdbc.JdbcDatabaseAdapter;
import net.zdsoft.bigdata.system.entity.BgModule;
import net.zdsoft.bigdata.system.entity.BgRolePerm;
import net.zdsoft.bigdata.system.entity.BgUserRole;
import net.zdsoft.bigdata.system.service.BgAuthorityService;
import net.zdsoft.bigdata.system.service.BgModuleService;
import net.zdsoft.bigdata.system.service.BgRolePermService;
import net.zdsoft.bigdata.system.service.BgUserRoleService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bgAuthorityService")
public class BgAuthorityServiceImpl implements BgAuthorityService {

    @Autowired
    private BgRolePermService bgRolePermService;

    @Autowired
    private BgModuleService bgModuleService;

    @Autowired
    private BgUserRoleService bgUserRoleService;

    @Autowired
    private OptionService optionService;

    @Override
    public List<BgModule> getAuthorityModuleList(String userId,
                                                 String userType, boolean isAdmin) {
        int style = 1;
        OptionDto styleDto = optionService.getAllOptionParam("pageStyle");
        if (styleDto != null) {
            style = Integer.valueOf(styleDto.getFrameParamMap()
                    .get("style"));
        }


        List<BgModule> myValidModuleList = new ArrayList<BgModule>();
        List<BgModule> resultList = new ArrayList<BgModule>();
        List<BgModule> moduleList = bgModuleService.findAllModuleList();
        Set<String> roleIds = new HashSet<String>();
        Set<String> moduleIds = new HashSet<String>();
        List<BgUserRole> userRoleList = bgUserRoleService
                .findUserRoleListByUserId(userId);
        for (BgUserRole userRole : userRoleList) {
            if (BgUserRole.TYPE_ROLE == userRole.getType()) {
                roleIds.add(userRole.getRoleId());
            }
            if (BgUserRole.TYPE_MODULE == userRole.getType()) {
                moduleIds.add(userRole.getModuleId());
            }
        }
        List<BgRolePerm> rolePermList = new ArrayList<BgRolePerm>();
        if (roleIds.size() > 0)
            rolePermList = bgRolePermService.findRolePermListByRoleIds(roleIds
                    .toArray(new String[0]));
        for (BgRolePerm rolePerm : rolePermList) {
            moduleIds.add(rolePerm.getModuleId());
        }
        Map<String, List<BgModule>> childModuleMap = new HashMap<String, List<BgModule>>();
        for (BgModule module : moduleList) {
            if (module.getMark() == 0)
                continue;
            if (!isAdmin) {
                if (module.getFixed() == 1 && module.getType().equals("item"))
                    continue;
            }
            if (!module.getUserType().contains(userType))
                continue;
            //增加页面模式的过滤
            if (!BgModule.USER_TYPE_BACKGROUND.equals(userType) && style != module.getStyle()) {
                continue;
            }
            if (module.getCommon() == 1) {
                myValidModuleList.add(module);
            } else {
                if (moduleIds.contains(module.getId())) {
                    myValidModuleList.add(module);
                } else {
                    continue;
                }
            }
            // 如果目录下没有模块就不显示目录
            if (module.getType().equals("item")) {
                List<BgModule> childModuleList = childModuleMap.get(module
                        .getParentId());
                if (CollectionUtils.isEmpty(childModuleList))
                    childModuleList = new ArrayList<BgModule>();
                childModuleList.add(module);
                childModuleMap.put(module.getParentId(), childModuleList);
            }
        }
        for (BgModule module : myValidModuleList) {
            if (module.getType().equals("dir")) {
                if (childModuleMap.containsKey(module.getId())) {
                    resultList.add(module);
                }
            } else {
                resultList.add(module);
            }
        }
        return resultList;
    }
}
