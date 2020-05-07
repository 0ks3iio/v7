package net.zdsoft.bigdata.extend.data.action;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.biz.GroupAnalysisBiz;
import net.zdsoft.bigdata.extend.data.dto.ScaleDto;
import net.zdsoft.bigdata.extend.data.entity.UserProfile;
import net.zdsoft.bigdata.extend.data.entity.UserProfileTemplate;
import net.zdsoft.bigdata.extend.data.entity.UserTag;
import net.zdsoft.bigdata.extend.data.service.*;
import net.zdsoft.bigdata.frame.data.elastic.EsClientService;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.v3.index.action.BigdataBiBaseAction;
import net.zdsoft.bigdata.v3.index.entity.HeadInfo;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.coords.geo.Geo;
import net.zdsoft.echarts.element.Continuous;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.enu.*;
import net.zdsoft.echarts.series.EMap;
import net.zdsoft.echarts.series.Pie;
import net.zdsoft.echarts.series.Scatter;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.BarData;
import net.zdsoft.echarts.series.data.ScatterData;
import net.zdsoft.echarts.style.ItemStyle;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wangdongdong on 2018/7/23 16:58.
 */
@Controller
@RequestMapping("/bigdata/groupAnalysis")
public class GroupAnalysisController extends BigdataBiBaseAction {

    @Resource
    private UserTagService userTagService;
    @Resource
    private UserProfileService userProfileService;
    @Resource
    private StudentTagResultService studentTagResultService;
    @Resource
    private TeacherTagResultService teacherTagResultService;
    @Resource
    private EsClientService esClientService;
    @Resource
    private GroupAnalysisBiz groupAnalysisBiz;
    @Resource
    private UserProfileTemplateService userProfileTemplateService;

    @RequestMapping("/bi/index")
    public String index(ModelMap map) {
        String groupUrl = "/bigdata/groupAnalysis/index?style=bi";
        HeadInfo headInfo = getHeadInfo();
        headInfo.setTitle("用户画像");
        map.put("headInfo", headInfo);
        map.put("currentModuleUrl", groupUrl);
        return "/bigdata/v3/templates/bi/bi-transfer.ftl";
    }

    /**
     * 进去群体画像分析页面
     *
     * @param profileCode
     * @param model
     * @param type
     * @return
     */
    @RequestMapping("/index")
    public String index(String profileCode, ModelMap model, String type, String style) {
        boolean biStyle = false;
        if (StringUtils.isNotBlank(style) && "bi".equals(style)) {
            biStyle = true;
        }
        OptionDto esDto = optionService.getAllOptionParam("es");
        if (esDto == null || esDto.getStatus() == 0) {
            model.addAttribute("serverName", "Es");
            model.addAttribute("serverCode", "es");
            return "/bigdata/noServer.ftl";
        }
        List<UserProfile> userProfiles = userProfileService.findAll();
        if (StringUtils.isBlank(profileCode) && userProfiles.size() > 0) {
            profileCode = userProfiles.get(0).getCode();

            if ("kylin".equals(userProfiles.get(0).getAggEngine())) {
                OptionDto kylinDto = optionService.getAllOptionParam("kylin");
                if (kylinDto == null || kylinDto.getStatus() == 0) {
                    model.addAttribute("serverName", "Kylin");
                    model.addAttribute("serverCode", "kylin");
                    return "/bigdata/noServer.ftl";
                }
            }
        }
        // 关键查询条件
        List<UserTag> mainUserTags = userTagService.findListBy(new String[]{
                "profileCode", "isMainQc"}, new String[]{profileCode, "1"});
        Map<String, List<UserTag>> mainUserTagMap = Maps.newHashMap();
        mainUserTags.forEach(e -> mainUserTagMap.put(e.getId(), getSortedList(e.getId())));
        mainUserTags.sort(new UserTagComparator());

        // 非关键查询条件
        List<UserTag> secondaryUserTags = userTagService.findListBy(
                new String[]{"profileCode", "isSecondaryQc"}, new String[]{
                        profileCode, "1"});
        Map<String, List<UserTag>> secondaryUserTagMap = Maps.newHashMap();
        secondaryUserTags.forEach(e -> secondaryUserTagMap.put(e.getId(), getSortedList(e.getId())));
        secondaryUserTags.sort(new UserTagComparator());

        // 多选条件
        List<UserTag> multiUserTags = userTagService.findListBy(new String[]{
                "profileCode", "isMultipleChoice"}, new String[]{
                profileCode, "1"});
        model.addAttribute("userProfiles", userProfiles);
        model.addAttribute("mainUserTags", mainUserTags);
        model.addAttribute("secondaryUserTags", secondaryUserTags);
        model.addAttribute("multiUserTags", multiUserTags);
        model.addAttribute("mainUserTagMap", mainUserTagMap);
        model.addAttribute("secondaryUserTagMap", secondaryUserTagMap);
        model.addAttribute("profileCode", profileCode);
        model.addAttribute("type", type);
        model.addAttribute("biStyle", biStyle);
        return "/bigdata/extend/profileAnalysis/groupAnalysis.ftl";
    }

