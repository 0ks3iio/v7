/**
 * FileName: INumberOptionWrap.java
 * Author:   shenke
 * Date:     2018/5/29 下午4:37
 * Descriptor:
 */
package net.zdsoft.bigdata.data.echarts;

/**
 * @author shenke
 * @since 2018/5/29 下午4:37
 */
public class INumberOptionWrap extends OptionEx<INumber, INumberOptionWrap> {

    @Override
    public boolean isEcharts() {
        return false;
    }

    @Override
    public INumber getOption() {
        return this.option;
    }
}
