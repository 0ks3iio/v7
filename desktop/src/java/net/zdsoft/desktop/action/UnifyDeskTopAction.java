package net.zdsoft.desktop.action;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.entity.SysPlatformModel;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import net.zdsoft.system.remote.service.SysPlatformModelRemoteService;

/**
 * 桌面统一登录
 * created by shenke 2017/3/3 10:51
 */
@Controller
@RequestMapping("/desktop")
public class UnifyDeskTopAction extends BaseAction{

    private Logger logger = LoggerFactory.getLogger(UnifyDeskTopAction.class);

    @Autowired
    private ServerRemoteService serverRemoteService;
    @Autowired
    private ModelRemoteService modelRemoteService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Autowired
    private SysPlatformModelRemoteService sysPlatformModelRemoteService;
    
    @RequestMapping("/unify/unify-desktop.action")
    public String execute(ModelMap map, @RequestParam(name = "appId", required = false, defaultValue = "-1") int appId, String uid, String module){
        try {
            if(StringUtils.isNotBlank(module)){
            	Model model = SUtils.dc(modelRemoteService.findOneById(Integer.valueOf(module)), Model.class);
            	if(model == null) {
            		SysPlatformModel sysPlatformModel = SUtils.dc(sysPlatformModelRemoteService.findOneById(Integer.valueOf(module)), SysPlatformModel.class);
            		appId = sysPlatformModel.getSubsystem();
            	}else{
            		appId = model.getSubSystem();
            	}
            }
            Server server = SUtils.dc(serverRemoteService.findOneBy("subId", appId), Server.class);
            if (server == null) {
                logger.error(" can't find base_sever by appId [{}]", appId);
                //重定向到错误页面
                map.addAttribute("error", "appId错误");
                return "/desktop/unify/error.ftl";
            }
            List<Model> models = SUtils.dt(modelRemoteService.findListBySubId(appId), Model.class);
            models = models.stream().filter(e -> Integer.valueOf(1).equals(e.getMark())).collect(Collectors.toList());

            if (models.isEmpty()) {
                map.addAttribute("error", "该Ap下无可用模块");
                return "/desktop/unify/error.ftl";
            }

            HttpSession session = getSession();
            if (ServletUtils.getLoginInfo(session) == null) {
                //开发模式手动登录
                if (Evn.isDevModel()) {
                    LoginAction loginAction = Evn.getBean("loginAction");
                    loginAction.loginNoPwd(uid, null);
                }
                else {
                    //检查是否需要跳转passport
                    if (Evn.isPassport()) {
                        String indexUrl = sysOptionRemoteService.findValue("INDEX.URL");
                        String redirectUrl = PassportClientUtils.getPassportClient().getLoginURL(UrlUtils.getPrefix(getRequest())
                                + "/desktop/unify/index?appId=" + appId, getRequest().getContextPath(), indexUrl);
                        return "redirect:" + redirectUrl;
                    }
                    //没有连接passport
                    map.addAttribute("error", "请配置V7接入单点");
                    return "/desktop/unify/error.ftl";
                }
                getRequest().getSession().setAttribute("appId", appId);
            } else {
                getRequest().getSession().setAttribute("appId", appId);
            }
            getRequest().getSession().setAttribute("module", module);
        }catch (Exception e){
            return "/desktop/unify/error.ftl" ;
        }
        //map.addAttribute("appId", appId);
        return "/desktop/unify/v7-index.ftl" ;
    }

    @RequestMapping("/unify/index")
    public String index(int appId, ModelMap map) {
		getRequest().getSession().setAttribute("appId", appId);
        return "/desktop/unify/v7-index.ftl";
    }
}
