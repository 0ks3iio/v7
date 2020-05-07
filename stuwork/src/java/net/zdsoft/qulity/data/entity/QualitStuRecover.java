package net.zdsoft.qulity.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "quality_stu_recover")
public class QualitStuRecover extends BaseEntity<String> {
    private String unitId;
    private String acadyear;
    private String studentId;
    private Integer hasChange;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getAcadyear() {
        return acadyear;
    }

    public void setAcadyear(String acadyear) {
        this.acadyear = acadyear;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getHasChange() {
        return hasChange;
    }

    public void setHasChange(Integer hasChange) {
        this.hasChange = hasChange;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String fetchCacheEntitName() {
        return "QualitStuRecover";
    }
}
