package net.zdsoft.bigdata.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import net.zdsoft.bigdata.daq.data.component.BizOperationLogCollector;
import net.zdsoft.bigdata.daq.data.entity.BizOperationLog;
import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.biz.DataModelBiz;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.entity.*;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static net.zdsoft.bigdata.data.ChartBusinessType.MODEL_REPORT;

/**
 * Created by wangdongdong on 2018/8/28 10:46.
 */
@Controller
@RequestMapping("/bigdata/model")
public class DataModelViewController extends BigdataBaseAction {

    @Resource
    private DataModelService dataModelService;
    @Resource
    private DataModelFavoriteService dataModelFavoriteService;
    @Resource
    private DataModelBiz dataModelBiz;
    @Resource
    private TagService tagService;
    @Resource
    private TagRelationService tagRelationService;
    @Resource
    private OptionService optionService;
    @Resource
    private DataModelFavoriteParamService dataModelFavoriteParamService;
    @Resource
    private DataModelParamService dataModelParamService;

    @RequestMapping("/report")
    public String report(ModelMap map) {
        //(当前单位新增的)
        List<Tag> tags = tagService.findTagsByUnitIdAndTagType(
                getLoginInfo().getUnitId(), MODEL_REPORT.getBusinessType().shortValue());
        map.put("tags", tags);
        return "/bigdata/dataModelView/index.ftl";
    }

    @RequestMapping("/report/manage")
    public String manage(ModelMap map) {
        //(当前单位新增的)
        List<Tag> tags = tagService.findTagsByUnitIdAndTagType(
                getLoginInfo().getUnitId(), MODEL_REPORT.getBusinessType().shortValue());
        map.put("tags", tags);
        return "/bigdata/dataModel/manage/index.ftl";
    }

    @RequestMapping("/report/list")
    public String list(ModelMap map, String name, @RequestParam(value = "tags", required = false) String tags, boolean canDelete) {
        LoginInfo loginInfo = getLoginInfo();
        String currentUnitId = loginInfo.getUnitId();
        Pagination pagination = createPagination();
        String[] tagIdArray = null;
        if (StringUtils.isNotBlank(tags)) {
            tagIdArray = StringUtils.trim(tags).split(",");
        }

        List<DataModelFavorite> favorites = dataModelFavoriteService.getUserReport(currentUnitId, loginInfo.getUserId(), name, tagIdArray, pagination, canDelete);
        favorites.forEach(e-> {
            e.setTags(tagRelationService.findTagRelationByBusinessId(e.getId()).stream().map(TagRelation::getTagName).toArray(String[]::new));
            e.setCanDelete(currentUnitId.equals(e.getUnitId()));
            Optional<OrderType> optional = OrderType.from(e.getOrderType());
            e.setOrderName(optional.isPresent() ? optional.get().getOrderName() : StringUtils.EMPTY);
        });
        map.put("favorites", favorites);
        map.put("name", name);
        map.put("pagination", pagination);
        map.put("canDelete", canDelete);
        map.put("isSearch", StringUtils.isNotBlank(name));
        map.put("orderTypes", OrderType.useValues());
        map.put("isTagSearch", tagIdArray != null);
        map.put("currentUnitId", currentUnitId);
        return "/bigdata/dataModelView/list.ftl";
    }

