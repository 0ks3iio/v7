/**
 * FileName: JEchartQueryController.java
 * Author:   shenke
 * Date:     2018/6/27 下午12:56
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.code.ChartCategory;
import net.zdsoft.bigdata.data.code.ChartType;
import net.zdsoft.bigdata.data.echarts.OptionEx;
import net.zdsoft.bigdata.data.echarts.OptionExposeFactory;
import net.zdsoft.bigdata.data.echarts.WrapChart;
import net.zdsoft.bigdata.data.echarts.WrapConvert;
import net.zdsoft.bigdata.data.entity.Api;
import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.entity.ChartDetail;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.entity.NosqlDatabase;
import net.zdsoft.bigdata.data.manager.IResult;
import net.zdsoft.bigdata.data.manager.api.Invoker;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.data.service.ApiService;
import net.zdsoft.bigdata.data.service.ChartDetailService;
import net.zdsoft.bigdata.data.service.ChartService;
import net.zdsoft.bigdata.data.service.DatabaseService;
import net.zdsoft.bigdata.data.service.NosqlDatabaseService;
import net.zdsoft.bigdata.data.utils.JexlContextHolder;
import net.zdsoft.bigdata.data.utils.JexlUtils;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datasource.Adapter;
import net.zdsoft.bigdata.datasource.DataType;
import net.zdsoft.bigdata.datasource.DatasourceQueryChecker;
import net.zdsoft.bigdata.datasource.NoopAdapter;
import net.zdsoft.bigdata.datasource.QueryExtractor;
import net.zdsoft.bigdata.datasource.QueryResponse;
import net.zdsoft.bigdata.datasource.QueryStatement;
import net.zdsoft.bigdata.datasource.jdbc.JdbcDatabaseAdapter;
import net.zdsoft.bigdata.datasource.redis.RedisDatabaseAdapter;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.ServletContextResource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

/**
 * @author shenke
 * @since 2018/6/27 下午12:56
 */
@Controller
@RequestMapping(
        value = "bigdata/echarts"
)
public class JEchartQueryController extends BigdataBaseAction {

    private Logger logger = LoggerFactory.getLogger(JEchartQueryController.class);

    @Resource
    private ServletContext servletContext;
    @Resource
    private ChartService chartService;
    @Resource
    private ApiService apiService;
    @Resource
    private Invoker invoker;
    @Resource
    private ChartDetailService chartDetailService;
    @Autowired
    private RegionRemoteService regionRemoteService;

    @ResponseBody
    @RequestMapping(
            value = "view"
    )
    public Response execute(@RequestParam("chartId") String chartId,
                            @RequestParam(value = "cockpit", defaultValue = "false") boolean cockpit,
                            @RequestParam(value = "chartType", required = false) Integer chartType,
                            @RequestParam(value = "parent", required = false) String parent,
                            @RequestParam(value = "parentParent", required = false) String parentParent) {
        //获取数据及配置
        Chart chart = chartService.findOne(chartId);
        if (chart == null) {
            return Response.error().message("该图表找不到了").build();
        }
        String top = chart.getMap();
        chart.setMap(getMapName(chart.getMap(), parent, parentParent));
        OptionEx ex = null;
        List<ChartDetail> chartDetailList = chartDetailService.getByChartId(chartId);

        //当在驾驶舱展示的时候，需要替换Title的通配符
        chart.setName(JexlUtils.evaluate(chart.getName(), TitleContext.create(), false));

        if (chartDetailList.size() == 0) {
            //查询数据，可能是多组数据
            Result result = queryData(chart, parent, parentParent, top);
            WrapChart wrapChart = WrapChart.build(chart, result);
            if (chartType != null) {
                wrapChart.setChartType(chartType);
            }
            try {
                String optionExpose = chart.getParameters();
                Object oe = null;
                if (StringUtils.isNotBlank(optionExpose)) {
                    oe = OptionExposeFactory.getOptionExpose(optionExpose, ChartCategory.valueFrom(chart.getChartType()).get().getChartSeries(), chart.getChartType());
                } else {
                    oe = OptionExposeFactory.getOptionExpose(chart.getChartType(), ChartCategory.valueFrom(chart.getChartType()).get().getChartSeries());
                }
                ex = WrapConvert.convert(Collections.singletonList(wrapChart), wrapChart.getChartType(), cockpit, oe);
            } catch (Exception e) {
                logger.error("cockpit execute error", e);
                return Response.error().message(e.getMessage()).build();
            }
            ex.setChartType(wrapChart.getChartType());
            ex.mapName(chart.getMap());
            ex.setTop(top);
            ex.setParentParent(parentParent);
            ex.setParentMap(chart.getMap());
            ex.setDivName(chart.getName());
            ex.setAutoRefresh(chart.getUpdateInterval() != null && chart.getUpdateInterval() > 0);
            ex.setAutoRefreshInterval(chart.getUpdateInterval());
            ex.success(true);
        }
        if (!ex.isSuccess()) {
            return Response.error().message(ex.getMessage()).build();
        }
        return Response.ok().data(ex.toJSONString()).build();
    }

