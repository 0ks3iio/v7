package net.zdsoft.desktop.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.desktop.entity.DesktopCalendar;
import net.zdsoft.desktop.enu.SystemNotifyType;
import net.zdsoft.desktop.service.DesktopCalendarService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

/**
 * @author yangsj  2017-5-9下午4:24:56
 */
@Controller
@RequestMapping("/desktop/calendar")
public class CalendarAction extends DeskTopBaseAction {

    @Autowired
    private DesktopCalendarService desktopCalendarService;
    @Autowired
    private DateInfoRemoteService dateInfoRemoteService;
    
    @ResponseBody
    @RequestMapping(value = "restDay")
    public ResponseEntity<Map<String, String>> getRestDay(@RequestParam Date beginTime, @RequestParam Date endTime) {
        List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByDates(getLoginInfo().getUnitId(),
                beginTime, endTime), DateInfo.class);
        Map<String, String> restDayMap = Maps.newHashMap();
        for (DateInfo dateInfo : dateInfoList) {
            restDayMap.put(DateFormatUtils.format(dateInfo.getInfoDate(), "yyyy-MM-dd"),
                    String.valueOf("Y".equals(dateInfo.getIsFeast())));
        }

        return new ResponseEntity<>(restDayMap, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/save")
    @ControllerInfo("保存日历信息")  //DesktopCalendar calendar
    public String doSaveCalendar(@RequestBody String schedule, HttpServletRequest request) {
        CalEvent calEvent = new CalEvent(schedule);
        try {
            DesktopCalendar realCalendar = new DesktopCalendar();
            realCalendar.setId(UuidUtils.generateUuid());
            realCalendar.setUserId(getLoginInfo().getUserId());
            realCalendar.setColor(calEvent.getClassName());
            realCalendar.setBeginTime(getDate(calEvent.getStart()));
            realCalendar.setEndTime(getDate(calEvent.getEnd()));
            realCalendar.setContent(StringEscapeUtils.escapeHtml4(calEvent.getTitle()));
            realCalendar.setCreationTime(new Date());
            realCalendar.setIsDeleted(DesktopCalendar.DEFAULT_IS_DELETED);
            realCalendar.setIsAllday(calEvent.getAllDay());
            realCalendar.setSystemNotifyType(calEvent.getSystemNotifyType());
            setNotifyTime(calEvent, realCalendar);
            desktopCalendarService.save(realCalendar);
            RedisUtils.del("my.calendar." + getLoginInfo().getUserId());
            return success("保存成功");
        } catch (Exception e) {
            LOG.error("保存失败", e);
            return error("保存失败");
        }
    }

    @ResponseBody
    @RequestMapping("/delete")
    @ControllerInfo("删除日历信息")  //DesktopCalendar calendar
    public String doDeleteCalendar(@RequestBody String schedule) {

        CalEvent calEvent = new CalEvent(schedule);

        try {
            desktopCalendarService.delete(calEvent.getId());
            RedisUtils.del("my.calendar." + getLoginInfo().getUserId());
            return success("删除成功");
        } catch (Exception e) {
            LOG.error("删除失败", e);
            return error("删除失败");
        }
    }

    @ResponseBody
    @RequestMapping("/update")
    @ControllerInfo("更新日历信息")  //DesktopCalendar calendar
    public String doUpdateCalendar(@RequestBody String schedule) {
        CalEvent calEvent = new CalEvent(schedule);

        DesktopCalendar desktopCalendar = desktopCalendarService.findOne(calEvent.getId());
        desktopCalendar.setModifyTime(new Date());
        desktopCalendar.setContent(StringEscapeUtils.escapeHtml4(calEvent.getTitle()));
        desktopCalendar.setColor(calEvent.getClassName());
        //desktopCalendar.setNotifyTime(calEvent.getNotifyTime());
        desktopCalendar.setSystemNotifyType(calEvent.getSystemNotifyType());
        desktopCalendar.setBeginTime(getDate(calEvent.start));
        desktopCalendar.setEndTime(getDate(calEvent.end));

        setNotifyTime(calEvent, desktopCalendar);
        try {
            desktopCalendarService.save(desktopCalendar);
            RedisUtils.del("my.calendar." + getLoginInfo().getUserId());
            return success("更新成功");
        } catch (Exception e) {
            LOG.error("更新失败", e);
            return error("更新失败");
        }
    }

    @ResponseBody
    @RequestMapping("/findByTime")
    @ControllerInfo(value = "查找日历信息")  //DesktopCalendar calendar
    public String doFindCalendar(@RequestBody String schedule, ModelMap map) {
//		DesktopCalendar desktopCalendar = SUtils.dc(schedule, DesktopCalendar.class);
        CalEvent calEvent = new CalEvent(schedule);
        String userId = getLoginInfo().getUserId();
        List<DesktopCalendar> cList = desktopCalendarService.findConstantsByTimeSlot(userId, getDate(calEvent.getStart()), getDate(calEvent.getEnd()));

        JSONObject json = new JSONObject();
        json.put("events", cList);
        map.put("events", cList);

        return json.toJSONString();
    }


    @ResponseBody
    @RequestMapping("/dragUpdate")
    @ControllerInfo("拖拽更新日历")  //DesktopCalendar calendar
    public String doDragUpdateCalendar(@RequestBody String schedule) {
        CalEvent calEvent = new CalEvent(schedule);
        DesktopCalendar desktopCalendar = desktopCalendarService.findOne(calEvent.getId());
        Date dateEnd = null;
        if (StringUtils.isBlank(calEvent.getEnd())) {
            if (calEvent.getAllDay().equals("0")) {

                Date dateStart = getDate(calEvent.getStart());
                dateEnd = DateUtils.addHours(dateStart, 2);
            } else {
                Date dateStart = getDate(calEvent.getStart());
                dateEnd = DateUtils.addDays(dateStart, 1);
            }
        } else {
            dateEnd = getDate(calEvent.getEnd());
        }
        desktopCalendar.setModifyTime(new Date());
        desktopCalendar.setIsAllday(calEvent.getAllDay());
        desktopCalendar.setBeginTime(getDate(calEvent.getStart()));
        desktopCalendar.setEndTime(dateEnd);
        //desktopCalendar.setNotifyTime(calEvent.getNotifyTime());
        desktopCalendar.setSystemNotifyType(calEvent.getSystemNotifyType());

        setNotifyTime(calEvent, desktopCalendar);

        List<String> updateStrings = new ArrayList<String>();
        updateStrings.add("modifyTime");
        updateStrings.add("isAllday");
        updateStrings.add("beginTime");
        updateStrings.add("endTime");
        updateStrings.add("systemNotifyType");

        try {
            desktopCalendarService.update(desktopCalendar, desktopCalendar.getId(), updateStrings.toArray(new String[updateStrings.size()]));
            RedisUtils.del("my.calendar." + getLoginInfo().getUserId());
            return success("更新成功");
        } catch (Exception e) {
            LOG.error("更新失败", e);
            return error("更新失败");
        }
    }

    private void setNotifyTime(CalEvent calEvent, DesktopCalendar realCalendar) {
        if(calEvent.getSystemNotifyType() != null && !SystemNotifyType.NONE.equals(SystemNotifyType.valueOf(calEvent.getSystemNotifyType())) ) {
            long time = realCalendar.getBeginTime().getTime() - (long) SystemNotifyType.valueOf(calEvent.getSystemNotifyType()).getMinutes() * 60 * 1000;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            realCalendar.setNotifyTime(calendar.getTime());
        } else {
            realCalendar.setNotifyTime(calEvent.getNotifyTime());
        }
    }

    private Date getDate(String dateStr) {
        Date date = null;
        try {
            date = DateUtils.parseDate(dateStr, "yyyy-MM-dd", "yyyy-MM-dd HH:mm" , "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }

    public class CalEvent {
        private String id;
        private String title;
        private String start;
        private String end;
        private String allDay;
        private String className;
        private Date notifyTime;
        private Integer systemNotifyType;

        public CalEvent(String schedule) {
            JSONObject jsonObject = SUtils.dc(schedule, JSONObject.class);
            String title = jsonObject.getString("title");
            String start = jsonObject.getString("start");
            String end = jsonObject.getString("end");
            String color = jsonObject.getString("className");
            String allDay = jsonObject.getString("allDay");
            String id = jsonObject.getString("id");
            Integer systemNotifyType = jsonObject.getInteger("systemNotifyType");
            try {
                if ( jsonObject.getString("notifyTime") != null )
                this.notifyTime = DateUtils.parseDate(jsonObject.getString("notifyTime"),
                        "yyyy-MM-dd HH:mm");
            } catch (ParseException e) {
            }

            this.title = title;
            this.start = start;
            this.end = end;
            this.allDay = allDay;
            this.className = color;
            this.id = id;
            this.systemNotifyType = systemNotifyType ;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getAllDay() {
            return allDay;
        }

        public void setAllDay(String allDay) {
            this.allDay = allDay;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Date getNotifyTime() {
            return notifyTime;
        }

        public Integer getSystemNotifyType() {
            return systemNotifyType;
        }
    }
}
  
