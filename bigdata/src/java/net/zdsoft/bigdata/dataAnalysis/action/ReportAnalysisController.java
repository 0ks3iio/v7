package net.zdsoft.bigdata.dataAnalysis.action;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.zdsoft.bigdata.daq.data.component.BizOperationLogCollector;
import net.zdsoft.bigdata.daq.data.entity.BizOperationLog;
import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.entity.*;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.dataAnalysis.biz.AnalysisBiz;
import net.zdsoft.bigdata.dataAnalysis.entity.QueryParam;
import net.zdsoft.bigdata.dataAnalysis.vo.ReportTableVO;
import net.zdsoft.bigdata.metadata.entity.Folder;
import net.zdsoft.bigdata.metadata.entity.FolderDetail;
import net.zdsoft.bigdata.metadata.entity.FolderEx;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.bigdata.metadata.service.FolderService;
import net.zdsoft.bigdata.system.service.BgUserAuthService;
import net.zdsoft.bigdata.v3.index.action.BigdataBiBaseAction;
import net.zdsoft.bigdata.v3.index.entity.BiBizModule;
import net.zdsoft.bigdata.v3.index.entity.HeadInfo;
import net.zdsoft.framework.entity.LoginInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
 * 多维报表分析
 * Created by wangdongdong on 2019/6/19 8:58.
 */
@Controller
@RequestMapping("/bigdata/data/analyse")
public class ReportAnalysisController extends BigdataBiBaseAction {

    @Resource
    private OptionService optionService;
    @Resource
    private DataModelService dataModelService;
    @Resource
    private DataModelParamService dataModelParamService;
    @Resource
    private ModelDatasetService modelDatasetService;
    @Resource
    private ModelDatasetUserService modelDatasetUserService;
    @Resource
    private BgUserAuthService bgUserAuthService;
    @Resource
    private AnalysisBiz analysisBiz;
    @Resource
    private DataModelFavoriteService dataModelFavoriteService;
    @Resource
    private TagRelationService tagRelationService;
    @Resource
    private DataModelFavoriteParamService dataModelFavoriteParamService;
    @Resource
    private FolderDetailService folderDetailService;
    @Resource
    private TagService tagService;
    @Resource
    private FolderService folderService;

    @RequestMapping("/bi/index")
    public String biIndex(ModelMap map) {
        List<BiBizModule> modules = Lists.newArrayList();

        BiBizModule data_import = new BiBizModule();
        data_import.setCode("data_import");
        data_import.setName("数据导入");
        data_import.setUrl("/bigdata/data/analyse/import");
        modules.add(data_import);

        BiBizModule create_model = new BiBizModule();
        create_model.setCode("model_create");
        create_model.setName("数据建模");
        create_model.setUrl("/bigdata/data/analyse/module");
        modules.add(create_model);

        BiBizModule data_analysis = new BiBizModule();
        data_analysis.setCode("data_analysis");
        data_analysis.setName("多维分析");
        data_analysis.setUrl("/bigdata/data/analyse/analyse");
        modules.add(data_analysis);

        BiBizModule data_board = new BiBizModule();
        data_board.setCode("data_board");
        data_board.setName("数据看板");
        data_board.setUrl("/bigdata/data/analyse/dashboard");
        modules.add(data_board);

        BiBizModule data_report = new BiBizModule();
        data_report.setCode("data_report");
        data_report.setName("数据看板");
        data_report.setUrl("/bigdata/data/analyse/report");
        modules.add(data_report);

        HeadInfo headInfo = getHeadInfo();
        headInfo.setTitle("多维分析");
        map.put("headInfo", headInfo);
        map.put("bizModuleList", modules);
        map.put("currentModuleCode", "data_analysis");
        return "/bigdata/v3/templates/bi/bi-transfer.ftl";
    }

