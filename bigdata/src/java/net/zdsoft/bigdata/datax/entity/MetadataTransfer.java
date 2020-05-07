package net.zdsoft.bigdata.datax.entity;

import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;

import java.util.List;

/**
 * Created by wangdongdong on 2019/6/18 14:15.
 */
public class MetadataTransfer {

    private String tableName;

    private List<MetadataTableColumn> columnList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<MetadataTableColumn> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<MetadataTableColumn> columnList) {
        this.columnList = columnList;
    }
}
