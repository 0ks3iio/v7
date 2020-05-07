/**
 * FileName: ChartBusinessType
 * Author:   shenke
 * Date:     2018/4/24 上午9:45
 * Descriptor:
 */
package net.zdsoft.bigdata.data;

/**
 * @author shenke
 * @since 2018/4/24 上午9:45
 */
public enum ChartBusinessType {

    /** 大屏 */
    COCKPIT {
        @Override
        public Integer getBusinessType() {
            return 2;
        }
    },
    /** 基本图表 */
    CHART {
        @Override
        public Integer getBusinessType() {
            return 1;
        }
    },
    /** 报表 */
    REPORT {
        @Override
        public Integer getBusinessType() {
            return 3;
        }
    },
    COCKPIT_CREATE_CHART {
        @Override
        public Integer getBusinessType() {
            return 4;
        }
    },
    /** 多维报表 */
    MODEL_REPORT {
        @Override
        public Integer getBusinessType() {
            return 5;
        }
    },

    /** 数据看板 */
    DATA_BOARD {
        @Override
        public Integer getBusinessType() {
            return 6;
        }
    },

    /** 数据报告 */
    DATA_REPORT {
        @Override
        public Integer getBusinessType() {
            return 7;
        }
    };

    public abstract Integer getBusinessType();

    public static ChartBusinessType valueOfBusinessType(Integer businessType) {
        for (ChartBusinessType type : ChartBusinessType.values()) {
            if (businessType == type.getBusinessType()) {
                return type;
            }
        }
        return null;
    }
}
