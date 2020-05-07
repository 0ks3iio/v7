package net.zdsoft.newgkelective.data.dto;

import java.util.List;

import net.zdsoft.basedata.entity.Unit;

public class NewGkUnitMake {
	Unit unit;
	boolean isReport;
	List<NewGkUnitMake> childUnitList;
	boolean isEdu;
	int index=0;//层次
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public boolean isReport() {
		return isReport;
	}
	public void setReport(boolean isReport) {
		this.isReport = isReport;
	}
	public List<NewGkUnitMake> getChildUnitList() {
		return childUnitList;
	}
	public void setChildUnitList(List<NewGkUnitMake> childUnitList) {
		this.childUnitList = childUnitList;
	}
	public boolean isEdu() {
		return isEdu;
	}
	public void setEdu(boolean isEdu) {
		this.isEdu = isEdu;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
}
