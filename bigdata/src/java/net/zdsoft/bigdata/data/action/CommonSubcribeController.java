package net.zdsoft.bigdata.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.bigdata.daq.data.component.BizOperationLogCollector;
import net.zdsoft.bigdata.daq.data.entity.BizOperationLog;
import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.entity.*;
import net.zdsoft.bigdata.data.enu.BatchOperateType;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.ShotPath;
import net.zdsoft.bigdata.datav.entity.Screen;
import net.zdsoft.bigdata.datav.service.ScreenService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 授权通用
 * Created by wangdongdong on 2019/2/26 15:42.
 */
@Controller
@RequestMapping("/bigdata/subcribe")
public class CommonSubcribeController extends BigdataBaseAction {

    @Resource
    private DataModelService dataModelService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Resource
    private SubscribeService subscribeService;
    @Resource
    private DataModelFavoriteService dataModelFavoriteService;
    @Resource
    private MultiReportService multiReportService;
    @Resource
    private ChartService chartService;
    @Resource
    private ScreenService screenService;
    @Resource
    private SysOptionRemoteService sysOptionRemoteService;

    // 6、批量授权页面
    @RequestMapping(value = "batchOperate", method = RequestMethod.GET)
    public String batchOperate(ModelMap map, String type, Integer orderType) throws BigDataBusinessException {
        // 获取所有图表
        LoginInfo login = getLoginInfo();
        String currentUnitId = login.getUnitId();
        String userId = login.getUserId();
        BatchOperateType batchOperateType = BatchOperateType.valueOfType(type);
        List data = null;
        String columnName;
        switch (batchOperateType) {
            case CHART:
                data = chartService.getCurrentUserCharts(userId, currentUnitId,
                        null, null, null, true, false, false);
                columnName = "数据图表名称";
                break;
            case REPORT:
                data = chartService.getCurrentUserCharts(userId, currentUnitId,
                        null, null, null, true, true, false);
                columnName = "数据报表名称";
                break;
            case SCREEN:
                data = screenService.getScreensByUnitId(login.getUnitId(), userId);
                columnName = "数据大屏名称";
                break;
            case DATA_MODEL:
                data = dataModelService.findAll(currentUnitId);
                columnName = "数据模型名称";
                break;
            case DATA_MODEL_FAVORITE:
                data = dataModelFavoriteService.findAll(currentUnitId);
                columnName = "多维报表名称";
                break;
            case DATA_BOARD:
                data = multiReportService.findAll(currentUnitId, MultiReport.BOARD);
                columnName = "数据看板名称";
                break;
            case DATA_REPORT:
                data = multiReportService.findAll(currentUnitId, MultiReport.REPORT);
                columnName = "数据报告名称";
                break;
            default:
                throw new BigDataBusinessException("授权类型不存在");
        }

        List<CommonSubcribeController.BatchOperateModel> operateModels = Lists.newArrayList();

        data.forEach(e -> {
            CommonSubcribeController.BatchOperateModel operateModel = new CommonSubcribeController.BatchOperateModel();

            switch (batchOperateType) {
                case CHART:
                case REPORT:
                    operateModel.setId(((Chart) e).getId());
                    operateModel.setName(((Chart) e).getName());
                    operateModel.setOrderType(((Chart) e).getOrderType());
                    operateModel.setUnitId(((Chart) e).getUnitId());
                    break;
                case SCREEN:
                    operateModel.setId(((Screen) e).getId());
                    operateModel.setName(((Screen) e).getName());
                    operateModel.setOrderType(((Screen) e).getOrderType());
                    operateModel.setUnitId(((Screen) e).getUnitId());
                    break;
                case DATA_MODEL:
                    operateModel.setId(((DataModel) e).getId());
                    operateModel.setName(((DataModel) e).getName());
                    operateModel.setOrderType(((DataModel) e).getOrderType());
                    operateModel.setUnitId(((DataModel) e).getUnitId());
                    break;
                case DATA_MODEL_FAVORITE:
                    operateModel.setId(((DataModelFavorite) e).getId());
                    operateModel.setName(((DataModelFavorite) e).getFavoriteName());
                    operateModel.setOrderType(((DataModelFavorite) e).getOrderType());
                    operateModel.setUnitId(((DataModelFavorite) e).getUnitId());
                    break;
                case DATA_BOARD:
                case DATA_REPORT:
                    operateModel.setId(((MultiReport) e).getId());
                    operateModel.setName(((MultiReport) e).getName());
                    operateModel.setOrderType(((MultiReport) e).getOrderType());
                    operateModel.setUnitId(((MultiReport) e).getUnitId());
                    break;
            }
            // 授权单位名称
            operateModel.setOrderUnitsName(this.getOrderUnitNames(operateModel, currentUnitId));
            // 授权用户名称
            operateModel.setOrderUsersName(this.getOrderUserNames(operateModel));

            // 不是本单位创建的图表，不能删除
            if (!currentUnitId.equals(operateModel.getUnitId())) {
                operateModel.setDelete(false);
                operateModel.setUnitAuthorization(false);
                operateModel.setUserAuthorization(true);
                if (OrderType.USER_AUTHORIZATION.getOrderType() == operateModel.getOrderType()) {
                    operateModel.setUserAuthorization(false);
                }
            } else {
                operateModel.setDelete(true);
                operateModel.setUnitAuthorization(true);
                operateModel.setUserAuthorization(true);

                if (OrderType.UNIT_ORDER.getOrderType() == operateModel.getOrderType()) {
                    operateModel.setUserAuthorization(false);
                }

                if (OrderType.USER_AUTHORIZATION.getOrderType() == operateModel.getOrderType()) {
                    operateModel.setUnitAuthorization(false);
                }
            }
            operateModels.add(operateModel);
        });
        // 订阅类型
        map.put("orderTypes", OrderType.useValues());
        map.put("models", operateModels);
        map.put("type", type);
        map.put("columnName", columnName);
        map.put("orderType", orderType == null ? OrderType.USER_AUTHORIZATION.getOrderType() : orderType);

        return "/bigdata/batchOperate/index.ftl";
    }

