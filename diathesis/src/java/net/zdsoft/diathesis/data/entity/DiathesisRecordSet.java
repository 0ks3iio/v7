package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 *
 * 和poject表中的id是同一个id
 * 写实记录的赋分 设置
 *
 * @Author: panlf
 * @Date: 2019/8/8 17:37
 */
@Entity
@Table(name = "newdiathesis_record_set")
public class DiathesisRecordSet extends BaseEntity<String> {
   // 和poject表中projectType='3'(写实项目) 的id是同一个id

    /**
     * 单位id
     */
    private String unitId;
    /**
     * 赋分依据  0:按次数加分    1: 按单选加
     */
    private String scoreType;
    /**
     * 学期最高得分
     */
    private String semesterMaxScore;
    /**
     * 在校最高得分
     */
    private String allMaxScore;
    /**
     * 一次写实的得分
     */
    private String score;

    /**
     * 统计类型  0:逐条展示    1:按统计次数展示
     */
    private String countType;

    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 操作人
     */
    private String operator;

    private String scoreStructureId;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getScoreStructureId() {
        return scoreStructureId;
    }

    public void setScoreStructureId(String scoreStructureId) {
        this.scoreStructureId = scoreStructureId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public String getSemesterMaxScore() {
        return semesterMaxScore;
    }

    public void setSemesterMaxScore(String semesterMaxScore) {
        this.semesterMaxScore = semesterMaxScore;
    }

    public String getAllMaxScore() {
        return allMaxScore;
    }

    public void setAllMaxScore(String allMaxScore) {
        this.allMaxScore = allMaxScore;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCountType() {
        return countType;
    }

    public void setCountType(String countType) {
        this.countType = countType;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
