package net.zdsoft.bigdata.datav.render.crete.custom;

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

/**
 * @author shenke
 * @since 2018/12/4 上午10:37
 */
public class SimpleTabCreator implements RenderOptionCreator<RenderOption<SimpleTabOption>> {

    @Override
    public RenderOption<SimpleTabOption> create(Result result, Diagram diagram,
                                                List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.TAB.getType().equals(diagram.getDiagramType())) {
            return null;
        }

        List<SimpleTabOption.Tab> tabs = ObjectParseUtils.parseTabs(result);
        if (tabs == null) {
            throw new EntryUtils.DataException("不合法的数据");
        }

        RenderOption<SimpleTabOption> renderOption = new RenderOption<>();
        SimpleTabOption option = new SimpleTabOption();
        option.setTabs(tabs);
        option.setTabStyle(earlyRender(earlyParameters));
        renderOption.setOp(option);
        return renderOption;
    }

    private SimpleTabOption.TabStyle earlyRender(List<EarlyParameter> earlyParameters) {
        return earlyParameters.stream()
                .filter(earlyParameter -> GroupKey.tab_style.name().equals(earlyParameter.getGroupKey()))
                .findAny().map(earlyParameter -> {
            return NeedRenderGroupBuilder.buildGroup(earlyParameter.getEarlyParameters(),
                    GroupKey.tab_style, SimpleTabOption.TabStyle.class);
        }).get();
    }
}
