package net.zdsoft.bigdata.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.jasperreports.engine.JRException;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.biz.ReportDataQueryBiz;
import net.zdsoft.bigdata.data.code.ChartCategory;
import net.zdsoft.bigdata.data.code.ChartClassification;
import net.zdsoft.bigdata.data.code.ChartClassificationUtils;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.entity.*;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.data.vo.DatasourceTypeVO;
import net.zdsoft.bigdata.data.vo.ReportVO;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.metadata.entity.Folder;
import net.zdsoft.bigdata.metadata.entity.FolderDetail;
import net.zdsoft.bigdata.metadata.entity.FolderEx;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.bigdata.metadata.service.FolderService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static net.zdsoft.bigdata.data.ChartBusinessType.REPORT;

@Controller
@RequestMapping("/bigdata/report/template")
public class ReportEditController extends BigdataBaseAction {

    private Logger logger = LoggerFactory.getLogger(ReportEditController.class);
    @Resource
    private ReportTemplateService reportTemplateService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private ChartService chartService;
    @Resource
    private TagRelationService tagRelationService;
    @Resource
    private TagService tagService;
    @Resource
    private ReportTermService reportTermService;
    @Resource
    private ReportDataQueryBiz reportDataQueryBiz;
    @Autowired
    private UserRemoteService userRemoteService;
    @Resource
    private SubscribeService subscribeService;
    @Resource
    private DatabaseService databaseService;
    @Resource
    private FolderService folderService;
    @Resource
    private FolderDetailService folderDetailService;
    @Resource
    private BigLogService bigLogService;

    /**
     * 数据报表设计页面
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public String index(ModelMap model, HttpServletRequest request) {
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
                null, null, null, true, true, false);
        String[] chartIdArray = charts.stream().map(Chart::getId).toArray(String[]::new);
        if (ArrayUtils.isNotEmpty(chartIdArray)) {
            List<TagRelation> tagRelations = tagRelationService.getByBusinessId(chartIdArray);
            graphIndexTags.addAll(tagRelations.parallelStream().map(tagRelation -> {
                return GraphConfigurationController.GraphIndexTag.create(tagRelation.getTagId(), tagRelation.getTagName());
            }).collect(Collectors.toSet()));
        }
        model.addAttribute("tags", graphIndexTags);//.addObject("charts", chartVOList);
        model.addAttribute("fileUrl", sysOptionRemoteService.getFileUrl(request.getServerName()));
        return "/bigdata/report/index.ftl";
    }

    /**
     * 2、列表页
     *
     * @param ts 标签ID s 以","分割
     */
    @RequestMapping(
            value = "list"
    )
    public String list(@RequestParam(value = "tags", required = false) String ts,
                       @RequestParam(value = "chartName", required = false) String name,
                       ModelMap map, @RequestParam(value = "canDelete", required = false) String canDelete) {
        LoginInfo login = getLoginInfo();

        String[] tagIdArray = null;
        if (StringUtils.isNotBlank(ts)) {
            tagIdArray = StringUtils.trim(ts).split(",");
        }
        Pagination pagination = createPagination();
        List<Chart> chartList = chartService.getCurrentUserCharts(login.getUserId(),
                login.getUnitId(), tagIdArray, name, pagination, true, true, false);
        //获取每个图表标记的标签
        Map<String, List<String>> eachChartTagNames = new HashMap<>(chartList.size());
        tagRelationService.getByBusinessId(chartList.stream().map(Chart::getId).toArray(String[]::new))
                .forEach(tagRelation ->
                eachChartTagNames.computeIfAbsent(tagRelation.getBusinessId(), k -> new ArrayList<>())
                        .add(tagRelation.getTagName()));
        /**
         * 当图表是由上级单位授权而来的，该图表不可编辑
         * 根据订阅关系可以得知，由上级授权而来的图表具有以下任意特征
         *  1、 {@link net.zdsoft.bigdata.data.OrderType#OPEN} 该图表为系统级别公开
         *  2、 {@link net.zdsoft.bigdata.data.OrderType#UNIT_ORDER} 单位级别授权
         *      此时 {@link net.zdsoft.bigdata.data.entity.Chart} unitId和当前单位ID不一致
         *  3、 {@link net.zdsoft.bigdata.data.OrderType#UNIT_ORDER_USER_AUTHORIZATION 单位级别授权个人需授权
         *      此时 {@link net.zdsoft.bigdata.data.entity.Chart} unitId 和当前单位ID不一致，并且userId不为空
         * 当 {@link net.zdsoft.bigdata.data.entity.Chart#unitId} 和当前单位ID一致，则说明该图表是该单位新增的图表
         */
        final String fileUrl = sysOptionRemoteService.getFileUrl(getRequest().getServerName());
        final String currentUnitId = login.getUnitId();
        List<GraphConfigurationController.GraphIndexChart> graphIndexChartList = chartList.parallelStream().map(chart -> {
            GraphConfigurationController.GraphIndexChart c = new GraphConfigurationController.GraphIndexChart();
            ReportTemplate template = reportTemplateService.findOne(chart.getTemplateId());
            c.setId(chart.getId()).setName(chart.getName())
                    .setThumbnailPath(fileUrl + "/" + template.getThumbnailPath())
                    .setEdit(currentUnitId.equals(chart.getUnitId()))
                    .setDelete(currentUnitId.equalsIgnoreCase(chart.getUnitId()))
                    .setTagNameList(eachChartTagNames.get(chart.getId()));
            Optional<OrderType> optional = OrderType.from(chart.getOrderType());
            c.setOrderName(optional.isPresent() ? optional.get().getOrderName() : StringUtils.EMPTY);
            return c;
        }).collect(Collectors.toList());
        map.put("charts", graphIndexChartList);
        map.put("pagination", pagination);
        map.put("canDelete", StringUtils.isNotBlank(canDelete));
        map.put("isSearch", StringUtils.isNotBlank(name));
        map.put("isTagSearch", tagIdArray != null);
        return "/bigdata/reportQuery/reportQueryList.ftl";
    }

