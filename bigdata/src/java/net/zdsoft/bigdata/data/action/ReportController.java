package net.zdsoft.bigdata.data.action;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerSchemaStatVisitor;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import net.sf.jasperreports.engine.JRException;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.biz.ReportDataQueryBiz;
import net.zdsoft.bigdata.data.entity.*;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.manager.InvocationBuilder;
import net.zdsoft.bigdata.data.manager.api.Invoker;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.data.utils.JexlContextHolder;
import net.zdsoft.bigdata.data.vo.ReportVO;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static net.zdsoft.bigdata.data.ChartBusinessType.REPORT;

@Controller
@RequestMapping("/bigdata/report")
public class ReportController extends BigdataBaseAction {

    private Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Resource
    private Invoker invoker;
    @Resource
    private ReportTemplateService reportTemplateService;
    @Resource
    private ReportTermService reportTermService;
    @Resource
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private ChartService chartService;
    @Resource
    private TagRelationService tagRelationService;
    @Resource
    private TagService tagService;
    @Resource
    private DatabaseService databaseService;
    @Resource
    private ReportDataQueryBiz reportDataQueryBiz;
    @Resource
    private ApiService apiService;

    /**
     * 进入报表列表
     * @return
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public ModelAndView index() {

        //加载所有可以查询的标签
        LoginInfo loginInfo = getLoginInfo();
        Set<GraphConfigurationController.GraphIndexTag> graphIndexTags;
        //(当前单位新增的)
        List<Tag> tags = tagService.findTagsByUnitIdAndTagType(
                loginInfo.getUnitId(), REPORT.getBusinessType().shortValue());
        graphIndexTags = tags.parallelStream().map(tag -> GraphConfigurationController.GraphIndexTag.create(tag.getId(), tag.getTagName()))
                .collect(Collectors.toSet());
        //订阅列表自带的
        List<Chart> charts = chartService.getCurrentUserCharts(loginInfo.getUserId(), loginInfo.getUnitId(),
                null, null, null, false, true, false);
        String[] chartIdArray = charts.stream().map(Chart::getId).toArray(String[]::new);
        if (ArrayUtils.isNotEmpty(chartIdArray)) {
            List<TagRelation> tagRelations = tagRelationService.getByBusinessId(chartIdArray);
            graphIndexTags.addAll(tagRelations.parallelStream().map(tagRelation -> {
                return GraphConfigurationController.GraphIndexTag.create(tagRelation.getTagId(), tagRelation.getTagName());
            }).collect(Collectors.toSet()));
        }
        return new ModelAndView("/bigdata/reportQuery/reportQueryIndex.ftl")
                .addObject("tags", graphIndexTags);
    }

    /**
     * 查询
     * @param model
     * @param tags
     * @param canDelete
     * @return
     */
    @RequestMapping("/list")
    public String list(ModelMap model, @RequestParam(value = "tags", required = false) String tags,
                       @RequestParam(value = "chartName", required = false) String name,
                       @RequestParam(value = "canDelete", required = false) String canDelete) {
        LoginInfo login = getLoginInfo();
        String[] tagIdArray = null;
        if (StringUtils.isNotBlank(tags)) {
            tagIdArray = StringUtils.trim(tags).split(",");
        }
        Pagination pagination = createPagination();
        List<Chart> chartList = chartService.getCurrentUserCharts(login.getUserId(),
                login.getUnitId(), tagIdArray, name ,pagination, false, true, false);
        //获取每个图表标记的标签
        Map<String, List<String>> eachChartTagNames = new HashMap<>(chartList.size());
        tagRelationService.getByBusinessId(chartList.stream().map(Chart::getId).toArray(String[]::new))
                .forEach(tagRelation ->
                eachChartTagNames.computeIfAbsent(tagRelation.getBusinessId(), k -> new ArrayList<>())
                        .add(tagRelation.getTagName()));
        final String fileUrl = sysOptionRemoteService.getFileUrl(getRequest().getServerName());
        List<GraphQueryController.GraphIndexChart> graphIndexChartList = chartList.parallelStream().map(chart -> {
            GraphQueryController.GraphIndexChart c = new GraphQueryController.GraphIndexChart();
            ReportTemplate template = reportTemplateService.findOne(chart.getTemplateId());
            c.setId(chart.getId()).setName(chart.getName())
                    .setThumbnailPath(fileUrl + "/" + template.getThumbnailPath())
                    .setTagNameList(eachChartTagNames.get(chart.getId()));
            return c;
        }).collect(Collectors.toList());
        model.put("charts", graphIndexChartList);
        model.put("pagination", pagination);
        model.put("canDelete", StringUtils.isNotBlank(canDelete));
        model.put("isSearch", StringUtils.isNotBlank(name));
        model.put("isTagSearch", tagIdArray != null);
        return "/bigdata/reportQuery/reportQueryList.ftl";
    }

