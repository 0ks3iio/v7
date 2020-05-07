package net.zdsoft.bigdata.dataAnalysis.vo;

import java.io.Serializable;

/**
 * Created by wangdongdong on 2019/6/20 18:40.
 */
public class ReportTableVO implements Serializable {


    private String rowHeader;

    private String columnHeader;

    private String table;

    public String getRowHeader() {
        return rowHeader;
    }

    public void setRowHeader(String rowHeader) {
        this.rowHeader = rowHeader;
    }

    public String getColumnHeader() {
        return columnHeader;
    }

    public void setColumnHeader(String columnHeader) {
        this.columnHeader = columnHeader;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
