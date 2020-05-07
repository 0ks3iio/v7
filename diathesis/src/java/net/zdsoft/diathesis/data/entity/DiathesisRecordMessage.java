package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: panlf
 * @Date: 2019/10/29 9:47
 */
@Entity
@Table(name = "newdiathesis_record_message")
public class DiathesisRecordMessage extends BaseEntity<String> {
    private String unitId;
    private String recordId;
    private String leaveMsgPeopleId;
    private Date modifyTime;
    private String content;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getLeaveMsgPeopleId() {
        return leaveMsgPeopleId;
    }

    public void setLeaveMsgPeopleId(String leaveMsgPeopleId) {
        this.leaveMsgPeopleId = leaveMsgPeopleId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String fetchCacheEntitName() {
        return null;
    }
}
