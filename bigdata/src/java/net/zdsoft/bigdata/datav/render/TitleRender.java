package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupTitle;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.element.Title;
import net.zdsoft.echarts.enu.FontWeightEx;
import net.zdsoft.echarts.enu.LeftEx;
import net.zdsoft.echarts.enu.Target;
import net.zdsoft.echarts.enu.TopEx;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/24 下午6:44
 */
public class TitleRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.title, diagram.getDiagramType())) {
            return;
        }

        GroupTitle groupTitle = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.title, GroupTitle.class);
        if (groupTitle == null) {
            return;
        }

        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
        Title title = eChartsRenderOption.getOp().title();
        title.show(groupTitle.getTitleShow())
                .left(LeftEx.create(groupTitle.getTitleLeft()))
                .top(TopEx.create(groupTitle.getTitleTop()))
                .link(groupTitle.getTitleUrl())
                .target(Target.blank)
                .text(groupTitle.getTitleName())
                .textStyle().color(groupTitle.getTitleFontColor())
                .fontWeight(FontWeightEx.create(groupTitle.getTitleFontWeight()))
                .fontSize(groupTitle.getTitleFontSize());

    }
}
