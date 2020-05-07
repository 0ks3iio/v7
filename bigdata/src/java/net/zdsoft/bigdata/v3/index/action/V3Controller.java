package net.zdsoft.bigdata.v3.index.action;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.system.entity.BgModule;
import net.zdsoft.bigdata.system.service.BgAuthorityService;
import net.zdsoft.bigdata.system.service.BgUserAuthService;
import net.zdsoft.bigdata.v3.index.Templates;
import net.zdsoft.bigdata.v3.index.V3Model;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 3。0版本大数据首页
 *
 * @author shenke
 * @since 2019/2/19 上午9:56
 */
@Controller()
@RequestMapping(value = "/bigdata/v3")
public class V3Controller extends BigdataBaseAction {

    private static final String ROOT_ID = "00000000000000000000000000000000";

    @Resource
    private BgAuthorityService bgAuthorityService;

    @Resource
    private BgUserAuthService bgUserAuthService;

    @Resource
    private OptionService optionService;

    @Resource
    private SysOptionRemoteService sysOptionRemoteService;

    @GetMapping("")
    public String execute(Model model, HttpServletRequest request) {

        LoginInfo loginInfo = getLoginInfo();

        String userType = loginInfo.getUserType().toString();

        boolean isTopAdmin = false;
        if (loginInfo.getUserType() == User.USER_TYPE_TOP_ADMIN) {
            isTopAdmin = true;
        }
        boolean isBackgroundUser = bgUserAuthService.isBackgroundUser(
                loginInfo.getUserId(), loginInfo.getUserType());
        if (isBackgroundUser) {
            userType = BgModule.USER_TYPE_BACKGROUND;
        }

        OptionDto optionDto = optionService.getAllOptionParam("desktop");
        String platformName = "万数";
        String logo = UrlUtils.getPrefix(request)
                + "/bigdata/v3/static/images/public/logo.png";
        if (optionDto != null) {
            platformName = Optional.ofNullable(
                    optionDto.getFrameParamMap().get("platform_name")).orElse(
                    platformName);

            String dbLogo = optionDto.getFrameParamMap().get("logo");
            if (StringUtils.isNotBlank(dbLogo)) {
                logo = sysOptionRemoteService.findValue("FILE.URL") + dbLogo;
            }
            model.addAttribute("platformName", platformName);
            model.addAttribute("logoUrl", logo);
        }

        optionDto = optionService.getAllOptionParam("dashboard");
        String homeUrl = optionDto.getFrameParamMap().get("dashboard_url");

        model.addAttribute("homeUrl", homeUrl);
        model.addAttribute("username", loginInfo.getRealName());
        String prefix = UrlUtils.getPrefix(request);
        if (prefix.endsWith("/")) {
            prefix = prefix + "bigdata/v3";
        } else {
            prefix = prefix + "/bigdata/v3";
        }
        model.addAttribute("v3Index", UrlUtils.encode(prefix, "UTF-8"));
        model.addAttribute("userType", userType);

        OptionDto styleDto = optionService.getAllOptionParam("pageStyle");
        String style = styleDto.getFrameParamMap().get("style");

        if ("2".equals(style) && !BgModule.USER_TYPE_BACKGROUND.equals(userType)) {

            logo = UrlUtils.getPrefix(request)
                    + "/bigdata/v3/static/images/bi/wanshu-logo.png";
            model.addAttribute("logoUrl", logo);
            return Templates.of("/bi/bi-index.ftl");
        }

        List<BgModule> moduleList = bgAuthorityService.getAuthorityModuleList(
                loginInfo.getUserId(), userType, isTopAdmin);
        Map<String, List<BgModule>> moduleMap = new HashMap<>();
        for (BgModule bgModule : moduleList) {
            List<BgModule> modules = moduleMap.computeIfAbsent(
                    bgModule.getParentId(), k -> new ArrayList<>());
            modules.add(bgModule);
        }

        List<BgModule> rootList = moduleList.stream()
                .filter(e -> ROOT_ID.equals(e.getParentId()))
                .sorted(Comparator.comparing(BgModule::getOrderId))
                .collect(Collectors.toList());
        List<V3Model> rootV3Model = new ArrayList<>(rootList.size());
        for (BgModule bgModule : rootList) {
            V3Model v3Model = convert(bgModule);
            if ("item".equals(bgModule.getType())) {
                rootV3Model.add(v3Model);
            } else {
                rootV3Model.add(recursiveExecuteChildren(bgModule, moduleMap));
            }
        }
        model.addAttribute("rootList", rootV3Model);
        return Templates.of("/bigdata-index.ftl");
    }

    private V3Model recursiveExecuteChildren(BgModule module,
                                             Map<String, List<BgModule>> moduleMap) {
        V3Model v3Model = convert(module);
        List<BgModule> children = moduleMap.get(module.getId());
        if (children != null && !children.isEmpty()) {
            List<V3Model> childs = new ArrayList<>(children.size());
            for (BgModule child : children) {
                childs.add(recursiveExecuteChildren(child, moduleMap));
            }
            v3Model.setChildren(childs);
        }
        return v3Model;
    }

    private V3Model convert(BgModule bgModule) {
        V3Model v3Model = new V3Model();
        v3Model.setId(bgModule.getId());
        v3Model.setName(bgModule.getName());
        v3Model.setOpenType(bgModule.getOpenType());
        v3Model.setType(bgModule.getType());
        v3Model.setUrl(bgModule.getUrl());
        if (".png".equals(bgModule.getIcon())) {
            v3Model.setIcon("icon-view-fill");
        } else {
            v3Model.setIcon(bgModule.getIcon());
        }
        v3Model.setPinyinName(PinyinUtils.toHanyuPinyin(bgModule.getName(),
                false));
        return v3Model;
    }
}
