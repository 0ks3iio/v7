package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkReportDivide;

public class ReportDivideSaveDto {
	
	List<NewGkReportDivide> divideList = new ArrayList<NewGkReportDivide>();

	public List<NewGkReportDivide> getDivideList() {
		return divideList;
	}

	public void setDivideList(List<NewGkReportDivide> divideList) {
		this.divideList = divideList;
	}
	

}
