/* 
 * @(#)DeveloperManageAction.java    Created on 2017-3-6
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.action.openapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.remote.openapi.dto.DeveloperDto;
import net.zdsoft.remote.openapi.dto.EntityDto;
import net.zdsoft.remote.openapi.dto.InterfaceDto;
import net.zdsoft.remote.openapi.enums.YesNoEnum;
import net.zdsoft.remote.openapi.remote.service.DeveloperRemoteService;
import net.zdsoft.remote.openapi.remote.service.OpenApiEntityTicketRemoteService;
import net.zdsoft.remote.openapi.remote.service.OpenApiInterfaceCountRemoteService;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ServerRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-6 下午03:35:21 $
 */
@Controller
@RequestMapping("/system/developer/")
public class DeveloperManageAction extends BaseAction {
    @Autowired
    @Lazy
    private DeveloperRemoteService developerRemoteService;
    @Autowired
    private ServerRemoteService serverRemoteService;
    @Autowired
	private OpenApiInterfaceCountRemoteService openApiInterfaceCountRemoteService;
    @Autowired
    private OpenApiEntityTicketRemoteService openApiEntityTicketRemoteService;

    @RequestMapping("/index")
    public String developerManageIndex(ModelMap map) {
        String url = "/system/developer/manage";
        map.put("url", url);
        return "/system/common/home.ftl";
    }
    
    /**
     * 开发者管理
     * 
     * @author chicb
     * @param map
     * @return
     */
    @RequestMapping("/manage")
    public String shwoManage(ModelMap map) {
        List<DeveloperDto> developerDtos = developerRemoteService.getDevelperSimpleInfos();
        if (CollectionUtils.isNotEmpty(developerDtos)) {
            List<String> devIds = EntityUtils.getList(developerDtos, DeveloperDto::getId);
            List<Server> servers = serverRemoteService.findAppsByDevIds(devIds
                    .toArray(new String[devIds.size()]));
            Map<String, List<String>> appsNumByDevIds = new HashMap<String, List<String>>();
            for (Server app : servers) {
                String devId = app.getDevId();
                List<String> appNames = appsNumByDevIds.get(devId);
                if (appNames == null) {
                    appNames = new ArrayList<String>();
                }
                appNames.add(app.getName());
                appsNumByDevIds.put(devId, appNames);
            }
            
            addServerNumToDeveloperDto(developerDtos, appsNumByDevIds);
            map.put("developerDtos", developerDtos);
        }
        return "/openapi/system/developer/developerManage.ftl";
    }
    
    /**
     * 重置密码
     * 
     * @author chicb
     * @return
     */
    @ResponseBody
    @RequestMapping("/defaultPw")
    public String defaultPw(String developerId) {
        if (YesNoEnum.YES.getValue() == developerRemoteService.defaultPw(developerId)) {
            return success("success");
        }
        return error("error");
    }

    @ResponseBody
    @RequestMapping("/modifyUnitName")
    public String modifyDeveloperUnitName(String developerId, String unitName) {
        if (!Validators.isName(unitName, 4, 60)) {
            return error("2-30个中文字符");
        }
        if (1 == developerRemoteService.modifyDeveloperUnitName(developerId, unitName)) {
            return returnSuccess();
        }
        return error("服务开小差，请刷新后重试...");
    }

    @ResponseBody
    @RequestMapping("/modifyIps")
    public String modifyDeveloperIps(String developerId, String ips) {
        if (1 == developerRemoteService.modifyDeveloperIps(developerId, ips)) {
            return returnSuccess();
        }
        return error("服务开小差，请刷新后重试...");
    }


    /**
     * @author chicb
     * @param developerDtos
     * @param appsNumByDevIds
     */
    private void addServerNumToDeveloperDto(List<DeveloperDto> developerDtos, Map<String, List<String>> appsNumByDevIds) {
        if (MapUtils.isEmpty(appsNumByDevIds)) {
            return;
        }
        for (DeveloperDto d : developerDtos) {
            List<String> list = appsNumByDevIds.get(d.getId());
            d.setServerNum(0);
            if (CollectionUtils.isNotEmpty(list)) {
                d.setServerNum(list.size());
                d.setServerNames(StringUtils.join(list, "<br>"));
            }
        }
    }
}
