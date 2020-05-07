package net.zdsoft.api.openapi.remote.openapi.action;

import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.enums.AppStatusEnum;
import net.zdsoft.api.base.service.ApiDeveloperService;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.api.openapi.remote.openapi.dto.OpenApiAppInput;
import net.zdsoft.api.openapi.remote.openapi.vo.OpenApiAppEdit;
import net.zdsoft.api.openapi.remote.openapi.vo.OpenApiList;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/5/21 上午11:21
 */
@Controller
@RequestMapping("bigdata/app")
public class OpenApiAppManagementAction {

    private Logger logger = LoggerFactory.getLogger(OpenApiAppManagementAction.class);

    @Resource
    private OpenApiAppService openApiAppService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private ApiDeveloperService apiDeveloperService;
    @Resource
    private BigLogService bigLogService;

    @RequestMapping(
            value = "index",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE
    )
    public String execute(Pageable pageable,String applyType, Model model) {
    	Page<OpenApiApp> appList;
    	if(StringUtils.isBlank(applyType) || applyType.equals("-1")){
    		appList = openApiAppService.getApps(pageable);
    	}else{
    		appList = openApiAppService.getAppsByApptype(pageable,applyType);
    		model.addAttribute("applyType", applyType);
    	}
        String[] developerIds = appList.stream().map(OpenApiApp::getDeveloperId)
                .filter(Objects::nonNull).toArray(String[]::new);
        //base 方法自动进行空判定了
        Map<String, String> developerNamesMap = apiDeveloperService.findListByIds(developerIds).stream()
                .collect(Collectors.toMap(ApiDeveloper::getId, ApiDeveloper::getRealName));

        List<OpenApiList> apps = appList.getContent().stream().map(OpenApiList::convert)
                .map(e->e.setDeveloperName(developerNamesMap.getOrDefault(e.getDeveloperId(), "")))
                .collect(Collectors.toList());
        model.addAttribute("appList", new PageImpl<>(apps, pageable, appList.getTotalElements()));
        return "/api/app/appManagement-index.ftl";
    }
    
    @RequestMapping(
            value = "",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_XML_VALUE
    )
    public String doEditApp(@RequestParam(required = false) String appId, Model model) {
        OpenApiAppEdit app;
        if (StringUtils.isNotBlank(appId)) {
            app = OpenApiAppEdit.convert(openApiAppService.getApp(appId), sysOptionRemoteService.findValue(Constant.FILE_URL));
        } else {
            app = new OpenApiAppEdit();
        }
        model.addAttribute("app", app);
        return "/api/app/appManagement-edit2.ftl";
    }
    @RequestMapping(
            value = "audit",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_XML_VALUE
    )
    public String doAuditApp(@RequestParam String appId, Model model) {
        doEditApp(appId, model);
        model.addAttribute("audit", true);
        return "/api/app/appManagement-edit2.ftl";
    }


