package net.zdsoft.credit.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exammanage_credit_set")
public class CreditSet extends BaseEntity<String> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4735606352964824736L;
    private String unitId;
    private String acadyear;
    private String semester;
    private Float usualScore;//平时成绩
    private Float moduleScore;//模块成绩
    private Float dailyScore;//日常表现
    private Float passLine;//及格线
    private float sumScore;//总分

    @Transient
    private List<CreditDailySet> dailySetList = new ArrayList<>();

    public List<CreditDailySet> getDailySetList() {
        return dailySetList;
    }

    public void setDailySetList(List<CreditDailySet> dailySetList) {
        this.dailySetList = dailySetList;
    }

    public String getUnitId() {
        return unitId;
    }


    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }


    public String getAcadyear() {
        return acadyear;
    }


    public void setAcadyear(String acadyear) {
        this.acadyear = acadyear;
    }


    public String getSemester() {
        return semester;
    }


    public void setSemester(String semester) {
        this.semester = semester;
    }


    public Float getUsualScore() {
        return usualScore;
    }


    public void setUsualScore(Float usualScore) {
        this.usualScore = usualScore;
    }


    public Float getModuleScore() {
        return moduleScore;
    }


    public void setModuleScore(Float moduleScore) {
        this.moduleScore = moduleScore;
    }


    public Float getDailyScore() {
        return dailyScore;
    }


    public void setDailyScore(Float dailyScore) {
        this.dailyScore = dailyScore;
    }


    public Float getPassLine() {
        return passLine;
    }


    public void setPassLine(Float passLine) {
        this.passLine = passLine;
    }


    public float getSumScore() {
        return sumScore;
    }


    public void setSumScore(float sumScore) {
        this.sumScore = sumScore;
    }


    @Override
    public String fetchCacheEntitName() {
        return "getCreditSet";
    }
}
