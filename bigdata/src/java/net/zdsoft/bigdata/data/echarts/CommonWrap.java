/**
 * FileName: CommonWrap.java
 * Author:   shenke
 * Date:     2018/7/3 下午5:06
 * Descriptor:
 */
package net.zdsoft.bigdata.data.echarts;

/**
 * @author shenke
 * @since 2018/7/3 下午5:06
 */
public class CommonWrap extends OptionEx<Object, CommonWrap> {

    @Override
    public boolean isEcharts() {
        return false;
    }

    @Override
    public Object getOption() {
        return this.option;
    }
}
