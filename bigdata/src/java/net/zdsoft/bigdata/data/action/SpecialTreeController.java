/**
 * FileName: SpecialTreeController.java
 * Author:   shenke
 * Date:     2018/6/1 上午11:19
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action;

import static net.zdsoft.basedata.entity.Unit.UNIT_CLASS_SCHOOL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.zdsoft.basedata.dto.TreeNodeDto;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.TreeRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.bigdata.data.ChartBusinessType;
import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.entity.MultiReport;
import net.zdsoft.bigdata.data.entity.Subscribe;
import net.zdsoft.bigdata.data.service.ChartService;
import net.zdsoft.bigdata.data.service.CockpitService;
import net.zdsoft.bigdata.data.service.MultiReportService;
import net.zdsoft.bigdata.data.service.SubscribeService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.entity.Screen;
import net.zdsoft.bigdata.datav.service.ScreenService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author shenke
 * @since 2018/6/1 上午11:19
 */
@Controller
@RequestMapping(
        value = "bigdata/tree"
)
public class SpecialTreeController extends BigdataBaseAction {

    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Resource
    private SubscribeService subscribeService;
    @Resource
    private ChartService chartService;
    @Autowired
    private TreeRemoteService treeRemoteService;
    @Resource
    private CockpitService cockpitService;
    @Resource
    private ScreenService screenService;
    @Resource
    private MultiReportService multiReportService;


    /**
     * 获取某单位的所有下级单位树
     */
    @ResponseBody
    @RequestMapping(
            value = "/unit",
            method = RequestMethod.GET
    )
    public Response doGetAllSubUnitTree(
            @RequestParam(value = "chartId", required = false) String chartId,
            /* 是否包含当前单位 */
            @RequestParam("containCurrentUnit") boolean containCurrentUnit,
            @RequestParam(value = "cockpit", required = false, defaultValue = "false") boolean cockpit,
            @RequestParam(value = "tagType", required = false, defaultValue = "0") Integer tagType) {
        Set<String> orderUnits = new HashSet<>();

        boolean hide = false;

        if (tagType != null && (tagType == ChartBusinessType.DATA_BOARD.getBusinessType() || tagType == ChartBusinessType.DATA_REPORT.getBusinessType())) {
            MultiReport multiReport = multiReportService.findOne(chartId);
            if (multiReport != null) {
                if (multiReport.getOrderType() >= OrderType.UNIT_ORDER.getOrderType()) {
                    orderUnits = subscribeService.getSubscribeUnits(chartId).stream()
                            .map(Subscribe::getUnitId).collect(Collectors.toSet());
                }
                hide = !multiReport.getUnitId().equals(getLoginInfo().getUnitId());
            }
        } else {
            if (cockpit) {
                Screen currentCockpit = screenService.findOne(chartId);
                if (currentCockpit != null) {
                    if (currentCockpit.getOrderType() >= OrderType.UNIT_ORDER.getOrderType()) {
                        orderUnits = subscribeService.getSubscribeUnits(chartId).stream()
                                .map(Subscribe::getUnitId).collect(Collectors.toSet());
                    }
                    hide = !currentCockpit.getUnitId().equals(getLoginInfo().getUnitId());
                }

            } else {
                Chart chart = chartService.findOne(chartId);
                if (chart != null) {
                    if (chart.getOrderType() >= OrderType.UNIT_ORDER.getOrderType()) {
                        orderUnits = subscribeService.getSubscribeUnits(chartId).stream()
                                .map(Subscribe::getUnitId).collect(Collectors.toSet());
                    }
                    hide = !chart.getUnitId().equals(getLoginInfo().getUnitId());
                }
            }
        }

        String topUnitId = null;
        if (hide) {
            topUnitId = unitRemoteService.findTopUnitObject(getLoginInfo().getUnitId()).getId();
        }
        String parentUnitId = getLoginInfo().getUnitId();
        List<Unit> units = SUtils.dt(unitRemoteService.getAllSubUnitByParentId(hide?topUnitId : parentUnitId), Unit.class);
        //Unit currentUnit = unitRemoteService.findOneObjectById(parentUnitId);
        //units.add(currentUnit);
        final JSONArray treeArray = new JSONArray();
        for (Unit unit : units) {
            TreeNodeDto treeNode = new TreeNodeDto();
            treeNode.setpId(parentUnitId.equals(unit.getId()) ? "" : unit.getParentId());
            treeNode.setId(unit.getId());
            //为本单位打上标记
            if (StringUtils.equals(parentUnitId, unit.getId())) {
                treeNode.setName(unit.getUnitName() + "(本单位)");
            } else {
                treeNode.setName(unit.getUnitName());
            }
            treeNode.setTitle(unit.getUnitName());
            treeNode.setChecked(orderUnits.contains(unit.getId()));
            if (treeNode.isChecked()) {
                treeNode.setChkDisabled(hide);
            }
            treeNode.setOpen(false);
            if (Integer.valueOf(UNIT_CLASS_SCHOOL).equals(unit.getUnitClass())) {
                treeNode.setType("school");
                treeNode.setIsParent(false);
            } else {
                treeNode.setIsParent(true);
            }
            treeArray.add(JSONObject.toJSON(treeNode));
        }
        return Response.ok().data(JSONObject.toJSONString(treeArray)).build();
    }

