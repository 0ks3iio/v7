package net.zdsoft.bigdata.data.action.convert.echarts.enu;

/**
 * @author ke_shen@126.com
 * @since 2018/4/11 下午8:20
 */
public enum SeriesType {

    line {
        @Override
        public String showName() {
            return "线状图";
        }
        @Override
        public String category() {
            return this.name();
        }
    },
    pie {
        @Override
        public String showName() {
            return "饼状图";
        }

        @Override
        public String category() {
            return this.name();
        }
    },
    bar {
        @Override
        public String showName() {
            return "柱状图";
        }
        @Override
        public String category() {
            return this.name();
        }
    },
    scatter {
        @Override
        public String showName() {
            return "散点图";
        }
        @Override
        public String category() {
            return this.name();
        }
    },
    map {
        @Override
        public String showName() {
            return "地图";
        }
        @Override
        public String category() {
            return this.name();
        }
    },
    radar {
        @Override
        public String showName() {
            return "雷达图";
        }

        @Override
        public String category() {
            return this.name();
        }
    },
    /** 主要用于地图上 */
    lines {
        @Override
        public String showName() {
            return "飞线";
        }

        @Override
        public String category() {
            return "lines";
        }
    },
    effectScatter {
        @Override
        public String showName() {
            return "涟漪气泡";
        }

        @Override
        public String category() {
            return "effectScatter";
        }
    },
    funnel {
        @Override
        public String showName() {
            return "漏斗图";
        }

        @Override
        public String category() {
            return "funnel";
        }
    },

    wordCloud {
        @Override
        public String showName() {
            return "字符云";
        }

        @Override
        public String category() {
            return "wordCloud";
        }
    },

    gauge{
        @Override
        public String showName() {
            return "仪表盘";
        }

        @Override
        public String category() {
            return this.name();
        }
    },

    /* 一下类型为eis自扩展类型，为了大屏展示 */

    number {
        @Override
        public String showName() {
            return "其他";
        }
        @Override
        public String category() {
            return "other";
        }
    },
    table {
        @Override
        public String showName() {
            return "其他";
        }
        @Override
        public String category() {
            return "other";
        }
    };

    public abstract String showName();

    public abstract String category();
}
