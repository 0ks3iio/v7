package net.zdsoft.bigdata.data.enu;

/**
 * Created by wangdongdong on 2019/2/26 14:47.
 */
public enum BatchOperateType {

    /**
     * 图表
     */
    CHART("图表", "chart"),
    /**
     * 报表
     */
    REPORT("报表", "report"),
    /**
     * 大屏
     */
    SCREEN("大屏", "screen"),
    /**
     * 数据模型
     */
    DATA_MODEL("数据模型管理", "model"),
    /**
     * 多维报表
     */
    DATA_MODEL_FAVORITE("多维报表设计", "modelChart"),
    /**
     * 数据看板
     */
    DATA_BOARD("数据看板设置", "board"),
    /**
     * 数据报告
     */
    DATA_REPORT("数据报告设置", "data_report");

    private String name;

    private String value;

    BatchOperateType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static BatchOperateType valueOfType(String value) {
        for (BatchOperateType e : BatchOperateType.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }

}
