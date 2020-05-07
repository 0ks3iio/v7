package net.zdsoft.bigdata.extend.data.action;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.entity.Event;
import net.zdsoft.bigdata.extend.data.entity.EventIndicator;
import net.zdsoft.bigdata.extend.data.entity.EventProperty;
import net.zdsoft.bigdata.extend.data.entity.EventType;
import net.zdsoft.bigdata.extend.data.service.EventIndicatorService;
import net.zdsoft.bigdata.extend.data.service.EventPropertyService;
import net.zdsoft.bigdata.extend.data.service.EventService;
import net.zdsoft.bigdata.extend.data.service.EventTypeService;
import net.zdsoft.bigdata.frame.data.druid.DruidClientService;
import net.zdsoft.bigdata.frame.data.kafka.KafkaUtils;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

/**
 * Created by wangdongdong on 2018/12/24 10:03.
 */
@Controller
@RequestMapping("/bigdata/eventMaintain")
public class EventMaintainController extends BigdataBaseAction {

    private static Logger logger = LoggerFactory
            .getLogger(EventMaintainController.class);

    @Resource
    private EventTypeService eventTypeService;
    @Resource
    private EventService eventService;
    @Resource
    private EventPropertyService eventPropertyService;
    @Resource
    private EventIndicatorService eventIndicatorService;
    @Resource
    private DruidClientService druidClientService;

    @RequestMapping("/eventTypeList")
    public String eventTypeList(ModelMap model) {
        List<EventType> eventTypes = eventTypeService
                .findAllByUnitId(getLoginInfo().getUnitId());
        model.put("eventTypes", eventTypes);
        return "/bigdata/extend/event/maintain/eventTypeList.ftl";
    }

    @RequestMapping("/eventTypeEdit")
    public String eventTypeEdit(String id, ModelMap model) {
        EventType eventType = StringUtils.isNotBlank(id) ? eventTypeService
                .findOne(id) : new EventType();
        model.put("eventType", eventType);
        return "/bigdata/extend/event/maintain/eventTypeEdit.ftl";
    }

