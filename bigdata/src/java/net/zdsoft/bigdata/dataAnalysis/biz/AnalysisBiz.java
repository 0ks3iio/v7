package net.zdsoft.bigdata.dataAnalysis.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.bigdata.base.entity.PropertyTag;
import net.zdsoft.bigdata.base.service.PropertyTagService;
import net.zdsoft.bigdata.data.entity.DataModel;
import net.zdsoft.bigdata.data.entity.DataModelFavorite;
import net.zdsoft.bigdata.data.entity.DataModelParam;
import net.zdsoft.bigdata.data.entity.ModelDataset;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.dataAnalysis.entity.QueryParam;
import net.zdsoft.bigdata.dataAnalysis.vo.ReportTableVO;
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
import net.zdsoft.echarts.coords.AxisPointer;
import net.zdsoft.echarts.coords.AxisPointerLabel;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.element.inner.LegendData;
import net.zdsoft.echarts.enu.*;
import net.zdsoft.echarts.series.Bar;
import net.zdsoft.echarts.series.Line;
import net.zdsoft.echarts.series.Pie;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.BarData;
import net.zdsoft.echarts.style.ItemStyle;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wangdongdong on 2019/6/19 11:37.
 */
@Service
public class AnalysisBiz {

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

    // 自定义模型
    private static final Integer CUSTOM_MODEL = 1;
    // 系统内置模型
    private static final Integer SYS_MODEL = 0;

    /**
     * 组装行表头
     *
     * @param queryParam
     * @return
     */
    public String builderRowHeader(QueryParam queryParam) throws BigDataBusinessException {

        DataModel model = dataModelService.findOne(queryParam.getModelId());
        // 返回结果字段
        List<Json> resultFieldList = Lists.newArrayList();
        List<DataModelParam> modelParams = Lists.newArrayList();
        for (String rowId : queryParam.getDimensionRowParam()) {
            DataModelParam param = dataModelParamService.findOne(rowId);
            // TODO
            if (model.getSource() == 0) {
                param.setUseTable(metadataService.findOne(param.getUseTable()).getTableName());
                param.setUseField(metadataTableColumnService.findOne(param.getUseField()).getColumnName());
                param.setOrderField(param.getOrderField() != null ? metadataTableColumnService.findOne(param.getOrderField()).getColumnName() : null);
            }
            modelParams.add(param);
            Json json = new Json();
            json.put("field", param.getUseField());
            json.put("dataType", "string");
            json.put("paramId", param.getId());
            json.put("orderJson", param.getOrderJson());

            // 判断能否下钻
            List<DataModelParam> params = dataModelParamService.findChildDimensionById(param.getId());
            if (params.size() > 0) {
                json.put("hasChild", "true");
            }
            resultFieldList.add(json);
        }
        // 组装查询sql
        String sql = constructHeaderSql(modelParams, queryParam);
        List<Json> result = mysqlClientService.getDataListFromMysqlThrowException(model.getDbId(), null, sql, null, resultFieldList);
        queryParam.setRowSize(result.size());
        // 组装行表头
        return builderRowHeaderHtml(resultFieldList, result);
    }

    /**
     * 组装列表头
     *
     * @param queryParam
     * @return
     */
    public String builderColumnHeader(QueryParam queryParam) throws BigDataBusinessException {
        String[] measureParam = queryParam.getMeasureParam();

        DataModel model = dataModelService.findOne(queryParam.getModelId());
        List<DataModelParam> modelParams = Lists.newArrayList();
        List<Json> resultFieldList = Lists.newArrayList();

        for (String rowId : queryParam.getDimensionColumnParam()) {
            DataModelParam param = dataModelParamService.findOne(rowId);
            if (model.getSource() == 0) {
                param.setUseTable(metadataService.findOne(param.getUseTable()).getTableName());
                param.setUseField(metadataTableColumnService.findOne(param.getUseField()).getColumnName());
                param.setOrderField(param.getOrderField() != null ? metadataTableColumnService.findOne(param.getOrderField()).getColumnName() : null);
            }

            modelParams.add(param);
            Json json = new Json();
            json.put("field", param.getUseField());
            json.put("dataType", "string");
            json.put("paramId", param.getId());
            json.put("orderJson", param.getOrderJson());

            // 判断能否下钻
            List<DataModelParam> params = dataModelParamService.findChildDimensionById(param.getId());
            if (params.size() > 0) {
                json.put("hasChild", "true");
            }
            resultFieldList.add(json);
        }

        String sql = constructHeaderSql(modelParams, queryParam);

        List<Json> result = mysqlClientService.getDataListFromMysqlThrowException(model.getDbId(), null, sql, null, resultFieldList);
        queryParam.setColumnSize(result.size());
        return constructColumnHeaderHtml(measureParam, resultFieldList, result);

    }

    /**
     * 组装行表头
     *
     * @param resultFieldList 返回字段
     * @param queryResult     返回结果
     * @return
     */
    private String builderRowHeaderHtml(List<Json> resultFieldList, List<Json> queryResult) {
        StringBuilder header = new StringBuilder();
        for (Json tr : queryResult) {
            header.append("<tr>");
            for (Json td : resultFieldList) {
                if ("true".equals(td.get("hasChild"))) {
                    header.append("<th class='hasChild row' data-id='").append(td.getString("paramId")).append("'>");
                } else {
                    header.append("<th>");
                }
                header.append(tr.size() > 0 ? Optional.ofNullable(tr.getString(td.getString("field"))).orElse("未知"):"未知");
                header.append("</th>");
            }
            header.append("</tr>");
        }
        return header.toString();
    }

