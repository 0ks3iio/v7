/* 
 * @(#)ServerAuthorizeAction.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.action.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.dto.UnitDto;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.dto.common.TabMenuDto;
import net.zdsoft.system.dto.server.ServerDto;
import net.zdsoft.system.dto.server.UnitAuthorizeDto;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.server.ServerAuthorize;
import net.zdsoft.system.enums.YesNoEnum;
import net.zdsoft.system.enums.server.AppStatusEnum;
import net.zdsoft.system.enums.server.OrderTypeEnum;
import net.zdsoft.system.service.server.ServerAuthorizeService;
import net.zdsoft.system.service.server.ServerService;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午10:12:20 $
 */
@Controller
@RequestMapping("/system/serverAuthorize")
public class ServerAuthorizeAction extends BaseAction {

    @Autowired
    private ServerAuthorizeService serverAuthorizeService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private ServerService serverService;

    @RequestMapping("/index")
    public String serverAuthorizeHome(String tabCode, ModelMap map) {
        String url = "/system/serverAuthorize/tab";
        if (StringUtils.isNotEmpty(tabCode)) {
            url += "?tabCode=" + tabCode;
        }
        map.put("url", url);
        return "/system/common/home.ftl";
    }

    /**
     * 
     * @author cuimq
     * @return
     */
    @RequestMapping("/tab")
    public String serverAuthorizeTab(String tabCode, ModelMap map) {
        List<TabMenuDto> tabList = new ArrayList<TabMenuDto>();
        tabList.add(new TabMenuDto("按应用授权", "/system/serverAuthorize/server/serverAuthorizeList", "serverAuthorize"));
        tabList.add(new TabMenuDto("按单位授权", "/system/serverAuthorize/unitAuthorize/unitList", "unitServerAuthorize"));

        map.put("tabCode", tabCode);
        map.put("tabList", tabList);
        return "/system/common/tabHome.ftl";
    }

    /**
     * 单位授权列表页面
     * 
     * @author cuimq
     * @return
     */
    @ControllerInfo("进入单位授权列表页面")
    @RequestMapping("/unitAuthorize/unitList")
    public String unitAuthorizeView(String region, String unitName, HttpServletRequest request, String regionName,
            ModelMap map) {
        List<UnitAuthorizeDto> unitAuthorizeDtoList = new ArrayList<UnitAuthorizeDto>();

        // 查找单位列表
        if (StringUtils.isNotEmpty(unitName)) {
            try {
                unitName = URLDecoder.decode(unitName, "utf-8");
            }
            catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(regionName)) {
            try {
                regionName = URLDecoder.decode(regionName, "utf-8");
            }
            catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Pagination page = createPagination();
        String unitResult = unitRemoteService.findByRegionAndUnitName(region, unitName, SUtils.s(page));
        JSONObject unitResultJson = JSONObject.parseObject(unitResult);
        page.setMaxRowCount(unitResultJson.getIntValue("count"));
        List<UnitDto> unitList = SUtils.dt(unitResultJson.getString("data"), new TR<List<UnitDto>>() {
        });

        // 查找单位订阅应用信息
        if (CollectionUtils.isNotEmpty(unitList)) {
            Set<String> unitIdSet = EntityUtils.getSet(unitList, "unitId");

            Map<String, List<Server>> unitIdAndAuthorizeServerMap = serverAuthorizeService
                    .getUnitIdAndAuthorizeServerMap(unitIdSet.toArray(new String[unitIdSet.size()]));

            for (UnitDto unit : unitList) {
                unitAuthorizeDtoList.add(new UnitAuthorizeDto(unit, unitIdAndAuthorizeServerMap.get(unit.getUnitId())));
            }
        }

        map.put("unitAuthorizeList", unitAuthorizeDtoList);
        map.put("pagination", page);
        map.put("regionCode", region == null ? "" : region);
        map.put("regionName", regionName == null ? "" : regionName);
        map.put("unitName", unitName == null ? "" : unitName);
        return "/system/serverAuthorize/unitAuthorizeList.ftl";
    }

    /**
     * 修改单位授权
     * 
     * @author cuimq
     * @param unitId
     * @param unitClass
     * @param map
     * @return
     */
    @ResponseBody
    @ControllerInfo("进入修改单位授权页面")
    @RequestMapping("/unitAuthorize/modifyUnitAuthorize")
    public String modifyUnitAuthorizeView(String unitId, int unitClass) {
        // 可授权应用列表,不考虑学段条件
        List<Server> serverList = serverService.findServerList(
                new Integer[] { OrderTypeEnum.UNIT_PERSONAL_AUTH.getValue(),
                        OrderTypeEnum.UNIT_PERSONAL_NO_AUTH.getValue() }, unitClass, AppStatusEnum.ONLINE.getValue(),
                "", 0, null, YesNoEnum.YES.getValue(), null);

        List<ServerDto> serverDtoList = new ArrayList<ServerDto>();
        if (CollectionUtils.isNotEmpty(serverList)) {
            // 单位已订阅应用id
            List<Integer> authorizeServerIds = serverAuthorizeService.listServerIds(unitId);

            // 标识应用已订阅
            for (Server server : serverList) {
                ServerDto serverDto = new ServerDto();
                serverDto.setId(server.getId());
                serverDto.setName(server.getName());
                if (authorizeServerIds.contains(server.getId())) {
                    serverDto.setHasAuthorized(YesNoEnum.YES.getValue());
                }
                serverDtoList.add(serverDto);
            }
        }

        JSONObject result = new JSONObject();
        result.put("serverList", serverDtoList);
        return result.toJSONString();
    }

    /**
     * 保存单位应用订阅信息
     * 
     * @author cuimq
     * @param serverIds
     * @param unitId
     * @return
     */
    @ResponseBody
    @ControllerInfo("修改单位{unitId}应用授权信息")
    @RequestMapping("/unitAuthorize/{unitId}/saveUnitAuthorize")
    public String saveUnitAuthorize(String[] serverIds, @PathVariable String unitId) {

        List<ServerAuthorize> serverAuthorizeList = new ArrayList<ServerAuthorize>();
        List<String> newServerIds = Arrays.stream(serverIds).filter(Objects::nonNull)
                .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(newServerIds)) {
            Date now = new Date();
            for (String serverId : newServerIds) {
                ServerAuthorize serverAuthorize = new ServerAuthorize();
                serverAuthorize.setUnitId(unitId);
                serverAuthorize.setServerId(Integer.valueOf(serverId));
                serverAuthorize.setId(UuidUtils.generateUuid());
                serverAuthorize.setIsDeleted(YesNoEnum.NO.getValue());
                serverAuthorize.setCreationTime(now);
                serverAuthorizeList.add(serverAuthorize);
            }

        }

        serverAuthorizeService.updateUnitAuthorize(unitId, serverAuthorizeList);

        return success("修改成功");
    }

