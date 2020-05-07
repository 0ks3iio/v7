package net.zdsoft.credit.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exammanage_credit_daily_set")
public class CreditDailySet extends BaseEntity<String> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4930388623256590431L;

    private String setId;
    private String name;
    private String parentId;
    private Float score;
    private String scoreType;
    @Transient
    private String isParent;

    @Transient
    private List<CreditDailySet> subSetList = new ArrayList<>();

    public List<CreditDailySet> getSubSetList() {
        return subSetList;
    }

    public void setSubSetList(List<CreditDailySet> subSetList) {
        this.subSetList = subSetList;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    @Override
    public String fetchCacheEntitName() {
        return "getCreditDailySet";
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }
}
