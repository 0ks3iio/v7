/**
 * FileName: GraphController.java
 * Author:   shenke
 * Date:     2018/6/4 下午7:26
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.parser.Feature;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.biz.CockpitDataQueryBiz;
import net.zdsoft.bigdata.data.biz.ReportDataQueryBiz;
import net.zdsoft.bigdata.data.code.ChartCategory;
import net.zdsoft.bigdata.data.code.ChartClassification;
import net.zdsoft.bigdata.data.code.ChartClassificationUtils;
import net.zdsoft.bigdata.data.code.ChartType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.echarts.OptionExposeFactory;
import net.zdsoft.bigdata.data.echarts.WrapChart;
import net.zdsoft.bigdata.data.echarts.WrapConvert;
import net.zdsoft.bigdata.data.entity.*;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.metadata.entity.Folder;
import net.zdsoft.bigdata.metadata.entity.FolderDetail;
import net.zdsoft.bigdata.metadata.entity.FolderEx;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.bigdata.metadata.service.FolderService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.echarts.enu.PositionEnum;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static net.zdsoft.bigdata.data.ChartBusinessType.CHART;

/**
 * 图表设置Controller
 *
 * @author shenke
 * @since 2018/6/4 下午7:26
 */
@Controller
@RequestMapping(
        value = "bigdata/chart"
)
public class GraphConfigurationController extends BigdataBaseAction {
    private Logger logger = LoggerFactory.getLogger(GraphConfigurationController.class);

    @Resource
    private ChartService chartService;
    @Resource
    private CockpitDataQueryBiz cockpitDataQueryBiz;
    @Resource
    private TagRelationService tagRelationService;
    @Resource
    private TagService tagService;
    @Resource
    private ReportTemplateService reportTemplateService;
    @Resource
    private ReportDataQueryBiz reportDataQueryBiz;
    @Resource
    private SubscribeService subscribeService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private RegionRemoteService regionRemoteService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private DatabaseService databaseService;
    @Resource
    private NosqlDatabaseService nosqlDatabaseService;
    @Resource
    private ApiService apiService;
    @Resource
    private FolderService folderService;
    @Resource
    private FolderDetailService folderDetailService;
    @Resource
    private BigLogService bigLogService;

    //@GetMapping(
    //        value = "{id}"
    //)
    @ResponseBody
    public Response doGetById(@Length(max = 32, min = 32, message = "不合法的id") @PathVariable("id") String id) {
        if (StringUtils.trimToEmpty(id).length() != 32) {
            return Response.error().message("ID不合法").build();
        }
        Chart chart = chartService.findOne(id);
        if (chart == null) {
            logger.error("{}对应的Chart-bg_chart 为空", id);
            return Response.error().message("图表为空").build();
        }
        return Response.ok().data(chart).build();
    }

    //1、列表首页
    @RequestMapping(
            value = "index",
            method = RequestMethod.GET
    )
    public String execute(ModelMap map) {
        //加载所有可以查询的标签
        LoginInfo loginInfo = getLoginInfo();
        Set<GraphIndexTag> graphIndexTags;
        //(当前单位新增的)
        List<Tag> tags = tagService.findTagsByUnitIdAndTagType(
                loginInfo.getUnitId(), CHART.getBusinessType().shortValue());
        graphIndexTags = tags.parallelStream().map(tag -> GraphIndexTag.create(tag.getId(), tag.getTagName()))
                .collect(Collectors.toSet());
        //订阅列表自带的
        List<Chart> charts = chartService.getCurrentUserCharts(loginInfo.getUserId(), loginInfo.getUnitId(),
                null, null, null, true, false, false);
        String[] chartIdArray = charts.stream().map(Chart::getId).toArray(String[]::new);
        if (ArrayUtils.isNotEmpty(chartIdArray)) {
            List<TagRelation> tagRelations = tagRelationService.getByBusinessId(chartIdArray);
            graphIndexTags.addAll(tagRelations.parallelStream().map(tagRelation -> {
                return GraphIndexTag.create(tagRelation.getTagId(), tagRelation.getTagName());
            }).collect(Collectors.toSet()));
        }
        map.put("tags", graphIndexTags);
        return "/bigdata/chart/chartEditIndex.ftl";
    }

