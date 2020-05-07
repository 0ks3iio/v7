package net.zdsoft.bigdata.data.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.bigdata.base.entity.PropertyTag;
import net.zdsoft.bigdata.base.service.PropertyTagService;
import net.zdsoft.bigdata.daq.data.component.BizOperationLogCollector;
import net.zdsoft.bigdata.daq.data.entity.BizOperationLog;
import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.entity.*;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.frame.data.impala.ImpalaClientService;
import net.zdsoft.bigdata.frame.data.kylin.KylinClientService;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.entity.MetadataTag;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.bigdata.metadata.service.MetadataTagService;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.element.inner.LegendData;
import net.zdsoft.echarts.enu.*;
import net.zdsoft.echarts.series.Pie;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.BarData;
import net.zdsoft.echarts.style.ItemStyle;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 多维报表 Created by wangdongdong on 2019/1/14 10:36.
 */
@Service
public class DataModelBiz {

    @Resource
    private DataModelService dataModelService;
    @Resource
    private DataModelParamService dataModelParamService;
    @Resource
    private KylinClientService kylinClientService;
    @Resource
    private MysqlClientService mysqlClientService;
    @Resource
    private ImpalaClientService impalaClientService;
    @Resource
    private UnitRemoteService unitRemoteService;
    @Resource
    private ModelDatasetService modelDatasetService;
    @Resource
    private DataModelFavoriteService dataModelFavoriteService;
    @Resource
    private SubscribeService subscribeService;
    @Resource
    private MetadataService metadataService;
    @Resource
    private MetadataTableColumnService metadataTableColumnService;
    @Resource
    private PropertyTagService propertyTagService;
    @Resource
    private MetadataTagService metadataTagService;

    public List<String> getFilterData(String code, String dimensionId,
                                      String modelDatasetId) {
        List<DataModel> list = dataModelService.findListBy("code", code);
        DataModel dataModel = list.get(0);
        String projectName = dataModel.getProject();

        DataModelParam dataModelParam = dataModelParamService
                .findOne(dimensionId);
        if ("date".equals(dataModelParam.getCode())) {
            dataModelParam.setUseTable(dataModel.getDateDimTable());
        }
        if (dataModel.getSource() == 0) {
            dataModelParam.setUseTable(metadataService.findOne(dataModelParam.getUseTable()).getTableName());
            dataModelParam.setUseField(metadataTableColumnService.findOne(dataModelParam.getUseField()).getColumnName());
            dataModelParam.setOrderField(dataModelParam.getOrderField() != null ? metadataTableColumnService.findOne(dataModelParam.getOrderField()).getColumnName() : null);
        }

        Json field = new Json();
        field.put("field", dataModelParam.getUseField());
        field.put("dataType", "string");
        // 组装sql
        StringBuilder sql = new StringBuilder("select distinct ")
                .append(dataModelParam.getUseField()).append(" from ")
                .append(dataModelParam.getUseTable());

        // 加入用户数据集筛选
        if (dataModel.getUserDatasetSwitch() != null
                && dataModel.getUserDatasetSwitch() == 1) {
            if (StringUtils.isBlank(modelDatasetId)) {
                return Lists.newArrayList();
            }
            ModelDataset dataset = modelDatasetService.findOne(modelDatasetId);
            if (dataset.getDsConditionSql().contains(
                    dataModelParam.getUseField())) {
                sql.append(" where ").append(dataset.getDsConditionSql());
            }
        }

        String dataSourceId = (dataModel.getSource()!= null && dataModel.getSource() == 1) ? dataModel.getDbId() : null;

        List<Json> result = new ArrayList<>();
        if ("kylin".equals(dataModel.getType())) {
            result = kylinClientService.getDataListFromKylin(dataSourceId, projectName,
                    sql.toString(), Lists.newArrayList(),
                    Lists.newArrayList(field));
        } else if ("impala".equals(dataModel.getType())) {
            result = impalaClientService.getDataListFromImpala(dataSourceId,
                    null, sql.toString(),
                    Lists.newArrayList(), Lists.newArrayList(field));
        } else if ("mysql".equals(dataModel.getType())) {
            result = mysqlClientService.getDataListFromMysql(null,
                    null, sql.toString(),
                    Lists.newArrayList(), Lists.newArrayList(field));
        }

        List<String> filterData = Lists.newArrayList();
        result.forEach(e -> filterData.add(e.getString(dataModelParam
                .getUseField())));
        return filterData;
    }