    /**
     * 查看报表
     * @param model
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/view")
    public String edit(ModelMap model, @RequestParam(value = "chartId", required = false) String id, HttpServletRequest request) {
        if (StringUtils.isNotBlank(id)) {
            // 查出报表
            Chart chart = chartService.findOne(id);
            if (chart == null) {
                model.put("errorMsg", "该报表已经被删除");
                return "/bigdata/v3/common/error.ftl";
            }
            // 查询模版
            ReportTemplate reportTemplate = reportTemplateService.findOne(chart.getTemplateId());
            // 查询条件
            List<ReportTerm> reportTerms = reportTermService.getReportTermByReportId(id);
            // 数据源id
            String datasourceId = StringUtils.isBlank(chart.getApiId()) ? chart.getDatabaseId() : chart.getApiId();
            // 处理json内 [] 字符
            reportTerms.forEach(e -> e.setDataSet(e.getDataSet() != null ? e.getDataSet().replaceAll("\\[", "@").replaceAll("\\]", "#").replaceAll("\r\n|\r|\n"," ") : StringUtils.EMPTY));
            model.addAttribute("reportTerms", reportDataQueryBiz.toJSONString(JSON.toJSON(reportTerms).toString()).replaceAll("\n", "\\n"));
            model.addAttribute("template", reportTemplate);
            model.addAttribute("chart", chart);
            model.addAttribute("datasourceId", datasourceId);
        } else {
            model.addAttribute("chart", new Chart());
            model.addAttribute("template", new ReportTemplate());
        }
        model.addAttribute("fileUrl", sysOptionRemoteService.getFileUrl(getRequest().getServerName()));
        return "/bigdata/reportQuery/reportQueryView.ftl";
    }

    /**
     * 查询条件
     * @param chart
     * @param termDatabaseId
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryCondition")
    public Object queryCondition(Chart chart, String termDatabaseId, String selectMap) {
        chart.setDatabaseId(termDatabaseId);
        // 验证sql格式是否正确
        Response response = this.validateSql(chart);
        if (!response.isSuccess()) {
            return response;
        }
        try {
            this.initParamMap(selectMap);
            Result result = null;
            if (chart.getDataSourceType() == DataSourceType.DB.getValue()) {
                result = invoker.invoke(
                        InvocationBuilder.getInstance().dataSourceId(chart.getDatabaseId())
                                .updateInterval(0)
                                .queryStatement(chart.getDataSet())
                                .type(DataSourceType.DB).build());
            } else {
                Api api = apiService.findOne(chart.getDatabaseId());
                result = invoker.invoke(
                        InvocationBuilder.getInstance().dataSourceId(chart.getDatabaseId())
                                .updateInterval(0)
                                .queryStatement(api.getUrl())
                                .type(DataSourceType.API).build());
                JSONObject jsonObject = JSON.parseObject(String.valueOf(result.getValue()));
                String data = jsonObject.getString("infolist");
                return result.hasError() ? Response.error().message(result.getException().getMessage()).build() : Response.ok().data(data).build();
            }

            return result.hasError() ? Response.error().message(result.getException().getMessage()).build() : Response.ok().data(result.getValue()).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        } finally {
            JexlContextHolder.clearJexlContext();
        }
    }

    /**
     * 导出文件 pdf excel
     * @param id
     * @param selectMap
     * @param type
     * @param response
     * @param request
     * @throws BigDataBusinessException
     */
    @ResponseBody
    @RequestMapping("/exportToFile")
    public void exportToFile(@RequestParam(value = "id") String id,
                             String selectMap, @RequestParam(value = "type", required = false) String type,
                             HttpServletResponse response, HttpServletRequest request) throws BigDataBusinessException {
        Chart chart = chartService.findOne(id);
        ReportVO reportVO = new ReportVO().setDataSet(chart.getDataSet())
                .setDataSourceId(StringUtils.isBlank(chart.getDatabaseId()) ? chart.getApiId() : chart.getDatabaseId())
                .setAutoUpdate(chart.getAutoUpdate())
                .setDataSourceType(chart.getDataSourceType())
                .setTemplateId(chart.getTemplateId())
                .setUpdateInterval(chart.getUpdateInterval()).setReportId(id);
        reportVO.setName(chart.getName());
        ReportTemplate template = reportTemplateService.findOne(chart.getTemplateId());
        //输出html
        try {
            if (StringUtils.isNotBlank(selectMap)) {
                String[] keyList = StringUtils.splitByWholeSeparatorPreserveAllTokens(selectMap, "$");
                for (String s : keyList) {
                    String[] key = StringUtils.splitByWholeSeparatorPreserveAllTokens(s, "~");
                    if (key.length == 2) {
                        reportVO.getParamMap().put(key[0], key[1]);
                    }
                }
            }
            String jasperPath = reportDataQueryBiz.getJasperPath(reportVO, template, request);
            if (StringUtils.isNotBlank(type) && type.equals("0")) {
                reportDataQueryBiz.exportToPdf(reportVO, jasperPath, request, response);
            } else
                reportDataQueryBiz.exportToExcel(reportVO, jasperPath, request, response);
        } catch (JRException e) {
            logger.error("数据结果集与模版不匹配，请输入正确的sql", e);
            throw new BigDataBusinessException("数据结果集与模版不匹配【" + e.getMessage() + "】，请输入正确的sql");
        } catch (IOException e) {
            logger.error("模版加载出错，请确认模版是否存在", e);
            throw new BigDataBusinessException("模版加载出错，请确认模版是否存在");
        } catch (Exception e) {
            logger.error("系统错误", e);
            throw new BigDataBusinessException("系统错误");
        }
    }

