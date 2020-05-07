package net.zdsoft.bigdata.v3.index.action;

import net.zdsoft.bigdata.data.ChartBusinessType;
import net.zdsoft.bigdata.data.export.Statistical;
import net.zdsoft.bigdata.data.service.BiFavoriteService;
import net.zdsoft.bigdata.data.service.BiShareService;
import net.zdsoft.bigdata.system.entity.BgModule;
import net.zdsoft.bigdata.system.service.BgAuthorityService;
import net.zdsoft.bigdata.v3.index.Templates;
import net.zdsoft.bigdata.v3.index.V3Model;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UrlUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller()
@RequestMapping(value = "/bigdata/v3/bi")
public class V3BiController extends BigdataBiBaseAction {

    private static final String ROOT_ID = "00000000000000000000000000000000";

    @Resource
    private BgAuthorityService bgAuthorityService;

    @Resource
    private Statistical statistical;

    @Resource
    private BiFavoriteService biFavoriteService;

    @Resource
    private BiShareService biShareService;

    @GetMapping("/module")
    public String modole(Model model, HttpServletRequest request) {

        LoginInfo loginInfo = getLoginInfo();

        String userType = loginInfo.getUserType().toString();

        List<BgModule> moduleList = bgAuthorityService.getAuthorityModuleList(
                loginInfo.getUserId(), userType, false);
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
        String prefix = UrlUtils.getPrefix(request);
        if (prefix.endsWith("/")) {
            prefix = prefix + "bigdata/v3";
        } else {
            prefix = prefix + "/bigdata/v3";
        }
        return Templates.of("/bi/bi-module.ftl");

    }

    @GetMapping("/list")
    public String list(Model model, Integer group, Integer businessType, HttpServletRequest request) {

        String prefix = UrlUtils.getPrefix(request);
        if (prefix.endsWith("/")) {
            prefix = prefix + "bigdata/v3";
        } else {
            prefix = prefix + "/bigdata/v3";
        }
        model.addAttribute("businessType", businessType);
        model.addAttribute("group", group);
        if (1 == group) {
            String key = getLoginInfo().getUserId() + "-user-report-stat";
            Json statData = RedisUtils.getObject(key, Json.class);
            if (statData == null) {
                statData = new Json();
                statData.put("user_cockpit_num", statistical.countUserReport(
                        getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
                        ChartBusinessType.COCKPIT.getBusinessType()));
                statData.put("user_chart_num", statistical.countUserReport(
                        getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
                        ChartBusinessType.CHART.getBusinessType()));
//                statData.put("user_report_num", statistical.countUserReport(
//                        getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
//                        ChartBusinessType.REPORT.getBusinessType()));
//                statData.put("user_model_report_num", statistical.countUserReport(
//                        getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
//                        ChartBusinessType.MODEL_REPORT.getBusinessType()));
                statData.put("user_data_board_num", statistical.countUserReport(
                        getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
                        ChartBusinessType.DATA_BOARD.getBusinessType()));
                statData.put("user_data_report_num", statistical.countUserReport(
                        getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
                        ChartBusinessType.DATA_REPORT.getBusinessType()));
                RedisUtils.set(key, statData.toJSONString(), 600);
            }
            model.addAttribute("statData", statData);
        } else if (2 == group) {
            String key = getLoginInfo().getUserId() + "-user-diy-stat";
            Json statData = RedisUtils.getObject(key, Json.class);
            if (statData == null) {
                statData = new Json();
                statData.put("user_favorite_num", biFavoriteService.findAllByUserId(getLoginInfo().getUserId()));
                Integer myShareCount = biShareService.findMyShareByUserId(getLoginInfo().getUserId());
                Integer beSharedCount=biShareService.findBeShareByUserId(getLoginInfo().getUserId());
                statData.put("user_share_num",myShareCount+beSharedCount);
                RedisUtils.set(key, statData.toJSONString(), 600);
            }
            model.addAttribute("statData", statData);
        }
        return Templates.of("/bi/bi-list.ftl");
    }


    @GetMapping("/businessCase")
    public String businessCase(Model model, HttpServletRequest request) {

        String prefix = UrlUtils.getPrefix(request);
        if (prefix.endsWith("/")) {
            prefix = prefix + "bigdata/v3";
        } else {
            prefix = prefix + "/bigdata/v3";
        }
        return Templates.of("/bi/bi-case.ftl");
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
        v3Model.setIcon(bgModule.getIcon());
        v3Model.setDescription(bgModule.getDescription());
        return v3Model;
    }

}
