package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 间距
 */
@Entity
@Table(name = "exammanage_space_item")
public class EmSpaceItem extends BaseEntity<String> {

    public static final String PARM_SPACE = "1";//间距
    public static final String PARM_PERCENT_F = "2";//前百分比
    public static final String PARM_PERCENT_B = "3";//后百分比
    public static final String PARM_CLASS_RANK = "4";//名次段
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String statParmId;
    private Float lowScore;
    private Float upScore;
    private String name;
    private String parmType;

    public String getStatParmId() {
        return statParmId;
    }

    public void setStatParmId(String statParmId) {
        this.statParmId = statParmId;
    }

    public Float getLowScore() {
        return lowScore;
    }

    public void setLowScore(Float lowScore) {
        this.lowScore = lowScore;
    }

    public Float getUpScore() {
        return upScore;
    }

    public void setUpScore(Float upScore) {
        this.upScore = upScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParmType() {
        return parmType;
    }

    public void setParmType(String parmType) {
        this.parmType = parmType;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emSpaceItem";
    }

}
