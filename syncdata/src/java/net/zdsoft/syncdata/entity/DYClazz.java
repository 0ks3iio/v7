package net.zdsoft.syncdata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_class_relation")
public class DYClazz extends BaseEntity<String> {

    @Column(updatable = false)
    private String fixClassId;

    @Override
    public String fetchCacheEntitName() {
        return "dyClazz";
    }

    public String getFixClassId() {
        return fixClassId;
    }

    public void setFixClassId(String fixClassId) {
        this.fixClassId = fixClassId;
    }

}
