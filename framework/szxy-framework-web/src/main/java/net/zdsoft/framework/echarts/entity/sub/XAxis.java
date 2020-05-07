package net.zdsoft.framework.echarts.entity.sub;

import java.util.List;

public class XAxis {
    private String type = "category";
    private List<String> data;
    private boolean boundaryGap = true;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public boolean isBoundaryGap() {
        return boundaryGap;
    }

    public void setBoundaryGap(boolean boundaryGap) {
        this.boundaryGap = boundaryGap;
    }

}
