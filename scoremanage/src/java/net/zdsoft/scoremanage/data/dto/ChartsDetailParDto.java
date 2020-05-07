package net.zdsoft.scoremanage.data.dto;

import java.io.Serializable;

public class ChartsDetailParDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String parName;//参数名称
	private String parCode;//参数代码，对应数据库字段名称
	private Integer isAssistPar;//是否为辅助参数:即不做为实际传递的参数，只为筛选最终条件
	private Integer isMust;//是否必须
	private Integer isMultiple;//是否可多选
	
	public String getParName() {
		return parName;
	}
	public void setParName(String parName) {
		this.parName = parName;
	}
	public Integer getIsMust() {
		return isMust;
	}
	public void setIsMust(Integer isMust) {
		this.isMust = isMust;
	}
	public Integer getIsMultiple() {
		return isMultiple;
	}
	public void setIsMultiple(Integer isMultiple) {
		this.isMultiple = isMultiple;
	}
	public String getParCode() {
		return parCode;
	}
	public void setParCode(String parCode) {
		this.parCode = parCode;
	}
	public Integer getIsAssistPar() {
		return isAssistPar;
	}
	public void setIsAssistPar(Integer isAssistPar) {
		this.isAssistPar = isAssistPar;
	}
	
}
