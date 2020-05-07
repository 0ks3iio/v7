package net.zdsoft.api.dataset.vo;

import java.util.List;

public class DataSetRuleDto {

	private String value;
	private String id;
	private String paramType;
	private List<RuleXX>  ruleXXs;
	private String relationName;   // 且  或 
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public List<RuleXX> getRuleXXs() {
		return ruleXXs;
	}
	public void setRuleXXs(List<RuleXX> ruleXXs) {
		this.ruleXXs = ruleXXs;
	}
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}



	public static class RuleXX {
		private String columnName;
		private String rule;
		private String ruleValue;
		public String getColumnName() {
			return columnName;
		}
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
		public String getRule() {
			return rule;
		}
		public void setRule(String rule) {
			this.rule = rule;
		}
		public String getRuleValue() {
			return ruleValue;
		}
		public void setRuleValue(String ruleValue) {
			this.ruleValue = ruleValue;
		}
	}
}