    /**
     * 应用授权列表
     * 
     * @author cuimq
     * @param serverId
     * @param unitType
     * @param authorizeStatus
     * @param region
     * @param map
     * @return
     */
    @ControllerInfo("进入应用授权列表页面")
    @RequestMapping("/server/serverAuthorizeList")
    public String serverAuthorizeView(Integer serverId, String unitType, Integer authorizeStatus, String region,
            String regionName, ModelMap map) {

        if (StringUtils.isNotEmpty(regionName)) {
            try {
                regionName = URLDecoder.decode(regionName, "utf-8");
            }
            catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // 应用列表
        List<Server> serverList = serverService.findByOrderTypeAndStatus(new Integer[] {
                OrderTypeEnum.UNIT_PERSONAL_AUTH.getValue(), OrderTypeEnum.UNIT_PERSONAL_NO_AUTH.getValue() },
                AppStatusEnum.ONLINE.getValue());

        List<UnitAuthorizeDto> unitAuthorizeDtoList = new ArrayList<UnitAuthorizeDto>();

        Pagination page = createPagination();

        if (null != serverId) {
            // 单位类型
            String[] unitTypeAry = unitType.split(",");
            Integer[] unitClass = new Integer[unitTypeAry.length];
            for (int i = 0; i < unitTypeAry.length; i++) {
                unitClass[i] = StringUtils.isNotEmpty(unitTypeAry[i]) ? Integer.valueOf(unitTypeAry[i]) : 0;
            }

            String result = "";
            if (authorizeStatus == YesNoEnum.YES.getValue()) {// 已授权
                result = unitRemoteService.findAutorizeServerUnit(unitClass, region, serverId, SUtils.s(page));
            }
            else if (authorizeStatus == YesNoEnum.NO.getValue()) {// 未授权
                result = unitRemoteService.findUnAutorizeServerUnit(unitClass, region, serverId, SUtils.s(page));
            }

            JSONObject unitResultJson = JSONObject.parseObject(result);
            if (null != unitResultJson) {
                page.setMaxRowCount(unitResultJson.getIntValue("count"));
                List<UnitDto> unitList = SUtils.dt(unitResultJson.getString("data"), new TR<List<UnitDto>>() {
                });

                if (CollectionUtils.isNotEmpty(unitList)) {
                    for (UnitDto unit : unitList) {
                        unitAuthorizeDtoList.add(new UnitAuthorizeDto(unit, authorizeStatus));
                    }
                }
            }

        }

        map.put("serverList", serverList);
        map.put("unitAuthorizeList", unitAuthorizeDtoList);
        map.put("authorizeStatus", authorizeStatus);
        map.put("pagination", page);
        map.put("serverId", serverId);
        map.put("authorizeStatus", authorizeStatus);
        map.put("regionCode", region == null ? "" : region);
        map.put("regionName", regionName == null ? "" : regionName);
        return "/system/serverAuthorize/serverAuthorizeList.ftl";
    }

    /**
     * 批量授权/批量取消授权
     * 
     * @author cuimq
     * @param unitIds
     * @param serverId
     * @param authorizeStatus
     * @return
     */
    @ResponseBody
    @ControllerInfo("批量修改应用{serverId}与单位的订阅信息")
    @RequestMapping("/server/{serverId}/batchAuthorize")
    public String batchAuthorize(String[] unitIds, @PathVariable Integer serverId, int authorizeStatus) {
        // 授权/取消授权
        authorize(unitIds, serverId, authorizeStatus);

        return success("操作成功");
    }

    /**
     * 全部授权/全部取消授权
     * 
     * @author cuimq
     * @param serverId
     * @param unitType
     * @param authorizeStatus
     * @param region
     * @return
     */
    @ResponseBody
    @ControllerInfo("全部修改应用{serverId}与单位的订阅信息")
    @RequestMapping("/server/{serverId}/allAuthorize")
    public String allAuthorize(@PathVariable Integer serverId, String unitType, int authorizeStatus, String region) {

        // 单位类型
        String[] unitTypeAry = unitType.split(",");
        Integer[] unitClass = new Integer[unitTypeAry.length];
        for (int i = 0; i < unitTypeAry.length; i++) {
            unitClass[i] = Integer.valueOf(unitTypeAry[i]);
        }

        if (authorizeStatus == YesNoEnum.NO.getValue()) {// 取消已授权单位
            serverAuthorizeService.removeByServerIdAndUnitCondition(serverId, region, unitClass);
        }
        else if (authorizeStatus == YesNoEnum.YES.getValue()) {// 授权未授权单位
            // 未授权单位
            String result = unitRemoteService.findUnAutorizeServerUnit(unitClass, region, serverId, "");

            JSONObject unitResultJson = JSONObject.parseObject(result);
            List<UnitDto> unitList = SUtils.dt(unitResultJson.getString("data"), new TR<List<UnitDto>>() {
            });

            if (CollectionUtils.isNotEmpty(unitList)) {
                List<ServerAuthorize> serverAuthorizeList = new ArrayList<ServerAuthorize>();
                Date now = new Date();
                for (UnitDto unit : unitList) {
                    ServerAuthorize serverAuthorize = new ServerAuthorize();
                    serverAuthorize.setCreationTime(now);
                    serverAuthorize.setId(UuidUtils.generateUuid());
                    serverAuthorize.setServerId(serverId);
                    serverAuthorize.setUnitId(unit.getUnitId());
                    serverAuthorizeList.add(serverAuthorize);
                }

                serverAuthorizeService.saveServerAuthorizes(serverAuthorizeList);
            }
        }

        return success("操作成功");
    }

    /**
     * 授权/取消授权
     * 
     * @author cuimq
     * @param unitIds
     * @param serverId
     * @param authorizeStatus
     */
    private void authorize(String[] unitIds, Integer serverId, int authorizeStatus) {
        if (authorizeStatus == YesNoEnum.YES.getValue()) {// 授权
            List<ServerAuthorize> serverAuthorizeList = new ArrayList<ServerAuthorize>();
            Date now = new Date();
            for (String unitId : unitIds) {
                ServerAuthorize serverAuthorize = new ServerAuthorize();
                serverAuthorize.setCreationTime(now);
                serverAuthorize.setId(UuidUtils.generateUuid());
                serverAuthorize.setServerId(serverId);
                serverAuthorize.setUnitId(unitId);
                serverAuthorizeList.add(serverAuthorize);
            }

            serverAuthorizeService.saveServerAuthorizes(serverAuthorizeList);

        }
        else if (authorizeStatus == YesNoEnum.NO.getValue()) {// 取消授权
            serverAuthorizeService.removeServerAuthorizes(serverId, unitIds);
        }
    }
}
