package net.zdsoft.bigdata.datav.render.crete.custom;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupSeries;
import net.zdsoft.bigdata.datav.parameter.GroupText;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.NeedRenderGroupBuilder;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2018/10/15 9:36
 */
public class StateCardCreator implements RenderOptionCreator<CustomStateCardRenderOption> {

    @Override
    public CustomStateCardRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.STATE_CARD.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        CustomStateCardRenderOption customStateCardRenderOption = new CustomStateCardRenderOption();
        customStateCardRenderOption.setOp(ObjectParseUtils.parseStateCard(result));
        earlyRender(earlyParameters, customStateCardRenderOption);
        return customStateCardRenderOption;
    }

    private void earlyRender(List<EarlyParameter> earlyParameters,
                             CustomStateCardRenderOption customStateCardRenderOption) {
        earlyParameters.stream().filter(ep -> GroupKey.series.name().equals(ep.getGroupKey()))
                .findFirst().ifPresent(earlyParameter -> {
            List<GroupSeries> groupSeriesList = NeedRenderGroupBuilder.buildGroups(earlyParameter.getEarlyParameters(), GroupKey.series, GroupSeries.class);
            if (groupSeriesList != null) {
                Map<String, String> colors = groupSeriesList.stream().collect(Collectors.toMap(GroupSeries::getSeriesName,
                        GroupSeries::getColor, (k, v)->v));
                int counter = 0;
                for (StateCardInnerOptionSeries series : customStateCardRenderOption.getOp().getSeries()) {
                    int key = counter % groupSeriesList.size();
                    if (key == groupSeriesList.size()) {
                        key = key - 1;
                    }
                    series.setBackgroundColor(colors.getOrDefault(series.getKey(), groupSeriesList.get(key).getColor()));
                    counter++;
                }
            }
        });

        earlyParameters.stream().filter(ep -> GroupKey.text.name().equals(ep.getGroupKey()))
                .findFirst().ifPresent(earlyParameter -> {
            GroupText groupText = NeedRenderGroupBuilder.buildGroup(earlyParameter.getEarlyParameters(), GroupKey.text, GroupText.class);
            StateCardInnerOption stateCardInnerOption = customStateCardRenderOption.getOp();
            if (groupText != null) {
                stateCardInnerOption.setTextFontColor(groupText.getTextFontColor());
                stateCardInnerOption.setTextFontSize(groupText.getTextFontSize());
                stateCardInnerOption.setTextFontWeight(groupText.getTextFontWeight());
            }
        });
    }
}
