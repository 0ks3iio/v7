/**
 * FileName: GraphQueryController.java
 * Author:   shenke
 * Date:     2018/6/5 下午8:38
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.bigdata.data.biz.CockpitDataQueryBiz;
import net.zdsoft.bigdata.data.code.ChartCategory;
import net.zdsoft.bigdata.data.code.ChartClassificationUtils;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.entity.Tag;
import net.zdsoft.bigdata.data.entity.TagRelation;
import net.zdsoft.bigdata.data.service.ChartService;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.data.service.TagRelationService;
import net.zdsoft.bigdata.data.service.TagService;
import net.zdsoft.bigdata.data.utils.JexlUtils;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static net.zdsoft.bigdata.data.ChartBusinessType.CHART;

/**
 * @author shenke
 * @since 2018/6/5 下午8:38
 */
@Controller
@RequestMapping("bigdata/chartQuery")
public class GraphQueryController extends BigdataBaseAction {

    @Resource
    private ChartService chartService;
    @Resource
    private CockpitDataQueryBiz cockpitDataQueryBiz;
    @Resource
    private TagRelationService tagRelationService;
    @Resource
    private TagService tagService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private OptionService optionService;

    @Resource
    private FolderDetailService folderDetailService;


    @RequestMapping(
            value = "index"
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
        Unit unit = unitRemoteService.findOneObjectById(loginInfo.getUnitId());
        //订阅列表自带的
        List<Chart> charts = chartService.getCurrentUserCharts(loginInfo.getUserId(), loginInfo.getUnitId(),
                null, null, null, false, false, false);
        String[] chartIdArray = charts.stream().map(Chart::getId).toArray(String[]::new);
        if (ArrayUtils.isNotEmpty(chartIdArray)) {
            List<TagRelation> tagRelations = tagRelationService.getByBusinessId(chartIdArray);
            graphIndexTags.addAll(tagRelations.parallelStream().map(tagRelation -> {
                return GraphIndexTag.create(tagRelation.getTagId(), tagRelation.getTagName());
            }).collect(Collectors.toSet()));
        }
        map.put("tags", graphIndexTags);
        return "/bigdata/chartQuery/chartQueryIndex.ftl";
    }

    @RequestMapping(
            value = "chartList"
    )
    public String chartList(@RequestParam(value = "tags", required = false) String ts,
                            @RequestParam(value = "chartName", required = false) String name,
                            ModelMap map, HttpServletRequest request, Pageable pageable) {
        LoginInfo login = getLoginInfo();

        String[] tagIdArray = null;
        if (StringUtils.isNotBlank(ts)) {
            tagIdArray = StringUtils.trim(ts).split(",");
        }
        Pagination pagination = createPagination();
        pagination.setPageSize(10);

        String[] ids = folderDetailService.getCharts(login.getUnitId(), login.getUserId()).toArray(new String[0]);

        Page<Chart> chartPages=null;
        List<Chart> chartList;
        if (ArrayUtils.isNotEmpty(ids)){
            chartPages=chartService.getChartsByIds(ids,pageable);
            chartList=chartPages.getContent();
        }else{
            chartList=Collections.emptyList();
        }
                //chartService.getCurrentUserCharts(login.getUserId(),
                //login.getUnitId(), tagIdArray, name, pagination, false, false, false);
        //获取每个图表标记的标签
        Map<String, List<String>> eachChartTagNames = new HashMap<>(chartList.size());
        tagRelationService.getByBusinessId(chartList.stream().map(Chart::getId).toArray(String[]::new))
                .forEach(tagRelation ->
                eachChartTagNames.computeIfAbsent(tagRelation.getBusinessId(), k -> new ArrayList<>())
                        .add(tagRelation.getTagName()));
        String currentRequest = UrlUtils.getPrefix(getRequest());
        final String fileUrl = sysOptionRemoteService.getFileUrl(getRequest().getServerName());
        JEchartQueryController.TitleContext context = JEchartQueryController.TitleContext.create();
        List<GraphIndexChart> graphIndexChartList = chartList.parallelStream().map(chart -> {
            GraphIndexChart c = new GraphIndexChart();
            c.setId(chart.getId()).setName(JexlUtils.evaluate(chart.getName(), context, false))
                    .setThumbnailPath(fileUrl + "/" + chart.getThumbnailPath())
                    .setTagNameList(eachChartTagNames.get(chart.getId()));
            if (chart.getChartType()>=95) {
                c.setThumbnailPath(currentRequest + "/static/bigdata/images/thumbnail-" + chart.getChartType() + ".png");
            }
            return c;
        }).collect(Collectors.toList());

        Page<GraphIndexChart>page=new PageImpl<>(graphIndexChartList,pageable,chartPages.getTotalElements());
        map.put("charts", page);
        map.put("isSearch", StringUtils.isNotBlank(name));
        map.put("isTagSearch", tagIdArray != null);
        if (isNewStyle()) {
            return "/bigdata/chartQuery/new-chartQueryList.ftl";
        }

        return "/bigdata/chartQuery/chartQueryList.ftl";
    }

    private boolean isNewStyle() {
        OptionDto styleDto = optionService.getAllOptionParam("pageStyle");
        String style = styleDto.getFrameParamMap().get("style");
        return "2".equals(style);
    }

    @RequestMapping(
            value = "{chartId}",
            method = RequestMethod.GET
    )
    public Object view(@PathVariable("chartId") String chartId) {
        Chart chart = chartService.findOne(chartId);
        Optional<ChartCategory> optional = ChartCategory.valueFrom(chart.getChartType());
        return new ModelAndView("/bigdata/chartQuery/chartQueryView.ftl")
                .addObject("chart", chart)
                .addObject("seriesName", optional.orElse(ChartCategory.basic_bar).getChartSeries().name())
                .addObject("chartTypes",
                        ChartClassificationUtils.getEnableClassifications(UrlUtils.getPrefix(getRequest())));
    }

    //@ResponseBody
    //@RequestMapping(
    //        value = "{chartId}/{chartType}",
    //        method = RequestMethod.GET,
    //        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    //)
    //public Response executeChart(@PathVariable("chartId") String chartId,
    //                             @PathVariable("chartType") int charType) {
    //    Chart chart = chartService.findOne(chartId);
    //    chart.setChartType(charType);
    //    if (chart.getChartType() <= 0) {
    //        return Response.error().message("不支持的图表类型 %s", charType).build();
    //    }
    //    OptionEx ex = cockpitDataQueryBiz.executeQuery(getSession(), chart);
    //    if (!ex.isSuccess()) {
    //        return Response.error().message(ex.getMessage()).build();
    //    }
    //    if (ex.isEcharts()) {
    //        ((Option)ex.getOption()).title().left("center").bottom("bottom").right("center").top(null);
    //    }
    //    return Response.ok().data(ex.toJSONString()).build();
    //}

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

    public static class GraphIndexChart {
        private String id;
        private String name;
        /**
         * 图表缩略图地址 http
         */
        private String thumbnailPath;
        private List<String> tagNameList;

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
    }
}