    @ResponseBody
    @PostMapping("clone")
    public Response doClone(@RequestParam("id") String id,
                            @RequestParam("name") String name) {
        if (StringUtils.isBlank(id)) {
            return Response.error().message("id不能为空").build();
        }
        Chart chart = chartService.findOne(id);
        if (chart == null) {
            return Response.error().message("该图表已不存在，请刷新重试").build();
        }
        Chart copyChart = new Chart();
        copyChart.setId(UuidUtils.generateUuid());
        copyChart.setDatabaseId(chart.getDatabaseId());
        copyChart.setApiId(chart.getApiId());
        copyChart.setAutoUpdate(chart.getAutoUpdate());
        copyChart.setUpdateInterval(chart.getUpdateInterval());
        copyChart.setChartType(chart.getChartType());
        copyChart.setIsForCockpit(chart.getIsForCockpit());
        copyChart.setName(name);
        copyChart.setBusinessType(chart.getBusinessType());
        copyChart.setUnitId(getLoginInfo().getUnitId());
        copyChart.setModifyTime(new Date());
        copyChart.setDataSet(chart.getDataSet());
        copyChart.setDataSourceType(chart.getDataSourceType());
        copyChart.setCreationTime(new Date());
        copyChart.setOrderType(chart.getOrderType());
        copyChart.setParameters(chart.getParameters());
        copyChart.setTemplateId(chart.getTemplateId());
        copyChart.setThumbnailPath(chart.getThumbnailPath());
        copyChart.setMap(chart.getMap());
        chartService.save(copyChart);
        //业务日志埋点  新增
        LogDto logDto=new LogDto();
        logDto.setBizCode("insert-copyChart");
        logDto.setDescription("复制图表 "+copyChart.getName());
        logDto.setNewData(copyChart);
        logDto.setBizName("数据图表设计");
        bigLogService.insertLog(logDto);
        return Response.ok().build();
    }

    /**
     * 2、列表页
     *
     * @param ts 标签ID s 以","分割
     */
    @RequestMapping(
            value = "editList"
    )
    public String chartList(@RequestParam(value = "tags", required = false) String ts,
                            @RequestParam(value = "chartName", required = false) String name,
                            @RequestParam(value = "forCockpit", required = false) Boolean forCockpit,
                            ModelMap map) {
        LoginInfo login = getLoginInfo();

        String[] tagIdArray = null;
        if (StringUtils.isNotBlank(ts)) {
            tagIdArray = StringUtils.trim(ts).split(",");
        }
        Pagination pagination = createPagination();
        List<Chart> chartList = chartService.getCurrentUserCharts(login.getUserId(),
                login.getUnitId(), tagIdArray, name, pagination,true, false, forCockpit);
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
        String currentRequest = UrlUtils.getPrefix(getRequest());
        List<GraphIndexChart> graphIndexChartList = chartList.parallelStream().map(chart -> {
            GraphIndexChart c = new GraphIndexChart();
            c.setId(chart.getId()).setName(chart.getName())
                    .setThumbnailPath(fileUrl + "/" + chart.getThumbnailPath())
                    .setEdit(currentUnitId.equals(chart.getUnitId()))
                    .setDelete(currentUnitId.equalsIgnoreCase(chart.getUnitId()))
                    .setTagNameList(eachChartTagNames.get(chart.getId()));
            if (chart.getChartType()>=95) {
                c.setThumbnailPath(currentRequest + "/static/bigdata/images/thumbnail-" + chart.getChartType() + ".png");
            }
            Optional<OrderType> optional = OrderType.from(chart.getOrderType());
            c.setOrderName(optional.isPresent() ? optional.get().getOrderName() : StringUtils.EMPTY);
            return c;
        }).collect(Collectors.toList());
        map.put("charts", graphIndexChartList);
        map.put("pagination", pagination);
        map.put("isSearch", StringUtils.isNotBlank(name));
        map.put("isTagSearch", tagIdArray != null);
        return "/bigdata/chart/chartEditList.ftl";
    }

