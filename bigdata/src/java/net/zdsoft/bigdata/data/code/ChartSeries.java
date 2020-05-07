/**
 * FileName: ChartSeries.java
 * Author:   shenke
 * Date:     2018/5/28 上午10:11
 * Descriptor:
 */
package net.zdsoft.bigdata.data.code;

/**
 * 图表系列
 * 每一个系列下面包含多种分类
 * 目前支持的系列
 * <p>
 *     柱状图: 基本柱状图
 *     折线图: 基本折线图
 *     饼状图: 基本饼状图
 *     散点图: 基本散点图
 * </p>
 * @author shenke
 * @since 2018/5/28 上午10:11
 */
public enum ChartSeries {

    bar {
        @Override
        public String getName() {
            return "柱状图";
        }

        @Override
        public Integer getOrder() {
            return 1;
        }

    },
    pie {
        @Override
        public String getName() {
            return "饼状图";
        }

        @Override
        public Integer getOrder() {
            return 2;
        }
    },
    line {
        @Override
        public String getName() {
            return "折线图";
        }

        @Override
        public Integer getOrder() {
            return 3;
        }
    },
    scatter {
        @Override
        public String getName() {
            return "散点图";
        }

        @Override
        public Integer getOrder() {
            return 4;
        }
    },
    radar {
        @Override
        public String getName() {
            return "雷达图";
        }

        @Override
        public Integer getOrder() {
            return 5;
        }
    },
    map {
        @Override
        public String getName() {
            return "地图";
        }

        @Override
        public Integer getOrder() {
            return 6;
        }
    },

    funnel {
        @Override
        public String getName() {
            return "漏斗图";
        }

        @Override
        public Integer getOrder() {
            return 7;
        }
    },
    gauge{
        @Override
        public String getName() {
            return "仪表盘";
        }

        @Override
        public Integer getOrder() {
            return 8;
        }
    },
    wordCloud {
        @Override
        public String getName() {
            return "字符云";
        }

        @Override
        public Integer getOrder() {
            return 9;
        }
    },
    treeMap{
        @Override
        public String getName() {
            return "矩形树图";
        }

        @Override
        public Integer getOrder() {
            return 10;
        }
    },
    sankey {
        @Override
        public String getName() {
            return "桑基图";
        }

        @Override
        public Integer getOrder() {
            return 11;
        }
    },
    graph {
        @Override
        public String getName() {
            return "关系图";
        }

        @Override
        public Integer getOrder() {
            return 12;
        }
    },
    other {
        @Override
        public String getName() {
            return "其他";
        }

        @Override
        public Integer getOrder() {
            return Integer.MAX_VALUE;
        }
    };

    public abstract String getName();

    public abstract Integer getOrder();
}