    /**
     * 授权单位名称
     * @param operateModel
     * @param currentUnitId
     * @return
     */
    private String getOrderUnitNames(BatchOperateModel operateModel, String currentUnitId) {
        if (!operateModel.getUnitId().equals(currentUnitId)) {
            return "上级单位创建，无法查看其他授权单位";
        }

        if (OrderType.USER_AUTHORIZATION.getOrderType() == operateModel.getOrderType()) {
            return "暂无授权单位";
        }

        if (operateModel.getOrderType() < OrderType.UNIT_ORDER.getOrderType()) {
            return String.format("%s 不需要（无法）给下属单位授权", OrderType.from(operateModel.getOrderType()).get().getOrderName());
        }

        StringBuilder orderUnits = new StringBuilder();

        // 获取授权的单位
        List<Subscribe> subscribeUnits = subscribeService.getSubscribeUnits(operateModel.getId());
        if (subscribeUnits.size() < 1) {
            return "暂无授权单位";
        }
        List<Unit> units = unitRemoteService.findListObjectByIds(subscribeUnits.stream().map(Subscribe::getUnitId).toArray(String[]::new));
        // 组装单位名称
        units.stream().forEach(unit -> orderUnits.append(unit.getUnitName()).append("、"));
        return  orderUnits.substring(0, orderUnits.length() - 1);
    }

    /**
     * 授权用户名称
     * @param operateModel
     * @return
     */
    private String getOrderUserNames(BatchOperateModel operateModel) {

        if (OrderType.UNIT_ORDER.getOrderType() == operateModel.getOrderType()) {
            return "本单位用户 不需要授权";
        }

        if (operateModel.getOrderType() < OrderType.UNIT_ORDER_USER_AUTHORIZATION.getOrderType()) {
            return String.format("%s 不需要（无法）给下属单位授权", OrderType.from(operateModel.getOrderType()).get().getOrderName());
        }

        // 授权的用户
        List<Subscribe> subscribeUsers = subscribeService.getSubscribeUsers(operateModel.getId(), operateModel.getOrderType());

        if (subscribeUsers.size() < 1) {
            return "暂无授权用户";
        }

        List<User> users = userRemoteService.findListObjectByIds(subscribeUsers.stream().map(Subscribe::getUserId).toArray(String[]::new));

        StringBuilder orderUserBuilder = new StringBuilder();

        users.stream().forEach(user -> orderUserBuilder.append(user.getRealName()).append("、"));

        return orderUserBuilder.substring(0, orderUserBuilder.length() - 1);
    }

    @ResponseBody
    @RequestMapping(value = "/unit", method = RequestMethod.GET)
    public Response doGetAuthorUnits(
            @RequestParam(value = "chartId", required = false) String chartId) {
        JSONArray unitArray = new JSONArray();
        if (StringUtils.isBlank(chartId)) {
            return Response.ok().data(unitArray.toJSONString()).build();
        }

        List<Unit> units = unitRemoteService.findListObjectByIds(subscribeService
                .getSubscribeUnits(chartId).stream()
                .map(Subscribe::getUnitId).toArray(String[]::new));

        for (Unit u : units) {
            JSONObject j = new JSONObject();
            j.put("unitId", u.getId());
            j.put("unitName", u.getUnitName());
            unitArray.add(j);
        }
        return Response.ok().data(unitArray.toJSONString()).build();
    }