    public Response getSearchResult(String code, String indexParam,
                                    String dimensionRowParam, String dimensionColumnParam,
                                    String filterDataMap, boolean isResport, String chartType,
                                    String modelDatasetId, DataModelFavorite dataModelFavorite,
                                    LoginInfo loginInfo) {
        Map<String, String[]> filterData = JSON.parseObject(filterDataMap,
                new TypeReference<HashMap<String, String[]>>() {
                });

        List<DataModel> list = dataModelService.findListBy("code", code);
        DataModel dataModel = list.get(0);

        // 若启用了用户数据集 没有选择用户数据集 则返回空
        if (dataModel.getUserDatasetSwitch() != null
                && dataModel.getUserDatasetSwitch() == 1
                && StringUtils.isBlank(modelDatasetId)) {
            return Response.error().message("请至少选择一个数据集").build();
        }

        String[] indexParamIds = JSON.parseArray(indexParam).toArray(
                new String[]{});
        String[] dimensionRowParamIds = JSON.parseArray(dimensionRowParam)
                .toArray(new String[]{});
        String[] dimensionColumnParamIds = JSON
                .parseArray(dimensionColumnParam).toArray(new String[]{});

        if (indexParamIds.length < 1) {
            return Response.error().message("请至少选择一个指标").build();
        }

        if (dimensionRowParamIds.length < 1) {
            return Response.error().message("请至少选择一个行维度").build();
        }
        // 组装查询参数
        ModelSearchParam modelSearchParam = new ModelSearchParam(isResport, modelDatasetId, loginInfo, filterData, dataModel, indexParamIds, dimensionRowParamIds, dimensionColumnParamIds);
        modelSearchParam.chartType = chartType;
        // 过滤查询参数
        this.filterModelSearchParam(modelSearchParam);
        // 保存
        if (dataModelFavorite != null) {
            dataModelFavorite.setModelId(dataModel.getId());
            dataModelFavorite.setQuerySql(modelSearchParam.sql);
            dataModelFavorite.setQueryRow(JSON.toJSONString(modelSearchParam.rowDimensionList));
            dataModelFavorite.setQueryColumn(JSON.toJSONString(modelSearchParam.columnDimensionList));
            dataModelFavorite.setQueryIndex(JSON.toJSONString(modelSearchParam.indexList));
            ModelSearchParam chartSearchParam = new ModelSearchParam(false, modelDatasetId, loginInfo, filterData, dataModel, indexParamIds, dimensionRowParamIds, dimensionColumnParamIds);
            dataModelFavorite.setQueryChartSql(chartSearchParam.sql);
            if (StringUtils.isBlank(dataModelFavorite.getId())) {
                dataModelFavorite.setUserId(loginInfo.getUserId());
                dataModelFavorite.setUnitId(loginInfo.getUnitId());
                dataModelFavorite.setOrderType(OrderType.OPEN.getOrderType());
            }
            try {
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

        if (isResport) {
            return this
                    .getReportResult(dataModel, modelSearchParam.sql, modelSearchParam.resultFieldList,
                            modelSearchParam.rowDimensionList, modelSearchParam.columnDimensionList, modelSearchParam.indexList,
                            loginInfo);
        }

        return this.getChartResult(dataModel, modelSearchParam.sql, modelSearchParam.resultFieldList,
                null, modelSearchParam.rowDimensionList, modelSearchParam.columnDimensionList, modelSearchParam.indexList, chartType,
                loginInfo);
    }

    private void filterModelSearchParam(ModelSearchParam modelSearchParam) {

        if (modelSearchParam.filterData.size() > 0) {
            Iterator<Json> columnIt = modelSearchParam.columnDimensionList.iterator();
            while (columnIt.hasNext()) {
                Json e = columnIt.next();
                if (!modelSearchParam.filterData.containsKey(e.getString("paramId"))
                        || !modelSearchParam.selectParam.contains(e.getString("paramId"))) {
                    columnIt.remove();
                }
            }

            // 过滤掉返回结果
            Iterator<Json> resultIt = modelSearchParam.resultFieldList.iterator();
            while (resultIt.hasNext()) {
                Json e = resultIt.next();
                if (!modelSearchParam.selectParam.contains(e.getString("paramId"))) {
                    resultIt.remove();
                }
            }

            Iterator<Json> rowIt = modelSearchParam.rowDimensionList.iterator();
            while (rowIt.hasNext()) {
                Json e = rowIt.next();
                if (!modelSearchParam.filterData.containsKey(e.getString("paramId"))
                        || !modelSearchParam.selectParam.contains(e.getString("paramId"))) {
                    rowIt.remove();
                }
            }
        }

        modelSearchParam.indexParamList.forEach(e -> {
            Json rJson = new Json();
            String indexKey = StringUtils.isBlank(e.getUseField()) ? (e
                    .getUseTable() + "_" + e.getMeasures()) : (e.getUseField()
                    + "_" + e.getMeasures());
            rJson.put("field", indexKey);
            rJson.put("dataType", "string");
            rJson.put("paramId", e.getId());
            rJson.put("orderJson", e.getOrderJson());
            modelSearchParam.resultFieldList.add(rJson);
        });

        // 如果列存在了 则删除对应的行维度
        modelSearchParam.columnDimensionList.forEach(e -> {
            for (int i = modelSearchParam.rowDimensionList.size() - 1; i >= 0; i--) {
                if (modelSearchParam.rowDimensionList.get(i).getString("paramId")
                        .equals(e.getString("paramId"))) {
                    modelSearchParam.rowDimensionList.remove(i);
                }
            }
        });
    }

    public Response getReportResult(DataModel dataModel, String sql,
                                    List<Json> resultFieldList, List<Json> rowDimensionList,
                                    List<Json> columnDimensionList, List<Json> indexList,
                                    LoginInfo loginInfo) {
        // 业务日志埋点
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_QUERY);
        bizLog.setBizCode("model-query");
        bizLog.setBizName(dataModel.getName());
        bizLog.setSubSystem("大数据管理");
        bizLog.setDescription(dataModel.getName() + "查询");
        bizLog.setOperator(loginInfo.getRealName() + "("
                + loginInfo.getUserName() + ")");
        bizLog.setOperationTime(new Date());
        BizOperationLogCollector.submitBizOperationLog(bizLog);

        String dataSourceId = (dataModel.getSource()!= null && dataModel.getSource() == 1) ? dataModel.getDbId() : null;

        if ("kylin".equals(dataModel.getType())) {
            String result = kylinClientService.getDataListFromKylin(dataSourceId,
                    dataModel.getProject(), sql, new ArrayList<>(),
                    resultFieldList, rowDimensionList, columnDimensionList,
                    indexList);
            return Response.ok().data(result).message(sql).build();
        }

        if ("impala".equals(dataModel.getType())) {
            String result = impalaClientService.getDataListFromImpala(dataSourceId,
                    null, sql, new ArrayList<>(),
                    resultFieldList, rowDimensionList, columnDimensionList,
                    indexList);
            return Response.ok().data(result).message(sql).build();
        }

        String result = mysqlClientService.getDataListFromMysql(dataSourceId,
                null, sql, new ArrayList<>(), resultFieldList,
                rowDimensionList, columnDimensionList, indexList);
        return Response.ok().data(result).message(sql).build();
    }

    public Response getChartResult(DataModel dataModel, String sql,
                                   List<Json> resultFieldList, List<Json> originRowDimensionList, List<Json> rowDimensionList,
                                   List<Json> columnDimensionList, List<Json> indexList,
                                   String chartType, LoginInfo loginInfo) {
        List<Json> result = null;

        // 业务日志埋点
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_QUERY);
        bizLog.setBizCode("model-query");
        bizLog.setBizName(dataModel.getName());
        bizLog.setSubSystem("大数据管理");
        bizLog.setDescription(dataModel.getName() + "查询");
        bizLog.setOperator(loginInfo.getRealName() + "("
                + loginInfo.getUserName() + ")");
        bizLog.setOperationTime(new Date());
        BizOperationLogCollector.submitBizOperationLog(bizLog);

        String dataSourceId = (dataModel.getSource()!= null && dataModel.getSource() == 1) ? dataModel.getDbId() : null;
        if ("kylin".equals(dataModel.getType())) {
            result = kylinClientService.getDataListFromKylin(dataSourceId,
                    dataModel.getProject(), sql, new ArrayList<>(),
                    resultFieldList);
        } else if ("impala".equals(dataModel.getType())) {
            result = impalaClientService.getDataListFromImpala(dataSourceId,
                    null, sql, new ArrayList<>(),
                    resultFieldList);
        } else {
            result = mysqlClientService.getDataListFromMysql(dataSourceId,
                    null, sql, new ArrayList<>(),
                    resultFieldList);
        }

        // 行名称
        String rowKey = rowDimensionList.size() > 0 ? rowDimensionList.get(0)
                .getString("key") : "";
        // 列名称
        String columnKey = columnDimensionList.size() > 0 ? columnDimensionList
                .get(0).getString("key") : "";

        if (rowDimensionList.size() > 1 && StringUtils.isBlank(columnKey)) {
            rowKey = rowDimensionList.get(rowDimensionList.size() - 1).getString("key");
            columnKey = rowDimensionList.get(rowDimensionList.size() - 2).getString("key");
        }

        List<JData.Entry> entryList = Lists.newArrayList();
        boolean isMutilSeries = true;
        if (indexList.size() > 1 && result.size() == 1) {
            String key = rowKey;
            if (StringUtils.isBlank(key) && StringUtils.isNotBlank(columnKey)) {
                key = columnKey;
            }
            for (Json index : indexList) {
                JData.Entry entry = new JData.Entry();
                entry.setX(index.getString("name"));
                entry.setY(result.get(0).getString(index.getString("key")));
                entry.setName(result.get(0).getString(key));
                entryList.add(entry);
            }
        } else if (indexList.size() == 1 && StringUtils.isNotBlank(rowKey)
                && StringUtils.isNotBlank(columnKey)) {
            for (Json data : result) {
                JData.Entry entry = new JData.Entry();
                if (StringUtils.isBlank(data.getString(rowKey))) {
                    if (originRowDimensionList != null) {
                        Iterator<Json> iterator = originRowDimensionList.iterator();
                        while (StringUtils.isBlank(data.getString(rowKey)) && iterator.hasNext()) {
                            rowKey = iterator.next().getString("key");
                        }
                    }
                }

                entry.setX(StringUtils.defaultIfBlank(data.getString(rowKey),
                        "未知"));
                entry.setY(data.getString(indexList.get(0).getString("key")));
                entry.setName(StringUtils.defaultIfBlank(
                        data.getString(columnKey), "未知"));
                entryList.add(entry);
            }
        } else {
            String key = rowKey;
            if (StringUtils.isBlank(key) && StringUtils.isNotBlank(columnKey)) {
                key = columnKey;
            }
            for (Json data : result) {
                for (Json index : indexList) {
                    JData.Entry entry = new JData.Entry();
                    entry.setX(StringUtils.defaultIfBlank(
                            data.getString(key), "未知"));
                    entry.setY(data.getString(index.getString("key")));
                    entry.setName(index.getString("name"));
                    entryList.add(entry);
                }
            }

            if (indexList.size() == 1) {
                isMutilSeries = false;
            }
        }

        if (resultFieldList.size() > 0) {
            String orderJson = resultFieldList.get(0).getString("orderJson");
            if (net.zdsoft.framework.utils.StringUtils.isNotBlank(orderJson)) {
                Map<String, String> sortMap = JSON.parseObject(orderJson,
                        new TypeReference<Map<String, String>>() {
                        });
                entryList.sort(Comparator.comparing(o -> sortMap.get(o.getX())));
            }
        }

        if (entryList.size() < 1) {
            return Response.error().message(sql).build();
        }

        if ("line".equals(chartType)) {
            return Response.ok().message(sql).data(getLineChart(entryList, isMutilSeries).getData()).build();
        }

        if ("cookie".equals(chartType)) {
            return Response.ok().message(sql).data(getPieChart(entryList, isMutilSeries).getData()).build();
        }

        return Response.ok().message(sql).data(getBarChart(entryList, isMutilSeries).getData()).build();
    }

