package net.zdsoft.bigdata.v3.index.action;

import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.v3.index.entity.HeadInfo;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BigdataBiBaseAction extends BigdataBaseAction {

    protected static final Logger log = Logger
            .getLogger(BigdataBiBaseAction.class);

    @Autowired
    public OptionService optionService;

    @Autowired
    public SysOptionRemoteService sysOptionRemoteService;


    public HeadInfo getHeadInfo() {
        OptionDto optionDto = optionService.getAllOptionParam("desktop");
        HeadInfo info = new HeadInfo();
        String platformName = "万朋大数据";
        String logo = UrlUtils.getPrefix(getRequest())
                + "/bigdata/v3/static/images/bi/wanshu-logo.png";
        if (optionDto != null) {
            platformName = Optional.ofNullable(
                    optionDto.getFrameParamMap().get("platform_name")).orElse(
                    platformName);

            String dbLogo = optionDto.getFrameParamMap().get("logo");
            if (StringUtils.isNotBlank(dbLogo)) {
                logo = sysOptionRemoteService.findValue("FILE.URL") + dbLogo;
            }

        }
        info.setPlatformName(platformName);
        info.setLogo(logo);
        return info;
    }
}
