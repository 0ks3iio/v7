package net.zdsoft.bigdata.datav.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.datav.entity.Screen;
import net.zdsoft.bigdata.datav.service.ScreenGroupService;
import net.zdsoft.bigdata.datav.service.ScreenService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shenke
 * @since 2018/10/17 9:57
 */
@Controller
@RequestMapping("/bigdata/cockpitQuery")
public class DataVQueryModelController extends BigdataBaseAction {

    @Resource
    private ScreenService screenService;
    @Resource
    private ScreenGroupService screenGroupService;
    @Resource
    private OptionService optionService;

    private static final String STYLE_NEW = "2";

    private String fileUrl;

    @Autowired
    public DataVQueryModelController(SysOptionRemoteService sysOptionRemoteService) {
        fileUrl = sysOptionRemoteService.getFileUrl(getRequest().getServerName());
    }

    @GetMapping("index")
    public String index(ModelMap model) {
        LoginInfo loginInfo = getLoginInfo();
        List<Screen> screenList = screenService.getScreensByUnitIdAndUserId(loginInfo.getUnitId(), loginInfo.getUserId());
        HttpServletRequest request = getRequest();
        screenList.forEach(e->{
            if (StringUtils.isNotBlank(e.getUrl())) {
                if (!StringUtils.startsWithIgnoreCase(e.getUrl(), "http")) {
                    e.setUrl(UrlUtils.getPrefix(request) + "/" + UrlUtils.ignoreFirstLeftSlash(e.getUrl()));
                }
            }
        });
        model.addAttribute("cockpits", screenList);
        model.addAttribute("fileUrl", fileUrl);

        if (isNewStyle()) {
            return "/bigdata/datav/model/new/new-query-index.ftl";
        }
        return "/bigdata/datav/model/queryIndex.ftl";
    }

    private boolean isNewStyle() {
        OptionDto styleDto = optionService.getAllOptionParam("pageStyle");
        String style = styleDto.getFrameParamMap().get("style");
        return STYLE_NEW.equals(style);
    }

}
