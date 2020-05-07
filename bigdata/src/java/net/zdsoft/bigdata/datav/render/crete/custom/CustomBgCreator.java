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
 * @since 2018/11/20 下午1:56
 */
public class CustomBgCreator implements RenderOptionCreator<CustomBgRenderOption> {

    @Override
    public CustomBgRenderOption create(Result result, Diagram diagram,
                                       List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.CUSTOM_BG.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        return new CustomBgRenderOption();
    }
}
