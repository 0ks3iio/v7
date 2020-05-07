package net.zdsoft.bigdata.extend.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.zdsoft.bigdata.daq.data.component.BizOperationLogCollector;
import net.zdsoft.bigdata.daq.data.entity.BizOperationLog;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.entity.*;
import net.zdsoft.bigdata.extend.data.enums.EventCompareTimeEnum;
import net.zdsoft.bigdata.extend.data.enums.EventTimeEnum;
import net.zdsoft.bigdata.extend.data.service.*;
import net.zdsoft.bigdata.frame.data.druid.DruidAggregationParam;
import net.zdsoft.bigdata.frame.data.druid.DruidClientService;
import net.zdsoft.bigdata.frame.data.druid.DruidConstants;
import net.zdsoft.bigdata.frame.data.druid.DruidParam;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.bigdata.v3.index.action.BigdataBiBaseAction;
import net.zdsoft.bigdata.v3.index.entity.HeadInfo;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.coords.AxisPointer;
import net.zdsoft.echarts.coords.AxisPointerLabel;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.coords.data.AxisData;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.element.SliderDataZoom;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.enu.*;
import net.zdsoft.echarts.series.Line;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.style.ItemStyle;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 事件管理 Created by wangdongdong on 2018/9/26 15:16.
 */
@Controller
@RequestMapping("/bigdata/event")
public class EventManageController extends BigdataBiBaseAction {

    @Resource
    private EventService eventService;
    @Resource
    private EventTypeService eventTypeService;
    @Resource
    private EventPropertyService eventPropertyService;
    @Resource
    private EventIndicatorService eventIndicatorService;
    @Resource
    private DruidClientService druidClientService;
    @Resource
    private EventGroupService eventGroupService;
    @Resource
    private EventFavoriteService eventFavoriteService;
    @Resource
    private EventFavoriteParamService eventFavoriteParamService;
    @Resource
    private EventGroupFavoriteService eventGroupFavoriteService;
    @Resource
    private OptionService optionService;

    @RequestMapping("/bi/index")
    public String index4Bi(ModelMap map) {
        map.put("currentModuleUrl", "/bigdata/event/query");
        HeadInfo headInfo = getHeadInfo();
        headInfo.setTitle("事件分析");
        map.put("headInfo", headInfo);
        return "/bigdata/v3/templates/bi/bi-transfer.ftl";
    }

    @RequestMapping("/query")
    public String index(ModelMap map, String favoriteId, String groupId, @RequestParam(value = "isCopy", required = false) Boolean isCopy) {
        OptionDto druidDto = optionService.getAllOptionParam("druid");
        if (druidDto == null || druidDto.getStatus() == 0) {
            map.put("serverName", "druid");
            map.put("serverCode", "druid");
            return "/bigdata/noServer.ftl";
        }
        // 查询所有事件
        String unitId = getLoginInfo().getUnitId();
        Map<String, EventType> eventTypeMap = eventTypeService
                .findMapByUnitId(unitId);
        List<Event> events = eventService.findAll(unitId);
        Map<String, List<Event>> eventMap = events.stream().collect(Collectors.groupingBy(Event::getTypeId, Collectors.toList()));
        // 查询所有分组
        List<EventGroup> eventGroups = eventGroupService
                .findGroupListByUserId(getLoginInfo().getUserId());
        EventFavorite eventFavorite = StringUtils.isNotBlank(favoriteId) ? eventFavoriteService
                .findOne(favoriteId) : new EventFavorite();


        map.put("events", events);
        map.put("eventMap", eventMap);
        map.put("eventTypeMap", eventTypeMap);
        map.put("eventGroups", eventGroups);
        map.put("eventFavorite", eventFavorite);
        map.put("favoriteGroupIds", groupId);
        map.put("timeMap", EventTimeEnum.timeMap());
        map.put("compareTimeMap", EventCompareTimeEnum.timeMap());
        map.put("isCopy", isCopy);

        if (StringUtils.isBlank(favoriteId)) {
            return "/bigdata/extend/event/eventEdit.ftl";
        }

        this.initEventFavorite(map, favoriteId, eventFavorite);
        return "/bigdata/extend/event/eventEdit.ftl";
    }

    @RequestMapping("/manage")
    public String manage(ModelMap model) {
        return "/bigdata/extend/event/maintain/index.ftl";
    }

    /**
     * 初始化
     *
     * @param map
     * @param favoriteId
     * @param eventFavorite
     */
    private void initEventFavorite(ModelMap map, String favoriteId, EventFavorite eventFavorite) {
        Map<String, String> paramMap = eventFavoriteParamService
                .getMapByFavoriteId(favoriteId);
        // 属性
        map.put("conditionList", paramMap.get("filter"));
        map.put("eventPropertyIds", paramMap.get("dimension"));
        map.put("conditionRelation", paramMap.get("conditionRelation"));
        String contrastTimeInterval = paramMap.get("contrastTimeInterval");
        if (StringUtils.isNotBlank(contrastTimeInterval)) {
            map.put("compareTimeIntervalList", JSON.parseObject(contrastTimeInterval, String[].class));
        }
        map.put("eventPropertyIdList",
                Json.parseArray(paramMap.get("dimension"), String.class));

        Event event = eventService.findOne(eventFavorite.getEventId());
        Map<String, List<EventProperty>> propertyMap = getEventProperties(event);
        map.put("propertyMap", propertyMap);

        List<List<EventCondition>> conditions = JSON.parseObject(
                paramMap.get("filter"), new TypeReference<List<List<EventCondition>>>() {
                });

        Map<String, List<String>> propertyDictionaryMap = Maps.newHashMap();
        List<EventProperty> eventProperties = eventPropertyService.findAll();
        eventProperties.forEach(e -> {
            String dataDictionary = e.getDataDictionary();
            Map<String, String> stringMap = JSON.parseObject(dataDictionary, new TypeReference<Map<String, String>>() {
            });
            if (stringMap != null && stringMap.size() > 0) {
                propertyDictionaryMap.put(e.getId(), stringMap.values().stream().collect(Collectors.toList()));
            }
        });

        map.put("propertyDictionaryMap", propertyDictionaryMap);
        map.put("conditions", conditions);
        // 指标
        map.put("indexId", paramMap.get("index"));
        // 所有指标
        List<EventIndicator> indexList = eventIndicatorService
                .findIndicatorListByEventId(event.getId());
        map.put("indexList", indexList);
        // 时间
        String beginDate = DateUtils.date2String(eventFavorite.getBeginDate(),
                "yyyy-MM-dd");
        String endDate = DateUtils.date2String(eventFavorite.getEndDate(),
                "yyyy-MM-dd");
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("timeInfo", paramMap.get("timeInfo"));

        List<EventGroupFavorite> groupFavorites = eventGroupFavoriteService
                .findGroupListByFavoriteId(favoriteId);
        StringBuilder sb = new StringBuilder();
        groupFavorites.forEach(e -> sb.append(e.getGroupId()).append(","));
        map.put("favoriteGroupIds", sb.toString());
    }

