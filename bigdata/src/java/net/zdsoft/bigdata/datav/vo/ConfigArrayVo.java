package net.zdsoft.bigdata.datav.vo;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/12 14:59
 */
public class ConfigArrayVo {

    private String arrayName;
    private String groupKey;
    private List<ConfigArrayItem> configArrayItems;
    private String elementId;

    public String getArrayName() {
        return arrayName;
    }

    public void setArrayName(String arrayName) {
        this.arrayName = arrayName;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public List<ConfigArrayItem> getConfigArrayItems() {
        return configArrayItems;
    }

    public void setConfigArrayItems(List<ConfigArrayItem> configArrayItems) {
        this.configArrayItems = configArrayItems;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }
}
