package net.zdsoft.bigdata.datav.render.crete.decoration;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.NeedRenderGroupBuilder;
import net.zdsoft.bigdata.datav.render.crete.RenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;

import java.util.List;
import java.util.Optional;

/**
 * @author shenke
 * @since 2018/12/17 上午9:54
 */
public class DecorationBorderCreator implements RenderOptionCreator<RenderOption<DecorationOption>> {


    @Override
    public RenderOption<DecorationOption> create(Result result, Diagram diagram,
                         List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.BORDER.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        RenderOption<DecorationOption> option = new RenderOption<>();

        Optional<EarlyParameter> optional = earlyParameters.stream().filter(earlyParameter -> GroupKey.border_common.name().equals(earlyParameter.getGroupKey())).findFirst();
        if (optional.isPresent()) {
            DecorationOption decorationOption = NeedRenderGroupBuilder.buildGroup(optional.get().getEarlyParameters(), GroupKey.border_common, DecorationOption.class);
            option.setOp(decorationOption);
        }
        return option;
    }
}
