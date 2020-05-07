package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/27 15:07
 */
public interface Render {

    void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram);

    /**
     * 是否支持ECharts
     * @return
     */
    default boolean supportECharts() {
        return true;
    }

    /**
     * 是否支持其他自定义图表
     * @return
     */
    default boolean supportOthers() {
        return false;
    }
}
