package net.zdsoft.bigdata.datav.render.crete.scatter;

import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.enu.CoordinateSystem;
import net.zdsoft.echarts.enu.SeriesEnum;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/10 16:34
 */
public class AbstractScatterCreator {

    protected Option createOption(List<JData.Entry> entryList) {
        JData data = new JData();
        data.setType(SeriesEnum.scatter);
        data.setCoordSys(CoordinateSystem.cartesian2d);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);

        Option option = new Option();
        JConverter.convert(data, option);
        return option;
    }
}
