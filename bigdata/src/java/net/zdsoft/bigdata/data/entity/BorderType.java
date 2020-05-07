package net.zdsoft.bigdata.data.entity;

/**
 * @author shenke
 * @since 2018/8/8 下午5:38
 */
public enum BorderType {

    /**
     * 不显示
     */
    none(0),
    /**
     * 全部显示
     */
    all(1),
    /**
     * 四角显示
     */
    quadrangle(2);

    private Integer value;

    BorderType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