    /**
     * 组装sql通用
     *
     * @param modelParams
     * @param queryParam
     * @return
     */
    private String constructHeaderSql(List<DataModelParam> modelParams, QueryParam queryParam) {
        DataModel dataModel = dataModelService.findOne(queryParam.getModelId());
        StringBuilder sql = new StringBuilder("select ");
        Map<String, DataModelParam> paramMap = Maps.newHashMap();

        String tableName = "";
        for (DataModelParam param : modelParams) {
            sql.append(param.getUseField()).append(",");
            tableName = param.getUseTable();
            paramMap.put(param.getId(), param);
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" from ").append(tableName);

        // 筛选条件
        String filterDataMap = queryParam.getFilterDataMap();
        if (filterDataMap != null) {
            Map<String, String[]> filterData = JSON.parseObject(filterDataMap,
                    new TypeReference<HashMap<String, String[]>>() {
                    });

            int m = 0;
            for (Map.Entry<String, String[]> entry : filterData.entrySet()) {
                DataModelParam dataModelParam = dataModelParamService.findOne(entry.getKey());
                if (dataModel.getSource() == 0) {
                    dataModelParam.setUseTable(metadataService.findOne(dataModelParam.getUseTable()).getTableName());
                    dataModelParam.setUseField(metadataTableColumnService.findOne(dataModelParam.getUseField()).getColumnName());
                    dataModelParam.setOrderField(dataModelParam.getOrderField() != null ? metadataTableColumnService.findOne(dataModelParam.getOrderField()).getColumnName() : null);
                }
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
            }
        }

        sql.append(" group by ");
        for (DataModelParam param : modelParams) {
            sql.append(param.getUseField()).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);

        String orderDataMap = queryParam.getOrderDataMap();
        if (orderDataMap != null) {
            Map<String, String> orderData = JSON.parseObject(orderDataMap,
                    new TypeReference<HashMap<String, String>>() {
                    });
            int s = 0;
            for (Map.Entry<String, String> entry : orderData.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                if (paramMap.containsKey(k)) {
                    if (s++ == 0) {
                        sql.append(" order by");
                    }
                    DataModelParam field = paramMap.get(k);
                    sql.append(" ").append(field.getUseField()).append(" ").append(v).append(",");
                }
            }
            if (s > 0) {
                sql.deleteCharAt(sql.length() - 1);
            }

        }
        return sql.toString();
    }

    /**
     * 列头html
     *
     * @param measureParam    列头包括指标
     * @param resultFieldList
     * @param queryResult
     * @return
     */
    private String constructColumnHeaderHtml(String[] measureParam, List<Json> resultFieldList, List<Json> queryResult) {
        StringBuilder header = new StringBuilder();
        int measureCount = measureParam.length != 0 ? measureParam.length : 1;
        int totalData = 0;
        for (Json td : resultFieldList) {
            header.append("<tr>");
            for (Json tr : queryResult) {
                for (int m = 0; m < measureCount; m++) {
                    if ("true".equals(td.get("hasChild"))) {
                        header.append("<th class='hasChild column' data-id='").append(td.getString("paramId")).append("'>");
                    } else {
                        header.append("<th>");
                    }
                    header.append(tr.size() > 0 ? Optional.ofNullable(tr.getString(td.getString("field"))).orElse("未知"):"未知");
                    header.append("</th>");
                    totalData++;
                }
            }
            header.append("</tr>");
        }

        // 计算总列数
        int totalColumn = totalData / resultFieldList.size();
        if (measureParam != null && measureParam.length > 0) {
            header.append("<tr>");
            int i = 0;
            while (i < totalColumn) {
                for (String measureId : measureParam) {
                    DataModelParam measure = dataModelParamService.findOne(measureId);
                    header.append("<th class='measure' data-id='").append(measureId).append("'>");
                    header.append(measure.getName());
                    header.append("</th>");
                    i++;
                }
            }
            header.append("</tr>");
        }
        return header.toString();
    }

    /**
     * 查询报表（不包括行头和列头）
     *
     * @param queryParam
     * @return
     */
    public ReportTableVO queryReport(QueryParam queryParam) throws BigDataBusinessException {

        if (queryParam.getMeasureParam().length < 1) {
            ReportTableVO reportTableVO = new ReportTableVO();
            if (queryParam.getDimensionRowParam().length < 1) {
                reportTableVO.setRowHeader("<tr><th>合计</th></tr>");
                queryParam.setRowSize(1);
            } else {
                reportTableVO.setRowHeader(builderRowHeader(queryParam));
            }

            if (queryParam.getDimensionColumnParam().length < 1) {
                reportTableVO.setColumnHeader("<tr><th>合计</th></tr>");
                queryParam.setColumnSize(1);
            } else {
                reportTableVO.setColumnHeader(builderColumnHeader(queryParam));
            }

            StringBuilder table = new StringBuilder();
            for (int row = 0; row < queryParam.getRowSize(); row++) {
                table.append("<tr>");
                for (int col = 0; col < queryParam.getColumnSize(); col++) {
                    table.append("<td>-</td>");
                }
                table.append("</tr>");
            }
            reportTableVO.setTable(table.toString());
            return reportTableVO;
        }

        DataModel dataModel = dataModelService.findOne(queryParam.getModelId());
        // 添加了筛选的维度拼装进sql

        List<DataModelParam> indexParamList = Lists.newArrayList();
        List<DataModelParam> dimensionRowParamList = Lists.newArrayList();
        List<DataModelParam> dimensionColumnParamList = Lists.newArrayList();

        this.constructParam(dataModel, queryParam.getMeasureParam(), queryParam.getDimensionRowParam(), queryParam.getDimensionColumnParam(), dimensionRowParamList, dimensionColumnParamList, indexParamList);

        Map<String, String[]> filterData = JSON.parseObject(queryParam.getFilterDataMap(),
                new TypeReference<HashMap<String, String[]>>() {
                });

        List<Json> dataList = queryResultFromDB(queryParam, dataModel, indexParamList, dimensionRowParamList, dimensionColumnParamList, filterData);

        // 行维度和列维度都存在时处理
        if (dimensionRowParamList.size() > 0 && dimensionColumnParamList.size() > 0) {
            return getAllReportTableVO(queryParam, indexParamList, dimensionRowParamList, dimensionColumnParamList, dataList);
        }

        // 只有行维度
        if (dimensionRowParamList.size() > 0) {
            return getRowReportTableVO(queryParam, indexParamList, dimensionRowParamList, dataList);
        }

        // 只有列维度
        return getColumnReportTableVO(queryParam, indexParamList, dimensionColumnParamList, dataList);
    }

