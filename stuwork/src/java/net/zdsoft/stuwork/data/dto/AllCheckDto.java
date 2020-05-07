package net.zdsoft.stuwork.data.dto;

import java.util.HashMap;
import java.util.Map;

import net.zdsoft.stuwork.data.entity.DyAllCheck;

public class AllCheckDto {
	
	private String weekStr;
	
	private String name;
	
	Map<String,DyAllCheck> inMap=new HashMap<String,DyAllCheck>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWeekStr() {
		return weekStr;
	}

	public void setWeekStr(String weekStr) {
		this.weekStr = weekStr;
	}

	public Map<String, DyAllCheck> getInMap() {
		return inMap;
	}

	public void setInMap(Map<String, DyAllCheck> inMap) {
		this.inMap = inMap;
	}


	
}
