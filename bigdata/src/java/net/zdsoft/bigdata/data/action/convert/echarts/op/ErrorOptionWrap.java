/**
 * FileName: ErrorOptionWrap.java
 * Author:   shenke
 * Date:     2018/5/29 上午10:32
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.op;

import net.zdsoft.bigdata.data.echarts.OptionEx;

/**
 * @author shenke
 * @since 2018/5/29 上午10:32
 */
public class ErrorOptionWrap extends OptionEx<Object, ErrorOptionWrap> {

    @Override
    public Object getOption() {
        return null;
    }

    public static ErrorOptionWrap error(String message) {
        return new ErrorOptionWrap().message(message).success(false);
    }

    @Override
    public boolean isEcharts() {
        throw new RuntimeException("not support");
    }
}