    @ResponseBody
    @RequestMapping(
            value = "/teacher",
            method = RequestMethod.GET
    )
    public Response doGetCurrentTeacherTree(@RequestParam(value = "chartId", required = false) String chartId,
                                            @RequestParam(value = "cockpit", required = false, defaultValue = "false") boolean cockpit,
                                            @RequestParam(value = "tagType", required = false, defaultValue = "0") Integer tagType) {
        List<User> orderUsers = new ArrayList<>();
        String currentUnitId = getLoginInfo().getUnitId();
        //只显示本单位的授权
        if (tagType != null && (tagType == ChartBusinessType.DATA_BOARD.getBusinessType() || tagType == ChartBusinessType.DATA_REPORT.getBusinessType())) {
            MultiReport multiReport = multiReportService.findOne(chartId);
            if (multiReport != null && multiReport.getOrderType() >= OrderType.UNIT_ORDER.getOrderType()) {
                orderUsers = userRemoteService.findListObjectByIds(subscribeService.getSubscribeUsers(chartId, multiReport.getOrderType()).stream()
                        .map(Subscribe::getUserId).toArray(String[]::new));
            }
        } else {
            if (cockpit) {
                Screen currentCockpit = screenService.findOne(chartId);
                if (currentCockpit != null && currentCockpit.getOrderType() >= OrderType.UNIT_ORDER.getOrderType()) {
                    orderUsers = userRemoteService.findListObjectByIds(subscribeService.getSubscribeUsers(chartId, currentCockpit.getOrderType()).stream()
                            .map(Subscribe::getUserId).toArray(String[]::new));
                }
            } else {
                Chart chart = chartService.findOne(chartId);
                if (chart != null && chart.getOrderType() >= OrderType.UNIT_ORDER.getOrderType()) {
                    orderUsers = userRemoteService.findListObjectByIds(subscribeService.getSubscribeUsers(chartId, chart.getOrderType()).stream()
                            .map(Subscribe::getUserId).toArray(String[]::new));
                }
            }
        }

        JSONArray array = treeRemoteService.deptTeacherForUnitInsetTree(getLoginInfo().getUnitId());
        for (Object t : array) {
            if (t instanceof JSONObject) {
                JSONObject treeNode = (JSONObject) t;
                if ("teacher".equals(treeNode.getString("type"))) {
                    Optional<User> u = orderUsers.stream().filter(user -> user.getOwnerId().equals(treeNode.getString("id"))).findFirst();
                    treeNode.put("checked", u.isPresent());
                    u.ifPresent(user -> treeNode.put("chkDisabled", !user.getUnitId().equals(currentUnitId)));
                }
            }
        }
        return Response.ok().data(JSONObject.toJSONString(array)).build();
    }

}