    /**
     * 3、编辑（新增）页面
     */
    @RequestMapping(
            //       why-conflict-delete/chartEdit
            value = "why-conflict-delete/chartEdit",
            method = RequestMethod.GET
    )
    public String edit(@RequestParam(value = "chartId", required = false) String chartId,
                       ModelMap map) {
        Chart chart = null;
        int datasourceType = 0;
        if (StringUtils.isNotBlank(chartId)) {
            chart = chartService.findOne(chartId);
            datasourceType = chart.getDataSourceType();
        } else {
            chart = new Chart();
            datasourceType = DataSourceType.STATIC.getValue();
            chart.setDataSourceType(datasourceType);
            chart.setOrderType(OrderType.SELF.getOrderType());
        }
        initDatasourceType2ModelMap(datasourceType, map);
        map.put("chart", chart);
        initEditData2ModelMap(chart, map);
        initEditMapData2ModelMap(map);
        map.addAttribute("province", StringUtils.substring(chart.getMap(), 0, 2));
        map.addAttribute("city", StringUtils.substring(chart.getMap(), 2));
        //填充各种图形的默认数据

        //return "/bigdata/chart/chartEditEdit.ftl";
        map.put("database", chart.getDataSourceType() == DataSourceType.DB.getValue());
        map.put("api", chart.getDataSourceType() == DataSourceType.API.getValue());
        map.put("orderTypes", OrderType.useValues());
        map.put("orderType", chart.getOrderType());
        Map<String, List<Folder>> folderMap = folderService.findAllFolderForDirectory();
        List<FolderEx> folderTree = folderService.findFolderTree();
        List<FolderDetail> folderDetails = folderDetailService.findListBy("businessId", chart.getId());
        map.put("folderDetail", folderDetails.size() > 0 ? folderDetails.get(0) : new FolderDetail());
        map.put("folderMap", folderMap);
        map.put("folderTree", folderTree);
        return "/bigdata/chart/chartEditConfig.ftl";
    }



    @RequestMapping(path = "parameters/echarts")
    public String loadEchartsParametersPage(@RequestParam("type") Integer type,
                                            @RequestBody Chart chart,
                                            ModelMap map) {
        //根据不同的图表类型，position不同

        Optional<ChartCategory> category = ChartCategory.valueFrom(type);
        if (category.isPresent()) {
            switch (category.get().getChartSeries()) {
                case bar:
                case line:
                case scatter:
                case map:
                case radar:
                    map.put("sLabelPositions", Arrays.stream(PositionEnum.values()).map(PositionEnum::name).collect(Collectors.toList()));
                    break;
                case pie:
                case funnel:
                    map.put("sLabelPositions", Arrays.asList("center", "outside", PositionEnum.inside.name()));
                    break;
                default:
                    map.put("sLabelPositions", Collections.emptyList());
            }

            //新的或者demo数据
            Object oe = null;
            if (StringUtils.isNotBlank(chart.getId())) {
                Chart c = chartService.findOne(chart.getId());
                if (c != null && StringUtils.isNotBlank(c.getParameters())
                        && c.getChartType() == type) {
                    oe = OptionExposeFactory.getOptionExpose(c.getParameters(), ChartCategory.valueFrom(chart.getChartType()).get().getChartSeries(), chart.getChartType());
                }
            }
            if (chart.getOptionExpose() != null && !((Map)chart.getOptionExpose()).isEmpty()) {
                oe = OptionExposeFactory.getOptionExpose(JSONObject.toJSONString(chart.getOptionExpose()), ChartCategory.valueFrom(chart.getChartType()).get().getChartSeries(), chart.getChartType());
            }
            if (oe == null) {
                oe = OptionExposeFactory.getOptionExpose(chart.getChartType(), ChartCategory.valueFrom(chart.getChartType()).get().getChartSeries());
            }

            Result result = cockpitDataQueryBiz.executeQueryResult(getSession(), chart);
            //默认的OE在转换的时候设置
            map.put("dynamicSetting", true);
            try {
                WrapConvert.convert(Collections.singletonList(WrapChart.build(chart, result)), chart.getChartType(), false, oe);
            } catch (EntryUtils.DataException e) {
                map.put("dynamicSetting", false);
            }
            map.put("optionExpose", oe);
        }
        map.put("type", type);
        return category.map(chartCategory -> "/bigdata/chart/parameters/chartEditEcharts-" + chartCategory.getChartSeries().name() + ".ftl")
                .orElse("/bigdata/chart/parameters/chartEditEcharts-error.ftl");
    }