    @RequestMapping("/getEventByType")
    @ResponseBody
    public Response getEventByType(String eventTypeId) {
        return Response.ok().data(eventService.findAllByTypeId(eventTypeId))
                .build();
    }

    @RequestMapping("/getEventIndex")
    @ResponseBody
    public Response getEventIndex(String eventId) {
        return Response
                .ok()
                .data(eventIndicatorService.findIndicatorListByEventId(eventId))
                .build();
    }

    @RequestMapping("/getEventProperty")
    @ResponseBody
    public Response getEventProperty(String eventId) {
        Event event = eventService.findOne(eventId);
        if (event == null) {
            return Response.ok().data(Maps.newHashMap()).build();
        }
        Map<String, List<EventProperty>> propertyMap = getEventProperties(event);
        return Response.ok().data(propertyMap).build();
    }

    @RequestMapping("/getPropertyDictionary")
    @ResponseBody
    public Response getPropertyDictionary(String propertyId) {
        EventProperty eventProperty = eventPropertyService.findOne(propertyId);
        if (eventProperty != null && StringUtils.isNotBlank(eventProperty.getDataDictionary())) {
            Map<String, String> stringMap = JSON.parseObject(eventProperty.getDataDictionary(), new TypeReference<Map<String, String>>() {
            });
            if (stringMap != null && stringMap.size() > 0) {
                return Response.ok().data(stringMap.values().stream().collect(Collectors.toList())).build();
            }
        }
        return Response.ok().data(Lists.newArrayList()).build();
    }

    private Map<String, List<EventProperty>> getEventProperties(Event event) {
        Map<String, List<EventProperty>> propertyMap = Maps.newLinkedHashMap();
        if (event.getEnvProperty() != null && event.getEnvProperty() == 1) {
            List<EventProperty> propertyList = eventPropertyService.getChartEventProperty("00000000000000000000000000000env");
            if (propertyList.size() > 0) {
                propertyMap.put("系统", propertyList);
            }
        }

        if (event.getTimeProperty() != null && event.getTimeProperty() == 1) {
            List<EventProperty> propertyList = eventPropertyService.getChartEventProperty("0000000000000000000000000000time");
            if (propertyList.size() > 0) {
                propertyMap.put("时间", propertyList);
            }
        }

        if (event.getUserProperty() != null && event.getUserProperty() == 1) {
            List<EventProperty> propertyList = eventPropertyService.getChartEventProperty("0000000000000000000000000000user");
            if (propertyList.size() > 0) {
                propertyMap.put("用户", propertyList);
            }
        }

        List<EventProperty> properties = eventPropertyService
                .getChartEventProperty(event.getId());

        if (properties.size() > 0) {
            propertyMap.put("自定义", properties);
        }

        return propertyMap;
    }

