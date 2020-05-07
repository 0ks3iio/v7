package net.zdsoft.bigdata.datav.render.crete.pie;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.enu.RoseTypeEnum;
import net.zdsoft.echarts.series.Pie;
import net.zdsoft.echarts.series.Series;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/10 15:24
 */
public class AbstractRoseCreator extends AbstractPieCreator  {

    @Override
    protected Option createOption(List<JData.Entry> entryList) throws EntryUtils.DataException {
        Option option = super.createOption(entryList);
        for (Series series : option.series()) {
            ((Pie) series).roseType(RoseTypeEnum.radius);
        }
        return option;
    }
}
