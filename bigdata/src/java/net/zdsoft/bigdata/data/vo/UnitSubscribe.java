/**
 * FileName: UnitSubscribe.java
 * Author:   shenke
 * Date:     2018/5/18 下午2:54
 * Descriptor:
 */
package net.zdsoft.bigdata.data.vo;

/**
 * @author shenke
 * @since 2018/5/18 下午2:54
 */
public class UnitSubscribe {

    private String unitId;
    private String unitName;
    private Boolean subscribe;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }
}
