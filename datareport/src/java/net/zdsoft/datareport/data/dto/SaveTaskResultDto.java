package net.zdsoft.datareport.data.dto;

import java.util.List;

import net.zdsoft.datareport.data.entity.DataReportResults;

public class SaveTaskResultDto {
	
	private String taskId;
	private String reportId;
	private String unitId;
	private String ownerId;
	private Integer tableType;
	private List<DataReportResults> dataReportResults;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public List<DataReportResults> getDataReportResults() {
		return dataReportResults;
	}
	public void setDataReportResults(List<DataReportResults> dataReportResults) {
		this.dataReportResults = dataReportResults;
	}
	public Integer getTableType() {
		return tableType;
	}
	public void setTableType(Integer tableType) {
		this.tableType = tableType;
	}
	
}
