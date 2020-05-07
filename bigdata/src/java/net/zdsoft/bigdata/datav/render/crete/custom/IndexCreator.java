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
 * @since 2018/10/15 9:11
 */
public class IndexCreator implements RenderOptionCreator<CustomIndexRenderOption> {

    @Override
    public CustomIndexRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.INDEX.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        List<IndexInnerOptionSeries> series = ObjectParseUtils.parseIndex(result);
        IndexInnerOption indexInnerOption = new IndexInnerOption();
        indexInnerOption.setSeries(series);
        CustomIndexRenderOption customIndexRenderOption = new CustomIndexRenderOption();
        customIndexRenderOption.setOp(indexInnerOption);
        earlyRender(earlyParameters, customIndexRenderOption);
        return customIndexRenderOption;
    }

    private void earlyRender(List<EarlyParameter> earlyParameters, CustomIndexRenderOption customIndexRenderOption) {
        earlyParameters.stream().filter(ep -> GroupKey.series.name().equals(ep.getGroupKey()))
                .findFirst().ifPresent(earlyParameter -> {
            List<GroupSeries> groupSeriesList = NeedRenderGroupBuilder.buildGroups(earlyParameter.getEarlyParameters(), GroupKey.series, GroupSeries.class);
            if (groupSeriesList != null) {
                Map<String, String> colors = groupSeriesList.stream().collect(Collectors.toMap(GroupSeries::getSeriesName, GroupSeries::getColor, (k,v)->v));
                int counter = 0;
                for (IndexInnerOptionSeries series : customIndexRenderOption.getOp().getSeries()) {
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
            if (groupText != null) {
                customIndexRenderOption.getOp().setTextFontColor(groupText.getTextFontColor());
                customIndexRenderOption.getOp().setTextFontSize(groupText.getTextFontSize());
                customIndexRenderOption.getOp().setTextFontWeight(groupText.getTextFontWeight());
            }
        });
    }
}