    private ReportTableVO getColumnReportTableVO(QueryParam queryParam, List<DataModelParam> indexParamList, List<DataModelParam> dimensionColumnParamList, List<Json> dataList) throws BigDataBusinessException {
        StringBuilder table = new StringBuilder();
        ReportTableVO reportTableVO = new ReportTableVO();
        Set<String> columnNames = Sets.newLinkedHashSet();
        Map<String, Json> resultMap = Maps.newHashMap();

        DataModel model = dataModelService.findOne(queryParam.getModelId());
        // 返回结果字段
        List<Json> columnFieldList = Lists.newArrayList();
        List<DataModelParam> modelParams = dimensionColumnParamList;
        for (DataModelParam column : dimensionColumnParamList) {
            Json json = new Json();
            json.put("field", column.getUseField());
            json.put("dataType", "string");
            json.put("paramId", column.getId());
            json.put("orderJson", column.getOrderJson());

            // 判断能否下钻
            List<DataModelParam> params = dataModelParamService.findChildDimensionById(column.getId());
            if (params.size() > 0) {
                json.put("hasChild", "true");
            }
            columnFieldList.add(json);
        }

        // 组装查询sql
        String column_sql = constructHeaderSql(modelParams, queryParam);
        List<Json> columnResult = mysqlClientService.getDataListFromMysqlThrowException(model.getDbId(), null, column_sql, null, columnFieldList);

        StringBuilder columnHeader = new StringBuilder();
        int measureCount = indexParamList.size() != 0 ? indexParamList.size() : 1;
        int totalData = 0;

        for (Json tr : columnResult) {
            StringBuilder columName = new StringBuilder();
            for (Json td : columnFieldList) {

                String rName = tr.getString(td.getString("field"));
                columName.append(rName).append("_");
            }
            columName.deleteCharAt(columName.length() - 1);
            columnNames.add(columName.toString());
        }

        for (Json td : columnFieldList) {
            columnHeader.append("<tr>");
            for (Json tr : columnResult) {
                for (int m = 0; m < measureCount; m++) {
                    if ("true".equals(td.get("hasChild"))) {
                        columnHeader.append("<th class='hasChild column' data-id='").append(td.getString("paramId")).append("'>");
                    } else {
                        columnHeader.append("<th>");
                    }
                    columnHeader.append(tr.size() > 0 ? Optional.ofNullable(tr.getString(td.getString("field"))).orElse("未知"):"未知");
                    columnHeader.append("</th>");
                    totalData++;
                }
            }
            columnHeader.append("</tr>");
        }

        // 计算总列数
        int totalColumn = totalData / columnFieldList.size();
        if (indexParamList != null && indexParamList.size() > 0) {
            columnHeader.append("<tr>");
            int i = 0;
            while (i < totalColumn) {
                for (DataModelParam index : indexParamList) {
                    columnHeader.append("<th class='measure' data-id='").append(index.getId()).append("'>");
                    columnHeader.append(index.getName());
                    columnHeader.append("</th>");
                    i++;
                }
            }
            columnHeader.append("</tr>");
        }

        for (Json json : dataList) {
            StringBuilder key = new StringBuilder();
            // 一万测试单位
            for (DataModelParam col : dimensionColumnParamList) {
                String colName = json.getString(col.getUseField());
                key.append(colName).append("_");
            }
            key.deleteCharAt(key.length() - 1);
            resultMap.put(key.toString(), json);
        }

        Map<String, String[]> warningData = getWarningMap(queryParam);

        table.append("<tr>");
        for (String col : columnNames) {
            for (DataModelParam measure : indexParamList) {
                String indexKey = getMeasureSqlColumnName(measure);
                Json json = resultMap.get(col);
                String value = json != null ? json.getString(indexKey) : "-";
                if (warningData != null && warningData.containsKey(measure.getId())) {
                    table.append(getWarningTd(warningData, measure.getId(), value));
                } else {
                    table.append("<td>");
                    table.append("-".equals(value) ? "-" : value);
                    table.append("</td>");
                }
            }
        }
        table.append("</tr>");


        reportTableVO.setTable(table.toString());
        reportTableVO.setRowHeader("<tr><th>合计</th></tr>");
        reportTableVO.setColumnHeader(columnHeader.toString());
        return reportTableVO;
    }

    private Map<String, String[]> getWarningMap(QueryParam queryParam) {
        if (StringUtils.isNotBlank(queryParam.getWarningDataMap())) {
            return JSON.parseObject(queryParam.getWarningDataMap(),
                    new TypeReference<HashMap<String, String[]>>() {
                    });
        }
        return null;
    }

    private ReportTableVO getRowReportTableVO(QueryParam queryParam, List<DataModelParam> indexParamList, List<DataModelParam> dimensionRowParamList, List<Json> dataList) throws BigDataBusinessException {
        ReportTableVO reportTableVO = new ReportTableVO();
        Set<String> rowNames = Sets.newLinkedHashSet();
        Map<String, Json> resultMap = Maps.newHashMap();
        StringBuilder table = new StringBuilder();

        DataModel model = dataModelService.findOne(queryParam.getModelId());
        // 返回结果字段
        List<Json> resultFieldList = Lists.newArrayList();
        List<DataModelParam> modelParams = dimensionRowParamList;
        for (DataModelParam row : dimensionRowParamList) {
            Json json = new Json();
            json.put("field", row.getUseField());
            json.put("dataType", "string");
            json.put("paramId", row.getId());
            json.put("orderJson", row.getOrderJson());

            // 判断能否下钻
            List<DataModelParam> params = dataModelParamService.findChildDimensionById(row.getId());
            if (params.size() > 0) {
                json.put("hasChild", "true");
            }
            resultFieldList.add(json);
        }
        // 组装查询sql
        String sql = constructHeaderSql(modelParams, queryParam);
        List<Json> rowResult = mysqlClientService.getDataListFromMysqlThrowException(model.getDbId(), null, sql, null, resultFieldList);

        StringBuilder rowHeader = new StringBuilder();
        for (Json tr : rowResult) {
            StringBuilder rowName = new StringBuilder();
            rowHeader.append("<tr>");
            for (Json td : resultFieldList) {
                if ("true".equals(td.get("hasChild"))) {
                    rowHeader.append("<th class='hasChild row' data-id='").append(td.getString("paramId")).append("'>");
                } else {
                    rowHeader.append("<th>");
                }
                rowHeader.append(tr.size() > 0 ? Optional.ofNullable(tr.getString(td.getString("field"))).orElse("未知"):"未知");
                rowHeader.append("</th>");

                String rName = tr.getString(td.getString("field"));
                rowName.append(rName).append("_");
            }
            rowHeader.append("</tr>");

            rowName.deleteCharAt(rowName.length() - 1);
            rowNames.add(rowName.toString());
        }

        for (Json json : dataList) {
            StringBuilder key = new StringBuilder();

            StringBuilder rowName = new StringBuilder();
            for (DataModelParam row : dimensionRowParamList) {
                String rName = json.getString(row.getUseField());
                key.append(rName).append("_");
                rowName.append(rName).append("_");
            }
            rowName.deleteCharAt(rowName.length() - 1);
            rowNames.add(rowName.toString());

            key.deleteCharAt(key.length() - 1);
            resultMap.put(key.toString(), json);
        }

        Map<String, String[]> warningData = getWarningMap(queryParam);

        for (String row : rowNames) {
            table.append("<tr>");
            for (DataModelParam measure : indexParamList) {
                Json json = resultMap.get(row);
                String indexKey = getMeasureSqlColumnName(measure);
                String value = json != null ? json.getString(indexKey) : "-";

                if (warningData != null && warningData.containsKey(measure.getId())) {
                    table.append(getWarningTd(warningData, measure.getId(), value));
                } else {
                    table.append("<td>");
                    table.append("-".equals(value) ? "-" : value);
                    table.append("</td>");
                }
            }
            table.append("</tr>");
        }

        reportTableVO.setTable(table.toString());
        if (queryParam.isContainHeader()) {
            reportTableVO.setRowHeader(rowHeader.toString());
            StringBuilder columnHeader = new StringBuilder();
            columnHeader.append("<tr>");
            for (DataModelParam index : indexParamList) {
                columnHeader.append("<th class='measure' data-id='").append(index.getId()).append("'>");
                columnHeader.append(index.getName()).append("</th>");
            }
            columnHeader.append("</tr>");
            reportTableVO.setColumnHeader(columnHeader.toString());
        }
        return reportTableVO;
    }

