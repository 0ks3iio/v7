package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "exammanage_stat_space")
public class EmStatSpace extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String statRangeId;
    private String spaceItemId;
    private String statObjectId;
    private String scoreNum;
    @Transient
    private String spaceItemName;
    @Transient
    private Float spaceItemUpScore;

    @Transient
    private String className;
    private String isCon;//是否赋分等级统计(单科统计，选考科目使用)
    private int scoreRank;//赋分等级
    private Float blance;//人数比例

    public String getIsCon() {
        return isCon;
    }

    public void setIsCon(String isCon) {
        this.isCon = isCon;
    }

    public String getSpaceItemId() {
        return spaceItemId;
    }

    public void setSpaceItemId(String spaceItemId) {
        this.spaceItemId = spaceItemId;
    }

    public int getScoreRank() {
        return scoreRank;
    }

    public void setScoreRank(int scoreRank) {
        this.scoreRank = scoreRank;
    }

    public Float getBlance() {
        return blance;
    }

    public void setBlance(Float blance) {
        this.blance = blance;
    }

    public String getStatObjectId() {
        return statObjectId;
    }

    public void setStatObjectId(String statObjectId) {
        this.statObjectId = statObjectId;
    }

    public String getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(String scoreNum) {
        this.scoreNum = scoreNum;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emStatSpace";
    }

    public String getStatRangeId() {
        return statRangeId;
    }

    public void setStatRangeId(String statRangeId) {
        this.statRangeId = statRangeId;
    }

    public String getSpaceItemName() {
        return spaceItemName;
    }

    public void setSpaceItemName(String spaceItemName) {
        this.spaceItemName = spaceItemName;
    }

    public Float getSpaceItemUpScore() {
        return spaceItemUpScore;
    }

    public void setSpaceItemUpScore(Float spaceItemUpScore) {
        this.spaceItemUpScore = spaceItemUpScore;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getItemIdAndStatRangeId() {
        return statRangeId + spaceItemId;
    }
}
