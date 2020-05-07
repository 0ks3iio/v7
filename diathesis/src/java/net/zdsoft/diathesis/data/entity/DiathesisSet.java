package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Author: panlf
 * @Date: 2019/3/27 18:04
 */
@Entity
@Table(name="newdiathesis_set")
public class DiathesisSet extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

    private String unitId;
    /**
     * 等第项
     */
    private String rankItems;
    /**
     * 评价值录入形式  等第/分值
     */
    private String inputValueType;
    /**
     * 等第对应值
     */
    private String rankValues;
    /**
     * 录入人类型
     */
    private String inputTypes;
    /**
     * 审核人类型
     */
    private String auditorTypes;
    /**
     * 平均分范围
     */
    private String scoreScopes;
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    /**
     * 修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    /**
     * 软删  0:正常   1:删除
     */
    private Integer isDeleted;
    /**
     * 操作人
     */
    private String operator;

    /**
     *  分数类型 S:分数范围制  P:分数比例制
     */
    private String scoreType;

    /**
     *  学生互评 类型   L:组长统一录入  M:组内人员相互评价
     */
    private String mutualType;

    /**
     *  设置评价人类型 (不涉及权限,自己单位使用)
     */
    private String evaluation;

    /**
     *   0:不赋分  1:赋分
     * @return
     */
    private String isAssignPoint ;

    /**
     * 学期分数占比
     */
    private String semesterScoreProp;

    @Override
    public DiathesisSet clone() {
        DiathesisSet set = new DiathesisSet();
        set.setId(this.getId());
        set.setUnitId(this.getUnitId());
        set.setRankItems(this.getRankItems());
        set.setInputValueType(this.getInputValueType());
        set.setRankValues(this.getRankValues());
        set.setInputTypes(this.getInputTypes());
        set.setAuditorTypes(this.getAuditorTypes());
        set.setScoreScopes(this.getScoreScopes());
        set.setCreationTime(this.getCreationTime());
        set.setModifyTime(this.getModifyTime());
        set.setIsDeleted(this.getIsDeleted());
        set.setOperator(this.getOperator());
        set.setScoreType(this.getScoreType());
        set.setMutualType(this.getMutualType());
        set.setEvaluation(this.getEvaluation());
        set.setIsAssignPoint(this.getIsAssignPoint());
        set.setSemesterScoreProp(this.getSemesterScoreProp());
        return set;
    }

    public String getSemesterScoreProp() {
        return semesterScoreProp;
    }

    public void setSemesterScoreProp(String semesterScoreProp) {
        this.semesterScoreProp = semesterScoreProp;
    }

    public String getIsAssignPoint() {
        return isAssignPoint;
    }

    public void setIsAssignPoint(String isAssignPoint) {
        this.isAssignPoint = isAssignPoint;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getMutualType() {
        return mutualType;
    }

    public void setMutualType(String mutualType) {
        this.mutualType = mutualType;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getRankItems() {
        return rankItems;
    }

    public void setRankItems(String rankItems) {
        this.rankItems = rankItems;
    }

    public String getInputValueType() {
        return inputValueType;
    }

    public void setInputValueType(String inputValueType) {
        this.inputValueType = inputValueType;
    }

    public String getRankValues() {
        return rankValues;
    }

    public void setRankValues(String rankValues) {
        this.rankValues = rankValues;
    }

    public String getInputTypes() {
        return inputTypes;
    }

    public void setInputTypes(String inputTypes) {
        this.inputTypes = inputTypes;
    }

    public String getAuditorTypes() {
        return auditorTypes;
    }

    public void setAuditorTypes(String auditorTypes) {
        this.auditorTypes = auditorTypes;
    }

    public String getScoreScopes() {
        return scoreScopes;
    }

    public void setScoreScopes(String scoreScopes) {
        this.scoreScopes = scoreScopes;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String fetchCacheEntitName() {
        return "diathesisSet";
    }
}
