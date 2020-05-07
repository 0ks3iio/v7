package net.zdsoft.datareport.data.dto;

import java.util.List;

import net.zdsoft.datareport.data.entity.DataReportColumn;
import net.zdsoft.datareport.data.entity.DataReportInfo;

public class DataReportSaveDto {
	
	private String objectIds; 
	private Integer objectType;
	private String header;
	private String remark;
	private DataReportInfo dataReportInfo;
	private List<DataReportColumn> rowColumns;
	private List<DataReportColumn> rankColumns;
	
	public String getObjectIds() {
		return objectIds;
	}
	public void setObjectIds(String objectIds) {
		this.objectIds = objectIds;
	}
	public Integer getObjectType() {
		return objectType;
	}
	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public DataReportInfo getDataReportInfo() {
		return dataReportInfo;
	}
	public void setDataReportInfo(DataReportInfo dataReportInfo) {
		this.dataReportInfo = dataReportInfo;
	}
	public List<DataReportColumn> getRowColumns() {
		return rowColumns;
	}
	public void setRowColumns(List<DataReportColumn> rowColumns) {
		this.rowColumns = rowColumns;
	}
	public List<DataReportColumn> getRankColumns() {
		return rankColumns;
	}
	public void setRankColumns(List<DataReportColumn> rankColumns) {
		this.rankColumns = rankColumns;
	}
	
}