    @RequestMapping("/getFavoriteChartOption")
    @ResponseBody
    public Response getFavoriteChartOption(String favoriteId) {
        try {
            EventFavorite favorite = eventFavoriteService.findOne(favoriteId);

            BizOperationLog bizLog = new BizOperationLog();
            bizLog.setLogType(BizOperationLog.LOG_TYPE_QUERY);
            bizLog.setBizCode("event-query");
            bizLog.setBizName(favorite.getFavoriteName());
            bizLog.setSubSystem("大数据管理");
            bizLog.setDescription(favorite.getFavoriteName() + "查询");
            bizLog.setOperator(getLoginInfo().getRealName() + "(" + getLoginInfo().getUserName() + ")");
            bizLog.setOperationTime(new Date());
            BizOperationLogCollector.submitBizOperationLog(bizLog);

            if (favorite == null) {
                return Response.error().build();
            }
            Map<String, String> paramMap = eventFavoriteParamService
                    .getMapByFavoriteId(favoriteId);

            List<List<EventCondition>> conditions = JSON.parseObject(
                    paramMap.get("filter"), new TypeReference<List<List<EventCondition>>>() {
                    });

            List<EventProperty> properties = eventPropertyService
                    .findListByIdIn(Json.parseArray(paramMap.get("dimension"))
                            .toArray(new String[]{}));
            String timeInterval = paramMap.get("timeInfo");
            if (StringUtils.isBlank(timeInterval)) {
                String beginDate = DateUtils.date2String(favorite.getBeginDate(),
                        "yyyy-MM-dd");
                String endDate = DateUtils.date2String(favorite.getEndDate(),
                        "yyyy-MM-dd");
                timeInterval = beginDate + "~" + endDate;
            }
            String timeUnit = favorite.getGranularity();

            String chartType = favorite.getChartType();

            List<JData.Entry> entryList = getChartData(timeInterval, timeUnit, favorite.getEventId(),
                    paramMap.get("index"), conditions,
                    paramMap.get("conditionRelation"), properties, chartType);
            if (entryList.size() < 1) {
                return Response.error().message("查询无数据").build();
            }

            String contrastTimeInterval = paramMap.get("contrastTimeInterval");
            if (StringUtils.isNotBlank(contrastTimeInterval)) {
                List<List<JData.Entry>> contrastEntryList = Lists.newArrayList();

                String[] contrastTime = JSON.parseObject(contrastTimeInterval, String[].class);
                for (String interval : contrastTime) {
                    contrastEntryList.add(this.getChartData(interval, timeUnit,
                            favorite.getEventId(), paramMap.get("index"), conditions, paramMap.get("conditionRelation"), properties, chartType));
                }

                // 判断图表类型 需要页面上传值
                if ("bar".equals(chartType)) {
                    return Response.ok().data(getBarChartOption(entryList, "week".equals(timeUnit) ? 4 : 2, false, contrastEntryList)).build();
                }

                if ("pie".equals(chartType)) {
                    return Response.ok().data(getPieChartOption(entryList, favorite.getWindowSize(), contrastEntryList)).build();
                }

                if ("dynamic".equals(chartType)) {
                    return Response.ok().data(getLineChartOption(entryList, favorite.getWindowSize(), false, contrastEntryList)).build();
                }

                return Response.ok().data(getLineChartOption(entryList, "week".equals(timeUnit) ? 4 : 2, false, contrastEntryList)).build();
            }

            // 判断图表类型 需要页面上传值
            if ("bar".equals(chartType)) {
                return Response.ok().data(getBarChartOption(entryList, favorite.getWindowSize(), false, null)).build();
            }

            if ("pie".equals(chartType)) {
                return Response.ok().data(getPieChartOption(entryList, favorite.getWindowSize(), null)).build();
            }
            return Response.ok().data(getLineChartOption(entryList, favorite.getWindowSize(), false, null)).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/getChartOption")
    @ResponseBody
    public Response getChartOption(String eventId, String indexId,
                                   String conditionList, String chartType, String eventPropertyIds,
                                   String timeUnit, String timeInterval, String conditionRelation,
                                   @RequestParam(value = "contrastTimeInterval[]", required = false) String[] contrastTimeInterval) {
        try {
            if (StringUtils.isBlank(eventId) || StringUtils.isBlank(indexId)) {
                return Response.error().message("事件和指标不能为空").build();
            }

            List<List<EventCondition>> conditions = JSON.parseObject(
                    conditionList, new TypeReference<List<List<EventCondition>>>() {
                    });

            List<EventProperty> properties = eventPropertyService
                    .findListByIdIn(Json.parseArray(eventPropertyIds).toArray(
                            new String[]{}));

            List<JData.Entry> entryList = getChartData(timeInterval, timeUnit,
                    eventId, indexId, conditions, conditionRelation, properties, chartType);

            if (entryList.size() < 1) {
                return Response.error().build();
            }
            if (contrastTimeInterval != null && contrastTimeInterval.length > 0) {

                List<List<JData.Entry>> contrastEntryList = Lists.newArrayList();
                for (String interval : contrastTimeInterval) {
                    contrastEntryList.add(this.getChartData(interval, timeUnit,
                            eventId, indexId, conditions, conditionRelation, properties, chartType));
                }

                // 判断图表类型 需要页面上传值
                if ("bar".equals(chartType)) {
                    return Response.ok().data(getBarChartOption(entryList, "week".equals(timeUnit) ? 4 : 2, true, contrastEntryList)).build();
                }

                if ("pie".equals(chartType)) {
                    return Response.ok().data(getPieChartOption(entryList, 2, contrastEntryList)).build();
                }

                if ("dynamic".equals(chartType)) {
                    return Response.ok().data(getLineChartOption(entryList, 3, true, contrastEntryList)).build();
                }

                return Response.ok().data(getLineChartOption(entryList, "week".equals(timeUnit) ? 4 : 2, true, contrastEntryList)).build();
            }

            // 判断图表类型 需要页面上传值
            if ("bar".equals(chartType)) {
                return Response.ok().data(getBarChartOption(entryList, 2, true, null)).build();
            }

            if ("pie".equals(chartType)) {
                return Response.ok().data(getPieChartOption(entryList, 2, null)).build();
            }

            if ("dynamic".equals(chartType)) {
                return Response.ok().data(getLineChartOption(entryList, 3, true, null)).build();
            }

            return Response.ok().data(getLineChartOption(entryList, 2, true, null)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error().data("出错了:" + e.getMessage()).build();
        }
    }

    @RequestMapping("/saveEventFavorite")
    @ResponseBody
    public Response saveEventFavorite(EventFavorite eventFavorite,
                                      String eventGroupIds, String conditionList,
                                      String conditionRelation, String indexId, String eventPropertyIds,
                                      String timeInfo, String contrastTimeInterval) {
        try {
            if (StringUtils.isBlank(timeInfo)) {
                return Response.error().message("时间范围不能为空!").build();
            }

            if (eventFavorite.getOrderId() == null || eventFavorite.getOrderId() < 0) {
                return Response.error().message("排序号不能为负数!").build();
            }

            LoginInfo loginInfo = getLoginInfo();

            eventFavorite.setUnitId(loginInfo.getUnitId());
            eventFavorite.setUserId(loginInfo.getUserId());
            // 参数
            List<EventFavoriteParam> eventFavoriteParams = getEventFavoriteParams(conditionList, conditionRelation, indexId, eventPropertyIds, contrastTimeInterval);
            List<String> eventGroupIdList = Json.parseArray(eventGroupIds, String.class);

            if (!timeInfo.contains("~")) {
                EventFavoriteParam timeParam = new EventFavoriteParam();
                timeParam.setParamType("timeInfo");
                timeParam.setParamValue(timeInfo);
                eventFavoriteParams.add(timeParam);
                eventFavorite.setBeginDate(new Date());
                eventFavorite.setEndDate(new Date());
            } else {
                String[] times = timeInfo.split("~");
                if (times.length != 2) {
                    return Response.error().message("日期格式不正确!").build();
                }
                Date beginDate = DateUtils.string2Date(times[0] + " 00:00:00",
                        "yyyy-MM-dd HH:mm:ss");
                Date endDate = DateUtils.string2Date(times[1] + " 00:00:00",
                        "yyyy-MM-dd HH:mm:ss");
                eventFavorite.setBeginDate(beginDate);
                eventFavorite.setEndDate(DateUtils.getEndDate(endDate));
            }

            String id = eventFavorite.getId();
            eventFavoriteService.saveEventFavorite(eventFavorite,
                    eventFavoriteParams, eventGroupIdList);

            // 业务日志埋点
            BizOperationLog bizLog = new BizOperationLog();
            if (StringUtils.isBlank(id) || !id.equalsIgnoreCase(eventFavorite.getId())) {
                bizLog.setLogType(BizOperationLog.LOG_TYPE_INSERT);
                bizLog.setBizCode("insert-eventFavorite");
                bizLog.setNewData(JSON.toJSONString(eventFavorite));
                bizLog.setDescription("新增" + eventFavorite.getFavoriteName());
            } else {
                bizLog.setLogType(BizOperationLog.LOG_TYPE_UPDATE);
                bizLog.setBizCode("update-eventFavorite");
                EventFavorite favorite = eventFavoriteService.findOne(id);
                bizLog.setOldData(JSON.toJSONString(favorite));
                bizLog.setNewData(JSON.toJSONString(eventFavorite));
                bizLog.setDescription("修改" + favorite.getFavoriteName());
            }
            bizLog.setBizName("事件管理");
            bizLog.setSubSystem("大数据管理");
            bizLog.setOperator(getLoginInfo().getRealName() + "("
                    + getLoginInfo().getUserName() + ")");
            bizLog.setOperationTime(new Date());
            BizOperationLogCollector.submitBizOperationLog(bizLog);

            return Response.ok().data(eventFavorite.getId()).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/eventLibrary")
    public String eventLibrary(ModelMap map, String favoriteName) {
        List<EventFavorite> eventFavorites = eventFavoriteService.findByUserId(getLoginInfo().getUserId(), favoriteName);
        map.put("eventFavorites", eventFavorites);
        return "/bigdata/extend/event/eventLibrary.ftl";
    }

    @RequestMapping("/deleteEventFavorite")
    @ResponseBody
    public Response deleteEventFavorite(String id) {
        eventFavoriteService.deleteEventFavorite(id);
        return Response.ok().build();
    }

    /**
     * 解析参数
     *
     * @param conditionList
     * @param conditionRelation
     * @param indexId
     * @param eventPropertyIds
     * @return
     */
    private List<EventFavoriteParam> getEventFavoriteParams(String conditionList, String conditionRelation, String indexId, String eventPropertyIds, String contrastTimeInterval) {
        List<EventFavoriteParam> eventFavoriteParams = Lists.newArrayList();
        EventFavoriteParam filterParam = new EventFavoriteParam();
        filterParam.setParamType("filter");
        filterParam.setParamValue(conditionList);
        eventFavoriteParams.add(filterParam);

        EventFavoriteParam indexParam = new EventFavoriteParam();
        indexParam.setParamType("index");
        indexParam.setParamValue(indexId);
        eventFavoriteParams.add(indexParam);

        EventFavoriteParam dimensionParam = new EventFavoriteParam();
        dimensionParam.setParamType("dimension");
        dimensionParam.setParamValue(eventPropertyIds);
        eventFavoriteParams.add(dimensionParam);

        EventFavoriteParam conditionRelationParam = new EventFavoriteParam();
        conditionRelationParam.setParamType("conditionRelation");
        conditionRelationParam.setParamValue(StringUtils
                .isNotBlank(conditionRelation) ? conditionRelation : "and");
        eventFavoriteParams.add(conditionRelationParam);

        EventFavoriteParam contrastTimeParam = new EventFavoriteParam();
        contrastTimeParam.setParamType("contrastTimeInterval");
        contrastTimeParam.setParamValue(contrastTimeInterval);
        eventFavoriteParams.add(contrastTimeParam);
        return eventFavoriteParams;
    }

    private List<JData.Entry> getChartData(String timeInterval,
                                           String interval, String eventId, String indexId,
                                           List<List<EventCondition>> conditions, String conditionRelation,
                                           List<EventProperty> properties, String chartType) throws BigDataBusinessException {
        // 查询数据
        List<Json> aggResultList = queryFromDruid(timeInterval, interval, eventId, indexId, conditions, conditionRelation, properties, chartType);
        List<JData.Entry> entryList = Lists.newArrayList();
        if ("pie".equals(chartType)) {
            aggResultList.forEach(e -> {
                JData.Entry entry = new JData.Entry();
                entry.setX(e.getString("serials"));
                entry.setY(e.getString("value"));
                entryList.add(entry);
            });
            return entryList;
        }
        aggResultList.forEach(e -> {
            JData.Entry entry = new JData.Entry();
            if (e.containsKey("date")
                    && StringUtils.isNotBlank(e.getString("date"))) {
                entry.setX(e.getString("date"));
                entry.setY(e.getString("value"));
                entry.setName(e.getString("serials"));
                entryList.add(entry);
            }
        });
        return entryList;
    }

    private List<Json> queryFromDruid(String timeInterval, String interval, String eventId, String indexId, List<List<EventCondition>> conditions, String conditionRelation, List<EventProperty> properties, String chartType) throws BigDataBusinessException {
        Event event = eventService.findOne(eventId);
        Json dateRangeParam = this.getDateRangeParam(timeInterval);

        DruidParam druidParam = new DruidParam();
        druidParam.setQueryType(DruidConstants.QUERY_TYPE_GROUP_BY);
        druidParam.setDataSource(event.getTableName());
        druidParam.setGranularity("pie".equals(chartType) ? DruidConstants.GRANULARITY_ALL : interval);

        // 维度
        List<String> dimensions = new ArrayList<>();
        properties.forEach(e -> dimensions.add(e.getFieldName()));
        druidParam.setDimensions(dimensions);

        // 条件过滤
        if (conditions.size() > 0) {
            Json filter = new Json();
            List<Json> filterList = Lists.newArrayList();

            conditions.forEach(e -> {

                Json filterItem = new Json();

                List<Json> childFilterList = Lists.newArrayList();
                e.forEach(c -> {
                    Json childFilterItem = new Json();
                    childFilterItem.put("type", c.getRuleSymbol());
                    EventProperty eventProperty = eventPropertyService
                            .findOne(c.eventPropertyId);
                    childFilterItem.put("dimension", eventProperty.getFieldName());
                    if ("in".equals(c.getRuleSymbol())) {
                        // 包含
                        childFilterItem.put("type", "search");
                        Json contain = new Json();
                        contain.put("type", "contains");
                        contain.put("value", c.getConditionValue());
                        childFilterItem.put("query", contain);
                    } else if ("regex".equals(c.getRuleSymbol())) {
                        // 正则匹配
                        childFilterItem.put("pattern", c.getConditionValue());
                    } else if ("regexNot".equals(c.getRuleSymbol())) {
                        // 正则不匹配
                        childFilterItem.put("type", "not");
                        Json regexNotFilter = new Json();
                        regexNotFilter.put("type", "regex");
                        regexNotFilter.put("pattern", c.getConditionValue());
                        regexNotFilter.put("dimension", eventProperty.getFieldName());
                        childFilterItem.put("field", regexNotFilter);
                    } else if ("not".equals(c.getRuleSymbol())) {
                        // 不等于
                        Json notFilter = new Json();
                        notFilter.put("type", "selector");
                        notFilter.put("dimension", eventProperty.getFieldName());
                        notFilter.put("value", c.getConditionValue());
                        childFilterItem.put("field", notFilter);
                    } else if ("notIn".equals(c.getRuleSymbol())) {
                        // 不包含
                        childFilterItem.put("type", "not");

                        Json notInFilter = new Json();
                        notInFilter.put("type", "search");
                        notInFilter.put("dimension", eventProperty.getFieldName());

                        Json contain = new Json();
                        contain.put("type", "contains");
                        contain.put("value", c.getConditionValue());

                        notInFilter.put("query", contain);
                        childFilterItem.put("field", notInFilter);
                    } else if ("upperStrict".equals(c.getRuleSymbol())) {
                        // 大于
                        childFilterItem.put("type", "bound");
                        childFilterItem.put("dimension", eventProperty.getFieldName());
                        childFilterItem.put("lower", c.getConditionValue());
                        childFilterItem.put("lowerStrict", true);
                        childFilterItem.put("ordering", "numeric");
                    } else if ("upper".equals(c.getRuleSymbol())) {
                        // 大于等于
                        childFilterItem.put("type", "bound");
                        childFilterItem.put("dimension", eventProperty.getFieldName());
                        childFilterItem.put("lower", c.getConditionValue());
                        childFilterItem.put("ordering", "numeric");
                    } else if ("lowerStrict".equals(c.getRuleSymbol())) {
                        // 小于
                        childFilterItem.put("type", "bound");
                        childFilterItem.put("dimension", eventProperty.getFieldName());
                        childFilterItem.put("upper", c.getConditionValue());
                        childFilterItem.put("upperStrict", true);
                        childFilterItem.put("ordering", "numeric");
                    } else if ("lower".equals(c.getRuleSymbol())) {
                        // 小于等于
                        childFilterItem.put("type", "bound");
                        childFilterItem.put("dimension", eventProperty.getFieldName());
                        childFilterItem.put("upper", c.getConditionValue());
                        childFilterItem.put("ordering", "numeric");
                    } else if ("isNull".equals(c.getRuleSymbol())) {
                        // 为空
                        childFilterItem.put("value", "");
                    } else if ("isNotNull".equals(c.getRuleSymbol())) {
                        childFilterItem.put("type", "not");
                        // 不为空
                        Json notNullFilter = new Json();
                        notNullFilter.put("type", "selector");
                        notNullFilter.put("dimension", eventProperty.getFieldName());
                        notNullFilter.put("value", "");
                        childFilterItem.put("field", notNullFilter);
                    } else {
                        // 等于
                        childFilterItem.put("value", c.getConditionValue());
                    }
                    childFilterList.add(childFilterItem);
                });
                filterItem.put("type", StringUtils.isNotBlank(conditionRelation) ? ("or".equals(conditionRelation) ? "and" : "or") : "and");
                filterItem.put("fields", childFilterList);
                filterList.add(filterItem);
            });

            filter.put("type", StringUtils.isNotBlank(conditionRelation) ? conditionRelation : "and");
            filter.put("fields", filterList);
            druidParam.setFilter(filter);
        }

        // 指标值 从指标表中获取
        EventIndicator eventIndicator = eventIndicatorService.findOne(indexId);
        List<DruidAggregationParam> aggregationParamList = Lists.newArrayList();

        DruidAggregationParam aggregationParam = new DruidAggregationParam();
        aggregationParam.setType(eventIndicator.getAggType());
        aggregationParam.setName(eventIndicator.getAggOutputName());
        if (!eventIndicator.getAggType().equals("count")) {
            aggregationParam.setFieldName(eventIndicator.getAggField());
        }
        aggregationParamList.add(aggregationParam);
        druidParam.setAggregations(aggregationParamList);

        List<String> intervals = new ArrayList<>();
        intervals.add(dateRangeParam.getString("intervals"));
        druidParam.setIntervals(intervals);

        List<Json> resultFieldList = new ArrayList<Json>();
        Json field1 = new Json();
        JSONArray keys = new JSONArray();
        properties.forEach(e -> keys.add(e.getFieldName()));
        field1.put("keys", keys);
        field1.put("resultField", eventIndicator.getAggOutputName());
        field1.put("resultDataType", "int");
        // 如果什么维度都不选的话 系列显示页面上选中的值
        if (keys.size() == 0)
            field1.put("serials", eventIndicator.getIndicatorName());
        resultFieldList.add(field1);

        return druidClientService.getDruidQueries(
                druidParam, resultFieldList);
    }

    /**
     * 获取开始时间和结束时间
     *
     * @param timeInterval
     * @return
     * @throws BigDataBusinessException
     */
    private Json getDateRangeParam(String timeInterval)
            throws BigDataBusinessException {
        Json dateRangeParam = new Json();

        DateTimeFormatter dtFormatter = DateTimeFormat
                .forPattern("yyyy-MM-dd HH:mm:ss");
        String compareInterval = EventCompareTimeEnum.timeMap().get(timeInterval);

        if (compareInterval == null) {
            compareInterval = EventTimeEnum.timeMap().get(timeInterval);
        }

        if (compareInterval != null) {
            timeInterval = compareInterval;
        }

        if (timeInterval.contains("最近")) {
            timeInterval = timeInterval.replaceAll("最近", "")
                    .replaceAll("周", "").replaceAll("天", "")
                    .replaceAll("月", "");
        } else {
            String[] times = timeInterval.split("~");
            if (times.length != 2) {
                throw new BigDataBusinessException("日期格式不正确");
            }
            dateRangeParam.put("start_date", times[0]);
            dateRangeParam.put("end_date", times[1]);
            // 日期加上8小时
            Date startDate = DateUtils.addHour(DateUtils.string2Date(times[0]), 8);
            Date endDate = DateUtils.addHour(DateUtils.addDay(DateUtils.string2Date(times[1]), 1), 8);

            DateTime startTime = dtFormatter.parseDateTime(DateUtils.date2String(startDate, "yyyy-MM-dd HH:mm:ss"));
            DateTime endTime = dtFormatter.parseDateTime(DateUtils.date2String(DateUtils.addSecond(endDate, -1), "yyyy-MM-dd HH:mm:ss"));

            String intervals = startTime + "/" + endTime;

            dateRangeParam.put("intervals", intervals);
            return dateRangeParam;
        }

        int days = Integer.parseInt(timeInterval);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1 - days);
        // 计算开始时间和结束时间
        // 日期加上8小时
        Date startDate = DateUtils.addHour(DateUtils.getStartDate(c.getTime()), 8);
        Date endDate = DateUtils.addHour(DateUtils.currentEndDate(), 8);

        DateTime startTime = dtFormatter.parseDateTime(DateUtils.date2String(startDate, "yyyy-MM-dd HH:mm:ss"));
        DateTime endTime = dtFormatter.parseDateTime(DateUtils.date2String(endDate, "yyyy-MM-dd HH:mm:ss"));
        dateRangeParam.put("start_date", startTime);
        dateRangeParam.put("end_date", endTime);
        String intervals = startTime + "/" + endTime;

        dateRangeParam.put("intervals", intervals);
        return dateRangeParam;

    }

    /**
     * 折线图
     *
     * @param entryList         数据
     * @param windowSize        窗口大小 1、中 2、大 3、动态窗口
     * @param isEdit            是否是编辑页面
     * @param contrastEntryList 对比数据
     * @return
     */
    private String getLineChartOption(List<JData.Entry> entryList, Integer windowSize, boolean isEdit, List<List<JData.Entry>> contrastEntryList) {

        if (windowSize == 3 && entryList.size() > 20) {
            entryList = entryList.subList(entryList.size() - 20, entryList.size());
        }

        JData data = new JData();
        data.setType(SeriesEnum.line);
        data.setCoordSys(CoordinateSystem.cartesian2d);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);

        Option option = new Option();

        JConverter.convert(data, option);

        if (contrastEntryList != null && contrastEntryList.size() > 0) {
            // 对比日期
            contrastEntryList.forEach(e -> this.compareData(e, data, option));
        }

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

        this.setOptionPosition(isEdit, option, tooltip);

        if (windowSize != 3) {
            setInterval(windowSize, option, contrastEntryList != null ? contrastEntryList.size() : 0);
        }

        if (isEdit) {
            addDataZoom(option);
        }

        option.series().forEach(e -> {
            Line line = (Line) e;
            line.symbol(SymbolEnum.circle);
            line.symbolSize("10");
        });

        return JSON.toJSON(option).toString();
    }

    private void setOptionPosition(boolean isEdit, Option option, Tooltip tooltip) {
        option.legend().show(true)
                .type(LegendEnum.scroll)
                .orient(Orient.horizontal);
        if (isEdit) {
            option.legend().bottom(BottomEx.create(30));
        } else
            option.legend().top(TopEnum.bottom);

        int gridBottom = isEdit ? 70 : 30;
        option.grid().forEach(
                e -> {
                    e.left(LeftEx.create(14)).right(RightEx.create(20))
                            .top(TopEx.create(40)).bottom(BottomEx.create(gridBottom))
                            .containLabel(true);
                });

        tooltip.trigger(Trigger.axis);
    }

    private String getBarChartOption(List<JData.Entry> entryList, Integer windowSize, boolean isEdit, List<List<JData.Entry>> contrastEntryList) {
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

        if (contrastEntryList != null && contrastEntryList.size() > 0) {
            // 对比日期
            contrastEntryList.forEach(e -> this.compareData(e, data, option));
        }

        for (Cartesian2dAxis dAxis : option.xAxis()) {
            dAxis.axisLabel().interval(0);
        }
        // 设置标题，提示框等
        option.title().show(true).left(LeftEnum.left);
        Tooltip tooltip = new Tooltip().option(option);
        tooltip.axisPointer().type(AxisPointerType.shadow);
        option.tooltip(tooltip);
        // 设置位置
        this.setOptionPosition(isEdit, option, tooltip);
        // 设置间隔
        this.setInterval(windowSize, option, contrastEntryList != null ? contrastEntryList.size() : 0);
        // 添加下拉条
        if (isEdit) {
            addDataZoom(option);
        }

        return JSON.toJSON(option).toString();
    }

    /**
     * 设置x轴间隔
     *
     * @param windowSize
     * @param option
     */
    private void setInterval(Integer windowSize, Option option, int compareSize) {
        if (option.xAxis().size() > 0) {
            int xNumber = option.xAxis().get(0).getData().size();
            if (xNumber > 10) {
                for (Cartesian2dAxis e : option.xAxis()) {
                    if (compareSize > 0) {
                        e.axisLabel().interval(windowSize == 2 ? (xNumber / 10 * compareSize) : (xNumber / 5 * compareSize));
                    } else {
                        e.axisLabel().interval(windowSize == 2 ? (xNumber / 10) : (xNumber / 5));
                    }
                }
            }
        }
    }

    /**
     * 添加下拉条
     *
     * @param option
     */
    private void addDataZoom(Option option) {
        SliderDataZoom zoom = new SliderDataZoom();
        zoom.setType(DataZoomType.slider);
        zoom.show(true).xAxisIndex(0);
        zoom.right(RightEx.create("5%")).left(LeftEx.create("5%"));
        zoom.bottom(BottomEx.create(0));
        option.setDataZoom(Lists.newArrayList(zoom));
    }

    /**
     * 对比日期
     *
     * @param contrastEntryList
     * @param data
     * @param option
     */
    private void compareData(List<JData.Entry> contrastEntryList, JData data, Option option) {
        if (contrastEntryList != null && contrastEntryList.size() > 0) {
            data.setEntryList(contrastEntryList);
            Option contrastOption = new Option();
            JConverter.convert(data, contrastOption);
            // 修改x轴
            LinkedHashSet<AxisData<Cartesian2dAxis>> axisData = option.xAxis().get(0).getData();
            LinkedHashSet<AxisData<Cartesian2dAxis>> contrastAxisData = contrastOption.xAxis().get(0).getData();
            ArrayList<AxisData<Cartesian2dAxis>> axisDataArrayList = Lists.newArrayList(contrastAxisData);

//            if (axisData.size() < contrastAxisData.size()) {
//                // x轴扩充
//                int j = contrastAxisData.size() - axisData.size();
//                axisData.addAll(axisDataArrayList.subList(j, contrastAxisData.size()));
//            }

            int i = 0;
            for (AxisData<Cartesian2dAxis> axisAxisData : axisData) {
                if (i < axisDataArrayList.size()) {
                    axisAxisData.setValue(axisAxisData.getValue() + "/" + axisDataArrayList.get(i++).getValue());
                }
            }

            String[] color = new String[]{"#4876FF", "#77DCC1", "#F29D57",
                    "#F4CF51", "#7A8DE9", "#63C797", "#F7C789", "#73d13d",
                    "#ffec3d", "#ffa940", "#ff4d4f", "#9254de"};

            String[] contrastColor = new String[]{"#6495ED", "#C9F1E6", "#FAD8BC",
                    "#FBECBA", "#CAD2F2", "#C1E9D6", "#FCE9D0", "#73F43D",
                    "#FFD33D", "#FFDE40", "#FF6F4F", "#927ADE"};

            Map<String, String> colorMap = Maps.newHashMap();

            int c = 0;
            for (Series e : option.series()) {
                ItemStyle itemStyle = new ItemStyle();
                itemStyle.color(color[c % color.length]);
                colorMap.put(e.getName(), String.valueOf(c));
                c++;
                e.itemStyle(itemStyle);
            }

            int cc = 0;
            for (Series e : contrastOption.series()) {
                ItemStyle itemStyle = new ItemStyle();
                if (colorMap.get(e.getName()) != null) {
                    itemStyle.color(contrastColor[Integer.valueOf(colorMap.get(e.getName())) % contrastColor.length]);
                } else {
                    itemStyle.color(contrastColor[cc % contrastColor.length]);
                }
                cc++;
                e.itemStyle(itemStyle);
            }

            // 合并series
            option.series().addAll(contrastOption.series());
            option.series().sort(Comparator.comparing(Series::getName));
        }
    }

    private String getPieChartOption(List<JData.Entry> entryList, Integer windowSize, List<List<JData.Entry>> contrastEntryList) {
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
        data.setRoseType(RoseTypeEnum.radius);

        Option option = new Option();
        JConverter.convert(data, option);

        // 设置标题，提示框等
        option.title().show(true);
        Tooltip tooltip = new Tooltip().option(option);
        option.tooltip(tooltip);
        option.legend().show(true)
                .type(LegendEnum.scroll).top(TopEnum.bottom)
                .orient(Orient.horizontal);

        option.grid().forEach(
                e -> e.left(LeftEx.create(14)).right(RightEx.create(20))
                        .top(TopEx.create(40)).bottom(BottomEx.create(40))
                        .containLabel(true)
        );
        tooltip.trigger(Trigger.item);
        return JSON.toJSON(option).toString();
    }

    @RequestMapping("/getReport")
    public String getReport(String eventId, String indexId, String conditionList, String timeInterval,
                            String chartType, String eventPropertyIds, String timeUnit, String conditionRelation,
                            ModelMap map, @RequestParam(value = "contrastTimeInterval[]", required = false) String[] contrastTimeInterval) {
        map.put("contrastTimeInterval", contrastTimeInterval);
        map.put("timeInterval", timeInterval);
        map.put("pageSize", countSize(eventId, indexId, conditionList, chartType, eventPropertyIds, timeUnit, timeInterval, conditionRelation, map));
        return "/bigdata/extend/event/eventReport.ftl";
    }

    public Integer countSize(String eventId, String indexId, String conditionList,
                             String chartType, String eventPropertyIds, String timeUnit,
                             String timeInterval, String conditionRelation, ModelMap map) {

        try {
            if ("pie".equals(chartType)) {
                return 0;
            }

            List<List<EventCondition>> conditions = JSON.parseObject(conditionList, new TypeReference<List<List<EventCondition>>>() {
            });
            List<EventProperty> properties = eventPropertyService.findListByIdIn(Json.parseArray(eventPropertyIds).toArray(new String[]{}));
            List<Json> result = queryFromDruid(timeInterval, timeUnit, eventId, indexId, conditions, conditionRelation, properties, chartType);
            Set<String> dateSet = Sets.newHashSet();

            for (Json json : result) {
                String date = json.getString("date");
                if (!dateSet.contains(date)) {
                    dateSet.add(date);
                }
            }

            if (dateSet.size() > 16) {
                map.put("isShowExport", true);
                return 16;
            }
            return dateSet.size();
        } catch (BigDataBusinessException e) {
            return 0;
        }
    }

    @RequestMapping("/getReportData")
    public String getReportData(String eventId, String indexId,
                                String conditionList, String chartType, String eventPropertyIds,
                                String timeUnit, String timeInterval, String selectInterval, String conditionRelation, ModelMap map,
                                Boolean isHide, @RequestParam(name = "pageSize", defaultValue = "0", required = false) Integer pageSize) {
        try {
            if (StringUtils.isBlank(eventId) || StringUtils.isBlank(indexId)) {
                return "";
            }

            Map<String, String> valueMap = Maps.newHashMap();
            Set<String> indexSet = Sets.newHashSet();
            List<String> indexList = Lists.newArrayList();
            List<String> dateList = Lists.newArrayList();

            if (StringUtils.isBlank(selectInterval)) {
                selectInterval = timeInterval;
            }

            this.constructExportData(eventId, indexId, conditionList, chartType, eventPropertyIds, timeUnit, selectInterval, conditionRelation, valueMap, indexSet, indexList, dateList);

            String[] indexArray = indexList.toArray(new String[]{});
            if (dateList.size() > pageSize) {
                dateList = dateList.subList(0, pageSize);
            }
            String[] dateArray = dateList.toArray(new String[]{});
            map.put("valueMap", valueMap);
            map.put("indexArray", indexArray);
            map.put("dateArray", dateArray);
            map.put("selectInterval", selectInterval.replace("~", ""));
            map.put("isHide", isHide);
            return "/bigdata/extend/event/eventReportTable.ftl";
        } catch (BigDataBusinessException e) {
            log.error(e.getMessage());
            return "/bigdata/extend/event/eventReportTable.ftl";
        }
    }

    @RequestMapping("/exportReport")
    public void exportReport(String eventId, String indexId,
                             String conditionList, String chartType, String eventPropertyIds,
                             String timeUnit, String timeInterval, String conditionRelation,
                             HttpServletResponse response,
                             String contrastTimeInterval) {

        doExport(eventId, indexId, conditionList, chartType, eventPropertyIds, timeUnit, timeInterval, conditionRelation, contrastTimeInterval, response);

    }

    private List<Map<String, String>> getSheetRecord(String eventId, String indexId,
                                                     String conditionList, String chartType, String eventPropertyIds,
                                                     String timeUnit, String timeInterval, String conditionRelation, List<String> indexList) {
        try {
            Map<String, String> valueMap = Maps.newHashMap();
            Set<String> indexSet = Sets.newHashSet();
            List<String> dateList = Lists.newArrayList();
            this.constructExportData(eventId, indexId, conditionList, chartType, eventPropertyIds, timeUnit, timeInterval, conditionRelation, valueMap, indexSet, indexList, dateList);

            List<Map<String, String>> recordList = Lists.newArrayList();
            dateList.add(0, "总计");
            for (String date : dateList) {
                Map<String, String> sMap = Maps.newHashMap();
                sMap.put("指标", date);
                for (String index : indexList) {
                    sMap.put(index, valueMap.getOrDefault(date + index, "0"));
                }
                recordList.add(sMap);
            }
            return recordList;
        } catch (BigDataBusinessException e) {
            log.error(e.getMessage());
            return Lists.newArrayList();
        }
    }

    private void doExport(String eventId, String indexId,
                          String conditionList, String chartType, String eventPropertyIds,
                          String timeUnit, String timeInterval, String conditionRelation,
                          String contrastTimeInterval, HttpServletResponse response) {

        Map<String, List<Map<String, String>>> sheetName2RecordListMap = Maps
                .newLinkedHashMap();
        Map<String, List<String>> titleMap = Maps.newHashMap();


        this.constructSheetMap(eventId, indexId, conditionList, chartType, eventPropertyIds, timeUnit, conditionRelation, sheetName2RecordListMap, titleMap, timeInterval);

        String[] contrastTime = null;
        if (StringUtils.isNotBlank(contrastTimeInterval)) {
            contrastTime = JSON.parseObject(contrastTimeInterval, String[].class);
        }

        if (contrastTime != null && contrastTime.length > 0) {
            for (String interval : contrastTime) {
                this.constructSheetMap(eventId, indexId, conditionList, chartType, eventPropertyIds, timeUnit, conditionRelation, sheetName2RecordListMap, titleMap, interval);
            }
        }

        ExportUtils ex = ExportUtils.newInstance();
        ex.exportXLSFile("事件分析导出", titleMap, sheetName2RecordListMap, response);
    }

    private void constructSheetMap(String eventId, String indexId, String conditionList, String chartType, String eventPropertyIds, String timeUnit, String conditionRelation, Map<String, List<Map<String, String>>> sheetName2RecordListMap, Map<String, List<String>> titleMap, String interval) {
        List<String> indexL = Lists.newArrayList();
        sheetName2RecordListMap.put(interval, getSheetRecord(eventId, indexId, conditionList, chartType, eventPropertyIds, timeUnit,
                interval, conditionRelation, indexL));
        indexL.add(0, "指标");
        titleMap.put(interval, indexL);
    }

    @RequestMapping("/exportReportById")
    public void exportReportById(String favoriteId, HttpServletResponse response) {
        EventFavorite eventFavorite = eventFavoriteService.findOne(favoriteId);

        Map<String, String> paramMap = eventFavoriteParamService
                .getMapByFavoriteId(favoriteId);

        String timeInterval = paramMap.get("timeInfo");
        if (StringUtils.isBlank(timeInterval)) {
            String beginDate = DateUtils.date2String(eventFavorite.getBeginDate(),
                    "yyyy-MM-dd");
            String endDate = DateUtils.date2String(eventFavorite.getEndDate(),
                    "yyyy-MM-dd");
            timeInterval = beginDate + "~" + endDate;
        }
        String timeUnit = eventFavorite.getGranularity();

        String contrastTimeInterval = paramMap.get("contrastTimeInterval");

        doExport(eventFavorite.getEventId(), paramMap.get("index"), paramMap.get("filter"), eventFavorite.getChartType(), paramMap.get("dimension"),
                timeUnit, timeInterval, paramMap.get("conditionRelation"), contrastTimeInterval, response);

    }

    private void constructExportData(String eventId, String indexId, String conditionList,
                                     String chartType, String eventPropertyIds, String timeUnit,
                                     String timeInterval, String conditionRelation,
                                     Map<String, String> valueMap, Set<String> indexSet,
                                     List<String> indexList, List<String> dateList) throws BigDataBusinessException {

        List<List<EventCondition>> conditions = JSON.parseObject(conditionList, new TypeReference<List<List<EventCondition>>>() {
        });
        List<EventProperty> properties = eventPropertyService.findListByIdIn(Json.parseArray(eventPropertyIds).toArray(new String[]{}));

        List<Json> result = queryFromDruid(timeInterval, timeUnit, eventId, indexId, conditions, conditionRelation, properties, chartType);
        Set<String> dateSet = Sets.newHashSet();

        long total = 0l;
        for (Json json : result) {
            String date = json.getString("date");
            String value = json.getString("value");
            String index = json.getString("serials");

            if (value != null) {
                total = total + Long.valueOf(value);
            }

            valueMap.put(date + index, value);
            if (!indexSet.contains(index)) {
                indexSet.add(index);
                indexList.add(index);
            }

            if (!dateSet.contains(date)) {
                dateSet.add(date);
                dateList.add(date);
            }
        }

        for (String index : indexList) {
            long t = 0l;
            for (String date : dateList) {
                String v = valueMap.get(date + index);
                if (StringUtils.isNotBlank(v)) {
                    t = t + Long.valueOf(v);
                }
            }
            valueMap.put("总计" + index, String.valueOf(t));
        }

    }

    public static class EventCondition {
        private String eventPropertyId;
        private String ruleSymbol;
        private String conditionValue;

        public String getEventPropertyId() {
            return eventPropertyId;
        }

        public void setEventPropertyId(String eventPropertyId) {
            this.eventPropertyId = eventPropertyId;
        }

        public String getRuleSymbol() {
            return ruleSymbol;
        }

        public void setRuleSymbol(String ruleSymbol) {
            this.ruleSymbol = ruleSymbol;
        }

        public String getConditionValue() {
            return conditionValue;
        }

        public void setConditionValue(String conditionValue) {
            this.conditionValue = conditionValue;
        }
    }
}