    private Response getBarChart(List<JData.Entry> entryList,
                                 boolean isMutilSeries) {
        JData data = new JData();
        data.setType(SeriesEnum.bar);
        data.setCoordSys(CoordinateSystem.cartesian2d);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);
        data.setXAxisType(AxisType.category);
        JData.JAxisPosition xp = new JData.JAxisPosition();
        JData.JAxisPosition yp = new JData.JAxisPosition();
        data.setXJAxisPosition(xp);
        data.setYJAxisPosition(yp);

        Option option = new Option();

        JConverter.convert(data, option);
        for (Cartesian2dAxis dAxis : option.xAxis()) {
            dAxis.axisLabel().interval(0);
        }
        // 设置标题，提示框等
        option.title().show(true).left(LeftEnum.left);
        Tooltip tooltip = new Tooltip().option(option);
        tooltip.axisPointer().type(AxisPointerType.shadow);
        option.tooltip(tooltip);
        option.legend().show(true).right(RightEnum.center)
                .type(LegendEnum.scroll).top(TopEnum.top)
                .orient(Orient.horizontal);

        // 不是多系列的颜色随机
        if (!isMutilSeries) {
            option.legend().show(false);
            String[] color = new String[]{"#597ef7", "#36cfc9", "#bae637",
                    "#ffc53d", "#ff7a45", "#f759ab", "#40a9ff", "#73d13d",
                    "#ffec3d", "#ffa940", "#ff4d4f", "#9254de"};

            for (Series e : option.series()) {
                int i = 0;
                for (Object b : e.getData()) {
                    BarData bd = (BarData) b;
                    ItemStyle itemStyle = new ItemStyle();
                    itemStyle.color(color[i++ % color.length]);
                    bd.itemStyle(itemStyle);
                }
            }
        }

