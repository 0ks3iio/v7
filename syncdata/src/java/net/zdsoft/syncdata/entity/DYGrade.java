package net.zdsoft.syncdata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_grade_relation")
public class DYGrade extends BaseEntity<String> {

    @Column(updatable = false)
    private String fixGradeId;

    @Override
    public String fetchCacheEntitName() {
        return "dyGrade";
    }

    public String getFixGradeId() {
        return fixGradeId;
    }

    public void setFixGradeId(String fixGradeId) {
        this.fixGradeId = fixGradeId;
    }

}
