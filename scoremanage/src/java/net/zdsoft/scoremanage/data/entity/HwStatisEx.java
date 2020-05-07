package net.zdsoft.scoremanage.data.entity;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 杭外定制成绩统计详细信息
 * @author niuchao
 * @date 2019/11/5 9:57
 */
@Entity
@Table(name = "scoremanage_hw_statis_ex")
public class HwStatisEx extends BaseEntity<String> {
    @ColumnInfo(displayName = "单位id")
    private String unitId;
    @ColumnInfo(displayName = "方案id")
    private String hwPlanId;
    @ColumnInfo(displayName = "01统计,02汇总")
    private String planType;
    @ColumnInfo(displayName = "统计id")
    private String hwStatisId;
    @ColumnInfo(displayName = "科目id或列名code")
    private String objectId;
    @ColumnInfo(displayName = "类型")
    private String objectType;
    @ColumnInfo(displayName = "科目或列名名称")
    private String objectName;
    @ColumnInfo(displayName = "值")
    private String objectVal;
    @ColumnInfo(displayName = "原始分")
    private String score;
    @ColumnInfo(displayName = "原始转换分")
    private String convertScore;
    @ColumnInfo(displayName = "补考分")
    private String makeScore;
    @ColumnInfo(displayName = "总评分")
    private String totalScore;
    @ColumnInfo(displayName = "总评转换分")
    private String totalConvertScore;

    @Transient
    private String classId;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getHwPlanId() {
        return hwPlanId;
    }

    public void setHwPlanId(String hwPlanId) {
        this.hwPlanId = hwPlanId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getHwStatisId() {
        return hwStatisId;
    }

    public void setHwStatisId(String hwStatisId) {
        this.hwStatisId = hwStatisId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectVal() {
        return objectVal;
    }

    public void setObjectVal(String objectVal) {
        this.objectVal = objectVal;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getConvertScore() {
        return convertScore;
    }

    public void setConvertScore(String convertScore) {
        this.convertScore = convertScore;
    }

    public String getMakeScore() {
        return makeScore;
    }

    public void setMakeScore(String makeScore) {
        this.makeScore = makeScore;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getTotalConvertScore() {
        return totalConvertScore;
    }

    public void setTotalConvertScore(String totalConvertScore) {
        this.totalConvertScore = totalConvertScore;
    }

    @Override
    public String fetchCacheEntitName() {
        return "hwStatisEx";
    }
}
