package net.zdsoft.bigdata.datav.render.crete.clock;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.crete.NullRenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;

import java.util.List;

/**
 * @author shenke
 * @since 2018/12/20 下午5:20
 */
public class ClockCreator implements RenderOptionCreator<NullRenderOption> {

    @Override
    public NullRenderOption create(Result result, Diagram diagram,
                                   List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.CLOCK.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        return new NullRenderOption();
    }
}
