/**
 * FileName: VisualMap
 * Author:   shenke
 * Date:     2018/4/26 下午3:36
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.component;

/**
 * @author shenke
 * @since 2018/4/26 下午3:36
 */
public class VisualMap {

    private Integer min;
    private Integer max;

    private Boolean calculable;
    private String[] text;
    private String type;


    public VisualMap max(Integer max) {
        this.max = max;
        return this;
    }

    public VisualMap min(Integer min) {
        this.min = min;
        return this;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    public VisualMap calculable(Boolean calculable) {
        this.calculable = calculable;
        return this;
    }

    public VisualMap text(String[] text) {
        this.text = text;
        return this;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Boolean getCalculable() {
        return calculable;
    }

    public void setCalculable(Boolean calculable) {
        this.calculable = calculable;
    }

    public String[] getText() {
        return text;
    }

    public void setText(String[] text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
