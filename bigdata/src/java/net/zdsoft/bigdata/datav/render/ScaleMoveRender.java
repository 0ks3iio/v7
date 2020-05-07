package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupScaleMove;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.coords.geo.Geo;
import net.zdsoft.echarts.enu.Roam;
import net.zdsoft.echarts.enu.RoamEnum;
import net.zdsoft.echarts.enu.RoamEx;

import java.util.List;

/**
 * @author shenke
 * @since 2018/11/5 上午10:08
 */
public class ScaleMoveRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.scale_move, diagram.getDiagramType())) {
            return;
        }

        GroupScaleMove scaleMove = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.scale_move, GroupScaleMove.class);
        if (scaleMove == null) {
            return;
        }
        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
        Roam roam = parse(scaleMove);
        for (Geo geo : eChartsRenderOption.getOp().geo()) {
            geo.roam(roam);
        }
    }

    private Roam parse(GroupScaleMove scaleMove) {
        if (Boolean.TRUE.equals(scaleMove.getMove()) && Boolean.TRUE.equals(scaleMove.getScale())) {
            return RoamEx.enable();
        }
        else if (Boolean.TRUE.equals(scaleMove.getMove()) && !Boolean.TRUE.equals(scaleMove.getScale())) {
            return RoamEnum.move;
        }
        else if (!Boolean.TRUE.equals(scaleMove.getMove()) && Boolean.TRUE.equals(scaleMove.getScale())) {
            return RoamEnum.scale;
        }
        else {
            return RoamEx.disable();
        }
    }
}
