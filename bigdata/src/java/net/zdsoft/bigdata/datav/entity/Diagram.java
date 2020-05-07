package net.zdsoft.bigdata.datav.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 10:44
 */
@Entity
@Table(name = "bg_diagram")
public class Diagram extends AbstractDiagram {

    /**
     * 大屏id
     */
    private String screenId;

    //private String layerGroupId;

    @Column(name = "is_lock")
    private Integer lock;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    /**
     * TODO 这里不能使用diagrmaId 我怀疑和重写字段和数据库的影射有关
     */
    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "diagram_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<DiagramParameter> parameters;

    public Integer getLock() {
        return lock;
    }

    public void setLock(Integer lock) {
        this.lock = lock;
    }

    public List<DiagramParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<DiagramParameter> parameters) {
        this.parameters = parameters;
    }

}