    @RequestMapping("/saveEventType")
    @ResponseBody
    public Response saveEventType(EventType eventType) {
        eventType.setUnitId(getLoginInfo().getUnitId());
        try {
            eventTypeService.saveEventType(eventType);
            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/deleteEventType")
    @ResponseBody
    public Response deleteEventType(String id) {
        try {
            eventTypeService.deleteEventType(id);
            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/eventList")
    public String eventList(String typeId, ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        Map<String, EventType> eventTypeMap = eventTypeService
                .findMapByUnitId(unitId);
        List<Event> eventList;
        if (StringUtils.isBlank(typeId)) {
            eventList = eventService.findAll(getLoginInfo().getUnitId());
        } else {
            eventList = eventService.findAllByTypeId(typeId);
        }
        eventList.forEach(e -> e.setEventTypeName(eventTypeMap.get(
                e.getTypeId()) != null ? eventTypeMap.get(
                e.getTypeId()).getTypeName() : ""));
        map.put("eventList", eventList);
        map.put("eventTypes", eventTypeService.findAllByUnitId(unitId));
        map.put("typeId", typeId);
        return "/bigdata/extend/event/maintain/eventList.ftl";
    }

    @RequestMapping("/eventEdit")
    public String eventEdit(String id, ModelMap model) {
        Event event = StringUtils.isNotBlank(id) ? eventService.findOne(id)
                : new Event();
        model.put("event", event);
        model.put("eventTypes",
                eventTypeService.findAllByUnitId(getLoginInfo().getUnitId()));
        return "/bigdata/extend/event/maintain/eventEdit.ftl";
    }

    @RequestMapping("/saveEvent")
    @ResponseBody
    public Response saveEvent(Event event) {
        try {
            event.setUnitId(getLoginInfo().getUnitId());
            event.setIsCustom(1);
            if (event.getEnvProperty() == null) {
                event.setEnvProperty((short) 0);
            }
            if (event.getUserProperty() == null) {
                event.setUserProperty((short) 0);
            }
            if (event.getTimeProperty() == null) {
                event.setTimeProperty((short) 0);
            }
            eventService.saveEvent(event);
            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/deleteEvent")
    @ResponseBody
    public Response deleteEvent(String id) {
        try {
            eventService.deleteEvent(id);
            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/eventPropertyList")
    public String eventPropertyList(
            @RequestParam(value = "typeId", required = false) String eventId,
            ModelMap map, HttpServletRequest request) {
        String unitId = getLoginInfo().getUnitId();
        List<EventProperty> eventPropertyList;
        Pagination page = createPagination(request);
        eventPropertyList = eventPropertyService.findByPage(page, eventId,
                unitId);
        sendPagination(request, map,null, page);
        Map<String, EventType> eventTypeMap = eventTypeService
                .findMapByUnitId(unitId);
        List<Event> events = eventService.findAll(unitId);
        Map<String, List<Event>> eventMap = events.stream().collect(
                Collectors.groupingBy(Event::getTypeId, Collectors.toList()));
        Map<String, String> eventNameMap = events.stream().collect(
                Collectors.toMap(Event::getId, Event::getEventName));
        eventNameMap.put("0000000000000000000000000000user", "用户");
        eventNameMap.put("00000000000000000000000000000env", "系统");
        eventNameMap.put("0000000000000000000000000000time", "时间");
        eventPropertyList.forEach(eventProperty -> eventProperty
                .setEventName(eventNameMap.get(eventProperty.getEventId())));
        map.put("eventPropertyList", eventPropertyList);
        map.put("eventTypeMap", eventTypeMap);
        map.put("eventMap", eventMap);
        map.put("eventId", eventId);
        map.put("sum", page.getMaxRowCount());
        return "/bigdata/extend/event/maintain/eventPropertyList.ftl";
    }

    @RequestMapping("/eventPropertyEdit")
    public String eventPropertyEdit(String id, ModelMap model) {
        EventProperty eventProperty = StringUtils.isNotBlank(id) ? eventPropertyService
                .findOne(id) : new EventProperty();
        String unitId = getLoginInfo().getUnitId();
        Map<String, EventType> eventTypeMap = eventTypeService
                .findMapByUnitId(unitId);
        List<Event> events = eventService.findAll(unitId);
        Map<String, List<Event>> eventMap = events.stream().collect(
                Collectors.groupingBy(Event::getTypeId, Collectors.toList()));
        model.put("eventProperty", eventProperty);
        model.put("events", events);
        model.put("eventTypeMap", eventTypeMap);
        model.put("eventMap", eventMap);
        return "/bigdata/extend/event/maintain/eventPropertyEdit.ftl";
    }

    @RequestMapping("/saveEventProperty")
    @ResponseBody
    public Response saveEventProperty(EventProperty eventProperty) {
        try {
            eventProperty.setUnitId(getLoginInfo().getUnitId());
            eventPropertyService.saveEventProperty(eventProperty);
            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/deleteEventProperty")
    @ResponseBody
    public Response deleteEventProperty(String id) {
        eventPropertyService.delete(id);
        return Response.ok().build();
    }

    @RequestMapping("/changeEventPropertyStatu")
    @ResponseBody
    public Response changeEventPropertyStatu(String id, Short status) {
        EventProperty eventProperty = eventPropertyService.findOne(id);
        eventProperty.setIsShowChart(status);
        eventPropertyService.update(eventProperty, id,
                new String[]{"isShowChart"});
        return Response.ok().build();
    }

    @RequestMapping("/eventIndexList")
    public String eventIndexList(
            @RequestParam(value = "typeId", required = false) String eventId,
            ModelMap model) {
        String unitId = getLoginInfo().getUnitId();
        List<EventIndicator> eventIndicatorList = StringUtils
                .isNotBlank(eventId) ? eventIndicatorService
                .findIndicatorListByEventId(eventId) : eventIndicatorService
                .findByUnitId(unitId);
        Map<String, EventType> eventTypeMap = eventTypeService
                .findMapByUnitId(unitId);
        List<Event> events = eventService.findAll(unitId);
        Map<String, List<Event>> eventMap = events.stream().collect(
                Collectors.groupingBy(Event::getTypeId, Collectors.toList()));
        Map<String, String> eventNameMap = events.stream().collect(
                Collectors.toMap(Event::getId, Event::getEventName));
        eventIndicatorList.forEach(e -> e.setEventName(eventNameMap.get(e
                .getEventId())));
        model.put("eventTypeMap", eventTypeMap);
        model.put("eventMap", eventMap);
        model.put("eventIndicatorList", eventIndicatorList);
        model.put("eventId", eventId);
        return "/bigdata/extend/event/maintain/eventIndexList.ftl";
    }

    @RequestMapping("/eventIndexEdit")
    public String eventIndexEdit(String id, ModelMap model) {
        EventIndicator eventIndicator = StringUtils.isNotBlank(id) ? eventIndicatorService
                .findOne(id) : new EventIndicator();
        String unitId = getLoginInfo().getUnitId();
        Map<String, EventType> eventTypeMap = eventTypeService
                .findMapByUnitId(unitId);
        List<Event> events = eventService.findAll(unitId);
        Map<String, List<Event>> eventMap = events.stream().collect(
                Collectors.groupingBy(Event::getTypeId, Collectors.toList()));
        model.put("events", events);
        model.put("eventTypeMap", eventTypeMap);
        model.put("eventMap", eventMap);
        model.put("eventIndicator", eventIndicator);
        return "/bigdata/extend/event/maintain/eventIndexEdit.ftl";
    }

    @RequestMapping("/saveEventIndex")
    @ResponseBody
    public Response saveEventIndex(EventIndicator eventIndicator) {
        eventIndicator.setUnitId(getLoginInfo().getUnitId());
        try {
            eventIndicatorService.saveEventIndicator(eventIndicator);
            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/deleteEventIndex")
    @ResponseBody
    public Response deleteEventIndex(String id) {
        eventIndicatorService.delete(id);
        return Response.ok().build();
    }

    // 配置
    @RequestMapping("/configure")
    public String configure(String eventId, ModelMap model) {
        model.put("eventId", eventId);
        return "/bigdata/extend/event/configure/configure.ftl";
    }

    // 配置
    @RequestMapping("/configure/sample")
    public String configureSimple(String type, ModelMap model) {
        if ("kafka".equals(type))
            return "/bigdata/extend/event/configure/kafkaSample.ftl";
        return "/bigdata/extend/event/configure/druidSample.ftl";
    }

    // 配置-kafka
    @RequestMapping("/configure/kafka")
    public String configure4kafka(String eventId, ModelMap model) {
        Event event = eventService.findOne(eventId);
        model.put("eventId", eventId);
        model.put("kafkaInfo", event.getKafkaInfo());
        return "/bigdata/extend/event/configure/kafkaConfigure.ftl";
    }

    // 配置-kafka -submit
    @RequestMapping("/configure/kafka/submit")
    @ResponseBody
    public Response configure4kafkaSubmit(String eventId, String kafkaInfo) {
        try {
            Json kafkaData = JSON.parseObject(kafkaInfo, Json.class);
            eventService.updateKafkaInfo(eventId, kafkaInfo);
            KafkaUtils kafkaUtils = new KafkaUtils();
            kafkaUtils.createTopic(kafkaData.getString("topic"),
                    kafkaData.getIntValue("replicationFactor"),
                    kafkaData.getIntValue("partitions"));
            return Response.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.error().message(e.getMessage()).build();
        }
    }

    // 配置-druid
    @RequestMapping("/configure/druid")
    public String configure4druid(String eventId, ModelMap model) {
        Event event = eventService.findOne(eventId);
        model.put("eventId", eventId);
        model.put("metadata", event.getMetadata());
        return "/bigdata/extend/event/configure/druidConfigure.ftl";
    }

    // 配置-druid -submit
    @RequestMapping("/configure/druid/submit")
    @ResponseBody
    public Response configure4druidSubmit(String eventId, String metadata) {
        try {
            eventService.updateMetadata(eventId, metadata);
            boolean result = druidClientService.submitDruidJob(metadata);
            if (result)
                return Response.ok().build();
            return Response.error().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.error().build();
        }
    }
}
