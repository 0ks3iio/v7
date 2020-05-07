/*
 * @(#)ServerAction.java    Created on 2017-3-4
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.action.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.MailUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.remote.openapi.dto.DeveloperDto;
import net.zdsoft.remote.openapi.remote.service.DeveloperRemoteService;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.enums.YesNoEnum;
import net.zdsoft.system.enums.server.AppStatusEnum;
import net.zdsoft.system.enums.server.OrderTypeEnum;
import net.zdsoft.system.enums.server.ServerClassEnum;
import net.zdsoft.system.enums.unit.SectionsTypeEnum;
import net.zdsoft.system.enums.unit.UnitClassEnum;
import net.zdsoft.system.enums.user.UserTypeEnum;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@RequestMapping("/system/server")
@Controller
public class ServerAction extends BaseAction {

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
    private ServerRemoteService serverRemoteService;
    @Autowired
    @Lazy
    private DeveloperRemoteService developerRemoteService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Autowired
    private ModelRemoteService modelRemoteService;

    /**
     * 加载已启用的应用列表
     *
     * @author dingw
     * @param map
     * @return
     */
    @RequestMapping("/loadEnableServers")
    public String loadEnableServers(ModelMap map) {
        map.put("enableServers",
                serverRemoteService.findListBy(new String[] { "status", "serverClass" },
                        new Integer[] { AppStatusEnum.ONLINE.getValue(), ServerClassEnum.INNER_PRODUCT.getValue() }));
        return "/system/enableServers.ftl";
    }

    @ResponseBody
    @RequestMapping("/modify")
    public String modify(Server server) {
        // TODO 保存部分字段
        serverRemoteService.save(server);
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("/saveAppInfo")
    @ControllerInfo("超管新增应用")
    public String saveAppInfo(Server server) {
        try {
            // 超管新增获取对应的appId 和 appkey
            // 设置server对应属性
            fixBaseServerForAdd(server);
            // 新增server
            serverRemoteService.addAppAndRegisterPassPort(server);
            // registerPassPort(resultServer);
        }
        catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("/modifyAppInfo")
    @ControllerInfo("超管修改应用")
    public String modifyAppInfo(Server server) {
        try {
            fixBaseServerForModify(server);
//            serverRemoteService.updateAppInfoByAppId(server);
            serverRemoteService.updateAppAndRegisterPassPort(server);
        }
        catch (Exception e) {
            return returnError();
        }
        return returnSuccess();
    }

    /**
     * 应用通过
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/passApp")
    @ControllerInfo("超管通过应用")
    public String passApp(Server server) {
        fixBaseServerForModify(server);
        server.setStatus(AppStatusEnum.OFFLINE.getValue());
        serverRemoteService.updateAppAndRegisterPassPort(server);
        if (StringUtils.isNotEmpty(server.getDevId())) {
            DeveloperDto developerDto = developerRemoteService.getDeveloper(server.getDevId());
            String emailAddress = developerDto.getEmail();
            sendEmail(emailAddress, "您的应用————" + server.getName() + "已审核通过", "亲爱的 " + developerDto.getRealName()
                    + ":\n 您好!\n 很高兴的通知您,您提交的应用————" + server.getName() + "已经通过审核!");
        }

        return returnSuccess();
    }

    /**
     * 应用不通过
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/notPassApp")
    @ControllerInfo("超管不通过应用")
    public String notPassApp(int appId, String devId, String appName) {
        serverRemoteService.updateAppStatusByAppId(AppStatusEnum.NOTPASS.getValue(), appId);
        // TODO 发邮件通知
        if (StringUtils.isNotEmpty(devId)) {
            DeveloperDto developerDto = developerRemoteService.getDeveloper(devId);
            String emailAddress = developerDto.getEmail();
            sendEmail(emailAddress, "您的应用————" + appName + "未通过审核", "亲爱的 " + developerDto.getRealName()
                    + ":\n 您好!\n 很抱歉的通知您,您提交的应用————" + appName + "未通过审核!请修改后再次申请提交。");
        }
        return returnSuccess();
    }

    /**
     * 删除应用
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delApp")
    @ControllerInfo("超管删除应用")
    public String delApp(int appId, String devId, String appName) {
        serverRemoteService.removeAppByAppId(appId);
        if (StringUtils.isNotEmpty(devId)) {
            DeveloperDto developerDto = developerRemoteService.getDeveloper(devId);
            String emailAddress = developerDto.getEmail();
            String adminContact = sysOptionRemoteService.findValue(Constant.ADMIN_CONTACT);
            sendEmail(emailAddress, "您的应用————" + appName + "已被删除",
                    "亲爱的 " + developerDto.getRealName() + ":\n 您好!\n 很抱歉的通知您,您的应用————" + appName + "已被删除!如有疑问请联系管理员。"
                            + (StringUtils.isNotEmpty(adminContact) ? adminContact : ""));
        }
        return returnSuccess();
    }

    /**
     * 应用上线
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/onlineApp")
    @ControllerInfo("超管上线应用")
    public String onlineApp(int appId, String devId, String appName) {
        serverRemoteService.updateAppStatusByAppId(AppStatusEnum.ONLINE.getValue(), appId);
        if (StringUtils.isNotEmpty(devId)) {
            DeveloperDto developerDto = developerRemoteService.getDeveloper(devId);
            String emailAddress = developerDto.getEmail();
            sendEmail(emailAddress, "您的应用————" + appName + "已上线", "亲爱的 " + developerDto.getRealName()
                    + ":\n 您好!\n 很高兴的通知您,您的应用————" + appName + "已上线!");
        }
        return returnSuccess();
    }

    /**
     * 应用下线
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/offlineApp")
    @ControllerInfo("超管下线应用")
    public String offlineApp(int appId, String devId, String appName) {
        serverRemoteService.updateAppStatusByAppId(AppStatusEnum.OFFLINE.getValue(), appId);
        if (StringUtils.isNotEmpty(devId)) {
            DeveloperDto developerDto = developerRemoteService.getDeveloper(devId);
            String emailAddress = developerDto.getEmail();
            String adminContact = sysOptionRemoteService.findValue(Constant.ADMIN_CONTACT);
            sendEmail(emailAddress, "您的应用————" + appName + "已经下线",
                    "亲爱的 " + developerDto.getRealName() + ":\n 您好!\n 很抱歉的通知您,您的应用————" + appName + "已经下线!如有疑问请联系管理员。"
                            + (StringUtils.isNotEmpty(adminContact) ? adminContact : ""));
        }
        return returnSuccess();
    }

    /**
     * 应用停用
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/stopApp")
    @ControllerInfo("超管停用应用")
    public String stopApp(int appId, String devId, String appName) {
        serverRemoteService.updateAppStatusByAppId(AppStatusEnum.STOP.getValue(), appId);
        if (StringUtils.isNotEmpty(devId)) {
            DeveloperDto developerDto = developerRemoteService.getDeveloper(devId);
            String emailAddress = developerDto.getEmail();
            String adminContact = sysOptionRemoteService.findValue(Constant.ADMIN_CONTACT);
            sendEmail(emailAddress, "您的应用————" + appName + "已被停用",
                    "亲爱的 " + developerDto.getRealName() + ":\n 您好!\n 很抱歉的通知您,您的应用————" + appName + "已被停用!如有疑问请联系管理员。"
                            + (StringUtils.isNotEmpty(adminContact) ? adminContact : ""));
        }
        return returnSuccess();
    }

    /**
     * 应用启用
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/startApp")
    @ControllerInfo("超管启用应用")
    public String startApp(int appId, String devId, String appName) {
        serverRemoteService.updateAppStatusByAppId(AppStatusEnum.START.getValue(), appId);
        if (StringUtils.isNotEmpty(devId)) {
            DeveloperDto developerDto = developerRemoteService.getDeveloper(devId);
            String emailAddress = developerDto.getEmail();
            sendEmail(emailAddress, "您的应用————" + appName + "已启用", "亲爱的 " + developerDto.getRealName()
                    + ":\n 您好!\n 很高兴的通知您,您的应用————" + appName + "已启用!");
        }
        return returnSuccess();
    }

    @RequestMapping("/index")
    public String appIndex(ModelMap map) {
        String url = "/system/server/appList";
        map.put("url", url);
        return "/system/common/home.ftl";
    }

    /**
     * 应用管理列表
     *
     * @return
     */
    @RequestMapping("/appList")
    @ControllerInfo("进入超管应用列表")
    public String appList(String appName, Integer status, String reservation, Integer source, org.springframework.ui.Model model) {

        Date startTime = null;
        Date endTime = null;
        if (StringUtils.isNotEmpty(reservation)) {
            Date[] dateArray = handleReservation(reservation);
            startTime = dateArray[0];
            endTime = dateArray[1];
        }

        // 获取当前环境的来源
        String appSourceStr = sysOptionRemoteService.findValue(Constant.APP_SERVER_CLASS);
        int isShowSource = YesNoEnum.YES.getValue();
        if (StringUtils.isNotEmpty(appSourceStr)) {
            String[] appSources = appSourceStr.split(",");
            // appSourceList = Arrays.asList(appSources);
            if (appSources.length == 1) {
                isShowSource = YesNoEnum.NO.getValue();
                source = Integer.valueOf(appSources[0]);
            }
        }

        Pagination page = createPagination();
        List<Server> apps = serverRemoteService.getAllApps(appName, status, source, startTime, endTime, page);

        fixApp(apps);
        model.addAttribute("apps", apps);
        model.addAttribute("appName", appName);
        model.addAttribute("status", status);
        model.addAttribute("isShowSource", isShowSource);
        model.addAttribute("source", source);
        model.addAttribute("reservation", reservation);
        model.addAttribute("pagination", page);
        return "/openapi/system/appManage/appList.ftl";
    }

    /**
     * 新增应用
     *
     * @return
     */
    @RequestMapping("/addApp")
    @ControllerInfo("进入超管新增应用页面")
    public String addApp() {
        return "/openapi/system/appManage/addApp.ftl";
    }

    /**
     * 修改应用页面
     *
     * @return
     */
    @RequestMapping("/modifyApp")
    @ControllerInfo("进入超管应用修改页面")
    public String modifyApp(org.springframework.ui.Model model, String devName, int appId) {
        Server app = serverRemoteService.getAppByAppId(appId);
        app.setStatusName(AppStatusEnum.getName(app.getStatus()));
        if (app.getApplyTime() != null) {
            app.setTimeStr(DateUtils.date2StringByDay(app.getApplyTime()));
        }
        setUnitTypeAndUserTypeAndSectionsArray(app);
        String fileUrl = sysOptionRemoteService.getFileUrl(getRequest().getServerName());
        // String fileUrl = "http://www.file.dev/file";
        app.setFullIcon(fixDoamin(app.getIconUrl(), fileUrl));
        model.addAttribute("app", app);
        model.addAttribute("devName", devName);
        return "/openapi/system/appManage/modifyApp.ftl";
    }

    private String fixDoamin(String url, String fileUrl) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }

        if (!url.startsWith("http")) {
            url = fileUrl + url;
        }
        return url;
    }

    private void setUnitTypeAndUserTypeAndSectionsArray(Server app) {
        String unitTypeStr = app.getUnittype();
        String userTypeStr = app.getUsertype();
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
     * 查看模块
     *
     * @return
     */
    @ControllerInfo("查看模块")
    @RequestMapping("/lookModel")
    public String lookModel(Integer subId,ModelMap map) {
        List<Model> models = SUtils.dt(modelRemoteService.findListBySubId(subId), Model.class);

        models = EntityUtils.filter(models, new EntityUtils.Filter<Model>() {
            @Override
            public boolean doFilter(Model model) {
                return !model.getParentId().equals(-1);
            }
        });
        List<Integer> idList = EntityUtils.getList(models, Model::getId);
        models = EntityUtils.merge(models,SUtils.dt(modelRemoteService.findListByIn("parentId", EntityUtils.toArray(idList, Integer.class)), Model.class));
        // 教育局模块
        List<Model> educationModel = new ArrayList<Model>();
        // 学校模块
        List<Model> schoolModel = new ArrayList<Model>();
        for (Model model : models) {
            if (model.getUnitClass() == UnitClassEnum.EDUCATION.getValue()) {
                educationModel.add(model);
            }
            if (model.getUnitClass() == UnitClassEnum.SCHOOL.getValue()) {
                schoolModel.add(model);
            }
        }
        sortModelByParentId(educationModel);
        sortModelByParentId(schoolModel);
        //先教育局后学校
        List<Model> showModel = new ArrayList<Model>();
        showModel.addAll(educationModel);
        showModel.addAll(schoolModel);
        //查找所有parentId
        Map<String, List<Model>> parentIdMap = new HashMap<String, List<Model>>(); ;
        if(!CollectionUtils.isEmpty(educationModel))
        parentIdMap.put(educationModel.get(0).getUnitClass().toString(), educationModel);
        if(!CollectionUtils.isEmpty(schoolModel))
        parentIdMap.put(schoolModel.get(0).getUnitClass().toString(), getRoot(schoolModel));

        //所属子系统
        List<Server> apps = getChooserApp();
        map.put("apps", apps);
        map.put("parentIdMap", parentIdMap);
        map.put("showModel", showModel);
        return "/openapi/system/appManage/showModule.ftl";
    }

	/**
	 * @return  得到有根目录的子系统
	 */
	protected List<Server> getChooserApp() {
		List<Server> apps = serverRemoteService.findAllObject();
        List<Server> showApps = new ArrayList<Server>();
        for (Server server : apps) {
        	if(server.getSubId() == null){
        		continue;
        	}
        	List<Model> models1 = SUtils.dt(modelRemoteService.findListBySubId(server.getSubId()), Model.class);
        	models1 = EntityUtils.filter(models1, new EntityUtils.Filter<Model>() {
                @Override
                public boolean doFilter(Model model) {
                    return !model.getParentId().equals(-1);
                }
            });
        	if(CollectionUtils.isNotEmpty(models1)){
        		showApps.add(server);
        	}
        }
		return showApps;
	}

    /**
     * 校验是否存在相同的应用名
     *
     * @param appName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/checkAppName" })
    public String checkAppName(String appName) {
        int appCount = serverRemoteService.getAppCountByName(appName);
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
    @ControllerInfo("超管上传应用图片")
    public String uploadAppImage(@RequestParam("file") MultipartFile file, String prePath) {
        if (file.isEmpty()) {
            return error("请选择文件");
        }

        String originalFileName = file.getOriginalFilename();// 文件原始名字
        long fileSize = file.getSize();// 字节
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String path = getUploadPath(prePath);// 相对路径
        String fileName = UuidUtils.generateUuid() + "." + suffix;
        String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
        String fileUrl = sysOptionRemoteService.getFileUrl(getRequest().getServerName());
        // String filePath = "E:\\data";
        // String fileUrl = "http://www.file.dev/file";
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

            String fullPath = filePath + path;// 完整路径
            File dir = new File(fullPath);
            if (!dir.exists()) {// 如果不存在目录则创建目录
                dir.mkdirs();
            }

            file.transferTo(new File(fullPath + "/" + fileName));
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(!System.getProperty("os.name").toLowerCase().startsWith("win")) {
                    Runtime.getRuntime().exec("chmod -R 755 " + filePath + path);
                }
            } catch (IOException e) {
                log.error("文件授权失败 {}", e);
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "1");
        jsonObject.put("originalFileName", originalFileName);
        jsonObject.put("iconUrl", path + "/" + fileName);
        jsonObject.put("fullUrl", fileUrl + path + "/" + fileName);
        return jsonObject.toJSONString();
    }

    private void sortModelByParentId(List<Model> model) {
        Collections.sort(model, new Comparator<Model>() {
            @Override
            public int compare(Model m1, Model m2) {
                if (m1.getParentId() < m2.getParentId()) {
                    return 1;
                }

                if (m1.getParentId() > m2.getParentId()) {
                    return -1;
                }

                return 0;
            }
        });
    }

    private void sendEmail(String emailAddress, String title, String content) {
        String emailServer = sysOptionRemoteService.findValue(Constant.EMAIL_SERVER);
        String userName = sysOptionRemoteService.findValue(Constant.EMAIL_USERNAME);
        String passWord = sysOptionRemoteService.findValue(Constant.EMAIL_PASSWORD);
        if (StringUtils.isNotEmpty(emailServer) && StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(passWord)) {
            MailUtils.send(emailServer, userName, passWord, emailAddress, title, content);
        }
    }

    /**
     * 新增应用部分数据补充
     *
     * @param server
     */
    private void fixBaseServerForAdd(Server server) {
    	if(server.getOrderType() == null){
    		server.setOrderType(OrderTypeEnum.UNIT_PERSONAL_NO_AUTH.getValue());// 第三方应用新增 默认是单位订阅个人免费
    	}
        server.setCreationTime(new Date());
        server.setUnittype(StringUtils.join(ArrayUtils.nullToEmpty(server.getUnitTypeArray()), ","));
        if (server.getOrderType() == OrderTypeEnum.UNIT_PERSONAL_AUTH.getValue()) {
            server.setUsertype(String.valueOf(UserTypeEnum.TEACHER.getValue()));
        }
        else {
            if (server.getUnittype().equals(String.valueOf(UnitClassEnum.EDUCATION.getValue()))) {
                server.setUsertype(String.valueOf(UserTypeEnum.TEACHER.getValue()));
            }
            else {
                server.setUsertype(StringUtils.join(ArrayUtils.nullToEmpty(server.getUserTypeArray()), ","));
            }
        }
        server.setSections(StringUtils.join(ArrayUtils.nullToEmpty(server.getSectionsArray()), ","));
        server.setStatus(AppStatusEnum.OFFLINE.getValue());// 超管新增的应用默认为下线状态
        server.setSystemId(UuidUtils.generateUuid());
        server.setServerClass(ServerClassEnum.AP_PRODUCT.getValue());
    }

    /**
     * 应用修改数据补充
     *
     * @param server
     */
    private void fixBaseServerForModify(Server server) {
        server.setUnittype(StringUtils.join(ArrayUtils.nullToEmpty(server.getUnitTypeArray()), ","));
        if (server.getOrderType() == OrderTypeEnum.UNIT_PERSONAL_AUTH.getValue()) {
            server.setUsertype(String.valueOf(UserTypeEnum.TEACHER.getValue()));
        }
        else {

            if (server.getUnittype().equals(String.valueOf(UnitClassEnum.EDUCATION.getValue()))) {
                server.setUsertype(String.valueOf(UserTypeEnum.TEACHER.getValue()));
            }
            else {
                server.setUsertype(StringUtils.join(ArrayUtils.nullToEmpty(server.getUserTypeArray()), ","));
            }
        }
        server.setSections(StringUtils.join(ArrayUtils.nullToEmpty(server.getSectionsArray()), ","));
        server.setModifyTime(new Date());
        if (server.getStatus() != null && server.getStatus() == AppStatusEnum.ONLINE.getValue()) {
            server.setOnlineTime(new Date());
        }
    }

    /**
     * 获得上传路径
     *
     * @return
     */
    private String getUploadPath(String prePath) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (Validators.isEmpty(prePath)) {
            return "/upload/openapi/image/icon/" + year + "/" + month + "/" + day;
        }
        return prePath + year + "/" + month + "/" + day;
    }

    /**
     * 应用列表日期处理
     *
     * @param reservation
     * @return
     */
    private Date[] handleReservation(String reservation) {
        Date[] result = new Date[2];
        String[] timeStrArray = reservation.split("\\s{1}-\\s{1}");
        result[0] = DateUtils.string2Date(timeStrArray[0]);
        result[1] = DateUtils.getEndDate(DateUtils.string2Date(timeStrArray[1]));
        return result;
    }

    /**
     * 应用属性填充
     *
     * @param apps
     */
    private void fixApp(List<Server> apps) {
        List<String> devIds = EntityUtils.getList(apps, "devId");
        Map<String, String> map = new HashMap<String, String>();
        List<String> devIds2 = new ArrayList<String>();
        for(String devId : devIds){
        	if(StringUtils.isNotBlank(devId))
        		devIds2.add(devId);
        }
        if (!CollectionUtils.isEmpty(devIds2)) {
            map = developerRemoteService.getDeveloper(devIds);
        }

        for (Server app : apps) {
            app.setDevName(map.get(app.getDevId()));
            app.setStatusName(AppStatusEnum.getName(app.getStatus()));
            app.setStatusColor(getStatusColor(app.getStatus()));
            app.setAppSource(ServerClassEnum.getName(app.getServerClass()));
            app.setTimeStr(DateUtils.date2StringByDay(app.getModifyTime()));
            Integer orderType = app.getOrderType();
            app.setOrderTypeName(OrderTypeEnum.getName(orderType == null ? OrderTypeEnum.UNIT_PERSONAL_AUTH.getValue() : orderType));
            fixUserTypeNameAndUnitTypeName(app);
        }
    }

    private void fixUserTypeNameAndUnitTypeName(Server app) {
        String userType = app.getUsertype();
        String unitType = app.getUnittype();
        String sections = app.getSections();
        app.setUserTypeName(fixUserTypeName(userType));
        app.setUnitTypeName(fixUnitTypeName(unitType));
        app.setSectionsName(fixSectionsName(sections));
    }

    private String fixUserTypeName(String userType) {
        if (StringUtils.isEmpty(userType)) {
            return null;
        }

        String[] userTypeArray = userType.split(",");

        return getEnumString(userTypeArray, 1);
    }

    private String fixUnitTypeName(String unitType) {
        if (StringUtils.isEmpty(unitType)) {
            return null;
        }
        String[] unitTypeArray = unitType.split(",");
        return getEnumString(unitTypeArray, 2);
    }

    private String fixSectionsName(String sections) {
        if (StringUtils.isEmpty(sections)) {
            return null;
        }
        String[] sectionsArray = sections.split(",");
        return getEnumString(sectionsArray, 3);
    }

    private String getEnumString(String[] array, int enumType) {
        String result = "";
        for (int i = 0; i < array.length; i++) {
            String name = "";
            if (enumType == 1) {
                name = UserTypeEnum.getName(Integer.valueOf(array[i]));
            }

            if (enumType == 2) {
                name = UnitClassEnum.getName(Integer.valueOf(array[i]));
            }

            if (enumType == 3) {
                name = SectionsTypeEnum.getName(Integer.valueOf(array[i]));
            }

            if (i == array.length - 1) {
                result += name;
                break;
            }
            result += name + "、";
        }
        return result;
    }

    /**
     * 获取状态对应的颜色
     *
     * @param status
     * @return
     */
    private String getStatusColor(int status) {
        String color = "";
        if (status == AppStatusEnum.UNCOMMITTED.getValue()) {
            color = "badge-lightblue";
        }
        else if (status == AppStatusEnum.AUDIT.getValue()) {
            color = "badge-blue";
        }
        else if (status == AppStatusEnum.NOTPASS.getValue()) {
            color = "badge-red";
        }
        else if (status == AppStatusEnum.OFFLINE.getValue()) {
            color = "badge-yellow";
        }
        else if (status == AppStatusEnum.ONLINE.getValue()) {
            color = "badge-lightgreen";
        }
        else if (status == AppStatusEnum.STOP.getValue()) {
            color = "badge-grey";
        }
        return color;
    }

    @RequestMapping("/appIndex")
    public String serverAuthorizeHome() {
        return "/openapi/system/appManage/appManageHome.ftl";
    }

    public static void main(String[] args) {
        String[] appSources = "2".split(",");
        System.out.println(appSources.length);
    }


    @ResponseBody
	@ControllerInfo("更新模块")
	@RequestMapping("/updateModel")
	public String updateModel(@RequestBody String functions, ModelMap map){

		List<JSONObject> jsonObjects= SUtils.dt(functions, JSONObject.class);
		List<Model> listModels = new ArrayList<Model>();;
		if(!CollectionUtils.isEmpty(jsonObjects)){
			for (JSONObject jsonObject : jsonObjects) {
				if(jsonObject == null || jsonObject.isEmpty())
					continue;
				Model model = modelRemoteService.findOneObjectById(Integer.valueOf(jsonObject.getString("id")));
				model.setName(jsonObject.getString("modelName"));
				model.setParentId(Integer.valueOf(jsonObject.getString("parentId")));
				model.setMark(Integer.valueOf(jsonObject.getString("mark")));
//				model.setSubSystem(Integer.valueOf(jsonObject.getString("subsystem")));
				listModels.add(model);
			}
		}
		modelRemoteService.saveAll(EntityUtils.toArray(listModels,Model.class));
		return success("更新成功");
	}


    private  List<Model> getRoot(List<Model> list)
    {
    	List<Model> list2 = new ArrayList<Model>();
        if(!CollectionUtils.isEmpty(list)){
        	for (Model model : list) {
				if(Objects.equals("-1", model.getParentId().toString()) ){
					list2.add(model);
				}
			}
        	return list2;
        }else{
        	return list;
        }

    }


    /**
     * 查看子系统的根目录
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/findParentId")
    @ControllerInfo("查看子系统的根目录")
    public String findParentId(Integer subId) {
    	List<Model> models = SUtils.dt(modelRemoteService.findListBySubId(subId), Model.class);
    	List<Model> rootModels = getRoot(models);
    	if(!CollectionUtils.isEmpty(rootModels)){
    		return JSON.toJSONString(rootModels);
    	}else{
    		return "没有目录";
    	}
    }
}