    private ReportTableVO getAllReportTableVO(QueryParam queryParam, List<DataModelParam> indexParamList, List<DataModelParam> dimensionRowParamList, List<DataModelParam> dimensionColumnParamList, List<Json> dataList) throws BigDataBusinessException {
        Set<String> columnNames = Sets.newLinkedHashSet();
        Set<String> rowNames = Sets.newLinkedHashSet();
        Map<String, Json> resultMap = Maps.newHashMap();
        ReportTableVO reportTableVO = new ReportTableVO();

        DataModel model = dataModelService.findOne(queryParam.getModelId());
        // 返回结果字段
        List<Json> resultFieldList = Lists.newArrayList();
        List<DataModelParam> modelParams = dimensionRowParamList;
        for (DataModelParam row : dimensionRowParamList) {
            Json json = new Json();
            json.put("field", row.getUseField());
            json.put("dataType", "string");
            json.put("paramId", row.getId());
            json.put("orderJson", row.getOrderJson());

            // 判断能否下钻
            List<DataModelParam> params = dataModelParamService.findChildDimensionById(row.getId());
            if (params.size() > 0) {
                json.put("hasChild", "true");
            }
            resultFieldList.add(json);
        }
        // 组装查询sql
        String sql = constructHeaderSql(modelParams, queryParam);
        List<Json> rowResult = mysqlClientService.getDataListFromMysqlThrowException(model.getDbId(), null, sql, null, resultFieldList);

        StringBuilder rowHeader = new StringBuilder();
        for (Json tr : rowResult) {
            StringBuilder rowName = new StringBuilder();
            rowHeader.append("<tr>");
            for (Json td : resultFieldList) {
                if ("true".equals(td.get("hasChild"))) {
                    rowHeader.append("<th class='hasChild row' data-id='").append(td.getString("paramId")).append("'>");
                } else {
                    rowHeader.append("<th>");
                }
                rowHeader.append(tr.size() > 0 ? Optional.ofNullable(tr.getString(td.getString("field"))).orElse("未知"):"未知");
                rowHeader.append("</th>");

                String rName = tr.size() > 0 ? Optional.ofNullable(tr.getString(td.getString("field"))).orElse("未知"):"未知";
                rowName.append(rName).append("_");
            }
            rowHeader.append("</tr>");

            rowName.deleteCharAt(rowName.length() - 1);
            rowNames.add(rowName.toString());
        }


        // 返回结果字段
        List<Json> columnFieldList = Lists.newArrayList();
        modelParams = dimensionColumnParamList;
        for (DataModelParam column : dimensionColumnParamList) {
            Json json = new Json();
            json.put("field", column.getUseField());
            json.put("dataType", "string");
            json.put("paramId", column.getId());
            json.put("orderJson", column.getOrderJson());

            // 判断能否下钻
            List<DataModelParam> params = dataModelParamService.findChildDimensionById(column.getId());
            if (params.size() > 0) {
                json.put("hasChild", "true");
            }
            columnFieldList.add(json);
        }

        // 如果是行下钻 则不加上过滤(移除)
        if ("row".equals(queryParam.getChildPosition())) {
            queryParam.setFilterDataMap(queryParam.getRowFilterDataMap());
        }
        // 组装查询sql
        String column_sql = constructHeaderSql(modelParams, queryParam);
        List<Json> columnResult = mysqlClientService.getDataListFromMysqlThrowException(model.getDbId(), null, column_sql, null, columnFieldList);

        StringBuilder columnHeader = new StringBuilder();
        int measureCount = indexParamList.size() != 0 ? indexParamList.size() : 1;
        int totalData = 0;

        for (Json tr : columnResult) {
            StringBuilder columName = new StringBuilder();
            for (Json td : columnFieldList) {

                String rName = tr.size() > 0 ? Optional.ofNullable(tr.getString(td.getString("field"))).orElse("未知"):"未知";
                columName.append(rName).append("_");
            }
            columName.deleteCharAt(columName.length() - 1);
            columnNames.add(columName.toString());
        }

        for (Json td : columnFieldList) {
            columnHeader.append("<tr>");
            for (Json tr : columnResult) {
                for (int m = 0; m < measureCount; m++) {
                    if ("true".equals(td.get("hasChild"))) {
                        columnHeader.append("<th class='hasChild column' data-id='").append(td.getString("paramId")).append("'>");
                    } else {
                        columnHeader.append("<th>");
                    }
                    columnHeader.append(tr.size() > 0 ? Optional.ofNullable(tr.getString(td.getString("field"))).orElse("未知"):"未知");
                    columnHeader.append("</th>");
                    totalData++;
                }
            }
            columnHeader.append("</tr>");
        }

        // 计算总列数
        int totalColumn = totalData / columnFieldList.size();
        if (indexParamList != null && indexParamList.size() > 0) {
            columnHeader.append("<tr>");
            int i = 0;
            while (i < totalColumn) {
                for (DataModelParam index : indexParamList) {
                    columnHeader.append("<th class='measure' data-id='").append(index.getId()).append("'>");
                    columnHeader.append(index.getName());
                    columnHeader.append("</th>");
                    i++;
                }
            }
            columnHeader.append("</tr>");
        }


        // 获取行
        for (Json json : dataList) {
            StringBuilder key = new StringBuilder();
            for (DataModelParam row : dimensionRowParamList) {

                String rName = json.size() > 0 ? Optional.ofNullable(json.getString(row.getUseField())).orElse("未知"):"未知";
                key.append(rName).append("_");
            }

            // 一万测试单位
            for (DataModelParam col : dimensionColumnParamList) {
                String colName = json.size() > 0 ? Optional.ofNullable(json.getString(col.getUseField())).orElse("未知"):"未知";
                key.append(colName).append("_");
            }
            key.deleteCharAt(key.length() - 1);
            resultMap.put(key.toString(), json);
        }

        Map<String, String[]> warningData = this.getWarningMap(queryParam);

        StringBuilder table = new StringBuilder();
        for (String row : rowNames) {
            table.append("<tr>");
            for (String col : columnNames) {
                for (DataModelParam measure : indexParamList) {

                    String indexKey = getMeasureSqlColumnName(measure);
                    Json json = resultMap.get(row + "_" + col);
                    String value = json != null ? json.getString(indexKey) : "-";

                    if (warningData != null && warningData.containsKey(measure.getId())) {
                        table.append(getWarningTd(warningData, measure.getId(), value));
                    } else {
                        table.append("<td>");
                        table.append("-".equals(value) ? "-" : value);
                        table.append("</td>");
                    }
                }
            }
            table.append("</tr>");
        }

        reportTableVO.setTable(table.toString());
        if (queryParam.isContainHeader()) {
            reportTableVO.setRowHeader(rowHeader.toString());
            reportTableVO.setColumnHeader(columnHeader.toString());
        }
        return reportTableVO;
    }

