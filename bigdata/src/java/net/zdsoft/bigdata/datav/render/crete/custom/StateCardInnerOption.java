package net.zdsoft.bigdata.datav.render.crete.custom;

import net.zdsoft.bigdata.datav.parameter.GroupText;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/15 9:34
 */
public class StateCardInnerOption extends GroupText {

    private List<StateCardInnerOptionSeries> series;

    public List<StateCardInnerOptionSeries> getSeries() {
        return series;
    }

    public void setSeries(List<StateCardInnerOptionSeries> series) {
        this.series = series;
    }
}
