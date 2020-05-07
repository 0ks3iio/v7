package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exammanage_limit_detail")
public class EmLimitDetail extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String limitId;
    private String classId;
    private String classType;

    @Override
    public String fetchCacheEntitName() {
        return "emLimitDetail";
    }

    public String getLimitId() {
        return limitId;
    }

    public void setLimitId(String limitId) {
        this.limitId = limitId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

}
