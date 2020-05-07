package net.zdsoft.bigdata.dataimport.entity;


import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="bg_da_custom_table_column")
public class BgDaCustomTableColumn extends BaseEntity<String> {

  private String tableId;
  private String columnName;
  private String columnType;
  private String orderId;



  public String getTableId() {
    return tableId;
  }

  public void setTableId(String tableId) {
    this.tableId = tableId;
  }


  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }


  public String getColumnType() {
    return columnType;
  }

  public void setColumnType(String columnType) {
    this.columnType = columnType;
  }


  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  @Override
  public String fetchCacheEntitName() {
    return null;
  }
}