    @ResponseBody
    @RequestMapping(
            value = "modify",
            method = RequestMethod.POST
            //produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Object doModifyApp(OpenApiAppInput openApiAppInput) {

        //TODO 这个方法需要优化

        //identity
        String id = openApiAppInput.getId();
        if (StringUtils.isBlank(id)) {
            id = UuidUtils.generateUuid();
        }

        //save icon
        String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
        String iconUrl = null;
        try {
            iconUrl = openApiAppInput.transferIconFile(filePath, id);
        } catch (IOException e) {
            logger.error("图标保存失败", e);
            return Response.error().message("图标保存失败").build();
        }

        OpenApiApp app = openApiAppInput.convert();
        //modify
        if (StringUtils.isNotBlank(openApiAppInput.getId())) {
            OpenApiApp persistentApp = openApiAppService.getApp(openApiAppInput.getId());
            //业务日志埋点  修改
            LogDto logDto=new LogDto();
            logDto.setBizCode("update-openApiAppInput");
            logDto.setDescription("应用 "+persistentApp.getName());
            logDto.setOldData(persistentApp);
            BeanUtils.copyProperties(app, persistentApp, "developerId", "id", "deleted", "creationTime", "status", "iconUrl", "visible");
            persistentApp.setModifyTime(new Date());
            if (StringUtils.isNotBlank(iconUrl)) {
                persistentApp.setIconUrl(iconUrl);
            }
            if (openApiAppInput.getStatus() != null) {
                persistentApp.setStatus(openApiAppInput.getStatus());
            }
            openApiAppService.addApp(persistentApp);
 
            logDto.setNewData(persistentApp);
            logDto.setBizName("应用管理");
            bigLogService.updateLog(logDto);

        }
        //save
        else {
            app.setId(id);
            app.setDeleted(0);
            app.setCreationTime(new Date());
            app.setModifyTime(app.getCreationTime());
            app.setStatus(AppStatusEnum.OFFLINE.getValue());
            app.setVisible(1);
            app.setIconUrl(iconUrl);
            openApiAppService.addApp(app);
            //业务日志埋点  新增
            LogDto logDto=new LogDto();
            logDto.setBizCode("insert-openApiAppInput");
            logDto.setDescription("应用 "+app.getName());
            logDto.setNewData(app);
            logDto.setBizName("应用管理");
            bigLogService.insertLog(logDto);



        }
        if (logger.isDebugEnabled()) {
            logger.debug("AppInfo {}", app);
        }
        return Response.ok().build();
    }

    @ResponseBody
    @RequestMapping(
            value = "modify/status/{appId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Object doModifyAppStatus(@PathVariable("appId") String appId,
                                    Integer appStatus) {
        AppStatusEnum statusEnum = AppStatusEnum.from(appStatus);
        if (statusEnum == null) {
            return Response.error().message("不合法的status").build();
        }
        OpenApiApp oldApp = openApiAppService.getApp(appId);
        openApiAppService.modifyAppStatus(statusEnum, appId);
        //业务日志埋点  修改
        LogDto logDto=new LogDto();
        logDto.setBizCode("update-appStatus");
        logDto.setDescription("应用 "+oldApp.getName()+" 的上下线状态");
        logDto.setOldData(oldApp.getStatus());
        OpenApiApp newApp = openApiAppService.getApp(appId);
        logDto.setNewData(newApp.getStatus());
        logDto.setBizName("应用管理");
        bigLogService.updateLog(logDto);
        return Response.ok().build();
    }

    @RequestMapping(
            value = "modify/visible/{appId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Object doModifyAppVisible(@PathVariable("appId") String appId,
                                     @RequestBody Integer visible) {
        openApiAppService.modifyAppVisible(visible, appId);
        return "";
    }

    @ResponseBody
    @RequestMapping(
            value = "delete/{appId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Object doDeleteApp(@PathVariable("appId") String appId) {
        OpenApiApp app = openApiAppService.getApp(appId);
        openApiAppService.deleteApp(appId);
        //业务日志埋点  删除
        LogDto logDto=new LogDto();
        logDto.setBizCode("delete-openApiApp");
        logDto.setDescription("应用 "+app.getName());
        logDto.setBizName("应用管理");
        logDto.setOldData(app);
        bigLogService.deleteLog(logDto);
        return Response.ok().build();
    }


    @ResponseBody
    @RequestMapping(
            value = "validate",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Object doCheckAppName(String appName, String id) {
        if (StringUtils.isBlank(appName)) {
            return Response.error().message("appName不能为空").build();
        }
        if (StringUtils.isNotBlank(id)) {
            OpenApiApp app = openApiAppService.getApp(id);
            if (app != null && app.getName().equals(appName)) {
                return Response.ok().build();
            }
        }
        Boolean exists = openApiAppService.existsByAppName(appName);
        if (Boolean.TRUE.equals(exists)) {
            return Response.error().message("应用名称已存在").build();
        }
        return Response.ok().build();
    }

}