    @ResponseBody
    @RequestMapping(
            value = "template/data"
    )
    public Response doGetTemplate(@RequestParam(value = "chartType", defaultValue = "-1") Integer chartType,
                                  @RequestParam(value = "mapName", required = false) String mapName) {
        String fileName = null;
        switch (chartType) {
            case ChartType.BAR_STACK:
            case ChartType.BAR_STACK_STRIPE:
                fileName = "s2-stack";
                break;
            case ChartType.BAR_BASIC:
            case ChartType.BAR_STRIPE:
            case ChartType.LINE_AREA:
            case ChartType.LINE_BROKEN:
            case ChartType.BAR_LINE:
            case ChartType.SCATTER:
                fileName = "scatter";
                break;
            case ChartType.RADAR_BASIC:
                fileName = "radar";
                break;
            case ChartType.FUNNEL:
            case ChartType.FUNNEL_ASCENDING:
                fileName = "funnel";
                break;
            case ChartType.PIE_BASIC:
                fileName = "s1";
                break;
            case ChartType.PIE_FNF:
            case ChartType.PIE_DOUGHNUT:
            case ChartType.INNER_PIE_OUTTER_DOUGHNUT:
                fileName = "s2";
                break;
            case ChartType.PIE_DOUGHNUT_COMPOSITE:
                fileName = "3-bar";
                break;
            case ChartType.MAP:
                fileName = "map";
                break;
            case ChartType.MAP_LINE:
                fileName = "mapLine";
                break;
            case ChartType.WORD_CLOUD:
                fileName = "wordCloud";
                break;
            case ChartType.DYNAMIC_NUMBER:
            case ChartType.NUMBER_DOWN:
            case ChartType.NUMBER_UP:
                fileName = "dynamic";
                break;
            case ChartType.SELF_NUMBER:
                fileName = "myNumber";
                break;
            case ChartType.SELF_TABLE:
                fileName = "myTable";
                break;
            case ChartType.GAUGE:
                fileName = "gauge";
                break;
            case ChartType.SANKEY:
                fileName = "sankey";
                break;
            case ChartType.GRAPH:
                fileName = "graph";
                break;
            case ChartType.TREE_MAP:
                fileName= "treeMap";
                break;
            default:
                fileName = "s1";
        }

        ClassPathResource resource = new ClassPathResource("/template/" + fileName + ".json");
        try {
            Object obj = new JSONReader(new InputStreamReader(resource.getInputStream(), "UTF-8"), Feature.OrderedField).readObject();
            Chart chart = new Chart();
            chart.setDataSet(JSONObject.toJSONString(obj));
            chart.setDataSourceType(DataSourceType.STATIC.getValue());
            chart.setChartType(chartType);
            chart.setMap(mapName);
            JSONObject val = new JSONObject();
            val.put("chart", chart);
            val.put("data", obj);
            return Response.ok().data(val).build();
        } catch (IOException e) {
            return Response.error().build();
        }
    }

    private void initMapElement2ModelMap(ModelMap map) {
        //支持的地图组件类型
    }

    private void initDatasourceType2ModelMap(int datasourceType, ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        List<Database> databases = databaseService.findDatabasesByUnitId(unitId);
        List<NosqlDatabase> nosqlDatabases = nosqlDatabaseService.findNosqlDatabasesByUnitIdAndTypes(unitId);
        List<AbstractDatabase> allDatabases = new ArrayList<>(databases.size() + nosqlDatabases.size());
        allDatabases.addAll(databases);
        allDatabases.addAll(nosqlDatabases);

        map.put("databases", allDatabases);
        List<Api> apis = apiService.findApisByUnitId(getLoginInfo().getUnitId());
        map.put("apis", apis);
    }

    private void initEditData2ModelMap(Chart chart, ModelMap map) {
        //图表类型
        List<ChartClassification> classifications =
                ChartClassificationUtils.getEnableClassifications(UrlUtils.getPrefix(getRequest()));
        map.put("chartTypes", classifications);
        //数据源类型
        map.put("dataSourceTypes", Arrays.asList(DataSourceType.values()));
        //标签
        Map<String, TagRelation> tagRelationMap = new HashMap<>();
        if (StringUtils.isNotBlank(chart.getId())) {
            //该
            tagRelationMap.putAll(tagRelationService.getByBusinessId(new String[]{chart.getId()})
                    .stream().collect(Collectors.toMap(TagRelation::getTagId, t -> t)));
        }
        List<GraphEditTag> editTagList = tagService.findTagsByUnitIdAndTagType(
                getLoginInfo().getUnitId(), CHART.getBusinessType().shortValue())
                .parallelStream().map(tag -> {
                    GraphEditTag editTag = new GraphEditTag();
                    editTag.setId(tag.getId());
                    editTag.setName(tag.getTagName());
                    editTag.setSelected(tagRelationMap.containsKey(tag.getId()));
                    return editTag;
                }).collect(Collectors.toList());
        map.put("tags", editTagList);
        //属于哪个系列
        Optional<ChartCategory> optional = ChartCategory.valueFrom(chart.getChartType());

        map.put("seriesName", optional.orElse(ChartCategory.basic_bar).getChartSeries().name());
    }