    private List<UserTag> getSortedList(String parentId) {
        List<UserTag> userTags = userTagService.findListBy("parentId", parentId);
        userTags.sort(new UserTagComparator());
        return userTags;
    }

    class UserTagComparator implements Comparator<UserTag> {
        @Override
        public int compare(UserTag o1, UserTag o2) {
            if (o1.getOrderId() == null)
                return -1;
            if (o2.getOrderId() == null) {
                return 1;
            }
            return o1.getOrderId().compareTo(o2.getOrderId());
        }
    }

    /**
     * 查询用户列表
     *
     * @param profileCode
     * @param tagArray
     * @param model
     * @param pageIndex
     * @return
     */
    @RequestMapping("/list")
    public String list(String profileCode, String tagArray, ModelMap model, Integer pageIndex) throws BigDataBusinessException {
        try {
            model.addAttribute("profileCode", profileCode);
            List<MetadataTableColumn> cols = userProfileService.findMainCols(profileCode);
            model.addAttribute("cols", cols);
            if (pageIndex == null) {
                model.addAttribute("pageIndex", 1);
                return "/bigdata/extend/profileAnalysis/groupAnalysisList.ftl";
            }
            String[] tagIds = JSON.parseArray(tagArray).toArray(new String[]{});
            List<UserTag> userTags = userTagService.findListByIdIn(tagIds);
            List<Json> userList = this.getUserList(pageIndex, userTags, profileCode,cols);
            model.addAttribute("userList", userList);
            model.addAttribute("pageIndex", pageIndex + 1);
            model.addAttribute("count", userList.size());
        } catch (Exception e) {
            throw new BigDataBusinessException("查询出错，请检查标签是否维护正确!");
        }
        return "/bigdata/extend/profileAnalysis/groupAnalysisListDetail.ftl";
    }

    /**
     * 获取用户集合
     *
     * @param pageIndex
     * @param userTags
     * @param profileCode
     * @return
     */
    private List<Json> getUserList(Integer pageIndex, List<UserTag> userTags, String profileCode,List<MetadataTableColumn> cols) {
        return  queryResultFromEs(pageIndex, 50, userTags, profileCode,cols);
    }

