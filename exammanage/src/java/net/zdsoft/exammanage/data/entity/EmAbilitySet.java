package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exammanage_ability_set")
public class EmAbilitySet extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = 100001L;
    private String statObjectId;

    private String statParmId;

    private String subjectId;
    /**
     * 权重
     */
    private Float weights;

    private String examId;


    public String getStatObjectId() {
        return statObjectId;
    }

    public void setStatObjectId(String statObjectId) {
        this.statObjectId = statObjectId;
    }

    public String getStatParmId() {
        return statParmId;
    }

    public void setStatParmId(String statParmId) {
        this.statParmId = statParmId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Float getWeights() {
        return weights;
    }

    public void setWeights(Float weights) {
        this.weights = weights;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    @Override
    public String fetchCacheEntitName() {
        return "getEmAbilitySet";
    }

}