    private void initEditMapData2ModelMap(ModelMap map) {
        List<Region> regions = SUtils.dt(regionRemoteService.findProviceRegionByType(Region.TYPE_1), Region.class);
        regions = regions.stream().filter(region -> !"00".equals(region.getRegionCode())).collect(Collectors.toList());
        map.put("regions", regions);
    }

    @RequestMapping(
            value = "next",
            method = RequestMethod.POST
    )
    public String next(@RequestBody Chart chart, ModelMap map) {
        initEditData2ModelMap(chart, map);
        map.put("chart", chart);
        map.put("orderTypes", OrderType.useValues());
        map.put("orderType", chart.getOrderType());
        return "/bigdata/chart/chartEditBasicEdit.ftl";
    }

    @RequestMapping(
            value = "back/edit",
            method = RequestMethod.POST
    )
    public String backEditFromBasicEdit(@RequestBody Chart chart, ModelMap map,
                                        @RequestParam(value = "tags[]", required = false) String[] tags,
                                        @RequestParam(value = "orderUnit[]", required = false) String[] orderUnits,
                                        @RequestParam(value = "orderTeacher[]", required = false) String[] orderTeachers) {

        initDatasourceType2ModelMap(chart.getDataSourceType(), map);
        if (chart.getOptionExpose() != null && !((Map) chart.getOptionExpose()).isEmpty()) {
            Object oe = OptionExposeFactory.getOptionExpose(JSONObject.toJSONString(chart.getOptionExpose()), ChartCategory.valueFrom(chart.getChartType()).get().getChartSeries(), chart.getChartType());
            map.put("optionExpose", oe);
        }
        map.put("chart", chart);
        initEditData2ModelMap(chart, map);
        initEditMapData2ModelMap(map);
        map.put("run", true);
        return "/bigdata/chart/chartEditConfig.ftl";
    }

    //4、生成Echarts图表或者自定义图表页面

    /**
     * 保存
     */
    @ResponseBody
    @RequestMapping(
            value = "save",
            method = RequestMethod.POST

    )
    public Response save(@RequestBody Chart chart) {

        boolean flag=false;
        Chart oldChart=new Chart();
        Response validate = validate(chart);
        if (validate != null) {
            return validate;
        }
        String[] tags = Optional.ofNullable(chart.getTags()).orElseGet(Collections::emptyList).toArray(new String[0]);
        String[] orderUnits = Optional.ofNullable(chart.getOrderUnits()).orElseGet(Collections::emptyList).toArray(new String[0]);
        String[] orderTeachers = Optional.ofNullable(chart.getOrderTeachers()).orElseGet(Collections::emptyList).toArray(new String[0]);
        List<Chart> c = chartService.findListBy("name", chart.getName());
        LoginInfo loginInfo = getLoginInfo();
        if (StringUtils.isBlank(chart.getId())) {
            if (c.size() > 0) {
                return Response.error().message("图表名称已被使用").build();
            }
            chart.setId(UuidUtils.generateUuid());
            chart.setCreationTime(new Date());
            chart.setModifyTime(chart.getCreationTime());
            chart.setUnitId(loginInfo.getUnitId());
            chart.setOperatorId(loginInfo.getUserId());
            flag=true;
        } else {
            oldChart=chartService.findOne(chart.getId());
            if (c.size() > 1 || ( c.size()==1 && !c.get(0).getId().equals(chart.getId()))) {
                return Response.error().message("图表名称已被使用").build();
            }
            chart.setModifyTime(new Date());
        }
        if (!loginInfo.getUnitId().equals(chart.getUnitId())) {
            return Response.error().message("只有图表的创建单位才可以修改图表").build();
        }
        chart.setBusinessType(CHART.getBusinessType());
        String[] orderUsers = orderTeachers;
        if (OrderType.USER_AUTHORIZATION.getOrderType() != chart.getOrderType()) {
            if (!ArrayUtils.isEmpty(orderTeachers)) {
                orderUsers = userRemoteService.findListObjectByIn("ownerId", orderTeachers).stream()
                        .map(User::getId).toArray(String[]::new);
            }
        }

        if (chart.getOptionExpose() != null) {
            chart.setParameters(JSONObject.toJSONString(chart.getOptionExpose()));
        }

        //默认缩略图
        if (StringUtils.isBlank(chart.getThumbnailPath())) {
            chart.setThumbnailPath(CopyDefaultToStore.default_chart_icon_path);
        }
        chart.setDataSet(StringUtils.trimToEmpty(chart.getDataSet()));
        if (DataSourceType.STATIC.match(chart.getDataSourceType())) {
            if (StringUtils.startsWith(chart.getDataSet(), "[")) {
                chart.setDataSet(JSONObject.parseArray(chart.getDataSet()).toJSONString());
            } else {
                chart.setDataSet(JSONObject.parseObject(chart.getDataSet()).toJSONString());
            }
        } else {
            chart.setDataSet(chart.getDataSet());
        }
        chartService.saveChart(chart, tags, orderUnits, orderUsers);
       if (flag){
           //业务日志埋点  新增
           LogDto logDto=new LogDto();
           logDto.setBizCode("insert-chart");
           logDto.setDescription("数据图表 "+chart.getName());
           logDto.setNewData(chart);
           logDto.setBizName("数据图表设计");
           bigLogService.insertLog(logDto);
       }else{
           //业务日志埋点  修改
           LogDto logDto=new LogDto();
           logDto.setBizCode("update-chart");
           logDto.setDescription("数据图表 "+oldChart.getName());
           logDto.setOldData(oldChart);
           logDto.setNewData(chart);
           logDto.setBizName("数据图表设计");
           bigLogService.updateLog(logDto);
       }
        return Response.ok().data(chart.getId()).build();
    }

