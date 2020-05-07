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
 * @since 2018/12/19 下午6:29
 */
public class TitleDecorationCreator implements RenderOptionCreator<RenderOption<TitleDecoration>> {

    @Override
    public RenderOption<TitleDecoration> create(Result result, Diagram diagram,
                                                List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.TITLE_DECORATION.getType().equals(diagram.getDiagramType())) {
            return null;
        }

        RenderOption<TitleDecoration> option = new RenderOption<>();
        Optional<EarlyParameter> optional = earlyParameters.stream().filter(earlyParameter -> GroupKey.title_decoration.name().equals(earlyParameter.getGroupKey())).findFirst();
        if (optional.isPresent()) {
            TitleDecoration decorationOption = NeedRenderGroupBuilder.buildGroup(optional.get().getEarlyParameters(), GroupKey.title_decoration, TitleDecoration.class);
            option.setOp(decorationOption);
        }
        return option;
    }
}