        option.grid().forEach(
                e -> {
                    e.left(LeftEx.create(14)).right(RightEx.create(20))
                            .top(TopEx.create(40)).bottom(BottomEx.create("5%")).containLabel(true);
                });

        tooltip.trigger(Trigger.axis);
        if (option.xAxis().size() > 0) {
            int xNumber = option.xAxis().get(0).getData().size();
            if (xNumber > 10) {
                for (Cartesian2dAxis e : option.xAxis()) {
                    e.axisLabel().interval(xNumber / 5);
                }
            }
        }

        return Response.ok().data(JSON.toJSON(option).toString()).build();
    }

    private Response getLineChart(List<JData.Entry> entryList,
                                  boolean isMutilSeries) {
        JData data = new JData();
        data.setType(SeriesEnum.line);
        data.setCoordSys(CoordinateSystem.cartesian2d);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);

        Option option = new Option();

        JConverter.convert(data, option);
        for (Cartesian2dAxis dAxis : option.xAxis()) {
            dAxis.axisLabel().interval(0);
        }
        // 设置标题，提示框等
        option.title().show(true).left(LeftEnum.left);
        option.setAnimation(true);

        Tooltip tooltip = new Tooltip().option(option);
        tooltip.axisPointer().type(AxisPointerType.shadow);
        option.tooltip(tooltip);
        option.legend().show(true).right(RightEnum.center)
                .type(LegendEnum.scroll).top(TopEnum.top)
                .orient(Orient.horizontal);
        tooltip.trigger(Trigger.axis);
        if (!isMutilSeries) {
            option.legend().show(false);
            String[] color = new String[]{"#597ef7", "#36cfc9", "#bae637",
                    "#ffc53d", "#ff7a45", "#f759ab", "#40a9ff", "#73d13d",
                    "#ffec3d", "#ffa940", "#ff4d4f", "#9254de"};

            Random random = new Random();
            int index = random.nextInt(10) % (10 - 0 + 1) + 0;
            for (Series e : option.series()) {
                ItemStyle itemStyle = new ItemStyle();
                itemStyle.color(color[index]);
                e.setItemStyle(itemStyle);
            }
        }

        if (option.xAxis().size() > 0) {
            int xNumber = option.xAxis().get(0).getData().size();
            if (xNumber > 10) {
                for (Cartesian2dAxis e : option.xAxis()) {
                    e.axisLabel().interval(xNumber / 5);
                }
            }
        }

        option.grid().forEach(
                e -> {
                    e.left(LeftEx.create(14)).right(RightEx.create(20))
                            .top(TopEx.create(40)).bottom(BottomEx.create("5%")).containLabel(true);
                });
        return Response.ok().data(JSON.toJSON(option).toString()).build();
    }

    private Response getPieChart(List<JData.Entry> entryList,
                                 boolean isMutilSeries) {
        JData data = new JData();
        data.setType(SeriesEnum.pie);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);
        data.setXAxisType(AxisType.category);
        JData.JAxisPosition xp = new JData.JAxisPosition();
        JData.JAxisPosition yp = new JData.JAxisPosition();
        data.setXJAxisPosition(xp);
        data.setYJAxisPosition(yp);

        Option option = new Option();
        JConverter.convert(data, option);

        int size = option.series().size();
        if (size > 1) {
            int width = 100 / size + 1;
            int i = 0;
            for (Series e : option.series()) {
                ((Pie) e).radius(new Object[]{i, i + width});
                i = i + width + 5;
            }
        }

        // 设置标题，提示框等
        option.title().show(true);
        Tooltip tooltip = new Tooltip().option(option);
        option.tooltip(tooltip);
        option.legend().show(true).right(RightEnum.center)
                .type(LegendEnum.scroll).top(TopEnum.top)
                .orient(Orient.horizontal);

        LinkedHashSet dataSet = new LinkedHashSet();
        for (JData.Entry e : entryList) {
            LegendData d = new LegendData();
            d.setName(e.getX());
            dataSet.add(d);
        }
        option.legend().setData(dataSet);

        option.grid().forEach(e -> e.bottom(BottomEx.create("5%")).containLabel(true));

        tooltip.trigger(Trigger.item);
        tooltip.formatter("{a} <br/>{b} : {c} ({d}%)");
        return Response.ok().data(JSON.toJSON(option).toString()).build();
    }

    public boolean checkIsExist(DataModelFavorite dataModelFavorite) {
        List<DataModelFavorite> list = dataModelFavoriteService.findListBy("favoriteName", dataModelFavorite.getFavoriteName());
        if (list.size() < 1) {
            return false;
        }

        if (StringUtils.isBlank(dataModelFavorite.getId()) && list.size() > 0) {
            return false;
        }

        if (!list.get(0).getId().equals(dataModelFavorite.getId())) {
            return true;
        }
        return false;
    }

    public boolean hasModelPermission(String unitId, String userId, DataModelFavorite dataModelFavorite) {
        String modelId = dataModelFavorite.getModelId();
        DataModel dataModel = dataModelService.findOne(modelId);
        Integer orderType = dataModel.getOrderType();
        if (orderType == OrderType.SELF.getOrderType()) {
            return false;
        }

        if (orderType == OrderType.UNIT_OPEN.getOrderType() && !unitId.equals(dataModel.getUnitId())) {
            return false;
        }

        if (orderType == OrderType.UNIT_ORDER.getOrderType()) {
            List<Subscribe> list = subscribeService.findListBy(new String[]{"businessId", "unitId"}, new String[]{modelId, unitId});
            if (list.size() < 1) {
                return false;
            }
        }

        if (orderType == OrderType.USER_AUTHORIZATION.getOrderType()) {
            List<Subscribe> list = subscribeService.findListBy(new String[]{"businessId", "userId"}, new String[]{modelId, userId});
            if (list.size() < 1) {
                return false;
            }
        }
        return true;
    }

    private class ModelSearchParam {
        private LoginInfo loginInfo;
        private Map<String, String[]> filterData;
        private DataModel dataModel;
        private List<DataModelParam> indexParamList;
        private List<Json> indexList;
        private List<Json> resultFieldList;
        private List<Json> rowDimensionList;
        private List<Json> columnDimensionList;
        private Set<String> selectParam;
        private String sql;
        private String chartType;

        public ModelSearchParam(boolean isResport, String modelDatasetId, LoginInfo loginInfo, Map<String, String[]> filterData, DataModel dataModel, String[] indexParamIds, String[] dimensionRowParamIds, String... dimensionColumnParamIds) {
            this.loginInfo = loginInfo;
            this.filterData = filterData;
            this.dataModel = dataModel;

            // 如果是图表，则取行和列的最后一个维度
            if (!isResport) {
                if (dimensionRowParamIds.length > 1) {
                    dimensionRowParamIds = Arrays.copyOfRange(dimensionRowParamIds,
                            dimensionRowParamIds.length - 1,
                            dimensionRowParamIds.length);
                }
                if (dimensionColumnParamIds.length > 1 && indexParamIds.length == 1) {
                    dimensionColumnParamIds = Arrays.copyOfRange(
                            dimensionColumnParamIds,
                            dimensionColumnParamIds.length - 1,
                            dimensionColumnParamIds.length);
                }
            }

            Set<String> rowSet = new TreeSet<>(
                    Arrays.asList(dimensionRowParamIds));
            Set<String> columnSet = new TreeSet<>(
                    Arrays.asList(dimensionColumnParamIds));
            // 添加了筛选的维度拼装进sql
            filterData.forEach((k, v) -> {
                if (v.length != 0 && !rowSet.contains(k)) {
                    // 如果k的值不在维度集合里面 加进去
                    rowSet.add(k);
                }
            });

            indexParamList = Lists.newArrayList();
            List<DataModelParam> dimensionRowParamList = Lists.newArrayList();
            List<DataModelParam> dimensionColumnParamList = Lists.newArrayList();

            this.constructParam(dataModel, indexParamIds, rowSet, columnSet, dimensionRowParamList, dimensionColumnParamList);

            // 必须选择
            indexList = new ArrayList<Json>();

            resultFieldList = new ArrayList<Json>();
            // 必须选择
            rowDimensionList = new ArrayList<Json>();
            // 可选项
            columnDimensionList = new ArrayList<Json>();

            indexParamList.forEach(e -> {
                String indexKey = StringUtils.isBlank(e.getUseField()) ? (e
                        .getUseTable() + "_" + e.getMeasures()) : (e.getUseField()
                        + "_" + e.getMeasures());
                Json fieldJson = new Json();
                fieldJson.put("key", indexKey);
                fieldJson.put("name", e.getName());
                indexList.add(fieldJson);
            });

            dimensionColumnParamList.forEach(e -> {
                Json rJson = new Json();
                rJson.put("field", e.getUseField());
                rJson.put("dataType", "string");
                rJson.put("paramId", e.getId());
                resultFieldList.add(rJson);

                Json json = new Json();
                json.put("key", e.getUseField());
                json.put("name", e.getName());
                json.put("paramId", e.getId());
                columnDimensionList.add(json);

                if (!filterData.containsKey(e.getId())) {
                    filterData.put(e.getId(), new String[]{});
                }
            });

            dimensionRowParamList.forEach(e -> {
                Json rJson = new Json();
                rJson.put("field", e.getUseField());
                rJson.put("dataType", "string");
                rJson.put("paramId", e.getId());
                resultFieldList.add(rJson);

                Json json = new Json();
                json.put("key", e.getUseField());
                json.put("name", e.getName());
                json.put("paramId", e.getId());
                json.put("orderJson", e.getOrderJson());
                rowDimensionList.add(json);

                if (!filterData.containsKey(e.getId())) {
                    filterData.put(e.getId(), new String[]{});
                }
            });

            selectParam = new HashSet<>(
                    Arrays.asList(dimensionRowParamIds));
            selectParam.addAll(Arrays.asList(dimensionColumnParamIds));

            // 表名称
            String tableName = indexParamList.get(0).getUseTable();

            List<DataModelParam> dimensionList = Lists.newArrayList();
            // 必须选择
            dimensionList.addAll(dimensionRowParamList);
            dimensionList.addAll(dimensionColumnParamList);

            // 组装sql
            sql = this.constructSql(indexParamList, dimensionList,
                    resultFieldList, tableName, filterData, selectParam, dataModel,
                    modelDatasetId, loginInfo);
        }

        private void constructParam(DataModel dataModel, String[] indexParamIds, Set<String> rowSet, Set<String> columnSet, List<DataModelParam> dimensionRowParamList, List<DataModelParam> dimensionColumnParamList) {
            if (dataModel.getSource() != null && dataModel.getSource() == 0) {
                List<PropertyTag> propertyTags = propertyTagService.findListBy("code", "model");
                PropertyTag propertyTag = propertyTags.get(0);
                List<MetadataTag> metadataTags = metadataTagService.findByTagId(propertyTag.getId());
                List<String> ids = metadataTags.stream().map(MetadataTag::getMdId).collect(Collectors.toList());
                List<Metadata> metadataList = metadataService.findByDbTypeAndIds(dataModel.getType(), ids);
                Map<String, String> metadataMap = metadataList.stream().collect(Collectors.toMap(Metadata::getId, Metadata::getTableName));
                List<MetadataTableColumn> metadataTableColumnList = metadataTableColumnService.findAllModelUsed();
                Map<String, String> metadataColumnMap = metadataTableColumnList.stream().collect(Collectors.toMap(MetadataTableColumn::getId, MetadataTableColumn::getColumnName));

                for (String id : indexParamIds) {
                    DataModelParam param = getDataModelParam(metadataMap, metadataColumnMap, id);
                    indexParamList.add(param);
                }

                for (String id : rowSet) {
                    if (!columnSet.contains(id)) {
                        DataModelParam param = getDataModelParam(metadataMap, metadataColumnMap, id);
                        dimensionRowParamList.add(param);
                    }
                }

                for (String id : columnSet) {
                    DataModelParam param = getDataModelParam(metadataMap, metadataColumnMap, id);
                    dimensionColumnParamList.add(param);
                }

            } else {
                for (String id : indexParamIds) {
                    indexParamList.add(dataModelParamService.findOne(id));
                }

                for (String id : rowSet) {
                    if (!columnSet.contains(id)) {
                        dimensionRowParamList.add(dataModelParamService.findOne(id));
                    }
                }

                for (String id : columnSet) {
                    dimensionColumnParamList.add(dataModelParamService.findOne(id));
                }
            }
        }

        private DataModelParam getDataModelParam(Map<String, String> metadataMap, Map<String, String> metadataColumnMap, String id) {
            DataModelParam param = dataModelParamService.findOne(id);
            param.setUseTable(metadataMap.getOrDefault(param.getUseTable(), StringUtils.EMPTY));
            param.setUseField(metadataColumnMap.getOrDefault(param.getUseField(), StringUtils.EMPTY));
            param.setOrderField(metadataColumnMap.getOrDefault(param.getOrderField(), StringUtils.EMPTY));
            return param;
        }


        private String constructSql(List<DataModelParam> indexParamList,
                                    List<DataModelParam> dimensionList, List<Json> resultFieldList,
                                    String tableName, Map<String, String[]> filterData,
                                    Set<String> selectParam, DataModel dataModel,
                                    String modelDatasetId, LoginInfo loginInfo) {
            String unitId = loginInfo.getUnitId();

            StringBuilder sql = new StringBuilder("select ");
            List<DataModelParam> joinTables = Lists.newArrayList();
            // sql查询结果集字段
            dimensionList.forEach(e -> {
                if (e.getUseTable().equals(tableName)) {
                    sql.append(e.getUseTable()).append(".").append(e.getUseField())
                            .append(",");
                } else {
                    // 时间维度关联
                    if ("date".equals(e.getCode())) {
                        sql.append(" ").append(dataModel.getDateDimTable())
                                .append(".").append(e.getUseField()).append(",");
                        e.setUseTable(dataModel.getDateDimTable());
                        e.setFactForeignId(dataModel.getDateColumn());
                    } else {
                        // 普通的关联
                        sql.append(" ").append(e.getUseTable()).append(".")
                                .append(e.getUseField()).append(",");
                    }
                    joinTables.add(e);
                }
            });
            // 查询指标函数
            int i = 1;
            for (DataModelParam e : indexParamList) {
                if ("count".equals(e.getMeasures())) {
                    sql.append(e.getMeasures()).append("(1) as ");
                } else {
                    sql.append(e.getMeasures()).append("(").append(e.getUseField())
                            .append(") as ");
                }
                String indexKey = StringUtils.isBlank(e.getUseField()) ? (e
                        .getUseTable() + "_" + e.getMeasures()) : (e.getUseField()
                        + "_" + e.getMeasures());
                // mysql不支持 -
                sql.append(indexKey);
                if (i++ < indexParamList.size()) {
                    sql.append(",");
                }
            }

            // 连接其他表查询
            sql.append(" from ").append(tableName);
            Set<String> hasJoin = Sets.newHashSet();
            joinTables.forEach(e -> {
                // 已经关联的表不再关联
                if (hasJoin.contains(e.getUseTable())) {
                    return;
                }
                sql.append(" inner join ").append(e.getUseTable())
                        .append(" on ").append(tableName).append(".")
                        .append(e.getFactForeignId()).append(" = ")
                        .append(e.getUseTable()).append(".")
                        .append(e.getDimForeignId());
                hasJoin.add(e.getUseTable());
            });
            Integer datasetType = dataModel.getDatasetType();
            int conditions = 0;
            // 加入筛选条件
            if (datasetType == null || datasetType == 1) {
                if (joinTables.size() == 0) {
                    int m = 0;
                    for (DataModelParam dataModelParam : dimensionList) {
                        if (filterData.containsKey(dataModelParam.getId())) {
                            String[] fieldValue = filterData.get(dataModelParam
                                    .getId());
                            if (fieldValue.length == 0) {
                                continue;
                            }
                            if (m++ == 0) {
                                sql.append(" where ");
                            } else
                                sql.append(" and ");

                            sql.append(tableName).append(".")
                                    .append(dataModelParam.getUseField())
                                    .append(" in");
                            sql.append(arraysToInSql(fieldValue));
                            conditions++;
                        }
                    }
                } else {
                    for (DataModelParam e : dimensionList) {
                        if (filterData.containsKey(e.getId())) {
                            String[] fieldValue = filterData.get(e.getId());
                            if (fieldValue.length == 0) {
                                continue;
                            }
                            sql.append(" and ").append(e.getUseTable()).append(".")
                                    .append(e.getUseField()).append(" in");
                            sql.append(arraysToInSql(fieldValue));
                            conditions++;
                        }
                    }
                }
            } else {
                if (datasetType == 2) {
                    if (joinTables.size() > 0) {
                        sql.append(" and ").append(tableName).append(".")
                                .append("unit_id").append(" = ")
                                .append("'" + unitId + "'");
                    } else {
                        sql.append(" where ").append(tableName).append(".")
                                .append("unit_id").append(" = ")
                                .append("'" + unitId + "'");
                    }
                    conditions++;
                } else if (datasetType == 3) {
                    Unit unit = unitRemoteService.findOneObjectById(unitId);
                    if (joinTables.size() > 0) {
                        sql.append(" and ").append(tableName).append(".")
                                .append("union_id").append(" like ")
                                .append("'" + unit.getUnionCode() + "%'");
                    } else {
                        sql.append(" where ").append(tableName).append(".")
                                .append("union_id").append(" like ")
                                .append("'" + unit.getUnionCode() + "%'");
                    }
                    conditions++;
                }

                for (DataModelParam dataModelParam : dimensionList) {
                    if (filterData.containsKey(dataModelParam.getId())) {
                        String[] fieldValue = filterData
                                .get(dataModelParam.getId());
                        if (fieldValue.length == 0) {
                            continue;
                        }
                        sql.append(" and ").append(dataModelParam.getUseTable())
                                .append(".").append(dataModelParam.getUseField())
                                .append(" in");
                        sql.append(arraysToInSql(fieldValue));
                        conditions++;
                    }
                }
            }

            // 加入用户数据集筛选
            if (dataModel.getUserDatasetSwitch() != null
                    && dataModel.getUserDatasetSwitch() == 1
                    && StringUtils.isNotBlank(modelDatasetId)) {
                ModelDataset dataset = modelDatasetService.findOne(modelDatasetId);
                if (conditions++ == 0) {
                    sql.append(" where ");
                } else
                    sql.append(" and ");
                sql.append(dataset.getDsConditionSql());
            }

            // 分组
            sql.append(" group by ");

            for (DataModelParam e : dimensionList) {
                if (!selectParam.contains(e.getId())) {
                    continue;
                }
                if (StringUtils.isNotBlank(e.getOrderField())
                        && e.getUseTable().equals(tableName)
                        && !e.getUseField().equals(e.getOrderField())) {
                    sql.append(e.getOrderField()).append(",");
                }
            }

            int j = 1;
            Set<String> isAdd = Sets.newHashSet();
            for (Json e : resultFieldList) {
                String field = e.getString("field");
                if (selectParam.contains(e.getString("paramId"))
                        && !isAdd.contains(field)) {
                    sql.append(field);
                    if (j++ < selectParam.size()) {
                        sql.append(",");
                    }
                    isAdd.add(field);
                }
            }

            // 排序
            sql.append(" order by ");
            int k = 1;
            for (DataModelParam e : dimensionList) {
                if (StringUtils.isNotBlank(e.getOrderField())
                        && e.getUseTable().equals(tableName)) {
                    sql.append(e.getOrderField());
                } else {
                    sql.append(e.getUseField());
                }
                if (k++ < dimensionList.size()) {
                    sql.append(",");
                }
            }
            return sql.toString();
        }

        private String arraysToInSql(String[] str) {
            StringBuilder sb = new StringBuilder("(");
            for (String s : str) {
                sb.append("'").append(s).append("'").append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append(")");
            return sb.toString();
        }
    }
}
