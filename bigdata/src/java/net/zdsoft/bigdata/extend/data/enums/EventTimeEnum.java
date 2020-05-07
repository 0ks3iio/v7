package net.zdsoft.bigdata.extend.data.enums;

import com.google.common.collect.Maps;
import net.zdsoft.framework.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by wangdongdong on 2019/4/4 15:01.
 */
public enum EventTimeEnum {

    TODAY("当日"),

    YESTERDAY("昨日"),

    RECENT_SEVEN_DAYS("最近7天"),

    RECENT_THIRTY_DAYS("最近30天"),

    WEEK("本周"),

    MONTH("本月"),

    QUARTER("本季度"),

    YEAR("本年度");

    private String name;

    EventTimeEnum(String name) {
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
        for (EventTimeEnum e : EventTimeEnum.values()) {
            switch (e) {
                case TODAY:
                    timeMap.put(e.name, today());
                    break;
                case YESTERDAY:
                    timeMap.put(e.name, yesterday());
                    break;
                case RECENT_SEVEN_DAYS:
                    timeMap.put(e.name, getRecentDaysTime(7));
                    break;
                case RECENT_THIRTY_DAYS:
                    timeMap.put(e.name, getRecentDaysTime(30));
                    break;
                case WEEK:
                    timeMap.put(e.name, week());
                    break;
                case MONTH:
                    timeMap.put(e.name, month());
                    break;
                case QUARTER:
                    timeMap.put(e.name, quarter());
                    break;
                case YEAR:
                    timeMap.put(e.name, year());
            }
        }
        return timeMap;
    }

    private static String today() {
        return getTimeInterval(new Date(), new Date());
    }

    private static String yesterday() {
        Date yesterday = DateUtils.addDay(new Date(), -1);
        return getTimeInterval(yesterday, yesterday);
    }

    private static String getRecentDaysTime(int days) {
        return getTimeInterval(DateUtils.addDay(new Date(), -days), new Date());
    }

    private static String week() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 2 - dayOfWeek);
        return getTimeInterval(cal.getTime(), new Date());
    }

    private static String month() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), 1, 0, 0, 0);
        return getTimeInterval(cal.getTime(), new Date());
    }

    private static String quarter() {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 3)
            cal.set(Calendar.MONTH, 0);
        else if (currentMonth >= 4 && currentMonth <= 6)
            cal.set(Calendar.MONTH, 3);
        else if (currentMonth >= 7 && currentMonth <= 9)
            cal.set(Calendar.MONTH, 6);
        else if (currentMonth >= 10 && currentMonth <= 12)
            cal.set(Calendar.MONTH, 9);
        cal.set(Calendar.DATE, 1);
        return getTimeInterval(cal.getTime(), new Date());
    }

    private static String year() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        return getTimeInterval(cal.getTime(), new Date());
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