    @RequestMapping("/analyse")
    public String analyseIndex(ModelMap map, String favoriteId) {

        LoginInfo loginInfo = getLoginInfo();
        String userId = loginInfo.getUserId();
        String unitId = loginInfo.getUnitId();

        List<DataModelParam> dataModelParams = dataModelParamService.findAll();
        Map<String, String> paramNameMap = dataModelParams.stream().collect(Collectors.toMap(DataModelParam::getId, DataModelParam::getName));
        map.put("paramNameMap", JSON.toJSONString(paramNameMap));

        if (StringUtils.isNotBlank(favoriteId)) {
            DataModelFavorite dataModelFavorite = dataModelFavoriteService.findOne(favoriteId);
            List<DataModelFavoriteParam> paramList = dataModelFavoriteParamService.findListBy("favoriteId", favoriteId);
            Map<String, String> paramMap = paramList.stream().collect(Collectors.toMap(DataModelFavoriteParam::getParamType, DataModelFavoriteParam::getParamValue));

            map.put("paramMap", paramMap);
            map.put("dataModelFavorite", dataModelFavorite);
        } else {
            map.put("paramMap", Maps.newHashMap());
            map.put("dataModelFavorite", new DataModelFavorite());
        }

        List<DataModel> dataModels = dataModelService.getCurrentUserDataModel(userId, unitId);

        OptionDto impalaDto = optionService.getAllOptionParam("impala");
        OptionDto mysqlDto = optionService.getAllOptionParam("mysql");
        OptionDto kylinDto = optionService.getAllOptionParam("kylin");
        Iterator<DataModel> it = dataModels.iterator();
        while (it.hasNext()) {
            DataModel dataModel = it.next();
            if ("impala".equals(dataModel.getType())) {
                if (impalaDto == null || impalaDto.getStatus() == 0) {
                    it.remove();
                    continue;
                }
            }

            if ("mysql".equals(dataModel.getType())) {
                if (mysqlDto == null || mysqlDto.getStatus() == 0) {
                    it.remove();
                    continue;
                }
            }

            if ("kylin".equals(dataModel.getType())) {
                if (kylinDto == null || kylinDto.getStatus() == 0) {
                    it.remove();
                    continue;
                }
            }
        }

        map.put("models", dataModels);

        map.put("isBackgroundUser", bgUserAuthService.isBackgroundUser(userId, loginInfo.getUserType()));

        return "/bigdata/dataAnalyse/analyseIndex.ftl";
    }

