/**
 * FileName: CompareTimeCalendar
 * Author:   shenke
 * Date:     2018/4/24 下午2:26
 * Descriptor:
 */
package net.zdsoft;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author shenke
 * @since 2018/4/24 下午2:26
 */
public class CompareTimeCalendar {

    @Test
    public void compare() {
        int number = 100;
        long start = System.currentTimeMillis();
        while (number != 0) {
            Calendar.getInstance().getTime();
            number --;
        }
        System.out.println(System.currentTimeMillis() - start);
        number = 100;
        start = System.currentTimeMillis();
        while (number != 0) {
            new Date();
            number --;
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
