package net.zdsoft.scoremanage.data.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ChartsDetailDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String documentLabel;//图表唯一性
	private String urlFtl;//返回的ftl地址
	private Map<String,ChartsDetailParDto> parMap = new HashMap<String,ChartsDetailParDto>();//参数 key ChartsDetailParDto.parCode
	private Integer chartType;//1为BI，2为echarts
	
	public Integer getChartType() {
		return chartType;
	}
	public void setChartType(Integer chartType) {
		this.chartType = chartType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDocumentLabel() {
		return documentLabel;
	}
	public void setDocumentLabel(String documentLabel) {
		this.documentLabel = documentLabel;
	}
	public String getUrlFtl() {
		return urlFtl;
	}
	public void setUrlFtl(String urlFtl) {
		this.urlFtl = urlFtl;
	}
	public Map<String, ChartsDetailParDto> getParMap() {
		return parMap;
	}
	public void setParMap(Map<String, ChartsDetailParDto> parMap) {
		this.parMap = parMap;
	}
	
	
}
