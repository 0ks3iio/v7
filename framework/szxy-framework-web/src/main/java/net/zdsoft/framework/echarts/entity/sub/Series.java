package net.zdsoft.framework.echarts.entity.sub;

import java.util.List;

public class Series {
    private String name;
    private String type;
    private List<SerialData> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SerialData> getData() {
        return data;
    }

    public void setData(List<SerialData> data) {
        this.data = data;
    }

}
