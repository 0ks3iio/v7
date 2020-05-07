package net.zdsoft.bigdata.datav.enu;

/**
 * @author shenke
 * @since 2018/9/28 18:43
 */
public enum  ValueEnum {

    /**
     * 数字
     */
    NUMBER(1),
    /**
     * 是否选择
     */
    YES_NO(2),
    /**
     * 颜色
     */
    COLOR(3),
    /**
     * 字体粗细选择
     */
    FONT_WEIGHT(4),
    /**
     * 字符
     */
    STRING(5),
    /**
     * 横向位置选择
     */
    POSITION_TRANSVERSE(6),
    /**
     * 纵向位置选择
     */
    POSITION_PORTRAIT(7),
    /**
     * 背景颜色
     */
    BACKGROUND_COLOR(8),
    /**
     * 排列方式
     */
    ORIENT(9),
    SERIES_TYPE(10),
    /**
     * 饼图专用
     */
    LABEL_POSITION(11),
    /**
     * bar line scatter sankey
     */
    LABEL_POSITION_COMMON(12),

    LABEL_POSITION_FUNNEL(13),
    BORDER(14),
    LINE_TYPE(15),
    EFFECT_SCATTER_TYPE(16),

    DIGITAL_CARD_TURNER_TITLE_POSITION(17),
    /**
     * tab样式
     */
    BORDER_TYPE(18),
    /**
     * 装饰边框类型
     */
    COMMON_BORDER_TYPE(21),
    TITLE_TYPE(19),
    DIGITAL_CARD_TURNER_CONTENT_POSITION(20),
    ;

    private Integer type;

    ValueEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
