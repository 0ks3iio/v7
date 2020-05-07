package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupEffectScatter;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.enu.RippleEffectBrushType;
import net.zdsoft.echarts.series.EffectScatter;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.inner.RippleEffect;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/17 20:21
 */
public class EffectScatterRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.effect_scatter_common, diagram.getDiagramType())) {
            return;
        }
        EChartsRenderOption eChartsRenderOption = ((EChartsRenderOption) ro);
        GroupEffectScatter groupScatter = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.effect_scatter_common, GroupEffectScatter.class);
        if (groupScatter == null) {
            return;
        }
        for (Series series : eChartsRenderOption.getOp().series()) {
            if (series instanceof EffectScatter) {
                RippleEffect effect = ((EffectScatter) series).getRippleEffect();
                if (effect == null) {
                    effect = new RippleEffect();
                }
                effect.brushType(RippleEffectBrushType.valueOf(groupScatter.getEffectScatterType()))
                        .period(groupScatter.getEffectScatterPeriod())
                        .scale(Double.valueOf(groupScatter.getEffectScatterScale()));
                ((EffectScatter) series).rippleEffect(effect);
                ((EffectScatter) series).itemStyle().color(groupScatter.getEffectScatterColor());
            }
        }
    }
}
