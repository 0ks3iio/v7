package net.zdsoft.bigdata.data.code;

/**
 * 图表类型常量
 * @author ke_shen@126.com
 * @since 2018/4/12 下午2:42
 */
public class ChartType {

    /** 直线图 */
    @Deprecated
    public static final int LINE_STRAIGHT       = 11;
    /** 曲线图 */
    public static final int LINE_BROKEN         = 12;
    /** 折线面积图 */
    public static final int LINE_AREA           = 13;
    /** 基本柱状图 */
    public static final int BAR_BASIC           = 21;
    /** 堆叠柱状图 */
    public static final int BAR_STACK           = 23;
    /** 条形图 */
    public static final int BAR_STRIPE          = 24;
    /** 堆叠条形图 */
    public static final int BAR_STACK_STRIPE    = 25;
    public static final int BAR_2_STRIPE        = 27;
    /**
     * 柱状图折线复合
     */
    public static final int BAR_LINE            = 26;
    /** 3d柱状图 */
    public static final int BAR_3D              = 22;
    /** 基本饼状图 */
    public static final int PIE_BASIC           = 31;
    /** 环形图（也是饼图） */
    public static final int PIE_DOUGHNUT        = 32;
    /**
     * 复合的环形图，文字居中
     */
    public static final int PIE_DOUGHNUT_COMPOSITE  = 35;
    /** 内饼外环 */
    public static final int INNER_PIE_OUTTER_DOUGHNUT = 33;
    /** 数值不同 半径大小不同(南丁格尔图) */
    public static final int PIE_FNF             = 34;
    /** 基本条状图 （就是bar）横着的柱状图  */
    public static final int STRIP               = 41;
    /** 基本雷达图 */
    public static final int RADAR_BASIC         = 61;
    /** 散点图 */
    public static final int SCATTER             = 51;
    /** 气泡散点图 */
    public static final int SCATTER_EFFECT      = 52;

    /** 仪表盘 */
    public static final int GAUGE               = 81;
    public static final int GRAPH               = 85;
    public static final int SANKEY               = 86;
    public static final int TREE_MAP               = 87;

    /** 漏斗图(倒金字塔) */
    public static final int FUNNEL              = 71;
    /** 正金字塔 */
    public static final int FUNNEL_ASCENDING    = 72;

    /** 字符云 */
    public static final int WORD_CLOUD          = 92;

    /** 地图 */
    public static final int MAP                 = 91;
    public static final int MAP_LINE            = 93;

    public static final int NUMBER_UP           = 95;
    public static final int NUMBER_DOWN         = 96;

    /**
     * 动态变化的数字
     */
    public static final int DYNAMIC_NUMBER      = 97;
    /** 自定义类型 数字 （不属于echarts类型） */
    public static final int SELF_NUMBER         = 98;
    /** 自定义类型 列表 （不属于echarts类型） */
    public static final int SELF_TABLE          = 99;

    public static final int TEXT_TITLE = 94;

    ///** 非echarts图表 */
    //public static final int NAN                 = -1;
    //private static List<EnableType> types;
    //static {
    //    types = new ArrayList<>(4);
    //    types.add(new EnableType("折线图" , LINE_BROKEN));
    //    types.add(new EnableType("柱状图", BAR_BASIC));
    //    types.add(new EnableType("饼状图", PIE_BASIC));
    //    types.add(new EnableType("散点图" ,SCATTER));
    //}
    //public static List<EnableType> getEnableChartTypes() {
    //    return new ArrayList<>(types);
    //}
    //
    //public static class EnableType {
    //    private String name;
    //    private Integer type;
    //
    //    public EnableType(String name, Integer type) {
    //        this.name = name;
    //        this.type = type;
    //    }
    //
    //    public String getName() {
    //        return name;
    //    }
    //
    //    public void setName(String name) {
    //        this.name = name;
    //    }
    //
    //    public Integer getType() {
    //        return type;
    //    }
    //
    //    public void setType(Integer type) {
    //        this.type = type;
    //    }
    //
    //}
}
