package net.zdsoft.exammanage.data.entity;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.exammanage.data.entity
 * @ClassName: EmStatCrossEntity
 * @Description: 交叉报表实体类
 * @Author: Sweet
 * @CreateDate: 2018/8/22 16:26
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/8/22 16:26
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class EmStatCrossEntity {
    private String columnOrder;
    private String rowName;
    private String rowOrder;
    private String value;
    private String columnName;

    public String getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(String columnOrder) {
        this.columnOrder = columnOrder;
    }

    public String getRowName() {
        return rowName;
    }

    public void setRowName(String rowName) {
        this.rowName = rowName;
    }

    public String getRowOrder() {
        return rowOrder;
    }

    public void setRowOrder(String rowOrder) {
        this.rowOrder = rowOrder;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
