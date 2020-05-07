package net.zdsoft.bigdata.datav.render.crete.custom;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/14 15:41
 */
public class ProportionalCreator implements RenderOptionCreator<CustomProportionalRenderOption> {

    @Override
    public CustomProportionalRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.PROPORTIONAL.getType().equals(diagram.getDiagramType())) {
            return null;
        }

        CustomProportionalRenderOption customProportionalRenderOption = new CustomProportionalRenderOption();
        customProportionalRenderOption.setOp(ObjectParseUtils.parseProportional(result));
        return customProportionalRenderOption;
    }
}
