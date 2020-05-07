package net.zdsoft.api.monitor.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Function;

import javax.annotation.Resource;

import net.zdsoft.api.base.dto.ApiCensusDto;
import net.zdsoft.api.base.entity.eis.ApiCensusCount;
import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.entity.eis.ApiInterfaceCount;
import net.zdsoft.api.base.enums.CensusTypeEnum;
import net.zdsoft.api.base.service.ApiCensusCountService;
import net.zdsoft.api.base.service.ApiDeveloperService;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.api.base.service.ApiInterfaceTypeService;
import net.zdsoft.api.monitor.constant.ApiConstant;
import net.zdsoft.api.monitor.service.ApiCensusJobService;
import net.zdsoft.api.monitor.vo.Census;
import net.zdsoft.api.monitor.vo.InterfaceMonitorDto;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.frame.data.druid.DruidAggregationParam;
import net.zdsoft.bigdata.frame.data.druid.DruidClientService;
import net.zdsoft.bigdata.frame.data.druid.DruidConstants;
import net.zdsoft.bigdata.frame.data.druid.DruidParam;
import net.zdsoft.bigdata.frame.data.redis.BgRedisUtils;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;

import org.apache.commons.collections.CollectionUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;

@Controller
@RequestMapping("/bigdata/monitor/api")
public class MonitorAction {
    @Autowired
    private ApiCensusJobService apiService;
    @Autowired
    private ApiCensusCountService apiCensusCountService;
    @Autowired
    private ApiDeveloperService apiDeveloperService;
    @Autowired
    private ApiInterfaceService apiInterfaceService;
    @Autowired
    private ApiInterfaceTypeService apiInterfaceTypeService;
    @Resource
    private DruidClientService druidClientService;

    @RequestMapping("/index")
    public String interfaceManageIndex(ModelMap map) throws JSONException {
        Date dayFirst = DateUtils.getStartDate(new Date());
        Date dayEnd = DateUtils.currentEndDate();
        List<ApiCensusCount> allDeveloperCounts = apiCensusCountService.findByDate(dayFirst, dayEnd);
        if (CollectionUtils.isEmpty(allDeveloperCounts)) {
            apiService.doCensus();
            allDeveloperCounts = apiCensusCountService.findByDate(dayFirst, dayEnd);
        }
        Map<String, List<ApiCensusCount>> keyMap = EntityUtils.getListMap(allDeveloperCounts, ApiCensusCount::getKey, Function.identity());
        map.put("all", keyMap.get(CensusTypeEnum.ALL.getName()).get(0));
        List<ApiCensusDto> developerDtos = new ArrayList<ApiCensusDto>();
        List<ApiCensusCount> developerCounts = keyMap.get(CensusTypeEnum.DEVELOPER.getName());
        if (CollectionUtils.isNotEmpty(developerCounts)) {
            Set<String> ticketSet = EntityUtils.getSet(developerCounts, ApiCensusCount::getValue);
            List<ApiDeveloper> allDevelopers = apiDeveloperService.findByTicketKeyIn(ticketSet.toArray(new String[ticketSet.size()]));
            Map<String, String> tickeyMap = EntityUtils.getMap(allDevelopers, ApiDeveloper::getTicketKey, ApiDeveloper::getUnitName);
            developerCounts.forEach(c -> {
                ApiCensusDto dto = new ApiCensusDto();
                dto.setKey("developer");
                dto.setValue(c.getValue());
                dto.setCensusCount(c);
                dto.setDeveloperName(tickeyMap.get(c.getValue()));
                List<String> typeList = new ArrayList<>();
                List<Integer> findCountList = new ArrayList<>();
                List<Integer> findNumList = new ArrayList<>();
                List<Integer> saveCountList = new ArrayList<>();
                List<Integer> saveNumList = new ArrayList<>();
                String allTypeCensus = c.getAllCensus();
                List<Census> allCensus = JSON.parseArray(allTypeCensus, Census.class);
                allCensus.forEach(t -> {
                    typeList.add(t.getResultType());
                    findCountList.add(t.gettFindCount());
                    findNumList.add(t.gettFindNum());
                    saveCountList.add(t.gettSaveCount());
                    saveNumList.add(t.gettSaveNum());
                });
                dto.setTypeArray(typeList.toArray(new String[typeList.size()]));
                dto.setFindCountArray(findCountList.toArray(new Integer[findCountList.size()]));
                dto.setFindNumArray(findNumList.toArray(new Integer[findNumList.size()]));
                dto.setSaveCountArray(saveCountList.toArray(new Integer[saveCountList.size()]));
                dto.setSaveNumArray(saveNumList.toArray(new Integer[saveNumList.size()]));
                developerDtos.add(dto);
            });
        }
        List<ApiCensusCount> typeCounts = keyMap.get(CensusTypeEnum.APITYPE.getName());
        if (CollectionUtils.isNotEmpty(typeCounts)) {
            ApiCensusDto mouthDto = getTypeCensus(typeCounts, "mouth");
            ApiCensusDto weekDto = getTypeCensus(typeCounts, "week");
            ApiCensusDto dayDto = getTypeCensus(typeCounts, "day");
            map.put("mouthDto", mouthDto);
            map.put("weekDto", weekDto);
            map.put("dayDto", dayDto);
        }

        map.put("developers", developerDtos);
        map.put("all", keyMap.get(CensusTypeEnum.ALL.getName()).get(0));
        return "/api/monitor/index.ftl";
    }

