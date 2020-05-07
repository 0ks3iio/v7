package net.zdsoft.bigdata.datav.render.crete.funnel;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupBarCommon;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.NeedRenderGroupBuilder;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.enu.PositionEnum;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.enu.SortEnum;
import net.zdsoft.echarts.series.Funnel;
import net.zdsoft.echarts.series.Series;

import java.util.List;
import java.util.Optional;

/**
 * @author shenke
 * @since 2018/10/15 10:33
 */
public class FunnelCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.COMMON_FUNNEL.getType().equals(diagram.getDiagramType())) {
            return null;
        }

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setSelfCoordSys(true);
        data.setType(SeriesEnum.funnel);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);

        Option option = new Option();
        JConverter.convert(data, option);
        earlyRender(option, earlyParameters);
        return EChartsRenderOption.of(option);
    }

    private void earlyRender(Option option, List<EarlyParameter> earlyParameters) {
        Optional<EarlyParameter> earlyParameter = earlyParameters.stream().filter(ep -> GroupKey.common.name().equals(ep.getGroupKey())).findAny();
        SortEnum sortEnum = SortEnum.descending;
        if (earlyParameter.isPresent()) {
            GroupBarCommon groupBarCommon = NeedRenderGroupBuilder.buildGroup(earlyParameter.get().getEarlyParameters(),
                    GroupKey.common, GroupBarCommon.class);
            if (groupBarCommon !=null && Boolean.FALSE.equals(groupBarCommon.getSort())) {
                sortEnum = SortEnum.ascending;
            }
        }
        option.legend().show(Boolean.FALSE);
        for (Series series : option.series()) {
            series.label().position(PositionEnum.inside);
            ((Funnel) series).sort(sortEnum);
        }
    }
}
