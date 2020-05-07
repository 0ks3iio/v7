package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "exammanage_title_info")
public class EmTitleInfo extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;
    private String unitId;
    private String examId;
    private int titleCode;
    //k表示客观题，z表示主观题
    private String titleType;
    private String subjectId;
    private float totalScore;
    private Date creationTime;

    @Transient
    private String titleName;//字段名

    @Override
    public String fetchCacheEntitName() {
        return "emTitleInfo";
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public int getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(int titleCode) {
        this.titleCode = titleCode;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
}
