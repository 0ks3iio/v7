package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="NEWDIATHESIS_SUBJECT_FIELD")
public class DiathesisSubjectField extends BaseEntity<String> {


  private String unitId;

  private String subjectType;
  private String fieldName;
  private Integer sortNum;
  private Date modifyTime;
  private String fieldCode;
  private String isUsing;


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

  public String getSubjectType() {
    return subjectType;
  }

  public void setSubjectType(String subjectType) {
    this.subjectType = subjectType;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public Integer getSortNum() {
    return sortNum;
  }

  public void setSortNum(Integer sortNum) {
    this.sortNum = sortNum;
  }

  public Date getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }

  public String getFieldCode() {
    return fieldCode;
  }

  public void setFieldCode(String fieldCode) {
    this.fieldCode = fieldCode;
  }

  public String getIsUsing() {
    return isUsing;
  }

  public void setIsUsing(String isUsing) {
    this.isUsing = isUsing;
  }
}
