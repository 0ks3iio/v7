package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 记录哪个图表开启了交互
 * @author shenke
 * @since 2018/10/26 上午9:57
 */
@Entity
@Table(name = "bg_interaction_active")
public class InteractionActive extends BaseEntity<String> {

    private String elementId;
    /**
     * 是否启用
     */
    @Enumerated
    private Active active;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public Active getActive() {
        return active;
    }

    public void setActive(Active active) {
        this.active = active;
    }
}
