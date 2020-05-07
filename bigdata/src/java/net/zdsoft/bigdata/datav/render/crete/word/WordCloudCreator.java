package net.zdsoft.bigdata.datav.render.crete.word;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.enu.SeriesEnum;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/15 10:36
 */
public class WordCloudCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {

        if (!DiagramEnum.WORD_CLOUD.getType().equals(diagram.getDiagramType())) {
            return null;
        }

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setSelfCoordSys(true);
        data.setType(SeriesEnum.wordCloud);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);

        Option option = new Option();
        JConverter.convert(data, option);
        return EChartsRenderOption.of(option);
    }
}
