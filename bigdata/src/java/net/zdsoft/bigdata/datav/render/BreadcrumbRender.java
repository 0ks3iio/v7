package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupBreadcrumb;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.TreeMap;
import net.zdsoft.echarts.series.inner.Breadcrumb;
import net.zdsoft.echarts.style.ItemStyle;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/23 上午11:34
 */
public class BreadcrumbRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.breadcrumb, DiagramEnum.TREE_MAP.getType())) {
            return;
        }

        GroupBreadcrumb groupBreadcrumb = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.breadcrumb, GroupBreadcrumb.class);
        if (groupBreadcrumb == null) {
            return;
        }

        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
        for (Series series : eChartsRenderOption.getOp().series()) {
            if (series instanceof TreeMap) {
                Breadcrumb breadcrumb = new Breadcrumb();
                breadcrumb.setShow(groupBreadcrumb.getBreadcrumbShow());
                ItemStyle itemStyle = new ItemStyle();
                itemStyle.setColor(groupBreadcrumb.getBreadcrumbBackgroundColor());
                breadcrumb.setItemStyle(itemStyle);
                ((TreeMap) series).setBreadcrumb(breadcrumb);
            }
        }
    }
}