    private String getMapName(String map, String name, String parentParent) {
        if (name == null) {
            return map;
        }
        if ("00".equals(name)) {
            return "00";
        }
        String destination = RedisUtils.get(name + parentParent);
        if (destination != null) {
            return destination;
        }
        Region region = regionRemoteService.getRegionByCodeOrName(name);
        if (region == null) {
            try {
                destination = tryGetRegionFromGeoJson(parentParent, name);
            } catch (Exception e) {
                throw new IllegalArgumentException("不合法的code或者行政区划名称");
            }
        } else {
            destination = region.getRegionCode();
        }
        RedisUtils.set(name + parentParent, destination);
        return destination;
    }

    private String tryGetRegionFromGeoJson(String parentNameOrCode, String currentName) throws IOException {
        Region region = regionRemoteService.getRegionByCodeOrName(parentNameOrCode);
        if (region != null) {
            String parentCode = region.getRegionCode();
            org.springframework.core.io.Resource resource = new ServletContextResource(servletContext,
                    "/static/bigdata/js/echarts/map/json/province/" + parentCode + ".json");
            JSONObject geoJson = new JSONReader(new FileReader(resource.getFile())).readObject(JSONObject.class);
            String regionCode = "";
            String p = "";
            for (Object features : geoJson.getJSONArray("features")) {
                if (currentName.equals(p = ((JSONObject) features).getJSONObject("properties").getString("name"))) {
                    regionCode = ((JSONObject) features).getJSONObject("properties").getString("id");
                    break;
                }
            }
            if (regionCode.matches("[0-9]{4}00")) {
                return regionCode.substring(0, 4);
            } else if (regionCode.matches("[0-9]{2}0000")) {
                return regionCode.substring(0, 2);
            } else if (regionCode.matches("[0-9]{4}")) {
                return regionCode;
            }
        }
        throw new IllegalArgumentException("不合法的code或者行政区划名称");
    }

