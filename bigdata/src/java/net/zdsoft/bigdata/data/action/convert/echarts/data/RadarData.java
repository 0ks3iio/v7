/**
 * FileName: RadarData.java
 * Author:   shenke
 * Date:     2018/5/30 上午11:22
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @since 2018/5/30 上午11:22
 */
public class RadarData {

    private String name;
    private List<Object> value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getValue() {
        return value;
    }

    public void setValue(List<Object> value) {
        this.value = value;
    }

    public List<Object> value() {
        if (value == null) {
            value = new ArrayList<>();
        }
        return value;
    }
}
