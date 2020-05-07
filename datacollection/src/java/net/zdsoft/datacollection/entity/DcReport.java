package net.zdsoft.datacollection.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.transaction.annotation.Transactional;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dc_report")
public class DcReport extends BaseEntity<String> {

	@Column(length = 500)
	private String reportName;
	@Column(length = 32)
	private String unitId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	@Column(length = 32)
	private String createUserId;
	@Column(length = 32)
	private String templateId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Column(length = 9)
	private String acadyear;
	@Column(length = 1)
	private Integer semester;
	@Column(length = 1)
	private Integer state;
	@Column(length = 100)
	private String reportCode;
	@Column(length = 500)
	private String templatePath;
	@Column(length = 500)
	private String listTemplatePath;
	
	@Transient
	private String showColumnNames;
	@Transient
	private String showColumns;
	/**
	 * 1=单位1分数据，2=个人1份数据
	 */
	private Integer reportType;
	
	private String parameter;

	@Override
	public String fetchCacheEntitName() {
		return null;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

	public String getShowColumnNames() {
		return showColumnNames;
	}

	public void setShowColumnNames(String showColumnNames) {
		this.showColumnNames = showColumnNames;
	}

	public String getShowColumns() {
		return showColumns;
	}

	public void setShowColumns(String showColumns) {
		this.showColumns = showColumns;
	}

	public String getListTemplatePath() {
		return listTemplatePath;
	}

	public void setListTemplatePath(String listTemplatePath) {
		this.listTemplatePath = listTemplatePath;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
