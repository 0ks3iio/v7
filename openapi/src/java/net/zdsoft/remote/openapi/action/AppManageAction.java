/* 
 * @(#)AppManageAction.java    Created on 2017-2-20
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.action;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.enums.AppStatusEnum;
import net.zdsoft.remote.openapi.enums.OrderTypeEnum;
import net.zdsoft.remote.openapi.enums.ServerClassEnum;
import net.zdsoft.remote.openapi.enums.UnitClassEnum;
import net.zdsoft.remote.openapi.enums.UserTypeEnum;
import net.zdsoft.system.remote.dto.ServerRemoteDto;
import net.zdsoft.system.remote.service.AppManageRemoteService;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-2-20 下午05:22:02 $
 */
@Controller
@RequestMapping(value = { "/appManage" })
public class AppManageAction extends OpenApiBaseAction {

    private static final int IMAGE_MAX_SIZE = 1024 * 1024;
    private static final int IMAGE_WIDTH = 512;
    private static final int IMAGE_HEIGHT = 512;
    private static Set<String> imageSuffix;
    static {
        imageSuffix = new HashSet<String>();
        imageSuffix.add("png");
        imageSuffix.add("jpg");
        imageSuffix.add("jpeg");
        imageSuffix.add("bmp");
        imageSuffix.add("gif");
    }
    @Autowired
    private AppManageRemoteService appManageRemoteService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Autowired
    private ModelRemoteService modelRemoteService;
    @Autowired
    private ServerRemoteService serverRemoteService;

    /**
     * 应用列表
     * 
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = { "/appList" })
    public String showAppList(Model model) {
        Developer developer = getDeveloper();
        String devId = developer.getId();
        List<ServerRemoteDto> apps = decorateApps(appManageRemoteService.getAppsByDevId(devId));
        model.addAttribute("apps", apps);
        return "/openapi/appManage/appList.ftl";
    }

    /**
     * 统计数据页面
     * 
     * @param id
     * @return
     */
    @RequestMapping("/countData")
    public String countData(String id, Model model) {
        ServerRemoteDto app = appManageRemoteService.getAppBySystemId(id);
        String fileUrl = sysOptionRemoteService.findValue(OpenApiConstants.FILE_URL);
        app.setFullIcon(fixDoamin(app.getIconUrl(), fileUrl));
        app.setTimeStr(DateUtils.date2StringByDay(getDateByAppStatus(app.getStatus(), app)));
        model.addAttribute("app", app);
        return "/openapi/appManage/countData.ftl";
    }

    /**
     * 增加应用页面
     * 
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = { "/addApp" })
    public String addApp(Model model) {
        model.addAttribute("devId", getDeveloper().getId());
        return "/openapi/appManage/addApp.ftl";
    }

    /**
     * 删除应用
     * 
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/delApp" })
    public String delApp(String id) {
        appManageRemoteService.removeApp(id);
        return returnSuccess();
    }

    /**
     * 修改应用页面
     * 
     * @return
     */
    @RequestMapping(value = { "/modifyApp" })
    public String modifyApp(String id, int isLook, Model model) {
        ServerRemoteDto app = appManageRemoteService.getAppBySystemId(id);
        setUnitTypeAndUserTypeAndSectionsArray(app);
        String fileUrl = sysOptionRemoteService.findValue(OpenApiConstants.FILE_URL);
        app.setFullIcon(fixDoamin(app.getIconUrl(), fileUrl));
        model.addAttribute("app", app);
        model.addAttribute("isLook", isLook);// 是否是只查看
        return "/openapi/appManage/modifyApp.ftl";
    }

