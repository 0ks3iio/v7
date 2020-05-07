package net.zdsoft.bigdata.datav.enu;

/**
 * @author shenke
 * @since 2018/9/27 16:37
 */
public enum DiagramEnum {

    /**
     * 通用柱状图
     */
    COMMON_BAR(21),
    CAPSULE_BAR(22),
    STRIPE_COMMON_BAR(24),
    STRIPE_CAPSULE_BAR(25),
    COMPOSITE_BAR_LINE(26),
    STRIPE_PN_BAR(27),

    COMMON_PIE(31),
    COMMON_PIE_ANNULAR(32),
    SINGLE_ANNULAR(34),
    PROPORTION_ANNULAR(33),
    COMMON_PIE_ROSE(35),
    COMMON_PIE_ANNULAR_ROSE(36),

    COMMON_LINE(41),
    COMMON_LINE_AREA(42),

    SCATTER(51),
    SCATTER_EFFECT(52),

    COMMON_RADAR(61),

    COMMON_FUNNEL(71),

    WORD_CLOUD(92),
    GRAPH(85),
    SANKEY(86),
    TREE_MAP(87),

    MAP(91),


    TITLE(94),
    TABLE(99),
    PROPORTIONAL(98),
    UP_NUMBER(95),
    /**
     * 指标卡
     */
    INDEX(96),
    /**
     * 状态卡片
     */
    STATE_CARD(100),
    DYNAMIC_NUMBER(97),
    /**
     * 翻牌器
     */
    DIGITAL_CARD_TURNER(101),

    /**
     * 单张图片
     */
    SINGLE_IMAGE(110),
    /**
     * 轮播图片
     */
    SHUFFLING_IMAGE(111),

    CLOCK(112),

    /**
     * 空白
     */
    BLANK(201),
    /**
     * 自定义背景
     */
    CUSTOM_BG(202),
    I_FRAME(203),
    /**
     * 装饰框框
     */
    BORDER(204),

    /**
     * 标题装饰
     */
    TITLE_DECORATION(205),

    /**
     * 交互组件 时间轴
     */
    TIMELINE(301),
    /**
     * 交互组件tab
     */
    TAB(302);

    private Integer type;

    DiagramEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