    @RequestMapping("/builderRowHeader")
    @ResponseBody
    public Response builderRowHeader(@RequestParam(value = "dimensionRowParam[]", required = true) String[] dimensionRowParam,
                                     String filterDataMap,
                                     String modelId) {

        try {
            String result = analysisBiz.builderRowHeader((new QueryParam()
                    .setDimensionRowParam(dimensionRowParam)
                    .setFilterDataMap(filterDataMap)
                    .setModelId(modelId)));

            return Response.ok().data(result).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/builderColumnHeader")
    @ResponseBody
    public Response builderColumnHeader(@RequestParam(value = "dimensionColumnParam[]", required = true) String[] dimensionColumnParam,
                                        @RequestParam(value = "measureParam[]", required = false) String[] measureParam,
                                        String filterDataMap,
                                        String modelId) {
        try {
            String result = analysisBiz.builderColumnHeader(new QueryParam()
                    .setDimensionColumnParam(dimensionColumnParam)
                    .setMeasureParam(measureParam)
                    .setFilterDataMap(filterDataMap)
                    .setModelId(modelId));

            return Response.ok().data(result).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/queryReport")
    @ResponseBody
    public Response queryReport(@RequestParam(value = "dimensionRowParam[]", required = false) String[] dimensionRowParam,
                                @RequestParam(value = "dimensionColumnParam[]", required = false) String[] dimensionColumnParam,
                                @RequestParam(value = "measureParam[]", required = false) String[] measureParam,
                                String filterDataMap,
                                String warningDataMap,
                                String modelId) {
        try {
            ReportTableVO reportTableVO = analysisBiz.queryReport(new QueryParam().setDimensionRowParam(dimensionRowParam)
                    .setDimensionColumnParam(dimensionColumnParam)
                    .setMeasureParam(measureParam)
                    .setFilterDataMap(filterDataMap)
                    .setWarningDataMap(warningDataMap)
                    .setContainHeader(false)
                    .setModelId(modelId));
            return Response.ok().data(reportTableVO.getTable()).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/queryReportAll")
    @ResponseBody
    public Response queryReportAll(@RequestParam(value = "dimensionRowParam[]", required = false) String[] dimensionRowParam,
                                   @RequestParam(value = "dimensionColumnParam[]", required = false) String[] dimensionColumnParam,
                                   @RequestParam(value = "measureParam[]", required = false) String[] measureParam,
                                   String orderDataMap,
                                   String filterDataMap,
                                   String warningDataMap,
                                   String modelId) {
        try {
            return Response.ok().data(analysisBiz.queryReport(new QueryParam().setDimensionRowParam(dimensionRowParam)
                    .setDimensionColumnParam(dimensionColumnParam)
                    .setMeasureParam(measureParam)
                    .setFilterDataMap(filterDataMap)
                    .setOrderDataMap(orderDataMap)
                    .setWarningDataMap(warningDataMap)
                    .setContainHeader(true)
                    .setModelId(modelId))).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    /**
     * 只选择指标的情况下查询指标合计
     *
     * @param measureParam
     * @param modelId
     * @return
     */
    @RequestMapping("/queryReportTotal")
    @ResponseBody
    public Response queryReportTotal(@RequestParam(value = "measureParam[]", required = false) String[] measureParam,
                                     String filterDataMap,
                                     String warningDataMap,
                                     String modelId) {

        if (measureParam.length < 1) {
            return Response.error().message("请至少选择一个行维度").build();
        }

        try {
            String result = analysisBiz.queryReportTotal(new QueryParam().setMeasureParam(measureParam)
                    .setModelId(modelId).setFilterDataMap(filterDataMap).setWarningDataMap(warningDataMap));

            return Response.ok().data(result).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    /**
     * 获取过滤选择项
     *
     * @param modelId
     * @param dimensionId
     * @return
     */
    @RequestMapping("/getFilterData")
    @ResponseBody
    public Response getFilterData(String modelId, String dimensionId, String modelDatasetId) {
        try {
            return Response.ok().data(analysisBiz.getFilterData(modelId, dimensionId, modelDatasetId)).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    /**
     * 获取下级维度
     *
     * @param dimensionId
     * @return
     */
    @RequestMapping("/getChildDimension")
    @ResponseBody
    public Response getChildDimension(String dimensionId) {
        try {
            return Response.ok().data(analysisBiz.getChildDimension(dimensionId)).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/queryChildReport")
    @ResponseBody
    public Response queryChildReport(@RequestParam(value = "dimensionRowParam[]", required = false) String[] dimensionRowParam,
                                     @RequestParam(value = "dimensionColumnParam[]", required = false) String[] dimensionColumnParam,
                                     @RequestParam(value = "measureParam[]", required = false) String[] measureParam,
                                     @RequestParam(value = "childDimensionIdArray[]", required = false) String[] childDimensionIdArray,
                                     String orderDataMap,
                                     String filterDataMap,
                                     String childDimensionFilterMap,
                                     String childDimensionId,
                                     String childPosition,
                                     String warningDataMap,
                                     String modelId) {
        try {
            return Response.ok().data(analysisBiz.queryChildReport(new QueryParam().setDimensionRowParam(dimensionRowParam)
                    .setDimensionColumnParam(dimensionColumnParam)
                    .setMeasureParam(measureParam)
                    .setFilterDataMap(filterDataMap)
                    .setOrderDataMap(orderDataMap)
                    .setChildDimensionId(childDimensionId)
                    .setChildDimensionFilterMap(childDimensionFilterMap)
                    .setContainHeader(true)
                    .setChildPosition(childPosition)
                    .setChildDimensionIdArray(childDimensionIdArray)
                    .setWarningDataMap(warningDataMap)
                    .setModelId(modelId))).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.error().message(e.getMessage()).build();
        }
    }

    /**
     * 保存多维报表页面
     *
     * @param map
     * @return
     */
    @RequestMapping("/saveReport")
    public String saveReport(ModelMap map, String code, String favoriteId, Boolean isChart) {
        if (StringUtils.isNotBlank(favoriteId)) {
            DataModelFavorite favorite = dataModelFavoriteService.findOne(favoriteId);
            DataModel model = dataModelService.findOne(favorite.getModelId());
            code = model.getCode();
            map.put("favorite", favorite);
            map.put("tags", getTags(favoriteId));
            // 已选择的条件
            List<DataModelFavoriteParam> params = dataModelFavoriteParamService.findListBy(new String[]{"favoriteId", "paramType"}, new String[]{favoriteId, "condition"});
            if (params.size() > 0) {
                String condition = params.get(0).getParamValue();
                String[] conditionIds = JSON.parseObject(condition, String[].class);
                map.put("conditionIds", conditionIds);
            }
            List<FolderDetail> folderDetails = folderDetailService.findListBy("businessId", favoriteId);
            map.put("folderDetail", folderDetails.size() > 0 ? folderDetails.get(0) : new FolderDetail());
        } else {
            map.put("favorite", new DataModelFavorite());
            List<Tag> tags = tagService.findListBy("tagType", MODEL_REPORT.getBusinessType().shortValue());
            map.put("tags", tags);
            map.put("folderDetail", new FolderDetail());
        }

        List<DataModelParam> dimensionList = dataModelParamService.findByCodeAndType(code, "dimension");
        Map<String, List<Folder>> folderMap = folderService.findAllFolderForDirectory();
        List<FolderEx> folderTree = folderService.findFolderTree();
        map.put("folderMap", folderMap);
        map.put("folderTree", folderTree);
        map.put("dimensionList", dimensionList);

        if (isChart != null && isChart) {
            return "/bigdata/dataAnalyse/chart/saveUI.ftl";
        }
        return "/bigdata/dataAnalyse/report/saveUI.ftl";
    }

    private List<Tag> getTags(String favoriteId) {
        Set<String> selectTag = tagRelationService.getByBusinessId(new String[]{favoriteId}).stream().map(TagRelation::getTagId).collect(Collectors.toSet());
        List<Tag> tags = tagService.findListBy("tagType", MODEL_REPORT.getBusinessType().shortValue());

        if (selectTag.size() < 1) {
            return tags;
        }
        tags.forEach(e -> {
            if (selectTag.contains(e.getId())) {
                e.setSelected(true);
            }
        });
        return tags;
    }

    @RequestMapping("/saveDataModelFavorite")
    @ResponseBody
    public Response saveDataModelFavorite(@RequestParam(value = "tagArrays[]", required = false) String[] tagArrays,
                                          @RequestParam(value = "dimensionRowParam[]", required = false) String[] dimensionRowParam,
                                          @RequestParam(value = "dimensionColumnParam[]", required = false) String[] dimensionColumnParam,
                                          @RequestParam(value = "measureParam[]", required = false) String[] measureParam,
                                          @RequestParam(value = "conditionParam[]", required = false) String[] conditionParam,
                                          String orderDataMap,
                                          String filterDataMap,
                                          String warningDataMap,
                                          String modelDatasetId,
                                          String chartDimensionId,
                                          String chartLegendId,
                                          Boolean isChart,
                                          DataModelFavorite dataModelFavorite) {

        LoginInfo loginInfo = getLoginInfo();
        if (analysisBiz.checkIsExist(dataModelFavorite)) {
            return Response.error().message("该名称已存在!").build();
        }
        dataModelFavorite.setTags(tagArrays);

        DataModelFavoriteParam indexFavoriteParam = new DataModelFavoriteParam();
        indexFavoriteParam.setParamType("index");
        indexFavoriteParam.setParamValue(JSON.toJSONString(measureParam));

        DataModelFavoriteParam rowFavoriteParam = new DataModelFavoriteParam();
        rowFavoriteParam.setParamType("row");
        rowFavoriteParam.setParamValue(JSON.toJSONString(dimensionRowParam));

        DataModelFavoriteParam columnFavoriteParam = new DataModelFavoriteParam();
        columnFavoriteParam.setParamType("column");
        columnFavoriteParam.setParamValue(JSON.toJSONString(dimensionColumnParam));

        DataModelFavoriteParam datasetParam = new DataModelFavoriteParam();
        datasetParam.setParamType("dataset");
        datasetParam.setParamValue(modelDatasetId);

        DataModelFavoriteParam filterDataParam = new DataModelFavoriteParam();
        filterDataParam.setParamType("filterDataMap");
        filterDataParam.setParamValue(filterDataMap);

        DataModelFavoriteParam orderDataParam = new DataModelFavoriteParam();
        orderDataParam.setParamType("orderDataMap");
        orderDataParam.setParamValue(orderDataMap);

        DataModelFavoriteParam warningDataParam = new DataModelFavoriteParam();
        warningDataParam.setParamType("warningDataMap");
        warningDataParam.setParamValue(warningDataMap);

        DataModelFavoriteParam chartDimensionIdParam = new DataModelFavoriteParam();
        chartDimensionIdParam.setParamType("chartDimensionId");
        chartDimensionIdParam.setParamValue(chartDimensionId);

        DataModelFavoriteParam chartLegendIdParam = new DataModelFavoriteParam();
        chartLegendIdParam.setParamType("chartLegendId");
        chartLegendIdParam.setParamValue(chartLegendId);

        dataModelFavorite.getDataModelFavoriteParams().add(indexFavoriteParam);
        dataModelFavorite.getDataModelFavoriteParams().add(rowFavoriteParam);
        dataModelFavorite.getDataModelFavoriteParams().add(columnFavoriteParam);
        dataModelFavorite.getDataModelFavoriteParams().add(datasetParam);
        dataModelFavorite.getDataModelFavoriteParams().add(filterDataParam);
        dataModelFavorite.getDataModelFavoriteParams().add(orderDataParam);
        dataModelFavorite.getDataModelFavoriteParams().add(warningDataParam);
        dataModelFavorite.getDataModelFavoriteParams().add(chartDimensionIdParam);
        dataModelFavorite.getDataModelFavoriteParams().add(chartLegendIdParam);

        if (ArrayUtils.isNotEmpty(conditionParam)) {
            DataModelFavoriteParam conditionFavoriteParam = new DataModelFavoriteParam();
            conditionFavoriteParam.setParamType("condition");
            conditionFavoriteParam.setParamValue(JSON.toJSONString(conditionParam));
            dataModelFavorite.getDataModelFavoriteParams().add(conditionFavoriteParam);
        }

        if (StringUtils.isBlank(dataModelFavorite.getId())) {
            dataModelFavorite.setUserId(loginInfo.getUserId());
            dataModelFavorite.setUnitId(loginInfo.getUnitId());
            dataModelFavorite.setOrderType(OrderType.OPEN.getOrderType());
        }

        try {
            dataModelFavorite.setQuerySql("null");
            dataModelFavorite.setQueryIndex("null");
            dataModelFavorite.setQueryRow("null");
            dataModelFavorite.setQueryColumn("null");
            if (isChart != null && isChart) {
                dataModelFavorite.setIsShowChart(1);
                dataModelFavorite.setIsShowReport(0);
            } else {
                dataModelFavorite.setIsShowReport(1);
                dataModelFavorite.setIsShowChart(0);
            }
            dataModelFavoriteService.saveDataModelFavorite(dataModelFavorite);
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }

        // 业务日志埋点
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_INSERT);
        bizLog.setBizCode("insert-dataModelFavorite");
        bizLog.setDescription("增加多维报表");
        bizLog.setBizName("数据可视化设计");
        bizLog.setSubSystem("大数据管理");
        bizLog.setOperator(loginInfo.getRealName() + "("
                + loginInfo.getUserName() + ")");
        bizLog.setOperationTime(new Date());
        bizLog.setNewData(JSON.toJSONString(dataModelFavorite));
        BizOperationLogCollector.submitBizOperationLog(bizLog);

        return Response.ok().build();
    }

    /**
     * 列表
     *
     * @param map
     * @return
     */
    @RequestMapping("/reportList")
    public String reportList(ModelMap map, String name,
                             @RequestParam(value = "selectTags", required = false) String selectTags,
                             boolean canDelete) {
        //(当前单位新增的)
        List<Tag> tags = tagService.findTagsByUnitIdAndTagType(
                getLoginInfo().getUnitId(), MODEL_REPORT.getBusinessType().shortValue());
        map.put("tags", tags);

        LoginInfo loginInfo = getLoginInfo();
        String currentUnitId = loginInfo.getUnitId();

        String[] tagIdArray = null;
        if (StringUtils.isNotBlank(selectTags)) {
            tagIdArray = StringUtils.trim(selectTags).split(",");
        }

        List<DataModelFavorite> favorites = dataModelFavoriteService.getUserReport(loginInfo.getUserId(), name, tagIdArray);

        favorites.forEach(e -> {
            e.setTags(tagRelationService.findTagRelationByBusinessId(e.getId()).stream().map(TagRelation::getTagName).toArray(String[]::new));
            e.setCanDelete(currentUnitId.equals(e.getUnitId()));
            Optional<OrderType> optional = OrderType.from(e.getOrderType());
            e.setOrderName(optional.isPresent() ? optional.get().getOrderName() : StringUtils.EMPTY);
        });

        map.put("favorites", favorites);
        map.put("name", name);

        return "/bigdata/dataAnalyse/report/reportList.ftl";
    }

    /**
     * 列表
     *
     * @param map
     * @return
     */
    @RequestMapping("/reportManage")
    public String reportList(ModelMap map) {
        //(当前单位新增的)
        List<Tag> tags = tagService.findTagsByUnitIdAndTagType(
                getLoginInfo().getUnitId(), MODEL_REPORT.getBusinessType().shortValue());
        map.put("tags", tags);
        return "/bigdata/dataAnalyse/report/reportManage.ftl";
    }

    /**
     * 授权页面
     *
     * @param map
     * @return
     */
    @RequestMapping("/authorization")
    public String authorization(ModelMap map, String favoriteId) {
        DataModelFavorite dataModelFavorite = dataModelFavoriteService.findOne(favoriteId);
        // 标签
        map.put("orderTypes", OrderType.useValues());
        map.put("orderType", dataModelFavorite.getOrderType());
        map.put("favorite", dataModelFavorite);
        map.put("favoriteJson", JSON.toJSONString(dataModelFavorite));
        return "/bigdata/dataAnalyse/authorization.ftl";
    }

    /**
     * 授权
     *
     * @return
     */
    @RequestMapping("/saveAuthorization")
    @ResponseBody
    public Response saveAuthorization(String favoriteId,
                                      @RequestParam(value = "orderUnit[]", required = false) String[] orderUnits,
                                      @RequestParam(value = "orderTeacher[]", required = false) String[] orderTeachers) {

        return Response.ok().build();
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/deleteModelFavorite")
    @ResponseBody
    public Response deleteModelFavorite(String id) {

        DataModelFavorite dataModelFavorite=dataModelFavoriteService.findOne(id);
        dataModelFavoriteService.deleteDataModelFavorite(id);

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
}
