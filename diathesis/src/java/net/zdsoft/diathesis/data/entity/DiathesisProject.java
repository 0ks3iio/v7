package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: panlf
 * @Date: 2019/3/27 17:55
 */
@Entity
@Table(name="newdiathesis_project")
public class DiathesisProject extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 单位id
	 */
	private String unitId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目类型
     */
    private String projectType;
    /**
     * 排序值
     */
    private Integer sortNumber;
    /**
     * 评价类型
     */
    @Transient
    private String evaluationTypes;
    /**
     * 评价占比
     */
    @Transient
    private String proportions;
    /**
     * 备注
     */
    private String remark;
    /**
     * 上一级id
     */
    private String parentId;
    /**
     * 审核人类型
     */
    @Transient
    private String auditorTypes;
    /**
     * 录入人类型
     */
    @Transient
    private String inputTypes;
    /**
     * 修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    /**
     * 操作人
     */
    private String operator;
    private String groupId;
    /**
     * 一类项目占比
     */
    private Integer topProp;


    public Integer getTopProp() {
        return topProp;
    }

    public void setTopProp(Integer topProp) {
        this.topProp = topProp;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAuditorTypes() {
        return auditorTypes;
    }

    public void setAuditorTypes(String auditorTypes) {
        this.auditorTypes = auditorTypes;
    }

    public String getInputTypes() {
        return inputTypes;
    }

    public void setInputTypes(String inputTypes) {
        this.inputTypes = inputTypes;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public DiathesisProject clone(){
        DiathesisProject project = new DiathesisProject();
        project.setId(this.getId());
        project.setUnitId(this.getUnitId());
        project.setProjectType(this.getProjectType());
        project.setProjectName(this.getProjectName());
        project.setSortNumber(this.getSortNumber());
        project.setEvaluationTypes(this.getEvaluationTypes());
        project.setProportions(this.getProportions());
        project.setRemark(this.getRemark());
        project.setParentId(this.getParentId());
        project.setAuditorTypes(this.getAuditorTypes());
        project.setInputTypes(this.getInputTypes());
        project.setModifyTime(this.getModifyTime());
        project.setOperator(this.getOperator());
        project.setGroupId(this.getGroupId());
        project.setTopProp(this.getTopProp());
        return project;
    }



    @Override
    public String fetchCacheEntitName() {
        return "diathesisProject";
    }
}
