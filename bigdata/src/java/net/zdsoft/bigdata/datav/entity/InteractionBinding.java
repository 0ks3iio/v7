package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 记录哪一个图表实际的交互参数
 * @author shenke
 * @since 2018/10/25 下午6:35
 */
@Entity
@Table(name = "bg_interaction_binding")
public class InteractionBinding extends BaseEntity<String> {

    private String elementId;
    private String key;
    private String bindKey;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBindKey() {
        return bindKey;
    }

    public void setBindKey(String bindKey) {
        this.bindKey = bindKey;
    }
}
