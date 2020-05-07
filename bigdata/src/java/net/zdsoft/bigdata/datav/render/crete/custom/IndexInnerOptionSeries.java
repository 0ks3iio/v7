package net.zdsoft.bigdata.datav.render.crete.custom;

/**
 * @author shenke
 * @since 2018/10/15 9:14
 */
public class IndexInnerOptionSeries {

    /**
     * 类似series name
     */
    private String key;
    /**
     * 百分比 %
     */
    private String value;
    private String backgroundColor;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
