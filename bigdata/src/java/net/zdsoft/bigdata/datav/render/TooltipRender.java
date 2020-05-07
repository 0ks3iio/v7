package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupTooltip;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.enu.AxisPointerType;
import net.zdsoft.echarts.enu.Trigger;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/23 下午5:40
 */
public class TooltipRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.tooltip, diagram.getDiagramType())) {
            return;
        }
        GroupTooltip groupTooltip = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.tooltip, GroupTooltip.class);
        if (groupTooltip == null) {
            return;
        }

        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
        Tooltip tooltip = new Tooltip();
        individuation(tooltip, diagram.getDiagramType());
        tooltip.show(groupTooltip.getTooltipShow());
        eChartsRenderOption.getOp().tooltip(tooltip);
    }

    private void individuation(Tooltip tooltip, Integer diagramType) {
        //
        if (DiagramEnum.COMMON_BAR.getType().equals(diagramType)
                || DiagramEnum.CAPSULE_BAR.getType().equals(diagramType)
                || DiagramEnum.STRIPE_COMMON_BAR.getType().equals(diagramType)
                || DiagramEnum.STRIPE_CAPSULE_BAR.getType().equals(diagramType)) {
            tooltip.axisPointer().type(AxisPointerType.shadow)
                    .show(Boolean.TRUE);
            tooltip.trigger(Trigger.axis);
        }
        else if (DiagramEnum.SCATTER.getType().equals(diagramType)
                || DiagramEnum.SCATTER_EFFECT.getType().equals(diagramType)) {
            tooltip.trigger(Trigger.axis);
            tooltip.axisPointer().type(AxisPointerType.none).show(Boolean.FALSE);
        }
        else if (DiagramEnum.COMMON_LINE.getType().equals(diagramType)
                || DiagramEnum.COMMON_LINE_AREA.getType().equals(diagramType)) {
            tooltip.trigger(Trigger.axis);
            tooltip.axisPointer().type(AxisPointerType.cross).show(Boolean.TRUE)
                    .label().backgroundColor("#000");
        }
    }
}
