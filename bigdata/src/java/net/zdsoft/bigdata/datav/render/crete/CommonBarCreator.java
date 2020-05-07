package net.zdsoft.bigdata.datav.render.crete;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;

import java.util.List;

/**
 * 通用柱状图
 * 不包括 柱状图和折线的混合图
 * 不包括 正负柱状图
 * @author shenke
 * @since 2018/9/27 16:29
 */
public class CommonBarCreator extends AbstractBarCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.COMMON_BAR.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        return EChartsRenderOption.of(createBarOption(result));
    }
}
