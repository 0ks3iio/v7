package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: panlf
 * @Date: 2019/6/13 17:49
 */
@Entity
@Table(name = "newdiathesis_project_ex")
public class DiathesisProjectEx extends BaseEntity<String> {
    private String unitId;
    private String projectId;
    /**
     * 评价人分数占比
     */
    private String proportions;
    /**
     * 评价类型
     */
    private String evaluationTypes;

    private String inputTypes;
    private String auditorTypes;
    private Date modifyTime;
    private String operator;

    @Override
    public DiathesisProjectEx clone() {
        DiathesisProjectEx ex = new DiathesisProjectEx();
        ex.setId(this.getId());
        ex.setUnitId(this.getUnitId());
        ex.setEvaluationTypes(this.getEvaluationTypes());
        ex.setAuditorTypes(this.getAuditorTypes());
        ex.setInputTypes(this.getInputTypes());
        ex.setProportions(this.getProportions());
        ex.setModifyTime(this.getModifyTime());
        ex.setProjectId(this.getProjectId());
        ex.setOperator(this.getOperator());
        return ex;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getEvaluationTypes() {
        return evaluationTypes;
    }

    public void setEvaluationTypes(String evaluationTypes) {
        this.evaluationTypes = evaluationTypes;
    }

    public String getProportions() {
        return proportions;
    }

    public void setProportions(String proportions) {
        this.proportions = proportions;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    @Override
    public String fetchCacheEntitName() {
        return null;
    }
}
