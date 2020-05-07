package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author: panlf
 * @Date: 2019/8/15 14:11
 */
@Entity
@Table(name="NEWDIATHESIS_IMPORT_STATUS")
public class DiathesisImportStatus extends BaseEntity<String> {

    private String unitId;
    private String subjectId;
    private String fieldId;
    //0没有 1:导入完毕
    private String importStatus;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }

    @Override
    public String fetchCacheEntitName() {
        return null;
    }
}