    @RequestMapping("/report/view")
    public String view(ModelMap map, String id, boolean isEdit, boolean showTitle, @RequestParam(defaultValue = "true") boolean showChart) {
        DataModelFavorite favorite = dataModelFavoriteService.findOne(id);

        if (favorite == null) {
            map.put("errorMsg", "该多维报表已经被删除");
            return "/bigdata/v3/common/error.ftl";
        }
        DataModel model = dataModelService.findOne(favorite.getModelId());
        OptionDto impalaDto = optionService.getAllOptionParam("impala");
        OptionDto mysqlDto = optionService.getAllOptionParam("mysql");
        OptionDto kylinDto = optionService.getAllOptionParam("kylin");

        if ("impala".equals(model.getType())) {
            if (impalaDto == null || impalaDto.getStatus() == 0) {
                map.put("serverName", "Impala");
                map.put("serverCode", "impala");
                return "/bigdata/noServer.ftl";
            }
        }

        if ("mysql".equals(model.getType())) {
            if (mysqlDto == null || mysqlDto.getStatus() == 0) {
                map.put("serverName", "Mysql");
                map.put("serverCode", "mysql");
                return "/bigdata/noServer.ftl";
            }
        }

        if ("kylin".equals(model.getType())) {
            if (kylinDto == null || kylinDto.getStatus() == 0) {
                map.put("serverName", "Kylin");
                map.put("serverCode", "kylin");
                return "/bigdata/noServer.ftl";
            }
        }

        Map<String, String> paramMap = this.getParamMap(favorite.getId());
        if (paramMap != null) {
            String condition = paramMap.get("condition");
            String[] conditionIds = JSON.parseObject(condition, String[].class);
            if (ArrayUtils.isNotEmpty(conditionIds)) {
                map.put("dataModelParams", dataModelParamService.findListByIdIn(conditionIds));
            }
            String filterDataMap = paramMap.get("filterDataMap");
            if (StringUtils.isNotBlank(filterDataMap)) {
                Map<String, String[]> filterMap = JSON.parseObject(filterDataMap, new TypeReference<Map<String, String[]>>() {});
                map.put("filterMap", filterMap);
            }
            map.put("modelDatasetId", paramMap.get("dataset"));
        }

        map.put("favorite", favorite);
        map.put("isEdit", isEdit);
        map.put("showTitle", showTitle);
        map.put("code", model.getCode());
        if (showChart) {
            return "/bigdata/dataModelView/view.ftl";
        }

        return "/bigdata/dataModelView/noChartView.ftl";
    }

    @RequestMapping("/view/showReport")
    @ResponseBody
    public Response showReport(String favoriteId, String filterDataMap) {
        DataModelFavorite favorite = dataModelFavoriteService.findOne(favoriteId);
        DataModel dataModel = dataModelService.findOne(favorite.getModelId());
        if (StringUtils.isNotBlank(filterDataMap) && !"[]".equals(filterDataMap)) {
            Map<String, String> paramMap = this.getParamMap(favoriteId);
            return dataModelBiz.getSearchResult(dataModel.getCode(), paramMap.get("index"), paramMap.get("row"),
                    paramMap.get("column"), filterDataMap, true, null,
                    paramMap.get("dataset"), null, getLoginInfo());
        }

        List<Json> resultFieldList = Lists.newArrayList();
        List<Json> rowDimensionList = JSON.parseArray(favorite.getQueryRow(), Json.class);
        List<Json> columnDimensionList = JSON.parseArray(favorite.getQueryColumn(), Json.class);
        List<Json> indexList = JSON.parseArray(favorite.getQueryIndex(), Json.class);

        constructResultField(resultFieldList, columnDimensionList);
        constructResultField(resultFieldList, rowDimensionList);
        constructResultField(resultFieldList, indexList);

        return dataModelBiz.getReportResult(dataModel, favorite.getQuerySql(), resultFieldList, rowDimensionList, columnDimensionList, indexList, getLoginInfo());
    }