    @ResponseBody
    @RequestMapping(
            value = "view/editChart"
    )
    public Response execute(@RequestBody Chart chart) {
        OptionEx ex = null;
        String top = chart.getMap();
        String current = chart.getParent();
        //查询数据，可能是多组数据
        try {
            Result result = queryData(chart, current, chart.getParentParent(), top);
            Object oe = null;
            if (chart.getOptionExpose() != null && ((Map) chart.getOptionExpose()).size() > 0) {
                oe = OptionExposeFactory.getOptionExpose(JSONObject.toJSONString(chart.getOptionExpose()), ChartCategory.valueFrom(chart.getChartType()).get().getChartSeries(), chart.getChartType());
            } else {
                if (StringUtils.isNotBlank(chart.getId())) {
                    Chart c = chartService.findOne(chart.getId());
                    if (c != null && StringUtils.isNotBlank(c.getParameters())
                            && chart.getChartType() == c.getChartType()) {
                        oe = OptionExposeFactory.getOptionExpose(c.getParameters().trim(), ChartCategory.valueFrom(chart.getChartType()).get().getChartSeries(), chart.getChartType());
                    }
                }
                if (oe == null) {
                    oe = OptionExposeFactory.getOptionExpose(chart.getChartType(), ChartCategory.valueFrom(chart.getChartType()).get().getChartSeries());
                }
            }
            chart.setMap(getMapName(chart.getMap(), current, chart.getParentParent()));
            ex = WrapConvert.convert(Collections.singletonList(WrapChart.build(chart, result)), chart.getChartType(), false, oe);
        } catch (Exception e) {
            logger.error("edit chart execute error", e);
            return Response.error().message(e.getMessage()).build();
        }
        ex.setTop(top);
        ex.setParentParent(chart.getParentParent());
        ex.setParentMap(chart.getMap());
        ex.setChartType(chart.getChartType());
        ex.mapName(chart.getMap());
        ex.setDivName(chart.getName());
        ex.setAutoRefresh(chart.getUpdateInterval() != null && chart.getUpdateInterval() > 0);
        ex.setAutoRefreshInterval(chart.getUpdateInterval());
        if (!ex.isSuccess()) {
            return Response.error().message(ex.getMessage()).build();
        }
        return Response.ok().data(ex.toJSONString()).build();
    }

    @Resource
    private DatasourceQueryChecker datasourceQueryChecker;
    @Resource
    private DatabaseService databaseService;
    @Resource
    private NosqlDatabaseService nosqlDatabaseService;

    private Result queryData(Chart chart, String name, String parentParent, String top) {
        JexlContextHolder.setJexlContext(getLoginInfo());

        try {
            Result result = null;
            if (DataSourceType.DB.getValue() == chart.getDataSourceType()) {
                QueryExtractor extractor = null;
                Database database = databaseService.findOne(chart.getDatabaseId());
                //1 try to jdbc
                Adapter adapter;
                if (database != null) {
                    extractor = QueryExtractor.extractorResultSetForJSONList();
                    adapter = jdbcDatabaseAdapter(database);
                }
                //2 try no sql
                else {
                    NosqlDatabase nosqlDatabase = nosqlDatabaseService.findOne(chart.getDatabaseId());
                    if (nosqlDatabase != null) {
                        switch (nosqlDatabase.getType()) {
                            case "09":
                            case "07":
                                Database db = new Database();
                                db.setPort(nosqlDatabase.getPort());
                                db.setDomain(nosqlDatabase.getDomain());
                                db.setDbName(nosqlDatabase.getDbName());
                                db.setUsername(nosqlDatabase.getUserName());
                                db.setPassword(nosqlDatabase.getPassword());
                                db.setType(nosqlDatabase.getType());
                                adapter = jdbcDatabaseAdapter(db);
                                extractor = QueryExtractor.extractorResultSetForJSONList();
                                break;
                            case "12":
                                adapter = RedisDatabaseAdapter.RedisDatabaseKeyBuilder.builder()
                                        .dataType(DatabaseType.REDIS).withDomain(nosqlDatabase.getDomain())
                                        .withPassword(nosqlDatabase.getPassword()).withPort(nosqlDatabase.getPort())
                                        .build();
                                extractor = QueryExtractor.identity();
                                break;
                            default:
                                return new IResult(null, new RuntimeException("Not Support type"));
                        }
                    } else {
                        return new IResult(null, new RuntimeException("Database is null"));
                    }
                }
                QueryStatement<Adapter> queryStatement = new QueryStatement<>(adapter, chart.getDataSet());
                QueryResponse queryResponse = datasourceQueryChecker.query(queryStatement, extractor);
                return new IResult(queryResponse.getQueryValue(), queryResponse.getError());
            } else if (DataSourceType.API.getValue() == chart.getDataSourceType()) {
                Api api = apiService.findOne(chart.getApiId());
                NoopAdapter adapter = NoopAdapter.noopAdapter(DataType.api());
                QueryStatement<NoopAdapter> queryStatement = new QueryStatement<>(adapter, api.getUrl());
                QueryResponse<String> queryResponse = datasourceQueryChecker.query(queryStatement, QueryExtractor.identity());
                return new IResult(queryResponse.getQueryValue(), queryResponse.getError());
            } else if (DataSourceType.STATIC.getValue() == chart.getDataSourceType()) {
                result = new IResult(chart.getDataSet(), null);
            } else {
                return null;
            }
            if (result != null
                    && (chart.getChartType() == ChartType.MAP || chart.getChartType() == ChartType.MAP_LINE)) {
                Object val = result.getValue();
                if (val instanceof String) {
                    val = JSONObject.parseArray((String) val);
                }
                List<JSONObject> regions = new ArrayList<>();
                String regionCode = getMapName("", name, parentParent);

                String p = null;
                for (Object o : ((JSONArray) val)) {
                    if (name != null && !"00".equals(name)) {
                        if (StringUtils.equals(top, name)) {
                            if ((p = ((JSONObject) o).getString("parent")) == null) {
                                regions.add((JSONObject) o);
                            }
                        } else {
                            if (name.equals(p = ((JSONObject) o).getString("parent")) || regionCode.equals(p)
                                    || getMapName("", p, parentParent).equals(name)) {
                                regions.add((JSONObject) o);
                            }
                        }
                    } else {
                        if (StringUtils.isBlank(p = ((JSONObject) o).getString("parent"))) {
                            regions.add((JSONObject) o);
                        }
                    }
                }
                return new IResult(JSONObject.toJSONString(regions));
            }
            return result;
        } finally {
            JexlContextHolder.clearJexlContext();
        }
    }

