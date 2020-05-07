package net.zdsoft.bigdata.metadata.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件夹详情图标
 * Created by wangdongdong on 2019/2/25 09:25.
 */
public enum FolderDetailImg {


    /**
     * 图表
     */
    CHART(1, "img-data-chart.png"),

    /**
     * 数据面板
     */
    DATA_BOARD(6, "img-kanban.png"),

    /**
     * 数据报告
     */
    DATA_REPORT(7, "img-data-report.png"),

    /**
     * 数据报表
     */
    REPORT(3, "img-report.png"),

    /**
     * 多维报表
     */
    MULTIREPORT(5, "img-multi-report.png");

    private Integer type;

    private String value;

    FolderDetailImg(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static Map<String, String> toMap() {
        Map<String, String> fMap = new HashMap<>();
        for (FolderDetailImg fi : FolderDetailImg.values()) {
            fMap.put(fi.type.toString(), fi.value);
        }
        return fMap;
    }
}
