package net.zdsoft.framework.echarts.entity.sub;

public class YAxis {
    
    public YAxis() {}
    
    public YAxis(String type) {
        this.type = type;
    }
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
