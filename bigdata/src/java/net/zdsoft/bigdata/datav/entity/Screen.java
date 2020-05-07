package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 10:43
 */
@Entity
@Table(name = "bg_screen")
public class Screen extends BaseEntity<String> {

    private String unitId;
    @Column(length = 2)
    private Integer orderType;
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    /**
     * 创建者
     */
    private String createUserId;
    /**
     * 个性化大屏地址
     */
    private String url;

    //@OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    //@JoinColumn(name = "screen_Id", referencedColumnName = "id", insertable = false, updatable = false)
    //private List<DiagramLayerGroup> diagramLayerGroups = new ArrayList<>();

    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "screen_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<Diagram> diagrams;

    @Override
    public String fetchCacheEntitName() {
        return "";
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Diagram> getDiagrams() {
        return diagrams;
    }

    public void setDiagrams(List<Diagram> diagrams) {
        this.diagrams = diagrams;
    }
}
