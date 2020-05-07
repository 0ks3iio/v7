package net.zdsoft.bigdata.datav.vo;

/**
 * @author shenke
 * @since 2018/10/11 15:01
 */
public class ModifyDiagramParameterVo {

    private String key;
    private String value;
    private String arrayName;
    private String elementId;

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

    public String getArrayName() {
        return arrayName;
    }

    public void setArrayName(String arrayName) {
        this.arrayName = arrayName;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }
}
