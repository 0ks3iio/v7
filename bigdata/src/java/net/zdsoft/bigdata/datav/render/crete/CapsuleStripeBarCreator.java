package net.zdsoft.bigdata.datav.render.crete;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.series.Bar;
import net.zdsoft.echarts.series.Series;

import java.util.List;

/**
 * 胶囊水平柱状图
 * @author shenke
 * @since 2018/9/29 13:40
 */
public class CapsuleStripeBarCreator extends AbstractCommonStripeBarCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.STRIPE_CAPSULE_BAR.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        EChartsRenderOption eChartsRenderOption = EChartsRenderOption.of(createBarOption(result));
        exchangeXY(eChartsRenderOption.getOp());
        earlyRender(eChartsRenderOption.getOp(), earlyParameters);
        return eChartsRenderOption;
    }

    private void earlyRender(Option option, List<EarlyParameter> earlyParameters) {
        for (Series series : option.getSeries()) {
            if (series instanceof Bar) {
                ((Bar) series).itemStyle().setBarBorderRadius(new Object[]{60, 60});
            }
        }
    }
}
