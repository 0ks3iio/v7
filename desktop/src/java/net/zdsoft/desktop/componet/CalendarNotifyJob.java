package net.zdsoft.desktop.componet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import net.zdsoft.desktop.entity.DesktopCalendar;
import net.zdsoft.desktop.service.DesktopCalendarService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenke
 * @since 2017.10.17
 */
public class CalendarNotifyJob implements Job {

    private Logger logger = Logger.getLogger(CalendarNotifyJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Date fireTime = jobExecutionContext.getFireTime();

        Date realFireTime = null;
        try {
            realFireTime = DateUtils.parseDate(DateFormatUtils.format(fireTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
        } catch (ParseException e) {
            logger.error("日历提醒定时任务，时间转换出错，任务触发时间["
                    + DateFormatUtils.format(jobExecutionContext.getFireTime(), "yyyy-MM-dd HH:mm:ss")
                    + "], 请检查遗漏的提醒消息", e);
        }
        DesktopCalendarService desktopCalendarService = Evn.getBean("desktopCalendarService");
        List<DesktopCalendar> desktopCalendarList = desktopCalendarService.findByNotifyTime(realFireTime);
        if ( logger.isDebugEnabled() ) {
            int size = desktopCalendarList != null ? desktopCalendarList.size() : 0;
            logger.debug("扫描日历提醒消息【" + size + "】");
        }
        //调用手机端接口推送 每分钟起一个线程
        if ( desktopCalendarList != null && desktopCalendarList.size() > 0 ) {
            new Thread(new PushThreadRunnable(desktopCalendarList)).start();
        }
    }

    class PushThreadRunnable implements Runnable {

        private List<DesktopCalendar> desktopCalendars;

        public PushThreadRunnable(List<DesktopCalendar> desktopCalendars) {
            this.desktopCalendars = desktopCalendars;
        }

        @Override
        public void run() {
            Map<String, JSONObject> msgMap = new HashMap<>(15);
            for (final DesktopCalendar calendar : desktopCalendars) {
                JSONObject msgParam ;
                if ( (msgParam = msgMap.get(calendar.getUserId())) == null ) {
                    msgParam = new JSONObject();
                    //msgTitle
                    msgParam.put("msgTitle", "日程提醒");
                    //bodyTitle
                    msgParam.put("bodyTitle", "日程提醒");
                    //jumpType
                    msgParam.put("jumpType", 0);
                    //url
                    msgParam.put("url", StringUtils.EMPTY);
                    //userIdArray
                    List<Object> userIds = new ArrayList<Object>() {
                        {
                            add(calendar.getUserId());
                        }
                    };
                    msgParam.put("userIdArray", new JSONArray(userIds));

                    List<Object> contents = new ArrayList<Object>() {
                        {
                            add(calendar.getContent());
                            add("");
                            String timeStr = DateFormatUtils.format(calendar.getBeginTime(), "yyyy-MM-dd HH:mm") +
                                    "——" + DateFormatUtils.format(calendar.getEndTime(), "yyyy-MM-dd HH:mm");
                            add(timeStr);
                        }
                    };
                    msgParam.put("rowsContent", new JSONArray(contents));
                    msgMap.put(calendar.getUserId(), msgParam);
                } else {
                    JSONArray rowsContent = msgParam.getJSONArray("rowsContent");
                    rowsContent.add(calendar.getContent());
                }
            }
            OpenApiOfficeService apiOfficeService = Evn.getBean("openApiOfficeService");
            if ( apiOfficeService == null ) {
                logger.error("请检查6.0 dubbo服务是否正常[net.zdsoft.remote.openapi.service.OpenApiOfficeService == null]");
                return ;
            }
            JSONArray jsonArray = new JSONArray(Lists.newArrayList(msgMap.values()));
            apiOfficeService.pushWeikeMessage("", jsonArray.toJSONString());
        }
    }
}
