package net.zdsoft.bigdata.datav.render.crete.pie;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.enu.SeriesEnum;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author shenke
 * @since 2018/10/10 15:25
 */
public class AbstractPieCreator {

    protected Option createOption(List<JData.Entry> entryList) throws EntryUtils.DataException {
        if (entryList.stream().map(JData.Entry::getName).filter(Objects::nonNull).count()>1) {
            throw new EntryUtils.DataException("数据格式异常, 不能包含s");
        }

        JData data = new JData();
        data.setType(SeriesEnum.pie);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);
        data.setXAxisType(AxisType.category);
        JData.JAxisPosition xp = new JData.JAxisPosition();
        JData.JAxisPosition yp = new JData.JAxisPosition();
        data.setXJAxisPosition(xp);
        data.setYJAxisPosition(yp);
        Option option = new Option();
        JConverter.convert(data, option);
        return option;
    }
}
