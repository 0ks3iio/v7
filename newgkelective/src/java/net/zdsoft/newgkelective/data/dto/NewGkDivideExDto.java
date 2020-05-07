package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.newgkelective.data.entity.NewGkDivideEx;

public class NewGkDivideExDto {
	private String groupType;//文理 语数英  groupType
	private String allgroupName;
	//key subjectType
	private Map<String,NewGkDivideEx> exMap=new HashMap<String,NewGkDivideEx>();
	private List<NewGkDivideEx> exList=new ArrayList<NewGkDivideEx>();
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getAllgroupName() {
		return allgroupName;
	}
	public void setAllgroupName(String allgroupName) {
		this.allgroupName = allgroupName;
	}
	public Map<String, NewGkDivideEx> getExMap() {
		return exMap;
	}
	public void setExMap(Map<String, NewGkDivideEx> exMap) {
		this.exMap = exMap;
	}
	public List<NewGkDivideEx> getExList() {
		return exList;
	}
	public void setExList(List<NewGkDivideEx> exList) {
		this.exList = exList;
	}
	

}
