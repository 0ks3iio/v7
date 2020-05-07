package net.zdsoft.bigdata.datav.render.crete;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;

import java.util.List;

/**
 * 基本水平柱状图
 * @author shenke
 * @since 2018/9/29 10:37
 */
public class CommoStripeBarCreator extends AbstractCommonStripeBarCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.STRIPE_COMMON_BAR.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        EChartsRenderOption eChartsRenderOption = EChartsRenderOption.of(createBarOption(result));
        exchangeXY(eChartsRenderOption.getOp());
        return eChartsRenderOption;
    }
}