    private String getWarningTd(Map<String, String[]> warningData, String measureId, String originValue) {
        StringBuilder td = new StringBuilder();
        String[] data = warningData.get(measureId);
        String symbol1 = data[0];
        String symbol2 = data[1];
        String num1 = data[2];
        String num2 = data[3];

        if ("-".equals(originValue)) {
            if (StringUtils.isNotBlank(num2)) {
                td.append("<td class='green_warning'>");
                td.append("<div><span>-</span></div>");
                td.append("</td>");
                return td.toString();
            }
            td.append("<td>");
            td.append("-");
            td.append("</td>");
            return td.toString();
        }

        BigDecimal v1 = BigDecimal.valueOf(Double.valueOf(originValue));
        if (StringUtils.isBlank(num1) && StringUtils.isBlank(num2)) {
            td.append("<td>");
            td.append(originValue);
            td.append("</td>");
            return td.toString();
        }

        if (StringUtils.isBlank(num1) && StringUtils.isNotBlank(num2)) {
            BigDecimal v2 = BigDecimal.valueOf(Double.valueOf(num2));
            if (">=".equals(symbol2)) {
                if (v1.compareTo(v2) >= 0) {
                    td.append("<td class='yellow_warning'>");
                    td.append(originValue);
                    td.append("</td>");
                } else {
                    td.append("<td class='green_warning'>");
                    td.append(originValue);
                    td.append("</td>");
                }
            } else {
                if (v1.compareTo(v2) > 0) {
                    td.append("<td class='yellow_warning'>");
                    td.append("<div><span>").append(originValue).append("</span></div>");
                    td.append("</td>");
                } else {
                    td.append("<td class='green_warning'>");
                    td.append("<div><span>").append(originValue).append("</span></div>");
                    td.append("</td>");
                }
            }
            return td.toString();
        }

        if (StringUtils.isBlank(num2) && StringUtils.isNotBlank(num1)) {

            BigDecimal v2 = BigDecimal.valueOf(Double.valueOf(num1));
            if (">=".equals(symbol1)) {
                if (v1.compareTo(v2) >= 0) {
                    td.append("<td class='red_warning'>");
                    td.append("<div><span>").append(originValue).append("</span></div>");
                    td.append("</td>");
                } else {
                    td.append("<td class='yellow_warning'>");
                    td.append("<div><span>").append(originValue).append("</span></div>");
                    td.append("</td>");
                }
            } else {
                if (v1.compareTo(v2) > 0) {
                    td.append("<td class='red_warning'>");
                    td.append("<div><span>").append(originValue).append("</span></div>");
                    td.append("</td>");
                } else {
                    td.append("<td class='yellow_warning'>");
                    td.append("<div><span>").append(originValue).append("</span></div>");
                    td.append("</td>");
                }
            }

            return td.toString();
        }


        BigDecimal v2 = BigDecimal.valueOf(Double.valueOf(num1));
        if (">=".equals(symbol1)) {
            if (v1.compareTo(v2) >= 0) {
                td.append("<td class='red_warning'>");
                td.append("<div><span>").append(originValue).append("</span></div>");
                td.append("</td>");
            } else {

                BigDecimal v3 = BigDecimal.valueOf(Double.valueOf(num2));
                if (">=".equals(symbol2)) {
                    if (v1.compareTo(v3) >= 0) {
                        td.append("<td class='yellow_warning'>");
                        td.append("<div><span>").append(originValue).append("</span></div>");
                        td.append("</td>");
                    } else {
                        td.append("<td class='green_warning'>");
                        td.append("<div><span>").append(originValue).append("</span></div>");
                        td.append("</td>");
                    }
                } else {
                    if (v1.compareTo(v3) > 0) {
                        td.append("<td class='red_warning'>");
                        td.append("<div><span>").append(originValue).append("</span></div>");
                        td.append("</td>");
                    } else {
                        td.append("<td class='green_warning'>");
                        td.append("<div><span>").append(originValue).append("</span></div>");
                        td.append("</td>");
                    }
                }

            }
        } else {
            if (v1.compareTo(v2) > 0) {
                td.append("<td class='red_warning'>");
                td.append("<div><span>").append(originValue).append("</span></div>");
                td.append("</td>");
            } else {

                BigDecimal v3 = BigDecimal.valueOf(Double.valueOf(num2));
                if (">=".equals(symbol2)) {
                    if (v1.compareTo(v3) >= 0) {
                        td.append("<td class='yellow_warning'>");
                        td.append("<div><span>").append(originValue).append("</span></div>");
                        td.append("</td>");
                    } else {
                        td.append("<td class='green_warning'>");
                        td.append("<div><span>").append(originValue).append("</span></div>");
                        td.append("</td>");
                    }
                } else {
                    if (v1.compareTo(v3) > 0) {
                        td.append("<td class='red_warning'>");
                        td.append("<div><span>").append(originValue).append("</span></div>");
                        td.append("</td>");
                    } else {
                        td.append("<td class='green_warning'>");
                        td.append("<div><span>").append(originValue).append("</span></div>");
                        td.append("</td>");
                    }
                }

            }
        }
        return td.toString();
    }