    /**
     * es 查询数据
     *
     * @param pageIndex
     * @param userTags
     * @return
     */
    private List<Json> queryResultFromEs(Integer pageIndex, Integer pageSize, List<UserTag> userTags, String profileCode,List<MetadataTableColumn> cols) {
        List<UserTag> firstTagList = userTagService
                .getfirstUserTagByProfileCode(profileCode);
        Map<String, UserTag> colMap = EntityUtils.getMap(firstTagList, UserTag::getId);
        userTags.forEach(e -> {
            if (colMap.containsKey(e.getParentId())) e.setTargetColumn(colMap.get(e.getParentId()).getTargetColumn());
        });
        Map<String, List<UserTag>> columnMap = userTags.stream().collect(Collectors.groupingBy(UserTag::getTargetColumn));
        List<Json> paramList = new ArrayList<>();
        columnMap.forEach((k, v) -> {
            Json queryParam = new Json();
            queryParam.put("type", "must");
            queryParam.put("field", k);
            String value = "";
            for (UserTag userTag : v) {
                value = value + userTag.getTagName() + ";";
            }
            value = value.substring(0, value.length() - 1);
            queryParam.put("value", value);
            paramList.add(queryParam);
        });

        List<String> resultFieldList = new ArrayList<String>();
        cols.forEach(c ->{
        	 resultFieldList.add(c.getColumnName());
        });

        Json pg = new Json();
        pg.put("pageIndex", pageIndex);
        pg.put("pageSize", pageSize);

        List<UserProfile> userProfiles = userProfileService.findListBy("code", profileCode);
        UserProfile profile = userProfiles.get(0);

        return esClientService.multiGroupQuery(profile.getIndexName(), profile.getTypeName(), paramList, resultFieldList, null, pg);
    }

    /**
     * 切换到画像页面
     *
     * @param profileCode
     * @param tagArray
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/groupProfile")
    public String groupProfile(String profileCode, String tagArray,
                               HttpServletRequest request, ModelMap model) {

        model.addAttribute("profileCode", profileCode);
        model.addAttribute("tagArray", tagArray);
        if ("student".equals(profileCode)) {
            return "/bigdata/extend/profileAnalysis/studentGroupProfile.ftl";
        } else if ("teacher".equals(profileCode)) {
            return "/bigdata/extend/profileAnalysis/teacherGroupProfile.ftl";
        }
        return "";
    }

    /**
     * 导出
     *
     * @param allColumn
     * @param profileCode
     * @param tagArray
     * @param response
     */
    /*@RequestMapping("/export1")
    public void export(String profileCode, String tagArray,
                       HttpServletResponse response) {
        Map<String, List<Map<String, String>>> sheetName2RecordListMap = Maps
                .newHashMap();
        List<Map<String, String>> recordList = Lists.newArrayList();
        Map<String, List<String>> titleMap = Maps.newHashMap();
        String[] tagIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(
                tagArray, ",");

        List<UserTag> userTags = userTagService.findListByIdIn(tagIds);
        // 查询数据
        List<MetadataTableColumn> cols = userProfileService.findMainCols(profileCode);
        List<Json> esList = queryResultFromEs(1, 9999, userTags, profileCode,cols);
        String fileName = "群体画像分析";
        List<String> tis = cols.stream().map(p -> p.getName()).collect(Collectors.toList());
        esList.forEach(e -> {
        	 Map<String, String> sMap = Maps.newHashMap();
        	 cols.forEach(c ->{
                 System.out.print(c.getName()+"------");
                 System.out.print(c.getColumnName()+"------");
                 System.out.println(e.getString(c.getColumnName()));
                 sMap.put(c.getName(), e.getString(c.getColumnName()));
        	 });
        	 recordList.add(sMap);
        });
        if ("student".equals(profileCode)) {
            fileName = fileName + "-学生";
        } else if ("teacher".equals(profileCode)) {
            fileName = fileName + "-教师";
        }

        sheetName2RecordListMap.put("groupAnalysis", recordList);
        titleMap.put("groupAnalysis", tis);
        ExportUtils ex = ExportUtils.newInstance();
        ex.exportXLSFile(fileName, titleMap, sheetName2RecordListMap, response);

    }*/


