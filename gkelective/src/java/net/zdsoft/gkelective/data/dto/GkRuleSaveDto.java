package net.zdsoft.gkelective.data.dto;

import java.util.List;

import net.zdsoft.gkelective.data.entity.GkAllocation;

public class GkRuleSaveDto {
	
	public List<GkAllocation> allocationList;

	public List<GkAllocation> getAllocationList() {
		return allocationList;
	}

	public void setAllocationList(List<GkAllocation> allocationList) {
		this.allocationList = allocationList;
	}
}
