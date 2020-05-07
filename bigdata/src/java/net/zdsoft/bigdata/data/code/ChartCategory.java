/**
 * FileName: ChartCategory.java
 * Author:   shenke
 * Date:     2018/5/28 上午10:10
 * Descriptor:
 */
package net.zdsoft.bigdata.data.code;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType;

/**
 * 图表分类
 * @author shenke
 * @since 2018/5/28 上午10:10
 */
public enum ChartCategory {

    basic_bar {
        @Override
        public String getName() {
            return "基本柱状图";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.BAR_BASIC;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.bar;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.bar;
        }
    },

    stripe_bar {
        @Override
        public String getName() {
            return "条形图";
        }

        @Override
        public Integer getOrder() {
            return 2;
        }

        @Override
        public Integer getChartType() {
            return ChartType.BAR_STRIPE;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.bar;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.bar;
        }
    },

    stack_bar {
        @Override
        public String getName() {
            return "堆叠柱转图";
        }

        @Override
        public Integer getOrder() {
            return 3;
        }

        @Override
        public Integer getChartType() {
            return ChartType.BAR_STACK;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.bar;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.bar;
        }
    },

    stack_stripe_bar{
        @Override
        public String getName() {
            return "堆叠";
        }

        @Override
        public Integer getOrder() {
            return 4;
        }

        @Override
        public Integer getChartType() {
            return ChartType.BAR_STACK_STRIPE;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.bar;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.bar;
        }
    },

    bar_lie {
        @Override
        public String getName() {
            return "复合图";
        }

        @Override
        public Integer getOrder() {
            return 5;
        }

        @Override
        public Integer getChartType() {
            return ChartType.BAR_LINE;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.bar;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.bar;
        }
    },

    bar_2{
        @Override
        public String getName() {
            return "横向";
        }

        @Override
        public Integer getOrder() {
            return 6;
        }

        @Override
        public Integer getChartType() {
            return ChartType.BAR_2_STRIPE;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.bar;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.bar;
        }
    },

    basic_line {
        @Override
        public String getName() {
            return "基本折线图";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.LINE_BROKEN;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.line;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.line;
        }
    },

    area_line {
        @Override
        public String getName() {
            return "面积折线图";
        }

        @Override
        public Integer getOrder() {
            return 2;
        }

        @Override
        public Integer getChartType() {
            return ChartType.LINE_AREA;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.line;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.line;
        }
    },

    basic_pie {
        @Override
        public String getName() {
            return "基本饼状图";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.PIE_BASIC;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.pie;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.pie;
        }
    },

    pie_doughnut {
        @Override
        public String getName() {
            return "环形图";
        }

        @Override
        public Integer getOrder() {
            return 2;
        }

        @Override
        public Integer getChartType() {
            return ChartType.PIE_DOUGHNUT;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.pie;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.pie;
        }
    },

    inner_pie_outer_doughnut {
        @Override
        public String getName() {
            return "复合图形";
        }

        @Override
        public Integer getOrder() {
            return 3;
        }

        @Override
        public Integer getChartType() {
            return ChartType.INNER_PIE_OUTTER_DOUGHNUT;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.pie;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.pie;
        }
    },
    pie_fnf {
        @Override
        public String getName() {
            return "南丁格尔图";
        }

        @Override
        public Integer getOrder() {
            return 4;
        }

        @Override
        public Integer getChartType() {
            return ChartType.PIE_FNF;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.pie;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.pie;
        }
    },
    pie_doughnut_composite{
        @Override
        public String getName() {
            return "多环图";
        }

        @Override
        public Integer getOrder() {
            return 5;
        }

        @Override
        public Integer getChartType() {
            return ChartType.PIE_DOUGHNUT_COMPOSITE;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.pie;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.pie;
        }
    },
    basic_radar {
        @Override
        public String getName() {
            return "基本雷达图";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.RADAR_BASIC;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.radar;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return null;
        }
    }
    ,
    basic_scatter {
        @Override
        public String getName() {
            return "基本散点图";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.SCATTER;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.scatter;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.scatter;
        }
    },

    /** 气泡散点图 */
    //effect_scatter {
    //    @Override
    //    public String getName() {
    //        return "气泡散点图";
    //    }
    //
    //    @Override
    //    public Integer getOrder() {
    //        return 2;
    //    }
    //
    //    @Override
    //    public Integer getChartType() {
    //        return ChartType.SCATTER_EFFECT;
    //    }
    //
    //    @Override
    //    public ChartSeries getChartSeries() {
    //        return ChartSeries.scatter;
    //    }
    //
    //    @Override
    //    public SeriesType getEchartSeriesType() {
    //        return SeriesType.scatter;
    //    }
    //},

