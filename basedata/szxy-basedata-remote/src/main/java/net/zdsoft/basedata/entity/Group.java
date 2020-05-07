package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_group")
public class Group extends BaseEntity<String>{
    private static final long serialVersionUID = 1L;

    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String createUserId;
    @Column
    private Integer type;
    @Column
    private Date creationTime;
    @Column
    private Date modifyTime;
    @Column
    private String unitId;
    @Column
    private Integer opened;
    @Column
    private Integer displayOrder;

    @Override
    public String fetchCacheEntitName() {
        return this.getClass().getSimpleName();
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the createUserId.
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * @param createUserId
     *            The createUserId to set.
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * @return Returns the type.
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return Returns the creationTime.
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * @param creationTime
     *            The creationTime to set.
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * @return Returns the modifyTime.
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     *            The modifyTime to set.
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return Returns the unitId.
     */
    public String getUnitId() {
        return unitId;
    }

    /**
     * @param unitId
     *            The unitId to set.
     */
    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    /**
     * @return Returns the opened.
     */
    public Integer getOpened() {
        return opened;
    }

    /**
     * @param opened
     *            The opened to set.
     */
    public void setOpened(Integer opened) {
        this.opened = opened;
    }

    /**
     * @return Returns the displayOrder.
     */
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    /**
     * @param displayOrder
     *            The displayOrder to set.
     */
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

}
