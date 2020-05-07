package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: panlf
 * @Date: 2019/8/14 19:20
 */
@Entity
@Table(name = "newdiathesis_score_info_ex")
public class DiathesisScoreInfoEx  extends BaseEntity<String> {

    private String scoreInfoId;

    private String fieldCode;

    private String fieldValue;

    private String scoreTypeId;

    private Date modifyTime;

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getScoreTypeId() {
        return scoreTypeId;
    }

    public void setScoreTypeId(String scoreTypeId) {
        this.scoreTypeId = scoreTypeId;
    }

    public String getScoreInfoId() {
        return scoreInfoId;
    }

    public void setScoreInfoId(String scoreInfoId) {
        this.scoreInfoId = scoreInfoId;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public String fetchCacheEntitName() {
        return null;
    }
}
