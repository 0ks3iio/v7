package net.zdsoft.api.openapi.remote.openapi.action;

import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.enums.AppStatusEnum;
import net.zdsoft.api.base.service.ApiDeveloperService;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.api.openapi.remote.openapi.dto.OpenApiAppInput;
import net.zdsoft.api.openapi.remote.openapi.vo.OpenApiAppEdit;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static net.zdsoft.api.openapi.remote.openapi.constant.OpenApiConstants.DEVELOPER_SESSION;

/**
 * 开发者应用管理
 * @author shenke
 * @since 2019/5/23 下午8:09
 */
@Controller
@RequestMapping("/bigdata/api/developer/front")
public class DeveloperAppAction {

    private Logger logger = LoggerFactory.getLogger(DeveloperAppAction.class);

    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private OpenApiAppService openApiAppService;
    @Resource
    private ApiDeveloperService apiDeveloperService;

    @ResponseBody
    @RequestMapping("init")
    public Object init(@RequestParam("developerId") String developerId,  HttpServletRequest request) {
        ApiDeveloper developer = apiDeveloperService.findOne(developerId);
        request.getSession().setAttribute(DEVELOPER_SESSION, developer);
        return Response.ok().build();
    }

    @RequestMapping("app/index")
    public String index(@SessionAttribute(DEVELOPER_SESSION) ApiDeveloper developer,
                        Model model) {
        String fileUrl = sysOptionRemoteService.findValue(Constant.FILE_URL);
        List<OpenApiApp> apiApps = openApiAppService.getAppsByDeveloperId(developer.getId());
        model.addAttribute("apps", apiApps);
        model.addAttribute("fileUrl", fileUrl);
        return "/api/developer/front/developerFront-app.ftl";
    }

    @RequestMapping("app/view")
    public String doView(@RequestParam(value = "id") String id, Model model) {
        OpenApiAppEdit app = OpenApiAppEdit.convert(openApiAppService.getApp(id), sysOptionRemoteService.findValue(Constant.FILE_URL));
        model.addAttribute("app", app);
        return "/api/developer/front/developerFront-appView.ftl";
    }

    @RequestMapping({"app/add", "app/edit"})
    public String doAdd(@RequestParam(value = "id", required = false) String id, Model model) {
        OpenApiAppEdit app;
        if (StringUtils.isNotBlank(id)) {
            app = OpenApiAppEdit.convert(openApiAppService.getApp(id), sysOptionRemoteService.findValue(Constant.FILE_URL));
        } else {
            app = new OpenApiAppEdit();
        }
        model.addAttribute("app", app);
        return "/api/developer/front/developerFront-appAdd.ftl";
    }

    @ResponseBody
    @RequestMapping("app/save")
    public Object doSave(OpenApiAppInput apiAppInput, @SessionAttribute(DEVELOPER_SESSION) ApiDeveloper developer) {

        String id = StringUtils.isNotBlank(apiAppInput.getId()) ? apiAppInput.getId() : UuidUtils.generateUuid();
        String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
        String iconUrl = null;
        try {
            iconUrl = apiAppInput.transferIconFile(filePath, id);
        } catch (IOException e) {
            logger.error("图标保存失败", e);
            return Response.error().message("图标保存失败").build();
        }
        OpenApiApp app = apiAppInput.convert();
        if (StringUtils.isNotBlank(apiAppInput.getId())) {
            OpenApiApp persistentApp = openApiAppService.getApp(apiAppInput.getId());
            BeanUtils.copyProperties(app, persistentApp,  "developerId", "id", "deleted", "creationTime", "status", "iconUrl", "visible");
            persistentApp.setModifyTime(new Date());
            if (StringUtils.isNotBlank(iconUrl)) {
                persistentApp.setIconUrl(iconUrl);
            }
            app.setDeveloperId(developer.getId());
            openApiAppService.addApp(persistentApp);
            return Response.ok().build();
        }
        app.setId(id);
        app.setDeleted(0);
        app.setCreationTime(new Date());
        app.setModifyTime(app.getCreationTime());
        app.setStatus(AppStatusEnum.UNCOMMITTED.getValue());
        app.setVisible(1);
        app.setIconUrl(iconUrl);
        app.setDeveloperId(developer.getId());
        openApiAppService.addApp(app);
        return Response.ok().build();
    }

    @ResponseBody
    @RequestMapping("app/commit")
    public Object commit(@RequestParam("id") String id) {
        openApiAppService.modifyAppStatus(AppStatusEnum.AUDIT, id);
        return Response.ok().build();
    }

    @ResponseBody
    @RequestMapping("app/delete")
    public Object delete(@RequestParam("id") String id) {
        openApiAppService.deleteApp(id);
        return Response.ok().build();
    }
}
