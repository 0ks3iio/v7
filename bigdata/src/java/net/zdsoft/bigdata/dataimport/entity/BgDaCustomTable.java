package net.zdsoft.bigdata.dataimport.entity;


import com.alibaba.fastjson.annotation.JSONField;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="bg_da_custom_table")
public class BgDaCustomTable extends BaseEntity<String> {

  private String unitId;
  private String userId;
  private String name;
  private String tableName;
  private String source;
  private String remark;
  private String dataNum;
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date creationTime;
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date modifyTime;

  @Override
  public String fetchCacheEntitName() {
    return null;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getDataNum() {
    return dataNum;
  }

  public void setDataNum(String dataNum) {
    this.dataNum = dataNum;
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public Date getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }
}
