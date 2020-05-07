/**
 * FileName: Op.java
 * Author:   shenke
 * Date:     2018/5/28 下午4:52
 * Descriptor:
 */
package net.zdsoft.bigdata.data.echarts;

/**
 * @author shenke
 * @since 2018/5/28 下午4:52
 */
public interface Op<O, T extends Op> {

    O getOption();

}