    private void constructParam(DataModel dataModel, String[] indexParamIds, String[] rowParam, String[] columnParam, List<DataModelParam> dimensionRowParamList, List<DataModelParam> dimensionColumnParamList, List<DataModelParam> indexParamList) {
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

            for (String id : rowParam) {
                DataModelParam param = getDataModelParam(metadataMap, metadataColumnMap, id);
                dimensionRowParamList.add(param);
            }

            for (String id : columnParam) {
                DataModelParam param = getDataModelParam(metadataMap, metadataColumnMap, id);
                dimensionColumnParamList.add(param);
            }

        } else {
            for (String id : indexParamIds) {
                indexParamList.add(dataModelParamService.findOne(id));
            }

            for (String id : rowParam) {
                dimensionRowParamList.add(dataModelParamService.findOne(id));
            }

            for (String id : columnParam) {
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
                                String modelDatasetId, LoginInfo loginInfo, QueryParam queryParam) {
//        String unitId = loginInfo.getUnitId();

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
            String indexKey = getMeasureSqlColumnName(e);
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
                for (Map.Entry<String, String[]> entry : filterData.entrySet()) {
                    DataModelParam dataModelParam = dataModelParamService.findOne(entry.getKey());
                    if (dataModel.getSource() == 0) {
                        dataModelParam.setUseTable(metadataService.findOne(dataModelParam.getUseTable()).getTableName());
                        dataModelParam.setUseField(metadataTableColumnService.findOne(dataModelParam.getUseField()).getColumnName());
                        dataModelParam.setOrderField(dataModelParam.getOrderField() != null ? metadataTableColumnService.findOne(dataModelParam.getOrderField()).getColumnName() : null);
                    }
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
            } else {
                for (Map.Entry<String, String[]> entry : filterData.entrySet()) {
                    DataModelParam dataModelParam = dataModelParamService.findOne(entry.getKey());
                    if (dataModel.getSource() == 0) {
                        dataModelParam.setUseTable(metadataService.findOne(dataModelParam.getUseTable()).getTableName());
                        dataModelParam.setUseField(metadataTableColumnService.findOne(dataModelParam.getUseField()).getColumnName());
                        dataModelParam.setOrderField(dataModelParam.getOrderField() != null ? metadataTableColumnService.findOne(dataModelParam.getOrderField()).getColumnName() : null);
                    }
                    String[] fieldValue = filterData.get(dataModelParam
                            .getId());
                    if (fieldValue.length == 0) {
                        continue;
                    }

                    sql.append(tableName).append(".")
                            .append(dataModelParam.getUseField())
                            .append(" in");
                    sql.append(arraysToInSql(fieldValue));
                    conditions++;
                }
            }
        } else {
//            if (datasetType == 2) {
//                if (joinTables.size() > 0) {
//                    sql.append(" and ").append(tableName).append(".")
//                            .append("unit_id").append(" = ")
//                            .append("'" + unitId + "'");
//                } else {
//                    sql.append(" where ").append(tableName).append(".")
//                            .append("unit_id").append(" = ")
//                            .append("'" + unitId + "'");
//                }
//                conditions++;
//            } else if (datasetType == 3) {
//                Unit unit = unitRemoteService.findOneObjectById(unitId);
//                if (joinTables.size() > 0) {
//                    sql.append(" and ").append(tableName).append(".")
//                            .append("union_id").append(" like ")
//                            .append("'" + unit.getUnionCode() + "%'");
//                } else {
//                    sql.append(" where ").append(tableName).append(".")
//                            .append("union_id").append(" like ")
//                            .append("'" + unit.getUnionCode() + "%'");
//                }
//                conditions++;
//            }
//
//            for (DataModelParam dataModelParam : dimensionList) {
//                if (filterData.containsKey(dataModelParam.getId())) {
//                    String[] fieldValue = filterData
//                            .get(dataModelParam.getId());
//                    if (fieldValue.length == 0) {
//                        continue;
//                    }
//                    sql.append(" and ").append(dataModelParam.getUseTable())
//                            .append(".").append(dataModelParam.getUseField())
//                            .append(" in");
//                    sql.append(arraysToInSql(fieldValue));
//                    conditions++;
//                }
//            }
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

        String orderDataMap = queryParam.getOrderDataMap();
        Map<String, String> orderData = orderDataMap != null ? JSON.parseObject(orderDataMap,
                new TypeReference<HashMap<String, String>>() {
                }) : Maps.newHashMap();

        // 排序
        sql.append(" order by ");
        int k = 1;
        for (DataModelParam e : dimensionList) {
            if (StringUtils.isNotBlank(e.getOrderField())
                    && e.getUseTable().equals(tableName)) {
                sql.append(e.getOrderField());
                if (orderData.get(e.getId()) != null) {
                    sql.append(" ").append(orderData.get(e.getId()));
                }
            } else {
                sql.append(e.getUseField());
                if (orderData.get(e.getId()) != null) {
                    sql.append(" ").append(orderData.get(e.getId()));
                }
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

    public String queryReportTotal(QueryParam queryParam) throws BigDataBusinessException {
        String[] measureParam = queryParam.getMeasureParam();
        DataModel model = dataModelService.findOne(queryParam.getModelId());
        // 真实指标
        List<DataModelParam> measureParams = getRealDataModelParams(model, measureParam);
        // 查询结果
        List<Json> dataList = queryTotalResultFromDB(measureParams, model, queryParam.getFilterDataMap());

        Map<String, String[]> warningData = this.getWarningMap(queryParam);

        StringBuilder table = new StringBuilder();
        table.append("<tr>");
        for (Json json : dataList) {
            for (DataModelParam measure : measureParams) {
                String indexKey = getMeasureSqlColumnName(measure);
                String value = json != null ? json.getString(indexKey) : "-";

                if (warningData != null && warningData.containsKey(measure.getId())) {
                    table.append(getWarningTd(warningData, measure.getId(), value));
                } else {
                    table.append("<td>");
                    table.append(value);
                    table.append("</td>");
                }
            }
        }
        table.append("</tr>");

        return table.toString();
    }

    private List<Json> queryTotalResultFromDB(List<DataModelParam> measureParams, DataModel model, String filterDataMap) throws BigDataBusinessException {

        StringBuilder sql = new StringBuilder();

        List<Json> resultFieldList = Lists.newArrayList();

        String tableName = "";
        sql.append("select ");
        // 查询指标函数
        for (DataModelParam measure : measureParams) {
            tableName = measure.getUseTable();
            if ("count".equals(measure.getMeasures())) {
                sql.append(measure.getMeasures()).append("(1) as ");
            } else {
                sql.append(measure.getMeasures()).append("(").append(measure.getUseField())
                        .append(") as ");
            }
            String indexKey = getMeasureSqlColumnName(measure);
            // mysql不支持 -
            sql.append(indexKey).append(",");

            Json rJson = new Json();
            rJson.put("field", measure.getUseField());
            rJson.put("dataType", "string");
            rJson.put("paramId", measure.getId());
            resultFieldList.add(rJson);
        }
        sql.deleteCharAt(sql.length() - 1);

        sql.append(" from ").append(tableName);

        if (StringUtils.isNotBlank(filterDataMap)) {
            Map<String, String[]> filterData = JSON.parseObject(filterDataMap,
                    new TypeReference<HashMap<String, String[]>>() {
                    });

            int m = 0;
            // 加入篩選
            for (Map.Entry<String, String[]> entry : filterData.entrySet()) {
                DataModelParam dataModelParam = dataModelParamService.findOne(entry.getKey());
                if (model.getSource() == 0) {
                    dataModelParam.setUseTable(metadataService.findOne(dataModelParam.getUseTable()).getTableName());
                    dataModelParam.setUseField(metadataTableColumnService.findOne(dataModelParam.getUseField()).getColumnName());
                    dataModelParam.setOrderField(dataModelParam.getOrderField() != null ? metadataTableColumnService.findOne(dataModelParam.getOrderField()).getColumnName() : null);
                }
                String[] fieldValue = filterData.get(dataModelParam
                        .getId());
                if (fieldValue.length == 0) {
                    continue;
                }

                if (m++ == 0) {
                    sql.append(" where");
                } else {
                    sql.append(" and");
                }

                sql.append(" ").append(tableName).append(".")
                        .append(dataModelParam.getUseField())
                        .append(" in");
                sql.append(arraysToInSql(fieldValue));
            }
        }

        return mysqlClientService.getDataListFromMysqlThrowException(model.getDbId(), null, sql.toString(), null,
                resultFieldList);
    }

    private String getMeasureSqlColumnName(DataModelParam measure) {
        return StringUtils.isBlank(measure.getUseField()) ? (measure
                .getUseTable() + "_" + measure.getMeasures()) : (measure.getUseField()
                + "_" + measure.getMeasures());
    }

    public List<String> getFilterData(String modelId, String dimensionId,
                                      String modelDatasetId) throws BigDataBusinessException {
        DataModel dataModel = dataModelService.findOne(modelId);
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

        String dataSourceId = (dataModel.getSource() != null && dataModel.getSource() == 1) ? dataModel.getDbId() : null;

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
            result = mysqlClientService.getDataListFromMysqlThrowException(dataModel.getDbId(),
                    null, sql.toString(),
                    Lists.newArrayList(), Lists.newArrayList(field));
        }

        List<String> filterData = Lists.newArrayList();
        result.forEach(e -> {
            if (e.getString(dataModelParam.getUseField()) != null ) {
                filterData.add(e.getString(dataModelParam.getUseField()));
            }
        });
        return filterData;
    }

    private List<Json> queryResultFromDB(QueryParam queryParam, DataModel dataModel, List<DataModelParam> indexParamList, List<DataModelParam> dimensionRowParamList, List<DataModelParam> dimensionColumnParamList, Map<String, String[]> filterData) throws BigDataBusinessException {

        // 必须选择
        List<Json> indexList = new ArrayList<Json>();

        List<Json> resultFieldList = new ArrayList<Json>();
        // 必须选择
        List<Json> rowDimensionList = new ArrayList<Json>();
        // 可选项
        List<Json> columnDimensionList = new ArrayList<Json>();

        indexParamList.forEach(e -> {
            Json fieldJson = new Json();
            fieldJson.put("key", getMeasureSqlColumnName(e));
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
            json.put("field", e.getUseField());
            json.put("paramId", e.getId());
            columnDimensionList.add(json);
        });

        dimensionRowParamList.forEach(e -> {
            Json rJson = new Json();
            rJson.put("field", e.getUseField());
            rJson.put("dataType", "string");
            rJson.put("paramId", e.getId());
            resultFieldList.add(rJson);

            Json json = new Json();
            json.put("key", e.getUseField());
            json.put("field", e.getUseField());
            json.put("name", e.getName());
            json.put("paramId", e.getId());
            json.put("orderJson", e.getOrderJson());
            rowDimensionList.add(json);
        });

        Set<String> selectParam = new HashSet<>(Arrays.asList(queryParam.getDimensionRowParam()));
        selectParam.addAll(Arrays.asList(queryParam.getDimensionColumnParam()));

        // 表名称
        String tableName = indexParamList.get(0).getUseTable();

        List<DataModelParam> dimensionList = Lists.newArrayList();
        // 必须选择
        dimensionList.addAll(dimensionRowParamList);
        dimensionList.addAll(dimensionColumnParamList);

        // 组装sql
        String sql = this.constructSql(indexParamList, dimensionList,
                resultFieldList, tableName, filterData, selectParam, dataModel,
                null, null, queryParam);

        return mysqlClientService.getDataListFromMysqlThrowException(dataModel.getDbId(), null, sql, null,
                resultFieldList);
    }

    public Object queryChart(String chartDimensionId, String chartLegendId, String[] measureParam, String filterDataMap, String modelId, String chartType, String chartOrderDataMap) throws BigDataBusinessException {

        if (StringUtils.isBlank(chartDimensionId) && StringUtils.isBlank(chartLegendId)) {

            DataModel model = dataModelService.findOne(modelId);
            // 真实指标
            List<DataModelParam> measureParams = getRealDataModelParams(model, measureParam);
            // 查询结果
            List<Json> dataList = queryTotalResultFromDB(measureParams, model, filterDataMap);
            List<JData.Entry> entryList = Lists.newArrayList();
            // y轴维度
            DataModelParam yParam = measureParams.get(0);

            for (Json data : dataList) {
                JData.Entry entry = new JData.Entry();
                entry.setX("合计");
                String indexKey = getMeasureSqlColumnName(yParam);
                entry.setY(data.getString(indexKey));
                entryList.add(entry);
            }

            if ("line".equals(chartType)) {
                return getLineChart(entryList, false);
            }

            if ("cookie".equals(chartType)) {
                return getPieChart(entryList, false);
            }

            return getBarChart(entryList, false);
        }

        QueryParam queryParam = new QueryParam()
                .setMeasureParam(measureParam)
                .setDimensionRowParam(StringUtils.isNotBlank(chartDimensionId) ? new String[]{chartDimensionId} : new String[]{})
                .setDimensionColumnParam(StringUtils.isNotBlank(chartLegendId) ? new String[]{chartLegendId} : new String[]{})
                .setModelId(modelId)
                .setOrderDataMap(chartOrderDataMap)
                .setFilterDataMap(filterDataMap);

        DataModel dataModel = dataModelService.findOne(modelId);
        // 添加了筛选的维度拼装进sql

        List<DataModelParam> indexParamList = Lists.newArrayList();
        List<DataModelParam> dimensionRowParamList = Lists.newArrayList();
        List<DataModelParam> dimensionColumnParamList = Lists.newArrayList();

        this.constructParam(dataModel, queryParam.getMeasureParam(), queryParam.getDimensionRowParam(), queryParam.getDimensionColumnParam(), dimensionRowParamList, dimensionColumnParamList, indexParamList);

        Map<String, String[]> filterData = JSON.parseObject(queryParam.getFilterDataMap(),
                new TypeReference<HashMap<String, String[]>>() {
                });

        List<Json> dataList = queryResultFromDB(queryParam, dataModel, indexParamList, dimensionRowParamList, dimensionColumnParamList, filterData);

        if (measureParam.length == 1) {
            List<JData.Entry> entryList = Lists.newArrayList();
            // x轴维度
            DataModelParam xParam = dimensionRowParamList.size() > 0 ? dimensionRowParamList.get(0) : null;
            // y轴维度
            DataModelParam yParam = indexParamList.get(0);

            DataModelParam legend = dimensionColumnParamList.size() > 0 ? dimensionColumnParamList.get(0) : null;

            for (Json data : dataList) {
                JData.Entry entry = new JData.Entry();
                entry.setX(xParam == null ? "合计" : StringUtils.defaultIfBlank(
                        data.getString(xParam.getUseField()), "未知"));

                String indexKey = getMeasureSqlColumnName(yParam);

                entry.setY(data.getString(indexKey));

                entry.setName(legend == null ? xParam.getName() : data.getString(legend.getUseField()));
                entryList.add(entry);
            }

            if ("line".equals(chartType)) {
                return getLineChart(entryList, legend == null ? false : true);
            }

            if ("cookie".equals(chartType)) {
                return getPieChart(entryList, legend == null ? false : true);
            }

            return getBarChart(entryList, legend == null ? false : true);
        }


        return null;
    }


    private String getBarChart(List<JData.Entry> entryList,
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
        AxisPointerLabel<AxisPointer<Tooltip>> label = new AxisPointerLabel<>();
        label.precision(0);
        tooltip.axisPointer().type(AxisPointerType.cross).setLabel(label);
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

        option.series().forEach(e -> ((Bar) e).setBarMaxWidth("40px"));

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

        return JSON.toJSONString(option);
    }

    private String getLineChart(List<JData.Entry> entryList,
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
        AxisPointerLabel<AxisPointer<Tooltip>> label = new AxisPointerLabel<>();
        label.precision(0);
        tooltip.axisPointer().type(AxisPointerType.cross).setLabel(label);
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

        option.series().forEach(e -> {
            Line line = (Line) e;
            line.symbol(SymbolEnum.circle);
            line.symbolSize("10");
        });

        option.grid().forEach(
                e -> {
                    e.left(LeftEx.create(14)).right(RightEx.create(20))
                            .top(TopEx.create(40)).bottom(BottomEx.create("5%")).containLabel(true);
                });
        return JSON.toJSONString(option);
    }

    private String getPieChart(List<JData.Entry> entryList,
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
        return JSON.toJSONString(option);
    }

    public List<DataModelParam> getChildDimension(String dimensionId) {
        return dataModelParamService.findChildDimensionById(dimensionId);
    }

    public ReportTableVO queryChildReport(QueryParam queryParam) throws BigDataBusinessException {

        // 查询表头 重新设置参数
        if ("row".equals(queryParam.getChildPosition())) {
            queryParam.setDimensionRowParam(queryParam.getChildDimensionIdArray());
            queryParam.setRowFilterDataMap(queryParam.getFilterDataMap());
        } else {
            queryParam.setDimensionColumnParam(queryParam.getChildDimensionIdArray());
        }

        // 添加下钻维度过滤
        Map<String, String[]> filterData = JSON.parseObject(queryParam.getFilterDataMap(),
                new TypeReference<HashMap<String, String[]>>() {
                });

        Map<String, String> childFilterData = JSON.parseObject(queryParam.getChildDimensionFilterMap(),
                new TypeReference<HashMap<String, String>>() {
                });

        childFilterData.forEach((k, v) -> {
            if (filterData.containsKey(k)) {
                String[] data = filterData.get(k);
                List<String> datas = Lists.newArrayList(data);
                datas.add(v);
                filterData.put(k, datas.toArray(new String[datas.size()]));
            } else {
                List<String> datas = Lists.newArrayList(v);
                filterData.put(k, datas.toArray(new String[datas.size()]));
            }
        });

        queryParam.setFilterDataMap(Json.toJSONString(filterData));

        if (queryParam.getMeasureParam().length < 1) {
            ReportTableVO reportTableVO = new ReportTableVO();
            if (queryParam.getDimensionRowParam().length < 1) {
                reportTableVO.setRowHeader("<tr><th>合计</th></tr>");
                queryParam.setRowSize(1);
            } else {
                reportTableVO.setRowHeader(builderRowHeader(queryParam));
            }

            if (queryParam.getDimensionColumnParam().length < 1) {
                reportTableVO.setColumnHeader("<tr><th>合计</th></tr>");
                queryParam.setColumnSize(1);
            } else {
                reportTableVO.setColumnHeader(builderColumnHeader(queryParam));
            }

            StringBuilder table = new StringBuilder();
            for (int row = 0; row < queryParam.getRowSize(); row++) {
                table.append("<tr>");
                for (int col = 0; col < queryParam.getColumnSize(); col++) {
                    table.append("<td>-</td>");
                }
                table.append("</tr>");
            }
            reportTableVO.setTable(table.toString());
            return reportTableVO;
        }

        DataModel dataModel = dataModelService.findOne(queryParam.getModelId());
        // 添加了筛选的维度拼装进sql

        List<DataModelParam> indexParamList = Lists.newArrayList();
        List<DataModelParam> dimensionRowParamList = Lists.newArrayList();
        List<DataModelParam> dimensionColumnParamList = Lists.newArrayList();

        this.constructParam(dataModel, queryParam.getMeasureParam(), queryParam.getDimensionRowParam(), queryParam.getDimensionColumnParam(), dimensionRowParamList, dimensionColumnParamList, indexParamList);

        List<Json> dataList = queryResultFromDB(queryParam, dataModel, indexParamList, dimensionRowParamList, dimensionColumnParamList, filterData);

        // select class_name, count(1) from bg_student_model where grade_name = '高一' group by class_name

        // 行维度和列维度都存在时处理
        if (dimensionRowParamList.size() > 0 && dimensionColumnParamList.size() > 0) {
            return getAllReportTableVO(queryParam, indexParamList, dimensionRowParamList, dimensionColumnParamList, dataList);
        }

        // 只有行维度
        if (dimensionRowParamList.size() > 0) {
            return getRowReportTableVO(queryParam, indexParamList, dimensionRowParamList, dataList);
        }

        // 只有列维度
        return getColumnReportTableVO(queryParam, indexParamList, dimensionColumnParamList, dataList);
    }

    private List<DataModelParam> getRealDataModelParams(DataModel model, String[] paramIds) {
        List<DataModelParam> dataModelParams = Lists.newArrayList();
        for (String measureId : paramIds) {
            DataModelParam param = dataModelParamService.findOne(measureId);
            if (model.getSource() == 0) {
                param.setUseTable(metadataService.findOne(param.getUseTable()).getTableName());
                if (param.getUseField() != null) {
                    param.setUseField(metadataTableColumnService.findOne(param.getUseField()).getColumnName());
                    param.setOrderField(metadataTableColumnService.findOne(param.getOrderField()).getColumnName());
                }
            }
            dataModelParams.add(param);
        }
        return dataModelParams;
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
}