    @RequestMapping("/interface/countList")
    public String interfaceCountList(ModelMap map, String resultType, String showType) throws JSONException {
        List<InterfaceMonitorDto> dtos = new ArrayList<InterfaceMonitorDto>();
        Date dayFirst = DateUtils.getStartDate(new Date());
        Date dayEnd = DateUtils.currentEndDate();
        List<ApiCensusCount> typeCounts = apiCensusCountService.getInterfaceCounts(CensusTypeEnum.APITYPE.getName(), resultType, dayFirst, dayEnd);
        if (CollectionUtils.isNotEmpty(typeCounts)) {
            ApiCensusCount c = typeCounts.get(0);
            String apiCensus = null;
            if ("mouth".equals(showType)) {
                apiCensus = c.getMouthApiCensus();
            } else if ("week".equals(showType)) {
                apiCensus = c.getWeekApiCensus();
            } else if ("day".equals(showType)) {
                apiCensus = c.getDayApiCensus();
            }
            List<Census> census = JSON.parseArray(apiCensus, Census.class);
            Set<String> interfaceIdSet = EntityUtils.getSet(census, Census::getInterfaceId);
            if (CollectionUtils.isNotEmpty(interfaceIdSet)) {
                List<ApiInterface> allInterfaces = apiInterfaceService.findListByIdIn(interfaceIdSet.toArray(new String[interfaceIdSet.size()]));
                Map<String, String> descriptMap = EntityUtils.getMap(allInterfaces, ApiInterface::getId, ApiInterface::getDescription);
                census.forEach(t -> {
                    InterfaceMonitorDto dto = new InterfaceMonitorDto();
                    dto.setDescription(descriptMap.get(t.getInterfaceId()));
                    dto.setSaveCount(t.getiSaveCount());
                    dto.setSaveNum(t.getiSaveNum());
                    dto.setFindCount(t.getiFindCount());
                    dto.setFindNum(t.getiFindNum());
                    dtos.add(dto);
                });
            }
        }
        map.put("interfaceMonitorDtos", dtos);
        return "/api/monitor/apiCountList.ftl";
    }

