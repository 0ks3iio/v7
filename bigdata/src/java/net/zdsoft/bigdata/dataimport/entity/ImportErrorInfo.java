package net.zdsoft.bigdata.dataimport.entity;

import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class ImportErrorInfo extends BaseRowModel implements Serializable {

	private static final long serialVersionUID = 2385745176943715500L;

	@ExcelProperty(value = { "行号" }, index = 0)
	private int rowNum;
	@ExcelProperty(value = "原始数据", index = 1)
	private String originalData;
	@ExcelProperty(value = "错误信息", index = 2)
	private String errorMsg;

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public String getOriginalData() {
		return originalData;
	}

	public void setOriginalData(String originalData) {
		this.originalData = originalData;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
