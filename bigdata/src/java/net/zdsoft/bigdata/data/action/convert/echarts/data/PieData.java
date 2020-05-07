package net.zdsoft.bigdata.data.action.convert.echarts.data;

/**
 * @author ke_shen@126.com
 * @since 2018/4/12 下午2:14
 */
public class PieData {

    private String name;
    private Object value;

    public PieData(String name, Object value) {
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