    @RequestMapping("/realTime/count")
    public String realTimeCountList(ModelMap map) throws JSONException {
        //实时警告
        List<String> warningList = BgRedisUtils.lrange(ApiConstant.API_CALL_WARNING_KEY);
        List<ApiInterfaceCount> warning = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(warningList)) {
            warningList.forEach(c -> {
                ApiInterfaceCount count = JSON.parseObject(c, ApiInterfaceCount.class);
                warning.add(count);
            });
        }
        //实时调用
        List<String> detailList = BgRedisUtils.lrange(ApiConstant.API_CALL_DETAIL_KEY);
        List<ApiInterfaceCount> detail = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(detailList)) {
            Set<String> ticketKeySet = new HashSet<String>();
            detailList.forEach(c -> {
                ApiInterfaceCount count = JSON.parseObject(c, ApiInterfaceCount.class);
                ticketKeySet.add(count.getTicketKey());
                detail.add(count);
            });
            List<ApiDeveloper> developers = apiDeveloperService.findByTicketKeyIn(ticketKeySet.toArray(new String[ticketKeySet.size()]));
            Map<String, String> unitNameMap = EntityUtils.getMap(developers, ApiDeveloper::getTicketKey, ApiDeveloper::getUnitName);
            detail.forEach(c -> {
                c.setDeveloperName(unitNameMap.get(c.getTicketKey()));
            });
        }
        map.put("warning", warning);
        map.put("detail", detail);
        return "/api/monitor/realTime.ftl";
    }

    @ResponseBody
    @RequestMapping("/realTime/census")
    public Response realTimeCensusList(String status) throws JSONException {
        DruidParam druidParam = new DruidParam();
        int interval = 30;
        if ("append".equals(status))
            interval = 5;
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        // CST(北京时间)在东8区
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String startTime = sdf.format(DateUtils.addMinute(currentDate, -interval));
        String endTime = sdf.format(currentDate);

        String intervals = startTime + "/" + endTime;
//        intervals="2000-01-01T00:00Z/3000-01-01T00:00Z";
        List<String> intervalList = new ArrayList<>();
        intervalList.add(intervals);
        druidParam.setIntervals(intervalList);


        druidParam.setQueryType(DruidConstants.QUERY_TYPE_GROUP_BY);
        druidParam.setDataSource("api_monitor_by_minute");
        druidParam.setGranularity(DruidConstants.GRANULARITY_MINUTE);

        // 维度
        List<String> dimensions = new ArrayList<>();
        dimensions.add("dataStatus");
        druidParam.setDimensions(dimensions);

        // 条件过滤
        Json filter = new Json();
        filter.put("type", "selector");
        filter.put("dimension", "sequenceId");
        filter.put("value", 1);
        druidParam.setFilter(filter);

        // 指标值 从指标表中获取
        List<DruidAggregationParam> aggregationParamList = Lists.newArrayList();
        DruidAggregationParam aggregationParam = new DruidAggregationParam();
        aggregationParam.setType("longSum");
        aggregationParam.setName("times");
        aggregationParam.setFieldName("times");
        aggregationParamList.add(aggregationParam);
        druidParam.setAggregations(aggregationParamList);

        List<Json> resultFieldList = new ArrayList<Json>();
        JSONArray keys = new JSONArray();
        keys.add("dataStatus");
        Json field = new Json();
        field.put("keys", keys);
        field.put("resultField", "times");
        field.put("resultDataType", "int");
        resultFieldList.add(field);

        List<Json> aggResultList = druidClientService.getDruidQueries(
                druidParam, resultFieldList);
        List<String> datelist = new LinkedList<>();
        Map<String, Json> resultMap = new HashMap<>();
        for (Json json : aggResultList) {
            if (!datelist.contains(json.getString("date")))
                datelist.add(json.getString("date"));
            Json result = resultMap.get(json.getString("date"));
            if (result == null)
                result = new Json();
            if ("推送".equals(json.getString("key"))) {
                result.put("tuisong", json.getIntValue("value"));
            }
            if ("拉取".equals(json.getString("key"))) {
                result.put("laqu", json.getIntValue("value"));
            }
            resultMap.put(json.getString("date"), result);
        }
        List<Json> resultlist = new LinkedList<>();
        for (String d : datelist) {
            Json result = resultMap.get(d);
            result.put("shijian", d);
            resultlist.add(result);
        }
        return Response.ok().data(JSON.toJSONString(resultlist)).build();
    }

    // --------------------------------------私有方法 --------------------------
    private ApiCensusDto getTypeCensus(List<ApiCensusCount> typeCounts, String showType) {
        ApiCensusDto dto = new ApiCensusDto();
        dto.setKey(CensusTypeEnum.APITYPE.getName());
        List<String> typeList = new ArrayList<>();
        List<Integer> findCountList = new ArrayList<>();
        List<Integer> findNumList = new ArrayList<>();
        List<Integer> saveCountList = new ArrayList<>();
        List<Integer> saveNumList = new ArrayList<>();
        typeCounts.forEach(c -> {
            String tCensus = null;
            if ("mouth".equals(showType)) {
                tCensus = c.getMouthCensus();
            } else if ("week".equals(showType)) {
                tCensus = c.getWeekCensus();
            } else if ("day".equals(showType)) {
                tCensus = c.getDayCensus();
            }
            List<Census> census = JSON.parseArray(tCensus, Census.class);
            if (CollectionUtils.isNotEmpty(census)) {
                typeList.add(c.getValue());
            }
            census.forEach(t -> {
                findCountList.add(t.gettFindCount());
                findNumList.add(t.gettFindNum());
                saveCountList.add(t.gettSaveCount());
                saveNumList.add(t.gettSaveNum());
            });
        });
        dto.setTypeArray(typeList.toArray(new String[typeList.size()]));
        dto.setFindCountArray(findCountList.toArray(new Integer[findCountList.size()]));
        dto.setFindNumArray(findNumList.toArray(new Integer[findNumList.size()]));
        dto.setSaveCountArray(saveCountList.toArray(new Integer[saveCountList.size()]));
        dto.setSaveNumArray(saveNumList.toArray(new Integer[saveNumList.size()]));
        return dto;
    }

}
