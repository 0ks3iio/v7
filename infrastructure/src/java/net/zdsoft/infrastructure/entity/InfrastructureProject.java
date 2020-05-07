package net.zdsoft.infrastructure.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by luf on 2018/11/14.
 */
@Table
@Entity(name="infrastr_project")
public class InfrastructureProject extends BaseEntity<String> {

    private String projectSchool;
    private String projectName;
    private String projectNature;
    private String surveyUnit;
    private String surveyingUnit;
    private String renderingDesignUnit;
    private String drawingDesignUnit;
    private String drawingReviewUnit;
    private String costUnit;
    private String controlPrice;
    private String agency;
    private String supervisoryUnit;
    private String constructionCompany;
    private String contractPrice;
    private Date constructionTime;
    private String projectProgress;
    private String auditUnit;
    private String pricing;
    private String unitId;
    private Date creationTime;
    private Date modifyTime;

    public String getProjectSchool() {
        return projectSchool;
    }

    public void setProjectSchool(String projectSchool) {
        this.projectSchool = projectSchool;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectNature() {
        return projectNature;
    }

    public void setProjectNature(String projectNature) {
        this.projectNature = projectNature;
    }

    public String getSurveyUnit() {
        return surveyUnit;
    }

    public void setSurveyUnit(String surveyUnit) {
        this.surveyUnit = surveyUnit;
    }

    public String getSurveyingUnit() {
        return surveyingUnit;
    }

    public void setSurveyingUnit(String surveyingUnit) {
        this.surveyingUnit = surveyingUnit;
    }

    public String getRenderingDesignUnit() {
        return renderingDesignUnit;
    }

    public void setRenderingDesignUnit(String renderingDesignUnit) {
        this.renderingDesignUnit = renderingDesignUnit;
    }

    public String getDrawingDesignUnit() {
        return drawingDesignUnit;
    }

    public void setDrawingDesignUnit(String drawingDesignUnit) {
        this.drawingDesignUnit = drawingDesignUnit;
    }

    public String getDrawingReviewUnit() {
        return drawingReviewUnit;
    }

    public void setDrawingReviewUnit(String drawingReviewUnit) {
        this.drawingReviewUnit = drawingReviewUnit;
    }

    public String getCostUnit() {
        return costUnit;
    }

    public void setCostUnit(String costUnit) {
        this.costUnit = costUnit;
    }

    public String getControlPrice() {
        return controlPrice;
    }

    public void setControlPrice(String controlPrice) {
        this.controlPrice = controlPrice;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getSupervisoryUnit() {
        return supervisoryUnit;
    }

    public void setSupervisoryUnit(String supervisoryUnit) {
        this.supervisoryUnit = supervisoryUnit;
    }

    public String getConstructionCompany() {
        return constructionCompany;
    }

    public void setConstructionCompany(String constructionCompany) {
        this.constructionCompany = constructionCompany;
    }

    public String getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(String contractPrice) {
        this.contractPrice = contractPrice;
    }

    public Date getConstructionTime() {
        return constructionTime;
    }

    public void setConstructionTime(Date constructionTime) {
        this.constructionTime = constructionTime;
    }

    public String getProjectProgress() {
        return projectProgress;
    }

    public void setProjectProgress(String projectProgress) {
        this.projectProgress = projectProgress;
    }

    public String getAuditUnit() {
        return auditUnit;
    }

    public void setAuditUnit(String auditUnit) {
        this.auditUnit = auditUnit;
    }

    public String getPricing() {
        return pricing;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
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

    @Override
    public String fetchCacheEntitName() {
        return "infrastructureProject";
    }
}