    private Response validate(Chart chart) {

        if (chart.getDataSourceType() == DataSourceType.DB.getValue()) {
            if (StringUtils.isBlank(chart.getDatabaseId())) {
                return Response.error().message("数据源为数据库类型时 数据源不能为空").build();
            }
            if (StringUtils.isBlank(chart.getDataSet())) {
                return Response.error().message("数据源为数据库类型时 SQL不能为空").build();
            }
        } else if (chart.getDataSourceType() == DataSourceType.API.getValue()) {
            if (StringUtils.isBlank(chart.getApiId())) {
                return Response.error().message("数据源为API类型时 数据源不能为空").build();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("dataSourceType is API, set dataSet null, commit dataSet is [{}]", chart.getDataSet());
            }
            chart.setDataSet(null);
        } else if (chart.getDataSourceType() == DataSourceType.STATIC.getValue()) {
            if (StringUtils.isBlank(chart.getDataSet())) {
                return Response.error().message("数据源类型为静态数据时 静态数据不能为空").build();
            }
            //数据格式校验

            chart.setApiId(null);
            chart.setDatabaseId(null);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(
            value = "delete",
            method = RequestMethod.GET
    )
    //@RequestMapping(
    //        value = "{chartId}",
    //        method = RequestMethod.DELETE
    //)
    public Response doDelete(String chartId) {
        //TODO 检查该图表是否被使用
        try {
            if (StringUtils.isBlank(chartId)) {
                return Response.error().message("无法删除chartId为空的图表！").build();
            }
            Chart oldChart = chartService.findOne(chartId);
            chartService.deleteChart(chartId);

            //业务日志埋点  删除
            LogDto logDto=new LogDto();
            logDto.setBizCode("delete-chart");
            logDto.setDescription("数据图表 "+oldChart.getName());
            logDto.setBizName("数据图表设计");
            logDto.setOldData(oldChart);
            bigLogService.deleteLog(logDto);

            if (logger.isDebugEnabled()) {
                logger.debug("图表 {} 被 {} 删除", chartId, getLoginInfo().getUserName());
            }
        } catch (Exception e) {
            logger.error("删除图表失败 chartId :" + chartId, e);
            return Response.error().message("删除失败 " + e.getMessage()).build();
        }
        return Response.ok().message("删除成功！").build();
    }

    @ResponseBody
    @RequestMapping(
            value = "batch",
            method = RequestMethod.POST
    )
    public Response doBatchDelete(@RequestParam("chartIds[]") String[] chartIds) {
        if (ArrayUtils.isNotEmpty(chartIds)) {
            chartService.batchDelete(chartIds);
        } else {
            return Response.error().message("图表ID为空，删除失败").build();
        }
        return Response.ok().message("删除成功！").build();
    }


    /**
     * 上传缩略图
     * 新增上传、编辑上传
     * 返回缩略图的最新路径
     */
    @ResponseBody
    @RequestMapping(
            value = "/upload/screenshot"
    )
    public Response uploadScreenShot(String data, String chartId) {

        String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
        //filePath = "/Users/shenke";
        File screenshotsDir = new File(filePath + File.separator + "bigdata");
        if (!screenshotsDir.exists()) {
            boolean createDir = screenshotsDir.mkdirs();
            if (!createDir) {
                logger.error("无法创建 {} 目录", screenshotsDir.getAbsolutePath());
                return Response.error().message("无法创建目录").build();
            }
        }
        String screenshotFilename = null;
        if (StringUtils.isBlank(chartId)) {
            screenshotFilename = UuidUtils.generateUuid() + ".png";
        } else {
            screenshotFilename = chartId + ".png";
        }
        //String screenshotsFilename = screenshotsDir.getPath() + File.separator + chartId + ".png";
        //将图片转换为特定的数据格式
        Base64 base64 = new Base64();
        byte[] imageBytes = base64.decode(data.substring("data:image/jpeg;base64".length()));
        //将图片转换为指定大小 237 * 135
        InputStream in = new ByteArrayInputStream(imageBytes);
        try {
            BufferedImage image = ImageIO.read(in);
            ImageIO.write(image, "png",
                    new File(screenshotsDir.getPath() + File.separator + screenshotFilename));
        } catch (IOException e) {
            logger.error("文件写入失败", e);
            return Response.error().message("缩略图更新失败").build();
        }
        //编辑上传，直接保存缩略图，更细
        String thumbnailPath = "bigdata" + File.separator + screenshotFilename + "?" + System.currentTimeMillis();
        if (StringUtils.isNotBlank(chartId)) {
            Chart chart = chartService.findOne(chartId);
            if (chart != null) {
                chart.setThumbnailPath(thumbnailPath);
                chartService.save(chart);
            }
        }
        return Response.ok().data(thumbnailPath).build();
    }

    public static class GraphEditTag {
        private String id;
        private String name;
        private Boolean selected;

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

        public Boolean getSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }
    }

    /**
     * 图表列表首页用于展示图表
     */
    public static class GraphIndexChart {
        private String id;
        private String name;
        /** 授权类型 */
        private String orderName;
        /**
         * 图表缩略图地址 http
         */
        private String thumbnailPath;
        private List<String> tagNameList;
        /**
         * 是否可以编辑
         */
        private Boolean edit;
        /**
         * 是否可以删除
         */
        private Boolean delete;

        public String getId() {
            return id;
        }

        public GraphIndexChart setId(String id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public GraphIndexChart setName(String name) {
            this.name = name;
            return this;
        }

        public String getThumbnailPath() {
            return thumbnailPath;
        }

        public GraphIndexChart setThumbnailPath(String thumbnailPath) {
            this.thumbnailPath = thumbnailPath;
            return this;
        }

        public List<String> getTagNameList() {
            return tagNameList;
        }

        public GraphIndexChart setTagNameList(List<String> tagNameList) {
            this.tagNameList = tagNameList;
            return this;
        }

        public Boolean getEdit() {
            return edit;
        }

        public GraphIndexChart setEdit(Boolean edit) {
            this.edit = edit;
            return this;
        }

        public Boolean getDelete() {
            return delete;
        }

        public GraphIndexChart setDelete(Boolean delete) {
            this.delete = delete;
            return this;
        }

        public String getOrderName() {
            return orderName;
        }

        public void setOrderName(String orderName) {
            this.orderName = orderName;
        }
    }

    /**
     * 图表列表首页用于通过标签查询使用
     */
    public static class GraphIndexTag {
        private String id;
        private String name;

        static GraphIndexTag create(String id, String name) {
            return new GraphIndexTag().setId(id).setName(name);
        }

        public String getId() {
            return id;
        }

        public GraphIndexTag setId(String id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public GraphIndexTag setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            GraphIndexTag that = (GraphIndexTag) o;
            return Objects.equals(id, that.id) &&
                    Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {

            return Objects.hash(id, name);
        }
    }
}
