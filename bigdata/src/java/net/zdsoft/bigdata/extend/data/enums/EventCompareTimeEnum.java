package net.zdsoft.bigdata.extend.data.enums;

import com.google.common.collect.Maps;
import net.zdsoft.framework.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by wangdongdong on 2019/4/4 15:01.
 */
public enum EventCompareTimeEnum {

    YESTERDAY("昨天"),

    SEVEN_DAYS_AGO("7天前"),

    THIRTY_DAYS_AGO("30天前"),

    LAST_WEEK("上周"),

    LAST_MONTH("上月"),

    LAST_QUARTER("上季度"),

    LAST_YEAR("上年度"),

    LAST_TIME("上一时段"),

    LAST_YEAR_TIME("去年同期");

    private String name;

    EventCompareTimeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Map<String, String> timeMap() {
        Map<String, String> timeMap = Maps.newLinkedHashMap();
        for (EventCompareTimeEnum e : EventCompareTimeEnum.values()) {
            switch (e) {
                case YESTERDAY:
                    timeMap.put(e.name, yesterday());
                    break;
                case SEVEN_DAYS_AGO:
                    timeMap.put(e.name, sevenDaysAgo());
                    break;
                case THIRTY_DAYS_AGO:
                    timeMap.put(e.name, thirtyDaysAgo());
                    break;
                case LAST_WEEK:
                    timeMap.put(e.name, lastWeek());
                    break;
                case LAST_MONTH:
                    timeMap.put(e.name, lastMonth());
                    break;
                case LAST_QUARTER:
                    timeMap.put(e.name, lastQuarter());
                    break;
                case LAST_YEAR:
                    timeMap.put(e.name, lastYear());
                    break;
                case LAST_TIME:
                    timeMap.put(e.name, "");
                    break;
                case LAST_YEAR_TIME:
                    timeMap.put(e.name, "");
                    break;
            }
        }
        return timeMap;
    }

    private static String yesterday() {
        Date yesterday = DateUtils.addDay(new Date(), -1);
        return getTimeInterval(yesterday, yesterday);
    }

    private static String sevenDaysAgo() {
        Date yesterday = DateUtils.addDay(new Date(), -1);
        return getTimeInterval(DateUtils.addDay(new Date(), -7), yesterday);
    }

    private static String thirtyDaysAgo() {
        Date yesterday = DateUtils.addDay(new Date(), -1);
        return getTimeInterval(DateUtils.addDay(new Date(), -30), yesterday);
    }

    private static String lastWeek() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        cal.add(Calendar.DATE, 1 - dayOfWeek - 7);
        return getTimeInterval(cal.getTime(), DateUtils.addDay(cal.getTime(), 6));
    }

    private static String lastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY)-1, 1);
        Calendar cal1 = Calendar.getInstance();
        cal1.set(cal.get(Calendar.YEAR), cal1.get(Calendar.MONDAY), 0);
        return getTimeInterval(cal.getTime(),cal1.getTime());
    }

    private static String lastQuarter() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, (startCalendar.get(Calendar.MONTH) / 3 - 1) * 3);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, (endCalendar.get(Calendar.MONTH) / 3 - 1) * 3 + 2);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return getTimeInterval(startCalendar.getTime(), (endCalendar.getTime()));
    }

    private static String lastYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR)-1, 0, 1);
        Calendar cal1 = Calendar.getInstance();
        cal1.set(cal1.get(Calendar.YEAR), 0, 0);
        return getTimeInterval(cal.getTime(), cal1.getTime());
    }

    private static String getTimeInterval(Date beginDate, Date endDate) {
        return dateToString(beginDate) + "~" + dateToString(endDate);
    }

    private static String dateToString(Date date) {
        return DateUtils.date2StringByDay(date);
    }

    public static void main(String[] args) {
        Map<String, String> map = timeMap();
        map.forEach((key, value)->{
            System.out.println(key + ":" + value);
        });
    }
}