    basic_map {
        @Override
        public String getName() {
            return "基本地图";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.MAP;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.map;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.map;
        }
    },

    map_line {
        @Override
        public String getName() {
            return "飞线";
        }

        @Override
        public Integer getOrder() {
            return 2;
        }

        @Override
        public Integer getChartType() {
            return ChartType.MAP_LINE;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.map;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.map;
        }
    },

    /** 正金字塔 */
    funnel {
        @Override
        public String getName() {
            return "漏斗图";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.FUNNEL;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.funnel;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.funnel;
        }
    },

    gauge{
        @Override
        public String getName() {
            return "仪表盘";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.GAUGE;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.gauge;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.gauge;
        }
    },

    //funnel_ascending {
    //    @Override
    //    public String getName() {
    //        return "倒漏斗图";
    //    }
    //
    //    @Override
    //    public Integer getOrder() {
    //        return 2;
    //    }
    //
    //    @Override
    //    public Integer getChartType() {
    //        return ChartType.FUNNEL_ASCENDING;
    //    }
    //
    //    @Override
    //    public ChartSeries getChartSeries() {
    //        return ChartSeries.funnel;
    //    }
    //
    //    @Override
    //    public SeriesType getEchartSeriesType() {
    //        return SeriesType.funnel;
    //    }
    //},

    word_cloud {
        @Override
        public String getName() {
            return "字符云";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.WORD_CLOUD;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.wordCloud;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return SeriesType.wordCloud;
        }
    },

    graph {
        @Override
        public String getName() {
            return "基本关系图";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.GRAPH;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.graph;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return null;
        }
    },
    sankey {
        @Override
        public String getName() {
            return "桑基图";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.SANKEY;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.sankey;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return null;
        }
    },
    tree_map {
        @Override
        public String getName() {
            return "矩形树图";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.TREE_MAP;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.treeMap;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return null;
        }
    },

    dynamic_number{
        @Override
        public String getName() {
            return "翻牌器";
        }

        @Override
        public Integer getOrder() {
            return 3;
        }

        @Override
        public Integer getChartType() {
            return ChartType.DYNAMIC_NUMBER;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.other;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return null;
        }
    },

    indicators{
        @Override
        public String getName() {
            return "指标";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

        @Override
        public Integer getChartType() {
            return ChartType.SELF_NUMBER;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.other;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return null;
        }
    },
    table {
        @Override
        public String getName() {
            return "表格";
        }

        @Override
        public Integer getOrder() {
            return 2;
        }

        @Override
        public Integer getChartType() {
            return ChartType.SELF_TABLE;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.other;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return null;
        }
    } ,
    number_up {
        @Override
        public String getName() {
            return "上升的数字";
        }

        @Override
        public Integer getOrder() {
            return 3;
        }

        @Override
        public Integer getChartType() {
            return ChartType.NUMBER_UP;
        }

        @Override
        public ChartSeries getChartSeries() {
            return ChartSeries.other;
        }

        @Override
        public SeriesType getEchartSeriesType() {
            return null;
        }
    };
    //number_down {
    //    @Override
    //    public String getName() {
    //        return "下降的数字";
    //    }
    //
    //    @Override
    //    public Integer getOrder() {
    //        return 4;
    //    }
    //
    //    @Override
    //    public Integer getChartType() {
    //        return ChartType.NUMBER_DOWN;
    //    }
    //
    //    @Override
    //    public ChartSeries getChartSeries() {
    //        return ChartSeries.other;
    //    }
    //
    //    @Override
    //    public SeriesType getEchartSeriesType() {
    //        return null;
    //    }
    //};


    /** 图表的具体名称 ex：基本柱状图 */
    public abstract String getName();
    /** 排序号（相对每一个系列的排序） 用于页面展示 */
    public abstract Integer getOrder();
    /** chartType 用于EIS控制的图表类型（基本上属于对于Echarts图表类型的细分）包括EIS扩展图表类类型 */
    public abstract Integer getChartType();
    /** 该图表属于哪一个系列 ex：基本柱状图和堆叠柱状图 属于柱状图 */
    public abstract ChartSeries getChartSeries();
    /** 该图表对应echarts的那种Series类型 null 则不属于echarts的图表类型*/
    public abstract SeriesType getEchartSeriesType();

    public String getStatic() {

        //table


        return "";
    }

    private static Map<Integer, ChartCategory> categoryMap;
    static {
        categoryMap =
                Arrays.stream(ChartCategory.values()).collect(Collectors.toMap(ChartCategory::getChartType, c->c));
    }

    public static Optional<ChartCategory> valueFrom(Integer chartType) {
        return Optional.ofNullable(categoryMap.get(chartType));
    }
}
