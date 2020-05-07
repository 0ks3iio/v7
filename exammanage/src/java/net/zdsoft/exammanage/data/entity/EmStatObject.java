package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 统计对象(同一场考试，教育局可以统计，学校也可以统计 只是学校只能统计自己的)
 */
@Entity
@Table(name = "exammanage_stat_object")
public class EmStatObject extends BaseEntity<String> {

    public static final String STAT_BELONG = "1";//作为考试建立方总统计
    public static final String STAT_NOTBELONG = "2";//不是考试建立方 学校自行统计
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String examId;
    private String unitId;
    private String statType;//统计类型 1：作为考试建立方总统计 2：不是考试建立方 学校自行统计
    private String isStat;

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getStatType() {
        return statType;
    }

    public void setStatType(String statType) {
        this.statType = statType;
    }

    public String getIsStat() {
        return isStat;
    }

    public void setIsStat(String isStat) {
        this.isStat = isStat;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emStatObject";
    }

}
