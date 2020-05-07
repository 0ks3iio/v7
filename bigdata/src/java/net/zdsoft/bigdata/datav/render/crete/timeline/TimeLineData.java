package net.zdsoft.bigdata.datav.render.crete.timeline;

import net.zdsoft.echarts.enu.Symbol;
import net.zdsoft.echarts.series.data.Tooltip;

/**
 * @author shenke
 * @since 2018/11/13 下午2:45
 */
public class TimeLineData {

    private String name;
    private String value;
    private Tooltip<TimeLineData> tooltip;
    private Symbol symbol;
    private Integer symbolSize;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Tooltip<TimeLineData> getTooltip() {
        return tooltip;
    }

    public void setTooltip(Tooltip<TimeLineData> tooltip) {
        this.tooltip = tooltip;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public Integer getSymbolSize() {
        return symbolSize;
    }

    public void setSymbolSize(Integer symbolSize) {
        this.symbolSize = symbolSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