    /**
     * 提交审核
     * 
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/submitAudit" })
    public String submitAudit(String id) {
        appManageRemoteService.updateAppStatus(AppStatusEnum.AUDIT.getValue(), id);
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping(value = { "/checkAppName" })
    public String checkAppName(String appName) {
        int appCount = appManageRemoteService.getAppCountByName(appName);
        if (appCount > 0) {
            return returnError();
        }
        return returnSuccess();
    }

    /**
     * 上传应用图片
     * 
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/upload" }, produces = "text/json;charset=UTF-8")
    public String uploadAppImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return error("请选择文件");
        }
        String originalFileName = file.getOriginalFilename();// 文件原始名字
        long fileSize = file.getSize();// 字节
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String path = getUploadPath();// 相对路径
        String fileName = UuidUtils.generateUuid() + "." + suffix;
        String filePath = sysOptionRemoteService.findValue(OpenApiConstants.FILE_PATH);
        String fileUrl = sysOptionRemoteService.findValue(OpenApiConstants.FILE_URL);
        String fullPath = null;
        try {
            // BufferedImage image = ImageIO.read(file.getInputStream());
            if (!imageSuffix.contains(suffix)) {
                return error("建议上传png、jpg、jpeg、bmp、gif格式的图片");
            }

            if (fileSize > IMAGE_MAX_SIZE) {
                return error("图标大小应小于1M");
            }
            // if (image.getWidth() != IMAGE_WIDTH || image.getHeight() != IMAGE_HEIGHT) {
            // return error("图标尺寸应为512px*512px");
            // }

            fullPath = filePath + path;// 完整路径
            File dir = new File(fullPath);
            if (!dir.exists()) {// 如果不存在目录则创建目录
                dir.mkdirs();
            }

            file.transferTo(new File(fullPath + "/" + fileName));
        }
        catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Runtime.getRuntime().exec("chmod -R 755 " + fullPath);
            } catch (IOException e) {
                log.error("文件" + fullPath + " 授权失败");
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "1");
        jsonObject.put("originalFileName", originalFileName);
        jsonObject.put("iconUrl", path + "/" + fileName);
        jsonObject.put("fullUrl", fileUrl + path + "/" + fileName);
        return jsonObject.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = { "/saveAppInfo" })
    public String saveAppInfo(ServerRemoteDto dto) {
        try {
            fixBaseServerForAdd(dto);
            appManageRemoteService.addApp(dto);
        }
        catch (Exception e) {
            return returnError();
        }
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping(value = { "/modifyAppInfo" })
    public String modifyAppInfo(ServerRemoteDto dto) {
        try {
            fixBaseServerForModify(dto);
            appManageRemoteService.updateAppInfo(dto);
        }
        catch (Exception e) {
            return returnError();
        }
        return returnSuccess();
    }

    /**
     * 订阅人数统计
     * 
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/orderNum")
    public String countTotalOrder(int serverId, int subId, int orderType, String userType) {
        // 1、系统订阅个人免费 和 单位订阅个人免费(需看订阅身份)
        // 2、系统订阅个人需授权 和 单位订阅个人需授权

        int orderNum = 0;// 订阅人数

        Integer[] userTypes = new Integer[3];
        String[] userTypesStr = userType.split(",");
        for (int i = 0; i < userTypesStr.length; i++) {
            userTypes[i] = Integer.valueOf(userTypesStr[i]);
        }

        if (orderType == OrderTypeEnum.SYSTEM.getValue() || orderType == OrderTypeEnum.UNIT_PERSONAL_AUTH.getValue()) {
            orderNum = modelRemoteService.countAuthUserBySubId(subId);
        }

        if (orderType == OrderTypeEnum.SYSTEM_NO_AUTH.getValue()) {
            orderNum = serverRemoteService.countFreeOrderNum(null, userTypes);
        }

        if (orderType == OrderTypeEnum.UNIT_PERSONAL_NO_AUTH.getValue()) {
            orderNum = serverRemoteService.countFreeOrderNum(serverId, userTypes);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderNum", orderNum);
        return jsonObject.toJSONString();
    }

    private void fixBaseServerForAdd(ServerRemoteDto dto) {
        dto.setOrderType(OrderTypeEnum.UNIT_PERSONAL_NO_AUTH.getValue());// 第三方应用 默认单位订阅个人免费
        dto.setCreationTime(new Date());
        dto.setUnitType(StringUtils.join(ArrayUtils.nullToEmpty(dto.getUnitTypeArray()), ","));
        if (dto.getUnitType().equals(String.valueOf(UnitClassEnum.EDUCATION.getValue()))) {
            dto.setUserType(String.valueOf(UserTypeEnum.TEACHER.getValue()));
        }
        else {
            dto.setUserType(StringUtils.join(ArrayUtils.nullToEmpty(dto.getUserTypeArray()), ","));
        }
        dto.setSections(StringUtils.join(ArrayUtils.nullToEmpty(dto.getSectionsArray()), ","));
        dto.setStatus(AppStatusEnum.UNCOMMITTED.getValue());
        dto.setSystemId(UuidUtils.generateUuid());
        dto.setServerClass(ServerClassEnum.AP_PRODUCT.getValue());
    }

    private void fixBaseServerForModify(ServerRemoteDto dto) {
        dto.setUnitType(StringUtils.join(ArrayUtils.nullToEmpty(dto.getUnitTypeArray()), ","));
        if (dto.getOrderType() == OrderTypeEnum.UNIT_PERSONAL_AUTH.getValue()) {
            dto.setUserType(String.valueOf(UserTypeEnum.TEACHER.getValue()));
        }
        else {
            if (dto.getUnitType().equals(String.valueOf(UnitClassEnum.EDUCATION.getValue()))) {
                dto.setUserType(String.valueOf(UserTypeEnum.TEACHER.getValue()));
            }
            else {
                dto.setUserType(StringUtils.join(ArrayUtils.nullToEmpty(dto.getUserTypeArray()), ","));
            }
        }
        dto.setSections(StringUtils.join(ArrayUtils.nullToEmpty(dto.getSectionsArray()), ","));
        dto.setModifyTime(new Date());
    }

    private void setUnitTypeAndUserTypeAndSectionsArray(ServerRemoteDto app) {
        String unitTypeStr = app.getUnitType();
        String userTypeStr = app.getUserType();
        String sectionsStr = app.getSections();

        if (StringUtils.isNotEmpty(unitTypeStr)) {
            app.setUnitTypeArray(unitTypeStr.split(","));
        }
        if (StringUtils.isNotEmpty(userTypeStr)) {
            app.setUserTypeArray(userTypeStr.split(","));
        }
        if (StringUtils.isNotEmpty(sectionsStr)) {
            app.setSectionsArray(sectionsStr.split(","));
        }

    }

    /**
     * 获得上传路径
     * 
     * @return
     */
    private String getUploadPath() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return "/upload/openapi/image/icon/" + year + "/" + month + "/" + day;
    }

    /**
     * 补充app的相关字段 如:时间转换、补充完整图片地址、描述修改
     * 
     * @param apps
     * @return
     */
    private List<ServerRemoteDto> decorateApps(List<ServerRemoteDto> apps) {
        String fileUrl = sysOptionRemoteService.findValue(OpenApiConstants.FILE_URL);
        // String fileUrl = "http://www.file.dev/file";
        if (!CollectionUtils.isEmpty(apps)) {
            for (ServerRemoteDto app : apps) {
                int status = app.getStatus();
                app.setStatusName(AppStatusEnum.getName(status));// 状态名
                app.setFullIcon(fixDoamin(app.getIconUrl(), fileUrl));// 应用图片地址域名
                app.setTimeStr(DateUtils.date2StringByDay(getDateByAppStatus(status, app)));
            }
        }
        return apps;
    }

    /**
     * 图片地址域名
     * 
     * @param url
     * @return
     */
    private String fixDoamin(String url, String fileUrl) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }

        if (!url.startsWith("http")) {
            url = fileUrl + url;
        }
        return url;
    }

    /**
     * 根据当前应用状态返回对应的时间
     * 
     * @param statuss
     * @param app
     * @return
     */
    private Date getDateByAppStatus(int status, ServerRemoteDto app) {
        if (status == AppStatusEnum.ONLINE.getValue()) {
            return app.getOnlineTime();
        }
        else if (status == AppStatusEnum.AUDIT.getValue()) {
            return app.getApplyTime();
        }
        else if (app.getModifyTime() != null) {
            return app.getModifyTime();
        }
        return app.getCreationTime();
    }

}