    @RequestMapping("/report/delete")
    @ResponseBody
    public Response delete(String favoriteId) {
    	DataModelFavorite dataModelFavorite=dataModelFavoriteService.findOne(favoriteId);
        dataModelFavoriteService.deleteDataModelFavorite(favoriteId);

        // 业务日志埋点
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_DELETE);
        bizLog.setBizCode("delete-dataModelFavorite");
        bizLog.setDescription("删除多维报表");
        bizLog.setBizName("多维报表");
        bizLog.setSubSystem("大数据管理");
        bizLog.setOperator(getLoginInfo().getRealName() + "("
                + getLoginInfo().getUserName() + ")");
        bizLog.setOperationTime(new Date());
        bizLog.setOldData(JSON.toJSONString(dataModelFavorite));
        BizOperationLogCollector.submitBizOperationLog(bizLog);
        return Response.ok().build();
    }

    @RequestMapping("/report/changeOrderType")
    @ResponseBody
    public Response changeOrderType(String id, Integer orderType) {
        DataModelFavorite dataModelFavorite=dataModelFavoriteService.findOne(id);
        dataModelFavorite.setOrderType(orderType);
        dataModelFavoriteService.updateOrderType(dataModelFavorite);

        // 业务日志埋点
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_UPDATE);
        bizLog.setBizCode("delete-dataModelFavorite");
        bizLog.setDescription("修改授权类型");
        bizLog.setBizName("多维报表");
        bizLog.setSubSystem("大数据管理");
        bizLog.setOperator(getLoginInfo().getRealName() + "("
                + getLoginInfo().getUserName() + ")");
        bizLog.setOperationTime(new Date());
        bizLog.setOldData(JSON.toJSONString(dataModelFavorite));
        BizOperationLogCollector.submitBizOperationLog(bizLog);
        return Response.ok().build();
    }

    @RequestMapping("/view/showChart")
    @ResponseBody
    public Response showChart(String favoriteId, String type, String filterDataMap) {
        DataModelFavorite favorite = dataModelFavoriteService.findOne(favoriteId);
        DataModel dataModel = dataModelService.findOne(favorite.getModelId());
        if (StringUtils.isNotBlank(filterDataMap) && !"[]".equals(filterDataMap)) {
            Map<String, String> paramMap = this.getParamMap(favoriteId);
            return dataModelBiz.getSearchResult(dataModel.getCode(), paramMap.get("index"), paramMap.get("row"),
                    paramMap.get("column"), filterDataMap, false, type,
                    paramMap.get("dataset"), null, getLoginInfo());
        }

        List<Json> resultFieldList = Lists.newArrayList();
        List<Json> rowDimensionList = JSON.parseArray(favorite.getQueryRow(), Json.class);
        List<Json> originRowDimensionList = JSON.parseArray(favorite.getQueryRow(), Json.class);
        if (rowDimensionList.size() > 1) {
            rowDimensionList = rowDimensionList.subList(rowDimensionList.size()-1, rowDimensionList.size());
        }
        List<Json> columnDimensionList = JSON.parseArray(favorite.getQueryColumn(), Json.class);
        if (columnDimensionList.size() > 1) {
            columnDimensionList = columnDimensionList.subList(columnDimensionList.size()-1, columnDimensionList.size());
        }
        List<Json> indexList = JSON.parseArray(favorite.getQueryIndex(), Json.class);

        constructResultField(resultFieldList, columnDimensionList);
        constructResultField(resultFieldList, rowDimensionList.subList(rowDimensionList.size()-1, rowDimensionList.size()));
        constructResultField(resultFieldList, indexList);

        return dataModelBiz.getChartResult(dataModel, favorite.getQueryChartSql(), resultFieldList, originRowDimensionList, rowDimensionList, columnDimensionList, indexList, StringUtils.isNotBlank(type) ? type : favorite.getChartType(), getLoginInfo());
    }

    private void constructResultField(List<Json> resultFieldList, List<Json> paramList) {
        paramList.forEach(e -> {
            Json rJson = new Json();
            rJson.put("field", e.getString("key"));
            rJson.put("dataType", "string");
            rJson.put("paramId", e.getString("paramId"));
            resultFieldList.add(rJson);
        });
    }

    private Map<String, String> getParamMap(String favoriteId) {
        List<DataModelFavoriteParam> favoriteParams = dataModelFavoriteParamService.findListBy("favoriteId", favoriteId);
        if (favoriteParams.size() > 0) {
            return favoriteParams.stream().collect(Collectors.toMap(DataModelFavoriteParam::getParamType, DataModelFavoriteParam::getParamValue));
        }
        return null;
    }
}
