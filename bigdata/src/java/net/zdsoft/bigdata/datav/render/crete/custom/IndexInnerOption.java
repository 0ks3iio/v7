package net.zdsoft.bigdata.datav.render.crete.custom;

import net.zdsoft.bigdata.datav.parameter.GroupText;

import java.util.List;

/**
 * 指标卡
 * @author shenke
 * @since 2018/10/15 9:09
 */
public class IndexInnerOption extends GroupText {

    private List<IndexInnerOptionSeries> series;

    public List<IndexInnerOptionSeries> getSeries() {
        return series;
    }

    public void setSeries(List<IndexInnerOptionSeries> series) {
        this.series = series;
    }
}
