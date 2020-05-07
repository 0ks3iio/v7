/**
 * FileName: TableOptionWrap.java
 * Author:   shenke
 * Date:     2018/5/28 下午5:04
 * Descriptor:
 */
package net.zdsoft.bigdata.data.echarts;

/**
 * @author shenke
 * @since 2018/5/28 下午5:04
 */
public class TableOptionWrap extends OptionEx<Table, TableOptionWrap> {

    @Override
    public Table getOption() {
        return this.option;
    }

    @Override
    public boolean isEcharts() {
        return false;
    }
}
