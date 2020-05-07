package net.zdsoft.bigdata.datav.render.crete;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/29 17:47
 */
public class LineCreator extends AbstractLineCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        //if (!DiagramEnum.COMMON_LINE.getType().equals(diagram.getDiagramType())) {
        //    return null;
        //}
        return null;
        //return EChartsRenderOption.of(crateOption(result));
    }
}