    @RequestMapping("/export")
    public void export(String profileCode, String tagArray,
                       HttpServletResponse response) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);

            String[] tagIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(
                    tagArray, ",");
            List<UserTag> userTags = userTagService.findListByIdIn(tagIds);
            // 查询数据
            List<MetadataTableColumn> cols = userProfileService.findMainCols(profileCode);
            List<Json> esList = queryResultFromEs(1, 9999, userTags, profileCode,cols);

            List<String> head=new ArrayList<>();
            List<List<Object>> data = new ArrayList<>();
            //获取表头
            for(int i=0;i<cols.size();i++){
                head.add(cols.get(i).getName());
            }
            //获取内容
            esList.forEach(e->{
                List<Object> content=new ArrayList<>();
                cols.forEach(c->{
                    content.add(e.getString(c.getColumnName()));
                });
                data.add(content);
            });
            String fileName = "群体画像分析" ;
            if ("student".equals(profileCode)) {
                fileName = fileName + "-学生";
            } else if ("teacher".equals(profileCode)) {
                fileName = fileName + "-教师";
            }
            fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
            Sheet sheet1=new Sheet(1,0);
            List<List<String>> list = new ArrayList<>();
            head.forEach(h -> list.add(Collections.singletonList(h)));
            sheet1.setHead(list);
            writer.write1(data, sheet1);
            writer.finish();
            response.setHeader("Cache-Control", "");
            response.setContentType("application/data;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            OutputStream ro = response.getOutputStream();
            ro.write(out.toByteArray());
            ro.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 男女比例
     *
     * @param profileCode
     * @param tagArray
     * @return
     */
    @RequestMapping("/getSexProportion")
    @ResponseBody
    public Response getSexProportion(String profileCode, String tagArray) {
        try {
            List<Json> kylinList = groupAnalysisBiz.queryTagResult(profileCode, tagArray, "sex");
            long total = 0;
            long manCount = 0;
            long womanCount = 0;
            for (Json json : kylinList) {
                String sex = json.getString("key");
                if ("男".equals(sex)) {
                    manCount = json.getLong("value");
                } else if ("女".equals(sex)) {
                    womanCount = json.getLong("value");
                }
                total += json.getLong("value");
            }
            Json json = new Json();
            json.put("totalCount", total);
            json.put("manCount", manCount);
            json.put("womanCount", womanCount);
            json.put("manProportion", total == 0 ? 0 : ((float) manCount / (float) total * 100));
            json.put("womanProportion", total == 0 ? 0 : ((float) womanCount / (float) total * 100));
            return Response.ok().data(json).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.error().message("查询出错，请检查标签是否维护正确").build();
        }
    }

    /**
     * 地图
     *
     * @return
     */
    @RequestMapping("/getMapOption")
    @ResponseBody
    public Response getMapOption(String profileCode, String tagArray) {

        List<Json> kylinList = null;
        try {
            kylinList = groupAnalysisBiz.queryTagResult(profileCode, tagArray, "location");
        } catch (Exception e) {
            return Response.error().message("查询出错，请检查标签是否维护正确").build();
        }

        if (kylinList.size() < 1) {
            return Response.error().build();
        }

        List<JData.Entry> entryList = Lists.newArrayList();
        kylinList.forEach(e -> {
            JData.Entry entry = new JData.Entry();
            entry.setX(e.getString("key"));
            entry.setY(e.getLong("value"));
            entryList.add(entry);
        });

        JData data = new JData();
        data.setCoordSys(CoordinateSystem.geo);
        data.setSelfCoordSys(true);
        data.setMapType("china");
        data.setType(SeriesEnum.map);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);

        Option option = new Option();
        JConverter.convert(data, option);

        // 设置标题，提示框等
        option.title().show(true);
        Tooltip tooltip = new Tooltip().option(option);
        option.tooltip(tooltip);
        tooltip.trigger(Trigger.item).formatter("{b}<br/>{c}");

        // 设置视觉映射组件
        Continuous continuous = new Continuous().option(option);
        double max = 0;
        double min = 0;
        for (JData.Entry entry : entryList) {
            if (entry.getY().getClass().isPrimitive()
                    || entry.getY() instanceof Number) {
                Double d = Double.valueOf(entry.getY().toString());
                max = Math.max(d, max);
                min = Math.min(d, min);
            }
        }

        continuous.calculable(true);
        continuous.max(max);
        if (min > 0) {
            min = 0;
        }
        continuous.min(min);
        option.visualMap(continuous);
        Geo geo = new Geo().option(option);
        geo.setMap("00");
        for (Series series : option.series()) {
            series.label().show(true);
            ((EMap) series).roam(RoamEx.enable()).scaleLimit().max(2d).min(1d);
        }
        return Response.ok().data(JSON.toJSON(option).toString()).build();
    }

    private String dealString(String value) {
        return (StringUtils.isNotBlank(value) && !"null".equals(value)) ? value : "未知";
    }


    /**
     * 模板数据获取
     *
     * @param profileCode
     * @param tagArray
     * @return
     */
    @RequestMapping("/template")
    @ResponseBody
    public Response template(String profileCode, String tagArray, String templateId) {
        try {
            UserProfileTemplate temp = userProfileTemplateService.findOne(templateId);
            if (temp == null) {
                return Response.error().message("查询出错，模板已删除").build();
            }
            UserTag tag = userTagService.findOne(temp.getTagId());
            if (tag == null) {
                return Response.error().message("查询出错，标签已删除").build();
            }
            List<UserTag> stags = userTagService.getSecondaryUserTagByParenrId(tag.getId());
            if (CollectionUtils.isEmpty(stags)) {
            	return Response.error().message("查询出错，标签已删除").build();
            }
            List<Json> kylinList = groupAnalysisBiz.queryTagResult(profileCode, tagArray, tag.getTargetColumn());
            List<Json> orderList = Lists.newLinkedList();
            for(UserTag t:stags){
            	for(Json j:kylinList){
            		if(t.getTagName()!=null&&t.getTagName().equals(j.getString("key"))){
            			orderList.add(j);
            		}
            	}
            }
            if (CollectionUtils.isEmpty(orderList)) {
                return Response.error().message("无数据").build();
            }
            switch (temp.getChartType()) {
                case "scale":
                    ScaleDto scale = dealScale(orderList);
                    return Response.ok().data(scale).build();
                case "map":
                    Option map = dealMap(orderList, temp.getRegionCode());
                    return Response.ok().data(JSON.toJSON(map).toString()).build();
                case "bar"://柱状图
                    Option bar = dealBar(orderList);
                    return Response.ok().data(JSON.toJSON(bar).toString()).build();
                case "pie"://饼状图
                    Option pie = dealPie(orderList, tag.getTagName());
                    return Response.ok().data(JSON.toJSON(pie).toString()).build();
                case "line"://折线图
                    Option line = dealLine(orderList);
                    return Response.ok().data(JSON.toJSON(line).toString()).build();
                default:
                    return Response.error().message("图表类型未匹配").build();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.error().message("查询出错，请检查标签是否维护正确").build();
        }
    }

    private Option dealLine(List<Json> kylinList) {
        List<JData.Entry> entryList = Lists.newArrayList();
        kylinList.forEach(e -> {
            JData.Entry entry = new JData.Entry();
            entry.setX(e.getString("key"));
            entry.setY(e.getLong("value"));
            entryList.add(entry);
        });
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
        return option;
    }

    private Option dealPie(List<Json> kylinList, String name) {
        List<JData.Entry> entryList = Lists.newArrayList();
        kylinList.forEach(e -> {
            JData.Entry entry = new JData.Entry();
            entry.setX(e.getString("key"));
            entry.setY(e.getLong("value"));
            entryList.add(entry);
        });
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
                e.setName(name);
                ((Pie) e).radius(new Object[]{i, i + width});
                i = i + width + 5;
            }
        } else {
            for (Series e : option.series()) {
                e.setName(name);
                ((Pie) e).radius(new Object[]{"45%", "80%"});
            }
        }

        // 设置标题，提示框等
        option.title().show(true);
        Tooltip tooltip = new Tooltip().option(option);
        option.tooltip(tooltip);

        option.grid().forEach(e -> e.bottom(BottomEx.create("5%")).containLabel(true));

        tooltip.trigger(Trigger.item);
        tooltip.formatter("{a} <br/>{b} : {c} ({d}%)");
        return option;
    }

    private Option dealBar(List<Json> kylinList) {
        List<JData.Entry> entryList = Lists.newLinkedList();
        kylinList.forEach(e -> {
            JData.Entry entry = new JData.Entry();
            entry.setX(e.getString("key"));
            entry.setY(e.getLong("value"));
            entryList.add(entry);
        });
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
        option.legend().show(true);
        tooltip.trigger(Trigger.axis);

        option.grid().forEach(e -> e.left(LeftEx.create("10%")).right(RightEx.create("5%")).top(TopEx.create("5%")).bottom(BottomEx.create("15%")));


        String[] color = new String[]{"#37a2da", "#34c4e9", "#96bfff",
                "#317deb", "#fedb5b", "#96beb8", "#ae89f3", "#ff9f7f",
                "#ae89f3", "#46c6a3"};

        for (Series e : option.series()) {
            int i = 0;
            for (Object b : e.getData()) {
                BarData bd = (BarData) b;
                ItemStyle itemStyle = new ItemStyle();
                itemStyle.color(color[i++ % color.length]);
                bd.itemStyle(itemStyle);
            }
        }
        return option;
    }

    private Option dealMap(List<Json> kylinList, String mapType) {
        List<JData.Entry> entryList = Lists.newArrayList();
        kylinList.forEach(e -> {
            JData.Entry entry = new JData.Entry();
            entry.setX(e.getString("key"));
            entry.setY(e.getLong("value"));
            entryList.add(entry);
        });

        JData data = new JData();
        data.setCoordSys(CoordinateSystem.geo);
        data.setSelfCoordSys(true);
        data.setMapType(mapType);
        data.setType(SeriesEnum.map);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);

        Option option = new Option();
        JConverter.convert(data, option);