    @ResponseBody
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Response doGetAuthorUsers(
            @RequestParam(value = "chartId", required = false) String chartId, Integer orderType) {
        JSONArray userArray = new JSONArray();
        if (StringUtils.isBlank(chartId)) {
            return Response.ok().data(userArray.toJSONString()).build();
        }

        List<User> users = userRemoteService.findListObjectByIds(subscribeService
                .getSubscribeUsers(chartId, orderType).stream()
                .map(Subscribe::getUserId).toArray(String[]::new));

        for (User u : users) {
            JSONObject j = new JSONObject();
            j.put("userId", u.getId());
            j.put("userName", u.getRealName());
            userArray.add(j);
        }
        return Response.ok().data(userArray.toJSONString()).build();
    }

    @ResponseBody
    @RequestMapping(value = "/batchDelete")
    public Response batchDelete(
            @RequestParam(value = "modelIds[]", required = false) String[] modelIds, String type) throws BigDataBusinessException {
        BatchOperateType batchOperateType = BatchOperateType.valueOfType(type);
        switch (batchOperateType) {
            case CHART:
            case REPORT:
                chartService.batchDelete(modelIds);
                break;
            case SCREEN:
                screenService.batchDelete(modelIds);
                String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
                Arrays.stream(modelIds).forEach(id->{
                    try {
                        Files.deleteIfExists(Paths.get(ShotPath.of(filePath, getLoginInfo().getUnitId(), id)));
                    } catch (IOException e) {
                        //ignore
                    }
                });
                break;
            case DATA_MODEL:
                dataModelService.deleteDataModelByIds(modelIds);
                break;
            case DATA_MODEL_FAVORITE:
                dataModelFavoriteService.deleteDataModelFavoriteByIds(modelIds);
                break;
            case DATA_BOARD:
            case DATA_REPORT:
                multiReportService.deleteMultiReportByIds(modelIds);
                break;
            default:
                throw new BigDataBusinessException("授权类型不存在");
        }

        // 业务日志埋点
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_DELETE);
        bizLog.setBizCode("delete-model-auth");
        bizLog.setDescription("批量删除授权");
        bizLog.setBizName(batchOperateType.getName());
        bizLog.setSubSystem("大数据管理");
        bizLog.setOperator(getLoginInfo().getRealName() + "("
                + getLoginInfo().getUserName() + ")");
        bizLog.setOperationTime(new Date());
        bizLog.setOldData(JSON.toJSONString(modelIds));
        BizOperationLogCollector.submitBizOperationLog(bizLog);
        return Response.ok().build();
    }


    @RequestMapping("/userTreeIndex")
    public String userTreeIndex(String users, ModelMap map) {
        if (StringUtils.isBlank(users))
            users = "[]";
        map.put("users", users);
        return "/bigdata/batchOperate/userTree.ftl";
    }

    @RequestMapping("/unitTreeIndex")
    public String unitTreeIndex(ModelMap map, boolean isChart) {
        if (isChart) {
            return "/bigdata/batchOperate/chart/basicUnitTree.ftl";
        }
        return "/bigdata/batchOperate/basicUnitTree.ftl";
    }

    @ResponseBody
    @RequestMapping(
            value = "authorization",
            method = RequestMethod.POST
    )
    public Response authorization(@RequestParam("chartIds[]") String[] chartIds,
                                         @RequestParam(value = "orderUnits[]", required = false) String[] orderUnits,
                                         @RequestParam(value = "orderUsers[]", required = false) String[] orderUsers,
                                         @RequestParam("orderType") int orderType) {

        if (OrderType.UNIT_ORDER.getOrderType() > orderType) {
            return Response.error().message("非法的类型").build();
        }

        subscribeService.addAuthorization(chartIds, orderUnits, orderUsers, getLoginInfo().getUnitId(), orderType);
        return Response.ok().message("操作成功").build();
    }

    /**
     * 批量操作时可用
     */
    public static class BatchOperateModel {
        private String id;
        private String name;
        private String orderUnitsName;
        private String orderUsersName;
        private Integer orderType;
        private boolean unitAuthorization;
        private boolean userAuthorization;
        private boolean delete;
        private String unitId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrderUnitsName() {
            return orderUnitsName;
        }

        public void setOrderUnitsName(String orderUnitsName) {
            this.orderUnitsName = orderUnitsName;
        }

        public String getOrderUsersName() {
            return orderUsersName;
        }

        public void setOrderUsersName(String orderUsersName) {
            this.orderUsersName = orderUsersName;
        }

        public Integer getOrderType() {
            return orderType;
        }

        public void setOrderType(Integer orderType) {
            this.orderType = orderType;
        }

        public boolean isUnitAuthorization() {
            return unitAuthorization;
        }

        public void setUnitAuthorization(boolean unitAuthorization) {
            this.unitAuthorization = unitAuthorization;
        }

        public boolean isUserAuthorization() {
            return userAuthorization;
        }

        public void setUserAuthorization(boolean userAuthorization) {
            this.userAuthorization = userAuthorization;
        }

        public boolean isDelete() {
            return delete;
        }

        public void setDelete(boolean delete) {
            this.delete = delete;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }
    }

}