    /**
     * 编辑
     *
     * @param model
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/edit")
    public String edit(ModelMap model, @RequestParam(value = "chartId", required = false) String id, Chart chart, ReportTemplate template, String termMap, HttpServletRequest request) {

        if (StringUtils.isNotBlank(id)) {
            // 查出报表
            chart = chartService.findOne(id);
            // 查询模版
            ReportTemplate reportTemplate = reportTemplateService.findOne(chart.getTemplateId());
            // 查询条件
            List<ReportTerm> reportTerms = reportTermService.getReportTermByReportId(id);
            // 数据源id
            String datasourceId = StringUtils.isBlank(chart.getApiId()) ? chart.getDatabaseId() : chart.getApiId();

            model.addAttribute("template", reportTemplate);
            reportTerms.forEach(e -> e.setDataSet(e.getDataSet() != null ? (e.getDataSet().replaceAll("\\[", "@").replaceAll("\\]", "#").replaceAll("\r\n|\r|\n"," ")) : StringUtils.EMPTY));
            model.addAttribute("reportTerms", reportDataQueryBiz.toJSONString(JSON.toJSON(reportTerms).toString()).replaceAll("\n", "\\n"));
            model.addAttribute("chart", chart);
            model.addAttribute("datasourceId", datasourceId);
        } else {
            String datasourceId = StringUtils.isBlank(chart.getApiId()) ? chart.getDatabaseId() : chart.getApiId();
            template.setId(chart.getTemplateId());
            model.addAttribute("template", template);
            model.addAttribute("chart", chart);
            model.addAttribute("datasourceId", datasourceId);
            if (StringUtils.isNotBlank(termMap)) {
                LinkedHashMap<String, Map<String, String>> map = JSON.parseObject(
                        termMap, new TypeReference<LinkedHashMap<String, Map<String, String>>>() {
                        });
                List<ReportTerm> reportTerms = getReportTerms(map);
                model.addAttribute("reportTerms", reportDataQueryBiz.toJSONString(JSON.toJSON(reportTerms).toString()).replaceAll("\n", "\\n"));
            }
        }
        model.addAttribute("tags", this.getTags(chart));
        model.addAttribute("dataBaseList", databaseService.findDatabasesByUnitId(getLoginInfo().getUnitId()));
        model.addAttribute("dataSourceTypeVOs", DatasourceTypeVO.datasourceTypeVOs);
        model.addAttribute("fileUrl", sysOptionRemoteService.getFileUrl(getRequest().getServerName()));
        return "/bigdata/report/edit.ftl";
    }

    @ResponseBody
    @RequestMapping("/save")
    public Response save(ReportVO reportVO, ReportTemplate template, String termMap, Integer orderType,
                         @RequestParam(value = "tags[]", required = false) String[] tags,
                         @RequestParam(value = "orderUnit[]", required = false) String[] orderUnits,
                         @RequestParam(value = "orderTeacher[]", required = false) String[] orderTeachers) {
        //参数校验
        Response result = validate(reportVO);
        if (result != null) {
            return result;
        }
        // 组装图表对象
        Chart chart = constructChart(reportVO, orderType);
        if (StringUtils.isBlank(chart.getUnitId())) {
            chart.setUnitId(getLoginInfo().getUnitId());
        }
        if (!getLoginInfo().getUnitId().equals(chart.getUnitId())) {
            return Response.error().message("只有报表的创建单位才可以修改报表").build();
        }
        template.setUnitId(getLoginInfo().getUnitId());
        template.setId(StringUtils.isNotBlank(reportVO.getTemplateId()) ? reportVO.getTemplateId() : null);
        try {
            String[] orderUsers = orderTeachers;
            if (OrderType.USER_AUTHORIZATION.getOrderType() != orderType) {
                if (!ArrayUtils.isEmpty(orderTeachers)) {
                    orderUsers = userRemoteService.findListObjectByIn("ownerId", orderTeachers).stream().map(User::getId).toArray(String[]::new);
                }
                if (StringUtils.isNotBlank(chart.getId()) && ArrayUtils.isEmpty(orderUnits) && ArrayUtils.isEmpty(orderTeachers)) {
                    orderUnits = subscribeService.getSubscribeUnits(chart.getId()).stream().map(e -> e.getUnitId()).toArray(String[]::new);
                    orderUsers = subscribeService.getSubscribeUsers(chart.getId()).stream().map(e -> e.getUserId()).toArray(String[]::new);
                }
            }
            chartService.saveReport(chart, template, this.getReportTermTrees(termMap), tags, orderUnits, orderUsers);


        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
        return Response.ok().data(chart.getId()).build();
    }

    /**
     * 组装条件树
     *
     * @param termMap
     * @return
     */
    private List<ReportTermTree> getReportTermTrees(String termMap) {
        List<ReportTermTree> reportTermTrees = Lists.newArrayList();
        LinkedHashMap<String, Map<String, String>> map = JSON.parseObject(
                termMap, new TypeReference<LinkedHashMap<String, Map<String, String>>>() {
                });
        for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
            Map<String, String> mapValue = entry.getValue();
            String parentNode = mapValue.get("parentNode");
            if (StringUtils.isBlank(parentNode)) {
                ReportTermTree tree = new ReportTermTree();
                tree.setNodeName(entry.getKey());
                tree.setReportTerm(this.getReportTerm(mapValue));
                setChild(tree, map);
                reportTermTrees.add(tree);
            }
        }
        return reportTermTrees;
    }

    /**
     * 验证保存数据
     *
     * @param reportVO
     * @return
     */
    private Response validate(ReportVO reportVO) {
        if (logger.isDebugEnabled()) {
            logger.debug("save chartEditVO {}", reportVO.toString());
        }
        if (reportVO.getDataSourceType() == DataSourceType.DB.getValue()) {
            if (StringUtils.isBlank(reportVO.getDataSourceId())) {
                return Response.error().message("数据源为数据库类型时 数据源不能为空").build();
            }
            if (StringUtils.isBlank(reportVO.getDataSet())) {
                return Response.error().message("数据源为数据库类型时 SQL不能为空").build();
            }
        } else if (reportVO.getDataSourceType() == DataSourceType.API.getValue()) {
            if (StringUtils.isBlank(reportVO.getDataSourceId())) {
                return Response.error().message("数据源为API类型时 数据源不能为空").build();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("dataSourceType is API, set dataSet null, commit dataSet is [{}]", reportVO.getDataSet());
            }
            reportVO.setDataSet(null);
        } else if (reportVO.getDataSourceType() == DataSourceType.STATIC.getValue()) {
            if (StringUtils.isBlank(reportVO.getDataSet())) {
                return Response.error().message("数据源类型为静态数据时 静态数据不能为空").build();
            }
            //数据格式校验
            reportVO.setDataSourceId(null);
        }
        return null;
    }

    /**
     * 组装图表对象
     *
     * @param reportVO
     * @param orderType
     * @return
     */
    private Chart constructChart(ReportVO reportVO, Integer orderType) {
        Chart chart = new Chart();
        chart.setName(reportVO.getName());
        chart.setDataSourceType(reportVO.getDataSourceType());
        chart.setChartType(0);
        chart.setBusinessType(REPORT.getBusinessType());
        chart.setFolderId(reportVO.getFolderId());
        chart.setOrderId(reportVO.getOrderId());
        if (reportVO.getDataSourceType() == DataSourceType.DB.getValue()) {
            chart.setDatabaseId(reportVO.getDataSourceId());
        } else if (reportVO.getDataSourceType() == DataSourceType.API.getValue()) {
            chart.setApiId(reportVO.getDataSourceId());
        }
        chart.setOrderType((orderType == null || orderType==0) ? 1 : orderType);
        chart.setDataSet(reportVO.getDataSet());
        chart.setAutoUpdate(0);
        chart.setUpdateInterval(0);
        chart.setTemplateId(reportVO.getTemplateId());
        //入库
        if (StringUtils.isBlank(reportVO.getId())) {
            if (logger.isDebugEnabled()) {
                logger.debug("新增chart {}", reportVO);
            }
            chart.setId(UuidUtils.generateUuid());
            chart.setCreationTime(new Date());
            chart.setModifyTime(chart.getCreationTime());
            //业务日志埋点  新增
            LogDto logDto=new LogDto();
            logDto.setBizCode("insert-reportTemplate");
            logDto.setDescription("数据报表 "+chart.getName());
            logDto.setNewData(chart);
            logDto.setBizName("数据报表设计");
            bigLogService.insertLog(logDto);

        } else {
            chart.setId(reportVO.getId());
            Chart one = chartService.findOne(chart.getId());
            chart.setCreationTime(one.getCreationTime());
            chart.setUnitId(one.getUnitId());
            chart.setModifyTime(new Date());
            //业务日志埋点  修改
            LogDto logDto=new LogDto();
            logDto.setBizCode("update-reportTemplate");
            logDto.setDescription("数据报表 "+one.getName());
            logDto.setOldData(one);
            logDto.setNewData(chart);
            logDto.setBizName("数据报表设计");
            bigLogService.updateLog(logDto);

        }
        chart.setOperatorId(getLoginInfo().getUserId());
        return chart;
    }

    private void setChild(ReportTermTree tree, Map<String, Map<String, String>> map) {
        List<ReportTermTree> childs = Lists.newArrayList();
        for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
            Map<String, String> mapValue = entry.getValue();
            String parentNode = mapValue.get("parentNode");
            if (StringUtils.isNotBlank(parentNode) && parentNode.equals(tree.getNodeName())) {
                ReportTermTree child = new ReportTermTree();
                child.setNodeName(entry.getKey());
                child.setReportTerm(this.getReportTerm(mapValue));
                childs.add(child);
                tree.setChilds(childs);
                setChild(child, map);
            }
        }
    }

    private ReportTerm getReportTerm(Map<String, String> mapValue) {
        ReportTerm reportTerm = new ReportTerm();
        reportTerm.setTermName(mapValue.get("termName"));
        reportTerm.setTermKey(mapValue.get("termKey"));
        reportTerm.setDataSet(mapValue.get("dataSet"));
        reportTerm.setTermColumn(mapValue.get("termColumn"));
        Integer isFinalTerm = (mapValue.get("isFinalTerm") == null || "0".equals(mapValue.get("isFinalTerm"))) ? 0 : 1;
        reportTerm.setIsFinalTerm(isFinalTerm.shortValue());
        Integer isQuery = (mapValue.get("isQuery") == null || "0".equals(mapValue.get("isQuery"))) ? 0 : 1;
        reportTerm.setIsQuery(isQuery.shortValue());
        int termDataSourceType = Integer.parseInt(mapValue.get("termDataSourceType"));
        reportTerm.setDataSourceType(termDataSourceType);
        if (termDataSourceType == DataSourceType.DB.getValue()) {
            reportTerm.setTermDatabaseId(mapValue.get("termDataSourceId"));
        } else
            reportTerm.setTermApiId(mapValue.get("termDataSourceId"));
        return reportTerm;
    }

    private void initEditData2ModelMap(Chart chart, ModelMap map) {
        //图表类型
        List<ChartClassification> classifications =
                ChartClassificationUtils.getEnableClassifications(UrlUtils.getPrefix(getRequest()));
        map.put("chartTypes", classifications);
        //数据源类型
        map.put("dataSourceTypes", Arrays.asList(DataSourceType.values()));
        //标签
        map.put("tags", this.getTags(chart));
        //属于哪个系列
        Optional<ChartCategory> optional = ChartCategory.valueFrom(chart.getChartType());

        map.put("seriesName", optional.orElse(ChartCategory.basic_bar).getChartSeries().name());
    }

    /**
     * 基础设置
     * @param chart
     * @param template
     * @param map
     * @param termMap
     * @return
     */
    @RequestMapping(
            value = "next",
            method = RequestMethod.POST
    )
    public String next(Chart chart, ReportTemplate template, ModelMap map, String termMap) {
        initEditData2ModelMap(chart, map);
        if (chart.getOrderType() == 0) {
            chart.setOrderType(1);
        }
        map.put("orderTypes", OrderType.useValues());
        map.put("orderType", chart.getOrderType());
        map.put("chart", chart);
        map.put("termMap", reportDataQueryBiz.toJSONString(termMap));
        map.put("template", template);
        Map<String, List<Folder>> folderMap = folderService.findAllFolderForDirectory();
        List<FolderEx> folderTree = folderService.findFolderTree();
        List<FolderDetail> folderDetails = folderDetailService.findListBy("businessId", chart.getId());
        map.put("folderDetail", folderDetails.size() > 0 ? folderDetails.get(0) : new FolderDetail());
        map.put("folderMap", folderMap);
        map.put("folderTree", folderTree);
        return "/bigdata/report/baseEdit.ftl";
    }

    /**
     * 删除报表
     * @param reportId
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(String reportId) {
        Chart chart = chartService.findOne(reportId);
        // 删除报表
        chartService.deleteReport(reportId, chart.getTemplateId());
        //业务日志埋点  删除
        LogDto logDto=new LogDto();
        logDto.setBizCode("delete-reportTemplate");
        logDto.setDescription("数据报表 "+chart.getName());
        logDto.setBizName("数据报表设计");
        logDto.setOldData(chart);
        bigLogService.deleteLog(logDto);

        return Response.ok().data(reportId).build();
    }

    /**
     * 展示报表
     * @param reportVO
     * @param template
     * @param selectMap
     * @param request
     * @param response
     * @throws BigDataBusinessException
     */
    @ResponseBody
    @RequestMapping(
            value = "queryReport",
            method = RequestMethod.POST
    )
    public void queryReport(ReportVO reportVO, ReportTemplate template, String selectMap, HttpServletRequest request, HttpServletResponse response) throws BigDataBusinessException, IOException {
        //输出html
        try {
            if (StringUtils.isNotBlank(selectMap)) {
                Map<String, String> map = JSON.parseObject(
                        selectMap, new TypeReference<Map<String, String>>() {
                        });
                reportVO.getParamMap().putAll(map);
            }
            reportDataQueryBiz.showHtml(reportVO, reportDataQueryBiz.getJasperPath(reportVO, template, request), request, response);
        } catch (JRException e) {
            logger.error("数据结果集与模版不匹配，请输入正确的sql", e);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write("数据结果集与模版不匹配【" + e.getMessage() + "】，请输入正确的sql");
        } catch (IOException e) {
            logger.error("模版加载出错，请确认模版是否存在", e);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write("模版加载出错，请确认模版是否存在");
        } catch (BigDataBusinessException e) {
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误", e);
            throw new BigDataBusinessException("系统错误");
        }
    }

    @RequestMapping("previewReport")
    public String previewReport(ModelMap map, Chart chart, ReportTemplate template, String termMap) {
        map.put("chart", chart);
        if (StringUtils.isNotBlank(termMap)) {
            LinkedHashMap<String, Map<String, String>> tMap = JSON.parseObject(
                    termMap, new TypeReference<LinkedHashMap<String, Map<String, String>>>() {
                    });
            List<ReportTerm> reportTerms = getReportTerms(tMap);
            map.put("reportTerms", reportDataQueryBiz.toJSONString(JSON.toJSON(reportTerms).toString()).replaceAll("\n", "\\n"));
        }
        map.put("template", template);
        return "/bigdata/report/preview.ftl";
    }

    private List<ReportTerm> getReportTerms(Map<String, Map<String, String>> tMap) {
        List<ReportTerm> reportTerms = Lists.newArrayList();
        for (Map.Entry<String, Map<String, String>> entry : tMap.entrySet()) {
            Map<String, String> mapValue = entry.getValue();
            ReportTerm reportTerm = this.getReportTerm(mapValue);
            reportTerm.setId(entry.getKey());
            String parentNode = mapValue.get("parentNode");
            if (StringUtils.isNotBlank(parentNode)) {
                reportTerm.setCascadeTermId(parentNode);
            }
            reportTerm.setDataSet(reportTerm.getDataSet() != null ? reportTerm.getDataSet().replaceAll("\\[", "@").replaceAll("\\]", "#").replaceAll("\r\n|\r|\n"," ") : StringUtils.EMPTY);
            reportTerms.add(reportTerm);
        }
        return reportTerms;
    }

    private List<GraphConfigurationController.GraphEditTag> getTags(Chart chart) {

        Map<String, TagRelation> tagRelationMap = StringUtils.isNotBlank(chart.getId()) ? tagRelationService.getByBusinessId(new String[]{chart.getId()})
                .stream().collect(Collectors.toMap(TagRelation::getTagId, t -> t)) : Maps.newHashMap();

        return tagService.findTagsByUnitIdAndTagType(
                getLoginInfo().getUnitId(), REPORT.getBusinessType().shortValue())
                .parallelStream().map(tag -> {
                    GraphConfigurationController.GraphEditTag editTag = new GraphConfigurationController.GraphEditTag();
                    editTag.setId(tag.getId());
                    editTag.setName(tag.getTagName());
                    editTag.setSelected(tagRelationMap.containsKey(tag.getId()));
                    return editTag;
                }).collect(Collectors.toList());
    }

}
