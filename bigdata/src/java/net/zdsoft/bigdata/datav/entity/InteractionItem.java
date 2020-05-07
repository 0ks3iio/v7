package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 记录图表、图表组件交互默认的参数
 * @author shenke
 * @since 2018/10/25 上午11:15
 */
@Entity
@Table(name = "bg_interaction_item")
public class InteractionItem extends BaseEntity<String> {

    /**
     * 图表或者图表组件类型
     */
    private Integer diagramType;
    /**
     * 交互参数的名称
     */
    private String key;
    /**
     * 绑定到其他图表或者组件上的key的名称
     */
    private String bindKey;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public Integer getDiagramType() {
        return diagramType;
    }

    public void setDiagramType(Integer diagramType) {
        this.diagramType = diagramType;
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
