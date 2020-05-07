package net.zdsoft.bigdata.datav.enu;

/**
 * 组件类型
 * @author shenke
 * @since 2018/10/15 13:26
 */
public enum  ElementEnum {

    /**
     * 散点
     */
    SCATTER(1),
    EFFECT_SCATTER(2),
    LINES(3),
    /**
     * 热力图
     */
    HEAT(4),
    ;

    private Integer type;

    ElementEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
