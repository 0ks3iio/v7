/**
 * FileName: EchartConstant
 * Author:   shenke
 * Date:     2018/4/25 上午11:10
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts;

import java.util.Random;

/**
 * echarts相关的常量声明
 * @author shenke
 * @since 2018/4/25 上午11:10
 */
public class EchartConstant {

    /** echarts默认的调色盘颜色 */
    public static final Object OPTION_COLOR = new String[]{
            "#9949d7", "#ee913a", "#1ebcd3", "#cb309a", "#1f83f5", "#93f51f", "#3bb7f0", "#2fe3c7", "#ee3a71","#d142a4"
    };

    public static Object randomColor() {
        String[] colors = ((String[])OPTION_COLOR);
        return colors[new Random().nextInt(colors.length)];
    }

}
