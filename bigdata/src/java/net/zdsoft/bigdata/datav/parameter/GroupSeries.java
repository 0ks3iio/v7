package net.zdsoft.bigdata.datav.parameter;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author shenke
 * @since 2018/9/29 11:47
 */
public class GroupSeries implements Comparable<GroupSeries> {

    private String arrayName;
    private String seriesShowName;
    private String seriesName;
    private String color;
    private String type;
    private Integer symbolSize;

    public String getSeriesShowName() {
        return seriesShowName;
    }

    public void setSeriesShowName(String seriesShowName) {
        this.seriesShowName = seriesShowName;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArrayName() {
        return arrayName;
    }

    public void setArrayName(String arrayName) {
        this.arrayName = arrayName;
    }

    public Integer getSymbolSize() {
        return symbolSize;
    }

    public void setSymbolSize(Integer symbolSize) {
        this.symbolSize = symbolSize;
    }

    @Override
    public int compareTo(GroupSeries o) {
        if (o == null) {
            return -1;
        }
        Integer oi = NumberUtils.toInt(o.getArrayName().replaceAll("[^0-9]", ""));
        Integer ii = NumberUtils.toInt(this.arrayName.replaceAll("[^0-9]", ""));
        return ii.compareTo(oi);
    }
}