//	        data.setCoordSys(CoordinateSystem.geo);
//	        data.setCoordSysIndex(0);
//	        data.setType(SeriesEnum.scatter);
//	        JConverter.convert(data, option);

        //设置标题，提示框等
        option.title().show(true).text("");
        Tooltip tooltip = new Tooltip().option(option);
        option.tooltip(tooltip);
        tooltip.trigger(Trigger.item).formatter("{b}<br/>{c}");

        //设置视觉映射组件
        Continuous continuous = new Continuous().option(option);
        double max = 0;
        double min = 0;
        for (JData.Entry entry : entryList) {
            if (entry.getY().getClass().isPrimitive() || entry.getY() instanceof Number) {
                Double d = Double.valueOf(entry.getY().toString());
                max = Math.max(d, max);
                min = Math.min(d, min);
            }
        }

        continuous.calculable(true);
        continuous.max(max);
        if (min > 0) {
            min = 0;
        }
        continuous.min(min);
        option.visualMap(continuous);
        Geo geo = new Geo().option(option);
        geo.setMap(mapType);
        for (Series series : option.series()) {
            if (series instanceof Scatter) {
                ((Scatter) series).symbolSize(0).symbol(SymbolEnum.pin);
                for (Object d : series.getData()) {
                    ((ScatterData) d).setVisualMap(false);
                }
                series.label().show(true).formatter("{@2}");
            } else if (series instanceof EMap) {
                series.label().show(true);
                ((EMap) series).roam(RoamEx.enable()).scaleLimit().max(2d).min(1d);
            }
        }
        option.setColor(Arrays.asList("#b9396d", "#d042a4", "#1ebcd3", "#9949d7", "#ee913a", "#3bb7f0", "#cdb112", "#b9396d"));
        return option;
    }

    private ScaleDto dealScale(List<Json> kylinList) {
        ScaleDto scale = new ScaleDto();
        for (Json json : kylinList) {
            scale.getDatas().add(scale.getData().setCount(json.getLong("value")).setName(json.getString("key")));
        }
        scale.computeScale();
        return scale;
    }


}