    private Adapter jdbcDatabaseAdapter(Database database) {
        return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
                .characterEncoding(database.getCharacterEncoding())
                .dbName(database.getDbName()).username(database.getUsername())
                .domain(database.getDomain()).password(database.getPassword())
                .port(database.getPort()).type(DatabaseType.parse(database.getType())).build();
    }


    public static class TitleContext {

        static DateTimeFormatter _day = DateTimeFormatter.ofPattern("dd");
        static DateTimeFormatter _month = DateTimeFormatter.ofPattern("MM");
        static DateTimeFormatter _year = DateTimeFormatter.ofPattern("yyyy");

        public static TitleContext create() {
            TitleContext context = new TitleContext();
            LocalDate localDate = LocalDate.now();
            context.setYear(localDate.format(_year));
            context.setLastYear(localDate.minusYears(1).format(_year));
            context.setMonth(localDate.format(_month));
            context.setLastMonth(localDate.minusMonths(1).format(_month));
            context.setDay(localDate.format(_day));
            context.setYesterday(localDate.minusDays(1).format(_day));
            return context;
        }

        public static void main(String[] args) {
            TitleContext context = create();
            System.out.println(JexlUtils.evaluate("${day}", context));
            System.out.println(JexlUtils.evaluate("${year}", context));
            System.out.println(JexlUtils.evaluate("${lastYear}", context));
            System.out.println(JexlUtils.evaluate("${yesterday}", context));
            System.out.println(JexlUtils.evaluate("${month}", context));
            System.out.println(JexlUtils.evaluate("${lastMonth}", context));
        }

        private String year;
        private String lastYear;
        private String month;
        private String lastMonth;
        private String day;
        private String yesterday;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getLastYear() {
            return lastYear;
        }

        public void setLastYear(String lastYear) {
            this.lastYear = lastYear;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getLastMonth() {
            return lastMonth;
        }

        public void setLastMonth(String lastMonth) {
            this.lastMonth = lastMonth;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getYesterday() {
            return yesterday;
        }

        public void setYesterday(String yesterday) {
            this.yesterday = yesterday;
        }
    }

}
