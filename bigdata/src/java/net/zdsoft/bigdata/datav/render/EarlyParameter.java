package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.DiagramParameter;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/29 13:28
 */
public class EarlyParameter {

    private String groupKey;
    private List<DiagramParameter> earlyParameters;

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public List<DiagramParameter> getEarlyParameters() {
        return earlyParameters;
    }

    public void setEarlyParameters(List<DiagramParameter> earlyParameters) {
        this.earlyParameters = earlyParameters;
    }
}
