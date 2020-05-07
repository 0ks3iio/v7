package net.zdsoft.stutotality.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "stutotality_code")
public class StutotalityCode extends BaseEntity<String> {

    private static final long serialVersionUID = 13456789432171L;

    private String unitId;

    private String itemId;//由于不与年级有关联且有不是学科的项目，此时的itemId已无实际性作用。直接通过名称匹配

    private String itemName;//新加字段，匹配二维码的作用

    private String name;

    private Float score;
    /**
     * 1二维码 2项目缩写的logo
     */
    private Integer type;

    private Integer hasUsing;

    private Date creationTime;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getHasUsing() {
        return hasUsing;
    }

    public void setHasUsing(Integer hasUsing) {
        this.hasUsing = hasUsing;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String fetchCacheEntitName() {
        return "StutotalityCode";
    }
}
