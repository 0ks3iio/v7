package net.zdsoft.bigdata.datav.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author shenke
 * @since 2018/9/26 11:13
 * 组件收藏表（组件库）
 */
@Entity
@Table(name = "bg_diagram_library")
public class DiagramLibrary extends AbstractDiagram{
    /**
     * 拥有者
     */
    private String userId;
    /**
     * 名称
     */
    private String name;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