    /**
     * 验证sql格式是否正确
     * @param chart
     * @return
     */
    private Response validateSql(Chart chart) {
        if (chart.getDataSourceType() != DataSourceType.DB.getValue()) {
            return Response.ok().build();
        }
        if (StringUtils.isBlank(chart.getDatabaseId())) {
            return Response.error().message("请选择正确的数据源!").build();
        }
        Database db = databaseService.findOne(chart.getDatabaseId());
        DatabaseType dbType = DatabaseType.parse(db.getType());
        String parseType = JdbcConstants.MYSQL;
        SchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        switch (dbType) {
            case ORACLE:
                parseType = JdbcConstants.ORACLE;
                visitor = new OracleSchemaStatVisitor();
                break;
            case SQL_SERVER:
                parseType = JdbcConstants.SQL_SERVER;
                visitor = new SQLServerSchemaStatVisitor();
                break;
            default:
                break;

        }
        try {
            List<SQLStatement> sqlStatements = SQLUtils.parseStatements(chart.getDataSet(), parseType);
            if (sqlStatements.size() != 1) {
                return Response.error().message("只支持输入一条sql!").build();
            }
            sqlStatements.get(0).accept(visitor);
            Map<String, String> aliasMap = visitor.getAliasMap();
            if (!aliasMap.containsKey("id") || !aliasMap.containsKey("name")) {
                return Response.error().message("sql查询结果集必须包含id、name两列!").build();
            }
        } catch (ParserException e) {
            return Response.error().message("sql语法错误:" + e.getMessage()).build();
        }
        return Response.ok().build();
    }

    private void initParamMap(String selectMap) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (StringUtils.isNotBlank(selectMap)) {
            Map<String, String> paramMap = Maps.newHashMap();
            Map map = BeanUtils.describe(getLoginInfo());
            paramMap.putAll(map);
            paramMap.putAll(JSON.parseObject(
                    selectMap, new TypeReference<Map<String, String>>() {
                    }));
            JexlContextHolder.setJexlContext(paramMap);
            return;
        }
        JexlContextHolder.setJexlContext(getLoginInfo());
    }

}
