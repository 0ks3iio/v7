package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupBarCommon;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.coords.cartesian2d.Grid;
import net.zdsoft.echarts.enu.BottomEx;
import net.zdsoft.echarts.enu.LeftEx;
import net.zdsoft.echarts.enu.RightEx;
import net.zdsoft.echarts.enu.TopEx;
import net.zdsoft.echarts.series.Line;
import net.zdsoft.echarts.series.Sankey;
import net.zdsoft.echarts.series.Series;

import java.util.List;

/**
 * 直角坐标系参数渲染
 *
 * @author shenke
 * @since 2018/9/29 10:54
 */
public class Cartesian2dRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.common, diagram.getDiagramType())) {
            return;
        }
        GroupBarCommon groupBarCommon = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.common, GroupBarCommon.class);
        if (groupBarCommon != null) {
            if (ro instanceof EChartsRenderOption) {

                for (Series series : ((EChartsRenderOption) ro).getOp().series()) {
                    if (series instanceof Line) {
                        ((Line) series).smooth(groupBarCommon.getSmooth());
                    }
                }

                if (DiagramEnum.SANKEY.getType().equals(diagram.getDiagramType())
                        || DiagramEnum.COMMON_FUNNEL.getType().equals(diagram.getDiagramType())
                        || DiagramEnum.TIMELINE.getType().equals(diagram.getDiagramType())) {
                    renderSankey(groupBarCommon, ((EChartsRenderOption) ro).getOp());
                    return;
                }

                List<Grid> grids = ((EChartsRenderOption) ro).getOp().getGrid();
                if (grids == null) {
                    return;
                }
                int counter = 0;
                for (Grid grid : grids) {
                    if (DiagramEnum.STRIPE_PN_BAR.getType().equals(diagram.getDiagramType())) {
                        if (counter == 0) {
                            renderLeft(groupBarCommon.getLeftCs(), grid);
                        } else {
                            renderRight(groupBarCommon.getRightCs(), grid);
                        }
                    } else {
                        renderLeft(groupBarCommon.getLeftCs(), grid);
                        renderRight(groupBarCommon.getRightCs(), grid);
                    }

                    if (groupBarCommon.getTopCs() != null) {
                        grid.top(TopEx.create(groupBarCommon.getTopCs()));
                    }
                    if (groupBarCommon.getBottomCs() != null) {
                        grid.bottom(BottomEx.create(groupBarCommon.getBottomCs()));
                    }
                    counter++;
                }
            }
        }
    }

    private void renderSankey(GroupBarCommon groupBarCommon, Option option) {
        for (Series series : option.series()) {
            if (groupBarCommon.getLeftCs() != null) {
                (series).left(LeftEx.create(groupBarCommon.getLeftCs()));
            }
            if (groupBarCommon.getTopCs() != null) {
                (series).top(TopEx.create(groupBarCommon.getTopCs()));
            }
            if (groupBarCommon.getRightCs() != null) {
                (series).right(RightEx.create(groupBarCommon.getRightCs()));
            }
            if (groupBarCommon.getBottomCs() != null) {
                (series).bottom(BottomEx.create(groupBarCommon.getBottomCs()));
            }
        }
    }

    private void renderLeft(String left, Grid grid) {
        if (left != null) {
            grid.left(LeftEx.create(left));
        }
    }

    private void renderRight(String right, Grid grid) {
        if (right != null) {
            grid.right(RightEx.create(right));
        }
    }
}
