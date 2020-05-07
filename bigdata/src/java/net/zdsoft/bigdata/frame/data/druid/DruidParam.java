package net.zdsoft.bigdata.frame.data.druid;

import java.io.Serializable;
import java.util.List;

import net.zdsoft.framework.entity.Json;

public class DruidParam implements Serializable {

	private static final long serialVersionUID = -4906097198334515774L;

	private String queryType;
	
	private String dataSource;
	
	private String granularity;
	
	private String descending;
	
	private String metric;
	
	private String threshold;
	
	private String dimension;
	
	private List<String> dimensions;
	
	private Json filter;//格式不固定，故用Json格式
	
	private List<DruidAggregationParam> aggregations;
	
	private List<DruidPostAggregationParam> postAggregations;
	
	private DruidHavingParam having;
	
	private DruidLimitSpecParam limitSpec;
	
	private List<String> intervals;

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getGranularity() {
		return granularity;
	}

	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}

	public String getDescending() {
		return descending;
	}

	public void setDescending(String descending) {
		this.descending = descending;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public List<String> getDimensions() {
		return dimensions;
	}

	public void setDimensions(List<String> dimensions) {
		this.dimensions = dimensions;
	}

	public Json getFilter() {
		return filter;
	}

	public void setFilter(Json filter) {
		this.filter = filter;
	}

	public List<DruidAggregationParam> getAggregations() {
		return aggregations;
	}

	public void setAggregations(List<DruidAggregationParam> aggregations) {
		this.aggregations = aggregations;
	}

	public List<DruidPostAggregationParam> getPostAggregations() {
		return postAggregations;
	}

	public void setPostAggregations(List<DruidPostAggregationParam> postAggregations) {
		this.postAggregations = postAggregations;
	}

	public DruidHavingParam getHaving() {
		return having;
	}

	public void setHaving(DruidHavingParam having) {
		this.having = having;
	}

	public DruidLimitSpecParam getLimitSpec() {
		return limitSpec;
	}

	public void setLimitSpec(DruidLimitSpecParam limitSpec) {
		this.limitSpec = limitSpec;
	}

	public List<String> getIntervals() {
		return intervals;
	}

	public void setIntervals(List<String> intervals) {
		this.intervals = intervals;
	}
	
}
