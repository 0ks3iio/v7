package net.zdsoft.bigdata.dataimport.entity;

import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class QualityResultDetailInfo extends BaseRowModel implements Serializable {

	private static final long serialVersionUID = 2385745176943715500L;

	@ExcelProperty(value = { "表名" }, index = 0)
	private String tableName;
	@ExcelProperty(value = "字段名", index = 1)
	private String columnName;
	@ExcelProperty(value = "结果", index = 2)
	private String result;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
