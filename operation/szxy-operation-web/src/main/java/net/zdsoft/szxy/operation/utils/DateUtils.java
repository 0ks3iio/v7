package net.zdsoft.szxy.operation.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/4/2 下午3:49
 */
public final class DateUtils {

    public static String toString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String toString(Date date) {
        return toString(date, "yyyy-MM-dd");
    }
}
