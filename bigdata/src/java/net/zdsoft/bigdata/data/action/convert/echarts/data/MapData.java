package net.zdsoft.bigdata.data.action.convert.echarts.data;

/**
 * @author ke_shen@126.com
 * @since 2018/4/17 下午3:04
 */
public class MapData {

    private String name;
    private Object value;

    public MapData(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
