package net.zdsoft.bigdata.dataimport.entity;

import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class ExcelTemplate4NoAnn extends BaseRowModel implements Serializable {

	private static final long serialVersionUID = 2385745176943715500L;
	@ExcelProperty(index = 0)
	private int col1;
	@ExcelProperty(index = 1)
	private String col2;
	@ExcelProperty(index = 2)
	private String col3;
	public int getCol1() {
		return col1;
	}
	public void setCol1(int col1) {
		this.col1 = col1;
	}
	public String getCol2() {
		return col2;
	}
	public void setCol2(String col2) {
		this.col2 = col2;
	}
	public String getCol3() {
		return col3;
	}
	public void setCol3(String col3) {
		this.col3 = col3;
	}
	
}
